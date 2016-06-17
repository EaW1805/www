package com.eaw1805.www.scenario.views.overview;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.RegionSettings;

import java.util.HashMap;
import java.util.Map;


public class ShipsOverView extends VerticalPanel {
    final Grid grid;

    public ShipsOverView() {
        grid = new Grid(17, 5);
        getElement().getStyle().setBackgroundColor("grey");

        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            if (nation.getNationId() > 0) {
                grid.setWidget(nation.getNationId() - 1, 0, new Label(nation.getName().substring(0, 4)));
                grid.setWidget(nation.getNationId() - 1, 1, new Image("http://static.eaw1805.com/images/buttons/icons/formations/warship.png"));
                grid.setWidget(nation.getNationId() - 1, 2, new Label("0"));
                grid.setWidget(nation.getNationId() - 1, 3, new Image("http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png"));
                grid.setWidget(nation.getNationId() - 1, 4, new Label("0"));

            }
        }
        add(grid);
    }

    public void updateOverview() {
        int regionId = RegionSettings.region.getRegionId();
        final Map<Integer, Integer[]> nationToStats = new HashMap<Integer, Integer[]>();
        for (int index = 1; index<= 17; index++) {
            nationToStats.put(index, new Integer[2]);
            nationToStats.get(index)[0] = 0;
            nationToStats.get(index)[1] = 0;
        }
        for (Map.Entry<Integer, Map<Integer, Map<Integer, ShipDTO>>> entry : EditorStore.getInstance().getShips().get(regionId).entrySet()) {
            for (Map.Entry<Integer, Map<Integer, ShipDTO>> entry2 : entry.getValue().entrySet()) {
                for (ShipDTO ship : entry2.getValue().values()) {
                    if (ship.getNationId() > 0) {
                        if (ship.getType().getShipClass() == 0) {
                            nationToStats.get(ship.getNationId())[1]++;
                        } else {
                            nationToStats.get(ship.getNationId())[0]++;
                        }
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
