package com.eaw1805.www.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface GameStartedHandler
        extends EventHandler {

    void onGameStarted(final GameStartedEvent event);

}
