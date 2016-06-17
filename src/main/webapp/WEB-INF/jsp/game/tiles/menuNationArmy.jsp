<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="gameId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="nationId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="game" scope="request" class="com.eaw1805.data.model.Game"/>
<ul class="tabs">
    <li><a href='<c:url value="/game/${gameId}/nation/${nationId}"/>'>Nation</a></li>
    <li><a href='<c:url value="/game/${gameId}/nation/${nationId}/army"/>' class="selected">Armed Forces</a></li>
    <li><a href='<c:url value="/game/${gameId}/nation/${nationId}/economy"/>'>Economy</a></li>
    <li><a href='<c:url value="/game/${gameId}/nation/${nationId}/politics"/>'>Politics</a></li>
    <li><a href='<c:url value="/game/${gameId}/nation/${nationId}/overview"/>'>Reports</a></li>
</ul>
<h1 class="playButton">
    <a href='<c:url value="/play/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}"/>' target="_blank">
        <img src='http://static.eaw1805.com/images/buttons/PlayButtonSmall.png'
             alt="Play game"
             class="toolTip"
             title="Play game"
             aling="right"
             border=0></a>
</h1>
<ul class="subtabs">
    <li>
        <a href='<c:url value="/game/${gameId}/nation/${nationId}/army"/>'
                <% if (request.getParameter("page").equals("army")) { %>
           class="selected"
                <%}%>>Armies</a>
        <a href='<c:url value="/game/${gameId}/nation/${nationId}/navy"/>'
                <% if (request.getParameter("page").equals("navy")) { %>
           class="selected"
                <%}%>>Fleets</a>
        <a href='<c:url value="/game/${gameId}/nation/${nationId}/commanders"/>'
                <% if (request.getParameter("page").equals("commanders")) { %>
           class="selected"
                <%}%>>Commanders</a>
        <a href='<c:url value="/game/${gameId}/nation/${nationId}/barracks"/>'
                <% if (request.getParameter("page").equals("barracks")) { %>
           class="selected"
                <%}%>>Barracks &amp; Shipyards</a>
    </li>
</ul>
