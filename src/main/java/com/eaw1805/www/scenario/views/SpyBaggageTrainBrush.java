package com.eaw1805.www.scenario.views;


import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.www.fieldbattle.widgets.OptionEAW;
import com.eaw1805.www.fieldbattle.widgets.SelectEAW;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.WidgetStore;
import com.eaw1805.www.scenario.widgets.OptionWidgetEAW;

public class SpyBaggageTrainBrush extends VerticalPanel {
    boolean brushEnabled = true;
    SelectEAW<NationDTO> nationSelect;
    RadioButton spyButton;
    RadioButton bTrainButton;
    public SpyBaggageTrainBrush() {
        getElement().getStyle().setBackgroundColor("grey");
        add(new Label("*Spy-Baggage Trains View*"));
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

        spyButton = new RadioButton("sbtrainGroup", "Spy");
        bTrainButton = new RadioButton("sbtrainGroup", "Baggage train");

        add(nationSelect);
        add(spyButton);
        add(bTrainButton);
        nationSelect.selectOption(0);
        spyButton.setValue(true);

    }

    public NationDTO getNation() {
        return nationSelect.getValue();
    }

    public boolean isSpy() {
        return spyButton.getValue();
    }

    public boolean isBTrain() {
        return bTrainButton.getValue();
    }

    public boolean isBrushEnabled() {
        return brushEnabled;
    }
}
