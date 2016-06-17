package com.eaw1805.www.client.views.infopanels.units.mini;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.util.ArmyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;


public class ArmyInfoMini extends VerticalPanel implements ArmyConstants {

    private final ClickAbsolutePanel armyInfoPanel = new ClickAbsolutePanel();
    private ArmyDTO army = new ArmyDTO();
    private Label lblXCorps, lblYBrigades, lblZBattalions, lblInfantryno, lblCavalryno, lblArtilleryno, lblArmyName;
    private Label lblCommAss = new Label("");
    private final UnitChangedHandler unitChangedHandler;

    public ArmyInfoMini(final ArmyDTO army, final boolean enabled) {
        setArmy(army);
        this.setStyleName("generalMiniInfoPanel");
        setSize("170px", "89px");
        armyInfoPanel.setStyleName("clickArmyPanel");
        add(armyInfoPanel);
        armyInfoPanel.setSize("170px", "89px");

        if (getArmy().getArmyId() != 0) {
            lblArmyName = new RenamingLabel("Army Name", ARMY, getArmy().getArmyId());
        } else {
            lblArmyName = new Label();
        }
        lblArmyName.setStyleName("clearFontMedSmall");
        lblArmyName.setStyleName("editName", true);
        armyInfoPanel.add(lblArmyName, 36, 3);
        this.lblArmyName.setSize("132px", "15px");

        lblXCorps = new Label("x Corps");
        lblXCorps.setStyleName("clearFontSmall");
        armyInfoPanel.add(lblXCorps, 28, 40);
        this.lblXCorps.setSize("25px", "15px");

        lblYBrigades = new Label("y Brigades");
        lblYBrigades.setStyleName("clearFontSmall");
        armyInfoPanel.add(lblYBrigades, 28, 56);
        this.lblYBrigades.setSize("25px", "15px");

        lblZBattalions = new Label("z Battalions");
        lblZBattalions.setStyleName("clearFontSmall");
        armyInfoPanel.add(lblZBattalions, 28, 72);
        this.lblZBattalions.setSize("25px", "15px");

        final Image infantryImg = new Image("http://static.eaw1805.com/images/armies/dominant/infantry.png");
        armyInfoPanel.add(infantryImg, 59, 40);
        infantryImg.setSize("25px", "15px");

        final Image cavalryImg = new Image("http://static.eaw1805.com/images/armies/dominant/cavalry.png");
        armyInfoPanel.add(cavalryImg, 59, 56);
        cavalryImg.setSize("25px", "15px");

        final Image artilleryImg = new Image("http://static.eaw1805.com/images/armies/dominant/artillery.png");
        armyInfoPanel.add(artilleryImg, 59, 72);
        artilleryImg.setSize("25px", "15px");

        lblInfantryno = new Label("InfantryNo");
        lblInfantryno.setStyleName("clearFontSmall");
        armyInfoPanel.add(lblInfantryno, 86, 40);
        this.lblInfantryno.setSize("50px", "15px");

        lblCavalryno = new Label("CavalryNo");
        lblCavalryno.setStyleName("clearFontSmall");
        armyInfoPanel.add(lblCavalryno, 86, 56);
        this.lblCavalryno.setSize("50px", "15px");

        lblArtilleryno = new Label("ArtilleryNo");
        lblArtilleryno.setStyleName("clearFontSmall");
        armyInfoPanel.add(lblArtilleryno, 86, 72);
        this.lblArtilleryno.setSize("50px", "15px");

        final Image corpsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/corps.png");
        corpsImg.setTitle("Corps");
        armyInfoPanel.add(corpsImg, 3, 40);
        corpsImg.setSize("25px", "15px");

        final Image battalionsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/battalion.png");
        battalionsImg.setTitle("Battalions");
        armyInfoPanel.add(battalionsImg, 3, 72);
        battalionsImg.setSize("25px", "15px");

        final Image brigadesImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/brigade.png");
        brigadesImg.setTitle("Brigades");
        armyInfoPanel.add(brigadesImg, 3, 56);
        brigadesImg.setSize("25px", "15px");

        if (!enabled) {
            final Image disabledImage = new Image("http://static.eaw1805.com/images/infopanels/disabledMini.png");
            disabledImage.setTitle("Further orders are disabled due to orders already issued that may be in conflict");
            armyInfoPanel.add(disabledImage);
        }

        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {
                if (event.getInfoType() == ARMY && event.getInfoId() == army.getArmyId()) {
                    setUpLabels();
                } else if (event.getInfoType() == COMMANDER) {
                    if (army.getCommander() != null) {
                        setUpLabels();
                    }
                }
            }
        };
        setUpLabels();
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void onAttach() {
        super.onAttach();
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    private void setUpLabels() {
        final ArmyUnitInfoDTO armyInf = MiscCalculators.getArmyInfo(army);
        if (!getArmy().getCorps().isEmpty() && getArmy().getCorps().values().iterator().next().getArmyId() == 0) {
            getLblArmyName().setText("Corps in no army.");
        } else if (getArmy() != null && !getArmy().getName().equals("")) {
            getLblArmyName().setText(getArmy().getName());
        } else {
            getLblArmyName().setText("Click to set name");
        }
        getLblXCorps().setText(String.valueOf(armyInf.getCorps()));
        getLblYBrigades().setText(String.valueOf(armyInf.getBrigades()));
        getLblZBattalions().setText(String.valueOf(armyInf.getBattalions()));
        getLblInfantryno().setText(String.valueOf(armyInf.getInfantry()));
        getLblCavalryno().setText(String.valueOf(armyInf.getCavalry()));
        getLblArtilleryno().setText(String.valueOf(armyInf.getArtillery()));

        final Image commanderImg;
        if (army.getCommander() != null && army.getCommander().getId() != 0) {
            final int imageId = army.getCommander().getIntId();
            commanderImg = new Image("http://static.eaw1805.com/img/commanders/s"
                    + GameStore.getInstance().getScenarioId() + "/"
                    + army.getCommander().getNationId() + "/" + imageId + ".jpg");
            lblCommAss.setText("Commander:" + army.getCommander().getName());
            new ToolTipPanel(commanderImg) {
                @Override
                public void generateTip() {
                    setTooltip(new CommanderInfoMini(army.getCommander()));
                }
            };
        } else {
            commanderImg = new Image("http://static.eaw1805.com/images/armies/dominant/comUnknown.png");
            lblCommAss.setText("Commander:none");
            lblCommAss.setStyleName("clearFontMini");
        }
        lblCommAss.setStyleName("clearFontMini");
        armyInfoPanel.add(lblCommAss, 38, 25);
        armyInfoPanel.add(commanderImg, 2, 2);
        commanderImg.setSize("30px", "30px");

    }

    public void setArmy(final ArmyDTO army) {
        this.army = army;
    }

    public ArmyDTO getArmy() {
        return army;
    }

    public ClickAbsolutePanel getArmyInfoPanel() {
        return armyInfoPanel;
    }

    public Label getLblXCorps() {
        return lblXCorps;
    }

    public Label getLblYBrigades() {
        return lblYBrigades;
    }

    public Label getLblZBattalions() {
        return lblZBattalions;
    }

    public Label getLblInfantryno() {
        return lblInfantryno;
    }

    public Label getLblCavalryno() {
        return lblCavalryno;
    }

    public Label getLblArtilleryno() {
        return lblArtilleryno;
    }


    public Label getLblArmyName() {
        return lblArmyName;
    }
}
