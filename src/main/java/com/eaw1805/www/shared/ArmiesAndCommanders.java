package com.eaw1805.www.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class ArmiesAndCommanders
        implements IsSerializable, Serializable {

    /**
     * Required by Serializable interface.
     */
    static final long serialVersionUID = 49L; //NOPMD

    private Collection<ArmyDTO> armies = null;
    private List<ArmyDTO> deletedArmies = null;
    private List<CorpDTO> deletedCorps = null;
    private List<CommanderDTO> commanders = null;
    private List<BattalionDTO> mergedBatts = null;
    private List<CommanderDTO> capturedCommanders = null;

    public ArmiesAndCommanders(final Collection<ArmyDTO> armies,
                               final List<CommanderDTO> commanders,
                               final List<BattalionDTO> mergedBatts,
                               final List<ArmyDTO> deletedArmies,
                               final List<CorpDTO> deletedCorps,
                               final List<CommanderDTO> capturedCommanders) {
        this.armies = armies;
        this.deletedCorps = deletedCorps;
        this.deletedArmies = deletedArmies;
        this.commanders = commanders;
        this.mergedBatts = mergedBatts;
        this.capturedCommanders = capturedCommanders;
    }

    public ArmiesAndCommanders() {
        // do nothing
    }

    /**
     * @return the armies
     */
    public Collection<ArmyDTO> getArmies() {
        return armies;
    }

    /**
     * @return the commanders
     */
    public List<CommanderDTO> getCommanders() {
        return commanders;
    }

    /**
     * @return the mergedBatts
     */
    public List<BattalionDTO> getMergedBatts() {
        return mergedBatts;
    }

    public List<ArmyDTO> getDeletedArmies() {
        return deletedArmies;
    }

    public List<CorpDTO> getDeletedCorps() {
        return deletedCorps;
    }

    public List<CommanderDTO> getCapturedCommanders() {
        return capturedCommanders;
    }
}
