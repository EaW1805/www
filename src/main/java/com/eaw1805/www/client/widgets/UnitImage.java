package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.PopupPanel;

public class UnitImage extends DualStateImage {

    private PopupPanel popupInfo;

    public UnitImage(final String url, final PopupPanel unitInfoPopup) {
        super(url);
        this.setPopupInfo(unitInfoPopup);
        this.setHeight("64px");
        this.setWidth("64px");
        this.setStyleName("pointer");
        if (unitInfoPopup != null) {
            addPopupInfoToImage(unitInfoPopup);
        }
    }

    public final void addPopupInfoToImage(final PopupPanel unitInfoPopup) {
        unitInfoPopup.setAutoHideEnabled(true);
        final UnitImage mySelf = this;
        this.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(final MouseOverEvent event) {
                unitInfoPopup.showRelativeTo(mySelf);
                unitInfoPopup.show();

            }
        });

        this.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                unitInfoPopup.hide();

            }
        });
    }

    public PopupPanel getPopupInfo() {
        return this.popupInfo;
    }

    /**
     * @param popupInfo the popupInfo to set
     */
    public final void setPopupInfo(final PopupPanel popupInfo) {
        this.popupInfo = popupInfo;
    }

}
