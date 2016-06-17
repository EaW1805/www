<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:c="http://www.springframework.org/schema/util"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/util ">
<head>
    <meta name="distribution" content="global"/>
    <meta name="keywords" content="Strategy Diplomacy Napoleonic-Era Napoleon War Empires"/>
    <META NAME="Description" CONTENT="Empires at War 1805 Napoleonic-Era Web-based Strategy Game"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <META http-equiv="content-style-type" content="text/css"/>
    <META http-equiv="imagetoolbar" content="no"/>
    <META name="resource-type" content="document"/>
    <title>Empires at War 1805: Problem Encountered</title>

    <link rel="icon" type="image/ico" href='http://static.eaw1805.com/images/site/eaw1805-favicon.ico'/>

    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/basic.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/widgetkit.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/autocomplete-styles.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/sliderman.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/jquery.qtip.min.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/InfoPanels.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/panels.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/ArmyRelated.css'/>

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
                    $('.pagehead').css({'min-height': (($(window).height()) - 389)});
                }
            });
        });

        $(window).resize(function () {
            $(function () {
                if ($(document).height() <= $(window).height()) {
                    $('.pagehead').css({'min-height': (($(window).height()) - 389)});
                } else if ($('.pagehead').height() >= $('content').height()) {
                    $('.pagehead').css({'min-height': (($(window).height()) - 389)});
                }
            });
        });
    </script>
</head>
<body>
<div id="wrap">
    <div id="main">
        <div id="header">
            <div class="eaw-logo" onclick="window.location='http://www.eaw1805.com/home';"></div>
            <div class="french-soldier"></div>
            <div class="parchment-head"></div>
        </div>
        <div class="site">
            <div class="pagehead">
                <div class="topbox">
                    <ul>
                        <!-- anonymousLayout.listgames.tooltip -->
                        <li><a href="<c:url value="/listgames"/>"
                               title="Active, on-going games"><spring:message
                                code="anonymousLayout.listgames"/></a></li>
                        <li><a href="<c:url value="/scenario/1802/info"/>"
                               title="<spring:message code="baseLayout.scenario.tooltip"/>"><spring:message
                                code="baseLayout.scenario"/></a></li>
                        <security:authorize ifNotGranted="ROLE_ANONYMOUS">
                            <li><a href="http://forum.eaw1805.com"
                                   title="<spring:message code="baseLayout.forum.tooltip"/>"><spring:message
                                    code="baseLayout.forum"/></a></li>
                        </security:authorize>
                        <li><a href="<c:url value="/hallOfFame"/>"
                               title="<spring:message code="baseLayout.halloffame.tooltip"/>"><spring:message
                                code="baseLayout.halloffame"/></a></li>
                        <li><a href="<c:url value="/handbook"/>"
                               title="<spring:message code="baseLayout.help.tooltip"/>"><spring:message
                                code="baseLayout.help"/></a></li>
                    </ul>
                </div>
                <div id="content">
                    <c:if test="${!fn:contains(exception,'Negative Balance') }">
                        <h1 style="font-size: 50px;text-align: center;">This is not the page you are looking for</h1>

                        <h1 style="padding-top: 20px;">error 404</h1>

                        <p>The requested URL caused an internal error on this server.</p>

                        <pre>${exception.message}
                        </pre>

                        <p>Analytical information follows:</p>
                        <pre>
                        <c:forEach var="stackTraceElem" items="${exception.stackTrace}">
                            <c:out value="${stackTraceElem}"/>
                        </c:forEach>
                        </pre>

                        <p>That’s all we know.</p>

                        <h1 style="padding-top: 20px;">Contact &amp; Support</h1>

                        <p class="manual">For bugs reporting, or questions about the rules and the user interface, we
                            suggest that you use our
                            <a href="http://forum.eaw1805.com">forums</a> so that everyone else can read your comments
                            and
                            our
                            replies. It will also help us a great deal if you could use the <strong>Build
                                Number</strong>
                            reported in the
                            <a href="<c:url value="/contact"/>">contact &amp; support</a> page
                            when you report a bug.</p>
                    </c:if>
                    <c:if test="${fn:contains(exception,'Negative Balance') }">

                        <h1 style="font-size: 50px;text-align: center;">Your account has been deactivated</h1>

                        <h1 style="padding-top: 20px;">Negative Balance</h1>

                        <p>Our records indicate that you have a negative balance.</p>

                        <p>Click <a style="font-size:14px;" href="<c:url value="/settings"/>">here</a> to buy more
                            credits in order to activate your account.</p>

                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="footer">
    <div class="footer_upper">
        <div class="grass"></div>
        <div class="site">
            <div class="parchment-footer"></div>
            <div class="eaw-logo" onclick="window.location='http://www.eaw1805.com/home';"></div>
            <div class="footer_nav">
                <ul class="footer_nav">
                    <li><a href="<c:url value="/features"/>"
                           title="<spring:message code="baseLayout.status.tooltip"/>"><spring:message
                            code="baseLayout.status"/></a></li>
                    <li><a href="<c:url value="/news"/>"
                           title="<spring:message code="anonymousLayout.news.tooltip"/>"><spring:message
                            code="anonymousLayout.news"/></a></li>
                    <li><a href="<c:url value="/contact"/>"
                           title="<spring:message code="anonymousLayout.contact.tooltip"/>"><spring:message
                            code="anonymousLayout.contact"/></a></li>
                    <li><a href="<c:url value="/about"/>"
                           title="<spring:message code="baseLayout.about.tooltip"/>"><spring:message
                            code="baseLayout.about"/></a></li>
                </ul>
            </div>
            <div class="footer_nav2">
                <ul class="footer_nav">
                    <li><a href="<c:url value="/help/introduction"/>"
                           title="<spring:message code="handbook.introduction.tooltip"/>"><spring:message
                            code="handbook.introduction"/></a></li>
                    <li><a href="<c:url value="/handbook"/>"
                           title="<spring:message code="footer.handbook"/>"><spring:message
                            code="footer.handbook"/></a></li>
                    <li style="height: 20px; ">&nbsp;</li>
                    <li><a href="<c:url value="/joingame"/>"
                           title="<spring:message code="baseLayout.joingame.tooltip"/>"><spring:message
                            code="baseLayout.joingame"/></a></li>
                </ul>
            </div>
            <div class="footer_nav3">
                <ul class="footer_nav">
                    <li><a href="http://igg.me/p/247705?a=1582854" target="_blank"
                           style="text-decoration:none"><img height=20 style="vertical-align:middle;"
                                                             src="http://www.oplongames.com/sites/default/files/social/icon-indiegogo.jpg">
                        Support us</a></li>
                    <li><a href="http://www.facebook.com/empires1805" target="_blank" style="text-decoration:none"><img
                            height=20 style="vertical-align:middle;"
                            src="http://www.oplongames.com/sites/default/files/social/icon-facebook.png"> Like us</a>
                    </li>
                    <li><a href="http://twitter.com/eaw1805" target="_blank" style="text-decoration:none"><img height=20
                                                                                                               style="vertical-align:middle;"
                                                                                                               src="http://www.oplongames.com/sites/default/files/social/icon-twitter.png">
                        Follow us</a></li>
                    <li><a href="http://www.linkedin.com/company/2624926?trk=tyah" target="_blank"
                           style="text-decoration:none"><img height=20 style="vertical-align:middle;"
                                                             src="http://www.oplongames.com/sites/default/files/social/icon-linkedin.png">
                        Connect with us</a></li>
                    </li>
                </ul>
            </div>
            <div class="oplon-logo" onclick="window.open('http://www.oplongames.com','_blank');"></div>
            <div class="footer_low">
                <p>&copy; 2011-2014 Copyright <a
                        href="http://www.oplongames.com" target="_blank"><img
                        src="http://www.oplongames.com/sites/all/themes/theme621/images/footer_logo.png"
                        height=24
                        style="margin-bottom: -8px;"
                        alt="Oplon Games">&nbsp;Oplon Games</a>
                    &nbsp;-&nbsp;
                    All rights reserved - Funded by <a
                            href="http://www.taneo.gr" target="_blank"><img
                            src="http://static.eaw1805.com/images/site/taneo.png"
                            style="margin-bottom: -4px;"
                            alt="TANEO Venture Capital"></a>
                    &nbsp;-&nbsp;
                    <a href='<c:url value="/terms"/>'>Terms of Service</a>
                </p>
            </div>
        </div>
    </div>
</div>
<div id="tooltip-container" style="display: none;position: fixed; left: 0px;top: 0px; z-index: 65500;"></div>
</body>
</html>
