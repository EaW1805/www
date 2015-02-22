package com.eaw1805.www.controllers.remote.hotspot.army;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;
import com.eaw1805.www.shared.orders.economy.ChangeBarrackNameOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Apply changes the orders
 */
public class BarrackApplyChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, ArmyConstants, OrderConstants {

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_REN_BARRACK
    };

    /**
     * A list containing all the available barracks and the corresponding map.
     */
    private transient List<BarrackDTO> barracksList = new ArrayList<BarrackDTO>();

    private transient final Map<Integer, BarrackDTO> barracksMap = new HashMap<Integer, BarrackDTO>();

    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();

    public BarrackApplyChangesProcessor(final int thisScenario, final int thisGame, final int thisNation, final int thisTurn) {
        super(thisScenario, thisGame, thisNation, thisTurn);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chOrders) {
        barracksList = (List<BarrackDTO>) dbData;
        for (BarrackDTO barrack : barracksList) {
            barracksMap.put(barrack.getId(), barrack);
        }

        orders = (List<OrderDTO>) chOrders;
    }

    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        // do nothing
    }

    public List<BarrackDTO> processChanges() {
        for (OrderDTO order : orders) {
            if (order.getType() == ORDER_REN_BARRACK) {
                execOrderRename(order);
            }
        }

        return barracksList;
    }

    private void execOrderRename(final OrderDTO order) {
        final int barId = Integer.parseInt(order.getParameter1());
        if (barracksMap.containsKey(barId)) {
            final ChangeBarrackNameOrder ccnName = new ChangeBarrackNameOrder(barracksMap, order.getParameter2());
            ccnName.execute(barId);
        }
    }

}
