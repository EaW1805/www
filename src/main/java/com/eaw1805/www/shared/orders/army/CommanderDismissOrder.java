package empire.webapp.shared.orders.army;

import empire.data.constants.MilitaryCalculators;
import empire.data.constants.RegionConstants;
import empire.data.dto.web.army.CommanderDTO;
import empire.webapp.shared.orders.Order;

public class CommanderDismissOrder implements Order, RegionConstants {

    final private CommanderDTO commander;

    public CommanderDismissOrder(final CommanderDTO commander) {
        this.commander = commander;
    }

    public int execute(final int unitId) {
        if (commander.getStartInPool()) {
            commander.setInTransit(false);

        } else {
            if (commander.getRegionId() != EUROPE) {
                commander.setInTransit(true);
                commander.setTransit(1);
            }
        }

        commander.setPool(true);
        return 0;
    }

}
