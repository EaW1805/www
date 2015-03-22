package com.eaw1805.www.shared.orders.economy;

import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;

import java.util.Map;

/**
 * Change the name of the baggage train object.
 * so it appears correctly on clients view.
 */
public class ChangeBaggageTrainNameOrder {
    private Map<Integer, BaggageTrainDTO> bTrainMap;
    private String newName;

    /**
     * Provides the basic information for renaming baggage train.
     *
     * @param barMap  A map of b trains ids and b trains
     * @param newName The new name to set.
     */
    public ChangeBaggageTrainNameOrder(final Map<Integer, BaggageTrainDTO> barMap,
                                       final String newName) {
        this.bTrainMap = barMap;
        this.newName = newName;
    }

    /**
     * Executes the rename baggage train order.
     *
     * @param unitId The id of the baggage train to rename.
     * @return If success 1, otherwise 0.
     */
    public int execute(final int unitId) {
        if (bTrainMap.containsKey(unitId)) {
            bTrainMap.get(unitId).setName(newName);
            return 1;
        } else {
            return 0;
        }
    }
}
