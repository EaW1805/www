package com.eaw1805.www.scenario.views;


import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.army.RankDTO;
import com.eaw1805.www.fieldbattle.widgets.OptionEAW;
import com.eaw1805.www.fieldbattle.widgets.SelectEAW;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.WidgetStore;
import com.eaw1805.www.scenario.widgets.OptionWidgetEAW;

public class CommanderBrushView extends VerticalPanel {

    boolean brushEnabled = true;
    final SelectEAW<NationDTO> nationSelect;
    final TextBox nameBox;
    final SelectEAW<RankDTO> rankSelect;
    final TextBox comcBox;
    final TextBox strBox;
    public CommanderBrushView() {
        getElement().getStyle().setBackgroundColor("grey");
        add(new Label("*Commander Brush*"));
        final CheckBox enabled = new CheckBox("Enabled");
        enabled.setValue(brushEnabled);
        enabled.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {

                brushEnabled = event.getValue();

            }
        });
        add(enabled);

        final Grid grid = new Grid(5, 2);
        add(grid);
        grid.setWidget(0, 0, new Label("Nation :"));
        nationSelect = new SelectEAW<NationDTO>("Select nation") {
            @Override
            public void onChange() {
                //do nothing here
            }
        };
        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            if (nation.getNationId() > 0) {
                nationSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getNationImg(nation.getNationId(), 20), WidgetStore.getLabel(nation.getName())), nation);
            }
        }

        grid.setWidget(0, 1, nationSelect);

        grid.setWidget(1, 0, new Label("Name :"));
        nameBox = new TextBox();
        nameBox.setWidth("50px");
        grid.setWidget(1, 1, nameBox);

        grid.setWidget(2, 0, new Label("Rank :"));
        rankSelect = new SelectEAW<RankDTO>("Select commanders rank") {
            @Override
            public void onChange() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        for (final RankDTO rank : EditorStore.getInstance().getRanks()) {
            rankSelect.addOption(new OptionEAW(120, 15, rank.getName()), rank);
        }
        grid.setWidget(2, 1, rankSelect);

        grid.setWidget(3, 0, new Label("ComC :"));
        comcBox = new TextBox();
        grid.setWidget(3, 1, comcBox);

        grid.setWidget(4, 0, new Label("StrC :"));
        strBox = new TextBox();
        grid.setWidget(4, 1, strBox);
        nationSelect.selectOption(0);
        rankSelect.selectOption(0);
    }

    public NationDTO getNation() {
        return nationSelect.getValue();
    }

    public String getName() {
        return nameBox.getText();
    }

    public RankDTO getRank() {
        return rankSelect.getValue();
    }

    public int getComc() {
        int comc = 0;
        try {
            comc = Integer.parseInt(comcBox.getText());
        } catch (Exception e) {
            //eat it
        }
        return comc;
    }

    public int getStr() {
        int str = 0;
        try {
            str = Integer.parseInt(strBox.getText());
        } catch (Exception e) {
            //eat it
        }
        return str;
    }

    public boolean isBrushEnabled() {
        return brushEnabled;
    }

}
