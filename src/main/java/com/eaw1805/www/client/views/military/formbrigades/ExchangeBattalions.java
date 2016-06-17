package com.eaw1805.www.client.views.military.formbrigades;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.views.infopanels.units.mini.BattInfoMini;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.ExchangeBattalionsHandler;
import com.eaw1805.www.shared.stores.util.ClientUtil;

public class ExchangeBattalions
        extends AbsolutePanel
        implements ArmyConstants {

    private transient final Label lblFName;
    private transient final Label lblSName;
    private transient final ClickAbsolutePanel[] fBrigPanels = new ClickAbsolutePanel[6];
    private transient final ClickAbsolutePanel[] secBrigPanels = new ClickAbsolutePanel[6];
    private transient final PickupDragController dndCtrl;
    private transient BrigadeDTO fBrig, sBrig;
    private final UnitChangedHandler unitChangedHandler;
    public ExchangeBattalions(final FormBrigadesView parent) {
        setSize("720px", "547px");
        dndCtrl = new PickupDragController(this, false);
        dndCtrl.addDragHandler(new ExchangeBattalionsHandler(parent));

        final Label lblSelectedBrigade = new Label("Selected Brigade:");
        lblSelectedBrigade.setStyleName("clearFontMedLarge");
        add(lblSelectedBrigade, 269, 23);

        this.lblFName = new Label("None");
        this.lblFName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.lblFName.setStyleName("clearFontMedSmall redText");
        add(this.lblFName, 269, 47);
        this.lblFName.setSize("182px", "15px");

        final Label label = new Label("Selected Brigade:");
        label.setStyleName("clearFontMedLarge");
        add(label, 269, 285);
        label.setSize("182px", "24px");

        this.lblSName = new Label("None");
        this.lblSName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.lblSName.setStyleName("clearFontMedSmall redText");
        add(this.lblSName, 269, 309);
        this.lblSName.setSize("182px", "15px");

        for (int slot = 0; slot < 6; slot++) {
            int fTop = 0, sTop = 0;
            int left = 0;
            final AbsolutePanel[] fBrigHlds = new AbsolutePanel[6];
            fBrigHlds[slot] = new AbsolutePanel();

            final AbsolutePanel[] secBrigHlds = new AbsolutePanel[6];
            secBrigHlds[slot] = new AbsolutePanel();
            fBrigHlds[slot].setSize("180px", "99px");
            secBrigHlds[slot].setSize("180px", "99px");
            fBrigHlds[slot].setStyleName("infoPanelHolder");
            secBrigHlds[slot].setStyleName("infoPanelHolder");
            if (slot < 3) {
                fTop = 62;
                sTop = 330;
            } else if (slot < 6) {
                fTop = 167;
                sTop = 435;
            }

            if (slot == 0 || slot == 3) {
                left = 45;
            } else if (slot == 1 || slot == 4) {
                left = 270;
            } else {
                left = 495;
            }

            add(fBrigHlds[slot], left, fTop);
            add(secBrigHlds[slot], left, sTop);
            fBrigPanels[slot] = new ClickAbsolutePanel();
            secBrigPanels[slot] = new ClickAbsolutePanel();
            fBrigPanels[slot].setSize("170px", "89px");
            secBrigPanels[slot].setSize("170px", "89px");
            fBrigHlds[slot].add(fBrigPanels[slot], 5, 5);
            secBrigHlds[slot].add(secBrigPanels[slot], 5, 5);
            fBrigPanels[slot].setIndex(slot + 1);
            secBrigPanels[slot].setIndex(slot + 1);
            DropController dp = new AbsolutePositionDropController(fBrigPanels[slot]);
            DropController dp2 = new AbsolutePositionDropController(secBrigPanels[slot]);
            dndCtrl.registerDropController(dp);
            dndCtrl.registerDropController(dp2);
        }
        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {

                if (event.getInfoType() == BRIGADE) {
                    ClientUtil.startSpeedTest("exchangebattalionsc");
                    if (fBrig != null && sBrig != null) {
                        setSelectedBrigade(1, fBrig);
                        setSelectedBrigade(2, sBrig);
                    }
                    ClientUtil.stopSpeedTest("exchangebattalionsc", "excBatUC");
                }

            }
        };
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    public void removeGWTHandlers() {
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void setSelectedBrigade(final int listNo, final BrigadeDTO brigade) {
        ClickAbsolutePanel[] selPanels;
        if (listNo == 1) {
            selPanels = fBrigPanels;
            fBrig = brigade;
            lblFName.setText(brigade.getName());

        } else {
            selPanels = secBrigPanels;
            sBrig = brigade;
            lblSName.setText(brigade.getName());
        }

        for (int slot = 0; slot < 6; slot++) {
            selPanels[slot].clear();
            selPanels[slot].setId(brigade.getBrigadeId());
        }

        for (final BattalionDTO batt : brigade.getBattalions()) {
            final BattInfoMini battInfo = new BattInfoMini(batt);
            dndCtrl.makeDraggable(battInfo, battInfo.getBattalionPanel());
            int index = batt.getOrder() - 1;
            selPanels[index].add(battInfo);
        }

    }
}
