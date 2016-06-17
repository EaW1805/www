package com.eaw1805.www.client.views.popups.actions;

import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.BarrackShipYardView;
import com.eaw1805.www.client.views.economy.HandOverLandView;
import com.eaw1805.www.client.views.economy.buildSite.ProductionSiteBuildView;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.shared.StaticWidgets;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.economy.ProductionSiteStore;
import com.eaw1805.www.shared.stores.economy.TradeCityStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ForeignUnitsStore;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;
import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Fade;

import java.util.List;


public class TileActionsPopup
        extends PopupPanelEAW
        implements HasMouseOutHandlers, ProductionSiteConstants, RelationConstants {

    private AbsolutePanel basePanel;
    private ImageButton buildImg;
    private ImageButton popUpImg, popDownImg;
    private ImageButton cedeTer;

    private RegionStore regionStore = RegionStore.getInstance();
    private MapStore mapStore = MapStore.getInstance();
    private ImageButton openBarrackImg;
    private BarrackDTO barrack = null;
    private Image rightBrdImg;
    private Image butt5Img;
    private Image leftBrdImg;
    private Image butt1Img;
    private Image butt2Img;
    private Image butt3Img;
    private Image butt4Img;


    private int countUpDown = 0;
    private static final int lowOpacity = 50;
    private static final int highOpacity = 100;

    public TileActionsPopup() {
        final TileActionsPopup mySelf = this;
        this.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                mySelf.hide();
            }
        });
        setStyleName("none");
        setAutoHideEnabled(true);

        basePanel = new AbsolutePanel();
        setWidget(basePanel);
        basePanel.setSize("279px", "137px");

        setupBackground();

        buildImg = StaticWidgets.BUILD_IMAGE_TILE_ACTIONS_POPUP;
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (!buildImg.isDisabled()) {
                    final SectorDTO selTileSector = regionStore.getSelectedSector(mapStore.getActiveRegion());
                    final int nationId = GameStore.getInstance().getNationId();
                    if (nationId == selTileSector.getNationId()) {

                        final int psId = selTileSector.getProductionSiteId();

                        if (!ProductionSiteStore.getInstance().getSectorProdSites().containsKey(selTileSector)) {
                            // if we have not demolished it this round
                            int psToBuild;

                            if (ProductionSiteStore.getInstance().getSectorProdSites().get(selTileSector) != null) {
                                psToBuild = ProductionSiteStore.getInstance().getSectorProdSites().get(selTileSector);

                            } else {
                                psToBuild = -1;
                            }
                            if ((!ProductionSiteStore.getInstance().getSectorProdSites().containsKey(selTileSector)) ||
                                    (psToBuild != -1
                                            && psToBuild != ProductionSiteConstants.PS_BARRACKS
                                            && psToBuild != ProductionSiteConstants.PS_BARRACKS_FS
                                            && psToBuild != ProductionSiteConstants.PS_BARRACKS_FM
                                            && psToBuild != ProductionSiteConstants.PS_BARRACKS_FH)) {
                                final ProductionSiteBuildView prBuildView = new ProductionSiteBuildView(selTileSector);
                                GameStore.getInstance().getLayoutView()
                                        .addWidgetToLayoutPanelEAW(prBuildView);
                                GameStore.getInstance().getLayoutView().positionTocCenter(prBuildView);

                            }
                        } else {
                            ProductionSiteStore.getInstance().cancelOrder(selTileSector, true);
                        }
                        setProductionSiteInfo(selTileSector);
                    }
                }
            }
        }).addToElement(buildImg.getElement()).register();


        buildImg.setTitle("Build new production site.");
        buildImg.setStyleName("pointer");
        buildImg.setSize("36px", "36px");

        popUpImg = StaticWidgets.POPUP_IMAGE_TILE_ACTIONS_POPUP;
        popUpImg.setSize("36px", "36px");
        popUpImg.setStyleName("pointer", true);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (!popUpImg.isDisabled()) {
                    final SectorDTO selTileSector = regionStore.getSelectedSector(mapStore.getActiveRegion());
                    if (regionStore.isIncreased(selTileSector.getId())) {
                        regionStore.cancelOrder(selTileSector);
                        popUpImg.deselect();

                    } else {
                        if (selTileSector.getPopulation() >= selTileSector.getTerrain().getMaxDensity()) {
                            popUpImg.setVisible(false);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "Sector already reached maximum population size.", false);

                        } else {
                            regionStore.increasePopulation(selTileSector);
                            popUpImg.setSelected(true);
                            popDownImg.deselect();
                        }
                    }
                }
            }
        }).addToElement(popUpImg.getElement()).register();


        popDownImg = StaticWidgets.POPDOWN_IMAGE_TILE_ACTIONS_POPUP;
        popDownImg.setSize("36px", "36px");
        popDownImg.setStyleName("pointer", true);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (!popDownImg.isDisabled()) {
                    final SectorDTO selTileSector = regionStore.getSelectedSector(mapStore.getActiveRegion());
                    if (regionStore.isDecreased(selTileSector.getId())) {
                        regionStore.cancelOrder(selTileSector);
                        popDownImg.deselect();

                    } else {
                        regionStore.decreasePopulation(selTileSector);
                        popDownImg.setSelected(true);
                        popUpImg.deselect();
                    }
                }
            }
        }).addToElement(popDownImg.getElement()).register();

        openBarrackImg = StaticWidgets.OPEN_BARRACK_IMAGE_TILE_ACTIONS_POPUP;
        openBarrackImg.setTitle("Build new production site.");
        openBarrackImg.setStyleName("pointer");
        openBarrackImg.setSize("36px", "36px");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (barrack != null
                        && !openBarrackImg.isDisabled()) {
                    final SectorDTO sector = regionStore.getSelectedSector(mapStore.getActiveRegion());
                    final BarrackShipYardView barrackShipYardView = new BarrackShipYardView(barrack, sector);
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(barrackShipYardView);
                    GameStore.getInstance().getLayoutView().positionTocCenter(barrackShipYardView);

                    if (TutorialStore.getInstance().isTutorialMode()
                            && TutorialStore.getInstance().getMonth() == 5
                            && TutorialStore.getInstance().getTutorialStep() == 1) {
                        TutorialStore.getInstance().clearPositionClickedHandler();
                        TutorialStore.nextStep(false);
                    }
                    if (TutorialStore.getInstance().isTutorialMode()
                            && TutorialStore.getInstance().getMonth() == 6
                            && TutorialStore.getInstance().getTutorialStep() == 1) {
                        TutorialStore.getInstance().clearPositionClickedHandler();
                    }
                }
            }
        }).addToElement(openBarrackImg.getElement()).register();

        cedeTer = StaticWidgets.HAND_OVER_IMAGE_TILE_ACTIONS_POPUP;
        cedeTer.setTitle("Click to hand over territory to ally");
        cedeTer.setStyleName("pointer", true);

        cedeTer.setSize("36px", "36px");
        cedeTer.deselect();

        cedeTer.setUrl("http://static.eaw1805.com/images/buttons/ButHandOverTileOff.png");
        cedeTer.setTitle("Click to hand over territory to ally");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (!cedeTer.isDisabled()) {
                    final HandOverLandView hoLandView = new HandOverLandView(regionStore.getSelectedSector(mapStore.getActiveRegion()));
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(hoLandView);
                    GameStore.getInstance().getLayoutView().positionTocCenter(hoLandView);
                }
            }
        }).addToElement(cedeTer.getElement()).register();


    }


    public void initActions(SectorDTO selSector) {
        setSelectedSectorInfo(selSector);
    }

    private void setProductionSiteInfo(final SectorDTO sector) {
        final ProductionSiteStore eStore = ProductionSiteStore.getInstance();
        final int nationId = GameStore.getInstance().getNationId();
        final int psId = sector.getProductionSiteId();
        if (nationId == sector.getNationId()) {
            if (sector.getProductionSiteId() > 0) {
                buildImg.setSelected(true);
                buildImg.setUrl("http://static.eaw1805.com/images/buttons/ButBuildProdSiteOn.png");

            } else if (eStore.getSectorProdSites().containsKey(sector)) {
                buildImg.setSelected(true);
                buildImg.setUrl("http://static.eaw1805.com/images/buttons/ButBuildProdSiteOn.png");

            } else {
                buildImg.setUrl("http://static.eaw1805.com/images/buttons/ButBuildProdSiteOff.png");
                buildImg.deselect();
            }

            if (psId == ProductionSiteConstants.PS_BARRACKS
                    || psId == ProductionSiteConstants.PS_BARRACKS_FS
                    || psId == ProductionSiteConstants.PS_BARRACKS_FM
                    || psId == ProductionSiteConstants.PS_BARRACKS_FL) {
                // if we have not demolished it this round
                int psToBuild = -1;
                if (ProductionSiteStore.getInstance().getSectorProdSites().get(sector) != null) {
                    psToBuild = ProductionSiteStore.getInstance().getSectorProdSites().get(sector);

                } else {
                    psToBuild = -1;
                }

                if ((!ProductionSiteStore.getInstance().getSectorProdSites().containsKey(sector)) ||
                        (ProductionSiteStore.getInstance().getSectorProdSites().containsKey(sector)
                                && psToBuild != -1
                                && psToBuild != ProductionSiteConstants.PS_BARRACKS
                                && psToBuild != ProductionSiteConstants.PS_BARRACKS_FS
                                && psToBuild != ProductionSiteConstants.PS_BARRACKS_FM
                                && psToBuild != ProductionSiteConstants.PS_BARRACKS_FL)) {
                    buildImg.setUrl("http://static.eaw1805.com/images/buttons/ButBuildProdSiteOff.png");
                    buildImg.deselect();
                }
            }

        } else {
            buildImg.setUrl("http://static.eaw1805.com/images/buttons/buildPSNA.png");

        }
    }

    public void setSelectedSectorInfo(final SectorDTO sector) {
        if (sector != null) {
            if (sector.getNationId() == GameStore.getInstance().getNationId()) {
                // Check if sector has reached max population size
                if (sector.getPopulation() >= sector.getTerrain().getMaxDensity()) {
                    popUpImg.setVisible(false);

                } else {
                    popUpImg.setVisible(true);
                    if (RegionStore.getInstance().isIncreased(sector.getId())) {
                        popUpImg.setSelected(true);
                    } else {
                        popUpImg.deselect();
                    }
                }

                if (RegionStore.getInstance().isDecreased(sector.getId())) {
                    popDownImg.setSelected(true);
                } else {
                    popDownImg.deselect();
                }
            }

            setProductionSiteInfo(sector);
            setBarrackCommands(sector);
            updateBackgroundPositioning(sector);
        } else {

        }
    }

    private void setBarrackCommands(SectorDTO sector) {
        final BarrackDTO barrack = BarrackStore.getInstance().getBarrackByPosition(sector);
        this.barrack = barrack;
        if (barrack != null) {

            if (barrack.getNotSupplied()) {
                openBarrackImg.setDisabled(true);
                openBarrackImg.setTitle("The barrack is not supplied.");

            } else {

                openBarrackImg.setDisabled(false);
                openBarrackImg.setTitle("Click to open barrack");
                if (ProductionSiteStore.getInstance().getSectorProdSites().containsKey(sector)) {
                    if (ProductionSiteStore.getInstance().getSectorProdSites().get(sector) == -1) {
                        openBarrackImg.setDisabled(true);
                        openBarrackImg.setTitle("You gave order to demolish this barrack. If you want to use the barrack cancel that order.");
                    }
                }
            }

        } else {
            openBarrackImg.setDisabled(true);
            openBarrackImg.setTitle("There is no barrack available on this tile");
        }
    }


    public HandlerRegistration addMouseOutHandler(final MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    public void setupBackground() {
        leftBrdImg = new Image("http://static.eaw1805.com/images/buttons/menu/LeftEnd.png");
        leftBrdImg.setSize("37px", "73px");

        rightBrdImg = new Image("http://static.eaw1805.com/images/buttons/menu/RightEnd.png");
        rightBrdImg.setSize("37px", "73px");

        butt1Img = new Image("http://static.eaw1805.com/images/buttons/menu/Middle.png");
        butt1Img.setSize("41px", "73px");

        butt2Img = new Image("http://static.eaw1805.com/images/buttons/menu/Middle.png");
        butt2Img.setSize("41px", "73px");

        butt3Img = new Image("http://static.eaw1805.com/images/buttons/menu/Middle.png");
        butt3Img.setSize("41px", "73px");

        butt4Img = new Image("http://static.eaw1805.com/images/buttons/menu/Middle.png");
        butt4Img.setSize("41px", "73px");

        butt5Img = new Image("http://static.eaw1805.com/images/buttons/menu/Middle.png");
        butt5Img.setSize("41px", "73px");
    }

    public void updateBackgroundPositioning(final SectorDTO sector) {
        basePanel.add(leftBrdImg, 0, 0);
        int posX = 37;

        if (!GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
            final List<ArmyDTO> foreignArmies = ForeignUnitsStore.getInstance().getArmiesBySectorRelation(sector, REL_TRADE, REL_COLONIAL_WAR, REL_WAR);
            final boolean foreignUnits = (foreignArmies != null && foreignArmies.size() > 0);
            if (!foreignUnits || sector.getTradeCity()) {
                basePanel.add(butt1Img, posX, 0);
                basePanel.add(buildImg, posX + 3, 19);
                posX += 41;
            }

            // check if sector has large or huge fort
            final boolean hasLargeHugeFort = (sector.getProductionSiteId() == ProductionSiteConstants.PS_BARRACKS_FL
                    || sector.getProductionSiteId() == ProductionSiteConstants.PS_BARRACKS_FH);

            // check if the sector is a trade city
            final TradeCityDTO city = TradeCityStore.getInstance().getTradeCityByPosition(sector);
            final boolean isTradeCity = (city != null);

            if (sector != null) {
                if (sector.getNationId() == GameStore.getInstance().getNationId()) {
                    // Check if sector has reached max population size
                    if (sector.getPopulation() >= sector.getTerrain().getMaxDensity()
                            || (sector.getPopulation() >= 4 && CostCalculators.getSphere(sector, sector.getNationDTO()) == 3)) {
                        popUpImg.setVisible(false);

                    } else {
                        basePanel.add(butt2Img, posX, 0);
                        basePanel.add(popUpImg, posX + 3, 19);
                        posX += 41;
                    }
                }
            }

            if (sector.getPopulation() > 0 && sector.getConqueredCounter() == 0 && !foreignUnits) {
                butt3Img.setVisible(true);
                popDownImg.setVisible(true);
                basePanel.add(butt3Img, posX, 0);
                basePanel.add(popDownImg, posX + 3, 19);
                posX += 41;

            } else {
                butt3Img.setVisible(false);
                popDownImg.setVisible(false);
            }

            if (barrack == null || (foreignUnits && !hasLargeHugeFort && !isTradeCity)) {
                butt4Img.setVisible(false);
                openBarrackImg.setVisible(false);

            } else {
                butt4Img.setVisible(true);
                openBarrackImg.setVisible(true);
                basePanel.add(butt4Img, posX, 0);
                basePanel.add(openBarrackImg, posX + 3, 19);
                posX += 41;
            }

            basePanel.add(butt5Img, posX, 0);
            basePanel.add(cedeTer, posX + 3, 19);
            posX += 41;

        } else {
            //if empire is dead construct the dead image
            basePanel.add(butt1Img, posX, 0);
            final Image scull1 = new Image("http://static.eaw1805.com/img/commanders/skull.png");
            scull1.setSize("36px", "36px");
            posX += 41;
        }
        basePanel.add(rightBrdImg, posX, 0);
        basePanel.setSize((posX + 41) + "px", "137px");

    }


    public void highLightButton(Widget w) {
        final Fade f;
        f = new Fade(w.getElement());
        f.setDuration(1);
        f.addEffectCompletedHandler(new EffectCompletedHandler() {
            @Override
            public void onEffectCompleted(final EffectCompletedEvent event) {
                countUpDown++;
                if (countUpDown <= 7) {
                    if (countUpDown % 2 == 1) {
                        f.setStartOpacity(lowOpacity);
                        f.setEndOpacity(highOpacity);
                    } else {
                        f.setStartOpacity(highOpacity);
                        f.setEndOpacity(lowOpacity);
                    }
                    f.play();
                }
            }
        });
        countUpDown = 1;
        f.setStartOpacity(lowOpacity);
        f.setEndOpacity(highOpacity);
        f.play();
    }

    public void setDisabledButtons(ImageButton... buttons) {
        buildImg.setDisabled(false);
        popUpImg.setDisabled(false);
        popDownImg.setDisabled(false);
        cedeTer.setDisabled(false);
        for (final ImageButton button : buttons) {
            button.setDisabled(true);
        }
    }

}
