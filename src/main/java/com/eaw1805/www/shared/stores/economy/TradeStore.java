package empire.webapp.shared.stores.economy;

import com.google.gwt.user.client.Window;
import empire.data.constants.AdminCommandPoints;
import empire.data.constants.ArmyConstants;
import empire.data.constants.GoodConstants;
import empire.data.constants.OrderConstants;
import empire.data.constants.RegionConstants;
import empire.data.constants.RelationConstants;
import empire.data.constants.TradeCalculations;
import empire.data.dto.common.PositionDTO;
import empire.data.dto.web.ClientOrderDTO;
import empire.data.dto.web.OrderCostDTO;
import empire.data.dto.web.TradeUnitAbstractDTO;
import empire.data.dto.web.economy.StoredGoodDTO;
import empire.data.dto.web.economy.TradeCityDTO;
import empire.data.dto.web.fleet.FleetDTO;
import empire.data.dto.web.fleet.ShipDTO;
import empire.webapp.client.events.trade.TradeEventManager;
import empire.webapp.client.widgets.ErrorPopup;
import empire.webapp.shared.stores.DataStore;
import empire.webapp.shared.stores.GameStore;
import empire.webapp.shared.stores.MovementStore;
import empire.webapp.shared.stores.RegionStore;
import empire.webapp.shared.stores.RelationsStore;
import empire.webapp.shared.stores.units.AlliedUnitsStore;
import empire.webapp.shared.stores.units.BaggageTrainStore;
import empire.webapp.shared.stores.units.NavyStore;
import empire.webapp.shared.stores.units.TransportStore;
import empire.webapp.shared.stores.util.ClientUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TradeStore
        implements OrderConstants, ArmyConstants, GoodConstants, RelationConstants, RegionConstants {

    /**
     * Our instance of the Manager.
     */
    private transient static TradeStore ourInstance = null;

    /**
     * Variable telling us if our data are initialized.
     */
    private transient boolean isInitialized = false;

    /**
     * Method returning the trade city manager if already initialized.
     *
     * @return a new instance of the store.
     */
    public static TradeStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new TradeStore();
        }
        return ourInstance;
    }

    /**
     * Method that completes a transaction between two trade Units.
     *
     * @param tdUnit1 the first trade unit (the from unit).
     * @param tdUnit2 the second trade unit (the to unit).
     * @param goodId  the id of the trade good.
     * @param qte     the quantity of the trade good.
     * @param phase   tha phase of trading.
     */
    public boolean doTransaction(final TradeUnitAbstractDTO tdUnit1,
                                 final TradeUnitAbstractDTO tdUnit2,
                                 final Integer goodId,
                                 final int qte,
                                 final int phase) {

        if (qte < 0) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot perform transaction with negative amount", false);
            return false;
        } else if (qte == 0) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot perform transaction with zero amount", false);
            return false;
        }

        if (defineAndAddTradeOrder(tdUnit1, tdUnit2, goodId, qte, phase)) {

            switch (tdUnit2.getUnitType()) {
                case BAGGAGETRAIN:
                    if (tdUnit2.getNationId() == GameStore.getInstance().getNationId()) {
                        BaggageTrainStore.getInstance().loadGood(goodId, tdUnit2.getId(), qte);
                    } else {
                        AlliedUnitsStore.getInstance().loadGood(BAGGAGETRAIN, goodId, tdUnit2.getId(), qte);
                    }
                    break;

                case SHIP:
                    if (tdUnit2.getNationId() == GameStore.getInstance().getNationId()) {
                        NavyStore.getInstance().loadGood(goodId, tdUnit2.getId(), qte);
                    } else {
                        AlliedUnitsStore.getInstance().loadGood(SHIP, goodId, tdUnit2.getId(), qte);
                    }
                    break;

                case TRADECITY:
                    TradeCityStore.getInstance().buyGood(tdUnit2.getId(), goodId, qte);

                        // Determine good rate taking into account the special Trade Cities Traits
                    double goodRate = TradeCalculations.determineSellTradeRate(tdUnit2.getRegionId(),
                            tdUnit2.getName(), goodId,
                            ((TradeCityDTO) tdUnit2).getGoodsTradeLvl().get(goodId));


                    // Refund the trade unit 1 for the goods bought from it
                    final int monetaryCost = TradeCalculations.getSellGoodCost(WarehouseStore.getGoodFactor(goodId),
                            goodRate,
                            qte,
                            ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                            GameStore.getInstance().getDeficit() == tdUnit2.getId(),
                            GameStore.getInstance().getSurplus() == tdUnit2.getId());

                    if (tdUnit1.getUnitType() == WAREHOUSE) {
                        WarehouseStore.getInstance().getBackResource(GOOD_MONEY, monetaryCost, RegionConstants.EUROPE);
                        TradeEventManager.giveGood(tdUnit1.getId(), GOOD_MONEY,
                                tdUnit1.getUnitType(), monetaryCost, RegionConstants.EUROPE);

                    } else {
                        tdUnit1.getGoodsDTO().get(GOOD_MONEY).setQte(tdUnit1.getGoodsDTO().get(GOOD_MONEY).getQte() + monetaryCost);
                        TradeEventManager.giveGood(tdUnit1.getId(), GOOD_MONEY,
                                tdUnit1.getUnitType(), monetaryCost, tdUnit1.getRegionId());

                    }

                    //Spend the corresponding administration points
                    WarehouseStore.getInstance().spendResource(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_EXCHF), EUROPE);
                    TradeEventManager.giveGood(WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getId(),
                            GOOD_AP, WAREHOUSE, AdminCommandPoints.P_ADM.get(ORDER_EXCHF), EUROPE);

                    break;

                case WAREHOUSE:
                default:
                    WarehouseStore.getInstance().getBackResource(goodId, qte,
                            tdUnit2.getRegionId());
                    break;
            }

            TradeEventManager.getGood(tdUnit2.getId(), goodId, tdUnit2.getUnitType(), qte, tdUnit2.getRegionId());

            switch (tdUnit1.getUnitType()) {
                case BAGGAGETRAIN:
                    if (tdUnit1.getNationId() == GameStore.getInstance().getNationId()) {
                        BaggageTrainStore.getInstance().unLoadGood(goodId, tdUnit1.getId(), qte);
                    } else {
                        AlliedUnitsStore.getInstance().unLoadGood(BAGGAGETRAIN, goodId, tdUnit1.getId(), qte);
                    }
                    break;

                case SHIP:
                    if (tdUnit1.getNationId() == GameStore.getInstance().getNationId()) {
                        NavyStore.getInstance().unLoadGood(goodId, tdUnit1.getId(), qte);
                    } else {
                        AlliedUnitsStore.getInstance().unLoadGood(SHIP, goodId, tdUnit1.getId(), qte);
                    }
                    break;

                case TRADECITY:
                    TradeCityStore.getInstance().sellGood(tdUnit1.getId(), goodId, qte);
                    // Determine good rate taking into account the special Trade Cities Traits
                    double goodRate = TradeCalculations.determineBuyTradeRate(tdUnit1.getName(), goodId,
                            ((TradeCityDTO) tdUnit1).getGoodsTradeLvl().get(goodId));
                    // Refund the trade unit 1 for the goods bought from it
                    final int monetaryCost = TradeCalculations.getBuyGoodCost(WarehouseStore.getGoodFactor(goodId),
                            goodRate,
                            qte,
                            ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                            GameStore.getInstance().getDeficit() == tdUnit1.getId(),
                            GameStore.getInstance().getSurplus() == tdUnit1.getId());

                    if (tdUnit2.getUnitType() == WAREHOUSE) {
                        WarehouseStore.getInstance().spendResource(GOOD_MONEY, monetaryCost, RegionConstants.EUROPE);
                        TradeEventManager.getGood(tdUnit2.getId(), GOOD_MONEY,
                                tdUnit2.getUnitType(), monetaryCost, RegionConstants.EUROPE);
                    } else {
                        tdUnit2.getGoodsDTO().get(GOOD_MONEY).setQte(tdUnit2.getGoodsDTO().get(GOOD_MONEY).getQte() - monetaryCost);
                        TradeEventManager.getGood(tdUnit2.getId(), GOOD_MONEY,
                                tdUnit2.getUnitType(), monetaryCost, tdUnit2.getRegionId());

                    }

                    //Spend the corresponding administration points
                    WarehouseStore.getInstance().spendResource(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_EXCHF), EUROPE);
                    TradeEventManager.giveGood(WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getId(),
                            GOOD_AP, WAREHOUSE, AdminCommandPoints.P_ADM.get(ORDER_EXCHF), EUROPE);

                    break;

                case WAREHOUSE:
                default:
                    WarehouseStore.getInstance().spendResource(goodId, qte,
                            tdUnit1.getRegionId());
                    break;
            }

            TradeEventManager.giveGood(tdUnit1.getId(), goodId, tdUnit1.getUnitType(), qte, tdUnit1.getRegionId());

        } else if (WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_AP).getQte() == 0) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "You need at least one Administration Points to perform trade", false);
            return false;
        }
        return true;
    }

    /**
     * Method that adds the transaction order between two trade Units.
     *
     * @param tdUnit1 the first trade unit (the from unit).
     * @param tdUnit2 the second trade unit (the to unit).
     * @param goodId  the id of the trade good.
     * @param qte     the quantity of the trade good.
     * @param phase   the phase of trading.
     * @return if the order was successfully added.
     */
    private boolean defineAndAddTradeOrder(final TradeUnitAbstractDTO tdUnit1,
                                           final TradeUnitAbstractDTO tdUnit2,
                                           final Integer goodId,
                                           final int qte,
                                           final int phase) {
        final int typeId;
        if (phase == 1) {
            typeId = ORDER_EXCHF;

        } else {
            typeId = ORDER_EXCHS;
        }

        final OrderCostDTO orderCost = new OrderCostDTO();
        orderCost.setNumericCost(goodId, qte);
        if (tdUnit2.getUnitType() == TRADECITY) {
            final int monetaryCost = TradeCalculations.getSellGoodCost(WarehouseStore.getInstance().getGoodFactor(goodId),
                    tdUnit2.getGoodsDTO().get(goodId).getQte(),
                    qte,
                    ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                    GameStore.getInstance().getDeficit() == tdUnit2.getId(),
                    GameStore.getInstance().getSurplus() == tdUnit2.getId());

            orderCost.setNumericCost(GOOD_MONEY, monetaryCost);

            orderCost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(typeId));
            orderCost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(typeId));

        } else if (tdUnit1.getUnitType() == TRADECITY) {
            final int monetaryCost = TradeCalculations.getBuyGoodCost(WarehouseStore.getInstance().getGoodFactor(goodId),
                    tdUnit1.getGoodsDTO().get(goodId).getQte(),
                    qte,
                    ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                    GameStore.getInstance().getDeficit() == tdUnit1.getId(),
                    GameStore.getInstance().getSurplus() == tdUnit1.getId());
            orderCost.setNumericCost(GOOD_MONEY, monetaryCost);

            orderCost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(typeId));
            orderCost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(typeId));
        }

        final int regionId = tdUnit1.getRegionId();
        final int identifiers[] = new int[6];
        identifiers[0] = tdUnit1.getUnitType();
        identifiers[1] = tdUnit1.getId();
        identifiers[2] = tdUnit2.getUnitType();
        identifiers[3] = tdUnit2.getId();
        identifiers[4] = goodId;
        identifiers[5] = qte;
        //construct a dummy order to see if it can be added.
        final ClientOrderDTO dummyOrder = OrderStore.getInstance().constructOrder(typeId, orderCost, regionId, "", identifiers, 0, "");
        int canAdd = 1;
        try {
            canAdd = WarehouseStore.getInstance().canAddOrderSpecific(dummyOrder);
            if (canAdd == 0) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "There are not enough materials to perform this trade action.", false);
            } else if (canAdd == -1) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot do this action, it will overload the trade unit.", false);
            }
        } catch (Exception e) {
            Window.alert("FTD : " + e.toString());
        }
        return (WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_AP).getQte() >= 1
                || orderCost.getNumericCost(GOOD_AP) == 0)
                && canAdd == 1
                && (OrderStore.getInstance().addNewOrder(typeId, orderCost, regionId, "", identifiers, 0, "") == 1);
    }


    /**
     * Method that undoes the corresponding order.
     *
     * @param order the trading order we are trying to undo
     */
    public void undoExchange(final ClientOrderDTO order) {
        //check if undo is possible for trade action
        int canUndo = WarehouseStore.getInstance().canRefundOrder(order);
        if (canUndo == 0) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot undo trade order because goods have been used for an other transaction", false);
            return;
        } else if (canUndo == -1) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Undoing this order will overload the trade unit with goods. Cannot do this action.", false);
            return;
        }
        try {
            final int[] ids = new int[6];
            ids[0] = order.getIdentifier(0);
            ids[1] = order.getIdentifier(1);
            ids[2] = order.getIdentifier(2);
            ids[3] = order.getIdentifier(3);
            ids[4] = order.getIdentifier(4);
            ids[5] = order.getIdentifier(5);

            if (OrderStore.getInstance().removeOrder(order.getOrderTypeId(), ids)) {
                final int sourceType = order.getIdentifier(0);
                final int targetType = order.getIdentifier(2);
                final int sourceId = order.getIdentifier(1);
                final int targetId = order.getIdentifier(3);
                int goodType = 0, goodQte = 0;
                for (int i = (GOOD_LAST - 2); i >= GOOD_FIRST; i--) {
                    if (order.getCosts().getNumericCost(i) > 0) {
                        goodType = i;
                        goodQte = order.getCosts().getNumericCost(i);
                        break;
                    }
                }

                switch (sourceType) {
                    case BAGGAGETRAIN:
                        if (BaggageTrainStore.getInstance().getBaggageTrainById(sourceId) != null) {
                            BaggageTrainStore.getInstance().loadGood(goodType, sourceId, goodQte);
                        } else {
                            AlliedUnitsStore.getInstance().loadGood(BAGGAGETRAIN, goodType, sourceId, goodQte);
                        }
                        break;

                    case SHIP:
                        if (NavyStore.getInstance().getShipById(sourceId) != null) {
                            NavyStore.getInstance().loadGood(goodType, sourceId, goodQte);
                        } else {
                            AlliedUnitsStore.getInstance().loadGood(SHIP, goodType, sourceId, goodQte);
                        }
                        break;

                    case TRADECITY:
                        TradeCityStore.getInstance().undoSellGood(sourceId, goodType, goodQte);

                        final TradeUnitAbstractDTO targetTradeUnit = getTradeUnitByType(targetType, targetId);

                        if (targetTradeUnit.getUnitType() == WAREHOUSE) {
                            WarehouseStore.getInstance().getBackResource(GOOD_MONEY, order.getCosts().getNumericCost(GOOD_MONEY), RegionConstants.EUROPE);
                            TradeEventManager.getGood(targetTradeUnit.getId(),
                                    GOOD_MONEY, targetTradeUnit.getUnitType(),
                                    order.getCosts().getNumericCost(GOOD_MONEY), RegionConstants.EUROPE);
                        } else {
                            targetTradeUnit.getGoodsDTO()
                                    .get(GOOD_MONEY)
                                    .setQte(targetTradeUnit.getGoodsDTO()
                                            .get(GOOD_MONEY).getQte()
                                            + order.getCosts().getNumericCost(GOOD_MONEY));

                            TradeEventManager.getGood(targetTradeUnit.getId(),
                                    GOOD_MONEY, targetTradeUnit.getUnitType(),
                                    order.getCosts().getNumericCost(GOOD_MONEY), targetTradeUnit.getRegionId());

                        }

                        WarehouseStore.getInstance().getBackResource(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_EXCHF), EUROPE);
                        TradeEventManager.getGood(WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getId(),
                                GOOD_AP, WAREHOUSE, AdminCommandPoints.P_ADM.get(ORDER_EXCHF), EUROPE);

                        break;

                    case WAREHOUSE:
                        WarehouseStore.getInstance().getBackResource(goodType,
                                goodQte, order.getRegionId());
                        break;
                    default:
                        break;
                }

                switch (targetType) {
                    case BAGGAGETRAIN:
                        if (BaggageTrainStore.getInstance().getBaggageTrainById(targetId) != null) {
                            BaggageTrainStore.getInstance().unLoadGood(goodType,
                                    targetId, goodQte);
                        } else {
                            AlliedUnitsStore.getInstance().unLoadGood(BAGGAGETRAIN, goodType, targetId, goodQte);
                        }
                        break;
                    case SHIP:
                        if (NavyStore.getInstance().getShipById(targetId) != null) {
                            NavyStore.getInstance().unLoadGood(goodType, targetId,
                                    goodQte);
                        } else {
                            AlliedUnitsStore.getInstance().unLoadGood(SHIP, goodType, targetId, goodQte);
                        }
                        break;
                    case TRADECITY:
                        TradeCityStore.getInstance().undoBuyGood(targetId, goodType,
                                goodQte
                        );

                        final TradeUnitAbstractDTO targetTradeUnit = getTradeUnitByType(sourceType, sourceId);

                        if (targetTradeUnit.getUnitType() == WAREHOUSE) {
                            WarehouseStore.getInstance().spendResource(GOOD_MONEY, order.getCosts().getNumericCost(GOOD_MONEY), RegionConstants.EUROPE);
                            TradeEventManager.giveGood(targetTradeUnit.getId(),
                                    GOOD_MONEY, targetTradeUnit.getUnitType(),
                                    order.getCosts().getNumericCost(GOOD_MONEY), RegionConstants.EUROPE);
                        } else {
                            targetTradeUnit
                                    .getGoodsDTO()
                                    .get(GOOD_MONEY)
                                    .setQte(targetTradeUnit.getGoodsDTO()
                                            .get(GOOD_MONEY).getQte()
                                            - order.getCosts().getNumericCost(GOOD_MONEY));

                            TradeEventManager.giveGood(targetTradeUnit.getId(),
                                    GOOD_MONEY, targetTradeUnit.getUnitType(),
                                    order.getCosts().getNumericCost(GOOD_MONEY), targetTradeUnit.getRegionId());

                        }

                        WarehouseStore.getInstance().getBackResource(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_EXCHF), EUROPE);
                        TradeEventManager.getGood(WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getId(),
                                GOOD_AP, WAREHOUSE, AdminCommandPoints.P_ADM.get(ORDER_EXCHF), EUROPE);

                        break;
                    case WAREHOUSE:
                        WarehouseStore.getInstance().spendResource(goodType,
                                goodQte, order.getRegionId());
                        break;
                    default:
                        break;
                }

                TradeEventManager.giveGood(sourceId, goodType, sourceType, goodQte, order.getRegionId());
                TradeEventManager.getGood(targetId, goodType, targetType, goodQte, order.getRegionId());
            }

        } catch (Exception e) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Unable to undo the exchange action", false);
        }

    }

    /**
     * Method that returns the trade unit with the given id
     *
     * @param type the type of the unit
     * @param id   the id of the unit
     * @return the TradeUnitAbstractDTO
     * @throws Exception in case a store encounters a problem
     */
    public TradeUnitAbstractDTO getTradeUnitByType(final int type, final int id) throws Exception {
        switch (type) {
            case BAGGAGETRAIN:
                return BaggageTrainStore.getInstance().getBaggageTMap().get(id);

            case SHIP:
                return NavyStore.getInstance().getShipById(id);

            case TRADECITY:
                return TradeCityStore.getInstance().getTradeCityById(id);

            case WAREHOUSE:
                return WarehouseStore.getInstance().getWareHouseById(id);

            default:
                throw new Exception("No such trade unit found");
        }
    }

    /**
     * Method that returns the current load of a trade unit in
     * tons.
     *
     * @param tradeUnit the trade unit whose weight we want.
     * @return the total weight of the goods.
     */
    public int getTradeUnitLoad(final TradeUnitAbstractDTO tradeUnit) {
        int weight = 0;
        if (!(tradeUnit.getUnitType() == FLEET
                || (tradeUnit.getUnitType() == SHIP
                && !NavyStore.getInstance().isTradeShip((ShipDTO) tradeUnit)))) {
            for (final StoredGoodDTO good : tradeUnit.getGoodsDTO().values()) {
                if (good.getGoodDTO().getGoodId() != GOOD_MONEY) {
                    weight += good.getQte() * (1d / good.getGoodDTO().getWeightOfGood());
                }
            }

        } else if (tradeUnit.getUnitType() == SHIP
                && !NavyStore.getInstance().isTradeShip((ShipDTO) tradeUnit)) {

            return 0;

        } else {
            if (tradeUnit instanceof FleetDTO) {
                for (final ShipDTO ship : ((FleetDTO) tradeUnit).getShips().values()) {
                    if (NavyStore.getInstance().isTradeShip(ship)) {
                        weight += getTradeUnitLoad(ship);
                    }
                }
            }
        }

        return weight;
    }


    /**
     * Method that checks if the trade unit has initiated the
     * second trading phase.
     *
     * @param tradeUnitId the id of the target trade unit.
     * @return true if it has or false if it hasn't started the second trading phase.
     */
    public boolean hasInitSecondPhase(final int tradeUnitId) {
        for (final ClientOrderDTO order : OrderStore.getInstance().getCargoRelatedOrders()) {
            if (order.getOrderTypeId() == ORDER_EXCHS
                    && (order.getIdentifier(1) == tradeUnitId || order.getIdentifier(3) == tradeUnitId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method that checks if the trade unit has initiated the
     * second trading phase.
     *
     * @param unitType    the type of the unit in question
     * @param tradeUnitId the id of the target trade unit.
     * @return true if it has or false if it hasn't started the second trading phase.
     */
    public boolean hasInitSecondPhase(final int unitType, final int tradeUnitId) {
        for (final ClientOrderDTO order : OrderStore.getInstance().getCargoRelatedOrders()) {
            if (order.getOrderTypeId() == ORDER_EXCHS
                    && ((order.getIdentifier(0) == unitType && order.getIdentifier(1) == tradeUnitId) ||
                    (order.getIdentifier(2) == unitType && order.getIdentifier(3) == tradeUnitId))) {
                return true;
            }
        }

        return TransportStore.getInstance().hasInitSecondTransportPhase(unitType, tradeUnitId);
    }

    public int getTradeUnitWeight(final TradeUnitAbstractDTO tradeUnit) {
        if (tradeUnit.getUnitType() == BAGGAGETRAIN) {
            return 1500;

        } else if (tradeUnit.getUnitType() == SHIP) {
            return ((ShipDTO) tradeUnit).getType().getLoadCapacity();

        } else if (tradeUnit.getUnitType() == TRADECITY) {
            return ((TradeCityDTO) tradeUnit).getGoodsTradeLvl().get(1);

        } else if (tradeUnit.getUnitType() == FLEET) {
            int weight = 0;
            for (ShipDTO ship : ((FleetDTO) tradeUnit).getShips().values()) {
                weight += getTradeUnitWeight(ship);
            }

            return weight;

        } else {
            return -1;
        }
    }

    public List<TradeUnitAbstractDTO> getTradeUnitsByRegionTypeAndPhase(final int type, final int region, final boolean onlyNotScuttle) {
        final List<TradeUnitAbstractDTO> retList = new ArrayList<TradeUnitAbstractDTO>();
        if (type == BAGGAGETRAIN) {
            retList.addAll(BaggageTrainStore.getInstance().getBaggageTrainsByRegion(region, onlyNotScuttle));

        } else if (type == SHIP) {
            retList.addAll(NavyStore.getInstance().getShipsByRegion(region, onlyNotScuttle));

        } else {
            retList.addAll(TradeCityStore.getInstance().getTradeCitiesByRegion(region, false));
        }
        return retList;
    }

    /**
     * Method that returns the trading units available for trading with
     * the target unit
     *
     * @param type           the type of the target unit
     * @param typeId         the id of the target unit
     * @param region         the region of the target unit
     * @param tradePhase     the trading phase
     * @param xPos           the xPos Position of the target unit
     * @param yPos           the yPos position of the target unit
     * @param onlyNotScuttle only units that are not scuttled.
     * @return List of the available for trading units
     */
    public List<TradeUnitAbstractDTO> getTradeUnitsByRegionTypePhasePos(final int type,
                                                                        final int typeId,
                                                                        final int region,
                                                                        final int tradePhase,
                                                                        final int xPos,
                                                                        final int yPos,
                                                                        final boolean onlyNotScuttle) {

        final List<TradeUnitAbstractDTO> tuaList = new ArrayList<TradeUnitAbstractDTO>();
        final List<TradeUnitAbstractDTO> retList = new ArrayList<TradeUnitAbstractDTO>();

        // add all the baggage trains to the temp list
        tuaList.addAll(BaggageTrainStore.getInstance().getBaggageTrainsByRegion(region, onlyNotScuttle));

        // add all the ships to the temp list
        tuaList.addAll(NavyStore.getInstance().getShipsByRegion(region, onlyNotScuttle));

        // add all the trading cities to the temp list
        tuaList.addAll(TradeCityStore.getInstance().getTradeCitiesByRegion(region, true));

        // determine the last unit position
        final PositionDTO pos;

        // if it has not moved or we are at the first stage
        // ignore the movement
        if (tradePhase == 1) {
            pos = new PositionDTO();
            pos.setRegionId(region);
            pos.setX(xPos);
            pos.setY(yPos);

        } else {
            final PositionDTO startPos = new PositionDTO();
            startPos.setRegionId(region);
            startPos.setX(xPos);
            startPos.setY(yPos);
            pos = MovementStore.getInstance().getUnitPosition(type, typeId, startPos);
            pos.setRegionId(region);
        }

        // check if we are on a barracks and add it to the list
        if (RegionStore.getInstance().getRegionSectorsByRegionId(region)[pos.getX()][pos.getY()].hasShipyardOrBarracks()) {
            final int nationId = RegionStore.getInstance().getRegionSectorsByRegionId(region)[pos.getX()][pos.getY()].getNationDTO().getNationId();
            if (nationId == GameStore.getInstance().getNationId()) {
                retList.add(WarehouseStore.getInstance().getWareHouseByRegion(region));

            } else if (RelationsStore.getInstance().getOriginalRelationByNationId(nationId) <= REL_TRADE) {
                final TradeUnitAbstractDTO nationUnit = new TradeUnitAbstractDTO();
                nationUnit.setUnitType(BARRACK);
                nationUnit.setId(nationId);
                nationUnit.setX(pos.getX());
                nationUnit.setY(pos.getY());
                nationUnit.setRegionId(region);
                nationUnit.setName(DataStore.getInstance().getNationNameByNationId(nationId) + "'s Warehouse");
                nationUnit.setGoodsDTO(getGiftedGoodsMap());
                retList.add(nationUnit);
            }
        }

        for (final TradeUnitAbstractDTO tdUnit : tuaList) {

            // if it has not moved or we are at the first stage
            // ignore the movement
            if (tradePhase == 1) {
                // If the target is on the same position with the trading unit, add said unit to the list
                // Do not add if the unit is the same with the target unit
                if (tdUnit.getRegionId() == pos.getRegionId() && tdUnit.getXStart() == pos.getX() && tdUnit.getYStart() == pos.getY()
                        && (tdUnit.getId() != typeId || tdUnit.getUnitType() != type)) {
                    retList.add(tdUnit);
                }

            } else {
                // If the target is on the same position with the trading unit, add said unit to the list
                // Do not add if the unit is the same with the target unit
                if (tdUnit.getRegionId() == pos.getRegionId() && tdUnit.getX() == pos.getX() && tdUnit.getY() == pos.getY()
                        && (tdUnit.getId() != typeId || tdUnit.getUnitType() != type)) {
                    retList.add(tdUnit);
                }
            }
        }
        return retList;
    }


    /**
     * Method that returns the number of the target good that can be loaded
     * on the trade unit
     *
     * @param tdUnit the trade unit.
     * @param goodId the id of the target good
     * @return the items in float representation
     */
    public double getRemainingLoadItemsByGoodId(final TradeUnitAbstractDTO tdUnit, final int goodId) {
        final double itemWeight = WarehouseStore.getGoodWeight(goodId);
        switch (tdUnit.getUnitType()) {
            case SHIP:
            case BAGGAGETRAIN:
                final int totalWeight = getTradeUnitWeight(tdUnit);
                final int currLoad = getTradeUnitLoad(tdUnit) + TransportStore.getInstance()
                        .getUnitsLoadedWeight(tdUnit.getUnitType(), tdUnit.getId(), true);

                if (goodId == GOOD_MONEY) {
                    return Double.MAX_VALUE;

                } else {
                    return (totalWeight - currLoad) / itemWeight;
                }

            case TRADECITY:
            case WAREHOUSE:
                return Double.MAX_VALUE;

            default:
                return 0;
        }
    }

    /**
     * Method that calculates and returns the gifted good
     * quantity this turn
     *
     * @param goodId the id of the good being gifted
     * @return the qte gifted
     */
    public int getGiftQteThisTurn(final int goodId) {
        final List<ClientOrderDTO> ordersF = OrderStore.getInstance().getClientOrders().get(ORDER_EXCHF);
        final List<ClientOrderDTO> ordersS = OrderStore.getInstance().getClientOrders().get(ORDER_EXCHS);
        final List<ClientOrderDTO> orders = new ArrayList<ClientOrderDTO>();
        if (ordersF != null) {
            orders.addAll(OrderStore.getInstance().getClientOrders().get(ORDER_EXCHF));
        }

        if (ordersS != null) {
            orders.addAll(OrderStore.getInstance().getClientOrders().get(ORDER_EXCHS));
        }

        int all = 0;
        for (final ClientOrderDTO order : orders) {
            if (order.getIdentifier(2) == BARRACK
                    && order.getCosts().getNumericCost(goodId) > 0) {
                all += order.getCosts().getNumericCost(goodId);
            }
        }

        return all;
    }

    /**
     * Method that returns a map containing all
     * the gifted quantities this turn
     *
     * @return HashMap of the type <GoodId,GoodDTO>
     */
    public Map<Integer, StoredGoodDTO> getGiftedGoodsMap() {
        final Map<Integer, StoredGoodDTO> goods = new HashMap<Integer, StoredGoodDTO>();
        goods.put(0, new StoredGoodDTO());
        for (int goodTPE = GOOD_FIRST; goodTPE < GOOD_AP; goodTPE++) {
            final StoredGoodDTO gd = new StoredGoodDTO();
            gd.setTpe(goodTPE);
            gd.setQte(getGiftQteThisTurn(goodTPE));
            goods.put(goodTPE, gd);
        }
        return goods;
    }

    /**
     * Method that returns the names of the trade units
     * involved in a transaction
     *
     * @param clOrder the target order
     * @return the two names
     */
    public String getUnitsNamesById(final ClientOrderDTO clOrder) {
        try {
            final String fromName = getTradeUnitByType(clOrder.getIdentifier(0), clOrder.getIdentifier(1)).getName();
            final String toName = getTradeUnitByType(clOrder.getIdentifier(2), clOrder.getIdentifier(3)).getName();
            return "From: " + fromName + " To: " + toName;

        } catch (Exception e) {
            return "unknown error";
        }
    }

    /**
     * @param isInitialized the isInitialized to set.
     */
    public void setInitialized(final boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    /**
     * @return the isInitialized.
     */
    public boolean isInitialized() {
        return isInitialized;
    }

}
