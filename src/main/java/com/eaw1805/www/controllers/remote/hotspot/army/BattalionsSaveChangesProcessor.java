package com.eaw1805.www.controllers.remote.hotspot.army;

import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattalionsSaveChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    private transient final Map<Integer, BattalionDTO> dbBattalions = new HashMap<Integer, BattalionDTO>(),
            chBattalions = new HashMap<Integer, BattalionDTO>();

    private transient final Map<Integer, List<ClientOrderDTO>> relOrders;

    /**
     * Default constructor.
     *
     * @param gameId    the game of the order.
     * @param nationId  the owner of the order.
     * @param turn      the turn of the order.
     * @param relOrders the orders.
     */
    public BattalionsSaveChangesProcessor(final int thisScenario, final int gameId,
                                          final int nationId,
                                          final int turn,
                                          final Map<Integer, List<ClientOrderDTO>> relOrders) {
        super(thisScenario, gameId, nationId, turn);
        this.relOrders = relOrders;
    }

    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        // do nothing

    }

    @SuppressWarnings({"restriction", "unchecked"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        if (dbData != null) {
            dbBattalions.putAll((HashMap<Integer, BattalionDTO>) dbData);
            chBattalions.putAll((HashMap<Integer, BattalionDTO>) chData);
        }
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> battallionsOrders = new ArrayList<OrderDTO>();
        for (final BattalionDTO dbBattalion : dbBattalions.values()) {
            // Check if the battalion exists in the new battallion table
            if (!chBattalions.containsKey(dbBattalion.getId())) {
                // if it does not exist demolish the battalion
                final List<ClientOrderDTO> orders = relOrders.get(ORDER_D_BATT);
                if (orders != null) {
                    for (ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == dbBattalion.getId()) {
                            final OrderDTO demolishBattalion = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_D_BATT, order.getPriority(), 0,
                                    String.valueOf(dbBattalion.getId()), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                            battallionsOrders.add(demolishBattalion);
                            break;
                        }
                    }
                }
            }
        }

        for (final BattalionDTO chBattalion : chBattalions.values()) {
            // Check if the battalion exists in the old army table
            if (dbBattalions.containsKey(chBattalion.getId())) {
                //If it does check if the Battalion has changed Brigade
                if (chBattalion.getBrigadeId() != dbBattalions.get(chBattalion.getId()).getBrigadeId()) {
                    final List<ClientOrderDTO> orders = relOrders.get(ORDER_ADDTO_BRIGADE);
                    if (orders != null) {
                        for (final ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == chBattalion.getId()) {
                                final OrderDTO changeBattalionBrigade = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_ADDTO_BRIGADE, order.getPriority(), 0,
                                        String.valueOf(chBattalion.getId()),
                                        String.valueOf(chBattalion.getBrigadeId()), String.valueOf(chBattalion.getOrder()), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                                battallionsOrders.add(changeBattalionBrigade);
                                break;
                            }
                        }
                    }
                }

                if (chBattalion.getMergedWith() != 0) {
                    final List<ClientOrderDTO> orders = relOrders.get(ORDER_MRG_BATT);
                    if (orders != null) {
                        for (final ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == chBattalion.getId()) {
                                final OrderDTO mergeBattalions = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_MRG_BATT, order.getPriority(), 0,
                                        String.valueOf(chBattalion.getId()),
                                        String.valueOf(chBattalion.getMergedWith()), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                                battallionsOrders.add(mergeBattalions);
                                break;
                            }
                        }
                    }
                }
            }
        }

        final List<ClientOrderDTO> orders = relOrders.get(ORDER_ADD_BATT);
        if (orders != null) {
            for (final ClientOrderDTO order : orders) {
                final OrderDTO addBattalionToBrigade = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_ADD_BATT, order.getPriority(), 0,
                        String.valueOf(order.getIdentifier(0)),
                        String.valueOf(order.getIdentifier(1)), String.valueOf(order.getIdentifier(2)), "", "", "", "", "", "", String.valueOf(order.getRegionId()), "", "", "", "", "", "", "", "");
                battallionsOrders.add(addBattalionToBrigade);
            }
        }
        return battallionsOrders;
    }

}
