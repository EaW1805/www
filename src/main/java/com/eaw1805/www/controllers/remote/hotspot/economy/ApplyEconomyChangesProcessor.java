package com.eaw1805.www.controllers.remote.hotspot.economy;

import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplyEconomyChangesProcessor
        extends AbstractChangesProcessor
        implements OrderConstants {

    private final transient Map<Integer, SectorDTO> sectorsMap = new HashMap<Integer, SectorDTO>();

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_B_PRODS,
            ORDER_D_PRODS
    };

    /**
     * A hash map containing all the Sectors and their corresponding production sites.
     */
    private transient final Map<SectorDTO, Integer> sectorProdSites = new HashMap<SectorDTO, Integer>();

    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();

    /**
     * Default constructor.
     *
     * @param thisGame   the game of the order.
     * @param thisNation the owner of the order.
     * @param thisTurn   the turn of the order.
     */
    public ApplyEconomyChangesProcessor(final int thisScenario, final int thisGame, final int thisNation, final int thisTurn) {
        super(thisScenario, thisGame, thisNation, thisTurn);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chOrders) {
        for (SectorDTO sector : (List<SectorDTO>) dbData) {
            sectorsMap.put(sector.getId(), sector);
        }
        orders = (List<OrderDTO>) chOrders;
    }

    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        // do nothing
    }

    public Map<SectorDTO, Integer> processChanges() {
        for (OrderDTO order : orders) {
            switch (order.getType()) {
                case ORDER_B_PRODS:
                    final int prodSiteId = Integer.parseInt(order.getParameter2());
                    sectorProdSites.put(sectorsMap.get(Integer.parseInt(order.getParameter1())), prodSiteId);
                    break;

                case ORDER_D_PRODS:
                    sectorProdSites.put(sectorsMap.get(Integer.parseInt(order.getParameter1())), -1);
                    break;

                default:
                    // do nothing
            }

        }
        return sectorProdSites;
    }

}
	
