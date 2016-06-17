package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Simulates the tool tip behavior with fixed look n feel.
 */
public abstract class MapTooltipPanel
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

    public MapTooltipPanel(final Widget widget, boolean backgroundColor) {
        setStyleName("none");

        final MapTooltipPanel myself = this;
        setAutoHideEnabled(true);
        final FlowPanel container = new FlowPanel();
        tooltipContainer = new FlowPanel();

        getElement().getStyle().setZIndex(1000);
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
//                myself.hide();
            }
        }, MouseMoveEvent.getType());

        final Timer t = new Timer() {
            public void run() {
                if (tooltipElement == null) {
                    generateTip();
                }
                if (tooltipElement != null) {
                    myself.setPopupPosition(0, 0);
                    myself.show();
                    myself.setPopupPosition(widget.getAbsoluteLeft(), widget.getAbsoluteTop() + 70);
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

    public abstract void generateTip();

}
