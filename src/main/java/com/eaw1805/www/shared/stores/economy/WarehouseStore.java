package com.eaw1805.www.shared.stores.economy;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.NaturalResourcesConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.TradeUnitAbstractDTO;
import com.eaw1805.data.dto.web.economy.WarehouseDTO;
import com.eaw1805.www.client.views.economy.orders.OrderValidateView;
import com.eaw1805.www.client.views.layout.EconomyView;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class WarehouseStore
        implements GoodConstants, ProductionSiteConstants, NaturalResourcesConstants, RegionConstants
        , ArmyConstants {

    /**
     * The names of the goods.
     */
    private static final String[] NAMES =
            {"Money", "Citizens",
                    "Industrial Points", "Food", "Stone", "Wood", "Ore", "Gems",
                    "Horses", "Fabric", "Wool", "Precious Metals", "Wine", "Colonial Goods"};

    /**
     * The weights of the goods.
     */
    private static final Integer[] WEIGHTS =
            {0, 10,
                    25, 1, 2, 2, 1, 10,
                    5, 1, 1, 1, 1, 1};

    /**
     * The good factor for each good.
     */
    private static final int[] FACTORS =
            {0, 0,
                    17, 11, 2, 6, 250, 350,
                    5, 26, 3, 380, 70, 140, 0, 0};

    private final transient Map<Integer, WarehouseDTO> wareHouseMap = new HashMap<Integer, WarehouseDTO>();

    /**
     * A map containing the max values you can gift and initialised
     * on client startup <goodId,qte>
     */

    private final transient Map<Integer, Double> maxGiftQtes = new HashMap<Integer, Double>();

    /**
     * Our instance of the Manager.
     */
    private static WarehouseStore ourInstance = null;

    /**
     * Variable telling us if our data are initialized.
     */
    private boolean isInitialized = false, isClient = false;

    /**
     * The economy panel used by the client.
     */
    private EconomyView economyWidget;

    // Method Used By the service to initialize Warehouse the Map
    public void initDbWareHouses(final List<WarehouseDTO> warehousesList) {
        try {
            // clear any previous additions to collections
            final List<WarehouseDTO> wareHouseList = warehousesList;
            for (WarehouseDTO warehouse : wareHouseList) {
                wareHouseMap.put(warehouse.getRegionId(), warehouse);
            }

            for (int goodId = GOOD_FIRST; goodId < GOOD_AP; goodId++) {
                int all = 0;
                for (int regionId = EUROPE; regionId <= AFRICA; regionId++) {
                    if (wareHouseMap.containsKey(regionId)) {
                        all += wareHouseMap.get(regionId).getGoodsDTO().get(goodId).getQte();
                    }
                }

                // 33% of the aggregated warehouse total for all regions
                maxGiftQtes.put(goodId, all / 3d);
            }

            setInitialized(true);

            if (OrderStore.getInstance().isInitialized() && isClient()) {
                OrderStore.getInstance().setOrderExpenses();
            }

        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to initialize warehouses due to unexpected reason", false);
        }
    }


    /**
     * Check if canceling the given order will create a conflict or not.
     * This function is being used from canceling trade orders and decrease population only.
     *
     * @param order The order you want to check if it can be canceled.
     * @return True if the order can be canceled.
     */
    public int canRefundOrder(final ClientOrderDTO order) {
        //first case is for trade orders
        if (order.getOrderTypeId() == OrderConstants.ORDER_EXCHF ||
                order.getOrderTypeId() == OrderConstants.ORDER_EXCHS) {
            final int sourceType = order.getIdentifier(0);
            final int targetType = order.getIdentifier(2);
            final int sourceId = order.getIdentifier(1);
            final int targetId = order.getIdentifier(3);
            //find 1st unit
            TradeUnitAbstractDTO unit1 = null;
            switch (sourceType) {
                case BAGGAGETRAIN:
                    unit1 = BaggageTrainStore.getInstance().getBaggageTrainById(sourceId);
                    if (unit1 == null) {
                        unit1 = AlliedUnitsStore.getInstance().getBaggageTrainById(sourceId);
                    }
                    break;
                case SHIP:
                    unit1 = NavyStore.getInstance().getShipById(sourceId);
                    if (unit1 == null) {
                        unit1 = AlliedUnitsStore.getInstance().getShipById(sourceId);
                    }
                    break;
                case WAREHOUSE:
                    unit1 = WarehouseStore.getInstance().getWareHouseByRegion(order.getRegionId());
                    break;
                case TRADECITY://if it is trade city then don't check
                    unit1 = null;
                    break;
            }
            //check if canceling the order creates conflict for first trade unit.
            if (unit1 != null) {
                int check = canRefundOrder(unit1, order);
                if (check != 1) {
                    return check;
                }
            }
            //find 2nd unit
            TradeUnitAbstractDTO unit2 = null;
            switch (targetType) {
                case BAGGAGETRAIN:
                    unit2 = BaggageTrainStore.getInstance().getBaggageTrainById(targetId);
                    if (unit2 == null) {
                        unit2 = AlliedUnitsStore.getInstance().getBaggageTrainById(targetId);
                    }
                    break;
                case SHIP:
                    unit2 = NavyStore.getInstance().getShipById(targetId);
                    if (unit2 == null) {
                        unit2 = AlliedUnitsStore.getInstance().getShipById(targetId);
                    }
                    break;
                case WAREHOUSE:
                    unit2 = WarehouseStore.getInstance().getWareHouseByRegion(order.getRegionId());
                    break;
                case TRADECITY:
                    unit2 = null;//if it is trade city then don't check
                    break;
            }
            //check if canceling the order creates conflict for the second trade unit.
            if (unit2 != null) {
                int check = canRefundOrder(unit2, order);
                if (check != 1) {
                    return check;
                }
            }

        } else {
            //for decrease population order (same case can be applied for any other order) just check for warehouse.
            final TradeUnitAbstractDTO unit1 = WarehouseStore.getInstance().getWareHouseByRegion(order.getRegionId());
            int check = canRefundOrder(unit1, order);
            if (check != 1) {
                return check;
            }


        }
        return 1;
    }

    /**
     * Check if canceling the given order will create a conflict or not.
     * This function is being used from canceling trade orders and decrease population only.
     *
     * @param unit         The trade unit, we need its warehouse and type.
     * @param orderToCheck The order you want to check if it can be canceled.
     * @return True if the order can be canceled.
     */
    public int canRefundOrder(final TradeUnitAbstractDTO unit, final ClientOrderDTO orderToCheck) {
        //initialize temporal warehouse with initial warehouse data
        final Map<Integer, Integer> tempGoods = new HashMap<Integer, Integer>();
        if (unit.getUnitType() == WAREHOUSE) {
            for (int good = GOOD_FIRST; good <= GOOD_LAST - 2; good++) {
                if (good == GOOD_MONEY) {
                    tempGoods.put(good, getWareHouseByRegion(EUROPE).getOriginalGoodsDTO().get(good).getQte());
                } else {
                    tempGoods.put(good, unit.getOriginalGoodsDTO().get(good).getQte());
                }
            }
        } else {
            for (int good = GOOD_FIRST; good <= GOOD_LAST - 2; good++) {
                tempGoods.put(good, unit.getOriginalGoodsDTO().get(good).getQte());
            }
        }

        int canRefund = 1;
        int totalWeight = TradeStore.getInstance().getTradeUnitWeight(unit);
        if (totalWeight == -1) {
            totalWeight = Integer.MAX_VALUE;
        }
        switch (unit.getUnitType()) {
            case SHIP:
            case BAGGAGETRAIN:
                //for ship and baggage trains there are only trade orders that can create conflict.
                for (final List<ClientOrderDTO> orders : OrderStore.getInstance().getClientOrders().values()) {
                    for (final ClientOrderDTO order : orders) {
                        if (orderToCheck.getOrderTypeId() == order.getOrderTypeId() && orderToCheck.getPriority() == order.getPriority()) {
                            continue;
                        }
                        if (order.getOrderTypeId() == OrderConstants.ORDER_EXCHF ||
                                order.getOrderTypeId() == OrderConstants.ORDER_EXCHS) {
                            int tradeChange = calculateTradeChange(tempGoods, order, unit, totalWeight);
                            if (tradeChange != 1) {
                                canRefund = tradeChange;
                            }
                        }
                    }
                }
                break;
            case WAREHOUSE:
                for (final List<ClientOrderDTO> orders : OrderStore.getInstance().getClientOrders().values()) {
                    for (final ClientOrderDTO order : orders) {
                        if (orderToCheck.getOrderTypeId() == order.getOrderTypeId() && orderToCheck.getPriority() == order.getPriority()) {
                            continue;
                        }
                        //for region warehouse check if order is trade and calculate its affect.
                        if (order.getOrderTypeId() == OrderConstants.ORDER_EXCHF ||
                                order.getOrderTypeId() == OrderConstants.ORDER_EXCHS) {
                            int tradeChange = calculateTradeChange(tempGoods, order, unit, totalWeight);
                            if (tradeChange != 1) {
                                canRefund = tradeChange;
                            }
                        } else {
                            //for any other order just substruct the values from the warehouse
                            for (int i = (GOOD_LAST - 2); i >= GOOD_FIRST; i--) {
                                if (i == GOOD_MONEY || order.getRegionId() == unit.getRegionId()) {
                                    tempGoods.put(i, tempGoods.get(i) - order.getCosts().getNumericCost(i));
                                    if (tempGoods.get(i) < 0) {
                                        canRefund = 0;
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default:
                //do nothing at this point
        }
        return canRefund;
    }

    // Method returning the economy manager if already initialized
    // or the a new instance
    public static WarehouseStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new WarehouseStore();
        }
        return ourInstance;
    }

    /**
     * @param regionId the region.
     * @return the corresponding region warehouse
     */
    public WarehouseDTO getWareHouseByRegion(final int regionId) {
        return getWareHouseMap().get(regionId);
    }

    /**
     * Method that returns warehouse by it's internal id
     *
     * @param id the internal id
     * @return the warehouse dto
     */
    public TradeUnitAbstractDTO getWareHouseById(final int id) {
        for (WarehouseDTO whouse : wareHouseMap.values()) {
            if (whouse.getId() == id) {
                return whouse;
            }
        }
        return null;
    }

    /**
     * Method that spends the target recourse
     *
     * @param goodId   The id of the good we want to spend
     * @param quantity The value we want to spend of the given id
     * @param regionId The region of the warehouse we will use
     */
    public void spendResource(final int goodId, final int quantity, final int regionId) {
        WarehouseDTO warehouse;
        if (goodId == GOOD_MONEY || goodId == GOOD_AP || goodId == GOOD_CP) {
            warehouse = getWareHouseMap().get(1);
        } else {
            warehouse = getWareHouseMap().get(regionId);
        }
        warehouse.getGoodsDTO().get(goodId).setQte(
                warehouse.getGoodsDTO().get(goodId).getQte() - quantity
        );
        if (quantity != 0) {
            GameStore.getInstance().getLayoutView().getEconomyView().getAnimatedPopups()[EconomyView.POSITION[goodId - 1]].setValueAndShow(-quantity);
        }
    }

    /**
     * Method that refunds the target recourse
     *
     * @param goodId   The id of the good we want to spend
     * @param quantity The value we want to spend of the given id
     * @param regionId The region of the warehouse we will use
     */
    public void getBackResource(final int goodId, final int quantity, final int regionId) {
        WarehouseDTO warehouse;
        if (goodId == GOOD_MONEY || goodId == GOOD_AP || goodId == GOOD_CP) {
            warehouse = getWareHouseMap().get(1);
        } else {
            warehouse = getWareHouseMap().get(regionId);
        }
        warehouse.getGoodsDTO().get(goodId).setQte(
                warehouse.getGoodsDTO().get(goodId).getQte() + quantity
        );
        if (quantity != 0) {
            GameStore.getInstance().getLayoutView().getEconomyView().getAnimatedPopups()[EconomyView.POSITION[goodId - 1]].setValueAndShow(quantity);
        }
    }

    /**
     * This function checks if a spesific order can be added without creating conflict.
     * This function is been used for adding trade orders but it can be used for any order.
     *
     * @param order The order to check if it can be added.
     * @return True if it can be added without conflicts.
     */
    public int canAddOrderSpecific(final ClientOrderDTO order) {
        //first case check if the order is trade order
        if (order.getOrderTypeId() == OrderConstants.ORDER_EXCHF ||
                order.getOrderTypeId() == OrderConstants.ORDER_EXCHS) {
            final int sourceType = order.getIdentifier(0);
            final int targetType = order.getIdentifier(2);
            final int sourceId = order.getIdentifier(1);
            final int targetId = order.getIdentifier(3);
            //find 1st trade unit
            TradeUnitAbstractDTO unit1 = null;
            switch (sourceType) {
                case BAGGAGETRAIN:
                    unit1 = BaggageTrainStore.getInstance().getBaggageTrainById(sourceId);
                    if (unit1 == null) {
                        unit1 = AlliedUnitsStore.getInstance().getBaggageTrainById(sourceId);
                    }
                    break;
                case SHIP:
                    unit1 = NavyStore.getInstance().getShipById(sourceId);
                    if (unit1 == null) {
                        unit1 = AlliedUnitsStore.getInstance().getShipById(sourceId);
                    }
                    break;
                case WAREHOUSE:
                    unit1 = WarehouseStore.getInstance().getWareHouseByRegion(order.getRegionId());
                    break;
                case TRADECITY:
                    unit1 = null;//TradeCityStore.getInstance().getTradeCityById(sourceId);
                    break;
            }
            //check if there is any conflict for 1st trade unit.
            if (unit1 != null) {
                int check  = canAddOrderSpecific(unit1, order);

                if (check != 1) {
                    return check;
                }
            }

            //find 2nd trade unit.
            TradeUnitAbstractDTO unit2 = null;
            switch (targetType) {
                case BAGGAGETRAIN:
                    unit2 = BaggageTrainStore.getInstance().getBaggageTrainById(targetId);
                    if (unit2 == null) {
                        unit2 = AlliedUnitsStore.getInstance().getBaggageTrainById(targetId);
                    }
                    break;
                case SHIP:
                    unit2 = NavyStore.getInstance().getShipById(targetId);
                    if (unit2 == null) {
                        unit2 = AlliedUnitsStore.getInstance().getShipById(targetId);
                    }
                    break;
                case WAREHOUSE:
                    unit2 = WarehouseStore.getInstance().getWareHouseByRegion(order.getRegionId());
                    break;
                case TRADECITY:
                    unit2 = null;//TradeCityStore.getInstance().getTradeCityById(targetId);
                    break;
            }
            //check if there is conflict for 2nd trade unit.
            if (unit2 != null) {
                int check = canAddOrderSpecific(unit2, order);

                if (check != 1) {
                    return check;
                }
            }

        } else {
            //for any other order the unit is always the regional warehouse.
            final TradeUnitAbstractDTO unit1 = WarehouseStore.getInstance().getWareHouseByRegion(order.getRegionId());
            int check = canAddOrderSpecific(unit1, order);
            if (check != 1) {
                return check;
            }
        }
        return 1;
    }

    /**
     * This function checks if a spesific order can be added without creating conflict.
     * This function is been used for adding trade orders but it can be used for any order.
     *
     * @param unit         The trade unit that will use its warehouse and type.
     * @param orderToCheck The order to check if it can be added.
     * @return True if it can be added without conflicts.
     */
    public int canAddOrderSpecific(final TradeUnitAbstractDTO unit, final ClientOrderDTO orderToCheck) {
        //initialize temporal warehouse with units initial data
        final Map<Integer, Integer> tempGoods = new HashMap<Integer, Integer>();
        if (unit.getUnitType() == WAREHOUSE) {
            for (int good = GOOD_FIRST; good <= GOOD_LAST - 2; good++) {
                if (good == GOOD_MONEY) {
                    tempGoods.put(good, getWareHouseByRegion(EUROPE).getOriginalGoodsDTO().get(good).getQte());
                } else {
                    tempGoods.put(good, unit.getOriginalGoodsDTO().get(good).getQte());
                }
            }
        } else {
            for (int good = GOOD_FIRST; good <= GOOD_LAST - 2; good++) {
                tempGoods.put(good, unit.getOriginalGoodsDTO().get(good).getQte());
            }
        }

        int canRefund = 1;

        int totalWeight = TradeStore.getInstance().getTradeUnitWeight(unit);
        if (totalWeight == -1) {
            totalWeight = Integer.MAX_VALUE;
        }
        switch (unit.getUnitType()) {
            case SHIP:
            case BAGGAGETRAIN:
                //for ships and baggage trains there are only trade orders to check
                for (int beforeAfter = 0; beforeAfter < 2; beforeAfter++) {
                    if (beforeAfter == 1) {
                        //if the previous orders are done calculate the new order before calculating the next orders
                        int tradeChange = calculateTradeChange(tempGoods, orderToCheck, unit, totalWeight);
                        if (tradeChange != 1) {
                            canRefund = tradeChange;
                        }
                    }
                    //for every order before the new order and then after the new order
                    for (final List<ClientOrderDTO> orders : OrderStore.getInstance().getClientOrders().values()) {
                        for (final ClientOrderDTO order : orders) {
                            if ((beforeAfter == 0 && order.getOrderTypeId() <= orderToCheck.getOrderTypeId())
                                    || (beforeAfter == 1 && order.getOrderTypeId() > orderToCheck.getOrderTypeId())) {
                                if (order.getOrderTypeId() == OrderConstants.ORDER_EXCHF ||
                                        order.getOrderTypeId() == OrderConstants.ORDER_EXCHS) {
                                    int tradeChange = calculateTradeChange(tempGoods, order, unit, totalWeight);
                                    if (tradeChange != 1) {
                                        canRefund = tradeChange;
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case WAREHOUSE:
                //for warehouses we need all orders
                for (int beforeAfter = 0; beforeAfter < 2; beforeAfter++) {
                    if (beforeAfter == 1) {
                        //if the previous from the new order are done calculate the new order
                        if (orderToCheck.getOrderTypeId() == OrderConstants.ORDER_EXCHF ||
                                orderToCheck.getOrderTypeId() == OrderConstants.ORDER_EXCHS) {
                            //if it is trade order calculate the changes
                            int tradeChange = calculateTradeChange(tempGoods, orderToCheck, unit, totalWeight);
                            if (tradeChange != 1) {
                                canRefund = tradeChange;
                            }
                        } else {
                            //for every other order just substruct the costs
                            for (int i = (GOOD_LAST - 2); i >= GOOD_FIRST; i--) {
                                tempGoods.put(i, tempGoods.get(i) - orderToCheck.getCosts().getNumericCost(i));
                                if (tempGoods.get(i) < 0) {
                                    canRefund = 0;
                                }
                            }
                        }
                    }
                    //for every order before the new order and then after the new order
                    for (final List<ClientOrderDTO> orders : OrderStore.getInstance().getClientOrders().values()) {
                        for (final ClientOrderDTO order : orders) {
                            if ((beforeAfter == 0 &&
                                    order.getOrderTypeId() <= orderToCheck.getOrderTypeId())
                                    || (beforeAfter == 1 && order.getOrderTypeId() > orderToCheck.getOrderTypeId())) {
                                if (order.getOrderTypeId() == OrderConstants.ORDER_EXCHF ||
                                        order.getOrderTypeId() == OrderConstants.ORDER_EXCHS) {
                                    int tradeChange = calculateTradeChange(tempGoods, order, unit, totalWeight);
                                    if (tradeChange != 1) {
                                        canRefund = tradeChange;
                                    }
                                } else {
                                    for (int i = (GOOD_LAST - 2); i >= GOOD_FIRST; i--) {
                                        if (i == GOOD_MONEY || order.getRegionId() == unit.getRegionId()) {
                                            tempGoods.put(i, tempGoods.get(i) - order.getCosts().getNumericCost(i));
                                            if (tempGoods.get(i) < 0) {
                                                canRefund = 0;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default:
                //do nothing at this point
        }
        return canRefund;
    }

    /**
     * This functions given a temporal warehouse, a trade order and a participating trade unit
     * calculates the affect in warehouse quantities from the order to the warehouse for the given unit.
     *
     * @param tempGoods A map that represents <goodType, quantity>
     * @param order     A trade order
     * @param unit      One of the two units that participates in the transaction and for who we want to calculate the affect of the order.
     * @return True if the order doesn't create any conflict for the units warehouse.
     */
    public int calculateTradeChange(final Map<Integer, Integer> tempGoods, final ClientOrderDTO order, final TradeUnitAbstractDTO unit, final int weight) {
        int canRefund = 1;
        final int sourceType = order.getIdentifier(0);
        final int targetType = order.getIdentifier(2);
        final int sourceId = order.getIdentifier(1);
        final int targetId = order.getIdentifier(3);

        //if it is not my order... then just return all ok with this unit and this order...
        if ((sourceType != unit.getUnitType() || sourceId != unit.getId())
                && (targetType != unit.getUnitType() || targetId != unit.getId())) {
            return canRefund;
        }

        //find the good type and the quantity for the transaction.
        int goodType = 0, goodQte = 0;
        for (int i = (GOOD_LAST - 2); i >= GOOD_FIRST; i--) {
            if (order.getCosts().getNumericCost(i) > 0) {
                goodType = i;
                goodQte = order.getCosts().getNumericCost(i);
                break;
            }
        }
        if (sourceType == unit.getUnitType() && sourceId == unit.getId()) {
            if (goodType != GOOD_MONEY) {//its a sale
                tempGoods.put(goodType, tempGoods.get(goodType) - goodQte);
                tempGoods.put(GOOD_MONEY, tempGoods.get(GOOD_MONEY) + order.getCosts().getNumericCost(GOOD_MONEY));

            } else {//its just an exchange
                tempGoods.put(goodType, tempGoods.get(goodType) - goodQte);
            }
        } else if (targetType == unit.getUnitType() && targetId == unit.getId()) {
            if (goodType != GOOD_MONEY) {//its a sale
                tempGoods.put(goodType, tempGoods.get(goodType) + goodQte);
                tempGoods.put(GOOD_MONEY, tempGoods.get(GOOD_MONEY) - order.getCosts().getNumericCost(GOOD_MONEY));

            } else {//its just an exchange
                tempGoods.put(goodType, tempGoods.get(goodType) + goodQte);
            }
        }
        //if we have any negative value then there is conflict
        if (tempGoods.get(goodType) < 0) {
            canRefund = 0;
        }
        if (tempGoods.get(GOOD_MONEY) < 0) {
            canRefund = 0;
        }
        //check weight restrictions only if the unit is current nations.
        //otherwise we only sell to this unit so  no complex problems.
        if ((unit.getUnitType() == BAGGAGETRAIN || unit.getUnitType() == SHIP) && unit.getNationId() == GameStore.getInstance().getNationId()) {
            if (calculateWarehouseWeight(tempGoods) > weight) {
                canRefund = -1;
            }
        }

        return canRefund;
    }

    public int calculateWarehouseWeight(final Map<Integer, Integer> tempGoods) {
        int weight = 0;
        for (int good = GOOD_FIRST; good <= GOOD_LAST - 2; good++) {
            if (good == GOOD_MONEY) {
                continue;
            }
            int quantity = tempGoods.get(good);
            final double goodWeight = WarehouseStore.getGoodWeight(good);
            weight += quantity*goodWeight;
        }
        return weight;
    }


    public boolean canAddOrder(final ClientOrderDTO orderToCheck, final int regionId) {
        boolean fundsAvail = true;
        boolean openConflictPopup = false;
        Set<Integer> conflictGoods = new HashSet<Integer>();
        final Map<Integer, Map<Integer, Integer>> tempGoods = new HashMap<Integer, Map<Integer, Integer>>();
        tempGoods.put(EUROPE, new HashMap<Integer, Integer>());
        tempGoods.put(AFRICA, new HashMap<Integer, Integer>());
        tempGoods.put(CARIBBEAN, new HashMap<Integer, Integer>());
        tempGoods.put(INDIES, new HashMap<Integer, Integer>());

        //init first values
        for (int goodId = 1; goodId <= GOOD_LAST; goodId++) {
            tempGoods.get(EUROPE).put(goodId, getWareHouseMap().get(EUROPE).getOriginalGoodsDTO().get(goodId).getQte());
            tempGoods.get(AFRICA).put(goodId, getWareHouseMap().get(AFRICA).getOriginalGoodsDTO().get(goodId).getQte());
            tempGoods.get(CARIBBEAN).put(goodId, getWareHouseMap().get(CARIBBEAN).getOriginalGoodsDTO().get(goodId).getQte());
            tempGoods.get(INDIES).put(goodId, getWareHouseMap().get(INDIES).getOriginalGoodsDTO().get(goodId).getQte());
        }
        //check if low priority orders can be executed
        for (final List<ClientOrderDTO> orders : OrderStore.getInstance().getClientOrders().values()) {
            for (final ClientOrderDTO order : orders) {

                if (order.getOrderTypeId() <= orderToCheck.getOrderTypeId()) {
                    for (int goodId = 1; goodId <= GOOD_LAST; goodId++) {
                        final int sign = getSign(order, goodId);
                        if (sign == -2) {
                            continue;
                        }
                        int curRegionId = EUROPE;
                        if (goodId != 1 && goodId != GOOD_AP && goodId != GOOD_CP) {
                            curRegionId = order.getRegionId();
                        }
                        if (curRegionId == 0) {//if region doesn't matter.. user europe to avoid crashes
                            curRegionId = EUROPE;
                        }
                        tempGoods.get(curRegionId).put(goodId, tempGoods.get(curRegionId).get(goodId) + sign * order.getCosts().getNumericCost(goodId));
                        if (tempGoods.get(curRegionId).get(goodId) < 0) {
                            conflictGoods.add(goodId);

                            fundsAvail = false;
                        }
                    }
                }
            }
        }
        //check if current order can be executed
        for (int goodId = 1; goodId <= GOOD_LAST; goodId++) {
            final int sign = getSign(orderToCheck, goodId);
            if (sign == -2) {
                continue;
            }
            int curRegionId = EUROPE;
            if (goodId != 1 && goodId != GOOD_AP && goodId != GOOD_CP) {
                curRegionId = regionId;
            }
            if (curRegionId == 0) {//if region doesn't matter.. user europe to avoid crashes
                curRegionId = EUROPE;
            }
            tempGoods.get(curRegionId).put(goodId, tempGoods.get(curRegionId).get(goodId) + sign * orderToCheck.getCosts().getNumericCost(goodId));
            if (tempGoods.get(curRegionId).get(goodId) < 0) {
                conflictGoods.add(goodId);

                fundsAvail = false;
            }
        }


        //check if high priority orders can be executed
        for (final List<ClientOrderDTO> orders : OrderStore.getInstance().getClientOrders().values()) {
            for (final ClientOrderDTO order : orders) {
                if (order.getOrderTypeId() > orderToCheck.getOrderTypeId()) {
                    for (int goodId = 1; goodId <= GOOD_LAST; goodId++) {
                        final int sign = getSign(order, goodId);
                        if (sign == -2) {
                            continue;
                        }
                        int curRegionId = EUROPE;
                        if (goodId != 1 && goodId != GOOD_AP && goodId != GOOD_CP) {
                            curRegionId = order.getRegionId();
                        }
                        if (curRegionId == 0) {//if region doesn't matter.. user europe to avoid crashes
                            curRegionId = EUROPE;
                        }

                        tempGoods.get(curRegionId).put(goodId, tempGoods.get(curRegionId).get(goodId) + sign * order.getCosts().getNumericCost(goodId));
                        if (tempGoods.get(curRegionId).get(goodId) < 0) {
                            if (order.getCosts().getNumericCost(goodId) != 0) {
                                openConflictPopup = true;
                            }
                            conflictGoods.add(goodId);

                            fundsAvail = false;
                        }
                    }
                }
            }
        }

        if (!fundsAvail) {
            GameStore.getInstance().getLayoutView().getEconomyView().highLightGoods(conflictGoods);
            if (openConflictPopup) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Can not perform certain action, "
                        + "check orders effectiveness in " + RegionStore.getInstance().getRegionNameById(regionId)
                        + " warehouse to find the reason?", true) {
                    public void onAccept() {
                        final OrderValidateView view = new OrderValidateView(orderToCheck);
                        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(view);
                    }
                };
            }
        }
        return fundsAvail;
    }

    /**
     * Method that spends the funds needed for the execution of
     * an order
     *
     * @param orderToCheck The id of the good we want to spend
     * @param regionId     The region of the warehouse we will use
     * @param spendAnyway  Do not check for availability.
     * @return true if the funds were available and false otherwise
     */
    public boolean buyOrder(final ClientOrderDTO orderToCheck, final int regionId, final boolean spendAnyway) {
        boolean fundsAvail = true;

        if (!spendAnyway) {
            fundsAvail = canAddOrder(orderToCheck, regionId);
        }

        if (fundsAvail || spendAnyway) {
            for (int goodId = 1; goodId <= GOOD_LAST; goodId++) {
                spendResource(goodId, orderToCheck.getCosts().getNumericCost(goodId), regionId);
            }
        }

        if (isClient()) {
            economyWidget.populateGoodsLabels(getWareHouseMap().get(regionId), false);
        }
        return fundsAvail;

    }

    public int getSign(final ClientOrderDTO order, final int goodId) {
        int sign = -1;
        if ((order.getOrderTypeId() == OrderConstants.ORDER_EXCHF
                || order.getOrderTypeId() == OrderConstants.ORDER_EXCHS)
                && order.getIdentifier(2) != WAREHOUSE
                && order.getIdentifier(0) != WAREHOUSE) {
            if (goodId == GOOD_AP || goodId == GOOD_CP) {
                return -1;//this means remove it from warehouse
            }
            return -2;   //this means it doesn't count.
        } else if (order.getOrderTypeId() == OrderConstants.ORDER_EXCHF
                || order.getOrderTypeId() == OrderConstants.ORDER_EXCHS) {
            if (goodId == GOOD_AP || goodId == GOOD_CP) {
                return -1;//this means remove it from warehouse
            }
            if (order.getIdentifier(0) == WAREHOUSE) {//this means the warehouse gives.
                if (order.getIdentifier(2) == TRADECITY && goodId == GOOD_MONEY) {
                    sign = 1; //this means add it to the warehouse
                } else {
                    sign = -1; // this means remove it from warehouse
                }

            } else if (order.getIdentifier(2) == WAREHOUSE) {
                if (order.getIdentifier(0) == TRADECITY) {
                    if (goodId == GOOD_MONEY) {
                        sign = -1;//this means remove it from warehouse
                    } else {
                        sign = 1;//this means add it to warehouse.
                    }
                } else {
                    sign = 1;//this means add it to warehouse
                }

            }

        }
        return sign;
    }

    /**
     * Method that refunds the player if he cancels the command
     *
     * @param orderCost The id of the good we want to spend
     * @param regionId  The region of the warehouse we will use
     */
    public void refundOrder(final OrderCostDTO orderCost, final int regionId) {
        for (int goodId = 1; goodId <= GOOD_LAST; goodId++) {
            getBackResource(goodId, orderCost.getNumericCost(goodId), regionId);
        }
        if (isClient()) {
            economyWidget.populateGoodsLabels(getWareHouseMap().get(regionId), false);
        }
    }

    /**
     * Method that returns the weight of a requested good
     *
     * @param goodId the id of the good whose weight we require
     * @return an integer representing the items needed for a ton
     */
    public static double getGoodWeight(final int goodId) {
        if (goodId >= GOOD_FIRST && goodId <= GOOD_LAST) {
            return (1d / WEIGHTS[goodId - 1]);
        } else {
            return 0d;
        }
    }

    /**
     * Method that returns the name of a requested good
     *
     * @param goodId the id of the good whose name we require
     * @return an integer representing the name
     */
    public String getGoodName(final int goodId) {
        if (goodId >= GOOD_FIRST && goodId <= GOOD_LAST) {
            return NAMES[goodId - 1];
        } else {
            return "";
        }
    }

    /**
     * Method that returns the Factor of a requested good
     *
     * @param goodId the id of the good whose factor we require
     * @return an integer representing the good factor
     */
    public static int getGoodFactor(final int goodId) {
        if (goodId >= GOOD_FIRST && goodId <= GOOD_LAST) {
            return FACTORS[goodId - 1];

        } else {
            return 0;
        }
    }

    /**
     * Method that returns the max possible quantity
     * you can accumulatively give to everyone
     *
     * @param goodId the id of the good
     * @return the qte you can give
     */
    public double getMaxGiftQte(final int goodId) {
        if (maxGiftQtes.containsKey(goodId)) {
            return maxGiftQtes.get(goodId);

        } else {
            return 0d;
        }
    }

    /**
     * @return the wareHouseMap
     */
    public Map<Integer, WarehouseDTO> getWareHouseMap() {
        return wareHouseMap;
    }

    public void setClient(final boolean isClient) {
        this.isClient = isClient;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setEconomyWidget(final EconomyView economyWidget) {
        this.economyWidget = economyWidget;
    }

    public void setInitialized(final boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * @return the economyWidget
     */
    public EconomyView getEconomyWidget() {
        return economyWidget;
    }


}
