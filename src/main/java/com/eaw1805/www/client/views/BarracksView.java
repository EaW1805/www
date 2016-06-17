package com.eaw1805.www.client.views;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.BaggageTrainBuildCostMini;
import com.eaw1805.www.client.views.military.BuildBrigadeView;
import com.eaw1805.www.client.views.military.CrackEliteView;
import com.eaw1805.www.client.views.military.TrainArmiesView;
import com.eaw1805.www.client.views.military.UpHdCountView;
import com.eaw1805.www.client.views.military.addbattalion.AddBattalionView;
import com.eaw1805.www.client.views.military.buildships.BuildShipView;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.economy.ProductionSiteStore;
import com.eaw1805.www.shared.stores.economy.TradeCityStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.units.ForeignUnitsStore;

import java.util.List;

public class BarracksView extends Composite implements ArmyConstants, ProductionSiteConstants, StyleConstants, RelationConstants {

    private final int sectorId;
    private final ImageButton leftImg;
    private final ImageButton rightImg;
    boolean byTheSea = false;
    final ImageButton buildBrigImg;

    public BarracksView(final BarrackDTO barrack,
                        final SectorDTO barrSectorDto,
                        final BarrackShipYardView parent,
                        final List<ShipTypeDTO> _empireShipTypeDTOs) {

        byTheSea = ProductionSiteStore.getInstance().isTileNeighborWithSeaById(barrSectorDto.getX(), barrSectorDto.getY(), barrSectorDto.getRegionId());
        sectorId = Integer.parseInt(String.valueOf(barrSectorDto.getId()));
        final AbsolutePanel absolutePanel = new AbsolutePanel();
        initWidget(absolutePanel);

        //get armies on this sector...
        final List<ArmyDTO> armiesOnSector = ArmyStore.getInstance().getArmiesBySector(barrSectorDto, false);
        final boolean armyActionsEnabled = (armiesOnSector != null && !armiesOnSector.isEmpty());
        final List<ArmyDTO> foreignOnSector = ForeignUnitsStore.getInstance().getArmiesBySectorRelation(barrSectorDto, REL_TRADE, REL_COLONIAL_WAR, REL_WAR);

        // check if foreign units are enemies
        boolean foreignArmiesOnSector = false;
        if (foreignOnSector == null || foreignOnSector.isEmpty()) {
            foreignArmiesOnSector = false;

        } else if (barrSectorDto.getProductionSiteDTO().getId() == PS_BARRACKS_FH
                || barrSectorDto.getProductionSiteDTO().getId() == PS_BARRACKS_FL
                || TradeCityStore.getInstance().getTradeCityByPosition(barrSectorDto) != null) {
            foreignArmiesOnSector = false;

        } else {
            // Check out foreign armies
            for (ArmyDTO armyDTO : foreignOnSector) {
                // check relations
                final int relation = RelationsStore.getInstance().getOriginalRelationByNationId(armyDTO.getNationId());
                if (relation == RelationConstants.REL_WAR
                        || (barrSectorDto.getRegionId() != RegionConstants.EUROPE && relation == RelationConstants.REL_COLONIAL_WAR)) {
                    foreignArmiesOnSector = true;
                    break;
                }
            }
        }

        absolutePanel.setStyleName("barracksPanel2");
        absolutePanel.setSize("496px", "417px");

        buildBrigImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButBarracksBuildBrigadeOff.png");
        if (foreignArmiesOnSector) {
            buildBrigImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButBarracksBuildBrigadeOff-Gray.png");
            buildBrigImg.setTitle("Cannot build new brigades because there are foreign armies on this sector.");
        } else {
            buildBrigImg.setStyleName(CLASS_POINTER);
            buildBrigImg.setTitle("Build new brigades.");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    final BuildBrigadeView empireBuildBrigadesWidget = new BuildBrigadeView(barrack, sectorId);
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(empireBuildBrigadesWidget);
                    GameStore.getInstance().getLayoutView().positionTocCenter(empireBuildBrigadesWidget);
                    if (TutorialStore.getInstance().isTutorialMode()
                            && TutorialStore.getInstance().getMonth() == 5
                            && TutorialStore.getInstance().getTutorialStep() == 2) {
                        TutorialStore.nextStep(false);
                    }
                }
            }).addToElement(buildBrigImg.getElement()).register();
        }
        absolutePanel.add(buildBrigImg, 10, 55);
        buildBrigImg.setSize(SIZE_117PX, SIZE_174PX);

        final ImageButton trainImg = new ImageButton("");
        if (armyActionsEnabled && !foreignArmiesOnSector) {
            trainImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButBarracksUpgradeTroopsOff.png");
            trainImg.setStyleName(CLASS_POINTER);
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    final TrainArmiesView trainPanel = new TrainArmiesView(barrSectorDto);
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(trainPanel);
                    GameStore.getInstance().getLayoutView()
                            .setWidgetPosition(trainPanel,
                                    (Document.get().getClientWidth() - trainPanel.getOffsetWidth()) / 2,
                                    (Document.get().getClientHeight() - trainPanel.getOffsetHeight()) / 2, false, true);
                    if (TutorialStore.getInstance().isTutorialMode()
                            && TutorialStore.getInstance().getMonth() == 6
                            && TutorialStore.getInstance().getTutorialStep() == 1) {
                        TutorialStore.nextStep(false);
                    }
                }
            }).addToElement(trainImg.getElement()).register();

        } else {
            trainImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButBarracksUpgradeTroopsOff-Gray.png");
        }

        if (foreignArmiesOnSector) {
            trainImg.setTitle("Cannot train your troops because there are foreign armies on this sector.");
        } else {
            trainImg.setTitle("Train your troops.");
        }
        absolutePanel.add(trainImg, 130, 55);
        trainImg.setSize(SIZE_117PX, SIZE_174PX);

        final ImageButton upgTroopsImg = new ImageButton("");
        if (armyActionsEnabled && !foreignArmiesOnSector) {
            upgTroopsImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButBarracksIncreaseExpOff.png");
            upgTroopsImg.setStyleName(CLASS_POINTER);
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    final CrackEliteView crackPanel = new CrackEliteView(barrSectorDto, barrack);
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(crackPanel);
                    GameStore.getInstance().getLayoutView()
                            .setWidgetPosition(crackPanel,
                                    (Document.get().getClientWidth() - crackPanel.getOffsetWidth()) / 2,
                                    (Document.get().getClientHeight() - crackPanel.getOffsetHeight()) / 2, false, true);
                }
            }).addToElement(upgTroopsImg.getElement()).register();

        } else {
            upgTroopsImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButBarracksIncreaseExpOff-Gray.png");
        }
        if (foreignArmiesOnSector) {
            upgTroopsImg.setTitle("Cannot upgrade your troops because there are foreign armies on this sector.");
        } else {
            upgTroopsImg.setTitle("Upgrade your troops.");
        }

        absolutePanel.add(upgTroopsImg, 250, 55);
        upgTroopsImg.setSize(SIZE_117PX, SIZE_174PX);

        final ImageButton addBattImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButBarracksAddBattalionOff.png");
        if (foreignArmiesOnSector) {
            addBattImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButBarracksAddBattalionOff-Gray.png");
            addBattImg.setTitle("Cannot add battalions to brigades because there are foreign forces on this sector.");
        } else {
            addBattImg.setStyleName(CLASS_POINTER);
            addBattImg.setTitle("Add Battalion.");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    final AddBattalionView addBattPanel = new AddBattalionView(barrSectorDto);
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(addBattPanel);
                    GameStore.getInstance().getLayoutView()
                            .positionTocCenter(addBattPanel);
                }
            }).addToElement(addBattImg.getElement()).register();

        }
        absolutePanel.add(addBattImg, 370, 55);
        addBattImg.setSize(SIZE_117PX, SIZE_174PX);


        final ImageButton upgHeadCImg = new ImageButton("");

        if (armyActionsEnabled && !foreignArmiesOnSector) {
            upgHeadCImg.setStyleName(CLASS_POINTER);
            upgHeadCImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButBarracksIncreaseHeadcountOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    try {
                        final UpHdCountView UpHdCountPanel = new UpHdCountView(barrSectorDto);

                        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(UpHdCountPanel);
                        GameStore.getInstance().getLayoutView()
                                .positionTocCenter(UpHdCountPanel);
                    } catch (Exception e) {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to increase headcount", false);
                    }
                    upgHeadCImg.deselect();
                }
            }).addToElement(upgHeadCImg.getElement()).register();

        } else {
            upgHeadCImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButBarracksIncreaseHeadcountOff-Gray.png");
        }
        if (foreignArmiesOnSector) {
            upgHeadCImg.setTitle("Cannot increase headcount of your troops because there are foreign armies on this sector.");
        } else {
            upgHeadCImg.setTitle("Increase battalion headcount");
        }

        absolutePanel.add(upgHeadCImg, 10, 230);
        upgHeadCImg.setSize(SIZE_117PX, SIZE_174PX);


        final Image mergeBattalions = new ImageButton("");
        if (armyActionsEnabled) {
            mergeBattalions.setStyleName(CLASS_POINTER);
            mergeBattalions.setUrl("http://static.eaw1805.com/images/panels/barracks/ButBarracksMergeBattalionsOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    MapStore.getInstance().getMapsView()
                            .getArmyOrgPanel()
                            .initBySector(barrSectorDto.getRegionId(),
                                    barrSectorDto.getX(), barrSectorDto.getY(), CORPS);
                }
            }).addToElement(mergeBattalions.getElement()).register();
        } else {
            mergeBattalions.setUrl("http://static.eaw1805.com/images/panels/barracks/ButBarracksMergeBattalionsOff-Gray.png");
        }
        mergeBattalions.setTitle("Merge Battalions.");

        absolutePanel.add(mergeBattalions, 130, 230);
        mergeBattalions.setSize(SIZE_117PX, SIZE_174PX);


        final ImageButton buildShipImage = new ImageButton("");
        if (byTheSea) {
            buildShipImage.setStyleName(CLASS_POINTER);
            buildShipImage.setUrl("http://static.eaw1805.com/images/panels/barracks/ButBarracksBuildShipOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    if (byTheSea) {
                        final BuildShipView buildShipsView = new BuildShipView(_empireShipTypeDTOs, barrSectorDto);
                        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(buildShipsView);
                        GameStore.getInstance().getLayoutView()
                                .setWidgetPosition(buildShipsView,
                                        (Document.get().getClientWidth() - buildShipsView.getOffsetWidth()) / 2,
                                        (Document.get().getClientHeight() - buildShipsView.getOffsetHeight()) / 2, false, true);
                        if (TutorialStore.getInstance().isTutorialMode()
                                && TutorialStore.getInstance().getMonth() == 9
                                && TutorialStore.getInstance().getTutorialStep() == 3) {
                            TutorialStore.nextStep(false);
                        }
                    } else {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "The sector doesn't have shipyard", false);
                    }
                }
            }).addToElement(buildShipImage.getElement()).register();

        } else {
            buildShipImage.setUrl("http://static.eaw1805.com/images/panels/barracks/ButBarracksBuildShipOff-Gray.png");
        }
        buildShipImage.setTitle("Build Ship.");

        absolutePanel.add(buildShipImage, 250, 230);
        buildShipImage.setSize(SIZE_117PX, SIZE_174PX);


        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");

        final ImageButton buildBaggageTrainImage = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButBarracksBuildBagTrainOff.png");
        buildBaggageTrainImage.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 9
                        && TutorialStore.getInstance().getTutorialStep() == 2) {
                    TutorialStore.highLightButton(imgX);
                }

                if (BaggageTrainStore.getInstance().buildNewBaggageTrain(barrSectorDto, "New Baggage Train")) {
                    new ErrorPopup(ErrorPopup.Level.NORMAL, "New Baggage Train is being constructed", false);
                }
            }
        }).addToElement(buildBaggageTrainImage.getElement()).register();

        buildBaggageTrainImage.setTitle("Build Baggage Train.");

        buildBaggageTrainImage.setSize(SIZE_117PX, SIZE_174PX);
        absolutePanel.add(buildBaggageTrainImage, 370, 230);

        new ToolTipPanel(buildBaggageTrainImage) {

            @Override
            public void generateTip() {
                setTooltip(new BaggageTrainBuildCostMini());
            }
        };


        imgX.setStyleName(CLASS_POINTER);
        imgX.setTitle("Close with any unsaved changes");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(parent);
                imgX.deselect();
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 9
                        && TutorialStore.getInstance().getTutorialStep() == 2) {
                    TutorialStore.nextStep(false);
                }
            }
        }).addToElement(imgX.getElement()).register();

        absolutePanel.add(imgX, 446, 8);
        imgX.setSize("36px", "36px");

        final Label lblBarrackid = new Label(barrack.positionToString() + " - "
                + barrSectorDto.getProductionSiteDTO().getName());
        lblBarrackid.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblBarrackid.setStyleName("clearFontMiniTitle");
        lblBarrackid.setStyleName("whiteText", true);
        absolutePanel.add(lblBarrackid, 55, 15);

        this.leftImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButLeftOff.png");
        this.leftImg.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                int index = BarrackStore.getInstance().getBarracksList().indexOf(barrack);
                if (index > 0) {
                    index--;
                } else {
                    index = BarrackStore.getInstance().getBarracksList().size() - 1;
                }

                final BarrackDTO nextBarrack = BarrackStore.getInstance().getBarracksList().get(index);
                final SectorDTO barrackSector = RegionStore.getInstance().getRegionSectorsByRegionId(nextBarrack.getRegionId())[nextBarrack.getX()][nextBarrack.getY()];

                MapStore.getInstance().getMapsView().goToPosition(nextBarrack);

                //SectorDTO sector = regionStore.getSelectedSector(mapStore.getActiveRegion());
                final BarrackShipYardView barShipView = new BarrackShipYardView(nextBarrack, barrackSector);
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW
                        (barShipView);
                GameStore.getInstance().getLayoutView().setWidgetPosition(barShipView, parent.getAbsoluteLeft(), parent.getAbsoluteTop(), false, true);
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(parent);

                leftImg.deselect();
                leftImg.setUrl(leftImg.getUrl().replace("off", "Hover"));
            }
        }).addToElement(leftImg.getElement()).register();

        absolutePanel.add(this.leftImg, 10, 9);
        this.leftImg.setSize(SIZE_35PX, SIZE_35PX);
        this.rightImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButRightOff.png");
        this.rightImg.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                int index = BarrackStore.getInstance().getBarracksList().indexOf(barrack);
                if (index < BarrackStore.getInstance().getBarracksList().size() - 1) {
                    index++;
                } else {
                    index = 0;
                }

                final BarrackDTO nextBarrack = BarrackStore.getInstance().getBarracksList().get(index);
                final SectorDTO barrackSector = RegionStore.getInstance().getRegionSectorsByRegionId(nextBarrack.getRegionId())[nextBarrack.getX()][nextBarrack.getY()];
                MapStore.getInstance().getMapsView().goToPosition(nextBarrack);

                final BarrackShipYardView barShipView = new BarrackShipYardView(nextBarrack, barrackSector);
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(barShipView);
                GameStore.getInstance().getLayoutView().setWidgetPosition(barShipView, parent.getAbsoluteLeft(), parent.getAbsoluteTop(), false, true);
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(parent);
                rightImg.deselect();
                rightImg.setUrl(rightImg.getUrl().replace("off", "Hover"));
            }
        }).addToElement(rightImg.getElement()).register();

        absolutePanel.add(this.rightImg, 408, 9);
        this.rightImg.setSize(SIZE_35PX, SIZE_35PX);

        if (TutorialStore.getInstance().isTutorialMode()
                && TutorialStore.getInstance().getMonth() == 9
                && TutorialStore.getInstance().getTutorialStep() == 1) {
            TutorialStore.nextStep(false);
        }
        //check if there is any waiting key to be pressed by this panel so tutorial can continue.
        if (TutorialStore.getInstance().isTutorialMode()
                && TutorialStore.getInstance().getAnimatedBarracksButton() > 0) {
            final ImageButton button;
            switch (TutorialStore.getInstance().getAnimatedBarracksButton()) {
                case TutorialStore.BUILD_BRIGADES:
                    button = buildBrigImg;
                    break;
                case TutorialStore.TRAIN_TROOPS:
                    button = trainImg;
                    break;
                case TutorialStore.BUILD_BTRAIN:
                    button = buildBaggageTrainImage;
                    break;
                case TutorialStore.BUILD_SHIPS:
                    button = buildShipImage;
                    break;
                default:
                    button = null;
            }
            if (button != null) {
                TutorialStore.getInstance().setActionWidget(button);
                TutorialStore.highLightButton(button);
            }
        }
    }

    private HandlerRegistration tempReg;
}
