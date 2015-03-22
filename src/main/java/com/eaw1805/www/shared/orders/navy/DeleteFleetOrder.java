package com.eaw1805.www.shared.orders.navy;

import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.Map;

public class DeleteFleetOrder implements Order {

    private Map<Integer, FleetDTO> fleetsMap;
    private Map<Integer, FleetDTO> deletedFleetsMap;
    private int fleetId;

    public DeleteFleetOrder(final Map<Integer, FleetDTO> fleetsMap,
                            final int fleetId, final Map<Integer, FleetDTO> deletedFleetsMap) {
        this.fleetsMap = fleetsMap;
        this.fleetId = fleetId;
        this.deletedFleetsMap = deletedFleetsMap;
    }

    public int execute(final int unitId) {
        try {
            if (fleetsMap.containsKey(unitId)) {
                deletedFleetsMap.put(unitId, fleetsMap.remove(fleetId));
                return 1;

            } else {
                return 0;
            }
        } catch (Exception ex) {
            return 0;
        }
    }

}
