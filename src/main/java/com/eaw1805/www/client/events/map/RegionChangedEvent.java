package com.eaw1805.www.client.events.map;

import com.google.gwt.event.shared.GwtEvent;

public class RegionChangedEvent
        extends GwtEvent<RegionChangedHandler> {

    private static final Type<RegionChangedHandler> TYPE = new Type<RegionChangedHandler>();

    private final int regionId;

    public RegionChangedEvent(final int regionId) {
        this.regionId = regionId;
    }

    public static Type<RegionChangedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final RegionChangedHandler handler) {
        handler.onRegionChanged(this);
    }

    public Type<RegionChangedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the zoomLvl
     */
    public int getRegionId() {
        return regionId;
    }

}

