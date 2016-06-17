package com.eaw1805.www.fieldbattle.widgets;

import org.vaadin.gwtgraphics.client.Image;


public class UntouchableImage extends Image {
    public UntouchableImage(int x, int y, int width, int height, String href) {
        super(x, y, width, height, href);
        setStyleName("disablePointerEvents");
    }
}
