package com.eaw1805.www.client.events.movement;

import com.google.gwt.event.shared.EventHandler;

public interface UndoMoveHandler
        extends EventHandler {

    void onUndoMove(final UndoMoveEvent event);

}
