<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="game" scope="request" class="com.eaw1805.data.model.Game"/>
<jsp:useBean id="gameId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="nation" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="nationId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="shipsWithOrders" scope="request" type="java.util.Set<java.lang.Integer>"/>
<jsp:useBean id="fleetToShowGoods" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Boolean>"/>
<jsp:useBean id="fleetToBrigades" scope="request" type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.army.Brigade>>"/>
<jsp:useBean id="fleetToCommanders" scope="request" type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.army.Commander>>"/>
<jsp:useBean id="fleetToSpies" scope="request" type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.army.Spy>>"/>
<jsp:useBean id="nameToNation" scope="request" type="java.util.Map<java.lang.String, com.eaw1805.data.model.Nation>"/>
<jsp:useBean id="shipToBrigades" scope="request" type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.army.Brigade>>"/>
<jsp:useBean id="shipToCommanders" scope="request" type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.army.Commander>>"/>
<jsp:useBean id="shipToSpies" scope="request" type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.army.Spy>>"/>

<jsp:useBean id="sortedFleets" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, com.eaw1805.data.model.fleet.Fleet>>"/>

<c:set var="expStyle" value="color:brown;font-size:13px;"/>
<c:set var="headcountStyle" value="color:cyan;font-size:13px;"/>
<c:set var="positionCounter" value="0"/>

<%@ include file="/WEB-INF/jsp/cards/shipCard.jsp" %>
<%@ include file="/WEB-INF/jsp/cards/BrigadeCard.jsp" %>
<%@ include file="/WEB-INF/jsp/cards/CommanderCard.jsp" %>
<%@ include file="/WEB-INF/jsp/cards/SpyCard.jsp" %>

<c:forEach items="${sortedFleets}" var="regionFleets">
<c:forEach items="${regionFleets.value}" var="coordFleets">

<% if (request.getParameter("fixForClient") == null) { %>
</div>

<div style="clear: both; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;">
    <c:if test="${positionCounter == 0}">
        <h2 style="padding-left: 60px;">${gameDate}</h2>
    </c:if>
</div>

<div style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 15px;">${coordFleets.key}</h2>
</div>

<div id="article-<c:out value="${positionCounter}"/>"
style="position: relative;margin: 4px 0px 0px -35px;width: 1000px;padding: 0px 40px; min-height: 400px;
background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
<h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>
<c:set var="positionCounter" value="${positionCounter + 1}"/>
<% } %>

<article>

    <c:forEach items="${coordFleets.value}" var="fleet">
        <jsp:useBean id="fleet" type="com.eaw1805.data.model.fleet.Fleet"/>
        <fleet>

            <h2><c:out value="${fleet.name}"/></h2>
            <ul class="shippanel"
                style="margin-right: -18px !important;">
                <c:forEach items="${fleetToShips[fleet.fleetId]}" var="typesToShips">
                    <li>
                        <c:forEach items="${typesToShips.value}" var="item">
                            <c:set value="${item.value[3]}" var="ships"/>
                            <jsp:useBean id="ships" type="java.util.ArrayList<com.eaw1805.data.model.fleet.Ship>"/>
                            <div class="image">
                                <img alt=""
                                     class="popupedImg"
                                     tooltip="<%=getShipsCards(ships, request, shipsWithOrders, 0)%>"
                                     src='http://static.eaw1805.com/images/ships/${nationId}/${item.key}.png'
                                     width="72"/>
                                <c:forEach items="${ships}" var="ship">
                                    <jsp:useBean id="ship" type="com.eaw1805.data.model.fleet.Ship"/>
                                    <div id="_ship_brigs_${ship.shipId}" style="display: none"><%=getBrigadesCards(shipToBrigades.get(ship.getShipId()), request, 1)%></div>
                                    <div id="_ship_spies_${ship.shipId}" style="display: none"><%=getSpiesCards(shipToSpies.get(ship.getShipId()), request, nameToNation, 1)%></div>
                                    <div id="_ship_comm_${ship.shipId}" style="display: none"><%=getCommandersCards(shipToCommanders.get(ship.getShipId()), request, 1)%></div>
                                </c:forEach>
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
                    </li>
                </c:forEach>
            </ul>

            <ul class="infopanel"
                style="width: 135px; float: left;">
                <li>
                    <table width="100%">
                        <thead>
                        <tr>
                            <td width="0">&nbsp;</td>
                            <td width="0" class="infopanel">#&nbsp;</td>
                            <td width="0" class="infopanelTroops">#&nbsp;Marines&nbsp;</td>
                            <td width="0" class="infopanelTroops">#&nbsp;Tonnage&nbsp;</td>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td class="infopanel">
                                <img border="0" height="16" class="toolTip" alt="Merchant ships"
                                     src='http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png'
                                     title="Merchant ships">&nbsp;
                            </td>
                            <td class="infopanel">
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${merchantShips[fleet.fleetId]}"/>

                            </td>
                            <td class="infopanelTroops">
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${marines[fleet.fleetId]['0']}"/>

                            </td>
                            <td class="infopanelTroops">
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${tonnage[fleet.fleetId]['0']}"/>

                            </td>
                        </tr>
                        <tr>
                            <td class="infopanel">
                                <img border="0" height="16" class="toolTip" alt="War ships"
                                     src='http://static.eaw1805.com/images/buttons/icons/formations/warship.png'
                                     title="War ships">&nbsp;
                            </td>
                            <td class="infopanel">
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${warShips[fleet.fleetId]}"/>
                            </td>
                            <td class="infopanelTroops">
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${marines[fleet.fleetId]['1']}"/>
                            </td>
                            <td class="infopanelTroops">
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${tonnage[fleet.fleetId]['1']}"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="infopanelTotals">Total&nbsp;</td>
                            <td class="infopanelTotals">
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${merchantShips[fleet.fleetId] + warShips[fleet.fleetId]}"/>
                            </td>
                            <td class="infopanelTotals">
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${marines[fleet.fleetId]['0'] + marines[fleet.fleetId]['1']}"/>
                            </td>
                            <td class="infopanelTotals">
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${tonnage[fleet.fleetId]['0'] + tonnage[fleet.fleetId]['1']}"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </li>
            </ul>
            <c:if test="${fleetToShowGoods[fleet.fleetId]}">
                <ul class="infopanel"
                    style="width: 135px; float: left;">
                    <li>
                        <table width="100%">
                            <c:forEach items="${fleetToGoods[fleet.fleetId]}" var="good">
                                <c:if test="${good.value > 0}">
                                    <tr>
                                        <td class="infopanel"><img
                                                src="http://static.eaw1805.com/images/goods/good-${good.key}.png"
                                                style="width:15px;height:15px"
                                                title="${good.value}"/></td>
                                        <td class="infopanelTotals">${good.value}</td>
                                    </tr>
                                </c:if>
                                <%--<c:if test="${good.value > 0}">--%>

                                <%--</c:if>--%>
                            </c:forEach>
                        </table>
                    </li>
                </ul>
            </c:if>
                <%--${fleetToLandUnits[fleet.fleetId]}--%>
            <c:if test="${fn:length(fleetToLandUnits[fleet.fleetId]) > 0 }">
                <ul class="infopanel" style="width: 135px; float: left;">
                    <li>
                        <table width="100%">
                            <c:forEach items="${fleetToLandUnits[fleet.fleetId]}" var="landUnit">
                                <tr>
                                    <c:choose>
                                        <%--for spies--%>
                                        <c:when test="${landUnit.key == 8}">
                                            <td class="infopanel"><img
                                                    src="http://static.eaw1805.com/images/layout/buttons/unitMenu/ButSpiesOff.png"
                                                    tooltip="<%=getSpiesCards(fleetToSpies.get(fleet.getFleetId()), request, nameToNation, 0)%>"
                                                    style="width:15px;height:15px"
                                                    title="Loaded spies : ${landUnit.value}"/></td>
                                        </c:when>
                                        <%--for commanders--%>
                                        <c:when test="${landUnit.key == 7}">
                                            <td class="infopanel"><img
                                                    src="http://static.eaw1805.com/images/layout/buttons/unitMenu/ButCommandersOff.png"
                                                    tooltip="<%=getCommandersCards(fleetToCommanders.get(fleet.getFleetId()), request, 0)%>"
                                                    style="width:15px;height:15px"
                                                    title="Loaded commanders : ${landUnit.value}"/></td>
                                        </c:when>
                                        <%--for brigades--%>
                                        <c:when test="${landUnit.key == 3}">
                                            <td class="infopanel"><img
                                                    src="http://static.eaw1805.com/images/layout/buttons/unitMenu/ButArmiesOff.png"
                                                    style="width:15px;height:15px"
                                                    tooltip="<%=getBrigadesCards(fleetToBrigades.get(fleet.getFleetId()), request, 0)%>"
                                                    title="Loaded brigades : ${landUnit.value}"/></td>
                                        </c:when>
                                    </c:choose>
                                    <td class="infopanelTotals">${landUnit.value}</td>
                                </tr>

                                <%--<c:if test="${good.value > 0}">--%>

                                <%--</c:if>--%>
                            </c:forEach>
                        </table>
                    </li>
                </ul>
            </c:if>
        </fleet>
    </c:forEach>
</article>
</c:forEach>
</c:forEach>

<script type="text/javascript">
    $(document).ready(function () {
        <c:forEach var="articleCounter" begin="0" end="${positionCounter-1}" step="1">
        $('#article-<c:out value="${articleCounter}"/>').css({ 'min-height':$('#article-<c:out value="${articleCounter+1}"/>').position().top - $('#article-<c:out value="${articleCounter}"/>').position().top - 116});
        </c:forEach>
        $('#article-<c:out value="${positionCounter}"/>').css({ 'min-height':$('#main').position().bottom - $('#article-<c:out value="${positionCounter}"/>').position().top - 96});
    });
</script>
