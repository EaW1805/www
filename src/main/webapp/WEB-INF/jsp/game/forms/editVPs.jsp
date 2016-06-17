<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="turnToReport" scope="request" class="java.util.Map<java.lang.Integer, com.eaw1805.data.model.Report>"/>
<jsp:useBean id="tunToNews" scope="request" class="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.News>>"/>
<jsp:useBean id="newsToAchievement" scope="request" class="java.util.Map<java.lang.Integer, com.eaw1805.data.model.Achievement>"/>
<jsp:useBean id="turnToUsers" scope="request" class="java.util.Map<java.lang.Integer, com.eaw1805.data.model.Profile>"/>
<jsp:useBean id="game" scope="request" class="com.eaw1805.data.model.Game"/>
<jsp:useBean id="nation" scope="request" class="com.eaw1805.data.model.Nation"/>

<h1>Edit Game #${game.gameId} ${nation.name} VPs</h1>

<table border="1">
    <tr>
        <th>Turn</th>
        <th>User</th>
        <th>User VPs</th>
        <th>Nation VPs</th>
        <th>VPs contribution - Description</th>

    </tr>
<c:forEach begin="0" end="${game.turn - 1}" step="1" var="turn">
    <tr>
        <td>#${turn}</td>
        <td>
            <a href="<c:url value="/user/${turnToUsers[turn].user.username}"/>">
                <img width="48" height="48"
                     title="${turnToUsers[turn].user.username}"
                     src="https://secure.gravatar.com/avatar/${turnToUsers[turn].user.emailEncoded}?s=48&amp;d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png" style="cursor: default;">
                ${turnToUsers[turn].user.username}
            </a>
        </td>
        <td>${turnToUsers[turn].value}</td>
        <td>${turnToReport[turn].value}</td>
        <td>
            <ul>
            <c:forEach items="${tunToNews[turn]}" var="news">
                <li>
                ${news.baseNewsId} - ${news.text} (Achievement : ${newsToAchievement[news.newsId].description})
                <a href="<c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/nation/${nation.id}/vps/remove/${news.newsId}"/>">(DELETE)</a>
                </li>
            </c:forEach>
                <li>
                    <form method="post" action="<c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/nation/${nation.id}/vps"/>">
                        VPs: <input name="vps" value="0"/>
                        Description: <input name="text" value=""/>
                        <input type="hidden" name="turn" value="${turn}"/>
                        <input type="submit" value="add"/>
                    </form>
                </li>
            </ul>
        </td>

    </tr>
</c:forEach>
</table>
