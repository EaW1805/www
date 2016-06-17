package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.user.client.ui.Image;
import com.eaw1805.data.dto.web.army.BrigadeDTO;


public class ImageBrigade extends Image {
    BrigadeDTO brigade;

    public ImageBrigade(String url) {
        super(url);
    }

    public BrigadeDTO getBrigade() {
        return brigade;
    }

    public void setBrigade(BrigadeDTO brigade) {
        this.brigade = brigade;
    }
}
