package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcService;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcServiceAsync;
import com.eaw1805.www.fieldbattle.tooltips.Tips;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.MiniMap;

public class MiniMapPanel extends AbsolutePanel {

    MiniMap map = new MiniMap(MiniMap.MINIMAP_WIDTH, MiniMap.MINIMAP_HEIGHT);
    private final static EmpireFieldBattleRpcServiceAsync service = GWT.create(EmpireFieldBattleRpcService.class);
    boolean opened = true;
    final SettingsPanel settingsPanel = new SettingsPanel();

    public MiniMapPanel() {
        setSize("230px", "236px");
        setStyleName("minimapFieldBattlePanel");
//        getElement().getStyle().setBackgroundColor("black");
//        setStyleName("minimapPanel");
        add(map, 0, 0);
        final ImageButton settings = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButPreferencesOff.png", Tips.ELEMENT_SETTINGS);
        settings.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                MainPanel.getInstance().addToCenter(settingsPanel);
            }
        });
        settings.setHeight("23px");
        add(settings, 169, 195);

        final ImageButton zoomIn = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButZoomInOff.png", Tips.ELEMENT_ZOOM_IN);
        zoomIn.setSize("26px", "23px");
        zoomIn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                MainPanel.getInstance().getDrawingArea().zoom(true);
            }
        });
        add(zoomIn, 112, 195);

        final ImageButton zoomOut = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButZoomOutOff.png", Tips.ELEMENT_ZOOM_OUT);
        zoomOut.setSize("26px", "23px");
        zoomOut.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                MainPanel.getInstance().getDrawingArea().zoom(false);
            }
        });
        add(zoomOut, 140, 195);

        final Timer closeT = new Timer() {
            @Override
            public void run() {
                if (getAbsoluteLeft() > -200) {
                    MainPanel.getInstance().setWidgetPosition(MiniMapPanel.this, getAbsoluteLeft() - 5, 0);
                } else {
                    MainPanel.getInstance().setWidgetPosition(MiniMapPanel.this, -200, 0);
                    cancel();
                }

            }
        };

        final Timer openT = new Timer() {
            @Override
            public void run() {
                if (getAbsoluteLeft() < 0) {
                    MainPanel.getInstance().setWidgetPosition(MiniMapPanel.this, getAbsoluteLeft() + 5, 0);
                } else {
                    MainPanel.getInstance().setWidgetPosition(MiniMapPanel.this, 0, 0);
                    cancel();
                }
            }
        };

        final ImageButton toggleButton = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchOff.png", Tips.ELEMENT_SHOW_MINIMAP);
        toggleButton.setSize("24px", "24px");
        toggleButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (opened) {
                    openT.cancel();//be sure to stop the opener if running...
                    closeT.scheduleRepeating(10);
                } else {
                    closeT.cancel();//be sure to stop the closer if running...
                    openT.scheduleRepeating(10);
                }
                opened = !opened;
            }
        });

        add(toggleButton, 192, 195);

    }

    public SettingsPanel getSettingsPanel() {
        return settingsPanel;
    }

    public void updateMapPosition() {
        setWidgetPosition(map, -5 + (MiniMap.MINIMAP_WIDTH - MainPanel.getInstance().getMiniMapUtils().getMiniMapWidth()) / 2, -11 + (MiniMap.MINIMAP_HEIGHT - MainPanel.getInstance().getMiniMapUtils().getMiniMapHeight()) / 2);
    }

    public MiniMap getMap() {
        return map;
    }
}
