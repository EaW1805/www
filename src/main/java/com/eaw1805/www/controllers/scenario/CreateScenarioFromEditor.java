package com.eaw1805.www.controllers.scenario;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.managers.RelationsManager;
import com.eaw1805.data.managers.army.SpyManager;
import com.eaw1805.data.managers.beans.*;
import com.eaw1805.data.managers.map.ProductionSiteManager;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.NationsRelation;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.army.*;
import com.eaw1805.data.model.economy.BaggageTrain;
import com.eaw1805.data.model.economy.Good;
import com.eaw1805.data.model.economy.TradeCity;
import com.eaw1805.data.model.economy.Warehouse;
import com.eaw1805.data.model.fleet.Fleet;
import com.eaw1805.data.model.fleet.Ship;
import com.eaw1805.data.model.fleet.ShipType;
import com.eaw1805.data.model.map.*;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
public class CreateScenarioFromEditor extends BaseController implements GoodConstants, ProductionSiteConstants, NationConstants, RelationConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(CreateScenarioFromEditor.class);

    @RequestMapping(method = RequestMethod.GET, value = "/generate/scenario/editor/{mapId}/scenario/{scenarioId}")
    public String createScenario(@PathVariable("mapId") String mapId,
                                 @PathVariable("scenarioId") String scenarioId) throws Exception {
        try {
            LOGGER.info("Moving editor " + mapId + " to scenario " + scenarioId);
            ScenarioContextHolder.setScenario(HibernateUtil.DB_EDITOR);

            final User user = getUser();

            if (mapId.isEmpty() || scenarioId.isEmpty()) {
                throw new InvalidPageException("Page not found");
            }


            final int map = Integer.parseInt(mapId);

            //first retrieve all data.
            LOGGER.info("Loading editor data");

            final Game editorGame = gameManager.getByID(map);
            if (user.getUserType() != 3 && user.getUserId() != editorGame.getUserId()) {
                throw new InvalidPageException("Page not found");
            }
            //retrieve dynamic data
            final List<Sector> editorSectors = sectorManager.listByGame(editorGame);
            final List<Army> editorArmies = armyManager.listGame(editorGame);
            final List<Corp> editorCorps = corpManager.listFreeByGame(editorGame);
            final List<Brigade> editorBrigades = brigadeManager.listFreeByGame(editorGame);
            final List<Commander> editorCommanders = commanderManager.listByGame(editorGame);
            final List<Spy> editorSpies = spyManager.listByGame(editorGame);
            final List<BaggageTrain> editorTrains = bTrainManager.listByGame(editorGame);
            final List<Warehouse> editorWarehouses = warehouseManager.listByGame(editorGame);
            final List<Region> editorRegions = regionManager.list(editorGame);
            final List<Fleet> editorFleets = fleetManager.listByGame(editorGame);
            final List<Ship> editorShips = shipManager.listByGame(editorGame);
            //retrieve static data

            final List<Good> goods = goodManager.list();

            final List<ProductionSite> prSites = prSiteManager.list();
            final List<Terrain> terrains = terrainManager.list();
            final List<NaturalResource> resources = resourceManager.list();
            final List<Nation> nations = nationManager.list();
            final List<ArmyType> armyTypes = armyTypeManager.list();
            final List<ShipType> shipTypes = shipTypeManager.list();
            final List<Rank> ranks = rankManager.list();

            LOGGER.info("Copying to scenario");

            ScenarioContextHolder.setScenario(scenarioId);

            //first save the static data.
            LOGGER.info("Copying production sites");
            for (Good good : goods) {
                goodManager.add(good);
            }
            for (ProductionSite prSite : prSites) {
                prSiteManager.add(prSite);
            }
            for (Terrain terrain : terrains) {
                terrainManager.add(terrain);
            }
            for (NaturalResource resource : resources) {
                resourceManager.add(resource);
            }
            for (Nation nation : nations) {
                nationManager.add(nation);
            }
            for (ArmyType type : armyTypes) {
                armyTypeManager.add(type);
            }
            for (ShipType type : shipTypes) {
                shipTypeManager.add(type);
            }
            for (Rank rank : ranks) {
                rankManager.add(rank);
            }

            LOGGER.info("done");
            LOGGER.info("Adding game");
            final Game game = new Game();
            game.setGameId(-1);
            game.setDescription("Scenario game");
            game.setDateNextProc(new Date());
            game.setDateStart(new Date());
            game.setDateLastProc(new Date());
            game.setCronSchedule("");
            game.setCronScheduleDescr("");
            game.setUserId(editorGame.getUserId());
            gameManager.add(game);
            final Map<Integer, Region> regionToCopy = new HashMap<Integer, Region>();
            for (final Region region : editorRegions) {
                Region copy = new Region();
                copy.setCode(region.getCode());
                copy.setGame(game);
                copy.setName(region.getName());
                regionManager.add(copy);
                regionToCopy.put(region.getId(), copy);
            }

            LOGGER.info("Adding sectors");

            for (final Sector sector : editorSectors) {
                final Sector copy = new Sector();
                copy.setClimaticZone(sector.getClimaticZone());
                copy.setPosition((Position) sector.getPosition().clone());
                copy.getPosition().setRegion(regionToCopy.get(sector.getPosition().getRegion().getId()));
                copy.getPosition().setGame(game);
                copy.setPopulation(sector.getPopulation());
                copy.setTerrain(sector.getTerrain());
                copy.setNaturalResource(sector.getNaturalResource());
                copy.setBuildProgress(sector.getBuildProgress());
                copy.setNation(sector.getNation());
                copy.setPoliticalSphere(sector.getPoliticalSphere());
                copy.setConqueredCounter(sector.getConqueredCounter());
                copy.setTradeCity(sector.getTradeCity());
                copy.setEpidemic(sector.getEpidemic());
                copy.setRebelled(sector.getRebelled());
                copy.setName(sector.getName());
                copy.setImage(sector.getImage());
                copy.setImageGeo(sector.getImageGeo());
                copy.setTempNation(sector.getTempNation());
                copy.setStorm(sector.getStorm());
                copy.setPayed(sector.getPayed());
                copy.setConquered(sector.getConquered());
                copy.setFow(sector.getFow());
                copy.setRiverNorth(sector.isRiverNorth());
                copy.setRiverEast(sector.isRiverEast());
                copy.setRiverSouth(sector.isRiverSouth());
                copy.setRiverWest(sector.isRiverWest());
                copy.setClimaticZone(sector.getClimaticZone());

                if (copy.getTradeCity()) {
                    final TradeCity tCity = new TradeCity();
                    tCity.setPosition((Position) copy.getPosition().clone());
                    tCity.setNation(copy.getNation());
                    tCity.setName(copy.getName());
                    final Map<Integer, Integer> qteGoods = new HashMap<Integer, Integer>(); // NOPMD
                    for (int goodID = GOOD_FIRST; goodID <= GOOD_COLONIAL; goodID++) {
                        qteGoods.put(goodID, 0);
                    }
                    tCity.setGoodsTradeLvl(qteGoods);
                    tradeCityManager.add(tCity);
                    if (copy.getProductionSite() == null
                            || copy.getProductionSite().getId() < PS_BARRACKS) {
                        //if trade city, be sure there is a barrack in the position.
                        copy.setProductionSite(prSiteManager.getByID(PS_BARRACKS));
                    }
                }
                if (copy.getProductionSite() != null
                        && copy.getProductionSite().getId() >= PS_BARRACKS) {
                    final Barrack barrack = new Barrack();
                    barrack.setName(copy.getName());
                    barrack.setNation(copy.getNation());
                    barrack.setNotSupplied(false);
                    barrack.setPosition((Position) copy.getPosition().clone());
                    barrackManager.add(barrack);
                }
                sectorManager.add(copy);

            }



            LOGGER.info("Adding spies");
            for (final Spy spy : editorSpies) {
                final Spy copy = new Spy();
                copy.setPosition((Position) spy.getPosition().clone());
                copy.getPosition().setGame(game);
                copy.getPosition().setRegion(regionToCopy.get(spy.getPosition().getRegion().getId()));
                copy.setName(spy.getName());
                copy.setNation(spy.getNation());
                copy.setStationary(spy.getStationary());
                copy.setColonial(spy.getColonial());
                copy.setReportBattalions(spy.getReportBattalions());
                copy.setReportBrigades(spy.getReportBrigades());
                copy.setReportShips(spy.getReportShips());
                copy.setReportNearbyShips(spy.getReportNearbyShips());
                copy.setReportTrade(spy.getReportTrade());
                copy.setReportRelations(spy.getReportRelations());
                copy.setCarrierInfo(new CarrierInfo());
                copy.getCarrierInfo().setCarrierType(spy.getCarrierInfo().getCarrierType());
                copy.getCarrierInfo().setCarrierId(spy.getCarrierInfo().getCarrierId());
                spyManager.add(copy);
            }

            LOGGER.info("Adding baggage trains");
            for (final BaggageTrain bTrain : editorTrains) {
                BaggageTrain copy = new BaggageTrain();
                copy.setPosition((Position) bTrain.getPosition().clone());
                copy.getPosition().setRegion(regionToCopy.get(bTrain.getPosition().getRegion().getId()));
                copy.getPosition().setGame(game);
                copy.setName(bTrain.getName());
                copy.setNation(bTrain.getNation());
                copy.setCondition(bTrain.getCondition());
                final Map<Integer, Integer> qteGoods = new HashMap<Integer, Integer>(); // NOPMD
                for (int goodID = GOOD_FIRST; goodID <= GOOD_COLONIAL; goodID++) {
                    qteGoods.put(goodID, 0);
                }
                copy.setStoredGoods(qteGoods);
                bTrainManager.add(copy);
            }
            final Map<Integer, Fleet> fleetToCopy = new HashMap<Integer, Fleet>();
            LOGGER.info("Adding fleets");
            for (final Fleet fleet : editorFleets) {
                final Fleet copy = new Fleet();
                copy.setNation(fleet.getNation());
                copy.setName(fleet.getName());
                copy.setPosition((Position) fleet.getPosition().clone());
                copy.getPosition().setGame(game);
                copy.getPosition().setRegion(regionToCopy.get(fleet.getPosition().getRegion().getId()));
                copy.setMps(fleet.getMps());
                fleetManager.add(copy);
                fleetToCopy.put(fleet.getFleetId(), copy);
            }
            LOGGER.info("Adding ships");
            for (final Ship ship : editorShips) {
                final Ship copy = new Ship();
                copy.setPosition((Position) ship.getPosition().clone());
                copy.getPosition().setGame(game);
                copy.getPosition().setRegion(regionToCopy.get(ship.getPosition().getRegion().getId()));
                copy.setNation(ship.getNation());
                copy.setName(ship.getName());
                copy.setType(ship.getType());
                if (ship.getFleet() != 0) {
                    copy.setFleet(fleetToCopy.get(ship.getFleet()).getFleetId());
                }
                copy.setCondition(ship.getCondition());
                copy.setMarines(ship.getMarines());
                copy.setExp(ship.getExp());
                copy.setCapturedByNation(ship.getCapturedByNation());
                copy.setNavalBattle(ship.getNavalBattle());
                copy.setNoWine(ship.getNoWine());
                copy.setHasMoved(ship.getHasMoved());
                copy.setJustConstructed(ship.getJustConstructed());
                final Map<Integer, Integer> qteGoods = new HashMap<Integer, Integer>(); // NOPMD
                for (int goodID = GOOD_FIRST; goodID <= GOOD_COLONIAL; goodID++) {
                    qteGoods.put(goodID, 0);
                }
                copy.setStoredGoods(qteGoods);
                shipManager.add(copy);
            }

            LOGGER.info("Adding armies");
            for (final Army army : editorArmies) {
                saveArmy(army, regionToCopy.get(army.getPosition().getRegion().getId()), game);
            }
            LOGGER.info("Adding corps");
            for (final Corp corp : editorCorps) {
                saveCorp(corp, regionToCopy.get(corp.getPosition().getRegion().getId()), game, 0);
            }
            LOGGER.info("Adding brigades");
            for (final Brigade brigade : editorBrigades) {
                saveBrigade(brigade, regionToCopy.get(brigade.getPosition().getRegion().getId()), game, 0);
            }
            LOGGER.info("Adding commanders");
            for (Commander commander : editorCommanders) {
                if (commander.getArmy() == 0 && commander.getCorp() == 0) {
                    saveCommander(commander, regionToCopy.get(commander.getPosition().getRegion().getId()), game, 0, 0);
                }
            }
            LOGGER.info("Adding warehouses");
            for (Warehouse warehouse : editorWarehouses) {
                final Warehouse copy = new Warehouse();
                copy.setNation(warehouse.getNation());
                copy.setGame(game);
                copy.setRegion(regionToCopy.get(warehouse.getRegion().getId()));
                final Map<Integer, Integer> stGoods = new HashMap<Integer, Integer>();
                for (Map.Entry<Integer, Integer> entry : warehouse.getStoredGoodsQnt().entrySet()) {
                    stGoods.put(entry.getKey(), entry.getValue());
                }
                copy.setStoredGoodsQnt(stGoods);
                warehouseManager.add(copy);
            }
            LOGGER.info("Adding relations");
            for (final Nation thisNation : nations) {
                for (final Nation thatNation : nations) {
                    if (thisNation.getId() == thatNation.getId()) {
                        continue;
                    }
                    final NationsRelation thisRel = new NationsRelation(); // NOPMD
                    thisRel.setGame(game);
                    thisRel.setNation(thisNation);
                    thisRel.setTarget(thatNation);
                    thisRel.setRelation(REL_TRADE);
                    thisRel.setPrisoners(0);
                    thisRel.setTurnCount(0);
                    thisRel.setPeaceCount(0);
                    thisRel.setSurrenderCount(0);
                    relationsManager.add(thisRel);
                }
            }



            LOGGER.info("done");

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveArmy(final Army army, final Region region, final Game game) {
        final Army copy = new Army();
        copy.setPosition((Position) army.getPosition().clone());
        copy.getPosition().setGame(game);
        copy.getPosition().setRegion(region);
        copy.setNation(army.getNation());
        copy.setName(army.getName());
        copy.setMps(army.getMps());
        //apply commander
        armyManager.add(copy);
        if (army.getCommander() != null) {
            copy.setCommander(saveCommander(army.getCommander(), army.getPosition().getRegion(), game, 0, copy.getArmyId()));
        }
        //apply corps
        Set<Corp> corpSet = new HashSet<Corp>();
        for (Corp corp : army.getCorps()) {
            corpSet.add(saveCorp(corp, region, game, copy.getArmyId()));
        }
        copy.setCorps(corpSet);
        armyManager.update(copy);
    }

    public Corp saveCorp(Corp corp, Region region, Game game, int army) {
        final Corp copy = new Corp();
        copy.setPosition((Position) corp.getPosition().clone());
        copy.getPosition().setGame(game);
        copy.getPosition().setRegion(region);
        copy.setNation(corp.getNation());
        copy.setName(corp.getName());
        copy.setMps(corp.getMps());
        copy.setArmy(army);
        corpManager.add(copy);
        if (corp.getCommander() != null) {
            copy.setCommander(saveCommander(corp.getCommander(), region, game, copy.getCorpId(), 0));
        }
        Set<Brigade> brigsSet = new HashSet<Brigade>();
        for (final Brigade brigade : corp.getBrigades()) {
            brigsSet.add(saveBrigade(brigade, region, game, copy.getCorpId()));
        }
        corp.setBrigades(brigsSet);
        corpManager.update(copy);
        return copy;
    }

    public Brigade saveBrigade(Brigade brigade, Region region, Game game, int corp) {
        final Brigade copy = new Brigade();
        copy.setPosition((Position) brigade.getPosition().clone());
        copy.getPosition().setGame(game);
        copy.getPosition().setRegion(region);
        copy.setName(brigade.getName());
        copy.setMps(brigade.getMps());
        copy.setCorp(corp);
        copy.setNation(brigade.getNation());
        copy.setArmType(brigade.getArmType());
        copy.setFormation(brigade.getFormation());
        copy.setMoraleStatus(brigade.getMoraleStatus());
        copy.setFromInit(brigade.getFromInit());
        brigadeManager.add(copy);
        Set<Battalion> batSet = new HashSet<Battalion>();
        for (Battalion battalion : brigade.getBattalions()) {
            batSet.add(saveBattalion(battalion, copy));
        }
        copy.setBattalions(batSet);
        brigadeManager.update(copy);
        return copy;
    }

    public Battalion saveBattalion(Battalion battalion, final Brigade brigade) {
        final Battalion copy = new Battalion();
        copy.setOrder(battalion.getOrder());
        copy.setType(battalion.getType());
        copy.setExperience(battalion.getExperience());
        copy.setHeadcount(battalion.getHeadcount());
        copy.setBrigade(brigade);
        copy.setFleeing(battalion.isFleeing());
        copy.setAttackedByCav(battalion.isAttackedByCav());
        copy.setExpIncByComm(battalion.getExpIncByComm());
        copy.setParticipated(battalion.isParticipated());
        copy.setUnloaded(battalion.getUnloaded());
        copy.setHasMoved(battalion.getHasMoved());
        copy.setHasEngagedBattle(battalion.getHasEngagedBattle());
        copy.setHasLost(battalion.getHasLost());
        copy.setNotSupplied(battalion.getNotSupplied());
        copy.setCarrierInfo(battalion.getCarrierInfo());
        battalionManager.add(copy);
        return copy;
    }

    public Commander saveCommander(Commander commander, Region region, Game game, int corp, int army) {
        Commander copy = new Commander();
        copy.setPosition(commander.getPosition());
        copy.getPosition().setRegion(region);
        copy.getPosition().setGame(game);
        copy.setRank(commander.getRank());
        copy.setName(commander.getName());
        copy.setMps(commander.getMps());
        copy.setComc(commander.getComc());
        copy.setStrc(commander.getStrc());
        copy.setCorp(corp);
        copy.setArmy(army);
        copy.setNation(commander.getNation());
        copy.setIntId(commander.getIntId());
        copy.setDead(commander.getDead());
        copy.setSupreme(commander.getSupreme());
        copy.setSick(commander.getSick());
        copy.setInTransit(commander.getInTransit());
        copy.setPool(commander.getPool());
        copy.setTransit(commander.getTransit());
        copy.setCavalryLeader(commander.getCavalryLeader());
        copy.setArtilleryLeader(commander.getArtilleryLeader());
        copy.setStoutDefender(commander.getStoutDefender());
        copy.setFearlessAttacker(commander.getFearlessAttacker());
        copy.setLegendaryCommander(commander.getLegendaryCommander());
        copy.setCarrierInfo(commander.getCarrierInfo());
        copy.setCaptured(commander.getCaptured());
        commanderManager.add(copy);
        return copy;
    }

    @Autowired
    @Qualifier("relationsManagerBean")
    private transient RelationsManagerBean relationsManager;

    @Autowired
    @Qualifier("tradeCityManagerBean")
    private transient TradeCityManagerBean tradeCityManager;

    @Autowired
    @Qualifier("barrackManagerBean")
    private transient BarrackManagerBean barrackManager;

    @Autowired
    @Qualifier("battalionManagerBean")
    private transient BattalionManagerBean battalionManager;

    @Autowired
    @Qualifier("shipManagerBean")
    private transient ShipManagerBean shipManager;

    @Autowired
    @Qualifier("fleetManagerBean")
    private transient FleetManagerBean fleetManager;

    @Autowired
    @Qualifier("regionManagerBean")
    private transient RegionManagerBean regionManager;

    @Autowired
    @Qualifier("rankManagerBean")
    private transient RankManagerBean rankManager;

    @Autowired
    @Qualifier("shipTypeManagerBean")
    private transient ShipTypeManagerBean shipTypeManager;

    @Autowired
    @Qualifier("armyTypeManagerBean")
    private transient ArmyTypeManagerBean armyTypeManager;

    @Autowired
    @Qualifier("nationManagerBean")
    private transient NationManagerBean nationManager;

    @Autowired
    @Qualifier("naturalResourceManagerBean")
    private transient NaturalResourceManagerBean resourceManager;

    @Autowired
    @Qualifier("terrainManagerBean")
    private transient TerrainManagerBean terrainManager;

    @Autowired
    @Qualifier("productionSiteManagerBean")
    private transient ProductionSiteManagerBean prSiteManager;

    @Autowired
    @Qualifier("goodManagerBean")
    private transient GoodManagerBean goodManager;

    @Autowired
    @Qualifier("warehouseManagerBean")
    private transient WarehouseManagerBean warehouseManager;

    @Autowired
    @Qualifier("baggageTrainManagerBean")
    private transient BaggageTrainManagerBean bTrainManager;

    @Autowired
    @Qualifier("spyManagerBean")
    private transient SpyManagerBean spyManager;

    @Autowired
    @Qualifier("gameManagerBean")
    private transient GameManagerBean gameManager;

    @Autowired
    @Qualifier("sectorManagerBean")
    private transient SectorManagerBean sectorManager;

    @Autowired
    @Qualifier("armyManagerBean")
    private transient ArmyManagerBean armyManager;

    @Autowired
    @Qualifier("corpManagerBean")
    private transient CorpManagerBean corpManager;

    @Autowired
    @Qualifier("brigadeManagerBean")
    private transient BrigadeManagerBean brigadeManager;

    @Autowired
    @Qualifier("commanderManagerBean")
    private transient CommanderManagerBean commanderManager;

}
