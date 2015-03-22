package com.eaw1805.www.controllers.field;

import com.eaw1805.battles.BattleField;
import com.eaw1805.battles.field.generation.MapBuilder;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.converters.FieldBattleMapConverter;
import com.eaw1805.data.managers.beans.BrigadeManagerBean;
import com.eaw1805.data.managers.beans.CommanderManagerBean;
import com.eaw1805.data.managers.beans.FieldBattleMapManagerBean;
import com.eaw1805.data.managers.beans.FieldBattleReportManagerBean;
import com.eaw1805.data.managers.beans.ProductionSiteManagerBean;
import com.eaw1805.data.managers.beans.RegionManagerBean;
import com.eaw1805.data.managers.beans.SectorManagerBean;
import com.eaw1805.data.managers.beans.TerrainManagerBean;
import com.eaw1805.data.managers.beans.UserFieldBattleManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.army.Battalion;
import com.eaw1805.data.model.army.Brigade;
import com.eaw1805.data.model.army.Commander;
import com.eaw1805.data.model.battles.FieldBattleReport;
import com.eaw1805.data.model.battles.field.FieldBattleMap;
import com.eaw1805.data.model.battles.field.FieldBattlePosition;
import com.eaw1805.data.model.battles.field.UserFieldBattle;
import com.eaw1805.data.model.map.*;
import com.eaw1805.fieldmap.CreateMap;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class CreateFieldbattleController
        extends ExtendedController
        implements RegionConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(CreateFieldbattleController.class);


    @ModelAttribute("terrains")
    public List<Terrain> getTerrains() {
        return terrainManager.list();
    }

    @ModelAttribute("productions")
    public List<ProductionSite> getProductionSites() {
        return prSiteManager.list();
    }

    @ModelAttribute("nations")
    public List<Nation> getNations() {
        List<Nation> nations = getNationManager().list();
        nations.remove(0);
        return nations;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fieldbattle/create")
    public String setupForm(final ModelMap model) {
        return "fieldbattle/create";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fieldbattle/create")
    public String processSettingsSubmit() {
        ScenarioContextHolder.setScenario(HibernateUtil.DB_FIELDBATTLE);
        //get http request object.
        try {
            final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            int terrainId = Integer.parseInt(request.getParameter("terrain"));
            int productionId = Integer.parseInt(request.getParameter("production"));
            int population = Integer.parseInt(request.getParameter("population"));
            String[] side1Str = request.getParameterValues("nation1");
            Set<Integer> side1 = new HashSet<Integer>();
            if (side1Str != null) {
                for (String nat : side1Str) {
                    side1.add(Integer.parseInt(nat));
                }
            } else {
                return null;
            }
            String[] side2Str = request.getParameterValues("nation2");
            Set<Integer> side2 = new HashSet<Integer>();
            if (side2Str != null) {
                for (String nat : side2Str) {
                    side2.add(Integer.parseInt(nat));
                }
            } else {
                return null;
            }

            LOGGER.info("Creating fieldbattle for side1 : " + side1 + " -- side2 : " + side2);
//            int side1 = Integer.parseInt(request.getParameter("nation1"));
//            int side2 = Integer.parseInt(request.getParameter("nation2"));
            final int armySize = Integer.parseInt(request.getParameter("armySize"));
            final int newX = fieldBattleManager.list().size();

            final Game initGame = getGameManager().getByID(-1);
            final Set<Nation> side1Nation = new HashSet<Nation>();
            final Set<Nation> side2Nation = new HashSet<Nation>();
            for (Integer nat : side1) {
                side1Nation.add(getNationManager().getByID(nat));
            }
            for (Integer nat : side2) {
                side2Nation.add(getNationManager().getByID(nat));
            }

            final Position dummyPosition = new Position();
            dummyPosition.setGame(null);
            dummyPosition.setRegion(regionManager.getByID(EUROPE));
            dummyPosition.setX(newX);
            dummyPosition.setY(0);

            final Sector dummySector = new Sector();
            dummySector.setNation(side1Nation.iterator().next());
            dummySector.setTerrain(terrainManager.getByID(terrainId));
            dummySector.setProductionSite(prSiteManager.getByID(productionId));
            dummySector.setPosition((Position) dummyPosition.clone());
            dummySector.setPopulation(population);
            dummySector.setName("fieldbattle sector");
            sectorManager.add(dummySector);

            final FieldBattleReport battle = new FieldBattleReport();
            battle.setOwnerId(2);
            battle.setScenarioBattle(true);
            battle.setName("Scenario battle");
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

            final Region europe = regionManager.getByID(EUROPE);

            // copy commanders for side 1
            for (final Nation nat : side1Nation) {
                ScenarioContextHolder.setScenario(HibernateUtil.DB_S2);
                List<Commander> comms = commanderManager.listGameNation(initGame, nat);
                ScenarioContextHolder.setScenario(HibernateUtil.DB_FIELDBATTLE);
                for (Commander comm : comms) {
                    final Commander newComm = cloneCommander(comm, dummyPosition);
                    commanderManager.add(newComm);
                }
            }

            // copy commanders for side 2
            for (final Nation nat : side2Nation) {
                ScenarioContextHolder.setScenario(HibernateUtil.DB_S2);
                List<Commander> comms = commanderManager.listGameNation(initGame, nat);
                ScenarioContextHolder.setScenario(HibernateUtil.DB_FIELDBATTLE);
                for (Commander comm : comms) {
                    final Commander newComm = cloneCommander(comm, dummyPosition);
                    commanderManager.add(newComm);
                }
            }

            int battleId = battle.getBattleId();
            for (int size = 0; size < armySize; size++) {
                for (Nation nat : side1Nation) {
                    ScenarioContextHolder.setScenario(HibernateUtil.DB_S2);
                    final List<Brigade> originalSide1Brigades = brigadeManager.listByGameNationRegion(initGame, nat, europe);
                    LOGGER.info(nat.getName() + " : will create " + originalSide1Brigades.size() + " brigades");
                    ScenarioContextHolder.setScenario(HibernateUtil.DB_FIELDBATTLE);
                    for (Brigade brigade : originalSide1Brigades) {
                        final Brigade newBrig = cloneBrigade(brigade, dummyPosition);
                        brigadeManager.add(newBrig);
                    }
                }

                for (Nation nat : side2Nation) {
                    ScenarioContextHolder.setScenario(HibernateUtil.DB_S2);
                    final List<Brigade> originalSide2Brigades = brigadeManager.listByGameNationRegion(initGame, nat, europe);
                    LOGGER.info(nat.getName() + " : will create " + originalSide2Brigades.size() + " brigades");
                    ScenarioContextHolder.setScenario(HibernateUtil.DB_FIELDBATTLE);
                    for (Brigade brigade : originalSide2Brigades) {
                        final Brigade newBrig = cloneBrigade(brigade, dummyPosition);
                        brigadeManager.add(newBrig);
                    }
                }
            }

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

            final BattleField field = new BattleField(dummySector);//34083
            for (Nation nation : battle.getSide1()) {
                field.addNation(0, nation);
            }

            for (Nation nation : battle.getSide2()) {
                field.addNation(1, nation);
            }

            final MapBuilder mapBuilder = new MapBuilder(field, sides);
            FieldBattleMap map = mapBuilder.buildMap();
            map.setBattleId(battleId);

            fieldMapManager.add(map);

            for (Nation nation : battle.getSide1()) {
                final UserFieldBattle userField = new UserFieldBattle();
                userField.setReady(false);
                userField.setUserId(2);//admin id
                userField.setBattleId(battleId);
                userField.setNationId(nation.getId());
                userFieldManager.add(userField);
            }

            for (Nation nation : battle.getSide2()) {
                final UserFieldBattle userField = new UserFieldBattle();
                userField.setReady(false);
                userField.setUserId(2);//admin id
                userField.setBattleId(battleId);
                userField.setNationId(nation.getId());
                userFieldManager.add(userField);
            }

            new CreateMap(FieldBattleMapConverter.convert(map), HibernateUtil.DB_FIELDBATTLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/games";
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

    @Autowired
    @Qualifier("terrainManagerBean")
    private transient TerrainManagerBean terrainManager;

    @Autowired
    @Qualifier("productionSiteManagerBean")
    private transient ProductionSiteManagerBean prSiteManager;

    @Autowired
    @Qualifier("fieldBattleReportManagerBean")
    private transient FieldBattleReportManagerBean fieldBattleManager;

    @Autowired
    @Qualifier("fieldBattleMapManagerBean")
    private transient FieldBattleMapManagerBean fieldMapManager;

    @Autowired
    @Qualifier("brigadeManagerBean")
    private transient BrigadeManagerBean brigadeManager;


    @Autowired
    @Qualifier("regionManagerBean")
    private transient RegionManagerBean regionManager;

    @Autowired
    @Qualifier("sectorManagerBean")
    private transient SectorManagerBean sectorManager;

    @Autowired
    @Qualifier("commanderManagerBean")
    private transient CommanderManagerBean commanderManager;

    @Autowired
    @Qualifier("userFieldBattleManagerBean")
    private transient UserFieldBattleManagerBean userFieldManager;

}
