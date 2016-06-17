package com.eaw1805.www.client.events.units;

import com.google.gwt.event.shared.GwtEvent;

public class UnitUndoSelectedEvent
        extends GwtEvent<UnitUndoSelectedHandler> {

    private static final Type<UnitUndoSelectedHandler> TYPE = new Type<UnitUndoSelectedHandler>();

    public static Type<UnitUndoSelectedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final UnitUndoSelectedHandler handler) {
        handler.onUnitUndoSelected(this);
    }

    public com.google.gwt.event.shared.GwtEvent.Type<UnitUndoSelectedHandler> getAssociatedType() {
        return TYPE;
    }

}
