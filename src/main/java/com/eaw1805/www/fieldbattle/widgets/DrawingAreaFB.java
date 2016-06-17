package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.field.FieldBattleOrderDTO;
import com.eaw1805.data.dto.web.field.FieldBattleSectorDTO;
import com.eaw1805.www.fieldbattle.EmpireFieldBattleClient;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.calculators.MapStore;
import com.eaw1805.www.fieldbattle.stores.utils.AnimationUtils;
import com.eaw1805.www.fieldbattle.stores.utils.BasicHandler;
import com.eaw1805.www.fieldbattle.stores.utils.MapConstants;
import com.eaw1805.www.fieldbattle.stores.utils.MapUtils;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import java.util.List;


public class DrawingAreaFB extends DrawingArea implements MapConstants {
    ScrollPanel scroller;

    enum Action {
        NORMAL,
        SCROLLING,
        PREPARE_SELECTING_POSITION,
        SELECTING_POSITION_DOWN,
        SELECTING_POSITION,
        C_DOWN//this applies for rectangular multiselect

    }


    Action action = Action.NORMAL;
    private int lastX = 0;
    private int lastY = 0;
    double zoomLevel = 1;
    double zoomToRemember = 1;

    boolean[] DIRECTIONS = new boolean[4];
    private Rectangle selectionRectangle;
    BrigadeDTO brigToSave;
    boolean brigSaveStrategicPoint = false;
    boolean isBasicOrder = true;

    Image movementLine;

    enum KeyAction {
        NONE,
        PRESSED
    }

    KeyAction cAction = KeyAction.NONE;

    int[] selectionStart = new int[2];
    Rectangle selectionRect = new Rectangle(0, 0, 0, 0);
    MultiSelectGroup selectedGroup = new MultiSelectGroup();

    public DrawingAreaFB(int width, int height, final MainPanel parent) {
        super(width, height);
        selectionRect.setFillColor("red");
        selectionRect.setFillOpacity(0.3);
        selectionRect.setStrokeColor("black");
        selectionRect.setStrokeOpacity(0.5);
        setStyleName("mapstyle");
        this.addDomHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(final MouseDownEvent event) {
                event.stopPropagation();
                event.preventDefault();
                MainPanel.getInstance().getMapUtils().hideLastBrigadeMenu();

                if (cAction == KeyAction.PRESSED) {
                    action = Action.C_DOWN;
                    if (!selectionRect.isAttached()) {
                        add(selectionRect);
                    }
                    int x = event.getClientX();
                    int y = event.getClientY();
                    x = (MainPanel.getInstance().getMapUtils().translateToMapX(x) + SIDE_OFFSET) * TILE_WIDTH;
                    y = (MainPanel.getInstance().getMapUtils().translateToMapY(y) + SIDE_OFFSET) * TILE_HEIGHT;

                    selectionStart[0] = x;
                    selectionStart[1] = y;

                } else {


                    if (action != Action.PREPARE_SELECTING_POSITION && action != Action.SELECTING_POSITION) {
                        selectedGroup.hidePanel();
                        action = Action.SCROLLING;
                    } else {
                        action = Action.SELECTING_POSITION_DOWN;
                    }
                }
                lastX = event.getClientX();
                lastY = event.getClientY();

                DrawingAreaFB.this.setStyleName("noScrollBarsMove", true);
                DrawingAreaFB.this.removeStyleName("noScrollBars");
//                setStyleName("pointer");
            }
        }, MouseDownEvent.getType());

        addDomHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent mouseUpEvent) {
                if (action == Action.C_DOWN && cAction == KeyAction.PRESSED) {

                    int posXStart = (selectionRect.getX() / TILE_WIDTH) - SIDE_OFFSET;
                    int posYStart = (selectionRect.getY() / TILE_HEIGHT) - SIDE_OFFSET;
                    if (posXStart < 0) {
                        posXStart = 0;
                    }
                    if (posYStart < 0) {
                        posYStart = 0;
                    }
                    int posXEnd = ((selectionRect.getX() + selectionRect.getWidth()) / TILE_WIDTH) - SIDE_OFFSET - 1;
                    int posYEnd = ((selectionRect.getY() + selectionRect.getHeight()) / TILE_HEIGHT) - SIDE_OFFSET - 1;

                    if (posXEnd >= MainPanel.getInstance().getMapUtils().getNumTilesX()) {
                        posXEnd = MainPanel.getInstance().getMapUtils().getNumTilesX() - 1;
                    }
                    if (posYEnd >= MainPanel.getInstance().getMapUtils().getNumTilesY()) {
                        posYEnd = MainPanel.getInstance().getMapUtils().getNumTilesY() - 1;
                    }

                    selectedGroup.selectBrigadesFromSectors(posXStart, posXEnd, posYStart, posYEnd);
                    selectedGroup.showPanel();
                    selectionRect.setWidth(0);
                    selectionRect.setHeight(0);
                    remove(selectionRect);
                } else if (action == Action.SELECTING_POSITION || action == Action.SELECTING_POSITION_DOWN) {

                    int x = mouseUpEvent.getClientX();
                    int y = mouseUpEvent.getClientY();
                    x = MainPanel.getInstance().getMapUtils().translateToMapX(x);
                    y = MainPanel.getInstance().getMapUtils().translateToMapY(y);

                    if (x >= 0 && x < MainPanel.getInstance().getMapUtils().getNumTilesX()
                            && y >= 0 && y < MainPanel.getInstance().getMapUtils().getNumTilesY()) {
                        final FieldBattleOrderDTO order;
                        if (isBasicOrder) {
                            order = brigToSave.getBasicOrder();
                        } else {
                            order = brigToSave.getAdditionalOrder();
                        }
                        if (brigSaveStrategicPoint) {
                            if (!order.hasStrategicPoint1()) {
                                order.getStrategicPoint1().setX(x);
                                order.getStrategicPoint1().setY(y);
                            } else if (!order.hasStrategicPoint2()) {
                                order.getStrategicPoint2().setX(x);
                                order.getStrategicPoint2().setY(y);
                            } else if (!order.hasStrategicPoint3()) {
                                order.getStrategicPoint3().setX(x);
                                order.getStrategicPoint3().setY(y);
                            }
                        } else {
                            if (!order.hasCheckPoint1()) {
                                order.getCheckPoint1().setX(x);
                                order.getCheckPoint1().setY(y);
                            } else if (!order.hasCheckPoint2()) {
                                order.getCheckPoint2().setX(x);
                                order.getCheckPoint2().setY(y);
                            } else if (!order.hasCheckPoint3()) {
                                order.getCheckPoint3().setX(x);
                                order.getCheckPoint3().setY(y);
                            }
                        }
                        if (selectedGroup.isAttached()) {
                            selectedGroup.setBrigadeMovement(x, y, isBasicOrder, brigSaveStrategicPoint);
                        }
                        if (BaseStore.getInstance().isStartRound()) {
                            MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigToSave, 0);
                        } else {
                            MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigToSave, MainPanel.getInstance().getPlayback().getRound()-1);
                        }

                        if (brigToSave.isPlacedOnFieldMap()) {
                            if (BaseStore.getInstance().isStartRound()) {
                                MainPanel.getInstance().getMapUtils().addArmyImage(brigToSave, true, false, 0, false);
                            } else {
                                MainPanel.getInstance().getMapUtils().addArmyImage(brigToSave, true, false, MainPanel.getInstance().getPlayback().getRound() - 1, false);
                            }
//                            MainPanel.getInstance().getMiniMap().getMap().moveMap(MainPanel.getInstance().getMiniMapUtils().getPointX(brigToSave.getFieldBattleX()), MainPanel.getInstance().getMiniMapUtils().getPointX(brigToSave.getFieldBattleY()));
                            MainPanel.getInstance().getMapUtils().openBrigadeMenu(brigToSave, false, MainPanel.getInstance().getPlayback().getRound() - 1);
                        }
//                        MainPanel.getInstance().getOrdersPanel().saveBrigadeOrder(brigToSave);
                    } else {
                        Window.alert("Not a valid coordinate, select a coordinate in the size of map");
                    }
                    MainPanel.getInstance().getMapUtils().hideCoordinatesGroup();
                }

                if (action == Action.PREPARE_SELECTING_POSITION) {
                    action = Action.SELECTING_POSITION;
                } else {
                    action = Action.NORMAL;
                }
                selectionRectangle.setFillColor("red");
                DrawingAreaFB.this.setStyleName("noScrollBars", true);
                DrawingAreaFB.this.removeStyleName("noScrollBarsMove");
//                removeStyleName("pointer");
            }
        }, MouseUpEvent.getType());

        addDomHandler(new MouseMoveHandler() {
            @Override
            public void onMouseMove(final MouseMoveEvent event) {
                event.preventDefault();
                int x = event.getClientX();
                int y = event.getClientY();

                if (action == Action.C_DOWN && cAction == KeyAction.PRESSED) {
                    int x1 = (MainPanel.getInstance().getMapUtils().translateToMapX(x) + MapUtils.SIDE_OFFSET) * MapUtils.TILE_WIDTH;
                    int y1 = (MainPanel.getInstance().getMapUtils().translateToMapY(y) + MapUtils.SIDE_OFFSET) * MapUtils.TILE_HEIGHT;

                    int x2 = selectionStart[0];
                    int y2 = selectionStart[1];



                    if (x1 < x2) {
                        x2 = selectionRect.getX() + selectionRect.getWidth();
                    }
                    if (y1 < y2) {
                        y2 = selectionRect.getY() + selectionRect.getHeight();
                    }

                    selectionRect.setX(Math.min(x1, x2));
                    selectionRect.setY(Math.min(y1, y2));
                    selectionRect.setWidth(Math.abs(x1 - x2));
                    selectionRect.setHeight(Math.abs(y1 - y2));
                } else if (action == Action.SCROLLING || action == Action.PREPARE_SELECTING_POSITION || action == Action.SELECTING_POSITION_DOWN) {
                    if (action == Action.SELECTING_POSITION_DOWN) {
                        action = Action.PREPARE_SELECTING_POSITION;
                    }
                    AnimationUtils.cancelMapAnimation();
                    scroller.setHorizontalScrollPosition(scroller.getHorizontalScrollPosition() + (lastX - x));
                    scroller.setVerticalScrollPosition(scroller.getVerticalScrollPosition() + (lastY - y));
                    lastX = x;
                    lastY = y;
                    parent.getMiniMap().getMap().updateRectangle();
                } else {
                    x = event.getX();
                    y = event.getY();
                    updateRectangle(getPointX(getPositionX(x)), getPointY(getPositionY(y)));
                    MainPanel.getInstance().getMapUtils().updateLevelLabel(MapStore.getInstance().getSectorByXY(getPositionX(x), getPositionY(y)), MainPanel.getInstance().getPlayback().getRound() - 1);
//                    MainPanel.getInstance().getSectorPanel().updateSectorInfo(MapStore.getInstance().getSectorByXY(getPositionX(x), getPositionY(y)));
                }
                if (action == Action.PREPARE_SELECTING_POSITION || action == Action.SELECTING_POSITION) {
                    if (movementLine != null && movementLine.isAttached()) {
                        remove(movementLine);
                    }
                    movementLine = MainPanel.getInstance().getMapUtils().getArrow(getPointX(brigToSave.getLastCheckPoint(isBasicOrder).getX()) + 32,
                            getPointY(brigToSave.getLastCheckPoint(isBasicOrder).getY()) + 32,
                            getPointX(getPositionX(event.getX())) + 32,
                            getPointY(getPositionY(event.getY())) + 32);
                    if (brigToSave.getBrigadeId() > 0) {
                        add(movementLine);
                    }

                    if (selectedGroup.isAttached()) {
                        selectedGroup.drawMovementLines(getPositionX(event.getX()), getPositionY(event.getY()), isBasicOrder);
                    }
                } else {
                    if (movementLine.isAttached()) {
                        remove(movementLine);
                    }
                    if (selectedGroup.isAttached()) {
                        selectedGroup.hideMovementLines();
                    }
                }

            }
        }, MouseMoveEvent.getType());
        addDomHandler(new MouseWheelHandler() {
            @Override
            public void onMouseWheel(final MouseWheelEvent event) {
                if (event.getDeltaY() < 0) {
                    zoom(true);
                } else if (event.getDeltaY() > 0) {
                    zoom(false);
                }
            }
        }, MouseWheelEvent.getType());

    }

    public boolean canBrigadeMoveToSector(final FieldBattleSectorDTO sector, final BrigadeDTO brigade) {
        final boolean hasArtillery = brigade.hasArtillery();
        if (sector == null) {
            return false;
        }
        if (sector.getNationSetup() != BaseStore.getInstance().getNationId()) {
            return false;
        }
        if (sector.hasWall()) {
            return false;
        }
        if (sector.hasTown() && hasArtillery) {
            return false;
        }
        if (sector.hasVillage() && hasArtillery) {
            return false;
        }
        if (sector.hasChateau() && hasArtillery) {
            return false;
        }
        if (sector.isMinorRiver() || sector.isMajorRiver()) {
            return false;
        }
        if (sector.isLake()) {
            return false;
        }
        final List<BrigadeDTO> sectorBrigades = ArmyStore.getInstance().getBrigadesByPosition(sector.getX(), sector.getY());
        for (BrigadeDTO other : sectorBrigades) {
            if (other.getBrigadeId() != brigade.getBrigadeId()) {
                return false;
            }
        }
        return true;

    }

    public void zoom(double level) {
        zoomLevel = level;
        doZoom(level);
        MainPanel.getInstance().getMiniMap().getMap().updateRectangle();
    }
    static final double MAX_ZOOM = 1.2;
    public void zoom(boolean zoomIn) {
        double screenHeight = (double) Window.getClientHeight();
        double mapHeight = (double) MainPanel.getInstance().getMapUtils().getMapHeight();
        double minZoom = screenHeight * 1.0 / mapHeight;
        if (minZoom < 0.1) {
            minZoom = 0.1;
        }
        if (zoomIn) {
            zoomLevel += 0.1;
            if (zoomLevel > MAX_ZOOM) {
                zoomLevel = MAX_ZOOM;
            }
            doZoom(zoomLevel);
        } else {
            zoomLevel -= 0.1;
            if (zoomLevel < minZoom) {
                zoomLevel = minZoom;
            }
            doZoom(zoomLevel);
        }
        MainPanel.getInstance().getMiniMap().getMap().updateRectangle();
    }

    public void rememberCurrentZoom() {
        zoomToRemember = zoomLevel;
    }

    public void zoomToRememberedZoom() {
        zoom(zoomToRemember);
    }


    public void setScroller(final ScrollPanel value) {
        this.scroller = value;
    }

    private native void doZoom(double zoom) /*-{
        var svgEl = $wnd.$(".mapstyle");
        if (zoom == 1) {
            svgEl.css({transform:""});
        } else {
            svgEl.css({transform:"scale(" + zoom + "," + zoom + ")" });
        }
    }-*/;




    public void selectPosition(final BrigadeDTO brigade, boolean forSp, final boolean isBasic) {
        MainPanel.getInstance().getMapUtils().hideBrigadeMenu(brigade);

//        MainPanel.getInstance().fitMapToScreen();
        brigToSave = brigade;
        action = Action.SELECTING_POSITION;
//        MainPanel.getInstance().getMapUtils().showCoordinatesGroup();
        brigSaveStrategicPoint = forSp;
        isBasicOrder = isBasic;
    }

    public boolean isNormal() {
        return action == Action.NORMAL;
    }



    public void moveMap() {
        int horMove = 0;
        int verMove = 0;
        if (DIRECTIONS[0]) {
            horMove -= 4;
        }
        if (DIRECTIONS[1]) {
            verMove += 4;
        }
        if (DIRECTIONS[2]) {
            horMove += 4;
        }
        if (DIRECTIONS[3]) {
            verMove -= 4;
        }
        AnimationUtils.cancelMapAnimation();
        scroller.setHorizontalScrollPosition(scroller.getHorizontalScrollPosition() + horMove);
        scroller.setVerticalScrollPosition(scroller.getVerticalScrollPosition() + verMove);
        MainPanel.getInstance().getMiniMap().getMap().updateRectangle();
    }

    public void handleKeyDownEvent(final int key) {
        final Timer tm = new Timer() {
            public void run() {
                if (DIRECTIONS[0] || DIRECTIONS[1] || DIRECTIONS[2] || DIRECTIONS[3]) {
                    moveMap();
                } else {
                    this.cancel();
                }
            }
        };
        if (key == 'a' || key == 'A') {
            DIRECTIONS[0] = true;
        } else if (key == 's' || key == 'S') {
            DIRECTIONS[1] = true;
        } else if (key == 'd' || key == 'D') {
            DIRECTIONS[2] = true;
        } else if (key == 'w' || key == 'W') {
            DIRECTIONS[3] = true;
        } else if (key == 'c' || key == 'C') {
            cAction = KeyAction.PRESSED;
        } else if (key == 'm' || key == 'M') {
            if (MainPanel.getInstance().getOverviewPanel().isVisible()) {
                AnimationUtils.hideElement(MainPanel.getInstance().getOverviewPanel());
            } else {
                AnimationUtils.showElement(MainPanel.getInstance().getOverviewPanel(), new BasicHandler() {
                    @Override
                    public void run() {
                        MainPanel.getInstance().getOverviewPanel().resizeBar();
                    }
                });
            }
        }

        tm.scheduleRepeating(1);
    }

    public void handleKeyUpEvent(final int key) {
        if (key == 'a' || key == 'A') {
            DIRECTIONS[0] = false;
        } else if (key == 's' || key == 'S') {
            DIRECTIONS[1] = false;
        } else if (key == 'd' || key == 'D') {
            DIRECTIONS[2] = false;
        } else if (key == 'w' || key == 'W') {
            DIRECTIONS[3] = false;
        } else if (key == 'c' || key == 'C') {
            cAction = KeyAction.NONE;
        }
    }

    public void updateRectangle(final int posX, final int posY) {
        selectionRectangle.setX(posX);
        selectionRectangle.setY(posY);
    }

    public double getZoomedTileSize() {
        return zoomLevel * TILE_WIDTH;
    }

    public int getZoomedPointY(final int posY) {
        return (int) ((SIDE_OFFSET + posY) * zoomLevel * TILE_WIDTH);
    }

    public int getPositionX(final int pointX) {
        return (int) ((pointX / getZoomedTileSize()) - SIDE_OFFSET);
    }

    public int getPositionY(final int pointY) {
        return (int) ((pointY / getZoomedTileSize()) - SIDE_OFFSET);
    }

    public int getPointX(final int posX) {
        return (int) ((SIDE_OFFSET + posX) * TILE_WIDTH);
    }

    public double getZoomLevel() {
        return zoomLevel;
    }

    public int getZoomedPointX(final int posX) {
        return (int) ((SIDE_OFFSET + posX) * zoomLevel * TILE_WIDTH);
    }

    public int getPointY(final int posY) {
        return (int) ((SIDE_OFFSET + posY) * TILE_WIDTH);
    }

    public Rectangle getSelectionRectangle() {
        return selectionRectangle;
    }

    public void setSelectionRectangle(Rectangle selectionRectangle) {
        this.selectionRectangle = selectionRectangle;
    }

    public ScrollPanel getScroller() {
        return scroller;
    }

    public double getZoomOffsetX() {
        return (1 - zoomLevel) * MainPanel.getInstance().getMapUtils().getMapWidth() / 2;//5632
    }

    public double getZoomOffsetY() {
        return (1 - zoomLevel) * MainPanel.getInstance().getMapUtils().getMapHeight() / 2;//5312
    }

    public MultiSelectGroup getSelectedGroup() {
        return selectedGroup;
    }
}
