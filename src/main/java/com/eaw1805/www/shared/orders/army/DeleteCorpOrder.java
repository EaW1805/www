package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.Map;


public class DeleteCorpOrder implements Order {

    private Map<Integer, CorpDTO> corpsMap;
    private Map<Integer, CorpDTO> deletedCorpsMap;
    public DeleteCorpOrder(final Map<Integer, CorpDTO> corpsMap, final Map<Integer, CorpDTO> deletedCorps) {
        this.corpsMap = corpsMap;
        this.deletedCorpsMap = deletedCorps;
    }

    public int execute(final int unitId) {
        if (corpsMap.containsKey(unitId)) {
            deletedCorpsMap.put(unitId, corpsMap.remove(unitId));
            return 1;

        } else {
            return 0;
        }
    }

}

