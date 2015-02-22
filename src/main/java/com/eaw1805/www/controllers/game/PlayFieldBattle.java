package com.eaw1805.www.controllers.game;

import com.eaw1805.data.managers.beans.FieldBattleReportManagerBean;
import com.eaw1805.data.managers.beans.NationManagerBean;
import com.eaw1805.data.managers.beans.SectorManagerBean;
import com.eaw1805.data.managers.beans.UserFieldBattleManagerBean;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.battles.FieldBattleReport;
import com.eaw1805.data.model.battles.field.UserFieldBattle;
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
import java.util.Map;

@Controller
public class PlayFieldBattle extends ExtendedController {

    private static final Logger LOGGER = LogManager.getLogger(PlayFieldBattle.class);

    @RequestMapping(method = RequestMethod.GET, value = "/play/scenario/{scenarioId}/fieldbattle/{battleId}/nation/{nationId}")
    protected ModelAndView handle(@PathVariable("scenarioId") String scenarioId,
                                  @PathVariable("battleId") String battleId,
                                  @PathVariable("nationId") String nationId,
                                  HttpServletRequest request) throws Exception {
        // Check that user is allowed to view nation
        try {
        final User thisUser = getUser();

        // Determine scenario
        if ((scenarioId == null) || (scenarioId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            // Set the scenario
            ScenarioContextHolder.setScenario(scenarioId);
        }

        int thisBattleId = Integer.parseInt(battleId);
        final FieldBattleReport battle = fieldBattleManager.getByID(thisBattleId);
        int side = 1;
        int thisNationId = Integer.parseInt(nationId);
        for (Nation nation : battle.getSide2()) {
            if (thisNationId == nation.getId()) {
                side = 2;
                break;
            }
        }

        UserFieldBattle userField = userFieldManager.getByBattleNation(thisBattleId, thisNationId);
        if (userField == null) {
            userField = new UserFieldBattle();
            userField.setBattleId(thisBattleId);
            userField.setNationId(thisNationId);
            userField.setUserId(getUser().getUserId());
            userField.setReady(false);
            userFieldManager.add(userField);
        }

        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("battleId", battleId);
        refData.put("scenarioId", scenarioId);
        refData.put("nationId", nationId);
        refData.put("round", battle.getRound());
        refData.put("side", side);
        refData.put("gameEnded", battle.getWinner() != -1 || battle.getRound() == 23);
        refData.put("winner", battle.getWinner());
        refData.put("standAlone", 0);
        if (userField.isReady()) {
            refData.put("sideReady", 1);
        } else {
            refData.put("sideReady", 0);
        }

        refData.put("title", "Field battle at " + sectorManager.getByPosition(battle.getPosition()).getPosition().toString());

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Play Field Battle id=" + battleId + "/side=" + side + "/nation=" + nationId);
        return new ModelAndView("game/playFieldBattle", refData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private transient FieldBattleReportManagerBean fieldBattleManager;

    private transient NationManagerBean nationManager;

    public void setFieldBattleManager(FieldBattleReportManagerBean fieldBattleManager) {
        this.fieldBattleManager = fieldBattleManager;
    }

    public void setNationManager(NationManagerBean nationManager) {
        this.nationManager = nationManager;
    }

    private transient SectorManagerBean sectorManager;

    public void setSectorManager(SectorManagerBean sectorManager) {
        this.sectorManager = sectorManager;
    }

    private transient UserFieldBattleManagerBean userFieldManager;

    public void setUserFieldManager(UserFieldBattleManagerBean userFieldManager) {
        this.userFieldManager = userFieldManager;
    }
}
