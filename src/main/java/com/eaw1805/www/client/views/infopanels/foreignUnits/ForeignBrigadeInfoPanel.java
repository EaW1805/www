package com.eaw1805.www.client.views.infopanels.foreignUnits;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.HorizontalPanelLabelValue;
import com.eaw1805.www.shared.stores.RelationsStore;

public class ForeignBrigadeInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants {

    private Image selectedImg;

    public ForeignBrigadeInfoPanel(final BrigadeDTO brigade) {
        setSize("366px", "90px");

        final ClickAbsolutePanel brigadePanel = new ClickAbsolutePanel();
        this.add(brigadePanel);
        brigadePanel.setStylePrimaryName("brigadeInfoPanel");
        brigadePanel.setStyleName("clickArmyPanel", true);
        brigadePanel.setSize("363px", "87px");

        selectedImg = new Image("http://static.eaw1805.com/images/infopanels/selected.png");
        deselect();
        brigadePanel.add(selectedImg, 0, 0);
        selectedImg.setSize("363px", "87px");

        final Label lblBrigade;
        if (brigade.getBrigadeId() >= 0) {
            lblBrigade = new Label("Brigade");
        } else {
            lblBrigade = new Label(brigade.getName());
        }
        lblBrigade.setStyleName("clearFontMiniTitle");

        final Label lblXy = new Label(brigade.positionToString());
        lblXy.setTitle("Brigades position");
        lblXy.setStyleName("clearFontSmall");
        brigadePanel.add(lblXy, 315, 3);
        lblXy.setSize("47px", "15px");

        final HorizontalPanel relationPanel = new HorizontalPanel();
        relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        relationPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        final VerticalPanel infoPanel = new VerticalPanel();
        infoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + brigade.getNationId() + "-120.png");
        flag.setSize("", "82px");

        int bats = 0;
        if (brigade.getBattalions() != null) {
            bats = brigade.getBattalions().size();
        }

        final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(brigade.getNationId()));
        relationStatus.setStyleName("clearFont");
        infoPanel.add(new HorizontalPanelLabelValue(3, lblBrigade));
        infoPanel.add(new HorizontalPanelLabelValue(3, relationStatus));

        final Image batsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/battalion.png");

        final Label numOfBats = new Label(String.valueOf(bats));
        numOfBats.setStyleName("clearFont");
        infoPanel.add(new HorizontalPanelLabelValue(3, batsImg, 3, numOfBats));
        relationPanel.add(flag);
        relationPanel.add(infoPanel);
        brigadePanel.add(relationPanel, 3, 3);
    }

    public void select() {
        selectedImg.setVisible(true);
    }

    public void deselect() {
        selectedImg.setVisible(false);
    }

}
