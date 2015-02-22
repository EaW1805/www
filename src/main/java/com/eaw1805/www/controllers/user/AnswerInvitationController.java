package com.eaw1805.www.controllers.user;

import com.eaw1805.core.EmailManager;
import com.eaw1805.data.constants.ForumRoleConstants;
import com.eaw1805.data.managers.beans.GameManagerBean;
import com.eaw1805.data.managers.beans.NationManagerBean;
import com.eaw1805.data.managers.beans.UserPermissionManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.data.model.forum.UserPermissions;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
public class AnswerInvitationController extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(AnswerInvitationController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/invitation/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/accept")
    public String processAcceptInvitation(@PathVariable("scenarioId") String scenarioId,
                                          @PathVariable final String gameId,
                                          @PathVariable final String nationId,
                                          final HttpServletRequest request,
                                          final HttpServletResponse response) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game game = gameManager.getByID(Integer.parseInt(gameId));
        final Nation nation = nationManmager.getByID(Integer.parseInt(nationId));

        final List<UserGame> userGames = userGameManager.list(game, nation);
        final User thisUser = getUser();
        PrintWriter writer = null;
        try {
            writer = response.getWriter();

        } catch (IOException e) {
            LOGGER.error("Unable to acquire PDFwriter", e);
        }

        boolean found = false;
        for (UserGame userGame : userGames) {
            if (userGame.getUserId() == thisUser.getUserId()) {
                userGame.setAccepted(true);
                userGame.setActive(true);
                userGameManager.update(userGame);
                found = true;

                //Update Forum Permissions.
                updatePermissions(thisUser, userGame.getGame().getForumId(), ForumRoleConstants.ROLE_FORUM_POLLS);
                break;
            }
        }

        if (found) {
            writer.write("a,1");
            LOGGER.info("INVITATION ACCEPTED/" + thisUser.getUsername() + "/" + scenarioId + "/" + gameId + "/" + nationId);

        } else {
            writer.write("a,0");
            LOGGER.info("INVITATION ACCEPTED NOT FOUND/" + thisUser.getUsername() + "/" + scenarioId + "/" + gameId + "/" + nationId);
        }

        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/invitation/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/reject")
    public String processRejectInvitation(@PathVariable("scenarioId") String scenarioId,
                                          @PathVariable final String gameId,
                                          @PathVariable final String nationId,
                                          final HttpServletRequest request,
                                          final HttpServletResponse response) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game game = gameManager.getByID(Integer.parseInt(gameId));
        final Nation nation = nationManmager.getByID(Integer.parseInt(nationId));

        final List<UserGame> userGames = userGameManager.list(game, nation);
        final User thisUser = getUser();
        PrintWriter writer = null;
        try {
            writer = response.getWriter();

        } catch (IOException e) {
            LOGGER.error("Unable to acquire PDFwriter", e);
        }

        boolean found = false;
        for (UserGame userGame : userGames) {
            if (userGame.getUserId() == thisUser.getUserId()) {
                userGame.setAccepted(true);
                userGame.setUserId(2);
                userGame.setActive(false);
                userGameManager.update(userGame);
                found = true;

                //if it is not the owner of the game.
                if (thisUser.getUserId() != game.getUserId()) {
                    updatePermissions(thisUser, userGame.getGame().getForumId(), ForumRoleConstants.ROLE_FORUM_READONLY);
                }
                break;
            }
        }

        if (found) {
            writer.write("r,1");
            LOGGER.info("INVITATION REJECTED/" + thisUser.getUsername() + "/" + scenarioId + "/" + gameId + "/" + nationId);

        } else {
            writer.write("r,0");
            LOGGER.info("INVITATION REJECTED NOT FOUND/" + thisUser.getUsername() + "/" + scenarioId + "/" + gameId + "/" + nationId);
        }

        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/invitation/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/user/{username}/send")
    public String processInvitePlayer(@PathVariable("scenarioId") String scenarioId,
                                      @PathVariable final String gameId,
                                      @PathVariable final String nationId,
                                      @PathVariable final String username,
                                      final HttpServletRequest request,
                                      final HttpServletResponse response) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game game = gameManager.getByID(Integer.parseInt(gameId));
        final Nation nation = nationManmager.getByID(Integer.parseInt(nationId));

        final List<UserGame> userGames = userGameManager.list(game, nation);
        final User thisUser = getUser();
        if (game.getUserId() != thisUser.getUserId() && thisUser.getUserType() != 3) {
            return null;
        }

        final User toInvite = userManager.getByUserName(username);
        if (toInvite == null) {
            return null;
        }

        PrintWriter writer = null;
        try {
            writer = response.getWriter();

        } catch (IOException e) {
            LOGGER.error("Unable to acquire PDFwriter", e);
        }

        //first check if user plays an other nation in this game
        boolean found = false;
        final List<UserGame> otherGames = userGameManager.list(toInvite, game);
        if (!otherGames.isEmpty()) {
            if (otherGames.get(0).isAccepted()) {
                writer.write("e,4");

            } else {
                writer.write("e,3");
            }

            writer.flush();
            return null;
        }

        final List<UserGame> otherNationGames = userGameManager.listPlayedUserGames(toInvite, game, nation);
        if (!otherNationGames.isEmpty()) {
            writer.write("e,5");
            writer.flush();
            return null;
        }

        for (UserGame userGame : userGames) {
            if (userGame.getUserId() != 2 && userGame.isAccepted()) {
                //you cannot invite someone for a position that a player already accepted.
                writer.write("e,1");
                writer.flush();
                return null;
            }

            final User oldUser = userManager.getByID(userGame.getUserId());
            userGame.setUserId(toInvite.getUserId());
            userGame.setAccepted(false);
            userGame.setActive(toInvite.getUserId() != 2);
            userGameManager.update(userGame);
            if (oldUser.getUserId() != 2) {
                EmailManager.getInstance().sendInvitationCancellationNotification(oldUser, userManager.getByID(game.getUserId()), userGame);
            }
            EmailManager.getInstance().sendInvitationNotification(toInvite, userManager.getByID(game.getUserId()), userGame);
            found = true;
            break;
        }

        if (found) {
            writer.write("i,0");
            LOGGER.info("INVITATION SENT/" + toInvite.getUsername() + "/" + scenarioId + "/" + gameId + "/" + nationId);

        } else {
            writer.write("e,2");
            LOGGER.info("INVITATION SENT NOT FOUND/" + toInvite.getUsername() + "/" + scenarioId + "/" + gameId + "/" + nationId);
        }

        writer.flush();
        return null;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/invitation/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/cancel")
    public String processCancelInvitation(@PathVariable("scenarioId") String scenarioId,
                                          @PathVariable final String gameId,
                                          @PathVariable final String nationId,
                                          final HttpServletRequest request,
                                          final HttpServletResponse response) {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game game = gameManager.getByID(Integer.parseInt(gameId));
        final Nation nation = nationManmager.getByID(Integer.parseInt(nationId));

        final List<UserGame> userGames = userGameManager.list(game, nation);
        final User thisUser = getUser();
        if (game.getUserId() != thisUser.getUserId() && thisUser.getUserType() != 3) {
            return null;
        }

        PrintWriter writer = null;
        try {
            writer = response.getWriter();

        } catch (IOException e) {
            LOGGER.error("Unable to acquire PDFwriter", e);
        }

        boolean found = false;
        for (UserGame userGame : userGames) {
            if (userGame.getUserId() != 2 && userGame.isAccepted()) {
                //you cannot cancel an invitation that a player already accepted.
                writer.write("e,1");
                writer.flush();
                return null;
            }
            User oldUser = userManager.getByID(userGame.getUserId());
            userGame.setUserId(2);
            userGame.setAccepted(true);
            userGame.setActive(false);
            userGameManager.update(userGame);
            if (oldUser.getUserId() != 2) {
                //send notification to the user.
                EmailManager.getInstance().sendInvitationCancellationNotification(oldUser, userManager.getByID(game.getUserId()), userGame);
            }

            found = true;
        }

        if (found) {
            writer.write("i,1");
            LOGGER.info("INVITATION CANCELED/" + thisUser.getUsername() + "/" + scenarioId + "/" + gameId + "/" + nationId);

        } else {
            writer.write("i,0");
            LOGGER.info("INVITATION CANCELED NOT FOUND/" + thisUser.getUsername() + "/" + scenarioId + "/" + gameId + "/" + nationId);
        }
        writer.flush();
        return null;
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


    private transient GameManagerBean gameManager;
    private transient NationManagerBean nationManmager;

    public void setGameManager(GameManagerBean gameManager) {
        this.gameManager = gameManager;
    }

    public void setNationManmager(NationManagerBean nationManmager) {
        this.nationManmager = nationManmager;
    }

    private transient UserPermissionManagerBean permissionManager;

    public void setPermissionManager(UserPermissionManagerBean permissionManager) {
        this.permissionManager = permissionManager;
    }
}
