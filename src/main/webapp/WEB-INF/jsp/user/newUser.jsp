<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:useBean id="dates" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>

<jsp:useBean id="userGames" scope="request" type="java.util.Set<com.eaw1805.data.model.UserGame>"/>
<jsp:useBean id="userGameOrders" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Boolean>"/>
<jsp:useBean id="userGameVps" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
<jsp:useBean id="userGameStats" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.lang.String>>"/>
<jsp:useBean id="userGamesDeadTurns" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>

<jsp:useBean id="activityStat" scope="request" type="java.util.Map"/>
<jsp:useBean id="name" scope="request" class="java.lang.String"/>
<jsp:useBean id="gameStatsMessages" scope="request" type="java.util.Map<java.lang.String, java.lang.String>"/>

<jsp:useBean id="freeNations" scope="request" type="java.util.LinkedList<java.util.LinkedList<java.lang.Object>>"/>
<jsp:useBean id="profileStats" scope="request" type="java.util.HashMap<java.lang.String, java.lang.Object>"/>

<jsp:useBean id="scenario1804" scope="request" class="java.lang.Integer"/>

<jsp:useBean id="turnsPlayed" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="turnsPlayedSolo" scope="request" class="java.lang.Integer"/>

<jsp:useBean id="connectedFriends" scope="request" type="java.util.List<com.eaw1805.data.model.User>"/>

<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.sparkline.min.js'></script>


<script>

    function openPickupForm(scenario, game, nation, nationName, cost) {

        <c:url var="thisUrl" value="/"/>
        var url = "${thisUrl}" + "scenario/" + scenario + "/game/" + game + "/pickup/" + nation;
        $('#joinForm').attr('action', url);
        $('#form_nationId').text(nation);
        $('#nationImgFile').attr('src', "http://static.eaw1805.com/images/nations/nation-" + nation + "-list.jpg");
        $('#nationImgFile').attr('title', nationName);
        $('#cost').text(cost);
        $('#joinTitle').text("Join Game " + game);
        $('#ok').blur();
        $.blockUI({ message: $('#question'), css: { width: '390px' } });
    }

    $.blockUI.defaults.applyPlatformOpacityRules = false;
    var costPerCredit = 1;
    var isConfirmDialogOpen = false;
    function openCreditsForm() {
        $.blockUI({ message: $('#creditsDialog'), css: { width: '800px', height: '700px', margin: '-43px', top: '10%', left: '26%',
            background: 'none'
        }});
    }

    function updateCost() {
        var numOfCredits = parseInt(document.getElementById("numOfCredits").value);
        if (!isNaN(numOfCredits)) {
            document.getElementById("amount").value = numOfCredits * costPerCredit;
        }
    }

    function updateCredits() {
        var amount = parseInt(document.getElementById("amount").value);
        if (!isNaN(amount)) {
            document.getElementById("numOfCredits").value = amount / costPerCredit;
        }
    }

    function unblock() {
        $.unblockUI();
        isConfirmDialogOpen = false;
    }

    function isMobileDevice() {
        var isMob = navigator.userAgent.match(/(iPad)|(iPhone)|(iPod)|(android)|(webOS)/i)
        return isMob != null;
    }

    $(document).ready(function () {
        if (!isMobileDevice()) {
            $("#flash-container").css("display", "");
        }
    });


</script>
<style type="text/css">
    .pagehead {
        background: none;
    }

    #footer .parchment-footer {
        background: none;
    }

    #content {
        padding-left: 10px;
        padding-right: 60px;
        padding-bottom: 0px;
        overflow: visible;
    }

    div.player {
        position: relative;
        margin: -10px 0px 0px -25px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;
        padding-left: 28pt;
    }

    article {
        width: 965px;
        margin-left: 35px;
    }

    div.todo {
        height: 100px;
        width: 311px;
        float: left;
        margin: 0;
        margin-right: 2px;
        margin-bottom: 2px;
        border-right: 1px solid #8F8F8F;
        border-radius: 10px;
        border-top: 1px solid white;
        border-bottom: 1px solid #8F8F8F;
        background: rgba(243, 243, 243, 0.3) none repeat-x 0 0;
    }

    div.todoDis {
        height: 100px;
        width: 311px;
        background-color: #808080;
        opacity: 0.65;
        float: left;
        margin: 0;
        margin-right: 2px;
        margin-bottom: 2px;
        border-right: 1px solid #8F8F8F;
        border-radius: 10px;
        border-top: 1px solid #8F8F8F;
        border-bottom: 1px solid #8F8F8F;
        margin-left: -314px;
        z-index: 1000;
    }

    div.connection-item {
        height: 46px !important;
    }

</style>
<script type="text/javascript">
    $.blockUI.defaults.applyPlatformOpacityRules = false;

    $(function () {
        /** This code runs when everything has been loaded on the page */
        /* Use 'html' instead of an array of values to pass options
         to a sparkline with data in the tag */
        $('.inlinebar').sparkline('html', {type: 'bar', barColor: '#0000ff'});
        $('.inlinebar-player').sparkline('html', {type: 'bar', barColor: '#ff0000'});
    });

    $(document).ready(function () {
        <c:if test="${param.err != null}">
        $.blockUI({ message: $('#error_message_${param.err}'), css: { width: '390px' } });
        </c:if>
    });
</script>
<div id="main-article"
style="min-height:400px;position: relative;margin: 0px 0px 0px -35px;width: 1033px;background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px -200px transparent;clear: both;">

<!-- compute height of side column -->
<c:set var="sideHeight" value="395"/>
<c:set var="friendsHeight" value="0"/>
<c:if test="${fn:length(connectedFriends) > 0}">
    <c:set var="friendsRows" value="${fn:length(connectedFriends)/7}"/>
    <c:set var="friendsRowHeight"><fmt:formatNumber value="${friendsRows+(1-(friendsRows%1))%1}" type="number"
                                                    pattern="#"/></c:set>
    <c:set var="friendsHeight" value="${46 + friendsRowHeight*42}"/>
    <c:set var="sideHeight" value="${sideHeight + friendsHeight}"/>
</c:if>

<!-- compute height of main column -->
<c:set var="gamesHeight" value="0"/>
<c:set var="soloGameHeight" value="207"/>

<!-- compute minimum height -->
<c:set var="minHeight" value="${sideHeight}"/>
<c:if test="${gamesHeight + soloGameHeight > sideHeight}">
    <c:set var="minHeight" value="${gamesHeight + soloGameHeight}"/>
</c:if>

<c:if test="${profileUser.userId == user.userId && scenario1804 == 0 && turnsPlayedSolo > 0}">
    <c:set var="minHeight" value="620"/>
</c:if>

<script type="text/javascript">
    //If IE show Warning.
    if ($.browser.msie) {
        $('#main-article').css({ 'min-height':${minHeight + 10 + 220}});
    } else {
        $('#main-article').css({ 'min-height':${minHeight + 10}});
    }
</script>
<article>
<c:if test="${profileUser.userId == user.userId}">

<div style="float:left;width: 640px;">
<c:if test="${profileUser.userId == user.userId && scenario1804 > 0}">
    <jsp:useBean id="scenario1804date" scope="request" class="java.lang.String"/>
    <section class="gameInfo" id="gameInfo" style="height: 268px; float: left; width: 951px; border: none;">

        <div style="float:left; height: 0px; margin-top: -18px;">
            <img src='http://static.eaw1805.com/images/buttons/PlayButtonSmall.png'
                 id="Play-Button"
                 usemap="#Play-Button-Map-1804"
                 alt="Play Scenario 1804"
                 title="Play Scenario 1804"
                 align="right"
                 border=0
                 width=380
                 onclick="window.open('<c:url value="/play/scenario/1804/game/${scenario1804}/nation/5"/>', '_blank');"
                 style="z-index: -10000; cursor: pointer;">
        </div>

        <h1 style="width:500px; text-align:center; font-size: 47px !important;float: right;margin-top: 95px!important;">
            Your own Game is at ${scenario1804date}</h1>

        <div style="display:none;" id="dropPopup_1804">
            <h3 style="margin:auto; width:90%; margin-top: 17px;">Finish Scenario 1804 Solo-Game</h3>

            <div style="margin-left: 20px; color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                text-shadow: 1px 1px 0.1px rgb(123, 113, 75);font-weight: normal;letter-spacing: -1px;
                font-size: 16px;text-align: left; margin-top:5px;clear: both;">
                Once you choose to finish your game, all records will be permanently removed.
            </div>

            <div style="margin-left: 50px;color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                text-shadow: 1px 1px 0.1px rgb(123, 113, 75);font-weight: normal;letter-spacing: -1px;
                margin-top: 10px;
                font-size: 16px;text-align: left;clear: both;">
                Do you want to finish your Scenario 1804 game?
            </div>

            <button class="ok" onfocus=" this.blur();" title="Drop position"
                    style="clear: both; margin-top: 15px;margin-right: 5px;"
                    onclick="window.location='<c:url value="/joingame/finish"/>';"
                    value="Drop Position"/>
            <button id="cancelb" class="cancel" title="Cancel" onfocus=" this.blur();"
                    style="clear: both; margin-top: 15px;margin-left: 5px; " aria-describedby=""
                    onclick="$.unblockUI();$('#'+$('#cancelb').attr('aria-describedby')).hide();return false;"
                    value="cancel"/>
        </div>

        <div style="clear:both;width:150px;float: left; margin-left: 189px; margin-top: 55px; ">
                <%--<div style="width:200px;float: right; ">
                    <a href='<c:url value="/help/introduction"/>'
                       title="Quick Introduction"
                       style="float:right;line-height: 1.3!important;"><span
                            style="font-size: 14px;">Quick Introduction</span></a>
                </div>
                <div style="width:200px;float: right; ">
                    <a href='<c:url value="/handbook"/>'
                       title="Player's Handbook"
                       style="float:right;line-height: 1.3!important;"><span
                            style="font-size: 14px;">Player's Handbook</span></a>
                </div>--%>
            <div style="width:150px;float: right;">
                <span style="color: #654900;font-size: 14px;">If you want to finish the Solo Game click</span>
                <a href="javascript:$.blockUI({ message: $('#dropPopup_1804'), css: { width: '390px',cursor: 'default' } });"
                   class="minibutton"
                   title="Finish this game"
                   style="float:right;line-height: 1.3!important;"><span
                        style="font-size: 14px;">here</span></a>
            </div>
        </div>


    </section>
</c:if>


</div>
<div style="float: right;width: 230px;margin-top: -120px;clear: both;">
<section id="thisBrowser" class="topRight" style="display: none;height: 200px;">
<h1 style="margin:0;">Warning</h1>

<div style="margin: 5px;margin-bottom:20px;text-align: justify;margin-top: -20px;">We
strongly encourage you to use Firefox browser or Chrome browser.
Please use the link bellow to download the latest version.
</div>
<div>
<a style="float:none;"
href='http://www.mozilla.org/en-US/firefox/new/'>
<img src='http://static.eaw1805.com/images/site/firefoxBanner.png'
alt="Download Firefox"
title="Download Firefox"
aling="center"
border=0
width=290
style="border: none;margin: 0px;margin-top: -8px;margin-left: 8px;"></a>
</div>
</section>
</div>

</article>

<c:if test="${profileUser.userId == user.userId && scenario1804 == 0 && turnsPlayedSolo == 0}">
    <div class="slide" style="width: 1035px;margin-left: 49px;">
        <a href="<c:url value="/joingame/start"/>">
            <img src="http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_C_Off.png"
                 alt="Free Strategy Game"
                 title="Play a Free Position"
                 onclick="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_C_On.png'"
                 onmouseover="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_C_Hover.png'"
                 onmouseout="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_C_Off.png'"
                 width=320px
                 style="float: left; border: 0 none; margin-top: 30px; margin-left: 10px; cursor: pointer;">
        </a>

        <div style="float: right; text-align: center; width: 580px; margin-top: 30px; margin-right: 100px;">
            <p style="float: right; text-align: center; width: 580px; margin-top: 0px; margin-right: 5px;  font-size: 29pt;">
                <strong>Play Empires at War for free</strong></p>

            <p style="float: right; text-align: center; width: 580px; margin-top: 20px; margin-right: 5px;  font-size: 18pt;">
                <strong>Start now and learn the game mechanics</strong></p>

            <p style="float: right; text-align: center; width: 580px; margin-top: 35px; margin-right: 5px; font-size: 14pt;">
                Assume the role of Napoleon Bonaparte starting from January 1804.</p>

            <p style="float: right; text-align: center; width: 580px; margin-top: 15px; margin-right: 5px; font-size: 14pt;">
                Become the political and military leader of France during the latter stages of the French Revolution and
                its
                associated wars in Europe.</p>

            <p style="float: right; text-align: center; width: 580px; margin-top: 30px; margin-right: 5px; font-size: 12pt;">
                <strong>Empires at War is a sophisticated Browser strategy game</strong></p>

            <p style="float: right; text-align: center; width: 580px; margin-top: 15px; margin-right: 5px; font-size: 26pt;">
                <strong>No download required</strong></p>
        </div>
    </div>
</c:if>

<c:if test="${profileUser.userId == user.userId && fn:length(userGames) == 0 && scenario1804 == 0 && turnsPlayedSolo > 0}">
    <div class="pagehead"
         style="background-image: url('http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtonsBase.png');
                background-position: 40px 0px;
                background-repeat: no-repeat;
                float: left;
                clear: both;
                background-size: 585px 597px;
                margin-top: 0px;
                margin-left: 185px;
                margin-bottom: -30px;
                width: 680px;
                height: 621px;">

        <div style="margin-top: 60px;
                    margin-left: 25px;
                    width: 290px;
                    height: 290px;
                    float: left;">
            <a href="<c:url value="/joingame/new"/>">
                <img src="http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_A_Off.png"
                     alt="Play Empires at War 1805"
                     title="Start a position in a new Game"
                     onclick="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_A_On.png'"
                     onmouseover="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_A_Hover.png'"
                     onmouseout="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_A_Off.png'">
            </a></div>

        <div style="margin-top: 60px;
                    margin-left: -15px;
                    width: 290px;
                    height: 290px;
                    float: left;">
            <a href="<c:url value="/joingame/running"/>">
                <img src="http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_B_Off.png"
                     alt="Play Empires at War 1805"
                     title="Pick-up a position in an on going Game"
                     onclick="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_B_On.png'"
                     onmouseover="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_B_Hover.png'"
                     onmouseout="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_B_Off.png'">
            </a></div>

        <div style="margin-top: -52px;
                    margin-left: 163px;
                    width: 290px;
                    height: 290px;
                    float: left;">
            <a href="<c:url value="/joingame/free"/>">
                <img src="http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_C_Off.png"
                     alt="Free Play Empires at War 1805"
                     title="Play a Free Position"
                     onclick="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_C_On.png'"
                     onmouseover="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_C_Hover.png'"
                     onmouseout="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_C_Off.png'">
            </a></div>
    </div>
</c:if>

</div>


<div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
</div>


<c:if test="${turnsPlayedSolo < 5 }">
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
                Baggage trains to perform trade, and Spies to conduct espionage. Each element is crucial for your empire
                to survive and prevail the wars that are about to start.
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
    </div>


    <div style="z-index: 2; clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
        <h2 style="font-size: 42px; padding-left: 40px; padding-top: 20px;">Game Tutorials</h2>
    </div>

    <div style=" z-index: 1; position: relative;margin: -10px 0px 0px -35px;width: 1000px;min-height: 303px;padding: 30px 40px;
background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
        <table style="margin-left: 10px;">
            <tr valign="top">
                <td width="510" valign="top">
                    <h3 style="padding-top: 6px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                        Tutorial 1: Forming Corps &amp; Armies</h3>
                    <iframe width="400" height="225" src="http://www.youtube.com/embed/4toIINeKfxY" frameborder="0"
                            allowfullscreen></iframe>
                </td>

                <td width="510" valign="top">
                    <h3 style="padding-top: 6px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                        Tutorial 2: Managing Fleets</h3>
                    <iframe width="400" height="225" src="http://www.youtube.com/embed/3NcPqVQpHcg" frameborder="0"
                            allowfullscreen></iframe>
                </td>
            </tr>
            <tr valign="top">
                <td width="510" valign="top">
                    <h3 style="padding-top: 16px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                        Tutorial 3: Exchanging &amp; Merging Battalions</h3>
                    <iframe width="400" height="225" src="http://www.youtube.com/embed/__7-udFwhEM" frameborder="0"
                            allowfullscreen></iframe>
                </td>

                <td width="510" valign="top">
                    <h3 style="padding-top: 16px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                        Tutorial 4: Movement</h3>
                    <iframe width="400" height="225" src="http://www.youtube.com/embed/S62I6UWO0U0" frameborder="0"
                            allowfullscreen></iframe>
                </td>
            </tr>
            <tr valign="top">
                <td width="510" valign="top">
                    <h3 style="padding-top: 16px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                        Tutorial 5: Conquest</h3>
                    <iframe width="400" height="225" src="http://www.youtube.com/embed/NZ1pDkFCLS8" frameborder="0"
                            allowfullscreen></iframe>
                </td>

                <td width="510" valign="top">
                    <h3 style="padding-top: 16px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                        Tutorial 6: Trading</h3>
                    <iframe width="400" height="225" src="http://www.youtube.com/embed/Pmd8m8TRnBg" frameborder="0"
                            allowfullscreen></iframe>
                </td>
            </tr>
        </table>
    </div>
    <div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78"></div>
</c:if>

<c:if test="${fn:length(newGames)> 0}">
    <jsp:useBean id="newGames" scope="request"
                 type="java.util.HashMap<com.eaw1805.data.model.Game, java.util.TreeSet<com.eaw1805.data.model.UserGame>>"/>
    <div style="z-index: 2;position: relative; margin: 0 -40px 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
        <h1 class="bigmap" style="width: 600px !important; font-size: 36px; padding-left: 40px; padding-top: 20px;">
            Express New Nation Pick Up</h1>
    </div>
    <div style="z-index: 4; position: relative; float: right; margin-right: 10px; margin-top:-20px; margin-bottom: -23px;">
        <a href="<c:url value="/joingame/new"/>"
           class="minibutton"
           title="When the game starts you will receive a notification, </br>but you can already put your first turn in right now!">
            <img src="http://static.eaw1805.com/images/layout/buttons/ButSpyReportsOff.png"
                 alt="Join New Game"
                 onmouseover="this.src='http://static.eaw1805.com/images/layout/buttons/ButSpyReportsHover.png'"
                 onmouseout="this.src='http://static.eaw1805.com/images/layout/buttons/ButSpyReportsOff.png'"
                 border=0></a>
    </div>
    <div id="free-new-nations" style="min-height:100px;position: relative; float: left; margin: -10px 0px 0px -35px;width: 1033px;
     background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
     repeat-y scroll 0px 0px transparent;clear: both; padding-left: 35px;">
        <script type="text/javascript">
            <c:set var="rows" value="${fn:length(newGames)/3}"/>
            <c:set var="height"><fmt:formatNumber value="${rows+(1-(rows%1))%1}" type="number" pattern="#"/></c:set>
            $('#free-new-nations').css({ 'min-height':${height*200 + 35}});
        </script>
        <nationList style="width: 954px; margin: 0 0 5px 3px;  border: none; float: left; padding-top: 30px;">
            <ul class="nationList">
                <c:forEach items="${newGames}" var="newGame">
                    <li class="nationList" style="cursor: pointer;height: 195px;width: 470px; float:left; margin:0; margin-right: 2px; margin-bottom: 2px;
border-right: 1px solid rgb(143, 143, 143);"
                        onclick='window.location="<c:url value="/joingame/new"/>" '>
                        <dl class="nationList" style="margin-top:5px;">
                            <dt class="nationList" style="width: 140px;">

                                <a title="Show Game Info"
                                   href='<c:url value="/scenario/${newGame.key.scenarioIdToString}/game/${newGame.key.gameId}/info" />'>
                                    <h1 style="font-size:25px;">Game ${newGame.key.gameId}</h1>
                                </a>

                                <a href="<c:url value="/scenario/${newGame.key.scenarioIdToString}/info"/>"><h1
                                        style="margin-top: -22px; margin-left: 102px; font-size:25px;">/
                                    Scenario ${newGame.key.scenarioIdToString}</h1></a>
                            </dt>
                            <dd class="nationList"
                                style='width: 20pt; padding-top: 7px;margin-left: 150px; line-height: 1.3 !important;
                                                   font-family: Georgia,"Times New Roman",Times,serif;
                                                   text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);font-size: 1.2em;'>
                                    <%--${newGamesMonths[newGame.key.gameId]}--%>
                                Turn Schedule: ${ newGame.key.schedule} days
                            </dd>

                            <dt class="nationList" style="width: 140px; clear: both;">
                            <ul style="display: block;list-style-type: none; margin-top: 10px;margin-left:2px;width: 470px;">
                                <c:forEach items="${newGame.value}" var="thisUserGame">
                                    <li style="border-radius: 10px;border-top: 1px solid #DED356;
                                            border-bottom: 1px solid #8F8F8F;
                                    <c:choose>
                                    <c:when test="${thisUserGame.userId !=2}">
                                            background: transparent;
                                    </c:when>
                                    <c:otherwise>
                                            background: rgba(208, 158, 50, 0.5) none repeat-x 0 0;
                                    </c:otherwise>
                                    </c:choose>
                                            margin-bottom: 3px !important; padding: 3px 1px 0px 6px; width: 80px;float: left;">
                                        <img src='http://static.eaw1805.com/images/nations/nation-${thisUserGame.nation.id}-36.png'
                                             alt="${thisUserGame.nation.name}"
                                             class="toolTip"
                                             title="${thisUserGame.nation.name}"
                                             border=0 height=24>&nbsp;

                                        <c:if test="${thisUserGame.userId != 2}">
                                            <img src="https://secure.gravatar.com/avatar/${gameUsers[thisUserGame.userId].emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                                 style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                                 alt="${gameUsers[thisUserGame.userId].username}"
                                                 height="24"
                                                 width="24"
                                                 title="${gameUsers[thisUserGame.userId].username}">
                                        </c:if>
                                    </li>
                                </c:forEach>
                            </ul>
                            </dt>
                        </dl>
                    </li>
                </c:forEach>
            </ul>
        </nationList>
    </div>

    <div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
    </div>
</c:if>

<c:if test="${fn:length(freeNations)> 0}">
    <div style="z-index: 2;position: relative; margin: 0 -40px 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
        <h1 class="bigmap" style="width: 600px !important; font-size: 36px; padding-left: 40px; padding-top: 20px;">
            Express Free Nation Pick Up</h1>
    </div>
    <div style="z-index: 4; position: relative; float: right; margin-right: 10px; margin-top:-20px; margin-bottom: -23px;">
        <a href="<c:url value="/joingame/running"/>"
           class="minibutton"
           title="Joining an existing game places you faster into action, and is also good for new players </br>
                since they learn how to play with an existing position before setting their own empire up.">
            <img src="http://static.eaw1805.com/images/layout/buttons/ButSpyReportsOff.png"
                 alt="Join Running Game"
                 onmouseover="this.src='http://static.eaw1805.com/images/layout/buttons/ButSpyReportsHover.png'"
                 onmouseout="this.src='http://static.eaw1805.com/images/layout/buttons/ButSpyReportsOff.png'"
                 border=0></a>
    </div>
    <div id="free-nations" style="z-index: 1; min-height:100px; position: relative; float: left; margin: -10px 0 0 -35px;width: 1033px;
     background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
     repeat-y scroll 0px 0px transparent;clear: both; padding-left: 35px;">
        <c:set var="rows" value="2"/>
        <c:if test="${fn:length(freeNations) < 6}">
            <c:set var="rows" value="${fn:length(freeNations)/3.0}"/>
        </c:if>
        <c:set var="height"><fmt:formatNumber value="${rows+(1-(rows%1))%1}" type="number" pattern="#"/></c:set>
        <script type="text/javascript">
            $('#free-nations').css({ 'min-height':${height*104 + 35}});
        </script>
        <nationList style="width: 954px; margin: 0 0 5px 3px;  border: none; float: left; padding-top: 30px;">
            <ul class="nationList">
                <c:set var="nationIndex" value="0"/>
                <c:forEach items="${freeNations}" var="thisNationData">
                    <c:if test="${nationIndex < 6}">
                        <li class="nationList" style="cursor: pointer;height: 100px;width: 311px; float:left; margin:0; margin-right: 2px; margin-bottom: 2px;
border-right: 1px solid rgb(143, 143, 143);" aria-describedby="" title="Play now"
                            onclick='openPickupForm("${thisNationData[0].scenarioIdToString}","${thisNationData[0].gameId}","${thisNationData[1].id}", "${thisNationData[1].name}", "${thisNationData[4].cost}" );'>
                            <dl class="nationList">
                                <dt class="nationList" style="width: 140px;"><a
                                        href='<c:url value="/scenario/${thisNationData[0].scenarioIdToString}/game/${thisNationData[0].gameId}/info" />'>
                                    <img style="margin: 0; border: 0; border-radius: 0;"
                                         src='http://static.eaw1805.com/images/nations/nation-${thisNationData[1].id}-list.jpg'
                                         alt="Nation's Flag"
                                         class="toolTip"
                                         title="Show more info"
                                         border=0 width=180
                                         aria-describedby=""></a></dt>
                                <dd class="nationList"
                                    style='width: 20pt; padding-top: 7px;margin-top: -11px;margin-left: 42px; line-height: 1.3 !important;
                                                   font-family: Georgia,"Times New Roman",Times,serif;
                                                   text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);font-size: 1.7em;'>
                                    Game ${thisNationData[0].gameId} /
                                </dd>
                                <dd class="nationList"
                                    style='width: 20pt; padding-top: 3px;margin-left: 50px; line-height: 1.3 !important;
                                                   font-family: Georgia,"Times New Roman",Times,serif;
                                                   text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);font-size: 1.1em;'>
                                        ${monthsSmall[thisNationData[0].gameId]}
                                </dd>
                                <dd class="nationList"
                                    style='width: 20pt; padding-top: 7px; line-height: 1.3 ! important; font-family: Georgia,"Times New Roman",Times,serif; text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2); font-size: 1.3em; margin-left: -102px; margin-top: 12px;'>
                                    Scenario ${thisNationData[0].scenarioIdToString}
                                </dd>

                                <dt class="nationList" style="width: 140px; clear: both;">
                                        <c:set var="index" value="${0}"/>
                                        <c:set var="statistics" value="${thisNationData[3]}"/>
                                    <c:forEach items="${statistics}" var="stats">
                                    <c:if test="${index < 3}">
                                <div style="clear:both;float: left;margin-left: 5px;width: 190px; margin-top:2px;">
                                    <div style="float: left;width: 150px;text-align: right;margin-right: 10px;">
                                        <spring:message code="${stats.key}"/></div>
                                    <div style="float: left;width: 25px;">${stats.value}</div>
                                </div>
                                </c:if>
                                <c:set var="index" value="${index+1}"/>
                                </c:forEach>
                                </dt>

                                <dd class="nationList"
                                    style="width: 30pt; padding-top: 7px;margin-top: -3px;margin-left: 55px;">
                                    vp:
                                </dd>
                                <dd class="nationList"
                                    style="width: 20pt; padding-top: 7px;margin-top: -11px;font-size:26px; margin-left: 5px;">
                                    <fmt:formatNumber
                                            type="number"
                                            maxFractionDigits="0"
                                            groupingUsed="true"
                                            value="${thisNationData[2]}"/>
                                </dd>
                                <div style="float: right;margin-left: 5px;width: 80px; margin-top:-5px; height:30px;">
                                    <div>Position Cost</div>
                                    <div style="float: left;width: 20px;">${thisNationData[4].cost}</div>
                                    <div><img style="float: left; border: 0;margin: 0;padding: 0;"
                                              src='http://static.eaw1805.com/images/goods/good-1.png'
                                              alt="Credits"
                                              class="toolTip"
                                              border=0 height=15/></div>
                                    <div style="margin-left: 5px;float: left;"> / &nbsp;turn</div>

                                        <%--/ turn--%>
                                </div>
                            </dl>
                        </li>
                    </c:if>
                    <c:set var="nationIndex" value="${nationIndex+1}"></c:set>
                </c:forEach>
            </ul>
        </nationList>
    </div>

    <div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
    </div>
</c:if>
<div style=" width: 338px;
background: url('http://static.eaw1805.com/images/site/Empire_parchment.png') repeat-y scroll 0px 0px transparent;
background-size: 338px 376px;
float: left;
min-height: 360px;
margin-left: -34px;
line-height: 1.3 !important;
font-size: 95%;
font-family: Georgia,'Times New Roman',Times,serif;
text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);
position: relative;
clear: both;">
<div style="margin:10px">
<h1>Empire Statistics</h1>
<c:forEach var="key" items="${empireKeys}">
    <div style="clear:both;float: left;margin-left: 5px;">
        <div style="float: left;width: 190px;text-align: left;margin-right: 5px; line-height: 1.4!important;">
            <spring:message code="${key}"/>
        </div>
        <div style="float: left;width: 80px; line-height: 1.4!important;text-align: right;">
            <c:choose>
                <c:when test="${profileStats[key]!= undefinedInt}">
                    <fmt:formatNumber
                            type="number"
                            maxFractionDigits="0"
                            groupingUsed="true"
                            value='${profileStats[key]}'/>
                </c:when>
                <c:otherwise>
                    -
                </c:otherwise>
            </c:choose>
        </div>
        <div style="float: left;width: 20px; line-height: 1.4!important; text-align: right; margin-left: 5px;">
            <c:set var="position">${key}Pos</c:set>
                ${profileStats[position]}
        </div>
    </div>
</c:forEach>
</div>
</div>

<div style=" width: 338px;
background: url('http://static.eaw1805.com/images/site/Politics_parchment.png') repeat-y scroll 0px 0px transparent;
background-size: 338px 290px;
float: left;
min-height: 290px;
margin-left: 10px;
line-height: 1.3 !important;
font-size: 95%;
font-family: Georgia,'Times New Roman',Times,serif;
text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);
position: relative;
">

<div style="margin:10px">
<h1>Politics Statistics</h1>
<c:forEach var="key" items="${politicsKeys}">
    <div style="clear:both;float: left;margin-left: 5px;">
        <div style="float: left;width: 190px;text-align: left;margin-right: 5px; line-height: 1.4!important;">
            <spring:message code="${key}"/>
        </div>
        <div style="float: left;width: 70px; line-height: 1.4!important;text-align: right;">
            <c:choose>
                <c:when test="${profileStats[key]!= undefinedInt}">
                    <fmt:formatNumber
                            type="number"
                            maxFractionDigits="0"
                            groupingUsed="true"
                            value='${profileStats[key]}'/>
                </c:when>
                <c:otherwise>
                    -
                </c:otherwise>
            </c:choose>
        </div>
        <div style="float: left;width: 15px; line-height: 1.4!important; text-align: right; margin-left: 5px;">
            <c:set var="position">${key}Pos</c:set>
                ${profileStats[position]}
        </div>
    </div>
</c:forEach>
</div>
</div>

<div style=" width: 338px;
background: url('http://static.eaw1805.com/images/site/Warfare_parchment.png') repeat-y scroll 0px 0px transparent;
background-size: 338px 465px;
float: right;
min-height: 465px;
margin-right: -34px;
margin-bottom: -30px;
line-height: 1.3 !important;
font-size: 95%;
font-family: Georgia,'Times New Roman',Times,serif;
text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);
position: relative;
">

<div style="margin:10px">
<h1>Warfare Statistics</h1>
<c:forEach var="key" items="${warfareKeys}">
    <div style="clear:both;float: left;margin-left: 5px;">
        <div style="float: left;width: 200px;text-align: left;margin-right: 5px; line-height: 1.4!important;">
            <spring:message code="${key}"/>
        </div>
        <div style="float: left;width: 70px; line-height: 1.4!important;text-align: right;">
            <c:choose>
                <c:when test="${profileStats[key]!= undefinedInt}">
                    <fmt:formatNumber
                            type="number"
                            maxFractionDigits="0"
                            groupingUsed="true"
                            value='${profileStats[key]}'/>
                </c:when>
                <c:otherwise>
                    -
                </c:otherwise>
            </c:choose>
        </div>
        <div style="float: left;width: 15px; line-height: 1.4!important; text-align: right; margin-left: 5px;">
            <c:set var="position">${key}Pos</c:set>
                ${profileStats[position]}
        </div>
    </div>
    <c:if test="${key == 'battles.tactical.lost'
                                || key == 'battles.field.lost'
                                || key == 'battles.naval.lost' }">
        <div style="clear:both;float: left;margin: 5px;width: 295px; border-bottom: dashed; border-width: 1px;"></div>
    </c:if>
</c:forEach>
</div>
</div>

<script>
//If IE show Warning.
if ($.browser.msie) {
$('#thisBrowser').show();
}
</script>

<div id="error_message_stcd" style="display:none; cursor: default">
<h3 style="margin: auto; margin-top: 20px;width: 60%;">Cannot drop your position</h3>


<div style="margin-left: 30px;color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
text-shadow: 1px 1px 0.1px rgb(123, 113, 75);font-weight: normal;letter-spacing: -1px;
font-size: 16px;width: 80%;text-align: left; margin-top: 20px;">
You will be able to drop your position on the next game turn.
</div>

<div style="margin-top: 10px;">
<button class="ok" onfocus="this.blur();" id="close_error_hoag" onclick="$.unblockUI();" value="Ok"/>
</div>
</div>

<div id="question" style="display:none; cursor: default">
<h3 id="joinTitle" style="margin-top: 22px;width: 120px;margin-left: 14px;float: left;">Join Game</h3>
<img style="float:right;margin-top: 20px; margin-right: 20px;" id="nationImgFile"
src='http://static.eaw1805.com/images/nations/nation-1-list.jpg'
alt="Nation's Flag"
class="toolTip"
title="#"
border=0 height=33>

<div>
<h3 style="margin-top:20px;margin-left: 70px; float: left; width: 150px;">Position costs &nbsp;
<div id="cost" style="float:right;width: 20px;margin-right: 5px;"/>
</h3>
<img style="margin-top:25px;float:left;" src='http://static.eaw1805.com/images/goods/good-1.png'
alt="Credits"
class="toolTip"
title="Credits"
border=0 height=20>

<h3 style="margin-top:20px;margin-left: 7px; float: left; width: 80px;">per turn</h3>
</div>

<form style="clear: both;" method="get" id="joinForm" action='<c:url value="/scenario/1/game/1/pickup/1"/>'>
<button class="ok" id="ok" onfocus=" this.blur();" style="margin-top: 20px; margin-right: 5px;"
title="Pick up position "></button>
<button class="cancel" title="Cancel" id="cancel" value="Cancel" style="margin-left: 5px;"
onclick="$.unblockUI();$('#'+$('#cancel').attr('aria-describedby')).hide();return false;"
aria-describedby=""></button>
</form>
<div>
<img style="float:left;margin-left: 20px; margin-top: -5px;"
src='http://static.eaw1805.com/images/buttons/taxation/MUINormalTaxSlc.png'
alt="Credits"
class="toolTip"
title="Account Balance"
border=0 height=32>

<div style="float: left;font-size: 20px; margin: 2px;margin-top: -2px; margin-left: 5px;">${user.creditFree+user.creditTransferred+user.creditBought}</div>
</div>
</div>


<div id="creditsDialog" style="cursor: default;
margin-top: -20px;
height: 700px; width: 800px;
background-image: url(http://static.eaw1805.com/images/site/Politics_parchment.png) !important;
background-position: 0px 0px !important;
background-repeat: no-repeat;
background-size: 802px 700px; display: none;">
<h3 style="width: 300px;font-size: 36px; padding-top: 10px; padding-left: 20px;">Acquire
&nbsp;<img src="http://static.eaw1805.com/images/goods/good-1.png"
height=30
style="float:none;vertical-align: top !important; padding-top: 6px;"
alt="Empires at War 1805 Credits" title="EaW Credits"></h3>

<p class="manual" style="font-size: 12pt; margin-left: 15pt; margin-top: -5px; width: 750px;">Empires at War works
with a credits' system: You need
<img src="http://static.eaw1805.com/images/goods/good-1.png" height="18"
style="float:none;vertical-align: middle !important;"
alt="Empires at War 1805 Credits" title="EaW Credits">
in order to pick up and run Empires, and you can purchase them here.
The &#8364;-to-<img src="http://static.eaw1805.com/images/goods/good-1.png" height="18"
style="float:none;vertical-align: middle !important;"
alt="Empires at War 1805 Credits" title="EaW Credits">
ratio is 1 to 10,
so for example 20 &#8364; will buy you 200 <img src="http://static.eaw1805.com/images/goods/good-1.png"
height="18"
style="float:none;vertical-align: middle !important;"
alt="Empires at War 1805 Credits" title="EaW Credits">,
enough credit for you to play a single Empire for months.</p>

<div style="margin-left:100px; margin-top:-5px;
float: left;
width:209px; height:470px;
background: url('http://static.eaw1805.com/images/site/paypal/BuyCreditsButtonsBase.png') no-repeat;
background-size: 209px 470px !important;">
<form:form commandName="paypalCommand" method="POST" action='paypal'
           style="width:182px !important; height:100px !important;
           background-size: 182px 100px !important;
           float: none! important;
           clear: both;
           margin-left: 13px!important;
           margin-top: 22px!important;">
    <form:input type="hidden" path="amount" id="amount" value="20"/>
    <input type="submit" class="paypal-20-input" value="" onfocus=" this.blur();"
           title="Buy 200 credits for 20 &#8364;"
           style="width:182px !important; height:100px !important;
                   background-size: 182px 100px !important;
                   float: none! important;">
</form:form>
<form:form commandName="paypalCommand" method="POST" action='paypal'
           style="width:182px !important; height:106px !important;
           background-size: 182px 106px !important;
           float: none! important;
           clear: both;
           margin-left: 12px!important;
           margin-top: 0px!important;">
    <div style="margin:0;padding:0"></div>
    <div class="formbody">
        <form:input type="hidden" path="amount" id="amount" value="30"/>
        <input type="submit" class="paypal-30-input" value="" title="Buy 330 credits for 30 &#8364;"
               style="width:182px !important; height:106px !important;
                       background-size: 182px 106px !important;
                       float: none! important;">
    </div>
</form:form>
<form:form commandName="paypalCommand" method="POST" action='paypal'
           style="width:182px !important; height:100px !important;
           background-size: 182px 100px !important;
           float: none! important;
           clear: both;
           margin-left: 14px!important;
           margin-top: 11px!important;">
    <div style="margin:0;padding:0"></div>
    <div class="formbody">
        <form:input type="hidden" path="amount" id="amount" value="50"/>
        <input type="submit" class="paypal-50-input" value="" title="Buy 560 credits for 50 &#8364;"
               style="width:182px !important; height:100px !important;
                       background-size: 182px 100px !important;
                       float: none! important;">
    </div>
</form:form>
<form:form commandName="paypalCommand" method="POST" action='paypal'
           style="width:182px !important; height:100px !important;
           background-size: 182px 100px !important;
           float: none! important;
           clear: both;
           margin-left: 13px!important;
           margin-top: 7px!important;">
    <div style="margin:0;padding:0"></div>
    <div class="formbody">
        <form:input type="hidden" path="amount" id="amount" value="100"/>
        <input type="submit" class="paypal-100-input" value="" title="Buy 1200 credits for 100 &#8364;"
               style="width:182px !important; height:100px !important;
                        background-size: 182px 100px !important;
                        float: none! important;">
    </div>
</form:form>
</div>
<div style="float: left;width: 170px;height: 470px;color: black;margin-top: 8px;">
<div style="font-size: 20px;height: 100px;margin-top: 47px; font-family: Georgia, helvetica, arial, freesans, clean, sans-serif;
text-shadow: 1px 1px 0.1px #7B714B;">for only 20 &#8364;!
</div>
<div style="font-size: 20px;height: 100px;margin-top: 11px; font-family: Georgia, helvetica, arial, freesans, clean, sans-serif;
text-shadow: 1px 1px 0.1px #7B714B;">for only 30 &#8364;!
</div>
<div style="font-size: 20px;height: 100px;margin-top: 11px; font-family: Georgia, helvetica, arial, freesans, clean, sans-serif;
text-shadow: 1px 1px 0.1px #7B714B;">for only 50 &#8364;!
</div>
<div style="font-size: 20px;height: 100px;margin-top: 11px; font-family: Georgia, helvetica, arial, freesans, clean, sans-serif;
text-shadow: 1px 1px 0.1px #7B714B;">for only 100 &#8364;!
</div>
</div>
<div style="float: right;width: 200px;height: 400px;color: black;
margin-top: 40px; margin-right:40px;">
<p class="manual" style="font-size: 12pt;">
The positions listed in the <a href="<c:url
        value="/scenario/1802/info"/>">Scenario 1805 info page</a> indicate the cost of each
position so that you can chose to play according to your budget.</p>

<p class="manual" style="font-size: 12pt; margin-top:115px;">
Price differences have nothing to do with the ability of an
empire to win the game, they are simply being charged based on their popularity amongst
players: the less popular empires receive a discount.
</p>
</div>
<p class="manual"
style="font-size: 12pt; margin-top: -5px; margin-left: 15pt; width: 750px; float: left; clear:both;">
Payments are being processed through PayPal.
Oplon Games has no access to your credit card in any way. Once you choose your preferred credit amount,
you will be redirected to a secure location outside our website were you will be able to
access your PayPal account.</p>
<button class="cancel" style="float: right; margin-right: 25px; margin-top: -25px;" value="No"
onclick="unblock();"/>
</div>
