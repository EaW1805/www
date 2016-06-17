package com.eaw1805.www.client.events.movement;

import com.google.gwt.event.shared.GwtEvent;

public class MovementStopEvent
        extends GwtEvent<MovementStopHandler> {

    private static final Type<MovementStopHandler> TYPE = new Type<MovementStopHandler>();

    private final int infoType;

    private final int infoId;

    public MovementStopEvent(final int infoType, final int infoId) {
        this.infoType = infoType;
        this.infoId = infoId;
    }

    public static Type<MovementStopHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final MovementStopHandler handler) {
        handler.onMovementStop(this);
    }

    public Type<MovementStopHandler> getAssociatedType() {
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
