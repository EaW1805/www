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
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="icon" type="image/ico" href='http://static.eaw1805.com/images/site/eaw1805-favicon.ico'/>
    <link rel="stylesheet" type="text/css" href='http://direct.eaw1805.com/style/basic.css'/>

    <script type="text/javascript" src='http://static.eaw1805.com/js/jquery-1.6.3.min.js'></script>
    <style type="text/css">
        #main {
            padding-bottom: 0;
        }
    </style>
</head>

<script type="text/javascript">
    $(document).ready(function () {
        var currentTimeMillis = new Date().getTime();
        $(function () {
            $("#main").css('padding-bottom', ($(window).height() - $("#main").height()))
        });

        $.ajax({
            url:'<c:url value="/solo/user/${user.username}"/>?rt=' + currentTimeMillis,
            success:onUpdate
        });

        if ("anonymous" == "${user.username}") {
            window.location = '<c:url value="/games"/>';
        }
    });
</script>
<body>
<div id="wrap">
<div id="main">

<table cellspacing="0" cellpadding="0" class="loginPanel" style="width: 100%; height: 800px; margin-top: 100px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: middle;">
            <div style="position: relative; overflow: hidden; width: 560px; height: 315px;"
                 class="tutorialInfoPanel">
                <iframe width="560" height="315" src="//www.youtube.com/embed/be_Y4V6qEy4?rel=0&autoplay=1"
                        frameborder="0" allowfullscreen></iframe>
            </div>
        </td>
    </tr>
    <tr>
        <td align="center" style="vertical-align: middle;">
            <div style="position: relative; overflow: hidden; width: 570px; height: 550px;"><img
                    src="http://static.eaw1805.com/images/loading/LoadingScreen.png" class="gwt-Image"
                    style="width: 1440px; height: 900px; position: absolute; left: -467px; top: -201px;">

                <div style="width: 432px; height: 30px; position: absolute; left: 68px; top: 279px;">
                    <svg overflow="hidden" width="432" height="30">
                        <defs></defs>
                        <image id="img1" preserveAspectRatio="none" x="6" y="6" width="35" height="20"
                               xlink:href="http://static.eaw1805.com/images/loading/loadingBar1.png"></image>
                        <image id="img2" preserveAspectRatio="none" x="41" y="6" width="33" height="20"
                               xlink:href="http://static.eaw1805.com/images/loading/loadingBar2.png"
                               style="display: none;"></image>
                        <image id="img3" preserveAspectRatio="none" x="74" y="6" width="29" height="20"
                               xlink:href="http://static.eaw1805.com/images/loading/loadingBar3.png"
                               style="display: none;"></image>
                        <image id="img4" preserveAspectRatio="none" x="103" y="6" width="27" height="20"
                               xlink:href="http://static.eaw1805.com/images/loading/loadingBar4.png"
                               style="display: none;"></image>
                        <image id="img5" preserveAspectRatio="none" x="130" y="6" width="40" height="20"
                               xlink:href="http://static.eaw1805.com/images/loading/loadingBar5.png"
                               style="display: none;"></image>
                        <image id="img6" preserveAspectRatio="none" x="170" y="6" width="38" height="20"
                               xlink:href="http://static.eaw1805.com/images/loading/loadingBar6.png"
                               style="display: none;"></image>
                        <image id="img7" preserveAspectRatio="none" x="208" y="6" width="47" height="20"
                               xlink:href="http://static.eaw1805.com/images/loading/loadingBar7.png"
                               style="display: none;"></image>
                        <image id="img8" preserveAspectRatio="none" x="254" y="6" width="46" height="20"
                               xlink:href="http://static.eaw1805.com/images/loading/loadingBar8.png"
                               style="display: none;"></image>
                        <image id="img9" preserveAspectRatio="none" x="300" y="6" width="38" height="20"
                               xlink:href="http://static.eaw1805.com/images/loading/loadingBar9.png"
                               style="display: none;"></image>
                        <image id="img10" preserveAspectRatio="none" x="338" y="6" width="40" height="20"
                               xlink:href="http://static.eaw1805.com/images/loading/loadingBar10.png"
                               style="display: none;"></image>
                        <image id="img11" preserveAspectRatio="none" x="378" y="6" width="47" height="20"
                               xlink:href="http://static.eaw1805.com/images/loading/loadingBar11.png"
                               style="display: none;"></image>
                    </svg>
                </div>
                <div class="clearFont" style="position: absolute; left: 68px; top: 315px;">Creating your new World!
                </div>

            </div>
        </td>
    </tr>
    </tbody>
</table>
<%--<div id="main-article"
     style="min-height:104px; height: 195px; position: relative;margin: 0px 0px 0px -35px;width: 1033px;
     background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
     repeat-y scroll 0px 0px transparent;clear: both;
     padding-left: 35px;">


    <h1>Welcome to Empires at War 1805</h1>

    <p class="manual">Your account was successfully created.</p>

    <p class="manual">Log in to your account via the following link: <a href="<c:url value="/login"/>">login</a></p>

    <p style="width: 900px;" class="manual">You can find valuable information about the game in the <a
            href="<c:url value="/help/introduction"/>">Quick Start Guide</a>.
        Detailed information about the game rules and the mechanisms of Empires at War 1805 are available in the <a
                href="<c:url value="/handbook"/>">Players' Handbook</a>.</p>

    <p class="manual">For questions about the rules and the user interface, we suggest that you use our
        <a href="http://forum.eaw1805.com">forums</a> where all players post their comments and our replies.

    <p class="manual">Check out the active games in the <a href="<c:url value="/listgames"/>">Games Lobby</a>.
</div>

<div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
</div>

<div id="flash-container" style="display: none; position: relative;margin: 15px 0px 0px -35px; width: 1035px;min-height: 390px;
background: url('http://static.eaw1805.com/images/site/NewsParchment.png') repeat-y scroll 0px 0px transparent;clear: both;
background-size: 1035px 390px;">
    <div style="padding-top: 25px; padding-left: 30px;">
        <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" width="975" height="330" id="eawflash"
                align="middle">
            <param name="movie" value="http://static.eaw1805.com/images/site/eaw-playnow.swf"/>
            <param name="quality" value="high"/>
            <param name="bgcolor" value="#ffffff"/>
            <param name="play" value="true"/>
            <param name="loop" value="true"/>
            <param name="wmode" value="window"/>
            <param name="scale" value="showall"/>
            <param name="menu" value="true"/>
            <param name="devicefont" value="false"/>
            <param name="salign" value=""/>
            <param name="allowScriptAccess" value="sameDomain"/>
            <!--[if !IE]>-->
            <object type="application/x-shockwave-flash"
                    data="http://static.eaw1805.com/images/site/eaw-playnow.swf" width="975"
                    height="330">
                <param name="movie" value="ttp://static.eaw1805.com/images/site/eaw-playnow.swf"/>
                <param name="quality" value="high"/>
                <param name="bgcolor" value="#ffffff"/>
                <param name="play" value="true"/>
                <param name="loop" value="true"/>
                <param name="wmode" value="window"/>
                <param name="scale" value="showall"/>
                <param name="menu" value="true"/>
                <param name="devicefont" value="false"/>
                <param name="salign" value=""/>
                <param name="allowScriptAccess" value="sameDomain"/>
                <!--<![endif]-->
                <a href="http://www.adobe.com/go/getflash">
                    <img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif"
                         alt="Get Adobe Flash player"/>
                </a>
                <!--[if !IE]>-->
            </object>
            <!--<![endif]-->
        </object>
    </div>
</div>


<div style="position: relative;margin: 15px 0px 0px -35px; width: 1035px;min-height: 500px; padding: 20px 40px;
background: url('http://static.eaw1805.com/images/site/NewsParchment.png') repeat-y scroll 0px 0px transparent;clear: both;
background-size: 1035px 540px;">
    <h1 style="font-size: 42px;">Quick Start</h1>

    <div style="float: left; margin-left: -55px; margin-top: -20px; width: 600px;"><img
            src='http://static.eaw1805.com/images/site/MainPageBritishSoldierGrassFlipped.png'
            alt="British Soldier"
            title="British Soldier"
            border=0
            width="620"
            style="float: left; border: 0 none;">
    </div>

    <div style="float: left; width: 430px; margin-right: 0px;">
        <h3 style="margin-bottom: 2px;">
            <a
                    style="font-weight: normal;
                        text-align: left;
                        color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 28px;"
                    href='<c:url value="/help/introduction?id=156"/>'>Getting Started
            </a>
        </h3>
        <ul style="float: right; width: 400px; margin-right: 15px; white-space: normal; text-align: justify; font-size: 13px; margin-bottom: 20px;">
            The gaming environment is rich with details about all the critical aspects your empire.
            Start by getting a strategic-level introduction of the main elements of the game.
            &nbsp;&nbsp;<a style="font-weight: normal; color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 13px;"
                           href='<c:url value="/help/introduction?id=156"/>'>Read more</a>
        </ul>

        <h3 style="margin-bottom: 8px; ">
            <a
                    style="font-weight: normal;
                        text-align: left;
                        color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 28px;"
                    href='<c:url value="/help/introduction?id=158"/>'>Command your Forces
            </a>
        </h3>
        <ul style="float: right; width: 400px; margin-right: 15px; white-space: normal; text-align: justify; font-size: 13px; margin-bottom: 20px; margin-top: 28px; ">
            In Empires at War 1805 you can control Armies & Corps to conquer, Ships to expand and colonize,
            Baggage trains to perform trade, and Spies to conduct espionage. Each element is crucial for your empire to
            survive and prevail the wars that are about to start.
            &nbsp;&nbsp;<a style="font-weight: normal; color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 13px;"
                           href='<c:url value="/help/introduction?id=158"/>'>Read more</a>
        </ul>

        <h3 style="margin-bottom: 8px; ">
            <a
                    style="font-weight: normal;
                        text-align: left;
                        color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 28px;"
                    href='<c:url value="/help/introduction?id=157"/>'>Control & Conquer
            </a>
        </h3>
        <ul style="float: right; width: 400px; margin-right: 15px; white-space: normal; text-align: justify; font-size: 13px; margin-bottom: 20px; margin-top: 28px; ">
            The basis of your empire are your people. You need them for a solid production, drafting of troops and
            financial
            support.
            &nbsp;&nbsp;<a style="font-weight: normal; color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 13px;"
                           href='<c:url value="/help/introduction?id=157"/>'>Read more</a>
        </ul>

        <h3 style="margin-bottom: 8px; ">
            <a
                    style="font-weight: normal;
                        text-align: left;
                        color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 28px;"
                    href='<c:url value="/help/introduction?id=155"/>'>Basic Game Mechanics
            </a>
        </h3>
        <ul style="float: right; width: 400px; margin-right: 15px; white-space: normal; text-align: justify; font-size: 13px; margin-bottom: 20px; margin-top: 28px; ">
            Get valuable insight on the basic elements of the Napoleonic era of Empires at War 1805. Understand how
            orders
            are issued to your forces to establish a strong sphere of influence to the
            four theaters of operation: Europe, The Caribbean, Africa and India.
            &nbsp;&nbsp;<a style="font-weight: normal; color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 12px;"
                           href='<c:url value="/help/introduction?id=155"/>'>Read more</a>
        </ul>
    </div>
</div>--%>

<script type="text/javascript">
    var myVar = setInterval(function () {
        var currentTimeMillis = new Date().getTime();
        $.ajax({
            url:'<c:url value="/solo/user/${user.username}"/>?rt=' + currentTimeMillis,
            success:onUpdate
        });
    }, 5000);

    function onUpdate(data) {
        var currentTimeMillis = new Date().getTime();
        if (data == "0") {
            $.ajax({
                url:'<c:url value="/solo/user/${user.username}/game"/>?rt=' + currentTimeMillis,
                success:redirectToSoloGame
            });

        } else if (data == "-3") {
            window.location = '<c:url value="/games"/>';
        }
    }

    var currentImage = 2;
    var loadingTimer = setInterval(function () {
        var currentTimeMillis = new Date().getTime();
        if (currentImage <= 11) {
            $("#img" + currentImage).show();
            currentImage++;
        } else {
            clearInterval(loadingTimer);
            $.ajax({
                url:'<c:url value="/solo/user/${user.username}/game"/>?rt=' + currentTimeMillis,
                success:redirectToSoloGame
            });
        }
    }, 16300);


    function redirectToSoloGame(data) {
        if (data == "-2") {
            //Do nothing
        } else {
            var tmp = '<c:url value="/play/scenario/1804/game/"/>';
            window.location = tmp + data;
        }
    }

</script>

</div>
</div>
</body>
</html>
