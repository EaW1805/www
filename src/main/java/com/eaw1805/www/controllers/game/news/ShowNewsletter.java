package com.eaw1805.www.controllers.game.news;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.GameConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.NewsConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.managers.beans.NewsManagerBean;
import com.eaw1805.data.managers.beans.RegionManagerBean;
import com.eaw1805.data.managers.beans.ReportManagerBean;
import com.eaw1805.data.managers.beans.ShipManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.News;
import com.eaw1805.data.model.Report;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.fleet.Ship;
import com.eaw1805.data.model.map.Region;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.data.cache.Cachable;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Display the nation of a particular game.
 */
@Controller
public class ShowNewsletter
        extends ExtendedController
        implements RegionConstants, ReportConstants, NewsConstants, NationConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowNewsletter.class);

    private static final String TITLES_FIRST[] = {
            "",
            "Vienna",
            "Munich",
            "Copenhagen",
            "Madrid",
            "Paris",
            "London",
            "Nieuwe",
            "Rome",
            "Lisbon",
            "L'Echo du",
            "Naples",
            "Berlin",
            "Moscow",
            "Stockholm",
            "Constantinople",
            "Warsaw",
            "Cairo"
    };

    private static final int SIZE_FIRST[] = {
            0,
            68,
            68,
            51,
            66,
            68,
            68,
            68,
            68,
            68,
            66,
            68,
            68,
            68,
            62,
            44,
            68,
            68
    };

    private static final String TITLES_SECOND[] = {
            "",
            "Presse",
            "Presse", //Germanique
            "Statstidente",
            "Heraldo",
            "La Gazette",
            "Tribune",
            "Amsterdamsche",
            "Gazeta",
            "Telegrafo",
            "Rabat",
            "Gazeta",
            "Presse",
            "Vedomosti",
            "Posten",
            "Minitor",
            "Bibula",
            "Times"
    };

    private static final int SIZE_SECOND[] = {
            0,
            68,
            68, //Germanique
            53,
            68,
            61,
            68,
            41,
            68,
            68,
            68,
            68,
            68,
            62,
            68,
            68,
            68,
            68
    };

    private static final Map<Integer, Integer> NEWS_TUTORIAL = new HashMap<Integer, Integer>();

    static {
        NEWS_TUTORIAL.put(0, 211);
        NEWS_TUTORIAL.put(1, 212);
        NEWS_TUTORIAL.put(2, 213);
        NEWS_TUTORIAL.put(3, 214);
        NEWS_TUTORIAL.put(4, 215);
        NEWS_TUTORIAL.put(5, 216);
    }

    private static final Map<Integer, Integer> NEWS_NEWSLETTER_S1 = new HashMap<Integer, Integer>();

    static {
        NEWS_NEWSLETTER_S1.put(0, 231);
        NEWS_NEWSLETTER_S1.put(1, 232);
        NEWS_NEWSLETTER_S1.put(2, 233);
        NEWS_NEWSLETTER_S1.put(3, 234);
        NEWS_NEWSLETTER_S1.put(4, 235);
        NEWS_NEWSLETTER_S1.put(5, 236);
    }

    private static final Map<Integer, Integer> NEWS_NEWSLETTER_S2 = new HashMap<Integer, Integer>();

    static {
        NEWS_NEWSLETTER_S2.put(0, 190);
        NEWS_NEWSLETTER_S2.put(2, 191);
        NEWS_NEWSLETTER_S2.put(3, 192);
        NEWS_NEWSLETTER_S2.put(4, 193);
        NEWS_NEWSLETTER_S2.put(5, 194);
        NEWS_NEWSLETTER_S2.put(6, 195);
        NEWS_NEWSLETTER_S2.put(7, 196);
        NEWS_NEWSLETTER_S2.put(8, 197);
        NEWS_NEWSLETTER_S2.put(9, 198);
        NEWS_NEWSLETTER_S2.put(10, 199);
        NEWS_NEWSLETTER_S2.put(11, 200);
        NEWS_NEWSLETTER_S2.put(12, 210);
    }

    private static final Map<Integer, Integer> NEWS_NEWSLETTER_S3 = new HashMap<Integer, Integer>();

    private static final String POP_TYPES[] = {
            "Home Region",
            "Sphere of Influence",
            "Foreign"
    };

    private static final String ARMY_TYPES[] = {
            "In",
            "Ca",
            "Ar",
            "Kt",
            "Co",
            "MC",
            "CC"
    };

    private static final String ARMY_TYPES_DESCR[] = {
            "<img src='http://static.eaw1805.com/images/armies/dominant/infantry.png' title='Infantry' border=0 style='vertical-align: middle;'>&nbsp;Infantry",
            "<img src='http://static.eaw1805.com/images/armies/dominant/cavalry.png' title='Cavalry' border=0 style='vertical-align: middle;'>&nbsp;Cavalry",
            "<img src='http://static.eaw1805.com/images/armies/dominant/artillery.png' title='Artillery' border=0 style='vertical-align: middle;'>&nbsp;Artillery",
            "<img src='http://static.eaw1805.com/images/armies/dominant/infantry.png' title='Colonial Marines-' border=0 style='vertical-align: middle;'>&nbsp;Colonial Marines",
            "<img src='http://static.eaw1805.com/images/armies/dominant/infantry.png' title='Colonial Infantry' border=0 style='vertical-align: middle;'>&nbsp;Colonial Infantry",
            "<img src='http://static.eaw1805.com/images/armies/dominant/cavalry.png' title='Colonial Cavalry' border=0 style='vertical-align: middle;'>&nbsp;Colonial Cavalry",
            "CC"
    };

    private static final String REGION_IMG[] = {
            "",
            "&nbsp;<img src='http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOff.png' height=20 title='Europe' border=0 style='vertical-align: middle;'>&nbsp;",
            "&nbsp;<img src='http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png' height=20 title='Caribbean' border=0 style='vertical-align: middle;'>&nbsp;",
            "&nbsp;<img src='http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png' height=20 title='Indies' border=0 style='vertical-align: middle;'>&nbsp;",
            "&nbsp;<img src='http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png' height=20 title='Africa' border=0 style='vertical-align: middle;'>&nbsp;",
    };

    private static final String ARMY_STATS[] = {
            A_TOT_BAT,
            A_TOT_MONEY,
            A_TOT_FOOD + EUROPE,
            A_TOT_FOOD + CARIBBEAN,
            A_TOT_FOOD + INDIES,
            A_TOT_FOOD + AFRICA,
            A_TOT_UNPAID,
            A_TOT_STARV_QTE,
            A_TOT_STARV_DES
    };

    private static final String ARMY_STATS_DESCR[] = {
            "<img src='http://static.eaw1805.com/images/buttons/icons/formations/battalion.png' height=15 title='Battalions' border=0 style='vertical-align: middle;'>&nbsp;Total Battalions",
            "<img src='http://static.eaw1805.com/images/goods/good-1.png' height=18 title='Money' border=0 style='vertical-align: middle;'>&nbsp;Soldiers Salaries",
            "<img src='http://static.eaw1805.com/images/goods/good-4.png' height=14 title='Food' border=0 style='vertical-align: middle;'><img src='http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOff.png' height=16 title='Europe' border=0 style='vertical-align: middle;'>&nbsp;<span style='font-size: 9px;'>Food used in Europe</span>",
            "<img src='http://static.eaw1805.com/images/goods/good-4.png' height=14 title='Food' border=0 style='vertical-align: middle;'><img src='http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png' height=16 title='Europe' border=0 style='vertical-align: middle;'>&nbsp;<span style='font-size: 9px;'>Food used in Caribbean</span>",
            "<img src='http://static.eaw1805.com/images/goods/good-4.png' height=14 title='Food' border=0 style='vertical-align: middle;'><img src='http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png' height=16 title='Europe' border=0 style='vertical-align: middle;'>&nbsp;<span style='font-size: 9px;'>Food used in Indies</span>",
            "<img src='http://static.eaw1805.com/images/goods/good-4.png' height=14 title='Food' border=0 style='vertical-align: middle;'><img src='http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png' height=16 title='Europe' border=0 style='vertical-align: middle;'>&nbsp;<span style='font-size: 9px;'>Food used in Africa</span>",
            "Unpaid Soldiers",
            "Starving Soldiers",
            "Deserting Soldiers"
    };

    private static final String NAVY_TYPES[] = {
            S_TOT_SHIPS,
            S_TOT_CANNONS,
            S_TOT_MARINES
    };

    private static final String NAVY_TYPES_DESCR[] = {
            "Total Ships",
            "Total Cannons",
            "Total Marines"
    };

    private static final String NAVY_STATS[] = {
            S_TOT_MONEY,
            S_TOT_WINE + EUROPE,
            S_TOT_WINE + CARIBBEAN,
            S_TOT_WINE + INDIES,
            S_TOT_WINE + AFRICA,
            S_TOT_UNPD,
            S_SOBMRNS_QTE,
            S_SOBMRNS_COST
    };

    private static final String NAVY_STATS_DESCR[] = {
            "<img src='http://static.eaw1805.com/images/goods/good-1.png' height=18 title='Money' border=0 style='vertical-align: middle;'>&nbsp;Marines Salaries",
            "<img src='http://static.eaw1805.com/images/goods/good-13.png' height=14 title='Wine' border=0 style='vertical-align: middle;'><img src='http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOff.png' height=16 title='Europe' border=0 style='vertical-align: middle;'>&nbsp;<span style='font-size: 9px;'>Wine used in Europe</span>",
            "<img src='http://static.eaw1805.com/images/goods/good-13.png' height=14 title='Wine' border=0 style='vertical-align: middle;'><img src='http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png' height=16 title='Europe' border=0 style='vertical-align: middle;'>&nbsp;<span style='font-size: 9px;'>Wine used in Caribbean</span>",
            "<img src='http://static.eaw1805.com/images/goods/good-13.png' height=14 title='Wine' border=0 style='vertical-align: middle;'><img src='http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png' height=16 title='Europe' border=0 style='vertical-align: middle;'>&nbsp;<span style='font-size: 9px;'>Wine used in Indies</span>",
            "<img src='http://static.eaw1805.com/images/goods/good-13.png' height=14 title='Wine' border=0 style='vertical-align: middle;'><img src='http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png' height=16 title='Europe' border=0 style='vertical-align: middle;'>&nbsp;<span style='font-size: 9px;'>Wine used in Africa</span>",
            "Unpaid Marines",
            "Sober Marines",
            "Sober Marines Extra Costs"
    };

    private static final int TOT_NEWS_PICS[] = {
            0,
            13, // Surrenders
            15,  // Declarations of war
            40,  // conquer capital
            0,  // field battle
            5,  // random events
            117, // tactical battle
            24,  // naval battle
            4,  // ship construction
            7,   // fort construction
            1, // World Domination
            2 // Defeat
    };

    private static final int TOT_NEWS_PICS_ALT[] = {
            0,
            7, // Surrenders
            9, // Declarations of war
            42, // conquer capital
            0, // field battle
            0, // random events
            22, // tactical battle
            13, // naval battle
            4, // ship construction
            5,  // fort construction
            0, // World Domination
            0 // Defeat
    };

    private static final String RANDOM_MESSAGES[] = {
            "THE KING IS SICK! Sickness makes our Monarch neglect his duties this month. The Nation wishes a fast recovery.",
            "WHERE IS THE MONARCH? Rumour has it our beloved King is at love, neglecting his duties the last month to spend more time with the chosen beauty of his heart.",
            "BOREDOM! A border guard and his dog died of boredom last month. Where is this country going to?",
            "WAKE UP! Glory awaits those who dare… will history remember us?"
    };

    @RequestMapping(method = RequestMethod.GET, value = "/report/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/newsletter")
    protected ModelAndView handle(@PathVariable final String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  HttpServletRequest request) throws Exception {
        return handle(scenarioId, gameId, nationId, null, request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/report/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/month/{turnStr}")
    protected ModelAndView handle(@PathVariable final String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  @PathVariable final String turnStr,
                                  HttpServletRequest request) throws Exception {
        ScenarioContextHolder.defaultScenario();
        if (scenarioId == null || scenarioId.isEmpty()) {
            throw new InvalidPageException("Page not found");
        } else {
            try {
                ScenarioContextHolder.setScenario(scenarioId);

            } catch (Exception e) {
                throw new InvalidPageException("Page not found");
            }
        }
        // Retrieve Game entity
        Game thisGame;
        if ((gameId == null) || (gameId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisGame = getGameManager().getByID(Integer.parseInt(gameId));
            if (thisGame == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Retrieve Nation entity
        Nation thisNation;
        if ((nationId == null) || (nationId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisNation = getNationManager().getByID(Integer.parseInt(nationId));
            if (thisNation == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Retrieve Turn
        int turnInt = thisGame.getTurn();
        if (turnStr != null) {
            try {
                turnInt = Integer.parseInt(turnStr);
                if (turnInt > thisGame.getTurn()) {
                    turnInt = thisGame.getTurn();
                }
            } catch (Exception ex) {
                // rejected
            }
        }

        // retrieve user
        final User thisUser = getUser();
        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Newsletter for game=" + thisGame.getGameId() + "/nation=" + thisNation.getName() + "/turn=" + turnInt);

        // Check that user is allowed to view nation
        if (thisUser.getUserType() != 3 && getUserGameManager().listActive(thisUser, thisGame, thisNation).isEmpty()) {
            throw new InvalidPageException("Page not found");
        }

        // produce report + Prepare data to pass to jsp
        final Map<String, Object> refData = prepareReport(thisGame, thisNation, turnInt);

        // Fill in data to pass to jsp
        refData.put("game", thisGame);
        refData.put("gameId", thisGame.getGameId());
        refData.put("nation", thisNation);
        refData.put("nationId", thisNation.getId());
        refData.put("turn", turnInt);
        refData.put("scenarioId", scenarioId);

        // check duration of game
        final double modifier;
        switch (thisGame.getType()) {
            case GameConstants.DURATION_SHORT:
                modifier = .7d;
                break;

            case GameConstants.DURATION_LONG:
                modifier = 1.3d;
                break;

            case GameConstants.DURATION_NORMAL:
            default:
                modifier = 1d;
        }
        refData.put("gameLengthModifier", modifier);

        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        if (request.getParameter("eawClient") == null) {
            return new ModelAndView("game/newsletter", refData);
        } else {
            return new ModelAndView("game/newsletterMinimal", refData);
        }
    }

    /**
     * Prepare report.
     *
     * @param thisGame   the game to examine.
     * @param thisNation the nation to examine.
     * @param turnInt    the turn to examine.
     * @return the map with the data that will be passed to the jsp.
     */
    @Cachable(cacheName = "longGameCache")
    protected Map<String, Object> prepareReport(final Game thisGame,
                                                final Nation thisNation,
                                                final int turnInt) {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // access Calendar object
        final Calendar thisCal = calendar(thisGame);
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuf.append(" ");
        strBuf.append(thisCal.get(Calendar.YEAR));
        refData.put("gameDate", strBuf.toString());

        final Calendar thatCal = calendar(thisGame, turnInt);
        final StringBuilder strBuff = new StringBuilder();
        strBuff.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuff.append(" ");
        strBuff.append(thatCal.get(Calendar.YEAR));

        final List<News> lstNews = newsManager.listGameNation(thisGame, turnInt - 1, thisNation);
        final List<News> lstNewsUncategorized = new ArrayList<News>();
        final List<News> lstVP = new ArrayList<News>();
        final List<News> lstWorld = new ArrayList<News>();
        final List<News> lstEconomy = new ArrayList<News>();
        final List<News> lstPolitics = new ArrayList<News>();
        final List<News> lstMilitary = new ArrayList<News>();
        final List<News> lstLetters = new ArrayList<News>();

        for (final News newsEntry : lstNews) {
            switch (newsEntry.getType()) {

                case NEWS_POLITICAL:
                    lstPolitics.add(newsEntry);
                    break;

                case NEWS_MILITARY:
                    lstMilitary.add(newsEntry);
                    break;

                case NEWS_ECONOMY:
                    lstEconomy.add(newsEntry);
                    break;

                case NEWS_WORLD:
                    lstWorld.add(newsEntry);
                    break;

                case NEWS_LETTER:
                case NEWS_LETTER_ANONYMOUS:
                    if (!newsEntry.getText().isEmpty()) {
                        // enforce maximum size to images
                        newsEntry.setText(newsEntry.getText().replaceAll("<img", "<img style='max-width: 800px; max-height: 600px;'"));

                        lstLetters.add(newsEntry);
                    }
                    break;

                case NEWS_VP:
                    lstVP.add(newsEntry);
                    break;

                case NEWS_HISTORY:
                    // ignore this entry
                    break;

                case NEWS_FOREIGN:
                default:
                    lstNewsUncategorized.add(newsEntry);
            }
        }

        // Identify front news articles
        final List<News> frontNews = new ArrayList<News>();
        News topStory = new News();
        int usedPics[] = new int[TOT_NEWS_PICS.length];

        // 0. If the game has ended, identify winners announcement entries
        if (thisGame.getTurn() == turnInt && thisGame.getEnded()) {
            // locate winners
            for (final News entry : lstWorld) {
                if (entry.getText().contains(" achieved world domination !")) {
                    entry.setType(10);

                    decidePicture(entry, usedPics);
                    usedPics[entry.getType()]++;
                    topStory = entry;
                    break;
                }
            }

            // locate co-winners
            for (final News entry : lstWorld) {
                if (entry.getText().contains(" also very close to world domination !")) {
                    entry.setType(1);

                    decidePicture(entry, usedPics);
                    usedPics[entry.getType()]++;

                    frontNews.add(entry);
                    break;
                }
            }

            // locate runner-ups
            for (final News entry : lstWorld) {
                if (entry.getText().contains(" for world domination !")) {
                    entry.setType(1);

                    decidePicture(entry, usedPics);
                    usedPics[entry.getType()]++;

                    frontNews.add(entry);
                    break;
                }
            }

        } else {

            // 0. Check if a country has entered civil disorder.
            for (final News entry : lstMilitary) {
                if (!entry.getFront()
                        && (entry.getText().contains(" enters civil disorder !"))) {
                    entry.setType(11);

                    decidePicture(entry, usedPics);
                    usedPics[entry.getType()]++;

                    frontNews.add(entry);
                    if (frontNews.size() > 3) {
                        break;
                    }
                }
            }

            // 1. If a country surrendered to us, or we surrender to another country, is mentioned first in the news.
            for (final News entry : lstPolitics) {
                if (!entry.getFront()
                        && (entry.getText().contains("We surrendered ")
                        || entry.getText().contains("We signed a piece treaty ")
                        || entry.getText().contains("We signed an alliance")
                        || entry.getText().contains(" surrendered to us!"))) {
                    entry.setType(1);

                    decidePicture(entry, usedPics);
                    usedPics[entry.getType()]++;

                    frontNews.add(entry);
                    if (frontNews.size() > 3) {
                        break;
                    }
                }
            }

            // 2. Declarations of all wars from own country.
            // If declaring war to more than two countries in the same month, only the first 2
            // (in terms of VPs) are listed in the front page.
            if (frontNews.size() < 4) {
                for (final News entry : lstPolitics) {
                    if (!entry.getFront()
                            && entry.getText().contains("We declared war to ")) {
                        entry.setType(2);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 3) {
                            break;
                        }
                    }
                }
            }

            // 3. Declarations of all wars to own country.
            // If declared by more than two, only the first 2 (in terms of VPs) are listed)
            if (frontNews.size() < 4) {
                for (final News entry : lstPolitics) {
                    if (!entry.getFront()
                            && entry.getText().contains("declared war upon us")) {
                        entry.setType(2);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 3) {
                            break;
                        }
                    }
                }
            }

            // 4. If no declarations of war, battles follow next.
            // If there is a field battle result, it is mentioned here.
            // (If more than one result, the one with the highest number of troops from own country is mentioned)
            // @todo field battle newsletter entry

            // 5. If there is no field battle result, but there is an initiation of a Field Battle, it is mentioned here.
            // (If more than one field battle start up, the one with the highest number of troops from own country is mentioned)


            // 6a. If there is no epidemic in the capital, land tactical battles are checked,
            // and the largest tactical is mentioned in the news.
            if (frontNews.size() < 4) {
                for (final News entry : lstMilitary) {
                    if (!entry.getFront()
                            && (entry.getText().contains("We defeated the land forces of ")
                            || entry.getText().contains("Our forces were defeated by the land forces of ")
                            || entry.getText().contains("Our forces battled against the land forces of "))) {
                        entry.setType(6);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 3) {
                            break;
                        }
                    }
                }
            }

            // 7. If there is neither field result or set up, major naval battles are checked.
            // If there is any, it is reported here (for more than one,  the one with most ships is mentioned)
            if (frontNews.size() < 4) {
                for (final News entry : lstMilitary) {
                    if (!entry.getFront()
                            && (entry.getText().contains("We defeated the naval forces of ")
                            || entry.getText().contains("Our forces were defeated by the naval forces of ")
                            || entry.getText().contains("Our forces battled against the naval forces of ")
                            || entry.getText().contains("Our naval force ")
                            || entry.getText().contains("Our naval forces ")
                            || entry.getText().contains("overrun our forces at")
                            || entry.getText().contains(" victoriously led our troops to glory."))) {
                        entry.setType(7);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 3) {
                            break;
                        }
                    }
                }
            }

            // 8. Conquer of capital
            if (frontNews.size() < 4) {
                for (final News entry : lstMilitary) {
                    if (!entry.getFront()
                            && (entry.getText().contains("We lost ")
                            || entry.getText().contains("conquered")
                            || entry.getText().contains("We conquered"))) {
                        entry.setType(3);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 3) {
                            break;
                        }
                    }
                }
            }

            // 1. If a country surrendered to us, or we surrender to another country, is mentioned first in the news.
            if (frontNews.size() < 4) {
                for (final News entry : lstPolitics) {
                    if (!entry.getFront()
                            && (entry.getText().contains(" surrendered ")
                            || entry.getText().contains(" signed a piece treaty ")
                            || entry.getText().contains(" signed an alliance ")
                            || entry.getText().contains(" of our soldiers as prisoners "))) {
                        entry.setType(1);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);
                        if (frontNews.size() > 3) {
                            break;
                        }
                    }
                }
            }

            // 2. Declarations of all wars from own country.
            // If declaring war to more than two countries in the same month, only the first 2
            // (in terms of VPs) are listed in the front page.
            if (frontNews.size() < 4) {
                for (final News entry : lstPolitics) {
                    if (!entry.getFront()
                            && entry.getText().contains(" declared war ")) {
                        entry.setType(2);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 3) {
                            break;
                        }
                    }
                }
            }


            // 6. If there is no epidemic in the capital, land tactical battles are checked,
            // and the largest tactical is mentioned in the news.
            if (frontNews.size() < 4) {
                for (final News entry : lstMilitary) {
                    if (!entry.getFront()
                            && (entry.getText().contains("defeated the land forces of ")
                            || entry.getText().contains("battled against the land forces of "))) {
                        entry.setType(6);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 3) {
                            break;
                        }
                    }
                }
            }

            // 7. If there is neither field result or set up, major naval battles are checked.
            // If there is any, it is reported here (for more than one,  the one with most ships is mentioned)
            if (frontNews.size() < 4) {
                for (final News entry : lstMilitary) {
                    if (!entry.getFront()
                            && (entry.getText().contains("defeated the naval forces of ")
                            || entry.getText().contains("were defeated by the naval forces of ")
                            || entry.getText().contains("battled against the naval forces of "))) {
                        entry.setType(7);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 3) {
                            break;
                        }
                    }
                }
            }


            // Check if we need to add some extra articles
            // X1. Total kills exceed certain limit to acquire VP
            if (frontNews.size() <= 1) {
                for (final News entry : lstMilitary) {
                    if (!entry.getFront()
                            && (entry.getText().contains("Our capable commanders")
                            || entry.getText().contains("enemy capitals"))) {
                        entry.setType(5);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 2) {
                            break;
                        }
                    }
                }
            }

            // X2. Total sinks exceed certain limit to acquire VP
            if (frontNews.size() <= 1) {
                for (final News entry : lstMilitary) {
                    if (!entry.getFront() && entry.getText().contains("Our capable admirals")) {
                        entry.setType(5);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 2) {
                            break;
                        }
                    }
                }
            }

            // X3. Conquer of a city
            if (frontNews.size() <= 1) {
                for (final News entry : lstMilitary) {
                    if (!entry.getFront() && entry.getText().contains("conquered the")) {
                        entry.setType(3);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 2) {
                            break;
                        }
                    }
                }
            }

            // X4. Construction of fort
            if (frontNews.size() <= 1) {
                for (final News entry : lstMilitary) {
                    if (!entry.getFront() && entry.getText().contains("We built a")) {
                        entry.setType(9);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 2) {
                            break;
                        }
                    }
                }
            }

            // X5. Construction of class 3+ war ship
            if (frontNews.size() <= 1) {
                for (final News entry : lstMilitary) {
                    if (!entry.getFront() && entry.getText().contains("The construction of ship")) {
                        entry.setType(8);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 2) {
                            break;
                        }
                    }
                }
            }

            // X6. Political Random Events
            if (frontNews.size() <= 1) {
                for (final News entry : lstPolitics) {
                    if (!entry.getFront()
                            && (entry.getText().contains("Serious problems")
                            || entry.getText().contains("Shocking behavior")
                            || entry.getText().contains("Riots in our colonies")
                            || entry.getText().contains("Our ministers report")
                            || entry.getText().contains("Our very own artist")
                            || entry.getText().contains("A personality of significant")
                            || entry.getText().contains("Our scientists")
                            || entry.getText().contains("Persia has attacked us")
                            || entry.getText().contains("Royalists are revolting")
                            || entry.getText().contains("A serious political scandal"))) {
                        entry.setType(5);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 2) {
                            break;
                        }
                    }
                }
            }

            // X7. Military Random Events
            if (frontNews.size() <= 1) {
                for (final News entry : lstMilitary) {
                    if (!entry.getFront()
                            && (entry.getText().contains("Our ministers report")
                            || entry.getText().contains("Our commander"))) {
                        entry.setType(5);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 2) {
                            break;
                        }
                    }
                }
            }

            // X8. Economy Events
            if (frontNews.isEmpty()) {
                for (final News entry : lstEconomy) {
                    if (!entry.getFront()
                            && (entry.getText().contains("Our capable traders managed to bargain")
                            || entry.getText().contains("Our people are very happy"))) {
                        entry.setType(5);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 2) {
                            break;
                        }
                    }
                }
            }

            // Final check -- we may still miss some entries
            if (frontNews.size() <= 1) {
                for (final News entry : lstMilitary) {
                    if (!entry.getFront()) {
                        entry.setType(5);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 2) {
                            break;
                        }
                    }
                }
            }

            // Final check -- we may still miss some entries
            if (frontNews.size() <= 1) {
                for (final News entry : lstPolitics) {
                    if (!entry.getFront()) {
                        entry.setType(5);

                        decidePicture(entry, usedPics);
                        usedPics[entry.getType()]++;

                        frontNews.add(entry);

                        if (frontNews.size() > 2) {
                            break;
                        }
                    }
                }
            }

            // Get main story and sub-stories
            if (frontNews.isEmpty()) {
                //Add one from predefined messages.
                final News newsEntry = new News();
                newsEntry.setType(5);
                newsEntry.setText(RANDOM_MESSAGES[(turnInt + thisNation.getId()) % 4]);
                newsEntry.setNewsId(1);
                decidePicture(newsEntry, usedPics);
                topStory = newsEntry;

            } else {
                topStory = frontNews.remove(0);
            }
        }


        // --- SPLIT NEWS Sections for better fit

        // Calculate total number of military news
        int totMilitaryNews = 0;
        for (final News entry : lstMilitary) {
            if (!entry.getFront()) {
                totMilitaryNews++;
            }
        }

        // Calculate total number of political news
        int totPoliticalNews = 0;
        for (final News entry : lstPolitics) {
            if (!entry.getFront()) {
                totPoliticalNews++;
            }
        }

        // Calculate total number of economy news
        int totEconomyNews = 0;
        for (final News entry : lstEconomy) {
            if (!entry.getFront()) {
                totEconomyNews++;
            }
        }

        // Identify length of VP list
        int heightVP = 0;
        for (final News news : lstVP) {
            heightVP += 1 + (news.getText().length() / 23);
        }

        // The maximum news entry that may appear on the left side
        int maxSideEntries;
        if (lstVP.isEmpty() || heightVP < 5) {
            maxSideEntries = 12;

        } else {
            maxSideEntries = 12 + (int) ((heightVP - 4) / 2.5d);
        }

        // Take into account height of section header for political news
        if (totMilitaryNews < maxSideEntries && totPoliticalNews > 0) {
            maxSideEntries--;
        }

        // Take into account height of section header for economy news
        if (totMilitaryNews + totPoliticalNews < maxSideEntries && totEconomyNews > 0) {
            maxSideEntries--;
        }

        // Split War news if too many
        int newsCount = 0;
        final List<News> topMilitary;
        final List<News> addMilitary = new ArrayList<News>();
        if (totMilitaryNews > maxSideEntries) {
            topMilitary = new ArrayList<News>();
            for (final News entry : lstMilitary) {
                if (!entry.getFront()) {
                    newsCount++;

                    if (newsCount > maxSideEntries) {
                        addMilitary.add(entry);

                    } else {
                        topMilitary.add(entry);
                    }
                }
            }

        } else {
            topMilitary = lstMilitary;
            newsCount += totMilitaryNews;
        }

        // Split Political news if too many
        final List<News> topPolitical;
        final List<News> addPolitical = new ArrayList<News>();
        if (newsCount < maxSideEntries
                && totMilitaryNews + totPoliticalNews > maxSideEntries) {

            topPolitical = new ArrayList<News>();
            for (final News entry : lstPolitics) {
                if (!entry.getFront()) {
                    newsCount++;

                    if (newsCount > maxSideEntries) {
                        addPolitical.add(entry);

                    } else {
                        topPolitical.add(entry);
                    }
                }
            }

        } else {
            topPolitical = lstPolitics;
            newsCount += totPoliticalNews;
        }

        // Split Economy news if too many
        final List<News> topEconomy;
        final List<News> addEconomy = new ArrayList<News>();
        if (newsCount < maxSideEntries
                && totMilitaryNews + totPoliticalNews + totEconomyNews > maxSideEntries) {

            topEconomy = new ArrayList<News>();
            for (final News entry : lstEconomy) {
                if (!entry.getFront()) {
                    newsCount++;

                    if (newsCount > maxSideEntries) {
                        addEconomy.add(entry);

                    } else {
                        topEconomy.add(entry);
                    }
                }
            }

        } else {
            topEconomy = lstEconomy;
            newsCount += totEconomyNews;
        }

        // Split World news if too many
        final List<News> topWorld = new ArrayList<News>();
        final List<News> addWorld = new ArrayList<News>();
        if (newsCount < maxSideEntries
                && totMilitaryNews + totPoliticalNews + totEconomyNews + lstWorld.size() > maxSideEntries) {

            for (final News entry : lstWorld) {
                if (!entry.getFront()) {
                    newsCount++;

                    if (newsCount > maxSideEntries) {
                        addWorld.add(entry);

                    } else {
                        topWorld.add(entry);
                    }
                }
            }

        } else {

            for (final News entry : lstWorld) {
                if (!entry.getFront()) {
                    topWorld.add(entry);
                }
            }

            newsCount += topWorld.size();
        }

        // Check if tutorial article exists
        boolean hasTutorial = false;
        String txtTutorial = "";

        if (NEWS_TUTORIAL.containsKey(turnInt)) {
            hasTutorial = true;
            txtTutorial = getArticleManager().getArticleAsHtml(NEWS_TUTORIAL.get(turnInt));

        }

        // Check if newsletter article exists
        boolean hasHistory = false;
        String txtHistory = "";

        switch (thisGame.getScenarioId()) {

            case HibernateUtil.DB_S1:
                if (NEWS_NEWSLETTER_S1.containsKey(turnInt)) {
                    hasHistory = true;
                    txtHistory = getArticleManager().getArticleAsHtml(NEWS_NEWSLETTER_S1.get(turnInt));
                }
                break;

            case HibernateUtil.DB_S2:
                if (NEWS_NEWSLETTER_S2.containsKey(turnInt)) {
                    hasHistory = true;
                    txtHistory = getArticleManager().getArticleAsHtml(NEWS_NEWSLETTER_S2.get(turnInt));
                }
                break;

            case HibernateUtil.DB_S3:
                if (NEWS_NEWSLETTER_S3.containsKey(turnInt)) {
                    hasHistory = true;
                    txtHistory = getArticleManager().getArticleAsHtml(NEWS_NEWSLETTER_S3.get(turnInt));
                }
                break;

            default:
                hasHistory = false;

        }

        // Fill in remaining data to pass to jsp
        refData.put("titleFirst", TITLES_FIRST[thisNation.getId()]);
        refData.put("titleSecond", TITLES_SECOND[thisNation.getId()]);
        refData.put("sizeFirst", SIZE_FIRST[thisNation.getId()]);
        refData.put("sizeSecond", SIZE_SECOND[thisNation.getId()]);
        refData.put("newsTopStory", topStory);
        refData.put("newsFront", frontNews);
        refData.put("news", lstNewsUncategorized);
        refData.put("newsLetters", lstLetters);
        refData.put("newsMilitary", topMilitary);
        refData.put("addMilitary", addMilitary);
        refData.put("newsPolitics", topPolitical);
        refData.put("addPolitical", addPolitical);
        refData.put("newsEconomy", topEconomy);
        refData.put("addEconomy", addEconomy);
        refData.put("topWorld", topWorld);
        refData.put("addWorld", addWorld);
        refData.put("maxSideEntries", maxSideEntries);
        refData.put("totMilitaryNews", totMilitaryNews);
        refData.put("totPoliticalNews", totPoliticalNews);
        refData.put("totEconomyNews", totEconomyNews);
        refData.put("demographics", prepareDemographics(thisGame, thisNation, turnInt - 1));
        refData.put("armyStatsRegion", prepareArmyRegionStatistics(thisGame, thisNation, turnInt - 1));
        refData.put("armyStatsType", prepareArmyTypeStatistics(thisGame, thisNation, turnInt - 1));
        refData.put("armyStatsCosts", prepareArmyCostsStatistics(thisGame, thisNation, turnInt - 1));
        refData.put("navyStatsRegion", prepareNavyRegionStatistics(thisGame, thisNation, turnInt - 1));
        refData.put("navyStatsType", prepareNavyTypeStatistics(thisGame, thisNation, turnInt - 1));
        refData.put("navyStatsCosts", prepareNavyCostsStatistics(thisGame, thisNation, turnInt - 1));
        refData.put("vpList", lstVP);
        refData.put("vpNow", retrieveVPs(thisGame, thisNation, turnInt - 1));
        refData.put("vpPrev", retrieveVPs(thisGame, thisNation, turnInt - 2));
        refData.put("hasHistory", hasHistory);
        refData.put("txtHistory", txtHistory);
        refData.put("hasTutorial", hasTutorial);
        refData.put("txtTutorial", txtTutorial);
        refData.put("months", constructMonths(thisGame));
        refData.put("turnMonth", strBuff.toString());

        return refData;
    }

    private void decidePicture(final News entry, final int[] usedPics) {
        entry.setFront(true);

        int maxPics = TOT_NEWS_PICS[entry.getType()];

        // Check if we should use Muslim images
        if (TOT_NEWS_PICS_ALT[entry.getType()] > 0
                && (entry.getNation().getId() == NATION_MOROCCO
                || entry.getNation().getId() == NATION_OTTOMAN
                || entry.getNation().getId() == NATION_EGYPT
                || entry.getSubject().getId() == NATION_MOROCCO
                || entry.getSubject().getId() == NATION_OTTOMAN
                || entry.getSubject().getId() == NATION_EGYPT)) {
            //entry.setAnnouncement(true);
            maxPics = TOT_NEWS_PICS_ALT[entry.getType()];
        }

        // choose pic
        entry.setBaseNewsId(1 + ((entry.getNewsId() + usedPics[entry.getType()]) % maxPics));
    }

    protected List<News> prepareDemographics(final Game thisGame, final Nation thisNation, final int turnInt) {
        // Add demographics
        final List<News> demographics = new ArrayList<News>();
        final List<Region> lstRegions = regionManager.list();

        // Report Europe first
        lstRegions.remove(0);
        final Report europeRep = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, E_POP_SIZE + EUROPE);
        if (europeRep != null) {
            final int totPop = Integer.parseInt(europeRep.getValue());
            final News entry = new News();
            entry.setText(REGION_IMG[EUROPE] + "Europe");
            entry.setBaseNewsId(totPop);
            entry.setNewsId(europeRep.getId());
            demographics.add(entry);
        }

        // Report European population by type
        for (int type = 0; type <= 2; type++) {
            final Report popTypeRep = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, E_POP_TYPE + type);
            if (popTypeRep != null) {
                final int totPop = Integer.parseInt(popTypeRep.getValue());
                final News entry = new News();
                entry.setText("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + POP_TYPES[type]);
                entry.setBaseNewsId(totPop);
                entry.setNewsId(popTypeRep.getId());
                demographics.add(entry);
            }
        }

        // Report the other regions
        for (final Region region : lstRegions) {
            final Report thisRep = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, E_POP_SIZE + region.getId());
            if (thisRep != null) {
                final int totPop = Integer.parseInt(thisRep.getValue());
                final News entry = new News();
                entry.setText(REGION_IMG[region.getId()] + region.getName());
                entry.setBaseNewsId(totPop);
                entry.setNewsId(region.getId());
                demographics.add(entry);
            }
        }

        return demographics;
    }

    protected List<News> prepareArmyRegionStatistics(final Game thisGame, final Nation thisNation, final int turnInt) {
        final List<Region> lstRegions = regionManager.list();

        // Add army statistics
        final List<News> statistics = new ArrayList<News>();

        for (final Region region : lstRegions) {
            final Report thisRep = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, A_TOT_SLDR_REG + region.getId());
            if (thisRep != null) {
                final int totPop = Integer.parseInt(thisRep.getValue());
                final News entry = new News();
                entry.setText(REGION_IMG[region.getId()] + "Army in " + region.getName());
                entry.setBaseNewsId(totPop);
                entry.setNewsId(region.getId());
                statistics.add(entry);
            }
        }

        return statistics;
    }

    protected List<News> prepareArmyTypeStatistics(final Game thisGame, final Nation thisNation, final int turnInt) {
        // Add army statistics
        final List<News> statistics = new ArrayList<News>();

        for (int type = 0; type < 5; type++) {
            final Report thisRep = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, A_TOT_SLDR_TPE + ARMY_TYPES[type]);
            int totPop = 0;
            if (thisRep != null) {
                totPop += Integer.parseInt(thisRep.getValue());
            }

            final News entry = new News();
            entry.setText(ARMY_TYPES_DESCR[type]);
            entry.setBaseNewsId(totPop);
            statistics.add(entry);
        }

        final Report colCav1 = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, A_TOT_SLDR_TPE + ARMY_TYPES[5]);
        final Report colCav2 = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, A_TOT_SLDR_TPE + ARMY_TYPES[6]);
        int totColCav = 0;
        if (colCav1 != null) {
            totColCav += Integer.parseInt(colCav1.getValue());
        }
        if (colCav2 != null) {
            totColCav += Integer.parseInt(colCav2.getValue());
        }

        final News entry = new News();
        entry.setText(ARMY_TYPES_DESCR[5]);
        entry.setBaseNewsId(totColCav);
        statistics.add(entry);

        return statistics;
    }

    protected List<News> prepareArmyCostsStatistics(final Game thisGame, final Nation thisNation, final int turnInt) {
        // Add army statistics
        final List<News> statistics = new ArrayList<News>();

        for (int type = 0; type < 9; type++) {
            final Report thisRep = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, ARMY_STATS[type]);
            int totPop = 0;
            if (thisRep != null) {
                totPop = Integer.parseInt(thisRep.getValue());
            }

            final News entry = new News();
            entry.setText(ARMY_STATS_DESCR[type]);
            entry.setBaseNewsId(totPop);
            statistics.add(entry);
        }

        return statistics;
    }

    protected List<News> prepareNavyRegionStatistics(final Game thisGame, final Nation thisNation, final int turnInt) {
        final List<Region> lstRegions = regionManager.list();

        // Add army statistics
        final List<News> statistics = new ArrayList<News>();

        for (final Region region : lstRegions) {
            final List<Ship> lstShips = getShipManager().listGameNationRegion(thisGame, thisNation, region);
            int totWarships = 0;
            int totMerchants = 0;
            for (final Ship ship : lstShips) {
                if (ship.getCapturedByNation() == 0
                        && (ship.getCondition() > 0)
                        && (ship.getMarines() > 0)) {
                    if (ship.getType().getShipClass() == 0) {
                        totMerchants++;
                    } else {
                        totWarships++;
                    }
                }
            }

            final News entryW = new News();
            entryW.setText("<img src='http://static.eaw1805.com/images/buttons/icons/formations/warship.png' height=15 title='War Ships' border=0 style='vertical-align: middle;'>" + REGION_IMG[region.getId()] + "WarShips in " + region.getName());
            entryW.setBaseNewsId(totWarships);
            entryW.setNewsId(region.getId());
            statistics.add(entryW);

            final News entryM = new News();
            entryM.setText("<img src='http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png' height=15 title='Merchant Ships' border=0 style='vertical-align: middle;'>" + REGION_IMG[region.getId()] + "Merchants in " + region.getName());
            entryM.setBaseNewsId(totMerchants);
            entryM.setNewsId(region.getId());
            statistics.add(entryM);
        }

        return statistics;
    }

    protected List<News> prepareNavyTypeStatistics(final Game thisGame, final Nation thisNation, final int turnInt) {
        // Add army statistics
        final List<News> statistics = new ArrayList<News>();

        for (int type = 0; type < 3; type++) {
            final Report thisRep = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, NAVY_TYPES[type]);
            int totPop = 0;
            if (thisRep != null) {
                totPop = Integer.parseInt(thisRep.getValue());
            }

            final News entry = new News();
            entry.setText(NAVY_TYPES_DESCR[type]);
            entry.setBaseNewsId(totPop);
            statistics.add(entry);
        }

        return statistics;
    }

    protected List<News> prepareNavyCostsStatistics(final Game thisGame, final Nation thisNation, final int turnInt) {
        // Add army statistics
        final List<News> statistics = new ArrayList<News>();

        for (int type = 0; type < 8; type++) {
            final Report thisRep = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, NAVY_STATS[type]);
            int totPop = 0;
            if (thisRep != null) {
                totPop = Integer.parseInt(thisRep.getValue());
            }

            final News entry = new News();
            entry.setText(NAVY_STATS_DESCR[type]);
            entry.setBaseNewsId(totPop);
            statistics.add(entry);
        }

        return statistics;
    }


    /**
     * Retrieve the victory points of the country.
     *
     * @param thisGame   the game to look up.
     * @param thisNation the nation to look up.
     * @param turnInt    the turn number.
     * @return the victory points of the country.
     */
    protected int retrieveVPs(final Game thisGame, final Nation thisNation, final int turnInt) {
        final Report thisRep = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, N_VP);
        int totVP = 0;
        if (thisRep != null) {
            totVP = Integer.parseInt(thisRep.getValue());
        }

        return totVP;
    }

    protected final String convertRank(final int rank) {
        final StringBuilder strBld = new StringBuilder();
        if (rank == 0) {
            strBld.append("-");
        } else {
            strBld.append(rank);
            if (rank >= 10 && rank <= 14) {
                strBld.append("th");
            } else {
                switch ((rank % 10) + 1) {

                    case 2:
                        strBld.append("st");
                        break;

                    case 3:
                        strBld.append("nd");
                        break;

                    case 4:
                        strBld.append("rd");
                        break;

                    default:
                        strBld.append("th");
                        break;
                }
            }
        }

        return strBld.toString();
    }

    /**
     * Go through the list until you find the nation we are looking for.
     *
     * @param key        -- the key used to rank nations.
     * @param thisGame   -- the game.
     * @param thisNation -- the nation we are looking for.
     * @return the position of the nation on the link.
     */
    protected final int getPosition(final String key, final Game thisGame, final Nation thisNation) {
        final List<Nation> ranked = reportManager.rankNations(key, thisGame, thisGame.getTurn() - 1);
        if (ranked.isEmpty()) {
            return 0;
        }
        int rank = 1;
        for (final Nation nation : ranked) {
            if (nation.getId() == thisNation.getId()) {
                break;
            }
            rank++;
        }

        return rank;
    }

    /**
     * Instance ReportManager class to perform queries
     * about report objects.
     */
    private transient ReportManagerBean reportManager;

    /**
     * Setter method used by spring to inject a reportManager bean.
     *
     * @param injReportManager a reportManager bean.
     */
    public void setReportManager(final ReportManagerBean injReportManager) {
        reportManager = injReportManager;
    }

    /**
     * Instance NewsManager class to perform queries
     * about news objects.
     */
    private transient NewsManagerBean newsManager;

    /**
     * Setter method used by spring to inject a newsManager bean.
     *
     * @param injNewsManager a newsManager bean.
     */
    public void setNewsManager(final NewsManagerBean injNewsManager) {
        newsManager = injNewsManager;
    }

    /**
     * Instance RegionManagerBean class to perform queries
     * about region objects.
     */
    private transient RegionManagerBean regionManager;

    /**
     * Setter method used by spring to inject a RegionManager bean.
     *
     * @param injRegionManager a RegionManager bean.
     */
    public void setRegionManager(final RegionManagerBean injRegionManager) {
        regionManager = injRegionManager;
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
