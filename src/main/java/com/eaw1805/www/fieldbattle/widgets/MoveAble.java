package com.eaw1805.www.fieldbattle.widgets;


import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.eaw1805.www.fieldbattle.stores.utils.MapConstants;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Image;

public abstract class MoveAble {

    boolean down = false;
    boolean move = false;
    int xPos = 0;
    int yPos = 0;
    HandlerRegistration handlerRegistration = null;
    HandlerRegistration moveRegistration = null;
    final protected Image clone;

    public MoveAble(final DrawingAreaFB drawingArea, final Image obj) {
        clone = new Image(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight(), obj.getHref());

        final MouseUpHandler handler = new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                if (moveRegistration != null) {
                    moveRegistration.removeHandler();
                }
                if (handlerRegistration != null) {
                    handlerRegistration.removeHandler();
                }

                down = false;
                if (move) {
                    move = false;
                    onMoveEnd(event.getX(), event.getY());
                } else {
                    onJustClick();
                }
                drawingArea.remove(clone);
                obj.setX(clone.getX());
                obj.setY(clone.getY());
                obj.setVisible(true);
            }
        };
        final MouseMoveHandler moveHandler = new MouseMoveHandler() {
            @Override
            public void onMouseMove(final MouseMoveEvent event) {
                event.stopPropagation();
                int x = event.getX();
                int y = event.getY();
                double zoomEffect = 1/ drawingArea.getZoomLevel();
                clone.setX(xPos + (int)((x*zoomEffect - xPos)) - MapConstants.TILE_WIDTH/2);
                clone.setY(yPos + (int)((y*zoomEffect - yPos)) - MapConstants.TILE_HEIGHT/2);
                xPos = clone.getX();
                yPos = clone.getY();
                if (down) {
                    move = true;
                    MoveAble.this.onMouseMove(x, y);
                }
            }
        };


        obj.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                obj.setVisible(false);
                drawingArea.add(clone);
                event.stopPropagation();
                event.preventDefault();
                down = true;
                xPos = clone.getX();
                yPos = clone.getY();
                moveRegistration = drawingArea.addMouseMoveHandler(moveHandler);
                handlerRegistration = drawingArea.addMouseUpHandler(handler);

            }
        });



    }

    public abstract void onMouseMove(final int x, final int y);
    public abstract void onMoveEnd(final int x, final int y);
    public abstract void onJustClick();


}
