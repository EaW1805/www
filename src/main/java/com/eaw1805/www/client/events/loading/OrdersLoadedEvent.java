package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by IntelliJ IDEA.
 * User: karavias
 * Date: 3/10/12
 * Time: 11:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrdersLoadedEvent
        extends GwtEvent<OrdersLoadedHandler> {

    private static final Type<OrdersLoadedHandler> TYPE = new Type<OrdersLoadedHandler>();

    public OrdersLoadedEvent() {
    }

    public static Type<OrdersLoadedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final OrdersLoadedHandler handler) {
        handler.onOrdersLoaded(this);
    }

    public Type<OrdersLoadedHandler> getAssociatedType() {
        return TYPE;
    }


}
