package com.eaw1805.www.fieldbattle.views.layout.infopanels.mini;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;


public class LogTitleInfoPanel extends AbsolutePanel {

    public LogTitleInfoPanel(String text) {
        setSize("229px", "22px");
        setStyleName("battleLogTitlePanel");
        add(createLabel(text), 3, 3);

    }

    public Label createLabel(String text) {
        Label out = new Label(text);
        out.setWidth("223px");
        out.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        out.setStyleName("clearFontMedMini");
        return out;
    }
}
