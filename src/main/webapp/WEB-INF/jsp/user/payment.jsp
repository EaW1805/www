<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="history" scope="request" type="java.util.List<com.eaw1805.data.model.PaymentHistory>"/>


<h1>Payment History<c:if test="${user.userId != userId}"> / ${username} </c:if></h1>
<article>
    <div class="header" style="margin: 5px;">

        <div style="width: 280px;float: left;margin-left: 591px;">
            <h2 style="font-size: 12pt; text-align: center;">
                Account Balance</h2>
        </div>
        <div style="width: 50px;float: left;clear: both;"><h2 style="font-size: 12pt; text-align: center;"></h2></div>
        <div style="width: 150px;float: left;"><h2 style="font-size: 12pt; text-align: center;">Date</h2></div>
        <div style="width: 440px;float: left;"><h2 style="font-size: 12pt; text-align: center;">Reason</h2></div>
        <div style="width: 282px;float: left;">
            <h2 style="font-size: 12pt; text-align: center;width:90px;float:left;">Free</h2>
            <h2 style="font-size: 12pt; text-align: center;width:90px;float:left;">Transferred</h2>
            <h2 style="font-size: 12pt; text-align: center;width:90px;float:left;">Bought</h2>
        </div>
        <div style="width: 50px;float: left;"><h2 style="font-size: 12pt; text-align: center;">Cost</h2></div>

    </div>
    <orderList>
        <ul class="orderList">

            <c:forEach items="${history}" var="payment">
                <li class="orderList">
                    <dl class="orderList">
                        <dt class="orderList" style="width: 577px!important; float: left;">${payment.date}</dt>
                        <dt class="orderList" style="width: 80px!important;float: left;text-align: center;">${payment.creditFree}</dt>
                        <dt class="orderList" style="width: 80px!important;float: left;text-align: center;">${payment.creditTransferred}</dt>
                        <dt class="orderList" style="width: 80px!important;float: left; text-align: center;">${payment.creditBought}</dt>

                        <dt class="orderList" style="width: 60px!important;text-align: center;font-weight: bold;">
                            <c:if test="${payment.chargeBought != 0}">
                                ${payment.chargeBought}
                            </c:if>
                            <c:if test="${payment.chargeFree != 0}">
                                ${payment.chargeFree}
                            </c:if>
                            <c:if test="${payment.chargeTransferred != 0}">
                                ${payment.chargeTransferred}
                            </c:if>
                        </dt>
                    </dl>
                    <dl class="orderList">
                        <ds class="orderList"
                            style="text-align:left; font-size: 12px;margin-left: 130px;width: 450px!important;">${payment.comment}</ds>
                    </dl>
                </li>
            </c:forEach>
        </ul>
    </orderList>
</article>

