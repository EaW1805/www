package com.eaw1805.www.fieldbattle.widgets;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class ScrollVerticalBar
        extends HorizontalPanel {

    private HorizontalPanel mainContainer;
    private ScrollPanel scrollArea;
    private Image scrollBar;
    private AbsolutePanel scrollBarPanel;


    private boolean moveBar;
    private double barFactor;
    private int barPosY;

    private int oldHeight;
    private int wheelSpeed;
    private Image barImage;
    private boolean useStep = false;
    private int step;

    private static final int scrollerHeight = 31;
    private static final int upDownButtonsTotalHeight = 28;

    /**
     * Default constructor.
     * Setups the panel but not the widget to be scrolled.
     */
    public ScrollVerticalBar() {
        wheelSpeed = 10;
        setUpPanel();
    }

    /**
     * Constructor.
     *
     * @param scrolledWidget The widget to be scrolled.
     */
    public ScrollVerticalBar(final Widget scrolledWidget) {
        wheelSpeed = 10;
        setUpPanel();
        setWidget(scrolledWidget);

    }

    public void addScrollPanelStyle(final String style) {
        scrollArea.setStyleName(style, true);
    }

    /**
     * Set the step.
     * The step indicates how many pixels you want to be idle before you scroll up or down.
     * This also overrides the wheel speed variable, since it should scroll with step "speed"
     *
     * @param step The size of the step to set.
     */
    public void enableAndSetStep(final int step) {
        useStep = true;
        this.step = step;
        wheelSpeed = step;
    }

    /**
     * Constructor that sets the default speed.
     *
     * @param wheelSpeed The scrolling speed of the wheel.
     */
    public ScrollVerticalBar(final int wheelSpeed) {
        this.wheelSpeed = wheelSpeed;
        setUpPanel();
    }

    /**
     * Constructor that sets the default speed.
     *
     * @param scrolledWidget The widget we want to set the panel for.
     * @param wheelSpeed     The scrolling speed of the wheel.
     */
    public ScrollVerticalBar(final Widget scrolledWidget, final int wheelSpeed) {
        this.wheelSpeed = wheelSpeed;
        setUpPanel();
        setWidget(scrolledWidget);
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

                ScrollVerticalBar.this.getParent().addDomHandler(new MouseUpHandler() {
                    public void onMouseUp(final MouseUpEvent mouseUpEvent) {
                        moveBar = false;
                    }
                }, MouseUpEvent.getType());
                ScrollVerticalBar.this.getParent().addDomHandler(new MouseMoveHandler() {
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
            }

        };
        scrollArea.setStyleName("noScrollBars");


        scrollBar = new Image("http://static.eaw1805.com/images/panels/trade/ScrollerVertical.png");
        scrollBar.setSize("", scrollerHeight + "px");

        barImage = new Image("http://static.eaw1805.com/images/panels/trade/barVertical.png");
        barImage.setSize("5px", "0px");

        scrollBarPanel = new AbsolutePanel();
        scrollBarPanel.add(barImage, 6, 0);
        scrollBarPanel.add(scrollBar, 0, 0);

        final Image imageUp =
                new Image("http://static.eaw1805.com/images/panels/trade/ButZoomInOff.png");
        imageUp.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                scrollUp(wheelSpeed, false);

            }
        });


        final Image imageDown =
                new Image("http://static.eaw1805.com/images/panels/trade/ButZoomOutOff.png");
        imageDown.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                scrollDown(wheelSpeed, false);
            }
        });


        final VerticalPanel scrollBarContainer = new VerticalPanel();
        scrollBarContainer.add(imageUp);
        scrollBarContainer.add(scrollBarPanel);
        scrollBarContainer.add(imageDown);

        mainContainer.add(scrollArea);
        mainContainer.add(scrollBarContainer);


        mainContainer.addDomHandler(new MouseWheelHandler() {
            public void onMouseWheel(final MouseWheelEvent event) {
                event.stopPropagation();
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

            if (!useStep || ((int) (scrollBarPanel.getWidgetTop(scrollBar) * barFactor)) % step <= (int) (barFactor)) {
                scrollArea.setVerticalScrollPosition((int) (scrollBarPanel.getWidgetTop(scrollBar) * barFactor));
            }
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

            if (!useStep || ((int) (scrollBarPanel.getWidgetTop(scrollBar) * barFactor)) % step <= (int) (barFactor)) {
                scrollArea.setVerticalScrollPosition((int) (scrollBarPanel.getWidgetTop(scrollBar) * barFactor));
            }
        } else {
            scrollArea.setVerticalScrollPosition(scrollArea.getVerticalScrollPosition() - speed);
            scrollBarPanel.setWidgetPosition(scrollBar, 0, (int) (scrollArea.getVerticalScrollPosition() / barFactor));

        }
    }


    /**
     * Set the panels size.
     *
     * @param width  The width.
     * @param height The height.
     */
    public void setSize(final int width, final int height) {
        mainContainer.setSize(width + "px", height + "px");
        scrollArea.setSize((width - 16) + "px", height + "px");
        scrollBarPanel.setSize("16px", (height - upDownButtonsTotalHeight) + "px");
        barImage.setHeight((height - upDownButtonsTotalHeight) + "px");
        publicWidth = width;
        publicHeight = height;
        resizeBar();
    }

    int publicWidth = 0;
    int publicHeight = 0;
    private boolean hideSideBar = false;

    public void hideSideBar() {
        hideSideBar = true;
    }


    /**
     * Resize scroll bar.
     */
    public final void resizeBar() {
        if (oldHeight != scrollArea.getWidget().getOffsetHeight()) {
            oldHeight = scrollArea.getWidget().getOffsetHeight();
            final int barHeight = scrollerHeight;

            final int pixelsToScrollInBar = scrollArea.getOffsetHeight() - upDownButtonsTotalHeight - barHeight;
            final int pixelsToScrollInWidget = scrollArea.getElement().getScrollHeight() - scrollArea.getElement().getClientHeight();

            barFactor = pixelsToScrollInWidget * 1.0 / pixelsToScrollInBar;
            scrollBarPanel.setWidgetPosition(scrollBar, 0, (int) (scrollArea.getVerticalScrollPosition() / barFactor));
            if (hideSideBar || scrollArea.getWidget().getOffsetHeight() <= scrollArea.getOffsetHeight()) {
                scrollBarPanel.getParent().setVisible(false);
                scrollArea.setWidth((publicWidth) + "px");
            } else {
                scrollArea.setWidth((publicWidth - 16) + "px");
                scrollBarPanel.getParent().setVisible(true);
            }
        }
    }

    public void ensureVisible(UIObject object) {
        scrollArea.ensureVisible(object);
    }

    /**
     * Scroll to bottom and update the bar position.
     */
    public void scrollToBottom() {
        scrollArea.scrollToBottom();
        scrollBarPanel.setWidgetPosition(scrollBar, 0, (int) (scrollArea.getVerticalScrollPosition() / barFactor));
    }

    /**
     * Scroll to top and update the bar position.
     */
    public void scrollToTop() {
        scrollArea.scrollToTop();
        scrollBarPanel.setWidgetPosition(scrollBar, 0, (int) (scrollArea.getVerticalScrollPosition() / barFactor));
    }

    public ScrollPanel getScrollArea() {
        return scrollArea;
    }
}
