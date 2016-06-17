<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="name" scope="request" class="java.lang.String"/>

<c:choose>
    <c:when test="${profileUser.userId == user.userId}">
        <h2>Players I am Following!</h2>
        <script type="text/javascript">
            //init autocommplete functionality.
            $(function () {
                var allUsers = new Array();
                <c:forEach items="${allUsers}" var="curUser" varStatus="status">
                allUsers[${status.index}] = '${curUser.username}';
                </c:forEach>
                a2 = $('#searchUser').autocomplete({

//                                                width: 384,
                    delimiter:/(,|;)\s*/,
                    lookup:allUsers
                });
            });

            function followUser() {
                window.location = '<c:url value="/user/"/>' + document.getElementById("searchUser").value + '/toggleFollow/follow';
            }

            function visitProfile() {
                window.location = '<c:url value="/user/"/>' + document.getElementById("searchUser").value;
            }
        </script>

        search:
        <input type="text" name="searchUser" id="searchUser"/>
        <input type="button" value="Follow" onclick="followUser()"/>
        <input type="button" value="Profile" onclick="visitProfile()"/>
        <br><br>
    </c:when>
    <c:otherwise>
        <h2>Players ${profileUser.username} is Following</h2>
    </c:otherwise>
</c:choose>
<br><br>

<c:forEach items="${follows}" var="follow">
    <hr>
    <div>
        <c:set var="leader" value="${follow.leader}"/>
        <a href='<c:url value="/user/${leader.username}"/>'>
                ${leader.username} (${leader.fullname})
        </a>
        <c:if test="${user.userId == profileUser.userId}">
            <ul class="singleGameActions">
                <li>
                    <a href='<c:url value="/user/${leader.username}/toggleFollow/unfollow"/>'
                       class="minibutton"
                       title="<spring:message code="baseLayout.profile.tooltip"/>"
                       style="float:right;"><span>Unfollow</span></a>
                </li>
            </ul>
        </c:if>
        <br><br>
    </div>
</c:forEach>
<hr>
