package empire.webapp.shared.orders.army;

import empire.data.dto.web.army.CommanderDTO;
import empire.webapp.shared.orders.Order;

public class CommanderUndoHireOrder implements Order {

    private final CommanderDTO commander;

    public CommanderUndoHireOrder(final CommanderDTO commander) {
        this.commander = commander;
    }

    public int execute(final int unitId) {
        if (commander.getInTransit()) {
            commander.setInTransit(false);
        }

        if (!commander.getInPool()) {
            commander.setPool(true);
        }
        return 1;
    }

}
