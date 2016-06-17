package com.eaw1805.www.client.widgets;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Image;

import java.util.ArrayList;
import java.util.List;

public class MyPushButton extends Image {
    private List<BasicHandler> pushHandlers = new ArrayList<BasicHandler>();
    boolean pressed = false;
    final String titlePushed, titleReleased;

    public MyPushButton(final String url, final String titlePushed, final String titleReleased) {
        super(url);
        this.titlePushed = titlePushed;
        this.titleReleased = titleReleased;
        setStyleName("pointer");
        setTitle(titleReleased);
        addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent mouseOverEvent) {

            }
        });
        final MyPushButton mySelf = this;
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
                    setTitle(titlePushed);
                    mySelf.setUrl(mySelf.getUrl().replace("Off.png", "Hover.png"));
                } else {
                    setTitle(titleReleased);
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
            setTitle(titlePushed);
            setUrl(getUrl().replace("Off.png", "Hover.png"));
            executeHandlers();
        } else {
            setTitle(titleReleased);
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
