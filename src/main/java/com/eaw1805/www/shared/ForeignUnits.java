package com.eaw1805.www.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to store all foreign units.
 */
public class ForeignUnits
        implements IsSerializable, Serializable {

    /**
     * Required by Serializable interface.
     */
    static final long serialVersionUID = 5124L; //NOPMD

    /**
     * Integer is the sector id where the unit resides
     */
    private Map<Integer, List<ArmyDTO>> armies = new HashMap<Integer, List<ArmyDTO>>();  //NOPMD

    private Map<Integer, List<SpyDTO>> spies = new HashMap<Integer, List<SpyDTO>>();    //NOPMD

    private Map<Integer, List<BaggageTrainDTO>> baggageTrains = new HashMap<Integer, List<BaggageTrainDTO>>(); //NOPMD

    private Map<Integer, List<CommanderDTO>> commanders = new HashMap<Integer, List<CommanderDTO>>(); //NOPMD

    private Map<Integer, List<FleetDTO>> fleets = new HashMap<Integer, List<FleetDTO>>(); //NOPMD

    /**
     * @return the armies
     */
    public Map<Integer, List<ArmyDTO>> getArmies() {
        return armies;
    }

    /**
     * @return the spies
     */
    public Map<Integer, List<SpyDTO>> getSpies() {
        return spies;
    }

    /**
     * @return the baggageTrains
     */
    public Map<Integer, List<BaggageTrainDTO>> getBaggageTrains() {
        return baggageTrains;
    }

    /**
     * @return the fleets
     */
    public Map<Integer, List<FleetDTO>> getFleets() {
        return fleets;
    }

    /**
     * @return the commanders
     */
    public Map<Integer, List<CommanderDTO>> getCommanders() {
        return commanders;
    }

}
