package com.eaw1805.www.client.views;

import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.client.events.map.MapEventManager;
import com.eaw1805.www.client.events.map.RegionChangedEvent;
import com.eaw1805.www.client.events.map.RegionChangedHandler;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.map.MapStore;

public class LoadingGraphicsPanel extends AbsolutePanel {

    final MapStore mapStore = MapStore.getInstance();
    Image mapImage;
    Timer t = new Timer() {
        @Override
        public void run() {
            setVisible(true);
        }
    };
    public LoadingGraphicsPanel() {
        setStyleName("popUpPanel");
        setStyleName("disablePointerEvents", true);
        getElement().getStyle().setZIndex(100000000);
        setSize("300px", "200px");
        final Label loadingLbl = new Label("Downloading Graphics...");
        loadingLbl.setStyleName("clearFontMiniTitle whiteText");
        add(loadingLbl, 9, 12);
        mapImage = new Image(mapStore.getBaseMapImageByRegion(mapStore.getActiveRegion()).getHref());
        mapImage.addLoadHandler(new LoadHandler() {
            @Override
            public void onLoad(LoadEvent loadEvent) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanel(LoadingGraphicsPanel.this);
            }
        });
        mapImage.setSize("286px", "143px");
        add(mapImage, 7, 53);

        MapEventManager.addRegionChangedHandler(new RegionChangedHandler() {
            @Override
            public void onRegionChanged(RegionChangedEvent event) {
                update();
            }
        });

    }

    public void onAttach() {
        super.onAttach();
        setVisible(false);
        try {
            t.cancel();
        } catch (Exception e) {
            //eat it
        }
        t.schedule(2000);
    }

    public void update() {
        remove(mapImage);
        GameStore.getInstance().getLayoutView().addWidgetToPanel(LoadingGraphicsPanel.this, Window.getClientWidth() - 300, 50);
        mapImage = new Image(mapStore.getBaseMapImageByRegion(mapStore.getActiveRegion()).getHref());
        mapImage.addLoadHandler(new LoadHandler() {
            @Override
            public void onLoad(LoadEvent loadEvent) {


                GameStore.getInstance().getLayoutView().removeWidgetFromPanel(LoadingGraphicsPanel.this);
            }
        });
        mapImage.setSize("286px", "143px");
        add(mapImage, 7, 53);
    }
}
