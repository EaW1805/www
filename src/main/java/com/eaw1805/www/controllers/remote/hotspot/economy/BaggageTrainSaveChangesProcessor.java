package com.eaw1805.www.controllers.remote.hotspot.economy;

import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.controllers.remote.EmpireRpcServiceImpl;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaggageTrainSaveChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, OrderConstants {

    private transient List<BaggageTrainDTO> baggageTList, dbBaggageTList = new ArrayList<BaggageTrainDTO>();

    private transient Map<Integer, List<BaggageTrainDTO>> newBaggageTMap = new HashMap<Integer, List<BaggageTrainDTO>>();

    private transient final Map<Integer, List<ClientOrderDTO>> orderMap = new HashMap<Integer, List<ClientOrderDTO>>();

    private transient final EmpireRpcServiceImpl service;

    public BaggageTrainSaveChangesProcessor(final int thisScenarioId, final int thisGame, final int thisNation,
                                            final int thisTurn,
                                            final EmpireRpcServiceImpl empireRpcServiceImpl) {
        super(thisScenarioId, thisGame, thisNation, thisTurn);
        service = empireRpcServiceImpl;
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        dbBaggageTList = (List<BaggageTrainDTO>) dbData;
        baggageTList = (List<BaggageTrainDTO>) chData;
    }

    public void addOrders(final Map<Integer, List<ClientOrderDTO>> clientOrders) {
        orderMap.putAll(clientOrders);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        newBaggageTMap = (Map<Integer, List<BaggageTrainDTO>>) chData;
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> orders = new ArrayList<OrderDTO>();
        final List<ClientOrderDTO> scutOrders = orderMap.get(ORDER_SCUTTLE_BTRAIN);
        for (final BaggageTrainDTO btrain : dbBaggageTList) {
            //save scuttle baggage train orders
            if (scutOrders != null) {
                for (ClientOrderDTO order : scutOrders) {
                    if (order.getIdentifier(0) == btrain.getId()) {
                        orders.add(createOrderScuttle(btrain, order));
                        break;
                    }
                }
            }

            for (final BaggageTrainDTO cbtrain : baggageTList) {
                //save rename orders
                final String name = cbtrain.getName();
                if (btrain.getId() == cbtrain.getId()) {
                    if (!name.equals(btrain.getName())) {
                    final List<ClientOrderDTO> renOrder = orderMap.get(ORDER_REN_BTRAIN);
                        if (renOrder != null) {
                            for (final ClientOrderDTO order : renOrder) {
                                if (order.getIdentifier(0) == btrain.getId()) {
                                    orders.add(createOrderRename(btrain, name, order));
                                    break;
                                }
                            }
                        }
                    }

                    //save repair orders
                    if (btrain.getCondition() != cbtrain.getCondition()) {
                        final SectorDTO sector = service.getSectorByCoordinates(getScenarioId(), getGameId(), btrain.getRegionId(), btrain.getX(), btrain.getY());
                        orders.add(createOrderRepair(btrain, sector));
                    }
                    break;
                }
            }
        }

        for (final int sectorId : newBaggageTMap.keySet()) {
            for (final BaggageTrainDTO btrain : newBaggageTMap.get(sectorId)) {
                orders.add(createOrderBuild(sectorId, btrain));
            }
        }
        return orders;
    }

    private OrderDTO createOrderBuild(final int sectorId, final BaggageTrainDTO btrain) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_B_BTRAIN, 0, 0, String.valueOf(sectorId), String.valueOf(btrain.getId())
                , btrain.getName(), ""
                , "", "", "", "", "", String.valueOf(btrain.getRegionId()), "", "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderRepair(final BaggageTrainDTO btrain, final SectorDTO sector) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_R_BTRAIN, 0, 0,
                String.valueOf(btrain.getId()),
                String.valueOf(sector.getId()),
                String.valueOf(100 - btrain.getCondition())
                , "", "", "", "", "", "",
                String.valueOf(btrain.getRegionId())
                , "", "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderRename(final BaggageTrainDTO btrain, final String name, final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_REN_BTRAIN, order.getPriority(), 0, String.valueOf(btrain.getId()), name, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderScuttle(final BaggageTrainDTO btrain, final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_SCUTTLE_BTRAIN, order.getPriority(), 0, String.valueOf(btrain.getId()), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

}
