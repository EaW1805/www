package com.eaw1805.www.client.remote;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.collections.DataCollection;
import com.eaw1805.data.dto.common.BattleDTO;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.GameSettingsDTO;
import com.eaw1805.data.dto.web.OrderPositionDTO;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.data.dto.web.RelationDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.ForeignArmyDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.data.dto.web.economy.WarehouseDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.data.dto.web.movement.MovementDTO;
import com.eaw1805.data.dto.web.movement.PathDTO;
import com.eaw1805.www.shared.AlliedUnits;
import com.eaw1805.www.shared.ArmiesAndCommanders;
import com.eaw1805.www.shared.ForeignUnits;
import com.eaw1805.www.shared.TilesCollection;
import com.eaw1805.www.shared.stores.support.Taxation;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The async counterpart of <code>EmpireService</code>.
 */
public interface EmpireRpcServiceAsync {

    void getRegion(final int scenarioId, AsyncCallback<List<RegionDTO>> callback);

    void getNations(final int scenarioId, AsyncCallback<List<NationDTO>> callback);

    void getNaturalResAndProdSites(final int scenarioId, AsyncCallback<DataCollection> callback);

    void getArmyTypes(final int scenarioId, int nationId, AsyncCallback<List<ArmyTypeDTO>> callback);

    void getShipTypes(final int scenarioId, int nationId, AsyncCallback<List<ShipTypeDTO>> callback);

    void getRegionByGameAndNation(final int scenarioId, int id, int gameId, int turn, int nationId,
                                  AsyncCallback<TilesCollection> callback);

    void getBarracksByNation(final int scenarioId, int nationId, int gameId, int turn, AsyncCallback<List<BarrackDTO>> callback);

    void getFleetsByNation(final int scenarioId, int nationId, int gameId,
                           int turn, AsyncCallback<Map<Boolean, List<FleetDTO>>> callback);

    void getNationsRelations(final int scenarioId, int gameId, int nationId, int turn,
                             AsyncCallback<List<RelationDTO>> callback);

    void getWarehouses(final int scenarioId, int gameId, int nationId, AsyncCallback<List<WarehouseDTO>> asyncCallback);

    /**
     * Calculate the supply lines for the given scenario, game, player.
     *
     * @param scenarioId the scenario of the game.
     * @param gameId     the identity of the game.
     * @param nationId   the identity of the nation.
     * @param callback   a set of positions that are within supply lines reach.
     */
    void getSupplyLines(final int scenarioId, final int gameId, final int regionId, final int nationId, AsyncCallback<Set<PositionDTO>> callback);

    void getPossibleMovementTiles(final int scenarioId, final int gameId, final int nationId,
                                  int startX, int startY, int mpAv, int regionId,
                                  int unitType, final int conqueredSectors,
                                  final int conqueredNeutralSectors, final int warShips, final List<Integer> otherNations, AsyncCallback<java.util.Set<PathDTO>> callback);

    void getArmiesByNation(final int scenarioId, int nationId, int gameId, int turn,
                           AsyncCallback<ArmiesAndCommanders> callback);

    void saveUnitChanges(final int scenarioId, final List<ArmyDTO> chArmiesList,
                         final Map<Integer, List<BrigadeDTO>> newBrigadesmap,
                         final Map<Integer, List<ClientOrderDTO>> relOrders,
                         final int nationId,
                         final int gameId,
                         final int turn, AsyncCallback<Integer> callback);

    void saveCommanderChanges(final int scenarioId, List<CommanderDTO> commandersList, Map<Integer, List<ClientOrderDTO>> map, int nationId,
                              int gameId, int turn, AsyncCallback<Integer> callback);

    void saveEconomyChanges(final int scenarioId, final Map<SectorDTO, Integer> sectorProdSites,
                            final Map<Integer, List<ClientOrderDTO>> orderMap,
                            final int nationId,
                            final int gameId,
                            final int turn, AsyncCallback<Integer> callback);

    void saveBarrackChanges(final int scenarioId, final Map<Integer, BarrackDTO> chBarracksMap,
                            final Map<Integer, List<ClientOrderDTO>> orderMap,
                            final int nationId,
                            final int gameId,
                            final int turn, AsyncCallback<Integer> async);

    void saveRelationsChanges(final int scenarioId, List<RelationDTO> relationsList,
                              int nationId,
                              int gameId, int turn, AsyncCallback<Integer> callback);

    void saveMovementChanges(final int scenarioId, Map<Integer, Map<Integer, MovementDTO>> typeMvMap,
                             final Map<Integer, List<ClientOrderDTO>> orderMap,
                             int nationId, int gameId, int turn, AsyncCallback<Integer> callback);

    void saveGameSettingsChanges(final int scenarioId, GameSettingsDTO settings, int nationId,
                                 int gameId, int turn, AsyncCallback<Integer> callback);

    void getGameSettings(final int scenarioId, int nationId, int gameId, int turn,
                         AsyncCallback<GameSettingsDTO> callback);

    void getMovementOrdersByGameNationAndTurn(final int scenarioId, int nationId, int gameId, int turn,
                                              AsyncCallback<Map<Integer, Map<Integer, MovementDTO>>> callback);

    void saveNavyChanges(final int scenarioId, Map<Integer, FleetDTO> idFleetMap,
                         Map<Integer, List<ShipDTO>> newShipMap, Map<Integer, List<ClientOrderDTO>> orderMap, int nationId, int gameId,
                         int turn, AsyncCallback<Integer> callback);

    void saveRegionChanges(final int scenarioId, Map<Integer, OrderPositionDTO> sectorOrderMap, Map<Integer, List<ClientOrderDTO>> orderMap, int nationId,
                           int gameId, int turn, AsyncCallback<Integer> callback);

    void getRegionOrdersByGameNationAndTurn(final int scenarioId, int nationId, int gameId, int turn,
                                            AsyncCallback<Map<Integer, OrderPositionDTO>> callback);

    void getNewBrigadeOrdersByGameNationAndTurn(final int scenarioId, int nationId, int gameId,
                                                int turn, AsyncCallback<Map<Integer, List<BrigadeDTO>>> callback);

    void getNewBaggageTrainOrdersByGameNationAndTurn(final int scenarioId, int nationId, int gameId,
                                                     int turn, AsyncCallback<Map<Integer, List<BaggageTrainDTO>>> callback);

    void saveTaxationChanges(final int scenarioId, Taxation taxation, int nationId, int gameId, int turn,
                             AsyncCallback<Integer> callback);

    void saveTradeChanges(final int scenarioId, List<ClientOrderDTO> tradeOrders, int nationId, int gameId,
                          int turn, AsyncCallback<Integer> callback);

    void saveNewsletter(final int scenarioId, final int nationId, final int gameId,
                        final int targetNation, final String message, final int type,
                        AsyncCallback<Integer> callback);

    void saveTransportChanges(final int scenarioId, List<ClientOrderDTO> cargoRelatedOrders,
                              int nationId, int gameId, int turn,
                              AsyncCallback<Integer> callback);

    void getTaxationByNationAndGame(final int scenarioId, int nationId, int gameId, int turn,
                                    AsyncCallback<Taxation> callback);

    void getBaggageTrainsBySector(final int scenarioId, final int sectorId, final int nationId, final int gameId,
                                  AsyncCallback<List<BaggageTrainDTO>> callback);

    /**
     * Save changes to all the baggage trains. And that means orders about repairs
     * and new baggage trains
     *
     * @param baggageTList   the baggage train list
     * @param ordersMap      the orders list
     * @param newBaggageTMap the new baggage train list
     * @param nationId       the identity of the nation.
     * @param gameId         the identity of the game.
     * @param turn           the turn of the game.
     * @param callback       the callback function.
     */
    void saveBaggageTrainChanges(final int scenarioId, List<BaggageTrainDTO> baggageTList, Map<Integer, List<ClientOrderDTO>> ordersMap,
                                 Map<Integer, List<BaggageTrainDTO>> newBaggageTMap, int nationId,
                                 int gameId, int turn, AsyncCallback<Integer> callback);


    void getNewShipOrdersByGameNationAndTurn(final int scenarioId, int nationId, int gameId,
                                             int turn, AsyncCallback<Map<Integer, List<ShipDTO>>> callback);

    /**
     * Retrieve spies for a specific game, nation (player) and turn.
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @param callback a list of SpyDTO object.
     */
    void getSpies(final int scenarioId, final int nationId, final int gameId, final int turn, final boolean applyOrders, AsyncCallback<List<SpyDTO>> callback);

    /**
     * Retrieve baggage trains for a specific game, nation (player) and turn.
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @param callback a list of BaggageTrainDTO object.
     */
    void getBaggageTrains(final int scenarioId, final int nationId, final int gameId, final int turn,
                          final boolean isAllied, final int originalNationId, final boolean visible,
                          AsyncCallback<List<BaggageTrainDTO>> callback);

    /**
     * Retrieve orders and corresponding costs for a specific game, nation (player) and turn.
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @param callback a list of BaggageTrainDTO object.
     */
    void getClientOrders(final int scenarioId, int nationId, int gameId, int turn,
                         AsyncCallback<Map<Integer, List<ClientOrderDTO>>> callback);

    /**
     * Retrieve trade cities for a specific game, nation (player) and turn.
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @param callback a list of TradeCityDTO object.
     */
    void getTradeCities(final int scenarioId, int nationId, int gameId, int turn,
                        AsyncCallback<List<TradeCityDTO>> callback);

    /**
     * Retrieve allied units for a given nation,game and turn
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @param callback a map of type nationId - AlliedUnits object
     */
    void getAlliedUnits(final int scenarioId, int nationId, int gameId, int turn,
                        AsyncCallback<Map<Integer, Map<Integer, AlliedUnits>>> callback);

    /**
     * Retrieve foreign units for a given game and nation.
     *
     * @param gameId   the identity of the game to check.
     * @param nationId the identity of the nation (land owner).
     * @param callback a list of Foreign armies.
     */
    void getForeignArmies(final int scenarioId, int gameId, int nationId,
                          AsyncCallback<List<ForeignArmyDTO>> callback);


    void getForeignUnitsOnNationTerritory(final int scenarioId, int nationId, int gameId, int turn,
                                          AsyncCallback<ForeignUnits> callback);

    void getChatMessageHistory(final int scenarioId, final int nationId, final int gameId,
                               AsyncCallback<List<String>> callback);

    void getCallForAllies(final int scenarioId, final int nationId, final int gameId, final int turn,
                          AsyncCallback<List<String>> callback);

    void getNavalTacticalBattlesPositions(final int scenarioId, final int nationId, final int gameId,
                                          final int turn, AsyncCallback<Map<Boolean, Map<Integer, BattleDTO>>> callback);

    /**
     * Retrieve the game's status to check if it is in process
     *
     * @param gameId   the id of the game
     * @param callback the call-back to handle the response.
     */
    void getGameStatus(final int scenarioId, int gameId, AsyncCallback<Boolean> callback);

    /**
     * Retrieve the game's status to get the date of process
     *
     * @param gameId   the id of the game
     * @param callback the call-back to handle the response.
     */
    void getProcessDate(final int scenarioId, int gameId, final int offset, AsyncCallback<String> callback);

    void getNationsStatus(final int scenarioId, final int gameId, final int turn, AsyncCallback<Map<Integer, Boolean>> callback);

    void updateAllianceView(final int scenarioId, final int gameId, final int nationId, final int targetNationId, final boolean visible, AsyncCallback<Integer> callback);

    void skipTutorial(final int gameId, AsyncCallback<Integer> callback);
}
