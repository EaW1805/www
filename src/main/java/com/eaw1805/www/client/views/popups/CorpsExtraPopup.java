package com.eaw1805.www.client.views.popups;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.views.infopanels.units.BrigadeInfoPanel;
import com.eaw1805.www.client.widgets.BrowserWidget;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RefreshAble;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.util.ArmyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

public class CorpsExtraPopup extends AbsolutePanel implements RefreshAble {

    private int x;
    private int y;
    private final CorpDTO corps;
    private final BrowserWidget browser;

    public CorpsExtraPopup(final CorpDTO corp, final BrowserWidget browser) {
        corps = corp;
        this.browser = browser;
        refreshContent();
    }

    public void refreshContent() {
        clear();
        final ArmyUnitInfoDTO unitInfo = MiscCalculators.getCorpInfo(corps);
        setSize("193px", "140px");
        y = 3;
        x = 3;

        add(createLabel("Exp: " + unitInfo.getAvgExperience() + "/" + unitInfo.getMaxExperience()), x, y);
        final ImageButton train = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        train.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (!train.isDisabled()) {
                    ArmyStore.getInstance().upgradeCorps(corps.getCorpId(), false, RegionStore.getInstance().getRegionSectorsByRegionId(corps.getRegionId())[corps.getXStart()][corps.getYStart()]);
                    refreshContent();
                }
            }
        });
        train.setSize("15px", "15px");
        add(train, 160, y);
        final ImageButton undoTrain = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        undoTrain.setSize("15px", "15px");
        undoTrain.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (!undoTrain.isDisabled()) {
                    ArmyStore.getInstance().cancelUpgradeCorps(corps.getCorpId(), false, corps.getRegionId(), false);
                    refreshContent();
                }
            }
        });
        add(undoTrain, 175, y);
        if (corps.isUpgraded()) {
            train.setDisabled(true);
            undoTrain.setDisabled(false);
        } else if (ArmyStore.getInstance().canCorpsUpgrade(corps, false)) {
            train.setDisabled(false);
            undoTrain.setDisabled(true);
        } else {
            train.setDisabled(true);
            undoTrain.setDisabled(true);
        }

        y += 10;
        add(createLabel("Crack elite: " + unitInfo.getNumOfBattalionsNeedCrackElite() + " battalions"), x, y);
        final ImageButton crack = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        crack.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (!crack.isDisabled()) {
                    ArmyStore.getInstance().upgradeCorps(corps.getCorpId(), true, RegionStore.getInstance().getRegionSectorsByRegionId(corps.getRegionId())[corps.getXStart()][corps.getYStart()]);
                    refreshContent();
                }
            }
        });
        crack.setSize("15px", "15px");
        add(crack, 160, y);
        final ImageButton undoCrack = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        undoCrack.setSize("15px", "15px");
        undoCrack.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (!undoCrack.isDisabled()) {
                    ArmyStore.getInstance().cancelUpgradeCorps(corps.getCorpId(), true, corps.getRegionId(), false);
                    refreshContent();
                }
            }
        });
        add(undoCrack, 175, y);

        if (corps.isUpgradedToElite()) {
            crack.setDisabled(true);
            undoCrack.setDisabled(false);
        } else if (ArmyStore.getInstance().canCorpsUpgrade(corps, true)) {
            crack.setDisabled(false);
            undoCrack.setDisabled(true);
        } else {
            crack.setDisabled(true);
            undoCrack.setDisabled(true);
        }
        y += 10;
        add(createLabel("Headcount: " + unitInfo.getHeadCount() + "/" + unitInfo.getMaxHeadCount()), x, y);
        final ImageButton incr = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        incr.setSize("15px", "15px");
        incr.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (!incr.isDisabled()) {
                    ArmyStore.getInstance().upHeadCountCorps(corps.getCorpId(), 1000, RegionStore.getInstance().getRegionSectorsByRegionId(corps.getRegionId())[corps.getXStart()][corps.getYStart()]);
                    refreshContent();
                }
            }
        });
        add(incr, 160, y);

        final ImageButton undoIncr = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        undoIncr.setSize("15px", "15px");
        undoIncr.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (!undoIncr.isDisabled()) {
                    ArmyStore.getInstance().cancelUpHeadCountCorps(corps.getCorpId());
                    refreshContent();
                }
            }
        });
        add(undoIncr, 175, y);

        if (corps.isUpHeadcount()) {
            incr.setDisabled(true);
            undoIncr.setDisabled(false);
        } else if (unitInfo.getHeadCount() < unitInfo.getMaxHeadCount()) {
            incr.setDisabled(false);
            undoIncr.setDisabled(true);
        } else {
            incr.setDisabled(true);
            undoIncr.setDisabled(true);
        }
        y += 10;

        add(createLabel("Brigades"), x, y);
        y += 10;

        final AbsolutePanel brigadesContainer = new AbsolutePanel();
        int rows = corps.getBrigades().size() / 5 + 1;
        brigadesContainer.setSize("193px", (rows * 35 + 5) + "px");
        final ScrollVerticalBarEAW scroller = new ScrollVerticalBarEAW(brigadesContainer, false);
        scroller.setSize(193, 70);
        add(scroller, x, y);
        x = 3;
        y = 3;
        for (final BrigadeDTO brigade : corps.getBrigades().values()) {
            Image unitImg = new Image("http://static.eaw1805.com/images/figures/" + corps.getNationId() + "/UnitMap00.png");
            unitImg.setSize("32px", "32px");
            browser.addLink(unitImg, new BrigadeExtraPopup(brigade));
            brigadesContainer.add(unitImg, x, y);
            new ToolTipPanel(unitImg) {
                @Override
                public void generateTip() {
                    setTooltip(new BrigadeInfoPanel(brigade, false));
                }
            };
            x += 35;
            if (x >= 193 - 35) {
                x = 3;
                y += 35;
            }
        }
        scroller.resizeBar();
    }


    public Label createLabel(final String text) {
        final Label out = new Label();
        out.setText(text);
        return out;
    }

}
