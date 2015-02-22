package empire.webapp.shared.orders.army;

import empire.data.constants.MilitaryCalculators;
import empire.data.constants.RegionConstants;
import empire.data.dto.web.army.CommanderDTO;
import empire.webapp.shared.orders.Order;
import empire.webapp.shared.stores.GameStore;

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
