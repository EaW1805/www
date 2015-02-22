package com.eaw1805.www.controllers;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.cache.Cachable;
import com.eaw1805.data.dto.web.ChatMessageDTO;
import com.eaw1805.data.managers.beans.AchievementManagerBean;
import com.eaw1805.data.managers.beans.ActiveUserManagerBean;
import com.eaw1805.data.managers.beans.ChatMessageManagerBean;
import com.eaw1805.data.managers.beans.FollowManagerBean;
import com.eaw1805.data.managers.beans.MessageManagerBean;
import com.eaw1805.data.managers.beans.UserGameManagerBean;
import com.eaw1805.data.managers.beans.UserManagerBean;
import com.eaw1805.data.managers.beans.WatchGameManagerBean;
import com.eaw1805.data.model.Achievement;
import com.eaw1805.data.model.ActiveUser;
import com.eaw1805.data.model.ChatMessage;
import com.eaw1805.data.model.Follow;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.data.model.WatchGame;
import com.eaw1805.www.controllers.cache.helper.GameHelperBean;
import com.eaw1805.www.controllers.site.ArticleManager;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Methods used by all controllers.
 */
public class BaseController {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LogManager.getLogger(BaseController.class);

    /**
     * Instance of UserManager class.
     */
    @Autowired
    @Qualifier("userManagerBean")
    protected transient UserManagerBean userManager;

    /**
     * Instance of MessageManager class.
     */
    @Autowired
    @Qualifier("messageManagerBean")
    protected transient MessageManagerBean messageManager;

    /**
     * Instance NationManager class to perform queries
     * about nation objects.
     */
    @Autowired
    @Qualifier("followManagerBean")
    protected transient FollowManagerBean followManager;

    /**
     * Instance NationManager class to perform queries
     * about nation objects.
     */
    @Autowired
    @Qualifier("watchGameManagerBean")
    protected transient WatchGameManagerBean watchGameManager;

    /**
     * Instance GameHelperBean class.
     */
    @Autowired
    @Qualifier("gameHelperBean")
    protected transient GameHelperBean gameHelper;

    /**
     * Instance NationManager class to perform queries
     * about nation objects.
     */
    @Autowired
    @Qualifier("usersGamesManagerBean")
    protected transient UserGameManagerBean userGameManager;

    /**
     * Instance of ArticleManager.
     */
    @Autowired
    @Qualifier("articleManagerBean")
    protected transient ArticleManager articleManager;

    /**
     * Instance of ActiveUsersManager.
     */
    @Autowired
    @Qualifier("activeUserManagerBean")
    private ActiveUserManagerBean activeUsersManager;

    /**
     * Instance of ChatManager.
     */
    @Autowired
    @Qualifier("chatMessageManagerBean")
    private ChatMessageManagerBean chatManager;

    /**
     * Instance of AchievementManager.
     */
    @Autowired
    @Qualifier("achievementManagerBean")
    private AchievementManagerBean achievementManager;


    @ModelAttribute("user")
    public User loggedInUser() {
        return getUser();
    }

    @ModelAttribute("activeUsers")
    public List<ActiveUser> activeUsers() {
        return activeUsersManager.list();
    }

    @ModelAttribute("userDateJoined")
    public Date userDateJoined() {
        final User thisUser = getUser();
        if (thisUser == null) {
            return new Date();
        }

        return new Date(getUser().getDateJoin() * 1000);
    }

    @ModelAttribute("followingCnt")
    public Integer getFollowingCnt() {
        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() == -1) {
            return 0;
        }

        return followManager.listByFollower(getUser(), false).size();
    }

    @ModelAttribute("followingList")
    public List<User> getFollowingList() {
        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() == -1) {
            return new ArrayList<User>();
        }

        //get users that this person follows.
        final List<Follow> following = followManager.listByFollower(thisUser, false);
        final List<User> followingList = new ArrayList<User>();
        int count = 0;
        for (final Follow follow : following) {
            count++;
            if (count > 10) {
                break;
            }
            followingList.add(follow.getLeader());
        }

        return followingList;
    }

    @ModelAttribute("followersCnt")
    public Integer getFollowersCnt() {
        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() == -1) {
            return 0;
        }

        return followManager.listByLeader(thisUser, false).size();
    }

    @ModelAttribute("followersList")
    public List<User> getFollowers() {
        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() == -1) {
            return new ArrayList<User>();
        }

        //get this user followers.
        final List<Follow> followers = followManager.listByLeader(thisUser, false);
        int count = 0;
        final List<User> followersList = new ArrayList<User>();
        for (final Follow follow : followers) {
            count++;
            if (count > 10) {
                break;
            }
            followersList.add(follow.getFollower());
        }

        return followersList;
    }

    @ModelAttribute("watchCnt")
    public Integer getWatchedGamesCnt() {
        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() == -1) {
            return 0;
        }

        int count = 0;
        final List<Game> watchGames = new ArrayList<Game>();
        for (int db = ScenarioContextHolder.FIRST_SCENARIO; db <= ScenarioContextHolder.LAST_SCENARIO; db++) {
            ScenarioContextHolder.setScenario(db);
            count += watchGameManager.listByUser(thisUser).size();
        }

        return count;
    }

    @ModelAttribute("gameList")
    public List<Game> getWatchedGames() {
        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() == -1) {
            return new ArrayList<Game>();
        }

        int count = 0;
        final List<Game> watchGames = new ArrayList<Game>();
        for (int db = ScenarioContextHolder.FIRST_SCENARIO; db <= ScenarioContextHolder.LAST_SCENARIO; db++) {
            ScenarioContextHolder.setScenario(db);

            final List<WatchGame> watchedGames = watchGameManager.listByUser(getUser());
            for (final WatchGame watchGame : watchedGames) {
                count++;
                if (count > 15) {
                    break;
                }
                watchGames.add(watchGame.getGame());
            }
        }

        return watchGames;
    }

    @ModelAttribute("profileUser")
    public User profileUser() {
        return getUser();
    }

    @ModelAttribute("allUsers")
    public List<User> getAllUsers() {
        return userManager.list();
    }

    @ModelAttribute("unreadMessagesCount")
    public int countUnreadMessages() {
        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() == -1) {
            return 0;
        }

        return messageManager.countUnreadMessagesByReceiver(thisUser);
    }

    @ModelAttribute("globalMessages")
    public List<ChatMessageDTO> getMessages() {
        return getGlobalChatMessages();
    }

    @ModelAttribute("userNewAchievements")
    public List<Achievement> getAchievements() {
        return getNewAchievements();
    }

    /**
     * Retrieve all user games for the current user that should appear as a popup.
     *
     * @return A list of user games.
     */
    @ModelAttribute("userGameResults")
    public List<UserGame> getUserGamesWithResult() {
        final List<UserGame> out = new ArrayList<UserGame>();

        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() == -1) {
            return new ArrayList<UserGame>();
        }
        //Search for non drop user games.
        for (int scenario = HibernateUtil.DB_FIRST; scenario <= HibernateUtil.DB_LAST; scenario++) {
             ScenarioContextHolder.setScenario(scenario);

            final List<UserGame> currentUserGames = getUserGameManager().list(thisUser);
            for (UserGame userGame : currentUserGames) {
                if (userGame.getUserId() == 2) {
                    continue;
                }
                if (!userGame.isResultViewed()) {
                    if (userGame.getGame().getEnded()) {
                        out.add(userGame);
                    } else if (!userGame.isAlive()) {
                        out.add(userGame);
                    }

                }
            }
            //Search for drops
            final List<UserGame> dropUserGames = getUserGameManager().listInActive(thisUser);
            for (UserGame userGame : dropUserGames) {
                if (userGame.getUserId() == 2) {
                    continue;
                }
                if (!userGame.isResultViewed()) {
                    if (userGame.getTurnDrop() > userGame.getTurnPickUp()) {
                        out.add(userGame);
                    }
                }

            }
        }

        return out;
    }

    /**
     * Retrieve the user object from the database.
     *
     * @return the User entity.
     */
    protected final User getUser() {
        User thisUser = new User();
        try {
            // Retrieve principal object
            final UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Lookup user based on username
            thisUser = userManager.getByUserName(principal.getUsername());

        } catch (Exception ex) {
            // do nothing
            thisUser.setUserId(-1);
            thisUser.setUsername("anonymous");
        }

        try {
            // Retrieve remote IP
            final String ipAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("CF-Connecting-IP");
            if (ipAddress == null) {
                thisUser.setRemoteAddress(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr());

            } else {
                thisUser.setRemoteAddress(ipAddress);
            }

        } catch (Exception ex) {
            // do nothing
            thisUser.setRemoteAddress("unknown");
        }

        return thisUser;
    }

    /**
     * Encrypts the password string of the new user.
     *
     * @param password the password inserted to the form
     * @return the encrypted password.
     * @throws java.security.NoSuchAlgorithmException
     *          No encryption algorithm exception.
     */
    protected String convertToMD5(final String password) throws NoSuchAlgorithmException {

        final MessageDigest messageDialect = MessageDigest.getInstance("MD5");
        messageDialect.update(password.getBytes());

        final byte byteData[] = messageDialect.digest();

        //convert the byte to hex format
        final StringBuilder hexString = new StringBuilder();

        for (final byte singleByte : byteData) {
            final String hex = Integer.toHexString(0xff & singleByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public Map<Integer, Integer> getWatchedGames(final User thisUser) {
        final Map<Integer, Integer> watchGames = new HashMap<Integer, Integer>();

        if (thisUser == null || thisUser.getUserId() == -1) {
            return watchGames;
        }

        // Iterate through watched games
        final List<WatchGame> watchedGames = watchGameManager.listByUser(getUser());
        for (final WatchGame watchGame : watchedGames) {
            watchGames.put(watchGame.getGame().getGameId(), 1);
        }

        // Iterate through games played
        final List<UserGame> userGameslist = userGameManager.list(thisUser);
        for (final UserGame playedGame : userGameslist) {
            watchGames.put(playedGame.getGame().getGameId(), 2);
        }

        return watchGames;
    }

    @Cachable(cacheName = "gameCache")
    protected Map<Integer, String> constructMonths(final Game thisGame) {
        Map<Integer, String> months = new TreeMap<Integer, String>();
        final Calendar thatCal = calendar(thisGame, 0);
        for (int turn = 0; turn <= thisGame.getTurn(); turn++) {
            final StringBuilder strBufff = new StringBuilder();
            strBufff.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
            strBufff.append("&nbsp;");
            strBufff.append(thatCal.get(Calendar.YEAR));
            months.put(turn, strBufff.toString());
            thatCal.add(Calendar.MONTH, 1);
        }
        return months;
    }


    protected List<ChatMessageDTO> getGlobalChatMessages() {
        final List<ChatMessage> chatMessageList = chatManager.listGlobal();

        final List<ChatMessageDTO> chatMessages = new LinkedList<ChatMessageDTO>();

        final ObjectMapper objectMapper = new ObjectMapper();

        for (final ChatMessage chatMessage : chatMessageList) {
            try {

                final ChatMessageDTO chatMessageDTO
                        = objectMapper.readValue(chatMessage.getMessage(), ChatMessageDTO.class);

                chatMessages.add(chatMessageDTO);
            } catch (final Exception e) {
                LOGGER.error(e);
            }
        }
        return chatMessages;
    }

    public List<Achievement> getNewAchievements() {
        if (getUser() == null) {
            return new ArrayList<Achievement>();
        }
        return achievementManager.listNew(getUser().getUserId());
    }

    public final String formatCalendar(final Calendar thatCal) {
        final StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuilder.append(" ");
        strBuilder.append(thatCal.get(Calendar.YEAR));
        return strBuilder.toString();
    }

    /**
     * Get the current calendar.
     *
     * @param thisGame the Game instance.
     * @return the calendar.
     */
    public final Calendar calendar(final Game thisGame) {
        final Calendar thisCal;
        switch (thisGame.getScenarioId()) {
            case HibernateUtil.DB_S1:
                if (thisGame.getGameId() < 8) {
                    thisCal = calendar(1805, Calendar.JANUARY, thisGame.getTurn());

                } else {
                    thisCal = calendar(1802, Calendar.APRIL, thisGame.getTurn());
                }
                break;

            case HibernateUtil.DB_S2:
                thisCal = calendar(1805, Calendar.JANUARY, thisGame.getTurn());
                break;

            case HibernateUtil.DB_S3:
                thisCal = calendar(1808, Calendar.SEPTEMBER, thisGame.getTurn());
                break;

            case HibernateUtil.DB_FREE:
            default:
                thisCal = calendar(1804, Calendar.JANUARY, thisGame.getTurn());

        }

        return thisCal;
    }

    /**
     * Get the current calendar.
     *
     * @param thisGame the Game instance.
     * @param turn     the Turn to calculate.
     * @return the calendar.
     */
    public final Calendar calendar(final Game thisGame, final int turn) {
        final Calendar thisCal;
        switch (thisGame.getScenarioId()) {
            case HibernateUtil.DB_S1:
                if (thisGame.getGameId() < 8) {
                    thisCal = calendar(1805, Calendar.JANUARY, turn);

                } else {
                    thisCal = calendar(1802, Calendar.APRIL, turn);
                }
                break;

            case HibernateUtil.DB_S2:
                thisCal = calendar(1805, Calendar.JANUARY, turn);
                break;

            case HibernateUtil.DB_S3:
                thisCal = calendar(1808, Calendar.SEPTEMBER, turn);
                break;

            case HibernateUtil.DB_FREE:
            default:
                thisCal = calendar(1804, Calendar.JANUARY, turn);

        }

        return thisCal;
    }

    /**
     * Get the current calendar.
     *
     * @param turn          the Turn to calculate.
     * @param startingYear  the starting year of the scenario.
     * @param startingMonth the starting month of the scenario.
     * @return the calendar.
     */
    protected final Calendar calendar(final int startingYear, final int startingMonth, final int turn) {
        final Calendar thisCal = Calendar.getInstance();
        thisCal.set(startingYear, startingMonth, 1);
        thisCal.add(Calendar.MONTH, turn);
        return thisCal;
    }

    /**
     * Get the bean for handling requests regarding User entities.
     *
     * @return the bean for handling requests regarding User entities.
     */
    public UserManagerBean getUserManager() {
        return userManager;
    }

    public ArticleManager getArticleManager() {
        return articleManager;
    }

    /**
     * Getter method used to access the userGameManagerionManager bean.
     *
     * @return the userGameManagerionManager bean.
     */
    public UserGameManagerBean getUserGameManager() {
        return userGameManager;
    }

    
    /**
     * Getter method used to access the AchievementManager bean.
     *
     * @return the achievementManager bean.
     */
    public AchievementManagerBean getAchievementManager() {
        return achievementManager;
    }

}
