package com.eaw1805.www.client.movement;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.movement.MovementDTO;
import com.eaw1805.data.dto.web.movement.PathDTO;
import com.eaw1805.data.dto.web.movement.PathSectorDTO;
import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.views.TradePanelView;
import com.eaw1805.www.client.views.infopanels.TradeInfoViewInterface;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

public class MovementMenuGroup
        extends PopupPanelEAW
        implements ArmyConstants, TradeInfoViewInterface {

    // 3rd level hierarchy vectors of movement.
    // Represents the images of the menu choices
    // when clicking the target figure.
    private transient final Image undoImg;
    private transient final ImageButton stopNSaveImg;
    private transient final ImageButton normalModeImg, forcedMarchImg, patrolMoveImg;

    // Extra variables for the Class usage
    private transient final boolean isFirst;
    private transient boolean isLast;
    private boolean isPatrolMove = false;
    private boolean isForcedMove = false;

    /**
     * Object that provides access to the parent.
     */
    private transient final FiguresGroup parentGroup;

    /**
     * Some more info needed.
     */
    private transient final PositionDTO position;

    private transient final int unitType, unitId;

    private transient int movementPoints, conqSectors, conqNeutralSectors;

    private transient ImageButton tradeImg;

    public MovementMenuGroup(final FiguresGroup fgGroup,
                             final boolean isLast,
                             final PositionDTO pos,
                             final int totalMP,
                             final int unitId,
                             final boolean isFirst,
                             final int conSectors,
                             final int conNeutralSectors) {
        super();
        setAutoHideEnabled(true);
        setStyleName("");

        position = pos;
        movementPoints = totalMP;
        unitType = fgGroup.getUnitType();
        this.unitId = unitId;
        conqNeutralSectors = conNeutralSectors;
        conqSectors = conSectors;

        // Initialize children
        this.isLast = isLast;
        this.isFirst = isFirst;
        parentGroup = fgGroup;
        isPatrolMove = parentGroup.getMovementGroup().isPatrolMove();
        isForcedMove = parentGroup.getMovementGroup().isForcedMarch();

        // Initialize image buttons
        forcedMarchImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButForcedMarchOff.png");
        forcedMarchImg.setStyleName("pointer");
        forcedMarchImg.setTitle("Forced March Move");

        normalModeImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
        normalModeImg.setStyleName("pointer");
        normalModeImg.setTitle("Normal mode");

        stopNSaveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButOrdersStopMoveOff.png");

        if (unitType == SHIP || unitType == FLEET) {
            patrolMoveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButShipPatrolOff.png");
            patrolMoveImg.setTitle("Patrol");

        } else {
            patrolMoveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButTroopPatrolOff.png");
            patrolMoveImg.setTitle("Engage & move");
        }
        patrolMoveImg.setStyleName("pointer");


        undoImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelMoveOff.png");
        undoImg.setStyleName("pointer");
        undoImg.setTitle("Undo movement");
        undoImg.setSize("36px", "36px");

        initMenuItems();

        setupBackground();
    }

    public Image newImageMiddle() {
        final Image imgMiddle = new Image("http://static.eaw1805.com/images/buttons/menu/Middle.png");
        imgMiddle.setSize("41px", "73px");
        return imgMiddle;
    }

    public void setupBackground() {
        final AbsolutePanel menuBase = new AbsolutePanel();
        menuBase.setStyleName("");
        menuBase.setSize("361px", "137px");
        setWidget(menuBase);

        final Image imgLeftEnd = new Image("http://static.eaw1805.com/images/buttons/menu/LeftEnd.png");
        imgLeftEnd.setSize("37px", "73px");
        menuBase.add(imgLeftEnd, 0, 0);
        int posX = 37;

        if (unitType == ARMY
                || unitType == CORPS
                || unitType == BRIGADE
                || unitType == SHIP
                || unitType == FLEET) {

            normalModeImg.setSize("36px", "36px");
            menuBase.add(newImageMiddle(), posX, 0);
            menuBase.add(normalModeImg, posX + 3, 19);
            posX += 41;
        }

        if (unitType == BRIGADE
                || unitType == CORPS
                || unitType == ARMY) {

            forcedMarchImg.setSize("36px", "36px");
            menuBase.add(newImageMiddle(), posX, 0);
            menuBase.add(forcedMarchImg, posX + 3, 19);
            posX += 41;
        }

        if (unitType == BRIGADE
                || unitType == CORPS
                || unitType == ARMY
                || (unitType == SHIP && !NavyStore.getInstance().isTradeShip(NavyStore.getInstance().getShipById(unitId)))
                || unitType == FLEET) {
            boolean showEngage = true;
            if (unitType == BRIGADE) {
                final BrigadeDTO brig = ArmyStore.getInstance().getBrigadeById(unitId);
                for (BattalionDTO batt : brig.getBattalions()) {
                    if (batt.getHasLost()) {
                        showEngage = false;
                        break;
                    }
                }
            } else if (unitType == CORPS) {
                for (BrigadeDTO brig : ArmyStore.getInstance().getCorpByID(unitId).getBrigades().values()) {
                    for (BattalionDTO batt : brig.getBattalions()) {
                        if (batt.getHasLost()) {
                            showEngage = false;
                            break;
                        }
                    }
                    if (!showEngage) {
                        break;
                    }
                }
            } else if (unitType == ARMY) {
                for (CorpDTO corp : ArmyStore.getInstance().getArmyById(unitId).getCorps().values()) {
                    for (BrigadeDTO brig : corp.getBrigades().values()) {
                        for (BattalionDTO batt : brig.getBattalions()) {
                            if (batt.getHasLost()) {
                                showEngage = false;
                                break;
                            }
                        }
                        if (!showEngage) {
                            break;
                        }
                    }
                    if (!showEngage) {
                        break;
                    }
                }
            }


            if (showEngage) {
                patrolMoveImg.setSize("36px", "36px");
                menuBase.add(newImageMiddle(), posX, 0);
                menuBase.add(patrolMoveImg, posX + 3, 19);
                posX += 41;
            }
        }

        stopNSaveImg.setSize("36px", "36px");
        menuBase.add(newImageMiddle(), posX, 0);
        menuBase.add(stopNSaveImg, posX + 3, 19);
        posX += 41;

        if (!isFirst) {
            menuBase.add(newImageMiddle(), posX, 0);
            menuBase.add(undoImg, posX + 3, 19);
            undoImg.setSize("36px", "36px");
            posX += 41;
        }

//        if ((unitType == SHIP && NavyStore.getInstance().isTradeShip(NavyStore.getInstance().getShipById(unitId))) || unitType == BAGGAGETRAIN ||
//                (unitType == FLEET && MiscCalculators.getFleetInfo(NavyStore.getInstance().getIdFleetMap().get(unitId)).getMerchantShips() > 0)) {
//            tradeImg.setSize("36px", "36px");
//            menuBase.add(newImageMiddle(), posX, 0);
//            menuBase.add(tradeImg, posX + 3, 19);
//            posX += 41;
//        }

        final Image imgRightEnd = new Image("http://static.eaw1805.com/images/buttons/menu/RightEnd.png");
        imgRightEnd.setSize("37px", "73px");
        menuBase.add(imgRightEnd, posX, 0);
        posX += 37;
        menuBase.setSize(posX + "px", "137px");
    }

    /**
     * Method that any give time that the menu opens
     * we recalculate what images should be shown and their
     * respective states
     */
    public void initUrlsAndStates() {
        isPatrolMove = parentGroup.getMovementGroup().isPatrolMove();
        isForcedMove = parentGroup.getMovementGroup().isForcedMarch();

        if (isForcedMove || (isPatrolMove && (unitType == FLEET || unitType == SHIP))) {
            normalModeImg.setUrl("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
            normalModeImg.deselect();
            if (isForcedMove) {
                forcedMarchImg.setSelected(true);
                forcedMarchImg.setUrl("http://static.eaw1805.com/images/buttons/ButForcedMarchOn.png");
            }

        } else {
            normalModeImg.setUrl("http://static.eaw1805.com/images/buttons/ButMoveOn.png");
            normalModeImg.setSelected(true);
            forcedMarchImg.setSelected(false);
            forcedMarchImg.setUrl("http://static.eaw1805.com/images/buttons/ButForcedMarchOff.png");
        }

        if (isPatrolMove) {
            if (unitType == SHIP || unitType == FLEET) {
                patrolMoveImg.setUrl("http://static.eaw1805.com/images/buttons/ButShipPatrolOn.png");

            } else {
                patrolMoveImg.setUrl("http://static.eaw1805.com/images/buttons/ButTroopPatrolOn.png");
            }
            patrolMoveImg.setSelected(true);

        } else {
            if (unitType == SHIP || unitType == FLEET) {
                patrolMoveImg.setUrl("http://static.eaw1805.com/images/buttons/ButShipPatrolOff.png");

            } else {
                patrolMoveImg.setUrl("http://static.eaw1805.com/images/buttons/ButTroopPatrolOff.png");
            }
            patrolMoveImg.deselect();
        }

        if (isLast) {
            undoImg.setUrl("http://static.eaw1805.com/images/buttons/ButCancelMoveOff.png");
            undoImg.setTitle("Undo movement");

        } else {
            undoImg.setUrl("http://static.eaw1805.com/images/buttons/ButCancelMoveNA.png");
            undoImg.setTitle("Cannot undo this movement. Only the last command \n can be undone.");

        }
    }


    private void initMenuItems() {
        stopNSaveImg.setUrl("http://static.eaw1805.com/images/buttons/ButOrdersStopMoveOff.png");
        stopNSaveImg.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent clickEvent) {
                // return to normal selecting of the figures
                MovementEventManager.stopMovement(unitType, unitId);
                // unregister movement from escape
                GameStore.getInstance().unRegisterMovementComponent(unitId, unitType);
                hideMenu();
            }
        });

        if (unitType == ARMY
                || unitType == CORPS
                || unitType == BRIGADE) {
            // LAND UNITS
            normalModeImg.addClickHandler(new ClickHandler() {
                public void onClick(final ClickEvent clickEvent) {
                    if (isForcedMove) {
                        isForcedMove = false;
                        if (!parentGroup.getMovementGroup().setStateForcedMarch(false)) {
                            new ErrorPopup(ErrorPopup.Level.WARNING, "You cannot switch to normal mode now because you have exceeded the MPs for normal move. You need to undo the movement first.", false);
                        }
                    }
                    hide();
                }
            });

        } else {
            // SHIPS + FLEETS
            normalModeImg.addClickHandler(new ClickHandler() {
                public void onClick(final ClickEvent clickEvent) {
                    if (isPatrolMove) {
                        isPatrolMove = false;
                        parentGroup.getMovementGroup().setStatePatrol(false);
                        patrolMoveImg.deselect();
                        patrolMoveImg.setUrl("http://static.eaw1805.com/images/buttons/ButShipPatrolOff.png");
                        normalModeImg.setSelected(true);
                        normalModeImg.setUrl("http://static.eaw1805.com/images/buttons/ButMoveOn.png");
                    }
                }
            });
        }

        if (unitType == ARMY
                || unitType == CORPS
                || unitType == BRIGADE) {
            // LAND UNITS
            forcedMarchImg.addClickHandler(new ClickHandler() {
                public void onClick(final ClickEvent clickEvent) {

                    final MovementDTO mvDto = MovementStore.getInstance().getMvDTOByTypeAndId(unitType, unitId);
                    if (mvDto != null) {
                        final PositionDTO unitPos = ArmyStore.getInstance().getUnitByTypeAndId(unitType, unitId);

                        int count = 0;
                        for (final PathDTO path : mvDto.getPaths()) {
                            count++;

                            boolean canForce = true;
                            if (count == 1 && !mvDto.getPatrol()) {
                                int inCount = 0;
                                for (PathSectorDTO pathSector : path.getPathSectors()) {
                                    inCount++;
                                    if (inCount != 1 || !pathSector.positionToString().equals(unitPos.startPositionToString())) {
                                        canForce = canForce && pathSector.getCanForceMarch();
                                    }
                                }
                            } else {
                                canForce = path.getCanForceMarchBySectors();
                            }
                            if (!canForce) {

                                new ErrorPopup(ErrorPopup.Level.WARNING, "You cannot force march this unit because the path has neutral sectors", false);
                                hide();
                                return;
                            }
                        }
                    }
                    if (!isForcedMove) {
                        isForcedMove = true;
                        parentGroup.getMovementGroup().setStateForcedMarch(true);
                    }
                    hide();
                }
            });
        }

        if (unitType == ARMY
                || unitType == CORPS
                || unitType == BRIGADE) {
            // LAND UNITS
            patrolMoveImg.addClickHandler(new ClickHandler() {
                public void onClick(final ClickEvent clickEvent) {
                    isPatrolMove = !isPatrolMove;
                    if (!parentGroup.getMovementGroup().setStatePatrol(isPatrolMove)) {
                        //if not succeeded revert value
                        isPatrolMove = !isPatrolMove;
                    }
                    patrolMoveImg.setSelected(isPatrolMove);

                    if (isPatrolMove) {
                        patrolMoveImg.setUrl("http://static.eaw1805.com/images/buttons/ButTroopPatrolOn.png");

                    } else {
                        patrolMoveImg.setUrl("http://static.eaw1805.com/images/buttons/ButTroopPatrolOff.png");
                    }
                }
            });

        } else {
            // SEA UNITS
            patrolMoveImg.addClickHandler(new ClickHandler() {
                public void onClick(final ClickEvent clickEvent) {
                    if (!isPatrolMove) {
                        isPatrolMove = true;
                        parentGroup.getMovementGroup().setStatePatrol(true);

                        patrolMoveImg.setSelected(true);
                        patrolMoveImg.setUrl("http://static.eaw1805.com/images/buttons/ButShipPatrolOn.png");

                        normalModeImg.deselect();
                        normalModeImg.setUrl("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
                    }
                }
            });
        }

        // Initializing undo move command functions
        undoImg.addMouseOverHandler(new MouseOverHandler() {

            public void onMouseOver(final MouseOverEvent event) {
                if (!isLast) {
                    undoImg.setUrl("http://static.eaw1805.com/images/buttons/ButCancelMoveNA.png");
                    undoImg.setTitle("Cannot undo this movement.Only the last command \n can b undone.");
                }
            }
        });

        undoImg.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent event) {
                if (isLast || TradeStore.getInstance().hasInitSecondPhase(getUnitType(), unitId)) {
                    if (isLast && ((getUnitType() != BAGGAGETRAIN && getUnitType() != SHIP && getUnitType() != FLEET) || !TradeStore.getInstance().hasInitSecondPhase(getUnitType(), unitId))) {
                        hideMenu();
                        parentGroup.removeLastMovement();

                    } else {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "The unit has initiated the second trading phase. \nUndo command to resume moving.", false);
                    }
                } else {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot undo this movement. Only the last command \n can be undone.", false);
                }

            }
        });


        // If the unit can trade show the trade image
        if ((unitType == SHIP && NavyStore.getInstance().isTradeShip(NavyStore.getInstance().getShipById(unitId)))
                || unitType == BAGGAGETRAIN
                || (unitType == FLEET && MiscCalculators.getFleetInfo(NavyStore.getInstance().getIdFleetMap().get(unitId)).getMerchantShips() > 0)) {
            tradeImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButLoadOff.png");
            tradeImg.addClickHandler(new ClickHandler() {

                public void onClick(final ClickEvent event) {
                    if (unitType == FLEET) {
                        ShipDTO firstTrade = null;
                        for (ShipDTO ship : NavyStore.getInstance().getIdFleetMap().get(unitId).getShips().values()) {
                            if (NavyStore.getInstance().isTradeShip(ship)) {
                                firstTrade = ship;
                                break;
                            }
                        }

                        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(
                                new TradePanelView(firstTrade, MovementMenuGroup.this, SHIP));

                    } else if (unitType == SHIP) {
                        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(
                                new TradePanelView(NavyStore.getInstance().getShipById(unitId), MovementMenuGroup.this, unitType));

                    } else {
                        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(
                                new TradePanelView(BaggageTrainStore.getInstance().getBaggageTMap().get(unitId), MovementMenuGroup.this, unitType));
                    }

                    hideMenu();
                }
            });
            tradeImg.setTitle("Trade");
            tradeImg.setStyleName("pointer");
        }
    }

    public void startNextStep() {
        if (isLast && ((getUnitType() != BAGGAGETRAIN && getUnitType() != SHIP && getUnitType() != FLEET)
                || !TradeStore.getInstance().hasInitSecondPhase(getUnitType(), unitId))) {
            if (!(getParentGroup().getTargetTiles().getVectorObjectCount() > 0)) {
                getParentGroup().getPossibleMovementTiles(position.getXStart(), position.getYStart(),
                        getMovementPoints(), position.getRegionId(), getUnitType(),
                        unitId, conqSectors, conqNeutralSectors);
            }
            getParentGroup().showTargetTiles();
            hideMenu();
        }
    }

    public void showMenu() {
        // Clearing to avoid umbrella exceptions
        this.show();
    }

    public void hideMenu() {
        // Adding the vector objects to the extended subGroup of menu
        this.hide();
    }

    public FiguresGroup getParentGroup() {
        return parentGroup;
    }

    /**
     * @return the MP
     */
    public int getMovementPoints() {
        return movementPoints;
    }

    /**
     * @return the unitType
     */
    public int getUnitType() {
        return unitType;
    }

    public void closeTradePanel() {
        // do nothing
    }

    public void setLast(final boolean value) {
        isLast = value;
    }

}
