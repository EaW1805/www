package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.Map;

public class CommanderLeaveFederationOrder implements Order {

    private Map<Integer, ArmyDTO> armiesMap;
    private CommanderDTO commander;

    public CommanderLeaveFederationOrder(final Map<Integer, ArmyDTO> armiesMap,
                                         final CommanderDTO commander) {
        this.armiesMap = armiesMap;
        this.commander = commander;
    }

    public int execute(final int unitId) {
        if (commander != null && commander.getArmy() != 0) {
            final ArmyDTO thisArmy = armiesMap.get(commander.getArmy());
            if (thisArmy != null) {
                if (thisArmy.getCommander() == null || thisArmy.getCommander().getId() == commander.getId()) {
                    thisArmy.setCommander(new CommanderDTO());
                }
                commander.setArmy(0);
                return 1;
            }

        } else if (commander != null && commander.getCorp() != 0) {
            for (final ArmyDTO army : armiesMap.values()) {
                if (army.getCorps().containsKey(commander.getCorp())) {
                    if (army.getCorps().get(commander.getCorp()).getCommander() == null || army.getCorps().get(commander.getCorp()).getCommander().getId() == commander.getId()) {
                        army.getCorps().get(commander.getCorp()).setCommander(new CommanderDTO());
                    }
                    break;
                }
            }
            commander.setCorp(0);
            commander.setArmy(0);
            return 1;
        }
        return 0;
    }


}
