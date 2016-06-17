package com.eaw1805.www.client.widgets;

import org.vaadin.gwtgraphics.client.Image;


public class MapImage
        extends Image {

    /**
     * The xPos-tile position of the image on the map
     */
    int xPos = 0;

    /**
     * The yPos-tile position of the image
     */
    int yPos = 0;

    /**
     * The id of the object the image represents
     */
    int id = 0;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final int id) {
        this.id = id;
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

    /**
     * @param xPos the xPos to set
     */
    public void setxPos(final int xPos) {
        this.xPos = xPos;
    }

    /**
     * @param yPos the yPos to set
     */
    public void setyPos(final int yPos) {
        this.yPos = yPos;
    }

    public MapImage(final int xPos, final int yPos, final int width, final int height, final String href) {
        super(xPos, yPos, width, height, href);
    }

}
