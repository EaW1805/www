package com.eaw1805.www.fieldbattle.views.layout.infopanels;


import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

public class SingleLogInfoPanel extends AbsolutePanel {
    public SingleLogInfoPanel(String log) {
        setSize("229px", "38px");
        setStyleName("singleBattleLogPanel");
        add(createLabel(log), 3, 3);
    }

    public Label createLabel(String text) {
        HTML out = new HTML(text);
        out.setStyleName("clearFontMedMini");
        return out;
    }


}
