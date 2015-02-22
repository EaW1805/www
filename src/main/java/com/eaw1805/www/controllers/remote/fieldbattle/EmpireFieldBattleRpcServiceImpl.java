package com.eaw1805.www.controllers.remote.fieldbattle;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.eaw1805.battles.BattleField;
import com.eaw1805.battles.field.generation.MapBuilder;
import com.eaw1805.data.model.battles.field.FieldBattleHalfRoundStatistics;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.collections.FieldData;
import com.eaw1805.data.dto.converters.*;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.dto.web.field.FieldBattleHalfRoundStatisticsDTO;
import com.eaw1805.data.dto.web.field.FieldBattleMapDTO;
import com.eaw1805.data.dto.web.field.FieldBattleOrderDTO;
import com.eaw1805.data.managers.beans.*;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.army.Battalion;
import com.eaw1805.data.model.army.Brigade;
import com.eaw1805.data.model.army.Commander;
import com.eaw1805.data.model.battles.FieldBattleReport;
import com.eaw1805.data.model.battles.field.*;
import com.eaw1805.data.model.map.Position;
import com.eaw1805.data.model.map.Sector;
import com.eaw1805.www.controllers.site.ArticleManager;
import com.eaw1805.www.fieldbattle.asyncs.ArmyData;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcService;
import com.eaw1805.www.fieldbattle.stores.dto.SocialGame;
import com.eaw1805.www.fieldbattle.stores.dto.SocialPosition;
import com.eaw1805.www.fieldbattle.stores.dto.SocialSettings;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static java.nio.file.StandardCopyOption.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * RPC implementation used by fieldbattle gwt module
 * to perform queries with the server.
 */
public class EmpireFieldBattleRpcServiceImpl
        extends RemoteServiceServlet
        implements EmpireFieldBattleRpcService, RegionConstants {


    /**
     * Picks up a position for the current user for a fieldbattle game.
     *
     * @param scenarioId The scenario id.
     * @param battleId The field battle id.
     * @param nationId The wanted nation id.
     * @return A status of success.
     */
    public int pickupPosition(final int scenarioId, final int battleId, final int nationId) {
        final User currentUser = getUser();
        return pickupPosition(scenarioId, battleId, nationId, currentUser.getUserId());

    }

    public int pickupPosition(final int scenarioId, final int battleId, final int nationId, final int userId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final UserFieldBattle userField = userFieldManager.getByBattleNation(battleId, nationId);
        if (userField.getUserId() != 2) {
            return 0;
        }
        //check if user already has position in this game
        final UserFieldBattle userField2 = userFieldManager.getByBattleUser(battleId, userId);
        if (userField2 != null) {
            return -1;
        }
        userField.setUserId(userId);
        userField.setFacebookId(userManager.getByID(userId).getFacebookId());
        userFieldManager.update(userField);
        return 1;
    }

    /**
     * Create a new game from a base game.
     *
     * @param scenarioId The scenario id.
     * @param battleId The id of the base game.
     * @param socialGame The info retrieved from the client for the game (player positions).
     * @return A status for the success of the process.
     */
    public int createGame(final int scenarioId, final int battleId, final SocialGame socialGame) {
        ScenarioContextHolder.setScenario(scenarioId);
        final User user = getUser();
        try {
            final int newX = fieldBattleManager.list().size();
            FieldBattleReport scenarioBattle = fieldBattleManager.getByID(battleId);
            Sector scenarioSector = sectorManager.getByPosition(scenarioBattle.getPosition());
            final Set<Nation> side1Nation = new HashSet<Nation>();
            final Set<Nation> side2Nation = new HashSet<Nation>();
            for (Nation nat : scenarioBattle.getSide1()) {
                side1Nation.add(nat);
            }
            for (Nation nat : scenarioBattle.getSide2()) {
                side2Nation.add(nat);
            }
//
            final Position dummyPosition = new Position();
            dummyPosition.setGame(null);
            dummyPosition.setRegion(regionManager.getByID(EUROPE));
            dummyPosition.setX(newX);
            dummyPosition.setY(0);
//
            final Sector dummySector = new Sector();
            dummySector.setNation(side1Nation.iterator().next());
            dummySector.setTerrain(scenarioSector.getTerrain());
            dummySector.setProductionSite(scenarioSector.getProductionSite());
            dummySector.setPosition((Position) dummyPosition.clone());
            dummySector.setPopulation(scenarioSector.getPopulation());
            dummySector.setName("fieldbattle sector");
            sectorManager.add(dummySector);
//
            final FieldBattleReport battle = new FieldBattleReport();
            battle.setName(scenarioBattle.getName());
            battle.setOwnerId(user.getUserId());
            battle.setWinner(-1);
            battle.setPosition((Position) dummyPosition.clone());
            battle.setRound(-1);
            final Set<Nation> side1Nations = new HashSet<Nation>();
            side1Nations.addAll(side1Nation);
            battle.setSide1(side1Nations);
            final Set<Nation> side2Nations = new HashSet<Nation>();
            side2Nations.addAll(side2Nation);
            battle.setSide2(side2Nations);
            fieldBattleManager.add(battle);
//
//            // copy commanders for side 1
            for (final Nation nat : side1Nation) {
                for (Commander comm : commanderManager.listGameNation(null, nat)) {
                    final Commander newComm = cloneCommander(comm, dummyPosition);
                    commanderManager.add(newComm);
                }
            }
//
            // copy commanders for side 2
            for (final Nation nat : side2Nation) {
                for (Commander comm : commanderManager.listGameNation(null, nat)) {
                    final Commander newComm = cloneCommander(comm, dummyPosition);
                    commanderManager.add(newComm);
                }
            }
//
            int newBattleId = battle.getBattleId();
            System.out.println("battle? " + scenarioBattle.getPosition().toString());
            for (Nation nat : side1Nation) {
                final List<Brigade> originalSide1Brigades = brigadeManager.listByGameNationPosition(null, nat, scenarioBattle.getPosition());
                System.out.println("Brigades for nation 1 : " + nat.getName() + " - " + originalSide1Brigades.size());
                for (Brigade brigade : originalSide1Brigades) {
                    final Brigade newBrig = cloneBrigade(brigade, dummyPosition);
                    brigadeManager.add(newBrig);
                }
            }

            for (Nation nat : side2Nation) {
                final List<Brigade> originalSide2Brigades = brigadeManager.listByGameNationPosition(null, nat, scenarioBattle.getPosition());
                System.out.println("Brigades for nation 2 : " + nat.getName() + " - " + originalSide2Brigades.size());
                for (Brigade brigade : originalSide2Brigades) {
                    final Brigade newBrig = cloneBrigade(brigade, dummyPosition);
                    brigadeManager.add(newBrig);
                }
            }

            final FieldBattleMap scenarioMap = fieldMapManager.getByBattleID(battleId);

            final FieldBattleMap map = new FieldBattleMap();
            map.setSectorsSet(new LinkedHashSet<FieldBattleSector>());
            map.setSizeX(scenarioMap.getSizeX());
            map.setSizeY(scenarioMap.getSizeY());
            for (FieldBattleSector sector : scenarioMap.getSectorsSet()) {
                final FieldBattleSector copy = new FieldBattleSector();
                copy.setX(sector.getX());
                copy.setY(sector.getY());
                copy.setChateau(sector.getChateau());
                copy.setVillage(sector.getVillage());
                copy.setMinorRiver(sector.isMinorRiver());
                copy.setMajorRiver(sector.isMajorRiver());
                copy.setBridge(sector.getBridge());
                copy.setRoad(sector.isRoad());
                copy.setTown(sector.getTown());
                copy.setLake(sector.isLake());
                copy.setForest(sector.isForest());
                copy.setBush(sector.isBush());
                copy.setFortificationInterior(sector.isFortificationInterior());
                copy.setWall(sector.getWall());
                copy.setEntrenchment(sector.getEntrenchment());
                copy.setMap(map);
                copy.setNation(sector.getNation());
                copy.setAltitude(sector.getAltitude());
                copy.setStrategicPoint(sector.isStrategicPoint());
                copy.setCurrentHolder(sector.getCurrentHolder());
                map.getSectorsSet().add(copy);

            }
            map.setBattleId(newBattleId);

            fieldMapManager.add(map);
            //copy of  scenario map to newly created game.
            try {
                Files.copy(new File("/srv/eaw1805/images/fieldmaps/s" + scenarioId + "/m" + scenarioBattle.getBattleId() + ".jpg").toPath(), new File("/srv/eaw1805/images/fieldmaps/s" + scenarioId + "/m" + newBattleId + ".jpg").toPath(), REPLACE_EXISTING);
                Files.copy(new File("/srv/eaw1805/images/fieldmaps/s" + scenarioId + "/mm" + scenarioBattle.getBattleId() + ".jpg").toPath(), new File("/srv/eaw1805/images/fieldmaps/s" + scenarioId + "/mm" + newBattleId + ".jpg").toPath(), REPLACE_EXISTING);
//
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (final Nation nation : battle.getSide1()) {
                final UserFieldBattle userField = new UserFieldBattle();
                userField.setReady(false);
                userField.setUserId(2);//admin id
                for (SocialPosition pos : socialGame.getSide1()) {
                    if (pos.getNation().getNationId() == nation.getId()) {
                        userField.setFacebookId(pos.getFacebookId());
                        if (user.getFacebookId().equals(pos.getFacebookId())) {
                            userField.setUserId(user.getUserId());
                        }
                        break;
                    }
                }
                userField.setBattleId(newBattleId);
                userField.setNationId(nation.getId());
                userFieldManager.add(userField);
            }
//
            for (final Nation nation : battle.getSide2()) {
                final UserFieldBattle userField = new UserFieldBattle();
                userField.setReady(false);
                userField.setUserId(2);//admin id
                for (SocialPosition pos : socialGame.getSide2()) {
                    if (pos.getNation().getNationId() == nation.getId()) {
                        userField.setFacebookId(pos.getFacebookId());
                        if (user.getFacebookId().equals(pos.getFacebookId())) {
                            userField.setUserId(user.getUserId());
                        }
                        break;
                    }
                }
                userField.setBattleId(newBattleId);
                userField.setNationId(nation.getId());
                userFieldManager.add(userField);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    private Commander cloneCommander(final Commander original, final Position pos) {
        final Commander clone = new Commander();
        clone.setPosition((Position) pos.clone());
        clone.setArmy(0);
        clone.setCorp(0);
        clone.setArtilleryLeader(original.getArtilleryLeader());
        clone.setCarrierInfo(original.getCarrierInfo());
        clone.setCavalryLeader(original.getCavalryLeader());
        clone.setComc(original.getComc());
        clone.setFearlessAttacker(original.getFearlessAttacker());
        clone.setIntId(original.getIntId());
        clone.setMps(original.getMps());
        clone.setName(original.getName());
        clone.setNation(original.getNation());
        clone.setLegendaryCommander(original.getLegendaryCommander());
        clone.setRank(original.getRank());
        clone.setStrc(original.getStrc());
        clone.setStoutDefender(original.getStoutDefender());
        clone.setSupreme(original.getSupreme());
        clone.setDead(false);
        clone.setSick(0);
        clone.setPool(false);
        clone.setTransit(original.getTransit());
        clone.setCaptured(original.getCaptured());
        return clone;
    }

    private FieldBattlePosition createPosition() {
        final FieldBattlePosition out = new FieldBattlePosition();
        out.setY(0);
        out.setY(0);
        out.setPlaced(false);
        return out;
    }

    private Brigade cloneBrigade(Brigade original, final Position pos) {
        final Brigade clone = new Brigade();
        clone.setFieldBattlePosition(original.getFieldBattlePosition());
        clone.setPosition((Position) pos.clone());
        clone.setFieldBattlePosition(createPosition());
        clone.setName(original.getName());
        clone.setMps(original.getMps());
        clone.setCorp(original.getCorp());
        clone.setNation(original.getNation());
        Set<Battalion> bats = new HashSet<Battalion>();
        for (Battalion battalion : original.getBattalions()) {
            bats.add(cloneBattalion(battalion, clone));
        }
        clone.setBattalions(bats);
        clone.setArmType(original.getArmType());
        clone.setFormation(original.getFormation());
        clone.setMoraleStatus(original.getMoraleStatus());
        clone.setFromInit(original.getFromInit());
        return clone;
    }

    private Battalion cloneBattalion(Battalion original, final Brigade brigade) {
        final Battalion clone = new Battalion();
        clone.setOrder(original.getOrder());
        clone.setBrigade(brigade);
        clone.setType(original.getType());
        clone.setExperience(original.getExperience());
        clone.setHeadcount(original.getHeadcount());
        clone.setFleeing(original.isFleeing());
        clone.setAttackedByCav(original.isAttackedByCav());
        clone.setExpIncByComm(original.getExpIncByComm());
        clone.setParticipated(original.isParticipated());
        clone.setUnloaded(original.getUnloaded());
        clone.setHasMoved(original.getHasMoved());
        clone.setHasEngagedBattle(original.getHasEngagedBattle());
        clone.setHasLost(original.getHasLost());
        clone.setNotSupplied(original.getNotSupplied());
        clone.setCarrierInfo(original.getCarrierInfo());
        return clone;
    }


    public List<FieldBattleHalfRoundStatisticsDTO> getFieldBattleReports(final int scenarioId, final int battleId) {
        ScenarioContextHolder.setScenario(scenarioId);

        final FieldBattleReport battle = fieldBattleManager.getByID(battleId);
        List<FieldBattleHalfRoundStatisticsDTO> out = new ArrayList<FieldBattleHalfRoundStatisticsDTO>();
        try {

            final ObjectInputStream objectIn = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(battle.getStats())));
            List<byte[]> bytes = (List<byte[]>) objectIn.readObject();
            for (byte[] thisRound : bytes) {
                final ObjectInputStream roundIn = new ObjectInputStream(new ByteArrayInputStream(thisRound));
                FieldBattleHalfRoundStatistics stat = (FieldBattleHalfRoundStatistics) roundIn.readObject();
                out.add(FieldBattleHalfRoundStatisticsConverter.convert(stat));
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return out;


    }


    /**
     * Get the field battle terrain
     *
     * @return A FieldBattleMapDTO object that represents the map terrain.
     */
    public FieldBattleMapDTO getMap(int scenarioId, int battleId) {
        ScenarioContextHolder.setScenario(scenarioId);

        final FieldBattleReport battle = fieldBattleManager.getByID(battleId);
        FieldBattleMap map = fieldMapManager.getByBattleID(battleId);
        if (map == null) {//if map doesn't exist... generate it...

            final List<Brigade> brigades1 = new ArrayList<Brigade>();
            for (Nation nation : battle.getSide1()) {
                brigades1.addAll(brigadeManager.listByPositionNation(battle.getPosition(), nation));
            }

            final List<Brigade> brigades2 = new ArrayList<Brigade>();
            for (Nation nation : battle.getSide1()) {
                brigades2.addAll(brigadeManager.listByPositionNation(battle.getPosition(), nation));
            }


            final List<List<Brigade>> sides = new ArrayList<List<Brigade>>();
            sides.add(brigades1);
            sides.add(brigades2);

            final BattleField field = new BattleField(sectorManager.getByPosition(battle.getPosition()));//34083
            for (Nation nation : battle.getSide1()) {
                field.addNation(0, nation);
            }
            for (Nation nation : battle.getSide2()) {
                field.addNation(1, nation);
            }

            final MapBuilder mapBuilder = new MapBuilder(field, sides);
            map = mapBuilder.buildMap();
            map.setBattleId(battleId);

            fieldMapManager.add(map);
        }

        //return the map.
        return FieldBattleMapConverter.convert(map);
    }

    /**
     * Retrieve all games that the player has to play and have not ended yet.
     *
     * @param scenarioId The scenario id.
     * @return A list of social games.
     */
    public List<SocialGame>getMyGames(final int scenarioId) {
        ScenarioContextHolder.setScenario(scenarioId);
        List<SocialGame> myGames = constructSocialGames(userFieldManager.listByUser(getUser().getUserId()));
        Iterator<SocialGame> iter = myGames.iterator();
        while (iter.hasNext()) {
            if (iter.next().isGameEnded()) {
                iter.remove();
            }
        }
        return myGames;
    }

    public String getGameStatus(final int scenarioId, final int battleId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final FieldBattleReport battle = fieldBattleManager.getByID(battleId);
        StringBuilder builder = new StringBuilder();
        final Calendar now = Calendar.getInstance();
        if ("Being processed".equals(battle.getStatus())) {
            builder.append("Processing...");

        } else if (battle.isScenarioBattle()) {
            builder.append("Scenario Battle");
        } else if (battle.getWinner() == 0) {
            builder.append("Winner: ");
            for (final Nation nation : battle.getSide1()) {
                builder.append("<img src='http://static.eaw1805.com/images/nations/nation-").append(nation.getId()).append("-120.png' style='width: 18px;'/>");
            }
        } else if (battle.getWinner() == 1) {
            builder.append("Winner: ");
            for (final Nation nation : battle.getSide2()) {
                builder.append("<img src='http://static.eaw1805.com/images/nations/nation-").append(nation.getId()).append("-120.png' style='width: 18px;'/>");
            }
        } else if (battle.isGameEnded()) {
            builder.append("Ended tie");
        } else if (battle.getNextProcessDate() != null && battle.getNextProcessDate().before(now.getTime()) && battle.getWinner() == -1) {
            builder.append("Scheduled...");
        } else if (battle.getRound() == -1) {
            builder.append("Round 0 " );
            final List<UserFieldBattle> usersField = userFieldManager.listByBattleId(battleId);
            boolean ready = false;
            for (final UserFieldBattle userField : usersField) {
                if (userField.isReady()) {
                    if (!ready) {
                        builder.append(", ready: ");
                        ready = true;
                    }
                    builder.append("<img src='http://static.eaw1805.com/images/nations/nation-").append(userField.getNationId()).append("-120.png' style='width: 18px;'/>");
                }
            }
        } else if (battle.getRound() == 11) {
            builder.append("Round " + battle.getRound());
            final List<UserFieldBattle> usersField = userFieldManager.listByBattleId(battleId);
            boolean ready = false;
            for (final UserFieldBattle userField : usersField) {
                if (userField.isReady()) {
                    if (!ready) {
                        builder.append(", ready: ");
                        ready = true;
                    }
                    builder.append("<img src='http://static.eaw1805.com/images/nations/nation-").append(userField.getNationId()).append("-120.png' style='width: 18px;'/>");
                }
            }

        } else {
            builder.append("Status: " + battle.getStatus());
        }
        return builder.toString();
    }

    /**
     * Retrieve pending games to this FACEBOOK user.
     *
     * @param scenarioId The scenario which we should search for pending games.
     * @return A List of pending games.
     */
    public List<SocialGame> getPendingGames(final int scenarioId) {
        ScenarioContextHolder.setScenario(scenarioId);
        return constructSocialGames(userFieldManager.listPending(getUser().getFacebookId()));

    }

    /**
     * Retrieve all games already created with available positions.
     *
     * @param scenarioId The scenario id.
     */
    public List<SocialGame> getAvailableGames(final int scenarioId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final User user = getUser();
        if (user == null) {
            return new ArrayList<SocialGame>();
        }
        List<SocialGame> out = constructSocialGames(userFieldManager.listByUser(2));
        Iterator<SocialGame> iter = out.iterator();
        while (iter.hasNext()) {
            SocialGame game = iter.next();
            if (game.isUserPlaying(user.getFacebookId())
                    || !game.hasAvailablePosition()) {
                iter.remove();
            }
        }
        return out;
    }

    public List<SocialGame> constructSocialGames(final List<UserFieldBattle> userFields) {
        Set<Integer> battleIds = new TreeSet<Integer>();

        for (UserFieldBattle battle : userFields) {
            battleIds.add(battle.getBattleId());
        }
        battleIds.remove(Integer.valueOf(-1));
        final List<SocialGame> out = new ArrayList<SocialGame>();
        for (final Integer battleId : battleIds) {
            final FieldBattleReport battle = fieldBattleManager.getByID(battleId);
            if (battle.isScenarioBattle()) {
                //if this is a scenario battle, don't process it here.
                continue;
            }
            SocialGame game = new SocialGame();
            game.setRound(battle.getRound());
            game.setWinner(battle.getWinner());
            game.setName(battle.getName());
            game.setId(battle.getBattleId());
            game.setSector(SectorConverter.convert(sectorManager.getByPosition(battle.getPosition())));
            game.setScenarioGame(battle.isScenarioBattle());
            SocialPosition pos;
            for (Nation nation : battle.getSide1()) {
                UserFieldBattle userField = userFieldManager.getByBattleNation(battle.getBattleId(), nation.getId());
                pos = new SocialPosition();
                pos.setPlayerId(userField.getUserId());
                pos.setFacebookId(userField.getFacebookId());
                pos.setNation(NationConverter.convert(nation));
                game.getSide1().add(pos);
            }
            for (Nation nation : battle.getSide2()) {
                UserFieldBattle userField = userFieldManager.getByBattleNation(battle.getBattleId(), nation.getId());
                pos = new SocialPosition();
                pos.setPlayerId(userField.getUserId());
                pos.setFacebookId(userField.getFacebookId());
                pos.setNation(NationConverter.convert(nation));
                game.getSide2().add(pos);
            }
            out.add(game);
        }
        return out;
    }








    /**
     * Retrieve all games that are scenario games.
     *
     * @param scenarioId The database scenario id to retrieve the data.
     * @return A list of social games.
     */
    public List<SocialGame> getScenarioGames(final int scenarioId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final List<SocialGame> out = new ArrayList<SocialGame>();
        final List<FieldBattleReport> battles = fieldBattleManager.listScenario();
        for (FieldBattleReport battle : battles) {

            final SocialGame game = new SocialGame();
            game.setName(battle.getName());
            game.setId(battle.getBattleId());
            game.setSector(SectorConverter.convert(sectorManager.getByPosition(battle.getPosition())));
            game.setScenarioGame(battle.isScenarioBattle());
            SocialPosition pos;
            for (final Nation nation : battle.getSide1()) {
                final UserFieldBattle userField = userFieldManager.getByBattleNation(battle.getBattleId(), nation.getId());
                pos = new SocialPosition();
                pos.setPlayerId(userField.getUserId());
                pos.setFacebookId(userManager.getByID(userField.getUserId()).getFacebookId());
                pos.setNation(NationConverter.convert(nation));
                game.getSide1().add(pos);
            }
            for (final Nation nation : battle.getSide2()) {
                final UserFieldBattle userField = userFieldManager.getByBattleNation(battle.getBattleId(), nation.getId());
                pos = new SocialPosition();
                pos.setPlayerId(userField.getUserId());
                pos.setFacebookId(userManager.getByID(userField.getUserId()).getFacebookId());
                pos.setNation(NationConverter.convert(nation));
                game.getSide2().add(pos);
            }
            out.add(game);

        }
        System.out.println("sending : " + out.size());
        return out;
    }


    /**
     * Get all commanders given the nation.
     *
     * @param battleId The battle id.
     * @param nationId The nation id.
     * @return List of the nations commanders.
     */
    public Map<Integer, List<CommanderDTO>> getCommandersByNation(final int scenarioId, final int battleId, final int nationId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final FieldBattleReport battle = fieldBattleManager.getByID(battleId);
        final Map<Integer, List<CommanderDTO>> out = new HashMap<Integer, List<CommanderDTO>>();
        for (Nation nation : battle.getSide1()) {
            out.put(nation.getId(), CommanderConverter.convert(commanderManager.listByPositionNation(battle.getPosition(), nation)));
        }
        for (Nation nation : battle.getSide2()) {
            out.put(nation.getId(), CommanderConverter.convert(commanderManager.listByPositionNation(battle.getPosition(), nation)));
        }
        return out;
    }

    private Order createOrder() {
        final Order out = new Order();

        out.setCheckpoint1(new FieldBattlePosition());
        out.setCheckpoint2(new FieldBattlePosition());
        out.setCheckpoint3(new FieldBattlePosition());
        out.setStrategicPoint1(new FieldBattlePosition());
        out.setStrategicPoint2(new FieldBattlePosition());
        out.setStrategicPoint3(new FieldBattlePosition());
        out.setCustomStrategicPoint1(new FieldBattlePosition());
        out.setCustomStrategicPoint2(new FieldBattlePosition());
        out.setCustomStrategicPoint3(new FieldBattlePosition());
        out.setTargetNations(new HashSet<Nation>());
        out.setFormation("COLUMN");
        //default values
        out.getCheckpoint1().setY(-1);
        out.getCheckpoint1().setX(-1);
        out.getCheckpoint2().setY(-1);
        out.getCheckpoint2().setX(-1);
        out.getCheckpoint3().setY(-1);
        out.getCheckpoint3().setX(-1);

        out.getStrategicPoint1().setY(-1);
        out.getStrategicPoint1().setX(-1);
        out.getStrategicPoint2().setY(-1);
        out.getStrategicPoint2().setX(-1);
        out.getStrategicPoint3().setY(-1);
        out.getStrategicPoint3().setX(-1);

        out.getCustomStrategicPoint1().setY(-1);
        out.getCustomStrategicPoint1().setX(-1);
        out.getCustomStrategicPoint2().setY(-1);
        out.getCustomStrategicPoint2().setX(-1);
        out.getCustomStrategicPoint3().setY(-1);
        out.getCustomStrategicPoint3().setX(-1);

        return out;
    }

    /**
     * Save brigades state, position and orders.
     *
     * @param brigades The list of brigades to save.
     * @return An ok status = 1.
     */
    public int saveBrigadesPositions(final int scenarioId, final int battleId, final int nationId, final List<BrigadeDTO> brigades, final boolean ready) {
        ScenarioContextHolder.setScenario(scenarioId);
        final UserFieldBattle userBattle = userFieldManager.getByBattleNation(battleId, nationId);
        if (userBattle.isReady()) {
            //if user is already ready, then he can't save again.
            return -1;
        }
        userBattle.setReady(ready);
        userFieldManager.update(userBattle);


        final FieldBattleReport battle = fieldBattleManager.getByID(battleId);
//        System.out.println("SAVING FOR BATTLE : " + battle.getBattleId());
        if (battle.getRound() > 0) {

            try {
                for (BrigadeDTO brigade : brigades) {
//                    System.out.println("Looking for brigade 2 ? " + brigade.getName() + ", " + brigade.getNationId());
                    if (brigade.isFieldBattleUpdateMiddleRound()) {
                        final Brigade dbBrigade = brigadeManager.getByID(brigade.getBrigadeId());
                        if (dbBrigade != null) {
                            if (brigade.getBasicOrder() != null) {
                                if (dbBrigade.getBasicOrder() == null) {
                                    dbBrigade.setBasicOrder(createOrder());
                                }
                                if (dbBrigade.getBasicOrder().getBackup() == null) {
                                    dbBrigade.getBasicOrder().setBackup(createOrder());
                                }
                                copyOrder(dbBrigade.getBasicOrder().getBackup(), dbBrigade.getBasicOrder());
                                updateOrderFromDTO(brigade.getBasicOrder(), dbBrigade.getBasicOrder());
                            }
                            if (brigade.getAdditionalOrder() != null) {
                                if (dbBrigade.getAdditionalOrder() == null) {
                                    dbBrigade.setAdditionalOrder(createOrder());
                                }

                                if (dbBrigade.getAdditionalOrder().getBackup() == null) {
                                    dbBrigade.getAdditionalOrder().setBackup(createOrder());
                                }
                                copyOrder(dbBrigade.getAdditionalOrder().getBackup(), dbBrigade.getAdditionalOrder());
                                updateOrderFromDTO(brigade.getAdditionalOrder(), dbBrigade.getAdditionalOrder());
                            }
                            brigadeManager.update(dbBrigade);
                        }
                    } else {
                        final Brigade dbBrigade = brigadeManager.getByID(brigade.getBrigadeId());
                        if (dbBrigade != null) {
                            if (dbBrigade.getBasicOrder() != null && dbBrigade.getBasicOrder().getBackup() != null) {
                                copyOrder(dbBrigade.getBasicOrder(), dbBrigade.getBasicOrder().getBackup());
                                dbBrigade.getBasicOrder().setBackup(null);
                            }
                            if (dbBrigade.getAdditionalOrder() != null && dbBrigade.getAdditionalOrder().getBackup() != null) {
                                copyOrder(dbBrigade.getAdditionalOrder(), dbBrigade.getAdditionalOrder().getBackup());
                                dbBrigade.getAdditionalOrder().setBackup(null);
                            }
                            brigadeManager.update(dbBrigade);
                        }
                    }
                }
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            try {
                for (BrigadeDTO brigade : brigades) {
//                    System.out.println("Looking for brigade ? " + brigade.getName() + ", " + brigade.getNationId());
                    final Brigade dbBrigade = brigadeManager.getByID(brigade.getBrigadeId());
                    if (dbBrigade != null) {
                        dbBrigade.getFieldBattlePosition().setX(brigade.getFieldBattleX());
                        dbBrigade.getFieldBattlePosition().setY(brigade.getFieldBattleY());
                        dbBrigade.getFieldBattlePosition().setPlaced(brigade.isPlacedOnFieldMap());
                        dbBrigade.setFieldBattleCommanderId(brigade.getFieldBattleCommanderId());
                        dbBrigade.setFieldBattleOverallCommanderId(brigade.getFieldBattleOverallCommanderId());
                        if (brigade.getBasicOrder() == null) {//then just add an empty order since the engine will expect to see something
                            if (brigade.isPlacedOnFieldMap()) {
//                                System.out.println("Creating empty basic order");
                                dbBrigade.setBasicOrder(createOrder());
                            } else {
//                                System.out.println("setting null basic order");
                                dbBrigade.setBasicOrder(null);
                            }

                        } else {
//                            System.out.println("creating new basic order");
                            if (dbBrigade.getBasicOrder() == null) {
                                dbBrigade.setBasicOrder(createOrder());
                            }
                            updateOrderFromDTO(brigade.getBasicOrder(), dbBrigade.getBasicOrder());
                        }
                        if (brigade.getAdditionalOrder() == null) {
                            if (brigade.isPlacedOnFieldMap()) {
//                                System.out.println("Creating empty additional order");
                                dbBrigade.setAdditionalOrder(createOrder());
                            } else {
                                dbBrigade.setAdditionalOrder(null);
                            }
                        } else {
                            if (dbBrigade.getAdditionalOrder() == null) {
                                dbBrigade.setAdditionalOrder(createOrder());
                            }
                            updateOrderFromDTO(brigade.getAdditionalOrder(), dbBrigade.getAdditionalOrder());
                        }
                        brigadeManager.update(dbBrigade);
                    }
                }

                final List<UserFieldBattle> battleUserFields = userFieldManager.listByBattleId(battleId);
                boolean allReady = true;
                for (UserFieldBattle pos : battleUserFields) {
                    if (!pos.isReady()) {
                        allReady = false;
                    }
                }
                if (allReady) {
                    if (battle.getWinner() == -1
                            && !"Being processed".equalsIgnoreCase(battle.getStatus())) {
                        Calendar now = Calendar.getInstance();
                        now.add(Calendar.DATE, -1);
                        battle.setNextProcessDate(now.getTime());
                        fieldBattleManager.update(battle);
                        //also schedule it to be processed

                        getArticleManager().getBuild(-103, 0);

                    }
                }

                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }


    }




    public void copyOrder(final Order order1, final Order order2) {
        order1.setOrderType(order2.getOrderType());
        order1.setActivationRound(order2.getActivationRound());
        order1.getCheckpoint1().setX(order2.getCheckpoint1().getX());
        order1.getCheckpoint1().setY(order2.getCheckpoint1().getY());
        order1.getCheckpoint2().setX(order2.getCheckpoint2().getX());
        order1.getCheckpoint2().setY(order2.getCheckpoint2().getY());
        order1.getCheckpoint3().setX(order2.getCheckpoint3().getX());
        order1.getCheckpoint3().setY(order2.getCheckpoint3().getY());
        order1.setConstructionCounter(order2.getConstructionCounter());
        order1.getCustomStrategicPoint1().setX(order2.getCustomStrategicPoint1().getX());
        order1.getCustomStrategicPoint1().setY(order2.getCustomStrategicPoint1().getY());
        order1.getCustomStrategicPoint2().setX(order2.getCustomStrategicPoint2().getX());
        order1.getCustomStrategicPoint2().setY(order2.getCustomStrategicPoint2().getY());
        order1.getCustomStrategicPoint3().setX(order2.getCustomStrategicPoint3().getX());
        order1.getCustomStrategicPoint3().setY(order2.getCustomStrategicPoint3().getY());
        order1.setEnemySideCapturedOwnStrategicPoint(order2.isEnemySideCapturedOwnStrategicPoint());
        order1.setFormation(order2.getFormation());
        order1.setHeadCountThreshold(order2.getHeadCountThreshold());
        order1.setLastDestinationReached(order2.isLastDestinationReached());
        order1.setMaintainDistance(order2.getMaintainDistance());
        order1.setReachedCheckpoint1(order2.isReachedCheckpoint1());
        order1.setReachedCheckpoint2(order2.isReachedCheckpoint2());
        order1.setReachedCheckpoint3(order2.isReachedCheckpoint3());
        order1.setOwnSideCapturedEnemyStrategicPoint(order2.isOwnSideCapturedEnemyStrategicPoint());
        order1.getStrategicPoint1().setX(order2.getStrategicPoint1().getX());
        order1.getStrategicPoint1().setY(order2.getStrategicPoint1().getY());
        order1.getStrategicPoint2().setX(order2.getStrategicPoint2().getX());
        order1.getStrategicPoint2().setY(order2.getStrategicPoint2().getY());
        order1.getStrategicPoint3().setX(order2.getStrategicPoint3().getX());
        order1.getStrategicPoint3().setY(order2.getStrategicPoint3().getY());
        order1.setTargetArm(order2.getTargetArm());
        order1.setTargetClosestInRange(order2.isTargetClosestInRange());
        order1.setTargetFormation(order2.getTargetFormation());
        order1.setTargetHighestHeadcount(order2.isTargetHighestHeadcount());
        order1.setTargetNations(order2.getTargetNations());
        order1.setDetachmentLeaderId(order2.getDetachmentLeaderId());
        order1.setDetachmentPosition(order2.getDetachmentPosition());
    }

    public void updateOrderFromDTO(final FieldBattleOrderDTO orderDTO, final Order order) {
        if (orderDTO.getOrderType() != null && "SELECT_AN_ORDER".equals(orderDTO.getOrderType())) {
            order.setOrderType(null);
        } else {
            order.setOrderType(orderDTO.getOrderType());
        }
        order.getCheckpoint1().setX(orderDTO.getCheckPoint1().getX());
        order.getCheckpoint1().setY(orderDTO.getCheckPoint1().getY());
        order.getCheckpoint2().setX(orderDTO.getCheckPoint2().getX());
        order.getCheckpoint2().setY(orderDTO.getCheckPoint2().getY());
        order.getCheckpoint3().setX(orderDTO.getCheckPoint3().getX());
        order.getCheckpoint3().setY(orderDTO.getCheckPoint3().getY());
        if (orderDTO.getFormation() != null && "ALL".equals(orderDTO.getFormation())) {
            order.setFormation(null);
        } else {
            order.setFormation(orderDTO.getFormation());
        }
        order.setMaintainDistance(orderDTO.getMaintainDistance());
        order.getStrategicPoint1().setX(orderDTO.getStrategicPoint1().getX());
        order.getStrategicPoint1().setY(orderDTO.getStrategicPoint1().getY());
        order.getStrategicPoint2().setX(orderDTO.getStrategicPoint2().getX());
        order.getStrategicPoint2().setY(orderDTO.getStrategicPoint2().getY());
        order.getStrategicPoint3().setX(orderDTO.getStrategicPoint3().getX());
        order.getStrategicPoint3().setY(orderDTO.getStrategicPoint3().getY());

        if (orderDTO.getTargetArm() != null && "ALL".equals(orderDTO.getTargetArm())) {
            order.setTargetArm(null);
        } else {
            order.setTargetArm(orderDTO.getTargetArm());
        }
        if (orderDTO.getTargetFormation() != null && "ALL".equals(orderDTO.getTargetFormation())) {
            order.setTargetFormation(null);
        } else {
            order.setTargetFormation(orderDTO.getTargetFormation());
        }

        order.setTargetHighestHeadcount(orderDTO.isTargetHighestHeadcount());
        order.setTargetClosestInRange(orderDTO.isTargetClosestInRange());
        order.setActivationRound(orderDTO.getActivationRound());
        order.setHeadCountThreshold(orderDTO.getHeadCountThreshold());
        order.setLastDestinationReached(orderDTO.isLastDestinationReached());
        order.setOwnSideCapturedEnemyStrategicPoint(orderDTO.isOwnSideCapturedEnemyStrategicPoint());
        order.setEnemySideCapturedOwnStrategicPoint(orderDTO.isEnemySideCapturedOwnStrategicPoint());
        order.getCustomStrategicPoint1().setX(orderDTO.getCustomStrategicPoint1().getX());
        order.getCustomStrategicPoint1().setY(orderDTO.getCustomStrategicPoint1().getY());
        order.getCustomStrategicPoint2().setX(orderDTO.getCustomStrategicPoint2().getX());
        order.getCustomStrategicPoint2().setY(orderDTO.getCustomStrategicPoint2().getY());
        order.getCustomStrategicPoint3().setX(orderDTO.getCustomStrategicPoint3().getX());
        order.getCustomStrategicPoint3().setY(orderDTO.getCustomStrategicPoint3().getY());
        order.setDetachmentLeaderId(orderDTO.getLeaderId());
        order.setDetachmentPosition(orderDTO.getDetachmentPosition());
        //update also the nations set
        order.getTargetNations().clear();
//        ordersManager.add(order);
//        for (Integer nationId : orderDTO.getTargetNations()) {
//            System.out.println("Nation? " + nationId);
//            System.out.println("Nation? " + nationManager.getByID(nationId).getName());
//            order.getTargetNations().add(nationManager.getByID(nationId));
//        }
//        ordersManager.update(order);

//        ordersManager.add(order);
    }

    public SocialSettings loadGame(final int scenarioId, final int battleId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final User user = getUser();
        UserFieldBattle userField = userFieldManager.getByBattleUser(battleId, user.getUserId());
        if (userField == null) {
            return null;
        }
        FieldBattleReport battle = fieldBattleManager.getByID(userField.getBattleId());
        final SocialSettings settings = new SocialSettings();

        //that is a default for the field battle
        settings.setScenarioId(1800);
        settings.setBattleId(battleId);
        settings.setNationId(userField.getNationId());
        settings.setRound(battle.getRound());
        int side = 1;

        for (Nation nation : battle.getSide2()) {
            if (userField.getNationId() == nation.getId()) {
                side = 2;
                break;
            }
        }
        settings.setSide(side);
        settings.setGameEnded(battle.getWinner() != -1 || battle.getRound() == 23);
        settings.setStandAlone(true);
        settings.setWinner(battle.getWinner());
        settings.setSideReady(userField.isReady());
        settings.setFacebookId(user.getFacebookId());
        settings.setTitle("Field battle at " + sectorManager.getByPosition(battle.getPosition()).getPosition().toString());

        return settings;
    }


    public FieldData getNations(final int scenarioId, final int battleId, final int nationId) {
        System.out.println("Scenario id? " +scenarioId);
        ScenarioContextHolder.setScenario(scenarioId);
        final FieldBattleReport battle = fieldBattleManager.getByID(battleId);

        FieldData data = new FieldData();
        data.setAllNations(NationConverter.convert(nationManager.list()));
        //check if nation belongs to the first side.
        for (Nation nation : battle.getSide1()) {
            if (nation.getId() == nationId) {
                data.setAlliedNations(NationConverter.convert(new ArrayList<Nation>(battle.getSide1())));
                data.setEnemyNations(NationConverter.convert(new ArrayList<Nation>(battle.getSide2())));
                break;
            }
        }
        //check if nation belongs to the second side.
        for (Nation nation : battle.getSide2()) {
            if (nation.getId() == nationId) {
                data.setAlliedNations(NationConverter.convert(new ArrayList<Nation>(battle.getSide2())));
                data.setEnemyNations(NationConverter.convert(new ArrayList<Nation>(battle.getSide1())));
                break;
            }
        }

        return data;

    }

    public ArmyData getArmyData(final int scenarioId, final int battleId, final int nationId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final FieldBattleReport battle = fieldBattleManager.getByID(battleId);
        final Nation thisNation = nationManager.getByID(nationId);

        //retrieve data from database.
        final List<ArmyDTO> armies = ArmyConverter.convert(armyManager.listGameNation(battle.getPosition().getGame(), thisNation));
        final List<CorpDTO> corps = CorpConverter.convert(corpManager.listGameNation(battle.getPosition().getGame(), thisNation));

        //prepare data for client.
        final Map<Integer, ArmyDTO> armiesMap = new HashMap<Integer, ArmyDTO>();
        final Map<Integer, CorpDTO> corpsMap = new HashMap<Integer, CorpDTO>();

        //add armies
        for (ArmyDTO army : armies) {
            armiesMap.put(army.getArmyId(), army);
        }
        //add zero army
        final ArmyDTO army = new ArmyDTO();
        army.setArmyId(0);
        army.setName("Free Corps");
        armiesMap.put(0, army);
        //add corps
        for (CorpDTO corp : corps) {
            corpsMap.put(corp.getCorpId(), corp);
        }
        //add zero corps
        final CorpDTO corp = new CorpDTO();
        corp.setArmyId(0);
        corp.setCorpId(0);
        corp.setName("Free Brigades");
        corpsMap.put(0, corp);
        return new ArmyData().setData(armiesMap, corpsMap);
    }

    /**
     * Getting all armies participating on the fieldbattle.
     *
     * @return A map containing all the armies, by side, nation, brigade id.
     */
    public Map<Boolean, Map<Integer, Map<Integer, BrigadeDTO>>> getArmies(final int scenarioId, final int battleId, final int nationId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Map<Boolean, Map<Integer, Map<Integer, BrigadeDTO>>> out = new HashMap<Boolean, Map<Integer, Map<Integer, BrigadeDTO>>>();

        //just build a test map for now.

        final FieldBattleReport battle = fieldBattleManager.getByID(battleId);
        int side = 0;
        for (Nation nation : battle.getSide1()) {
            if (nation.getId() == nationId) {
                side = 1;
                break;
            }
        }
        if (side == 0) {
            for (Nation nation : battle.getSide2()) {
                if (nation.getId() == nationId) {
                    side = 2;
                    break;
                }
            }
        }
        final List<BrigadeDTO> brigades2 = new ArrayList<BrigadeDTO>();
        final List<BrigadeDTO> brigades1 = new ArrayList<BrigadeDTO>();
        if (side == 1) {
            for (Nation nation : battle.getSide1()) {
                brigades1.addAll(BrigadeConverter.convert(brigadeManager.listByPositionNation(battle.getPosition(), nation)));
            }
            for (Nation nation : battle.getSide2()) {
                brigades2.addAll(BrigadeConverter.convert(brigadeManager.listByPositionNation(battle.getPosition(), nation)));
            }
        } else if (side == 2) {
            for (Nation nation : battle.getSide2()) {
                brigades1.addAll(BrigadeConverter.convert(brigadeManager.listByPositionNation(battle.getPosition(), nation)));
            }
            for (Nation nation : battle.getSide1()) {
                brigades2.addAll(BrigadeConverter.convert(brigadeManager.listByPositionNation(battle.getPosition(), nation)));
            }
        }


        out.put(true, new HashMap<Integer, Map<Integer, BrigadeDTO>>());
        out.put(false, new HashMap<Integer, Map<Integer, BrigadeDTO>>());

        for (BrigadeDTO brigade : brigades1) {
            if (!out.get(true).containsKey(brigade.getNationId())) {
                out.get(true).put(brigade.getNationId(), new HashMap<Integer, BrigadeDTO>());
            }
            out.get(true).get(brigade.getNationId()).put(brigade.getBrigadeId(), brigade);
        }
        for (BrigadeDTO brigade : brigades2) {
            if (!out.get(false).containsKey(brigade.getNationId())) {
                out.get(false).put(brigade.getNationId(), new HashMap<Integer, BrigadeDTO>());
            }
            out.get(false).get(brigade.getNationId()).put(brigade.getBrigadeId(), brigade);
        }
        return out;
    }


    /**
     * Retrieve the user object from the database.
     *
     * @return the User entity.
     */
    protected final User getUser() {
        User thisUser = new User();
        try {
            // Retrieve principal object
            final UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Lookup user based on username
            thisUser = userManager.getByUserName(principal.getUsername());

        } catch (Exception ex) {
            // do nothing
            thisUser.setUserId(-1);
            thisUser.setUsername("anonymous");
        }

        try {
            // Retrieve remote IP
            final String ipAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("CF-Connecting-IP");
            if (ipAddress == null) {
                thisUser.setRemoteAddress(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr());

            } else {
                thisUser.setRemoteAddress(ipAddress);
            }

        } catch (Exception ex) {
            // do nothing
            thisUser.setRemoteAddress("unknown");
        }

        return thisUser;
    }


    /**
     * SectorManagerBean object to perform queries with database for sectors.
     */
    @Autowired
    @Qualifier("sectorManagerBean")
    private transient SectorManagerBean sectorManager;

    /**
     * Instance of BrigadeManagerBean for transactions with the database.
     */
    @Autowired
    @Qualifier("brigadeManagerBean")
    private transient BrigadeManagerBean brigadeManager;

    /**
     * Instance of NationManagerBean for transactions with the database.
     */
    @Autowired
    @Qualifier("nationManagerBean")
    private transient NationManagerBean nationManager;

    /**
     * Instance of GameManagerBean for transactions with the database.
     */
    @Autowired
    @Qualifier("gameManagerBean")
    private transient GameManagerBean gameManager;

    /**
     * Instance of ordersManager for transactions with the database.
     */
    @Autowired
    @Qualifier("fieldBattleOrderManagerBean")
    private transient OrderManagerBean ordersManager;

    /**
     * Instance of CommanderManagerBean for transactions with the database.
     */
    @Autowired
    @Qualifier("commanderManagerBean")
    private transient CommanderManagerBean commanderManager;

    @Autowired
    @Qualifier("fieldBattleReportManagerBean")
    private transient FieldBattleReportManagerBean fieldBattleManager;


    @Autowired
    @Qualifier("fieldBattleMapManagerBean")
    private transient FieldBattleMapManagerBean fieldMapManager;

    @Autowired
    @Qualifier("userFieldBattleManagerBean")
    private transient UserFieldBattleManagerBean userFieldManager;

    /**
     * Instance of ArticleManager.
     */
    @Autowired
    @Qualifier("articleManagerBean")
    private transient ArticleManager articleManager;

    public ArticleManager getArticleManager() {
        return articleManager;
    }

    @Autowired
    @Qualifier("armyManagerBean")
    private transient ArmyManagerBean armyManager;

    @Autowired
    @Qualifier("corpManagerBean")
    private transient CorpManagerBean corpManager;

    @Autowired
    @Qualifier("userManagerBean")
    private transient UserManagerBean userManager;

    @Autowired
    @Qualifier("regionManagerBean")
    private transient RegionManagerBean regionManager;

    @Autowired
    @Qualifier("terrainManagerBean")
    private transient TerrainManagerBean terrainManager;
}
