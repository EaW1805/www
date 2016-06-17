package com.eaw1805.www.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class GameReportInProcessEvent
        extends GwtEvent<GameReportInProcessHandler> {

    private final Boolean inProcces;

    private static final Type<GameReportInProcessHandler> TYPE = new Type<GameReportInProcessHandler>();

    /**
     * Constructor
     *
     * @param inProcces the state of the event
     */
    public GameReportInProcessEvent(final Boolean inProcces) {
        super();
        this.inProcces = inProcces;
    }

    public static Type<GameReportInProcessHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final GameReportInProcessHandler handler) {
        handler.onGameInProcces(this);
    }

    public Type<GameReportInProcessHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the inProcces
     */
    public Boolean getInProcces() {
        return inProcces;
    }


}
