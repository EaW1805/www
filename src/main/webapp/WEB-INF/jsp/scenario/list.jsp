<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:useBean id="lstScenaria" scope="request" type="java.util.List<java.lang.String>"/>
<jsp:useBean id="lstScenariaFuture" scope="request" type="java.util.List<java.lang.String>"/>
<jsp:useBean id="mapName" scope="request" type="java.util.Map<java.lang.String,java.lang.String>"/>
<jsp:useBean id="mapDescription" scope="request" type="java.util.Map<java.lang.String,java.lang.String>"/>
<jsp:useBean id="mapPlayers" scope="request" type="java.util.Map<java.lang.String,java.lang.String>"/>
<jsp:useBean id="freeNations" scope="request" type="java.util.LinkedList<java.util.LinkedList<java.lang.Object>>"/>
<jsp:useBean id="specialOffer" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Boolean>"/>
<style type="text/css">
    .pagehead {
        background: none;
        margin-top: -192px;
    }

    #content {
        padding-left: 10px;
        padding-right: 60px;
        padding-bottom: 0px;
        overflow: visible;
        z-index: 1;
    }

    .noScrollBars {
        overflow: hidden !important;
        cursor: crosshair;
    }
</style>
<script type="text/javascript">
    $.blockUI.defaults.applyPlatformOpacityRules = false;
    function openPickupForm(scenario, game, nation, nationName, cost) {

    <c:url var="thisUrl" value="/"/>
        var url = "${thisUrl}" + "scenario/" + scenario + "/game/" + game + "/pickup/" + nation;
        $('#joinForm').attr('action', url);
        $('#form_nationId').text(nation);
        $('#nationImgFile').attr('src', "http://static.eaw1805.com/images/nations/nation-" + nation + "-list.jpg");
        $('#nationImgFile').attr('title', nationName);
        $('#cost').text(cost);
        $('#joinTitle').text("Join Game " + game);
        $('#ok').blur();
        $.blockUI({ message:$('#question'), css:{ width:'390px' } });
    }
</script>
<div style="z-index: 20; position: relative;margin: 45px 0px 0px -35px;width: 1000px; overflow: auto; padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
    <h1 style="z-index: 2; font-size: 42px; margin-top: -10px;">Empires at War Campaigns Scenarios</h1>

    <p class="manual" style="padding-top: 5px;">Empires at War 1805 offers different campaign scenarios.</p>
</div>

<c:set var="prevLink" value=""/>

<c:forEach items="${lstScenaria}" var="scenarioId">
    <div style="z-index: 2; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;">
        <c:if test="${fn:length(prevLink)>0}">
            <p class="manual" style="padding-left: 38px; padding-top: 8px;"><a style="font-size: 16px;"
                                                                               href='<c:url value="${prevLink}"/>'>Read
                more</a></p>
        </c:if>
    </div>

    <div id="main-article-top"
         style="z-index: 2; position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
        <h2 style="padding-left: 40px; padding-top: 20px; font-size: 28px;">
            Scenario ${scenarioId} ${mapName[scenarioId]}</h2>
    </div>

    <div style="z-index: 1; position: relative;margin: -10px 0px 0px -35px;width: 990px; padding: 0px 40px; overflow:auto;
    background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">

        <div style="float: left; width: 380px; margin-top: 15px;"><img
                style="border-radius: 5px;"
                src="http://static.eaw1805.com/site/images/images-game/scenario_${scenarioId}.jpg"
                border="0" width=380/></div>

        <div style="float: right; width: 560px; margin-top: 15px; margin-right: 40px;">${mapDescription[scenarioId]}</div>

        <div style="float: left; width: 940px; margin-top: 10px;">
            <div style="float:left; margin-top: 2px; margin-right: 8px;"><p class="manual"
                                                                            style="font-size: 14px; font-weight: bold;">
                Available Countries:</p></div>
            <div style="float: left;">${mapPlayers[scenarioId]}</div>
        </div>

        <c:set var="nations" value="0"/>
        <c:forEach items="${freeNations}" var="thisNationData">
            <c:if test="${thisNationData[0].scenarioIdToString == scenarioId}">
                <c:set var="nations" value="${nations+1}"/>
            </c:if>
        </c:forEach>
        <c:set var="rows" value="${nations/3}"/>
        <c:set var="height"><fmt:formatNumber value="${rows+(1-(rows%1))%1}" type="number" pattern="#"/></c:set>
        <c:if test="${nations > 0}">
            <div style="float:left; width: 955px; margin-left: 0px; margin-top: 5px; min-height:${height*104 + 45}px;">
                <p class="manual"
                   style="font-size: 14px; font-weight: bold; margin-top: 0px; padding-top: 0px; padding-bottom: 10px;">
                    Join Existing Game and Start Playing Now:</p>
                <nationList>
                    <ul class="nationList">
                        <c:forEach items="${freeNations}" var="thisNationData">
                            <c:if test="${thisNationData[0].scenarioIdToString == scenarioId}">
                                <li class="nationList" style="cursor: pointer;height: 100px;width: 311px; float:left; margin:0; margin-right: 2px; margin-bottom: 2px;
border-right: 1px solid rgb(143, 143, 143);" aria-describedby="" title="Play now"
                                    onclick='openPickupForm("${thisNationData[0].scenarioIdToString}", "${thisNationData[0].gameId}","${thisNationData[1].id}", "${thisNationData[1].name}", "${thisNationData[4].cost}" );'>
                                    <dl class="nationList">
                                        <dt class="nationList" style="width: 140px;"><a
                                                href='<c:url value="/scenario/${thisNationData[0].scenarioIdToString}/game/${thisNationData[0].gameId}/info" />'>
                                            <img
                                                    style="margin: 0; border: 0; border-radius: 1px;"
                                                    src='http://static.eaw1805.com/images/nations/nation-${thisNationData[1].id}-list.jpg'
                                                    alt="Nation's Flag"
                                                    class="toolTip"
                                                    title="Show more info"
                                                    border=0 width=180
                                                    aria-describedby=""
                                                    ></a></dt>
                                        <dd class="nationList"
                                            style='width: 20pt; padding-top: 7px;margin-top: -11px;margin-left: 42px; line-height: 1.3 !important;
                                                   font-family: Georgia,"Times New Roman",Times,serif;
                                                   text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);font-size: 1.6em;'>
                                            Game ${thisNationData[0].gameId}
                                        </dd>
                                        <dd class="nationList"
                                            style='width: 20pt; padding-top: 0px;margin-left: 50px; line-height: 1.3 !important;
                                                   font-family: Georgia,"Times New Roman",Times,serif;
                                                   text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);font-size: 1.1em;'>
                                                ${monthsSmall[thisNationData[0].gameId]}
                                        </dd>
                                        <dd class="nationList"
                                            style='width: 20pt; padding-top: 7px; line-height: 1.3 ! important; font-family: Georgia,"Times New Roman",Times,serif; text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2); font-size: 1.3em; margin-left: -102px; margin-top: 12px;'>
                                            Scenario ${thisNationData[0].scenarioIdToString}
                                        </dd>
                                        <dt class="nationList" style="width: 140px; clear: both;">
                                                <c:set var="index" value="${0}"/>
                                                <c:set var="statistics" value="${thisNationData[3]}"/>
                                            <c:forEach items="${statistics}" var="stats">
                                            <c:if test="${index < 3}">
                                        <div style="clear:both;float: left;margin-left: 5px;width: 190px; margin-top:2px;">
                                            <div style="float: left;width: 150px;text-align: right;margin-right: 10px;">
                                                <spring:message code="${stats.key}"/></div>
                                            <div style="float: left;width: 25px;">${stats.value}</div>
                                        </div>
                                        </c:if>
                                        <c:set var="index" value="${index+1}"/>
                                        </c:forEach>
                                        </dt>
                                        <dd class="nationList"
                                            style="width: 30pt; padding-top: 10px;margin-top: -3px;margin-left: 55px;">
                                            vp:
                                        </dd>
                                        <dd class="nationList"
                                            style="width: 20pt; padding-top: 7px;margin-top: -11px;font-size:26px; margin-left: 5px;">
                                            <fmt:formatNumber
                                                    type="number"
                                                    maxFractionDigits="0"
                                                    groupingUsed="true"
                                                    value="${thisNationData[2]}"/>
                                        </dd>
                                        <div style="float: right;margin-left: 5px;width: 80px; margin-top:-5px; height:30px;">
                                            <div>Position Cost</div>
                                            <div style="float: left;width: 20px;">${thisNationData[4].cost}</div>
                                            <div><img style="float: left; border: 0;margin: 0;padding: 0;"
                                                      src='http://static.eaw1805.com/images/goods/good-1.png'
                                                      alt="Credits"
                                                      class="toolTip"
                                                      border=0 height=15/></div>
                                            <div style="margin-left: 5px;float: left;"> / &nbsp;turn</div>
                                        </div>
                                        <c:if test="${specialOffer[thisNationData[4].id]}">
                                            <div style="float: left; width: 90px; height:88px; margin-top:-83px; margin-left: -162px;">
                                                <img style="float: left; border: 0;margin: 0;padding: 0;"
                                                     src='http://static.eaw1805.com/images/site/paypal/60-free-ribbon.png'
                                                     title="Special Offer: Get 60 credits for FREE"
                                                     alt="Special offer"
                                                     class="toolTip"
                                                     border=0/>
                                            </div>
                                        </c:if>
                                    </dl>
                                </li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </nationList>
            </div>
        </c:if>
    </div>
    <c:choose>
        <c:when test="${scenarioId=='1804'}">
            <c:set var="prevLink" value="/joingame/free"/>
        </c:when>
        <c:otherwise>
            <c:set var="prevLink" value="/scenario/${scenarioId}/info"/>
        </c:otherwise>
    </c:choose>
</c:forEach>

<div style="z-index: 2; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;">
    <p class="manual" style="padding-left: 38px; padding-top: 8px;"><a style="font-size: 16px;"
                                                                       href='<c:url value="${prevLink}"/>'>Read more</a>
    </p>
</div>

<div id="main-article-top"
     style="z-index: 2; position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 20px; font-size: 42px;">Coming Soon</h2>
</div>

<div style="z-index: 1; position: relative; margin: -10px 0px 0px -35px; width: 990px; padding: 0px 40px; overflow:auto;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
    <c:forEach items="${lstScenariaFuture}" var="scenarioId">
        <h2 style="padding-left: 40px; padding-top: 20px; clear: both;">
            Scenario ${scenarioId} ${mapName[scenarioId]}</h2>

        <div style="float: left; width: 300px; margin-left: 40px;"><img
                style=" border-radius: 5px;"
                src="http://static.eaw1805.com/site/images/images-game/scenario_${scenarioId}.jpg"
                border="0" width=300/></div>

        <div style="float: right; width: 595px; margin-right: 40px;">${mapDescription[scenarioId]}</div>
    </c:forEach>
</div>

<div id="question" style="display:none; cursor: default">
    <h3 id="joinTitle" style="margin-top: 22px;width: 120px;margin-left: 14px;float: left;">Join Game</h3>
    <img style="float:right;margin-top: 20px; margin-right: 20px;" id="nationImgFile"
         src='http://static.eaw1805.com/images/nations/nation-1-list.jpg'
         alt="Nation's Flag"
         class="toolTip"
         title="#"
         border=0 height=33>

    <div>
        <h3 style="margin-top:20px;margin-left: 70px; float: left; width: 150px;">Position costs &nbsp;
            <div id="cost" style="float:right;width: 20px;margin-right: 5px;"/>
        </h3>
        <img style="margin-top:25px;float:left;" src='http://static.eaw1805.com/images/goods/good-1.png'
             alt="Credits"
             class="toolTip"
             title="Credits"
             border=0 height=20>

        <h3 style="margin-top:20px;margin-left: 7px; float: left; width: 80px;">per turn</h3>
    </div>
    <form style="clear: both;" method="get" id="joinForm" action='<c:url value="/scenario/1/game/1/pickup/1"/>'>
        <button class="ok" id="ok" onfocus=" this.blur();" style="margin-top: 20px; margin-right: 5px;"
                title="Pick up position "></button>
        <button class="cancel" title="Cancel" id="cancel" value="Cancel" style="margin-left: 5px;"
                onclick="$.unblockUI();$('#'+$('#cancel').attr('aria-describedby')).hide();return false;"
                aria-describedby=""></button>
    </form>
    <div>
        <img style="float:left;margin-left: 20px; margin-top: -5px;"
             src='http://static.eaw1805.com/images/buttons/taxation/MUINormalTaxSlc.png'
             alt="Credits"
             class="toolTip"
             title="Account Balance"
             border=0 height=32>

        <div style="float: left;font-size: 20px; margin: 2px;margin-top: -2px; margin-left: 5px;">${user.creditFree+user.creditTransferred+user.creditBought}</div>
    </div>
</div>
