package com.eaw1805.www.controllers.remote.hotspot.army;

import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CorpsSaveChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    private transient Map<Integer, CorpDTO> dbCorps = new HashMap<Integer, CorpDTO>(), chCorps = new HashMap<Integer, CorpDTO>();
    private transient final Map<Integer, List<ClientOrderDTO>> relOrders;

    /**
     * Default constructor.
     *
     * @param gameId    the game of the order.
     * @param nationId  the owner of the order.
     * @param turn      the turn of the order.
     * @param relOrders the orders.
     */
    public CorpsSaveChangesProcessor(final int thisScenario, final int gameId,
                                     final int nationId,
                                     final int turn,
                                     final Map<Integer, List<ClientOrderDTO>> relOrders) {
        super(thisScenario, gameId, nationId, turn);
        this.relOrders = relOrders;
    }


    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        // do nothing
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        dbCorps = (HashMap<Integer, CorpDTO>) dbData;
        chCorps = (HashMap<Integer, CorpDTO>) chData;
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> corpsOrders = new ArrayList<OrderDTO>();
        for (CorpDTO dbCorp : dbCorps.values()) {
            // Check if the corp exists in the new corp table
            if (chCorps.containsKey(dbCorp.getCorpId())) {
                // If it does check if the corp has the same name
                final String name = chCorps.get(dbCorp.getCorpId()).getName();
                if (!name.equals(dbCorp.getName())) {
                    //If it does not add a new change corp name order
                    final List<ClientOrderDTO> orders = relOrders.get(ORDER_REN_CORP);
                    if (orders != null) {
                        for (ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == dbCorp.getCorpId()) {
                                final OrderDTO changeArmyName = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_REN_CORP, order.getPriority(), 0,
                                        String.valueOf(dbCorp.getCorpId()), name, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                                corpsOrders.add(changeArmyName);
                                break;
                            }
                        }
                    }
                }

            } else {
                final List<ClientOrderDTO> orders = relOrders.get(ORDER_D_CORP);
                if (orders != null) {
                    for (ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == dbCorp.getCorpId()) {
                            final OrderDTO demolishCorp = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_D_CORP, order.getPriority(), 0,
                                    String.valueOf(dbCorp.getCorpId()), String.valueOf(dbCorp.getArmyId()), "", "", "", "", "", "", "", dbCorp.getName(), "", "", "", "", "", "", "", "");
                            corpsOrders.add(demolishCorp);
                            break;
                        }
                    }
                }
            }
        }

        for (CorpDTO chCorp : chCorps.values()) {
            // Check if the corp exists in the old army table
            if (dbCorps.containsKey(chCorp.getCorpId())) {
                //If it does check if the Corp has changed army
//                if (chCorp.getArmyId() != dbCorps.get(chCorp.getCorpId()).getArmyId()) {
//                    final List<ClientOrderDTO> orders = relOrders.get(ORDER_ADDTO_ARMY);
//                    if (orders != null) {
//                        for (ClientOrderDTO order : orders) {
//                            if (order.getIdentifier(0) == chCorp.getCorpId()) {
//                                final OrderDTO changeCorpArmy = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_ADDTO_ARMY, order.getPriority(), 0,
//                                        String.valueOf(chCorp.getCorpId()),
//                                        String.valueOf(chCorp.getArmyId()), "", "", "", "", "", "", "", chCorp.getName(), "", "", "", "", "", "", "", "");
//                                corpsOrders.add(changeCorpArmy);
//                                break;
//                            }
//                        }
//                    }
//                }

            } else {
                //If it does not add a new create Army Order
                String commanderId = "";
                if (chCorp.getCommander() != null) {
                    commanderId = String.valueOf(chCorp.getCommander().getId());
                }

                List<ClientOrderDTO> orders = relOrders.get(ORDER_B_CORP);
                if (orders != null) {
                    for (ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == chCorp.getCorpId()) {
                            final OrderDTO createCorp = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_B_CORP, order.getPriority(), 0,
                                    String.valueOf(chCorp.getCorpId()),
                                    String.valueOf(chCorp.getArmyId()),
                                    chCorp.getName(),
                                    String.valueOf(chCorp.getXStart()),
                                    String.valueOf(chCorp.getYStart()),
                                    String.valueOf(chCorp.getRegionId()),
                                    String.valueOf(chCorp.getNationId()), commanderId, "", "", "", "", "", "", "", "", "", "");
                            corpsOrders.add(createCorp);
                            break;
                        }
                    }
                }

//                orders = relOrders.get(ORDER_ADDTO_ARMY);
//                if (orders != null) {
//                    for (ClientOrderDTO order : orders) {
//                        if (order.getIdentifier(0) == chCorp.getCorpId()) {
//                            final OrderDTO changeCorpArmy = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_ADDTO_ARMY, order.getPriority(), 0,
//                                    String.valueOf(chCorp.getCorpId()),
//                                    String.valueOf(chCorp.getArmyId()), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
//                            corpsOrders.add(changeCorpArmy);
//                            break;
//                        }
//                    }
//                }
            }
        }
        //just save all change corps army orders
        final List<ClientOrderDTO> addToArmyOrders;
        addToArmyOrders = relOrders.get(ORDER_ADDTO_ARMY);
        if (addToArmyOrders != null) {
            for (ClientOrderDTO order : addToArmyOrders) {
                final OrderDTO changeCorpArmy = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_ADDTO_ARMY, order.getPriority(), 0,
                        String.valueOf(order.getIdentifier(0)),
                        String.valueOf(order.getIdentifier(1)), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                corpsOrders.add(changeCorpArmy);
            }
        }

        //just save all increase experience/upgrade orders
        final List<ClientOrderDTO> upgradeOrders = relOrders.get(ORDER_INC_EXP_CORPS);
        if (upgradeOrders != null) {
            for (ClientOrderDTO order : upgradeOrders) {
                final OrderDTO trainCorp = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_INC_EXP_CORPS, order.getPriority(), 0,
                        String.valueOf(order.getIdentifier(0)),
                        String.valueOf(order.getIdentifier(1)),
                        String.valueOf(order.getIdentifier(2)),
                        "",
                        "",
                        "", "", "", "", "", order.getCosts().convertToString(), "", "", "", "", "", "", "");
                corpsOrders.add(trainCorp);
            }
        }

        // Also check if the battalion has been upgraded
        // in respect to Head-Count
        // If it is add a new upgrade battalion order
        final List<ClientOrderDTO> upHeadCountOrders = relOrders.get(ORDER_INC_HEADCNT_CORPS);
        if (upHeadCountOrders != null) {
            for (ClientOrderDTO order : upHeadCountOrders) {
                final OrderDTO upgradeHdCntBrigade = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_INC_HEADCNT_CORPS, order.getPriority(), 0,
                        String.valueOf(order.getIdentifier(0)), String.valueOf(order.getIdentifier(1)),
                        String.valueOf(order.getIdentifier(2)), "", "", "", "", "", "", "", order.getCosts().convertToString(), "", "", "", "", "", "", "");
                corpsOrders.add(upgradeHdCntBrigade);
            }
        }

        return corpsOrders;
    }

}
