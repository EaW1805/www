package com.eaw1805.www.scenario.views.infopanels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.army.*;
import com.eaw1805.www.fieldbattle.stores.utils.ArmyUnitInfoDTO;
import com.eaw1805.www.fieldbattle.stores.utils.calculators.MiscCalculators;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.ToolTipPanel;
import com.eaw1805.www.scenario.stores.ArmyUtils;
import com.eaw1805.www.scenario.views.EditorPanel;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.fieldbattle.widgets.shared.TextBoxEditable;
import com.eaw1805.www.scenario.widgets.popups.UnitViewerPopup;
import com.eaw1805.www.shared.stores.GameStore;


public class ArmyInfoPanel
        extends AbsolutePanel
        implements ArmyConstants, StyleConstants {

    private final AbsolutePanel mainPanel = new AbsolutePanel();
    private ArmyDTO army = new ArmyDTO();
    private final Label lblXCorps, lblYBrigades, lblZBattalions, lblInfantryno, lblCavalryno, lblArtilleryno, lblXArmies;

    private ArmyUnitInfoDTO armyInf;
    private final Label lblMps;
    final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();



    public ArmyInfoPanel(final ArmyDTO army) {
        setArmy(army);
        int leftFactor = 0;
        if (army.getArmyId() == 0) {
            leftFactor = 80;
        }

        this.setStyleName("armyInfoPanel");
        this.setSize("366px", "90px");
        mainPanel.setStyleName("clickArmyPanel");
        this.add(mainPanel);
        mainPanel.setSize("363px", "87px");

        final TextBoxEditable lblArmyName = new TextBoxEditable("Army Name");
        lblArmyName.setText(army.getName());
        lblArmyName.initHandler(new BasicHandler() {
            @Override
            public void run() {
                army.setName(lblArmyName.getText());
            }
        });

        mainPanel.add(lblArmyName, 90 - leftFactor, 3);



        lblXArmies = new Label("x Armies");
        lblXArmies.setStyleName(CLASS_CLEARFONTSMALL);




        final Image corpImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/corps.png");
        corpImg.setTitle("Corps");
        mainPanel.add(corpImg, 90 - leftFactor, 28);
        corpImg.setSize("", SIZE_15PX);
        corpImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new UnitViewerPopup<CorpDTO>(army.getCorps().values()) {

                    @Override
                    public Widget getUnitWidget(CorpDTO unit) {
                        return new CorpsInfoPanel(unit);
                    }
                }.showRelativeTo(corpImg);
            }
        });


        lblXCorps = new Label("x Corps");
        lblXCorps.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblXCorps, 118 - leftFactor, 28);


        final Image brigImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/brigade.png");
        brigImg.setTitle("Brigades");
        mainPanel.add(brigImg, 90 - leftFactor, 45);
        brigImg.setSize("", SIZE_15PX);

        lblYBrigades = new Label("y Brigades");
        lblYBrigades.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblYBrigades, 118 - leftFactor, 45);


        final Image batImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/battalion.png");
        batImg.setTitle("Batttalions");
        mainPanel.add(batImg, 90 - leftFactor, 62);

        lblZBattalions = new Label("z Battalions");
        lblZBattalions.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblZBattalions, 118 - leftFactor, 62);
        lblZBattalions.setSize("84px", SIZE_15PX);


        //battalion types counters
        final Image infantryImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/infantry.png");
        infantryImg.setTitle("Infantry");
        mainPanel.add(infantryImg, 160 - leftFactor, 28);
        infantryImg.setSize("25px", SIZE_15PX);

        lblInfantryno = new Label("InfantryNo");
        lblInfantryno.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblInfantryno, 188 - leftFactor, 28);

        final Image cavalryImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/cavalry.png");
        cavalryImg.setTitle("Cavalry");
        mainPanel.add(cavalryImg, 160 - leftFactor, 46);
        cavalryImg.setSize("25px", SIZE_15PX);

        lblCavalryno = new Label("CavalryNo");
        lblCavalryno.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblCavalryno, 188 - leftFactor, 46);

        final Image artilleryImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/artillery.png");
        artilleryImg.setTitle("Artillery");
        mainPanel.add(artilleryImg, 160 - leftFactor, 64);
        artilleryImg.setSize("25px", SIZE_15PX);

        lblArtilleryno = new Label("ArtilleryNo");
        lblArtilleryno.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblArtilleryno, 188 - leftFactor, 64);

        lblMps = new Label(MiscCalculators.getArmyMps(army) + " MPs");
        lblMps.setTitle("Movement points");
        lblMps.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblMps, 315, 20);

        final ImageButton deleteImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png", "Delete brigade");
        deleteImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //first delete this brigade
                ArmyUtils.deleteArmy(army);
                //then remove it from parent
                removeFromParent();
                //finally update the army overview
                EditorPanel.getInstance().getArmyOverView().updateOverview();
            }
        });
        deleteImg.setSize("20px", "20px");
        add(deleteImg, 270, 60);

        setUpLabels();
    }


    private void setUpLabels() {


        armyInf = MiscCalculators.getArmyInfo(army);



        lblXCorps.setText(numberFormat.format(armyInf.getCorps()));


        lblYBrigades.setText(numberFormat.format(armyInf.getBrigades()));
        lblZBattalions.setText(numberFormat.format(armyInf.getBattalions()));
        lblInfantryno.setText(numberFormat.format(armyInf.getInfantry()));
        lblCavalryno.setText(numberFormat.format(armyInf.getCavalry()));
        lblArtilleryno.setText(numberFormat.format(armyInf.getArtillery()));

        final Image commanderImg;
        if (army.getCommander() != null && army.getCommander().getId() != 0) {
            final int imageId;
            if (army.getCommander().getIntId() > 10) {
                imageId = 0;

            } else {
                imageId = army.getCommander().getIntId();
            }
            commanderImg = new Image("http://static.eaw1805.com/img/commanders/s" + GameStore.getInstance().getScenarioId()
                    + "/" + army.getCommander().getNationId() + "/" + imageId + ".jpg");
            new ToolTipPanel(commanderImg, true) {
                @Override
                public void generateTip() {
                    setTooltip(new CommanderInfoPanel(army.getCommander()));
                }
            };
        } else {
            commanderImg = new Image("http://static.eaw1805.com/images/armies/dominant/comUnknown.png");
        }
        if (army.getArmyId() != 0) {
            mainPanel.add(commanderImg, 3, 3);
        }
        commanderImg.setSize("", "82px");

        lblMps.setText(MiscCalculators.getArmyMps(army) + " MPs");



    }


    public final void setArmy(final ArmyDTO army) {
        this.army = army;
    }

    public final ArmyDTO getArmy() {
        return army;
    }


}
