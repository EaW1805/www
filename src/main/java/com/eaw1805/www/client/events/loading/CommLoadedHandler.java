package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.EventHandler;

public interface CommLoadedHandler
        extends EventHandler {

    void onCommLoaded(final CommLoadedEvent event);

}
