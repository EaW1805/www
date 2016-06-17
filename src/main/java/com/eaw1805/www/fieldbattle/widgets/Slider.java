package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;

import java.util.ArrayList;
import java.util.List;

public class Slider extends AbsolutePanel {
    final Image stepper;
    int minValue = 0;
    int maxValue = 0;
    int step = 1;
    int offsetWidth;
    int offsetHeight;
    boolean move = false;
    int currentValue = 0;
    int stepperWidth = 45;
    List<ValueChangeHandler> handlers = new ArrayList<ValueChangeHandler>();
    final List<Image> valueImages = new ArrayList<Image>();
    final AbsolutePanel stepsContainer = new AbsolutePanel();
    ToolTipPanel tooltip;
    Label tip = new Label("0");

    public Slider() {
        final Image bgImage = new Image("http://static.eaw1805.com/images/panels/trade/tradeBar.png");
        bgImage.setSize("100%", "8px");
        stepper = new Image("http://static.eaw1805.com/images/panels/trade/tradeBarScroller.png");
        stepper.setWidth(stepperWidth + "px");
        add(stepsContainer, 0, 0);
        add(bgImage, 0, 5);
        add(stepper, 0, 0);
        tooltip = new ToolTipPanel(stepper, true) {
            @Override
            public void generateTip() {
                //do nothing here.. will do manually
            }
        };
        tooltip.setLockOpen(true);
        tooltip.setOpenImmediately(true);
        tooltip.setTooltip(tip);


        addDomHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {

                event.stopPropagation();
                event.preventDefault();
                move = true;

            }
        }, MouseDownEvent.getType());
        addDomHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(final MouseUpEvent event) {

                event.stopPropagation();
                event.preventDefault();

            }
        }, MouseUpEvent.getType());

        addDomHandler(new MouseMoveHandler() {
            @Override
            public void onMouseMove(final MouseMoveEvent event) {

                event.stopPropagation();
                event.preventDefault();
                if (move) {
                    int x = event.getX() - stepper.getWidth() / 2;
                    if (x < 0) {
                        x = 0;
                    }
                    if (x + stepper.getWidth() > getOffsetWidth()) {
                        x = getOffsetWidth() - stepper.getWidth();
                    }
                    setWidgetPosition(stepper, x, 0);

                    //update to new value
                    int range = (maxValue - minValue) / step;
                    int totalWidth = getOffsetWidth() - stepper.getWidth();
                    double stepWidth = totalWidth * 1.0 / (double) range;
                    int value = (int) ((getWidgetLeft(stepper) + stepper.getWidth() / 2) * 1.0 / stepWidth);
                    if (value > maxValue) {
                        value = maxValue;
                    }
                    if (value < minValue) {
                        value = minValue;
                    }
                    tip.setText(String.valueOf(value));
                }

            }
        }, MouseMoveEvent.getType());

        addDomHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {

                event.stopPropagation();
                event.preventDefault();

                move = false;
                //update widget position
                int x = event.getX() - stepper.getWidth() / 2;
                if (x < 0) {
                    x = 0;
                }
                if (x + stepper.getWidth() > getOffsetWidth()) {
                    x = getOffsetWidth() - stepper.getWidth();
                }
                setWidgetPosition(stepper, x, 0);

                //update to new value
                int range = (maxValue - minValue) / step;
                int totalWidth = getOffsetWidth() - stepper.getWidth();
                double stepWidth = totalWidth * 1.0 / (double) range;
                int value = (int) ((getWidgetLeft(stepper) + stepper.getWidth() / 2) * 1.0 / stepWidth);
                if (value > maxValue) {
                    value = maxValue;
                }
                if (value < minValue) {
                    value = minValue;
                }

                if (value != currentValue) {
                    setValue(value);
                    executeHandlers();
                }
                fixPosition();

            }
        }, ClickEvent.getType());
    }

    public void setSize(int width, int height) {
        offsetWidth = width;
        offsetHeight = height;
        stepsContainer.setSize(offsetWidth + "px", offsetHeight + "px");
        setSize(offsetWidth + "px", offsetHeight + "px");
    }

    public int getValue() {
        return currentValue;
    }

    public void fixPosition() {
        int range = (maxValue - minValue) / step;
        int totalWidth = getOffsetWidth() - stepper.getWidth();
        double stepWidth = totalWidth * 1.0 / (double) range;
        int index = (currentValue - minValue) / step;
        setWidgetPosition(stepper, (int) (index * stepWidth), 0);
    }

    public void setValue(int value) {
        currentValue = value;
        fixPosition();
    }

    public Slider setMinValue(int min) {
        minValue = min;
        updateLayout();
        return this;
    }

    public Slider setMaxValue(int max) {
        maxValue = max;
        updateLayout();
        return this;
    }

    public Slider setStep(int inStep) {
        step = inStep;
        updateLayout();
        return this;
    }

    public void onAttach() {
        super.onAttach();
        updateLayout();
    }

    public void updateLayout() {
        for (final Image img : valueImages) {
            img.removeFromParent();
        }
        valueImages.clear();
        int range = (maxValue - minValue) / step;
        int totalWidth = offsetWidth - stepperWidth;
        double stepWidth = totalWidth * 1.0 / (double) range;
        MainPanel.getInstance().setDebugMessage(maxValue + ", " + minValue + " , " + step + " , " + range + " , " + stepWidth + " , " + getOffsetWidth());
//        int index = (currentValue - minValue)/step;
        for (int index = 0; index <= range; index++) {
            Image stepImg = new Image("http://static.eaw1805.com/images/panels/trade/barVertical.png");
            stepImg.setSize("5px", "23px");
            stepsContainer.add(stepImg, (int) (index * stepWidth + stepperWidth / 2), 0);
            valueImages.add(stepImg);
        }
    }

    public void addValueChangedHandler(final ValueChangeHandler handler) {
        handlers.add(handler);
    }

    public void executeHandlers() {
        ValueChangeEvent event = new ValueChangeEvent(currentValue);
        for (ValueChangeHandler handler : handlers) {
            handler.onChange(event);
        }
    }

    public void nextStep() {
        currentValue += step;
        if (currentValue > maxValue) {
            currentValue = maxValue;
        }
        fixPosition();
        executeHandlers();
    }

    public void previousStep() {
        currentValue -= step;
        if (currentValue < minValue) {
            currentValue = minValue;
        }
        fixPosition();
        executeHandlers();
    }

    public boolean finished() {
        return currentValue == maxValue;
    }

    public boolean hasNextStep() {
        return currentValue < maxValue;
    }

    public boolean hasPreviousStep() {
        return currentValue > minValue;
    }


}
