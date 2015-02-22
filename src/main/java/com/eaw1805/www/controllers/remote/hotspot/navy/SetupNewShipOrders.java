package com.eaw1805.www.controllers.remote.hotspot.navy;

import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.data.model.map.Sector;
import com.eaw1805.www.controllers.remote.EmpireRpcServiceImpl;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupNewShipOrders
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_B_SHIP
    };

    private transient final Map<Integer, List<ShipDTO>> newShips = new HashMap<Integer, List<ShipDTO>>();

    private transient final Map<Integer, ShipTypeDTO> shipTypes = new HashMap<Integer, ShipTypeDTO>();

    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();

    private transient final EmpireRpcServiceImpl service;

    private int regionId;

    /**
     * Default constructor.
     *
     * @param thisGame             the game of the order.
     * @param thisNation           the owner of the order.
     * @param thisTurn             the turn of the order.
     * @param empireRpcServiceImpl the remote service end point.
     */
    public SetupNewShipOrders(final int thisScenario, final int thisGame,
                              final int thisNation,
                              final int thisTurn,
                              final EmpireRpcServiceImpl empireRpcServiceImpl) {
        super(thisScenario, thisGame, thisNation, thisTurn);
        this.service = empireRpcServiceImpl;
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chOrders) {
        for (ShipTypeDTO shipTypeDTO : (List<ShipTypeDTO>) dbData) {
            shipTypes.put(shipTypeDTO.getIntId(), shipTypeDTO);
        }

        orders = (List<OrderDTO>) chOrders;
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        newShips.putAll((Map<Integer, List<ShipDTO>>) dbData);
    }

    public List<Map<Integer, List<ShipDTO>>> processChanges() {
        final List<Map<Integer, List<ShipDTO>>> dummyList = new ArrayList<Map<Integer, List<ShipDTO>>>();
        int currentId = 0;
        for (final OrderDTO order : orders) {
            currentId--;
            final ShipDTO newShip = new ShipDTO();

            newShip.setNationId(getNationId());
            newShip.setName(order.getParameter3());
            if (order.getTurn() == getTurn()) {//the new orders should be first in the list...
                newShip.setId(Integer.parseInt(order.getTemp1()));
                newShip.setJustUnderConstruction(true);
            } else {//so assign ids greater than the greater id of the new orders.
                newShip.setId(Integer.MIN_VALUE - currentId);
                newShip.setJustUnderConstruction(false);
            }

            final ShipTypeDTO shipTypeDTO = getShipTypeId(Integer.parseInt(order.getParameter2()));
            newShip.setTypeId(shipTypeDTO.getIndPt());
            newShip.setType(shipTypeDTO);

            final Sector sector = service.getSectorManager().getByID(Integer.parseInt(order.getParameter1()));
            newShip.setRegionId(sector.getPosition().getRegion().getId());
            newShip.setX(sector.getPosition().getX());
            newShip.setY(sector.getPosition().getY());

            //done checking for conflicts
            if (!newShips.containsKey(sector.getId())) {
                newShips.put(sector.getId(), new ArrayList<ShipDTO>());
            }
            newShips.get(sector.getId()).add(newShip);
        }

        dummyList.add(newShips);
        return dummyList;
    }

    /**
     * @param shipTypeID the type of the ship.
     * @return the new ship type dto.
     */
    private ShipTypeDTO getShipTypeId(final int shipTypeID) {
        final ShipTypeDTO shipTypeDto = new ShipTypeDTO();
        if (shipTypes.containsKey(shipTypeID)) {
            return shipTypes.get(shipTypeID);
        }
        return shipTypeDto;
    }

    public void setRegionId(final int regionId) {
        this.regionId = regionId;
    }

    public int getRegionId() {
        return regionId;
    }

}
