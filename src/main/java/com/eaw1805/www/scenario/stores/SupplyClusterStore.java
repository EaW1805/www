package com.eaw1805.www.scenario.stores;


import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.www.scenario.algorithms.SectorNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplyClusterStore {

    private static SupplyClusterStore instance;
    final static int SUPPLY_DISTANCE = 40;
    final Map<Integer, Map<String, SectorNode>> nationToSupplyNodes = new HashMap<Integer, Map<String, SectorNode>>();

    public static SupplyClusterStore getInstance() {
        if (instance == null) {
            instance = new SupplyClusterStore();
        }
        return instance;
    }

    private SupplyClusterStore() {
        //hide this from the outer world.
    }









}
