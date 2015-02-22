package com.eaw1805.www.controllers.game.army;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.managers.beans.BrigadeManagerBean;
import com.eaw1805.data.managers.beans.CommanderManagerBean;
import com.eaw1805.data.managers.beans.FleetManagerBean;
import com.eaw1805.data.managers.beans.PlayerOrderManagerBean;
import com.eaw1805.data.managers.beans.ShipManagerBean;
import com.eaw1805.data.managers.beans.ShipTypeManagerBean;
import com.eaw1805.data.managers.beans.SpyManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.PlayerOrder;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.army.Brigade;
import com.eaw1805.data.model.army.Commander;
import com.eaw1805.data.model.army.Spy;
import com.eaw1805.data.model.fleet.Fleet;
import com.eaw1805.data.model.fleet.Ship;
import com.eaw1805.data.model.fleet.ShipType;
import com.eaw1805.data.model.map.Position;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


/**
 * Controller for user requests related to armed forces.
 */
@Controller
public class ShowNavy
        extends ExtendedController implements OrderConstants, ArmyConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowNavy.class);

    @RequestMapping(method = RequestMethod.GET, value = "/report/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/navy")
    protected ModelAndView handle(@PathVariable final String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  HttpServletRequest request)
            throws Exception {
        ScenarioContextHolder.defaultScenario();
        if (scenarioId == null || scenarioId.isEmpty()) {
            throw new InvalidPageException("Page not found");
        } else {
            try {
                ScenarioContextHolder.setScenario(scenarioId);
            } catch (Exception e) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Retrieve Game entity
        final Game thisGame = getGame(gameId);
        if (thisGame == null) {
            throw new InvalidPageException("Page not found");
        }

        // Retrieve Nation entity
        final Nation thisNation = getNation(nationId);
        if (thisNation == null) {
            throw new InvalidPageException("Page not found");
        }


        // Check that user is allowed to view nation
        final User thisUser = getUser();
        if (thisUser.getUserType() != 3 && getUserGameManager().listActive(thisUser, thisGame, thisNation).isEmpty()) {
            throw new InvalidPageException("Page not found");
        }

        // access Calendar object
        final Calendar thisCal = calendar(thisGame);
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuf.append(" ");
        strBuf.append(thisCal.get(Calendar.YEAR));

        // Retrieve Armies
//        final List<Army> lstArmies = armyManager.listGameNation(thisGame, thisNation);

//        final List<Ship> lstShips = shipManager.listGameNation(thisGame, thisNation);

        final List<Fleet> lstFleets = fleetManager.listGameNation(thisGame, thisNation);
        final Map<Integer, Integer> merchantShips = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> warShips = new HashMap<Integer, Integer>();
        final Map<Integer, Map<String, Integer>> marines = new HashMap<Integer, Map<String, Integer>>();
        final Map<Integer, Map<String, Integer>> tonnage = new HashMap<Integer, Map<String, Integer>>();

        //construct dummy fleets
        final List<Ship> freeShips = shipManager.listFreeByGameNation(thisGame, thisNation);
        final Map<String, List<Ship>> positionToShip = new TreeMap<String, List<Ship>>();
        int dummyId = -1;
        for (Ship ship : freeShips) {
            if (!positionToShip.containsKey(ship.getPosition().toString())) {
                final Fleet dummyFleet = new Fleet();
                dummyFleet.setFleetId(dummyId--);
                dummyFleet.setName("Free Ships");
                dummyFleet.setNation(thisNation);
                dummyFleet.setPosition((Position) ship.getPosition().clone());
                lstFleets.add(dummyFleet);
                positionToShip.put(ship.getPosition().toString(), new ArrayList<Ship>());
            }
            positionToShip.get(ship.getPosition().toString()).add(ship);
        }


        final Map<Integer, Map<String, List<Fleet>>> sortedFleets = new TreeMap<Integer, Map<String, List<Fleet>>>();
        for (Fleet fleet : lstFleets) {
            if (!sortedFleets.containsKey(fleet.getPosition().getRegion().getId())) {
                sortedFleets.put(fleet.getPosition().getRegion().getId(), new TreeMap<String, List<Fleet>>());
            }
            if (!sortedFleets.get(fleet.getPosition().getRegion().getId()).containsKey(fleet.getPosition().toString())) {
                sortedFleets.get(fleet.getPosition().getRegion().getId()).put(fleet.getPosition().toString(), new ArrayList<Fleet>());
            }
            sortedFleets.get(fleet.getPosition().getRegion().getId()).get(fleet.getPosition().toString()).add(fleet);
        }

        final List<ShipType> shipTypes = shipTypeManager.list();
        final Map<Integer, Integer> intIdToClass = new HashMap<Integer, Integer>();
        for (final ShipType shipType : shipTypes) {
            intIdToClass.put(shipType.getIntId(), shipType.getShipClass());
        }

        final Map<Integer, Map<Integer, Map<Integer, Object[]>>> fleetToShips = new HashMap<Integer, Map<Integer, Map<Integer, Object[]>>>();
        final Map<Integer, Integer> fleetToNumberOfShips = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> fleetToNumberOfCannons = new HashMap<Integer, Integer>();
        final Map<Integer, Map<Integer, Integer>> fleetToGoods = new HashMap<Integer, Map<Integer, Integer>>();
        final Map<Integer, Boolean> fleetToShowGoods = new HashMap<Integer, Boolean>();
        final Map<Integer, Map<Integer, Integer>> fleetToLandUnits = new HashMap<Integer, Map<Integer, Integer>>();
        final Map<Integer, List<Brigade>> fleetToBrigades = new HashMap<Integer, List<Brigade>>();
        final Map<Integer, List<Spy>> fleetToSpies = new HashMap<Integer, List<Spy>>();
        final Map<Integer, List<Commander>> fleetToCommanders = new HashMap<Integer, List<Commander>>();

        final Map<Integer, List<Brigade>> shipToBrigades = new HashMap<Integer, List<Brigade>>();
        final Map<Integer, List<Spy>> shipToSpies = new HashMap<Integer, List<Spy>>();
        final Map<Integer, List<Commander>> shipToCommanders = new HashMap<Integer, List<Commander>>();

        for (final Fleet fleet : lstFleets) {
            fleetToShowGoods.put(fleet.getFleetId(), false);
            marines.put(fleet.getFleetId(), new HashMap<String, Integer>());
            marines.get(fleet.getFleetId()).put("0", 0);
            marines.get(fleet.getFleetId()).put("1", 0);
            tonnage.put(fleet.getFleetId(), new HashMap<String, Integer>());
            tonnage.get(fleet.getFleetId()).put("0", 0);
            tonnage.get(fleet.getFleetId()).put("1", 0);
            merchantShips.put(fleet.getFleetId(), 0);
            warShips.put(fleet.getFleetId(), 0);

            fleetToShips.put(fleet.getFleetId(), new TreeMap<Integer, Map<Integer, Object[]>>(new Comparator<Integer>() {
                @Override
                public int compare(final Integer o1, final Integer o2) {
                    return o2 - o1;
                }
            }));
            final List<Ship> fleetShips;
            if (fleet.getFleetId() > 0) {
                fleetShips = shipManager.listByFleet(thisGame, fleet.getFleetId());
            } else {
                fleetShips = positionToShip.get(fleet.getPosition().toString());
            }
            //initialize fleet warehouse
            fleetToGoods.put(fleet.getFleetId(), new TreeMap<Integer, Integer>());
            for (int goodId = GoodConstants.GOOD_FIRST; goodId <= GoodConstants.GOOD_LAST - 2; goodId++) {
                fleetToGoods.get(fleet.getFleetId()).put(goodId, 0);
            }
            //initialize fleet loaded units
            fleetToLandUnits.put(fleet.getFleetId(), new TreeMap<Integer, Integer>());
            fleetToBrigades.put(fleet.getFleetId(), new ArrayList<Brigade>());
            fleetToSpies.put(fleet.getFleetId(), new ArrayList<Spy>());
            fleetToCommanders.put(fleet.getFleetId(), new ArrayList<Commander>());
            fleetToNumberOfShips.put(fleet.getFleetId(), fleetShips.size());
            int numOfCannons = 0;
            for (Ship ship : fleetShips) {
                shipToBrigades.put(ship.getShipId(), new ArrayList<Brigade>());
                shipToCommanders.put(ship.getShipId(), new ArrayList<Commander>());
                shipToSpies.put(ship.getShipId(), new ArrayList<Spy>());
                for (int goodId = GoodConstants.GOOD_FIRST; goodId <= GoodConstants.GOOD_LAST - 2; goodId++) {
                    if (ship.getStoredGoods().containsKey(goodId)) {
                        fleetToGoods.get(fleet.getFleetId()).put(goodId, fleetToGoods.get(fleet.getFleetId()).get(goodId) + ship.getStoredGoods().get(goodId));
                        if (ship.getStoredGoods().get(goodId) > 0) {
                            fleetToShowGoods.put(fleet.getFleetId(), true);
                        }
                    }
                }
                for (Map.Entry<Integer, Integer> entry : ship.getStoredGoods().entrySet()) {
                    if (entry.getKey() > GoodConstants.GOOD_LAST) {
                        final int cargoType;
                        if (entry.getKey() >= ArmyConstants.SPY * 1000) {
                            cargoType = ArmyConstants.SPY;
                            Spy spy = spyManager.getByID(entry.getValue());
                            fleetToSpies.get(fleet.getFleetId()).add(spy);
                            shipToSpies.get(ship.getShipId()).add(spy);
                        } else if (entry.getKey() >= ArmyConstants.COMMANDER * 1000) {
                            cargoType = ArmyConstants.COMMANDER;
                            Commander commander = commanderManager.getByID(entry.getValue());
                            fleetToCommanders.get(fleet.getFleetId()).add(commander);
                            shipToCommanders.get(ship.getShipId()).add(commander);
                        } else if (entry.getKey() >= ArmyConstants.BRIGADE * 1000) {
                            cargoType = ArmyConstants.BRIGADE;
                            Brigade brigade = brigadeManager.getByID(entry.getValue());
                            fleetToBrigades.get(fleet.getFleetId()).add(brigade);
                            shipToBrigades.get(ship.getShipId()).add(brigade);
                        } else {
                            cargoType = -1;
                        }
                        if (!fleetToLandUnits.get(fleet.getFleetId()).containsKey(cargoType)) {
                            fleetToLandUnits.get(fleet.getFleetId()).put(cargoType, 1);
                        } else {
                            fleetToLandUnits.get(fleet.getFleetId()).put(cargoType, fleetToLandUnits.get(fleet.getFleetId()).get(cargoType) + 1);
                        }

                    }
                }
                int type;
                if (ship.getType().getShipClass() == 0) {
                    merchantShips.put(fleet.getFleetId(), merchantShips.get(fleet.getFleetId()) + 1);
                    type = 0;
                } else {
                    warShips.put(fleet.getFleetId(), warShips.get(fleet.getFleetId()) + 1);
                    type = 1;
                }
                if (!fleetToShips.get(fleet.getFleetId()).containsKey(type)) {
                    fleetToShips.get(fleet.getFleetId()).put(type, new TreeMap<Integer, Object[]>(new Comparator<Integer>() {
                        @Override
                        public int compare(Integer o1, Integer o2) {
                            if (intIdToClass.get(o2).equals(intIdToClass.get(o1))) {
                                return o2 - o1;
                            } else {
                                return intIdToClass.get(o2) - intIdToClass.get(o1);
                            }

                        }
                    }));
                }
                marines.get(fleet.getFleetId()).put(Integer.toString(type), marines.get(fleet.getFleetId()).get(Integer.toString(type)) + ship.getMarines());
                tonnage.get(fleet.getFleetId()).put(Integer.toString(type), tonnage.get(fleet.getFleetId()).get(Integer.toString(type)) + ship.getType().getLoadCapacity());
                final Object[] stats;
                if (fleetToShips.get(fleet.getFleetId()).get(type).containsKey(ship.getType().getIntId())) {
                    stats = fleetToShips.get(fleet.getFleetId()).get(type).get(ship.getType().getIntId());
                    stats[0] = (Double) stats[0] + 1;
                    stats[1] = (Double) stats[1] + ship.getCondition();
                    stats[2] = (Double) stats[2] + ship.getMarines();
                    List<Ship> shipsList = (ArrayList<Ship>) stats[3];
                    shipsList.add(ship);
                    fleetToShips.get(fleet.getFleetId()).get(type).put(ship.getType().getIntId(), stats);
                } else {
                    stats = new Object[4];
                    stats[0] = 1.0;//the number of ships
                    stats[1] = (double) ship.getCondition();//the average experience.
                    stats[2] = (double) ship.getMarines();//the total number of marines.
                    List<Ship> shipsList = new ArrayList<Ship>();
                    shipsList.add(ship);
                    stats[3] = shipsList;
                    fleetToShips.get(fleet.getFleetId()).get(type).put(ship.getType().getIntId(), stats);
                }
                numOfCannons += ship.getType().getCannons();
            }
            fleetToNumberOfCannons.put(fleet.getFleetId(), numOfCannons);
        }

        for (Map.Entry<Integer, Map<Integer, Map<Integer, Object[]>>> entry : fleetToShips.entrySet()) {
            int fleetId = entry.getKey();
            for (Map.Entry<Integer, Map<Integer, Object[]>> entry2 : entry.getValue().entrySet()) {
                int type = entry2.getKey();
                for (Map.Entry<Integer, Object[]> entry3 : entry2.getValue().entrySet()) {
                    int typeId = entry3.getKey();
                    Object[] stats = entry3.getValue();
                    stats[1] = (Double) stats[1] / (Double) stats[0];
                    fleetToShips.get(fleetId).get(type).put(typeId, stats);
                }

            }
        }

        final List<PlayerOrder> orders = ordersManager.listByGameNationType(thisGame, thisNation, thisGame.getTurn() - 1, new Object[]{ORDER_EXCHF, ORDER_EXCHS});
        final Set<Integer> shipsWithOrders = new HashSet<Integer>();
        for (PlayerOrder order : orders) {
            if (Integer.parseInt(order.getParameter1()) == SHIP) {
                shipsWithOrders.add(Integer.parseInt(order.getParameter2()));
            }
            if (Integer.parseInt(order.getParameter3()) == SHIP) {
                shipsWithOrders.add(Integer.parseInt(order.getParameter4()));
            }
        }

        final List<Nation> allNations = getNationManager().list();
        final Map<String, Nation> nameToNation = new HashMap<String, Nation>();
        for (Nation nation : allNations) {
            nameToNation.put(nation.getName(), nation);
        }
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("fleetToNumberOfShips", fleetToNumberOfShips);
        refData.put("fleetToNumberOfCannons", fleetToNumberOfCannons);
        refData.put("user", thisUser);
        refData.put("game", thisGame);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        refData.put("turn", thisGame.getTurn());
        refData.put("nation", thisNation);
        refData.put("gameId", thisGame.getGameId());
        refData.put("gameDate", strBuf.toString());
        refData.put("nationId", thisNation.getId());
        refData.put("sortedFleets", sortedFleets);
        refData.put("fleets", lstFleets);
        refData.put("fleetToShips", fleetToShips);
        refData.put("vp", 0);
        refData.put("merchantShips", merchantShips);
        refData.put("warShips", warShips);
        refData.put("marines", marines);
        refData.put("tonnage", tonnage);
        refData.put("fleetToGoods", fleetToGoods);
        refData.put("fleetToShowGoods", fleetToShowGoods);
        refData.put("fleetToLandUnits", fleetToLandUnits);
        refData.put("shipsWithOrders", shipsWithOrders);
        refData.put("fleetToBrigades", fleetToBrigades);
        refData.put("fleetToCommanders", fleetToCommanders);
        refData.put("fleetToSpies", fleetToSpies);

        refData.put("shipToBrigades", shipToBrigades);
        refData.put("shipToCommanders", shipToCommanders);
        refData.put("shipToSpies", shipToSpies);
        refData.put("nameToNation", nameToNation);
        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show navy for game=" + gameId + "/nation=" + thisNation.getName());

        if (request.getParameter("eawClient") == null) {
            return new ModelAndView("game/navy", refData);
        } else {
            return new ModelAndView("game/navyMinimal", refData);
        }

    }

    /**
     * Instance ShipManager class to perform queries
     * about ship objects.
     */
    private transient ShipManagerBean shipManager;

    /**
     * Setter method used by spring to inject a ShipManager bean.
     *
     * @param injShipManager a ShipManager bean.
     */
    public void setShipManager(final ShipManagerBean injShipManager) {
        this.shipManager = injShipManager;
    }

    /**
     * Instance FleetManager class to perform queries
     * about fleet objects.
     */
    private transient FleetManagerBean fleetManager;

    /**
     * Setter method used by spring to inject a FleetManager bean.
     *
     * @param injFleetManager a FleetManager bean.
     */
    public void setFleetManager(final FleetManagerBean injFleetManager) {
        this.fleetManager = injFleetManager;
    }

    private transient PlayerOrderManagerBean ordersManager;

    public void setOrdersManager(PlayerOrderManagerBean ordersManager) {
        this.ordersManager = ordersManager;
    }

    private transient BrigadeManagerBean brigadeManager;

    public void setBrigadeManager(BrigadeManagerBean brigadeManager) {
        this.brigadeManager = brigadeManager;
    }

    private transient SpyManagerBean spyManager;

    public void setSpyManager(SpyManagerBean spyManager) {
        this.spyManager = spyManager;
    }

    private transient CommanderManagerBean commanderManager;

    public void setCommanderManager(CommanderManagerBean commanderManager) {
        this.commanderManager = commanderManager;
    }

    private transient ShipTypeManagerBean shipTypeManager;

    public void setShipTypeManager(ShipTypeManagerBean shipTypeManager) {
        this.shipTypeManager = shipTypeManager;
    }
}
