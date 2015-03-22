package com.eaw1805.www.shared.stores.units;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.views.popups.OrdersViewerPopup;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.orders.navy.ChangeFleetNameOrder;
import com.eaw1805.www.shared.orders.navy.ChangeShipFleetOrder;
import com.eaw1805.www.shared.orders.navy.ChangeShipNameOrder;
import com.eaw1805.www.shared.orders.navy.CreateFleetOrder;
import com.eaw1805.www.shared.orders.navy.DeleteFleetOrder;
import com.eaw1805.www.shared.orders.navy.HandOverShipOrder;
import com.eaw1805.www.shared.orders.navy.ScuttleShipOrder;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.util.NavyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class is final.
 * We only need one manager for our application and only once the data.
 */
public final class NavyStore
        implements OrderConstants, GoodConstants, ArmyConstants {

    /**
     * A list containing all the available fleets as taken from the service
     */
    private transient List<FleetDTO> dbFleetlist = new ArrayList<FleetDTO>();

    /**
     * A hash map containing all the fleet ids and their corresponding objects
     */
    private final transient Map<Integer, FleetDTO> idFleetMap = new HashMap<Integer, FleetDTO>();

    private final transient Map<Integer, FleetDTO> idDeletedFleetMap = new HashMap<Integer, FleetDTO>();

    /**
     * A hash map containing all the ship ids and their corresponding objects
     */
    private final transient Map<Integer, ShipDTO> idShipMap = new HashMap<Integer, ShipDTO>();

    /**
     * The ship types we have for this nation.
     */
    private final List<ShipTypeDTO> shipTypesList = new ArrayList<ShipTypeDTO>();

    /**
     * A map of position ids and new ships.
     */
    private final transient Map<Integer, List<ShipDTO>> barrShipMap = new HashMap<Integer, List<ShipDTO>>();
    private final transient Map<Integer, List<ShipDTO>> barrShipMapNew = new HashMap<Integer, List<ShipDTO>>();


    /**
     * Our instance of the Manager
     */
    private static transient NavyStore ourInstance = null;

    /**
     * Variable telling us if our data are initialized
     */
    private boolean isInitialized = false, isClient = false;

    int newShipId;

    /**
     * constructor
     */
    private NavyStore() {
        super();
        newShipId = 0;
    }

    /**
     * Method Used By the service to initialize  the Map.
     *
     * @param fleets the list of fleet objects.
     */
    public void initDbFleets(final List<FleetDTO> fleets, final List<FleetDTO> deletedFleets) {
        try {
            // clear any previous additions to collections
            dbFleetlist = fleets;
            if (fleets != null) {
                for (final FleetDTO fleet : dbFleetlist) {
                    // Fleet with id -1 is a dummy fleet and
                    // we don't want it in our data
                    // If there is no such fleet in our map
                    if (fleet.getFleetId() != -1
                            && !idFleetMap.containsKey(fleet.getFleetId())) {
                        // Make one.
                        idFleetMap.put(fleet.getFleetId(), fleet);

                        // Add all fleet ships
                        for (final ShipDTO ship : fleet.getShips().values()) {
                            idShipMap.put(ship.getId(), ship);
                        }
                    }
                }
            }
            if (deletedFleets != null) {
                for (final FleetDTO fleet : deletedFleets) {
                    if (fleet.getFleetId() != -1
                            && !idDeletedFleetMap.containsKey(fleet.getFleetId())) {
                        idDeletedFleetMap.put(fleet.getFleetId(), fleet);
                    }
                }
            }

            if (isClient()) {
                LoadEventManager.loadNavy(fleets);
            }

            isInitialized = true;

        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Failed to initialize fleets due to unexpected reason", false);
            if (isClient()) {
                LoadEventManager.loadNavy(fleets);
            }
        }
    }

    /**
     * Method Used By the async callback to initialize  the ship types.
     *
     * @param shipTypesList a list of ship types.
     */
    public void initDbShipTypes(final List<ShipTypeDTO> shipTypesList) {
        try {
            this.shipTypesList.clear();
            this.shipTypesList.addAll(shipTypesList);
            isInitialized = true;

        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to initialize ship types due to unexpected reason", false);
        }
    }

    /**
     * Method Used By the async callback to initialize the new ships.
     *
     * @param newShipMap a map of ships list per region.
     */
    public void initNewShips(final Map<Integer, List<ShipDTO>> newShipMap) {
        try {
            barrShipMap.clear();
            barrShipMapNew.clear();
            barrShipMap.putAll(newShipMap);
            for (Map.Entry<Integer, List<ShipDTO>> entry : newShipMap.entrySet()) {
                barrShipMapNew.put(entry.getKey(), new ArrayList<ShipDTO>());
                for (ShipDTO ship : entry.getValue()) {
                    if (ship.isJustUnderConstruction()) {
                        barrShipMapNew.get(entry.getKey()).add(ship);
                    }
                }
            }
            newShipId = 0;
            for (List<ShipDTO> shipDTOs : NavyStore.getInstance().getBarrShipMap().values()) {
                for (ShipDTO ship : shipDTOs) {
                    if (ship.getId() > -1000 && ship.getId() <= newShipId) {
                        //there is no chanse we will build 1000 ships in one round.
                        //if the id is lower than 1000, that means it is being constructed
                        //from the previous round (those get Math.MinValue ids).
                        newShipId = ship.getId() - 1;
                    }
                }
            }
            isInitialized = true;

        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to initialize new ships due to unexpected reason", false);
        }
    }

    /**
     * Method returning the fleet manager if already initialized
     * or the a new instance.
     *
     * @return the unique instance of the store.
     */
    public static NavyStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new NavyStore();
        }
        return ourInstance;
    }

    public Map<Integer, FleetDTO> getIdFleetMap() {
        return idFleetMap;
    }

    public boolean hasTransportLoadedUnits(final TransportUnitDTO ship) {
        int itemCount = 0;
        if (ship.getLoadedUnitsMap() != null && ship.getLoadedUnitsMap().values().size() > 0) {
            for (List<Integer> units : ship.getLoadedUnitsMap().values()) {
                itemCount += units.size();
            }
        }
        return itemCount != 0;
    }


    public int getFleetCondition(final FleetDTO fleet) {
        int sumCondition = 0;
        for (ShipDTO ship : fleet.getShips().values()) {
            sumCondition += ship.getCondition();
        }
        return sumCondition / fleet.getShips().values().size();
    }

    /**
     * Method that changes the ships's fleet
     *
     * @param shipId     the identity of the ship to lookup.
     * @param oldFleetId the ID of the old fleet.
     * @param newFleetId the ID of the new fleet.
     * @param checkLoad  check that the ship is loaded.
     * @return the result of the operation.
     */
    public boolean changeShipFleet(final int shipId, final int oldFleetId, final int newFleetId, final boolean checkLoad, final boolean isCancel) {
        if (oldFleetId != 0 && MovementStore.getInstance().hasMovedThisTurn(FLEET, oldFleetId)) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot do action because ship or fleet has been moved", false);
            return false;
        }
        if (!checkLoad || !hasTransportLoadedUnits(getShipById(shipId)) || oldFleetId == 0) {
            if (oldFleetId > 0 &&
                    hasTransportLoadedUnits(NavyStore.getInstance().getFleetById(oldFleetId))) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot remove ship from fleet because fleet has loaded troops", false);
                return false;
            }
            final int[] idents = new int[2];
            idents[0] = shipId;
            idents[1] = oldFleetId;

            OrderStore.getInstance().removeOrder(ORDER_ADDTO_FLT, idents);
            if (newFleetId != (getShipById(shipId)).getStartFleet()) {
                idents[1] = newFleetId;
                OrderStore.getInstance().addNewOrder(ORDER_ADDTO_FLT, new OrderCostDTO(), 1, "", idents, 0, "");
            }
            //check if new fleet exists
            if (isCancel) {
                FleetDTO newFleet = getFleetById(newFleetId);
                if (newFleet == null) {
                    idFleetMap.put(newFleetId, idDeletedFleetMap.remove(newFleetId));//recover deleted fleet
                }
            }
            if (newFleetId == 0) {//if it is the dummy fleet for the free ships, check it exists
                if (!idFleetMap.containsKey(0)) {
                    FleetDTO dummyFleet = new FleetDTO();
                    dummyFleet.setName("Free Ships");
                    dummyFleet.setOriginalName("Free Ships");
                    dummyFleet.setFleetId(0);
                    dummyFleet.setNationId(GameStore.getInstance().getNationId());
                    dummyFleet.setShips(new HashMap<Integer, ShipDTO>());
                    idFleetMap.put(0, dummyFleet);
                }
            }

            final ChangeShipFleetOrder csfOrder = new ChangeShipFleetOrder(idFleetMap, newFleetId);
            csfOrder.execute(shipId);

            UnitEventManager.changeUnit(SHIP, shipId);
            UnitEventManager.changeUnit(FLEET, newFleetId);
            UnitEventManager.changeUnit(FLEET, oldFleetId);

            //if old fleet is empty and is not the dummy fleet... delete it...
            if (oldFleetId != 0) {
                final FleetDTO oldFleet = getFleetById(oldFleetId);
                if (oldFleet != null && oldFleet.getShips().size() == 0) {
                    deleteFleet(oldFleetId);
                }
            }


            return true;

        } else {
            if (isClient()) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot remove ship from fleet because it is loaded with troops", false);
            }
            return false;
        }
    }

    /**
     * Method that creates a new fleet.
     *
     * @param fleetId  the ID of the new fleet.
     * @param index    the index of the fleet in the map.
     * @param name     the name of the new fleet.
     * @param xPos     the coordinate.
     * @param yPos     the coordinate.
     * @param regionId the region.
     * @param nationId the nation.
     * @return the new object.
     */
    public FleetDTO createFleet(final int fleetId, final int index, final String name,
                                final int xPos, final int yPos,
                                final int regionId, final int nationId) {
        if (idFleetMap.containsKey(fleetId)) {
            return null;
        } else {
            final CreateFleetOrder cfOrder = new CreateFleetOrder(idFleetMap, name, null, xPos, yPos, regionId, nationId);
            cfOrder.execute(fleetId);
            return idFleetMap.get(fleetId);

        }
    }

    /**
     * Method that deletes an empty fleet.
     *
     * @param fleetId the identity of the fleet.
     */
    public void deleteFleet(final int fleetId) {
        if (MovementStore.getInstance().hasMovedThisTurn(FLEET, fleetId)) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot undo order because fleet has been moved, First undo the movement and try again.", false);
            return;
        }
        if (idFleetMap.get(fleetId).getShips().values().isEmpty()) {
            final int[] idents = new int[2];
            idents[0] = fleetId;
            OrderStore.getInstance().removeOrder(ORDER_B_FLT, idents);
            final DeleteFleetOrder dfOrder = new DeleteFleetOrder(idFleetMap, fleetId, idDeletedFleetMap);
            dfOrder.execute(fleetId);
            UnitEventManager.destroyUnit(FLEET, fleetId);
        } else {
            if (isClient) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "The fleet is not empty.", false);
            }
        }

    }

    /**
     * Remove fleet rename order.
     *
     * @param fleetId The fleet to undo the rename.
     */
    public void undoRenameFleet(final int fleetId) {
        int[] ids = new int[1];
        ids[0] = fleetId;
        OrderStore.getInstance().removeOrder(ORDER_REN_FLT, ids);
        FleetDTO fleet = getFleetById(fleetId);
        if (fleet != null) {
            fleet.setName(fleet.getOriginalName());
        }
        UnitEventManager.changeUnit(FLEET, fleetId);
    }

    /**
     * Add the rename order for fleets.
     *
     * @param fleetId The fleet id to rename.
     * @param name    The new name to set.
     */
    public void renameFleet(final int fleetId, final String name) {
        if (fleetId != 0) {
            int[] ids = new int[1];
            ids[0] = fleetId;
            undoRenameFleet(fleetId);
            final FleetDTO fleet = getFleetById(fleetId);
            if (!name.equals(fleet.getOriginalName())) {
                if (OrderStore.getInstance().addNewOrder(ORDER_REN_FLT, CostCalculators.getShipRenameCost(), 1, name, ids, 0, "") == 1) {
                    changeFleetName(fleetId, name);
                    GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_REN_FLT, ids);
                }
                UnitEventManager.changeUnit(FLEET, fleetId);
            }
        }
    }

    /**
     * Method that changes an existing fleet's name.
     *
     * @param fleetId the identity of the fleet.
     * @param newName the new name.
     */
    public void changeFleetName(final int fleetId, final String newName) {
        final ChangeFleetNameOrder cfnOrder = new ChangeFleetNameOrder(idFleetMap, newName);
        cfnOrder.execute(fleetId);
    }

    /**
     * Add a new scuttle baggage train order.
     *
     * @param shipId   The baggage train to scuttle.
     * @param regionId The region id.
     */
    public void addScuttleOrder(final int shipId, final int regionId) {
        final int[] ids = new int[1];
        ids[0] = shipId;
        undoScuttleOrder(shipId);
        if (OrderStore.getInstance().addNewOrder(ORDER_SCUTTLE_SHIP, CostCalculators.getScuttleShipCost(getShipById(shipId)), regionId, "", ids, 0, "") == 1) {
            scuttleShip(shipId, true, false);
        }
    }

    /**
     * Execute scuttle baggage train order.
     *
     * @param shipId  The baggage train to scuttle.
     * @param scuttle True to scuttle.
     * @param isInit  True if it is called from server code.
     */
    public void scuttleShip(final int shipId, final boolean scuttle, final boolean isInit) {
        //execute command
        final ScuttleShipOrder sbtOrder = new ScuttleShipOrder(idShipMap, scuttle);
        sbtOrder.execute(shipId);

        if (!isInit) {
            //if scuttle is true inform the interested parties
            UnitEventManager.changeUnit(SHIP, shipId);
        }
    }

    /**
     * Undo scuttle baggage train order.
     *
     * @param shipId The baggage train to undo the order.
     */
    public void undoScuttleOrder(final int shipId) {
        final int[] ids = new int[1];
        ids[0] = shipId;
        OrderStore.getInstance().removeOrder(ORDER_SCUTTLE_SHIP, ids);
        //un-scuttle baggage train
        scuttleShip(shipId, false, false);
    }


    /**
     * Build a ship on the specified position.
     *
     * @param sectorId the id fo the sector.
     * @param newShip  the new ship object.
     * @return a map of the ships to be built.
     */
    public Map<Integer, List<ShipDTO>> buildShip(final int sectorId, final ShipDTO newShip) {
        if (!barrShipMap.containsKey(sectorId)) {
            barrShipMap.put(sectorId, new ArrayList<ShipDTO>());
        }
        barrShipMap.get(sectorId).add(newShip);
        if (!barrShipMapNew.containsKey(sectorId)) {
            barrShipMapNew.put(sectorId, new ArrayList<ShipDTO>());
        }
        barrShipMapNew.get(sectorId).add(newShip);

        MapStore.getInstance().getUnitGroups().getNewShipsByRegionId(newShip.getRegionId()).clear();
        final List<FleetDTO> fleets = new ArrayList<FleetDTO>();
        fleets.add(getNewShipsByRegionAsFleet(newShip.getRegionId()));
        MapStore.getInstance().getUnitGroups().getNewShipsByRegionId(newShip.getRegionId()).setImageUrls(fleets, GameStore.getInstance().getNationId(), true, false, false);
        return barrShipMap;
    }

    /**
     * Cancel the building of the ship on the target position
     *
     * @param sectorId the id of the sector.
     * @param ship     the object of the ship to cancel.
     */
    public void cancelBuildShip(final int sectorId, final ShipDTO ship) {
        if (ship == null) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Ship could not be removed for unknown reason 1", false);
        } else {
            final int[] idents = new int[3];
            idents[0] = ship.getId();
            idents[1] = sectorId;
            idents[2] = ship.getType().getIntId();
            if (OrderStore.getInstance().removeOrder(ORDER_B_SHIP, idents)) {
                final Iterator<ShipDTO> iter = barrShipMap.get(sectorId).iterator();
                while (iter.hasNext()) {
                    if (iter.next().getId() == ship.getId()) {
                        iter.remove();
                        break;
                    }
                }

                final Iterator<ShipDTO> iter2 = barrShipMapNew.get(sectorId).iterator();
                while (iter2.hasNext()) {
                    if (iter2.next().getId() == ship.getId()) {
                        iter2.remove();
                        break;
                    }
                }

                MapStore.getInstance().getUnitGroups().getNewShipsByRegionId(ship.getRegionId()).clear();
                final List<FleetDTO> fleets = new ArrayList<FleetDTO>();
                fleets.add(getNewShipsByRegionAsFleet(ship.getRegionId()));
                MapStore.getInstance().getUnitGroups().getNewShipsByRegionId(ship.getRegionId()).setImageUrls(fleets, GameStore.getInstance().getNationId(), true, false, false);
            } else {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Ship could not be removed for unknown reason 2", false);
            }
        }

    }

    /**
     * Method that returns the newly built ship.
     *
     * @param shipId   the id of the newly built ship.
     * @param sectorId the sector where the ship will be built.
     * @param typeId   the id of the ship type.
     * @return the newly built ship.
     */
    public ShipDTO getNewShip(final int shipId, final int sectorId, final int typeId) {
        for (final ShipDTO ship : barrShipMap.get(sectorId)) {
            if (ship.getId() == shipId && ship.getType().getIntId() == typeId) {
                return ship;
            }
        }
        return null;
    }

    /**
     * Get all new ships in the corresponding sector.
     *
     * @param sectorId The sector id.
     * @return A list with new ships.
     */
    public List<ShipDTO> getNewShipBySector(final int sectorId) {
        return barrShipMap.get(sectorId);
    }

    /**
     * Get all the under construction ships by region.
     *
     * @param regionId The region to look up.
     * @return A list with the under construction ships.
     */
    public List<ShipDTO> getNewShipsByRegion(final int regionId) {
        final List<ShipDTO> out = new ArrayList<ShipDTO>();
        for (final Integer sectorId : barrShipMap.keySet()) {
            if (RegionStore.getInstance().getRegionBySectorId(sectorId) == regionId) {
                out.addAll(barrShipMap.get(sectorId));
            }
        }
        return out;
    }

    public ShipDTO getNewShip(final int shipId, final int sectorId) {
        for (final ShipDTO ship : barrShipMap.get(sectorId)) {
            if (ship.getId() == shipId) {
                return ship;
            }
        }
        return null;
    }

    public FleetDTO getNewShipsByRegionAsFleet(final int regionId) {
        final FleetDTO dummy = new FleetDTO();
        dummy.setFleetId(0);
        dummy.setShips(new HashMap<Integer, ShipDTO>());
        for (ShipDTO ship : getNewShipsByRegion(regionId)) {
            dummy.getShips().put(ship.getId(), ship);
        }
        return dummy;
    }

    /**
     * Function that hands over specified ship to the target allied nation.
     *
     * @param shipId           The sip you want to hand over.
     * @param selectedNationId The nation to which you will hand over the ship.
     */
    public void handOverShip(final int shipId, final int selectedNationId) {

        //check for conflicts
        final List<ClientOrderDTO> changeRelationOrders = OrderStore.getInstance().getClientOrders().get(ORDER_POLITICS);
        final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
        if (changeRelationOrders != null) {
            for (ClientOrderDTO order : changeRelationOrders) {
                if (order.getIdentifier(0) == selectedNationId) {
                    conflictOrders.add(order);
                }
            }
        }

        if (!conflictOrders.isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "You cannot handover tile to a nation that you changed the relations first. Review conflict orders?", true) {
                public void onAccept() {
                    super.onAccept();
                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, "Orders that conflict with your action");
                    viewer.show();
                    viewer.center();
                }
            };
            return;
        }

        undoHandOverShip(shipId);//just be sure there is no handover for this ship order.

        int[] ids = new int[2];
        ids[0] = shipId;
        ids[1] = selectedNationId;

        if (OrderStore.getInstance().addNewOrder(ORDER_HOVER_SHIP, CostCalculators.getHandOverCost(ORDER_HOVER_SHIP, 0), MapStore.getInstance().getActiveRegion(), "", ids, 0, "") == 1) {
            final HandOverShipOrder cfnOrder = new HandOverShipOrder(idShipMap, selectedNationId);
            cfnOrder.execute(shipId);
            UnitEventManager.changeUnit(SHIP, shipId);
        }

    }

    public void undoHandOverShip(final int shipId) {
        int[] ids = new int[1];
        ids[0] = shipId;
        OrderStore.getInstance().removeOrder(ORDER_HOVER_SHIP, ids);
        getShipById(shipId).sethOverNationId(0);
        UnitEventManager.changeUnit(SHIP, shipId);
    }


    public boolean repairFleet(final int fleetId, final int sectorId) {
        final FleetDTO fleet = idFleetMap.get(fleetId);
        if (fleet == null) {
            return false;
        }
        final int[] ids = new int[4];
        ids[0] = fleetId;
        ids[1] = sectorId;
        ids[2] = fleet.getRegionId();
        ids[3] = GameStore.getInstance().getNationId();
        boolean hasFleetBeenRepaired = false;
        if (cancelRepairFleet(fleetId, sectorId)) {
            hasFleetBeenRepaired = true;
        }
        final OrderCostDTO cost = CostCalculators.getFleetRepairCost(fleet);
        if (OrderStore.getInstance().addNewOrder(ORDER_R_FLT, cost, fleet.getRegionId(), "", ids, 0, cost.convertToString()) == 1) {
            for (ShipDTO ship : fleet.getShips().values()) {
                //remove ship repair for this order if exist
                //it means the ship has been repaired
                if (ship.getOriginalCondition() < ship.getCondition() && !hasFleetBeenRepaired) {
                    cancelRepairShip(fleetId, ship.getId(), sectorId);
                }
                //update the ships status to 100% since it gets repaired
                ship.setCondition(100);
                ship.setMarines(ship.getType().getCitizens());
            }
            UnitEventManager.changeUnit(FLEET, fleetId);
            return true;
        } else {
            return false;
        }
    }

    public boolean cancelRepairFleet(final int fleetId, final int sectorId) {
        final FleetDTO fleet = idFleetMap.get(fleetId);
        final int[] idents = new int[4];
        idents[0] = fleetId;
        idents[1] = sectorId;
        idents[2] = fleet.getRegionId();
        idents[3] = GameStore.getInstance().getNationId();
        if (OrderStore.getInstance().removeOrder(ORDER_R_FLT, idents)) {
            for (final ShipDTO ship : fleet.getShips().values()) {
                ship.setCondition(ship.getOriginalCondition());
                ship.setMarines(ship.getOriginalMarines());
            }
            UnitEventManager.changeUnit(FLEET, fleetId);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Function that repairs a damaged ship.
     *
     * @param shipId   The id of the ship we want to repair.
     * @param sectorId The id of the position where the ship starts this turn.
     * @return true if ship is repaired.
     */
    public boolean repairShip(final int shipId, final int sectorId) {
        final ShipDTO ship = getShipById(shipId);
        final int[] idents = new int[4];
        idents[0] = shipId;
        idents[1] = sectorId;
        idents[2] = ship.getRegionId();
        idents[3] = ship.getNationId();
        if (OrderStore.getInstance().addNewOrder(ORDER_R_SHP, CostCalculators.getShipRepairCost(ship.getCondition(), ship), ship.getRegionId(), "", idents, 0, "") == 1) {
            ship.setCondition(100);
            ship.setMarines(ship.getType().getCitizens());
            UnitEventManager.changeUnit(SHIP, shipId);
            UnitEventManager.changeUnit(FLEET, ship.getFleet());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Function that cancels repairs on a damaged ship
     *
     * @param fleetId  the id of the fleet in which the ship resides
     * @param shipId   The id of the ship we want to repair
     * @param sectorId The id of the position where the ship starts
     *                 this turn
     * @return true if the order was canceled succesfully
     */
    public boolean cancelRepairShip(final int fleetId, final int shipId, final int sectorId) {
        final ShipDTO ship = getShipById(shipId);
        final int[] idents = new int[4];
        idents[0] = shipId;
        idents[1] = sectorId;
        idents[2] = ship.getRegionId();
        idents[3] = ship.getNationId();
        if (OrderStore.getInstance().removeOrder(ORDER_R_SHP, idents)) {
            final ShipDTO thisShip = idFleetMap.get(fleetId).getShips().get(shipId);
            thisShip.setCondition(thisShip.getOriginalCondition());
            thisShip.setMarines(thisShip.getOriginalMarines());
            UnitEventManager.changeUnit(SHIP, shipId);
            return true;
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Unexpected Problem. Could not cancel command", false);
            return false;
        }
    }

    /**
     * Remove ship rename order.
     *
     * @param shipId The ship to undo the rename.
     */
    public void undoRenameShip(final int shipId) {
        final int[] ids = new int[1];
        ids[0] = shipId;
        OrderStore.getInstance().removeOrder(ORDER_REN_SHIP, ids);
        final ShipDTO ship = getShipById(shipId);
        if (ship != null) {
            ship.setName(ship.getOriginalName());
        }
        UnitEventManager.changeUnit(SHIP, shipId);
    }

    /**
     * Rename a ship.
     *
     * @param shipId the identity of the ship.
     * @param name   the new name.
     */
    public void renameShip(final int shipId, final String name) {
        if (shipId != 0) {
            int[] ids = new int[1];
            ids[0] = shipId;
            undoRenameShip(shipId);
            final ShipDTO ship = getShipById(shipId);
            if (!name.equals(ship.getOriginalName())) {
                if (OrderStore.getInstance().addNewOrder(ORDER_REN_SHIP, CostCalculators.getShipRenameCost(), 1, name, ids, 0, "") == 1) {
                    changeShipName(shipId, name);
                    GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_REN_SHIP, ids);
                }
                UnitEventManager.changeUnit(SHIP, shipId);
            }

        }
    }

    /**
     * Change the ship name.
     *
     * @param shipId the identity of the ship.
     * @param name   the new name.
     */
    public void changeShipName(final int shipId, final String name) {
        for (final FleetDTO fleet : idFleetMap.values()) {
            if (fleet.getShips().containsKey(shipId)) {
                final ChangeShipNameOrder ccName = new ChangeShipNameOrder(fleet.getShips(), name);
                ccName.execute(shipId);
                break;
            }
        }
    }

    /**
     * Method that returns the fleets that exist on the requested tile,
     * in the specified region.
     *
     * @param position       the Position to look up.
     * @param withMovement   if the units have moved.
     * @param onlyNotScuttle only to inspect ships that have not been scuttled.
     * @return a list of fleets on the requested tile.
     */
    public List<FleetDTO> getFleetsByRegionAndTile(final PositionDTO position,
                                                   final boolean withMovement,
                                                   final boolean onlyNotScuttle) {
        final List<FleetDTO> fleetList = new ArrayList<FleetDTO>();
        FleetDTO dummyFleet = null;
        for (final FleetDTO fleet : idFleetMap.values()) {
            if (fleet.getFleetId() == 0) {
                dummyFleet = new FleetDTO();
                dummyFleet.setRegionId(position.getRegionId());
                boolean regionHasShip = false;
                for (ShipDTO ship : fleet.getShips().values()) {
                    if (ship.equals(position) && (!onlyNotScuttle || !ship.isScuttle())) {
                        regionHasShip = true;
                        dummyFleet.getShips().put(ship.getId(), ship);
                    }
                }
                // Only if the region has free ships on the specified tile add the dummy fleet
                if (regionHasShip) {
                    fleetList.add(dummyFleet);
                }
            } else {
                if (fleet.equals(position)) {
                    fleetList.add(fleet);
                }
            }
        }
        if (withMovement) {
            final List<Integer> fleetIds = MovementStore.getInstance().getFleetsFromCoords(position);
            final List<Integer> shipIds = MovementStore.getInstance().getShipsFromCoords(position);
            for (Integer fleetId : fleetIds) {
                fleetList.add(idFleetMap.get(fleetId));
            }
            for (Integer shipId : shipIds) {
                if (dummyFleet == null) {
                    dummyFleet = new FleetDTO();
                    dummyFleet.setRegionId(position.getRegionId());
                    fleetList.add(dummyFleet);
                }
                dummyFleet.getShips().put(shipId, getShipById(shipId));
            }
        }
        return fleetList;
    }

    /**
     * Method that returns the fleets of a region.
     *
     * @param regionId       the ID of the region.
     * @param onlyNotScuttle only list ships that have not been scuttled.
     * @return a list of the requested fleets.
     */

    public List<FleetDTO> getFleetsByRegion(final int regionId, final boolean onlyNotScuttle) {
        final List<FleetDTO> fleetList = new ArrayList<FleetDTO>();
        for (FleetDTO fleet : idFleetMap.values()) {
            if (fleet.getFleetId() == 0) {
                final FleetDTO dummyFleet = new FleetDTO();
                dummyFleet.setRegionId(regionId);
                boolean regionHasShip = false;
                for (ShipDTO ship : fleet.getShips().values()) {
                    if (ship.getRegionId() == regionId && (!onlyNotScuttle || !ship.isScuttle())) {
                        regionHasShip = true;
                        dummyFleet.getShips().put(ship.getId(), ship);
                    }
                }
                // Only if the region has free ships on the specified tile add the dummy fleet
                if (regionHasShip) {
                    fleetList.add(dummyFleet);
                }
            } else {
                if (fleet.getRegionId() == regionId) {
                    fleetList.add(fleet);
                }
            }
        }
        return fleetList;
    }

    /**
     * Method called when we want to re initialize the
     * data in the fleet manager
     */

    public void clearAllData() {
        dbFleetlist.clear();
        idFleetMap.clear();

    }

    /**
     * @return the dbFleetlist
     */
    public List<FleetDTO> getDbFleetlist() {
        return dbFleetlist;
    }

    /**
     * Method that returns the corresponding name of the order.
     *
     * @param orderTypeId the id of the order type.
     * @param identifier  the id of the unit.
     * @return the name of the unit.
     */
    public String getUnitName(final int orderTypeId, final int identifier) {
        switch (orderTypeId) {
            case ORDER_B_FLT:
            case ORDER_M_FLEET:
                if (idFleetMap.containsKey(identifier)) {
                    return idFleetMap.get(identifier).getName();
                } else {
                    return "Unkown fleet name";
                }

            case ORDER_B_SHIP:
                for (List<ShipDTO> shipList : barrShipMapNew.values()) {
                    for (ShipDTO ship : shipList) {
                        if (ship.getId() == identifier) {
                            return ship.getName();
                        }
                    }
                }
                break;
            case ORDER_ADDTO_FLT:
                for (FleetDTO fleet : idFleetMap.values()) {
                    if (fleet.getShips().containsKey(identifier)) {
                        return fleet.getShips().get(identifier).getName() + "-" +
                                fleet.getName();
                    }
                }
                break;
            case ORDER_HOVER_SHIP:
            case ORDER_M_SHIP:
            case ORDER_SCUTTLE_SHIP:
                for (FleetDTO fleet : idFleetMap.values()) {
                    if (fleet.getShips().containsKey(identifier)) {
                        return fleet.getShips().get(identifier).getName();
                    }
                }
                break;
            case ORDER_REN_SHIP:
                for (FleetDTO fleet : idFleetMap.values()) {
                    if (fleet.getShips().containsKey(identifier)) {
                        return fleet.getShips().get(identifier).getName();
                    }
                }
                break;

            default:
                break;
        }
        return null;
    }

    /**
     * Method that checks if the ship can trade.
     *
     * @param ship the target ship we are checking.
     * @return true if the ship can trade
     *         false otherwise.
     */
    public boolean isTradeShip(final ShipDTO ship) {
        return ship.getType().getShipClass() == 0;
    }

    /**
     * Method that returns a ship with a given id.
     *
     * @param shipId the id of the ship.
     * @return the ship or null when the ship is not
     *         found.
     */
    public ShipDTO getShipById(final int shipId) {
        if (idShipMap.containsKey(shipId)) {
            return idShipMap.get(shipId);
        }

        return null;
    }

    public FleetDTO getFleetById(final int fleetId) {
        return idFleetMap.get(fleetId);
    }

    /**
     * Method that checks if there are ships that can be boarded on this
     * position.
     *
     * @param position       the tile we are checking.
     * @param onlyNotScuttle if the ship is going to be scuttled.
     * @return true if there are.
     */
    public boolean hasShipsToEmbark(final PositionDTO position, final boolean onlyNotScuttle) {
        final List<FleetDTO> tileFleets = getFleetsByRegion(position.getRegionId(), onlyNotScuttle);
        for (FleetDTO fleet : tileFleets) {
            if (fleet.getFleetId() == 0) {
                for (ShipDTO ship : fleet.getShips().values()) {
                    final PositionDTO pos = MovementStore.getInstance().getUnitPosition(SHIP, ship.getId(), ship);
                    if (pos.equals(position)) {
                        return true;
                    }
                }
            } else {
                final PositionDTO pos = MovementStore.getInstance().getUnitPosition(FLEET, fleet.getFleetId(), fleet);
                if (pos.equals(position)) {
                    return true;
                }

            }
        }

        return false;
    }

    public boolean hasWarShips(final SectorDTO sector, final boolean onlyNotScuttle) {
        final List<FleetDTO> tileFleets = getFleetsByRegionAndTile(sector, false, onlyNotScuttle);
        NavyUnitInfoDTO fleetInfo;
        for (FleetDTO fleetDTO : tileFleets) {
            fleetInfo = MiscCalculators.getFleetInfo(fleetDTO);
            if (fleetInfo.getWarShips() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMerchantShips(final SectorDTO sector, final boolean onlyNotScuttle) {
        final List<FleetDTO> tileFleets = getFleetsByRegionAndTile(sector, false, onlyNotScuttle);
        NavyUnitInfoDTO fleetInfo;
        for (FleetDTO fleetDTO : tileFleets) {
            fleetInfo = MiscCalculators.getFleetInfo(fleetDTO);
            if (fleetInfo.getMerchantShips() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the isInitialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * @return the isClient
     */
    public boolean isClient() {
        return isClient;
    }

    /**
     * @param isInitialized the isInitialized to set
     */
    public void setInitialized(final boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    /**
     * @param isClient the isClient to set
     */
    public void setClient(final boolean isClient) {
        this.isClient = isClient;
    }

    public List<ShipTypeDTO> getShipTypesList() {
        return shipTypesList;
    }

    public ShipTypeDTO getShipTypeByID(final int typeId) {
        for (ShipTypeDTO shipType : shipTypesList) {
            if (shipType.getTypeId() == typeId) {
                return shipType;
            }
        }
        return null;
    }

    public ShipTypeDTO getShipTypeByIntId(final int intId) {
        for (ShipTypeDTO shipType : shipTypesList) {
            if (shipType.getIntId() == intId) {
                return shipType;
            }
        }
        return null;
    }

    public Map<Integer, List<ShipDTO>> getBarrShipMap() {
        return barrShipMap;
    }

    public Map<Integer, List<ShipDTO>> getBarrShipMapNew() {
        return barrShipMapNew;
    }

    public List<TransportUnitDTO> getShipsByRegion(final int regionId, final boolean onlyNotScuttle) {
        final List<TransportUnitDTO> shipList = new ArrayList<TransportUnitDTO>();
        for (FleetDTO fleet : idFleetMap.values()) {
            if (fleet.getFleetId() == 0 || fleet.getRegionId() == regionId) {
                for (ShipDTO ship : fleet.getShips().values()) {
                    if (ship.getRegionId() == regionId && (!onlyNotScuttle || !ship.isScuttle())) {
                        shipList.add(ship);
                    }
                }
            }
        }
        return shipList;
    }

    /**
     * Get all ships by sector.
     *
     * @param sector         The sector to search for ships.
     * @param onlyNotScuttle If we want only not scuttle ships.
     * @return A list of ships.
     */
    public List<TransportUnitDTO> getShipsBySector(final SectorDTO sector, final boolean onlyNotScuttle) {
        final List<TransportUnitDTO> shipList = new ArrayList<TransportUnitDTO>();
        for (FleetDTO fleet : idFleetMap.values()) {
            if (fleet.getFleetId() == 0 || fleet.equals(sector)) {
                for (ShipDTO ship : fleet.getShips().values()) {
                    if (ship.equals(sector) && (!onlyNotScuttle || !ship.isScuttle())) {
                        shipList.add(ship);
                    }
                }
            }
        }
        return shipList;
    }

    public boolean canTradeWithOtherShip(final SectorDTO sector,
                                         final int shipId,
                                         final boolean onlyNotScuttle) {
        final ShipDTO thisShip = getShipById(shipId);
        return thisShip.equals(sector)
                && (!onlyNotScuttle || !thisShip.isScuttle())
                && isTradeShip(thisShip);
    }

    /**
     * Method that loads a good in a ship
     *
     * @param goodId the id of the good
     * @param id     the id of the ship
     * @param qte    the quantity of the goods we want to load
     */
    public void loadGood(final int goodId, final int id, final int qte) {
        getShipById(id).getGoodsDTO().get(goodId).setQte(getShipById(id).getGoodsDTO().get(goodId).getQte() + qte);
    }

    /**
     * Method that loads a good in a ship
     *
     * @param goodId the id of the good
     * @param id     the id of the ship
     * @param qte    the quantity of the goods we want to load
     */
    public void unLoadGood(final Integer goodId, final int id, final int qte) {
        getShipById(id).getGoodsDTO().get(goodId).setQte(getShipById(id).getGoodsDTO().get(goodId).getQte() - qte);
    }

    /**
     * Method that loads a unit on a fleet without setting
     * any other information. On client side use LoadUnit
     *
     * @param fleetId   the id of the fleet
     * @param cargoType the type of the cargo
     * @param cargoId   the id of the cargo
     */
    public void loadUnitOnFleet(final int fleetId, final int cargoType, final int cargoId) {
        final TransportUnitDTO tUnit = idFleetMap.get(fleetId);
        if (tUnit != null) {
            // Make sure that a list exists for the specific cargo type
            if (!tUnit.getLoadedUnitsMap().containsKey(cargoType)) {
                tUnit.getLoadedUnitsMap().put(cargoType, new ArrayList<Integer>());
            }
            tUnit.getLoadedUnitsMap().get(cargoType).add(cargoId);
        }
    }

    /**
     * Method that loads a unit on a ship without setting
     * any other information. On client side use LoadUnit
     *
     * @param shipId    the id of the ship
     * @param cargoType the type of the cargo
     * @param cargoId   the id of the cargo
     */
    public void loadUnitOnShip(final int shipId, final int cargoType, final int cargoId) {
        final ShipDTO tUnit = getShipById(shipId);
        if (tUnit != null) {
            if (!tUnit.getLoadedUnitsMap().containsKey(cargoType)) {
                tUnit.getLoadedUnitsMap().put(cargoType, new ArrayList<Integer>());
            }
            tUnit.getLoadedUnitsMap().get(cargoType).add(cargoId);
        }
    }

    /**
     * Method that returns a list of sectors with ships on them.
     *
     * @param regionId       the target region.
     * @param onlyNotScuttle list ships that have not been scuttled.
     * @return the List of sectors with ships.
     */
    public List<SectorDTO> getRegionSectorsWithFleets(final int regionId, final boolean onlyNotScuttle) {
        final Set<SectorDTO> fleetSectors = new HashSet<SectorDTO>();
        for (FleetDTO fleet : getFleetsByRegion(regionId, onlyNotScuttle)) {
            if (fleet.getId() == 0) {
                for (ShipDTO ship : fleet.getShips().values()) {
                    final SectorDTO sec = RegionStore.getInstance().getSectorByPosition(ship);
                    if (!fleetSectors.contains(sec)) {
                        fleetSectors.add(sec);
                    }
                }
            } else {
                final SectorDTO sec = RegionStore.getInstance().getSectorByPosition(fleet);
                fleetSectors.add(sec);

            }
        }

        return (new ArrayList<SectorDTO>(fleetSectors));
    }

    /**
     * Moves a unit to the specified position
     *
     * @param type the type of the unit
     * @param id   the id of the unit
     * @param pos  the specified position
     */
    public void moveUnitByTypeToNewPosition(final int type, final int id, final PositionDTO pos) {
        PositionDTO oldPos = null;
        switch (type) {
            case FLEET:
                oldPos = idFleetMap.get(id);
                if (oldPos != null) {
                    for (final ShipDTO ship : ((FleetDTO) oldPos).getShips().values()) {
                        moveUnitByTypeToNewPosition(SHIP, ship.getId(), pos);
                    }
                }
                break;

            case SHIP:
                oldPos = getShipById(id);
                break;

            default:
                // do nothing
        }

        if (oldPos != null) {
            oldPos.setX(pos.getX());
            oldPos.setY(pos.getY());
        }
    }

    /**
     * Method that returns the nation to whom the fleet belongs.
     *
     * @param fleet the fleet object.
     * @return the nation ID.
     */
    public int getNationIdFromFleet(final FleetDTO fleet) {
        int nationId = GameStore.getInstance().getNationId();
        if (!fleet.getShips().isEmpty()) {
            nationId = fleet.getShips().values().iterator().next().getNationId();
        }
        return nationId;
    }

    public List<ShipDTO> getShipsInFleets(final List<FleetDTO> fleets,
                                          final boolean merchant,
                                          final boolean war) {
        final List<ShipDTO> out = new ArrayList<ShipDTO>();
        for (FleetDTO fleet : fleets) {
            try {
                for (final ShipDTO ship : fleet.getShips().values()) {
                    if (isTradeShip(ship) && merchant) {
                        out.add(ship);
                    }
                    if (!isTradeShip(ship) && war) {
                        out.add(ship);
                    }
                }

            } catch (Exception e) {
                // eat it
            }
        }

        return out;
    }

    public boolean hasMerchantShips(final FleetDTO fleet) {
        for (ShipDTO ship : fleet.getShips().values()) {
            if (isTradeShip(ship)) {
                return true;
            }
        }
        return false;
    }

    public int getNewShipId() {
        newShipId--;
        return newShipId;
    }
}
