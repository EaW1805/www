package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Image;
import com.eaw1805.www.fieldbattle.tooltips.Tips;

public class ImageButton extends Image {

    public ImageButton(final String url, final String title) {
        super(url);
        if (title != null) {
            Tips.generateTip(this, title);
        }
        setStyleName("pointer");
        addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent mouseOverEvent) {

            }
        });
        final ImageButton mySelf = this;
        // Add handler to change the image on mouse over
        this.addMouseOverHandler(new MouseOverHandler() {

            public void onMouseOver(final MouseOverEvent event) {
                if (mySelf.getUrl().endsWith("Off.png")
                        && !mySelf.getUrl().endsWith("Hover.png")) {
                    mySelf.setUrl(mySelf.getUrl().replace("Off.png", "Hover.png"));
                }
            }
        });
        // Add handler to change the image on mouse out
        this.addMouseOutHandler(new MouseOutHandler() {

            public void onMouseOut(final MouseOutEvent event) {
                if (mySelf.getUrl().endsWith("Hover.png") && !mySelf.getUrl().endsWith("Off.png")) {
                    mySelf.setUrl(mySelf.getUrl().replace("Hover.png", "Off.png"));
                }

            }
        });
    }
}
