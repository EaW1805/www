package com.eaw1805.www.controllers.scenario;

import com.eaw1805.data.cache.Cachable;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main view for the scenario.
 */
@org.springframework.stereotype.Controller
public class ScenarioController
        extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ScenarioController.class);

    /**
     * List of current scenaria.
     */
    private static final List<String> lstScenaria = new ArrayList<String>();

    /**
     * List of future scenaria.
     */
    private static final List<String> lstScenariaFuture = new ArrayList<String>();

    /**
     * Name of scenaria.
     */
    private static final Map<String, String> mapName = new HashMap<String, String>();

    /**
     * Description of scenaria.
     */
    private static final Map<String, String> mapDescription = new HashMap<String, String>();

    /**
     * Description of scenaria.
     */
    private static final Map<String, String> mapPlayers = new HashMap<String, String>();

    static {
        lstScenaria.add("1802");
        lstScenaria.add("1804");
        lstScenaria.add("1805");
        lstScenaria.add("1808");

        lstScenariaFuture.add("1792");
        lstScenariaFuture.add("1812");

        mapName.put("1802", "Treaty of Amiens");
        mapDescription.put("1802", "<p class=\"manual\">The Treaty of Amiens ended hostilities between the French Republic and Great Britain during the French Revolutionary Wars, which is the reason it is widely accepted as the treaty  that ended the Revolutionary period. It was signed in the city of Amiens on 25 March 1802,  by Joseph Bonaparte and the Marquees Cornwallis as a \"Definitive Treaty of Peace\".</p>\n" +
                "<p class=\"manual\">Under the terms of the treaty Great Britain gave back some of their recently conquered colonial possessions from France and their allies and recognized the French Republic, while France agreed to withdraw their forces from Papal States and the Kingdom of Naples.</p>\n" +
                "<p class=\"manual\">The consequent peace spread all across Europe, but lasted only for one year. That short time however was the only period of peace during the so-called 'Great French War' between 1792 and 1815. Soon many objections regarding the treaty would rise, especially after certain terms failed to be implemented, and it was not long before the \"break\" was over and Europe plummeted into war again.</p>");
        mapPlayers.put("1802", "");

        mapName.put("1804", "The First French Empire");
        mapDescription.put("1804", "<p class=\"manual\">The First French Empire, also known as the Greater French Empire or Napoleonic Empire, was the empire of Napoleon I of France. It was the dominant power of much of continental Europe at the beginning of the 19th century.</p>\n" +
                "<p class=\"manual\">On 18 May 1804, Napoleon was given the title of emperor by the Senate; finally, on 2 December 1804, he was solemnly crowned, after receiving the Iron Crown of the Lombard kings, and was consecrated by Pope Pius VII in Notre-Dame de Paris.</p>\n" +
                "<p class=\"manual\">After this, in four campaigns, the Emperor transformed his \"Carolingian\" feudal and federal empire into one modelled on the Roman Empire. The memories of imperial Rome were for a third time, after Julius Caesar and Charlemagne, to modify the historical evolution of France. Though the vague plan for an invasion of Britain was never executed, the Battle of Ulm and the Battle of Austerlitz overshadowed the defeat of Trafalgar, and the camp at Boulogne put at Napoleon's disposal the best military resources he had commanded, in the form of La Grande Armée.</p>");
        mapPlayers.put("1804", "");

        mapName.put("1805", "Europe in Flames");
        mapDescription.put("1805", "<p class=\"manual\">The period between 1805 and 1815 was a particularly troublesome period for Europe. A series of devastating wars primarily against Napoleonic France, were fought on both land and sea, in Europe and the Colonies.  As a continuation of the wars sparked by the French Revolution of 1789, those wars revolutionized European armies and played out on an unprecedented scale, mainly owning to the application of modern mass conscription.</p>\n" +
                "<p class=\"manual\">The Revolutionary Wars between 1792 and 1802 were to be only a prelude of the mass scale warfare that was to follow. With the Revolution successfully defended, France would seek to expand the ideas of the Republic beyond its borders. The Peace of Amiens in 1802 which in effect ended the Revolutionary wars, was proven to be only a ceasefire in the grand scale of events.</p>\n" +
                "<p class=\"manual\">In May 1803, Great Britain and France went to war again ending the short period of peace. For 2 years hostilities resumed in the seas with Napoleon planning to cross the Channel and launch an attack on the British mainland, a plan that never came to fruition since the straight was always heavily guarded by the British Royal Navy.</p>\n" +
                "<p class=\"manual\">On December 30, 1804, Napoleon I ascended on the throne as Emperor of the French. His elevation to Emperor was overwhelmingly approved by the French citizens in a referendum. With his coronation started a series of great events that would unbalance Europe for 10 years, and change the shape of the world.</p>");
        mapPlayers.put("1805", "");

        mapName.put("1808", "Peninsular War");
        mapDescription.put("1808", "<p class=\"manual\">The Peninsular War (1807-1814) was a military conflict between France and the allied powers of Spain, the United Kingdom and Portugal for control of the Iberian Peninsula during the Napoleonic Wars. The war started when French and Spanish armies occupied Portugal in 1807, and escalated in 1808 when France turned on Spain, its ally until then. The war on the peninsula lasted until the Sixth Coalition defeated Napoleon in 1814, and is regarded as one of the first wars of national liberation, significant for the emergence of large-scale guerrilla warfare.</p>");
        mapPlayers.put("1808", "");

        mapName.put("1792", "French Revolutionary Wars");
        mapDescription.put("1792", "<p class=\"manual\">The French Revolutionary Wars were a series of major conflicts, from 1792 until 1802, fought between the French Revolutionary government and several European states. Marked by French revolutionary fervour and military innovations, the campaigns saw the French Revolutionary Armies defeat a number of opposing coalitions. They resulted in expanded French control to the Low Countries, Italy, and the Rhineland. The wars depended on extremely high numbers of soldiers, recruited by modern mass conscription.</p>");
        mapPlayers.put("1792", "");

        mapName.put("1812", "French Invasion of Russia");
        mapDescription.put("1812", "<p class=\"manual\">The French Invasion of Russia in 1812, also known as the Russian Campaign in France and the Patriotic War of 1812 in Russia, was a turning point during the Napoleonic Wars. It reduced the French and allied invasion forces (the Grande Armée) to a tiny fraction of their initial strength and triggered a major shift in European politics as it dramatically weakened French hegemony in Europe. The reputation of Napoleon as an undefeated military genius was severely shaken, while the French Empire's former allies, at first Prussia and then the Austrian Empire, broke their alliance with France and switched camps, which triggered the War of the Sixth Coalition.</p>");
        mapPlayers.put("1812", "");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/scenario/list")
    public ModelAndView handleRequest(final HttpServletRequest request,
                                      final HttpServletResponse response) {

        // Construct static part
        final Map<String, Object> refData = prepareScenarioList();

        // Retrieve free nations
        refData.putAll(gameHelper.getAllNewNations());
        refData.putAll(gameHelper.getAllFreePlayedNations());

        return new ModelAndView("scenario/list", refData);
    }

    @Cachable(cacheName = "constantCache")
    private Map<String, Object> prepareScenarioList() {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("lstScenaria", lstScenaria);
        refData.put("lstScenariaFuture", lstScenariaFuture);
        refData.put("mapName", mapName);
        refData.put("mapDescription", mapDescription);
        refData.put("mapPlayers", mapPlayers);

        for (String scenarioId : lstScenaria) {
            ScenarioContextHolder.setScenario(scenarioId);
            // Retrieve list of nations
            final List<Nation> nationList = getScenarioNationList(scenarioId);

            final StringBuilder htmlNations = new StringBuilder();
            for (final Nation nation : nationList) {
                htmlNations.append("<a href=\"http://www.eaw1805.com/scenario/");
                htmlNations.append(scenarioId);
                htmlNations.append("/nation/");
                htmlNations.append(nation.getId());
                htmlNations.append("\">");
                htmlNations.append("<img style=\"padding-right:2px;\" border=\"0\" src=\"http://static.eaw1805.com/site/../images/nations/nation-");
                htmlNations.append(nation.getId());
                htmlNations.append("-36.png\"");
                htmlNations.append("title=\"");
                htmlNations.append(nation.getName());
                htmlNations.append("\"></img></a>");
            }
            mapPlayers.put(scenarioId, htmlNations.toString());
        }

        // For solo game put just 1 country (France)
        mapPlayers.put("1804", "<img border=\"0\" src=\"http://static.eaw1805.com/site/../images/nations/nation-5-36.png\" title=\"France\"></img>");

        return refData;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/info")
    public ModelAndView handleRequest(@PathVariable("scenarioId") String scenarioId,
                                      final HttpServletRequest request,
                                      final HttpServletResponse response)
            throws Exception {

        ScenarioContextHolder.setScenario(scenarioId);

        // Retrieve list of nations
        final List<Nation> nationList = getScenarioNationList(scenarioId);

        // retrieve user
        final User thisUser = getUser();

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("scenarioStr", scenarioId);
        refData.put("user", thisUser);

        if ("1805".equals(scenarioId)) {
            refData.put("staticData", getArticleManager().getItemAsHtml(35, 109));

        } else if ("1802".equals(scenarioId)) {//else if 1802
            refData.put("staticData", getArticleManager().getItemAsHtml(10, 101));

        } else if ("1808".equals(scenarioId)) {//else if 1802
            refData.put("staticData", getArticleManager().getItemAsHtml(37, 101));

        } else {
            refData.put("staticData", getArticleManager().getItemAsHtml(10, 101));
        }

        refData.put("nationList", nationList);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Scenario 1805");
        return new ModelAndView("scenario/info", refData);
    }

}
