package com.eaw1805.www.client.widgets;

import org.vaadin.gwtgraphics.client.Line;

public class MapLine
        extends Line {

    int xPos = 0;
    int yPos = 0;

    int state = 0;

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @return the xPos
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * @return the yPos
     */
    public int getyPos() {
        return yPos;
    }

    public MapLine(final int xPos1, final int yPos1, final int xPos2, final int yPos2) {
        super(xPos1, yPos1, xPos2, yPos2);
    }


}
