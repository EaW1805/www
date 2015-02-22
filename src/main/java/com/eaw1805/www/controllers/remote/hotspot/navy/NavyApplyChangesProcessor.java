package com.eaw1805.www.controllers.remote.hotspot.navy;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;
import com.eaw1805.www.shared.orders.navy.ChangeFleetNameOrder;
import com.eaw1805.www.shared.orders.navy.ChangeShipFleetOrder;
import com.eaw1805.www.shared.orders.navy.CreateFleetOrder;
import com.eaw1805.www.shared.orders.navy.DeleteFleetOrder;
import com.eaw1805.www.shared.orders.navy.HandOverShipOrder;
import com.eaw1805.www.shared.orders.navy.ScuttleShipOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NavyApplyChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, ArmyConstants {

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_B_FLT,
            ORDER_D_FLT,
            ORDER_ADDTO_FLT,
            ORDER_REN_FLT,
            ORDER_REN_SHIP,
            ORDER_HOVER_SHIP,
            ORDER_R_FLT,
            ORDER_R_SHP,
            ORDER_LOAD_TROOPSF,
            ORDER_LOAD_TROOPSS,
            ORDER_UNLOAD_TROOPSF,
            ORDER_UNLOAD_TROOPSS,
            ORDER_SCUTTLE_SHIP,
            ORDER_M_UNIT,
            ORDER_M_FLEET,
            ORDER_M_SHIP,
            ORDER_P_FLEET,
            ORDER_P_SHIP

    };

    public static final Object[] ORDERS_TYPES_ALLIED = {
            ORDER_R_FLT,
            ORDER_R_SHP
    };

    public static final Object[] ORDERS_TYPES_CURRENT_FOR_ALLIES = {
            ORDER_LOAD_TROOPSF,
            ORDER_LOAD_TROOPSS,
            ORDER_UNLOAD_TROOPSF,
            ORDER_UNLOAD_TROOPSS
    };

    /**
     * A list containing all the available fleets as taken from the service
     */
    private transient List<FleetDTO> dbFleetlist = new ArrayList<FleetDTO>();

    /**
     * A hash map containing all the fleet ids and their corresponding objects
     */
    private transient final Map<Integer, FleetDTO> idFleetMap = new HashMap<Integer, FleetDTO>();

    private transient final Map<Integer, FleetDTO> idDeletedFleetMap = new HashMap<Integer, FleetDTO>();

    /**
     * A hash map containing all the ship ids and their corresponding objects
     */
    private transient final Map<Integer, ShipDTO> idShipMap = new HashMap<Integer, ShipDTO>();

    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();

    /**
     * Default constructor.
     *
     * @param thisGame   the game of the order.
     * @param thisNation the owner of the order.
     * @param thisTurn   the turn of the order.
     */
    public NavyApplyChangesProcessor(final int thisScenario, final int thisGame, final int thisNation, final int thisTurn) {
        super(thisScenario, thisGame, thisNation, thisTurn);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chOrders) {
        // clear any previous additions to collections
        dbFleetlist = (List<FleetDTO>) dbData;
        for (final FleetDTO fleet : dbFleetlist) {
            // Fleet with id -1 is a dummy fleet and
            // we don't want it in our data
            // If there is no such fleet in our map
            if (fleet.getFleetId() != -1 && !idFleetMap.containsKey(fleet.getFleetId())) {
                // Make one.
                idFleetMap.put(fleet.getFleetId(), fleet);

                // Add all fleet ships
                for (final ShipDTO ship : fleet.getShips().values()) {
                    idShipMap.put(ship.getId(), ship);
                }
            }
        }

        orders = (List<OrderDTO>) chOrders;
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        // do nothing
    }

    public List<FleetDTO> processChanges() {
        for (OrderDTO order : orders) {
            switch (order.getType()) {
                case ORDER_B_FLT: {
                    execOrderBuild(order);
                    break;
                }

                case ORDER_D_FLT: {
                    execOrderDel(order);
                    break;
                }

                case ORDER_ADDTO_FLT: {
                    execOrderAdd(order);
                    break;
                }

                case ORDER_REN_FLT: {
                    execOrderRename(order);
                    break;
                }

                case ORDER_REN_SHIP: {
                    final int shipId = Integer.parseInt(order.getParameter1());
                    if (idShipMap.containsKey(shipId)) {
                        idShipMap.get(shipId).setName(order.getParameter2());
                    }
                    break;
                }

                case ORDER_HOVER_SHIP: {
                    execOrderHandover(order);
                    break;
                }

                case ORDER_R_FLT:
                    if (Integer.parseInt(order.getParameter4()) == getNationId()) {
                        final int fleetId = Integer.parseInt(order.getParameter1());
                        final FleetDTO fleet = idFleetMap.get(fleetId);
                        //when setting up navy for allied nations because we do this by region
                        //this fleet will be null in 3 out of 4 regions.
                        if (fleet != null) {
                            for (ShipDTO ship : fleet.getShips().values()) {
                                //update the ships status to 100% since it gets repaired
                                ship.setCondition(100);
                                ship.setMarines(ship.getType().getCitizens());
                            }
                        }
                    }
                    break;

                case ORDER_R_SHP: {
                    if (Integer.parseInt(order.getParameter4()) == getNationId()) {
                        final int shipId = Integer.parseInt(order.getParameter1());
                        final ShipDTO ship = getShipById(shipId);
                        //when setting up navy for allied nations because we do this by region
                        //this ship will be null in 3 out of 4 regions.
                        if (ship != null) {
                            ship.setCondition(100);
                            ship.setMarines(ship.getType().getCitizens());
                        }
                    }
                    break;
                }

                case ORDER_LOAD_TROOPSF:
                case ORDER_LOAD_TROOPSS:
                    switch (Integer.parseInt(order.getParameter1())) {
                        case FLEET:
                            loadUnitOnFleet(Integer.parseInt(order.getParameter2()),
                                    Integer.parseInt(order.getParameter3()),
                                    Integer.parseInt(order.getParameter4()));
                            break;

                        case SHIP:
                            loadUnitOnShip(Integer.parseInt(order.getParameter2()),
                                    Integer.parseInt(order.getParameter3()),
                                    Integer.parseInt(order.getParameter4()));
                            break;

                        default:
                            // do nothing
                    }
                    break;

                case ORDER_UNLOAD_TROOPSF:
                case ORDER_UNLOAD_TROOPSS:
                    switch (Integer.parseInt(order.getParameter1())) {
                        case FLEET:
                            unloadUnitOnFleet(Integer.parseInt(order.getParameter2()),
                                    Integer.parseInt(order.getParameter3()),
                                    Integer.parseInt(order.getParameter4()));
                            break;

                        case SHIP:
                            unloadUnitOnShip(Integer.parseInt(order.getParameter2()),
                                    Integer.parseInt(order.getParameter3()),
                                    Integer.parseInt(order.getParameter4()));
                            break;

                        default:
                            // do nothing
                    }
                    break;

                case ORDER_SCUTTLE_SHIP: {
                    execOrderScuttle(order);
                    break;
                }

                case ORDER_M_FLEET:
                case ORDER_M_SHIP:
                case ORDER_M_UNIT:
                    final int unitType = Integer.parseInt(order.getParameter1());
                    if (order.getType() != ORDER_M_UNIT || (unitType == FLEET || unitType == SHIP)
                            && !order.getParameter3().isEmpty()) {
                        final PositionDTO pos = getLastPositionByString(order.getParameter3());
                        moveUnitByTypeToNewPosition(Integer.parseInt(order.getParameter1()),
                                Integer.parseInt(order.getParameter2()), pos);
                    }
                case ORDER_P_FLEET:
                case ORDER_P_SHIP:
                    // Patrol orders need not move the ship or fleet
                    break;

                default:
                    // do nothing
            }
        }

        return dbFleetlist;
    }

    private void execOrderBuild(final OrderDTO order) {
        final int fleetId = Integer.parseInt(order.getParameter1());
        if (!idFleetMap.containsKey(fleetId)) {
            final CreateFleetOrder cfOrder = new CreateFleetOrder(idFleetMap,
                    order.getParameter2(),
                    null,
                    Integer.parseInt(order.getParameter4()),
                    Integer.parseInt(order.getParameter5()),
                    Integer.parseInt(order.getParameter6()),
                    getNationId());
            cfOrder.execute(fleetId);
            dbFleetlist.add(idFleetMap.get(fleetId));
        }
    }

    private void execOrderDel(final OrderDTO order) {
        final int fleetId = Integer.parseInt(order.getParameter1());

        if (idFleetMap.containsKey(fleetId) &&//be sure the fleet is in the map.. not sure for allied units
                !(idFleetMap.get(fleetId).getShips().values().size() > 0)) {
            final DeleteFleetOrder dfOrder = new DeleteFleetOrder(idFleetMap, fleetId, idDeletedFleetMap);
            dfOrder.execute(fleetId);
            final Iterator<FleetDTO> iter = dbFleetlist.iterator();
            while (iter.hasNext()) {
                if (iter.next().getFleetId() == fleetId) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    private void execOrderAdd(final OrderDTO order) {
        final int newFleetId = Integer.parseInt(order.getParameter2());
        final int shipId = Integer.parseInt(order.getParameter1());
        if (newFleetId == 0) {//check if the dummy fleet is initialized in the map
            if (!idFleetMap.containsKey(0)) {
                FleetDTO dummyFleet = new FleetDTO();
                dummyFleet.setName("Free Ships");
                dummyFleet.setOriginalName("Free Ships");
                dummyFleet.setFleetId(0);
                dummyFleet.setNationId(getGameId());
                dummyFleet.setShips(new HashMap<Integer, ShipDTO>());
                idFleetMap.put(0, dummyFleet);
                dbFleetlist.add(dummyFleet);
            }
        }
        final ChangeShipFleetOrder csfOrder = new ChangeShipFleetOrder(idFleetMap, newFleetId);
        csfOrder.execute(shipId);

        //now check source fleet
        final FleetDTO source = csfOrder.getSource();
        if (source != null && source.getShips().isEmpty()) {//if the fleet has no more ships then disband it.
            final DeleteFleetOrder dfOrder = new DeleteFleetOrder(idFleetMap, source.getFleetId(), idDeletedFleetMap);
            dfOrder.execute(source.getFleetId());
            final Iterator<FleetDTO> iter = dbFleetlist.iterator();
            while (iter.hasNext()) {
                if (iter.next().getFleetId() == source.getFleetId()) {
                    iter.remove();
                    break;
                }
            }
        }

    }

    private void execOrderRename(final OrderDTO order) {
        final int fleetId = Integer.parseInt(order.getParameter1());
        final ChangeFleetNameOrder cfnOrder = new ChangeFleetNameOrder(idFleetMap, order.getParameter2());
        cfnOrder.execute(fleetId);
    }

    private void execOrderHandover(final OrderDTO order) {
        final int shipId = Integer.parseInt(order.getParameter1());
        final HandOverShipOrder cfnOrder = new HandOverShipOrder(idShipMap, Integer.parseInt(order.getParameter2()));
        cfnOrder.execute(shipId);
    }

    private void execOrderScuttle(final OrderDTO order) {
        final int shipId = Integer.parseInt(order.getParameter1());
        final ScuttleShipOrder sbtOrder = new ScuttleShipOrder(idShipMap, true);
        sbtOrder.execute(shipId);
    }

    public Map<Integer, ShipDTO> getIdShipMap() {
        return idShipMap;
    }

    public Map<Integer, FleetDTO> getIdDeletedFleetMap() {
        return idDeletedFleetMap;
    }

    /**
     * Method that returns a ship with a given id.
     *
     * @param shipId the id of the ship.
     * @return the ship or null when the ship is not
     *         found.
     */
    private ShipDTO getShipById(final int shipId) {
        if (idShipMap.containsKey(shipId)) {
            return idShipMap.get(shipId);
        }

        return null;
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
     * Moves a unit to the specified position
     *
     * @param type the type of the unit
     * @param id   the id of the unit
     * @param pos  the specified position
     */
    private void moveUnitByTypeToNewPosition(final int type, final int id, final PositionDTO pos) {
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
     * Method that loads a unit on a fleet without setting
     * any other information. On client side use LoadUnit
     *
     * @param fleetId   the id of the fleet
     * @param cargoType the type of the cargo
     * @param cargoId   the id of the cargo
     */
    private void loadUnitOnFleet(final int fleetId, final int cargoType, final int cargoId) {
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
    private void loadUnitOnShip(final int shipId, final int cargoType, final int cargoId) {
        final ShipDTO tUnit = getShipById(shipId);
        if (tUnit != null) {
            if (!tUnit.getLoadedUnitsMap().containsKey(cargoType)) {
                tUnit.getLoadedUnitsMap().put(cargoType, new ArrayList<Integer>());
            }
            tUnit.getLoadedUnitsMap().get(cargoType).add(cargoId);
        }
    }

    /**
     * Method that unloads a unit from a fleet without setting
     * any other information. On client side use UnLoadUnit
     *
     * @param fleetId   the id of the fleet
     * @param cargoType the type of the cargo
     * @param cargoId   the id of the cargo
     */
    private void unloadUnitOnFleet(final int fleetId, final int cargoType, final Integer cargoId) {
        final TransportUnitDTO tUnit = idFleetMap.get(fleetId);
        if (tUnit != null) {
            if (tUnit.getLoadedUnitsMap().containsKey(cargoType)) {
                tUnit.getLoadedUnitsMap().get(cargoType).remove(cargoId);
            }
        }
    }

    /**
     * Method that unloads a unit on a ship without setting
     * any other information. On client side use UnLoadUnit
     *
     * @param shipId    the id of the ship
     * @param cargoType the type of the cargo
     * @param cargoId   the id of the cargo
     */
    private void unloadUnitOnShip(final int shipId, final int cargoType, final Integer cargoId) {
        final ShipDTO tUnit = getShipById(shipId);
        if (tUnit != null) {
            tUnit.getLoadedUnitsMap().get(cargoType).remove(cargoId);
        }
    }

}
