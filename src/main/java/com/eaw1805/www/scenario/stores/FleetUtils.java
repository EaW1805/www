package com.eaw1805.www.scenario.stores;


import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;

import java.util.HashMap;
import java.util.Map;

public class FleetUtils {

    private static int fleetId = -1;


    public static FleetDTO createFleet(String name, int regionId, int x, int y, final int nationId) {
        final FleetDTO fleet = new FleetDTO();
        fleet.setFleetId(getFleetId());
        fleet.setName(name);
        fleet.setNationId(nationId);
        fleet.setRegionId(regionId);
        fleet.setX(x);
        fleet.setY(y);
        final Map<Integer, Map<Integer, Map<Integer, FleetDTO>>> fleetsMap = EditorStore.getInstance().getFleets().get(regionId);
        if (!fleetsMap.containsKey(x)) {
            fleetsMap.put(x, new HashMap<Integer, Map<Integer, FleetDTO>>());
        }
        if (!fleetsMap.get(x).containsKey(y)) {
            fleetsMap.get(x).put(y, new HashMap<Integer, FleetDTO>());
        }
        fleetsMap.get(x).get(y).put(fleet.getFleetId(), fleet);
        return fleet;
//        EditorMapUtils.getInstance().drawBrigade(brigade);
    }

    public static void addShipToFleet(ShipDTO ship, FleetDTO fleet) {
        //just be sure if the brigade belongs in another corps to update that corps.
        removeShipFromFleet(ship);
        //then just add the brigade to corps
        fleet.getShips().put(ship.getId(), ship);
        ship.setFleet(fleet.getFleetId());
    }

    public static void removeShipFromFleet(ShipDTO ship) {
        if (ship.getFleet() == 0) {
            return;
        }
        final int x = ship.getX();
        final int y = ship.getY();
        final Map<Integer, Map<Integer, Map<Integer, FleetDTO>>> fleetMap = EditorStore.getInstance().getFleets().get(ship.getRegionId());
        if (!fleetMap.containsKey(x)) {
            fleetMap.put(x, new HashMap<Integer, Map<Integer, FleetDTO>>());
        }
        if (!fleetMap.get(x).containsKey(y)) {
            fleetMap.get(x).put(y, new HashMap<Integer, FleetDTO>());
        }
        final FleetDTO fleet = fleetMap.get(x).get(y).get(ship.getFleet());
        fleet.getShips().remove(ship.getId());
        ship.setFleet(0);
    }

    public static void deleteFleet(FleetDTO fleet) {
        //update all brigades
        for (ShipDTO ship : fleet.getShips().values()) {
            ship.setFleet(0);
        }
        //update corps map
        EditorStore.getInstance().getFleets().get(fleet.getRegionId()).get(fleet.getX()).get(fleet.getY()).remove(fleet.getFleetId());
    }

    private static int getFleetId() {
        fleetId--;
        return fleetId;
    }
}
