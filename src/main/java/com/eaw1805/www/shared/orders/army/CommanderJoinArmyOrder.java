package empire.webapp.shared.orders.army;

import empire.data.constants.ArmyConstants;
import empire.data.dto.web.army.ArmyDTO;
import empire.data.dto.web.army.CommanderDTO;
import empire.data.dto.web.army.CorpDTO;
import empire.webapp.client.events.units.UnitEventManager;
import empire.webapp.shared.orders.Order;

import java.util.Map;

public class CommanderJoinArmyOrder implements Order, ArmyConstants {

    private Map<Integer, ArmyDTO> armiesMap;
    private CommanderDTO commander;


    public CommanderJoinArmyOrder(final Map<Integer, ArmyDTO> armiesMap,
                                  final CommanderDTO commander) {
        this.armiesMap = armiesMap;
        this.commander = commander;
    }

    public int execute(final int unitId) {

        CorpDTO corpSource = null;
        ArmyDTO armySource = null;
        final ArmyDTO target = armiesMap.get(unitId);
        for (final ArmyDTO army : armiesMap.values()) {
            if (army.getCommander() != null && army.getCommander().getId() == commander.getId()) {
                armySource = army;
                break;
            }

            for (final CorpDTO corp : army.getCorps().values()) {
                if (corp.getCommander() != null && corp.getCommander().getId() == commander.getId()) {
                    corpSource = corp;
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
            commander.setArmy(unitId);
            commander.setCorp(0);
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
