package com.eaw1805.www.scenario.stores;

import com.google.gwt.user.client.Window;
import com.eaw1805.data.constants.TerrainConstants;
import com.eaw1805.data.dto.common.*;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.data.dto.web.army.*;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.economy.GoodDTO;
import com.eaw1805.data.dto.web.economy.StoredGoodDTO;
import com.eaw1805.data.dto.web.economy.WarehouseDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.scenario.stores.map.TileGroup;
import com.eaw1805.www.scenario.views.BrushView;
import com.eaw1805.www.scenario.views.EditorPanel;

import java.util.*;

public class RegionUtils implements TerrainConstants {
    private  static int regionId = -1;

    public static void createNewRegion(final String name) {
        RegionDTO region = new RegionDTO();
        region.setName(name);
        region.setRegionId(getNewRegionId());
        EditorStore.getInstance().getRegions().add(region);
        EditorStore.getInstance().getRegionSectors().put(region.getRegionId(), new ArrayList<SectorDTO>());
        SectorDTO initialSector = new SectorDTO();
        initialSector.setRegionId(region.getRegionId());
        initialSector.setTerrain(EditorStore.getInstance().getIdToTerrain().get(TERRAIN_O));
        initialSector.setX(0);
        initialSector.setY(0);
        initialSector.setPoliticalSphere('?');
        initialSector.setClimaticZone('e');

        initWarehouses(region);

        EditorStore.getInstance().getRegionSectors().get(region.getRegionId()).add(initialSector);
        EditorStore.getInstance().getBrigades().put(region.getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, BrigadeDTO>>>());
        EditorStore.getInstance().getCorps().put(region.getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, CorpDTO>>>());
        EditorStore.getInstance().getArmies().put(region.getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, ArmyDTO>>>());
        EditorStore.getInstance().getCommanders().put(region.getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, CommanderDTO>>>());
        EditorStore.getInstance().getSpies().put(region.getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, SpyDTO>>>());
        EditorStore.getInstance().getBaggageTrains().put(region.getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, BaggageTrainDTO>>>());
        EditorStore.getInstance().getShips().put(region.getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, ShipDTO>>>());
        EditorStore.getInstance().getFleets().put(region.getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, FleetDTO>>>());
        EditorPanel.getInstance().getMapsMenu().resetRegions();
        selectRegion(region);

    }

    private static void initWarehouses(final RegionDTO region) {
        EditorStore.getInstance().getRegionToNationToWarehouse().put(region.getRegionId(), new HashMap<Integer, WarehouseDTO>());
        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            //create the new warehouse
            WarehouseDTO wh = new WarehouseDTO();
            wh.setRegionId(region.getRegionId());
            wh.setNationId(nation.getNationId());
            wh.setGoodsDTO(new HashMap<Integer, StoredGoodDTO>());
            for (GoodDTO good : EditorStore.getInstance().getGoods()) {
                StoredGoodDTO in = new StoredGoodDTO();
                in.setGoodDTO(good);
                in.setTpe(good.getGoodId());
                in.setQte(0);
                wh.getGoodsDTO().put(good.getGoodId(), in);
            }
            //update editor store
            EditorStore.getInstance().getRegionToNationToWarehouse().get(region.getRegionId()).put(nation.getNationId(),  wh);
        }
    }

    public static void deleteRegion(final RegionDTO region) {
        //remove region data
        EditorStore.getInstance().getRegions().remove(region);
        EditorStore.getInstance().getRegionSectors().remove(region.getRegionId());
        //remove units in region
        EditorStore.getInstance().getBrigades().remove(region.getRegionId());
        EditorStore.getInstance().getCorps().remove(region.getRegionId());
        EditorStore.getInstance().getArmies().remove(region.getRegionId());
        EditorStore.getInstance().getCommanders().remove(region.getRegionId());
        EditorStore.getInstance().getSpies().remove(region.getRegionId());
        EditorStore.getInstance().getBaggageTrains().remove(region.getRegionId());
        EditorStore.getInstance().getShips().remove(region.getRegionId());
        EditorStore.getInstance().getFleets().remove(region.getRegionId());
        //remove all jump off points issosiated with this region
        final Iterator<JumpOffDTO> iter = EditorStore.getInstance().getJumpOffPoints().iterator();
        while (iter.hasNext()) {
            final JumpOffDTO jumpOff = iter.next();
            if (jumpOff.getDestinationRegion() == region.getRegionId()
                    || jumpOff.getDepartureRegion() == region.getRegionId()) {
                iter.remove();
            }
        }
        //update maps panel
        EditorPanel.getInstance().getMapsMenu().resetRegions();
        if (region.getRegionId() == RegionSettings.region.getRegionId()) {
            EditorMapUtils.getInstance().clearArea();
        }
    }

    public static void selectRegion(final RegionDTO region) {
        RegionSettings.generateSettings(region, EditorStore.getInstance().getRegionSectors().get(region.getRegionId()));
        EditorPanel.getInstance().getRegionPanel().resetPanel();
        EditorMapUtils.getInstance().drawRegion(region, EditorStore.getInstance().getRegionSectors().get(region.getRegionId()));
        EditorPanel.getInstance().getMapOverView().updateOverView();
        EditorPanel.getInstance().getArmyOverView().updateOverview();
        EditorPanel.getInstance().getCommOverview().updateOverview();
        EditorPanel.getInstance().getSpyBTrainOverView().updateOverview();
        EditorPanel.getInstance().getShipOverView().updateOverview();
        EditorPanel.getInstance().getWarehousePanel().initPanel(region);
        EditorPanel.getInstance().getRegionPanel().setVisible(true);

    }

    public static void updateRegion(final String name, final int sizeX, final int sizeY) {
        RegionSettings.region.setName(name);
        RegionSettings.name = name;
        long start = new Date().getTime();
        String measures = "";
        if (sizeX != RegionSettings.sizeX) {
            if (sizeX > RegionSettings.sizeX) {
                for (int indexX = RegionSettings.sizeX; indexX < sizeX; indexX++) {
                    for (int indexY = 0; indexY < RegionSettings.sizeY; indexY++) {
                        final SectorDTO sector = new SectorDTO();
                        sector.setRegionId(RegionSettings.region.getRegionId());
                        sector.setTerrain(EditorStore.getInstance().getIdToTerrain().get(TERRAIN_O));
                        sector.setX(indexX);
                        sector.setY(indexY);
                        sector.setPoliticalSphere('?');
                        sector.setClimaticZone('e');
                        EditorStore.getInstance().getRegionSectors().get(RegionSettings.region.getRegionId()).add(sector);
                    }
                }
            } else if (sizeX < RegionSettings.sizeX) {
                final Iterator<SectorDTO> iter = EditorStore.getInstance().getRegionSectors().get(RegionSettings.region.getRegionId()).iterator();
                while (iter.hasNext()) {
                    if (iter.next().getX() >= sizeX) {
                        iter.remove();
                    }
                }
            }
            RegionSettings.sizeX = sizeX;
        }
        measures += " - " + (new Date().getTime() - start);
        start = new Date().getTime();
        if (sizeY != RegionSettings.sizeY) {
            if (sizeY > RegionSettings.sizeY) {
                for (int indexX = 0; indexX < RegionSettings.sizeX; indexX++) {
                    for (int indexY = RegionSettings.sizeY; indexY < sizeY; indexY++) {
                        final SectorDTO sector = new SectorDTO();
                        sector.setRegionId(RegionSettings.region.getRegionId());
                        sector.setTerrain(EditorStore.getInstance().getIdToTerrain().get(TERRAIN_O));
                        sector.setX(indexX);
                        sector.setY(indexY);
                        sector.setPoliticalSphere('?');
                        sector.setClimaticZone('e');
                        EditorStore.getInstance().getRegionSectors().get(RegionSettings.region.getRegionId()).add(sector);
                    }
                }
            } else if (sizeY < RegionSettings.sizeY) {
                final Iterator<SectorDTO> iter = EditorStore.getInstance().getRegionSectors().get(RegionSettings.region.getRegionId()).iterator();
                while (iter.hasNext()) {
                    if (iter.next().getY() >= sizeY) {
                        iter.remove();
                    }
                }
            }
            RegionSettings.sizeY = sizeY;
        }
        measures += " - " + (new Date().getTime() - start);
        start = new Date().getTime();
        EditorPanel.getInstance().getRegionPanel().resetPanel();
        measures += " - " + (new Date().getTime() - start);
        start = new Date().getTime();
        EditorPanel.getInstance().getMapsMenu().resetRegions();
        measures += " - " + (new Date().getTime() - start);
        start = new Date().getTime();
        EditorMapUtils.getInstance().drawRegion(RegionSettings.region, EditorStore.getInstance().getRegionSectors().get(RegionSettings.region.getRegionId()));
        measures += " - " + (new Date().getTime() - start);
        start = new Date().getTime();
        EditorPanel.getInstance().setDebugMessage(measures);
    }

    public static void updateSector(final int x, final int y) {
        final TileGroup sectorGroup = EditorMapUtils.getInstance().getSectorTileGroup(x, y);
        final BrushView brushView = EditorPanel.getInstance().getBrushView();
        if (brushView.getNation() != null) {
            sectorGroup.getSector().setNationDTO(brushView.getNation());
        }
        if (brushView.getPrSite() != null) {
            sectorGroup.getSector().setProductionSiteDTO(brushView.getPrSite());
        }
        if (brushView.getResource() != null) {
            sectorGroup.getSector().setNatResDTO(brushView.getResource());
        }
        if (brushView.getTerrain() != null) {
            sectorGroup.getSector().setTerrain(brushView.getTerrain());
        }
        if (brushView.getPolSphere() != null) {
            sectorGroup.getSector().setPoliticalSphere(brushView.getPolSphere().getCode());
        }
        if (brushView.getPopulationSize() != -1) {
            sectorGroup.getSector().setPopulation(brushView.getPopulationSize());
        }
        if (brushView.getClimaticZone() != '?') {
            sectorGroup.getSector().setClimaticZone(brushView.getClimaticZone());
        }
        sectorGroup.draw();
    }

    public static void resetSector(final int x, final int y) {
        final TileGroup sectorGroup = EditorMapUtils.getInstance().getSectorTileGroup(x, y);
        final SectorDTO sector = sectorGroup.getSector();
        sector.setNationDTO(null);
        sector.setTerrain(EditorStore.getInstance().getIdToTerrain().get(TERRAIN_O));
        sector.setPoliticalSphere('?');
        sector.setNatResDTO(null);
        sector.setProductionSiteDTO(null);
        sector.setClimaticZone('e');
        sector.setPopulation(0);

        sectorGroup.draw();
    }


    private static int getNewRegionId() {
        regionId--;
        return regionId;
    }
}
