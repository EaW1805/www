/**
 *
 */
package com.eaw1805.www.controllers.remote.hotspot.navy;


import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tsakygr
 */
public class FleetSaveChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    private transient Map<Integer, FleetDTO> dbFleets, chFleets;

    private transient final Map<Integer, List<ClientOrderDTO>> orderMap;

    /**
     * Default constructor.
     *
     * @param gameId   the game of the order.
     * @param nationId the owner of the order.
     * @param turn     the turn of the order.
     * @param orderMap the orders.
     */
    public FleetSaveChangesProcessor(final int thisScenario, final int gameId,
                                     final int nationId,
                                     final int turn,
                                     final Map<Integer, List<ClientOrderDTO>> orderMap) {
        super(thisScenario, gameId, nationId, turn);
        this.orderMap = orderMap;
    }

    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        // empty
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        dbFleets = (HashMap<Integer, FleetDTO>) dbData;
        chFleets = (HashMap<Integer, FleetDTO>) chData;
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> armiesOrders = new ArrayList<OrderDTO>();
        final List<ClientOrderDTO> repairOrders = orderMap.get(ORDER_R_FLT);
        for (final FleetDTO dbFleet : dbFleets.values()) {
            // Check if the army exists in the new army table
            if (chFleets.containsKey(dbFleet.getFleetId())) {
                // If it does check if the army has the same name
                final String name = chFleets.get(dbFleet.getFleetId()).getName();
                if (!name.equals(dbFleet.getName())) {
                    //If it does not add a new change army name order
                    final List<ClientOrderDTO> orders = orderMap.get(ORDER_REN_FLT);
                    if (orders != null) {
                        for (final ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == dbFleet.getFleetId()) {
                                armiesOrders.add(createOrderRenameF(dbFleet, name, order));
                                break;
                            }
                        }
                    }
                }

            } else {
                final List<ClientOrderDTO> orders = orderMap.get(ORDER_D_FLT);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == dbFleet.getFleetId()) {
                            armiesOrders.add(createOrderDemolishF(dbFleet, order));
                        }
                    }
                }
            }

            if (repairOrders != null) {
                for (final ClientOrderDTO order : repairOrders) {
                    if (order.getIdentifier(0) == dbFleet.getFleetId()) {
                        armiesOrders.add(createOrderRepear(dbFleet, order));
                        break;
                    }
                }
            }
        }

        for (final FleetDTO chFleet : chFleets.values()) {
            // Check if the army exists in the old army table
            if (!dbFleets.containsKey(chFleet.getFleetId())) {
                //If it does not add a new create Fleet Order
                final List<ClientOrderDTO> orders = orderMap.get(ORDER_B_FLT);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == chFleet.getFleetId()) {
                            armiesOrders.add(createOrderBuildF(chFleet, order));
                            break;
                        }
                    }
                }

                if (repairOrders != null) {
                    for (final ClientOrderDTO order : repairOrders) {
                        if (order.getIdentifier(0) == chFleet.getFleetId()) {
                            armiesOrders.add(createOrderRepear(chFleet, order));
                            break;
                        }
                    }
                }
            }
        }

        //save repair orders for allied fleets
        if (repairOrders != null) {
            for (final ClientOrderDTO order : repairOrders) {
                if (order.getIdentifier(3) != getNationId()) {
                    armiesOrders.add(createOrderRepairAF(order));
                }
            }
        }
        return armiesOrders;
    }

    private OrderDTO createOrderRepairAF(final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_R_FLT, order.getPriority(), 0,
                String.valueOf(order.getIdentifier(0)),
                String.valueOf(order.getIdentifier(1)),
                String.valueOf(order.getIdentifier(2)),
                String.valueOf(order.getIdentifier(3)),
                "",
                "", "", "", "", ""
                , order.getComment(), "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderBuildF(final FleetDTO chFleet, final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_B_FLT, order.getPriority(), 0,
                String.valueOf(chFleet.getFleetId()),
                chFleet.getName(),/*chFleet.getCommander().getComc()+*/"",
                String.valueOf(chFleet.getXStart()), String.valueOf(chFleet.getYStart()),
                String.valueOf(chFleet.getRegionId()), "", "", "", "", "", "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderRepear(final FleetDTO dbFleet, final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_R_FLT, order.getPriority(), 0,
                String.valueOf(dbFleet.getId()),
                String.valueOf(order.getIdentifier(1)),
                String.valueOf(dbFleet.getRegionId()),
                String.valueOf(order.getIdentifier(3)),
                "",
                "", "", "", "", ""
                , order.getComment(), "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderDemolishF(final FleetDTO dbFleet, final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_D_FLT, order.getPriority(), 0, String.valueOf(dbFleet.getFleetId()), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderRenameF(final FleetDTO dbFleet, final String name, final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_REN_FLT, order.getPriority(), 0, String.valueOf(dbFleet.getFleetId()), name, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

}
