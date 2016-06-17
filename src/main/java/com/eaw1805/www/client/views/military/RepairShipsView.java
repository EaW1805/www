package com.eaw1805.www.client.views.military;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

import java.util.List;

public class RepairShipsView extends DialogBox implements
        ArmyConstants, OrderConstants, GoodConstants, StyleConstants {


    private final VerticalPanel availPanel;
    private Image repairImg;
    private final SectorDTO sector;


    private final VerticalPanel repairPanel;

    private final Label lblMoney, lblIndpts, lblWood, lblFabrics, lblPeople;

    public RepairShipsView(final SectorDTO sector) {
        this.setSize("642px", "639px");
        this.setStylePrimaryName("");
        this.sector = sector;
        final AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setStyleName("repairShipView");
        absolutePanel.setSize("642px", "639px");
        this.add(absolutePanel);
        final Label lblRepairShips = new Label("Repair Ships");
        lblRepairShips.setStyleName("LoginLabel");
        lblRepairShips.setStyleName("clearFont-large", true);
        lblRepairShips.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        absolutePanel.add(lblRepairShips, 0, 0);
        lblRepairShips.setSize("640px", "36px");

        final Label lblX = new Label("X");
        lblX.setStyleName("XLabel");
        lblX.setStyleName("outSetBorder", true);
        lblX.setDirectionEstimator(true);
        lblX.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        absolutePanel.add(lblX, 608, 2);
        lblX.setSize("32px", "32px");
        lblX.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                ((DialogBox) lblX.getParent().getParent().getParent()).hide();
            }
        });


        final ScrollPanel avScrollPanel = new ScrollPanel();
        avScrollPanel.setStyleName("selectArmyPanel");
        avScrollPanel.setAlwaysShowScrollBars(true);
        absolutePanel.add(avScrollPanel, 32, 118);
        avScrollPanel.setSize("230px", "383px");

        this.availPanel = new VerticalPanel();
        avScrollPanel.setWidget(this.availPanel);
        this.availPanel.setSize("100%", "0");

        final Label lblDamagedShips = new Label("Damaged Ships:");
        lblDamagedShips.setStyleName("clearFontMiniTitle");
        absolutePanel.add(lblDamagedShips, 32, 84);

        final Label lblRepairedShips = new Label("Repaired Ships:");
        lblRepairedShips.setStyleName("clearFontMiniTitle");
        absolutePanel.add(lblRepairedShips, 350, 84);
        lblRepairedShips.setSize("113px", "20px");

        final ScrollPanel repScrollPanel = new ScrollPanel();
        repScrollPanel.setStyleName("armiesPanel");
        repScrollPanel.setAlwaysShowScrollBars(true);
        absolutePanel.add(repScrollPanel, 350, 118);
        repScrollPanel.setSize("230px", "383px");

        this.repairPanel = new VerticalPanel();
        repScrollPanel.setWidget(this.repairPanel);
        this.repairPanel.setSize("100%", "0");

        final Image moneyImg = new Image("http://static.eaw1805.com/images/goods/good-1.png");
        absolutePanel.add(moneyImg, 30, 554);

        final Image indPtsImg = new Image("http://static.eaw1805.com/images/goods/good-3.png");
        absolutePanel.add(indPtsImg, 150, 554);
        indPtsImg.setSize(SIZE_24PX, SIZE_24PX);

        final Image woodImg = new Image("http://static.eaw1805.com/images/goods/good-6.png");
        absolutePanel.add(woodImg, 268, 554);

        final Image fabricsImg = new Image("http://static.eaw1805.com/images/goods/good-12.png");
        absolutePanel.add(fabricsImg, 30, 605);
        fabricsImg.setSize(SIZE_24PX, SIZE_24PX);

        final Image peopleImg = new Image("http://static.eaw1805.com/images/goods/good-2.png");
        absolutePanel.add(peopleImg, 150, 605);
        peopleImg.setSize(SIZE_24PX, SIZE_24PX);

        this.lblMoney = new Label("moneyLbl");
        absolutePanel.add(this.lblMoney, 60, 563);

        this.lblIndpts = new Label("indPts Lbl");
        absolutePanel.add(this.lblIndpts, 180, 563);

        this.lblWood = new Label("indPts Lbl");
        absolutePanel.add(this.lblWood, 298, 563);
        this.lblWood.setSize("58px", "15px");

        this.lblFabrics = new Label("moneyLbl");
        absolutePanel.add(this.lblFabrics, 60, 614);
        this.lblFabrics.setSize("56px", "15px");

        this.lblPeople = new Label("people");
        absolutePanel.add(this.lblPeople, 180, 614);

        final Label lblRepairCosts = new Label("Repair Costs");
        lblRepairCosts.setStyleName("clearFontMiniTitle");
        absolutePanel.add(lblRepairCosts, 32, 519);

        refreshAvailShipsPanel();
        refreshRepairShipsPanel();
        refreshCosts(new OrderCostDTO());
    }

    private void refreshAvailShipsPanel() {
        availPanel.clear();
        final List<FleetDTO> fleets = NavyStore.getInstance().getFleetsByRegionAndTile(sector, false, true);
        for (FleetDTO fleet : fleets) {
            for (ShipDTO ship : fleet.getShips().values()) {
                if (ship.getCondition() < 100) {
                    final ClickAbsolutePanel shipPanel;
                    Image typeImg;
                    Label lblShipname;
                    Label lblCondition;
                    shipPanel = new ClickAbsolutePanel();
                    shipPanel.setId(ship.getId());
                    shipPanel.setStyleName("fleetsPanel");
                    availPanel.add(shipPanel);
                    shipPanel.setSize("214px", SIZE_45PX);
                    final ShipDTO dmShip = ship;
                    shipPanel.addMouseOverHandler(new MouseOverHandler() {
                        public void onMouseOver(final MouseOverEvent event) {
                            refreshCosts(CostCalculators.getShipRepairCost(dmShip.getCondition(), dmShip));

                        }
                    });
                    shipPanel.addMouseOutHandler(new MouseOutHandler() {
                        public void onMouseOut(final MouseOutEvent event) {
                            refreshCosts(new OrderCostDTO());
                        }
                    });

                    typeImg = new Image("http://static.eaw1805.com/images/ships/" + GameStore.getInstance().getGameId() + "/" + ship.getType().getIntId() + ".png");
                    typeImg.setTitle(ship.getType().getName());
                    shipPanel.add(typeImg, 0, 0);
                    typeImg.setSize(SIZE_45PX, SIZE_45PX);

                    lblShipname = new Label("ShipName:" + ship.getName());
                    lblShipname.setStyleName(CLASS_CLEARFONT);
                    lblShipname.setStyleName(CLASS_WHITETEXT, true);
                    shipPanel.add(lblShipname, 51, 0);
                    lblShipname.setSize("163px", SIZE_18PX);

                    lblCondition = new Label("Condition:" + ship.getCondition());
                    lblCondition.setStyleName(CLASS_CLEARFONT);
                    lblCondition.setStyleName(CLASS_WHITETEXT, true);
                    shipPanel.add(lblCondition, 51, 27);
                    lblCondition.setSize("86px", SIZE_18PX);

                    repairImg = new Image("http://static.eaw1805.com/images/buttons/ZoomIn.png");
                    shipPanel.add(repairImg, 190, 21);
                    repairImg.setSize(SIZE_24PX, SIZE_24PX);
                    repairImg.addClickHandler(new ClickHandler() {

                        public void onClick(final ClickEvent event) {
                            if (NavyStore.getInstance().repairShip(shipPanel.getId(), sector.getId())) {
                                refreshAvailShipsPanel();
                                refreshRepairShipsPanel();
                            } else {
                                new ErrorPopup(ErrorPopup.Level.WARNING, "Not enough materials available", false);
                            }
                        }
                    });
                }
            }
        }
    }

    public final void refreshRepairShipsPanel() {
        repairPanel.clear();
        final List<ClientOrderDTO> repairOrders = OrderStore.getInstance()
                .getClientOrders().get(ORDER_R_SHP);
        if (repairOrders != null && !repairOrders.isEmpty()) {
            for (ClientOrderDTO order : repairOrders) {
                if (order.getIdentifier(1) == sector.getId()) {
                    final ShipDTO ship = (ShipDTO) NavyStore.getInstance().getShipById(order.getIdentifier(0));
                    final ClickAbsolutePanel shipPanel;
                    Image typeImg;
                    Label lblShipname;
                    Label lblCondition;
                    shipPanel = new ClickAbsolutePanel();
                    shipPanel.setId(ship.getId());
                    shipPanel.setStyleName("fleetsPanel");
                    repairPanel.add(shipPanel);
                    shipPanel.setSize("214px", SIZE_45PX);

                    typeImg = new Image("http://static.eaw1805.com/images/ships/" + GameStore.getInstance().getGameId() + "/" + ship.getType().getIntId() + ".png");
                    typeImg.setTitle(ship.getType().getName());
                    shipPanel.add(typeImg, 0, 0);
                    typeImg.setSize(SIZE_45PX, SIZE_45PX);

                    lblShipname = new Label("ShipName:" + ship.getName());
                    lblShipname.setStyleName(CLASS_CLEARFONT);
                    lblShipname.setStyleName(CLASS_WHITETEXT, true);
                    shipPanel.add(lblShipname, 51, 0);
                    lblShipname.setSize("163px", SIZE_18PX);

                    lblCondition = new Label("Condition:" + ship.getCondition());
                    lblCondition.setStyleName(CLASS_CLEARFONT);
                    lblCondition.setStyleName(CLASS_WHITETEXT, true);
                    shipPanel.add(lblCondition, 51, 27);
                    lblCondition.setSize("86px", SIZE_18PX);

                    repairImg = new Image("http://static.eaw1805.com/images/buttons/ZoomOut.png");
                    repairImg.setTitle("Cancel repair order");
                    repairImg.setStyleName("pointer");
                    shipPanel.add(repairImg, 190, 21);
                    repairImg.setSize(SIZE_24PX, SIZE_24PX);
                    final int fleetId = ship.getFleet();
                    repairImg.addClickHandler(new ClickHandler() {

                        public void onClick(final ClickEvent event) {
                            if (NavyStore.getInstance().cancelRepairShip(fleetId, shipPanel.getId(), sector.getId())) {
                                refreshAvailShipsPanel();
                                refreshRepairShipsPanel();
                            } else {
                                new ErrorPopup(ErrorPopup.Level.WARNING, "Not enough materials available", false);
                            }
                        }
                    });
                }
            }
        }

    }

    private void refreshCosts(final OrderCostDTO cost) {
        lblMoney.setText(cost.getNumericCost(GOOD_MONEY) + " pounds");
        lblIndpts.setText(cost.getNumericCost(GOOD_INPT) + " units");
        lblWood.setText(cost.getNumericCost(GOOD_WOOD) + " tons");
        lblFabrics.setText(cost.getNumericCost(GOOD_FABRIC) + " tons");
        lblPeople.setText(cost.getNumericCost(GOOD_PEOPLE) + " citizents");
    }
}
