package com.eaw1805.www.fieldbattle.events.loading;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 2/14/13
 * Time: 4:29 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ArmiesLoadedHandler extends EventHandler {
    void onUnitChanged(final ArmiesLoadedEvent event);
}
