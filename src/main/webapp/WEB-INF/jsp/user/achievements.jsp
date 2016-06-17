<%
    response.setHeader("Access-Control-Allow-Origin", "http://forum.eaw1805.com");
    response.setHeader("Access-Control-Allow-Credentials", "true");

%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<jsp:useBean id="userNewAchievements" scope="request" type="java.util.List<com.eaw1805.data.model.Achievement>"/>

<c:forEach items="${userNewAchievements}" var="achievement">
    <div class="achievement_viewer" id="achievement_${achievement.achievementID}" style="position:fixed; left: 5px; top: 5px; display: none; z-index:5;" achievement="${achievement.achievementID}">
        <img src='http://static.eaw1805.com/images/achievements/achievementBar.png'
             height="62" style="position:absolute;left: 0px; top: 0px;"/>
        <img src='http://static.eaw1805.com/images/achievements/ach-${achievement.category}-${achievement.level}.png'
             height="53" style="position:absolute;left: 5px; top: 4px;"/>
        <div style="position: absolute; width: 30px; color: white; font-size: 19px; left: 326px; top: 17px;" align="center">${achievement.level}</div>
        <div style="position: absolute; color: white; top: 8px; width: 210px; left: 75px;">
            ${achievement.descriptionTitle} </div>
        <div style="position: absolute; color: white; left: 75px; top: 34px; font-size: 12px; width: 229px;">
            ${achievement.descriptionBody} </div>
    </div>
</c:forEach>
