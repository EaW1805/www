package com.eaw1805.www.client.views.infopanels.foreignUnits;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.HorizontalPanelLabelValue;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.util.NavyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

public class ForeignFleetInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants {

    public ForeignFleetInfoPanel(final FleetDTO fleet) {
        this.setStyleName("");
        this.setSize("366px", "90px");

        final ClickAbsolutePanel fleetInfoPanel = new ClickAbsolutePanel();
        fleetInfoPanel.setStyleName("fleetInfoPanel");
        this.add(fleetInfoPanel);
        fleetInfoPanel.setSize("363px", "87px");

        final Label lblFleetName = new Label(fleet.getName());
        lblFleetName.setStyleName("clearFontMiniTitle");

        final Label lblXy = new Label("N/A");
        if (fleet.getFleetId() != 0) {
            lblXy.setText(fleet.positionToString());
        }
        lblXy.setTitle("Fleets position");
        lblXy.setStyleName("clearFontSmall");
        lblXy.setSize("38px", "18px");
        fleetInfoPanel.add(lblXy, 315, 3);

        final HorizontalPanel relationPanel = new HorizontalPanel();
        relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        relationPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        final VerticalPanel infoPanel = new VerticalPanel();
        infoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + fleet.getNationId() + "-120.png");
        flag.setSize("", "82px");

        final NavyUnitInfoDTO fleetInfo = MiscCalculators.getFleetInfo(fleet);
        final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(fleet.getNationId()));
        relationStatus.setStyleName("clearFont");
        infoPanel.add(new HorizontalPanelLabelValue(3, lblFleetName));
        infoPanel.add(new HorizontalPanelLabelValue(3, relationStatus));

        final Image batsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png");
        final Label numOfBats = new Label(String.valueOf(fleetInfo.getMerchantShips() + fleetInfo.getWarShips()));
        numOfBats.setStyleName("clearFont");
        infoPanel.add(new HorizontalPanelLabelValue(3, batsImg, 3, numOfBats));
        relationPanel.add(flag);
        relationPanel.add(infoPanel);
        fleetInfoPanel.add(relationPanel, 3, 3);
    }

}
