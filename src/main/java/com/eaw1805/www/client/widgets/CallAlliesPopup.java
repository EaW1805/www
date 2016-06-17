package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RelationsStore;

public class CallAlliesPopup extends PopupPanelEAW implements StyleConstants {

    private final int call;

    public CallAlliesPopup(final int reporter, final int reported, final int call) {
        this.call = call;
        this.getElement().getStyle().setZIndex(3000000);
        this.setModal(true);
        final AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setStyleName("popUpPanel");
        setStyleName("none");
        setWidget(absolutePanel);
        absolutePanel.setSize("351px", "210px");

        final Label lblWarning = new Label("Call for allies");
        lblWarning.setStyleName("clearFontMedLarge whiteText");
        absolutePanel.add(lblWarning, 20, 14);
        lblWarning.setSize("314px", "27px");


        final Label lblErrorString = new Label(DataStore.getInstance().getNationNameByNationId(reporter) + " called us to join the war against " +
                DataStore.getInstance().getNationNameByNationId(reported) +
                ". We have to decide this month if we will join forces! Do you want to join forces?");
        lblErrorString.setStyleName("clearFontMedSmall  whiteText");
        lblErrorString.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        absolutePanel.add(lblErrorString, 27, 60);
        lblErrorString.setSize("300px", "34px");

        final Image reporterImg = new Image("http://static.eaw1805.com/images/nations/nation-" + reporter + "-36.png");
        final Image reportedImg = new Image("http://static.eaw1805.com/images/nations/nation-" + reported + "-36.png");

        final Label allyLbl = new Label("Ally : ");
        allyLbl.setStyleName("clearFontMedSmall  whiteText");
        final Label warLbl = new Label("Declare War : ");
        warLbl.setStyleName("clearFontMedSmall  whiteText");
        absolutePanel.add(allyLbl, 27, 128);
        absolutePanel.add(reporterImg, 67, 123);
        absolutePanel.add(warLbl, 127, 128);
        absolutePanel.add(reportedImg, 216, 123);

        final Image acceptImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                RelationsStore.getInstance().changeNationRelationship(reported, RelationConstants.REL_WAR, RelationConstants.NO_ACTION);
                CallAlliesPopup.this.hide();
            }
        }).addToElement(acceptImg.getElement()).register();

        acceptImg.setStyleName("pointer");
        acceptImg.setTitle("Accept changes");
        absolutePanel.add(acceptImg, 135, 156);
        acceptImg.setSize(SIZE_36PX, SIZE_36PX);

        final Image rejectImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        rejectImg.setStyleName("pointer");
        rejectImg.setTitle("No cancel action");
        absolutePanel.add(rejectImg, 180, 156);
        rejectImg.setSize(SIZE_36PX, SIZE_36PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                CallAlliesPopup.this.hide();
            }
        }).addToElement(rejectImg.getElement()).register();


        this.show();
        this.center();



    }

    public void onDetach() {
        GameStore.getInstance().showCallForAllies(call + 1);
    }

}
