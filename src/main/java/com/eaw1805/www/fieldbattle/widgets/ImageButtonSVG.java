package com.eaw1805.www.fieldbattle.widgets;


import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import org.vaadin.gwtgraphics.client.Image;

public class ImageButtonSVG extends Image {
    public ImageButtonSVG(int x, int y, int width, int height, String href, final String title) {
        super(x, y, width, height, href);
        if (title != null) {
            setTitle(title);
        }
        addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent mouseOverEvent) {

            }
        });
        final ImageButtonSVG mySelf = this;
        // Add handler to change the image on mouse over
        this.addMouseOverHandler(new MouseOverHandler() {

            public void onMouseOver(final MouseOverEvent event) {
                if (mySelf.getHref().endsWith("Off.png")
                        && !mySelf.getHref().endsWith("Hover.png")) {
                    mySelf.setHref(mySelf.getHref().replace("Off.png", "Hover.png"));
                }
            }
        });
        // Add handler to change the image on mouse out
        this.addMouseOutHandler(new MouseOutHandler() {

            public void onMouseOut(final MouseOutEvent event) {
                if (mySelf.getHref().endsWith("Hover.png") && !mySelf.getHref().endsWith("Off.png")) {
                    mySelf.setHref(mySelf.getHref().replace("Hover.png", "Off.png"));
                }

            }
        });
    }
}
