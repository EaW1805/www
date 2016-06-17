<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
<jsp:useBean id="treasury" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="gameDate" scope="request" type="java.lang.String"/>
</div>

<div style="z-index: 2; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;">
    <h2 style="padding-left: 60px;">${gameDate}</h2>
</div>

<div id="main-article-top"
     style="z-index: 2; position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 20px;">National Treasury</h2>
</div>

<div style="z-index: 1; position: relative; margin: -10px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">

    <table style="background: none; border: none; border-style: hidden; margin-top: 0px; margin-left: 50px; box-shadow: 0 0 0 0;">
        <tr class="treasury">
            <th align="center" width="36" class="treasury"><img src='http://static.eaw1805.com/images/goods/good-1.png'
                                                                alt="Available Money"
                                                                class="toolTip"
                                                                title="Available Money"
                                                                border=0 width=36></th>
            <th align="right" class="treasury"><fmt:formatNumber type="number" maxFractionDigits="0" groupingUsed="true"
                                                                 value="${treasury}"/></th>
        </tr>
    </table>

</div>

<div style="z-index: 2; position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 20px;">Regional Warehouses</h2>
</div>

<div style="z-index: 1; position: relative;margin: -10px 0px 0px -35px;width: 990px; overflow: visible; padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">


    <table style="background: none; border: none; border-style: hidden; margin-top: 0px; box-shadow: 0 0 0 0;">

        <tr class="warehouse">
            <th align="left" width="38" class="warehouse">&nbsp;</th>
            <c:forEach items="${regionList}" var="thisRegion">
                <c:if test="${thisRegion.id > 0}">
                    <th class="warehouse">${thisRegion.name}</th>
                </c:if>
            </c:forEach>
        </tr>
        <c:forEach items="${goodList}" var="good">
            <tr class="warehouse">
                <th align="center" width="38" class="warehouse"><img
                        src='http://static.eaw1805.com/images/goods/good-${good.goodId}.png'
                        class="toolTip"
                        title="${good.name}"
                        alt="${good.name}"
                        border=0 width=24></th>
                <c:forEach items="${warehouseList}" var="warehouse">
                    <td class="warehouse"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                            groupingUsed="true"
                                                            value="${warehouse.storedGoodsQnt[good.goodId]}"/></td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>
</div>
