package com.eaw1805.www.scenario.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.www.scenario.stores.EditorStore;



public class CommandersNamePanel extends PopupPanel {

    public CommandersNamePanel(final NationDTO nation) {
        getElement().getStyle().setBackgroundColor("grey");
        VerticalPanel container = new VerticalPanel();
        setWidget(container);
        final TextBox[] boxes = new TextBox[EditorStore.getInstance().getCommanderNamesByNation(nation.getNationId()).size()];
        int index = 0;
        for (final String name : EditorStore.getInstance().getCommanderNamesByNation(nation.getNationId())) {
            boxes[index] = new TextBox();
            boxes[index].setText(name);
            container.add(boxes[index]);
            index++;
        }

        final TextBox box = new TextBox();
        container.add(box);
        Button ok = new Button("UPDATE");
        ok.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                EditorStore.getInstance().getCommanderNamesByNation(nation.getNationId()).clear();
                for (TextBox curBox : boxes) {
                    if (!curBox.getText().isEmpty()) {
                        EditorStore.getInstance().getCommanderNamesByNation(nation.getNationId()).add(curBox.getText());
                    }
                }
                if (!box.getText().isEmpty()) {
                    EditorStore.getInstance().getCommanderNamesByNation(nation.getNationId()).add(box.getText());
                }
                //destroy the current panel
                hide();
                //create an new panel
                new CommandersNamePanel(nation).show();
            }
        });
        container.add(ok);
        final Button close = new Button("Close");
        close.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                hide();
            }
        });
        container.add(close);

    }

}
