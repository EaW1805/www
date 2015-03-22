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
import com.eaw1805.data.model.paypal.PaypalTransaction;
import com.eaw1805.www.controllers.cache.async.EawAsync;
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

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
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
import java.util.Properties;
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

    private static final String SMTP_HOST_NAME = "localhost";
    private static final String SMTP_AUTH_USER = "engine";
    private static final String SMTP_AUTH_PWD = "eng123456passwd";
    private static final boolean SMTP_SSL = false;

    /**
     * Send an e-mail.
     *
     * @param sender   Sender email.
     * @param fullName Sender's full name.
     * @param subject  the subject of the e-mail.
     * @param text     the body of the e-mail.
     */
    @EawAsync
    protected void sendContact(final String sender, final String fullName, final String subject, final String text)
            throws MessagingException, UnsupportedEncodingException {

        // Sender's email ID needs to be mentioned
        final String from = "engine@oplon-games.com";

        // Get system properties
        final Properties properties = prepareProperties();

        // Authenticate
        final Authenticator auth = new SMTPAuthenticator();

        // Get the default Session object.
        final Session session = Session.getInstance(properties, auth);
        session.setDebug(false);

        // Create a default MimeMessage object.
        final MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        final InternetAddress senderAddr = new InternetAddress(from);
        senderAddr.setPersonal("EaW1805 Team");
        message.setFrom(senderAddr);

        // Set To: header field of the header.
        message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress("support@eaw1805.com", "Empires at War 1805 Team"));

        // Set Subject: header field
        message.setSubject("[EaW1805] " + subject);

        // Set reply-to
        final Address replyAddr[] = new Address[1];
        final InternetAddress reply = new InternetAddress(sender);
        reply.setPersonal(fullName);
        replyAddr[0] = reply;
        message.setReplyTo(replyAddr);

        message.addRecipient(javax.mail.Message.RecipientType.CC, new InternetAddress(sender, fullName));

        // Set the email message text.
        final MimeBodyPart messagePart = new MimeBodyPart();
        messagePart.setText(text);

        // Set the email attachment file 1
        final MimeBodyPart attachmentPart1 = new MimeBodyPart();
        final FileDataSource fileDataSource1 = new FileDataSource("/srv/eaw1805/images/logo/oplon-games.png") {
            @Override
            public String getContentType() {
                return "image/png";
            }
        };
        attachmentPart1.setDataHandler(new DataHandler(fileDataSource1));
        attachmentPart1.setFileName(fileDataSource1.getName());
        attachmentPart1.setDescription("Oplon Games");

        final Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messagePart);
        multipart.addBodyPart(attachmentPart1);

        message.setContent(multipart);

        // Send message
        final Transport transport = session.getTransport();
        transport.connect();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        LOGGER.info("Contact Form sent for " + sender);
    }

    /**
     * Send notification for new inbox message.
     *
     * @param thisMessage The new message.
     */
    @EawAsync
    protected void sendMessageNotification(final com.eaw1805.data.model.Message thisMessage) {
        try {
            // Sender's email ID needs to be mentioned
            final String from = "engine@oplon-games.com";

            // Get system properties
            final Properties properties = prepareProperties();

            // Authenticate
            final Authenticator auth = new SMTPAuthenticator();

            // Get the default Session object.
            final Session session = Session.getInstance(properties, auth);
            session.setDebug(false);

            // Create a default MimeMessage object.
            final MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            final InternetAddress senderAddr = new InternetAddress(from);
            senderAddr.setPersonal("EaW1805 Team");
            message.setFrom(senderAddr);

            // Set Reply-to
            if (thisMessage.getSender().getPublicMail()) {
                // Set reply-to
                final Address replyAddr[] = new Address[1];
                final InternetAddress reply = new InternetAddress(thisMessage.getSender().getEmail(), thisMessage.getSender().getUsername());
                reply.setPersonal(thisMessage.getSender().getUsername());
                replyAddr[0] = reply;
                message.setReplyTo(replyAddr);
            }

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(thisMessage.getReceiver().getEmail(),
                    thisMessage.getReceiver().getFullname()));

            // Set Subject: header field
            message.setSubject("[EaW1805] " + thisMessage.getSubject());

            // Prepare text
            final StringBuilder strText = new StringBuilder();
            strText.append(thisMessage.getSender().getUsername()).append(" sent you a message.\n\n");
            strText.append("Subject:\t\t");
            strText.append(thisMessage.getSubject());
            strText.append("\n\n");

            strText.append(thisMessage.getBodyMessage());
            strText.append("\n\n");

            strText.append("To reply to this message, follow the link below:\n");
            strText.append("http://www.eaw1805.com/inbox/private/"
                    + thisMessage.getRootId() + "/view");
            strText.append("\n\n");

            strText.append("Want to control which emails you receive from EaW 1805? Go to:\n");
            strText.append("http://www.eaw1805.com/settings");
            strText.append("\n\n");

            // Set the email message text.
            final MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setText(strText.toString());

            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);

            message.setContent(multipart);

            // Send message
            final Transport transport = session.getTransport();
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            LOGGER.info("Inbox notification sent for " + thisMessage.getReceiver().getUsername() + "/" + thisMessage.getReceiver().getFullname());

        } catch (final Exception e) {
            LOGGER.error("Exception: ", e);
        }
    }

    /**
     * Send an e-mail.
     *
     * @param newUser The new user.
     */
    @EawAsync
    protected void sendWelcome(final User newUser)
            throws MessagingException, UnsupportedEncodingException {

        // Sender's email ID needs to be mentioned
        final String from = "engine@oplon-games.com";

        // Get system properties
        final Properties properties = prepareProperties();

        // Authenticate
        final Authenticator auth = new SMTPAuthenticator();

        // Get the default Session object.
        final Session session = Session.getInstance(properties, auth);
        session.setDebug(false);

        // Create a default MimeMessage object.
        final MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        final InternetAddress senderAddr = new InternetAddress(from);
        senderAddr.setPersonal("EaW1805 Team");
        message.setFrom(senderAddr);
        message.addRecipient(Message.RecipientType.BCC, new InternetAddress("admin@eaw1805.com", "Empires at War 1805 Team"));

        // Set Subject: header field
        message.setSubject("Welcome to Empires at War 1805");

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(newUser.getEmail(), newUser.getFullname()));

        // Prepare text
        final StringBuilder strText = new StringBuilder();
        strText.append("Dear ");
        strText.append(newUser.getUsername());
        strText.append(",\n\n");
        strText.append("Welcome to Empires at War 1805.\n\n");
        strText.append("You can log in to your account via the following link:\n\n");
        strText.append("http://www.eaw1805.com/login\n\n");

        strText.append("We have added 50 free credits to your account so that you can start playing without delays.\n\n");

        strText.append("*SPECIAL OFFERS*: \n\n");

        //strText.append("  o If you are a student then you are entitled to 150 additional free credits. Send us an e-mail to support@eaw1805.com and we will add the credits to your account!\n\n");

        strText.append("  o Get 200 free credits by spreading the word of EAW to your friends. Follow the 6 simple steps:\n");
        strText.append("    http://www.eaw1805.com/settings\n\n");

        strText.append("  o Get 60 free credits by picking up an position in an on-going EAW game. Check out the available positions:\n");
        strText.append("    http://www.eaw1805.com/joingame/running\n\n");

        strText.append("You can find valuable information about the game in the Quick Start Guide:\n\n");
        strText.append("http://www.eaw1805.com/help/introduction\n\n");

        strText.append("We have prepared a set of video tutorials to help you get useful insights on the game mechanics:\n\n");
        strText.append("http://www.eaw1805.com/help\n\n");

        strText.append("Detailed information about the game rules and the mechanisms of Empires at War 1805 are available in the Players' Handbook:\n\n");
        strText.append("http://www.eaw1805.com/handbook\n\n");

        strText.append("For questions about the rules and the user interface, we suggest that you use the forums where all players post their comments along with replies from the GMs:\n\n");
        strText.append("http://forum.eaw1805.com\n\n");

        strText.append("We hope that you will enjoy playing Empires at War 1805,\nOplon Games");

        // Set the email message text.
        final MimeBodyPart messagePart = new MimeBodyPart();
        messagePart.setText(strText.toString());

        // Set the email attachment file 1
        final MimeBodyPart attachmentPart1 = new MimeBodyPart();
        final FileDataSource fileDataSource1 = new FileDataSource("/srv/eaw1805/images/logo/oplon-games.png") {
            @Override
            public String getContentType() {
                return "image/png";
            }
        };
        attachmentPart1.setDataHandler(new DataHandler(fileDataSource1));
        attachmentPart1.setFileName(fileDataSource1.getName());
        attachmentPart1.setDescription("Oplon Games");

        final Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messagePart);
        multipart.addBodyPart(attachmentPart1);

        message.setContent(multipart);

        // Send message
        try {
            final Transport transport = session.getTransport();
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            LOGGER.info("New User Form sent for " + newUser.getUsername() + "/" + newUser.getFullname());

        } catch (Exception ex) {
            LOGGER.info("Failed to send New User Form for " + newUser.getUsername() + "/" + newUser.getFullname());
        }
    }

    /**
     * Send an e-mail.
     *
     * @param thisUser The new user.
     */
    @EawAsync
    protected void sendUsername(final User thisUser)
            throws MessagingException, UnsupportedEncodingException {

        // Sender's email ID needs to be mentioned
        final String from = "engine@oplon-games.com";

        // Get system properties
        final Properties properties = prepareProperties();

        // Authenticate
        final Authenticator auth = new SMTPAuthenticator();

        // Get the default Session object.
        final Session session = Session.getInstance(properties, auth);
        session.setDebug(false);

        // Create a default MimeMessage object.
        final MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        final InternetAddress senderAddr = new InternetAddress(from);
        senderAddr.setPersonal("EaW1805 Team");
        message.setFrom(senderAddr);

        // Set Subject: header field
        message.setSubject("[EaW1805] Retrieve your Username");

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(thisUser.getEmail(), thisUser.getFullname()));

        // Prepare text
        final StringBuilder strText = new StringBuilder();
        strText.append("Dear ");
        strText.append(thisUser.getUsername());
        strText.append(",\n\n");
        strText.append("This is an automated mail generated after a request for retrieving your username for Empires at War 1805.\n\n");
        strText.append("Your username is:\n\n");
        strText.append(thisUser.getUsername());

        strText.append("\n\nYou can log in to your account via the following link:\n\n");
        strText.append("http://www.eaw1805.com/login\n\n");

        strText.append("We hope that you enjoy playing Empires at War 1805,\nOplon Games");

        // Set the email message text.
        final MimeBodyPart messagePart = new MimeBodyPart();
        messagePart.setText(strText.toString());

        // Set the email attachment file 1
        final MimeBodyPart attachmentPart1 = new MimeBodyPart();
        final FileDataSource fileDataSource1 = new FileDataSource("/srv/eaw1805/images/logo/oplon-games.png") {
            @Override
            public String getContentType() {
                return "image/png";
            }
        };
        attachmentPart1.setDataHandler(new DataHandler(fileDataSource1));
        attachmentPart1.setFileName(fileDataSource1.getName());
        attachmentPart1.setDescription("Oplon Games");

        final Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messagePart);
        multipart.addBodyPart(attachmentPart1);

        message.setContent(multipart);

        // Send message
        final Transport transport = session.getTransport();
        transport.connect();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        LOGGER.info("Username sent for " + thisUser.getUsername() + "/" + thisUser.getFullname());
    }

    /**
     * Send an e-mail.
     *
     * @param thisUser The new user.
     */
    @EawAsync
    protected void sendPaymentReceipt(final User thisUser, final int credits, final String transactionId)
            throws MessagingException, UnsupportedEncodingException {

        // Sender's email ID needs to be mentioned
        final String from = "engine@oplon-games.com";

        // Get system properties
        final Properties properties = prepareProperties();

        // Authenticate
        final Authenticator auth = new SMTPAuthenticator();

        // Get the default Session object.
        final Session session = Session.getInstance(properties, auth);
        session.setDebug(false);

        // Create a default MimeMessage object.
        final MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        final InternetAddress senderAddr = new InternetAddress(from);
        senderAddr.setPersonal("EaW1805 Team");
        message.setFrom(senderAddr);
        message.addRecipient(Message.RecipientType.BCC, new InternetAddress("engine@eaw1805.com", "Empires at War 1805 Team"));

        // Set Subject: header field
        message.setSubject("[EaW1805] Payment Received");

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(thisUser.getEmail(), thisUser.getFullname()));

        // Prepare text
        final StringBuilder strText = new StringBuilder();
        strText.append("Dear ");
        strText.append(thisUser.getUsername());
        strText.append(",\n\n");
        strText.append("This is an automated mail generated after your credit purchase for Empires at War 1805.\n\n");
        strText.append("You have purchased ");
        strText.append(credits);
        strText.append(" credits.\n\n");

        strText.append("If you wish to receive a receipt for your payment, you need to fill in following form:\n\n");
        strText.append("http://www.eaw1805.com/receipt/id/");
        strText.append(transactionId);
        strText.append("\n\n");

        strText.append("We hope that you enjoy playing Empires at War 1805,\nOplon Games");

        // Set the email message text.
        final MimeBodyPart messagePart = new MimeBodyPart();
        messagePart.setText(strText.toString());

        // Set the email attachment file 1
        final MimeBodyPart attachmentPart1 = new MimeBodyPart();
        final FileDataSource fileDataSource1 = new FileDataSource("/srv/eaw1805/images/logo/oplon-games.png") {
            @Override
            public String getContentType() {
                return "image/png";
            }
        };
        attachmentPart1.setDataHandler(new DataHandler(fileDataSource1));
        attachmentPart1.setFileName(fileDataSource1.getName());
        attachmentPart1.setDescription("Oplon Games");

        final Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messagePart);
        multipart.addBodyPart(attachmentPart1);

        message.setContent(multipart);

        // Send message
        final Transport transport = session.getTransport();
        transport.connect();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        LOGGER.info("Payment notification sent for " + thisUser.getUsername() + "/" + thisUser.getFullname());
    }

    /**
     * Send an e-mail.
     *
     * @param thisUser The new user.
     */
    @EawAsync
    protected void sendPaymentReceiptRequest(final User thisUser, final PaypalTransaction thisTrans)
            throws MessagingException, UnsupportedEncodingException {

        // Sender's email ID needs to be mentioned
        final String from = "engine@oplon-games.com";

        // Get system properties
        final Properties properties = prepareProperties();

        // Authenticate
        final Authenticator auth = new SMTPAuthenticator();

        // Get the default Session object.
        final Session session = Session.getInstance(properties, auth);
        session.setDebug(false);

        // Create a default MimeMessage object.
        final MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        final InternetAddress senderAddr = new InternetAddress(from);
        senderAddr.setPersonal("EaW1805 Team");
        message.setFrom(senderAddr);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("payment@eaw1805.com", "Empires at War 1805 Team"));

        // Set Subject: header field
        message.setSubject("[EaW1805] Payment Received");

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.CC, new InternetAddress(thisUser.getEmail(), thisUser.getFullname()));

        // Set reply-to
        final Address replyAddr[] = new Address[1];
        final InternetAddress reply = new InternetAddress(thisUser.getEmail());
        reply.setPersonal(thisUser.getFullname());
        replyAddr[0] = reply;
        message.setReplyTo(replyAddr);

        // Prepare text
        final StringBuilder strText = new StringBuilder();
        strText.append("This is an automated mail generated by the user's request for a receipt of credit purchase for Empires at War 1805.\n\n");
        strText.append("User:\t\t");
        strText.append(thisUser.getUsername());
        strText.append("\n\n");

        strText.append("Credits:\t");
        strText.append(thisTrans.getPmHistory().getChargeBought());
        strText.append("\n\n");

        strText.append("Amount:\t\t");
        strText.append(thisTrans.getGrossAmount());
        strText.append(" Euro\n\n");

        strText.append("Postal Address:\n\n");
        strText.append(thisTrans.getPayerName());
        strText.append("\n");
        strText.append(thisTrans.getPayerAddress());
        strText.append("\n");
        strText.append(thisTrans.getPayerCity());
        strText.append(" ");
        strText.append(thisTrans.getPayerPOCode());
        strText.append("\n");
        strText.append(thisTrans.getPayerCountry());
        strText.append("\n\n");

        strText.append("You can review your PayPal transaction via the following link:\n\n");
        strText.append("https://www.paypal.com/vst/id=");
        strText.append(thisTrans.getTransactionId());
        strText.append("\n\n");

        // Set the email message text.
        final MimeBodyPart messagePart = new MimeBodyPart();
        messagePart.setText(strText.toString());

        // Set the email attachment file 1
        final MimeBodyPart attachmentPart1 = new MimeBodyPart();
        final FileDataSource fileDataSource1 = new FileDataSource("/srv/eaw1805/images/logo/oplon-games.png") {
            @Override
            public String getContentType() {
                return "image/png";
            }
        };
        attachmentPart1.setDataHandler(new DataHandler(fileDataSource1));
        attachmentPart1.setFileName(fileDataSource1.getName());
        attachmentPart1.setDescription("Oplon Games");

        final Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messagePart);
        multipart.addBodyPart(attachmentPart1);

        message.setContent(multipart);

        // Send message
        final Transport transport = session.getTransport();
        transport.connect();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        LOGGER.info("Payment receipt request sent for " + thisUser.getUsername() + "/" + thisUser.getFullname());
    }

    /**
     * Send an e-mail.
     *
     * @param thisUser The new user.
     */
    @EawAsync
    protected void sendPassword(final User thisUser, final String newPassword)
            throws MessagingException, UnsupportedEncodingException {

        // Sender's email ID needs to be mentioned
        final String from = "engine@oplon-games.com";

        // Get system properties
        final Properties properties = prepareProperties();

        // Authenticate
        final Authenticator auth = new SMTPAuthenticator();

        // Get the default Session object.
        final Session session = Session.getInstance(properties, auth);
        session.setDebug(false);

        // Create a default MimeMessage object.
        final MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        final InternetAddress senderAddr = new InternetAddress(from);
        senderAddr.setPersonal("EaW1805 Team");
        message.setFrom(senderAddr);

        // Set Subject: header field
        message.setSubject("[EaW1805] Retrieve your Username");

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(thisUser.getEmail(), thisUser.getFullname()));

        // Prepare text
        final StringBuilder strText = new StringBuilder();
        strText.append("Dear ");
        strText.append(thisUser.getUsername());
        strText.append(",\n\n");
        strText.append("This is an automated mail generated after a request for retrieving your passowrd for Empires at War 1805.\n\n");
        strText.append("Your username is:\n\n");
        strText.append(thisUser.getUsername());
        strText.append("Your new password is:\n\n");
        strText.append(newPassword);

        strText.append("\n\nYou can log in to your account via the following link:\n\n");
        strText.append("http://www.eaw1805.com/login\n\n");

        strText.append("\n\nFor security reasons, we highly recommend to change your password as soon as you log in to the system via the following link:\n\n");
        strText.append("http://www.eaw1805.com/settings\n\n");

        strText.append("We hope that you enjoy playing Empires at War 1805,\nOplon Games");

        // Set the email message text.
        final MimeBodyPart messagePart = new MimeBodyPart();
        messagePart.setText(strText.toString());

        // Set the email attachment file 1
        final MimeBodyPart attachmentPart1 = new MimeBodyPart();
        final FileDataSource fileDataSource1 = new FileDataSource("/srv/eaw1805/images/logo/oplon-games.png") {
            @Override
            public String getContentType() {
                return "image/png";
            }
        };
        attachmentPart1.setDataHandler(new DataHandler(fileDataSource1));
        attachmentPart1.setFileName(fileDataSource1.getName());
        attachmentPart1.setDescription("Oplon Games");

        final Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messagePart);
        multipart.addBodyPart(attachmentPart1);

        message.setContent(multipart);

        // Send message
        final Transport transport = session.getTransport();
        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
        LOGGER.info("New Password sent for " + thisUser.getUsername() + "/" + thisUser.getFullname());
    }

    /**
     * Send notification to a user that a game starts.
     * Game has already been updated and has the next process date updated.
     *
     * @param game The game to send the notification for.
     * @param user The user to send the notification.
     */
    @EawAsync
    protected void sendGameLaunchNotification(final Game game, final User user) {
        // Sender's email ID needs to be mentioned
        final String from = "engine@oplon-games.com";

        // Get system properties
        final Properties properties = prepareProperties();

        // Authenticate
        final Authenticator auth = new SMTPAuthenticator();

        // Get the default Session object.
        final Session session = Session.getInstance(properties, auth);
        session.setDebug(false);

        try {
            // Create a default MimeMessage object.
            final MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            final InternetAddress senderAddr = new InternetAddress(from);
            senderAddr.setPersonal("EaW1805 Team");
            message.setFrom(senderAddr);

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("engine@eaw1805.com", "Empires at War 1805 Team"));

            // Set Subject: header field
            message.setSubject("[EaW1805] Launch of G" + game.getGameId());

            // Set reply-to
            final Address replyAddr[] = new Address[1];
            final InternetAddress reply = new InternetAddress("support@eaw1805.com", "Empires at War 1805 Team");
            reply.setPersonal("EaW1805 Support");
            replyAddr[0] = reply;
            message.setReplyTo(replyAddr);

            StringBuilder text = new StringBuilder();
            text.append("Dear ").append(user.getUsername()).append(",\n\n");
            text.append("Game G").append(game.getGameId()).append(" has started!\n\n");

            // Add link to game info page
            text.append("You can play your nation via the following link:\n\n");
            text.append("http://www.eaw1805.com/play/scenario/").append(game.getScenarioIdToString()).append("/game/");
            text.append(game.getGameId());
            text.append("\n\n");

            // Report new deadline
            text.append("The first turn will be processed on ");
            text.append(game.getDateNextProc());
            text.append("\n\n");

            text.append("To get more details about the game click here:\nhttp://www.eaw1805.com/scenario/").append(game.getScenarioIdToString()).append("/game/").append(game.getGameId()).append("/info\n\n");
            text.append("Good luck & Have fun!\n");
            text.append("The Eaw 1805 team\n");

            // Set the email message text.
            final MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setText(text.toString());

            // Set the email attachment file 1
            final MimeBodyPart attachmentPart1 = new MimeBodyPart();
            final FileDataSource fileDataSource1 = new FileDataSource("/srv/eaw1805/images/logo/oplon-games.png") {
                @Override
                public String getContentType() {
                    return "image/png";
                }
            };
            attachmentPart1.setDataHandler(new DataHandler(fileDataSource1));
            attachmentPart1.setFileName(fileDataSource1.getName());
            attachmentPart1.setDescription("Oplon Games");

            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);
            multipart.addBodyPart(attachmentPart1);

            message.setContent(multipart);

            // Send message
            final Transport transport = session.getTransport();
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            LOGGER.info("Sent game launched notification to " + user.getEmail());

        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex);

        } catch (MessagingException mex) {
            LOGGER.error(mex);
        }
    }

    /**
     * Send notification to a user that a game got canceled.
     * To be called only for players that joined this game.
     *
     * @param game The game to send the email for.
     * @param user The user to send the email.
     */
    @EawAsync
    protected void sendGameCancellationNotification(final Game game, final User user) {
        // Sender's email ID needs to be mentioned
        final String from = "engine@oplon-games.com";

        // Get system properties
        final Properties properties = prepareProperties();

        // Authenticate
        final Authenticator auth = new SMTPAuthenticator();

        // Get the default Session object.
        final Session session = Session.getInstance(properties, auth);
        session.setDebug(false);

        try {
            // Create a default MimeMessage object.
            final MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            final InternetAddress senderAddr = new InternetAddress(from);
            senderAddr.setPersonal("EaW1805 Team");
            message.setFrom(senderAddr);

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("engine@eaw1805.com", "Empires at War 1805 Team"));

            // Set Subject: header field
            message.setSubject("[EaW1805] Cancellation of G" + game.getGameId());

            // Set reply-to
            final Address replyAddr[] = new Address[1];
            final InternetAddress reply = new InternetAddress("support@eaw1805.com", "Empires at War 1805 Team");
            reply.setPersonal("EaW1805 Support");
            replyAddr[0] = reply;
            message.setReplyTo(replyAddr);

            StringBuilder text = new StringBuilder();
            text.append("Dear ").append(user.getUsername()).append(",\n\n");
            text.append("Game G").append(game.getGameId()).append(" was just cancelled...\n\n");

            text.append("You can talk with the Game Creator for further details via personal messaging.\n\n");

            text.append("If you wish to play another position in an on-going EAW game, check out the available positions:\n");
            text.append("    http://www.eaw1805.com/joingame/running\n\n");

            text.append("We hope to see you in another game!\n");
            text.append("The Eaw 1805 team\n");

            // Set the email message text.
            final MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setText(text.toString());

            // Set the email attachment file 1
            final MimeBodyPart attachmentPart1 = new MimeBodyPart();
            final FileDataSource fileDataSource1 = new FileDataSource("/srv/eaw1805/images/logo/oplon-games.png") {
                @Override
                public String getContentType() {
                    return "image/png";
                }
            };
            attachmentPart1.setDataHandler(new DataHandler(fileDataSource1));
            attachmentPart1.setFileName(fileDataSource1.getName());
            attachmentPart1.setDescription("Oplon Games");

            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);
            multipart.addBodyPart(attachmentPart1);

            message.setContent(multipart);

            // Send message
            final Transport transport = session.getTransport();
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            LOGGER.info("Sent game cancellation notification to " + user.getEmail());

        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex);

        } catch (MessagingException mex) {
            LOGGER.error(mex);
        }
    }

    /**
     * Send an email to a user about a new game invitation.
     *
     * @param recipient The user to send.
     * @param gameOwner The owner of the game, same person that canceled the invitation.
     * @param userGame  The user game instance.
     */
    @EawAsync
    protected void sendInvitationNotification(final User recipient, final User gameOwner, final UserGame userGame) {
        // Sender's email ID needs to be mentioned
        final String from = "engine@oplon-games.com";

        // Get system properties
        final Properties properties = prepareProperties();

        // Authenticate
        final Authenticator auth = new SMTPAuthenticator();

        // Get the default Session object.
        final Session session = Session.getInstance(properties, auth);
        session.setDebug(false);

        try {
            // Create a default MimeMessage object.
            final MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            final InternetAddress senderAddr = new InternetAddress(from);
            senderAddr.setPersonal("EaW1805 Team");
            message.setFrom(senderAddr);

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.getEmail()));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("engine@eaw1805.com", "Empires at War 1805 Team"));

            // Set Subject: header field
            message.setSubject("[EaW1805] Invitation to join G" + userGame.getGame().getGameId());

            // Set reply-to
            final Address replyAddr[] = new Address[1];
            final InternetAddress reply = new InternetAddress("support@eaw1805.com", "Empires at War 1805 Team");
            reply.setPersonal("EaW1805 Support");
            replyAddr[0] = reply;
            message.setReplyTo(replyAddr);

            StringBuilder text = new StringBuilder();
            text.append("Dear ").append(recipient.getUsername()).append(",\n\n");
            text.append(gameOwner.getUsername()).append(" is the owner of game G").append(userGame.getGame().getGameId()).append(" and invites you to join with ").append(userGame.getNation().getName()).append("!\n\n");
            text.append("To reply to this invitation, simply login to your account and click on \"accept\" via the following link:\nhttp://www.eaw1805.com/games\n\n");
            text.append("To get more details about the game click here:\nhttp://www.eaw1805.com/scenario/").append(userGame.getGame().getScenarioIdToString()).append("/game/").append(userGame.getGame().getGameId()).append("/info\n\n");
            text.append("Check out the game creator from the following link:\nhttp://www.eaw1805.com/user/").append(gameOwner.getUsername()).append("\n\n");
            text.append("You can talk with the Game Creator about a different position or further game details directly via personal messaging.\n\n");
            text.append("Good luck & Have fun!\n");
            text.append("The Eaw 1805 team\n");

            // Set the email message text.
            final MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setText(text.toString());

            // Set the email attachment file 1
            final MimeBodyPart attachmentPart1 = new MimeBodyPart();
            final FileDataSource fileDataSource1 = new FileDataSource("/srv/eaw1805/images/logo/oplon-games.png") {
                @Override
                public String getContentType() {
                    return "image/png";
                }
            };
            attachmentPart1.setDataHandler(new DataHandler(fileDataSource1));
            attachmentPart1.setFileName(fileDataSource1.getName());
            attachmentPart1.setDescription("Oplon Games");

            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);
            multipart.addBodyPart(attachmentPart1);

            message.setContent(multipart);

            // Send message
            final Transport transport = session.getTransport();
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            LOGGER.info("Sent invitation notification to " + recipient.getEmail());

        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex);

        } catch (MessagingException mex) {
            LOGGER.error(mex);
        }
    }

    /**
     * Send an email to a user about canceling an invitation.
     *
     * @param recipient The user to send.
     * @param gameOwner The owner of the game, same person that canceled the invitation.
     * @param userGame  The user game instance.
     */
    @EawAsync
    protected void sendInvitationCancellationNotification(final User recipient, final User gameOwner, final UserGame userGame) {
        // Sender's email ID needs to be mentioned
        final String from = "engine@oplon-games.com";

        // Get system properties
        final Properties properties = prepareProperties();

        // Authenticate
        final Authenticator auth = new SMTPAuthenticator();

        // Get the default Session object.
        final Session session = Session.getInstance(properties, auth);
        session.setDebug(false);

        try {
            // Create a default MimeMessage object.
            final MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            final InternetAddress senderAddr = new InternetAddress(from);
            senderAddr.setPersonal("EaW1805 Team");
            message.setFrom(senderAddr);

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.getEmail()));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("engine@eaw1805.com", "Empires at War 1805 Team"));

            // Set Subject: header field
            message.setSubject("[EaW1805] Invitation to join G" + userGame.getGame().getGameId() + " revoked");

            // Set reply-to
            final Address replyAddr[] = new Address[1];
            final InternetAddress reply = new InternetAddress("support@eaw1805.com", "Empires at War 1805 Team");
            reply.setPersonal("EaW1805 Support");
            replyAddr[0] = reply;
            message.setReplyTo(replyAddr);

            StringBuilder text = new StringBuilder();
            text.append("Dear ").append(recipient.getUsername()).append(",\n\n");
            text.append("The invitation to join game G").append(userGame.getGame().getGameId()).append(" war revoked.\n\n");
            text.append("You can talk with the Game Creator about the game via personal messaging.\n\n");
            text.append("Check out the game creator from the following link:\nhttp://www.eaw1805.com/user/").append(gameOwner.getUsername()).append("\n\n");
            text.append("Good luck & Have fun!\n");
            text.append("The Eaw 1805 team\n");

            // Set the email message text.
            final MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setText(text.toString());

            // Set the email attachment file 1
            final MimeBodyPart attachmentPart1 = new MimeBodyPart();
            final FileDataSource fileDataSource1 = new FileDataSource("/srv/eaw1805/images/logo/oplon-games.png") {
                @Override
                public String getContentType() {
                    return "image/png";
                }
            };
            attachmentPart1.setDataHandler(new DataHandler(fileDataSource1));
            attachmentPart1.setFileName(fileDataSource1.getName());
            attachmentPart1.setDescription("Oplon Games");

            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);
            multipart.addBodyPart(attachmentPart1);

            message.setContent(multipart);

            // Send message
            final Transport transport = session.getTransport();
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            LOGGER.info("Sent invitation cancellation to " + recipient.getEmail());

        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex);

        } catch (MessagingException mex) {
            LOGGER.error(mex);
        }
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            final String username = SMTP_AUTH_USER;
            final String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }
    }

    private Properties prepareProperties() {
        // Get system properties
        final Properties properties = System.getProperties();

        // Setup mail server
        if (SMTP_SSL) {
            // Use the following if you need SSL
            properties.put("mail.smtp.socketFactory.port", 465);
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.socketFactory.fallback", "false");

            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", "465");

        } else {
            properties.put("mail.smtp.port", "25");
        }

        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", SMTP_HOST_NAME);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.user", SMTP_AUTH_USER);
        properties.put("mail.password", SMTP_AUTH_PWD);

        return properties;
    }

}
