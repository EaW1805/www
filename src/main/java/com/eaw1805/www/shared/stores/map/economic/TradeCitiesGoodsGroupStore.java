package com.eaw1805.www.shared.stores.map.economic;

import com.google.gwt.user.client.Window;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.TradeCalculations;
import com.eaw1805.data.constants.TradeConstants;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeCitiesGoodsGroupStore implements GoodConstants, TradeConstants, RegionConstants {

    Map<Integer, Group> groupMap = new HashMap<Integer, Group>();

    public TradeCitiesGoodsGroupStore() {
        //do nothing at this point
    }

    public void initGroups(final List<TradeCityDTO> trCities) {
        try {
            for (TradeCityDTO trCity : trCities) {
                initializeTradeCityGroup(trCity.getRegionId(), trCity);
            }
        } catch (Exception e) {
            Window.alert("ff ? " + e.toString());
        }
    }

    public Group getTradeCitiesGoodsByRegion(final int region) {
        if (!groupMap.containsKey(region)) {
            groupMap.put(region, new Group());
        }
        return groupMap.get(region);
    }

    public void initializeTradeCityGroup(final int region, final TradeCityDTO tcity) {
        getTradeCitiesGoodsByRegion(region).add(new Group() {
            boolean tcityinitialized = false;
            public void onAttach() {
                super.onAttach();
                if (!tcityinitialized) {
                    this.add(constructGoodsPanel(tcity));
                    tcityinitialized = true;
                }
            }
        });

    }

    private Group constructGoodsPanel(final TradeCityDTO trCity) {
        int pointX = MapStore.getInstance().getPointX(trCity.getX() - 1) - 3;
        int pointY = MapStore.getInstance().getPointY(trCity.getY() + 1) - 3;

        final Group out = new Group();

        final Image bgImg = new Image(pointX, pointY, (int) (3 * MapStore.TILE_SIZE + 6), (int) (3 * MapStore.TILE_SIZE + 6), "http://static.eaw1805.com/images/infopanels/costInfoMini.png");
        out.add(bgImg);
        final Text cityName = new Text(pointX + 6, pointY + 19, trCity.getName());
        cityName.setFillColor("black");
        cityName.setStrokeColor("black");
        cityName.setFontSize(16);
        out.add(cityName);
        int posX = pointX + 5;
        int posY = pointY + 30;

        for (int goodId = GOOD_INPT; goodId <= GOOD_COLONIAL; goodId++) {
            final Image goodImage = new Image(posX, posY, 20, 20, "http://static.eaw1805.com/images/goods/good-" + goodId + ".png");
            out.add(goodImage);

            if (trCity.getGoodsTradeLvl().get(goodId) < TRADE_HD &&
                    trCity.getGoodsTradeLvl().get(goodId) > 0) {
                final Image suruplusImg = new Image(posX + 25, posY, 50, 20, "http://static.eaw1805.com/images/panels/trade/buttons/" +
                        "CLR" + WarehouseStore.getInstance().getGoodName(goodId).split(" ")[0]
                        + "0" + (TRADE_HD - trCity.getGoodsTradeLvl().get(goodId))
                        + ".png");
                out.add(suruplusImg);

                double goodRate = TradeCalculations.determineSellTradeRate(trCity.getRegionId(),
                        trCity.getName(), goodId,
                        trCity.getGoodsTradeLvl().get(goodId));
                if (goodRate > trCity.getGoodsTradeLvl().get(goodId)) {
                    final Image plusImg = new Image(posX + 60, posY, 17, 23, "http://static.eaw1805.com/images/tiles/MUIPopSellUp.png");
                    out.add(plusImg);
                } else if (goodRate < trCity.getGoodsTradeLvl().get(goodId)) {
                    final Image downImg = new Image(posX + 60, posY, 17, 23, "http://static.eaw1805.com/images/tiles/MUIPopSellDown.png");
                    out.add(downImg);
                }

                double buyRate = TradeCalculations.determineBuyTradeRate(trCity.getName(), goodId,
                        trCity.getGoodsTradeLvl().get(goodId));
                if (buyRate < trCity.getGoodsTradeLvl().get(goodId)) {
                    final Image plusImg = new Image(posX + 60, posY, 17, 23, "http://static.eaw1805.com/images/tiles/MUIPopBuyUp.png");
                    out.add(plusImg);
                } else if (buyRate > trCity.getGoodsTradeLvl().get(goodId)) {
                    final Image downImg = new Image(posX + 60, posY, 17, 23, "http://static.eaw1805.com/images/tiles/MUIPopBuyDown.png");

                    out.add(downImg);
                }

            }
            posY += 23;
            if (goodId == 9) {
                posX += 83;
                posY = pointY + 30;
            }
        }
        return out;
    }


}
