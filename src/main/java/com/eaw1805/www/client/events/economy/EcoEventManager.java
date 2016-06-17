package com.eaw1805.www.client.events.economy;

import com.google.gwt.event.shared.HandlerManager;
import com.eaw1805.data.dto.web.ClientOrderDTO;

public class EcoEventManager {

    static public HandlerManager handlerManager = new HandlerManager(null);

    public static void changePrSite(final int region) {
        handlerManager.fireEvent(new PrSiteChangeEvent(region));
    }

    public static void addPrSiteChangeHandler(final PrSiteChangeHandler handler) {
        handlerManager.addHandler(PrSiteChangeEvent.getType(), handler);
    }

    public static void changeSector(final int region) {
        handlerManager.fireEvent(new SectorChangeEvent(region));
    }

    public static void addSectorChangeHandler(final SectorChangeHandler handler) {
        handlerManager.addHandler(SectorChangeEvent.getType(), handler);
    }

    public static void addOrder(final ClientOrderDTO order) {
        handlerManager.fireEvent(new OrderAddedEvent(order));
    }

    public static void addOrderAddedHandler(final OrderAddedHandler handler) {
        handlerManager.addHandler(OrderAddedEvent.getType(), handler);
    }

    public static void removeOrder(final ClientOrderDTO order) {
        handlerManager.fireEvent(new OrderRemovedEvent(order));
    }

    public static void addOrderRemovedHandler(final OrderRemovedHandler handler) {
        handlerManager.addHandler(OrderRemovedEvent.getType(), handler);
    }

    public static void removeOrderAddedHandler(final OrderAddedHandler handler) {
        handlerManager.removeHandler(OrderAddedEvent.getType(), handler);
    }
}
