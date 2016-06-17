package com.eaw1805.www.client.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
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
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("empire.rpc")
public interface EmpireRpcService extends RemoteService {

    /**
     * Retrieve all the regions.
     *
     * @param scenarioId the scenario of the game.
     * @return a list of Regions.
     */
    List<RegionDTO> getRegion(final int scenarioId);

    /**
     * Retrieves the nations of the game.
     *
     * @param scenarioId the scenario of the game.
     * @return a list of nations.
     */
    List<NationDTO> getNations(final int scenarioId);

    /**
     * Retrieve the tables of Natural Resources and Production Sites.
     *
     * @param scenarioId the scenario of the game.
     * @return two lists of NaturalResources and ProductionSites objects.
     */
    DataCollection getNaturalResAndProdSites(final int scenarioId);

    /**
     * Retrieves the army types for a particular nation.
     *
     * @param scenarioId the scenario of the game.
     * @param nationId   the identity of the nation.
     * @return the list of armytype objects.
     */
    List<ArmyTypeDTO> getArmyTypes(final int scenarioId, int nationId);

    /**
     * Retrieves the ship types for a particular nation.
     *
     * @param scenarioId the scenario of the game.
     * @param nationId   the identity of the nation.
     * @return the list of shiptype objects.
     */
    List<ShipTypeDTO> getShipTypes(final int scenarioId, int nationId);

    /**
     * Retrieve all sectors for a particular region.
     *
     * @param scenarioId the scenario of the game.
     * @param id         the identity of the Region.
     * @param gameId     the identity of the game.
     * @param turn       the turn of the game.
     * @return a list of sectors with the associated production sites and terrain objects.
     */
    TilesCollection getRegionByGameAndNation(final int scenarioId, int id, int gameId, int turn, int nationId);

    /**
     * Retrieve NationsRelations for a specific game, nation (player) and region of the map.
     *
     * @param scenarioId the scenario of the game.
     * @param nationId   the identity of the nation.
     * @param gameId     the identity of the game.
     * @param turn       the turn of the game.
     * @return a list of EmpireRelationDTO objects.
     */
    List<RelationDTO> getNationsRelations(final int scenarioId, int gameId, int nationId, int turn);

    /**
     * Retrieve the warehouse information for a specific game and nation (player).
     *
     * @param scenarioId the scenario of the game.
     * @param nationId   the identity of the nation.
     * @param gameId     the identity of the game.
     * @return a list of EmpireRelationDTO objects.
     */
    List<WarehouseDTO> getWarehouses(final int scenarioId, int gameId, int nationId);

    /**
     * Retrieve all the Sectors owned by a particular nation that have a barrack or shipyard.
     *
     * @param scenarioId the scenario of the game.
     * @param nationId   the identity of the nation.
     * @param gameId     the identity of the game.
     * @return a list of sectors.
     */
    List<BarrackDTO> getBarracksByNation(final int scenarioId, int nationId, int gameId, int turn);

    /**
     * Retrieve ships for a specific game, nation (player) and region of the map.
     *
     * @param scenarioId the scenario of the game.
     * @param nationId   the identity of the nation.
     * @param gameId     the identity of the game.
     * @param turn       the turn of the game.
     * @return a EmpireShipsCollection object.
     */
    Map<Boolean, List<FleetDTO>> getFleetsByNation(final int scenarioId, int nationId,
                                                   int gameId, int turn);

    /**
     * Retrieve Armies for a specific game, nation (player).
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @return a list of ArmyDTO and a list of CommanderDTO objects.
     */
    ArmiesAndCommanders getArmiesByNation(final int scenarioId, int nationId, int gameId, int turn);

    List<BaggageTrainDTO> getBaggageTrainsBySector(final int scenarioId, final int sectorId, final int nationId, final int gameId);

    /**
     * Retrieve spies for a specific game, nation (player) and turn.
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @return a list of SpyDTO object.
     */
    public List<SpyDTO> getSpies(final int scenarioId, final int nationId, final int gameId, final int turn, final boolean applyOrders);

    /**
     * Retrieve baggage trains for a specific game, nation (player) and turn.
     *
     * @param scenarioId the scenario of the game.
     * @param nationId   the identity of the nation.
     * @param gameId     the identity of the game.
     * @param turn       the turn of the game.
     * @return a list of BaggageTrainDTO objects.
     */
    List<BaggageTrainDTO> getBaggageTrains(final int scenarioId, final int nationId, final int gameId, final int turn,
                                           final boolean isAllied, final int originalNationId, final boolean visible);

    /**
     * Calculate the supply lines for the given scenario, game, player.
     *
     * @param scenarioId the scenario of the game.
     * @param gameId     the identity of the game.
     * @param regionId   the region of the game.
     * @param nationId   the identity of the nation.
     * @return a set of positions that are within supply lines reach.
     */
    Set<PositionDTO> getSupplyLines(final int scenarioId, final int gameId, final int regionId, final int nationId);

    /**
     * Calculate the possible movements of an army.
     *
     * @param scenarioId the scenario of the game.
     * @param gameId     the identity of the game.
     * @param nationId   the identity of the nation.
     * @param startX     the startX coordinate of the army
     * @param startY     the startY coordinate of the army
     * @param mpAvail    movement points available
     * @param regionId   the region of the movement.
     * @param unitType   the type of the unit.
     * @return a list of movement paths.
     */
    Set<PathDTO> getPossibleMovementTiles(final int scenarioId, final int gameId, final int nationId,
                                          int startX, int startY,
                                          int mpAvail,
                                          int regionId,
                                          int unitType,
                                          final int conqueredSectors,
                                          final int warShips,
                                          final int conqueredNeutralSectors,
                                          final List<Integer> otherNations);

    int saveUnitChanges(final int scenarioId, final List<ArmyDTO> chArmiesList,
                        final Map<Integer, List<BrigadeDTO>> newBrigadesmap,
                        final Map<Integer, List<ClientOrderDTO>> relOrders,
                        final int nationId,
                        final int gameId,
                        final int turn);

    int saveCommanderChanges(final int scenarioId, List<CommanderDTO> commandersList,
                             Map<Integer, List<ClientOrderDTO>> orderMap, int nationId,
                             int gameId, int turn);

    int saveEconomyChanges(final int scenarioId, Map<SectorDTO, Integer> sectorProdSites,
                           Map<Integer, List<ClientOrderDTO>> orderMap, int nationId,
                           int gameId, int turn);

    int saveBarrackChanges(final int scenarioId, final Map<Integer, BarrackDTO> chBarracksMap,
                           final Map<Integer, List<ClientOrderDTO>> orderMap,
                           final int nationId,
                           final int gameId,
                           final int turn);

    int saveRelationsChanges(final int scenarioId, List<RelationDTO> relationsList,
                             int nationId, int gameId, int turn);

    int saveMovementChanges(final int scenarioId, Map<Integer, Map<Integer, MovementDTO>> typeMvMap,
                            final Map<Integer, List<ClientOrderDTO>> orderMap,
                            int nationId, int gameId, int turn);

    int saveGameSettingsChanges(final int scenarioId, GameSettingsDTO settings,
                                int nationId, int gameId, int turn);

    int saveTransportChanges(final int scenarioId, List<ClientOrderDTO> cargoRelatedOrders,
                             int nationId, int gameId, int turn);

    int saveNavyChanges(final int scenarioId, Map<Integer, FleetDTO> idFleetMap,
                        Map<Integer, List<ShipDTO>> newShipMap,
                        Map<Integer, List<ClientOrderDTO>> orderMap, int nationId,
                        int gameId, int turn);

    int saveRegionChanges(final int scenarioId, Map<Integer, OrderPositionDTO> sectorOrderMap,
                          Map<Integer, List<ClientOrderDTO>> orderMap, int nationId,
                          int gameId, int turn);

    int saveTradeChanges(final int scenarioId, List<ClientOrderDTO> tradeOrders, int nationId, int gameId, int turn);


    /**
     * Post a news entry or send a private message.
     *
     * @param scenarioId   the scenario of the game.
     * @param nationId     the sender nation.
     * @param gameId       the game.
     * @param targetNation the target nation.
     * @param message      the body of the message.
     * @param type         if this is a news entry (anonymous/eponymous) or a private message.
     * @return 1 if successful, -1 if error occured.
     */
    int saveNewsletter(final int scenarioId, final int nationId, final int gameId,
                       final int targetNation, final String message, final int type);

    int saveBaggageTrainChanges(final int scenarioId, List<BaggageTrainDTO> baggageTList, Map<Integer, List<ClientOrderDTO>> orderMap,
                                Map<Integer, List<BaggageTrainDTO>> newBaggageTMap, int nationId,
                                int gameId, int turn);


    GameSettingsDTO getGameSettings(final int scenarioId, int nationId, int gameId, int turn);

    /**
     * Gets the data structure that contains all the movement information
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @return Map of movement objects
     */
    Map<Integer, Map<Integer, MovementDTO>> getMovementOrdersByGameNationAndTurn(final int scenarioId, int nationId,
                                                                                 int gameId,
                                                                                 int turn);

    Map<Integer, OrderPositionDTO> getRegionOrdersByGameNationAndTurn(final int scenarioId, int nationId,
                                                                      int gameId,
                                                                      int turn);

    /**
     * Gets the data structure that contains all the new brigades
     * we built this round up till now
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @return Map of movement objects
     */
    Map<Integer, List<BrigadeDTO>> getNewBrigadeOrdersByGameNationAndTurn(
            final int scenarioId, int nationId, int gameId, int turn);

    /**
     * Gets the data structure that contains all the new ships we built
     * this round up till now
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @return Map of movement objects
     */
    Map<Integer, List<ShipDTO>> getNewShipOrdersByGameNationAndTurn(
            final int scenarioId, int nationId, int gameId, int turn);

    int saveTaxationChanges(final int scenarioId, Taxation taxation, int nationId, int gameId,
                            int turn);

    Taxation getTaxationByNationAndGame(final int scenarioId, int nationId, int gameId, int turn);

    /**
     * Gets the data structure that contains all the new baggageTrains we built
     * this round up till now
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @return Map of baggageTrain objects selected by sector Id number
     */
    Map<Integer, List<BaggageTrainDTO>> getNewBaggageTrainOrdersByGameNationAndTurn(
            final int scenarioId, int nationId, int gameId, int turn);

    /**
     * Gets the data structure that contains all the orders
     * this round up till now
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @return Map of movement objects
     */
    Map<Integer, List<ClientOrderDTO>> getClientOrders(final int scenarioId, int nationId, int gameId,
                                                       int turn);

    /**
     * Gets the data structure that contains all the trade
     * cities
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @return List of the trade cities
     */
    List<TradeCityDTO> getTradeCities(final int scenarioId, int nationId, int gameId, int turn);

    /**
     * Retrieve allied units for a given nation,game and turn.
     *
     * @param nationId the identity of the nation.
     * @param gameId   the identity of the game.
     * @param turn     the turn of the game.
     * @return a map of type regiondid - nationId - AlliedUnits object
     */
    Map<Integer, Map<Integer, AlliedUnits>> getAlliedUnits(final int scenarioId, int nationId, int gameId, int turn);

    /**
     * Retrieve foreign units for a given game and nation.
     *
     * @param gameId   the identity of the game to check.
     * @param nationId the identity of the nation (land owner).
     * @return a list of Foreign armies.
     */
    List<ForeignArmyDTO> getForeignArmies(final int scenarioId, int gameId, int nationId);

    ForeignUnits getForeignUnitsOnNationTerritory(final int scenarioId, int nationId, int gameId, int turn);


    List<String> getChatMessageHistory(final int scenarioId, final int nationId, final int gameId);

    List<String> getCallForAllies(final int scenarioId, final int nationId, final int gameId, final int turn);

    Map<Boolean, Map<Integer, BattleDTO>> getNavalTacticalBattlesPositions(final int scenarioId, final int nationId,
                                                                           final int gameId,
                                                                           final int turn);

    /**
     * Retrieve the game's status to check if it is in process
     *
     * @param gameId the id of the game
     * @return true if the status is in process
     */
    boolean getGameStatus(final int scenarioId, int gameId);

    /**
     * Retrieve the game's date and time of process
     *
     * @param gameId the id of the game
     * @return the date string if the status is in process
     */
    String getProcessDate(final int scenarioId, int gameId, final int offset);

    /**
     * Detect if the given nation is alive or has lost.
     *
     * @param scenarioId the scenario.
     * @param gameId     the game.
     * @param turn       the turn.
     * @return true if the nation is still alive.
     */
    Map<Integer, Boolean> getNationsStatus(final int scenarioId, final int gameId, final int turn);

    int updateAllianceView(final int scenarioId, final int gameId, final int nationId, final int targetNationId, final boolean visible);

    /**
     * Skip tutorial for a current game.
     *
     * @param gameId The game to skip the tutorial steps.
     * @return 1 for success.
     */
    int skipTutorial(final int gameId);
}
