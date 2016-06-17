package com.eaw1805.www.client.views.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.events.loading.AppInitEvent;
import com.eaw1805.www.client.events.loading.AppInitHandler;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.loading.RegionLoadedEvent;
import com.eaw1805.www.client.events.loading.RegionLoadedHandler;
import com.eaw1805.www.client.events.map.MapEventManager;
import com.eaw1805.www.client.gui.GuiComponentMovement;
import com.eaw1805.www.client.movement.FiguresGroup;
import com.eaw1805.www.client.movement.MovementGroup;
import com.eaw1805.www.client.views.military.FormFederationsView;
import com.eaw1805.www.client.views.military.FormNavyView;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.DrawingAreaWC;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.economy.TradeCityStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsView
        extends AbsolutePanel
        implements RegionConstants {

    /**
     * The basic images of the map
     */
    //private Image[] regionMaps = new Image[4];

    /**
     * The scroll panels.
     */
    private final ScrollPanel mapScrollPanel;

    /**
     * The maps minimap
     */
    private final MiniMapPanel miniMapPanel;

    /**
     * The drawing area of our game
     */
    private final DrawingAreaWC mapDrawArea = new DrawingAreaWC(0, 0, this);

    private final MapStore mapStore = MapStore.getInstance();

    private final BarrackStore barrStore = BarrackStore.getInstance();

    private final Group allMovements = new Group();

    private final LayoutPanel miniMapParent = new LayoutPanel();
    /**
     * Two lists that store the placement of the map and mini-map
     */
    private final List<List<Integer>> lkp = new ArrayList<List<Integer>>(5);
    private final List<List<Integer>> mmlkp = new ArrayList<List<Integer>>(5);

    private int armyRotatePos = -1;
    private int barrackRotatePos = -1;
    /**
     * A list indicating if the region has been loaded in the past for this session
     */
    private final Map<Integer, Boolean> regionLoadedMap = new HashMap<Integer, Boolean>();

    private final FormFederationsView armyOrgPanel = new FormFederationsView();
    private final FormNavyView navyOrgPanel = new FormNavyView();

    private final int scenarioId;

    private final int[] regionSizeX, regionSizeY;

    public MapsView() {
        scenarioId = GameStore.getInstance().getScenarioId();
        switch (scenarioId) {
            case HibernateUtil.DB_FREE:
                regionSizeX = RegionConstants.REGION_1804_SIZE_X;
                regionSizeY = RegionConstants.REGION_1804_SIZE_Y;
                break;

            case HibernateUtil.DB_S3:
                regionSizeX = RegionConstants.REGION_1808_SIZE_X;
                regionSizeY = RegionConstants.REGION_1808_SIZE_Y;
                break;

            case HibernateUtil.DB_S1:
            case HibernateUtil.DB_S2:
            default:
                regionSizeX = RegionConstants.REGION_1805_SIZE_X;
                regionSizeY = RegionConstants.REGION_1805_SIZE_Y;
                break;
        }

        this.setSize("100%", "100%");
        this.mapScrollPanel = new ScrollPanel();

        this.mapScrollPanel.addScrollHandler(new ScrollHandler() {
            public void onScroll(final ScrollEvent event) {
                if (!mapStore.isNavigating()) {
                    mapStore.setNavigating(true);
                }
            }
        });

        this.mapScrollPanel.setStyleName("noScrollBars");
        this.mapScrollPanel.setAlwaysShowScrollBars(false);
        add(mapScrollPanel);

        AbsolutePanel mapContainer = new AbsolutePanel();
        mapContainer.add(mapDrawArea, (int) MapStore.MAP_CONTAINER_OFFSET_X, (int) MapStore.MAP_CONTAINER_OFFSET_Y);
        this.mapScrollPanel.setWidget(mapContainer);
        setMapSize();
        final int regionId = MapStore.getInstance().getActiveRegion();

        this.mapDrawArea.setMapScrollPanel(mapScrollPanel);
        this.mapDrawArea.setSize(MapStore.getInstance().getPointX((int) (regionSizeX[regionId - 1] + MapStore.OFFSET_X_SEC)) + "px",
                MapStore.getInstance().getPointY((int) (regionSizeY[regionId - 1] + MapStore.OFFSET_Y_SEC)) + "px");
        mapContainer.setSize((MapStore.getInstance().getPointX((int) (regionSizeX[regionId - 1] + MapStore.OFFSET_X_SEC)) + 2 * MapStore.MAP_CONTAINER_OFFSET_X) + "px",
                (MapStore.getInstance().getPointY((int) (regionSizeY[regionId - 1] + MapStore.OFFSET_Y_SEC)) + 2 * MapStore.MAP_CONTAINER_OFFSET_X) + "px");
        this.miniMapPanel = new MiniMapPanel(mapDrawArea);
        mapDrawArea.setMiniMapPanel(miniMapPanel);
        miniMapParent.add(miniMapPanel);
        this.miniMapPanel.setSize("261px", "291px");
        miniMapParent.setSize("261px", "291px");
        this.add(miniMapParent, 0, 0);
        GameStore.getInstance().setMiniMapPanel(miniMapPanel);
        //if it is mobile device and not tutorial mode.. close minimap on startup.
        if (GameStore.getInstance().isMobileDevice() && !TutorialStore.getInstance().isTutorialMode()) {
            miniMapPanel.toggleMiniMap();
        }
        initPositioningVariables();

        LoadEventManager.addRegionLoadedHandler(new RegionLoadedHandler() {
            public void onRegionLoaded(final RegionLoadedEvent event) {
                // If we have loaded all the needed region data
                // First construct the map
                try {
                    constructBaseRegionTiles(EUROPE);
                    final Group minimap = mapStore.getMinimapGroups().getMinimapImageByRegionId(EUROPE);
                    initOnAddToMapPanel(minimap);

                    LoadEventManager.baseMapConstructed();
                } catch (Exception e) {
//                    Window.alert("onRegion2 " + e.toString());
                }
            }
        });

        LoadEventManager.addAppInitHandler(new AppInitHandler() {
            public void onApplicationInit(final AppInitEvent e) {
                constructVectorObject(EUROPE, true);
                mapDrawArea.initMapRectangle();
                mapDrawArea.reAddRectangle();
            }
        });
    }

    public List<SectorDTO> getVisibleOnScreen() {
        final List<SectorDTO> out = new ArrayList<SectorDTO>();
        double xStart = mapScrollPanel.getHorizontalScrollPosition() - mapStore.getZoomOffsetX();
        double yStart = mapScrollPanel.getVerticalScrollPosition() - mapStore.getZoomOffsetY();
        double xEnd = xStart + Window.getClientWidth();
        double yEnd = yStart + Window.getClientHeight();

        int posXStart = mapStore.getPositionX((int) xStart) - 5;
        int posYStart = mapStore.getPositionY((int) yStart) - 5;
        int posXEnd = mapStore.getPositionX((int) xEnd) + 5;
        int posYEnd = mapStore.getPositionY((int) yEnd) + 5;
        if (posXStart < 0) {
            posXStart = 0;
        }
        if (posYStart < 0) {
            posYStart = 0;
        }
        if (posXEnd >= RegionStore.getInstance().getRegionSectorsByRegionId(mapStore.getActiveRegion()).length) {
            posXEnd = RegionStore.getInstance().getRegionSectorsByRegionId(mapStore.getActiveRegion()).length - 1;
        }
        if (posYEnd >= RegionStore.getInstance().getRegionSectorsByRegionId(mapStore.getActiveRegion())[0].length) {
            posYEnd = RegionStore.getInstance().getRegionSectorsByRegionId(mapStore.getActiveRegion())[0].length - 1;
        }
        for (int indexX = posXStart; indexX < posXEnd; indexX++) {
            for (int indexY = posYStart; indexY < posYEnd; indexY++) {
                out.add(RegionStore.getInstance().getRegionSectorsByRegionId(mapStore.getActiveRegion())[indexX][indexY]);
            }
        }
        return out;
    }

    public final void setMapSize() {
        final int width = Window.getClientWidth();
        final int height = Window.getClientHeight();
        this.mapScrollPanel.setSize(width + "px", height + "px");
    }

    public void constructMap(final int regionId, final boolean regionChanged) {
        constructBaseRegionTiles(regionId);
        constructVectorObject(regionId, regionChanged);
    }

    /**
     * Constructs the basic map structure of the region
     * (tiles and basic stuff)
     *
     * @param regionId the region we want to construct
     */
    private void constructBaseRegionTiles(final int regionId) {
        mapDrawArea.clear();
        miniMapPanel.clearDrawArea();

        final Group minimapImages = mapStore.getMinimapGroups().getMinimapImageByRegionId(regionId);

        if (GameStore.getInstance().isShowPolitical()) {
            final Group mapSecImages = mapStore.getRegionMapImagesByRegionId(regionId);
            mapDrawArea.add(mapSecImages);
        } else {
            final Group mapGeoImages = mapStore.getRegionGeoMapImagesByRegionId(regionId);
            mapDrawArea.add(mapGeoImages);
        }

        miniMapPanel.setImage(minimapImages);
    }

    private void constructVectorObject(final int regionId, final boolean regionChanged) {
        try {
            if (GameStore.getInstance().isShowPolitical()) {
                final Group economicImages = mapStore.getEconomicGroups().getRegionEconomicImages(regionId);
                final Group spyReports = mapStore.getUnitGroups().getSpyReportsByRegion(regionId);
                final Group unitImages = mapStore.getUnitGroups().getRegionUnitImages(regionId);
                final Group alliedUnitImages = mapStore.getAlliedUnitGroups().getRegionAlliedUnitImages(regionId);
                final Group ForeignUnitImages = mapStore.getForeignUnitsGroup().getRegionForeignUnitImages(regionId);
                final Group ForeignReportedUnitImages = mapStore.getForeignUnitsGroup().getRegionReportedForeignUnitImages(regionId);
                final Group regionFigures = mapStore.getRegionFiguresById(regionId);
                final Group regionGrid = mapStore.getRegionGridLinesByRegionId(regionId);
                final Group influence = mapStore.getMinimapGroups().getRegionInfluenceTilesById(regionId);
                final Group battleImages = mapStore.getRegionBattleImagesByRegionId(regionId);
                final Group speedUpGroup = mapStore.getSpeedUpGroupByRegion(regionId);
                final Group trCitiesGoodsGroup = TradeCityStore.getInstance().getGroupGoodsPanelsByRegion(regionId);
                //remove previous if exist
                allMovements.clear();
                if (GameStore.getInstance().isShowMovement()) {
                    //add the new ones
                    mapStore.displayAllMovements(regionId);
                }

                mapDrawArea.add(allMovements);
                mapDrawArea.add(economicImages);
                if (GameStore.getInstance().isShowTradeCities()) {
                    mapDrawArea.add(trCitiesGoodsGroup);
                }
                mapDrawArea.add(speedUpGroup);
                mapDrawArea.add(spyReports);
                mapDrawArea.add(alliedUnitImages);
                mapDrawArea.add(ForeignUnitImages);
                mapDrawArea.add(ForeignReportedUnitImages);

                mapDrawArea.add(unitImages);
                mapDrawArea.add(regionFigures);
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().isTutorialTurn()) {
                    final Group tutorialImages = TutorialStore.getInstance().getGroup();
                    mapDrawArea.add(tutorialImages);
                }

                if (GameStore.getInstance().isShowMinimap()) {
                    showMinimap();
                } else {
                    hideMinimap();
                }

                if (GameStore.getInstance().isShowGrid()) {
                    mapDrawArea.add(regionGrid);
                }

                if (GameStore.getInstance().isShowInfluence()) {
                    mapDrawArea.add(influence);
                }

                if (GameStore.getInstance().isShowSupplyLines()) {
                    mapStore.getMinimapGroups().loadSupplies(regionId);
                }

                mapDrawArea.add(battleImages);
            }

            if (regionChanged) {
                if (!regionLoadedMap.get(regionId)) {
                    regionLoadedMap.put(regionId, true);
                    gotoStartingCoord(regionId);
//                    MapEventManager.addRegionChangedHandler(new RegionChangedHandler() {
//                        @Override
//                        public void onRegionChanged(RegionChangedEvent event) {
//                            gotoStartingCoord(regionId);
//                            MapEventManager.removeRegionChangedHandler(this);
//                        }
//                    });
                }

                MapEventManager.fireTileImagesLoaded(0);
            }
        } catch (Exception e) {
            //do nothing here
        }
    }

    public void goToPosition(final PositionDTO position) {
        final int regionId = position.getRegionId();
        final int offsetWidth = getOffsetWidth();
        final int offsetHeight = getOffsetHeight();
        int scrollPosX = mapStore.getZoomedPointX(position.getX()) + (int) mapStore.getZoomOffsetX() - (offsetWidth - 10) / 2;
        final int scrollPosY = mapStore.getZoomedPointY(position.getY()) + (int) mapStore.getZoomOffsetY() - ((offsetHeight - 10) / 2);
        if (scrollPosX < 0) {
            scrollPosX = 0;
        }
        if (scrollPosX < 0) {
            scrollPosX = 0;
        }
        lkp.get(regionId).set(0, scrollPosX);
        lkp.get(regionId).set(1, scrollPosY);
        final double mapToMiniMapX = ((regionSizeX[regionId - 1] + MapStore.OFFSET_X_SEC) * 2d) / MapStore.getInstance().getPointX(regionSizeX[regionId - 1] + (int) MapStore.OFFSET_X_SEC);
        final double mapToMiniMapY = ((regionSizeY[regionId - 1] + MapStore.OFFSET_Y_SEC) * 2d) / MapStore.getInstance().getPointY(regionSizeY[regionId - 1] + (int) MapStore.OFFSET_Y_SEC);
        mmlkp.get(regionId).set(0, (int) (scrollPosX * mapToMiniMapX));
        mmlkp.get(regionId).set(1, (int) (scrollPosY * mapToMiniMapY));
        if (!(mapStore.getActiveRegion() == regionId)) {
            MapStore.getInstance().selectRegion(regionId);
            MapStore.getInstance().setActiveRegion(regionId);
        }
        setMapToLastKnownPosition();
    }

    private void initPositioningVariables() {
        for (int region = RegionConstants.REGION_FIRST - 1; region <= RegionConstants.REGION_LAST; region++) {
            getLkp().add(new ArrayList<Integer>(2));
            getLkp().get(getLkp().size() - 1).add(0);
            getLkp().get(getLkp().size() - 1).add(0);
            getMmlkp().add(new ArrayList<Integer>(2));
            getMmlkp().get(getMmlkp().size() - 1).add(0);
            getMmlkp().get(getMmlkp().size() - 1).add(0);
            regionLoadedMap.put(region, false);
        }
    }

    public void setMapToLastKnownPosition() {
        final int regionId = mapStore.getActiveRegion();
        mapScrollPanel.setHorizontalScrollPosition(lkp.get(regionId).get(0));
        mapScrollPanel.setVerticalScrollPosition(lkp.get(regionId).get(1));
        try {
            final Rectangle rect = ((Rectangle) mapStore.getMinimapGroups().getMinimapImageByRegionId(regionId).getVectorObject(2));
            rect.setX(mmlkp.get(regionId).get(0));
            rect.setY(mmlkp.get(regionId).get(1));
        } catch (Exception ex) {
            GWT.log(ex.getMessage());
        }
    }

    public void setLastKnownPosition(final int regionId, final int mapX, final int mapY, final int mmapX, final int mmapY) {
        lkp.get(regionId).set(0, mapX);
        lkp.get(regionId).set(1, mapY);
        mmlkp.get(regionId).set(0, mmapX);
        mmlkp.get(regionId).set(1, mmapY);
    }

    public void initOnAddToMapPanel(final Group miniMap) {
        try {
            final int noTilesX = regionSizeX[mapStore.getActiveRegion() - 1] + (int) MapStore.OFFSET_X_SEC;
            final int noTilesY = regionSizeY[mapStore.getActiveRegion() - 1] + (int) MapStore.OFFSET_Y_SEC;
            mapDrawArea.setPixelSize(MapStore.getInstance().getPointX(noTilesX),
                    MapStore.getInstance().getPointY(noTilesY));

            final Image thisMap = (Image) miniMap.getVectorObject(0);
            final int height = thisMap.getHeight();
            final int width = thisMap.getWidth();
            miniMapPanel.setSize(width, height);
            final float scrollheight = (float) Window.getClientHeight() - 10;
            final int drawAreaWidth = MapStore.getInstance().getZoomedPointX(noTilesX);
            final int drawAreaHeight = MapStore.getInstance().getZoomedPointY(noTilesY);
            final float miniDrawAreaHeight = (float) (miniMapPanel.getHeight());
            final int rectHeight = (int) ((scrollheight / drawAreaHeight) * miniDrawAreaHeight);
            final int rectWidth = (int) (((Window.getClientWidth() - 10f) / drawAreaWidth) * miniMapPanel.getWidth());

            final Rectangle rect = ((Rectangle) miniMap.getVectorObject(2));
            rect.setHeight(rectHeight);
            rect.setWidth(rectWidth);

            setMapToLastKnownPosition();

        } catch (Exception ex) {
            GWT.log(ex.getMessage());
        }
    }

    public void gotoStartingCoord(final int regionId) {
        // Then find where the map should be centered
        final SectorDTO[][] sectors = RegionStore.getInstance().getRegionSectorsByRegionId(regionId);

        // Iterate all the barracks in this region
        boolean found = false;
        for (final BarrackDTO barrack : barrStore.getBarracksList()) {
            if (barrack.getRegionId() == regionId) {
                //if we found at least on barrack mark this

                final int x = barrack.getX(), y = barrack.getY();
                final String polShpere = String.valueOf(sectors[x][y].getPoliticalSphere());
                final String code = String.valueOf(sectors[x][y].getNationDTO().getCode());
                // check if the sector is a trade city
                if (sectors[x][y].getTradeCity()) {
                    found = true;
                    // If the political sphere is our nation
                    if (polShpere.compareToIgnoreCase(code) == 0) {
                        // go to this sector
                        mapStore.getMapsView().goToPosition(barrack);
                        break;

                    } else {
                        // any other case go to the first trade city barrack avail
                        mapStore.getMapsView().goToPosition(barrack);
                    }
                }
            }
        }

        // If no trade city barrack was found
        if (!found) {
            for (BarrackDTO barrack : barrStore.getBarracksList()) {
                if (barrack.getRegionId() == regionId) {
                    // go to the first availiable in this region
                    found = true;
                    mapStore.getMapsView().goToPosition(barrack);
                    break;
                }
            }
        }
        // If no barrack was found
        if (!found) {
            for (SectorDTO[] sectorsRow : sectors) {
                for (SectorDTO sector : sectorsRow) {
                    if (sector.getNationDTO().getNationId() == GameStore.getInstance().getNationId()) {
                        // go to the first sector owned by this nation
                        found = true;
                        mapStore.getMapsView().goToPosition(sector);
                        break;
                    }
                }
            }
        }

        // In any other case
        if (!found) {
            // go to the center of the region
            goToPosition(sectors[regionSizeX[regionId - 1] / 2][regionSizeY[regionId - 1] / 2]);
        }
    }


    /**
     * with this you go to the position on the map where the next army stands.
     */
    public void goToNextArmy() {
        final List<PositionDTO> armyPositions = generatePositionListForArmies();
        armyRotatePos++;
        if (armyRotatePos >= armyPositions.size()) {
            armyRotatePos = 0;
        }
        GameStore.getInstance().getLayoutView().getMap().goToPosition(armyPositions.get(armyRotatePos));
        GameStore.getInstance().getLayoutView()
                .getNotificationPopup().showMessage("Armies at " + armyPositions.get(armyRotatePos).positionToString());
    }

    public List<PositionDTO> generatePositionListForArmies() {
        final List<PositionDTO> out = new ArrayList<PositionDTO>();
        final Collection<ArmyDTO> armies = ArmyStore.getInstance().getcArmiesList();
        for (ArmyDTO army : armies) {
            if (army.getArmyId() > 0) {
                if (!out.contains(army)) {
                    out.add(army);
                }

            } else {
                for (CorpDTO corp : army.getCorps().values()) {
                    if (corp.getCorpId() > 0) {
                        if (!out.contains(corp)) {
                            out.add(corp);
                        }
                    } else {
                        for (BrigadeDTO brigade : corp.getBrigades().values()) {
                            if (!out.contains(brigade)) {
                                out.add(brigade);
                            }
                        }
                    }
                }
            }
        }
        return out;
    }

    /**
     * with this you go to the position on the map where the previous army stands.
     */
    public void goToPreviousArmy() {
        final List<PositionDTO> armyPositions = generatePositionListForArmies();
        armyRotatePos--;
        if (armyRotatePos < 0) {
            armyRotatePos = armyPositions.size() - 1;
        }
        GameStore.getInstance().getLayoutView().getMap().goToPosition(armyPositions.get(armyRotatePos));
        GameStore.getInstance().getLayoutView()
                .getNotificationPopup().showMessage("Armies at " + armyPositions.get(armyRotatePos).positionToString());
    }

    public void goToNextBarrack() {
        barrackRotatePos++;
        if (barrackRotatePos >= BarrackStore.getInstance().getBarracksList().size()) {
            barrackRotatePos = 0;
        }
        GameStore.getInstance().getLayoutView().getMap().goToPosition(BarrackStore.getInstance().getBarracksList().get(barrackRotatePos));
        GameStore.getInstance().getLayoutView().getNotificationPopup().showMessage(BarrackStore.getInstance().getBarracksName(
                BarrackStore.getInstance().getBarracksList().get(barrackRotatePos))
                + " at " + BarrackStore.getInstance().getBarracksList().get(barrackRotatePos).positionToString());

    }

    public void goToPreviousBarrack() {
        barrackRotatePos--;
        if (barrackRotatePos < 0) {
            barrackRotatePos = BarrackStore.getInstance().getBarracksList().size() - 1;

        }
        GameStore.getInstance().getLayoutView().getMap().goToPosition(BarrackStore.getInstance().getBarracksList().get(barrackRotatePos));
        GameStore.getInstance().getLayoutView().getNotificationPopup().showMessage(BarrackStore.getInstance().getBarracksName(
                BarrackStore.getInstance().getBarracksList().get(barrackRotatePos))
                + " at " + BarrackStore.getInstance().getBarracksList().get(barrackRotatePos).positionToString());
    }

    /**
     * Add figure on map.
     *
     * @param unitType       the type of the unit.
     * @param unitId         the ID of the unit.
     * @param position       the position.
     * @param movementPoints the available movement points.
     * @param power          the power of the unit.
     */
    public void addFigureOnMap(final int unitType, final int unitId,
                               final PositionDTO position,
                               final int movementPoints,
                               final int power,
                               final int conquered,
                               final int conqueredNeutral) {
        //first check if unit can move.
        if (!MovementStore.getInstance().canUnitMove(unitType, unitId)) {
            return;
        }
        final int regionId = position.getRegionId();

        mapStore.getRegionFiguresById(regionId).clear();

        //get group movement group from store if exist or if it is not an actual movement.
        MovementGroup mvGroup = MovementStore.getInstance().getMovementGroup(unitType, unitId, regionId);
        if (mvGroup == null || mvGroup.getMvDTO() == null
                || (mvGroup.getMvDTO().getPaths().size() == 0 && !mvGroup.getMvDTO().getPatrol())) {
            //else create a new one.
            mvGroup = new MovementGroup(unitType, unitId, position, movementPoints, power, conquered, conqueredNeutral);
        }

        mapStore.setNavigating(false);
        mapDrawArea.setState(DrawingAreaWC.Mode.MOVING);
        mapDrawArea.setState(DrawingAreaWC.Mode.MOVING);
        mapStore.getRegionFiguresById(regionId).add(mvGroup);

        // In case the move has been started before and
        // now we have done a second phase action
        if (TradeStore.getInstance().hasInitSecondPhase(unitType, unitId)) {
            final FiguresGroup figure = (FiguresGroup) mvGroup.getFigures().getVectorObject(mvGroup.getFigures().getVectorObjectCount() - 1);
            figure.getTargetTiles().clear();
            figure.getMenu().startNextStep();
        }

        //register movement so it can be canceled with escape...
        final GuiComponentMovement movementComp = new GuiComponentMovement(unitId, unitType, mvGroup);
        movementComp.registerComponent();
    }

    public void hideMinimap() {
        if (miniMapPanel.isAttached()) {
            remove(miniMapParent);
        }
    }

    public void showMinimap() {
        if (!miniMapPanel.isAttached()) {
            add(miniMapParent, 0, 0);
        }
    }

    /**
     * @return the lkp
     */
    public List<List<Integer>> getLkp() {
        return lkp;
    }

    /**
     * @return the mmlkp
     */
    public List<List<Integer>> getMmlkp() {
        return mmlkp;
    }

    /**
     * @return the mapDrawArea
     */
    public DrawingAreaWC getMapDrawArea() {
        return mapDrawArea;
    }

    /**
     * @return the armyOrgPanel
     */
    public FormFederationsView getArmyOrgPanel() {
        return armyOrgPanel;
    }

    /**
     * @return the navyOrgPanel
     */
    public FormNavyView getNavyOrgPanel() {
        return navyOrgPanel;
    }

    public Group getAllMovements() {
        return allMovements;
    }

}
