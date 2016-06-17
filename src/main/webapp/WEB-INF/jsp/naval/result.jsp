<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="gameId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="weather" scope="request" type="java.lang.String"/>
<jsp:useBean id="region" scope="request" class="com.eaw1805.data.model.map.Region"/>
<jsp:useBean id="sector" scope="request" class="com.eaw1805.data.model.map.Sector"/>
<jsp:useBean id="nation1" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="nation2" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="winner" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="stats" scope="request" type="java.util.List<empire.battles.naval.result.RoundStat>"/>
<%@ include file="/WEB-INF/jsp/cards/shipDTOCard.jsp" %>
<%
    int[] sideCnt = {0, 1};
    pageContext.setAttribute("sideCnt", sideCnt, PageContext.REQUEST_SCOPE);

    int[] shipType = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    pageContext.setAttribute("shipType", shipType, PageContext.REQUEST_SCOPE);

    String[] roundTitle = {"Maneuvering of Fleets",
            "Round 1: Long-Range fire of Ships-of-the-Line (50% effectiveness)",
            "Round 2: Long-Range fire of all warships (75% effectiveness)",
            "Round 3: Long-Range fire of all warships (100% effectiveness)",
            "Round 4: Hand-to-Hand combat of the boarding ships",
            "Round 5: Hand-to-Hand combat of the boarding ships",
            "Round 6: Disengagement of ships",
            "Determination of Winner",
            "Round 7: Capturing of merchant ships",
            "Aftermath"};
    pageContext.setAttribute("roundTitle", roundTitle, PageContext.REQUEST_SCOPE);

    String[] imgTitle = {"M", "1", "2", "3", "4", "5", "Total", "Cap", "Lost"};
    pageContext.setAttribute("imgTitle", imgTitle, PageContext.REQUEST_SCOPE);

    String[] spanTitle = {"battlepanelleft", "battlepanelright"};
    pageContext.setAttribute("spanTitle", spanTitle, PageContext.REQUEST_SCOPE);

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
<%}%>
<br/>
<br/>

<h5 class="battlepanel" style="color: black; text-align: center; font-size: 28px !important; text-shadow: 1.5px 1.5px 1px #7b714b;
font-weight: normal; letter-spacing: -1px; padding-top: 0px; margin-top: -30px; margin-bottom: -5px;">${battleDate}</h5>
<br>
<h1 class="battlepanel">
    <img src="http://static.eaw1805.com/images/battles/navalpanel.png"
         alt="Battle Panel">
    <img class="nationA"
        <% if (request.getParameter("fixForClient") != null) { %>
         style="left:-331px; margin-bottom: -75px"
        <%} else {%>
         style="margin-bottom: -70px"
        <%}%>
         src="http://static.eaw1805.com/images/nations/nation-${nation1.id}-120.png"
         alt="${nation1.name}"
         title="${nation1.name}">
    <img class="nationB"
        <% if (request.getParameter("fixForClient") != null) { %>
         style="left:352px; margin-bottom: -75px"
        <%} else {%>
         style="left:715px; margin-bottom: -70px"
        <%}%>
         src="http://static.eaw1805.com/images/nations/nation-${nation2.id}-120.png"
         alt="${nation2.name}"
         title="${nation2.name}">
</h1>

<h1 class="battlesidenameA"
        <% if (request.getParameter("fixForClient") != null) { %>
    style="margin-top: 8px; "
        <%}%>
        >${nation1.name}</h1>

<h1 class="battlesidenameB"
        <% if (request.getParameter("fixForClient") != null) { %>
    style="margin-top: 8px; margin-left: 8px; "
        <%}%>
        >${nation2.name}</h1>

<table class="reportAlliedFlags" width="100%">
    <tr>
        <td>
            <div class="side1Flags"
                 style="padding-top: 10px; padding-left: 10px;">
                <c:forEach items="${allies1}" var="ally">
                    <img src="http://static.eaw1805.com/images/nations/nation-${ally.id}-120.png" style="width:60px"/>
                </c:forEach>
            </div>
        </td>
        <td>
            <div class="sideSeperator"></div>
        </td>
        <td>
            <div class="side2Flags" align="right" style="margin-top: 10px; margin-right: 10px;">
                <c:forEach items="${allies2}" var="ally">
                    <img src="http://static.eaw1805.com/images/nations/nation-${ally.id}-120.png" style="width:60px"/>
                </c:forEach>
            </div>
        </td>
    </tr>
</table>

<h2 class="battlepanel">
    <table class="battleinfo" style="top: 50px;">
        <tr valign="bottom">
            <td>
                Region: ${region.name}<br/>
                Location: ${sector.position.toString()}<br/>
                Weather: ${weather}
            </td>
        </tr>
    </table>
</h2>
<h3 class="battleresult"
        <% if (request.getParameter("fixForClient") != null) { %>
    style="margin-top: 90px; margin-bottom: -70px;"
        <% } else { %>
    style="margin-top: 50px; margin-botto2: -10px;"
        <%}%>
        >
    <c:choose>
        <c:when test="${winner == 1}">${nation1.name} wins the naval battle at ${sectorsName}</c:when>
        <c:when test="${winner == 0}">The naval battle at ${sectorsName} was undecided</c:when>
        <c:otherwise>${nation2.name} wins the naval battle at ${sectorsName}</c:otherwise>
    </c:choose>
</h3>
<table width="100%" class="battlepanel">
<c:forEach items="${stats}" var="stat">
<tr class="battlepanel">
    <th width="100%" colspan=9 class="battlepanel"><c:out value="${roundTitle[stat.round]}"/></th>
</tr>
<tbody id="${stat.round}">
<tr class="battlepanel">
<c:forEach items="${sideCnt}" var="side">
<td class="<c:out value="${spanTitle[side]}"/>" rowspan=9>
<c:choose>
<c:when test="${stat.round == 0 || stat.round == 9}">

    <c:forEach items="${sideShipCounters[stat.round][side]}" var="nationsMap">
        <c:forEach items="${nationsMap.value}" var="item">
            <c:set value="${item.value[3]}" var="ships"/>
            <jsp:useBean id="ships" class="java.util.ArrayList"/>
            <div class="image">
                <img alt=""
                     tooltip="<%=getShipsCards(ships, request)%>"
                     src='http://static.eaw1805.com/images/ships/${nationsMap.key}/${item.key}.png'
                     width="60"/>

                <div class="numBats">
                    <p><fmt:formatNumber value="${item.value[0]}"
                                         maxFractionDigits="0"/></p>
                </div>
                <div class="avgExp">
                    <p style="${expStyle}"><fmt:formatNumber value="${item.value[1]}"
                                                             maxFractionDigits="0"/></p>
                </div>
                <div class="headcount">
                    <p style="${headcountStyle}"><fmt:formatNumber value="${item.value[2]}"
                                                                   maxFractionDigits="0"/></p>
                </div>
            </div>
        </c:forEach>
    </c:forEach>
</c:when>
<c:when test="${stat.round == 7}">
    <h5 class="battlepanel">
        <c:choose>
            <c:when test="${winner == side + 1}">Winner</c:when>
            <c:when test="${winner == 0}">Undecided</c:when>
            <c:otherwise>Defeated</c:otherwise>
        </c:choose>
    </h5>
    <h6 class="battlepanel">
        <c:forEach items="${stat.capturedShips[side]}" var="ship">
            <jsp:useBean id="ship" class="com.eaw1805.data.dto.web.fleet.ShipDTO"/>
            <img src='http://static.eaw1805.com/images/ships/${ship.nationId}/${ship.type.intId}.png'
                 title="<%=getShipCard(ship, request)%>"
                 border=0
                 class="battlepanel"/>
        </c:forEach>
    </h6>
</c:when>
<c:when test="${stat.round == 8 && stat.result != 0}">
    <c:forEach items="${stat.capturedShips[side]}" var="ship">
        <jsp:useBean id="ship" class="com.eaw1805.data.dto.web.fleet.ShipDTO"/>
        <img src='http://static.eaw1805.com/images/ships/${ship.nationId}/${ship.type.intId}.png'
             title="<%=getShipCard(ship, request)%>"
             border=0
             class="battlepanel"/>
    </c:forEach>
</c:when>
<c:when test="${stat.round == 1 || stat.round == 2 || stat.round == 3}">
    <c:forEach items="${stat.sidePairs}" var="shipPair">
        <c:choose>
            <c:when test="${side == 0}">
                <c:set var="ship" value="${shipPair.shipA}"/>
                <h6 class="battlepanel">
                    <jsp:useBean id="ship" class="com.eaw1805.data.dto.web.fleet.ShipDTO"/>
                    <img src='http://static.eaw1805.com/images/ships/${ship.nationId}/${ship.type.intId}.png'
                         title="<%=getShipCard(ship, request)%>"
                         border=0
                         class="shipPairLeft"/>
                    <c:if test="${shipPair.combatPoints[side]>0}">
                        Attack <fmt:formatNumber type="number"
                                                 maxFractionDigits="0"
                                                 groupingUsed="true"
                                                 value="${shipPair.combatPoints[side]}"/>
                    </c:if>
                    <c:if test="${shipPair.combatPoints[side]>0 && shipPair.lossTonnage[side] + shipPair.lossMarines[side]>0}">
                        &nbsp;/&nbsp;
                    </c:if>
                    <c:if test="${shipPair.lossTonnage[side] + shipPair.lossMarines[side]>0}">
                        <em>Lost:</em>
                        <fmt:formatNumber type="number" maxFractionDigits="0"
                                          groupingUsed="true"
                                          value="${shipPair.lossTonnage[side]}"/>&nbsp;Tonnage,&nbsp;
                        <fmt:formatNumber type="number" maxFractionDigits="0"
                                          groupingUsed="true"
                                          value="${shipPair.lossMarines[side]}"/>&nbsp;Marines
                    </c:if>
                </h6>
            </c:when>
            <c:when test="${side == 1}">
                <c:set var="ship" value="${shipPair.shipB}"/>
                <h6 class="battlepanel">
                    <jsp:useBean id="ship" class="com.eaw1805.data.dto.web.fleet.ShipDTO"/>
                    <c:if test="${shipPair.combatPoints[side]>0}">
                        Attack <fmt:formatNumber type="number"
                                                 maxFractionDigits="0"
                                                 groupingUsed="true"
                                                 value="${shipPair.combatPoints[side]}"/>
                    </c:if>
                    <c:if test="${shipPair.combatPoints[side]>0 && shipPair.lossTonnage[side] + shipPair.lossMarines[side]>0}">
                        &nbsp;/&nbsp;
                    </c:if>
                    <c:if test="${shipPair.lossTonnage[side] + shipPair.lossMarines[side]>0}">
                        <em>Lost:</em>
                        <fmt:formatNumber type="number" maxFractionDigits="0"
                                          groupingUsed="true"
                                          value="${shipPair.lossTonnage[side]}"/>&nbsp;Tonnage,&nbsp;
                        <fmt:formatNumber type="number" maxFractionDigits="0"
                                          groupingUsed="true"
                                          value="${shipPair.lossMarines[side]}"/>&nbsp;Marines
                    </c:if>
                    <img src='http://static.eaw1805.com/images/ships/${ship.nationId}/${ship.type.intId}.png'
                         title="<%=getShipCard(ship, request)%>"
                         border=0
                         class="shipPairRight"/>
                </h6>
            </c:when>
        </c:choose>
    </c:forEach>
</c:when>
<c:when test="${stat.round == 4 || stat.round == 5}">
    <c:forEach items="${stat.sidePairs}" var="shipPair">
        <c:choose>
            <c:when test="${side == 0}">
                <c:set var="ship" value="${shipPair.shipA}"/>
                <h7 class="battlepanel">
                    <jsp:useBean id="ship" class="com.eaw1805.data.dto.web.fleet.ShipDTO"/>
                    <img src='http://static.eaw1805.com/images/ships/${ship.nationId}/${ship.type.intId}.png'
                         title="<%=getShipCard(ship, request)%>"
                         border=0
                         class="shipPairLeft"/>
                    <em>ROUND 1</em>&nbsp;&nbsp;
                    Attack <fmt:formatNumber type="number"
                                             maxFractionDigits="0"
                                             groupingUsed="true"
                                             value="${shipPair.combatPoints[side]}"/>
                    &nbsp;/&nbsp;<em>Lost</em>&nbsp;
                    <fmt:formatNumber type="number" maxFractionDigits="0"
                                      groupingUsed="true"
                                      value="${shipPair.lossMarines[side]}"/>&nbsp;Marines<br>
                    <c:choose>
                        <c:when test="${shipPair.captured1[side] == true}">
                            <em class="resultBad">Captured by enemy</em>
                            <br>
                            <br>
                            <br>
                        </c:when>
                        <c:when test="${shipPair.captured1[(side + 1) % 2] == true}">
                            <em class="resultGood">Captured enemy ship</em>
                            <br>
                            <br>
                            <br>
                        </c:when>
                        <c:otherwise>
                            <em>ROUND 2</em>&nbsp;&nbsp;
                            Attack <fmt:formatNumber type="number"
                                                     maxFractionDigits="0"
                                                     groupingUsed="true"
                                                     value="${shipPair.combatPoints2[side]}"/>
                            &nbsp;/&nbsp;<em>Lost</em>&nbsp;
                            <fmt:formatNumber type="number" maxFractionDigits="0"
                                              groupingUsed="true"
                                              value="${shipPair.lossMarines2[side]}"/>&nbsp;Marines<br>
                            <c:choose>
                                <c:when test="${shipPair.captured2[side] == true}">
                                    <em class="resultBad">Captured by enemy</em>
                                    <br>
                                    <br>
                                </c:when>
                                <c:when test="${shipPair.captured2[(side + 1) % 2] == true}">
                                    <em class="resultGood">Captured enemy ship</em>
                                    <br>
                                    <br>
                                </c:when>
                                <c:otherwise>
                                    <em>ROUND 3</em>&nbsp;&nbsp;
                                    Attack <fmt:formatNumber type="number"
                                                             maxFractionDigits="0"
                                                             groupingUsed="true"
                                                             value="${shipPair.combatPoints3[side]}"/>
                                    &nbsp;/&nbsp;<em>Lost</em>&nbsp;
                                    <fmt:formatNumber type="number" maxFractionDigits="0"
                                                      groupingUsed="true"
                                                      value="${shipPair.lossMarines3[side]}"/>&nbsp;Marines<br>
                                    <c:choose>
                                        <c:when test="${shipPair.captured3[side] == true}">
                                            <em class="resultBad" style="padding-left: 42px;">Captured by enemy</em><br>
                                        </c:when>
                                        <c:when test="${shipPair.captured3[(side + 1) % 2] == true}">
                                            <em class="resultGood" style="padding-left: 42px;">Captured enemy
                                                ship</em><br>
                                        </c:when>
                                        <c:otherwise><br></c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </h7>
            </c:when>
            <c:when test="${side == 1}">
                <c:set var="ship" value="${shipPair.shipB}"/>
                <h7 class="battlepanel">
                    <jsp:useBean id="ship" class="com.eaw1805.data.dto.web.fleet.ShipDTO"/>
                    <img src='http://static.eaw1805.com/images/ships/${ship.nationId}/${ship.type.intId}.png'
                         title="<%=getShipCard(ship, request)%>"
                         border=0
                         class="shipPairRight"/>
                    <em>ROUND 1</em>&nbsp;&nbsp;
                    Attack <fmt:formatNumber type="number"
                                             maxFractionDigits="0"
                                             groupingUsed="true"
                                             value="${shipPair.combatPoints[side]}"/>
                    &nbsp;/&nbsp;<em>Lost</em>&nbsp;
                    <fmt:formatNumber type="number" maxFractionDigits="0"
                                      groupingUsed="true"
                                      value="${shipPair.lossMarines[side]}"/>&nbsp;Marines<br>
                    <c:choose>
                        <c:when test="${shipPair.captured1[side] == true}">
                            <em class="resultBad">Captured by enemy</em>
                            <br>
                            <br>
                            <br>
                        </c:when>
                        <c:when test="${shipPair.captured1[(side + 1) % 2] == true}">
                            <em class="resultGood">Captured enemy ship</em>
                            <br>
                            <br>
                            <br>
                        </c:when>
                        <c:otherwise>
                            <em>ROUND 2</em>&nbsp;&nbsp;
                            Attack <fmt:formatNumber type="number"
                                                     maxFractionDigits="0"
                                                     groupingUsed="true"
                                                     value="${shipPair.combatPoints2[side]}"/>
                            &nbsp;/&nbsp;<em>Lost</em>&nbsp;
                            <fmt:formatNumber type="number" maxFractionDigits="0"
                                              groupingUsed="true"
                                              value="${shipPair.lossMarines2[side]}"/>&nbsp;Marines<br>
                            <c:choose>
                                <c:when test="${shipPair.captured2[side] == true}">
                                    <em class="resultBad">Captured by enemy</em>
                                    <br>
                                    <br>
                                </c:when>
                                <c:when test="${shipPair.captured2[(side + 1) % 2] == true}">
                                    <em class="resultGood">Captured enemy ship</em>
                                    <br>
                                    <br>
                                </c:when>
                                <c:otherwise>
                                    <em>ROUND 3</em>&nbsp;&nbsp;
                                    Attack <fmt:formatNumber type="number"
                                                             maxFractionDigits="0"
                                                             groupingUsed="true"
                                                             value="${shipPair.combatPoints3[side]}"/>
                                    &nbsp;/&nbsp;<em>Lost</em>&nbsp;
                                    <fmt:formatNumber type="number" maxFractionDigits="0"
                                                      groupingUsed="true"
                                                      value="${shipPair.lossMarines3[side]}"/>&nbsp;Marines<br>
                                    <c:choose>
                                        <c:when test="${shipPair.captured3[side] == true}">
                                            <em class="resultBad" style="padding-right: 42px;">Captured by
                                                enemy</em><br>
                                        </c:when>
                                        <c:when test="${shipPair.captured3[(side + 1) % 2] == true}">
                                            <em class="resultGood" style="padding-right: 42px;">Captured enemy ship</em><br>
                                        </c:when>
                                        <c:otherwise>
                                            <br>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </h7>
            </c:when>
        </c:choose>
    </c:forEach>
</c:when>
</c:choose>
</td>
<c:if test="${side == 0}">
    <td colspan=7 class="battlepanelcenter">&nbsp;</td>
</c:if>
</c:forEach>
</tr>
<c:if test="${stat.round != 6}">
    <tr class="battlepanel">
        <td colspan=7 class="battlepanelcenter">
            <table>
                <tr class="battlepanel">
                    <td width="1%" class="battlepanelinfo" nowrap>&nbsp;Ships&nbsp;</td>
                    <td width="1%" class="battlepanelinfo" nowrap>&nbsp;Tonnage&nbsp;</td>
                    <td width="1%" class="battlepanelinfo" nowrap>&nbsp;Marines&nbsp;</td>
                    <td width="1%" class="battlepanelmiddle">&nbsp;</td>
                    <td width="1%" class="battlepanelinfo" nowrap>&nbsp;Marines&nbsp;</td>
                    <td width="1%" class="battlepanelinfo" nowrap>&nbsp;Tonnage&nbsp;</td>
                    <td width="1%" class="battlepanelinfo" nowrap>&nbsp;Ships&nbsp;</td>
                </tr>
                <c:forEach items="${shipType}" var="index">
                    <tr class="battlepanel">
                        <td class="battlepanelinfo">
                            <c:if test="${index==6}"><b></c:if>
                            <c:out value="${stat.sideStat[0][index].ships}"/>
                            <c:if test="${index==6}"></b></c:if></td>
                        <td class="battlepanelinfo">
                            <c:if test="${index==6}"><b></c:if>
                            <c:out value="${stat.sideStat[0][index].tonnage}"/>
                            <c:if test="${index==6}"></b></c:if></td>
                        <td class="battlepanelinfo">
                            <c:if test="${index==6}"><b></c:if>
                            <c:out value="${stat.sideStat[0][index].marines}"/>
                            <c:if test="${index==6}"></b></c:if></td>
                        <td class="battlepanelmiddle"><c:url value="${imgTitle[index]}"/></td>
                        <td class="battlepanelinfo">
                            <c:if test="${index==6}"><b></c:if>
                            <c:out value="${stat.sideStat[1][index].marines}"/>
                            <c:if test="${index==6}"></b></c:if></td>
                        <td class="battlepanelinfo">
                            <c:if test="${index==6}"><b></c:if>
                            <c:out value="${stat.sideStat[1][index].tonnage}"/>
                            <c:if test="${index==6}"></b></c:if></td>
                        <td class="battlepanelinfo">
                            <c:if test="${index==6}"><b></c:if>
                            <c:out value="${stat.sideStat[1][index].ships}"/>
                            <c:if test="${index==6}"></b></c:if></td>
                    </tr>
                </c:forEach>
            </table>
        </td>
    </tr>
</c:if>
<c:if test="${stat.round == 6}">
    <tr class="battlepanelempty">
        <td colspan=7 class="battlepaneldisengagement">All ships are disenganged</td>
    </tr>
</c:if>
<tr class="battlepanelempty">
    <td colspan=7 class="battlepanelempty">&nbsp;</td>
</tr>
</tbody>
</c:forEach>
</tbody>
</table>
