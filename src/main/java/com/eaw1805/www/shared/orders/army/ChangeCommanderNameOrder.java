package empire.webapp.shared.orders.army;

import empire.data.dto.web.army.CommanderDTO;
import empire.webapp.shared.orders.Order;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: karavias
 * Date: 1/24/12
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChangeCommanderNameOrder implements Order {

    private Map<Integer, CommanderDTO> commanderMap;
    private String newName;

    public ChangeCommanderNameOrder(final Map<Integer, CommanderDTO> commMap, String newName) {
        this.commanderMap = commMap;
        this.newName = newName;
    }

    public int execute(final int unitId) {
        try {
            if (commanderMap.containsKey(unitId)) {
                commanderMap.get(unitId).setName(newName);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            return 0;
        }
    }
}
