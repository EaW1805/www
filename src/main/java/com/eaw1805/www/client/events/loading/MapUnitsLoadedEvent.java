package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;

public class MapUnitsLoadedEvent
        extends GwtEvent<MapUnitsLoadedHandler> {

    public static Type<MapUnitsLoadedHandler> TYPE = new Type<MapUnitsLoadedHandler>();

    public com.google.gwt.event.shared.GwtEvent.Type<MapUnitsLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(final MapUnitsLoadedHandler handler) {
        handler.onMapUnitsLoadedEvent(this);

    }

}
