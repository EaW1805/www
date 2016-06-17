package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.EventHandler;

public interface RegionLoadedHandler
        extends EventHandler {

    void onRegionLoaded(final RegionLoadedEvent event);

}
