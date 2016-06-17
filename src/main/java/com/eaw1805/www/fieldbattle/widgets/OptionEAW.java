package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;

public class OptionEAW extends AbsolutePanel {
    private final int width;
    private final int height;
    public OptionEAW(int width, int height, String text) {
        setSize(width + "px", height + "px");
        Label textLabel = new Label(text);
        textLabel.setStyleName("clearFontMedMini");
        add(textLabel);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
