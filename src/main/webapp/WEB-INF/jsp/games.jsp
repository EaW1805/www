<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="dates" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
<jsp:useBean id="gameList" scope="request" type="java.util.List"/>
<jsp:useBean id="endedList" scope="request" type="java.util.List"/>
<jsp:useBean id="activityStat" scope="request" type="java.util.Map"/>
<jsp:useBean id="watchedGames" scope="request"
             type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="gameToNationToPlayer" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, com.eaw1805.data.model.User>>"/>
<jsp:useBean id="gameToNationToStatus" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, java.lang.Boolean>>"/>
<jsp:useBean id="gameToNationToAlive" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, java.lang.Boolean>>"/>
<jsp:useBean id="gameToTopVP" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.Nation>>"/>
<jsp:useBean id="gameToTopSize" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.Nation>>"/>
<jsp:useBean id="gameToTopArmy" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.Nation>>"/>
<jsp:useBean id="gameToTopNavy" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.Nation>>"/>
<jsp:useBean id="gameToTopMoney" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.Nation>>"/>
<jsp:useBean id="gameToWinner" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.Nation>>"/>
<jsp:useBean id="gameToCoWinner" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.Nation>>"/>
<jsp:useBean id="gameToRunnerUp" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.Nation>>"/>
<jsp:useBean id="gameNations" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.Nation>>"/>
<jsp:useBean id="gameToFree" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.Nation>>"/>
<jsp:useBean id="gameToDead" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.Nation>>"/>
<jsp:useBean id="freeNations" scope="request" type="java.util.LinkedList<java.util.LinkedList<java.lang.Object>>"/>
<jsp:useBean id="specialOffer" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Boolean>"/>
<jsp:useBean id="userIdToUser" scope="request" type="java.util.Map<java.lang.Integer, com.eaw1805.data.model.User>"/>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.sparkline.min.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/social.js'></script>
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
</style>
<script type="text/javascript">
    $(function () {
        /** This code runs when everything has been loaded on the page */
        /* Use 'html' instead of an array of values to pass options
         to a sparkline with data in the tag */
        $('.inlinebar').sparkline('html', {type:'bar', barColor:'#c57101'});
        $('.inlinebar-player').sparkline('html', {type:'bar', barColor:'#feff00'});
    });
</script>
<script type="text/javascript">
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

    $(document).ready(function () {
        <c:choose>
        <c:when test="${param.err != null}">
        $.blockUI({ message:$('#error_message_${param.err}'), css:{ width:'390px' } });
        </c:when>
        <c:when test="${param.status != null}">
        <c:if test="${param.status == 'joined'}">
        $.blockUI({ message:$('#status_joined'), css:{ width:'390px' } });
        </c:if>
        </c:when>
        </c:choose>
    });
</script>
<div id="main-article"
     style="min-height:464px;position: relative;margin: 0px 0px 0px -35px;width: 1033px;
     background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
     repeat-y scroll 0px 0px transparent;clear: both;
     padding-left: 35px;">
<script type="text/javascript">
    $('#main-article').css({ 'min-height':${fn:length(gameList) * 236} +40});
</script>
<article>
<h1 class="bigmap" style="font-size: 36px;">Active Games</h1>
<c:forEach items="${gameList}" var="game">
<game>
<h1 style="width: 500px; font-size: 36px !important;">
    <a href="<c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/info"/>">Game #${game.gameId}</a>
    / ${dates[game.gameId]}
</h1>

<div style="float:right; margin-right: 5px;margin-top: -50px;">
    <c:choose>
        <c:when test="${game.status.contains('processed')}">
            <b>${game.status}</b>
        </c:when>
        <c:when test="${game.turn == 0}">
            <b>Waiting for players</b>
        </c:when>
        <c:otherwise>
            Deadline: ${game.dateNextProc}
        </c:otherwise>
    </c:choose>
</div>

<c:if test="${game.userId != 2}">

    <div style="position: relative; width: 50px; margin-left: 498px; margin-top: -46px;">
        <ul>
            <li style="width: 111px; height: 38px;">
                <div style="position: relative; width: 66px; top: -7px;">
                    <h3>Game<br>Owned By</h3>
                </div>
                <a href="<c:url value="/user/${userIdToUser[game.userId].username}"/>"
                   title="${userIdToUser[game.userId].username}">
                    <img src="https://secure.gravatar.com/avatar/${userIdToUser[game.userId].emailEncoded}?s=36&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                         class="avatarLink"
                         width="36"
                         style="position: relative; left: 69px; top: -32px; border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                         title="${userIdToUser[game.userId].username}"/>
                </a>
            </li>
        </ul>
    </div>
</c:if>

<div style="float:right;margin-top: -40px;margin-right: 300px;">
    <c:if test="${!watchedGames.containsKey(game.gameId)}">
        <a href="<c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/toggleWatch/watch"/>"
           class="minibutton"
           title="Watch game">
            <img src="http://static.eaw1805.com/images/buttons/ButSpyReportsMapOff.png"
                 alt="Watch Game"
                <%--      title="Watch Game"--%>
                 onmouseover="this.src='http://static.eaw1805.com/images/buttons/ButSpyReportsMapHover.png'"
                 onmouseout="this.src='http://static.eaw1805.com/images/buttons/ButSpyReportsMapOff.png'"
                 border=0></a>
    </c:if>
    <c:if test="${watchedGames.containsKey(game.gameId) && watchedGames[game.gameId]==1}">
        <a href="<c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/toggleWatch/unwatch"/>"
           class="minibutton"
           title="Stop watching game">
            <img src="http://static.eaw1805.com/images/buttons/ButSpyReportsMapOn.png"
                 alt="Stop watching game"
                 title="Stop watching game"
                 onmouseover="this.src='http://static.eaw1805.com/images/buttons/ButSpyReportsMapHover.png'"
                 onmouseout="this.src='http://static.eaw1805.com/images/buttons/ButSpyReportsMapOn.png'"
                 border=0></a>
    </c:if>
</div>

<div style="float:left; padding: 3px; width:95px; margin-top: -10px;">
    <h3 style="font-weight: normal; margin-left: 12px; margin-bottom: -2px;">Top-3 VP</h3>
    <ul>
        <c:forEach items="${gameToTopVP[game.gameId]}" var="nation">
            <li><img
            <c:choose>
            <c:when test="${gameToNationToAlive[game.gameId][nation.id] == false}">
                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36Slc.png'
            </c:when>
            <c:otherwise>
                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
            </c:otherwise>
            </c:choose>
                    alt="${nation.name}"
                    class="toolTip"
                    title="${nation.name}"
                    border=0 height=24>&nbsp;
                <c:if test="${gameToNationToStatus[game.gameId][nation.id] == true}">
                    <a href="<c:url value="/user/${gameToNationToPlayer[game.gameId][nation.id].username}"/>"
                       title="${gameToNationToPlayer[game.gameId][nation.id].username}"><img
                            src="https://secure.gravatar.com/avatar/${gameToNationToPlayer[game.gameId][nation.id].emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                            alt="${gameToNationToPlayer[game.gameId][nation.id].username}" height="24"
                            style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                            width="24"
                            class="tooltip"></a>
                </c:if>
            </li>
        </c:forEach>
    </ul>
</div>

<div style="float:left;  padding: 3px;  width:95px;margin-top: -10px;">
    <h3 style="font-weight: normal; margin-left: 6px; margin-bottom: -2px;">Top-3 Land</h3>
    <ul>
        <c:forEach items="${gameToTopSize[game.gameId]}" var="nation">
            <li><img
            <c:choose>
            <c:when test="${gameToNationToAlive[game.gameId][nation.id] == false}">
                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36Slc.png'
            </c:when>
            <c:otherwise>
                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
            </c:otherwise>
            </c:choose>
                    alt="${nation.name}"
                    class="toolTip"
                    title="${nation.name}"
                    border=0 height=24>&nbsp;
                <c:if test="${gameToNationToStatus[game.gameId][nation.id] == true}">
                    <a href="<c:url value="/user/${gameToNationToPlayer[game.gameId][nation.id].username}"/>"
                       title="${gameToNationToPlayer[game.gameId][nation.id].username}"><img
                            src="https://secure.gravatar.com/avatar/${gameToNationToPlayer[game.gameId][nation.id].emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                            alt="${gameToNationToPlayer[game.gameId][nation.id].username}" height="24"
                            style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                            width="24"
                            class="tooltip"></a>
                </c:if>
            </li>
        </c:forEach>
    </ul>
</div>

<div style="float:left; padding: 3px;  width:95px;margin-top: -10px;">
    <h3 style="font-weight: normal; margin-left: 6px; margin-bottom: -2px;">Top-3 Money</h3>
    <ul>
        <c:forEach items="${gameToTopMoney[game.gameId]}" var="nation">
            <li><img
            <c:choose>
            <c:when test="${gameToNationToAlive[game.gameId][nation.id] == false}">
                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36Slc.png'
            </c:when>
            <c:otherwise>
                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
            </c:otherwise>
            </c:choose>
                    alt="${nation.name}"
                    class="toolTip"
                    title="${nation.name}"
                    border=0 height=24>&nbsp;
                <c:if test="${gameToNationToStatus[game.gameId][nation.id] == true}">
                    <a href="<c:url value="/user/${gameToNationToPlayer[game.gameId][nation.id].username}"/>"
                       title="${gameToNationToPlayer[game.gameId][nation.id].username}"><img
                            src="https://secure.gravatar.com/avatar/${gameToNationToPlayer[game.gameId][nation.id].emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                            alt="${gameToNationToPlayer[game.gameId][nation.id].username}" height="24"
                            style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                            width="24"
                            class="tooltip"></a>
                </c:if>
            </li>
        </c:forEach>
    </ul>
</div>

<div style="float:left; padding: 3px;  width:95px;margin-top: -10px;">
    <h3 style="font-weight: normal; margin-left: 6px; margin-bottom: -2px;">Top-3 Army</h3>
    <ul>
        <c:forEach items="${gameToTopArmy[game.gameId]}" var="nation">
            <li><img
            <c:choose>
            <c:when test="${gameToNationToAlive[game.gameId][nation.id] == false}">
                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36Slc.png'
            </c:when>
            <c:otherwise>
                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
            </c:otherwise>
            </c:choose>
                    alt="${nation.name}"
                    class="toolTip"
                    title="${nation.name}"
                    border=0 height=24>&nbsp;

                <c:if test="${gameToNationToStatus[game.gameId][nation.id] == true}">
                    <a href="<c:url value="/user/${gameToNationToPlayer[game.gameId][nation.id].username}"/>"
                       title="${gameToNationToPlayer[game.gameId][nation.id].username}"><img
                            src="https://secure.gravatar.com/avatar/${gameToNationToPlayer[game.gameId][nation.id].emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                            alt="${gameToNationToPlayer[game.gameId][nation.id].username}" height="24"
                            style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                            width="24"
                            class="tooltip"></a>
                </c:if>
            </li>
        </c:forEach>
    </ul>
</div>

<div style="float:left; padding: 3px;  width:95px;margin-top: -10px;">
    <h3 style="font-weight: normal; margin-left: 6px; margin-bottom: -2px;">Top-3 Navy</h3>
    <ul>
        <c:forEach items="${gameToTopNavy[game.gameId]}" var="nation">
            <li><img
            <c:choose>
            <c:when test="${gameToNationToAlive[game.gameId][nation.id] == false}">
                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36Slc.png'
            </c:when>
            <c:otherwise>
                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
            </c:otherwise>
            </c:choose>
                    alt="${nation.name}"
                    class="toolTip"
                    title="${nation.name}"
                    border=0 height=24>&nbsp;
                <c:if test="${gameToNationToStatus[game.gameId][nation.id] == true}">
                    <a href="<c:url value="/user/${gameToNationToPlayer[game.gameId][nation.id].username}"/>"
                       title="${gameToNationToPlayer[game.gameId][nation.id].username}"><img
                            src="https://secure.gravatar.com/avatar/${gameToNationToPlayer[game.gameId][nation.id].emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                            alt="${gameToNationToPlayer[game.gameId][nation.id].username}" height="24"
                            style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                            width="24"
                            class="tooltip"></a>
                </c:if>
            </li>
        </c:forEach>
    </ul>
</div>

<div class="mapContainer" style="margin-top: -30px;">
    <c:if test="${game.scenarioId != 3}">
    <div class="map" style="float:left; height: 178px;overflow: hidden;">
        <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${game.turn}-4-small.png'
             alt="Africa"
             style="float: left; border-radius: 5px;"
             border="0" height="58" title="Africa" usemap="#img1map">
        <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${game.turn}-2-small.png'
             alt="Caribbean"
             style="float: left;clear: both;margin-top: 1.5px; border-radius: 5px;"
             border="0" height="58" title="Caribbean" usemap="#img1map">
        <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${game.turn}-3-small.png'
             alt="Indies"
             style="float: left;clear: both;margin-top: 1.5px; border-radius: 5px;"
             border="0" height="58" title="Indies" usemap="#img1map">
    </div>
    </c:if>

    <div class="map" <%--style="margin-left: 80px; margin-top: -178px;"--%> style="float: right;margin-right: 5px;">
        <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${game.turn}-1-small.png'
             style="border-radius: 5px;"
             alt="Europe"
             border="0" height="178" title="Europe" usemap="#img1map">
    </div>
</div>

<div style="clear:both; margin-left: 15px; float: left; margin-top: 2px">
            <span class="inlinebar">
                <c:if test="${game.turn > 0}">
                    <c:forEach items="${activityStat[game.gameId]}" var="turn">${turn},</c:forEach>
                </c:if>
                <c:if test="${game.turn == 0}">&nbsp;</c:if>
            </span>
</div>
<div style="clear: both; float: left; margin-left: 5px;">
    last process: ${game.dateLastProc}
</div>
<div style="clear: both; float: left; margin-left: 373px; margin-top: -19px;">
    <a href='<c:url value="/scenario/${game.scenarioIdToString}/info"/>'
       style="font-size: 16px;">Scenario ${game.scenarioIdToString}</a>
</div>
</game>
</c:forEach>
</article>
</div>
<c:if test="${fn:length(newGames) + fn:length(freeNations) + fn:length(endedList)> 0}">
    <div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
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
    <div id="free-new-nations" style="z-index: 1; min-height:100px;position: relative; float: left;margin: -10px 0px 0px -35px;width: 1033px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
    repeat-y scroll 0px 0px transparent;clear: both;padding-left: 35px;">
        <script type="text/javascript">
            <c:set var="rows" value="${fn:length(newGames)/3}"/>
            <c:set var="height"><fmt:formatNumber value="${rows+(1-(rows%1))%1}" type="number" pattern="#"/></c:set>
            $('#free-new-nations').css({ 'min-height':${height*200 + 35}});
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
                                        <img
                                                src='http://static.eaw1805.com/images/nations/nation-${thisUserGame.nation.id}-36.png'
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
    <c:if test="${fn:length(freeNations) + fn:length(endedList)> 0}">
        <div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
        </div>
    </c:if>
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
        <script type="text/javascript">
            <c:set var="rows" value="${fn:length(freeNations)/3}"/>
            <c:set var="height"><fmt:formatNumber value="${rows+(1-(rows%1))%1}" type="number" pattern="#"/></c:set>
            $('#free-nations').css({ 'min-height':${height*104 + 35}});
        </script>
        <nationList style="width: 954px; margin: 0 0 5px 3px;  border: none; float: left; padding-top: 30px;">
            <ul class="nationList">
                <c:forEach items="${freeNations}" var="thisNationData">
                    <li class="nationList" style="cursor: pointer;height: 100px;width: 311px; float:left; margin:0; margin-right: 2px; margin-bottom: 2px;
border-right: 1px solid rgb(143, 143, 143);" aria-describedby="" title="Play now"
                        onclick='openPickupForm("${thisNationData[0].scenarioIdToString}", "${thisNationData[0].gameId}","${thisNationData[1].id}", "${thisNationData[1].name}", "${thisNationData[4].cost}" );'>
                        <dl class="nationList">
                            <dt class="nationList" style="width: 140px;"><a
                                    href='<c:url value="/scenario/${thisNationData[0].scenarioIdToString}/game/${thisNationData[0].gameId}/info" />'>
                                <img
                                        style="margin: 0; border: 0; border-radius: 0;"
                                        src='http://static.eaw1805.com/images/nations/nation-${thisNationData[1].id}-list.jpg'
                                        alt="Nation's Flag"
                                        class="toolTip"
                                        title="Show more info"
                                        border=0 width=180
                                        aria-describedby=""
                                        ></a></dt>
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
                                style="width: 30pt; padding-top: 10px;margin-top: -3px;margin-left: 55px;">
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
                </c:forEach>
            </ul>
        </nationList>
    </div>

    <c:if test="${fn:length(endedList)> 0}">
        <div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
        </div>
    </c:if>
</c:if>
<c:if test="${fn:length(endedList)> 0}">
<div style="z-index: 2; position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h1 class="bigmap" style="font-size: 36px; padding-left: 40px; padding-top: 20px;">Finished Games</h1>
</div>
<div id="ended-games" style="z-index: 1; min-height:100px;position: relative;margin: -10px 0px 0px -35px;width: 1033px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
    repeat-y scroll 0px 0px transparent;clear: both;
    padding-left: 35px;">
<script type="text/javascript">
    $('#ended-games').css({ 'min-height':${fn:length(endedList) * 235 + 20}});
</script>
<article style="padding-top: 20px;">
<c:forEach items="${endedList}" var="game">
<game>
<h1 style="width: 500px; font-size: 36px !important;">
    <a href="<c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/info"/>">Game #${game.gameId}</a>
    / ${dates[game.gameId]}
</h1>
<c:if test="${game.userId != 2}">

    <div style="position: relative; width: 50px; margin-left: 498px; margin-top: -46px;">
        <ul>
            <li style="width: 111px; height: 38px;">
                <div style="position: relative; width: 66px; top: -7px;">
                    <h3>Game<br>Owned By</h3>
                </div>
                <a href="<c:url value="/user/${userIdToUser[game.userId].username}"/>"
                   title="${userIdToUser[game.userId].username}">
                    <img src="https://secure.gravatar.com/avatar/${userIdToUser[game.userId].emailEncoded}?s=36&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                         class="avatarLink"
                         width="36"
                         style="position: relative; left: 69px; top: -32px; border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                         title="${userIdToUser[game.userId].username}"/>
                </a>
            </li>
        </ul>
    </div>
</c:if>
<c:set var="columns" value="1"/>
<div style="float:left; padding: 3px;  width:95px;margin-top: -10px;">
    <h3 style="font-weight: normal; margin-left: 6px; margin-bottom: -2px;">Winners</h3>
    <ul>
        <c:forEach items="${gameToWinner[game.gameId]}" var="nation">
            <li><img
            <c:choose>
            <c:when test="${gameToNationToAlive[game.gameId][nation.id] == false}">
                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36Slc.png'
            </c:when>
            <c:otherwise>
                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
            </c:otherwise>
            </c:choose>
                    alt="${nation.name}"
                    class="toolTip"
                    title="${nation.name}"
                    border=0 height=24>&nbsp;
                <c:if test="${gameToNationToStatus[game.gameId][nation.id] == true}">
                    <a href="<c:url value="/user/${gameToNationToPlayer[game.gameId][nation.id].username}"/>"
                       title="${gameToNationToPlayer[game.gameId][nation.id].username}"><img
                            src="https://secure.gravatar.com/avatar/${gameToNationToPlayer[game.gameId][nation.id].emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                            alt="${gameToNationToPlayer[game.gameId][nation.id].username}" height="24"
                            style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                            width="24"
                            class="tooltip"></a>
                </c:if>
            </li>
        </c:forEach>
    </ul>
</div>

<c:if test="${fn:length(gameToCoWinner[game.gameId]) > 0}">
    <c:set var="columns" value="${columns + 1}"/>
    <div style="float:left; padding: 3px;  width:95px;margin-top: -10px;">
        <h3 style="font-weight: normal; margin-left: 6px; margin-bottom: -2px;">Co-Winners</h3>
        <ul>
            <c:forEach items="${gameToCoWinner[game.gameId]}" var="nation">
                <li><img
                <c:choose>
                <c:when test="${gameToNationToAlive[game.gameId][nation.id] == false}">
                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36Slc.png'
                </c:when>
                <c:otherwise>
                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
                </c:otherwise>
                </c:choose>
                        alt="${nation.name}"
                        class="toolTip"
                        title="${nation.name}"
                        border=0 height=24>&nbsp;
                    <c:if test="${gameToNationToStatus[game.gameId][nation.id] == true}">
                        <a href="<c:url value="/user/${gameToNationToPlayer[game.gameId][nation.id].username}"/>"
                           title="${gameToNationToPlayer[game.gameId][nation.id].username}"><img
                                src="https://secure.gravatar.com/avatar/${gameToNationToPlayer[game.gameId][nation.id].emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                alt="${gameToNationToPlayer[game.gameId][nation.id].username}" height="24"
                                style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                width="24"
                                class="tooltip"></a>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<c:if test="${fn:length(gameToRunnerUp[game.gameId]) > 0}">
    <c:set var="columns" value="${columns + 1}"/>
    <div style="float:left; padding: 3px;  width:95px;margin-top: -10px;">
        <h3 style="font-weight: normal; margin-left: 6px; margin-bottom: -2px;">Runners-Up</h3>
        <ul>
            <c:forEach items="${gameToRunnerUp[game.gameId]}" var="nation">
                <li><img
                <c:choose>
                <c:when test="${gameToNationToAlive[game.gameId][nation.id] == false}">
                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36Slc.png'
                </c:when>
                <c:otherwise>
                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
                </c:otherwise>
                </c:choose>
                        alt="${nation.name}"
                        class="toolTip"
                        title="${nation.name}"
                        border=0 height=24>&nbsp;
                    <c:if test="${gameToNationToStatus[game.gameId][nation.id] == true}">
                        <a href="<c:url value="/user/${gameToNationToPlayer[game.gameId][nation.id].username}"/>"
                           title="${gameToNationToPlayer[game.gameId][nation.id].username}"><img
                                src="https://secure.gravatar.com/avatar/${gameToNationToPlayer[game.gameId][nation.id].emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                alt="${gameToNationToPlayer[game.gameId][nation.id].username}" height="24"
                                style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                width="24"
                                class="tooltip"></a>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:if>


<c:if test="${columns < 6}">
    <c:set var="columns" value="${columns + 1}"/>
    <div style="float:left;  padding: 3px;  width:95px;margin-top: -10px;">
        <h3 style="font-weight: normal; margin-left: 6px; margin-bottom: -2px;">Top-3 Land</h3>
        <ul>
            <c:forEach items="${gameToTopSize[game.gameId]}" var="nation">
                <li><img
                <c:choose>
                <c:when test="${gameToNationToAlive[game.gameId][nation.id] == false}">
                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36Slc.png'
                </c:when>
                <c:otherwise>
                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
                </c:otherwise>
                </c:choose>
                        alt="${nation.name}"
                        class="toolTip"
                        title="${nation.name}"
                        border=0 height=24>&nbsp;
                    <c:if test="${gameToNationToStatus[game.gameId][nation.id] == true}">
                        <a href="<c:url value="/user/${gameToNationToPlayer[game.gameId][nation.id].username}"/>"
                           title="${gameToNationToPlayer[game.gameId][nation.id].username}"><img
                                src="https://secure.gravatar.com/avatar/${gameToNationToPlayer[game.gameId][nation.id].emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                alt="${gameToNationToPlayer[game.gameId][nation.id].username}" height="24"
                                style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                width="24"
                                class="tooltip"></a>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<c:if test="${columns < 6}">
    <c:set var="columns" value="${columns + 1}"/>
    <div style="float:left; padding: 3px;  width:95px;margin-top: -10px;">
        <h3 style="font-weight: normal; margin-left: 6px; margin-bottom: -2px;">Top-3 Money</h3>
        <ul>
            <c:forEach items="${gameToTopMoney[game.gameId]}" var="nation">
                <li><img
                <c:choose>
                <c:when test="${gameToNationToAlive[game.gameId][nation.id] == false}">
                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36Slc.png'
                </c:when>
                <c:otherwise>
                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
                </c:otherwise>
                </c:choose>
                        alt="${nation.name}"
                        class="toolTip"
                        title="${nation.name}"
                        border=0 height=24>&nbsp;
                    <c:if test="${gameToNationToStatus[game.gameId][nation.id] == true}">
                        <a href="<c:url value="/user/${gameToNationToPlayer[game.gameId][nation.id].username}"/>"
                           title="${gameToNationToPlayer[game.gameId][nation.id].username}"><img
                                src="https://secure.gravatar.com/avatar/${gameToNationToPlayer[game.gameId][nation.id].emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                alt="${gameToNationToPlayer[game.gameId][nation.id].username}" height="24"
                                style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                width="24"
                                class="tooltip"></a>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<c:if test="${columns < 6}">
    <c:set var="columns" value="${columns + 1}"/>
    <div style="float:left; padding: 3px;  width:95px;margin-top: -10px;">
        <h3 style="font-weight: normal; margin-left: 6px; margin-bottom: -2px;">Top-3 Army</h3>
        <ul>
            <c:forEach items="${gameToTopArmy[game.gameId]}" var="nation">
                <li><img
                <c:choose>
                <c:when test="${gameToNationToAlive[game.gameId][nation.id] == false}">
                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36Slc.png'
                </c:when>
                <c:otherwise>
                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
                </c:otherwise>
                </c:choose>
                        alt="${nation.name}"
                        class="toolTip"
                        title="${nation.name}"
                        border=0 height=24>&nbsp;

                    <c:if test="${gameToNationToStatus[game.gameId][nation.id] == true}">
                        <a href="<c:url value="/user/${gameToNationToPlayer[game.gameId][nation.id].username}"/>"
                           title="${gameToNationToPlayer[game.gameId][nation.id].username}"><img
                                src="https://secure.gravatar.com/avatar/${gameToNationToPlayer[game.gameId][nation.id].emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                alt="${gameToNationToPlayer[game.gameId][nation.id].username}" height="24"
                                style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                width="24"
                                class="tooltip"></a>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<c:if test="${columns < 6}">
    <c:set var="columns" value="${columns + 1}"/>
    <div style="float:left; padding: 3px;  width:95px;margin-top: -10px;">
        <h3 style="font-weight: normal; margin-left: 6px; margin-bottom: -2px;">Top-3 Navy</h3>
        <ul>
            <c:forEach items="${gameToTopNavy[game.gameId]}" var="nation">
                <li><img
                <c:choose>
                <c:when test="${gameToNationToAlive[game.gameId][nation.id] == false}">
                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36Slc.png'
                </c:when>
                <c:otherwise>
                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
                </c:otherwise>
                </c:choose>
                        alt="${nation.name}"
                        class="toolTip"
                        title="${nation.name}"
                        border=0 height=24>&nbsp;
                    <c:if test="${gameToNationToStatus[game.gameId][nation.id] == true}">
                        <a href="<c:url value="/user/${gameToNationToPlayer[game.gameId][nation.id].username}"/>"
                           title="${gameToNationToPlayer[game.gameId][nation.id].username}"><img
                                src="https://secure.gravatar.com/avatar/${gameToNationToPlayer[game.gameId][nation.id].emailEncoded}?s=24&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                alt="${gameToNationToPlayer[game.gameId][nation.id].username}" height="24"
                                style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                width="24"
                                class="tooltip"></a>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<div class="mapContainer" style="margin-top: -30px;">
    <div class="map" style="float:left; height: 178px;overflow: hidden;">
        <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${game.turn}-4-small.png'
             alt="Africa"
             style="float: left; border-radius: 5px;"
             border="0" height="58" title="Africa" usemap="#img1map">
        <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${game.turn}-2-small.png'
             alt="Caribbean"
             style="float: left;clear: both;margin-top: 1.5px; border-radius: 5px;"
             border="0" height="58" title="Caribbean" usemap="#img1map">
        <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${game.turn}-3-small.png'
             alt="Indies"
             style="float: left;clear: both;margin-top: 1.5px; border-radius: 5px;"
             border="0" height="58" title="Indies" usemap="#img1map">
    </div>

    <div class="map" <%--style="margin-left: 80px; margin-top: -178px;"--%>
         style="float: right;margin-right: 5px;">
        <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${game.turn}-1-small.png'
             style="border-radius: 5px;"
             alt="Europe"
             border="0" height="178" title="Europe" usemap="#img1map">
    </div>
</div>

<div style="clear:both; margin-left: 15px; float: left; margin-top: 2px">
            <span class="inlinebar">
                <c:if test="${game.turn > 0}">
                    <c:forEach items="${activityStat[game.gameId]}" var="turn">${turn},</c:forEach>
                </c:if>
                <c:if test="${game.turn == 0}">&nbsp;</c:if>
            </span>
</div>
<div style="clear: both; float: left; margin-left: 5px;">
    last process: ${game.dateLastProc}
</div>
<div style="clear: both; float: left; margin-left: 373px; margin-top: -19px;">
    <a href='<c:url value="/scenario/${game.scenarioIdToString}/info"/>'
       style="font-size: 16px;">Scenario ${game.scenarioIdToString}</a>
</div>
</game>
</c:forEach>
</article>
</div>
</c:if>


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

<div id="error_message_nec" style="display:none; cursor: default">
    <h3>Not enough credits</h3>

    <p>You do not have enough credits.<br>
        click <a href="">here</a> to buy more credits.
    </p><br>
    <input type="button" id="close_error_nec" onclick="$.unblockUI();" value="Ok"/>
    <br><br>
</div>

<div id="error_message_hoag" style="display:none; cursor: default">
    <h3>Cannot pick up position</h3>

    <p>Dear ${user.username}, you cannot pick up another empire in this game, because you've already played one at an
        earlier time.</p>
    <br>
    <input type="button" id="close_error_hoag" onclick="$.unblockUI();" value="Ok"/>
    <br><br>
</div>

<div id="status_joined" style="display:none; cursor: default">
    <h3>Joined new Game!!</h3>

    <p>You have joined game ${param.g} with nation ${param.n}.<br>
    </p>
    <br>
    <input type="button" id="close_status" onclick="$.unblockUI();" value="Ok"/>
    <br><br>
</div>
