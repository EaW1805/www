package com.eaw1805.www.client.events.units;

import com.google.gwt.event.shared.EventHandler;

public interface UnitCreatedHandler
        extends EventHandler {

    void onUnitCreated(final UnitCreatedEvent event);

}
