package com.eaw1805.www.client.views.infopanels.foreignUnits;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.HorizontalPanelLabelValue;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.util.ArmyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;


public class ForeignArmyInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants {

    public ForeignArmyInfoPanel(final ArmyDTO army) {
        this.setStyleName("armyInfoPanel");
        this.setSize("366px", "90px");

        final ClickAbsolutePanel armyInfoPanel = new ClickAbsolutePanel();
        armyInfoPanel.setStyleName("clickArmyPanel");
        this.add(armyInfoPanel);
        armyInfoPanel.setSize("363px", "87px");

        final Label lblArmyName = new Label("Army");
        lblArmyName.setStyleName("clearFontMiniTitle");
        lblArmyName.setSize("182px", "24px");

        //add the commanders image
        final Image commanderImg = new Image("http://static.eaw1805.com/images/armies/dominant/comUnknown.png");
        if (army.getCommander() != null && army.getCommander().getId() != 0) {
            commanderImg.setUrl("http://static.eaw1805.com/img/commanders/s"
                    + GameStore.getInstance().getScenarioId() + "/"
                    + army.getCommander().getNationId() + "/" + army.getCommander().getIntId() + ".jpg");
        }
        commanderImg.setSize("", "82px");
        armyInfoPanel.add(commanderImg, 3, 3);

        final Label label = new Label(army.positionToString());
        label.setTitle("Armys position.");
        label.setStyleName("clearFontSmall");
        armyInfoPanel.add(label, 315, 3);

        final HorizontalPanel relationPanel = new HorizontalPanel();
        relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        relationPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        final VerticalPanel infoPanel = new VerticalPanel();
        infoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + ArmyStore.getInstance().getArmyNation(army) + "-120.png");
        flag.setSize("", "82px");
        final ArmyUnitInfoDTO brigInfo = MiscCalculators.getArmyInfo(army);

        final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(ArmyStore.getInstance().getArmyNation(army)));
        relationStatus.setStyleName("clearFont");
        infoPanel.add(new HorizontalPanelLabelValue(3, lblArmyName));
        infoPanel.add(new HorizontalPanelLabelValue(3, relationStatus));

        final Image batsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/battalion.png");
        final Label numOfBats = new Label(String.valueOf(brigInfo.getBattalions()));
        numOfBats.setStyleName("clearFont");
        infoPanel.add(new HorizontalPanelLabelValue(3, batsImg, 3, numOfBats));
        relationPanel.add(flag);
        relationPanel.add(infoPanel);
        armyInfoPanel.add(relationPanel, 90, 3);
    }

}
