<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<jsp:useBean id="game" scope="request" class="com.eaw1805.data.model.Game"/>
<jsp:useBean id="gameDate" scope="request" class="java.lang.String"/>
<jsp:useBean id="gameOwner" scope="request" class="com.eaw1805.data.model.User"/>
<div class="player" style="height: 200px;">
    <h1 class="game">Game ${game.gameId}</h1>

    <h2 class="game">${game.description}</h2>

    <h3 class="game">${gameDate}</h3>

    <h3 style="margin-left: 856px; margin-top: 35px;z-index: 5;">
        <a href="<c:url value="/scenario/${game.scenarioIdToString}/info"/>">
            Scenario ${game.scenarioIdToString}
        </a>
    </h3>

    <c:if test="${game.userId != 2}">
        <div style="left: 190px; position: relative; width: 115px; top: -57px;">
            <h3>Game Owned By</h3>
        </div>
        <div style="margin-left: 306px; position: relative; margin-top: -88px; width:50px;">
            <a href="<c:url value="/user/${gameOwner.username}"/>"
               title="${gameOwner.username}">
                <img src="https://secure.gravatar.com/avatar/${gameOwner.emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                     style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                     class="avatarLink"
                     width="24"
                     title="${gameOwner.username}"/>
            </a>
        </div>
    </c:if>
</div>
