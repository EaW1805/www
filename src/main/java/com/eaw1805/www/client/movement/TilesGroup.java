package com.eaw1805.www.client.movement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.movement.PathDTO;
import com.eaw1805.data.dto.web.movement.PathSectorDTO;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.SoundStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

public class TilesGroup
        extends Group
        implements ArmyConstants {

    /**
     * 3rd level hierarchy vectors of movement.
     */
    private Rectangle targetTileRect;

    /**
     * Represents the available target tile choices and the path-lines to the selected target.
     */
    private final Group pathTiles = new Group();

    /**
     * The sectors visited by the movement path.
     */
    private PathDTO path;

    /**
     * Object that provides access to the parent.
     */
    private FiguresGroup fgGroup;

    /**
     * The manager that adds and removes movements.
     */
    private final MovementStore mvStore = MovementStore.getInstance();

    /**
     * Ending tile
     */
    private PathSectorDTO finishTile;

    private String pathUrl;

    private static final String DEBUG_URL = "http://127.0.0.1:8888/images";

    private static final String SERVER_URL = "http://static.eaw1805.com";

    private final MapStore mapStore = MapStore.getInstance();

    private static final String CORNER_TOP_LEFT = "SoldiersMovePath30.png";

    private static final String CORNER_TOP_RIGHT = "SoldiersMovePath31.png";

    private static final String CORNER_BOTTOM_LEFT = "SoldiersMovePath32.png";

    private static final String CORNER_BOTTOM_RIGHT = "SoldiersMovePath29.png";

    private String lastDirection = "";


    /**
     * Default constructor.
     *
     * @param figuresGroup the subject of the movement order.
     * @param thisPath     the movement path.
     */
    public TilesGroup(final FiguresGroup figuresGroup, final PathDTO thisPath) {
        super();
        initTilesGroup(figuresGroup, thisPath);
    }

    public void initTilesGroup(final FiguresGroup figuresGroup, final PathDTO thisPath) {
        if (SERVER_URL.equals("http://127.0.0.1:8888/")) {
            pathUrl = DEBUG_URL;

        } else {
            pathUrl = SERVER_URL;
        }

        // Initializing children
        fgGroup = figuresGroup;
        path = thisPath;

        // Setup rectangle
        finishTile = path.getPathSectors().get(path.getTotLength() - 1);

        int totalMps = figuresGroup.getMovementGroup().getUsedMPs() + path.getTotalCost();
        initRect(finishTile, totalMps, figuresGroup.getMovementGroup().getCurrentMp(), figuresGroup.getMovementGroup().getMPsTip());
        add(targetTileRect);

        //get image colors
        findPathColor(figuresGroup.getUnitType(), fgGroup.getMovementGroup().isForcedMarch(), fgGroup.getMovementGroup().isPatrolMove());
        // add path
//        addPath();
    }

    public TilesGroup(final int unitType, final PathDTO thisPath, final Boolean forcedMarch, final boolean patrol) {
        super();
        if (SERVER_URL.equals("http://127.0.0.1:8888/")) {
            pathUrl = DEBUG_URL;

        } else {
            pathUrl = SERVER_URL;
        }

        path = thisPath;
        findPathColor(unitType, forcedMarch, patrol);
        addPathSimple(patrol, unitType);
    }


    public void findPathColor(final int unitType, final boolean isForcedMarch, final boolean isPatrolMove) {
        switch (unitType) {
            case ARMY:
            case CORPS:
            case BRIGADE:
                if (isForcedMarch) {
                    pathUrl += "/tiles/move/red/";

                } else {
                    pathUrl += "/tiles/move/yellow/";

                }
                break;

            case SHIP:
            case FLEET:
                if (isPatrolMove) {
                    pathUrl += "/tiles/move/red/";

                } else {
                    pathUrl += "/tiles/move/yellow/";
                }
                break;

            case BAGGAGETRAIN:
            case SPY:
            case COMMANDER:
                pathUrl += "/tiles/move/white/";
                break;

            default:
                pathUrl += "/tiles/move/blue/";
                break;
        }

    }

    public Group getPathTiles() {
        return pathTiles;
    }

    public void addPathSimple(final boolean isPatrol, final int unitType) {

        final double tileSize = mapStore.getTileSize();
        Image thisTile;

        final StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(pathUrl);

        for (final PathSectorDTO thisSector : path.getPathSectors()) {
            String pathStr;
            if (isPatrol
                    && (unitType == BRIGADE || unitType == CORPS || unitType == ARMY)
                    && thisSector.getPath().contains("start")) {
                pathStr = thisSector.getPath().replace("start", "patrol");
            } else {
                pathStr = thisSector.getPath();
            }

            strBuilder.delete(0, strBuilder.length());
            strBuilder.append(pathUrl);
            strBuilder.append(pathStr);
            strBuilder.append(".png");
            addAdditionImages(pathStr, thisSector);

            thisTile = new Image(mapStore.getPointX(thisSector.getX()),
                    mapStore.getPointY(thisSector.getY()),
                    (int) tileSize, (int) tileSize, strBuilder.toString());
            thisTile.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
            pathTiles.add(thisTile);
        }
    }


    /**
     * Adds the images needed when path crosses corners of game squares.
     *
     * @param pathStr    The path of the original image.
     * @param thisSector The sector.
     */
    public void addAdditionImages(final String pathStr, final PathSectorDTO thisSector) {
        if (pathStr.contains("end")) {
            return;
        }
        if (pathStr.contains("-RU")
                && !"-LD".equals(lastDirection)) {
            lastDirection = "-RU";
            addCornerImage(thisSector, CORNER_TOP_LEFT, 1, 0);
            addCornerImage(thisSector, CORNER_BOTTOM_RIGHT, 0, -1);
        } else if (pathStr.contains("-RD")
                && !"-LU".equals(lastDirection)) {
            lastDirection = "-RD";
            addCornerImage(thisSector, CORNER_BOTTOM_LEFT, 1, 0);
            addCornerImage(thisSector, CORNER_TOP_RIGHT, 0, 1);
        } else if (pathStr.contains("-LD")
                && !"-RU".equals(lastDirection)) {
            lastDirection = "-LD";
            addCornerImage(thisSector, CORNER_BOTTOM_RIGHT, -1, 0);
            addCornerImage(thisSector, CORNER_TOP_LEFT, 0, 1);
        } else if (pathStr.contains("-LU")
                && !"-RD".equals(lastDirection)) {
            lastDirection = "-LU";
            addCornerImage(thisSector, CORNER_BOTTOM_LEFT, 0, -1);
            addCornerImage(thisSector, CORNER_TOP_RIGHT, -1, 0);
        } else {
            lastDirection = "";
        }

    }

    private void addCornerImage(final PathSectorDTO thisSector, final String imageUrl, final int xStep, final int yStep) {
        final double tileSize = mapStore.getTileSize();

        final Image tl = new Image(mapStore.getPointX(thisSector.getX() + xStep),
                mapStore.getPointY(thisSector.getY() + yStep),
                (int) tileSize, (int) tileSize, pathUrl + imageUrl);
        tl.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        pathTiles.add(tl);
    }


    /**
     * Function that takes the path and provides the lines that make the road.
     */
    private void addPath() {
        final MapStore mapStore = MapStore.getInstance();
        final double tileSize = mapStore.getTileSize();
        Image thisTile = null;
        int posX;
        int posY;
        for (final PathSectorDTO thisSector : path.getPathSectors()) {
            posX = thisSector.getX();
            posY = thisSector.getY();
            String path;

            if (fgGroup.getMovementGroup().isPatrolMove()
                    && (fgGroup.getUnitType() == BRIGADE || fgGroup.getUnitType() == CORPS || fgGroup.getUnitType() == ARMY)
                    && thisSector.getPath().contains("start")) {
                path = thisSector.getPath().replace("start", "patrol");
            } else {
                path = thisSector.getPath();
            }
            addAdditionImages(path, thisSector);
            thisTile = new Image(mapStore.getPointX(posX), mapStore.getPointY(posY),
                    (int) tileSize, (int) tileSize, pathUrl + path + ".png");
            thisTile.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);

            thisTile.addMouseOutHandler(new MouseOutHandler() {

                public void onMouseOut(final MouseOutEvent event) {
                    hidePath();
                }
            });

            pathTiles.add(thisTile);
        }

        if (thisTile != null) {
            thisTile.addClickHandler(new ClickHandler() {

                @SuppressWarnings("restriction")
                public void onClick(final ClickEvent event) {
                    if (!MapStore.getInstance().isNavigating()) {
//                        fgGroup.getMovementGroup().getTooltip().hideFancy();
                        SoundStore.getInstance().playMoveSound(fgGroup.getMovementGroup().getUnitType(), fgGroup.getMovementGroup().getUnitId());
                        hidePath();
                        final MovementGroup moveGroup = fgGroup.getMovementGroup();
                        final int normalMps = moveGroup.getBaseMP();
                        finishTile.setXStart(finishTile.getX());
                        finishTile.setYStart(finishTile.getY());
                        final int totalMps = moveGroup.addNewFigureAndPath(finishTile, path, pathTiles);

                        boolean forcedMarch = false;
                        if (normalMps < totalMps) {
                            forcedMarch = true;
                        }

                        boolean patrol = moveGroup.isPatrolMove();

                        mvStore.addNewMovementOrder(fgGroup.getMovementGroup().getUnitType(),
                                fgGroup.getMovementGroup().getUnitId(),
                                path,
                                forcedMarch, patrol);

                        fgGroup.hideTargetTiles();

                    } else {
                        mapStore.setNavigating(false);
                    }
                }
            });
        }
    }

    /**
     * Function that initializes the Rectangle target, and provides basic functionality to it.
     *
     * @param targetSector the sector dto.
     */
    private void initRect(final PathSectorDTO targetSector, final int usedMps, final int baseMps, final Text tooltip) {
        final MapStore mapStore = MapStore.getInstance();
        final double tileSize = mapStore.getTileSize();
        targetTileRect = new Rectangle(mapStore.getPointX(targetSector.getX()),
                mapStore.getPointY(targetSector.getY()),
                (int) tileSize, (int) tileSize);
        if (targetSector.getNeedsConquer()) {
            targetTileRect.setFillColor("red");
        } else {
            targetTileRect.setFillColor("green");
        }
        targetTileRect.setFillOpacity(0.4);

        targetTileRect.addMouseOverHandler(new MouseOverHandler() {

            public void onMouseOver(final MouseOverEvent event) {
                tooltip.setX(mapStore.getPointX(targetSector.getX()));
                tooltip.setY(mapStore.getPointY(targetSector.getY()) + 12);
                tooltip.setText(usedMps + "/" + baseMps + " MP");
                //targetTileRect.setFillColor("green");
                fgGroup.hidePossiblePaths();
                showPath();
            }
        });
    }

    public void showPath() {

        try {
            if (pathTiles.getVectorObjectCount() == 0) {
                addPath();
            }
            add(pathTiles);
        } catch (Exception ex) {
            GWT.log(ex.getMessage());
        }
    }

    public void hidePath() {
        try {
            remove(pathTiles);
        } catch (Exception ex) {
            GWT.log(ex.getMessage());
        }
    }

}
