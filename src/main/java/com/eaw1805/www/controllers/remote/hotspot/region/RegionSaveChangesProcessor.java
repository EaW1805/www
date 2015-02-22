package com.eaw1805.www.controllers.remote.hotspot.region;

import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.OrderPositionDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RegionSaveChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, OrderConstants {

    private transient Map<Integer, OrderPositionDTO> sectorOrderMap;

    private transient final Map<Integer, List<ClientOrderDTO>> orderMap;

    /**
     * Default constructor.
     *
     * @param gameId   the game of the order.
     * @param nationId the owner of the order.
     * @param turn     the turn of the order.
     * @param orderMap the orders.
     */
    public RegionSaveChangesProcessor(final int thisScenario, final int gameId,
                                      final int nationId,
                                      final int turn,
                                      final Map<Integer, List<ClientOrderDTO>> orderMap) {
        super(thisScenario, gameId, nationId, turn);
        this.orderMap = orderMap;
    }

    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        // do nothing
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        sectorOrderMap = (Map<Integer, OrderPositionDTO>) dbData;
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> newSectorOrders = new ArrayList<OrderDTO>();
        if (sectorOrderMap != null) {
            for (final OrderPositionDTO orderPosition : sectorOrderMap.values()) {
                final int param1 = Integer.parseInt(orderPosition.getParameter1());
                switch (orderPosition.getOrderType()) {
                    case ORDER_INC_POP: {
                        final List<ClientOrderDTO> orders = orderMap.get(ORDER_INC_POP);
                        if (orders != null) {
                            for (final ClientOrderDTO order : orders) {
                                if (order.getIdentifier(0) == param1) {
                                    newSectorOrders.add(createOrderIncrease(orderPosition, order));
                                    break;
                                }
                            }
                        }
                        //it also could contain data for hand over
                        final List<ClientOrderDTO> handOverOrders = orderMap.get(ORDER_HOVER_SEC);
                        if (handOverOrders != null) {
                            for (final ClientOrderDTO order : handOverOrders) {
                                if (order.getIdentifier(0) == param1) {
                                    newSectorOrders.add(createOrderHandover(orderPosition, order));
                                    break;
                                }
                            }
                        }

                        break;
                    }

                    case ORDER_DEC_POP: {
                        final List<ClientOrderDTO> orders = orderMap.get(ORDER_DEC_POP);
                        if (orders != null) {
                            for (ClientOrderDTO order : orders) {
                                if (order.getIdentifier(0) == param1) {
                                    newSectorOrders.add(createOrderDecrease(orderPosition, order));
                                    break;
                                }
                            }
                        }
                        //it also could contain data for hand over
                        final List<ClientOrderDTO> handOverOrders = orderMap.get(ORDER_HOVER_SEC);
                        if (handOverOrders != null) {
                            for (final ClientOrderDTO order : handOverOrders) {
                                if (order.getIdentifier(0) == param1) {
                                    newSectorOrders.add(createOrderHandover(orderPosition, order));
                                    break;
                                }
                            }
                        }

                        break;
                    }

                    case ORDER_HOVER_SEC: {
                        final List<ClientOrderDTO> orders = orderMap.get(ORDER_HOVER_SEC);
                        if (orders != null) {
                            for (final ClientOrderDTO order : orders) {
                                if (order.getIdentifier(0) == param1) {
                                    newSectorOrders.add(createOrderHandover(orderPosition, order));
                                    break;
                                }
                            }
                        }

                        break;
                    }

                    default:
                        // do nothing
                }
            }
        }

        return newSectorOrders;
    }

    private OrderDTO createOrderIncrease(final OrderPositionDTO orderPosition,
                                         final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_INC_POP, order.getPriority(), 0, orderPosition.getParameter1()
                , orderPosition.getParameter3(), "", "", "", "", "", "", "", orderPosition.getParameter2(), "", "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderDecrease(final OrderPositionDTO orderPosition,
                                         final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_DEC_POP, order.getPriority(), 0, orderPosition.getParameter1()
                , orderPosition.getParameter3(), "", "", "", "", "", "", "", orderPosition.getParameter2(), "", "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderHandover(final OrderPositionDTO orderPosition,
                                         final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_HOVER_SEC, order.getPriority(), 0, orderPosition.getParameter1()
                , orderPosition.getParameter4(), "", "", "", "", "", "", "", orderPosition.getParameter2(), "", "", "", "", "", "", "", "");
    }

}
