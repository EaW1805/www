package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.EventHandler;

public interface MapUnitsLoadedHandler
        extends EventHandler {

    void onMapUnitsLoadedEvent(final MapUnitsLoadedEvent e);

}
