package com.eaw1805.www.client.views.frames;


import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.eaw1805.data.dto.common.BattleDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ScrollBarScrollsOverEAW;
import com.eaw1805.www.shared.StaticWidgets;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.SoundStore;

public class BattleFrame extends DraggablePanel {
    private HTMLPanel htmlPanel = null;
    private ScrollBarScrollsOverEAW scrollPanel;
    BattleDTO battle = null;

    public BattleFrame(final int battleId, final boolean isLand, final boolean isEconomic, final boolean isNewsletter, final BattleDTO battle, final boolean CallForAlliesOnExit) {
        this.battle = battle;
        setStyleName("tipPanel");
        this.setSize("1030px", "630px");
        final ImageButton imgX;
        if (isEconomic) {
            imgX = StaticWidgets.CLOSE_IMAGE_ECONOMY_REPORTS;
        } else {
            imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        }
        final String domain = "http://www.eaw1805.com";
//        final String domain = "http://localhost:8080/empire-web";
        final String urlPrefix = domain + "/report/scenario/" + GameStore.getInstance().getScenarioStr() + "/game/" + GameStore.getInstance().getGameId() +
                "/nation/" + GameStore.getInstance().getNationId() +
                "/";

        if (isEconomic) {
            requestPage(urlPrefix + "overview?eawClient=1");

        } else if (isNewsletter) {
            requestPage(urlPrefix + "newsletter?eawClient=1");

        } else if (isLand) {
            requestPage(urlPrefix + "battle/" + battleId + "?eawClient=1");

        } else {
            requestPage(urlPrefix + "naval/" + battleId + "?eawClient=1");
        }





        if (isEconomic) {
            int posX = 30;

            final ImageButton overviewImg = new ImageButton("http://static.eaw1805.com/images/panels/reports/ButHorOverviewOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    requestPage(urlPrefix + "overview?eawClient=1");
                    overviewImg.deselect();
                }
            }).addToElement(overviewImg.getElement()).register();

            overviewImg.setSize("150px", "");
            add(overviewImg, posX, 17);

            posX += 160;

            final ImageButton ordersImg = new ImageButton("http://static.eaw1805.com/images/panels/reports/ButHorTextOrdersOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    requestPage(urlPrefix + "orders/" + GameStore.getInstance().getTurn() + "?eawClient=1");
                    ordersImg.deselect();
                }
            }).addToElement(ordersImg.getElement()).register();

            ordersImg.setSize("150px", "");
            add(ordersImg, posX, 17);

            posX += 160;


            final ImageButton economyImg = new ImageButton("http://static.eaw1805.com/images/panels/reports/ButHorTextEconomyOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    requestPage(urlPrefix + "economy?eawClient=1");
                    economyImg.deselect();
                }
            }).addToElement(economyImg.getElement()).register();

            economyImg.setSize("150px", "");
            add(economyImg, posX, 17);

            posX += 160;

            final ImageButton productionImg = new ImageButton("http://static.eaw1805.com/images/panels/reports/ButHorTextProductionSitesOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    requestPage(urlPrefix + "economy/production?eawClient=1");
                    productionImg.deselect();
                }
            }).addToElement(productionImg.getElement()).register();

            productionImg.setSize("150px", "");
            add(productionImg, posX, 17);
            posX += 160;

            final ImageButton battleImg = new ImageButton("http://static.eaw1805.com/images/panels/reports/ButHorTextBattleReportsOn.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    requestPage(urlPrefix + "battles?eawClient=1");
                    battleImg.deselect();
                }
            }).addToElement(battleImg.getElement()).register();

            battleImg.setSize("150px", "");
            add(battleImg, posX, 17);
            posX += 160;


            final ImageButton landForcesImg = new ImageButton("http://static.eaw1805.com/images/panels/reports/ButHorTextLandForcesOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    requestPage(urlPrefix + "army?eawClient=1");
                    landForcesImg.deselect();
                }
            }).addToElement(landForcesImg.getElement()).register();

            landForcesImg.setSize("150px", "");
            add(landForcesImg, posX, 17);

            posX += 160;

            final ImageButton navalForcesImg = new ImageButton("http://static.eaw1805.com/images/panels/reports/ButHorTextNavalForcesOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    requestPage(urlPrefix + "navy?eawClient=1");
                    navalForcesImg.deselect();
                }
            }).addToElement(navalForcesImg.getElement()).register();

            navalForcesImg.setSize("150px", "");
            add(navalForcesImg, 30, 46);
        }


        imgX.setStyleName("pointer");
        imgX.setTitle("Close report");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(BattleFrame.this);
                if (CallForAlliesOnExit) {
                    GameStore.getInstance().showCallForAllies(0);
                }
                imgX.deselect();
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 7) {
                    TutorialStore.nextStep(false);
                }
                if (isEconomic
                        && TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 4
                        && TutorialStore.getInstance().getTutorialStep() == 4) {
                    TutorialStore.nextStep(false);
                }
            }
        }).addToElement(imgX.getElement()).register();

        this.add(imgX, 1000, 17);
        imgX.setSize("36px", "36px");
    }

    public void reInitHtmlPanel(final String html) {
        try {
            if (htmlPanel != null) {
                htmlPanel.removeFromParent();
            }
        } catch (Exception ignore) {
        }
        try {
            if (scrollPanel != null) {
                scrollPanel.removeFromParent();
            }
        } catch (Exception ignore) {
        }

        htmlPanel = new HTMLPanel(html);
        htmlPanel.setStyleName("battleContainer");

        scrollPanel = new ScrollBarScrollsOverEAW(htmlPanel, 50);
        scrollPanel.setSize(1050, 575, 18);
        BattleFrame.this.add(scrollPanel, -9, 56);
    }

    public void requestPage(final String url) {
        final RequestBuilder request = new RequestBuilder(RequestBuilder.GET, url);
        request.setCallback(new RequestCallback() {
            public void onResponseReceived(Request request, Response response) {
                reInitHtmlPanel(response.getText());
                runJavascript(response.getText());
                if (battle != null) {
                    if (battle.getSide() == -1) {
                        if (battle.getBattleType() == 1) {
                            SoundStore.getInstance().playBattleLand();
                        } else {
                            SoundStore.getInstance().playBattleSea();
                        }
                    } else if (battle.getSide() == battle.getWinner()) {
                        SoundStore.getInstance().playVictory();
                    } else if (battle.getWinner() == 0) {
                        SoundStore.getInstance().playDraw();
                    } else {
                        SoundStore.getInstance().playDefeat();
                    }
                }
                //try to run the javascript that came with the response text

            }

            public void onError(Request request, Throwable throwable) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to retrive data", false);
//                Window.alert("error : " + throwable.getMessage());
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        try {
            request.send();
        } catch (Exception e) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to send request", false);
//            Window.alert("failed to send " + e);
        }
    }

    native void runJavascript(final String str) /*-{
        $wnd.parseScript(str);

    }-*/;

    native void debugOnConsole(final String str) /*-{
        console.log(str);

    }-*/;
}
