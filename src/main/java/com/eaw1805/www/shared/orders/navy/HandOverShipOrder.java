package com.eaw1805.www.shared.orders.navy;

import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.Map;

public class HandOverShipOrder implements Order {

    private Map<Integer, ShipDTO> shipMap;
    private int selectedNationId;

    public HandOverShipOrder(final Map<Integer, ShipDTO> idFleetMap,
                             final int selectedNationId) {
        shipMap = idFleetMap;
        this.selectedNationId = selectedNationId;
    }

    public int execute(final int unitId) {
        try {
            final ShipDTO targetShip = shipMap.get(unitId);
            if (targetShip != null) {
                if (targetShip.getNationId() == selectedNationId) {
                    targetShip.sethOverNationId(0);
                } else {
                    targetShip.sethOverNationId(selectedNationId);
                }

                return 1;

            } else {
                return 0;
            }

        } catch (Exception ex) {
            return 0;
        }
    }

}
