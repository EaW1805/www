package com.eaw1805.www.fieldbattle.events.loading;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;

import java.util.List;


public class LoadingEventManager {
    static public HandlerManager handlerManager = new HandlerManager(null);

    public static void ArmiesLoaded(final List<BrigadeDTO> brigades) {
        handlerManager.fireEvent(new ArmiesLoadedEvent(brigades));
    }

    public static HandlerRegistration addArmiesLoadedHandler(final ArmiesLoadedHandler handler) {
        return handlerManager.addHandler(ArmiesLoadedEvent.getType(), handler);
    }

    public static void CommandersLoaded(final List<CommanderDTO> commanders) {
        handlerManager.fireEvent(new CommandersLoadedEvent(commanders));
    }

    public static HandlerRegistration addCommandersLoadedHandler(final CommandersLoadedHandler handler) {
        return handlerManager.addHandler(CommandersLoadedEvent.getType(), handler);
    }


}
