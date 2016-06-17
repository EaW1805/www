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
<jsp:useBean id="turn" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="gameDate" scope="request" class="java.lang.String"/>
<jsp:useBean id="orders" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.PlayerOrder>>"/>
<jsp:useBean id="orderDescr" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
<jsp:useBean id="orderNames" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
<jsp:useBean id="phaseNames" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
<jsp:useBean id="months" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
<jsp:useBean id="news" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<% if (request.getParameter("fixForClient") == null) { %>
</div>

<div style="z-index: 2; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;">
    <h2 style="padding-left: 60px;">${gameDate}</h2>
</div>

<div id="main-article-top"
     style="z-index: 2; position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 20px;">${tableTile}</h2>
</div>

<div id="main-article"
     style="z-index: 1; position: relative;margin: -10px 0px 0px -35px;width: 1000px;min-height: 200px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent; clear: both;">
    <% } %>
    <article>
        <c:forEach items="${orders}" var="orderGroup">
            <orderList>
                <ul class="orderList">
                    <li class="header">
                        <h2 class="bigmap">${phaseNames[orderGroup.key]}</h2>
                    </li>
                    <c:forEach items="${orderGroup.value}" var="thisOrder">
                        <li class="orderList">
                            <dl class="orderList">
                                <dt class="orderList">${orderDescr[thisOrder.orderId]}</dt>
                                <c:if test="${thisOrder.result != 0}">
                                    <dk class="orderList">
                                        <img width=20 class="tooltip"
                                        <c:if test="${thisOrder.result > 0}">
                                             src="http://static.eaw1805.com/images/buttons/ButAcceptOff.png"
                                             title="Success"
                                        </c:if>
                                        <c:if test="${thisOrder.result < 1}">
                                             src="http://static.eaw1805.com/images/buttons/ButCancelOff.png"
                                             title="Failed"
                                        </c:if>
                                                ></dk>
                                    <dj class="orderList">
                                        <img width=20 class="tooltip"
                                        <c:choose>
                                        <c:when test="${thisOrder.region.id == 1}">
                                             src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOff.png"
                                        </c:when>
                                        <c:when test="${thisOrder.region.id == 2}">
                                             src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png"
                                        </c:when>
                                        <c:when test="${thisOrder.region.id == 3}">
                                             src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png"
                                        </c:when>
                                        <c:when test="${thisOrder.region.id == 4}">
                                             src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png"
                                        </c:when>
                                        </c:choose>
                                             title="${thisOrder.region.name}"
                                                ></dj>
                                    <di class="orderList">
                                        <c:forEach items="${thisOrder.usedGoodsQnt}" var="thisEntry">
                                            <c:if test="${thisEntry.value != 0}">
                                                <img width=16
                                                     style="padding-top: 1px; vertical-align: bottom;"
                                                     src="http://static.eaw1805.com/images/goods/good-${thisEntry.key}.png">&nbsp;
                                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                                  groupingUsed="true"
                                                                  value="${thisEntry.value}"/>&nbsp;
                                            </c:if>
                                        </c:forEach>
                                    </di>
                                    <ds class="orderList">${thisOrder.explanation}</ds>
                                </c:if>
                            </dl>
                        </li>
                    </c:forEach>
                </ul>
            </orderList>
        </c:forEach>
        <c:if test="${fn:length(news)>0}">
            <orderList>
                <ul class="orderList">
                    <li class="header">
                        <h2 class="bigmap">Newsletter entries</h2>
                    </li>
                    <c:forEach items="${news}" var="entry">
                        <li class="orderList">
                            <dl class="orderList">
                                <dt class="orderList"><c:if test="${entry.type==11}">Anonymous&nbsp</c:if>Announcement
                                </dt>
                                <dk class="orderList"><a
                                        href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${entry.game.gameId}/nation/${entry.nation.id}/orders/remove/${entry.baseNewsId}"/>'>
                                    <img width=20 class="tooltip"
                                         src="http://static.eaw1805.com/images/buttons/ButCancelOff.png"
                                         title="Cancel announcement"></a></dk>
                                <dn class="orderList">${entry.text}</dn>
                            </dl>
                        </li>
                    </c:forEach>
                </ul>
            </orderList>
        </c:if>
    </article>
    <% if (request.getParameter("fixForClient") == null) { %>
</div>

<div id="main-article-bottom"
     style="clear: both;position: relative; margin: -20px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78"></div>

<div style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 20px;">Older months</h2>
</div>

<div id="orders-article" style=" position: relative;margin: 4px 0px 0px -35px;width: 990px;min-height: 30px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">

    <h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>

    <c:forEach items="${months}" var="thisMonth">
    <c:if test="${turn== thisMonth.key}"><b></c:if>
    &nbsp;<a href='<c:url
        value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/orders/${thisMonth.key}"/>'>${thisMonth.value}</a>&nbsp;
    <c:if test="${turn== thisMonth.key}"></b></c:if>
    </c:forEach>

    <script type="text/javascript">
        $(document).ready(function () {
            setTimeout(function(){$('#main-article').css({ 'min-height':$('#main-article-bottom').position().top - $('#main-article-top').position().top - 68});},1000);
            $('#main-article').css({ 'min-height':$('#main-article-bottom').position().top - $('#main-article-top').position().top - 68});
            $('#orders-article').css({ 'min-height':$('#main').height() - $('#orders-article').position().top});
        });
    </script>
        <%}%>

