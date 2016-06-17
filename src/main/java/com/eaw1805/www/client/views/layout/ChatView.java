package com.eaw1805.www.client.views.layout;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.www.client.events.loading.ChatMessagesLoadedEvent;
import com.eaw1805.www.client.events.loading.ChatMessagesLoadedHandler;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.loading.NationsLoadedEvent;
import com.eaw1805.www.client.events.loading.NationsLoadedHandler;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.views.ChatMessageView;
import com.eaw1805.www.client.views.infopanels.ChatOptionInfoPanel;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.client.widgets.SelectEAW;
import com.eaw1805.www.client.widgets.TextBoxEAW;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatView extends AbsolutePanel {
    private SelectEAW selector;
    boolean opened = false;
    private final Timer opener;
    private final Timer closer;
    private final VerticalPanelScrollChild chatArea;
    boolean initialized = false;
    private final ScrollVerticalBarEAW chatAreaScroller;
    private static String protocol = "game";
    private final Map<String, List<ChatMessageView>> selectionToChatMessages = new HashMap<String, List<ChatMessageView>>();
    private final Map<String, ChatMessageView> lastMessageViewsByChat = new HashMap<String, ChatMessageView>();

    public ChatView() {

        setSize("427px", "437px");
        setStyleName("chatPanel");

        chatArea = new VerticalPanelScrollChild();
        chatArea.setSize("338px", "");

        chatAreaScroller = new ScrollVerticalBarEAW(chatArea, true);
        chatAreaScroller.setSize(354, 274);

        this.add(chatAreaScroller, 53, 80);

        final ImageButton toggler = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (opened) {
                    opener.cancel();
                    closer.scheduleRepeating(9);
                    opened = false;
                } else {
                    closer.cancel();
                    opener.scheduleRepeating(9);
                    opened = true;

                }
                toggler.deselect();
            }
        }).addToElement(toggler.getElement()).register();

        final TextBoxEAW textBox = new TextBoxEAW(false);


        textBox.setStyleName("transparentTextBox");
        textBox.setStyleName("empireChatTextBoxClick", true);
        textBox.setSize("348px", "29px");
        this.add(textBox, 52, 376);

        this.add(toggler, 18, 18);
        final ChatView myself = this;
        opener = new Timer() {
            @Override
            public void run() {
                final int width = Window.getClientWidth();
                final LayoutView view = GameStore.getInstance().getLayoutView();
                view.setWidgetPosition(myself, view.getWidgetX(myself) - 9, -8, true, true);
                if (view.getWidgetX(myself) < (width - (myself.getOffsetWidth() - 14))) {
                    view.setWidgetPosition(myself, width - (myself.getOffsetWidth() - 14), -8, true, true);
                    this.cancel();
                }
            }
        };

        closer = new Timer() {
            @Override
            public void run() {
                final int width = Window.getClientWidth();
                final LayoutView view = GameStore.getInstance().getLayoutView();
                view.setWidgetPosition(myself, view.getWidgetX(myself) + 9, -8, true, true);
                if (view.getWidgetX(myself) > (width - 40)) {
                    view.setWidgetPosition(myself, width - 40, -8, true, true);
                    this.cancel();
                }
            }
        };

        LoadEventManager.addNationsLoadedHandler(new NationsLoadedHandler() {
            public void onNationsLoaded(NationsLoadedEvent event) {
                selector = new SelectEAW() {
                    @Override
                    public void onChange() {
                        protocol = (String) this.getValue();
                        final List<ChatMessageView> messageViews = selectionToChatMessages.get(protocol);
                        chatArea.clear();
                        int count = 0;
                        for (ChatMessageView messageView : messageViews) {
                            count++;
                            chatArea.add(messageView);
                            if (count < messageViews.size()) {
                                chatArea.add(new HTML("<hr>"));
                            }
                        }
                        chatAreaScroller.scrollToBottom();
                        //finally mark panel as viewed
                        final ChatOptionInfoPanel panel = (ChatOptionInfoPanel) this.getSelectedOption();
                        panel.updateNotification(0);
                    }
                };
                selector.setOptionsStyles("optionDefault320x30", "optionHover320x30", "optionSelected320x30");
                selector.addOption(new ChatOptionInfoPanel(false, true, null), "glob");
                selectionToChatMessages.put("glob", new ArrayList<ChatMessageView>());
                if (!TutorialStore.getInstance().isTutorialMode()) {
                    selector.addOption(new ChatOptionInfoPanel(true, false, null), "game" + GameStore.getInstance().getGameId());
                    selectionToChatMessages.put("game" + GameStore.getInstance().getGameId(), new ArrayList<ChatMessageView>());
                    for (NationDTO nation : DataStore.getInstance().getNations()) {
                        if (GameStore.getInstance().getNationId() != nation.getNationId()
                                && nation.getNationId() > 0) {
                            final String protocol = "self" + GameStore.getInstance().getGameId()
                                    + "_" + nation.getNationId();
                            selector.addOption(new ChatOptionInfoPanel(false, false, nation), protocol);
                            selectionToChatMessages.put(protocol, new ArrayList<ChatMessageView>());
                        }
                    }
                }
                selector.setSize(320, 30, 377, 420, 9, 20, 20, 23);
                selector.setDropDownStyleName("dropDown320x420");

                ChatView.this.add(selector, 51, 20);
                selector.selectOption(0);


            }
        });
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean isOpened() {
        return opened;
    }

    public void onOpen(final String message) {
        chatArea.add(new HTML(message));
        chatAreaScroller.scrollToBottom();
    }


    public void onReconnect(final String message) {
//        chatArea.add(new HTML(message));
        chatAreaScroller.scrollToBottom();
    }

    /**
     * callback receiving a message from atmosphere
     *
     * @param scope    The scope, aka, the protocol, aka, the mapping in the dropdown select
     * @param message  The message received
     * @param gameId   The game id
     * @param nationId The nation id
     */
    public void onMessage(final String scope, String message, final int gameId, final int nationId, final boolean newMessage, final String emailEncoded,
                          final Long time, final String username) {

        final ChatMessageView messageView;
        boolean updateLast = false;
        boolean updateTime = true;
        boolean addHR = false;
        String fixedScope = scope;
        if (scope.startsWith("self")
                && GameStore.getInstance().getNationId() == nationId) {
            fixedScope = "self" + gameId + "_" + nationId;
        }


        if (!lastMessageViewsByChat.containsKey(fixedScope)
                || !lastMessageViewsByChat.get(fixedScope).getEncodedEmail().equals(emailEncoded)) {
            if (lastMessageViewsByChat.containsKey(fixedScope)) {
                addHR = true;
            }
            messageView = new ChatMessageView(gameId, message, emailEncoded, time, username);
            lastMessageViewsByChat.put(fixedScope, messageView);

        } else {
            messageView = lastMessageViewsByChat.get(fixedScope);
            updateLast = true;
            //since it is update... remove the date so it will look better
            final long passedTime = time - messageView.getTime();

            final int minutesPassed = (int) ((passedTime / (1000 * 60)) % 60);
            if (minutesPassed < 5) {
                updateTime = false;
                message = message.split("<br>")[1];
            }
        }

        boolean notify = false;
        String panelCode = null;
        if ("glob".equalsIgnoreCase(scope)) {
            if (!updateLast) {
                selectionToChatMessages.get("glob").add(messageView);
            }
            if ("glob".equalsIgnoreCase(protocol)) {
                if (updateLast) {
                    messageView.addMessageToContainer(message, time, updateTime);
                } else {
                    if (addHR) {
                        chatArea.add(new HTML("<hr>"));
                    }
                    chatArea.add(messageView);
                }
            } else {
                if (updateLast) {
                    messageView.addMessageToContainer(message, time, updateTime);
                }
                notify = true;
                panelCode = "glob";
//                GameEventManager.chatMessageReceived(message, gameId, nationId, scope);
            }
        } else if (("game" + GameStore.getInstance().getGameId()).equalsIgnoreCase(scope)) {
            if (!updateLast) {
                selectionToChatMessages.get("game" + GameStore.getInstance().getGameId()).add(messageView);
            }
            if (("game" + GameStore.getInstance().getGameId()).equalsIgnoreCase(protocol)) {
                if (updateLast) {
                    messageView.addMessageToContainer(message, time, updateTime);
                } else {
                    if (addHR) {
                        chatArea.add(new HTML("<hr>"));
                    }
                    chatArea.add(messageView);
                }
            } else {
                if (updateLast) {
                    messageView.addMessageToContainer(message, time, updateTime);
                }
                notify = true;
                panelCode = "game" + GameStore.getInstance().getGameId();
//                GameEventManager.chatMessageReceived(message, gameId, nationId, scope);
            }
        } else {
            if (GameStore.getInstance().getNationId() == nationId) {
                if (!updateLast) {
                    selectionToChatMessages.get(scope).add(messageView);
                }
                if (scope.equalsIgnoreCase(protocol)) {
                    if (updateLast) {
                        messageView.addMessageToContainer(message, time, updateTime);
                    } else {
                        if (addHR) {
                            chatArea.add(new HTML("<hr>"));
                        }
                        chatArea.add(messageView);
                    }
                } else {
                    if (updateLast) {
                        messageView.addMessageToContainer(message, time, updateTime);
                    }
                    notify = true;
                    panelCode = scope;
//                    GameEventManager.chatMessageReceived(message, gameId, nationId, scope);
                }
            } else {
                final String changedScope = "self" + gameId + "_" + nationId;
                if (!updateLast) {
                    selectionToChatMessages.get(changedScope).add(messageView);
                }
                if (changedScope.equalsIgnoreCase(protocol)) {
                    if (updateLast) {
                        messageView.addMessageToContainer(message, time, updateTime);
                    } else {
                        if (addHR) {
                            chatArea.add(new HTML("<hr>"));
                        }
                        chatArea.add(messageView);
                    }
                } else {
                    if (updateLast) {
                        messageView.addMessageToContainer(message, time, updateTime);
                    }
                    notify = true;
                    panelCode = changedScope;
//                    GameEventManager.chatMessageReceived(message, gameId, nationId, scope);
                }
            }
        }
        if (notify && newMessage) {
            int index = -1;
            for (Object obj : selector.getValues()) {
                index++;
                final String code = (String) obj;
                if (code.equals(panelCode)) {
                    final ChatOptionInfoPanel panel = (ChatOptionInfoPanel) selector.getOption(index);
                    panel.updateNotification(1);
                    break;
                }
            }
            if (gameId != GameStore.getInstance().getGameId()) {
                GameStore.getInstance().getLayoutView().getNotificationPopup().showMessage("You have a new global chat message from nation " +
                        DataStore.getInstance().getNationNameByNationId(nationId) + " from game " + gameId);
            } else if (nationId != GameStore.getInstance().getNationId()) {
                GameStore.getInstance().getLayoutView().getNotificationPopup().showMessage("You have a new chat message from nation " +
                        DataStore.getInstance().getNationNameByNationId(nationId));
            }

        } else {
            chatAreaScroller.scrollToBottom();
        }
    }


    public void onError(final String message) {
        chatArea.add(new HTML(message));
        chatAreaScroller.scrollToBottom();
    }

//    native void sendMessage(String message) /*-{
//      $wnd.sendWebSocketChatMessage(message); // $wnd is a JSNI synonym for 'window'
//    }-*/;

    /**
     * When chat is attached then connect with the atmosphere servlet
     */
    public void onAttach() {
        super.onAttach();
        try {
            if (DataStore.getInstance().isChatMessagesLoaded()) {//if messages have been loaded use them...
                initChatMessages(DataStore.getInstance().getChatMessages());
            } else {//else wait for them to load
                LoadEventManager.addChatMessagesLoadedHandler(new ChatMessagesLoadedHandler() {

                    public void onChatMessagesLoaded(final ChatMessagesLoadedEvent event) {
                        initChatMessages(event.getMessages());
                    }
                });

            }
        } catch (Exception e) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Something went wrong trying to retrieve message history", false);
        }

        if (!initialized) {
            try {
                initializeChat(ChatView.this, GameStore.getInstance().getGameId(), GameStore.getInstance().getNationId());
            } catch (Exception e) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Could not connect to chat server", false);
            }
        }
        initialized = true;
    }

    public void initChatMessages(final List<String> messages) {
        for (final String message : messages) {
            addMessageToPanel(message, false);

        }
    }

    public void addMessageToPanel(final String message, final boolean newMessage) {
        try {
            final JSONValue value = JSONParser.parse(message);
            //debug(value.toString());
            final JSONObject json = value.isObject();
            //debug("isObject "+ json);
            final Date date = new Date();
            date.setTime((long) json.get("time").isNumber().doubleValue());


            onMessage(json.get("author").isString().stringValue(), "<span style='color:brown'> @ "
                    + dateToString(date) + "</span><br>" + json.get("message").isString().stringValue(),
                    (int) json.get("gameId").isNumber().doubleValue(),
                    (int) json.get("nationId").isNumber().doubleValue(), newMessage, json.get("encodedEmail").isString().stringValue(),
                    (long) json.get("time").isNumber().doubleValue(), json.get("username").isString().stringValue());
        } catch (Exception e) {
            debug("Failed to convert json " + e.toString());
        }
    }

    native void debug(final String message)/*-{
        console.log(message);
    }-*/;

    public String dateToString(final Date date) {
        return DateTimeFormat.getMediumDateTimeFormat().format(date);
    }


    native void initializeChat(ChatView cv, int thisGamesId, int thisNationsId) /*-{
        try {

            var url = "ws://gamekeeper.oplongames.com:8098/chateaw/websocket";
            var ws;
            var myVar;
//            var url = "ws://150.140.5.10:8080/chateaw/websocket";
//            var url = "ws://127.0.0.1:8080/chateaw/websocket";

            function wsopen(event) {
                try {
                    ws.send("subCode:" + thisNationsId + "_" + thisGamesId);
                    myVar = setInterval(function () {
                        sendPing()
                    }, 60000);

                } catch (e) {
                    alert("error while sending message : " + e);
                }
            }

            function wsmessage(event) {
                try {
                    cv.@com.eaw1805.www.client.views.layout.ChatView::addMessageToPanel(Ljava/lang/String;Z)(event.data, true);
                } catch (e) {
                    alert("on message : " + e);
                }
            }

            function wsclose(event) {
                console.log("Shouldn't be printed!!");
                clearInterval(myVar);
                //print a message and try to reconnect
                ws = new WebSocket(url, "EAW1805");
                ws.onopen = wsopen;
                ws.onmessage = wsmessage;
                ws.onclose = wsclose;
            }

            //connect to the message service
            ws = new WebSocket(url, "EAW1805");
            ws.onopen = wsopen;
            ws.onmessage = wsmessage;
            ws.onclose = wsclose;

            function sendPing() {
                ws.send("ping");
            }

            var input = $wnd.$(".empireChatTextBoxClick");

            input.keydown(function (e) {
                try {
                    if (e.keyCode === 13) {

                        var msg = $wnd.$(this).val();
                        console.log(ws);
                        ws.send(JSON.stringify({ author:cv.@com.eaw1805.www.client.views.layout.ChatView::getProtocol()(), message:msg, gameId:thisGamesId, nationId:thisNationsId}));
//                        subSocket.push(JSON.stringify({ author: cv.@com.eaw1805.www.client.views.layout.ChatView::getProtocol()(), message: msg , gameId: thisGamesId, nationId: thisNationsId}));

                        $wnd.$(this).val('');
                        $wnd.$(this).focus();
                    }
                } catch (e) {
                    alert("on key down : " + e);
                }
            });

        } catch (e) {
            alert("jsni : " + e);
        }

    }-*/;
}
