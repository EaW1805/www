package com.eaw1805.www.client.views.infopanels.units.mini;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;

import java.util.Map;

public class CommanderInfoMini extends VerticalPanel implements ArmyConstants {
    private CommanderDTO commander;
    private Label lblRank, lblCommands;
    private Image selectedImg;
    private boolean isSelected = false;
    private Label lblXy;

    public CommanderInfoMini(final CommanderDTO commander) {
        this.setCommander(commander);
        setStyleName("generalMiniInfoPanel");
        setSize("170px", "89px");

        final ClickAbsolutePanel commanderPanel = new ClickAbsolutePanel();
        add(commanderPanel);
        commanderPanel.setSize("170px", "89px");

        final int imageId = commander.getIntId();
        final Image commanderImg = new Image("http://static.eaw1805.com/img/commanders/s"
                + GameStore.getInstance().getScenarioId() + "/"
                + commander.getNationId() + "/" + imageId + ".jpg");
        commanderImg.setSize("36px", "36px");
        commanderPanel.add(commanderImg, 3, 3);

        final RenamingLabel lblName = new RenamingLabel(commander.getName(),
                COMMANDER, commander.getId());

        if (commander.getName().length() > 17) {
            lblName.setStyleName("clearFontMini");

        } else if (commander.getName().length() >= 15) {
            lblName.setStyleName("clearFontSmall");

        } else {
            lblName.setStyleName("clearFontMedium");
        }

        lblName.setSize("131px", "20px");
        commanderPanel.add(lblName, 39, 3);

        lblRank = new Label("");
        lblRank.setStyleName("clearFontSmall");
        this.lblRank.setSize("167px", "20px");
        commanderPanel.add(lblRank, 3, 44);

        lblCommands = new Label("");
        lblCommands.setStyleName("clearFontMini");
        commanderPanel.add(lblCommands, 3, 63);

        lblXy = new Label(commander.positionToString());
        lblXy.setTitle("Commanders position.");
        lblXy.setStyleName("clearFontSmall");
        commanderPanel.add(lblXy, 39, 24);
        lblXy.setSize("78px", "15px");

        final Label lbmps = new Label(commander.getMps() + " MPs");
        lbmps.setTitle("Movement points");
        lbmps.setStyleName("clearFontSmall");
        commanderPanel.add(lbmps, 120, 24);
        lbmps.setSize("47px", "15px");

        this.selectedImg = new Image("http://static.eaw1805.com/images/infopanels/selected.png");
        this.selectedImg.setStyleName("pointer");
        commanderPanel.add(selectedImg, 0, 0);
        this.selectedImg.setSize("170px", "89px");
        setupLabels();
        deselect();
    }

    public void setupLabels() {
        boolean inThePool = false;

        lblRank.setText(commander.getRank().getName() +
                " (" + commander.getStrc() + "-" + commander.getComc() + ")");

        if (commander.getArmy() > 0) {
            if (commander.getInTransit()) {
                switch (commander.getTransit()) {
                    case 1:
                        lblCommands.setText("In transit to lead " + ArmyStore.getInstance().getcArmies().get(commander.getArmy()).getName() + " army. Expected to reach army at the end of this month.");
                        break;

                    case 2:
                        lblCommands.setText("In transit to lead " + ArmyStore.getInstance().getcArmies().get(commander.getArmy()).getName() + " army. Expected to reach army at the end of next month.");
                        break;

                    default:
                        lblCommands.setText("In transit to lead " + ArmyStore.getInstance().getcArmies().get(commander.getArmy()).getName() + " army. Expected to reach army in " + commander.getTransit() + " months.");
                        break;
                }

            } else {
                if (ArmyStore.getInstance().getcArmies().containsKey(commander.getArmy()))
                    lblCommands.setText("Leads " + ArmyStore.getInstance().getcArmies().get(commander.getArmy()).getName() + " army");
            }

        } else if (commander.getCorp() > 0) {

            //find corp
            String name = "No name";
            final Map<Integer, ArmyDTO> armies = ArmyStore.getInstance().getcArmies();
            for (ArmyDTO army : armies.values()) {
                if (army.getCorps().containsKey(commander.getCorp())) {
                    name = army.getCorps().get(commander.getCorp()).getName();
                }
            }

            if (commander.getInTransit()) {
                switch (commander.getTransit()) {
                    case 1:
                        lblCommands.setText("In transit to lead " + name + " corps. Expected to reach corps at the end of this month.");
                        break;

                    case 2:
                        lblCommands.setText("In transit to lead " + name + " corps. Expected to reach corps at the end of next month.");
                        break;

                    default:
                        lblCommands.setText("In transit to lead " + name + " corps. Expected to reach corps in " + commander.getTransit() + " months.");
                        break;
                }


            } else {
                lblCommands.setText("Leads " + name + " corps");
            }
        } else if (commander.getInPool()) {
            inThePool = true;

            lblCommands.setText("Commander is in the pool");

        } else {
            lblCommands.setText("Commander is unassigned");
        }

        if (inThePool) {
            lblXy.setText(commander.positionToString().substring(0, 1) + " Pool");

        } else {
            lblXy.setText(commander.positionToString());
        }

        if (lblCommands.getText().length() > 20) {
            lblCommands.setStyleName("clearFontMini");

        } else {
            lblCommands.setStyleName("clearFontSmall");
        }

    }

    public void select() {
        isSelected = true;
        this.selectedImg.setVisible(true);

    }

    public void deselect() {
        isSelected = false;
        this.selectedImg.setVisible(false);

    }

    public void MouseOver() {
        if (!isSelected) {
            selectedImg.setVisible(true);
        }

    }

    public void MouseOut() {
        if (!isSelected) {
            selectedImg.setVisible(false);
        }

    }

    /**
     * @param commander the commander to set
     */
    public void setCommander(final CommanderDTO commander) {
        this.commander = commander;
    }

    /**
     * @return the commander
     */
    public CommanderDTO getCommander() {
        return commander;
    }

}
