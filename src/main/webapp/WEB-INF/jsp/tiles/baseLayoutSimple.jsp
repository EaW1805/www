<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
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

    <link rel="icon" type="image/ico" href='http://static.eaw1805.com/images/site/eaw1805-favicon.ico'/>
    <title>Empires at War 1805: <tiles:insertAttribute name="title" ignore="true"/></title>

    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/basic.css'/>

    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/widgetkit.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/sliderman.css'/>
</head>
<body>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.cookie.js'></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/aplweb.scrollbars.js"></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/jquery.event.drag-2.0.min.js"></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/jquery.ba-resize.min.js"></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/mousehold.js"></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.blockUI.js'></script>
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
        $.blockUI.defaults.applyPlatformOpacityRules = false;
        $(function () {
            if ($(document).height() <= $(window).height()) {
                $('.pagehead').css({ 'min-height':(($(window).height()) - 389)});
            }
        });
    });

    $(window).resize(function () {
        $(function () {
            if ($(document).height() <= $(window).height()) {
                $('.pagehead').css({ 'min-height':(($(window).height()) - 389)});
            } else if ($('.pagehead').height() >= $('content').height()) {
                $('.pagehead').css({ 'min-height':(($(window).height()) - 389)});
            }
        });
    });
</script>

<div id="wrap">
    <div id="main">
        <div id="header" class="true">
            <a class="logo" href="<c:url value="/home"/>">
                <img alt="eaw1805" class="default"
                     src="http://static.eaw1805.com/images/eaw1805-mini.png" height="50">
                <img alt="eaw1805" class="hover"
                     src="http://static.eaw1805.com/eaw1805-anim.gif" height="50">
            </a>

            <div class="userbox">
                <security:authorize ifNotGranted="ROLE_ANONYMOUS">
                    <div class="avatarname">
                        <a href="<c:url value="/games"/>"><img
                                src="https://secure.gravatar.com/avatar/${user.emailEncoded}?s=20&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                alt="" height="20" width="20"></a>
                        <a href="<c:url value="/games"/>"
                           class="name"
                           title="<spring:message code="baseLayout.user.tooltip"/>"><security:authentication
                                property='principal.username'/></a>
                    </div>
                    <ul class="usernav">
                        <li>
                            <a href="<c:url value="/inbox"/>"
                               title="<spring:message code="baseLayout.inbox.tooltip"/>"><spring:message
                                    code="baseLayout.inbox"/></a>
                            <a href="<c:url value="/inbox"/>"
                               class="unread_count notifications_count new tooltipped downwards js-notification-count"
                               title="<spring:message code="baseLayout.unread.tooltip"/>">0</a>
                        </li>
                        <li>
                            <a href="<c:url value="/honour"/>"
                               title="<spring:message code="baseLayout.honour.tooltip"/>"><spring:message
                                    code="baseLayout.honour"/></a></li>
                        <li>
                            <a href="<c:url value="/settings"/>"
                               title="<spring:message code="baseLayout.settings.tooltip"/>"><spring:message
                                    code="baseLayout.settings"/></a></li>
                        <li><a href="<c:url value="/logout"/>"
                               title="<spring:message code="baseLayout.logout.tooltip"/>"><spring:message
                                code="baseLayout.logout"/></a></li>
                    </ul>
                </security:authorize>
                <security:authorize ifAnyGranted="ROLE_ANONYMOUS">
                    <li class="first"><a href="<c:url value="/signup"/>"
                                         title="<spring:message code="anonymousLayout.signup.tooltip"/>"><spring:message
                            code="anonymousLayout.signup"/></a></li>
                    <ul class="usernav">
                        <li><a href="<c:url value="/listgames"/>"
                               title="<spring:message code="anonymousLayout.listgames.tooltip"/>"><spring:message
                                code="anonymousLayout.listgames"/></a></li>
                        <li><a href="<c:url value="/features"/>"
                               title="<spring:message code="anonymousLayout.features.tooltip"/>"><spring:message
                                code="anonymousLayout.features"/></a></li>
                        <li><a href="<c:url value="/login"/>"
                               title="<spring:message code="anonymousLayout.login.tooltip"/>"><spring:message
                                code="anonymousLayout.login"/></a></li>
                    </ul>
                </security:authorize>
            </div>
            <div class="topbox">
                <ul>
                    <li><a href="<c:url value="/scenario/list"/>"
                           title="<spring:message code="baseLayout.scenario.tooltip"/>"><spring:message
                            code="baseLayout.scenario"/></a></li>
                    <li><a href="<c:url value="/hallOfFame"/>"
                           title="<spring:message code="baseLayout.halloffame.tooltip"/>"><spring:message
                            code="baseLayout.halloffame"/></a></li>
                    <li><a href="<c:url value="/about"/>"
                           title="<spring:message code="baseLayout.about.tooltip"/>"><spring:message
                            code="baseLayout.about"/></a></li>
                </ul>
            </div>
        </div>

        <div class="site">
            <div class="pagehead">
                <div class="body">
                    <tiles:insertAttribute name="header"/>
                    <tiles:insertAttribute name="menu"/>
                </div>

                <div class="content">
                    <tiles:insertAttribute name="body"/>
                </div>
            </div>
            <div class="push"></div>
        </div>
    </div>
</div>
<tiles:insertAttribute name="footer"/>
</body>
</html>
