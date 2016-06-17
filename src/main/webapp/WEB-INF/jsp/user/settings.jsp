<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<jsp:useBean id="user" scope="request" type="com.eaw1805.data.model.User"/>
<jsp:useBean id="followingListAll" scope="request" type="java.util.TreeSet<com.eaw1805.data.model.User>"/>
<jsp:useBean id="gameList" scope="request" type="java.util.List<com.eaw1805.data.model.WatchGame>"/>
<jsp:useBean id="history" scope="request" type="java.util.List<com.eaw1805.data.model.PaymentHistory>"/>
<jsp:useBean id="allUsers" scope="request" type="java.util.List<com.eaw1805.data.model.User>"/>
<%@ page session="false" %>
<script type="text/javascript" src='http://connect.facebook.net/en_US/all.js'></script>
<script src="http://js.nicedit.com/nicEdit-latest.js" type="text/javascript"></script>

<div id="fb-root"></div>
<script>
    window.fbAsyncInit = function () {
        // init the FB JS SDK
        FB.init({
            appId: '105656276218992', // App ID from the App Dashboard
            /*channelUrl : '//WWW.YOUR_DOMAIN.COM/channel.html', // Channel File for x-domain communication*/
            status: true, // check the login status upon init?
            cookie: true, // set sessions cookies to allow your server to access the session?
            xfbml: true  // parse XFBML tags on this page?
        });

        // Additional initialization code such as adding Event Listeners goes here
        FB.Event.subscribe('edge.create',
                function (response) {
                    if (response != null) {
                        window.location = '<c:url value="/social/fb_like?redirect=/settings"/>';
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

        return window.twttr || (t = { _e: [], ready: function (f) {
            t._e.push(f)
        } });

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
                window.location = '<c:url value="/social/fb_share?redirect=/settings"/>';
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
            window.location = '<c:url value="/social/tw_tweet?redirect=/settings"/>';
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
                window.location = '<c:url value="/social/tw_follow?redirect=/settings"/>'
            }, 3000);
        }

    }

    // Wait for the asynchronous resources to load
    twttr.ready(function (twttr) {
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
        $.blockUI({ message: $('#question'), css: { width: '390px' } });
    }
</script>
<style type="text/css">
    .blockUI.blockMsg.blockPage {
        width: auto !important;
        height: auto !important;
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

    article section.connections img {
        float: right;
    }
</style>
<script type="text/javascript">
    $.blockUI.defaults.applyPlatformOpacityRules = false;
    var costPerCredit = 1;
    var isConfirmDialogOpen = false;
    function openCreditsForm() {
        $.blockUI({ message: $('#creditsDialog'), css: { width: '800px', height: '700px', margin: '-43px', top: '10%', left: '26%',
            background: 'none'
        }});
    }

    function openConfirmForm() {
        $.blockUI({ message: $('#confirmDialog'), css: { width: '390px' } });
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

    $(document).ready(function () {
        $('#newPassword').val("");
        $('#no').click(function () {
            $.unblockUI();
            return false;
        });
    });

</script>

<script type="text/javascript">
    //init autocommplete functionality.
    $(function () {
        var usersAll = new Array();
        <c:forEach items="${allUsers}" var="curUser" varStatus="status">
        <c:if test="${curUser.username!=thisUsername}">
        usersAll[${status.index}] = '${curUser.username}';
        </c:if>
        </c:forEach>
        a2 = $('#receiverUserName').autocomplete({
            delimiter: /(,|;)\s*/,
            lookup: usersAll
        });
    });
</script>

<script type="text/javascript">
    //init autocommplete functionality.
    $(function () {
        var allUsers = new Array();
        <c:forEach items="${allUsers}" var="curUser" varStatus="status">
        <c:if test="${curUser.username!=thisUsername}">
        allUsers[${status.index}] = '${curUser.username}';
        </c:if>
        </c:forEach>
        a2 = $('#searchUser').autocomplete({

            delimiter: /(,|;)\s*/,
            lookup: allUsers
        });
    });

    function followUser() {
        window.location = '<c:url value="/user/"/>' + document.getElementById("searchUser").value + '/toggleFollow/followFromSettings';
    }

    function visitProfile() {
        window.location = '<c:url value="/user/"/>' + document.getElementById("searchUser").value;
    }
</script>

<article>

<c:if test="${profileUser.userId == user.userId}">
<script type="text/javascript">
<c:set var="connectionsHeight" value="230"/>
<c:if test="${user.facebookStatus != 3 && user.twitterStatus != 3}">
    <c:set var="connectionsHeight" value="331"/>
</c:if>
</script>
<c:if test="${user.facebookStatus != 3 || user.twitterStatus != 3}">
<section class="connections" style="width: 950px;height: ${connectionsHeight}px">
<h1 style="width: 700px;margin: 7px!important;font-size: 45px!important;margin-bottom: 20px!important;">6 Simple Steps
    to earn&nbsp;<img src="http://static.eaw1805.com/images/goods/good-1.png"
                      height=30
                      style="float:none;vertical-align: bottom !important; padding-top: 6px;"
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
    <div style="float: right;width: 435px;text-align: justify;margin-right: 10px;">
        <b>Important:</b> You have to do this through the EaW1805 website so that your account can claim the free
        credits! If -
        for example - you have already "liked" us with your facebook profile, then 'unlike' us, and repeat the process
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
                                            <input type="hidden" name="redirect" value="<c:url value="/s3t" />"/>
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
            &nbsp;&nbsp;&nbsp;&nbsp;<b>+10</b><br/><img src="http://static.eaw1805.com/images/goods/good-1.png"
                                                        height=30
                                                        style=" vertical-align: bottom !important; "
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
            <div style="overflow: hidden;" class="fb-like" data-href="http://facebook.com/empires1805" data-send="false"
                 data-width="230" data-show-faces="true"></div>
        </div>
        <div style="float: left;margin-top: 4px;font-size: 23px;height: 100px;width: 71px;text-align: center;">
            &nbsp;&nbsp;&nbsp;&nbsp;<b>+40</b><br/><img src="http://static.eaw1805.com/images/goods/good-1.png"
                                                        height=30
                                                        style="vertical-align: bottom !important; "
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
            &nbsp;&nbsp;&nbsp;&nbsp;<b>+60</b><br/><img src="http://static.eaw1805.com/images/goods/good-1.png"
                                                        height=30
                                                        style=" vertical-align: bottom !important; "
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
                                                       value="<c:url value="/s3t" />"/>
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
            &nbsp;&nbsp;&nbsp;&nbsp;<b>+10</b><br/><img src="http://static.eaw1805.com/images/goods/good-1.png"
                                                        height=30
                                                        style="vertical-align: bottom !important; "
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
            &nbsp;&nbsp;&nbsp;&nbsp;<b>+20</b><br/><img src="http://static.eaw1805.com/images/goods/good-1.png"
                                                        height=30
                                                        style="vertical-align: bottom !important; "
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
                                                           data-text="Brand new HTML5 wargame! Rule an Empire. Test your Strategy skills. Can you prevail in the Napoleonic era?"
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
            &nbsp;&nbsp;&nbsp;&nbsp;<b>+40</b><br/><img src="http://static.eaw1805.com/images/goods/good-1.png"
                                                        height=30
                                                        style="vertical-align: bottom !important; "
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
</section>
</c:if>

<section class="settings-top-left">
<h1>General Settings</h1>
<c:set var="enableNotifications" value="${user.enableNotifications}"/>
<c:set var="publicMail" value="${user.publicMail}"/>
<c:set var="thisUsername" value="${user.username}"/>

<form:form name="generalSettings" commandName="user" method="POST" action='settings'>

    <label for="fullname">Full Name:</label>

    <div>
        <form:input class="customInput" path="fullname" id="fullname"/>
        <spring:bind path="fullname">
            <c:if test="${status.error}">
                <img style="float: left;margin-bottom: -5px;" src="http://static.eaw1805.com/images/site/error.jpeg"
                     width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
            </c:if>
        </spring:bind>
    </div>

    <label for="newPassword">Password:</label>

    <div>
        <input class="customInput" type="password" name="newPassword" id="newPassword"/>
        <spring:bind path="password">
            <c:if test="${status.error}">
                <img style="float: left;margin-bottom: -5px;" src="http://static.eaw1805.com/images/site/error.jpeg"
                     width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
            </c:if>
        </spring:bind>
    </div>

    <label for="email">Email:</label>

    <div>
        <form:input class="customInput" path="email" id="email"/>
        <spring:bind path="email">
            <c:if test="${status.error}">
                <img style="float: left;margin-bottom: -5px;" src="http://static.eaw1805.com/images/site/error.jpeg"
                     width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
            </c:if>
        </spring:bind>
    </div>

    <label for="location">Location:</label>

    <div style="float: right;margin-right: 5px;">
        <jsp:include page="tiles/locationSelect.jsp"/>
    </div>

    <spring:bind path="location">
        <c:if test="${status.error}">
            <img style="float: left;margin-bottom: -15px;"
                 src="http://static.eaw1805.com/images/site/error.jpeg"
                 width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
        </c:if>
    </spring:bind>


    <label for="timezone">Time Zone:</label>

    <div style="float: right;margin-right: 5px;">
        <jsp:include page="tiles/timezone.jsp"/>
    </div>

    <label for="notification">Notification/Newsletter:</label>

    <div>
        <input class="settingsCheckbox" type="checkbox" name="notification" id="notification"
                <c:if test="${enableNotifications == true}">
                    checked="true"
                </c:if>
               value="enable"/><br>
    </div>

    <label id="public_email">Public e-mail:</label>

    <div>
        <input class="settingsCheckbox" type="checkbox" name="publicemail" id="publicemail"
                <c:if test="${publicMail == true}">
                    checked="true"
                </c:if>
               value="enable"/><br>
    </div>

    <label id="inboxSize">Inbox threads size:</label>

    <div>
        <select class="settingsCheckbox" name="drop1" id="drop1" style="margin-top: 3px;">
            <option value="-1" <c:if test="${user.inboxThreadSize == -1}"> selected="selected" </c:if>>ALL</option>
            <option value="6"  <c:if test="${user.inboxThreadSize == 6}"> selected="selected" </c:if>>6</option>
            <option value="10" <c:if test="${user.inboxThreadSize == 10}"> selected="selected" </c:if>>10</option>
            <option value="14" <c:if test="${user.inboxThreadSize == 14}"> selected="selected" </c:if>>14</option>
            <option value="18" <c:if test="${user.inboxThreadSize == 18}"> selected="selected" </c:if>>18</option>
            <option value="22" <c:if test="${user.inboxThreadSize == 22}"> selected="selected" </c:if>>22</option>
        </select>
    </div>

    <input type="hidden" name="oldPasswd" id="oldPasswd" hidden="true"/>

    <label class='save-setting-btn'
           style="margin-left:329px; margin-top:0px;width: 36px; height: 36px; float: right;" title="Save Changes">
        <input type="submit" value=""/>
    </label>

</form:form>
<script>
$("form:first").submit(function () {
if ($('input[name=newPassword]').val() == "") {
return true;
}
if (!isConfirmDialogOpen) {
isConfirmDialogOpen = true;
openConfirmForm();
} else {
return true;
}
return false;
});
</script>
</section>

<section class="avatar">
<h1>Avatar</h1>

<div>
<div style="height:100%; margin-left: 5px;">
Change your avatar at<BR><a href="http://gravatar.com" target="_blank">gravatar.com</a><br>
</div>
<div style="float:right; margin-top: -45px;">
<img src="https://secure.gravatar.com/avatar/${user.emailEncoded}?s=48&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
alt="" height="48" width="48">
</div>

</div>
</section>


<section class="connections">
<h1>Your Connections</h1>

<div class="connections">

<c:forEach var="providerId" items="${providerIds}">
    <c:set var="connections" value="${connectionMap[providerId]}"/>
    <spring:message code="${providerId}.displayName" var="providerDisplayName"/>

    <c:if test="${not empty connections}">
        <div class="connection-item">
                    <span class="connection-icon">
                       <img src="http://static.eaw1805.com/images/site/${providerDisplayName}.png"
                            alt="${providerDisplayName}"></span>
            <span class="connection-provider">${providerDisplayName}</span>
            <span class="connection-user">(${connections[0].displayName})</span>
                    <span class="connection-control">
                        <form action="<c:url value="/connect/${providerId}" />" method="post">
                            <input type="hidden" name="redirect" value="<c:url value="/settings" />"/>
                            <button class="disconButton" type="submit" title="Disconnect"></button>
                            <input type="hidden" name="_method" value="delete"/>
                        </form>
                    </span>
        </div>
    </c:if>
    <c:if test="${empty connections}">
        <div class="connection-item">
                    <span class="connection-icon">
                      <img class="connect" src="http://static.eaw1805.com/images/site/${providerDisplayName}.png"
                           alt="${providerDisplayName}"></span>
            <span class="connection-provider">${providerDisplayName}</span>
                    <span class="connection-control">
                        <form action="<c:url value="/connect/${providerId}" />" method="POST">
                            <input type="hidden" name="redirect" value="<c:url value="/settings" />"/>
                            <c:if test="${providerId == 'facebook'}">
                                <input type="hidden" name="scope" value="publish_stream,offline_access"/>
                            </c:if>
                            <button class="conButton" type="submit" value="connect">Connect</button>
                        </form>
                    </span>
        </div>
    </c:if>

</c:forEach>
</div>
</section>

<section class="balance" id="balance-section">
<h1>Balance
<div style="font-size: small; float: right; margin-right: 4.5em; margin-top: 3px;">(in credits)</div>
</h1>

<div style="height:70px; width:170px;margin-top: 8px;">
<label for="free_credits">Free:</label>
<span id="free_credits" style="text-align: right">${user.creditFree}</span>

<label for="trans_credits">Transferred:</label>
<span id="trans_credits" style="text-align: right">${user.creditTransferred}</span>

<label for="bought_credits">Bought:</label>
<span id="bought_credits" style="text-align: right">${user.creditBought}</span>

</div>
<a href="javascript:openCreditsForm()" style="float: right;width: 109px;margin-top: -10px;margin-right: 5px;">Acquire Credits
<img src="http://static.eaw1805.com/images/site/paypal/paypal.gif"
style="float: right;margin: 0;border: 0;padding: 0;">
</a>
</section>

<section class="transfer">

<h1>Transfer Credits</h1>

<form:form commandName="transferCommand" method="POST" action="transfer">
    <label class="transfer-label" for="receiverUserName">To</label>
    <input class="customInput" type="text" name="receiverUserName" id="receiverUserName"/>
    <spring:bind path="receiver">
        <c:if test="${status.error}">
            <img style="float:right;" src="http://static.eaw1805.com/images/site/error.jpeg"
                 width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
        </c:if>
    </spring:bind>

    <label class="transfer-label" for="creditsAmount">Credits to transfer</label>
    <form:input class="customInput" path="creditsAmount" id="creditsAmount" value="0"/>
    <spring:bind path="creditsAmount">
        <c:if test="${status.error}">
            <img style="float:right;" src="http://static.eaw1805.com/images/site/error.jpeg"
                 width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
        </c:if>
    </spring:bind>

    <label class="transfer-label" for="givenPassword">Enter your password</label>
    <form:input class="customInput" type="password" path="givenPassword" id="givenPassword"/>
    <spring:bind path="givenPassword">
        <c:if test="${status.error}">
            <img style="float:right;" src="http://static.eaw1805.com/images/site/error.jpeg"
                 width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
        </c:if>
    </spring:bind>

    <label class='save-setting-btn'
           style="clear:both;margin-right:5px;width: 36px; height: 36px; float: right;" title="Transfer Credits">
        <input type="submit" value="" style="clear: both!important;"/>
    </label>

</form:form>

</section>


<section class="section-post-create">
<form method="post" action="postMessage">
<h1>Personal Message</h1>
<label class='save-setting-btn'
style="clear:both;margin-right:5px;width: 36px; height: 36px; float: right; margin-top:-25px;" title="Update personal message">
<input class="tick-submit" type="submit" value="" style="clear: both!important;" onclick="updateHTMLContent();"/>
</label>
<div style="height: 100%; width: 100%; border-radius: 8px 8px 8px 8px;overflow: hidden;">
<textarea id="post_create_area" name="messageBody" style="width:100%; height: 205px;">${user.profileHtml}</textarea>
</div>
</form>

</section>
<script type="text/javascript">
new nicEditor({fullPanel : true}).panelInstance('post_create_area',{hasPanel : true});

function updateHTMLContent() {
nicEditors.findEditor("post_create_area").saveContent();
}
</script>


<section class="section-followers">
<div id="followContent" style="overflow: auto;">
<h1>Follows</h1>

<c:forEach var="leader" items="${followingListAll}">
    <div style="float:left; text-align:center;width: 60px;height: 75px;margin: 5px;">
            <%--${leader.username}--%>
        <a href='<c:url value="/user/${leader.username}"/>'>
            <img src="https://secure.gravatar.com/avatar/${leader.emailEncoded}?s=48&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                 alt="" height="48" width="48" title="${leader.username}">
        </a>

        <a href='<c:url value="/user/${leader.username}/toggleFollow/unfollowFromSettings"/>'
           class="minibutton" title="Unfollow">Unfollow</a>
    </div>
</c:forEach>

<c:if test="${fn:length(followingListAll) > 10 }">
    <script>
        $('.section-followers').css({ 'min-height': (<c:out value="${73 * ((fn:length(followingListAll)/4)+2)}" />)});
        $('.section-followers').css({ 'height': (<c:out value="${73 * ((fn:length(followingListAll)/4)+2)}" />)});
    </script>
</c:if>
</br>
</div>
<div id="searchdiv" style=" margin-left: 5px;float:right; margin-right: 5px;clear: both;">
Search:
<input style="width:190px;" type="text" name="searchUser" id="searchUser"/>
<input class="buttons" type="button" value="Follow" onclick="followUser()"/>
<input class="buttons" type="button" value="Profile" onclick="visitProfile()"/>
</div>
<script>
$('#searchdiv').css({ 'margin-top':$('.section-followers').height() - $('#followContent').height() - 25 + 'px'});
</script>
</section>

<section class="section-watching">
<h1>Watching Games</h1>
<c:forEach items="${gameList}" var="thisGame">
    <div style="margin-left:5px;">
        <a href="<c:url value="/scenario/${thisGame.scenarioIdToString}/game/${thisGame.gameId}/info"/>">Game
            #${thisGame.gameId}</a>

        <form style="margin-top:5px;float: right;"
              action="<c:url value="/scenario/${game.scenarioIdToString}/game/${thisGame.gameId}/toggleWatch/unwatch" />"
              method="get">
            <button class="unWatch" type="submit" title="Unwatch Game ${thisGame.gameId}"></button>
        </form>

        <BR>

        <div style="margin-left:15px;">${dates[thisGame.gameId]}
            (<i>last process: ${thisGame.dateLastProc}</i>)
        </div>
    </div>
</c:forEach>
<c:if test="${fn:length(gameList) > 4 }">
    <script>
        $('.section-watching').css({ 'min-height': (<c:out value="${(60 * fn:length(gameList))+30}" />)});
        $('.section-watching').css({ 'height': (<c:out value="${(60 * fn:length(gameList)) +30}" />)});
    </script>
</c:if>
</section>

<section class="section-history">
<div style="height:100%;">
<h1>Account History</h1>

<c:set var="length" value="${fn:length(history)-1}"/>
<c:if test="${fn:length(history) > 5}">
    <c:set var="length" value="5"/>
</c:if>

<c:forEach begin="0" end="${length}" var="historyCounter">
    <div id="history-div" style="width:100%;text-align: justify; white-space: normal;margin-top: 5px;">
            <span style="float:left;margin-left:5px;margin-right:15px;"><fmt:formatDate
                    value="${history[historyCounter].date}"
                    pattern="yyyy-MM-dd hh:mm:ss"/></span>
            <span style="float:right;margin-left:5px;margin-right:15px; font-weight:bold;">
            <c:if test="${history[historyCounter].chargeBought != 0}">
                ${history[historyCounter].chargeBought}
            </c:if>
                <c:if test="${history[historyCounter].chargeFree != 0}">
                    ${history[historyCounter].chargeFree}
                </c:if>
                <c:if test="${history[historyCounter].chargeTransferred != 0}">
                    ${history[historyCounter].chargeTransferred}
                </c:if>
            </span><br>
        <span style="float:left;margin-left:5px;margin-right:15px; font-size: 80%;">Reason: ${history[historyCounter].comment}</span><br>
    </div>
</c:forEach>
</div>
<c:if test="${fn:length(history) > 5}">
    <div style="margin-right: 16px; float: right;margin-top: -20px;"><a
            href='<c:url value="/user/paymentHistory"/>'>View History</a></div>
</c:if>
</section>


<c:if test="${user.userType == 3 }">

    <section class="section-followers" style="height: 240px;">
        <h1>Admin Links </h1>

        <ul>
            <li style="list-style-type: none;">
                <a href="<c:url value="/admin/statistics"/>"
                   title="Views Site Statistics">View Site Statistics
                </a>
            </li>
            <li style="list-style-type: none;">
                <a href="<c:url value="/admin/questionnaire/statistics"/>"
                   title="Views Site Statistics">View Questionnaire Statistics
                </a>
            </li>
            <li style="list-style-type: none;">
                <a href="<c:url value="/users/list"/>"
                   title="Views News Without Cache">View All Users
                </a>
            </li>
            <li style="list-style-type: none;">
                <a href="<c:url value="/newsNoCache"/>"
                   title="Views News Without Cache">View uncached News
                </a>
            </li>
            <li style="list-style-type: none;">
                <a href="<c:url value="/cache/evict/constantCache"/>"
                   title="Clear Constant Cache">Clear Constant Cache (constantCache)
                </a>
            </li>
            <li style="list-style-type: none;">
                <a href="<c:url value="/cache/evict/newsCache"/>"
                   title="Clear News Cache">Clear News Cache (newsCache)
                </a>
            </li>
            <li style="list-style-type: none;">
                <a href="<c:url value="/cache/evict/userCache"/>"
                   title="Clear User Cache">Clear User Cache (userCache)
                </a>
            </li>
            <li style="list-style-type: none;">
                <a href="<c:url value="/cache/evict/gameCache"/>"
                   title="Clear Game Cache">Clear Game Cache (gameCache)
                </a>
            </li>
            <li style="list-style-type: none;">
                <a href="<c:url value="/cache/evict/longGameCache"/>"
                   title="Clear Long Cache">Clear Long Cache (longGameCache)
                </a>
            </li>
            <li style="list-style-type: none;">
                <a href="<c:url value="/cache/evict/adminCache"/>"
                   title="Clear Default Cache">Clear Admin Cache (adminCache)
                </a>
            </li>
        </ul>
    </section>

    <section class="settings-top-left" style="height: 120px;">
        <h1>Battle Engines</h1>
        <ul>
            <li style="list-style-type: none;">
                <a href="<c:url value="/tactical/battle"/>"
                   title="Tactical Battle Engine">Tactical Battle Engine
                </a>
            </li>

            <li style="list-style-type: none;">
                <a href="<c:url value="/naval/battle"/>"
                   title="Naval Battle Engine">Naval Battle Engine
                </a>
            </li>

            <li style="list-style-type: none;">
                <a href="<c:url value="/fieldbattle/list"/>"
                   title="List Fieldbattle Games">View/Play Existing Field Battles
                </a>
            </li>

            <li style="list-style-type: none;">
                <a href="<c:url value="/fieldbattle/create"/>"
                   title="Create Fieldbattle Game">Create New Field Battle
                </a>
            </li>


        </ul>
    </section>

    <section class="settings-top-left" style="height: 50px;">
        <h1>Scenario Editor</h1>
        <ul>
            <li style="list-style-type: none;">
                <a href="<c:url value="/scenario/editor/list"/>"
                   title="Scenario editor">scenario editor
                </a>
            </li>
        </ul>
    </section>

</c:if>
</article>

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

<p class="manual" style="font-size: 12pt; margin-left: 15pt; margin-top: -5px; width: 750px;">Empires at War works with a credits' system: You need
<img src="http://static.eaw1805.com/images/goods/good-1.png" height="18"
style="float:none;vertical-align: middle !important;"
alt="Empires at War 1805 Credits" title="EaW Credits">
in order to pick up and run Empires, and you can purchase them here.
The &#8364;-to-<img src="http://static.eaw1805.com/images/goods/good-1.png" height="18"
style="float:none;vertical-align: middle !important;"
alt="Empires at War 1805 Credits" title="EaW Credits">
ratio is 1 to 10,
so for example 20 &#8364; will buy you 200 <img src="http://static.eaw1805.com/images/goods/good-1.png" height="18"
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
    <input type="submit" class="paypal-20-input" value="" onfocus=" this.blur();" title="Buy 200 credits for 20 &#8364;"
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
<p class="manual" style="font-size: 12pt; margin-top: -5px; margin-left: 15pt; width: 750px; float: left; clear:both;">
Payments are being processed through PayPal.
Oplon Games has no access to your credit card in any way. Once you choose your preferred credit amount,
you will be redirected to a secure location outside our website were you will be able to
access your PayPal account.</p>
<button class="cancel" style="float: right; margin-right: 25px; margin-top: -25px;" value="No" onclick="unblock();"/>
</div>


<div id="confirmDialog" style="display:none; cursor: default;">
<h3 id="joinTitle" style="margin-top: 20px;margin-left: 20px;">Change Password</h3>


<div style="width: 360px; margin-left: 25px; margin-top: 10px;">
<label id="oldPassword" style="width: 11em;">Old Password:</label>
<label style="margin-left: 5px; width: 16em;">
<input style="float: left;width: 12em;" type="password" name="oldPass" id="oldPass"/>
<img id="oldPassImg" style="display: none;margin-top: 5px;"
src="http://static.eaw1805.com/images/site/error.jpeg"
width="15" height="15" class="error_tooltip" title=""/>
<br>
</label>
</div>


<div style="width: 360px; margin-left: 25px;">
<label id="confNewPassword" style="width: 11em;">Confirm New Password:</label>
<label style="margin-left: 5px; width: 16em;">
<input style="float: left;width: 12em;" type="password" name="confNewPass" id="confNewPass"/>
<img id="newPassImg" style="display: none;margin-top: 5px;"
src="http://static.eaw1805.com/images/site/error.jpeg"
width="15" height="15" class="error_tooltip" title=""/>
<br>
</label>

</div>

<div style="clear:both; margin-top: 70px">
Are you sure you want to change your password?
</div>

<%--<button type="submit">Buy</button>--%>
<div style=" margin-top: 10px">
<button class="ok" style="margin: 5px;" value="Yes" onclick="validateDialog();"/>
<button class="cancel" style="margin: 5px;" value="No" onclick="unblock();"/>
</div>
<%--</form>--%>
<script>
function unblock() {

$('#newPassImg').hide();
$('#oldPassImg').hide();

$('#newPassImg').attr('title', '');
$('#oldPassImg').attr('title', '');

$('#confNewPass').val("");
$('#oldPass').val("");

$.unblockUI();
isConfirmDialogOpen = false;
}

function validateDialog() {

$('#newPassImg').hide();
$('#oldPassImg').hide();

if ($('#confNewPass').val() == "") {
$('#newPassImg').show();
$('#newPassImg').attr('title', 'Confirm Password cannot be empty');
return false;
}

if ($('#oldPass').val() == "") {
$('#oldPassImg').show();
$('#oldPassImg').attr('title', 'Old Password cannot be empty');
return false;
}

if ($('#confNewPass').val() != $('#newPassword').val()) {
$('#newPassImg').show();
$('#newPassImg').attr('title', 'Confirm Password is different from New Password');
return false;
}

$('input[name=oldPasswd]').val($('#oldPass').val());
$.unblockUI();
$("form:first").submit();
isConfirmDialogOpen = false;
$('input[name=oldPasswd]').val("");
}

</script>
</div>
