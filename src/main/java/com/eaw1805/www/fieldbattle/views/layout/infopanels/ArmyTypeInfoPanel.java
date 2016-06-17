package com.eaw1805.www.fieldbattle.views.layout.infopanels;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.www.fieldbattle.stores.utils.calculators.WeightCalculators;
import com.eaw1805.www.fieldbattle.widgets.ClickAbsolutePanel;


public class ArmyTypeInfoPanel
        extends Composite
        implements GoodConstants, StyleConstants {

    private ArmyTypeDTO armyType;

    public ArmyTypeInfoPanel(final ArmyTypeDTO armyType, final BattalionDTO battalion) {
        if (battalion == null) {
            this.setArmyType(armyType);
        } else {
            this.setArmyType(battalion.getEmpireArmyType());
        }

        final int nationId = armyType.getNationId();
        final ClickAbsolutePanel battalionPanel = new ClickAbsolutePanel();
        battalionPanel.setStyleName("ArmyTypeInfoPanel2");
        battalionPanel.setStylePrimaryName("ArmyTypeInfoPanel2");
        battalionPanel.setStyleName("clickArmyPanel", true);
        initWidget(battalionPanel);
        battalionPanel.setSize("142px", "330px");

        final Image arTypeImg = new Image("http://static.eaw1805.com/images/armies/" + nationId + "/"
                + getArmyType().getIntId() + ".jpg");
        battalionPanel.add(arTypeImg, 3, 3);
        arTypeImg.setSize("135px", "");

        final Label lblName = new Label(getArmyType().getName().trim());
        lblName.setStyleName(CLASS_CLEARFONTMEDSMALL);
        battalionPanel.add(lblName, 6, 141);
        lblName.setSize("127", "18px");


        String type = "";
        if (getArmyType().isElite()) {
            type = "<b>Crack Elite</b> ";
        } else if (getArmyType().isCrack()) {
            type = "<b>Crack</b> ";
        }
        if (getArmyType().getType().equals("In")) {
            type += "Infantry";
        } else if (getArmyType().getType().equals("Ca")) {
            type += "Cavalry";
        } else if (getArmyType().getType().equals("Ar")) {
            type += "Artillery";
        } else if (getArmyType().getType().equals("Kt")) {
            type += "Colonial Troops";
        } else if (getArmyType().getType().equals("Co")) {
            type += "Colonial Troops";
        } else if (getArmyType().getType().equals("MC")) {
            type += "Colonial Cavalry";
        } else if (getArmyType().getType().equals("CC")) {
            type += "Colonial Cavalry";
        }

        HTML typeHtml = new HTML(type);
        typeHtml.setStyleName(CLASS_CLEARFONTMEDSMALL);

        battalionPanel.add(typeHtml, 6, 159);


        Grid infoGrid = new Grid(7, 2);
        battalionPanel.add(infoGrid, 4, 174);

        if (battalion == null) {
            int headcount = 800;
            if (getArmyType().getNationId() == NationConstants.NATION_MOROCCO
                    || getArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                    || getArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                headcount = 1000;
            }
            final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();


            StringBuilder costStr = new StringBuilder();
            costStr.append(numberFormat.format(getArmyType().getCost())).append("/").append(numberFormat.format(getArmyType().getIndPt())).append("/").append(numberFormat.format(headcount));
            if (getArmyType().needsHorse()) {
                costStr.append("/").append(numberFormat.format(headcount));
            }

            infoGrid.setWidget(0, 0, createTitle("Cost"));
            infoGrid.setWidget(0, 1, createLabel(costStr.toString()));

            infoGrid.setWidget(1, 0, createTitle("Heads"));
            infoGrid.setWidget(1, 1, createLabel(numberFormat.format(headcount)));

            infoGrid.setWidget(2, 0, createTitle("Exp"));
            infoGrid.setWidget(2, 1, createLabel(String.valueOf(getArmyType().getMaxExp())));

            infoGrid.setWidget(3, 0, createTitle("Str"));
            infoGrid.setWidget(3, 1, createLabel(getArmyType().getLongCombat() + " / " + getArmyType().getLongRange() + " / " + getArmyType().getHandCombat()));


            infoGrid.setWidget(4, 0, createTitle("MP"));
            infoGrid.setWidget(4, 1, createLabel(getArmyType().getMps() + " / " + getArmyType().getSps()));


            //also provide cost information
            int posY2 = 5;
            final Image moneyImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_MONEY + PNG);
            moneyImg.setSize(SIZE_10PX, SIZE_10PX);
            battalionPanel.add(moneyImg, 6, posY2);

            final Label moneyValue = new Label(numberFormat.format(getArmyType().getCost()));
            moneyValue.setStyleName(CLASS_CLEARFONTMINI);
            battalionPanel.add(moneyValue, 21, posY2);

            posY2 += 11;
            final Image inPtImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_INPT + PNG);
            inPtImg.setSize(SIZE_10PX, SIZE_10PX);
            battalionPanel.add(inPtImg, 6, posY2);


            final Label inPtValue = new Label(numberFormat.format(getArmyType().getIndPt()));
            inPtValue.setStyleName(CLASS_CLEARFONTMINI);
            battalionPanel.add(inPtValue, 21, posY2);

            posY2 += 11;
            final Image peopleImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_PEOPLE + PNG);
            peopleImg.setSize(SIZE_10PX, SIZE_10PX);
            battalionPanel.add(peopleImg, 6, posY2);


            final Label peopleValue = new Label(numberFormat.format(headcount));
            peopleValue.setStyleName(CLASS_CLEARFONTMINI);
            battalionPanel.add(peopleValue, 21, posY2);

            if (getArmyType().needsHorse()) {
                posY2 += 11;
                final Image horseImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_HORSE + PNG);
                horseImg.setSize(SIZE_10PX, SIZE_10PX);
                battalionPanel.add(horseImg, 6, posY2);

                final Label horseValue = new Label(numberFormat.format(headcount));
                horseValue.setStyleName(CLASS_CLEARFONTMINI);
                battalionPanel.add(horseValue, 21, posY2);
            }


        } else {
            infoGrid.setWidget(0, 0, createTitle("Heads"));
            infoGrid.setWidget(0, 1, createLabel(String.valueOf(battalion.getHeadcount())));

            infoGrid.setWidget(1, 0, createTitle("Exp"));
            infoGrid.setWidget(1, 1, createLabel(battalion.getExperience() + "/"
                    + getArmyType().getMaxExp()));

            infoGrid.setWidget(2, 0, createTitle("Str"));
            infoGrid.setWidget(2, 1, createLabel(getArmyType().getLongCombat() + " / " + getArmyType().getLongRange() + " / " + getArmyType().getHandCombat()));

            infoGrid.setWidget(3, 0, createTitle("Weight"));
            infoGrid.setWidget(3, 1, createLabel(WeightCalculators.getBattalionWeight(battalion) + " tons"));


            infoGrid.setWidget(4, 0, createTitle("MP"));
            infoGrid.setWidget(4, 1, createLabel(getArmyType().getMps() + " / " + getArmyType().getSps()));
        }


        final StringBuilder strFormations = new StringBuilder();

        boolean found = false;
        int abilities = 0;
        if (getArmyType().getFormationLi()) {
            strFormations.append(", Line");
            found = true;
            abilities++;
        }
        if (getArmyType().getFormationCo()) {
            strFormations.append(", Column");
            found = true;
            abilities++;
        }
        if (getArmyType().getFormationSq()) {
            strFormations.append(", Square");
            found = true;
            abilities++;
        }
        if (getArmyType().getFormationSk()) {
            strFormations.append(", Skirmish");
            found = true;
            abilities++;
        }
        if (!found) {
            strFormations.append("  no formation");
        }
        infoGrid.setWidget(5, 0, createTitle("Form"));
        infoGrid.setWidget(5, 1, createLabel(strFormations.substring(2)));


        final StringBuilder strCapabilities = new StringBuilder();
        final Label lblAbilities = new Label("");
        found = false;
        if (getArmyType().getTroopSpecsLc()) {
            strCapabilities.append(", Light Cavalry");
            found = true;
        }
        if (getArmyType().getTroopSpecsCu()) {
            strCapabilities.append(", Cuirassiers");
            found = true;
        }
        if (getArmyType().getTroopSpecsLr()) {
            strCapabilities.append(", Lancers");
            found = true;
        }
        if (found) {
            lblAbilities.setText(strCapabilities.substring(2));
        } else {
            lblAbilities.setText("  no capabilities");
        }

        infoGrid.setWidget(6, 0, createTitle("Special"));
        infoGrid.setWidget(6, 1, createLabel(strCapabilities.substring(2)));


    }

    /**
     * @return the armyType
     */
    public final ArmyTypeDTO getArmyType() {
        return armyType;
    }

    /**
     * @param armyType the armyType to set
     */
    public final void setArmyType(final ArmyTypeDTO armyType) {
        this.armyType = armyType;
    }

    public Label createLabel(final String text) {
        final Label out = new Label(text);
        out.setStyleName("clearFontMini");
        return out;
    }

    private Label createTitle(final String text) {
        final Label out = createLabel(text);
        out.setStyleName("bold", true);
        return out;
    }
}
