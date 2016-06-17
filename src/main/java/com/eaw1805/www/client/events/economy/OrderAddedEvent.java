package com.eaw1805.www.client.events.economy;

import com.google.gwt.event.shared.GwtEvent;
import com.eaw1805.data.dto.web.ClientOrderDTO;

public class OrderAddedEvent
        extends GwtEvent<OrderAddedHandler> {

    private static final Type<OrderAddedHandler> TYPE = new Type<OrderAddedHandler>();

    private final ClientOrderDTO clientOrder;

    public OrderAddedEvent(final ClientOrderDTO order) {
        this.clientOrder = order;
    }

    public static Type<OrderAddedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final OrderAddedHandler handler) {
        handler.onOrderAdded(this);
    }

    public Type<OrderAddedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the PrSiteChangeLvl
     */
    public ClientOrderDTO getClientOrder() {
        return clientOrder;
    }

}
