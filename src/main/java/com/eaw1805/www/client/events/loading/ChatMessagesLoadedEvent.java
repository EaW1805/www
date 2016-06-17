package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 5/31/12
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChatMessagesLoadedEvent
        extends GwtEvent<ChatMessagesLoadedHandler> {

    private final List<String> messages;

    private static final GwtEvent.Type<ChatMessagesLoadedHandler> TYPE = new GwtEvent.Type<ChatMessagesLoadedHandler>();

    public ChatMessagesLoadedEvent(final List<String> oldMessages) {
        this.messages = oldMessages;
    }

    public static GwtEvent.Type<ChatMessagesLoadedHandler> getType() {
        return TYPE;
    }

    protected void dispatch(final ChatMessagesLoadedHandler handler) {
        handler.onChatMessagesLoaded(this);
    }

    public GwtEvent.Type<ChatMessagesLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the alliedUnits
     */
    public List<String> getMessages() {
        return messages;
    }


}
