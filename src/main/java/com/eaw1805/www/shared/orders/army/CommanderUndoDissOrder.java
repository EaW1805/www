package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.shared.orders.Order;

public class CommanderUndoDissOrder implements Order {

    private final CommanderDTO commander;

    public CommanderUndoDissOrder(final CommanderDTO commander) {
        this.commander = commander;
    }

    public int execute(final int unitId) {
        if (commander.getInTransit()) {
            commander.setInTransit(false);
        }

        if (commander.getInPool()) {
            commander.setPool(false);
        }
        return 1;
    }

}
