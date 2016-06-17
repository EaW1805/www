package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Window;
import com.eaw1805.www.fieldbattle.stores.utils.*;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

public class MiniMap extends DrawingArea implements MapConstants {
    Rectangle viewRectangle;

    boolean move = false;
    public static int MINIMAP_WIDTH = 225;
    public static int MINIMAP_HEIGHT = 225;

    public MiniMap(int width, int height) {
        super(width, height);


        addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent mouseDownEvent) {
                mouseDownEvent.preventDefault();
                mouseDownEvent.stopPropagation();
                move = true;
                moveMap(mouseDownEvent.getX(), mouseDownEvent.getY(), true);
            }
        });

        addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent mouseOutEvent) {
                move = false;
            }
        });
        addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent mouseUpEvent) {
                move = false;
            }
        });
        addMouseMoveHandler(new MouseMoveHandler() {
            @Override
            public void onMouseMove(MouseMoveEvent mouseMoveEvent) {
                if (move) {
                    moveMap(mouseMoveEvent.getX(), mouseMoveEvent.getY(), false);
                }
            }
        });

    }

    public void moveMap(final int x, final int y, final boolean animate) {
        final MiniMapUtils miniUtils = MainPanel.getInstance().getMiniMapUtils();

        int fixedX = x-SIDE_OFFSET_MINIMAP*miniUtils.getTileSize();
        int fixedY = y-SIDE_OFFSET_MINIMAP*miniUtils.getTileSize();
        int centerX = (int)((((fixedX*1.0/miniUtils.getTileSize())*TILE_WIDTH) + SIDE_OFFSET*TILE_WIDTH)*MainPanel.getInstance().getDrawingArea().getZoomLevel() + MainPanel.getInstance().getDrawingArea().getZoomOffsetX());
        int centerY = (int)((((fixedY*1.0/miniUtils.getTileSize())*TILE_HEIGHT) + SIDE_OFFSET*TILE_HEIGHT)*MainPanel.getInstance().getDrawingArea().getZoomLevel() + MainPanel.getInstance().getDrawingArea().getZoomOffsetY());
        int screenX = centerX - Window.getClientWidth()/2;
        int screenY = centerY - Window.getClientHeight()/2;
        if (animate) {
            AnimationUtils.animateMapScroll(screenX, screenY, new BasicHandler() {
                @Override
                public void run() {
                    updateRectangle();
                }
            }, null);
        } else {
            //since we custom animate here, cancel any already assigned animation..
            AnimationUtils.cancelMapAnimation();
            MainPanel.getInstance().getDrawingArea().getScroller().setHorizontalScrollPosition(screenX);
            MainPanel.getInstance().getDrawingArea().getScroller().setScrollPosition(screenY);
            updateRectangle();
        }




    }


    public void moveMapRelative(final int moveX, final int moveY, final boolean animate) {
        int newX = MainPanel.getInstance().getDrawingArea().getScroller().getHorizontalScrollPosition() + moveX;
        int newY = MainPanel.getInstance().getDrawingArea().getScroller().getVerticalScrollPosition() + moveY;
        if (animate) {
            AnimationUtils.animateMapScroll(newX, newY, new BasicHandler() {
                @Override
                public void run() {
                    updateRectangle();
                }
            }, null);
        } else {
            MainPanel.getInstance().getDrawingArea().getScroller().setHorizontalScrollPosition(newX);
            MainPanel.getInstance().getDrawingArea().getScroller().setVerticalScrollPosition(newY);
            updateRectangle();
        }



    }

    public void setViewRectangle(final Rectangle viewRectangle) {
        this.viewRectangle = viewRectangle;
    }

    public void updateRectangle() {
        final MiniMapUtils miniUtils = MainPanel.getInstance().getMiniMapUtils();
        final MapUtils mapUtils = MainPanel.getInstance().getMapUtils();

        int screenX = (int)(MainPanel.getInstance().getDrawingArea().getScroller().getHorizontalScrollPosition() - (SIDE_OFFSET - SIDE_OFFSET_MINIMAP)*TILE_WIDTH*MainPanel.getInstance().getDrawingArea().getZoomLevel());
        int screenY = (int)(MainPanel.getInstance().getDrawingArea().getScroller().getVerticalScrollPosition() - (SIDE_OFFSET - SIDE_OFFSET_MINIMAP)*TILE_HEIGHT*MainPanel.getInstance().getDrawingArea().getZoomLevel());

        double sizeFactorX = (double) (miniUtils.getMiniMapWidth() + (SIDE_OFFSET - SIDE_OFFSET_MINIMAP)*2*miniUtils.getTileSize()) * 1.0 / (double) (mapUtils.getMapWidth() * MainPanel.getInstance().getDrawingArea().getZoomLevel());
        double sizeFactorY = (double) (miniUtils.getMiniMapHeight() + (SIDE_OFFSET - SIDE_OFFSET_MINIMAP)*2*miniUtils.getTileSize()) * 1.0 / (double) (mapUtils.getMapHeight() * MainPanel.getInstance().getDrawingArea().getZoomLevel());

        int rectX = (int) ((screenX - MainPanel.getInstance().getDrawingArea().getZoomOffsetX()) * sizeFactorX);
        int rectY = (int) ((screenY - MainPanel.getInstance().getDrawingArea().getZoomOffsetY()) * sizeFactorY);

        int width = (int) (Window.getClientWidth() * sizeFactorX);
        int height = (int) (Window.getClientHeight() * sizeFactorY);

        viewRectangle.setX(rectX);
        viewRectangle.setY(rectY);
        viewRectangle.setWidth(width);
        viewRectangle.setHeight(height);
    }
}
