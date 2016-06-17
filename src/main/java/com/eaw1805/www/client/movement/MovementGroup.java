package com.eaw1805.www.client.movement;

import com.google.gwt.user.client.Window;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.movement.MovementDTO;
import com.eaw1805.data.dto.web.movement.PathDTO;
import com.eaw1805.data.dto.web.movement.PathSectorDTO;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Text;

public class MovementGroup
        extends Group
        implements ArmyConstants {

    /**
     * 1st level hierarchy vectors of movement.
     * Represents the figures and their children added to the first Group.
     */
    private transient final Group figures = new Group();

    private transient final Group pathLines = new Group();

    // Various variable needed
    private transient final int baseMP;

    private transient int currentMP;

    // Variables that represent the unit type and the
    // unit id of the unit which the figure represents
    private transient final int unitType, unitId;

    // The manager that adds and removes movements
    private transient final MovementStore mvStore = MovementStore.getInstance();


    // The dto containing the string that the path consists of
    // The string is <movement>!<movement>!...!<movement>
    // where movement = x1:y1-x2:y2...xn:yn
    private transient MovementDTO mvDTO;

    // Some more info needed
    private transient final PositionDTO position;

    private transient int usedMPs = 0;

    private transient final int power;

    private transient boolean forcedMarch = false;

    private transient boolean patrolMove = false;

    /**
     * Number of conquered sectors.
     */
    int currentConqueredSectors = 0;

    /**
     * The used conquered sectors.
     */
    int usedConqueredSectors = 0;

    /**
     * Number of neutral conquered sectors.
     */
    int currentConqueredNeutralSectors = 0;

    /**
     * The used conquered neutral sectors.
     */
    int usedConqueredNeutralSectors = 0;

    final Text MPsTip = new Text(0, 0, "");

    public MovementGroup(final int thisType,
                         final int thisId,
                         final PositionDTO pos,
                         final int movementPoints,
                         final int thePower,
                         final int conquer,
                         final int conquerNeutral) {
        super();

        currentMP = movementPoints;
        baseMP = movementPoints;
        unitId = thisId;
        unitType = thisType;
        position = pos;
        power = thePower;
        currentConqueredSectors = conquer;
        currentConqueredNeutralSectors = conquerNeutral;
        add(pathLines);
        add(figures);
        add(MPsTip);
        MPsTip.setFillColor("black");
        MPsTip.setStrokeColor("black");
        MPsTip.setFontSize(12);
        mvDTO = mvStore.getMvDTOByTypeAndId(unitType, unitId);

        // Adding the figures to the extended Group
        addFiguresFromOrders(mvDTO, position, movementPoints, conquer, conquerNeutral);

        MovementStore.getInstance().addMovementGroup(this, pos.getRegionId(), thisType, thisId);

        // Check if it is a naval patrol move
        if (mvDTO != null && (unitType == FLEET || unitType == SHIP) && mvDTO.getPatrol()) {
            patrolMove = mvDTO.getPatrol();

        } else if (mvDTO != null && (unitType == ARMY || unitType == CORPS || unitType == BRIGADE) && mvDTO.getPatrol()) {
            patrolMove = mvDTO.getPatrol();
        }

        reCalculateCurrentMP();
        reDrawMovement();
    }

    private void addFiguresFromOrders(final MovementDTO mvDtos,
                                      final PositionDTO pos,
                                      final int movementPoints,
                                      final int conquerPoints,
                                      final int conquerNeutralPoints) {

        try {
            // Add the first figure. If the orders are not null
            // add a figure for each order on the list.
            if (mvDtos == null
                    || mvDtos.getPaths().isEmpty() || (mvDtos.getPaths().size() == 1
                    && mvDtos.getPaths().get(0).getPathSectors().size() == 0)) {
                usedMPs = 0;
                usedConqueredNeutralSectors = 0;
                usedConqueredSectors = 0;
                if ((unitType == CORPS || unitType == BRIGADE || unitType == ARMY) && patrolMove) {
                    final SectorDTO sector = RegionStore.getInstance().getRegionSectorsByRegionId(pos.getRegionId())[pos.getXStart()][pos.getYStart()];
                    if (sector.getNationId() != GameStore.getInstance().getNationId()) {
                        if (sector.getNationId() == -1) {
                            usedConqueredNeutralSectors += 1;
                        } else if (RelationsStore.getInstance().getRelationsMap().get(sector.getNationId()).getRelation() == RelationConstants.REL_WAR) {
                            usedConqueredSectors += 1;
                        }
                    }
                }
                final FiguresGroup figure = new FiguresGroup(unitType, unitId, pos, movementPoints, true, this, true, conquerPoints - usedConqueredSectors, conquerNeutralPoints - usedConqueredNeutralSectors);
                figures.add(figure);


            } else {
                usedMPs = 0;
                usedConqueredNeutralSectors = 0;
                usedConqueredSectors = 0;

                if ((unitType == CORPS || unitType == BRIGADE || unitType == ARMY) && patrolMove) {
                    final SectorDTO sector = RegionStore.getInstance().getRegionSectorsByRegionId(pos.getRegionId())[pos.getXStart()][pos.getYStart()];
                    if (sector.getNationId() != GameStore.getInstance().getNationId()) {
                        if (sector.getNationId() == -1) {
                            usedConqueredNeutralSectors += 1;
                        } else if (RelationsStore.getInstance().getRelationsMap().get(sector.getNationId()).getRelation() == RelationConstants.REL_WAR) {
                            usedConqueredSectors += 1;
                        }
                    }
                }

                // Draw the starting figure
                final FiguresGroup startFigure = new FiguresGroup(unitType, unitId, pos, movementPoints, true, this, true, conquerPoints - usedConqueredSectors, conquerNeutralPoints - usedConqueredNeutralSectors);
                figures.add(startFigure);

                // Iterate paths
                for (int index = 0; index <= mvDTO.getPaths().size() - 1; index++) {
                    final PathDTO thisPath = mvDTO.getPaths().get(index);
                    final boolean last = (index == (mvDTO.getPaths().size() - 1));

                    // Retrieve last sector
                    final PathSectorDTO finishTile = thisPath.getPathSectors().get(thisPath.getTotLength() - 1);
                    finishTile.setXStart(finishTile.getX());
                    finishTile.setYStart(finishTile.getY());
                    finishTile.setRegionId(MapStore.getInstance().getActiveRegion());

                    // Update used movement points.
                    //try to fix conquer calculations...
                    //recalculate conquer sectors
                    thisPath.setTotalConquer(0);
                    thisPath.setTotalConquerNeutral(0);
                    if (thisPath.getPathSectors() != null) {
                        int count = 0;
                        for (PathSectorDTO pathSector : thisPath.getPathSectors()) {
                            count++;
                            if ((count == 1 && index != 0) ||
                                    (count==1 && index == 0 && !patrolMove)) {
                                continue;
                            }
                            final SectorDTO sector = RegionStore.getInstance().getRegionSectorsByRegionId(pos.getRegionId())[pathSector.getX()][pathSector.getY()];
                            if (sector != null) {
                                if (sector.getNationId() == -1) {
                                    thisPath.setTotalConquerNeutral(thisPath.getTotalConquerNeutral() + 1);
                                } else if (sector.getNationId() != GameStore.getInstance().getNationId() &&
                                        RelationsStore.getInstance().getRelationsMap().get(sector.getNationId()).getRelation() == RelationConstants.REL_WAR) {
                                    thisPath.setTotalConquer(thisPath.getTotalConquer() + 1);
                                }
                            }
                        }
                    }
                    if (thisPath.getRegionId() == RegionConstants.EUROPE) {
                        usedMPs += thisPath.getTotalCost();
                    } else {
                        usedMPs += 2*thisPath.getTotalCost();
                    }
                    usedConqueredNeutralSectors += thisPath.getTotalConquerNeutral();
                    usedConqueredSectors += thisPath.getTotalConquer();

                    final boolean forceMarchBefore = isForcedMarch();
                    final FiguresGroup figure = new FiguresGroup(unitType, unitId, finishTile,
                            movementPoints - usedMPs,
                            last,
                            this, false,
                            conquerPoints - usedConqueredSectors,
                            conquerNeutralPoints - usedConqueredNeutralSectors);

                    if (forceMarchBefore != isForcedMarch()) {
                        break;
                    }

                    final TilesGroup tgroup = new TilesGroup(figure, thisPath);
                    pathLines.add(tgroup.getPathTiles());
                    figures.add(figure);

                }
            }
        } catch (Exception ex) {
            Window.alert("Ff? " + ex.toString());
            new ErrorPopup(ErrorPopup.Level.ERROR, "Error drawing movement figures", false) {

            };
        }
    }

    // Function Called primarily by TilesGroup object when the
    // user clicks on an available target tile
    public int addNewFigureAndPath(final PositionDTO position,
                                   final PathDTO thisPath,
                                   final Group lines) {
        int totalCost = thisPath.getTotalCost();
        usedMPs += totalCost;
        usedConqueredSectors += thisPath.getTotalConquer();
        usedConqueredNeutralSectors += thisPath.getTotalConquerNeutral();

        final FiguresGroup figure = new FiguresGroup(unitType, unitId,
                position,
                currentMP - usedMPs,
                true,
                this, false,
                currentConqueredSectors - usedConqueredSectors,
                currentConqueredNeutralSectors - usedConqueredNeutralSectors);

        ((FiguresGroup) figures.getVectorObject(figures.getVectorObjectCount() - 1)).setLast(false);
        figures.add(figure);
        pathLines.add(lines);

        try {
//            final TilesGroup temp = new TilesGroup(unitType, thisPath, isForcedMarch(), isPatrolMove());
//            MovementStore.getInstance()
//                    .addMovementLinesGroup(temp.getPathTiles(), position.getRegionId(), unitType, unitId, false);
            //just to be sure the path shows the correct color in all its length...
            //restart it.
            MovementStore.getInstance().restartUnitMovement(position.getRegionId(), unitType, unitId);
        } catch (Exception e) {
//            Window.alert("oops!" + e.toString());
        }

        figure.getMenu().startNextStep();
        return usedMPs;
    }

    //Removes last movement and related data
    public int removeMyMovement(FiguresGroup figuresGroup) {
        if (figuresGroup == null) {
            figuresGroup = (FiguresGroup) figures.getVectorObject(figures.getVectorObjectCount() - 1);
        }
        if (figuresGroup.isLast() && figures.getVectorObjectCount() > 1) {
            figures.remove(figures.getVectorObject(figures.getVectorObjectCount() - 1));
            // regain the lost movement points
            usedMPs = (currentMP - ((FiguresGroup) figures.getVectorObject(figures.getVectorObjectCount() - 1)).getMovementPoints());
            usedConqueredSectors = (currentConqueredSectors - ((FiguresGroup) figures.getVectorObject(figures.getVectorObjectCount() - 1)).getConqueredSectors());
            usedConqueredNeutralSectors = (currentConqueredNeutralSectors - ((FiguresGroup) figures.getVectorObject(figures.getVectorObjectCount() - 1)).getConqueredNeutralSectors());

            boolean forcedMarch = false;
            if ((baseMP < currentMP - usedMPs)) {
                forcedMarch = true;
            }

            mvStore.removeMovementOrder(unitType, unitId, mvStore.getMvDTOByTypeAndId(unitType, unitId).getPaths(), forcedMarch);
            final FiguresGroup figure = (FiguresGroup) figures.getVectorObject(figures.getVectorObjectCount() - 1);
            figure.setLast(true);

            pathLines.remove(pathLines.getVectorObject(pathLines.getVectorObjectCount() - 1));
            figure.getMenu().startNextStep();

        } else {
            figures.remove(figuresGroup);
        }

        //restart the path that appears when selecting "movement" from options panel
        MovementStore.getInstance().restartUnitMovement(position.getRegionId(), unitType, unitId);
        UnitEventManager.changeUnit(unitType, unitId);
        return (currentMP - usedMPs);
    }

    /**
     * @return the forcedMarch
     */
    public boolean isForcedMarch() {
        return forcedMarch;
    }

    // Sets the movement type as forced march and 
    // vice versa
    public boolean setStateForcedMarch(final boolean value) {
        forcedMarch = value;
        reCalculateCurrentMP();
        reDrawMovement();
        return forcedMarch == value;
    }

    public void reCalculateCurrentMP() {
        if (isForcedMarch()) {
            currentMP = Math.round((float) (baseMP * 1.5d));
        } else {
            currentMP = baseMP;
        }

        if (isPatrolMove()) {
            if (unitType == FLEET || unitType == SHIP) {
                currentMP /= 2;
            } else {
                final SectorDTO startSector = RegionStore.getInstance().getSectorByPosition(position);
                if (startSector.getRegionId() == RegionConstants.EUROPE) {
                    currentMP -= startSector.getTerrain().getActualMPs();
                } else {
                    currentMP -= GameStore.getInstance().getColoniesMPsModifier() * startSector.getTerrain().getActualMPs();
                }

            }
        }
    }

    public boolean setStatePatrol(final boolean value) {
        boolean previousValue = patrolMove;
        patrolMove = value;
        mvStore.setPatrolState(unitType, unitId, value);
        reCalculateCurrentMP();

        if (currentMP < usedMPs) {
            patrolMove = previousValue;
            mvStore.setPatrolState(unitType, unitId, previousValue);
            reCalculateCurrentMP();
            new ErrorPopup(ErrorPopup.Level.WARNING, "Not enough movement points to use patrol", false);
            return false;
        }

        if (unitType == FLEET || unitType == SHIP) {
            String from;
            String to;
            if (value) {
                from = "yellow";
                to = "red";
            } else {
                from = "red";
                to = "yellow";
            }
            for (int i = 0; i < pathLines.getVectorObjectCount(); i++) {
                for (int j = 0; j < ((Group) pathLines.getVectorObject(i)).getVectorObjectCount(); j++) {
                    Image vcImage = (Image) ((Group) pathLines.getVectorObject(i)).getVectorObject(j);
                    vcImage.setHref(vcImage.getHref().replace(from, to));
                }
            }
        }

        reDrawMovement();
        //be sure to update the images if he changes the patrol state.
        MovementStore.getInstance().restartUnitMovement(position.getRegionId(), unitType, unitId);
        return true;
    }

    private void reDrawMovement() {
        pathLines.clear();
        figures.clear();

        // Adding the figures to the extended Group
        mvDTO = mvStore.getMvDTOByTypeAndId(unitType, unitId);
        addFiguresFromOrders(mvDTO, position, currentMP, currentConqueredSectors, currentConqueredNeutralSectors);

        // On change start the new movement
        final FiguresGroup figure = (FiguresGroup) figures.getVectorObject(figures.getVectorObjectCount() - 1);
        figure.getMenu().startNextStep();
    }

    public boolean isPatrolMove() {
        return patrolMove;
    }

    public Group getFigures() {
        return figures;
    }

    public int getUnitType() {
        return unitType;
    }

    public int getUnitId() {
        return unitId;
    }

    public int getBaseMP() {
        return baseMP;
    }

    public MovementDTO getMvDTO() {
        return mvDTO;
    }

    public int getCurrentMp() {
        return currentMP;
    }

    public Text getMPsTip() {
        return MPsTip;
    }

    public int getUsedMPs() {
        return usedMPs;
    }
}
