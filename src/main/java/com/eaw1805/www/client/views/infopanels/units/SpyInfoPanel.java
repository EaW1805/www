package com.eaw1805.www.client.views.infopanels.units;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.client.events.deploy.DeployEventManager;
import com.eaw1805.www.client.events.deploy.DisembarkUnitEvent;
import com.eaw1805.www.client.events.deploy.DisembarkUnitHandler;
import com.eaw1805.www.client.events.deploy.EmbarkUnitEvent;
import com.eaw1805.www.client.events.deploy.EmbarkUnitHandler;
import com.eaw1805.www.client.events.loading.CommLoadedEvent;
import com.eaw1805.www.client.events.loading.CommLoadedHandler;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.loading.SpiesLoadedEvent;
import com.eaw1805.www.client.events.loading.SpiesLoadedHandler;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.GuiComponentSpyReports;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.SpyReportsView;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.views.military.deployment.DeployTroopsView;
import com.eaw1805.www.client.views.popups.SpyReportPanel;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.client.widgets.SelectableWidget;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.TransportStore;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpyInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants, SelectableWidget<SpyDTO>, StyleConstants {

    private transient final MapStore mapStore = MapStore.getInstance();

    private transient final SpyDTO spy;
    private transient ImageButton viewImg;
    private transient ImageButton reportsImg;
    private transient final Group inspectionTiles = new Group();

    private transient Image loadedImg;
    private transient ImageButton repOnMapImg;

    private transient GuiComponentSpyReports tilesComp;

    private transient final ClickAbsolutePanel spyPanel;

    private transient final Map<String, Integer> onSectorToBrigs = new HashMap<String, Integer>();
    private transient final Map<String, Integer> onSectorToShips = new HashMap<String, Integer>();
    private transient final Set<String> onSectorNations = new HashSet<String>();

    private transient int surTotalBrigades = 0;
    private transient int surTotalShips = 0;
    private final RenamingLabel lblSpyName;

    public SpyInfoPanel(final SpyDTO thisSpy) {

        final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
        spy = thisSpy;

        spyPanel = new ClickAbsolutePanel();
        spyPanel.setStyleName("spyInfoPanel");
        spyPanel.setSize("366px", "90px");
        this.add(spyPanel);

        final Image spyImg = new Image("http://static.eaw1805.com/images/buttons/icons/spy.png");
        spyPanel.add(spyImg, 3, 3);
        spyImg.setSize("", "82px");

        initSpyReportVars();

        lblSpyName = new RenamingLabel(spy.getName(), SPY, spy.getSpyId());
        lblSpyName.setStyleName("clearFontMiniTitle");
        lblSpyName.setSize("155px", SIZE_20PX);
        spyPanel.add(lblSpyName, 90, 3);

        final Label lblOnSector = new Label("On sector");
        lblOnSector.setStyleName(CLASS_CLEARFONTSMALL);
        spyPanel.add(lblOnSector, 90, 20);

        final int posY = 34;
        int posX = 90;
        if (onSectorNations.isEmpty()) {
            final Label onSectorEmpty = new Label("No reports");
            onSectorEmpty.setStyleName(CLASS_CLEARFONTSMALL);
            spyPanel.add(onSectorEmpty, 90, posY);

        } else {
            for (final String nation : onSectorNations) {
                // Count number of battalions
                final int battalions;
                if (onSectorToBrigs.containsKey(nation)) {
                    battalions = onSectorToBrigs.get(nation);

                } else {
                    battalions = 0;
                }

                // Count number of ships
                final int ships;
                if (onSectorToShips.containsKey(nation)) {
                    ships = onSectorToShips.get(nation);
                } else {
                    ships = 0;
                }

                // Retrieve nation Id
                final int nationId = DataStore.getInstance().getNationByName(nation).getNationId();

                final Image empireFlag = new Image("http://static.eaw1805.com/images/nations/nation-" + nationId + "-36.png");
                empireFlag.setSize("", SIZE_15PX);
                spyPanel.add(empireFlag, posX + 1, posY);

                final Image batImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/battalion.png");
                batImg.setTitle("Battalions");
                batImg.setSize("", SIZE_15PX);
                spyPanel.add(batImg, posX, posY + 17);

                final Label lblBats = new Label(numberFormat.format(battalions));
                lblBats.setStyleName(CLASS_CLEARFONTSMALL);
                spyPanel.add(lblBats, posX + 27, posY + 17);

                final Image shipsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png");
                shipsImg.setTitle("Ships");
                shipsImg.setSize("", SIZE_15PX);
                spyPanel.add(shipsImg, posX, posY + 34);

                final Label lblNationReport = new Label(numberFormat.format(ships));
                lblNationReport.setStyleName(CLASS_CLEARFONTSMALL);
                spyPanel.add(lblNationReport, posX + 27, posY + 34);
                posX += 40;
            }
        }

        final Label lblSurround = new Label("Surrounding");
        lblSurround.setStyleName(CLASS_CLEARFONTSMALL);
        spyPanel.add(lblSurround, 208, 20);

        if (surTotalBrigades > 0 || surTotalShips > 0) {
            final Image brigsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/brigade.png");
            brigsImg.setTitle("Brigades");
            brigsImg.setSize("", SIZE_15PX);
            spyPanel.add(brigsImg, 210, 34);

            final Label brigsCountLabel = new Label(numberFormat.format(surTotalBrigades));
            brigsCountLabel.setStyleName(CLASS_CLEARFONTSMALL);
            spyPanel.add(brigsCountLabel, 238, 34);

            final Image shipsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png");
            shipsImg.setTitle("Ships");
            shipsImg.setSize("", SIZE_15PX);
            spyPanel.add(shipsImg, 210, 53);

            final Label shipsCountLabel = new Label(numberFormat.format(surTotalShips));
            shipsCountLabel.setStyleName(CLASS_CLEARFONTSMALL);
            spyPanel.add(shipsCountLabel, 238, 53);

        } else {
            final Label onSurEmpty = new Label("No reports");
            onSurEmpty.setStyleName(CLASS_CLEARFONTSMALL);
            spyPanel.add(onSurEmpty, 210, 34);
        }

        final Label lblLocation = new Label(spy.positionToString());
        lblLocation.setTitle("Spy position.");
        lblLocation.setStyleName(CLASS_CLEARFONTSMALL);
        lblLocation.setSize("47px", SIZE_15PX);
        spyPanel.add(lblLocation, 315, 3);

        final Label lbMovementPoints = new Label(spy.getSpyMP() + " MPs");
        if (GameStore.getInstance().getNationId() == spy.getNationId()) {
            lbMovementPoints.setTitle("Movement points.");
            lbMovementPoints.setStyleName(CLASS_CLEARFONTSMALL);
            spyPanel.add(lbMovementPoints, 315, 20);
            lbMovementPoints.setSize("47px", SIZE_15PX);
        }

        //action buttons
        if (GameStore.getInstance().getNationId() == spy.getNationId()) {
            viewImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff.png");
            viewImg.setTitle("Go to spy position");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(MouseEvent event) {
                    mapStore.getMapsView().goToPosition(spy);
                    viewImg.deselect();
                }
            }).addToElement(viewImg.getElement()).register();

            viewImg.setStyleName("pointer", true);
            spyPanel.add(viewImg, 338, 63);
            viewImg.setSize(SIZE_20PX, SIZE_20PX);

            if (!GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
                final ImageButton moveImage = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        mapStore.getMapsView().goToPosition(spy);
                        mapStore.getMapsView().addFigureOnMap(SPY, spy.getSpyId(), spy, 80, 0, 0, 0);
                        moveImage.deselect();
                    }
                }).addToElement(moveImage.getElement()).register();

                moveImage.setTitle("Click here to move the spy");
                spyPanel.add(moveImage, 315, 63);
                moveImage.setSize(SIZE_20PX, SIZE_20PX);


                repOnMapImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButSpyReportsMapOff.png");
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        mapStore.getUnitGroups().toggleInspectionTiles(inspectionTiles, spy.getSpyId(), tilesComp);
                        final PositionDTO position = new PositionDTO();
                        position.setRegionId(spy.getRegionId());
                        position.setX(spy.getXStart());
                        position.setY(spy.getYStart());
                        position.setXStart(spy.getXStart());
                        position.setYStart(spy.getYStart());
                        mapStore.getMapsView().goToPosition(position);
                        repOnMapImg.deselect();
                    }
                }).addToElement(repOnMapImg.getElement()).register();

                repOnMapImg.setTitle("Show reports on map");
                spyPanel.add(repOnMapImg, 292, 63);
                repOnMapImg.setSize(SIZE_20PX, SIZE_20PX);

                reportsImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButSpyOpenReportsOff.png");
                reportsImg.setTitle("Open report overview");
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        final SpyReportsView spyReportsView = new SpyReportsView(RegionStore.getInstance().getRegionSectorsByRegionId(spy.getRegionId())[spy.getXStart()][spy.getYStart()].getNationDTO());
                        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(spyReportsView);
                        GameStore.getInstance().getLayoutView()
                                .setWidgetPosition(spyReportsView,
                                        (Document.get().getClientWidth() - spyReportsView.getOffsetWidth()) / 2,
                                        (Document.get().getClientHeight() - spyReportsView.getOffsetHeight()) / 2, false, true);

                        reportsImg.deselect();
                    }
                }).addToElement(reportsImg.getElement()).register();

                this.setStyleName("pointer", true);
                spyPanel.add(reportsImg, 269, 63);
                reportsImg.setSize(SIZE_20PX, SIZE_20PX);


                final Image boardImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButEmbarkOff.png");
                boardImg.setTitle("Board");
                boardImg.setSize(SIZE_20PX, SIZE_20PX);
                boardImg.setStyleName("pointer");
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        final PositionDTO pos = MovementStore.getInstance().getUnitPosition(SPY, spy.getSpyId(), spy);
                        final boolean hasTransports = TransportStore.getInstance().hasTransportUnits(pos);
                        if (hasTransports) {
                            final DeployTroopsView dpView = new DeployTroopsView(pos.getRegionId(), 0, 0);
                            GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                            GameStore.getInstance().getLayoutView().positionTocCenter(dpView);
                        }
                    }
                }).addToElement(boardImg.getElement()).register();


                loadedImg = new Image("http://static.eaw1805.com/images/buttons/icons/transport.png");
                loadedImg.setTitle("This item is loaded on a transport vehicle");
                loadedImg.setSize(SIZE_20PX, SIZE_20PX);
                setLoadedImgStatus();
                spyPanel.add(loadedImg, 339, 34);
                spyPanel.add(boardImg, 246, 63);
            }
        } else {
            final HorizontalPanel relationPanel = new HorizontalPanel() {
                protected void onAttach() {
                    //the easiest way to reposition this...
                    spyPanel.setWidgetPosition(this, 366 - 3 - this.getOffsetWidth(), 90 - 3 - this.getOffsetHeight());
                }
            };
            relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);

            final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + spy.getNationId() + "-36.png");
            final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(spy.getNationId()) + " - ");
            relationStatus.setStyleName("clearFont");
            relationPanel.add(relationStatus);
            relationPanel.add(flag);
            spyPanel.add(relationPanel, 246, 63);
        }

        DeployEventManager.addEmbarkUnitHandler(new EmbarkUnitHandler() {
            public void onEmbarkUnit(final EmbarkUnitEvent event) {
                setLoadedImgStatus();
            }
        });

        DeployEventManager.addDisembarkUnitHandler(new DisembarkUnitHandler() {
            public void onDisembarkUnit(final DisembarkUnitEvent event) {
                setLoadedImgStatus();
            }
        });

        initInspectionTiles();
        try {
            tilesComp = new GuiComponentSpyReports(inspectionTiles, spy);
        } catch (Exception e) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to create spy gui component", false);
        }

        LoadEventManager.addCommLoadeddHandler(new CommLoadedHandler() {
            public void onCommLoaded(final CommLoadedEvent event) {
                try {
                    if (tilesComp != null) {
                        tilesComp = new GuiComponentSpyReports(inspectionTiles, spy);
                    }
                } catch (Exception e) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to create spy gui component 2", false);
                }
            }
        });

        LoadEventManager.addSpiesLoadedHandler(new SpiesLoadedHandler() {
            public void onSpiesLoaded(final SpiesLoadedEvent event) {
                try {
                    if (tilesComp != null) {
                        tilesComp = new GuiComponentSpyReports(inspectionTiles, spy);
                    }
                } catch (Exception e) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to create spy gui component 3", false);
                }
            }
        });
        UnitEventManager.addUnitChangedHandler(new UnitChangedHandler() {
            @Override
            public void onUnitChanged(UnitChangedEvent event) {
                if (event.getInfoType() == SPY && event.getInfoId() == spy.getSpyId()) {
                    updateSpyName();
                }
            }
        });
    }

    private void updateSpyName() {
        lblSpyName.setText(spy.getName());
    }

    private void setLoadedImgStatus() {
        this.loadedImg.setVisible(spy.getLoaded());
    }

    private void initInspectionTiles() {
        final int xPos = spy.getXStart();
        final int yPos = spy.getYStart();
        final MapStore mapStore = MapStore.getInstance();
        final int tileSize = (int) mapStore.getTileSize();
        for (int index = -1; index < 2; index++) {
            for (int j = -1; j < 2; j++) {

                final Rectangle rect = new Rectangle(mapStore.getPointX(xPos + index),
                        mapStore.getPointY(yPos + j),
                        tileSize, tileSize);

                boolean myTile;
                if (index == 0 && j == 0) {
                    rect.setFillColor("green");
                    myTile = true;

                } else {
                    rect.setFillColor("yellow");
                    myTile = false;
                }
                rect.setFillOpacity(0.5);

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

    private void initSpyReportVars() {
        final String battalionReports = spy.getReportBattalions();
        final String brigadeReports = spy.getReportBrigades();
        final String shipReports = spy.getReportShips();
        final String nearShipReport = spy.getReportNearbyShips();

        //retrieve spy sector reports
        if (battalionReports != null && !battalionReports.isEmpty()) {
            final String battRep[] = battalionReports.split("\\|");
            for (final String aBattRep : battRep) {
                final String[] items = aBattRep.split(":");
                if (!items[0].equals("") && !items[1].equals("")) {
                    onSectorToBrigs.put(items[0], Integer.parseInt(items[1]));
                    onSectorNations.add(items[0]);
                }
            }
        }

        if (shipReports != null && !shipReports.isEmpty()) {
            final String shipRep[] = shipReports.split("\\|");
            for (final String aShipRep : shipRep) {
                final String[] items = aShipRep.split(":");
                if (!items[0].equals("") && !items[1].equals("")) {
                    int shipsReported = Integer.parseInt(items[1].substring(2));
                    if (items.length == 3 && !items[2].equals("")) {
                        shipsReported += Integer.parseInt(items[2].substring(2));
                    }
                    onSectorToShips.put(items[0], shipsReported);
                    onSectorNations.add(items[0]);
                }
            }
        }

        //retrieve surrounding sector report.
        if (brigadeReports != null && !brigadeReports.isEmpty()) {
            final String brigRep[] = brigadeReports.split("\\|");
            for (final String aBrigRep : brigRep) {
                final String[] items = aBrigRep.split(":");
                if (!items[0].equals("") && !items[1].equals("")) {
                    surTotalBrigades = surTotalBrigades + Integer.parseInt(items[1]);
                }
            }
        }

        if (nearShipReport != null && !nearShipReport.isEmpty()) {
            final String nearShipRep[] = nearShipReport.split("\\|");
            for (final String aNearShipRep : nearShipRep) {
                final String[] items = aNearShipRep.split(":");
                if (!items[0].equals("") && !items[1].equals("")) {
                    surTotalShips = surTotalShips + Integer.parseInt(items[1]);
                    if (items.length == 3 && !items[2].equals("")) {
                        surTotalShips = surTotalShips + Integer.parseInt(items[2]);
                    }
                }
            }
        }

    }

    @Override
    public SpyDTO getValue() {
        return spy;
    }

    @Override
    public int getIdentifier() {
        return SPY;
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public void setSelected(final boolean selected) {
        if (selected) {
            spyPanel.setStyleName("infoPanelSelected");

        } else {
            spyPanel.setStyleName("spyInfoPanel");
        }
    }

    @Override
    public void onEnter() {
        // do nothing
    }
}
