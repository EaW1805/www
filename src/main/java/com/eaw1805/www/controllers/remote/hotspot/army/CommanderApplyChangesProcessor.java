package com.eaw1805.www.controllers.remote.hotspot.army;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;
import com.eaw1805.www.shared.orders.army.ChangeCommanderNameOrder;
import com.eaw1805.www.shared.orders.army.CommanderDismissOrder;
import com.eaw1805.www.shared.orders.army.CommanderHireOrder;
import com.eaw1805.www.shared.orders.army.CommanderJoinArmyOrder;
import com.eaw1805.www.shared.orders.army.CommanderJoinCorpOrder;
import com.eaw1805.www.shared.orders.army.CommanderLeaveFederationOrder;
import com.eaw1805.www.shared.orders.army.CommanderUndoDissOrder;
import com.eaw1805.www.shared.stores.units.TransportStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommanderApplyChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, ArmyConstants {

    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();

    /**
     * This nation's commanders
     */
    private transient final Map<Integer, CommanderDTO> commandersMap = new HashMap<Integer, CommanderDTO>();

    /**
     * The army as we have made it so far mapped to their IDs.
     */
    private transient Map<Integer, ArmyDTO> armiesMap = new HashMap<Integer, ArmyDTO>();

    private transient Map<Integer, ArmyDTO> deletedArmiesMap = new HashMap<Integer, ArmyDTO>();
    private transient Map<Integer, CorpDTO> deletedCorpsMap = new HashMap<Integer, CorpDTO>();

    /**
     * Default constructor.
     *
     * @param thisGame   the game of the order.
     * @param thisNation the owner of the order.
     * @param thisTurn   the turn of the order.
     */
    public CommanderApplyChangesProcessor(final int thisScenario, final int thisGame, final int thisNation, final int thisTurn) {
        super(thisScenario, thisGame, thisNation, thisTurn);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chOrders) {
        for (final CommanderDTO commander : (List<CommanderDTO>) dbData) {
            commandersMap.put(commander.getId(), commander);
        }
        orders = (List<OrderDTO>) chOrders;
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        armiesMap = (Map<Integer, ArmyDTO>) dbData;
    }

    public void addExtraData(final Map<Integer, ArmyDTO> deletedArmies, final Map<Integer, CorpDTO> deletedCorps) {
        deletedArmiesMap.putAll(deletedArmies);
        deletedCorpsMap.putAll(deletedCorps);
    }

    public List<CommanderDTO> processChanges() {
        for (OrderDTO order : orders) {
            switch (order.getType()) {
                case ORDER_REN_COMM:
                    execOrderRename(order);
                    break;

                case ORDER_HIRE_COM:
                    execOrderHire(order);
                    break;

                case ORDER_ARMY_COM:
                    execOrderArmy(order);
                    break;

                case ORDER_CORP_COM:
                    execOrderCorps(order);
                    break;

                case ORDER_LEAVE_COM:
                    execOrderCorpsL(order);
                    break;

                case ORDER_DISS_COM:
                    execOrderDiss(order);
                    break;

                case ORDER_LOAD_TROOPSF:
                case ORDER_LOAD_TROOPSS:
                    execOrderLoad(order);
                    break;

                case ORDER_UNLOAD_TROOPSF:
                case ORDER_UNLOAD_TROOPSS:
                    execOrderUnload(order);
                    break;

                case ORDER_M_COMM:
                case ORDER_M_UNIT:
                    execOrderMove(order);
                    break;

                default:
                    // do nothing
            }
        }

        return new ArrayList<CommanderDTO>(commandersMap.values());
    }

    private void execOrderMove(final OrderDTO order) {
        final int commId = Integer.parseInt(order.getParameter2());
        final int unitType = Integer.parseInt(order.getParameter1());
        if (order.getType() == ORDER_M_COMM || unitType == COMMANDER) {
            final PositionDTO pos = getLastPositionByString(order.getParameter3());
            moveUnitByTypeToNewPosition(unitType, commId, pos);
        }
    }

    private void execOrderUnload(final OrderDTO order) {
        final int commId = Integer.parseInt(order.getParameter4());
        final int type1 = Integer.parseInt(order.getParameter3());
        if (type1 == COMMANDER) {
            final CommanderDTO commander = getCommanderById(commId);
            commander.setLoaded(false);
            final int[] xy = TransportStore.getCoordsByDirection(Integer.parseInt(order.getParameter5()),
                    Integer.parseInt(order.getTemp1()),
                    Integer.parseInt(order.getTemp2()));
            commander.setX(xy[0]);
            commander.setY(xy[1]);
            commander.setXStart(xy[0]);
            commander.setYStart(xy[1]);
        }
    }

    private void execOrderLoad(final OrderDTO order) {
        final int commId = Integer.parseInt(order.getParameter4());
        final int type1 = Integer.parseInt(order.getParameter3());
        if (type1 == COMMANDER) {
            CommanderDTO comm = getCommanderById(commId);
            comm.setLoaded(true);
            final CommanderLeaveFederationOrder cjaOrder = new CommanderLeaveFederationOrder(armiesMap, comm);
            cjaOrder.execute(commId);
        }
    }

    private void execOrderDiss(final OrderDTO order) {
        final int commId = Integer.parseInt(order.getParameter1());
        final CommanderDTO commander = getCommanderById(commId);
        final int armyId = commander.getArmy();
        final int corpId = commander.getCorp();
        if ((armyId == 0 && corpId == 0 && !commander.getInPool()) || commander.isCaptured()) {
            final CommanderDismissOrder cdOrder = new CommanderDismissOrder(commander);
            cdOrder.execute(commId);
        }
    }

    private void execOrderCorpsL(final OrderDTO order) {
        final int commId = Integer.parseInt(order.getParameter2());
        final CommanderDTO commander = getCommanderById(commId);

        if (commander != null) {
            //first check for deleted units
            if (commander.getArmy() > 0 && deletedArmiesMap.containsKey(commander.getArmy())) {
                if (deletedArmiesMap.get(commander.getArmy()).getCommander() != null
                        && deletedArmiesMap.get(commander.getArmy()).getCommander().getId() == commId) {
                    deletedArmiesMap.get(commander.getArmy()).setCommander(new CommanderDTO());
                }
                commander.setArmy(0);
            }
            if (commander.getCorp() > 0 && deletedCorpsMap.containsKey(commander.getCorp())) {
                if (deletedCorpsMap.get(commander.getCorp()).getCommander() != null
                        && deletedCorpsMap.get(commander.getCorp()).getCommander().getId() == commId) {
                    deletedCorpsMap.get(commander.getCorp()).setCommander(new CommanderDTO());
                }
                commander.setCorp(0);
            }
            //then be sure for active units
            final CommanderLeaveFederationOrder cjaOrder = new CommanderLeaveFederationOrder(armiesMap, commander);
            cjaOrder.execute(commId);
        }
    }

    private void execOrderCorps(final OrderDTO order) {
        final int commId = Integer.parseInt(order.getParameter1());
        final CommanderDTO commander = getCommanderById(commId);
        //first check if the army or corps the commander is already assigned has been deleted
        if (commander.getArmy() > 0 && deletedArmiesMap.containsKey(commander.getArmy())) {
            deletedArmiesMap.get(commander.getArmy()).setCommander(new CommanderDTO());
            commander.setArmy(0);
        }
        if (commander.getCorp() > 0 && deletedCorpsMap.containsKey(commander.getCorp())) {
            deletedCorpsMap.get(commander.getCorp()).setCommander(new CommanderDTO());
            commander.setCorp(0);
        }
        if ((!commander.getInPool())
                || (commander.getInPool() && hireCommander(commId, commander.getRegionId()))) {
            final CommanderJoinCorpOrder cjaOrder = new CommanderJoinCorpOrder(armiesMap, commander);
            cjaOrder.execute(Integer.parseInt(order.getParameter2()));
        }
    }

    private void execOrderArmy(final OrderDTO order) {
        final int commId = Integer.parseInt(order.getParameter1());
        final CommanderDTO commander = getCommanderById(commId);
        //first check if the army or corps the commander is already assigned has been deleted
        if (commander.getArmy() > 0 && deletedArmiesMap.containsKey(commander.getArmy())) {
            deletedArmiesMap.get(commander.getArmy()).setCommander(new CommanderDTO());
            commander.setArmy(0);
        }
        if (commander.getCorp() > 0 && deletedCorpsMap.containsKey(commander.getCorp())) {
            deletedCorpsMap.get(commander.getCorp()).setCommander(new CommanderDTO());
            commander.setCorp(0);
        }
        if ((!commander.getInPool())
                || (commander.getInPool() && hireCommander(commId, commander.getRegionId()))) {
            final CommanderJoinArmyOrder cjaOrder = new CommanderJoinArmyOrder(armiesMap, commander);
            cjaOrder.execute(Integer.parseInt(order.getParameter2()));
        }
    }

    private void execOrderHire(final OrderDTO order) {
        final int commId = Integer.parseInt(order.getParameter1());
        final CommanderDTO commander = getCommanderById(commId);
        if (commander.getInPool()) {
            final CommanderHireOrder cdOrder = new CommanderHireOrder(commander, Integer.parseInt(order.getParameter2()));
            cdOrder.execute(commId);
        }
    }

    private void execOrderRename(final OrderDTO order) {
        final int commId = Integer.parseInt(order.getParameter1());
        if (commandersMap.containsKey(commId)) {
            final ChangeCommanderNameOrder ccnName = new ChangeCommanderNameOrder(commandersMap, order.getParameter2());
            ccnName.execute(commId);
        }
    }

    /**
     * Returns the commander with id
     *
     * @param id the id of the commander
     * @return the commander
     */
    private CommanderDTO getCommanderById(final int id) {
        if (commandersMap.containsKey(id)) {
            return commandersMap.get(id);

        } else {
            return null;
        }
    }

    /**
     * Moves a unit to the specified position
     *
     * @param type the type of the unit
     * @param id   the id of the unit
     * @param pos  the specified position
     */
    private void moveUnitByTypeToNewPosition(final int type, final int id, final PositionDTO pos) {
        PositionDTO oldPos = null;
        if (type == COMMANDER) {
            oldPos = commandersMap.get(id);
        }

        if (oldPos != null) {
            oldPos.setX(pos.getX());
            oldPos.setY(pos.getY());
        }
    }

    /**
     * Method that returns the last movement sector depending
     * to the movement string saved in the database
     *
     * @param movementString the target movement string
     * @return the position dto object
     */
    private PositionDTO getLastPositionByString(final String movementString) {
        final PositionDTO lastPosition = new PositionDTO();
        final String[] moves = movementString.split("!");
        final String lastMove = moves[moves.length - 1];
        final String[] sectors = lastMove.split("-");
        final String lastSector = sectors[sectors.length - 1];
        final String[] coords = lastSector.split(":");

        lastPosition.setX(Integer.parseInt(coords[0]));
        lastPosition.setY(Integer.parseInt(coords[1]));

        return lastPosition;
    }

    /**
     * Method that hires a target commander to
     * the pool
     *
     * @param commanderId the id of the target commander
     * @param regionId    the id of the region where the
     *                    commander will be hired
     * @return true if the commander was properly hired
     */
    private boolean hireCommander(final int commanderId, final int regionId) {
        final boolean success;
        final CommanderDTO commander = getCommanderById(commanderId);
//        undoDismissCommander(commanderId);
        if (commander.getInPool()) {
            final CommanderHireOrder cdOrder = new CommanderHireOrder(commander, regionId);
            success = (cdOrder.execute(commanderId) == 1);

        } else {
            success = false;
        }
        return success;
    }

    /**
     * Method that will undo the dismissal of the commander
     * and return him to the pool
     *
     * @param commanderId the id of the target commander.
     * @return the result of the operation.
     */
    public boolean undoDismissCommander(final int commanderId) {
        final boolean success;
        final CommanderDTO commander = getCommanderById(commanderId);
        if (commander.getInPool()) {
            final CommanderUndoDissOrder cdOrder = new CommanderUndoDissOrder(commander);
            success = (cdOrder.execute(commanderId) == 1);

        } else {
            success = false;
        }
        return success;

    }

}
