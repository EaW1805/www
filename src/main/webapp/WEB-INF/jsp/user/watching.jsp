<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="name" scope="request" class="java.lang.String"/>
<c:choose>
    <c:when test="${profileUser.userId == user.userId}">
        <h2>Games I am watching !</h2>
    </c:when>
    <c:otherwise>
        <h2>Games ${profileUser.username} is watching!</h2>
    </c:otherwise>
</c:choose>
<br><br>
<c:forEach items="${watches}" var="watch">
    <hr>
    <c:set var="game" value="${watch.game}"/>
    <div>
        <a href='<c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/info"/>'>
            Game #${game.gameId}
        </a>
        <c:if test="${user.userId == profileUser.userId}">
            <ul class="singleGameActions">
                <li>
                    <a href='<c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/toggleWatch/unwatch"/>'
                       class="minibutton"
                       title="<spring:message code="baseLayout.profile.tooltip"/>"
                       style="float:right;"><span>Unwatch</span></a>
                </li>
            </ul>
        </c:if>
        <br><br>
    </div>
</c:forEach>
<hr>