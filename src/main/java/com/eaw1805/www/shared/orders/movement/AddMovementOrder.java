package com.eaw1805.www.shared.orders.movement;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.movement.MovementDTO;
import com.eaw1805.data.dto.web.movement.PathDTO;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.shared.orders.Order;
import com.eaw1805.www.shared.stores.MovementStore;

import java.util.HashMap;
import java.util.Map;

public class AddMovementOrder implements Order, ArmyConstants {

    private MovementDTO mvDTO;

    private Map<Integer, Map<Integer, MovementDTO>> mvMap;

    public AddMovementOrder(final MovementDTO movementDto,
                            final Map<Integer, Map<Integer, MovementDTO>> typeMvMap) {
        mvDTO = movementDto;
        mvMap = typeMvMap;
    }

    public int execute(final int unitId) {
        try {
            MovementDTO oldDto;
            final int unitType = mvDTO.getArmyType();
            if (mvMap.containsKey(unitType)
                    && mvMap.get(unitType).containsKey(unitId)) {
                oldDto = mvMap.get(unitType).get(unitId);
                oldDto.getPaths().addAll(mvDTO.getPaths());
                oldDto.setForcedMarch(mvDTO.getForcedMarch());
                oldDto.setPatrol(mvDTO.getPatrol());
            }

            if (!mvMap.containsKey(unitType)) {
                mvMap.put(unitType, new HashMap<Integer, MovementDTO>());
            }

            if (!mvMap.get(unitType).containsKey(unitId)) {
                mvMap.get(unitType).put(unitId, mvDTO);
            }

            final PositionDTO mvUnit = MovementStore.getInstance()
                    .getUnitPosObjectByTypeAndId(mvDTO.getArmyType(), unitId);
            final int totPaths = mvDTO.getPaths().size();
            final PathDTO lastPath = mvDTO.getPaths().get(totPaths - 1);

            final int totSectors = lastPath.getPathSectors().size();
            if ((totSectors > 0) && ((unitType != FLEET && unitType != SHIP) || !mvDTO.getPatrol())) {
                final PositionDTO currPoss = lastPath.getPathSectors()
                        .get(totSectors - 1);
                mvUnit.setX(currPoss.getX());
                mvUnit.setY(currPoss.getY());
                if (unitType == FLEET && ((FleetDTO) mvUnit).getShips() != null) {
                    for (ShipDTO ship : ((FleetDTO) mvUnit).getShips().values()) {
                        ship.setX(currPoss.getX());
                        ship.setY(currPoss.getY());
                    }
                } else if (unitType == ARMY
                        && ((ArmyDTO) mvUnit).getCorps() != null) {
                    for (CorpDTO corp : ((ArmyDTO) mvUnit).getCorps().values()) {
                        corp.setX(currPoss.getX());
                        corp.setY(currPoss.getY());
                        for (BrigadeDTO brigade : corp.getBrigades().values()) {
                            brigade.setX(currPoss.getX());
                            brigade.setY(currPoss.getY());
                        }
                    }
                    CommanderDTO commander = ((ArmyDTO) mvUnit).getCommander();
                    if (commander != null) {
                        commander.setX(currPoss.getX());
                        commander.setY(currPoss.getY());
                        UnitEventManager.changeUnit(COMMANDER,
                                commander.getId());
                    }

                } else if (unitType == CORPS
                        && ((CorpDTO) mvUnit).getBrigades() != null) {
                    for (BrigadeDTO brigade : ((CorpDTO) mvUnit).getBrigades()
                            .values()) {
                        brigade.setX(currPoss.getX());
                        brigade.setY(currPoss.getY());
                    }

                    final CommanderDTO commander = ((CorpDTO) mvUnit).getCommander();
                    if (commander != null) {
                        commander.setX(currPoss.getX());
                        commander.setY(currPoss.getY());
                        UnitEventManager.changeUnit(COMMANDER,
                                commander.getId());
                    }
                }

            } else {
                // If we changed to patrol make sure
                // everyone returns to the startig position
                mvUnit.setX(mvUnit.getXStart());
                mvUnit.setY(mvUnit.getYStart());
                if (unitType == FLEET && ((FleetDTO) mvUnit).getShips() != null) {
                    for (ShipDTO ship : ((FleetDTO) mvUnit).getShips().values()) {
                        ship.setX(mvUnit.getXStart());
                        ship.setY(mvUnit.getYStart());
                    }
                }
            }

            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

}
