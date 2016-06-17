package com.eaw1805.www.client.views.infopanels.units;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.constants.TradeCalculations;
import com.eaw1805.data.dto.web.TradeUnitAbstractDTO;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.data.dto.web.economy.WarehouseDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.views.infopanels.units.mini.LoadedUnitMini;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.TradeCityStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.units.TransportStore;


public class TradeUnitInfoPanel
        extends HorizontalPanel
        implements ArmyConstants, StyleConstants {

    private final ClickAbsolutePanel basePanel;
    private final TradeUnitAbstractDTO tdUnit;
    AbsolutePanel goodsPanel;

    public TradeUnitInfoPanel(final TradeUnitAbstractDTO thisUnit, final int tradePhase) {
        setSize("297px", "106px");
        tdUnit = thisUnit;
        int type;
        if (tdUnit.getClass().equals(BaggageTrainDTO.class)) {
            type = BAGGAGETRAIN;

        } else if (tdUnit.getClass().equals(ShipDTO.class)) {
            type = SHIP;

        } else if (tdUnit.getClass().equals(WarehouseDTO.class)) {
            type = WAREHOUSE;

        } else if (tdUnit.getClass().equals(TradeCityDTO.class)) {
            type = TRADECITY;

        } else {
            type = BARRACK;
        }

        basePanel = new ClickAbsolutePanel();
        basePanel.setStyleName("tradeUnit");
        basePanel.setStyleName("pointer", true);
        basePanel.setSize("297px", "106px");
        add(basePanel);

        final Image tduImg;
        final Label lblType;
        switch (type) {
            case BAGGAGETRAIN:
                tduImg = new Image("http://static.eaw1805.com/images/buttons/icons/baggage.png");
                lblType = new Label("Type: Baggage train.");
                break;

            case SHIP:
                tduImg = new Image("http://static.eaw1805.com/images/ships/" + ((ShipDTO) tdUnit).getNationId() + "/" + ((ShipDTO) tdUnit).getType().getIntId() + ".png");
                lblType = new Label("Type: Trade ship");
                break;

            case WAREHOUSE:
                tduImg = new Image("http://static.eaw1805.com/images/buttons/warehouse.png");
                lblType = new Label("Warehouse");
                break;

            case TRADECITY:
                tduImg = new Image("http://static.eaw1805.com/images/buttons/icons/tcity.png");
                lblType = new Label("Trade city");
                break;

            case BARRACK:
            default:
                tduImg = new Image("http://static.eaw1805.com/tiles/sites/tprod-12.png");
                lblType = new Label("Regional warehouse");
                break;
        }

        tduImg.setSize("65px", "65px");
        basePanel.add(tduImg, 0, 0);

        lblType.setStyleName("clearFont");
        lblType.setSize("250px", "19px");
        basePanel.add(lblType, 71, 46);

        final Label lblName = new Label(tdUnit.getName());
        lblName.setStyleName("clearFontMiniTitle");
        lblName.setSize("193px", "26px");
        basePanel.add(lblName, 71, 0);

        if ((TradeCityStore.getInstance().getTradeCityByPosition(tdUnit) != null && tradePhase == 2)
                || (TradeCityStore.getInstance().getTradeCityByPosition(tdUnit) != null && tradePhase == 1)) {
            final Image tcImg = new Image("http://static.eaw1805.com/images/buttons/icons/tcity.png");
            tcImg.setTitle("Unit is currently in a trade city.");
            basePanel.add(tcImg, 269, 18);
            tcImg.setSize("24px", "24px");
        }

        final Label lblWeight;
        if (type == SHIP || type == BAGGAGETRAIN) {
            lblWeight = new Label("Weight : " + (TradeStore.getInstance().getTradeUnitLoad(tdUnit) + TransportStore.getInstance()
                    .getUnitsLoadedWeight(tdUnit.getUnitType(), tdUnit.getId(), true)) +
                    " / " + TradeStore.getInstance().getTradeUnitWeight(tdUnit));

        } else if (type == WAREHOUSE || type == BARRACK) {
            lblWeight = new Label("Weight: Unlimited");

        } else {
            final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
            lblWeight = new Label("Wealth: " + numberFormat.format(TradeStore.getInstance().getTradeUnitWeight(tdUnit)));
        }

        lblWeight.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblWeight.setStyleName("clearFont");
        lblWeight.setSize("199px", SIZE_15PX);
        basePanel.add(lblWeight, 1, 67);

        goodsPanel = new AbsolutePanel();
        goodsPanel.setSize("300px", "35px");
        basePanel.add(goodsPanel, 1, 87);
        if (type != WAREHOUSE) {
            final Label position = new Label(tdUnit.positionToString());
            position.setStyleName("clearFontSmall");
            basePanel.add(position, 252, 2);
            if (type != TRADECITY && type != BARRACK
                    && tdUnit.getNationId() != GameStore.getInstance().getNationId()) {
                final Image nationImg = new Image("http://static.eaw1805.com/images/nations/nation-" + tdUnit.getNationId() + "-120.png");
                nationImg.setTitle(DataStore.getInstance().getNationNameByNationId(tdUnit.getNationId()));
                nationImg.setWidth("50px");
                basePanel.add(nationImg, 243, 47);
            }
        }
        if (type == TRADECITY) {
            boolean hasBonus = false;
            for (int goodId = GoodConstants.GOOD_FIRST; goodId < GoodConstants.GOOD_AP; goodId++) {
                TradeCityDTO tCity = (TradeCityDTO) thisUnit;
                double goodRate = TradeCalculations.determineSellTradeRate(tCity.getRegionId(),
                        tCity.getName(), goodId,
                        tCity.getGoodsTradeLvl().get(goodId));
                if (goodRate > tCity.getGoodsTradeLvl().get(goodId)) {
                    hasBonus = true;
                    break;
                } else if (goodRate < tCity.getGoodsTradeLvl().get(goodId)) {
                    hasBonus = true;
                    break;
                }

                double buyRate = TradeCalculations.determineBuyTradeRate(tCity.getName(), goodId,
                        tCity.getGoodsTradeLvl().get(goodId));
                if (buyRate < tCity.getGoodsTradeLvl().get(goodId)) {
                    hasBonus = true;
                    break;
                } else if (buyRate > tCity.getGoodsTradeLvl().get(goodId)) {
                    hasBonus = true;
                    break;
                }
            }
            if (hasBonus) {
                final Image bonusImg = new Image("http://static.eaw1805.com/images/tiles/bonus_star.png");
                bonusImg.setSize("20px", "20px");
                bonusImg.setTitle("Goods with bonuses");
                basePanel.add(bonusImg, 272, 44);
            }
        }
        setupGoodsPanel();

    }

    /**
     * @return the basePanel.
     */
    public ClickAbsolutePanel getBasePanel() {
        return basePanel;
    }

    public void select() {
        basePanel.setStyleName("tradeUnitSel");
        basePanel.setStyleName("pointer", true);
    }

    public void deSelect() {
        basePanel.setStyleName("tradeUnit");
        basePanel.setStyleName("pointer", true);
    }

    /**
     * @return the tdUnit.
     */
    public TradeUnitAbstractDTO getTdUnit() {
        return tdUnit;
    }

    /**
     * Setup images for the goods loaded on this baggage train.
     */
    public final void setupGoodsPanel() {
        //update available capacity
        goodsPanel.clear();
        //goods
        int posX = 0;
        int posY = 0;
        for (int index = 0; index < 14; index++) {
            final Image goodImage;
            if (tdUnit.getUnitType() != TRADECITY && tdUnit.getGoodsDTO().get(index + 1).getQte() > 0) {

                goodImage = new Image("http://static.eaw1805.com/images/goods/good-" + (index + 1) + ".png");
                goodsPanel.add(goodImage, posX, posY);
                goodImage.setSize(SIZE_15PX, SIZE_15PX);

                if (tdUnit.getGoodsDTO().get(index + 1) == null) {
                    goodImage.setTitle("null");
                } else {
                    goodImage.setTitle(tdUnit.getGoodsDTO().get(index + 1).getGoodDTO().getName() + ":" + tdUnit.getGoodsDTO().get(index + 1).getQte());
                }

                posX += 17;
                posY = 0;
            }
        }
        //if it is a ship or baggage train also search for loaded units
        if (tdUnit.getUnitType() == SHIP || tdUnit.getUnitType() == BAGGAGETRAIN) {
            final TransportUnitDTO trUnit = TransportStore.getInstance().getTransportUnitById(tdUnit.getUnitType(), tdUnit.getId());

            if (trUnit.getLoadedUnitsMap() != null) {
                Image unitImage;
                if (trUnit.getLoadedUnitsMap().containsKey(BRIGADE)
                        && !trUnit.getLoadedUnitsMap().get(BRIGADE).isEmpty()) {
                    unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButArmiesOff.png");
                    setUnitsLoadedPopup(unitImage, BRIGADE);
                    unitImage.setSize(SIZE_15PX, SIZE_15PX);
                    goodsPanel.add(unitImage, posX, posY);

                    posX += 17;
                    posY = 0;
                }

                if (trUnit.getLoadedUnitsMap().containsKey(COMMANDER)
                        && !trUnit.getLoadedUnitsMap().get(COMMANDER).isEmpty()) {

                    unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButCommandersOff.png");
                    setUnitsLoadedPopup(unitImage, COMMANDER);

                    unitImage.setSize(SIZE_15PX, SIZE_15PX);
                    goodsPanel.add(unitImage, posX, posY);

                    posX += 17;
                    posY = 0;
                }

                if (trUnit.getLoadedUnitsMap().containsKey(SPY)
                        && !trUnit.getLoadedUnitsMap().get(SPY).isEmpty()) {
                    unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButSpiesOff.png");
                    setUnitsLoadedPopup(unitImage, SPY);
                    unitImage.setSize(SIZE_15PX, SIZE_15PX);
                    goodsPanel.add(unitImage, posX, posY);
                }

            }
        }
    }

    public void setUnitsLoadedPopup(final Image unitImage, final int unitType) {
        new ToolTipPanel(unitImage) {
            @Override
            public void generateTip() {
                setTooltip(new LoadedUnitMini(TransportStore.getInstance().getTransportUnitById(tdUnit.getUnitType(), tdUnit.getId()), unitType));
            }
        };
    }
}
