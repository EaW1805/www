package com.eaw1805.www.client.views.infopanels.units.mini;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.util.ArmyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

public class CorpsInfoMini
        extends VerticalPanel
        implements ArmyConstants {

    private ClickAbsolutePanel corpInfoPanel;
    private CorpDTO corp;
    private Label lblXBrigades, lblYBattalions,
            lblInfantryNo, lblCavalryNo, lblArtilleryNo,
            lblCorpName, lblMps;

    private Image corpImage;
    //private Label lblXy;
    private ArmyUnitInfoDTO corpInfo;
    final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
    private Label lblArmy;
    private final UnitChangedHandler unitChangedHandler;
    public CorpsInfoMini(final CorpDTO corp, final boolean enabled) {
        setCorp(corp);

        corpInfo = MiscCalculators.getCorpInfo(corp);
        this.setStyleName("generalMiniInfoPanel");

        this.setSize("170px", "90px");

        corpInfoPanel = new ClickAbsolutePanel();
        corpInfoPanel.setStyleName("clickArmyPanel");
        this.add(corpInfoPanel);
        this.corpInfoPanel.setSize("170px", "89px");

        corpImage = new Image();
        if (corp.getCorpId() != 0) {
            corpInfoPanel.add(corpImage, 141, 60);
            this.corpImage.setSize("26px", "26px");
        }

        if (getCorp().getCorpId() == 0) {
            lblCorpName = new Label("Corps Name");
        } else {
            lblCorpName = new RenamingLabel("Corps Name", CORPS, getCorp().getCorpId());
        }

        lblCorpName.setStyleName("clearFontMedSmall");
        corpInfoPanel.add(lblCorpName, 3, 3);
        this.lblCorpName.setSize("163px", "21px");


        final Image brigImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/brigade.png");
        brigImg.setTitle("Brigades");
        corpInfoPanel.add(brigImg, 3, 34);
        brigImg.setSize("", "15px");

        lblXBrigades = new Label("X");
        lblXBrigades.setStyleName("clearFontSmall");
        corpInfoPanel.add(lblXBrigades, 31, 34);

        final Image batImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/battalion.png");
        batImg.setTitle("Batttalions");
        corpInfoPanel.add(batImg, 3, 52);
        batImg.setSize("", "15px");

        lblYBattalions = new Label("Y");
        lblYBattalions.setStyleName("clearFontSmall");
        corpInfoPanel.add(lblYBattalions, 31, 52);


        final Image infImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/infantry.png");
        final Image cavImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/cavalry.png");
        final Image artImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/artillery.png");
        corpInfoPanel.add(infImage, 56, 34);
        corpInfoPanel.add(cavImage, 56, 52);
        corpInfoPanel.add(artImage, 56, 70);

        infImage.setSize("25px", "15px");
        cavImage.setSize("25px", "15px");
        artImage.setSize("25px", "15px");
        infImage.setTitle("Infantry");
        cavImage.setTitle("Cavalry");
        artImage.setTitle("Artillery");


        lblInfantryNo = new Label("");
        this.lblInfantryNo.setStyleName("clearFontSmall");
        corpInfoPanel.add(lblInfantryNo, 84, 34);

        lblCavalryNo = new Label("");
        this.lblCavalryNo.setStyleName("clearFontSmall");
        corpInfoPanel.add(lblCavalryNo, 84, 52);

        lblArtilleryNo = new Label("");
        this.lblArtilleryNo.setStyleName("clearFontSmall");
        corpInfoPanel.add(lblArtilleryNo, 84, 70);
        //this.lblXy = new Label(getCorp().positionToString());
        //lblXy.setTitle("Corps position.");
        //this.lblXy.setStyleName("clearFontSmall");
        //this.corpInfoPanel.add(this.lblXy, 119, 6);

        lblMps = new Label(corpInfo.getMps() + " MPs");
        lblMps.setTitle("Movement points.");
        lblMps.setStyleName("clearFontMini");
        corpInfoPanel.add(lblMps, 127, 3);
        this.lblMps.setSize("43px", "12px");

        this.lblArmy = new Label("Army:" + ArmyStore.getInstance().getArmyNameById(corp.getArmyId()));
        this.lblArmy.setStyleName("clearFontMini");
        this.lblArmy.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.corpInfoPanel.add(this.lblArmy, 3, 21);
        this.lblArmy.setSize("116px", "12px");


        boolean notSupplied = false;

        for (BrigadeDTO brigade : corp.getBrigades().values()) {
            for (BattalionDTO battalion : brigade.getBattalions()) {
                if (battalion.getNotSupplied()) {
                    notSupplied = true;
                }
            }
            if (notSupplied) {
                break;
            }
        }

        if (notSupplied) {
            final Image notSupplyImg = new Image("http://static.eaw1805.com/images/buttons/OutOfSupply32.png");
            notSupplyImg.setSize("30px", "30px");
            this.corpInfoPanel.add(notSupplyImg, 286, 3);
            notSupplyImg.setTitle("Not supplied");
        }


        setUpLabels();

        if (!enabled) {
            final Image disabledImage = new Image("http://static.eaw1805.com/images/infopanels/disabledMini.png");
            disabledImage.setTitle("Further orders are disabled due to orders already issued that may be in conflict");
            corpInfoPanel.add(disabledImage);
        }
        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(UnitChangedEvent event) {
                if (event.getInfoType() == CORPS && event.getInfoId() == corp.getCorpId() || event.getInfoId() == 0) {
                    setUpLabels();
                }
            }
        };
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void onAttach() {
        super.onAttach();
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    private void setUpLabels() {
        corpInfo = MiscCalculators.getCorpInfo(corp);
        if (!getCorp().getBrigades().isEmpty() && getCorp().getBrigades().values().iterator().next().getCorpId() == 0) {
            getLblCorpName().setText("Brigades without corps.");
        } else if (getCorp() != null && getCorp().getName() != null && !getCorp().getName().equals("")) {
            getLblCorpName().setText(getCorp().getName());
        } else {
            getLblCorpName().setText("Click to set name");
        }
        getLblXBrigades().setText(numberFormat.format(corpInfo.getBrigades()));
        getLblYBattalions().setText(numberFormat.format(corpInfo.getBattalions()));
        getLblInfantryNo().setText(numberFormat.format(corpInfo.getInfantry()));
        getLblCavalryNo().setText(numberFormat.format(corpInfo.getCavalry()));
        getLblArtilleryNo().setText(numberFormat.format(corpInfo.getArtillery()));
        lblMps.setText(corpInfo.getMps() + " MPs");
        lblArmy.setText("Army:" + ArmyStore.getInstance().getArmyNameById(corp.getArmyId()));


        if (corp.getCommander() == null || corp.getCommander().getNationId() == 0) {
            corpImage.setUrl("http://static.eaw1805.com/img/commanders/Generic_Naval_Commander.png");
        } else {
            final int imageId = corp.getCommander().getIntId();
            corpImage.setUrl("http://static.eaw1805.com/img/commanders/s"
                    + GameStore.getInstance().getScenarioId() + "/"
                    + corp.getCommander().getNationId() + "/" + imageId + ".jpg");
            corpImage.setTitle(corp.getCommander().getName());
            new ToolTipPanel(corpImage) {
                @Override
                public void generateTip() {
                    setTooltip(new CommanderInfoMini(corp.getCommander()));
                }
            };
        }
    }

    public CorpDTO getCorp() {
        return corp;
    }

    public void setCorp(final CorpDTO corp) {
        this.corp = corp;
    }

    public Label getLblXBrigades() {
        return lblXBrigades;
    }

    public Label getLblYBattalions() {
        return lblYBattalions;
    }

    public Label getLblInfantryNo() {
        return lblInfantryNo;
    }

    public Label getLblCavalryNo() {
        return lblCavalryNo;
    }

    public Label getLblArtilleryNo() {
        return lblArtilleryNo;
    }

    public Label getLblCorpName() {
        return lblCorpName;
    }

    public ClickAbsolutePanel getCorpInfoPanel() {
        return corpInfoPanel;
    }
}
