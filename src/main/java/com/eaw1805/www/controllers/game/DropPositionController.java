package com.eaw1805.www.controllers.game;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.AchievementConstants;
import com.eaw1805.data.constants.ForumRoleConstants;
import com.eaw1805.data.constants.ProfileConstants;
import com.eaw1805.data.managers.beans.PaymentHistoryManagerBean;
import com.eaw1805.data.managers.beans.ProfileManagerBean;
import com.eaw1805.data.managers.beans.UserPermissionManagerBean;
import com.eaw1805.data.model.Achievement;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.PaymentHistory;
import com.eaw1805.data.model.Profile;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.data.model.forum.UserPermissions;
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

/**
 * Drops a position.
 */
@Controller
public class DropPositionController
        extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(DropPositionController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/game/{gameId}/drop/{nationId}")
    protected ModelAndView handle(@PathVariable("scenarioId") String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  HttpServletRequest request) throws Exception {
        ScenarioContextHolder.setScenario(scenarioId);
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
            throw new InvalidPageException("Page not found");
        }

        //search game by user, game, nation
        List<UserGame> userGames = userGameManager.list(thisUser, thisGame, thisNation);
        //if empty position is free.
        //else don't allow to take the position.
        for (UserGame userGame : userGames) {
            //if usergame is not already dropped
            if (userGame.isActive()) {

                if (userGame.getTurnPickUp() == userGame.getGame().getTurn()) {
                    //stcd: Same Turn, Cannot Drop Your Position
                    final RedirectView redirectView = new RedirectView(request.getContextPath() + "/games?err=stcd");
                    redirectView.setExposeModelAttributes(false);
                    return new ModelAndView(redirectView);
                }
                userGame.setActive(false);
                userGame.setCurrent(false);
                userGame.setTurnDrop(userGame.getGame().getTurn());
                userGameManager.update(userGame);

                // check if free credits were given to the position due to a special offer
                if (userGame.getOffer() > 0) {
                    // Remove remaining credits
                    undoSpecialOffer(thisGame, thisNation, thisUser, userGame.getOffer());
                }

                userGame.setUserId(2);
                userGame.setAccepted(true);
                userGame.setTurnPickUp(0);
                userGame.setTurnDrop(0);
                userGame.setCurrent(true);
                userGame.setTurnFirstLoad(true);
                userGameManager.add(userGame);

                //Update Forum Permissions.
                updatePermissions(thisUser, thisGame.getForumId(), ForumRoleConstants.ROLE_FORUM_READONLY);

                changeProfile(thisUser, thisGame, thisNation, ProfileConstants.VPS, AchievementConstants.DROP_POSITION);
                addAchievement(thisUser, thisGame, AchievementConstants.DESC_DROP_POSITION,
                        AchievementConstants.VPS, AchievementConstants.LEVEL_1,
                        AchievementConstants.DROP_POSITION, 0);
            }
        }

        final RedirectView redirectView = new RedirectView(request.getContextPath() + "/games");
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
    }

    /**
     * Remove remaining free credits and double AP/CP.
     *
     * @param thisGame   the game.
     * @param thisNation the nation.
     * @param thisUser   the user.
     */
    private void undoSpecialOffer(final Game thisGame, final Nation thisNation, final User thisUser, final int credits) {
        final User admin = userManager.getByUserName("admin");

        // Add credits to user
        thisUser.setCreditFree(thisUser.getCreditFree() - credits);
        userManager.update(thisUser);

        admin.setCreditFree(admin.getCreditFree() + credits);
        userManager.update(admin);

        // keep track of history
        final PaymentHistory receiverUserPayment = new PaymentHistory();
        receiverUserPayment.setUser(thisUser);
        receiverUserPayment.setComment("Removing unused FREE credits after dropping " + thisNation.getName() + " G" + thisGame.getGameId());
        receiverUserPayment.setDate(new Date());
        receiverUserPayment.setAgent(admin);
        receiverUserPayment.setType(PaymentHistory.TYPE_OFFER);
        receiverUserPayment.setCreditFree(thisUser.getCreditFree());
        receiverUserPayment.setCreditBought(thisUser.getCreditBought());
        receiverUserPayment.setCreditTransferred(thisUser.getCreditTransferred());
        receiverUserPayment.setChargeBought(0);
        receiverUserPayment.setChargeFree(-credits);
        receiverUserPayment.setChargeTransferred(0);
        pmHistoryManager.add(receiverUserPayment);

        final PaymentHistory adminUserPayment = new PaymentHistory();
        adminUserPayment.setUser(admin);
        adminUserPayment.setComment("Removed unused FREE credits from user " + thisUser.getUsername() + " due to drop of " + thisNation.getName() + " G" + thisGame.getGameId());
        adminUserPayment.setDate(new Date());
        adminUserPayment.setAgent(thisUser);
        adminUserPayment.setType(PaymentHistory.TYPE_OFFER);
        adminUserPayment.setCreditFree(admin.getCreditFree());
        adminUserPayment.setCreditBought(admin.getCreditBought());
        adminUserPayment.setCreditTransferred(admin.getCreditTransferred());
        adminUserPayment.setChargeBought(0);
        adminUserPayment.setChargeFree(credits);
        adminUserPayment.setChargeTransferred(0);
        pmHistoryManager.add(adminUserPayment);
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

    public void setPermissionManager(final UserPermissionManagerBean value) {
        this.permissionManager = value;
    }

    private transient ProfileManagerBean profileManager;

    public void setProfileManager(final ProfileManagerBean value) {
        this.profileManager = value;
    }

    /**
     * Instance of PaymentHistoryManager class.
     */
    protected transient PaymentHistoryManagerBean pmHistoryManager;

    public void setPmHistoryManager(PaymentHistoryManagerBean pmHistoryManager) {
        this.pmHistoryManager = pmHistoryManager;
    }
}
