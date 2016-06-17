package com.eaw1805.www.client.events.economy;

import com.google.gwt.event.shared.GwtEvent;
import com.eaw1805.data.dto.web.ClientOrderDTO;


public class OrderRemovedEvent
        extends GwtEvent<OrderRemovedHandler> {

    private static final Type<OrderRemovedHandler> TYPE = new Type<OrderRemovedHandler>();

    private final ClientOrderDTO clientOrder;

    public OrderRemovedEvent(final ClientOrderDTO order) {
        this.clientOrder = order;
    }

    public static Type<OrderRemovedHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final OrderRemovedHandler handler) {
        handler.onOrderRemoved(this);
    }

    public Type<OrderRemovedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the PrSiteChangeLvl
     */
    public ClientOrderDTO getClientOrder() {
        return clientOrder;
    }

}
