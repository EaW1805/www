package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.EventHandler;


public interface ArmiesLoadedHandler
        extends EventHandler {

    void onArmiesLoaded(final ArmiesLoadedEvent event);

}
