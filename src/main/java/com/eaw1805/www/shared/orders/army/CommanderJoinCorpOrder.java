package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.shared.orders.Order;

import java.util.Map;

public class CommanderJoinCorpOrder implements Order, ArmyConstants {

    private Map<Integer, ArmyDTO> armiesMap;
    private CommanderDTO commander;

    public CommanderJoinCorpOrder(final Map<Integer, ArmyDTO> armiesMap,
                                  final CommanderDTO commander) {
        this.armiesMap = armiesMap;
        this.commander = commander;
    }

    public int execute(final int unitId) {
        CorpDTO corpSource = null;
        ArmyDTO armySource = null;
        CorpDTO target = null;
        for (final ArmyDTO army : armiesMap.values()) {
            if (army.getCommander() != null && army.getCommander().getId() == commander.getId()) {
                armySource = army;
                break;
            }
            for (CorpDTO corp : army.getCorps().values()) {
                if (corp.getCommander() != null && corp.getCommander().getId() == commander.getId()) {
                    corpSource = corp;
                }
                if (corp.getCorpId() == unitId) {
                    target = corp;
                }
                if (corpSource != null && target != null) {
                    break;
                }
            }

            if (corpSource != null && target != null) {
                break;
            }
        }

        if (target != null) {
            if (corpSource != null) {
                corpSource.setCommander(new CommanderDTO());

            } else if (armySource != null) {
                armySource.setCommander(new CommanderDTO());
            }

            commander.setArmy(0);
            commander.setCorp(unitId);

            // Make the old commander free for others
            if (target.getCommander() != null) {
                target.getCommander().setArmy(0);
                target.getCommander().setCorp(0);
                UnitEventManager.changeUnit(COMMANDER, target.getCommander().getId());
            }

            target.setCommander(commander);
            return 1;

        } else {
            return 0;
        }
    }

    public void setCommander(final CommanderDTO commander) {
        this.commander = commander;
    }

    public CommanderDTO getCommander() {
        return commander;
    }

}
