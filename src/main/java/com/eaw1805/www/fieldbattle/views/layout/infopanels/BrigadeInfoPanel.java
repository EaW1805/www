package com.eaw1805.www.fieldbattle.views.layout.infopanels;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.CommanderStore;
import com.eaw1805.www.fieldbattle.stores.utils.BasicHandler;
import com.eaw1805.www.fieldbattle.tooltips.Tips;
import com.eaw1805.www.fieldbattle.views.layout.CommanderSelectorPopup;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.mini.CommanderInfoMini;
import com.eaw1805.www.fieldbattle.widgets.ArmyImage;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.StyledCheckBox;
import com.eaw1805.www.fieldbattle.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;


public class BrigadeInfoPanel
        extends AbsolutePanel
        implements ArmyConstants, StyleConstants {


    private BrigadeDTO brigade;


    private final Label lblBrigade;
    private final int nationId;


    private final Label explbls[] = new Label[7];
    private final ImageButton commButt;
    private final ImageButton commRemButt;
    private final Image commImg;
    private final StyledCheckBox commOverall;
    private final ToolTipPanel tooltip;

    public BrigadeInfoPanel(final BrigadeDTO brigade, final boolean dead, final boolean fleeing, boolean readOnly) {
        //if it is requested for readonly, or it is not your brigade,
        //then it is only for view and not for editing.
        final boolean viewOnly = readOnly || brigade.getNationId() != BaseStore.getInstance().getNationId();

        setSize("366px", "90px");
        this.setBrigade(brigade);
        nationId = brigade.getNationId();


        setStylePrimaryName("brigadeInfoPanel");
        setStyleName("clickArmyPanel", true);
        setSize("363px", "87px");


        lblBrigade = new Label(brigade.getName());
        lblBrigade.setStyleName("clearFontMiniTitle");
        add(lblBrigade, 8, 3);

        final Label lblXy;
        if (fleeing) {
            lblXy = new Label("Fleeing");
        } else {
            lblXy = new Label(brigade.positionFieldBattleToString());
        }


        lblXy.setTitle("Brigades position");
        lblXy.setStyleName(CLASS_CLEARFONTSMALL);
        add(lblXy, 270, 3);
        lblXy.setSize("60px", SIZE_15PX);


        for (int index = 0; index < 7; index++) {
            explbls[index] = new Label();
        }

        commButt = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButCommandersOff.png", Tips.ADD_COMMANDER);
        commButt.setSize("20px", "20px");
        add(commButt, 315, 40);
        commButt.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                CommanderSelectorPopup commSelPanel = new CommanderSelectorPopup(brigade);
                commSelPanel.showRelativeTo(commButt);
                commSelPanel.setOnChangeHandler(new BasicHandler() {
                    @Override
                    public void run() {
                        setUpImages(dead, viewOnly);
                    }
                });
            }
        });

        commImg = new Image("");
        tooltip = new ToolTipPanel(commImg, true) {
            @Override
            public void generateTip() {
                setTooltip(null);
            }
        };

        commImg.setSize("49px", "49px");
        add(commImg, 309, 23);


        commRemButt = new ImageButton("http://static.eaw1805.com/images/buttons/ButDismissCommanderOff.png", Tips.REMOVE_COMMANDER);
        commRemButt.setSize("20px", "20px");
        add(commRemButt, 335, 66);
        commRemButt.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                brigade.setFieldBattleCommanderId(0);
                brigade.setFieldBattleOverallCommanderId(0);

                if (brigade.isPlacedOnFieldMap()) {
                    MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, 0);
                    MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false);
                }
                setUpImages(dead, viewOnly);

            }
        });
        commOverall = new StyledCheckBox("", false, true, Tips.OVERALL_COMMANDER);
        commOverall.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                int commId = 0;
                if (brigade.getFieldBattleCommanderId() > 0) {
                    commId = brigade.getFieldBattleCommanderId();
                } else if (brigade.getFieldBattleOverallCommanderId() > 0) {
                    commId = brigade.getFieldBattleOverallCommanderId();
                }
                if (commOverall.isChecked()) {
                    brigade.setFieldBattleCommanderId(0);
                    brigade.setFieldBattleOverallCommanderId(commId);
                } else {
                    brigade.setFieldBattleCommanderId(commId);
                    brigade.setFieldBattleOverallCommanderId(0);
                }

                for (BrigadeDTO curBrig : ArmyStore.getInstance().getBrigadesByNation(BaseStore.getInstance().getNationId())) {
                    if (curBrig.getFieldBattleOverallCommanderId() != 0 && curBrig.getBrigadeId() != brigade.getBrigadeId()) {
                        curBrig.setFieldBattleCommanderId(curBrig.getFieldBattleOverallCommanderId());
                        curBrig.setFieldBattleOverallCommanderId(0);
                    }
                }
                setUpImages(dead, viewOnly);

            }
        });
        add(commOverall, 337, 3);
        setUpImages(dead, viewOnly);
    }

    public void onAttach() {
        super.onAttach();
        commOverall.setChecked(brigade.getFieldBattleOverallCommanderId() > 0);
    }

    private void setUpImages(final boolean isDead, final boolean viewOnly) {


        int indexer = 0;
        for (final BattalionDTO battalion : brigade.getBattalions()) {
            if (battalion.getOrder() == 0) {
                indexer = 1;
                break;
            }
        }
        final ArmyImage[] battImages = new ArmyImage[7];

        for (final BattalionDTO battalion : brigade.getBattalions()) {

            final int index = battalion.getOrder() - 1 + indexer;
            battImages[index] = new ArmyImage();
            battImages[index].setArmyTypeDTO(battalion.getEmpireArmyType());
            battImages[index].setEmpireBattalionDTO(battalion);


            battImages[index].setUrl("http://static.eaw1805.com/images/armies/" + nationId + "/" + battalion.getEmpireArmyType().getIntId() + ".jpg");
            addOverViewPanelToImage(battImages[index]);
            battImages[index].setTitle(battalion.getEmpireArmyType().getName());

            if (isDead) {
                explbls[index].setText(battalion.getExperience() + "-" + 0);
            } else {
                explbls[index].setText(battalion.getExperience() + "-" + battalion.getHeadcount());
            }


            battImages[index].setSize("49px", "49px");


            explbls[index].setStylePrimaryName(CLASS_CLEARFONTSMALL);

            add(battImages[index], 8 + (49 * index), 23);
            add(explbls[index], 8 + (49 * index), 72);


        }

        if (brigade.getName().isEmpty()) {
            lblBrigade.setText("Brigade Name");

        } else {
            lblBrigade.setText(brigade.getName());
        }
        CommanderDTO commander = null;
        if (brigade.getFieldBattleCommanderId() > 0) {
            commander = CommanderStore.getInstance().getCommanderById(brigade.getFieldBattleCommanderId());
            commImg.setUrl("http://static.eaw1805.com/img/commanders/s" + GameStore.getInstance().getScenarioId() + "/" + commander.getNationId() + "/" + commander.getIntId() + ".jpg");
            commImg.setVisible(true);
            commRemButt.setVisible(true);
            commOverall.setChecked(false);
            commOverall.setVisible(true);
            commButt.setVisible(false);
        } else if (brigade.getFieldBattleOverallCommanderId() > 0) {
            commander = CommanderStore.getInstance().getCommanderById(brigade.getFieldBattleOverallCommanderId());
            commImg.setUrl("http://static.eaw1805.com/img/commanders/s" + GameStore.getInstance().getScenarioId() + "/" + commander.getNationId() + "/" + commander.getIntId() + ".jpg");
            commImg.setVisible(true);
            commRemButt.setVisible(true);
            commOverall.setChecked(true);
            commOverall.setVisible(true);
            commButt.setVisible(false);
        } else {
            commImg.setVisible(false);
            commRemButt.setVisible(false);
            commOverall.setVisible(false);
            commButt.setVisible(true);
        }
        if (!BaseStore.getInstance().isStartRound()) {
            commButt.setVisible(false);
            commOverall.setEnabled(false);
            commRemButt.setVisible(false);
        }

        commButt.setVisible(commButt.isVisible() && !viewOnly);
        commOverall.setVisible(commOverall.isVisible() && !viewOnly);
        commRemButt.setVisible(commRemButt.isVisible() && !viewOnly);
        if (commander == null) {
            tooltip.setTooltip(null);
        } else {
            tooltip.setTooltip(new CommanderInfoMini(commander));
        }
    }

    /**
     * Add tooltip popup panel that
     * shows information about a battalion.
     *
     * @param armyTypeImg The image to add the hover event
     */
    private void addOverViewPanelToImage(final ArmyImage armyTypeImg) {
        armyTypeImg.setStyleName("pointer", true);


        new ToolTipPanel(armyTypeImg, true) {
            @Override
            public void generateTip() {
                try {
                    setTooltip(new ArmyTypeInfoPanel(armyTypeImg.getEmpireBattalionDTO().getEmpireArmyType(),
                            armyTypeImg.getEmpireBattalionDTO()));
                } catch (Exception e) {

                }
            }
        };
    }


    public BrigadeDTO getBrigade() {
        return brigade;
    }

    public final void setBrigade(final BrigadeDTO value) {
        this.brigade = value;
    }

    public BrigadeDTO getValue() {
        return getBrigade();
    }

    public int getIdentifier() {
        return BRIGADE;
    }

    public Widget getWidget() {
        return this;
    }


    public void onEnter() {
        // do nothing
    }
}
