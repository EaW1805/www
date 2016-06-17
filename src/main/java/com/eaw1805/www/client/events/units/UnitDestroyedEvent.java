package com.eaw1805.www.client.events.units;

import com.google.gwt.event.shared.GwtEvent;

public class UnitDestroyedEvent
        extends GwtEvent<UnitDestroyedHandler> {

    private static final Type<UnitDestroyedHandler> TYPE = new Type<UnitDestroyedHandler>();

    private final int infoType;

    private final int infoId;

    public UnitDestroyedEvent(final int infoType, final int infoId) {
        this.infoType = infoType;
        this.infoId = infoId;
    }

    public static Type<UnitDestroyedHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final UnitDestroyedHandler handler) {
        handler.onUnitDestroyed(this);
    }

    public com.google.gwt.event.shared.GwtEvent.Type<UnitDestroyedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the infoType
     */
    public int getInfoType() {
        return infoType;
    }

    /**
     * @return the infoId
     */
    public int getInfoId() {
        return infoId;
    }
}
