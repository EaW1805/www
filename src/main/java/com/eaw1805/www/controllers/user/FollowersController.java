package com.eaw1805.www.controllers.user;

import com.eaw1805.data.model.Follow;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.WatchGame;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Display the Features page.
 */
@Controller
public class FollowersController
        extends BaseController {

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userName}/followers")
    protected ModelAndView handle(@PathVariable final String userName,
                                  HttpServletRequest request) throws Exception {
        ScenarioContextHolder.defaultScenario();

        // Retrieve Game entity
        if (userName == null || userName.isEmpty()) {
            throw new InvalidPageException("Page not found");
        }

        //get current user.
        final User thisUser = getUser();

        //get profile user.
        final User profileUser = userManager.getByUserName(userName);
        if (profileUser == null) {
            throw new InvalidPageException("Page not found");
        }

        //get users that this person follows.
        final List<Follow> following = followManager.listByFollower(thisUser, true);
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

        final List<WatchGame> watchedGames = watchGameManager.listByUser(thisUser);
        final List<Game> watchGames = new ArrayList<Game>();
        count = 0;
        for (final WatchGame watchGame : watchedGames) {
            count++;
            if (count > 10) {
                break;
            }
            watchGames.add(watchGame.getGame());
        }

        Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("user", thisUser);
        refData.put("profileUser", profileUser);
        refData.put("followingCnt", following.size());
        refData.put("followingList", followingList);
        refData.put("followersCnt", followers.size());
        refData.put("followersList", followersList);
        refData.put("watchCnt", watchedGames.size());
        refData.put("gameList", watchGames);
        refData.put("follows", followManager.listByLeader(profileUser, true));
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        return new ModelAndView("user/followers", refData);
    }

}
