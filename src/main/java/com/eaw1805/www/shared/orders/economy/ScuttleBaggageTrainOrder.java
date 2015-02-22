package empire.webapp.shared.orders.economy;

import empire.data.dto.web.economy.BaggageTrainDTO;

import java.util.Map;

/**
 * Apply baggage train scuttle order.
 */
public class ScuttleBaggageTrainOrder {
    private Map<Integer, BaggageTrainDTO> bTrainMap;
    private boolean scuttle;

    /**
     * Set the basic information needed for scuttle.
     *
     * @param barMap  A map of barrack ids and barracks
     * @param scuttle true if scuttle baggage train.
     */
    public ScuttleBaggageTrainOrder(final Map<Integer, BaggageTrainDTO> barMap,
                                    final boolean scuttle) {
        this.bTrainMap = barMap;
        this.scuttle = scuttle;
    }

    /**
     * Execute the scuttle baggage train order.
     *
     * @param unitId The id of the baggage train to scuttle.
     * @return If success 1, otherwise 0.
     */
    public int execute(final int unitId) {
        if (bTrainMap.containsKey(unitId)) {
            bTrainMap.get(unitId).setScuttle(scuttle);
            return 1;

        } else {
            return 0;
        }
    }

}
