package com.eaw1805.www.client.views.military.formCorp;

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
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.BrigadeInfoMini;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.DelayIterator;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.TransportStore;
import com.eaw1805.www.shared.stores.util.ClientUtil;

import java.util.ArrayList;
import java.util.List;

public class FreeBrigadesSelector extends AbsolutePanel implements ArmyConstants {
    private ScrollPanel brigScrollPanel;
    private VerticalPanel freeBrigContainer;
    private ImageButton freeBrigUp;
    private ImageButton freeBrigDown;
    private SectorDTO sector;
    private PickupDragController dragController;
    private AbsolutePanel absolutePanel;
    private final FormCorpView formCorpView;
    private final MovementStore mvStore = MovementStore.getInstance();
    private BrigadeInfoMini firstBrigadeInfo = null;

    public FreeBrigadesSelector(final List<BrigadeDTO> brigades, final PickupDragController dragController, final SectorDTO sector, final FormCorpView formCorpView) {
        setSize("180px", "544px");
        this.sector = sector;
        this.dragController = dragController;
        this.formCorpView = formCorpView;
        this.absolutePanel = new AbsolutePanel();
        this.absolutePanel.setStyleName("verticalListPanel");
        add(this.absolutePanel, 0, 22);
        this.absolutePanel.setSize("180px", "500px");
        this.brigScrollPanel = new ScrollPanel();
        this.absolutePanel.add(this.brigScrollPanel, 5, 5);
        brigScrollPanel.setStyleName("noScrollBars");
        this.brigScrollPanel.setSize("170px", "490px");

        this.freeBrigContainer = new VerticalPanel();
        this.brigScrollPanel.setWidget(this.freeBrigContainer);
        this.freeBrigContainer.setSize("100%", "44px");

        DropController dp = new VerticalPanelDropController(freeBrigContainer);


        this.freeBrigUp = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowUpOff.png");
        this.freeBrigUp.setStyleName("pointer");
        this.add(this.freeBrigUp, 0, 0);
        this.freeBrigUp.setSize("180px", "22px");

        this.freeBrigDown = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowDownOff.png");
        this.freeBrigDown.setStyleName("pointer");
        this.add(this.freeBrigDown, 0, 522);
        this.freeBrigDown.setSize("180px", "22px");

        if (brigades != null) {
            final List<BrigadeDTO> movedBrigs = new ArrayList<BrigadeDTO>();
            new DelayIterator(0, brigades.size(), 1) {

                @Override
                public void executeStep() {
                    final BrigadeDTO brigade = brigades.get(ITERATE_INDEX);
                    if (!mvStore.hasMovedThisTurn(BRIGADE, brigade.getBrigadeId())
                            && !brigade.getLoaded()
                            && !TransportStore.getInstance().hasUnitLoadOrder(BRIGADE, brigade.getBrigadeId())) {
                        final BrigadeInfoMini brigInfo = new BrigadeInfoMini(brigade, true);
                        if (firstBrigadeInfo == null) {
                            firstBrigadeInfo = brigInfo;
                        }
                        dragController.makeDraggable(brigInfo, brigInfo.getBrigadePanel());
                        freeBrigContainer.add(brigInfo);
                        brigInfo.addDomHandler(new DoubleClickHandler() {
                            public void onDoubleClick(DoubleClickEvent event) {
                                if (formCorpView.getFormCorpPanel().hasSpace()) {
                                    brigInfo.removeFromParent();
                                    formCorpView.getFormCorpPanel().addBrigade(brigInfo.getBrigade());
                                }

                            }
                        }, DoubleClickEvent.getType());
                    } else {
                        movedBrigs.add(brigade);
                    }
                }

                @Override
                public void executeLast() {
                    for (BrigadeDTO brigade : movedBrigs) {
                        freeBrigContainer.add(new BrigadeInfoMini(brigade, false));
                    }
                }
            }.run();
        }
        if (firstBrigadeInfo != null
                && TutorialStore.getInstance().isTutorialMode()
                && TutorialStore.getInstance().getMonth() == 8
                && TutorialStore.getInstance().getTutorialStep() == 3
                && !TutorialStore.getInstance().isBrigadeDraggedFlag()) {
            TutorialStore.highLightButton(firstBrigadeInfo);
        }

        addScrollingFunctionality(freeBrigDown, freeBrigUp, brigScrollPanel);
        dragController.registerDropController(dp);
    }

    int index;
    private Timer t;

    public void reInitBrigades() {
        freeBrigContainer.clear();
        final List<BrigadeDTO> brigades = ArmyStore.getInstance().getFreeBrigadesBySector(sector);

        if (brigades != null) {
            index = 0;
            ClientUtil.startSpeedTest("reinitbrigades2");
            final List<BrigadeDTO> movedBrigs = new ArrayList<BrigadeDTO>();
            t = new Timer() {
                @Override
                public void run() {
                    if (index < brigades.size()) {
                        final BrigadeDTO brigade = brigades.get(index);

                        if (!formCorpView.getFormCorpPanel().hasBrigade(brigade)) {//siriako
                            if (!mvStore.hasMovedThisTurn(BRIGADE, brigade.getBrigadeId())
                                    && !brigade.getLoaded()
                                    && !TransportStore.getInstance().hasUnitLoadOrder(BRIGADE, brigade.getBrigadeId())) {
                                final BrigadeInfoMini brigInfo = new BrigadeInfoMini(brigade, true);
                                brigInfo.addDomHandler(new DoubleClickHandler() {
                                    public void onDoubleClick(DoubleClickEvent event) {
                                        if (formCorpView.getFormCorpPanel().hasSpace()) {
                                            brigInfo.removeFromParent();
                                            formCorpView.getFormCorpPanel().addBrigade(brigInfo.getBrigade());
                                        }
                                    }
                                }, DoubleClickEvent.getType());
                                dragController.makeDraggable(brigInfo, brigInfo.getBrigadePanel());
                                freeBrigContainer.add(brigInfo);

                            } else {
                                movedBrigs.add(brigade);
                            }
                        }


                        index++;
                        t.schedule(10);
                    } else {
                        for (BrigadeDTO brigade : movedBrigs) {
                            freeBrigContainer.add(new BrigadeInfoMini(brigade, false));
                        }
                    }
                }
            };
            t.run();
            ClientUtil.stopSpeedTest("reinitbrigades2", "init1");
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
                fleetScroll.setHorizontalScrollPosition(fleetScroll
                        .getHorizontalScrollPosition() - 9);
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
                        .getHorizontalScrollPosition() + 9);
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
