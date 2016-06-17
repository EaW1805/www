package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;
import com.eaw1805.www.shared.ForeignUnits;

/**
 * Created by IntelliJ IDEA.
 * User: karavias
 * Date: 3/7/12
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForeignUnitsLoadedEvent
        extends GwtEvent<ForeignUnitsLoadedHandler> {

    private final int infoType;

    private final ForeignUnits foreignUnits;

    private static final Type<ForeignUnitsLoadedHandler> TYPE = new Type<ForeignUnitsLoadedHandler>();

    public ForeignUnitsLoadedEvent(final int infoType,
                                   final ForeignUnits foreignUnits) {
        this.infoType = infoType;
        this.foreignUnits = foreignUnits;
    }

    public static Type<ForeignUnitsLoadedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final ForeignUnitsLoadedHandler handler) {
        handler.onForeignUnitsLoaded(this);
    }

    public Type<ForeignUnitsLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the alliedUnits
     */
    public ForeignUnits getForeignUnits() {
        return foreignUnits;
    }

    /**
     * @return the infoType
     */
    public int getInfoType() {
        return infoType;
    }


}
