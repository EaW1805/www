package com.eaw1805.www.scenario.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.eaw1805.data.dto.common.JumpOffDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.data.dto.web.army.*;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.economy.WarehouseDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.scenario.stores.ArmyData;
import com.eaw1805.www.scenario.stores.StaticEditorData;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("scenario.rpc")
public interface EmpireScenarioRpcService extends RemoteService {
    Map<RegionDTO, List<SectorDTO>> getRegionData(final int gameId);
    StaticEditorData getEditorStaticData();
    Map<Integer, Map<Integer, WarehouseDTO>> getWarehouses(final int gameId);
    ArmyData getArmies(final int gameId);

    Map<Integer, List<String>> getCommanderNamesByNation(final int gameId);
    int saveData(final int gameId, final List<RegionDTO> regions, final Map<Integer, List<SectorDTO>> regionSectors,
                 final Map<Integer, Map<Integer, WarehouseDTO>> regionNationWarehouses,
                 final Map<Integer, Map<Integer, Map<Integer, Map<Integer, BrigadeDTO>>>> brigades,
                 final Map<Integer, Map<Integer, Map<Integer, Map<Integer, CorpDTO>>>> corps,
                 final Map<Integer, Map<Integer, Map<Integer, Map<Integer, ArmyDTO>>>> armies,
                 final Map<Integer, Map<Integer, Map<Integer, Map<Integer, CommanderDTO>>>> commanders,
                 final Map<Integer, Map<Integer, Map<Integer, Map<Integer, SpyDTO>>>> spies,
                 final Map<Integer, Map<Integer, Map<Integer, Map<Integer, BaggageTrainDTO>>>> bTrains,
                 final Map<Integer, Map<Integer, Map<Integer, Map<Integer, ShipDTO>>>> ships,
                 final Map<Integer, Map<Integer, Map<Integer, Map<Integer, FleetDTO>>>> fleets,
                 final List<JumpOffDTO> jumpOffs);
}
