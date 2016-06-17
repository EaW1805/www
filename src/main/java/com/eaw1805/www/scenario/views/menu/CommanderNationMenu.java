package com.eaw1805.www.scenario.views.menu;

import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.scenario.widgets.MenuPopup;

public class CommanderNationMenu extends MenuPopup {

    public CommanderNationMenu(Widget parent) {
        super(parent);
    }

    @Override
    public void initChildren() {
        for (final NationDTO nation : EditorStore.getInstance().getNations()) {
            addChild(nation.getName(), new BasicHandler() {
                @Override
                public void run() {
//                    EditorPanel.getInstance().getCommBrush().setNation(nation);
                    hide();
                }
            });
        }
    }


}
