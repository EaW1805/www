package com.eaw1805.www.client.views.tutorial;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.client.remote.EmpireRpcService;
import com.eaw1805.www.client.remote.EmpireRpcServiceAsync;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.GameStore;

public class SkipTutorialPanel extends AbsolutePanel {

    private final EmpireRpcServiceAsync eService = GWT.create(EmpireRpcService.class);

    public SkipTutorialPanel() {
        setSize("120px", "25px");
        Label label = new Label("Skip Tutorial");
        add(label, 26, 3);

        final ImageButton skipButton = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        skipButton.setSize("20px", "20px");
        add(skipButton, 3, 2);
        skipButton.setTitle("Skip tutorial help and continue solo game");
        skipButton.setStyleName("pointer");
        skipButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eService.skipTutorial(GameStore.getInstance().getGameId(), new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new ErrorPopup(ErrorPopup.Level.ERROR, "Failed to skip tutorial.", false);
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        Window.Location.reload();
                    }
                });
            }
        });
    }

}
