package com.eaw1805.www.scenario.views.menu;

import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.scenario.widgets.MenuPopup;

import java.util.Map;


public class CommanderMenu extends MenuPopup {

    private NationDTO nation;

    public CommanderMenu(Widget parent) {
        super(parent);
    }

    @Override
    public void initChildren() {
        if (nation == null) {
            return;
        }
        Map<Integer, Map<Integer, Map<Integer, Map<Integer, CommanderDTO>>>> commMap = EditorStore.getInstance().getCommanders();
        for (Map.Entry<Integer, Map<Integer, Map<Integer, Map<Integer, CommanderDTO>>>> entry : commMap.entrySet()) {
            for (Map.Entry<Integer, Map<Integer, Map<Integer, CommanderDTO>>> entry2 : entry.getValue().entrySet()) {
                for (Map.Entry<Integer, Map<Integer, CommanderDTO>> entry3 : entry2.getValue().entrySet()) {
                    for (final CommanderDTO commander : entry3.getValue().values()) {
                        if (commander.getNationId() == nation.getNationId()) {
                            addChild(commander.getName(), new BasicHandler() {
                                @Override
                                public void run() {
//                                    EditorPanel.getInstance().getCommBrush().setCommander(commander);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    public void reset(final NationDTO nation) {
        this.nation = nation;
        container.clear();
        initChildren();
    }
}
