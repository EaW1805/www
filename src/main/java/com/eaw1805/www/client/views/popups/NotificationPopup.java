package com.eaw1805.www.client.views.popups;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.shared.stores.GameStore;


public class NotificationPopup extends AbsolutePanel {

    Timer closer;
    Label message;

    public NotificationPopup() {

        setSize("366px", "89px");
        setStyleName("armyInfoPanel");
        closer = new Timer() {
            @Override
            public void run() {
                GameStore.getInstance().getLayoutView().hideNotification();
            }
        };
        message = new Label("");
        message.setWidth("360px");
        message.setStyleName("clearFont");
        add(message, 3, 3);
        //just something huge....
        getElement().getStyle().setZIndex(10000000);
    }

    public void showMessage(final String msg) {
        message.setText(msg);
        GameStore.getInstance().getLayoutView().showNotification();
    }

    public void reSchedule() {
        closer.cancel();
        closer.schedule(3000);
    }

    public void onAttach() {
        super.onAttach();
        reSchedule();
    }

}
