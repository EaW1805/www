package com.eaw1805.www.client.views.popups;

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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.FleetInfoMini;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.List;

public class FleetsListPopup extends AbsolutePanel {
    private final ScrollPanel fleetsScrollPanel = new ScrollPanel();
    private final VerticalPanel fleetsVPanel = new VerticalPanel();
    private List<FleetDTO> fleets;
    private final AbsolutePanel noFleetsPanel = new AbsolutePanel();
    private int selFleet = -1, index;
    private final Label lblSelectFleet;
    private final ImageButton upImg;
    private final ImageButton downImg;
    private final FleetDTO fleet2;

    public FleetsListPopup(final SectorDTO sector,
                           final com.eaw1805.www.client.views.military.exchShips.ExchangeShipsView exchView, final int index, final FleetDTO fleet2) {
        this.getElement().getStyle().setZIndex(3);
        this.index = index;
        this.fleet2 = fleet2;
        fleets = NavyStore.getInstance().getFleetsByRegionAndTile(sector, false, true);
        this.setStyleName("selectorPopup");
        this.setSize("220px", "500px");
        this.add(fleetsScrollPanel, 15, 83);
        fleetsScrollPanel.setAlwaysShowScrollBars(true);
        fleetsScrollPanel.setStyleName("noScrollBars");
        fleetsScrollPanel.setSize("171px", "366px");
        fleetsScrollPanel.setWidget(fleetsVPanel);
        fleetsVPanel.setSize("170px", "39px");

        this.lblSelectFleet = new Label("Select fleet:");
        this.lblSelectFleet.setStyleName("clearFontMedLarge whiteText");
        this.add(this.lblSelectFleet, 15, 14);
        this.lblSelectFleet.setSize("220px", "27px");

        this.upImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowUpOff.png");
        this.upImg.setStyleName("pointer");
        add(this.upImg, 15, 54);
        this.upImg.setSize("171px", "24px");

        this.downImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowDownOff.png");
        this.downImg.setStyleName("pointer");
        add(this.downImg, 15, 454);
        this.downImg.setSize("171px", "24px");

        addScrollingFunctionality(downImg, upImg, fleetsScrollPanel);
        setUpFleetsVPanel(fleets, exchView);
    }

    public final void setUpFleetsVPanel(final List<FleetDTO> fleets, final com.eaw1805.www.client.views.military.exchShips.ExchangeShipsView parent) {
        getFleetsVPanel().clear();
        final FleetInfoMini[] aiPanl = new FleetInfoMini[fleets.size()];
        int index = 0;
        if (fleets.isEmpty()) {
            fleetsVPanel.add(noFleetsPanel);
            noFleetsPanel.setHeight("501px");

            final Label lblNo = new Label("No");
            lblNo.setStyleName("gwt-empireLabel");
            noFleetsPanel.add(lblNo, 91, 151);
            lblNo.setSize("63px", "48px");

            final Label lblFleets_1 = new Label("Fleets");
            lblFleets_1.setStyleName("gwt-empireLabel");
            noFleetsPanel.add(lblFleets_1, 82, 198);

            final Label lblFound = new Label("Found!");
            lblFound.setStyleName("gwt-empireLabel");
            noFleetsPanel.add(lblFound, 82, 252);
        } else {
            for (FleetDTO fleet : fleets) {
                if (fleet.getFleetId() != 0 && (fleet2 == null || fleet.getFleetId() != fleet2.getFleetId())) {
                    final int idx = index;
                    aiPanl[index] = new FleetInfoMini(fleet, true);
                    aiPanl[index].addDomHandler(new ClickHandler() {
                        public void onClick(final ClickEvent event) {
                            for (int i = 0; i < aiPanl.length; i++) {
                                if (aiPanl[i] != null) {
                                    aiPanl[i].deselect();
                                }
                            }
                            aiPanl[idx].select();
                            // Set selected fleet and save it to return on accept
                            selFleet = aiPanl[idx].getFleet().getFleetId();
                            if (getSelFleet() != -1) {
                                parent.setFleetSelected(selFleet, FleetsListPopup.this.index);
                            }
                            ((PopupPanel) FleetsListPopup.this.getParent()).hide();
                        }
                    }, ClickEvent.getType());
                    aiPanl[index].addDomHandler(new MouseOverHandler() {
                        public void onMouseOver(final MouseOverEvent event) {
                            aiPanl[idx].MouseOver();
                        }
                    }, MouseOverEvent.getType());
                    aiPanl[index].addDomHandler(new MouseOutHandler() {
                        public void onMouseOut(final MouseOutEvent event) {
                            aiPanl[idx].MouseOut();
                        }
                    }, MouseOutEvent.getType());
                    getFleetsVPanel().add(aiPanl[index]);
                    index++;
                }
            }
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
            public void onMouseDown(final MouseDownEvent event) {
                timer2.scheduleRepeating(1);

            }
        });
        fleetUp.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
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
                        .getHorizontalScrollPosition() + 3);
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
    }

    public ScrollPanel getFleetsScrollPanel() {
        return fleetsScrollPanel;
    }

    public VerticalPanel getFleetsVPanel() {
        return fleetsVPanel;
    }

    public List<FleetDTO> getFleets() {
        return fleets;
    }

    public void setFleets(final List<FleetDTO> fleetList) {
        this.fleets = fleetList;
    }

    public AbsolutePanel getNoFleetsPanel() {
        return noFleetsPanel;
    }

    public void setSelFleet(final int selFleet) {
        this.selFleet = selFleet;
    }

    public int getSelFleet() {
        return selFleet;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
