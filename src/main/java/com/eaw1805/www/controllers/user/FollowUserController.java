package com.eaw1805.www.controllers.user;

import com.eaw1805.data.model.Follow;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Display the playing followed by the user.
 */
@Controller
public class FollowUserController
        extends BaseController {

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userName}/toggleFollow/{action}")
    protected ModelAndView handle(@PathVariable final String userName,
                                  @PathVariable final String action,
                                  HttpServletRequest request) throws Exception {
        ScenarioContextHolder.defaultScenario();

        // Check User + Action
        if (userName == null || userName.isEmpty()
                || action == null || action.isEmpty()) {
            throw new InvalidPageException("Page not found");
        }
        // get profile user
        final User profileUser = userManager.getByUserName(userName);
        if (profileUser == null) {
            throw new InvalidPageException("Page not found");
        }

        //get logged in user
        final User thisUser = getUser();

        //check if user is the same.
        if (thisUser.getUserId() == profileUser.getUserId()) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/home");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }
        //check if logged in user already follows the leader.
        //if yes remove the connection,
        //else add a new connection.
        final List<Follow> follows = followManager.getByLeaderFollower(profileUser, thisUser);

        if ("follow".equalsIgnoreCase(action) || "followFromSettings".equalsIgnoreCase(action)) {
            if (follows.isEmpty()) {
                final Follow follow = new Follow();
                follow.setLeader(profileUser);
                follow.setFollower(thisUser);
                followManager.add(follow);
            }
        }

        if ("unfollow".equalsIgnoreCase(action) || "unfollowFromSettings".equalsIgnoreCase(action)) {
            if (!follows.isEmpty()) {
                followManager.delete(follows.get(0));
            }
        }
        if ("unfollowFromSettings".equalsIgnoreCase(action) || "followFromSettings".equalsIgnoreCase(action)) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/settings");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);

        } else {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/user/" + profileUser.getUsername());
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }
    }

}
