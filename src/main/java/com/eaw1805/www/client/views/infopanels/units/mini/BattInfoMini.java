package com.eaw1805.www.client.views.infopanels.units.mini;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.shared.stores.GameStore;

public class BattInfoMini extends VerticalPanel {
    private ClickAbsolutePanel battalionPanel;
    private BattalionDTO battalion;

    public BattInfoMini(final BattalionDTO battalion) {
        this.battalion = battalion;
        final int nationId = GameStore.getInstance().getNationId();
        battalionPanel = new ClickAbsolutePanel();
        this.battalionPanel.setStyleName("battalionInfoPanel");
        battalionPanel.setStylePrimaryName("battalionInfoPanel");
        battalionPanel.setStyleName("clickArmyPanel", true);
        add(battalionPanel);
        this.battalionPanel.setSize("170px", "89px");

        final Image arTypeImg = new Image("http://static.eaw1805.com/images/armies/" + nationId + "/" + battalion.getEmpireArmyType().getIntId() + ".jpg");
        this.battalionPanel.add(arTypeImg, 0, 0);
        arTypeImg.setSize("49px", "46px");

        final Label lblName = new Label("Name: " + battalion.getEmpireArmyType().getName());
        lblName.setStyleName("clearFontMedium");
        this.battalionPanel.add(lblName, 52, 0);
        lblName.setSize("118px", "46px");

        final Label lblXp = new Label("Xp:" + battalion.getExperience());
        lblXp.setStyleName("clearFontMedSmall");
        this.battalionPanel.add(lblXp, 53, 71);

        final Label lblMp = new Label("Mp:" + battalion.getEmpireArmyType().getMps());
        lblMp.setStyleName("clearFontMedSmall");
        this.battalionPanel.add(lblMp, 0, 71);

        final Label lblHeadcount = new Label("HeadCount:" + battalion.getHeadcount());
        lblHeadcount.setStyleName("clearFontMedSmall");
        this.battalionPanel.add(lblHeadcount, 0, 47);

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
