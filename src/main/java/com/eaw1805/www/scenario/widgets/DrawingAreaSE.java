package com.eaw1805.www.scenario.widgets;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.eaw1805.data.constants.TerrainConstants;
import com.eaw1805.www.scenario.stores.*;
import com.eaw1805.www.scenario.stores.map.TileGroup;
import com.eaw1805.www.scenario.views.EditorPanel;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Rectangle;


public class DrawingAreaSE extends DrawingArea implements HasContextMenuHandlers {
    private static int MOUSE_BUTTON_LEFT = 1;
    private static int MOUSE_BUTTON_RIGHT = 2;
    private static int MOUSE_BUTTON_MIDDLE = 4;

    enum Action {
        NORMAL,
        DOWN

    }

    enum KeyAction {
        NONE,
        PRESSED
    }
    Action action = Action.NORMAL;
    KeyAction zAction = KeyAction.NONE;
    KeyAction xAction = KeyAction.NONE;
    KeyAction cAction = KeyAction.NONE;
    private int lastX = 0;
    private int lastY = 0;
    boolean[] DIRECTIONS = new boolean[4];
    ScrollPanel scroller;
    final TileMenu tileMenu;
    int[] selectionStart = new int[2];
    Rectangle selectionRect = new Rectangle(0, 0, 0, 0);

    public DrawingAreaSE(int width, int height) {
        super(width, height);
        selectionRect.setFillColor("red");
        selectionRect.setFillOpacity(0.3);
        selectionRect.setStrokeColor("black");
        selectionRect.setStrokeOpacity(0.5);
        setStyleName("mapstyle");
        tileMenu = new TileMenu();
        addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(final MouseDownEvent event) {
                event.stopPropagation();
                event.preventDefault();

                if (!tileMenu.isAttached() || xAction == KeyAction.PRESSED
                        || cAction == KeyAction.PRESSED) {
                    action = Action.DOWN;
                }
//                EditorPanel.getInstance().setDebugMessage("key : " + event.getNativeButton());

                if (xAction == KeyAction.PRESSED) {
                    if (event.getNativeButton() == MOUSE_BUTTON_LEFT) {
                        int x = event.getClientX();
                        int y = event.getClientY();
                        x = EditorMapUtils.getInstance().translateToMapX(x);
                        y = EditorMapUtils.getInstance().translateToMapY(y);
                        tileMenu.selectTile(x, y, true);
                    } else if (event.getNativeButton() == MOUSE_BUTTON_RIGHT) {
                        tileMenu.hideTileMenu();
                        int x = event.getClientX();
                        int y = event.getClientY();
                        x = EditorMapUtils.getInstance().translateToMapX(x);
                        y = EditorMapUtils.getInstance().translateToMapY(y);
                        tileMenu.selectTile(x, y, true);
                        tileMenu.showTileMenu();
                    }
                } else if (cAction == KeyAction.PRESSED) {
                    if (!selectionRect.isAttached()) {
                        add(selectionRect);
                    }
                    int x = event.getClientX();
                    int y = event.getClientY();
                    double zoomEffect = 1.0 / zoomLevel;
                    x = (int)(EditorMapUtils.getInstance().translateToMapXPure(x)*zoomEffect);
                    y = (int)(EditorMapUtils.getInstance().translateToMapYPure(y)*zoomEffect);
                    EditorPanel.getInstance().setDebugMessage(event.getClientX() + " : " + zoomLevel + " : " + zoomEffect + " : " + EditorMapUtils.getInstance().translateToMapXPure(x) + " : " + x + " : " + y);

                    selectionStart[0] = x;
                    selectionStart[1] = y;
                    if (event.getNativeButton() == MOUSE_BUTTON_LEFT) {

                        selectionRect.setX(x);
                        selectionRect.setY(y);
                        tileMenu.selectTile(x, y, true);
                    } else if (event.getNativeButton() == MOUSE_BUTTON_RIGHT) {
                        tileMenu.hideTileMenu();
                        selectionRect.setX(x);
                        selectionRect.setY(y);
//                        tileMenu.selectTile(x, y, true);
//                        tileMenu.showTileMenu();
                    }
                } else {
                    tileMenu.hideTileMenu();
                }

                lastX = event.getClientX();
                lastY = event.getClientY();

                DrawingAreaSE.this.setStyleName("noScrollBarsMove", true);
                DrawingAreaSE.this.removeStyleName("noScrollBars");
//                setStyleName("pointer");
            }
        });

        addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                event.stopPropagation();
                event.preventDefault();
            }
        });

        addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent mouseUpEvent) {
                mouseUpEvent.preventDefault();
                mouseUpEvent.stopPropagation();
                if (action == Action.DOWN && (cAction == KeyAction.PRESSED || selectionRect.isAttached())) {
                    if (selectionRect.isAttached()) {
                        int posXStart = EditorMapUtils.getInstance().getPositionXWithoutZoom(selectionRect.getX());
                        int posYStart = EditorMapUtils.getInstance().getPositionYWithoutZoom(selectionRect.getY());
                        if (posXStart < 0) {
                            posXStart = 0;
                        }
                        if (posYStart < 0) {
                            posYStart = 0;
                        }
                        int posXEnd = EditorMapUtils.getInstance().getPositionXWithoutZoom(selectionRect.getX() + selectionRect.getWidth());
                        int posYEnd = EditorMapUtils.getInstance().getPositionYWithoutZoom(selectionRect.getY() + selectionRect.getHeight());
                        if (posXEnd >= RegionSettings.sizeX) {
                            posXEnd = RegionSettings.sizeX - 1;
                        }
                        if (posYEnd >= RegionSettings.sizeY) {
                            posYEnd = RegionSettings.sizeY - 1;
                        }

                        for (int indexX = posXStart; indexX <= posXEnd; indexX++) {
                            for (int indexY = posYStart; indexY <= posYEnd; indexY++) {
                                tileMenu.selectTile(indexX, indexY, false);
                            }
                        }
                        if (posXEnd >= posXStart && posYEnd >= posYStart) {
                            tileMenu.showTileMenu();
                        }
                        selectionRect.setWidth(0);
                        selectionRect.setHeight(0);
                        remove(selectionRect);
                    }
                } else if (action == Action.DOWN && (xAction == KeyAction.PRESSED || tileMenu.isAttached())
                        && mouseUpEvent.getNativeButton() == 1) {
                    tileMenu.showTileMenu();
                } else if (action == Action.DOWN && zAction != KeyAction.PRESSED && xAction != KeyAction.PRESSED) {
                    int x = mouseUpEvent.getClientX();
                    int y = mouseUpEvent.getClientY();
                    x = EditorMapUtils.getInstance().translateToMapX(x);
                    y = EditorMapUtils.getInstance().translateToMapY(y);
                    try {
                        if (RegionSettings.region != null && x >= 0 && y >= 0 && x < RegionSettings.sizeX && y < RegionSettings.sizeY) {
                            if (EditorPanel.getInstance().isMapEdit() && EditorPanel.getInstance().getBrushView().isBrushEnabled()) {
                                final TileGroup sectorGroup = EditorMapUtils.getInstance().getSectorTileGroup(x, y);
                                if (!EditorPanel.getInstance().getBrushView().isOnlyLand() || sectorGroup.getSector().getTerrain().getId() != TerrainConstants.TERRAIN_O) {
                                    if (EditorPanel.getInstance().getBrushView().isReset()) {
                                        RegionUtils.resetSector(x, y);
                                    } else {
                                        RegionUtils.updateSector(x, y);
                                    }
                                }
                                EditorPanel.getInstance().getMapOverView().updateOverView();
                            }
                            if (EditorPanel.getInstance().isArmyEdit() && EditorPanel.getInstance().getArmyBrush().isBrushEnabled()) {
                                BrigadeUtils.createBrigade(x, y);
                                EditorPanel.getInstance().getArmyOverView().updateOverview();
                            }
                            if (EditorPanel.getInstance().isCommanderEdit() && EditorPanel.getInstance().getCommBrush().isBrushEnabled()) {
                                CommanderUtils.createCommander(x, y);
                                EditorPanel.getInstance().getCommOverview().updateOverview();
                            }
                            if (EditorPanel.getInstance().isSpyBTrainEdit() && EditorPanel.getInstance().getSpyBTrainBrush().isBrushEnabled()) {
                                if (EditorPanel.getInstance().getSpyBTrainBrush().isSpy()) {
                                    SpyUtils.createSpy(x, y);
                                } else {
                                    BaggageTrainUtils.createBaggageTrain(x, y);
                                }
                                EditorPanel.getInstance().getSpyBTrainOverView().updateOverview();
                            }
                            if (EditorPanel.getInstance().isShipEdit() && EditorPanel.getInstance().getShipBrush().isBrushEnabled()) {
                                ShipUtils.createShip(x, y);
                                EditorPanel.getInstance().getShipOverView().updateOverview();
                            }
                        }
                    } catch (Exception e) {
                        EditorPanel.getInstance().setDebugMessage("Failed to update sector : " + x +", " + y);
                    }
                }
                action = Action.NORMAL;

//                selectionRectangle.setFillColor("red");
                DrawingAreaSE.this.setStyleName("noScrollBars", true);
                DrawingAreaSE.this.removeStyleName("noScrollBarsMove");
//                removeStyleName("pointer");
            }
        });

        addMouseMoveHandler(new MouseMoveHandler() {
            @Override
            public void onMouseMove(final MouseMoveEvent event) {
                event.preventDefault();
                event.stopPropagation();
                int x = event.getClientX();
                int y = event.getClientY();
                if (cAction == KeyAction.PRESSED && action == Action.DOWN  && event.getNativeButton() == 1) {
                    x = event.getClientX();
                    y = event.getClientY();
                    double zoomEffect = 1.0 / zoomLevel;
                    int x1 = (int)(EditorMapUtils.getInstance().translateToMapXPure(x) * zoomEffect);
                    int y1 = (int)(EditorMapUtils.getInstance().translateToMapYPure(y) * zoomEffect);

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
                } else if (xAction == KeyAction.PRESSED && action == Action.DOWN && event.getNativeButton() == 1) {
                    x = EditorMapUtils.getInstance().translateToMapX(x);
                    y = EditorMapUtils.getInstance().translateToMapY(y);
                    tileMenu.selectTile(x, y, false);
                } else if (zAction == KeyAction.PRESSED && action == Action.DOWN) {
                    scroller.setHorizontalScrollPosition(scroller.getHorizontalScrollPosition() + (lastX - x));
                    scroller.setScrollPosition(scroller.getScrollPosition() + (lastY - y));
//                    EditorPanel.getInstance().setDebugMessage("hovering : " + EditorMapUtils.getInstance().translateToMapX(x) +", " + EditorMapUtils.getInstance().translateToMapY(y));
                    lastX = x;
                    lastY = y;
                } else if (zAction == KeyAction.NONE && action == Action.DOWN && !tileMenu.isAttached()) {
                    x = EditorMapUtils.getInstance().translateToMapX(x);
                    y = EditorMapUtils.getInstance().translateToMapY(y);
                    try {
                        if (RegionSettings.region != null && x >= 0 && y >= 0 && x < RegionSettings.sizeX && y < RegionSettings.sizeY) {
                            if (EditorPanel.getInstance().getBrushView().isBrushEnabled()
                                    && EditorPanel.getInstance().isMapEdit()) {
                                final TileGroup sectorGroup = EditorMapUtils.getInstance().getSectorTileGroup(x, y);
                                if (!EditorPanel.getInstance().getBrushView().isOnlyLand() || sectorGroup.getSector().getTerrain().getId() != TerrainConstants.TERRAIN_O) {
                                    if (EditorPanel.getInstance().getBrushView().isReset()) {
                                        RegionUtils.resetSector(x, y);
                                    } else {
                                        RegionUtils.updateSector(x, y);
                                    }

                                }
                            }
                        }
                    } catch (Exception e) {
                        EditorPanel.getInstance().setDebugMessage("Failed to update sector : " + x +", " + y);
                    }
                }

            }
        });

        addContextMenuHandler(new ContextMenuHandler() {

            public void onContextMenu(final ContextMenuEvent event) {
                event.preventDefault();
                event.stopPropagation();
            }

        });

        addMouseWheelHandler(new MouseWheelHandler() {
            @Override
            public void onMouseWheel(final MouseWheelEvent event) {
                event.stopPropagation();
                event.preventDefault();
                if (event.getDeltaY() < 0) {
                    zoom(true);
                } else if (event.getDeltaY() > 0) {
                    zoom(false);
                }
            }
        });

    }

    static final double MAX_ZOOM = 3d;
    private double zoomLevel = 1.0;
    public void zoom(boolean zoomIn) {
        double screenHeight = (double) Window.getClientHeight();
        double mapHeight = (double) EditorMapUtils.getInstance().getMapHeight();
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
    }

    public final HandlerRegistration addContextMenuHandler(final ContextMenuHandler handler) {
        return addDomHandler(handler, ContextMenuEvent.getType());
    }

    private native void doZoom(double zoom) /*-{
        var svgEl = $wnd.$(".mapstyle");
        if (zoom == 1) {
            svgEl.css({transform:""});
        } else {
            svgEl.css({transform:"scale(" + zoom + "," + zoom + ")" });
        }
    }-*/;


    public void setScroller(final ScrollPanel value) {
        this.scroller = value;
    }

    public ScrollPanel getScroller() {
        return scroller;
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
        } else if (key == 'z' || key == 'Z') {
            zAction = KeyAction.PRESSED;
//            EditorPanel.getInstance().setDebugMessage("Move Map Mode - Drag to move");
        } else if (key == 'x' || key == 'X') {
            xAction = KeyAction.PRESSED;
//            EditorPanel.getInstance().setDebugMessage("Sector Map Edit Mode - Use Mouse to select Sectors on map");
        } else if (key == 'c' || key == 'C') {
            cAction = KeyAction.PRESSED;
//            EditorPanel.getInstance().setDebugMessage("Sector Map Edit Mode - Use Mouse to select Sectors on map");
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
        } else if (key == 'z' || key == 'Z') {
            zAction = KeyAction.NONE;
            EditorPanel.getInstance().setDebugMessage("Brush Mode - (hotkeys : Z = move map, X = Edit sectors on map)");
        } else if (key == 'x' || key == 'X') {
            xAction = KeyAction.NONE;
            EditorPanel.getInstance().setDebugMessage("Brush Mode - (hotkeys : Z = move map, X = Edit sectors on map)");
        } else if (key == 'c' || key == 'C') {
            cAction = KeyAction.NONE;
            EditorPanel.getInstance().setDebugMessage("Brush Mode - (hotkeys : Z = move map, X = Edit sectors on map)");
        }
    }

    public void moveMap(final int x, final int y) {
        int scrollX = EditorMapUtils.getInstance().getPointX(x);
        int scrollY = EditorMapUtils.getInstance().getPointY(y);
        scroller.setHorizontalScrollPosition(scrollX - Window.getClientWidth()/2);
        scroller.setScrollPosition(scrollY - Window.getClientHeight()/2);

    }

    public void moveMap() {
        if (zAction == KeyAction.PRESSED) {
        //ok use a,s,d,w to move map. But only if Z,z button is pressed. Otherwise it is confusing when editing a text box
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
            scroller.setHorizontalScrollPosition(scroller.getHorizontalScrollPosition() + horMove);
            scroller.setScrollPosition(scroller.getScrollPosition() + verMove);
        }
//        MainPanel.getInstance().getMiniMap().getMap().updateRectangle();
    }

    public double getZoomLevel() {
        return zoomLevel;
    }

}
