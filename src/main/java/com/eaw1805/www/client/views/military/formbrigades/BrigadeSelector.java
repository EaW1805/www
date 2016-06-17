package com.eaw1805.www.client.views.military.formbrigades;

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
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.BrigadeInfoMini;
import com.eaw1805.www.client.widgets.DelayIterator;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.util.ClientUtil;

import java.util.ArrayList;
import java.util.List;

public class BrigadeSelector
        extends AbsolutePanel {

    private transient final VerticalPanel freeBrigContainer;
    private transient final FormBrigadesView formBrigadesView;
    private transient final int listNo;
    private BrigadeInfoMini remBrigPanel = null;
    private int remBrigPos = 0;
    private BrigadeDTO selectedBrigade = null;

    public BrigadeSelector(final List<BrigadeDTO> brigades,
                           final FormBrigadesView formBrigadesView,
                           final int listNo) {
        setSize("180px", "544px");
        this.listNo = listNo;
        this.formBrigadesView = formBrigadesView;

        final AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setStyleName("verticalListPanel");
        add(absolutePanel, 0, 22);
        absolutePanel.setSize("180px", "500px");

        final ScrollPanel brigScrollPanel = new ScrollPanel();
        absolutePanel.add(brigScrollPanel, 5, 5);
        brigScrollPanel.setStyleName("noScrollBars");
        brigScrollPanel.setSize("170px", "490px");

        this.freeBrigContainer = new VerticalPanel();
        this.freeBrigContainer.setSize("100%", "44px");
        brigScrollPanel.setWidget(this.freeBrigContainer);

        final ImageButton freeBrigUp = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowUpOff.png");
        freeBrigUp.setStyleName("pointer");
        freeBrigUp.setSize("180px", "22px");
        add(freeBrigUp, 0, 0);

        final ImageButton freeBrigDown = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowDownOff.png");
        freeBrigDown.setStyleName("pointer");
        freeBrigDown.setSize("180px", "22px");
        add(freeBrigDown, 0, 522);
        ClientUtil.startSpeedTest("brigadeselector");
        if (brigades != null) {
            final List<BrigadeDTO> disabledBrigs = new ArrayList<BrigadeDTO>();
            new DelayIterator(0, brigades.size(), 1) {
                @Override
                public void executeStep() {
                    final BrigadeDTO brigade = brigades.get(ITERATE_INDEX);
                    if (!brigade.getStartLoaded()
                            && !brigade.isUpgraded()
                            && !brigade.isUpgradedToElite()
                            && !brigade.IsUpHeadcount()) {
                        final BrigadeInfoMini brigInfo = new BrigadeInfoMini(brigade, true);
                        brigInfo.getBrigadePanel().setStyleName("pointer", true);
                        freeBrigContainer.add(brigInfo);
                        brigInfo.addDomHandler(new ClickHandler() {
                            public void onClick(final ClickEvent event) {
                                for (int i = 0; i < freeBrigContainer.getWidgetCount(); i++) {
                                    ((BrigadeInfoMini) freeBrigContainer.getWidget(i)).deselect();
                                }
                                selectedBrigade = brigade;
                                brigInfo.select();
                                BrigadeSelector.this.formBrigadesView.selectBrigade(BrigadeSelector.this.listNo, brigInfo.getBrigade());
                            }
                        }, ClickEvent.getType());

                        brigInfo.getBrigadePanel().addDomHandler(new MouseOverHandler() {
                            public void onMouseOver(final MouseOverEvent event) {
                                brigInfo.MouseOver();
                            }
                        }, MouseOverEvent.getType());

                        brigInfo.addDomHandler(new MouseOutHandler() {
                            public void onMouseOut(final MouseOutEvent event) {
                                brigInfo.MouseOut();
                            }
                        }, MouseOutEvent.getType());
                    } else {
                        disabledBrigs.add(brigade);
                    }
                }

                @Override
                public void executeLast() {
                    for (BrigadeDTO brigade : disabledBrigs) {
                        freeBrigContainer.add(new BrigadeInfoMini(brigade, false));
                    }
                }
            }.run();
        }
        ClientUtil.stopSpeedTest("brigadeselector", "excbrisel");
        addScrollingFunctionality(freeBrigDown, freeBrigUp, brigScrollPanel);
    }

    public void removeBrigade(final int brigadeId) {
        if (remBrigPanel != null) {
            freeBrigContainer.insert(remBrigPanel, remBrigPos);
        }

        for (int slot = 0; slot < freeBrigContainer.getWidgetCount(); slot++) {
            final BrigadeInfoMini brigInfo = (BrigadeInfoMini) freeBrigContainer.getWidget(slot);
            if (brigInfo.getBrigade().getBrigadeId() == brigadeId) {
                remBrigPanel = brigInfo;
                remBrigPos = slot;
                brigInfo.removeFromParent();
                break;
            }
        }
    }

    public void reSelectBrigade() {
        formBrigadesView.selectBrigade(listNo, selectedBrigade);
    }

    private void addScrollingFunctionality(final ImageButton fleetDown,
                                           final ImageButton fleetUp,
                                           final ScrollPanel fleetScroll) {
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
            public void onMouseDown(final MouseDownEvent event) {
                timer2.scheduleRepeating(1);

            }
        });

        fleetUp.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                timer2.cancel();

            }
        });

        fleetUp.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(final MouseUpEvent event) {
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
            public void onMouseDown(final MouseDownEvent event) {
                timer.scheduleRepeating(1);

            }
        });

        fleetDown.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                timer.cancel();

            }
        });

        fleetDown.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(final MouseUpEvent event) {
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
