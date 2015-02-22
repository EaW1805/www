package empire.webapp.shared.orders.army;

import empire.data.dto.web.army.BrigadeDTO;
import empire.webapp.shared.orders.Order;

import java.util.Map;


public class ChangeBrigadeNameOrder implements Order {

    private Map<Integer, BrigadeDTO> brigadesMap;
    private String newName;


    public ChangeBrigadeNameOrder(final Map<Integer, BrigadeDTO> brigadesMap,
                                  final String newName) {
        this.brigadesMap = brigadesMap;
        this.newName = newName;
    }

    public int execute(final int unitId) {

        try {
            if (brigadesMap.containsKey(unitId)) {
                brigadesMap.get(unitId).setName(getNewName());
                return 1;

            } else {
                return 0;
            }

        } catch (Exception ex) {
            return 0;
        }
    }

    public String getNewName() {
        return newName;
    }

}
