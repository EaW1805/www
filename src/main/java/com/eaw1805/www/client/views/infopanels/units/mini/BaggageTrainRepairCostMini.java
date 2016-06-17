package com.eaw1805.www.client.views.infopanels.units.mini;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

/**
 * Tooltip panel that informs user about the repairing cost of the baggage train.
 */
public class BaggageTrainRepairCostMini
        extends VerticalPanel
        implements GoodConstants, ArmyConstants {

    private Label inPtCost;
    private Label horseCost;
    private Label woodCost;
    final Label peopleCost;
    final Label moneyCost;
    private BaggageTrainDTO thisBTrain;
    private final UnitChangedHandler unitChangedHandler;
    public BaggageTrainRepairCostMini(final BaggageTrainDTO bTrain) {
        thisBTrain = bTrain;
        final ClickAbsolutePanel bTrainCostPanel = new ClickAbsolutePanel();
        bTrainCostPanel.setStyleName("ToolTip245x75");
        bTrainCostPanel.setStylePrimaryName("ToolTip245x75");
        add(bTrainCostPanel);
        bTrainCostPanel.setSize("245px", "75px");


        final Label title = new Label("Baggage train repair cost");
        title.setStyleName("clearFontMiniTitle");
        bTrainCostPanel.add(title, 3, 3);
        int posX = 5;
        final int topFactor = 20;
        final Image moneyImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_MONEY + ".png");
        moneyImg.setSize("20px", "20px");
        bTrainCostPanel.add(moneyImg, posX, 5 + topFactor);
        moneyCost = new Label("");
        moneyCost.setStyleName("clearFontMini");
        bTrainCostPanel.add(moneyCost, posX + 23, 7 + topFactor);

        final Image peopleImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_PEOPLE + ".png");
        peopleImg.setSize("20px", "20px");
        bTrainCostPanel.add(peopleImg, posX, 30 + topFactor);
        peopleCost = new Label("");
        peopleCost.setStyleName("clearFontMini");
        bTrainCostPanel.add(peopleCost, posX + 23, 32 + topFactor);

        posX += 80;

        final Image woodImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_WOOD + ".png");
        woodImg.setSize("20px", "20px");
        bTrainCostPanel.add(woodImg, posX, 5 + topFactor);
        woodCost = new Label("");
        woodCost.setStyleName("clearFontMini");
        bTrainCostPanel.add(woodCost, posX + 23, 7 + topFactor);

        final Image fabricImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_HORSE + ".png");
        fabricImg.setSize("20px", "20px");
        bTrainCostPanel.add(fabricImg, posX, 30 + topFactor);
        horseCost = new Label("");
        horseCost.setStyleName("clearFontMini");
        bTrainCostPanel.add(horseCost, posX + 23, 32 + topFactor);

        posX += 80;

        final Image indPtImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_INPT + ".png");
        indPtImg.setSize("20px", "20px");
        bTrainCostPanel.add(indPtImg, posX, 5 + topFactor);

        inPtCost = new Label("");
        inPtCost.setStyleName("clearFontMini");
        bTrainCostPanel.add(inPtCost, posX + 23, 7 + topFactor);


        setupLabels();
        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(UnitChangedEvent event) {
                if (event.getInfoType() == BAGGAGETRAIN && event.getInfoId() == thisBTrain.getId()) {
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
        final OrderCostDTO bTrainCost = CostCalculators.getBaggageTrainRepairCost(thisBTrain.getCondition());
        moneyCost.setText(numberFormat.format(bTrainCost.getNumericCost(GOOD_MONEY)));
        peopleCost.setText(numberFormat.format(bTrainCost.getNumericCost(GOOD_PEOPLE)));
        woodCost.setText(numberFormat.format(bTrainCost.getNumericCost(GOOD_WOOD)));
        horseCost.setText(numberFormat.format(bTrainCost.getNumericCost(GOOD_HORSE)));
        inPtCost.setText(numberFormat.format(bTrainCost.getNumericCost(GOOD_INPT)));
    }


}
