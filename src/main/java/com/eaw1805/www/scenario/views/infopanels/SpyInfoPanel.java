package com.eaw1805.www.scenario.views.infopanels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.scenario.stores.EditorMapUtils;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.views.EditorPanel;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.fieldbattle.widgets.shared.TextBoxEditable;


public class SpyInfoPanel
        extends AbsolutePanel
        implements ArmyConstants, StyleConstants {

    public SpyInfoPanel(final SpyDTO thisSpy) {

        setStyleName("spyInfoPanel");
        setSize("366px", "90px");
        final Image spyImg = new Image("http://static.eaw1805.com/images/buttons/icons/spy.png");
        add(spyImg, 3, 3);
        spyImg.setSize("", "82px");

        final TextBoxEditable lblSpyName = new TextBoxEditable("Set name");
        lblSpyName.setText(thisSpy.getName());
        lblSpyName.initHandler(new BasicHandler() {
            @Override
            public void run() {
                thisSpy.setName(lblSpyName.getText());
            }
        });
        lblSpyName.setStyleName("clearFontMiniTitle");
        lblSpyName.setSize("155px", SIZE_20PX);
        add(lblSpyName, 90, 3);

        final Label lblLocation = new Label(thisSpy.positionToString());
        lblLocation.setTitle("Spy position.");
        lblLocation.setStyleName(CLASS_CLEARFONTSMALL);
        lblLocation.setSize("47px", SIZE_15PX);
        add(lblLocation, 315, 3);


        final Image nationImg = new Image("http://static.eaw1805.com/images/nations/nation-" + thisSpy.getNationId() + "-120.png");
        nationImg.setHeight("30px");
        add(nationImg, 300, 40);

        final ImageButton deleteImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png", "Delete spy");
        deleteImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //first delete this brigade
                EditorStore.getInstance().getSpies().get(thisSpy.getRegionId()).get(thisSpy.getX()).get(thisSpy.getY()).remove(thisSpy.getSpyId());
                //then remove it from map
                EditorMapUtils.getInstance().drawSpies(thisSpy.getRegionId());
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
