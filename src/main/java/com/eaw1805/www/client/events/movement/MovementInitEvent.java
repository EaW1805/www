package com.eaw1805.www.client.events.movement;

import com.google.gwt.event.shared.GwtEvent;

public class MovementInitEvent
        extends GwtEvent<MovementInitHandler> {

    private static final Type<MovementInitHandler> TYPE = new Type<MovementInitHandler>();

    private final int infoType;

    private final int infoId;

    public MovementInitEvent(final int infoType, final int infoId) {
        this.infoType = infoType;
        this.infoId = infoId;
    }

    public static Type<MovementInitHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final MovementInitHandler handler) {
        handler.onMovementInit(this);
    }

    public Type<MovementInitHandler> getAssociatedType() {
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
