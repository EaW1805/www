package com.eaw1805.www.client.views.economy.trade;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.TradeCalculations;
import com.eaw1805.data.constants.TradeConstants;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.shared.stores.economy.TradeCityStore;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;

public class TradeCitiesView
        extends AbsolutePanel
        implements GoodConstants, ArmyConstants, RegionConstants, TradeConstants {

    private static final String IMAGE_NAME = "http://static.eaw1805.com/images/panels/trade/tradeCity.png";

    public TradeCitiesView() {
        this.setSize("1148px", "560px");

        //Get an instance of the localstorage.
        final Storage localStorage = Storage.getLocalStorageIfSupported();

        //Create a new canvas.
        final Canvas canvas = Canvas.createIfSupported();

        //Check if Canvas and LocalStorage is supported.
        if (localStorage != null && canvas != null) {

            //Retrieve the image from localstorage.
            final String localImageData = localStorage.getItem(IMAGE_NAME);

            if (localImageData == null) {
                //Get the Image from the server.
                final Image img = new Image(IMAGE_NAME);
                img.setWidth("100%");
                img.setVisible(true);
                this.add(img);

                //Add image loader handler. Base64 data should be extracted after the complete downloading of the image.
                img.addLoadHandler(new LoadHandler() {

                    public void onLoad(final LoadEvent event) {
                        logMessage("Image loaded");

                        //Get the 2d context
                        final Context2d context = canvas.getContext2d();

                        //Get Image dimensions
                        final double ch = img.getHeight();
                        final double cw = img.getWidth();

                        //Set the Coordinates of the canvas
                        canvas.setCoordinateSpaceHeight((int) ch);
                        canvas.setCoordinateSpaceWidth((int) cw);

                        //Create a new ImageElement from the image file.
                        final ImageElement imageElement = ImageElement.as(img.getElement());

                        //Get the dimensions of the image.
                        final double sx = 0;
                        final double sy = 0;
                        final double sw = imageElement.getWidth();
                        final double sh = imageElement.getHeight();

                        // draw the imageElement to canvas
                        context.drawImage(imageElement, sx, sy, sw, sh);

                        //Get the base64 data.
                        final String data = canvas.toDataUrl();

                        //Store the base64 data to storage.
                        localStorage.setItem(IMAGE_NAME, data);

                        logMessage("Image added to localstorage");
                    }
                });
            } else {

                final Image localImage = new Image();
                //Update image src attribute with the base64 encoded data.
                loadImage(localImage.getElement(), localImageData);
                localImage.setWidth("100%");
                localImage.setVisible(true);
                this.add(localImage);

                logMessage("Image loaded from local storage");
            }

        } else {
            this.setStyleName("tradeCitiesPanel");
            logMessage("Local Storage or Canvas is not supported. Using CSS file.");
        }

        final VerticalPanelScrollChild verticalPanel = new VerticalPanelScrollChild();
//        add(this.verticalPanel, 23, 41);
        verticalPanel.setSize("1082px", "30px");

        final ScrollVerticalBarEAW scroller = new ScrollVerticalBarEAW(verticalPanel, false);
        scroller.setSize(1107, 509);
        scroller.enableAndSetStep(30);
        add(scroller, 20, 41);
        for (TradeCityDTO tcity : TradeCityStore.getInstance().getTradeCitiesList()) {
            verticalPanel.add(getTcityRow(tcity));
        }

        final Label lblWealth = new Label("Wealth");
        lblWealth.setStyleName("clearFont-large");
        lblWealth.setStyleName("whiteText", true);
        add(lblWealth, 200, 5);

        final Label lblCity = new Label("City");
        lblCity.setStyleName("clearFont-large");
        lblCity.setStyleName("whiteText", true);
        add(lblCity, 45, 5);
    }

    private AbsolutePanel getTcityRow(final TradeCityDTO tcity) {
        final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
        final AbsolutePanel tCityRowPanel = new AbsolutePanel();
        tCityRowPanel.setHeight("30px");

        final Label lblName = new Label(tcity.getName());
        lblName.setStyleName("clearFont");
        lblName.setSize("107px", "15px");
        tCityRowPanel.add(lblName, 0, 7);

        final Label lblWealth = new Label(numberFormat.format(tcity.getGoodsDTO().get(GOOD_MONEY).getQte()));
        lblWealth.setStyleName("clearFont textRight");
        lblWealth.setSize("161px", "15px");
        tCityRowPanel.add(lblWealth, 130, 7);
        for (int goodId = GOOD_INPT; goodId <= GOOD_COLONIAL; goodId++) {
            // Special Trade Cities Traits


            if (tcity.getGoodsTradeLvl().get(goodId) < TRADE_HD &&
                    tcity.getGoodsTradeLvl().get(goodId) > 0) {
                final Image goodImg = new Image("http://static.eaw1805.com/images/panels/trade/buttons/" +
                        "CLR" + WarehouseStore.getInstance().getGoodName(goodId).split(" ")[0]
                        + "0" + (TRADE_HD - tcity.getGoodsTradeLvl().get(goodId))
                        + ".png");
                tCityRowPanel.add(goodImg, 309 + ((goodId - GOOD_INPT) * 4) + ((goodId - GOOD_INPT) * 60), 3);
                goodImg.setSize("60px", "24px");
            }
            double goodRate = TradeCalculations.determineSellTradeRate(tcity.getRegionId(),
                    tcity.getName(), goodId,
                    tcity.getGoodsTradeLvl().get(goodId));
            if (goodRate > tcity.getGoodsTradeLvl().get(goodId)) {
                final Image plusImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopSellUp.png");
                plusImg.setHeight("23px");
                tCityRowPanel.add(plusImg, 309 + ((goodId - GOOD_INPT) * 4) + ((goodId - GOOD_INPT) * 60) + 36, 3);
            } else if (goodRate < tcity.getGoodsTradeLvl().get(goodId)) {
                final Image downImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopSellDown.png");
                downImg.setHeight("23px");
                tCityRowPanel.add(downImg, 309 + ((goodId - GOOD_INPT) * 4) + ((goodId - GOOD_INPT) * 60) + 36, 3);
            }

            double buyRate = TradeCalculations.determineBuyTradeRate(tcity.getName(), goodId,
                    tcity.getGoodsTradeLvl().get(goodId));
            if (buyRate < tcity.getGoodsTradeLvl().get(goodId)) {
                final Image plusImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopBuyUp.png");
                plusImg.setHeight("23px");
                tCityRowPanel.add(plusImg, 309 + ((goodId - GOOD_INPT) * 4) + ((goodId - GOOD_INPT) * 60) + 36, 3);
            } else if (buyRate > tcity.getGoodsTradeLvl().get(goodId)) {
                final Image downImg = new Image("http://static.eaw1805.com/images/tiles/MUIPopBuyDown.png");
                downImg.setHeight("23px");
                tCityRowPanel.add(downImg, 309 + ((goodId - GOOD_INPT) * 4) + ((goodId - GOOD_INPT) * 60) + 36, 3);
            }
        }
        return tCityRowPanel;
    }

    private native void loadImage(final Element obj, final String data) /*-{
        obj.src = data;
    }-*/;

    private native void logMessage(final String data) /*-{
        console.log(data);
    }-*/;
}
