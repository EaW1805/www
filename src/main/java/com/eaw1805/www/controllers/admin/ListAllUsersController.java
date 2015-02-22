package com.eaw1805.www.controllers.admin;

import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * List all registered Users.
 */
@org.springframework.stereotype.Controller
public class ListAllUsersController
        extends BaseController {


    @RequestMapping(method = RequestMethod.GET, value = "/users/list")
    public ModelAndView setupPage(final ModelMap model) throws InvalidPageException {

        final User thisUser = getUser();

        //be sure user is logged in.
        if (thisUser == null || thisUser.getUserId() <= 0 || thisUser.getUserType() != 3) {
            throw new InvalidPageException("Page not found");
        }

        //get http request object.
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        PagedListHolder pagedListHolder = (PagedListHolder) request.getSession().getAttribute("usersList");

        if (pagedListHolder == null) {

            //initialize a new pageListHolder.
            pagedListHolder = new PagedListHolder(getAllUsers());

            //Show all messages if inbox size equals 40
            pagedListHolder.setPageSize(40);

        } else {
            final String asc = (String) request.getParameter("asc");
            sort(pagedListHolder, true, asc);
            final String desc = (String) request.getParameter("desc");
            sort(pagedListHolder, false, desc);

            final String page = (String) request.getParameter("page");
            if ("next".equals(page)) {
                pagedListHolder.nextPage();
            } else if ("previous".equals(page)) {
                pagedListHolder.previousPage();
            } else {
                try {
                    final int goToPage = Integer.valueOf(page);
                    if (goToPage <= pagedListHolder.getPageCount() && goToPage >= 0) {
                        pagedListHolder.setPage(goToPage);
                    }
                } catch (Exception e) {
                    //eat it
                }
            }
        }

        request.getSession().setAttribute("usersList", pagedListHolder);
        model.put("usersList", pagedListHolder);

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] List Users");
        return new ModelAndView("users/list", model);
    }


    /**
     * Resort the holder object based on the input parameters.
     *
     * @param pagedListHolder the pagedListHolder
     * @param asc             true if asc, false if desc.
     * @param property        the property
     */
    public void sort(final PagedListHolder pagedListHolder, final boolean asc, final String property) {
        if (property == null || property.isEmpty()) {
            return;
        }

        if (property.equals("player")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("username");
        } else if (property.equals("fullname")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("fullname");
        } else if (property.equals("location")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("location");
        } else if (property.equals("free")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("creditFree");
        } else if (property.equals("transferred")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("creditTransferred");
        } else if (property.equals("bought")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("creditBought");
        } else if (property.equals("userId")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("userId");
        } else if (property.equals("dateJoin")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("dateJoin");
        } else if (property.equals("userLastVisit")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("userLastVisit");
        }


        ((MutableSortDefinition) pagedListHolder.getSort()).setAscending(asc);
        pagedListHolder.resort();
    }
}
