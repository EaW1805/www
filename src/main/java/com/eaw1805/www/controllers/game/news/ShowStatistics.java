package com.eaw1805.www.controllers.game.news;

import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.User;
import com.eaw1805.www.commands.NationCommand;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Display the statistics for a given nation of a particular game.
 */
public class ShowStatistics
        extends ShowNewsletter {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowNewsletter.class);

    protected ModelAndView handle(final HttpServletRequest request,
                                  final HttpServletResponse response, final Object commandObj, final BindException errors)
            throws Exception {

        final NationCommand command = (NationCommand) commandObj;

        // Retrieve Game entity
        final String gameId = command.getGameId();
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
        final String nationId = command.getNationId();
        Nation thisNation;
        if ((nationId == null) || (nationId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisNation = getNationManager().getByID(Integer.parseInt(nationId));
            if (thisNation == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Retrieve Turn
        final String turnStr = command.getTurn();
        int turnInt = thisGame.getTurn();
        if (turnStr != null) {
            try {
                turnInt = Integer.parseInt(turnStr);
                if (turnInt > thisGame.getTurn()) {
                    turnInt = thisGame.getTurn();
                }
            } catch (Exception ex) {
                // rejected
            }
        }

        // retrieve user
        final User thisUser = getUser();
        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show Nation for game=" + thisGame.getGameId() + "/nation=" + thisNation.getName() + "/turn=" + turnInt);

        // Check that user is allowed to view nation
        if (thisUser.getUserType() != 3 && getUserGameManager().listActive(thisUser, thisGame, thisNation).isEmpty()) {
            throw new InvalidPageException("Page not found");
        }

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // Fill in data to pass to jsp
        refData.put("game", thisGame);
        refData.put("gameId", thisGame.getGameId());
        refData.put("nation", thisNation);
        refData.put("nationId", thisNation.getId());
        refData.put("turn", turnInt);

        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));

        if (request.getParameter("eawClient") == null) {
            return new ModelAndView("game/statistics", refData);
        } else {
            return new ModelAndView("game/statisticsMinimal", refData);
        }

    }

}
