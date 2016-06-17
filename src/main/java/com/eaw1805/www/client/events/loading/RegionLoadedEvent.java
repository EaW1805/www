package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;

public class RegionLoadedEvent
        extends GwtEvent<RegionLoadedHandler> {

    private static final Type<RegionLoadedHandler> TYPE = new Type<RegionLoadedHandler>();

    public static Type<RegionLoadedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final RegionLoadedHandler handler) {
        handler.onRegionLoaded(this);
    }

    public Type<RegionLoadedHandler> getAssociatedType() {
        return TYPE;
    }

}
