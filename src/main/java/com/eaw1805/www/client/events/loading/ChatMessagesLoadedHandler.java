package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 5/31/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ChatMessagesLoadedHandler
        extends EventHandler {

    void onChatMessagesLoaded(final ChatMessagesLoadedEvent event);

}

