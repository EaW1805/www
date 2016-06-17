package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class ClickAbsolutePanel
        extends AbsolutePanel
        implements HasAllMouseHandlers, HasClickHandlers, HasDoubleClickHandlers {

    private int id = -1;
    private int index = -1;


    public void setId(final int id) {
        this.id = id;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(final int index) {
        this.index = index;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    public int getId() {
        return id;
    }

    public HandlerRegistration addClickHandler(final ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    public HandlerRegistration addDoubleClickHandler(final DoubleClickHandler handler) {
        return addDomHandler(handler, DoubleClickEvent.getType());
    }

    public HandlerRegistration addMouseDownHandler(final MouseDownHandler handler) {
        return addDomHandler(handler, MouseDownEvent.getType());
    }

    public HandlerRegistration addMouseMoveHandler(final MouseMoveHandler handler) {
        return addDomHandler(handler, MouseMoveEvent.getType());
    }

    public HandlerRegistration addMouseUpHandler(final MouseUpHandler handler) {
        return addDomHandler(handler, MouseUpEvent.getType());
    }

    public HandlerRegistration addMouseOutHandler(final MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    public HandlerRegistration addMouseOverHandler(final MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    public HandlerRegistration addMouseWheelHandler(final MouseWheelHandler handler) {
        return null;
    }

}
