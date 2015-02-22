package com.eaw1805.www.controllers;

import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Display Help pages.
 */
@org.springframework.stereotype.Controller
public class HelpController
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(HelpController.class);

    /**
     * The items for the Help pages (from the static site).
     */
    public static final Map<String, Integer[]> staticItems = new HashMap<String, Integer[]>();

    /**
     * Static Item titles.
     */
    public static final Map<Integer, String> titles = new HashMap<Integer, String>();

    /**
     * Main Category titles.
     */
    public static final Map<String, String> mainTitles = new HashMap<String, String>();

    /**
     * Static Handbook Table of Contents.
     */
    public static final String toc[] = {
            "economy", "trade", "politics", "espionage", "units", "navy", "movement", "warfare", "other"
    };

    /**
     * Static Handbook Description for each chapter.
     */
    public static final String tocDescription[] = {
            "Setting up expansion campaigns requires a strong Economy to support a costly war. This is based on exploitation of local resources through production sites, taxation imposed on inhabitants and trade.",
            "Trading can become a significant addition to a country's income. Trade with local merchants as well as with other nations is a hefty source of income and resources, and the lucrative colonial trade routes can prove extremely beneficial to the Empires that control them.",
            "Foreign relations with other empires may be the detrimental factor for victory or defeat, irrespective of how strong a nation’s economy or army is. Forming alliances, offering right-of-passage or trade status, or declaring colonial and European wars: the choices are difficult but crucial.",
            "While the fog-of-war system allows for limited knowledge of other countries' condition, the efficient use of spies can provide valuable information regarding political relations, economic growth and of-course the military forces of your friends and opponents.",
            "There is a variety of land units in the game, such as infantry, cavalry or artillery battalions, brigades, baggage trains and personalities like commanders and spies. Each has a different role in the empire’s operations and they have to be efficiently managed in order to optimize the gains from their use.",
            "Operations at sea are crucial. Dominance of the oceans can offer supply routes, lucrative trading, opportunities for invasions and colonial expansion. There are many different types of war & merchant ships, from small corvettes to huge Ships-of-the-Line, to be used for naval operations.",
            "Efficient maneuvering was an important trait of a successful campaign in the Napoleonic era. The limited range of land forces, the attrition incurred by the harsh terrain and inefficient supply conditions, and the limited communications between allies made decisions regarding movement of troops sometimes even more important than battles themselves.",
            "Land Warfare is the ultimate decider of who the victor will be. Warfare in Empires at war 1805 is so detailed, portraying the complexity of military operations of the era, that achieving victory requires exceptional organization and strategic skills. When large armies meet, the scale is taken from the strategic to the tactical level, allowing leaders to demonstrate their commanding capabilities in the field of battle.",
            "In order to depict uncontrolled events that very frequently spoiled plans, random events occur within the game. The weather may change each month and heavy winter can be devastating for marching armies. News and rumours may influence leaders’ decisions, not always in the right way. A breadth of information exchange is possible in Empires at War 1805, allowing leaders to practice their propaganda skills."
    };

    /**
     * Static Handbook image for each chapter.
     */
    public static final String tocImages[] = {
            "http://static.eaw1805.com/images/buttons/taxation/MUINormalTax.png",
            "http://static.eaw1805.com/images/buttons/ButLoadOff.png",
            "http://static.eaw1805.com/images/buttons/ButSpyReportsMapOff.png",
            "http://static.eaw1805.com/images/buttons/ButSpyOpenReportsOff.png",
            "http://static.eaw1805.com/images/buttons/ButFormFedOff.png",
            "http://static.eaw1805.com/images/buttons/ButFormFleetOff.png",
            "http://static.eaw1805.com/images/buttons/ButForcedMarchOff.png",
            "http://static.eaw1805.com/tiles/battleLand.png",
            "http://static.eaw1805.com/images/buttons/ButHandOverTileOff.png",
    };

    public HelpController() {
        super();

        //Introduction titles
        titles.put(156, "Gaming Environment");
        titles.put(158, "Land & Naval Forces");
        titles.put(157, "Control & Conquer");
        titles.put(155, "Game Mechanics");

        //economy titles
        titles.put(141, "Goods");
        titles.put(145, "Production Sites");
        titles.put(146, "Taxation");
        titles.put(147, "Population");
        titles.put(149, "Expenses");

        //Politics titles
        titles.put(110, "Declaration of War");
        titles.put(109, "Ending a War");
        titles.put(105, "Changing Relations");

        //Espionage titles
        titles.put(138, "Recruitment & Capture");
        titles.put(137, "Spies Management");
        titles.put(139, "Reports");
        titles.put(188, "Scouting");

        //trade titles
        titles.put(128, "Trading System");
        titles.put(124, "Transaction Fees");
        titles.put(123, "Neutral Merchants");
        titles.put(121, "Performing Trade");
        titles.put(114, "Owned Trade Cities");

        //movement titles
        titles.put(101, "Movement");
        titles.put(98, "Force March");
        titles.put(97, "Engage");
        titles.put(96, "Crossing maps");
        titles.put(95, "Special Cases");
        titles.put(94, "Movement Orders");
        titles.put(90, "Attrition");
        titles.put(87, "Supply in Europe");
        titles.put(82, "Supply in the Colonies");
        titles.put(81, "Sea Supply");

        //warfare titles
        titles.put(79, "Land Warfare");
        titles.put(77, "Invading Europe");
        titles.put(76, "Invading Colonies");
        titles.put(75, "Sector Ownership");
        titles.put(73, "Engaging Enemies");
        titles.put(71, "Battles");
        titles.put(69, "Tactical Battles");
        titles.put(61, "Fortifications");
        titles.put(62, "Commanders");
        titles.put(55, "Special Cases");
        titles.put(52, "Baggage Trains");
        titles.put(48, "Rebellions");

        //other titles
        titles.put(159, "Weather");
        titles.put(160, "Random Events");
        titles.put(161, "News");
        titles.put(164, "Civil Disorder");

        //units titles
        titles.put(183, "Barracks");
        titles.put(184, "Land Forces");
        titles.put(187, "Commanders");
        titles.put(189, "Baggage Trains");

        //navy titles
        titles.put(203, "Loading ");
        titles.put(202, "Sea Patrol");
        titles.put(201, "Naval Forces ");

        staticItems.put("introduction", new Integer[]{156, 158, 157, 155});
        staticItems.put("economy", new Integer[]{141, 145, 146, 147, 149});
        staticItems.put("trade", new Integer[]{128, 124, 123, 121, 114});
        staticItems.put("politics", new Integer[]{110, 109, 105});
        staticItems.put("espionage", new Integer[]{137, 139, 138, 188});
        staticItems.put("movement", new Integer[]{101, 98, 97, 96, 95, 94, 90, 87, 82, 81});
        staticItems.put("warfare", new Integer[]{79, 77, 76, 75, 73, 71, 69, 61, 62, 55, 52, 48});
        staticItems.put("other", new Integer[]{159, 160, 161, 164});
        staticItems.put("units", new Integer[]{183, 184, 187, 189});
        staticItems.put("navy", new Integer[]{201, 203, 202});

        //Main titles
        mainTitles.put("introduction", "Quick Start");
        mainTitles.put("politics", "Foreign Relations");
        mainTitles.put("espionage", "Espionage");
        mainTitles.put("economy", "Economy");
        mainTitles.put("trade", "Trade");
        mainTitles.put("movement", "Movement & Supply");
        mainTitles.put("warfare", "Warfare on Land");
        mainTitles.put("other", "Other Aspects");
        mainTitles.put("units", "Land Units");
        mainTitles.put("navy", "Naval Units");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/help/{page}")
    protected ModelAndView handle(final @PathVariable("page") String page,
                                  final HttpServletRequest request)
            throws Exception {
        ScenarioContextHolder.defaultScenario();

        // retrieve user
        final User thisUser = getUser();

        // check if the page requested exists
        if (page == null || page.isEmpty() || !staticItems.containsKey(page)) {
            throw new InvalidPageException("Page not found");
        }

        // retrieve page info
        final Integer[] pageInfo = staticItems.get(page);

        //Data retrieved from static pages
        final LinkedHashMap<Integer, String> staticData = new LinkedHashMap<Integer, String>();

        for (final Integer itemId : pageInfo) {
            staticData.put(itemId, getArticleManager().getArticleAsHtml(itemId));
        }

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("helpPage", page);
        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        //refData.put("staticData", articleManager.getItemAsHtml(pageInfo[0], pageInfo[1]));
        refData.put("staticData", staticData);
        refData.put("titles", titles);
        refData.put("title", mainTitles.get(page));
        refData.put("titleOverride", 1);

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Help page=" + page);
        return new ModelAndView("help", refData);
    }

}
