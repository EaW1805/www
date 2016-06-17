package com.eaw1805.www.client.events.units;

import com.google.gwt.event.shared.GwtEvent;

public class UnitChangedEvent
        extends GwtEvent<UnitChangedHandler> {

    private static final Type<UnitChangedHandler> TYPE = new Type<UnitChangedHandler>();

    private final int infoType;

    private final int infoId;

    public UnitChangedEvent(final int infoType, final int infoId) {
        this.infoType = infoType;
        this.infoId = infoId;
    }

    public static Type<UnitChangedHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final UnitChangedHandler handler) {
        handler.onUnitChanged(this);
    }

    public com.google.gwt.event.shared.GwtEvent.Type<UnitChangedHandler> getAssociatedType() {
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
