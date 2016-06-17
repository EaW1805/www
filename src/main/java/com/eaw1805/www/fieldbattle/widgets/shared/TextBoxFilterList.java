package com.eaw1805.www.fieldbattle.widgets.shared;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.www.fieldbattle.widgets.ScrollVerticalBar;
import com.eaw1805.www.fieldbattle.widgets.SelectList;

import java.util.ArrayList;
import java.util.List;

public abstract class TextBoxFilterList<J> extends SelectList<J> {
    List<String> searchKeys = new ArrayList<String>();
    List<Widget> options = new ArrayList<Widget>();
    List<J> values = new ArrayList<J>();
    PopupPanel panel = new PopupPanel(true);

    public TextBoxFilterList(final TextBoxEditable box) {
        super();
        panel.setWidget(this);
        panel.getElement().getStyle().setZIndex(111111);
        box.initHandler(new BasicHandler() {
            @Override
            public void run() {
                try {
                //first hide all options

                TextBoxFilterList.super.clearOptions();
                //then find who's options match better
                if (box.getText().isEmpty()) {
                    for (int index = 0; index < searchKeys.size(); index++) {
                        addOption(options.get(index), values.get(index));
                    }
                } else {

                    for (int index = 0; index < searchKeys.size(); index++) {
                        if (searchKeys.get(index).toLowerCase().contains(box.getText().toLowerCase())) {
                            addOption(options.get(index), values.get(index));
                        }
                    }
                }

                panel.showRelativeTo(box);


                } catch (Exception e) {
                    Window.alert(e.toString());
                }
            }
        });
        box.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {

                panel.showRelativeTo(box);
            }
        });
    }



    public void hidePanel() {
        panel.hide();
    }

    public void addOption(final Widget option, final J value, final String searchKey) {
        super.addOption(option, value);
        searchKeys.add(searchKey);
        options.add(option);
        values.add(value);
    }


    public void clearOptions() {
        super.clearOptions();
        searchKeys.clear();
        options.clear();
        values.clear();
    }

}
