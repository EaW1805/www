<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="news" scope="request" type="java.util.List<java.lang.String>"/>


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
    #header .parchment-head {
        position: relative;
        margin: 0;
        width: 0;
        height: 0;
        padding: 0;
        background: none;
    }
    span.logout-button-icon button {
        margin-top: -126px;
    }

    .loginbox {
        float: right;
        clear: right;
        margin-top: -127px;
        margin-right: 58px;
        display: block;
    }

    .userbox {
        margin-top: -127px;
        margin-right: 92px;
    }
    .topbox {
        margin-top: -133px;
    }

    #footer .parchment-footer {
        background: none;
    }
</style>

<c:forEach items="${news}" var="new">
    <div style=" position: relative;margin: 0px 0px 0px -35px;width: 1000px;min-height: 360px;padding: 0px 40px;
background: url('http://static.eaw1805.com/images/site/NewsParchment.png') repeat-y scroll 0px 0px transparent;clear: both;
background-size: 1035px 360px;">
        <div style="padding-top:20px; margin-left: -20px; width:990px;"> ${new}
        </div>
    </div>
</c:forEach>