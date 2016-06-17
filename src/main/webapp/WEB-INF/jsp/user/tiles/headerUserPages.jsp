<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="user" scope="request" class="com.eaw1805.data.model.User"/>
<h1 class="player">
      <span original-title="Change your avatar at gravatar.com" class="tooltipped downwards">
          <c:if test="${profileUser.userId == user.userId}">
                <a href="http://gravatar.com/emails/">
                    </c:if>
                    <img src="https://secure.gravatar.com/avatar/${profileUser.emailEncoded}?s=48&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                         alt="" height="48" width="48">

                    <c:if test="${profileUser.userId == user.userId}">
                </a>
          </c:if>
      </span>
    <c:out value="${profileUser.username}"/>
    <em>(<tiles:insertAttribute name="description" ignore="true"/>)</em>
</h1>

<div class="rule"></div>
