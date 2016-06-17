package com.eaw1805.www.client.events.economy;

import com.google.gwt.event.shared.EventHandler;

public interface OrderRemovedHandler
        extends EventHandler {

    void onOrderRemoved(final OrderRemovedEvent event);

}
