package com.eaw1805.www.client.events.map;

import com.google.gwt.event.shared.EventHandler;

public interface ZoomHandler
        extends EventHandler {

    void onZoom(final ZoomEvent event);

}
