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

public class ImageButton
        extends Image {

    private boolean selected = false;
    private boolean disabled = false;
    private int id;

    public ImageButton() {
        super();
        addImageUrlChangeLogic();
    }

    public ImageButton(final Element element) {
        super(element);
        addImageUrlChangeLogic();
    }

    public ImageButton(final ImageResource resource) {
        super(resource);
        addImageUrlChangeLogic();
    }

    /**
     * @wbp.parser.constructor
     */
    public ImageButton(final String url, final int left, final int top, final int width, final int height) {
        super(url, left, top, width, height);
        addImageUrlChangeLogic();
    }

    public ImageButton(final String url) {
        super(url);
        addImageUrlChangeLogic();
    }

    private void addImageUrlChangeLogic() {

        final Image mySelf = this;
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
        // Add handler to change the image on click
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                SoundStore.getInstance().playClickWooden();
                setSelected(true);
            }
        }).addToElement(this.getElement()).register();
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
        updateImageURL();
    }

    public boolean isSelected() {
        return selected;
    }

    public void updateImageURL() {
        if (disabled) {
            if (getUrl().endsWith("Hover.png")) {
                setUrl(getUrl().replace("Hover.png", "NA.png"));
            } else if (getUrl().endsWith("On.png")) {
                setUrl(getUrl().replace("On.png", "NA.png"));
            } else if (getUrl().endsWith("Off.png")) {
                setUrl(getUrl().replace("Off.png", "NA.png"));
            }
        } else {
            if (selected) {
                if (getUrl().endsWith("Hover.png")) {
                    setUrl(getUrl().replace("Hover.png", "On.png"));
                } else if (getUrl().endsWith("Off.png")) {
                    setUrl(getUrl().replace("Off.png", "On.png"));
                } else if (getUrl().endsWith("NA.png")) {
                    setUrl(getUrl().replace("NA.png", "On.png"));
                }
            } else {
                if (getUrl().endsWith("On.png")) {
                    setUrl(getUrl().replace("On.png", "Off.png"));
                } else if (getUrl().endsWith("NA.png")) {
                    setUrl(getUrl().replace("NA.png", "Off.png"));
                }
            }
        }
    }

    public void deselect() {
        setSelected(false);
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDisabled(final boolean value) {
        disabled = value;
        updateImageURL();
    }

    public boolean isDisabled() {
        return disabled;
    }
}
