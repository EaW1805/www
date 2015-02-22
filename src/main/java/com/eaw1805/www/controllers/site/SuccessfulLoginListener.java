package com.eaw1805.www.controllers.site;

import com.eaw1805.data.managers.beans.ActiveUserManagerBean;
import com.eaw1805.data.managers.beans.ForumSessionBean;
import com.eaw1805.data.managers.beans.UserManagerBean;
import com.eaw1805.data.model.ActiveUser;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.forum.ForumSessions;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Listener for Successful logins. Called after login.
 */
public class SuccessfulLoginListener extends SavedRequestAwareAuthenticationSuccessHandler {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(SuccessfulLoginListener.class);

    /**
     * Instance of UserManager class to perfom queries about
     * users.
     */
    private transient UserManagerBean userManager;

    /**
     * Instance of ActiveUserManager class to perfom queries about
     * active users.
     */
    private ActiveUserManagerBean activeUsersManager;

    /**
     * Instance of ForumSessionBean class to perfom queries about
     * forum sessions.
     */
    private ForumSessionBean forumSessionsManager;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) throws IOException, ServletException {

        final UserDetails principal = (UserDetails) authentication.getPrincipal();

        final String username = principal.getUsername();
        if (username != null) {
            final User thisUser = userManager.getByUserName(username);
            if (thisUser != null) {

                // time of login
                final int timeOfLogin = (int) (request.getSession().getCreationTime() / 1000);

                // Identify IP
                final WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
                String ipAddress = request.getHeader("CF-Connecting-IP");
                if (ipAddress == null) {
                    ipAddress = webAuthenticationDetails.getRemoteAddress();

                    if (ipAddress == null) {
                        ipAddress = "unknown";
                    }
                }

                LOGGER.info("New login from " + ipAddress);

                // Update user login data
                thisUser.setUserLastIP(ipAddress);
                thisUser.setUserLastVisit(timeOfLogin);
                userManager.update(thisUser);

                LOGGER.info("Updated user");

                //Delete other active user with the same username.
                activeUsersManager.deleteByUsername(username);

                LOGGER.info("Deleted active users");

                //Delete Forum Sessions.
                forumSessionsManager.delete(thisUser.getUserId());

                LOGGER.info("Deleted sessions");

                final ActiveUser activeUser = new ActiveUser();
                activeUser.setUserId(thisUser.getUserId());
                activeUser.setUsername(username);
                activeUser.setEmailEncoded(thisUser.getEmailEncoded());
                activeUser.setLoginTime(new Date());
                activeUser.setRemoteIpAddress(ipAddress);
                activeUser.setSessionId(request.getSession().getId());
                activeUsersManager.add(activeUser);

                LOGGER.info("added active users");

                //Add Cookies for PHPBB
                final String sessionId = request.getSession().getId();

                final ForumSessions forumSession = new ForumSessions();
                forumSession.setSessionId(sessionId);
                forumSession.setUser(thisUser);
                forumSession.setForumId(0);
                forumSession.setLastVisit(timeOfLogin);
                forumSession.setSessionStart(timeOfLogin);
                forumSession.setSessionTime(timeOfLogin);
                forumSession.setSessionIp(ipAddress);
                forumSession.setSessionPage("ucp.php");

                String agent = request.getHeader("user-agent");
                if (agent != null) {
                    if (agent.length() > 255) {
                        agent = agent.substring(0, 254);
                    }
                    forumSession.setSessionBrowser(agent);
                } else {
                    forumSession.setSessionBrowser("");
                }

                forumSession.setViewOnline(true);
                forumSession.setAutoLogin(false);
                forumSession.setAdmin(false);

                forumSessionsManager.add(forumSession);

                LOGGER.info("added session");

                response.addCookie(createForumCookie("empires1805_sid", sessionId));
                response.addCookie(createForumCookie("empires1805_u", "" + thisUser.getUserId()));
                response.addCookie(createForumCookie("empires1805_k", ""));

                // Examine the referer url
                //final String referrer = request.getHeader("referer");
                //LOGGER.info(referrer);

                setDefaultTargetUrl("/games");
                setAlwaysUseDefaultTargetUrl(true);
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * Create a cookie with the specified name and value.
     *
     * @param name  the Cookie name.
     * @param value the Cookie value.
     * @return a new Cookie.
     */
    private Cookie createForumCookie(final String name, final String value) {
        final Cookie newCookie = new Cookie(name, value);
        newCookie.setDomain(".eaw1805.com");
        newCookie.setMaxAge((int) 3.15569e7);
        newCookie.setPath("/");
        return newCookie;
    }

    /**
     * Setter method used by spring to inject a ActiveUsersManager bean.
     *
     * @param value a ActiveUsersManager bean.
     */
    public void setActiveUsersManager(final ActiveUserManagerBean value) {
        this.activeUsersManager = value;
    }

    /**
     * Setter method used by spring to inject a userManager bean.
     *
     * @param injUserManager a userManager bean.
     */
    public void setUserManager(final UserManagerBean injUserManager) {
        this.userManager = injUserManager;
    }

    /**
     * Setter method used by spring to inject a ForumSession bean.
     *
     * @param value a ForumSession bean.
     */
    public void setForumSessionsManager(final ForumSessionBean value) {
        this.forumSessionsManager = value;
    }
}