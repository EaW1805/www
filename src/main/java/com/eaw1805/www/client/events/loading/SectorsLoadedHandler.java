package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.EventHandler;

public interface SectorsLoadedHandler
        extends EventHandler {

    void onSectorsLoaded(final SectorsLoadedEvent event);

}
