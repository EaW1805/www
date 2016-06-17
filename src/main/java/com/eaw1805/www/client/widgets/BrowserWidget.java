package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class BrowserWidget extends AbsolutePanel {

    AbsolutePanel container = new AbsolutePanel();
    List<RefreshAble> history = new ArrayList<RefreshAble>();
    int currentIndex = 0;
    final ImageButton back;
    final ImageButton next;

    public BrowserWidget() {
        back = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        back.setTitle("Go back");
        back.setSize("20px", "20px");
        next = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        next.setSize("20px", "20px");
        next.setTitle("Go forward");
        add(back, 3, 3);
        add(next, 30, 3);
        add(container, 3, 23);
        back.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                goBack();
            }
        });
        next.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                goForward();
            }
        });
    }

    public void refreshAll() {
        for (RefreshAble widget : history) {
            widget.refreshContent();
        }
    }

    public void goToWidget(final RefreshAble widget) {
        container.clear();
        for (int index = history.size() - 1; index > currentIndex; index--) {
            history.remove(index);
        }
        container.add((Widget) widget);
        history.add(widget);
        currentIndex = history.size() - 1;
        back.setVisible(currentIndex > 0);
        next.setVisible(currentIndex < history.size() - 1);
        widget.refreshContent();
    }

    public void addLink(final Widget widget, final RefreshAble link) {
        widget.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                addWidgetAndGo(link);
            }
        }, ClickEvent.getType());
    }

    public void goBack() {
        currentIndex--;
        container.clear();
        container.add((Widget) history.get(currentIndex));
        back.setVisible(currentIndex > 0);
        next.setVisible(currentIndex < history.size() - 1);
        history.get(currentIndex).refreshContent();
    }

    public void goForward() {
        currentIndex++;
        container.clear();
        container.add((Widget) history.get(currentIndex));
        back.setVisible(currentIndex > 0);
        next.setVisible(currentIndex < history.size() - 1);
        history.get(currentIndex).refreshContent();
    }

    public void addWidgetAndGo(final RefreshAble widget) {
        goToWidget(widget);
    }

    public void setSize(int width, int height) {
        setSize(width + "px", height + "px");
        container.setSize((width - 6) + "px", (height - 26) + "px");
    }

}
