package com.eaw1805.www.scenario.views.overview;

import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.fieldbattle.stores.utils.ArmyUnitInfoDTO;
import com.eaw1805.www.fieldbattle.stores.utils.calculators.MiscCalculators;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.RegionSettings;

import java.util.HashMap;
import java.util.Map;

public class ArmyOverView extends VerticalPanel {

    Grid grid;

    public ArmyOverView() {
        grid = new Grid(17, 11);
        getElement().getStyle().setBackgroundColor("grey");

        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            if (nation.getNationId() > 0) {
                grid.setWidget(nation.getNationId() - 1, 0, new Label(nation.getName().substring(0, 4)));
                grid.setWidget(nation.getNationId() - 1, 1, new Image("http://static.eaw1805.com/images/buttons/icons/formations/infantry.png"));
                grid.setWidget(nation.getNationId() - 1, 2, new Label("0"));
                grid.setWidget(nation.getNationId() - 1, 3, new Image("http://static.eaw1805.com/images/buttons/icons/formations/cavalry.png"));
                grid.setWidget(nation.getNationId() - 1, 4, new Label("0"));
                grid.setWidget(nation.getNationId() - 1, 5, new Image("http://static.eaw1805.com/images/buttons/icons/formations/artillery.png"));
                grid.setWidget(nation.getNationId() - 1, 6, new Label("0"));
                grid.setWidget(nation.getNationId() - 1, 7, new Image("http://static.eaw1805.com/images/buttons/icons/formations/corps.png"));
                grid.setWidget(nation.getNationId() - 1, 8, new Label("0"));
                grid.setWidget(nation.getNationId() - 1, 9, new Image("http://static.eaw1805.com/images/buttons/icons/formations/army.png"));
                grid.setWidget(nation.getNationId() - 1, 10, new Label("0"));

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
            nationToStats.get(index)[2] = 0;
        }
        for (Map.Entry<Integer, Map<Integer, Map<Integer, BrigadeDTO>>> entry : EditorStore.getInstance().getBrigades().get(regionId).entrySet()) {
            for (Map.Entry<Integer, Map<Integer, BrigadeDTO>> entry2 : entry.getValue().entrySet()) {
                for (BrigadeDTO brigade : entry2.getValue().values()) {
                    if (brigade.getNationId() > 0) {
                        ArmyUnitInfoDTO brigInfo = MiscCalculators.getBrigadeInfo(brigade);
                        nationToStats.get(brigade.getNationId())[0]+=brigInfo.getInfantry();
                        nationToStats.get(brigade.getNationId())[1]+=brigInfo.getCavalry();
                        nationToStats.get(brigade.getNationId())[2]+=brigInfo.getArtillery();
                    }
                }
            }
        }
        for (int index = 1; index <= 17; index++) {
            grid.setWidget(index - 1, 2, new Label(String.valueOf(nationToStats.get(index)[0])));
            grid.setWidget(index - 1, 4, new Label(String.valueOf(nationToStats.get(index)[1])));
            grid.setWidget(index - 1, 6, new Label(String.valueOf(nationToStats.get(index)[2])));
        }
    }

}
