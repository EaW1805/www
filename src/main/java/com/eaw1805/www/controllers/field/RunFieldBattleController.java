package com.eaw1805.www.controllers.field;

import com.eaw1805.data.managers.beans.FieldBattleReportManagerBean;
import com.eaw1805.data.model.battles.FieldBattleReport;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Controller
public class RunFieldBattleController extends ExtendedController {

    protected static final Logger LOGGER = LogManager.getLogger(RunFieldBattleController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/fieldbattle/{battleId}/run")
    public ModelAndView listFields(@PathVariable("battleId") String battleId,
                                   HttpServletRequest request,
                                   final ModelMap model) {
        try {
            ScenarioContextHolder.defaultScenario();
            int id = Integer.parseInt(battleId);
            FieldBattleReport battle = fieldBattleManager.getByID(id);
            if (battle.getWinner() != -1) {
                LOGGER.debug("Cannot schedule battle " + battleId + ", battle has ended.");
                return new ModelAndView(new RedirectView(request.getContextPath() + "/fieldbattle/list"));
            }

            if ("Being processed".equalsIgnoreCase(battle.getStatus())) {
                LOGGER.debug("Cannot schedule battle " + battleId + ", already in process.");
                return new ModelAndView(new RedirectView(request.getContextPath() + "/fieldbattle/list"));
            }
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DATE, -1);
            battle.setNextProcessDate(now.getTime());
            fieldBattleManager.update(battle);
            //also schedule it to be processed

            // Invoke engine
            LOGGER.debug("Scheduling battle " + id + " for process.");
            getArticleManager().getBuild(-100, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView(new RedirectView(request.getContextPath() + "/fieldbattle/list"));
    }

    FieldBattleReportManagerBean fieldBattleManager;

    public void setFieldBattleManager(FieldBattleReportManagerBean fieldBattleManagerBean) {
        this.fieldBattleManager = fieldBattleManagerBean;
    }


}
