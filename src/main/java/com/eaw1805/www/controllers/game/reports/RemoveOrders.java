package com.eaw1805.www.controllers.game.reports;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.NavigationConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.managers.beans.NewsManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.News;
import com.eaw1805.data.model.User;
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
import java.util.List;

/**
 * Removes orders for a given nation of a particular game.
 */
@SuppressWarnings("restriction")
@Controller
public class RemoveOrders
        extends ExtendedController
        implements OrderConstants, ArmyConstants, RelationConstants, NavigationConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(RemoveOrders.class);

    @RequestMapping(method = RequestMethod.GET, value = "/report/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/orders/remove/{newsStr}")
    protected ModelAndView handle(@PathVariable final String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  @PathVariable final String newsStr,
                                  HttpServletRequest request)
            throws Exception {

        ScenarioContextHolder.defaultScenario();
        if (scenarioId == null || scenarioId.isEmpty()) {
            throw new InvalidPageException("Page not found");
        } else {
            ScenarioContextHolder.setScenario(scenarioId);

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

        // Retrieve news entry
        int newsInt = 0;
        if (newsStr != null) {
            try {
                newsInt = Integer.parseInt(newsStr);
            } catch (Exception ex) {
                // rejected
                throw new InvalidPageException("Page not found");
            }
        }

        // retrieve news entry
        final News entry = newsManager.getByID(newsInt);
        if ((entry.getSubject().getId() == thisNation.getId() && entry.getNation().getId() == -1)
                || (thisUser.getUserType() == 3)) {
            // delete all related orders
            final List<News> lstNews = newsManager.listBase(thisGame, newsInt);
            for (final News news : lstNews) {
                newsManager.delete(news);
            }

            // delete parent entry
            newsManager.delete(entry);
        }

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Delete news entry=" + newsInt + " for game=" + gameId + "/nation=" + thisNation.getName());
        final RedirectView redirectView = new RedirectView(request.getContextPath() + "/report/scenario/" + thisGame.getScenarioIdToString() + "/game/" + gameId + "/nation/" + nationId + "/orders/" + thisGame.getTurn());
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
    }

    /**
     * Instance NewsManager class to perform queries
     * about news objects.
     */
    private transient NewsManagerBean newsManager;

    /**
     * Setter method used by spring to inject a newsManager bean.
     *
     * @param injNewsManager a newsManager bean.
     */
    public void setNewsManager(final NewsManagerBean injNewsManager) {
        newsManager = injNewsManager;
    }

}
