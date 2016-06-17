package com.eaw1805.www.scenario.views;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.ArmyTypeInfoPanel;
import com.eaw1805.www.fieldbattle.widgets.OptionEAW;
import com.eaw1805.www.fieldbattle.widgets.SelectEAW;
import com.eaw1805.www.fieldbattle.widgets.ToolTipPanel;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.WidgetStore;
import com.eaw1805.www.scenario.widgets.OptionWidgetEAW;


public class ArmyBrushView extends VerticalPanel {


    final SelectEAW<NationDTO> nationSelect;
    final SelectEAW<BattalionDTO>[] battSelects;
    boolean brushEnabled = true;
    public ArmyBrushView() {

        getElement().getStyle().setBackgroundColor("grey");
        add(new Label("*Army Brush*"));
        final CheckBox enable = new CheckBox("enabled");
        enable.setValue(brushEnabled);
        enable.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                brushEnabled = event.getValue();
            }
        });
        add(enable);



        nationSelect = new SelectEAW<NationDTO>("Select Nation") {
            @Override
            public void onChange() {
                for (int index = 0; index < 6; index++) {
                    battSelects[index].clearOptions();
                    battSelects[index].addOption(new OptionEAW(130, 15, "None Selected"), null);
                    if (getValue() != null) {
                        for (ArmyTypeDTO armyType : EditorStore.getInstance().getArmyTypesByNation(getValue().getNationId())) {
                            final BattalionDTO batt = new BattalionDTO();
                            if (getValue().getNationId() == NationConstants.NATION_MOROCCO
                                    || getValue().getNationId() == NationConstants.NATION_EGYPT
                                    || getValue().getNationId() == NationConstants.NATION_OTTOMAN) {
                                batt.setHeadcount(1000);

                            } else {
                                batt.setHeadcount(800);
                            }
                            batt.setOrder(index + 1);
                            batt.setExperience(armyType.getMaxExp());
                            batt.setEmpireArmyType(armyType);
                            OptionWidgetEAW option = new OptionWidgetEAW(130, 15, WidgetStore.getBattalionImg(getValue().getNationId(), batt.getEmpireArmyType().getIntId(), 20), WidgetStore.getLabel(batt.getEmpireArmyType().getName()));
                            new ToolTipPanel(option, true) {

                                @Override
                                public void generateTip() {
                                    try {
                                        setTooltip(new ArmyTypeInfoPanel(batt.getEmpireArmyType(),
                                                batt));
                                    } catch (Exception e) {

                                    }
                                }
                            };
                            battSelects[index].addOption(option, batt);
                        }

                    }
                    battSelects[index].selectOption(0);
                }
            }
        };
        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            if (nation.getNationId() > 0) {
                nationSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getNationImg(nation.getNationId(), 20), WidgetStore.getLabel(nation.getName())), nation);
            }
        }

        battSelects = new SelectEAW[6];
        for (int index =0; index < 6; index++) {
            battSelects[index] = new SelectEAW<BattalionDTO>("Select Battalion") {
                @Override
                public void onChange() {
                    //do nothing here
                }
            };
            battSelects[index].addOption(new OptionEAW(130, 15, "None Selected"), null);
        }

        Grid grid = new Grid(7, 2);
        grid.setWidget(0, 0, new Label("Nation :"));
        grid.setWidget(0, 1, nationSelect);
        for (int index =0; index< 6; index++) {
            grid.setWidget(1 + index, 0, new Label("Battalion " + (index + 1) + " :"));
            grid.setWidget(1 + index, 1, battSelects[index]);
        }
        nationSelect.selectOption(0);
        add(grid);
    }

    public NationDTO getNation() {
        return nationSelect.getValue();
    }

    public BattalionDTO getBatt1() {
        return battSelects[0].getValue();
    }

    public BattalionDTO getBatt2() {
        return battSelects[1].getValue();
    }

    public BattalionDTO getBatt3() {
        return battSelects[2].getValue();
    }

    public BattalionDTO getBatt4() {
        return battSelects[3].getValue();
    }

    public BattalionDTO getBatt5() {
        return battSelects[4].getValue();
    }

    public BattalionDTO getBatt6() {
        return battSelects[5].getValue();
    }

    public boolean isBrushEnabled() {
        return brushEnabled;
    }
}
