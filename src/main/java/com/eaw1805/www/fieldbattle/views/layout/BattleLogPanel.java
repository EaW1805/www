package com.eaw1805.www.fieldbattle.views.layout;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.field.BrigadeMovementLogEntryDTO;
import com.eaw1805.data.dto.web.field.LongRangeAttackLogEntryDTO;
import com.eaw1805.data.dto.web.field.MeleeAttackLogEntryDTO;
import com.eaw1805.data.dto.web.field.RallyLogEntryDTO;
import com.eaw1805.www.fieldbattle.stores.PlaybackStore;
import com.eaw1805.www.fieldbattle.tooltips.Tips;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.SingleLogInfoPanel;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.mini.LogTitleInfoPanel;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.ScrollVerticalBar;

import java.util.HashMap;
import java.util.Map;

public class BattleLogPanel extends AbsolutePanel {
    private final ScrollVerticalBar scroller;
    private final VerticalPanel logContainer = new VerticalPanel();

    private final Map<Integer, LogTitleInfoPanel> mapLabels;
    boolean opened = true;

    public BattleLogPanel() {
        setStyleName("battleLogPanel");
        setSize("275px", "415px");
        scroller = new ScrollVerticalBar(logContainer);
        scroller.setSize(245, 350);
        add(scroller, 25, 55);
        mapLabels = new HashMap<Integer, LogTitleInfoPanel>();

        final Timer closeT = new Timer() {
            @Override
            public void run() {
                if (getAbsoluteLeft() < Window.getClientWidth() - 25) {
                    MainPanel.getInstance().setWidgetPosition(BattleLogPanel.this, getAbsoluteLeft() + 5, Window.getClientHeight() - 415);
                } else {
                    MainPanel.getInstance().setWidgetPosition(BattleLogPanel.this, Window.getClientWidth() - 25, Window.getClientHeight() - 415);
                    cancel();
                }

            }
        };

        final Timer openT = new Timer() {
            @Override
            public void run() {
                if (getAbsoluteLeft() > Window.getClientWidth() - 275) {
                    MainPanel.getInstance().setWidgetPosition(BattleLogPanel.this, getAbsoluteLeft() - 5, Window.getClientHeight() - 415);

                } else {
                    MainPanel.getInstance().setWidgetPosition(BattleLogPanel.this, Window.getClientWidth() - 275, Window.getClientHeight() - 415);
                    cancel();
                }
            }
        };


        final ImageButton toggleButton = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchOff.png", Tips.ELEMENT_SHOW_PANELS);


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

        add(toggleButton, 20, 20);
    }

    public boolean isOpened() {
        return opened;
    }

    public void initPanel() {
        for (int round = 0; round < PlaybackStore.getInstance().getRounds(); round++) {
            int count = 0;
            mapLabels.put(round, createLabel(PlaybackStore.getInstance().getRoundName(round), round, 0));
            logContainer.add(mapLabels.get(round));
            logContainer.add(createLabel("Rally check", round, 1));
            for (RallyLogEntryDTO entry : PlaybackStore.getInstance().getRallyEntriesByRound(round)) {
                count++;
                logContainer.add(createLabel(PlaybackStore.getInstance().stringOfRallyEntry(entry, round), round, entry.getBrigadeId(), 1, "Rally check"));
            }
            logContainer.add(createLabel("Fleeing brigades", round, 2));
            for (Integer entry : PlaybackStore.getInstance().getFleeingBrigadesByRound(round)) {
                count++;
                logContainer.add(createLabel(PlaybackStore.getInstance().stringOfFleeingEntry(entry, round), round, entry, 2, "Fleeing brigades"));
            }
            logContainer.add(createLabel("Movements", round, 3));
            for (BrigadeMovementLogEntryDTO entry : PlaybackStore.getInstance().getMovementsByRound(round)) {
                count++;
                logContainer.add(createLabel(PlaybackStore.getInstance().stringOfMovementEntry(entry, round), round, entry.getBrigadeId(), 3, "Movements"));
            }
            logContainer.add(createLabel("Long range fire", round, 4));
            for (LongRangeAttackLogEntryDTO entry : PlaybackStore.getInstance().getLongRangeByRound(round)) {
                count++;
                logContainer.add(createLabel(PlaybackStore.getInstance().stringOfLongRangeAttack(entry, round), round, entry.getAttackerBrigadeId(), 4, "Long range fire"));
            }
            logContainer.add(createLabel("Melee attacks", round, 5));
            for (MeleeAttackLogEntryDTO entry : PlaybackStore.getInstance().getMeleeAttacksByRound(round)) {
                count++;
                logContainer.add(createLabel(PlaybackStore.getInstance().stringOfMeleeAttack(entry, round), round, entry.getAttackerBrigadeId(), 5, "Melee attack"));
            }
        }
        scroller.resizeBar();
    }

    public void resizeBar() {
        scroller.resizeBar();
    }

    public LogTitleInfoPanel createLabel(String text, final int round, final int type) {
        LogTitleInfoPanel out = new LogTitleInfoPanel(text);
        switch (type) {
            case 1:
                out.getElement().getStyle().setBackgroundColor("rgba(0, 200, 200, 0.5)");
                break;
            case 2:
                out.getElement().getStyle().setBackgroundColor("rgba(200, 0, 200, 0.5)");
                break;
            case 3:
                out.getElement().getStyle().setBackgroundColor("rgba(200, 200, 0, 0.5)");
                break;
            case 4:
                out.getElement().getStyle().setBackgroundColor("rgba(0, 0, 200, 0.5)");
                break;
            case 5:
                out.getElement().getStyle().setBackgroundColor("rgba(0, 200, 0, 0.5)");
                break;
            default:
                out.getElement().getStyle().setBackgroundColor("rgba(200, 0, 0, 0.5)");
                break;
        }
        out.setTitle(PlaybackStore.getInstance().getRoundName(round));
        return out;
    }

    public SingleLogInfoPanel createLabel(final String text, final int round, final int brigadeId, final int type, final String title) {
        final SingleLogInfoPanel out = new SingleLogInfoPanel(text);
        out.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                final BrigadeDTO brig = PlaybackStore.getInstance().getBrigadeByIdRound(brigadeId, round);
                MainPanel.getInstance().getPlayback().goToRound(round + 1);
                MainPanel.getInstance().getMapUtils().gotoBrigade(brig);
                MainPanel.getInstance().getMapUtils().deselectAllBrigades(round);
                MainPanel.getInstance().getMapUtils().selectBrigade(brig, false, round);
                MainPanel.getInstance().getPlaybackInfo().updateSelectedInfo(brig, true, true);
                MainPanel.getInstance().getPlaybackInfo().lockBrigadeInfo();
            }
        }, ClickEvent.getType());
        //just generate for each type a different color code
        switch (type) {
            case 1:
                out.getElement().getStyle().setBackgroundColor("rgba(0, 200, 200, 0.5)");
                break;
            case 2:
                out.getElement().getStyle().setBackgroundColor("rgba(200, 0, 200, 0.5)");
                break;
            case 3:
                out.getElement().getStyle().setBackgroundColor("rgba(200, 200, 0, 0.5)");
                break;
            case 4:
                out.getElement().getStyle().setBackgroundColor("rgba(0, 0, 200, 0.5)");
                break;
            case 5:
                out.getElement().getStyle().setBackgroundColor("rgba(0, 200, 0, 0.5)");
                break;
            default:
                out.getElement().getStyle().setBackgroundColor("rgba(200, 0, 0, 0.5)");
                break;
        }
        out.setTitle(PlaybackStore.getInstance().getRoundName(round) + ", " + title);
        out.setStyleName("pointer", true);
        return out;
    }

}
