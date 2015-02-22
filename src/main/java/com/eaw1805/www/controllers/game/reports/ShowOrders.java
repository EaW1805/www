package com.eaw1805.www.controllers.game.reports;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.NavigationConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.News;
import com.eaw1805.data.model.PlayerOrder;
import com.eaw1805.data.model.User;
import com.eaw1805.orders.army.*;
import com.eaw1805.orders.economy.BuildBaggageTrain;
import com.eaw1805.orders.economy.ChangeTaxation;
import com.eaw1805.orders.economy.RepairBaggageTrain;
import com.eaw1805.orders.economy.ScuttleBaggageTrain;
import com.eaw1805.orders.economy.TransferFirst;
import com.eaw1805.orders.economy.TransferSecond;
import com.eaw1805.orders.fleet.BuildShip;
import com.eaw1805.orders.fleet.DemolishFleet;
import com.eaw1805.orders.fleet.HandOverShip;
import com.eaw1805.orders.fleet.LoadFirst;
import com.eaw1805.orders.fleet.LoadSecond;
import com.eaw1805.orders.fleet.RenameShip;
import com.eaw1805.orders.fleet.RepairFleet;
import com.eaw1805.orders.fleet.RepairShip;
import com.eaw1805.orders.fleet.ScuttleShip;
import com.eaw1805.orders.fleet.SetupFleet;
import com.eaw1805.orders.fleet.ShipJoinFleet;
import com.eaw1805.orders.fleet.UnloadFirst;
import com.eaw1805.orders.fleet.UnloadSecond;
import com.eaw1805.orders.map.BuildProductionSite;
import com.eaw1805.orders.map.DecreasePopDensity;
import com.eaw1805.orders.map.DemolishProductionSite;
import com.eaw1805.orders.map.HandOverTerritoryOrderProcessor;
import com.eaw1805.orders.map.IncreasePopDensity;
import com.eaw1805.orders.movement.ArmyForcedMovement;
import com.eaw1805.orders.movement.ArmyMovement;
import com.eaw1805.orders.movement.BaggageTrainMovement;
import com.eaw1805.orders.movement.BrigadeForcedMovement;
import com.eaw1805.orders.movement.BrigadeMovement;
import com.eaw1805.orders.movement.CommanderMovement;
import com.eaw1805.orders.movement.CorpForcedMovement;
import com.eaw1805.orders.movement.CorpMovement;
import com.eaw1805.orders.movement.FleetMovement;
import com.eaw1805.orders.movement.FleetPatrolMovement;
import com.eaw1805.orders.movement.MerchantShipMovement;
import com.eaw1805.orders.movement.ShipPatrolMovement;
import com.eaw1805.orders.movement.SpyMovement;
import com.eaw1805.orders.movement.WarShipMovement;
import com.eaw1805.orders.politics.PoliticsOrderProcessor;
import com.eaw1805.www.controllers.GameReportsController;
import com.eaw1805.data.cache.Cachable;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Display the orders for a given nation of a particular game.
 */
@SuppressWarnings("restriction")
@Controller
public class ShowOrders
        extends GameReportsController
        implements OrderConstants, ArmyConstants, RelationConstants, NavigationConstants, RegionConstants {

    public static final Map<Integer, String> PHASE_NAMES = new HashMap<Integer, String>();

    static {
        PHASE_NAMES.put(10, "Organize Land & Naval Forces");
        PHASE_NAMES.put(20, "Organize Territories & Population");
        PHASE_NAMES.put(30, "Troop Training at Barracks");
        PHASE_NAMES.put(40, "Build Baggage Trains");
        PHASE_NAMES.put(50, "Constructions at Shipyards");
        PHASE_NAMES.put(60, "Repair Ships & Baggage Trains");
        PHASE_NAMES.put(70, "Scuttle & Disband");
        PHASE_NAMES.put(80, "1st Loading/Unloading & Trading Phase");
        PHASE_NAMES.put(90, "Movement");
        PHASE_NAMES.put(100, "2nd Loading/Unloading & Trading Phase");
        PHASE_NAMES.put(110, "Hand-over Ships & Cede Territories");
        PHASE_NAMES.put(120, "Politics");
        PHASE_NAMES.put(130, "Taxation");
        PHASE_NAMES.put(150, "Other Orders");
    }

    public static final Map<Integer, Integer> ORDER_PHASE = new HashMap<Integer, Integer>();

    static {
        // Brigades
        ORDER_PHASE.put(AdditionalBattalions.ORDER_TYPE, 30);
        ORDER_PHASE.put(BrigadeJoinCorp.ORDER_TYPE, 10);
        ORDER_PHASE.put(DemolishBattalion.ORDER_TYPE, 70);
        ORDER_PHASE.put(DemolishBrigade.ORDER_TYPE, 70);
        ORDER_PHASE.put(ExchangeBattalions.ORDER_TYPE, 10);
        ORDER_PHASE.put(IncreaseExperience.ORDER_TYPE, 30);
        ORDER_PHASE.put(IncreaseHeadcount.ORDER_TYPE, 30);
        ORDER_PHASE.put(MergeBattalions.ORDER_TYPE, 30);
        ORDER_PHASE.put(RenameBrigade.ORDER_TYPE, 10);
        ORDER_PHASE.put(SetupBrigade.ORDER_TYPE, 30);

        // Corps
        ORDER_PHASE.put(CommanderJoinCorp.ORDER_TYPE, 10);
        ORDER_PHASE.put(CorpJoinArmy.ORDER_TYPE, 10);
        ORDER_PHASE.put(DemolishCorp.ORDER_TYPE, 10);
        ORDER_PHASE.put(RenameCorp.ORDER_TYPE, 10);
        ORDER_PHASE.put(SetupCorp.ORDER_TYPE, 10);
        ORDER_PHASE.put(IncreaseHeadcountCorps.ORDER_TYPE, 30);
        ORDER_PHASE.put(IncreaseExperienceCorps.ORDER_TYPE, 30);

        // Armies
        ORDER_PHASE.put(CommanderJoinArmy.ORDER_TYPE, 10);
        ORDER_PHASE.put(DemolishArmy.ORDER_TYPE, 10);
        ORDER_PHASE.put(RenameArmy.ORDER_TYPE, 10);
        ORDER_PHASE.put(SetupArmy.ORDER_TYPE, 10);
        ORDER_PHASE.put(IncreaseHeadcountArmy.ORDER_TYPE, 30);
        ORDER_PHASE.put(IncreaseExperienceArmy.ORDER_TYPE, 30);

        // Commanders
        ORDER_PHASE.put(HireCommander.ORDER_TYPE, 10);
        ORDER_PHASE.put(DismissCommander.ORDER_TYPE, 10);
        ORDER_PHASE.put(RenameCommander.ORDER_TYPE, 10);
        ORDER_PHASE.put(CommanderLeaveUnit.ORDER_TYPE, 10);

        // Fleets
        ORDER_PHASE.put(BuildShip.ORDER_TYPE, 50);
        ORDER_PHASE.put(RepairShip.ORDER_TYPE, 60);
        ORDER_PHASE.put(ScuttleShip.ORDER_TYPE, 70);
        ORDER_PHASE.put(ShipJoinFleet.ORDER_TYPE, 10);
        ORDER_PHASE.put(DemolishFleet.ORDER_TYPE, 10);
        ORDER_PHASE.put(RenameShip.ORDER_TYPE, 10);
        ORDER_PHASE.put(SetupFleet.ORDER_TYPE, 10);
        ORDER_PHASE.put(RepairFleet.ORDER_TYPE, 60);
        ORDER_PHASE.put(LoadFirst.ORDER_TYPE, 80);
        ORDER_PHASE.put(LoadSecond.ORDER_TYPE, 100);
        ORDER_PHASE.put(UnloadFirst.ORDER_TYPE, 80);
        ORDER_PHASE.put(UnloadSecond.ORDER_TYPE, 100);

        // Economy
        ORDER_PHASE.put(ChangeTaxation.ORDER_TYPE, 130);
        ORDER_PHASE.put(BuildBaggageTrain.ORDER_TYPE, 40);
        ORDER_PHASE.put(RepairBaggageTrain.ORDER_TYPE, 60);
        ORDER_PHASE.put(ScuttleBaggageTrain.ORDER_TYPE, 70);
        ORDER_PHASE.put(TransferFirst.ORDER_TYPE, 80);
        ORDER_PHASE.put(TransferSecond.ORDER_TYPE, 100);
        ORDER_PHASE.put(RenameBarrack.ORDER_TYPE, 20);

        // Map
        ORDER_PHASE.put(BuildProductionSite.ORDER_TYPE, 20);
        ORDER_PHASE.put(DemolishProductionSite.ORDER_TYPE, 20);
        ORDER_PHASE.put(IncreasePopDensity.ORDER_TYPE, 20);
        ORDER_PHASE.put(DecreasePopDensity.ORDER_TYPE, 20);
        ORDER_PHASE.put(HandOverTerritoryOrderProcessor.ORDER_TYPE, 110);
        ORDER_PHASE.put(HandOverShip.ORDER_TYPE, 110);

        // Movement
        ORDER_PHASE.put(OrderConstants.ORDER_M_UNIT, 90);
        ORDER_PHASE.put(BrigadeMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(BrigadeForcedMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(CorpMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(CorpForcedMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(ArmyMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(ArmyForcedMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(CommanderMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(SpyMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(BaggageTrainMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(MerchantShipMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(WarShipMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(ShipPatrolMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(FleetMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(FleetPatrolMovement.ORDER_TYPE, 90);

        // Politics
        ORDER_PHASE.put(PoliticsOrderProcessor.ORDER_TYPE, 120);
    }

    public static final Map<Integer, String> ORDER_NAMES = new HashMap<Integer, String>();

    static {
        // Brigades
        ORDER_NAMES.put(AdditionalBattalions.ORDER_TYPE, "Setup Additional Battalions");
        ORDER_NAMES.put(BrigadeJoinCorp.ORDER_TYPE, "Brigade Join Corps");
        ORDER_NAMES.put(DemolishBattalion.ORDER_TYPE, "Demolish Battalion");
        ORDER_NAMES.put(DemolishBrigade.ORDER_TYPE, "Demolish Brigade");
        ORDER_NAMES.put(ExchangeBattalions.ORDER_TYPE, "Exchange Battalions");
        ORDER_NAMES.put(IncreaseExperience.ORDER_TYPE, "Increase Experience");
        ORDER_NAMES.put(IncreaseHeadcount.ORDER_TYPE, "Increase Headcount");
        ORDER_NAMES.put(MergeBattalions.ORDER_TYPE, "Merge Battalions");
        ORDER_NAMES.put(RenameBrigade.ORDER_TYPE, "Rename Brigade");
        ORDER_NAMES.put(SetupBrigade.ORDER_TYPE, "Setup Brigade");

        // Corps
        ORDER_NAMES.put(CommanderJoinCorp.ORDER_TYPE, "Commander Join Corps");
        ORDER_NAMES.put(CorpJoinArmy.ORDER_TYPE, "Corps Join Army");
        ORDER_NAMES.put(DemolishCorp.ORDER_TYPE, "Demolish Corps");
        ORDER_NAMES.put(RenameCorp.ORDER_TYPE, "Rename Corps");
        ORDER_NAMES.put(SetupCorp.ORDER_TYPE, "Setup Corps");
        ORDER_NAMES.put(IncreaseHeadcountCorps.ORDER_TYPE, "Increase Headcount Corps");
        ORDER_NAMES.put(IncreaseExperienceCorps.ORDER_TYPE, "Increase Experience Corps");

        // Armies
        ORDER_NAMES.put(CommanderJoinArmy.ORDER_TYPE, "Commander Join Army");
        ORDER_NAMES.put(DemolishArmy.ORDER_TYPE, "Demolish Army");
        ORDER_NAMES.put(RenameArmy.ORDER_TYPE, "Rename Army");
        ORDER_NAMES.put(SetupArmy.ORDER_TYPE, "Setup Army");
        ORDER_NAMES.put(IncreaseHeadcountArmy.ORDER_TYPE, "Increase Headcount Army");
        ORDER_NAMES.put(IncreaseExperienceArmy.ORDER_TYPE, "Increase Experience Army");

        // Commanders
        ORDER_NAMES.put(HireCommander.ORDER_TYPE, "Hire Commander");
        ORDER_NAMES.put(RenameCommander.ORDER_TYPE, "Rename Commander");
        ORDER_NAMES.put(CommanderLeaveUnit.ORDER_TYPE, "Remove Commander from Army/Corps");


        // Fleets
        ORDER_NAMES.put(BuildShip.ORDER_TYPE, "Build Ship");
        ORDER_NAMES.put(RepairShip.ORDER_TYPE, "Repair Ship");
        ORDER_NAMES.put(ScuttleShip.ORDER_TYPE, "Remove Ship");
        ORDER_NAMES.put(ShipJoinFleet.ORDER_TYPE, "Ship Join Fleet");
        ORDER_NAMES.put(DemolishFleet.ORDER_TYPE, "Demolish Fleet");
        ORDER_NAMES.put(RenameShip.ORDER_TYPE, "Rename Ship");
        ORDER_NAMES.put(SetupFleet.ORDER_TYPE, "Setup Fleet");
        ORDER_NAMES.put(RepairFleet.ORDER_TYPE, "Repair Fleet");
        ORDER_NAMES.put(LoadFirst.ORDER_TYPE, "Load (1)");
        ORDER_NAMES.put(LoadSecond.ORDER_TYPE, "Load (2)");
        ORDER_NAMES.put(UnloadFirst.ORDER_TYPE, "Unload (1)");
        ORDER_NAMES.put(UnloadSecond.ORDER_TYPE, "Unload (2)");

        // Economy
        ORDER_NAMES.put(ChangeTaxation.ORDER_TYPE, "Change Taxation");
        ORDER_NAMES.put(BuildBaggageTrain.ORDER_TYPE, "Build Baggage Train");
        ORDER_NAMES.put(RepairBaggageTrain.ORDER_TYPE, "Repair Baggage Train");
        ORDER_NAMES.put(ScuttleBaggageTrain.ORDER_TYPE, "Scuttle Baggage Train");
        ORDER_NAMES.put(TransferFirst.ORDER_TYPE, "Load/Unload/Sell/Buy Goods (P1)");
        ORDER_NAMES.put(TransferSecond.ORDER_TYPE, "Load/Unload/Sell/Buy Goods (P2)");

        // Map
        ORDER_NAMES.put(BuildProductionSite.ORDER_TYPE, "Build Production Site");
        ORDER_NAMES.put(DemolishProductionSite.ORDER_TYPE, "Demolish Production Site");
        ORDER_NAMES.put(IncreasePopDensity.ORDER_TYPE, "Increase Sector Population Density");
        ORDER_NAMES.put(DecreasePopDensity.ORDER_TYPE, "Decrease Sector Population Density");
        ORDER_NAMES.put(HandOverTerritoryOrderProcessor.ORDER_TYPE, "Hand-Over Territory");
        ORDER_NAMES.put(HandOverShip.ORDER_TYPE, "Hand-Over Ship");

        // Movement
        ORDER_NAMES.put(OrderConstants.ORDER_M_UNIT, "Movement");
        ORDER_NAMES.put(BrigadeMovement.ORDER_TYPE, "Brigade Movement");
        ORDER_NAMES.put(BrigadeForcedMovement.ORDER_TYPE, "Brigade Forced Movement");
        ORDER_NAMES.put(CorpMovement.ORDER_TYPE, "Corps Movement");
        ORDER_NAMES.put(CorpForcedMovement.ORDER_TYPE, "Corps Forced Movement");
        ORDER_NAMES.put(ArmyMovement.ORDER_TYPE, "Army Movement");
        ORDER_NAMES.put(ArmyForcedMovement.ORDER_TYPE, "Army Forced Movement");
        ORDER_NAMES.put(CommanderMovement.ORDER_TYPE, "Commander Movement");
        ORDER_NAMES.put(SpyMovement.ORDER_TYPE, "Spy Movement");
        ORDER_NAMES.put(BaggageTrainMovement.ORDER_TYPE, "Baggage Train Movement");
        ORDER_NAMES.put(MerchantShipMovement.ORDER_TYPE, "Merchant Ship Movement");
        ORDER_NAMES.put(WarShipMovement.ORDER_TYPE, "Ship Movement");
        ORDER_NAMES.put(ShipPatrolMovement.ORDER_TYPE, "Ship Patrol");
        ORDER_NAMES.put(FleetMovement.ORDER_TYPE, "Fleet Movement");
        ORDER_NAMES.put(FleetPatrolMovement.ORDER_TYPE, "Fleet Patrol");

        // Politics
        ORDER_NAMES.put(PoliticsOrderProcessor.ORDER_TYPE, "Change Relations");
    }

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowOrders.class);


    @RequestMapping(method = RequestMethod.GET, value = "/report/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/orders/{turnStr}")
    protected ModelAndView handle(@PathVariable final String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  @PathVariable final String turnStr,
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
        if (thisUser.getUserType() != 3 && getUserGameManager().list(thisUser, thisGame, thisNation).isEmpty()) {
            throw new InvalidPageException("Page not found");
        }

        // Retrieve Turn
        final int turnInt = getTurn(thisGame.getTurn(), turnStr);

        // produce report & prepare data to pass to jsp
        final Map<String, Object> refData;

        if (turnInt == thisGame.getTurn()) {
            refData = prepareReport(thisGame, thisNation);

        } else {
            refData = prepareReport(thisGame, thisNation, turnInt);
        }

        // Prepare data to pass to jsp
        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show orders for game=" + gameId + "/turn=" + turnInt + "/nation=" + thisNation.getName());
        if (request.getParameter("eawClient") == null) {
            return new ModelAndView("game/orders", refData);
        } else {
            return new ModelAndView("game/ordersMinimal", refData);
        }

    }

    /**
     * Report current turn orders. THIS MUST NOT BE CACHED !!
     *
     * @param thisGame   the game to examine.
     * @param thisNation the nation to examine.
     * @return the map with the data that will be passed to the jsp.
     */
    public Map<String, Object> prepareReport(final Game thisGame,
                                             final Nation thisNation) {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // Retrieve Orders
        final List<PlayerOrder> orders = getOrdersManager().listByGameNation(thisGame, thisNation, thisGame.getTurn());

        if (orders == null) {
            return refData;
        }

        // Describe orders
        final Map<Integer, String> descOrders = new HashMap<Integer, String>();

        // Group orders by type
        final Map<Integer, List<PlayerOrder>> playerOrders = new TreeMap<Integer, List<PlayerOrder>>();
        List<PlayerOrder> subList;
        for (final PlayerOrder order : orders) {
            descOrders.put(order.getOrderId(), getOrderDescription(order));

            final int phaseId;
            if (!ORDER_PHASE.containsKey(order.getType())) {
                phaseId = 150;
            } else {
                phaseId = ORDER_PHASE.get(order.getType());
            }

            if (playerOrders.containsKey(phaseId)) {
                subList = playerOrders.get(phaseId);

            } else {
                subList = new ArrayList<PlayerOrder>();
                playerOrders.put(phaseId, subList);
            }

            subList.add(order);
        }

        // access Calendar object
        final Calendar thisCal = calendar(thisGame);
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuf.append(" ");
        strBuf.append(thisCal.get(Calendar.YEAR));

        // Check for any news entries
        final List<News> lstNews = getNewsManager().listGameNationAnnouncements(thisGame, thisGame.getTurn(), thisNation);

        // Prepare data to pass to jsp
        refData.put("game", thisGame);
        refData.put("nation", thisNation);
        refData.put("gameId", thisGame.getGameId());
        refData.put("gameDate", strBuf.toString());
        refData.put("nationId", thisNation.getId());
        refData.put("turn", thisGame.getTurn());
        refData.put("orders", playerOrders);
        refData.put("orderDescr", descOrders);
        refData.put("orderNames", ORDER_NAMES);
        refData.put("phaseNames", PHASE_NAMES);
        refData.put("vp", 0);
        refData.put("months", constructMonths(thisGame));
        refData.put("news", lstNews);

        return refData;
    }

    /**
     * Prepare report.
     *
     * @param thisGame   the game to examine.
     * @param thisNation the nation to examine.
     * @param turnInt    the turn to examine.
     * @return the map with the data that will be passed to the jsp.
     */
    @Cachable(cacheName = "longGameCache")
    public Map<String, Object> prepareReport(final Game thisGame,
                                             final Nation thisNation,
                                             final int turnInt) {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // Retrieve Orders
        final List<PlayerOrder> orders = getOrdersManager().listByGameNation(thisGame, thisNation, turnInt);

        if (orders == null) {
            return refData;
        }

        // Describe orders
        final Map<Integer, String> descOrders = new HashMap<Integer, String>();

        // Group orders by type
        final Map<Integer, List<PlayerOrder>> playerOrders = new TreeMap<Integer, List<PlayerOrder>>();
        List<PlayerOrder> subList;
        for (final PlayerOrder order : orders) {
            descOrders.put(order.getOrderId(), getOrderDescription(order));

            final int phaseId;
            if (!ORDER_PHASE.containsKey(order.getType())) {
                phaseId = 150;
            } else {
                phaseId = ORDER_PHASE.get(order.getType());
            }

            if (playerOrders.containsKey(phaseId)) {
                subList = playerOrders.get(phaseId);

            } else {
                subList = new ArrayList<PlayerOrder>();
                playerOrders.put(phaseId, subList);
            }

            subList.add(order);
        }

        // access Calendar object
        final Calendar thisCal = calendar(thisGame, turnInt);
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuf.append(" ");
        strBuf.append(thisCal.get(Calendar.YEAR));

        // Prepare data to pass to jsp
        refData.put("game", thisGame);
        refData.put("nation", thisNation);
        refData.put("gameId", thisGame.getGameId());
        refData.put("gameDate", strBuf.toString());
        refData.put("nationId", thisNation.getId());
        refData.put("turn", turnInt);
        refData.put("orders", playerOrders);
        refData.put("orderDescr", descOrders);
        refData.put("orderNames", ORDER_NAMES);
        refData.put("phaseNames", PHASE_NAMES);
        refData.put("vp", 0);
        refData.put("months", constructMonths(thisGame));
        refData.put("news", new ArrayList<News>());

        return refData;
    }

}
