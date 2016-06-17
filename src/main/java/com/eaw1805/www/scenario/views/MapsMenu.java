package com.eaw1805.www.scenario.views;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.RegionUtils;

public class MapsMenu extends VerticalPanel {

    public MapsMenu() {
        initMenu();
    }

    public void initMenu() {
        getElement().getStyle().setBackgroundColor("grey");
        final Label title = new Label("*Scenario Maps*");
        add(title);



        for (final RegionDTO region : EditorStore.getInstance().getRegions()) {
            final AbsolutePanel container = new AbsolutePanel();
            container.setSize("130px", "25px");
            final RadioButton button = new RadioButton("mapradio", region.getName());
            button.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    RegionUtils.selectRegion(region);
                }
            });
//            final MenuElementWidget menu = new MenuElementWidget(region.getName());
//            menu.initHandler(new BasicHandler() {
//                @Override
//                public void run() {
//                    RegionUtils.selectRegion(region);
//                }
//            });
            container.add(button, 0, 0);

            final ImageButton deleteRegion = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png", "delete region");
            deleteRegion.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    RegionUtils.deleteRegion(region);
                }
            });
            deleteRegion.setSize("15px", "15px");
            container.add(deleteRegion, 115, 6);
            add(container);
        }

        HorizontalPanel newRegionContainer = new HorizontalPanel();
        final TextBox newRegionName = new TextBox();
        newRegionName.setWidth("140px");
        final Button createNew = new Button("+");
        createNew.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                try {
                    RegionUtils.createNewRegion(newRegionName.getText());
                } catch (Exception e) {
                    Window.alert("Failed to add a new region : " + e.toString());
                }
            }
        });
        newRegionContainer.add(newRegionName);
        newRegionContainer.add(createNew);
        add(newRegionContainer);
    }

    public void resetRegions() {
        clear();
        initMenu();
    }
}
