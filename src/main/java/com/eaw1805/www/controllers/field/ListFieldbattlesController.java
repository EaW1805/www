package com.eaw1805.www.controllers.field;

import com.eaw1805.data.managers.beans.FieldBattleMapManagerBean;
import com.eaw1805.data.managers.beans.FieldBattleReportManagerBean;
import com.eaw1805.data.managers.beans.SectorManagerBean;
import com.eaw1805.data.model.battles.FieldBattleReport;
import com.eaw1805.data.model.map.Sector;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ListFieldbattlesController extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(CreateFieldbattleController.class);

    @ModelAttribute("battles")
    public List<FieldBattleReport> getBattles() {
        ScenarioContextHolder.defaultScenario();
        return fieldBattleManager.list();
    }

    @ModelAttribute("battleToSector")
    public Map<Integer, Sector> getSectorBattles() {
        ScenarioContextHolder.defaultScenario();
        Map<Integer, Sector> out = new HashMap<Integer, Sector>();
        final List<FieldBattleReport> battles = getBattles();
        for (FieldBattleReport battle : battles) {

            out.put(battle.getBattleId(), sectorManager.getByPosition(battle.getPosition()));
        }
        return out;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/fieldbattle/list")
    public String listFields(final ModelMap model) {

        return "fieldbattle/list";
    }

    FieldBattleReportManagerBean fieldBattleManager;

    public void setFieldBattleManager(FieldBattleReportManagerBean fieldBattleManager) {
        this.fieldBattleManager = fieldBattleManager;
    }

    FieldBattleMapManagerBean fieldMapManager;

    public void setFieldMapManager(FieldBattleMapManagerBean fieldMapManager) {
        this.fieldMapManager = fieldMapManager;
    }

    SectorManagerBean sectorManager;

    public void setSectorManager(SectorManagerBean sectorManager) {
        this.sectorManager = sectorManager;
    }
}
