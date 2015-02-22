package com.eaw1805.www.controllers.remote.hotspot.region;

import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.OrderPositionDTO;
import com.eaw1805.data.managers.beans.SectorManagerBean;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionApplyChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_INC_POP,
            ORDER_DEC_POP,
            ORDER_HOVER_SEC
    };

    private transient Map<Integer, OrderPositionDTO> sectorOrderMap;

    private transient List<OrderDTO> orders;

    private transient SectorManagerBean sectorManager;

    /**
     * Default constructor.
     *
     * @param thisGame   the game of the order.
     * @param thisNation the owner of the order.
     * @param thisTurn   the turn of the order.
     */
    public RegionApplyChangesProcessor(final int thisScenario, final int thisGame, final int thisNation, final int thisTurn, final SectorManagerBean manager) {
        super(thisScenario, thisGame, thisNation, thisTurn);
        sectorManager = manager;
    }



    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        orders = (List<OrderDTO>) chData;
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        sectorOrderMap = (HashMap<Integer, OrderPositionDTO>) dbData;
    }

    public List<?> processChanges() {
        final List<Map<Integer, OrderPositionDTO>> dummyList = new ArrayList<Map<Integer, OrderPositionDTO>>();
        for (OrderDTO order : orders) {
            OrderPositionDTO orderPos = new OrderPositionDTO();
            switch (order.getType()) {

                case ORDER_INC_POP:
                    orderPos.setOrderType(ORDER_INC_POP);
                    orderPos.setPosition(order.getPosition());
                    orderPos.setParameter1(order.getParameter1());
                    // In temp1 we save the population size of the sector
                    orderPos.setParameter2(order.getTemp1());
                    orderPos.setParameter3(order.getParameter2());
                    orderPos.setParameter4(String.valueOf(sectorManager.getByID(Integer.parseInt(order.getParameter1())).getNation().getId()));
                    sectorOrderMap.put(Integer.parseInt(order.getParameter1()), orderPos);
                    break;

                case ORDER_DEC_POP:
                    orderPos.setOrderType(ORDER_DEC_POP);
                    orderPos.setPosition(order.getPosition());
                    orderPos.setParameter1(order.getParameter1());
                    // In temp1 we save the population size of the sector
                    orderPos.setParameter2(order.getTemp1());
                    orderPos.setParameter3(order.getParameter2());
                    orderPos.setParameter4(String.valueOf(sectorManager.getByID(Integer.parseInt(order.getParameter1())).getNation().getId()));
                    sectorOrderMap.put(Integer.parseInt(order.getParameter1()), orderPos);
                    break;

                case ORDER_HOVER_SEC:
                    if (sectorOrderMap.containsKey(Integer.parseInt(order.getParameter1()))) {
                        orderPos = sectorOrderMap.get(Integer.parseInt(order.getParameter1()));
                        orderPos.setParameter4(order.getParameter2());
                    } else {
                        orderPos.setOrderType(ORDER_HOVER_SEC);
                        orderPos.setPosition(order.getPosition());
                        orderPos.setParameter1(order.getParameter1());
                        orderPos.setParameter2(order.getTemp1());
                        // In temp1 we save the population size of the sector
                        orderPos.setParameter3(String.valueOf(sectorManager.getByID(Integer.parseInt(order.getParameter1())).getPosition().getRegion().getId()));
                        orderPos.setParameter4(order.getParameter2());
                        sectorOrderMap.put(Integer.parseInt(order.getParameter1()), orderPos);
                    }

                    break;

                default:
                    // do nothing
            }
        }
        dummyList.add(sectorOrderMap);
        return dummyList;
    }

}