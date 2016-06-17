package com.eaw1805.www.client.events.economy;

import com.google.gwt.event.shared.GwtEvent;

public class SectorChangeEvent
        extends GwtEvent<SectorChangeHandler> {

    private static final Type<SectorChangeHandler> TYPE = new Type<SectorChangeHandler>();

    private final int region;

    public SectorChangeEvent(final int region) {
        this.region = region;
    }

    public static Type<SectorChangeHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final SectorChangeHandler handler) {
        handler.onSectorChange(this);
    }

    public Type<SectorChangeHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the SectorChangeLvl
     */
    public int getRegion() {
        return region;
    }

}
