<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<jsp:useBean id="gameId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="nationId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="turn" scope="request" class="java.lang.Integer"/>
<% if (request.getParameter("menu").equals("overview")
        || request.getParameter("menu").equals("economy")
        || request.getParameter("menu").equals("orders")
        || request.getParameter("menu").equals("production")
        || request.getParameter("menu").equals("battles")
        || request.getParameter("menu").equals("army")
        || request.getParameter("menu").equals("trains")
        || request.getParameter("menu").equals("navy")) { %>
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
    }

    .noScrollBars {
        overflow: hidden !important;
        cursor: crosshair;
    }
</style>
<div style=" position: relative;margin: 45px 0px 0px -35px;width: 1000px;min-height: 75px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
        <%}%>
    <h1 style="margin-left: 20px;">
        <% if (request.getParameter("menu").equals("overview")) { %>
        <img src='http://static.eaw1805.com/images/panels/reports/ButHorOverviewOn.png'
             onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorOverviewHover.png'"
             onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorOverviewOn.png'"
             alt="Turn Overview"
             style="width: 150px;">
        <%} else {%>
        <a href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/overview"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorOverviewOff.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorOverviewHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorOverviewOff.png'"
                alt="Turn Overview"
                style="width: 150px;"></a>
        <%}%>

        <% if (request.getParameter("menu").equals("newsletter")) { %>
        <a href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/newsletter"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorNewsletterOn.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorNewsletterHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorNewsletterOff.png'"
                alt="Newsletter"
                style="width: 150px;"></a>
        <%} else {%>
        <a href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/newsletter"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorNewsletterOff.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorNewsletterHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorNewsletterOff.png'"
                alt="Newsletter"
                style="width: 150px;"></a>
        <%}%>

        <% if (request.getParameter("menu").equals("orders")) { %>
        <img src='http://static.eaw1805.com/images/panels/reports/ButHorTextOrdersOn.png'
             onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextOrdersHover.png'"
             onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextOrdersOn.png'"
             alt="History of Orders"
             style="width: 150px;">
        <%} else {%>
        <a href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/orders/${turn}"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorTextOrdersOff.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextOrdersHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextOrdersOff.png'"
                alt="History of Orders"
                style="width: 150px;"></a>
        <%}%>
        <% if (request.getParameter("menu").equals("economy")) { %>
        <img src='http://static.eaw1805.com/images/panels/reports/ButHorTextEconomyOn.png'
             onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextEconomyHover.png'"
             onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextEconomyOn.png'"
             alt="Economy Report"
             style="width: 150px;">
        <%} else {%>
        <a href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/economy"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorTextEconomyOff.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextEconomyHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextEconomyOff.png'"
                alt="Economy Report"
                style="width: 150px;"></a>
        <%}%>
        <% if (request.getParameter("menu").equals("production")) { %>
        <img src='http://static.eaw1805.com/images/panels/reports/ButHorTextProductionSitesOn.png'
             onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextProductionSitesHover.png'"
             onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextProductionSitesOn.png'"
             alt="Production & Warehouse Statistics"
             style="width: 150px;">
        <%} else {%>
        <a href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/economy/production"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorTextProductionSitesOff.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextProductionSitesHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextProductionSitesOff.png'"
                alt="Production & Warehouse Statistics"
                style="width: 150px;"></a>
        <%}%>
        <% if (request.getParameter("menu").equals("battles")) { %>
        <img src='http://static.eaw1805.com/images/panels/reports/ButHorTextBattleReportsOn.png'
             onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextBattleReportsHover.png'"
             onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextBattleReportsOn.png'"
             alt="Tactical & Naval Battle Reports"
             style="width: 150px;">
        <%} else {%>
        <a href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/battles"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorTextBattleReportsOff.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextBattleReportsHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextBattleReportsOff.png'"
                alt="Tactical & Naval Battle Reports"
                style="width: 150px;"></a>
        <%}%>
        <%--
            <a href='<c:url value="/game/${gameId}/nation/${nationId}/barracks"/>'><img
                    src='http://static.eaw1805.com/images/panels/reports/ButHorTextBarracksShipyardsOff.png'
                    onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextBarracksShipyardsHover.png'"
                    onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextBarracksShipyardsOff.png'"
                    alt="Barracks & Shipyards"
                    style="width: 150px;"></a>
        --%>
        <% if (request.getParameter("menu").equals("army")) { %>
        <a href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/army"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorTextLandForcesOn.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextLandForcesHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextLandForcesOff.png'"
                alt="Land forces"
                style="width: 150px;"></a>
        <%} else {%>
        <a href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/army"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorTextLandForcesOff.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextLandForcesHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextLandForcesOff.png'"
                alt="Land forces"
                style="width: 150px;"></a>
        <%}%>
        <% if (request.getParameter("menu").equals("navy")) { %>
        <a href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/navy"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorTextNavalForcesOn.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextNavalForcesHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextNavalForcesOff.png'"
                alt="Naval forces"
                style="width: 150px;"></a>
        <%} else {%>
        <a href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/navy"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorTextNavalForcesOff.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextNavalForcesHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorTextNavalForcesOff.png'"
                alt="Naval forces"
                style="width: 150px;"></a>
        <%}%>
        <% if (request.getParameter("menu").equals("btrain")) { %>
        <a href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/baggagetrains"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorBaggageTrainsOn.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorBaggageTrainsHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorBaggageTrainsOff.png'"
                alt="Naval forces"
                style="width: 150px;"></a>
        <%} else {%>
        <a href='<c:url value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/baggagetrains"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorBaggageTrainsOff.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorBaggageTrainsHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorBaggageTrainsOff.png'"
                alt="Naval forces"
                style="width: 150px;"></a>
        <%}%>

        <% if (request.getParameter("menu").equals("gameInfo")) { %>
        <a href='<c:url value="/scenario/${game.scenarioIdToString}/game/${gameId}/info"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorGameInfoOn.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorGameInfoHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorGameInfoOff.png'"
                alt="Game Info"
                style="width: 150px; margin-left: 312px;"></a>
        <%} else {%>
        <a href='<c:url value="/scenario/${game.scenarioIdToString}/game/${gameId}/info"/>'><img
                src='http://static.eaw1805.com/images/panels/reports/ButHorGameInfoOff.png'
                onmouseover="this.src='http://static.eaw1805.com/images/panels/reports/ButHorGameInfoHover.png'"
                onmouseout="this.src='http://static.eaw1805.com/images/panels/reports/ButHorGameInfoOff.png'"
                alt="Game Info"
                style="width: 150px; margin-left: 312px;"></a>
        <%}%>
    </h1>
