package com.eaw1805.www.fieldbattle.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
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

public interface EmpireFieldBattleRpcServiceAsync {
    void getMap(int scenarioId, int fieldbattleId, AsyncCallback<FieldBattleMapDTO> callback);

    void getArmies(final int scenarioId, final int battleId, final int nationId, AsyncCallback<Map<Boolean, Map<Integer, Map<Integer, BrigadeDTO>>>> callback);

    void getArmyData(final int scenarioId, final int battleId, final int nationId, AsyncCallback<ArmyData> callback);

    void saveBrigadesPositions(final int scenarioId, final int battleId, final int nationId, final List<BrigadeDTO> brigades, final boolean ready, AsyncCallback<Integer> callback);

    void getCommandersByNation(final int scenarioId, final int battleId, final int nationId, AsyncCallback<Map<Integer, List<CommanderDTO>>> callback);

    void getNations(final int scenarioId, final int battleId, final int nationId, AsyncCallback<FieldData> callback);

    void getFieldBattleReports(final int scenarioId, final int battleId, AsyncCallback<List<FieldBattleHalfRoundStatisticsDTO>> callback);

    void getAvailableGames(final int scenarioId, AsyncCallback<List<SocialGame>> callback);

    void getScenarioGames(final int scenarioId, AsyncCallback<List<SocialGame>> callback);

    void createGame(final int scenarioId, final int battleId, final SocialGame socialGame, AsyncCallback<Integer> callback);

    void pickupPosition(final int scenarioId, final int battleId, final int nationId, AsyncCallback<Integer> callback);

    void getMyGames(final int scenarioId, AsyncCallback<List<SocialGame>> callback);

    void getPendingGames(final int scenarioId, AsyncCallback<List<SocialGame>> callback);

    void loadGame(final int scenarioId, final int battleId, AsyncCallback<SocialSettings> callback);

    void getGameStatus(final int scenarioId, final int battleId, AsyncCallback<String> callback);
}
