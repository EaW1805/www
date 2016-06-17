package com.eaw1805.www.client.events.movement;

import com.google.gwt.event.shared.GwtEvent;

public class StopAllyMoveEvent
        extends GwtEvent<StopAllyMoveHandler> {

    private static final Type<StopAllyMoveHandler> TYPE = new Type<StopAllyMoveHandler>();

    private final int infoType;

    private final int infoId;

    public StopAllyMoveEvent(final int infoType, final int infoId) {
        this.infoType = infoType;
        this.infoId = infoId;
    }

    public static Type<StopAllyMoveHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final StopAllyMoveHandler handler) {
        handler.onStopAllyMove(this);
    }

    public Type<StopAllyMoveHandler> getAssociatedType() {
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
