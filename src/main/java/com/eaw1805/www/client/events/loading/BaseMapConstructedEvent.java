package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;

public class BaseMapConstructedEvent
        extends GwtEvent<BaseMapConstructedHandler> {

    public static final Type<BaseMapConstructedHandler> TYPE = new Type<BaseMapConstructedHandler>();

    public com.google.gwt.event.shared.GwtEvent.Type<BaseMapConstructedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(final BaseMapConstructedHandler handler) {
        handler.onBaseMapConstructed(this);
    }

}
