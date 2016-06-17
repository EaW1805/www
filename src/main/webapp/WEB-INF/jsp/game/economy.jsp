<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="nation" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="regionList" scope="request" type="java.util.List<com.eaw1805.data.model.map.Region>"/>
<jsp:useBean id="goodList" scope="request" type="java.util.List<com.eaw1805.data.model.economy.Good>"/>
<jsp:useBean id="siteList" scope="request" type="java.util.List"/>
<jsp:useBean id="warehouseList" scope="request" type="java.util.List"/>
<jsp:useBean id="turn" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="months" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
<jsp:useBean id="treasury" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="gameDate" scope="request" type="java.lang.String"/>

<c:forEach items="${regionList}" var="thisRegion">
<c:if test="${activity[thisRegion] == true}">
<c:choose>
    <c:when test="${thisRegion.id == 0}">
        <c:set var="columns" value="2"/>
        <c:set var="tableTile" value="National Treasury Balance Sheet"/>
        <c:set var="tableStyle" value="warehouse"/>

        <% if (request.getParameter("fixForClient") == null) { %>
        </div>

        <div style="z-index: 2; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;">
            <h2 style="padding-left: 60px;">${gameDate}</h2>
        </div>

        <div style="z-index: 2; position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
            <h2 style="padding-left: 40px; padding-top: 20px;">${tableTile}</h2>
        </div>

        <div style="z-index: 1; position: relative;margin: 4px 0px 0px -35px;width: 1000px;min-height: 200px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
        <% } %>

    </c:when>
    <c:otherwise>
        <c:set var="columns" value="15"/>
        <c:set var="tableTile" value="${thisRegion.name} Warehouse Balance Sheet"/>
        <c:set var="tableStyle" value="warehouse-activity"/>

        <% if (request.getParameter("fixForClient") == null) { %>
        </div>

        <div style="clear: both;position: relative; margin: -20px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78;"></div>

        <div style="z-index: 2; position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
            <h2 style="padding-left: 40px; padding-top: 20px;">${tableTile}</h2>
        </div>

        <div style="z-index: 1; position: relative;margin: 4px 0px 0px -35px;width: 1000px;min-height: 200px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">

        <% } %>
    </c:otherwise>
</c:choose>

<% if (request.getParameter("fixForClient") == null) { %>
<h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>
<% } %>

<table class="${tableStyle}"
        <% if (request.getParameter("fixForClient") == null) { %>
       style="background: none; border: none; border-style: hidden; margin-top: 0px; box-shadow: 0 0 0 0;"
        <% } %>
        >
<% if (request.getParameter("fixForClient") != null) { %>
<tr class="warehouse-activity">
    <th colspan="${columns}" class="warehouse-head">${tableTile}</th>
</tr>
<% } %>
<tr class="warehouse-activity">
    <th align="center" width="38" class="warehouse-activity">&nbsp;</th>
    <c:choose>
        <c:when test="${thisRegion.id == 0}">
            <th align="center" width="56" class="warehouse-activity"><img
                    src='http://static.eaw1805.com/images/goods/good-1.png'
                    class="toolTip"
                    title="Money"
                    alt="Money"
                    border=0 width=24></th>
        </c:when>
        <c:otherwise>
            <c:forEach items="${goodList}" var="good">
                <th align="center" width="56" class="warehouse-activity"><img
                        src='http://static.eaw1805.com/images/goods/good-${good.goodId}.png'
                        class="toolTip"
                        title="${good.name}"
                        alt="${good.name}"
                        border=0 width=24></th>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</tr>
<tr class="warehouse-activity">
    <td align="center" width="38" class="warehouse-activity">From&nbsp;previous&nbsp;Month</td>
    <c:forEach items="${initial[thisRegion]}" var="good">
        <td class="warehouse-activity"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                         groupingUsed="true"
                                                         value="${good}"/></td>
    </c:forEach>
</tr>
<tr class="warehouse-activity">
    <td colspan="${columns}" class="warehouse-sep">&nbsp;</td>
</tr>
<c:forEach items="${orders}" var="thisOrder">
    <c:if test="${thisOrder.result > 0}">
        <c:choose>
            <c:when test="${thisRegion.id == 0}">
                <c:if test="${thisOrder.usedGoodsQnt != null}">
                    <c:if test="${thisOrder.usedGoodsQnt[moneyGood] != null}">
                        <tr class="warehouse-details">
                            <td align="center" width="38"
                                class="warehouse-details">${thisOrder.temp9}</td>
                            <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                            groupingUsed="true"
                                                                            value="${thisOrder.usedGoodsQnt[moneyGood] * -1}"/></td>
                        </tr>
                    </c:if>
                </c:if>
            </c:when>
            <c:otherwise>
                <c:if test="${thisOrder.region.id == thisRegion.id}">
                    <c:if test="${thisOrder.usedGoodsQnt != null}">
                        <tr class="warehouse-details">
                            <td align="center" width="38"
                                class="warehouse-details">${thisOrder.temp9}</td>
                            <c:forEach items="${goodList}" var="good">
                                <td class="warehouse-details">
                                    <c:if test="${thisOrder.usedGoodsQnt[good.goodId] != null}">
                                        <fmt:formatNumber type="number" maxFractionDigits="0"
                                                          groupingUsed="true"
                                                          value="${thisOrder.usedGoodsQnt[good.goodId] * -1}"/></c:if></td>
                            </c:forEach>
                        </tr>
                    </c:if>
                </c:if>
            </c:otherwise>
        </c:choose>
    </c:if>
</c:forEach>
<c:choose>
    <c:when test="${thisRegion.id == 0}">
        <tr class="warehouse-activity">
            <td align="center" width="38" class="warehouse-activity">Order's&nbsp;sub-total</td>
            <td class="warehouse-activity"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                             groupingUsed="true"
                                                             value="${moneyConsumption * -1}"/></td>
        </tr>
    </c:when>
    <c:otherwise>
        <tr class="warehouse-activity">
            <td align="center" width="38" class="warehouse-activity">Order's&nbsp;sub-total</td>
            <c:forEach items="${goodList}" var="good">
                <td class="warehouse-activity"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                 groupingUsed="true"
                                                                 value="${goodConsumption[thisRegion][good.goodId] * -1}"/></td>
            </c:forEach>
        </tr>
    </c:otherwise>
</c:choose>
<c:choose>
<c:when test="${thisRegion.id == 0}">
    <tr class="warehouse-activity">
        <td colspan="${columns}" class="warehouse-sep">&nbsp;</td>
    </tr>
    <tr class="warehouse-activity">
        <td align="center" width="38" class="warehouse-activity">Mint&nbsp;Production</td>
        <c:forEach items="${production[thisRegion]}" var="good">
            <td class="warehouse-activity"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                             groupingUsed="true"
                                                             value="${good}"/></td>
        </c:forEach>
    </tr>
    <tr class="warehouse-activity">
        <td align="center" width="38" class="warehouse-activity">Transfers&nbsp;Received&nbsp;&amp;&nbsp;Transaction&nbsp;Fees</td>
        <c:forEach items="${transfers[thisRegion]}" var="good">
            <td class="warehouse-activity"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                             groupingUsed="true"
                                                             value="${good}"/></td>
        </c:forEach>
    </tr>
    <tr class="warehouse-activity">
        <td colspan="${columns}" class="warehouse-sep">&nbsp;</td>
    </tr>
    <tr class="warehouse-activity">
        <td align="center" width="38" class="warehouse-activity">Taxation</td>
        <td class="warehouse-activity"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                         groupingUsed="true"
                                                         value="${taxation.value}"/></td>
    </tr>
</c:when>
<c:otherwise>
    <tr class="warehouse-activity">
        <td colspan="${columns}" class="warehouse-sep">&nbsp;</td>
    </tr>
    <c:set var="factoryProduction" value="100"/>
    <c:if test="${thisRegion.id != 1}"><c:set var="factoryProduction" value="75"/></c:if>
    <c:if test="${game.boostedProduction == true}"><c:set var="factoryProduction" value="${factoryProduction * 1.25}"/></c:if>
    <c:forEach items="${productionDetails}" var="thisReport">
        <c:if test='${thisReport.key.indexOf(thisRegion.code) > 0}'>
            <c:choose>
                <c:when test='${thisReport.key.contains("estate")}'>
                    <tr class="warehouse-details">
                        <td align="center" width="38" class="warehouse-details">
                            Estate&nbsp;at&nbsp;${thisReport.key.substring(12+6)}</td>
                        <td colspan="2" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value}"/></td>
                        <td colspan="10" class="warehouse-details">&nbsp;</td>
                    </tr>
                </c:when>
                <c:when test='${thisReport.key.contains("factory")}'>
                    <tr class="warehouse-details">
                        <td align="center" width="38" class="warehouse-details">
                            Factory&nbsp;at&nbsp;${thisReport.key.substring(12+7)}</td>
                        <td colspan="1" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value}"/></td>
                        <td colspan="2" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value * -20 / factoryProduction}"/></td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value * -1 / factoryProduction}"/></td>
                        <td colspan="2" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value * -5 / factoryProduction}"/></td>
                        <td colspan="4" class="warehouse-details">&nbsp;</td>
                    </tr>
                </c:when>
                <c:when test='${thisReport.key.contains("horsebreedingfarm")}'>
                    <tr class="warehouse-details">
                        <td align="center" width="38" class="warehouse-details">
                            Horse&nbsp;breeding&nbsp;farm&nbsp;at&nbsp;${thisReport.key.substring(12+17)}</td>
                        <td colspan="7" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value}"/></td>
                        <td colspan="5" class="warehouse-details">&nbsp;</td>
                    </tr>
                </c:when>
                <c:when test='${thisReport.key.contains("lumbercamp")}'>
                    <tr class="warehouse-details">
                        <td align="center" width="38" class="warehouse-details">
                            Lumbercamp&nbsp;at&nbsp;${thisReport.key.substring(12+10)}</td>
                        <td colspan="4" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value}"/></td>
                        <td colspan="8" class="warehouse-details">&nbsp;</td>
                    </tr>
                </c:when>
                <c:when test='${thisReport.key.contains("mine.type.7")}'>
                    <tr class="warehouse-details">
                        <td align="center" width="38" class="warehouse-details">
                            Ore&nbsp;mine&nbsp;at&nbsp;${thisReport.key.substring(12+11)}</td>
                        <td colspan="5" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value}"/></td>
                        <td colspan="7" class="warehouse-details">&nbsp;</td>
                    </tr>
                </c:when>
                <c:when test='${thisReport.key.contains("mine.type.8")}'>
                    <tr class="warehouse-details">
                        <td align="center" width="38" class="warehouse-details">
                            Gems&nbsp;mine&nbsp;at&nbsp;${thisReport.key.substring(12+11)}</td>
                        <td colspan="6" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value}"/></td>
                        <td colspan="6" class="warehouse-details">&nbsp;</td>
                    </tr>
                </c:when>
                <c:when test='${thisReport.key.contains("mine.type.12")}'>
                    <tr class="warehouse-details">
                        <td align="center" width="38" class="warehouse-details">
                            Precious&nbsp;metals&nbsp;mine&nbsp;at&nbsp;${thisReport.key.substring(12+12)}</td>
                        <td colspan="10" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value}"/></td>
                        <td colspan="2" class="warehouse-details">&nbsp;</td>
                    </tr>
                </c:when>
                <c:when test='${thisReport.key.contains("mint")}'>
                    <c:set var="mintProduction" value="30000"/>
                    <c:if test="${game.boostedProduction == true}"><c:set var="mintProduction" value="${mintProduction * 1.25}"/></c:if>
                    <tr class="warehouse-details">
                        <td align="center" width="38" class="warehouse-details">
                            Mint&nbsp;at&nbsp;${thisReport.key.substring(12+4)}</td>
                        <td colspan="10" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value * -1 / mintProduction}"/></td>
                        <td colspan="2" class="warehouse-details">&nbsp;</td>
                    </tr>
                </c:when>
                <c:when test='${thisReport.key.contains("plantation")}'>
                    <tr class="warehouse-details">
                        <td align="center" width="38" class="warehouse-details">
                            Plantation&nbsp;at&nbsp;${thisReport.key.substring(12+10)}</td>
                        <td colspan="12" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value}"/></td>
                    </tr>
                </c:when>
                <c:when test='${thisReport.key.contains("quarry")}'>
                    <tr class="warehouse-details">
                        <td align="center" width="38" class="warehouse-details">
                            Quarry&nbsp;at&nbsp;${thisReport.key.substring(12+6)}</td>
                        <td colspan="3" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value}"/></td>
                        <td colspan="9" class="warehouse-details">&nbsp;</td>
                    </tr>
                </c:when>
                <c:when test='${thisReport.key.contains("sheepfarm")}'>
                    <tr class="warehouse-details">
                        <td align="center" width="38" class="warehouse-details">
                            Sheep&nbsp;Farm&nbsp;at&nbsp;${thisReport.key.substring(12+9)}</td>
                        <td colspan="9" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value}"/></td>
                        <td colspan="3" class="warehouse-details">&nbsp;</td>
                    </tr>
                </c:when>
                <c:when test='${thisReport.key.contains("vineyard")}'>
                    <tr class="warehouse-details">
                        <td align="center" width="38" class="warehouse-details">
                            Vineyard&nbsp;at&nbsp;${thisReport.key.substring(12+8)}</td>
                        <td colspan="11" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value}"/></td>
                        <td class="warehouse-details">&nbsp;</td>
                    </tr>
                </c:when>
                <c:when test='${thisReport.key.contains("weavingmill")}'>
                    <tr class="warehouse-details">
                        <td align="center" width="38" class="warehouse-details">
                            Weaving&nbsp;Mill&nbsp;at&nbsp;${thisReport.key.substring(12+11)}</td>
                        <td colspan="8" class="warehouse-details">&nbsp;</td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value}"/></td>
                        <td class="warehouse-details"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                        groupingUsed="true"
                                                                        value="${thisReport.value * -2}"/></td>
                        <td colspan="3" class="warehouse-details">&nbsp;</td>
                    </tr>
                </c:when>
            </c:choose>
        </c:if>
    </c:forEach>
    <tr class="warehouse-activity">
        <td align="center" width="38" class="warehouse-activity">Materials&nbsp;Production</td>
        <c:forEach items="${production[thisRegion]}" var="good">
            <td class="warehouse-activity"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                             groupingUsed="true"
                                                             value="${good}"/></td>
        </c:forEach>
    </tr>
    <tr class="warehouse-activity">
        <td align="center" width="38" class="warehouse-activity">Materials&nbsp;Transferred</td>
        <c:forEach items="${transfers[thisRegion]}" var="good">
            <td class="warehouse-activity"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                             groupingUsed="true"
                                                             value="${good}"/></td>
        </c:forEach>
    </tr>
</c:otherwise>
</c:choose>
<c:forEach items="${individualReports[thisRegion]}" var="thisReport">
    <tr class="warehouse-activity">
        <td align="center" width="38" class="warehouse-activity">${thisReport.key}</td>
        <c:choose>
            <c:when test='${thisReport.key.contains("food")}'>
                <td colspan="2" class="warehouse-activity">&nbsp;</td>
                <td class="warehouse-activity"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                 groupingUsed="true"
                                                                 value="${thisReport.value * -1}"/></td>
                <td colspan="10" class="warehouse-activity">&nbsp;</td>
            </c:when>
            <c:when test='${thisReport.key.contains("wine")}'>
                <td colspan="11" class="warehouse-activity">&nbsp;</td>
                <td class="warehouse-activity"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                 groupingUsed="true"
                                                                 value="${thisReport.value * -1}"/></td>
                <td colspan="1" class="warehouse-activity">&nbsp;</td>
            </c:when>
            <c:when test='${thisReport.key.contains("increase")}'>
                <td class="warehouse-activity"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                 groupingUsed="true"
                                                                 value="${thisReport.value}"/></td>
                <td colspan="12" class="warehouse-activity">&nbsp;</td>
            </c:when>
            <c:otherwise>
                <td class="warehouse-activity"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                                 groupingUsed="true"
                                                                 value="${thisReport.value * -1}"/></td>
            </c:otherwise>
        </c:choose>
    </tr>
</c:forEach>
<tr class="warehouse-activity">
    <td colspan="${columns}" class="warehouse-sep">&nbsp;</td>
</tr>
<tr class="warehouse-activity">
    <td align="center" width="38" class="warehouse-activity">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Available&nbsp;at&nbsp;end&nbsp;of&nbsp;Month
    </td>
    <c:forEach items="${available[thisRegion]}" var="good">
        <td class="warehouse-activity"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                         groupingUsed="true"
                                                         value="${good}"/></td>
    </c:forEach>
</tr>
</table>
</c:if>
</c:forEach>

<% if (request.getParameter("fixForClient") == null) { %>
</div>

<div style="clear: both;position: relative; margin: -20px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78"></div>

<div style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 20px;">Older months</h2>
</div>

<div style=" position: relative;margin: 4px 0px 0px -35px;width: 990px;min-height: 41px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">

    <h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>

    <c:forEach items="${months}" var="thisMonth">
        <c:if test="${game.turn!= thisMonth.key}">
            <c:if test="${turn== thisMonth.key}"><b></c:if>
            &nbsp;<a href='<c:url
                value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/economy/${thisMonth.key}"/>'>${thisMonth.value}</a>&nbsp;
            <c:if test="${turn== thisMonth.key}"></b></c:if>
        </c:if>
    </c:forEach>
    <%}%>
</div>
