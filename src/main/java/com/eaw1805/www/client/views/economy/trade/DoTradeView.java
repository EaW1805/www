package com.eaw1805.www.client.views.economy.trade;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.constants.*;
import com.eaw1805.data.dto.web.TradeUnitAbstractDTO;
import com.eaw1805.data.dto.web.economy.StoredGoodDTO;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.www.client.events.trade.*;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.widgets.StyledCheckBox;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;
import com.eaw1805.www.shared.stores.units.TransportStore;

public class DoTradeView
        extends AbsolutePanel
        implements GoodConstants, ArmyConstants, RegionConstants, TradeConstants {

    private final VerticalPanel tradeBarsPanel;
    private final TradeUnitAbstractDTO tradedUnit1, tradedUnit2;
    private final int tradePhase;

    public DoTradeView(final TradeUnitAbstractDTO tdUnit1,
                       final TradeUnitAbstractDTO tdUnit2,
                       final int tradePhase) {
        this.tradedUnit1 = tdUnit1;
        this.tradedUnit2 = tdUnit2;
        setStyleName("doTradeTab");
        setSize("1148px", "559px");
        this.tradePhase = tradePhase;

        final VerticalPanel tdUnit1GoodsPanel = new VerticalPanel();
        tdUnit1GoodsPanel.setSize("314px", "448px");
        add(tdUnit1GoodsPanel, 64, 59);

        final VerticalPanel tdUnit2GoodsPanel = new VerticalPanel();
        tdUnit2GoodsPanel.setSize("273px", "448px");
        add(tdUnit2GoodsPanel, 869, 59);

        this.tradeBarsPanel = new VerticalPanel();
        add(this.tradeBarsPanel, 384, 59);
        this.tradeBarsPanel.setSize("445px", "448px");

        //Initialize the first trade goods panel
        intGoodsPanel(tdUnit1GoodsPanel, tdUnit1, 1);

        //Initialize the second trade goods panel
        intGoodsPanel(tdUnit2GoodsPanel, tdUnit2, 2);

        final Label lblTdunit1name = new Label(tdUnit1.getName());
        lblTdunit1name.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblTdunit1name.setStyleName("clearFont-large");
        lblTdunit1name.setStyleName("whiteText", true);
        lblTdunit1name.setSize("346px", "28px");
        add(lblTdunit1name, 32, 25);

        final Label lblTdunit2name = new Label(tdUnit2.getName());
        lblTdunit2name.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblTdunit2name.setStyleName("clearFont-large");
        lblTdunit2name.setStyleName("whiteText", true);
        lblTdunit2name.setSize("317px", "28px");
        add(lblTdunit2name, 834, 25);

        if (tradedUnit1.getUnitType() == TRADECITY || tradedUnit2.getUnitType() == TRADECITY) {
            intiTradeBarsPanel(tdUnit1, tdUnit2, GOOD_INPT);
        } else {
            intiTradeBarsPanel(tdUnit1, tdUnit2, GOOD_MONEY);
        }
    }

    private void intiTradeBarsPanel(final TradeUnitAbstractDTO tdUnit1,
                                    final TradeUnitAbstractDTO tdUnit2,
                                    final int goodId) {
        tradeBarsPanel.clear();
        tradeBarsPanel.add(new ExchangePanel(goodId, tdUnit1, tdUnit2, tradePhase));

    }

    private void intGoodsPanel(final VerticalPanel tdUnitGoodsPanel,
                               final TradeUnitAbstractDTO tdUnit,
                               final int container) {
        final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
        tdUnitGoodsPanel.clear();
        final StyledCheckBox[] checkBox = new StyledCheckBox[14];
        int i = 0;

        for (final StoredGoodDTO good : tdUnit.getGoodsDTO().values()) {
            final int goodId = good.getGoodDTO().getGoodId();
            if (goodId < GOOD_AP) {

                final AbsolutePanel goodPanel = new AbsolutePanel();
                tdUnitGoodsPanel.add(goodPanel);
                goodPanel.setSize("100%", "32px");

                final Label lblName = new Label(WarehouseStore.getInstance().getGoodName(goodId));
                lblName.setStyleName("clearFontMedium");
                goodPanel.add(lblName, 0, 10);
                lblName.setSize("127px", "15px");

                final Label lblQte;
                final Image imgQte;
                if (tdUnit.getUnitType() == TRADECITY) {

                    if (goodId == GOOD_MONEY) {
                        // Change name from 'Money' to 'Wealth'
                        lblName.setText("Wealth");

                        lblQte = new Label(numberFormat.format(tdUnit.getGoodsDTO().get(goodId).getQte()));
                        lblQte.setStyleName("clearFontMedSmall textRight");
                        lblQte.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
                        lblQte.setSize("65px", "15px");
                        goodPanel.add(lblQte, 130, 10);

                    } else {
                        if (((TradeCityDTO) tdUnit).getGoodsTradeLvl().get(goodId) < TRADE_HD) {
                            lblQte = null;
                            if (((TradeCityDTO) tdUnit).getGoodsTradeLvl().get(goodId) != 0 &&
                                    ((TradeCityDTO) tdUnit).getGoodsTradeLvl().get(goodId) != TRADE_HD) {
                                imgQte = new Image("http://static.eaw1805.com/images/panels/trade/buttons/" +
                                        "CLR" + WarehouseStore.getInstance().getGoodName(goodId).split(" ")[0]
                                        + "0" + (TRADE_HD - ((TradeCityDTO) tdUnit).getGoodsTradeLvl().get(goodId))
                                        + ".png");
                                imgQte.setSize("60px", "24px");
                                goodPanel.add(imgQte, 130, 4);
                            }

                        } else {
                            lblQte = new Label("");
                            lblQte.setStyleName("clearFontMedSmall textRight");
                            lblQte.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                            lblQte.setSize("78px", "15px");
                            goodPanel.add(lblQte, 130, 10);
                        }

                    }
                    if (goodId != GOOD_MONEY) {
                        final TradeCityDTO tcity = (TradeCityDTO) tdUnit;
                        final double goodRate = TradeCalculations.determineSellTradeRate(tcity.getRegionId(),
                                tcity.getName(), goodId,
                                tcity.getGoodsTradeLvl().get(goodId));

                        if (goodRate > tcity.getGoodsTradeLvl().get(goodId)) {
                            final Image plusImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopSellUp.png");
                            plusImg.setHeight("23px");
                            goodPanel.add(plusImg, 182, 4);

                        } else if (goodRate < tcity.getGoodsTradeLvl().get(goodId)) {
                            final Image downImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopSellDown.png");
                            downImg.setHeight("23px");
                            goodPanel.add(downImg, 182, 4);
                        }

                        final double buyRate = TradeCalculations.determineBuyTradeRate(tcity.getName(), goodId,
                                tcity.getGoodsTradeLvl().get(goodId));
                        if (buyRate < tcity.getGoodsTradeLvl().get(goodId)) {
                            final Image plusImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopBuyUp.png");
                            plusImg.setHeight("23px");
                            goodPanel.add(plusImg, 182, 4);

                        } else if (buyRate < tcity.getGoodsTradeLvl().get(goodId)) {
                            final Image downImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopBuyDown.png");
                            downImg.setHeight("23px");
                            goodPanel.add(downImg, 182, 4);
                        }
                    }

                } else {
                    if (tdUnit.getUnitType() == WAREHOUSE && goodId == GOOD_MONEY) {
                        lblQte = new Label(numberFormat.format(WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_MONEY).getQte()));

                    } else {
                        lblQte = new Label(numberFormat.format(tdUnit.getGoodsDTO().get(goodId).getQte()));
                    }

                    lblQte.setStyleName("clearFontMedSmall textRight");
                    lblQte.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                    goodPanel.add(lblQte, 120, 10);
                    lblQte.setSize("75px", "15px");
                }

                final double goodWeight = WarehouseStore.getGoodWeight(goodId);
                final float weightAll = TradeStore.getInstance().getTradeUnitWeight(tdUnit);
                final Label lblPerc;
                if (tdUnit.getUnitType() == TRADECITY) {
                    lblPerc = new Label("-");
                    lblPerc.setStyleName("clearFontMedSmall");
                    goodPanel.add(lblPerc, 205, 10);
                    lblPerc.setSize("48px", "15px");

                } else if (tdUnit.getUnitType() == WAREHOUSE) {
                    lblPerc = new Label("-");
                    lblPerc.setStyleName("clearFontMedSmall");
                    goodPanel.add(lblPerc, 205, 10);
                    lblPerc.setSize("48px", "15px");

                } else {
                    final String value = NumberFormat.getFormat("00.0").format((goodWeight * (float) tdUnit.getGoodsDTO().get(goodId).getQte() / weightAll) * 100);
                    if (goodId == GOOD_MONEY) {
                        lblPerc = new Label("0%");

                    } else {
                        lblPerc = new Label(value + "%");
                    }
                    lblPerc.setStyleName("clearFontMedSmall");
                    goodPanel.add(lblPerc, 205, 10);
                    lblPerc.setSize("48px", "15px");
                }
                lblPerc.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

                final int index = i;
                if (container == 1) {
                    final boolean isMarket = (tradedUnit1.getUnitType() == TRADECITY || tradedUnit2.getUnitType() == TRADECITY);
                    if (!isMarket && goodId == GOOD_MONEY) {
                        checkBox[i] = new StyledCheckBox("", true, true);

                    } else if (isMarket && goodId == GOOD_INPT) {
                        checkBox[i] = new StyledCheckBox("", true, true);

                    } else if ((!isMarket && goodId == GOOD_PEOPLE && tradedUnit1.getNationId() == GameStore.getInstance().getNationId()
                            && tradedUnit2.getNationId() == GameStore.getInstance().getNationId())
                            || (!isMarket && goodId != GOOD_PEOPLE)
                            || (isMarket && goodId != GOOD_PEOPLE && goodId != GOOD_MONEY)) {
                        checkBox[i] = new StyledCheckBox("", false, true);
                    }

                    if (checkBox[i] != null) {
                        (new DelEventHandlerAbstract() {
                            @Override
                            public void execute(final MouseEvent event) {
                                final boolean isMarket = (tradedUnit1.getUnitType() == TRADECITY || tradedUnit2.getUnitType() == TRADECITY);
                                if (checkBox[index] != null) {
                                    if (!checkBox[index].isChecked()) {
                                        checkBox[index].setChecked(true);
                                    }
                                    intiTradeBarsPanel(tradedUnit1, tradedUnit2, goodId);
                                    for (int j = 0; j < 14; j++) {
                                        if (index != j
                                                && (!isMarket || (isMarket && j != 0))
                                                && checkBox[j] != null) {
                                            checkBox[j].setChecked(false);
                                        }
                                    }
                                }
                            }
                        }).addToElement(checkBox[i].getCheckBox().getElement()).register();
                        goodPanel.add(checkBox[i], 280, 5);
                    }
                }

                final StoredGoodDTO gd = tdUnit.getGoodsDTO().get(goodId);
                TradeEventManager.addGetGoodHanlder(new GetGoodHandler() {

                    public void onGetGoodIn(final GetGoodEvent getGoodEvent) {
                        if (gd.getGoodDTO().getGoodId() == getGoodEvent.getGoodId()
                                && tdUnit.getId() == getGoodEvent.getUnitId()
                                && tdUnit.getUnitType() == getGoodEvent.getUnitType()) {
                            final double goodWeight = WarehouseStore.getGoodWeight(gd.getGoodDTO().getGoodId());
                            final float weightAll = TradeStore.getInstance().getTradeUnitWeight(tdUnit);

                            if (tdUnit.getUnitType() == WAREHOUSE && goodId == GOOD_MONEY) {
                                lblQte.setText(NumberFormat.getDecimalFormat().format(WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_MONEY).getQte()));

                            } else if (getGoodEvent.getUnitType() != TRADECITY && lblQte != null) {
                                lblQte.setText(NumberFormat.getDecimalFormat().format(gd.getQte()));
                            }

                            if (getGoodEvent.getUnitType() != TRADECITY
                                    && getGoodEvent.getUnitType() != WAREHOUSE) {
                                final String value = NumberFormat.getFormat("00.0").format((goodWeight * (float) gd.getQte() / weightAll) * (float) 100);
                                if (goodId == GOOD_MONEY) {
                                    lblPerc.setText("0%");
                                } else {
                                    lblPerc.setText(value + "%");
                                }
                            }

                            if (goodId != GOOD_MONEY || (tradedUnit1.getUnitType() != TRADECITY && tradedUnit2.getUnitType() != TRADECITY)) {
                                intiTradeBarsPanel(tradedUnit1, tradedUnit2, goodId);
                            }
                        }
                    }
                });

                TradeEventManager.addGiveGoodHanlder(new GiveGoodHandler() {
                    public void onGiveGoodIn(final GiveGoodEvent giveGoodEvent) {
                        if (gd.getGoodDTO().getGoodId() == giveGoodEvent.getGoodId() && tdUnit.getId() == giveGoodEvent.getUnitId() &&
                                tdUnit.getUnitType() == giveGoodEvent.getUnitType()) {

                            final double goodWeight = WarehouseStore.getGoodWeight(gd.getGoodDTO().getGoodId());
                            final float weightAll = TradeStore.getInstance().getTradeUnitWeight(tdUnit);
                            if (tdUnit.getUnitType() == WAREHOUSE && goodId == GOOD_MONEY) {
                                lblQte.setText(NumberFormat.getDecimalFormat().format(WarehouseStore.getInstance().getWareHouseByRegion(EUROPE).getGoodsDTO().get(GOOD_MONEY).getQte()));

                            } else if (giveGoodEvent.getUnitType() != TRADECITY && lblQte != null) {
                                lblQte.setText(NumberFormat.getDecimalFormat().format(gd.getQte()));
                            }

                            if (giveGoodEvent.getUnitType() != TRADECITY && giveGoodEvent.getUnitType() != WAREHOUSE) {
                                final String value = NumberFormat.getFormat("00.0").format((goodWeight * (float) gd.getQte() / weightAll) * (float) 100);
                                if (goodId == GOOD_MONEY) {
                                    lblPerc.setText("0%");
                                } else {
                                    lblPerc.setText(value + "%");
                                }
                            }

                            if (goodId != GOOD_MONEY || (tradedUnit1.getUnitType() != TRADECITY && tradedUnit2.getUnitType() != TRADECITY)) {
                                intiTradeBarsPanel(tradedUnit1, tradedUnit2, goodId);
                            }
                        }
                    }
                });
                i++;
                if (tdUnit.getUnitType() == TRADECITY && goodId == GOOD_MONEY) {
                    addDummyRow(tdUnitGoodsPanel);
                }
            }
        }
        if (tdUnit.getUnitType() == SHIP || tdUnit.getUnitType() == BAGGAGETRAIN) {
            addLoadedUnitsRow(tdUnitGoodsPanel, tdUnit);
        }
    }

    private void addDummyRow(final VerticalPanel tdUnitGoodsPanel) {
        final AbsolutePanel goodPanel = new AbsolutePanel();
        tdUnitGoodsPanel.add(goodPanel);
        goodPanel.setSize("100%", "32px");

        final Label lblName = new Label("People");
        lblName.setStyleName("clearFontMedium");
        goodPanel.add(lblName, 0, 10);
        lblName.setSize("127px", "15px");

        final Label lblQte;
        lblQte = new Label("-");
        lblQte.setStyleName("clearFontMedium");
        goodPanel.add(lblQte, 130, 10);
        lblQte.setSize("86px", "15px");

        final Label lblPerc;
        lblPerc = new Label("-");
        lblPerc.setStyleName("clearFontMedium");
        lblPerc.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        goodPanel.add(lblPerc, 205, 10);
        lblPerc.setSize("48px", "15px");
    }

    /**
     * Add a row containing info about the weight of the loaded units.
     *
     * @param tdUnitGoodsPanel The panel to store the info.
     * @param tdUnit           The trade / transport unit.
     */
    private void addLoadedUnitsRow(final VerticalPanel tdUnitGoodsPanel,
                                   final TradeUnitAbstractDTO tdUnit) {
        final AbsolutePanel goodPanel = new AbsolutePanel();
        tdUnitGoodsPanel.add(goodPanel);
        goodPanel.setSize("100%", "32px");

        final Label lblName = new Label("Loaded Units");
        lblName.setStyleName("clearFontMedium");
        goodPanel.add(lblName, 0, 10);
        lblName.setSize("127px", "15px");

        final int weight = TransportStore.getInstance()
                .getUnitsLoadedWeight(tdUnit.getUnitType(), tdUnit.getId(), true);
        final Label lblQte;
        lblQte = new Label(Integer.toString(weight));
        lblQte.setStyleName("clearFontMedium");
        goodPanel.add(lblQte, 130, 10);
        lblQte.setSize("86px", "15px");

        final float weightAll = TradeStore.getInstance().getTradeUnitWeight(tdUnit);
        final String value = NumberFormat.getFormat("00.0").format((weight / weightAll) * 100);
        final Label lblPerc;
        lblPerc = new Label(value + "%");
        lblPerc.setStyleName("clearFontMedium");
        goodPanel.add(lblPerc, 205, 10);
        lblPerc.setSize("48px", "15px");
    }
}
