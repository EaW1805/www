package com.eaw1805.www.client.views.economy.trade;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.TradeCalculations;
import com.eaw1805.data.dto.web.TradeUnitAbstractDTO;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.www.client.widgets.DualStateImage;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ParseIntBox;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.SoundStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;
import com.eaw1805.www.shared.stores.util.ClientUtil;

public class TradeBarWidget
        extends AbsolutePanel
        implements GoodConstants, ArmyConstants, RegionConstants {

    private final static int IMG_PIXELS = 116;

    private final ImageButton leftButtonImg;
    private final ImageButton rightButtonImg;
    private final DualStateImage barButtonImg;
    private final Image barImg;
    private int left = 32, right = 148, curr = 0;
    private final int offset = 32;
    private boolean selected = false;
    private final int goodId;

    /**
     * Goods/pixels ration
     * represents how many good items is one pixel.
     */
    private float fraction = 1;
    private final static String path = "http://static.eaw1805.com/images/panels/trade/";
    private final AbsolutePanel barPanel;
    private ParseIntBox integerBox;
    private int all;
    private final Label lblCost;
    private final Label lblItemCost;
    final Label lblWeight;

    public TradeBarWidget(final int theGoodId,
                          final TradeUnitAbstractDTO tdUnit1,
                          final TradeUnitAbstractDTO tdUnit2,
                          final int tradePhase) {

        setStyleName("exchPanel");
        setSize("420px", "166px");
        goodId = theGoodId;

        barPanel = new AbsolutePanel();
        barPanel.setSize("180px", "33px");
        add(barPanel, 45, 68);

        barImg = new Image(path + "tradeBar.png");
        barImg.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(final MouseDownEvent event) {
                event.preventDefault();
            }
        });
        barImg.setStyleName("pointer");
        barImg.setSize("180px", "33px");
        barPanel.add(this.barImg, 0, 0);

        barButtonImg = new DualStateImage(path + "tradeBarScroller.png");
        barButtonImg.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                barButtonImg.deselect();
                event.stopPropagation();
            }
        });

        // Determing good Factor and trade Rate.
        final double goodRate;
        final int goodFactor = WarehouseStore.getGoodFactor(goodId);

        if (tdUnit1.getUnitType() == TRADECITY) {
            // Determine good rate taking into account the special Trade Cities Traits
            goodRate = TradeCalculations.determineBuyTradeRate(tdUnit1.getName(), goodId,
                    ((TradeCityDTO) tdUnit1).getGoodsTradeLvl().get(goodId));

        } else if (tdUnit2.getUnitType() == TRADECITY) {
            // Determine good rate taking into account the special Trade Cities Traits
            goodRate = TradeCalculations.determineSellTradeRate(tdUnit2.getRegionId(),
                    tdUnit2.getName(), goodId,
                    ((TradeCityDTO) tdUnit2).getGoodsTradeLvl().get(goodId));

        } else {
            goodRate = 0d;
        }

        addDomHandler(new MouseMoveHandler() {

            public void onMouseMove(final MouseMoveEvent event) {
                if (selected
                        && event.getRelativeX(barImg.getElement()) >= 0
                        && event.getRelativeX(barImg.getElement()) <= 180) {
                    curr = event.getRelativeX(barImg.getElement()) - offset;
                    if (curr <= left - offset) {
                        curr = left - offset;

                    } else if (curr >= right - offset) {
                        curr = right - offset;
                    }

                    final int qte = (int) ((float) curr * fraction);

                    final int cost;
                    if (tdUnit1.getUnitType() == TRADECITY) {
                        cost = TradeCalculations.getBuyGoodCost(goodFactor,
                                goodRate,
                                qte,
                                ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                                GameStore.getInstance().getDeficit() == tdUnit1.getId(),
                                GameStore.getInstance().getSurplus() == tdUnit1.getId());

                    } else if (tdUnit2.getUnitType() == TRADECITY) {
                        cost = TradeCalculations.getSellGoodCost(goodFactor,
                                goodRate,
                                qte,
                                ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                                GameStore.getInstance().getDeficit() == tdUnit2.getId(),
                                GameStore.getInstance().getSurplus() == tdUnit2.getId());

                    } else {
                        cost = 0;
                    }

                    setGoodQuantities(curr, qte, cost, all);
                    barPanel.add(barButtonImg, curr, 0);
                }

            }
        }, MouseMoveEvent.getType());


        addDomHandler(new MouseUpHandler() {
            public void onMouseUp(final MouseUpEvent event) {
                selected = false;
                barButtonImg.deselect();

            }
        }, MouseUpEvent.getType());

        barButtonImg.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(final MouseDownEvent event) {
                selected = true;
                event.stopPropagation();
                event.preventDefault();
            }
        });
        barButtonImg.setStyleName("pointer");
        barButtonImg.setSize("64px", "33px");
        barPanel.add(this.barButtonImg, 56, 0);

        leftButtonImg = new ImageButton(path + "ButSliderLeftOff.png");
        leftButtonImg.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                leftButtonImg.deselect();
                leftButtonImg.setUrl(leftButtonImg.getUrl().replace("Off", "Hover"));
            }
        });
        leftButtonImg.setStyleName("pointer");
        leftButtonImg.setSize("29px", "33px");
        add(leftButtonImg, 16, 68);

        rightButtonImg = new ImageButton(path + "ButSliderRightOff.png");
        rightButtonImg.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                rightButtonImg.deselect();
                rightButtonImg.setUrl(rightButtonImg.getUrl().replace("Off", "Hover"));
            }
        });
        rightButtonImg.setStyleName("pointer");
        rightButtonImg.setSize("29px", "33px");
        add(rightButtonImg, 225, 68);

        rightButtonImg.addDoubleClickHandler(new DoubleClickHandler() {
            @Override
            public void onDoubleClick(DoubleClickEvent doubleClickEvent) {
                curr = right - offset;


                final int qte = (int) ((float) curr * fraction);

                final int cost;
                if (tdUnit1.getUnitType() == TRADECITY) {
                    cost = TradeCalculations.getBuyGoodCost(goodFactor,
                            goodRate,
                            qte,
                            ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                            GameStore.getInstance().getDeficit() == tdUnit1.getId(),
                            GameStore.getInstance().getSurplus() == tdUnit1.getId());

                } else if (tdUnit2.getUnitType() == TRADECITY) {
                    cost = TradeCalculations.getSellGoodCost(goodFactor,
                            goodRate,
                            qte,
                            ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                            GameStore.getInstance().getDeficit() == tdUnit2.getId(),
                            GameStore.getInstance().getSurplus() == tdUnit2.getId());

                } else {
                    cost = 0;
                }

                setGoodQuantities(curr, qte, cost, all);
                barPanel.add(barButtonImg, curr, 0);
            }

        });


        final Label lblTitle1 = new Label(tdUnit1.getName());
        lblTitle1.setStyleName("clearFont");
        lblTitle1.setSize("280px", "32px");
        add(lblTitle1, 16, 12);

        final Image arrowImg = new Image("http://static.eaw1805.com/images/buttons/icons/RightArrow.png");
        final int arrowX = 16 + 8 * tdUnit1.getName().length() + 32;
        arrowImg.setSize("48px", "32px");
        add(arrowImg, arrowX, 5);

        final Label lblTitle2 = new Label(tdUnit2.getName());
        final int title2X = arrowX + 62;
        lblTitle2.setStyleName("clearFont");
        lblTitle2.setSize("280px", "32px");
        add(lblTitle2, title2X, 12);

        final Label lblQuantity = new Label("Quantity:");
        lblQuantity.setStyleName("clearFont");
        lblQuantity.setSize("73px", "19px");
        add(lblQuantity, 16, 106);

        if (tdUnit1.getUnitType() == TRADECITY) {
            TradeCityDTO tcity = (TradeCityDTO) tdUnit1;
            double buyRate = TradeCalculations.determineBuyTradeRate(tcity.getName(), goodId,
                    tcity.getGoodsTradeLvl().get(goodId));
            if (buyRate < tcity.getGoodsTradeLvl().get(goodId)) {
                final Image plusImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopBuyUp.png");
                plusImg.setHeight("23px");
                add(plusImg, 378, 95);
            } else if (buyRate < tcity.getGoodsTradeLvl().get(goodId)) {
                final Image downImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopBuyDown.png");
                downImg.setHeight("23px");
                add(downImg, 378, 95);
            }
        }
        if (tdUnit2.getUnitType() == TRADECITY) {
            final TradeCityDTO tcity = (TradeCityDTO) tdUnit2;
            double sellRate = TradeCalculations.determineSellTradeRate(tcity.getRegionId(),
                    tcity.getName(), goodId,
                    tcity.getGoodsTradeLvl().get(goodId));
            if (sellRate > tcity.getGoodsTradeLvl().get(goodId)) {
                final Image plusImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopSellUp.png");
                plusImg.setHeight("23px");
                add(plusImg, 378, 95);
            } else if (sellRate < tcity.getGoodsTradeLvl().get(goodId)) {
                final Image downImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopSellDown.png");
                downImg.setHeight("23px");
                add(downImg, 378, 95);
            }
        }
        if (tdUnit1.getUnitType() != TRADECITY && tdUnit2.getUnitType() != TRADECITY) {
            final ImageButton transImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButLoadOff.png");
            transImg.setStyleName("pointer");
            transImg.setTitle("Do transaction");
            transImg.addClickHandler(new ClickHandler() {

                public void onClick(final ClickEvent event) {
                    transImg.deselect();
                    if (TradeStore.getInstance().doTransaction(tdUnit1, tdUnit2, goodId, Integer.parseInt(integerBox.getText()), tradePhase)) {
                        SoundStore.getInstance().playBuySell();
                    }
                }
            });
            add(transImg, 378, 124);

        } else {
            final DualStateImage transImg = new DualStateImage("http://static.eaw1805.com/images/buttons/taxation/MUIHarshTax.png");
            transImg.setStyleName("pointer");
            transImg.setTitle("Do transaction");
            transImg.addClickHandler(new ClickHandler() {

                public void onClick(final ClickEvent event) {
                    transImg.deselect();
                    if (TradeStore.getInstance().doTransaction(tdUnit1, tdUnit2, goodId, Integer.parseInt(integerBox.getText()), tradePhase)) {
                        SoundStore.getInstance().playBuySell();
                    }
                }
            });
            add(transImg, 378, 124);
        }

        final Image imgGood = new Image("http://static.eaw1805.com/images/goods/good-" + goodId + ".png");
        imgGood.setSize("24px", "24px");
        add(imgGood, 260, 71);

        final Image moneyImg = new Image("http://static.eaw1805.com/images/goods/good-1.png");
        moneyImg.setTitle("Total Monetary Cost");
        moneyImg.setSize("24px", "24px");
        add(moneyImg, 260, 132);

        final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
        lblWeight = new Label("Weight: " + numberFormat.format(WarehouseStore.getGoodWeight(goodId)));
        if (goodId == GOOD_MONEY) {
            lblWeight.setText("Weight: 0");
        }
        lblWeight.setStyleName("clearFont");
        lblWeight.setSize("120px", "19px");
        add(lblWeight, 290, 75);

        lblCost = new Label("0");
        lblCost.setStyleName("clearFont");
        add(lblCost, 290, 139);

        lblItemCost = new Label("Item cost:");
        lblItemCost.setStyleName("clearFont");
        add(lblItemCost, 260, 107);

        if (tdUnit1.getUnitType() != TRADECITY && tdUnit2.getUnitType() != TRADECITY) {
            moneyImg.setVisible(false);
            lblCost.setVisible(false);
            lblItemCost.setVisible(false);
        }

        // Actions to determine limits
        determineLimits(tdUnit1, tdUnit2);
    }

    private void setGoodQuantities(final int curr, int qte, final int cost, final int all) {
        final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
        if (curr + offset == right) {
            qte = all;
        }
        integerBox.setText(String.valueOf(qte));
        lblCost.setText(numberFormat.format(cost));
        lblItemCost.setText("Item cost: " + Math.ceil(cost / qte));
    }

    private void determineLimits(final TradeUnitAbstractDTO tdUnit1, final TradeUnitAbstractDTO tdUnit2) {
        final double goodRate;
        final int goodFactor = WarehouseStore.getGoodFactor(goodId);
        final int itemCost;
        if ((tdUnit1.getUnitType() == SHIP || tdUnit1.getUnitType() == BAGGAGETRAIN)
                && tdUnit1.getNationId() != GameStore.getInstance().getNationId()) {
            right = offset;
            all = 0;
            itemCost = 0;
            goodRate = 1;

        } else if (tdUnit1.getUnitType() == TRADECITY) {

            // Determine good rate taking into account the special Trade Cities Traits
            goodRate = TradeCalculations.determineBuyTradeRate(tdUnit1.getName(), goodId,
                    ((TradeCityDTO) tdUnit1).getGoodsTradeLvl().get(goodId));

            int maxBuy = TradeCalculations.getMaxBuyQTE(goodId,
                    goodFactor,
                    goodRate,
                    tdUnit1.getGoodsDTO().get(GOOD_MONEY).getQte(),
                    ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                    GameStore.getInstance().getDeficit() == tdUnit1.getId(),
                    GameStore.getInstance().getSurplus() == tdUnit1.getId(),
                    true);

            // Reduce based on the amount already bought
            maxBuy -= ((TradeCityDTO) tdUnit1).getBoughtGoods().get(goodId);

            // Check if unit has enough weight available
            final double loadUnitRemItems = TradeStore.getInstance().getRemainingLoadItemsByGoodId(tdUnit2, goodId);
            maxBuy = Math.min(maxBuy, (int) loadUnitRemItems);

            // Calculate cost of buying the maximum quantity
            final int buyCostMax = TradeCalculations.getBuyGoodCost(goodFactor,
                    goodRate,
                    maxBuy,
                    ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                    GameStore.getInstance().getDeficit() == tdUnit1.getId(),
                    GameStore.getInstance().getSurplus() == tdUnit1.getId());

            // Check available money
            final int moneyAvail;
            if (tdUnit2.getUnitType() == WAREHOUSE) {
                moneyAvail = WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_MONEY).getQte();

            } else {
                moneyAvail = tdUnit2.getGoodsDTO().get(GOOD_MONEY).getQte();
            }

            // If money available are not enough to buy max quantity
            if (buyCostMax > moneyAvail) {
                // Calculate maximum quantity based on money available
                maxBuy = TradeCalculations.getMaxBuyQTE(-1,
                        goodFactor,
                        goodRate,
                        moneyAvail,
                        ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                        GameStore.getInstance().getDeficit() == tdUnit1.getId(),
                        GameStore.getInstance().getSurplus() == tdUnit1.getId(),
                        true);
            }

            // This can be negative
            if (maxBuy < 0) {
                maxBuy = 0;
            }

            //lblWeight.setText("MAX Buy " + maxBuy);

            // Scroll-bar fraction
            fraction = maxBuy / (float) IMG_PIXELS;

            left = offset;
            right = offset;
            if (fraction > 0) {
                right = offset + IMG_PIXELS;
            }

            all = maxBuy;
            if (all < 1) {
                itemCost = 0;

            } else {
                itemCost = TradeCalculations.getBuyGoodCost(goodFactor,
                        goodRate,
                        1,
                        ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                        GameStore.getInstance().getDeficit() == tdUnit1.getId(),
                        GameStore.getInstance().getSurplus() == tdUnit1.getId());
            }

        } else if (tdUnit2.getUnitType() == TRADECITY) {

            // Determine good rate taking into account the special Trade Cities Traits
            goodRate = TradeCalculations.determineSellTradeRate(tdUnit2.getRegionId(),
                    tdUnit2.getName(), goodId,
                    ((TradeCityDTO) tdUnit2).getGoodsTradeLvl().get(goodId));

            // Maximum quantity that can be sold to trade city
            int maxSell = TradeCalculations.getMaxSellQTE(goodFactor,
                    goodRate,
                    tdUnit2.getGoodsDTO().get(GOOD_MONEY).getQte(),
                    ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                    GameStore.getInstance().getDeficit() == tdUnit2.getId(),
                    GameStore.getInstance().getSurplus() == tdUnit2.getId(),
                    true);

            // Deduct quantities already sold
            maxSell -= ((TradeCityDTO) tdUnit2).getSoldGoods().get(goodId);

            // This can be negative
            if (maxSell < 0) {
                maxSell = 0;
            }

            // Check available quantities
            maxSell = Math.min(maxSell, tdUnit1.getGoodsDTO().get(goodId).getQte());

            //lblWeight.setText("MAX Sell " + maxSell);

            fraction = (float) maxSell / (float) IMG_PIXELS;

            left = offset;
            right = offset;
            if (fraction > 0) {
                right = offset + IMG_PIXELS;
            }

            all = maxSell;
            if (all < 1) {
                itemCost = 0;

            } else {
                itemCost = TradeCalculations.getSellGoodCost(goodFactor,
                        goodRate,
                        1,
                        ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                        GameStore.getInstance().getDeficit() == tdUnit2.getId(),
                        GameStore.getInstance().getSurplus() == tdUnit2.getId());
            }

        } else if (tdUnit1.getUnitType() == BARRACK) {
            right = offset;
            all = 0;
            itemCost = 0;
            goodRate = 1;

        } else if (tdUnit2.getUnitType() == BARRACK) {
            final int maxGift = (int) WarehouseStore.getInstance().getMaxGiftQte(goodId);
            final int thisTurnGift = TradeStore.getInstance().getGiftQteThisTurn(goodId);
            final int qte;
            if (tdUnit1.getUnitType() == WAREHOUSE && goodId == GOOD_MONEY) {
                qte = WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_MONEY).getQte();

            } else {
                qte = tdUnit1.getGoodsDTO().get(goodId).getQte();
            }

            fraction = (float) (maxGift - thisTurnGift) / (float) IMG_PIXELS;
            left = offset;
            if (fraction == 0) {
                right = offset;
            } else {
                right = (int) (offset + Math.min((float) qte / fraction, IMG_PIXELS));

            }

            all = Math.min(qte, maxGift - thisTurnGift);
            itemCost = 0;
            goodRate = 1;

        } else {

            final int qte;
            if (tdUnit1.getUnitType() == WAREHOUSE && goodId == GOOD_MONEY) {
                qte = WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_MONEY).getQte();
            } else {
                qte = tdUnit1.getGoodsDTO().get(goodId).getQte();
            }

            fraction = qte / (float) IMG_PIXELS;
            final double loadUnitRemItems = TradeStore.getInstance().getRemainingLoadItemsByGoodId(tdUnit2, goodId);

            left = offset;
            if (fraction == 0) {
                right = offset;
            } else {
                right = offset + (int) Math.min(loadUnitRemItems / fraction, IMG_PIXELS);
            }

            all = (int) Math.min(loadUnitRemItems, qte);
            itemCost = 0;
            goodRate = 1;
        }

        integerBox = new ParseIntBox(all);
        integerBox.setText("0");
        integerBox.setSize("126px", "21px");
        integerBox.addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(final KeyUpEvent event) {
                if (!integerBox.getText().equals("")) {
                    curr = (int) (Integer.parseInt(integerBox.getText()) / fraction);
                    barPanel.add(barButtonImg, curr, 0);

                    final int qte = Integer.parseInt(integerBox.getText());

                    final int cost;
                    if (tdUnit1.getUnitType() == TRADECITY) {
                        cost = TradeCalculations.getBuyGoodCost(goodFactor,
                                goodRate,
                                qte,
                                ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                                GameStore.getInstance().getDeficit() == tdUnit1.getId(),
                                GameStore.getInstance().getSurplus() == tdUnit1.getId());

                    } else if (tdUnit2.getUnitType() == TRADECITY) {
                        cost = TradeCalculations.getSellGoodCost(goodFactor,
                                goodRate,
                                qte,
                                ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                                GameStore.getInstance().getDeficit() == tdUnit2.getId(),
                                GameStore.getInstance().getSurplus() == tdUnit2.getId());

                    } else {
                        cost = 0;
                    }

                    setGoodQuantities(curr, qte, cost, all);
                }
            }
        });
        add(integerBox, 16, 127);

        lblItemCost.setText("Item cost: " + itemCost);
        setWidgetPositionImpl(barButtonImg, 0, 0);

        addScrollingFunctionality(leftButtonImg, rightButtonImg, 0, all, tdUnit1, tdUnit2);
    }

    private void addScrollingFunctionality(final ImageButton leftArrow,
                                           final ImageButton rightArrow,
                                           final int min,
                                           final int max,
                                           final TradeUnitAbstractDTO tdUnit1,
                                           final TradeUnitAbstractDTO tdUnit2) {

        final double goodRate;
        final int goodFactor = WarehouseStore.getGoodFactor(goodId);

        if (tdUnit1.getUnitType() == TRADECITY) {
            // Determine good rate taking into account the special Trade Cities Traits
            goodRate = TradeCalculations.determineBuyTradeRate(tdUnit1.getName(), goodId,
                    ((TradeCityDTO) tdUnit1).getGoodsTradeLvl().get(goodId));

        } else if (tdUnit2.getUnitType() == TRADECITY) {
            // Determine good rate taking into account the special Trade Cities Traits
            goodRate = TradeCalculations.determineSellTradeRate(tdUnit2.getRegionId(),
                    tdUnit2.getName(), goodId,
                    ((TradeCityDTO) tdUnit2).getGoodsTradeLvl().get(goodId));

        } else {
            goodRate = 0;
        }

        final Timer timer = new Timer() {
            public void run() {
                if (Integer.parseInt(integerBox.getText()) >= min && Integer.parseInt(integerBox.getText()) < max) {
                    integerBox.setText(String.valueOf(Integer.parseInt(integerBox.getText()) + 1));
                    curr = (int) (Integer.parseInt(integerBox.getText()) / fraction);

                    final int qte = Integer.parseInt(integerBox.getText());

                    final int cost;
                    if (tdUnit1.getUnitType() == TRADECITY) {
                        cost = TradeCalculations.getBuyGoodCost(goodFactor,
                                goodRate,
                                qte,
                                ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                                GameStore.getInstance().getDeficit() == tdUnit1.getId(),
                                GameStore.getInstance().getSurplus() == tdUnit1.getId());

                    } else if (tdUnit2.getUnitType() == TRADECITY) {
                        cost = TradeCalculations.getSellGoodCost(goodFactor,
                                goodRate,
                                qte,
                                ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                                GameStore.getInstance().getDeficit() == tdUnit2.getId(),
                                GameStore.getInstance().getSurplus() == tdUnit2.getId());

                    } else {
                        cost = 0;
                    }

                    setGoodQuantities(curr, qte, cost, all);

                    barPanel.add(barButtonImg, curr, 0);
                }

            }
        };
        final Timer timer2 = new Timer() {
            public void run() {
                if (Integer.parseInt(integerBox.getText()) > min && Integer.parseInt(integerBox.getText()) <= max) {
                    integerBox.setText(String.valueOf(Integer.parseInt(integerBox.getText()) - 1));
                    curr = (int) (Integer.parseInt(integerBox.getText()) / fraction);

                    final int qte = Integer.parseInt(integerBox.getText());

                    final int cost;
                    if (tdUnit1.getUnitType() == TRADECITY) {
                        cost = TradeCalculations.getBuyGoodCost(goodFactor,
                                goodRate,
                                qte,
                                ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                                GameStore.getInstance().getDeficit() == tdUnit1.getId(),
                                GameStore.getInstance().getSurplus() == tdUnit1.getId());

                    } else if (tdUnit2.getUnitType() == TRADECITY) {
                        cost = TradeCalculations.getSellGoodCost(goodFactor,
                                goodRate,
                                qte,
                                ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                                GameStore.getInstance().getDeficit() == tdUnit2.getId(),
                                GameStore.getInstance().getSurplus() == tdUnit2.getId());

                    } else {
                        cost = 0;
                    }

                    setGoodQuantities(curr, qte, cost, all);

                    barPanel.add(barButtonImg, curr, 0);
                }
            }
        };

        leftArrow.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent event) {
                if (Integer.parseInt(integerBox.getText()) > min && Integer.parseInt(integerBox.getText()) <= max) {
                    integerBox.setText(String.valueOf(Integer.parseInt(integerBox.getText()) - 1));
                    curr = (int) (Integer.parseInt(integerBox.getText()) / fraction);

                    final int qte = Integer.parseInt(integerBox.getText());

                    final int cost;
                    if (tdUnit1.getUnitType() == TRADECITY) {
                        cost = TradeCalculations.getBuyGoodCost(goodFactor,
                                goodRate,
                                qte,
                                ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                                GameStore.getInstance().getDeficit() == tdUnit1.getId(),
                                GameStore.getInstance().getSurplus() == tdUnit1.getId());

                    } else if (tdUnit2.getUnitType() == TRADECITY) {
                        cost = TradeCalculations.getSellGoodCost(goodFactor,
                                goodRate,
                                qte,
                                ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                                GameStore.getInstance().getDeficit() == tdUnit2.getId(),
                                GameStore.getInstance().getSurplus() == tdUnit2.getId());

                    } else {
                        cost = 0;
                    }

                    setGoodQuantities(curr, qte, cost, all);

                    barPanel.add(barButtonImg, curr, 0);
                }
                leftArrow.deselect();
            }
        });

        leftArrow.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(final MouseDownEvent event) {
                timer2.scheduleRepeating(1);

            }
        });

        leftArrow.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                timer2.cancel();

            }
        });

        leftArrow.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(final MouseUpEvent event) {
                timer2.cancel();
            }
        });

        rightArrow.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent event) {
                if (Integer.parseInt(integerBox.getText()) >= min && Integer.parseInt(integerBox.getText()) < max) {
                    integerBox.setText(String.valueOf(Integer.parseInt(integerBox.getText()) + 1));
                    curr = (int) (Integer.parseInt(integerBox.getText()) / fraction);

                    final int qte = Integer.parseInt(integerBox.getText());

                    final int cost;
                    if (tdUnit1.getUnitType() == TRADECITY) {
                        cost = TradeCalculations.getBuyGoodCost(goodFactor,
                                goodRate,
                                qte,
                                ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                                GameStore.getInstance().getDeficit() == tdUnit1.getId(),
                                GameStore.getInstance().getSurplus() == tdUnit1.getId());

                    } else if (tdUnit2.getUnitType() == TRADECITY) {
                        cost = TradeCalculations.getSellGoodCost(goodFactor,
                                goodRate,
                                qte,
                                ClientUtil.isMerchantile(GameStore.getInstance().getNationId()),
                                GameStore.getInstance().getDeficit() == tdUnit2.getId(),
                                GameStore.getInstance().getSurplus() == tdUnit2.getId());

                    } else {
                        cost = 0;
                    }

                    setGoodQuantities(curr, qte, cost, all);

                    barPanel.add(barButtonImg, curr, 0);
                }
                rightArrow.deselect();
            }
        });

        rightArrow.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(final MouseDownEvent event) {
                timer.scheduleRepeating(1);

            }
        });

        rightArrow.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                timer.cancel();

            }
        });

        rightArrow.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(final MouseUpEvent event) {
                timer.cancel();
            }
        });
    }
}
