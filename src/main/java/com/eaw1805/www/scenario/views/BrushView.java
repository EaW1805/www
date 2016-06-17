package com.eaw1805.www.scenario.views;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.NaturalResourceDTO;
import com.eaw1805.data.dto.common.ProductionSiteDTO;
import com.eaw1805.data.dto.common.TerrainDTO;
import com.eaw1805.www.fieldbattle.widgets.OptionEAW;
import com.eaw1805.www.fieldbattle.widgets.SelectEAW;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.WidgetStore;
import com.eaw1805.www.scenario.widgets.OptionWidgetEAW;

public class BrushView extends VerticalPanel {
    boolean brushEnabled = true;
    boolean onlyLand = false;
    boolean reset = false;
    SelectEAW<NationDTO> nationSelect;
    SelectEAW<NationDTO> polSphereSelect;
    SelectEAW<TerrainDTO> terrainSelect;
    SelectEAW<NaturalResourceDTO> resourceSelect;
    SelectEAW<ProductionSiteDTO> prSiteSelect;
    SelectEAW<Integer> popSizeSelect;
    SelectEAW<Character> climaticSelect;
    public BrushView() {
        getElement().getStyle().setBackgroundColor("grey");
        add(new Label("*Brush*"));
        final CheckBox enabled = new CheckBox("Enabled");
        enabled.setValue(brushEnabled);
        enabled.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {

                brushEnabled = event.getValue();

            }
        });
        add(enabled);
        final CheckBox onlyLandChkbx = new CheckBox("Apply Only On Land");
        onlyLandChkbx.setValue(onlyLand);
        onlyLandChkbx.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                onlyLand = booleanValueChangeEvent.getValue();
            }
        });
        add(onlyLandChkbx);

        final CheckBox resetChkbx = new CheckBox("Reset Sector");
        resetChkbx.setValue(reset);
        resetChkbx.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                reset= booleanValueChangeEvent.getValue();
            }
        });
        add(resetChkbx);

        nationSelect = new SelectEAW<NationDTO>("Select Nation") {
            @Override
            public void onChange() {
                //do nothing here
            }
        };
        nationSelect.addOption(new OptionEAW(130, 15, "None Selected"), null);
        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            nationSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getNationImg(nation.getNationId(), 20), WidgetStore.getLabel(nation.getName())), nation);
        }
        polSphereSelect = new SelectEAW<NationDTO>("Select Political Sphere") {
            @Override
            public void onChange() {
                //do nothing here
            }
        };
        polSphereSelect.addOption(new OptionEAW(130, 15, "None Selected"), null);
        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            polSphereSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getNationImg(nation.getNationId(), 20), WidgetStore.getLabel(nation.getName())), nation);
        }

        terrainSelect = new SelectEAW<TerrainDTO>("Select Terrain") {
            @Override
            public void onChange() {
                //do nothing here
            }
        };
        terrainSelect.addOption(new OptionEAW(130, 15, "None Selected"), null);
        for (TerrainDTO terrain : EditorStore.getInstance().getTerrains()) {
            terrainSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getTerrainImg(terrain.getId(), 20), WidgetStore.getLabel(terrain.getName())), terrain);
        }

        resourceSelect = new SelectEAW<NaturalResourceDTO>("Select Natural Resource") {
            @Override
            public void onChange() {
                //do nothing here
            }
        };
        resourceSelect.addOption(new OptionEAW(130, 15, "None Selected"), null);
        for (NaturalResourceDTO resource : EditorStore.getInstance().getResources()) {
            resourceSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getResourceImg(resource.getId(), 20), WidgetStore.getLabel(resource.getName())), resource);
        }

        prSiteSelect = new SelectEAW<ProductionSiteDTO>("Select Production Site") {
            @Override
            public void onChange() {
                //do nothing here
            }
        };

        prSiteSelect.addOption(new OptionEAW(130, 15, "None Selected"), null);
        for (ProductionSiteDTO prSite : EditorStore.getInstance().getPrSites()) {
            prSiteSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getProductionSiteImg(prSite.getId(), 20), WidgetStore.getLabel(prSite.getName())), prSite);
        }

        popSizeSelect = new SelectEAW<Integer>("Select population size") {
            @Override
            public void onChange() {
                //do nothing here
            }
        };

        popSizeSelect.addOption(new OptionEAW(130, 15, "None Selected"), -1);
        for (int size = 0; size < 10; size++) {
            popSizeSelect.addOption(new OptionEAW(130, 15, String.valueOf(size)), size);
        }

        climaticSelect = new SelectEAW<Character>("Select Climatic Zone") {
            @Override
            public void onChange() {
                //do nothing here
            }
        };

        climaticSelect.addOption(new OptionEAW(130, 15, "None Selected"), '?');
        climaticSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getClimateImg('a', 12), WidgetStore.getLabel("Arctic")), 'a');
        climaticSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getClimateImg('e', 12), WidgetStore.getLabel("Central European")), 'e');
        climaticSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getClimateImg('m', 12), WidgetStore.getLabel("Mediterranean")), 'm');


        Grid grid = new Grid(7, 2);
        grid.setWidget(0, 0, new Label("Nation :"));
        grid.setWidget(0, 1, nationSelect);
        grid.setWidget(1, 0, new Label("Political Sphere :"));
        grid.setWidget(1, 1, polSphereSelect);
        grid.setWidget(2, 0, new Label("Terrain : "));
        grid.setWidget(2, 1, terrainSelect);
        grid.setWidget(3, 0, new Label("Natural Resource : "));
        grid.setWidget(3, 1, resourceSelect);
        grid.setWidget(4, 0, new Label("Production Site : "));
        grid.setWidget(4, 1, prSiteSelect);
        grid.setWidget(5, 0, new Label("Population Size : "));
        grid.setWidget(5, 1, popSizeSelect);
        grid.setWidget(6, 0, new Label("Climatic Zone : "));
        grid.setWidget(6, 1, climaticSelect);
        nationSelect.selectOption(0);
        polSphereSelect.selectOption(0);
        terrainSelect.selectOption(0);
        resourceSelect.selectOption(0);
        prSiteSelect.selectOption(0);
        popSizeSelect.selectOption(0);
        climaticSelect.selectOption(0);
        add(grid);
    }

    public NationDTO getNation() {
        return nationSelect.getValue();
    }


    public TerrainDTO getTerrain() {
        return terrainSelect.getValue();
    }

    public NaturalResourceDTO getResource() {
        return resourceSelect.getValue();
    }

    public ProductionSiteDTO getPrSite() {
        return prSiteSelect.getValue();
    }


    public NationDTO getPolSphere() {
        return polSphereSelect.getValue();
    }

    public int getPopulationSize() {
        return popSizeSelect.getValue();
    }

    public char getClimaticZone() {
        return climaticSelect.getValue();
    }

    public boolean isBrushEnabled() {
        return brushEnabled;
    }

    public boolean isOnlyLand() {
        return onlyLand;
    }
    public boolean isReset() {
        return reset;
    }
}
