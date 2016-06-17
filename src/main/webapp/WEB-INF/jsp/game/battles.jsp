<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="game" scope="request" class="com.eaw1805.data.model.Game"/>
<jsp:useBean id="gameId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="scenarioId" scope="request" class="java.lang.String"/>
<jsp:useBean id="nation" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="nationId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="battleList" scope="request" type="java.util.List<com.eaw1805.www.commands.BattleData>"/>
<jsp:useBean id="navalList" scope="request" type="java.util.List<com.eaw1805.www.commands.BattleData>"/>
</div>

<div style="clear: both; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;">
    <h2 style="padding-left: 60px;">${gameDate}</h2>
</div>

<div style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 20px;">Tactical Battles</h2>
</div>

<div id="parchment-body" style=" position: relative;margin: 4px 0px 0px -35px;width: 1000px;min-height: 300px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">

    <h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>
    <table class="warehouse"
           style="background: none; border: none; border-style: hidden; margin-top: 0px; box-shadow: 0 0 0 0;">
        <thead>
        <tr class="warehouse">
            <th align="center" class="warehouse">Turn</th>
            <th align="center" class="warehouse">Position</th>
            <th align="center" class="warehouse">Enemies</th>
            <th align="center" class="warehouse">Allies</th>
            <th align="center" class="warehouse">Result</th>
        </tr>
        <c:forEach items="${battleList}" var="report">
        <tr class="warehouse">
            <td align="center" class="warehouse">${report.turn}</td>
            <td align="center" class="warehouse">${report.position}</td>
            <td align="center" class="warehouse">
                <c:forEach items="${report.sideEnemies}" var="side">
                    <img src='http://static.eaw1805.com/images/nations/nation-${side.id}-36.png'
                         alt="Nation's Flag"
                         class="toolTip"
                         title="${side.name}"
                         border=0 width=28>
                </c:forEach>
            </td>
            <td align="center" class="warehouse">
                <c:forEach items="${report.sideAllies}" var="side">
                    <img src='http://static.eaw1805.com/images/nations/nation-${side.id}-36.png'
                         alt="Nation's Flag"
                         class="toolTip"
                         title="${side.name}"
                         border=0 width=28>
                </c:forEach>
            </td>
            <td align="center" class="warehouse"><a
                    href='<c:url value="/report/scenario/${scenarioId}/game/${gameId}/nation/${nationId}/battle/${report.battleId}"/>'
                    <% if (request.getParameter("fixForClient") != null) { %>target="_blank"<%}%>
                    >${report.winner}</a>
            </td>
        </tr>
        </c:forEach>
    </table>
</div>

<div id="parchment-footer" style="clear: both; position: relative; margin: -21px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;"></div>

<div id="main-article-top" style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 20px;">Naval Battles</h2>
</div>

<div id="main-article" style=" position: relative;margin: 4px 0px -20px -35px;width: 1000px; min-height: 300px; padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">

    <h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>
    <table class="warehouse"
           style="background: none; border: none; border-style: hidden; margin-top: 0px; box-shadow: 0 0 0 0;">
        <thead>
        <tr class="warehouse">
            <th align="center" class="warehouse">Turn</th>
            <th align="center" class="warehouse">Position</th>
            <th align="center" class="warehouse">Enemies</th>
            <th align="center" class="warehouse">Allies</th>
            <th align="center" class="warehouse">Result</th>
        </tr>
        <c:forEach items="${navalList}" var="report">
        <tr class="warehouse">
            <td align="center" class="warehouse">${report.turn}</td>
            <td align="center" class="warehouse">${report.position}</td>
            <td align="center" class="warehouse">
                <c:forEach items="${report.sideEnemies}" var="side">
                    <img src='http://static.eaw1805.com/images/nations/nation-${side.id}-36.png'
                         alt="Nation's Flag"
                         class="toolTip"
                         title="${side.name}"
                         border=0 width=28>
                </c:forEach>
            </td>
            <td align="center" class="warehouse">
                <c:forEach items="${report.sideAllies}" var="side">
                    <img src='http://static.eaw1805.com/images/nations/nation-${side.id}-36.png'
                         alt="Nation's Flag"
                         class="toolTip"
                         title="${side.name}"
                         border=0 width=28>
                </c:forEach>
            </td>
            <td align="center" class="warehouse"><a
                    href='<c:url value="/report/scenario/${scenarioId}/game/${gameId}/nation/${nationId}/naval/${report.battleId}"/>'
                    <% if (request.getParameter("fixForClient") != null) { %>target="_blank"<%}%>
                    >${report.winner}</a>
            </td>
        </tr>
        </c:forEach>
    </table>

    <script type="text/javascript">
        $(document).ready(function () {
            setTimeout(function(){$('#parchment-body').css({ 'min-height':$('#parchment-footer').position().top - $('#parchment-body').position().top})},1000);
            $('#parchment-body').css({ 'min-height':$('#parchment-footer').position().top - $('#parchment-body').position().top});
        });
    </script>
