package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.HashMap;
import java.util.Map;

public class CreateCorpOrder implements Order {

    private Map<Integer, CorpDTO> corpsMap;
    private String name;
    private int xPos, yPos, regionId, nationId, armyId;

    public CreateCorpOrder(final Map<Integer, CorpDTO> corpsMap,
                           final String name,
                           final int xPos, final int yPos, final int regionId,
                           final int nationId,
                           final int armyId) {
        this.corpsMap = corpsMap;
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.regionId = regionId;
        this.nationId = nationId;
        this.setArmyId(armyId);
    }

    public int execute(final int unitId) {
        final CorpDTO newCorp = new CorpDTO();
        newCorp.setCorpId(unitId);
        newCorp.setArmyId(getArmyId());
        newCorp.setCommander(new CommanderDTO());
        newCorp.setBrigades(new HashMap<Integer, BrigadeDTO>());
        newCorp.setName(getName());
        newCorp.setOriginalName(getName());
        newCorp.setRegionId(getRegionId());
        newCorp.setX(getX());
        newCorp.setY(getY());
        newCorp.setXStart(getX());
        newCorp.setYStart(getY());
        newCorp.setNationId(getNationId());
        corpsMap.put(unitId, newCorp);
        return 1;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setX(final int x) {
        this.xPos = x;
    }

    public void setY(final int y) {
        this.yPos = y;
    }

    public void setRegionId(final int regionId) {
        this.regionId = regionId;
    }

    public void setNationId(final int nationId) {
        this.nationId = nationId;
    }

    public int getNationId() {
        return nationId;
    }

    public void setArmyId(final int armyId) {
        this.armyId = armyId;
    }

    public int getArmyId() {
        return armyId;
    }

}

