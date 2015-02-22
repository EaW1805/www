package com.eaw1805.www.controllers.game;

import com.eaw1805.data.model.Game;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


@Controller
public class Processing extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(Play.class);

    @RequestMapping(method = RequestMethod.GET, value = "/processing/scenario/{scenarioId}/game/{gameId}/turn/{turnId}")
    protected ModelAndView handle(@PathVariable final String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String turnId) throws Exception {
        Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("scenarioId", scenarioId);
        refData.put("gameId", gameId);
        refData.put("turnId", turnId);
        return new ModelAndView("game/processing", refData);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/processing/scenario/{scenarioId}/game/{gameId}/turn/{turnId}/check")
    protected ModelAndView handleCheck(@PathVariable final String scenarioId,
                                       @PathVariable final String gameId,
                                       @PathVariable final String turnId,
                                       final HttpServletResponse response) throws Exception {
        ScenarioContextHolder.setScenario(scenarioId);
        PrintWriter writer = response.getWriter();

        final Game game = getGameManager().getByID(Integer.parseInt(gameId));
        if (game.getTurn() > Integer.parseInt(turnId)) {
            writer.write("1");
        } else {
            writer.write("0");
        }

        writer.flush();
        writer.close();
        return null;
    }

}
