package empire.webapp.shared.orders.navy;

import empire.data.dto.web.fleet.ShipDTO;
import empire.webapp.shared.orders.Order;

import java.util.Map;

/**
 * Scuttle ship order.
 */
public class ScuttleShipOrder implements Order {

    private Map<Integer, ShipDTO> shipsMap;
    private boolean scuttle;

    /**
     * Set the basic information needed.
     *
     * @param shipsMap Map with the ships.
     * @param scuttle  The name to set.
     */
    public ScuttleShipOrder(final Map<Integer, ShipDTO> shipsMap,
                            final boolean scuttle) {
        this.shipsMap = shipsMap;
        this.scuttle = scuttle;
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
                shipsMap.get(unitId).setScuttle(scuttle);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            return 0;
        }
    }
}
