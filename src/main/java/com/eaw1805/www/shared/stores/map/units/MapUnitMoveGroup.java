package empire.webapp.shared.stores.map.units;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import empire.data.constants.ArmyConstants;
import empire.data.dto.common.PositionDTO;
import empire.data.dto.common.SectorDTO;
import empire.data.dto.web.movement.PathDTO;
import empire.webapp.client.events.movement.MovementEventManager;
import empire.webapp.client.events.movement.StartAllyMoveEvent;
import empire.webapp.client.events.movement.StartAllyMoveHandler;
import empire.webapp.client.events.movement.StopAllyMoveEvent;
import empire.webapp.client.events.movement.StopAllyMoveHandler;
import empire.webapp.shared.stores.map.MapStore;
import empire.webapp.shared.stores.units.AlliedUnitsStore;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;

import java.util.List;

public class MapUnitMoveGroup
        extends Group
        implements ArmyConstants {

    private static final String BASE_PATH = "http://static.eaw1805.com/images/figures/";

    public MapUnitMoveGroup() {
        super();
        MovementEventManager.addStartAllyMoveHandler(new StartAllyMoveHandler() {
            public void onStartAllyMove(final StartAllyMoveEvent event) {
                MapUnitMoveGroup.this.clear();
                final List<PathDTO> sectors = AlliedUnitsStore.getInstance().getMovementByTypeAndId(event.getInfoType(),
                        event.getInfoId(), MapStore.getInstance().getActiveRegion(), event.getNationId());
                if (sectors != null) {
                    //addPathAndFiguresToMap(sectors, event.getNationId(), event.getInfoType());
                } else {
                    addFigureToMap(event.getStartSector(), Integer.toString(event.getNationId()), event.getInfoType());
                }
            }

        });

        MovementEventManager.addStopAllyMoveHandler(new StopAllyMoveHandler() {
            public void onStopAllyMove(final StopAllyMoveEvent event) {
                MapUnitMoveGroup.this.clear();
            }
        });
    }

    @SuppressWarnings({"unused", "restriction"})
    private void addPathAndFiguresToMap(final List<SectorDTO> sectors, final int nationId, final int infoType) {
        addFigureToMap(sectors.get(0), Integer.toString(nationId), infoType);
        final String pathUrl = "http://static.eaw1805.com/tiles/move/red/";
        final int tileSize = (int) MapStore.getInstance().getTileSize();
        Image thisTile;
        int preX = sectors.get(0).getX();
        int preY = sectors.get(0).getY();
        int posX;
        int posY;
        int nextX = 0;
        int nextY = 0;
        for (final SectorDTO thisSector : sectors) {
            StringBuilder path = new StringBuilder();
            path.append(pathUrl);
            if (sectors.indexOf(thisSector) == 0) {
                path.append("start-");
            } else if (sectors.indexOf(thisSector) == sectors.size() - 1) {
                path.append("end-");
            } else {
                path.append("move-");
            }
            posX = thisSector.getX();
            posY = thisSector.getY();
            if ((sectors.indexOf(thisSector) + 1) <= (sectors.size() - 1)) {
                nextX = sectors.get(sectors.indexOf(thisSector) + 1).getX();
                nextY = sectors.get(sectors.indexOf(thisSector) + 1).getY();
            }
            if ((sectors.indexOf(thisSector) - 1) > 0) {
                preX = sectors.get(sectors.indexOf(thisSector) - 1).getX();
                preY = sectors.get(sectors.indexOf(thisSector) - 1).getY();
            }
            if (!path.toString().contains("start") && !path.toString().contains("end")) {
                path.append(getStartingDirection(preX, preY, posX, posY));
            }
            if (!path.toString().contains("start") && !path.toString().contains("end")) {
                path.append("-");
            }
            if (!path.toString().contains("end")) {
                path.append(getEndingDirection(posX, posY, nextX, nextY));
            } else {
                path.append(getEndingDirection(preX, preY, posX, posY));
            }

            thisTile = new Image(MapStore.getInstance().getPointX(posX),
                    MapStore.getInstance().getPointY(posY),
                    tileSize, tileSize, path.toString() + ".png");
            thisTile.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);

            this.add(thisTile);
        }
        addFigureToMap(sectors.get(sectors.size() - 1), Integer.toString(nationId), infoType);
    }

    private void addFigureToMap(final PositionDTO sector, final String nationId, final int type) {
        final String imagePath, imagePathHover, imagePathSlc;
        switch (type) {
            case ARMY:
            case CORPS:
            case BRIGADE:
                imagePath = BASE_PATH + nationId + "/UnitMap00.png";
                imagePathHover = BASE_PATH + nationId + "/UnitMap00Hvr.png";
                imagePathSlc = BASE_PATH + nationId + "/UnitMap00Slc.png";
                break;

            case FLEET:
            case SHIP:
                imagePath = BASE_PATH + nationId + "/FleetMap00.png";
                imagePathHover = BASE_PATH + nationId + "/FleetMap00Hvr.png";
                imagePathSlc = BASE_PATH + nationId + "/FleetMap00Slc.png";
                break;

            case COMMANDER:
                imagePath = BASE_PATH + nationId + "/commander.png";
                imagePathHover = BASE_PATH + nationId + "/commanderHvr.png";
                imagePathSlc = BASE_PATH + nationId + "/commanderSlc.png";
                break;

            case SPY:
                imagePath = BASE_PATH + nationId + "/spy.png";
                imagePathHover = BASE_PATH + nationId + "/spyHvr.png";
                imagePathSlc = BASE_PATH + nationId + "/spySlc.png";
                break;

            case BAGGAGETRAIN:
                imagePath = "http://static.eaw1805.com/images/figures/baggage.png";
                imagePathHover = "http://static.eaw1805.com/images/figures/baggageHvr.png";
                imagePathSlc = "http://static.eaw1805.com/images/figures/baggageSlc.png";
                break;

            default:
                imagePath = "http://static.eaw1805.com/images/figures/caravan.png";
                imagePathHover = "http://static.eaw1805.com/images/figures/caravanHover.png";
                imagePathSlc = "http://static.eaw1805.com/images/figures/caravanSelected.png";
                break;
        }

        final double tileSize = MapStore.getInstance().getTileSize();
        final double analogyX = (tileSize) / MapStore.TILE_SIZE;
        final double analogyY = (tileSize) / MapStore.TILE_SIZE;
        final double figureSizeX = java.lang.Math.round(analogyX * MapStore.TILE_SIZE);
        double figureSizeY = java.lang.Math.round(analogyY * MapStore.TILE_SIZE);
        if (type == ARMY || type == CORPS || type == BRIGADE) {
            figureSizeY = java.lang.Math.round(analogyY * MapStore.FIGURE_HEIGHT);
        }

        final double xPos = MapStore.getInstance().getPointX(sector.getX());
        final double yPos = MapStore.getInstance().getPointY(sector.getY()) - MapStore.getInstance().getTileSize() / 4;
        final Image armyFigure = new Image((int) xPos, (int) yPos, (int) figureSizeX, (int) figureSizeY, imagePath);

        // Add functionality to army figure
        armyFigure.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(final MouseOverEvent event) {
                if (!armyFigure.getHref().endsWith("Hvr.png")) {
                    armyFigure.setHref(imagePathHover);
                }

            }
        });

        armyFigure.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                if (armyFigure.getHref().endsWith("Hvr.png")) {
                    armyFigure.setHref(imagePath);
                }
                armyFigure.setStyleName("");
            }
        });

        armyFigure.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                armyFigure.setHref(imagePathSlc);
                MovementEventManager.stopAllyMovement(type, 0);

            }
        });
        this.add(armyFigure);

    }


    private String getEndingDirection(final int startX, final int startY,
                                      final int thisX, final int thisY) {
        if (startX < thisX && startY == thisY) {
            return "R";

        } else if (startX < thisX && startY < thisY) {
            return "RD";

        } else if (startX < thisX && startY > thisY) {
            return "RU";

        } else if (startX > thisX && startY == thisY) {
            return "L";

        } else if (startX > thisX && startY < thisY) {
            return "LD";

        } else if (startX > thisX && startY > thisY) {
            return "LU";

        } else if (startX == thisX && startY < thisY) {
            return "D";

        } else if (startX == thisX && startY > thisY) {
            return "U";
        }

        return "";
    }

    private String getStartingDirection(final int startX, final int startY,
                                        final int thisX, final int thisY) {

        if (startX < thisX && startY == thisY) {
            return "L";

        } else if (startX < thisX && startY < thisY) {
            return "LU";

        } else if (startX < thisX && startY > thisY) {
            return "LD";

        } else if (startX > thisX && startY == thisY) {
            return "R";

        } else if (startX > thisX && startY < thisY) {
            return "RU";

        } else if (startX > thisX && startY > thisY) {
            return "RD";

        } else if (startX == thisX && startY < thisY) {
            return "U";

        } else if (startX == thisX && startY > thisY) {
            return "D";
        }

        return "";
    }
}
