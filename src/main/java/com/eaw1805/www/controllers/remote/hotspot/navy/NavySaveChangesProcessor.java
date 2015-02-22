package com.eaw1805.www.controllers.remote.hotspot.navy;

import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.controllers.remote.EmpireRpcServiceImpl;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavySaveChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    /**
     * A hash map containing all the fleet ids and their corresponding objects
     */
    private transient final Map<Integer, FleetDTO> idFleetMap = new HashMap<Integer, FleetDTO>();

    private transient final Map<Integer, FleetDTO> chFleetsMap = new HashMap<Integer, FleetDTO>();

    private transient final Map<Integer, List<ShipDTO>> newShipMap = new HashMap<Integer, List<ShipDTO>>();

    private transient final FleetSaveChangesProcessor fChProcc;

    private transient final ShipSaveChangesProcessor sChProcc;

    /**
     * Default constructor.
     *
     * @param gameId   the game of the order.
     * @param nationId the owner of the order.
     * @param turn     the turn of the order.
     * @param orderMap the orders.
     */
    public NavySaveChangesProcessor(final int thisScenario, final int gameId,
                                    final int nationId,
                                    final int turn,
                                    final Map<Integer, List<ClientOrderDTO>> orderMap,
                                    final EmpireRpcServiceImpl service) {
        super(thisScenario, gameId, nationId, turn);
        this.fChProcc = new FleetSaveChangesProcessor(thisScenario, gameId, nationId, turn, orderMap);
        this.sChProcc = new ShipSaveChangesProcessor(thisScenario, gameId, nationId, turn, orderMap, service);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chOrders) {
        // clear any previous additions to collections
        final List<FleetDTO> dbFleetlist = (List<FleetDTO>) dbData;
        for (final FleetDTO fleet : dbFleetlist) {
            // Fleet with id -1 is a dummy fleet and
            // we don't want it in our data
            // and If there is no such fleet in our map
            if (fleet.getFleetId() != -1 && !idFleetMap.containsKey(fleet.getFleetId())) {
                // Make one.
                idFleetMap.put(fleet.getFleetId(), fleet);
            }
        }
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        chFleetsMap.putAll((HashMap<Integer, FleetDTO>) chData);
    }

    public void addNewShipData(final Map<Integer, List<ShipDTO>> newShipMap2) {
        newShipMap.putAll(newShipMap2);
    }


    public List<OrderDTO> processChanges() {
        // The returned list of commands
        final List<OrderDTO> allnavyCommands = new ArrayList<OrderDTO>();

        // Preparing data for the other processors
        final Map<Integer, FleetDTO> dbFleets = new HashMap<Integer, FleetDTO>(), chFleets = new HashMap<Integer, FleetDTO>();
        final Map<Integer, ShipDTO> dbShips = new HashMap<Integer, ShipDTO>(), chShips = new HashMap<Integer, ShipDTO>();

        for (final FleetDTO fleet : idFleetMap.values()) {
            if (fleet.getFleetId() != 0) {
                dbFleets.put(fleet.getFleetId(), fleet);
            }
            dbShips.putAll(fleet.getShips());
        }

        for (final FleetDTO fleet : chFleetsMap.values()) {
            if (fleet.getFleetId() != 0) {
                chFleets.put(fleet.getFleetId(), fleet);
            }
            chShips.putAll(fleet.getShips());
        }

        fChProcc.addData(dbFleets, chFleets);
        allnavyCommands.addAll(fChProcc.processChanges());

        sChProcc.addData(dbShips, chShips);
        sChProcc.addNewShipData(getNewShipMap());
        allnavyCommands.addAll(sChProcc.processChanges());

        return allnavyCommands;
    }

    public Map<Integer, List<ShipDTO>> getNewShipMap() {
        return newShipMap;
    }


}
