package com.eaw1805.www.controllers.remote.hotspot.navy;

import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.controllers.remote.EmpireRpcServiceImpl;
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
public class ShipSaveChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    private transient final Map<Integer, ShipDTO> dbShips = new HashMap<Integer, ShipDTO>(), chShips = new HashMap<Integer, ShipDTO>();

    private transient final Map<Integer, List<ShipDTO>> newShipMap = new HashMap<Integer, List<ShipDTO>>();

    private transient final EmpireRpcServiceImpl service;

    private transient final Map<Integer, List<ClientOrderDTO>> orderMap;

    /**
     * Default constructor.
     *
     * @param gameId               the game of the order.
     * @param nationId             the owner of the order.
     * @param turn                 the turn of the order.
     * @param orderMap             the orders.
     * @param empireRpcServiceImpl the calling remote interface.
     */
    public ShipSaveChangesProcessor(final int thisScenario, final int gameId,
                                    final int nationId,
                                    final int turn,
                                    final Map<Integer, List<ClientOrderDTO>> orderMap,
                                    final EmpireRpcServiceImpl empireRpcServiceImpl) {
        super(thisScenario, gameId, nationId, turn);
        this.orderMap = orderMap;
        this.service = empireRpcServiceImpl;
    }

    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        // do nothing
    }

    @SuppressWarnings({"restriction", "unchecked"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        dbShips.putAll((Map<Integer, ShipDTO>) dbData);
        chShips.putAll((Map<Integer, ShipDTO>) chData);
    }

    public void addNewShipData(final Map<Integer, List<ShipDTO>> newShipData) {
        newShipMap.putAll(newShipData);
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> shipsOrders = new ArrayList<OrderDTO>();
        final List<ClientOrderDTO> scutOrders = orderMap.get(ORDER_SCUTTLE_SHIP);
        for (ShipDTO dbShip : dbShips.values()) {
            //save scuttle ship orders
            if (scutOrders != null) {
                for (final ClientOrderDTO order : scutOrders) {
                    if (order.getIdentifier(0) == dbShip.getId()) {
                        shipsOrders.add(createOrderScuttle(dbShip, order));
                        break;
                    }
                }
            }

            // Check if the ship exists in the new ship table
            if (chShips.containsKey(dbShip.getId())) {
                // If it does check if the ship has the same name
                final String name = chShips.get(dbShip.getId()).getName();
                if (!name.equals(dbShip.getName())) {
                    //If it does not add a new change ship name order
                    final List<ClientOrderDTO> orders = orderMap.get(ORDER_REN_SHIP);
                    if (orders != null) {
                        for (final ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == dbShip.getId()) {
                                final OrderDTO changeShipName = new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_REN_SHIP, order.getPriority(), 0, String.valueOf(dbShip.getId()), name, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                                shipsOrders.add(changeShipName);
                                break;
                            }
                        }
                    }
                }

                // Check if we have repaired the ship in this round
                if (chShips.get(dbShip.getId()).getCondition() != dbShip.getCondition()) {
                    final List<ClientOrderDTO> orders = orderMap.get(ORDER_R_SHP);
                    if (orders != null) {
                        for (final ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == dbShip.getId()) {
                                final SectorDTO sector = service.getSectorByCoordinates(getScenarioId(), getGameId(), dbShip.getRegionId(), dbShip.getX(), dbShip.getY());
                                shipsOrders.add(createOrderRepair(dbShip, order, sector));
                                break;
                            }
                        }
                    }
                }
            }
        }

        for (final ShipDTO chShip : chShips.values()) {
            // Check if the ship exists in the old fleet table
            if (dbShips.containsKey(chShip.getId())) {
                //If it does check if the Ship has changed fleet
                if (chShip.getFleet() != dbShips.get(chShip.getId()).getFleet()) {
                    final List<ClientOrderDTO> orders = orderMap.get(ORDER_ADDTO_FLT);
                    if (orders != null) {
                        for (final ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == chShip.getId()) {
                                shipsOrders.add(createOrderAdd(chShip, order));
                                break;
                            }
                        }
                    }
                }

                // Now check if we want to hand over the ship to an ally
                if (chShip.gethOverNationId() != 0) {
                    final List<ClientOrderDTO> orders = orderMap.get(ORDER_HOVER_SHIP);
                    if (orders != null) {
                        for (final ClientOrderDTO order : orders) {
                            if (order.getIdentifier(0) == chShip.getId()) {
                                shipsOrders.add(createOrderHandover(chShip));
                                break;
                            }
                        }
                    }
                }
            }
        }

        // Add commands for new ships
        for (final int sectorId : newShipMap.keySet()) {
            for (final ShipDTO newShip : newShipMap.get(sectorId)) {
                final List<ClientOrderDTO> orders = orderMap.get(ORDER_B_SHIP);
                if (orders != null) {
                    for (final ClientOrderDTO order : orders) {
                        if (order.getIdentifier(0) == newShip.getId()) {
                            shipsOrders.add(createOrderBuild(sectorId, newShip, order));
                            break;
                        }
                    }
                }
            }
        }

        final List<ClientOrderDTO> orders = orderMap.get(ORDER_R_SHP);

        //save repair orders for allied fleets
        if (orders != null) {
            for (final ClientOrderDTO order : orders) {
                if (order.getIdentifier(3) != getNationId()) {
                    shipsOrders.add(createOrderRepairA(order));
                }
            }
        }

        return shipsOrders;
    }

    private OrderDTO createOrderRepairA(final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_R_SHP, order.getPriority(), 0,
                String.valueOf(order.getIdentifier(0)),
                String.valueOf(order.getIdentifier(1)),
                String.valueOf(order.getIdentifier(2)),
                String.valueOf(order.getIdentifier(3))
                , "", "", "", "", "",
                String.valueOf(String.valueOf(order.getIdentifier(2)))
                , "", "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderBuild(final int sectorId, final ShipDTO newShip, final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_B_SHIP, order.getPriority(), 0,
                String.valueOf(sectorId),
                String.valueOf(newShip.getType().getIntId()),
                newShip.getName(), "", "", "", "", "", "", String.valueOf(newShip.getId()), "", "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderHandover(final ShipDTO chShip) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_HOVER_SHIP, 0, 0,
                String.valueOf(chShip.getId()),
                String.valueOf(chShip.gethOverNationId()), "", "", "", "", "", "", "", "0", "", "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderAdd(final ShipDTO chShip, final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_ADDTO_FLT, order.getPriority(), 0,
                String.valueOf(chShip.getId()),
                String.valueOf(chShip.getFleet()), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderRepair(final ShipDTO dbShip, final ClientOrderDTO order, final SectorDTO sector) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_R_SHP, order.getPriority(), 0,
                String.valueOf(dbShip.getId()),
                String.valueOf(sector.getId()),
                String.valueOf(order.getIdentifier(2)),
                String.valueOf(order.getIdentifier(3)),
                "", "", "", "", "",
                String.valueOf(dbShip.getRegionId())
                , "", "", "", "", "", "", "", "");
    }

    private OrderDTO createOrderScuttle(final ShipDTO dbShip, final ClientOrderDTO order) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_SCUTTLE_SHIP, order.getPriority(), 0,
                String.valueOf(dbShip.getId()), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

}
