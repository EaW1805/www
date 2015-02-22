package com.eaw1805.www.controllers;

import com.eaw1805.data.constants.ProfileConstants;
import com.eaw1805.data.dto.web.HofDTO;
import com.eaw1805.data.model.User;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;

/**
 * Display Hall of Fame pages.
 */
@Controller
public class HoFController
        extends BaseController
        implements ProfileConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(HoFController.class);

    /**
     * Pagination page size.
     */
    private static final int PAGE_SIZE = 18;

    @RequestMapping(method = RequestMethod.GET, value = "/hallOfFame")
    protected ModelAndView handle(final HttpServletRequest request, final ModelMap model) throws Exception {
        ScenarioContextHolder.defaultScenario();
        User thisUser = getUser();
        //check that users are valid.
        if (thisUser == null) {
            thisUser = new User();
            thisUser.setUserId(-1);
            thisUser.setUsername("anonymous");
        }

        PagedListHolder pagedListHolder = (PagedListHolder) request.getSession().getAttribute("hofProfiles");
        final LinkedList<HofDTO> list = gameHelper.prepareHallOfFame();

        if (pagedListHolder == null) {
            //initialize a new pageListHolder.
            pagedListHolder = new PagedListHolder(list);
            pagedListHolder.setPageSize(PAGE_SIZE);
            final HofDTO usrDTO = new HofDTO(thisUser);
            final int index = list.indexOf(usrDTO);
            if (index != -1) {
                pagedListHolder.setPage(index / PAGE_SIZE);
            }
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

        request.getSession().setAttribute("hofProfiles", pagedListHolder);
        model.put("hofProfiles", pagedListHolder);
        model.put("userGames", gameHelper.prepareUserGames(list));

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] HoF");
        return new ModelAndView("hof", model);
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
        } else if (property.equals("vps")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("vpsPosition");
        } else if (property.equals("achievements")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("achievementsPosition");
        } else if (property.equals("games")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("playedGames");
        } else if (property.equals("tbw")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("battlesTacticalWonPosition");
        } else if (property.equals("fbw")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("battlesFieldWonPosition");
        } else if (property.equals("nbw")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("battlesNavalWonPosition");
        } else if (property.equals("tk")) {
            ((MutableSortDefinition) pagedListHolder.getSort()).setProperty("enemyKilledPosition");
        }
        ((MutableSortDefinition) pagedListHolder.getSort()).setAscending(asc);
        pagedListHolder.resort();

        final HofDTO usrDTO = new HofDTO(getUser());
        final int index = pagedListHolder.getSource().indexOf(usrDTO);
        if (index != -1) {
            pagedListHolder.setPage(index / PAGE_SIZE);
        }
    }

}
