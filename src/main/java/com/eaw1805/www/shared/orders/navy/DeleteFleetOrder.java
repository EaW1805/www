package empire.webapp.shared.orders.navy;

import empire.data.dto.web.fleet.FleetDTO;
import empire.webapp.shared.orders.Order;

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
