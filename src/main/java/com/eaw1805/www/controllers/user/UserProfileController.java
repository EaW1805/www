package com.eaw1805.www.controllers.user;

import com.eaw1805.core.EmailManager;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.GameConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.managers.beans.NationManagerBean;
import com.eaw1805.data.managers.beans.NewsManagerBean;
import com.eaw1805.data.managers.beans.PaymentHistoryManagerBean;
import com.eaw1805.data.managers.beans.PlayerOrderManagerBean;
import com.eaw1805.data.managers.beans.PostMessageManagerBean;
import com.eaw1805.data.managers.beans.ReportManagerBean;
import com.eaw1805.data.model.Follow;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Message;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.News;
import com.eaw1805.data.model.Report;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.data.model.WatchGame;
import com.eaw1805.www.commands.AdminTransferCommand;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.antisamy.AntisamyManager;
import com.eaw1805.www.controllers.cache.async.EawAsync;
import com.eaw1805.www.controllers.cache.helper.GameHelperBean;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.controllers.validators.InboxValidator;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@org.springframework.stereotype.Controller
public class UserProfileController
        extends BaseController
        implements ReportConstants {

    private static final String MODEL_JSTL_KEY = "message";

    private static final String HOME = "/home";

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(UserProfileController.class);

    @ModelAttribute(MODEL_JSTL_KEY)
    public Message getCommandObject() {
        return new Message();
    }

    /**
     * Instance of PaymentHistoryManager class.
     */
    protected transient PaymentHistoryManagerBean pmHistoryManager;

    /**
     * Instance of NewsManager class.
     */
    protected transient NewsManagerBean newsManager;

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
     * Instance of AntisamManager class.
     */
    protected transient AntisamyManager antisamyManager;

    /**
     * Send Email Notification to the receiver of the message.
     *
     * @param message the input message.
     */
    @EawAsync
    private void sendEmailNotification(final Message message) {
        // Send out mail
        EmailManager.getInstance().sendMessageNotification(message);
    }

    @ModelAttribute("adminTransferCommand")
    public AdminTransferCommand transferCommand() {
        return new AdminTransferCommand();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/{userName}")
    public String processProfileMessageSubmit(
            @ModelAttribute(MODEL_JSTL_KEY) Message message, final BindingResult result,
            final SessionStatus status, final ModelMap model) throws InvalidPageException {
        ScenarioContextHolder.defaultScenario();

        //get http request object.
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() == -1) {
            throw new InvalidPageException("Access Denied");
        }

        //check that recipient is given.
        final String recipient = request.getParameter("recipient");

        //check that recipient is given.
        if (recipient == null || recipient.isEmpty()) {
            throw new InvalidPageException("Access Denied");
        }

        final User endUser = userManager.getByUserName(recipient);

        //check that users are valid.
        if (endUser == null || thisUser.equals(endUser)) {
            throw new InvalidPageException("Access Denied");
        }

        model.addAllAttributes(prepareUserProfile(thisUser, endUser));

        //Clear html code.
        message.setBodyMessage(antisamyManager.scanWithOutImgTag(message.getBodyMessage()));
        message.setSubject(antisamyManager.scanWithOutImgTag(message.getSubject()));

        final InboxValidator inboxValidator = new InboxValidator();
        inboxValidator.validate(message, result);
        if (result.hasErrors()) {
            return "user/profileUser";
        }

        final Message rootMessage =
                messageManager.getRootMessage(thisUser.getUserId(), endUser.getUserId(), message.getSubject());

        //This is a new root message
        if (rootMessage == null) {
            message.setRootId(0);
        } else {
            message.setRootId(rootMessage.getMessageId());
        }

        message.setSender(thisUser);
        message.setReceiver(endUser);
        message.setDate(new Date());
        message.setOpened(false);

        messageManager.add(message);

        if (message.getReceiver().getEnableNotifications()) {
            if (message.getRootId() == 0) {
                final Message thisMessage =
                        messageManager.getRootMessage(thisUser.getUserId(), endUser.getUserId(), message.getSubject());
                message.setRootId(thisMessage.getMessageId());
            }
            sendEmailNotification(message);
        }

        model.put("message", new Message());
        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Inbox Post");
        return "user/profileUser";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userName}")
    protected ModelAndView handle(@PathVariable final String userName,
                                  final HttpServletRequest request, final ModelMap model)
            throws Exception {
        ScenarioContextHolder.defaultScenario();
        // Retrieve Game entity
        if (userName == null || userName.isEmpty()) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + HOME);
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        // retrieve user
        final User thisUser = getUser();

        // get profile user
        final User profileUser = userManager.getByUserName(userName);

        if (profileUser == null) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + HOME);
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);

        } else if (profileUser.getUsername().equals(thisUser.getUsername())) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/games");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        model.addAllAttributes(prepareUserProfile(thisUser, profileUser));
        model.addAttribute("postMessages", postMessageManager.listByUser(profileUser.getUserId()));
        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Home/UserProfile");
        return new ModelAndView("user/profileUser", model);
    }

    /**
     * Prepares user Profile data.
     *
     * @param thisUser    the logged in user.
     * @param profileUser the profile user.
     * @return a HashMap with the data
     */
    private HashMap<String, Object> prepareUserProfile(final User thisUser, final User profileUser) {

        final HashMap<String, Object> model = new HashMap<String, Object>();

        // Retrieve list of nations
        final List<Nation> nationList = nationManager.list();
        nationList.remove(0); // remove "Free Nation" entry

        //get users that this person follows.
        final List<Follow> following = followManager.listByFollower(profileUser, false);
        final List<User> followingList = new ArrayList<User>();
        int count = 0;
        for (final Follow follow : following) {
            count++;
            if (count > 10) {
                break;
            }
            followingList.add(follow.getLeader());
        }

        //get this user followers.
        final List<Follow> followers = followManager.listByLeader(profileUser, false);
        count = 0;
        final List<User> followersList = new ArrayList<User>();
        for (final Follow follow : followers) {
            count++;
            if (count > 10) {
                break;
            }
            followersList.add(follow.getFollower());
        }

        //Check if current user is following profile user.
        boolean isFollowed = false;
        for (final Follow follow : followers) {
            if (follow.getFollower().getUserId() == thisUser.getUserId()) {
                isFollowed = true;
                break;
            }
        }

        // identify games watched by player
        final List<WatchGame> watchedGames = watchGameManager.listByUser(profileUser);
        final List<Game> watchGames = new ArrayList<Game>();
        count = 0;
        for (final WatchGame watchGame : watchedGames) {
            count++;
            if (count > 15) {
                break;
            }
            watchGames.add(watchGame.getGame());
        }

        // the games of the player
        final List<UserGame> userGameslist = new ArrayList<UserGame>();
        final Map<Object, List<BigInteger>> gameTotalOrders = new HashMap<Object, List<BigInteger>>();
        for (int db = ScenarioContextHolder.FIRST_SCENARIO; db <= ScenarioContextHolder.LAST_SCENARIO; db++) {
            ScenarioContextHolder.setScenario(db);
            userGameslist.addAll(userGameManager.list(profileUser));
            gameTotalOrders.putAll(playerOrderManager.countOrders());
        }

        // Short games based on activity
        final Set<UserGame> userGames = new TreeSet<UserGame>(new Comparator<UserGame>() {

            public int compare(final UserGame thatGame, final UserGame thisGame) {
                return Integer.compare(thisGame.getGame().getGameId(), thatGame.getGame().getGameId());
            }
        });

        final HashMap<Integer, Integer> userGameVps = new HashMap<Integer, Integer>();
        final HashMap<Integer, Double> userGameModifier = new HashMap<Integer, Double>();
        final HashMap<Integer, HashMap<String, String>> userGameStats = new HashMap<Integer, HashMap<String, String>>();
        final Map<Integer, String> dates = new HashMap<Integer, String>();

        for (final UserGame userGame : userGameslist) {
            ScenarioContextHolder.setScenario(userGame.getGame().getScenarioId());

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

            userGames.add(userGame);
            userGameVps.put(userGame.getGame().getGameId(), retrieveVPs(userGame.getGame(), userGame.getNation(), userGame.getGame().getTurn() - 1));
            userGameModifier.put(userGame.getGame().getGameId(), modifier);

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

            // Calculate calendar dates for each game
            final StringBuilder strBuilder = new StringBuilder();
            final Calendar thatCal = calendar(userGame.getGame());
            strBuilder.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
            strBuilder.append(" ");
            strBuilder.append(thatCal.get(Calendar.YEAR));
            dates.put(userGame.getGame().getGameId(), strBuilder.toString());
        }

        //get sparkbarsf
        final Map<Integer, Map<Integer, List<Long>>> gameToNationToStats = new HashMap<Integer, Map<Integer, List<Long>>>();
        for (final UserGame userGame : userGameslist) {
            ScenarioContextHolder.setScenario(userGame.getGame().getScenarioId());

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
            }
        }

        final Set<News> newsList = new TreeSet<News>(new Comparator<News>() {
            public int compare(final News news, final News news1) {
                return news.getNewsId() - news1.getNewsId();
            }
        });

        final HashMap<String, String> gameStatsMessages = new HashMap<String, String>();
        gameStatsMessages.put(N_VP, "Victory points");
        gameStatsMessages.put("taxation", "Best game economy");
        gameStatsMessages.put("population.size", "Largest population");
        gameStatsMessages.put(E_SEC_SIZE_TOT, "Largest country");
        gameStatsMessages.put(A_TOT_BAT, "Largest army");
        gameStatsMessages.put(S_TOT_SHIPS, "Largest navy");
        gameStatsMessages.put(A_TOT_KILLS, "Most casualties inflicted");
        gameStatsMessages.put(A_TOT_DEATHS, "Most casualties suffered");
        gameStatsMessages.put(S_SINKS, "Most ship sinks/captures");
        gameStatsMessages.put(S_SINKED, "Most ships sunk");

        model.put("user", thisUser);
        model.put("profileUser", profileUser);
        model.put("userLastVisit", new Date(profileUser.getUserLastVisit() * 1000));
        model.put("userDateJoined", new Date(profileUser.getDateJoin() * 1000));
        model.put("followingCnt", following.size());
        model.put("followingList", followingList);
        model.put("followersCnt", followers.size());
        model.put("followersList", followersList);
        model.put("isFollowed", isFollowed);
        model.put("watchCnt", watchedGames.size());
        model.put("gameList", watchGames);
        model.put("nationList", nationList);
        model.put("userGames", userGames);
        model.put("userGameVps", userGameVps);
        model.put("userGameModifier", userGameModifier);
        model.put("dates", dates);
        model.put("activityStat", gameToNationToStats);
        model.put("globalActivityStat", gameTotalOrders);
        model.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        model.put("userGameStats", userGameStats);
        model.put("gameStatsMessages", gameStatsMessages);
        model.put("newsList", newsList);
        model.put("history", pmHistoryManager.list(profileUser));

        model.put("profileStats", gameHelper.prepareUserStatistics(profileUser));
        model.put("forumStats", gameHelper.getUserForumStatistics(profileUser));
        model.put("empireKeys", GameHelperBean.EMPIRE_STATISTICS);
        model.put("politicsKeys", GameHelperBean.POLITICS_STATISTICS);
        model.put("warfareKeys", GameHelperBean.WARFARE_STATISTICS);
        model.put("undefinedInt", GameHelperBean.UNDEFINED_VALUE);

        model.put("recentAchievements", getAchievementManager().listRecent(profileUser.getUserId()));


        final Map<String, Object> freePlayedNationsData = gameHelper.getAllFreePlayedNations();
        model.put("monthsSmall", freePlayedNationsData.get("monthsOneLetter"));

        ScenarioContextHolder.setScenario(HibernateUtil.DB_FREE);
        final List<UserGame> freeUserGames = userGameManager.list(profileUser);
        if (freeUserGames.size() > 0) {
            model.put("freeUserGame", freeUserGames.get(0));
        }

        return model;
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
     * Sort an <String, Integer> Map by value.
     *
     * @param map the unsorted map.
     * @return the sorted map.
     */
    private LinkedHashMap<String, String> sortByValue(final Map<String, Integer> map) {

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

    /**
     * Setter method used by spring to inject a nationManager bean.
     *
     * @param value a nationManager bean.
     */
    public void setNationManager(final NationManagerBean value) {
        nationManager = value;
    }

    /**
     * Setter method used by spring to inject a PlayerOrderManager bean.
     *
     * @param value a playerOrderManager bean.
     */
    public void setPlayerOrderManager(final PlayerOrderManagerBean value) {
        playerOrderManager = value;
    }

    /**
     * Setter method used by spring to inject a newsManager bean.
     *
     * @param value newsManager bean.
     */
    public void setNewsManager(final NewsManagerBean value) {
        this.newsManager = value;
    }

    /**
     * Getter method used to access the reportManager bean injected by Spring.
     *
     * @return a reportManager bean.
     */
    public ReportManagerBean getReportManager() {
        return reportManager;
    }

    /**
     * Setter method used by spring to inject a reportManager bean.
     *
     * @param value a reportManager bean.
     */
    public void setReportManager(final ReportManagerBean value) {
        reportManager = value;
    }

    /**
     * Setter method used by spring to inject a PaymentHistoryManager bean.
     *
     * @param value a PaymentHistoryManager bean.
     */
    public void setPmHistoryManager(final PaymentHistoryManagerBean value) {
        this.pmHistoryManager = value;
    }

    /**
     * Getter method used to access the antisamyManager bean.
     *
     * @return the antisamyManager bean.
     */
    public AntisamyManager getAntisamyManager() {
        return antisamyManager;
    }

    /**
     * Setter method used by spring to inject a AntisamyManager bean.
     *
     * @param value a antisamyManager bean.
     */
    public void setAntisamyManager(final AntisamyManager value) {
        this.antisamyManager = value;
    }

    PostMessageManagerBean postMessageManager;

    public void setPostMessageManager(PostMessageManagerBean postMessageManager) {
        this.postMessageManager = postMessageManager;
    }
}
