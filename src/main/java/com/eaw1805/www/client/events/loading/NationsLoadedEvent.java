package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;
import com.eaw1805.data.dto.common.NationDTO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: karavias
 * Date: 4/25/12
 * Time: 3:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class NationsLoadedEvent
        extends GwtEvent<NationsLoadedHandler> {

    private final List<NationDTO> nations;
    private static final Type<NationsLoadedHandler> TYPE = new Type<NationsLoadedHandler>();

    public NationsLoadedEvent(final List<NationDTO> nations) {
        this.nations = nations;
    }

    public static Type<NationsLoadedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final NationsLoadedHandler handler) {
        handler.onNationsLoaded(this);

    }

    public Type<NationsLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the alliedUnits
     */
    public List<NationDTO> getNations() {
        return nations;
    }


}

