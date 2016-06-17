package com.eaw1805.www.controllers.cache;

import com.eaw1805.data.managers.beans.UserManagerBean;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Evicts Caches.
 */
@org.springframework.stereotype.Controller
public class EvictCacheController {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LogManager.getLogger(EvictCacheController.class);

    /**
     * Instance of UserManager class.
     */
    protected transient UserManagerBean userManager;

    /**
     * Handles request on "/cache/evict/{cacheName}".
     *
     * @param cacheName the cache to be evicted
     * @param request   the HttpRequest Object.
     * @return ModelAndView object.
     * @throws Exception throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "/cache/evict/{cacheName}")
    public ModelAndView handle(@PathVariable final String cacheName,
                               final HttpServletRequest request) throws Exception {
        final User user = getUser();

        //check that user is valid.
        if (user == null
                || user.getUserType() != 3
                || cacheName.isEmpty()) {

            throw new InvalidPageException("Page not found");
        }

        //Clear the cache with the given Name.
        if (redisTemplate.hasKey(CachingAspect.CACHE_NAME + cacheName)) {
            redisTemplate.delete(CachingAspect.CACHE_NAME + cacheName);
        }

        LOGGER.info("Evicting Cache: " + cacheName);
        final RedirectView redirectView = new RedirectView(request.getContextPath() + "/home");
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
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
     * Setter method used by spring to inject a userManager bean.
     *
     * @param value a userManager bean.
     */
    public void setUserManager(final UserManagerBean value) {
        this.userManager = value;
    }

    // inject the actual template
    @Autowired
    private org.springframework.data.redis.core.RedisTemplate<String, Object> redisTemplate;
}
