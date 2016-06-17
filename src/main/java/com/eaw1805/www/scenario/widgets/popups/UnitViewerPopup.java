package com.eaw1805.www.scenario.widgets.popups;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.ScrollVerticalBar;

import java.util.Collection;
import java.util.List;


public abstract class UnitViewerPopup<J> extends PopupPanel {


    public UnitViewerPopup(final Collection<J> units) {
        getElement().getStyle().setBackgroundColor("grey");
        setSize("800px", "478px");

        VerticalPanel container = new VerticalPanel();
        final HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setWidth("752px");
        final VerticalPanel side1 = new VerticalPanel();
        final VerticalPanel side2 = new VerticalPanel();

        hPanel.add(side1);
        hPanel.add(side2);

        final ScrollVerticalBar scrollPanel = new ScrollVerticalBar(hPanel);
        scrollPanel.setSize(766, 396);
        container.add(scrollPanel);


        int column = 0;
        for (J unit : units) {
            if (column == 0) {
                side1.add(getUnitWidget(unit));
                column = 1;
            } else {
                side2.add(getUnitWidget(unit));
                column = 0;
            }
       }

        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png", "close panel");
        imgX.setStyleName("pointer");
        imgX.setTitle("Close popup");
        container.add(imgX);
        imgX.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                hide();
            }
        });

        setWidget(container);
        imgX.setSize("36px", "36px");
    }

    public abstract Widget getUnitWidget(J unit);
}
