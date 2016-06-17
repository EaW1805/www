<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="gameId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="fortress" scope="request" type="java.lang.String"/>
<jsp:useBean id="winner" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="terrainType" scope="request" class="com.eaw1805.data.model.map.Terrain"/>
<jsp:useBean id="region" scope="request" class="com.eaw1805.data.model.map.Region"/>
<jsp:useBean id="sector" scope="request" class="com.eaw1805.data.model.map.Sector"/>
<jsp:useBean id="nation1" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="nation2" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="comm1" scope="request" class="com.eaw1805.data.model.army.Commander"/>
<jsp:useBean id="comm2" scope="request" class="com.eaw1805.data.model.army.Commander"/>
<jsp:useBean id="stats" scope="request" type="java.util.List<empire.battles.tactical.result.RoundStatistics>"/>
<jsp:useBean id="sectorsName" scope="request" type="java.lang.String"/>
<jsp:useBean id="justName" scope="request" type="java.lang.String"/>
<jsp:useBean id="battleDate" scope="request" type="java.lang.String"/>
<jsp:useBean id="gameDate" scope="request" type="java.lang.String"/>
<%@ include file="/WEB-INF/jsp/cards/battalionDTOCard.jsp" %>
<%
    int[] sideCnt = {0, 1};
    int[] troopType = {0, 1, 2, 3, 4};
    String[] imgSrc = {"infantry.png", "cavalry.png", "artillery.png", "engineers.png"};
    String[] imgTitle = {"Infantry", "Cavalry", "Artillery", "Engineers"};
    String[] roundTitle = {"Maneuvering of Troops",
            "Round 1: Artillery long-range combat<br/><span style='font-size: 16px; font-weight:normal;'>(artillery only)</span>",
            "Round 1b: Mounted Artillery long-range combat<br/><span style='font-size: 16px;'>(mounted artillery only)</span>",
            "Round 2: Morale Check +20%",
            "Round 3: Skirmishers long-range combat",
            "Round 4: Troop long-range combat<br/><span style='font-size: 16px; font-weight:normal;'>(artillery & non-skirmishers units only)</span>",
            "Round 5: Morale Check +10%",
            "Round 6: Hand-to-Hand combat",
            "Round 7: Morale Check",
            "Round 8: Cavalry Hand-to-Hand combat",
            "Round 9: Morale Check<br/><span style='font-size: 16px; font-weight:normal;'>(only for units attacked by cavalry)</span>",
            "Round 10: Disengagement Hand-to-Hand combat",
            "Round 11: Disengagement long-range combat<br/><span style='font-size: 16px; font-weight:normal;'>(artillery & infantry skirmish units only)</span>",
            "Determination of Winner",
            "Round 12: Pursuit",
            "Aftermath: Raise Experience",
            "Aftermath: Capture Commanders"};

    pageContext.setAttribute("sideCnt", sideCnt, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("troopType", troopType, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("imgSrc", imgSrc, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("imgTitle", imgTitle, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("roundTitle", roundTitle, PageContext.REQUEST_SCOPE);
%>
<c:set var="expStyle" value="color:brown;font-size:13px;"/>
<c:set var="headcountStyle" value="color:cyan;font-size:13px;"/>
<% if (request.getParameter("fixForClient") != null) { %>
<style type="text/css">
    h1 {
        color: #777777;
        font-size: 2em;
        font-weight: bold;
        text-align: center;
        margin: 0;
    }
</style>
<br/>
<%}%>
<br/>
<c:if test="${fn:length(justName) > 0}">
    <h5 class="battlepanel" style="color: black; text-align: center; font-size: 56px !important; text-shadow: 1.5px 1.5px 1px #7b714b;
font-weight: normal; letter-spacing: -1px; padding-top: 0px; margin-top: -30px;">The Battle of ${justName}</h5>
    <h5 class="battlepanel" style="color: black; text-align: center; font-size: 28px !important; text-shadow: 1.5px 1.5px 1px #7b714b;
font-weight: normal; letter-spacing: -1px; padding-top: 0px; margin-top: -30px; margin-bottom: -5px;">${battleDate}</h5>
</c:if>
<h1 class="battlepanel">
    <img class="tacticalpanel"
         src="http://static.eaw1805.com/images/battles/battlepanel.png"
         alt="Battle Panel">
    <img class="nationA"
        <% if (request.getParameter("fixForClient") != null) { %>
         style="margin-bottom: -75px; left: -252px"
        <%}%>
         src="http://static.eaw1805.com/images/nations/nation-${nation1.id}-120.png"
         alt="${nation1.name}"
         title="${nation1.name}">
    <img class="nationB"
        <% if (request.getParameter("fixForClient") != null) { %>
         style="margin-bottom: -75px; left: 431px"
        <%}%>
         src="http://static.eaw1805.com/images/nations/nation-${nation2.id}-120.png"
         alt="${nation2.name}"
         title="${nation2.name}">
    <img class="commA"
        <% if (request.getParameter("fixForClient") != null) { %>
         style="margin-bottom: -75px; left: -164px"
        <%}%>
    <c:choose>
         <c:when test="${comm1.intId <= 10}">src="http://static.eaw1805.com/img/commanders/s${game.scenarioId}/${comm1.nation.id}/${comm1.intId}.jpg"</c:when>
         <c:otherwise>src="http://static.eaw1805.com/img/commanders/commander.jpg"</c:otherwise>
    </c:choose>
         alt="${comm1.name}"
         title="${comm1.name}">
    <img class="commB"
        <% if (request.getParameter("fixForClient") != null) { %>
         style="margin-bottom: -75px; left: -44px"
        <%}%>
    <c:choose>
         <c:when test="${comm2.intId <= 10}">src="http://static.eaw1805.com/img/commanders/s${game.scenarioId}/${comm2.nation.id}/${comm2.intId}.jpg"</c:when>
         <c:otherwise>src="http://static.eaw1805.com/img/commanders/commander.jpg"</c:otherwise>
    </c:choose>
         alt="${comm2.name}"
         title="${comm2.name}">
</h1>

<h1 class="battlesidenameA"
        <% if (request.getParameter("fixForClient") != null) { %>
    style="margin-top: 8px; margin-left: 8px; "
        <%}%>
        >${nation1.name}</h1>

<h1 class="battlesidenameB"
        <% if (request.getParameter("fixForClient") != null) { %>
    style="margin-top: 8px; margin-left: 8px; "
        <%}%>
        >${nation2.name}</h1>

<table style="position:relative; left:13px; top:7px;">
    <tr>
        <td>
            <div style="width: 411px;float:left;">
                <c:forEach items="${allies1}" var="ally">
                    <img src="http://static.eaw1805.com/images/nations/nation-${ally.id}-120.png" style="width:60px"/>
                </c:forEach>
            </div>
        </td>
        <td>
            <div style="width:109px;"></div>
        </td>
        <td>
            <div style="width:411px;float:right;" align="right">
                <c:forEach items="${allies2}" var="ally">
                    <img src="http://static.eaw1805.com/images/nations/nation-${ally.id}-120.png" style="width:60px"/>
                </c:forEach>
            </div>
        </td>
    </tr>
</table>
<br>

<h2 class="battlepanel"
        <% if (request.getParameter("fixForClient") != null) { %>
    style="margin-top: 10px; margin-bottom: -10px;"
        <%}%>
        >
    <table class="battleinfo">
        <tr valign="bottom">
            <%--<td>--%>
            <%--<img src='<c:url value="/game/${gameId}/region/${sector.position.region.id}/nation/${nation1.id}/zoom/0.35/${sector.position.x-3}/${sector.position.y-3}/${sector.position.x+3}/${sector.position.y+3}/sniplet.png"/>'--%>
            <%--class="battlemap"--%>
            <%--alt="Tactical battle location on map ${sector.position.toString()}"--%>
            <%--title="Tactical battle took place at ${sector.position.toString()}">--%>
            <%--</td>--%>
            <td>
                Month: ${gameDate}<br/>
                Region: ${region.name}<br/>
                Terrain type: ${terrainType.name}<br/>
                Location: ${sector.position.toString()}<br/>
                Fortress: ${fortress}
            </td>
        </tr>
    </table>
</h2>
<h3 class="battleresult"
        <% if (request.getParameter("fixForClient") != null) { %>
    style="margin-top: 30px; margin-bottom: -60px;"
        <%}%>
        >
    <c:choose>
        <c:when test="${winner == 0}">${nation1.name} wins the tactical battle at ${sectorsName}</c:when>
        <c:when test="${winner == -1}">The tactical battle at ${sectorsName} was undecided</c:when>
        <c:otherwise>${nation2.name} wins the tactical battle at ${sectorsName}</c:otherwise>
    </c:choose>
</h3>
<table width="100%" class="battlepanel">
<c:forEach items="${stats}" var="stat">
<tr class="battlepanel">
    <th width="100%" colspan=7 class="battlepanel">${roundTitle[stat.round]}</th>
</tr>
<tbody id="${stat.round}">
<tr class="battlepanel">
    <c:forEach items="${sideCnt}" var="side">
        <td class="battlepanelleft" rowspan=7>
            <c:set value="${stat.sideStat[side]}" var="statSide"/>
            <c:choose>
                <c:when test="${stat.round == 0}">
                </c:when>
                <c:when test="${stat.round == 3 || stat.round == 6 || stat.round == 8}">
                    <h4 class="battlepanel">Already fleeing battalions: <fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${statSide[2]}"/><br/>
                        Battalions failed check: <fmt:formatNumber type="number" maxFractionDigits="0"
                                                                   groupingUsed="true"
                                                                   value="${statSide[0]}"/><br/>
                        Fleeing battalions regained morale: <fmt:formatNumber type="number"
                                                                              maxFractionDigits="0"
                                                                              groupingUsed="true"
                                                                              value="${statSide[1]}"/></h4>
                </c:when>
                <c:when test="${stat.round == 10}">
                    <h4 class="battlepanel">Already fleeing battalions: <fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${statSide[2]}"/><br/>
                        Battalions failed check: <fmt:formatNumber type="number" maxFractionDigits="0"
                                                                   groupingUsed="true"
                                                                   value="${statSide[0]}"/><br/>
                        Fleeing battalions regained morale: <fmt:formatNumber type="number"
                                                                              maxFractionDigits="0"
                                                                              groupingUsed="true"
                                                                              value="${statSide[1]}"/><br/>
                        Battalions that cannot form square: <fmt:formatNumber type="number"
                                                                              maxFractionDigits="0"
                                                                              groupingUsed="true"
                                                                              value="${statSide[3]}"/></h4>
                </c:when>
                <c:when test="${stat.round == 15}">
                    <h4 class="battlepanel">Battalions gained experience level: <fmt:formatNumber type="number"
                                                                                                  maxFractionDigits="0"
                                                                                                  groupingUsed="true"
                                                                                                  value="${statSide[0]}"/><br/>
                        Battalions with max exp. level: <fmt:formatNumber type="number" maxFractionDigits="0"
                                                                          groupingUsed="true"
                                                                          value="${statSide[1]}"/></h4>
                </c:when>
                <c:when test="${stat.round == 16}">
                    <h4 class="battlepanel">Commanders killed/captured: <fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${statSide[2]}"/></h4>
                </c:when>
                <c:when test="${stat.round == 13}">
                    <h5 class="battlepanel">
                        <c:choose>
                            <c:when test="${winner == side}">Winner</c:when>
                            <c:when test="${winner == -1}">Undecided</c:when>
                            <c:otherwise>Defeated</c:otherwise>
                        </c:choose>
                    </h5>
                    <h4 class="battlepanel">Total Casualties: <fmt:formatNumber type="number"
                                                                                maxFractionDigits="0"
                                                                                groupingUsed="true"
                                                                                value="${statSide[1]}"/><br/>
                        Routed soldiers: <fmt:formatNumber type="number" maxFractionDigits="0"
                                                           groupingUsed="true"
                                                           value="${statSide[2]}"/></h4>
                </c:when>
                <c:when test="${stat.round == 9}">
                    <h4 class="battlepanel">Combat Points: <fmt:formatNumber type="number" maxFractionDigits="0"
                                                                             groupingUsed="true"
                                                                             value="${statSide[0]}"/><br/>
                        Casualties: <fmt:formatNumber type="number" maxFractionDigits="0"
                                                      groupingUsed="true"
                                                      value="${statSide[1]}"/><br/>
                        Infantry batallions attacked by enemy cavalry: <fmt:formatNumber type="number"
                                                                                         maxFractionDigits="0"
                                                                                         groupingUsed="true"
                                                                                         value="${statSide[2]}"/></h4>
                </c:when>
                <c:otherwise>
                    <h4 class="battlepanel">Combat Points: <fmt:formatNumber type="number" maxFractionDigits="0"
                                                                             groupingUsed="true"
                                                                             value="${statSide[0]}"/><br/>
                        Casualties: <fmt:formatNumber type="number" maxFractionDigits="0"
                                                      groupingUsed="true"
                                                      value="${statSide[1]}"/></h4>
                </c:otherwise>
            </c:choose>
        </td>
        <c:if test="${side == 0}">
            <td colspan=5 class="battlepanelright">&nbsp;</td>
        </c:if>
    </c:forEach>
</tr>
<tr class="battlepanel" height="1px">
    <td width="1%" height="1px" class="battlepanelinfo" nowrap>&nbsp;#&nbsp;Battalions&nbsp;</td>
    <td width="1%" height="1px" class="battlepanelinfo" nowrap>&nbsp;#&nbsp;Troops&nbsp;</td>
    <td width="1%" height="1px" class="battlepanelmiddle">&nbsp;</td>
    <td width="1%" height="1px" class="battlepanelinfo" nowrap>&nbsp;#&nbsp;Troops&nbsp;</td>
    <td width="1%" height="1px" class="battlepanelinfo" nowrap>&nbsp;#&nbsp;Battalions&nbsp;</td>
</tr>
<c:forEach items="${troopType}" var="index">
    <tr class="battlepanel" height="1px">
        <td class="battlepanelinfo" height="1px"><c:out value="${stat.getArmySizes()[0][index][0]}"/></td>
        <td class="battlepanelinfo" height="1px"><c:out value="${stat.getArmySizes()[0][index][1]}"/></td>
        <td class="battlepanelmiddle" height="1px">
            <c:if test="${index<4}">
                <img src='http://static.eaw1805.com/images/armies/dominant/${imgSrc[index]}'
                     title="<c:url value='${imgTitle[index]}'/>"
                     height=18
                     class="battlepanel"
                     border=0>
            </c:if>
            <c:if test="${index==4}">
                <b>Total</b>
            </c:if>
        </td>
        <td class="battlepanelinfo" height="1px"><c:out value="${stat.getArmySizes()[1][index][1]}"/></td>
        <td class="battlepanelinfo" height="1px"><c:out value="${stat.getArmySizes()[1][index][0]}"/></td>
    </tr>
</c:forEach>
<tr class="battlepanelempty">
    <td colspan=7 class="battlepanelempty">&nbsp;</td>
</tr>
<c:choose>
    <c:when test="${stat.round == 0}">
        <tr class="battlepanelarmy">
            <c:forEach items="${sideCnt}" var="side">
                <td class="battlepanelarmy">
                    <div style="margin-top: -110px;">
                        <c:forEach items="${counterStats[stat.round][side]}" var="partMap">
                            <c:forEach items="${partMap.value}" var="nationsMap">
                                <c:set var="curNation" value="${nationsMap.key}"/>
                                <c:forEach items="${nationsMap.value}" var="item">
                                    <c:set value="${item.value[3]}" var="battalions"/>
                                    <jsp:useBean id="battalions" class="java.util.ArrayList"/>
                                    <div class="image">
                                        <img alt=""
                                             tooltip="<%=getBattalionsCards(battalions, request, false)%>"
                                             src='http://static.eaw1805.com/images/armies/${curNation}/${item.key}.jpg'
                                             width="60"/>

                                        <div class="numBats">
                                            <p><fmt:formatNumber value="${item.value[0]}"
                                                                 maxFractionDigits="0"/></p>
                                        </div>
                                        <div class="avgExp">
                                            <p style="${expStyle}"><fmt:formatNumber value="${item.value[1]}"
                                                                                     maxFractionDigits="1"
                                                                                     minFractionDigits="1"/></p>
                                        </div>
                                        <c:if test="${hasSideAllies[side]}">
                                            <div class="nationFlag">
                                                <img src="http://static.eaw1805.com/images/nations/nation-${curNation}-36.png"
                                                     alt="nation flag">
                                            </div>
                                        </c:if>
                                        <div class="headcount">
                                            <p style="${headcountStyle}"><fmt:formatNumber value="${item.value[2]}"
                                                                                           maxFractionDigits="0"/></p>
                                        </div>

                                    </div>
                                </c:forEach>
                            </c:forEach>
                        </c:forEach>
                    </div>
                </td>
                <c:if test="${side == 0}">
                    <td class="battlepanelempty" colspan="5">&nbsp;</td>
                </c:if>
            </c:forEach>
        </tr>
    </c:when>
    <c:when test="${stat.round == 1 || stat.round == 2 || stat.round == 4 || stat.round == 5 || stat.round == 7 || stat.round == 9 || stat.round == 11 || stat.round == 12 || stat.round == 14 || stat.round == 15}">
        <tr class="battlepanelarmy">
            <c:forEach items="${sideCnt}" var="side">
                <td class="battlepanelarmy">
                    <div style="margin-top: -40px;">
                        <c:set value="0" var="unitCnt"/>
                        <c:forEach items="${counterStats[stat.round][side][true]}" var="nationsMap">
                            <c:forEach items="${nationsMap.value}" var="item">
                                <c:set value="${item.value[3]}" var="battalions"/>
                                <jsp:useBean id="battalions" class="java.util.ArrayList"/>
                                <div class="image">
                                    <img alt=""
                                         tooltip="<%=getBattalionsCards(battalions, request, false)%>"
                                         src='http://static.eaw1805.com/images/armies/${nationsMap.key}/${item.key}.jpg'
                                         width="60"/>

                                    <div class="numBats">
                                        <p><fmt:formatNumber value="${item.value[0]}"
                                                             maxFractionDigits="0"/></p>
                                    </div>
                                    <div class="avgExp">
                                        <p style="${expStyle}"><fmt:formatNumber value="${item.value[1]}"
                                                                                 maxFractionDigits="1"
                                                                                 minFractionDigits="1"/></p>
                                    </div>
                                    <c:if test="${hasSideAllies[side]}">
                                        <div class="nationFlag">
                                            <img src="http://static.eaw1805.com/images/nations/nation-${nationsMap.key}-36.png"
                                                 alt="nation flag">
                                        </div>
                                    </c:if>
                                    <div class="headcount">
                                        <p style="${headcountStyle}"><fmt:formatNumber value="${item.value[2]}"
                                                                                       maxFractionDigits="0"/></p>
                                    </div>
                                </div>
                            </c:forEach>

                        </c:forEach>
                    </div>
                </td>
                <c:if test="${side == 0}">
                    <td class="battlepanelempty" colspan="5">&nbsp;</td>
                </c:if>
            </c:forEach>
        </tr>
    </c:when>
    <c:when test="${stat.round == 3 || stat.round == 6 || stat.round == 8 || stat.round == 10}">
        <tr class="battlepanelarmy">
            <c:forEach items="${sideCnt}" var="side">
                <td class="battlepanelarmy">
                    <div style="margin-top: -35px;">
                        <c:set value="0" var="unitCnt"/>
                        <c:forEach items="${counterStats[stat.round][side][true]}" var="nationsMap">
                            <c:forEach items="${nationsMap.value}" var="item">
                                <c:set value="${item.value[3]}" var="battalions"/>
                                <jsp:useBean id="battalions" class="java.util.ArrayList"/>
                                <div class="image">
                                    <img alt=""
                                         tooltip="<%=getBattalionsCards(battalions, request, true)%>"
                                         src='http://static.eaw1805.com/images/armies/${nationsMap.key}/${item.key}.jpg'
                                         width="60"/>

                                    <div class="numBats">
                                        <p><fmt:formatNumber value="${item.value[0]}"
                                                             maxFractionDigits="0"/></p>
                                    </div>
                                    <div class="avgExp">
                                        <p style="${expStyle}"><fmt:formatNumber value="${item.value[1]}"
                                                                                 maxFractionDigits="1"
                                                                                 minFractionDigits="1"/></p>
                                    </div>
                                    <c:if test="${hasSideAllies[side]}">
                                        <div class="nationFlag">
                                            <img src="http://static.eaw1805.com/images/nations/nation-${nationsMap.key}-36.png"
                                                 alt="nation flag">
                                        </div>
                                    </c:if>
                                    <div class="headcount">
                                        <p style="${headcountStyle}"><fmt:formatNumber value="${item.value[2]}"
                                                                                       maxFractionDigits="0"/></p>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:forEach>
                    </div>
                </td>
                <c:if test="${side == 0}">
                    <td class="battlepanelempty" colspan="5">&nbsp;</td>
                </c:if>
            </c:forEach>
        </tr>
    </c:when>
    <c:when test="${stat.round == 16}">
        <!-- @todo report dead commanders -->
    </c:when>
</c:choose>
</tbody>
</c:forEach>
</tbody>
</table>
