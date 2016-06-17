package com.eaw1805.www.scenario.widgets;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;

public class MenuElementWidget extends AbsolutePanel {

    Label namelbl;

    public MenuElementWidget(final String name, final BasicHandler handler) {
        initWidget(name);
        initHandler(handler);

    }

    private void initWidget(String name) {
        setSize("170px", "23px");
        namelbl = new Label(name);
        add(namelbl, 3, 3);
    }

    public void setName(String name) {
        namelbl.setText(name);
    }

    public void initHandler(final BasicHandler handler) {
        addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                handler.run();
            }
        }, ClickEvent.getType());
    }
    
    public MenuElementWidget(final String name) {
        initWidget(name);
    }


}
