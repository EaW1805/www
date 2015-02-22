package com.eaw1805.www.controllers.user;

import com.eaw1805.data.constants.AchievementConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.ProfileConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.managers.beans.ForumPostManagerBean;
import com.eaw1805.data.managers.beans.ProfileManagerBean;
import com.eaw1805.data.model.Achievement;
import com.eaw1805.data.model.Follow;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Profile;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.WatchGame;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.cache.helper.GameHelperBean;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * REST controller for showing current user's honour.
 */
@SuppressWarnings("restriction")
@org.springframework.stereotype.Controller
public class HonourController
        extends BaseController
        implements ReportConstants, AchievementConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(UserHonourController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/honour")
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        ScenarioContextHolder.defaultScenario();

        // retrieve user
        final User thisUser = getUser();

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

        final Profile vpsProfile = profileManager.getByOwnerKey(thisUser, ProfileConstants.VPS);
        int vps = 0;
        if (vpsProfile != null) {
            vps = vpsProfile.getValue();
        }

        // Setup map for holding values to be passed to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        refData.put("user", thisUser);
        refData.put("userDateJoined", new Date(thisUser.getDateJoin() * 1000));
        refData.put("profileUser", thisUser);
        refData.put("followingCnt", following.size());
        refData.put("followingList", followingList);
        refData.put("followersCnt", followers.size());
        refData.put("followersList", followersList);
        refData.put("watchCnt", watchedGames.size());
        refData.put("gameList", watchGames);

        refData.put("shortAchievementID", ACHIEVEMENT_ID);
        refData.put("shortAchievementLMax", ACHIEVEMENT_LMAX);
        refData.put("shortAchievementName", ACHIEVEMENT_NAMES);
        refData.put("shortAchievementStr", ACHIEVEMENT_STR);
        refData.put("shortAchievementTitle", ACHIEVEMENT_TITLE);
        refData.put("longAchievementID", ACHIEVEMENT_FULL_ID);
        refData.put("longAchievementLMAX", ACHIEVEMENT_FULL_LMAX);
        refData.put("longAchievementName", ACHIEVEMENT_FULL_NAMES);
        refData.put("longAchievementStr", ACHIEVEMENT_FULL_STR);
        refData.put("specialAchievementID", SPECIAL_ID);
        refData.put("specialAchievementStr", SPECIAL_STR);
        refData.put("specialAchievementTitle", SPECIAL_TITLE);
        refData.put("vps", vps);

        // Initialize map
        final Map<Integer, Integer> userAchievements = new HashMap<Integer, Integer>();
        for (int category : ACHIEVEMENT_ID) {
            userAchievements.put(category, 0);
        }

        // Initialize map of nation-based achievements
        final Map<Integer, Integer> nationAchievements = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> dominationAchievements = new HashMap<Integer, Integer>();
        for (int nation = NationConstants.NATION_FIRST; nation <= NationConstants.NATION_LAST; nation++) {
            nationAchievements.put(nation, 0);
            dominationAchievements.put(nation, 0);
        }

        // Initialize special achievements
        final Map<Integer, Integer> specialAchievements = new HashMap<Integer, Integer>();
        for (int level = SPECIAL_L_MIN; level <= SPECIAL_L_MAX; level++) {
            specialAchievements.put(level, 0);
        }

        // Retrieve achievements of particular user
        final List<Achievement> lstAchievements = getAchievementManager().list(thisUser.getUserId());
        for (final Achievement achievement : lstAchievements) {
            switch (achievement.getCategory()) {
                case PLAYNATION:
                    nationAchievements.put(achievement.getLevel(), 1);
                    break;

                case WINNATION:
                    dominationAchievements.put(achievement.getLevel(), 1);
                    break;

                case SPECIAL:
                    specialAchievements.put(achievement.getLevel(), 1);
                    break;

                default:
                    userAchievements.put(achievement.getCategory(), achievement.getLevel());
            }
        }

        // keep track of forum posts
        final int totPosts = forumPostsManager.getNumberOfPosts(thisUser);
        int postsLevel = 0;

        // Check if achievement is reached
        for (int level = AchievementConstants.FORUMPOSTS_L_MIN; level <= AchievementConstants.FORUMPOSTS_L_MAX; level++) {
            if (totPosts >= AchievementConstants.FORUMPOSTS_L[level]) {
                postsLevel = level;
            }
        }

        userAchievements.put(AchievementConstants.FORUMPOSTS, postsLevel);

        refData.put("userAchievements", userAchievements);
        refData.put("nationAchievements", nationAchievements);
        refData.put("dominationAchievements", dominationAchievements);
        refData.put("specialAchievements", specialAchievements);

        refData.put("recentAchievements", getAchievementManager().listRecentHonour(thisUser.getUserId(), 15));
        refData.put("warfareKeys", GameHelperBean.WARFARE_STATISTICS);
        refData.put("politicsKeys", GameHelperBean.POLITICS_STATISTICS);
        refData.put("empireKeys", GameHelperBean.EMPIRE_STATISTICS);
        refData.put("undefinedInt", GameHelperBean.UNDEFINED_VALUE);
        refData.put("profileStats", gameHelper.prepareUserStatistics(thisUser));

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Honour/User");
        return new ModelAndView("user/honour", refData);
    }

    private transient ProfileManagerBean profileManager;

    public void setProfileManager(ProfileManagerBean profileManager) {
        this.profileManager = profileManager;
    }

    /**
     * Instance ForumPostManager class to perform queries
     * about forumPosts objects.
     */
    private ForumPostManagerBean forumPostsManager;

    /**
     * Setter method used by spring to inject a ForumPostsManager bean.
     *
     * @param value a ForumPostsManager bean.
     */
    public void setForumPostsManager(final ForumPostManagerBean value) {
        this.forumPostsManager = value;
    }

}
