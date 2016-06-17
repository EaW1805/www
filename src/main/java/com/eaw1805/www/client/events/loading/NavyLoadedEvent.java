package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;
import com.eaw1805.data.dto.web.fleet.FleetDTO;

import java.util.List;

public class NavyLoadedEvent
        extends GwtEvent<NavyLoadedHandler> {

    private static final Type<NavyLoadedHandler> TYPE = new Type<NavyLoadedHandler>();

    private final int infoType;

    private final List<FleetDTO> fleets;

    public NavyLoadedEvent(final int infoType, final List<FleetDTO> fleets) {
        this.infoType = infoType;
        this.fleets = fleets;
    }

    public static Type<NavyLoadedHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final NavyLoadedHandler handler) {
        handler.onNavyLoaded(this);
    }

    public Type<NavyLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the infoType
     */
    public int getInfoType() {
        return infoType;
    }

    /**
     * @return the fleets
     */
    public List<FleetDTO> getFleets() {
        return fleets;
    }

}
