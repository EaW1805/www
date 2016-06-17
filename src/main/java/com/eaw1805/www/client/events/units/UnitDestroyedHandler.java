package com.eaw1805.www.client.events.units;

import com.google.gwt.event.shared.EventHandler;

public interface UnitDestroyedHandler
        extends EventHandler {

    void onUnitDestroyed(final UnitDestroyedEvent event);

}
