package com.eaw1805.www.client.views.military.buildships;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.economy.ProductionSiteStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.util.ClientUtil;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

import java.util.List;

public class BuildShipView
        extends DraggablePanel
        implements OrderConstants, NationConstants {

    private transient final List<ShipTypeDTO> listShips;

    private transient final VerticalPanel shipsPanel;

    private ShipTypeDTO selShipTypeDTO = null;

    private transient final ImageButton buildImg;

    private transient final RenamingLabel txtShipName;

    private transient final Label lblMoney;
    private transient final Label lblWood;
    private transient final Label lblFabrics;
    private transient final Label lblEcPoints;
    private transient final Label lblCitizens;
    private transient final Label lblError;
    private transient final Label lblName;
    private transient final Label lblMarines;
    private transient final Label lblClass;
    private transient final Label lblMovement;
    private transient final Label lblLoadingCapacity;
    private transient final Label lblMaintCosts;
    private transient final Label lblTurnsToBuild;
    private transient final OrderPanel cancelOrderPanel;
    private transient final ImageButton imgLeftArrow, imgRightArrow;
    private transient final Label lblPosition;

    private SectorDTO sector;

    private transient final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();

    public BuildShipView(final List<ShipTypeDTO> shipTypeList, final SectorDTO thisSector) {
        /* Set up need variables and collections */
        sector = thisSector;
        listShips = shipTypeList;
        setStyleName("buildShipsView whiteText");
        setSize("628px", "620px");

        lblError = new Label("*");
        lblError.setStyleName("serverResponseLabelError");
        lblError.setSize("34px", "25px");
        add(lblError, 624, 91);

        final Label lblShipInformation = new Label("Ship Information: ");
        lblShipInformation.setStyleName("clearFont19");
        add(lblShipInformation, 207, 52);

        final Label lblChooseShip = new Label("Choose Ship:");
        lblChooseShip.setStyleName("clearFont19");
        add(lblChooseShip, 22, 52);

        final ShipTypesList lstShipType = new ShipTypesList(this);
        lstShipType.setSize("143px", "25px");
        add(lstShipType, 24, 82);

        final ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.setSize("179px", "473px");
        add(scrollPanel, 19, 120);

        shipsPanel = new VerticalPanel();
        scrollPanel.setWidget(shipsPanel);
        shipsPanel.setSize("100%", "51px");

        final Label lblShipName = new Label("Ship Name:");
        lblShipName.setStyleName("clearFont");
        add(lblShipName, 206, 88);

        final AbsolutePanel absolutePanel2 = new AbsolutePanel();
        absolutePanel2.setSize("413px", "137px");
        add(absolutePanel2, 207, 114);

        final Label lblCharacteristics = new Label("Characteristics:");
        lblCharacteristics.setStyleName("clearFont19");
        lblCharacteristics.setSize("255px", "23px");
        absolutePanel2.add(lblCharacteristics, 0, 0);

        final Label lblTypeLabel = new Label("Type:");
        lblTypeLabel.setStyleName("clearFontMedSmall");
        absolutePanel2.add(lblTypeLabel, 25, 33);

        final Label lblClassLabel = new Label("Class:");
        lblClassLabel.setStyleName("clearFontMedSmall");
        absolutePanel2.add(lblClassLabel, 25, 59);

        final Label lblMovementLabel = new Label("Movement:");
        lblMovementLabel.setStyleName("clearFontMedSmall");
        absolutePanel2.add(lblMovementLabel, 25, 84);

        final Label lblMarinesLabel = new Label("Marines:");
        lblMarinesLabel.setStyleName("clearFontMedSmall");
        lblMarinesLabel.setSize("57px", "17px");
        absolutePanel2.add(lblMarinesLabel, 199, 33);

        final Label lblLCLabel = new Label("Load Capacity:");
        lblLCLabel.setStyleName("clearFontMedSmall");
        absolutePanel2.add(lblLCLabel, 199, 59);

        final Label lblMCLabel = new Label("Maint. Costs:");
        lblMCLabel.setStyleName("clearFontMedSmall");
        absolutePanel2.add(lblMCLabel, 199, 84);

        this.lblName = new Label("");
        this.lblName.setStyleName("clearFontVerySmall blackText");
        this.lblName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.lblName.setSize("140px", "15px");
        absolutePanel2.add(this.lblName, 50, 34);

        lblMarines = new Label("");
        lblMarines.setStyleName("clearFontMedSmall blackText");
        lblMarines.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblMarines.setSize("146px", "15px");
        absolutePanel2.add(lblMarines, 257, 34);

        this.lblClass = new Label("");
        this.lblClass.setStyleName("clearFontMedSmall blackText");
        this.lblClass.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.lblClass.setSize("121px", "15px");
        absolutePanel2.add(this.lblClass, 70, 59);

        lblMovement = new Label("");
        lblMovement.setStyleName("clearFontMedSmall blackText");
        lblMovement.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblMovement.setSize("93px", "15px");
        absolutePanel2.add(lblMovement, 98, 84);

        lblLoadingCapacity = new Label("");
        lblLoadingCapacity.setStyleName("clearFontMedSmall blackText");
        lblLoadingCapacity.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblLoadingCapacity.setSize("96px", "15px");
        absolutePanel2.add(lblLoadingCapacity, 307, 59);

        lblMaintCosts = new Label("");
        lblMaintCosts.setStyleName("clearFontMedSmall blackText");
        lblMaintCosts.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblMaintCosts.setSize("118px", "15px");
        absolutePanel2.add(lblMaintCosts, 285, 85);

        final Label lblTurnsToBuild2 = new Label("Turns to Build:");
        lblTurnsToBuild2.setStyleName("clearFontMedSmall");
        absolutePanel2.add(lblTurnsToBuild2, 25, 109);

        lblTurnsToBuild = new Label("");
        lblTurnsToBuild.setStyleName("clearFontMedSmall blackText");
        lblTurnsToBuild.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblTurnsToBuild.setSize("57px", "17px");
        absolutePanel2.add(lblTurnsToBuild, 135, 108);


        txtShipName = new RenamingLabel("", -1, -1);
        txtShipName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        txtShipName.setStyleName("clearFontMedSmall blackText");
        txtShipName.setSize("380px", "15px");
        add(txtShipName, 308, 88);


        final AbsolutePanel costsPanel = new AbsolutePanel();
        costsPanel.setSize("413px", "97px");
        add(costsPanel, 207, 250);

        final Label lblCost = new Label("Cost:");
        lblCost.setStyleName("clearFont19");
        lblCost.setSize("61px", "23px");
        costsPanel.add(lblCost, 0, 0);

        lblWood = new Label("");
        lblWood.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblWood.setStyleName("clearFontMedSmall blackText");
        lblWood.setSize("124px", "15px");
        costsPanel.add(lblWood, 67, 30);

        lblMoney = new Label("");
        lblMoney.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblMoney.setStyleName("clearFontMedSmall blackText");
        lblMoney.setSize("124px", "15px");
        costsPanel.add(lblMoney, 67, 79);

        lblFabrics = new Label("");
        this.lblFabrics.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblFabrics.setStyleName("clearFontMedSmall blackText");
        this.lblFabrics.setSize("124px", "15px");
        costsPanel.add(lblFabrics, 67, 55);

        lblEcPoints = new Label("");
        this.lblEcPoints.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblEcPoints.setStyleName("clearFontMedSmall blackText");
        this.lblEcPoints.setSize("131px", "15px");
        costsPanel.add(lblEcPoints, 272, 55);

        lblCitizens = new Label("");
        lblCitizens.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblCitizens.setStyleName("clearFontMedSmall blackText");
        lblCitizens.setSize("131px", "17px");
        costsPanel.add(lblCitizens, 272, 30);

        final Image woodImg = new Image("http://static.eaw1805.com/images/goods/good-6.png");
        woodImg.setSize("22px", "22px");
        costsPanel.add(woodImg, 37, 26);

        final Image fabricImg = new Image("http://static.eaw1805.com/images/goods/good-10.png");
        fabricImg.setSize("22px", "22px");
        costsPanel.add(fabricImg, 37, 51);

        final Image moneyImg = new Image("http://static.eaw1805.com/images/goods/good-1.png");
        moneyImg.setSize("22px", "22px");
        costsPanel.add(moneyImg, 37, 75);

        final Image citImg = new Image("http://static.eaw1805.com/images/goods/good-2.png");
        costsPanel.add(citImg, 242, 26);
        citImg.setSize("22px", "22px");

        final Image imptsImg = new Image("http://static.eaw1805.com/images/goods/good-3.png");
        imptsImg.setSize("22px", "22px");
        costsPanel.add(imptsImg, 242, 51);

        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");


        buildImg = new ImageButton("http://static.eaw1805.com/images/panels/buildShips/ButPlaceOrderOff.png");
        buildImg.setStyleName("pointer");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (selShipTypeDTO != null) {

                    final ShipDTO newShip = new ShipDTO();
                    newShip.setId(-1);
                    newShip.setTypeId(selShipTypeDTO.getIntId());
                    newShip.setType(selShipTypeDTO);
                    newShip.setRegionId(sector.getRegionId());
                    newShip.setNationId(GameStore.getInstance().getNationId());
                    newShip.setX(sector.getX());
                    newShip.setY(sector.getY());
                    if (txtShipName.getText().length() > 5) {
                        newShip.setName(txtShipName.getText());
                    } else {
                        newShip.setName("New " + selShipTypeDTO.getName());
                    }

                    final int[] ids = new int[3];
                    ids[0] = NavyStore.getInstance().getNewShipId();
                    ids[1] = sector.getId();
                    ids[2] = selShipTypeDTO.getIntId();

                    if (OrderStore.getInstance().addNewOrder(ORDER_B_SHIP, CostCalculators.getShipCost(newShip), sector.getRegionId(), newShip.getName(), ids, 0, "") == 1) {
                        newShip.setId(ids[0]);
                        NavyStore.getInstance().buildShip(sector.getId(), newShip);
                        //update the order after the orders description after the creation of the ship.
                        try {
                            GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_B_SHIP, ids);
                        } catch (Exception e) {
                            ClientUtil.printMessage("could not update order : " + e.toString());
                        }
                        if (TutorialStore.getInstance().isTutorialMode()
                                && TutorialStore.getInstance().getMonth() == 9
                                && TutorialStore.getInstance().getTutorialStep() == 4) {
                            TutorialStore.highLightButton(imgX);
                        }
                    }

                    cancelOrderPanel.repopulateOrdersList();
                    buildImg.deselect();
                    buildImg.setUrl(buildImg.getUrl().replace("Off", "Hover"));

                    int shipNo = 1;
                    if (NavyStore.getInstance().getBarrShipMap().containsKey(BuildShipView.this.sector.getId())) {
                        shipNo = NavyStore.getInstance().getBarrShipMap().get(BuildShipView.this.sector.getId()).size() + 1;
                    }
                    txtShipName.setText(selShipTypeDTO.getName() + " " + shipNo);

                }
            }
        }).addToElement(buildImg.getElement()).register();

        buildImg.setSize("273px", "30px");
        add(buildImg, 293, 368);

        cancelOrderPanel = new OrderPanel(sector);
        add(cancelOrderPanel, 225, 413);

        imgLeftArrow = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButLeftOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                final BarrackDTO barrack = BarrackStore.getInstance().getBarrackByPosition(sector);
                BarrackDTO nxtBar = BarrackStore.getInstance().getNextBarrack(barrack);
                while (!ProductionSiteStore.getInstance().isTileNeighborWithSea(nxtBar)) {
                    nxtBar = BarrackStore.getInstance().getNextBarrack(nxtBar);
                }
                sector = RegionStore.getInstance().getRegionSectorsByRegionId(nxtBar.getRegionId())[nxtBar.getX()][nxtBar.getY()];
                populateShipsPanelByType(0);
                MapStore.getInstance().getMapsView().goToPosition(nxtBar);
                lblPosition.setText(sector.positionToString() + " - Build a new ship!");
                cancelOrderPanel.repopulateOrdersList();
                imgLeftArrow.deselect();
                imgLeftArrow.setUrl(imgLeftArrow.getUrl().replace("Off", "Hover"));
            }
        }).addToElement(imgLeftArrow.getElement()).register();

        imgLeftArrow.setStyleName("pointer");
        imgLeftArrow.setSize("32px", "36px");
        add(this.imgLeftArrow, 15, 8);

        imgRightArrow = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButRightOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                final BarrackDTO barrack = BarrackStore.getInstance().getBarrackByPosition(sector);
                BarrackDTO prvBar = BarrackStore.getInstance().getPreviousBarrack(barrack);
                while (!ProductionSiteStore.getInstance().isTileNeighborWithSea(prvBar)) {
                    prvBar = BarrackStore.getInstance().getPreviousBarrack(prvBar);
                }
                sector = RegionStore.getInstance().getRegionSectorsByRegionId(prvBar.getRegionId())[prvBar.getX()][prvBar.getY()];
                populateShipsPanelByType(0);
                MapStore.getInstance().getMapsView().goToPosition(prvBar);
                lblPosition.setText(sector.positionToString() + " - Build a new ship!");
                imgRightArrow.deselect();
                imgRightArrow.setUrl(imgRightArrow.getUrl().replace("Off", "Hover"));
            }
        }).addToElement(imgRightArrow.getElement()).register();

        imgRightArrow.setStyleName("pointer");
        imgRightArrow.setSize("32px", "36px");
        add(imgRightArrow, 545, 8);

        lblPosition = new Label(sector.positionToString() + " - Build a new ship!");
        lblPosition.setStyleName("whiteText", true);
        lblPosition.setStyleName("clearFont-large");
        lblPosition.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        add(lblPosition, 50, 10);

        populateShipsPanelByType(0);
        cancelOrderPanel.repopulateOrdersList();

        final BuildShipView myself = this;
        imgX.setStyleName("pointer");
        imgX.setTitle("Close");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().getOptionsMenu().getRelImage().deselect();
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(myself);
                imgX.deselect();
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 9
                        && TutorialStore.getInstance().getTutorialStep() == 4) {
                    TutorialStore.nextStep(false);
                }
            }
        }).addToElement(imgX.getElement()).register();

        this.add(imgX, 580, 7);
        imgX.setSize("36px", "36px");
        if (TutorialStore.getInstance().isTutorialMode()
                && TutorialStore.getInstance().getMonth() == 9
                && TutorialStore.getInstance().getTutorialStep() == 4) {
            TutorialStore.highLightButton(shipsPanel);
            TutorialStore.highLightButton(lstShipType);
        }
    }

    void populateShipsPanelByType(final int type) {
        shipsPanel.clear();
        final boolean isColony = (sector.getRegionId() != RegionConstants.EUROPE);
        final boolean africa = isAfrica(sector);
        final NationDTO nation = DataStore.getInstance().getNationById(GameStore.getInstance().getNationId());
        final int sphere = CostCalculators.getSphere(sector, nation);

        lblError.setVisible(false);

        for (final ShipTypeDTO typeDTO : listShips) {
            if ((type == 0 && typeDTO.getShipClass() == 0)
                    || (type == 1 && typeDTO.getShipClass() > 0)) {

                if (!isColony || typeDTO.getCanColonies()) {

                    if ((typeDTO.getIntId() == 31 && sphere < 3 && (nation.getNationId() == NATION_GREATBRITAIN || nation.getNationId() == NATION_HOLLAND))
                            || (typeDTO.getShipClass() < 3 && typeDTO.getIntId() != 23 )
                            || ((typeDTO.getShipClass() > 2 || typeDTO.getIntId() == 23) && sphere < 3)
                            || ((typeDTO.getIntId() == 11 || typeDTO.getIntId() == 12 || typeDTO.getIntId() == 24 || typeDTO.getIntId() == 25) && africa && (nation.getNationId() == NATION_MOROCCO || nation.getNationId() == NATION_OTTOMAN || nation.getNationId() == NATION_EGYPT))
                            ) {

                        final ShipTypePanel empireShipTypeWidget = new ShipTypePanel(typeDTO, this);
                        shipsPanel.add(empireShipTypeWidget);
                    }
                }
            }
        }
    }

    protected boolean isAfrica(final SectorDTO sector) {
        return (sector.getX() >= 0 && sector.getX() <= 31 && sector.getY() > 46)
                || (sector.getX() >= 32 && sector.getX() <= 44 && sector.getY() > 52)
                || (sector.getX() >= 45 && sector.getX() <= 81 && sector.getY() > 39);
    }

    public void populateShipInformation(final ShipTypeDTO typeDTO) {
        // Double costs custom game option
        final int doubleCosts = (GameStore.getInstance().isDoubleCostsNavy()) ? 2 : 1;

        // Fast building of ships custom game option
        final boolean fastBuild = GameStore.getInstance().isFastShipConstruction();

        lblError.setVisible(false);
        selShipTypeDTO = typeDTO;
        lblCitizens.setText("" + numberFormat.format(selShipTypeDTO.getCitizens()));
        int shipNo = 1;
        if (NavyStore.getInstance().getBarrShipMap().containsKey(BuildShipView.this.sector.getId())) {
            shipNo = NavyStore.getInstance().getBarrShipMap().get(BuildShipView.this.sector.getId()).size() + 1;
        }

        lblMoney.setText(numberFormat.format(doubleCosts * selShipTypeDTO.getCost()));
        lblWood.setText(numberFormat.format(doubleCosts * selShipTypeDTO.getWood()));
        lblFabrics.setText(numberFormat.format(doubleCosts * selShipTypeDTO.getFabrics()));
        lblLoadingCapacity.setText(numberFormat.format(selShipTypeDTO.getLoadCapacity()) + " tons");
        lblMaintCosts.setText(numberFormat.format(doubleCosts * selShipTypeDTO.getMaintenance()));
        lblMovement.setText(numberFormat.format(selShipTypeDTO.getMovementFactor()));
        lblMarines.setText(numberFormat.format(selShipTypeDTO.getCitizens()));
        lblName.setText(selShipTypeDTO.getName());
        lblEcPoints.setText(numberFormat.format(doubleCosts * selShipTypeDTO.getIndPt()));

        txtShipName.setText(selShipTypeDTO.getName() + " " + shipNo);
        lblClass.setText("" + selShipTypeDTO.getShipClass());
        if (fastBuild || selShipTypeDTO.getShipClass() < 3) {
            lblTurnsToBuild.setText("1");

        } else if (selShipTypeDTO.getShipClass() < 5) {
            lblTurnsToBuild.setText("2");

        } else {
            lblTurnsToBuild.setText("3");
        }

        if (selShipTypeDTO.getShipClass() == 5
                || selShipTypeDTO.getIntId() == 11
                || selShipTypeDTO.getIntId() == 12) {
            lblName.setText(selShipTypeDTO.getName().replace("Cannon", "C."));
        }

    }

    public void unSelectPreviousSelection() {
        for (int index = 0; index < shipsPanel.getWidgetCount(); index++) {
            final ShipTypePanel empireShipType = ((ShipTypePanel) shipsPanel.getWidget(index));
            empireShipType.getContainerPanel().setStyleName("ShipPicPanel-new");
        }
    }

}
