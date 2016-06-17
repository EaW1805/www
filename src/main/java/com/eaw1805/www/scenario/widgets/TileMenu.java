package com.eaw1805.www.scenario.widgets;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.eaw1805.data.dto.common.*;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.www.fieldbattle.widgets.OptionEAW;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.fieldbattle.widgets.shared.TextBoxEditable;
import com.eaw1805.www.scenario.stores.EditorMapUtils;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.RegionSettings;
import com.eaw1805.www.scenario.stores.WidgetStore;
import com.eaw1805.www.scenario.stores.map.TileGroup;
import com.eaw1805.www.scenario.views.EditorPanel;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileMenu {

    SelectEAW<NationDTO> nationSelect;
    SelectEAW<NationDTO> polSphereSelect;
    SelectEAW<TerrainDTO> terrainSelect;
    SelectEAW<NaturalResourceDTO> resourceSelect;
    SelectEAW<ProductionSiteDTO> prSiteSelect;
    SelectEAW<Integer> popSizeSelect;
    SelectEAW<Character> climaticSelect;
    CheckBox tradeCity;
    TextBoxEditable nameBox;
    CheckBox jumpOffPoint;
    SelectEAW<Integer> regionSelect;
    TextBoxEditable xBox;
    TextBoxEditable yBox;
    HorizontalPanel jumpContainer;
    final Group rectsGroup = new Group();

    List<TileGroup> sectorGroups = new ArrayList<TileGroup>();
    boolean allowUpdate = false;
    public TileMenu() {

        nationSelect = new SelectEAW<NationDTO>("Select Nation") {
            @Override
            public void onChange() {
                if (allowUpdate) {
                    for (TileGroup sectorGroup : sectorGroups) {
                        sectorGroup.getSector().setNationDTO(getValue());
                        sectorGroup.draw();
                    }
                }
                EditorPanel.getInstance().getMapOverView().updateOverView();
            }
        };
        nationSelect.getElement().getStyle().setBackgroundColor("grey");
        nationSelect.addOption(new OptionEAW(130, 15, "None Selected"), null);
        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            nationSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getNationImg(nation.getNationId(), 20), WidgetStore.getLabel(nation.getName())), nation);
        }

        polSphereSelect = new SelectEAW<NationDTO>("Select Political Sphere") {
            @Override
            public void onChange() {
                if (allowUpdate) {
                    for (TileGroup sectorGroup : sectorGroups) {
                        if (getValue() == null) {
                            sectorGroup.getSector().setPoliticalSphere('?');
                        } else {
                            sectorGroup.getSector().setPoliticalSphere(getValue().getCode());
                        }
                        sectorGroup.draw();
                    }
                    EditorPanel.getInstance().getMapOverView().updateOverView();
                }
            }
        };
        polSphereSelect.addOption(new OptionEAW(130, 15, "None Selected"), null);
        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            polSphereSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getNationImg(nation.getNationId(), 20), WidgetStore.getLabel(nation.getName())), nation);
        }

        terrainSelect = new SelectEAW<TerrainDTO>("Select Terrain") {
            @Override
            public void onChange() {
                if (allowUpdate) {
                    for (TileGroup sectorGroup : sectorGroups) {
                        sectorGroup.getSector().setTerrain(getValue());
                        sectorGroup.draw();
                    }
                    EditorPanel.getInstance().getMapOverView().updateOverView();
                }
            }
        };
        terrainSelect.addOption(new OptionEAW(130, 15, "None Selected"), null);
        for (TerrainDTO terrain : EditorStore.getInstance().getTerrains()) {
            terrainSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getTerrainImg(terrain.getId(), 20), WidgetStore.getLabel(terrain.getName())), terrain);
        }

        resourceSelect = new SelectEAW<NaturalResourceDTO>("Select Natural Resource") {
            @Override
            public void onChange() {
                if (allowUpdate) {
                    for (TileGroup sectorGroup : sectorGroups) {
                        sectorGroup.getSector().setNatResDTO(getValue());
                        sectorGroup.draw();
                    }
                    EditorPanel.getInstance().getMapOverView().updateOverView();
                }
            }
        };
        resourceSelect.addOption(new OptionEAW(130, 15, "None Selected"), null);
        for (NaturalResourceDTO resource : EditorStore.getInstance().getResources()) {
            resourceSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getResourceImg(resource.getId(), 20), WidgetStore.getLabel(resource.getName())), resource);
        }

        prSiteSelect = new SelectEAW<ProductionSiteDTO>("Select Production Site") {
            @Override
            public void onChange() {
                if (allowUpdate) {
                    for (TileGroup sectorGroup : sectorGroups) {
                        sectorGroup.getSector().setProductionSiteDTO(getValue());
                        sectorGroup.draw();
                    }
                    EditorPanel.getInstance().getMapOverView().updateOverView();
                }
            }
        };

        prSiteSelect.addOption(new OptionEAW(130, 15, "None Selected"), null);
        for (ProductionSiteDTO prSite : EditorStore.getInstance().getPrSites()) {
            prSiteSelect.addOption(new OptionWidgetEAW(130, 15, WidgetStore.getProductionSiteImg(prSite.getId(), 20), WidgetStore.getLabel(prSite.getName())), prSite);
        }

        popSizeSelect = new SelectEAW<Integer>("Select population size") {
            @Override
            public void onChange() {
                if (allowUpdate) {
                    for (TileGroup sectorGroup : sectorGroups) {
                        sectorGroup.getSector().setPopulation(getValue());
                        sectorGroup.draw();
                    }
                    EditorPanel.getInstance().getMapOverView().updateOverView();
                }
            }
        };

        popSizeSelect.addOption(new OptionEAW(130, 15, "None Selected"), -1);
        for (int size = 0; size < 10; size++) {
            popSizeSelect.addOption(new OptionEAW(130, 15, String.valueOf(size)), size);
        }

        climaticSelect = new SelectEAW<Character>("Select Climatic Zone") {
            @Override
            public void onChange() {
                if (allowUpdate) {
                    for (TileGroup sectorGroup : sectorGroups) {
                        sectorGroup.getSector().setClimaticZone(getValue());
                        sectorGroup.draw();
                    }
                    EditorPanel.getInstance().getMapOverView().updateOverView();
                }
            }
        };

        climaticSelect.addOption(new OptionEAW(130, 15, "None Selected"), '?');
        climaticSelect.addOption(new OptionEAW(130, 15, "Arctic"), 'a');
        climaticSelect.addOption(new OptionEAW(130, 15, "Central European"), 'e');
        climaticSelect.addOption(new OptionEAW(130, 15, "Mediterranean"), 'm');

        tradeCity = new CheckBox("Trade City");
        tradeCity.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (allowUpdate) {
                    for (TileGroup sectorGroup : sectorGroups) {
                        sectorGroup.getSector().setTradeCity(event.getValue());
                        sectorGroup.draw();
                    }
                    EditorPanel.getInstance().getMapOverView().updateOverView();
                }
            }
        });

        nameBox = new TextBoxEditable("Sectors name");
        nameBox.initHandler(new BasicHandler() {
            @Override
            public void run() {
                if (allowUpdate) {
                    for (TileGroup sectorGroup : sectorGroups) {
                        sectorGroup.getSector().setName(nameBox.getText());
                        sectorGroup.draw();
                    }
                }
            }
        });
        jumpOffPoint = new CheckBox("Jump Off Point");
        jumpOffPoint.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (allowUpdate) {
                    for (TileGroup sectorGroup : sectorGroups) {
                        if (event.getValue()) {
                            if (!EditorStore.getInstance().hasSectorJumpOff(sectorGroup.getSector())) {
                                final JumpOffDTO jumpOff = new JumpOffDTO();
                                jumpOff.setDepartureRegion(sectorGroup.getSector().getRegionId());
                                jumpOff.setDepartureX(sectorGroup.getSector().getX());
                                jumpOff.setDepartureY(sectorGroup.getSector().getY());
                                //make destination same as departure, let the user change the values.
                                jumpOff.setDestinationRegion(sectorGroup.getSector().getRegionId());
                                jumpOff.setDestinationX(sectorGroup.getSector().getX());
                                jumpOff.setDestinationY(sectorGroup.getSector().getY());
                                EditorStore.getInstance().getJumpOffPoints().add(jumpOff);
                                if (sectorGroups.size() == 1) {
                                    xBox.setText(String.valueOf(jumpOff.getDestinationX()));
                                    yBox.setText(String.valueOf(jumpOff.getDestinationY()));
                                    regionSelect.selectOptionByValue(jumpOff.getDestinationRegion());
                                }
                            }

                        } else {
                            final Iterator<JumpOffDTO> iter = EditorStore.getInstance().getJumpOffPoints().iterator();
                            while (iter.hasNext()) {
                                final JumpOffDTO jump = iter.next();
                                if (jump.getDepartureRegion() == sectorGroup.getSector().getRegionId()
                                        && jump.getDepartureX() == sectorGroup.getSector().getX()
                                        && jump.getDepartureY() == sectorGroup.getSector().getY()) {
                                    iter.remove();
                                }
                            }
                        }
                        jumpContainer.setVisible(event.getValue());
                    }
                    EditorMapUtils.getInstance().drawJumps(RegionSettings.region.getRegionId());
                }
            }
        });
        jumpContainer = new HorizontalPanel();
        regionSelect = new SelectEAW<Integer>("Select Region") {
            @Override
            public void onChange() {
                if (allowUpdate) {
                    for (TileGroup sectorGroup : sectorGroups) {
                        JumpOffDTO jump = EditorStore.getInstance().getJumpOffBySector(sectorGroup.getSector());
                        if (jump != null) {
                            jump.setDestinationRegion(getValue());

                        }
                    }
                    EditorMapUtils.getInstance().drawJumps(RegionSettings.region.getRegionId());
                }
            }
        };

        xBox = new TextBoxEditable("X");
        xBox.setWidth("35px");
        yBox = new TextBoxEditable("Y");
        yBox.setWidth("35px");
        xBox.initHandler(new BasicHandler() {
            @Override
            public void run() {
                if (allowUpdate) {
                    for (TileGroup sectorGroup : sectorGroups) {
                        JumpOffDTO jump = EditorStore.getInstance().getJumpOffBySector(sectorGroup.getSector());
                        if (jump != null) {
                            try {
                                jump.setDestinationX(Integer.parseInt(xBox.getText()));

                            } catch (Exception e) {
                                //eat it like a sir
                            }
                        }
                    }
                    EditorMapUtils.getInstance().drawJumps(RegionSettings.region.getRegionId());
                }
            }
        });
        yBox.initHandler(new BasicHandler() {
            @Override
            public void run() {
                if (allowUpdate) {
                    for (TileGroup sectorGroup : sectorGroups) {
                        JumpOffDTO jump = EditorStore.getInstance().getJumpOffBySector(sectorGroup.getSector());
                        if (jump != null) {
                            try {
                                jump.setDestinationY(Integer.parseInt(yBox.getText()));

                            } catch (Exception e) {
                                //eat it like a sir
                            }
                        }
                    }
                    EditorMapUtils.getInstance().drawJumps(RegionSettings.region.getRegionId());
                }
            }
        });
        jumpContainer.add(regionSelect);
        jumpContainer.add(xBox);
        jumpContainer.add(yBox);

    }

    public void selectTile(final int x, final int y, final boolean toggle) {
        TileGroup sectorGroup = EditorMapUtils.getInstance().getSectorTileGroup(x, y);
        if (sectorGroup != null) {
            if (!rectsGroup.isAttached()) {
                //be sure to re-add it so it will be on top
                EditorPanel.getInstance().getDrawingArea().add(rectsGroup);
            }
            if (!sectorGroups.contains(sectorGroup)) {//only if it isn't already there
                final Rectangle rect = new Rectangle(EditorMapUtils.getInstance().getPointX(x), EditorMapUtils.getInstance().getPointY(y), EditorMapUtils.TILE_WIDTH, EditorMapUtils.TILE_HEIGHT);
                rect.setStrokeColor("green");
                rect.setStrokeWidth(2);
                rect.setFillOpacity(0.3);
                rectsGroup.add(rect);
                sectorGroups.add(sectorGroup);
            } else if (toggle) {
                final int index = sectorGroups.indexOf(sectorGroup);
                sectorGroups.remove(sectorGroup);
                //hopefully the index is the same.
                rectsGroup.remove(rectsGroup.getVectorObject(index));
            }
        }
    }



    public void showTileMenu() {
        if (sectorGroups.isEmpty()) {
            hideTileMenu();
            return;
        }
        if (rectsGroup.isAttached()) {
            //be sure to re-add it so it will be on top
            EditorPanel.getInstance().getDrawingArea().remove(rectsGroup);
        }

        EditorPanel.getInstance().getDrawingArea().add(rectsGroup);


        int xAbs = rectsGroup.getVectorObject(rectsGroup.getVectorObjectCount() - 1).getAbsoluteLeft();
        int yAbs = rectsGroup.getVectorObject(rectsGroup.getVectorObjectCount() - 1).getAbsoluteTop();

        if (nationSelect.isAttached()) {
            EditorPanel.getInstance().remove(nationSelect);
            EditorPanel.getInstance().remove(polSphereSelect);
            EditorPanel.getInstance().remove(terrainSelect);
            EditorPanel.getInstance().remove(resourceSelect);
            EditorPanel.getInstance().remove(prSiteSelect);
            EditorPanel.getInstance().remove(popSizeSelect);
            EditorPanel.getInstance().remove(climaticSelect);
            EditorPanel.getInstance().remove(tradeCity);
            EditorPanel.getInstance().remove(nameBox);
            EditorPanel.getInstance().remove(jumpOffPoint);
            EditorPanel.getInstance().remove(jumpContainer);
        }
        EditorPanel.getInstance().add(nationSelect, xAbs, yAbs - EditorMapUtils.TILE_HEIGHT*2);
        EditorPanel.getInstance().add(polSphereSelect, xAbs - EditorMapUtils.TILE_WIDTH * 2, yAbs - EditorMapUtils.TILE_HEIGHT);
        EditorPanel.getInstance().add(terrainSelect, xAbs + EditorMapUtils.TILE_WIDTH, yAbs - EditorMapUtils.TILE_HEIGHT);
        EditorPanel.getInstance().add(resourceSelect, xAbs - EditorMapUtils.TILE_WIDTH*3, yAbs);
        EditorPanel.getInstance().add(prSiteSelect, xAbs + EditorMapUtils.TILE_WIDTH*2, yAbs);
        EditorPanel.getInstance().add(popSizeSelect, xAbs - EditorMapUtils.TILE_WIDTH * 2, yAbs + EditorMapUtils.TILE_HEIGHT);
        EditorPanel.getInstance().add(climaticSelect, xAbs + EditorMapUtils.TILE_WIDTH, yAbs + EditorMapUtils.TILE_HEIGHT);
        EditorPanel.getInstance().add(tradeCity, xAbs, yAbs + EditorMapUtils.TILE_HEIGHT*2);
        EditorPanel.getInstance().add(nameBox, xAbs, yAbs + 30 + EditorMapUtils.TILE_HEIGHT*2);
        EditorPanel.getInstance().add(jumpOffPoint, xAbs, yAbs + 60 + EditorMapUtils.TILE_HEIGHT*2);
        EditorPanel.getInstance().add(jumpContainer, xAbs, yAbs + 90 + EditorMapUtils.TILE_HEIGHT*2);



        allowUpdate = false;
        regionSelect.clearOptions();
        regionSelect.addOption(new OptionEAW(130, 15, "Select Region"), 0);
        for (RegionDTO region :EditorStore.getInstance().getRegions()) {
            regionSelect.addOption(new OptionEAW(130, 15, region.getName()), region.getRegionId());
        }

        if (sectorGroups.size() == 1) {
            final SectorDTO sector = sectorGroups.get(0).getSector();
            nationSelect.selectOptionByValue(sector.getNationDTO());
            polSphereSelect.selectOptionByValue(EditorStore.getInstance().getNationByCode(sector.getPoliticalSphere()));
            terrainSelect.selectOptionByValue(sector.getTerrain());
            resourceSelect.selectOptionByValue(sector.getNatResDTO());
            prSiteSelect.selectOptionByValue(sector.getProductionSiteDTO());
            popSizeSelect.selectOptionByValue(sector.getPopulation());
            climaticSelect.selectOptionByValue(sector.getClimaticZone());
            tradeCity.setValue(sector.getTradeCity());
            nameBox.setText(sector.getName());
            JumpOffDTO jump = EditorStore.getInstance().getJumpOffBySector(sector);
            jumpOffPoint.setValue(jump != null);
            if (jump == null) {
                regionSelect.selectOptionByValue(0);
                xBox.setText("");
                yBox.setText("");
            } else {
                regionSelect.selectOptionByValue(jump.getDestinationRegion());
                xBox.setText(String.valueOf(jump.getDestinationX()));
                yBox.setText(String.valueOf(jump.getDestinationY()));
            }


        } else {
            //todo: find common options between sectors and pre-select them
            nationSelect.selectOptionByValue(null);
            polSphereSelect.selectOptionByValue(null);
            terrainSelect.selectOptionByValue(null);
            resourceSelect.selectOptionByValue(null);
            prSiteSelect.selectOptionByValue(null);
            popSizeSelect.selectOptionByValue(0);
            climaticSelect.selectOptionByValue('?');
            tradeCity.setValue(false);
            nameBox.setText("");
            jumpOffPoint.setValue(false);
            regionSelect.selectOptionByValue(0);
            xBox.setText("");
            yBox.setText("");
        }
        jumpContainer.setVisible(jumpOffPoint.getValue());
        allowUpdate = true;
    }

    public void hideTileMenu() {
        if (rectsGroup.isAttached()) {
            EditorPanel.getInstance().getDrawingArea().remove(rectsGroup);
        }

        if (nationSelect.isAttached()) {
            nationSelect.removeFromParent();
            polSphereSelect.removeFromParent();
            terrainSelect.removeFromParent();
            resourceSelect.removeFromParent();
            prSiteSelect.removeFromParent();
            popSizeSelect.removeFromParent();
            climaticSelect.removeFromParent();
            tradeCity.removeFromParent();
            nameBox.removeFromParent();
            jumpOffPoint.removeFromParent();
            jumpContainer.removeFromParent();
        }
        rectsGroup.clear();
        sectorGroups.clear();
    }

    public boolean isAttached() {
        return nationSelect.isAttached() || rectsGroup.isAttached();
    }
}
