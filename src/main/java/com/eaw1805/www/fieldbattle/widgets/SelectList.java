package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class SelectList<J> extends VerticalPanel {

    final List<Widget> options = new ArrayList<Widget>();
    final List<J> values = new ArrayList<J>();
    private final ScrollVerticalBar scroll;
    private final VerticalPanel dropDownView;
    List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

    //    static String hoverStyle = "optionHover320x30";
//    static String selectedStyle = "optionSelected320x30";
    static String hoverBG = "yellow";
    static String selectedBG = "green";
    int selectedIndex = -1;

    public SelectList() {
        dropDownView = new VerticalPanel();
        scroll = new ScrollVerticalBar(dropDownView, 15);
        add(scroll);
    }


    public void setSize(final int width, final int height) {
        scroll.setSize(width, height);
        scroll.setStyleName("selectorOption");
        scroll.getElement().getStyle().setPadding(4, Style.Unit.PX);
    }

    public void addOption(final Widget option, final J value) {
        final int index = options.size();
        options.add(option);
        values.add(value);
//        scroll.add(option);

        handlers.add(option.addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent mouseOverEvent) {
                if (selectedIndex != index) {
                    option.getElement().getStyle().setBackgroundColor(hoverBG);
                }
            }
        }, MouseOverEvent.getType()));

        handlers.add(option.addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent mouseOutEvent) {
                if (selectedIndex != index) {
                    option.getElement().getStyle().setBackgroundColor("");
                }
            }
        }, MouseOutEvent.getType()));

        handlers.add(option.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                selectOption(index);
                onChange(option, value);

            }
        }, ClickEvent.getType()));
        dropDownView.add(option);
        scroll.resizeBar();
    }

    public abstract void onChange(final Widget option, final J value);

    public Widget getSelectedOption() {
        //be sure you are on the right size
        if (selectedIndex != -1
                && selectedIndex < options.size()) {
            return options.get(selectedIndex);
        } else {
            return null;
        }
    }

    public boolean hasSelectedOption() {
        return selectedIndex != -1;
    }

    public J getSelectedValue() {
        if (selectedIndex != -1) {
            return values.get(selectedIndex);
        } else {
            return null;
        }
    }

    public void selectOption(final int index) {
        if (selectedIndex != -1) {
            getSelectedOption().getElement().getStyle().setBackgroundColor("");
        }
        selectedIndex = index;
        final Widget option = getSelectedOption();
        option.getElement().getStyle().setBackgroundColor(selectedBG);
    }

    public void selectOptionByValue(final J value, final Comparator<J> comparator, final boolean executeChange) {
        int index = 0;
        for (J currentValue : values) {
            if (comparator.compare(value, currentValue) == 0) {
                selectOption(index);
                if (executeChange) {
                    onChange(getSelectedOption(), getSelectedValue());
                }
                break;
            }
            index++;
        }
    }

    public void ensureVisible(final Widget option) {
        scroll.ensureVisible(option);
    }

    public void clearOptions() {
        for (Widget option : options) {
            option.getElement().getStyle().setBackgroundColor("");
        }
        for (HandlerRegistration handler : handlers) {
            handler.removeHandler();
        }
        handlers.clear();
        options.clear();
        values.clear();
        selectedIndex = -1;
        dropDownView.clear();
    }

    /**
     * Call this function if you want to un-select the current selected option.
     */
    public void deselect() {
        //reset selected index and colour
        final Widget option = getSelectedOption();
        if (option != null) {
            option.getElement().getStyle().setBackgroundColor("");
        }
        selectedIndex = -1;
    }

    public int getScrollPosition() {
        return scroll.getScrollArea().getVerticalScrollPosition();
    }

    public void setScrollPosition(int pos) {
        scroll.getScrollArea().setVerticalScrollPosition(pos);

    }
}
