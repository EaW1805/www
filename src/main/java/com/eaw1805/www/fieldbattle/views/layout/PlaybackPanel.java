package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.PlaybackStore;
import com.eaw1805.www.fieldbattle.tooltips.Tips;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.Slider;
import com.eaw1805.www.fieldbattle.widgets.ValueChangeEvent;
import com.eaw1805.www.fieldbattle.widgets.ValueChangeHandler;

import java.util.Date;

public class PlaybackPanel extends AbsolutePanel {
    private Slider slider = new Slider();
    private boolean playing = false;
    boolean playFirst = false;
    //    String host = "http://localhost";
    String host = "http://static.eaw1805.com";

    public PlaybackPanel() {


        final Timer timer = new Timer() {

            @Override
            public void run() {
                if (playFirst) {
                    playFirst = false;
                    slider.setValue(0);
                    slider.executeHandlers();
                    schedule(2000);
                }
                if (slider.hasNextStep()) {
                    slider.nextStep();
                    schedule(2000);
                }
            }
        };
        setSize("614px", "57px");
        setStyleName("playbackPanel");

        final ImageButton previousButton = new ImageButton(host + "/images/field/panels/playback/PreviousOff.png", Tips.PLAYBACK_PREVIOUS);
        previousButton.setSize("20px", "20px");
        previousButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (slider.hasPreviousStep()) {
                    slider.previousStep();
                }
            }
        });

        final ImageButton playButton = new ImageButton(host + "/images/field/panels/playback/PlayOff.png", Tips.PLAYBACK_PLAY);
        playButton.setSize("20px", "20px");
        playButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                playing = !playing;
                if (playing) {
                    timer.schedule(2000);
                    playButton.setUrl(host + "/images/field/panels/playback/PauseOff.png");
                    playFirst = true;

                } else {
                    timer.cancel();
//                    slider.setValue(0);
                    playButton.setUrl(host + "/images/field/panels/playback/PlayOff.png");

                }
            }
        });

        final ImageButton nextButton = new ImageButton(host + "/images/field/panels/playback/NextOff.png", Tips.PLAYBACK_NEXT);
        nextButton.setSize("20px", "20px");
        nextButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (slider.hasNextStep()) {
                    slider.nextStep();
                }
            }
        });

        slider.setSize(400, 40);
        final Label valueLbl = new Label("0");
        valueLbl.setStyleName("whiteText");
        slider.addValueChangedHandler(new ValueChangeHandler() {
            @Override
            public void onChange(ValueChangeEvent event) {
                valueLbl.setText("value is " + event.getValue());
                try {
                    MainPanel.getInstance().getPlaybackInfo().updateInfo(event.getValue() - 1, true);
                    MainPanel.getInstance().updateNationInfoPanels(event.getValue() - 1);
                    MainPanel.getInstance().getMapUtils().showRoundBrigades(event.getValue() - 1);
                    MainPanel.getInstance().getMapUtils().showPlaybackAdditionsByRound(event.getValue() - 1);
                } catch (Exception e) {
                    Window.alert("mapupdate " + e.toString());
                }
                valueLbl.setText(PlaybackStore.getInstance().getRoundName(event.getValue() - 1));
//                MainPanel.getInstance().getArmyInfo().debugText.setText(measures);
//                Window.alert("Total time? " + measures);
            }
        });
        add(previousButton, 17, 17);
        add(playButton, 39, 17);
        add(nextButton, 61, 17);
        add(slider, 84, 17);
        add(valueLbl, 504, 17);


    }

    public int getRound() {
        if (BaseStore.getInstance().isStartRound()) {
            return 0;
        } else {
            return slider.getValue();
        }
    }

    public void goToRound(int round) {
        slider.setValue(round);
        slider.executeHandlers();
    }

    public void nextStep() {
        if (slider.hasNextStep()) {
            slider.nextStep();
        }
    }

    public void updateSliderValues(int minValue, int maxValue, int step) {
        slider.setMinValue(minValue).setMaxValue(maxValue).setStep(step);
    }
}
