package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.EventHandler;

public interface BtrainLoadedHandler
        extends EventHandler {

    void onBtrainLoaded(final BtrainLoadedEvent event);

}
