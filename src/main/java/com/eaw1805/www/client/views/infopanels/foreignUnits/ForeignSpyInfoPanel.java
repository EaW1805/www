package com.eaw1805.www.client.views.infopanels.foreignUnits;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.HorizontalPanelLabelValue;
import com.eaw1805.www.shared.stores.RelationsStore;

public class ForeignSpyInfoPanel
        extends Composite
        implements ArmyConstants {

    public ForeignSpyInfoPanel(final SpyDTO spy) {
        final ClickAbsolutePanel spyPanel = new ClickAbsolutePanel();
        spyPanel.setStyleName("spyInfoPanel");
        spyPanel.setSize("366px", "90px");
        initWidget(spyPanel);

        final Image spyImg = new Image("http://static.eaw1805.com/images/buttons/icons/spy.png");
        spyPanel.add(spyImg, 3, 3);
        spyImg.setSize("", "82px");

        final Label lblSpyName = new Label("Spy");
        lblSpyName.setStyleName("clearFontMiniTitle");
        lblSpyName.setSize("155px", "20px");

        final Label lblLocation = new Label(spy.positionToString());
        lblLocation.setTitle("Spy position.");
        lblLocation.setStyleName("clearFontSmall");
        lblLocation.setSize("47px", "15px");
        spyPanel.add(lblLocation, 315, 3);

        final HorizontalPanel relationPanel = new HorizontalPanel();
        relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        relationPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        final VerticalPanel infoPanel = new VerticalPanel();
        infoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + spy.getNationId() + "-120.png");
        flag.setSize("", "82px");

        final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(spy.getNationId()));
        relationStatus.setStyleName("clearFont");
        infoPanel.add(new HorizontalPanelLabelValue(3, lblSpyName));
        infoPanel.add(new HorizontalPanelLabelValue(3, relationStatus));
        relationPanel.add(flag);
        relationPanel.add(infoPanel);
        spyPanel.add(relationPanel, 90, 3);
    }

}
