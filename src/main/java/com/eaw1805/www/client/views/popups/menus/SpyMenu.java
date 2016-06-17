package com.eaw1805.www.client.views.popups.menus;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.gui.GuiComponentSpyReports;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.SpyReportsView;
import com.eaw1805.www.client.views.military.deployment.DeployTroopsView;
import com.eaw1805.www.client.views.popups.SpyReportPanel;
import com.eaw1805.www.client.widgets.FigureItem;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.TransportStore;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import java.util.List;

public class SpyMenu extends UnitMenu implements ArmyConstants, StyleConstants {

    private final ImageButton allInfoImg;
    private final ImageButton reportsImg;
    private MapStore mapStore = MapStore.getInstance();
    private final Group inspectionTiles = new Group();
    private final SpyDTO spy;
    private GuiComponentSpyReports tilesComp;

    public SpyMenu(final SpyDTO spy, final PopupPanelEAW caller) {
        setPopupParent(caller);
        try {
            tilesComp = new GuiComponentSpyReports(inspectionTiles, spy);
        } catch (Exception e) {

        }
        this.spy = spy;
        setSize("238px", "137px");
        setStyleName("");

        final AbsolutePanel basePanel = new AbsolutePanel();
        add(basePanel, 86, 73);
        basePanel.setSize("64px", "64px");


        final FigureItem spyImage = new FigureItem("http://static.eaw1805.com/images/figures/"
                + spy.getNationId() + "/spy.png", 64, SPY, spy.getSpyId(), spy.getNationId(),
                RegionStore.getInstance().getSectorByPosition(spy).getId(), true, 0);
        basePanel.add(spyImage, 0, 0);

        if (TradeStore.getInstance().hasInitSecondPhase(SPY, spy.getSpyId())) {
            moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/moveNA.png");
            moveImg.setTitle("You have initiated the second phase. Cannot move.");
        } else {
            moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    mapStore.getMapsView().goToPosition(spy);
                    if (spy.getNationId() == GameStore.getInstance().getNationId()) {
                        mapStore.getMapsView().addFigureOnMap(SPY, spy.getSpyId(), spy, 80, 0, 0, 0);

                    } else {
                        MovementEventManager.startAllyMovement(SPY, spy.getSpyId(), spy.getNationId(),
                                RegionStore.getInstance().getSectorByPosition(spy));
                    }
                    caller.hide();
                }
            }).addToElement(moveImg.getElement()).register();
            moveImg.setTitle("Issue move orders.");
        }
        moveImg.setStyleName(CLASS_POINTER);
        moveImg.setSize(SIZE_36PX, SIZE_36PX);


        allInfoImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButSpyOpenReportsOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final SpyReportsView spyReportsView = new SpyReportsView(RegionStore.getInstance().getRegionSectorsByRegionId(spy.getRegionId())[spy.getXStart()][spy.getYStart()].getNationDTO());
                GameStore
                        .getInstance()
                        .getLayoutView()
                        .addWidgetToLayoutPanelEAW(spyReportsView);
                GameStore.getInstance().getLayoutView().positionTocCenter(spyReportsView);
                caller.hide();
            }
        }).addToElement(allInfoImg.getElement()).register();

        allInfoImg.setTitle("Show nation and tile reports.");
        allInfoImg.setStyleName(CLASS_POINTER);
        allInfoImg.setSize(SIZE_36PX, SIZE_36PX);

        reportsImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButSpyReportsMapOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                mapStore.getUnitGroups().toggleInspectionTiles(inspectionTiles, spy.getSpyId(), tilesComp);
                final PositionDTO position = new PositionDTO();
                position.setRegionId(spy.getRegionId());
                position.setX(spy.getXStart());
                position.setY(spy.getYStart());
                position.setXStart(spy.getXStart());
                position.setYStart(spy.getYStart());
                mapStore.getMapsView().goToPosition(position);
                caller.hide();
            }
        }).addToElement(reportsImg.getElement()).register();

        reportsImg.setTitle("Show report on map.");
        reportsImg.setStyleName(CLASS_POINTER);
        reportsImg.setSize(SIZE_36PX, SIZE_36PX);


        boardImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButEmbarkOff.png");
        boardImg.setTitle("Board.");
        boardImg.setStyleName(CLASS_POINTER);
        boardImg.setSize(SIZE_36PX, SIZE_36PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final PositionDTO pos = MovementStore.getInstance().getUnitPosition(SPY, spy.getSpyId(), spy);
                final boolean hasTransports = TransportStore.getInstance().hasTransportUnits(pos);
                if (hasTransports) {
                    final DeployTroopsView dpView = new DeployTroopsView(pos.getRegionId(), 0, 0);
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                    GameStore.getInstance().getLayoutView().positionTocCenter(dpView);
                    caller.hide();
                }
            }
        }).addToElement(boardImg.getElement()).register();

        initInspectionTiles();
        setupImages();
        finalizeMenu(190);
    }

    private void initInspectionTiles() {
        final int xPos = spy.getXStart();
        final int yPos = spy.getYStart();
        final MapStore mapStore = MapStore.getInstance();
        final double tileSize = mapStore.getTileSize();
        for (int offsetX = -1; offsetX < 2; offsetX++) {
            for (int offsetY = -1; offsetY < 2; offsetY++) {
                final Rectangle rect = new Rectangle((int) mapStore.getPointX(xPos + offsetX),
                        (int) mapStore.getPointY(yPos + offsetY),
                        (int) tileSize, (int) tileSize);

                boolean myTile;
                if (offsetX == 0 && offsetY == 0) {
                    rect.setFillColor("green");
                    myTile = true;

                } else {
                    rect.setFillColor("yellow");
                    myTile = false;
                }

                rect.setFillOpacity(0.5);
                //rect.setStyleName(CLASS_POINTER, true);
                final PopupPanelEAW overViewPanel = new PopupPanelEAW();
                final SpyReportPanel repoPanel = new SpyReportPanel(spy, myTile);
                overViewPanel.add(repoPanel);
                rect.addMouseMoveHandler(new MouseMoveHandler() {
                    public void onMouseMove(final MouseMoveEvent event) {
                        int xPos, yPos;
                        if (event.getClientX() - 250 <= 0) {
                            xPos = event.getClientX() + 20;

                        } else {
                            xPos = event.getClientX() - (245 + 20);
                        }

                        if (event.getClientY() - 400 <= 0) {
                            yPos = event.getClientY() + 20;

                        } else {
                            yPos = event.getClientY() - (75 + 20);
                        }
                        overViewPanel.setPopupPosition(xPos, yPos);
                        overViewPanel.show();
                    }
                });

                rect.addMouseOutHandler(new MouseOutHandler() {
                    public void onMouseOut(final MouseOutEvent event) {
                        overViewPanel.hide();
                    }
                });

                rect.addMouseDownHandler(new MouseDownHandler() {
                    public void onMouseDown(final MouseDownEvent event) {
                        overViewPanel.hide();
                    }
                });

                inspectionTiles.add(rect);
            }
        }
    }


    private final ImageButton moveImg;
    private final ImageButton boardImg;

    public void clearImages() {
        try {
            remove(moveImg);
        } catch (Exception ignored) {
            //do nothing here
        }
        try {
            remove(allInfoImg);
        } catch (Exception ignored) {
            //do nothing here
        }
        try {
            remove(reportsImg);
        } catch (Exception ignored) {
            //do nothing here
        }
        try {
            remove(boardImg);
        } catch (Exception ignored) {
            //do nothing here
        }
    }


    public final void setupImages() {
        try {
            clearImages();
        } catch (Exception e) {
            //do nothing here
        }

        if (GameStore.getInstance().isNationDead() || GameStore.getInstance().isGameEnded()) {
            return;
        }

        if (!spy.getLoaded()) {
            if (GameStore.getInstance().getNationId() == spy.getNationId()) {
                addImageButton(moveImg);
            }

            addImageButton(allInfoImg);
            addImageButton(reportsImg);
            if (GameStore.getInstance().getNationId() == spy.getNationId()) {
                final SectorDTO sector = MapStore.getInstance()
                        .getRegionSectorByRegionIdXY(spy.getRegionId(),
                                spy.getX(),
                                spy.getY());

                final List<TransportUnitDTO> transports =
                        TransportStore.getInstance().getAllTransportUnitsBySector(sector);

                if (!transports.isEmpty()) {
                    addImageButton(boardImg);
                }
            }
        }
    }


}
