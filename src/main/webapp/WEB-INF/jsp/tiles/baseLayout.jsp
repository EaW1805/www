<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="java.util.concurrent.TimeUnit" %>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<jsp:useBean id="user" scope="request" type="com.eaw1805.data.model.User"/>
<jsp:useBean id="unreadMessagesCount" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="userNewAchievements" scope="request" type="java.util.List<com.eaw1805.data.model.Achievement>"/>

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
    <meta name="keywords"
          content="eaw1805, eaw 1805, empires at war, empires at war 1805, Strategy Diplomacy Napoleonic-Era Napoleon War Empires"/>
    <META NAME="Description" CONTENT="Empires at War 1805 Napoleonic-Era Web-based Strategy Game"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <META http-equiv="content-style-type" content="text/css"/>
    <META http-equiv="imagetoolbar" content="no"/>
    <META name="resource-type" content="document"/>
    <title>Empires at War 1805:
        <c:choose>
            <c:when test="${titleOverride == 1}">
                ${title}
            </c:when>
            <c:otherwise>
                <tiles:insertAttribute name="title" ignore="true"/>
            </c:otherwise>
        </c:choose>
    </title>
    <link rel="icon" type="image/ico" href='http://static.eaw1805.com/images/site/eaw1805-favicon.ico'/>
    <link rel="stylesheet" type="text/css" href='http://direct.eaw1805.com/style/basic.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/widgetkit.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/autocomplete-styles.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/sliderman.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/jquery.qtip.min.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/InfoPanels.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/panels.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/ArmyRelated.css'/>
    <link rel="stylesheet" type="text/css" href="http://static.eaw1805.com/style/scrollbars.css"/>
    <style type="text/css">
        .scrollable .scrollcontent {
            bottom: 5px;
        }

        div#chatPanel.chatPanel div#chatContentPanel.scrollable.no_scroll_v div.scrollcontent {
            bottom: 12px !important;
            height: 279px;
            width: 364px;
        }

        div#chatPanel.chatPanel div#chatContentPanel.scrollable div.scrollcontent {
            bottom: 12px !important;
            height: 275px;
            width: 364px;
        }

        .higher-zindex {
            z-index: 10000000 !important;
        }
    </style>
</head>
<body>
<%--<script type="text/javascript" src='http://static.eaw1805.com/js/jquery-1.6.3.min.js'></script>--%>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<%----%>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
<%--<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>--%>
<script type="text/javascript">
    var jQuery_1_8 = jQuery.noConflict(true);
    var jQueryOriginal = jQuery || jQuery_1_8;
    window.jQuery = window.$ = jQueryOriginal;

</script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.qtip.min.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.autocomplete.js'></script>
<script type="text/javascript" src='http://direct.eaw1805.com/js/qtip-init.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.ui.core.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.ui.widget.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.ui.mouse.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.ui.draggable.js'></script>

<script type="text/javascript" src="http://static.eaw1805.com/js/aplweb.scrollbars.js"></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/jquery.event.drag-2.0.min.js"></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/jquery.ba-resize.min.js"></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/mousehold.js"></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.cookie.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.blockUI.js'></script>

<script type="text/javascript">


    $(document).ready(function () {
        $.blockUI.defaults.applyPlatformOpacityRules = false;
        $("#playersPanel").scrollbars();
        <security:authorize ifNotGranted="ROLE_ANONYMOUS">

        $("#chatContentPanel").scrollbars();

        </security:authorize>
    });
</script>
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
    if ($.cookie('isPlayersPanelOpen') == null) {
        $.cookie('isPlayersPanelOpen', '0', { expires:7, path:'/', domain:'.eaw1805.com' });
    }

    <security:authorize ifNotGranted="ROLE_ANONYMOUS">
    if ($.cookie('isChatOpen') == null) {
        $.cookie('isChatOpen', '0', { expires:7, path:'/', domain:'.eaw1805.com' });
    }
    </security:authorize>

    $(document).ready(function () {
        $(function () {
            var width = 389;
            if ($('.pagehead').css('background-image') == 'none') {
                width = 344;
            }
            if ($(document).height() <= $(window).height()) {
                $('.pagehead').css({ 'min-height':(($(window).height()) - width)});
            }
        });
        setPlayersPanelPosition();
        <security:authorize ifNotGranted="ROLE_ANONYMOUS">
        $("#chatContentPanel").find('.scrollcontent').scrollTop(5000000);
        setChatPanelPosition();
        </security:authorize>
    });

    $(window).resize(function () {
        $(function () {
            var width = 389;
            if ($('.pagehead').css('background-image') == 'none') {
                width = 344;
            }
            if ($(document).height() <= $(window).height()) {
                $('.pagehead').css({ 'min-height':(($(window).height()) - width)});
            } else if ($('.pagehead').height() >= $('content').height()) {
                $('.pagehead').css({ 'min-height':(($(window).height()) - width)});
            }
        });
        setPlayersPanelPosition();
        <security:authorize ifNotGranted="ROLE_ANONYMOUS">
        setChatPanelPosition();
        </security:authorize>
    });

    function setPlayersPanelPosition() {
        var isPlayersPanelOpen = false;
        if ($.cookie('isPlayersPanelOpen') != null) {
            if ($.cookie('isPlayersPanelOpen') == 1) {
                isPlayersPanelOpen = true;
            }
        }
        var wid = 40;
        if (isPlayersPanelOpen) {
            wid = 250;
        }
        $('#pPanel').css({ 'top':(($(window).height()) - 441)});
        if ($('#pPanel').css('left') != $('body').innerWidth() - wid) {
            $('#pPanel').css({ 'left':($('body').innerWidth() - wid)});
        }
    }

    function setChatPanelPosition() {
        var isChatOpen = false;
        if ($.cookie('isChatOpen') != null) {
            if ($.cookie('isChatOpen') == 1) {
                isChatOpen = true;
            }
        }
        var wid = 40;
        if (isChatOpen) {
            wid = 417;
        }
        if ($('#chatPanel').css('left') != $('body').innerWidth() - wid) {
            $('#chatPanel').css({ 'left':($('body').innerWidth() - wid)});
        }
    }

    function togglePlayersPanelPosition() {
        var isPlayersPanelOpen = false;
        if ($.cookie('isPlayersPanelOpen') != null) {
            if ($.cookie('isPlayersPanelOpen') == 1) {
                isPlayersPanelOpen = true;
            }
        }

        if (isPlayersPanelOpen) {
            $('#pPanel').animate({'left':'+=212px'}, 'slow');
            $.cookie('isPlayersPanelOpen', '0', { expires:7, path:'/', domain:'.eaw1805.com' });
        } else {
            $('#pPanel').animate({'left':'-=212px'}, 'slow');
            $.cookie('isPlayersPanelOpen', '1', { expires:7, path:'/', domain:'.eaw1805.com' });
        }
    }

    function toggleChatPanelPosition() {
        var isChatOpen = false;
        if ($.cookie('isChatOpen') != null) {
            if ($.cookie('isChatOpen') == 1) {
                isChatOpen = true;
            }
        }

        if (isChatOpen) {
            $('#chatPanel').animate({'left':'+=378px'}, 'slow');
            $.cookie('isChatOpen', '0', { expires:7, path:'/', domain:'.eaw1805.com' });
        } else {
            $('#chatPanel').animate({'left':'-=378px'}, 'slow');
            $.cookie('isChatOpen', '1', { expires:7, path:'/', domain:'.eaw1805.com' });
            try {
                clearInterval(indicatorTImer);
                var src = $("#widget-105").attr("src");
                if (src.indexOf("Hover") != -1) {
                    $("#widget-105").attr("src", "http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchOff.png");
                }
            } catch (e) {
                //eat it
            }
        }
    }
</script>
<div id="wrap">
<div id="main">
<div id="header">
    <div class="eaw-logo" onclick="window.location='http://www.eaw1805.com/home';"></div>
    <div class="french-soldier"></div>
    <div class="parchment-head"></div>
</div>
<security:authorize ifNotGranted="ROLE_ANONYMOUS">
    <div id="chatPanel" class="chatPanel"
         style="overflow: hidden; width: 417px; height: 437px;  top: -8px;   left: 3000px;
             position: fixed; overflow: hidden; z-index: 900;">

        <input id="inputChatBox"
               style="width: 347px;height: 29px;position: absolute;left: 54px;top: 376px;background: transparent;border: 0;border-color: transparent;"
               type="text">

        <img style="position: absolute; left: 23px; top: 18px;" id="widget-105" class="gwt-Image"
             onclick="toggleChatPanelPosition();"
             src="http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchOff.png">

        <div id="chatContentPanel" style="width: 354px;height: 292px;position: absolute;left: 54px;top: 82px">
                <%--<jsp:useBean id="globalMessages" scope="request"
                type="java.util.LinkedList<com.eaw1805.data.dto.web.ChatMessageDTO>"/>--%>
            <c:set var="isFisrt" value="1"/>
            <c:set var="currentUser" value="0"/>
            <c:set var="showDate" value="1" scope="page"/>
            <c:set var="currentDate"/>

            <c:forEach items="${globalMessages}" var="globalMessage">
                <table class="chatMessage" id="${globalMessage.username}_${globalMessage.time}" cellspacing="0"
                       cellpadding="0" style="width: 334px; ">
                    <tbody>
                    <c:if test="${currentUser != 0 && currentUser!= globalMessage.username}">
                        <div style="margin-left: 4px;width: 338px;margin-bottom: 3px;">
                            <hr>
                        </div>
                    </c:if>

                    <tr>
                        <td align="left" style="vertical-align: top; " width="30px">
                            <c:if test="${currentUser == 0 || currentUser!= globalMessage.username}">
                                <div style="position: relative; overflow: hidden; width: 30px; height: 30px; ">
                                    <a href='<c:url value="/user/${globalMessage.username}"/>'>
                                        <img src="https://secure.gravatar.com/avatar/${globalMessage.encodedEmail}?s=38&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                             alt="" height="29" width="29" title="${globalMessage.username}"
                                             class="pointer"
                                             style="width: 29px; position: absolute; left: 0px; top: 0px; border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;">
                                    </a>
                                </div>
                            </c:if>
                        </td>
                        <td align="left" style="vertical-align: top; ">
                            <table cellspacing="0" cellpadding="0">
                                <tbody>
                                <tr>
                                    <td align="left" style="vertical-align: top; ">
                                        <div class="gwt-HTML"><span
                                                style="color:brown">
                                        <c:if test="${isFisrt != 1}">
                                            <c:set var="thisDate" value="${globalMessage.time}" scope="page"/>
                                            <%
                                                final TimeUnit timeUnit = TimeUnit.MINUTES;
                                                final long thisDate = (Long) pageContext.getAttribute("thisDate");
                                                final long currentDate = (Long) pageContext.getAttribute("currentDate");
                                                final long diffInMillies = thisDate - currentDate;
                                                final SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");

                                                final String timezone;
                                                if (user.getTimezone() < 0) {
                                                    timezone = "GMT" + (int) user.getTimezone();
                                                } else {
                                                    timezone = "GMT+" + (int) user.getTimezone();
                                                }
                                                ft.setTimeZone(TimeZone.getTimeZone(timezone));

                                                if (timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS) > 5) {
                                                    pageContext.setAttribute("showDate", 1);

                                                    pageContext.setAttribute("thisDate", ft.format(new Date(thisDate)));
                                                } else {
                                                    pageContext.setAttribute("showDate", 0);
                                                    pageContext.setAttribute("thisDate", ft.format(new Date(thisDate)));
                                                }

                                            %>
                                        </c:if>
                                        <c:if test="${isFisrt == 1}">
                                            <c:set var="thisDate" value="${globalMessage.time}" scope="page"/>
                                            <%
                                                final long thisDate = (Long) pageContext.getAttribute("thisDate");
                                                final SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");

                                                final String timezone;
                                                if (user.getTimezone() < 0) {
                                                    timezone = "GMT" + (int) user.getTimezone();
                                                } else {
                                                    timezone = "GMT+" + (int) user.getTimezone();
                                                }
                                                ft.setTimeZone(TimeZone.getTimeZone(timezone));

                                                pageContext.setAttribute("showDate", 1);
                                                pageContext.setAttribute("thisDate", ft.format(new Date(thisDate)));
                                            %>
                                        </c:if>

                                        <c:if test="${showDate == 1 || globalMessage.username != currentUser}">
                                            <a style="color:#654900;"
                                               href='http://www.eaw1805.com/user/${globalMessage.username}'
                                               title="${globalMessage.username}">
                                                    ${globalMessage.username}</a> @ ${thisDate}<br>
                                        </c:if>
                                        </span> ${globalMessage.message}
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    </tbody>

                </table>

                <c:set var="currentUser" value="${globalMessage.username}"/>
                <c:set var="currentDate" value="${globalMessage.time}"/>
                <c:set var="isFisrt" value="0"/>
            </c:forEach>
        </div>
        <table style="position: absolute; left: 71px; top: 12px;" cellpadding="0" cellspacing="0">
            <tbody>
            <tr>
                <td style="vertical-align: top;" align="left">
                    <table cellpadding="0" cellspacing="0">
                        <tbody>
                        <tr>
                            <td style="vertical-align: top;" align="left">
                                <div style="width: 320px; height: 50px;" class="clearFont whiteText">
                                    <div style="position: relative; overflow: hidden; width: 320px; height: 50px;">
                                        <img
                                                style=" height: 50px; position: absolute; left: 3px; top: 0;"
                                                class="gwt-Image"
                                                src="http://static.eaw1805.com/images/panels/chat/ButGlobalChat.png">
                                    </div>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div id="userGames_container">
            <%--this will be filled in with ajax call--%>
    </div>
    <div id="achievement_container">
        <%--this will be filled in with ajax call--%>
    </div>
    <script type="text/javascript">

        var currentUserGameIndex = 0;
        var processingAnim = false;
        function retrieveUserGameResults() {
            <c:url var="thisUrl" value="/"/>
            var url = "${thisUrl}" + "userGamesResult/retrieve?t=" + (new Date().getTime());
            $.ajax({
                url:url,
                type:'get',
                dataType:"html"
            }).done(function (data) {
                        $("#userGames_container").html(data);
                        showUserGameResult();
                    });
        }

        function showUserGameResult() {
            if($("#userGameResult_container_" + currentUserGameIndex).length == 0) {
                //then start showing the achievements. oeo.
                showAchievement();
                showAchievement();
                showAchievement();
                return;
            }
            processingAnim = true;
            $("#userGameResult_container_" + currentUserGameIndex).css("top", Math.max(0, ($(window).height() - 496) / 2) + "px");
            $("#userGameResult_container_" + currentUserGameIndex).css("left", Math.max(0, ($(window).width() - 752) / 2) + "px");
            $("#userGameResult_container_" + currentUserGameIndex).fadeIn( 2000, function() {
                processingAnim = false;
                window.setTimeout("hideAchievement()", 15000);
                window.setTimeout("markResultAsViewed(" + $("#userGameResult_container_" + currentUserGameIndex).attr("scenario") +", " + $("#userGameResult_container_" + currentUserGameIndex).attr("userGame") + ")", 3000);
            });
        }

        function hideUserGameResult() {
            //you can't close the window durring a fadein our fadeout effect. Will break things if happens...
            if (processingAnim) {
                return;
            }
            processingAnim = true;
            $("#userGameResult_container_" + currentUserGameIndex).animate({
                opacity: 0
            }, 2000, function() {
                $("#userGameResult_container_" + currentUserGameIndex).css("display", "none");
                currentUserGameIndex++;
                showUserGameResult();
                processingAnim = false;

            });
        }

        function markResultAsViewed(scenario, resId) {
            <c:url var="thisUrl" value="/"/>
            var url = "${thisUrl}" + "scenario/" + scenario + "/gameResult/" + resId + "/viewed?t=" + (new Date().getTime());
            $.ajax({
                url:url,
                type:'get',
                dataType:"html"
            }).done(function (data) {
                //nothing to do here
            });
        }





        var achievementIds = [];
        var curAchivement = 0;
        var currentVisible = [];

        function retrieveAchievements() {
        <c:url var="thisUrl" value="/"/>
            var url = "${thisUrl}" + "achievements/retrieve?t=" + (new Date().getTime());
            $.ajax({
                url:url,
                type:'get',
                dataType:"html"
            }).done(function (data) {
                        $("#achievement_container").html(data);
                        var countAch = 0;
                        $(".achievement_viewer").each(function() {
                            achievementIds[countAch] = parseInt($(this).attr("achievement"));
                            countAch++;
                        });

                    });
        }

        function showAchievement() {

            if (curAchivement >= achievementIds.length) {
                return;
            }
            var localAchievement = achievementIds[curAchivement];
            currentVisible[currentVisible.length] = localAchievement;
            $("#achievement_" + localAchievement).css("top" , ($(window).height() - (100 + (currentVisible.length - 1)*70)) +"px");
            $("#achievement_" + localAchievement).fadeIn( 2000, function() {
                window.setTimeout("hideAchievement()", 15000);
                window.setTimeout("markAsViewed(" + localAchievement + ")", 3000);

            });
            curAchivement++;
        }

        function hideAchievement() {

            if (currentVisible.length == 0) {
                return;
            }
            var localAchievement = currentVisible[0];
            $( "#achievement_" + localAchievement).animate({
                opacity: 0
            }, 2000, function() {
                if (curAchivement == achievementIds.length) {

                }
                showAchievement();

            });
            currentVisible.splice(0, 1 );
            for (var index in currentVisible) {
                $( "#achievement_" + currentVisible[index]).animate({
                    top: "+=70"
                }, 2000, function() {

                });
            }


        }

        function markAsViewed(achId) {
            <c:url var="thisUrl" value="/"/>
            var url = "${thisUrl}" + "achievement/" + achId + "/viewed?t=" + (new Date().getTime());
            $.ajax({
                url:url,
                type:'get',
                dataType:"html"
            }).done(function (data) {
            //nothing to do here
            });
        }

        $(document).ready(function () {
            //retrieve all new achievements
            retrieveUserGameResults();
            retrieveAchievements();
        });

    </script>



</security:authorize>
<div id="pPanel"
     style="position: fixed; overflow: hidden; width: 256px; height: 441px;
             left: 3000px; top: 278px; z-index: 9999999;"
     class="orderSidePanel">

    <div id="activePlayers" class="clearFontMiniTitle whiteText"
         style="position: absolute; left: 60px; top: 18px; color: #ffffff;">
        Active Players: ${fn:length(activeUsers)}
    </div>
    <img src="http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchOn.png" class="gwt-Image"
         id="widget-107" onclick="togglePlayersPanelPosition();"
         style="position: absolute; left: 19px; top: 412px; ">

    <div id="playersPanel" style="width: 197px;height: 372px;position: absolute;left: 50px;top: 69px;">
        <c:forEach items="${activeUsers}" var="activeUser">
            <div id="active-${activeUser.userId}"
                 style="position: relative; overflow: hidden; width: 170px; height: 45px; background-size:170px 45px;"
                 class="orderInfoPanel">

                <div class="clearFontMini" style="position: absolute; left: 3px; top: 3px; float: left;">
                    <a href='<c:url value="/user/${activeUser.username}"/>'>
                        <img src="https://secure.gravatar.com/avatar/${activeUser.emailEncoded}?s=38&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                             style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                             alt="" height="38" width="38" title="${activeUser.username}">
                    </a>
                </div>
                <div style="float: left; margin-left: 50px; margin-top: 2px;font: small/1.4 helvetica,arial,freesans,clean,sans-serif;">
                        ${activeUser.username}
                </div>
                <div style="clear:both;float: left; margin-left: 50px; margin-top: -2px;font: 8px helvetica,arial,freesans,clean,sans-serif;">
                    Active for:
                </div>
                <c:set var="thisDate" value="${activeUser.loginTime}" scope="page"/>
                <%
                    final TimeUnit timeUnit = TimeUnit.MINUTES;
                    final Date loginTime = (Date) pageContext.getAttribute("thisDate");
                    final long diffInMillies = System.currentTimeMillis() - loginTime.getTime();
                    pageContext.setAttribute("thisDate", timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS));
                %>
                <div style="clear:both;float: left; margin-left: 50px; font: 8px helvetica,arial,freesans,clean,sans-serif;">
                        ${thisDate} minutes
                </div>

                <a href='<c:url value="/user/${activeUser.username}"/>'>
                    <img src="http://static.eaw1805.com/images/buttons/ButMagGlassOff.png"
                         class="pointer" id="widget-704"
                         title="Go to position related to this order"
                         style="width: 20px; height: 20px; position: absolute; left: 147px; top: 22px; ">
                </a>
            </div>
        </c:forEach>
    </div>
</div>

<div class="site">
    <div class="pagehead">
        <div class="topbox">
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
        <security:authorize ifAnyGranted="ROLE_ANONYMOUS">
            <div class="loginbox">
                    <span class="login">
                        <form name="f" action="<c:url value='/j_spring_security_check'/>" method="POST"
                              accept-charset="UTF-8">
                        <span class="username">
                            <input type='text' name='j_username' size="18" tabindex="1" type="text"
                                   value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'
                                   onblur="if(this.value=='') this.value='Username';"
                                   onfocus="if(this.value=='Username') this.value='';"/>
                         </span>
                        <span class="password">
                            <input class="text" name='j_password' size="10" tabindex="2" type="password" value=""/>
                        </span>
                        <span class="login-button-icon">
                            <input name="submit" tabindex="3" type="submit" value=""/>
                        </span>
                        </form>
                    </span>
            </div>
        </security:authorize>
        <security:authorize ifNotGranted="ROLE_ANONYMOUS">
            <div class="userbox">
                <ul class="usernav">
                    <li>
                        <div class="avatarname">
                            <a href="<c:url value="/games"/>"><img
                                    src="https://secure.gravatar.com/avatar/${user.emailEncoded}?s=20&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                    alt="" height="20" width="20"></a>
                            <a href="<c:url value="/games"/>"
                               class="name"
                               title="<spring:message code="baseLayout.user.tooltip"/>"><security:authentication
                                    property='principal.username'/></a>
                        </div>
                    </li>
                    <li>
                        <a href="<c:url value="/inbox"/>"
                           title="<spring:message code="baseLayout.inbox.tooltip"/>"><spring:message
                                code="baseLayout.inbox"/></a>
                        <% if (unreadMessagesCount > 0) { %>
                        <a href="<c:url value="/inbox"/>"
                           class="unread_count"
                           title="<spring:message code="baseLayout.unread.tooltip"/>">${unreadMessagesCount}</a>
                        <% } %>
                    </li>
                    <li>
                        <a href="<c:url value="/honour"/>"
                           title="<spring:message code="baseLayout.honour.tooltip"/>"><spring:message
                                code="baseLayout.honour"/></a></li>
                    <li>
                        <a href="<c:url value="/settings"/>"
                           title="<spring:message code="baseLayout.settings.tooltip"/>"><spring:message
                                code="baseLayout.settings"/></a></li>
                </ul>
            </div>
                    <span class="logout-button-icon">
			        <button value="" name="Submit" type="submit"
                            onclick="window.location.href='<c:url value="/logout"/>'"
                            title="<spring:message code="baseLayout.logout.tooltip"/>"></button>
			        </span>
        </security:authorize>
        <tiles:insertAttribute name="header"/>
        <tiles:insertAttribute name="menu"/>
        <div id="content">
            <tiles:insertAttribute name="submenu"/>
            <tiles:insertAttribute name="body"/>
        </div>
    </div>
</div>
</div>
</div>
<tiles:insertAttribute name="footer"/>
</body>
</html>
<script type="text/javascript">
    var myVar = setInterval(function () {
        $.ajax({
            url:'/activeUsers',
            success:updateActiveUser
        });
    }, 60000);

    function updateActiveUser(data) {
        $(".orderSidePanel .scrollwrap").empty();
        $(".orderSidePanel .scrollwrap").append(data);
        $("#activePlayers").empty();
        $("#activePlayers").append("Active Players: " + $('.orderSidePanel .orderInfoPanel').size());
    }

</script>
<security:authorize ifNotGranted="ROLE_ANONYMOUS">
    <script type="text/javascript">
        var indicatorTImer;
        try {

            var url = "ws://gamekeeper.oplongames.com:8098/chateaw/websocket";

            var ws;
            var myVar;

            function wsopen(event) {
                try {
                    ws.send("subCode:0_0");
                    myVar = setInterval(function () {
                        sendPing()
                    }, 60000);

                } catch (e) {
                    console.log("error while sending message : " + e);
                }
            }

            function wsmessage(event) {
                try {

                    var lastElement = $(".chatMessage")[$(".chatMessage").length - 1].id.split('_');
                    var data = JSON.parse(event.data);

                    //Different User
                    var sameUser = false;
                    var showTime = true;

                    if (data.username == lastElement[0]) {
                        sameUser = true;
                        if (data.time - lastElement[1] < 300000) {
                            showTime = false;
                        }
                    }
                    if (!sameUser) {
                        $(".chatPanel .scrollwrap").append('<div id="hr" style="margin-left: 4px;width: 338px;margin-bottom: 3px;"><hr></div>');
                    }

                    var newMessage = $("#-1").clone();

                    newMessage.addClass('chatMessage');
                    newMessage.attr('id', data.username + '_' + data.time);
                    if (sameUser) {
                        newMessage.find("#imgDiv").remove();
                    } else {
                        newMessage.find("#url").attr('href', newMessage.find("#url").attr('href') + data.username);
                        newMessage.find("#img").attr('src', 'https://secure.gravatar.com/avatar/' + data.encodedEmail + '?s=29&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png');
                        newMessage.find("#img").attr('title', data.username);
                    }
                    if (showTime) {
                        var newDate = new Date(data.time);
                        newMessage.find("#cont").append(
                                '<span style="color:brown"> ' +
                                        '<a style="color:#654900;" href="http://www.eaw1805.com/user/' + data.username + ' " title=" ' + data.username + ' ">' + data.username + '</a> @ ' + jQuery_1_8.datepicker.formatDate('yy-mm-dd', newDate) + getTime(newDate) +
                                        ' <br></span>' + data.message);
                    } else {
                        newMessage.find("#cont").append('<span style="color:brown"></span>' + data.message);
                    }
                    $(".chatPanel .scrollwrap").append(newMessage);
                    newMessage.show();
                    $("#chatContentPanel").find('.scrollcontent').scrollTop(5000000);

                    var isChatOpen = false;
                    if ($.cookie('isChatOpen') != null) {
                        if ($.cookie('isChatOpen') == 1) {
                            isChatOpen = true;
                        }
                    }
                    if (!isChatOpen) {
                        indicatorTImer = setInterval(function () {
                            chatIndicator()
                        }, 500);
                    }

                } catch (e) {
                    alert("on message : " + e);
                }
            }

            function getTime(date) {

                var hours = date.getHours();
                var minutes = date.getMinutes();
                var seconds = date.getSeconds();

                var time = ' ';
                if (hours < 10) {
                    time += '0';
                }
                time += date.getHours() + ':';

                if (minutes < 10) {
                    time += '0';
                }
                time += date.getMinutes() + ':';

                if (seconds < 10) {
                    time += '0';
                }
                time += date.getSeconds();
                return time;
            }

            function wsclose(event) {
                console.log("Shouldn't be printed!!");
                clearInterval(myVar);
                //print a message and try to reconnect
                ws = new WebSocket(url, "EAW1805");
                ws.onopen = wsopen;
                ws.onmessage = wsmessage;
                ws.onclose = wsclose;
            }

            $(document).ready(function () {
                //connect to the message service
                ws = new WebSocket(url, "EAW1805");
                ws.onopen = wsopen;
                ws.onmessage = wsmessage;
                ws.onclose = wsclose;
            });

            function sendPing() {
                ws.send("ping");
            }

            function chatIndicator() {
                var src = $("#widget-105").attr("src");
                if (src.indexOf("Alert") == -1) {
                    $("#widget-105").attr("src", "http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchAlert.png");
                } else {
                    $("#widget-105").attr("src", "http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchOff.png");
                }
            }

            var input = $("#inputChatBox");

            input.keydown(function (e) {
                try {
                    if (e.keyCode === 13) {
                        var msg = $("#inputChatBox").val();
                        ws.send(JSON.stringify({ author:'glob', message:msg, gameId:0, nationId:0, userId:${user.userId}}));

                        $("#inputChatBox").val('');
                        $("#inputChatBox").focus();
                    }
                } catch (e) {
                    console.log("on key down : " + e);
                }
            });

        } catch (e) {
            console.log("error initializin websocket: " + e);
        }

    </script>
</security:authorize>
<security:authorize ifNotGranted="ROLE_ANONYMOUS">
    <table id="-1" cellspacing="0" cellpadding="0" style="width: 334px;display: none;">
        <tbody>
        <tr>
            <td align="left" style="vertical-align: top; " width="30px">
                <div id="imgDiv" style="position: relative; overflow: hidden; width: 30px; height: 30px; ">
                    <a id="url" href='/user/'>
                        <img id="img"
                             src="https://secure.gravatar.com/avatar/${user.emailEncoded}?s=29&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                             alt="" height="29" width="29" title="${user.username}"
                             class="pointer"
                             style="width: 29px; position: absolute; left: 0px; top: 0px; border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;">
                    </a>
                </div>
            </td>
            <td align="left" style="vertical-align: top; ">
                <table cellspacing="0" cellpadding="0">
                    <tbody>
                    <tr>
                        <td align="left" style="vertical-align: top; ">
                            <div id="cont" class="gwt-HTML">
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        </tbody>
    </table>
</security:authorize>

