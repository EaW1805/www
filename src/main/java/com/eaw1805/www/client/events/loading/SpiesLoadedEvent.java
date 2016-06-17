package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;
import com.eaw1805.data.dto.web.army.SpyDTO;

import java.util.List;

public class SpiesLoadedEvent
        extends GwtEvent<SpiesLoadedHandler> {

    private static final Type<SpiesLoadedHandler> TYPE = new Type<SpiesLoadedHandler>();

    private final int infoType;

    private final List<SpyDTO> spies;

    public SpiesLoadedEvent(final int infoType, final List<SpyDTO> spies) {
        this.infoType = infoType;
        this.spies = spies;
    }

    public static Type<SpiesLoadedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final SpiesLoadedHandler handler) {
        handler.onSpiesLoaded(this);
    }

    public Type<SpiesLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the infoType
     */
    public int getInfoType() {
        return infoType;
    }

    /**
     * @return the spies
     */
    public List<SpyDTO> getSpies() {
        return spies;
    }

}
