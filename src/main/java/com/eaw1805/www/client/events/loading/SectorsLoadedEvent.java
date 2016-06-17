package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;

public class SectorsLoadedEvent
        extends GwtEvent<SectorsLoadedHandler> {

    private static final Type<SectorsLoadedHandler> TYPE = new Type<SectorsLoadedHandler>();

    public static Type<SectorsLoadedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final SectorsLoadedHandler handler) {
        handler.onSectorsLoaded(this);
    }

    public Type<SectorsLoadedHandler> getAssociatedType() {
        return TYPE;
    }

}
