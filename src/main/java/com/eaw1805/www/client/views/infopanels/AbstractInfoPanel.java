package com.eaw1805.www.client.views.infopanels;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.www.shared.stores.InfoPanelsStore;

public class AbstractInfoPanel
        extends AbsolutePanel {

    public AbstractInfoPanel() {
        super();
        this.addDomHandler(new MouseDownHandler() {
            public void onMouseDown(final MouseDownEvent mouseDownEvent) {
                if (InfoPanelsStore.getInstance().isInfoPanelLocked()) {
                    mouseDownEvent.stopPropagation();
                }
            }
        }, MouseDownEvent.getType());
    }

}
