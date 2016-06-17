package com.eaw1805.www.client.events.trade;

import com.google.gwt.event.shared.HandlerManager;


public final class TradeEventManager {
    static public HandlerManager handlerManager = new HandlerManager(null);

    public static void giveGood(final int unitId, final int goodId, final int unitType, final int qte, final int regionId) {
        handlerManager.fireEvent(new GiveGoodEvent(unitId, goodId, unitType, qte, regionId));
    }

    public static void getGood(final int unitId, final int goodId, final int unitType, final int qte, final int regionId) {
        handlerManager.fireEvent(new GetGoodEvent(unitId, goodId, unitType, qte, regionId));
    }

    public static void addGiveGoodHanlder(final GiveGoodHandler handler) {
        handlerManager.addHandler(GiveGoodEvent.getType(), handler);
    }

    public static void addGetGoodHanlder(final GetGoodHandler handler) {
        handlerManager.addHandler(GetGoodEvent.getType(), handler);
    }
}
