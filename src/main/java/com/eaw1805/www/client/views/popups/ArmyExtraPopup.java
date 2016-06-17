package com.eaw1805.www.client.views.popups;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.views.infopanels.units.CorpsInfoPanel;
import com.eaw1805.www.client.widgets.BrowserWidget;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RefreshAble;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.util.ArmyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

public class ArmyExtraPopup extends AbsolutePanel implements RefreshAble {

    private int x;
    private int y;
    private final ArmyDTO army;
    private final BrowserWidget browser;
    public ArmyExtraPopup(final ArmyDTO army, final BrowserWidget browser) {
        this.army = army;
        this.browser = browser;
        refreshContent();
    }

    public void refreshContent() {
        clear();
        final ArmyUnitInfoDTO unitInfo = MiscCalculators.getArmyInfo(army);
        setSize("193px", "140px");
        y = 3;
        x = 3;

        add(createLabel("Exp: " + unitInfo.getAvgExperience()+"/"+unitInfo.getMaxExperience()), x, y);
        final ImageButton train = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        train.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (!train.isDisabled()) {
                    ArmyStore.getInstance().upgradeArmy(army.getArmyId(), false, RegionStore.getInstance().getRegionSectorsByRegionId(army.getRegionId())[army.getXStart()][army.getYStart()]);
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
                    ArmyStore.getInstance().cancelUpgradeArmy(army.getArmyId(), false, army.getRegionId(), false);
                    refreshContent();
                }
            }
        });
        add(undoTrain, 175, y);
        if (army.isUpgraded()) {
            train.setDisabled(true);
            undoTrain.setDisabled(false);
        } else if (ArmyStore.getInstance().canArmyUpgrade(army, false)) {
            train.setDisabled(false);
            undoTrain.setDisabled(true);
        } else {
            train.setDisabled(true);
            undoTrain.setDisabled(true);
        }

        y += 10;
        add(createLabel("Crack elite: " + unitInfo.getNumOfBattalionsNeedCrackElite()+" battalions"), x, y);
        final ImageButton crack = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        crack.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (!crack.isDisabled()) {
                    ArmyStore.getInstance().upgradeArmy(army.getArmyId(), true, RegionStore.getInstance().getRegionSectorsByRegionId(army.getRegionId())[army.getXStart()][army.getYStart()]);
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
                    ArmyStore.getInstance().cancelUpgradeArmy(army.getArmyId(), true, army.getRegionId(), false);
                    refreshContent();
                }
            }
        });
        add(undoCrack, 175, y);

        if (army.isUpgradedToElite()) {
            crack.setDisabled(true);
            undoCrack.setDisabled(false);
        } else if (ArmyStore.getInstance().canArmyUpgrade(army, true)) {
            crack.setDisabled(false);
            undoCrack.setDisabled(true);
        } else {
            crack.setDisabled(true);
            undoCrack.setDisabled(true);
        }
        y += 10;
        add(createLabel("Headcount: " + unitInfo.getHeadCount() +"/"+unitInfo.getMaxHeadCount()), x, y);
        final ImageButton incr = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        incr.setSize("15px", "15px");
        incr.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (!incr.isDisabled()) {
                    ArmyStore.getInstance().upHeadCountArmy(army.getArmyId(), 1000, RegionStore.getInstance().getRegionSectorsByRegionId(army.getRegionId())[army.getXStart()][army.getYStart()]);
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
                    ArmyStore.getInstance().cancelUpHeadCountArmy(army.getArmyId());
                    refreshContent();
                }
            }
        });
        add(undoIncr, 175, y);

        if (army.isUpHeadcount()) {
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
        add(createLabel("Corps"), x, y);
        y+=10;
        for (final CorpDTO corps : army.getCorps().values()) {
            Image corpsBack = new Image("http://static.eaw1805.com/images/figures/corpBase.png");
            corpsBack.setSize("32px", "32px");
            Image unitImg = new Image("http://static.eaw1805.com/images/figures/" + army.getNationId() + "/UnitMap00.png");
            browser.addLink(unitImg, new CorpsExtraPopup(corps, browser));
            new ToolTipPanel(unitImg) {
                @Override
                public void generateTip() {
                    setTooltip(new CorpsInfoPanel(corps, false));
                }
            };
            unitImg.setSize("32px", "32px");
            add(corpsBack, x, y);
            add(unitImg, x, y);
            x += 35;
        }
    }

    public Label createLabel(final String text) {
        final Label out = new Label();
        out.setText(text);
        return out;
    }

}
