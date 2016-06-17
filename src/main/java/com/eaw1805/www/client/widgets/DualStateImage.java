package com.eaw1805.www.client.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.shared.stores.SoundStore;

public class DualStateImage
        extends Image {

    private boolean selected = false;
    private int id;

    public DualStateImage() {
        super();
        addImageUrlChangeLogic();
    }

    public DualStateImage(final Element element) {
        super(element);
        addImageUrlChangeLogic();
    }

    public DualStateImage(final ImageResource resource) {
        super(resource);
        addImageUrlChangeLogic();
    }

    public DualStateImage(final String url, final int left, final int top, final int width, final int height) {
        super(url, left, top, width, height);
        addImageUrlChangeLogic();
    }

    private void addImageUrlChangeLogic() {
        final Image mySelf = this;
        // Add handler to change the image on mouse over
        this.addMouseOverHandler(new MouseOverHandler() {

            public void onMouseOver(final MouseOverEvent event) {
                if (!mySelf.getUrl().endsWith("NA.png") && !mySelf.getUrl().endsWith("Slc.png")) {
                    mySelf.setUrl(mySelf.getUrl().replace(".png", "Slc.png"));
                }
            }
        });
        // Add handler to change the image on mouse out
        this.addMouseOutHandler(new MouseOutHandler() {

            public void onMouseOut(final MouseOutEvent event) {
                if (!mySelf.getUrl().endsWith("NA.png") && mySelf.getUrl().endsWith("Slc.png") && !isSelected()) {
                    mySelf.setUrl(mySelf.getUrl().replace("Slc.png", ".png"));
                }

            }
        });
        // Add handler to change the image on click
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                SoundStore.getInstance().playClickWooden();
                setSelected(true);
            }
        }).addToElement(this.getElement()).register();
    }

    public DualStateImage(final String url) {
        super(url);
        addImageUrlChangeLogic();
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    public final boolean isSelected() {
        return selected;
    }

    public boolean deselect() {
        if (!this.getUrl().endsWith("NA.png") && this.getUrl().endsWith("Slc.png") && isSelected()) {
            this.setUrl(this.getUrl().replace("Slc.png", ".png"));
            this.setSelected(false);
            return true;
        } else {
            return false;
        }

    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


}
