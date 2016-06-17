<%@ page import="java.util.Date" %>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<jsp:useBean id="user" scope="request" type="com.eaw1805.data.model.User"/>


<script type="text/javascript">

    $(document).ready(function () {

        <c:if test="${param.asc != null}">
        $('#${param.asc}').attr('class', 'headerSortDown');
        </c:if>

        <c:if test="${param.desc != null}">
        $('#${param.desc}').attr('class', 'headerSortUp');
        </c:if>

    });

    function resortList(th) {
        if (th.getAttribute('class') === "header") {
            $('.headerSortUp').attr('class', 'header');
            $('.headerSortDown').attr('class', 'header');
            $('#' + th.getAttribute('id')).attr('class', 'headerSortDown');
            window.location = '<c:url value="/users/list?desc="/>' + th.getAttribute('id');
        } else if (th.getAttribute('class') === "headerSortUp") {
            $('.headerSortUp').attr('class', 'header');
            $('.headerSortDown').attr('class', 'header');
            $('#' + th.getAttribute('id')).attr('class', 'headerSortUp');
            window.location = '<c:url value="/users/list?asc="/>' + th.getAttribute('id');
        } else if (th.getAttribute('class') === "headerSortDown") {
            $('.headerSortUp').attr('class', 'header');
            $('.headerSortDown').attr('class', 'header');
            $('#' + th.getAttribute('id')).attr('class', 'headerSortDown');
            window.location = '<c:url value="/users/list?desc="/>' + th.getAttribute('id');
        }
    }
</script>
<article>

    <table id="myTable" class="tablesorter" style="margin-left: 0px; width: 960px;">
        <thead style="margin-bottom: 40px;">
        <tr style="height: 52px;">
            <th onclick="resortList(this);" onmousedown="return false;" class="header" id="userId" style="height: 52px;"
                width="20px;"><h2 style="font-size: 12pt; text-align: center;">ID</h2></th>

            <th width="15px;"/>

            <th onclick="resortList(this);" onmousedown="return false;" class="header" id="player" style="height: 52px;"
                width="180px;"><h2 style="font-size: 12pt; text-align: center;">Player</h2></th>

            <th onclick="resortList(this);" onmousedown="return false;" class="header" id="fullName"
                style="height: 52px;"
                width="140px;"><h2 style="font-size: 12pt; text-align: center;">FullName</h2></th>

            <th onclick="resortList(this);" onmousedown="return false;" class="header" id="location"
                style="height: 52px;" width="100px;"><h2 style="font-size: 12pt; text-align: center;">Location</h2>
            </th>

            <th onclick="resortList(this);" onmousedown="return false;" class="header" id="free" style="height: 52px;"
                width="100px;"><h2 style="font-size: 12pt; text-align: center;">Free</h2></th>

            <th onclick="resortList(this);" onmousedown="return false;" class="header" id="transferred"
                style="height: 52px;"
                width="100px;"><h2 style="font-size: 12pt; text-align: center;">Transferred</h2></th>

            <th onclick="resortList(this);" onmousedown="return false;" class="header" id="bought" style="height: 52px;"
                width="100px;"><h2 style="font-size: 12pt; text-align: center;">Bought</h2></th>

            <th onclick="resortList(this);" onmousedown="return false;" class="header" id="dateJoin"
                style="height: 52px;"
                width="100px;"><h2 style="font-size: 12pt; text-align: center;">Join Date</h2></th>

            <th onclick="resortList(this);" onmousedown="return false;" class="header" id="userLastVisit"
                style="height: 52px;"
                width="100px;"><h2 style="font-size: 12pt; text-align: center;">Last Visit</h2></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${usersList.pageList}" var="thisUser">
            <tr style="height: auto;">

                <td width="15px;" style="text-align: left;height: auto;font-size: 8px;">
                        ${thisUser.userId}
                </td>
                <td width="21px;" style="text-align: right;height: auto;">
                    <a href="mailto:${thisUser.email}">
                        <div
                                style="width: 20px;
                                height: 20px;
                                background: url(http://forum.eaw1805.com/styles/eaw/imageset/icon_contact_email.gif);
                                float:right;"
                                title="${thisUser.email}"></div>
                    </a>
                </td>
                <td width="45px;" style="height: auto;"><a
                        href="<c:url value="/user/${thisUser.username}"/>"
                        title="${thisUser.username}"><img
                        src="https://secure.gravatar.com/avatar/${thisUser.emailEncoded}?s=33&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                        alt="${thisUser.username}" height="33"
                        width="33"
                        title="${thisUser.username}"
                        class="tooltip"
                        style="margin-left: 5px; border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"></a>
                </td>

                <td width="130px;" style="text-align: left; height: auto;"><a
                        href="<c:url value="/user/${thisUser.username}"/>"
                        title="${thisUser.username}">${thisUser.username}</a>
                </td>
                <td width="120px;" style="text-align: right;height: auto;">
                        ${thisUser.fullname}
                </td>
                <td width="100px;" style="text-align: right;height: auto;">
                        ${thisUser.location}
                </td>

                <td width="95px;" style="text-align: right;height: auto;">
                    <a href="<c:url value="/user/${thisUser.username}/paymentHistory"/>"
                       title="Payment History">${thisUser.creditFree}
                    </a>
                </td>
                <td width="100px;" style="text-align: right;height: auto;">
                    <a href="<c:url value="/user/${thisUser.username}/paymentHistory"/>"
                       title="Payment History">${thisUser.creditTransferred}
                    </a>
                </td>
                <td width="100px;" style="text-align: right;height: auto;">
                    <a href="<c:url value="/user/${thisUser.username}/paymentHistory"/>"
                       title="Payment History">${thisUser.creditBought}
                    </a>
                </td>
                <td width="95px;" style="text-align: right;height: auto;">
                    <c:set var="thisDate" value="${thisUser.dateJoin*1000}" scope="page"/>
                    <%
                        long thisDate = (Long) pageContext.getAttribute("thisDate");
                        pageContext.setAttribute("thisDate", new Date(thisDate));
                    %>
                    <fmt:formatDate value="${thisDate}" type="both" pattern="MMM d, yyyy"/>
                </td>
                <td width="100px;" style="text-align: right;height: auto;">
                    <c:set var="thisDate" value="${thisUser.userLastVisit*1000}" scope="page"/>
                    <%
                        thisDate = (Long) pageContext.getAttribute("thisDate");
                        pageContext.setAttribute("thisDate", new Date(thisDate));
                    %>
                    <fmt:formatDate value="${thisDate}" type="both" pattern="MMM d, yyyy"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="paging" style="clear:both;margin-top: 5px;margin-left: 50%; height: 15px;">
        <c:if test="${!usersList.firstPage}">
            <a class="previous" href='<c:url value="/users/list?page=previous"/>' title="Previous Page"
               style="width: 40px; margin-left: -50px; padding-left: -50px;">
                <div id="previous" style="width: 40px;margin-left: -45px;float: left;"></div>
            </a>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <c:if test="${!usersList.lastPage}">
            <a class="next" href='<c:url value="/users/list?page=next"/>' title="Next Page"
               style="width: 40px;margin-left: -50px; padding-left: -50px;">
                <div id="next" style="width: 40px;float: left;"></div>
            </a>
        </c:if>
    </div>
</article>
