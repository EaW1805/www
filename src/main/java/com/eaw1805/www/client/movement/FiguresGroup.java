package com.eaw1805.www.client.movement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.movement.PathDTO;
import com.eaw1805.www.client.asyncs.MovementAsyncCallback;
import com.eaw1805.www.client.remote.EmpireRpcService;
import com.eaw1805.www.client.remote.EmpireRpcServiceAsync;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FiguresGroup
        extends Group
        implements ArmyConstants {

    // 2nd level hierarchy vectors of movement.
    // Represents the images of the figures
    // the menu group and the TilesGroup
    // of the available target tiles
    private Image armyFigure;

    private MovementMenuGroup menu;

    private int unitType;

    /**
     * The tiles of each movement path.
     */
    private Group targetTiles = new Group();

    private List<TilesGroup> possiblePaths;

    private int movementPoints;

    private boolean isLast;

    private int conqueredSectors;

    private int conqueredNeutralSectors;

    // Parent Movement group, to provide
    // access to the parent group
    private MovementGroup movementGroup;

    public FiguresGroup(final int theType, final int unitId,
                        final PositionDTO pos,
                        final int MPs,
                        final boolean isLast,
                        final MovementGroup mGroup,
                        final boolean isFirst,
                        final int conSectors,
                        final int conNeutralSectors) {
        super();

        // Initialize array
        possiblePaths = new ArrayList<TilesGroup>();

        unitType = theType;
        movementPoints = MPs;
        movementGroup = mGroup;
        conqueredSectors = conSectors;
        conqueredNeutralSectors = conNeutralSectors;

        // Initialize children
        menu = new MovementMenuGroup(this, isLast, pos, movementPoints, unitId, isFirst, conSectors, conNeutralSectors);

        initFigureImageAndFunctionality(pos, unitType);

        this.setLast(isLast);

        // if the figure has negative mps then
        // we are talking about a forced march
        if ((movementPoints < 0) && (!movementGroup.isForcedMarch()) && (unitType == ARMY || unitType == CORPS || unitType == BRIGADE)) {
            movementGroup.setStateForcedMarch(true);
        }
    }

    public void initFigureImageAndFunctionality(final PositionDTO pos, final int unitType) {
        if (armyFigure != null) {
            remove(armyFigure);
        }
        initFigureImage(pos, unitType);

        // Adding the vector objects to the extended subGroup of Figures
        // A figures group consists of all the figures of a movement unit
        this.add(armyFigure);

        armyFigure.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                final int tileSize = (int) MapStore.getInstance().getTileSize();
                menu.setPopupPosition(event.getClientX() - (150), event.getClientY() - (tileSize + 50));
                menu.initUrlsAndStates();
                menu.showMenu();
            }
        });

    }

    public void getPossibleMovementTiles(final int xPos, final int yPos,
                                         final int movementPoints, final int region,
                                         final int unitType, final int unitId,
                                         final int conqueredSectors,
                                         final int conqueredNeutralSectors) {

        final MovementAsyncCallback movemntAsyncCallback = new MovementAsyncCallback(this);
        if (isLast()) {
            // if it is not last there is no need to ask for the target tiles.
            int warShips = 0;
            final List<Integer> otherNations = new ArrayList<Integer>();
            if (unitType == FLEET) {
                warShips = MiscCalculators.getFleetInfo(NavyStore.getInstance().getFleetById(unitId)).getWarShips();

                final FleetDTO train = NavyStore.getInstance().getFleetById(unitId);

                if (train.getLoadedUnitsMap().containsKey(BRIGADE) && !train.getLoadedUnitsMap().get(BRIGADE).isEmpty()) {
                    warShips = 1;
                    for (Integer brigId : train.getLoadedUnitsMap().get(BRIGADE)) {
                        BrigadeDTO brig = ArmyStore.getInstance().getBrigadeById(brigId);
                        if (brig == null) {
                            brig = AlliedUnitsStore.getInstance().getBrigadeById(brigId);
                        }
                        if (brig != null) {
                            otherNations.add(brig.getNationId());
                        }
                    }
                }

            } else if (unitType == SHIP) {
                if (!NavyStore.getInstance().isTradeShip(NavyStore.getInstance().getShipById(unitId))) {
                    warShips = 1;
                }
                final ShipDTO train = NavyStore.getInstance().getShipById(unitId);

                if (train.getLoadedUnitsMap().containsKey(BRIGADE) && !train.getLoadedUnitsMap().get(BRIGADE).isEmpty()) {
                    warShips = 1;
                    for (Integer brigId : train.getLoadedUnitsMap().get(BRIGADE)) {
                        BrigadeDTO brig = ArmyStore.getInstance().getBrigadeById(brigId);
                        if (brig == null) {
                            brig = AlliedUnitsStore.getInstance().getBrigadeById(brigId);
                        }
                        if (brig != null) {
                            otherNations.add(brig.getNationId());
                        }
                    }
                }


            } else if (unitType == BAGGAGETRAIN) {
                final BaggageTrainDTO train = BaggageTrainStore.getInstance().getBaggageTrainById(unitId);

                if (train.getLoadedUnitsMap().containsKey(BRIGADE) && !train.getLoadedUnitsMap().get(BRIGADE).isEmpty()) {
                    warShips = 1;
                    for (Integer brigId : train.getLoadedUnitsMap().get(BRIGADE)) {
                        BrigadeDTO brig = ArmyStore.getInstance().getBrigadeById(brigId);
                        if (brig == null) {
                            brig = AlliedUnitsStore.getInstance().getBrigadeById(brigId);
                        }
                        if (brig != null) {
                            otherNations.add(brig.getNationId());
                        }
                    }
                }
            }

            final EmpireRpcServiceAsync empireService = GWT.create(EmpireRpcService.class);
            empireService.getPossibleMovementTiles(GameStore.getInstance().getScenarioId(), GameStore.getInstance().getGameId(),
                    GameStore.getInstance().getNationId(), xPos, yPos, movementPoints, region, unitType, conqueredSectors, conqueredNeutralSectors, warShips, otherNations, movemntAsyncCallback);
        }
    }

    public int getUnitType() {
        return unitType;
    }

    private void initFigureImage(final PositionDTO pos, final int unitType) {
        final String imagePath, imagePathHover, imagePathSlc, nationId = String.valueOf(GameStore.getInstance().getNationId());
        switch (unitType) {
            case ARMY:
            case CORPS:
            case BRIGADE:
                imagePath = "http://static.eaw1805.com/images/figures/" + nationId + "/UnitMap00.png";
                imagePathHover = "http://static.eaw1805.com/images/figures/" + nationId + "/UnitMap00Hvr.png";
                imagePathSlc = "http://static.eaw1805.com/images/figures/" + nationId + "/UnitMap00Slc.png";
                break;

            case FLEET:
            case SHIP:
                imagePath = "http://static.eaw1805.com/images/figures/" + nationId + "/FleetMap00.png";
                imagePathHover = "http://static.eaw1805.com/images/figures/" + nationId + "/FleetMap00Hvr.png";
                imagePathSlc = "http://static.eaw1805.com/images/figures/" + nationId + "/FleetMap00Slc.png";
                break;

            case COMMANDER:
                imagePath = "http://static.eaw1805.com/images/figures/" + nationId + "/commander.png";
                imagePathHover = "http://static.eaw1805.com/images/figures/" + nationId + "/commanderHvr.png";
                imagePathSlc = "http://static.eaw1805.com/images/figures/" + nationId + "/commanderSlc.png";
                break;

            case SPY:
                imagePath = "http://static.eaw1805.com/images/figures/" + nationId + "/spy.png";
                imagePathHover = "http://static.eaw1805.com/images/figures/" + nationId + "/spyHvr.png";
                imagePathSlc = "http://static.eaw1805.com/images/figures/" + nationId + "/spySlc.png";
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
        final double analogyX = tileSize / MapStore.TILE_SIZE;
        final double analogyY = tileSize / MapStore.TILE_SIZE;
        final double figureSizeX = java.lang.Math.round(analogyX * MapStore.TILE_SIZE);
        double figureSizeY = java.lang.Math.round(analogyY * MapStore.TILE_SIZE);
        if (unitType == ARMY || unitType == CORPS || unitType == BRIGADE) {
            figureSizeY = java.lang.Math.round(analogyY * MapStore.FIGURE_HEIGHT);
        }

        final MapStore mapStore = MapStore.getInstance();
        final double xPos = mapStore.getPointX(pos.getXStart());
        final double yPos = mapStore.getPointY(pos.getYStart()) - mapStore.getTileSize() / 4d;
        armyFigure = new Image((int) xPos, (int) yPos, (int) figureSizeX, (int) figureSizeY, imagePath);

        // Add functionality to army figure
        armyFigure.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(MouseOverEvent event) {
                if (!getArmyFigure().getHref().endsWith("Hvr.png")) {
                    getArmyFigure().setHref(imagePathHover);
                }

            }
        });

        armyFigure.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                if (getArmyFigure().getHref().endsWith("Hvr.png")) {
                    getArmyFigure().setHref(imagePath);
                }
                getArmyFigure().setStyleName("");
            }
        });

        armyFigure.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                getArmyFigure().setHref(imagePathSlc);
                for (int i = 0; i < movementGroup.getFigures().getVectorObjectCount(); i++) {
                    ((FiguresGroup) movementGroup.getFigures().getVectorObject(i)).getMenu().hideMenu();
                }
                getMenu().showMenu();

            }
        });
    }

    public void addToAvailableMovementRectanglesGroup(final Set<PathDTO> allPaths) {
        possiblePaths.clear();
        for (final PathDTO path : allPaths) {
            if (path != null) {
                if (!path.getCanForceMarch() && movementGroup.isForcedMarch()) {
                    continue;
                }
                final TilesGroup thisTile = new TilesGroup(this, path);
                targetTiles.add(thisTile);
                possiblePaths.add(thisTile);

            }
        }
    }

    /**
     * Ask the parent movement group to remove this figures group and undo the move.
     */
    public void removeLastMovement() {
        movementGroup.removeMyMovement(this);
    }

    public void showTargetTiles() {
        clear();
        add(targetTiles);
        add(armyFigure);
    }

    public void hideTargetTiles() {
        remove(targetTiles);
    }

    public void hidePossiblePaths() {
        for (final TilesGroup thisPath : possiblePaths) {
            thisPath.hidePath();
        }
    }

    public Image getArmyFigure() {
        return armyFigure;
    }

    public MovementMenuGroup getMenu() {
        return menu;
    }

    public Group getTargetTiles() {
        return targetTiles;
    }

    public void setLast(final boolean value) {
        isLast = value;
        menu.setLast(value);
    }

    public boolean isLast() {
        return isLast;
    }

    public MovementGroup getMovementGroup() {
        return movementGroup;
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    public int getConqueredSectors() {
        return conqueredSectors;
    }

    public int getConqueredNeutralSectors() {
        return conqueredNeutralSectors;
    }
}
