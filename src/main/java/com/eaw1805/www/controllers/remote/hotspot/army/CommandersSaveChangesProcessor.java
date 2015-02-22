package com.eaw1805.www.controllers.remote.hotspot.army;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CommandersSaveChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, ArmyConstants, GoodConstants {

    private transient List<CommanderDTO> dbCommanders, chCommanders;
    private transient final Map<Integer, List<ClientOrderDTO>> orderMap;

    public CommandersSaveChangesProcessor(final int thisScenario, final int thisGame,
                                          final int thisNation,
                                          final int thisTurn,
                                          final Map<Integer, List<ClientOrderDTO>> orderMap) {
        super(thisScenario, thisGame, thisNation, thisTurn);
        this.orderMap = orderMap;
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        dbCommanders = (List<CommanderDTO>) dbData;
        chCommanders = (List<CommanderDTO>) chData;

    }

    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        // do nothing
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> commanderOrders = new ArrayList<OrderDTO>();
        for (final CommanderDTO dbComm : dbCommanders) {
            CommanderDTO chComm = null;
            for (final CommanderDTO chCom : chCommanders) {
                if (dbComm.getId() == chCom.getId()) {
                    chComm = chCom;
                    break;
                }
            }

            if (chComm != null) {
                final String name = chComm.getName();
                if (!name.equals(dbComm.getName())) {
                    final List<ClientOrderDTO> orders = orderMap.get(ORDER_REN_COMM);
                    if (orders != null) {
                        for (final ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == dbComm.getId()) {
                                final OrderDTO changeCommanderName = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_REN_COMM, order.getPriority(), 0, String.valueOf(dbComm.getId()), name, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                                commanderOrders.add(changeCommanderName);
                                break;
                            }
                        }
                    }
                }
            }

            if (chComm != null && chComm.getStartInPool() != chComm.getInPool()) {
                if (chComm.getInPool()) {
                    final List<ClientOrderDTO> orders = orderMap.get(ORDER_DISS_COM);
                    if (orders != null) {
                        for (final ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == dbComm.getId()) {
                                final OrderDTO orderLeave = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_DISS_COM, order.getPriority(), 0,
                                        String.valueOf(dbComm.getId()), String.valueOf(order.getIdentifier(1)), "",
                                        "", "", "", "", "", "",
                                        String.valueOf(order.getCosts().getNumericCost(GOOD_AP)), "", "", "", "", "", "", "", "");
                                commanderOrders.add(orderLeave);
                                break;
                            }
                        }
                    }

                } else {
                    final List<ClientOrderDTO> orders = orderMap.get(ORDER_HIRE_COM);
                    if (orders != null) {
                        for (final ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == dbComm.getId()) {
                                final OrderDTO orderLeave = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_HIRE_COM, order.getPriority(), 0,
                                        String.valueOf(dbComm.getId()), String.valueOf(order.getIdentifier(1)), "",
                                        "", "", "", "", "", "",
                                        String.valueOf(order.getCosts().getNumericCost(GOOD_AP)), "", "", "", "", "", "", "", "");
                                commanderOrders.add(orderLeave);
                                break;
                            }
                        }
                    }
                }
            }

            if (dbComm.getArmy() != chComm.getArmy()) {
                if (dbComm.getArmy() == 0) {
                    if (dbComm.getCorp() != 0) {
                        final List<ClientOrderDTO> orders = orderMap.get(ORDER_LEAVE_COM);
                        if (orders != null) {
                            for (final ClientOrderDTO order : orders) {
                                if (order.getIdentifier(0) == dbComm.getId()) {
                                    final OrderDTO orderLeave = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_LEAVE_COM, order.getPriority(), 0,
                                            String.valueOf(CORPS), String.valueOf(dbComm.getId()), String.valueOf(dbComm.getCorp()),
                                            "", "", "", "", "", "",
                                            "", "", "", "", "", "", "", "", "");
                                    commanderOrders.add(orderLeave);
                                    break;
                                }
                            }
                        }
                    }

                } else {
                    final List<ClientOrderDTO> orders = orderMap.get(ORDER_LEAVE_COM);
                    if (orders != null) {
                        for (final ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == dbComm.getId()) {
                                final OrderDTO orderLeave = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_LEAVE_COM, order.getPriority(), 0,
                                        String.valueOf(ARMY), String.valueOf(dbComm.getId()), String.valueOf(dbComm.getArmy()),
                                        "", "", "", "", "", "",
                                        "", "", "", "", "", "", "", "", "");
                                commanderOrders.add(orderLeave);
                                break;
                            }
                        }
                    }
                }

                final List<ClientOrderDTO> orders = orderMap.get(ORDER_ARMY_COM);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == chComm.getId() && order.getIdentifier(1) == chComm.getArmy()) {
                            final OrderDTO orderJoinArmy = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_ARMY_COM, order.getPriority(), 0,
                                    String.valueOf(dbComm.getId()), String.valueOf(chComm.getArmy()),
                                    "", "", "", "", "", "", "",
                                    "", "", "", "", "", "", "", "", "");
                            commanderOrders.add(orderJoinArmy);
                            break;
                        }
                    }
                }

            }

            if (dbComm.getCorp() != chComm.getCorp()) {
                if (dbComm.getArmy() == 0) {
                    if (dbComm.getCorp() != 0) {
                        final List<ClientOrderDTO> orders = orderMap.get(ORDER_LEAVE_COM);
                        if (orders != null) {
                            for (final ClientOrderDTO order : orders) {
                                if (order.getIdentifier(0) == chComm.getId()) {
                                    final OrderDTO orderLeave = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_LEAVE_COM, order.getPriority(), 0,
                                            String.valueOf(CORPS), String.valueOf(dbComm.getId()), String.valueOf(dbComm.getCorp()),
                                            "", "", "", "", "", "",
                                            "", "", "", "", "", "", "", "", "");
                                    commanderOrders.add(orderLeave);
                                }
                            }
                        }
                    }

                } else {
                    final List<ClientOrderDTO> orders = orderMap.get(ORDER_LEAVE_COM);
                    if (orders != null) {
                        for (final ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == chComm.getId()) {
                                final OrderDTO orderLeave = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_LEAVE_COM, order.getPriority(), 0,
                                        String.valueOf(ARMY), String.valueOf(dbComm.getId()), String.valueOf(dbComm.getArmy()),
                                        "", "", "", "", "", "",
                                        "", "", "", "", "", "", "", "", "");
                                commanderOrders.add(orderLeave);
                                break;
                            }
                        }
                    }
                }

                final List<ClientOrderDTO> orders = orderMap.get(ORDER_CORP_COM);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == chComm.getId() && order.getIdentifier(1) == chComm.getCorp()) {
                            final OrderDTO orderJoinCorp = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_CORP_COM, order.getPriority(), 0,
                                    String.valueOf(dbComm.getId()), String.valueOf(chComm.getCorp()),
                                    "", "", "", "", "", "", "",
                                    "", "", "", "", "", "", "", "", "");
                            commanderOrders.add(orderJoinCorp);
                            break;
                        }
                    }
                }
            }
        }

        return commanderOrders;
    }

}
