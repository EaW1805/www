package com.eaw1805.www.scenario.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.www.fieldbattle.widgets.OptionEAW;
import com.eaw1805.www.scenario.stores.EditorMapUtils;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.RegionSettings;
import com.eaw1805.www.scenario.stores.WidgetStore;
import com.eaw1805.www.scenario.widgets.OptionWidgetEAW;
import com.eaw1805.www.scenario.widgets.SelectEAW;

/**
 * This view contains options about calculating/displaying supply lines.
 * Lets you choose a nation and then calculate.
 */
public class SupplyLinesView extends VerticalPanel {
    public SupplyLinesView() {
        getElement().getStyle().setBackgroundColor("grey");
        add(new Label("Supply Lines Options"));
        final SelectEAW<NationDTO> nationSelect = new SelectEAW<NationDTO>("Select Nation") {
            @Override
            public void onChange() {
                //do nothing here
            }
        };
        nationSelect.addOption(new OptionEAW(130, 15, "None Selected"), null);
        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            nationSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getNationImg(nation.getNationId(), 20), WidgetStore.getLabel(nation.getName())), nation);
        }
        add(nationSelect);
        nationSelect.selectOption(0);
        final Button calculateSupplies = new Button("Calculate supplies");
        calculateSupplies.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (nationSelect.getValue() != null) {
                    EditorMapUtils.getInstance().drawSupply(nationSelect.getValue().getNationId());
                }
            }
        });
        add(calculateSupplies);
    }
}
