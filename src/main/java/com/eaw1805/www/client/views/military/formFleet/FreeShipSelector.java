package com.eaw1805.www.client.views.military.formFleet;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.ShipInfoMini;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.ArrayList;
import java.util.List;

public class FreeShipSelector extends AbsolutePanel implements ArmyConstants {
    private ScrollPanel shipScrollPanel;
    private VerticalPanel freeShipContainer;
    private ImageButton freeShipsDown;
    private ImageButton freeShipsUp;
    private PickupDragController dragController;
    private AbsolutePanel shipBackPanel;
    private final FormFleetView formFleetView;
    private final MovementStore mvStore = MovementStore.getInstance();
    private final UnitChangedHandler unitChangedHandler;

    public FreeShipSelector(final FormFleetView formFleetView, PickupDragController dragController) {
        this.dragController = dragController;
        this.formFleetView = formFleetView;
        setSize("180px", "544px");

        this.shipBackPanel = new AbsolutePanel();
        this.shipBackPanel.setStyleName("verticalListPanel");
        add(this.shipBackPanel, 0, 22);
        this.shipBackPanel.setSize("180px", "500px");
        this.shipScrollPanel = new ScrollPanel();
        this.shipBackPanel.add(this.shipScrollPanel, 5, 5);
        shipScrollPanel.setStyleName("noScrollBars");
        this.shipScrollPanel.setSize("170px", "490px");

        this.freeShipContainer = new VerticalPanel();
        shipScrollPanel.setWidget(this.freeShipContainer);
        this.freeShipContainer.setSize("170px", "104px");

        shipBackPanel.addDomHandler(new MouseWheelHandler() {
            public void onMouseWheel(MouseWheelEvent event) {
                if (event.getDeltaY() < 0) {
                    shipScrollPanel.setVerticalScrollPosition(
                            shipScrollPanel.getVerticalScrollPosition() - 89);
                } else {
                    shipScrollPanel.setVerticalScrollPosition(
                            shipScrollPanel.getVerticalScrollPosition() + 89);
                }
            }
        }, MouseWheelEvent.getType());

        DropController dp1 = new VerticalPanelDropController(freeShipContainer);
        //initShips(ships);
        dragController.registerDropController(dp1);

        this.freeShipsDown = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowDownOff.png");
        this.freeShipsDown.setStyleName("pointer");
        this.add(this.freeShipsDown, 0, 522);
        this.freeShipsDown.setSize("180px", "22px");

        this.freeShipsUp = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowUpOff.png");
        this.freeShipsUp.setStyleName("pointer");
        this.add(this.freeShipsUp, 0, 0);
        this.freeShipsUp.setSize("180px", "22px");


        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(UnitChangedEvent event) {
                if (event.getInfoType() == SHIP) {
                    initShips();
                }
            }
        };


        addScrollingFunctionality(freeShipsDown, freeShipsUp, shipScrollPanel);
    }

    public void onAttach() {
        super.onAttach();
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void initShips() {
        List<ShipDTO> ships = new ArrayList<ShipDTO>();
        for (FleetDTO fleet : NavyStore.getInstance().getFleetsByRegionAndTile(formFleetView.getSector(), false, true)) {
            if (fleet.getFleetId() == 0) {
                for (ShipDTO ship : fleet.getShips().values()) {
                    if (!formFleetView.getFormFleet().hasShip(ship)) {
                        ships.add(ship);
                    }
                }
            }
        }
        freeShipContainer.clear();
        int index = 1;
        List<ShipDTO> movedShips = new ArrayList<ShipDTO>();
        for (ShipDTO ship : ships) {
            if ((formFleetView.getFormFleet().getType() == FromFleet.Type.WAR && !NavyStore.getInstance().isTradeShip(ship)) ||
                    (formFleetView.getFormFleet().getType() == FromFleet.Type.MERCHANT && NavyStore.getInstance().isTradeShip(ship)) ||
                    (formFleetView.getFormFleet().getType() == FromFleet.Type.ALL)) {
                if (mvStore.hasMovedThisTurn(SHIP, ship.getId())) {
                    movedShips.add(ship);
                } else {
                    final ShipInfoMini shipPanel = new ShipInfoMini(ship, true, false);
                    shipPanel.addDomHandler(new DoubleClickHandler() {
                        public void onDoubleClick(DoubleClickEvent event) {
                            if (formFleetView.getFormFleet().addShipToFleet(shipPanel.getShip())) {
                                shipPanel.removeFromParent();
                            }
                        }
                    }, DoubleClickEvent.getType());
                    freeShipContainer.add(shipPanel);
                    dragController.makeDraggable(shipPanel, shipPanel.getShipPanel());
                    index++;
                }
            }
        }
        for (ShipDTO ship : movedShips) {
            final ShipInfoMini shipPanel = new ShipInfoMini(ship, false, false);
            freeShipContainer.add(shipPanel);
        }
    }

    private void addScrollingFunctionality(final ImageButton fleetDown,
                                           final ImageButton fleetUp, final ScrollPanel fleetScroll) {
        final Timer timer = new Timer() {
            public void run() {
                fleetScroll.setVerticalScrollPosition(fleetScroll
                        .getVerticalScrollPosition() + 3);

            }
        };
        final Timer timer2 = new Timer() {
            public void run() {
                fleetScroll.setVerticalScrollPosition(fleetScroll
                        .getVerticalScrollPosition() - 3);

            }
        };

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                fleetScroll.setHorizontalScrollPosition(fleetScroll
                        .getHorizontalScrollPosition() - 3);
                fleetUp.deselect();
            }
        }).addToElement(fleetUp.getElement()).register();

        fleetUp.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                timer2.scheduleRepeating(1);

            }
        });
        fleetUp.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                timer2.cancel();

            }
        });
        fleetUp.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(MouseUpEvent event) {
                timer2.cancel();
            }
        });

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                fleetScroll.setHorizontalScrollPosition(fleetScroll
                        .getHorizontalScrollPosition() + 3);
                fleetDown.deselect();
            }
        }).addToElement(fleetDown.getElement()).register();

        fleetDown.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                timer.scheduleRepeating(1);

            }
        });
        fleetDown.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                timer.cancel();

            }
        });
        fleetDown.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(MouseUpEvent event) {
                timer.cancel();
            }
        });
    }

    /**
     * @return the freeShipContainer
     */
    public VerticalPanel getFreeShipContainer() {
        return freeShipContainer;
    }

}
