package com.eaw1805.www.client.views.popups;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RefreshAble;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.util.ArmyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

public class BrigadeExtraPopup extends AbsolutePanel implements RefreshAble {

    int x;
    int y;
    private final BrigadeDTO brigade;
    public BrigadeExtraPopup(final BrigadeDTO brigade) {
        this.brigade = brigade;
        refreshContent();
    }

    public void refreshContent() {
        clear();
        final ArmyUnitInfoDTO unitInfo = MiscCalculators.getBrigadeInfo(brigade);
        setSize("193px", "140px");
        y = 3;
        x = 3;

        add(createLabel("Exp: " + unitInfo.getAvgExperience()+"/"+unitInfo.getMaxExperience()), x, y);
        final ImageButton train = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        train.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (!train.isDisabled()) {
                    ArmyStore.getInstance().upgradeBrigade(brigade.getBrigadeId(), false, RegionStore.getInstance().getRegionSectorsByRegionId(brigade.getRegionId())[brigade.getXStart()][brigade.getYStart()]);
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
                    ArmyStore.getInstance().cancelUpgradeBrigade(brigade.getBrigadeId(), false, brigade.getRegionId(), false);
                    refreshContent();
                }
            }
        });
        add(undoTrain, 175, y);
        if (brigade.isUpgraded()) {
            train.setDisabled(true);
            undoTrain.setDisabled(false);
        } else if (ArmyStore.getInstance().canBrigadeUpgrade(brigade, false)) {
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
                    ArmyStore.getInstance().upgradeBrigade(brigade.getBrigadeId(), true, RegionStore.getInstance().getRegionSectorsByRegionId(brigade.getRegionId())[brigade.getXStart()][brigade.getYStart()]);
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
                    ArmyStore.getInstance().cancelUpgradeBrigade(brigade.getBrigadeId(), true, brigade.getRegionId(), false);
                    refreshContent();
                }
            }
        });
        add(undoCrack, 175, y);

        if (brigade.isUpgradedToElite()) {
            crack.setDisabled(true);
            undoCrack.setDisabled(false);
        } else if (ArmyStore.getInstance().canBrigadeUpgrade(brigade, true)) {
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
                    ArmyStore.getInstance().upHeadCountBrigade(brigade.getBrigadeId(), 1000, RegionStore.getInstance().getRegionSectorsByRegionId(brigade.getRegionId())[brigade.getXStart()][brigade.getYStart()]);
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
                    ArmyStore.getInstance().cancelUpHeadCountBrigade(brigade.getBrigadeId());
                    refreshContent();
                }
            }
        });
        add(undoIncr, 175, y);

        if (brigade.IsUpHeadcount()) {
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

    }

    public Label createLabel(final String text) {
        final Label out = new Label();
        out.setText(text);
        return out;
    }

}
