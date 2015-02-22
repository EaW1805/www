package com.eaw1805.www.controllers.remote.hotspot.army;

import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ArmySaveChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    private Map<Integer, ArmyDTO> dbArmies, chArmies;
    private final Map<Integer, List<ClientOrderDTO>> relOrders;

    /**
     * Default constructor.
     *
     * @param gameId    the game of the order.
     * @param nationId  the owner of the order.
     * @param turn      the turn of the order.
     * @param relOrders the orders affecting the stores.
     */
    public ArmySaveChangesProcessor(final int thisScenario, final int gameId,
                                    final int nationId,
                                    final int turn,
                                    final Map<Integer, List<ClientOrderDTO>> relOrders) {
        super(thisScenario, gameId, nationId, turn);
        this.relOrders = relOrders;
    }

    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        // empty function
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        dbArmies = (Map<Integer, ArmyDTO>) dbData;
        chArmies = (Map<Integer, ArmyDTO>) chData;
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> armiesOrders = new ArrayList<OrderDTO>();
        for (final ArmyDTO dbArmy : dbArmies.values()) {
            // Check if the army exists in the new army table
            if (chArmies.containsKey(dbArmy.getArmyId())) {
                // If it does check if the army has the same name
                final String name = chArmies.get(dbArmy.getArmyId()).getName();
                if (!name.equals(dbArmy.getName())) {
                    //If it does not add a new change army name order
                    final List<ClientOrderDTO> orders = relOrders.get(ORDER_REN_ARMY);
                    if (orders != null) {
                        for (ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == dbArmy.getArmyId()) {
                                final OrderDTO changeArmyName = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_REN_ARMY, order.getPriority(), 0,
                                        String.valueOf(dbArmy.getArmyId()),
                                        name, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                                armiesOrders.add(changeArmyName);
                                break;
                            }
                        }
                    }
                }

            } else {
                final List<ClientOrderDTO> orders = relOrders.get(ORDER_D_ARMY);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == dbArmy.getArmyId()) {
                            final OrderDTO demolishArmy = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_D_ARMY, order.getPriority(), 0,
                                    String.valueOf(dbArmy.getArmyId()), "", "", "", "", "", "", "", "", dbArmy.getName(), "", "", "", "", "", "", "", "");
                            armiesOrders.add(demolishArmy);
                            break;
                        }
                    }
                }
            }
        }

        for (final ArmyDTO chArmy : chArmies.values()) {
            // Check if the army exists in the old army table
            if (!dbArmies.containsKey(chArmy.getArmyId())) {
                //If it does not add a new create Army Order
                final List<ClientOrderDTO> orders = relOrders.get(ORDER_B_ARMY);
                for (final ClientOrderDTO order : orders) {
                    if (order.getIdentifier(0) == chArmy.getArmyId()) {
                        final OrderDTO createArmy = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_B_ARMY, order.getPriority(), 0,
                                String.valueOf(chArmy.getArmyId()),
                                String.valueOf(chArmy.getName()),
                                String.valueOf(chArmy.getCommander().getId()),
                                String.valueOf(chArmy.getXStart()),
                                String.valueOf(chArmy.getYStart()),
                                String.valueOf(chArmy.getRegionId()), "", "", "", "", "", "", "", "", "", "", "", "");
                        armiesOrders.add(createArmy);
                        break;
                    }
                }
            }
        }
        final List<ClientOrderDTO> upgradeOrders = relOrders.get(ORDER_INC_EXP_ARMY);
        if (upgradeOrders != null) {
            for (final ClientOrderDTO order : upgradeOrders) {
                final OrderDTO createArmy = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_INC_EXP_ARMY, order.getPriority(), 0,
                        String.valueOf(order.getIdentifier(0)),
                        String.valueOf(order.getIdentifier(1)),
                        String.valueOf(order.getIdentifier(2)),
                        "",
                        "",
                        "", "", "", "", "", order.getCosts().convertToString(), "", "", "", "", "", "", "");
                armiesOrders.add(createArmy);
            }
        }

        // Also check if the battalion has been upgraded
        // in respect to Head-Count
        //If it is add a new upgrade battalion order
        final List<ClientOrderDTO> upHeadCountOrders = relOrders.get(ORDER_INC_HEADCNT_ARMY);
        if (upHeadCountOrders != null) {
            for (final ClientOrderDTO order : upHeadCountOrders) {
                final OrderDTO upgradeHdCntBrigade = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_INC_HEADCNT_ARMY, order.getPriority(), 0,
                        String.valueOf(order.getIdentifier(0)), String.valueOf(order.getIdentifier(1)),
                        String.valueOf(order.getIdentifier(2)), "", "", "", "", "", "", "", order.getCosts().convertToString(), "", "", "", "", "", "", "");
                armiesOrders.add(upgradeHdCntBrigade);
            }
        }

        return armiesOrders;
    }

}
