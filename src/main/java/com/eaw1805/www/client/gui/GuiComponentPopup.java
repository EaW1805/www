package com.eaw1805.www.client.gui;

import com.google.gwt.user.client.ui.PopupPanel;

public class GuiComponentPopup
        extends GuiComponentAbstract<PopupPanel>
        implements GuiComponent {

    public GuiComponentPopup(final PopupPanel widget) {
        this.widget = widget;
    }

    public void handleEscape() {
        widget.hide();
        gameStore.unRegisterComponent(this, false);
    }

    @Override
    public void handleWave() {
        //do nothing here
    }
}
