package com.eaw1805.www.scenario.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.widgets.MenuElementWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandersPanel extends PopupPanel {

    public CommandersPanel() {
        getElement().getStyle().setBackgroundColor("grey");
        setAutoHideEnabled(true);

        final HorizontalPanel container = new HorizontalPanel();
        setWidget(container);
        final VerticalPanel[] nationPanels = new VerticalPanel[17];

        final Map<Integer, List<CommanderDTO>> nationCommanders = new HashMap<Integer, List<CommanderDTO>>();
        for (final NationDTO nation : EditorStore.getInstance().getNations()) {
            nationCommanders.put(nation.getNationId(), new ArrayList<CommanderDTO>());
        }

        for (final Map.Entry<Integer, Map<Integer, Map<Integer, Map<Integer, CommanderDTO>>>> entry : EditorStore.getInstance().getCommanders().entrySet()) {
            for (final Map.Entry<Integer, Map<Integer, Map<Integer, CommanderDTO>>> entry2 : entry.getValue().entrySet()) {
                for (final Map.Entry<Integer, Map<Integer, CommanderDTO>> entry3 : entry2.getValue().entrySet()) {
                    for (final CommanderDTO comm : entry3.getValue().values()) {
                        nationCommanders.get(comm.getNationId()).add(comm);
                    }
                }
            }
        }

        for (final NationDTO nation : EditorStore.getInstance().getNations()) {
            if (nation.getNationId() == -1) {
                continue;
            }
            nationPanels[nation.getNationId() - 1] = new VerticalPanel();
            container.add(nationPanels[nation.getNationId() - 1]);
            nationPanels[nation.getNationId() - 1].add(new Label("*" + nation.getName() + "*"));
            for (CommanderDTO comm : nationCommanders.get(nation.getNationId())) {
                nationPanels[nation.getNationId() - 1].add(new Label(comm.getName()));
            }
            final Label addNew = new Label("[add new]");
            addNew.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
//                    new CreateCommanderPanel(nation, CommandersPanel.this).show();

                }
            });
            nationPanels[nation.getNationId() - 1].add(addNew);
        }
    }
}
