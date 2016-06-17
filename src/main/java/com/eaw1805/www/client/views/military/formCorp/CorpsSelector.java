package com.eaw1805.www.client.views.military.formCorp;

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
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.events.units.UnitCreatedEvent;
import com.eaw1805.www.client.events.units.UnitCreatedHandler;
import com.eaw1805.www.client.events.units.UnitDestroyedEvent;
import com.eaw1805.www.client.events.units.UnitDestroyedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.CorpsInfoMini;
import com.eaw1805.www.client.widgets.DelayIterator;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;

import java.util.ArrayList;
import java.util.List;


public class CorpsSelector extends AbsolutePanel implements ArmyConstants {
    private ScrollPanel corpScrollPanel;
    private VerticalPanel freeCorpContainer;
    private ImageButton freeCorpDown;
    private ImageButton freeCorpUp;
    private FormCorpView formCorpView;
    private AbsolutePanel absolutePanel;
    private MovementStore mvStore = MovementStore.getInstance();
    private UnitDestroyedHandler unitDestroyedHandler;
    private UnitCreatedHandler unitChangedHandler;

    public CorpsSelector(List<CorpDTO> corps, final SectorDTO sector, FormCorpView formCorpView) {
        this.formCorpView = formCorpView;
        setSize("180px", "544px");

        this.absolutePanel = new AbsolutePanel();
        this.absolutePanel.setStyleName("verticalListPanel");
        add(this.absolutePanel, 0, 22);
        this.absolutePanel.setSize("180px", "500px");
        this.corpScrollPanel = new ScrollPanel();
        this.absolutePanel.add(this.corpScrollPanel, 5, 5);
        corpScrollPanel.setStyleName("noScrollBars");
        this.corpScrollPanel.setSize("170px", "490px");

        this.freeCorpContainer = new VerticalPanel();
        corpScrollPanel.setWidget(this.freeCorpContainer);
        this.freeCorpContainer.setSize("148px", "27px");

        this.freeCorpDown = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowDownOff.png");
        this.freeCorpDown.setStyleName("pointer");
        this.add(this.freeCorpDown, 0, 522);
        this.freeCorpDown.setSize("180px", "22px");

        this.freeCorpUp = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowUpOff.png");
        this.freeCorpUp.setStyleName("pointer");
        this.add(this.freeCorpUp, 0, 0);
        this.freeCorpUp.setSize("180px", "22px");
        initCorps(corps);

        addScrollingFunctionality(freeCorpDown, freeCorpUp, corpScrollPanel);
        unitChangedHandler = new UnitCreatedHandler() {
            @Override
            public void onUnitCreated(UnitCreatedEvent event) {
                if (event.getInfoType() == CORPS) {
                    List<ArmyDTO> armies = ArmyStore.getInstance().getArmiesBySector(sector, true);
                    List<CorpDTO> corps = new ArrayList<CorpDTO>();
                    if (armies != null && armies.size() > 0) {
                        for (ArmyDTO army : armies) {
                            if (army.getCorps() != null && army.getCorps().size() > 0) {
                                corps.addAll(new ArrayList<CorpDTO>(army.getCorps().values()));
                            }
                        }
                    }
                    initCorps(corps);
                }
            }
        };
        UnitEventManager.addUnitCreatedHandler(unitChangedHandler);
        unitDestroyedHandler = new UnitDestroyedHandler() {
            public void onUnitDestroyed(final UnitDestroyedEvent event) {
                if (event.getInfoType() == CORPS) {
                    List<ArmyDTO> armies = ArmyStore.getInstance().getArmiesBySector(sector, true);
                    List<CorpDTO> corps = new ArrayList<CorpDTO>();
                    if (armies != null && armies.size() > 0) {
                        for (ArmyDTO army : armies) {
                            if (army.getCorps() != null && army.getCorps().size() > 0) {
                                corps.addAll(new ArrayList<CorpDTO>(army.getCorps().values()));
                            }
                        }
                    }
                    initCorps(corps);
                }
            }
        };
        UnitEventManager.addUnitDestroyedHandler(unitDestroyedHandler);

    }

    public void removeGWTHandler() {
        UnitEventManager.removeUnitDestroyedHandler(unitDestroyedHandler);
        UnitEventManager.removeUnitCreatedHandler(unitChangedHandler);
    }

    private void initCorps(final List<CorpDTO> corps) {
        freeCorpContainer.clear();

        final ImageButton newCorpImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButNewCorpsOff.png");
        newCorpImg.setSize("170px", "30px");
        newCorpImg.setTitle("New corps");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                CorpsSelector.this.formCorpView.createNewCorp();
            }
        }).addToElement(newCorpImg.getElement()).register();

        newCorpImg.setStyleName("pointer");
        this.freeCorpContainer.add(newCorpImg);

        final List<CorpDTO> moveCorps = new ArrayList<CorpDTO>();

        if (corps != null && corps.size() > 0) {
            new DelayIterator(0, corps.size(), 1) {

                @Override
                public void executeStep() {
                    final CorpDTO corp = corps.get(ITERATE_INDEX);
                    if (corp.getCorpId() != 0) {
                        if (corp.isUpgraded()
                                || corp.isUpgradedToElite()
                                || corp.isUpHeadcount()
                                || mvStore.hasMovedThisTurn(CORPS, corp.getCorpId())
                                || mvStore.hasMovedThisTurn(ARMY, corp.getArmyId())) {
                            moveCorps.add(corp);

                        } else {
                            final CorpsInfoMini corpInfo = new CorpsInfoMini(corp, true);
                            corpInfo.getCorpInfoPanel().addClickHandler(new ClickHandler() {
                                public void onClick(ClickEvent event) {
                                    formCorpView.changeOldCorp(corpInfo.getCorp());

                                }
                            });
                            freeCorpContainer.add(corpInfo);
                        }

                    }
                }

                @Override
                public void executeLast() {
                    for (CorpDTO corp : moveCorps) {
                        final CorpsInfoMini corpInfo = new CorpsInfoMini(corp, false);
                        freeCorpContainer.add(corpInfo);
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
