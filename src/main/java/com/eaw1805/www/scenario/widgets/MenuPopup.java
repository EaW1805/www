package com.eaw1805.www.scenario.widgets;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;

public abstract class MenuPopup extends PopupPanel {
    final Widget parent;
    protected VerticalPanel container = new VerticalPanel();
    public MenuPopup(Widget parent) {
        this.parent = parent;
        setAutoHideEnabled(true);
        getElement().getStyle().setBackgroundColor("grey");
        add(container);
    }

    public void addChild(String name, BasicHandler handler) {
        container.add(new MenuElementWidget(name, handler));

    }

    public void showMenu() {
        int x = parent.getAbsoluteLeft();
        int y = parent.getAbsoluteTop();
        setPopupPosition(x + parent.getOffsetWidth(), y);
        show();
    }

    public abstract void initChildren();
}
