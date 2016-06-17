package com.eaw1805.www.fieldbattle.events.loading;

import com.google.gwt.event.shared.GwtEvent;
import com.eaw1805.data.dto.web.army.CommanderDTO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 2/17/13
 * Time: 11:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommandersLoadedEvent  extends GwtEvent<CommandersLoadedHandler> {

    private static final GwtEvent.Type<CommandersLoadedHandler> TYPE = new GwtEvent.Type<CommandersLoadedHandler>();

    final List<CommanderDTO> brigades;

    public CommandersLoadedEvent(List<CommanderDTO> inBrigs) {
        brigades = inBrigs;
    }

    public static Type<CommandersLoadedHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<CommandersLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final CommandersLoadedHandler handler) {
        handler.onUnitChanged(this);
    }

    public List<CommanderDTO> getBrigades() {
        return brigades;
    }
}
