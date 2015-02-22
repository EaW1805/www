package empire.webapp.shared.orders.army;

import empire.data.dto.web.army.ArmyDTO;
import empire.webapp.shared.orders.Order;

import java.util.Map;


public class DeleteArmyOrder
        implements Order {

    private Map<Integer, ArmyDTO> armiesMap;
    private Map<Integer, ArmyDTO> deletedArmiesMap;
    private int armyId;

    public DeleteArmyOrder(final Map<Integer, ArmyDTO> armiesMap,
                           final int armyId, final Map<Integer, ArmyDTO> deletedArmies) {
        this.armiesMap = armiesMap;
        this.armyId = armyId;
        this.deletedArmiesMap = deletedArmies;
    }

    public int execute(final int unitId) {
        if (armiesMap.containsKey(unitId)) {
            // Remove any remaining commanders
            armiesMap.get(unitId).getCommander().setArmy(0);

            // Remove the army altogether
            deletedArmiesMap.put(armyId, armiesMap.remove(armyId));
            return 1;

        } else {
            return 0;
        }
    }

}

