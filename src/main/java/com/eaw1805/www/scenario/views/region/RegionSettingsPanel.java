package com.eaw1805.www.scenario.views.region;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.RegionSettings;
import com.eaw1805.www.scenario.stores.RegionUtils;

public class RegionSettingsPanel extends VerticalPanel {

    public RegionSettingsPanel() {
        getElement().getStyle().setBackgroundColor("grey");
        initPanel();
    }

    private void initPanel() {
        add(new Label("Region Settings"));

        final int x = RegionSettings.sizeX;
        final int y = RegionSettings.sizeY;
        final int totalSectors = x*y;
        final String name = RegionSettings.name;

        final Label xLbl = new Label("Width:");
        final TextBox xVal = new TextBox();
        xVal.setText(String.valueOf(x));

        final Label yLbl = new Label("Height:");
        final TextBox yVal = new TextBox();
        yVal.setText(String.valueOf(y));

        final Label tsLbl = new Label("Total : " + totalSectors + " sectors");
        final Label nameLbl = new Label("Name:");
        final TextBox nameVal = new TextBox();
        nameVal.setText(name);

        final Button apply = new Button("Apply");
        apply.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                try {
                RegionUtils.updateRegion(nameVal.getText(), Integer.parseInt(xVal.getText()), Integer.parseInt(yVal.getText()));

                } catch (Exception e) {
                    Window.alert("Failed to update region : " + e.toString());
                }
            }
        });

        add(xLbl);
        add(xVal);
        add(yLbl);
        add(yVal);
        add(tsLbl);
        add(nameLbl);
        add(nameVal);
        add(apply);


    }

    public void resetPanel() {
        clear();
        initPanel();
    }

}
