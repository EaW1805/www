<%@ page import="org.springframework.beans.support.PagedListHolder" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="hofProfiles" scope="request" type="org.springframework.beans.support.PagedListHolder"/>
<%!
    List<Integer> getNavigationMenu(final PagedListHolder messagesList) {

        final List<Integer> navigationMenu = new ArrayList<Integer>();

        final int currentPage = messagesList.getPage();
        final int numberOfPages = messagesList.getPageCount();

        if (messagesList.isFirstPage()) {
            if (numberOfPages > 5) {
                for (int i = 0; i < 4; i++) {
                    navigationMenu.add(i);
                }
                navigationMenu.add(-1);
                navigationMenu.add(numberOfPages - 1);
            } else {
                for (int i = 0; i < numberOfPages; i++) {
                    navigationMenu.add(i);
                }
            }
        } else if (messagesList.isLastPage()) {
            if (numberOfPages > 5) {
                navigationMenu.add(0);
                navigationMenu.add(-1);
                for (int i = numberOfPages - 4; i < numberOfPages; i++) {
                    navigationMenu.add(i);
                }
            } else {
                for (int i = 0; i < numberOfPages; i++) {
                    navigationMenu.add(i);
                }
            }
        } else if (numberOfPages < 5) {
            for (int i = 0; i < numberOfPages; i++) {
                navigationMenu.add(i);
            }
        } else if (numberOfPages > 5) {

            for (int i = currentPage - 1; i <= currentPage + 1; i++) {
                if (i >= 0 && i <= numberOfPages) {
                    navigationMenu.add(i);
                }
            }

            if (navigationMenu.get(0) != 0) {
                if (navigationMenu.get(1) != 2) {
                    navigationMenu.add(0, -1);
                }
                navigationMenu.add(0, 0);
            }

            if (navigationMenu.get(navigationMenu.size() - 1) != numberOfPages && navigationMenu.get(navigationMenu.size() - 1) <= numberOfPages - 2) {
                if (navigationMenu.get(navigationMenu.size() - 1) != numberOfPages - 2) {
                    navigationMenu.add(-1);

                }
                navigationMenu.add(numberOfPages - 1);

            }

            if (navigationMenu.size() == 5) {
                if (navigationMenu.get(navigationMenu.size() - 2) == -1) {
                    navigationMenu.add(3, 3);
                }

                if (navigationMenu.get(1) == -1) {
                    navigationMenu.add(2, numberOfPages - 4);
                }
            }

        }

        return navigationMenu;
    }
%>
<script type="text/javascript">

    $(document).ready(function () {

        <c:if test="${param.asc != null}">
        $('#${param.asc}').attr('class', 'headerSortDown');
        </c:if>

        <c:if test="${param.desc != null}">
        $('#${param.desc}').attr('class', 'headerSortUp');
        </c:if>

    });

    function resortHof(th) {
        if (th.getAttribute('class') === "header") {
            $('.headerSortUp').attr('class', 'header');
            $('.headerSortDown').attr('class', 'header');
            $('#' + th.getAttribute('id')).attr('class', 'headerSortDown');
            window.location = '<c:url value="/hallOfFame?desc="/>' + th.getAttribute('id');
        } else if (th.getAttribute('class') === "headerSortUp") {
            $('.headerSortUp').attr('class', 'header');
            $('.headerSortDown').attr('class', 'header');
            $('#' + th.getAttribute('id')).attr('class', 'headerSortUp');
            window.location = '<c:url value="/hallOfFame?asc="/>' + th.getAttribute('id');
        } else if (th.getAttribute('class') === "headerSortDown") {
            $('.headerSortUp').attr('class', 'header');
            $('.headerSortDown').attr('class', 'header');
            $('#' + th.getAttribute('id')).attr('class', 'headerSortDown');
            window.location = '<c:url value="/hallOfFame?desc="/>' + th.getAttribute('id');
        }
    }
</script>
<article>
<table id="myTable" class="tablesorter">
    <thead style="margin-bottom: 40px;">
    <tr style="height: 52px;">
        <th onclick="resortHof(this);" onmousedown="return false;" class="header" id="player" style="height: 52px;"
            width="125px;"><h2 style="font-size: 12pt; text-align: center;">Player</h2></th>
        <th onclick="resortHof(this);" onmousedown="return false;" class="header" id="vps" style="height: 52px;"
            width="70px;"><h2 style="font-size: 12pt; text-align: center;">Honour</h2></th>
        <th onclick="resortHof(this);" onmousedown="return false;" class="header" id="achievements"
            style="height: 52px;" width="110px;"><h2 style="font-size: 12pt; text-align: center;">Achievements</h2>
        </th>
        <th onclick="resortHof(this);" onmousedown="return false;" class="header" id="games" style="height: 52px;"
            width="70px;"><h2 style="font-size: 12pt; text-align: center;">Games played</h2></th>
        <th onclick="resortHof(this);" onmousedown="return false;" class="header" id="tbw" style="height: 52px;"
            width="90px;"><h2 style="font-size: 12pt; text-align: center;">Tactical<br/>Battles won</h2></th>
        <th onclick="resortHof(this);" onmousedown="return false;" class="header" id="fbw" style="height: 52px;"
            width="85px;"><h2 style="font-size: 12pt; text-align: center;">Field<br/>Battles won</h2></th>
        <th onclick="resortHof(this);" onmousedown="return false;" class="header" id="nbw" style="height: 52px;"
            width="85px;"><h2 style="font-size: 12pt; text-align: center;">Naval<br/>Battles won</h2></th>
        <th onclick="resortHof(this);" onmousedown="return false;" class="header" id="tk" style="height: 52px;"
            width="85px;"><h2 style="font-size: 12pt; text-align: center;">Troops<br/>killed</h2></th>
        <th onmousedown="return false;" width="110px;"><h2 style="font-size: 12pt; text-align: center;">Last 2<br/>Nations
            played</h2></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${hofProfiles.pageList}" var="hofProfile">
        <tr>
            <td width="45px;"><a href="<c:url value="/user/${hofProfile.user.username}"/>"
                                 title="${hofProfile.user.username}"><img
                    src="https://secure.gravatar.com/avatar/${hofProfile.user.emailEncoded}?s=33&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                    alt="${hofProfile.user.username}" height="33"
                    width="33"
                    title="${hofProfile.user.username}"
                    class="tooltip"
                    style="margin-left: 5px; border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"></a>
            </td>
            <td width="90px;" style="text-align: left;"><a href="<c:url value="/user/${hofProfile.user.username}"/>"
                                                           title="${hofProfile.user.username}">${hofProfile.user.username}</a>
            </td>
            <td width="35px;" style="text-align: right;">
                <c:choose>
                    <c:when test="${hofProfile.vps != hofProfile.undefined}">
                        ${hofProfile.vps}
                    </c:when>
                    <c:otherwise>
                        <span style="display: none"> -50000</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <td width="30px;" style="text-align: left;">
                    <span class="positions">
                    <c:if test="${hofProfile.vpsPosition  != hofProfile.undefined}">
                        (${hofProfile.vpsPosition})
                    </c:if>
                    </span>
            </td>
            <td width="65px;" style="text-align: right;">
                <c:choose>
                    <c:when test="${hofProfile.achievements != hofProfile.undefined}">
                        ${hofProfile.achievements}
                    </c:when>
                    <c:otherwise>
                        <span style="display: none"> -50000</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <td width="35px;" style="text-align: left;">
                <c:if test="${hofProfile.achievementsPosition != hofProfile.undefined}">
                    <span class="positions">(${hofProfile.achievementsPosition})</span>
                </c:if>
            </td>
            <td width="80px;"><fmt:formatNumber
                    type="number"
                    maxFractionDigits="0"
                    groupingUsed="true"
                    value='${hofProfile.playedGames}'/></td>
            <td width="55px;" style="text-align: right;">
                <c:choose>
                    <c:when test="${hofProfile.battlesTacticalWon != hofProfile.undefined}">
                        <fmt:formatNumber
                                type="number"
                                maxFractionDigits="0"
                                groupingUsed="true"
                                value='${hofProfile.battlesTacticalWon}'/>
                    </c:when>
                    <c:otherwise>
                        <span style="display: none"> -50000</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <td width="30px;" style="text-align: left;">
                <c:if test="${hofProfile.battlesTacticalWon != hofProfile.undefined}">
                    <span class="positions">(${hofProfile.battlesTacticalWonPosition})</span>
                </c:if>
            </td>
            <td width="40px;" style="text-align: right;">
                <c:choose>
                    <c:when test="${hofProfile.battlesFieldWon  != hofProfile.undefined}">
                        <fmt:formatNumber
                                type="number"
                                maxFractionDigits="0"
                                groupingUsed="true"
                                value='${hofProfile.battlesFieldWon}'/>
                    </c:when>
                    <c:otherwise>
                        <span style="display: none"> -50000</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <td width="30px;" style="text-align: left;">
                <c:if test="${hofProfile.battlesFieldWonPosition  != hofProfile.undefined}">
                    <span class="positions">(${hofProfile.battlesFieldWonPosition})</span>
                </c:if>
            </td>
            <td width="60px;" style="text-align: right;">
                <c:choose>
                    <c:when test="${hofProfile.battlesNavalWon  != hofProfile.undefined}">
                        <fmt:formatNumber
                                type="number"
                                maxFractionDigits="0"
                                groupingUsed="true"
                                value='${hofProfile.battlesNavalWon}'/>
                    </c:when>
                    <c:otherwise>
                        <span style="display: none"> -50000</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <td width="30px;" style="text-align: left;">
                <c:if test="${hofProfile.battlesNavalWonPosition  != hofProfile.undefined}">
                    <span class="positions">(${hofProfile.battlesNavalWonPosition})</span>
                </c:if>
            </td>
            <td width="70px;" style="text-align: right;">
                <c:choose>
                    <c:when test="${hofProfile.enemyKilled  != hofProfile.undefined}">
                        <fmt:formatNumber
                                type="number"
                                maxFractionDigits="0"
                                groupingUsed="true"
                                value='${hofProfile.enemyKilled}'/>
                    </c:when>
                    <c:otherwise>
                        <span style="display: none"> -50000</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <td width="30px;" style="text-align: left;">
                <c:if test="${hofProfile.enemyKilledPosition  != hofProfile.undefined}">
                    <span class="positions">(${hofProfile.enemyKilledPosition})</span>
                </c:if>
            </td>
            <c:set var="index" value="0"/>
            <c:forEach items="${userGames[hofProfile.user.userId]}" var="thisUserGame">

                <c:if test="${index < 2}">

                    <td width="30px;">
                        <a href='<c:url value="/scenario/${thisUserGame.game.scenarioIdToString}/game/${thisUserGame.game.gameId}/info"/>'>
                            <img style="float:left;clear: both; margin-left: 5px;"
                                 src='http://static.eaw1805.com/images/nations/nation-${thisUserGame.nation.id}-36.png'
                                 alt="Nation Info Page"
                                 class="toolTip"
                                 title="${thisUserGame.nation.name}, Game ${thisUserGame.game.gameId}"
                                 border=0>
                        </a>
                    </td>
                    <c:set var="index" value="${index+1}"/>
                </c:if>
            </c:forEach>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="paging" style="clear:both;margin-top: 15px;margin-left: 450px; height: 15px;">
    <c:if test="${!hofProfiles.firstPage}">
        <a class="previous" href="hallOfFame?page=previous" title="Previous Page"
           style="width: 40px; margin-left: -82px; padding-left: -50px;">
            <div id="previous" style="width: 40px;margin-left: -45px;float: left;"></div>
        </a>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </c:if>
    <c:if test="${!hofProfiles.lastPage}">
        <a class="next" href="hallOfFame?page=next" title="Next Page"
           style="width: 40px;margin-left: -10px; padding-left: -50px;">
            <div id="next" style="width: 40px;float: left;"></div>
        </a>
    </c:if>
    <h4 class="pagination"
        style="clear: both; float: right !important; width: 380px !important; margin-top: -50px !important; margin-right: 65px;">
        ${hofProfiles.nrOfElements} Famous Players • Page ${hofProfiles.page + 1 }
        of ${hofProfiles.pageCount} •

        <c:forEach items="<%=getNavigationMenu(hofProfiles)%>" var="thisPage">

            <c:if test="${thisPage == hofProfiles.page  }">
                <a class="current" href='<c:url value="/hallOfFame?page=${thisPage}"/>'> ${thisPage+1}</a>
            </c:if>
            <c:if test="${thisPage != hofProfiles.page  }">
                <c:if test="${thisPage != -1 }">
                    <a class="others" href='<c:url value="/hallOfFame?page=${thisPage}"/>'> ${thisPage+1}</a>
                </c:if>
                <c:if test="${thisPage == -1 }">
                    <custom style="font-size: 10px; margin: 0px;">...</custom>
                </c:if>
            </c:if>
        </c:forEach>
    </h4>
</div>
</article>