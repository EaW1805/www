package com.eaw1805.www.client.views.economy.trade;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

import java.util.List;

public class BuildRepairBaggageTrainView extends DraggablePanel implements
        ArmyConstants, OrderConstants, GoodConstants {
    private final Label lblBuildrepairBaggageTrains, lblBuild, lblCosts,
            lblCitizents, lblWoodTons, lblHorses;
    private final Button btnPlaceOrder;
    private final ScrollPanel newBtrainsScroll;
    private final Label lblBuiltThisTurn;
    private final VerticalPanel newBtrainsPanel;
    private final Label lblRepairBaggagetrains, lblAvailable;
    private final ScrollPanel avBtrainScroll, repairBtrainScroll;
    private final Label lblRepairing;
    private final VerticalPanel avBtrainPanel, repairBtrainPanel;
    private final Image moneyImg, peopleImg, horsesImg, woodImg;
    private final Label moneyLbl, peopelLbl, woodLbl, horsesLabel;
    private final Label lblNameTitle;
    private final RenamingLabel lblName;
    private final Label lblRepair;
    private final Label lblIndPoints;

    public BuildRepairBaggageTrainView(final SectorDTO bsector) {


        this.setStyleName("barracksNTPanel");

        this.setSize("642px", "616px");

        this.lblBuildrepairBaggageTrains = new Label(
                "Build/Repair Baggage Trains");
        this.lblBuildrepairBaggageTrains
                .setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.lblBuildrepairBaggageTrains.setStyleName("clearFont-large whiteText");
        this.add(this.lblBuildrepairBaggageTrains, 0, 29);
        this.lblBuildrepairBaggageTrains.setSize("642px", "32px");

        final BuildRepairBaggageTrainView myself = this;

        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        imgX.setTitle("Close panel");

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(myself);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        this.add(imgX, 573, 26);
        imgX.setSize("36px", "36px");

        this.lblBuild = new Label("Build baggage train:");
        this.lblBuild.setStyleName("clearFontMiniTitle whiteText");
        this.add(this.lblBuild, 108, 76);

        this.lblCosts = new Label("Cost: 300,000 pounds");
        this.lblCosts.setStyleName("clearFont whiteText");
        this.add(this.lblCosts, 128, 147);

        this.lblCitizents = new Label("Citizents: 1000");
        this.lblCitizents.setStyleName("clearFont whiteText");
        this.add(this.lblCitizents, 128, 171);

        this.lblWoodTons = new Label("Wood: 500 tons");
        this.lblWoodTons.setStyleName("clearFont whiteText");
        this.add(this.lblWoodTons, 286, 147);

        this.lblHorses = new Label("Horses:2000");
        this.lblHorses.setStyleName("clearFont whiteText");
        this.add(this.lblHorses, 286, 169);

        this.btnPlaceOrder = new Button("Place Order");
        this.btnPlaceOrder.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                BaggageTrainStore.getInstance().buildNewBaggageTrain(bsector,
                        lblName.getText());
                lblName.setText("Baggage Train "
                        + BaggageTrainStore.getInstance().getTotalSize());
                refreshNewBtrainsPanel(bsector);
            }
        });
        this.add(this.btnPlaceOrder, 291, 193);

        this.newBtrainsScroll = new ScrollPanel();
        this.newBtrainsScroll.setStyleName("noScrollBars");
        this.newBtrainsScroll.setStyleName("prodSitePanelSel", true);
        this.add(this.newBtrainsScroll, 406, 102);
        this.newBtrainsScroll.setSize("158px", "192px");

        this.newBtrainsPanel = new VerticalPanel();
        this.newBtrainsScroll.setWidget(this.newBtrainsPanel);
        this.newBtrainsPanel.setSize("100%", "100%");

        this.lblBuiltThisTurn = new Label("Built this turn:");
        this.lblBuiltThisTurn.setStyleName("clearFontMiniTitle whiteText");
        this.add(this.lblBuiltThisTurn, 406, 76);

        this.lblRepairBaggagetrains = new Label("Repair BaggageTrains:");
        this.lblRepairBaggagetrains.setStyleName("clearFontMiniTitle whiteText");
        this.add(this.lblRepairBaggagetrains, 108, 290);

        this.lblAvailable = new Label("Available:");
        this.lblAvailable.setStyleName("clearFont whiteText");
        this.add(this.lblAvailable, 108, 316);

        this.avBtrainScroll = new ScrollPanel();
        this.avBtrainScroll.setStyleName("noScrollBars");
        this.avBtrainScroll.setStyleName("evenRow", true);
        this.add(this.avBtrainScroll, 108, 340);
        this.avBtrainScroll.setSize("158px", "192px");

        this.avBtrainPanel = new VerticalPanel();
        this.avBtrainScroll.setWidget(this.avBtrainPanel);
        this.avBtrainPanel.setSize("100%", "100%");

        this.repairBtrainScroll = new ScrollPanel();
        this.repairBtrainScroll.setStyleName("noScrollBars");
        this.repairBtrainScroll.setStyleName("oddRow", true);
        this.add(this.repairBtrainScroll, 406, 340);
        this.repairBtrainScroll.setSize("158px", "192px");

        this.repairBtrainPanel = new VerticalPanel();
        this.repairBtrainScroll.setWidget(this.repairBtrainPanel);
        this.repairBtrainPanel.setSize("100%", "100%");

        this.lblRepairing = new Label("Repairing:");
        this.lblRepairing.setStyleName("clearFont whiteText");
        this.add(this.lblRepairing, 406, 316);

        this.moneyImg = new Image("http://static.eaw1805.com/images/goods/good-1.png");
        this.moneyImg.setTitle("Money needed for repairs");
        this.add(this.moneyImg, 272, 367);
        this.moneyImg.setSize("24px", "24px");

        this.peopleImg = new Image("http://static.eaw1805.com/images/goods/good-2.png");
        this.peopleImg.setTitle("People needed for repairs");
        this.add(this.peopleImg, 272, 397);
        this.peopleImg.setSize("24px", "24px");

        this.horsesImg = new Image("http://static.eaw1805.com/images/goods/good-9.png");
        this.horsesImg.setTitle("Horses needed for repairs");
        this.add(this.horsesImg, 272, 499);
        this.horsesImg.setSize("24px", "24px");

        this.woodImg = new Image("http://static.eaw1805.com/images/goods/good-6.png");
        this.woodImg.setTitle("Wood needed for repairs");
        this.add(this.woodImg, 272, 469);
        this.woodImg.setSize("24px", "24px");

        this.moneyLbl = new Label("0");
        this.add(this.moneyLbl, 302, 376);

        this.peopelLbl = new Label("0");
        this.add(this.peopelLbl, 302, 406);

        this.woodLbl = new Label("0");
        this.add(this.woodLbl, 302, 478);

        this.horsesLabel = new Label("0");
        this.add(this.horsesLabel, 302, 508);

        this.lblNameTitle = new Label("Name:");
        this.lblNameTitle.setStyleName("clearFontMiniTitle whiteText");
        this.add(this.lblNameTitle, 118, 108);

        this.lblName = new RenamingLabel("Baggage Train "
                + BaggageTrainStore.getInstance().getTotalSize(), BAGGAGETRAIN,
                -1);
        this.lblName.setStyleName("clearFontDual whiteText");
        this.add(this.lblName, 176, 108);
        this.lblName.setSize("203px", "20px");


        this.lblRepair = new Label("Repair %:");
        this.lblRepair.setStyleName("clearFont whiteText");
        this.add(this.lblRepair, 287, 438);

        this.lblIndPoints = new Label("Ind. Points: 500");
        this.lblIndPoints.setStyleName("clearFont whiteText");
        this.add(this.lblIndPoints, 128, 193);

        refreshNewBtrainsPanel(bsector);
        refreshAvailablePanel(bsector);
        refreshRepairBtrainsPanel(bsector);

    }

    private void refreshNewBtrainsPanel(final SectorDTO sector) {
        newBtrainsPanel.clear();
        for (Integer sIds : BaggageTrainStore.getInstance().getNewBaggageTMap()
                .keySet()) {
            final Label sectorTitle = new Label("Order on sector:" + sIds);
            newBtrainsPanel.add(sectorTitle);
            for (BaggageTrainDTO btrain : BaggageTrainStore.getInstance()
                    .getNewBaggageTMap().get(sIds)) {
                final AbsolutePanel newBtrainPanel = new AbsolutePanel();
                newBtrainPanel.setSize("100%", "20px");
                final Label bTrainName = new Label("Name: " + btrain.getName());
                bTrainName.setWidth("145px");
                bTrainName.setStyleName("clearFont whiteText");
                bTrainName.setStyleName("evenRow", true);
                newBtrainPanel.add(bTrainName, 0, 0);
                if (sector.getId() == sIds) {
                    final Label cancelBuild = new Label("X");
                    cancelBuild.setWidth("13px");
                    newBtrainPanel.add(cancelBuild, 145, 0);
                    final int btrainId = btrain.getId();
                    cancelBuild.addClickHandler(new ClickHandler() {
                        public void onClick(final ClickEvent event) {
                            BaggageTrainStore.getInstance().removeBuildOrder(
                                    sector, btrainId);
                            lblName.setText("Baggage Train "
                                    + BaggageTrainStore.getInstance().getTotalSize());
                            refreshNewBtrainsPanel(sector);

                        }
                    });
                }
                newBtrainsPanel.add(newBtrainPanel);
            }

        }
    }

    private void refreshAvailablePanel(final SectorDTO bsector) {
        avBtrainPanel.clear();
        final List<BaggageTrainDTO> baggageTrains = BaggageTrainStore.getInstance()
                .getBaggageTrainsBySector(bsector, true);
        for (final BaggageTrainDTO btrain : baggageTrains) {
            if (btrain.getCondition() < 100) {
                final ClickAbsolutePanel newBtrainPanel = new ClickAbsolutePanel();
                newBtrainPanel.setSize("100%", "20px");
                final Label bTrainName = new Label("Name: " + btrain.getName());
                bTrainName.setWidth("145px");
                bTrainName.setStyleName("clearFont whiteText");
                bTrainName.setStyleName("evenRow", true);
                newBtrainPanel.add(bTrainName, 0, 0);
                final Image addBuild = new Image("http://static.eaw1805.com/images/buttons/ZoomIn.png");
                addBuild.setSize("13px", "13px");
                addBuild.setStyleName("pointer");
                newBtrainPanel.add(addBuild, 145, 0);
                final int btrainId = btrain.getId();
                addBuild.addClickHandler(new ClickHandler() {
                    public void onClick(final ClickEvent event) {
                        BaggageTrainStore.getInstance().addRepairOrder(
                                btrainId, bsector.getId());
                        refreshAvailablePanel(bsector);
                        refreshRepairBtrainsPanel(bsector);
                    }
                });
                newBtrainPanel.addMouseOverHandler(new MouseOverHandler() {
                    public void onMouseOver(final MouseOverEvent event) {
                        populateRepairCostLabels(btrain.getCondition());
                    }
                });
                newBtrainPanel.addMouseOutHandler(new MouseOutHandler() {
                    public void onMouseOut(final MouseOutEvent event) {
                        populateRepairCostLabels(100);

                    }
                });
                avBtrainPanel.add(newBtrainPanel);
            }
        }
    }

    private void refreshRepairBtrainsPanel(final SectorDTO bsector) {
        repairBtrainPanel.clear();
        final List<ClientOrderDTO> repairOrders = OrderStore.getInstance()
                .getClientOrders().get(ORDER_R_BTRAIN);
        if (repairOrders != null && !repairOrders.isEmpty()) {
            for (ClientOrderDTO order : repairOrders) {
                if (order.getIdentifier(1) == bsector.getId()) {
                    final BaggageTrainDTO btrain = BaggageTrainStore.getInstance()
                            .getBaggageTMap().get(order.getIdentifier(0));
                    final AbsolutePanel newBtrainPanel = new AbsolutePanel();
                    newBtrainPanel.setSize("100%", "20px");
                    final Label bTrainName = new Label("Name: " + btrain.getName());
                    bTrainName.setWidth("145px");
                    bTrainName.setStyleName("clearFont whiteText");
                    bTrainName.setStyleName("evenRow", true);
                    newBtrainPanel.add(bTrainName, 0, 0);
                    final Image addBuild = new Image("http://static.eaw1805.com/images/buttons/ZoomOutSlc.png");
                    addBuild.setSize("13px", "13px");
                    addBuild.setStyleName("pointer");
                    newBtrainPanel.add(addBuild, 145, 0);
                    final int btrainId = btrain.getId();
                    addBuild.addClickHandler(new ClickHandler() {
                        public void onClick(final ClickEvent event) {
                            BaggageTrainStore.getInstance().cancelRepairOrder(
                                    btrainId, bsector.getId());
                            refreshAvailablePanel(bsector);
                            refreshRepairBtrainsPanel(bsector);
                        }
                    });
                    repairBtrainPanel.add(newBtrainPanel);
                }
            }
        }

    }

    private void populateRepairCostLabels(final int condition) {
        final OrderCostDTO baggageTrainCost = CostCalculators.getBaggageTrainRepairCost(condition);
        lblRepair.setText("Condition %:" + (condition));
        woodLbl.setText(baggageTrainCost.getNumericCost(GOOD_WOOD) + " tons");
        moneyLbl.setText(baggageTrainCost.getNumericCost(GOOD_MONEY) + " pounds");
        peopelLbl.setText(baggageTrainCost.getNumericCost(GOOD_PEOPLE) + " people");
        horsesLabel.setText(baggageTrainCost.getNumericCost(GOOD_HORSE) + " horses");
    }
}
