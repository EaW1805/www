package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;
import com.eaw1805.data.dto.web.army.ArmyDTO;

import java.util.Collection;

public class ArmiesLoadedEvent
        extends GwtEvent<ArmiesLoadedHandler> {

    private static final Type<ArmiesLoadedHandler> TYPE = new Type<ArmiesLoadedHandler>();

    private final int infoType;

    private final Collection<ArmyDTO> armies;

    public ArmiesLoadedEvent(final int infoType, Collection<ArmyDTO> armies) {
        this.infoType = infoType;
        this.armies = armies;
    }

    public static Type<ArmiesLoadedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final ArmiesLoadedHandler handler) {
        handler.onArmiesLoaded(this);
    }

    public Type<ArmiesLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the infoType
     */
    public int getInfoType() {
        return infoType;
    }

    /**
     * @return the armies
     */
    public Collection<ArmyDTO> getArmies() {
        return armies;
    }
}
