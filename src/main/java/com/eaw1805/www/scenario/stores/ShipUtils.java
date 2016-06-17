package com.eaw1805.www.scenario.stores;

import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.scenario.views.EditorPanel;
import com.eaw1805.www.scenario.views.ShipBrush;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 12/14/13
 * Time: 10:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShipUtils {

    private static int shipId = -1;

    public static void createShip(final int x, final int y) {
        final ShipBrush brush = EditorPanel.getInstance().getShipBrush();
        ShipDTO ship = new ShipDTO();
        ship.setId(getShipId());
        ship.setName(brush.getShipType().getName());
        ship.setType(brush.getShipType());
        ship.setNationId(brush.getNation().getNationId());
        ship.setRegionId(RegionSettings.region.getRegionId());
        ship.setX(x);
        ship.setY(y);
        ship.setMarines(brush.getShipType().getCitizens());

        final Map<Integer, Map<Integer, Map<Integer, ShipDTO>>> shipMap = EditorStore.getInstance().getShips().get(RegionSettings.region.getRegionId());
        if (!shipMap.containsKey(x)) {
            shipMap.put(x, new HashMap<Integer, Map<Integer, ShipDTO>>());
        }
        if (!shipMap.get(x).containsKey(y)) {
            shipMap.get(x).put(y, new HashMap<Integer, ShipDTO>());
        }
        shipMap.get(x).get(y).put(ship.getId(), ship);
        EditorMapUtils.getInstance().drawShip(ship);
    }

    public static void deleteShip(final ShipDTO ship) {
        //first if the brigade belongs to a corps.. update the corps.
        if (ship.getFleet() != 0) {
            EditorStore.getInstance().getFleets().get(ship.getRegionId()).get(ship.getX()).get(ship.getY()).get(ship.getFleet()).getShips().remove(ship.getId());
        }
        //second remove the brigade from the store.
        EditorStore.getInstance().getShips().get(ship.getRegionId()).get(ship.getX()).get(ship.getY()).remove(ship.getId());
    }



    private static int getShipId() {
        shipId--;
        return shipId;
    }
}
