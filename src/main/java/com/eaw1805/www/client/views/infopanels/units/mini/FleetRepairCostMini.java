package com.eaw1805.www.client.views.infopanels.units.mini;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

public class FleetRepairCostMini
        extends VerticalPanel
        implements GoodConstants, ArmyConstants {

    private Label inPtCost;
    private Label fabricCost;
    private Label woodCost;
    final Label peopleCost;
    final Label moneyCost;
    private FleetDTO thisFleet;
    private final UnitChangedHandler unitChangedHandler;
    public FleetRepairCostMini(final FleetDTO fleet) {
        thisFleet = fleet;
        final ClickAbsolutePanel fleetCostPanel = new ClickAbsolutePanel();
        fleetCostPanel.setStyleName("ToolTip245x75");
        fleetCostPanel.setStylePrimaryName("ToolTip245x75");
        add(fleetCostPanel);
        fleetCostPanel.setSize("245px", "75px");


        final Label title = new Label("Fleet repair cost");
        title.setStyleName("clearFontMiniTitle");
        fleetCostPanel.add(title, 3, 3);
        int posX = 5;
        final int topFactor = 20;
        final Image moneyImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_MONEY + ".png");
        moneyImg.setSize("20px", "20px");
        fleetCostPanel.add(moneyImg, posX, 5 + topFactor);
        moneyCost = new Label("");
        moneyCost.setStyleName("clearFontMini");
        fleetCostPanel.add(moneyCost, posX + 23, 7 + topFactor);

        final Image peopleImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_PEOPLE + ".png");
        peopleImg.setSize("20px", "20px");
        fleetCostPanel.add(peopleImg, posX, 30 + topFactor);
        peopleCost = new Label("");
        peopleCost.setStyleName("clearFontMini");
        fleetCostPanel.add(peopleCost, posX + 23, 32 + topFactor);

        posX += 80;

        final Image woodImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_WOOD + ".png");
        woodImg.setSize("20px", "20px");
        fleetCostPanel.add(woodImg, posX, 5 + topFactor);
        woodCost = new Label("");
        woodCost.setStyleName("clearFontMini");
        fleetCostPanel.add(woodCost, posX + 23, 7 + topFactor);

        final Image fabricImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_FABRIC + ".png");
        fabricImg.setSize("20px", "20px");
        fleetCostPanel.add(fabricImg, posX, 30 + topFactor);
        fabricCost = new Label("");
        fabricCost.setStyleName("clearFontMini");
        fleetCostPanel.add(fabricCost, posX + 23, 32 + topFactor);

        posX += 80;
        final Image inPtImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_INPT + ".png");
        inPtImg.setSize("20px", "20px");
        fleetCostPanel.add(inPtImg, posX, 5 + topFactor);
        inPtCost = new Label("");
        inPtCost.setStyleName("clearFontMini");
        fleetCostPanel.add(inPtCost, posX + 23, 7 + topFactor);

        setupLabels();
        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(UnitChangedEvent event) {
                if (event.getInfoType() == FLEET && event.getInfoId() == thisFleet.getFleetId()) {
                    setupLabels();
                }
            }
        };
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void onAttach() {
        super.onAttach();
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    public void setupLabels() {
        final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
        final OrderCostDTO shipCost = CostCalculators.getFleetRepairCost(thisFleet);
        moneyCost.setText(numberFormat.format(shipCost.getNumericCost(GOOD_MONEY)));
        peopleCost.setText(numberFormat.format(shipCost.getNumericCost(GOOD_PEOPLE)));
        woodCost.setText(numberFormat.format(shipCost.getNumericCost(GOOD_WOOD)));
        fabricCost.setText(numberFormat.format(shipCost.getNumericCost(GOOD_FABRIC)));
        inPtCost.setText(numberFormat.format(shipCost.getNumericCost(GOOD_INPT)));
    }
}
