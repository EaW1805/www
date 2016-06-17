package com.eaw1805.www.fieldbattle.views.layout.social;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.www.fieldbattle.LoadUtil;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcService;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcServiceAsync;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.dto.SocialFriend;
import com.eaw1805.www.fieldbattle.stores.dto.SocialGame;
import com.eaw1805.www.fieldbattle.stores.SocialStore;
import com.eaw1805.www.fieldbattle.stores.dto.SocialPosition;
import com.eaw1805.www.fieldbattle.stores.dto.SocialSettings;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.NationSocialInfoPanel;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.SocialGameInfoPanel;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.SocialPlayerInfoPanel;
import com.eaw1805.www.fieldbattle.widgets.ScrollVerticalBar;
import com.eaw1805.www.fieldbattle.widgets.SelectList;

import java.util.Comparator;
import java.util.List;

public class StandAloneSocialPanel extends AbsolutePanel {

    private static StandAloneSocialPanel instance = null;

    public static StandAloneSocialPanel getInstance() {
        if (instance == null) {
            instance = new StandAloneSocialPanel();
        }
        return instance;
    }

    private final static EmpireFieldBattleRpcServiceAsync service = GWT.create(EmpireFieldBattleRpcService.class);

    SelectList<SocialGame> scenarioScroller;
    SelectList<SocialGame> joinScroller;
    SelectList<SocialGame> myGamesScroller;
    SelectList<SocialGame> pendingScroller;

    VerticalPanel side1 = new VerticalPanel();
    VerticalPanel side2 = new VerticalPanel();
    boolean isNewGame = true;
    Button createGameButton;
    SocialGame curGame = null;
    boolean enabled = true;

    private StandAloneSocialPanel() {


        getElement().getStyle().setZIndex(10001);
        setSize("800px", "600px");
        setStyleName("tipPanel");

        final Image mapImg = new Image();
        mapImg.setSize("190px", "190px");
        add(mapImg, 262, 68);

        final Label newGameLabel = new Label("Create Game");
        newGameLabel.setStyleName("clearFont-large");
        add(newGameLabel, 20, 50);

        final Label myGamesLabel = new Label("My Games");
        myGamesLabel.setStyleName("clearFont-large");
        add(myGamesLabel, 590, 50);

        final Label pendingLabel = new Label("Pending");
        pendingLabel.setStyleName("clearFont-large");
        add(pendingLabel, 590, 290);

        final Label side1Label = new Label("Side 1");
        side1Label.setStyleName("clearFont-large");
        add(side1Label, 269, 284);

        final Label side2Label = new Label("Side 2");
        side2Label.setStyleName("clearFont-large");
        add(side2Label, 472, 284);

        final Label joinGameLabel = new Label("Join Game");
        joinGameLabel.setStyleName("clearFont-large");
        add(joinGameLabel, 20, 290);

        final Label vsLabel = new Label("VS");
        vsLabel.setStyleName("clearFont-large");
        add(vsLabel, 387, 371);



        joinScroller = new SelectList<SocialGame>() {
            @Override
            public void onChange(Widget option, SocialGame value) {
                scenarioScroller.deselect();
                myGamesScroller.deselect();
                pendingScroller.deselect();
                side1.clear();
                side2.clear();
                isNewGame = false;
                curGame = value;
                createGameButton.setVisible(false);
                for (SocialPosition nation : value.getSide1()) {
                    side1.add(new NationSocialInfoPanel(value, nation, isNewGame));
                }
                for (SocialPosition nation : value.getSide2()) {
                    side2.add(new NationSocialInfoPanel(value, nation, isNewGame));
                }
                mapImg.setUrl("http://direct.eaw1805.com/fieldmaps/s" + BaseStore.getInstance().getScenarioId() + "/m" + value.getId() + ".jpg");
            }
        };

        joinScroller.setSize(200, 200);
        add(joinScroller, 20, 320);

        scenarioScroller = new SelectList<SocialGame>() {

            @Override
            public void onChange(Widget option, SocialGame value) {
                joinScroller.deselect();
                myGamesScroller.deselect();
                pendingScroller.deselect();
                createGameButton.setText("Create Game");
                createGameButton.setVisible(true);
                side1.clear();
                side2.clear();
                isNewGame = true;
                curGame = value;
                for (SocialPosition nation : value.getSide1()) {
                    //reset facebook id...
                    nation.setFacebookId("");
                    side1.add(new NationSocialInfoPanel(value, nation, isNewGame));
                }
                for (SocialPosition nation : value.getSide2()) {
                    //reset facebook id...
                    nation.setFacebookId("");
                    side2.add(new NationSocialInfoPanel(value, nation, isNewGame));
                }
                mapImg.setUrl("http://direct.eaw1805.com/fieldmaps/s" + BaseStore.getInstance().getScenarioId() + "/m" + value.getId() + ".jpg");

            }
        };
        scenarioScroller.setSize(200, 200);
        add(scenarioScroller, 20, 80);

        myGamesScroller = new SelectList<SocialGame>() {
            @Override
            public void onChange(Widget option, SocialGame value) {
                joinScroller.deselect();
                scenarioScroller.deselect();
                pendingScroller.deselect();
                side1.clear();
                side2.clear();
                isNewGame = false;
                curGame = value;
                createGameButton.setText("Play Game");
                createGameButton.setVisible(true);
                for (SocialPosition nation : value.getSide1()) {
                    side1.add(new NationSocialInfoPanel(value, nation, isNewGame));
                }
                for (SocialPosition nation : value.getSide2()) {
                    side2.add(new NationSocialInfoPanel(value, nation, isNewGame));
                }
                mapImg.setUrl("http://direct.eaw1805.com/fieldmaps/s" + BaseStore.getInstance().getScenarioId() + "/m" + value.getId() + ".jpg");

            }
        };
        myGamesScroller.setSize(200, 200);
        add(myGamesScroller, 590, 80);

        pendingScroller = new SelectList<SocialGame>() {
            @Override
            public void onChange(Widget option, SocialGame value) {
                joinScroller.deselect();
                scenarioScroller.deselect();
                myGamesScroller.deselect();
                side1.clear();
                side2.clear();
                isNewGame = false;
                curGame = value;
                createGameButton.setVisible(false);

                for (SocialPosition nation : value.getSide1()) {
                    side1.add(new NationSocialInfoPanel(value, nation, isNewGame));
                }
                for (SocialPosition nation : value.getSide2()) {
                    side2.add(new NationSocialInfoPanel(value, nation, isNewGame));
                }
                mapImg.setUrl("http://direct.eaw1805.com/fieldmaps/s" + BaseStore.getInstance().getScenarioId() + "/m" + value.getId() + ".jpg");
            }
        };
        pendingScroller.setSize(200, 200);
        add(pendingScroller, 590, 320);


        AbsolutePanel stylePanel = new AbsolutePanel();
        stylePanel.setStyleName("selectorOption");
        stylePanel.setSize("132px", "228px");
        add(stylePanel, 239, 311);

        stylePanel = new AbsolutePanel();
        stylePanel.setStyleName("selectorOption");
        stylePanel.setSize("132px", "228px");
        add(stylePanel, 444, 311);

        add(side1, 242, 311);
        add(side2, 447, 311);

        createGameButton = new Button("Create Game");
        createGameButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (isNewGame) {
                    service.createGame(BaseStore.getInstance().getScenarioId(), scenarioScroller.getSelectedValue().getId(), curGame, new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(final Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(final Integer integer) {
                            //now continue loading data from server, oeo
                            updateMyGames();
                            updateAvailableGames();
                            for (SocialPosition pos : curGame.getSide1()) {
                                if (pos.getFacebookId() != null
                                        && !pos.getFacebookId().isEmpty()
                                        && !pos.getFacebookId().equals(BaseStore.getInstance().getFacebookId())) {
                                    SocialStore.getInstance().inviteFriend(pos.getFacebookId());
                                }
                            }
                            for (SocialPosition pos : curGame.getSide2()) {
                                if (pos.getFacebookId() != null
                                        && !pos.getFacebookId().isEmpty()
                                        && !pos.getFacebookId().equals(BaseStore.getInstance().getFacebookId())) {
                                    SocialStore.getInstance().inviteFriend(pos.getFacebookId());
                                }
                            }
//                            Window.alert("To do : load data from server");
                        }
                    });
                } else {
                    service.loadGame(BaseStore.getInstance().getScenarioId(), curGame.getId(), new AsyncCallback<SocialSettings>() {
                        @Override
                        public void onFailure(final Throwable throwable) {
                            Window.alert("Failed to load game... please try again");
                        }

                        @Override
                        public void onSuccess(final SocialSettings settings) {
                            LoadUtil.getInstance().load(settings);

                        }
                    });
                }
            }
        });
        add(createGameButton, 365, 554);
        //load base games.
        loadBaseGames();
    }

    public void loadBaseGames() {
        service.getScenarioGames(BaseStore.getInstance().getScenarioId(), new AsyncCallback<List<SocialGame>>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert("Error occured : " + throwable.getMessage());
            }

            @Override
            public void onSuccess(List<SocialGame> socialGames) {
                SocialStore.getInstance().getScenarioGames().clear();
                SocialStore.getInstance().getScenarioGames().addAll(socialGames);
                updateScenarioGames();
                SocialLoadingPanel.getInstance().nextStep("Base games loaded.");
            }
        });
    }

    public void updateAvailableGames() {
        service.getAvailableGames(BaseStore.getInstance().getScenarioId(), new AsyncCallback<List<SocialGame>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(List<SocialGame> socialGames) {
                updateAvailableGames(socialGames);
            }
        });
    }

    public void updatePending() {
        service.getPendingGames(BaseStore.getInstance().getScenarioId(), new AsyncCallback<List<SocialGame>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(List<SocialGame> socialGames) {
                updatePending(socialGames);
            }
        });
    }

    public void updateMyGames() {
        service.getMyGames(BaseStore.getInstance().getScenarioId(), new AsyncCallback<List<SocialGame>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(List<SocialGame> socialGames) {
                updateMyGames(socialGames);
            }
        });
    }

    public void autoUpdate() {
        new Timer() {

            @Override
            public void run() {
                if (enabled) {
                    updateAvailableGames();
                }
                //update again after 5 seconds
                schedule(5000);
            }
        }.run();
    }

    /**
     * Retrieve the available games, games you don't play.
     *
     * @param availableGames All available games to join.
     */
    public void updateAvailableGames(final List<SocialGame> availableGames) {
        int pos = joinScroller.getScrollPosition();
        joinScroller.clearOptions();
        for (final SocialGame game : availableGames) {
            joinScroller.addOption(new SocialGameInfoPanel(game, false), game);
        }
        joinScroller.setScrollPosition(pos);
        if (curGame != null) {
            //try to select from this list

            joinScroller.selectOptionByValue(curGame, new Comparator<SocialGame>() {
                @Override
                public int compare(SocialGame o1, SocialGame o2) {
                    return o1.getId() - o2.getId();
                }
            }, true);

        }
    }

    public void updatePending(final List<SocialGame> pendingGames) {
        int pos = pendingScroller.getScrollPosition();
        pendingScroller.clearOptions();
        for (final SocialGame game : pendingGames) {
            pendingScroller.addOption(new SocialGameInfoPanel(game, false), game);
        }
        pendingScroller.setScrollPosition(pos);
        if (curGame != null) {
            //try to select from this list
            pendingScroller.selectOptionByValue(curGame, new Comparator<SocialGame>() {
                @Override
                public int compare(SocialGame o1, SocialGame o2) {
                    return o1.getId() - o2.getId();
                }
            }, true);
        }
    }

    public void updateMyGames(final List<SocialGame> myGames) {
        int pos = myGamesScroller.getScrollPosition();
        myGamesScroller.clearOptions();
        for (final SocialGame game : myGames) {
            myGamesScroller.addOption(new SocialGameInfoPanel(game, true), game);
        }
        myGamesScroller.setScrollPosition(pos);
        if (curGame != null) {
            //try to select from this list
            myGamesScroller.selectOptionByValue(curGame, new Comparator<SocialGame>() {
                @Override
                public int compare(SocialGame o1, SocialGame o2) {
                    return o1.getId() - o2.getId();
                }
            }, true);
        }
    }

    public void updateScenarioGames() {
        int pos = scenarioScroller.getScrollPosition();
        scenarioScroller.clearOptions();
        for (SocialGame game : SocialStore.getInstance().getScenarioGames()) {
            scenarioScroller.addOption(new SocialGameInfoPanel(game, false), game);
        }
        scenarioScroller.setScrollPosition(pos);
    }

    private Label createLabel(final String text) {
        final Label out = new Label(text);
        out.setStyleName("whiteText");
        return out;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
