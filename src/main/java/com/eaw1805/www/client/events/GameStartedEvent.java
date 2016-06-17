package com.eaw1805.www.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class GameStartedEvent
        extends GwtEvent<GameStartedHandler> {

    private static final Type<GameStartedHandler> TYPE = new Type<GameStartedHandler>();

    public static Type<GameStartedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final GameStartedHandler handler) {
        handler.onGameStarted(this);
    }

    public com.google.gwt.event.shared.GwtEvent.Type<GameStartedHandler> getAssociatedType() {
        return TYPE;
    }

}
