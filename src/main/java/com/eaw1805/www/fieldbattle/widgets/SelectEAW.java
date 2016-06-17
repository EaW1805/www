package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.www.fieldbattle.tooltips.Tips;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SelectEAW<J>
        extends VerticalPanel {

    private final SimplePanel selectionView;
    private final PopupPanel dropDown;

    private final AbsolutePanel container;
    private final Map<Integer, Widget> indexToOption;
    private final Map<Integer, J> indexToValue;
    private final VerticalPanel dropDownView;
    private final HorizontalPanel selectionContainer;
    private final List<Widget> options;
    private final List<J> values;
    private int selectedIndex = -1;
    private int totalNumber = -1;
    private boolean opened = false;
    private final ScrollVerticalBar scroll;

    private String defaultStyle = "none";
    private String hoverStyle = "none";
    private String selectedStyle = "none";
    private final Image openCloseImg;
    private final int MAX_HEIGHT = 200;
    private int optionWidth;
    private int optionHeight;
    private boolean debug = false;

    public SelectEAW(final String title) {
        if (title != null) {
            Tips.generateTip(this, title);
        }
        selectionContainer = new HorizontalPanel();
        container = new AbsolutePanel();
        indexToOption = new HashMap<Integer, Widget>();
        indexToValue = new HashMap<Integer, J>();
        dropDown = new PopupPanel() {
            public void onDetach() {
                super.onDetach();
                opened = false;
                onSelectorDetach();
            }

            public void onAttach() {
                super.onAttach();
                fixSize();
                onSelectorAttach();
            }
        };
        dropDown.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                clickEvent.stopPropagation();
                clickEvent.preventDefault();
            }
        }, ClickEvent.getType());
        dropDown.addDomHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent mouseDownEvent) {
                mouseDownEvent.stopPropagation();
                mouseDownEvent.preventDefault();
            }
        }, MouseDownEvent.getType());

        selectionView = new SimplePanel();
        selectionView.setStyleName("clearFontMedMini whiteText");
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

        dropDownView = new VerticalPanel();

        options = new ArrayList<Widget>();
        values = new ArrayList<J>();
        scroll = new ScrollVerticalBar(dropDownView, 15);
        container.add(scroll);
//        dropDown.setAnimationEnabled(true);
        dropDown.setWidget(container);
        dropDown.setAutoHideEnabled(true);
        //assign a very big z-index so it will always be on top
        dropDown.getElement().getStyle().setZIndex(10000);
        selectionContainer.add(selectionView);
        add(selectionContainer);

        openCloseImg = new Image("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButZoomOutOff.png");
        openCloseImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (opened) {
                    dropDown.hide();
                    opened = false;
                } else {
                    dropDown.showRelativeTo(selectionContainer);
                    opened = true;
                }
            }
        });


        openCloseImg.setSize("20px", "20px");
        selectionContainer.add(openCloseImg);
    }

    public void setDebug(boolean value) {
        debug = value;
    }

    protected void onSelectorAttach() {
        //for override purposes.
    }

    protected void onSelectorDetach() {
        //for override purposes.
    }

    boolean hideSidebar = false;

    public void hideSideBar() {
        hideSidebar = true;
        scroll.hideSideBar();
    }


    public void fixSize() {
        int scrollWidgetHeight = dropDownView.getOffsetHeight();
        if (scrollWidgetHeight > MAX_HEIGHT) {
            scrollWidgetHeight = MAX_HEIGHT;
        }

        openCloseImg.setSize(optionHeight + "px", optionHeight + "px");
        selectionView.setSize((optionWidth) + "px", optionHeight + "px");
        container.setSize((optionWidth + 26) + "px", (scrollWidgetHeight + 15) + "px");


        scroll.setSize(optionWidth + 11, scrollWidgetHeight);
        scroll.resizeBar();
        container.setWidgetPosition(scroll, 5, 10);
    }


    public void setDropDownStyleName(final String styleName) {
        dropDown.setStyleName("fieldArmySelector");
        scroll.setStyleName("selectorOption");
    }

    public void onAttach() {
        super.onAttach();
        fixSize();
    }

    public void clearOptions() {
        selectedIndex = -1;
        totalNumber = -1;
        indexToOption.clear();
        indexToValue.clear();
        dropDownView.clear();
        options.clear();
        values.clear();
    }

    public void addOption(final OptionEAW option, final J value) {
        optionWidth = option.getWidth();
        optionHeight = option.getHeight();
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
        fixSize();
        scroll.resizeBar();

    }


    public void setOptionsStyles(final String defaultStyle, final String hoverStyle, final String selectedStyle) {
        this.defaultStyle = defaultStyle;
        this.hoverStyle = hoverStyle;
        this.selectedStyle = selectedStyle;
    }

    public void selectOptionByValue(final J value) {
        if (debug) {
            Window.alert(value.toString() + ", " + values.contains(value) + ", " + values.indexOf(value));
        }
        if (values.contains(value)) {
            selectOption(values.indexOf(value));
        } else {
            selectOption(0);
        }
    }

    public void selectOption(final int index) {
        try {
            DOM.setInnerHTML(selectionView.getElement(), "");
            if (selectedIndex != -1) {
                indexToOption.get(selectedIndex).removeStyleName(selectedStyle);
                indexToOption.get(selectedIndex).setStyleName(defaultStyle, true);
            }

            selectedIndex = index;
            indexToOption.get(selectedIndex).removeStyleName(defaultStyle);
            indexToOption.get(selectedIndex).removeStyleName(hoverStyle);
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

        } catch (Exception e) {
            Window.alert("Comeon!!: " + e.toString());
        }
    }

    public J getValue() {
        return values.get(selectedIndex);
    }

    public List<J> getValues() {
        return values;
    }

    public Widget getSelectedOption() {
        return options.get(selectedIndex);
    }

    public Object getOption(final int index) {
        return options.get(index);
    }

    public abstract void onChange();
}
