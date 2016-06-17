package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.EventHandler;

public interface AllUnitsLoadedHandler
        extends EventHandler {

    void onAllLoaded(AllUnitsLoadedEvent event);

}
