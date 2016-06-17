package com.eaw1805.www.client.events.units;

import com.google.gwt.event.shared.EventHandler;

public interface UnitChangedHandler
        extends EventHandler {

    void onUnitChanged(final UnitChangedEvent event);

}
