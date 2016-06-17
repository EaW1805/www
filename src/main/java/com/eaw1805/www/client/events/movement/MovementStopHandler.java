package com.eaw1805.www.client.events.movement;

import com.google.gwt.event.shared.EventHandler;


public interface MovementStopHandler
        extends EventHandler {

    void onMovementStop(final MovementStopEvent event);

}

