package com.eaw1805.www.scenario.views.overview;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.RegionSettings;

import java.util.HashMap;
import java.util.Map;

public class SpyBTrainOverView extends VerticalPanel {
    Grid grid;

    public SpyBTrainOverView() {
        grid = new Grid(17, 5);
        getElement().getStyle().setBackgroundColor("grey");

        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            if (nation.getNationId() > 0) {
                grid.setWidget(nation.getNationId() - 1, 0, new Label(nation.getName().substring(0, 4)));
                final Image spy = new Image("http://static.eaw1805.com/images/figures/" + nation.getNationId() + "/spy.png");
                spy.setSize("20px", "20px");
                grid.setWidget(nation.getNationId() - 1, 1, spy);
                grid.setWidget(nation.getNationId() - 1, 2, new Label("0"));
                Image bTrain = new Image("http://static.eaw1805.com/images/figures/baggage.png");
                bTrain.setSize("20px", "20px");
                grid.setWidget(nation.getNationId() - 1, 3, bTrain);
                grid.setWidget(nation.getNationId() - 1, 4, new Label("0"));


            }
        }
        add(grid);
    }

    public void updateOverview() {
        int regionId = RegionSettings.region.getRegionId();
        final Map<Integer, Integer[]> nationToStats = new HashMap<Integer, Integer[]>();
        for (int index = 1; index<= 17; index++) {
            nationToStats.put(index, new Integer[3]);
            nationToStats.get(index)[0] = 0;
            nationToStats.get(index)[1] = 0;
        }
        for (Map.Entry<Integer, Map<Integer, Map<Integer, SpyDTO>>> entry : EditorStore.getInstance().getSpies().get(regionId).entrySet()) {
            for (Map.Entry<Integer, Map<Integer, SpyDTO>> entry2 : entry.getValue().entrySet()) {
                for (SpyDTO spy : entry2.getValue().values()) {
                    if (spy.getNationId() > 0) {
                        nationToStats.get(spy.getNationId())[0]++;
                    }
                }
            }
        }
        for (Map.Entry<Integer, Map<Integer, Map<Integer, BaggageTrainDTO>>> entry : EditorStore.getInstance().getBaggageTrains().get(regionId).entrySet()) {
            for (Map.Entry<Integer, Map<Integer, BaggageTrainDTO>> entry2 : entry.getValue().entrySet()) {
                for (BaggageTrainDTO bTrain : entry2.getValue().values()) {
                    if (bTrain.getNationId() > 0) {
                        nationToStats.get(bTrain.getNationId())[1]++;
                    }
                }
            }
        }
        for (int index = 1; index <= 17; index++) {
            grid.setWidget(index - 1, 2, new Label(String.valueOf(nationToStats.get(index)[0])));
            grid.setWidget(index - 1, 4, new Label(String.valueOf(nationToStats.get(index)[1])));
        }
    }
}
