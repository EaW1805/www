package com.eaw1805.www.client.views;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.DualStateImage;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.StyledCheckBox;
import com.eaw1805.www.shared.StaticWidgets;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.economy.ProductionSiteStore;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;
import com.eaw1805.www.shared.stores.support.Taxation;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

public class TaxationView
        extends DraggablePanel
        implements OrderConstants, GoodConstants, RegionConstants, StyleConstants {

    private final DualStateImage lowTaxImg;
    private final DualStateImage midTaxImg;
    private final DualStateImage highTaxImg;

    private final Label lblEuropeanPopulation;
    private final StyledCheckBox useColonials20;
    private final StyledCheckBox chckbxUseGems;
    private final StyledCheckBox industrialPoints;
    private final StyledCheckBox useMoney;
    private final StyledCheckBox useColonials;

    private final Label lblColGoods;
    private final Label lblGems;
    private final Label lblNeeded;
    private final Image colGoodsImg;
    private final Image gemsImg;

    public TaxationView() {
        final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
        setStyleName("barracksPanel");
        setStyleName("whiteText", true);
        setSize("382px", "418px");

        final Label title = new Label("European Taxes");
        title.setStyleName("whiteText clearFontMedLarge");
        add(title, 12, 12);

        final ImageButton imgX = StaticWidgets.CLOSE_IMAGE_TAXATION_VIEW;
        imgX.setStyleName(CLASS_POINTER);
        final TaxationView self = this;
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().getOptionsMenu().getTaxationImg().deselect();
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(self);
                imgX.deselect();
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 17) {
                    TutorialStore.nextStep(false);
                }
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 4
                        && TutorialStore.getInstance().getTutorialStep() == 2) {
                    TutorialStore.nextStep(false);
                }

            }
        }).addToElement(imgX.getElement()).register();

        imgX.setSize("36px", "36px");
        add(imgX, 330, 10);

        final Label lblThreeTaxationOptions = new Label("Three taxation options:");
        add(lblThreeTaxationOptions, 50, 60);

        lowTaxImg = new DualStateImage("http://static.eaw1805.com/images/buttons/taxation/MUILowTax.png");
        lowTaxImg.setTitle("Set taxes to low value");
        lowTaxImg.setSize("32", "32");
        lowTaxImg.setTitle("Set taxes to low (-50% money, +25% recruits)");
        lowTaxImg.setStyleName(CLASS_POINTER, true);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (GameStore.getInstance().isNationDead()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Your empire is dead, you cannot change taxes", false);

                } else if (GameStore.getInstance().isGameEnded()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, you cannot change taxes.", false);

                } else {
                    setTaxation(TAX_LOW);

                }
            }
        }).addToElement(lowTaxImg.getElement()).register();

        add(lowTaxImg, 110, 97);

        midTaxImg = new DualStateImage("http://static.eaw1805.com/images/buttons/taxation/MUINormalTax.png");
        midTaxImg.setTitle("Set taxes to medium value");
        midTaxImg.setSize("32", "32");
        midTaxImg.setTitle("Set taxes to normal (+0% money, +0% recruits)");
        midTaxImg.setStyleName(CLASS_POINTER, true);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (GameStore.getInstance().isNationDead()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Your empire is dead, you cannot change taxes", false);

                } else if (GameStore.getInstance().isGameEnded()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, you cannot change taxes.", false);

                } else {
                    setTaxation(0);

                }
            }
        }).addToElement(midTaxImg.getElement()).register();
        add(midTaxImg, 179, 97);

        highTaxImg = new DualStateImage("http://static.eaw1805.com/images/buttons/taxation/MUIHarshTax.png");
        highTaxImg.setTitle("Set taxes to high value");
        highTaxImg.setSize("32", "32");
        highTaxImg.setTitle("Set taxes to harsh (+25% money, -50% recruits)");
        highTaxImg.setStyleName(CLASS_POINTER, true);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (GameStore.getInstance().isNationDead()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Your empire is dead, you cannot change taxes", false);

                } else if (GameStore.getInstance().isGameEnded()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, you cannot change taxes.", false);

                } else {
                    if (GameStore.getInstance().isAllowHarshTaxation()) {
                        setTaxation(TAX_HARSH);
                    } else {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "You cannot use harsh taxation 3 months in a row.", false);
                    }
                }
            }
        }).addToElement(highTaxImg.getElement()).register();

        add(highTaxImg, 243, 97);

        final Image europe = new Image("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOn.png");
        europe.setTitle("European population");
        add(europe, 51, 145);

        final Image population = new Image("http://static.eaw1805.com/images/goods/good-2.png");
        population.setTitle("European population");
        add(population, 90, 145);

        lblEuropeanPopulation = new Label(numberFormat.format(GameStore.getInstance().getTotalPopulation()) + " citizens");
        lblEuropeanPopulation.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblEuropeanPopulation.setSize("233px", "15px");
        add(lblEuropeanPopulation, 130, 151);

        industrialPoints = new StyledCheckBox("Use 10,000 Industrial Points to gain +1 VP", false, false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                //check warehouse.
                if (industrialPoints.isChecked()
                        && WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_INPT).getQte() < 10000) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Not enough industrial points in warehouse", false);
                    industrialPoints.setChecked(false);
                }
                setTaxation(0);
            }
        }).addToElement(industrialPoints.getCheckBox().getElement()).register();

        industrialPoints.setSize(SIZE_350PX, SIZE_21PX);
        industrialPoints.setTextStyle("clearFontSmall whiteText");
        add(industrialPoints, 20, 195);

        useMoney = new StyledCheckBox("Use 10,000,000 money to gain +1 VP", false, false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                //check warehouse.
                if (useMoney.isChecked()
                        && WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_MONEY).getQte() < 10000000) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Not enough money in warehouse", false);
                    useMoney.setChecked(false);
                }
                setTaxation(0);
            }
        }).addToElement(useMoney.getCheckBox().getElement()).register();

        useMoney.setSize(SIZE_350PX, SIZE_21PX);
        useMoney.setTextStyle("clearFontSmall whiteText");
        add(useMoney, 20, 225);

        useColonials = new StyledCheckBox("Use 500 colonial goods to gain +1 VP", false, false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                //check warehouse.
                if (useColonials.isChecked()
                        && WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_COLONIAL).getQte() < 500) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Not enough colonial goods in warehouse", false);
                    useColonials.setChecked(false);
                }
                setTaxation(0);
            }
        }).addToElement(useColonials.getCheckBox().getElement()).register();

        useColonials.setSize(SIZE_350PX, SIZE_21PX);
        useColonials.setTextStyle("clearFontSmall whiteText");
        add(useColonials, 20, 255);

        useColonials20 = new StyledCheckBox("Use colonial goods for a 20% income boost", false, false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (useColonials20.isChecked()
                        && WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_COLONIAL).getQte() <
                        (int) Math.ceil(GameStore.getInstance().getTotalPopulation() / 10000d)) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Not enough colonial goods in warehouse", false);
                    useColonials20.setChecked(false);
                }
                if (useColonials20.isChecked()) {
                    chckbxUseGems.setChecked(false);
                }

                setTaxation(0);
            }
        }).addToElement(useColonials20.getCheckBox().getElement()).register();

        useColonials20.setSize(SIZE_350PX, SIZE_21PX);
        useColonials20.setTextStyle("clearFontSmall whiteText");
        add(useColonials20, 20, 285);

        chckbxUseGems = new StyledCheckBox("Use colonial goods and gems for a 20% income boost", false, false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (chckbxUseGems.isChecked()
                        && WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_GEMS).getQte() <
                        (int) Math.ceil(GameStore.getInstance().getTotalPopulation() / 100000d)) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Not enough gems in warehouse", false);
                    chckbxUseGems.setChecked(false);
                }
                if (chckbxUseGems.isChecked()) {
                    useColonials20.setChecked(false);
                }
                setTaxation(0);
            }
        }).addToElement(chckbxUseGems.getCheckBox().getElement()).register();

        chckbxUseGems.setSize(SIZE_350PX, SIZE_21PX);
        chckbxUseGems.setTextStyle("clearFontSmall whiteText");
        add(chckbxUseGems, 20, 315);

        lblNeeded = new Label("Needed");
        lblNeeded.setStyleName("clearFontMiniTitle");
        add(lblNeeded, 51, 355);

        colGoodsImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_COLONIAL + PNG);
        colGoodsImg.setSize(SIZE_24PX, SIZE_24PX);
        add(colGoodsImg, 150, 355);

        lblColGoods = new Label("0");
        add(lblColGoods, 185, 360);

        gemsImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_GEMS + PNG);
        gemsImg.setSize(SIZE_24PX, SIZE_24PX);
        add(gemsImg, 225, 355);

        lblGems = new Label("0");
        lblGems.setSize("41px", "15px");
        add(lblGems, 260, 360);

        addAttachHandler(new AttachEvent.Handler() {
            public void onAttachOrDetach(final AttachEvent event) {
                initTaxation();
            }
        });
    }


    public void initTaxation() {
        final Taxation tax = ProductionSiteStore.getInstance().getTax();

        if (tax.getTaxLevel() == TAX_HARSH || tax.getTaxLevel() == TAX_LOW) {
            chckbxUseGems.setEnabled(false);
            useMoney.setEnabled(false);
            useColonials20.setEnabled(false);
            industrialPoints.setEnabled(false);
            useColonials.setEnabled(false);
            useColonials.setChecked(false);
            industrialPoints.setChecked(false);
            useColonials20.setChecked(false);
            useMoney.setChecked(false);
            chckbxUseGems.setChecked(false);
        } else {
            if (tax.isUseIndPoints()) {
                industrialPoints.setEnabled(true);

            } else {
                if (WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_INPT).getQte() < 10000) {
                    industrialPoints.setEnabled(false);
                } else {
                    industrialPoints.setEnabled(true);
                }
            }
            industrialPoints.setChecked(tax.isUseIndPoints());

            if (tax.isUseMoney()) {
                useMoney.setEnabled(true);
            } else {
                if (WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_MONEY).getQte() < 10000000) {
                    useMoney.setEnabled(false);
                } else {
                    useMoney.setEnabled(true);
                }
            }
            useMoney.setChecked(tax.isUseMoney());

            if (tax.isUseColonials()) {
                useColonials.setEnabled(true);
            } else {
                if (WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_COLONIAL).getQte() < 500) {
                    useColonials.setEnabled(false);
                } else {
                    useColonials.setEnabled(true);
                }
            }
            useColonials.setChecked(tax.isUseColonials());

            if (tax.isUseColGoods() && !tax.isUseGems()) {
                useColonials20.setEnabled(true);
            } else {
                if (WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_COLONIAL).getQte() <
                        (int) Math.ceil(GameStore.getInstance().getTotalPopulation() / 10000d)) {
                    useColonials20.setEnabled(false);
                } else {
                    useColonials20.setEnabled(true);
                }
            }
            useColonials20.setChecked(tax.isUseColGoods() && !tax.isUseGems());

            if (tax.isUseGems()) {
                chckbxUseGems.setEnabled(true);
            } else {
                if (WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_GEMS).getQte() <
                        (int) Math.ceil(GameStore.getInstance().getTotalPopulation() / 100000d)
                        || WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_COLONIAL).getQte() <
                        (int) (0.7 * Math.ceil(GameStore.getInstance().getTotalPopulation() / 10000d))) {
                    chckbxUseGems.setEnabled(false);
                } else {
                    chckbxUseGems.setEnabled(true);
                }
            }
            chckbxUseGems.setChecked(tax.isUseGems());
        }
        if (useColonials20.isChecked()) {
            showCGoodsNeeded();
            hideGemsNeeded();
        } else if (chckbxUseGems.isChecked()) {
            showCGoodsNeeded();
            showGemsNeeded();
        } else {
            hideCGoodsNeeded();
            hideGemsNeeded();
        }

        if (useColonials20.isChecked()) {
            int reqColonialGoods = (int) Math.ceil(GameStore.getInstance().getTotalPopulation() / 10000d);
            int reqGems = 0;
            lblColGoods.setText(String.valueOf(reqColonialGoods));
            lblGems.setText(String.valueOf(reqGems));
        }
        if (chckbxUseGems.isChecked()) {
            int reqColonialGoods = (int) Math.ceil(GameStore.getInstance().getTotalPopulation() / 10000d);
            int reqGems = 0;
            reqGems = (int) Math.ceil(GameStore.getInstance().getTotalPopulation() / 100000d);
            reqColonialGoods = (int) (0.7 * Math.ceil(GameStore.getInstance().getTotalPopulation() / 10000d));
            lblColGoods.setText(String.valueOf(reqColonialGoods));
            lblGems.setText(String.valueOf(reqGems));
        }
        updateLevelsImages(tax.getTaxLevel());
    }


    public void updateTaxationOrder(final int level, final OrderCostDTO cost) {
        int[] ids = new int[1];
        ids[0] = 0;
        OrderStore.getInstance().removeOrder(ORDER_TAXATION, ids);
        //if it worths to be viewed as an order...
        if (level != 0
                || useColonials20.isChecked()
                || chckbxUseGems.isChecked()
                || useMoney.isChecked()
                || industrialPoints.isChecked()
                || useColonials.isChecked()) {
            //then add a new order...
            OrderStore.getInstance().addNewOrder(ORDER_TAXATION, cost, 1, "", ids, 0, "");
        }
    }

    public void hideCGoodsNeeded() {
//        chckbxUseGems.setVisible(false);
        lblColGoods.setVisible(false);
        lblGems.setVisible(false);
        lblNeeded.setVisible(false);
        colGoodsImg.setVisible(false);
        gemsImg.setVisible(false);
    }

    public void showCGoodsNeeded() {
//        chckbxUseGems.setVisible(true);
        lblColGoods.setVisible(true);
        lblNeeded.setVisible(true);
        colGoodsImg.setVisible(true);
    }

    public void hideGemsNeeded() {
        lblGems.setVisible(false);
        gemsImg.setVisible(false);
    }

    public void showGemsNeeded() {
        lblGems.setVisible(true);
        gemsImg.setVisible(true);
    }


    public void setTaxation(final int level) {

        final Taxation tax = ProductionSiteStore.getInstance().getTax();
        tax.setTaxLevel(level);

        if (level == TAX_HARSH || level == TAX_LOW) {
            useColonials.setChecked(false);
            industrialPoints.setChecked(false);
            useColonials20.setChecked(false);
            useMoney.setChecked(false);
            chckbxUseGems.setChecked(false);
        }


        tax.setUseColonials(useColonials.isChecked());
        tax.setUseColGoods(useColonials20.isChecked() || chckbxUseGems.isChecked());
        tax.setUseMoney(useMoney.isChecked());
        tax.setUseGems(chckbxUseGems.isChecked());
        tax.setUseIndPoints(industrialPoints.isChecked());

        final OrderCostDTO cost = getTaxationCost();
        for (int goodId = GOOD_FIRST; goodId < GOOD_LAST; goodId++) {
            tax.getCost().setNumericCost(goodId, cost.getNumericCost(goodId));
        }
        updateTaxationOrder(tax.getTaxLevel(), cost);
        initTaxation();//be sure the checkboxes will be updated as enabled/disabled after the changes
    }

    public void updateLevelsImages(int level) {
        switch (level) {
            case TAX_LOW:
                midTaxImg.deselect();
                highTaxImg.deselect();
                lowTaxImg.setSelected(true);
                midTaxImg.removeStyleName(CLASS_BORDERSELECTED);
                highTaxImg.removeStyleName(CLASS_BORDERSELECTED);
                lowTaxImg.setStyleName(CLASS_BORDERSELECTED, true);

                if (!lowTaxImg.getUrl().endsWith(SLC_PNG)) {
                    lowTaxImg.setUrl(lowTaxImg.getUrl().replace(PNG, SLC_PNG));
                }
                break;
            case TAX_HARSH:
                lowTaxImg.deselect();
                midTaxImg.deselect();
                highTaxImg.setSelected(true);

                midTaxImg.removeStyleName(CLASS_BORDERSELECTED);
                lowTaxImg.removeStyleName(CLASS_BORDERSELECTED);
                highTaxImg.setStyleName(CLASS_BORDERSELECTED, true);
                if (!highTaxImg.getUrl().endsWith(SLC_PNG)) {
                    highTaxImg.setUrl(highTaxImg.getUrl().replace(PNG, SLC_PNG));
                }
                break;
            default:
                final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
                lblEuropeanPopulation.setText(numberFormat.format(GameStore.getInstance().getTotalPopulation()) + " citizens");
                lowTaxImg.deselect();
                highTaxImg.deselect();
                midTaxImg.setSelected(true);
                lowTaxImg.removeStyleName(CLASS_BORDERSELECTED);
                highTaxImg.removeStyleName(CLASS_BORDERSELECTED);
                midTaxImg.setStyleName(CLASS_BORDERSELECTED, true);
                if (!midTaxImg.getUrl().endsWith(SLC_PNG)) {
                    midTaxImg.setUrl(midTaxImg.getUrl().replace(PNG, SLC_PNG));
                }
                break;
        }
    }


    public OrderCostDTO getTaxationCost() {
        final int totPopulation = GameStore.getInstance().getTotalPopulation();

        int reqColonialGoods = 0;
        int reqGems = 0;
        int reqMoney = 0;
        int reqInPts = 0;

        if (industrialPoints.isChecked()) {
            reqInPts = 10000;
        }

        if (useMoney.isChecked()) {
            reqMoney = 10000000;
        }

        if (useColonials.isChecked()) {
            reqColonialGoods = 500;
        }

        if (useColonials20.isChecked()) {
            reqColonialGoods += (int) Math.ceil(totPopulation / 10000d);
        }

        if (chckbxUseGems.isChecked()) {
            reqGems = (int) Math.ceil(totPopulation / 100000d);
            reqColonialGoods += (int) (0.7 * Math.ceil(totPopulation / 10000d));
        }

        return CostCalculators.getTaxationCost(reqInPts, reqMoney, reqColonialGoods, reqGems);
    }
}
