package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;

import java.util.List;

public class BtrainLoadedEvent
        extends GwtEvent<BtrainLoadedHandler> {

    private static final Type<BtrainLoadedHandler> TYPE = new Type<BtrainLoadedHandler>();

    private final int infoType;

    private final List<BaggageTrainDTO> btrains;

    public BtrainLoadedEvent(final int infoType, List<BaggageTrainDTO> btrains) {
        this.infoType = infoType;
        this.btrains = btrains;
    }

    public static Type<BtrainLoadedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final BtrainLoadedHandler handler) {
        handler.onBtrainLoaded(this);
    }

    public Type<BtrainLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the infoType
     */
    public int getInfoType() {
        return infoType;
    }

    /**
     * @return the btrains
     */
    public List<BaggageTrainDTO> getBtrains() {
        return btrains;
    }

}
