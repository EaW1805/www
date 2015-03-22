package com.eaw1805.www.controllers.remote;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.eaw1805.algorithms.DistanceCalculator;
import com.eaw1805.algorithms.MovementShortestPath;
import com.eaw1805.algorithms.SupplyLinesConnectivity;
import com.eaw1805.data.cache.Cachable;
import com.eaw1805.data.cache.ClientCachable;
import com.eaw1805.data.cache.EvictCache;
import com.eaw1805.data.cache.GameCachable;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.AchievementConstants;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GameConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.NewsConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.ProfileConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.constants.TerrainConstants;
import com.eaw1805.data.dto.collections.DataCollection;
import com.eaw1805.data.dto.common.BattleDTO;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.converters.*;
import com.eaw1805.data.dto.web.ChatMessageDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.GameSettingsDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.OrderPositionDTO;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.data.dto.web.RelationDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.dto.web.army.ForeignArmyDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.data.dto.web.economy.WarehouseDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.data.dto.web.movement.MovementDTO;
import com.eaw1805.data.dto.web.movement.PathDTO;
import com.eaw1805.data.managers.beans.*;
import com.eaw1805.data.model.Achievement;
import com.eaw1805.data.model.ChatMessage;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.GameSettings;
import com.eaw1805.data.model.Message;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.NationsRelation;
import com.eaw1805.data.model.News;
import com.eaw1805.data.model.PlayerOrder;
import com.eaw1805.data.model.Profile;
import com.eaw1805.data.model.Report;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.army.Brigade;
import com.eaw1805.data.model.army.Commander;
import com.eaw1805.data.model.army.Corp;
import com.eaw1805.data.model.battles.NavalBattleReport;
import com.eaw1805.data.model.battles.TacticalBattleReport;
import com.eaw1805.data.model.comparators.TargetNationId;
import com.eaw1805.data.model.fleet.ShipType;
import com.eaw1805.data.model.map.Barrack;
import com.eaw1805.data.model.map.NaturalResource;
import com.eaw1805.data.model.map.Position;
import com.eaw1805.data.model.map.ProductionSite;
import com.eaw1805.data.model.map.Region;
import com.eaw1805.data.model.map.Sector;
import com.eaw1805.data.model.orders.PatrolOrderDetails;
import com.eaw1805.www.client.remote.EmpireRpcService;
import com.eaw1805.www.controllers.cache.async.EawAsync;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.controllers.remote.hotspot.OrderApplyChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.SetupAlliedUnitsProccessor;
import com.eaw1805.www.controllers.remote.hotspot.SetupForeignUnitsProcessor;
import com.eaw1805.www.controllers.remote.hotspot.SpyApplyChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.army.ArmyApplyChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.army.BarrackApplyChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.army.BarrackSaveChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.army.CommanderApplyChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.army.CommandersSaveChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.army.SetupNewBrigadeOrders;
import com.eaw1805.www.controllers.remote.hotspot.army.UnitsSaveChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.economy.ApplyEconomyChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.economy.BaggageTrainApplyChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.economy.BaggageTrainSaveChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.economy.SetupNewBaggageTrainOrders;
import com.eaw1805.www.controllers.remote.hotspot.economy.TradeChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.economy.TradeCitiesApplyChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.economy.TradeShipApplyChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.economy.TransportChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.movement.MovementApplyChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.movement.MovementSaveChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.navy.NavyApplyChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.navy.NavySaveChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.navy.SetupNewShipOrders;
import com.eaw1805.www.controllers.remote.hotspot.politics.PoliticsApplyChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.politics.PoliticsSaveChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.region.ProdSitesChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.region.RegionApplyChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.region.RegionSaveChangesProcessor;
import com.eaw1805.www.controllers.site.ArticleManager;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import com.eaw1805.www.shared.AlliedUnits;
import com.eaw1805.www.shared.ArmiesAndCommanders;
import com.eaw1805.www.shared.ForeignUnits;
import com.eaw1805.www.shared.TilesCollection;
import com.eaw1805.www.shared.stores.support.Taxation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * The server side implementation of the RPC service.
 */
@Controller
@SuppressWarnings({"serial", "restriction"})
public class EmpireRpcServiceImpl
        extends RemoteServiceServlet
        implements EmpireRpcService, OrderConstants, ArmyConstants, RegionConstants, GoodConstants, RelationConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(EmpireRpcServiceImpl.class);

    @RequestMapping(method = RequestMethod.GET, value = "/cache/scenario/{scenarioIdStr}/game/{gameIdStr}/nation/{nationIdStr}")
    protected ModelAndView handle(@PathVariable final String scenarioIdStr,
                                  @PathVariable final String gameIdStr,
                                  @PathVariable final String nationIdStr,
                                  HttpServletRequest request) throws Exception {
        ScenarioContextHolder.defaultScenario();

        // Determine scenario
        int scenarioNum;
        if ((scenarioIdStr == null) || (scenarioIdStr.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            // Set the scenario
            ScenarioContextHolder.setScenario(scenarioIdStr);

            if ("1804".equals(scenarioIdStr)) {
                scenarioNum = HibernateUtil.DB_FREE;

            } else if ("1808".equals(scenarioIdStr)) {
                scenarioNum = HibernateUtil.DB_S3;

            } else if ("1805".equals(scenarioIdStr)) {
                scenarioNum = HibernateUtil.DB_S2;

            } else {//else if 1802
                scenarioNum = HibernateUtil.DB_S1;
            }
        }

        // Check that user is allowed to view nation
        final User thisUser = getUser();
        if (thisUser.getUserType() != 3) {
            throw new InvalidPageException("Access denied");
        }

        // Retrieve Game entity
        Game thisGame;
        if ((gameIdStr == null) || (gameIdStr.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisGame = getGame(scenarioNum, Integer.parseInt(gameIdStr));
            if (thisGame == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Retrieve Nation entity
        Nation thisNation;
        if ((nationIdStr == null) || (nationIdStr.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisNation = nationManager.getByID(Integer.parseInt(nationIdStr));
            if (thisNation == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        final int scenarioId = thisGame.getScenarioId();
        final int gameId = thisGame.getGameId();
        final int nationId = thisNation.getId();
        final int turn = thisGame.getTurn();

        // Preparing Constant cache
        getRegion(scenarioId);
        getNations(scenarioId);
        getNaturalResAndProdSites(scenarioId);
        getArmyTypes(scenarioId, nationId);
        getShipTypes(scenarioId, nationId);

        // Preparing Game Cache
        final List<Region> lstRegions = regionManager.list();
        for (Region region : lstRegions) {
            getSupplyLines(scenarioId, gameId, region.getId(), nationId);
        }

        getNationsRelations(scenarioId, gameId, nationId, turn);
        getNationSectors(scenarioId, gameId, nationId);
        getWarehouses(scenarioId, gameId, nationId);
        getCommandersByGameNation(scenarioId, gameId, nationId);
        getCallForAllies(scenarioId, nationId, gameId, turn);
        getNavalTacticalBattlesPositions(scenarioId, nationId, gameId, turn);
        getForeignArmies(scenarioId, gameId, nationId);
        getNationsStatus(scenarioId, gameId, turn);

        // Preparing Client Cache
        for (Region region : lstRegions) {
            getRegionByGameAndNation(scenarioId, region.getId(), gameId, turn, nationId);
            getFleetsByNationRegion(scenarioId, nationId, region.getId(), gameId, turn, true, nationId, true);
            getArmiesByNationRegion(scenarioId, nationId, region.getId(), gameId, turn, true);
        }

        getTaxationByNationAndGame(scenarioId, nationId, gameId, turn);
        getBarracksByNation(scenarioId, nationId, gameId, turn);
        getFleetsByNation(scenarioId, nationId, gameId, turn);
        getArmiesByNation(scenarioId, nationId, gameId, turn);
        getAlliedUnits(scenarioId, nationId, gameId, turn);
        getForeignUnitsOnNationTerritory(scenarioId, nationId, gameId, turn);

//        // Load all sectors
//        final List<Sector> lstSectors = sectorManager.listByGame(thisGame);
//        for (final Sector sector : lstSectors) {
//            getSectorByCoordinates(scenarioNum, gameId, sector.getPosition().getRegion().getId(),
//                    sector.getPosition().getX(), sector.getPosition().getY());
//        }

//        // Prepare data to pass to jsp
//        final Map<String, Object> refData = new HashMap<String, Object>();
//        refData.put("user", thisUser);
//        refData.put("engine", new EngineProcess());
//        refData.put("unreadMessagesCount", 0);
//        refData.put("userNewAchievements", new ArrayList<empire.data.model.Achievement>());

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Cache game=" + gameIdStr + "/turn=" + thisGame.getTurn() + "/nation=" + thisNation.getName());
        return null;
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

    //no cache here, tutorial gets messed up.
//    @Cachable(cacheName = "gameCache")
    public Game getGame(final int scenarioId, final int gameId) {
        ScenarioContextHolder.setScenario(scenarioId);
        return gameManager.getByID(gameId);
    }

    /**
     * Retrieve all the regions.
     *
     * @return a list of Regions.
     */
    @Cachable(cacheName = "constantCache")
    public List<RegionDTO> getRegion(final int scenarioId) {
        ScenarioContextHolder.setScenario(scenarioId);
        return RegionConverter.convert(regionManager.list());
    }

    @Cachable(cacheName = "constantCache")
    public Region getRegion(final int scenarioId, final int regionId) {
        ScenarioContextHolder.setScenario(scenarioId);
        return regionManager.getByID(regionId);
    }

    /**
     * Retrieves the nations of the game.
     *
     * @return a list of nations.
     */
    @Cachable(cacheName = "constantCache")
    public List<NationDTO> getNations(final int scenarioId) {
        ScenarioContextHolder.setScenario(scenarioId);
        return NationConverter.convert(nationManager.list());
    }

    @Cachable(cacheName = "constantCache")
    public Nation getFreeNation(final int scenarioId) {
        ScenarioContextHolder.setScenario(scenarioId);
        return nationManager.getByID(NationConstants.NATION_NEUTRAL);
    }

    @Cachable(cacheName = "constantCache")
    public Nation getNation(final int scenarioId, final int nationId) {
        ScenarioContextHolder.setScenario(scenarioId);
        return nationManager.getByID(nationId);
    }

    /**
     * Retrieve the tables of Natural Resources and Production Sites.
     *
     * @return two lists of NaturalResources and ProductionSites objects.
     */
    @Cachable(cacheName = "constantCache")
    public DataCollection getNaturalResAndProdSites(final int scenarioId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final DataCollection dataCol = new DataCollection();

        // Add nations
        dataCol.setNations(NationConverter.convert(nationManager.list()));

        // Natural Resources
        final List<NaturalResource> lstNatRes = naturalResourceManager.list();
        dataCol.setNatResources(NaturalResourceConverter.convert(lstNatRes));

        // Production sites
        final List<ProductionSite> lstProdSite = productionSiteManager.list();
        dataCol.setProdSites(ProductionSiteConverter.convert(lstProdSite));

        return dataCol;
    }

    /**
     * Retrieves the army types for a particular nation.
     *
     * @param nationId the identity of the nation.
     * @return the list of armytype objects.
     */
    @Cachable(cacheName = "constantCache")
    public List<ArmyTypeDTO> getArmyTypes(final int scenarioId, final int nationId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Nation thisNation = getNation(scenarioId, nationId);
        return ArmyTypeConverter.convert(armyTypeManager.list(thisNation));
    }

    /**
     * Retrieves the ship types for a particular nation.
     *
     * @param nationId the identity of the nation.
     * @return the list of shiptype objects.
     */
    @Cachable(cacheName = "constantCache")
    public List<ShipTypeDTO> getShipTypes(final int scenarioId, final int nationId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final List<ShipTypeDTO> shipTypeList = new ArrayList<ShipTypeDTO>();

        // Remove special ship types
        for (final ShipType shipTPE : shipTypeManager.list()) {
            // Make sure that Indiamen are only available to Great Britain and Holland
            if ((shipTPE.getIntId() == 31)
                    && (nationId != NationConstants.NATION_GREATBRITAIN)
                    && (nationId != NationConstants.NATION_HOLLAND)) {

                continue;
            }

            // Make sure that corsairs and dhows are only available to Morocco, Egypt and Ottomans
            if (((shipTPE.getIntId() == 11)
                    || (shipTPE.getIntId() == 12)
                    || (shipTPE.getIntId() == 24)
                    || (shipTPE.getIntId() == 25))
                    && (nationId != NationConstants.NATION_MOROCCO)
                    && (nationId != NationConstants.NATION_OTTOMAN)
                    && (nationId != NationConstants.NATION_EGYPT)
                    ) {
                continue;
            }

            shipTypeList.add(ShipTypeConverter.convert(shipTPE));
        }
        return shipTypeList;
    }

    /**
     * Retrieve all sectors for a particular region.
     *
     * @param id       the identity of the Region.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @param nationId the identity of the nation.
     * @return a list of sectors with the associated production sites and terrain objects.
     */
    @ClientCachable(gameId = 2, nationId = 4)
    public TilesCollection getRegionByGameAndNation(final int scenarioId, final int id,
                                                    final int gameId,
                                                    final int turn,
                                                    final int nationId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game thisGame = getGame(scenarioId, gameId);
        final TilesCollection tilesCollection = new TilesCollection();
        final List<SectorDTO> regionSectorDTO = new ArrayList<SectorDTO>();
        final List<Region> lstRegion = regionManager.list();
        for (Region thisRegion : lstRegion) {
            final List<Sector> sectorList = sectorManager.listByGameRegion(thisGame, thisRegion);
            final List<SectorDTO> sectorDTO = SectorConverter.convert(sectorList, nationId, thisGame.isFogOfWar(), thisGame);
            tilesCollection.setUpRegion(scenarioId, thisRegion.getId(), sectorDTO);
            regionSectorDTO.addAll(sectorDTO);
        }

        //get for turn-1 since you want to see the battles that happened in the previous turn
        tilesCollection.setBattleToPositions(getNavalTacticalBattlesPositions(scenarioId, nationId, gameId, turn - 1));

        //	Get all orders for this turn
        final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, ApplyEconomyChangesProcessor.ORDERS_TYPES);

        // Initialize an economy changes processor
        final ApplyEconomyChangesProcessor eCProc = new ApplyEconomyChangesProcessor(scenarioId, gameId, nationId, turn);

        // Add the orders to the processor
        eCProc.addData(regionSectorDTO, orders);

        // Process the changes on the ps list and add them to the region data
        tilesCollection.setRegionBuildDemPrSitesMap(eCProc.processChanges());

        return tilesCollection;
    }

    @ClientCachable(gameId = 0, nationId = 1)
    public List<NationsRelation> listNationRelation(final Game thisGame, final Nation thisNation) {
        return relationsManager.listByGameNation(thisGame, thisNation);
    }

    @GameCachable()
    public Map<Integer, Map<Integer, Integer>> mapNationRelation(final Game thisGame) {
        final Map<Integer, Map<Integer, Integer>> mapRelations = new HashMap<Integer, Map<Integer, Integer>>();
        final List<Nation> lstNations = nationManager.list();
        for (final Nation nation : lstNations) {
            final List<NationsRelation> lstRelations = listNationRelation(thisGame, nation);
            final Map<Integer, Integer> nationRelations = new HashMap<Integer, Integer>();
            for (final NationsRelation relation : lstRelations) {
                nationRelations.put(relation.getTarget().getId(), relation.getRelation());
            }
            mapRelations.put(nation.getId(), nationRelations);
        }

        return mapRelations;
    }

    /**
     * Retrieve NationsRelations for a specific game, nation (player) and region of the map.
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @return a list of EmpireRelationDTO objects.
     */
    @ClientCachable(gameId = 1, nationId = 2)
    public List<RelationDTO> getNationsRelations(final int scenarioId, final int gameId, final int nationId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Nation thisNation = getNation(scenarioId, nationId);
        final Game thisGame = getGame(scenarioId, gameId);

        // Retrieve relations and make sure they are sorted properly
        // Fix for ticket:1667
        final List<NationsRelation> relations = listNationRelation(thisGame, thisNation);
        Collections.sort(relations, new TargetNationId());

        final List<RelationDTO> empireRelationsDTOs = RelationConverter.convert(relations, relationsManager);

        // Check call to allies
        for (RelationDTO targetNation : empireRelationsDTOs) {
            // Check if we declare war due to a call to Allies
            final List<Report> lstReports = reportManager.listByOwnerTurnKey(thisNation, thisGame, turn - 1,
                    "callallies." + targetNation.getTargetNationId());

            targetNation.setCalledToAllies(!lstReports.isEmpty());
        }

        //	Get all orders for this turn
        final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, PoliticsApplyChangesProcessor.ORDERS_TYPES);

        // Initialize a changes processor
        final PoliticsApplyChangesProcessor pcProccessor = new PoliticsApplyChangesProcessor(scenarioId, gameId, nationId, turn);

        // Add the relations and orders to the processor
        pcProccessor.addData(empireRelationsDTOs, orders);

        // Return the processed relations list
        return pcProccessor.processChanges();
    }

    @ClientCachable(cacheName = "gameCache", gameId = 1, nationId = 2)
    public List<SectorDTO> getNationSectors(final int scenarioId, final int gameId, final int nationId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Nation thisNation = getNation(scenarioId, nationId);
        final Game thisGame = getGame(scenarioId, gameId);
        return SectorConverter.convert(sectorManager.listByGameNation(thisGame, thisNation));
    }

    /**
     * Retrieve the warehouse information for a specific game and nation (player).
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @return a list of EmpireRelationDTO objects.
     */
    @ClientCachable(cacheName = "gameCache", gameId = 1, nationId = 2)
    public List<WarehouseDTO> getWarehouses(final int scenarioId, final int gameId, final int nationId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Nation thisNation = getNation(scenarioId, nationId);
        final Game thisGame = getGame(scenarioId, gameId);
        return WarehouseConverter.convert(warehouseManager.listByGameNation(thisGame, thisNation), goodManager);
    }

    /**
     * Retrieve all the Sectors owned by a particular nation that have a barrack or shipyard.
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @return a list of sectors.
     */
    @ClientCachable(gameId = 2, nationId = 1)
    public List<BarrackDTO> getBarracksByNation(final int scenarioId, final int nationId,
                                                final int gameId,
                                                final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Nation thisNation = getNation(scenarioId, nationId);
        final Game thisGame = getGame(scenarioId, gameId);
        final List<Barrack> barrackList = barrackManager.listByGameNation(thisGame, thisNation);
        final List<BarrackDTO> dbBarracks = BarrackConverter.convert(barrackList);
        final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, BarrackApplyChangesProcessor.ORDERS_TYPES);

        // Initialize a changes processor
        final BarrackApplyChangesProcessor brProc = new BarrackApplyChangesProcessor(scenarioId, gameId, nationId, turn);

        // process player orders
        brProc.addData(dbBarracks, orders);

        return brProc.processChanges();
    }

    /**
     * Retrieve ships for a specific game, nation (player) and region of the map.
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @return a EmpireShipsCollection object.
     */
    @ClientCachable(gameId = 2, nationId = 1)
    public Map<Boolean, List<FleetDTO>> getFleetsByNation(final int scenarioId, final int nationId,
                                                          final int gameId,
                                                          final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game thisGame = getGame(scenarioId, gameId);
        final Nation thisNation = getNation(scenarioId, nationId);

        // Add all fleets and their ships
        final List<FleetDTO> fleetDtoList = FleetConverter.convert(fleetManager.listGameNation(thisGame, thisNation), shipManager, goodManager);

        // We also need to add the ships that are not in a fleet
        final FleetDTO emptyFleet = new FleetDTO();
        emptyFleet.setFleetId(0);
        emptyFleet.setShips(ShipConverter.convertToMap(shipManager.listFreeByGameNation(thisGame, thisNation), goodManager));
        fleetDtoList.add(emptyFleet);

        //	Get all orders for this turn
        final List<OrderDTO> orders1 = getOrders(scenarioId, gameId, nationId, turn, NavyApplyChangesProcessor.ORDERS_TYPES);

        List<NationsRelation> relations = relationsManager.listAlliesByGameNation(thisGame, thisNation);
        for (NationsRelation relation : relations) {
            if (relation.getTarget().getId() != thisNation.getId()) {
                orders1.addAll(getOrders(scenarioId, gameId, relation.getTarget().getId(), turn, NavyApplyChangesProcessor.ORDERS_TYPES_ALLIED));
                orders1.addAll(getOrders(scenarioId, gameId, relation.getTarget().getId(), turn, NavyApplyChangesProcessor.ORDERS_TYPES_CURRENT_FOR_ALLIES));
            }
        }

        // Initialize a changes processor
        final NavyApplyChangesProcessor aCProc = new NavyApplyChangesProcessor(scenarioId, gameId, nationId, turn);

        // Add the fleets and orders to the processor
        aCProc.addData(fleetDtoList, orders1);

        // Process the changes on the armies list
        aCProc.processChanges();

        // Initialize a trade ship changes changes processor
        final TradeShipApplyChangesProcessor tsCProc = new TradeShipApplyChangesProcessor(scenarioId, gameId, nationId, turn);

        //	Get all orders for this turn
        final List<OrderDTO> orders2 = getOrders(scenarioId, gameId, nationId, turn, TradeShipApplyChangesProcessor.ORDERS_TYPES);
        for (NationsRelation relation : relations) {
            if (relation.getTarget().getId() != thisNation.getId()) {
                orders2.addAll(getOrders(scenarioId, gameId, relation.getTarget().getId(), turn, TradeShipApplyChangesProcessor.ORDERS_TYPES));
            }
        }

        // Add the orders to the processor
        tsCProc.addData(fleetDtoList, orders2);
        tsCProc.addData(aCProc.getIdShipMap(), null);

        final Map<Boolean, List<FleetDTO>> out = new HashMap<Boolean, List<FleetDTO>>();
        out.put(true, tsCProc.processChanges());
        out.put(false, new ArrayList<FleetDTO>(aCProc.getIdDeletedFleetMap().values()));
        // Process the changes on the trade ships
        return out;
    }

    /**
     * Retrieve ships for a specific game, nation (player) and region of the map.
     *
     * @param nationId         the identity of the nation.
     * @param regionId         the identity of the region.
     * @param gameId           the identity of the game.
     * @param turn             the turn of the game.
     * @param isAllied         also include allied items.
     * @param originalNationId the original nation id before any handover.
     * @return a EmpireShipsCollection object.
     */
    @ClientCachable(gameId = 3, nationId = 1)
    public List<FleetDTO> getFleetsByNationRegion(final int scenarioId, final int nationId,
                                                  final int regionId,
                                                  final int gameId,
                                                  final int turn,
                                                  final boolean isAllied,
                                                  final int originalNationId,
                                                  final boolean visible) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game thisGame = getGame(scenarioId, gameId);
        final Nation thisNation = getNation(scenarioId, nationId);
        final Region thisRegion = getRegion(scenarioId, regionId);

        // Add all fleets and their ships
        final List<FleetDTO> fleetDtoList = FleetConverter.convert(fleetManager.listGameNationRegion(thisGame, thisNation, thisRegion),
                shipManager, goodManager);

        // We also need to add the ships that are not in a fleet
        final FleetDTO emptyFleet = new FleetDTO();
        emptyFleet.setFleetId(0);
        emptyFleet.setShips(ShipConverter.convertToMap(shipManager.listFreeByGameNationRegion(thisGame, thisNation, thisRegion),
                goodManager));
        fleetDtoList.add(emptyFleet);
        if (visible) {
            //	Get all orders for this turn
            final List<OrderDTO> orders1 = getOrders(scenarioId, gameId, nationId, turn, NavyApplyChangesProcessor.ORDERS_TYPES);
            //be sure to calculate also the repair for allied ships/fleets
            if (isAllied) {
                orders1.addAll(getOrders(scenarioId, gameId, originalNationId, turn, NavyApplyChangesProcessor.ORDERS_TYPES_ALLIED));
                orders1.addAll(getOrders(scenarioId, gameId, originalNationId, turn, NavyApplyChangesProcessor.ORDERS_TYPES_CURRENT_FOR_ALLIES));
            }
            // Initialize a changes processor
            final NavyApplyChangesProcessor aCProc = new NavyApplyChangesProcessor(scenarioId, gameId, nationId, turn);

            // Add the fleets and orders to the processor
            aCProc.addData(fleetDtoList, orders1);

            // Process the changes on the armies list
            aCProc.processChanges();

            // Initialize a trade ship changes changes processor
            final TradeShipApplyChangesProcessor tsCProc = new TradeShipApplyChangesProcessor(scenarioId, gameId, nationId, turn);

            //	Get all orders for this turn
            final List<OrderDTO> orders2 = getOrders(scenarioId, gameId, nationId, turn, TradeShipApplyChangesProcessor.ORDERS_TYPES);
            if (isAllied) {
                orders2.addAll(getOrders(scenarioId, gameId, originalNationId, turn, TradeShipApplyChangesProcessor.ORDERS_TYPES));
            }
            // Add the orders to the processor
            tsCProc.addData(fleetDtoList, orders2);
            tsCProc.addData(aCProc.getIdShipMap(), null);

            // Process the changes on the trade ships
            return tsCProc.processChanges();
        } else if (isAllied) {
            //	Get all orders for this turn
            final List<OrderDTO> orders1 = new ArrayList<OrderDTO>();
            //be sure to calculate also the repair for allied ships/fleets

            orders1.addAll(getOrders(scenarioId, gameId, originalNationId, turn, NavyApplyChangesProcessor.ORDERS_TYPES_ALLIED));
            orders1.addAll(getOrders(scenarioId, gameId, originalNationId, turn, NavyApplyChangesProcessor.ORDERS_TYPES_CURRENT_FOR_ALLIES));

            // Initialize a changes processor
            final NavyApplyChangesProcessor aCProc = new NavyApplyChangesProcessor(scenarioId, gameId, nationId, turn);

            // Add the fleets and orders to the processor
            aCProc.addData(fleetDtoList, orders1);

            // Process the changes on the armies list
            aCProc.processChanges();

            // Initialize a trade ship changes changes processor
            final TradeShipApplyChangesProcessor tsCProc = new TradeShipApplyChangesProcessor(scenarioId, gameId, nationId, turn);

            //	Get all orders for this turn
            final List<OrderDTO> orders2 = new ArrayList<OrderDTO>();
            orders2.addAll(getOrders(scenarioId, gameId, originalNationId, turn, TradeShipApplyChangesProcessor.ORDERS_TYPES));

            // Add the orders to the processor
            tsCProc.addData(fleetDtoList, orders2);
            tsCProc.addData(aCProc.getIdShipMap(), null);

            // Process the changes on the trade ships
            return tsCProc.processChanges();
        } else {

            return fleetDtoList;
        }

    }

    /**
     * Retrieve Armies for a specific game, nation (player).
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @return a list of EmpireArmyDTO objects.
     */
    @ClientCachable(gameId = 2, nationId = 1)
    public ArmiesAndCommanders getArmiesByNation(final int scenarioId, final int nationId, final int gameId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game thisGame = getGame(scenarioId, gameId);
        final Nation thisNation = getNation(scenarioId, nationId);
        final List<Brigade> brigadeList = brigadeManager.listFreeByGameNation(thisGame, thisNation);
        final List<Corp> corpList = corpManager.listFreeByGameNation(thisGame, thisNation);
        final List<ArmyDTO> empireArmyDTOList = ArmyConverter.convert(armyManager.listGameNation(thisGame, thisNation));

        // Add Brigades without a Corp
        final CorpDTO freeCorpDTO = new CorpDTO();
        freeCorpDTO.setCorpId(0);
        freeCorpDTO.setArmyId(0);

        final Map<Integer, BrigadeDTO> brigadeDTOList = new HashMap<Integer, BrigadeDTO>();

        // Include Brigades of this Corp
        for (final Brigade brigade : brigadeList) {
            final BrigadeDTO brigadeDTO = BrigadeConverter.convert(brigade);
            brigadeDTOList.put(brigadeDTO.getBrigadeId(), brigadeDTO);
        }

        // Set Brigade list
        freeCorpDTO.setBrigades(brigadeDTOList);

        // Add Corps without an army and Brigades without a Corp
        final ArmyDTO freeDTO = new ArmyDTO();
        freeDTO.setArmyId(0);

        final Map<Integer, CorpDTO> freeCorpDTOMap = new HashMap<Integer, CorpDTO>();

        // Include Corps of this Army
        for (final Corp corp : corpList) {
            freeCorpDTOMap.put(corp.getCorpId(), CorpConverter.convert(corp));
        }

        // Also add free Brigades
        freeCorpDTOMap.put(freeCorpDTO.getCorpId(), freeCorpDTO);

        // Set Corp list
        freeDTO.setCorps(freeCorpDTOMap);

        // Add army in the list
        empireArmyDTOList.add(freeDTO);

        //	Get all orders for this turn
        final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, ArmyApplyChangesProcessor.ORDERS_TYPES);

        // Initialize a changes processor
        final ArmyApplyChangesProcessor aCProc = new ArmyApplyChangesProcessor(scenarioId, gameId, nationId, turn, this, 0, false);

        // Add the armies and orders to the processor
        aCProc.addData(empireArmyDTOList, orders);

        // Process the changes on the armies list
        final List<ArmyDTO> armies = aCProc.processChanges();

        // Initialize a changes processor
        final CommanderApplyChangesProcessor cCProc = new CommanderApplyChangesProcessor(scenarioId, gameId, nationId, turn);

        // Add the armies and orders to the processor
        cCProc.addData(getCommandersByGameNation(scenarioId, gameId, nationId), orders);
        cCProc.addData(aCProc.getArmiesMap(), null);
        cCProc.addExtraData(aCProc.getDeletedArmies(), aCProc.getDeletedCorps());
        // Process the changes on the commanders list
        final List<CommanderDTO> commanders = cCProc.processChanges();

        final List<CommanderDTO> capturedCommanders = CommanderConverter.convert(commanderManager.listCapturedByGameNation(thisGame, thisNation));

        // Return the processed army list
        return new ArmiesAndCommanders(armies, commanders, aCProc.getMergedBatts(), new ArrayList<ArmyDTO>(aCProc.getDeletedArmies().values()), new ArrayList<CorpDTO>(aCProc.getDeletedCorps().values()), capturedCommanders);
    }

    /**
     * Retrieve Armies for a specific game, nation (player).
     *
     * @param nationId the identity of the nation.
     * @param regionId the identity of the region.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @return a list of EmpireArmyDTO objects.
     */
    @ClientCachable(gameId = 3, nationId = 1)
    public ArmiesAndCommanders getArmiesByNationRegion(final int scenarioId, final int nationId,
                                                       final int regionId,
                                                       final int gameId,
                                                       final int turn,
                                                       final boolean visible) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game thisGame = getGame(scenarioId, gameId);
        final Nation thisNation = getNation(scenarioId, nationId);
        final Region thisRegion = getRegion(scenarioId, regionId);

        final List<Brigade> brigadeList = brigadeManager.listFreeByGameNationRegion(thisGame, thisNation, thisRegion);
        final List<Corp> corpList = corpManager.listFreeByGameNationRegion(thisGame, thisNation, thisRegion);
        final List<ArmyDTO> empireArmyDTOList = ArmyConverter.convert(armyManager.listGameNationRegion(thisGame, thisNation, thisRegion));

        // Add Brigades without a Corp
        final CorpDTO freeCorpDTO = new CorpDTO();
        freeCorpDTO.setCorpId(0);
        freeCorpDTO.setArmyId(0);

        final Map<Integer, BrigadeDTO> brigadeDTOList = new HashMap<Integer, BrigadeDTO>();

        // Include Brigades of this Corp
        for (final Brigade brigade : brigadeList) {
            final BrigadeDTO brigadeDTO = BrigadeConverter.convert(brigade);
            brigadeDTOList.put(brigadeDTO.getBrigadeId(), brigadeDTO);
        }

        // Set Brigade list
        freeCorpDTO.setBrigades(brigadeDTOList);

        // Add Corps without an army and Brigades without a Corp
        final ArmyDTO freeDTO = new ArmyDTO();
        freeDTO.setArmyId(0);

        final Map<Integer, CorpDTO> freeCorpDTOMap = new HashMap<Integer, CorpDTO>();

        // Include Corps of this Army
        for (final Corp corp : corpList) {
            freeCorpDTOMap.put(corp.getCorpId(), CorpConverter.convert(corp));
        }

        // Also add free Brigades
        freeCorpDTOMap.put(freeCorpDTO.getCorpId(), freeCorpDTO);

        // Set Corp list
        freeDTO.setCorps(freeCorpDTOMap);

        // Add army in the list
        empireArmyDTOList.add(freeDTO);
        if (visible) {
            //	Get all orders for this turn
            final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, ArmyApplyChangesProcessor.ORDERS_TYPES);

            // Initialize a changes processor
            final ArmyApplyChangesProcessor aCProc = new ArmyApplyChangesProcessor(scenarioId, gameId, nationId, turn, this, regionId, true);

            // Add the armies and orders to the processor
            aCProc.addData(empireArmyDTOList, orders);

            // Process the changes on the armies list
            final List<ArmyDTO> armies = aCProc.processChanges();

            // Initialize a changes processor
            final CommanderApplyChangesProcessor cCProc = new CommanderApplyChangesProcessor(scenarioId, gameId, nationId, turn);

            // Add the armies and orders to the processor
            cCProc.addData(getCommandersByGameNation(scenarioId, gameId, nationId), orders);
            cCProc.addData(aCProc.getArmiesMap(), null);

            // Process the changes on the commanders list
            final List<CommanderDTO> commanders = cCProc.processChanges();
            // Return the processed army list
            return new ArmiesAndCommanders(armies, commanders, aCProc.getMergedBatts(), new ArrayList<ArmyDTO>(aCProc.getDeletedArmies().values()), new ArrayList<CorpDTO>(aCProc.getDeletedCorps().values()), new ArrayList<CommanderDTO>());
        } else {
            return new ArmiesAndCommanders(empireArmyDTOList, getCommandersByGameNation(scenarioId, gameId, nationId), new ArrayList<BattalionDTO>(), new ArrayList<ArmyDTO>(), new ArrayList<CorpDTO>(), new ArrayList<CommanderDTO>());
        }

    }

    /**
     * Calculate the supply lines for the given scenario, game, player.
     *
     * @param scenarioId the scenario of the game.
     * @param gameId     the identity of the game.
     * @param regionId   the region of the game.
     * @param nationId   the identity of the nation.
     * @return a set of positions that are within supply lines reach.
     */
    @ClientCachable(cacheName = "gameCache", gameId = 1, nationId = 3)
    public Set<PositionDTO> getSupplyLines(final int scenarioId, final int gameId, final int regionId, final int nationId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Set<PositionDTO> suppliedSectors = new HashSet<PositionDTO>();
        final Set<Sector> supplySources = new HashSet<Sector>();
        final Game thisGame = getGame(scenarioId, gameId);
        final Region thisRegion = getRegion(scenarioId, regionId);
        final Nation thisNation = getNation(scenarioId, nationId);

        if (thisRegion == null || thisNation == null) {
            return suppliedSectors;
        }

        LOGGER.debug("Requesting Supply Lines for " + gameId + "/" + thisRegion.getName() + "/" + thisNation.getName());

        // Setup an offline engine
        final OfflineEngine engine = new OfflineEngine(thisGame, nationManager.list());

        // An instance of the distance calculator for each region.
        final Map<Region, DistanceCalculator> distCalc = new HashMap<Region, DistanceCalculator>();
        distCalc.put(thisRegion, new DistanceCalculator(thisGame, thisRegion, thisNation,
                relationsManager, sectorManager, battalionManager));

        final Map<Integer, PatrolOrderDetails> emptyPatrols = new HashMap<Integer, PatrolOrderDetails>();
        final SupplyLinesConnectivity slc = new SupplyLinesConnectivity(engine,
                thisNation, distCalc, emptyPatrols,
                relationsManager, sectorManager, regionManager, barrackManager, tradeCityManager);

        // Start by supplying the barracks
        slc.setupSupplyLines();

        // Retrieve all supply sources
        int maxX = 0, minX = Integer.MAX_VALUE, maxY = 0, minY = Integer.MAX_VALUE;
        final Map<Region, List<Sector>> suppliedBarracks = slc.getBarracksInSupply();
        for (final Sector thisSector : suppliedBarracks.get(thisRegion)) {
            final PositionDTO suppliedPos = new PositionDTO();
            suppliedPos.setRegionId(regionId);
            suppliedPos.setX(thisSector.getPosition().getX());
            suppliedPos.setY(thisSector.getPosition().getY());

            if (thisSector.getPosition().getRegion().getId() == EUROPE
                    && thisSector.getNation().getId() == thisNation.getId()
                    && getSphere(thisSector, thisNation) == 1) {
                suppliedPos.setXStart(1);
            }

            if (thisSector.getNation().getId() == thisNation.getId() && thisSector.hasBarrack()) {
                suppliedPos.setYStart(1);
            }

            supplySources.add(thisSector);
            suppliedSectors.add(suppliedPos);
            maxX = java.lang.Math.max(thisSector.getPosition().getX(), maxX);
            minX = java.lang.Math.min(thisSector.getPosition().getX(), minX);
            maxY = java.lang.Math.max(thisSector.getPosition().getY(), maxY);
            minY = java.lang.Math.min(thisSector.getPosition().getY(), minY);
        }

        LOGGER.debug("Identified " + suppliedSectors.size() + " supply sources");

        // Evaluate all other sectors
        final List<Sector> lstSectors = sectorManager.listLandByGameRegion(thisGame, thisRegion, minX - 10, minY - 10, maxX + 10, maxY + 10);
        for (final Sector thisSector : lstSectors) {
            if (supplySources.contains(thisSector)) {
                continue;
            }

            boolean inSupply;

            // Exclude supply check for units in any home nation (of the same nation as the unit)
            // controlled (by the owner of the unit) sectors.
            inSupply = (thisSector.getPosition().getRegion().getId() == EUROPE && thisSector.getNation().getId() == thisNation.getId() && getSphere(thisSector, thisNation) == 1)
                    || slc.checkSupply(thisSector);

            if (inSupply) {
                final PositionDTO suppliedPos = new PositionDTO();
                suppliedPos.setRegionId(regionId);
                suppliedPos.setX(thisSector.getPosition().getX());
                suppliedPos.setY(thisSector.getPosition().getY());

                if (thisSector.getPosition().getRegion().getId() == EUROPE
                        && thisSector.getNation().getId() == thisNation.getId()
                        && getSphere(thisSector, thisNation) == 1) {
                    suppliedPos.setXStart(1);
                }

                if (thisSector.getNation().getId() == thisNation.getId() && thisSector.hasBarrack()) {
                    suppliedPos.setYStart(1);
                }

                suppliedSectors.add(suppliedPos);
            }
        }

        LOGGER.debug("Reporting " + suppliedSectors.size() + " sectors reachable by supply lines");
        return suppliedSectors;
    }

    /**
     * Calculate the possible movements of an army.
     *
     * @param scenarioId the scenario of the game.
     * @param gameId     the identity of the game.
     * @param nationId   the identity of the nation.
     * @param startX     the startX coordinate of the army
     * @param startY     the startY coordinate of the army
     * @param mpAvail    movement points available
     * @param regionId   the region of the movement.
     * @param unitType   the type of the unit.
     * @param warShips   the number of warships in case of sea movement.
     * @return a list of movement paths.
     */
    @ClientCachable(cacheName = "gameCache", gameId = 1, nationId = 2)
    public Set<PathDTO> getPossibleMovementTiles(final int scenarioId, final int gameId, final int nationId,
                                                 final int startX, final int startY,
                                                 final int mpAvail,
                                                 final int regionId,
                                                 final int unitType,
                                                 final int conqueredSectors,
                                                 final int conqueredNeutralSectors,
                                                 final int warShips,
                                                 final List<Integer> otherNations) {
        final long timeStamp = System.currentTimeMillis();
        ScenarioContextHolder.setScenario(scenarioId);
        final Game thisGame = getGame(scenarioId, gameId);
        final Region thisRegion = getRegion(scenarioId, regionId);

        // Custom Game: Full Mps at Colonies
        final boolean isColonies = thisRegion.getId() != EUROPE && !thisGame.isFullMpsAtColonies();

        // SEA Sectors cost 1 MP to move
        int totSectors = mpAvail;
        if ((unitType != SHIP) && (unitType != FLEET)) {
            // The easiest terrain type for Land units costs 4 MP to move.
            totSectors /= 4d;

            // Land units at the Colonies cost twice the MP to move.
            if (isColonies) {
                totSectors /= 2d;
            }
        }

        // Put upper bound on total sectors.
        if (totSectors > 10) {
            totSectors = 10;
        }

        if (totSectors < 0) {
            totSectors = 1;
        }

        final int[] regionSizeX, regionSizeY;
        switch (scenarioId) {
            case HibernateUtil.DB_FREE:
                regionSizeX = RegionConstants.REGION_1804_SIZE_X;
                regionSizeY = RegionConstants.REGION_1804_SIZE_Y;
                break;

            case HibernateUtil.DB_S3:
                regionSizeX = RegionConstants.REGION_1808_SIZE_X;
                regionSizeY = RegionConstants.REGION_1808_SIZE_Y;
                break;

            case HibernateUtil.DB_S1:
            case HibernateUtil.DB_S2:
            default:
                regionSizeX = RegionConstants.REGION_1805_SIZE_X;
                regionSizeY = RegionConstants.REGION_1805_SIZE_Y;
                break;
        }

        // Determine boundaries
        final int maxX = Math.min(regionSizeX[regionId - 1] - 1, startX + totSectors);
        final int minX = Math.max(0, startX - totSectors);
        final int maxY = Math.min(regionSizeY[regionId - 1] - 1, startY + totSectors);
        final int minY = Math.max(0, startY - totSectors);

        LOGGER.debug("Requesting paths for " + minX + ".." + maxX + "/" + minY + ".." + maxY
                + " tot MP: " + mpAvail + " max Path: " + totSectors
                + " cS:" + conqueredSectors
                + " cNS:" + conqueredNeutralSectors
                + " wS:" + warShips
                + " oN:" + otherNations);

        final List<Sector> sectorList = sectorManager.listByGameRegion(thisGame, thisRegion, minX, minY, maxX, maxY);

        LOGGER.debug("Total sectors retrieved " + sectorList.size());

        // Setup array
        final SectorDTO[][] mapSector = new SectorDTO[totSectors * 2 + 3][totSectors * 2 + 3];

        int actualMinX = Integer.MAX_VALUE;
        int actualMaxX = 0;
        int actualMinY = Integer.MAX_VALUE;
        int actualMaxY = 0;

        // identify if severe weather is in effect
        final Nation freeNation = getFreeNation(scenarioId);
        final boolean hasArctic = getReport(scenarioId, thisGame, freeNation, "winter.arctic").equals("1");
        final boolean hasCentral = getReport(scenarioId, thisGame, freeNation, "winter.central").equals("1");
        final boolean hasMediterranean = getReport(scenarioId, thisGame, freeNation, "winter.mediterranean").equals("1");

        // Iterate through all sectors
        for (final Sector sector : sectorList) {
            final int coordX = sector.getPosition().getX() - minX + 1;
            final int coordY = sector.getPosition().getY() - minY + 1;
            mapSector[coordX][coordY] = SectorConverter.convert(sector);

            if (unitType == SHIP || unitType == FLEET) {
                // Fleets+Ships require 1MP.
                mapSector[coordX][coordY].getTerrain().setActualMPs(1);

                // Check if sector is affected by storm
                if (sector.getStorm() > 0) {
                    // It will cost one extra movement point per storm coordinate passing through
                    mapSector[coordX][coordY].getTerrain().setActualMPs(mapSector[coordX][coordY].getTerrain().getActualMPs() + 1);
                }

            } else {
                // Keep the original costs and apply modifiers
                if (isWinter(scenarioId, mapSector[coordX][coordY], hasArctic, hasCentral, hasMediterranean)) {
                    mapSector[coordX][coordY].getTerrain().setActualMPs(sector.getTerrain().getMpsWinter());

                } else {
                    mapSector[coordX][coordY].getTerrain().setActualMPs(sector.getTerrain().getMps());
                }

                if (isColonies && sector.getTerrain().getId() != TerrainConstants.TERRAIN_O) {
                    mapSector[coordX][coordY].getTerrain().setActualMPs(mapSector[coordX][coordY].getTerrain().getActualMPs() * 2);
                }
            }

            actualMinX = Math.min(actualMinX, sector.getPosition().getX());
            actualMaxX = Math.max(actualMaxX, sector.getPosition().getX());
            actualMinY = Math.min(actualMinY, sector.getPosition().getY());
            actualMaxY = Math.max(actualMaxY, sector.getPosition().getY());
        }

        LOGGER.debug("Retrieved sector array [" + (mapSector.length) + "x" + (mapSector.length) + "] for " + actualMinX + ".." + actualMaxX + "/" + actualMinY + ".." + actualMaxY + " {" + hasArctic + "," + hasCentral + "," + hasMediterranean + "}");
        LOGGER.debug("Total sectors retrieved " + sectorList.size());

        final long timeStampSectors = System.currentTimeMillis();
        final Map<Integer, Map<Integer, Integer>> relationsMap = mapNationRelation(thisGame);

        // Construct shortest path
        final MovementShortestPath mvSp = new MovementShortestPath(thisGame, mapSector,
                startX, startY,
                actualMinX, actualMinY,
                unitType, nationId,
                conqueredNeutralSectors, conqueredSectors, warShips, otherNations,
                relationsMap);

        final long timeStampMSP = System.currentTimeMillis();

        // Return all shortest paths
        final Set<PathDTO> response = mvSp.getAllPaths(thisGame, nationId, mpAvail, totSectors, relationsMap);
        for (PathDTO path : response) {
            path.setRegionId(regionId);
        }
        final String strUnitType;
        switch (unitType) {
            case ARMY:
                strUnitType = "army";
                break;

            case CORPS:
                strUnitType = "corps";
                break;

            case BRIGADE:
                strUnitType = "brigade";
                break;

            case SPY:
                strUnitType = "spy";
                break;

            case COMMANDER:
                strUnitType = "commander";
                break;

            case BAGGAGETRAIN:
                strUnitType = "train";
                break;

            case SHIP:
                strUnitType = "ship";
                break;

            case FLEET:
                strUnitType = "fleet";
                break;

            default:
                strUnitType = "unknown";
        }

        final long timeStampTOTAL = System.currentTimeMillis();

        LOGGER.info("MovementTime [" + strUnitType + "] Avail MP="
                + mpAvail
                + " / Tot Sectors="
                + totSectors
                + " / Tot Paths="
                + response.size()
                + " / Total response="
                + (timeStampTOTAL - timeStamp) + " ms"
                + " / DB="
                + (timeStampSectors - timeStamp) + " ms"
                + " / MSP="
                + (timeStampMSP - timeStampSectors) + " ms"
                + " / Paths="
                + (timeStampTOTAL - timeStampMSP) + " ms");
        return response;
    }

    /**
     * Determine if it is winter for this particular sector.
     *
     * @param thisSector       the sector to examine.
     * @param hasArctic        If arctic zone suffers from severe winter.
     * @param hasCentral       If central zone suffers from severe winter.
     * @param hasMediterranean If mediterranean zone suffers from severe winter.
     * @return true if the sector should be painted with winter colors.
     */
    private boolean isWinter(final int scenarioId, final SectorDTO thisSector, final boolean hasArctic, final boolean hasCentral, final boolean hasMediterranean) {
        ScenarioContextHolder.setScenario(scenarioId);
        switch (thisSector.getRegionId()) {
            case EUROPE:
                if (thisSector.getY() <= 10) {
                    return hasArctic;

                } else if ((thisSector.getY() >= 11) && (thisSector.getY() <= 35)) {
                    return hasCentral;

                } else {
                    return hasMediterranean;
                }

            case CARIBBEAN:
            case INDIES:
            case AFRICA:
            default:
                return false;
        }
    }

    /**
     * Retrieve the commanders for a given game and nation.
     *
     * @param gameId   the identity of the game.
     * @param nationId the identity of the nation.
     * @return a list of Commander objects.
     */
    @ClientCachable(cacheName = "gameCache", gameId = 1, nationId = 2)
    public List<CommanderDTO> getCommandersByGameNation(final int scenarioId, final int gameId, final int nationId) {
        ScenarioContextHolder.setScenario(scenarioId);
        try {
            final Game thisGame = getGame(scenarioId, gameId);
            final Nation thisNation = getNation(scenarioId, nationId);
            final List<CommanderDTO> commanderList = new ArrayList<CommanderDTO>();
            final List<Commander> commanders = commanderManager.listGameNation(thisGame, thisNation);
            for (Commander commander : commanders) {
                commanderList.add(CommanderConverter.convert(commander));
            }
            return commanderList;

        } catch (Exception ex) {
            LOGGER.error("Error Retrieving Commanders", ex);
            return null;
        }
    }

    @ClientCachable(cacheName = "gameCache", gameId = 3, nationId = 2)
    public List<BaggageTrainDTO> getBaggageTrainsBySector(final int scenarioId, final int sectorId, final int nationId, final int gameId) {
        ScenarioContextHolder.setScenario(scenarioId);
        try {
            final Sector sector = sectorManager.getByID(sectorId);
            return BaggageTrainConverter.convert(baggageTrainManager.listAllByPosition(sector.getPosition()), goodManager);

        } catch (Exception ex) {
            LOGGER.error("Error Retrieving Baggage Trains", ex);
            return new ArrayList<BaggageTrainDTO>();
        }
    }

    public ShipDTO getShipById(final int scenarioId, final int shipId) {
        ScenarioContextHolder.setScenario(scenarioId);
        return ShipConverter.convert(shipManager.getByID(shipId), goodManager);
    }

    public BaggageTrainDTO getBaggageTrainById(final int scenarioId, final int bTrainId) {
        ScenarioContextHolder.setScenario(scenarioId);
        return BaggageTrainConverter.convert(baggageTrainManager.getByID(bTrainId), goodManager);
    }

    public List<FleetDTO> getFleetsBySector(final int scenarioId, final int sectorId) {
        ScenarioContextHolder.setScenario(scenarioId);

        try {
            // Add all fleets and their ships
            final Sector sector = sectorManager.getByID(sectorId);
            final List<FleetDTO> fleetDtoList = FleetConverter.convert(fleetManager.listAllByPosition(sector.getPosition()), shipManager, goodManager);

            // We also need to add the ships that are not in a fleet
            final FleetDTO emptyFleet = new FleetDTO();
            emptyFleet.setFleetId(0);
            emptyFleet.setShips(ShipConverter.convertToMap(shipManager.listAllBySector(sector.getPosition()), goodManager));

            //if there are free ships create the zero fleet.. otherwise there is no need to do that...
            if (emptyFleet.getShips().size() > 0) {
                fleetDtoList.add(emptyFleet);
            }

            return fleetDtoList;

        } catch (Exception ex) {
            return new ArrayList<FleetDTO>();
        }
    }

    public List<ArmyDTO> getArmiesBySector(final int scenarioId, final int sectorId) {
        ScenarioContextHolder.setScenario(scenarioId);
        try {
            final Sector sector = sectorManager.getByID(sectorId);
            final List<BrigadeDTO> brigades = BrigadeConverter.convert(brigadeManager.listAllBySector(sector.getPosition()));
            final List<CorpDTO> corps = CorpConverter.convert(corpManager.listAllBySector(sector.getPosition()));
            final List<ArmyDTO> armies = ArmyConverter.convert(armyManager.listAllBySector(sector.getPosition()));

            // Add Brigades without a Corp
            final CorpDTO freeCorpDTO = new CorpDTO();
            freeCorpDTO.setCorpId(0);
            freeCorpDTO.setArmyId(0);

            final Map<Integer, BrigadeDTO> brigadeDTOList = new HashMap<Integer, BrigadeDTO>();
            if (brigades.size() > 0) {
                // Include Brigades of this Corp
                for (final BrigadeDTO brigade : brigades) {
                    brigadeDTOList.put(brigade.getBrigadeId(), brigade);
                }

                // Set Brigade list
                freeCorpDTO.setBrigades(brigadeDTOList);
                corps.add(freeCorpDTO);
            }

            final ArmyDTO freeDTO = new ArmyDTO();
            if (corps.size() > 0) {
                freeDTO.setArmyId(0);
                final Map<Integer, CorpDTO> freeCorpDTOMap = new HashMap<Integer, CorpDTO>();

                // Include Corps of this Army
                for (final CorpDTO corp : corps) {
                    freeCorpDTOMap.put(corp.getCorpId(), corp);
                }

                // Set Corp list
                freeDTO.setCorps(freeCorpDTOMap);
                armies.add(freeDTO);
            }

            return armies;

        } catch (Exception e) {
            return new ArrayList<ArmyDTO>();
        }
    }

    /**
     * Retrieve spies for a specific game, nation (player) and turn.
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @return a list of SpyDTO object.
     */
    @ClientCachable(gameId = 2, nationId = 1)
    public List<SpyDTO> getSpies(final int scenarioId, final int nationId,
                                 final int gameId,
                                 final int turn,
                                 final boolean applyOrders) {
        ScenarioContextHolder.setScenario(scenarioId);
        try {
            final Game thisGame = getGame(scenarioId, gameId);
            final Nation thisNation = getNation(scenarioId, nationId);

            // Return the spy list
            final List<SpyDTO> lstSpies = SpyConverter.convert(spyManager.listGameNation(thisGame, thisNation));
            if (applyOrders) {
                // Get all orders for this turn
                final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, SpyApplyChangesProcessor.ORDERS_TYPES);

                // Initialize a changes processor
                final SpyApplyChangesProcessor sCProc = new SpyApplyChangesProcessor(scenarioId, gameId, nationId, turn);

                // Add the spies and orders to the processor
                sCProc.addData(lstSpies, orders);

                // Process the changes on the spies list
                return sCProc.processChanges();
            } else {
                return lstSpies;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Retrieve baggage trains for a specific game, nation (player) and turn.
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @return a list of BaggageTrainDTO objects.
     */
    @ClientCachable(gameId = 2, nationId = 1)
    public List<BaggageTrainDTO> getBaggageTrains(final int scenarioId, final int nationId,
                                                  final int gameId,
                                                  final int turn,
                                                  final boolean isAllied,
                                                  final int originalNationId,
                                                  final boolean visible) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game thisGame = getGame(scenarioId, gameId);
        final Nation thisNation = getNation(scenarioId, nationId);

        // Get all baggage trains
        final List<BaggageTrainDTO> lstBTrains = BaggageTrainConverter.convert(baggageTrainManager.listGameNation(thisGame, thisNation), goodManager);
        if (visible) {
            //	Get all orders for this turn
            final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, BaggageTrainApplyChangesProcessor.ORDERS_TYPES);
            if (isAllied) {
                orders.addAll(getOrders(scenarioId, gameId, originalNationId, turn, BaggageTrainApplyChangesProcessor.ALLIED_ORDERS_TYPES));
            } else {
                List<NationsRelation> relations = relationsManager.listAlliesByGameNation(thisGame, thisNation);
                for (NationsRelation relation : relations) {
                    if (relation.getTarget().getId() != thisNation.getId()) {
                        orders.addAll(getOrders(scenarioId, gameId, relation.getTarget().getId(), turn, BaggageTrainApplyChangesProcessor.ALLIED_ORDERS_TYPES));
                    }
                }
            }

            // Initialize a changes processor
            final BaggageTrainApplyChangesProcessor aCProc = new BaggageTrainApplyChangesProcessor(scenarioId, gameId, nationId, turn);

            // Add the baggageTrains and orders to the processor
            aCProc.addData(lstBTrains, orders);

            // Process the changes on the baggageTrain list
            return aCProc.processChanges();
        } else {
            return lstBTrains;
        }
    }

    /**
     * Retrieve the player orders for a given game, nation and turn.
     *
     * @param gameId     the identity of the game.
     * @param nationId   the identity of the nation.
     * @param turn       the turn of the game.
     * @param orderTypes the order types to fetch.
     * @return a list of OrderDTO objects.
     */
    @ClientCachable(gameId = 1, nationId = 2)
    public List<OrderDTO> getOrders(final int scenarioId, final int gameId, final int nationId, final int turn, final Object[] orderTypes) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Nation thisNation = getNation(scenarioId, nationId);
        final Game thisGame = getGame(scenarioId, gameId);
        return OrderConverter.convert(playerOrderManager.listByGameNationType(thisGame, thisNation, turn, orderTypes));
    }

    /**
     * Retrieve the player orders for a given game, nation and turn.
     *
     * @param gameId   the identity of the game.
     * @param nationId the identity of the nation.
     * @param turn     the turn of the game.
     * @return a list of OrderDTO objects.
     */
    @ClientCachable(gameId = 1, nationId = 2)
    public List<OrderDTO> getOrders(final int scenarioId, final int gameId, final int nationId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Nation thisNation = getNation(scenarioId, nationId);
        final Game thisGame = getGame(scenarioId, gameId);
        return OrderConverter.convert(playerOrderManager.listByGameNation(thisGame, thisNation, turn));
    }

    /**
     * Retrieve the trade cities (in client view) for a given game, nation and turn.
     *
     * @param gameId   the identity of the game.
     * @param nationId the identity of the nation.
     * @param turn     the turn of the game.
     * @return a list of TradeCityDTO objects.
     */
    @ClientCachable(gameId = 2, nationId = 1)
    public List<TradeCityDTO> getTradeCities(final int scenarioId, final int nationId, final int gameId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game thisGame = getGame(scenarioId, gameId);

        //	Get all orders for this turn
        final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, TradeCitiesApplyChangesProcessor.ORDERS_TYPES);

        // Initialize a changes processor
        final TradeCitiesApplyChangesProcessor tcCProc = new TradeCitiesApplyChangesProcessor(scenarioId, gameId, nationId, turn);

        // Add the tradeCities and orders to the processor
        tcCProc.addData(TradeCityConverter.convert(tradeCityManager.listByGame(thisGame)), orders);

        // Return the processed army list
        return tcCProc.processChanges();
    }

    /**
     * Retrieve the player orders (in client view) and costs for a given game, nation and turn.
     *
     * @param gameId   the identity of the game.
     * @param nationId the identity of the nation.
     * @param turn     the turn of the game.
     * @return a list of OrderDTO objects.
     */
    @ClientCachable(gameId = 2, nationId = 1)
    public Map<Integer, List<ClientOrderDTO>> getClientOrders(final int scenarioId, final int nationId,
                                                              final int gameId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn);
        final OrderApplyChangesProcessor orderProcessor = new OrderApplyChangesProcessor(scenarioId, gameId, nationId, turn, this);
        orderProcessor.addData(null, orders);
        return orderProcessor.processChanges();
    }

    /**
     * Retrieve allied units for a given nation,game and turn
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @return a map of type regionId - nationIdStr - AlliedUnits object
     */
    //@ClientCachable(gameId = 2, nationId = 1)
    public Map<Integer, Map<Integer, AlliedUnits>> getAlliedUnits(final int scenarioId, final int nationId,
                                                                  final int gameId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final SetupAlliedUnitsProccessor aUprocc = new SetupAlliedUnitsProccessor(scenarioId, this, gameId, nationId, turn);
        return aUprocc.getAlliedUnits();
    }

    @ClientCachable(gameId = 2, nationId = 1)
    public ForeignUnits getForeignUnitsOnNationTerritory(final int scenarioId, final int nationId, final int gameId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final SetupForeignUnitsProcessor fUProcc = new SetupForeignUnitsProcessor(scenarioId, this, gameId, nationId, turn);
        return fUProcc.getForeignUnits();
    }

    /**
     * Retrieve old messages for the current game and nation.
     *
     * @param nationId The nationIdStr.
     * @param gameId   The gameIdStr.
     * @return The list of messages
     */
    @SuppressWarnings("unchecked")
    public List<String> getChatMessageHistory(final int scenarioId, final int nationId, final int gameId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final List<String> out = new ArrayList<String>();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2);
        final ObjectMapper objectMapper = new ObjectMapper();

        for (final ChatMessage message : chatManager.listByNationGameFromDate(nationId, gameId, cal.getTime())) {
            try {
                final ChatMessageDTO chatMessageDTO
                        = objectMapper.readValue(message.getMessage(), ChatMessageDTO.class);
                out.add(message.getMessage());
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }

        return out;
    }

    @ClientCachable(cacheName = "gameCache", gameId = 2, nationId = 1)
    public List<String> getCallForAllies(final int scenarioId, final int nationId,
                                         final int gameId,
                                         final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final List<String> out = new ArrayList<String>();
        final List<Report> lstReports = reportManager.listByOwnerTurnKey(nationManager.getByID(nationId),
                getGame(scenarioId, gameId),
                turn - 1,
                "callallies.%");

        //construct the list.
        final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, new Object[]{ORDER_POLITICS});

        for (Report report : lstReports) {
            final String targetId = report.getKey().substring(11);
            final String callerId = report.getValue();

            boolean replied = false;
            for (OrderDTO order : orders) {
                if (order.getParameter1().equalsIgnoreCase(targetId)
                        && "5".equals(order.getParameter2())) {
                    replied = true;
                    break;
                }
            }

            if (!replied) {
                out.add(callerId + ":" + targetId);
            }
        }
        return out;
    }

    @ClientCachable(cacheName = "gameCache", gameId = 2, nationId = 1)
    public Map<Boolean, Map<Integer, BattleDTO>> getNavalTacticalBattlesPositions(final int scenarioId, final int nationId,
                                                                                  final int gameId,
                                                                                  final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Map<Boolean, Map<Integer, BattleDTO>> out = new HashMap<Boolean, Map<Integer, BattleDTO>>();
        //for naval battles
        out.put(false, new HashMap<Integer, BattleDTO>());
        //for tactical battles
        out.put(true, new HashMap<Integer, BattleDTO>());

        final Nation thisNation = getNation(scenarioId, nationId);
        final Game thisGame = getGame(scenarioId, gameId);

        //retrieve this natiosn relations
        final List<NationsRelation> relations = listNationRelation(thisGame, thisNation);

        final Map<Integer, Integer> relationMap = new HashMap<Integer, Integer>();
        for (NationsRelation relation : relations) {
            relationMap.put(relation.getTarget().getId(), relation.getRelation());
        }

        //retrieve naval battles
        final Set<NavalBattleReport> navalBattles = new HashSet<NavalBattleReport>();
        navalBattles.addAll(navalBattleManager.listGameNationTurn(thisGame, thisNation, turn));

        //also retrieve naval battles for allied nations
        for (final NationsRelation relation : relations) {
            navalBattles.addAll(navalBattleManager.listGameNationTurn(thisGame, relation.getTarget(), turn));
        }

        //retrieve tactical battles
        final Set<TacticalBattleReport> tacticalBattles = new HashSet<TacticalBattleReport>();
        tacticalBattles.addAll(tacticalBattleManager.listGameNationTurn(thisGame, thisNation, turn));

        //also retrieve tactical battles for allied nations
        for (final NationsRelation relation : relations) {
            tacticalBattles.addAll(tacticalBattleManager.listGameNationTurn(thisGame, relation.getTarget(), turn));
        }

        for (final NavalBattleReport battle : navalBattles) {
            out.get(false).put(battle.getBattleId(), BattleConverter.convert(battle, nationId, relationMap));
        }

        for (final TacticalBattleReport battle : tacticalBattles) {
            out.get(true).put(battle.getBattleId(), BattleConverter.convert(battle, nationId, relationMap));

        }

        return out;
    }

    /**
     * Retrieve foreign units for a given game and nation.
     *
     * @param gameId   the identity of the game to check.
     * @param nationId the identity of the nation (land owner).
     * @return a list of Foreign armies.
     */
    @ClientCachable(cacheName = "gameCache", gameId = 1, nationId = 2)
    public List<ForeignArmyDTO> getForeignArmies(final int scenarioId, final int gameId, final int nationId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Nation thisNation = getNation(scenarioId, nationId);
        final Game thisGame = getGame(scenarioId, gameId);
        return brigadeManager.listForeignArmies(thisGame, thisNation);
    }

    public GameSettingsDTO getGameSettings(final int scenarioId, final int nationId, final int gameId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Nation thisNation = getNation(scenarioId, nationId);
        final Game thisGame = getGame(scenarioId, gameId);
        final List<GameSettings> gameSettings = settingsManager.getByGameNation(thisGame, thisNation);
        final GameSettings newSettings;
        if (gameSettings.isEmpty()) {
            newSettings = new GameSettings();
            newSettings.setGame(thisGame);
            newSettings.setNation(thisNation);
            newSettings.setGrid(false);
            newSettings.setLandForces(true);
            newSettings.setMovements(true);
            newSettings.setNavalForces(true);
            newSettings.setPoliticalBorders(true);
            newSettings.setPopulationDensity(true);
            newSettings.setSphereOfInfluence(false);
            newSettings.setReportedUnits(true);
            newSettings.setPlayMusic(true);
            newSettings.setPlaySoundEffects(true);
            newSettings.setLowResolution(true);
            newSettings.setTradeCities(true);
            newSettings.setFullScreen(false);
            settingsManager.add(newSettings);

        } else {
            newSettings = gameSettings.get(0);
        }

        //check if empire is dead or alive
        final GameSettingsDTO settingsDTO = GameSettingsConverter.convert(newSettings);
        final Report report = reportManager.getByOwnerTurnKey(thisNation, thisGame, turn - 1, "nation.alive");

        if (report != null) {
            settingsDTO.setDeadNation("0".equals(report.getValue()));

        } else {
            settingsDTO.setDeadNation(false);
        }

        // Check if player can issue harsh taxation
        final List<PlayerOrder> lstOrders = playerOrderManager.listByTaxOrders(thisGame, thisNation, turn);
        settingsDTO.setAllowHarshTax(lstOrders.size() <= 1);

        // set if the game has ended
        settingsDTO.setGameEnded(thisGame.getEnded());

        return settingsDTO;
    }

    @SuppressWarnings("unchecked")
    @ClientCachable(gameId = 2, nationId = 1)
    public Map<Integer, Map<Integer, MovementDTO>> getMovementOrdersByGameNationAndTurn(final int scenarioId, final int nationId,
                                                                                        final int gameId,
                                                                                        final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Map<Integer, HashMap<Integer, MovementDTO>> typeIdMvMap = new HashMap<Integer, HashMap<Integer, MovementDTO>>();
        final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, MovementApplyChangesProcessor.ORDERS_TYPES);
        final MovementApplyChangesProcessor amcProcc = new MovementApplyChangesProcessor(scenarioId, gameId, nationId, turn, this);
        amcProcc.addData(typeIdMvMap, null);
        amcProcc.addData(null, orders);
        return (Map<Integer, Map<Integer, MovementDTO>>) amcProcc.processChanges().get(0);
    }

    @SuppressWarnings("unchecked")
    @ClientCachable(gameId = 2, nationId = 1)
    public Map<Integer, OrderPositionDTO> getRegionOrdersByGameNationAndTurn(final int scenarioId, final int nationId,
                                                                             final int gameId,
                                                                             final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Map<Integer, OrderPositionDTO> sectorOrdersMap = new HashMap<Integer, OrderPositionDTO>();
        final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, RegionApplyChangesProcessor.ORDERS_TYPES);
        final RegionApplyChangesProcessor arcProcc = new RegionApplyChangesProcessor(scenarioId, gameId, nationId, turn, sectorManager);
        arcProcc.addData(sectorOrdersMap, null);
        arcProcc.addData(null, orders);
        return (Map<Integer, OrderPositionDTO>) arcProcc.processChanges().get(0);
    }

    @ClientCachable(gameId = 2, nationId = 1)
    public Map<Integer, List<BrigadeDTO>> getNewBrigadeOrdersByGameNationAndTurn(final int scenarioId, final int nationId,
                                                                                 final int gameId,
                                                                                 final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Map<Integer, List<BrigadeDTO>> newBrigadesMap = new HashMap<Integer, List<BrigadeDTO>>();

        final List<ArmyTypeDTO> armyTypeDTO = getArmyTypes(scenarioId, nationId);
        final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, SetupNewBrigadeOrders.ORDERS_TYPES);
        final SetupNewBrigadeOrders arcProcc = new SetupNewBrigadeOrders(scenarioId, gameId, nationId, turn, this);
        arcProcc.addData(newBrigadesMap, null);
        arcProcc.addData(armyTypeDTO, orders);
        return arcProcc.processChanges().get(0);
    }

    @ClientCachable(gameId = 2, nationId = 1)
    public Map<Integer, List<ShipDTO>> getNewShipOrdersByGameNationAndTurn(final int scenarioId, final int nationId,
                                                                           final int gameId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game thisGame = getGame(scenarioId, gameId);
        final Nation thisNation = getNation(scenarioId, nationId);
        final Map<Integer, List<ShipDTO>> newShipsMap = new HashMap<Integer, List<ShipDTO>>();
        final List<ShipTypeDTO> shipTypeDTO = getShipTypes(scenarioId, nationId);
        final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, SetupNewShipOrders.ORDERS_TYPES);
        for (OrderDTO order : OrderConverter.convert(playerOrderManager.listByShipConstruction(thisGame, thisNation, turn))) {
            if ((order.getTurn() == turn - 1
                    && (Integer.parseInt(order.getParameter2()) == 3 || Integer.parseInt(order.getParameter2()) == 4
                    || Integer.parseInt(order.getParameter2()) == 5))
                    || (order.getTurn() == turn - 2 && Integer.parseInt(order.getParameter2()) == 5)) {
                orders.add(order);
            }
        }
        final SetupNewShipOrders arcProcc = new SetupNewShipOrders(scenarioId, gameId, nationId, turn, this);
        arcProcc.addData(newShipsMap, null);
        arcProcc.addData(shipTypeDTO, orders);
        return arcProcc.processChanges().get(0);
    }

    @ClientCachable(gameId = 2, nationId = 1)
    public Map<Integer, List<BaggageTrainDTO>> getNewBaggageTrainOrdersByGameNationAndTurn(final int scenarioId,
                                                                                           final int nationId,
                                                                                           final int gameId,
                                                                                           final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Map<Integer, List<BaggageTrainDTO>> newBaggageTrainsMap = new HashMap<Integer, List<BaggageTrainDTO>>();
        final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, SetupNewBaggageTrainOrders.ORDERS_TYPES);
        final SetupNewBaggageTrainOrders arcProcc = new SetupNewBaggageTrainOrders(scenarioId, gameId, nationId, turn);
        arcProcc.addData(null, orders);
        arcProcc.addData(newBaggageTrainsMap, null);
        return arcProcc.processChanges().get(0);
    }

    @ClientCachable(gameId = 2, nationId = 1)
    public Taxation getTaxationByNationAndGame(final int scenarioId, final int nationId, final int gameId,
                                               final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final List<OrderDTO> orders = getOrders(scenarioId, gameId, nationId, turn, new Object[]{ORDER_TAXATION});
        final Taxation tax = new Taxation();
        for (final OrderDTO order : orders) {
            tax.setTaxLevel(Integer.parseInt(order.getParameter1()));
            if (tax.getTaxLevel() == 0) {

                if (Integer.parseInt(order.getParameter2()) == 1) {
                    tax.setUseColGoods(true);
                    tax.getCost().setNumericCost(GOOD_COLONIAL, Integer.parseInt(order.getTemp1()));
                }

                if (Integer.parseInt(order.getParameter3()) == 1) {
                    tax.setUseGems(true);
                    tax.getCost().setNumericCost(GOOD_GEMS, Integer.parseInt(order.getTemp2()));
                }

                if (Integer.parseInt(order.getParameter4()) == 1) {
                    tax.setUseIndPoints(true);
                    tax.getCost().setNumericCost(GOOD_INPT, Integer.parseInt(order.getTemp3()));
                }

                if (Integer.parseInt(order.getParameter5()) == 1) {
                    tax.setUseMoney(true);
                    tax.getCost().setNumericCost(GOOD_MONEY, Integer.parseInt(order.getTemp4()));
                }

                if (Integer.parseInt(order.getParameter6()) == 1) {
                    tax.setUseColonials(true);
                    tax.getCost().setNumericCost(GOOD_COLONIAL, Integer.parseInt(order.getTemp1()));
                }
            }
        }

        // retrieve nation's VPs in case order is about elite/crack-elite troops with VPs limitation
        final Nation thisNation = getNation(scenarioId, nationId);
        final Game thisGame = getGame(scenarioId, gameId);
        final int lastTurnVPs = Integer.parseInt(getReport(scenarioId, thisGame, thisNation, thisGame.getTurn() - 1, ReportConstants.N_VP));

        // check duration of game
        final double modifier;
        switch (thisGame.getType()) {
            case GameConstants.DURATION_SHORT:
                modifier = .7d;
                break;

            case GameConstants.DURATION_LONG:
                modifier = 1.3d;
                break;

            case GameConstants.DURATION_NORMAL:
            default:
                modifier = 1d;
        }

        tax.setVps(lastTurnVPs);
        tax.setVpsMax((int) (thisNation.getVpWin() * modifier));

        // Retrieve population size
        final int popEurope = Integer.parseInt(getReport(scenarioId, thisGame, thisNation, thisGame.getTurn() - 1, ReportConstants.E_POP_SIZE + EUROPE));
        tax.setPopulation(popEurope);

        // Retrieve Deficit
        final String deficit = getReport(scenarioId, thisGame, thisNation, thisGame.getTurn(), ReportConstants.RE_DEFI);
        if (deficit.length() > 0) {
            final int tradeDeficit = Integer.parseInt(deficit);
            tax.setDeficit(tradeDeficit);
        }

        // Retrieve Surplus
        final String surplus = getReport(scenarioId, thisGame, thisNation, thisGame.getTurn(), ReportConstants.RE_SURP);
        if (surplus.length() > 0) {
            final int tradeSurplus = Integer.parseInt(surplus);
            tax.setSurplus(tradeSurplus);
        }

        return tax;
    }

    /**
     * Save the orders into the DB.
     *
     * @param empireOrderDTOs list of Order DTO.
     * @return a list of IDs for the new DB entities.
     */
    @EvictCache(cacheName = "client", gameId = 1, nationId = 2)
    public List<String> saveOrders(final int scenarioId, final int gameId, final int nationId, final List<OrderDTO> empireOrderDTOs) {
        ScenarioContextHolder.setScenario(scenarioId);
        final List<String> ids = new ArrayList<String>(empireOrderDTOs.size());
        if (empireOrderDTOs.isEmpty()) {
            return ids;
        }

        final Game thisGame = getGame(scenarioId, empireOrderDTOs.get(0).getGameId());
        final Nation thisNation = getNation(scenarioId, empireOrderDTOs.get(0).getNationId());

        for (final OrderDTO empireOrderDTO : empireOrderDTOs) {
            final PlayerOrder empireOrder = OrderConverter.convert(empireOrderDTO, thisGame, thisNation);
            playerOrderManager.add(empireOrder);
            ids.add(Integer.toString(empireOrder.getOrderId()));
        }
        //ok, now we have to our allies cache....
        for (NationsRelation relation : relationsManager.listAlliesByGameNation(thisGame, thisNation)) {
            evictCache(gameId, relation.getTarget().getId());
        }
        return ids;
    }

    @EvictCache(cacheName = "client", gameId = 0, nationId = 1)
    public void evictCache(final int gameId, final int nationId) {
        //do nothing here
    }

    /**
     * Save changes to armies.
     *
     * @param chArmiesList the new list of armies.
     * @param nationId     the owner.
     * @param gameId       the game.
     * @param turn         the turn number.
     * @return the number of changes made.
     */
    @EvictCache(cacheName = "client", gameId = 5, nationId = 4)
    public int saveUnitChanges(final int scenarioId, final List<ArmyDTO> chArmiesList,
                               final Map<Integer, List<BrigadeDTO>> newBrigMap,
                               final Map<Integer, List<ClientOrderDTO>> relOrders,
                               final int nationId,
                               final int gameId,
                               final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        playerOrderManager.deleteByGameNationType(gameId, nationId, turn,
                new Object[]{ORDER_ADD_BATT, ORDER_B_ARMY, ORDER_B_CORP, ORDER_B_BATT, ORDER_D_ARMY, ORDER_D_CORP,
                        ORDER_D_BRIG, ORDER_REN_ARMY, ORDER_REN_CORP, ORDER_REN_BRIG, ORDER_ADDTO_ARMY,
                        ORDER_ADDTO_CORP, ORDER_ADDTO_BRIGADE, ORDER_INC_EXP, ORDER_INC_HEADCNT, ORDER_INC_EXP_ARMY,
                        ORDER_INC_EXP_CORPS, ORDER_INC_HEADCNT_ARMY, ORDER_INC_HEADCNT_CORPS, ORDER_MRG_BATT});

        final Collection<ArmyDTO> dbArmiesList = getArmiesByNation(scenarioId, nationId, gameId, turn).getArmies();
        final List<OrderDTO> armyChanges = new ArrayList<OrderDTO>();
        final UnitsSaveChangesProcessor aProcc = new UnitsSaveChangesProcessor(scenarioId, gameId, nationId, turn, relOrders);
        aProcc.addData(dbArmiesList, chArmiesList);
        aProcc.addData(null, newBrigMap);
        armyChanges.addAll(aProcc.processChanges());
        saveOrders(scenarioId, gameId, nationId, armyChanges);
        return armyChanges.size();
    }

    /**
     * Save changes to commanders
     *
     * @param commandersList the list of the changed commanders
     * @param nationId       the owner.
     * @param gameId         the game.
     * @param turn           the turn number.
     * @return success 1 fail 0;
     */
    @EvictCache(cacheName = "client", gameId = 4, nationId = 3)
    public int saveCommanderChanges(final int scenarioId, final List<CommanderDTO> commandersList,
                                    final Map<Integer, List<ClientOrderDTO>> orderMap,
                                    final int nationId, final int gameId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        try {
            playerOrderManager.deleteByGameNationType(gameId, nationId, turn,
                    new Object[]{ORDER_ARMY_COM, ORDER_CORP_COM, ORDER_LEAVE_COM, ORDER_REN_COMM, ORDER_HIRE_COM,
                            ORDER_DISS_COM});

            final CommandersSaveChangesProcessor ccProcc = new CommandersSaveChangesProcessor(scenarioId, gameId, nationId, turn, orderMap);
            ccProcc.addData(getCommandersByGameNation(scenarioId, gameId, nationId), commandersList);
            List<OrderDTO> toSave = ccProcc.processChanges();
            saveOrders(scenarioId, gameId, nationId, toSave);
            return toSave.size();

        } catch (Exception ex) {
            LOGGER.error(ex);
            return 0;
        }
    }

    /**
     * Save changes to fleets.
     *
     * @param idFleetMap the new map of fleets by their identities.
     * @param nationId   the owner.
     * @param gameId     the game.
     * @param turn       the turn number.
     * @return the number of changes made.
     */
    @EvictCache(cacheName = "client", gameId = 5, nationId = 4)
    public int saveNavyChanges(final int scenarioId, final Map<Integer, FleetDTO> idFleetMap,
                               final Map<Integer, List<ShipDTO>> newShipMap,
                               final Map<Integer, List<ClientOrderDTO>> orderMap,
                               final int nationId,
                               final int gameId,
                               final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        playerOrderManager.deleteByGameNationType(gameId, nationId, turn,
                new Object[]{ORDER_B_FLT, ORDER_B_SHIP, ORDER_R_FLT, ORDER_D_FLT, ORDER_REN_FLT, ORDER_REN_SHIP,
                        ORDER_R_SHP, ORDER_ADDTO_FLT, ORDER_HOVER_SHIP, ORDER_SCUTTLE_SHIP});

        final List<FleetDTO> dbShipsList = getFleetsByNation(scenarioId, nationId, gameId, turn).get(true);
        final NavySaveChangesProcessor nProcc = new NavySaveChangesProcessor(scenarioId, gameId, nationId, turn, orderMap, this);
        nProcc.addData(dbShipsList, null);
        nProcc.addData(null, idFleetMap);
        nProcc.addNewShipData(newShipMap);

        final List<OrderDTO> navyChanges = new ArrayList<OrderDTO>();
        navyChanges.addAll(nProcc.processChanges());

        saveOrders(scenarioId, gameId, nationId, navyChanges);

        return navyChanges.size();
    }


    /**
     * Save changes to all production site orders for this nation, game and turn
     *
     * @param sectorProdSites a hash map containing the sectors and the production
     *                        site ids.
     * @param nationId        the owner.
     * @param gameId          the game.
     * @param turn            the turn.
     * @return an integer describing success.
     */
    @EvictCache(cacheName = "client", gameId = 4, nationId = 3)
    public int saveEconomyChanges(final int scenarioId, final Map<SectorDTO, Integer> sectorProdSites,
                                  final Map<Integer, List<ClientOrderDTO>> orderMap,
                                  final int nationId,
                                  final int gameId,
                                  final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        playerOrderManager.deleteByGameNationType(gameId, nationId, turn,
                new Object[]{ORDER_B_PRODS, ORDER_D_PRODS});

        final HashMap<SectorDTO, Integer> dbProdSiteMap = new HashMap<SectorDTO, Integer>();
        final ProdSitesChangesProcessor eProcc = new ProdSitesChangesProcessor(scenarioId, gameId, nationId, turn, orderMap);
        eProcc.addData(dbProdSiteMap, sectorProdSites);
        final List<OrderDTO> newOrdersList = eProcc.processChanges();
        saveOrders(scenarioId, gameId, nationId, newOrdersList);

        return newOrdersList.size();
    }

    /**
     * Save changes related to barracks.
     *
     * @param chBarracksMap Barracks map.
     * @param orderMap      Orders map.
     * @param nationId      Users nation.
     * @param gameId        Game id.
     * @param turn          Game turn.
     * @return 1 if success.
     */
    @EvictCache(cacheName = "client", gameId = 4, nationId = 3)
    public int saveBarrackChanges(final int scenarioId, final Map<Integer, BarrackDTO> chBarracksMap,
                                  final Map<Integer, List<ClientOrderDTO>> orderMap,
                                  final int nationId,
                                  final int gameId,
                                  final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        playerOrderManager.deleteByGameNationType(gameId, nationId, turn,
                new Object[]{ORDER_REN_BARRACK});

        final List<BarrackDTO> dbBarracks = getBarracksByNation(scenarioId, nationId, gameId, turn);
        final Map<Integer, BarrackDTO> dbBarracksMap = new HashMap<Integer, BarrackDTO>();
        for (BarrackDTO barrShip : dbBarracks) {
            dbBarracksMap.put(barrShip.getId(), barrShip);
        }

        final BarrackSaveChangesProcessor bProcc = new BarrackSaveChangesProcessor(scenarioId, gameId, nationId, turn, orderMap);
        bProcc.addData(dbBarracksMap, chBarracksMap);

        final List<OrderDTO> newOrdersList = bProcc.processChanges();
        saveOrders(scenarioId, gameId, nationId, newOrdersList);

        return newOrdersList.size();
    }

    /**
     * Save changes to all production site orders for this nation, game and turn
     *
     * @param relationsList a hash map containing the sectors and the production
     *                      site ids.
     * @param nationId      the owner.
     * @param gameId        the game.
     * @param turn          the turn.
     * @return an integer describing success.
     */
    @EvictCache(cacheName = "client", gameId = 3, nationId = 2)
    public int saveRelationsChanges(final int scenarioId, final List<RelationDTO> relationsList,
                                    final int nationId,
                                    final int gameId,
                                    final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        playerOrderManager.deleteByGameNationType(gameId, nationId, turn,
                new Object[]{ORDER_POLITICS});

        final PoliticsSaveChangesProcessor eProcc = new PoliticsSaveChangesProcessor(scenarioId, gameId, nationId, turn);
        eProcc.addData(null, relationsList);

        final List<OrderDTO> newOrdersList = eProcc.processChanges();
        saveOrders(scenarioId, gameId, nationId, newOrdersList);

        return newOrdersList.size();
    }

    /**
     * Save changes to all army movement orders
     *
     * @param typeMvMap a hash map containing the sectors and the production
     *                  site ids.
     * @param nationId  the owner.
     * @param gameId    the game.
     * @param turn      the turn.
     * @return an integer describing success.
     */
    @EvictCache(cacheName = "client", gameId = 4, nationId = 3)
    public int saveMovementChanges(final int scenarioId, final Map<Integer, Map<Integer, MovementDTO>> typeMvMap,
                                   final Map<Integer, List<ClientOrderDTO>> orderMap,
                                   final int nationId,
                                   final int gameId,
                                   final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        LOGGER.info("Saving movement orders for : " + scenarioId +"/"+ gameId + "/" + nationId +"/" + turn);
        playerOrderManager.deleteByGameNationType(gameId, nationId, turn,
                new Object[]{ORDER_M_UNIT});

        final MovementSaveChangesProcessor mvProcc = new MovementSaveChangesProcessor(scenarioId, gameId, nationId, turn, orderMap);
        mvProcc.addData(null, typeMvMap);

        final List<OrderDTO> newOrdersList = mvProcc.processChanges();
        saveOrders(scenarioId, gameId, nationId, newOrdersList);

        return newOrdersList.size();
    }

    public int saveGameSettingsChanges(final int scenarioId, final GameSettingsDTO settings, final int nationId,
                                       final int gameId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game game = getGame(scenarioId, gameId);
        final Nation nation = getNation(scenarioId, nationId);
        final List<GameSettings> gameSettings = settingsManager.getByGameNation(game, nation);
        final GameSettings newSettings;
        if (gameSettings.isEmpty()) {
            newSettings = new GameSettings();
            newSettings.setGame(game);
            newSettings.setNation(nation);

        } else {
            newSettings = gameSettings.get(0);
        }

        newSettings.setGrid(settings.isGrid());
        newSettings.setLandForces(settings.isLandForces());
        newSettings.setMovements(settings.isMovements());
        newSettings.setNavalForces(settings.isNavalForces());
        newSettings.setPoliticalBorders(settings.isPoliticalBorders());
        newSettings.setPopulationDensity(settings.isPopulationDensity());
        newSettings.setSphereOfInfluence(settings.isSphereOfInfluence());
        newSettings.setReportedUnits(settings.isVirtualReportedUnits());
        newSettings.setPlayMusic(settings.isMusic());
        newSettings.setPlaySoundEffects(settings.isSoundEffects());
        newSettings.setLowResolution(settings.isLowResolution());
        newSettings.setZoom(settings.getZoom());
        newSettings.setTradeCities(settings.isTradeCities());
        newSettings.setFullScreen(settings.isFullscreen());

        if (gameSettings.isEmpty()) {
            settingsManager.add(newSettings);
        } else {
            settingsManager.update(newSettings);
        }
        //if it is tutorial also schedule it to be processed.
        if (scenarioId == HibernateUtil.DB_FREE) {
            game.setStatus(GameConstants.GAME_SCHED);
            Calendar nextProc = Calendar.getInstance();
            nextProc.setTime(new Date());
            nextProc.set(Calendar.DATE, nextProc.get(Calendar.DATE) - 1);
            game.setDateNextProc(nextProc.getTime());
            gameManager.update(game);

            // Invoke engine
            articleManager.getBuild(HibernateUtil.DB_FREE, 0);
        }

        return 1;
    }

    /**
     * Save changes to transportation
     *
     * @param cargoRelatedOrders the order related to cargo transportation
     * @return 1 if it was successful
     */
    @EvictCache(cacheName = "client", gameId = 3, nationId = 2)
    public int saveTransportChanges(final int scenarioId, final List<ClientOrderDTO> cargoRelatedOrders,
                                    final int nationId, final int gameId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        playerOrderManager.deleteByGameNationType(gameId, nationId, turn,
                new Object[]{ORDER_LOAD_TROOPSF, ORDER_LOAD_TROOPSS, ORDER_UNLOAD_TROOPSF, ORDER_UNLOAD_TROOPSS});

        final TransportChangesProcessor tcProcc = new TransportChangesProcessor(scenarioId, gameId, nationId, turn);
        tcProcc.addData(null, cargoRelatedOrders);

        final List<OrderDTO> newOrdersList = tcProcc.processChanges();
        saveOrders(scenarioId, gameId, nationId, newOrdersList);

        return newOrdersList.size();
    }

    /**
     * Save changes to all sectors
     *
     * @param sectorPositionMap a hash map containing the sectors and the production
     *                          site ids.
     * @param nationId          the owner.
     * @param gameId            the game.
     * @param turn              the turn.
     * @return an integer describing success.
     */
    @EvictCache(cacheName = "client", gameId = 4, nationId = 3)
    public int saveRegionChanges(final int scenarioId,
                                 final Map<Integer, OrderPositionDTO> sectorPositionMap,
                                 final Map<Integer, List<ClientOrderDTO>> orderMap,
                                 final int nationId,
                                 final int gameId,
                                 final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        playerOrderManager.deleteByGameNationType(gameId, nationId, turn,
                new Object[]{ORDER_INC_POP, ORDER_DEC_POP, ORDER_HOVER_SEC});

        final RegionSaveChangesProcessor rcProcc = new RegionSaveChangesProcessor(scenarioId, gameId, nationId, turn, orderMap);
        rcProcc.addData(sectorPositionMap, null);

        final List<OrderDTO> newOrdersList = rcProcc.processChanges();
        saveOrders(scenarioId, gameId, nationId, newOrdersList);

        return newOrdersList.size();
    }

    /**
     * Save changes to taxation
     *
     * @param tax      the level of taxation
     * @param nationId the owner.
     * @param gameId   the game.
     * @param turn     the turn.
     * @return an integer describing success.
     */
    @EvictCache(cacheName = "client", gameId = 3, nationId = 2)
    public int saveTaxationChanges(final int scenarioId, final Taxation tax, final int nationId, final int gameId,
                                   final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        // Find taxation orders and save them in a list
        try {
            playerOrderManager.deleteByGameNationType(gameId, nationId, turn,
                    new Object[]{ORDER_TAXATION});

            // If the taxation level is different than normal
            if (tax.getTaxLevel() != 0
                    || tax.isUseColGoods()
                    || tax.isUseIndPoints()
                    || tax.isUseMoney()
                    || tax.isUseColonials()) {

                // Add a new order to change it for this turn
                int colGoods = 0, gems = 0, indPoints = 0, gold = 0, colonials = 0;
                if (tax.isUseColGoods()) {
                    colGoods = 1;
                }

                if (tax.isUseGems()) {
                    gems = 1;
                }

                if (tax.isUseIndPoints()) {
                    indPoints = 1;
                }

                if (tax.isUseMoney()) {
                    gold = 1;
                }

                if (tax.isUseColonials()) {
                    colonials = 1;
                }

                final List<OrderDTO> orders = new ArrayList<OrderDTO>();
                final OrderDTO newTaxOrder = new OrderDTO(gameId, nationId, turn, ORDER_TAXATION, 0, 0,
                        String.valueOf(tax.getTaxLevel()), String.valueOf(colGoods), String.valueOf(gems), String.valueOf(indPoints), String.valueOf(gold), String.valueOf(colonials), "", "", "",
                        String.valueOf(tax.getCost().getNumericCost(GOOD_COLONIAL)), String.valueOf(tax.getCost().getNumericCost(GOOD_GEMS)), String.valueOf(tax.getCost().getNumericCost(GOOD_INPT)), String.valueOf(tax.getCost().getNumericCost(GOOD_MONEY)), "", "", "", "", "");
                orders.add(newTaxOrder);
                saveOrders(scenarioId, gameId, nationId, orders);
                return 1;
            }
            return 0;

        } catch (Exception ex) {
            LOGGER.error(ex);
            return 0;
        }
    }

    /**
     * Save changes to trading
     *
     * @param tradeOrders a list of trade orders
     * @param nationId    the owner.
     * @param gameId      the game.
     * @param turn        the turn.
     * @return an integer describing success.
     */
    @EvictCache(cacheName = "client", gameId = 3, nationId = 2)
    public int saveTradeChanges(final int scenarioId, final List<ClientOrderDTO> tradeOrders, final int nationId, final int gameId,
                                final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        playerOrderManager.deleteByGameNationType(gameId, nationId, turn,
                new Object[]{ORDER_EXCHF, ORDER_EXCHS});

        final TradeChangesProcessor tcProcc = new TradeChangesProcessor(scenarioId, gameId, nationId, turn);
        tcProcc.addData(null, tradeOrders);

        final List<OrderDTO> newOrdersList = tcProcc.processChanges();
        saveOrders(scenarioId, gameId, nationId, newOrdersList);

        return newOrdersList.size();
    }

    public int updateAllianceView(final int scenarioId, final int gameId, final int nationId, final int targetNationId, final boolean visible) {
        try {
            ScenarioContextHolder.setScenario(scenarioId);
            final Game thisGame = getGame(scenarioId, gameId);
            final NationsRelation relation = relationsManager.getByNations(thisGame, nationId, targetNationId);
            relation.setVisible(visible);
            relationsManager.update(relation);
            evictCache(gameId, nationId);
            evictCache(gameId, targetNationId);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Save changes to baggage trains
     *
     * @param baggageTList   a list of baggage trains
     * @param newBaggageTMap a map of the new baggage trains
     * @param nationId       the owner.
     * @param gameId         the game.
     * @param turn           the turn.
     * @return an integer describing success.
     */
    @EvictCache(cacheName = "client", gameId = 5, nationId = 4)
    public int saveBaggageTrainChanges(final int scenarioId, final List<BaggageTrainDTO> baggageTList,
                                       final Map<Integer, List<ClientOrderDTO>> ordersMap,
                                       final Map<Integer, List<BaggageTrainDTO>> newBaggageTMap, final int nationId,
                                       final int gameId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);

        playerOrderManager.deleteByGameNationType(gameId, nationId, turn,
                new Object[]{ORDER_B_BTRAIN, ORDER_R_BTRAIN, ORDER_REN_BTRAIN, ORDER_SCUTTLE_BTRAIN});

        final BaggageTrainSaveChangesProcessor tcProcc = new BaggageTrainSaveChangesProcessor(scenarioId, gameId, nationId, turn, this);
        tcProcc.addData(getBaggageTrains(scenarioId, nationId, gameId, turn, false, nationId, true), baggageTList);
        tcProcc.addData(null, newBaggageTMap);
        tcProcc.addOrders(ordersMap);

        final List<OrderDTO> newOrdersList = tcProcc.processChanges();
        saveOrders(scenarioId, gameId, nationId, newOrdersList);

        return newOrdersList.size();
    }

    /**
     * Post a news entry or send a private message.
     *
     * @param nationId     the sender nation.
     * @param gameId       the game.
     * @param targetNation the target nation.
     * @param message      the body of the message.
     * @param type         if this is a news entry (anonymous/eponymous) or a private message.
     * @return 1 if successful, -1 if an error occurred.
     */
    public int saveNewsletter(final int scenarioId, final int nationId, final int gameId,
                              final int targetNation, final String message, final int type) {
        ScenarioContextHolder.setScenario(scenarioId);
        try {
            final Game game = getGame(scenarioId, gameId);
            final Nation currentNation = getNation(scenarioId, nationId);
            final User senderUser = userManager.getByID(userGameManager.list(game, currentNation).get(0).getUserId());

            // Make sure that message has a body
            if (message.isEmpty()
                    || message.trim().length() < 2
                    || message.replaceAll("<br>", "").trim().length() < 2) {
                return -1;
            }
            final String messageCleaned = Jsoup.clean(message, Whitelist.relaxed());
            switch (type) {

                case NewsConstants.NEWS_PRIVATE:
                    // Identify Sender/Receiver
                    final Nation receiverNation = getNation(scenarioId, targetNation);
                    final User receiverUser = userManager.getByID(userGameManager.list(game, receiverNation).get(0).getUserId());

                    if (senderUser == null
                            || receiverUser == null
                            || senderUser.getUserId() < 1
                            || receiverUser.getUserId() < 1) {
                        return -1;
                    }

                    // Construct Message
                    final Message msg = new Message();
                    msg.setDate(new Date());
                    msg.setBodyMessage(messageCleaned);
                    msg.setSender(senderUser);
                    msg.setReceiver(receiverUser);
                    msg.setOpened(false);

                    // Construct Message Subject

                    final Calendar thisCal = calendar(game, game.getTurn());
                    final StringBuilder strBuilder = new StringBuilder();
                    strBuilder.append("G");
                    strBuilder.append(gameId);
                    strBuilder.append("/");
                    strBuilder.append(currentNation.getName());
                    strBuilder.append(" diplomatic message to ");
                    strBuilder.append(receiverNation.getName());
                    strBuilder.append(" on ");
                    strBuilder.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
                    strBuilder.append(" ");
                    strBuilder.append(thisCal.get(Calendar.YEAR));
                    msg.setSubject(strBuilder.toString());

                    // Identify if there is an ongoing thread
                    final Message rootMessage =
                            messageManager.getRootMessage(senderUser.getUserId(), receiverUser.getUserId(), msg.getSubject());

                    //This is a new root message
                    if (rootMessage == null) {
                        msg.setRootId(0);

                    } else {
                        msg.setRootId(rootMessage.getMessageId());
                    }

                    messageManager.add(msg);

                    // send notification via mail
                    sendMessageNotification(msg);

                    break;

                case NewsConstants.NEWS_LETTER:
                    // Only eponymous newsletter posts count towards gaining achievement points.

                    // Update profile entry
                    final Profile entry = profileManager.getByOwnerKey(senderUser, ProfileConstants.NEWSLETTER_EPONYMOUS);
                    final int totPosts;

                    // If the entry does not exist, create
                    if (entry == null) {
                        final Profile newEntry = new Profile();
                        newEntry.setUser(senderUser);
                        newEntry.setKey(ProfileConstants.NEWSLETTER_EPONYMOUS);
                        newEntry.setValue(1);
                        profileManager.add(newEntry);

                        totPosts = 1;

                    } else {
                        // Make sure players do not end up with negative VPs
                        entry.setValue(entry.getValue() + 1);
                        profileManager.update(entry);

                        totPosts = entry.getValue();
                    }

                    // Check if achievement is reached
                    for (int level = AchievementConstants.NEWSLETTERPOSTS_L_MIN; level <= AchievementConstants.NEWSLETTERPOSTS_L_MAX; level++) {
                        if (totPosts >= AchievementConstants.NEWSLETTERPOSTS_L[level]
                                && !getAchievementManager().checkPlayerCategoryLevel(senderUser, AchievementConstants.NEWSLETTERPOSTS, level)) {

                            // Generate new entry
                            final Achievement achievement = new Achievement();
                            achievement.setUser(senderUser);
                            achievement.setCategory(AchievementConstants.NEWSLETTERPOSTS);
                            achievement.setLevel(level);
                            achievement.setAnnounced(false);
                            achievement.setFirstLoad(false);
                            achievement.setDescription(AchievementConstants.NEWSLETTERPOSTS_STR[level]);
                            achievement.setVictoryPoints(0);
                            achievement.setAchievementPoints(AchievementConstants.NEWSLETTERPOSTS_AP[level]);
                            getAchievementManager().add(achievement);
                        }
                    }

                case NewsConstants.NEWS_LETTER_ANONYMOUS:
                default:
                    ScenarioContextHolder.setScenario(scenarioId);
                    int baseNewsId = 0;
                    final List<Nation> lstNation = nationManager.list();
                    for (final Nation nation : lstNation) {
                        final News news = new News();
                        news.setGame(game);
                        news.setNation(nation);
                        news.setSubject(currentNation);
                        news.setType(type);
                        news.setText(messageCleaned);
                        news.setTurn(game.getTurn());
                        news.setBaseNewsId(baseNewsId);
                        newsManager.add(news);

                        if (baseNewsId == 0) {
                            baseNewsId = news.getNewsId();
                        }
                    }
                    break;

            }
            return 0;

        } catch (Exception e) {
            LOGGER.error("Newsletter Entry", e);
            return -1;
        }
    }

    @ClientCachable(cacheName = "gameCache", gameId = 1, nationId = 2)
    public SectorDTO getSectorByCoordinates(final int scenarioId, final int game, final int regiondId, final int xPos, final int yPos) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Position pt = new Position();
        pt.setGame(getGame(scenarioId, game));
        pt.setRegion(getRegion(scenarioId, regiondId));
        pt.setX(xPos);
        pt.setY(yPos);
        final Sector out = sectorManager.getByPosition(pt);
        if (out == null) {
            return null;
        } else {
            return SectorConverter.convert(out);
        }
    }

    /**
     * Retrieve a report entry for this turn.
     *
     * @param game  the game of the report entry.
     * @param owner the owner of the report entry.
     * @param key   the key of the report entry.
     * @return the value of the report entry.
     */
    @GameCachable
    public String getReport(final int scenarioId, final Game game, final Nation owner, final String key) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Report thisReport = reportManager.getByOwnerTurnKey(owner, game, game.getTurn(), key);
        if (thisReport == null) {
            return "";
        } else {
            return thisReport.getValue();
        }
    }

    /**
     * Retrieve a report entry for this turn.
     *
     * @param game  the game of the report entry.
     * @param owner the owner of the report entry.
     * @param turn  the turn of the report.
     * @param key   the key of the report entry.
     * @return the value of the report entry.
     */
    @GameCachable
    public String getReport(final int scenarioId, final Game game, final Nation owner, final int turn, final String key) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Report thisReport = reportManager.getByOwnerTurnKey(owner, game, turn, key);
        if (thisReport == null) {
            return "0";

        } else {
            return thisReport.getValue();
        }
    }

    /**
     * Method that returns the games status
     *
     * @param gameId the id of the game we are questioning about
     * @return true if the game is in process
     */
    public boolean getGameStatus(final int scenarioId, final int gameId) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game game = getGame(scenarioId, gameId);
        return game.getStatus().equals(GameConstants.GAME_PROC);
    }

    /**
     * Method that returns the games' date of process
     *
     * @param gameId the id of the game we are questioning about
     * @return true if the game is in process
     */
    public String getProcessDate(final int scenarioId, final int gameId, final int offset) {
        ScenarioContextHolder.setScenario(scenarioId);
        Game game = getGame(scenarioId, gameId);
        Date procDate = game.getDateNextProc();
        //remove servers offset.
        long serverOffset = -game.getDateNextProc().getTimezoneOffset() * 60 * 1000;
        long clientOffset = -offset * 60 * 1000;
        procDate.setTime(procDate.getTime() - serverOffset + clientOffset);


        return procDate.toString();
    }

    /**
     * Detect if the given nation is alive or has lost.
     *
     * @param scenarioId the scenario.
     * @param gameId     the game.
     * @param turn       the turn.
     * @return true if the nation is still alive.
     */
    @ClientCachable(cacheName = "gameCache", gameId = 1, nationId = 2)
    public Map<Integer, Boolean> getNationsStatus(final int scenarioId, final int gameId, final int turn) {
        ScenarioContextHolder.setScenario(scenarioId);
        Map<Integer, Boolean> out = new HashMap<Integer, Boolean>();
        for (Nation nation : nationManager.list()) {
            final Report report = reportManager.getByOwnerTurnKey(nation, getGame(scenarioId, gameId), turn - 1, "nation.alive");

            if (report != null) {
                out.put(nation.getId(), "0".equals(report.getValue()));

            } else {
                out.put(nation.getId(), false);
            }
        }
        return out;
    }

    /**
     * Identify if sector is a home region, inside sphere of influence, or outside of the receiving nation.
     *
     * @param sector   the sector to examine.
     * @param receiver the receiving nation.
     * @return 1 if home region, 2 if in sphere of influence, 3 if outside.
     */
    protected final int getSphere(final Sector sector, final Nation receiver) {
        final char thisNationCodeLower = String.valueOf(receiver.getCode()).toLowerCase().charAt(0);
        final char thisSectorCodeLower = String.valueOf(sector.getPoliticalSphere()).toLowerCase().charAt(0);
        int sphere = 1;

        // Τα χ2 και x3 ισχύουν μόνο για τα Ευρωπαικά αν χτίζονται στο SoI ή εκτός SoI
        if (sector.getPosition().getRegion().getId() != RegionConstants.EUROPE) {
            return 1;
        }

        // Check if this is not home region
        if (thisNationCodeLower != thisSectorCodeLower) {
            sphere = 2;

            // Check if this is outside sphere of influence
            if (receiver.getSphereOfInfluence().toLowerCase().indexOf(thisSectorCodeLower) < 0) {
                sphere = 3;
            }
        }

        return sphere;
    }

    /**
     * Skip tutorial for a current game.
     *
     * @param gameId The game to skip the tutorial steps.
     * @return 1 for success.
     */
    public int skipTutorial(final int gameId) {
        ScenarioContextHolder.setScenario(HibernateUtil.DB_FREE);
        final Game thisGame = getGame(HibernateUtil.DB_FREE, gameId);

        if (thisGame.getTurn() <= 10) {
            final int oldTurn = thisGame.getTurn();
            final int turnsSkipped = 11 - oldTurn;
            thisGame.setTurn(11);
            gameManager.update(thisGame);

            //rename game files
            String mapsPath = "/srv/eaw1805/images/maps/s-1/" + gameId + "/";
            try {
                LOGGER.debug("Renaming files " + oldTurn + ", " + 11);

                for (int regionId = RegionConstants.EUROPE; regionId <= RegionConstants.AFRICA; regionId++) {
                    renameFile(mapsPath + "map-" + gameId + "-" + oldTurn + "-" + regionId + "-info.png", mapsPath + "map-" + gameId + "-11-" + regionId + "-info.png");
                    renameFile(mapsPath + "map-" + gameId + "-" + oldTurn + "-" + regionId + "-small.png", mapsPath + "map-" + gameId + "-11-" + regionId + "-small.png");
                    renameFile(mapsPath + "map-" + gameId + "-" + oldTurn + "-" + regionId + ".png", mapsPath + "map-" + gameId + "-11-" + regionId + ".png");
                    renameFile(mapsPath + "map-" + gameId + "-" + oldTurn + "-" + regionId + "-5-lowres.png", mapsPath + "map-" + gameId + "-11-" + regionId + "-5-lowres.png");
                    renameFile(mapsPath + "map-" + gameId + "-" + oldTurn + "-" + regionId + "-5-vlowres.png", mapsPath + "map-" + gameId + "-11-" + regionId + "-5-vlowres.png");
                    renameFile(mapsPath + "map-" + gameId + "-" + oldTurn + "-" + regionId + "-5-vvlowres.png", mapsPath + "map-" + gameId + "-11-" + regionId + "-5-vvlowres.png");
                    renameFile(mapsPath + "map-" + gameId + "-" + oldTurn + "-" + regionId + "-5.png", mapsPath + "map-" + gameId + "-11-" + regionId + "-5.png");

                    renameFile(mapsPath + "map-" + gameId + "-" + oldTurn + "-" + regionId + "-5-border-lowres.png", mapsPath + "map-" + gameId + "-11-" + regionId + "-5-border-lowres.png");
                    renameFile(mapsPath + "map-" + gameId + "-" + oldTurn + "-" + regionId + "-5-border-vlowres.png", mapsPath + "map-" + gameId + "-11-" + regionId + "-5-border-vlowres.png");
                    renameFile(mapsPath + "map-" + gameId + "-" + oldTurn + "-" + regionId + "-5-border-vvlowres.png", mapsPath + "map-" + gameId + "-11-" + regionId + "-5-border-vvlowres.png");
                    renameFile(mapsPath + "map-" + gameId + "-" + oldTurn + "-" + regionId + "-5-border.png", mapsPath + "map-" + gameId + "-11-" + regionId + "-5-border.png");
                }

            } catch (Exception e) {
                LOGGER.error("Unable to skip tutorial", e);
            }

            // Update turns played
            final Nation currentNation = getNation(HibernateUtil.DB_FREE, NationConstants.NATION_FRANCE);
            final User sender = userManager.getByID(userGameManager.list(thisGame, currentNation).get(0).getUserId());
            final Profile entry = profileManager.getByOwnerKey(sender, ProfileConstants.TURNS_PLAYED_SOLO);

            // If the entry does not exist, create
            if (entry == null) {
                final Profile newEntry = new Profile();
                newEntry.setUser(sender);
                newEntry.setKey(ProfileConstants.TURNS_PLAYED_SOLO);
                newEntry.setValue(turnsSkipped);
                profileManager.add(newEntry);

            } else {
                // Make sure players do not end up with negative VPs
                entry.setValue(entry.getValue() + turnsSkipped);
                profileManager.update(entry);
            }
        }
        return 1;
    }

    public void renameFile(String filename, String newFilename) throws Exception {
        final File file = new File(filename);

        // File (or directory) with new name
        final File file2 = new File(newFilename);
        if (file2.exists()) {
            throw new IOException("file exists");
        }

        // Rename file (or directory)
        final boolean success = file.renameTo(file2);
        if (!success) {
            LOGGER.debug("Failed to rename file : " + filename);
        }
    }

    /**
     * Get the current calendar.
     *
     * @param thisGame the Game instance.
     * @param turn     the Turn to calculate.
     * @return the calendar.
     */
    public final Calendar calendar(final Game thisGame, final int turn) {
        final Calendar thisCal;
        switch (thisGame.getScenarioId()) {
            case HibernateUtil.DB_S1:
                if (thisGame.getGameId() < 8) {
                    thisCal = calendar(1805, Calendar.JANUARY, turn);

                } else {
                    thisCal = calendar(1802, Calendar.APRIL, turn);
                }
                break;

            case HibernateUtil.DB_S2:
                thisCal = calendar(1805, Calendar.JANUARY, turn);
                break;

            case HibernateUtil.DB_S3:
                thisCal = calendar(1808, Calendar.SEPTEMBER, turn);
                break;

            case HibernateUtil.DB_FREE:
            default:
                thisCal = calendar(1804, Calendar.JANUARY, turn);

        }

        return thisCal;
    }

    /**
     * Get the current calendar.
     *
     * @param turn          the Turn to calculate.
     * @param startingYear  the starting year of the scenario.
     * @param startingMonth the starting month of the scenario.
     * @return the calendar.
     */
    protected final Calendar calendar(final int startingYear, final int startingMonth, final int turn) {
        final Calendar thisCal = Calendar.getInstance();
        thisCal.set(startingYear, startingMonth, 1);
        thisCal.add(Calendar.MONTH, turn);
        return thisCal;
    }

    /**
     * Instance GameManager class to perform queries
     * about user objects.
     */
    @Autowired
    @Qualifier("gameManagerBean")
    public transient GameManagerBean gameManager;

    /**
     * Instance NationManager class to perform queries
     * about nation objects.
     */
    @Autowired
    @Qualifier("nationManagerBean")
    public transient NationManagerBean nationManager;

    public NationManagerBean getNationManager() {
        return nationManager;
    }

    /**
     * Instance RegionManager class to perform queries
     * about region objects.
     */
    @Autowired
    @Qualifier("regionManagerBean")
    public transient RegionManagerBean regionManager;

    /**
     * Instance ArmyManagerBean class to perform queries
     * about army objects.
     */
    @Autowired
    @Qualifier("armyManagerBean")
    private transient ArmyManagerBean armyManager;

    /**
     * Instance ArmyTypeManager class to perform queries
     * about armyType objects.
     */
    @Autowired
    @Qualifier("armyTypeManagerBean")
    private transient ArmyTypeManagerBean armyTypeManager;

    /**
     * Instance BattalionManager class to perform queries
     * about battalion objects.
     */
    @Autowired
    @Qualifier("battalionManagerBean")
    private transient BattalionManagerBean battalionManager;

    /**
     * Instance BrigadeManager class to perform queries
     * about brigade objects.
     */
    @Autowired
    @Qualifier("brigadeManagerBean")
    public transient BrigadeManagerBean brigadeManager;

    public BrigadeManagerBean getBrigadeManager() {
        return brigadeManager;
    }

    /**
     * Instance CorpManagerBean class to perform queries
     * about army objects.
     */
    @Autowired
    @Qualifier("corpManagerBean")
    private transient CorpManagerBean corpManager;

    /**
     * Instance ShipTypeManagerBean class to perform queries
     * about armyType objects.
     */
    @Autowired
    @Qualifier("shipTypeManagerBean")
    private transient ShipTypeManagerBean shipTypeManager;

    /**
     * Instance ShipManager class to perform queries
     * about ship objects.
     */
    @Autowired
    @Qualifier("shipManagerBean")
    private transient ShipManagerBean shipManager;

    /**
     * Instance ProductionSiteManager class to perform queries
     * about productionSite objects.
     */
    @Autowired
    @Qualifier("productionSiteManagerBean")
    private transient ProductionSiteManagerBean productionSiteManager;

    /**
     * Instance NaturalResourceManager class to perform queries
     * about commander objects.
     */
    @Autowired
    @Qualifier("naturalResourceManagerBean")
    private transient NaturalResourceManagerBean naturalResourceManager;

    /**
     * Instance SectorManager class to perform queries
     * about commander objects.
     */
    @Autowired
    @Qualifier("sectorManagerBean")
    private transient SectorManagerBean sectorManager;

    public SectorManagerBean getSectorManager() {
        return sectorManager;
    }

    /**
     * Instance RelationsManager class to perform queries
     * about NationRelation objects.
     */
    @Autowired
    @Qualifier("relationsManagerBean")
    private volatile RelationsManagerBean relationsManager;

    public RelationsManagerBean getRelationsManager() {
        return relationsManager;
    }

    /**
     * Instance WarehouseManager class to perform queries
     * about warehouse objects.
     */
    @Autowired
    @Qualifier("warehouseManagerBean")
    private transient WarehouseManagerBean warehouseManager;

    /**
     * Instance GoodManager class to perform queries
     * about Good objects.
     */
    @Autowired
    @Qualifier("goodManagerBean")
    private transient GoodManagerBean goodManager;

    /**
     * Instance WarehouseManager class to perform queries
     * about playerOrder objects.
     */
    @Autowired
    @Qualifier("playerOrderManagerBean")
    private transient PlayerOrderManagerBean playerOrderManager;

    /**
     * Instance BarrackManager class to perform queries
     * about Barracks objects.
     */
    @Autowired
    @Qualifier("barrackManagerBean")
    private transient BarrackManagerBean barrackManager;

    /**
     * Instance FleetManager class to perform queries
     * about Fleet objects.
     */
    @Autowired
    @Qualifier("fleetManagerBean")
    private transient FleetManagerBean fleetManager;

    /**
     * Instance FleetManager class to perform queries
     * about Fleet objects.
     */
    @Autowired
    @Qualifier("commanderManagerBean")
    private transient CommanderManagerBean commanderManager;

    /**
     * Instance SpyManager class to perform queries
     * about Spy objects.
     */
    @Autowired
    @Qualifier("spyManagerBean")
    public transient SpyManagerBean spyManager;

    /**
     * Instance BaggageTrainManager class to perform queries
     * about BaggageTrain objects.
     */
    @Autowired
    @Qualifier("baggageTrainManagerBean")
    private transient BaggageTrainManagerBean baggageTrainManager;

    /**
     * Instance TradeCityManager class to perform queries
     * about TradeCity objects.
     */
    @Autowired
    @Qualifier("tradeCityManagerBean")
    private transient TradeCityManagerBean tradeCityManager;

    /**
     * Instance ReportManager class to perform queries
     * about report objects.
     */
    @Autowired
    @Qualifier("reportManagerBean")
    public transient ReportManagerBean reportManager;

    @Autowired
    @Qualifier("gameSettingsManagerBean")
    public transient GameSettingsManagerBean settingsManager;

    @Autowired
    @Qualifier("tacticalBattleReportManagerBean")
    public transient TacticalBattleReportManagerBean tacticalBattleManager;


    @Autowired
    @Qualifier("navalBattleReportManagerBean")
    public transient NavalBattleReportManagerBean navalBattleManager;


    @Autowired
    @Qualifier("newsManagerBean")
    public transient NewsManagerBean newsManager;

    /**
     * Instance of UserManager class.
     */
    @Autowired
    @Qualifier("userManagerBean")
    protected transient UserManagerBean userManager;

    @Autowired
    @Qualifier("usersGamesManagerBean")
    public transient UserGameManagerBean userGameManager;

    @Autowired
    @Qualifier("messageManagerBean")
    public transient MessageManagerBean messageManager;

    @Autowired
    @Qualifier("chatMessageManagerBean")
    public transient ChatMessageManagerBean chatManager;

    private final static class Data {

        private final int gameId;
        private final int nationId;
        private final String text;
        private final String author;
        private final String encodedEmail;
        private final long time;
        private final String username;

        public Data(String author, String text, int gameId, int nationId, String encodedEmail,
                    long time, final String username) {
            this.author = author;
            this.text = text;
            this.gameId = gameId;
            this.nationId = nationId;
            this.encodedEmail = encodedEmail;
            this.time = time;
            this.username = username;
        }

        public String toStringForClient() {
            return "{ \"text\" : \"" + text
                    + "\", \"author\" : \"" + author
                    + "\" , \"time\" : " + time
                    + ", \"gameId\" :" + gameId
                    + ", \"nationId\" : " + nationId
                    + ", \"encodedEmail\" : \"" + encodedEmail + "\""
                    + ", \"username\" : \"" + username + "\"}";
        }

        public String toStringForDB() {
            return "{ \"text\" : \"" + text + "\", \"author\" : \"" + author + "\" , \"time\" : "
                    + time + ", \"gameIdStr\" :" + gameId + ", \"nationIdStr\" : " + nationId + "\"}";
        }
    }

    /**
     * Instance of ArticleManager.
     */
    @Autowired
    @Qualifier("articleManagerBean")
    private transient ArticleManager articleManager;


    /**
     * Instance of ProfileManagerBean.
     */
    @Autowired
    @Qualifier("profileManagerBean")
    private transient ProfileManagerBean profileManager;

    /**
     * Instance of AchievementManager.
     */
    @Autowired
    @Qualifier("achievementManagerBean")
    private transient AchievementManagerBean achievementManager;

    public AchievementManagerBean getAchievementManager() {
        return achievementManager;
    }

}
