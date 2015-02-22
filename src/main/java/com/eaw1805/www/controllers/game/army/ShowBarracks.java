package com.eaw1805.www.controllers.game.army;

import com.eaw1805.data.managers.beans.BattalionManagerBean;
import com.eaw1805.data.managers.beans.SectorManagerBean;
import com.eaw1805.data.managers.beans.ShipManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.army.Battalion;
import com.eaw1805.data.model.fleet.Ship;
import com.eaw1805.data.model.map.Sector;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Controller for user requests related to armed forces.
 */
public class ShowBarracks
        extends ExtendedController {

    private final int ARTILLERY = 1;
    private final int CAVALRY = 2;
    private final int INFANTRY = 3;
    private final int ENGINEER = 4;

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowBarracks.class);


    @RequestMapping(method = RequestMethod.GET, value = "/game/{gameId}/nation/{nationId}/barracks")
    protected ModelAndView handle(@PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  HttpServletRequest request)
            throws Exception {

        // Retrieve Game entity
        System.out.println("show barracks " + gameId + " - " + nationId);
        Game thisGame;
        if ((gameId == null) || (gameId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisGame = getGameManager().getByID(Integer.parseInt(gameId));
            if (thisGame == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Retrieve Nation entity

        Nation thisNation;
        if ((nationId == null) || (nationId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisNation = getNationManager().getByID(Integer.parseInt(nationId));
            if (thisNation == null) {
                throw new InvalidPageException("Page not found");
            }
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

        // Retrieve Barracks
        final List<Sector> lstBarracks = sectorManager.listBarracksByGameNation(thisGame, thisNation);

        //contains battalions from the specific game at the specific position
        Map<Integer, Map<Integer, Map<Integer, Object[]>>> barToBat = new HashMap<Integer, Map<Integer, Map<Integer, Object[]>>>();
        //contains ships from the specific game at the specific position
        Map<Integer, Map<Integer, Map<Integer, Object[]>>> barToShip = new HashMap<Integer, Map<Integer, Map<Integer, Object[]>>>();

        //find battalions kai ships for each barrack(sector actually) position
        for (Sector sector : lstBarracks) {
            barToBat.put(sector.getId(), new HashMap<Integer, Map<Integer, Object[]>>());
            barToShip.put(sector.getId(), new HashMap<Integer, Map<Integer, Object[]>>());
            //get all battalions in the sectors position
            for (Battalion battalion : battalionManager.listByGamePosition(sector.getPosition())) {
                int type;
                if (battalion.getType().isArtillery() ||
                        battalion.getType().isMArtillery()) {
                    type = ARTILLERY;
                } else if (battalion.getType().isCavalry()) {
                    type = CAVALRY;
                } else if (battalion.getType().isInfantry()) {
                    type = INFANTRY;
                } else {
                    type = ENGINEER;
                }
                if (!barToBat.get(sector.getId()).containsKey(type)) {
                    barToBat.get(sector.getId()).put(type, new HashMap<Integer, Object[]>());
                }
                final Object[] stats;
                if (barToBat.get(sector.getId()).get(type).containsKey(battalion.getType().getIntId())) {
                    stats = barToBat.get(sector.getId()).get(type).get(battalion.getType().getIntId());
                    stats[0] = (Double) stats[0] + 1;
                    stats[1] = (Double) stats[1] + battalion.getExperience();
                    stats[2] = (Double) stats[2] + battalion.getHeadcount();
                    List<Battalion> batsList = (ArrayList<Battalion>) stats[3];
                    batsList.add(battalion);
                    barToBat.get(sector.getId()).get(type).put(battalion.getType().getIntId(), stats);
                } else {
                    stats = new Object[4];
                    stats[0] = 1.0;
                    stats[1] = (double) battalion.getExperience();
                    stats[2] = (double) battalion.getHeadcount();
                    List<Battalion> batsList = new ArrayList<Battalion>();
                    batsList.add(battalion);
                    stats[3] = batsList;
                    barToBat.get(sector.getId()).get(type).put(battalion.getType().getIntId(), stats);
                }
            }
            for (Ship ship : shipManager.listByGamePosition(sector.getPosition())) {
                int type;
                if (ship.getType().getShipClass() == 0) {
                    type = 0;
                } else {
                    type = 1;
                }
                if (!barToShip.get(sector.getId()).containsKey(type)) {
                    barToShip.get(sector.getId()).put(type, new HashMap<Integer, Object[]>());
                }
                final Object[] stats;
                if (barToShip.get(sector.getId()).get(type).containsKey(ship.getType().getIntId())) {
                    stats = barToShip.get(sector.getId()).get(type).get(ship.getType().getIntId());
                    stats[0] = (Double) stats[0] + 1;
                    stats[1] = (Double) stats[1] + ship.getCondition();
                    stats[2] = (Double) stats[2] + ship.getMarines();
                    List<Ship> shipsList = (ArrayList<Ship>) stats[3];
                    shipsList.add(ship);
                    barToShip.get(sector.getId()).get(type).put(ship.getType().getIntId(), stats);
                } else {
                    stats = new Object[4];
                    stats[0] = 1.0;
                    stats[1] = (double) ship.getCondition();
                    stats[2] = (double) ship.getMarines();
                    List<Ship> shipsList = new ArrayList<Ship>();
                    shipsList.add(ship);
                    stats[3] = shipsList;
                    barToShip.get(sector.getId()).get(type).put(ship.getType().getIntId(), stats);
                }
            }
        }

        //update all average experiences...
        for (Map.Entry<Integer, Map<Integer, Map<Integer, Object[]>>> entry : barToBat.entrySet()) {
            int barId = entry.getKey();
            for (Map.Entry<Integer, Map<Integer, Object[]>> entry2 : entry.getValue().entrySet()) {
                int type = entry2.getKey();
                for (Map.Entry<Integer, Object[]> entry3 : entry2.getValue().entrySet()) {
                    int batTypeId = entry3.getKey();
                    Object[] stats = entry3.getValue();
                    stats[1] = (Double) stats[1] / (Double) stats[0];//average exp = total exp / total battalions
                    barToBat.get(barId).get(type).put(batTypeId, stats);
                }
            }
        }

        for (Map.Entry<Integer, Map<Integer, Map<Integer, Object[]>>> entry : barToShip.entrySet()) {
            int barId = entry.getKey();
            for (Map.Entry<Integer, Map<Integer, Object[]>> entry2 : entry.getValue().entrySet()) {
                int type = entry2.getKey();
                for (Map.Entry<Integer, Object[]> entry3 : entry2.getValue().entrySet()) {
                    int typeId = entry3.getKey();
                    Object[] stats = entry3.getValue();
                    stats[1] = (Double) stats[1] / (Double) stats[0];
                    barToShip.get(barId).get(type).put(typeId, stats);
                }
            }
        }

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("barToBat", barToBat);
        refData.put("barToShip", barToShip);
        refData.put("user", thisUser);
        refData.put("game", thisGame);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        refData.put("nation", thisNation);
        refData.put("gameId", thisGame.getGameId());
        refData.put("gameDate", strBuf.toString());
        refData.put("nationId", thisNation.getId());
        refData.put("barracks", lstBarracks);
        refData.put("vp", 0);

        LOGGER.debug("[" + thisUser.getUsername() + "/" + request.getHeader("CF-Connecting-IP") + "] Show barracks for game=" + gameId + "/nation=" + thisNation.getName());
        if (request.getParameter("eawClient") == null) {
            return new ModelAndView("game/barracks", refData);
        } else {
            return new ModelAndView("game/barracksMinimal", refData);
        }

    }

    /**
     * Instance SectorManager class to perform queries
     * about commander objects.
     */
    private transient SectorManagerBean sectorManager;

    /**
     * Setter method used by spring to inject a SectorManager bean.
     *
     * @param injSectorManager a SectorManager bean.
     */
    public void setSectorManager(final SectorManagerBean injSectorManager) {
        sectorManager = injSectorManager;
    }

    private transient BattalionManagerBean battalionManager;

    public void setBattalionManager(BattalionManagerBean battalionManager) {
        this.battalionManager = battalionManager;
    }

    private transient ShipManagerBean shipManager;

    public void setShipManager(ShipManagerBean shipManager) {
        this.shipManager = shipManager;
    }
}
