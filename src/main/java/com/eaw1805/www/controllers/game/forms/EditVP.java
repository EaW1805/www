package com.eaw1805.www.controllers.game.forms;

import com.eaw1805.core.WebsiteCacheManager;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.*;
import com.eaw1805.data.managers.beans.*;
import com.eaw1805.data.model.*;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This controller handles the view and save
 * of edit vps form for a spesific user.
 */
@Controller
public class EditVP extends BaseController {

    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/vps")
    public String showPage(@PathVariable("scenarioId") String scenarioId,
                           @PathVariable("gameId") String gameId,
                           @PathVariable("nationId") String nationId,
                            final Model model) throws Exception {

        //check for permissions
        final User thisUser = getUser();
        if (thisUser.getUserType() != 3) {
            throw new InvalidPageException("Page not found");
        }

        //select scenario
        ScenarioContextHolder.setScenario(scenarioId);
        final Game game = gameManager.getByID(Integer.parseInt(gameId));
        final Nation nation = nationManager.getByID(Integer.parseInt(nationId));

        //retrieve reports based on vps.
        final Map<Integer, Report> turnToReport = new HashMap<Integer, Report>();
        final List<Report> reports = reportManager.listByOwnerKey(nation, game, ReportConstants.N_VP);
        for (final Report report : reports) {
            turnToReport.put(report.getTurn(), report);
        }

        final Map<Integer, Profile> turnToUsers = getTurnToUsers(game, nation);
        //retrieve current profile vps...

        //retrieve news based on vps.
        final Map<Integer, List<News>> tunToNews = new HashMap<Integer, List<News>>();
        final Map<Integer, Achievement> newsToAchievement = new HashMap<Integer, Achievement>();
        final List<News> allNews = newsManager.listGameNationType(game, nation, NewsConstants.NEWS_VP);
        for (final News news : allNews) {
            if (!tunToNews.containsKey(news.getTurn())) {
                tunToNews.put(news.getTurn(), new ArrayList<News>());
            }
            tunToNews.get(news.getTurn()).add(news);

            if (turnToUsers.get(news.getTurn()) != null) {
                final List<Achievement> relatedAchievement =  getAchievementManager().list(turnToUsers.get(news.getTurn()).getUser().getUserId(), AchievementConstants.VPS, AchievementConstants.LEVEL_1, news.getText());
                if (!relatedAchievement.isEmpty()) {
                    newsToAchievement.put(news.getNewsId(), relatedAchievement.get(0));
                }
            }
        }



        model.addAttribute("turnToReport", turnToReport);
        model.addAttribute("tunToNews", tunToNews);
        model.addAttribute("game", game);
        model.addAttribute("nation", nation);
        model.addAttribute("turnToUsers", turnToUsers);
        model.addAttribute("newsToAchievement", newsToAchievement);


        return "game/forms/editVPs";

    }

    final Map<Integer, Profile> getTurnToUsers(final Game game, final Nation nation) {
        final Map<Integer, Profile> turnToUsers = new HashMap<Integer, Profile>();
        final List<UserGame> nationGames = userGameManager.listAll(game, nation);
        for (final UserGame userGame : nationGames) {
            if (userGame.getTurnDrop() > userGame.getTurnPickUp()) {
                for (int turn = userGame.getTurnPickUp(); turn < userGame.getTurnDrop(); turn++) {
                    turnToUsers.put(turn, profileManager.getByOwnerKey(userManager.getByID(userGame.getUserId()), ProfileConstants.VPS));
                }
            } else {
                for (int turn = userGame.getTurnPickUp(); turn < userGame.getGame().getTurn(); turn++) {
                    turnToUsers.put(turn, profileManager.getByOwnerKey(userManager.getByID(userGame.getUserId()), ProfileConstants.VPS));
                }
            }
        }
        return turnToUsers;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/vps/remove/{newsId}")
    public String removeVPs(@PathVariable("scenarioId") String scenarioId,
                            @PathVariable("gameId") String gameId,
                            @PathVariable("nationId") String nationId,
                            @PathVariable("newsId") String newsId) throws Exception {
        //check for permissions
        final User thisUser = getUser();
        if (thisUser.getUserType() != 3) {
            throw new InvalidPageException("Page not found");
        }

        ScenarioContextHolder.setScenario(scenarioId);
        final News news = newsManager.getByID(Integer.parseInt(newsId));

        //apply vp change to reports...
        int startTurn = news.getTurn();
        int endTurn = news.getGame().getTurn();
        for (int turn = startTurn; turn < endTurn; turn++) {
            final Report report = reportManager.getByOwnerTurnKey(news.getNation(), news.getGame(), turn, ReportConstants.N_VP);
            if (report != null) {
                report.setValue(String.valueOf(Integer.parseInt(report.getValue()) - news.getBaseNewsId()));
                reportManager.update(report);
            }
        }

        final Map<Integer, Profile> turnToUser = getTurnToUsers(news.getGame(), news.getNation());
        final Profile profile = turnToUser.get(news.getTurn());
        if (profile != null) {
            //apply changes to profile
            profile.setValue(profile.getValue() - news.getBaseNewsId());
            profileManager.update(profile);

            //delete related achievement
            final List<Achievement> relatedAchievement =  getAchievementManager().list(profile.getUser().getUserId(), AchievementConstants.VPS, AchievementConstants.LEVEL_1, news.getText());
            if (!relatedAchievement.isEmpty()) {
                //then just delete the first of them.
                Achievement toDelete = relatedAchievement.get(0);
                getAchievementManager().delete(toDelete);
            }
        }



        //finally delete the news
        newsManager.delete(news);

        //finally evict related caches
        //Clear the cache with the given Name.
        if (redisTemplate.hasKey(WebsiteCacheManager.CACHE_NAME)) {
            redisTemplate.delete(WebsiteCacheManager.CACHE_NAME);
        }
        //Clear the cache with the given Name.
        if (redisTemplate.hasKey("longGameCache")) {
            redisTemplate.delete("longGameCache");
        }
        //Clear the cache with the given Name.
        if (redisTemplate.hasKey(WebsiteCacheManager.USER_CACHE_NAME)) {
            redisTemplate.delete(WebsiteCacheManager.USER_CACHE_NAME);
        }
        //then clear the game cache
        if (redisTemplate.hasKey(WebsiteCacheManager.GAME_CACHE_NAME + "-" + news.getGame().getGameId())) {
            redisTemplate.delete(WebsiteCacheManager.GAME_CACHE_NAME + "-" + news.getGame().getGameId());
        }
        //redirect back to form page.
        return "redirect:/scenario/" + scenarioId + "/game/" + gameId + "/nation/" + nationId + "/vps";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/vps")
    public String addVPs(@PathVariable("scenarioId") String scenarioId,
                         @PathVariable("gameId") String gameId,
                         @PathVariable("nationId") String nationId,
                         final HttpServletRequest request)
                throws Exception {
        //check for permissions
        final User thisUser = getUser();
        if (thisUser.getUserType() != 3) {
            throw new InvalidPageException("Page not found");
        }

        ScenarioContextHolder.setScenario(scenarioId);
        final Game game = gameManager.getByID(Integer.parseInt(gameId));
        final Nation nation = nationManager.getByID(Integer.parseInt(nationId));

        int vps = Integer.parseInt(request.getParameter("vps"));
        String text = request.getParameter("text");
        int turn = Integer.parseInt(request.getParameter("turn"));

        final News thisNewsEntry = new News();
        thisNewsEntry.setGame(game);
        thisNewsEntry.setTurn(turn);
        thisNewsEntry.setNation(nation);
        thisNewsEntry.setSubject(nation);
        thisNewsEntry.setType(NewsConstants.NEWS_VP);
        thisNewsEntry.setBaseNewsId(vps);
        thisNewsEntry.setAnnouncement(false);
        thisNewsEntry.setText(text);
        newsManager.add(thisNewsEntry);

        //apply vp change to reports...
        int startTurn = thisNewsEntry.getTurn();
        int endTurn = thisNewsEntry.getGame().getTurn();
        for (turn = startTurn; turn < endTurn; turn++) {
            final Report report = reportManager.getByOwnerTurnKey(thisNewsEntry.getNation(), thisNewsEntry.getGame(), turn, ReportConstants.N_VP);
            if (report != null) {
                report.setValue(String.valueOf(Integer.parseInt(report.getValue()) + thisNewsEntry.getBaseNewsId()));
                reportManager.update(report);
            }
        }

        //apply changes to profile
        final Map<Integer, Profile> turnToUser = getTurnToUsers(thisNewsEntry.getGame(), thisNewsEntry.getNation());
        final Profile profile = turnToUser.get(thisNewsEntry.getTurn());
        if (profile != null) {
            profile.setValue(profile.getValue() + thisNewsEntry.getBaseNewsId());
            profileManager.update(profile);

            //create achievement
            final Achievement entry = new Achievement();
            entry.setUser(profile.getUser());
            entry.setCategory(AchievementConstants.VPS);
            entry.setLevel(AchievementConstants.LEVEL_1);
            entry.setAnnounced(false);
            entry.setFirstLoad(false);
            entry.setDescription(thisNewsEntry.getText());
            entry.setVictoryPoints(0);
            entry.setAchievementPoints(thisNewsEntry.getBaseNewsId());
            getAchievementManager().add(entry);
        }

        //finally evict related caches
        //Clear the cache with the given Name.
        if (redisTemplate.hasKey(WebsiteCacheManager.CACHE_NAME)) {
            redisTemplate.delete(WebsiteCacheManager.CACHE_NAME);
        }
        //Clear the cache with the given Name.
        if (redisTemplate.hasKey("longGameCache")) {
            redisTemplate.delete("longGameCache");
        }
        //Clear the cache with the given Name.
        if (redisTemplate.hasKey(WebsiteCacheManager.USER_CACHE_NAME)) {
            redisTemplate.delete(WebsiteCacheManager.USER_CACHE_NAME);
        }
        //then clear the game cache
        if (redisTemplate.hasKey(WebsiteCacheManager.GAME_CACHE_NAME + "-" + thisNewsEntry.getGame().getGameId())) {
            redisTemplate.delete(WebsiteCacheManager.GAME_CACHE_NAME + "-" + thisNewsEntry.getGame().getGameId());
        }

        //redirect back to form page.
        return "redirect:/scenario/" + scenarioId + "/game/" + gameId + "/nation/" + nationId + "/vps";
    }

    @Autowired
    @Qualifier("profileManagerBean")
    private transient ProfileManagerBean profileManager;

    @Autowired
    @Qualifier("reportManagerBean")
    private transient ReportManagerBean reportManager;

    @Autowired
    @Qualifier("newsManagerBean")
    private transient NewsManagerBean newsManager;

    @Autowired
    @Qualifier("gameManagerBean")
    private transient GameManagerBean gameManager;

    @Autowired
    @Qualifier("nationManagerBean")
    private transient NationManagerBean nationManager;

    @Autowired
    private transient org.springframework.data.redis.core.RedisTemplate<String, Object> redisTemplate;
}
