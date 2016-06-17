package com.eaw1805.www.client.events.economy;

import com.google.gwt.event.shared.EventHandler;

public interface OrderAddedHandler
        extends EventHandler {

    void onOrderAdded(final OrderAddedEvent event);

}
