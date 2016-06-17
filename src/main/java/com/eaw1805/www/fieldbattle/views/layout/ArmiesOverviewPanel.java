package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.model.army.Brigade;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.PlaybackStore;
import com.eaw1805.www.fieldbattle.stores.utils.ArmyUnitInfoDTO;
import com.eaw1805.www.fieldbattle.stores.utils.calculators.MiscCalculators;
import com.eaw1805.www.fieldbattle.tooltips.Tips;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.BrigadeInfoPanel;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.ScrollVerticalBar;

import java.util.ArrayList;
import java.util.List;

public class ArmiesOverviewPanel extends AbsolutePanel {
    private final ScrollVerticalBar scroller;
    final Label title;

    HorizontalPanel logContainer = new HorizontalPanel();
    int halfRound = 0;
    public ArmiesOverviewPanel() {
        setStyleName("tipPanel");
        setSize("870px", "600px");
        title = new Label("Half round " + halfRound);
        title.setStyleName("clearFont-Large");
        add(title, 363, 5);

        final Label vs = new Label("VS");
        vs.setStyleName("clearFontExtreme");
        add(vs, 400, 200);

        final ImageButton left = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButLeftOff.png", "View previous half round");
        left.setWidth("24px");
        add(left, 330, 5);
        left.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                halfRound--;
                if (halfRound < 0) {
                    halfRound = 0;
                }
                updateTurn(halfRound);
            }
        });

        final ImageButton right = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButRightOff.png", "View next half round");
        right.setWidth("24px");
        add(right, 546, 5);
        right.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                halfRound++;
                if (halfRound > PlaybackStore.getInstance().getRounds() - 1) {
                    halfRound = PlaybackStore.getInstance().getRounds() - 1;
                }
                updateTurn(halfRound);
            }
        });
        if (BaseStore.getInstance().isStartRound()) {
            right.setVisible(false);
            left.setVisible(false);
        }

        scroller = new ScrollVerticalBar(logContainer);
        scroller.setSize(850, 550);
        add(scroller, 25, 55);
    }

    private void showInitialTurn() {

        title.setText("Armies");

        logContainer.clear();
        final VerticalPanel side1Panel = new VerticalPanel();
        final VerticalPanel side2Panel = new VerticalPanel();

        final List<BrigadeDTO> freeAllied = ArmyStore.getInstance().getAlliedBrigadesByCorps(0);
        final List<BrigadeDTO> freeEnemy = ArmyStore.getInstance().getEnemyBrigadesByCorps(0);
        if (freeAllied.size() > 0) {
            side1Panel.add(createHeader("Free Brigades (100)", freeAllied));
        }
        if (freeEnemy.size() > 0) {
            side2Panel.add(createHeader("Free Brigades (100)", freeEnemy));
        }


        for (BrigadeDTO brigade : freeAllied) {
            side1Panel.add(new BrigadeInfoPanel(brigade, false, false, true));
        }

        for (BrigadeDTO brigade : freeEnemy) {
            side2Panel.add(new BrigadeInfoPanel(brigade, false, false, true));
        }

        logContainer.add(side1Panel);
        AbsolutePanel spacer = new AbsolutePanel();
        spacer.setSize("100px", "100px");
        logContainer.add(spacer);
        logContainer.add(side2Panel);
        //finally be sure bar has the appropriate behavior for the new content...
        scroller.resizeBar();
    }


    public void updateTurn(final int halfRound) {
        if (BaseStore.getInstance().isStartRound()) {
            showInitialTurn();
            return;
        }
        title.setText("Half round " + halfRound);
        this.halfRound =halfRound;
        logContainer.clear();
        final VerticalPanel side1Panel = new VerticalPanel();
        final VerticalPanel side2Panel = new VerticalPanel();
        boolean alliedHaveEmpty = false;
        boolean enemyHaveEmpty = false;

        for (final BrigadeDTO brigade : PlaybackStore.getInstance().getRoundStatistics(0).getAllBrigades()) {
            if (brigade.getCorpId() == 0) {
                if (BaseStore.getInstance().isNationAllied(brigade.getNationId())) {
                    alliedHaveEmpty = true;
                } else {
                    enemyHaveEmpty = true;
                }
            }
            if (alliedHaveEmpty && enemyHaveEmpty) {
                break;
            }
        }


        final List<BrigadeDTO> freeAllied = PlaybackStore.getInstance().getAlliedBrigadesByCorpsRound(0, 0);
        final List<BrigadeDTO> freeEnemy = PlaybackStore.getInstance().getEnemyBrigadesByCorpsRound(0, 0);
        if (freeAllied.size() > 0) {
            side1Panel.add(createHeader("Free Brigades (" + PlaybackStore.getInstance().getMoraleByRoundNation(halfRound, BaseStore.getInstance().getNationId()) + ")", freeAllied));
        }
        if (freeEnemy.size() > 0) {
            side2Panel.add(createHeader("Free Brigades (" + PlaybackStore.getInstance().getMoraleByRoundNation(halfRound, BaseStore.getInstance().getEnemyNations().get(0)) + ")", freeEnemy));
        }


        for (BrigadeDTO brigade : freeAllied) {
            //first check if brigade exists in this round.
            final BrigadeDTO curBrig = PlaybackStore.getInstance().getBrigadeByIdRound(brigade.getBrigadeId(), halfRound);
            if (curBrig == null) {//that means brigade no longer exists...
                side1Panel.add(new BrigadeInfoPanel(brigade, true, PlaybackStore.getInstance().isBrigadeFleeingByRound(brigade.getBrigadeId(), halfRound), true));
            } else {//that means brigade exists... so show info panel
                side1Panel.add(new BrigadeInfoPanel(curBrig, false, PlaybackStore.getInstance().isBrigadeFleeingByRound(curBrig.getBrigadeId(), halfRound), true));

            }
        }

        for (BrigadeDTO brigade : freeEnemy) {
            //first check if brigade exists in this round.
            final BrigadeDTO curBrig = PlaybackStore.getInstance().getBrigadeByIdRound(brigade.getBrigadeId(), halfRound);
            if (curBrig == null) {//that means brigade no longer exists...
                side2Panel.add(new BrigadeInfoPanel(brigade, true, PlaybackStore.getInstance().isBrigadeFleeingByRound(brigade.getBrigadeId(), halfRound), true));
            } else {//that means brigade exists... so show info panel
                side2Panel.add(new BrigadeInfoPanel(curBrig, false, PlaybackStore.getInstance().isBrigadeFleeingByRound(curBrig.getBrigadeId(), halfRound), true));

            }
        }


        //then find all empty corps
        for (final CorpDTO corp : ArmyStore.getInstance().getArmyData().getArmyToCorps().get(0)) {
            if (corp.getCorpId() == 0) {
                //we already added those above.
                continue;
            }
            if (corp.getCorpId() != 0) {
                if (BaseStore.getInstance().isNationAllied(corp.getNationId())) {
                    side1Panel.add(clearFontLabel("Corps : " + corp.getName()));
                } else {
                    side2Panel.add(clearFontLabel("Corps : " + corp.getName()));
                }
            }
            for (final BrigadeDTO brigade : PlaybackStore.getInstance().getRoundStatistics(0).getAllBrigades()) {

                if (brigade.getCorpId() == corp.getCorpId()) {
                    //first check if brigade exists in this round.
                    final BrigadeDTO curBrig = PlaybackStore.getInstance().getBrigadeByIdRound(brigade.getBrigadeId(), halfRound);

                    if (curBrig == null) {//that means brigade no longer exists...
                        if (BaseStore.getInstance().isNationAllied(brigade.getNationId())) {
                            side1Panel.add(new BrigadeInfoPanel(brigade, true, PlaybackStore.getInstance().isBrigadeFleeingByRound(brigade.getBrigadeId(), halfRound), true));
                        } else {
                            side2Panel.add(new BrigadeInfoPanel(brigade, true, PlaybackStore.getInstance().isBrigadeFleeingByRound(brigade.getBrigadeId(), halfRound), true));
                        }
                    } else {//that means brigade exists... so show info panel
                        if (BaseStore.getInstance().isNationAllied(curBrig.getNationId())) {
                            side1Panel.add(new BrigadeInfoPanel(curBrig, false, PlaybackStore.getInstance().isBrigadeFleeingByRound(curBrig.getBrigadeId(), halfRound), true));

                        } else {
                            side2Panel.add(new BrigadeInfoPanel(curBrig, false, PlaybackStore.getInstance().isBrigadeFleeingByRound(curBrig.getBrigadeId(), halfRound), true));
                        }

                    }
                }
            }
        }



        for (final ArmyDTO army : ArmyStore.getInstance().getArmyData().getArmies().values()) {
            if (army.getArmyId() == 0) {
                //we already added those above
                continue;
            }
            if (army.getArmyId() != 0) {
                if (BaseStore.getInstance().isNationAllied(army.getNationId())) {
                    side1Panel.add(clearFontLabel("Army : " + army.getName()));
                } else {
                    side2Panel.add(clearFontLabel("Army : " + army.getName()));
                }
            }

            for (final CorpDTO corp : ArmyStore.getInstance().getArmyData().getArmyToCorps().get(army.getArmyId())) {
                if (corp.getCorpId() != 0) {
                    if (BaseStore.getInstance().isNationAllied(corp.getNationId())) {
                        side1Panel.add(clearFontLabel("Corps : " + corp.getName()));
                    } else {
                        side2Panel.add(clearFontLabel("Corps : " + corp.getName()));
                    }
                }
                for (final BrigadeDTO brigade : PlaybackStore.getInstance().getRoundStatistics(0).getAllBrigades()) {

                    if (brigade.getCorpId() == corp.getCorpId()) {
                        //first check if brigade exists in this round.
                        final BrigadeDTO curBrig = PlaybackStore.getInstance().getBrigadeByIdRound(brigade.getBrigadeId(), halfRound);

                        if (curBrig == null) {//that means brigade no longer exists...
                            if (BaseStore.getInstance().isNationAllied(brigade.getNationId())) {
                                side1Panel.add(new BrigadeInfoPanel(brigade, true, PlaybackStore.getInstance().isBrigadeFleeingByRound(brigade.getBrigadeId(), halfRound), true));
                            } else {
                                side2Panel.add(new BrigadeInfoPanel(brigade, true, PlaybackStore.getInstance().isBrigadeFleeingByRound(brigade.getBrigadeId(), halfRound), true));
                            }
                        } else {//that means brigade exists... so show info panel
                            if (BaseStore.getInstance().isNationAllied(curBrig.getNationId())) {
                                side1Panel.add(new BrigadeInfoPanel(curBrig, false, PlaybackStore.getInstance().isBrigadeFleeingByRound(curBrig.getBrigadeId(), halfRound), true));

                            } else {
                                side2Panel.add(new BrigadeInfoPanel(curBrig, false, PlaybackStore.getInstance().isBrigadeFleeingByRound(curBrig.getBrigadeId(), halfRound), true));
                            }

                        }
                    }
                }
            }
        }
        logContainer.add(side1Panel);
        AbsolutePanel spacer = new AbsolutePanel();
        spacer.setSize("100px", "100px");
        logContainer.add(spacer);
        logContainer.add(side2Panel);
        //finally be sure bar has the appropriate behavior for the new content...
        scroller.resizeBar();

    }

    public AbsolutePanel createHeader(final String text, final List<BrigadeDTO> brigs) {
        final AbsolutePanel out = new AbsolutePanel();
        out.setSize("366px", "55px");
        out.add(clearFontLabel(text), 0, 0);
        final ArmyUnitInfoDTO info = new ArmyUnitInfoDTO();
        for (BrigadeDTO brig : brigs) {
            info.addToInfo(MiscCalculators.getBrigadeInfo(brig));
        }
        final Image infantry = new Image("http://static.eaw1805.com/images/buttons/icons/formations/infantry.png");
        Tips.generateTip(infantry, "Infantry");
        out.add(infantry, 9, 31);
        out.add(new Label(String.valueOf(info.getInfantry())), 41, 31);

        final Image cavalry = new Image("http://static.eaw1805.com/images/buttons/icons/formations/cavalry.png");
        Tips.generateTip(cavalry, "Cavalry");
        out.add(cavalry, 133, 31);
        out.add(new Label(String.valueOf(info.getCavalry())), 165, 31);

        final Image artillery = new Image("http://static.eaw1805.com/images/buttons/icons/formations/artillery.png");
        Tips.generateTip(artillery, "Artillery");
        out.add(artillery, 252, 31);
        out.add(new Label(String.valueOf(info.getArtillery())), 284, 31);
        return out;
    }


    public Label clearFontLabel(String text) {
        Label out = new Label(text);
        out.setStyleName("clearFont-Large");
        return out;
    }

    public void resizeBar() {
        scroller.resizeBar();
    }



}
