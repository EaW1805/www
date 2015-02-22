package com.eaw1805.www.controllers.user;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.GameConstants;
import com.eaw1805.data.constants.ProfileConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.managers.beans.EngineProcessManagerBean;
import com.eaw1805.data.managers.beans.GameManagerBean;
import com.eaw1805.data.managers.beans.GoodManagerBean;
import com.eaw1805.data.managers.beans.NationManagerBean;
import com.eaw1805.data.managers.beans.NewsManagerBean;
import com.eaw1805.data.managers.beans.PlayerOrderManagerBean;
import com.eaw1805.data.managers.beans.PostMessageManagerBean;
import com.eaw1805.data.managers.beans.ProfileManagerBean;
import com.eaw1805.data.managers.beans.ReportManagerBean;
import com.eaw1805.data.model.Achievement;
import com.eaw1805.data.model.ActiveUser;
import com.eaw1805.data.model.Follow;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.News;
import com.eaw1805.data.model.Profile;
import com.eaw1805.data.model.Report;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.data.model.WatchGame;
import com.eaw1805.www.commands.PaypalCommand;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.data.cache.Cachable;
import com.eaw1805.www.controllers.cache.helper.GameHelperBean;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.*;

/**
 * REST Controller for displaying user information.
 */
@SuppressWarnings("restriction")
@org.springframework.stereotype.Controller
public class UserHomeController
        extends BaseController
        implements ReportConstants, ProfileConstants {

    public static final Map<String, String> STAT_MESSAGES = new HashMap<String, String>();

    private static final int MAX_WATCHED_NEWS = 16;

    static {
        STAT_MESSAGES.put(N_VP, "Victory points");
        STAT_MESSAGES.put("taxation", "Best game economy");
        STAT_MESSAGES.put("population.size", "Largest population");
        STAT_MESSAGES.put(E_SEC_SIZE_TOT, "Largest country");
        STAT_MESSAGES.put(A_TOT_BAT, "Largest army");
        STAT_MESSAGES.put(S_TOT_SHIPS, "Largest navy");
        STAT_MESSAGES.put(A_TOT_KILLS, "Most casualties inflicted");
        STAT_MESSAGES.put(A_TOT_DEATHS, "Most casualties suffered");
        STAT_MESSAGES.put(S_SINKS, "Most ship sinks/captures");
        STAT_MESSAGES.put(S_SINKED, "Most ships sunk");
    }

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(UserHomeController.class);

    @ModelAttribute("paypalCommand")
    public PaypalCommand paypalCommand() {
        return new PaypalCommand();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/games")
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        ScenarioContextHolder.defaultScenario();

        // retrieve user
        final User thisUser = getUser();

        // Setup map for holding values to be passed to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // Identify if player has an active solo-game
        refData.put("scenario1804", 0);
        refData.put("scenario1804date", "January 1804");
        refData.put("scenario1804waiting", 0);

        // Check if player has a Scenario 1804 game
        ScenarioContextHolder.setScenario(HibernateUtil.DB_FREE);

        final List<UserGame> lstGames = getUserGameManager().list(thisUser);
        if (!lstGames.isEmpty()) {
            final UserGame userGame = lstGames.get(0);
            if (userGame.getGame().getTurn() > -1) {
                refData.put("scenario1804", userGame.getGame().getGameId());
                refData.put("scenario1804date", formatCalendar(calendar(userGame.getGame())));

            } else {
                refData.put("scenario1804waiting", 1);
            }
        }

        // Switch back to default DB
        ScenarioContextHolder.defaultScenario();

        // Retrieve list of games
        final List<Game> gameList = new ArrayList<Game>();
        for (int db = ScenarioContextHolder.FIRST_SCENARIO; db <= ScenarioContextHolder.LAST_SCENARIO; db++) {
            ScenarioContextHolder.setScenario(db);
            List<Game> games = gameManager.list();
            games.remove(0);
            gameList.addAll(games); // remove "Scenario" entry
        }

        // Retrieve list of nations
        final List<Nation> nationList = nationManager.list();
        nationList.remove(0); // remove "Free Nation" entry

        // Calculate calendar dates for each game
        final Map<Integer, String> dates = new HashMap<Integer, String>();
        for (final Game game : gameList) {
            final Calendar thatCal = calendar(game);
            dates.put(game.getGameId(), formatCalendar(thatCal));
        }

        //get users that this person follows.
        final List<Follow> following = followManager.listByFollower(thisUser, false);
        final List<User> followingList = new ArrayList<User>();
        final Set<Integer> followingSet = new HashSet<Integer>();
        final Map<Integer, User> userMap = new HashMap<Integer, User>();
        int count = 0;
        for (final Follow follow : following) {
            count++;
            if (count <= 10) {
                followingList.add(follow.getLeader());
            }
            followingSet.add(follow.getLeader().getUserId());
            userMap.put(follow.getLeader().getUserId(), follow.getLeader());
        }

        //get this user followers.
        final List<Follow> followers = followManager.listByLeader(thisUser, false);
        count = 0;
        final List<User> followersList = new ArrayList<User>();
        for (final Follow follow : followers) {
            count++;
            if (count > 10) {
                break;
            }
            followersList.add(follow.getFollower());
        }

        // List more recent achievements acquired by these players
        List<Achievement> leaderAchievements = new ArrayList<Achievement>();
        for (Follow follow : following) {
            leaderAchievements.addAll(getAchievementManager().listRecent(follow.getLeader().getUserId()));
        }

        // make sure we do not provide more than 18 entries
        if (leaderAchievements.size() > 18) {
            leaderAchievements = leaderAchievements.subList(0, 18);
        }

        // Sort achievements based on most recent one
        Collections.sort(leaderAchievements, new Comparator<Achievement>() {
            public int compare(final Achievement a1, final Achievement a2) {
                return Integer.compare(a2.getAchievementID(), a1.getAchievementID());
            }
        });

        // retrieve games watched
        final List<WatchGame> watchedGames = new ArrayList<WatchGame>();
        for (int db = ScenarioContextHolder.FIRST_SCENARIO; db <= ScenarioContextHolder.LAST_SCENARIO; db++) {
            ScenarioContextHolder.setScenario(db);
            watchedGames.addAll(watchGameManager.listByUser(thisUser));
        }

        // keep only the game objects
        final List<Game> watchGames = new ArrayList<Game>();
        for (final WatchGame watchGame : watchedGames) {
            watchGames.add(watchGame.getGame());
        }

        // Retrieve Profile Entries
        final Profile turnsPlayed = profileManager.getByOwnerKey(thisUser, ProfileConstants.TURNS_PLAYED);
        final Profile turnsPlayedSolo = profileManager.getByOwnerKey(thisUser, ProfileConstants.TURNS_PLAYED_SOLO);

        // the games of the player
        final List<UserGame> userGameslist = new ArrayList<UserGame>();
        final List<UserGame> pendingGames = new ArrayList<UserGame>();
        final Map<Object, List<BigInteger>> gameTotalOrders = new HashMap<Object, List<BigInteger>>();

        // Lookup all game scenarios
        for (int db = ScenarioContextHolder.FIRST_SCENARIO; db <= ScenarioContextHolder.LAST_SCENARIO; db++) {
            ScenarioContextHolder.setScenario(db);
            userGameslist.addAll(userGameManager.listAccepted(thisUser));
            pendingGames.addAll(userGameManager.listPending(thisUser));
            gameTotalOrders.putAll(playerOrderManager.countOrders());
        }

        // Do not show invitations for admin account
        if (thisUser.getUserId() == 2) {
            pendingGames.clear();
        }

        // Retrieve owners of custom games
        final Map<Integer, User> pendingUsers = new HashMap<Integer, User>();
        for (UserGame pending : pendingGames) {
            pendingUsers.put(pending.getGame().getUserId(), userManager.getByID(pending.getGame().getUserId()));
        }

        // Short games based on activity
        final Set<UserGame> userGames = new TreeSet<UserGame>(new Comparator<UserGame>() {

            public int compare(final UserGame thisGame, final UserGame thatGame) {
                return Integer.compare(thisGame.getGame().getGameId(), thatGame.getGame().getGameId());
            }
        });

        // examine games and produce sparkl bars, sort based on activity and check if orders have
        final Map<Integer, Boolean> userGameOrders = new HashMap<Integer, Boolean>();
        final Map<Integer, Integer> mapDeadTurns = new HashMap<Integer, Integer>();
        final Map<Integer, Map<Integer, List<Long>>> gameToNationToStats = new HashMap<Integer, Map<Integer, List<Long>>>();
        for (final UserGame userGame : userGameslist) {
            ScenarioContextHolder.setScenario(userGame.getGame().getScenarioId());

            // ignore dropped positions
            if (userGame.isActive() || !userGame.isAlive()) {
                userGames.add(userGame);

                if (gameToNationToStats.containsKey(userGame.getGame().getGameId())) {
                    gameToNationToStats.get(userGame.getGame().getGameId())
                            .put(userGame.getNation().getId(),
                                    playerOrderManager.countOrdersPerGameNation(userGame.getGame().getGameId(),
                                            userGame.getNation().getId()));

                } else {
                    final Map<Integer, List<Long>> tempMap = new HashMap<Integer, List<Long>>();
                    tempMap.put(userGame.getNation().getId(),
                            playerOrderManager.countOrdersPerGameNation(userGame.getGame().getGameId(), userGame.getNation().getId()));
                    gameToNationToStats.put(userGame.getGame().getGameId(), tempMap);

                    if (playerOrderManager.listByGameNation(userGame.getGame(), userGame.getNation(), userGame.getGame().getTurn()).isEmpty()) {
                        userGameOrders.put(userGame.getGame().getGameId(), false);

                    } else {
                        userGameOrders.put(userGame.getGame().getGameId(), true);
                    }
                }

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
            }
        }

        // Check if friends are online
        final List<User> connectedFriends = new ArrayList<User>();
        final List<ActiveUser> activeUserList = activeUsers();
        for (ActiveUser activeUser : activeUserList) {
            if (followingSet.contains(activeUser.getUserId())) {
                connectedFriends.add(userMap.get(activeUser.getUserId()));
            }
        }

        // Prepare data to pass to jsp
        refData.put("user", thisUser);
        refData.put("pendingGames", pendingGames);
        refData.put("pendingUsers", pendingUsers);
        refData.put("userDateJoined", new Date(thisUser.getDateJoin() * 1000));
        refData.put("profileUser", thisUser);
        refData.put("followingCnt", following.size());
        refData.put("followingList", followingList);
        refData.put("followingAchievements", leaderAchievements);
        refData.put("followersCnt", followers.size());
        refData.put("followersList", followersList);
        refData.put("watchCnt", watchedGames.size());
        refData.put("gameList", watchGames);
        refData.put("watchNews", produceWatchedNews(watchedGames));
        refData.put("nationList", nationList);
        refData.put("userGames", userGames);
        refData.put("userGameVps", producePositionVPs(thisUser));
        refData.put("userGameModifier", produceGameModifiers(thisUser));
        refData.put("userGameOrders", userGameOrders);
        refData.put("userGamesDeadTurns", mapDeadTurns);
        refData.put("dates", dates);
        refData.put("activityStat", gameToNationToStats);
        refData.put("globalActivityStat", gameTotalOrders);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        refData.put("userGameStats", producePositionStats(thisUser));
        refData.put("gameStatsMessages", STAT_MESSAGES);

        refData.put("profileStats", gameHelper.prepareUserStatistics(thisUser));
        refData.put("forumStats", gameHelper.getUserForumStatistics(thisUser));
        refData.put("empireKeys", GameHelperBean.EMPIRE_STATISTICS);
        refData.put("politicsKeys", GameHelperBean.POLITICS_STATISTICS);
        refData.put("warfareKeys", GameHelperBean.WARFARE_STATISTICS);
        refData.put("undefinedInt", GameHelperBean.UNDEFINED_VALUE);

        refData.put("connectedFriends", connectedFriends);

        refData.put("postMessages", postMessageManager.listByUser(thisUser.getUserId()));

        if (turnsPlayed != null) {
            LOGGER.debug("turnsPlayed - null");
            refData.put("turnsPlayed", turnsPlayed.getValue());

        } else {
            LOGGER.debug("turnsPlayed - 0");
            refData.put("turnsPlayed", 0);
        }

        if (turnsPlayedSolo != null) {
            LOGGER.debug("turnsPlayedSolo - null");
            refData.put("turnsPlayedSolo", turnsPlayedSolo.getValue());

        } else {
            LOGGER.debug("turnsPlayedSolo - 0");
            refData.put("turnsPlayedSolo", 0);
        }

        final List<Integer> playedGames = new ArrayList<Integer>();

        for (int db = ScenarioContextHolder.FIRST_SCENARIO; db <= ScenarioContextHolder.LAST_SCENARIO; db++) {
            playedGames.addAll(userGameManager.listPlayedGames(thisUser));
        }

        final Map<String, Object> freePlayedNationsData = gameHelper.getAllFreePlayedNations();
        refData.put("monthsSmall", freePlayedNationsData.get("monthsOneLetter"));

        final LinkedList<LinkedList<Object>> freePlayedNations =
                (LinkedList<LinkedList<Object>>) freePlayedNationsData.get("freeNations");

        final LinkedList<LinkedList<Object>> freeNations = new LinkedList<LinkedList<Object>>();

        for (final LinkedList<Object> freePlayedNation : freePlayedNations) {
            if (!playedGames.contains(((Game) freePlayedNation.get(0)).getGameId())) {
                freeNations.add(freePlayedNation);
            }
        }
        refData.put("freeNations", freeNations);

        refData.putAll(gameHelper.getAllNewNations());
        refData.put("specialOffer", freePlayedNationsData.get("specialOffer"));

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Home/User");

        //This is an old user
        if (userGames.size() > 0 || playedGames.size() > 0) {
            return new ModelAndView("user/user", refData);

        } else {
            //New user
            return new ModelAndView("user/newUser", refData);
        }

    }

    /**
     * Retrieve latest news items from watched games.
     *
     * @param watchedGames the list of games watched.
     * @return a list of most recent news entries.
     */
    @Cachable(cacheName = "gameCache")
    public List<News> produceWatchedNews(final List<WatchGame> watchedGames) {
        final TreeMap<Long, List<News>> sortedNews = new TreeMap<Long, List<News>>();

        // Inspect all games watched
        for (WatchGame watchedGame : watchedGames) {
            ScenarioContextHolder.setScenario(watchedGame.getGame().getScenarioId());

            // Add news from all watched games.
            final List<News> lstGameNews = newsManager.listGame(watchedGame.getGame(), watchedGame.getGame().getTurn() - 1);

//            // Add one entry for each process
//            final News entry1 = new News();
//            entry1.setGame(watchedGame.getGame());
//
            LOGGER.debug("Watching Game " + watchedGame.getGame().getGameId() + " / Turn " + (watchedGame.getGame().getTurn() - 1) + " (Scenario " + watchedGame.getGame().getScenarioId() + ") - news: " + lstGameNews.size());

            // Sort entries based on date
            sortedNews.put(watchedGame.getGame().getDateLastProc().getTime(), lstGameNews);
        }

        final List<News> lstNews = new ArrayList<News>();
        for (List<News> entries : sortedNews.descendingMap().values()) {
            for (News entry : entries) {
                lstNews.add(entry);

                // no more than MAX_WATCHED_NEWS entries
                if (lstNews.size() >= MAX_WATCHED_NEWS) {
                    break;
                }
            }

            // no more than MAX_WATCHED_NEWS entries
            if (lstNews.size() >= MAX_WATCHED_NEWS) {
                break;
            }
        }

        LOGGER.info("Watched news: " + lstNews.size());

        return lstNews;
    }

    /**
     * Retrieve Statistics per user game.
     *
     * @param thisUser the user to inspect.
     * @return a map of statistics of the player's position.
     */
    @Cachable(cacheName = "gameCache")
    public Map<Integer, Map<String, String>> producePositionStats(final User thisUser) {

        final List<UserGame> lstUserGames = new ArrayList<UserGame>();
        for (int db = ScenarioContextHolder.FIRST_SCENARIO; db <= ScenarioContextHolder.LAST_SCENARIO; db++) {
            ScenarioContextHolder.setScenario(db);
            lstUserGames.addAll(userGameManager.listAccepted(thisUser));
        }

        final Map<Integer, Map<String, String>> userGameStats = new HashMap<Integer, Map<String, String>>();

        for (final UserGame userGame : lstUserGames) {
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

            userGameStats.put(userGame.getGame().getGameId(), sortByValue(unSortedMap));
        }

        return userGameStats;
    }

    /**
     * Retrieve VPs per user game.
     *
     * @param thisUser the user to inspect.
     * @return a map of games with VPs of the player's position.
     */
    @Cachable(cacheName = "gameCache")
    public Map<Integer, Integer> producePositionVPs(final User thisUser) {
        final List<UserGame> lstUserGames = new ArrayList<UserGame>();
        for (int db = ScenarioContextHolder.FIRST_SCENARIO; db <= ScenarioContextHolder.LAST_SCENARIO; db++) {
            ScenarioContextHolder.setScenario(db);
            lstUserGames.addAll(userGameManager.listAccepted(thisUser));
        }

        final HashMap<Integer, Integer> userGameVps = new HashMap<Integer, Integer>();
        for (final UserGame userGame : lstUserGames) {
            userGameVps.put(userGame.getGame().getGameId(), retrieveVPs(userGame.getGame(), userGame.getNation(), userGame.getGame().getTurn() - 1));
        }

        return userGameVps;
    }

    /**
     * Retrieve VPs per user game.
     *
     * @param thisUser the user to inspect.
     * @return a map of games with VPs of the player's position.
     */
    @Cachable(cacheName = "gameCache")
    public Map<Integer, Double> produceGameModifiers(final User thisUser) {
        final List<UserGame> lstUserGames = new ArrayList<UserGame>();
        for (int db = ScenarioContextHolder.FIRST_SCENARIO; db <= ScenarioContextHolder.LAST_SCENARIO; db++) {
            ScenarioContextHolder.setScenario(db);
            lstUserGames.addAll(userGameManager.listAccepted(thisUser));
        }

        final HashMap<Integer, Double> userGameModifier = new HashMap<Integer, Double>();
        for (final UserGame userGame : lstUserGames) {
            // check duration of game
            final double modifier;
            switch (userGame.getGame().getType()) {
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

            userGameModifier.put(userGame.getGame().getGameId(), modifier);
        }

        return userGameModifier;
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

    public final List<LinkedList<Object>> reportNations(final Game game, final String category, final List<Integer> nations) {
        final ArrayList<LinkedList<Object>> nationsMap = new ArrayList<LinkedList<Object>>();
        final List<Report> lstReports = reportManager.listByTurnKey(game, game.getTurn() - 1, category);
        for (final Report report : lstReports) {
            if (nations.contains(report.getNation().getId())) {
                final LinkedList<Object> nationReport = new LinkedList<Object>();
                nationReport.addFirst(gameManager.getByID(game.getGameId()));
                nationReport.addLast(nationManager.getByID(report.getNation().getId()));
                nationReport.addLast(Integer.parseInt(report.getValue()));
                nationsMap.add(nationReport);
            }
        }
        return nationsMap;
    }

    /**
     * Instance GameManager class to perform queries
     * about game objects.
     */
    private transient GameManagerBean gameManager;

    /**
     * Setter method used by spring to inject a gameManager bean.
     *
     * @param injGameManager a gameManager bean.
     */
    public void setGameManager(final GameManagerBean injGameManager) {
        gameManager = injGameManager;
    }

    /**
     * Instance NationManager class to perform queries
     * about nation objects.
     */
    private transient NationManagerBean nationManager;

    /**
     * Setter method used by spring to inject a nationManager bean.
     *
     * @param injNationManager a nationManager bean.
     */
    public void setNationManager(final NationManagerBean injNationManager) {
        nationManager = injNationManager;
    }

    /**
     * Instance PlayerOrderManager class to perform queries
     * about playerOrder objects.
     */
    private transient PlayerOrderManagerBean playerOrderManager;

    /**
     * Setter method used by spring to inject a PlayerOrderManager bean.
     *
     * @param injPlayerOrderManager a playerOrderManager bean.
     */
    public void setPlayerOrderManager(final PlayerOrderManagerBean injPlayerOrderManager) {
        playerOrderManager = injPlayerOrderManager;
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
     * Instance GoodManager class to perform queries
     * about Good objects.
     */
    private transient GoodManagerBean goodManager;

    /**
     * Setter method used by spring to inject a GoodManager bean.
     *
     * @param injGoodManager a GoodManager bean.
     */
    public void setGoodManager(final GoodManagerBean injGoodManager) {
        goodManager = injGoodManager;
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
     * Instance EngineProcessManager class to perform queries
     * about engineProcess objects.
     */
    private transient EngineProcessManagerBean engineManager;

    /**
     * Setter method used by spring to inject a EngineProcessManagerBean bean.
     *
     * @param injEngineManager a EngineProcessManagerBean bean.
     */
    public void setEngineProcessManager(final EngineProcessManagerBean injEngineManager) {
        engineManager = injEngineManager;
    }

    /**
     * Instance ProfileManager class to perform queries
     * about Profile objects.
     */
    private transient ProfileManagerBean profileManager;

    /**
     * Setter method used by spring to inject a ProfileManagerBean bean.
     *
     * @param value a ProfileManagerBean bean.
     */
    public void setProfileManager(final ProfileManagerBean value) {
        this.profileManager = value;
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

    PostMessageManagerBean postMessageManager;

    public void setPostMessageManager(PostMessageManagerBean postMessageManager) {
        this.postMessageManager = postMessageManager;
    }
}
