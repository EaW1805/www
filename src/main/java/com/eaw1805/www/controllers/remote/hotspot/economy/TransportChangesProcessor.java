package com.eaw1805.www.controllers.remote.hotspot.economy;

import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TransportChangesProcessor extends AbstractChangesProcessor implements ChangesProcessor, OrderConstants, GoodConstants {

    private transient List<ClientOrderDTO> transportOrders;

    /**
     * Default constructor.
     *
     * @param thisGame   the game of the order.
     * @param thisNation the owner of the order.
     * @param thisTurn   the turn of the order.
     */
    public TransportChangesProcessor(final int scenarioId, final int thisGame, final int thisNation, final int thisTurn) {
        super(scenarioId, thisGame, thisNation, thisTurn);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        transportOrders = (List<ClientOrderDTO>) chData;
    }

    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        // do nothing.
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> orders = new ArrayList<OrderDTO>();
        for (final ClientOrderDTO order : transportOrders) {
            //be sure the orders are for transportation since two more type orders could arrive at this point.
            if (order.getOrderTypeId() == ORDER_LOAD_TROOPSF || order.getOrderTypeId() == ORDER_LOAD_TROOPSS
                    || order.getOrderTypeId() == ORDER_UNLOAD_TROOPSF || order.getOrderTypeId() == ORDER_UNLOAD_TROOPSS) {
                orders.add(createOrderTransport(order));
            }
        }

        return orders;
    }

    private OrderDTO createOrderTransport(final ClientOrderDTO order) {
        final OrderDTO dborder = new OrderDTO();
        dborder.setType(order.getOrderTypeId());
        dborder.setGameId(getGameId());
        dborder.setNationId(getNationId());
        dborder.setTurn(getTurn());
        dborder.setPosition(order.getPriority());

        switch (order.getOrderTypeId()) {

            case ORDER_LOAD_TROOPSF:
            case ORDER_LOAD_TROOPSS:
                dborder.setParameter1(String.valueOf(order.getIdentifier(0)));
                dborder.setParameter2(String.valueOf(order.getIdentifier(1)));
                dborder.setParameter3(String.valueOf(order.getIdentifier(2)));
                dborder.setParameter4(String.valueOf(order.getIdentifier(3)));
                dborder.setParameter5(String.valueOf(order.getIdentifier(4)));
                dborder.setParameter6(String.valueOf(order.getIdentifier(5)));
                dborder.setTemp3(String.valueOf(order.getCosts().getNumericCost(GOOD_CP)));
                break;

            case ORDER_UNLOAD_TROOPSF:
            case ORDER_UNLOAD_TROOPSS:
                dborder.setParameter1(String.valueOf(order.getIdentifier(0)));
                dborder.setParameter2(String.valueOf(order.getIdentifier(1)));
                dborder.setParameter3(String.valueOf(order.getIdentifier(2)));
                dborder.setParameter4(String.valueOf(order.getIdentifier(3)));
                dborder.setParameter5(String.valueOf(order.getIdentifier(4)));
                dborder.setTemp1(String.valueOf(order.getIdentifier(5)));
                dborder.setTemp2(String.valueOf(order.getIdentifier(6)));
                dborder.setTemp3(String.valueOf(order.getCosts().getNumericCost(GOOD_CP)));
                break;

            default:
                // do nothing
        }
        return dborder;
    }

}