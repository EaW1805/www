package com.eaw1805.www.scenario.views;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.www.scenario.stores.EditorMapUtils;


public class ViewMenu extends VerticalPanel {

    public ViewMenu() {
        getElement().getStyle().setBackgroundColor("grey");
        add(new Label("*Toggle Views*"));
        final CheckBox toggleNations = new CheckBox("Nations");
        toggleNations.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showNationGroup(event.getValue());
            }
        });
        toggleNations.setValue(true, true);

        final CheckBox togglePlSphere = new CheckBox("Political Sphere");
        togglePlSphere.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showPolSphereGroup(event.getValue());
            }
        });
        togglePlSphere.setValue(true, true);

        final CheckBox toggleTerrain = new CheckBox("Terrain");
        toggleTerrain.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showTerrainGroup(event.getValue());
            }
        });
        toggleTerrain.setValue(true, true);


        final CheckBox toggleResource = new CheckBox("Natural Resources");
        toggleResource.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showResourceGroup(event.getValue());
            }
        });
        toggleResource.setValue(true, true);

        final CheckBox toggleProdSite = new CheckBox("Production Sites");
        toggleProdSite.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showPrSiteGroup(event.getValue());
            }
        });
        toggleProdSite.setValue(true, true);

        final CheckBox togglePopulation = new CheckBox("Population");
        togglePopulation.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showPopulationGroup(event.getValue());
            }
        });
        togglePopulation.setValue(true, true);

        final CheckBox toggleClimate = new CheckBox("Climate");
        toggleClimate.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showClimate(event.getValue());
            }
        });
        toggleClimate.setValue(true, true);

        final CheckBox toggleArmies = new CheckBox("Armies");
        toggleArmies.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showArmies(event.getValue());
            }
        });
        toggleArmies.setValue(true, true);

        final CheckBox toggleCommanders = new CheckBox("Commanders");
        toggleCommanders.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showCommanders(event.getValue());
            }
        });
        toggleCommanders.setValue(true, true);

        final CheckBox toggleSpies = new CheckBox("Spies");
        toggleSpies.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showSpies(event.getValue());
            }
        });
        toggleSpies.setValue(true, true);

        final CheckBox toggleBTrains = new CheckBox("Baggage Trains");
        toggleBTrains.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showBTrains(event.getValue());
            }
        });
        toggleBTrains.setValue(true, true);

        final CheckBox toggleShips = new CheckBox("Ships");
        toggleShips.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showShips(event.getValue());
            }
        });
        toggleShips.setValue(true, true);

        final CheckBox toggleGrid = new CheckBox("Grid");
        toggleGrid.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showGrid(event.getValue());
            }
        });
        toggleGrid.setValue(true, true);

        final CheckBox toggleJumps = new CheckBox("Jump Off Points");
        toggleJumps.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showJumpGroup(event.getValue());
            }
        });
        toggleJumps.setValue(true, true);

        final CheckBox toggleSupplies = new CheckBox("Show supplies");
        toggleSupplies.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                EditorMapUtils.getInstance().showSupplyGroup(event.getValue());
            }
        });
        toggleSupplies.setValue(true, true);

        add(toggleNations);
        add(togglePlSphere);
        add(toggleTerrain);
        add(toggleResource);
        add(toggleProdSite);
        add(togglePopulation);
        add(toggleClimate);
        add(toggleArmies);
        add(toggleCommanders);
        add(toggleSpies);
        add(toggleBTrains);
        add(toggleShips);
        add(toggleGrid);
        add(toggleJumps);
        add(toggleSupplies);
    }
}
