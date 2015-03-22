package com.eaw1805.www.shared.orders.navy;

import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.Map;

public class ChangeFleetNameOrder implements Order {

    private Map<Integer, FleetDTO> fleetsMap;
    private String newName;

    public ChangeFleetNameOrder(final Map<Integer, FleetDTO> fleetsMap,
                                final String newName) {
        this.fleetsMap = fleetsMap;
        this.newName = newName;
    }

    public int execute(final int unitId) {
        try {
            if (fleetsMap.containsKey(unitId)) {
                fleetsMap.get(unitId).setName(newName);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            return 0;
        }
    }

}
