<%@ page import="java.util.HashSet" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="game" scope="request" class="com.eaw1805.data.model.Game"/>
<jsp:useBean id="gameId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="nation" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="nationId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="barracks" scope="request" type="java.util.List<com.eaw1805.data.model.map.Sector>"/>

<%@ include file="/WEB-INF/jsp/cards/battalionCard.jsp" %>
<%@ include file="/WEB-INF/jsp/cards/shipCard.jsp" %>
<c:set var="expStyle" value="color:brown;font-size:13px;"/>
<c:set var="headcountStyle" value="color:cyan;font-size:13px;"/>

<article>

    <c:forEach items="${barracks}" var="barrack" varStatus="status">
        <army>
            <h1><c:out value="${barrack.productionSite.name}"/></h1>
            <img src='http://static.eaw1805.com/tiles/sites/tprod-${barrack.productionSite.id}.png'
                 alt="${barrack.productionSite.name} Portrait" height="140" width="140">

            <h3 class="good"><fmt:formatNumber type="number" maxFractionDigits="0"
                                               groupingUsed="true"
                                               value="${barrack.populationCount()}"/>
                <img src='http://static.eaw1805.com/images/goods/good-2.png'
                     class="good"
                     title="People"
                     alt="People"
                     border=0 width=16></h3>
            Position: <c:out
                value="${barrack.position.toString()}"/>


            <div class="armiesContainer">
                <ul>
                    <c:forEach items="${barToBat[barrack.id]}" var="typeToBats">
                        <li>
                            <c:forEach items="${typeToBats.value}" var="item">
                                <c:set value="${item.value[3]}" var="battalions"/>
                                <jsp:useBean id="battalions" class="java.util.ArrayList"/>
                                <div class="image">
                                    <img alt=""
                                         tooltip="<%=getBattalionsCards(battalions, request)%>"
                                         src='http://static.eaw1805.com/images/armies/${nationId}/${item.key}.jpg'
                                         width="72"/>

                                    <div class="numBats">
                                        <p><fmt:formatNumber value="${item.value[0]}"
                                                             maxFractionDigits="0"/></p>
                                    </div>
                                    <div class="avgExp">
                                        <p style="${expStyle}"><fmt:formatNumber value="${item.value[1]}"
                                                                                 maxFractionDigits="1"
                                                                                 minFractionDigits="1"/></p>
                                    </div>
                                    <div class="headcount">
                                        <p style="${headcountStyle}"><fmt:formatNumber value="${item.value[2]}"
                                                                                       maxFractionDigits="0"/></p>
                                    </div>
                                </div>
                            </c:forEach>
                        </li>
                    </c:forEach>


                    <c:forEach items="${barToShip[barrack.id]}" var="typesToShips">
                        <li>
                            <c:forEach items="${typesToShips.value}" var="item">
                                <c:set value="${item.value[3]}" var="ships"/>
                                <jsp:useBean id="ships" class="java.util.ArrayList"/>
                                <div class="image">
                                    <img alt=""
                                         tooltip="<%=getShipsCards(ships, request, new HashSet<Integer>(), 0)%>"
                                         src='http://static.eaw1805.com/images/ships/${nationId}/${item.key}.png'
                                         width="72"/>

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
            </div>
        </army>
    </c:forEach>

</article>
