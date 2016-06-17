<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="game" scope="request" class="com.eaw1805.data.model.Game"/>
<jsp:useBean id="gameId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="nation" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="nationId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="sectors" scope="request" type="java.util.List[]"/>
<jsp:useBean id="mapUnitsSector" scope="request"
             type="java.util.Map<com.eaw1805.data.model.map.Position, java.util.List[]>"/>
<jsp:useBean id="positionHeight" scope="request"
             type="java.util.Map<com.eaw1805.data.model.map.Position, java.lang.Integer>"/>
<jsp:useBean id="armies" scope="request" type="java.util.List<com.eaw1805.data.model.army.Army>"/>
<jsp:useBean id="armyToBats" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, java.lang.Object[]>>>"/>
<jsp:useBean id="armyNotSupplied" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Boolean>"/>
<jsp:useBean id="armyInfTroops" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="armyInfBattalions" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="armyCavTroops" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="armyCavBattalions" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="armyArtTroops" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="armyArtBattalions" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="armyEngTroops" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="armyEngBattalions" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="armyTotTroops" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="armyTotBattalions" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="armyTotBrigades" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="armyHeight" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="corps" scope="request" type="java.util.List<com.eaw1805.data.model.army.Corp>"/>
<jsp:useBean id="corpsToBats" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, java.lang.Object[]>>>"/>
<jsp:useBean id="corpsNotSupplied" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Boolean>"/>
<jsp:useBean id="corpsInfTroops" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="corpsInfBattalions" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="corpsCavTroops" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="corpsCavBattalions" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="corpsArtTroops" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="corpsArtBattalions" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="corpsEngTroops" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="corpsEngBattalions" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="corpsTotTroops" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="corpsTotBattalions" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="corpsBattTypes" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.Set<java.lang.Integer>>"/>
<jsp:useBean id="brigades" scope="request" type="java.util.List<com.eaw1805.data.model.army.Brigade>"/>
<jsp:useBean id="commanders" scope="request" type="java.util.List<com.eaw1805.data.model.army.Commander>"/>
<jsp:useBean id="freeCommanders" scope="request" type="java.util.List<com.eaw1805.data.model.army.Commander>"/>

<%@ include file="/WEB-INF/jsp/cards/battalionCard.jsp" %>
<%@ include file="/WEB-INF/jsp/cards/BrigadeCard.jsp" %>
<c:set var="expStyle" value="color:brown;font-size:13px;"/>
<c:set var="headcountStyle" value="color:cyan;font-size:13px;"/>
<script type="text/javascript">
    $(function () {
        $("#draggable").draggable();
    });
</script>
<c:set var="positionCounter" value="0"/>

<%-- List all regions --%>
<c:forEach items="${sectors}" var="thisRegion">
<%-- List all positions --%>
<c:forEach items="${thisRegion}" var="thisPosition">
<jsp:useBean id="thisPosition" type="com.eaw1805.data.model.map.Position"/>

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
    <h2 style="padding-left: 40px; padding-top: 15px;">${thisPosition}</h2>
</div>

<div id="article-<c:out value="${positionCounter}"/>"
style="position: relative;margin: 4px 0px 0px -35px;width: 1000px;padding: 0px 40px;
background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
<h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>
<c:set var="positionCounter" value="${positionCounter + 1}"/>
<% } %>

<article>
    <%-- List all unit types in this position --%>
<c:set var="unitType" value="0"/>
<c:forEach items="${mapUnitsSector[thisPosition]}" var="unitList">
<%-- List all units in this position --%>
<c:choose>

<c:when test="${unitType == 0}">
<c:forEach items="${unitList}" var="army">
<jsp:useBean id="army" type="com.eaw1805.data.model.army.Army"/>

<army style="height: ${armyHeight[army.armyId]}px;">
<h2 style="height: 30px; float:left; width: 900px;"><c:out value="${army.name}"/></h2>
<c:if test="${armyNotSupplied[army.armyId]}">
    <img src='http://static.eaw1805.com/images/buttons/OutOfSupply32.png'
         title="Not Supplied"
         alt="Not Supplied"
         style="float: right; margin-top: 2px !important; margin-right: 5px !important;"
         border=0 width=30>
</c:if>
<div style="float: left;">
    <c:if test="${army.commander != null}">
        <ul class="commpanel" style="float: left;">
            <h3>Led by ${army.commander.name}</h3>
            <h4 style="margin-top: -8px !important; margin-bottom: 0px !important;">${army.commander.rank.name}</h4>
            <img src='http://static.eaw1805.com/img/commanders/s${game.scenarioId}/${army.commander.nation.id}/${army.commander.intId}.jpg'
                 alt="${army.commander.name} Portrait" width="128">
            <h4>Strategy Skill: <c:out value="${army.commander.strc}"/></h4>
            <h4>Command Skill: <c:out value="${army.commander.comc}"/></h4>
            <h4><img src='http://static.eaw1805.com/images/goods/good-1.png'
                     title="Money"
                     alt="Money"
                     style="vertical-align: bottom;"
                     border=0 width=12>&nbsp;<fmt:formatNumber type="number" maxFractionDigits="0"
                                                               groupingUsed="true"
                                                               value="${army.commander.rank.salary}"/></h4>
        </ul>
    </c:if>
    <ul class="infopanel" style="clear: both; float: left;">
        <li>
            <table width="100%">
                <thead>
                <tr>
                    <td width="0">&nbsp;</td>
                    <td width="0" class="infopanel">#&nbsp;Batts&nbsp;</td>
                    <td class="infopanelTroops">&nbsp;&nbsp;#&nbsp;Troops</td>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="infopanel"><img border="0" height="15" class="toolTip" alt="Infantry Battalions"
                                               src='http://static.eaw1805.com/images/armies/dominant/infantry.png'
                                               style="float: right;"
                                               title="Infantry Battalions">&nbsp;</td>
                    <td class="infopanel"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                            groupingUsed="true"
                                                            value="${armyInfBattalions[army.armyId]}"/></td>
                    <td class="infopanelTroops"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                  groupingUsed="true"
                                                                  value="${armyInfTroops[army.armyId]}"/></td>
                </tr>
                <tr>
                    <td class="infopanel"><img border="0" height="15" class="toolTip" alt="Cavalry Battalions"
                                               src='http://static.eaw1805.com/images/armies/dominant/cavalry.png'
                                               style="float: right;"
                                               title="Cavalry Battalions">&nbsp;</td>
                    <td class="infopanel"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                            groupingUsed="true"
                                                            value="${armyCavBattalions[army.armyId]}"/></td>
                    <td class="infopanelTroops"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                  groupingUsed="true"
                                                                  value="${armyCavTroops[army.armyId]}"/></td>
                </tr>
                <tr>
                    <td class="infopanel"><img border="0" height="15" class="toolTip" alt="Artillery Battalions"
                                               src='http://static.eaw1805.com/images/armies/dominant/artillery.png'
                                               style="float: right;"
                                               title="Artillery Battalions">&nbsp;</td>
                    <td class="infopanel"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                            groupingUsed="true"
                                                            value="${armyArtBattalions[army.armyId]}"/></td>
                    <td class="infopanelTroops"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                  groupingUsed="true"
                                                                  value="${armyArtTroops[army.armyId]}"/></td>
                </tr>
                <tr>
                    <td class="infopanel"><img border="0" height="15" class="toolTip" alt="Engineers Battalions"
                                               src='http://static.eaw1805.com/images/armies/dominant/engineers.png'
                                               style="float: right;"
                                               title="Engineers Battalions">&nbsp;</td>
                    <td class="infopanel"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                            groupingUsed="true"
                                                            value="${armyEngBattalions[army.armyId]}"/></td>
                    <td class="infopanelTroops"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                  groupingUsed="true"
                                                                  value="${armyEngTroops[army.armyId]}"/></td>
                </tr>
                <tr>
                    <td class="infopanelTotals"><img border="0" height="15" class="toolTip" alt="Total Battalions"
                                                     src='http://static.eaw1805.com/images/buttons/icons/formations/battalion.png'
                                                     style="float: right;"
                                                     title="Total Battalions">&nbsp;</td>
                    <td class="infopanelTotals"
                        style="text-align: center;"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                      groupingUsed="true"
                                                                      value="${armyTotBattalions[army.armyId]}"/></td>
                    <td class="infopanelTotals"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                  groupingUsed="true"
                                                                  value="${armyTotTroops[army.armyId]}"/></td>
                </tr>
                <tr>
                    <td class="infopanelTotals"><img border="0" height="15" class="toolTip" alt="Total Brigades"
                                                     src='http://static.eaw1805.com/images/buttons/icons/formations/brigade.png'
                                                     style="float: right;"
                                                     title="Total Brigades">&nbsp;</td>
                    <td class="infopanelTotals"
                        style="text-align: center;"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                      groupingUsed="true"
                                                                      value="${armyTotBrigades[army.armyId]}"/></td>
                    <td class="infopanelTotals">&nbsp;</td>
                </tr>
                </tbody>
            </table>
        </li>
    </ul>
</div>

<div style="width: 790px; float: right;">
    <c:set var="totCorps" value="0"/>
    <c:forEach items="${army.corps}" var="corp">
        <c:set var="totCorps" value="${totCorps+1}"/>
        <jsp:useBean id="corp" type="com.eaw1805.data.model.army.Corp"/>
        <c:choose>
            <c:when test="${fn:length(corpsBattTypes[corp.corpId]) > 10 && corp.commander == null}">
                <c:set var="height" value="190"/>
                <c:set var="heightBattPanel" value="155"/>
            </c:when>
            <c:when test="${corp.commander != null}">
                <c:set var="height" value="200"/>
                <c:set var="heightBattPanel" value="155"/>
            </c:when>
            <c:otherwise>
                <c:set var="height" value="115"/>
                <c:set var="heightBattPanel" value="75"/>
            </c:otherwise>
        </c:choose>
        <corp style="height:${height}px; width: 770px;">
            <h2 style="width:355px; float:left;"><c:out value="${corp.name}"/></h2>
            <img src="http://static.eaw1805.com/images/buttons/icons/formations/brigade.png"
                 style="float: right; margin-top: 5px; margin-right: 5px !important;"
                 tooltip="<%=getBrigadesCards(corp.getBrigades(), request, 0)%>"
                    />
            <c:if test="${corp.commander != null}">
                <ul class="commpanel" style="height: 162px;">
                    <h3>Led by ${corp.commander.name}</h3>
                    <h4 style="margin-top: -8px !important; margin-bottom: 0px !important;">${corp.commander.rank.name}</h4>
                    <c:set var="imageId" value="${corp.commander.intId}"/>
                    <c:if test="${imageId > 10}">
                        <c:set var="imageId" value="0"/>
                    </c:if>
                    <img src='http://static.eaw1805.com/img/commanders/s${game.scenarioId}/${corp.commander.nation.id}/${imageId}.jpg'
                         alt="${corp.commander.name} Portrait" width="96">
                    <h4>Strategy: <c:out value="${corp.commander.strc}"/>&nbsp;&nbsp;&nbsp;&nbsp;Command:
                        <c:out
                                value="${corp.commander.comc}"/></h4>
                    <h4><img src='http://static.eaw1805.com/images/goods/good-1.png'
                             title="Money"
                             alt="Money"
                             style="vertical-align: bottom;"
                             border=0 width=12>&nbsp;<fmt:formatNumber type="number" maxFractionDigits="0"
                                                                       groupingUsed="true"
                                                                       value="${corp.commander.rank.salary}"/>
                    </h4>
                </ul>
            </c:if>
            <ul class="battpanel"
                    <c:choose>
                        <c:when test="${corp.commander == null}">
                            style="height: ${heightBattPanel}px; width: 788px; margin-right: -18px !important;"
                        </c:when>
                        <c:otherwise>
                            style="height: ${heightBattPanel}px; width: 633px; margin-right: -18px !important; margin-top: -165px !important;"
                        </c:otherwise>
                    </c:choose>
                    >
                <c:forEach items="${corpsToBats[corp.corpId]}" var="typesToBats">
                    <li>
                        <c:forEach items="${typesToBats.value}" var="item">
                            <c:set value="${item.value[3]}" var="battalions"/>
                            <jsp:useBean id="battalions" class="java.util.ArrayList"/>
                            <div class="image">
                                <img alt=""
                                     tooltip="<%=getBattalionsCards(battalions, request)%>"
                                     class="popupedImg"
                                     src='http://static.eaw1805.com/images/armies/${nationId}/${item.key}.jpg'
                                     width="72"/>

                                <div class="numBats">
                                    <p><fmt:formatNumber value="${item.value[0]}"
                                                         maxFractionDigits="0"/></p>
                                </div>
                                <div class="avgExp">
                                    <p style="${expStyle}"><fmt:formatNumber
                                            value="${item.value[1]}"
                                            maxFractionDigits="1"
                                            minFractionDigits="1"/></p>
                                </div>
                                <div class="headcount">
                                    <p style="${headcountStyle}"><fmt:formatNumber
                                            value="${item.value[2]}"
                                            maxFractionDigits="0"/></p>
                                </div>
                            </div>
                        </c:forEach>
                    </li>
                </c:forEach>
            </ul>
        </corp>
    </c:forEach>
</div>
</army>
</c:forEach>
</c:when>

<c:when test="${unitType == 1}">
    <c:forEach items="${unitList}" var="corp">
        <jsp:useBean id="corp" type="com.eaw1805.data.model.army.Corp"/>
        <corp>
            <h2 style="width:355px; float:left;"><c:out value="${corp.name}"/></h2>
            <img src="http://static.eaw1805.com/images/buttons/icons/formations/brigade.png"
                 style="float: right; margin-top: 5px; margin-right: 5px !important;"
                 tooltip="<%=getBrigadesCards(corp.getBrigades(), request, 0)%>"
                    />
            <c:if test="${corpsNotSupplied[corp.corpId]}">
                <img src='http://static.eaw1805.com/images/buttons/OutOfSupply32.png'
                     title="Not Supplied"
                     alt="Not Supplied"
                     style="float: right; margin-right: 10px !important;"
                     border=0 width=30>
            </c:if>
            <div style="float: left;">
                <c:if test="${corp.commander != null}">
                    <ul class="commpanel" style="float: left;">
                        <h3>Led by ${corp.commander.name}</h3>
                        <h4 style="margin-top: -8px !important; margin-bottom: 0px !important;">${corp.commander.rank.name}</h4>
                        <c:set var="imageId" value="${corp.commander.intId}"/>
                        <c:if test="${imageId > 10}">
                            <c:set var="imageId" value="0"/>
                        </c:if>
                        <img src='http://static.eaw1805.com/img/commanders/s${game.scenarioId}/${corp.commander.nation.id}/${imageId}.jpg'
                             alt="${corp.commander.name} Portrait" width="128">
                        <h4>Strategy Skill: <c:out value="${corp.commander.strc}"/></h4>
                        <h4>Command Skill: <c:out value="${corp.commander.comc}"/></h4>
                        <h4><img src='http://static.eaw1805.com/images/goods/good-1.png'
                                 title="Money"
                                 alt="Money"
                                 style="vertical-align: bottom;"
                                 border=0 width=12>&nbsp;<fmt:formatNumber type="number" maxFractionDigits="0"
                                                                           groupingUsed="true"
                                                                           value="${corp.commander.rank.salary}"/></h4>
                    </ul>
                </c:if>
                <ul class="infopanel" style="float: left; clear:both;">
                    <li>
                        <table width="100%">
                            <thead>
                            <tr>
                                <td width="0">&nbsp;</td>
                                <td width="0" class="infopanel">#&nbsp;Batts&nbsp;</td>
                                <td class="infopanelTroops">&nbsp;&nbsp;#&nbsp;Troops</td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td class="infopanel"><img border="0" height="15" class="toolTip"
                                                           alt="Infantry Battalions"
                                                           src='http://static.eaw1805.com/images/armies/dominant/infantry.png'
                                                           style="float: right;"
                                                           title="Infantry Battalions">&nbsp;</td>
                                <td class="infopanel"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${corpsInfBattalions[corp.corpId]}"/></td>
                                <td class="infopanelTroops"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                              groupingUsed="true"
                                                                              value="${corpsInfTroops[corp.corpId]}"/></td>
                            </tr>
                            <tr>
                                <td class="infopanel"><img border="0" height="15" class="toolTip"
                                                           alt="Cavalry Battalions"
                                                           src='http://static.eaw1805.com/images/armies/dominant/cavalry.png'
                                                           style="float: right;"
                                                           title="Cavalry Battalions">&nbsp;</td>
                                <td class="infopanel"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${corpsCavBattalions[corp.corpId]}"/></td>
                                <td class="infopanelTroops"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                              groupingUsed="true"
                                                                              value="${corpsCavTroops[corp.corpId]}"/></td>
                            </tr>
                            <tr>
                                <td class="infopanel"><img border="0" height="15" class="toolTip"
                                                           alt="Artillery Battalions"
                                                           src='http://static.eaw1805.com/images/armies/dominant/artillery.png'
                                                           style="float: right;"
                                                           title="Artillery Battalions">&nbsp;</td>
                                <td class="infopanel"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${corpsArtBattalions[corp.corpId]}"/></td>
                                <td class="infopanelTroops"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                              groupingUsed="true"
                                                                              value="${corpsArtTroops[corp.corpId]}"/></td>
                            </tr>
                            <tr>
                                <td class="infopanel"><img border="0" height="15" class="toolTip"
                                                           alt="Engineers Battalions"
                                                           src='http://static.eaw1805.com/images/armies/dominant/engineers.png'
                                                           style="float: right;"
                                                           title="Engineers Battalions">&nbsp;</td>
                                <td class="infopanel"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${corpsEngBattalions[corp.corpId]}"/></td>
                                <td class="infopanelTroops"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                              groupingUsed="true"
                                                                              value="${corpsEngTroops[corp.corpId]}"/></td>
                            </tr>
                            <tr>
                                <td class="infopanelTotals"><img border="0" height="15" class="toolTip"
                                                                 alt="Total Battalions"
                                                                 src='http://static.eaw1805.com/images/buttons/icons/formations/battalion.png'
                                                                 style="float: right;"
                                                                 title="Total Battalions">&nbsp;</td>
                                <td class="infopanelTotals"
                                    style="text-align: center;"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                                  groupingUsed="true"
                                                                                  value="${corpsTotBattalions[corp.corpId]}"/></td>
                                <td class="infopanelTotals"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                              groupingUsed="true"
                                                                              value="${corpsTotTroops[corp.corpId]}"/></td>
                            </tr>
                            <tr>
                                <td class="infopanelTotals"><img border="0" height="15" class="toolTip"
                                                                 alt="Total Brigades"
                                                                 src='http://static.eaw1805.com/images/buttons/icons/formations/brigade.png'
                                                                 style="float: right;"
                                                                 title="Total Brigades">&nbsp;</td>
                                <td class="infopanelTotals"
                                    style="text-align: center;"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                                  groupingUsed="true"
                                                                                  value="${fn:length(corp.brigades)}"/></td>
                                <td class="infopanelTotals">&nbsp;</td>
                            </tr>
                            </tbody>
                        </table>
                    </li>
                </ul>
            </div>
            <ul class="battpanel" style="float: right; margin-right: -15px !important;">
                <c:forEach items="${corpsToBats[corp.corpId]}" var="typesToBats">
                    <li>
                        <c:forEach items="${typesToBats.value}" var="item">
                            <c:set value="${item.value[3]}" var="battalions"/>
                            <jsp:useBean id="battalions" class="java.util.ArrayList"/>
                            <div class="image">
                                <img alt=""
                                     tooltip="<%=getBattalionsCards(battalions, request)%>"
                                     class="popupedImg"
                                     src='http://static.eaw1805.com/images/armies/${nationId}/${item.key}.jpg'
                                     width="72"/>

                                <div class="numBats">
                                    <p><fmt:formatNumber value="${item.value[0]}"
                                                         maxFractionDigits="0"/></p>
                                </div>
                                <div class="avgExp">
                                    <p style="${expStyle}"><fmt:formatNumber
                                            value="${item.value[1]}"
                                            maxFractionDigits="1"
                                            minFractionDigits="1"/></p>
                                </div>
                                <div class="headcount">
                                    <p style="${headcountStyle}"><fmt:formatNumber
                                            value="${item.value[2]}"
                                            maxFractionDigits="0"/></p>
                                </div>
                            </div>
                        </c:forEach>
                    </li>
                </c:forEach>
            </ul>
        </corp>
    </c:forEach>
</c:when>

<c:when test="${unitType == 2}">
    <c:forEach items="${unitList}" var="brigade">
        <jsp:useBean id="brigade" type="com.eaw1805.data.model.army.Brigade"/>
        <brigade>
            <h2 style="height: 33px; width:385px; float:left;"><c:out value="${brigade.name}"/></h2>
            <c:if test="${notSupplied > 0}">
                <img src='http://static.eaw1805.com/images/buttons/OutOfSupply32.png'
                     title="Not Supplied"
                     alt="Not Supplied"
                     style="float: right; margin-top: 3px !important; margin-right: 5px !important;"
                     border=0 width=30>
            </c:if>
            <c:if test="${loaded > 0}">
                <img src='http://static.eaw1805.com/images/buttons/icons/transport.png'
                     title="Loaded on Transport Unit"
                     alt="Loaded on Transport Unit"
                     style="float: right; margin-top: 5px !important; margin-right: 5px !important;"
                     border=0 width=26>
            </c:if>
            <ul class="battpanel">
                <c:set var="totBattalions" value="0"/>
                <c:set var="notSupplied" value="0"/>
                <c:set var="loaded" value="0"/>
                <c:forEach items="${brigade.battalions}" var="battalion">
                    <jsp:useBean id="battalion" type="com.eaw1805.data.model.army.Battalion"/>
                    <c:set var="totBattalions" value="${totBattalions+1}"/>
                    <c:if test="${battalion.notSupplied}">
                        <c:set var="notSupplied" value="1"/>
                    </c:if>
                    <c:if test="${battalion.carrierInfo != null && battalion.carrierInfo.carrierId != 0}">
                        <c:set var="loaded" value="1"/>
                    </c:if>
                    <li>
                        <div class="image">
                            <img alt=""
                                 class="popupedImg"
                                 src='http://static.eaw1805.com/images/armies/${nationId}/${battalion.type.intId}.jpg'
                                 width="72"/>

                            <div class="avgExp">
                                <p style="${expStyle}"><fmt:formatNumber
                                        value="${battalion.experience}"
                                        maxFractionDigits="1"
                                        minFractionDigits="1"/></p>
                            </div>
                            <div class="headcount">
                                <p style="${headcountStyle}"><fmt:formatNumber
                                        value="${battalion.headcount}"
                                        maxFractionDigits="0"/></p>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </brigade>
    </c:forEach>
</c:when>

<c:when test="${unitType == 3}">
    <c:forEach items="${unitList}" var="commander">
        <jsp:useBean id="commander" type="com.eaw1805.data.model.army.Commander"/>
        <c:if test="${commander.dead == false}">
            <commander>
                <h2><c:out value="${commander.name}"/></h2>

                <h3><c:out value="${commander.rank.name}"/></h3>
                <c:set var="imageId" value="${commander.intId}"/>
                <c:if test="${imageId > 10}">
                    <c:set var="imageId" value="0"/>
                </c:if>
                <img src='http://static.eaw1805.com/img/commanders/s${game.scenarioId}/${commander.nation.id}/${imageId}.jpg'
                     alt="${commander.name} Portrait" width="160">
                <h4>Strategy Skill: <c:out value="${commander.strc}"/></h4>
                <h4>Command Skill: <c:out value="${commander.comc}"/></h4>
                <h4><img src='http://static.eaw1805.com/images/goods/good-1.png'
                         title="Money"
                         alt="Money"
                         style="vertical-align: bottom;"
                         border=0 width=16>&nbsp;<fmt:formatNumber type="number" maxFractionDigits="0"
                                                                   groupingUsed="true"
                                                                   value="${commander.rank.salary}"/></h4>
            </commander>
        </c:if>
    </c:forEach>
</c:when>

</c:choose>
<c:set var="unitType" value="${unitType+1}"/>
</c:forEach>
</article>

</c:forEach>
</c:forEach>

<script type="text/javascript">
    $(document).ready(function () {
        <c:forEach var="articleCounter" begin="0" end="${positionCounter-2}" step="1">
        $('#article-<c:out value="${articleCounter}"/>').css({ 'min-height': $('#article-<c:out value="${articleCounter+1}"/>').position().top - $('#article-<c:out value="${articleCounter}"/>').position().top - 116});
        </c:forEach>
        $('#article-<c:out value="${positionCounter-1}"/>').css({ 'min-height': $('#main').height() - $('#article-<c:out value="${positionCounter-1}"/>').position().top - 169});
    });
</script>
