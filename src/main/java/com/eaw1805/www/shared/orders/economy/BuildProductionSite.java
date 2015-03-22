package com.eaw1805.www.shared.orders.economy;

import com.eaw1805.www.shared.orders.Order;

public class BuildProductionSite implements Order {

    public int execute(final int unitId) {

        try {

            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

}
