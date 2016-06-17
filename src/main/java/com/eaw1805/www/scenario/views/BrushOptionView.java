package com.eaw1805.www.scenario.views;


import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;

public class BrushOptionView extends AbsolutePanel {

    final Label title = new Label();
    public BrushOptionView(String name) {
        setSize("170px", "23px");
        title.setText(name);
        add(title, 3, 3);
    }

    public void setName(String name) {
        title.setText(name);
    }
}
