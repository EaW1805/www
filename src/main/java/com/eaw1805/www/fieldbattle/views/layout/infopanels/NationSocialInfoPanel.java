package com.eaw1805.www.fieldbattle.views.layout.infopanels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcService;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcServiceAsync;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.dto.SocialFriend;
import com.eaw1805.www.fieldbattle.stores.SocialStore;
import com.eaw1805.www.fieldbattle.stores.dto.SocialGame;
import com.eaw1805.www.fieldbattle.stores.dto.SocialPosition;
import com.eaw1805.www.fieldbattle.views.layout.social.StandAloneSocialPanel;
import com.eaw1805.www.fieldbattle.widgets.shared.TextBoxEditable;
import com.eaw1805.www.fieldbattle.widgets.shared.TextBoxFilterList;


public class NationSocialInfoPanel extends AbsolutePanel {
    private final static EmpireFieldBattleRpcServiceAsync service = GWT.create(EmpireFieldBattleRpcService.class);

    Label nameLbl = new Label("Loading...");
    SocialPosition pos;
    public NationSocialInfoPanel(final SocialGame socialGame, final SocialPosition position, final boolean isNewGame) {
        pos = position;
        setSize("126px", "76px");
        final Image nationImg = new Image("http://static.eaw1805.com/images/nations/nation-" + position.getNation().getNationId() + "-120.png");
        nationImg.setHeight("45px");
        add(nationImg, 3, 3);

        final Image img;
        if (position.getFacebookId() == null) {
            img = new Image("http://graph.facebook.com//picture?type=square");
        } else {
            img = new Image("http://graph.facebook.com/" + position.getFacebookId() + "/picture?type=square");
        }
        img.setHeight("45px");
        add(img, 76, 3);

        if (isNewGame) {
            //if this is a newly created game, then you need to show selections for other players.
            final TextBoxEditable player = new TextBoxEditable("Select Player");
            TextBoxFilterList<SocialFriend> filterList = new TextBoxFilterList<SocialFriend>(player) {
                @Override
                public void onChange(final Widget option, final SocialFriend value) {
                    position.setFacebookId(value.getId());
                    img.setUrl("http://graph.facebook.com/" + position.getFacebookId() + "/picture?type=square");
                    player.setText(value.getName());
                    hidePanel();
                }
            };
            if (position.getFacebookId() != null && !position.getFacebookId().isEmpty()) {
                player.setText(position.getFacebookId());
            }
            filterList.setSize(200, 200);
            player.setWidth("120px");
            add(player, 3, 48);

            filterList.addOption(new SocialPlayerInfoPanel(SocialStore.getInstance().getNone()), SocialStore.getInstance().getNone(), SocialStore.getInstance().getNone().getName());
            filterList.addOption(new SocialPlayerInfoPanel(SocialStore.getInstance().getMe()), SocialStore.getInstance().getMe(), SocialStore.getInstance().getMe().getName());
            for (SocialFriend friend : SocialStore.getInstance().getFriends()) {
                filterList.addOption(new SocialPlayerInfoPanel(friend), friend, friend.getName());
            }
        } else {
            if (position.getFacebookId() != null && !position.getFacebookId().isEmpty()) {
                if (position.getFacebookId().equals(BaseStore.getInstance().getFacebookId())) {
                    //if this is my game
                    if (position.getPlayerId() == 2) {
                        //if i haven't accepted this game yet.
                        Button button = new Button("Accept Position");
                        button.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(final ClickEvent clickEvent) {
                                service.pickupPosition(BaseStore.getInstance().getScenarioId(), socialGame.getId(), position.getNation().getNationId(), new AsyncCallback<Integer>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        Window.alert("Failed to pickup position");
                                    }

                                    @Override
                                    public void onSuccess(Integer result) {
                                        if (result == 0) {
                                            Window.alert("Position already taken");
                                        } else if (result == -1) {
                                            Window.alert("You already have a nation in this battle");
                                        } else {

                                            if (getParent().getParent() instanceof StandAloneSocialPanel) {
                                                ((StandAloneSocialPanel) (getParent().getParent())).updateMyGames();
                                                ((StandAloneSocialPanel) (getParent().getParent())).updatePending();
                                                ((StandAloneSocialPanel) (getParent().getParent())).updateAvailableGames();
                                            }
                                        }
                                    }
                                });
                            }
                        });
                        add(button, 3, 48);
                    } else {
                        //if i have accepted this game.
                        img.setUrl("http://graph.facebook.com/" + position.getFacebookId() + "/picture?type=square");
                        if (SocialStore.getInstance().getUser(position.getFacebookId()) == null) {
                            SocialStore.getInstance().getUser(position.getFacebookId(), SocialStore.getInstance(), this);
                        } else {
                            nameLbl.setText(SocialStore.getInstance().getUser(position.getFacebookId()).getName());
                        }
                        add(nameLbl, 3, 48);
                    }

                } else {
                    img.setUrl("http://graph.facebook.com/" + position.getFacebookId() + "/picture?type=square");
                    if (SocialStore.getInstance().getUser(position.getFacebookId()) == null) {
                        SocialStore.getInstance().getUser(position.getFacebookId(), SocialStore.getInstance(), this);
                    } else {
                        nameLbl.setText(SocialStore.getInstance().getUser(position.getFacebookId()).getName());
                    }
                    add(nameLbl, 3, 48);
                }

            } else if (!socialGame.isUserPlaying(BaseStore.getInstance().getFacebookId())) {

                Button button = new Button("Play Position");
                button.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(final ClickEvent clickEvent) {
                        service.pickupPosition(BaseStore.getInstance().getScenarioId(), socialGame.getId(), position.getNation().getNationId(), new AsyncCallback<Integer>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                Window.alert("Failed to pickup position");
                            }

                            @Override
                            public void onSuccess(Integer result) {
                                if (result == 0) {
                                    Window.alert("Position already taken");
                                } else if (result == -1) {
                                    Window.alert("You already have a nation in this battle");
                                } else {

                                    Window.alert("Yeee!! picked up position");
                                    if (getParent().getParent() instanceof StandAloneSocialPanel) {
                                        Window.alert("update games");
                                        ((StandAloneSocialPanel) (getParent().getParent())).updateMyGames();
                                        ((StandAloneSocialPanel) (getParent().getParent())).updatePending();
                                        ((StandAloneSocialPanel) (getParent().getParent())).updateAvailableGames();
                                    }
                                }
                            }
                        });
                    }
                });
                add(button, 3, 48);
            }
        }

    }

    /**
     * Use this function to update the name of the owner position
     */
    public void updateLabel() {
        nameLbl.setText(SocialStore.getInstance().getUser(pos.getFacebookId()).getName());
    }

}
