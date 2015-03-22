package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.constants.MilitaryCalculators;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.shared.orders.Order;

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
