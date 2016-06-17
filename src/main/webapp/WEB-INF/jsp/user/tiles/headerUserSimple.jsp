<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="user" scope="request" class="com.eaw1805.data.model.User"/>
<jsp:useBean id="userDateJoined" scope="request" class="java.util.Date"/>
<jsp:useBean id="followingList" scope="request" type="java.util.List"/>
<jsp:useBean id="followersList" scope="request" type="java.util.List"/>
<jsp:useBean id="gameList" scope="request" type="java.util.List"/>
<jsp:useBean id="profileUser" scope="request" type="com.eaw1805.data.model.User"/>
<div class="player">
    <h1 class="player">
        <c:if test="${profileUser.userId == user.userId}">
        <a href="http://gravatar.com/emails/">
            </c:if>
            <img src="https://secure.gravatar.com/avatar/${profileUser.emailEncoded}?s=48&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                 class="avatarLink" alt="" height="48" width="48">
            <c:if test="${profileUser.userId == user.userId}">
        </a>
        </c:if>
        <c:out value="${profileUser.username}"/>
    </h1>

    <h2 class="player">
        ${profileUser.fullname}<br>
        ${profileUser.location}<br>
        Member Since <fmt:formatDate value="${userDateJoined}" type="both" pattern="MMM d, yyyy"/>
    </h2>

    <h3 class="player">
        <a href="<c:url value="/settings"/>">Following<br>${followingList.size()} players</a>
        <ul class="avatars">
            <c:forEach items="${followingList}" var="thisUser">
                <li>
                    <a href="<c:url value="/user/${thisUser.username}"/>" title="${thisUser.username}"><img
                            src="https://secure.gravatar.com/avatar/${thisUser.emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                            alt="" height="24" width="24"></a>
                </li>
            </c:forEach>
        </ul>

        <br>
        <a href="<c:url value="/user/${profileUser.username}/followers"/>">${followersList.size()} followers</a>
        <ul class="avatars">
            <c:forEach items="${followersList}" var="thisUser">
                <li>
                    <a href="<c:url value="/user/${thisUser.username}"/>" title="${thisUser.username}"><img
                            src="https://secure.gravatar.com/avatar/${thisUser.emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                            alt="" height="24" width="24"></a>
                </li>
            </c:forEach>
        </ul>

        <a href="<c:url value="/settings"/>">Watching ${gameList.size()}<br>games</a>
        <ul class="avatars">
            <c:forEach items="${gameList}" var="thisGame">
                <li><a href="<c:url value="/scenario/${thisGame.scenarioIdToString}/game/${thisGame.gameId}/info"/>"
                       title="Game ${thisGame.gameId}">${thisGame.gameId}</a></li>
            </c:forEach>
        </ul>
    </h3>
</div>
