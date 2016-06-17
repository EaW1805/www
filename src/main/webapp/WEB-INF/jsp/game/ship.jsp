<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<jsp:useBean id="goodList" scope="request" type="java.util.List<com.eaw1805.data.model.economy.Good>"/>
<jsp:useBean id="sortedOrdersList" scope="request" type="java.util.List<com.eaw1805.data.model.PlayerOrder>"/>
<jsp:useBean id="ship" scope="request" type="com.eaw1805.data.model.fleet.Ship"/>

<h2 style="padding-left: 40px; padding-top: 20px;">Ship ${ship.name} Balance Sheet</h2>
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
            <td class="warehouse-activity">${initialState[good.goodId]}</td>
        </c:forEach>
    </tr>

    <c:forEach items="${sortedOrdersList}" var="order">
        <tr class="warehouse-activity">
            <td class="warehouse-activity">
                ${order.explanation}
            </td>
            <c:forEach items="${goodList}" var="good">
                <td class="warehouse-activity">${orderToShipWarehouse[order.orderId][good.goodId]}</td>
            </c:forEach>
        </tr>
    </c:forEach>
    <tr class="warehouse-activity">
        <td class="warehouse-activity">Available at the end of the month</td>
        <c:forEach items="${goodList}" var="good">
            <td class="warehouse-activity">${finalState[good.goodId]}</td>
        </c:forEach>
    </tr>
</table>
