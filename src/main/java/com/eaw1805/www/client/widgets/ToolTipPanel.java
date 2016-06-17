package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 * Simulates the tool tip behavior with fixed look n feel.
 */
public abstract class ToolTipPanel
        extends PopupPanel {

    private Widget tooltipElement;
    private final FlowPanel tooltipContainer;

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

    public ToolTipPanel(final Widget widget) {
        setStyleName("none");

        final ToolTipPanel myself = this;
        final FlowPanel container = new FlowPanel();
        tooltipContainer = new FlowPanel();

        getElement().getStyle().setZIndex(1000);
        container.add(tooltipContainer);

        final int delay = 500;//in milliseconds

        //set style attributes programmaticaly..
        //css didn't work for some reason
        tooltipContainer.getElement().getStyle().setBackgroundColor("#ffffff");
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

        final Timer t = new Timer() {
            public void run() {
                if (tooltipElement == null) {
                    generateTip();
                }
                if (tooltipElement != null) {
                    myself.myShowRelativeTo(widget);
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
                    myself.myShowRelativeTo(widget);
                }
            }
        };

        widget.addDomHandler(new MouseMoveHandler() {
            public void onMouseMove(final MouseMoveEvent event) {
                if (tooltipElement == null) {
                    generateTip();
                }
                if (tooltipElement != null) {
                    t.schedule(delay);
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
                myself.hide();
                t.cancel();
            }
        }, MouseDownEvent.getType());
    }

    public void myShowRelativeTo(final UIObject widget) {
        super.showRelativeTo(widget);
        //then try to fix the position depending on the size of the popup
        if (getAbsoluteTop() < 0) {
            setPopupPosition(getAbsoluteLeft() + widget.getOffsetWidth(), 0);
        }

        if (getAbsoluteTop() + getOffsetHeight() > Window.getClientHeight()) {
            setPopupPosition(getAbsoluteLeft() + widget.getOffsetWidth(), Window.getClientHeight() - getOffsetHeight() - 10);
        }

        if (getAbsoluteLeft() < 0) {
            setPopupPosition(widget.getAbsoluteLeft() + widget.getOffsetWidth(), getAbsoluteTop());
        }
        if (getAbsoluteLeft() + getOffsetWidth() > Window.getClientWidth()) {
            setPopupPosition(widget.getAbsoluteLeft() - getOffsetWidth(), getAbsoluteTop());
        }
    }

    public abstract void generateTip();

}
