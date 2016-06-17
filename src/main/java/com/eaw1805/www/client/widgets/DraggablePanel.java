package com.eaw1805.www.client.widgets;

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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.eaw1805.www.client.gui.GuiComponentBase;
import com.eaw1805.www.shared.stores.GameStore;

public class DraggablePanel
        extends WindowPanelEAW {

    private boolean movePanel;
    private int globalPosX, globalPosY, newPosX, newPosY;

    private boolean drag = true;
    private final GuiComponentBase component;
    private final Timer opener;
    private final Timer closer;
    private static final int showHideStep = 50;
    private static final int showHideSpeed = 10;

    public DraggablePanel() {

        super();
        component = new GuiComponentBase(this);
        final DraggablePanel self = this;
        this.addDomHandler(new MouseDownHandler() {
            public void onMouseDown(final MouseDownEvent mouseDownEvent) {

                if (mouseDownEvent.getY() < 65) {
                    globalPosX = mouseDownEvent.getClientX();
                    globalPosY = mouseDownEvent.getClientY();
                    movePanel = true;
                }
                GameStore.getInstance().getLayoutView().bringToTop(self);
                GameStore.getInstance().bringComponentToTop(component, false);
            }
        }, MouseDownEvent.getType());


        this.addDomHandler(new MouseMoveHandler() {
            public void onMouseMove(final MouseMoveEvent event) {
                if (movePanel && drag
                        && (event.getClientX() != globalPosX || event.getClientY() != globalPosY)) {
                    //convert local to global
                    newPosX = GameStore.getInstance().getLayoutView().getWidgetX(self) + (event.getClientX() - globalPosX);
                    newPosY = GameStore.getInstance().getLayoutView().getWidgetY(self) + (event.getClientY() - globalPosY);
                    globalPosX = event.getClientX();
                    globalPosY = event.getClientY();
                    self.setStyleName("cursorMove", true);

                    GameStore.getInstance().getLayoutView()
                            .setWidgetPosition(self, newPosX, newPosY, false, true);
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

        closer = new Timer() {
            @Override
            public void run() {
                //determine the closer edge
                int stepVertical = 0;
                int stepHorizontal = 0;
                final int fromTop = DraggablePanel.this.getAbsoluteTop();
                final int fromLeft = DraggablePanel.this.getAbsoluteLeft();
                final int fromBottom = Window.getClientHeight() - (DraggablePanel.this.getOffsetHeight() + fromTop);
                final int fromRight = Window.getClientWidth() - (DraggablePanel.this.getOffsetWidth() + fromLeft);
                if (fromTop < fromLeft && fromTop < fromBottom && fromTop < fromRight) {
                    stepVertical = -showHideStep;
                } else if (fromBottom < fromTop && fromBottom < fromLeft && fromBottom < fromRight) {
                    stepVertical = showHideStep;
                } else if (fromLeft < fromTop && fromLeft < fromBottom && fromLeft < fromRight) {
                    stepHorizontal = -showHideStep;
                } else {
                    stepHorizontal = showHideStep;
                }
                //now make a step closer to that edge
                GameStore.getInstance().getLayoutView().setWidgetPosition(DraggablePanel.this, GameStore.getInstance().getLayoutView().getWidgetX(DraggablePanel.this) + stepHorizontal, GameStore.getInstance().getLayoutView().getWidgetY(DraggablePanel.this) + stepVertical, true, false);
                if (stepVertical < 0 && (GameStore.getInstance().getLayoutView().getWidgetY(DraggablePanel.this) + DraggablePanel.this.getOffsetHeight()) < 0) {
                    closer.cancel();
                } else if (stepVertical > 0 && GameStore.getInstance().getLayoutView().getWidgetY(DraggablePanel.this) > Window.getClientHeight()) {
                    closer.cancel();
                } else if (stepHorizontal < 0 && (GameStore.getInstance().getLayoutView().getWidgetX(DraggablePanel.this) + DraggablePanel.this.getOffsetWidth()) < 0) {
                    closer.cancel();
                } else if (stepHorizontal > 0 && GameStore.getInstance().getLayoutView().getWidgetX(DraggablePanel.this) > Window.getClientWidth()) {
                    closer.cancel();
                }
            }
        };


        opener = new Timer() {
            @Override
            public void run() {
                //determine where it should move first
                int stepHorizontal = 0;
                int stepVertical = 0;
                if (newPosX < GameStore.getInstance().getLayoutView().getWidgetX(DraggablePanel.this)) {
                    stepHorizontal = -showHideStep;
                } else if (newPosX > GameStore.getInstance().getLayoutView().getWidgetX(DraggablePanel.this)) {
                    stepHorizontal = showHideStep;
                } else if (newPosY < GameStore.getInstance().getLayoutView().getWidgetY(DraggablePanel.this)) {
                    stepVertical = -showHideStep;
                } else {
                    stepVertical = showHideStep;
                }
                //now go back to the original position
                GameStore.getInstance().getLayoutView().setWidgetPosition(DraggablePanel.this, GameStore.getInstance().getLayoutView().getWidgetX(DraggablePanel.this) + stepHorizontal, GameStore.getInstance().getLayoutView().getWidgetY(DraggablePanel.this) + stepVertical, true, false);
                //check if it came back to its original position... or enough close to it
                if (GameStore.getInstance().getLayoutView().getWidgetX(DraggablePanel.this) >= newPosX
                        && GameStore.getInstance().getLayoutView().getWidgetX(DraggablePanel.this) < newPosX + showHideStep
                        && GameStore.getInstance().getLayoutView().getWidgetY(DraggablePanel.this) >= newPosY
                        && GameStore.getInstance().getLayoutView().getWidgetY(DraggablePanel.this) < newPosY + showHideStep) {
                    //make it go to its absolute original position
                    GameStore.getInstance().getLayoutView().setWidgetPosition(DraggablePanel.this, newPosX, newPosY, true, true);
                    opener.cancel();

                }
            }
        };
    }

    public void hidePanel() {
        //close opener if running
        opener.cancel();
        //hide all
        closer.scheduleRepeating(showHideSpeed);
    }

    public void showPanel() {
        //close closer if running
        closer.cancel();
        //open all
        opener.scheduleRepeating(showHideSpeed);
    }

    //on attach register panel...
    protected void onAttach() {
        super.onAttach();
        component.registerComponent();
    }

    protected void onDetach() {
        super.onDetach();
        component.unRegisterComponent();
    }

    public void setPosition(final int xCoord, final int yCoord) {
        GameStore.getInstance().getLayoutView().setWidgetPosition(this, xCoord, yCoord, false, true);
    }

    public void open() {
        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(this);
        GameStore.getInstance().getLayoutView().positionTocCenter(this);
    }

    public void close() {
        GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(this);
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
