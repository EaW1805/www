package com.eaw1805.www.fieldbattle.views.layout.infopanels;


import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.fieldbattle.stores.dto.SocialFriend;



public class SocialPlayerInfoPanel extends AbsolutePanel {
    public SocialPlayerInfoPanel(final SocialFriend friend) {
        setSize("200px", "56px");
        Image profileImg = new Image(friend.getImageUrl());
        add(profileImg, 3, 3);
        add(new Label(friend.getName()), 56, 3);
        add(new Label(friend.getId()), 56, 15);
    }

}
