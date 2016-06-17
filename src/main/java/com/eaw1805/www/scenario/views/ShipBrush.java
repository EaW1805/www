package com.eaw1805.www.scenario.views;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.www.fieldbattle.widgets.OptionEAW;
import com.eaw1805.www.fieldbattle.widgets.SelectEAW;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.WidgetStore;
import com.eaw1805.www.scenario.widgets.OptionWidgetEAW;


public class ShipBrush extends VerticalPanel {

    boolean brushEnabled = true;

    SelectEAW<NationDTO> nationSelect;
    SelectEAW<ShipTypeDTO> shipTypeSelect;
    public ShipBrush() {
        getElement().getStyle().setBackgroundColor("grey");
        add(new Label("*Ship Brush*"));
        final CheckBox enabled = new CheckBox("Enabled");
        enabled.setValue(brushEnabled);
        enabled.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {

                brushEnabled = event.getValue();

            }
        });
        add(enabled);

        nationSelect = new SelectEAW<NationDTO>("Select Nation") {
            @Override
            public void onChange() {
                //do nothing here
            }
        };
        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            if (nation.getNationId() > 0) {
                nationSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getNationImg(nation.getNationId(), 20), WidgetStore.getLabel(nation.getName())), nation);
            }
        }

        shipTypeSelect = new SelectEAW<ShipTypeDTO>("Select ship type") {
            @Override
            public void onChange() {
                //do nothing here
            }
        };

        for (ShipTypeDTO shipType : EditorStore.getInstance().getShipTypes()) {
            shipTypeSelect.addOption(new OptionEAW(130, 15, shipType.getName()), shipType);
        }

        Grid grid = new Grid(2, 2);
        grid.setWidget(0, 0, new Label("Nation :"));
        grid.setWidget(0, 1, nationSelect);
        grid.setWidget(1, 0, new Label("Type :"));
        grid.setWidget(1, 1, shipTypeSelect);
        add(grid);
        nationSelect.selectOption(0);
        shipTypeSelect.selectOption(0);
    }

    public NationDTO getNation() {
        return nationSelect.getValue();
    }

    public ShipTypeDTO getShipType() {
        return shipTypeSelect.getValue();
    }

    public boolean isBrushEnabled() {
        return brushEnabled;
    }
}
