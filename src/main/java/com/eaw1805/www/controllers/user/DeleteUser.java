package com.eaw1805.www.controllers.user;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.managers.beans.*;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.List;


@Controller
public class DeleteUser extends BaseController {

    @RequestMapping(method = RequestMethod.GET, value = "/user/{username}/delete")
    public String deleteUser(@PathVariable("username") String username)
            throws Exception {
        try {
            ScenarioContextHolder.defaultScenario();

            //if you are not admin, get the heck out of here
            if (getUser().getUserType() != 3) {
                throw new InvalidPageException("Page not found");
            }
            final User toDelete = userManager.getByUserName(username);
            //first delete achievements.
            LOGGER.info(" -- *Deleting user *");
            deleteSoloGame(toDelete);
            LOGGER.info(" -- Solo games cleaned.");
            fixUserGames(toDelete);
            LOGGER.info(" -- User games fixed.");
            followManager.delete(toDelete);
            LOGGER.info(" -- Follows deleted.");
            userManager.markDeleted(toDelete);
            LOGGER.info(" -- *User deletion completed successfully*");

        } catch (Exception e) {
            LOGGER.error("Delete user", e);
        }

        //redirect back to profile
        return "redirect:/games";
    }

    /**
     * Delete tutorial game for the user.
     *
     * @param toDelete The user to delete the tutorial for.
     */
    public void deleteSoloGame(final User toDelete) {
        ScenarioContextHolder.setScenario(HibernateUtil.DB_FREE);

        // Check if user has an ongoing game (only 1 is allowed)
        final List<UserGame> lstGames = getUserGameManager().list(toDelete);
        LOGGER.info(lstGames.size());
        if (!lstGames.isEmpty()) {
            // check if game has started
            final UserGame userGame = lstGames.get(0);
            LOGGER.info(userGame.getGame().getGameId() + " - " + userGame.getGame().getTurn());
            if (userGame.getGame().getTurn() > -1) {
                // Delete Game
                gameManager.deleteGame(userGame.getGame().getGameId());

                // Create the directory of this game
                final File gameDir = new File("/srv/eaw1805/images/maps/s" + HibernateUtil.DB_FREE + "/" + userGame.getGame().getGameId());
                if (gameDir.exists()) {
                    final boolean result = deleteDir(gameDir);
                    if (result) {
                        LOGGER.info("Delete Game directory.");
                    } else {
                        LOGGER.error("Failed to delete Game directory. ");
                    }
                }

            }
        }
    }

    /**
     * Reset all users usergames and set them back to admin.
     *
     * @param toDelete
     */
    public void fixUserGames(final User toDelete) {
        for (int scenario = HibernateUtil.DB_FIRST; scenario <= HibernateUtil.DB_LAST; scenario++) {
            ScenarioContextHolder.setScenario(scenario);
            final List<UserGame> userGames = getUserGameManager().listAll(toDelete);
            for (final UserGame userGame : userGames) {
                if (userGame.isActive() && !userGame.getGame().getEnded()) {
                    if (userGame.getTurnPickUp() == userGame.getGame().getTurn()) {
                        //we don't actually delete these, we just set them back to admin
                        userGame.setUserId(2);
                        userGame.setAccepted(true);
                        userGame.setTurnPickUp(0);
                        userGame.setTurnDrop(0);
                        userGame.setCurrent(true);
                        userGame.setTurnFirstLoad(true);
                        userGameManager.update(userGame);
                    } else {
                        //stcd: Same Turn, Cannot Drop Your Position
                        userGame.setActive(false);
                        userGame.setCurrent(false);
                        userGame.setTurnDrop(userGame.getGame().getTurn());
                        userGameManager.update(userGame);

                        userGame.setUserId(2);
                        userGame.setAccepted(true);
                        userGame.setTurnPickUp(0);
                        userGame.setTurnDrop(0);
                        userGame.setCurrent(true);
                        userGame.setTurnFirstLoad(true);
                        userGameManager.add(userGame);

                    }
                }
            }

        }
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


    @Autowired
    @Qualifier("gameManagerBean")
    private transient GameManagerBean gameManager;

    @Autowired
    @Qualifier("followManagerBean")
    private transient FollowManagerBean followManager;

    @Autowired
    @Qualifier("watchGameManagerBean")
    private transient WatchGameManagerBean watchGameManager;


}
