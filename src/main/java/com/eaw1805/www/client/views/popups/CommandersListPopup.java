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
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.ArmyInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.CorpsInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.mini.CommanderInfoMini;
import com.eaw1805.www.client.views.military.formArmy.FormArmy;
import com.eaw1805.www.client.views.military.formCorp.FormCorp;
import com.eaw1805.www.client.views.popups.menus.ArmyMenu;
import com.eaw1805.www.client.views.popups.menus.CorpsMenu;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;

import java.util.List;


public class CommandersListPopup extends PopupPanelEAW implements StyleConstants {
    private final AbsolutePanel basePanel = new AbsolutePanel();
    private final ScrollPanel commandersScrollPanel = new ScrollPanel();
    private final VerticalPanel commandersVPanel = new VerticalPanel();
    private List<CommanderDTO> commanders;
    private final AbsolutePanel noCommandersPanel = new AbsolutePanel();
    private CommanderDTO selCommander;
    private int index;
    private final ImageButton upImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowUpOff.png");
    private final ImageButton downImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowDownOff.png");

    public CommandersListPopup(final List<CommanderDTO> commanders, final Widget parent, final int index) {
        this.setStylePrimaryName("none");
        this.getElement().getStyle().setZIndex(100000);
        this.add(basePanel);
        this.setAutoHideEnabled(true);
        setCommanders(commanders);
        this.index = index;
        basePanel.setStyleName("selectorPopup");
        basePanel.setSize("220px", "500px");

        final Label lblSelected = new Label("Select Commander");
        lblSelected.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblSelected.setStyleName("panelOut whiteText");
        lblSelected.setSize("220px", "30px");
        basePanel.add(lblSelected, 15, 15);

        commandersScrollPanel.setAlwaysShowScrollBars(true);
        commandersScrollPanel.setStyleName("noScrollBars");
        commandersScrollPanel.setSize(SIZE_171PX, "366px");
        commandersScrollPanel.setWidget(commandersVPanel);
        basePanel.add(commandersScrollPanel, 14, 83);

        commandersVPanel.setSize(SIZE_171PX, "48px");

        upImg.setStyleName("pointer");
        upImg.setSize(SIZE_171PX, "24px");
        basePanel.add(upImg, 14, 54);

        downImg.setStyleName("pointer");
        downImg.setSize(SIZE_171PX, "24px");
        basePanel.add(downImg, 14, 453);

        setUpCommandersVPanel(getCommanders(), parent);
        addScrollingFunctionality(downImg, upImg, commandersScrollPanel);
    }

    public final void setUpCommandersVPanel(final List<CommanderDTO> commanders, final Widget parent) {
        getCommandersVPanel().clear();
        for (CommanderDTO commander : commanders) {
            // If the commander was not dismissed this turn
            if ((commander.getInPool() && commander.getStartInPool()) || (!commander.getInPool() && !commander.getStartInPool())) {
                final CommanderInfoMini comInfoMini = new CommanderInfoMini(commander);
                commandersVPanel.add(comInfoMini);
                comInfoMini.addDomHandler(new ClickHandler() {
                    public void onClick(final ClickEvent event) {
                        for (int i = 0; i < commandersVPanel.getWidgetCount(); i++) {
                            ((CommanderInfoMini) commandersVPanel.getWidget(i)).deselect();
                        }
                        selCommander = comInfoMini.getCommander();
                        comInfoMini.select();
                        if (getSelCommander() != null) {
                            if (parent.getClass().equals(FormCorp.class)) {
                                ((FormCorp) parent).setSelCommander(getSelCommander());
                            } else if (parent.getClass().equals(FormArmy.class)) {
                                ((FormArmy) parent).setSelCommander(getSelCommander());
                            } else if (parent.getClass().equals(ArmyInfoPanel.class)) {
                                ((ArmyInfoPanel) parent).setCommander(getSelCommander());
                            } else if (parent.getClass().equals(CorpsInfoPanel.class)) {
                                ((CorpsInfoPanel) parent).setCommander(getSelCommander());
                            } else if (parent.getClass().equals(ArmyMenu.class)) {
                                ((ArmyMenu) parent).setCommander(getSelCommander());
                            } else if (parent.getClass().equals(CorpsMenu.class)) {
                                ((CorpsMenu) parent).setCommander(getSelCommander());
                            }
                        }
                        CommandersListPopup.this.hide();
                    }
                }, ClickEvent.getType());

                comInfoMini.addDomHandler(new MouseOverHandler() {

                    public void onMouseOver(MouseOverEvent event) {
                        comInfoMini.MouseOver();

                    }
                }, MouseOverEvent.getType());

                comInfoMini.addDomHandler(new MouseOutHandler() {
                    public void onMouseOut(MouseOutEvent event) {
                        comInfoMini.MouseOut();

                    }
                }, MouseOutEvent.getType());
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

    public final VerticalPanel getCommandersVPanel() {
        return commandersVPanel;
    }

    public final List<CommanderDTO> getCommanders() {
        return commanders;
    }

    public final void setCommanders(final List<CommanderDTO> commanders) {
        this.commanders = commanders;
    }

    public final CommanderDTO getSelCommander() {
        return selCommander;
    }

}
