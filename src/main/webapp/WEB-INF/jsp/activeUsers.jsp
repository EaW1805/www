<%
    response.setHeader("Access-Control-Allow-Origin", "http://forum.eaw1805.com");
%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.concurrent.TimeUnit" %>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:forEach items="${activeUsers}" var="activeUser">
    <div id="active-${activeUser.userId}"
         style="position: relative; overflow: hidden; width: 170px; height: 45px; background-size:170px 45px;"
         class="orderInfoPanel">

        <div class="clearFontMini" style="position: absolute; left: 3px; top: 3px; float: left;">
            <a href='<c:url value="http://www.eaw1805.com/user/${activeUser.username}"/>'>
                <img src="https://secure.gravatar.com/avatar/${activeUser.emailEncoded}?s=38&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                     style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                     alt="" height="38" width="38" title="${activeUser.username}">
            </a>
        </div>
        <div style="float: left; margin-left: 50px; margin-top: 2px;font: small/1.4 helvetica,arial,freesans,clean,sans-serif;">
                ${activeUser.username}
        </div>
        <div style="clear:both;float: left; margin-left: 50px; margin-top: -2px;font: 8px helvetica,arial,freesans,clean,sans-serif;">
            Active for:
        </div>
        <c:set var="thisDate" value="${activeUser.loginTime}" scope="page"/>
        <%
            final TimeUnit timeUnit = TimeUnit.MINUTES;
            final Date loginTime = (Date) pageContext.getAttribute("thisDate");
            final long diffInMillies = System.currentTimeMillis() - loginTime.getTime();
            pageContext.setAttribute("thisDate", timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS));
        %>
        <div style="clear:both;float: left; margin-left: 50px; font: 8px helvetica,arial,freesans,clean,sans-serif;">
                ${thisDate} minutes
        </div>

        <a href='http://www.eaw1805.com/user/${activeUser.username}'>
            <img src="http://static.eaw1805.com/images/buttons/ButMagGlassOff.png"
                 class="pointer" id="widget-704"
                 title="Go to position related to this order"
                 style="width: 20px; height: 20px; position: absolute; left: 147px; top: 22px; ">
        </a>
    </div>
</c:forEach>