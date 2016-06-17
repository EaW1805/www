package com.eaw1805.www.scenario.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
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

public interface EmpireScenarioRpcServiceAsync {
    void getRegionData(final int gameId, AsyncCallback<Map<RegionDTO, List<SectorDTO>>> callback);
    void getEditorStaticData(AsyncCallback<StaticEditorData> callback);
    void getWarehouses(final int gameId, final AsyncCallback<Map<Integer, Map<Integer, WarehouseDTO>>> callback);
    void getArmies(final int gameId, final AsyncCallback<ArmyData> callback);

    void getCommanderNamesByNation(final int gameId, final AsyncCallback<Map<Integer, List<String>>> callback);
    void saveData(final int gameId, final List<RegionDTO> regions, final Map<Integer, List<SectorDTO>> regionSectors,
                  final Map<Integer, Map<Integer, WarehouseDTO>> regionNationWarehouses,
                  final Map<Integer, Map<Integer, Map<Integer, Map<Integer, BrigadeDTO>>>> brigades,
                  final Map<Integer, Map<Integer, Map<Integer, Map<Integer, CorpDTO>>>> corps,
                  final Map<Integer, Map<Integer, Map<Integer, Map<Integer, ArmyDTO>>>> armies,
                  final Map<Integer, Map<Integer, Map<Integer, Map<Integer, CommanderDTO>>>> commanders,
                  final Map<Integer, Map<Integer, Map<Integer, Map<Integer, SpyDTO>>>> spies,
                  final Map<Integer, Map<Integer, Map<Integer, Map<Integer, BaggageTrainDTO>>>> bTrains,
                  final Map<Integer, Map<Integer, Map<Integer, Map<Integer, ShipDTO>>>> ships,
                  final Map<Integer, Map<Integer, Map<Integer, Map<Integer, FleetDTO>>>> fleets,
                  final List<JumpOffDTO> jumpOffs,
                  final AsyncCallback<Integer> callback);
}
