package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;
import com.eaw1805.www.shared.AlliedUnits;

import java.util.Map;

public class AlliedUnitsLoadedEvent
        extends GwtEvent<AlliedUnitsLoadedHandler> {

    private final int infoType;
    private final Map<Integer, Map<Integer, AlliedUnits>> alliedUnits;
    private static final Type<AlliedUnitsLoadedHandler> TYPE = new Type<AlliedUnitsLoadedHandler>();

    public AlliedUnitsLoadedEvent(final int infoType,
                                  final Map<Integer, Map<Integer, AlliedUnits>> alliedUnits) {
        this.infoType = infoType;
        this.alliedUnits = alliedUnits;
    }

    public static Type<AlliedUnitsLoadedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final AlliedUnitsLoadedHandler handler) {
        handler.onAlliedUnitsLoaded(this);

    }

    public Type<AlliedUnitsLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the alliedUnits
     */
    public Map<Integer, Map<Integer, AlliedUnits>> getAlliedUnits() {
        return alliedUnits;
    }

    /**
     * @return the infoType
     */
    public int getInfoType() {
        return infoType;
    }


}
