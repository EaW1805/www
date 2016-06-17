package com.eaw1805.www.client.views.military.formFleet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
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
import com.eaw1805.www.client.events.units.UnitCreatedEvent;
import com.eaw1805.www.client.events.units.UnitCreatedHandler;
import com.eaw1805.www.client.events.units.UnitDestroyedEvent;
import com.eaw1805.www.client.events.units.UnitDestroyedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.FleetInfoMini;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.ArrayList;
import java.util.List;

public class FleetSelector extends AbsolutePanel implements ArmyConstants {
    private ScrollPanel shipScrollPanel;
    private VerticalPanel fleetContainer;
    private ImageButton fleetsDown;
    private ImageButton fleetsUp;
    private AbsolutePanel shipBackPanel;
    private FormFleetView formFleetView;
    private MovementStore mvStore = MovementStore.getInstance();

    public FleetSelector(final FormFleetView formFleetView) {
        setSize("180px", "544px");
        this.formFleetView = formFleetView;
        this.shipBackPanel = new AbsolutePanel();
        this.shipBackPanel.setStyleName("verticalListPanel");
        add(this.shipBackPanel, 0, 22);
        this.shipBackPanel.setSize("180px", "500px");
        this.shipScrollPanel = new ScrollPanel();
        this.shipBackPanel.add(this.shipScrollPanel, 5, 5);
        shipScrollPanel.setStyleName("noScrollBars");
        this.shipScrollPanel.setSize("170px", "490px");

        this.fleetContainer = new VerticalPanel();
        shipScrollPanel.setWidget(this.fleetContainer);
        this.fleetContainer.setSize("170px", "104px");

        this.fleetsDown = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowDownOff.png");
        this.fleetsDown.setStyleName("pointer");
        this.add(this.fleetsDown, 0, 522);
        this.fleetsDown.setSize("180px", "22px");

        this.fleetsUp = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowUpOff.png");
        this.fleetsUp.setStyleName("pointer");
        this.add(this.fleetsUp, 0, 0);
        this.fleetsUp.setSize("180px", "22px");

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


        UnitEventManager.addUnitCreatedHandler(new UnitCreatedHandler() {
            public void onUnitCreated(UnitCreatedEvent event) {
                if (event.getInfoType() == FLEET) {
                    initFleets();
                }
            }
        });

        UnitEventManager.addUnitDestroyedHandler(new UnitDestroyedHandler() {
            public void onUnitDestroyed(UnitDestroyedEvent event) {
                if (event.getInfoType() == FLEET) {
                    initFleets();
                }
            }
        });

        addScrollingFunctionality(fleetsDown, fleetsUp, shipScrollPanel);
    }

    public void initFleets() {
        List<FleetDTO> fleets = NavyStore.getInstance().getFleetsByRegionAndTile(formFleetView.getSector(), false, true);
        fleetContainer.clear();
        ImageButton newFleetImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButNewFleetOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                formFleetView.selectFleet(null);
            }
        }).addToElement(newFleetImg.getElement()).register();

        newFleetImg.setSize("170px", "30px");
        fleetContainer.add(newFleetImg);
        List<FleetDTO> movedFleet = new ArrayList<FleetDTO>();
        for (FleetDTO fleet : fleets) {
            if (fleet.getFleetId() != 0) {
                if (mvStore.hasMovedThisTurn(FLEET, fleet.getFleetId())) {
                    movedFleet.add(fleet);
                } else {
                    final FleetInfoMini fleetPanel = new FleetInfoMini(fleet, true);
                    fleetPanel.addDomHandler(new ClickHandler() {
                        public void onClick(ClickEvent event) {
                            formFleetView.selectFleet(fleetPanel.getFleet());
                            for (int i = 1; i < fleetContainer.getWidgetCount(); i++) {
                                ((FleetInfoMini) fleetContainer.getWidget(i)).deselect();
                            }
                            fleetPanel.select();
                        }
                    }, ClickEvent.getType());
                    fleetPanel.addDomHandler(new MouseOverHandler() {
                        public void onMouseOver(MouseOverEvent event) {
                            fleetPanel.MouseOver();
                        }
                    }, MouseOverEvent.getType());
                    fleetPanel.addDomHandler(new MouseOutHandler() {
                        public void onMouseOut(MouseOutEvent event) {
                            fleetPanel.MouseOut();
                        }
                    }, MouseOutEvent.getType());
                    fleetContainer.add(fleetPanel);
                }
            }
        }
        for (FleetDTO fleet : movedFleet) {
            final FleetInfoMini fleetPanel = new FleetInfoMini(fleet, false);
            fleetContainer.add(fleetPanel);
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

}
