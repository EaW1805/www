package com.eaw1805.www.client.events.deploy;

import com.google.gwt.event.shared.GwtEvent;


public class EmbarkUnitEvent
        extends GwtEvent<EmbarkUnitHandler> {

    private final int cargoType;

    private static final Type<EmbarkUnitHandler> TYPE = new Type<EmbarkUnitHandler>();

    public EmbarkUnitEvent(final int cargoType) {
        super();
        this.cargoType = cargoType;
    }

    public static com.google.gwt.event.shared.GwtEvent.Type<EmbarkUnitHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final EmbarkUnitHandler handler) {
        handler.onEmbarkUnit(this);
    }

    public Type<EmbarkUnitHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the cargoType
     */
    public int getCargoType() {
        return cargoType;
    }

}
