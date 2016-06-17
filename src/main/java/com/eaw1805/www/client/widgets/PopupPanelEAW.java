package com.eaw1805.www.client.widgets;


import com.google.gwt.user.client.ui.PopupPanel;
import com.eaw1805.www.client.gui.GuiComponentPopup;

/**
 * Implementation of popup that hide itself when escape key is pressed
 */
public class PopupPanelEAW
        extends PopupPanel {

    GuiComponentPopup comp;
    public PopupPanelEAW() {
        super();
        comp = new GuiComponentPopup(this);
    }

    public void onDetach() {
        super.onDetach();
        comp.unRegisterComponent();

    }

    public void onAttach() {
        super.onAttach();
        comp.registerComponent();
    }



}
