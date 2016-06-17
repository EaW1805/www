package com.eaw1805.www.client.views.extras;

import com.eaw1805.www.client.widgets.ImageButton;

/**
 * Extends Image by adding RegionId information.
 */
public class RegionImage extends ImageButton {

    private int regionId;

    public RegionImage(final String url) {
        super(url);
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(final int regionId) {
        this.regionId = regionId;
    }
}
