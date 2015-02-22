package empire.webapp.shared.orders.army;

import empire.data.dto.web.army.SpyDTO;
import empire.webapp.shared.orders.Order;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: karavias
 * Date: 1/25/12
 * Time: 12:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class ChangeSpyNameOrder implements Order {

    private Map<Integer, SpyDTO> spyMap;
    private String newName;

    public ChangeSpyNameOrder(final Map<Integer, SpyDTO> spyMap, String newName) {
        super();
        this.spyMap = spyMap;
        this.newName = newName;
    }

    public int execute(final int unitId) {
        try {
            if (spyMap.containsKey(unitId)) {
                spyMap.get(unitId).setName(newName);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            return 0;
        }
    }
}
