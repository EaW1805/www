<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="gameId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="nationId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="game" scope="request" class="com.eaw1805.data.model.Game"/>
<ul class="tabs withsubs">
    <li><a href='<c:url value="/game/${gameId}/nation/${nationId}"/>'>Nation</a></li>
    <li><a href='<c:url value="/game/${gameId}/nation/${nationId}/army"/>'>Armed Forces</a></li>
    <li><a href='<c:url value="/game/${gameId}/nation/${nationId}/economy"/>'>Economy</a></li>
    <li><a href='<c:url value="/game/${gameId}/nation/${nationId}/politics"/>'>Politics</a></li>
    <li><a href='<c:url value="/game/${gameId}/nation/${nationId}/overview"/>' class="selected">Reports</a></li>
</ul>
<ul class="subtabs">
    <li>
        <a href='<c:url value="/game/${gameId}/nation/${nationId}/reports"/>'
                <% if (request.getParameter("page").equals("reports")) { %>
           class="selected"
                <%}%>>Balance&nbsp;Sheets</a>
        <a href='<c:url value="/game/${gameId}/nation/${nationId}/report/orders/${game.turn}"/>'
                <% if (request.getParameter("page").equals("orders")) { %>
           class="selected"
                <%}%>>Orders</a>
        <a href='<c:url value="/game/${gameId}/nation/${nationId}/report/battles"/>'
                <% if (request.getParameter("page").equals("battles")) { %>
           class="selected"
                <%}%>>Battles</a>
    </li>
</ul>

