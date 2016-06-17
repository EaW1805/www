package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.EventHandler;


public interface OrdersLoadedHandler
        extends EventHandler {

    void onOrdersLoaded(final OrdersLoadedEvent event);

}
