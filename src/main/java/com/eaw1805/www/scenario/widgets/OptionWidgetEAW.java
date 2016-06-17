package com.eaw1805.www.scenario.widgets;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.www.fieldbattle.widgets.OptionEAW;

public class OptionWidgetEAW extends OptionEAW {
    public OptionWidgetEAW(int width, int height, Widget... widgets) {
        super(width, height, "");
        //so you remove the default label added by the parent
        clear();
        final HorizontalPanel container = new HorizontalPanel();
        for (Widget widget : widgets) {
            container.add(widget);
        }
        add(container);
    }
}
