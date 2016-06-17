package com.eaw1805.www.client.events.units;

import com.google.gwt.event.shared.GwtEvent;

public class UnitCreatedEvent
        extends GwtEvent<UnitCreatedHandler> {

    private static final Type<UnitCreatedHandler> TYPE = new Type<UnitCreatedHandler>();

    private final int infoType;

    private final int infoId;

    public UnitCreatedEvent(final int infoType, final int infoId) {
        this.infoType = infoType;
        this.infoId = infoId;
    }

    public static Type<UnitCreatedHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final UnitCreatedHandler handler) {
        handler.onUnitCreated(this);
    }

    public com.google.gwt.event.shared.GwtEvent.Type<UnitCreatedHandler> getAssociatedType() {
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

