package com.eaw1805.www.client.events.deploy;

import com.google.gwt.event.shared.GwtEvent;

public class DisembarkUnitEvent
        extends GwtEvent<DisembarkUnitHandler> {

    private final int cargoType;

    private static final Type<DisembarkUnitHandler> TYPE = new Type<DisembarkUnitHandler>();

    public DisembarkUnitEvent(final int cargoType) {
        super();
        this.cargoType = cargoType;
    }

    public static Type<DisembarkUnitHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final DisembarkUnitHandler handler) {
        handler.onDisembarkUnit(this);
    }

    public Type<DisembarkUnitHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the cargoType
     */
    public int getCargoType() {
        return cargoType;
    }

}
