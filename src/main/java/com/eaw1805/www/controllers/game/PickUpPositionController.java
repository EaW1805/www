package com.eaw1805.www.controllers.game;


import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.AchievementConstants;
import com.eaw1805.data.constants.ForumRoleConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.ProfileConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.managers.beans.PaymentHistoryManagerBean;
import com.eaw1805.data.managers.beans.ProfileManagerBean;
import com.eaw1805.data.managers.beans.RegionManagerBean;
import com.eaw1805.data.managers.beans.UserPermissionManagerBean;
import com.eaw1805.data.managers.beans.WarehouseManagerBean;
import com.eaw1805.data.model.Achievement;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.PaymentHistory;
import com.eaw1805.data.model.Profile;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.data.model.economy.Warehouse;
import com.eaw1805.data.model.forum.UserPermissions;
import com.eaw1805.data.model.map.Region;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Pick up a position.
 */
@Controller
public class PickUpPositionController
        extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(PickUpPositionController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/game/{gameId}/pickup/{nationId}")
    protected ModelAndView handle(@PathVariable("scenarioId") String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  HttpServletRequest request) throws Exception {
        ScenarioContextHolder.setScenario(scenarioId);
        System.out.println("checking scenario id? " + scenarioId + "? " + ScenarioContextHolder.getScenario());
        // Retrieve UserGame entity
        if (gameId == null || gameId.isEmpty()
                || nationId == null || nationId.isEmpty()) {
            throw new InvalidPageException("Page not found");
        }

        final Game thisGame = getGameManager().getByID(Integer.parseInt(gameId));
        final Nation thisNation = getNationManager().getByID(Integer.parseInt(nationId));
        final User thisUser = getUser();

        //be sure that everything is ok to proceed.
        if (thisGame == null || thisNation == null || thisUser == null) {
            throw new InvalidPageException("Page not found");
        }

        if (thisUser.getUserId() <= 0) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/login");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        //validate amounts
        final int positionCost = thisNation.getCost() - (thisGame.getDiscount() * thisNation.getCost() / 100);

        //nec=not enough credits
        if (thisUser.getCreditFree() + thisUser.getCreditTransferred() + thisUser.getCreditBought() < positionCost) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/scenario/" + thisGame.getScenarioIdToString() + "/game/" + thisGame.getGameId() + "/info?err=nec&cost=" + thisNation.getCost());
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        //validate user games.
        final List<UserGame> playedGames = userGameManager.listPlayedUserGames(thisUser, thisGame, thisNation);

        boolean hasOtherActiveCountry = false;
        if (playedGames.size() > 0) {
            hasOtherActiveCountry = true;
        }

        //hoag=has other active game
        if (hasOtherActiveCountry) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/scenario/" + thisGame.getScenarioIdToString() + "/game/" + thisGame.getGameId() + "/info?err=hoag");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        //be sure position is not taken.
        final List<UserGame> userGames = userGameManager.list(thisGame, thisNation);

        //find if there is any active user in this game with this empire.
        boolean hasActiveUser = false;
        for (UserGame userGame : userGames) {
            if (userGame.isActive()) {
                hasActiveUser = true;
                break;
            }
        }

        //else don't allow to take the position.
        if (!hasActiveUser) {

            UserGame userGame;
            boolean userGameExists = false;

            //get the position
            if (!userGames.isEmpty()) {
                userGame = userGames.get(0);
                userGameExists = true;

            } else {
                userGame = new UserGame();
            }

            userGame.setGame(thisGame);
            userGame.setUserId(thisUser.getUserId());
            userGame.setAccepted(true);
            userGame.setNation(thisNation);
            userGame.setActive(true);
            userGame.setAlive(true);
            userGame.setHasWon(false);
            userGame.setTurnPickUp(thisGame.getTurn());
            userGame.setTurnDrop(0);
            userGame.setCurrent(true);
            userGame.setCost(positionCost);

            // check if special bonus applies
            userGame.setOffer(0);
            if (thisGame.getTurn() > 0) {
                // Check if this position is entitled to special offer for 60 free credits
                final List<UserGame> lstPrevPositions = userGameManager.listInActive(thisGame, userGame.getNation());
                if (!lstPrevPositions.isEmpty()) {
                    if (thisGame.getTurn() - lstPrevPositions.get(0).getTurnDrop() >= 2) {
                        userGame.setOffer(60);

                        // Give 60 Credits and double AP/CP
                        doSpecialOffer(thisGame, thisNation, thisUser);
                    }
                }
            }

            if (userGameExists) {
                userGameManager.update(userGame);

            } else {
                userGameManager.add(userGame);
            }

            //Update Forum Permissions.
            updatePermissions(thisUser, thisGame.getForumId(), ForumRoleConstants.ROLE_FORUM_POLLS);

            //Checks For VPs.
            if (thisGame.getTurn() == 0) {
                // Joining a new Game -- 20 Vps
                changeProfile(thisUser, thisGame, thisNation, ProfileConstants.VPS, AchievementConstants.PICKUP_POSITION);

                addAchievement(thisUser, thisGame, AchievementConstants.DESC_PICKUP_POSITION,
                        AchievementConstants.VPS, AchievementConstants.LEVEL_1,
                        AchievementConstants.PICKUP_POSITION, 0);

            } else {

                // Pick up an inactive position -- 30 Vps
                changeProfile(thisUser, thisGame, thisNation, ProfileConstants.VPS, AchievementConstants.PICKUP_INACTIVE_POSITION);

                addAchievement(thisUser, thisGame, AchievementConstants.DESC_PICKUP_INACTIVE_POSITION,
                        AchievementConstants.VPS, AchievementConstants.LEVEL_1,
                        AchievementConstants.PICKUP_INACTIVE_POSITION, 0);
            }
        }

        final RedirectView redirectView = new RedirectView(request.getContextPath() + "/scenario/" + thisGame.getScenarioIdToString() + "/game/"
                + thisGame.getGameId() + "/info?status=joined&g=" + gameId + "&n=" + thisNation.getId() + "&nn=" + thisNation.getName());
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
    }

    /**
     * Give 60 free credits and double AP/CP.
     *
     * @param thisGame   the game.
     * @param thisNation the nation.
     * @param thisUser   the user.
     */
    private void doSpecialOffer(final Game thisGame, final Nation thisNation, final User thisUser) {
        final User admin = userManager.getByUserName("admin");

        // Add credits to user
        thisUser.setCreditFree(thisUser.getCreditFree() + 60);
        userManager.update(thisUser);

        admin.setCreditFree(admin.getCreditFree() - 60);
        userManager.update(admin);

        // keep track of history
        final PaymentHistory receiverUserPayment = new PaymentHistory();
        receiverUserPayment.setUser(thisUser);
        receiverUserPayment.setComment("60 FREE credits for picking-up " + thisNation.getName() + " G" + thisGame.getGameId());
        receiverUserPayment.setDate(new Date());
        receiverUserPayment.setAgent(admin);
        receiverUserPayment.setType(PaymentHistory.TYPE_OFFER);
        receiverUserPayment.setCreditFree(thisUser.getCreditFree());
        receiverUserPayment.setCreditBought(thisUser.getCreditBought());
        receiverUserPayment.setCreditTransferred(thisUser.getCreditTransferred());
        receiverUserPayment.setChargeBought(0);
        receiverUserPayment.setChargeFree(60);
        receiverUserPayment.setChargeTransferred(0);
        pmHistoryManager.add(receiverUserPayment);

        final PaymentHistory adminUserPayment = new PaymentHistory();
        adminUserPayment.setUser(admin);
        adminUserPayment.setComment("Credited user " + thisUser.getUsername() + " 60 FREE credits for picking-up " + thisNation.getName() + " G" + thisGame.getGameId());
        adminUserPayment.setDate(new Date());
        adminUserPayment.setAgent(thisUser);
        adminUserPayment.setType(PaymentHistory.TYPE_OFFER);
        adminUserPayment.setCreditFree(admin.getCreditFree());
        adminUserPayment.setCreditBought(admin.getCreditBought());
        adminUserPayment.setCreditTransferred(admin.getCreditTransferred());
        adminUserPayment.setChargeBought(0);
        adminUserPayment.setChargeFree(-60);
        adminUserPayment.setChargeTransferred(0);
        pmHistoryManager.add(adminUserPayment);

        // Double AP, CP
        final Region europe = regionManager.getByID(RegionConstants.EUROPE);
        final Warehouse thisWarehouse = warehouseManager.getByNationRegion(thisGame, thisNation, europe);
        final Map<Integer, Integer> storedGoods = thisWarehouse.getStoredGoodsQnt();
        storedGoods.put(GoodConstants.GOOD_AP, 2 * storedGoods.get(GoodConstants.GOOD_AP));
        storedGoods.put(GoodConstants.GOOD_CP, 2 * storedGoods.get(GoodConstants.GOOD_CP));
        thisWarehouse.setStoredGoodsQnt(storedGoods);
        warehouseManager.update(thisWarehouse);
    }

    /**
     * Updates the user permissions.
     *
     * @param thisUser   the input user
     * @param forumId    the forum id
     * @param permission the permission role (ForumRoleConstants)
     */
    private void updatePermissions(final User thisUser, final int forumId, final int permission) {
        UserPermissions userPermissions = permissionManager.getForumPermissions(thisUser, forumId);
        if (userPermissions == null) {
            userPermissions = new UserPermissions();
            userPermissions.setUser(thisUser);
            userPermissions.setForumId(forumId);
            userPermissions.setAuthRoleId(permission);
            permissionManager.add(userPermissions);

        } else {
            userPermissions.setAuthRoleId(permission);
            permissionManager.update(userPermissions);
        }

        thisUser.setUserPermissions("");
        userManager.update(thisUser);
    }

    /**
     * Increase/Decrease a profile attribute for the player of the nation.
     *
     * @param user     the user instance.
     * @param game     the game instance.
     * @param owner    the Nation to change the profile of the player.
     * @param key      the profile key of the player.
     * @param increase the increase or decrease in the profile entry.
     */
    public final void changeProfile(final User user, final Game game, final Nation owner,
                                    final String key, final int increase) {
        // Ignore Free Scenario
        if (game.getGameId() < 0 || game.getScenarioId() <= HibernateUtil.DB_MAIN || owner.getId() <= 0) {
            return;
        }

        // Retrieve profile entry
        final Profile entry = profileManager.getByOwnerKey(user, key);

        // If the entry does not exist, create
        if (entry == null) {
            final Profile newEntry = new Profile();
            newEntry.setUser(user);
            newEntry.setKey(key);
            newEntry.setValue(increase);
            profileManager.add(newEntry);

        } else {
            entry.setValue(entry.getValue() + increase);
            profileManager.update(entry);
        }
    }

    /**
     * Adds a new Achievement.
     *
     * @param user        the user instance.
     * @param game        the game instance.
     * @param description the Description of the Achievement.
     * @param category    the category of the Achievement.
     * @param level       the level of the Achievement.
     * @param vps         the Victory Points of the Achievement.
     * @param aps         the Achievement Points of the Achievement.
     */
    public final void addAchievement(final User user, final Game game, final String description,
                                     final int category, final int level,
                                     final int vps, final int aps) {
        // Ignore Free Scenario
        if (game.getGameId() < 0 || game.getScenarioId() <= HibernateUtil.DB_MAIN) {
            return;
        }

        // Generate new entry
        final Achievement achievement = new Achievement();
        achievement.setUser(user);
        achievement.setCategory(category);
        achievement.setLevel(level);
        achievement.setAnnounced(false);
        achievement.setFirstLoad(false);
        achievement.setDescription(description);
        achievement.setVictoryPoints(vps);
        achievement.setAchievementPoints(aps);
        getAchievementManager().add(achievement);
    }

    private transient UserPermissionManagerBean permissionManager;

    public void setPermissionManager(UserPermissionManagerBean permissionManager) {
        this.permissionManager = permissionManager;
    }

    private transient ProfileManagerBean profileManager;

    public void setProfileManager(ProfileManagerBean profileManager) {
        this.profileManager = profileManager;
    }

    /**
     * Instance of PaymentHistoryManager class.
     */
    protected transient PaymentHistoryManagerBean pmHistoryManager;

    public void setPmHistoryManager(PaymentHistoryManagerBean pmHistoryManager) {
        this.pmHistoryManager = pmHistoryManager;
    }

    /**
     * Instance WarehouseManagerBean class to perform queries
     * about warehouse objects.
     */
    private transient WarehouseManagerBean warehouseManager;

    /**
     * Setter method used by spring to inject a WarehouseManager bean.
     *
     * @param injWarehouseManager a WarehouseManager bean.
     */
    public void setWarehouseManager(final WarehouseManagerBean injWarehouseManager) {
        this.warehouseManager = injWarehouseManager;
    }

    /**
     * Instance RegionManagerBean class to perform queries
     * about region objects.
     */
    private transient RegionManagerBean regionManager;

    /**
     * Setter method used by spring to inject a RegionManager bean.
     *
     * @param injRegionManager a RegionManager bean.
     */
    public void setRegionManager(final RegionManagerBean injRegionManager) {
        regionManager = injRegionManager;
    }

}
