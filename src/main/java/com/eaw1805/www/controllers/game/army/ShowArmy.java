package com.eaw1805.www.controllers.game.army;

import com.eaw1805.data.cache.GameCachable;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.managers.beans.ArmyManagerBean;
import com.eaw1805.data.managers.beans.BattalionManagerBean;
import com.eaw1805.data.managers.beans.BrigadeManagerBean;
import com.eaw1805.data.managers.beans.CommanderManagerBean;
import com.eaw1805.data.managers.beans.CorpManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.army.Army;
import com.eaw1805.data.model.army.Battalion;
import com.eaw1805.data.model.army.Brigade;
import com.eaw1805.data.model.army.Commander;
import com.eaw1805.data.model.army.Corp;
import com.eaw1805.data.model.map.Position;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Controller for user requests related to armed forces.
 */
@Controller
public class ShowArmy
        extends ExtendedController
        implements RegionConstants {

    private final static int INFANTRY = 1;
    private final static int ENGINEER = 2;
    private final static int CAVALRY = 3;
    private final static int ARTILLERY = 4;

    public final static int ARMY = 0;
    public final static int CORPS = 1;
    public final static int BRIGADE = 2;
    public final static int COMMANDER = 3;

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowArmy.class);

    @RequestMapping(method = RequestMethod.GET, value = "/report/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/army")
    protected ModelAndView handle(@PathVariable final String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  HttpServletRequest request)
            throws Exception {
        ScenarioContextHolder.defaultScenario();
        if (scenarioId == null || scenarioId.isEmpty()) {
            throw new InvalidPageException("Page not found");
        } else {
            try {
                ScenarioContextHolder.setScenario(scenarioId);
            } catch (Exception e) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Retrieve Game entity

        final Game thisGame = getGame(gameId);
        if (thisGame == null) {
            throw new InvalidPageException("Page not found");
        }

        // Retrieve Nation entity
        final Nation thisNation = getNation(nationId);
        if (thisNation == null) {
            throw new InvalidPageException("Page not found");
        }

        // Check that user is allowed to view nation
        final User thisUser = getUser();
        if (thisUser.getUserType() != 3 && getUserGameManager().listActive(thisUser, thisGame, thisNation).isEmpty()) {
            throw new InvalidPageException("Page not found");
        }

        // produce report + Prepare data to pass to jsp
        final Map<String, Object> refData = prepareReport(thisGame, thisNation);

        // Prepare data to pass to jsp
        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show army for game=" + gameId + "/nation=" + thisNation.getName());
        if (request.getParameter("eawClient") == null) {
            return new ModelAndView("game/army", refData);
        } else {
            return new ModelAndView("game/armyMinimal", refData);
        }
    }

    /**
     * Prepare land forces report.
     *
     * @param thisGame   the game to examine.
     * @param thisNation the nation to examine.
     * @return the map with the data that will be passed to the jsp.
     */
    @GameCachable()
    @SuppressWarnings("unchecked")
    public Map<String, Object> prepareReport(final Game thisGame,
                                             final Nation thisNation) {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // access Calendar object
        final Calendar thisCal = calendar(thisGame);
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuf.append(" ");
        strBuf.append(thisCal.get(Calendar.YEAR));

        // Keep track of battalions per sector so that we can sort positions by strength
        final Map<Position, Integer> mapBattSector = new HashMap<Position, Integer>();

        final Map<Position, List[]> mapUnitsSector = new HashMap<Position, List[]>();
        final Map<Position, Integer> positionHeight = new HashMap<Position, Integer>();

        // Retrieve Commanders
        final List<Commander> lstCommanders = commanderManager.listGameNation(thisGame, thisNation);
        final List<Commander> freeCommanders = new ArrayList<Commander>();
        for (final Commander commander : lstCommanders) {
            if (commander.getArmy() == 0 && commander.getCorp() == 0 && !commander.getDead()) {
                freeCommanders.add(commander);

                // Update list of sectors
                final List[] units;
                if (mapUnitsSector.containsKey(commander.getPosition())) {
                    // Retrieve items
                    units = mapUnitsSector.get(commander.getPosition());

                } else {
                    units = new List[COMMANDER + 1];
                    for (int index = ARMY; index <= COMMANDER; index++) {
                        units[index] = new ArrayList();
                    }
                    mapUnitsSector.put(commander.getPosition(), units);
                }
                units[COMMANDER].add(commander);

                // Compute aggregate height for location
                if (positionHeight.containsKey(commander.getPosition())) {
                    final int totHeight = positionHeight.get(commander.getPosition());
                    positionHeight.put(commander.getPosition(), 280 + totHeight);

                } else {
                    positionHeight.put(commander.getPosition(), 280);
                }

                if (!mapBattSector.containsKey(commander.getPosition())) {
                    mapBattSector.put(commander.getPosition(), 1);

                } else {
                    mapBattSector.put(commander.getPosition(), mapBattSector.get(commander.getPosition()) + 1);
                }
            }
        }

        // Retrieve stand-alone brigades
        final List<Brigade> lstBrigade = brigadeManager.listFreeByGameNation(thisGame, thisNation);

        // Count total battalions per sector
        for (final Brigade brigade : lstBrigade) {
            if (!mapBattSector.containsKey(brigade.getPosition())) {
                mapBattSector.put(brigade.getPosition(), brigade.getBattalions().size());

            } else {
                mapBattSector.put(brigade.getPosition(), mapBattSector.get(brigade.getPosition()) + brigade.getBattalions().size());
            }

            // Update list of sectors
            final List[] units;
            if (mapUnitsSector.containsKey(brigade.getPosition())) {
                // Retrieve items
                units = mapUnitsSector.get(brigade.getPosition());


            } else {
                units = new List[COMMANDER + 1];
                for (int index = ARMY; index <= COMMANDER; index++) {
                    units[index] = new ArrayList();
                }
                mapUnitsSector.put(brigade.getPosition(), units);
            }
            units[BRIGADE].add(brigade);

            // Compute aggregate height for location
            if (positionHeight.containsKey(brigade.getPosition())) {
                final int totHeight = positionHeight.get(brigade.getPosition());
                positionHeight.put(brigade.getPosition(), 120 + totHeight);

            } else {
                positionHeight.put(brigade.getPosition(), 120);
            }

        }

        // retrieve Corps
        final List<Corp> lstCorps = corpManager.listGameNation(thisGame, thisNation);
        final List<Corp> lstFreeCorps = new ArrayList<Corp>();
        final Map<Integer, Boolean> corpsNotSupplied = new HashMap<Integer, Boolean>();
        final Map<Integer, Integer> corpsInfTroops = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> corpsInfBattalions = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> corpsCavTroops = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> corpsCavBattalions = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> corpsArtTroops = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> corpsArtBattalions = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> corpsEngTroops = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> corpsEngBattalions = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> corpsTotTroops = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> corpsTotBattalions = new HashMap<Integer, Integer>();
        final Map<Integer, Set<Integer>> corpsBattTypes = new HashMap<Integer, Set<Integer>>();
        final Map<Integer, Map<Integer, Map<Integer, Object[]>>> corpsToBats = new HashMap<Integer, Map<Integer, Map<Integer, Object[]>>>();
        for (Corp corp : lstCorps) {
            if (corp.getArmy() == null || corp.getArmy() == 0) {
                lstFreeCorps.add(corp);
            }

            // Initialize maps
            corpsNotSupplied.put(corp.getCorpId(), false);
            corpsToBats.put(corp.getCorpId(), new HashMap<Integer, Map<Integer, Object[]>>());
            corpsInfTroops.put(corp.getCorpId(), 0);
            corpsInfBattalions.put(corp.getCorpId(), 0);
            corpsCavTroops.put(corp.getCorpId(), 0);
            corpsCavBattalions.put(corp.getCorpId(), 0);
            corpsArtTroops.put(corp.getCorpId(), 0);
            corpsArtBattalions.put(corp.getCorpId(), 0);
            corpsEngTroops.put(corp.getCorpId(), 0);
            corpsEngBattalions.put(corp.getCorpId(), 0);
            corpsTotTroops.put(corp.getCorpId(), 0);
            corpsTotBattalions.put(corp.getCorpId(), 0);
            corpsBattTypes.put(corp.getCorpId(), new HashSet<Integer>());

            int totBatts = 0;
            for (Brigade brigade : corp.getBrigades()) {
                totBatts += brigade.getBattalions().size();
                for (Battalion battalion : brigade.getBattalions()) {
                    int type;
                    corpsBattTypes.get(corp.getCorpId()).add(battalion.getType().getId());

                    // increase statistics
                    if (battalion.getType().isInfantry()) {
                        corpsInfTroops.put(corp.getCorpId(), corpsInfTroops.get(corp.getCorpId()) + battalion.getHeadcount());
                        corpsInfBattalions.put(corp.getCorpId(), corpsInfBattalions.get(corp.getCorpId()) + 1);

                    } else if (battalion.getType().isCavalry()) {
                        corpsCavTroops.put(corp.getCorpId(), corpsCavTroops.get(corp.getCorpId()) + battalion.getHeadcount());
                        corpsCavBattalions.put(corp.getCorpId(), corpsCavBattalions.get(corp.getCorpId()) + 1);

                    } else if (battalion.getType().isArtillery()) {
                        corpsArtTroops.put(corp.getCorpId(), corpsArtTroops.get(corp.getCorpId()) + battalion.getHeadcount());
                        corpsArtBattalions.put(corp.getCorpId(), corpsArtBattalions.get(corp.getCorpId()) + 1);

                    } else if (battalion.getType().isEngineer()) {
                        corpsEngTroops.put(corp.getCorpId(), corpsEngTroops.get(corp.getCorpId()) + battalion.getHeadcount());
                        corpsEngBattalions.put(corp.getCorpId(), corpsEngBattalions.get(corp.getCorpId()) + 1);
                    }
                    corpsTotTroops.put(corp.getCorpId(), corpsTotTroops.get(corp.getCorpId()) + battalion.getHeadcount());

                    if (battalion.getType().isArtillery() || battalion.getType().isMArtillery()) {
                        type = ARTILLERY;

                    } else if (battalion.getType().isCavalry()) {
                        type = CAVALRY;

                    } else if (battalion.getType().isInfantry()) {
                        type = INFANTRY;

                    } else {
                        type = ENGINEER;
                    }

                    if (!corpsToBats.get(corp.getCorpId()).containsKey(type)) {
                        corpsToBats.get(corp.getCorpId()).put(type, new HashMap<Integer, Object[]>());
                    }

                    Object[] stats;
                    if (corpsToBats.get(corp.getCorpId()).get(type).containsKey(battalion.getType().getIntId())) {
                        stats = corpsToBats.get(corp.getCorpId()).get(type).get(battalion.getType().getIntId());
                        stats[0] = (Double) stats[0] + 1;
                        stats[4] = (Double) stats[4] + battalion.getExperience();
                        stats[1] = (Double) stats[4] / (Double) stats[0];
                        stats[2] = (Double) stats[2] + battalion.getHeadcount();
                        final List<Battalion> batsList = (ArrayList<Battalion>) stats[3];
                        batsList.add(battalion);

                        // check if battalion is not supplied
                        if (battalion.getNotSupplied()) {
                            corpsNotSupplied.put(corp.getCorpId(), true);
                        }

                        corpsToBats.get(corp.getCorpId()).get(type).put(battalion.getType().getIntId(), stats);

                    } else {
                        stats = new Object[5];
                        stats[0] = 1.0;//the total number of battalions
                        stats[1] = (double) battalion.getExperience();//the average experience
                        stats[4] = (double) battalion.getExperience();//the average experience
                        stats[2] = (double) battalion.getHeadcount();//the total headcount
                        final List<Battalion> batsList = new ArrayList<Battalion>();
                        batsList.add(battalion);
                        stats[3] = batsList;

                        // check if battalion is not supplied
                        if (battalion.getNotSupplied()) {
                            corpsNotSupplied.put(corp.getCorpId(), true);
                        }

                        corpsToBats.get(corp.getCorpId()).get(type).put(battalion.getType().getIntId(), stats);
                    }
                }
            }

            // Keep track of total battalions per sector
            if (!mapBattSector.containsKey(corp.getPosition())) {
                mapBattSector.put(corp.getPosition(), totBatts);

            } else {
                mapBattSector.put(corp.getPosition(), mapBattSector.get(corp.getPosition()) + totBatts);
            }

            // and per corps
            corpsTotBattalions.put(corp.getCorpId(), corpsTotBattalions.get(corp.getCorpId()) + totBatts);

            // Update list of sectors
            if (corp.getArmy() == null || corp.getArmy() == 0) {
                final List[] units;
                if (mapUnitsSector.containsKey(corp.getPosition())) {
                    // Retrieve items
                    units = mapUnitsSector.get(corp.getPosition());

                } else {
                    units = new List[COMMANDER + 1];
                    for (int index = ARMY; index <= COMMANDER; index++) {
                        units[index] = new ArrayList();
                    }
                    mapUnitsSector.put(corp.getPosition(), units);
                }
                units[CORPS].add(corp);

                // Compute aggregate height for location
                if (positionHeight.containsKey(corp.getPosition())) {
                    final int totHeight = positionHeight.get(corp.getPosition());
                    positionHeight.put(corp.getPosition(), 384 + totHeight);

                } else {
                    positionHeight.put(corp.getPosition(), 384);
                }
            }
        }

        // Retrieve Armies
        final List<Army> lstArmies = armyManager.listGameNation(thisGame, thisNation);
        final Map<Integer, Boolean> armyNotSupplied = new HashMap<Integer, Boolean>();
        final Map<Integer, Integer> infTroops = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> infBattalions = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> cavTroops = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> cavBattalions = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> artTroops = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> artBattalions = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> engTroops = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> engBattalions = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> totTroops = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> totBattalions = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> armyHeight = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> totBrigades = new HashMap<Integer, Integer>();

        // Retrieve battalions for armies
        // armyId : armyTypeId : counter
        final Map<Integer, Map<Integer, Map<Integer, Object[]>>> armyToBat = new HashMap<Integer, Map<Integer, Map<Integer, Object[]>>>();
        for (Army army : lstArmies) {
            armyToBat.put(army.getArmyId(), new HashMap<Integer, Map<Integer, Object[]>>());
            armyNotSupplied.put(army.getArmyId(), false);

            // Calculate height of article
            int height = 5;

            final List<Brigade> brigades = new ArrayList<Brigade>();
            for (Corp corp : army.getCorps()) {
                height += 14;

                if (corp.getCommander() != null) {
                    height += 208;

                } else if (corpsBattTypes.get(corp.getCorpId()).size() > 10) {
                    height += 198;

                } else {
                    height += 120;
                }

                for (Brigade brigade : corp.getBrigades()) {
                    brigades.add(brigade);
                }
            }

            // keep number of total brigades
            totBrigades.put(army.getArmyId(), brigades.size());

            // finalize height of article
            armyHeight.put(army.getArmyId(), Math.max(height, 383));

            final List<Battalion> armyBattalions = battalionManager.listByBrigades(brigades);

            // keep track of total battalions per position
            if (!mapBattSector.containsKey(army.getPosition())) {
                mapBattSector.put(army.getPosition(), armyBattalions.size());
            } else {
                mapBattSector.put(army.getPosition(), mapBattSector.get(army.getPosition()) + armyBattalions.size());
            }

            // Update list of sectors
            final List[] units;
            if (mapUnitsSector.containsKey(army.getPosition())) {
                // Retrieve items
                units = mapUnitsSector.get(army.getPosition());

            } else {
                units = new List[COMMANDER + 1];
                for (int index = ARMY; index <= COMMANDER; index++) {
                    units[index] = new ArrayList();
                }
                mapUnitsSector.put(army.getPosition(), units);
            }
            units[ARMY].add(army);

            // Compute aggregate height for location
            if (positionHeight.containsKey(army.getPosition())) {
                final int totHeight = positionHeight.get(army.getPosition());
                positionHeight.put(army.getPosition(), height + totHeight);

            } else {
                positionHeight.put(army.getPosition(), height);
            }

            infTroops.put(army.getArmyId(), 0);
            infBattalions.put(army.getArmyId(), 0);
            cavTroops.put(army.getArmyId(), 0);
            cavBattalions.put(army.getArmyId(), 0);
            artTroops.put(army.getArmyId(), 0);
            artBattalions.put(army.getArmyId(), 0);
            engTroops.put(army.getArmyId(), 0);
            engBattalions.put(army.getArmyId(), 0);
            totTroops.put(army.getArmyId(), 0);
            totBattalions.put(army.getArmyId(), armyBattalions.size());

            for (Battalion battalion : armyBattalions) {

                if (battalion.getType().isInfantry()) {
                    infTroops.put(army.getArmyId(), infTroops.get(army.getArmyId()) + battalion.getHeadcount());
                    infBattalions.put(army.getArmyId(), infBattalions.get(army.getArmyId()) + 1);

                } else if (battalion.getType().isCavalry()) {
                    cavTroops.put(army.getArmyId(), cavTroops.get(army.getArmyId()) + battalion.getHeadcount());
                    cavBattalions.put(army.getArmyId(), cavBattalions.get(army.getArmyId()) + 1);

                } else if (battalion.getType().isArtillery()) {
                    artTroops.put(army.getArmyId(), artTroops.get(army.getArmyId()) + battalion.getHeadcount());
                    artBattalions.put(army.getArmyId(), artBattalions.get(army.getArmyId()) + 1);

                } else if (battalion.getType().isEngineer()) {
                    engTroops.put(army.getArmyId(), engTroops.get(army.getArmyId()) + battalion.getHeadcount());
                    engBattalions.put(army.getArmyId(), engBattalions.get(army.getArmyId()) + 1);
                }

                totTroops.put(army.getArmyId(), totTroops.get(army.getArmyId()) + battalion.getHeadcount());

                final Object[] stats;
                final int type;
                if (battalion.getType().isArtillery() ||
                        battalion.getType().isMArtillery()) {
                    type = ARTILLERY;

                } else if (battalion.getType().isCavalry()) {
                    type = CAVALRY;

                } else if (battalion.getType().isInfantry()) {
                    type = INFANTRY;

                } else {
                    type = ENGINEER;
                }

                if (!armyToBat.get(army.getArmyId()).containsKey(type)) {
                    armyToBat.get(army.getArmyId()).put(type, new HashMap<Integer, Object[]>());
                }

                // Summarize per battalion type
                if (armyToBat.get(army.getArmyId()).get(type).containsKey(battalion.getType().getIntId())) {
                    stats = armyToBat.get(army.getArmyId()).get(type).get(battalion.getType().getIntId());
                    stats[0] = (Double) stats[0] + 1;
                    stats[4] = (Double) stats[4] + battalion.getExperience();
                    stats[1] = (Double) stats[4] / (Double) stats[0];
                    stats[2] = (Double) stats[2] + battalion.getHeadcount();
                    final List<Battalion> batsList = (ArrayList<Battalion>) stats[3];
                    batsList.add(battalion);

                    // check if battalion is not supplied
                    if (battalion.getNotSupplied()) {
                        armyNotSupplied.put(army.getArmyId(), true);
                    }

                    armyToBat.get(army.getArmyId()).get(type).put(battalion.getType().getIntId(), stats);

                } else {
                    stats = new Object[5];
                    stats[0] = 1.0;//the total number of battalions
                    stats[1] = (double) battalion.getExperience();//the average experience
                    stats[4] = (double) battalion.getExperience();//the average experience
                    stats[2] = (double) battalion.getHeadcount();//the total headcount
                    final List<Battalion> batsList = new ArrayList<Battalion>();
                    batsList.add(battalion);
                    stats[3] = batsList;

                    // check if battalion is not supplied
                    if (battalion.getNotSupplied()) {
                        armyNotSupplied.put(army.getArmyId(), true);
                    }

                    armyToBat.get(army.getArmyId()).get(type).put(battalion.getType().getIntId(), stats);
                }
            }

        }

        //update all average experiences...
        for (Map.Entry<Integer, Map<Integer, Map<Integer, Object[]>>> entry : armyToBat.entrySet()) {
            int armyId = entry.getKey();
            for (Map.Entry<Integer, Map<Integer, Object[]>> entry2 : entry.getValue().entrySet()) {
                int type = entry2.getKey();
                for (Map.Entry<Integer, Object[]> entry3 : entry2.getValue().entrySet()) {
                    int batTypeId = entry3.getKey();
                    Object[] stats = entry3.getValue();
                    stats[1] = (Double) stats[1] / (Double) stats[0];//average exp = total exp / total battalions
                    armyToBat.get(armyId).get(type).put(batTypeId, stats);
                }
            }
        }

        // Sort positions by strength
        final Map[] sortSectorUnits = new TreeMap[REGION_LAST + 1];
        for (int region = EUROPE; region <= AFRICA; region++) {
            sortSectorUnits[region] = new TreeMap<Double, Position>();
        }

        for (final Map.Entry<Position, Integer> entry : mapBattSector.entrySet()) {
            double strength = -1 * (entry.getValue() + ((entry.getKey().getX() + entry.getKey().getY() / 100) / 100));
            while (sortSectorUnits[entry.getKey().getRegion().getId()].containsKey(strength)) {
                strength -= 0.01;//fix strength value so it will be unique in the map.
            }
            sortSectorUnits[entry.getKey().getRegion().getId()].put(strength, entry.getKey());
        }

        // prepare final list of sectors per region, sorted by strength
        final List[] sectors = new List[REGION_LAST + 1];
        for (int region = EUROPE; region <= AFRICA; region++) {
            sectors[region] = new ArrayList<Position>();
            for (Object position : sortSectorUnits[region].values()) {
                sectors[region].add(position);
            }
        }

        // Prepare data to pass to jsp
        refData.put("game", thisGame);
        refData.put("turn", thisGame.getTurn());
        refData.put("nation", thisNation);
        refData.put("gameId", thisGame.getGameId());
        refData.put("gameDate", strBuf.toString());
        refData.put("nationId", thisNation.getId());
        refData.put("vp", 0);
        refData.put("sectors", sectors);
        refData.put("mapUnitsSector", mapUnitsSector);
        refData.put("positionHeight", positionHeight);
        refData.put("armies", lstArmies);
        refData.put("armyNotSupplied", armyNotSupplied);
        refData.put("armyToBats", armyToBat);
        refData.put("armyInfTroops", infTroops);
        refData.put("armyInfBattalions", infBattalions);
        refData.put("armyCavTroops", cavTroops);
        refData.put("armyCavBattalions", cavBattalions);
        refData.put("armyArtTroops", artTroops);
        refData.put("armyArtBattalions", artBattalions);
        refData.put("armyEngTroops", engTroops);
        refData.put("armyEngBattalions", engBattalions);
        refData.put("armyTotTroops", totTroops);
        refData.put("armyTotBattalions", totBattalions);
        refData.put("armyTotBrigades", totBrigades);
        refData.put("armyHeight", armyHeight);
        refData.put("corps", lstFreeCorps);
        refData.put("corpsNotSupplied", corpsNotSupplied);
        refData.put("corpsToBats", corpsToBats);
        refData.put("corpsInfTroops", corpsInfTroops);
        refData.put("corpsInfBattalions", corpsInfBattalions);
        refData.put("corpsCavTroops", corpsCavTroops);
        refData.put("corpsCavBattalions", corpsCavBattalions);
        refData.put("corpsArtTroops", corpsArtTroops);
        refData.put("corpsArtBattalions", corpsArtBattalions);
        refData.put("corpsEngTroops", corpsEngTroops);
        refData.put("corpsEngBattalions", corpsEngBattalions);
        refData.put("corpsTotTroops", corpsTotTroops);
        refData.put("corpsTotBattalions", corpsTotBattalions);
        refData.put("corpsBattTypes", corpsBattTypes);
        refData.put("brigades", lstBrigade);
        refData.put("commanders", lstCommanders);
        refData.put("freeCommanders", freeCommanders);

        return refData;
    }

    /**
     * Instance ArmyManagerBean class to perform queries
     * about army objects.
     */
    private transient ArmyManagerBean armyManager;

    /**
     * Setter method used by spring to inject a ArmyManagerBean bean.
     *
     * @param injArmyManager a ArmyManagerBean bean.
     */
    public void setArmyManager(final ArmyManagerBean injArmyManager) {
        armyManager = injArmyManager;
    }

    /**
     * Instance BattalionManagerBean class to perform queries
     * about battalion objects.
     */
    private transient BattalionManagerBean battalionManager;

    /**
     * Setter method used by spring to inject a BattalionManagerBean bean.
     *
     * @param injBattalionManager a BattalionManagerBean.
     */
    public void setBattalionManager(final BattalionManagerBean injBattalionManager) {
        this.battalionManager = injBattalionManager;
    }

    /**
     * Instance CommanderManager class to perform queries
     * about commander objects.
     */
    private transient CommanderManagerBean commanderManager;

    /**
     * Setter method used by spring to inject a CommanderManager bean.
     *
     * @param injCommanderManager a CommanderManager bean.
     */
    public void setCommanderManager(final CommanderManagerBean injCommanderManager) {
        commanderManager = injCommanderManager;
    }

    /**
     * Instance CorpManager class to perform queries
     * about corp objects.
     */
    private transient CorpManagerBean corpManager;

    /**
     * Setter method used by spring to inject a CorpManager bean.
     *
     * @param injCorpManager a CorpManager bean.
     */
    public void setCorpManager(final CorpManagerBean injCorpManager) {
        this.corpManager = injCorpManager;
    }

    /**
     * Instance BrigadeManager class to perform queries
     * about brigade objects.
     */
    private transient BrigadeManagerBean brigadeManager;

    /**
     * Setter method used by spring to inject a BrigadeManager bean.
     *
     * @param injBrigadeManager a BrigadeManager bean.
     */
    public void setBrigadeManager(final BrigadeManagerBean injBrigadeManager) {
        this.brigadeManager = injBrigadeManager;
    }
}
