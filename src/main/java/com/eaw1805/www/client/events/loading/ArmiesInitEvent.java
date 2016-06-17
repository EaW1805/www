package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;

public class ArmiesInitEvent
        extends GwtEvent<ArmiesInitdHandler> {

    public static final Type<ArmiesInitdHandler> TYPE = new Type<ArmiesInitdHandler>();

    public com.google.gwt.event.shared.GwtEvent.Type<ArmiesInitdHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(final ArmiesInitdHandler handler) {
        handler.onArmiesInit(this);
    }

}
