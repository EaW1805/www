package com.eaw1805.www.controllers.game;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.model.Game;
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ScenarioCreator extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ScenarioCreator.class);

    /**
     * This handler is called to create a new Custom Scenario
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "/create/scenario")
    protected String handle(HttpServletRequest request) throws Exception {
        ScenarioContextHolder.setScenario(HibernateUtil.DB_EDITOR);

        //if not a valid user.
        if (getUser() == null || getUser().getUserId() <= 0) {
            throw new InvalidPageException("Access denied");
        }
        //here we create a new scenario game.
        final Game game = new Game();
        game.setName("New Scenario");
        game.setCronSchedule("");
        game.setCronScheduleDescr("");
        game.setUserId(getUser().getUserId());
        getGameManager().add(game);

        //we have to do that since game won't be updated with the game id from hibernate.
        System.out.println("just created? " + getGameManager().getLastGame().get(0).getGameId());
        return "redirect:/create/scenario/" + getGameManager().getLastGame().get(0).getGameId();
    }

    /**
     * This handler is called to start the Custom Scenario editor.
     *
     * @param identifier
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "/create/scenario/{identifier}")
    protected ModelAndView handle(@PathVariable("identifier") String identifier,
                                  HttpServletRequest request) throws Exception {
        //this is the editor db.
        ScenarioContextHolder.setScenario(HibernateUtil.DB_EDITOR);
        Game game;
        if (identifier == null || identifier.isEmpty()) {//then it is a new Game
            game = new Game();
        } else {
            game = getGameManager().getByID(Integer.parseInt(identifier));
        }

        // Check that user is allowed to view nation
        final User thisUser = getUser();
        //if you are not admin and you are not the owner, then access denied
        if (thisUser.getUserType() != 3 && (game.getUserId() != thisUser.getUserId())) {
            throw new InvalidPageException("Access denied");
        }

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("game", game);

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] / scenario creator");
        return new ModelAndView("game/creator", refData);
    }


    /**
     * This handler is called to start the Custom Scenario editor.
     *
     * @param identifier
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "/scenario/editor/list")
    protected ModelAndView scenariosList(HttpServletRequest request) throws Exception {
        //this is the editor db.
        ScenarioContextHolder.setScenario(HibernateUtil.DB_EDITOR);

        // Check that user is allowed to view nation
        final User thisUser = getUser();

        //first list all users scenarios
        final List<Game> usersScenarios = getGameManager().listByOwner(thisUser.getUserId());

        //then list all scenarios user doesn't own
        final List<Game> notUsersScenarios = getGameManager().listByNotOwner(thisUser.getUserId());

        // prepare info to be passed to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("usersScenarios", usersScenarios);
        refData.put("notUsersScenarios", notUsersScenarios);

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] / scenario creator");
        return new ModelAndView("scenario/editor/list", refData);
    }


}
