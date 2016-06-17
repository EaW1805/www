package com.eaw1805.www.scenario.views.infopanels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.managers.beans.GameSettingsManagerBean;
import com.eaw1805.data.model.GameSettings;
import com.eaw1805.www.fieldbattle.stores.utils.ArmyUnitInfoDTO;
import com.eaw1805.www.fieldbattle.stores.utils.calculators.MiscCalculators;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.ToolTipPanel;
import com.eaw1805.www.scenario.stores.*;
import com.eaw1805.www.scenario.views.EditorPanel;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.fieldbattle.widgets.shared.TextBoxEditable;
import com.eaw1805.www.scenario.widgets.popups.UnitViewerPopup;
import com.eaw1805.www.shared.stores.GameStore;

import java.util.Map;


public class CorpsInfoPanel
        extends AbsolutePanel
        implements ArmyConstants, StyleConstants {

    private CorpDTO corp;
    private final Label lblXBrigades, lblYBattalions,
            lblInfantryNo, lblCavalryNo, lblArtilleryNo,
            lblMps;
    private final Image corpImage;
    private final Label lblXy;
    private ArmyUnitInfoDTO corpInfo;
    private final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
    Label armyLbl = new Label();
    final Image brigImg;
    public CorpsInfoPanel(final CorpDTO corp) {
        setCorp(corp);
        int leftFactor = 0;
        if (corp.getCorpId() == 0) {
            leftFactor = 80;
        }


        corpInfo = MiscCalculators.getCorpInfo(corp);
        this.setStyleName("corpInfoPanel");

        this.setSize("366px", "90px");

        AbsolutePanel corpInfoPanel = new AbsolutePanel();
        corpInfoPanel.setStyleName("clickArmyPanel");
        this.add(corpInfoPanel);
        corpInfoPanel.setSize("363px", "87px");

        corpImage = new Image();
        if (corp.getCorpId() != 0) {
            corpInfoPanel.add(corpImage, 3, 3);
        }
        corpImage.setSize("82px", "82px");


        final TextBoxEditable lblCorpName = new TextBoxEditable("Corps Name");
        lblCorpName.setText(corp.getName());
        lblCorpName.initHandler(new BasicHandler() {
            @Override
            public void run() {
                corp.setName(lblCorpName.getText());
            }
        });



        corpInfoPanel.add(lblCorpName, 90 - leftFactor, 3);


        brigImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/brigade.png");
        brigImg.setTitle("Brigades");
        corpInfoPanel.add(brigImg, 90 - leftFactor, 28);
        brigImg.setSize("", SIZE_15PX);

        lblXBrigades = new Label("X");
        lblXBrigades.setStyleName(CLASS_CLEARFONTSMALL);
        corpInfoPanel.add(lblXBrigades, 118 - leftFactor, 28);

        Image batImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/battalion.png");
        batImg.setTitle("Batttalions");
        corpInfoPanel.add(batImg, 90 - leftFactor, 46);
        batImg.setSize("", SIZE_15PX);
        lblYBattalions = new Label("Y");
        lblYBattalions.setStyleName(CLASS_CLEARFONTSMALL);
        corpInfoPanel.add(lblYBattalions, 118 - leftFactor, 46);


        Image infImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/infantry.png");
        Image cavImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/cavalry.png");
        Image artImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/artillery.png");
        corpInfoPanel.add(infImage, 160 - leftFactor, 28);
        corpInfoPanel.add(cavImage, 160 - leftFactor, 46);
        corpInfoPanel.add(artImage, 160 - leftFactor, 64);

        infImage.setSize("25px", SIZE_15PX);
        cavImage.setSize("25px", SIZE_15PX);
        artImage.setSize("25px", SIZE_15PX);
        infImage.setTitle("Infantry");
        cavImage.setTitle("Cavalry");
        artImage.setTitle("Artillery");

        lblInfantryNo = new Label("");
        lblInfantryNo.setStyleName(CLASS_CLEARFONTSMALL);
        corpInfoPanel.add(lblInfantryNo, 188 - leftFactor, 28);

        lblCavalryNo = new Label("");
        lblCavalryNo.setStyleName(CLASS_CLEARFONTSMALL);
        corpInfoPanel.add(lblCavalryNo, 188 - leftFactor, 46);

        lblArtilleryNo = new Label("");
        lblArtilleryNo.setStyleName(CLASS_CLEARFONTSMALL);
        corpInfoPanel.add(lblArtilleryNo, 188 - leftFactor, 64);
        lblMps = new Label(corpInfo.getMps() + " MPs");

        lblMps.setTitle("Movement points.");
        lblMps.setStyleName(CLASS_CLEARFONTSMALL);
        corpInfoPanel.add(lblMps, 315, 20);
        lblMps.setSize("150px", "18px");

        lblXy = new Label(getCorp().positionToString());
        lblXy.setTitle("Corps position.");
        lblXy.setStyleName(CLASS_CLEARFONTSMALL);
        corpInfoPanel.add(lblXy, 315, 3);

        final ImageButton deleteImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png", "Delete brigade");
        deleteImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //first delete this brigade
                CorpsUtils.deleteCorps(corp);
                //then remove it from parent
                removeFromParent();
                //finally update the army overview
                EditorPanel.getInstance().getArmyOverView().updateOverview();
            }
        });
        deleteImg.setSize("20px", "20px");
        add(deleteImg, 270, 60);

        final Image armyImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/army.png");
        armyImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getMenu(corp.getNationId()).showRelativeTo(armyImg);
            }
        });

        add(armyImg, 296, 60);
        add(armyLbl, 290, 45);
        setUpLabels();
    }



    private void setUpLabels() {
        corpInfo = MiscCalculators.getCorpInfo(corp);

        lblXy.setText(getCorp().positionToString());
//        lblMps = new Label(corpInfo.getMps() + " MPs");
        lblXBrigades.setText(numberFormat.format(corpInfo.getBrigades()));
        lblYBattalions.setText(numberFormat.format(corpInfo.getBattalions()));
        lblInfantryNo.setText(numberFormat.format(corpInfo.getInfantry()));
        lblCavalryNo.setText(numberFormat.format(corpInfo.getCavalry()));
        lblArtilleryNo.setText(numberFormat.format(corpInfo.getArtillery()));


        if (corp.getCommander() == null || corp.getCommander().getNationId() == 0) {
            corpImage.setUrl("http://static.eaw1805.com/img/commanders/Generic_Naval_Commander.png");
        } else {
            final int imageId;
            if (corp.getCommander().getIntId() > 10) {
                imageId = 0;

            } else {
                imageId = corp.getCommander().getIntId();
            }
            corpImage.setUrl("http://static.eaw1805.com/img/commanders/s" + GameStore.getInstance().getScenarioId()
                    + "/" + corp.getCommander().getNationId() + "/" + imageId + ".jpg");
            new ToolTipPanel(corpImage, true) {
                @Override
                public void generateTip() {
                    setTooltip(new CommanderInfoPanel(corp.getCommander()));
                }
            };
        }

        lblMps.setText(corpInfo.getMps() + " MPs");

        brigImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new UnitViewerPopup<BrigadeDTO>(corp.getBrigades().values()) {

                    @Override
                    public Widget getUnitWidget(BrigadeDTO unit) {
                        return new BrigadeInfoPanel(unit);
                    }
                }.showRelativeTo(brigImg);

            }
        });
        if (corp.getArmyId() != 0) {
            ArmyDTO army = EditorStore.getInstance().getArmies().get(corp.getRegionId()).get(corp.getX()).get(corp.getY()).get(corp.getArmyId());
            armyLbl.setText(":" + army.getName() + ":");
        }
        armyLbl.setVisible(corp.getArmyId() != 0);
    }


    PopupPanel getMenu(int nationId) {
        final PopupPanel menu = new PopupPanel();
        menu.setAutoHideEnabled(true);
        final VerticalPanel container = new VerticalPanel();

        final Map<Integer, Map<Integer, Map<Integer, Map<Integer, ArmyDTO>>>> armiesMap = EditorStore.getInstance().getArmies();


        if (armiesMap.get(corp.getRegionId()).containsKey(corp.getX())
                && armiesMap.get(corp.getRegionId()).get(corp.getX()).containsKey(corp.getY())) {
            for (final ArmyDTO army : armiesMap.get(corp.getRegionId()).get(corp.getX()).get(corp.getY()).values()) {
                if (army.getNationId() == nationId) {
                    final Label armyName = new Label(army.getName());
                    armyName.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            ArmyUtils.addCorpsToArmy(corp, army);
                            menu.hide();
                            setUpLabels();
                        }
                    });
                    container.add(armyName);
                }
            }
        }
        final HorizontalPanel newCorps = new HorizontalPanel();
        final TextBox newArmyName = new TextBox();
        newCorps.add(newArmyName);
        final Button createCorps = new Button("Create And Add");
        newCorps.add(createCorps);
        createCorps.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (newArmyName.getText().trim().isEmpty()) {
                    Window.alert("Name cannot be empty");
                    return;
                }
                ArmyDTO army = ArmyUtils.createArmy(newArmyName.getText().trim(), corp.getRegionId(), corp.getX(), corp.getY(), corp.getNationId());
                ArmyUtils.addCorpsToArmy(corp, army);
                menu.hide();
                setUpLabels();
            }
        });
        container.add(newCorps);
        if (corp.getArmyId() != 0) {
            final Label free = new Label("Remove From Army");
            free.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    //remove brigade from corps
                    ArmyUtils.removeCorpsFromArmy(corp);
                    menu.hide();
                    setUpLabels();
                }
            });
            container.add(free);
        }

        menu.setWidget(container);
        return menu;
    }



    public final CorpDTO getCorp() {
        return corp;
    }

    public final void setCorp(final CorpDTO corp) {
        this.corp = corp;
    }




}
