package com.eaw1805.www.client.views.infopanels.foreignUnits;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.HorizontalPanelLabelValue;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.util.ArmyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;


public class ForeignCorpsInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants {

    public ForeignCorpsInfoPanel(final CorpDTO corp) {
        this.setStyleName("corpInfoPanel");
        this.setSize("366px", "90px");

        final ClickAbsolutePanel corpInfoPanel = new ClickAbsolutePanel();
        corpInfoPanel.setStyleName("clickArmyPanel");
        corpInfoPanel.setSize("363px", "87px");
        this.add(corpInfoPanel);

        final Image corpImage = new Image("http://static.eaw1805.com/images/armies/dominant/comUnknown.png");
        if (corp.getCommander() != null && corp.getCommander().getId() != 0) {
            final int imageId = corp.getCommander().getIntId();
            corpImage.setUrl("http://static.eaw1805.com/img/commanders/s"
                    + GameStore.getInstance().getScenarioId() + "/"
                    + corp.getCommander().getNationId() + "/" + imageId + ".jpg");
        }

        if (corp.getCorpId() != 0) {
            corpInfoPanel.add(corpImage, 3, 3);
        }
        corpImage.setSize("82px", "82px");

        final Label lblCorpName;
        if (corp.getCorpId() >= 0) {
            lblCorpName = new Label("Corps");
        } else {
            lblCorpName = new Label(corp.getName());
        }
        lblCorpName.setStyleName("clearFontMiniTitle");

        final Label lblXy = new Label(corp.positionToString());
        lblXy.setTitle("Corps position.");
        lblXy.setStyleName("clearFontSmall");
        corpInfoPanel.add(lblXy, 315, 3);

        final HorizontalPanel relationPanel = new HorizontalPanel();
        relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        relationPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        final VerticalPanel infoPanel = new VerticalPanel();
        infoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + corp.getNationId() + "-120.png");
        flag.setSize("", "82px");

        final ArmyUnitInfoDTO brigInfo = MiscCalculators.getCorpInfo(corp);

        final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(corp.getNationId()));
        relationStatus.setStyleName("clearFont");
        infoPanel.add(new HorizontalPanelLabelValue(3, lblCorpName));
        infoPanel.add(new HorizontalPanelLabelValue(3, relationStatus));

        final Image armiesImg;
        final Label numOfBats = new Label("");
        if (corp.getCorpId() < 0) {
            armiesImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/brigade.png");
            numOfBats.setText(String.valueOf(brigInfo.getBrigades()));
        } else {
            armiesImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/battalion.png");
            numOfBats.setText(String.valueOf(brigInfo.getBattalions()));
        }
        numOfBats.setStyleName("clearFont");
        infoPanel.add(new HorizontalPanelLabelValue(3, armiesImg, 3, numOfBats));
        relationPanel.add(flag);
        relationPanel.add(infoPanel);
        corpInfoPanel.add(relationPanel, 90, 3);
    }

}
