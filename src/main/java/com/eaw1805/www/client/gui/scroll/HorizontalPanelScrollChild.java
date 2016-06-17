package com.eaw1805.www.client.gui.scroll;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;

public class HorizontalPanelScrollChild extends HorizontalPanel implements ScrollChild {


    private ScrollVerticalBarEAW scroller;
    private boolean autoResize = false;
    public void setScroller(final ScrollVerticalBarEAW value, final boolean autoResize) {
        scroller = value;
        this.autoResize = autoResize;
    }

    public void onAttach() {
        super.onAttach();
        resizeScrollBar();
    }

    public void add(final Widget w) {
        super.add(w);
        if (autoResize) {
            resizeScrollBar();
        }
    }

    public void insert(com.google.gwt.user.client.ui.IsWidget w, int beforeIndex) {
        super.insert(w, beforeIndex);
        if (autoResize) {
            resizeScrollBar();
        }
    }

    public void insert(com.google.gwt.user.client.ui.Widget w, int beforeIndex) {
        super.insert(w, beforeIndex);
        if (autoResize) {
            resizeScrollBar();
        }
    }

    public boolean remove(com.google.gwt.user.client.ui.Widget w) {
        final boolean removed = super.remove(w);
        if (autoResize) {
            resizeScrollBar();
        }
        return removed;
    }

    public void clear() {
        super.clear();
        if (autoResize) {
            resizeScrollBar();
        }
    }

    public void resizeScrollBar() {
        if (scroller != null) {
            scroller.resizeBar();
        }
    }
}
