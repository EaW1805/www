package com.eaw1805.www.fieldbattle.stores;

import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommanderStore implements ClearAble {

    private Map<Integer, List<CommanderDTO>> commanders;


    private Map<Integer, CommanderDTO> idToCommander = new HashMap<Integer, CommanderDTO>();
    private CommanderDTO overallCommander;


    public static CommanderStore instance;
    boolean initialized = false;

    private CommanderStore() {
        //make it private
    }

    public void initOverallCommander() {
        List<BrigadeDTO> brigades = ArmyStore.getInstance().getBrigadesByNation(BaseStore.getInstance().getNationId());
        int overallCommanderId = 0;
        int otherCommander = 0;
        for (BrigadeDTO brigade : brigades) {
            if (brigade.getFieldBattleOverallCommanderId() != 0) {
                overallCommanderId = brigade.getFieldBattleOverallCommanderId();
                break;
            }
            if (brigade.getFieldBattleCommanderId() != 0) {
                otherCommander = brigade.getFieldBattleCommanderId();
            }
        }
        if (overallCommanderId > 0) {
            overallCommander = getCommanderById(overallCommanderId);
        } else if (otherCommander > 0) {
            overallCommander = getCommanderById(otherCommander);
        }
    }

    public int getNumberOfOrdersAllowChange() {
        int out = 4;
        if (overallCommander != null) {
            out += overallCommander.getRank().getRankId();
        }
        return out;
    }

    public static CommanderStore getInstance() {
        if (instance == null) {
            instance = new CommanderStore();
        }
        return instance;
    }

    public void initCommanders(final Map<Integer, List<CommanderDTO>> initComm) {
        commanders = initComm;

        for (final List<CommanderDTO> comms : initComm.values()) {
            for (final CommanderDTO comm : comms) {
                idToCommander.put(comm.getId(), comm);
            }
        }

        initialized = true;
    }

    public CommanderDTO getBaseCommander() {
        CommanderDTO comm = null;
        List<BrigadeDTO> brigades = ArmyStore.getInstance().getBrigadesByNation(BaseStore.getInstance().getNationId());
        for (BrigadeDTO brigade : brigades) {
            if (brigade.getFieldBattleOverallCommanderId() > 0) {
                comm = getCommanderById(brigade.getFieldBattleOverallCommanderId());
                break;
            } else if (brigade.getFieldBattleCommanderId() > 0) {
                comm = getCommanderById(brigade.getFieldBattleCommanderId());
            }
        }
        return comm;
    }

    public CommanderDTO getBaseEnemyCommander() {
        CommanderDTO comm = null;
        for (BrigadeDTO brigade : PlaybackStore.getInstance().getRoundStatistics(0).getAllBrigades()) {
            if (!BaseStore.getInstance().isNationAllied(brigade.getNationId())) {
                if (brigade.getFieldBattleOverallCommanderId() > 0) {
                    comm = getCommanderById(brigade.getFieldBattleOverallCommanderId());
                    break;
                } else if (brigade.getFieldBattleCommanderId() > 0) {
                    comm = getCommanderById(brigade.getFieldBattleCommanderId());
                }
            }
        }
        return comm;
    }


    public Map<Integer, List<CommanderDTO>> getCommanders() {
        return commanders;
    }

    public CommanderDTO getCommanderById(final int commId) {
        return idToCommander.get(commId);
    }

    @Override
    public void clear() {
        initialized = false;
        commanders.clear();
        idToCommander.clear();
        overallCommander = null;
    }
}
