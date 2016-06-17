package com.eaw1805.www.client.events.map;

import com.google.gwt.event.shared.HandlerManager;

public class MapEventManager {

    static public HandlerManager handlerManager = new HandlerManager(null);

    public static void changeZoom(final double zoomLvl) {
        handlerManager.fireEvent(new ZoomEvent(zoomLvl));
    }

    public static void fireRegionChange(final int regionId) {
        handlerManager.fireEvent(new RegionChangedEvent(regionId));
    }

    public static void fireTileImagesLoaded(final int imagesType) {
        handlerManager.fireEvent(new TileImagesLoadedEvent(imagesType));
    }

    public static void addZoomHandler(final ZoomHandler handler) {
        handlerManager.addHandler(ZoomEvent.getType(), handler);
    }

    public static void addRegionChangedHandler(final RegionChangedHandler handler) {
        handlerManager.addHandler(RegionChangedEvent.getType(), handler);
    }

    public static void addTileImagesLoadedHandler(final TileImagesLoadedHandler handler) {
        handlerManager.addHandler(TileImagesLoadedEvent.getType(), handler);
    }

    public static void removeRegionChangedHandler(final RegionChangedHandler handler) {
        handlerManager.removeHandler(RegionChangedEvent.getType(), handler);
    }

}
