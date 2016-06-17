package com.eaw1805.www.fieldbattle.events.loading;

import com.google.gwt.event.shared.EventHandler;

public interface CommandersLoadedHandler  extends EventHandler {
    void onUnitChanged(final CommandersLoadedEvent event);


}
