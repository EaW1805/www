package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.BrigadeInfoPanel;
import com.eaw1805.www.fieldbattle.widgets.HideAbleAbsolutePanel;
import com.eaw1805.www.fieldbattle.widgets.ScrollVerticalBar;
import com.eaw1805.www.fieldbattle.widgets.StyledCheckBox;


public class ArmySelectionPanel extends HideAbleAbsolutePanel {

    final VerticalPanel brigadesPanel = new VerticalPanel();
    final ScrollVerticalBar scroller;
    VerticalPanel infoPanelContainer = new VerticalPanel();

    public ArmySelectionPanel() {
        super();
        setStyleName("fieldArmySelector");
        setSize("200px", "600px");
        final StyledCheckBox artilleryCheck = new StyledCheckBox("Artillery", true, false, "");
        final StyledCheckBox infantryCheck = new StyledCheckBox("Infantry", true, false, "");
        final StyledCheckBox cavalryCheck = new StyledCheckBox("Cavalry", true, false, "");


        artilleryCheck.getCheckBox().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                infantryCheck.setChecked(false);
                cavalryCheck.setChecked(false);
                initBrigades(3);
            }
        });
        artilleryCheck.setChecked(false);
        infantryCheck.getCheckBox().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                artilleryCheck.setChecked(false);
                cavalryCheck.setChecked(false);
                initBrigades(1);
            }
        });
        infantryCheck.setChecked(false);
        cavalryCheck.getCheckBox().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                artilleryCheck.setChecked(false);
                infantryCheck.setChecked(false);
                initBrigades(2);
            }
        });
        cavalryCheck.setChecked(false);

        add(artilleryCheck, 3, 6);
        add(infantryCheck, 3, 26);
        add(cavalryCheck, 3, 46);


        scroller = new ScrollVerticalBar(brigadesPanel);
        scroller.setSize(140, 400);
        add(scroller, 3, 78);
        add(infoPanelContainer, 3, 500);
    }

    public void setInfoPanelForBrigade(final BrigadeDTO brigade) {
        infoPanelContainer.clear();
        BrigadeInfoPanel panel = new BrigadeInfoPanel(brigade, false, false, false);
        infoPanelContainer.add(panel);
    }

    public void initBrigades(final int type) {


    }


    @Override
    public void hidePanel() {
        show = false;
        MainPanel.getInstance().setWidgetPosition(this, Window.getClientWidth() - 10, Window.getClientHeight() - 600);
    }

    @Override
    public void showPanel() {
        show = true;
        MainPanel.getInstance().setWidgetPosition(this, Window.getClientWidth() - 200, Window.getClientHeight() - 600);
    }
}
