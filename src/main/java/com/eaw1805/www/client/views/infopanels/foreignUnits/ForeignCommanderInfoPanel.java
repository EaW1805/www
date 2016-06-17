package com.eaw1805.www.client.views.infopanels.foreignUnits;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.HorizontalPanelLabelValue;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RelationsStore;

public class ForeignCommanderInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants {

    public ForeignCommanderInfoPanel(final CommanderDTO commander) {
        setSize("366px", "90px");

        final ClickAbsolutePanel commanderInfoPanel = new ClickAbsolutePanel();
        commanderInfoPanel.setStyleName("commanderInfoPanel");
        commanderInfoPanel.setSize("363px", "87px");
        add(commanderInfoPanel);

        final int imageId = commander.getIntId();
        final Image commanderImg = new Image("http://static.eaw1805.com/img/commanders/s"
                + GameStore.getInstance().getScenarioId() + "/"
                + commander.getNationId() + "/" + imageId + ".jpg");
        commanderImg.setSize("", "82px");
        commanderInfoPanel.add(commanderImg, 3, 3);

        final Label lblName = new Label(commander.getName());
        lblName.setStyleName("clearFontMiniTitle");
        lblName.setSize("190px", "20px");

        final Label lblXy = new Label(commander.positionToString());
        lblXy.setTitle("Commanders position.");
        lblXy.setStyleName("clearFontSmall");
        commanderInfoPanel.add(lblXy, 315, 3);
        lblXy.setSize("47px", "15px");

        final HorizontalPanel relationPanel = new HorizontalPanel();
        relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        relationPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        final VerticalPanel infoPanel = new VerticalPanel();
        infoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + commander.getNationId() + "-120.png");
        flag.setSize("", "82px");

        final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(commander.getNationId()));
        relationStatus.setStyleName("clearFont");
        infoPanel.add(new HorizontalPanelLabelValue(3, lblName));
        infoPanel.add(new HorizontalPanelLabelValue(3, relationStatus));
        if (commander.getCaptured() == GameStore.getInstance().getNationId()) {
            final Label capturedLbl = new Label("Commander captured");
            capturedLbl.setStyleName("clearFontMedSmall");
            capturedLbl.setWidth("144px");
            infoPanel.add(new HorizontalPanelLabelValue(3, capturedLbl));
        }
        relationPanel.add(flag);
        relationPanel.add(infoPanel);
        commanderInfoPanel.add(relationPanel, 90, 3);
    }

}
