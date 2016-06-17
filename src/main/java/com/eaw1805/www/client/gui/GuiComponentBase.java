package com.eaw1805.www.client.gui;

import com.google.gwt.user.client.ui.Widget;

public class GuiComponentBase
        extends GuiComponentAbstract<Widget>
        implements GuiComponent {

    public GuiComponentBase(final Widget widget) {
        this.widget = widget;
    }

    public void handleEscape() {
        widget.removeFromParent();
        gameStore.unRegisterComponent(this, false);
    }

    public void handleWave() {
        //do nothing here for this
    }
}
