package com.eaw1805.www.fieldbattle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.dto.SocialSettings;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;

public class EmpireFieldBattleClient implements EntryPoint, RegionConstants {

    @Override
    public void onModuleLoad() {
        final RootPanel rootPanel = RootPanel.get("MainPanel");
        rootPanel.setSize("100%", "100%");


        final int battleId = Integer.parseInt(((InputElement) (Element) DOM.getElementById("battleId")).getValue());
        final int scenarioId = Integer.parseInt(((InputElement) (Element) DOM.getElementById("scenarioId")).getValue());
        final int nationId = Integer.parseInt(((InputElement) (Element) DOM.getElementById("nationId")).getValue());
        final int round = Integer.parseInt(((InputElement) (Element) DOM.getElementById("round")).getValue());
        final int side = Integer.parseInt(((InputElement) (Element) DOM.getElementById("side")).getValue());
        final String title = ((InputElement) (Element) DOM.getElementById("title")).getValue();
        final String ready = ((InputElement) (Element) DOM.getElementById("sideReady")).getValue();
        final String gameEnded = ((InputElement) (Element) DOM.getElementById("gameEnded")).getValue();
        final int winner = Integer.parseInt(((InputElement) (Element) DOM.getElementById("winner")).getValue());
        final String standAlone = ((InputElement) (Element) DOM.getElementById("standAlone")).getValue();
        final String facebookId = ((InputElement) (Element) DOM.getElementById("facebookId")).getValue();
        //check if user is authenticated.
        try {
        RootPanel.get().addDomHandler(new KeyDownHandler() {

            public void onKeyDown(final KeyDownEvent event) {
                MainPanel.getInstance().handleKeyDownEvent(event.getNativeKeyCode());

                if (event.getNativeKeyCode() == 9) {//if tab try to stop tabs bad effect
                    event.stopPropagation();
                    event.preventDefault();
                }
            }
        }, KeyDownEvent.getType());

        RootPanel.get().addDomHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(final KeyUpEvent event) {
                MainPanel.getInstance().handleKeyUpEvent(event.getNativeKeyCode());

                if (event.getNativeKeyCode() == 9) {//if tab try to stop tabs bad effect
                    event.stopPropagation();
                    event.preventDefault();
                }
            }
        }, KeyUpEvent.getType());

        rootPanel.add(MainPanel.getInstance());
        Window.addResizeHandler(new ResizeHandler() {
            public void onResize(final ResizeEvent event) {
                MainPanel.getInstance().setMapSize();
                MainPanel.getInstance().onResize();
            }
        });

        if ("1".equals(standAlone)) {
            if ("".equals(facebookId)) {
                //user is not authenticated.
                return;
            }
            try {
                BaseStore.getInstance().setFacebookId(facebookId);
                BaseStore.getInstance().setStandAlone("1".equals(standAlone));
                LoadUtil.getInstance().loadSocialPanel(false);

            } catch (Exception e) {
                Window.alert("eeeee? " + e.toString());
            }

        } else {
            final SocialSettings settings = new SocialSettings();
            settings.setScenarioId(scenarioId);
            settings.setBattleId(battleId);
            settings.setNationId(nationId);
            settings.setRound(round);
            settings.setSide(side);
            settings.setTitle(title);
            settings.setSideReady(ready != null && ready.equals("1"));
            settings.setGameEnded("true".equals(gameEnded));
            settings.setWinner(winner);
            settings.setStandAlone("1".equals(standAlone));
            settings.setFacebookId(facebookId);
            LoadUtil.getInstance().load(settings);
        }
        } catch (Exception e) {
            Window.alert("fffffff?" + e.toString());
        }
    }
}
