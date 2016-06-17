package com.eaw1805.www.scenario.stores;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.constants.TerrainConstants;
import com.eaw1805.data.dto.common.JumpOffDTO;
import com.eaw1805.data.dto.common.ProductionSiteDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.common.TerrainDTO;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.data.dto.web.army.*;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.scenario.algorithms.AStarNode;
import com.eaw1805.www.scenario.algorithms.SectorNode;
import com.eaw1805.www.scenario.algorithms.SupplyClusterAlgorithm;
import com.eaw1805.www.scenario.stores.map.TileGroup;
import com.eaw1805.www.scenario.views.EditorPanel;
import com.eaw1805.www.scenario.views.SectorBrigadesPanel;
import com.eaw1805.www.scenario.views.infopanels.*;
import com.eaw1805.www.scenario.widgets.DrawingAreaSE;
import com.eaw1805.www.scenario.widgets.popups.UnitViewerPopup;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import java.util.*;

public class EditorMapUtils implements TerrainConstants {

    public static int SIDE_OFFSET = 500;
    public static int TILE_WIDTH = 64;
    public static int TILE_HEIGHT = 64;


    TileGroup[][] tileSectors;

    final Group terrainGroup = new Group();
    final Group prSiteGroup = new Group();
    final Group nationGroup = new Group();
    final Group polSphereGroup = new Group();
    final Group resourceGroup = new Group();
    final Group populationGroup = new Group();
    final Group climateGroup = new Group();
    final Group armiesGroup = new Group();
    final Group commandersGroup = new Group();
    final Group spiesGroup = new Group();
    final Group bTrainGroup = new Group();
    final Group shipGroup = new Group();
    final Group gridGroup = new Group();
    final Group jumpGroup = new Group();
    final Group supplyGroup = new Group();

    private static EditorMapUtils instance;

    private EditorMapUtils() {}

    public static EditorMapUtils getInstance() {
        if (instance == null) {
            instance = new EditorMapUtils();
        }
        return instance;
    }

    public void clearArea() {
        final DrawingAreaSE drawingArea = EditorPanel.getInstance().getDrawingArea();
        drawingArea.clear();
        terrainGroup.clear();
        prSiteGroup.clear();
        nationGroup.clear();
        polSphereGroup.clear();
        resourceGroup.clear();
        populationGroup.clear();
        climateGroup.clear();
        armiesGroup.clear();
        commandersGroup.clear();
        spiesGroup.clear();
        bTrainGroup.clear();
        shipGroup.clear();
        gridGroup.clear();
        jumpGroup.clear();
        supplyGroup.clear();
    }

    public void drawRegion(RegionDTO region, final List<SectorDTO> sectors) {
        tileSectors = new TileGroup[RegionSettings.sizeX][RegionSettings.sizeY];
        final DrawingAreaSE drawingArea = EditorPanel.getInstance().getDrawingArea();
        clearArea();
        drawingArea.setSize((RegionSettings.sizeX + 2 * SIDE_OFFSET) * TILE_WIDTH + "px", (RegionSettings.sizeY + 2 * SIDE_OFFSET) * TILE_HEIGHT + "px");
        //first parse. add terrains
        drawingArea.add(terrainGroup);
        drawingArea.add(gridGroup);
        drawingArea.add(jumpGroup);

        drawingArea.add(prSiteGroup);
        drawingArea.add(nationGroup);
        drawingArea.add(polSphereGroup);
        drawingArea.add(resourceGroup);
        drawingArea.add(populationGroup);
        drawingArea.add(climateGroup);
        drawingArea.add(supplyGroup);
        drawingArea.add(armiesGroup);
        drawingArea.add(commandersGroup);
        drawingArea.add(spiesGroup);
        drawingArea.add(bTrainGroup);
        drawingArea.add(shipGroup);
        for (int indexX = 0; indexX < RegionSettings.sizeX; indexX++) {
            gridGroup.add(new Line(getPointX(indexX), getPointY(0), getPointX(indexX), getPointY(RegionSettings.sizeY)));
        }
        for (int indexY = 0; indexY < RegionSettings.sizeY; indexY++) {
            gridGroup.add(new Line(getPointX(0), getPointY(indexY), getPointX(RegionSettings.sizeX), getPointY(indexY)));
        }
        for (final SectorDTO sector : sectors) {
            tileSectors[sector.getX()][sector.getY()] = new TileGroup(sector, terrainGroup, prSiteGroup, nationGroup, resourceGroup, polSphereGroup, populationGroup, climateGroup);
            tileSectors[sector.getX()][sector.getY()].draw();
        }
        drawArmies(region.getRegionId());
        drawCommanders(region.getRegionId());
        drawSpies(region.getRegionId());
        drawBTrains(region.getRegionId());
        drawShips(region.getRegionId());
        drawJumps(region.getRegionId());
        EditorPanel.getInstance().getDrawingArea().moveMap(RegionSettings.sizeX/2, RegionSettings.sizeY/2);
    }

    public void drawSupply(final int nationId) {
        supplyGroup.clear();
        final Set<String> supplySources = new HashSet<String>();
        final List<SectorDTO> sectorsWithBarracks = new ArrayList<SectorDTO>();
        final AStarNode<SectorDTO>[][] data = new AStarNode[RegionSettings.sizeX][RegionSettings.sizeY];
        for (final SectorDTO sector : RegionSettings.sectors) {
            data[sector.getX()][sector.getY()] = new SectorNode(sector, null);
            if (sector.getNationDTO().getNationId() == nationId
                    && EditorStore.getInstance().getNationByCode(sector.getPoliticalSphere()) != null
                    && EditorStore.getInstance().getNationByCode(sector.getPoliticalSphere()).getNationId() == nationId
                    && sector.getProductionSiteDTO() != null
                    && sector.getProductionSiteDTO().getId() >= ProductionSiteConstants.PS_BARRACKS) {
                sectorsWithBarracks.add(sector);
                supplySources.add(sector.positionToString());
            } else if (sector.getTradeCity()
                    && sector.getNationDTO().getNationId() == nationId) {
                sectorsWithBarracks.add(sector);
                supplySources.add(sector.positionToString());
            }
        }
        findSupplyLines(nationId, sectorsWithBarracks, data, supplySources);
    }

    public void findSupplyLines(final int nationId, List<SectorDTO> sectors, final AStarNode<SectorDTO>[][] data, final Set<String> positionSupplies) {

        final List<SectorDTO> nextStepSources = new ArrayList<SectorDTO>();
        //first identify supply sources.
        for (final SectorDTO sector : sectors) {
            final Rectangle source = new Rectangle(getPointX(sector.getX()), getPointY(sector.getY()), TILE_WIDTH, TILE_HEIGHT);
            source.setFillOpacity(0.5);
            source.setFillColor("green");
            source.setStrokeColor("green");
            supplyGroup.add(source);
            new SupplyClusterAlgorithm<SectorDTO>(data, new SectorNode(sector, null)) {
                @Override
                public void executeResult(final AStarNode<SectorDTO> out) {
                    String extra = "-";
                    if (out.getObject().getNationDTO().getNationId() == nationId
                            && out.getObject().getProductionSiteDTO() != null
                            && out.getObject().getProductionSiteDTO().getId() >= ProductionSiteConstants.PS_BARRACKS
                            && !positionSupplies.contains(out.getObject().positionToString())) {
                        extra = "*";
                        nextStepSources.add(out.getObject());

                    } else if (out.getObject().getTradeCity()
                            && out.getObject().getNationDTO().getNationId() == nationId
                            && !positionSupplies.contains(out.getObject().positionToString())) {
                        extra = "!";
                        nextStepSources.add(out.getObject());

                    }

                    if (!positionSupplies.contains(out.getObject().positionToString())) {
                        final Group lineGroup = new Group();
                        AStarNode<SectorDTO> node = out;
                        while (node.getParent() != null) {
                            final Line curLine = new Line(getPointX(node.getParent().getObject().getX()) + TILE_WIDTH/2,
                                    getPointY(node.getParent().getObject().getY()) + TILE_HEIGHT/2,
                                    getPointX(node.getObject().getX()) + TILE_WIDTH/2,
                                    getPointY(node.getObject().getY()) + TILE_HEIGHT/2);
                            curLine.setStrokeColor("yellow");
                            lineGroup.add(curLine);
                            node = node.getParent();
                        }

                        final Rectangle supply = new Rectangle(getPointX(out.getObject().getX()), getPointY(out.getObject().getY()), TILE_WIDTH, TILE_HEIGHT);
                        EditorPanel.getInstance().setDebugMessage(openSet.size() + " - " + closeSet.size() + " - " + sector.positionToString() + " adding supply for : " + out.getObject().positionToString() + " - " + out.getTotalWeight() + " : " + extra);
                        supply.setFillOpacity(0.5);
                        supply.setFillColor("blue");
                        supply.setStrokeColor("blue");
                        supplyGroup.add(supply);
                        supply.addMouseOverHandler(new MouseOverHandler() {
                            @Override
                            public void onMouseOver(MouseOverEvent mouseOverEvent) {
                                supplyGroup.add(lineGroup);
                            }
                        });
                        supply.addMouseOutHandler(new MouseOutHandler() {
                            @Override
                            public void onMouseOut(MouseOutEvent mouseOutEvent) {
                                if (lineGroup.isAttached()) {
                                    supplyGroup.remove(lineGroup);
                                }
                            }
                        });
                        if (out.getParent() != null) {
                            supplyGroup.add(new Line(getPointX(out.getParent().getObject().getX()) + TILE_WIDTH/2,
                                    getPointY(out.getParent().getObject().getY()) + TILE_HEIGHT/2,
                                    getPointX(out.getObject().getX()) + TILE_WIDTH/2,
                                    getPointY(out.getObject().getY()) + TILE_HEIGHT/2));
                            Text weightText = new Text(getPointX(out.getObject().getX()) + (TILE_WIDTH/2) -5, getPointY(out.getObject().getY()) + (TILE_HEIGHT/2) + 10, String.valueOf(out.getTotalWeight()) + extra);
                            weightText.setFillColor("yellow");
                            weightText.setStrokeColor("yellow");
                            weightText.setFontSize(9);
                            supplyGroup.add(weightText);
                        }
                    }
                    positionSupplies.add(out.getObject().positionToString());
                }

                @Override
                public void onFinish() {
                    if (!nextStepSources.isEmpty()) {
                        findSupplyLines(nationId, nextStepSources, data, positionSupplies);
                    }
                }
            }.calculate();
        }
    }

    public void drawJumps(final int regionId) {
        jumpGroup.clear();
        for (JumpOffDTO jump : EditorStore.getInstance().getJumpOffPoints()) {
            if (jump.getDepartureRegion() == regionId) {
                final Rectangle outgoing = new Rectangle(getPointX(jump.getDepartureX()), getPointY(jump.getDepartureY()), TILE_WIDTH, TILE_HEIGHT);
                outgoing.setFillOpacity(0.2);
                outgoing.setFillColor("green");
                outgoing.setStrokeColor("yellow");
                jumpGroup.add(outgoing);
            } else if (jump.getDestinationRegion() == regionId) {
                final Rectangle incoming = new Rectangle(getPointX(jump.getDepartureX()), getPointY(jump.getDepartureY()), TILE_WIDTH, TILE_HEIGHT);
                incoming.setFillOpacity(0.2);
                incoming.setFillColor("yellow");
                incoming.setStrokeColor("green");
                jumpGroup.add(incoming);
            }
        }
    }

    public void drawArmies(final int regionId) {
        armiesGroup.clear();
        for (Map.Entry<Integer, Map<Integer, Map<Integer, BrigadeDTO>>> entry : EditorStore.getInstance().getBrigades().get(regionId).entrySet()) {
            for (Map.Entry<Integer, Map<Integer, BrigadeDTO>> entry2 : entry.getValue().entrySet()) {
                for (BrigadeDTO brigade : entry2.getValue().values()) {
                    drawBrigade(brigade);
                }
            }
        }
    }

    public void drawCommanders(final int regionId) {
        commandersGroup.clear();
        for (Map.Entry<Integer, Map<Integer, Map<Integer, CommanderDTO>>> entry : EditorStore.getInstance().getCommanders().get(regionId).entrySet()) {
            for (Map.Entry<Integer, Map<Integer, CommanderDTO>> entry2 : entry.getValue().entrySet()) {
                for (CommanderDTO commander : entry2.getValue().values()) {
                    drawCommander(commander);
                }
            }
        }
    }

    public void drawSpies(final int regionId) {
        spiesGroup.clear();
        for (Map.Entry<Integer, Map<Integer, Map<Integer, SpyDTO>>> entry : EditorStore.getInstance().getSpies().get(regionId).entrySet()) {
            for (Map.Entry<Integer, Map<Integer, SpyDTO>> entry2 : entry.getValue().entrySet()) {
                for (SpyDTO spy : entry2.getValue().values()) {
                    drawSpy(spy);
                }
            }
        }
    }

    public void drawBTrains(final int regionId) {
        bTrainGroup.clear();
        for (Map.Entry<Integer, Map<Integer, Map<Integer, BaggageTrainDTO>>> entry : EditorStore.getInstance().getBaggageTrains().get(regionId).entrySet()) {
            for (Map.Entry<Integer, Map<Integer, BaggageTrainDTO>> entry2 : entry.getValue().entrySet()) {
                for (BaggageTrainDTO bTrain : entry2.getValue().values()) {
                    drawBTrain(bTrain);
                }
            }
        }
    }

    public void drawShips(final int regionId) {
        shipGroup.clear();
        for (Map.Entry<Integer, Map<Integer, Map<Integer, ShipDTO>>> entry : EditorStore.getInstance().getShips().get(regionId).entrySet()) {
            for (Map.Entry<Integer, Map<Integer, ShipDTO>> entry2 : entry.getValue().entrySet()) {
                for (ShipDTO ship : entry2.getValue().values()) {
                    drawShip(ship);
                }
            }
        }
    }

    public void drawBrigade(final BrigadeDTO brigade) {
        final Image img = new Image(getPointX(brigade.getX()),getPointY(brigade.getY()),20,20,"http://static.eaw1805.com/images/figures/" + brigade.getNationId() + "/UnitMap00.png");
        img.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent mouseOverEvent) {
                img.setWidth(30);
                img.setHeight(30);
            }
        });
        img.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent mouseOutEvent) {
                img.setWidth(20);
                img.setHeight(20);
            }
        });
        img.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent mouseDownEvent) {
                mouseDownEvent.preventDefault();
                mouseDownEvent.stopPropagation();
            }
        });
        img.addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent mouseUpEvent) {
                mouseUpEvent.preventDefault();
                mouseUpEvent.stopPropagation();
            }
        });
        img.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                clickEvent.preventDefault();
                clickEvent.stopPropagation();
                try {
                    List<Object> brigsCorpsList = new ArrayList<Object>();

                    if (EditorStore.getInstance().getArmies().get(brigade.getRegionId()).containsKey(brigade.getX())
                            && EditorStore.getInstance().getArmies().get(brigade.getRegionId()).get(brigade.getX()).containsKey(brigade.getY())) {
                        brigsCorpsList.addAll(EditorStore.getInstance().getArmies().get(brigade.getRegionId()).get(brigade.getX()).get(brigade.getY()).values());
                    }
                    if (EditorStore.getInstance().getCorps().get(brigade.getRegionId()).containsKey(brigade.getX())
                            && EditorStore.getInstance().getCorps().get(brigade.getRegionId()).get(brigade.getX()).containsKey(brigade.getY())) {
                        brigsCorpsList.addAll(EditorStore.getInstance().getCorps().get(brigade.getRegionId()).get(brigade.getX()).get(brigade.getY()).values());
                    }
                    brigsCorpsList.addAll(EditorStore.getInstance().getBrigades().get(brigade.getRegionId()).get(brigade.getX()).get(brigade.getY()).values());
                    Iterator<Object> iter = brigsCorpsList.iterator();
                    while (iter.hasNext()) {
                        Object unit = iter.next();
                        if (unit instanceof BrigadeDTO) {
                            if (((BrigadeDTO) unit).getCorpId() != 0) {
                                iter.remove();
                            }
                        } else if (unit instanceof CorpDTO) {
                            if (((CorpDTO) unit).getArmyId() != 0) {
                                iter.remove();
                            }
                        }
                    }

                    new UnitViewerPopup<Object>(brigsCorpsList) {

                        @Override
                        public Widget getUnitWidget(Object unit) {
                            if (unit instanceof ArmyDTO) {
                                return new ArmyInfoPanel((ArmyDTO) unit);
                            } else if (unit instanceof CorpDTO) {
                                return new CorpsInfoPanel((CorpDTO) unit);
                            } else if (unit instanceof BrigadeDTO) {
                                return new BrigadeInfoPanel((BrigadeDTO) unit);
                            } else {
                                return new Label();
                            }

                        }
                    }.show();
//                new SectorBrigadesPanel(brigade.getRegionId(), brigade.getX(), brigade.getY()).show();
                } catch (Exception e) {
                    Window.alert("Failed ? " + e.toString());
                }
            }
        });
        armiesGroup.add(img);
    }

    public void drawCommander(final CommanderDTO commander) {
        final Image img = new Image(getPointX(commander.getX()),getPointY(commander.getY()) + 40,20,20,"http://static.eaw1805.com/images/figures/" + commander.getNationId() + "/commander.png");
        img.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent mouseOverEvent) {
                img.setWidth(30);
                img.setHeight(30);
            }
        });
        img.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent mouseOutEvent) {
                img.setWidth(20);
                img.setHeight(20);
            }
        });
        img.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent mouseDownEvent) {
                mouseDownEvent.preventDefault();
                mouseDownEvent.stopPropagation();
            }
        });
        img.addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent mouseUpEvent) {
                mouseUpEvent.preventDefault();
                mouseUpEvent.stopPropagation();
            }
        });
        img.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                clickEvent.preventDefault();
                clickEvent.stopPropagation();

                try {
                    new UnitViewerPopup<CommanderDTO>(EditorStore.getInstance().getCommanders().get(commander.getRegionId()).get(commander.getX()).get(commander.getY()).values()) {

                        @Override
                        public Widget getUnitWidget(CommanderDTO unit) {
                            return new CommanderInfoPanel(unit);  //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }.show();
//                new SectorBrigadesPanel(brigade.getRegionId(), brigade.getX(), brigade.getY()).show();
                } catch (Exception e) {
                    Window.alert("Failed ? " + e.toString());
                }

            }
        });
        commandersGroup.add(img);
    }

    public void drawSpy(final SpyDTO spy) {
        final Image img = new Image(getPointX(spy.getX()),getPointY(spy.getY()) + 20,20,20,"http://static.eaw1805.com/images/figures/" + spy.getNationId() + "/spy.png");
        img.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent mouseOverEvent) {
                img.setWidth(30);
                img.setHeight(30);
            }
        });
        img.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent mouseOutEvent) {
                img.setWidth(20);
                img.setHeight(20);
            }
        });
        img.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent mouseDownEvent) {
                mouseDownEvent.preventDefault();
                mouseDownEvent.stopPropagation();
            }
        });
        img.addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent mouseUpEvent) {
                mouseUpEvent.preventDefault();
                mouseUpEvent.stopPropagation();
            }
        });
        img.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                clickEvent.preventDefault();
                clickEvent.stopPropagation();

                try {

                    new UnitViewerPopup<SpyDTO>(EditorStore.getInstance().getSpies().get(spy.getRegionId()).get(spy.getX()).get(spy.getY()).values()) {

                        @Override
                        public Widget getUnitWidget(SpyDTO unit) {
                            return new SpyInfoPanel(unit);  //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }.show();
//                new SectorBrigadesPanel(brigade.getRegionId(), brigade.getX(), brigade.getY()).show();
                } catch (Exception e) {
                    Window.alert("Failed ? " + e.toString());
                }

            }
        });
        spiesGroup.add(img);
    }


    public void drawBTrain(final BaggageTrainDTO bTrain) {
        final Image img = new Image(getPointX(bTrain.getX()) + 40,getPointY(bTrain.getY()) + 40,20,20,"http://static.eaw1805.com/images/figures/baggage.png");
        img.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent mouseOverEvent) {
                img.setWidth(30);
                img.setHeight(30);
            }
        });
        img.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent mouseOutEvent) {
                img.setWidth(20);
                img.setHeight(20);
            }
        });
        img.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent mouseDownEvent) {
                mouseDownEvent.preventDefault();
                mouseDownEvent.stopPropagation();
            }
        });
        img.addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent mouseUpEvent) {
                mouseUpEvent.preventDefault();
                mouseUpEvent.stopPropagation();
            }
        });
        img.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                clickEvent.preventDefault();
                clickEvent.stopPropagation();

                try {
                    new UnitViewerPopup<BaggageTrainDTO>(EditorStore.getInstance().getBaggageTrains().get(bTrain.getRegionId()).get(bTrain.getX()).get(bTrain.getY()).values()) {

                        @Override
                        public Widget getUnitWidget(BaggageTrainDTO unit) {
                            return new BaggageTrainInfoPanel(unit);  //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }.show();
//                new SectorBrigadesPanel(brigade.getRegionId(), brigade.getX(), brigade.getY()).show();
                } catch (Exception e) {
                    Window.alert("Failed ? " + e.toString());
                }

            }
        });
        bTrainGroup.add(img);
    }

    public void drawShip(final ShipDTO ship) {
        final Image img = new Image(getPointX(ship.getX()) + 40,getPointY(ship.getY()),20,20,"http://static.eaw1805.com/images/figures/" + ship.getNationId() + "/FleetMap00.png");
        img.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent mouseOverEvent) {
                img.setWidth(30);
                img.setHeight(30);
            }
        });
        img.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent mouseOutEvent) {
                img.setWidth(20);
                img.setHeight(20);
            }
        });
        img.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent mouseDownEvent) {
                mouseDownEvent.preventDefault();
                mouseDownEvent.stopPropagation();
            }
        });
        img.addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent mouseUpEvent) {
                mouseUpEvent.preventDefault();
                mouseUpEvent.stopPropagation();
            }
        });
        img.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                clickEvent.preventDefault();
                clickEvent.stopPropagation();

                try {
                    List<Object> shipFleetList = new ArrayList<Object>();
                    if (EditorStore.getInstance().getFleets().get(ship.getRegionId()).containsKey(ship.getX())
                            && EditorStore.getInstance().getFleets().get(ship.getRegionId()).get(ship.getX()).containsKey(ship.getY())) {
                        shipFleetList.addAll(EditorStore.getInstance().getFleets().get(ship.getRegionId()).get(ship.getX()).get(ship.getY()).values());
                    }
                    shipFleetList.addAll(EditorStore.getInstance().getShips().get(ship.getRegionId()).get(ship.getX()).get(ship.getY()).values());
                    Iterator<Object> iter = shipFleetList.iterator();
                    while (iter.hasNext()) {
                        Object unit = iter.next();
                        if (unit instanceof ShipDTO) {
                            if (((ShipDTO) unit).getFleet() != 0) {
                                iter.remove();
                            }
                        }
                    }
                    new UnitViewerPopup<Object>(shipFleetList) {

                        @Override
                        public Widget getUnitWidget(Object unit) {
                            if (unit instanceof FleetDTO) {
                                return new FleetInfoPanel((FleetDTO) unit);
                            } else if (unit instanceof ShipDTO) {
                                return new ShipInfoPanel((ShipDTO) unit);
                            } else {
                                return new Label();
                            }

                        }
                    }.show();
//                new SectorBrigadesPanel(brigade.getRegionId(), brigade.getX(), brigade.getY()).show();
                } catch (Exception e) {
                    Window.alert("Failed ? " + e.toString());
                }

            }
        });
        shipGroup.add(img);
    }


    public TileGroup getSectorTileGroup(int x, int y) {
        return tileSectors[x][y];
    }

    public void showTerrainGroup(final boolean visible) {
        terrainGroup.setVisible(visible);
    }

    public void showPrSiteGroup(final boolean visible) {
        prSiteGroup.setVisible(visible);
    }

    public void showNationGroup(final boolean visible) {
        nationGroup.setVisible(visible);
    }

    public void showResourceGroup(final boolean visible) {
        resourceGroup.setVisible(visible);
    }

    public void showPolSphereGroup(final boolean visible) {
        polSphereGroup.setVisible(visible);
    }

    public void showPopulationGroup(final boolean visible) {
        populationGroup.setVisible(visible);
    }

    public void showClimate(final boolean visible) {
        climateGroup.setVisible(visible);
    }

    public void showArmies(final boolean visible) {
        armiesGroup.setVisible(visible);
    }

    public void showCommanders(final boolean visible) {
        commandersGroup.setVisible(visible);
    }

    public void showSpies(final boolean visible) {
        spiesGroup.setVisible(visible);
    }

    public void showBTrains(final boolean visible) {
        bTrainGroup.setVisible(visible);
    }

    public void showShips(final boolean visible) {
        shipGroup.setVisible(visible);
    }

    public void showGrid(final boolean visible) {
        gridGroup.setVisible(visible);
    }

    public void showJumpGroup(final boolean visible) {
        jumpGroup.setVisible(visible);
    }

    public void showSupplyGroup(final boolean visible) {
        supplyGroup.setVisible(visible);

    }

    public int getPointX(final int x) {
        return (SIDE_OFFSET + x) * TILE_WIDTH;
    }

    public int getPointY(final int y) {
        return (SIDE_OFFSET + y) * TILE_HEIGHT;
    }

    public int translateX(int x) {
        DrawingAreaSE dArea = EditorPanel.getInstance().getDrawingArea();
        int xOut = (int) (dArea.getScroller().getHorizontalScrollPosition() + x);
        return xOut;
    }

    public int translateY(int y) {
        DrawingAreaSE dArea = EditorPanel.getInstance().getDrawingArea();
        int yOut = (int) (dArea.getScroller().getVerticalScrollPosition() + y);
        return yOut;
    }

    public int translateToMapX(int x) {
        DrawingAreaSE dArea = EditorPanel.getInstance().getDrawingArea();
        int xOut = (int) (dArea.getScroller().getHorizontalScrollPosition() - getZoomOffsetX() + x);

        xOut = getPositionX(xOut);
        return xOut;
    }

    public int translateToMapXPure(int x) {
        DrawingAreaSE dArea = EditorPanel.getInstance().getDrawingArea();
        return  (int) (dArea.getScroller().getHorizontalScrollPosition() - getZoomOffsetX() + x);
    }

    public int translateToMapYPure(final int y) {
        DrawingAreaSE dArea = EditorPanel.getInstance().getDrawingArea();
        return (int) (dArea.getScroller().getVerticalScrollPosition() - getZoomOffsetY() + y);
    }

    public int translateToMapY(int y) {
        DrawingAreaSE dArea = EditorPanel.getInstance().getDrawingArea();

        int yOut = (int) (dArea.getScroller().getScrollPosition() - getZoomOffsetY() + y);

        yOut = getPositionY(yOut);
        return yOut;
    }

    public double getZoomOffsetX() {
        return (1 - EditorPanel.getInstance().getDrawingArea().getZoomLevel()) * EditorMapUtils.getInstance().getMapWidth() / 2;//5632
    }

    public double getZoomOffsetY() {
        return (1 - EditorPanel.getInstance().getDrawingArea().getZoomLevel()) * EditorMapUtils.getInstance().getMapHeight() / 2;//5632
    }

    public int getMapWidth() {
        return  (RegionSettings.sizeX + 2 * SIDE_OFFSET) * TILE_WIDTH;
    }

    public int getMapHeight() {
        return  (RegionSettings.sizeY + 2 * SIDE_OFFSET) * TILE_WIDTH;
    }

    public int getPositionX(final int pointX) {
        return (int) ((pointX / getZoomedTileSize()) - SIDE_OFFSET);
    }

    public int getPositionY(final int pointY) {
        return (int) ((pointY / getZoomedTileSize()) - SIDE_OFFSET);
    }

    public int getPositionXWithoutZoom(final int pointX) {
        return pointX/TILE_WIDTH - SIDE_OFFSET;
    }

    public int getPositionYWithoutZoom(final int pointY) {
        return pointY/TILE_WIDTH - SIDE_OFFSET;
    }

    public double getZoomedTileSize() {
        return EditorPanel.getInstance().getDrawingArea().getZoomLevel() * TILE_WIDTH;
    }
}
