package com.eaw1805.www.client.events;

import com.google.gwt.event.shared.HandlerManager;


public final class GameEventManager {

    static public HandlerManager handlerManager = new HandlerManager(null);

    public static void GameStarted() {
        handlerManager.fireEvent(new GameStartedEvent());
    }

    public static void addGameStartedHandler(final GameStartedHandler handler) {
        handlerManager.addHandler(GameStartedEvent.getType(), handler);
    }

    public static void reportInProcess(final boolean isInProcces) {
        handlerManager.fireEvent(new GameReportInProcessEvent(isInProcces));
    }

    public static void addGameReportInProccesHandler(final GameReportInProcessHandler handler) {
        handlerManager.addHandler(GameReportInProcessEvent.getType(), handler);
    }

    public static void reportProcessTime(final String procDate) {
        handlerManager.fireEvent(new GameProcessDateReportEvent(procDate));
    }

    public static void addGameProcessDateReportHandler(final GameProcessDateReportHandler handler) {
        handlerManager.addHandler(GameProcessDateReportEvent.getType(), handler);
    }

}
