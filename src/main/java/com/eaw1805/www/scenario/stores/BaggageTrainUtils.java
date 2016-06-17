package com.eaw1805.www.scenario.stores;

import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.scenario.views.EditorPanel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 12/14/13
 * Time: 9:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaggageTrainUtils {
    private static int bTrainId = -1;

    public static void createBaggageTrain(final int x, final int y) {
        BaggageTrainDTO bTrain = new BaggageTrainDTO();
        bTrain.setId(getBTrainId());
        bTrain.setNationId(EditorPanel.getInstance().getSpyBTrainBrush().getNation().getNationId());
        bTrain.setRegionId(RegionSettings.region.getRegionId());
        bTrain.setX(x);
        bTrain.setY(y);
        final Map<Integer, Map<Integer, Map<Integer, BaggageTrainDTO>>> bTrainMap = EditorStore.getInstance().getBaggageTrains().get(RegionSettings.region.getRegionId());
        if (!bTrainMap.containsKey(x)) {
            bTrainMap.put(x, new HashMap<Integer, Map<Integer, BaggageTrainDTO>>());
        }
        if (!bTrainMap.get(x).containsKey(y)) {
            bTrainMap.get(x).put(y, new HashMap<Integer, BaggageTrainDTO>());
        }
        bTrainMap.get(x).get(y).put(bTrain.getId(), bTrain);
        EditorMapUtils.getInstance().drawBTrain(bTrain);

    }

    private static int getBTrainId(){
        bTrainId--;
        return bTrainId;
    }

}
