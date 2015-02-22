package com.eaw1805.www.controllers.game;

import com.eaw1805.data.constants.GameConstants;
import com.eaw1805.data.managers.beans.*;
import com.eaw1805.data.model.Game;
import com.eaw1805.www.commands.GameCommand;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractRestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Processes a game.
 */
public class ProcessGame
        extends AbstractRestController
        implements GameConstants {

    public ProcessGame() {
        super();

        // Make sure to set which method this controller will support.
        this.setSupportedMethods(new String[]{METHOD_GET});
    }

    protected ModelAndView handle(final HttpServletRequest request,
                                  final HttpServletResponse response, final Object commandObj, final BindException errors)
            throws Exception {

        final GameCommand command = (GameCommand) commandObj;

        // Retrieve Game entity
        final String gameId = command.getGameId();
        final Game thisGame;
        if ((gameId == null) || (gameId.isEmpty())) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/home");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);

        } else {
            thisGame = gameManager.getByID(Integer.parseInt(gameId));
            if (thisGame == null) {
                final RedirectView redirectView = new RedirectView(request.getContextPath() + "/home");
                redirectView.setExposeModelAttributes(false);
                return new ModelAndView(redirectView);
            }
        }

        thisGame.setStatus(GAME_SCHED);
        gameManager.update(thisGame);

        final RedirectView redirectView = new RedirectView(request.getContextPath()  + "/scenario/" + thisGame.getScenarioIdToString() + "/game/" + thisGame.getGameId() + "/info");
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);

        /*
        final Logger logger = Logger.getRootLogger();
        final String appenderName = "servletOutputAppender";

        Appender servletAppender = logger.getAppender(appenderName);
        final OutputStream out = response.getOutputStream();
        final PrintWriter writer = new PrintWriter(out, true);

        if (servletAppender == null) {
            servletAppender = new WriterAppender(new SimpleLayout(), out);
            servletAppender.setName(appenderName);
            logger.addAppender(servletAppender);
        }

        try {
            // Pass manager instances
            ArmyManager.setInstance(armyManager);
            ArmyTypeManager.setInstance(armyTypeManager);
            BaggageTrainManager.setInstance(baggageTrainManager);
            BarrackManager.setInstance(barrackManager);
            BattalionManager.setInstance(battalionManager);
            BrigadeManager.setInstance(brigadeManager);
            CommanderManager.setInstance(commanderManager);
            CommanderNameManager.setInstance(commanderNameManager);
            CorpManager.setInstance(corpManager);
            GameManager.setInstance(gameManager);
            GoodManager.setInstance(goodManager);
            NationManager.setInstance(nationManager);
            NaturalResourceManager.setInstance(naturalResourceManager);
            NewsManager.setInstance(newsManager);
            PlayerOrderManager.setInstance(playerOrderManager);
            RankManager.setInstance(rankManager);
            RegionManager.setInstance(regionManager);
            RelationsManager.setInstance(relationsManager);
            ReportManager.setInstance(reportManager);
            SectorManager.setInstance(sectorManager);
            ShipTypeManager.setInstance(shipTypeManager);
            ShipManager.setInstance(shipManager);
            SpyManager.setInstance(spyManager);
            FleetManager.setInstance(fleetManager);
            TacticalBattleReportManager.setInstance(tacticalBattleReportManager);
            TerrainManager.setInstance(terrainManager);
            TradeCityManager.setInstance(tradeCityManager);
            ProductionSiteManager.setInstance(productionSiteManager);
            UserGameManager.setInstance(userGameManager);
            UserManager.setInstance(userManager);
            WarehouseManager.setInstance(warehouseManager);

            // find the real path
            //final String urlRoot = request.getSession().getServletContext().getRealPath(".");

            final GameEngine thisGE = new GameEngine(thisGame.getGameId(), ImageController.ENGINE_PATH);
            thisGE.init();
            thisGE.process();

        } finally {
            logger.removeAppender(appenderName);
        }

        writer.flush();
        writer.close();

        return null;
        */
    }

    /**
     * Instance GameManager class to perform queries
     * about game objects.
     */
    private transient GameManagerBean gameManager;

    /**
     * Setter method used by spring to inject a gameManager bean.
     *
     * @param injGameManager a gameManager bean.
     */
    public void setGameManager(final GameManagerBean injGameManager) {
        gameManager = injGameManager;
    }

    /**
     * Instance UserManager class to perform queries
     * about user objects.
     */
    private transient UserManagerBean userManager;

    /**
     * Setter method used by spring to inject a userManager bean.
     *
     * @param injUserManager a userManager bean.
     */
    public void setUserManager(final UserManagerBean injUserManager) {
        userManager = injUserManager;
    }

    /**
     * Instance UserGameManager class to perform queries
     * about user objects.
     */
    private transient UserGameManagerBean userGameManager;

    /**
     * Setter method used by spring to inject an entity manager bean.
     *
     * @param injUserGameManager a UserGameManager bean.
     */
    public void setUserGameManager(final UserGameManagerBean injUserGameManager) {
        userGameManager = injUserGameManager;
    }

    /**
     * Instance NationManager class to perform queries
     * about nation objects.
     */
    private transient NationManagerBean nationManager;

    /**
     * Setter method used by spring to inject a nationManager bean.
     *
     * @param injNationManager a nationManager bean.
     */
    public void setNationManager(final NationManagerBean injNationManager) {
        nationManager = injNationManager;
    }

    /**
     * Instance RegionManager class to perform queries
     * about region objects.
     */
    private transient RegionManagerBean regionManager;

    /**
     * Setter method used by spring to inject a RegionManager bean.
     *
     * @param injRegionManager a RegionManager bean.
     */
    public void setRegionManager(final RegionManagerBean injRegionManager) {
        regionManager = injRegionManager;
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
     * Instance ArmyTypeManager class to perform queries
     * about armyType objects.
     */
    private transient ArmyTypeManagerBean armyTypeManager;

    /**
     * Setter method used by spring to inject a ArmyTypeManager bean.
     *
     * @param injArmyTypeManager a ArmyTypeManager bean.
     */
    public void setArmyTypeManager(final ArmyTypeManagerBean injArmyTypeManager) {
        this.armyTypeManager = injArmyTypeManager;
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
        brigadeManager = injBrigadeManager;
    }

    /**
     * Instance BattalionManager class to perform queries
     * about battalion objects.
     */
    private transient BattalionManagerBean battalionManager;

    /**
     * Setter method used by spring to inject a BattalionManager bean.
     *
     * @param injBattalionManager a BattalionManager bean.
     */
    public void setBattalionManager(final BattalionManagerBean injBattalionManager) {
        battalionManager = injBattalionManager;
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
     * Instance CorpManagerBean class to perform queries
     * about army objects.
     */
    private transient CorpManagerBean corpManager;

    /**
     * Setter method used by spring to inject a CorpManagerBean bean.
     *
     * @param injCorpManager a ArmyManagerBean bean.
     */
    public void setCorpManager(final CorpManagerBean injCorpManager) {
        corpManager = injCorpManager;
    }

    /**
     * Instance ShipTypeManagerBean class to perform queries
     * about armyType objects.
     */
    private transient ShipTypeManagerBean shipTypeManager;

    /**
     * Setter method used by spring to inject a ShipTypeManager bean.
     *
     * @param injShipTypeManager a ShipTypeManager bean.
     */
    public void setShipTypeManager(final ShipTypeManagerBean injShipTypeManager) {
        shipTypeManager = injShipTypeManager;
    }

    /**
     * Instance ShipManager class to perform queries
     * about ship objects.
     */
    private transient ShipManagerBean shipManager;

    /**
     * Setter method used by spring to inject a ShipManager bean.
     *
     * @param injShipManager a shipManager bean.
     */
    public void setShipManager(final ShipManagerBean injShipManager) {
        shipManager = injShipManager;
    }

    /**
     * Instance FleetManager class to perform queries
     * about fleet objects.
     */
    private transient FleetManagerBean fleetManager;

    /**
     * Setter method used by spring to inject a FleetManager bean.
     *
     * @param injFleetManager a fleetManager bean.
     */
    public void setFleetManager(final FleetManagerBean injFleetManager) {
        fleetManager = injFleetManager;
    }

    /**
     * Instance ProductionSiteManager class to perform queries
     * about productionSite objects.
     */
    private transient ProductionSiteManagerBean productionSiteManager;

    /**
     * Setter method used by spring to inject a ProductionSiteManager bean.
     *
     * @param injProductionSiteManager a ProductionSiteManager bean.
     */
    public void setProductionSiteManager(final ProductionSiteManagerBean injProductionSiteManager) {
        productionSiteManager = injProductionSiteManager;
    }

    /**
     * Instance NaturalResourceManager class to perform queries
     * about commander objects.
     */
    private transient NaturalResourceManagerBean naturalResourceManager;

    /**
     * Setter method used by spring to inject a NaturalResourceManager bean.
     *
     * @param injNaturalResourceManager a NaturalResourceManager bean.
     */
    public void setNaturalResourceManager(final NaturalResourceManagerBean injNaturalResourceManager) {
        naturalResourceManager = injNaturalResourceManager;
    }

    /**
     * Instance SectorManager class to perform queries
     * about commander objects.
     */
    private transient SectorManagerBean sectorManager;

    /**
     * Setter method used by spring to inject a SectorManager bean.
     *
     * @param injSectorManager a SectorManager bean.
     */
    public void setSectorManager(final SectorManagerBean injSectorManager) {
        sectorManager = injSectorManager;
    }

    /**
     * Instance RelationsManager class to perform queries
     * about NationRelation objects.
     */
    private transient RelationsManagerBean relationsManager;

    /**
     * Setter method used by spring to inject a RelationsManager bean.
     *
     * @param injRelationsManager a RelationsManager bean.
     */
    public void setRelationsManager(final RelationsManagerBean injRelationsManager) {
        relationsManager = injRelationsManager;
    }

    /**
     * Instance WarehouseManager class to perform queries
     * about warehouse objects.
     */
    private transient WarehouseManagerBean warehouseManager;

    /**
     * Setter method used by spring to inject a WarehouseManager bean.
     *
     * @param injWarehouseManager a WarehouseManager bean.
     */
    public void setWarehouseManager(final WarehouseManagerBean injWarehouseManager) {
        warehouseManager = injWarehouseManager;
    }

    /**
     * Instance GoodManager class to perform queries
     * about Good objects.
     */
    private transient GoodManagerBean goodManager;

    /**
     * Setter method used by spring to inject a GoodManager bean.
     *
     * @param injGoodManager a GoodManager bean.
     */
    public void setGoodManager(final GoodManagerBean injGoodManager) {
        goodManager = injGoodManager;
    }

    /**
     * Instance PlayerOrderManager class to perform queries
     * about playerOrder objects.
     */
    private transient PlayerOrderManagerBean playerOrderManager;

    /**
     * Setter method used by spring to inject a PlayerOrderManager bean.
     *
     * @param injPlayerOrderManager a playerOrderManager bean.
     */
    public void setPlayerOrderManager(final PlayerOrderManagerBean injPlayerOrderManager) {
        playerOrderManager = injPlayerOrderManager;
    }

    /**
     * Instance CommanderNameManager class to perform queries
     * about CommanderName objects.
     */
    private transient CommanderNameManagerBean commanderNameManager;

    /**
     * Setter method used by spring to inject a PlayerOrderManager bean.
     *
     * @param injCommanderNameManager a commanderNameManager bean.
     */
    public void setCommanderNameManager(final CommanderNameManagerBean injCommanderNameManager) {
        commanderNameManager = injCommanderNameManager;
    }

    /**
     * Instance RankManager class to perform queries
     * about Rank objects.
     */
    private transient RankManagerBean rankManager;

    /**
     * Setter method used by spring to inject a RankManager bean.
     *
     * @param injRankManager a RankManager bean.
     */
    public void setRankManager(final RankManagerBean injRankManager) {
        rankManager = injRankManager;
    }

    /**
     * Instance ReportManager class to perform queries
     * about report objects.
     */
    private transient ReportManagerBean reportManager;

    /**
     * Setter method used by spring to inject a reportManager bean.
     *
     * @param injReportManager a reportManager bean.
     */
    public void setReportManager(final ReportManagerBean injReportManager) {
        reportManager = injReportManager;
    }

    /**
     * Instance TerrainManagerBean class to perform queries
     * about terrain objects.
     */
    private transient TerrainManagerBean terrainManager;

    /**
     * Setter method used by spring to inject a TerrainManager bean.
     *
     * @param injTerrainManagerBean a terrainManager bean.
     */
    public void setTerrainManager(final TerrainManagerBean injTerrainManagerBean) {
        terrainManager = injTerrainManagerBean;
    }

    /**
     * Instance BarrackManagerBean class to perform queries
     * about barrack objects.
     */
    private transient BarrackManagerBean barrackManager;

    /**
     * Setter method used by spring to inject a BarrackManager bean.
     *
     * @param injBarrackManagerBean a barrackManager bean.
     */
    public void setBarrackManager(final BarrackManagerBean injBarrackManagerBean) {
        barrackManager = injBarrackManagerBean;
    }

    /**
     * Instance SpyManager class to perform queries
     * about Spy objects.
     */
    private transient SpyManagerBean spyManager;

    /**
     * Setter method used by spring to inject a SpyManagerBean bean.
     *
     * @param injSpyManager a commanderManager bean.
     */
    public void setSpyManager(final SpyManagerBean injSpyManager) {
        spyManager = injSpyManager;
    }

    /**
     * Instance BaggageTrainManager class to perform queries
     * about BaggageTrain objects.
     */
    private transient BaggageTrainManagerBean baggageTrainManager;

    /**
     * Setter method used by spring to inject a BaggageTrainManagerBean bean.
     *
     * @param injBaggageTrainManager a baggageTrainManager bean.
     */
    public void setBaggageTrainManager(final BaggageTrainManagerBean injBaggageTrainManager) {
        baggageTrainManager = injBaggageTrainManager;
    }

    /**
     * Instance tradeCityManager class to perform queries
     * about TradeCity objects.
     */
    private transient TradeCityManagerBean tradeCityManager;

    /**
     * Setter method used by spring to inject a tradeCityManager bean.
     *
     * @param injTradeCityManager a tradeCityManager bean.
     */
    public void setTradeCityManager(final TradeCityManagerBean injTradeCityManager) {
        tradeCityManager = injTradeCityManager;
    }

    /**
     * Instance TacticalBattleReportManager class to perform queries
     * about tacticalBattleReport objects.
     */
    private transient TacticalBattleReportManagerBean tacticalBattleReportManager;

    /**
     * Setter method used by spring to inject a TacticalBattleReportManager bean.
     *
     * @param injTacticalBattleReportManager a tacticalBattleReportManager bean.
     */
    public void setTacticalBattleReportManager(final TacticalBattleReportManagerBean injTacticalBattleReportManager) {
        tacticalBattleReportManager = injTacticalBattleReportManager;
    }

    /**
     * Instance NewsManager class to perform queries
     * about news objects.
     */
    private transient NewsManagerBean newsManager;

    /**
     * Setter method used by spring to inject a newsManager bean.
     *
     * @param injNewsManager a newsManager bean.
     */
    public void setNewsManager(final NewsManagerBean injNewsManager) {
        newsManager = injNewsManager;
    }

}
