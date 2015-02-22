package com.eaw1805.www.controllers.admin;


import com.eaw1805.data.managers.QuestionnaireManager;
import com.eaw1805.data.managers.beans.QuestionnaireManagerBean;
import com.eaw1805.data.model.Questionnaire;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class QuestionnaireStatistics extends BaseController {



    @RequestMapping(method = RequestMethod.GET, value="/admin/questionnaire/statistics")
    public String getStatistics(final Model model) throws Exception {

        final User thisUser = getUser();

        //be sure user is logged in.
        if (thisUser == null || thisUser.getUserId() <= 0 || thisUser.getUserType() != 3) {
            throw new InvalidPageException("Page not found");
        }

        //retrieve all questionnaires
        final List<Questionnaire> questionnaires = questionnaireManager.list();

        final String[] titles = {
                "Replies",
                "Browsers"
        };
        final String[][] TYPES = {
                {
                    "BROWSER",
                    "SLOW UI",
                    "DIFFICULT UI",
                    "WEB SITE",
                    "RULES",
                    "NO TIME",
                    "GRAPHICS",
                    "PAYMENT",
                    "CUSTOMER SERVICE",
                    "TUTORIAL"
                },
                {
                    "B. CHROME",
                    "B. FIREFOX",
                    "B. IE",
                    "B. OPERA",
                    "B. NETSCAPE",
                    "B. OTHER"
                }

        };

        final Map<String, Integer> statsInt = new TreeMap<String, Integer>();
        statsInt.put("BROWSER", 0);
        statsInt.put("SLOW UI", 0);
        statsInt.put("DIFFICULT UI", 0);
        statsInt.put("WEB SITE", 0);
        statsInt.put("RULES", 0);
        statsInt.put("NO TIME", 0);
        statsInt.put("GRAPHICS", 0);
        statsInt.put("PAYMENT", 0);
        statsInt.put("CUSTOMER SERVICE", 0);
        statsInt.put("TUTORIAL", 0);
        statsInt.put("B. CHROME", 0);
        statsInt.put("B. FIREFOX", 0);
        statsInt.put("B. IE", 0);
        statsInt.put("B. OPERA", 0);
        statsInt.put("B. NETSCAPE", 0);
        statsInt.put("B. OTHER", 0);


        final Map<String, Double> statsDouble = new TreeMap<String, Double>();
        statsDouble.put("BROWSER", 0d);
        statsDouble.put("SLOW UI", 0d);
        statsDouble.put("DIFFICULT UI", 0d);
        statsDouble.put("WEB SITE", 0d);
        statsDouble.put("RULES", 0d);
        statsDouble.put("NO TIME", 0d);
        statsDouble.put("GRAPHICS", 0d);
        statsDouble.put("PAYMENT", 0d);
        statsDouble.put("CUSTOMER SERVICE", 0d);
        statsDouble.put("TUTORIAL", 0d);
        statsDouble.put("B. CHROME", 0d);
        statsDouble.put("B. FIREFOX", 0d);
        statsDouble.put("B. IE", 0d);
        statsDouble.put("B. OPERA", 0d);
        statsDouble.put("B. NETSCAPE", 0d);
        statsDouble.put("B. OTHER", 0d);

        for (final Questionnaire questionnaire : questionnaires) {
            if (questionnaire.isMyBrowser()) {
                statsInt.put("BROWSER", statsInt.get("BROWSER") + 1);
            }
            if (questionnaire.isSlowUI()) {
                statsInt.put("SLOW UI", statsInt.get("SLOW UI") + 1);
            }
            if (questionnaire.isDifficultUI()) {
                statsInt.put("DIFFICULT UI", statsInt.get("DIFFICULT UI") + 1);
            }
            if (questionnaire.isWebsite()) {
                statsInt.put("WEB SITE", statsInt.get("WEB SITE") + 1);
            }
            if (questionnaire.isRules()) {
                statsInt.put("RULES", statsInt.get("RULES") + 1);
            }
            if (questionnaire.isNoTime()) {
                statsInt.put("NO TIME", statsInt.get("NO TIME") + 1);
            }
            if (questionnaire.isGraphics()) {
                statsInt.put("GRAPHICS", statsInt.get("GRAPHICS") + 1);
            }
            if (questionnaire.isPayment()) {
                statsInt.put("PAYMENT", statsInt.get("PAYMENT") + 1);
            }
            if (questionnaire.isCustomerService()) {
                statsInt.put("CUSTOMER SERVICE", statsInt.get("CUSTOMER SERVICE") + 1);
            }
            if (questionnaire.isTutorial()) {
                statsInt.put("TUTORIAL", statsInt.get("TUTORIAL") + 1);
            }
            if (questionnaire.isBrowserChrome()) {
                statsInt.put("B. CHROME", statsInt.get("B. CHROME") + 1);
            }
            if (questionnaire.isBrowserFirefox()) {
                statsInt.put("B. FIREFOX", statsInt.get("B. FIREFOX") + 1);
            }
            if (questionnaire.isBrowserIE()) {
                statsInt.put("B. IE", statsInt.get("B. IE") + 1);
            }
            if (questionnaire.isBrowserOpera()) {
                statsInt.put("B. OPERA", statsInt.get("B. OPERA") + 1);
            }
            if (questionnaire.isBrowserNetscape()) {
                statsInt.put("B. NETSCAPE", statsInt.get("B. NETSCAPE") + 1);
            }
            if (questionnaire.isBrowserOther()) {
                statsInt.put("B. OTHER", statsInt.get("B. OTHER") + 1);
            }
        }

        for (final String entry : TYPES[0]) {
            statsDouble.put(entry, statsInt.get(entry)*100.0/(double)questionnaires.size());
        }

        for (final String entry : TYPES[1]) {
            statsDouble.put(entry, statsInt.get(entry)*100.0/(double)questionnaires.size());
        }


        model.addAttribute("TYPES", TYPES);
        model.addAttribute("statsInt", statsInt);
        model.addAttribute("statsDouble", statsDouble);
        model.addAttribute("titles", titles);
        return "admin/questionnaire";
    }

    @Autowired
    @Qualifier("questionnaireManagerBean")
    private transient QuestionnaireManagerBean questionnaireManager;
}
