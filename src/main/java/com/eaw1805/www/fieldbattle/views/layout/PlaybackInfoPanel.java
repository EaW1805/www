package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.fieldbattle.stores.PlaybackStore;
import com.eaw1805.www.fieldbattle.tooltips.Tips;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.PlaybackBrigadeInfoPanel;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;


public class PlaybackInfoPanel extends AbsolutePanel {


    AbsolutePanel selectedContainer = new AbsolutePanel();
    AbsolutePanel terrainInfo = new AbsolutePanel();
    private int thisRound = 0;
    boolean lockBrigadeInfo = false;
    private BrigadeDTO brigade = null;
    PlaybackBrigadeInfoPanel panel;


    public PlaybackInfoPanel() {
        setStyleName("brigadeLogPanel");
        setSize("315px", "300px");
//        createHeader();
        createSelectedInfo();

        final Timer closeT = new Timer() {
            @Override
            public void run() {
                if (getAbsoluteLeft() > -275) {
                    MainPanel.getInstance().setWidgetPosition(PlaybackInfoPanel.this, getAbsoluteLeft() - 5, Window.getClientHeight() - 425);
                } else {
                    MainPanel.getInstance().setWidgetPosition(PlaybackInfoPanel.this, -275, Window.getClientHeight() - 425);
                    cancel();
                }

            }
        };

        final Timer openT = new Timer() {
            @Override
            public void run() {
                if (getAbsoluteLeft() < 0) {
                    MainPanel.getInstance().setWidgetPosition(PlaybackInfoPanel.this, getAbsoluteLeft() + 5, Window.getClientHeight() - 425);
                } else {
                    MainPanel.getInstance().setWidgetPosition(PlaybackInfoPanel.this, 0, Window.getClientHeight() - 425);
                    cancel();
                }
            }
        };

        final ImageButton toggleButton = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchOff.png", Tips.ELEMENT_SHOW_PANELS);


        toggleButton.setSize("24px", "24px");
        toggleButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (MainPanel.getInstance().getArmyInfo().isOpened()) {
                    openT.cancel();//be sure to stop the opener if running...
                    closeT.scheduleRepeating(10);
                    MainPanel.getInstance().getArmyInfo().close();
                } else {
                    closeT.cancel();//be sure to stop the closer if running...
                    openT.scheduleRepeating(10);
                    MainPanel.getInstance().getArmyInfo().open();
                }
                MainPanel.getInstance().getArmyInfo().setOpened(!MainPanel.getInstance().getArmyInfo().isOpened());
            }
        });

        add(toggleButton, 268, 24);

    }


    public void updateSelectedInfo(final BrigadeDTO brigade, final boolean hard, final boolean updatePointer) {
        if (!lockBrigadeInfo || hard) {
            this.brigade = brigade;
            selectedContainer.clear();
            panel = new PlaybackBrigadeInfoPanel(brigade, thisRound, true);
            selectedContainer.add(panel);
            if (updatePointer) {
                MainPanel.getInstance().getMapUtils().clearPointers();
                MainPanel.getInstance().getMapUtils().addPointer1(brigade);
            }
        }
    }


    public void lockBrigadeInfo() {
        lockBrigadeInfo = true;
        if (panel != null) {
            panel.updateLockImg();
        }
    }

    public void unlockBrigadeInfo() {
        lockBrigadeInfo = false;
        if (panel != null) {
            panel.updateLockImg();
        }
    }

    public void updateInfo(int round, final boolean updatePointer) {
        thisRound = round;
//        headerLabel.setText("Half Round " + (round + 1));


        if (brigade != null) {
            final BrigadeDTO newBrigade = PlaybackStore.getInstance().getBrigadeByIdRound(brigade.getBrigadeId(), thisRound);
            if (newBrigade != null) {
                updateSelectedInfo(newBrigade, true, updatePointer);
//                MainPanel.getInstance().getMapUtils().gotoBrigade(newBrigade);
            } else {
                selectedContainer.clear();
            }
        }
    }


    public void createSelectedInfo() {
        selectedContainer.setSize("280px", "225px");
        add(selectedContainer, 10, 55);
    }

    public boolean isLocked() {
        return lockBrigadeInfo;
    }


    public Label createLabel(String text) {
        Label out = new Label(text);
        out.setStyleName("whiteText");
        return out;
    }
}
