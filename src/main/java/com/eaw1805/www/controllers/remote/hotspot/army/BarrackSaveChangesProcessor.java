package com.eaw1805.www.controllers.remote.hotspot.army;

import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Process barrack changes.
 */
public class BarrackSaveChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    private transient Map<Integer, BarrackDTO> dbBarracks, chBarracks;

    private transient final Map<Integer, List<ClientOrderDTO>> relOrders;

    public BarrackSaveChangesProcessor(final int thisScenario, final int gameId,
                                       final int nationId,
                                       final int turn,
                                       final Map<Integer, List<ClientOrderDTO>> relOrders) {
        super(thisScenario, gameId, nationId, turn);
        this.relOrders = relOrders;
    }

    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        //empty function
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        dbBarracks = (Map<Integer, BarrackDTO>) dbData;
        chBarracks = (Map<Integer, BarrackDTO>) chData;
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> barracksOrders = new ArrayList<OrderDTO>();

        //find all rename barrack orders
        for (final BarrackDTO dbBarrack : dbBarracks.values()) {
            //if the barrack exists in the chBarracks records.
            if (chBarracks.containsKey(dbBarrack.getId())) {
                final String name = chBarracks.get(dbBarrack.getId()).getName();
                if (chBarracks.containsKey(dbBarrack.getId())
                        && (dbBarrack.getName() == null && name != null)
                        || (dbBarrack.getName() != null && name != null
                        && !name.equals(dbBarrack.getName()))) {
                    final List<ClientOrderDTO> orders = relOrders.get(ORDER_REN_BARRACK);
                    if (orders != null) {
                        for (final ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == dbBarrack.getId()) {
                                barracksOrders.add(createOrderRename(dbBarrack, name, order));
                                break;
                            }
                        }
                    }
                }
            }
        }

        return barracksOrders;
    }

    private OrderDTO createOrderRename(final BarrackDTO dbBarrack, final String name, final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_REN_BARRACK, order.getPriority(), 0,
                String.valueOf(dbBarrack.getId()),
                name, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

}
