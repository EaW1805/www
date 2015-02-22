package com.eaw1805.www.controllers.site;

import com.eaw1805.data.managers.beans.ActiveUserManagerBean;
import com.eaw1805.data.managers.beans.ForumSessionBean;
import com.eaw1805.data.managers.beans.UserManagerBean;
import com.eaw1805.data.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

/**
 * Listens to active sessions to keep track of active users.
 */
public class SessionCounterListener extends HttpSessionEventPublisher {


    @Override
    public void sessionCreated(final HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(final HttpSessionEvent httpSessionEvent) {
        try {
            final SecurityContext securityContext = (SecurityContext) httpSessionEvent.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
            if (securityContext != null) {
                final UserDetails principal = (UserDetails) securityContext.getAuthentication().getPrincipal();
                final String username = principal.getUsername();
                if (username != null) {

                    final HttpSession session = httpSessionEvent.getSession();

                    final ApplicationContext ctx = WebApplicationContextUtils.
                            getWebApplicationContext(session.getServletContext());

                    final ActiveUserManagerBean activeUsersManager = (ActiveUserManagerBean) ctx.getBean("activeUserManagerBean");
                    if (activeUsersManager.getBySessionId(httpSessionEvent.getSession().getId()) != null) {
                        activeUsersManager.deleteByUsername(username);
                        final UserManagerBean userManager = (UserManagerBean) ctx.getBean("userManagerBean");

                        final User user = userManager.getByUserName(username);
                        if (user != null) {
                            final ForumSessionBean forumSessionManager = (ForumSessionBean) ctx.getBean("forumSessionsManagerBean");
                            forumSessionManager.delete(user.getUserId());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
