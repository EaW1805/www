package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.Map;


public class ChangeCorpNameOrder implements Order {

    private Map<Integer, CorpDTO> corpsMap;
    private String newName;


    public ChangeCorpNameOrder(final Map<Integer, CorpDTO> corpsMap,
                               final String newName) {
        this.corpsMap = corpsMap;
        this.newName = newName;
    }

    public int execute(final int unitId) {
        if (corpsMap.containsKey(unitId)) {
            corpsMap.get(unitId).setName(getNewName());
            return 1;
        } else {
            return 0;
        }
    }

    public String getNewName() {
        return newName;
    }

}
