package empire.webapp.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import empire.data.dto.web.army.ArmyDTO;
import empire.data.dto.web.army.CommanderDTO;
import empire.data.dto.web.army.SpyDTO;
import empire.data.dto.web.economy.BaggageTrainDTO;
import empire.data.dto.web.fleet.FleetDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class AlliedUnits
        implements IsSerializable, Serializable {
    /**
     * Required by Serializable interface.
     */
    static final long serialVersionUID = 54L; //NOPMD

    /**
     * Integer is the sector id where the unit resides
     */
    private Map<Integer, Map<Integer, ArmyDTO>> armies;
    private Map<Integer, List<SpyDTO>> spies;
    private Map<Integer, List<BaggageTrainDTO>> baggageTrains;
    private Map<Integer, List<CommanderDTO>> commanders;
    private Map<Integer, Map<Integer, FleetDTO>> fleets;
    private Map<Integer, List<AlliedMovement>> movements;

    public AlliedUnits() {
        // do nothing here
    }

    /**
     * @return the armies
     */
    public Map<Integer, Map<Integer, ArmyDTO>> getArmies() {
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
    public Map<Integer, Map<Integer, FleetDTO>> getFleets() {
        return fleets;
    }

    /**
     * @param armies the armies to set
     */
    public void setArmies(final Map<Integer, Map<Integer, ArmyDTO>> armies) {
        this.armies = armies;
    }

    /**
     * @param spies the spies to set
     */
    public void setSpies(final Map<Integer, List<SpyDTO>> spies) {
        this.spies = spies;
    }

    /**
     * @param baggageTrains the baggageTrains to set
     */
    public void setBaggageTrains(final Map<Integer, List<BaggageTrainDTO>> baggageTrains) {
        this.baggageTrains = baggageTrains;
    }

    /**
     * @param map the fleets to set
     */
    public void setFleets(final Map<Integer, Map<Integer, FleetDTO>> map) {
        this.fleets = map;
    }

    /**
     * @param commanders the commanders to set
     */
    public void setCommanders(final Map<Integer, List<CommanderDTO>> commanders) {
        this.commanders = commanders;
    }

    /**
     * @return the commanders
     */
    public Map<Integer, List<CommanderDTO>> getCommanders() {
        return commanders;
    }

    /**
     * @param movements the movements to set
     */
    public void setMovements(final Map<Integer, List<AlliedMovement>> movements) {
        this.movements = movements;
    }

    /**
     * @return the movements
     */
    public Map<Integer, List<AlliedMovement>> getMovements() {
        return movements;
    }


}
