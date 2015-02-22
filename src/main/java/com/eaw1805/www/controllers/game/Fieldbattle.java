package com.eaw1805.www.controllers.game;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.managers.beans.FieldBattleReportManagerBean;
import com.eaw1805.data.managers.beans.NationManagerBean;
import com.eaw1805.data.managers.beans.SectorManagerBean;
import com.eaw1805.data.managers.beans.UserFieldBattleManagerBean;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.battles.field.UserFieldBattle;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import java.util.Map;



@Controller
public class Fieldbattle extends ExtendedController {


    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/fieldbattle/")
    public ModelAndView handle(HttpServletRequest request) throws Exception{
        try {
            final JSONObject status = new SignedRequestParser().execute(request.getParameter("signed_request"));

            final FacebookImporter importer = new FacebookImporter();


            final User newUser;
            if (status == null || !status.containsKey("oauth_token")) {
                //if access token doesn't exist...
                newUser = null;
            } else {
                newUser = importer.saveUpdateFacebookUser(status.get("oauth_token").toString(), getUserManager(), getUser());
            }


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




//    public String parseSignedRequest(String signedRequest, String secret) {
//
//    }

//    function parse_signed_request($signed_request) {
//        list($encoded_sig, $payload) = explode('.', $signed_request, 2);
//
//        $secret = "appsecret"; // Use your app secret here
//
//        // decode the data
//        $sig = base64_url_decode($encoded_sig);
//        $data = json_decode(base64_url_decode($payload), true);
//
//        // confirm the signature
//        $expected_sig = hash_hmac('sha256', $payload, $secret, $raw = true);
//        if ($sig !== $expected_sig) {
//            error_log('Bad Signed JSON signature!');
//            return null;
//        }
//
//        return $data;
//    }
//
//    function base64_url_decode($input) {
//        return base64_decode(strtr($input, '-_', '+/'));
//    }
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
