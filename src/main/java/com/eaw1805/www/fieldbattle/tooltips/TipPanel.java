package com.eaw1805.www.fieldbattle.tooltips;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


public class TipPanel extends VerticalPanel {

    public TipPanel(final String tip) {
        setStyleName("tipPanel");
        final Label tipLbl = new Label(tip);
        tipLbl.setWidth("300px");
        add(tipLbl);
    }
}
