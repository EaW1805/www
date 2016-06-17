package com.eaw1805.www.client.views.tutorial;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.GameStore;

public class TutorialInfoPanel extends AbsolutePanel {

    private final Label infoLabel;
    private final Label countLabel;
    public enum ScreenPosition {
        CENTER,
        RIGHT,
        CUSTOM
    }

    int customX;
    int customY;
    ScreenPosition position;
    final ImageButton imgX, previousStep, nextStep;
    private int totalSteps = 0;
    private Label title;
    private Label part;
//    final Timer opener;
//    final Timer closer;
    final static int PANE_WIDTH = 600;
    boolean opened = true;
    public TutorialInfoPanel() {
        getElement().getStyle().setZIndex(1000000);//be sure always on top cause it will be complex
        setSize(PANE_WIDTH + "px", "500px");
        setStyleName("tutorialInfoPanel");

        final ImageButton toggleButton = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchOff.png");
        toggleButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                int y = GameStore.getInstance().getLayoutView().getWidgetY(TutorialInfoPanel.this);
                if (opened) {
                    opened = false;
                    GameStore.getInstance().getLayoutView().resetTutorialPosition();
                } else {
                    opened = true;
                    GameStore.getInstance().getLayoutView().resetTutorialPosition();
                }
                toggleButton.deselect();
            }
        });
        add(toggleButton, 10, 10);

        final Image commander = new Image("http://static.eaw1805.com/img/commanders/s-1/5/1.jpg");
        add(commander, 60, 30);
        commander.setSize("130px", "");

        final Label commandersName = new Label("Napoleon Bonaparte");
        commandersName.setStyleName("clearFontMedLarge");
        add(commandersName, 206, 31);

        title = new Label();
        title.setWidth("350px");
        title.setStyleName("clearFontMiniTitle");
        add(title, 206, 100);

        part = new Label();
        part.setWidth("350px");
        part.setStyleName("clearFontMiniTitle");
        add(part, 206, 66);



        countLabel = new Label();
        countLabel.setStyleName("clearFontMedLarge");
        add(countLabel, 270, 139);

        infoLabel = new Label();
        infoLabel.setWidth("560px");
        infoLabel.setStyleName("clearFontMiniTitleJustified");
        add(infoLabel, 20, 184);

        imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        imgX.setTitle("Click to continue");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                TutorialStore.nextStep(false);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        add(imgX, 272, 450);
        imgX.setSize("36px", "36px");

        nextStep = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                TutorialStore.nextStep(true);
                nextStep.deselect();
            }
        }).addToElement(nextStep.getElement()).register();
        nextStep.setSize("36px","36px");
        add(nextStep, 332, 450);
        nextStep.setTitle("Next step.");
        previousStep = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                TutorialStore.previousStep(true);
                previousStep.deselect();
            }
        }).addToElement(previousStep.getElement()).register();
        previousStep.setSize("36px","36px");
        add(previousStep, 212, 450);
        previousStep.setTitle("Previous step.");


        position = ScreenPosition.CENTER;
        setVisible(false);

        add(new SkipTutorialPanel(), 5, 467);

    }



    public void setTotalSteps(final int value) {
        this.totalSteps = value;
    }

    public void setPartAndTitle(final String partStr, final String titleStr) {
        part.setText(partStr);
        title.setText(titleStr);
    }

    public void updateInfo(final String info, final int step) {
        infoLabel.setText(info);
        countLabel.setText(step + " / " + totalSteps);
        previousStep.setVisible(TutorialStore.getInstance().getTutorialStep() > 1);
        nextStep.setVisible(TutorialStore.getInstance().getTutorialStep() < TutorialStore.getInstance().getMaxStepSoFar());
    }

    public ScreenPosition getPosition() {
        return position;
    }

    public void setPosition(ScreenPosition position) {
        opened=true;
        this.position = position;
        GameStore.getInstance().getLayoutView().resetTutorialPosition();
    }

    public void setPosition(ScreenPosition position, int x, int y) {
        opened= true;
        this.position = position;
        customX = x;
        customY = y;
        GameStore.getInstance().getLayoutView().resetTutorialPosition();
    }

    public int getCustomX() {
        return customX;
    }

    public void setCustomX(int customX) {
        this.customX = customX;
    }

    public int getCustomY() {
        return customY;
    }

    public void setCustomY(int customY) {
        this.customY = customY;
    }

    public boolean isWindowMaximized() {
        return opened;
    }

    public void setVisible(final boolean panelVisible, final boolean nextTurnVisible) {
        setVisible(panelVisible);
        imgX.setVisible(nextTurnVisible);
    }
}
