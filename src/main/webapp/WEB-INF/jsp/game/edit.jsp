    <%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<jsp:useBean id="user" scope="request" type="com.eaw1805.data.model.User"/>
<%@ include file="../../../js/dhtmlgoodies_calendar/calendar_css.jsp" %>
<%@ include file="../../../js/dhtmlgoodies_calendar/dhtmlgoodies_calendar.jsp" %>

<h3 class="remove-margin-t">Edit Game</h3>
<form:form commandName="game" method="POST" action='edit'>
    <div style="margin:0;padding:0"></div>
    <div class="formbody">

        <label for="dateNextProc">Next Processing Date:</label>
        <form:input path="dateNextProc" id="dateNextProc" readonly="true"/>
        <input type="button" value="Cal"
               onclick="displayCalendar(document.getElementById('dateNextProc'),'dd/mm/yyyy',this)">
        <form:errors path="dateNextProc" cssClass="error"/><br/><br/>

        <label for="schedule">Periodicity:</label>
        <form:input path="schedule" id="schedule"/>
        <form:errors path="schedule" cssClass="error"/><br/><br/>

        <label for="discount">Discount For All Nations(%):</label>
        <form:input path="discount" id="discount"/>
        <form:errors path="discount" cssClass="error"/><br/><br/>

        <label for="ended">Has Ended:</label>
        <form:checkbox path="ended" id="ended"/>
        <form:errors path="ended" cssClass="error"/><br/><br/>

        <label class='submit_btn'>
            <input type="submit" value="Save Changes"/>
        </label>
    </div>
</form:form>
<br><br><br>

<table border="1">
    <tr>
        <th>Nation</th>
        <th>User</th>
        <th>Original Cost</th>
        <th>Fixed Cost</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
    <c:forEach items="${nations}" var="nation">
        <tr>
            <td>
                <img src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
                     alt="Nation's Flag"
                     class="toolTip"
                     title="Flag of ${nation.name}"
                     border=0 width=28>
            </td>
            <c:choose>
                <c:when test="${nationToUser[nation.id]!=null}">
                    <td>${nationToUsername[nation.id].username}</td>
                    <td>${nation.cost}</td>
                    <td>${nationToUser[nation.id].cost}</td>
                    <td>
                        <c:choose>
                            <c:when test="${nationToUser[nation.id].alive}">
                                Alive
                            </c:when>
                            <c:otherwise>
                                dead
                            </c:otherwise>
                        </c:choose>
                    </td>
                </c:when>
                <c:otherwise>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </c:otherwise>
            </c:choose>

            <td>
                <a href='<c:url value="/scenario/${scenarioIdStr}/game/${gameId}/nation/${nation.id}/edit"/> '>
                    edit
                </a>
            </td>
        </tr>
    </c:forEach>
</table>
