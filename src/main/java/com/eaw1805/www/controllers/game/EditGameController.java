package com.eaw1805.www.controllers.game;

import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.validators.GameEditValidator;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EditGameController
        extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(EditGameController.class);

    protected static final String MODEL_JSTL_KEY = "game";

    @ModelAttribute("game")
    public Game populateCommand(final HttpServletRequest servletRequest) {
        final Map pathVariables = (Map) servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables == null || !pathVariables.containsKey("gameId")) {
            return null;
        }

        ScenarioContextHolder.setScenario(pathVariables.get("scenarioId").toString());

        // Check that user is allowed to edit nation
        final User thisUser = getUser();
        if (thisUser.getUserType() != 3) {
            return null;
        }

        final String gameId = pathVariables.get("gameId").toString();
        if (gameId == null || gameId.isEmpty()) {
            return new Game();
        }

        final Game thisGame = getGameManager().getByID(Integer.parseInt(gameId));

        LOGGER.info("Edit Game (populate) " + gameId + "{" + pathVariables.get("scenarioId") + "}[" + thisGame.getScenarioId() + "/" + thisGame.getScenarioIdToString() + "]");
        return thisGame;
    }


    @ModelAttribute("nations")
    public List<Nation> getGameNations(final HttpServletRequest servletRequest) {
        final Map pathVariables = (Map) servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        ScenarioContextHolder.setScenario(pathVariables.get("scenarioId").toString());
        final List<Nation> nations = getNationManager().list();
        nations.remove(0);
        return nations;
    }

    public void loadReferenceData(final Game game, final ModelMap model) {
        ScenarioContextHolder.setScenario(game.getScenarioId());
        LOGGER.info("Edit Game " + game.getGameId() + "[" + game.getScenarioId() + "/" + game.getScenarioIdToString() + "]");
        final List<Nation> nations = getNationManager().list();
        nations.remove(0);
        model.addAttribute("nations", nations);

        final Map<Integer, UserGame> nationToUser = new HashMap<Integer, UserGame>();
        final Map<Integer, User> nationToUsername = new HashMap<Integer, User>();
        final List<UserGame> userGames = userGameManager.list(game);
        for (UserGame userGame : userGames) {
            nationToUser.put(userGame.getNation().getId(), userGame);
            nationToUsername.put(userGame.getNation().getId(), userManager.getByID(userGame.getUserId()));
        }
        model.addAttribute("nationToUsername", nationToUsername);
        model.addAttribute("nationToUser", nationToUser);
        model.addAttribute("gameId", game.getGameId());
        model.addAttribute("scenarioIdStr", game.getScenarioId());
    }

    @RequestMapping(value = "/scenario/{scenarioId}/game/{gameId}/edit", method = RequestMethod.GET)
    public String setupForm(@PathVariable("scenarioId") String scenarioId, @ModelAttribute(MODEL_JSTL_KEY) Game game,
                            ModelMap model) {

        ScenarioContextHolder.setScenario(scenarioId);
        try {
            loadReferenceData(game, model);

        } catch (Exception e) {
            LOGGER.error(e, e);
        }
        return "game/edit";
    }

    @RequestMapping(value = "/scenario/{scenarioId}/game/{gameId}/edit", method = RequestMethod.POST)
    public ModelAndView processGameEdit(@PathVariable("scenarioId") String scenarioId,
                                        @ModelAttribute(MODEL_JSTL_KEY) Game game, BindingResult result, SessionStatus status,
                                        ModelMap model) {
        ScenarioContextHolder.setScenario(scenarioId);
        LOGGER.info("Edit Game (save) " + game.getGameId() + "[" + game.getScenarioId() + "/" + game.getScenarioIdToString() + "]");
        GameEditValidator gameEditValidator = new GameEditValidator();
        gameEditValidator.validate(game, result);

        if (result.hasErrors()) {
            loadReferenceData(game, model);
            return new ModelAndView("game/edit");
        }

        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        final String endedStr = request.getParameter("ended");
        if (endedStr != null) {
            game.setEnded(true);
        } else {
            game.setEnded(false);
        }

        // reload record
        final Game updatedRecord = getGameManager().getByID(game.getGameId());
        updatedRecord.setEnded(game.getEnded());
        updatedRecord.setDateNextProc(game.getDateNextProc());
        updatedRecord.setDiscount(game.getDiscount());
        updatedRecord.setSchedule(game.getSchedule());

        //finally save user to database
        getGameManager().update(updatedRecord);

        final RedirectView redirectView = new RedirectView(request.getContextPath() + "/scenario/" + updatedRecord.getScenarioIdToString() + "/game/" + game.getGameId() + "/info");
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, getCustomDateEditor());
    }

    /**
     * Custom editor injected with spring to manage class Date.
     */
    private CustomDateEditor customDateEditor;

    /**
     * Setter method used by spring to inject a customDateEditor bean
     * for the property customDateEditor.
     *
     * @param injCustDateEdit spring bean customDateEditor.
     */
    public void setCustomDateEditor(final CustomDateEditor injCustDateEdit) {
        this.customDateEditor = injCustDateEdit;
    }

    /**
     * Getter method for the property customDateEditor.
     *
     * @return the customDateEditor property.
     */
    public CustomDateEditor getCustomDateEditor() {
        return customDateEditor;
    }
}
