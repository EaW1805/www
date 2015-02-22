package com.eaw1805.www.controllers.remote.hotspot.region;

import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdSitesChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    private transient Map<SectorDTO, Integer> chSectorProductSites;

    private transient final Map<Integer, List<ClientOrderDTO>> orderMap;

    /**
     * Default constructor.
     *
     * @param gameId   the game of the order.
     * @param nationId the owner of the order.
     * @param turn     the turn of the order.
     * @param orderMap the orders indexed by order ID.
     */
    public ProdSitesChangesProcessor(final int thisScenario, final int gameId,
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
    public void addData(Map<?, ?> dbData, Map<?, ?> chData) {
        chSectorProductSites = (HashMap<SectorDTO, Integer>) chData;
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> buildDemProSiteOrders = new ArrayList<OrderDTO>();
        for (final SectorDTO sector : chSectorProductSites.keySet()) {
            final int prodSiteValue = chSectorProductSites.get(sector);
            OrderDTO bdOrder = new OrderDTO();
            if (prodSiteValue > 0) {
                final List<ClientOrderDTO> orders = orderMap.get(ORDER_B_PRODS);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == sector.getId() && order.getIdentifier(1) == prodSiteValue) {
                            bdOrder = createOrderBuild(sector, prodSiteValue, order);
                            break;
                        }
                    }
                }

            } else {
                final List<ClientOrderDTO> orders = orderMap.get(ORDER_D_PRODS);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == sector.getId()) {
                            bdOrder = createOrderDemolish(sector, order);
                            break;
                        }
                    }
                }
            }

            buildDemProSiteOrders.add(bdOrder);
        }

        return buildDemProSiteOrders;
    }

    private OrderDTO createOrderBuild(final SectorDTO sector, final int prodSiteValue, final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_B_PRODS, order.getPriority(), 0,
                String.valueOf(sector.getId()),
                String.valueOf(prodSiteValue), "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderDemolish(final SectorDTO sector, final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_D_PRODS, order.getPriority(), 0,
                String.valueOf(sector.getId()), "", "", "", "", "", "", "", ""
                , "", "", "", "", "", "", "", "", "");
    }

}
