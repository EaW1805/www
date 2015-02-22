package empire.webapp.shared.stores.units;

import empire.data.constants.ArmyConstants;
import empire.data.constants.OrderConstants;
import empire.data.dto.common.PositionDTO;
import empire.data.dto.common.SectorDTO;
import empire.data.dto.web.ClientOrderDTO;
import empire.data.dto.web.OrderCostDTO;
import empire.data.dto.web.army.ArmyDTO;
import empire.data.dto.web.army.CommanderDTO;
import empire.data.dto.web.army.CorpDTO;
import empire.webapp.client.events.loading.LoadEventManager;
import empire.webapp.client.events.units.UnitEventManager;
import empire.webapp.client.views.popups.OrdersViewerPopup;
import empire.webapp.client.widgets.ErrorPopup;
import empire.webapp.shared.orders.army.ChangeCommanderNameOrder;
import empire.webapp.shared.orders.army.CommanderDismissOrder;
import empire.webapp.shared.orders.army.CommanderHireOrder;
import empire.webapp.shared.orders.army.CommanderJoinArmyOrder;
import empire.webapp.shared.orders.army.CommanderJoinCorpOrder;
import empire.webapp.shared.orders.army.CommanderLeaveFederationOrder;
import empire.webapp.shared.orders.army.CommanderUndoDissOrder;
import empire.webapp.shared.orders.army.CommanderUndoHireOrder;
import empire.webapp.shared.stores.GameStore;
import empire.webapp.shared.stores.MovementStore;
import empire.webapp.shared.stores.economy.OrderStore;
import empire.webapp.shared.stores.util.calculators.CostCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommanderStore implements ArmyConstants, OrderConstants {

    /**
     * The commanders we have for this nation.
     */
    private final List<CommanderDTO> commandersList = new ArrayList<CommanderDTO>();

    private final List<CommanderDTO> capturedCommanders = new ArrayList<CommanderDTO>();

    /**
     * Singleton instance of the CommanderManager.
     */
    private static CommanderStore ourInstance = null;

    /**
     * This nation's commanders
     */
    private final Map<Integer, CommanderDTO> commandersMap = new HashMap<Integer, CommanderDTO>();

    /**
     * Variable indicating if the class is initailized on the client side
     */
    private boolean isClient = false;
    private boolean isInitialized = false;


    /**
     * Method returning the commander manager
     *
     * @return the one and only instance of the store
     */
    public static CommanderStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new CommanderStore();
        }
        return ourInstance;
    }

    /**
     * Method Used By the async callback to initialize  the commanders
     *
     * @param commandersList, a list of all the commanders of the nation
     */
    public void initCommanders(final List<CommanderDTO> commandersList, final List<CommanderDTO> capturedCommanders) {
        try {
            // Put all commanders in the free commanders Map
            this.commandersList.addAll(commandersList);
            for (final CommanderDTO commander : commandersList) {
                commandersMap.put(commander.getId(), commander);
            }
            this.capturedCommanders.addAll(capturedCommanders);
            isInitialized = true;
            if (isClient) {
                LoadEventManager.loadCommanders(commandersList);
            }
        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Failed to initialize commanders due to unexpected reason", false);
        }
    }

    /**
     * Method that adds a commander to a target army
     *
     * @param armyId      the id of the army we are targeting
     * @param commanderId the id of the commander we are adding to the army
     * @return true if the job was completed successfully
     */

    public boolean addCommanderToArmy(final int armyId, final int commanderId, final boolean isCancel) {
        boolean success = false;
        final CommanderDTO comm = getCommanderById(commanderId);
        if (isCancel) {//check if corps has been deleted and warn user.
            if (ArmyStore.getInstance().isArmyDeleted(armyId)) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "The army you are trying to assign the commander has been deleted. To assign the commander in this army you have to recreate it by undoing at least one \"Set corps free...\" order for this army", false);
                return false;
            }
        }
        //ckeck for restrictions
        if (comm.getLoaded()) {
            final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
            final List<ClientOrderDTO> loadOrders = new ArrayList<ClientOrderDTO>();
            loadOrders.addAll(OrderStore.getInstance().getClientOrders().get(ORDER_LOAD_TROOPSF));
            loadOrders.addAll(OrderStore.getInstance().getClientOrders().get(ORDER_LOAD_TROOPSS));
            for (ClientOrderDTO order : loadOrders) {
                if (order.getIdentifier(2) == COMMANDER
                        && order.getIdentifier(3) == commanderId) {
                    conflictOrders.add(order);
                }
            }

            if (!conflictOrders.isEmpty()) {
                new ErrorPopup(ErrorPopup.Level.WARNING,
                        "Cannot assign commander to army because commander is loaded to a transport unit. Review conflict orders?", true) {
                    public void onAccept() {
                        super.onAccept();
                        final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, "Orders that conflict with your action");
                        viewer.show();
                        viewer.center();
                    }
                };

            } else {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot assign commander because he is loaded to a transport unit.", false);
            }

            return false;
        }
        //done checking for restrictions
        final ArmyDTO targetArmy = ArmyStore.getInstance().getArmyById(armyId);
        if ((comm.getInPool() && hireCommander(commanderId, targetArmy.getRegionId())) || (!comm.getInPool())) {

            int[] idents = new int[1];
            idents[0] = commanderId;
            if (comm.getCorp() > 0) {
                undoAddCommanderToCorps(comm.getCorp(), commanderId);
            }
            if (comm.getArmy() > 0) {
                undoAddCommanderToArmy(comm.getArmy(), commanderId);
            }
            OrderStore.getInstance().removeOrder(ORDER_LEAVE_COM, idents);

            if (targetArmy.getCommander() != null
                    && targetArmy.getCommander().getId() > 0) {//be sure to remove previous commander of target corps.
                commanderLeaveFederation(targetArmy.getCommander().getId());
            }
            idents = new int[2];
            idents[0] = commanderId;
            if (armyId != comm.getStartArmy()) {
                idents[1] = armyId;
                OrderStore.getInstance().addNewOrder(ORDER_ARMY_COM, new OrderCostDTO(), comm.getRegionId(), comm.getName(), idents, 0, "");
            }

            final CommanderDTO commander = commandersMap.get(commanderId);
            final Map<Integer, ArmyDTO> armiesMap = ArmyStore.getInstance().getcArmies();
            final CommanderJoinArmyOrder cjaOrder = new CommanderJoinArmyOrder(armiesMap, commander);
            success = (cjaOrder.execute(armyId) == 1);
            UnitEventManager.changeUnit(COMMANDER, commanderId);
            UnitEventManager.changeUnit(ARMY, armyId);
        }
        return success;
    }

    /**
     * Method that adds a commander to a target corp
     *
     * @param corpId      the id of the corp we are targeting
     * @param commanderId the id of the commander we are adding to the corp
     * @return true if the job was completed successfully.
     */

    public boolean addCommanderToCorp(final int corpId, final int commanderId, final boolean isCancel) {
        boolean success = false;
        final CommanderDTO comm = getCommanderById(commanderId);
        if (isCancel) {//check if corps has been deleted and warn user.
            if (ArmyStore.getInstance().isCorpDeleted(corpId)) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "The corps you are trying to assign the commander has been deleted. To assign the commander in this corp you have to recreate it by undoing at least one \"Set brigade free...\" order for this corps", false);
                return false;
            }
        }


        //ckeck for restrictions
        if (comm.getLoaded()) {
            final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
            final List<ClientOrderDTO> loadOrdersP1 = OrderStore.getInstance().getClientOrders().get(ORDER_LOAD_TROOPSS);
            final List<ClientOrderDTO> loadOrdersP2 = OrderStore.getInstance().getClientOrders().get(ORDER_LOAD_TROOPSF);

            if (loadOrdersP1 != null) {
                for (ClientOrderDTO order : loadOrdersP1) {
                    if (order.getIdentifier(3) == commanderId) {
                        conflictOrders.add(order);
                    }
                }
            }
            if (loadOrdersP2 != null) {
                for (ClientOrderDTO order : loadOrdersP2) {
                    if (order.getIdentifier(3) == commanderId) {
                        conflictOrders.add(order);
                    }
                }
            }

            if (!conflictOrders.isEmpty()) {
                new ErrorPopup(ErrorPopup.Level.WARNING,
                        "Cannot assign commander to army because commander is loaded to a transport unit. Review conflict orders?", true) {
                    public void onAccept() {
                        super.onAccept();
                        final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, "Orders that conflict with your action");
                        viewer.show();
                        viewer.center();
                    }
                };

            } else {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot assign commander because he is loaded to a transport unit.", false);
            }

            return false;
        }
        //done checking for restrictions

        final CorpDTO targetCorps = ArmyStore.getInstance().getCorpByID(corpId);
        if ((comm.getInPool() && hireCommander(commanderId, targetCorps.getRegionId())) || (!comm.getInPool() && !comm.getInTransit())) {

            int[] idents = new int[1];
            idents[0] = commanderId;
            if (comm.getCorp() > 0) {
                undoAddCommanderToCorps(comm.getCorp(), commanderId);
            }
            if (comm.getArmy() > 0) {
                undoAddCommanderToArmy(comm.getArmy(), commanderId);
            }

            if (targetCorps.getCommander() != null
                    && targetCorps.getCommander().getId() > 0) {//be sure to remove previous commander of target corps.
                commanderLeaveFederation(targetCorps.getCommander().getId());
            }
            OrderStore.getInstance().removeOrder(ORDER_LEAVE_COM, idents);

            idents = new int[2];
            idents[0] = commanderId;
            if (corpId != comm.getStartCorp()) {
                idents[1] = corpId;
                OrderStore.getInstance().addNewOrder(ORDER_CORP_COM, new OrderCostDTO(), comm.getRegionId(), comm.getName(), idents, 0, "");
            }

            final CommanderDTO commander = commandersMap.get(commanderId);
            final Map<Integer, ArmyDTO> armiesMap = ArmyStore.getInstance().getcArmies();
            final CommanderJoinCorpOrder cjaOrder = new CommanderJoinCorpOrder(armiesMap, commander);
            success = (cjaOrder.execute(corpId) == 1);
            UnitEventManager.changeUnit(COMMANDER, commanderId);
            UnitEventManager.changeUnit(CORPS, corpId);
        }
        return success;
    }

    /**
     * Undo add commander to corps command.
     *
     * @param corpId      The corp the commander leads.
     * @param commanderId The commander.
     */
    public void undoAddCommanderToCorps(final int corpId, final int commanderId) {
        final CommanderDTO comm = getCommanderById(commanderId);
        int[] ids = new int[2];
        ids[0] = commanderId;
        ids[1] = corpId;
        if (OrderStore.getInstance().removeOrder(ORDER_CORP_COM, ids)) {
            fixCommanderAssignUndo(comm);
            final CorpDTO corp = ArmyStore.getInstance().getCorpByID(corpId);
            corp.setCommander(new CommanderDTO());
            if (OrderStore.getInstance().getOrderByTypeIds(ORDER_HIRE_COM, commanderId) != null) {
                undoHireCommander(commanderId);
            }
        }

        UnitEventManager.changeUnit(COMMANDER, commanderId);
        UnitEventManager.changeUnit(CORPS, corpId);
    }

    public void fixCommanderAssignUndo(final CommanderDTO commander) {
        if (commander.getStartArmy() > 0) {
            final ArmyDTO startArmy = ArmyStore.getInstance().getArmyById(commander.getStartArmy());
            if (startArmy == null || (startArmy.getCommander() != null && startArmy.getCommander().getId() > 0)) {
                commander.setArmy(0);
                commanderLeaveFederation(commander.getId());
            } else if (startArmy != null) {
                commander.setArmy(startArmy.getArmyId());
                startArmy.setCommander(commander);
            }
            if (startArmy != null) {
                UnitEventManager.changeUnit(ARMY, commander.getStartArmy());
            }
        } else {
            commander.setArmy(0);
        }
        if (commander.getStartCorp() > 0) {
            final CorpDTO startCorp = ArmyStore.getInstance().getCorpByID(commander.getStartCorp());
            if (startCorp == null || (startCorp.getCommander() != null && startCorp.getCommander().getId() > 0)) {
                commander.setCorp(0);
                commanderLeaveFederation(commander.getId());
            } else if (startCorp != null) {
                commander.setCorp(startCorp.getCorpId());
                startCorp.setCommander(commander);
            }
            if (startCorp != null) {
                UnitEventManager.changeUnit(CORPS, commander.getStartCorp());
            }
        } else {
            commander.setCorp(0);
        }
    }

    /**
     * Undo add commander to army command.
     *
     * @param armyId      The army the commander leads.
     * @param commanderId The commander.
     */
    public void undoAddCommanderToArmy(final int armyId, final int commanderId) {
        final CommanderDTO comm = getCommanderById(commanderId);
        int[] ids = new int[2];
        ids[0] = commanderId;
        ids[1] = armyId;
        if (OrderStore.getInstance().removeOrder(ORDER_ARMY_COM, ids)) {
            //check if start army exist
            fixCommanderAssignUndo(comm);
            final ArmyDTO army = ArmyStore.getInstance().getArmyById(armyId);
            army.setCommander(new CommanderDTO());
            if (OrderStore.getInstance().getOrderByTypeIds(ORDER_HIRE_COM, commanderId) != null) {
                undoHireCommander(commanderId);
            }

        }
        UnitEventManager.changeUnit(COMMANDER, commanderId);
        UnitEventManager.changeUnit(ARMY, armyId);
    }

    /**
     * Method that removes a commander from a target army/corp
     *
     * @param commanderId the id of the commander
     * @return true if the job was completed successfully
     */
    public boolean commanderLeaveFederation(final int commanderId) {
        boolean success;
        final CommanderDTO comm = getCommanderById(commanderId);
        if (comm == null) {
            return false;
        }
        final int armyId = comm.getArmy();
        final int corpId = comm.getCorp();
        final int[] idents = new int[2];
        idents[0] = commanderId;
        idents[1] = 0;
        if (armyId > 0) {
            undoAddCommanderToArmy(armyId, commanderId);
        }
        if (corpId > 0) {
            undoAddCommanderToCorps(corpId, commanderId);
        }
        if (0 != comm.getStartArmy() || 0 != comm.getStartCorp()) {
            OrderStore.getInstance().addNewOrder(ORDER_LEAVE_COM, new OrderCostDTO(), comm.getRegionId(), comm.getName(), idents, 0, "");
        }

        final CommanderDTO commander = commandersMap.get(commanderId);
        final Map<Integer, ArmyDTO> armiesMap = ArmyStore.getInstance().getcArmies();
        final CommanderLeaveFederationOrder cjaOrder = new CommanderLeaveFederationOrder(armiesMap, commander);
        success = (cjaOrder.execute(commanderId) == 1);
        UnitEventManager.changeUnit(COMMANDER, commanderId);
        UnitEventManager.changeUnit(CORPS, corpId);
        UnitEventManager.changeUnit(ARMY, armyId);

        return success;
    }

    /**
     * Method that dismisses a target commander to
     * the pool
     *
     * @param commanderId the id of the target commander
     * @return true if the commander was properly dismissed
     */

    public boolean dismissCommander(final int commanderId) {
        boolean success = false;
        final CommanderDTO commander = getCommanderById(commanderId);
        final int armyId = commander.getArmy();
        final int corpId = commander.getCorp();
        undoHireCommander(commanderId);
        if ((armyId == 0 && corpId == 0 && !commander.getInPool()) || (commander.isCaptured())) {
            final int[] idents = new int[2];
            idents[0] = commanderId;
            idents[1] = commander.getRegionId();
            if (OrderStore.getInstance().addNewOrder(ORDER_DISS_COM, CostCalculators.getDissCommCost(commander), commander.getRegionId(), "", idents, 0, "") == 1) {
                final CommanderDismissOrder cdOrder = new CommanderDismissOrder(commander);
                success = (cdOrder.execute(commanderId) == 1);
                UnitEventManager.changeUnit(COMMANDER, commanderId);
            }
        } else {
            success = false;
        }
        return success;
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

    public boolean hireCommander(final int commanderId, final int regionId) {
        boolean success = false;
        final CommanderDTO commander = getCommanderById(commanderId);
        undoDismissCommander(commanderId);
        if (commander.getInPool()) {
            final int[] idents = new int[2];
            idents[0] = commanderId;
            idents[1] = regionId;
            if (OrderStore.getInstance().addNewOrder(ORDER_HIRE_COM, CostCalculators.getHireCommCost(commander, regionId), regionId, "", idents, 0, "") == 1) {
                final CommanderHireOrder cdOrder = new CommanderHireOrder(commander, regionId);
                success = (cdOrder.execute(commanderId) == 1);
                UnitEventManager.changeUnit(COMMANDER, commanderId);
            }
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
        boolean success = false;
        final CommanderDTO commander = getCommanderById(commanderId);
        if (commander.getInPool()) {
            final int[] idents = new int[2];
            idents[0] = commanderId;
            idents[1] = commander.getRegionId();
            if (OrderStore.getInstance().removeOrder(ORDER_DISS_COM, idents)) {
                final CommanderUndoDissOrder cdOrder = new CommanderUndoDissOrder(commander);
                success = (cdOrder.execute(commanderId) == 1);
                UnitEventManager.changeUnit(COMMANDER, commanderId);
            }
        } else {
            success = false;
        }
        return success;

    }

    /**
     * Method that will undo the hire of the commander
     * and return him to the map
     *
     * @param commanderId the id of the target commander
     * @return the result of the operation.
     */
    public boolean undoHireCommander(final int commanderId) {
        boolean success = false;
        final CommanderDTO commander = getCommanderById(commanderId);
        if (commander.getInPool()) {
            success = false;
        } else {
            final int[] idents = new int[1];
            idents[0] = commanderId;
            if (OrderStore.getInstance().removeOrder(ORDER_HIRE_COM, idents)) {
                final CommanderUndoHireOrder cdOrder = new CommanderUndoHireOrder(commander);
                success = (cdOrder.execute(commanderId) == 1);
                UnitEventManager.changeUnit(COMMANDER, commanderId);
            }
        }
        return success;
    }


    /**
     * Returns all commanders starting the round from the target sector
     *
     * @param sector            the target sector
     * @param addPoolCommanders if true add the pool commanders
     * @param atStartingPos     if true returns result checking the starting pos only
     *                          else it returns everything there
     * @return a list of all the available(not in army nor corps) commanders
     */
    public List<CommanderDTO> getCommandersBySector(final SectorDTO sector,
                                                    final boolean addPoolCommanders,
                                                    final boolean atStartingPos) {

        final List<CommanderDTO> sectorCommList = new ArrayList<CommanderDTO>();
        for (final CommanderDTO commander : commandersMap.values()) {
            if ((commander.getXStart() == sector.getX() && commander.getYStart() == sector.getY() &&
                    commander.getRegionId() == sector.getRegionId())
                    || (commander.getX() == sector.getX() && commander.getY() == sector.getY() &&
                    commander.getRegionId() == sector.getRegionId() && !atStartingPos)
                    || (commander.getInPool() && !commander.getInTransit() && addPoolCommanders)) {
                sectorCommList.add(commander);
            }
        }

        return sectorCommList;
    }

    /**
     * Returns all commanders starting the round from the target sector
     * and still being there
     *
     * @param sector            the target sector
     * @param addPoolCommanders if true add the pool commanders
     * @param onlyAlive         only alive commanders.
     * @return a list of all the available(not in army nor corps) commanders
     */
    public List<CommanderDTO> getCommandersBySectorAndStartingPosition(final SectorDTO sector,
                                                                       final boolean addPoolCommanders,
                                                                       final boolean onlyAlive) {
        final List<CommanderDTO> sectorCommList = new ArrayList<CommanderDTO>();
        for (final CommanderDTO commander : commandersMap.values()) {
            if ((((commander.getXStart() == sector.getX() && commander.getYStart() == sector.getY() &&
                    commander.getRegionId() == sector.getRegionId()) &&
                    (commander.getX() == commander.getXStart() && commander.getY() == commander.getYStart() &&
                            commander.getRegionId() == sector.getRegionId()))
                    || (commander.getInPool() && !commander.getInTransit() && addPoolCommanders)) && ((!commander.getDead() && !commander.isCaptured()) || !onlyAlive)) {
                sectorCommList.add(commander);
            }
        }
        return sectorCommList;
    }

    /**
     * Returns all commanders from the target sector considering the movement commands
     *
     * @param regionId      the id of the target region
     * @param xPos          the xPos coordinate
     * @param yPos          the yPos coordinate
     * @param afterMovement consider positions after movement.
     * @param onlyAlive     consider only alive commanders.
     * @return a list of all the commanders
     */
    public List<CommanderDTO> getCommandersByPositionWithMovement(final int regionId,
                                                                  final int xPos,
                                                                  final int yPos,
                                                                  final boolean afterMovement,
                                                                  final boolean onlyAlive) {
        final List<CommanderDTO> sectorCommList = new ArrayList<CommanderDTO>();
        for (final CommanderDTO commander : commandersMap.values()) {
            if (!onlyAlive || (!commander.getDead() && !commander.isCaptured())) {
                PositionDTO pos;
                if (commander.getArmy() != 0) {
                    pos = MovementStore.getInstance().getUnitPosition(ARMY, commander.getArmy(), ArmyStore.getInstance().getcArmies().get(commander.getArmy()));
                } else if (commander.getCorp() != 0) {
                    pos = MovementStore.getInstance().getUnitPosition(CORPS, commander.getCorp(), ArmyStore.getInstance().getCorpByID(commander.getCorp()));
                } else {
                    pos = MovementStore.getInstance().getUnitPosition(COMMANDER, commander.getId(), commander);
                }

                if (!afterMovement) {
                    pos = new PositionDTO();
                    pos.setRegionId(commander.getRegionId());
                    pos.setX(commander.getX());
                    pos.setY(commander.getY());
                }

                if (pos.getX() == xPos && pos.getY() == yPos &&
                        pos.getRegionId() == regionId) {
                    sectorCommList.add(commander);
                }
            }
        }

        return sectorCommList;
    }

    /**
     * Get all commanders from the selected region
     *
     * @param regionId  the traget region id.
     * @param onlyAlive consider only alive commanders.
     * @return the commander object.
     */
    public List<CommanderDTO> getCommandersByRegion(final int regionId, final boolean onlyAlive) {
        final List<CommanderDTO> tileCommanders = new ArrayList<CommanderDTO>();
        for (final CommanderDTO commander : commandersMap.values()) {
            if (commander.getRegionId() == regionId
                    && (!onlyAlive || (!commander.getDead() && !commander.isCaptured()))) {
                tileCommanders.add(commander);
            }
        }
        return tileCommanders;
    }

    /**
     * Returns the commander with id
     *
     * @param id the id of the commander
     * @return the commander
     */
    public CommanderDTO getCommanderById(final int id) {
        if (commandersMap.containsKey(id)) {
            return commandersMap.get(id);

        } else {
            return null;
        }
    }


    /**
     * Method that returns true if the sector has free commanders
     *
     * @param sector the sector where we are investigating
     * @return true is the Sector has free commanders
     */
    public boolean hasSectorFreeCommanders(final SectorDTO sector) {
        for (final CommanderDTO commander : commandersMap.values()) {
            if (commander.getRegionId() == sector.getRegionId() &&
                    commander.getX() == sector.getX() && commander.getY() == sector.getY()
                    && commander.getArmy() < 1 && commander.getCorp() < 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove commander rename order.
     *
     * @param commanderId The commander to undo the rename.
     */
    public void undoRenameCommander(final int commanderId) {
        final int[] ids = new int[1];
        ids[0] = commanderId;
        OrderStore.getInstance().removeOrder(ORDER_REN_COMM, ids);
        CommanderDTO comm = getCommanderById(commanderId);
        if (comm != null) {
            comm.setName(comm.getOriginalName());
        }
        UnitEventManager.changeUnit(COMMANDER, commanderId);
    }

    /**
     * Add a new rename commander order.
     *
     * @param commId The commander to rename.
     * @param name   The new name to set.
     */
    public void renameCommander(final int commId, final String name) {
        if (commId != 0) {
            final int[] ids = new int[1];
            ids[0] = commId;
            undoRenameCommander(commId);
            final CommanderDTO comm = getCommanderById(commId);
            if (!name.equals(comm.getOriginalName())) {
                if (OrderStore.getInstance().addNewOrder(ORDER_REN_COMM, CostCalculators.getShipRenameCost(), 1, name, ids, 0, "") == 1) {
                    changeCommanderName(commId, name);
                    GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_REN_COMM, ids);
                }
                UnitEventManager.changeUnit(COMMANDER, commId);
            }
        }
    }

    /**
     * Apply rename commander in clients view.
     *
     * @param commId The commander to rename,
     * @param name   The name to set.
     */
    public void changeCommanderName(final int commId, final String name) {
        if (commandersMap.containsKey(commId)) {
            final ChangeCommanderNameOrder ccnName = new ChangeCommanderNameOrder(commandersMap, name);
            ccnName.execute(commId);
        }
    }

    /**
     * @return the commandersMap
     */
    public Map<Integer, CommanderDTO> getCommandersMap() {
        return commandersMap;
    }

    /**
     * @return the isClient
     */
    public boolean isClient() {
        return isClient;
    }

    /**
     * @param isClient the isClient to set
     */
    public void setClient(final boolean isClient) {
        this.isClient = isClient;
    }

    /**
     * @return the commandersList
     */
    public List<CommanderDTO> getCommandersList() {
        return commandersList;
    }

    /**
     * @param isInitialized the isInitialized to set
     */
    public void setInitialized(final boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    /**
     * @return the isInitialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    public List<CommanderDTO> getCapturedCommanders() {
        return capturedCommanders;
    }
}
