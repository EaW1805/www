package com.eaw1805.www.fieldbattle.widgets;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Image;
import com.eaw1805.www.fieldbattle.stores.utils.BasicHandler;
import com.eaw1805.www.fieldbattle.tooltips.Tips;

import java.util.ArrayList;
import java.util.List;

public class PushButton extends Image {
    private List<BasicHandler> pushHandlers = new ArrayList<BasicHandler>();
    boolean pressed = false;

    public PushButton(final String url, final String title) {
        super(url);
        setStyleName("pointer");
        if (title != null) {
            Tips.generateTip(this, title);

        }
        addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent mouseOverEvent) {

            }
        });
        final PushButton mySelf = this;
        // Add handler to change the image on mouse over
        this.addMouseOverHandler(new MouseOverHandler() {

            public void onMouseOver(final MouseOverEvent event) {
                if (!pressed) {
                    if (mySelf.getUrl().endsWith("Off.png")
                            && !mySelf.getUrl().endsWith("Hover.png")) {
                        mySelf.setUrl(mySelf.getUrl().replace("Off.png", "Hover.png"));
                    }
                }
            }
        });
        // Add handler to change the image on mouse out
        this.addMouseOutHandler(new MouseOutHandler() {

            public void onMouseOut(final MouseOutEvent event) {
                if (!pressed) {
                    if (mySelf.getUrl().endsWith("Hover.png") && !mySelf.getUrl().endsWith("Off.png")) {
                        mySelf.setUrl(mySelf.getUrl().replace("Hover.png", "Off.png"));
                    }
                }
            }
        });
        addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                pressed = !pressed;
                if (pressed) {
                    mySelf.setUrl(mySelf.getUrl().replace("Off.png", "Hover.png"));
                } else {
                    mySelf.setUrl(mySelf.getUrl().replace("Hover.png", "Off.png"));
                }
                executeHandlers();
            }
        });
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(final boolean value) {
        pressed = value;
        if (pressed) {
            setUrl(getUrl().replace("Off.png", "Hover.png"));
            executeHandlers();
        } else {
            setUrl(getUrl().replace("Hover.png", "Off.png"));
        }
    }

    private void executeHandlers() {
        for (BasicHandler handler : pushHandlers) {
            handler.run();
        }
    }

    public void addPushHandler(final BasicHandler handler) {
        pushHandlers.add(handler);
    }

}
