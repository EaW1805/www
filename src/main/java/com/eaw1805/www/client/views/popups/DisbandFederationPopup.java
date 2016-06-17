package com.eaw1805.www.client.views.popups;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.util.ClientUtil;

import java.util.ArrayList;
import java.util.List;

public class DisbandFederationPopup
        extends PopupPanelEAW
        implements ArmyConstants, StyleConstants {

    public DisbandFederationPopup(final int type, final int typeId) {
        this.getElement().getStyle().setZIndex(3);
        this.setModal(true);
        setStyleName("none");
        final AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setStyleName("popUpPanel");
        absolutePanel.setSize("359px", "161px");
        setWidget(absolutePanel);

        final Label lblWarning = new Label("Warning:");
        lblWarning.setStyleName("clearFontMedLarge whiteText");
        lblWarning.setSize("314px", "27px");
        absolutePanel.add(lblWarning, 20, 10);

        final Label lblYouAreAbout = new Label("You are about to disband:");
        lblYouAreAbout.setStyleName("clearFontMedSmall  whiteText");
        lblYouAreAbout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblYouAreAbout.setSize("300px", "34px");
        absolutePanel.add(lblYouAreAbout, 27, 62);

        String typeName = "";
        String fedName = "";
        switch (type) {
            case ARMY:
                typeName = "army";
                fedName = ArmyStore.getInstance().getArmyNameById(typeId);
                break;

            case CORPS:
                typeName = "corps";
                fedName = ArmyStore.getInstance().getCorpByID(typeId).getName();
                break;

            case FLEET:
                typeName = "fleet";
                fedName = NavyStore.getInstance().getFleetById(typeId).getName();
                break;
            default:
                break;
        }

        lblYouAreAbout.setText("You are about to disband the not-empty '" + fedName + "' " + typeName);

        final Image acceptImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                DisbandFederationPopup.this.hide();
                switch (type) {
                    case ARMY:
                        final ArmyDTO army = ArmyStore.getInstance().getcArmies().get(typeId);
                        final List<CorpDTO> corps = new ArrayList<CorpDTO>(army.getCorps().values());
                        for (CorpDTO corp : corps) {
                            ArmyStore.getInstance().changeCorpArmy(corp.getCorpId(), army.getArmyId(), 0);
                        }

                        if (army.getCommander() != null
                                && army.getCommander().getId() > 0) {
                            CommanderStore.getInstance().commanderLeaveFederation(army.getCommander().getId());
                        }

                        ArmyStore.getInstance().deleteArmy(army.getArmyId());
                        break;

                    case CORPS:
                        ClientUtil.startSpeedTest("DisbandCorp1");
                        final CorpDTO corp = ArmyStore.getInstance().getCorpByID(typeId);
                        final List<BrigadeDTO> brigades = new ArrayList<BrigadeDTO>(corp.getBrigades().values());
                        ClientUtil.stopSpeedTest("DisbandCorp1", "Dsb1");
                        ClientUtil.startSpeedTest("DisbandCorp2");
                        for (BrigadeDTO brigade : brigades) {
                            ArmyStore.getInstance().changeBrigadeCorp(brigade.getBrigadeId(), corp.getCorpId(), 0, false);
                        }
                        UnitEventManager.changeUnit(CORPS, 0);
                        ClientUtil.stopSpeedTest("DisbandCorp2", "Dsb2");
                        ClientUtil.startSpeedTest("DisbandCorp3");
                        if (corp.getCommander() != null
                                && corp.getCommander().getId() > 0) {
                            CommanderStore.getInstance().commanderLeaveFederation(corp.getCommander().getId());
                        }
                        ClientUtil.stopSpeedTest("DisbandCorp3", "Dsb3");
                        ClientUtil.startSpeedTest("DisbandCorp4");
                        if (corp.getArmyId() > 0) {
                            ArmyStore.getInstance().changeCorpArmy(typeId, corp.getArmyId(), 0);
                        }
                        ClientUtil.stopSpeedTest("DisbandCorp4", "Dsb4");
                        ClientUtil.startSpeedTest("DisbandCorp5");
                        ArmyStore.getInstance().deleteCorp(corp.getCorpId(), 0);
                        ClientUtil.stopSpeedTest("DisbandCorp5", "Dsb5");
                        break;

                    case FLEET:
                        if (NavyStore.getInstance().getIdFleetMap().get(typeId).hasLoadedItemsOrUnits()) {
                            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot delete loaded fleet", false);
                        } else {
                            final FleetDTO fleet = NavyStore.getInstance().getIdFleetMap().get(typeId);
                            final List<ShipDTO> ships = new ArrayList<ShipDTO>(fleet.getShips().values());
                            for (ShipDTO ship : ships) {
                                NavyStore.getInstance().changeShipFleet(ship.getId(), fleet.getFleetId(), 0, true, false);
                            }
                            NavyStore.getInstance().deleteFleet(fleet.getFleetId());
                        }
                        break;

                    default:
                        // do nothing
                }
            }
        }).addToElement(acceptImg.getElement()).register();

        acceptImg.setStyleName("pointer");
        acceptImg.setTitle("Accept changes");
        absolutePanel.add(acceptImg, 105, 102);
        acceptImg.setSize(SIZE_36PX, SIZE_36PX);

        final Image rejectImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                DisbandFederationPopup.this.hide();
            }
        }).addToElement(rejectImg.getElement()).register();

        rejectImg.setStyleName("pointer");
        rejectImg.setTitle("Reject changes");
        absolutePanel.add(rejectImg, 210, 102);
        rejectImg.setSize(SIZE_36PX, SIZE_36PX);

    }
}
