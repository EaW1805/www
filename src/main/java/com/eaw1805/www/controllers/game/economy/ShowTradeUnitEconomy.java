package com.eaw1805.www.controllers.game.economy;


import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.managers.beans.BaggageTrainManagerBean;
import com.eaw1805.data.managers.beans.ShipManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.PlayerOrder;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.economy.BaggageTrain;
import com.eaw1805.data.model.economy.Good;
import com.eaw1805.data.model.fleet.Ship;
import com.eaw1805.data.model.map.Carrier;
import com.eaw1805.www.controllers.GameReportsController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Used to display the economy of a particular nation.
 */
@Controller
public class ShowTradeUnitEconomy extends GameReportsController
        implements GoodConstants, ReportConstants {

    @RequestMapping(method = RequestMethod.GET, value = "/report/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/ship/{shipId}")
    protected ModelAndView handleShipEconomy(@PathVariable("scenarioId") String scenarioId,
                                             @PathVariable("gameId") String gameId,
                                             @PathVariable("nationId") String nationId,
                                             @PathVariable("shipId") String shipId)
            throws Exception {
        //validate inputs
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

        final Game game = getGameManager().getByID(Integer.parseInt(gameId));
        if (game == null) {
            throw new InvalidPageException("Page not found");
        }

        final Nation nation = getNationManager().getByID(Integer.parseInt(nationId));
        if (nation == null) {
            throw new InvalidPageException("Page not found");
        }

        final Ship ship = shipManager.getByID(Integer.parseInt(shipId));
        if (ship == null) {
            throw new InvalidPageException("Page not found");
        }
        final Map<String, Object> refData = new HashMap<String, Object>();
        prepareReport(ship, SHIP, ship.getShipId(), game, nation, refData);
        // Check that user is allowed to view nation
        final User thisUser = getUser();


        // Prepare data to pass to jsp
        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        refData.put("ship", ship);

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show ship economy for game=" + gameId + "/nation=" + nation.getName() + "/ship=" + ship.getShipId());
        return new ModelAndView("game/ship", refData);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/report/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/baggagetrain/{bTrainId}")
    protected ModelAndView handleBaggageTrainEconomy(@PathVariable("scenarioId") String scenarioId,
                                                     @PathVariable("gameId") String gameId,
                                                     @PathVariable("nationId") String nationId,
                                                     @PathVariable("bTrainId") String bTrainId)
            throws Exception {

        //validate inputs
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

        final Game game = getGameManager().getByID(Integer.parseInt(gameId));
        if (game == null) {
            throw new InvalidPageException("Page not found");
        }

        final Nation nation = getNationManager().getByID(Integer.parseInt(nationId));
        if (nation == null) {
            throw new InvalidPageException("Page not found");
        }

        final BaggageTrain train = bTrainManager.getByID(Integer.parseInt(bTrainId));
        if (train == null) {
            throw new InvalidPageException("Page not found");
        }

        final Map<String, Object> refData = new HashMap<String, Object>();
        prepareReport(train, BAGGAGETRAIN, train.getBaggageTrainId(), game, nation, refData);
        // Check that user is allowed to view nation
        final User thisUser = getUser();


        // Prepare data to pass to jsp
        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        refData.put("baggagetrain", train);

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show ship economy for game=" + gameId + "/nation=" + nation.getName() + "/baggagetrain=" + train.getBaggageTrainId());
        return new ModelAndView("game/baggagetrain", refData);


    }


    protected void prepareReport(final Carrier carrier, final int carrierType, final int carrierId, final Game game, final Nation nation, Map<String, Object> refData) {

        //retrieve previous turns orders
        final List<PlayerOrder> orders = getOrdersManager().listByGameNation(game, nation, game.getTurn() - 1);
        //sort orders
        final Map<Integer, Map<Integer, PlayerOrder>> sortedOrders = new TreeMap<Integer, Map<Integer, PlayerOrder>>();
        for (PlayerOrder order : orders) {
            if (!sortedOrders.containsKey(order.getType())) {
                sortedOrders.put(order.getType(), new TreeMap<Integer, PlayerOrder>());
            }
            sortedOrders.get(order.getType()).put(order.getPosition(), order);
        }

        //start from now and go back in reverse direction.
        //first get second trading phase.
        final Map<Integer, Map<Integer, Integer>> orderToShipWarehouse = new HashMap<Integer, Map<Integer, Integer>>();
        //copy current ship warehouse
        final Map<Integer, Integer> tempWarehouse = new HashMap<Integer, Integer>();
        for (int index = GOOD_FIRST; index <= GOOD_LAST - 2; index++) {
            tempWarehouse.put(index, carrier.getStoredGoods().get(index));
        }
        final Map<Integer, Integer> initialState = new HashMap<Integer, Integer>();
        Map<Integer, Integer> finalState = new HashMap<Integer, Integer>();
        finalState.putAll(copyMap(tempWarehouse));

        final List<PlayerOrder> sortedOrdersList = new ArrayList<PlayerOrder>();

        for (int i = 0; i < 2; i++) {
            final List<PlayerOrder> reversedOrders;
            if (i == 0) {
                if (sortedOrders.containsKey(ORDER_EXCHS)) {
                    reversedOrders = new ArrayList<PlayerOrder>(sortedOrders.get(ORDER_EXCHS).values());
                } else {
                    reversedOrders = new ArrayList<PlayerOrder>();
                }
            } else {
                if (sortedOrders.containsKey(ORDER_EXCHF)) {
                    reversedOrders = new ArrayList<PlayerOrder>(sortedOrders.get(ORDER_EXCHF).values());
                } else {
                    reversedOrders = new ArrayList<PlayerOrder>();
                }
            }

            Collections.reverse(reversedOrders);
            for (PlayerOrder order : reversedOrders) {
                boolean isSeller = (Integer.parseInt(order.getParameter1()) == carrierType && Integer.parseInt(order.getParameter2()) == carrierId);
                boolean isCustomer = (Integer.parseInt(order.getParameter3()) == carrierType && Integer.parseInt(order.getParameter4()) == carrierId);
                if (!isSeller && !isCustomer) {
                    continue;
                }
                sortedOrdersList.add(order);
                orderToShipWarehouse.put(order.getOrderId(), new HashMap<Integer, Integer>());
                int goodId = Integer.parseInt(order.getParameter5());
                int quantity = Integer.parseInt(order.getParameter6());
                int moneyCost = Integer.parseInt(order.getTemp1());
                if (goodId == GOOD_MONEY) {
                    moneyCost = 0;
                }
                if (isSeller) {
                    tempWarehouse.put(goodId, tempWarehouse.get(goodId) + quantity);
                    tempWarehouse.put(GOOD_MONEY, tempWarehouse.get(GOOD_MONEY) - moneyCost);
                    orderToShipWarehouse.get(order.getOrderId()).put(goodId, -quantity);
                    if (moneyCost != 0) {
                        orderToShipWarehouse.get(order.getOrderId()).put(GOOD_MONEY, moneyCost);
                    }
                } else {
                    tempWarehouse.put(goodId, tempWarehouse.get(goodId) - quantity);
                    tempWarehouse.put(GOOD_MONEY, tempWarehouse.get(GOOD_MONEY) + quantity);
                    orderToShipWarehouse.get(order.getOrderId()).put(goodId, quantity);
                    if (moneyCost != 0) {
                        orderToShipWarehouse.get(order.getOrderId()).put(GOOD_MONEY, -moneyCost);
                    }
                }
            }
        }
        initialState.putAll(copyMap(tempWarehouse));
        Collections.reverse(sortedOrdersList);

        final List<Good> goodList = getGoodManager().list();
        goodList.remove(GoodConstants.GOOD_CP - 1); // Remove Command Points -- this is a virtual good
        goodList.remove(GoodConstants.GOOD_AP - 1); // Remove Administrative Points -- this is a virtual good

        refData.put("initialState", initialState);
        refData.put("finalState", finalState);
        refData.put("goodList", goodList);
        refData.put("sortedOrdersList", sortedOrdersList);

        refData.put("orderToShipWarehouse", orderToShipWarehouse);

    }

    /**
     * Copy a Integer: Integer map.
     *
     * @param temp The map to copy.
     * @return The new map identical to the input map.
     */
    private Map<Integer, Integer> copyMap(final Map<Integer, Integer> temp) {
        final Map<Integer, Integer> out = new HashMap<Integer, Integer>();
        for (Integer key : temp.keySet()) {
            out.put(key, temp.get(key));
        }
        return out;
    }


    private ShipManagerBean shipManager;

    public void setShipManager(ShipManagerBean shipManager) {
        this.shipManager = shipManager;
    }

    private BaggageTrainManagerBean bTrainManager;

    public void setbTrainManager(BaggageTrainManagerBean bTrainManager) {
        this.bTrainManager = bTrainManager;
    }
}
