package com.eaw1805.www.controllers.cache.helper;

import com.eaw1805.data.cache.Cachable;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.GameConstants;
import com.eaw1805.data.constants.ProfileConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.dto.web.HofDTO;
import com.eaw1805.data.managers.beans.AchievementManagerBean;
import com.eaw1805.data.managers.beans.ForumPostManagerBean;
import com.eaw1805.data.managers.beans.GameManagerBean;
import com.eaw1805.data.managers.beans.NationManagerBean;
import com.eaw1805.data.managers.beans.NewsManagerBean;
import com.eaw1805.data.managers.beans.PlayerOrderManagerBean;
import com.eaw1805.data.managers.beans.ProfileManagerBean;
import com.eaw1805.data.managers.beans.ReportManagerBean;
import com.eaw1805.data.managers.beans.UserGameManagerBean;
import com.eaw1805.data.managers.beans.UserManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.Profile;
import com.eaw1805.data.model.Report;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.data.model.comparators.GameId;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Holds HOF data, and User Statistics.
 */
public class GameHelperBean {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(GameHelperBean.class);

    /**
     * Our instance of the GameHelperBean.
     */
    private static GameHelperBean ourInstance = null;

    /**
     * Instance ProfileManager class to perform queries
     * about profile objects.
     */
    protected transient ProfileManagerBean profileManager;

    /**
     * Instance UserGameManager class to perform queries
     * about usergame objects.
     */
    protected transient UserGameManagerBean userGameManager;

    /**
     * Instance of UserManager class.
     */
    protected transient UserManagerBean userManager;

    /**
     * Instance GameManager class to perform queries
     * about game objects.
     */
    private transient GameManagerBean gameManager;

    /**
     * Instance NationManager class to perform queries
     * about nation objects.
     */
    private transient NationManagerBean nationManager;

    /**
     * Instance ReportManager class to perform queries
     * about report objects.
     */
    private transient ReportManagerBean reportManager;

    /**
     * Instance PlayerOrderManager class to perform queries
     * about playerOrder objects.
     */
    private transient PlayerOrderManagerBean playerOrderManager;

    /**
     * Instance ForumPostManager class to perform queries
     * about forumPosts objects.
     */
    private ForumPostManagerBean forumPostsManager;

    /**
     * Instance NewsManager class to perform queries
     * about News objects.
     */
    private NewsManagerBean newsManager;

    /**
     * Returns the GameHelper instance.
     *
     * @return the GameHelper
     */
    public static GameHelperBean getInstance() {
        if (ourInstance == null) {
            ourInstance = new GameHelperBean();
        }
        return ourInstance;
    }

    /**
     * Empire Statistics Keys.
     */
    public static final ArrayList<String> EMPIRE_STATISTICS = new ArrayList<String>(
            Arrays.asList(ProfileConstants.EMPIRES_PLAYED,
                    ProfileConstants.TURNS_PLAYED,
                    ProfileConstants.VPS,
                    ProfileConstants.ACHIEVEMENTS,
                    ProfileConstants.INCOME_HIGHEST,
                    ProfileConstants.TRADE_HIGHEST,
                    ProfileConstants.FORTRESS_CONQUERED,
                    ProfileConstants.FORTRESS_BUILT,
                    ProfileConstants.FORCE_INFANTRY,
                    ProfileConstants.FORCE_CAVALRY,
                    ProfileConstants.FORCE_ARTILLERY,
                    ProfileConstants.BATT_HIGHEST,
                    ProfileConstants.FORCE_SHIP_1,
                    ProfileConstants.FORCE_SHIP_2,
                    ProfileConstants.FORCE_SHIP_3,
                    ProfileConstants.FORCE_SHIP_4,
                    ProfileConstants.FORCE_SHIP_5,
                    ProfileConstants.NEWSLETTER_EPONYMOUS,
                    ProfileConstants.FORUM_POSTS));

    /**
     * Politics Statistics Keys.
     */
    public static final ArrayList<String> POLITICS_STATISTICS = new ArrayList<String>(
            Arrays.asList(ProfileConstants.ALLIANCES_MADE,
                    ProfileConstants.RESPOND_CALLALLIES,
                    ProfileConstants.REFUSE_CALLALLIES,
                    ProfileConstants.WARS_DECLARED,
                    ProfileConstants.WARS_RECEIVED,
                    ProfileConstants.PEACE_MADE,
                    ProfileConstants.SURRENDERS_MADE,
                    ProfileConstants.SURRENDERS_ACCEPTED,
                    ProfileConstants.CAPITAL_CONQUERED,
                    ProfileConstants.TCITY_CONQUERED,
                    ProfileConstants.STARTUP_COLONY,
                    ProfileConstants.EMPIRES_DESTROYED,
                    ProfileConstants.ENEMY_PRISONERS));

    /**
     * Warfare Statistics Keys.
     */
    public static final ArrayList<String> WARFARE_STATISTICS = new ArrayList<String>(
            Arrays.asList(ProfileConstants.BATTLES_TACTICAL,
                    ProfileConstants.BATTLES_TACTICAL_WON,
                    ProfileConstants.BATTLES_TACTICAL_DRAW,
                    ProfileConstants.BATTLES_TACTICAL_LOST,
                    ProfileConstants.BATTLES_FIELD,
                    ProfileConstants.BATTLES_FIELD_WON,
                    ProfileConstants.BATTLES_FIELD_DRAW,
                    ProfileConstants.BATTLES_FIELD_LOST,
                    ProfileConstants.BATTLES_NAVAL,
                    ProfileConstants.BATTLES_NAVAL_WON,
                    ProfileConstants.BATTLES_NAVAL_DRAW,
                    ProfileConstants.BATTLES_NAVAL_LOST,
                    ProfileConstants.ENEMY_KILLED_ALL,
                    //"battles.killed.inf",
                    //"battles.killed.cav",
                    //"battles.killed.eng",
                    //"battles.killed.mar",
                    ProfileConstants.ENEMY_KILLED_COM,
                    ProfileConstants.ENEMY_SUNK_ALL,
                    ProfileConstants.ENEMY_SUNK_0,
                    ProfileConstants.ENEMY_SUNK_1,
                    ProfileConstants.ENEMY_SUNK_2,
                    ProfileConstants.ENEMY_SUNK_3,
                    ProfileConstants.ENEMY_SUNK_4,
                    ProfileConstants.ENEMY_SUNK_5));

    /**
     * Undefined value.
     */
    public static final Integer UNDEFINED_VALUE = Integer.MIN_VALUE;

    /**
     * Default Constructor.
     */
    private GameHelperBean() {

    }

    /**
     * Returns User's Forum Statistics.
     *
     * @param user the input User.
     * @return a HashMap with the User Statistics.
     */
    public HashMap<String, Object> getUserForumStatistics(final User user) {
        final HashMap<String, Object> userStatistics = new HashMap<String, Object>();
        userStatistics.put("Posts", forumPostsManager.getNumberOfPosts(user));
        return userStatistics;
    }

    /**
     * Returns the User Statistics.
     *
     * @param user the input user.
     * @return a HashMap with the Profile Statistics.
     */
    @Cachable(cacheName = "userCache")
    public HashMap<String, Object> prepareUserStatistics(final User user) {
        final HashMap<String, Object> userStatistics = new HashMap<String, Object>();

        final ArrayList<String> keys = new ArrayList<String>();

        keys.addAll(EMPIRE_STATISTICS);
        keys.addAll(POLITICS_STATISTICS);
        keys.addAll(WARFARE_STATISTICS);

        for (final String key : keys) {
            final Profile thisProfile = profileManager.getByOwnerKey(user, key);
            if (thisProfile != null) {
                userStatistics.put(key, thisProfile.getValue());
                userStatistics.put(key + "Pos", convertRank(getUserPosition(key, user)));

            } else {
                userStatistics.put(key, Integer.MIN_VALUE);
                userStatistics.put(key + "Pos", "-");
            }
        }

        userStatistics.put(ProfileConstants.FORUM_POSTS, forumPostsManager.getNumberOfPosts(user));
        userStatistics.put(ProfileConstants.ACHIEVEMENTS, achievementManager.sumPoints(user));

        // count games played properly
        int totGames = 0;
        for (int db = ScenarioContextHolder.LAST_SCENARIO; db >= ScenarioContextHolder.FIRST_SCENARIO; db--) {
            ScenarioContextHolder.setScenario(db);
            totGames += userGameManager.listPlayedGames(user).size();
        }
        userStatistics.put(ProfileConstants.EMPIRES_PLAYED, totGames);

        return userStatistics;
    }

    /**
     * Constructs the hall of fame data.
     *
     * @return an ArrayList with the data.
     */
    @Cachable(cacheName = "userCache")
    public LinkedList<HofDTO> prepareHallOfFame() {
        final LinkedHashMap<User, HofDTO> hodData = new LinkedHashMap<User, HofDTO>();
        ScenarioContextHolder.defaultScenario();

        final List<Profile> vps = new ArrayList<Profile>();
        final List<Profile> achievements = new ArrayList<Profile>();
        final List<Profile> battlesTacticalWon = new ArrayList<Profile>();
        final List<Profile> battlesFieldWon = new ArrayList<Profile>();
        final List<Profile> battlesNavalWon = new ArrayList<Profile>();
        final List<Profile> enemyKilledAll = new ArrayList<Profile>();

        vps.addAll(profileManager.listByKey(ProfileConstants.VPS));
        achievements.addAll(profileManager.listAchievements());
        battlesTacticalWon.addAll(profileManager.listByKey(ProfileConstants.BATTLES_TACTICAL_WON));
        battlesFieldWon.addAll(profileManager.listByKey(ProfileConstants.BATTLES_FIELD_WON));
        battlesNavalWon.addAll(profileManager.listByKey(ProfileConstants.BATTLES_NAVAL_WON));
        enemyKilledAll.addAll(profileManager.listByKey(ProfileConstants.ENEMY_KILLED_ALL));

        int index = 1;
        for (final Profile profile : vps) {
            //Remove admin and engine.
            if (profile.getUser().getUserId() != 2 && profile.getUser().getUserId() != 93) {
                if (hodData.containsKey(profile.getUser())) {
                    hodData.get(profile.getUser()).setVps(profile.getValue());
                    hodData.get(profile.getUser()).setVpsPosition(index);
                    index++;
                } else {
                    final HofDTO newUser = new HofDTO(profile.getUser());
                    newUser.setVps(profile.getValue());
                    newUser.setVpsPosition(index);
                    hodData.put(profile.getUser(), newUser);
                    index++;
                }
            }
        }

        index = 1;
        for (final Profile profile : achievements) {
            //Remove admin and engine.
            if (profile.getUser().getUserId() != 2 && profile.getUser().getUserId() != 93
                    && profile.getValue() > 0) {
                if (hodData.containsKey(profile.getUser())) {
                    hodData.get(profile.getUser()).setAchievements(profile.getValue());
                    hodData.get(profile.getUser()).setAchievementsPosition(index);
                    index++;
                } else {
                    final HofDTO newUser = new HofDTO(profile.getUser());
                    newUser.setAchievements(profile.getValue());
                    newUser.setAchievementsPosition(index);
                    hodData.put(profile.getUser(), newUser);
                    index++;
                }
            }
        }

        index = 1;
        for (final Profile profile : battlesTacticalWon) {
            if (profile.getUser().getUserId() != 2 && profile.getUser().getUserId() != 93) {
                if (hodData.containsKey(profile.getUser())) {
                    hodData.get(profile.getUser()).setBattlesTacticalWon(profile.getValue());
                    hodData.get(profile.getUser()).setBattlesTacticalWonPosition(index);
                    index++;
                } else {
                    final HofDTO newUser = new HofDTO(profile.getUser());
                    newUser.setBattlesTacticalWon(profile.getValue());
                    newUser.setBattlesTacticalWonPosition(index);
                    hodData.put(profile.getUser(), newUser);
                    index++;
                }
            }
        }

        index = 1;
        for (final Profile profile : battlesFieldWon) {
            if (profile.getUser().getUserId() != 2 && profile.getUser().getUserId() != 93) {
                if (hodData.containsKey(profile.getUser())) {
                    hodData.get(profile.getUser()).setBattlesFieldWon(profile.getValue());
                    hodData.get(profile.getUser()).setBattlesFieldWonPosition(index);
                    index++;
                } else {
                    final HofDTO newUser = new HofDTO(profile.getUser());
                    newUser.setBattlesFieldWon(profile.getValue());
                    newUser.setBattlesFieldWonPosition(index);
                    hodData.put(profile.getUser(), newUser);
                    index++;
                }
            }
        }

        index = 1;
        for (final Profile profile : battlesNavalWon) {
            if (profile.getUser().getUserId() != 2 && profile.getUser().getUserId() != 93) {
                if (hodData.containsKey(profile.getUser())) {
                    hodData.get(profile.getUser()).setBattlesNavalWon(profile.getValue());
                    hodData.get(profile.getUser()).setBattlesNavalWonPosition(index);
                    index++;
                } else {
                    final HofDTO newUser = new HofDTO(profile.getUser());
                    newUser.setBattlesNavalWon(profile.getValue());
                    newUser.setBattlesNavalWonPosition(index);
                    hodData.put(profile.getUser(), newUser);
                    index++;
                }
            }
        }

        index = 1;
        for (final Profile profile : enemyKilledAll) {
            if (profile.getUser().getUserId() != 2 && profile.getUser().getUserId() != 93) {
                if (hodData.containsKey(profile.getUser())) {
                    hodData.get(profile.getUser()).setEnemyKilled(profile.getValue());
                    hodData.get(profile.getUser()).setEnemyKilledPosition(index);
                    index++;
                } else {
                    final HofDTO newUser = new HofDTO(profile.getUser());
                    newUser.setEnemyKilled(profile.getValue());
                    newUser.setEnemyKilledPosition(index);
                    hodData.put(profile.getUser(), newUser);
                    index++;
                }
            }
        }

        for (final HofDTO user : hodData.values()) {
            final User thisUser = user.getUser();
            final List<UserGame> tempUserGames = new ArrayList<UserGame>();
            for (int db = ScenarioContextHolder.LAST_SCENARIO; db >= ScenarioContextHolder.FIRST_SCENARIO; db--) {
                ScenarioContextHolder.setScenario(db);
                tempUserGames.addAll(userGameManager.listPlayedUserGames(thisUser));
            }

            final List<Game> games = new ArrayList<Game>();
            int plGames = 0;
            for (final UserGame tempUserGame : tempUserGames) {
                if (!games.contains(tempUserGame.getGame())) {
                    plGames++;
                    games.add(tempUserGame.getGame());
                }
            }
            user.setPlayedGames(plGames);
        }

        return new LinkedList<HofDTO>(hodData.values());
    }

    /**
     * Constructs the top players data.
     *
     * @return an ArrayList with the data.
     */
    @Cachable(cacheName = "userCache")
    public HashMap<String, LinkedList<HofDTO>> getTopPlayers() {
        final LinkedList<HofDTO> hof = prepareHallOfFame();

        final TreeSet<HofDTO> vps = new TreeSet<HofDTO>(new Comparator<HofDTO>() {
            @Override
            public int compare(final HofDTO thisProfile, final HofDTO thatProfile) {
                return ((Integer) thatProfile.getVpsPosition()).compareTo(thisProfile.getVpsPosition());
            }
        });
        vps.addAll(hof);

        final TreeSet<HofDTO> tacticalBattles = new TreeSet<HofDTO>(new Comparator<HofDTO>() {
            @Override
            public int compare(final HofDTO thisProfile, final HofDTO thatProfile) {
                return ((Integer) thatProfile.getBattlesTacticalWonPosition()).compareTo(thisProfile.getBattlesTacticalWonPosition());

            }
        });
        tacticalBattles.addAll(hof);

        final TreeSet<HofDTO> troopsKilled = new TreeSet<HofDTO>(new Comparator<HofDTO>() {
            @Override
            public int compare(final HofDTO thisProfile, final HofDTO thatProfile) {
                return ((Integer) thatProfile.getEnemyKilledPosition()).compareTo(thisProfile.getEnemyKilledPosition());

            }
        });
        troopsKilled.addAll(hof);

        final LinkedList<HofDTO> bestPlayersVps = new LinkedList<HofDTO>();
        final LinkedList<HofDTO> bestPlayersBattles = new LinkedList<HofDTO>();
        final LinkedList<HofDTO> bestPlayersTroopsKilled = new LinkedList<HofDTO>();

        for (int i = 0; i < 5; i++) {
            bestPlayersVps.add(vps.pollLast());
            bestPlayersBattles.add(tacticalBattles.pollLast());
            bestPlayersTroopsKilled.add(troopsKilled.pollLast());
        }

        final HashMap<String, LinkedList<HofDTO>> data = new HashMap<String, LinkedList<HofDTO>>();
        data.put(ProfileConstants.VPS, bestPlayersVps);
        data.put(ProfileConstants.BATTLES_TACTICAL_WON, bestPlayersBattles);
        data.put(ProfileConstants.ENEMY_KILLED_ALL, bestPlayersTroopsKilled);
        return data;
    }

    /**
     * Return a List with last tow Nations Played per User. Used on HOF.
     *
     * @param input a list with HofDTO
     * @return a list with the last two usergames per user.
     */
    public HashMap<Integer, LinkedList<UserGame>> prepareUserGames(final LinkedList<HofDTO> input) {
        final HashMap<Integer, LinkedList<UserGame>> data = new HashMap<Integer, LinkedList<UserGame>>();
        for (final HofDTO user : input) {
            final LinkedList<UserGame> userGames = new LinkedList<UserGame>();
            for (int db = ScenarioContextHolder.LAST_SCENARIO; db >= ScenarioContextHolder.FIRST_SCENARIO; db--) {
                ScenarioContextHolder.setScenario(db);
                final List<UserGame> tempUserGames = userGameManager.listPlayedUserGames(user.getUser());
                final List<Game> games = new ArrayList<Game>();
                for (final UserGame tempUserGame : tempUserGames) {
                    if (!games.contains(tempUserGame.getGame())) {
                        userGames.add(tempUserGame);
                        games.add(tempUserGame.getGame());
                    }
                }
                data.put(user.getUser().getUserId(), userGames);
            }
        }
        return data;
    }

    /**
     * Prepare report.
     *
     * @return the map with the data that will be passed to the jsp.
     */
    @Cachable(cacheName = "gameCache")
    public Map<String, Object> getAllFreePlayedNations() {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        final LinkedList<LinkedList<Object>> nations = new LinkedList<LinkedList<Object>>();
        final Map<Integer, Boolean> specialOffer = new HashMap<Integer, Boolean>();
        final Map<Integer, String> months = new HashMap<Integer, String>();
        final Map<Integer, String> monthsOneLetter = new HashMap<Integer, String>();
        for (int db = HibernateUtil.DB_FIRST; db <= HibernateUtil.DB_LAST; db++) {
            ScenarioContextHolder.setScenario(db);
            // Retrieve list of games
            final List<Game> games = gameManager.list();
            games.remove(0); // remove "Scenario" entry

            //The free nations
            final HashMap<Game, List<Integer>> totalFreeNations = new HashMap<Game, List<Integer>>();

            //Iteration on Games
            for (final Game thisGame : games) {

                // Ignore games that have ended
                if (thisGame.getEnded()) {
                    continue;
                }

                // Ignore private games
                if (thisGame.isPrivateGame()) {
                    continue;
                }

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

                // Retrieve players for this game
                final List<UserGame> gameList = userGameManager.list(thisGame);

                // check if game is about to end
                double maxGoal = Double.MIN_VALUE;

                for (final UserGame userGame : gameList) {
                    final Report thisReport = reportManager.getByOwnerTurnKey(userGame.getNation(),
                            thisGame, thisGame.getTurn() - 1, ReportConstants.N_VP);

                    // make sure we check for a null pointer exception
                    if (thisReport != null) {
                        final int currentVP = Integer.parseInt(thisReport.getValue());
                        final double currentGoal = 100d * currentVP / (userGame.getNation().getVpWin() * modifier);
                        maxGoal = Math.max(maxGoal, currentGoal);
                    }
                }

                // Ignore games that are about to end
                if (maxGoal >= 80d) {
                    continue;
                }

                final List<Integer> thisGameFreeNations = new ArrayList<Integer>();

                for (final UserGame userGame : gameList) {
                    // Check if nation is dead
                    if (userGame.isAlive() && userGame.getGame().getTurn() > 0
                            && !userGame.isActive()) {

                        thisGameFreeNations.add(userGame.getNation().getId());
                        specialOffer.put(userGame.getId(), false);

                        // Check if this position is entitled to special offer for 60 free credits
                        final List<UserGame> lstPrevPositions = userGameManager.listInActive(thisGame, userGame.getNation());
                        if (!lstPrevPositions.isEmpty()) {
                            if (thisGame.getTurn() - lstPrevPositions.get(0).getTurnDrop() >= 2) {
                                specialOffer.put(userGame.getId(), true);
                            }
                        }
                    }
                }

                if (!thisGameFreeNations.isEmpty()) {
                    totalFreeNations.put(thisGame, thisGameFreeNations);
                }

                // access Calendar object
                final StringBuilder strBuilder = new StringBuilder();
                final StringBuilder strBuilderOneLetter = new StringBuilder();
                final Calendar thisCal = calendar(thisGame);
                strBuilder.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH).substring(0, 3));
                strBuilder.append(" ");
                strBuilder.append(String.valueOf(thisCal.get(Calendar.YEAR)).substring(2));

                strBuilderOneLetter.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH).substring(0, 1));
                strBuilderOneLetter.append(" ");
                strBuilderOneLetter.append(String.valueOf(thisCal.get(Calendar.YEAR)).substring(2));
                months.put(thisGame.getGameId(), strBuilder.toString());
                monthsOneLetter.put(thisGame.getGameId(), strBuilderOneLetter.toString());
            }

            final Set<LinkedList<Object>> nationVP = new TreeSet<LinkedList<Object>>(new Comparator<LinkedList<Object>>() {
                @Override
                public int compare(final LinkedList<Object> nation1, final LinkedList<Object> nation2) {
                    if (((Integer) nation2.getLast()).equals((Integer) nation1.getLast())) {
                        return -1;
                    } else {
                        return ((Integer) nation2.getLast()).compareTo((Integer) nation1.getLast());
                    }
                }
            });

            for (Game thisGame : totalFreeNations.keySet()) {
                // To produce the ranking of production we need to do it programmatically
                nationVP.addAll(reportNations(thisGame, ReportConstants.N_VP, totalFreeNations.get(thisGame)));
            }

            for (final LinkedList<Object> nation : nationVP) {
                final HashMap<String, Integer> unSortedMap = new HashMap<String, Integer>();
                unSortedMap.put(ReportConstants.N_VP, getPosition(ReportConstants.N_VP, (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put("taxation", getPosition("taxation", (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put("population.size", getPosition("population.size", (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put(ReportConstants.E_SEC_SIZE_TOT, getPosition(ReportConstants.E_SEC_SIZE_TOT, (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put(ReportConstants.A_TOT_BAT, getPosition(ReportConstants.A_TOT_BAT, (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put(ReportConstants.S_TOT_SHIPS, getPositionFixed(ReportConstants.S_TOT_SHIPS, (Game) nation.get(0), (Nation) nation.get(1)));

                unSortedMap.put(ReportConstants.A_TOT_KILLS, getPosition(ReportConstants.A_TOT_KILLS, (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put(ReportConstants.A_TOT_DEATHS, getPosition(ReportConstants.A_TOT_DEATHS, (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put(ReportConstants.S_SINKS, getPosition(ReportConstants.S_SINKS, (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put(ReportConstants.S_SINKED, getPosition(ReportConstants.S_SINKED, (Game) nation.get(0), (Nation) nation.get(1)));

                nation.addLast(sortByValue(unSortedMap));
                nation.addLast(userGameManager.list((Game) nation.get(0), (Nation) nation.get(1)).get(0));
                nations.addLast(nation);
            }
        }

        // return values
        refData.put("freeNations", nations);
        refData.put("monthsSmall", months);
        refData.put("monthsOneLetter", monthsOneLetter);
        refData.put("specialOffer", specialOffer);
        return refData;
    }

    /**
     * Prepare report.
     *
     * @return the map with the data that will be passed to the jsp.
     */
    @Cachable(cacheName = "gameCache")
    public Map<String, Object> getAllNewNations() {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        //The free nations
        final Map<Game, SortedSet<UserGame>> totalFreeNations = new HashMap<Game, SortedSet<UserGame>>();
        final Map<Integer, User> totalUsers = new HashMap<Integer, User>();
        final Map<Integer, Boolean> specialOffer = new HashMap<Integer, Boolean>();
        final Map<Integer, String> months = new HashMap<Integer, String>();
        final Map<Integer, User> gameOwners = new HashMap<Integer, User>();
        for (int db = HibernateUtil.DB_FIRST; db <= HibernateUtil.DB_LAST; db++) {
            ScenarioContextHolder.setScenario(db);

            // Retrieve list of games
            final List<Game> games = gameManager.listNewGames();
            final int totPlayers = nationManager.list().size() - 1;

            // Iteration on Games
            for (final Game thisGame : games) {
                gameOwners.put(thisGame.getUserId(), userManager.getByID(thisGame.getUserId()));
                // Ignore private games
                if (thisGame.isPrivateGame()) {
                    continue;
                }

                // Retrieve players for this game
                final List<UserGame> gameList = userGameManager.list(thisGame);

                final TreeSet<UserGame> thisGameFreeNations = new TreeSet<UserGame>(new GameId());
                final HashMap<Integer, User> gameUsers = new HashMap<Integer, User>();

                int totalActive = 0;

                for (final UserGame userGame : gameList) {
                    final User thisUser = userManager.getByID(userGame.getUserId());
                    gameUsers.put(thisUser.getUserId(), thisUser);
                    specialOffer.put(userGame.getId(), false);

                    // Check if nation is assigned
                    if (userGame.isAlive()
                            && userGame.isActive()
                            && userGame.getNation().getId() != -1) {
                        totalActive++;
                    }
                }

                if (totalActive != totPlayers) {
                    thisGameFreeNations.addAll(gameList);
                    totalFreeNations.put(thisGame, thisGameFreeNations);
                    totalUsers.putAll(gameUsers);

                    // access Calendar object
                    final StringBuilder strBuilder = new StringBuilder();
                    final Calendar thatCal = calendar(thisGame);
                    strBuilder.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
                    strBuilder.append(" ");
                    strBuilder.append(thatCal.get(Calendar.YEAR));

                    months.put(thisGame.getGameId(), strBuilder.toString());
                }
            }
        }
        // return values
        refData.put("newGames", totalFreeNations);
        refData.put("gameUsers", totalUsers);
        refData.put("gameOwners", gameOwners);
        refData.put("newGamesMonths", months);
        refData.put("specialOffer", specialOffer);
        return refData;
    }

    /**
     * Prepare report.
     *
     * @return the map with the data that will be passed to the jsp.
     */
    @Cachable(cacheName = "gameCache")
    public Map<String, Object> getAllNewFreeNations() {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        final LinkedList<LinkedList<Object>> nations = new LinkedList<LinkedList<Object>>();
        final Map<Integer, String> months = new HashMap<Integer, String>();
        final Map<Integer, String> monthsOneLetter = new HashMap<Integer, String>();
        for (int db = HibernateUtil.DB_FIRST; db <= HibernateUtil.DB_LAST; db++) {
            ScenarioContextHolder.setScenario(db);

            // Retrieve list of games
            final List<Game> games = gameManager.listNewGames();

            //The free nations
            final HashMap<Game, List<Integer>> totalFreeNations = new HashMap<Game, List<Integer>>();

            //Iteration on Games
            for (final Game thisGame : games) {

                // ignore private games
                if (thisGame.isPrivateGame()) {
                    continue;
                }

                // Retrieve players for this game
                final List<UserGame> gameList = userGameManager.list(thisGame);

                final List<Integer> thisGameFreeNations = new ArrayList<Integer>();

                for (final UserGame userGame : gameList) {
                    // Check if nation is dead
                    if (userGame.isAlive()
                            && !userGame.isActive()
                            && userGame.getNation().getId() != -1) {
                        thisGameFreeNations.add(userGame.getNation().getId());
                    }
                }
                if (!thisGameFreeNations.isEmpty()) {
                    totalFreeNations.put(thisGame, thisGameFreeNations);
                }

                // access Calendar object
                final StringBuilder strBuilder = new StringBuilder();
                final StringBuilder strBuilderOneLetter = new StringBuilder();
                final Calendar thisCal = calendar(thisGame);
                strBuilder.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH).substring(0, 3));
                strBuilder.append(" ");
                strBuilder.append(String.valueOf(thisCal.get(Calendar.YEAR)).substring(2));

                strBuilderOneLetter.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH).substring(0, 1));
                strBuilderOneLetter.append(" ");
                strBuilderOneLetter.append(String.valueOf(thisCal.get(Calendar.YEAR)).substring(2));
                months.put(thisGame.getGameId(), strBuilder.toString());
                monthsOneLetter.put(thisGame.getGameId(), strBuilderOneLetter.toString());
            }

            final Set<LinkedList<Object>> nationVP = new TreeSet<LinkedList<Object>>(new Comparator<LinkedList<Object>>() {
                @Override
                public int compare(final LinkedList<Object> nation1, final LinkedList<Object> nation2) {
                    if (((Integer) nation2.getLast()).equals((Integer) nation1.getLast())) {
                        return -1;
                    } else {
                        return ((Integer) nation2.getLast()).compareTo((Integer) nation1.getLast());
                    }
                }
            });

            for (Game thisGame : totalFreeNations.keySet()) {

                // To produce the ranking of production we need to do it programmatically
                nationVP.addAll(reportNations(thisGame, ReportConstants.N_VP, totalFreeNations.get(thisGame)));
            }

            for (final LinkedList<Object> nation : nationVP) {
                final HashMap<String, Integer> unSortedMap = new HashMap<String, Integer>();
                unSortedMap.put(ReportConstants.N_VP, getPosition(ReportConstants.N_VP, (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put("taxation", getPosition("taxation", (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put("population.size", getPosition("population.size", (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put(ReportConstants.E_SEC_SIZE_TOT, getPosition(ReportConstants.E_SEC_SIZE_TOT, (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put(ReportConstants.A_TOT_BAT, getPosition(ReportConstants.A_TOT_BAT, (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put(ReportConstants.S_TOT_SHIPS, getPositionFixed(ReportConstants.S_TOT_SHIPS, (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put(ReportConstants.A_TOT_KILLS, getPosition(ReportConstants.A_TOT_KILLS, (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put(ReportConstants.A_TOT_DEATHS, getPosition(ReportConstants.A_TOT_DEATHS, (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put(ReportConstants.S_SINKS, getPosition(ReportConstants.S_SINKS, (Game) nation.get(0), (Nation) nation.get(1)));
                unSortedMap.put(ReportConstants.S_SINKED, getPosition(ReportConstants.S_SINKED, (Game) nation.get(0), (Nation) nation.get(1)));

                nation.addLast(sortByValue(unSortedMap));
                nation.addLast(userGameManager.list((Game) nation.get(0), (Nation) nation.get(1)).get(0));
                nations.addLast(nation);
            }
        }
        // return values
        refData.put("freeNations", nations);
        refData.put("monthsSmall", months);
        refData.put("monthsOneLetter", monthsOneLetter);
        return refData;
    }

    /**
     * Prepare list of games.
     *
     * @return the map with the data that will be passed to the jsp.
     */
    @Cachable(cacheName = "gameCache")
    public Map<String, Object> prepareGamesList() {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // Calculate calendar dates for each game
        final Map<Integer, String> dates = new HashMap<Integer, String>();
        final Map<Integer, Map<Integer, User>> gameToNationToPlayer = new HashMap<Integer, Map<Integer, User>>();
        final Map<Integer, Map<Integer, Boolean>> gameToNationToStatus = new HashMap<Integer, Map<Integer, Boolean>>();
        final Map<Integer, Map<Integer, Boolean>> gameToNationToAlive = new HashMap<Integer, Map<Integer, Boolean>>();
        final Map<Integer, List<Nation>> gameToTopVP = new HashMap<Integer, List<Nation>>();
        final Map<Integer, List<Nation>> gameToTopSize = new HashMap<Integer, List<Nation>>();
        final Map<Integer, List<Nation>> gameToTopMoney = new HashMap<Integer, List<Nation>>();
        final Map<Integer, List<Nation>> gameToTopArmy = new HashMap<Integer, List<Nation>>();
        final Map<Integer, List<Nation>> gameToTopNavy = new HashMap<Integer, List<Nation>>();
        final Map<Integer, List<Nation>> gameToFree = new HashMap<Integer, List<Nation>>();
        final Map<Integer, List<Nation>> gameToDead = new HashMap<Integer, List<Nation>>();
        final Map<Integer, List<Nation>> gameToWinner = new HashMap<Integer, List<Nation>>();
        final Map<Integer, List<Nation>> gameToCoWinner = new HashMap<Integer, List<Nation>>();
        final Map<Integer, List<Nation>> gameToRunnerUp = new HashMap<Integer, List<Nation>>();
        final Map<Integer, List<Nation>> gameNations = new HashMap<Integer, List<Nation>>();
        final List<Game> endedList = new ArrayList<Game>();
        final List<Game> runningList = new ArrayList<Game>();
        final Map<Object, List<BigInteger>> activityStat = new HashMap<Object, List<BigInteger>>();
        final Map<Integer, User> userIdToUser = new HashMap<Integer, User>();

        for (int db = HibernateUtil.DB_FIRST; db <= HibernateUtil.DB_LAST; db++) {
            ScenarioContextHolder.setScenario(db);
            // Retrieve list of games
            final List<Game> gameList = gameManager.list();
            gameList.remove(0); // remove "Scenario" entry

            for (final Game game : gameList) {
                userIdToUser.put(game.getUserId(), userManager.getByID(game.getUserId()));
                gameToNationToPlayer.put(game.getGameId(), new HashMap<Integer, User>());
                gameToNationToStatus.put(game.getGameId(), new HashMap<Integer, Boolean>());
                gameToNationToAlive.put(game.getGameId(), new HashMap<Integer, Boolean>());
                gameToTopVP.put(game.getGameId(), reportManager.rankNations(ReportConstants.N_VP, game, game.getTurn() - 1, 3));
                gameToTopSize.put(game.getGameId(), reportManager.rankNations(ReportConstants.E_SEC_SIZE_TOT, game, game.getTurn() - 1, 3));
                gameToTopMoney.put(game.getGameId(), reportManager.rankNationsFixed(ReportConstants.W_REGION + "1" + ReportConstants.W_GOOD + "1", game, game.getTurn() - 1, 3));
                gameToTopArmy.put(game.getGameId(), reportManager.rankNations(ReportConstants.A_TOT_BAT, game, game.getTurn() - 1, 3));
                gameToTopNavy.put(game.getGameId(), reportManager.rankNationsFixed(ReportConstants.S_TOT_SHIPS, game, game.getTurn() - 1, 3));
                gameToWinner.put(game.getGameId(), new ArrayList<Nation>());
                gameToCoWinner.put(game.getGameId(), new ArrayList<Nation>());
                gameToRunnerUp.put(game.getGameId(), new ArrayList<Nation>());

                // Retrieve list of nations
                final List<Nation> nationList = nationManager.list();
                nationList.remove(0); // remove "Free Nation" entry
                gameNations.put(game.getGameId(), nationList);

                if (game.getEnded()) {
                    endedList.add(game);

                    // Also update list of winners, co-winners and runners-up
                    final StringTokenizer winToken = new StringTokenizer(game.getWinners().substring(1), "*");
                    while (winToken.hasMoreElements()) {
                        gameToWinner.get(game.getGameId()).add(nationManager.getByID(Integer.parseInt(winToken.nextToken())));
                    }

                    // Also update list of winners, co-winners and runners-up
                    if (game.getCoWinners() != null && !game.getCoWinners().isEmpty()) {
                        final StringTokenizer coWinToken = new StringTokenizer(game.getCoWinners().substring(1), "*");
                        while (coWinToken.hasMoreElements()) {
                            gameToCoWinner.get(game.getGameId()).add(nationManager.getByID(Integer.parseInt(coWinToken.nextToken())));
                        }
                    }

                    // Also update list of runners-up
                    if (game.getRunnerUp() != null && !game.getRunnerUp().isEmpty()) {
                        final StringTokenizer runToken = new StringTokenizer(game.getRunnerUp().substring(1), "*");
                        while (runToken.hasMoreElements()) {
                            gameToRunnerUp.get(game.getGameId()).add(nationManager.getByID(Integer.parseInt(runToken.nextToken())));
                        }
                    }

                } else {
                    runningList.add(game);
                }

                final List<UserGame> userGames = userGameManager.list(game);
                final List<Nation> freeNations = new ArrayList<Nation>();
                final List<Nation> deadNations = new ArrayList<Nation>();
                for (final UserGame userGame : userGames) {
                    final User user = userManager.getByID(userGame.getUserId());
                    gameToNationToPlayer.get(game.getGameId()).put(userGame.getNation().getId(), user);
                    gameToNationToStatus.get(game.getGameId()).put(userGame.getNation().getId(), userGame.isActive());
                    gameToNationToAlive.get(game.getGameId()).put(userGame.getNation().getId(), userGame.isAlive());

                    // Check if nation is free
                    if (!userGame.isActive()) {
                        freeNations.add(userGame.getNation());
                    }

                    // Check if nation is dead
                    if (!userGame.isAlive()) {
                        deadNations.add(userGame.getNation());
                    }
                }

                gameToFree.put(game.getGameId(), freeNations);
                gameToDead.put(game.getGameId(), deadNations);

                final StringBuilder strBuilder = new StringBuilder();
                final Calendar thatCal = calendar(game);
                strBuilder.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
                strBuilder.append(" ");
                strBuilder.append(thatCal.get(Calendar.YEAR));
                dates.put(game.getGameId(), strBuilder.toString());
            }
            activityStat.putAll(playerOrderManager.countOrders());
        }

        // Make sure list is sorted by game ID
        Collections.sort(runningList, new Comparator<Game>() {

            public int compare(Game g1, Game g2) {
                return Integer.compare(g1.getGameId(), g2.getGameId());
            }
        });

        // Make sure list is sorted by game ID
        Collections.sort(endedList, new Comparator<Game>() {

            public int compare(Game g1, Game g2) {
                return Integer.compare(g1.getGameId(), g2.getGameId());
            }
        });

        // Prepare data to pass to jsp
        refData.put("gameToFree", gameToFree);
        refData.put("gameToDead", gameToDead);
        refData.put("gameToNationToPlayer", gameToNationToPlayer);
        refData.put("gameToNationToStatus", gameToNationToStatus);
        refData.put("gameToNationToAlive", gameToNationToAlive);
        refData.put("gameToTopVP", gameToTopVP);
        refData.put("gameToTopSize", gameToTopSize);
        refData.put("gameToTopMoney", gameToTopMoney);
        refData.put("gameToTopArmy", gameToTopArmy);
        refData.put("gameToTopNavy", gameToTopNavy);
        refData.put("gameToWinner", gameToWinner);
        refData.put("gameToCoWinner", gameToCoWinner);
        refData.put("gameToRunnerUp", gameToRunnerUp);
        refData.put("gameNations", gameNations);
        refData.put("gameList", runningList);
        refData.put("endedList", endedList);
        refData.put("dates", dates);
        refData.put("activityStat", activityStat);
        refData.put("userIdToUser", userIdToUser);

        return refData;
    }

    /**
     * Prepare list of top Nations..
     *
     * @return the map with the data that will be passed to the jsp.
     */
    @Cachable(cacheName = "userCache")
    public Map<String, Object> prepareTopNations() {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // Retrieve list of games
        final List<Game> games = gameManager.list();
        games.remove(0); // remove "Scenario" entry

        //The top Nations <UserGame, VPs>
        final HashMap<UserGame, Integer> totalNations = new HashMap<UserGame, Integer>();

        final Map<Integer, String> months = new HashMap<Integer, String>();
        final Map<Integer, String> monthsOneLetter = new HashMap<Integer, String>();

        //Iteration on Games
        for (final Game thisGame : games) {
            if (thisGame.getEnded()) {
                // ignore ended games
                continue;
            }

            //Top 9 nations per game
            final List<Nation> nations = reportManager.rankNations(ReportConstants.N_VP, thisGame, thisGame.getTurn() - 1, 9);

            for (final Nation nation : nations) {
                final UserGame userGame = userGameManager.list(thisGame, nation).get(0);
                totalNations.put(userGame,
                        Integer.parseInt(reportManager.getByOwnerTurnKey(
                                nation,
                                thisGame,
                                thisGame.getTurn() - 1,
                                ReportConstants.N_VP).getValue()));
            }

            // access Calendar object
            final StringBuilder strBuilder = new StringBuilder();
            final StringBuilder strBuilderOneLetter = new StringBuilder();
            final Calendar thisCal = calendar(thisGame);
            strBuilder.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH).substring(0, 3));
            strBuilder.append(" ");
            strBuilder.append(String.valueOf(thisCal.get(Calendar.YEAR)).substring(2));

            strBuilderOneLetter.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH).substring(0, 1));
            strBuilderOneLetter.append(" ");
            strBuilderOneLetter.append(String.valueOf(thisCal.get(Calendar.YEAR)).substring(2));
            months.put(thisGame.getGameId(), strBuilder.toString());
            monthsOneLetter.put(thisGame.getGameId(), strBuilderOneLetter.toString());
        }

        //Sort by value
        final LinkedList<Map.Entry<UserGame, Integer>> list = new LinkedList<Map.Entry<UserGame, Integer>>(totalNations.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<UserGame, Integer>>() {
            public int compare(final Map.Entry<UserGame, Integer> thisEntry, final Map.Entry<UserGame, Integer> thatEntry) {
                return thatEntry.getValue().compareTo(thisEntry.getValue());
            }
        });
        final LinkedHashMap<UserGame, Integer> result = new LinkedHashMap<UserGame, Integer>();
        for (final Map.Entry<UserGame, Integer> thisEntry : list) {
            result.put(thisEntry.getKey(), thisEntry.getValue());
        }

        // return values
        refData.put("topNations", result);
        refData.put("monthsSmall", months);
        refData.put("monthsOneLetter", monthsOneLetter);

        return refData;
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

        if (ranked.isEmpty() || !ranked.contains(thisNation)) {
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
     * Go through the list until you find the user we are looking for.
     *
     * @param key  -- the key used to rank nations.
     * @param user -- the user.
     * @return the position of the user.
     */
    protected final int getUserPosition(final String key, final User user) {
        final List<Profile> ranked = profileManager.listByKey(key);

        if (ranked.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        boolean userFound = false;
        int rank = 1;
        for (final Profile profile : ranked) {
            if (profile.getUser().getUserId() == user.getUserId()) {
                userFound = true;
                break;
            }
            rank++;
        }
        if (userFound) {
            return rank;
        } else {
            return Integer.MAX_VALUE;
        }
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

    protected final List<LinkedList<Object>> reportNations(final Game game, final String category, final List<Integer> nations) {
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
     * Get the current calendar.
     *
     * @param thisGame the Game instance.
     * @return the calendar.
     */
    protected final Calendar calendar(final Game thisGame) {
        final Calendar thisCal = Calendar.getInstance();

        // Define starting date based on scenario.
        switch (thisGame.getScenarioId()) {
            case HibernateUtil.DB_S1:
                if (thisGame.getGameId() < 8) {
                    thisCal.set(1805, Calendar.JANUARY, 1);

                } else {
                    thisCal.set(1802, Calendar.APRIL, 1);
                }
                break;

            case HibernateUtil.DB_S2:
                thisCal.set(1805, Calendar.JANUARY, 1);
                break;

            case HibernateUtil.DB_S3:
                thisCal.set(1808, Calendar.SEPTEMBER, 1);
                break;

            case HibernateUtil.DB_FREE:
            default:
                thisCal.set(1804, Calendar.JANUARY, 1);
                break;

        }

        thisCal.add(Calendar.MONTH, thisGame.getTurn());
        return thisCal;
    }

    /**
     * Setter method used by spring to inject a profileManager bean.
     *
     * @param value a profileManager bean.
     */
    public void setProfileManager(final ProfileManagerBean value) {
        this.profileManager = value;
    }

    /**
     * Setter method used by spring to inject a userGameManager bean.
     *
     * @param value a UserGameManager bean.
     */
    public void setUserGameManager(final UserGameManagerBean value) {
        this.userGameManager = value;
    }

    /**
     * Setter method used by spring to inject a userManager bean.
     *
     * @param value a UserManager bean.
     */
    public void setUserManager(final UserManagerBean value) {
        this.userManager = value;
    }

    /**
     * Setter method used by spring to inject a gameManager bean.
     *
     * @param injGameManager a gameManager bean.
     */
    public void setGameManager(final GameManagerBean injGameManager) {
        gameManager = injGameManager;
    }

    /**
     * Setter method used by spring to inject a nationManager bean.
     *
     * @param injNationManager a nationManager bean.
     */
    public void setNationManager(final NationManagerBean injNationManager) {
        nationManager = injNationManager;
    }

    /**
     * Setter method used by spring to inject a reportManager bean.
     *
     * @param injReportManager a reportManager bean.
     */
    public void setReportManager(final ReportManagerBean injReportManager) {
        reportManager = injReportManager;
    }

    /**
     * Setter method used by spring to inject a PlayerOrderManager bean.
     *
     * @param injPlayerOrderManager a playerOrderManager bean.
     */
    public void setPlayerOrderManager(final PlayerOrderManagerBean injPlayerOrderManager) {
        playerOrderManager = injPlayerOrderManager;
    }

    /**
     * Setter method used by spring to inject a ForumPostsManager bean.
     *
     * @param value a ForumPostsManager bean.
     */
    public void setForumPostsManager(final ForumPostManagerBean value) {
        this.forumPostsManager = value;
    }

    /**
     * Setter method used by spring to inject a NewsManager bean.
     *
     * @param value a NewsManager bean.
     */
    public void setNewsManager(final NewsManagerBean value) {
        this.newsManager = value;
    }

    /**
     * Instance of AchievementManager.
     */
    private AchievementManagerBean achievementManager;

    /**
     * Getter method used to access the AchievementManager bean.
     *
     * @return the achievementManager bean.
     */
    public AchievementManagerBean getAchievementManager() {
        return achievementManager;
    }

    /**
     * Setter method used by spring to inject a AchievementManager bean.
     *
     * @param value an achievementManager bean.
     */
    public void setAchievementManager(final AchievementManagerBean value) {
        this.achievementManager = value;
    }

}

