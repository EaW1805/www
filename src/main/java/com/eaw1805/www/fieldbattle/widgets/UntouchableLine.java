package com.eaw1805.www.fieldbattle.widgets;

import org.vaadin.gwtgraphics.client.Line;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 7/22/13
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class UntouchableLine extends Line {
    public UntouchableLine(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
        setStyleName("disablePointerEvents");
    }
}
