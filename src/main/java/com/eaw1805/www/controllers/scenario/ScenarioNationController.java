package com.eaw1805.www.controllers.scenario;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.managers.beans.*;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.army.ArmyType;
import com.eaw1805.data.model.army.Battalion;
import com.eaw1805.data.model.army.Brigade;
import com.eaw1805.data.model.army.CommanderName;
import com.eaw1805.data.model.army.comparators.ArmyTypeOrder;
import com.eaw1805.data.model.fleet.Ship;
import com.eaw1805.data.model.fleet.ShipType;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to display scenario nation info.
 */
@Controller
public class ScenarioNationController
        extends ExtendedController
        implements ReportConstants, RegionConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ScenarioNationController.class);

    /**
     * The articles of the 17 records (from the static site).
     */
    private static final int[] ARTICLES =
            {24, 25, 26, 27,
                    28, 29, 30, 31, 32,
                    33, 34, 35, 36, 37, 38,
                    39, 40};

    /**
     * The articles of the 17 records (from the static site).
     */
    private static final int[] COMMANDERS =
            {166, 167, 168, 169,
                    170, 171, 172, 173, 174,
                    175, 176, 177, 178, 179, 180,
                    181, 182};

    private static final String[] DESCRIPTIONS =
            {"Agricultural - People in agricultural lands are particularly adept in the ways of farming and herding, and they can achieve better crop yields than the farmers in the more 'advanced' societies. These empires receive a 20% bonus on their food and wool production in Europe.",
                    "Militaristic - These nations have a national inclination towards warfare and militaristic discipline. Their superior General Staff provides an extra bonus on the Tactical Rating of their Supreme Commander, and their troops tend to gain 2% more experience after victorious land battles.",
                    "Industrius - Few countries have entered the industrial era yet, but some societies are already more advanced than others in the ways of the machines, resulting in a 10% bonus in factory production in Europe, as long as the materials are available in the regional warehouse.",
                    "Merchantile - Generations of merchants have turned some nations into masters in trading and haggling. These nations receive a 5% price bonus at every sale/purchase they make at a trade city, anywhere in the world.",
                    "Maritime - Sailors of maritime nations are the best in the world, and their ships have travelled further than ships of other nations. Their marines tend to gain 2% more experience after victorious naval battles."};

    /**
     * The traits of the 17 records (from the static site).
     */
    private static final String[] TRAITS =
            {DESCRIPTIONS[0], // Austria
                    DESCRIPTIONS[1], // Rhine
                    DESCRIPTIONS[2], // Denmark
                    DESCRIPTIONS[0], // Spain
                    DESCRIPTIONS[1], // France
                    DESCRIPTIONS[4], // England
                    DESCRIPTIONS[3], // Holland
                    DESCRIPTIONS[0], // Italy
                    DESCRIPTIONS[4], // Portugal
                    DESCRIPTIONS[0], // Morocco
                    DESCRIPTIONS[0], // Naples
                    DESCRIPTIONS[1], // Prussia
                    DESCRIPTIONS[0], // Russia
                    DESCRIPTIONS[2], // Sweden
                    DESCRIPTIONS[0], // Ottoman
                    DESCRIPTIONS[0], // Warsaw
                    DESCRIPTIONS[3]}; // Egypt

    /**
     * The traits of the 17 records (from the static site).
     */
    private static final String[] RELATIONS_S1 =
            {"Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all",
                    "Trade to all"};

    /**
     * The traits of the 17 records (from the static site).
     */
    private static final String[] RELATIONS_S2 =
            {"Alliance with Italy<BR>Right of Passage with Russia<BR>Neutral to France<BR>Trade to all other",
                    "Alliance with France<BR>Right of Passage with Holland<BR>Trade to all other",
                    "Alliance with Prussia<BR>Right of Passage with Sweden<BR>Trade to all other",
                    "Alliance with Naples<BR>Neutral to Great Britain<BR>Trade to all other",
                    "Alliance with Confederation of Rhine, Holland<BR>Neutral to Austria-Hungary, Prussia<BR>War with Great Britain<BR>Trade to all other",
                    "Alliance with Kingdom of Portugal, Egypt<BR>Neutral with Spain<BR>War with France<BR>Trade to all other",
                    "Alliance with France<BR>Right of Passage with Confederation of Rhine<BR>Trade to all other",
                    "Alliance with Austria-Hungary<BR>Right of Passage with Naples<BR>Trade to all other",
                    "Alliance with Great Britain<BR>Trade to all other",
                    "Alliance with Sweden<BR>Trade to all other",
                    "Alliance with Spain<BR>Right of Passage with Italy<BR>Trade to all other",
                    "Alliance with Denmark<BR>Neutral to France<BR>Trade to all other",
                    "Right of Passage with Austria-Hungary<BR>Neutral with Sweden<BR>Trade to all other",
                    "Alliance with Morocco<BR>Right of Passage with Denmark<BR>Neutral to Russia<BR>Trade to all other",
                    "Alliance with Duchy of Warsaw<BR>Neutral to Egypt<BR>Trade to all other",
                    "Alliance with Ottoman Empire<BR>Trade to all other",
                    "Alliance with Great Britain<BR>Neutral to Ottoman Empire<BR>Trade to all other"};

    /**
     * The traits of the 17 records (from the static site).
     */
    private static final String[] RELATIONS_S3 =
            {"",
                    "",
                    "",
                    "",
                    "Alliance with Great Britain<BR>War with France",
                    "War with Spain, Great Britain",
                    "Alliance with Spain<BR>War with France",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""};

    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/nation/{nationId}")
    protected ModelAndView handle(@PathVariable("scenarioId") String scenarioId,
                                  final @PathVariable("nationId") String nationId,
                                  final HttpServletRequest request)
            throws Exception {
        ScenarioContextHolder.setScenario(scenarioId);

        // Retrieve Nation entity
        final Nation thisNation;
        if ((nationId == null) || (nationId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisNation = getNationManager().getByID(Integer.parseInt(nationId));
            if (thisNation == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Retrieve list of nations
        final List<Nation> nationList = getScenarioNationList(scenarioId);

        // Retrieve army list
        final List<ArmyType> armyTypeList = armyTypeManager.list(thisNation);
        java.util.Collections.sort(armyTypeList, new ArmyTypeOrder());

        // Retrieve ship list
        final List<ShipType> shipTypeList = new ArrayList<ShipType>();

        // Remove special ship types
        for (final ShipType shipTPE : shipTypeManager.list()) {
            // Make sure that Indiamen are only available to Great Britain and Holland
            if ((shipTPE.getIntId() == 31)
                    && (thisNation.getId() != NationConstants.NATION_GREATBRITAIN)
                    && (thisNation.getId() != NationConstants.NATION_HOLLAND)) {

                continue;
            }

            // Make sure that corsairs and dhows are only available to Morocco, Egypt and Ottomans
            if (((shipTPE.getIntId() == 11)
                    || (shipTPE.getIntId() == 12)
                    || (shipTPE.getIntId() == 24)
                    || (shipTPE.getIntId() == 25))
                    && (thisNation.getId() != NationConstants.NATION_MOROCCO)
                    && (thisNation.getId() != NationConstants.NATION_OTTOMAN)
                    && (thisNation.getId() != NationConstants.NATION_EGYPT)
                    ) {
                continue;
            }

            shipTypeList.add(shipTPE);
        }

        // Retrieve commander names
        final List<CommanderName> commanderList = commanderNameManager.listNation(thisNation);

        // Calculate starting army
        final Game scenario = getGameManager().getByID(-1);
        final List<Brigade> brigadeList = brigadeManagerBean.listByGameNation(scenario, thisNation);
        final int armyStats[][] = new int[4][3];
        for (final Brigade brigade : brigadeList) {
            int artillery = 0, cavalry = 0, infantry = 0;
            for (final Battalion battalion : brigade.getBattalions()) {
                if (battalion.getType().isArtillery()) {
                    artillery += battalion.getHeadcount();

                } else if (battalion.getType().isCavalry()) {
                    cavalry += battalion.getHeadcount();

                } else {
                    infantry += battalion.getHeadcount();
                }
            }

            switch (brigade.getPosition().getRegion().getId()) {
                case EUROPE:
                    armyStats[0][0] += infantry;
                    armyStats[0][1] += cavalry;
                    armyStats[0][2] += artillery;
                    break;

                case AFRICA:
                    armyStats[1][0] += infantry;
                    armyStats[1][1] += cavalry;
                    armyStats[1][2] += artillery;
                    break;

                case CARIBBEAN:
                    armyStats[2][0] += infantry;
                    armyStats[2][1] += cavalry;
                    armyStats[2][2] += artillery;
                    break;

                case INDIES:
                    armyStats[3][0] += infantry;
                    armyStats[3][1] += cavalry;
                    armyStats[3][2] += artillery;
                    break;
            }
        }

        final List<Ship> shipList = shipManagerBean.listGameNation(scenario, thisNation);
        final int navyStats[][] = new int[4][2];
        for (final Ship ship : shipList) {
            int warship = 0, merchant = 0;
            if (ship.getType().getShipClass() == 0) {
                merchant = 1;
            } else {
                warship = 1;
            }

            switch (ship.getPosition().getRegion().getId()) {
                case EUROPE:
                    navyStats[0][0] += warship;
                    navyStats[0][1] += merchant;
                    break;

                case AFRICA:
                    navyStats[1][0] += warship;
                    navyStats[1][1] += merchant;
                    break;

                case CARIBBEAN:
                    navyStats[2][0] += warship;
                    navyStats[2][1] += merchant;
                    break;

                case INDIES:
                    navyStats[3][0] += warship;
                    navyStats[3][1] += merchant;
                    break;
            }
        }

        // retrieve user
        final User thisUser = getUser();

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("user", thisUser);
        refData.put("titleOverride", 1);
        refData.put("title", thisNation.getName());
        refData.put("nationList", nationList);
        refData.put("nation", thisNation);
        refData.put("nationId", thisNation.getId());
        refData.put("leaderName", commanderList.get(0).getName());
        commanderList.remove(0);
        refData.put("commanderList", commanderList);
        refData.put("armyTypeList", armyTypeList);
        refData.put("shipTypeList", shipTypeList);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        refData.put("staticData", getArticleManager().getArticleAsHtml(ARTICLES[thisNation.getId() - 1]));
        refData.put("leaderData", getArticleManager().getArticleAsHtml(COMMANDERS[thisNation.getId() - 1]));
        refData.put("trait", TRAITS[thisNation.getId() - 1]);

        switch (scenario.getScenarioId()) {

            case HibernateUtil.DB_S3:
                refData.put("relations", RELATIONS_S3[thisNation.getId() - 1]);
                break;

            case HibernateUtil.DB_S2:
                refData.put("relations", RELATIONS_S2[thisNation.getId() - 1]);
                break;

            case HibernateUtil.DB_S1:
            default:
                refData.put("relations", RELATIONS_S1[thisNation.getId() - 1]);
                break;

        }

        refData.put("armyStats", armyStats);
        refData.put("navyStats", navyStats);
        refData.put("scenarioStr", scenarioId);
        refData.put("scenarioId", scenario.getScenarioId());

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] List Scenario/Nation=" + thisNation.getId());
        return new ModelAndView("scenario/nation", refData);
    }

    /**
     * Instance ArmyTypeManagerBean class to perform queries
     * about nation objects.
     */
    private transient ArmyTypeManagerBean armyTypeManager;

    /**
     * Getter method used to access the ArmyTypeManager bean injected by Spring.
     *
     * @return a ArmyTypeManager bean.
     */
    public ArmyTypeManagerBean getArmyTypeManager() {
        return armyTypeManager;
    }

    /**
     * Setter method used by spring to inject a ArmyTypeManager bean.
     *
     * @param injArmyTypeManager a ArmyTypeManager bean.
     */
    public void setArmyTypeManager(final ArmyTypeManagerBean injArmyTypeManager) {
        armyTypeManager = injArmyTypeManager;
    }

    /**
     * Instance ShipTypeManagerBean class to perform queries
     * about ShipType objects.
     */
    private transient ShipTypeManagerBean shipTypeManager;

    /**
     * Setter method used by spring to inject a ShipTypeManager bean.
     *
     * @param injShipTypeManager a ShipTypeManager bean.
     */
    public void setShipTypeManagerBean(final ShipTypeManagerBean injShipTypeManager) {
        shipTypeManager = injShipTypeManager;
    }

    /**
     * Instance CommanderNameManagerBean class to perform queries
     * about CommanderName objects.
     */
    private transient CommanderNameManagerBean commanderNameManager;

    /**
     * Setter method used by spring to inject a CommanderNameManagerBean bean.
     *
     * @param injCommanderNameManagerBean a CommanderNameManagerBean bean.
     */
    public void setCommanderNameManagerBean(final CommanderNameManagerBean injCommanderNameManagerBean) {
        commanderNameManager = injCommanderNameManagerBean;
    }

    /**
     * Instance BrigadeManagerBean class to perform queries
     * about CommanderName objects.
     */
    private transient BrigadeManagerBean brigadeManagerBean;

    /**
     * Setter method used by spring to inject a BrigadeManagerBean bean.
     *
     * @param injBrigadeManagerBean a BrigadeManagerBean bean.
     */
    public void setBrigadeManagerBean(final BrigadeManagerBean injBrigadeManagerBean) {
        brigadeManagerBean = injBrigadeManagerBean;
    }

    public BrigadeManagerBean getBrigadeManager() {
        return brigadeManagerBean;
    }

    /**
     * Instance ShipManagerBean class to perform queries
     * about Ship objects.
     */
    private transient ShipManagerBean shipManagerBean;

    /**
     * Setter method used by spring to inject a ShipManagerBean bean.
     *
     * @param injShipManagerBean a ShipManagerBean bean.
     */
    public void setShipManagerBean(final ShipManagerBean injShipManagerBean) {
        shipManagerBean = injShipManagerBean;
    }

    public ShipManagerBean getShipManager() {
        return shipManagerBean;
    }

}
