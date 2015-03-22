package com.eaw1805.www.shared.stores.map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.constants.TerrainConstants;
import com.eaw1805.data.dto.common.BattleDTO;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.www.client.events.map.MapEventManager;
import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.events.movement.MovementStopEvent;
import com.eaw1805.www.client.events.movement.MovementStopHandler;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.frames.BattleFrame;
import com.eaw1805.www.client.views.layout.MapsView;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.MapImage;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;
import com.eaw1805.www.shared.stores.support.MapConstants;
import com.eaw1805.www.shared.stores.util.JumpOffCoordinates1804;
import com.eaw1805.www.shared.stores.util.JumpOffCoordinates1805;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MapStore
        implements MapConstants, RegionConstants, OrderConstants, TerrainConstants, StyleConstants {

    /**
     * Our instance of the Map manager
     */
    private static transient MapStore ourInstance = null;

    /**
     * The instance of the class containing the
     * unit groups
     */
    private final UnitGroups unitGroups = new UnitGroups();

    /**
     * The instance of the class containing the
     * unit groups
     */
    private final AlliedUnitsGroup alliedUnitGroups = new AlliedUnitsGroup();

    private final ForeignUnitsGroup foreignUnitsGroup = new ForeignUnitsGroup();

    /**
     * The instance of the class containing the
     * economic groups
     */
    private final EconomicGroups economicGroups = new EconomicGroups();

    /**
     * The instance of the class containing the
     * minimap groups
     */
    private final transient MinimapGroups minimapGroups = MinimapGroups.getInstance();

    /**
     * The instance of region store
     */
    private static transient RegionStore regionStore = RegionStore.getInstance();

    private static transient Map<Integer, SpeedUpGroup> speedUpGroups = new HashMap<Integer, SpeedUpGroup>();

    /**
     * An instance of the clients maps view set by the region async
     * call back.
     */
    private MapsView mapsView;

    /**
     * An integer that represents the active region
     */
    private int activeRegion = EUROPE;

    /**
     * A boolean that indicates if the map was in a moving state
     */
    private boolean navigating = false;

    /**
     * A boolean that indicates if user is writing in a text field
     */
    private boolean focusLocked = false;

    /**
     * Variable telling us if our data are initialized
     * and whether or not this class instance is used by the client or the servlet.
     */
    private boolean isInitialized = false, isClient = false;

    /**
     * The default tile size.
     */
    public static final double TILE_SIZE = 64;

    /**
     * The default tile size for elevated tiles.
     */
    public static final double TILE_ELEV_SIZE = TILE_SIZE * 1.6d;

    /**
     * The default tile size for natural resources.
     */
    public static final double TILE_NATRES_SIZE = TILE_SIZE * .2d;

    /**
     * The default tile size for event.
     */
    public static final double TILE_EVENT_SIZE = TILE_SIZE * .35d;

    /**
     * The default figure posY.
     */
    public static final double FIGURE_HEIGHT = 81;

    /**
     * The offset sectors for border on the horizontal axis.
     */
    public static final double OFFSET_X_SEC = 3;

    /**
     * The offset sectors for border on the vertical axis.
     */
    public static final double OFFSET_Y_SEC = 3;

    /**
     * Map container offset to be able to zoom until 120% without problems with the scroll panel
     */
    public static final double MAP_CONTAINER_OFFSET_X = 600;

    /**
     * Map container offset to be able to zoom until 120% without problems with the scroll panel
     */
    public static final double MAP_CONTAINER_OFFSET_Y = 600;


    /**
     * The zoom level.
     */
    private double zoomLevel = 1d;

    /**
     * Keep track of the suffix of the image file.
     */
    private String resolution;

    /**
     * Keep track of the suffix of the image file.
     */
    private String borders;

    /**
     * Region groups of Vectors
     */
    private final Map<Integer, Group> regionMapImages = new HashMap<Integer, Group>(4);
    private final Map<Integer, Group> regionGeoMapImages = new HashMap<Integer, Group>(4);

    private final Map<Integer, Group> regionJumpOffImages = new HashMap<Integer, Group>(4);
    private final Map<Integer, Group> regionGridLines = new HashMap<Integer, Group>(4);

    private final Map<Integer, Group> regionFigures = new HashMap<Integer, Group>(4);
    private final Map<Integer, Group> regionBattleImages = new HashMap<Integer, Group>();

    private final boolean[] loadedGeoImages = new boolean[REGION_LAST + 1];

    private final int scenarioId;

    private final int[] regionSizeX, regionSizeY;

    private final int[][] dimensions;

    private Map<Integer, Image> regionToBaseMap = new HashMap<Integer, Image>();

    /**
     * Default constructor.
     */
    private MapStore() {
        MovementEventManager.addMovementStopHandler(new MovementStopHandler() {
            public void onMovementStop(final MovementStopEvent event) {
                regionFigures.get(activeRegion).clear();
            }
        });

        scenarioId = GameStore.getInstance().getScenarioId();
        switch (scenarioId) {
            case HibernateUtil.DB_FREE:
                regionSizeX = RegionConstants.REGION_1804_SIZE_X;
                regionSizeY = RegionConstants.REGION_1804_SIZE_Y;
                dimensions = RegionConstants.MAP_1804_DIMENSIONS;
                break;

            case HibernateUtil.DB_S3:
                regionSizeX = RegionConstants.REGION_1808_SIZE_X;
                regionSizeY = RegionConstants.REGION_1808_SIZE_Y;
                dimensions = RegionConstants.MAP_1808_DIMENSIONS;
                break;

            case HibernateUtil.DB_S1:
            case HibernateUtil.DB_S2:
            default:
                regionSizeX = RegionConstants.REGION_1805_SIZE_X;
                regionSizeY = RegionConstants.REGION_1805_SIZE_Y;
                dimensions = RegionConstants.MAP_1805_DIMENSIONS;
                break;
        }
    }

    /**
     * Method returning the map manager
     *
     * @return MapStore
     */
    public static MapStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new MapStore();
        }
        return ourInstance;
    }

    public int[][] getDimensions() {
        return dimensions;
    }

    public double getZoomLevel() {
        return 1.0;
    }

    public double getZoomLevelSettings() {
        return zoomLevel;
    }

    public void setZoomLevel(final double value) {
        zoomLevel = value;
    }

    /**
     * Method that returns a sector based on the click
     * coordinates in the UI
     *
     * @param posX the posX coordinate
     * @param posY the posY coordinate
     * @return the target SectorDTO
     */
    public SectorDTO getTileInfoByPixels(final int posX, final int posY) {
        return regionStore.getRegionSectorsByRegionId(activeRegion)[getPositionX(posX)][getPositionY(posY)];
    }

    /**
     * Sets up images for a region
     *
     * @param regionId the region to setup.
     */
    public void setUpRegion(final int regionId) {
        final SectorDTO[][] regionSectors = regionStore.getRegionSectorsByRegionId(regionId);
        setMapRegionImages(regionSectors, regionId);
        minimapGroups.drawPolitical(regionSectors, regionId);
        minimapGroups.loadMiniMapImages(regionId);
    }

    /**
     * @param sectors  the array with all the sectors.
     * @param regionId the region to setup.
     */
    public void setMapGeoImages(final SectorDTO[][] sectors, final int regionId) {

        final String baseTileURL = "http://static.eaw1805.com/tiles-generated/";
        //baseTileURL = GWT.getHostPageBaseURL() + "/img/tiles-generated/";
        //final String baseTileURL = "http://localhost/tiles-generated/";

        final String primaryTileURL = "http://static.eaw1805.com/tiles/";
        //final String primaryTileURL = "http://localhost/tiles/";

        final double tileSize = getTileSize();
        final Group thisGeoImages = regionGeoMapImages.get(regionId);

        // add sea sectors tiles
        final String oceanUrl = primaryTileURL + "maps/s" + scenarioId + "/sea-" + regionId + resolution;
        final Image geoOceanTile = new Image(getPointX(0), getPointY(0),
                (int) (dimensions[0][regionId]),
                (int) (dimensions[1][regionId]), oceanUrl);
        geoOceanTile.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        thisGeoImages.add(geoOceanTile);

        // add all other tiles
        for (final SectorDTO[] sectorRow : sectors) {
            for (final SectorDTO sector : sectorRow) {
                if (sector == null) {
                    continue;
                }
                final int posX = getPointX(sector.getX());
                final int posY = getPointY(sector.getY());

                if (sector.getTerrainId() != TERRAIN_O
                        && !sector.getImage().contains("coast")) {

                    final String geoTileUrl = baseTileURL + sector.getImageGeo() + ".png";
                    final Image geoTile = new Image(posX, posY, (int) tileSize, (int) tileSize, geoTileUrl);
                    geoTile.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
                    thisGeoImages.add(geoTile);
                }
            }
        }

        switch (regionId) {
            case EUROPE:
                if (HibernateUtil.DB_S3 == GameStore.getInstance().getScenarioId()) {
                    thisGeoImages.add(new Image(getPointX(0), getPointY(0), (int) (4 * tileSize), (int) (4 * tileSize), "http://static.eaw1805.com/images/tiles/compass02.png"));
                } else if (TutorialStore.getInstance().isTutorialMode()) {
                    thisGeoImages.add(new Image(getPointX(12), getPointY(0), (int) (4 * tileSize), (int) (4 * tileSize), "http://static.eaw1805.com/images/tiles/compass02.png"));
                } else {
                    thisGeoImages.add(new Image(getPointX(6), getPointY(4), (int) (4 * tileSize), (int) (4 * tileSize), "http://static.eaw1805.com/images/tiles/compass02.png"));
                }
                break;

            case CARIBBEAN:
                thisGeoImages.add(new Image(getPointX(32), getPointY(4), (int) (4 * tileSize), (int) (4 * tileSize), "http://static.eaw1805.com/images/tiles/compass02.png"));
                break;

            case INDIES:
                thisGeoImages.add(new Image(getPointX(5), getPointY(24), (int) (4 * tileSize), (int) (4 * tileSize), "http://static.eaw1805.com/images/tiles/compass02.png"));
                break;

            case AFRICA:
                thisGeoImages.add(new Image(getPointX(4), getPointY(23), (int) (4 * tileSize), (int) (4 * tileSize), "http://static.eaw1805.com/images/tiles/compass02.png"));
                break;
        }

        loadedGeoImages[regionId] = true;
    }

    /**
     * @param sectors  the array with all the sectors.
     * @param regionId the region to setup.
     */
    public void setMapRegionImages(final SectorDTO[][] sectors, final int regionId) {
        final String baseTileURL = "http://static.eaw1805.com/tiles-generated/";
        //baseTileURL = GWT.getHostPageBaseURL() + "/img/tiles-generated/";
        //final String baseTileURL = "http://localhost/tiles-generated/";

        final String primaryTileURL = "http://static.eaw1805.com/tiles/";
        //final String primaryTileURL = "http://localhost/tiles/";

        final String baseMapURL = "http://static.eaw1805.com/maps/";
//        final String baseMapURL = "http://cyan.cti.gr/eaw1805/images/maps/";
        //final String baseMapURL = "http://localhost/maps/";

        final int gameId = GameStore.getInstance().getGameId();
        final int turnId = GameStore.getInstance().getTurn();
        final int scenarioId = GameStore.getInstance().getScenarioId();

        final double tileSize = getTileSize();
        final int halfTileSize = (int) (0.5 * getTileSize());
        final Group thisMapImages = regionMapImages.get(regionId);
        final Group thisBattleImages = regionBattleImages.get(regionId);

        thisMapImages.clear();
        thisBattleImages.clear();

        // add non-visible + sea tiles
        final String greyUrl = primaryTileURL + "maps/s" + scenarioId + "/region-" + regionId + resolution.replace("png", "jpg");
        final Image greyTile = new Image(getPointX(0), getPointY(0),
                (int) (dimensions[0][regionId]),
                (int) (dimensions[1][regionId]), greyUrl);
        greyTile.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        thisMapImages.add(greyTile);

        final String mapUrl = baseMapURL + "s" + scenarioId + "/" + gameId + "/map-" + gameId + "-" + turnId + "-" + regionId + "-" + GameStore.getInstance().getNationIdForFogOfWar() + borders + resolution;
        final Image mapTile = new Image(getPointX(0), getPointY(0),
                (int) (dimensions[0][regionId]),
                (int) (dimensions[1][regionId]), mapUrl);
//        mapTile.getElement().setAttribute("onload", "alert('image loaded oeo!');");
        mapTile.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        regionToBaseMap.put(regionId, mapTile);
        thisMapImages.add(mapTile);

        // Display all Naval Battles
        final Collection<BattleDTO> lstNavalBattles = RegionStore.getInstance().listNavalBattles();
        for (final BattleDTO navalBattle : lstNavalBattles) {
            if (navalBattle.getRegionId() != regionId) {
                continue;
            }

            final int posX = getPointX(navalBattle.getX());
            final int posY = getPointY(navalBattle.getY());

            final MapImage navalBattleImage = new MapImage(posX + halfTileSize,
                    (int) (posY + getEventTileSize() / 2d),
                    (int) (32), (int) (32),
                    primaryTileURL + "battleNaval.png");
            navalBattleImage.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);

            if (navalBattle.isShow()) {
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(final MouseEvent event) {
                        final BattleFrame frame = new BattleFrame(navalBattle.getBattleId(), false, false, false, navalBattle, false);
                        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(frame);
                        GameStore.getInstance().getLayoutView().positionTocCenter(frame);
                    }
                }).addToElement(navalBattleImage.getElement()).register();
                addHoverImageFunctionality(navalBattleImage);
            }

            thisBattleImages.add(navalBattleImage);
        }

        // Display all Tactical Battles
        final Collection<BattleDTO> lstTacticalBattles = RegionStore.getInstance().listTacticalBattles();
        for (final BattleDTO tacticalBattle : lstTacticalBattles) {
            if (tacticalBattle.getRegionId() != regionId) {
                continue;
            }

            final int posX = getPointX(tacticalBattle.getX());
            final int posY = getPointY(tacticalBattle.getY());

            final MapImage tacticalBattleImage = new MapImage(posX + halfTileSize,
                    (int) (posY + getEventTileSize() / 2d), (int) (32), (int) (32),
                    primaryTileURL + "battleLand.png");
            tacticalBattleImage.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);

            if (tacticalBattle.isShow()) {
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(final MouseEvent event) {
                        final BattleFrame frame = new BattleFrame(tacticalBattle.getBattleId(), true, false, false, tacticalBattle, false);
                        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(frame);
                        GameStore.getInstance().getLayoutView().positionTocCenter(frame);
                    }
                }).addToElement(tacticalBattleImage.getElement()).register();
                addHoverImageFunctionality(tacticalBattleImage);
            }

            thisBattleImages.add(tacticalBattleImage);
        }

        // Display jump-off points
        final Group thisJumpOffImages = regionJumpOffImages.get(regionId);
        thisJumpOffImages.clear();

        final List<SectorDTO> jumpOffSectors;
        switch (scenarioId) {
            case HibernateUtil.DB_FREE:
                jumpOffSectors = JumpOffCoordinates1804.jumpOffSectors.get(sectors[0][0].getRegionId());
                break;

            case HibernateUtil.DB_S3:
                jumpOffSectors = new ArrayList<SectorDTO>();
                break;

            case HibernateUtil.DB_S1:
            case HibernateUtil.DB_S2:
            default:
                jumpOffSectors = JumpOffCoordinates1805.jumpOffSectors.get(sectors[0][0].getRegionId());
                break;
        }

        for (final SectorDTO sector : jumpOffSectors) {
            final Rectangle rect = new Rectangle(getPointX(sector.getX()),
                    getPointY(sector.getY()), (int) getTileSize(), (int) getTileSize());
            rect.setFillOpacity(0.8d);
            int size = 12;
            final String label;
            switch (sector.getTerrainId()) {
                case EUROPE:
                    label = "Europe";
                    rect.setFillColor("green");
                    break;

                case CARIBBEAN:
                    label = "Caribbean";
                    rect.setFillColor("yellow");
                    size = 11;
                    break;

                case INDIES:
                    label = "Indies";
                    rect.setFillColor("purple");
                    break;

                case AFRICA:
                    label = "Africa";
                    rect.setFillColor("brown");
                    break;

                default:
                    label = "";
                    break;
            }

            final Text rectText = new Text(getPointX(sector.getX()) + 2,
                    getPointY(sector.getY()) + halfTileSize, label);
            rectText.setFontSize(size);
            thisJumpOffImages.add(rect);
            thisJumpOffImages.add(rectText);
        }

        switch (regionId) {
            case EUROPE:
                if (HibernateUtil.DB_S3 == GameStore.getInstance().getScenarioId()) {
                    thisMapImages.add(new Image(getPointX(0), getPointY(0), (int) (4 * tileSize), (int) (4 * tileSize), "http://static.eaw1805.com/images/tiles/compass02.png"));
                } else if (TutorialStore.getInstance().isTutorialMode()) {
                    thisMapImages.add(new Image(getPointX(12), getPointY(0), (int) (4 * tileSize), (int) (4 * tileSize), "http://static.eaw1805.com/images/tiles/compass02.png"));
                } else {
                    thisMapImages.add(new Image(getPointX(6), getPointY(4), (int) (4 * tileSize), (int) (4 * tileSize), "http://static.eaw1805.com/images/tiles/compass02.png"));
                }
                break;

            case CARIBBEAN:
                thisMapImages.add(new Image(getPointX(32), getPointY(4), (int) (4 * tileSize), (int) (4 * tileSize), "http://static.eaw1805.com/images/tiles/compass02.png"));
                break;

            case INDIES:
                thisMapImages.add(new Image(getPointX(5), getPointY(24), (int) (4 * tileSize), (int) (4 * tileSize), "http://static.eaw1805.com/images/tiles/compass02.png"));
                break;

            case AFRICA:
                thisMapImages.add(new Image(getPointX(4), getPointY(23), (int) (4 * tileSize), (int) (4 * tileSize), "http://static.eaw1805.com/images/tiles/compass02.png"));
                break;
        }

        thisMapImages.add(thisJumpOffImages);
    }

    public void changeMapHref() {
        final String baseMapURL = "http://static.eaw1805.com/maps/";
//        final String baseMapURL = "http://cyan.cti.gr/eaw1805/images/maps/";
        //final String baseMapURL = "http://localhost/maps/";

        final int gameId = GameStore.getInstance().getGameId();
        final int regionId = getActiveRegion();
        final int turnId = GameStore.getInstance().getTurn();
        final int scenarioId = GameStore.getInstance().getScenarioId();

        final String mapUrl = baseMapURL + "s" + scenarioId + "/" + gameId + "/map-" + gameId + "-" + turnId + "-" + regionId + "-" + GameStore.getInstance().getNationIdForFogOfWar() + borders + resolution;
        getBaseMapImageByRegion(regionId).setHref(mapUrl);
        GameStore.getInstance().getLayoutView().getLoadingGraphicsPanel().update();
    }

    public void initVectorHashMaps() {
        for (int regionId = REGION_FIRST; regionId <= REGION_LAST; regionId++) {
            regionMapImages.put(regionId, new Group());
            regionGeoMapImages.put(regionId, new Group());
            regionJumpOffImages.put(regionId, new Group());
            regionGridLines.put(regionId, new MapGridGroup(regionId));

            regionFigures.put(regionId, new Group());
            minimapGroups.initVectorHasMaps(regionId);
            regionBattleImages.put(regionId, new Group());
        }
    }

    public void initSpeedUpGroups(final Map<Integer, SectorDTO[][]> sectors) {
        for (int regionId = REGION_FIRST; regionId <= REGION_LAST; regionId++) {
            final SpeedUpGroup speedUpGroup = new SpeedUpGroup();
            speedUpGroup.initSpeedUpGroup(sectors.get(regionId));
            speedUpGroups.put(regionId, speedUpGroup);
        }
    }

    /**
     * Method that removes the old region from the map
     * and inserts the new one
     *
     * @param regionId the region's id that we will put on the map view.
     */
    public void selectRegion(final int regionId) {
        setActiveRegion(regionId);

        // Check if the region has been set up
        if (minimapGroups.getMinimapImageByRegionId(regionId).getVectorObjectCount() == 0) {
            setUpRegion(regionId);
        }

        WarehouseStore.getInstance().getEconomyWidget().populateGoodsLabels(WarehouseStore.getInstance().getWareHouseByRegion(regionId), false);
        getMapsView().constructMap(regionId, true);

        final Group minimap = minimapGroups.getMinimapImageByRegionId(regionId);
        getMapsView().initOnAddToMapPanel(minimap);

        MapEventManager.fireRegionChange(regionId);
    }

    /**
     * Method that gets the quarter clicked on the map
     *
     * @param xCoord the posX coordinate clicked
     * @param yCoord the posY coordinate clicked
     * @return the quarter or 0 if it was the tile
     */
    public int getQuarter(final int xCoord, final int yCoord) {
        final double tileSize = getTileSize();
        final int xTileNo = (int) (xCoord / tileSize), yTileNo = (int) (yCoord / tileSize);
        final int xLocal = xCoord - (int) (xTileNo * tileSize);
        final int yLocal = yCoord - (int) (yTileNo * tileSize);
        final double third = tileSize / 3d;
        if (xLocal < third && yLocal < third) {
            return 1;

        } else if (xLocal > (tileSize - third) && yLocal < third) {
            return 2;

        } else if (xLocal > (tileSize - third) && yLocal > (tileSize - third)) {
            return 3;

        } else if (xLocal < third && yLocal > (tileSize - third)) {
            return 4;

        } else {
            return 0;
        }
    }

    /**
     * Method that returns the position of the map scroll panel
     *
     * @return the Position where the panel is centered.
     */
    public PositionDTO getMapScrollPanelPosition() {
        final PositionDTO position = new PositionDTO();
        position.setX(mapsView.getMapDrawArea().getMapScrollPanel().getHorizontalScrollPosition());
        position.setY(mapsView.getMapDrawArea().getMapScrollPanel().getVerticalScrollPosition());
        return position;
    }

    public void displayAllMovements(final int regionId) {
        //clean previous movements first
        removeAllMovements();
        for (Group mvGroup : MovementStore.getInstance().getMovementLinesGroupsByRegion(regionId)) {
            try {
                mapsView.getAllMovements().add(mvGroup);
            } catch (Exception e) {
//                Window.alert("failed to add movement line " + e.toString());
            }
        }
        for (Group mvAlliedGroup : getAlliedUnitGroups().getMovementLinesByRegion(regionId)) {
            try {
                mapsView.getAllMovements().add(mvAlliedGroup);
            } catch (Exception e) {
//                Window.alert("failed to add allied movement line " + e.toString());
            }
        }

    }

    public void removeAllMovements() {
        mapsView.getAllMovements().clear();
    }

    /**
     * @param navigating the navigating to set
     */
    public void setNavigating(final boolean navigating) {
        this.navigating = navigating;
    }

    /**
     * @return the navigating
     */
    public boolean isNavigating() {
        return navigating;
    }

    /**
     * Get if user is writing in a text field.
     *
     * @return True if user is writing in a text field
     */
    public boolean isFocusLocked() {
        return focusLocked;
    }

    /**
     * Set true if user is writing in a text field.
     *
     * @param focusLocked The value to set.
     */
    public void setFocusLocked(final boolean focusLocked) {
        this.focusLocked = focusLocked;
    }

    public Group getRegionFiguresById(final int regionId) {
        return regionFigures.get(regionId);
    }

    public SectorDTO[][] getRegionSectorsByRegionId(final int regionId) {
        return RegionStore.getInstance().getRegionSectorsByRegionId(regionId);
    }

    public SectorDTO getRegionSectorByRegionIdXY(final int regionId, final int posX, final int posY) {
        return RegionStore.getInstance().getRegionSectorsByRegionId(regionId)[posX][posY];
    }

    public Group getRegionMapImagesByRegionId(final int regionId) {
        return regionMapImages.get(regionId);
    }

    public Group getRegionGeoMapImagesByRegionId(final int regionId) {
        if (!loadedGeoImages[regionId]) {
            final SectorDTO[][] regionSectors = regionStore.getRegionSectorsByRegionId(regionId);
            setMapGeoImages(regionSectors, regionId);
        }

        return regionGeoMapImages.get(regionId);
    }

    public Group getRegionBorderLinesByRegionId(final int regionId) {
        return new Group();
    }

    public Group getRegionGridLinesByRegionId(final int regionId) {
        return regionGridLines.get(regionId);
    }

    public Group getRegionBattleImagesByRegionId(final int regionId) {
        return regionBattleImages.get(regionId);
    }

    /**
     * @param activeRegion the activeRegion to set
     */
    public void setActiveRegion(final int activeRegion) {
        this.activeRegion = activeRegion;
    }

    /**
     * @return the activeRegion
     */
    public int getActiveRegion() {
        return activeRegion;
    }

    /**
     * @param isInitialized the isInitialized to set
     */
    public void setInitialized(final boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    /**
     * @return the isInitialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * @param isClient the isClient to set
     */
    public void setClient(final boolean isClient) {
        this.isClient = isClient;
    }

    /**
     * @return the isClient
     */
    public boolean isClient() {
        return isClient;
    }

    /**
     * @return the mapsView
     */
    public MapsView getMapsView() {
        return mapsView;
    }

    /**
     * @param mapsView the mapsView to set
     */
    public void setMapsView(final MapsView mapsView) {
        this.mapsView = mapsView;
    }

    /**
     * @return the minimapGroups
     */
    public MinimapGroups getMinimapGroups() {
        return minimapGroups;
    }

    /**
     * @return the unitGroups
     */
    public UnitGroups getUnitGroups() {
        return unitGroups;
    }

    /**
     * @return the alliedUnitGroups
     */
    public AlliedUnitsGroup getAlliedUnitGroups() {
        return alliedUnitGroups;
    }

    public ForeignUnitsGroup getForeignUnitsGroup() {
        return foreignUnitsGroup;
    }

    public void zoom(final boolean in) {
        double initLevel = zoomLevel;
        if (in) {
            zoomLevel += 0.1d;
        } else {
            zoomLevel -= 0.1d;
        }

        if (zoomLevel > 1.2d) {
            zoomLevel = 1.2d;
        }

        if (zoomLevel < .3d) {
            zoomLevel = .3d;
        }

        if (zoomLevel != initLevel) {
            doZoom(zoomLevel);
            mapsView.initOnAddToMapPanel(minimapGroups.getMinimapImageByRegionId(activeRegion));
        }
    }

    public native void doZoom(double zoom) /*-{
        var svgEl = $wnd.$(".noScrollBars-svg");
        if (zoom == 1) {
            svgEl.css({transform:""});
        } else {
            svgEl.css({transform:"scale(" + zoom + "," + zoom + ")" });
        }
    }-*/;

    public native void toggleFullScreen(final boolean fullscreen) /*-{
        if (fullscreen) {  // current working methods
            if ($doc.documentElement.requestFullscreen) {
                $doc.documentElement.requestFullscreen();
            } else if ($doc.documentElement.msRequestFullscreen) {
                $doc.documentElement.msRequestFullscreen();
            } else if ($doc.documentElement.mozRequestFullScreen) {
                $doc.documentElement.mozRequestFullScreen();
            } else if ($doc.documentElement.webkitRequestFullscreen) {
                $doc.documentElement.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
            }
        } else {
            if ($doc.exitFullscreen) {
                $doc.exitFullscreen();
            } else if ($doc.msExitFullscreen) {
                $doc.msExitFullscreen();
            } else if ($doc.mozCancelFullScreen) {
                $doc.mozCancelFullScreen();
            } else if ($doc.webkitExitFullscreen) {
                $doc.webkitExitFullscreen();
            }
        }

    }-*/;

    public native boolean isFullScreen() /*-{
        return document.fullscreenElement ||    // alternative standard method
            document.mozFullScreenElement || document.webkitFullscreenElement || document.msFullscreenElement
    }-*/;


    public void setResolution(final boolean low) {
        if (low) {
            if (GameStore.getInstance().isMobileDevice()) {
                resolution = "-vvlowres.png";

            } else {
                resolution = "-lowres.png";
            }

        } else {
            if (GameStore.getInstance().isMobileDevice()) {
                resolution = "-vlowres.png";

            } else {
                resolution = ".png";
            }
        }
    }

    public void setBorders(final boolean show) {
        if (show) {
            borders = "-border";

        } else {
            borders = "";
        }
    }

    /**
     * @return the economicGroups
     */
    public EconomicGroups getEconomicGroups() {
        return economicGroups;
    }

    public double getTileSize() {
        return TILE_SIZE;
    }

    public double getZoomedTileSize() {
        return zoomLevel * TILE_SIZE;
    }

    public double getElevTileSize() {
        return TILE_ELEV_SIZE;
    }

    public double getNatResTileSize() {
        return TILE_NATRES_SIZE;
    }

    public double getEventTileSize() {
        return TILE_EVENT_SIZE;
    }

    public int getPointX(final int posX) {
        return (int) ((OFFSET_X_SEC + posX) * TILE_SIZE);
    }

    public int getZoomedPointX(final int posX) {
        return (int) ((OFFSET_X_SEC + posX) * zoomLevel * TILE_SIZE);
    }

    public int getPointY(final int posY) {
        return (int) ((OFFSET_Y_SEC + posY) * TILE_SIZE);
    }

    public int getZoomedPointY(final int posY) {
        return (int) ((OFFSET_Y_SEC + posY) * zoomLevel * TILE_SIZE);
    }

    public int getPositionX(final int pointX) {
        return (int) ((pointX / getZoomedTileSize()) - OFFSET_X_SEC);
    }

    public int getPositionY(final int pointY) {
        return (int) ((pointY / getZoomedTileSize()) - OFFSET_Y_SEC);
    }

    public double getZoomOffsetX() {
        return (1 - zoomLevel) * mapsView.getMapDrawArea().getWidth() / 2 + MAP_CONTAINER_OFFSET_X;//5632
    }

    public double getZoomOffsetY() {
        return (1 - zoomLevel) * mapsView.getMapDrawArea().getHeight() / 2 + MAP_CONTAINER_OFFSET_Y;//5312
    }

    public int translateX(int x) {
        return (int) (x + ((1 - zoomLevel) * 5632 / 2));
    }

    public int translateY(int y) {
        return (int) (y + ((1 - zoomLevel) * 5312 / 2));
    }

    public void addHoverImageFunctionality(final Image img) {
        img.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(final MouseOverEvent event) {
                int xPos, yPos;
                xPos = (int) (img.getX() - img.getWidth() * 0.5);
                yPos = img.getY();
                img.setHeight((int) (img.getHeight() * 1.5));
                img.setWidth((int) (img.getWidth() * 1.5));
                img.setX(xPos);
                img.setY(yPos);
            }
        });

        img.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                int xPos, yPos;
                xPos = (int) (img.getX() + img.getWidth() * 0.333334);
                yPos = img.getY();
                img.setHeight(2 * img.getHeight() / 3);
                img.setWidth(2 * img.getWidth() / 3);
                img.setX(xPos);
                img.setY(yPos);
            }
        });
    }

    public SpeedUpGroup getSpeedUpGroupByRegion(final int regionId) {
        return speedUpGroups.get(regionId);
    }

    public Image getBaseMapImageByRegion(final int regionId) {
        return regionToBaseMap.get(regionId);
    }

}
