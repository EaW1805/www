package com.eaw1805.www.fieldbattle.views.layout.infopanels;


import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.dto.SocialGame;
import com.eaw1805.www.fieldbattle.stores.utils.MapUtils;

public class SocialGameInfoPanel extends AbsolutePanel {

    public boolean requestUpdate = false;
    final Timer t;

    public SocialGameInfoPanel(final SocialGame game, final boolean update) {
        requestUpdate = update;
        setSize("200px", "56px");
        Image mapImg = new Image("http://direct.eaw1805.com/fieldmaps/s" + BaseStore.getInstance().getScenarioId() + "/m" + game.getId() + ".jpg");
        mapImg.setSize("50px", "50px");
        add(mapImg, 3, 3);
        add(new Label(game.getName()), 56, 3);
        add(new HTML(game.getDescription()), 56, 21);
        final HTML status = new HTML();
        add(status, 56, 37);
        t = new Timer() {
            @Override
            public void run() {
                BaseStore.Service.getGameStatus(BaseStore.getInstance().getScenarioId(), game.getId(), new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        status.setHTML("Failed to get status");
                        //every 5 seconds, check again.
                        t.schedule(5000);
                    }

                    @Override
                    public void onSuccess(String s) {
                        status.setHTML(s);
                        //every 5 seconds, check status again.
                        t.schedule(5000);
                    }
                });

            }
        };
    }

    public void onAttach() {
        super.onAttach();
        if (requestUpdate) {
            t.run();
        }
    }

    public void onDetach() {
        super.onDetach();
        if (requestUpdate) {
            t.cancel();
        }
    }

}
