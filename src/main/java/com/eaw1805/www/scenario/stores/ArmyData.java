package com.eaw1805.www.scenario.stores;


import com.eaw1805.data.dto.common.JumpOffDTO;
import com.eaw1805.data.dto.web.army.*;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;

import java.util.ArrayList;
import java.util.List;

public class ArmyData implements com.google.gwt.user.client.rpc.IsSerializable {
    List<BrigadeDTO> brigades = new ArrayList<BrigadeDTO>();
    List<CorpDTO> corps = new ArrayList<CorpDTO>();
    List<ArmyDTO> armies = new ArrayList<ArmyDTO>();
    List<SpyDTO> spies = new ArrayList<SpyDTO>();
    List<BaggageTrainDTO> baggageTrains = new ArrayList<BaggageTrainDTO>();
    List<CommanderDTO> commanders = new ArrayList<CommanderDTO>();
    List<ShipDTO> ships = new ArrayList<ShipDTO>();
    List<FleetDTO> fleets = new ArrayList<FleetDTO>();
    List<JumpOffDTO> jumpOffPoints = new ArrayList<JumpOffDTO>();

    public List<BrigadeDTO> getBrigades() {
        return brigades;
    }

    public List<CorpDTO> getCorps() {
        return corps;
    }

    public List<ArmyDTO> getArmies() {
        return armies;
    }

    public List<SpyDTO> getSpies() {
        return spies;
    }

    public List<BaggageTrainDTO> getBaggageTrains() {
        return baggageTrains;
    }

    public List<CommanderDTO> getCommanders() {
        return commanders;
    }

    public List<ShipDTO> getShips() {
        return ships;
    }

    public List<FleetDTO> getFleets() {
        return fleets;
    }

    public List<JumpOffDTO> getJumpOffPoints() {
        return jumpOffPoints;
    }
}
