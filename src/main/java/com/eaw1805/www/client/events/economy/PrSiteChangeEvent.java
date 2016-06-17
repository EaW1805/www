package com.eaw1805.www.client.events.economy;

import com.google.gwt.event.shared.GwtEvent;

public class PrSiteChangeEvent
        extends GwtEvent<PrSiteChangeHandler> {

    private static final Type<PrSiteChangeHandler> TYPE = new Type<PrSiteChangeHandler>();

    private final int region;

    public PrSiteChangeEvent(final int region) {
        this.region = region;
    }

    public static Type<PrSiteChangeHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final PrSiteChangeHandler handler) {
        handler.onPrSiteChange(this);
    }

    public Type<PrSiteChangeHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the PrSiteChangeLvl
     */
    public int getRegion() {
        return region;
    }

}
