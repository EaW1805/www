package com.eaw1805.www.client.gui.eventHandlers;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.Element;

public interface DelEventHandler {

    DelEventHandler addToElement(final Element el);
    void execute(final MouseEvent event);
    void register();
    void registerOnce();
    void unRegister();
    String getId();
}
