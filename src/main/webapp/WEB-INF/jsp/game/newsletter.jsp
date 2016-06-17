<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="game" scope="request" class="com.eaw1805.data.model.Game"/>
<jsp:useBean id="gameId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="gameDate" scope="request" class="java.lang.String"/>
<jsp:useBean id="nation" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="nationId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="titleFirst" scope="request" class="java.lang.String"/>
<jsp:useBean id="titleSecond" scope="request" class="java.lang.String"/>
<jsp:useBean id="sizeFirst" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="sizeSecond" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="demographics" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="armyStatsRegion" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="armyStatsType" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="armyStatsCosts" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="navyStatsRegion" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="navyStatsType" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="navyStatsCosts" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="newsTopStory" scope="request" type="com.eaw1805.data.model.News"/>
<jsp:useBean id="newsFront" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="news" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="newsLetters" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="newsMilitary" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="addMilitary" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="newsPolitics" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="addPolitical" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="newsEconomy" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="addEconomy" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="topWorld" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="addWorld" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="maxSideEntries" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="totMilitaryNews" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="totPoliticalNews" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="totEconomyNews" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="vpList" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="vpNow" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="vpPrev" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="hasHistory" scope="request" class="java.lang.Boolean"/>
<jsp:useBean id="txtHistory" scope="request" class="java.lang.String"/>
<jsp:useBean id="hasTutorial" scope="request" class="java.lang.Boolean"/>
<jsp:useBean id="txtTutorial" scope="request" class="java.lang.String"/>
<jsp:useBean id="months" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
<jsp:useBean id="turn" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="turnMonth" scope="request" class="java.lang.String"/>
<script type="text/javascript" src='<c:url value="/js/social.js"/>'></script>


<style type="text/css">
    .shareButton {
        background: url("http://static.eaw1805.com/images/site/Facebook.png") no-repeat;
        background-size: 20px 20px;
        border: none;
        cursor: pointer;
        width: 20px;
        height: 20px;
    }
</style>

<div id="fb-root"
     style="position: absolute;
     margin-left: -434px;
     margin-top: -363px;"></div>
<script type="text/javascript">
    $(document).ready(function () {
        console.log("initi");
        window.fbAsyncInit = function () {
            // init the FB JS SDK
            FB.init({
                appId:'105656276218992', // App ID from the App Dashboard
                /*channelUrl : '//WWW.YOUR_DOMAIN.COM/channel.html', // Channel File for x-domain communication*/
                status:true, // check the login status upon init?
                cookie:false, // set sessions cookies to allow your server to access the session?
                xfbml:true  // parse XFBML tags on this page?
            });
        };
    });

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

    function postToFeed(img, caption, description, id) {

        var publish = {
            method:'feed',
            message:'Empires at War 1805',
            name:'Empires at War 1805',
            caption:caption,
            description:(
                    description
                    ),
            link:'http://www.eaw1805.com',
            picture:'http://static.eaw1805.com/newsletter-fb/' + img,
            actions:[
                { name:'Follow Us', link:'http://www.facebook.com/empires1805' }
            ],
            user_message_prompt:'Share your empire statistics'
        };

        FB.ui(publish, callback);

        function callback(response) {
        <c:if test="${game.turn == turn}">
            if (response != null) {

            <% if (request.getParameter("fixForClient") == null) { %>
                window.location = '<c:url value="/social/scenario/${scenarioId}/news/"/>' + id
                        + '?redirect=/report/scenario/${scenarioId}/game/${gameId}/nation/${nationId}/month/${turn}';
            <%}%>

            <% if (request.getParameter("fixForClient") != null) { %>
                $.ajax({
                    url:'<c:url value="/social/scenario/${scenarioId}/news/"/>' + id
                            + '?redirect=/report/scenario/${scenarioId}/game/${gameId}/nation/${nationId}/month/${turn}'
                });
            <%}%>

            }
        </c:if>
        }

    }

</script>
<article>

<c:if test="${newsTopStory.newsId > 0 && turn > 0}">
    <table width="948px" border=0 style="margin-bottom: 10px; ">
        <tr>
            <td width="506px" valign="middle">
                <div id="share1" class="shareButton"
                        <c:choose>
                            <c:when test="${game.turn == turn}">
                                title="Share on Facebook and get 1 free credit"
                            </c:when>
                            <c:otherwise>
                                title="Share on Facebook"
                            </c:otherwise>
                        </c:choose>
                     style="float: left;"></div>
                <h2 class="news"
                    style="margin-left: 25px; margin-right: 25px; margin-top: 0; font-size: 42px; width: 450px; text-align: center;">${newsTopStory.text}</h2>
                <c:if test="${newsTopStory.text.contains(' achieved world domination !')}">
                    <p class="manual" style="margin-top: 10px; margin-right: 10px; font-size: 16px;">After a series of
                        carefully planned campaigns and hard-won battles, the world finally
                        recognizes the dominance of the mightiest of Nations! With the long years of intrigue and
                        mass-scale
                        warfare now coming to an end, it was inevitable that Victory would be awarded to those with the
                        courage
                        and skill to claim her.</p>
                </c:if>
                <c:if test="${newsTopStory.text.contains('Our nation enters civil disorder !')}">
                    <p class="manual" style="margin-top: 10px; margin-right: 10px; font-size: 16px;">Despite our best
                        efforts, our once mighty Empire now stands defeated. Even as thousands of
                        enemy troops march upon the devastated lands we used to call our home, civil disorder is
                        breaking out.
                        The Royal Family is escaping to lands unknown. Our proud Nation is falling into Chaos.</p>

                    <p class="manual" style="margin-top: 10px; margin-right: 10px; font-size: 16px;">Will History
                        remember our last stand, or are we doomed to pass into oblivion...?</p>
                </c:if>
            </td>
            <td width="442px" valign="middle">
                <img src='http://static.eaw1805.com/newsletter/${newsTopStory.type}/${newsTopStory.baseNewsId}.jpg'
                <c:if test="${!newsTopStory.text.contains('following materials:')}">
                     alt="${newsTopStory.text}"
                     title="${newsTopStory.text}"
                </c:if>
                     width=430
                     style="border: 1px solid rgba(0, 0, 0, 0.5); opacity: 1 !important; padding: 1px; border-radius: 5px;">
            </td>
            <script type="text/javascript">
                $("#share1").click(function () {
                    postToFeed('${newsTopStory.type}/${newsTopStory.baseNewsId}.jpg',
                            '${nation.name} \/ Game ${gameId} - ${turnMonth}',
                            '${newsTopStory.text}', '${newsTopStory.newsId}');
                });
            </script>
        </tr>
    </table>
</c:if>

<c:choose>
    <c:when test="${fn:length(newsFront) == 1}">
        <table width="948px" border=0 style="margin-bottom: 10px; ">
            <tr>
                <td width="442px" valign="middle">
                    <img src='http://static.eaw1805.com/newsletter/${newsFront[0].type}/${newsFront[0].baseNewsId}.jpg'
                    <c:if test="${!newsFront[0].text.contains('following materials:')}">
                         alt="${newsFront[0].text}"
                         title="${newsFront[0].text}"
                    </c:if>
                         height=250
                         style="border: 1px solid rgba(0, 0, 0, 0.5); opacity: 1 !important; padding: 1px; border-radius: 5px;">
                </td>
                <td width="506px" valign="middle">
                    <div id="share2" class="shareButton" style="float: left;"
                            <c:choose>
                                <c:when test="${game.turn == turn}">
                                    title="Share on Facebook and get 1 free credit"
                                </c:when>
                                <c:otherwise>
                                    title="Share on Facebook"
                                </c:otherwise>
                            </c:choose>>
                    </div>
                    <script type="text/javascript">
                        $("#share2").click(function () {
                            postToFeed("${newsFront[0].type}/${newsFront[0].baseNewsId}.jpg",
                                    "${nation.name} \/ Game ${gameId} - ${turnMonth}",
                                    "${newsFront[0].text}", "${newsFront[0].newsId}");
                        });
                    </script>
                    <h2 class="news"
                        style="margin-left: 25px; margin-right: 25px; margin-top: 0; font-size: 42px; width: 450px; text-align: center;">${newsFront[0].text}</h2>
                </td>
            </tr>
        </table>
    </c:when>
    <c:when test="${fn:length(newsFront) == 2}">
        <table width="948px" border=0 style="margin-top: 15px;">
            <tr>
                <td width="630px" valign="middle">
                    <table width="630px" border=0>
                        <tr>
                            <td width="252px" valign="middle">
                                <img src='http://static.eaw1805.com/newsletter/${newsFront[0].type}/${newsFront[0].baseNewsId}.jpg'
                                <c:if test="${!newsFront[0].text.contains('following materials:')}">
                                     alt="${newsFront[0].text}"
                                     title="${newsFront[0].text}"
                                </c:if>
                                     width=240
                                     style="border: 1px solid rgba(0, 0, 0, 0.5); opacity: 1 !important; padding: 1px; border-radius: 5px;">
                            </td>
                            <td width="378px" valign="middle">
                                <h2 class="news"
                                    style="margin-left: 15px; margin-right: 15px; margin-top: 0; font-size: 28px; width: 366px; text-align: center;">${newsFront[0].text}</h2>
                            </td>
                        </tr>
                    </table>
                </td>
                <td width="312px" valign="middle"
                    style="border-left-style: dashed; border-left-width: 1px; border-left-color: black; padding-left: 10px;">
                    <h2 class="news"
                        style="margin-left: 5px; margin-right: 5px; margin-top: 0; font-size: 26px; width: 300px; text-align: center;">${newsFront[1].text}</h2>
                </td>
            </tr>
        </table>
        <div style="margin-top: -20px;margin-bottom: 35px;">
            <div style="width:646px;float: left;">
                <div id="share3" class="shareButton" style="float: right;"
                        <c:choose>
                            <c:when test="${game.turn == turn}">
                                title="Share on Facebook and get 1 free credit"
                            </c:when>
                            <c:otherwise>
                                title="Share on Facebook"
                            </c:otherwise>
                        </c:choose>></div>

            </div>
            <script type="text/javascript">
                $("#share3").click(function () {
                    postToFeed('${newsFront[0].type}/${newsFront[0].baseNewsId}.jpg',
                            '${nation.name} \/ Game ${gameId} - ${turnMonth}',
                            '${newsFront[0].text}', '${newsFront[0].newsId}');
                });
            </script>

            <div style="width:312px;float: left;">
                <div id="share4" class="shareButton" style="float: right;"
                        <c:choose>
                            <c:when test="${game.turn == turn}">
                                title="Share on Facebook and get 1 free credit"
                            </c:when>
                            <c:otherwise>
                                title="Share on Facebook"
                            </c:otherwise>
                        </c:choose>
                        ></div>
                <script type="text/javascript">
                    $("#share4").click(function () {
                        postToFeed('${newsFront[1].type}/${newsFront[1].baseNewsId}.jpg',
                                '${nation.name} \/ Game ${gameId} - ${turnMonth}',
                                '${newsFront[1].text}', '${newsFront[1].newsId}');
                    });
                </script>
            </div>
        </div>

    </c:when>
    <c:when test="${fn:length(newsFront) == 3}">
        <table width="948px" border=0 style="margin-top: 15px;">
            <tr>
                <td width="290px" valign="middle">
                    <h2 class="news"
                        style="margin-left: 25px; margin-right: 25px; margin-top: 0; font-size: 24px; width: 280px; text-align: center;">${newsFront[0].text}</h2>
                </td>
                <td width="290px" valign="middle" valign="middle"
                    style="border-left-style: dashed; border-left-width: 1px; border-left-color: black; padding-left: 10px;">
                    <h2 class="news"
                        style="margin-left: 5px; margin-right: 15px; margin-top: 0; font-size: 24px; width: 280px; text-align: center;">${newsFront[1].text}</h2>
                </td>
                <td width="290px" valign="middle" valign="middle"
                    style="border-left-style: dashed; border-left-width: 1px; border-left-color: black; padding-left: 10px;">
                    <h2 class="news"
                        style="margin-left: 5px; margin-right: 15px; margin-top: 0; font-size: 24px; width: 280px; text-align: center;">${newsFront[2].text}</h2>
                </td>
            </tr>
        </table>
        <div style="margin-top: -20px;margin-bottom: 35px;">
            <div style="width:330px;float: left;">
                <div id="share5" class="shareButton" style="float: right;"
                        <c:choose>
                            <c:when test="${game.turn == turn}">
                                title="Share on Facebook and get 1 free credit"
                            </c:when>
                            <c:otherwise>
                                title="Share on Facebook"
                            </c:otherwise>
                        </c:choose>
                        ></div>

            </div>
            <script type="text/javascript">
                $("#share5").click(function () {
                    postToFeed('${newsFront[0].type}/${newsFront[0].baseNewsId}.jpg',
                            '${nation.name} \/ Game ${gameId} - ${turnMonth}',
                            '${newsFront[0].text}', '${newsFront[0].newsId}');
                });
            </script>
            <div style="width:314px;float: left;">
                <div id="share6" class="shareButton" style="float: right;"
                        <c:choose>
                            <c:when test="${game.turn == turn}">
                                title="Share on Facebook and get 1 free credit"
                            </c:when>
                            <c:otherwise>
                                title="Share on Facebook"
                            </c:otherwise>
                        </c:choose>
                        ></div>
            </div>
            <script type="text/javascript">
                $("#share6").click(function () {
                    postToFeed('${newsFront[1].type}/${newsFront[1].baseNewsId}.jpg',
                            '${nation.name} \/ Game ${gameId} - ${turnMonth}',
                            '${newsFront[1].text}', '${newsFront[1].newsId}');
                });
            </script>
            <div style="width:310px;float: left;">
                <div id="share7" class="shareButton" style="float: right;"
                        <c:choose>
                            <c:when test="${game.turn == turn}">
                                title="Share on Facebook and get 1 free credit"
                            </c:when>
                            <c:otherwise>
                                title="Share on Facebook"
                            </c:otherwise>
                        </c:choose>
                        ></div>
                <script type="text/javascript">
                    $("#share7").click(function () {
                        postToFeed('${newsFront[2].type}/${newsFront[2].baseNewsId}.jpg',
                                '${nation.name} \/ Game ${gameId} - ${turnMonth}',
                                '${newsFront[2].text}', '${newsFront[2].newsId}');
                    });
                </script>
            </div>
        </div>
    </c:when>
</c:choose>

<c:if test="${turn == 0}">
    <table width="948px" border=0 style="margin-bottom: 10px; ">
        <tr>
            <td width="506px" valign="middle">
                <c:choose>
                    <c:when test="${game.scenarioId == 1}">
                        <h2 class="news"
                            style="margin-left: 25px; margin-right: 25px; margin-top: 0; font-size: 42px; width: 450px; text-align: center;">
                            Peace signed in Amiens!</h2>
                    </c:when>
                    <c:when test="${game.scenarioId == 2}">
                        <h2 class="news"
                            style="margin-left: 25px; margin-right: 25px; margin-top: 0; font-size: 42px; width: 450px; text-align: center;">
                            Napoleon Coronated Emperor of France!</h2>
                    </c:when>
                    <c:otherwise>
                        <h2 class="news"
                            style="margin-left: 25px; margin-right: 25px; margin-top: 0; font-size: 42px; width: 450px; text-align: center;">
                            An Era of Glory awaits us!</h2>
                    </c:otherwise>
                </c:choose>
            </td>
            <td width="442px" valign="middle">
                <c:choose>
                    <c:when test="${game.scenarioId == 1}">
                        <img src='http://static.eaw1805.com/newsletter/start/1802.jpg'
                             alt="Peace signed in Amiens!"
                             title="Peace signed in Amiens!"
                             width=430
                             style="border: 1px solid rgba(0, 0, 0, 0.5); opacity: 1 !important; padding: 1px; border-radius: 5px;">
                    </c:when>
                    <c:when test="${game.scenarioId == 2}">
                        <img src='http://static.eaw1805.com/newsletter/start/1805.jpg'
                             alt="Napoleon Coronated Emperor of France!"
                             title="Napoleon Coronated Emperor of France!"
                             width=430
                             style="border: 1px solid rgba(0, 0, 0, 0.5); opacity: 1 !important; padding: 1px; border-radius: 5px;">
                    </c:when>
                    <c:otherwise>
                        <img src='http://static.eaw1805.com/newsletter/start/1802.jpg'
                             alt="An Era of Glory awaits us!"
                             title="An Era of Glory awaits us!"
                             width=430
                             style="border: 1px solid rgba(0, 0, 0, 0.5); opacity: 1 !important; padding: 1px; border-radius: 5px;">
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
</c:if>

<c:set var="totNewsEntries" value="0"/>

<table width="948px" border=0>
<tr valign="top">
<td width="468px" valign="top" rowspan=2 height="1px">

<c:if test="${hasTutorial && turn == 0}">
    <news style="width: 468px;">
        <h1>Opinion</h1>
        <%= txtTutorial %>
    </news>
</c:if>

<c:if test="${totMilitaryNews > 0}">
    <news style="width: 468px;">
        <h1>Ministry of War</h1>
        <c:forEach items="${newsMilitary}" var="hofProfile">
            <c:if test="${hofProfile.front == false}">
                <c:set var="totNewsEntries" value="${totNewsEntries + 1}"/>
                <div class="flagEntry">
                    <c:choose>
                        <c:when test="${hofProfile.subject.id == -1}">
                            <input type="hidden" id="nationId${count}" value="none"/>
                            <img src='http://static.eaw1805.com/images/nations/nation-none-36.png'
                                 alt="Neutral"
                                 title="Neutral"
                                 height=24>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" id="nationId${count}" value="${hofProfile.subject.id}"/>
                            <img src='http://static.eaw1805.com/images/nations/nation-${hofProfile.subject.id}-36.png'
                                 alt="Flag of ${hofProfile.subject.name}"
                                 title="Flag of ${hofProfile.subject.name}"
                                 height=24>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="messageEntry">
                        ${hofProfile.text}
                </div>
            </c:if>
        </c:forEach>
    </news>
</c:if>

<c:set var="politicsAppeared" value="0"/>
<c:if test="${totNewsEntries < maxSideEntries && totPoliticalNews > 0}">
    <c:set var="politicsAppeared" value="1"/>
    <news style="width: 468px;">
        <h1>Politics</h1>
        <c:forEach items="${newsPolitics}" var="hofProfile">
            <c:if test="${hofProfile.front == false}">
                <c:set var="totNewsEntries" value="${totNewsEntries + 1}"/>
                <div class="flagEntry">
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
                <div class="messageEntry">
                        ${hofProfile.text}
                </div>
            </c:if>
        </c:forEach>
    </news>
</c:if>

<c:set var="economyAppeared" value="0"/>
<c:if test="${totNewsEntries < maxSideEntries && totEconomyNews > 0}">
<c:set var="economyAppeared" value="1"/>
<news style="width: 468px;">
<h1>Economy</h1>
<c:forEach items="${newsEconomy}" var="hofProfile">
<c:if test="${hofProfile.front == false}">
<c:set var="totNewsEntries" value="${totNewsEntries + 1}"/>
<c:choose>
<c:when test="${hofProfile.text.contains('Low taxation')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/images/buttons/taxation/MUILowTax.png'
             alt="Low Taxation"
             title="Low Taxation"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 390px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('Harsh taxation')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/images/buttons/taxation/MUIHarshTax.png'
             alt="Harsh Taxation"
             title="Harsh Taxation"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 390px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('rade city')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px;">
        <img src='http://static.eaw1805.com/tiles/sites/tcity01.png'
             alt="Trade city"
             title="Trade city"
             height=48>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 390px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('soldiers starved to death')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/tiles/epidemic.png'
             alt="Out of supply"
             title="Out of supply"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('died')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/img/commanders/skull.png'
             alt="Personality died"
             title="Personality died"
             height=32>
    </div>
    <div class="messageEntry" style="top: -18px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('Famine')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/tiles/epidemic.png'
             alt="Out of supply"
             title="Out of supply"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('not reachable')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 14px;">
        <img src='http://static.eaw1805.com/images/buttons/OutOfSupply32.png'
             alt="Out of supply"
             title="Out of supply"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('will not be operated')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px;">
        <img src='http://static.eaw1805.com/tiles/sites/tprod-${hofProfile.baseNewsId}.png'
             alt="Production site"
             title="Production site not operational"
             height=48>
    </div>
    <div class="messageEntry" style="top: -18px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('cannot operate')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px;">
        <img src='http://static.eaw1805.com/tiles/sites/tprod-${hofProfile.baseNewsId}.png'
             alt="Production site"
             title="Production site not operational"
             height=48>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('fishing')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/tiles/resources/resource-8.png'
             alt="Fishing"
             title="Fishing"
             height=24>
    </div>
    <div class="messageEntry" style="top: -16px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('colonial goods')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-14.png'
             alt="Colonial goods"
             title="Colonial goods"
             height=24>
    </div>
    <div class="messageEntry" style="top: -20px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('foreign aid')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 32px !important; margin-bottom: -6px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/images/buttons/ButHandOverTileOff.png'
             alt="Foreign Aid"
             title="Foreign Aid"
             height=32>
    </div>
    <div class="messageEntry" style="top: -20px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('capable traders')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 32px !important; margin-bottom: -6px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/images/buttons/ButLoadOff.png'
             alt="Money"
             title="Money"
             height=32>
    </div>
    <div class="messageEntry" style="top: -20px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('not have enough money')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-1.png'
             alt="Not enough money available"
             title="Not enough money available"
             height=24>
    </div>
    <div class="messageEntry" style="top: -20px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('money were collected')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-1.png'
             alt="Collected Taxes"
             title="Money"
             height=24>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; ">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('new citizens')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-2.png'
             alt="Collected Taxes"
             title="Money"
             height=24>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; ">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('marines remained sober')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -20px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-13.png'
             alt="Missing Wine"
             title="Missing Wine"
             height=24>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:otherwise>
    <div class="messageEntryLong">
            ${hofProfile.text}
    </div>
</c:otherwise>
</c:choose>
</c:if>
</c:forEach>
</news>
</c:if>

<c:set var="worldAppeared" value="0"/>
<c:if test="${totNewsEntries < maxSideEntries && !topWorld.isEmpty()}">
    <c:set var="worldAppeared" value="1"/>
    <news style="width: 468px;">
        <h1>World News</h1>
        <c:forEach items="${topWorld}" var="hofProfile">
            <c:if test="${hofProfile.front == false}">
                <c:set var="totNewsEntries" value="${totNewsEntries + 1}"/>
                <div class="flagEntry">
                    <c:choose>
                        <c:when test="${hofProfile.subject.id == -1}">
                            <input type="hidden" id="nationId${count}" value="none"/>
                            <img src='http://static.eaw1805.com/images/nations/nation-none-36.png'
                                 alt="Neutral"
                                 title="Neutral"
                                 height=24>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" id="nationId${count}" value="${hofProfile.subject.id}"/>
                            <img src='http://static.eaw1805.com/images/nations/nation-${hofProfile.subject.id}-36.png'
                                 alt="Flag of ${hofProfile.subject.name}"
                                 title="Flag of ${hofProfile.subject.name}"
                                 height=24>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="messageEntry">
                        ${hofProfile.text}
                </div>
            </c:if>
        </c:forEach>
    </news>
</c:if>

</td>
<td width="228px" valign="top" height="1px">
    <news>
        <h1>Victory Points</h1>
        <table border="0">
            <tr>
                <td align="left" width="190"><i>Previous Month</i></td>
                <td align="right" width="30"><i><fmt:formatNumber
                        type="number"
                        maxFractionDigits="0"
                        groupingUsed="true"
                        value="${vpPrev}"/></i>&nbsp;</td>
            </tr>
            <c:forEach items="${vpList}" var="hofProfile">
                <tr>
                    <td align="left" width="190">${hofProfile.text}</td>
                    <td align="right" width="30"><fmt:formatNumber
                            type="number"
                            maxFractionDigits="0"
                            groupingUsed="true"
                            value="${hofProfile.baseNewsId}"/>&nbsp;</td>
                </tr>
            </c:forEach>
            <tr>
                <th align="left" width="190"><b>Currect Victory Points</b></th>
                <td align="right" width="30"><fmt:formatNumber
                        type="number"
                        maxFractionDigits="0"
                        groupingUsed="true"
                        value="${vpNow}"/>&nbsp;</td>
            </tr>
            <tr>
                <th align="left" width="190"><b>Target Victory Points</b></th>
                <td align="right" width="30"><fmt:formatNumber
                        type="number"
                        maxFractionDigits="0"
                        groupingUsed="true"
                        value="${nation.vpWin}"/>&nbsp;(<fmt:formatNumber
                        type="number"
                        maxFractionDigits="2"
                        groupingUsed="true"
                        value="${100 * vpNow / (nation.vpWin * gameLengthModifier)}"/>%)
                </td>
            </tr>
        </table>
    </news>
</td>

<td width="228px" valign="top" height="1px">
    <news style="float: right; margin-right: 7pt;">
        <h1>Demographics</h1>
        <table border="0">
            <c:forEach items="${demographics}" var="hofProfile">
                <tr>
                    <td align="left" width="190">${hofProfile.text}</td>
                    <td align="right" width="30"><fmt:formatNumber
                            type="number"
                            maxFractionDigits="0"
                            groupingUsed="true"
                            value="${hofProfile.baseNewsId}"/>&nbsp;</td>
                </tr>
            </c:forEach>
        </table>
    </news>
</td>
</tr>

<tr valign="top" height="1px">
    <td width="228px" valign="top" height="1px">
        <news style="float: right; margin-right: 7pt;  height: 463px;">
            <h1>Land Forces</h1>
            <table border="0">
                <c:forEach items="${armyStatsRegion}" var="hofProfile">
                    <tr>
                        <td align="left" width="190">${hofProfile.text}</td>
                        <td align="right" width="30"><fmt:formatNumber
                                type="number"
                                maxFractionDigits="0"
                                groupingUsed="true"
                                value="${hofProfile.baseNewsId}"/>&nbsp;</td>
                    </tr>
                </c:forEach>
            </table>
            <hr class="orderList"
                style="border-style: dashed; border-color: black; margin-left: 4px; margin-right: 4px;">
            <table border="0">
                <c:forEach items="${armyStatsType}" var="hofProfile">
                    <tr>
                        <td align="left" width="190">${hofProfile.text}</td>
                        <td align="right" width="30"><fmt:formatNumber
                                type="number"
                                maxFractionDigits="0"
                                groupingUsed="true"
                                value="${hofProfile.baseNewsId}"/>&nbsp;</td>
                    </tr>
                </c:forEach>
            </table>
            <hr class="orderList"
                style="border-style: dashed; border-color: black; margin-left: 4px; margin-right: 4px;">
            <table border="0">
                <c:forEach items="${armyStatsCosts}" var="hofProfile">
                    <tr>
                        <td align="left" width="190">${hofProfile.text}</td>
                        <td align="right" width="30" valign="middle">
                            <c:choose>
                                <c:when test="${hofProfile.text.contains('Soldiers Salaries')}">
                                    <span style="font-size: smaller;">
                                    <fmt:formatNumber
                                            type="number"
                                            maxFractionDigits="0"
                                            groupingUsed="true"
                                            value="${hofProfile.baseNewsId}"/>&nbsp;</span>
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatNumber
                                            type="number"
                                            maxFractionDigits="0"
                                            groupingUsed="true"
                                            value="${hofProfile.baseNewsId}"/>&nbsp;
                                </c:otherwise>
                            </c:choose></td>
                    </tr>
                </c:forEach>
            </table>
        </news>
    </td>

    <td width="228px" valign="top" height="1px">
        <news style="float: right; margin-right: 7pt; height: 463px;">
            <h1>Naval Forces</h1>
            <table border="0">
                <c:forEach items="${navyStatsRegion}" var="hofProfile">
                    <tr>
                        <td align="left" width="195">${hofProfile.text}</td>
                        <td align="right" width="30"><fmt:formatNumber
                                type="number"
                                maxFractionDigits="0"
                                groupingUsed="true"
                                value="${hofProfile.baseNewsId}"/>&nbsp;</td>
                    </tr>
                </c:forEach>
            </table>
            <hr class="orderList"
                style="border-style: dashed; border-color: black; margin-left: 4px; margin-right: 4px;">
            <table border="0">
                <c:forEach items="${navyStatsType}" var="hofProfile">
                    <tr>
                        <td align="left" width="190">${hofProfile.text}</td>
                        <td align="right" width="30"><fmt:formatNumber
                                type="number"
                                maxFractionDigits="0"
                                groupingUsed="true"
                                value="${hofProfile.baseNewsId}"/>&nbsp;</td>
                    </tr>
                </c:forEach>
            </table>
            <hr class="orderList"
                style="border-style: dashed; border-color: black; margin-left: 4px; margin-right: 4px;">
            <table border="0">
                <c:forEach items="${navyStatsCosts}" var="hofProfile">
                    <tr>
                        <td align="left" width="190">${hofProfile.text}</td>
                        <td align="right" width="30" valign="middle">
                            <c:choose>
                                <c:when test="${hofProfile.text.contains('Marines Salaries')}">
                                    <span style="font-size: smaller;">
                                    <fmt:formatNumber
                                            type="number"
                                            maxFractionDigits="0"
                                            groupingUsed="true"
                                            value="${hofProfile.baseNewsId}"/>&nbsp;</span>
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatNumber
                                            type="number"
                                            maxFractionDigits="0"
                                            groupingUsed="true"
                                            value="${hofProfile.baseNewsId}"/>&nbsp;
                                </c:otherwise>
                            </c:choose></td>
                    </tr>
                </c:forEach>
            </table>
        </news>

    </td>
</tr>
</table>

<c:if test="${totPoliticalNews > 0 && politicsAppeared == 0}">
    <c:set var="politicsAppeared" value="1"/>
    <c:set var="extraArticles" value="${extraArticles + 1}"/>
    <news style="width: 468px;">
        <h1>Politics</h1>
        <c:forEach items="${newsPolitics}" var="hofProfile">
            <c:if test="${hofProfile.front == false}">
                <c:set var="totNewsEntries" value="${totNewsEntries + 1}"/>
                <div class="flagEntry">
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
                <div class="messageEntry">
                        ${hofProfile.text}
                </div>
            </c:if>
        </c:forEach>
    </news>
</c:if>

<c:if test="${totEconomyNews > 0 && economyAppeared == 0}">
<c:set var="extraArticles" value="${extraArticles + 1}"/>
<news style="width: 948px;">
<h1>Economy</h1>
<c:set var="count" value="0"/>
<table width="100%" border=0>
<tr>
<c:forEach items="${newsEconomy}" var="hofProfile">
<td
        <c:choose>
            <c:when test="${count % 2 == 1}">
                width="466px" style="border-left-style: dashed; border-left-width: 1px; border-left-color: black; padding-left: 10px;"
            </c:when>
            <c:otherwise>
                width="482px"
            </c:otherwise>
        </c:choose>
        >
<c:if test="${hofProfile.front == false}">
<c:choose>
<c:when test="${hofProfile.text.contains('Low taxation')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/images/buttons/taxation/MUILowTax.png'
             alt="Low Taxation"
             title="Low Taxation"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 390px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('Harsh taxation')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/images/buttons/taxation/MUIHarshTax.png'
             alt="Harsh Taxation"
             title="Harsh Taxation"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 390px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('rade city')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px;">
        <img src='http://static.eaw1805.com/tiles/sites/tcity01.png'
             alt="Trade city"
             title="Trade city"
             height=48>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 390px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('soldiers starved to death')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/tiles/epidemic.png'
             alt="Out of supply"
             title="Out of supply"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('died')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/img/commanders/skull.png'
             alt="Personality died"
             title="Personality died"
             height=32>
    </div>
    <div class="messageEntry" style="top: -18px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('Famine')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/tiles/epidemic.png'
             alt="Out of supply"
             title="Out of supply"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('not reachable')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 14px;">
        <img src='http://static.eaw1805.com/images/buttons/OutOfSupply32.png'
             alt="Out of supply"
             title="Out of supply"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('will not be operated')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px;">
        <img src='http://static.eaw1805.com/tiles/sites/tprod-${hofProfile.baseNewsId}.png'
             alt="Production site"
             title="Production site not operational"
             height=48>
    </div>
    <div class="messageEntry" style="top: -18px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('cannot operate')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px;">
        <img src='http://static.eaw1805.com/tiles/sites/tprod-${hofProfile.baseNewsId}.png'
             alt="Production site"
             title="Production site not operational"
             height=48>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('fishing')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/tiles/resources/resource-8.png'
             alt="Fishing"
             title="Fishing"
             height=24>
    </div>
    <div class="messageEntry" style="top: -16px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('colonial goods')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-14.png'
             alt="Colonial goods"
             title="Colonial goods"
             height=24>
    </div>
    <div class="messageEntry" style="top: -20px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('foreign aid')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 32px !important; margin-bottom: -6px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/images/buttons/ButHandOverTileOff.png'
             alt="Foreign Aid"
             title="Foreign Aid"
             height=32>
    </div>
    <div class="messageEntry" style="top: -20px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('capable traders')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 32px !important; margin-bottom: -6px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/images/buttons/ButLoadOff.png'
             alt="Money"
             title="Money"
             height=32>
    </div>
    <div class="messageEntry" style="top: -20px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('not have enough money')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-1.png'
             alt="Not enough money available"
             title="Not enough money available"
             height=24>
    </div>
    <div class="messageEntry" style="top: -20px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('money were collected')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-1.png'
             alt="Collected Taxes"
             title="Money"
             height=24>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; ">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('new citizens')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-2.png'
             alt="Collected Taxes"
             title="Money"
             height=24>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; ">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('marines remained sober')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -20px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-13.png'
             alt="Missing Wine"
             title="Missing Wine"
             height=24>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:otherwise>
    <div class="messageEntryLong">
            ${hofProfile.text}
    </div>
</c:otherwise>
</c:choose>
</c:if>
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
</c:if>


<c:set var="extraArticles" value="0"/>
<c:if test="${fn:length(addMilitary) > 0}">
    <c:set var="extraArticles" value="${extraArticles + 1}"/>
    <news style="width: 948px;">
        <h1>Ministry of War (continued)</h1>
        <c:set var="count" value="0"/>
        <table width="100%" border=0>
            <tr>
                <c:forEach items="${addMilitary}" var="hofProfile">
                <td
                        <c:choose>
                            <c:when test="${count % 2 == 1}">
                                width="466px" style="border-left-style: dashed; border-left-width: 1px; border-left-color: black; padding-left: 10px;"
                            </c:when>
                            <c:otherwise>
                                width="482px"
                            </c:otherwise>
                        </c:choose>
                        >
                    <c:set var="totNewsEntries" value="${totNewsEntries + 1}"/>
                    <div class="flagEntry">
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
                    <div class="messageEntry">
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
</c:if>

<c:if test="${totPoliticalNews > 0 && fn:length(addPolitical) > 0}">
    <c:set var="extraArticles" value="${extraArticles + 1}"/>
    <news style="width: 948px;">
        <h1>Politics (continued)</h1>
        <c:set var="count" value="0"/>
        <table width="100%" border=0>
            <tr>
                <c:forEach items="${addPolitical}" var="hofProfile">
                <td
                        <c:choose>
                            <c:when test="${count % 2 == 1}">
                                width="466px" style="border-left-style: dashed; border-left-width: 1px; border-left-color: black; padding-left: 10px;"
                            </c:when>
                            <c:otherwise>
                                width="482px"
                            </c:otherwise>
                        </c:choose>
                        >
                    <c:set var="totNewsEntries" value="${totNewsEntries + 1}"/>
                    <div class="flagEntry">
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
                    <div class="messageEntry">
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
</c:if>

<c:if test="${totEconomyNews > 0 && fn:length(addEconomy) > 0}">
<c:set var="extraArticles" value="${extraArticles + 1}"/>
<news style="width: 956px;">
<h1>Economy (continued)</h1>
<c:set var="count" value="0"/>
<table width="100%" border=0>
<tr>
<c:forEach items="${addEconomy}" var="hofProfile">
<td
        <c:choose>
            <c:when test="${count % 2 == 1}">
                width="466px" style="border-left-style: dashed; border-left-width: 1px; border-left-color: black; padding-left: 10px;"
            </c:when>
            <c:otherwise>
                width="482px"
            </c:otherwise>
        </c:choose>
        >
<c:if test="${hofProfile.front == false}">
<c:choose>
<c:when test="${hofProfile.text.contains('Low taxation')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/images/buttons/taxation/MUILowTax.png'
             alt="Low Taxation"
             title="Low Taxation"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 390px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('Harsh taxation')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/images/buttons/taxation/MUIHarshTax.png'
             alt="Harsh Taxation"
             title="Harsh Taxation"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 390px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('rade city')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px;">
        <img src='http://static.eaw1805.com/tiles/sites/tcity01.png'
             alt="Trade city"
             title="Trade city"
             height=48>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 390px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('soldiers starved to death')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/tiles/epidemic.png'
             alt="Out of supply"
             title="Out of supply"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('died')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/img/commanders/skull.png'
             alt="Personality died"
             title="Personality died"
             height=32>
    </div>
    <div class="messageEntry" style="top: -18px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('Famine')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/tiles/epidemic.png'
             alt="Out of supply"
             title="Out of supply"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('not reachable')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px; margin-left: 14px;">
        <img src='http://static.eaw1805.com/images/buttons/OutOfSupply32.png'
             alt="Out of supply"
             title="Out of supply"
             height=32>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('will not be operated')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px;">
        <img src='http://static.eaw1805.com/tiles/sites/tprod-${hofProfile.baseNewsId}.png'
             alt="Production site"
             title="Production site not operational"
             height=48>
    </div>
    <div class="messageEntry" style="top: -18px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('cannot operate')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -10px;">
        <img src='http://static.eaw1805.com/tiles/sites/tprod-${hofProfile.baseNewsId}.png'
             alt="Production site"
             title="Production site not operational"
             height=48>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('fishing')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/tiles/resources/resource-8.png'
             alt="Fishing"
             title="Fishing"
             height=24>
    </div>
    <div class="messageEntry" style="top: -16px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('colonial goods')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-14.png'
             alt="Colonial goods"
             title="Colonial goods"
             height=24>
    </div>
    <div class="messageEntry" style="top: -20px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('foreign aid')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 32px !important; margin-bottom: -6px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/images/buttons/ButHandOverTileOff.png'
             alt="Foreign Aid"
             title="Foreign Aid"
             height=32>
    </div>
    <div class="messageEntry" style="top: -20px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('capable traders')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 32px !important; margin-bottom: -6px; margin-left: 12px;">
        <img src='http://static.eaw1805.com/images/buttons/ButLoadOff.png'
             alt="Money"
             title="Money"
             height=32>
    </div>
    <div class="messageEntry" style="top: -20px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('not have enough money')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-1.png'
             alt="Not enough money available"
             title="Not enough money available"
             height=24>
    </div>
    <div class="messageEntry" style="top: -20px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('money were collected')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-1.png'
             alt="Collected Taxes"
             title="Money"
             height=24>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; ">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('new citizens')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 24px !important; margin-bottom: -10px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-2.png'
             alt="Collected Taxes"
             title="Money"
             height=24>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; ">
            ${hofProfile.text}
    </div>
</c:when>
<c:when test="${hofProfile.text.contains('marines remained sober')}">
    <div class="flagEntry"
         style="border-style: hidden; width: 48px !important; margin-bottom: -20px; margin-left: 18px;">
        <img src='http://static.eaw1805.com/images/goods/good-13.png'
             alt="Missing Wine"
             title="Missing Wine"
             height=24>
    </div>
    <div class="messageEntry" style="top: -12px !important; left: 58px !important; width: 400px;">
            ${hofProfile.text}
    </div>
</c:when>
<c:otherwise>
    <div class="messageEntryLong">
            ${hofProfile.text}
    </div>
</c:otherwise>
</c:choose>
</c:if>
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
</c:if>

<c:if test="${!topWorld.isEmpty() && worldAppeared == 0}">
    <news style="width: 956px;">
        <h1>World News</h1>
        <c:set var="count" value="0"/>
        <table width="100%" border=0>
            <tr>
                <c:forEach items="${topWorld}" var="hofProfile">
                <td
                        <c:choose>
                            <c:when test="${count % 2 == 1}">
                                width="466px" style="border-left-style: dashed; border-left-width: 1px; border-left-color: black; padding-left: 10px;"
                            </c:when>
                            <c:otherwise>
                                width="482px"
                            </c:otherwise>
                        </c:choose>
                        >
                    <div class="flagEntry">
                        <c:choose>
                            <c:when test="${hofProfile.subject.id == -1}">
                                <input type="hidden" id="nationId${count}" value="none"/>
                                <img src='http://static.eaw1805.com/images/nations/nation-none-36.png'
                                     alt="Neutral"
                                     title="Neutral"
                                     height=24>
                            </c:when>
                            <c:otherwise>
                                <input type="hidden" id="nationId${count}" value="${hofProfile.subject.id}"/>
                                <img src='http://static.eaw1805.com/images/nations/nation-${hofProfile.subject.id}-36.png'
                                     alt="Flag of ${hofProfile.subject.name}"
                                     title="Flag of ${hofProfile.subject.name}"
                                     height=24>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="messageEntry" style="width: 380px;">
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
</c:if>

<c:if test="${fn:length(addWorld) > 0}">
    <news style="width: 956px;">
        <h1>World News (continued)</h1>
        <c:set var="count" value="0"/>
        <table width="100%" border=0>
            <tr>
                <c:forEach items="${addWorld}" var="hofProfile">
                <td
                        <c:choose>
                            <c:when test="${count % 2 == 1}">
                                width="466px" style="border-left-style: dashed; border-left-width: 1px; border-left-color: black; padding-left: 10px;"
                            </c:when>
                            <c:otherwise>
                                width="482px"
                            </c:otherwise>
                        </c:choose>
                        >
                    <div class="flagEntry">
                        <c:choose>
                            <c:when test="${hofProfile.subject.id == -1}">
                                <input type="hidden" id="nationId${count}" value="none"/>
                                <img src='http://static.eaw1805.com/images/nations/nation-none-36.png'
                                     alt="Neutral"
                                     title="Neutral"
                                     height=24>
                            </c:when>
                            <c:otherwise>
                                <input type="hidden" id="nationId${count}" value="${hofProfile.subject.id}"/>
                                <img src='http://static.eaw1805.com/images/nations/nation-${hofProfile.subject.id}-36.png'
                                     alt="Flag of ${hofProfile.subject.name}"
                                     title="Flag of ${hofProfile.subject.name}"
                                     height=24>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="messageEntry" style="width: 380px;">
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
</c:if>

<c:if test="${fn:length(news) > 0}">
    <news style="width: 956px;">
        <h1>News and Rumours from Around the World</h1>
        <c:set var="count" value="0"/>
        <table width="100%" border=0>
            <tr>
                <c:forEach items="${news}" var="hofProfile">
                <td
                        <c:choose>
                            <c:when test="${count % 2 == 1}">
                                width="466px" style="border-left-style: dashed; border-left-width: 1px; border-left-color: black; padding-left: 10px;"
                            </c:when>
                            <c:otherwise>
                                width="482px"
                            </c:otherwise>
                        </c:choose>
                        >
                    <div class="flagEntry">
                        <c:choose>
                            <c:when test="${hofProfile.subject.id == -1}">
                                <input type="hidden" id="nationId${count}" value="none"/>
                                <img src='http://static.eaw1805.com/images/nations/nation-none-36.png'
                                     alt="Neutral"
                                     title="Neutral"
                                     height=24>
                            </c:when>
                            <c:otherwise>
                                <input type="hidden" id="nationId${count}" value="${hofProfile.subject.id}"/>
                                <img src='http://static.eaw1805.com/images/nations/nation-${hofProfile.subject.id}-36.png'
                                     alt="Flag of ${hofProfile.subject.name}"
                                     title="Flag of ${hofProfile.subject.name}"
                                     height=24>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="messageEntry" style="width: 380px;">
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
</c:if>

<c:if test="${!newsLetters.isEmpty()}">
    <c:set var="extraArticles" value="${extraArticles + 1}"/>
    <c:forEach items="${newsLetters}" var="hofProfile">
        <news style="width: 956px;">
            <h1>World Announcement<c:if
                    test="${hofProfile.type == 7}">&nbsp;from&nbsp;${hofProfile.subject.name}</c:if>
            </h1>
                ${hofProfile.text}
        </news>
    </c:forEach>
</c:if>

<c:if test="${hasTutorial && turn > 0}">
    <news style="width: 956px;">
        <h1>Opinion</h1>
        <%= txtTutorial %>
    </news>
</c:if>

<c:if test="${hasHistory}">
    <news style="width: 956px;">
        <%= txtHistory %>
    </news>
</c:if>

</article>
<% if (request.getParameter("fixForClient") == null) { %>
<br style="clear: both;"/>
<br/>
<hr class="orderList">
<h3>Older months</h3>
<c:forEach items="${months}" var="thisMonth">
    <c:if test="${turn== thisMonth.key}"><b></c:if>
    &nbsp;<a href='<c:url
        value="/report/scenario/${game.scenarioIdToString}/game/${gameId}/nation/${nationId}/month/${thisMonth.key}"/>'>${thisMonth.value}</a>&nbsp;
    <c:if test="${turn== thisMonth.key}"></b></c:if>
</c:forEach>
<%}%>
