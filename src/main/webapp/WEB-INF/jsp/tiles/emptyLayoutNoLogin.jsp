<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<jsp:useBean id="user" scope="request" type="com.eaw1805.data.model.User"/>
<jsp:useBean id="unreadMessagesCount" scope="request" class="java.lang.Integer"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:c="http://www.springframework.org/schema/util"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/util ">
<head>
    <META NAME="AUTHOR" CONTENT="Oplon Games">
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
    <META NAME="COPYRIGHT" CONTENT="&copy; 2011-2014 Oplon Games">
    <META HTTP-EQUIV="EXPIRES" CONTENT="0">
    <META NAME="ROBOTS" CONTENT="INDEX,FOLLOW">
    <meta name="distribution" content="global"/>
    <meta name="keywords" content="Strategy Diplomacy Napoleonic-Era Napoleon War Empires"/>
    <META NAME="Description" CONTENT="Empires at War 1805 Napoleonic-Era Web-based Strategy Game"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <META http-equiv="content-style-type" content="text/css"/>
    <META http-equiv="imagetoolbar" content="no"/>
    <META name="resource-type" content="document"/>
    <title>Empires at War 1805: <tiles:insertAttribute name="title" ignore="true"/></title>

    <link rel="icon" type="image/ico" href='http://static.eaw1805.com/images/site/eaw1805-favicon.ico'/>

    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/basic.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/widgetkit.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/autocomplete-styles.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/sliderman.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/jquery.qtip.min.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/InfoPanels.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/panels.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/ArmyRelated.css'/>
</head>
<body>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery-1.6.3.min.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.qtip.min.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.autocomplete.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/qtip-init.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.ui.core.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.ui.widget.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.ui.mouse.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.ui.draggable.js'></script>

<script type="text/javascript">
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-24086810-1']);
    _gaq.push(['_trackPageview']);

    (function () {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();
</script>

<script type="text/javascript">
    $(document).ready(function () {
        $(function () {
            if ($(document).height() <= $(window).height()) {
                $('.pagehead').css({ 'min-height':(($(window).height()) - 371)});
            }
        });
    });

    $(window).resize(function () {
        $(function () {
            if ($(document).height() <= $(window).height()) {
                $('.pagehead').css({ 'min-height':(($(window).height()) - 371)});
            } else if ($('.pagehead').height() >= $('content').height()) {
                $('.pagehead').css({ 'min-height':(($(window).height()) - 371)});
            }
        });
    });
</script>
<div id="wrap">
    <div id="mainClear">
        <div id="header" style="height: 50px;">
            <div class="no-logo-clear"
                 style="height: 10px;
                        margin-top: 20px;"></div>
        </div>
        <div class="site">
            <%--
                        <div id="leftSoldier" style="
                            background: url('http://static.eaw1805.com/images/site/MainPageFrenchSoldierGrass.png') 27px 0 no-repeat;
                            position: relative;
                            height: 596px;
                            margin-bottom: -740px;
                            background-size: 554px;
                            margin-left: -430px;
                            width: 600px;"></div>
            --%>

            <div class="emptyhead">
                <div class="topbox" style="margin-top: -69px; margin-left: -30px;">
                    <ul>
                        <li><a href="<c:url value="/listgames"/>"
                               title="<spring:message code="anonymousLayout.listgames.tooltip"/>"><spring:message
                                code="anonymousLayout.listgames"/></a></li>
                        <li><a href="<c:url value="/scenario/list"/>"
                               title="<spring:message code="baseLayout.scenario.tooltip"/>"><spring:message
                                code="baseLayout.scenario"/></a></li>
                        <li><a href="http://forum.eaw1805.com"
                               title="<spring:message code="baseLayout.forum.tooltip"/>"><spring:message
                                code="baseLayout.forum"/></a></li>
                        <li><a href="<c:url value="/hallOfFame"/>"
                               title="<spring:message code="baseLayout.halloffame.tooltip"/>"><spring:message
                                code="baseLayout.halloffame"/></a></li>
                        <li><a href="<c:url value="/help"/>"
                               title="<spring:message code="baseLayout.help.tooltip"/>"><spring:message
                                code="baseLayout.help"/></a></li>
                    </ul>
                </div>
                <tiles:insertAttribute name="header"/>
                <tiles:insertAttribute name="menu"/>
                <div id="content"
                     style="padding-left: 44px;padding-right: 60px;padding-top: 55px;overflow: auto;">
                    <tiles:insertAttribute name="body"/>
                </div>
            </div>
            <%--            <div id="rightSoldier" style="background: url('http://static.eaw1805.com/images/site/MainPageBritishSoldierGrass.png') 27px 0 no-repeat;
    height: 596px;width: 203px;background-size: 777px;margin-left: 740px;  padding-right: 500px;margin-top: 139px;"></div>--%>
        </div>
    </div>
</div>
<tiles:insertAttribute name="footer"/>
<div id="tooltip-container" style="display: none;position: fixed; left: 0px;top: 0px;"></div>
</body>
</html>
