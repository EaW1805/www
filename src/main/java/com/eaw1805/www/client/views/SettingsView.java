package com.eaw1805.www.client.views;


import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.StyledCheckBox;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.SoundStore;
import com.eaw1805.www.shared.stores.economy.TradeCityStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import org.vaadin.gwtgraphics.client.Group;

public class SettingsView
        extends DraggablePanel {

    private final StyledCheckBox chbShowArmies;
    private final StyledCheckBox chbShowMovements;
    private final StyledCheckBox chbShowNavy;
    private final StyledCheckBox chbShowNationInfluence;
    private final StyledCheckBox chbShowBorders;
    private final StyledCheckBox chbPopSizes;
    private final StyledCheckBox chbShowGrid;
    private final StyledCheckBox chbShowReportedUnits;
    private final StyledCheckBox chbPlayMusic;
    private final StyledCheckBox chbPlaySoundEffects;
    private final StyledCheckBox chbLowRes;
    private final StyledCheckBox chbRememberZoom;
    private final StyledCheckBox chbShowSupplyLines;
    private final StyledCheckBox chbTradeCities;
    private final StyledCheckBox chbFullScreen;

    public SettingsView(final ImageButton caller) {
        final MapStore mapStore = MapStore.getInstance();
        setStyleName("settingsPanel");
        setStyleName("clearFont", true);
        setSize("490px", "361px");

        chbShowGrid = new StyledCheckBox("Grid", false, false);
        chbShowGrid.setTitle("Show map grid");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final boolean selected = chbShowGrid.isChecked();
                if (selected) {
                    chbShowGrid.setTitle("Hide map grid");
                    mapStore.getMapsView().getMapDrawArea().add(mapStore.getRegionGridLinesByRegionId(mapStore.getActiveRegion()));
                } else {
                    chbShowGrid.setTitle("Show map grid");
                    mapStore.getMapsView().getMapDrawArea().remove(mapStore.getRegionGridLinesByRegionId(mapStore.getActiveRegion()));

                }
                GameStore.getInstance().setShowGrid(selected);
                GameStore.getInstance().getSettings().setGrid(selected);
            }
        }).addToElement(chbShowGrid.getCheckBox().getElement()).register();
        add(chbShowGrid, 20, 65);

        chbPopSizes = new StyledCheckBox("Population Density", false, false);
        chbPopSizes.setSize("210px", "20px");
        chbPopSizes.setTitle("Show the population density on the map");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final boolean selected = chbPopSizes.isChecked();
                if (selected) {
                    chbPopSizes.setTitle("Hide the population density on the map");
                    mapStore.getEconomicGroups().getRegionPopSizes(mapStore.getActiveRegion()).setVisible(true);
                } else {
                    chbPopSizes.setTitle("Show the population density on the map");
                    mapStore.getEconomicGroups().getRegionPopSizes(mapStore.getActiveRegion()).setVisible(false);

                }
                GameStore.getInstance().setShowPopulation(selected);
                GameStore.getInstance().getSettings().setPopulationDensity(selected);
            }
        }).addToElement(chbPopSizes.getCheckBox().getElement()).register();
        add(chbPopSizes, 20, 100);

        chbShowBorders = new StyledCheckBox("Political Borders", true, false);
        chbShowBorders.setTitle("Hide the Empires borders on the map");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final String baseMapURL = "http://static.eaw1805.com/maps/";
                final boolean selected = chbShowBorders.isChecked();
                GameStore.getInstance().setShowBorders(selected);
                if (selected) {
                    chbShowBorders.setTitle("Hide the Empires' borders on the map");

                } else {
                    chbShowBorders.setTitle("Show the Empires' borders on the map");
                }
                GameStore.getInstance().getSettings().setPoliticalBorders(selected);
                mapStore.setBorders(selected);
                mapStore.changeMapHref();
            }
        }).addToElement(chbShowBorders.getCheckBox().getElement()).register();
        add(chbShowBorders, 20, 135);

        chbShowNationInfluence = new StyledCheckBox("Sphere of Influence", false, false);
        chbShowNationInfluence.setTitle("Show the Sphere of Influence of your Empire on the map");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final boolean selected = chbShowNationInfluence.isChecked();
                if (selected) {
                    chbShowNationInfluence.setTitle("Hide the Sphere of Influence of your Empire on the map");
                    mapStore.getMapsView().getMapDrawArea().add(mapStore.getMinimapGroups().getRegionInfluenceTilesById(mapStore.getActiveRegion()));

                } else {
                    chbShowNationInfluence.setTitle("Show the Sphere of Influence of your Empire on the map");
                    mapStore.getMapsView().getMapDrawArea().remove(mapStore.getMinimapGroups().getRegionInfluenceTilesById(mapStore.getActiveRegion()));
                }
                GameStore.getInstance().setShowInfluence(selected);
                GameStore.getInstance().getSettings().setSphereOfInfluence(selected);
            }
        }).addToElement(chbShowNationInfluence.getCheckBox().getElement()).register();
        add(chbShowNationInfluence, 20, 170);


        chbLowRes = new StyledCheckBox("Low Resolution", true, false);
        chbLowRes.setTitle("Low Resolution for the map");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final boolean selected = chbLowRes.isChecked();
                GameStore.getInstance().setLowResolution(selected);
                GameStore.getInstance().getSettings().setLowResolution(selected);
                if (selected) {
                    chbLowRes.setTitle("Switch to High Resolution for the map");

                } else {
                    chbLowRes.setTitle("Switch to Low Resolution for the map");
                }
                mapStore.setResolution(selected);
                mapStore.changeMapHref();
            }
        }).addToElement(chbLowRes.getCheckBox().getElement()).register();
        add(chbLowRes, 20, 205);


        chbRememberZoom = new StyledCheckBox("Remember Zoom Level", true, false);
        chbRememberZoom.setTitle("Remember zoom level for the political map");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final boolean selected = chbRememberZoom.isChecked();
                if (selected) {
                    chbLowRes.setTitle("Do not remember the zoom level after the game is closed");
                    GameStore.getInstance().setZoomLevel(mapStore.getZoomLevelSettings());
                    GameStore.getInstance().getSettings().setZoom(mapStore.getZoomLevelSettings());

                } else {
                    chbLowRes.setTitle("Remember the zoom level after the game is closed");
                    GameStore.getInstance().setZoomLevel(-1d);
                    GameStore.getInstance().getSettings().setZoom(-1d);
                }
            }
        }).addToElement(chbRememberZoom.getCheckBox().getElement()).register();
        add(chbRememberZoom, 20, 240);


        chbTradeCities = new StyledCheckBox("Trade Cities", true, false);
        chbTradeCities.setTitle("Show trade cities trade rates on map");
        (new DelEventHandlerAbstract() {

            @Override
            public void execute(MouseEvent event) {
                final boolean selected = chbTradeCities.isChecked();
                if (selected) {
                    chbTradeCities.setTitle("Hide trade cities trade rates from map");
                    mapStore.getMapsView().getMapDrawArea().add(TradeCityStore.getInstance().getGroupGoodsPanelsByRegion(mapStore.getActiveRegion()));
                } else {
                    chbTradeCities.setTitle("Show trade cities trade rates on map");
                    mapStore.getMapsView().getMapDrawArea().remove(TradeCityStore.getInstance().getGroupGoodsPanelsByRegion(mapStore.getActiveRegion()));
                }
                GameStore.getInstance().setShowTradeCities(selected);
                GameStore.getInstance().getSettings().setTradeCities(selected);
            }
        }).addToElement(chbTradeCities.getCheckBox().getElement()).register();
        add(chbTradeCities, 20, 275);

        // =================================================================

        chbShowArmies = new StyledCheckBox("Land Forces", true, false);
        chbShowArmies.setTitle("Hide Land forces on the map");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final boolean selected = chbShowArmies.isChecked();
                if (selected) {
                    chbShowArmies.setTitle("Hide Land forces on the map");
                    final int regionId = mapStore.getActiveRegion();
                    mapStore.getUnitGroups().getRegionUnitImages(regionId).add(mapStore.getUnitGroups().getRegionArmyImagesById(regionId));
                } else {
                    chbShowArmies.setTitle("Show Land forces on the map");
                    final int regionId = mapStore.getActiveRegion();
                    mapStore.getUnitGroups().getRegionUnitImages(regionId).remove(mapStore.getUnitGroups().getRegionArmyImagesById(regionId));

                }
                GameStore.getInstance().setShowArmies(selected);
                GameStore.getInstance().getSettings().setLandForces(selected);
            }
        }).addToElement(chbShowArmies.getCheckBox().getElement()).register();
        add(chbShowArmies, 248, 65);

        chbShowNavy = new StyledCheckBox("Naval Forces", true, false);
        chbShowNavy.setTitle("Hide Naval forces on the map");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final boolean selected = chbShowNavy.isChecked();
                if (selected) {
                    chbShowNavy.setTitle("Hide Naval forces on the map");
                    final int regionId = mapStore.getActiveRegion();
                    mapStore.getUnitGroups().getRegionUnitImages(regionId).add(mapStore.getUnitGroups().getFleetsByRegionId(regionId));
                } else {
                    chbShowNavy.setTitle("Show Naval forces on the map");
                    final int regionId = mapStore.getActiveRegion();
                    mapStore.getUnitGroups().getRegionUnitImages(regionId).remove(mapStore.getUnitGroups().getFleetsByRegionId(regionId));
                }
                GameStore.getInstance().setShowNavy(selected);
                GameStore.getInstance().getSettings().setNavalForces(selected);
            }
        }).addToElement(chbShowNavy.getCheckBox().getElement()).register();

        add(chbShowNavy, 248, 100);

        chbShowMovements = new StyledCheckBox("Movements", true, false);
        chbShowMovements.setTitle("Show all movement orders on the map");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final boolean selected = chbShowMovements.isChecked();
                try {
                    if (selected) {
                        chbShowMovements.setTitle("Hide movement orders on the map");
                        final int regionId = mapStore.getActiveRegion();
                        mapStore.displayAllMovements(regionId);

                    } else {
                        chbShowMovements.setTitle("Show movement orders on the map");
                        mapStore.removeAllMovements();
                    }
                    GameStore.getInstance().setShowMovement(selected);
                    GameStore.getInstance().getSettings().setMovements(selected);
                } catch (Exception e) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Could not perform action", false);
                }
            }
        }).addToElement(chbShowMovements.getCheckBox().getElement()).register();
        add(chbShowMovements, 248, 135);


        chbShowReportedUnits = new StyledCheckBox("Reported Units", false, false);
        chbShowReportedUnits.setTitle("Show Virtual units reported by spies");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final boolean selected = chbShowReportedUnits.isChecked();
                if (selected) {
                    chbShowReportedUnits.setTitle("Hide Virtual units reported by spies");
                    mapStore.getMapsView().getMapDrawArea().add(mapStore.getForeignUnitsGroup().getRegionReportedForeignUnitImages(mapStore.getActiveRegion()));
                } else {
                    chbShowReportedUnits.setTitle("Show Virtual units reported by spies");
                    mapStore.getMapsView().getMapDrawArea().remove(mapStore.getForeignUnitsGroup().getRegionReportedForeignUnitImages(mapStore.getActiveRegion()));
                }
                GameStore.getInstance().setShowVirtualReportedUnits(selected);
                GameStore.getInstance().getSettings().setVirtualReportedUnits(selected);
            }
        }).addToElement(chbShowReportedUnits.getCheckBox().getElement()).register();
        add(chbShowReportedUnits, 248, 170);


        chbShowSupplyLines = new StyledCheckBox("Supply Lines", false, false);
        chbShowSupplyLines.setTitle("Display Supply Lines on the map");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final boolean selected = chbShowSupplyLines.isChecked();
                if (selected) {
                    chbShowSupplyLines.setTitle("Hide supply lines on the map");
                    mapStore.getMinimapGroups().loadSupplies(mapStore.getActiveRegion());

                } else {
                    chbShowSupplyLines.setTitle("Show supply lines on the map");
                    final Group supplies = mapStore.getMinimapGroups().getRegionSuppliesTilesById(mapStore.getActiveRegion());
                    if (supplies != null) {
                        mapStore.getMapsView().getMapDrawArea().remove(supplies);
                    }
                }
                GameStore.getInstance().setShowSupplyLines(selected);
            }
        }).addToElement(chbShowSupplyLines.getCheckBox().getElement()).register();
        add(chbShowSupplyLines, 248, 205);


        chbPlayMusic = new StyledCheckBox("Enable Music", true, false);
        chbPlayMusic.setTitle("Enable Music");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final boolean selected = chbPlayMusic.isChecked();
                GameStore.getInstance().setPlayMusic(selected);
                GameStore.getInstance().getSettings().setMusic(selected);
                if (selected) {
                    chbPlayMusic.setTitle("Disable Music");
                    SoundStore.getInstance().initIntro();
                } else {
                    chbPlayMusic.setTitle("Enable Music");
                    SoundStore.getInstance().stopIntro();
                }
            }
        }).addToElement(chbPlayMusic.getCheckBox().getElement()).register();
        add(chbPlayMusic, 248, 240);

        chbPlaySoundEffects = new StyledCheckBox("Enable Sound Effects", true, false);
        chbPlaySoundEffects.setTitle("Enable Sound Effects");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final boolean selected = chbPlaySoundEffects.isChecked();
                if (selected) {
                    chbPlaySoundEffects.setTitle("Disable Sound Effects");
                } else {
                    chbPlaySoundEffects.setTitle("Enable Sound Effects");
                }
                GameStore.getInstance().setPlaySoundEffects(selected);
                GameStore.getInstance().getSettings().setSoundEffects(selected);
            }
        }).addToElement(chbPlaySoundEffects.getCheckBox().getElement()).register();
        add(chbPlaySoundEffects, 248, 275);

        chbFullScreen = new StyledCheckBox("Toggle Full screen", false, false);
        chbFullScreen.setTitle("Toggle full screen");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final boolean selected = chbFullScreen.isChecked();
                GameStore.getInstance().getSettings().setFullscreen(selected);
                mapStore.toggleFullScreen(selected);
            }
        }).addToElement(chbFullScreen.getCheckBox().getElement()).register();
        add(chbFullScreen, 248, 310);


        final Label title = new Label("Display options");
        title.setStyleName("clearFont-large whiteText");
        add(title, 25, 9);

        final SettingsView self = this;
        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        imgX.setSize("36px", "36px");

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(self);
                caller.deselect();
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        add(imgX, 425, 9);
    }

    public StyledCheckBox getChbShowArmies() {
        return chbShowArmies;
    }

    public StyledCheckBox getChbShowMovements() {
        return chbShowMovements;
    }

    public StyledCheckBox getChbShowNavy() {
        return chbShowNavy;
    }

    public StyledCheckBox getChbShowNationInfluence() {
        return chbShowNationInfluence;
    }

    public StyledCheckBox getChbShowBorders() {
        return chbShowBorders;
    }

    public StyledCheckBox getChbPopSizes() {
        return chbPopSizes;
    }

    public StyledCheckBox getChbShowGrid() {
        return chbShowGrid;
    }

    public StyledCheckBox getChbShowReportedUnits() {
        return chbShowReportedUnits;
    }

    public StyledCheckBox getChbPlayMusic() {
        return chbPlayMusic;
    }

    public StyledCheckBox getChbPlaySoundEffects() {
        return chbPlaySoundEffects;
    }

    public StyledCheckBox getChbLowRes() {
        return chbLowRes;
    }

    public StyledCheckBox getChbRememberZoom() {
        return chbRememberZoom;
    }

    public StyledCheckBox getChbTradeCities() {
        return chbTradeCities;
    }

    public StyledCheckBox getChbFullScreen() {
        return chbFullScreen;
    }
}
