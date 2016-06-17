package com.eaw1805.www.scenario.views.infopanels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.scenario.stores.EditorMapUtils;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.views.EditorPanel;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.fieldbattle.widgets.shared.TextBoxEditable;

/**
 * Info panel that shows information about a baggage train.
 */
public class BaggageTrainInfoPanel
        extends AbsolutePanel
        implements ArmyConstants, GoodConstants,
        RelationConstants, StyleConstants {

    private final BaggageTrainDTO baggageTrain;

    public BaggageTrainInfoPanel(final BaggageTrainDTO train) {
        baggageTrain = train;
        this.setStyleName("baggageInfoPanel");
        this.setSize("366px", "90px");

        final Image image = new Image("http://static.eaw1805.com/images/figures/baggage.png");
        this.add(image, 3, 3);
        image.setSize("", "82px");

        final TextBoxEditable lblBaggageTrainName = new TextBoxEditable("Set Name");
        lblBaggageTrainName.initHandler(new BasicHandler() {
            @Override
            public void run() {
                baggageTrain.setName(lblBaggageTrainName.getText());
            }
        });
        lblBaggageTrainName.setText(train.getName());
        lblBaggageTrainName.setStyleName("clearFontMiniTitle");
        this.add(lblBaggageTrainName, 90, 3);
        lblBaggageTrainName.setSize("249px", "21px");

        //x/y,mps
        final Label lblLocation = new Label(baggageTrain.positionToString());
        lblLocation.setStyleName(CLASS_CLEARFONTSMALL);
        lblLocation.setTitle("Baggage trains position.");
        this.add(lblLocation, 315, 3);
        lblLocation.setSize("47px", SIZE_15PX);

        final Image nationImg = new Image("http://static.eaw1805.com/images/nations/nation-" + baggageTrain.getNationId() + "-120.png");
        nationImg.setHeight("30px");
        add(nationImg, 300, 40);


        final ImageButton deleteImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png", "Delete Baggage train");
        deleteImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //first delete this brigade
                EditorStore.getInstance().getBaggageTrains().get(baggageTrain.getRegionId()).get(baggageTrain.getX()).get(baggageTrain.getY()).remove(baggageTrain.getId());
                //then remove it from map
                EditorMapUtils.getInstance().drawBTrains(baggageTrain.getRegionId());
                //then remove it from parent
                removeFromParent();
                //finally update the army overview
                EditorPanel.getInstance().getSpyBTrainOverView().updateOverview();
            }
        });
        deleteImg.setSize("20px", "20px");
        add(deleteImg, 270, 60);
    }

}
