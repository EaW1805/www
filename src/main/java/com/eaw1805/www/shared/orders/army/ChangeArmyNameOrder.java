package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.Map;


public class ChangeArmyNameOrder
        implements Order {

    private Map<Integer, ArmyDTO> armiesMap;
    private String newName;

    public ChangeArmyNameOrder(final Map<Integer, ArmyDTO> armiesMap,
                               final String newName) {
        this.armiesMap = armiesMap;
        this.newName = newName;
    }

    public int execute(final int unitId) {
        if (armiesMap.containsKey(unitId)) {
            armiesMap.get(unitId).setName(getNewName());
            return 1;

        } else {
            return 0;
        }
    }

    public String getNewName() {
        return newName;
    }

}
