package com.eaw1805.www.client.views.popups;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.TradeCalculations;
import com.eaw1805.data.constants.TradeConstants;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;

public class TradeCityGoodsPopup extends AbsolutePanel
        implements GoodConstants, TradeConstants {

    boolean initialized = false;
    TradeCityDTO tCity;
    public TradeCityGoodsPopup(TradeCityDTO tCity) {
        this.tCity = tCity;

    }

    public void onAttach() {
        super.onAttach();
        if (!initialized) {
            initialize();
            initialized = true;
        }
    }

    public void initialize() {
        this.setSize("199px", "166px");
        this.setStyleName("costInfoMini");
        int posX = 3;
        int posY = 3;

        for (int goodId = GOOD_INPT; goodId <= GOOD_COLONIAL; goodId++) {
            final Image goodImage = new Image("http://static.eaw1805.com/images/goods/good-" + goodId + ".png");
            goodImage.setSize("20px", "20px");
            add(goodImage, posX, posY);
            if (tCity.getGoodsTradeLvl().get(goodId) < TRADE_HD &&
                    tCity.getGoodsTradeLvl().get(goodId) > 0) {


                final Image surplusImg = new Image("http://static.eaw1805.com/images/panels/trade/buttons/" +
                        "CLR" + WarehouseStore.getInstance().getGoodName(goodId).split(" ")[0]
                        + "0" + (TRADE_HD - tCity.getGoodsTradeLvl().get(goodId))
                        + ".png");
                add(surplusImg, posX + 25, posY);
                surplusImg.setHeight("20px");

                double goodRate = TradeCalculations.determineSellTradeRate(tCity.getRegionId(),
                        tCity.getName(), goodId,
                        tCity.getGoodsTradeLvl().get(goodId));
                if (goodRate > tCity.getGoodsTradeLvl().get(goodId)) {
                    final Image plusImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopSellUp.png");
                    plusImg.setHeight("23px");
                    add(plusImg, posX + 60, posY);
                } else if (goodRate < tCity.getGoodsTradeLvl().get(goodId)) {
                    final Image downImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopSellDown.png");
                    downImg.setHeight("23px");
                    add(downImg, posX + 60, posY);
                }

                double buyRate = TradeCalculations.determineBuyTradeRate(tCity.getName(), goodId,
                        tCity.getGoodsTradeLvl().get(goodId));
                if (buyRate < tCity.getGoodsTradeLvl().get(goodId)) {
                    final Image plusImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopBuyUp.png");
                    plusImg.setHeight("23px");
                    add(plusImg, posX + 60, posY);
                } else if (buyRate > tCity.getGoodsTradeLvl().get(goodId)) {
                    final Image downImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopBuyDown.png");
                    downImg.setHeight("23px");
                    add(downImg, posX + 60, posY);
                }

            }
            posY += 23;
            if (goodId == 9) {
                posX += 83;
                posY = 3;
            }
        }
    }


}
