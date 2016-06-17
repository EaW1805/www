package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;

public class ProSiteLoadedEvent
        extends GwtEvent<ProSiteLoadedHandler> {

    private static final Type<ProSiteLoadedHandler> TYPE = new Type<ProSiteLoadedHandler>();

    public ProSiteLoadedEvent() {
        // empty constructor
    }

    public static Type<ProSiteLoadedHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final ProSiteLoadedHandler handler) {
        handler.onProSiteLoaded(this);
    }

    public Type<ProSiteLoadedHandler> getAssociatedType() {
        return TYPE;
    }

}
