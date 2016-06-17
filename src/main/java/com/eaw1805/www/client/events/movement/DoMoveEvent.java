package com.eaw1805.www.client.events.movement;

import com.google.gwt.event.shared.GwtEvent;

public class DoMoveEvent
        extends GwtEvent<DoMoveHandler> {

    private static final Type<DoMoveHandler> TYPE = new Type<DoMoveHandler>();

    private final int infoType;

    private final int infoId;

    public DoMoveEvent(final int infoType, final int infoId) {
        this.infoType = infoType;
        this.infoId = infoId;
    }

    public static Type<DoMoveHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final DoMoveHandler handler) {
        handler.onDoMove(this);
    }

    public Type<DoMoveHandler> getAssociatedType() {
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
