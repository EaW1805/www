package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.shared.stores.GameStore;


public class CommandersImage extends AbsolutePanel {

    private CommanderDTO commander;

    public CommandersImage(final CommanderDTO commander) {
        this.commander = commander;
        setSize("125px", "30px");
        final Image img = new Image("http://static.eaw1805.com/img/commanders/s" + GameStore.getInstance().getScenarioId() + "/" + commander.getNationId() + "/" + commander.getIntId() + ".jpg");
        img.setSize("30px", "30px");
        add(img);
        add(new Label(commander.getName()), 35, 2);
    }

    public CommanderDTO getCommander() {
        return commander;
    }
}
