package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import org.vaadin.gwtgraphics.client.Group;

/**
 * Simulates the tool tip behavior with fixed look n feel.
 */
public abstract class ToolTipPanel
        extends PopupPanel {

    private Widget tooltipElement;
    private final FlowPanel tooltipContainer;
    private int offsetX = 0;
    private int offsetY = 70;

    public void setTooltip(final Widget tooltip) {
        tooltipContainer.clear();
        this.tooltipElement = tooltip;
        //if it is null... hide yourself..
        if (tooltip == null) {
            this.hide();
        } else {
            tooltipContainer.add(tooltip);
        }
    }

    boolean lockOpen = false;
    boolean openImmediately = false;

    public void setLockOpen(boolean value) {
        lockOpen = value;
    }

    public void setOpenImmediately(boolean value) {
        openImmediately = value;
    }


    protected final Timer t;

    public ToolTipPanel(final Widget widget, boolean backgroundColor) {
        setStyleName("none");

        final ToolTipPanel myself = this;
        final FlowPanel container = new FlowPanel();
        tooltipContainer = new FlowPanel();

        getElement().getStyle().setZIndex(100000);
        container.add(tooltipContainer);

        final int delay = 500;//in milliseconds

        //set style attributes programmaticaly..
        //css didn't work for some reason
        if (backgroundColor) {
            tooltipContainer.getElement().getStyle().setBackgroundColor("#ffffff");
        }
        // tooltipContainer.getElement().getStyle().setPadding(3.0, Style.Unit.PX);

        add(container);

        final Image pointer = new Image("");
        final AbsolutePanel spacer = new AbsolutePanel();
        spacer.setSize("5px", "5px");
        pointer.setSize("20px", "20px");
        this.setAnimationEnabled(true);
        //set the position to a place not visible for the user

        this.addDomHandler(new MouseMoveHandler() {
            public void onMouseMove(final MouseMoveEvent event) {
                //just be sure IF pointer goes on top of tooltip it will disapear
                //this can help if tooltip lose an event where it should hide itself.
                myself.hide();
            }
        }, MouseMoveEvent.getType());

        t = new Timer() {
            public void run() {
                if (tooltipElement == null) {
                    generateTip();
                }
                if (tooltipElement != null) {
                    if (widget instanceof Group || widget instanceof org.vaadin.gwtgraphics.client.Image) {
                        myself.setPopupPosition(0, 0);
                        myself.show();
                        myself.setPopupPosition(widget.getAbsoluteLeft() + offsetX, widget.getAbsoluteTop() + offsetY);
                    } else {
                        myself.showRelativeTo(widget);
                        if (widget.getAbsoluteTop() > myself.getAbsoluteTop()) {
                            pointer.setUrl("http://static.eaw1805.com/images/buttons/arrows/pop_down.png");
                            if (container.getWidgetCount() > 0) {
                                container.clear();
                            }
                            container.add(tooltipContainer);
                            container.add(spacer);
                            myself.setPopupPosition(myself.getAbsoluteLeft(), myself.getAbsoluteTop());

                        } else {
                            pointer.setUrl("http://static.eaw1805.com/images/buttons/arrows/pop_up.png");
                            if (container.getWidgetCount() > 0) {
                                container.clear();
                            }
                            container.add(spacer);
                            container.add(tooltipContainer);
                            myself.setPopupPosition(myself.getAbsoluteLeft(), myself.getAbsoluteTop());
                        }

                        if (widget.getAbsoluteLeft() == myself.getAbsoluteLeft()) {
                            container.getElement().setAttribute("align", "left");
                        } else {
                            container.getElement().setAttribute("align", "right");//setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                        }

                        //now correct the positioning..
                        myself.showRelativeTo(widget);
                    }
                }
            }
        };

        widget.addDomHandler(new MouseMoveHandler() {
            public void onMouseMove(final MouseMoveEvent event) {
                if (tooltipElement == null) {
                    generateTip();
                }
                if (tooltipElement != null) {
                    if (openImmediately) {
                        t.run();
                    } else {
                        t.schedule(delay);
                    }

                }
            }
        }, MouseMoveEvent.getType());

        widget.addDomHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                myself.hide();
                t.cancel();
            }
        }, MouseOutEvent.getType());

        widget.addDomHandler(new MouseDownHandler() {
            public void onMouseDown(final MouseDownEvent event) {
                if (!lockOpen) {
                    myself.hide();
                    t.cancel();
                }
            }
        }, MouseDownEvent.getType());

        if (widget instanceof Group) {
            widget.addAttachHandler(new AttachEvent.Handler() {
                @Override
                public void onAttachOrDetach(AttachEvent attachEvent) {
                    if (!attachEvent.isAttached()) {
                        hide();
                        t.cancel();
                    }
                }
            });
        }

        widget.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                if (!event.isAttached()) {
                    hide();
                    t.cancel();
                }
            }
        });
    }

    public void setOffsets(final int x, final int y) {
        offsetX = x;
        offsetY = y;
    }

    public abstract void generateTip();

}
