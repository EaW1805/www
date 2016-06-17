package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;
import com.eaw1805.data.dto.web.army.CommanderDTO;

import java.util.List;

public class CommLoadedEvent
        extends GwtEvent<CommLoadedHandler> {

    private static final Type<CommLoadedHandler> TYPE = new Type<CommLoadedHandler>();

    private final int infoType;

    private final List<CommanderDTO> commanders;

    public CommLoadedEvent(final int infoType, List<CommanderDTO> commanders) {
        this.infoType = infoType;
        this.commanders = commanders;
    }

    public static Type<CommLoadedHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final CommLoadedHandler handler) {
        handler.onCommLoaded(this);
    }

    public Type<CommLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the infoType
     */
    public int getInfoType() {
        return infoType;
    }

    /**
     * @return the commanders
     */
    public List<CommanderDTO> getCommanders() {
        return commanders;
    }


}
