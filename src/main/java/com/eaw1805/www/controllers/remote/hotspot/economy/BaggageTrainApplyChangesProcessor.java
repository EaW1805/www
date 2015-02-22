package com.eaw1805.www.controllers.remote.hotspot.economy;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;
import com.eaw1805.www.shared.orders.economy.ChangeBaggageTrainNameOrder;
import com.eaw1805.www.shared.orders.economy.ScuttleBaggageTrainOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaggageTrainApplyChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, ArmyConstants, GoodConstants {

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_EXCHF,
            ORDER_EXCHS,
            ORDER_R_BTRAIN,
            ORDER_LOAD_TROOPSF,
            ORDER_LOAD_TROOPSS,
            ORDER_UNLOAD_TROOPSF,
            ORDER_UNLOAD_TROOPSS,
            ORDER_REN_BTRAIN,
            ORDER_SCUTTLE_BTRAIN,
            ORDER_M_BTRAIN,
            ORDER_M_UNIT
    };

    public static final Object[] ALLIED_ORDERS_TYPES = {
            ORDER_LOAD_TROOPSF,
            ORDER_LOAD_TROOPSS,
            ORDER_UNLOAD_TROOPSF,
            ORDER_UNLOAD_TROOPSS,
            ORDER_EXCHF,
            ORDER_EXCHS
    };

    /**
     * A list containing all the available baggage trains and the corresponding map.
     */
    private transient List<BaggageTrainDTO> baggageTList = new ArrayList<BaggageTrainDTO>();

    /**
     * Indexes baggage trains based on the identity.
     */
    private transient final Map<Integer, BaggageTrainDTO> baggageTMap = new HashMap<Integer, BaggageTrainDTO>();

    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();

    public BaggageTrainApplyChangesProcessor(final int scenarioId, final int gameId, final int nationId, final int turn) {
        super(scenarioId, gameId, nationId, turn);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        baggageTList = (List<BaggageTrainDTO>) dbData;
        for (BaggageTrainDTO baggageTrain : this.baggageTList) {
            baggageTMap.put(baggageTrain.getId(), baggageTrain);
        }

        orders = (List<OrderDTO>) chData;
    }

    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        // do nothing
    }

    public List<BaggageTrainDTO> processChanges() {
        for (OrderDTO order : orders) {
            switch (order.getType()) {
                case ORDER_EXCHF:
                case ORDER_EXCHS:
                    execOrderTrade(order);
                    break;

                case ORDER_R_BTRAIN:
                    execOrderRepair(order);
                    break;

                case ORDER_LOAD_TROOPSF:
                case ORDER_LOAD_TROOPSS:
                    execOrderLoadT(order);
                    break;

                case ORDER_UNLOAD_TROOPSF:
                case ORDER_UNLOAD_TROOPSS:
                    execOrderUnloadT(order);
                    break;

                case ORDER_REN_BTRAIN:
                    execOrderRen(order);
                    break;

                case ORDER_SCUTTLE_BTRAIN:
                    execOrderScuttle(order);
                    break;

                case ORDER_M_BTRAIN:
                case ORDER_M_UNIT:
                    execOrderMove(order);
                    break;

                default:
                    // ignore order
            }
        }
        return baggageTList;
    }

    private void execOrderTrade(final OrderDTO order) {
        if (Integer.parseInt(order.getParameter1()) == BAGGAGETRAIN) {
            final int trainId = Integer.parseInt(order.getParameter2());
            final int tpe = Integer.parseInt(order.getParameter5());
            final int secondPart = Integer.parseInt(order.getParameter3());
            final int quantity = Integer.parseInt(order.getParameter6());
            if (baggageTMap.containsKey(trainId)) {
                final int originalQTE = baggageTMap.get(trainId).getGoodsDTO().get(tpe).getQte();
                if (quantity > 0 && originalQTE >= quantity) {
                    baggageTMap.get(trainId).getGoodsDTO().get(tpe).setQte(originalQTE - quantity);
                    if (order.getNationId() != getNationId()) {
                        baggageTMap.get(trainId).getOriginalGoodsDTO().get(tpe).setQte(originalQTE - quantity);
                    }
                    if (secondPart == TRADECITY) {
                        final int amount = Integer.parseInt(order.getTemp1());
                        final int originalMoney = baggageTMap.get(trainId).getGoodsDTO().get(GOOD_MONEY).getQte();
                        baggageTMap.get(trainId).getGoodsDTO().get(GOOD_MONEY).setQte(originalMoney + amount);
                        if (order.getNationId() != getNationId()) {
                            baggageTMap.get(trainId).getOriginalGoodsDTO().get(GOOD_MONEY).setQte(originalMoney + amount);
                        }
                    }
                }
            }
        }

        if (Integer.parseInt(order.getParameter3()) == BAGGAGETRAIN) {
            final int trainId = Integer.parseInt(order.getParameter4());
            final int tpe = Integer.parseInt(order.getParameter5());
            final int secondPart = Integer.parseInt(order.getParameter1());
            final int quantity = Integer.parseInt(order.getParameter6());
            if (baggageTMap.containsKey(trainId)) {
                final int originalQTE = baggageTMap.get(trainId).getGoodsDTO().get(tpe).getQte();
                if (quantity > 0) {
                    if (secondPart == TRADECITY) {
                        final int amount = Integer.parseInt(order.getTemp1());
                        final int originalMoney = baggageTMap.get(trainId).getGoodsDTO().get(GOOD_MONEY).getQte();

                        if (amount > 0 & originalMoney >= amount) {
                            baggageTMap.get(trainId).getGoodsDTO().get(GOOD_MONEY).setQte(originalMoney - amount);
                            baggageTMap.get(trainId).getGoodsDTO().get(tpe).setQte(originalQTE + quantity);
                            if (order.getNationId() != getNationId()) {
                                baggageTMap.get(trainId).getOriginalGoodsDTO().get(GOOD_MONEY).setQte(originalMoney - amount);
                                baggageTMap.get(trainId).getOriginalGoodsDTO().get(tpe).setQte(originalQTE + quantity);
                            }
                        }

                    } else {
                        baggageTMap.get(trainId).getGoodsDTO().get(tpe).setQte(originalQTE + quantity);
                        if (order.getNationId() != getNationId()) {
                            baggageTMap.get(trainId).getOriginalGoodsDTO().get(tpe).setQte(originalQTE + quantity);
                        }
                    }
                }
            }
        }
    }

    private void execOrderRepair(final OrderDTO order) {
        final int trainId = Integer.parseInt(order.getParameter1());
        final BaggageTrainDTO btrain = baggageTMap.get(trainId);
        btrain.setCondition(100);
    }

    private void execOrderLoadT(final OrderDTO order) {
        final int trainId = Integer.parseInt(order.getParameter2());
        final int type1 = Integer.parseInt(order.getParameter1());
        final int cargoType = Integer.parseInt(order.getParameter3());
        final int cargoId = Integer.parseInt(order.getParameter4());
        if (type1 == BAGGAGETRAIN) {
            final TransportUnitDTO tUnit = baggageTMap.get(trainId);
            if (tUnit != null) {
                if (!tUnit.getLoadedUnitsMap().containsKey(cargoType)) {
                    tUnit.getLoadedUnitsMap().put(cargoType, new ArrayList<Integer>());
                }
                tUnit.getLoadedUnitsMap().get(cargoType).add(cargoId);
            }
        }
    }

    private void execOrderUnloadT(final OrderDTO order) {
        final int trainId = Integer.parseInt(order.getParameter2());
        final int type1 = Integer.parseInt(order.getParameter1());
        final int cargoType = Integer.parseInt(order.getParameter3());
        final int cargoId = Integer.parseInt(order.getParameter4());
        if (type1 == BAGGAGETRAIN) {
            final TransportUnitDTO tUnit = baggageTMap.get(trainId);
            if (tUnit != null) {
                tUnit.getLoadedUnitsMap().get(cargoType).remove((Integer) cargoId);
            }
        }
    }

    private void execOrderRen(final OrderDTO order) {
        final int trainId = Integer.parseInt(order.getParameter1());
        if (baggageTMap.containsKey(trainId)) {
            final ChangeBaggageTrainNameOrder ccnName = new ChangeBaggageTrainNameOrder(baggageTMap, order.getParameter2());
            ccnName.execute(trainId);
        }
    }

    private void execOrderScuttle(final OrderDTO order) {
        final int trainId = Integer.parseInt(order.getParameter1());
        final ScuttleBaggageTrainOrder sbtOrder = new ScuttleBaggageTrainOrder(baggageTMap, true);
        sbtOrder.execute(trainId);
    }

    private void execOrderMove(final OrderDTO order) {
        final int unitType = Integer.parseInt(order.getParameter1());
        if (unitType == BAGGAGETRAIN) {
            final int type1 = Integer.parseInt(order.getParameter1());
            final int trainId = Integer.parseInt(order.getParameter2());
            final PositionDTO pos = getLastPositionByString(order.getParameter3());
            moveUnitByTypeToNewPosition(type1, trainId, pos);
        }
    }

    /**
     * Method that returns the last movement sector depending
     * to the movement string saved in the database
     *
     * @param movementString the target movement string
     * @return the position dto object
     */

    private PositionDTO getLastPositionByString(final String movementString) {
        final PositionDTO lastPosition = new PositionDTO();
        final String[] moves = movementString.split("!");
        final String lastMove = moves[moves.length - 1];
        final String[] sectors = lastMove.split("-");
        final String lastSector = sectors[sectors.length - 1];
        final String[] coords = lastSector.split(":");

        lastPosition.setX(Integer.parseInt(coords[0]));
        lastPosition.setY(Integer.parseInt(coords[1]));

        return lastPosition;
    }

    /**
     * Moves a unit to the specified position
     *
     * @param type the type of the unit
     * @param id   the id of the unit
     * @param pos  the specified position
     */
    public void moveUnitByTypeToNewPosition(final int type, final int id, final PositionDTO pos) {
        PositionDTO oldPos = null;
        if (type == BAGGAGETRAIN) {
            oldPos = baggageTMap.get(id);
        }

        if (oldPos != null) {
            oldPos.setX(pos.getX());
            oldPos.setY(pos.getY());
        }
    }

}
