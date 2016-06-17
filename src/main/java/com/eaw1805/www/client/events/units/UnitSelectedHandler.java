package com.eaw1805.www.client.events.units;

import com.google.gwt.event.shared.EventHandler;

public interface UnitSelectedHandler
        extends EventHandler {

    void onUnitSelected(final UnitSelectedEvent event);

}
