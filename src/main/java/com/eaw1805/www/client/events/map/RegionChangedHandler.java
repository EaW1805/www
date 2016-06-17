package com.eaw1805.www.client.events.map;

import com.google.gwt.event.shared.EventHandler;

public interface RegionChangedHandler
        extends EventHandler {

    void onRegionChanged(final RegionChangedEvent event);

}
