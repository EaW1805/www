<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="firstPage" scope="request" class="java.lang.Boolean"/>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.sparkline.min.js'></script>
<c:if test="${firstPage}">
    <div class="pagehead"
         style="background-image: url('http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtonsBase.png');
                background-position: 40px 0px;
                background-repeat: no-repeat;
                float: left;
                margin-top: -80px;
                clear: both;
                background-size: 585px 597px;
                margin-top: -20px;
                margin-left: 118px;
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
<c:if test="${!firstPage}">
<jsp:useBean id="pageType" scope="request" class="java.lang.String"/>
<c:if test="${pageType != 'new' && pageType != 'running'}">
    <style type="text/css">
        .pagehead {
            background: none;
        }

        #content {
            padding-left: 10px;
            padding-right: 60px;
            padding-bottom: 0px;
            overflow: visible;
        }

        #footer .parchment-footer {
            background: none;
        }

    </style>
</c:if>
<script type="text/javascript">
    $.blockUI.defaults.applyPlatformOpacityRules = false;
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
        $.blockUI({ message:$('#question'), css:{ width:'390px' } });
    }
</script>
<c:choose>
<c:when test="${pageType == 'new'}">
    <h1>Start New Game</h1>
    <jsp:useBean id="freeNations" scope="request"
                 type="java.util.HashMap<java.lang.Integer, java.util.LinkedList<java.util.LinkedList<java.lang.Object>>> "/>
    <c:choose>
        <c:when test="${fn:length(freeNations) < 1}">
            <p class="manual" style="font-size: 14pt;">There are <strong>no new games</strong> open.</p>

            <p class="manual" style="font-size: 12pt;">You can <a href="<c:url value='/joingame/running'/>">join a
                running game</a> by picking up an existing country.</p>

            <p class="manual" style="font-size: 12pt;">You can request for a new game to open by contacting the
                <a href="<c:url value="/contact"/>">support</a> stating the country you wish to play.</p>

            <p class="manual" style="font-size: 12pt;">In the mean time you can start playing a solo game
                <strong>now</strong> at your own pace,
                by clicking <a href="<c:url value="/joingame/free"/>">here</a>.</p>
        </c:when>
        <c:otherwise>
            <p class="manual" style="font-size: 12pt;">You can join a new game by picking up a new country below. The
                position
                will then appear in <a href="<c:url value='/games'/>">your games page</a> (just click your name at the
                top right
                of this page), scroll down to find the position, and start playing!</p>

            <p class="manual" style="font-size: 12pt;">If you wish to see more information about the game and who is
                playing in
                it, click on <a href="<c:url value='/listgames'/>">Games Lobby</a> at top left corner then click on the
                game
                number that you are interested in.</p>

            <p class="manual" style="font-size: 12pt;">When the game starts you will receive a notification, but you can
                already
                put your first
                turn in right now!</p>
        </c:otherwise>
    </c:choose>
</c:when>
<c:when test="${pageType == 'running'}">
    <h1>Join Existing Game</h1>
    <jsp:useBean id="freeNations" scope="request"
                 type="java.util.HashMap<java.lang.Integer, java.util.LinkedList<java.util.LinkedList<java.lang.Object>>> "/>
    <c:choose>
        <c:when test="${fn:length(freeNations) < 1}">
            <p class="manual" style="font-size: 14pt;">There are <strong>no available positions</strong> in running
                games.</p>

            <p class="manual" style="font-size: 12pt;">You can <a href="<c:url value='/joingame/new'/>">join a new
                game</a> by picking up an free country.</p>

            <p class="manual" style="font-size: 12pt;">You can request for a position by contacting the
                <a href="<c:url value="/contact"/>">support</a> stating the country you wish to play.</p>

            <p class="manual" style="font-size: 12pt;">In the mean time you can start playing a solo game
                <strong>now</strong> at your own pace,
                by clicking <a href="<c:url value="/joingame/free"/>">here</a>.</p>
        </c:when>
        <c:otherwise>

            <p class="manual" style="font-size: 12pt;">You can join a running game by picking up an existing country
                from below.
                The position will
                then appear in <a href="<c:url value='/games'/>">your games page</a> (just click your name at the top
                right of this page), scroll down to find the position, and start playing!</p>

            <p class="manual" style="font-size: 12pt;">If you wish to see more information about the game and who is
                playing in
                it before you
                pick it up, click on <a href="<c:url value='/listgames'/>">Games Lobby</a> at top left corner then click
                on
                the game number that you are interested in.</p>

            <p class="manual" style="font-size: 12pt;">Joining an existing game places you faster into action, and is
                also good
                for new players
                since they learn how to play with an existing position before setting their own empire up.</p>

            <p class="manual" style="font-size: 14pt;"><strong>Special Offer:</strong>
                If the position you pick up has been inactive for 2 or more turns, then we offer the following bonuses:
            <ul style="margin-left: 40px; padding-bottom: 10px;">
                <li class="manual" style="font-size: 14pt;">Double AP/CP for one turn,</li>
                <li class="manual" style="font-size: 14pt;">60 free credits to play the country.</li>
            </ul>
            </p>
        </c:otherwise>
    </c:choose>
</c:when>
<c:when test="${pageType == 'free'}">
    <div id="main-article"
    style="height: 280px; position: relative;margin: 0px 0px 0px -35px;width: 1030px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
    repeat-y scroll 0px 0px transparent;clear: both;
    padding-left: 35px;">

    <div style="width: 980px;">
        <h1>Start a Free Solo-Game</h1>

        <p class="manual" style="font-size: 12pt;">You can start a new solo-game and assume the role of Napoleon
            Bonaparte starting from
            January 1804. Napoleon Bonaparte was a French military and political leader who rose to prominence
            during the latter stages of the French Revolution and its associated wars in Europe.</p>

        <p class="manual" style="font-size: 12pt;">As soon as you press the <b>Play Now</b> button, your game will be
            created within the
            next 5-10 minutes.</p>

        <p class="manual" style="font-size: 12pt;">Once your game is created, you will receive a notification via
            e-mail. It will also appear
            in <a href="<c:url value='/games'/>">your games page</a> (just click your name at the top right
            of this page), scroll down to find the position, and start playing!</p>

        <div style="margin-top: 10px;
                    float: left;">
            <a href="<c:url value="/joingame/start"/>">
                <img src="http://static.eaw1805.com/images/site/gameButtons/CreateNewGameoff.png"
                     alt="Start Free Solo-Game of Empires at War 1805"
                     title="Start Free Solo-Game"
                     onclick="this.src='http://static.eaw1805.com/images/site/gameButtons/CreateNewGameoff.png'"
                     onmouseover="this.src='http://static.eaw1805.com/images/site/gameButtons/CreateNewGamehov.png'"
                     onmouseout="this.src='http://static.eaw1805.com/images/site/gameButtons/CreateNewGameoff.png'">
            </a></div>
    </div>

    <div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
    </div>
</c:when>
<c:when test="${pageType == 'start'}">
    <div id="main-article"
    style="height: 178px; position: relative;margin: 0px 0px 0px -35px;width: 1030px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
    repeat-y scroll 0px 0px transparent;clear: both;
    padding-left: 35px;">

    <div style="width: 980px;">
        <h1>Your Free Solo-Game is Being Generated</h1>

        <p class="manual" style="font-size: 12pt;">Your new solo-game request has been submitted and within the next
            5-10 minutes your free
            game will be created.</p>

        <p class="manual" style="font-size: 12pt;">Once your game is created, you will receive a notification via
            e-mail. It will also appear
            in <a href="<c:url value='/games'/>">your games page</a> (just click your name at the top right
            of this page), scroll down to find the position, and start playing!</p>

        <p class="manual" style="font-size: 12pt;">In the meantime you can go through the Quick start guide to get a
            basic introduction to
            Empires at War.</p>

        <p class="manual" style="font-size: 12pt;">Enjoy your free game!</p>
    </div>

    <div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
    </div>
</c:when>
<c:when test="${pageType == 'single'}">
    <div id="main-article"
    style="height: 457px; position: relative;margin: 0px 0px 0px -35px;width: 1030px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
    repeat-y scroll 0px 0px transparent;clear: both;
    padding-left: 35px;">

    <div style="width: 980px;">
        <h1>You already play an active Free Solo-Game</h1>

        <p class="manual" style="font-size: 12pt;">You can play one active free solo-game.</p>

        <p class="manual" style="font-size: 12pt;">If you wish to start start a new solo-game, we kindly ask you to
            first drop the solo-game
            that you are currently playing.</p>

        <p class="manual" style="font-size: 12pt;">As soon as you press the <b>Finish Game</b> button, your game will be
            finished.</p>

        <p class="manual" style="font-size: 12pt;">Once your game is finished, you will be able to create a new one by
            following the link
            <a href="<c:url value='/joingame/free'/>">Start a Free Game</a>.</p>

        <div style="margin-top: 10px;
                        margin-bottom: 10px;
                        float: left;">
            <a href="<c:url value="/joingame/finish"/>">
                <img src="http://static.eaw1805.com/images/site/gameButtons/FinishGameoff.png"
                     alt="Finish your Free Solo-Game of Empires at War 1805"
                     title="Finish your Free Solo-Game"
                     onclick="this.src='http://static.eaw1805.com/images/site/gameButtons/FinishGameoff.png'"
                     onmouseover="this.src='http://static.eaw1805.com/images/site/gameButtons/FinishGamehov.png'"
                     onmouseout="this.src='http://static.eaw1805.com/images/site/gameButtons/FinishGameoff.png'">
            </a></div>

        <p class="manual" style="font-size: 12pt; clear:both;">We would like to point our multi-player <a
                href="<c:url value='/scenario/1802/info'/>">Scenario
            1805</a>.</p>

        <p class="manual" style="font-size: 12pt;">You can play multiple games of Scenario 1805 at the same time along
            with your Scenario 1804
            Solo-Game!</p>

        <p class="manual" style="font-size: 12pt;">You can join a new game by picking up an <a
                href="<c:url value='/joingame/new'/>">new
            country</a>.</p>

        <p class="manual" style="font-size: 12pt;">You can also join a <a href="<c:url value='/joingame/running'/>">running
            game</a> by
            selecting an existing country. Joining an existing game places you faster into action, and is also good for
            new players
            since they learn how to play with an existing position before setting their own empire up.</p>

        <p class="manual" style="font-size: 12pt;">The position will then appear in <a href="<c:url value='/games'/>">your
            games page</a>
            (just click your name at the top right of this page), scroll down to
            find the position, and start playing!</p>

        <p class="manual" style="font-size: 12pt;">Enjoy Empires at War 1805!</p>
    </div>

    <div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
    </div>
</c:when>
<c:when test="${pageType == 'finish'}">
    <div id="main-article"
    style="height: 287px; position: relative;margin: 0px 0px 0px -35px;width: 1030px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
    repeat-y scroll 0px 0px transparent;clear: both;
    padding-left: 35px;">

    <div style="width: 980px;">
        <h1>Your Free Solo-Game is Finished</h1>

        <p class="manual" style="font-size: 12pt;">Your solo-game is now finished.</p>

        <p class="manual" style="font-size: 12pt;">We hope that you enjoyed playing Scenarion 1804.</p>

        <p class="manual" style="font-size: 12pt;">We invite you to join our multi-player <a
                href="<c:url value='/scenario/1802/info'/>">scenario</a>.</p>

        <p class="manual" style="font-size: 12pt;">You can join a new game by picking up an <a
                href="<c:url value='/joingame/new'/>">new
            country</a>.</p>

        <p class="manual" style="font-size: 12pt;">You can also join a <a href="<c:url value='/joingame/running'/>">running
            game</a> by
            selecting an existing country. Joining an existing game places you faster into action, and is also good for
            new players
            since they learn how to play with an existing position before setting their own empire up.</p>

        <p class="manual" style="font-size: 12pt;">The position will then appear in <a href="<c:url value='/games'/>">your
            games page</a>
            (just click your name at the top right of this page), scroll down to
            find the position, and start playing!</p>

        <p class="manual" style="font-size: 12pt;">Enjoy Empires at War 1805!</p>
    </div>

    <div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
    </div>
</c:when>
<c:when test="${pageType == 'abouttostart'}">
    <div id="main-article"
    style="height: 463px; position: relative;margin: 0px 0px 0px -35px;width: 1030px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
    repeat-y scroll 0px 0px transparent;clear: both;
    padding-left: 35px;">

    <div style="width: 980px;">
        <h1>Your Free Solo-Game is Being Generated</h1>

        <p class="manual" style="font-size: 12pt;">You have requested a new solo-game. Our servers are executing your
            request and within
            the next 5-10 minutes your free game will be created.</p>

        <p class="manual" style="font-size: 12pt;">Once your game is created, you will receive a notification via
            e-mail. It will also appear
            in <a href="<c:url value='/games'/>">your games page</a> (just click your name at the top right
            of this page), scroll down to find the position, and start playing!</p>

        <p class="manual" style="font-size: 12pt;">At this point we cannot recall your request for the new game.</p>

        <p class="manual" style="font-size: 12pt;">If you are certain that you do not wish to start this new game, we
            kindly ask you to
            request to drop your game as soon as your game is properly created.</p>

        <div style="margin-top: 10px;
                    margin-bottom: 10px;
                    float: left;">
            <a href="<c:url value="/joingame/finish"/>">
                <img src="http://static.eaw1805.com/images/site/gameButtons/FinishGameoff.png"
                     alt="Finish your Free Solo-Game of Empires at War 1805"
                     title="Finish your Free Solo-Game"
                     onclick="this.src='http://static.eaw1805.com/images/site/gameButtons/FinishGameoff.png'"
                     onmouseover="this.src='http://static.eaw1805.com/images/site/gameButtons/FinishGamehov.png'"
                     onmouseout="this.src='http://static.eaw1805.com/images/site/gameButtons/FinishGameoff.png'">
            </a></div>

        <p class="manual" style="font-size: 12pt; clear:both;">We would like to point our multi-player <a
                href="<c:url value='/scenario/1802/info'/>">Scenario
            1805</a>.</p>

        <p class="manual" style="font-size: 12pt;">You can play multiple games of Scenario 1805 at the same time!</p>

        <p class="manual" style="font-size: 12pt;">You can join a new game by picking up an <a
                href="<c:url value='/joingame/new'/>">new
            country</a>.</p>

        <p class="manual" style="font-size: 12pt;">You can also join a <a href="<c:url value='/joingame/running'/>">running
            game</a> by
            selecting an existing country. Joining an existing game places you faster into action, and is also good for
            new players
            since they learn how to play with an existing position before setting their own empire up.</p>

        <p class="manual" style="font-size: 12pt;">The position will then appear in <a href="<c:url value='/games'/>">your
            games page</a>
            (just click your name at the top right of this page), scroll down to
            find the position, and start playing!</p>

        <p class="manual" style="font-size: 12pt;">Enjoy Empires at War 1805!</p>

    </div>

    <div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
    </div>
</c:when>
<c:when test="${pageType == 'notfound'}">
    <div id="main-article"
    style="height: 154px; position: relative;margin: 0px 0px 0px -35px;width: 1030px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
    repeat-y scroll 0px 0px transparent;clear: both;
    padding-left: 35px;">

    <div style="width: 980px;">
        <h1>We cannot find your Free Solo-Game</h1>

        <p class="manual" style="font-size: 12pt;">You have requested to finish your free solo game. At this point we
            cannot locate your
            game.</p>

        <p class="manual" style="font-size: 12pt;">If you have already requested to finish your game, then we kindly ask
            you to ignore this
            message.</p>

        <p class="manual" style="font-size: 12pt;">If you have not requested to finish your game, please <a
                href="<c:url value="/contact"/>">contact
            us</a> to try to sort this problem.</p>

        <p class="manual" style="font-size: 12pt;">We apologize for any inconvenience caused.</p>
    </div>

    <div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
    </div>
</c:when>
</c:choose>
<c:choose>
<c:when test="${pageType == 'new' || pageType == 'running'}">
    <c:if test="${fn:length(freeNations) > 0}">
        <c:forEach items="${nations}" var="nation">
            <c:if test="${nation.id != -1 && fn:length(freeNations[nation.id]) > 0}">
                <section id="freeNations" style="width: 955px; height: auto;">
                    <nationList style="width: 954px; margin: 5px 0px 5px 3px;  border: none; float: left;">
                        <ul class="nationList">
                            <li class="header" style="width:900px;">
                                <dl class="nationList" style="height: 30px;margin-bottom: 7px;">
                                    <dt class="nationList">
                                        <img style="margin: 0; border: 0; border-radius: 0;"
                                             src='http://static.eaw1805.com/images/nations/nation-${nation.id}-list.jpg'
                                             alt="Nation's Flag"
                                             class="toolTip"
                                             title="${nation.name}"
                                             border=0>
                                </dl>
                            </li>
                            <c:forEach items="${freeNations[nation.id]}" var="thisNationData">
                                <li class="nationList" style="height: 90px;width: 290px; float:left; margin:0;margin-left: 5px; margin-bottom: 2px;
                                    border-right: 1px solid rgb(143, 143, 143);
                                    cursor: pointer;"
                                    title="Play now"
                                    onclick='openPickupForm("${thisNationData[0].scenarioIdToString}","${thisNationData[0].gameId}","${thisNationData[1].id}", "${thisNationData[1].name}", "${thisNationData[4].cost}" );'>
                                    <dl class="nationList">
                                        <dt class="nationList"
                                            style='width: 20pt; padding-top: 7px;margin-top: -6px;margin-left: 5px; line-height: 1.3 !important;
                                                       font-family: Georgia,"Times New Roman",Times,serif;
                                                       text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);font-size: 1.7em;'>
                                            Game ${thisNationData[0].gameId} /
                                        </dt>

                                        <dd class="nationList"
                                            style='width: 20pt; padding-top: 7px;margin-left: 50px; line-height: 1.3 !important;
                                                       font-family: Georgia,"Times New Roman",Times,serif;
                                                       text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);font-size: 1.1em;'>
                                                ${monthsSmall[thisNationData[0].gameId]}
                                        </dd>
                                        <dt class="nationList"
                                            style='width: 20pt; padding-top: 7px; font-family: Georgia,"Times New Roman",Times,serif; text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2); line-height: 1.3 ! important; margin-left: -113px; margin-top: 13px; font-size: 1.1em;'>
                                            Scenario ${thisNationData[0].scenarioIdToString}
                                        </dt>

                                        <dd class="nationList"
                                            style="width: 30pt; padding-top: 7px;margin-top: -2px;margin-left: 60px;">
                                            vp:
                                        </dd>

                                        <dd class="nationList"
                                            style="width: 20pt; padding-top: 7px;margin-top: -8px;font-size:30px; margin-left: 5px;">
                                            <fmt:formatNumber
                                                    type="number"
                                                    maxFractionDigits="0"
                                                    groupingUsed="true"
                                                    value="${thisNationData[2]}"/>
                                        </dd>

                                        <dt class="nationList" style="width: 140px; clear: both; margin-top: -10px;">
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

                                        <div style="float: right;margin-left: 5px;width: 80px; margin-top:15px; height:30px;">
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
                                            <div style="float: left; width: 90px; height:88px; margin-top:-30px; margin-left: -165px;">
                                                <img style="float: left; border: 0;margin: 0;padding: 0;"
                                                     src='http://static.eaw1805.com/images/site/paypal/60-free-ribbon-r.png'
                                                     title="Special Offer: Get 60 credits for FREE"
                                                     alt="Special offer"
                                                     class="toolTip"
                                                     border=0/>
                                            </div>
                                        </c:if>
                                    </dl>
                                </li>

                            </c:forEach>

                        </ul>
                    </nationList>
                </section>

            </c:if>
        </c:forEach>
    </c:if>
</c:when>
<c:otherwise>
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

    <div style=" z-index: 1; position: relative;margin: -10px 0px 0px -35px;width: 1000px;min-height: 800px;padding: 30px 40px;
background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">

        <div style="float: left;margin-left: 10px;">
            <h3 style="padding-top: 6px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                Tutorial 1: Forming Corps &amp; Armies</h3>
            <iframe width="400" height="225" src="http://www.youtube.com/embed/4toIINeKfxY" frameborder="0"
                    allowfullscreen></iframe>
        </div>

        <div style="float: right;margin-right: 60px;">
            <h3 style="padding-top: 6px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                Tutorial 2: Managing Fleets</h3>
            <iframe width="400" height="225" src="http://www.youtube.com/embed/3NcPqVQpHcg" frameborder="0"
                    allowfullscreen></iframe>
        </div>

        <div style="float: left;margin-left: 10px;">
            <h3 style="padding-top: 16px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                Tutorial 3: Exchanging &amp; Merging Battalions</h3>
            <iframe width="400" height="225" src="http://www.youtube.com/embed/__7-udFwhEM" frameborder="0"
                    allowfullscreen></iframe>
        </div>

        <div style="float: right;margin-right: 60px;">
            <h3 style="padding-top: 16px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                Tutorial 4: Movement</h3>
            <iframe width="400" height="225" src="http://www.youtube.com/embed/S62I6UWO0U0" frameborder="0"
                    allowfullscreen></iframe>
        </div>

        <div style="float: left;margin-left: 10px;">
            <h3 style="padding-top: 16px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                Tutorial 5: Conquest</h3>
            <iframe width="400" height="225" src="http://www.youtube.com/embed/NZ1pDkFCLS8" frameborder="0"
                    allowfullscreen></iframe>
        </div>

        <div style="float: right;margin-right: 60px;">
            <h3 style="padding-top: 16px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                Tutorial 6: Trading</h3>
            <iframe width="400" height="225" src="http://www.youtube.com/embed/Pmd8m8TRnBg" frameborder="0"
                    allowfullscreen></iframe>
        </div>
    </div>
    <div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78"></div>

</c:otherwise>
</c:choose>
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
</c:if>
