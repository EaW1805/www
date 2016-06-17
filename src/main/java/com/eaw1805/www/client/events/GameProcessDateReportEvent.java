package com.eaw1805.www.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class GameProcessDateReportEvent
        extends GwtEvent<GameProcessDateReportHandler> {

    private final String procDate;

    private static final Type<GameProcessDateReportHandler> TYPE = new Type<GameProcessDateReportHandler>();

    /**
     * Constructor
     *
     * @param procDate the state of the event
     */
    public GameProcessDateReportEvent(final String procDate) {
        super();
        this.procDate = procDate;
    }

    public static Type<GameProcessDateReportHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final GameProcessDateReportHandler handler) {
        handler.onGameProccesDateReport(this);
    }

    public Type<GameProcessDateReportHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the inProcces
     */
    public String getProcDate() {
        return procDate;
    }


}
