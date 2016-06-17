package com.eaw1805.www.client.views.infopanels.units.mini;


import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.shared.stores.GameStore;

public class BattalionInfoMini
        extends VerticalPanel {

    private ClickAbsolutePanel battalionPanel;
    private BattalionDTO battalion;

    public BattalionInfoMini(final BattalionDTO battalion) {
        this.battalion = battalion;
        final int nationId = GameStore.getInstance().getNationId();
        battalionPanel = new ClickAbsolutePanel();
        this.battalionPanel.setStyleName("ToolTip245x90");
        battalionPanel.setStylePrimaryName("ToolTip245x90");
        battalionPanel.setStyleName("clickArmyPanel", true);
        add(battalionPanel);
        this.battalionPanel.setSize("245px", "90px");

        final Image arTypeImg = new Image("http://static.eaw1805.com/images/armies/" + nationId + "/" + battalion.getEmpireArmyType().getIntId() + ".jpg");
        this.battalionPanel.add(arTypeImg, 3, 3);
        arTypeImg.setSize("86px", "86px");


        String nameType = battalion.getEmpireArmyType().getName();
        final Label lblName = new Label(nameType);

        this.battalionPanel.add(lblName, 92, 4);
        lblName.setSize("154px", "18px");
        lblName.setStyleName("clearFontMedSmall");

        String type = "Normal";
        if (battalion.getEmpireArmyType().isCrack()) {
            type = "Crack";
            if (battalion.getEmpireArmyType().isElite()) {
                type = type + " Elite";
            }
        }

        final Label typeLbl = new Label(type);
        typeLbl.setStyleName("clearFontMedSmall");
        battalionPanel.add(typeLbl, 92, 25);

        final Label lblStatus = new Label(battalion.getExperience() + "/"
                + battalion.getEmpireArmyType().getMaxExp() + " - "
                + battalion.getHeadcount() + " - "
                + battalion.getEmpireArmyType().getMps() + " MPs");
        lblStatus.setStyleName("clearFontMedSmall");
        battalionPanel.add(lblStatus, 92, 40);


        StringBuilder strCapabilities = new StringBuilder();
        final Label lblAbilities = new Label("");
        boolean found = false;
        if (battalion.getEmpireArmyType().getTroopSpecsLc()) {
            strCapabilities.append(", Light Cavalry");
            found = true;
        }
        if (battalion.getEmpireArmyType().getTroopSpecsCu()) {
            strCapabilities.append(", Cuirassiers");
            found = true;
        }
        if (battalion.getEmpireArmyType().getTroopSpecsLr()) {
            strCapabilities.append(", Lancers");
            found = true;
        }
        if (!found) {
            lblAbilities.setText("");
        } else {
            lblAbilities.setText(strCapabilities.substring(2));
        }

        lblAbilities.setStyleName("clearFontMedSmall");
        battalionPanel.add(lblAbilities, 92, 55);


        StringBuilder strFormations = new StringBuilder();
        final Label lblFormations = new Label("");
        found = false;
        int abilities = 0;
        if (battalion.getEmpireArmyType().getFormationLi()) {
            strFormations.append(", L");
            found = true;
            abilities++;
        }
        if (battalion.getEmpireArmyType().getFormationCo()) {
            strFormations.append(", C");
            found = true;
            abilities++;
        }
        if (battalion.getEmpireArmyType().getFormationSq()) {
            strFormations.append(", Q");
            found = true;
            abilities++;
        }
        if (battalion.getEmpireArmyType().getFormationSk()) {
            strFormations.append(", S");
            found = true;
            abilities++;
        }
        if (!found) {
            lblFormations.setText("No formation");
        } else {
            lblFormations.setText(strFormations.substring(2));
        }


        lblFormations.setStyleName("clearFontMedSmall");


        battalionPanel.add(lblFormations, 92, 70);


    }

    /**
     * @return the battalionPanel
     */
    public ClickAbsolutePanel getBattalionPanel() {
        return battalionPanel;
    }

    /**
     * @return the battalion
     */
    public BattalionDTO getBattalion() {
        return battalion;
    }

}
