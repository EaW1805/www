package empire.webapp.shared.orders.economy;

import empire.webapp.shared.orders.Order;

public class BuildProductionSite implements Order {

    public int execute(final int unitId) {

        try {

            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

}
