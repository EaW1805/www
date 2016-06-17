<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:useBean id="game" scope="request" class="com.eaw1805.data.model.Game"/>
<jsp:useBean id="gameId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="nation" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="nationId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="btrainList" scope="request" type="java.util.List<com.eaw1805.data.model.economy.BaggageTrain>"/>
<jsp:useBean id="goodList" scope="request" type="java.util.List<com.eaw1805.data.model.economy.Good>"/>
<jsp:useBean id="bTrainsWithOrders" scope="request" type="java.util.Set<java.lang.Integer>"/>
<jsp:useBean id="trainToBrigades" scope="request" type="java.util.Map<java.lang.Integer, java.util.ArrayList<com.eaw1805.data.model.army.Brigade>>"/>
<jsp:useBean id="trainToCommanders" scope="request" type="java.util.Map<java.lang.Integer, java.util.ArrayList<com.eaw1805.data.model.army.Commander>>"/>
<jsp:useBean id="trainToSpies" scope="request" type="java.util.Map<java.lang.Integer, java.util.ArrayList<com.eaw1805.data.model.army.Spy>>"/>
<jsp:useBean id="sortedTrains" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.util.ArrayList<com.eaw1805.data.model.economy.BaggageTrain>>>"/>
<jsp:useBean id="nameToNation" scope="request" type="java.util.Map<java.lang.String, com.eaw1805.data.model.Nation>"/>
<%@ include file="/WEB-INF/jsp/cards/BrigadeCard.jsp" %>
<%@ include file="/WEB-INF/jsp/cards/CommanderCard.jsp" %>
<%@ include file="/WEB-INF/jsp/cards/SpyCard.jsp" %>


</div>
<div style="clear: both; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;">
    <h2 style="padding-left: 60px;">${gameDate}</h2>
</div>
<c:set var="count" value="0"/>
<c:set var="totalPositions" value="0"/>
<c:forEach items="${sortedTrains}" var="entry">
    <c:set var="totalPositions" value="${totalPositions + fn:length(entry.value)}"/>
</c:forEach>
<c:set var="positionCounter" value="0"/>

<c:forEach items="${sortedTrains}" var="entry">
    <c:forEach items="${entry.value}" var="btrains">
        <c:set var="count" value="${count + 1}"/>
        <div style="clear: both;position: relative; margin: 0 0 -14px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">

        </div>
        <div id="article-<c:out value="${positionCounter}"/>"
                style="position: relative;margin: 4px 0px 0px -35px;width: 1000px;min-height: 190px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
            <c:set var="positionCounter" value="${positionCounter + 1}"/>
        <article>

            <h2><c:out value="${btrains.value[0].position.toString()}"/></h2>



            <c:forEach items="${btrains.value}" var="btrain">
                <jsp:useBean id="btrain" type="com.eaw1805.data.model.economy.BaggageTrain"/>
                <baggagetrain>
                    <h2><c:out value="${btrain.name}"/></h2>
                    <c:if test="${fn:length(bTrainsLandUnits[btrain.baggageTrainId]) > 1 }">
                        <ul class="infopanel">
                            <li>
                                <table width="100%">
                                    <c:forEach items="${bTrainsLandUnits[btrain.baggageTrainId]}" var="landUnit">
                                        <tr>
                                            <c:choose>
                                                <%--for spies--%>
                                                <c:when test="${landUnit.key == 8}">
                                                    <td class="infopanel"><img
                                                            src="http://static.eaw1805.com/images/layout/buttons/unitMenu/ButSpiesOff.png"
                                                            tooltip="<%=getSpiesCards(trainToSpies.get(btrain.getBaggageTrainId()), request, nameToNation, 0)%>"
                                                            style="width:15px;height:15px"
                                                            title="Loaded spies : ${landUnit.value}"/></td>
                                                </c:when>
                                                <%--for commanders--%>
                                                <c:when test="${landUnit.key == 7}">
                                                    <td class="infopanel"><img
                                                            src="http://static.eaw1805.com/images/layout/buttons/unitMenu/ButCommandersOff.png"
                                                            tooltip="<%=getCommandersCards(trainToCommanders.get(btrain.getBaggageTrainId()), request, 0)%>"
                                                            style="width:15px;height:15px"
                                                            title="Loaded commanders : ${landUnit.value}"/></td>
                                                </c:when>
                                                <%--for brigades--%>
                                                <c:when test="${landUnit.key == 3}">
                                                    <td class="infopanel"><img
                                                            src="http://static.eaw1805.com/images/layout/buttons/unitMenu/ButArmiesOff.png"
                                                            tooltip="<%=getBrigadesCards(trainToBrigades.get(btrain.getBaggageTrainId()), request, 0)%>"
                                                            style="width:15px;height:15px"
                                                            title="Loaded brigades : ${landUnit.value}"/></td>
                                                </c:when>
                                            </c:choose>
                                            <td class="infopanelTotals">${landUnit.value}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </li>
                        </ul>
                    </c:if>



                    <table class="warehouse-activity" style="background: none; border: none; border-style: hidden; margin-top: 0px; box-shadow: 0 0 0 0;">
                        <tr class="warehouse-activity">
                            <th class="warehouse-activity" width="38"></th>
                            <c:forEach items="${goodList}" var="good">

                                <th align="center" width="56" class="warehouse-activity"><img
                                        src='http://static.eaw1805.com/images/goods/good-${good.goodId}.png'
                                        class="toolTip"
                                        title="${good.name}"
                                        alt="${good.name}"
                                        border=0 width=24></th>
                            </c:forEach>
                        </tr>
                        <tr class="warehouse-activity">
                            <td class="warehouse-activity" width="38" style="width: 33%;" align="center">From previous Month</td>
                            <c:forEach items="${goodList}" var="good">
                                <td class="warehouse-activity">${statisticsData[btrain.baggageTrainId]["initialState"][good.goodId]}</td>
                            </c:forEach>
                        </tr>

                        <c:forEach items="${statisticsData[btrain.baggageTrainId]['sortedOrdersList']}" var="order">
                            <tr class="warehouse-activity">
                                <td class="warehouse-activity">
                                        ${order.explanation}
                                </td>
                                <c:forEach items="${goodList}" var="good">
                                    <td class="warehouse-activity">${statisticsData[btrain.baggageTrainId]["orderToShipWarehouse"][order.orderId][good.goodId]}</td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                        <tr class="warehouse-activity">
                            <td class="warehouse-activity">Available at the end of the month</td>
                            <c:forEach items="${goodList}" var="good">
                                <td class="warehouse-activity">${statisticsData[btrain.baggageTrainId]["finalState"][good.goodId]}</td>
                            </c:forEach>
                        </tr>
                    </table>
                </baggagetrain>
            </c:forEach>
        </article>
        </div>
        <c:if test="${count != totalPositions}">
            <div style="clear: both; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
        clear: both; opacity: 0.78;"></div>
        </c:if>
    </c:forEach>
</c:forEach>

<script type="text/javascript">
    $(document).ready(function () {
        <c:forEach var="articleCounter" begin="0" end="${positionCounter-2}" step="1">
        $('#article-<c:out value="${articleCounter}"/>').css({ 'min-height':$('#article-<c:out value="${articleCounter+1}"/>').position().top - $('#article-<c:out value="${articleCounter}"/>').position().top - 116});
        </c:forEach>
        $('#article-<c:out value="${positionCounter-1}"/>').css({ 'min-height':$('#main').height() - $('#article-<c:out value="${positionCounter-1}"/>').position().top - 95});
    });
</script>
