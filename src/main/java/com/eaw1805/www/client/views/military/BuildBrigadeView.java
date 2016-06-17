package com.eaw1805.www.client.views.military;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.views.extras.ArmyImage;
import com.eaw1805.www.client.views.infopanels.units.ArmyTypeInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.BrigadeInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.NewBrigadesListPanel;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

import java.util.ArrayList;
import java.util.List;

public class BuildBrigadeView
        extends DraggablePanel
        implements ArmyConstants, RegionConstants, GoodConstants, OrderConstants, StyleConstants {

    private final List<ArmyTypeDTO> armyTypeDTOs;
    private OrderCostDTO brigadeCost = new OrderCostDTO();

    private PickupDragController pickupDragController;
    private transient final AbsolutePanel newBattalionsPanel;
    private transient final RenamingLabel lblNewBrigade;

    private transient final AbsolutePanel battalionPanel[] = new AbsolutePanel[6];
    private transient final VerticalPanelScrollChild newBrigadesPanel;
    private int position;
    private transient final Label lblMoney, lblhorses, lblIndPts, lblPeople;
    private final BarrackDTO barrShip;


    private transient final ImageButton leftImg;
    private transient final ImageButton rightImg;
    private transient final HorizontalPanel infantryPanel, cavalryPanel, artilleryPanel;
    private int numOfBrigades;
    int offsetLeft = -13;
    final ImageButton btnAdd;

    public BuildBrigadeView(final BarrackDTO barrShip, final int position) {
        /* Set up need variables and collections */
        this.position = position;
        this.barrShip = barrShip;
        armyTypeDTOs = ArmyStore.getInstance().getArmyTypesList();

        /* Setup layout of the widget */
        this.setStyleName("buildBrigadePanel");
        this.setSize("1020px", "712px");

        final Label lblAwaitingTroops = new Label(barrShip.positionToString() + " - Build a new brigade!");
        lblAwaitingTroops.setStyleName("clearFontMedLarge");
        lblAwaitingTroops.setStyleName("whiteText", true);
        add(lblAwaitingTroops, 65 + offsetLeft, 16);

        leftImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButLeftOff.png");
        leftImg.setSize("35px", "35px");
        leftImg.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                int index = BarrackStore.getInstance().getBarracksList().indexOf(barrShip);
                if (index > 0) {
                    index--;

                } else {
                    index = BarrackStore.getInstance().getBarracksList().size() - 1;
                }

                final BarrackDTO nextBarrack = BarrackStore.getInstance().getBarracksList().get(index);
                final SectorDTO barrackSector = RegionStore.getInstance().getRegionSectorsByRegionId(nextBarrack.getRegionId())[nextBarrack.getX()][nextBarrack.getY()];

                MapStore.getInstance().getMapsView().goToPosition(nextBarrack);

                //SectorDTO sector = regionStore.getSelectedSector(mapStore.getActiveRegion());
                final BuildBrigadeView brigView = new BuildBrigadeView(nextBarrack, barrackSector.getId());
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(brigView);
                GameStore.getInstance().getLayoutView().setWidgetPosition(brigView, BuildBrigadeView.this.getAbsoluteLeft(), BuildBrigadeView.this.getAbsoluteTop(), false, true);
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(BuildBrigadeView.this);

                leftImg.deselect();
                leftImg.setUrl(leftImg.getUrl().replace("off", "Hover"));
            }
        }).addToElement(leftImg.getElement()).register();
        add(leftImg, 15, 8);

        rightImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButRightOff.png");
        this.rightImg.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                int index = BarrackStore.getInstance().getBarracksList().indexOf(barrShip);
                if (index < BarrackStore.getInstance().getBarracksList().size() - 1) {
                    index++;
                } else {
                    index = 0;
                }

                final BarrackDTO nextBarrack = BarrackStore.getInstance().getBarracksList().get(index);
                final SectorDTO barrackSector = RegionStore.getInstance().getRegionSectorsByRegionId(nextBarrack.getRegionId())[nextBarrack.getX()][nextBarrack.getY()];
                MapStore.getInstance().getMapsView().goToPosition(nextBarrack);

                final BuildBrigadeView brigView = new BuildBrigadeView(nextBarrack, barrackSector.getId());
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(brigView);
                GameStore.getInstance().getLayoutView().setWidgetPosition(brigView, BuildBrigadeView.this.getAbsoluteLeft(), BuildBrigadeView.this.getAbsoluteTop(), false, true);
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(BuildBrigadeView.this);

                rightImg.deselect();
                rightImg.setUrl(rightImg.getUrl().replace("off", "Hover"));
            }
        }).addToElement(rightImg.getElement()).register();

        rightImg.setSize("35px", "35px");
        add(this.rightImg, 900, 8);

        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName(CLASS_POINTER);
        imgX.setTitle("Close panel");
        final BuildBrigadeView myself = this;
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(myself);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        imgX.setSize("35px", "35px");
        add(imgX, 948, 8);

        lblNewBrigade = new RenamingLabel(BRIGADE_ + ArmyStore.getInstance().getBrigadesNum(), BRIGADE, 0);
        lblNewBrigade.setStyleName("clearFont");
        lblNewBrigade.setStyleName("whiteText", true);
        add(lblNewBrigade, 438 + offsetLeft, 350);

        newBattalionsPanel = new AbsolutePanel();
        newBattalionsPanel.setSize("220px", "34px");
        add(newBattalionsPanel, 436 + offsetLeft, 386);

        for (int slot = 0; slot < 6; slot++) {
            battalionPanel[slot] = new AbsolutePanel();
            if (slot < 4) {
                battalionPanel[slot].setStyleName("battalionPanel");
            } else {
                battalionPanel[slot].setStyleName("crackBattalionPanel");
            }
            battalionPanel[slot].setSize("30px", "30px");
            newBattalionsPanel.add(battalionPanel[slot], slot * 37, 0);
        }

        btnAdd = new ImageButton("http://static.eaw1805.com/images/panels/buildShips/ButPlaceOrderOff.png");
        btnAdd.addStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                boolean canBeBuilt = false;
                int battalions = 0, normal = 0, crack = 0;
                for (int slot = 0; slot < 6; slot++) {
                    if (battalionPanel[slot].getWidgetCount() > 0) {
                        battalions++;
                        final ArmyImage tmpImage = (ArmyImage) battalionPanel[slot].getWidget(0);
                        if (tmpImage.getArmyTypeDTO().isCrack()) {
                            crack++;
                        } else {
                            normal++;
                        }
                    }
                }

                if (battalions >= 4) {
                    canBeBuilt = true;

                } else {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "A new brigade must have at least 4 battalions", false);
                }

                if (crack > 0 && normal < 4) {
                    canBeBuilt = false;
                    new ErrorPopup(ErrorPopup.Level.WARNING, "You must build at least 4 normal for crack units to become available", false);
                }

                if (canBeBuilt) {
                    boolean defaultName = false;
                    for (int index = 0; index < numOfBrigades; index++) {
                        if (index == 0 && (BRIGADE_ + ArmyStore.getInstance().getBrigadesNum()).equals(lblNewBrigade.getText())) {
                            defaultName = true;
                        }
                        if (defaultName) {
                            lblNewBrigade.setText(BRIGADE_ + ArmyStore.getInstance().getBrigadesNum());
                        }
                        final boolean clearBats = (index == (numOfBrigades - 1));

                        final BrigadeDTO newBrigade = createBrigadeFromBattalions(clearBats, index, defaultName);
                        ArmyStore.getInstance().createBrigade(position, newBrigade);
                    }
                    lblNewBrigade.setText(BRIGADE_ + ArmyStore.getInstance().getBrigadesNum());
                    initNewBrigadesList();
                }
                btnAdd.deselect();
            }
        }).addToElement(btnAdd.getElement()).register();

        btnAdd.setSize("253px", "");
        add(btnAdd, 430 + offsetLeft, 480);

        final Label batsLbl = new Label("Battalions:");
        batsLbl.setStyleName("clearFontMedLarge whiteText");
        add(batsLbl, 437 + offsetLeft, 81);

        final ScrollPanel infantryScrollPanel = new ScrollPanel();
        infantryScrollPanel.setStyleName("noScrollBars");
        infantryScrollPanel.setSize("547px", "60px");
        add(infantryScrollPanel, 437 + offsetLeft, 117);

        infantryPanel = new HorizontalPanel();
        infantryPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        infantryScrollPanel.setWidget(infantryPanel);
        infantryPanel.setSize("0", HUNDRED_PER_CENT);

        final ScrollPanel artilleryScrollPanel = new ScrollPanel();
        artilleryScrollPanel.setStyleName("noScrollBars");
        artilleryScrollPanel.setSize("547px", "60px");
        add(artilleryScrollPanel, 437 + offsetLeft, 178);

        artilleryPanel = new HorizontalPanel();
        artilleryPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        artilleryScrollPanel.setWidget(artilleryPanel);
        artilleryPanel.setSize("0", HUNDRED_PER_CENT);

        final ScrollPanel cavalryScrollPanel = new ScrollPanel();
        cavalryScrollPanel.setStyleName("noScrollBars");
        cavalryScrollPanel.setSize("547px", "60px");
        add(cavalryScrollPanel, 437 + offsetLeft, 239);

        cavalryPanel = new HorizontalPanel();
        cavalryPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        cavalryScrollPanel.setWidget(cavalryPanel);
        cavalryPanel.setSize("0", HUNDRED_PER_CENT);

        final Label newBrigLbl = new Label("New brigades:");
        newBrigLbl.setStyleName("clearFontMedLarge whiteText");
        add(newBrigLbl, 30 + offsetLeft, 81);

        newBrigadesPanel = new VerticalPanelScrollChild();
        final ScrollVerticalBarEAW newBrigsScrollPanel = new ScrollVerticalBarEAW(this.newBrigadesPanel, 90, true);
        newBrigsScrollPanel.setSize(396, 503);
        newBrigadesPanel.setSize(HUNDRED_PER_CENT, "0");
        newBrigadesPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        add(newBrigsScrollPanel, 12, 116);

        final AbsolutePanel costPanel = new AbsolutePanel();
        costPanel.setSize("247px", "150px");
        add(costPanel, 688 + offsetLeft, 346);

        final Image moneyImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_MONEY + PNG);
        moneyImg.setSize("20px", "");
        costPanel.add(moneyImg, 10, 38);

        lblMoney = new Label("0.0");
        lblMoney.setStyleName("clearFontSmall");
        lblMoney.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblMoney.setSize("77px", "18px");
        costPanel.add(this.lblMoney, 41, 40);

        final Image horseImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_HORSE + PNG);
        horseImg.setSize("20px", "");
        costPanel.add(horseImg, 131, 78);

        lblhorses = new Label("0.0");
        lblhorses.setStyleName("clearFontSmall");
        lblhorses.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblhorses.setSize("77px", "18px");
        costPanel.add(this.lblhorses, 159, 81);

        final Image indPointsImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_INPT + PNG);
        indPointsImg.setSize("20px", "");
        costPanel.add(indPointsImg, 10, 78);

        lblIndPts = new Label("0.0");
        lblIndPts.setStyleName("clearFontSmall");
        lblIndPts.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblIndPts.setSize("77px", "18px");
        costPanel.add(this.lblIndPts, 41, 81);

        final Label label_1 = new Label("Costs:");
        label_1.setStyleName("whiteText");
        label_1.setSize("253px", "20px");
        costPanel.add(label_1, 10, 3);

        final Image peopleImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_PEOPLE + PNG);
        peopleImg.setSize("20px", "");
        costPanel.add(peopleImg, 131, 38);

        lblPeople = new Label("0.0");
        lblPeople.setStyleName("clearFontSmall");
        lblPeople.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblPeople.setSize("77px", "18px");
        costPanel.add(this.lblPeople, 159, 40);

        final int minCount = 1;
        final int maxCount = 20;
        numOfBrigades = 1;

        final Label countLbl = new Label(String.valueOf(numOfBrigades));
        countLbl.setStyleName("clearFont");
        countLbl.setSize("26px", "");
        countLbl.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        add(countLbl, 434 + offsetLeft, 442);

        final ImageButton countUp = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowUpOff.png");
        countUp.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (numOfBrigades < maxCount) {
                    numOfBrigades++;
                }
                countLbl.setText(String.valueOf(numOfBrigades));
                recalculateCosts();
            }
        }).addToElement(countUp.getElement()).register();

        countUp.setSize("24px", "24px");
        add(countUp, 468 + offsetLeft, 441);


        final ImageButton countDown = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowDownOff.png");
        countDown.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (numOfBrigades > minCount) {
                    numOfBrigades--;
                }
                countLbl.setText(String.valueOf(numOfBrigades));
                recalculateCosts();
            }
        }).addToElement(countDown.getElement()).register();

        countDown.setSize("24px", "24px");
        add(countDown, 493 + offsetLeft, 441);

        label_1.setStyleName("clearFont", true);

        /* Run initialisation functions */
        setupDragController();
        setupArmyPanels(barrShip);
        initDropPanels();
        initNewBrigadesList();
    }


    private void initNewBrigadesList() {
        newBrigadesPanel.clear();
        lblNewBrigade.setText("Brigade " + ArmyStore.getInstance().getBrigadesNum());

        final NewBrigadesListPanel blist;
        if (ArmyStore.getInstance().getBarrBrigMap().containsKey(getPosition())) {
            final List<BrigadeDTO> thisNewBrigs = ArmyStore.getInstance().getBarrBrigMap().get(getPosition());
            blist = new NewBrigadesListPanel(thisNewBrigs, true);
            newBrigadesPanel.add(blist);

        } else {
            blist = null;
        }

        for (int sectorId : ArmyStore.getInstance().getBarrBrigMap().keySet()) {
            if (sectorId != getPosition()) {
                final List<BrigadeDTO> thisNewBrigs2 = ArmyStore.getInstance().getBarrBrigMap().get(sectorId);
                newBrigadesPanel.add(new NewBrigadesListPanel(thisNewBrigs2, false));
            }
        }

        final ImageButton btnCancelSelectedBattalion = new ImageButton("http://static.eaw1805.com/images/panels/buildShips/ButDeleteOrderOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (blist == null) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "No armies build in this barrack", false);
                } else {
                    final BrigadeInfoPanel selBrig = blist.getSelectedBrigade();
                    if (selBrig == null) {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "Select the brigade you want to remove", false);
                    } else {
                        ArmyStore.getInstance().cancelBrigade(getPosition(), selBrig.getBrigade());
                        initNewBrigadesList();
                    }
                }
            }
        }).addToElement(btnCancelSelectedBattalion.getElement()).register();

        btnCancelSelectedBattalion.setSize("274px", "26px");
        add(btnCancelSelectedBattalion, 65 + offsetLeft, 636);
    }

    private void initDropPanels() {
        for (int slot = 1; slot < 6; slot++) {
            final DropController dc1 = new AbsolutePositionDropController(battalionPanel[slot]);
            pickupDragController.registerDropController(dc1);
        }
    }

    private void setupArmyPanels(final BarrackDTO barrShip) {
        final int nationId = barrShip.getNationId();
        for (ArmyTypeDTO armyType : armyTypeDTOs) {
            if (!armyType.isElite() &&
                    ((armyType.getRegionId() == 0 && barrShip.getRegionId() != EUROPE)
                            || (armyType.getRegionId() == barrShip.getRegionId()))) {
                final SectorDTO sector = RegionStore.getInstance().getSectorByPosition(barrShip);
                if (armyType.isCrack()
                        && CostCalculators.getSphere(sector, DataStore.getInstance().getNationById(GameStore.getInstance().getNationId())) == 3) {
                    continue;
                }
                final AbsolutePanel armyTypePanel = new AbsolutePanel();

                if (armyType.isArtillery() || armyType.isMArtillery()) {
                    artilleryPanel.add(armyTypePanel);

                } else if (armyType.isCavalry()) {
                    cavalryPanel.add(armyTypePanel);

                } else {
                    infantryPanel.add(armyTypePanel);
                }

                armyTypePanel.setSize("60px", "59px");

                final ArmyImage armyTypeImg = new ArmyImage("http://static.eaw1805.com/images/armies/" + nationId + "/" + armyType.getIntId() + ".jpg");
                armyTypeImg.setArmyTypeDTO(armyType);
                armyTypePanel.add(armyTypeImg, 0, 0);
                armyTypeImg.setSize("59px", "59px");

                addOverViewPanelToImage(armyTypeImg, armyType, true);

                //Make the image drag-able
                pickupDragController.makeDraggable(armyTypeImg);
            }
        }
    }


    private void addOverViewPanelToImage(final ArmyImage armyTypeImg, final ArmyTypeDTO armyType, final boolean doubleClickAdd) {
        armyTypeImg.setStyleName(CLASS_POINTER, true);

        new ToolTipPanel(armyTypeImg) {
            @Override
            public void generateTip() {
                setTooltip(new ArmyTypeInfoPanel(armyType, null));
            }
        };

        armyTypeImg.addDoubleClickHandler(new DoubleClickHandler() {
            public void onDoubleClick(final DoubleClickEvent event) {
                if (doubleClickAdd) {
                    //validate battalions
                    boolean hasInfantry = false;
                    boolean hasCavalry = false;
                    for (int slot = 0; slot < 6; slot++) {
                        if (battalionPanel[slot].getWidgetCount() > 0) {
                            final ArmyImage tmpImage = (ArmyImage) battalionPanel[slot].getWidget(0);
                            if (tmpImage.getArmyTypeDTO().isInfantry()) {
                                hasInfantry = true;
                            }
                            if (tmpImage.getArmyTypeDTO().isCavalry() || tmpImage.getArmyTypeDTO().isMArtillery()) {
                                hasCavalry = true;
                            }
                        }
                    }
                    if (armyTypeImg.getArmyTypeDTO().isInfantry()) {
                        hasInfantry = true;
                    }
                    if (armyTypeImg.getArmyTypeDTO().isCavalry() || armyTypeImg.getArmyTypeDTO().isMArtillery()) {
                        hasCavalry = true;
                    }
                    if (hasInfantry && hasCavalry) {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot mix infantry and cavalry battalions together in the same brigade", false);
                        return;
                    }

                    final ArmyImage armyTypeImg2 = new ArmyImage();
                    armyTypeImg2.setArmyTypeDTO(armyTypeImg.getArmyTypeDTO());
                    armyTypeImg2.setSize(armyTypeImg.getWidth() + "px", armyTypeImg.getHeight() + "px");
                    armyTypeImg2.setUrl(armyTypeImg.getUrl());
                    addOverViewPanelToImage(armyTypeImg2, armyTypeImg2.getArmyTypeDTO(), false);
                    int start = 0;
                    if (armyTypeImg2.getArmyTypeDTO().isCrack()) {
                        start = 4;
                    }
                    for (int slot = start; slot < 6; slot++) {
                        if (battalionPanel[slot].getWidgetCount() < 1) {
                            setNewBattalionImage(armyTypeImg2, battalionPanel[slot]);
                            break;
                        }
                        if (slot == 5) {
                            new ErrorPopup(ErrorPopup.Level.WARNING, "Proper slots for this brigade are full. Double click on one to remove and then add.", false);
                        }
                    }

                } else {
                    armyTypeImg.removeFromParent();
                }
                recalculateCosts();
            }
        });

    }

    private void setupDragController() {
        pickupDragController = new PickupDragController(this, false);
        pickupDragController.setBehaviorDragStartSensitivity(3);
        pickupDragController.setBehaviorBoundaryPanelDrop(false);
        final BuildBrigadeView myself = this;
        pickupDragController.addDragHandler(new DragHandler() {
            private AbsolutePanel fdSource;

            public void onPreviewDragStart(final DragStartEvent event)
                    throws VetoDragException {
                fdSource = (AbsolutePanel) event.getContext().draggable.getParent();
                myself.setMovePanel(false);//force to wait until the next key down.
            }

            public void onPreviewDragEnd(final DragEndEvent event)
                    throws VetoDragException {
                // do nothing
            }

            public void onDragStart(final DragStartEvent event) {
                myself.setMovePanel(false);//force to wait until the next key down.
                final ArmyImage tmpImage = new ArmyImage();

                tmpImage.setUrl(((ArmyImage) event.getContext().draggable).getUrl());
                tmpImage.setWidth(((ArmyImage) event.getContext().draggable).getWidth() + "px");
                tmpImage.setHeight(((ArmyImage) event.getContext().draggable).getHeight() + "px");
                tmpImage.setArmyTypeDTO(((ArmyImage) event.getContext().draggable).getArmyTypeDTO());
                addOverViewPanelToImage(tmpImage, tmpImage.getArmyTypeDTO(), true);
                pickupDragController.makeDraggable(tmpImage);
                pickupDragController.unregisterDropControllers();
                for (int slot = 0; slot < newBattalionsPanel.getWidgetCount(); slot++) {
                    if (newBattalionsPanel.getWidget(slot).getClass() == AbsolutePanel.class
                            && (!tmpImage.getArmyTypeDTO().isCrack() || slot > 3)) {
                        final DropController dropController = new AbsolutePositionDropController(
                                (AbsolutePanel) newBattalionsPanel.getWidget(slot));
                        pickupDragController.registerDropController(dropController);
                    }
                }
                fdSource.add(tmpImage);
            }

            public void onDragEnd(final DragEndEvent event) {

                final DragContext dg = event.getContext();
                if (dg.finalDropController != null) {
                    final AbsolutePanel fdTarget = (AbsolutePanel) dg.finalDropController.getDropTarget();
                    if (fdTarget.getWidgetCount() > 0) {
                        final ArmyImage fdSImage = (ArmyImage) dg.draggable;

                        //validate battalions
                        boolean hasInfantry = false;
                        boolean hasCavalry = false;
                        for (int slot = 0; slot < 6; slot++) {
                            if (battalionPanel[slot].getWidgetCount() > 0) {
                                final ArmyImage tmpImage = (ArmyImage) battalionPanel[slot].getWidget(0);
                                if (tmpImage.getArmyTypeDTO().isInfantry()) {
                                    hasInfantry = true;
                                }
                                if (tmpImage.getArmyTypeDTO().isCavalry() || tmpImage.getArmyTypeDTO().isMArtillery()) {
                                    hasCavalry = true;
                                }
                            }
                        }
                        if (fdSImage.getArmyTypeDTO().isInfantry()) {
                            hasInfantry = true;
                        }
                        if (fdSImage.getArmyTypeDTO().isCavalry() || fdSImage.getArmyTypeDTO().isMArtillery()) {
                            hasCavalry = true;
                        }
                        if (hasInfantry && hasCavalry) {
                            fdSImage.removeFromParent();
                            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot mix infantry and cavalry battalions together in the same brigade", false);
                            return;
                        }


                        final ArmyImage tmpImage = new ArmyImage();
                        tmpImage.setUrl(fdSImage.getUrl());
                        tmpImage.setWidth(fdSImage.getWidth() + "px");
                        tmpImage.setHeight(fdSImage.getHeight() + "px");
                        tmpImage.setArmyTypeDTO(fdSImage.getArmyTypeDTO());
                        addOverViewPanelToImage(tmpImage, tmpImage.getArmyTypeDTO(), false);
                        pickupDragController.makeNotDraggable(fdSImage);
                        setNewBattalionImage(tmpImage, fdTarget);
                        recalculateCosts();
                    }
                    if (fdSource.getWidgetCount() > 3) {
                        fdSource.remove(2);
                    }
                }
            }
        });

    }

    private void setNewBattalionImage(final ArmyImage armyImage, final AbsolutePanel ab) {
        ab.clear();
        final int nationId = GameStore.getInstance().getNationId();
        armyImage.setUrl("http://static.eaw1805.com/images/armies/" + nationId + "/"
                + armyImage.getArmyTypeDTO().getIntId() + ".jpg");
        armyImage.setSize("30px", "34px");
        armyImage.removeStyleName(CLASS_POINTER);
        ab.add(armyImage, 0, 0);
    }

    private HandlerRegistration tempReg;

    private void recalculateCosts() {
        // Double costs custom game option
        final int doubleCosts = (GameStore.getInstance().isDoubleCostsArmy()) ? 2 : 1;

        final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
        final int multiplier = numOfBrigades;
        final BrigadeDTO newBrigade = createBrigadeFromBattalions(false, 0, true);

        final SectorDTO sector = RegionStore.getInstance().getSectorByPosition(barrShip);
        final int sphere = CostCalculators.getSphere(sector, DataStore.getInstance().getNationById(GameStore.getInstance().getNationId()));
        final int multiplier2 = doubleCosts * sphere;

        setBrigadeCost(CostCalculators.getBrigadeCost(newBrigade, multiplier2, sphere));
        lblhorses.setText(numberFormat.format(getBrigadeCost().getNumericCost(GOOD_HORSE) * multiplier));
        lblMoney.setText(numberFormat.format(getBrigadeCost().getNumericCost(GOOD_MONEY) * multiplier));
        lblIndPts.setText(numberFormat.format(getBrigadeCost().getNumericCost(GOOD_INPT) * multiplier));
        lblPeople.setText(numberFormat.format(getBrigadeCost().getNumericCost(GOOD_PEOPLE) * multiplier));

        if (TutorialStore.getInstance().isTutorialMode()
                && TutorialStore.getInstance().getMonth() == 5
                && TutorialStore.getInstance().getTutorialStep() == 3) {

            int battalions = 0, normal = 0, crack = 0;
            for (int slot = 0; slot < 6; slot++) {
                if (battalionPanel[slot].getWidgetCount() > 0) {
                    battalions++;
                }
            }
            if (battalions >= 4) {
                tempReg = btnAdd.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        tempReg.removeHandler();
                        TutorialStore.nextStep(false);
                    }
                });
                TutorialStore.highLightButton(btnAdd);
            } else if (tempReg != null) {
                try {
                    tempReg.removeHandler();
                } catch (Exception e) {/*eat it*/}
            }
        }
    }

    public BrigadeDTO createBrigadeFromBattalions(final boolean clear, final int count, final boolean defaultName) {
        final BrigadeDTO newBrigade = new BrigadeDTO();
        newBrigade.setRegionId(barrShip.getRegionId());
        newBrigade.setNationId(barrShip.getNationId());
        newBrigade.setX(barrShip.getX());
        newBrigade.setY(barrShip.getY());
        if (count > 0 && !defaultName) {
            newBrigade.setName(lblNewBrigade.getText() + " " + (count + 1));
            newBrigade.setOriginalName(lblNewBrigade.getText() + " " + (count + 1));
        } else {
            newBrigade.setName(lblNewBrigade.getText());
            newBrigade.setOriginalName(lblNewBrigade.getText());
        }
        newBrigade.setBrigadeId(-1);
        newBrigade.setBattalions(new ArrayList<BattalionDTO>());

        for (int slot = 0; slot < 6; slot++) {
            if (battalionPanel[slot].getWidgetCount() > 0) {
                final ArmyImage tmpImage = (ArmyImage) battalionPanel[slot].getWidget(0);
                final BattalionDTO newBatt = new BattalionDTO();
                newBatt.setEmpireArmyType(tmpImage.getArmyTypeDTO());
                newBatt.setExperience(1);

                int headcount = 800;
                if (newBatt.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                        || newBatt.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                        || newBatt.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                    headcount = 1000;
                }

                newBatt.setHeadcount(headcount);
                newBatt.setOrder(slot + 1);
                newBatt.setTypeId(tmpImage.getArmyTypeDTO().getIntId());
                newBrigade.getBattalions().add(newBatt);
                if (clear) {
                    battalionPanel[slot].clear();
                }
            }
        }
        return newBrigade;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(final int position) {
        this.position = position;
    }

    /**
     * @return the brigadeCost
     */
    public OrderCostDTO getBrigadeCost() {
        return brigadeCost;
    }

    /**
     * @param brigadeCost the brigadeCost to set
     */
    public void setBrigadeCost(final OrderCostDTO brigadeCost) {
        this.brigadeCost = brigadeCost;
    }
}
