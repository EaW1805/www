package com.eaw1805.www.client.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SelectEAW
        extends VerticalPanel {

    private final SimplePanel selectionView;
    private final PopupPanelEAW dropDown;

    private final AbsolutePanel container;
    private final Map<Integer, Widget> indexToOption;
    private final Map<Integer, Object> indexToValue;
    private final VerticalPanelScrollChild dropDownView;
    private final HorizontalPanel selectionContainer;
    private final List<Widget> options;
    private final List<Object> values;
    private int selectedIndex = -1;
    private int totalNumber = -1;
    private boolean opened = false;
    private final ScrollVerticalBarEAW scroll;

    private String defaultStyle = "none";
    private String hoverStyle = "none";
    private String selectedStyle = "none";
    private final Image openCloseImg;

    public SelectEAW() {
        selectionContainer = new HorizontalPanel();
        container = new AbsolutePanel();
        indexToOption = new HashMap<Integer, Widget>();
        indexToValue = new HashMap<Integer, Object>();
        dropDown = new PopupPanelEAW() {
            public void onDetach() {
                super.onDetach();
                opened = false;
            }
        };
        selectionView = new SimplePanel();
        selectionView.setStyleName("clearFont whiteText");
        selectionView.addDomHandler(new ClickHandler() {
            public void onClick(final ClickEvent clickEvent) {
                if (opened) {
                    dropDown.hide();
                    opened = false;
                } else {
                    dropDown.showRelativeTo(selectionContainer);
                    opened = true;
                }
            }
        }, ClickEvent.getType());

        selectionView.setStyleName("pointer", true);

        dropDownView = new VerticalPanelScrollChild();

        options = new ArrayList<Widget>();
        values = new ArrayList<Object>();
        scroll = new ScrollVerticalBarEAW(dropDownView, true);
        container.add(scroll);
//        dropDown.setAnimationEnabled(true);
        dropDown.setWidget(container);
        dropDown.setAutoHideEnabled(true);
        //assign a very big z-index so it will always be on top
        dropDown.getElement().getStyle().setZIndex(10000);
        selectionContainer.add(selectionView);
        add(selectionContainer);

        openCloseImg = new Image("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButZoomOutOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (opened) {
                    dropDown.hide();
                    opened = false;
                } else {
                    dropDown.showRelativeTo(selectionContainer);
                    opened = true;
                }
            }
        }).addToElement(openCloseImg.getElement()).register();

        openCloseImg.setSize("20px", "20px");
        selectionContainer.add(openCloseImg);
    }

    public void setSize(final int selectionWidth, final int selectionHeight, final int dropDownWidth, final int dropDownHeight, final int offsetLeft, final int offsetRight, final int offsetTop, final int offsetBottom) {
        openCloseImg.setSize(selectionHeight + "px", selectionHeight + "px");
        selectionView.setSize(selectionWidth + "px", selectionHeight + "px");
        container.setSize(dropDownWidth + "px", dropDownHeight + "px");
        scroll.setSize(dropDownWidth - offsetLeft - offsetRight, dropDownHeight - offsetBottom - offsetTop);
        container.setWidgetPosition(scroll, offsetLeft, offsetTop);
    }

    public void setDropDownStyleName(final String styleName) {
        dropDown.setStyleName(styleName);
    }

    public void addOption(final Widget option, final Object value) {
        options.add(option);
        dropDownView.add(option);
        values.add(value);
        totalNumber++;
        final int thisNumber = totalNumber;
        indexToValue.put(thisNumber, value);
        indexToOption.put(thisNumber, option);
        option.setStyleName(defaultStyle, true);
        option.addDomHandler(new ClickHandler() {
            public void onClick(final ClickEvent clickEvent) {
                selectOption(thisNumber);

                clickEvent.stopPropagation();
            }
        }, ClickEvent.getType());

        option.addDomHandler(new MouseOverHandler() {
            public void onMouseOver(final MouseOverEvent mouseOverEvent) {
                option.setStyleName(hoverStyle, true);
            }
        }, MouseOverEvent.getType());

        option.addDomHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                option.removeStyleName(hoverStyle);
            }
        }, MouseOutEvent.getType());
    }

    public void setOptionsStyles(final String defaultStyle, final String hoverStyle, final String selectedStyle) {
        this.defaultStyle = defaultStyle;
        this.hoverStyle = hoverStyle;
        this.selectedStyle = selectedStyle;
    }

    public void selectOption(final int index) {
        DOM.setInnerHTML(selectionView.getElement(), "");
        if (selectedIndex != -1) {
            indexToOption.get(selectedIndex).removeStyleName(selectedStyle);
            indexToOption.get(selectedIndex).setStyleName(defaultStyle, true);
        }
        selectedIndex = index;
        indexToOption.get(selectedIndex).removeStyleName(defaultStyle);
        indexToOption.get(selectedIndex).setStyleName(selectedStyle, true);

        Element clone = null;
        try {
            clone = DOM.clone(indexToOption.get(selectedIndex).getElement(), true);
        } catch (Exception e) {

        }

        try {
            DOM.appendChild(selectionView.getElement(), (com.google.gwt.user.client.Element) clone);
        } catch (Exception e) {

        }

        dropDown.hide();
        scroll.scrollToTop();
        opened = false;
        onChange();
    }

    public Object getValue() {
        return values.get(selectedIndex);
    }

    public List<Object> getValues() {
        return values;
    }

    public Object getSelectedOption() {
        return options.get(selectedIndex);
    }

    public Object getOption(final int index) {
        return options.get(index);
    }

    public abstract void onChange();
}
