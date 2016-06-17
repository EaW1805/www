package com.eaw1805.www.fieldbattle.events.loading;

import com.google.gwt.event.shared.GwtEvent;
import com.eaw1805.data.dto.web.army.BrigadeDTO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 2/14/13
 * Time: 4:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class ArmiesLoadedEvent extends GwtEvent<ArmiesLoadedHandler> {

    private static final GwtEvent.Type<ArmiesLoadedHandler> TYPE = new GwtEvent.Type<ArmiesLoadedHandler>();

    final List<BrigadeDTO> brigades;

    public ArmiesLoadedEvent(List<BrigadeDTO> inBrigs) {
        brigades = inBrigs;
    }

    public static Type<ArmiesLoadedHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<ArmiesLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final ArmiesLoadedHandler handler) {
        handler.onUnitChanged(this);
    }

    public List<BrigadeDTO> getBrigades() {
        return brigades;
    }
}
