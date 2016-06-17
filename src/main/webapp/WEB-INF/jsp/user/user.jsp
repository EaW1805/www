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
<jsp:useBean id="pendingGames" scope="request" type="java.util.List<com.eaw1805.data.model.UserGame>"/>
<jsp:useBean id="pendingUsers" scope="request" type="java.util.Map<java.lang.Integer, com.eaw1805.data.model.User>"/>
<jsp:useBean id="userGameOrders" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Boolean>"/>
<jsp:useBean id="userGameVps" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
<jsp:useBean id="userGameStats" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.lang.String>>"/>
<jsp:useBean id="userGamesDeadTurns" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>

<jsp:useBean id="activityStat" scope="request" type="java.util.Map"/>
<jsp:useBean id="name" scope="request" class="java.lang.String"/>
<jsp:useBean id="gameStatsMessages" scope="request" type="java.util.Map<java.lang.String, java.lang.String>"/>

<jsp:useBean id="freeNations" scope="request" type="java.util.LinkedList<java.util.LinkedList<java.lang.Object>>"/>
<jsp:useBean id="specialOffer" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Boolean>"/>
<jsp:useBean id="profileStats" scope="request" type="java.util.HashMap<java.lang.String, java.lang.Object>"/>

<jsp:useBean id="scenario1804" scope="request" class="java.lang.Integer"/>

<jsp:useBean id="connectedFriends" scope="request" type="java.util.List<com.eaw1805.data.model.User>"/>
<%--<jsp:useBean id="postMessages" scope="request" type="java.util.List<com.eaw1805.data.model.PostMessage>"/>--%>

<jsp:useBean id="watchNews" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="followingAchievements" scope="request" type="java.util.List<com.eaw1805.data.model.Achievement>"/>


<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.sparkline.min.js'></script>

<div id="fb-root"></div>
<script>
    function getObjectSize(myObject) {
        var count = 0
        for (var key in myObject)
            count++
        return count
    }
    window.fbAsyncInit = function () {
        // init the FB JS SDK
        FB.init({
            appId: '105656276218992', // App ID from the App Dashboard
            /*channelUrl : '//WWW.YOUR_DOMAIN.COM/channel.html', // Channel File for x-domain communication*/
            status: false, // check the login status upon init?
            cookie: false, // set sessions cookies to allow your server to access the session?
            xfbml: true  // parse XFBML tags on this page?
        });

        // Additional initialization code such as adding Event Listeners goes here
        FB.Event.subscribe('edge.create',
                function (response) {
                    if (response != null) {
                        window.location = '<c:url value="/social/fb_like?redirect=/games"/>';
                    }
                }
        );
    };

    // Load the SDK's source Asynchronously
    (function (d) {
        var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
        if (d.getElementById(id)) {
            return;
        }
        js = d.createElement('script');
        js.id = id;
        js.async = true;
        js.src = "//connect.facebook.net/en_US/all.js";
        ref.parentNode.insertBefore(js, ref);
    }(document));

    window.twttr = (function (d, s, id) {

        var t, js, fjs = d.getElementsByTagName(s)[0];

        if (d.getElementById(id)) return;
        js = d.createElement(s);
        js.id = id;

        js.src = "//platform.twitter.com/widgets.js";
        fjs.parentNode.insertBefore(js, fjs);

        return window.twttr || (t = {
                    _e: [], ready: function (f) {
                        t._e.push(f)
                    }
                });

    }(document, "script", "twitter-wjs"));


    function postToFeed() {

        // calling the API ...
        var obj = {
            method: 'feed',
            link: 'http://www.eaw1805.com',
            picture: 'http://static.eaw1805.com/images/armies/5/33.jpg',
            name: 'Empires at War 1805',
            caption: 'Be the leader of a European power during the troublesome times starting in 1805.',
            description: 'Challenge your opponents in a complete web-based warfare system, depicting the richness of the combined arms military systems of the Napoleonic era. ' +
            '<center/>&nbsp;<center/>' +
            'Do you believe you can dominate Europe at the dawn of the 19th century ?'
            //actions:[
            //    { name:'share', link:'link here' }
            //]
        };

        function callback(response) {
            if (response != null) {
                window.location = '<c:url value="/social/fb_share?redirect=/games"/>';
            }
        }

        FB.ui(obj, callback);
    }

    // Define our custom event hanlders
    function clickEventToAnalytics(intent_event) {
        if (intent_event) {
            var label = intent_event.region;
            _gaq.push(['_trackEvent', 'twitter_web_intents', intent_event.type, label]);
            console.log(intent_event);
        }
        ;
    }

    function tweetIntentToAnalytics(intent_event) {
        if (intent_event) {
            var label = "tweet";
            _gaq.push(['_trackEvent', 'twitter_web_intents', intent_event.type, label]);
            window.location = '<c:url value="/social/tw_tweet?redirect=/games"/>';
        }
        ;
    }

    function favIntentToAnalytics(intent_event) {
        tweetIntentToAnalytics(intent_event);
    }

    function retweetIntentToAnalytics(intent_event) {
        if (intent_event) {
            var label = intent_event.data.source_tweet_id;
            _gaq.push(['_trackEvent', 'twitter_web_intents', intent_event.type, label]);
        }
    }

    function followIntentToAnalytics(intent_event) {
        if (intent_event) {
            var label = intent_event.data.user_id + " (" + intent_event.data.screen_name + ")";
            _gaq.push(['_trackEvent', 'twitter_web_intents', intent_event.type, label]);
            setTimeout(function () {
                window.location = '<c:url value="/social/tw_follow?redirect=/games"/>'
            }, 3000);
        }

    }

    // Wait for the asynchronous resources to load
    twttr.ready(function (followIntentToAnalyticstwttr) {
        // Now bind our custom intent events
        twttr.events.bind('click', clickEventToAnalytics);
        twttr.events.bind('tweet', tweetIntentToAnalytics);
        twttr.events.bind('retweet', retweetIntentToAnalytics);
        twttr.events.bind('favorite', favIntentToAnalytics);
        twttr.events.bind('follow', followIntentToAnalytics);
    });

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
        $.blockUI({message: $('#question'), css: {width: '390px'}});
    }


    $.blockUI.defaults.applyPlatformOpacityRules = false;
    var costPerCredit = 1;
    var isConfirmDialogOpen = false;
    function openCreditsForm() {
        $.blockUI({
            message: $('#creditsDialog'), css: {
                width: '800px', height: '700px', margin: '-43px', top: '10%', left: '26%',
                background: 'none'
            }
        });
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

    function openInvitationForm(userGame) {
        $.blockUI({message: $('#invitation_' + userGame), css: {width: '390px'}});
    }

    function answerInvitation(answer, scenario, game, nation) {
        <c:url var="thisUrl" value="/"/>
        var url;
        if (answer) {
            url = "${thisUrl}" + "invitation/scenario/" + scenario + "/game/" + game + "/nation/" + nation + "/accept";
        } else {
            url = "${thisUrl}" + "invitation/scenario/" + scenario + "/game/" + game + "/nation/" + nation + "/reject";
        }
        $.ajax({
            url: url,
            type: 'post',
            dataType: "html"
        }).done(function (data) {
            $.unblockUI();
            var accept = (data.split(",")[0] == "a");
            var success = (data.split(",")[1] == "1");
            if (!success) {
                alert("An error occured, Invitation is no longer available");
            }
            if (accept) {
                window.location.reload();
                return;
            }
            currentInvitation++;
            if (getObjectSize(invitationRequests) > currentInvitation) {

                openInvitationForm(invitationRequests[currentInvitation]);
            }

        });

    }

    function skipInvitation() {
        $.unblockUI();
        currentInvitation++;
        if (getObjectSize(invitationRequests) > currentInvitation) {
            openInvitationForm(invitationRequests[currentInvitation]);
        }
    }

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
        $.blockUI({message: $('#error_message_${param.err}'), css: {width: '390px'}});
        </c:if>
    });
</script>
<div id="main-article"
     style="position: relative;margin: 0px 0px 0px -35px;width: 1033px;background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px -200px transparent; clear: both;">

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
    <c:set var="runningGames" value="0"/>
    <c:set var="finishedGames" value="0"/>
    <c:set var="gamesHeight" value="0"/>
    <c:set var="soloGameHeight" value="0"/>
    <c:if test="${fn:length(userGames) > 0}">
        <c:forEach items="${userGames}" var="thisGame">
            <c:if test="${thisGame.game.ended == false && thisGame.alive == true}">
                <c:set var="gamesHeight" value="${gamesHeight  + 225 + 176}"/>
                <c:set var="runningGames" value="${runningGames + 1}"/>
            </c:if>
            <c:if test="${thisGame.game.ended == true || thisGame.alive == false}">
                <c:set var="finishedGames" value="${finishedGames + 1}"/>
            </c:if>
        </c:forEach>
    </c:if>
    <c:if test="${scenario1804 > 0}">
        <c:set var="soloGameHeight" value="207"/>
    </c:if>

    <!-- compute height of top column -->
    <c:set var="connectionsHeight" value="230"/>
    <c:if test="${user.facebookStatus != 3 && user.twitterStatus != 3}">
        <c:set var="connectionsHeight" value="331"/>
    </c:if>
    <c:if test="${user.facebookStatus == 3 && user.twitterStatus == 3}">
        <c:set var="connectionsHeight" value="0"/>
    </c:if>
    <c:if test="${user.creditBought + user.creditFree + user.creditTransferred >= 50}">
        <c:set var="connectionsHeight" value="0"/>
    </c:if>

    <!-- compute minimum height -->
    <c:set var="minHeight" value="${sideHeight}"/>
    <c:if test="${gamesHeight + soloGameHeight > sideHeight}">
        <c:set var="minHeight" value="${gamesHeight + soloGameHeight}"/>
    </c:if>

    <c:if test="${scenario1804 == 0 && runningGames == 0}">
        <c:set var="minHeight" value="620"/>
    </c:if>


    <script type="text/javascript">
        //If IE show Warning.
        <%--if ($.browser.msie) {--%>
        <%--$('#main-article').css({ 'min-height':${minHeight + connectionsHeight + 10 + 220}});--%>

        <%--} else {--%>
        <%--$('#main-article').css({ 'min-height':${minHeight + connectionsHeight + 10}});--%>
        <%--}--%>
    </script>
    <article id="main-article-top">
        <c:if test="${user.creditBought + user.creditFree + user.creditTransferred < 50}">
            <c:if test="${user.facebookStatus != 3 || user.twitterStatus != 3}">
                <section class="connections" style="width: 950px;height: ${connectionsHeight}px">
                <h1 style="width: 700px;margin: 7px!important;font-size: 45px!important;margin-bottom: 20px!important;">
                    6 Simple Steps
                    to earn&nbsp;<img src="http://static.eaw1805.com/images/goods/good-1.png"
                                      height=30
                                      style="position: absolute !important; vertical-align: bottom !important; padding-top: 6px;"
                                      alt="Empires at War 1805 Credits" title="EaW Credits"></h1>

                <div style="height: 50px;">
                    <div style="float: left;width:200px;">
                        <ul style="list-style: none;">
                            <li>
                                1. Click on the "link" account
                            </li>
                            <li>
                                2. Connect to your facebook and/or twitter
                            </li>
                            <li>
                                3. Take the steps on the right to earn more free credits!
                            </li>
                        </ul>
                    </div>
                    <div style="float: right;width: 465px;text-align: justify;margin-right: 10px;">
                        <b>Important:</b> You have to do this through the EaW1805 website so that your account can claim
                        the free
                        credits! If -
                        for example - you have already "liked" us with your facebook profile, then 'unlike' us, and
                        repeat the process
                        from here!
                    </div>
                </div>

                <c:if test="${user.facebookStatus != 3}">
                    <div class="todo" style="margin-left: 5px;">
                        <h1 style="margin: 10px;">Link Account</h1>

                        <div class="connections" style="margin-top: 10px;float: left;width:230px;;">
                            <div class="connection-item">
                        <span class="connection-icon">
                        <c:choose>
                            <c:when test="${user.facebookStatus == 0}">
                                <img class="connect" style="width: 40px"
                                     src="http://static.eaw1805.com/images/site/Facebook.png"
                                     alt="Facebook">
                            </c:when>
                            <c:when test="${user.facebookStatus != 0}">
                                <img class="connected" style="width: 40px"
                                     src="http://static.eaw1805.com/images/site/Facebook.png"
                                     alt="Facebook">
                            </c:when>
                        </c:choose>
                        </span>
                                <span class="connection-provider">Facebook</span>
                            <span class="connection-control">
                                <c:choose>
                                    <c:when test="${user.facebookStatus == 0}">
                                        <form action="<c:url value="/connect/facebook" />" method="POST">
                                            <input type="hidden" name="redirect" value="<c:url value="/games" />"/>
                                            <input type="hidden" name="scope" value="publish_stream,offline_access"/>
                                            <button class="conButton" type="submit" value="connect">Connect</button>
                                        </form>
                                    </c:when>
                                    <c:when test="${user.facebookStatus != 0}">
                                        Connected
                                    </c:when>
                                </c:choose>
                            </span>
                                </span>
                            </div>
                        </div>
                        <div style="float: left;margin-top: 2px;font-size: 23px;height: 100px;width: 71px;text-align: center;">
                            &nbsp;&nbsp;&nbsp;&nbsp;<b>+10</b><br/><img
                                src="http://static.eaw1805.com/images/goods/good-1.png"
                                height=30
                                style="position: absolute !important; vertical-align: bottom !important; "
                                alt="Empires at War 1805 Credits" title="EaW Credits">
                        </div>
                    </div>
                    <c:if test="${user.facebookStatus > 0}">
                        <div class="todoDis" style="background-color: lightgoldenrodyellow">
                            <h1 style="margin: 25px!important;width: 100px;font-size: 52px!important;margin-left: 25px!important;}">
                                Completed</h1>
                        </div>
                    </c:if>

                    <div class="todo">
                        <h1 style="margin: 10px;">Like our Page</h1>

                        <div style="float: left; width: 232px;margin-left: 5px; overflow: hidden;height: 70px;">
                            <div style="overflow: hidden;" class="fb-like" data-href="http://facebook.com/empires1805"
                                 data-send="false"
                                 data-width="230" data-show-faces="true"></div>
                        </div>
                        <div style="float: left;margin-top: 4px;font-size: 23px;height: 100px;width: 71px;text-align: center;">
                            &nbsp;&nbsp;&nbsp;&nbsp;<b>+40</b><br/><img
                                src="http://static.eaw1805.com/images/goods/good-1.png"
                                height=30
                                style="position: absolute !important; vertical-align: bottom !important; "
                                alt="Empires at War 1805 Credits" title="EaW Credits">
                        </div>
                    </div>

                    <c:if test="${user.facebookStatus < 1}">
                        <div class="todoDis">
                            <h1 style="margin: 25px!important;width: 100px;font-size: 52px!important;margin-left: 58px!important;}">
                                Disabled</h1>
                        </div>
                    </c:if>
                    <c:if test="${user.facebookStatus > 1}">
                        <div class="todoDis" style="background-color: lightgoldenrodyellow">
                            <h1 style="margin: 25px!important;width: 100px;font-size: 52px!important;margin-left: 25px!important;}">
                                Completed</h1>
                        </div>
                    </c:if>
                    <div class="todo">
                        <h1 style="margin: 10px;">Make a Post</h1>

                        <div class="connections" style="margin-top: 10px;float: left;width:230px;;">
                            <div class="connection-item">
                        <span class="connection-icon">
                        <c:choose>
                            <c:when test="${user.facebookStatus < 3}">
                                <img class="connect" style="width: 40px"
                                     src="http://static.eaw1805.com/images/site/Facebook.png"
                                     alt="Facebook">
                            </c:when>
                            <c:when test="${user.facebookStatus == 3}">
                                <img class="connected" style="width: 40px"
                                     src="http://static.eaw1805.com/images/site/Facebook.png"
                                     alt="Facebook">
                            </c:when>
                        </c:choose>
                        </span>
                                <span class="connection-provider">Facebook</span>
                            <span class="connection-control">
                                <c:choose>
                                    <c:when test="${user.facebookStatus <= 3}">
                                        <a onclick='postToFeed();return false;'>Post</a>
                                    </c:when>
                                    <c:when test="${user.facebookStatus == 3}">
                                        Connect
                                    </c:when>
                                </c:choose>
                            </span>
                                </span>
                            </div>
                        </div>
                        <div style="float: left;margin-top: 4px;font-size: 23px;height: 100px;width: 71px;text-align: center;">
                            &nbsp;&nbsp;&nbsp;&nbsp;<b>+60</b><br/><img
                                src="http://static.eaw1805.com/images/goods/good-1.png"
                                height=30
                                style="position: absolute !important; vertical-align: bottom !important; "
                                alt="Empires at War 1805 Credits" title="EaW Credits">
                        </div>
                    </div>
                    <c:if test="${user.facebookStatus < 2}">
                        <div class="todoDis">
                            <h1 style="margin: 25px!important;width: 100px;font-size: 52px!important;margin-left: 58px!important;}">
                                Disabled</h1>
                        </div>
                    </c:if>
                </c:if>

                <c:if test="${user.twitterStatus != 3}">
                    <div class="todo" style="margin-left: 5px;">
                        <h1 style="margin: 10px;">Link Account</h1>

                        <div class="connections" style="margin-top: 10px;float: left;width:230px;;">
                            <div class="connection-item">
                                <span class="connection-icon">
                                    <c:choose>
                                        <c:when test="${user.twitterStatus == 0}">
                                            <img class="connect" style="width: 40px"
                                                 src="http://static.eaw1805.com/images/site/Twitter.png"
                                                 alt="Twitter">
                                        </c:when>
                                        <c:when test="${user.twitterStatus != 0}">
                                            <img class="connected" style="width: 40px"
                                                 src="http://static.eaw1805.com/images/site/Twitter.png"
                                                 alt="Twitter">
                                        </c:when>
                                    </c:choose>
                                </span>
                                <span class="connection-provider">Twitter</span>
                                <span class="connection-control">
                                    <c:choose>
                                        <c:when test="${user.twitterStatus == 0}">
                                            <form action="<c:url value="/connect/twitter" />"
                                                  method="POST">
                                                <input type="hidden" name="redirect"
                                                       value="<c:url value="/games" />"/>
                                                <button class="conButton" type="submit" value="connect">
                                                    Connect
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:when test="${user.twitterStatus != 0}">
                                            Connected
                                        </c:when>
                                    </c:choose>
                                </span>
                            </div>

                        </div>
                        <div style="float: left;margin-top: 4px;font-size: 23px;height: 100px;width: 71px;text-align: center;">
                            &nbsp;&nbsp;&nbsp;&nbsp;<b>+10</b><br/><img
                                src="http://static.eaw1805.com/images/goods/good-1.png"
                                height=30
                                style="position: absolute !important; vertical-align: bottom !important; "
                                alt="Empires at War 1805 Credits" title="EaW Credits">
                        </div>
                    </div>
                    <c:if test="${user.twitterStatus > 0}">
                        <div class="todoDis" style="background-color: lightgoldenrodyellow">
                            <h1 style="margin: 25px!important;width: 100px;font-size: 52px!important;margin-left: 25px!important;}">
                                Completed</h1>
                        </div>
                    </c:if>

                    <div class="todo">
                        <h1 style="margin: 10px;">Follow Us</h1>

                        <div style="margin-left: 10px;margin-top: 20px;width:222px;float: left;">
                            <a href="https://twitter.com/eaw1805" class="twitter-follow-button" data-show-count="true"
                               data-lang="en">Follow @eaw1805</a>
                        </div>
                        <div style="float: left;margin-top: 4px;font-size: 23px;height: 100px;width: 71px;text-align: center;">
                            &nbsp;&nbsp;&nbsp;&nbsp;<b>+20</b><br/><img
                                src="http://static.eaw1805.com/images/goods/good-1.png"
                                height=30
                                style="position: absolute !important; vertical-align: bottom !important; "
                                alt="Empires at War 1805 Credits" title="EaW Credits">
                        </div>
                    </div>

                    <c:if test="${user.twitterStatus < 1}">
                        <div class="todoDis">
                            <h1 style="margin: 25px!important;width: 100px;font-size: 52px!important;margin-left: 58px!important;}">
                                Disabled</h1>
                        </div>
                    </c:if>
                    <c:if test="${user.twitterStatus > 1}">
                        <div class="todoDis" style="background-color: lightgoldenrodyellow">
                            <h1 style="margin: 25px!important;width: 100px;font-size: 52px!important;margin-left: 25px!important;}">
                                Completed</h1>
                        </div>
                    </c:if>


                    <div class="todo">
                        <h1 style="margin: 10px;">Make a Tweet</h1>

                        <div class="connections" style="margin-top: 10px;float: left;width:230px;;">
                            <div class="connection-item">
                            <span class="connection-icon">
                            <c:choose>
                                <c:when test="${user.twitterStatus < 3 }">
                                    <img class="connect" style="width: 40px"
                                         src="http://static.eaw1805.com/images/site/Twitter.png"
                                         alt="Twitter">
                                </c:when>
                                <c:when test="${user.twitterStatus == 3}">
                                    <img class="connected" style="width: 40px"
                                         src="http://static.eaw1805.com/images/site/Twitter.png"
                                         alt="Twitter">
                                </c:when>
                            </c:choose>
                            </span>
                                <span class="connection-provider">Twitter</span>
                                            <span class="connection-control">
                                                <c:choose>
                                                    <c:when test="${user.twitterStatus < 3}">
                                                        <a href="https://twitter.com/share"
                                                           class="twitter-share-button"
                                                           data-url="http://eaw1805.com"
                                                           data-counturl="http://eaw1805.com"
                                                           data-via="eaw1805"
                                                           data-text="Brand new HTML5 wargame! Rule an Empire.Test your Strategy skills.Can you prevail in the Napoleonic era?"
                                                           data-lang="en">Tweet</a>
                                                    </c:when>
                                                    <c:when test="${user.twitterStatus == 3}">
                                                        Connected
                                                    </c:when>
                                                </c:choose>
                                            </span>
                                </span>
                            </div>
                        </div>
                        <div style="float: left;margin-top: 4px;font-size: 23px;height: 100px;width: 71px;text-align: center;">
                            &nbsp;&nbsp;&nbsp;&nbsp;<b>+40</b><br/><img
                                src="http://static.eaw1805.com/images/goods/good-1.png"
                                height=30
                                style="position: absolute !important; vertical-align: bottom !important; "
                                alt="Empires at War 1805 Credits" title="EaW Credits">
                        </div>
                    </div>


                    <c:if test="${user.twitterStatus < 2}">
                        <div class="todoDis">
                            <h1 style="margin: 25px!important;width: 100px;font-size: 52px!important;margin-left: 58px!important;}">
                                Disabled</h1>
                        </div>
                    </c:if>
                </c:if>
            </c:if>
            </section>
        </c:if>

        <c:if test="${runningGames == 0 && scenario1804 == 0}">
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

        <c:if test="${runningGames > 0 || scenario1804 > 0}">
            <div style="float:left;width: 640px;">
                <c:if test="${scenario1804 > 0}">
                    <jsp:useBean id="scenario1804date" scope="request" class="java.lang.String"/>
                    <section class="gameInfo" id="gameInfo" style="height: 168px; float: left; ">
                        <h1 style="width: 440px;float: left; font-size: 27px !important;">Solo Game
                            / ${scenario1804date}</h1>
                        <img style="float:left; clear: both;"
                             src='http://static.eaw1805.com/images/nations/nation-5-trans.gif'
                             alt="Nation's Flag"
                             class="toolTip"
                             title="Flag of France"
                             border=0>

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

                        <div style="width:200px;float: right; margin-right: 6px; margin-top: -25px;">
                            <div style="width:200px;float: right; ">
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
                            </div>
                            <div style="width:200px;float: right; margin-top: 20px;">
                                <a href="javascript:$.blockUI({ message: $('#dropPopup_1804'), css: { width: '390px',cursor: 'default' } });"
                                   class="minibutton"
                                   title="Finish this game"
                                   style="float:right;line-height: 1.3!important;"><span
                                        style="font-size: 14px;">Finish Game</span></a>
                            </div>
                        </div>

                        <div style="float:right; clear: both; margin-top: -170px;margin-right: -10px; height: 0px;">
                            <img src='http://static.eaw1805.com/images/buttons/PlayButtonSmall.png'
                                 id="Play-Button-1804"
                                 usemap="#Play-Button-Map-1804"
                                 alt="Play Scenario 1804"
                                 title="Play Scenario 1804"
                                 align="right"
                                 border=0
                                 width=300
                                 onmouseover="this.src='http://static.eaw1805.com/images/buttons/PlayButtonHoverSmall.png'"
                                 onmouseout="this.src='http://static.eaw1805.com/images/buttons/PlayButtonSmall.png'"
                                 style="z-index: -10000; cursor: pointer;">
                            <map id="_Play-Button-Map-1804"
                                 name="Play-Button-Map-1804">
                                <area shape="rect" coords="10,41,170,208"
                                      href='<c:url value="/play/scenario/1804/game/${scenario1804}/nation/5"/>'
                                      onmouseover="document.getElementById('Play-Button-1804').src='http://static.eaw1805.com/images/buttons/PlayButtonHoverSmall.png'"
                                      onmouseout="document.getElementById('Play-Button-1804').src='http://static.eaw1805.com/images/buttons/PlayButtonSmall.png'"
                                      alt="Play Scenario 1804" title="Play Scenario 1804" target="_blank"/>
                                <area shape="rect" coords="169,83,289,171"
                                      href='<c:url value="/play/scenario/1804/game/${scenario1804}/nation/5"/>'
                                      onmouseover="document.getElementById('Play-Button-1804').src='http://static.eaw1805.com/images/buttons/PlayButtonHoverSmall.png'"
                                      onmouseout="document.getElementById('Play-Button-1804').src='http://static.eaw1805.com/images/buttons/PlayButtonSmall.png'"
                                      alt="Play Scenario 1804" title="Play Scenario 1804" target="_blank"/>
                            </map>
                        </div>
                    </section>
                </c:if>

                <c:if test="${fn:length(userGames) > 0 }">
                    <c:forEach items="${userGames}" var="game">
                        <jsp:useBean id="game" class="com.eaw1805.data.model.UserGame"/>
                        <c:set var="nation" value="${game.nation}"/>
                        <c:set var="vp" value="${userGameVps[game.game.gameId]}"/>
                        <c:set var="gameIndex" value="0"/>
                        <c:if test="${game.alive && !game.game.ended}">
                            <section class="gameInfo" id="gameInfo">
                                <c:set var="gamesPresentedCnt" value="${gamesPresentedCnt+1}"/>
                                <h1 style="width: 440px;float: left; font-size: 27px !important;"><a
                                        href="<c:url value="/scenario/${game.game.scenarioIdToString}/game/${game.game.gameId}/info"/>">Game ${game.game.gameId}</a>
                                    / ${dates[game.game.gameId]}
                                </h1>

                                <div style="clear: both; margin-left: 0px; float:right; margin-top: -40px; padding: 0px; margin-bottom: 0px;margin-right: 5px;">
                                    Deadline:
                                    <c:choose>
                                        <c:when test="${game.game.status.contains('processed')}">
                                            <b>${game.game.status}</b>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatDate type="both" dateStyle="default" timeStyle="default"
                                                            value="${game.game.dateNextProc}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div style="float: left; margin-left: 18px; margin-top: -5px;">
                                    <b>Scenario ${game.game.scenarioIdToString}</b>
                                </div>
                                <div style="display:none;" id="dropPopup_${game.game.gameId}_${nation.id}">
                                    <h3 style="margin:auto; width:30%; margin-top: 17px;">Drop position</h3>

                                    <div style="margin-left: 30px;color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                text-shadow: 1px 1px 0.1px rgb(123, 113, 75);font-weight: normal;letter-spacing: -1px;
                font-size: 16px;text-align: left; margin-top:5px;clear: both;">
                                        You are about to drop your position in game #${game.game.gameId} with
                                    </div>

                                    <img style="margin-top: 4px; margin-right: 20px;" id="nationImg"
                                         src='http://static.eaw1805.com/images/nations/nation-${nation.id}-list.jpg'
                                         alt="Nation's Flag"
                                         class="toolTip"
                                         title="${nation.name}"
                                         border=0 height=33>

                                    <div style="margin-left: 75px;color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                text-shadow: 1px 1px 0.1px rgb(123, 113, 75);font-weight: normal;letter-spacing: -1px;
                font-size: 16px;text-align: left;clear: both;">
                                        Do you want to drop your position?
                                    </div>

                                    <button class="ok" onfocus=" this.blur();" title="Drop position"
                                            style="clear: both; margin-top: 15px;margin-right: 5px;"
                                            onclick="window.location='<c:url
                                                    value="/scenario/${game.game.scenarioIdToString}/game/${game.game.gameId}/drop/${nation.id}"/>';"
                                            value="Drop Position"/>
                                    <button id="cancelb" class="cancel" title="Cancel" onfocus=" this.blur();"
                                            style="clear: both; margin-top: 15px;margin-left: 5px; " aria-describedby=""
                                            onclick="$.unblockUI();$('#'+$('#cancelb').attr('aria-describedby')).hide();return false;"
                                            value="cancel"/>
                                </div>

                                <img style="float:left; clear: both;"
                                     src='http://static.eaw1805.com/images/nations/nation-${nation.id}-trans.gif'
                                     alt="Nation's Flag"
                                     class="toolTip"
                                     title="Flag of ${nation.name}"
                                     border=0>

                                <div style="float: right; margin-right: 5px;margin-top: -15px;">
                                    <h1 style="float: right; font-size: 100px!important; margin-top: -5px!important; text-align: right;">
                                        vp: ${vp}</h1>
                                </div>

                                <c:set var="thisGameStats" value="${userGameStats[game.game.gameId]}"/>
                                <c:set var="index" value="${0}"/>
                                <div style="clear:both;float: left;margin-left: 5px; width: 150px;height: 124px;">
                                    <h3 style="float: right;">World position</h3>

                                    <c:forEach items="${thisGameStats}" var="stats">
                                        <c:if test="${index < 6}">
                                            <div style="clear:both;float: left;margin-left: 5px;width: 190px;">
                                                <div style="float: left;width: 150px;text-align: right;margin-right: 10px;">${gameStatsMessages[stats.key]} </div>
                                                <div style="float: left;width: 25px;">${stats.value}</div>
                                            </div>
                                        </c:if>
                                        <c:set var="index" value="${index+1}"/>
                                    </c:forEach>
                                </div>

                                <div style="float: left; margin-left: 65px; width: 125px;">
                                    <h3 style="float:right;">World Domination</h3>

                                    <h2 style="float:right; font-size: 30px!important; text-align: center; width: 125px;">
                                        <c:choose>
                                            <c:when test="${game.game.type == 0}">
                                                <fmt:formatNumber
                                                        type="number"
                                                        maxFractionDigits="2"
                                                        groupingUsed="true"
                                                        value="${100 * vp / nation.vpWin}"/>%
                                            </c:when>
                                            <c:when test="${game.game.type == -1}">
                                                <fmt:formatNumber
                                                        type="number"
                                                        maxFractionDigits="2"
                                                        groupingUsed="true"
                                                        value="${100 * vp / (0.7 * nation.vpWin)}"/>%
                                            </c:when>
                                            <c:when test="${game.game.type == 1}">
                                                <fmt:formatNumber
                                                        type="number"
                                                        maxFractionDigits="2"
                                                        groupingUsed="true"
                                                        value="${100 * vp / (1.3 * nation.vpWin)}"/>%
                                            </c:when>
                                        </c:choose>
                                    </h2>
                                </div>

                                <div style="width:200px;float: right; margin-right: 6px; margin-top: -56px;">
                                    <div style="width:200px; float: right;">
                                        <c:choose>
                                            <c:when test="${userGameOrders[game.game.gameId] == false}">
                                                <span style="padding-top: 4px; float:right; font-size: 14px; font-weight: bold;">No orders submitted</span>
                                            </c:when>
                                            <c:when test="${userGameOrders[game.game.gameId] == true}">
                                                <a href='<c:url value="/report/scenario/${game.game.scenarioIdToString}/game/${game.game.gameId}/nation/${nation.id}/orders/${game.game.turn}"/>'
                                                   title="Review orders submitted"
                                                   style="padding-top: 4px; float:right; line-height: 1.3!important;"><span
                                                        style="font-size: 14px;">Review Orders</span></a>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                    <div style="z-index:2; width:200px;float: right;  margin-top: 8px">
                                        <a href='<c:url value="/report/scenario/${game.game.scenarioIdToString}/game/${game.game.gameId}/nation/${nation.id}/newsletter"/>'
                                           title="Read Newsletter"
                                           style="float:right;line-height: 1.3!important;"><span
                                                style="font-size: 14px;">Newsletter</span></a>
                                    </div>
                                    <div style="z-index:2; width:200px;float: right; ">
                                        <a href='<c:url value="report/scenario/${game.game.scenarioIdToString}/game/${game.game.gameId}/nation/${nation.id}/overview"/>'
                                           title="Examine Reports"
                                           style="float:right;line-height: 1.3!important;"><span
                                                style="font-size: 14px;">Reports</span></a>
                                    </div>
                                    <div style="z-index:2; width:200px;float: right; ">
                                        <a href='http://forum.eaw1805.com/viewforum.php?f=${game.game.forumId}'
                                           title="Game Forum"
                                           style="float:right;line-height: 1.3!important;"><span
                                                style="font-size: 14px;">Game Forum</span></a>
                                    </div>
                                    <div style="z-index:2; width:200px;float: right; ">
                                        <a href='<c:url value="/scenario/${game.game.scenarioIdToString}/nation/${nation.id}"/>'
                                           title="Nation Info Page"
                                           style="float:right;line-height: 1.3!important;"><span
                                                style="font-size: 14px;">Nation Info Page</span></a>
                                    </div>

                                    <div style="z-index:2; width:200px;float: right; margin-top: 10px;">
                                        <a href="javascript:$.blockUI({ message: $('#dropPopup_${game.game.gameId}_${nation.id}'), css: { width: '390px',cursor: 'default' } });"
                                           class="minibutton"
                                           title="Leave this game"
                                           style="float:right;line-height: 1.3!important;"><span
                                                style="font-size: 14px;">Drop Position</span></a>
                                    </div>
                                </div>
                                <div style="clear:both;margin-left: 15px;float: left; margin-top: 15px">
            <span style="float:left;" class="inlinebar">
                <c:if test="${game.game.turn > 0}">
                    <c:forEach items="${globalActivityStat[game.game.gameId]}" var="turn">${turn},
                    </c:forEach>
                </c:if>
                <c:if test="${game.game.turn == 0}">&nbsp;</c:if>
            </span>
            <span style="clear:both;float:left;margin-top: -14px;" class="inlinebar-player">
                <c:if test="${game.game.turn > 0}">
                    <c:forEach
                            items="${activityStat[game.game.gameId][nation.id]}"
                            var="turn">${turn},
                    </c:forEach>
                </c:if>
                <c:if test="${game.game.turn == 0}">&nbsp;</c:if>
            </span>
                                </div>
                                <div style="clear: both;float: left; margin-left: 5px;">
                                    last process: <fmt:formatDate type="both" dateStyle="default"
                                                                  timeStyle="default"
                                                                  value="${game.game.dateLastProc}"/>
                                </div>
                                <div style="z-index:1; float:right; clear: both; margin-top: -170px;margin-right: -10px; height: 1px;">
                                    <img src='http://static.eaw1805.com/images/buttons/PlayButtonSmall.png'
                                         id="Play-Button-${game.game.gameId}-${nation.id}"
                                         usemap="#Play-Button-Map-${game.game.gameId}-${nation.id}"
                                         alt="Play game"
                                         title="Play game"
                                         align="right"
                                         border=0
                                         width=300
                                         onmouseover="this.src='http://static.eaw1805.com/images/buttons/PlayButtonHoverSmall.png'"
                                         onmouseout="this.src='http://static.eaw1805.com/images/buttons/PlayButtonSmall.png'"
                                         style="z-index: -10000; cursor: pointer;">
                                    <map id="_Play-Button-Map-${game.game.gameId}-${nation.id}"
                                         name="Play-Button-Map-${game.game.gameId}-${nation.id}">
                                        <area shape="rect" coords="10,30,170,208"
                                              href='<c:url value="/play/scenario/${game.game.scenarioIdToString}/game/${game.game.gameId}/nation/${nation.id}"/>'
                                              onmouseover="document.getElementById('Play-Button-${game.game.gameId}-${nation.id}').src='http://static.eaw1805.com/images/buttons/PlayButtonHoverSmall.png'"
                                              onmouseout="document.getElementById('Play-Button-${game.game.gameId}-${nation.id}').src='http://static.eaw1805.com/images/buttons/PlayButtonSmall.png'"
                                              alt="Play position" title="Play position" target="_blank"/>
                                        <area shape="rect" coords="169,83,289,171"
                                              href='<c:url value="/play/scenario/${game.game.scenarioIdToString}/game/${game.game.gameId}/nation/${nation.id}"/>'
                                              onmouseover="document.getElementById('Play-Button-${game.game.gameId}-${nation.id}').src='http://static.eaw1805.com/images/buttons/PlayButtonHoverSmall.png'"
                                              onmouseout="document.getElementById('Play-Button-${game.game.gameId}-${nation.id}').src='http://static.eaw1805.com/images/buttons/PlayButtonSmall.png'"
                                              alt="Play position" title="Play position" target="_blank"/>
                                    </map>
                                </div>
                            </section>
                        </c:if>
                    </c:forEach>
                </c:if>

            </div>
            <div style="float: right;width: 230px;">
                <section id="thisBrowser" class="topRight" style="display: none;height: 220px;">
                    <h1>Warning</h1>

                    <div style="margin: 5px;margin-bottom:20px;text-align: justify">We
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

                <section class="balance" id="balance-section"
                         style="float: right; margin-right: 8px; width: 305px; height: 380px;">
                    <h1 style="width: 305px;float: left; font-size: 27px !important; padding-top: 8px;">
                        <img style="float:left;margin-left: 0px; margin-top: -8px; margin-right: 10px; border-width: 0px;"
                             src='http://static.eaw1805.com/images/buttons/taxation/MUINormalTaxSlc.png'
                             alt="Credits"
                             class="title"
                             title="Account Balance"
                             border=0 height=32>
                        Available Credits
                        <a href="<c:url value="/user/paymentHistory"/>">
                            <img style="z-index: 2; float:right; margin-left: 0px; margin-top: -1px; margin-right: 7px; border-width: 0px;"
                                 src='http://static.eaw1805.com/images/layout/buttons/unitMenu/ButReviewOrdersOff.png'
                                 alt="Account History"
                                 class="title"
                                 title="Account History"
                                 onmouseover="this.src='http://static.eaw1805.com/images/layout/buttons/unitMenu/ButReviewOrdersHover.png'"
                                 onmouseout="this.src='http://static.eaw1805.com/images/layout/buttons/unitMenu/ButReviewOrdersOff.png'"
                                 border=0 height=24></a>
                    </h1>

                    <h1 style="width: 185px;float: left; font-size: 27px !important; padding-top: 0px; margin-top: -9px !important; text-align: right;">
                            ${user.creditFree+user.creditTransferred+user.creditBought}
                        <img style="float: right; border: 0; margin-left: 10px; margin-top: 0px; padding: 0;"
                             src='http://static.eaw1805.com/images/goods/good-1.png'
                             title="Credits"
                             alt="Credits"
                             class="toolTip"
                             border=0 height=24/>
                    </h1>

                    <div style="width:290px; margin-top: 5px; float: left;">
                        <label for="free_credits" style="width: 250px;">Credit analysis:</label>
                    </div>
                    <div style="height:20px; width:120px; margin-left: 10px; margin-top: 2px; margin-right: 14px; float: left; clear:both;">
                        <label for="free_credits" style="width: 50px;">Free:</label>
        <span id="free_credits" style="width: 70px;text-align: right; float: left;">${user.creditFree}
        <img style="float: right; border: 0; margin-left: 5px; margin-top: 0px; padding: 0;"
             src='http://static.eaw1805.com/images/goods/good-1.png'
             title="Credits"
             alt="Credits"
             class="toolTip"
             border=0 height=15/></span>
                    </div>
                    <div style="height:20px; width:130px;margin-top: 2px; margin-right: 12px; float: left; text-align: center;">
                        <label for="trans_credits" style="width: 68px;">Transferred:</label>
        <span id="trans_credits" style="width: 70px;text-align: right; float: left;">${user.creditTransferred}
        <img style="float: right; border: 0; margin-left: 5px; margin-top: 0px; padding: 0;"
             src='http://static.eaw1805.com/images/goods/good-1.png'
             title="Credits"
             alt="Credits"
             class="toolTip"
             border=0 height=15/></span>
                    </div>

                    <div style="height:20px; width:150px;margin-left: 10px; margin-top: 0px; float: left;">
                        <label for="bought_credits" style="width: 50px;">Bought:</label>
        <span id="bought_credits" style="width: 70px; text-align: right; float: left;">${user.creditBought}
        <img style="float: right; border: 0; margin-left: 5px; margin-top: 0px; padding: 0;"
             src='http://static.eaw1805.com/images/goods/good-1.png'
             title="Credits"
             alt="Credits"
             class="toolTip"
             border=0 height=15/></span>
                    </div>

                    <div style="width:290px; margin-left: 5px; margin-top: 10px; float: left;">
                        <p class="manual" style="font-size: 11pt;">Empires at War works with a credits' system: You need
                            <img src="http://static.eaw1805.com/images/goods/good-1.png" height="12"
                                 style="position: relative; float: none; border:0; margin: 0px !important; margin-top:-2px !important; padding:0px !important;  vertical-align: middle !important;"
                                 alt="Empires at War 1805 Credits" title="EaW Credits">
                            in order to pick up and run Empires, and you can purchase them here.
                            The &#8364;-to-<img src="http://static.eaw1805.com/images/goods/good-1.png" height="12"
                                                style="border:0; position: relative; margin: 0px !important; margin-top:-2px !important; padding:0px !important; float: none; vertical-align: middle !important;"
                                                alt="Empires at War 1805 Credits" title="EaW Credits">
                            ratio is 1 to 10, so for example 20 &#8364;
                            will buy you 200 <img src="http://static.eaw1805.com/images/goods/good-1.png" height="12"
                                                  style="border:0; position: relative; margin: 0px !important; margin-top:-2px !important; padding:0px !important; float: none; vertical-align: middle !important;"
                                                  alt="Empires at War 1805 Credits" title="EaW Credits">,
                            enough credit for you to play a single Empire for months.</p>
                    </div>

                    <div style="height:20px; width:300px;margin-top: 5px; float: left;">
                        <a href="javascript:openCreditsForm()" style="font-size: 17px !important;">
                            <img src="http://static.eaw1805.com/images/site/paypal/BuyCreditsButtonOff.png"
                                 width=280
                                 onmouseover="this.src='http://static.eaw1805.com/images/site/paypal/BuyCreditsButtonHover.png'"
                                 onmouseout="this.src='http://static.eaw1805.com/images/site/paypal/BuyCreditsButtonOff.png'"
                                 title="Acquire Credits"
                                 style="margin-left: 15px;border: 0;padding: 0; margin-top: -13px;">
                        </a>
                    </div>
                </section>

                <c:if test="${fn:length(connectedFriends) > 0}">
                    <section class="balance" id="balance-section"
                             style="float: right; margin-right: 8px; width: 305px; height: ${friendsHeight}px;">
                        <h1 style="width: 305px; float: left; font-size: 27px !important; padding-top: 8px; margin-bottom: -8px !important;">
                            <a href="<c:url value="/settings"/>">
                                <img style="float:left; margin-left: 2px; margin-top: -6px; margin-right: 0px; border-width: 0px;"
                                     src='http://static.eaw1805.com/images/layout/buttons/unitMenu/ButCommandersOff.png'
                                     alt="Following Users"
                                     class="title"
                                     title="Following Users"
                                     onmouseover="this.src='http://static.eaw1805.com/images/layout/buttons/unitMenu/ButCommandersHover.png'"
                                     onmouseout="this.src='http://static.eaw1805.com/images/layout/buttons/unitMenu/ButCommandersOff.png'"
                                     border=0 height=32></a>
                            Your friends are online
                        </h1>
                        <ul class="avatars" style="margin: 0 !important; padding-left: 2px !important;">
                            <c:forEach items="${connectedFriends}" var="thisUser">
                                <li>
                                    <a href="<c:url value="/user/${thisUser.username}"/>"
                                       title="${thisUser.username}"><img
                                            src="https://secure.gravatar.com/avatar/${thisUser.emailEncoded}?s=31&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                            alt="" height="31" width="31"></a>
                                </li>
                            </c:forEach>
                        </ul>
                    </section>
                </c:if>

                <section class="balance" id="balance-section"
                         style="float: right; margin-right: 8px; width: 305px; height: 180px;">
                    <h1 style="width: 305px; float: left; font-size: 27px !important; padding-top: 8px;">Create Your
                        Game</h1>

                    <p class="manual">Start your own game, customize the rules, be the first to pick up an Empire, and
                        invite
                        whoever you want to join in your game!</p>

                    <div style="height:75px; width:300px;margin-top: 5px; float: left;">
                        <a href="<c:url value="/game/createCustom"/>" style="font-size: 17px !important;">
                            <img src="http://static.eaw1805.com/images/site/gameButtons/CreateCustomGameOff.png"
                                 width=300
                                 onmouseover="this.src='http://static.eaw1805.com/images/site/gameButtons/CreateCustomGameHover.png'"
                                 onmouseout="this.src='http://static.eaw1805.com/images/site/gameButtons/CreateCustomGameOff.png'"
                                 title="Create your Custom Game"
                                 style="margin-left: 5px;border: 0;padding: 0;">
                        </a>
                    </div>
                </section>
            </div>
        </c:if>

    </article>
</div>

<div id="main-article-bottom-2" style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
</div>

<c:if test="${fn:length(userGames) == 0 && scenario1804 == 0}">
    <div id="flash-container" style="display: none; position: relative;margin: 0px 0px 0px -35px; width: 1035px;min-height: 390px;
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
                    <img src="http://static.eaw1805.com/images/site/playnow.png"
                         alt="Play Now Empires at War 1805"
                         onclick="window.location='<c:url value="/joingame"/>';"
                         style="cursor: pointer;">
                    <!--[if !IE]>-->
                </object>
                <!--<![endif]-->
            </object>
        </div>
    </div>
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
            $('#free-new-nations').css({'min-height':${height*200 + 35}});
        </script>
        <nationList style="width: 954px; margin: 0 0 5px 3px;  border: none; float: left; padding-top: 30px;">
            <ul class="nationList">
                <c:forEach items="${newGames}" var="newGame">
                    <li class="nationList" style="cursor: pointer;height: 195px;width: 470px; float:left; margin:0; margin-right: 2px; margin-bottom: 2px;
border-right: 1px solid rgb(143, 143, 143); position: relative;"
                        onclick='window.location="<c:url value="/joingame/new"/>" '>
                        <c:if test="${newGame.key.userId != 2}">
                            <div style="position: absolute; top: 5px; left: 314px; width: 135px; z-index: 1;">
                                <h3 style="float:left;">Game owned by</h3>
                                <img src="https://secure.gravatar.com/avatar/${gameOwners[newGame.key.userId].emailEncoded}?s=36&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                     alt="${gameOwners[newGame.key.userId].username}"
                                     height="28"
                                     width="28"
                                     style="float:right; border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                     title="${gameOwners[newGame.key.userId].username}">

                            </div>
                        </c:if>
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
                                                 alt="${gameUsers[thisUserGame.userId].username}"
                                                 height="24"
                                                 width="24"
                                                 style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
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
            $('#free-nations').css({'min-height':${height*104 + 35}});
        </script>
        <nationList style="width: 954px; margin: 0 0 5px 3px;  border: none; float: left; padding-top: 30px;">
            <ul class="nationList">
                <c:set var="nationIndex" value="0"/>
                <c:forEach items="${freeNations}" var="thisNationData">
                    <c:if test="${nationIndex < 6}">
                        <li class="nationList" style="cursor: pointer;height: 100px;width: 311px; float:left; margin:0; margin-right: 2px; margin-bottom: 2px;
border-right: 1px solid rgb(143, 143, 143);" aria-describedby="" title="Play now"
                            onclick='openPickupForm("${thisNationData[0].scenarioIdToString}","${thisNationData[0].gameId}","${thisNationData[1].id}", "${thisNationData[1].name}", "${thisNationData[4].cost}");'>
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
                                                   text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);font-size: 1.6em;'>
                                    Game ${thisNationData[0].gameId}
                                </dd>
                                <dd class="nationList"
                                    style='width: 20pt; padding-top: 0px;margin-left: 50px; line-height: 1.3 !important;
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
                                </div>
                                <c:if test="${specialOffer[thisNationData[4].id]}">
                                    <div style="float: left; width: 90px; height:88px; margin-top:-83px; margin-left: -162px;">
                                        <img style="float: left; border: 0;margin: 0;padding: 0;"
                                             src='http://static.eaw1805.com/images/site/paypal/60-free-ribbon.png'
                                             title="Special Offer: Get 60 credits for FREE"
                                             alt="Special offer"
                                             class="toolTip"
                                             border=0/>
                                    </div>
                                </c:if>
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

<c:if test="${fn:length(followingAchievements) > 0}">
    <div style="z-index: 2;position: relative; margin: 0 -40px -10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
        <h1 class="bigmap" style="width: 600px !important; font-size: 36px; padding-left: 40px; padding-top: 20px;">
            Your friends' recent achievements</h1>
    </div>
    <div style="z-index: 4; position: relative; float: right; margin-right: 10px; margin-top:-20px; margin-bottom: -30px;">
        <a style="line-height: 1.05;" href="<c:url value="/settings"/>">Following<br>${followingCnt} players</a>
        <ul class="avatars">
            <c:forEach items="${followingList}" var="thisUser">
                <li>
                    <a href="<c:url value="/user/${thisUser.username}"/>" title="${thisUser.username}"><img
                            src="https://secure.gravatar.com/avatar/${thisUser.emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                            alt="" height="24" width="24"></a>
                </li>
            </c:forEach>
        </ul>
    </div>

    <div id="followingAchievements-body" style="z-index: 1; position: relative; margin: -10px 0 0 -35px;width: 1033px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
    repeat-y scroll 0px 0px transparent;clear: both; padding-left: 35px;">
        <article>
            <news style="width: 980px; border-width: 0px; margin-left: -35px; margin-top: 25px; margin-bottom: 0px;">
                <c:set var="count" value="0"/>
                <table width="100%" border=0 cellspacing="0" cellpadding="0">
                    <tr style="line-height: 9pt;">
                        <c:forEach items="${followingAchievements}" var="thisAchievement">
                        <td
                                <c:choose>
                                    <c:when test="${count % 3 > 0}">
                                        width="270px" style="border-left-style: dashed; border-left-width: 1px; border-left-color: black; padding-left: 10px;"
                                    </c:when>
                                    <c:otherwise>
                                        width="280px"
                                    </c:otherwise>
                                </c:choose>
                                >
                            <div class="flagEntry" style="padding-top: 6px; float:left; border: none;">
                                <a href="<c:url value="/user/${thisAchievement.user.username}"/>"
                                   title="${thisAchievement.user.username}"><img
                                        src="https://secure.gravatar.com/avatar/${thisAchievement.user.emailEncoded}?s=36&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                        style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                        alt="" height="36" width="36"></a>
                            </div>
                            <div class="flagEntry" style=" float:left; border: none; margin-left: -1px;">
                                <img src='http://static.eaw1805.com/images/achievements/ach-${thisAchievement.category}-${thisAchievement.level}.png'
                                     alt="${thisAchievement.description}"
                                     title="${thisAchievement.description}"
                                     style="border: 0; padding: 0; margin: 0;"
                                     height=48>
                            </div>
                            <div class="messageEntry"
                                    <c:choose>
                                        <c:when test="${count % 2 == 1}">
                                            style="width: 200px; float:left; margin-left: 15px; margin-top: 0px; left: 0px; top: 0px; margin-bottom: 15px;"
                                        </c:when>
                                        <c:otherwise>
                                            style="width: 220px; float:left; margin-left: 15px; margin-top: 0px; left: 0px; top: 0px; margin-bottom: 15px;"
                                        </c:otherwise>
                                    </c:choose>
                                    >
                                <div style="font-size: 7pt;"><strong>${thisAchievement.user.username}</strong>
                                </div>
                                <div style="text-align: left !important;">${thisAchievement.description}</div>
                            </div>
                        </td>
                        <c:if test="${count % 3 == 2}">
                    </tr>
                    <tr>
                        </c:if>
                        <c:set var="count" value="${count+1}"/>
                        </c:forEach>
                    </tr>
                </table>
            </news>
        </article>
    </div>

    <div id="followingAchievements-footer" style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
    </div>

    <script type="text/javascript">
        $(document).ready(function () {
            setTimeout(function () {
                $('#watchedGames-body').css({'height': $('#watchedGames-footer').position().top - $('#watchedGames-body').position().top + 15})
            }, 1000);
            $('#followingAchievements-body').css({'height': $('#followingAchievements-footer').position().top - $('#followingAchievements-body').position().top + 15});
        });
    </script>

</c:if>

<c:if test="${fn:length(watchNews) > 0}">
    <div style="z-index: 2;position: relative; margin: 0 -40px -10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
        <h1 class="bigmap" style="width: 600px !important; font-size: 36px; padding-left: 40px; padding-top: 20px;">
            Recent activity of watched games</h1>
    </div>
    <div style="z-index: 4; position: relative; float: right; margin-right: 10px; margin-top:-20px; margin-bottom: -30px;">
        <a style="line-height: 1.05;" href="<c:url value="/settings"/>">Watching<br>games</a>
        <ul class="avatars">
            <c:forEach items="${gameList}" var="thisGame">
                <li class="watch"><a
                        href="<c:url value="/scenario/${thisGame.scenarioIdToString}/game/${thisGame.gameId}/info"/>"
                        title="Game ${thisGame.gameId}">${thisGame.gameId}</a></li>
            </c:forEach>
        </ul>
    </div>

    <div id="watchedGames-body" style="z-index: 1; position: relative; margin: -10px 0 0 -35px;width: 1033px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
    repeat-y scroll 0px 0px transparent;clear: both; padding-left: 35px;">

        <article>
            <news style="width: 980px; border-width: 0px; margin-left: -35px; margin-top: 25px; margin-bottom: 0px;">
                <c:set var="count" value="0"/>
                <table width="100%" border=0 cellspacing="0" cellpadding="0">
                    <tr style="line-height: 9pt;">
                        <c:forEach items="${watchNews}" var="hofProfile">
                        <td
                                <c:choose>
                                    <c:when test="${count % 2 == 1}">
                                        width="470px" style="border-left-style: dashed; border-left-width: 1px; border-left-color: black; padding-left: 10px;"
                                    </c:when>
                                    <c:otherwise>
                                        width="480px"
                                    </c:otherwise>
                                </c:choose>
                                >
                            <div class="flagEntry" style=" float:left;">
                                <c:choose>
                                    <c:when test="${hofProfile.subject.id == -1}">
                                        <img src='http://static.eaw1805.com/images/nations/nation-none-36.png'
                                             alt="Neutral"
                                             title="Neutral"
                                             height=24>
                                    </c:when>
                                    <c:otherwise>
                                        <img src='http://static.eaw1805.com/images/nations/nation-${hofProfile.subject.id}-36.png'
                                             alt="Flag of ${hofProfile.subject.name}"
                                             title="Flag of ${hofProfile.subject.name}"
                                             height=24>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="messageEntry"
                                    <c:choose>
                                        <c:when test="${count % 2 == 1}">
                                            style="width: 410px; float:left; margin-left: 5px; margin-top: 0px; left: 0px; top: 0px; margin-bottom: 15px;"
                                        </c:when>
                                        <c:otherwise>
                                            style="width: 430px; float:left; margin-left: 5px; margin-top: 0px; left: 0px; top: 0px; margin-bottom: 15px;"
                                        </c:otherwise>
                                    </c:choose>
                                    >
                                <div style="font-size: 7pt;"><strong>Game ${hofProfile.game.gameId}</strong>
                                    (Scenario ${hofProfile.game.scenarioIdToString} / ${dates[hofProfile.game.gameId]})
                                    -- <i><fmt:formatDate type="date" dateStyle="default" timeStyle="default"
                                                          value="${hofProfile.game.dateLastProc}"/></i>
                                </div>
                                    ${hofProfile.text}
                            </div>
                        </td>
                        <c:if test="${count % 2 == 1}">
                    </tr>
                    <tr>
                        </c:if>
                        <c:set var="count" value="${count+1}"/>
                        </c:forEach>
                    </tr>
                </table>
            </news>
        </article>
    </div>

    <div id="watchedGames-footer" style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
    </div>

    <script type="text/javascript">
        $(document).ready(function () {
            setTimeout(function () {
                $('#watchedGames-body').css({'height': $('#watchedGames-footer').position().top - $('#watchedGames-body').position().top + 15})
            }, 1000);
            $('#watchedGames-body').css({'height': $('#watchedGames-footer').position().top - $('#watchedGames-body').position().top + 15});
        });
    </script>

</c:if>

<c:if test="${finishedGames > 0}">
    <div style="z-index: 2;position: relative; margin: 0 -40px 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
        <h1 class="bigmap" style="width: 600px !important; font-size: 36px; padding-left: 40px; padding-top: 20px;">
            Finished games</h1>
    </div>

    <div id="finishedGames-body" style="min-height:50px; position: relative; float: left; margin: -10px 0px 0px -35px;width: 1033px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
    repeat-y scroll 0px 0px transparent;clear: both; padding-left: 35px;">

        <article style="margin-left: 0px; padding-top: 20px;">
            <c:forEach items="${userGames}" var="game">
                <jsp:useBean id="game" class="com.eaw1805.data.model.UserGame"/>
                <c:set var="nation" value="${game.nation}"/>
                <c:set var="vp" value="${userGameVps[game.game.gameId]}"/>
                <c:set var="gameIndex" value="0"/>
                <c:if test="${game.alive == false || game.game.ended}">
                    <section class="gameInfo" id="gameInfo" style="width: 468px; height: 170px; margin-bottom: 0px;">
                        <h1 style="width: 463px;float: left; font-size: 27px !important;">
                            <a style="font-size: 24px !important;"
                               href="<c:url value="/scenario/${game.game.scenarioIdToString}/game/${game.game.gameId}/info"/>">Game ${game.game.gameId}</a>
                            / ${dates[game.game.gameId]}
            <span style="float: right;font-size: 26px !important;">vp: ${vp}&nbsp;(
                                                        <c:choose>
                                                            <c:when test="${game.game.type == 0}">
                                                                <fmt:formatNumber
                                                                        type="number"
                                                                        maxFractionDigits="2"
                                                                        groupingUsed="true"
                                                                        value="${100 * vp / nation.vpWin}"/>%
                                                            </c:when>
                                                            <c:when test="${game.game.type == -1}">
                                                                <fmt:formatNumber
                                                                        type="number"
                                                                        maxFractionDigits="2"
                                                                        groupingUsed="true"
                                                                        value="${100 * vp / (0.7 * nation.vpWin)}"/>%
                                                            </c:when>
                                                            <c:when test="${game.game.type == 1}">
                                                                <fmt:formatNumber
                                                                        type="number"
                                                                        maxFractionDigits="2"
                                                                        groupingUsed="true"
                                                                        value="${100 * vp / (1.3 * nation.vpWin)}"/>%
                                                            </c:when>
                                                        </c:choose>)</span>
                        </h1>
                        <a href='<c:url value="/scenario/${game.game.scenarioIdToString}/nation/${nation.id}"/>'>
                            <img style="float:left;clear: both; margin-top: 5px ; margin-left: 5px;"
                                 src='http://static.eaw1805.com/images/nations/nation-${nation.id}-120.png'
                                 alt="Nation Info Page"
                                 class="toolTip"
                                 title="${nation.name} Info Page"
                                 border=0>
                        </a>

                        <div style="float: left; margin-left: 30px; margin-top: -5px;">
                            <b>Scenario ${game.game.scenarioIdToString}</b>
                        </div>

                        <% int heightAdjust = 47; %>
                        <div style="float: left; margin-left: -40px;margin-top: 0px;width: 130px; height: 125px;">
                            <c:choose>
                                <c:when test="${game.alive == false}">
                                    <div style="width:140px; margin-left: -46px; margin-top: 10px; font-size: 18px;">
                                        <span style="color: red;font-size: 22px !important;">Dead Empire</span>
                                    </div>
                                    <img style="float:left; margin-top: -48px ; margin-left: -48px;"
                                         src='http://static.eaw1805.com/images/site/skull-large.png'
                                         alt="Dead Empire"
                                         class="toolTip"
                                         title="Dead Empire"
                                         width="100"
                                         border=0>

                                    <div style="width:130px; margin-left: -35px; text-align: left; margin-top: <%=heightAdjust%>px;">
                                        <a href='<c:url value="/report/scenario/${game.game.scenarioIdToString}/game/${game.game.gameId}/nation/${nation.id}/month/${userGamesDeadTurns[game.game.gameId]}"/>'
                                           title="Read Newsletter"
                                           style="line-height: 1.3!important;"><span
                                                style="font-size: 14px;">Newsletter</span></a>
                                    </div>
                                    <div style="width:130px; margin-left: -35px; text-align: left; margin-top: 1px;">
                                        <a href='<c:url value="report/scenario/${game.game.scenarioIdToString}/game/${game.game.gameId}/nation/${nation.id}/overview"/>'
                                           title="Examine Reports"
                                           style="line-height: 1.3!important;"><span
                                                style="font-size: 14px;">Reports</span></a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div style="width:140px; margin-left: -46px; margin-top: 15px; font-size: 16px;">
                                        <% if (game.getGame().getWinners() != null && game.getGame().getWinners().contains("*" + game.getNation().getId() + "*")) { %>
                                        World Domination
                                        <% } else if (game.getGame().getCoWinners() != null && game.getGame().getCoWinners().contains("*" + game.getNation().getId() + "*")) { %>
                                        Co-Winner for World Domination
                                        <% heightAdjust = 27;
                                        } else if (game.getGame().getRunnerUp() != null && game.getGame().getRunnerUp().contains("*" + game.getNation().getId() + "*")) { %>
                                        Runner-Up for World Domination
                                        <% heightAdjust = 27;
                                        } else { %>
                                        Still Standing Nation
                                        <% heightAdjust = 27;
                                        } %>
                                    </div>
                                    <div style="width:130px; margin-left: -35px; text-align: left; margin-top: <%=heightAdjust%>px;">
                                        <a href='<c:url value="/report/scenario/${game.game.scenarioIdToString}/game/${game.game.gameId}/nation/${nation.id}/newsletter"/>'
                                           title="Read Newsletter"
                                           style="line-height: 1.3!important;"><span
                                                style="font-size: 14px;">Newsletter</span></a>
                                    </div>
                                    <div style="width:130px; margin-left: -35px; text-align: left; margin-top: 1px;">
                                        <a href='<c:url value="report/scenario/${game.game.scenarioIdToString}/game/${game.game.gameId}/nation/${nation.id}/overview"/>'
                                           title="Examine Reports"
                                           style="line-height: 1.3!important;"><span
                                                style="font-size: 14px;">Reports</span></a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div style="clear:both; width:140px;float: left; margin-left: 5px;margin-top: -40px;">
                            <div style="width:140px;float: right; ">
                                <a href='http://forum.eaw1805.com/viewforum.php?f=${game.game.forumId}'
                                   title="Game Forum"
                                   style="float:left;line-height: 1.3!important;"><span
                                        style="font-size: 14px;">Game Forum</span></a>
                            </div>
                            <div style="width:140px;float: right; ">
                                <a href='<c:url value="/play/scenario/${game.game.scenarioIdToString}/game/${game.game.gameId}/nation/${nation.id}"/>'
                                   title="View Nation State"
                                   target="_blank"
                                   style="float:left;line-height: 1.3!important;"><span
                                        style="font-size: 14px;">View Nation State</span></a>
                            </div>
                        </div>

                        <div style="float: left;
                        <c:choose>
                        <c:when test="${game.alive == false}">margin-top: -2px;</c:when>
                        <c:otherwise>margin-top: -2px;</c:otherwise>
                        </c:choose>
                                margin-left: 5px;">
                    <span style="float:left;" class="inlinebar">
                        <c:forEach items="${globalActivityStat[game.game.gameId]}" var="turn">${turn},
                        </c:forEach>
                    </span>
			        <span style="clear:both;float:left;margin-top: -14px;" class="inlinebar-player">
                        <c:forEach items="${activityStat[game.game.gameId][nation.id]}"
                                   var="turn">${turn},
                        </c:forEach>
                    </span>
                        </div>

                        <c:set var="thisGameStats" value="${userGameStats[game.game.gameId]}"/>
                        <c:set var="index" value="${0}"/>
                        <div style="float: right;margin-right: 5px; width: 170px;margin-top: -125px;">
                            <h3 style="float: right;">World position</h3>

                            <c:forEach items="${thisGameStats}" var="stats">
                                <c:if test="${index < 6}">
                                    <div style="clear:both;float: left;margin-left: 5px;width: 170px;">
                                        <div style="float: left;width: 135px;text-align: right;margin-right: 10px;">${gameStatsMessages[stats.key]} </div>
                                        <div style="float: left;width: 25px;">${stats.value}</div>
                                    </div>
                                </c:if>
                                <c:set var="index" value="${index+1}"/>
                            </c:forEach>
                        </div>
                    </section>
                </c:if>
            </c:forEach>
        </article>
    </div>

    <div id="finishedGames-footer" style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
    </div>

    <script type="text/javascript">
        $(document).ready(function () {
            setTimeout(function () {
                $('#finishedGames-body').css({'height': $('#finishedGames-footer').position().top - $('#finishedGames-body').position().top + 12})
            }, 1000);
            $('#finishedGames-body').css({'height': $('#finishedGames-footer').position().top - $('#finishedGames-body').position().top + 12});
        });
    </script>
</c:if>


<div style="width: 338px;
    background: url('http://static.eaw1805.com/images/site/Empire_parchment.png') repeat-y scroll 0px 0px transparent;
    background-size: 338px 376px;
    float: left;
    min-height: 370px;
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
                        <c:when test="${key == 'achievements' || key == 'empires.played' || key == 'forum.posts'}">
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
     position: relative;">
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

<div style="width: 338px;
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
        position: relative;">
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
<c:forEach items="${pendingGames}" var="userGame">
    <div id="invitation_${userGame.id}" style="display:none; cursor: default;position:relative;">

        <h3 id="invitationTitle" style="margin-top: 16px;width: 360px;margin-left: 20px;float: left;">Invitation for
            Game ${userGame.game.gameId} by ${pendingUsers[userGame.game.userId].username}
            <a href="<c:url value="/user/${pendingUsers[userGame.game.userId].username}"/>"
               title="${pendingUsers[userGame.game.userId].username}">
                <img style="position: relative; left: 5px; top: 7px; border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                     src="https://secure.gravatar.com/avatar/${pendingUsers[userGame.game.userId].emailEncoded}?s=31&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                     alt="" height="31" width="31">
            </a>
        </h3>


        <img style="float: left; margin-right: 0px; margin-top: 9px; margin-left: 21px;" id="invitationNationImgFile"
             src='http://static.eaw1805.com/images/nations/nation-${userGame.nation.id}-list.jpg'
             alt="Nation's Flag"
             class="toolTip"
             title="${userGame.nation.name}"
             border=0 height=33>

        <div>
            <h3 style="margin-top:20px;margin-left: 70px; float: left; width: 150px;">Position costs ${userGame.cost}
                <div id="invitationCost" style="float:right;width: 20px;margin-right: 5px;"/>
            </h3>
            <img style="margin-top:25px;float:left;" src='http://static.eaw1805.com/images/goods/good-1.png'
                 alt="Credits"
                 class="toolTip"
                 title="Credits"
                 border=0 height=20>

            <h3 style="margin-top:20px;margin-left: 7px; float: left; width: 80px;">per turn</h3>
        </div>
        <h1 style="position: absolute; top: 92px; left: 22px;"><a
                href="<c:url value="/scenario/${userGame.game.scenarioIdToString}/game/${userGame.game.gameId}/info"/>"
                target="_blank"> View game details</a></h1>

            <%--this form will be submitted only via ajax--%>

        <button class="ok" id="invitationOk" onfocus=" this.blur();"
                onclick="answerInvitation(true, ${userGame.game.scenarioIdToString}, ${userGame.game.gameId}, ${userGame.nation.id});"
                style="position: absolute; left: 167px; top: 154px;"
                title="Pick up position"></button>
        <button class="cancel" title="Reject Invitation" id="invitationCancel" value="Cancel"
                style="position: absolute; top: 154px; left: 223px;"
                onclick="answerInvitation(false, ${userGame.game.scenarioIdToString}, ${userGame.game.gameId}, ${userGame.nation.id});"
                aria-describedby=""></button>

        <div>
            <img style="position: absolute; left: 12px; top: 156px;"
                 src='http://static.eaw1805.com/images/buttons/taxation/MUINormalTaxSlc.png'
                 alt="Credits"
                 class="toolTip"
                 title="Account Balance"
                 border=0 height=32>

            <div style="font-size: 20px; position: absolute; left: 50px; top: 162px;">${user.creditFree+user.creditTransferred+user.creditBought}</div>
        </div>
        <h1 style="position: absolute; left: 298px; top: 166px;"><a href="javascript:skipInvitation()">decide later</a>
        </h1>

    </div>
</c:forEach>


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

<script type="text/javascript">
    var invitationRequests = new Object();
    <c:forEach items="${pendingGames}" var="userGame" varStatus="status">

    invitationRequests[${status.index}] = ${userGame.id};
    </c:forEach>
    var currentInvitation = 0;
    setTimeout(function () {
        $('#main-article').css({'min-height': $('#main-article-bottom-2').position().top - $('#main-article-top').position().top - 68});

        if (getObjectSize(invitationRequests) > 0) {
            openInvitationForm(invitationRequests[currentInvitation]);
        }

    }, 1000);
</script>
