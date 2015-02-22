package com.eaw1805.www.controllers.user;

import com.eaw1805.data.managers.beans.QuestionnaireManagerBean;
import com.eaw1805.data.model.Questionnaire;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@Controller
public class QuestionnairePage extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(QuestionnairePage.class);

    /**
     * Use this first time to init all users with a uuid.
     *
     * @return a view for the response.
     * @throws Exception not really.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/questionnaire/init")
    public String init() throws Exception{
        try {
        for (final User user : getUserManager().list()) {
            user.setLastProcDate(new Date());
            getUserManager().update(user);
            LOGGER.info(user.getUsername() + " : " + user.getQuestionnaireUUID());
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "games";

    }

    /**
     * Form backing object.
     *
     * @return A new questionnaire object.
     */
    @ModelAttribute("questionnaire")
    public Questionnaire formObject() {
        return new Questionnaire();
    }

    /**
     * Show the questionnaire page for a specific uuid.
     *
     * @param uuid The uuid to search the page for.
     * @param request The http servlet request.
     * @return The view for the questionnaire.
     * @throws Exception nothing really.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/questionnaire/{uuid}")
    public String showPage(@PathVariable("uuid") final String uuid,
                           final HttpServletRequest request, Model model) throws Exception{
        final User thisUser = getUserManager().getByQuestionnaireUUID(uuid);
        //be sure user is logged in....
        if (thisUser == null) {
            throw new InvalidPageException("Page has expired");
        }
        //be sure there is a uuid...
        if (uuid == null || uuid.isEmpty()) {
            throw new InvalidPageException("Page not found");
        }
        model.addAttribute("uuid", uuid);
        model.addAttribute("qUser", thisUser);
        authenticateUser(thisUser.getUsername(), userDetailsManager, request);
        return "user/questionnaire/questionnaireForm";

    }

    /**
     * Handle the submission of the questionnaire.
     *
     * @param questionnaire The object backed up from the form.
     * @param uuid The uuid of the form.
     * @return A redirect page for the questionnaire.
     * @throws Exception Nothing really.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/questionnaire/{uuid}")
    public String saveQuestionnaire(@ModelAttribute("questionnaire") Questionnaire questionnaire,
                                    @PathVariable("uuid") final String uuid) throws Exception{
        final User thisUser = getUserManager().getByQuestionnaireUUID(uuid);
        //be sure user is logged in....
        if (thisUser == null) {
            throw new InvalidPageException("Page has expired");
        }
        //be sure there is a uuid...
        if (uuid == null || uuid.isEmpty()) {
            throw new InvalidPageException("Page not found");
        }
        //add the corresponding user
        questionnaire.setUser(thisUser);
        questionnaire.setSubmitDate(new Date());
        //add questionnaire into the database.
        questionnaireManager.add(questionnaire);
        //update the user, uuid is not valid anymore.
        //so make it blank... no more needed.
        thisUser.setQuestionnaireUUID("");
        getUserManager().update(thisUser);
        return "user/questionnaire/submitted";

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
    @Qualifier("jdbcUserService")  // <-- this references the bean id
    private transient UserDetailsManager userDetailsManager;

    @Autowired
    @Qualifier("questionnaireManagerBean")
    private transient QuestionnaireManagerBean questionnaireManager;
}
