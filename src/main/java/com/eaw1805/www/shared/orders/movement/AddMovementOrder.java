package empire.webapp.shared.orders.movement;

import empire.data.constants.ArmyConstants;
import empire.data.dto.common.PositionDTO;
import empire.data.dto.web.army.ArmyDTO;
import empire.data.dto.web.army.BrigadeDTO;
import empire.data.dto.web.army.CommanderDTO;
import empire.data.dto.web.army.CorpDTO;
import empire.data.dto.web.fleet.FleetDTO;
import empire.data.dto.web.fleet.ShipDTO;
import empire.data.dto.web.movement.MovementDTO;
import empire.data.dto.web.movement.PathDTO;
import empire.webapp.client.events.units.UnitEventManager;
import empire.webapp.shared.orders.Order;
import empire.webapp.shared.stores.MovementStore;

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
