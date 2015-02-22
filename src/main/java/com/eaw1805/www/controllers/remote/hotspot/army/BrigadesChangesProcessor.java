package com.eaw1805.www.controllers.remote.hotspot.army;

import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrigadesChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    private transient Map<Integer, BrigadeDTO> dbBrigades = new HashMap<Integer, BrigadeDTO>(), chBrigades = new HashMap<Integer, BrigadeDTO>();
    private transient Map<Integer, List<BrigadeDTO>> newBrigMap = new HashMap<Integer, List<BrigadeDTO>>();
    private transient final Map<Integer, List<ClientOrderDTO>> relOrders;

    /**
     * Default constructor.
     *
     * @param gameId    the game of the order.
     * @param nationId  the owner of the order.
     * @param turn      the turn of the order.
     * @param relOrders the actual orders.
     */
    public BrigadesChangesProcessor(final int thisScenario, final int gameId,
                                    final int nationId,
                                    final int turn,
                                    final Map<Integer, List<ClientOrderDTO>> relOrders) {
        super(thisScenario, gameId, nationId, turn);
        this.relOrders = relOrders;
    }

    public void addData(Collection<?> dbData, Collection<?> chData) {
        // do nothing
    }

    @SuppressWarnings({"restriction", "unchecked"})
    public void addData(Map<?, ?> dbData, Map<?, ?> chData) {
        if (dbData == null) {
            newBrigMap = (Map<Integer, List<BrigadeDTO>>) chData;

        } else {
            dbBrigades = (HashMap<Integer, BrigadeDTO>) dbData;
            chBrigades = (HashMap<Integer, BrigadeDTO>) chData;
        }
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> brigadesOrders = new ArrayList<OrderDTO>();
        for (BrigadeDTO dbBrigade : dbBrigades.values()) {
            // If it does check if the brigade has the same name
            final String name = chBrigades.get(dbBrigade.getBrigadeId()).getName();

            // Check if the brigade exists in the new brigade table
            if (chBrigades.containsKey(dbBrigade.getBrigadeId()) && !name.equals(dbBrigade.getName())) {
                //If it does not add a new change corp name order
                final List<ClientOrderDTO> orders = relOrders.get(ORDER_REN_BRIG);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == dbBrigade.getBrigadeId()) {
                            final OrderDTO changeCorpName = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_REN_BRIG, order.getPriority(), 0,
                                    String.valueOf(dbBrigade.getBrigadeId()), name, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                            brigadesOrders.add(changeCorpName);
                            break;
                        }
                    }
                }

            } else {
                final List<ClientOrderDTO> orders = relOrders.get(ORDER_D_BRIG);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == dbBrigade.getBrigadeId()) {
                            final OrderDTO demolishBrigade = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_D_BRIG, order.getPriority(), 0,
                                    String.valueOf(dbBrigade.getBrigadeId()), "", "", "", "", "", "", "", "", dbBrigade.getName(), "", "", "", "", "", "", "", "");
                            brigadesOrders.add(demolishBrigade);
                            break;
                        }
                    }
                }
            }
        }

        for (final BrigadeDTO chBrigade : chBrigades.values()) {
            final List<ClientOrderDTO> chngCorpOrders = relOrders.get(ORDER_ADDTO_CORP);
            if (chngCorpOrders != null) {
                for (final ClientOrderDTO order : chngCorpOrders) {
                    if (order.getIdentifier(0) == chBrigade.getBrigadeId()) {
                        final OrderDTO changeBrigadeCorp = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_ADDTO_CORP, order.getPriority(), 0,
                                String.valueOf(chBrigade.getBrigadeId()),
                                String.valueOf(order.getIdentifier(1)), "", "", "", "", "", "", "", chBrigade.getName(), "", "", "", "", "", "", "", "");
                        brigadesOrders.add(changeBrigadeCorp);
                        break;
                    }
                }
            }

            // If it does check if the brigade has been upgraded
            final boolean isUpgraded = chBrigades.get(chBrigade.getBrigadeId()).isUpgraded();
            final boolean isUpgradedToElite = chBrigades.get(chBrigade.getBrigadeId()).isUpgradedToElite();
            if (isUpgraded) {
                final List<ClientOrderDTO> orders = relOrders.get(ORDER_INC_EXP);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == chBrigade.getBrigadeId() && order.getIdentifier(1) == 0) {
                            final OrderDTO upgradeBrigade = new OrderDTO(
                                    getGameId(), getNationId(), getTurn(),
                                    ORDER_INC_EXP, order.getPriority(), 0, String.valueOf(chBrigade
                                    .getBrigadeId()), "0", String.valueOf(order.getIdentifier(2)), "", "", "",
                                    "", "", "", "", order.getCosts().convertToString(), "", "", "", "", "", "", "");
                            brigadesOrders.add(upgradeBrigade);
                            break;
                        }
                    }
                }

            } else if (isUpgradedToElite) {
                final List<ClientOrderDTO> orders = relOrders.get(ORDER_INC_EXP);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == chBrigade.getBrigadeId() && order.getIdentifier(1) == 1) {
                            final OrderDTO upgradeBrigade = new OrderDTO(
                                    getGameId(), getNationId(), getTurn(),
                                    ORDER_INC_EXP, order.getPriority(), 0, String.valueOf(chBrigade
                                    .getBrigadeId()), "1", String.valueOf(order.getIdentifier(2)), "", "", "",
                                    "", "", "", "", order.getCosts().convertToString(), "", "", "", "", "", "", "");
                            brigadesOrders.add(upgradeBrigade);
                            break;
                        }
                    }
                }
            }

            // Also check if the battalion has been upgraded
            // in respect to Head-Count
            final boolean isUpHeadcount = chBrigades.get(chBrigade.getBrigadeId()).IsUpHeadcount();
            if (isUpHeadcount) {
                //If it is add a new upgrade battalion order
                final List<ClientOrderDTO> orders = relOrders.get(ORDER_INC_HEADCNT);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == chBrigade.getBrigadeId()) {
                            final OrderDTO upgradeHdCntBrigade = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_INC_HEADCNT, order.getPriority(), 0,
                                    String.valueOf(chBrigade.getBrigadeId()), String.valueOf(chBrigade.getUpHeadcount()),
                                    String.valueOf(order.getIdentifier(2)), "", "", "", "", "", "", "", order.getCosts().convertToString(), "", "", "", "", "", "", "");
                            brigadesOrders.add(upgradeHdCntBrigade);
                            break;
                        }
                    }
                }
            }
        }

        // Add commands for new brigades
        for (final int sectorId : newBrigMap.keySet()) {
            for (final BrigadeDTO newBrig : newBrigMap.get(sectorId)) {
                final int[] typeIds = new int[7];
                for (int index = 0; index < 7; index++) {
                    try {
                        typeIds[index] = newBrig.getBattalions().get(index).getEmpireArmyType().getIntId();

                    } catch (Exception Ex) {
                        typeIds[index] = 0;
                    }
                }

                final List<ClientOrderDTO> orders = relOrders.get(ORDER_B_BATT);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == sectorId && order.getIdentifier(1) == newBrig.getBrigadeId()) {
                            final OrderDTO createBrigade = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_B_BATT, order.getPriority(), 0,
                                    String.valueOf(sectorId),
                                    String.valueOf(typeIds[0]),
                                    String.valueOf(typeIds[1]),
                                    String.valueOf(typeIds[2]),
                                    String.valueOf(typeIds[3]),
                                    String.valueOf(typeIds[4]),
                                    String.valueOf(typeIds[5]),
                                    String.valueOf(typeIds[6]),
                                    newBrig.getName(), String.valueOf(newBrig.getBrigadeId()), "", "", "", "", "", "", "", "");
                            brigadesOrders.add(createBrigade);
                            break;
                        }
                    }
                }
            }
        }

        return brigadesOrders;
    }

}
