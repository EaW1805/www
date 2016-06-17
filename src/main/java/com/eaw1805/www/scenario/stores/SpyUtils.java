package com.eaw1805.www.scenario.stores;


import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.scenario.views.EditorPanel;

import java.util.HashMap;
import java.util.Map;

public class SpyUtils {

    private static int spyId = -1;

    public static void createSpy(int x, int y) {
        SpyDTO spy = new SpyDTO();
        spy.setSpyId(getSpyId());
        spy.setNationId(EditorPanel.getInstance().getSpyBTrainBrush().getNation().getNationId());
        spy.setRegionId(RegionSettings.region.getRegionId());
        spy.setX(x);
        spy.setY(y);
        final Map<Integer, Map<Integer, Map<Integer, SpyDTO>>> spyMap = EditorStore.getInstance().getSpies().get(RegionSettings.region.getRegionId());
        if (!spyMap.containsKey(x)) {
            spyMap.put(x, new HashMap<Integer, Map<Integer, SpyDTO>>());
        }
        if (!spyMap.get(x).containsKey(y)) {
            spyMap.get(x).put(y, new HashMap<Integer, SpyDTO>());
        }
        spyMap.get(x).get(y).put(spy.getSpyId(), spy);
        EditorMapUtils.getInstance().drawSpy(spy);
    }

    private static int getSpyId() {
        spyId--;
        return spyId;
    }
}
