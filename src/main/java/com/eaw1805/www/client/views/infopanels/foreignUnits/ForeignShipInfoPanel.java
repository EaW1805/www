package com.eaw1805.www.client.views.infopanels.foreignUnits;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.HorizontalPanelLabelValue;
import com.eaw1805.www.shared.stores.RelationsStore;

public class ForeignShipInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants {

    public ForeignShipInfoPanel(final ShipDTO ship) {
        setSize("366px", "90px");

        final ClickAbsolutePanel shipPanel = new ClickAbsolutePanel();
        this.add(shipPanel);
        shipPanel.setStyleName("ShipInfoPanel");
        shipPanel.setStylePrimaryName("ShipInfoPanel");
        shipPanel.setStyleName("clickArmyPanel", true);
        shipPanel.setSize("363px", "87px");

        final Image shipTypeImg = new Image("http://static.eaw1805.com/images/ships/" + ship.getNationId() + "/" + ship.getType().getIntId() + ".png");
        shipTypeImg.setTitle(ship.getType().getName());
        shipPanel.add(shipTypeImg, 3, 3);
        shipTypeImg.setSize("", "82px");

        //main
        final Label lblName = new Label("" + ship.getType().getName());
        lblName.setStyleName("clearFontMiniTitle");

        //right end
        final Label lblXy = new Label(ship.positionToString());
        lblXy.setTitle("Ships position");
        lblXy.setStyleName("clearFontSmall");
        lblXy.setSize("49px", "18px");
        shipPanel.add(lblXy, 315, 3);

        final HorizontalPanel relationPanel = new HorizontalPanel();
        relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        relationPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        final VerticalPanel infoPanel = new VerticalPanel();
        infoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + ship.getNationId() + "-120.png");
        flag.setSize("", "82px");

        final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(ship.getNationId()));
        relationStatus.setStyleName("clearFont");
        infoPanel.add(new HorizontalPanelLabelValue(3, lblName));
        infoPanel.add(new HorizontalPanelLabelValue(3, relationStatus));
        relationPanel.add(flag);
        relationPanel.add(infoPanel);
        shipPanel.add(relationPanel, 90, 3);
    }

}
