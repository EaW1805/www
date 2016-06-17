package com.eaw1805.www.client.views.infopanels.units.mini;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.views.extras.ArmyImage;
import com.eaw1805.www.client.views.infopanels.units.ArmyTypeInfoPanel;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.util.ArmyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;


public class BrigadeInfoMini
        extends VerticalPanel
        implements ArmyConstants {

    private final ClickAbsolutePanel brigadePanel;
    private BrigadeDTO brigade;
    private final ArmyImage[] battImages = new ArmyImage[7];
    private Image typeImage;
    private final Label hdCountlbls[] = new Label[7];
    private final Label explbls[] = new Label[7];
    private final Label lblBrigade, lblMp;
    private int nationId;
    private Image selectedImg;
    private boolean isSelected = false;
    private final UnitChangedHandler unitChangedHandler;
    private final boolean enabled;
    public BrigadeInfoMini(final BrigadeDTO brigade, final boolean enabled) {
        this.enabled = enabled;
        setSize("170px", "90px");
        this.brigade = brigade;
        nationId = brigade.getNationId();
        brigadePanel = new ClickAbsolutePanel();
        brigadePanel.setStylePrimaryName("brigadeMiniInfoPanel");
        brigadePanel.setStyleName("clickArmyPanel", true);
        brigadePanel.setSize("170px", "87px");
        add(brigadePanel);

        typeImage = new Image("");
        typeImage.setSize("", "19px");

        selectedImg = new Image("http://static.eaw1805.com/images/infopanels/selected.png");
        selectedImg.setSize("170px", "87px");
        deselect();

        lblBrigade = new RenamingLabel(brigade.getName(), BRIGADE, brigade.getBrigadeId());
        lblBrigade.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblBrigade.setStyleName("clearFontSmall");
        lblBrigade.setSize("167px", "18px");

        lblMp = new Label("");
        lblMp.setStyleName("clearFontMini");
        lblMp.setTitle("Movement points");

        setUpImages(enabled);
        unitChangedHandler = new UnitChangedHandler() {

            public void onUnitChanged(UnitChangedEvent event) {
                if (event.getInfoType() == BRIGADE && event.getInfoId() == brigade.getBrigadeId()) {
                    setUpImages(enabled);
                }
            }
        };
    }

    public void setUpImages(final boolean enabled) {
        brigadePanel.clear();
        brigadePanel.add(typeImage, 318, 38);
        brigadePanel.add(lblMp, 131, 3);

        this.brigadePanel.add(this.selectedImg, 0, 0);
        brigadePanel.add(lblBrigade, 3, 3);
        brigadePanel.add(lblMp, 131, 3);

        final ArmyUnitInfoDTO brigInfo = MiscCalculators.getBrigadeInfo(brigade);
        for (final BattalionDTO battalion : brigade.getBattalions()) {
            final int index = battalion.getOrder() - 1;
            battImages[index] = new ArmyImage();
            battImages[index].setArmyTypeDTO(battalion.getEmpireArmyType());
            battImages[index].setEmpireBattalionDTO(battalion);
            hdCountlbls[index] = new Label();
            explbls[index] = new Label();
            explbls[index].setSize("10px", "20px");

            if (brigade.getNationId() == GameStore.getInstance().getNationId()) {
                battImages[index].setUrl("http://static.eaw1805.com/images/armies/" + nationId + "/" + battalion.getEmpireArmyType().getIntId() + ".jpg");
                addOverViewPanelToImage(battImages[index]);
                battImages[index].setTitle(battalion.getEmpireArmyType().getName());
                hdCountlbls[index].setText(String.valueOf(battalion.getHeadcount()));
                explbls[index].setText(battalion.getExperience() + " " + battalion.getHeadcount());

            } else {
                battImages[index].setUrl("http://static.eaw1805.com/img/commanders/Generic_Naval_Commander.png");
                addOverViewPanelToImage(battImages[index]);
                battImages[index].setTitle("Unknown type");
                hdCountlbls[index].setText("N/A");
                explbls[index].setText("? ???");
            }

            battImages[index].setSize("26px", "26px");

            hdCountlbls[index].setStylePrimaryName("clearFontMini");

            explbls[index].setStylePrimaryName("clearFontMini");

            brigadePanel.add(battImages[index], 2 + (26 * index), 23);
            brigadePanel.add(explbls[index], 2 + (26 * index), 52);
        }

        if (brigade.getNationId() == GameStore.getInstance().getNationId()) {
            switch (brigInfo.getDominant()) {
                case 1:
                    getTypeImage().setUrl("http://static.eaw1805.com/images/armies/dominant/infantry.png");
                    break;

                case 2:
                    getTypeImage().setUrl("http://static.eaw1805.com/images/armies/dominant/cavalry.png");
                    break;

                case 3:
                    getTypeImage().setUrl("http://static.eaw1805.com/images/armies/dominant/artillery.png");
                    break;

                default:
                    break;
            }

        } else {
            getTypeImage().removeFromParent();
            final Image infImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/infantry.png");
            final Image cavImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/cavalry.png");
            final Image artImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/artillery.png");
            brigadePanel.add(infImage, 287, 26);
            brigadePanel.add(cavImage, 287, 44);
            brigadePanel.add(artImage, 287, 62);
            infImage.setSize("25px", "15px");
            cavImage.setSize("25px", "15px");
            artImage.setSize("25px", "15px");
            infImage.setTitle("Infantry");
            cavImage.setTitle("Cavalry");
            artImage.setTitle("Artillery");

            final Label lblInfantryNo = new Label(String.valueOf(brigInfo.getInfantry()));
            lblInfantryNo.setStyleName("clearFontSmall");
            brigadePanel.add(lblInfantryNo, 315, 26);

            final Label lblCavalryNo = new Label(String.valueOf(brigInfo.getCavalry()));
            lblCavalryNo.setStyleName("clearFontSmall");
            brigadePanel.add(lblCavalryNo, 315, 44);

            final Label lblArtilleryNo = new Label(String.valueOf(brigInfo.getArtillery()));
            lblArtilleryNo.setStyleName("clearFontSmall");
            brigadePanel.add(lblArtilleryNo, 315, 62);
        }

        if (brigade.getName().isEmpty()) {
            lblBrigade.setText("Brigade Name");

        } else {
            lblBrigade.setText(brigade.getName());
        }

        if (brigade.getNationId() == GameStore.getInstance().getNationId()) {
            lblMp.setText(brigInfo.getMps() + " MPs");
        } else {
            lblMp.removeFromParent();
        }

        if (!enabled) {
            final Image disabledImage = new Image("http://static.eaw1805.com/images/infopanels/disabledMini.png");
            if (brigade.getLoaded()) {
                disabledImage.setTitle("This panel is disabled because brigade is loaded");
            } else {
                disabledImage.setTitle("Further orders are disabled due to orders already issued that may be in conflict");
            }
            brigadePanel.add(disabledImage);
        }
    }

    private void addOverViewPanelToImage(final ArmyImage armyTypeImg) {
        armyTypeImg.setStyleName("pointer", true);
        new ToolTipPanel(armyTypeImg) {
            @Override
            public void generateTip() {
                setTooltip(new ArmyTypeInfoPanel(armyTypeImg.getEmpireBattalionDTO().getEmpireArmyType(),
                        armyTypeImg.getEmpireBattalionDTO()));
            }
        };
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void onAttach() {
        super.onAttach();
        setUpImages(enabled);
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
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

    public BrigadeDTO getBrigade() {
        return brigade;
    }

    public ClickAbsolutePanel getBrigadePanel() {
        return brigadePanel;
    }

    public Image getTypeImage() {
        return typeImage;
    }
}
