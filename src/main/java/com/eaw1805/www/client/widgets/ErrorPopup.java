package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;

public class ErrorPopup
        extends PopupPanelEAW implements StyleConstants {

    public enum Level {
        WARNING, ERROR, NORMAL, CALL_ALLIES
    }

    private Label lblWarning;

    public ErrorPopup(final Level lvl, final String errorMessage, final boolean enableReject) {
        this.getElement().getStyle().setZIndex(3000000);
        this.setModal(true);
        final AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setStyleName("popUpPanel");
        setStyleName("none");
        setWidget(absolutePanel);
        absolutePanel.setSize("360px", "160px");

        if (lvl == Level.WARNING) {
            this.lblWarning = new Label("Warning:");
            this.lblWarning.setStyleName("clearFontMedLarge whiteText");
        } else if (lvl == Level.ERROR) {
            this.lblWarning = new Label("Error:");
            this.lblWarning.setStyleName("clearFontMedLarge redText");
        } else if (lvl == Level.NORMAL) {
            this.lblWarning = new Label("Info:");
            this.lblWarning.setStyleName("clearFontMedLarge whiteText");
        } else if (lvl == Level.CALL_ALLIES) {
            this.lblWarning = new Label("Call for allies");
            this.lblWarning.setStyleName("clearFontMedLarge whiteText");
        }
        this.lblWarning.setSize("314px", "27px");
        absolutePanel.add(this.lblWarning, 20, 12);

        final Label lblErrorString = new Label(errorMessage);
        lblErrorString.setStyleName("clearFontMedSmall  whiteText");
        lblErrorString.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblErrorString.setSize("300px", "34px");
        absolutePanel.add(lblErrorString, 15, 62);

        if (enableReject) {
            final Image acceptImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    onAccept();
                    ErrorPopup.this.hide();
                }
            }).addToElement(acceptImg.getElement()).register();

            acceptImg.setStyleName("pointer");
            acceptImg.setTitle("Accept changes");
            acceptImg.setSize(SIZE_36PX, SIZE_36PX);
            absolutePanel.add(acceptImg, 130, 113);

            final Image rejectImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
            rejectImg.setStyleName("pointer");
            rejectImg.setTitle("No cancel action");
            rejectImg.setSize(SIZE_36PX, SIZE_36PX);
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    onReject();
                    ErrorPopup.this.hide();
                }
            }).addToElement(rejectImg.getElement()).register();
            absolutePanel.add(rejectImg, 170, 113);

        } else {
            final Image acceptImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    onAccept();
                    ErrorPopup.this.hide();
                }
            }).addToElement(acceptImg.getElement()).register();

            acceptImg.setStyleName("pointer");
            acceptImg.setTitle("Accept changes");
            acceptImg.setSize(SIZE_36PX, SIZE_36PX);
            absolutePanel.add(acceptImg, 150, 113);
        }

        this.show();
        this.center();

    }

    /**
     * This function is called when user clicks the accept button.
     * <p/>
     * It should be overridden.
     */
    public void onAccept() {
        // empty
    }

    /**
     * This function is called when user clicks the reject button.
     * <p/>
     * It should be overridden.
     */
    public void onReject() {
        // empty
    }

}
