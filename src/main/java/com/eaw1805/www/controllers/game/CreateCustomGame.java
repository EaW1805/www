package com.eaw1805.www.controllers.game;


import com.eaw1805.core.EmailManager;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.ForumRoleConstants;
import com.eaw1805.data.constants.GameConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.managers.beans.ForumManagerBean;
import com.eaw1805.data.managers.beans.PaymentHistoryManagerBean;
import com.eaw1805.data.managers.beans.UserPermissionManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.PaymentHistory;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.data.model.forum.Forum;
import com.eaw1805.data.model.forum.UserPermissions;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.quartz.CronExpression;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class CreateCustomGame
        extends ExtendedController
        implements RegionConstants, GameConstants {
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(CreateCustomGame.class);


    @ModelAttribute("nations")
    public List<Nation> getNations() {
        List<Nation> nations = getNationManager().list();
        nations.remove(0);
        return nations;
    }

    @ModelAttribute("game")
    public Game getGame() {
        return new Game();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/game/createCustom")
    public String setupForm(final ModelMap model) throws Exception {
        final User user = getUser();
        if (user == null) {
            throw new InvalidPageException("Page not found");
        }

        return "game/createCustom";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/game/createCustom")
    public String processSettingsSubmit(@ModelAttribute("game") final Game game,
                                        final BindingResult result,
                                        final SessionStatus status)
            throws Exception {
        //first things first... validate user
        final User user = getUser();
        if (user == null) {
            throw new InvalidPageException("Page not found");
        }
        if (user.getCreditFree() + user.getCreditBought() + user.getCreditTransferred() < 200) {
            throw new InvalidPageException("Page not found");
        }

        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        boolean monday = request.getParameter("ps_monday") != null;
        boolean tuesday = request.getParameter("ps_tuesday") != null;
        boolean wednesday = request.getParameter("ps_wednesday") != null;
        boolean thursday = request.getParameter("ps_thursday") != null;
        boolean friday = request.getParameter("ps_friday") != null;
        boolean saturday = request.getParameter("ps_saturday") != null;
        boolean sunday = request.getParameter("ps_sunday") != null;
        boolean weekly = request.getParameter("ps_weekly") != null;
        boolean every2Weeks = request.getParameter("ps_every2Weeks") != null;

        final StringBuilder cronBuilder = new StringBuilder();
        final StringBuilder cronDescription = new StringBuilder();
        int countDays = 0;
        if (sunday) {
            countDays++;
            cronBuilder.append(",1");
            cronDescription.append(", Sunday");
        }

        if (monday) {
            countDays++;
            cronBuilder.append(",2");
            cronDescription.append(", Monday");
        }

        if (tuesday) {
            countDays++;
            cronBuilder.append(",3");
            cronDescription.append(", Tuesday");
        }

        if (wednesday) {
            countDays++;
            cronBuilder.append(",4");
            cronDescription.append(", Wednesday");
        }

        if (thursday) {
            countDays++;
            cronBuilder.append(",5");
            cronDescription.append(", Thursday");
        }

        if (friday) {
            countDays++;
            cronBuilder.append(",6");
            cronDescription.append(", Friday");
        }

        if (saturday) {
            countDays++;
            cronBuilder.append(",7");
            cronDescription.append(", Saturday");
        }

        if (countDays == 0) {
            LOGGER.info("NO DAYS SPECIFIED FOR PROCESS");
        }

        if (countDays > 0 && every2Weeks) {
            LOGGER.info("MORE THAN ONE DAY SPECIFIED FOR PROCESSING AND SCHEDULED EVERY 2 WEEKS");
        }

        game.setUserId(user.getUserId());
        game.setDateStart(new Date());
        game.setDateLastProc(new Date());
        if (every2Weeks) {//if every 2 weeks.. then arithmetic step.
            game.setSchedule(14);
            game.setCronScheduleDescr("every 2 weeks " + cronDescription.toString().substring(2));

        } else {//if every week, cron scheduler.
            game.setCronSchedule("0 0 0 ? * " + cronBuilder.toString().substring(1) + " *");
            game.setCronScheduleDescr("every " + cronDescription.toString().substring(2));
        }

        game.setStatus(GAME_CREATE);
        game.setWinners("");
        game.setCoWinners("");
        game.setRunnerUp("");
        game.setTurn(-1);

        //be sure the name won't have any dangerous code inside it.
        game.setName(Jsoup.clean(game.getName(), Whitelist.relaxed()));

        //if it is new game creation... calculate its new id before anything else...
        int nextGameId = 1;
        for (int db = HibernateUtil.DB_S1; db <= HibernateUtil.DB_S3; db++) {
            ScenarioContextHolder.setScenario(db);
            final List<Game> list = getGameManager().list();
            if (!list.isEmpty()) {
                final Game lastGame = list.get(list.size() - 1);
                if (nextGameId < lastGame.getGameId() + 1) {
                    nextGameId = lastGame.getGameId() + 1;
                }
            }
        }
        ScenarioContextHolder.setScenario(game.getScenarioId());

        //set fixed game id
        game.setGameId(nextGameId);

        getGameManager().add(game);

        //now fix usergames
        final List<Nation> lstNations = getNationManager().list();
        lstNations.remove(0);
        for (Nation nation : lstNations) {
            final int nationId = nation.getId();
            User nationsUser = null;
            if (request.getParameter("player_" + nationId) != null && !request.getParameter("player_" + nationId).isEmpty()) {
                nationsUser = getUserManager().getByUserName(request.getParameter("player_" + nationId));
            }
            if (nationsUser == null) {//if user not found.. then just add admin
                nationsUser = getUserManager().getByID(2);
            }
            final UserGame entry = new UserGame();
            entry.setCurrent(true);
            entry.setHasWon(false);
            entry.setActive(nationsUser.getUserId() != 2);
            entry.setAlive(true);
            entry.setNation(nation);
            entry.setCost(nation.getCost());
            entry.setUserId(nationsUser.getUserId());
            entry.setAccepted(nationsUser.getUserId() == user.getUserId());
            entry.setGame(game);
            entry.setTurnFirstLoad(true);
            getUserGameManager().add(entry);
            if (nationsUser.getUserId() != 2
                    && nationsUser.getUserId() != user.getUserId()) {
                try {
                    EmailManager.getInstance().sendInvitationNotification(nationsUser, user, entry);
                } catch (Exception e) {
                    LOGGER.error("Failed to send invitation notification email", e);
                }
            }

        }

        //now create the forum.
        final Forum forum = new Forum();
        forum.setParentId(6);//this is the forum Games group

        int leftId = forumManager.getById(6).getRightId();
        //those values seem to be increment
        //temporal values for left and right id..
        forum.setLeftId(0);
        forum.setRightId(0);
        forum.setForumParents("a:1:{i:6;a:2:{i:0;s:5:\"Games\";i:1;i:0;}}");
        forum.setForumName("Game " + nextGameId);
        forum.setForumDesc(game.getDescription());
        forum.setForumDescOptions(7);
        forum.setForumDescUid("");
        forum.setForumLink("");
        forum.setForumPassword("");
        forum.setForumStyle(0);
        forum.setForumImage("");
        forum.setForumRules("");
        forum.setForumRulesLink("");
        forum.setForumRulesBitfield("");
        forum.setForumRulesOptions(7);
        forum.setForumRulesUid("");
        forum.setForumTopicsPerPage(0);
        forum.setForumType(1);
        forum.setForumStatus(0);
        forum.setForumFlags(48);
        forum.setForumOptions(0);
        forum.setDisplaySubforumList(true);
        forum.setDisplayOnIndex(false);
        forum.setEnableIndexing(true);
        forum.setEnableIcons(false);
        forum.setEnablePrune(false);
        forum.setPruneNext(0);
        forum.setPruneDays(7);
        forum.setPruneViewed(7);
        forum.setPruneFreq(1);
        forum.setForumLastPostSubject("");
        forum.setForumLastPosterName("");
        forum.setForumLastPosterColour("");
        forumManager.add(forum);
        game.setForumId(forum.getForumId());
        getGameManager().update(game);

        //insert forum groups for the new forum.
        forumManager.updateForumGroups(forum.getForumId());
        forumManager.updateLeftRightIds(forum.getForumId(), leftId, leftId + 1);
        //give permissions to the owner of the game/forum.
        updatePermissions(user, game.getForumId(), ForumRoleConstants.ROLE_FORUM_POLLS);

        //be sure every user will be able to see the forum.
        for (User aUser : userManager.list()) {
            //couldn't use update query so do this in one step.
            //so we iterate through all users.
            if (aUser.getUserId() != user.getUserId()) {
                aUser.setUserPermissions("");
                userManager.update(aUser);
            }
        }

        //since all done... construct the game.
        System.out.println("SENDING JENKINS REQUEST");
        getArticleManager().getBuild(game.getScenarioId(), -1000);

        //finally charge the user
        // add payment history
        final StringBuilder paymentText = new StringBuilder();
        paymentText.append("Setup custom game / scenario ");
        paymentText.append(game.getScenarioIdToString());
        paymentText.append(" / game ");
        paymentText.append(game.getGameId());

        // Required credits: 150
        int remains = 150;
        final User admin = userManager.getByID(2);

        // First try to use free credits
        if (user != null && user.getCreditFree() > 0 && remains > 0) {
            // add payment history
            final PaymentHistory trans = new PaymentHistory();
            trans.setUser(user);
            trans.setAgent(admin);
            trans.setType(PaymentHistory.TYPE_SETUP);
            user.setCreditFree(user.getCreditFree() - remains);

            int payment = remains;

            if (user.getCreditFree() < 0) {
                remains = -1 * user.getCreditFree();
                payment -= remains;

                user.setCreditFree(0);

            } else {
                remains = 0;
            }

            // track history
            trans.setCreditFree(user.getCreditFree());
            trans.setCreditBought(user.getCreditBought());
            trans.setCreditTransferred(user.getCreditTransferred());

            // add amount
            trans.setChargeFree(-payment);
            trans.setDate(new Date());
            trans.setComment(paymentText.toString());
            paymentHistoryManager.add(trans);
        }

        // then try to use transferred credits
        if (remains > 0 && user.getCreditTransferred() > 0) {
            // add payment history
            final PaymentHistory trans = new PaymentHistory();
            trans.setUser(user);
            trans.setAgent(admin);
            trans.setType(PaymentHistory.TYPE_SETUP);

            user.setCreditTransferred(user.getCreditTransferred() - remains);
            int payment = remains;

            if (user.getCreditTransferred() < 0) {
                remains = -1 * user.getCreditTransferred();
                payment -= remains;
                user.setCreditTransferred(0);

            } else {
                remains = 0;
            }

            // track history
            trans.setCreditFree(user.getCreditFree());
            trans.setCreditBought(user.getCreditBought());
            trans.setCreditTransferred(user.getCreditTransferred());

            // add amount
            trans.setChargeTransferred(-payment);
            trans.setDate(new Date());
            trans.setComment(paymentText.toString());
            paymentHistoryManager.add(trans);
        }

        // then try to use paid credits
        if (remains > 0) {
            // add payment history
            final PaymentHistory trans = new PaymentHistory();
            trans.setUser(user);
            trans.setAgent(admin);
            trans.setType(PaymentHistory.TYPE_SETUP);

            user.setCreditBought(user.getCreditBought() - remains);

            // track history
            trans.setCreditFree(user.getCreditFree());
            trans.setCreditBought(user.getCreditBought());
            trans.setCreditTransferred(user.getCreditTransferred());

            // add amount
            trans.setChargeBought(-remains);
            trans.setDate(new Date());
            trans.setComment(paymentText.toString());
            paymentHistoryManager.add(trans);
        }

        //update users credits
        userManager.update(user);

        //finally reset all user privileges from the forums.
        //so they will be re-initialized correctly for the newly created forum.
        //doesn't seem to work.. need a better solution
//            userManager.resetAllUsersForumPrivilege();

        LOGGER.info("createCustomGame/User " + getUser().getUsername() + " created a new " + game.getScenarioIdToString() + " game");

        return "redirect:../games";
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
     * Handler that launches the specified game.
     *
     * @param scenarioId The scenario of the game.
     * @param gameId     The id of the game.
     * @return Redirect view for the response.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/game/{gameId}/launch")
    public String launchGame(
            @PathVariable final String scenarioId,
            @PathVariable final String gameId) {


        //Choose scenario database.
        ScenarioContextHolder.setScenario(scenarioId);

        //retrieve data
        final Game game = getGameManager().getByID(Integer.parseInt(gameId));
        final User thisUser = getUser();

        //check for privileges.
        if (game.getUserId() != thisUser.getUserId() && thisUser.getUserType() != 3) {
            return null;
        }

        //check if game has already been launched
        if (!game.isWaitingForPlayers()) {
            return null;
        }

        game.setStatus(GameConstants.GAME_READY);


        // Identify date of next processing
        final Calendar nextTurn = Calendar.getInstance();
        nextTurn.set(Calendar.HOUR_OF_DAY, 0);
        nextTurn.set(Calendar.MINUTE, 0);
        nextTurn.set(Calendar.SECOND, 0);

        //go to next week(almost).
        nextTurn.add(Calendar.DATE, 6);
        if (game.getSchedule() > 0) {
            // Day-based periodic schedule
            nextTurn.add(Calendar.DATE, game.getSchedule());
        } else {
            // Custom schedule
            try {
                final CronExpression cexp = new CronExpression(game.getCronSchedule());
                nextTurn.setTime(cexp.getNextValidTimeAfter(nextTurn.getTime()));
            } catch (Exception ex) {
                LOGGER.error(ex);
                LOGGER.info("Setting next processing date after 7 days.");
                nextTurn.add(Calendar.DATE, 7);
            }
        }
        LOGGER.debug("NEXT PROCESSING DATE ? " + nextTurn.getTime().toString());
        game.setDateNextProc(nextTurn.getTime());
        getGameManager().update(game);

        //finally send the email notifications to the game players
        final List<UserGame> userGames = getUserGameManager().list(game);
        for (UserGame userGame : userGames) {
            if (userGame.getUserId() != 2) { //if not admin
                try {
                    EmailManager.getInstance().sendGameLaunchNotification(game, getUserManager().getByID(userGame.getUserId()));
                } catch (Exception e) {
                    LOGGER.error("Failed to send launch notification email", e);
                }
            }
        }

        LOGGER.info("LAUNCH GAME/User " + getUser().getUsername() + " : " + game.getScenarioIdToString() + "/" + game.getGameId());
        return "redirect:/scenario/" + game.getScenarioIdToString() + "/game/" + game.getGameId() + "/info";

    }


    /**
     * Handler that deletes a specified game.
     *
     * @param scenarioId The scenario of the game.
     * @param gameId     The id of the game.
     * @return Redirect view for the response.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/game/{gameId}/cancel")
    public String cancelGame(
            @PathVariable final String scenarioId,
            @PathVariable final String gameId) {

        ScenarioContextHolder.setScenario(scenarioId);

        //retrieve data
        final Game game = getGameManager().getByID(Integer.parseInt(gameId));
        final User thisUser = getUser();

        //check for privileges.
        if (game.getUserId() != thisUser.getUserId() && thisUser.getUserType() != 3) {
            return null;
        }

        //check if game has already been launched
        if (!game.isWaitingForPlayers()) {
            return null;
        }

        final User gameOwner = getUserManager().getByID(game.getUserId());


        int refund = 200;
        //this is the only identifier we have to locate all the payments done for this player for this game
        final StringBuilder paymentText = new StringBuilder();
        paymentText.append("Setup custom game / scenario ");
        paymentText.append(game.getScenarioIdToString());
        paymentText.append(" / game ");
        paymentText.append(game.getGameId());

        //calculate the free,bought and transfered credits that game owner spend for the creation of this game.
        int freeCredits = 0;
        int boughtCredits = 0;
        int transferredCredits = 0;
        for (PaymentHistory paymentHistory : paymentHistoryManager.list(gameOwner)) {
            if (paymentText.toString().equals(paymentHistory.getComment())) {
                freeCredits -= paymentHistory.getChargeFree();
                boughtCredits -= paymentHistory.getChargeBought();
                transferredCredits -= paymentHistory.getChargeTransferred();
                break;
            }
        }

        refundGameOwner(game, gameOwner, freeCredits, transferredCredits, boughtCredits);
        getUserManager().update(gameOwner);
        //Delete the maps directory for this game.
        final File gameDir = new File("/srv/eaw1805/images/maps/s" + game.getScenarioId() + "/" + game.getGameId());
        if (gameDir.exists()) {
            final boolean result = deleteDir(gameDir);
            if (result) {
                LOGGER.info("Delete Game directory.");
            } else {
                LOGGER.error("Failed to delete Game directory. ");
            }
        }

        //finally send the email notifications to the game players
        final List<UserGame> userGames = getUserGameManager().list(game);
        for (UserGame userGame : userGames) {
            if (userGame.getUserId() != 2) { //if not admin
                try {
                    EmailManager.getInstance().sendGameCancellationNotification(game, getUserManager().getByID(userGame.getUserId()));
                } catch (Exception e) {
                    LOGGER.error("Failed to send cancellation notification email", e);
                }
            }
        }

        //finally delete the actual game and all its entries.
        getGameManager().deleteGame(game.getGameId());

        return "redirect:/games";
    }

    /**
     * Deletes all files and sub directories under dir.
     * If a deletion fails, the method stops attempting to delete and returns false.
     *
     * @param dir the directory to delete.
     * @return true if all deletions were successful.
     */

    private boolean deleteDir(final File dir) {
        if (dir.isDirectory()) {
            for (final String aChildren : dir.list()) {
                if (!deleteDir(new File(dir, aChildren))) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }


    private void refundGameOwner(final Game game, final User gameOwner, final int freeCredits, final int transferredCredits, final int boughtCredits) {
        LOGGER.info("REFUNDING PLAYER " + gameOwner.getUsername() + " for canceling game " + game.getScenarioId() + "/" + game.getGameId() + "/" + freeCredits + "/" + transferredCredits + "/" + boughtCredits);
        //create comment for the payment history
        final StringBuilder refundText = new StringBuilder();
        refundText.append("Cancel custom game / scenario ");
        refundText.append(game.getScenarioIdToString());
        refundText.append(" / game ");
        refundText.append(game.getGameId());
        final User admin = getUserManager().getByID(2);//retrieve admin
        if (freeCredits > 0) {
            // add payment history
            final PaymentHistory trans = new PaymentHistory();
            trans.setUser(gameOwner);
            trans.setAgent(admin);
            trans.setType(PaymentHistory.TYPE_SETUP_REFUND);


            gameOwner.setCreditFree(gameOwner.getCreditFree() + freeCredits);

            // track history
            trans.setCreditFree(gameOwner.getCreditFree());
            trans.setCreditBought(gameOwner.getCreditBought());
            trans.setCreditTransferred(gameOwner.getCreditTransferred());

            // add amount
            trans.setChargeFree(freeCredits);
            trans.setDate(new Date());
            trans.setComment(refundText.toString());
            paymentHistoryManager.add(trans);
            LOGGER.info("ADDED FOR FREE CREDITS");
        }
        if (transferredCredits > 0) {
            // add payment history
            final PaymentHistory trans = new PaymentHistory();
            trans.setUser(gameOwner);
            trans.setAgent(admin);
            trans.setType(PaymentHistory.TYPE_SETUP_REFUND);


            gameOwner.setCreditTransferred(gameOwner.getCreditTransferred() + transferredCredits);

            // track history
            trans.setCreditFree(gameOwner.getCreditFree());
            trans.setCreditBought(gameOwner.getCreditBought());
            trans.setCreditTransferred(gameOwner.getCreditTransferred());

            // add amount
            trans.setChargeTransferred(transferredCredits);
            trans.setDate(new Date());
            trans.setComment(refundText.toString());
            paymentHistoryManager.add(trans);
            LOGGER.info("ADDED FOR TRANSFERED CREDITS");
        }

        if (boughtCredits > 0) {
            // add payment history
            final PaymentHistory trans = new PaymentHistory();
            trans.setUser(gameOwner);
            trans.setAgent(admin);
            trans.setType(PaymentHistory.TYPE_SETUP_REFUND);


            gameOwner.setCreditBought(gameOwner.getCreditBought() + boughtCredits);

            // track history
            trans.setCreditFree(gameOwner.getCreditFree());
            trans.setCreditBought(gameOwner.getCreditBought());
            trans.setCreditTransferred(gameOwner.getCreditTransferred());

            // add amount
            trans.setChargeBought(boughtCredits);
            trans.setDate(new Date());
            trans.setComment(refundText.toString());
            paymentHistoryManager.add(trans);
            LOGGER.info("ADDED FOR BOUGHT CREDITS");
        }
    }


    private transient PaymentHistoryManagerBean paymentHistoryManager;

    public void setPaymentHistoryManager(PaymentHistoryManagerBean paymentHistoryManager) {
        this.paymentHistoryManager = paymentHistoryManager;
    }


    private transient ForumManagerBean forumManager;

    public void setForumManager(ForumManagerBean forumManager) {
        this.forumManager = forumManager;
    }

    UserPermissionManagerBean permissionManager;

    public void setPermissionManager(UserPermissionManagerBean permissionManager) {
        this.permissionManager = permissionManager;
    }
}
