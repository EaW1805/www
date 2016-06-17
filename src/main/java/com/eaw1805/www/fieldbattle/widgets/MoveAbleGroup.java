package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.www.fieldbattle.stores.utils.MapConstants;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.VectorObject;

public abstract class MoveAbleGroup {

    boolean down = false;
    boolean move = false;
    boolean start = false;
    HandlerRegistration handlerRegistration = null;
    HandlerRegistration moveRegistration = null;
    Image posImg;
    int xPos = 0;
    int yPos = 0;
    /**
     * Make a group movable inside the drawing area.
     * All Vector objects inside group MUST be images.
     *
     * @param drawingArea The drawing area to make it movable for.
     * @param obj The object to move.
     */
    public MoveAbleGroup(final DrawingAreaFB drawingArea, final Group obj) {
        final Group clone = new Group();

        //clone all images from first object to second.
        for (int index = 0; index < obj.getVectorObjectCount(); index++) {
            final Image original = (Image)obj.getVectorObject(index);
            final Image cloneImg = new Image(original.getX(), original.getY(), original.getWidth(), original.getHeight(), original.getHref());
            clone.add(cloneImg);
        }
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
                start = false;
                if (move) {
                    move = false;
                    onMoveEnd(event.getX(), event.getY());
                } else {
                    onJustClick();
                }
                drawingArea.remove(clone);

                for (int index = 0; index < clone.getVectorObjectCount(); index++) {
                    final VectorObject vObj = clone.getVectorObject(index);
                    if (vObj instanceof Image) {
                        ((Image)obj.getVectorObject(index)).setX(((Image) vObj).getX());
                        ((Image)obj.getVectorObject(index)).setY(((Image) vObj).getY());
                    }
                }
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
                for (int index = 0; index < clone.getVectorObjectCount(); index++) {
                    final VectorObject vObj = clone.getVectorObject(index);
                    if (vObj instanceof Image) {
                        ((Image)vObj).setX(((Image) vObj).getX() + (int)((x * zoomEffect - xPos)) - MapConstants.TILE_WIDTH/2);
                        ((Image)vObj).setY(((Image) vObj).getY() + (int) ((y * zoomEffect - yPos)) - MapConstants.TILE_HEIGHT / 2);
                    }
                }
                xPos = posImg.getX();
                yPos = posImg.getY();
                if (down) {
                    if (!start) {
                        start = true;
                        onMoveStart();
                    }
                    move = true;
                    MoveAbleGroup.this.onMouseMove(x, y);
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
                xPos = Integer.MAX_VALUE;
                yPos = Integer.MAX_VALUE;
                for (int index = 0; index < clone.getVectorObjectCount(); index++) {
                    if (((Image) clone.getVectorObject(index)).getX() < xPos
                            || ((Image) clone.getVectorObject(index)).getY() < yPos) {
                        posImg = ((Image) clone.getVectorObject(index));
                        xPos = ((Image) clone.getVectorObject(index)).getX();
                        yPos = ((Image) clone.getVectorObject(index)).getY();
                    }
                }


                moveRegistration = drawingArea.addMouseMoveHandler(moveHandler);
                handlerRegistration = drawingArea.addMouseUpHandler(handler);

            }
        });



    }

    public abstract void onMoveStart();
    public abstract void onMouseMove(final int x, final int y);
    public abstract void onMoveEnd(final int x, final int y);
    public abstract void onJustClick();


}
