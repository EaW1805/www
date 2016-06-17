package com.eaw1805.www.client.views.military.addbattalion;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.extras.ArmyImage;
import com.eaw1805.www.client.views.infopanels.units.ArmyTypeInfoPanel;
import com.eaw1805.www.client.widgets.AddBattalionDragHandler;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

import java.util.List;

public class AddToSelectedPanel
        extends ClickAbsolutePanel
        implements RegionConstants, OrderConstants {

    private HorizontalPanel artilleryPanel;
    private transient final HorizontalPanel infantryPanel;
    private transient final HorizontalPanel cavalryPanel;
    private transient final Label lblName;
    private transient final PickupDragController pickupDragController;
    private transient final List<ArmyTypeDTO> armyTypeDTOs;
    private transient final ClickAbsolutePanel battalionPanel[] = new ClickAbsolutePanel[6];
    private transient final VerticalPanel battToAddPanel;
    private transient final ImageButton acceptImg;
    private transient final Label lblMoney;
    private transient final Label lblIndPts;
    private transient final Label lblPeople;
    private transient final Label lblHorses;
    private boolean brigSelected = false;
    private BrigadeDTO selBrigade = null;
    private transient final AddBattalionView addBattalionView;

    public AddToSelectedPanel(final SectorDTO barrSectorDto, final AddBattalionView addBattalionView) {
        setSize("570px", "708px");
        this.addBattalionView = addBattalionView;
        this.pickupDragController = new PickupDragController(this, false);
        this.pickupDragController.addDragHandler(new AddBattalionDragHandler(this));
        this.pickupDragController.setBehaviorDragStartSensitivity(3);
        armyTypeDTOs = ArmyStore.getInstance().getArmyTypesList();

        final Label lblBattalions = new Label("Battalions:");
        lblBattalions.setStyleName("clearFontMedLarge whiteText");
        lblBattalions.setSize("109px", "24px");
        add(lblBattalions, 0, 0);

        this.infantryPanel = new HorizontalPanel();
        this.infantryPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.infantryPanel.setSize("91px", "60px");
        add(this.infantryPanel, 8, 40);

        this.artilleryPanel = new HorizontalPanel();
        this.artilleryPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.artilleryPanel.setSize("91px", "60px");
        add(this.artilleryPanel, 8, 100);

        this.cavalryPanel = new HorizontalPanel();
        this.cavalryPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.cavalryPanel.setSize("91px", "60px");
        add(this.cavalryPanel, 8, 160);

        final Label lblSelectedBrigade = new Label("Selected Brigade:");
        lblSelectedBrigade.setStyleName("clearFont whiteText");
        add(lblSelectedBrigade, 10, 262);

        this.lblName = new Label("None selected");
        this.lblName.setStyleName("clearFontSmall whiteText");
        this.lblName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        add(this.lblName, 10, 287);
        this.lblName.setSize("215px", "15px");

        final ClickAbsolutePanel newBattalionsPanel = new ClickAbsolutePanel();
        add(newBattalionsPanel, 9, 306);
        newBattalionsPanel.setSize("251px", "34px");

        this.battToAddPanel = new VerticalPanel();
        this.battToAddPanel.setSize("251px", "30px");
        add(this.battToAddPanel, 8, 416);

        final Label lblAdditions = new Label("Additions:");
        lblAdditions.setStyleName("clearFont whiteText");
        add(lblAdditions, 10, 382);

        final Image moneyImg = new Image("http://static.eaw1805.com/images/goods/good-1.png");
        moneyImg.setTitle("Money");
        moneyImg.setSize("24px", "24px");
        add(moneyImg, 262, 302);

        final Image indPtImg = new Image("http://static.eaw1805.com/images/goods/good-3.png");
        indPtImg.setTitle("Industrial points");
        indPtImg.setSize("24px", "24px");
        add(indPtImg, 262, 342);

        final Image peopleImg = new Image("http://static.eaw1805.com/images/goods/good-2.png");
        peopleImg.setTitle("People");
        peopleImg.setSize("24px", "24px");
        add(peopleImg, 384, 302);

        final Image horsesImg = new Image("http://static.eaw1805.com/images/goods/good-9.png");
        horsesImg.setTitle("Horses");
        horsesImg.setSize("24px", "24px");
        add(horsesImg, 384, 342);

        this.acceptImg = new ImageButton("http://static.eaw1805.com/images/panels/buildShips/ButPlaceOrderOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (selBrigade != null) {
                    for (int index = 0; index < battToAddPanel.getWidgetCount(); index++) {
                        final AdditionRow addRow = (AdditionRow) battToAddPanel.getWidget(index);
                        ArmyStore.getInstance().addBattalionToBrigade(selBrigade.getBrigadeId(), addRow.getArmyType(), addRow.getSlot(), selBrigade.getRegionId());
                    }
                    restartWindow();
                }
                acceptImg.deselect();
            }
        }).addToElement(acceptImg.getElement()).register();

        this.acceptImg.setStyleName("pointer");
        this.acceptImg.setSize("253px", "");
        add(this.acceptImg, 262, 374);

        final Label lblCost = new Label("Cost:");
        lblCost.setStyleName("clearFont whiteText");
        add(lblCost, 262, 262);

        this.lblMoney = new Label("0");
        this.lblMoney.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.lblMoney.setSize("79px", "15px");
        add(this.lblMoney, 296, 306);

        this.lblIndPts = new Label("0");
        this.lblIndPts.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.lblIndPts.setSize("79px", "15px");
        add(this.lblIndPts, 296, 349);

        this.lblPeople = new Label("0");
        this.lblPeople.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.lblPeople.setSize("79px", "15px");
        add(this.lblPeople, 414, 306);

        this.lblHorses = new Label("0");
        this.lblHorses.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.lblHorses.setSize("79px", "15px");
        add(this.lblHorses, 414, 349);

        setupArmyPanels(barrSectorDto);

        for (int index = 0; index < 6; index++) {
            battalionPanel[index] = new ClickAbsolutePanel();
            battalionPanel[index].setId(index + 1);
            if (index < 4) {
                battalionPanel[index].setStyleName("battalionPanel");
            } else {
                battalionPanel[index].setStyleName("crackBattalionPanel");
            }
            battalionPanel[index].setSize("30px", "30px");
            newBattalionsPanel.add(battalionPanel[index], index * 36, 0);
        }
    }

    private void setupArmyPanels(final SectorDTO barrShip) {
        final int nationId = barrShip.getNationId();
        for (ArmyTypeDTO armyType : armyTypeDTOs) {
            if (!armyType.isElite()
                    && ((armyType.getRegionId() == 0 && barrShip.getRegionId() != EUROPE)
                    || (armyType.getRegionId() == barrShip.getRegionId()))) {
                final ClickAbsolutePanel armyTypePanel = new ClickAbsolutePanel();

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
        armyTypeImg.setStyleName("pointer", true);

        new ToolTipPanel(armyTypeImg) {
            @Override
            public void generateTip() {
                setTooltip(new ArmyTypeInfoPanel(armyType, null));
            }
        };

        armyTypeImg.addDoubleClickHandler(new DoubleClickHandler() {
            public void onDoubleClick(final DoubleClickEvent event) {
                if (doubleClickAdd && brigSelected) {

                    if (!canAddBattalion(armyTypeImg.getArmyTypeDTO())) {
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
                            addNewBattRow(armyTypeImg2.getArmyTypeDTO(), slot + 1);

                            break;
                        }
                        if (slot == 5) {
                            new ErrorPopup(ErrorPopup.Level.WARNING, "Proper slots for this brigade are full", false);
                        }
                    }

                }
                recalculateCosts();
            }

        });
    }

    public boolean canAddBattalion(ArmyTypeDTO armyType) {
        //validate battalions
        boolean hasInfantry = false;
        boolean hasCavalry = false;
        for (int slot = 0; slot < 6; slot++) {
            if (battalionPanel[slot].getWidgetCount() > 0) {
                if (!(battalionPanel[slot].getWidget(0) instanceof ArmyImage)) {
                    //something weird is happening here...
                    continue;
                }
                final ArmyImage tmpImage = (ArmyImage) battalionPanel[slot].getWidget(0);
                if (tmpImage.getArmyTypeDTO().isInfantry()) {
                    hasInfantry = true;
                }
                if (tmpImage.getArmyTypeDTO().isCavalry() || tmpImage.getArmyTypeDTO().isMArtillery()) {
                    hasCavalry = true;
                }
            }
        }
        if (armyType.isInfantry()) {
            hasInfantry = true;
        }
        if (armyType.isCavalry() || armyType.isMArtillery()) {
            hasCavalry = true;
        }
        return !(hasInfantry && hasCavalry);
    }


    public void addNewBattRow(final ArmyTypeDTO armyTypeDTO, final int order) {
        battToAddPanel.add(new AdditionRow(armyTypeDTO, order, this));
        recalculateCosts();
    }

    public void setNewBattalionImage(final ArmyImage armyImage, final ClickAbsolutePanel ab) {
        ab.clear();
        final int nationId = GameStore.getInstance().getNationId();
        armyImage.setUrl("http://static.eaw1805.com/images/armies/" + nationId + "/"
                + armyImage.getArmyTypeDTO().getIntId() + ".jpg");
        armyImage.setSize("30px", "34px");
        armyImage.removeStyleName("pointer");
        ab.add(armyImage, 0, 0);
        pickupDragController.unregisterDropControllers();
        for (int slot = 0; slot < 6; slot++) {
            if (battalionPanel[slot].getWidgetCount() == 0) {
                final DropController sp = new AbsolutePositionDropController(battalionPanel[slot]);
                pickupDragController.registerDropController(sp);
            }
        }
    }

    public void recalculateCosts() {
        // Double costs custom game option
        final int doubleCosts = (GameStore.getInstance().isDoubleCostsArmy()) ? 2 : 1;

        final SectorDTO sector = RegionStore.getInstance().getSectorByPosition(selBrigade);
        final int multiplier2 = doubleCosts * CostCalculators.getSphere(sector, DataStore.getInstance().getNationById(GameStore.getInstance().getNationId()));

        int money = 0, indPts = 0, horses = 0, people = 0;
        for (int slot = 0; slot < battToAddPanel.getWidgetCount(); slot++) {
            final AdditionRow addRow = (AdditionRow) battToAddPanel.getWidget(slot);
            int headcount = 800;
            if (addRow.getArmyType().getNationId() == NationConstants.NATION_MOROCCO
                    || addRow.getArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                    || addRow.getArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                headcount = 1000;
            }
            money += multiplier2 * addRow.getArmyType().getCost();
            indPts += multiplier2 * addRow.getArmyType().getIndPt();
            if (addRow.getArmyType().isCavalry() || addRow.getArmyType().isMArtillery()) {
                horses += headcount;
            }
            people += headcount;
        }
        final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
        lblMoney.setText(numberFormat.format(money));
        lblIndPts.setText(numberFormat.format(indPts));
        lblHorses.setText(numberFormat.format(horses));
        lblPeople.setText(numberFormat.format(people));
    }

    public void selectBrigade(final BrigadeDTO brig) {
        this.selBrigade = brig;
        lblName.setText(brig.getName());
        battToAddPanel.clear();
        for (int slot = 0; slot < 6; slot++) {
            battalionPanel[slot].clear();
        }
        pickupDragController.unregisterDropControllers();
        for (final BattalionDTO batt : brig.getBattalions()) {
            final int index = batt.getOrder();
            final ArmyImage armyTypeImg = new ArmyImage();
            armyTypeImg.setArmyTypeDTO(batt.getEmpireArmyType());
            armyTypeImg.setSize("30px", "30px");
            addOverViewPanelToImage(armyTypeImg, armyTypeImg.getArmyTypeDTO(), false);
            setNewBattalionImage(armyTypeImg, battalionPanel[index - 1]);
        }
        for (int slot = 0; slot < 6; slot++) {
            if (battalionPanel[slot].getWidgetCount() == 0) {
                final DropController sp = new AbsolutePositionDropController(battalionPanel[slot]);
                pickupDragController.registerDropController(sp);
            }
        }
        brigSelected = true;
    }

    public void removeBattFromBrig(final int slot) {
        battalionPanel[slot - 1].clear();
        recalculateCosts();
        for (int index = 0; index < 6; index++) {
            if (battalionPanel[index].getWidgetCount() == 0) {
                final DropController sp = new AbsolutePositionDropController(battalionPanel[index]);
                pickupDragController.registerDropController(sp);
            }
        }
    }

    public boolean isSelectedBrig() {
        return brigSelected;
    }

    private void restartWindow() {
        brigSelected = false;
        selBrigade = null;
        pickupDragController.unregisterDropControllers();
        battToAddPanel.clear();
        for (int slot = 0; slot < 6; slot++) {
            battalionPanel[slot].clear();
        }
        recalculateCosts();
        addBattalionView.populateBrigadesPanel();
    }

}
