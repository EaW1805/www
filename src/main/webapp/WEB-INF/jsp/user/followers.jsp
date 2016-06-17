<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="name" scope="request" class="java.lang.String"/>


<c:choose>
    <c:when test="${profileUser.userId == user.userId}">
        <h2>Players Following Me !</h2>
    </c:when>
    <c:otherwise>
        <h2>${profileUser.username}'s Followers</h2>
    </c:otherwise>
</c:choose>
<br><br>

<c:forEach items="${follows}" var="follow">
    <hr>
    <div style="margin-bottom: 10px; padding-top: 5px;">
        <c:set var="follower" value="${follow.follower}"/>

        <a href="<c:url value="/user/${follower.username}"/>"
           style="font-size: 12px;"
           title="${follower.username}"><img
                style="margin-bottom: -12px;"
                src="https://secure.gravatar.com/avatar/${follower.emailEncoded}?s=36&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                alt="" height="36" width="36"> &nbsp; ${follower.username} (${follower.fullname})
        </a>

    </div>
</c:forEach>
<hr>

