package com.eaw1805.www.controllers.remote.hotspot;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.shared.stores.units.TransportStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpyApplyChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, OrderConstants, ArmyConstants {

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_LOAD_TROOPSF,
            ORDER_LOAD_TROOPSS,
            ORDER_UNLOAD_TROOPSF,
            ORDER_UNLOAD_TROOPSS,
            ORDER_M_SPY,
            ORDER_M_UNIT
    };

    /**
     * A list containing all the spies of our target nation.
     */
    private transient List<SpyDTO> spyList = new ArrayList<SpyDTO>();

    /**
     * A hash map containing all the spy Ids and their corresponding spies.
     */
    private transient Map<Integer, SpyDTO> spyMap = new HashMap<Integer, SpyDTO>();

    @SuppressWarnings({"restriction"})
    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();

    public SpyApplyChangesProcessor(final int thisScenario, final int gameId, final int nationId, final int turn) {
        super(thisScenario, gameId, nationId, turn);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(Collection<?> dbData, Collection<?> chData) {
        spyList = (List<SpyDTO>) dbData;
        for (SpyDTO spy : spyList) {
            spyMap.put(spy.getSpyId(), spy);
        }

        orders = (List<OrderDTO>) chData;
    }

    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        // do nothing here
    }

    public List<SpyDTO> processChanges() {
        for (OrderDTO order : orders) {
            switch (order.getType()) {
                case ORDER_LOAD_TROOPSF:
                case ORDER_LOAD_TROOPSS:
                    final int thisType = Integer.parseInt(order.getParameter3());
                    if (thisType == SPY) {
                        getSpyById(Integer.parseInt(order.getParameter4())).setLoaded(true);
                    }
                    break;

                case ORDER_UNLOAD_TROOPSF:
                case ORDER_UNLOAD_TROOPSS:
                    final int thatType = Integer.parseInt(order.getParameter3());
                    if (thatType == SPY) {
                        final SpyDTO spy = getSpyById(Integer.parseInt(order.getParameter4()));
                        spy.setLoaded(false);

                        final int[] xy = TransportStore.getCoordsByDirection(Integer.parseInt(order.getParameter5()),
                                Integer.parseInt(order.getTemp1()),
                                Integer.parseInt(order.getTemp2()));
                        spy.setXStart(xy[0]);
                        spy.setYStart(xy[1]);
                        spy.setX(xy[0]);
                        spy.setY(xy[1]);
                    }
                    break;

                case ORDER_M_SPY:
                case ORDER_M_UNIT:
                    final int unitType = Integer.parseInt(order.getParameter1());
                    if (order.getType() == ORDER_M_SPY || unitType == SPY) {
                        final PositionDTO pos = getLastPositionByString(order.getParameter3());
                        moveUnitByTypeToNewPosition(Integer.parseInt(order.getParameter1()),
                                Integer.parseInt(order.getParameter2()), pos);
                    }
                    break;

                default:
                    // do nothing
            }
        }

        return spyList;
    }

    /**
     * Retrieve spy object based on identity.
     *
     * @param id the identity of the spy object.
     * @return the spy object, or null if not found.
     */
    private SpyDTO getSpyById(final int id) {
        if (spyMap.containsKey(id)) {
            return spyMap.get(id);
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
    public PositionDTO getLastPositionByString(final String movementString) {
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
    public void moveUnitByTypeToNewPosition(final int type, final int id, final PositionDTO pos) {
        PositionDTO oldPos = null;
        if (type == SPY) {
            oldPos = getSpyById(id);
        }

        if (oldPos != null) {
            oldPos.setX(pos.getX());
            oldPos.setY(pos.getY());
        }
    }

}
