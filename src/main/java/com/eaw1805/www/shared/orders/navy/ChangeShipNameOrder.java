package com.eaw1805.www.shared.orders.navy;

import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.Map;

/**
 * Execute rename ship order.
 */
public class ChangeShipNameOrder implements Order {

    private Map<Integer, ShipDTO> shipsMap;
    private String newName;

    /**
     * Set the basic information needed.
     *
     * @param shipsMap Map with the ships.
     * @param newName  The name to set.
     */
    public ChangeShipNameOrder(final Map<Integer, ShipDTO> shipsMap,
                               final String newName) {
        this.shipsMap = shipsMap;
        this.newName = newName;
    }

    /**
     * Rename given ship with the given name.
     *
     * @param unitId The ship to rename.
     * @return If success 1, else 0.
     */
    public int execute(final int unitId) {
        try {
            if (shipsMap.containsKey(unitId)) {
                shipsMap.get(unitId).setName(newName);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            return 0;
        }
    }
}
