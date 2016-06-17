package com.eaw1805.www.client.events.map;

import com.google.gwt.event.shared.GwtEvent;

public class ZoomEvent
        extends GwtEvent<ZoomHandler> {

    private static final Type<ZoomHandler> TYPE = new Type<ZoomHandler>();

    private final double zoomLvl;

    public ZoomEvent(final double zoomLvl) {
        this.zoomLvl = zoomLvl;
    }

    public static Type<ZoomHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final ZoomHandler handler) {
        handler.onZoom(this);
    }

    public Type<ZoomHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the zoomLvl
     */
    public double getZoomLvl() {
        return zoomLvl;
    }

}
