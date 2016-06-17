package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created by IntelliJ IDEA.
 * User: karavias
 * Date: 3/7/12
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ForeignUnitsLoadedHandler
        extends EventHandler {

    void onForeignUnitsLoaded(final ForeignUnitsLoadedEvent event);

}
