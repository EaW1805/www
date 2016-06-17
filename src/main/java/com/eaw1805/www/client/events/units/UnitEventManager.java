package com.eaw1805.www.client.events.units;

import com.google.gwt.event.shared.HandlerManager;

public final class UnitEventManager {

    static public HandlerManager handlerManager = new HandlerManager(null);

    public static void doSelection(final int infoType, final int infoId, final int nationId, final int sectorId) {
        handlerManager.fireEvent(new UnitSelectedEvent(infoType, infoId, nationId, sectorId));
    }

    public static void undoSelection() {
        handlerManager.fireEvent(new UnitUndoSelectedEvent());

    }

    public static void changeUnit(final int infoType, final int infoId) {
//        final int numOfHandlers = handlerManager.getHandlerCount(UnitChangedEvent.getType());
//        final Map<Boolean, Integer> fakeMap = new HashMap<Boolean, Integer>();
//        //I want a local variable to store an integer... but i want it final to be used inside the timer...
//        fakeMap.put(true, 0);
//        new Timer() {
//            @Override
//            public void run() {
//                if (fakeMap.get(true) < numOfHandlers) {
//                    final UnitChangedHandler handler = handlerManager.getHandler(UnitChangedEvent.getType(), fakeMap.get(true));
//                    handler.onUnitChanged(new UnitChangedEvent(infoType, infoId));
//                    fakeMap.put(true, fakeMap.get(true) + 1);
//                    schedule(10);
//                }
//            }
//        }.run();
//        GameStore.getInstance().getLayoutView().getSpeedTest().addMeasure(".|." + handlerManager.getHandlerCount(UnitChangedEvent.getType()));
        handlerManager.fireEvent(new UnitChangedEvent(infoType, infoId));
    }

    public static void createUnit(final int infoType, final int infoId) {
        handlerManager.fireEvent(new UnitCreatedEvent(infoType, infoId));

    }

    public static void destroyUnit(final int infoType, final int infoId) {
        handlerManager.fireEvent(new UnitDestroyedEvent(infoType, infoId));

    }

    public static void addUnitSelectedHandler(final UnitSelectedHandler handler) {
        handlerManager.addHandler(UnitSelectedEvent.getType(), handler);
    }

    public static void addUnitUndoSelectedHandler(final UnitUndoSelectedHandler handler) {
        handlerManager.addHandler(UnitUndoSelectedEvent.getType(), handler);
    }

    public static void addUnitChangedHandler(final UnitChangedHandler handler) {
        handlerManager.addHandler(UnitChangedEvent.getType(), handler);
    }

    public static void addUnitCreatedHandler(final UnitCreatedHandler handler) {
        handlerManager.addHandler(UnitCreatedEvent.getType(), handler);
    }

    public static void addUnitDestroyedHandler(final UnitDestroyedHandler handler) {
        handlerManager.addHandler(UnitDestroyedEvent.getType(), handler);
    }

    public static void removeUnitDestroyedHandler(final UnitDestroyedHandler handler) {
        handlerManager.removeHandler(UnitDestroyedEvent.getType(), handler);
    }

    public static void removeUnitChangedHandler(final UnitChangedHandler handler) {
        handlerManager.removeHandler(UnitChangedEvent.getType(), handler);
    }

    public static void removeUnitCreatedHandler(final UnitCreatedHandler handler) {
        handlerManager.removeHandler(UnitCreatedEvent.getType(), handler);
    }

}
