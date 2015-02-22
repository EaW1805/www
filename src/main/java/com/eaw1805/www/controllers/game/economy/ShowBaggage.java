package com.eaw1805.www.controllers.game.economy;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.managers.beans.BaggageTrainManagerBean;
import com.eaw1805.data.managers.beans.BrigadeManagerBean;
import com.eaw1805.data.managers.beans.CommanderManagerBean;
import com.eaw1805.data.managers.beans.GoodManagerBean;
import com.eaw1805.data.managers.beans.SpyManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.PlayerOrder;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.army.Brigade;
import com.eaw1805.data.model.army.Commander;
import com.eaw1805.data.model.army.Spy;
import com.eaw1805.data.model.economy.BaggageTrain;
import com.eaw1805.data.model.economy.Good;
import com.eaw1805.data.model.map.Carrier;
import com.eaw1805.www.controllers.GameReportsController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Display the Baggage Trains for a given nation of a particular game.
 */
@Controller
public class ShowBaggage
        extends GameReportsController
        implements GoodConstants, ReportConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowBaggage.class);


    @RequestMapping(method = RequestMethod.GET, value = "/report/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/baggagetrains")
    protected ModelAndView handle(@PathVariable("scenarioId") String scenarioId,
                                  @PathVariable("gameId") String gameId,
                                  @PathVariable("nationId") String nationId)
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

        final Game thisGame;
        if ((gameId == null) || (gameId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisGame = getGameManager().getByID(Integer.parseInt(gameId));
            if (thisGame == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Retrieve Nation entity

        final Nation thisNation;
        if ((nationId == null) || (nationId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisNation = getNationManager().getByID(Integer.parseInt(nationId));
            if (thisNation == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Check that user is allowed to view nation
        final User thisUser = getUser();
        if (thisUser.getUserType() != 3 && getUserGameManager().listActive(thisUser, thisGame, thisNation).isEmpty()) {
            throw new InvalidPageException("Page not found");
        }

        // access Calendar object
        final Calendar thisCal = calendar(thisGame);
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuf.append(" ");
        strBuf.append(thisCal.get(Calendar.YEAR));

        // Retrieve support tables
        final List<Good> goodList = goodManager.list();
        goodList.remove(GoodConstants.GOOD_CP - 1); // Remove Command Points -- this is a virtual good
        goodList.remove(GoodConstants.GOOD_AP - 1); // Remove Administrative Points -- this is a virtual good

        final Map<Integer, Map<Integer, Integer>> bTrainsToGoods = new HashMap<Integer, Map<Integer, Integer>>();
        final Map<Integer, Map<Integer, Integer>> bTrainsLandUnits = new HashMap<Integer, Map<Integer, Integer>>();

        // Retrieve spies
        final List<BaggageTrain> btrainList = baggageTrainManager.listGameNation(thisGame, thisNation);


        //sort trains by region and position
        final Map<Integer, Map<String, List<BaggageTrain>>> sortedTrains = new TreeMap<Integer, Map<String, List<BaggageTrain>>>();
        final Map<Integer, Map<String, Object>> statisticsData = new HashMap<Integer, Map<String, Object>>();
        final Map<Integer, List<Brigade>> trainToBrigades = new HashMap<Integer, List<Brigade>>();
        final Map<Integer, List<Commander>> trainToCommanders = new HashMap<Integer, List<Commander>>();
        final Map<Integer, List<Spy>> trainToSpies = new HashMap<Integer, List<Spy>>();
        for (BaggageTrain train : btrainList) {

            trainToBrigades.put(train.getBaggageTrainId(), new ArrayList<Brigade>());
            trainToCommanders.put(train.getBaggageTrainId(), new ArrayList<Commander>());
            trainToSpies.put(train.getBaggageTrainId(), new ArrayList<Spy>());

            statisticsData.put(train.getBaggageTrainId(), new HashMap<String, Object>());
            //generate report for this baggage train
            prepareReport(train, BAGGAGETRAIN, train.getBaggageTrainId(), thisGame, thisNation, statisticsData.get(train.getBaggageTrainId()));

            if (!sortedTrains.containsKey(train.getPosition().getRegion().getId())) {
                sortedTrains.put(train.getPosition().getRegion().getId(), new HashMap<String, List<BaggageTrain>>());
            }
            if (!sortedTrains.get(train.getPosition().getRegion().getId()).containsKey(train.getPosition().getRegion().toString())) {
                sortedTrains.get(train.getPosition().getRegion().getId()).put(train.getPosition().getRegion().toString(), new ArrayList<BaggageTrain>());
            }
            sortedTrains.get(train.getPosition().getRegion().getId()).get(train.getPosition().getRegion().toString()).add(train);

            bTrainsToGoods.put(train.getBaggageTrainId(), new TreeMap<Integer, Integer>());
            bTrainsLandUnits.put(train.getBaggageTrainId(), new TreeMap<Integer, Integer>());
            for (Map.Entry<Integer, Integer> entry : train.getStoredGoods().entrySet()) {
                if (entry.getKey() <= GOOD_LAST) {
                    if (entry.getValue() != 0) {
                        bTrainsToGoods.get(train.getBaggageTrainId()).put(entry.getKey(), entry.getValue());
                    }
                } else {

                    final int cargoType;
                    if (entry.getKey() >= ArmyConstants.SPY * 1000) {
                        cargoType = ArmyConstants.SPY;
                        trainToSpies.get(train.getBaggageTrainId()).add(spyManager.getByID(entry.getValue()));
                    } else if (entry.getKey() >= ArmyConstants.COMMANDER * 1000) {
                        cargoType = ArmyConstants.COMMANDER;
                        trainToCommanders.get(train.getBaggageTrainId()).add(commanderManager.getByID(entry.getValue()));
                    } else if (entry.getKey() >= ArmyConstants.BRIGADE * 1000) {
                        cargoType = ArmyConstants.BRIGADE;
                        trainToBrigades.get(train.getBaggageTrainId()).add(brigadeManager.getByID(entry.getValue()));
                    } else {
                        cargoType = -1;
                    }
                    if (!bTrainsLandUnits.get(train.getBaggageTrainId()).containsKey(cargoType)) {
                        bTrainsLandUnits.get(train.getBaggageTrainId()).put(cargoType, 1);
                    } else {
                        bTrainsLandUnits.get(train.getBaggageTrainId()).put(cargoType, bTrainsLandUnits.get(train.getBaggageTrainId()).get(cargoType) + 1);
                    }
                }
            }
        }


        final List<PlayerOrder> tradeOrders = getOrdersManager().listByGameNationType(thisGame, thisNation, thisGame.getTurn() - 1, new Object[]{ORDER_EXCHF, ORDER_EXCHS});
        final Set<Integer> bTrainsWithOrders = new HashSet<Integer>();
        for (PlayerOrder order : tradeOrders) {
            if (Integer.parseInt(order.getParameter1()) == BAGGAGETRAIN) {
                bTrainsWithOrders.add(Integer.parseInt(order.getParameter2()));
            }
            if (Integer.parseInt(order.getParameter3()) == BAGGAGETRAIN) {
                bTrainsWithOrders.add(Integer.parseInt(order.getParameter4()));
            }
        }

        final List<Nation> allNations = getNationManager().list();
        final Map<String, Nation> nameToNation = new HashMap<String, Nation>();
        for (Nation nation : allNations) {
            nameToNation.put(nation.getName(), nation);
        }

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("user", thisUser);
        refData.put("game", thisGame);
        refData.put("turn", thisGame.getTurn());
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        refData.put("nation", thisNation);
        refData.put("gameId", thisGame.getGameId());
        refData.put("gameDate", strBuf.toString());
        refData.put("nationId", thisNation.getId());
        refData.put("goodList", goodList);
        refData.put("btrainList", btrainList);
        refData.put("sortedTrains", sortedTrains);
        refData.put("bTrainsWithOrders", bTrainsWithOrders);
        refData.put("moneyGood", new Integer(1));
        refData.put("vp", 0);
        refData.put("months", constructMonths(thisGame));
        refData.put("bTrainsLandUnits", bTrainsLandUnits);
        refData.put("bTrainsToGoods", bTrainsToGoods);
        refData.put("statisticsData", statisticsData);
        refData.put("trainToBrigades", trainToBrigades);
        refData.put("trainToCommanders", trainToCommanders);
        refData.put("trainToSpies", trainToSpies);
        refData.put("nameToNation", nameToNation);

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show baggage trains for game=" + gameId + "/nation=" + thisNation.getName());
        return new ModelAndView("game/baggage", refData);
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
                if (order.getResult() < 0) {
                    continue;
                }
                boolean isSeller = (Integer.parseInt(order.getParameter1()) == carrierType && Integer.parseInt(order.getParameter2()) == carrierId);
                boolean isCustomer = (Integer.parseInt(order.getParameter3()) == carrierType && Integer.parseInt(order.getParameter4()) == carrierId);
                if (!isSeller && !isCustomer) {
                    continue;
                }
                sortedOrdersList.add(order);
                orderToShipWarehouse.put(order.getOrderId(), new HashMap<Integer, Integer>());
                int goodId = Integer.parseInt(order.getParameter5());
                int quantity = Integer.parseInt(order.getParameter6());
                int moneyCost = 0;
                try {
                    Integer.parseInt(order.getTemp9());
                } catch (Exception e) {
                    moneyCost = 0;
                }
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
                    tempWarehouse.put(GOOD_MONEY, tempWarehouse.get(GOOD_MONEY) + moneyCost);
                    orderToShipWarehouse.get(order.getOrderId()).put(goodId, quantity);
                    if (moneyCost != 0) {
                        orderToShipWarehouse.get(order.getOrderId()).put(GOOD_MONEY, -moneyCost);
                    }
                }
            }
        }
        initialState.putAll(copyMap(tempWarehouse));
        Collections.reverse(sortedOrdersList);


        refData.put("initialState", initialState);
        refData.put("finalState", finalState);

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

    /**
     * Instance GoodManager class to perform queries
     * about good objects.
     */
    private transient GoodManagerBean goodManager;

    /**
     * Setter method used by spring to inject a goodManager bean.
     *
     * @param injGoodManager a goodManager bean.
     */
    public void setGoodManager(final GoodManagerBean injGoodManager) {
        goodManager = injGoodManager;
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

    private transient BrigadeManagerBean brigadeManager;

    public void setBrigadeManager(BrigadeManagerBean brigadeManager) {
        this.brigadeManager = brigadeManager;
    }

    private transient SpyManagerBean spyManager;

    public void setSpyManager(SpyManagerBean spyManager) {
        this.spyManager = spyManager;
    }

    private transient CommanderManagerBean commanderManager;

    public void setCommanderManager(CommanderManagerBean commanderManager) {
        this.commanderManager = commanderManager;
    }

}
