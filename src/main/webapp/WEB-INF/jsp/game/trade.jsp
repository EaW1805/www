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
<jsp:useBean id="goodList" scope="request" type="java.util.List<com.eaw1805.data.model.economy.Good>"/>
<jsp:useBean id="citiesEurope" scope="request" type="java.util.List<com.eaw1805.data.model.economy.TradeCity>"/>
<jsp:useBean id="citiesCaribbean" scope="request" type="java.util.List<com.eaw1805.data.model.economy.TradeCity>"/>
<jsp:useBean id="citiesIndies" scope="request" type="java.util.List<com.eaw1805.data.model.economy.TradeCity>"/>
<jsp:useBean id="citiesAfrica" scope="request" type="java.util.List<com.eaw1805.data.model.economy.TradeCity>"/>

<div class="commanderTableContainer">
<table class="commanderContainer">
<tr>
    <td>
        <table class="warehouse">
            <thead>
            <tr class="warehouse">
                <th colspan="16" align="right" class="warehouse-head">European Trade Cities</th>
            </tr>
            <tr class="warehouse">
                <th align="left" class="warehouse">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;City</th>
                <th align="center" class="warehouse">Position</th>
                <c:forEach items="${goodList}" var="good">
                    <c:choose>
                        <c:when test="${good.goodId == 1}">
                            <th align="center" nowrap valign="top" class="warehouse">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img
                                    src='http://static.eaw1805.com/images/goods/good-${good.goodId}.png'
                                    class="toolTip"
                                    title="${good.name}"
                                    alt="${good.name}"
                                    border=0 width=24></th>
                        </c:when>
                        <c:otherwise>
                            <th align="center" valign="top" width="24" class="warehouse"><img
                                    src='http://static.eaw1805.com/images/goods/good-${good.goodId}.png'
                                    class="toolTip"
                                    title="${good.name}"
                                    alt="${good.name}"
                                    border=0 width=24></th>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${citiesEurope}" var="city">
                <tr class="warehouse">
                    <td align="left" class="warehouse">${city.name}</td>
                    <td class="warehouse" align="center">${city.position.toString()}</td>
                    <c:forEach items="${city.goodsTradeLvl}" var="good">
                        <c:choose>
                            <c:when test="${good.key == 1}">
                                <td align="right" class="warehouse"><fmt:formatNumber type="number"
                                                                                      maxFractionDigits="0"
                                                                                      groupingUsed="true"
                                                                                      value="${good.value}"/></td>
                            </c:when>
                            <c:otherwise>
                                <td align="center" class="warehouse">
                                    <c:choose>
                                        <c:when test="${good.value == 1}">HD</c:when>
                                        <c:when test="${good.value == 2}">D</c:when>
                                        <c:when test="${good.value == 3}">A</c:when>
                                        <c:when test="${good.value == 4}">S</c:when>
                                        <c:when test="${good.value == 5}">ES</c:when>
                                    </c:choose>
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </td>
</tr>
<tr>
    <td>

        <table class="warehouse">
            <thead>
            <tr class="warehouse">
                <th colspan="16" align="right" class="warehouse-head">Caribbean Trade Cities</th>
            </tr>
            <tr class="warehouse">
                <th align="left" class="warehouse">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;City</th>
                <th align="center" class="warehouse">Position</th>
                <c:forEach items="${goodList}" var="good">
                    <c:choose>
                        <c:when test="${good.goodId == 1}">
                            <th align="center" nowrap valign="top" class="warehouse">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img
                                    src='http://static.eaw1805.com/images/goods/good-${good.goodId}.png'
                                    class="toolTip"
                                    title="${good.name}"
                                    alt="${good.name}"
                                    border=0 width=24></th>
                        </c:when>
                        <c:otherwise>
                            <th align="center" valign="top" width="24" class="warehouse"><img
                                    src='http://static.eaw1805.com/images/goods/good-${good.goodId}.png'
                                    class="toolTip"
                                    title="${good.name}"
                                    alt="${good.name}"
                                    border=0 width=24></th>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${citiesCaribbean}" var="city">
                <tr class="warehouse">
                    <td align="left" class="warehouse">${city.name}</td>
                    <td align="center" class="warehouse">${city.position.toString()}</td>
                    <c:forEach items="${city.goodsTradeLvl}" var="good">
                        <c:choose>
                            <c:when test="${good.key == 1}">
                                <td align="right" class="warehouse"><fmt:formatNumber type="number"
                                                                                      maxFractionDigits="0"
                                                                                      groupingUsed="true"
                                                                                      value="${good.value}"/></td>
                            </c:when>
                            <c:otherwise>
                                <td align="center" class="warehouse">
                                    <c:choose>
                                        <c:when test="${good.value == 1}">HD</c:when>
                                        <c:when test="${good.value == 2}">D</c:when>
                                        <c:when test="${good.value == 3}">A</c:when>
                                        <c:when test="${good.value == 4}">S</c:when>
                                        <c:when test="${good.value == 5}">ES</c:when>
                                    </c:choose>
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </td>
</tr>
<tr>
    <td>
        <table class="warehouse">
            <thead>
            <tr class="warehouse">
                <th colspan="16" align="right" class="warehouse-head">Indian Trade Cities</th>
            </tr>
            <tr class="warehouse">
                <th align="left" class="warehouse">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;City</th>
                <th align="center" class="warehouse">Position</th>
                <c:forEach items="${goodList}" var="good">
                    <c:choose>
                        <c:when test="${good.goodId == 1}">
                            <th align="center" nowrap valign="top" class="warehouse">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img
                                    src='http://static.eaw1805.com/images/goods/good-${good.goodId}.png'
                                    class="toolTip"
                                    title="${good.name}"
                                    alt="${good.name}"
                                    border=0 width=24></th>
                        </c:when>
                        <c:otherwise>
                            <th align="center" valign="top" width="24" class="warehouse"><img
                                    src='http://static.eaw1805.com/images/goods/good-${good.goodId}.png'
                                    class="toolTip"
                                    title="${good.name}"
                                    alt="${good.name}"
                                    border=0 width=24></th>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${citiesIndies}" var="city">
                <tr class="warehouse">
                    <td align="left" class="warehouse">${city.name}</td>
                    <td align="center" class="warehouse">${city.position.toString()}</td>
                    <c:forEach items="${city.goodsTradeLvl}" var="good">
                        <c:choose>
                            <c:when test="${good.key == 1}">
                                <td align="right" class="warehouse"><fmt:formatNumber type="number"
                                                                                      maxFractionDigits="0"
                                                                                      groupingUsed="true"
                                                                                      value="${good.value}"/></td>
                            </c:when>
                            <c:otherwise>
                                <td align="center" class="warehouse">
                                    <c:choose>
                                        <c:when test="${good.value == 1}">HD</c:when>
                                        <c:when test="${good.value == 2}">D</c:when>
                                        <c:when test="${good.value == 3}">A</c:when>
                                        <c:when test="${good.value == 4}">S</c:when>
                                        <c:when test="${good.value == 5}">ES</c:when>
                                    </c:choose>
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </td>
</tr>
<tr>
    <td>
        <table class="warehouse">
            <thead>
            <tr class="warehouse">
                <th colspan="16" align="right" class="warehouse-head">African Trade Cities</th>
            </tr>
            <tr class="warehouse">
                <th align="left" class="warehouse">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;City</th>
                <th align="center" class="warehouse">Position</th>
                <c:forEach items="${goodList}" var="good">
                    <c:choose>
                        <c:when test="${good.goodId == 1}">
                            <th align="center" nowrap valign="top" class="warehouse">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img
                                    src='http://static.eaw1805.com/images/goods/good-${good.goodId}.png'
                                    class="toolTip"
                                    title="${good.name}"
                                    alt="${good.name}"
                                    border=0 width=24></th>
                        </c:when>
                        <c:otherwise>
                            <th align="center" valign="top" width="24" class="warehouse"><img
                                    src='http://static.eaw1805.com/images/goods/good-${good.goodId}.png'
                                    class="toolTip"
                                    title="${good.name}"
                                    alt="${good.name}"
                                    border=0 width=24></th>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${citiesAfrica}" var="city">
                <tr class="warehouse">
                    <td align="left" class="warehouse">${city.name}</td>
                    <td align="center" class="warehouse">${city.position.toString()}</td>
                    <c:forEach items="${city.goodsTradeLvl}" var="good">
                        <c:choose>
                            <c:when test="${good.key == 1}">
                                <td align="right" class="warehouse"><fmt:formatNumber type="number"
                                                                                      maxFractionDigits="0"
                                                                                      groupingUsed="true"
                                                                                      value="${good.value}"/></td>
                            </c:when>
                            <c:otherwise>
                                <td align="center" class="warehouse">
                                    <c:choose>
                                        <c:when test="${good.value == 1}">HD</c:when>
                                        <c:when test="${good.value == 2}">D</c:when>
                                        <c:when test="${good.value == 3}">A</c:when>
                                        <c:when test="${good.value == 4}">S</c:when>
                                        <c:when test="${good.value == 5}">ES</c:when>
                                    </c:choose>
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </td>
</tr>
</table>
</div>
