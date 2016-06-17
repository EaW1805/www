package com.eaw1805.www.client.views.military.deployment;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.units.TransportStore;

public class TransportInfoPanel
        extends ClickAbsolutePanel
        implements ArmyConstants {

    private Label lblName;

    private Label lblWeight;
    private TransportUnitDTO trUnit;
    private Label lblPosition;
    private final UnitChangedHandler unitChangedHandler;


    public TransportInfoPanel(final TransportUnitDTO thisUnit) {
        setSize("108px", "64px");
        setStyleName("tradeUnit");
        setStyleName("pointer", true);
        trUnit = thisUnit;

        lblName = new Label(trUnit.getName());
        lblName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblName.setStyleName("clearFontMini");
        lblName.setSize("107px", "24px");
        add(lblName, 3, 2);

        final Image typeImg;
        switch (trUnit.getUnitType()) {

            case SHIP:
                typeImg = new Image("http://static.eaw1805.com/images/ships/" + (trUnit).getNationId() + "/" + ((ShipDTO) trUnit).getType().getIntId() + ".png");
                break;

            case FLEET:
                //update condition to be sure it is updated with ship additions and removals
                trUnit.setCondition(NavyStore.getInstance().getFleetCondition((FleetDTO) trUnit));
                typeImg = new Image("http://static.eaw1805.com/images/figures/" + (trUnit).getNationId() + "/FleetMap00.png");
                break;
            default:
            case BAGGAGETRAIN:
                typeImg = new Image("http://static.eaw1805.com/images/figures/baggage.png");
                break;
        }

        typeImg.setSize("", "45px");
        add(typeImg, 3, 17);

        lblWeight = new Label(TradeStore.getInstance().getTradeUnitLoad(trUnit) + TransportStore.getInstance()
                .getUnitsLoadedWeight(trUnit.getUnitType(), trUnit.getId(), true) +
                " / " + TradeStore.getInstance().getTradeUnitWeight(trUnit));
        lblWeight.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblWeight.setStyleName("clearFontMini");
        lblWeight.setSize("112px", "15px");
        add(lblWeight, 54, 18);

        final Label lblCondition = new Label(trUnit.getCondition() + "%");
        lblCondition.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblCondition.setStyleName("clearFontMini");
        lblCondition.setSize("112px", "15px");
        add(lblCondition, 54, 32);

        this.lblPosition = new Label(trUnit.positionToString());
        lblPosition.setStyleName("clearFontMini");
        add(this.lblPosition, 71, 2);
        this.lblPosition.setSize("39px", "15px");

        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(UnitChangedEvent event) {
                if (event.getInfoType() == trUnit.getUnitType() && event.getInfoId() == trUnit.getId()) {
                    recalculateWeightValues();
                }
            }
        };


        final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + trUnit.getNationId() + "-36.png");
        add(flag, 82, 46);
        flag.setSize("24px", "");
        //if it is yours or it is not a btrain then don't show the flag.
        if (trUnit.getNationId() == GameStore.getInstance().getNationId()
                || trUnit.getUnitType() != ArmyConstants.BAGGAGETRAIN) {
            flag.setVisible(false);
        }
    }

    public void onAttach() {
        super.onAttach();
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    private void recalculateWeightValues() {
        lblName.setText(trUnit.getName());
        lblWeight.setText(TradeStore.getInstance().getTradeUnitLoad(trUnit) + TransportStore.getInstance()
                .getUnitsLoadedWeight(trUnit.getUnitType(), trUnit.getId(), true)
                + " / "
                + TradeStore.getInstance().getTradeUnitWeight(trUnit));
    }

    /**
     * @return the trUnit
     */
    public TransportUnitDTO getTrUnit() {
        return trUnit;
    }

    public void setPhase(final int phase) {
        if (phase == 1) {
            lblPosition.setText(trUnit.startPositionToString());
        } else {
            lblPosition.setText(trUnit.positionToString());
        }
    }
}
