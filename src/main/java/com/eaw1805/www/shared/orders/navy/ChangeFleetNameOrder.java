package empire.webapp.shared.orders.navy;

import empire.data.dto.web.fleet.FleetDTO;
import empire.webapp.shared.orders.Order;

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
