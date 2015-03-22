package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.Map;


public class ChangeCorpArmyOrder
        implements Order {

    private Map<Integer, ArmyDTO> armiesMap;
    private int armyId;


    public ChangeCorpArmyOrder(final Map<Integer, ArmyDTO> armiesMap,
                               final int armyId) {
        this.armiesMap = armiesMap;
        this.armyId = armyId;
    }
    ArmyDTO source = null;
    public int execute(final int unitId) {
        ArmyDTO target;
        target = armiesMap.get(armyId);
        for (ArmyDTO army : armiesMap.values()) {
            if (army.getCorps().containsKey(unitId)) {
                source = army;
                break;
            }
        }
        if (source != null && target != null && (source.getArmyId() != target.getArmyId())) {
            source.getCorps().get(unitId).setArmyId(getArmyId());
            target.getCorps().put(unitId, source.getCorps().get(unitId));
            source.getCorps().remove(unitId);
            return 1;

        } else {
            return 0;
        }
    }

    public ArmyDTO getSource() {
        return source;
    }

    public int getArmyId() {
        return armyId;
    }

    public void setArmyId(final int armyId) {
        this.armyId = armyId;
    }

}
