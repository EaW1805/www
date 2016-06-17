package com.eaw1805.www.fieldbattle.views.layout.social;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.SocialStore;
import com.eaw1805.www.fieldbattle.stores.SoundStore;
import com.eaw1805.www.fieldbattle.stores.utils.AnimationUtils;
import com.eaw1805.www.fieldbattle.stores.utils.BasicHandler;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import com.eaw1805.www.fieldbattle.views.layout.ResultPanel;

import java.util.ArrayList;
import java.util.List;


public class SocialLoadingPanel extends PopupPanel {
    final Label loadingDescr;
    AbsolutePanel loader = new AbsolutePanel();

    List<Image> barImgs = new ArrayList<Image>();
    private static int maxLoadingSteps = 3;


    private int loadingStep = 0;
    private static int FULL_LOAD_LENGTH = 420;
    private static int SIDE_BAR_LENGTH_LEFT = 35;
    private static int SIDE_BAR_LENGTH_RIGHT = 47;
    private static int MIDDLE_BAR_LENGTH = (FULL_LOAD_LENGTH - SIDE_BAR_LENGTH_LEFT - SIDE_BAR_LENGTH_RIGHT) / (maxLoadingSteps - 2);


    private static SocialLoadingPanel instance;

    public static SocialLoadingPanel getInstance() {
        if (instance == null) {
            instance = new SocialLoadingPanel();
        }
        return instance;
    }


    private SocialLoadingPanel() {
        setStyleName("none");
        getElement().getStyle().setZIndex(10002);
        loader.setSize(Window.getClientWidth() + "px", Window.getClientHeight() + "px");
        setWidget(loader);
        final Image imgLogo = new Image("http://static.eaw1805.com/images/loading/LoadingScreen.png");
        imgLogo.setSize("1440px", "900px");
        loader.add(imgLogo, (Window.getClientWidth() - 1440)/2, (Window.getClientHeight() - 900)/2);
        loadingDescr = new Label("Now loading...");
        loadingDescr.setStyleName("clearFont");
        int x = 463 + (Window.getClientWidth() - 1440)/2;
        int y = 200 + (Window.getClientHeight() - 900)/2;
        loader.add(loadingDescr, x + 68, y + 315);
    }

    public void load() {
        new Timer() {

            @Override
            public void run() {
                if (SocialStore.getInstance().getMe().getId() == null) {
                    SocialStore.getInstance().initPersonalData(SocialStore.getInstance());

                } else {
                    cancel();
                }

            }
        }.scheduleRepeating(2000);


    }

    private Image createMiddleImg() {
        Image out = new Image("http://static.eaw1805.com/images/loading/loadingBar7.png");
        out.setSize(MIDDLE_BAR_LENGTH + "px", "20px");
        return out;
    }

    public void nextStep(String descr) {
        loadingStep++;
        loadingDescr.setText(descr);
        int x = 463 + (Window.getClientWidth() - 1440)/2;
        int y = 200 + (Window.getClientHeight() - 900)/2;
        if (loadingStep == 1) {
            final Image img = new Image("http://static.eaw1805.com/images/loading/loadingBar1.png");
            barImgs.add(img);
            loader.add(img, x + 76, y + 286);
        } else if (loadingStep != maxLoadingSteps) {
            final Image img = createMiddleImg();
            barImgs.add(img);
            loader.add(img, x + 111 + (loadingStep - 2) * MIDDLE_BAR_LENGTH, y + 286);
        } else {
            final Image img = new Image("http://static.eaw1805.com/images/loading/loadingBar11.png");
            barImgs.add(img);
            loader.add(img, x + 111 + (loadingStep - 2) * MIDDLE_BAR_LENGTH, y + 286);
        }
        if (loadingStep == maxLoadingSteps) {
            //hide this panel
            setStyleName("disablePointerEvents", true);


            AnimationUtils.hideElement(this, new BasicHandler() {
                @Override
                public void run() {
                    SocialLoadingPanel.this.hide();
                    StandAloneSocialPanel.getInstance().setVisible(true);
                    MainPanel.getInstance().positionToCenter(StandAloneSocialPanel.getInstance());
                }
            });
        }
    }



}
