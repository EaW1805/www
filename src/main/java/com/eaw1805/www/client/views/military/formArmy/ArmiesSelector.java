package com.eaw1805.www.client.views.military.formArmy;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.www.client.events.units.UnitCreatedEvent;
import com.eaw1805.www.client.events.units.UnitCreatedHandler;
import com.eaw1805.www.client.events.units.UnitDestroyedEvent;
import com.eaw1805.www.client.events.units.UnitDestroyedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.ArmyInfoMini;
import com.eaw1805.www.client.widgets.DelayIterator;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;

import java.util.ArrayList;
import java.util.List;

public class ArmiesSelector extends AbsolutePanel implements ArmyConstants {
    private ScrollPanel armyScrollPanel;
    private VerticalPanel freeArmyContainer;
    private ImageButton freeArmyUp;
    private ImageButton freeArmyDown;
    private FormArmyView formArmyView;
    private ImageButton newArmyImg;
    private AbsolutePanel absolutePanel;
    private final MovementStore mvStore = MovementStore.getInstance();
    private final UnitCreatedHandler unitCreatedHandler;
    private final UnitDestroyedHandler unitDestroyedHandler;


    public ArmiesSelector(List<ArmyDTO> armies, final SectorDTO sector, FormArmyView formArmyView) {
        this.formArmyView = formArmyView;
        setSize("180px", "544px");

        this.absolutePanel = new AbsolutePanel();
        this.absolutePanel.setStyleName("verticalListPanel");
        add(this.absolutePanel, 0, 22);
        this.absolutePanel.setSize("180px", "500px");
        this.armyScrollPanel = new ScrollPanel();
        this.absolutePanel.add(this.armyScrollPanel, 5, 5);
        armyScrollPanel.setStyleName("noScrollBars");
        this.armyScrollPanel.setSize("170px", "490px");

        this.freeArmyContainer = new VerticalPanel();
        armyScrollPanel.setWidget(this.freeArmyContainer);
        this.freeArmyContainer.setSize("170px", "89px");

        this.freeArmyUp = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowUpOff.png");
        this.freeArmyUp.setStyleName("pointer");
        this.add(this.freeArmyUp, 0, 0);
        this.freeArmyUp.setSize("180px", "22px");

        this.freeArmyDown = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowDownOff.png");
        this.freeArmyDown.setStyleName("pointer");
        this.add(this.freeArmyDown, 0, 522);
        this.freeArmyDown.setSize("180px", "22px");
        initArmies(armies);

        unitCreatedHandler = new UnitCreatedHandler() {
            @Override
            public void onUnitCreated(UnitCreatedEvent event) {
                if (event.getInfoType() == ARMY) {
                    initArmies(ArmyStore.getInstance().getArmiesBySector(sector, true));
                }
            }
        };
        unitDestroyedHandler = new UnitDestroyedHandler() {
            public void onUnitDestroyed(UnitDestroyedEvent event) {
                if (event.getInfoType() == ARMY) {
                    initArmies(ArmyStore.getInstance().getArmiesBySector(sector, true));
                }
            }
        };

        UnitEventManager.addUnitCreatedHandler(unitCreatedHandler);

        UnitEventManager.addUnitDestroyedHandler(unitDestroyedHandler);

        addScrollingFunctionality(freeArmyDown, freeArmyUp, armyScrollPanel);

    }

    public void removeGWTHandlers() {
        UnitEventManager.removeUnitCreatedHandler(unitCreatedHandler);
        UnitEventManager.removeUnitDestroyedHandler(unitDestroyedHandler);
    }

    private void initArmies(final List<ArmyDTO> armies) {
        freeArmyContainer.clear();
        this.newArmyImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButNewArmyOff.png");
        this.newArmyImg.setStyleName("pointer");
        this.freeArmyContainer.add(this.newArmyImg);
        this.newArmyImg.setSize("170px", "30px");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                ArmiesSelector.this.formArmyView.createNewArmy();
                newArmyImg.deselect();
                event.stopPropagation();
            }
        }).addToElement(newArmyImg.getElement()).register();

        if (armies != null && armies.size() > 0) {
            final List<ArmyDTO> movedArmies = new ArrayList<ArmyDTO>();

            new DelayIterator(0, armies.size(), 1) {

                @Override
                public void executeStep() {
                    final ArmyDTO army = armies.get(ITERATE_INDEX);
                    if (army.getArmyId() != 0) {
                        if (!army.isUpgraded()
                                && !army.isUpgradedToElite()
                                && !army.isUpHeadcount()
                                && !mvStore.hasMovedThisTurn(ARMY, army.getArmyId())) {
                            final ArmyInfoMini armyInfo = new ArmyInfoMini(army, true);
                            armyInfo.getArmyInfoPanel().addClickHandler(new ClickHandler() {
                                public void onClick(ClickEvent event) {
                                    formArmyView.changeOldArmy(armyInfo.getArmy());

                                }
                            });
                            freeArmyContainer.add(armyInfo);
                        } else {
                            movedArmies.add(army);
                        }
                    }
                }

                @Override
                public void executeLast() {
                    for (ArmyDTO army : movedArmies) {
                        final ArmyInfoMini armyInfo = new ArmyInfoMini(army, false);
                        freeArmyContainer.add(armyInfo);
                    }
                }
            }.run();
        }

    }

    private void addScrollingFunctionality(final ImageButton fleetDown,
                                           final ImageButton fleetUp, final ScrollPanel fleetScroll) {
        final Timer timer = new Timer() {
            public void run() {
                fleetScroll.setVerticalScrollPosition(fleetScroll
                        .getVerticalScrollPosition() + 9);

            }
        };
        final Timer timer2 = new Timer() {
            public void run() {
                fleetScroll.setVerticalScrollPosition(fleetScroll
                        .getVerticalScrollPosition() - 9);

            }
        };

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                fleetScroll.setVerticalScrollPosition(fleetScroll
                        .getVerticalScrollPosition() - 9);
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
                fleetScroll.setVerticalScrollPosition(fleetScroll
                        .getVerticalScrollPosition() + 9);
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
        fleetScroll.addDomHandler(new MouseWheelHandler() {
            public void onMouseWheel(final MouseWheelEvent event) {
                if (event.getDeltaY() < 0) {
                    fleetScroll.setVerticalScrollPosition(fleetScroll.getVerticalScrollPosition() - 90);
                } else if (event.getDeltaY() > 0) {
                    fleetScroll.setVerticalScrollPosition(fleetScroll.getVerticalScrollPosition() + 90);
                }
            }
        }, MouseWheelEvent.getType());
    }
}
