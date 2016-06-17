package com.eaw1805.www.client.events.map;

import com.google.gwt.event.shared.GwtEvent;

public class TileImagesLoadedEvent
        extends GwtEvent<TileImagesLoadedHandler> {

    private static final Type<TileImagesLoadedHandler> TYPE = new Type<TileImagesLoadedHandler>();

    private final int imagesType;

    public TileImagesLoadedEvent(final int imagesType) {
        this.imagesType = imagesType;
    }

    public static Type<TileImagesLoadedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final TileImagesLoadedHandler handler) {
        handler.onTileImagesLoaded(this);
    }

    public Type<TileImagesLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the zoomLvl
     */
    public int getImagesType() {
        return imagesType;
    }

}
