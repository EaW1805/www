package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;

public class AppInitEvent
        extends GwtEvent<AppInitHandler> {

    static final Type<AppInitHandler> TYPE = new Type<AppInitHandler>();

    public com.google.gwt.event.shared.GwtEvent.Type<AppInitHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(final AppInitHandler handler) {
        handler.onApplicationInit(this);

    }

}
