package com.eaw1805.www.fieldbattle.asyncs;


import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmyData implements com.google.gwt.user.client.rpc.IsSerializable,
        Serializable {
    static final long serialVersionUID = 42L;
    Map<Integer, ArmyDTO> armies = new HashMap<Integer, ArmyDTO>();
    Map<Integer, CorpDTO> corps = new HashMap<Integer, CorpDTO>();
    Map<Integer, List<CorpDTO>> armyToCorps = new HashMap<Integer, List<CorpDTO>>();
    public ArmyData() {
        //do nothing here... darn
    }

    public ArmyData setData(final Map<Integer, ArmyDTO> armies, final Map<Integer, CorpDTO> corps) {
        this.armies = armies;
        this.corps = corps;
        for (final CorpDTO corp : corps.values()) {
            if (!armyToCorps.containsKey(corp.getArmyId())) {
                armyToCorps.put(corp.getArmyId(), new ArrayList<CorpDTO>());
            }
            armyToCorps.get(corp.getArmyId()).add(corp);
        }
        return this;
    }



    public Map<Integer, ArmyDTO> getArmies() {
        return armies;
    }

    public Map<Integer, CorpDTO> getCorps() {
        return corps;
    }

    public Map<Integer, List<CorpDTO>> getArmyToCorps() {
        return armyToCorps;
    }
}
