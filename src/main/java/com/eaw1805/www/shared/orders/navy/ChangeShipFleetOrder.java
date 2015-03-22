package com.eaw1805.www.shared.orders.navy;

import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.Map;

public class ChangeShipFleetOrder
        implements Order {

    private Map<Integer, FleetDTO> fleetsMap;
    private int fleetId;

    public ChangeShipFleetOrder(final Map<Integer, FleetDTO> fleetsMap,
                                final int newFleetId) {
        this.fleetsMap = fleetsMap;
        this.fleetId = newFleetId;
    }
    FleetDTO source = null;
    public int execute(final int unitId) {

        try {
            FleetDTO target = null;
            for (final FleetDTO fleet : fleetsMap.values()) {
                if (fleet.getFleetId() == fleetId) {
                    target = fleet;
                }
                if (fleet.getShips().containsKey(unitId)) {
                    source = fleet;
                }
            }
            if (source != null && target != null && (target.getFleetId() != source.getFleetId())) {
                source.getShips().get(unitId).setFleet(fleetId);
                target.getShips().put(unitId, source.getShips().get(unitId));
                source.getShips().remove(unitId);
                return 1;
            } else {
                return 0;
            }

        } catch (Exception ex) {
            return 0;
        }
    }

    public FleetDTO getSource() {
        return source;
    }

}
