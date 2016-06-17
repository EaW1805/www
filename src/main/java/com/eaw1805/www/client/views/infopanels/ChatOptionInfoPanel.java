package com.eaw1805.www.client.views.infopanels;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.common.NationDTO;

public class ChatOptionInfoPanel extends AbsolutePanel {

    private Label notification;
    private int pendingView = 0;

    public ChatOptionInfoPanel(final boolean isGameChat,
                               final boolean isGlobalChat, final NationDTO nation) {

        setSize("320px", "30px");

        Image nationImage;
        if (isGameChat) {
            nationImage = new Image("http://static.eaw1805.com/images/panels/chat/ButGamePublicChat-list.png");

        } else if (isGlobalChat) {
            nationImage = new Image("http://static.eaw1805.com/images/panels/chat/ButGlobalChat-list.png");

        } else {
            nationImage = new Image("http://static.eaw1805.com/images/nations/nation-" + nation.getNationId() + "-list.jpg");
        }
        nationImage.setSize("280px", "27px");
        add(nationImage, 3, 3);

        notification = new Label("");
        notification.setStyleName("clearFont");
        add(notification, 286, 7);
    }

    public void updateNotification(final int action) {
        if (action == 0) {
            pendingView = 0;
        } else {
            pendingView++;
        }
        this.pendingView = pendingView;
        if (pendingView == 0) {
            notification.setText("");
        } else {
            notification.setText(String.valueOf(pendingView));
        }

    }
}
