package com.eaw1805.www.controllers.remote.hotspot.economy;

import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TradeChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, OrderConstants, GoodConstants {

    private transient List<ClientOrderDTO> tradeOrders;

    /**
     * Default constructor.
     *
     * @param thisGame   the game of the order.
     * @param thisNation the owner of the order.
     * @param thisTurn   the turn of the order.
     */
    public TradeChangesProcessor(final int thisScenario, final int thisGame, final int thisNation, final int thisTurn) {
        super(thisScenario, thisGame, thisNation, thisTurn);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        tradeOrders = (List<ClientOrderDTO>) chData;
    }

    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        // do nothing
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> orders = new ArrayList<OrderDTO>();
        for (ClientOrderDTO order : tradeOrders) {
            //be sure you will save only the trade orders, since load unload also arrive here.
            if (order.getOrderTypeId() == ORDER_EXCHF
                    || order.getOrderTypeId() == ORDER_EXCHS) {
                final OrderDTO tOrder = createOrderTrade(order);
                if (tOrder != null) {
                    orders.add(tOrder);
                }
            }
        }

        return orders;
    }

    private OrderDTO createOrderTrade(final ClientOrderDTO order) {
        final OrderDTO dborder = new OrderDTO();
        dborder.setType(order.getOrderTypeId());
        dborder.setGameId(this.getGameId());
        dborder.setNationId(this.getNationId());
        dborder.setTurn(this.getTurn());
        dborder.setPosition(order.getPriority());

        // Parameter 2 is the id of the second half of the transaction
        final int goodId = getTargetGood(order.getCosts());
        if (goodId == 0) {
            return null;
        }
        dborder.setParameter5(String.valueOf(goodId));
        dborder.setParameter6(String.valueOf(order.getCosts().getNumericCost(goodId)));
        dborder.setParameter1(String.valueOf(order.getIdentifier(0)));
        dborder.setParameter2(String.valueOf(order.getIdentifier(1)));
        dborder.setParameter3(String.valueOf(order.getIdentifier(2)));
        dborder.setParameter4(String.valueOf(order.getIdentifier(3)));
        dborder.setParameter9(String.valueOf(order.getRegionId()));
        dborder.setTemp1(String.valueOf(order.getCosts().getNumericCost(GOOD_MONEY)));
        return dborder;
    }

    private int getTargetGood(final OrderCostDTO costs) {
        for (int goodIndex = (GOOD_LAST - 2); goodIndex >= GOOD_FIRST; goodIndex--) {
            if (costs.getNumericCost(goodIndex) != 0) {
                return goodIndex;
            }
        }
        return 0;
    }

}
