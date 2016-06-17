package com.eaw1805.www.fieldbattle.views.layout.infopanels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.field.BrigadeMovementLogEntryDTO;
import com.eaw1805.data.dto.web.field.LongRangeAttackLogEntryDTO;
import com.eaw1805.data.dto.web.field.MeleeAttackLogEntryDTO;
import com.eaw1805.data.dto.web.field.RallyLogEntryDTO;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.CommanderStore;
import com.eaw1805.www.fieldbattle.stores.PlaybackStore;
import com.eaw1805.www.fieldbattle.stores.utils.ArmyUnitInfoDTO;
import com.eaw1805.www.fieldbattle.stores.utils.calculators.MiscCalculators;
import com.eaw1805.www.fieldbattle.tooltips.Tips;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.ScrollVerticalBar;
import com.eaw1805.www.fieldbattle.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;

import java.util.List;

public class PlaybackBrigadeInfoPanel extends AbsolutePanel {
    final Image lockUnlockImg = new Image();

    public PlaybackBrigadeInfoPanel(final BrigadeDTO brigade, final int round, final boolean enableRounds) {
        setStyleName("brigadeLogInfoPanel");
        setSize("280px", "225px");
        add(createLabel(brigade.getName()), 3, 3);

        updateLockImg();
        Tips.generateTip(lockUnlockImg, Tips.BRIGADE_LOCK);
        lockUnlockImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (MainPanel.getInstance().getPlaybackInfo().isLocked()) {
                    MainPanel.getInstance().getPlaybackInfo().unlockBrigadeInfo();

                } else {
                    MainPanel.getInstance().getPlaybackInfo().lockBrigadeInfo();

                }

            }
        });
        lockUnlockImg.setStyleName("pointer");
        final Label headerLabel = createLabel((round + 1) + "/" + PlaybackStore.getInstance().getRounds());
        add(headerLabel, 178, 8);
        lockUnlockImg.setSize("20px", "20px");
        add(lockUnlockImg, 253, 11);


        final ImageButton leftButton = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButLeftOff.png", Tips.BRIGADE_PREVIOUS_ROUND);
        leftButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (round + 1 > 0) {
                    MainPanel.getInstance().getPlaybackInfo().updateInfo(round - 1, false);
                }
            }
        });
        leftButton.setSize("22px", "22px");
        add(leftButton, 155, 6);


        final ImageButton rightButton = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButRightOff.png", Tips.BRIGADE_NEXT_ROUND);
        rightButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (!BaseStore.getInstance().isLastHalfRound(round + 1)) {
                    MainPanel.getInstance().getPlaybackInfo().updateInfo(round + 1, false);
                }
            }
        });
        rightButton.setSize("22px", "22px");
        add(rightButton, 215, 6);

        headerLabel.setVisible(enableRounds);
        lockUnlockImg.setVisible(enableRounds);
        leftButton.setVisible(enableRounds);
        rightButton.setVisible(enableRounds);


        final ArmyUnitInfoDTO unitInfo = MiscCalculators.getBrigadeInfo(brigade);
        CommanderDTO comm = null;
        if (brigade.getFieldBattleCommanderId() != 0) {
            comm = CommanderStore.getInstance().getCommanderById(brigade.getFieldBattleCommanderId());
        }
        if (brigade.getFieldBattleOverallCommanderId() != 0) {
            comm = CommanderStore.getInstance().getCommanderById(brigade.getFieldBattleOverallCommanderId());
        }

        int x = 3;

        if (comm != null) {
            final Image commImg = new Image("http://static.eaw1805.com/img/commanders/"
                    + GameStore.getInstance().getScenarioId() + "/"
                    + comm.getNationId() + "/" + comm.getIntId() + ".jpg");
            commImg.setSize("30px", "30px");
            add(commImg, x, 20);
            add(createLabel(comm.getName() + " - " + comm.getRank().getName() +
                    " (" + comm.getStrc() + "-" + comm.getComc() + ")"), x + 36, 20);

            x += 36;
        }

        final Image infImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/infantry.png");
        add(infImg, x, 36);
        add(createLabel(String.valueOf(unitInfo.getInfantry())), x + 28, 36);
        //infantry
        x += 60;
        final Image cavImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/cavalry.png");
        add(cavImg, x, 36);
        add(createLabel(String.valueOf(unitInfo.getCavalry())), x + 28, 36);
        //cavalry
        x += 60;
        final Image artImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/artillery.png");
        add(artImg, x, 36);
        add(createLabel(String.valueOf(unitInfo.getArtillery())), x + 28, 36);
        //artillery


        x = 3;
        for (final BattalionDTO battalion : brigade.getBattalions()) {
            Image battImg = new Image("http://static.eaw1805.com/images/armies/" + brigade.getNationId() + "/" + battalion.getEmpireArmyType().getIntId() + ".jpg");
            battImg.setTitle(battalion.getEmpireArmyType().getName());
            new ToolTipPanel(battImg, true) {
                @Override
                public void generateTip() {
                    try {
                        setTooltip(new ArmyTypeInfoPanel(battalion.getEmpireArmyType(),
                                battalion));
                    } catch (Exception e) {

                    }
                }
            };
            battImg.setWidth("42px");
            add(battImg, x, 53);
            add(createLabel(battalion.getExperience() + " - " + battalion.getHeadcount()), x, 93);
            x += 44;
        }

        VerticalPanel logEntries = new VerticalPanel();
        ScrollVerticalBar scroller = new ScrollVerticalBar(logEntries);
        scroller.setSize(270, 105);
        add(scroller, 3, 110);
        final RallyLogEntryDTO rallyEntry = PlaybackStore.getInstance().getRallyEntryByBrigadeRound(brigade.getBrigadeId(), round);
        if (rallyEntry != null) {
            final Label label = createHTML(PlaybackStore.getInstance().stringOfRallyEntryShort(rallyEntry, round));
            label.addMouseOverHandler(new MouseOverHandler() {
                @Override
                public void onMouseOver(MouseOverEvent mouseOverEvent) {
                    MainPanel.getInstance().getMapUtils().addPointer1IfCurrentRound(PlaybackStore.getInstance().getBrigadeByIdRound(rallyEntry.getBrigadeId(), round), round);
                }
            });
            label.addMouseOutHandler(new MouseOutHandler() {
                @Override
                public void onMouseOut(MouseOutEvent mouseOutEvent) {
                    MainPanel.getInstance().getMapUtils().clearPointers();
                }
            });
            logEntries.add(label);
        }
        final BrigadeMovementLogEntryDTO moveEntry = PlaybackStore.getInstance().getMovementEntryByBrigadeRound(brigade.getBrigadeId(), round);
        if (moveEntry != null) {
            final Label label = createHTML(PlaybackStore.getInstance().stringOfMovementEntryShort(moveEntry, round));
            label.addMouseOverHandler(new MouseOverHandler() {
                @Override
                public void onMouseOver(MouseOverEvent mouseOverEvent) {
                    MainPanel.getInstance().getMapUtils().addPointer1IfCurrentRound(PlaybackStore.getInstance().getBrigadeByIdRound(moveEntry.getBrigadeId(), round), round);
                }
            });
            label.addMouseOutHandler(new MouseOutHandler() {
                @Override
                public void onMouseOut(MouseOutEvent mouseOutEvent) {
                    MainPanel.getInstance().getMapUtils().clearPointers();
                }
            });
            logEntries.add(label);
        }
        final LongRangeAttackLogEntryDTO longRangeEntry = PlaybackStore.getInstance().getLongRangeByBrigadeRound(brigade.getBrigadeId(), round);
        if (longRangeEntry != null) {
            final Label label = createHTML(PlaybackStore.getInstance().stringOfLongRangeAttackShort(longRangeEntry, round));
            label.addMouseOverHandler(new MouseOverHandler() {
                @Override
                public void onMouseOver(MouseOverEvent mouseOverEvent) {
                    MainPanel.getInstance().getMapUtils().addPointersIfCurrentRound(PlaybackStore.getInstance().getBrigadeByIdRound(longRangeEntry.getAttackerBrigadeId(), round), PlaybackStore.getInstance().getBrigadeByIdRound(longRangeEntry.getTargetBrigadeId(), round), round);
                }
            });
            label.addMouseOutHandler(new MouseOutHandler() {
                @Override
                public void onMouseOut(MouseOutEvent mouseOutEvent) {
                    MainPanel.getInstance().getMapUtils().clearPointers();
                }
            });
            logEntries.add(label);
        }
        final MeleeAttackLogEntryDTO meleeEntry = PlaybackStore.getInstance().getMeleeByBrigadeRound(brigade.getBrigadeId(), round);
        if (meleeEntry != null) {
            final Label label = createHTML(PlaybackStore.getInstance().stringOfMeleeAttackShort(meleeEntry, round));
            label.addMouseOverHandler(new MouseOverHandler() {
                @Override
                public void onMouseOver(MouseOverEvent mouseOverEvent) {
                    MainPanel.getInstance().getMapUtils().addPointersIfCurrentRound(PlaybackStore.getInstance().getBrigadeByIdRound(meleeEntry.getAttackerBrigadeId(), round), PlaybackStore.getInstance().getBrigadeByIdRound(meleeEntry.getTargetBrigadeId(), round), round);
                }
            });
            label.addMouseOutHandler(new MouseOutHandler() {
                @Override
                public void onMouseOut(MouseOutEvent mouseOutEvent) {
                    MainPanel.getInstance().getMapUtils().clearPointers();
                }
            });
            logEntries.add(label);
        }

        final List<LongRangeAttackLogEntryDTO> reverseLongRangeEntries = PlaybackStore.getInstance().getLongRangeReverseByBrigadeRound(brigade.getBrigadeId(), round);
        if (reverseLongRangeEntries != null) {
            for (final LongRangeAttackLogEntryDTO entry : reverseLongRangeEntries) {
                final Label label = createHTML(PlaybackStore.getInstance().stringOfLongRangeReverseAttackShort(entry, round));
                label.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        MainPanel.getInstance().getMapUtils().addPointersIfCurrentRound(PlaybackStore.getInstance().getBrigadeByIdRound(entry.getAttackerBrigadeId(), round), PlaybackStore.getInstance().getBrigadeByIdRound(entry.getTargetBrigadeId(), round), round);
                    }
                });
                label.addMouseOutHandler(new MouseOutHandler() {
                    @Override
                    public void onMouseOut(MouseOutEvent mouseOutEvent) {
                        MainPanel.getInstance().getMapUtils().clearPointers();
                    }
                });
                logEntries.add(label);
            }
        }

        final List<MeleeAttackLogEntryDTO> reverseMeleeEntries = PlaybackStore.getInstance().getMeleeReverseByBrigadeRound(brigade.getBrigadeId(), round);
        if (reverseMeleeEntries != null) {
            for (final MeleeAttackLogEntryDTO entry : reverseMeleeEntries) {
                final Label label = createHTML(PlaybackStore.getInstance().stringOfMeleeReverseAttackShort(entry, round));
                label.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        MainPanel.getInstance().getMapUtils().addPointersIfCurrentRound(PlaybackStore.getInstance().getBrigadeByIdRound(entry.getAttackerBrigadeId(), round), PlaybackStore.getInstance().getBrigadeByIdRound(entry.getTargetBrigadeId(), round), round);
                    }
                });
                label.addMouseOutHandler(new MouseOutHandler() {
                    @Override
                    public void onMouseOut(MouseOutEvent mouseOutEvent) {
                        MainPanel.getInstance().getMapUtils().clearPointers();
                    }
                });
                logEntries.add(label);
            }
        }

    }

    public void updateLockImg() {
        if (MainPanel.getInstance().getPlaybackInfo().isLocked()) {
            lockUnlockImg.setUrl("http://static.eaw1805.com/images/field/extra/lock.png");
        } else {
            lockUnlockImg.setUrl("http://static.eaw1805.com/images/field/extra/unlock.png");

        }
    }

    public HTML createHTML(final String text) {
        HTML out = new HTML(text);
        out.setStyleName("whiteText");
        return out;
    }

    public Label createLabel(String text) {
        Label out = new Label(text);
        out.setStyleName("whiteText");
        return out;
    }

}
