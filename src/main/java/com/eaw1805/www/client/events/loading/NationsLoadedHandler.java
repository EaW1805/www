package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.EventHandler;

public interface NationsLoadedHandler
        extends EventHandler {

    void onNationsLoaded(final NationsLoadedEvent event);

}
