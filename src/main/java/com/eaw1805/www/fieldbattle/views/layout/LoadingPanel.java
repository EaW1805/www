package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.client.ClientConstants;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.SoundStore;
import com.eaw1805.www.fieldbattle.stores.utils.AnimationUtils;
import com.eaw1805.www.fieldbattle.stores.utils.BasicHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class LoadingPanel extends AbsolutePanel {
    final Label loadingDescr;
    final AbsolutePanel mainContainer = new AbsolutePanel();
    List<Image> barImgs = new ArrayList<Image>();
    private static int maxLoadingSteps;

    static {
        if (BaseStore.getInstance().isStartRound()) {
            maxLoadingSteps = 5;
        } else {
            maxLoadingSteps = 5 + (BaseStore.getInstance().getRound() + 1) * 2 + 1;
        }
    }

    private int loadingStep = 0;
    private static int FULL_LOAD_LENGTH = 420;
    private static int SIDE_BAR_LENGTH_LEFT = 35;
    private static int SIDE_BAR_LENGTH_RIGHT = 47;
    private static int MIDDLE_BAR_LENGTH = (FULL_LOAD_LENGTH - SIDE_BAR_LENGTH_LEFT - SIDE_BAR_LENGTH_RIGHT) / (maxLoadingSteps - 2);

    public LoadingPanel() {
        ClientConstants myConstants = GWT.create(ClientConstants.class);
        setSize("100%", "100%");
        getElement().getStyle().setBackgroundColor("black");
        setStyleName("loginPanel");

        mainContainer.setSize("570px", "550px");
        int top = (Window.getClientHeight() - 550) / 2;
        if (top + 550 > Window.getClientHeight()) {
            top = Window.getClientHeight() - 550;
        }
        if (top < 0) {
            top = 0;
        }
        add(mainContainer, (Window.getClientWidth() - 570) / 2, top);


        final Image loadingScreen = new Image("http://static.eaw1805.com/images/loading/LoadingScreen.png");
        mainContainer.add(loadingScreen, -465, -200);

        loadingDescr = new Label("Now loading...");
        loadingDescr.setStyleName("clearFont");
        mainContainer.add(loadingDescr, 68, 315);
        final Label buildNumber = new Label("b" + myConstants.buildNumber());
        mainContainer.add(buildNumber, 460, 315);

        //add random quote
        final Random random = new Random();
        random.setSeed(new Date().getTime());
        int rand = random.nextInt(36) + 1;

        final Label quoteLabel = new Label(myConstants.quotesMap().get("q" + rand));
        quoteLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        quoteLabel.setWidth("415px");
        quoteLabel.setStyleName("quoteText");

        final Label personalityLabel = new Label(myConstants.personalitiesMap().get("p" + rand));
        personalityLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        personalityLabel.setWidth("415px");
        personalityLabel.setStyleName("quoteText");

        final AbsolutePanel quotePanel = new AbsolutePanel();
        quotePanel.setSize("464px", "155px");
        quotePanel.setStyleName("tutorialInfoPanel");
        quotePanel.add(quoteLabel, 23, 9);

        quotePanel.add(personalityLabel, 23, 129);
        mainContainer.add(quotePanel, 52, 364);
    }

    private Image createMiddleImg() {
        Image out = new Image("http://static.eaw1805.com/images/loading/loadingBar7.png");
        out.setSize(MIDDLE_BAR_LENGTH + "px", "20px");
        return out;
    }

    public void addLoadingStep(final String descr) {
        loadingStep++;
        loadingDescr.setText(descr);
        if (loadingStep == 1) {
            final Image img = new Image("http://static.eaw1805.com/images/loading/loadingBar1.png");
            barImgs.add(img);
            mainContainer.add(img, 76, 286);
        } else if (loadingStep != maxLoadingSteps) {
            final Image img = createMiddleImg();
            barImgs.add(img);
            mainContainer.add(img, 111 + (loadingStep - 2) * MIDDLE_BAR_LENGTH, 286);
        } else {
            final Image img = new Image("http://static.eaw1805.com/images/loading/loadingBar11.png");
            barImgs.add(img);
            mainContainer.add(img, 111 + (loadingStep - 2) * MIDDLE_BAR_LENGTH, 286);
        }
        if (loadingStep == maxLoadingSteps) {
            //hide this panel
            setStyleName("disablePointerEvents", true);
            if (BaseStore.getInstance().isGameEnded()) {
                if (BaseStore.getInstance().wonTheBattle()) {
                    SoundStore.getInstance().playVictorySong();
                } else if (BaseStore.getInstance().lostTheBattle()) {
                    SoundStore.getInstance().playDefeatSong();
                } else {
                    SoundStore.getInstance().playDrawSong();
                }
            }

            AnimationUtils.hideElement(this, new BasicHandler() {
                @Override
                public void run() {
                    if (BaseStore.getInstance().isGameEnded()) {
                        final ResultPanel panel = new ResultPanel();
                        MainPanel.getInstance().addToCenter(panel);
                        panel.setVisible(false);

                        AnimationUtils.showElement(panel, new BasicHandler() {
                            @Override
                            public void run() {
                                new Timer() {
                                    @Override
                                    public void run() {
//                                        AnimationUtils.hideElement(panel);
                                    }
                                }.schedule(3000);
                            }
                        });
                    }

                }
            });
        }
    }

    public void onResize() {
        int top = (Window.getClientHeight() - 550) / 2;
        if (top + 550 > Window.getClientHeight()) {
            top = Window.getClientHeight() - 550;
        }
        if (top < 0) {
            top = 0;
        }
        setWidgetPosition(mainContainer, (Window.getClientWidth() - 570) / 2, top);

    }

    public void resetLoading() {
        loadingStep = 0;
        if (BaseStore.getInstance().isStartRound()) {
            maxLoadingSteps = 5;
        } else {
            maxLoadingSteps = 5 + (BaseStore.getInstance().getRound() + 1) * 2 + 1;
        }
        for (Image img : barImgs) {
            img.removeFromParent();
        }
        barImgs.clear();
        removeStyleName("disablePointerEvents");
        setVisible(true);
        getElement().getStyle().setOpacity(1.0d);

    }
}
