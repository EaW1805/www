package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;

public class RelationsLoadedEvent
        extends GwtEvent<RelationsLoadedHandler> {

    public static Type<RelationsLoadedHandler> TYPE = new Type<RelationsLoadedHandler>();

    public com.google.gwt.event.shared.GwtEvent.Type<RelationsLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(final RelationsLoadedHandler handler) {
        handler.onRelationsLoadedEvent(this);

    }

}
