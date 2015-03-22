package com.eaw1805.www.shared.orders.economy;

import com.eaw1805.data.dto.web.army.BarrackDTO;

import java.util.Map;

/**
 * Apply rename barrack order.
 */
public class ChangeBarrackNameOrder {
    private Map<Integer, BarrackDTO> barrackMap;
    private String newName;

    /**
     * Set the basic information needed for renaming.
     *
     * @param barMap  A map of barrack ids and barracks
     * @param newName The new name to set.
     */
    public ChangeBarrackNameOrder(final Map<Integer, BarrackDTO> barMap,
                                  final String newName) {
        this.barrackMap = barMap;
        this.newName = newName;
    }

    /**
     * Execute the order rename given barrack.
     *
     * @param unitId The id of the barrack to rename.
     * @return If success 1, otherwise 0.
     */
    public int execute(final int unitId) {
        if (barrackMap.containsKey(unitId)) {
            barrackMap.get(unitId).setName(newName);
            return 1;
        } else {
            return 0;
        }
    }
}
