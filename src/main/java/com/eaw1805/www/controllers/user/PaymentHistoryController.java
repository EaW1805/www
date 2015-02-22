package com.eaw1805.www.controllers.user;

import com.eaw1805.data.managers.beans.PaymentHistoryManagerBean;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Analytics history of account.
 */
@org.springframework.stereotype.Controller
public class PaymentHistoryController
        extends BaseController {

    @RequestMapping(method = RequestMethod.GET, value = "/user/paymentHistory")
    protected ModelAndView viewPaymentHistory(final HttpServletRequest request, final ModelMap model)
            throws Exception {
        ScenarioContextHolder.defaultScenario();
        //check if user can view this message
        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() <= 0) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/home");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        model.put("history", pmHistoryManager.list(thisUser));
        model.put("userId", thisUser.getUserId());
        return new ModelAndView("user/payment", model);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{username}/paymentHistory")
    protected ModelAndView viewHistory(final HttpServletRequest request, final ModelMap model, @PathVariable("username") String username)
            throws Exception {
        ScenarioContextHolder.defaultScenario();
        //check if user can view this message
        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() <= 0 || thisUser.getUserType() != 3) {
            throw new InvalidPageException("Page not found");
        }

        if (username != null) {
            final User currentUser = userManager.getByUserName(username);
            if (currentUser != null) {
                model.put("history", pmHistoryManager.list(currentUser));
                model.put("userId", currentUser.getUserId());
                model.put("username", currentUser.getUsername());
            } else {
                throw new InvalidPageException("Page not found");
            }
        } else {
            throw new InvalidPageException("Page not found");
        }

        return new ModelAndView("user/payment", model);
    }

    private PaymentHistoryManagerBean pmHistoryManager;

    public void setPmHistoryManager(final PaymentHistoryManagerBean pmHistoryManager) {
        this.pmHistoryManager = pmHistoryManager;
    }

}
