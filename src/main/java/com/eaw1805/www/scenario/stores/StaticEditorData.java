package com.eaw1805.www.scenario.stores;


import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.NaturalResourceDTO;
import com.eaw1805.data.dto.common.ProductionSiteDTO;
import com.eaw1805.data.dto.common.TerrainDTO;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.RankDTO;
import com.eaw1805.data.dto.web.economy.GoodDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;

import java.util.ArrayList;
import java.util.List;

public class StaticEditorData implements com.google.gwt.user.client.rpc.IsSerializable {
    List<GoodDTO> goods = new ArrayList<GoodDTO>();
    List<ProductionSiteDTO> productionSites = new ArrayList<ProductionSiteDTO>();
    List<ArmyTypeDTO> armyTypes = new ArrayList<ArmyTypeDTO>();
    List<NationDTO> nations = new ArrayList<NationDTO>();
    List<NaturalResourceDTO> resources = new ArrayList<NaturalResourceDTO>();
    List<ShipTypeDTO> shipTypes = new ArrayList<ShipTypeDTO>();
    List<TerrainDTO> terrains = new ArrayList<TerrainDTO>();
    List<RankDTO> ranks = new ArrayList<RankDTO>();

    public void setData(List<GoodDTO> goods,
                            List<ProductionSiteDTO> productionSites,
                            List<ArmyTypeDTO> armyTypes,
                            List<NationDTO> nations,
                            List<NaturalResourceDTO> resources,
                            List<ShipTypeDTO> shipTypes,
                            List<TerrainDTO> terrains,
                            List<RankDTO> ranks) {
        this.goods = goods;
        this.productionSites = productionSites;
        this.armyTypes = armyTypes;
        this.nations = nations;
        this.resources = resources;
        this.shipTypes = shipTypes;
        this.terrains = terrains;
        this.ranks = ranks;
    }

    public StaticEditorData() {
    }

    public List<GoodDTO> getGoods() {
        return goods;
    }

    public List<ProductionSiteDTO> getProductionSites() {
        return productionSites;
    }

    public List<ArmyTypeDTO> getArmyTypes() {
        return armyTypes;
    }

    public List<NationDTO> getNations() {
        return nations;
    }

    public List<NaturalResourceDTO> getResources() {
        return resources;
    }

    public List<ShipTypeDTO> getShipTypes() {
        return shipTypes;
    }

    public List<TerrainDTO> getTerrains() {
        return terrains;
    }

    public List<RankDTO> getRanks() {
        return ranks;
    }
}
