package com.eaw1805.www.controllers.user;

import com.eaw1805.data.cache.Cachable;
import com.eaw1805.data.constants.GameConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.managers.beans.GameManagerBean;
import com.eaw1805.data.managers.beans.NationManagerBean;
import com.eaw1805.data.managers.beans.ReportManagerBean;
import com.eaw1805.data.managers.beans.UserGameManagerBean;
import com.eaw1805.data.model.Achievement;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.Report;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Controller
public class AchievementViewController
        extends BaseController
        implements ReportConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(AchievementViewController.class);

    /**
     * This handler marks a given achievement as viewed so it will not be displayed in the future.
     *
     * @param achievementId The achievement id to search for.
     * @return Nothing needs to be returned here.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/achievement/{achievementId}/viewed")
    public String processAcceptInvitation(@PathVariable("achievementId") String achievementId,
                                          final HttpServletRequest request,
                                          final HttpServletResponse response) {
        //allow cross domain access via ajax.
        try {
            response.setHeader("Access-Control-Allow-Origin", "http://forum.eaw1805.com");
            response.setHeader("Access-Control-Allow-Credentials", "true");

            //then retrieve achievement and mark it as viewed.
            Achievement achievement = getAchievementManager().getByID(Integer.parseInt(achievementId));
            achievement.setFirstLoad(true);
            getAchievementManager().update(achievement);
            LOGGER.info("ACHIEVEMENT MARKED AS VIEWED " + achievementId);

            //return something...
            response.getWriter().write("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This handler retrieves all achievements for a user
     *
     * @return Nothing needs to be returned here.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/achievements/retrieve")
    public String retrieveAchievements() {
        //there is nothing to do here really..
        // everything is done by the base controller..
        //just return the correct view.
        return "user/achievements";
    }


    /**
     * This handler marks a given achievement as viewed so it will not be displayed in the future.
     *
     * @param userGameId The achievement id to search for.
     * @return Nothing needs to be returned here.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/gameResult/{userGameId}/viewed")
    public String markUserGameResultAsMarked(@PathVariable("scenarioId") String scenarioId,
                                             @PathVariable("userGameId") String userGameId,
                                             final HttpServletRequest request,
                                             final HttpServletResponse response) {
        ScenarioContextHolder.setScenario(scenarioId);

        try {
            //allow cross domain access via ajax.
            response.setHeader("Access-Control-Allow-Origin", "http://forum.eaw1805.com");
            response.setHeader("Access-Control-Allow-Credentials", "true");

            //then retrieve achievement and mark it as viewed.
            final UserGame userGame = getUserGameManager().getByID(Integer.parseInt(userGameId));
            userGame.setResultViewed(true);
            getUserGameManager().update(userGame);


            LOGGER.info("USER_GAME MARKED AS VIEWED " + userGameId);

            //return something...
            response.getWriter().write("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void calculateResults(final List<UserGame> userGames, final Map<String, Object> refData) {
        // Calculate calendar dates for each game
        final Map<Integer, Map<String, String>> userGameStats = new HashMap<Integer, Map<String, String>>();
        final Map<Integer, String> dates = new HashMap<Integer, String>();
        final Map<Integer, Integer> mapDeadTurns = new HashMap<Integer, Integer>();
        final HashMap<Integer, Integer> userGameVps = new HashMap<Integer, Integer>();
        for (final UserGame userGame : userGames) {
            ScenarioContextHolder.setScenario(userGame.getGame().getScenarioId());
            userGameStats.put(userGame.getGame().getGameId(), sortByValue(producePositionStats(userGame)));
            final Calendar thatCal = calendar(userGame.getGame());
            dates.put(userGame.getGame().getGameId(), formatCalendar(thatCal));

            // detect last turn for dead positions
            if (!userGame.isAlive()) {
                final List<Report> lstNationStatus = reportManager.listByOwnerKey(userGame.getNation(), userGame.getGame(), ReportConstants.N_ALIVE);
                int deadTurn = userGame.getGame().getTurn();
                for (final Report status : lstNationStatus) {
                    if (status.getValue().equals("0")) {
                        deadTurn = java.lang.Math.min(deadTurn, status.getTurn());
                    }
                }

                // store turn
                mapDeadTurns.put(userGame.getGame().getGameId(), deadTurn);
            }

            userGameVps.put(userGame.getGame().getGameId(), retrieveVPs(userGame.getGame(), userGame.getNation(), userGame.getGame().getTurn() - 1));
        }

        refData.put("userGameVps", userGameVps);
        refData.put("userGamesDeadTurns", mapDeadTurns);
        refData.put("userGameStats", userGameStats);
        refData.put("dates", dates);
        refData.put("gameStatsMessages", UserHomeController.STAT_MESSAGES);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/resultImage")
    public ResponseEntity<byte[]> testphoto(@PathVariable("scenarioId") String scenarioId,
                                            @PathVariable("gameId") String gameId,
                                            @PathVariable("nationId") String nationId) throws IOException {

        try {
            ScenarioContextHolder.setScenario(scenarioId);
            final Game thisGame = gameManager.getByID(Integer.parseInt(gameId));
            final Nation thisNation = nationManager.getByID(Integer.parseInt(nationId));

//        AchievementViewController view = new AchievementViewController();

            //calculate data to fill in the image.
            final Map<String, Object> refData = new HashMap<String, Object>();
            final List<UserGame> userGames = userGameManager.list(thisGame, thisNation);
            calculateResults(userGames, refData);
            Map<Integer, Integer> userGameVps = (Map<Integer, Integer>) refData.get("userGameVps");
            Map<Integer, Integer> userGamesDeadTurns = (Map<Integer, Integer>) refData.get("userGamesDeadTurns");
            System.out.println("dates? " + refData.get("dates"));
            Map<Integer, String> dates = (Map<Integer, String>) refData.get("dates");
            Map<Integer, Map<String, String>> userGameStats = (Map<Integer, Map<String, String>>) refData.get("userGameStats");
            final UserGame userGameResult = userGames.get(0);
            //now depending on the data, retrieve the correct image.
            BufferedImage image = null;

            if (userGameResult.getGame().getWinners() != null && userGameResult.getGame().getWinners().contains("*" + userGameResult.getNation().getId() + "*")) {
                image = ImageIO.read(new URL(
                        "http://static.eaw1805.com/images/panels/gameResults/winner.png"));
            } else if (userGameResult.getGame().getRunnerUp() != null && userGameResult.getGame().getRunnerUp().contains("*" + userGameResult.getNation().getId() + "*")) {
                image = ImageIO.read(new URL(
                        "http://static.eaw1805.com/images/panels/gameResults/runnerUp.png"));
            } else if (userGameResult.getGame().getCoWinners() != null && userGameResult.getGame().getCoWinners().contains("*" + userGameResult.getNation().getId() + "*")) {
                image = ImageIO.read(new URL(
                        "http://static.eaw1805.com/images/panels/gameResults/winner.png"));
            } else {
                if (userGameResult.getTurnDrop() > userGameResult.getTurnPickUp()) {
                    image = ImageIO.read(new URL(
                            "http://static.eaw1805.com/images/panels/gameResults/dropOut.png"));
                } else if (userGameResult.getGame().getEnded()) {
                    if (userGameResult.isHasWon()) {
                        image = ImageIO.read(new URL(
                                "http://static.eaw1805.com/images/panels/gameResults/winner.png"));
                    } else if (userGameResult.isAlive()) {
                        image = ImageIO.read(new URL(
                                "http://static.eaw1805.com/images/panels/gameResults/survivor.png"));
                    } else {
                        image = ImageIO.read(new URL(
                                "http://static.eaw1805.com/images/panels/gameResults/dead.png"));
                    }
                } else if (!userGameResult.isAlive()) {
                    image = ImageIO.read(new URL(
                            "http://static.eaw1805.com/images/panels/gameResults/dead.png"));
                }
            }
            if (image == null) {
                return null;
            }
            final Font font = new Font("Georgia", Font.TRUETYPE_FONT, 40);
            final Font font2 = new Font("Georgia", Font.PLAIN, 26);
            final Font fontSmallBold = new Font("Georgia", Font.BOLD, 13);
            final Font fontSmallHeader = new Font("Arial Black", Font.BOLD, 15);
            final Font fontSmallPlain = new Font("Georgia", Font.BOLD, 13);

            final Font fontBigPlain = new Font("Georgia", Font.BOLD, 25);
            Graphics2D graphics = (Graphics2D) image.getGraphics();

            RenderingHints rh = new RenderingHints(

                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setRenderingHints(rh);

            final int width = image.getWidth();
            FontMetrics metrics = graphics.getFontMetrics(font);
            FontMetrics metrics2 = graphics.getFontMetrics(font2);

            int startG = (width - (metrics.stringWidth("Game 8 ") + metrics2.stringWidth("/ " + dates.get(userGameResult.getGame().getGameId())))) / 2;
            int startD = startG + metrics.stringWidth("Game 8") + 5;

            graphics.setFont(font);

            graphics.setColor(Color.BLACK);

            graphics.drawString("Game 8 ", startG, 250);
            graphics.setFont(font2);
            graphics.drawString("/ " + dates.get(userGameResult.getGame().getGameId()), startD, 250);


            metrics = graphics.getFontMetrics(fontSmallBold);
            graphics.setFont(fontSmallBold);
            graphics.drawString("Scenario " + scenarioId, (width - metrics.stringWidth("Scenario " + scenarioId)) / 2, 280);
            //now retrieve the flag image
            BufferedImage flagImg = ImageIO.read(new URL("http://static.eaw1805.com/images/nations/nation-" + userGameResult.getNation().getId() + "-120.png"));
            graphics.drawImage(flagImg, (width - 200) / 2, 290, 200, 133, null);

            graphics.setFont(fontSmallHeader);
            graphics.setColor(new Color(68, 68, 70));
            graphics.drawString("World position", 100, 320);
            graphics.setFont(fontSmallPlain);
            metrics = graphics.getFontMetrics();
            graphics.setColor(Color.BLACK);
            int count = 0;
            for (Map.Entry<String, String> entry : userGameStats.get(userGameResult.getGame().getGameId()).entrySet()) {
                if (count >= 6) {
                    break;
                }
                count++;

                graphics.drawString(UserHomeController.STAT_MESSAGES.get(entry.getKey()) + "  " + entry.getValue(), 250 - metrics.stringWidth(UserHomeController.STAT_MESSAGES.get(entry.getKey()) + "  " + entry.getValue()), 340 + (count - 1) * 15);
            }

            graphics.setColor(new Color(68, 68, 70));
            graphics.setFont(fontBigPlain);
            graphics.drawString("vp: " + userGameVps.get(userGameResult.getGame().getGameId()), 550, 340);
            DecimalFormat df = new DecimalFormat("#.##");

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

            graphics.drawString(df.format(100.0 * userGameVps.get(userGameResult.getGame().getGameId()) / userGameResult.getNation().getVpWin() * modifier) + "%", 550, 370);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] bytes = baos.toByteArray();
            baos.close();

            System.out.println("Image Created");

//        File fi = new File("/home/karavias/Projects/empire/trunk/dev-web/src/empire/webapp/images/panels/gameResults/survivor.png");
//        byte[] fileContent = Files.readAllBytes(fi.toPath());
//        InputStream in = new FileImageInputStream(new File("strawbefs;rry.jpg"));

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/result/{resultStatus}")
    public String retrieveUserGameResultCustom(@PathVariable("scenarioId") String scenarioId,
                                               @PathVariable("gameId") String gameId,
                                               @PathVariable("nationId") String nationId,
                                               @PathVariable("resultStatus") String status,
                                               final Model model) throws Exception {
//        if (getUser().getUserType() != 3) {
//            //if you are not admin, you don't have persmissions to view this page.
//            throw new InvalidPageException("Page not found");
//        }
        ScenarioContextHolder.setScenario(scenarioId);
        final Game thisGame = gameManager.getByID(Integer.parseInt(gameId));
        final Nation thisNation = nationManager.getByID(Integer.parseInt(nationId));

        final Map<String, Object> refData = new HashMap<String, Object>();
        final List<UserGame> userGames = getUserGameManager().list(thisGame, thisNation);
        calculateResults(userGames, refData);
        model.addAllAttributes(refData);
        model.addAttribute("userGameCustomResults", userGames);
        model.addAttribute("resultStatus", status);
        return "user/userGameResult";

    }

    /**
     * This handler retrieves all achievements for a user
     *
     * @return Nothing needs to be returned here.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/userGamesResult/retrieve")
    public ModelAndView retrieveUserGamesResults() {
        //there is nothing to do here really..
        // everything is done by the base controller..
        //just return the correct view.
        final Map<String, Object> refData = new HashMap<String, Object>();
        calculateResults(getUserGamesWithResult(), refData);
        return new ModelAndView("user/userGameResults", refData);
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
        ScenarioContextHolder.setScenario(thisGame.getScenarioId());
        final Report thisRep = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, N_VP);
        int totVP = 0;
        if (thisRep != null) {
            totVP = Integer.parseInt(thisRep.getValue());
        }

        return totVP;
    }


    /**
     * Retrieve Statistics per user game.
     *
     * @param userGame the user to inspect.
     * @return a map of statistics of the player's position.
     */
    @Cachable(cacheName = "userCache")
    public Map<String, Integer> producePositionStats(final UserGame userGame) {

        final HashMap<String, Integer> unSortedMap = new HashMap<String, Integer>();
        unSortedMap.put(N_VP, getPosition(N_VP, userGame.getGame(), userGame.getNation()));
        unSortedMap.put("taxation", getPosition("taxation", userGame.getGame(), userGame.getNation()));
        unSortedMap.put("population.size", getPosition("population.size", userGame.getGame(), userGame.getNation()));
        unSortedMap.put(E_SEC_SIZE_TOT, getPosition(E_SEC_SIZE_TOT, userGame.getGame(), userGame.getNation()));
        unSortedMap.put(A_TOT_BAT, getPosition(A_TOT_BAT, userGame.getGame(), userGame.getNation()));
        unSortedMap.put(S_TOT_SHIPS, getPositionFixed(S_TOT_SHIPS, userGame.getGame(), userGame.getNation()));
        unSortedMap.put(A_TOT_KILLS, getPosition(A_TOT_KILLS, userGame.getGame(), userGame.getNation()));
        unSortedMap.put(A_TOT_DEATHS, getPosition(A_TOT_DEATHS, userGame.getGame(), userGame.getNation()));
        unSortedMap.put(S_SINKS, getPosition(S_SINKS, userGame.getGame(), userGame.getNation()));
        unSortedMap.put(S_SINKED, getPosition(S_SINKED, userGame.getGame(), userGame.getNation()));
        return unSortedMap;
    }

    /**
     * Sort an <String, Integer> Map by value.
     *
     * @param map the unsorted map.
     * @return the sorted map.
     */
    private Map<String, String> sortByValue(final Map<String, Integer> map) {

        final LinkedList<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(final Map.Entry<String, Integer> o1, final Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        final LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();

        for (final Map.Entry<String, Integer> thisEntry : list) {
            result.put(thisEntry.getKey(), convertRank(thisEntry.getValue()));
        }
        return result;
    }

    /**
     * Convert Rank from int to String.
     *
     * @param rank the input int
     * @return the String Rank
     */
    protected final String convertRank(final int rank) {
        final StringBuilder strBld = new StringBuilder();
        if (rank == 0 || rank == Integer.MAX_VALUE) {
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
        ScenarioContextHolder.setScenario(thisGame.getScenarioId());
        final List<Nation> ranked = reportManager.rankNations(key, thisGame, thisGame.getTurn() - 1);
        if (ranked.isEmpty()) {
            return Integer.MAX_VALUE;
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
     * Go through the list until you find the nation we are looking for.
     *
     * @param key        -- the key used to rank nations.
     * @param thisGame   -- the game.
     * @param thisNation -- the nation we are looking for.
     * @return the position of the nation on the link.
     */
    protected final int getPositionFixed(final String key, final Game thisGame, final Nation thisNation) {
        ScenarioContextHolder.setScenario(thisGame.getScenarioId());
        final List<Nation> ranked = reportManager.rankNationsFixed(key, thisGame, thisGame.getTurn() - 1, 17);
        if (ranked.isEmpty()) {
            return Integer.MAX_VALUE;
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

    public static void main(String[] args) throws Exception {

//        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
//        imageGenerator.loadUrl(new URL("http://localhost:8080/empire-web/scenario/1805/game/8/nation/17/result/winner"));
//        imageGenerator.saveAsImage(new File("/home/karavias/images/test.png"));
//
//        Read Spring content
        final ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{
                "classpath*:empire/webapp/resources/empire-data.xml"});


        // Try to retrieve timesheet
        final GameManagerBean gameManager = (GameManagerBean) ctx.getBean("gameManagerBean");
        final NationManagerBean nationManager = (NationManagerBean) ctx.getBean("nationManagerBean");
        final UserGameManagerBean userGameManager = (UserGameManagerBean) ctx.getBean("usersGamesManagerBean");


        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies = ge.getAvailableFontFamilyNames();
        for (String fontfamily : fontFamilies) {
            System.out.println(fontfamily);
        }


        String scenarioId = "1802";
        String gameId = "5";
        String nationId = "17";
        ScenarioContextHolder.setScenario(scenarioId);
        final Game thisGame = gameManager.getByID(Integer.parseInt(gameId));
        final Nation thisNation = nationManager.getByID(Integer.parseInt(nationId));

//        AchievementViewController view = new AchievementViewController();

        //calculate data to fill in the image.
        final Map<String, Object> refData = new HashMap<String, Object>();
        final List<UserGame> userGames = userGameManager.list(thisGame, thisNation);
//        view.calculateResults(userGames, refData);
        Map<Integer, Integer> userGameVps = (Map<Integer, Integer>) refData.get("userGameVps");
        Map<Integer, Integer> userGamesDeadTurns = (Map<Integer, Integer>) refData.get("userGamesDeadTurns");
        Map<Integer, String> dates = (Map<Integer, String>) refData.get("dates");
        Map<Integer, Map<String, String>> userGameStats = (Map<Integer, Map<String, String>>) refData.get("userGameStats");
        final UserGame userGameResult = userGames.get(0);
        //now depending on the data, retrieve the correct image.
        BufferedImage image = null;

        if (userGameResult.getGame().getWinners() != null && userGameResult.getGame().getWinners().contains("*" + userGameResult.getNation().getId() + "*")) {
            image = ImageIO.read(new URL(
                    "http://static.eaw1805.com/images/panels/gameResults/winner.png"));
        } else if (userGameResult.getGame().getRunnerUp() != null && userGameResult.getGame().getRunnerUp().contains("*" + userGameResult.getNation().getId() + "*")) {
            image = ImageIO.read(new URL(
                    "http://static.eaw1805.com/images/panels/gameResults/runnerUp.png"));
        } else if (userGameResult.getGame().getCoWinners() != null && userGameResult.getGame().getCoWinners().contains("*" + userGameResult.getNation().getId() + "*")) {
            image = ImageIO.read(new URL(
                    "http://static.eaw1805.com/images/panels/gameResults/winner.png"));
        } else {
            if (userGameResult.getTurnDrop() > userGameResult.getTurnPickUp()) {
                image = ImageIO.read(new URL(
                        "http://static.eaw1805.com/images/panels/gameResults/dropOut.png"));
            } else if (userGameResult.getGame().getEnded()) {
                if (userGameResult.isHasWon()) {
                    image = ImageIO.read(new URL(
                            "http://static.eaw1805.com/images/panels/gameResults/winner.png"));
                } else if (userGameResult.isAlive()) {
                    image = ImageIO.read(new URL(
                            "http://static.eaw1805.com/images/panels/gameResults/survivor.png"));
                } else {
                    image = ImageIO.read(new URL(
                            "http://static.eaw1805.com/images/panels/gameResults/dead.png"));
                }
            } else if (!userGameResult.isAlive()) {
                image = ImageIO.read(new URL(
                        "http://static.eaw1805.com/images/panels/gameResults/dead.png"));
            }
        }
        if (image == null) {
            return;
        }
        final Font font = new Font("Georgia", Font.TRUETYPE_FONT, 40);
        final Font font2 = new Font("Georgia", Font.PLAIN, 26);
        final Font fontSmallBold = new Font("Georgia", Font.BOLD, 13);
        final Font fontSmallHeader = new Font("Arial Black", Font.BOLD, 15);
        final Font fontSmallPlain = new Font("Georgia", Font.BOLD, 13);

        final Font fontBigPlain = new Font("Georgia", Font.BOLD, 25);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        RenderingHints rh = new RenderingHints(

                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHints(rh);

        final int width = image.getWidth();
        FontMetrics metrics = graphics.getFontMetrics(font);
        FontMetrics metrics2 = graphics.getFontMetrics(font2);
        int startG = (width - (metrics.stringWidth("Game 8 ") + metrics2.stringWidth("/ January 1808"))) / 2;
        int startD = startG + metrics.stringWidth("Game 8") + 5;

        graphics.setFont(font);

        graphics.setColor(Color.BLACK);

        graphics.drawString("Game 8 ", startG, 250);
        graphics.setFont(font2);
        graphics.drawString("/ January 1808", startD, 250);


        metrics = graphics.getFontMetrics(fontSmallBold);
        graphics.setFont(fontSmallBold);
        graphics.drawString("Scenario " + scenarioId, (width - metrics.stringWidth("Scenario " + scenarioId)) / 2, 280);
        //now retrieve the flag image
        BufferedImage flagImg = ImageIO.read(new URL("http://static.eaw1805.com/images/nations/nation-" + userGameResult.getNation().getId() + "-120.png"));
        graphics.drawImage(flagImg, (width - 200) / 2, 290, 200, 133, null);

        graphics.setFont(fontSmallHeader);
        graphics.setColor(new Color(68, 68, 70));
        graphics.drawString("World position", 100, 320);
        graphics.setFont(fontSmallPlain);
        metrics = graphics.getFontMetrics();
        graphics.setColor(Color.BLACK);
        graphics.drawString("Most Ships Captures  4th", 250 - metrics.stringWidth("Most Ships Captures  4th"), 340);
        graphics.drawString("Most Ships Sunk  4th", 250 - metrics.stringWidth("Most Ships Sunk  4th"), 355);
        graphics.setColor(new Color(68, 68, 70));
        graphics.setFont(fontBigPlain);
        graphics.drawString("vp: 75", 550, 340);
        graphics.drawString("15.62%", 550, 370);
        //draw the date


//        graphics.drawString("test string", 10, 25);


        ImageIO.write(image, "png", new File("/home/karavias/images/test.png"));
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(image, "jpg", baos);
//        baos.flush();
//        byte[] bytes = baos.toByteArray();
//        baos.close();

        System.out.println("Image Created");


//        File fi = new File("/home/karavias/Projects/empire/trunk/dev-web/src/empire/webapp/images/panels/gameResults/survivor.png");
//        byte[] fileContent = Files.readAllBytes(fi.toPath());
//        InputStream in = new FileImageInputStream(new File("strawbefs;rry.jpg"));

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

    }

    @Autowired
    @Qualifier("reportManagerBean")
    private transient ReportManagerBean reportManager;

    @Autowired
    @Qualifier("gameManagerBean")
    private transient GameManagerBean gameManager;

    @Autowired
    @Qualifier("nationManagerBean")
    private transient NationManagerBean nationManager;
}
