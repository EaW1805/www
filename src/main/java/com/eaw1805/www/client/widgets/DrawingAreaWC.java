package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.www.client.events.map.MapEventManager;
import com.eaw1805.www.client.events.map.RegionChangedEvent;
import com.eaw1805.www.client.events.map.RegionChangedHandler;
import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.events.movement.MovementStopEvent;
import com.eaw1805.www.client.events.movement.MovementStopHandler;
import com.eaw1805.www.client.events.tutorial.TutorialEventManager;
import com.eaw1805.www.client.gui.GuiComponent;
import com.eaw1805.www.client.views.RelationsView;
import com.eaw1805.www.client.views.SpyReportsView;
import com.eaw1805.www.client.views.TradePanelView;
import com.eaw1805.www.client.views.frames.BattleFrame;
import com.eaw1805.www.client.views.layout.MapsView;
import com.eaw1805.www.client.views.layout.MiniMapPanel;
import com.eaw1805.www.client.views.military.deployment.DeployTroopsView;
import com.eaw1805.www.client.views.popups.actions.TileActionsPopup;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.SpyStore;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Rectangle;


public class DrawingAreaWC
        extends DrawingArea
        implements RegionConstants, HasContextMenuHandlers, HasKeyPressHandlers, HasKeyDownHandlers, HasKeyUpHandlers, ArmyConstants {

    public enum Mode {
        NORMAL, MOVING, NAVIGATING
    }

    boolean[] DIRECTIONS = new boolean[4];

    private Mode state = Mode.NORMAL, prState = Mode.NORMAL;

    private int posX = 0, posY = 0;
    private int posX2 = 0, posY2 = 0;
    double dist = 0;


    private transient ScrollPanel mapScrollPanel;

    private transient MiniMapPanel miniMapPanel;

    private transient final MapStore mapStore = MapStore.getInstance();

    private transient final RegionStore regionStore = RegionStore.getInstance();

    private transient final TileActionsPopup tileActions = new TileActionsPopup();

    private transient final MapsView mapsView;

    private transient int posDownX, posDownY;

    private transient final Rectangle[] mapRectangle;

    private transient int[] boundTopX, boundTopY, boundBottomX, boundBottomY;

    public DrawingAreaWC(final int width, final int height, final MapsView mapsView) {
        super(width, height);
        this.mapsView = mapsView;
        this.setStyleName("noScrollBars");
        final DrawingAreaWC myself = this;

        // Setup selection rectangle
        mapRectangle = new Rectangle[5];
        boundTopX = new int[5];
        boundTopY = new int[5];
        boundBottomX = new int[5];
        boundBottomY = new int[5];


        final int tileSize = (int) mapStore.getTileSize();
        for (int region = EUROPE; region <= AFRICA; region++) {
            mapRectangle[region] = new Rectangle(0, 0, tileSize, tileSize);
            mapRectangle[region].setFillOpacity(0.2);
            mapRectangle[region].setFillColor("red");

            boundTopX[region] = 0;
            boundTopY[region] = 0;
            boundBottomX[region] = 0;
            boundBottomY[region] = 0;
        }

        this.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {

                boolean draggingMap = false;
                if (event.getClientX() != posDownX || event.getClientY() != posDownY) {
                    draggingMap = true;
                }

                if (state == Mode.NORMAL && prState != Mode.NAVIGATING) {
                    final int regionId = mapStore.getActiveRegion();
                    final int quarter = mapStore.getQuarter(event.getX(), event.getY());
                    final SectorDTO selSector = regionStore.getSelectedSector(regionId);

                    if (!mapStore.isNavigating() && !draggingMap) {
                        showActionPopup(selSector, quarter);
                    }

                    mapRectangle[regionId].setX(mapStore.getPointX(selSector.getX()));
                    mapRectangle[regionId].setY(mapStore.getPointY(selSector.getY()));
                }
                if (TutorialStore.getInstance().isTutorialMode()) {
                    TutorialEventManager.PositionClicked(event.getX(), event.getY());
                }
            }
        });

        this.addMouseWheelHandler(new MouseWheelHandler() {
            @Override
            public void onMouseWheel(MouseWheelEvent event) {
                event.stopPropagation();
                event.preventDefault();
                if (event.getDeltaY() < 0) {
                    MapStore.getInstance().zoom(true);
                } else if (event.getDeltaY() > 0) {
                    MapStore.getInstance().zoom(false);
                }
            }
        });

        this.addMouseMoveHandler(new MouseMoveHandler() {
            public void onMouseMove(final MouseMoveEvent event) {
                event.preventDefault();

                if (state == Mode.NORMAL && prState != Mode.NAVIGATING) {
                    final int thisX = event.getX();
                    final int thisY = event.getY();
                    final int regionId = mapStore.getActiveRegion();
                    if (thisX < boundTopX[regionId]
                            || thisX > boundBottomX[regionId]
                            || thisY < boundTopY[regionId]
                            || thisY > boundBottomY[regionId]) {

                        final SectorDTO targetSector = mapStore.getTileInfoByPixels(thisX, thisY);
                        regionStore.setSelectedSector(targetSector);

                        mapRectangle[regionId].setX(mapStore.getPointX(targetSector.getX()));
                        mapRectangle[regionId].setY(mapStore.getPointY(targetSector.getY()));

                        boundTopX[regionId] = mapStore.getZoomedPointX(targetSector.getX());
                        boundTopY[regionId] = mapStore.getZoomedPointY(targetSector.getY());

                        boundBottomX[regionId] = (int) (boundTopX[regionId] + mapStore.getZoomedTileSize());
                        boundBottomY[regionId] = (int) (boundTopY[regionId] + mapStore.getZoomedTileSize());
//                        Window.alert(boundTopX[regionId] +", " + boundTopY[regionId] + " - " + boundBottomX[regionId] +", " + boundBottomY[regionId]);
                    }

                } else if (state == Mode.NAVIGATING) {

                    final int thisX = event.getX();
                    final int thisY = event.getY();
                    final int regionId = mapStore.getActiveRegion();

                    mapScrollPanel.setHorizontalScrollPosition(mapScrollPanel.getHorizontalScrollPosition() - ((thisX - posX)));
                    mapScrollPanel.setVerticalScrollPosition(mapScrollPanel.getVerticalScrollPosition() - ((thisY - posY)));

                    final float miniX = miniMapPanel.getWidth() / (float) (myself.getWidth() * mapStore.getZoomLevelSettings());
                    final float miniY = miniMapPanel.getHeight() / (float) (myself.getHeight() * mapStore.getZoomLevelSettings());

                    final Rectangle rect = miniMapPanel.getRectangle();
                    rect.setX((int) ((mapScrollPanel.getHorizontalScrollPosition() - mapStore.getZoomOffsetX()) * miniX));
                    rect.setY((int) ((mapScrollPanel.getVerticalScrollPosition() - mapStore.getZoomOffsetY()) * miniY));

                    posX = event.getX();
                    posY = event.getY();

                    mapsView.setLastKnownPosition(regionId,
                            mapScrollPanel.getHorizontalScrollPosition(),
                            mapScrollPanel.getVerticalScrollPosition(),
                            rect.getX(), rect.getY());
                }
            }
        });


        addDomHandler(new TouchMoveHandler() {
            @Override
            public void onTouchMove(TouchMoveEvent event) {
                event.preventDefault();
//                if (event.getTouches().length() == 1) {
                    final int thisX = event.getTouches().get(0).getClientX();
                    final int thisY = event.getTouches().get(0).getClientY();
                    final int regionId = mapStore.getActiveRegion();

                    mapScrollPanel.setHorizontalScrollPosition(mapScrollPanel.getHorizontalScrollPosition() - ((thisX - posX)));
                    mapScrollPanel.setVerticalScrollPosition(mapScrollPanel.getVerticalScrollPosition() - ((thisY - posY)));

                    final float miniX = miniMapPanel.getWidth() / (float) (myself.getWidth() * mapStore.getZoomLevelSettings());
                    final float miniY = miniMapPanel.getHeight() / (float) (myself.getHeight() * mapStore.getZoomLevelSettings());

                    final Rectangle rect = miniMapPanel.getRectangle();
                    rect.setX((int) ((mapScrollPanel.getHorizontalScrollPosition() - mapStore.getZoomOffsetX()) * miniX));
                    rect.setY((int) ((mapScrollPanel.getVerticalScrollPosition() - mapStore.getZoomOffsetY()) * miniY));

                    posX = thisX;
                    posY = thisY;

                    mapsView.setLastKnownPosition(regionId,
                            mapScrollPanel.getHorizontalScrollPosition(),
                            mapScrollPanel.getVerticalScrollPosition(),
                            rect.getX(), rect.getY());
//                }
//                else if (event.getTouches().length() == 2) {
//                    posX = event.getTouches().get(0).getClientX();
//                    posY = event.getTouches().get(0).getClientY();
//                    posX2 = event.getTouches().get(1).getClientX();
//                    posY2 = event.getTouches().get(1).getClientY();
//                    double curDist = Math.sqrt((posX2 - posX)*(posX2-posX)+ (posY2 - posY)*(posY2 - posY));
//                    if (curDist > dist + 20) {
//                        mapStore.zoom(true);
//                        dist = curDist;
//                    } else if (curDist < dist - 20) {
//                        mapStore.zoom(false);
//                        dist = curDist;
//                    }
//                }

            }
        }, TouchMoveEvent.getType());


        this.addContextMenuHandler(new ContextMenuHandler() {

            public void onContextMenu(final ContextMenuEvent event) {
                event.preventDefault();
                event.stopPropagation();
            }

        });

        this.addMouseDownHandler(new MouseDownHandler() {

            public void onMouseDown(final MouseDownEvent event) {
                posDownX = event.getClientX();
                posDownY = event.getClientY();
                event.preventDefault();
                setState(Mode.NAVIGATING);
                posX = event.getX();
                posY = event.getY();
                myself.setStylePrimaryName("noScrollBarsMove");
            }
        });

        addDomHandler(new TouchStartHandler() {
            @Override
            public void onTouchStart(TouchStartEvent event) {
//                if (event.getTouches().length() == 1) {
                    posX = event.getTouches().get(0).getClientX();
                    posY = event.getTouches().get(0).getClientY();

//                } else if (event.getTouches().length() == 2) {
//                    posX = event.getTouches().get(0).getClientX();
//                    posY = event.getTouches().get(0).getClientY();
//                    posX2 = event.getTouches().get(1).getClientX();
//                    posY2 = event.getTouches().get(1).getClientY();
//
//                    dist = Math.sqrt((posX2 - posX)*(posX2-posX)+ (posY2 - posY)*(posY2 - posY));
//                }
                myself.setStylePrimaryName("noScrollBarsMove");
            }
        }, TouchStartEvent.getType());

        this.addMouseUpHandler(new MouseUpHandler() {

            public void onMouseUp(final MouseUpEvent event) {
                setState(prState);
                myself.setStylePrimaryName("noScrollBars");
                if (mapStore.getRegionFiguresById(mapStore.getActiveRegion()).getVectorObjectCount() == 0) {
                    mapStore.setNavigating(false);
                }
            }
        });

//        addDomHandler(new TouchEndHandler() {
//            @Override
//            public void onTouchEnd(TouchEndEvent event) {
//                setState(prState);
//                myself.setStylePrimaryName("noScrollBars");
//                if (mapStore.getRegionFiguresById(mapStore.getActiveRegion()).getVectorObjectCount() == 0) {
//                    mapStore.setNavigating(false);
//                }
//            }
//        }, TouchEndEvent.getType());


        this.addMouseOutHandler(new MouseOutHandler() {

            public void onMouseOut(final MouseOutEvent event) {
                setState(prState);
                myself.setStylePrimaryName("noScrollBars");
            }
        });

        MovementEventManager.addMovementStopHandler(new MovementStopHandler() {
            public void onMovementStop(final MovementStopEvent event) {
                setState(Mode.NORMAL);
                setState(Mode.NORMAL);
            }
        });

        MapEventManager.addRegionChangedHandler(new RegionChangedHandler() {
            public void onRegionChanged(final RegionChangedEvent event) {
                final int regionId = event.getRegionId();
                final Group regionMapImages = mapStore.getRegionMapImagesByRegionId(regionId);
                regionMapImages.remove(mapRectangle[regionId]);

                final int tileSize = (int) mapStore.getTileSize();
                mapRectangle[regionId].setSize(tileSize + "px", tileSize + "px");

                boundTopX[regionId] = 0;
                boundTopY[regionId] = 0;
                boundBottomX[regionId] = 0;
                boundBottomY[regionId] = 0;

                regionMapImages.add(mapRectangle[regionId]);
            }
        });

    }

    public void onAttach() {
        super.onAttach();
        if (GameStore.getInstance().getZoomLevel() > 0) {
            mapStore.doZoom(GameStore.getInstance().getZoomLevel());
        }
    }

    public void reAddRectangle() {
        final int regionId = MapStore.getInstance().getActiveRegion();
        final Group regionMapImages = mapStore.getRegionMapImagesByRegionId(regionId);
        regionMapImages.remove(mapRectangle[regionId]);

        final int tileSize = (int) mapStore.getTileSize();
        mapRectangle[regionId].setSize(tileSize + "px", tileSize + "px");

        boundTopX[regionId] = 0;
        boundTopY[regionId] = 0;
        boundBottomX[regionId] = 0;
        boundBottomY[regionId] = 0;

        regionMapImages.add(mapRectangle[regionId]);
    }

    public void initMapRectangle() {
        for (int region = EUROPE; region <= AFRICA; region++) {
            final Group regionMapImages = mapStore.getRegionMapImagesByRegionId(region);
            regionMapImages.add(mapRectangle[region]);
        }
    }

    public final HandlerRegistration addContextMenuHandler(final ContextMenuHandler handler) {
        return addDomHandler(handler, ContextMenuEvent.getType());
    }

    private void showActionPopup(final SectorDTO selSector, final int quarter) {
        boolean hasOther = false;
        final double tileSize = mapStore.getZoomedTileSize();
        switch (quarter) {
            case 1:
                if (ArmyStore.getInstance().hasSectorArmies(selSector)) {
                    hasOther = true;
                }
                break;

            case 2:
                if (CommanderStore.getInstance().hasSectorFreeCommanders(selSector)) {
                    hasOther = true;
                }
                break;

            case 3:
                if (BaggageTrainStore.getInstance().hasSectorBtrains(selSector)) {
                    hasOther = true;
                }
                break;

            case 4:
                if (SpyStore.getInstance().hasSectorSpies(selSector)) {
                    hasOther = true;
                }
                break;

            default:
                break;
        }

        if (!hasOther
                && GameStore.getInstance().getNationId() == selSector.getNationId()) {
            tileActions.initActions(selSector);
            final int x = mapStore.getZoomedPointX(selSector.getX()) + (int) mapStore.getZoomOffsetX() - mapScrollPanel.getHorizontalScrollPosition();
            final int y = mapStore.getZoomedPointY(selSector.getY()) + (int) mapStore.getZoomOffsetY() - mapScrollPanel.getVerticalScrollPosition();
            tileActions.setPopupPosition(x - (int) ((279 - tileSize) / 2d), y - 30 - (int) ((138 - tileSize) / 2d));
            tileActions.show();
        }
    }

    public void setState(final Mode state) {
        setPrState(this.state);
        this.state = state;
    }

    public void setPrState(final Mode value) {
        // Previous state is used to return from
        // navigating state to our previous
        if (value != Mode.NAVIGATING) {
            prState = value;
        }
    }

    /**
     * @return the mapScrollPanel
     */
    public ScrollPanel getMapScrollPanel() {
        return mapScrollPanel;
    }

    /**
     * @param value the mapScrollPanel to set
     */
    public void setMapScrollPanel(final ScrollPanel value) {
        this.mapScrollPanel = value;
    }

    public void scrollKeyUnPressed(final int keyCode) {
        if (keyCode == (int) 'A' || keyCode == (int) 'a') {
            DIRECTIONS[0] = false;

        } else if (keyCode == (int) 'D' || keyCode == (int) 'd') {
            DIRECTIONS[1] = false;

        } else if (keyCode == (int) 'S' || keyCode == (int) 's') {
            DIRECTIONS[2] = false;
        } else if (keyCode == (int) 'W' || keyCode == (int) 'w') {
            DIRECTIONS[3] = false;
        }


    }

    public void scrollKeyPressed(final int keyCode) {
        final Timer tm = new Timer() {
            public void run() {
                if (DIRECTIONS[0] || DIRECTIONS[1] || DIRECTIONS[2] || DIRECTIONS[3]) {
                    moveMap();
                } else {
                    this.cancel();

                }
            }
        };

        if (keyCode == (int) 'A' || keyCode == (int) 'a') {
            DIRECTIONS[0] = true;

        } else if (keyCode == (int) 'D' || keyCode == (int) 'd') {
            DIRECTIONS[1] = true;

        } else if (keyCode == (int) 'S' || keyCode == (int) 's') {
            DIRECTIONS[2] = true;
        } else if (keyCode == (int) 'W' || keyCode == (int) 'w') {
            DIRECTIONS[3] = true;
        } else if (keyCode == 27) {//handle escape button
            final GuiComponent component = GameStore.getInstance().getActiveComponent();
            if (component != null) {
                component.handleEscape();
            }

        } else if (keyCode == 192) {//handle wave button : "~"
            final GuiComponent component = GameStore.getInstance().getActiveComponent();
            if (component != null) {
                component.handleWave();
            }

        } else if (keyCode == 49) {//move into open windows
            GameStore.getInstance().switchWindows();

        } else if (keyCode == 50) {//hide/show all opened windows
            GameStore.getInstance().showHideWindows();

        } else if (keyCode == 51) {//handle "3" key
            final SpyReportsView spyReportsView = new SpyReportsView(DataStore.getInstance().getNations().get(0));
            GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(spyReportsView);
            GameStore.getInstance().getLayoutView().positionTocCenter(spyReportsView);

        } else if (keyCode == 52) {//handle "4" key
            final RelationsView relView = new RelationsView();
            GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(relView);
            GameStore.getInstance().getLayoutView().positionTocCenter(relView);

        } else if (keyCode == 53) {//handle "5" key
            final SpyReportsView spyReportsView = new SpyReportsView(DataStore.getInstance().getNations().get(0));
            GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(spyReportsView);
            GameStore.getInstance().getLayoutView().positionTocCenter(spyReportsView);

        } else if (keyCode == 54) {//handle "6" key
            if (GameStore.getInstance().isNationDead()) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "A dead leader cannot write a newsletter", false);

            } else if (GameStore.getInstance().isGameEnded()) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, cannot write a newsletter.", false);

            } else {
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(GameStore.getInstance().getLayoutView().getOptionsMenu().getNewsView());
                GameStore.getInstance().getLayoutView().positionTocCenter(GameStore.getInstance().getLayoutView().getOptionsMenu().getNewsView());
            }

        } else if (keyCode == 55) {//handle "7" key
            if (GameStore.getInstance().isNationDead()) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Your empire is dead, you cannot deploy troops in transport units.", false);

            } else if (GameStore.getInstance().isGameEnded()) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, you cannot deploy troops in transport units.", false);

            } else {
                final DeployTroopsView dpView = new DeployTroopsView(MapStore.getInstance().getActiveRegion(), 0, 0);
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                GameStore.getInstance().getLayoutView().positionTocCenter(dpView);
            }

        } else if (keyCode == 56) {//handle "8" key
            final BattleFrame frame = new BattleFrame(0, false, true, false, null, false);
            GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(frame);
            GameStore.getInstance().getLayoutView().positionTocCenter(frame);

        } else if (keyCode == 57) {//handle "9" key
            GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(new TradePanelView(null, null, TRADECITY));

        } else if (keyCode == 48) {//handle "0" key
            GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(GameStore.getInstance().getLayoutView().getOptionsMenu().getTaxView());
            GameStore.getInstance().getLayoutView().positionTocCenter(GameStore.getInstance().getLayoutView().getOptionsMenu().getTaxView());

        } else if (keyCode == 188) {//handle < key
            GameStore.getInstance().getLayoutView().getMap().goToPreviousArmy();

        } else if (keyCode == 190) {//handle > key
            GameStore.getInstance().getLayoutView().getMap().goToNextArmy();

        } else if (keyCode == (int) 'M' || keyCode == (int) 'm') {//go to next barrack
            GameStore.getInstance().getLayoutView().getMap().goToNextBarrack();

        } else if (keyCode == (int) 'N' || keyCode == (int) 'n') {//go to prev barrack
            GameStore.getInstance().getLayoutView().getMap().goToPreviousBarrack();

        } else if (keyCode == 38) {//handle arrow up
            if (GameStore.getInstance().getLayoutView().getgIterView().isOpened()) {
                GameStore.getInstance().getLayoutView().getgIterView().moveUp();
            } else {
                GameStore.getInstance().getLayoutView().getgIterView().show();
            }

        } else if (keyCode == 40) {//handle arrow down
            if (GameStore.getInstance().getLayoutView().getgIterView().isOpened()) {
                GameStore.getInstance().getLayoutView().getgIterView().moveDown();
            } else {
                GameStore.getInstance().getLayoutView().getgIterView().show();
            }

        } else if (keyCode == 37) {//handle arrow left
            if (GameStore.getInstance().getLayoutView().getgIterView().isOpened()) {
                GameStore.getInstance().getLayoutView().getgIterView().moveLeft();
            } else {
                GameStore.getInstance().getLayoutView().getgIterView().show();
            }

        } else if (keyCode == 39) {//handle arrow right
            if (GameStore.getInstance().getLayoutView().getgIterView().isOpened()) {
                GameStore.getInstance().getLayoutView().getgIterView().moveRight();
            } else {
                GameStore.getInstance().getLayoutView().getgIterView().show();
            }

        } else if (keyCode == 13) {//handle enter
            final GuiComponent component = GameStore.getInstance().getActiveComponent();
            if (component != null) {
                component.handleEnter();
            }

        } else if (keyCode == (int) 'L' || keyCode == (int) 'l') {//open newsletter
            final BattleFrame frame = new BattleFrame(0, false, false, true, null, false);
            GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(frame);
            GameStore.getInstance().getLayoutView().positionTocCenter(frame);

        } else if (keyCode == (int) 'V' || keyCode == (int) 'v') {
            GameStore.getInstance().getLayoutView().getSpeedTest().setVisible(!GameStore.getInstance().getLayoutView().getSpeedTest().isVisible());

        } else if (keyCode == (int) 'Z' || keyCode == (int) 'z') {
            MapStore.getInstance().zoom(false);

        } else if (keyCode == (int) 'X' || keyCode == (int) 'x') {
            MapStore.getInstance().zoom(true);

        } else if (keyCode == (int) 'B' || keyCode == (int) 'b') {//open selected unit info
            if (GameStore.getInstance().isUnitSelected()) {
                GameStore.getInstance().openSelectedUnitInfoPanels();
            }
        } else {
//            Window.alert("key pressed : " + keyCode);
        }

        tm.scheduleRepeating(1);
    }

    private void moveMap() {
        final DrawingAreaWC myself = this;
        int verSpace = 0;
        int horSpace = 0;
        if (DIRECTIONS[0]) {
            horSpace -= 4;
        }
        if (DIRECTIONS[1]) {
            horSpace += 4;
        }
        if (DIRECTIONS[2]) {
            verSpace += 4;
        }
        if (DIRECTIONS[3]) {
            verSpace -= 4;
        }

        mapScrollPanel.setHorizontalScrollPosition(mapScrollPanel.getHorizontalScrollPosition() + horSpace);
        mapScrollPanel.setVerticalScrollPosition(mapScrollPanel.getVerticalScrollPosition() + verSpace);
        final float miniX = miniMapPanel.getWidth() / (float) myself.getWidth();
        final float miniY = miniMapPanel.getHeight() / (float) myself.getHeight();
        final Rectangle rect = miniMapPanel.getRectangle();
        rect.setX((int) (mapScrollPanel.getHorizontalScrollPosition() * miniX));
        rect.setY((int) (mapScrollPanel.getVerticalScrollPosition() * miniY));

        mapsView.setLastKnownPosition(mapStore.getActiveRegion(),
                mapScrollPanel.getHorizontalScrollPosition(),
                mapScrollPanel.getVerticalScrollPosition(),
                rect.getX(), rect.getY());
    }

    /**
     * @param value the miniMapPanel to set
     */
    public void setMiniMapPanel(final MiniMapPanel value) {
        miniMapPanel = value;
    }

    public HandlerRegistration addKeyPressHandler(final KeyPressHandler handler) {
        return addDomHandler(handler, KeyPressEvent.getType());
    }

    public HandlerRegistration addKeyUpHandler(final KeyUpHandler handler) {
        return addDomHandler(handler, KeyUpEvent.getType());
    }

    public HandlerRegistration addKeyDownHandler(final KeyDownHandler handler) {
        return addDomHandler(handler, KeyDownEvent.getType());
    }

    public TileActionsPopup getTileActions() {
        return tileActions;
    }

}
