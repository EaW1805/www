<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="user" scope="request" class="com.eaw1805.data.model.User"/>
<jsp:useBean id="userDateJoined" scope="request" class="java.util.Date"/>
<jsp:useBean id="followingCnt" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="followingList" scope="request" type="java.util.List"/>
<jsp:useBean id="followersCnt" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="followersList" scope="request" type="java.util.List"/>
<jsp:useBean id="watchCnt" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="gameList" scope="request" type="java.util.List"/>
<jsp:useBean id="profileUser" scope="request" type="com.eaw1805.data.model.User"/>
<script type="text/javascript">
    function changeAvatar() {
        window.location = "http://gravatar.com/emails/";
    }
</script>
<div class="player" style="height: 200px;">
    <h1 class="player">
        <div style="float:left; width: 60px;">
            <div style="float: left; width:50px;">
                <img src="https://secure.gravatar.com/avatar/${profileUser.emailEncoded}?s=48&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                     class="avatarLink" alt="" height="48" width="48"
                        <c:if test="${profileUser.userId == user.userId}">
                            onclick="changeAvatar();"
                            OnMouseOver="this.style.cursor='pointer';"
                            OnMouseOut="this.style.cursor='default';"
                        </c:if>
                        />
            </div>
            <div style="clear:both; width: 55px; font-size: 11px; text-align: center;">
                <c:if test="${profileUser.userId != user.userId}">
                    <c:choose>
                        <c:when test="${isFollowed}">
                            <a href="<c:url value="/user/${profileUser.username}/toggleFollow/unfollow"/>">Unfollow</a>
                        </c:when>
                        <c:otherwise>
                            <a href="<c:url value="/user/${profileUser.username}/toggleFollow/follow"/>">Follow</a>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </div>
        </div>
        <div style="float:left;margin-top: -3px;">
            <c:out value="${profileUser.username}"/>
        </div>
    </h1>

    <h2 class="player">
        ${profileUser.fullname}<br>
        ${profileUser.location}<br>
        Member Since <fmt:formatDate value="${userDateJoined}" type="both" pattern="MMM d, yyyy"/>
    </h2>

    <h3 class="player">
        <c:choose>
            <c:when test="${profileUser.userId != user.userId}">
                <a href="<c:url value="/user/${profileUser.username}"/>">Following<br>${followingCnt} players</a>
            </c:when>
            <c:otherwise>
                <a href="<c:url value="/settings"/>">Following<br>${followingCnt} players</a>
            </c:otherwise>
        </c:choose>
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
        <a href="<c:url value="/user/${profileUser.username}/followers"/>">${followersCnt} followers</a>
        <ul class="avatars">
            <c:forEach items="${followersList}" var="thisUser">
                <li>
                    <a href="<c:url value="/user/${thisUser.username}"/>" title="${thisUser.username}"><img
                            src="https://secure.gravatar.com/avatar/${thisUser.emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                            alt="" height="24" width="24"></a>
                </li>
            </c:forEach>
        </ul>

        <a href="<c:url value="/settings"/>">Watching<br>games</a>
        <ul class="avatars">
            <c:forEach items="${gameList}" var="thisGame">
                <li class="watch"><a href="<c:url value="/scenario/${thisGame.scenarioIdToString}/game/${thisGame.gameId}/info"/>"
                                     title="Game ${thisGame.gameId}">${thisGame.gameId}</a></li>
            </c:forEach>
        </ul>
    </h3>
</div>
