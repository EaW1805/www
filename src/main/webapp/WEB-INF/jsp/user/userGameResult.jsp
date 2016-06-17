<%
    response.setHeader("Access-Control-Allow-Origin", "http://forum.eaw1805.com");
    response.setHeader("Access-Control-Allow-Credentials", "true");

%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<jsp:useBean id="userGameCustomResults" scope="request" type="java.util.List<com.eaw1805.data.model.UserGame>"/>
<jsp:useBean id="userGameStats" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.lang.String>>"/>
<jsp:useBean id="gameStatsMessages" scope="request" type="java.util.Map<java.lang.String, java.lang.String>"/>
<jsp:useBean id="dates" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
<jsp:useBean id="userGamesDeadTurns" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="userGameVps" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="userGameModifier" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Double>"/>
<c:forEach items="${userGameCustomResults}" var="userGameResult" varStatus="status">
    <jsp:useBean id="userGameResult" type="com.eaw1805.data.model.UserGame"/>

    <div id="userGameResult_container_${status.index}" style="position: fixed; z-index:5;" viewed="0"
         userGame="${userGameResult.id}" scenario="${userGameResult.game.scenarioIdToString}">
        <article>
            <section class="gameInfo" style="opacity: 1 !important; border: 0;">
                status:  ${resultStatus}
                <c:choose>
                    <c:when test="${resultStatus == 'winner'}">
                        <img src="http://static.eaw1805.com/images/panels/gameResults/winner.png"
                             style="position: absolute; left: 0px; top: 0px; ">
                    </c:when>
                    <c:when test="${resultStatus == 'runner'}">
                        <img src="http://static.eaw1805.com/images/panels/gameResults/runnerUp.png"
                             style="position: absolute; left: 0px; top: 0px; ">
                    </c:when>
                    <c:when test="${resultStatus == 'cowinner'}">
                        <img src="http://static.eaw1805.com/images/panels/gameResults/winner.png"
                             style="position: absolute; left: 0px; top: 0px; ">
                    </c:when>
                    <c:when test="${resultStatus == 'survivor'}">
                        <img src="http://static.eaw1805.com/images/panels/gameResults/survivor.png"
                             style="position: absolute; left: 0px; top: 0px; ">
                    </c:when>
                    <c:when test="${resultStatus == 'dropOut'}">
                        <img src="http://static.eaw1805.com/images/panels/gameResults/dropOut.png"
                             style="position: absolute; left: 0px; top: 0px; ">
                    </c:when>
                    <c:when test="${resultStatus == 'dead'}">
                        <img src="http://static.eaw1805.com/images/panels/gameResults/dead.png"
                             style="position: absolute; left: 0px; top: 0px; ">
                    </c:when>
                </c:choose>

                <h1 style="float: left; font-size: 27px ! important; width: 752px; position: absolute; left: 0px; top: 216px; text-align: center;">
                    <a style="font-size: 40px ! important;"
                       href="<c:url value="/scenario/${userGameResult.game.scenarioIdToString}/game/${userGameResult.game.gameId}/info"/>">Game ${userGameResult.game.gameId}</a>
                    / ${dates[userGameResult.game.gameId]}
                </h1>

                <div style="width: 200px; position: absolute; left: 334px; top: 265px;">
                    <b>Scenario ${userGameResult.game.scenarioIdToString}</b>
                </div>

                <a href='<c:url value="/scenario/${userGameResult.game.scenarioIdToString}/nation/${userGameResult.nation.id}"/>'>
                    <img style="clear: both; position: absolute; left: 276px; top: 295px; width: 200px;"
                         src='http://static.eaw1805.com/images/nations/nation-${userGameResult.nation.id}-120.png'
                         alt="Nation Info Page"
                         class="toolTip"
                         title="${userGameResult.nation.name} Info Page"
                         border=0>
                </a>

                <div style="position: absolute; left: 40px; top: 290px;">
                    <c:set var="thisGameStats" value="${userGameStats[userGameResult.game.gameId]}"/>
                    <c:set var="index" value="${0}"/>
                    <div style="clear:both;float: left;margin-left: 5px; width: 150px;height: 124px;">
                        <h3 style="float: right;">World position</h3>

                        <c:forEach items="${thisGameStats}" var="stats">
                            <c:if test="${index < 6}">
                                <div style="clear:both;float: left;margin-left: 5px;width: 190px;">
                                    <div style="float: left;width: 150px;text-align: right;margin-right: 10px;">${gameStatsMessages[stats.key]} </div>
                                    <div style="float: left;width: 25px;">${stats.value}</div>
                                </div>
                            </c:if>
                            <c:set var="index" value="${index+1}"/>
                        </c:forEach>
                    </div>

                </div>


                <div style="width:130px; position: absolute; left:310px; top: 434px;">
                    <c:choose>
                        <c:when test="${userGameResult.alive}">
                            <a href='<c:url value="/report/scenario/${userGameResult.game.scenarioIdToString}/game/${userGameResult.game.gameId}/nation/${userGameResult.nation.id}/newsletter"/>'
                               title="Read Newsletter"
                               target="_blank"
                               style="line-height: 1.3!important;"><span
                                    style="font-size: 22px; color: #000000!important;">Newsletter</span></a>
                        </c:when>
                        <c:otherwise>
                            <a href='<c:url value="/report/scenario/${userGameResult.game.scenarioIdToString}/game/${userGameResult.game.gameId}/nation/${userGameResult.nation.id}/month/${userGamesDeadTurns[userGameResult.game.gameId]}"/>'
                               title="Read Newsletter"
                               target="_blank"
                               style="line-height: 1.3!important;"><span
                                    style="font-size: 22px; color: #000000!important;">Newsletter</span></a>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${userGameResult.current}">
                    <div style="position: absolute; left:577px; top:434px; width: 200px;">
                        <a href='<c:url value="/play/scenario/${userGameResult.game.scenarioIdToString}/game/${userGameResult.game.gameId}/nation/${userGameResult.nation.id}"/>'
                           title="View Nation State"
                           target="_blank"
                           style="float:left;line-height: 1.3!important;"><span
                                style="font-size: 22px;color: #000000!important;">Nation State</span></a>
                    </div>


                    <div style="position: absolute; left: 551px; top: 309px;">
                            <%--<h3 style="float:right;">World Domination</h3>--%>

                        <h2 style="float:right; font-size: 30px!important; text-align: center;">
                            vp: ${userGameVps[userGameResult.game.gameId]}<br>
                            <c:choose>
                                <c:when test="${userGameResult.game.type == 0}">
                                    <fmt:formatNumber
                                            type="number"
                                            maxFractionDigits="2"
                                            groupingUsed="true"
                                            value="${100 * userGameVps[userGameResult.game.gameId] / userGameResult.nation.vpWin}"/>%
                                </c:when>
                                <c:when test="${userGameResult.game.type == -1}">
                                    <fmt:formatNumber
                                            type="number"
                                            maxFractionDigits="2"
                                            groupingUsed="true"
                                            value="${100 * userGameVps[userGameResult.game.gameId] / (0.7 * userGameResult.nation.vpWin)}"/>%
                                </c:when>
                                <c:when test="${userGameResult.game.type == 1}">
                                    <fmt:formatNumber
                                            type="number"
                                            maxFractionDigits="2"
                                            groupingUsed="true"
                                            value="${100 * userGameVps[userGameResult.game.gameId] / (1.3 * userGameResult.nation.vpWin)}"/>%
                                </c:when>
                            </c:choose>
                        </h2>
                    </div>
                </c:if>
                <img src="http://static.eaw1805.com/images/buttons/ButAcceptOff.png"
                     onmouseover="this.src='http://static.eaw1805.com/images/buttons/ButAcceptHover.png'"
                     onmouseout="this.src='http://static.eaw1805.com/images/buttons/ButAcceptOff.png'"
                     style="position: absolute; width: 40px; left: 679px; top: 27px; cursor: pointer;"
                     onclick="hideUserGameResult();"
                     title="Close panel"/>

            </section>
        </article>
    </div>

</c:forEach>
