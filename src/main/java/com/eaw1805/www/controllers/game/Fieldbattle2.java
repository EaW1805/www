package com.eaw1805.www.controllers.game;


import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.managers.beans.FieldBattleReportManagerBean;
import com.eaw1805.data.managers.beans.NationManagerBean;
import com.eaw1805.data.managers.beans.SectorManagerBean;
import com.eaw1805.data.managers.beans.UserFieldBattleManagerBean;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.social.FacebookImporter;
import com.eaw1805.www.controllers.social.SignedRequestParser;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class Fieldbattle2 extends ExtendedController {



    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/fieldbattle/token/{accessToken}")
    public ModelAndView handle(@PathVariable("accessToken") String accessToken, final HttpServletRequest request) throws Exception{
        try {

            final FacebookImporter importer = new FacebookImporter();


            final User newUser = importer.saveUpdateFacebookUser(accessToken, getUserManager(), getUser());


            if (newUser != null) {
                authenticateUser(newUser.getUsername(), userDetailsManager, request);
            }
            // Check that user is allowed to view nation
            ScenarioContextHolder.setScenario(HibernateUtil.DB_FIELDBATTLE);


            final Map<String, Object> refData = new HashMap<String, Object>();
            refData.put("battleId", -1);
            refData.put("scenarioId", 1800);
            refData.put("nationId", 1);
            refData.put("round", 0);
            refData.put("side", 0);
            refData.put("gameEnded", false);
            refData.put("winner", 0);
            refData.put("standAlone", 1);
            if (newUser != null) {
                refData.put("facebookId", newUser.getFacebookId());
            } else {
                refData.put("facebookId", "");
            }
            refData.put("sideReady", 0);

            refData.put("title", "Field battle at ");

            LOGGER.info("[" + "] Play Field Battle id=" + -1 + "/side=" + 0 + "/nation=" + 1);
            System.out.println("[" + "] Play Field Battle id=" + -1 + "/side=" + 0 + "/nation=" + 1);
            return new ModelAndView("game/playFieldBattle", refData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * Authenticates user after successful signup.
     *
     * @param username    the username of the current user.
     * @param userManager the userManager Service.
     * @param request     the HTTP Request to create a new session
     */
    public void authenticateUser(final String username,
                                 final UserDetailsManager userManager,
                                 final HttpServletRequest request) {
        try {
            // generate session if one doesn't exist
            request.getSession();

            // Authenticate the user
            UserDetails user = userManager.loadUserByUsername(username);
            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }


    @Autowired
    private transient FieldBattleReportManagerBean fieldBattleReportManagerBean;

    @Autowired
    private transient NationManagerBean nationManagerBean;




    @Autowired
    private transient SectorManagerBean sectorManagerBean;


    @Autowired
    private transient UserFieldBattleManagerBean userFieldBattleManagerBean;

    @Autowired
    @Qualifier("jdbcUserService")  // <-- this references the bean id
    public UserDetailsManager userDetailsManager;


}
