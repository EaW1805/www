<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<jsp:useBean id="battles" scope="request" class="java.util.List<com.eaw1805.data.model.battles.FieldBattleReport>"/>
<jsp:useBean id="battleToSector" scope="request" class="java.util.Map<java.lang.Integer, com.eaw1805.data.model.map.Sector>"/>

<ul>
<c:forEach items="${battles}" var="battle">
    <li>

        #${battle.battleId}, Side 1 [
        <c:forEach items="${battle.side1}" var="nation">
            ${nation.name},
            <c:set var="side1" value="${nation.id}"/>
        </c:forEach>
        ],
        Side 2 [
        <c:forEach items="${battle.side2}" var="nation">
            ${nation.name},
            <c:set var="side2" value="${nation.id}"/>
        </c:forEach>
        ],
        ${battleToSector[battle.battleId].terrain.name},
        ${battleToSector[battle.battleId].productionSite.name},
        Round ${battle.round}, Status ${battle.status}-
        <a href='<c:url
                value="/play/scenario/1802/fieldbattle/${battle.battleId}/nation/${side1}"/>'>Play as side 1</a>,
        <a href='<c:url
                value="/play/scenario/1802/fieldbattle/${battle.battleId}/nation/${side2}"/>'>Play as side 2</a>,
        <c:if test="${battle.status != 'Being processed' && !battle.gameEnded}">
            <a href='<c:url
                    value="/fieldbattle/${battle.battleId}/run"/>'>Process rounds</a>
        </c:if>
        <c:if test="${battle.winner != -1}">
            Winner : ${battle.winner}
        </c:if>
    </li>

</c:forEach>
</ul>
