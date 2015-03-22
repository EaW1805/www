package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.constants.MilitaryCalculators;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.shared.orders.Order;
import com.eaw1805.www.shared.stores.GameStore;

public class CommanderHireOrder implements Order, RegionConstants {

    final private CommanderDTO commander;
    final private int regionId;

    public CommanderHireOrder(final CommanderDTO commander, final int regionId) {
        this.commander = commander;
        this.regionId = regionId;
    }

    public int execute(final int unitId) {
        if (!commander.getStartInPool()) {
            commander.setInTransit(false);
        } else {
            // if it is in the same continent then automatic
            if (commander.getRegionId() == regionId
                    || GameStore.getInstance().isFastAssignment()) {
                commander.setInTransit(false);

            } else {
                commander.setInTransit(true);
                // Determine how many turns the commander will be in transit
                commander.setTransit(MilitaryCalculators.getTransitDistance(commander.getRegionId(), regionId));
            }
        }
        commander.setPool(false);
        return 1;
    }

}
