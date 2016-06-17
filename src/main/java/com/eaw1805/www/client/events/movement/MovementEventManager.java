package com.eaw1805.www.client.events.movement;

import com.google.gwt.event.shared.HandlerManager;
import com.eaw1805.data.dto.common.SectorDTO;

public class MovementEventManager {
    static public HandlerManager handlerManager = new HandlerManager(null);

    public static void initMovement(final int infoType, final int infoId) {
        handlerManager.fireEvent(new MovementInitEvent(infoType, infoId));
    }

    public static void doMovement(final int unitType, final int unitId) {
        handlerManager.fireEvent(new DoMoveEvent(unitType, unitId));
    }

    public static void unDoMovement(final int unitType, final int unitId) {
        handlerManager.fireEvent(new UndoMoveEvent(unitType, unitId));
    }

    public static void stopMovement(final int infoType, final int infoId) {
        handlerManager.fireEvent(new MovementStopEvent(infoType, infoId));
    }

    public static void startAllyMovement(final int infoType, final int infoId, final int nationId,
                                         final SectorDTO startSector) {
        handlerManager.fireEvent(new StartAllyMoveEvent(infoType, infoId, nationId, startSector));
    }

    public static void stopAllyMovement(final int infoType, final int infoId) {
        handlerManager.fireEvent(new StopAllyMoveEvent(infoType, infoId));
    }

    public static void addStartAllyMoveHandler(final StartAllyMoveHandler handler) {
        handlerManager.addHandler(StartAllyMoveEvent.getType(), handler);
    }

    public static void addStopAllyMoveHandler(final StopAllyMoveHandler handler) {
        handlerManager.addHandler(StopAllyMoveEvent.getType(), handler);
    }

    public static void addDoMoveHandler(final DoMoveHandler handler) {
        handlerManager.addHandler(DoMoveEvent.getType(), handler);
    }

    public static void addUndoMoveHandler(final UndoMoveHandler handler) {
        handlerManager.addHandler(UndoMoveEvent.getType(), handler);
    }

    public static void addMovementInitHandler(final MovementInitHandler handler) {
        handlerManager.addHandler(MovementInitEvent.getType(), handler);
    }

    public static void addMovementStopHandler(final MovementStopHandler handler) {
        handlerManager.addHandler(MovementStopEvent.getType(), handler);
    }

}
