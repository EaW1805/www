package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 4/10/12
 * Time: 1:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class ScrollBarScrollsOverEAW
        extends AbsolutePanel {

    private AbsolutePanel mainContainer;
    private ScrollPanel scrollArea;
    private Image scrollBar;
    private AbsolutePanel scrollBarPanel;

    private boolean moveBar;
    private double barFactor;
    private int barPosY;
    private Timer t;
    private int oldHeight;
    private final int wheelSpeed;
    private Image barImage;
    private VerticalPanel scrollBarContainer = new VerticalPanel();

    private static final int scrollerHeight = 31;

    /**
     * Default constructor.
     * Setups the panel but not the widget to be scrolled.
     */
    public ScrollBarScrollsOverEAW() {
        wheelSpeed = 10;
        setUpPanel();
    }

    /**
     * Constructor.
     *
     * @param scrolledWidget The widget to be scrolled.
     */
    public ScrollBarScrollsOverEAW(final Widget scrolledWidget) {
        wheelSpeed = 10;
        setUpPanel();
        //        scrollArea.setWidget(scrolledWidget);
        scrollArea.setWidget(scrolledWidget);

    }

    /**
     * Constructor that sets the default speed.
     *
     * @param wheelSpeed The scrolling speed of the wheel.
     */
    public ScrollBarScrollsOverEAW(final int wheelSpeed) {
        this.wheelSpeed = wheelSpeed;
        setUpPanel();
    }

    /**
     * Constructor that sets the default speed.
     *
     * @param scrolledWidget The widget we want to set the panel for.
     * @param wheelSpeed     The scrolling speed of the wheel.
     */
    public ScrollBarScrollsOverEAW(final Widget scrolledWidget, final int wheelSpeed) {
        this.wheelSpeed = wheelSpeed;
        setUpPanel();

        scrollArea.setWidget(scrolledWidget);

        //        scrollArea.setWidget(scrolledWidget);
    }


    /**
     * Set up panel.
     */
    private final void setUpPanel() {
        mainContainer = this;

        oldHeight = -1;
        scrollArea = new ScrollPanel() {
            @Override
            public void onResize() {
                super.onResize();
            }

            protected void onAttach() {
                super.onAttach();
                resizeBar();
                t.scheduleRepeating(500);

                ScrollBarScrollsOverEAW.this.getParent().addDomHandler(new MouseUpHandler() {
                    public void onMouseUp(final MouseUpEvent mouseUpEvent) {
                        moveBar = false;
                    }
                }, MouseUpEvent.getType());

                ScrollBarScrollsOverEAW.this.getParent().addDomHandler(new MouseMoveHandler() {
                    public void onMouseMove(final MouseMoveEvent event) {
                        if (moveBar) {
                            if (event.getClientY() < barPosY) {
                                while (barPosY != event.getClientY()) {
                                    barPosY--;
                                    scrollUp(1, true);
                                }
                            } else if (event.getClientY() > barPosY) {
                                while (barPosY != event.getClientY()) {
                                    barPosY++;
                                    scrollDown(1, true);
                                }
                            }
                        }
                    }
                }, MouseMoveEvent.getType());
            }

            protected void onDetach() {
                super.onDetach();
                t.cancel();
            }

        };
        scrollArea.setStyleName("noScrollBars");

        barImage = new Image("http://static.eaw1805.com/images/panels/trade/barVertical.png");
        barImage.setSize("5px", "0px");

        scrollBar = new Image("http://static.eaw1805.com/images/panels/trade/ScrollerVertical.png");
        scrollBar.setSize("", scrollerHeight + "px");


        scrollBarPanel = new AbsolutePanel();
        scrollBarPanel.add(barImage, 6, 0);
        scrollBarPanel.add(scrollBar, 0, 0);

        final ImageButton imageUp =
                new ImageButton("http://static.eaw1805.com/images/panels/trade/ButZoomInOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                scrollUp(wheelSpeed, false);
                imageUp.deselect();
            }
        }).addToElement(imageUp.getElement()).register();


        final ImageButton imageDown =
                new ImageButton("http://static.eaw1805.com/images/panels/trade/ButZoomOutOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                scrollDown(wheelSpeed, false);
                imageDown.deselect();
            }
        }).addToElement(imageDown.getElement()).register();


        scrollBarContainer.add(imageUp);
        scrollBarContainer.add(scrollBarPanel);
        scrollBarContainer.add(imageDown);

        mainContainer.add(scrollArea);
        mainContainer.add(scrollBarContainer);


        mainContainer.addDomHandler(new MouseWheelHandler() {
            public void onMouseWheel(final MouseWheelEvent event) {
                if (event.getDeltaY() < 0) {
                    scrollUp(wheelSpeed, false);
                } else if (event.getDeltaY() > 0) {
                    scrollDown(wheelSpeed, false);
                }
            }
        }, MouseWheelEvent.getType());

        scrollBar.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(final MouseDownEvent mouseDownEvent) {
                moveBar = true;
                barPosY = mouseDownEvent.getClientY();
                mouseDownEvent.preventDefault();
                mouseDownEvent.stopPropagation();
            }
        });

        mainContainer.addDomHandler(new MouseUpHandler() {
            public void onMouseUp(final MouseUpEvent mouseUpEvent) {
                moveBar = false;
            }
        }, MouseUpEvent.getType());

        mainContainer.addDomHandler(new MouseMoveHandler() {
            public void onMouseMove(final MouseMoveEvent event) {
                if (moveBar) {
                    if (event.getClientY() < barPosY) {
                        while (barPosY != event.getClientY()) {
                            barPosY--;
                            scrollUp(1, true);
                        }
                    } else if (event.getClientY() > barPosY) {
                        while (barPosY != event.getClientY()) {
                            barPosY++;
                            scrollDown(1, true);
                        }
                    }
                }
            }
        }, MouseMoveEvent.getType());


        t = new Timer() {
            public void run() {
                if (mainContainer.isAttached()) {
                    resizeBar();
                }
            }
        };
    }


    /**
     * Set the widget you want to be scrolled.
     *
     * @param scrolledWidget The widget.
     */
    public void setWidget(final Widget scrolledWidget) {
        scrollArea.setWidget(scrolledWidget);

    }


    /**
     * Scroll down.
     *
     * @param speed How fast to scroll.
     * @param isBar If the panel is scrolled from the bar.
     */
    public void scrollDown(final int speed, final boolean isBar) {
        if (isBar) {
            if (scrollBarPanel.getWidgetTop(scrollBar) + scrollBar.getOffsetHeight() + speed > scrollBarPanel.getOffsetHeight()) {
                scrollBarPanel.setWidgetPosition(scrollBar, 0, scrollBarPanel.getOffsetHeight() - scrollBar.getOffsetHeight());

            } else {
                scrollBarPanel.setWidgetPosition(scrollBar, 0, scrollBarPanel.getWidgetTop(scrollBar) + speed);
            }
            scrollArea.setVerticalScrollPosition((int) (scrollBarPanel.getWidgetTop(scrollBar) * barFactor));
        } else {
            scrollArea.setVerticalScrollPosition(scrollArea.getVerticalScrollPosition() + speed);
            scrollBarPanel.setWidgetPosition(scrollBar, 0, (int) (scrollArea.getVerticalScrollPosition() / barFactor));

        }
    }

    /**
     * Scroll up.
     *
     * @param speed How fast to scroll.
     * @param isBar If the panel is scrolled from the bar.
     */
    public void scrollUp(final int speed, final boolean isBar) {
        if (isBar) {
            if (scrollBarPanel.getWidgetTop(scrollBar) - speed < 0) {
                scrollBarPanel.setWidgetPosition(scrollBar, 0, 0);
            } else {
                scrollBarPanel.setWidgetPosition(scrollBar, 0, scrollBarPanel.getWidgetTop(scrollBar) - speed);
            }
            scrollArea.setVerticalScrollPosition((int) (scrollBarPanel.getWidgetTop(scrollBar) * barFactor));

        } else {
            scrollArea.setVerticalScrollPosition(
                    scrollArea.getVerticalScrollPosition() - speed);
            scrollBarPanel.setWidgetPosition(scrollBar, 0, (int) (scrollArea.getVerticalScrollPosition() / barFactor));
        }
    }


    /**
     * Set the panels size.
     *
     * @param width  The width.
     * @param height The height.
     */
    public void setSize(final int width, final int height, final int barOffset) {
        mainContainer.setSize(width + "px", height + "px");
        scrollArea.setSize(width + "px", height + "px");
        scrollBarPanel.setSize("16px", (height - 44) + "px");
        barImage.setHeight((height - 44) + "px");
        mainContainer.add(scrollArea, 0, 0);
        mainContainer.add(scrollBarContainer, width - (11 + barOffset), 0);
        resizeBar();
    }


    /**
     * Resize scroll bar.
     */
    public final void resizeBar() {
        if (oldHeight != scrollArea.getWidget().getOffsetHeight()) {
            oldHeight = scrollArea.getWidget().getOffsetHeight();
            final int barHeight = scrollerHeight;
            final int pixelsToScrollInBar = scrollArea.getOffsetHeight() - 44 - barHeight;
            final int pixelsToScrollInWidget = scrollArea.getWidget().getOffsetHeight() - scrollArea.getOffsetHeight();

            barFactor = pixelsToScrollInWidget * 1.0 / pixelsToScrollInBar;
            scrollBarPanel.setWidgetPosition(scrollBar, 0,
                    (int) (scrollArea.getVerticalScrollPosition() / barFactor));
        }
    }


}
