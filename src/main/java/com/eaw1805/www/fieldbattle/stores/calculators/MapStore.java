package com.eaw1805.www.fieldbattle.stores.calculators;

import com.eaw1805.data.dto.web.field.FieldBattleMapDTO;
import com.eaw1805.data.dto.web.field.FieldBattleSectorDTO;

public class MapStore {

    public static MapStore instance;

    public static MapStore getInstance() {
        if (instance == null) {
            instance = new MapStore();
        }
        return instance;
    }
    FieldBattleMapDTO fieldMap;


    private MapStore(){};

    public void init(final FieldBattleMapDTO map) {
        fieldMap = map;
    }

    public FieldBattleSectorDTO getSectorByXY(int x, int y) {
        return fieldMap.getSectors()[x][y];
    }

    public FieldBattleMapDTO getMap() {
        return fieldMap;
    }


}
