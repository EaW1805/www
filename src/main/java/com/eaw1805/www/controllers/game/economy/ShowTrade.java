package com.eaw1805.www.controllers.game.economy;

import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.managers.beans.GoodManagerBean;
import com.eaw1805.data.managers.beans.TradeCityManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.economy.Good;
import com.eaw1805.data.model.economy.TradeCity;
import com.eaw1805.www.commands.NationCommand;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.controllers.scenario.ScenarioNationController;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Display the Trade Cities status for a given nation of a particular game.
 */
public class ShowTrade
        extends ScenarioNationController
        implements RegionConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowTrade.class);

    protected ModelAndView handle(final HttpServletRequest request,
                                  final HttpServletResponse response, final Object commandObj, final BindException errors)
            throws Exception {

        NationCommand command = (NationCommand) commandObj;

        // Retrieve Game entity
        final String gameId = command.getGameId();
        Game thisGame;
        if ((gameId == null) || (gameId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisGame = getGameManager().getByID(Integer.parseInt(gameId));
            if (thisGame == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Retrieve Nation entity
        final String nationId = command.getNationId();
        Nation thisNation;
        if ((nationId == null) || (nationId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisNation = getNationManager().getByID(Integer.parseInt(nationId));
            if (thisNation == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Check that user is allowed to view nation
        final User thisUser = getUser();
        if (thisUser.getUserType() != 3 && getUserGameManager().listActive(thisUser, thisGame, thisNation).isEmpty()) {
            throw new InvalidPageException("Page not found");
        }

        // access Calendar object
        final Calendar thisCal = calendar(thisGame);
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuf.append(" ");
        strBuf.append(thisCal.get(Calendar.YEAR));

        // Retrieve trade cities
        final List<TradeCity> lstCities = tradeCityManager.listByGame(thisGame);
        final List<TradeCity> lstEurope = new ArrayList<TradeCity>();
        final List<TradeCity> lstCaribbean = new ArrayList<TradeCity>();
        final List<TradeCity> lstIndies = new ArrayList<TradeCity>();
        final List<TradeCity> lstAfrica = new ArrayList<TradeCity>();

        for (final TradeCity thisCity : lstCities) {
            switch (thisCity.getPosition().getRegion().getId()) {
                case EUROPE:
                    lstEurope.add(thisCity);
                    break;

                case CARIBBEAN:
                    lstCaribbean.add(thisCity);
                    break;

                case INDIES:
                    lstIndies.add(thisCity);
                    break;

                case AFRICA:
                    lstAfrica.add(thisCity);
                    break;
            }
        }

        // Retrieve support tables
        final List<Good> goodList = goodManager.list();
        goodList.remove(GoodConstants.GOOD_CP - 1); // Remove Command Points -- this is a virtual good
        goodList.remove(GoodConstants.GOOD_AP - 1); // Remove Administrative Points -- this is a virtual good
        goodList.remove(GoodConstants.GOOD_PEOPLE - 1); // Remove People -- they are not traded

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("user", thisUser);
        refData.put("game", thisGame);
        refData.put("turn", thisGame.getTurn());
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        refData.put("nation", thisNation);
        refData.put("gameId", thisGame.getGameId());
        refData.put("gameDate", strBuf.toString());
        refData.put("nationId", thisNation.getId());
        refData.put("goodList", goodList);
        refData.put("citiesEurope", lstEurope);
        refData.put("citiesCaribbean", lstCaribbean);
        refData.put("citiesIndies", lstIndies);
        refData.put("citiesAfrica", lstAfrica);
        refData.put("vp", 0);
        refData.put("months", constructMonths(thisGame));

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show trade for game=" + gameId + "/nation=" + thisNation.getName());
        return new ModelAndView("game/trade", refData);
    }

    /**
     * Instance GoodManager class to perform queries
     * about good objects.
     */
    private transient GoodManagerBean goodManager;

    /**
     * Setter method used by spring to inject a goodManager bean.
     *
     * @param injGoodManager a goodManager bean.
     */
    public void setGoodManager(final GoodManagerBean injGoodManager) {
        goodManager = injGoodManager;
    }

    /**
     * Instance tradeCityManager class to perform queries
     * about TradeCity objects.
     */
    private transient TradeCityManagerBean tradeCityManager;

    /**
     * Setter method used by spring to inject a tradeCityManager bean.
     *
     * @param injTradeCityManager a tradeCityManager bean.
     */
    public void setTradeCityManager(final TradeCityManagerBean injTradeCityManager) {
        tradeCityManager = injTradeCityManager;
    }

}
