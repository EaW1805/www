package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;

public class AllUnitsLoadedEvent
        extends GwtEvent<AllUnitsLoadedHandler> {

    static final Type<AllUnitsLoadedHandler> TYPE = new Type<AllUnitsLoadedHandler>();

    public AllUnitsLoadedEvent() {
        super();
    }

    public Type<AllUnitsLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(final AllUnitsLoadedHandler handler) {
        handler.onAllLoaded(this);

    }

}
