package com.eaw1805.www.client.views;


import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.shared.stores.GameStore;

public class ChatMessageView extends HorizontalPanel {
    private final VerticalPanel msgContainer;
    private final String encodedEmail;
    private long time;

    public ChatMessageView(final int gameId, final String message, final String emailEncoded, final long time, final String username) {
        this.encodedEmail = emailEncoded;
        this.time = time;
        this.setSize("334px", "");
        setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        final AbsolutePanel avatarContainer = new AbsolutePanel();
        avatarContainer.setSize("55px", "55px");

        final Image userImage = new Image("https://secure.gravatar.com/avatar/" + emailEncoded + "?s=50&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png");

        //add the nation image and style the cell
        userImage.setStyleName("pointer");
        userImage.setSize("50px", "");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                Window.open("http://www.eaw1805.com/user/" + username, "_blank", "");
            }
        }).addToElement(userImage.getElement()).register();


        userImage.setTitle(username);
        avatarContainer.add(userImage, 0, 0);

        add(avatarContainer);
        setCellWidth(avatarContainer, "55px");
        if (GameStore.getInstance().getGameId() != gameId) {
            //if game it is a global message then show the game id and style the cell.
            final HTML game = new HTML("#" + gameId);
            add(game);
            setCellWidth(game, "10px");
        }
        //finally add the actual message
        msgContainer = new VerticalPanel();
        msgContainer.add(new HTML(message));
        add(msgContainer);

    }

    public String getEncodedEmail() {
        return encodedEmail;
    }

    public void addMessageToContainer(final String message, final long time, final boolean updateTime) {
        msgContainer.add(new HTML(message));
        if (updateTime) {
            this.time = time;
        }
    }

    public long getTime() {
        return time;
    }
}
