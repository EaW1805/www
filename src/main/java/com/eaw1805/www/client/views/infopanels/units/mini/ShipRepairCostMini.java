package com.eaw1805.www.client.views.infopanels.units.mini;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

/**
 * Tooltip panel for the ship cost info.
 */
public class ShipRepairCostMini
        extends VerticalPanel
        implements GoodConstants, ArmyConstants {

    private Label inPtCost;
    private Label fabricCost;
    private Label woodCost;
    private final Label peopleCost;
    private final Label moneyCost;
    private ShipDTO thisShip;
    private final UnitChangedHandler unitChangedHandler;
    public ShipRepairCostMini(final ShipDTO ship) {
        thisShip = ship;
        final ClickAbsolutePanel shipCostPanel = new ClickAbsolutePanel();
        shipCostPanel.setStyleName("ToolTip245x75");
        shipCostPanel.setStylePrimaryName("ToolTip245x75");
        add(shipCostPanel);
        shipCostPanel.setSize("245px", "75px");


        final Label title = new Label("Ship repair cost");
        title.setStyleName("clearFontMiniTitle");
        shipCostPanel.add(title, 3, 3);
        int posX = 5;
        final int topFactor = 20;
        final Image moneyImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_MONEY + ".png");
        moneyImg.setSize("20px", "20px");
        shipCostPanel.add(moneyImg, posX, 5 + topFactor);
        moneyCost = new Label("");
        moneyCost.setStyleName("clearFontMini");
        shipCostPanel.add(moneyCost, posX + 23, 7 + topFactor);

        final Image peopleImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_PEOPLE + ".png");
        peopleImg.setSize("20px", "20px");
        shipCostPanel.add(peopleImg, posX, 30 + topFactor);
        peopleCost = new Label("");
        peopleCost.setStyleName("clearFontMini");
        shipCostPanel.add(peopleCost, posX + 23, 32 + topFactor);

        posX += 80;

        final Image woodImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_WOOD + ".png");
        woodImg.setSize("20px", "20px");
        shipCostPanel.add(woodImg, posX, 5 + topFactor);
        woodCost = new Label("");
        woodCost.setStyleName("clearFontMini");
        shipCostPanel.add(woodCost, posX + 23, 7 + topFactor);

        final Image fabricImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_FABRIC + ".png");
        fabricImg.setSize("20px", "20px");
        shipCostPanel.add(fabricImg, posX, 30 + topFactor);
        fabricCost = new Label("");
        fabricCost.setStyleName("clearFontMini");
        shipCostPanel.add(fabricCost, posX + 23, 32 + topFactor);

        posX += 80;
        final Image inPtImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_INPT + ".png");
        inPtImg.setSize("20px", "20px");
        shipCostPanel.add(inPtImg, posX, 5 + topFactor);
        inPtCost = new Label("");
        inPtCost.setStyleName("clearFontMini");
        shipCostPanel.add(inPtCost, posX + 23, 7 + topFactor);

        setupLabels();
        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(UnitChangedEvent event) {
                if (event.getInfoType() == SHIP && event.getInfoId() == thisShip.getId()) {
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
        final OrderCostDTO shipCost = CostCalculators.getShipRepairCost(thisShip.getCondition(), thisShip);
        moneyCost.setText(numberFormat.format(shipCost.getNumericCost(GOOD_MONEY)));
        peopleCost.setText(numberFormat.format(shipCost.getNumericCost(GOOD_PEOPLE)));
        woodCost.setText(numberFormat.format(shipCost.getNumericCost(GOOD_WOOD)));
        fabricCost.setText(numberFormat.format(shipCost.getNumericCost(GOOD_FABRIC)));
        inPtCost.setText(numberFormat.format(shipCost.getNumericCost(GOOD_INPT)));
    }

}
