package com.eaw1805.www.controllers.remote.hotspot.economy;

import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupNewBaggageTrainOrders
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_B_BTRAIN
    };

    private transient Map<Integer, List<BaggageTrainDTO>> newBtrains = new HashMap<Integer, List<BaggageTrainDTO>>();

    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();

    /**
     * Default constructor.
     *
     * @param thisGame   the game of the order.
     * @param thisNation the owner of the order.
     * @param thisTurn   the turn of the order.
     */
    public SetupNewBaggageTrainOrders(final int scenarioId, final int thisGame, final int thisNation, final int thisTurn) {
        super(scenarioId, thisGame, thisNation, thisTurn);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chOrders) {
        orders = (List<OrderDTO>) chOrders;
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        newBtrains = (Map<Integer, List<BaggageTrainDTO>>) dbData;
    }

    public List<Map<Integer, List<BaggageTrainDTO>>> processChanges() {
        List<Map<Integer, List<BaggageTrainDTO>>> dummyList = new ArrayList<Map<Integer, List<BaggageTrainDTO>>>();
        for (OrderDTO order : orders) {
            if (!newBtrains.containsKey(Integer.parseInt(order.getParameter1()))) {
                newBtrains.put(Integer.parseInt(order.getParameter1()), new ArrayList<BaggageTrainDTO>());
            }

            final BaggageTrainDTO btrain = new BaggageTrainDTO();
            btrain.setId(Integer.parseInt(order.getParameter2()));
            btrain.setName(order.getParameter3());
            btrain.setRegionId(Integer.parseInt(order.getTemp1()));
            newBtrains.get(Integer.parseInt(order.getParameter1())).add(btrain);
        }

        dummyList.add(newBtrains);
        return dummyList;
    }

}