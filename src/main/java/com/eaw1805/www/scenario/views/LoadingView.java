package com.eaw1805.www.scenario.views;


import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

public class LoadingView extends PopupPanel {

    private static LoadingView instance;
    int requests = 0;
    public static LoadingView getInstance() {
        if (instance == null) {
            instance = new LoadingView();
        }
        return instance;
    }

    private LoadingView() {
        setStyleName("none");
        setModal(true);
        setAutoHideEnabled(false);

        setWidget(new Image("http://static.eaw1805.com/images/loading/loading2.gif"));

    }

    public void requestShow() {
        requests++;
        show();
    }

    public void requestHide() {
        requests--;
        if (requests < 0) {
            requests = 0;
        }
        if (requests == 0) {
            hide();
        }
    }
}
