package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;

public abstract class HideAbleAbsolutePanel extends AbsolutePanel implements HideAble {

    protected boolean show = false;
    protected boolean lockOpen = false;

    public HideAbleAbsolutePanel() {
        addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent mouseOverEvent) {
                showPanel();
            }
        }, MouseOverEvent.getType());
        addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent mouseOutEvent) {
                if (!lockOpen) {
                    hidePanel();
                }
            }
        }, MouseOutEvent.getType());
    }


    @Override
    public void updatePanelPosition() {
        if (show || lockOpen) {
            showPanel();
        } else {
            hidePanel();
        }
    }

    public void showAndLock() {
        lockOpen = true;
        updatePanelPosition();
    }

    public void unlockOpen() {
        lockOpen = false;
        updatePanelPosition();
    }


}
