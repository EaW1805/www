package com.eaw1805.www.scenario.views.overview;


import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.RegionSettings;
import com.eaw1805.www.scenario.views.CommandersNamePanel;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.scenario.widgets.MenuElementWidget;

import java.util.HashMap;
import java.util.Map;

public class CommandersOverView extends VerticalPanel {

    MenuElementWidget[] nationWidgets = new MenuElementWidget[17];
    public CommandersOverView() {
        getElement().getStyle().setBackgroundColor("grey");

        for (final NationDTO nation : EditorStore.getInstance().getNations()) {
            if (nation.getNationId() > 0) {
                nationWidgets[nation.getNationId() - 1] = new MenuElementWidget(nation.getName().substring(0, 4) + " - C:0, CM:0)");
                nationWidgets[nation.getNationId() - 1].initHandler(new BasicHandler() {
                    @Override
                    public void run() {
                        new CommandersNamePanel(nation).show();
                    }
                });
                add(nationWidgets[nation.getNationId() - 1]);
            }
        }
    }

    public void updateOverview() {
        int regionId = RegionSettings.region.getRegionId();
        Map<Integer, Integer> nationToStats = new HashMap<Integer, Integer>();
        for (int index = 1; index <= 17; index++) {
            nationToStats.put(index, 0);
        }
        for (Map.Entry<Integer, Map<Integer, Map<Integer, CommanderDTO>>> entry : EditorStore.getInstance().getCommanders().get(regionId).entrySet()) {
            for (Map.Entry<Integer, Map<Integer, CommanderDTO>> entry2 : entry.getValue().entrySet()) {
                for (CommanderDTO comm : entry2.getValue().values()) {
                    if (comm.getNationId() > 0) {
                        nationToStats.put(comm.getNationId(), nationToStats.get(comm.getNationId()) + 1);
                    }
                }
            }
        }
        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            if (nation.getNationId() > 0) {
                nationWidgets[nation.getNationId() - 1].setName(nation.getName().substring(0, 4) +
                        " - C:" + nationToStats.get(nation.getNationId()) +
                        ", CM:" + EditorStore.getInstance().getCommanderNamesByNation(nation.getNationId()).size());
            }
        }
    }

}
