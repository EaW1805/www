package com.eaw1805.www.client.events.movement;

import com.google.gwt.event.shared.GwtEvent;

public class UndoMoveEvent
        extends GwtEvent<UndoMoveHandler> {

    private static final Type<UndoMoveHandler> TYPE = new Type<UndoMoveHandler>();

    private final int infoType;

    private final int infoId;

    public UndoMoveEvent(final int infoType, final int infoId) {
        this.infoType = infoType;
        this.infoId = infoId;
    }

    public static Type<UndoMoveHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final UndoMoveHandler handler) {
        handler.onUndoMove(this);
    }

    public Type<UndoMoveHandler> getAssociatedType() {
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
