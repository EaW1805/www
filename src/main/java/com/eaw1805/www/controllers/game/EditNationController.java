package com.eaw1805.www.controllers.game;

import com.eaw1805.data.constants.ForumRoleConstants;
import com.eaw1805.data.managers.beans.GoodManagerBean;
import com.eaw1805.data.managers.beans.UserPermissionManagerBean;
import com.eaw1805.data.managers.beans.WarehouseManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.data.model.economy.Good;
import com.eaw1805.data.model.economy.Warehouse;
import com.eaw1805.data.model.forum.UserPermissions;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller
public class EditNationController
        extends ExtendedController {


    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(EditNationController.class);

    protected static final String MODEL_JSTL_KEY = "userGame";

    // inject the actual template
    @Autowired
    private org.springframework.data.redis.core.RedisTemplate<String, Object> redisTemplate;

    /**
     * Populate the command object to fill the form.
     *
     * @param servletRequest The servletRequest object to retrieve pathVariables.
     * @return A UserGame object.
     */
    @ModelAttribute(MODEL_JSTL_KEY)
    public UserGame populateCommand(final HttpServletRequest servletRequest) {
        Map pathVariables = (Map) servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables == null || !pathVariables.containsKey("gameId")
                || !pathVariables.containsKey("nationId")
                || !pathVariables.containsKey("scenarioId")) {
            return null;
        }
        ScenarioContextHolder.setScenario(pathVariables.get("scenarioId").toString());
        final String gameId = pathVariables.get("gameId").toString();
        final String nationId = pathVariables.get("nationId").toString();

        if (gameId == null || gameId.isEmpty()
                || nationId == null || nationId.isEmpty()) {
            return new UserGame();
        }

        final Game thisGame = getGameManager().getByID(Integer.parseInt(gameId));
        final Nation thisNation = getNationManager().getByID(Integer.parseInt(nationId));

        // Check that user is allowed to edit nation
        final User thisUser = getUser();
        if (thisUser.getUserType() != 3) {
            return null;
        }

        final List<UserGame> userGames = userGameManager.list(thisGame, thisNation);
        if (userGames.isEmpty()) {
            final UserGame newUG = new UserGame();
            newUG.setGame(thisGame);
            newUG.setNation(thisNation);
            newUG.setActive(true);
            newUG.setHasWon(false);
            newUG.setTurnPickUp(thisGame.getTurn());
            newUG.setActive(true);
            newUG.setOffer(0);
            newUG.setCost(thisNation.getCost() - (thisNation.getCost() * thisGame.getDiscount()) / 100);
            return newUG;
        } else {
            return userGames.get(userGames.size() - 1);
        }
    }

    @ModelAttribute("scenarioGoods")
    public List<Good> getTBLGoods(final HttpServletRequest servletRequest) {
        ScenarioContextHolder.defaultScenario();
        final Map pathVariables = (Map) servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables == null || !pathVariables.containsKey("scenarioId")) {
            return null;
        }
        ScenarioContextHolder.setScenario(pathVariables.get("scenarioId").toString());

        return goodManager.list();
    }

    @ModelAttribute("gameWarehouse")
    public List<Warehouse> populateWarehouse(final HttpServletRequest servletRequest) {

        ScenarioContextHolder.defaultScenario();
        final Map pathVariables = (Map) servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables == null || !pathVariables.containsKey("gameId")
                || !pathVariables.containsKey("nationId")
                || !pathVariables.containsKey("scenarioId")) {
            return null;
        }
        ScenarioContextHolder.setScenario(pathVariables.get("scenarioId").toString());

        final String gameId = pathVariables.get("gameId").toString();
        final String nationId = pathVariables.get("nationId").toString();

        if (gameId == null || gameId.isEmpty()
                || nationId == null || nationId.isEmpty()) {
            return new ArrayList<Warehouse>();
        }

        final Game thisGame = getGameManager().getByID(Integer.parseInt(gameId));
        final Nation thisNation = getNationManager().getByID(Integer.parseInt(nationId));

        return warehouseManager.listByGameNation(thisGame, thisNation);
    }

    /**
     * Populate the user of the specific game with specified nation.
     *
     * @param servletRequest The servletRequest object to retrieve pathVariables.
     * @return A User object.
     */
    @ModelAttribute("gameUser")
    public User populateUser(final HttpServletRequest servletRequest) {
        ScenarioContextHolder.defaultScenario();
        Map pathVariables = (Map) servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables == null || !pathVariables.containsKey("gameId")
                || !pathVariables.containsKey("nationId")
                || !pathVariables.containsKey("scenarioId")) {
            return null;
        }
        ScenarioContextHolder.setScenario(pathVariables.get("scenarioId").toString());

        final String gameId = pathVariables.get("gameId").toString();
        final String nationId = pathVariables.get("nationId").toString();


        if (gameId == null || gameId.isEmpty()
                || nationId == null || nationId.isEmpty()) {
            return new User();
        }

        final Game thisGame = getGameManager().getByID(Integer.parseInt(gameId));
        final Nation thisNation = getNationManager().getByID(Integer.parseInt(nationId));

        // Check that user is allowed to edit nation
        final User thisUser = getUser();
        if (thisUser.getUserType() != 3) {
            return null;
        }

        final List<UserGame> userGames = userGameManager.list(thisGame, thisNation);
        if (userGames.isEmpty()) {
            return new User();
        } else {
            return userManager.getByID(userGames.get(userGames.size() - 1).getUserId());
        }
    }

    /**
     * Return the view to display the form.
     *
     * @return The view to display the form.
     */
    @RequestMapping(value = "/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/edit", method = RequestMethod.GET)
    public String setupForm(@PathVariable String scenarioId,
                            @PathVariable final int gameId, @PathVariable final int nationId, final ModelMap model) {
        model.addAttribute("gameId", gameId);
        model.addAttribute("nationId", nationId);
        model.addAttribute("scenarioId", scenarioId);
        return "game/editNation";
    }

    /**
     * Handle submit action here.
     *
     * @param userGame The userGame object updated from form.
     * @param result
     * @param status
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/edit", method = RequestMethod.POST)
    public ModelAndView processNationEdit(
            @PathVariable("scenarioId") String scenarioId,
            @ModelAttribute(MODEL_JSTL_KEY) final UserGame userGame,
            final BindingResult result,
            final SessionStatus status)
            throws Exception {
        ScenarioContextHolder.setScenario(scenarioId);

        //get http request object.
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        final String newUserStr = request.getParameter("newUser");

        if (newUserStr != null && !newUserStr.isEmpty()) {
            final User newUser = userManager.getByUserName(newUserStr);
            userGame.setUserId(newUser.getUserId());

        } else {
            userGame.setUserId(2);
        }
        userGame.setAccepted(true);
        final String costStr = request.getParameter("newCost");
        if (costStr != null && !costStr.isEmpty()) {
            userGame.setCost(Integer.parseInt(costStr));
        }

        userGame.setAlive(request.getParameter("alive") != null);
        //you have to choose
        userGame.setActive(request.getParameter("active") != null
                        && userGame.getUserId() != 2);

        final UserGame dbUserGame = userGameManager.getByID(userGame.getId());
        final User user = userManager.getByID(userGame.getUserId());

        if (user == null) {
            //Error, Wrong User.
            throw new InvalidPageException("Page not found");
        }
        if (user.getUserId() == dbUserGame.getUserId()) {
            boolean dropped = false;
            boolean pickedup = false;

            //This game is assigned to admin. Only the Cost can be changed,
            if (userGame.getUserId() == 2) {
                dbUserGame.setCost(userGame.getCost());
                dbUserGame.setActive(false);
                userGameManager.update(dbUserGame);
                final RedirectView redirectView = new RedirectView(request.getContextPath() + "/scenario/" + userGame.getGame().getScenarioIdToString() + "/game/" + userGame.getGame().getGameId() + "/info");
                redirectView.setExposeModelAttributes(false);
                return new ModelAndView(redirectView);
            }

            if (!userGame.isActive() && userGame.isActive() != dbUserGame.isActive()) {
                dropped = true;
            } else if (userGame.isActive() && userGame.isActive() != dbUserGame.isActive()) {
                pickedup = true;
            }

            if (dropped) {

                //Drop current position
                dbUserGame.setTurnDrop(dbUserGame.getGame().getTurn());
                dbUserGame.setActive(false);
                dbUserGame.setCost(userGame.getCost());
                dbUserGame.setCurrent(false);
                userGameManager.update(dbUserGame);

                //Update Forum Permissions.
                updatePermissions(user, dbUserGame.getGame().getForumId(), ForumRoleConstants.ROLE_FORUM_READONLY);

                //Update UserGame Values. Assign a new UserGame to Admin.
                userGame.setUserId(2);
                userGame.setTurnPickUp(0);
                userGame.setTurnDrop(0);
                userGame.setCurrent(true);
                userGame.setAccepted(true);
                userGame.setActive(false);
                userGame.setAlive(dbUserGame.isAlive());
                userGameManager.add(userGame);

            } else if (pickedup) {

                //Pick up current position
                dbUserGame.setTurnPickUp(dbUserGame.getGame().getTurn());
                dbUserGame.setActive(dbUserGame.getUserId() != 2);
                dbUserGame.setCost(userGame.getCost());
                dbUserGame.setAccepted(true);
                dbUserGame.setCurrent(true);
                userGameManager.update(dbUserGame);

                //Update Forum Permissions.
                updatePermissions(user, dbUserGame.getGame().getForumId(), ForumRoleConstants.ROLE_FORUM_POLLS);

            } else {
                dbUserGame.setActive(dbUserGame.getUserId() != 2);
                dbUserGame.setCost(userGame.getCost());
                userGameManager.update(dbUserGame);
            }
        } else {
            boolean pickedup = false;

            //This UserGame is assigned to Admin.
            if (dbUserGame.getUserId() == 2) {
                pickedup = true;
            }

            if (pickedup) {
                //Pick up current position
                dbUserGame.setUserId(userGame.getUserId());
                dbUserGame.setAccepted(true);
                dbUserGame.setTurnPickUp(dbUserGame.getGame().getTurn());
                dbUserGame.setActive(userGame.isActive() && dbUserGame.getUserId() != 2);
                dbUserGame.setCost(userGame.getCost());
                dbUserGame.setCurrent(true);
                userGameManager.update(dbUserGame);

                //Update Forum Permissions.
                updatePermissions(user, dbUserGame.getGame().getForumId(), ForumRoleConstants.ROLE_FORUM_POLLS);

            } else {
                //Drop current position
                dbUserGame.setTurnDrop(dbUserGame.getGame().getTurn());
                dbUserGame.setActive(false);
                dbUserGame.setCost(userGame.getCost());
                dbUserGame.setCurrent(false);
                dbUserGame.setAccepted(true);
                userGameManager.update(dbUserGame);

                //Update Forum Permissions.
                updatePermissions(user, dbUserGame.getGame().getForumId(), ForumRoleConstants.ROLE_FORUM_READONLY);

                userGame.setTurnPickUp(userGame.getGame().getTurn());
                userGame.setTurnDrop(0);
                userGame.setCurrent(true);
                userGame.setAlive(dbUserGame.isAlive());
                userGame.setActive(userGame.getUserId() != 2);
                userGame.setAccepted(true);
                userGameManager.add(userGame);

                //Update Forum Permissions.
                updatePermissions(user, dbUserGame.getGame().getForumId(), ForumRoleConstants.ROLE_FORUM_POLLS);

            }
        }

        //update warehouses
        final List<Good> allGoods = goodManager.list();
        final List<Warehouse> warehouses = warehouseManager.listByGameNation(userGame.getGame(), userGame.getNation());
        for (Warehouse warehouse : warehouses) {
            for (Good good : allGoods) {
                final String quantity = request.getParameter("good_" + good.getGoodId() + "_" + warehouse.getRegion().getId());
                if (quantity != null && !quantity.isEmpty()) {
                    try {
                        int value = Integer.parseInt(quantity);
                        warehouse.getStoredGoodsQnt().put(good.getGoodId(), value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            warehouseManager.update(warehouse);
        }


        //Clear the cache with the given Name.
        final String cacheName = "client-" + userGame.getGame().getGameId() + "-" + userGame.getNation().getId();
        if (redisTemplate.hasKey(cacheName)) {
            redisTemplate.delete(cacheName);
        }

        // redirect back to game
        final RedirectView redirectView = new RedirectView(request.getContextPath() + "/scenario/" + userGame.getGame().getScenarioIdToString() + "/game/" + userGame.getGame().getGameId() + "/info");
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
    }


    /**
     * Updates the user permissions.
     *
     * @param thisUser   the input user
     * @param forumId    the forum id
     * @param permission the permission role (ForumRoleConstants)
     */
    private void updatePermissions(final User thisUser, final int forumId, final int permission) {
        UserPermissions userPermissions = permissionManager.getForumPermissions(thisUser, forumId);
        if (userPermissions == null) {
            userPermissions = new UserPermissions();
            userPermissions.setUser(thisUser);
            userPermissions.setForumId(forumId);
            userPermissions.setAuthRoleId(permission);
            permissionManager.add(userPermissions);
        } else {
            userPermissions.setAuthRoleId(permission);
            permissionManager.update(userPermissions);
        }
        thisUser.setUserPermissions("");
        userManager.update(thisUser);
    }

    /**
     * Instance UpdatePermissionManager class to perform queries
     * about permissions objects.
     */
    private transient UserPermissionManagerBean permissionManager;

    /**
     * Setter method used by spring to inject a UpdatePermissionManager bean.
     *
     * @param injPermissionManager a PermissionManager bean.
     */
    public void setPermissionManager(final UserPermissionManagerBean injPermissionManager) {
        this.permissionManager = injPermissionManager;
    }

    /**
     * Instance WarehouseManagerBean class to perform queries
     * about warehouse objects.
     */
    private transient WarehouseManagerBean warehouseManager;

    /**
     * Setter method used by spring to inject a WarehouseManager bean.
     *
     * @param injWarehouseManager a WarehouseManager bean.
     */
    public void setWarehouseManager(final WarehouseManagerBean injWarehouseManager) {
        this.warehouseManager = injWarehouseManager;
    }

    /**
     * Instance GoodManagerBean class to perform queries
     * about good objects.
     */
    private transient GoodManagerBean goodManager;

    /**
     * Setter method used by spring to inject a GoodManager bean.
     *
     * @param injGoodManager a GoodManager bean.
     */
    public void setGoodManager(GoodManagerBean injGoodManager) {
        this.goodManager = injGoodManager;
    }
}
