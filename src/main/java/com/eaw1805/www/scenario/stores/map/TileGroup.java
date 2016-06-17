package com.eaw1805.www.scenario.stores.map;


import com.eaw1805.data.constants.TerrainConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.common.TerrainDTO;
import com.eaw1805.www.scenario.stores.EditorMapUtils;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.views.EditorPanel;
import com.eaw1805.www.scenario.widgets.DrawingAreaSE;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Text;

public class TileGroup implements TerrainConstants {

    private SectorDTO sector;

    final Group terrainGroup = new Group();
    final Group prSiteGroup = new Group();
    final Group nationGroup = new Group();
    final Group resourceGroup = new Group();
    final Group polSphereGroup = new Group();
    final Group populationGroup = new Group();
    final Group climateGroup = new Group();
    public TileGroup(final SectorDTO sector, final Group parentTerrainGroup, final Group parentPrSiteGroup, final Group parentNationGroup,
                     final Group parentResourceGroup, final Group parentPolSphereGroup, final Group parentPopulationGroup,
                     final Group parentClimateGroup) {
        this.sector = sector;
        parentTerrainGroup.add(terrainGroup);
        parentPrSiteGroup.add(prSiteGroup);
        parentNationGroup.add(nationGroup);
        parentPolSphereGroup.add(polSphereGroup);
        parentResourceGroup.add(resourceGroup);
        parentPopulationGroup.add(populationGroup);
        parentClimateGroup.add(climateGroup);
    }


    public TileGroup draw() {
        EditorMapUtils utils = EditorMapUtils.getInstance();
        terrainGroup.clear();
        prSiteGroup.clear();
        resourceGroup.clear();
        nationGroup.clear();
        polSphereGroup.clear();
        populationGroup.clear();
        climateGroup.clear();
        if (sector.getTerrain() != null) {
            if (sector.getTerrain().getId() == TERRAIN_G ||
                    sector.getTerrain().getId() == TERRAIN_H) {
                terrainGroup.add(new Image(utils.getPointX(sector.getX()), utils.getPointY(sector.getY()), EditorMapUtils.TILE_WIDTH, EditorMapUtils.TILE_HEIGHT, "http://static.eaw1805.com/tiles/base/arable.png"));
            }
            terrainGroup.add(new Image(utils.getPointX(sector.getX()), utils.getPointY(sector.getY()), EditorMapUtils.TILE_WIDTH, EditorMapUtils.TILE_HEIGHT, getTerrainImageHref(sector.getTerrain())));
        }
        if (sector.getProductionSiteDTO() != null) {
            prSiteGroup.add(new Image(utils.getPointX(sector.getX()), utils.getPointY(sector.getY()), EditorMapUtils.TILE_WIDTH, EditorMapUtils.TILE_HEIGHT, "http://static.eaw1805.com/tiles/sites/tprod-" + sector.getProductionSiteDTO().getId() + ".png"));
        }
        if (sector.getTradeCity()) {
            prSiteGroup.add(new Image(utils.getPointX(sector.getX()), utils.getPointY(sector.getY()), EditorMapUtils.TILE_WIDTH, EditorMapUtils.TILE_HEIGHT, "http://static.eaw1805.com/tiles/sites/tcity04barracks.png"));
        }
        if (sector.getNationDTO() != null
                && sector.getNationDTO().getNationId() != -1) {
            nationGroup.add(new Image(utils.getPointX(sector.getX()), utils.getPointY(sector.getY()), 30, 15, "http://static.eaw1805.com/images/nations/nation-" + sector.getNationDTO().getNationId() + "-36.png"));
        }
        if (sector.getNatResDTO() != null) {
            resourceGroup.add(new Image(utils.getPointX(sector.getX()) + 24, utils.getPointY(sector.getY()) + 40, 16, 16, "http://static.eaw1805.com/tiles/resources/resource-" + sector.getNatResDTO().getId() + ".png"));
        }
        final NationDTO sphere = EditorStore.getInstance().getNationByCode(sector.getPoliticalSphere());
        if (sphere != null && sphere.getNationId() != -1) {
            polSphereGroup.add(new Image(utils.getPointX(sector.getX()) + 34, utils.getPointY(sector.getY()), 30, 15, "http://static.eaw1805.com/images/nations/nation-" + sphere.getNationId() + "-36.png"));
        }

        final Image popImg = new Image(utils.getPointX(sector.getX()) + 40, utils.getPointY(sector.getY()) + 50, 10, 10, "http://static.eaw1805.com/tiles/popsizes/pop_" + sector.getPopulation() + ".png");
        populationGroup.add(popImg);

        final Image climImg = new Image(utils.getPointX(sector.getX()) + 52, utils.getPointY(sector.getY()) + 50, 10, 10, "http://static.eaw1805.com/tiles/climates/climate_" + String.valueOf(sector.getClimaticZone()) + ".png");
        climateGroup.add(climImg);
        return this;
    }

    public SectorDTO getSector() {
        return sector;
    }

    public static String getTerrainImageHref(final TerrainDTO terrain) {
        String baseUrl = "http://static.eaw1805.com/tiles/";
        switch (terrain.getId()) {
            case TERRAIN_B:
                return baseUrl + "base/arable.png";
            case TERRAIN_D:
                return baseUrl + "base/desert1.png";
            case TERRAIN_G:
                return baseUrl + "elevation/tm01.png";
            case TERRAIN_H:
                return baseUrl + "elevation/th01.png";
            case TERRAIN_K:
                return baseUrl + "base/steppe1.png";
            case TERRAIN_Q:
                return baseUrl + "base/arable.png";
            case TERRAIN_W:
                return baseUrl + "base/forest1.png";
            case TERRAIN_S:
                return baseUrl + "elevation/tww01.png";
            case TERRAIN_T:
                return baseUrl + "base/forestw.png";
            case TERRAIN_J:
                return baseUrl + "base/jungle1.png";
            case TERRAIN_R:
                return baseUrl + "base/ocean.png";
            case TERRAIN_O:
                return baseUrl + "base/ocean.png";
            case TERRAIN_I:
                return baseUrl + "base/impassable2.png";
            default:
                return "";

        }

    }


}
