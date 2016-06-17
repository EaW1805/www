package com.eaw1805.www.client.events.tutorial;

import com.google.gwt.event.shared.GwtEvent;

public class PositionClickedEvent extends GwtEvent<PositionClickedHandler> {
    private static final GwtEvent.Type<PositionClickedHandler> TYPE = new GwtEvent.Type<PositionClickedHandler>();

    private final int x;
    private final int y;

    public PositionClickedEvent(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public static GwtEvent.Type<PositionClickedHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final PositionClickedHandler handler) {
        handler.onUnitChanged(this);
    }

    public com.google.gwt.event.shared.GwtEvent.Type<PositionClickedHandler> getAssociatedType() {
        return TYPE;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
