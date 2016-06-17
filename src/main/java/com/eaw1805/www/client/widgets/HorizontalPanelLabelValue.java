package com.eaw1805.www.client.widgets;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A simple widget that returns a horizontal panel with 2 widgets inside..
 * just to make our life easier..
 */
public class HorizontalPanelLabelValue
        extends HorizontalPanel {

    AbsolutePanel dist;

    public HorizontalPanelLabelValue(final int dist1, final Widget label, final int dist2, final Widget value) {
        dist = new AbsolutePanel();
        dist.setWidth(dist1 + "px");
        this.add(dist);
        this.add(label);
        dist = new AbsolutePanel();
        dist.setWidth(dist2 + "px");
        this.add(dist);
        this.add(value);
    }

    public HorizontalPanelLabelValue(final int dist1, final Widget label) {
        dist = new AbsolutePanel();
        dist.setWidth(dist1 + "px");
        this.add(dist);
        this.add(label);
    }
}
