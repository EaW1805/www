package com.eaw1805.www.scenario.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.army.RankDTO;
import com.eaw1805.www.fieldbattle.widgets.OptionEAW;
import com.eaw1805.www.fieldbattle.widgets.SelectEAW;
import com.eaw1805.www.scenario.stores.EditorStore;


public class CreateCommanderPanel extends VerticalPanel {

    final CommandersPanel parent;
    final NationDTO nation;
    public CreateCommanderPanel(final NationDTO nation, final CommandersPanel parent) {
        this.parent = parent;
        this.nation = nation;
        getElement().getStyle().setBackgroundColor("grey");
        Grid grid = new Grid(5, 2);
        add(grid);
        grid.setWidget(0, 0, new Label("Name:"));
        final TextBox nameBox = new TextBox();
        grid.setWidget(0, 1, nameBox);
        grid.setWidget(1, 0, new Label("Rank:"));
        final SelectEAW<RankDTO> rankSelect = new SelectEAW<RankDTO>("Rank") {
            @Override
            public void onChange() {
                //do nothing here.
            }
        };
        for (RankDTO rank : EditorStore.getInstance().getRanks()) {
            rankSelect.addOption(new OptionEAW(120, 15, rank.getName()), rank);
        }

        grid.setWidget(1, 1, rankSelect);
        grid.setWidget(2, 0, new Label("comc:"));
        final TextBox comcBox = new TextBox();
        grid.setWidget(2, 1, comcBox);
        grid.setWidget(3, 0, new Label("strc:"));

        final TextBox strcBox = new TextBox();
        grid.setWidget(3, 1, strcBox);
    }

}
