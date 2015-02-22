package com.eaw1805.www.controllers.remote.hotspot.movement;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.movement.MovementDTO;
import com.eaw1805.data.dto.web.movement.PathDTO;
import com.eaw1805.data.dto.web.movement.PathSectorDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MovementSaveChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, ArmyConstants, OrderConstants {


    private static final Logger LOGGER = LogManager.getLogger(MovementSaveChangesProcessor.class);

    private transient Map<Integer, Map<Integer, MovementDTO>> typeMvMap;

    private transient final Map<Integer, List<ClientOrderDTO>> orderMap;

    /**
     * Default constructor.
     *
     * @param gameId   the game of the order.
     * @param nationId the owner of the order.
     * @param turn     the turn of the order.
     */
    public MovementSaveChangesProcessor(final int thisScenario, final int gameId, final int nationId, final int turn,
                                        final Map<Integer, List<ClientOrderDTO>> orders) {
        super(thisScenario, gameId, nationId, turn);
        orderMap = orders;
    }

    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        // do nothing
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        typeMvMap = (Map<Integer, Map<Integer, MovementDTO>>) chData;
    }

    @SuppressWarnings("restriction")
    public List<OrderDTO> processChanges() {
        final List<OrderDTO> newMovementOrders = new ArrayList<OrderDTO>();
        final List<ClientOrderDTO> movementOrders = orderMap.get(ORDER_M_UNIT);
        for (final int unitType : typeMvMap.keySet()) {
            for (final int unitId : typeMvMap.get(unitType).keySet()) {
                final MovementDTO mvDTO = typeMvMap.get(unitType).get(unitId);

                // make sure there is a path defined
                if (mvDTO.getPaths().isEmpty()) {
                    LOGGER.info("Skipping movement order because path is empty " + unitType + ", " + unitId + ", " + getNationId());
                    continue;
                }

                int position = 0;
                for (ClientOrderDTO order : movementOrders) {
                    if (order.getIdentifier(1) == unitId
                            && order.getIdentifier(0) == unitType) {
                        position = order.getPriority();
                        break;
                    }
                }

                int orderType = 0;
                switch (unitType) {

                    case ARMY:
                        if (mvDTO.getForcedMarch()) {
                            orderType = ORDER_FM_ARMY;

                        } else {
                            orderType = ORDER_M_ARMY;
                        }
                        break;

                    case CORPS:
                        if (mvDTO.getForcedMarch()) {
                            orderType = ORDER_FM_CORP;

                        } else {
                            orderType = ORDER_M_CORP;
                        }
                        break;

                    case BRIGADE:
                        if (mvDTO.getForcedMarch()) {
                            orderType = ORDER_FM_BRIG;

                        } else {
                            orderType = ORDER_M_BRIG;
                        }
                        break;

                    case FLEET:
                        if (mvDTO.getPatrol()) {
                            orderType = ORDER_P_FLEET;

                        } else {
                            orderType = ORDER_M_FLEET;
                        }
                        break;

                    case SHIP:
                        if (mvDTO.getPatrol()) {
                            orderType = ORDER_P_SHIP;

                        } else {
                            orderType = ORDER_M_SHIP;
                        }
                        break;

                    case COMMANDER:
                        orderType = ORDER_M_COMM;
                        break;

                    case SPY:
                        orderType = ORDER_M_SPY;
                        break;

                    case BAGGAGETRAIN:
                        orderType = ORDER_M_BTRAIN;
                        break;

                    default:
                        break;
                }

                final StringBuilder strBuilder = new StringBuilder();
                for (final PathDTO newMovementOrder : mvDTO.getPaths()) {
                    strBuilder.append("!");
                    for (final PathSectorDTO movementOrder : newMovementOrder.getPathSectors()) {
                        strBuilder.append("-");
                        strBuilder.append(movementOrder.getX());
                        strBuilder.append(":");
                        strBuilder.append(movementOrder.getY());
                    }
                }

                String moveString;
                try {
                    moveString = strBuilder.substring(2);

                } catch (Exception ex) {
                    moveString = "";
                    LOGGER.debug("Failed to create move string");

                }

                // Check for dummy older instances of movement orders
                if (!mvDTO.getPaths().isEmpty()) {
                    newMovementOrders.add(createOrderMove(unitType, unitId, mvDTO, position, orderType, moveString));
                }
            }
        }

        return newMovementOrders;
    }

    private OrderDTO createOrderMove(final int unitType, final int unitId, final MovementDTO mvDTO, final int position, final int orderType, final String moveString) {
        final int patrol = (mvDTO.getPatrol() ? 1 : 0);
        return new OrderDTO(getGameId(), getNationId(), getTurn(),
                ORDER_M_UNIT, position, 0,
                String.valueOf(unitType),
                String.valueOf(unitId),
                moveString, String.valueOf(patrol),
                String.valueOf(orderType),
                "", "", "", "", String.valueOf(mvDTO.getRegionId()), "", "", "", "", "", "", "", "");
    }

}
