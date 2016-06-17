package com.eaw1805.www.client.events.economy;

import com.google.gwt.event.shared.EventHandler;

public interface SectorChangeHandler
        extends EventHandler {

    void onSectorChange(final SectorChangeEvent event);

}
