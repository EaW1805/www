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

import java.util.Map;

public class RemoveMovementOrder implements Order, ArmyConstants {

    private Map<Integer, Map<Integer, MovementDTO>> mvMap;
    private MovementDTO mvDto;

    public RemoveMovementOrder(final MovementDTO movementDto,
                               Map<Integer, Map<Integer, MovementDTO>> typeMvMap) {
        super();
        mvDto = movementDto;
        mvMap = typeMvMap;
    }

    public int execute(final int unitId) {
        try {
            int unitType = mvDto.getArmyType();
            int newX, newY;
            final MovementDTO mvOldDto = mvMap.get(mvDto.getArmyType()).get(
                    unitId);
            // Remove last leg
            final int totLegs = mvOldDto.getPaths().size();
            if (totLegs > 0) {
                mvOldDto.setTotalCost(mvOldDto.getTotalCost()
                        - mvOldDto.getPaths().get(totLegs - 1).getTotalCost());
                mvOldDto.getPaths().remove(totLegs - 1);
            }
            mvOldDto.setForcedMarch(mvDto.getForcedMarch());

            PositionDTO mvUnit = MovementStore.getInstance()
                    .getUnitPosObjectByTypeAndId(mvDto.getArmyType(), unitId);
            final int totPaths = mvOldDto.getPaths().size();
            if (totPaths > 0) {
                final PathDTO lastPath = mvOldDto.getPaths().get(totPaths - 1);
                final int totSectors = lastPath.getPathSectors().size();
                if (totSectors > 0) {
                    PositionDTO currPoss = lastPath.getPathSectors().get(
                            totSectors - 1);
                    newX = currPoss.getX();
                    newY = currPoss.getY();
                    mvUnit.setX(currPoss.getX());
                    mvUnit.setY(currPoss.getY());
                } else {
                    newX = mvUnit.getXStart();
                    newY = mvUnit.getYStart();
                    mvUnit.setX(mvUnit.getXStart());
                    mvUnit.setY(mvUnit.getYStart());
                }
            } else {
                newX = mvUnit.getXStart();
                newY = mvUnit.getYStart();
                mvUnit.setX(mvUnit.getXStart());
                mvUnit.setY(mvUnit.getYStart());
            }

            if ((unitType != FLEET && unitType != SHIP) || !this.mvDto.getPatrol()) {
                if (unitType == FLEET && ((FleetDTO) mvUnit).getShips() != null) {
                    for (ShipDTO ship : ((FleetDTO) mvUnit).getShips().values()) {
                        ship.setX(newX);
                        ship.setY(newY);
                    }
                } else if (unitType == ARMY
                        && ((ArmyDTO) mvUnit).getCorps() != null) {
                    for (CorpDTO corp : ((ArmyDTO) mvUnit).getCorps().values()) {
                        corp.setX(newX);
                        corp.setY(newY);
                        for (BrigadeDTO brigade : corp.getBrigades().values()) {
                            brigade.setX(newX);
                            brigade.setY(newY);
                        }
                    }
                    CommanderDTO commander = ((ArmyDTO) mvUnit).getCommander();
                    if (commander != null) {
                        commander.setX(newX);
                        commander.setY(newY);
                        UnitEventManager.changeUnit(COMMANDER,
                                commander.getId());
                    }

                } else if (unitType == CORPS
                        && ((CorpDTO) mvUnit).getBrigades() != null) {
                    for (BrigadeDTO brigade : ((CorpDTO) mvUnit).getBrigades()
                            .values()) {
                        brigade.setX(newX);
                        brigade.setY(newY);
                    }
                    CommanderDTO commander = ((CorpDTO) mvUnit).getCommander();
                    if (commander != null) {
                        commander.setX(newX);
                        commander.setY(newY);
                        UnitEventManager.changeUnit(COMMANDER,
                                commander.getId());
                    }

                }
            }

            return 1;
        } catch (Exception Ex) {
            return 0;
        }
    }

}
