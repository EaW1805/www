package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;

public class DraggablePanel extends AbsolutePanel {

    private boolean movePanel;
    private int globalPosX, globalPosY, newPosX, newPosY;

    private boolean drag = true;


    public DraggablePanel() {

        super();

        final DraggablePanel self = this;
        this.addDomHandler(new MouseDownHandler() {
            public void onMouseDown(final MouseDownEvent mouseDownEvent) {

                if (mouseDownEvent.getY() < 65) {
                    globalPosX = mouseDownEvent.getClientX();
                    globalPosY = mouseDownEvent.getClientY();
                    movePanel = true;
                }


            }
        }, MouseDownEvent.getType());


        this.addDomHandler(new MouseMoveHandler() {
            public void onMouseMove(final MouseMoveEvent event) {
                if (movePanel && drag
                        && (event.getClientX() != globalPosX || event.getClientY() != globalPosY)) {
                    //convert local to global
                    final MainPanel main = MainPanel.getInstance();
                    newPosX = main.getWidgetX(self) + (event.getClientX() - globalPosX);
                    newPosY = main.getWidgetY(self) + (event.getClientY() - globalPosY);
                    globalPosX = event.getClientX();
                    globalPosY = event.getClientY();
                    self.setStyleName("cursorMove", true);

                    main.setWidgetPosition(self, newPosX, newPosY);
                }
            }
        }, MouseMoveEvent.getType());

        this.addDomHandler(new DoubleClickHandler() {
            public void onDoubleClick(final DoubleClickEvent event) {
                movePanel = false;
                self.removeStyleName("cursorMove");
            }
        }, DoubleClickEvent.getType());

        this.addDomHandler(new MouseUpHandler() {
            public void onMouseUp(final MouseUpEvent mouseUpEvent) {
                movePanel = false;
                self.removeStyleName("cursorMove");
            }
        }, MouseUpEvent.getType());

        this.addDomHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                movePanel = false;
                self.removeStyleName("cursorMove");
            }
        }, MouseOutEvent.getType());


    }


    public void setPosition(final int xCoord, final int yCoord) {
        MainPanel.getInstance().setWidgetPosition(this, xCoord, yCoord);
    }

    public void open() {
        MainPanel.getInstance().addWidgetToScreen(this);
        MainPanel.getInstance().positionToCenter(this);
    }

    public void close() {
        MainPanel.getInstance().removePanelFromScreen(this);
    }

    /**
     * Set the move panel variable if needed.
     *
     * @param move The value to set.
     */
    public void setMovePanel(final boolean move) {
        this.movePanel = move;
    }

    public void setNewPosX(final int newPosX) {
        this.newPosX = newPosX;
    }

    public void setNewPosY(final int newPosY) {
        this.newPosY = newPosY;
    }
}
