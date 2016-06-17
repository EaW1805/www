package com.eaw1805.www.fieldbattle.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.eaw1805.data.dto.collections.FieldData;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.field.FieldBattleHalfRoundStatisticsDTO;
import com.eaw1805.data.dto.web.field.FieldBattleMapDTO;
import com.eaw1805.www.fieldbattle.asyncs.ArmyData;
import com.eaw1805.www.fieldbattle.stores.dto.SocialGame;
import com.eaw1805.www.fieldbattle.stores.dto.SocialSettings;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("fieldBattle.rpc")
public interface EmpireFieldBattleRpcService extends RemoteService {


    FieldBattleMapDTO getMap(int scenarioId, int fieldbattleId);

    Map<Boolean, Map<Integer, Map<Integer, BrigadeDTO>>> getArmies(final int scenarioId, final int battleId, final int nationId);

    ArmyData getArmyData(final int scenarioId, final int battleId, final int nationId);

    int saveBrigadesPositions(final int scenarioId, final int battleId, final int nationId, final List<BrigadeDTO> brigades, final boolean ready);

    Map<Integer, List<CommanderDTO>> getCommandersByNation(final int scenarioId, final int battleId, final int nationId);

    FieldData getNations(final int scenarioId, final int battleId, final int nationId);

    List<FieldBattleHalfRoundStatisticsDTO> getFieldBattleReports(final int scenarioId, final int battleId);

    List<SocialGame> getAvailableGames(final int scenarioId);

    List<SocialGame> getScenarioGames(final int scenarioId);

    int createGame(final int scenarioId, final int battleId, final SocialGame socialGame);

    int pickupPosition(final int scenarioId, final int battleId, final int nationId);

    List<SocialGame> getMyGames(final int scenarioId);

    List<SocialGame> getPendingGames(final int scenarioId);

    SocialSettings loadGame(final int scenarioId, final int battleId);

    String getGameStatus(final int scenarioId, final int battleId);
}
