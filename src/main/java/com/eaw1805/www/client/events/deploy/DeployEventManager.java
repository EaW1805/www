package com.eaw1805.www.client.events.deploy;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public class DeployEventManager {

    static public HandlerManager handlerManager = new HandlerManager(null);

    public static void reportEmbark(final int transportType, final int transportId, final int cargoType, final int cargoId) {
        handlerManager.fireEvent(new EmbarkUnitEvent(cargoType));
    }

    public static HandlerRegistration addEmbarkUnitHandler(final EmbarkUnitHandler handler) {
        return handlerManager.addHandler(EmbarkUnitEvent.getType(), handler);
    }

    public static void reportDisembark(final int transportType, final int transportId, final int cargoType, final int cargoId) {
        handlerManager.fireEvent(new DisembarkUnitEvent(cargoType));
    }

    public static void addDisembarkUnitHandler(final DisembarkUnitHandler handler) {
        handlerManager.addHandler(DisembarkUnitEvent.getType(), handler);
    }

}
