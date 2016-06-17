<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<jsp:useBean id="nations" scope="request" class="java.util.List<com.eaw1805.data.model.Nation>"/>
<jsp:useBean id="allUsers" scope="request" class="java.util.List<com.eaw1805.data.model.User>"/>
<script src="http://js.nicedit.com/nicEdit-latest.js" type="text/javascript"></script>
<script type="text/javascript">
    //init autocommplete functionality.
    $(function () {
        var allUsers = new Array();
        <c:forEach items="${allUsers}" var="curUser" varStatus="status">
        allUsers[${status.index}] = '${curUser.username}';
        </c:forEach>
        $('.user_position').each(function () {
            var el = this;
            var a2 = $(this).autocomplete({
                delimiter: /(,|;)\s*/,
                lookup: allUsers
            });
            a2.setOptions({zIndex: 1000000});
        });
    });

    var forceSubmit = false;

    var userToHash = new Object();
    <c:forEach items="${allUsers}" var="curUser">
    userToHash["${curUser.username}"] = "${curUser.emailEncoded}";
    </c:forEach>

    function getObjectSize(myObject) {
        var count = 0
        for (var key in myObject)
            count++
        return count
    }

    function updateNationUser(txtEl) {
        var jEl = $(txtEl);

        if (jEl.val() in userToHash) {
            var nId = jEl.attr("id").substr(7);
            jEl.hide();
            $("#avatar_" + nId).attr("src", "https://secure.gravatar.com/avatar/" + userToHash[jEl.val()] + "?s=33&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png");
            $("#avatar_" + nId).show();
            $("#usernameGoesHere_" + nId).html(jEl.val());
            $(".autocomplete").hide();
        }
//            wait for it
    }

    function clearNationUser(imgEl) {
        var jEl = $(imgEl);
        var nId = jEl.attr("id").substr(7);
        jEl.hide();
        $("#player_" + nId).val("");
        $("#player_" + nId).show();
        $("#usernameGoesHere_" + nId).html("");
    }

    function validateSchedule() {
        var monday = document.getElementById("ps_monday").checked;
        var tuesday = document.getElementById("ps_tuesday").checked;
        var wednesday = document.getElementById("ps_wednesday").checked;
        var thursday = document.getElementById("ps_thursday").checked;
        var friday = document.getElementById("ps_friday").checked;
        var saturday = document.getElementById("ps_saturday").checked;
        var sunday = document.getElementById("ps_sunday").checked;

        var weekly = document.getElementById("ps_weekly").checked;
        var every2Weeks = document.getElementById("ps_every2Weeks").checked;

        var count = 0;
        if (monday) {
            count++;
        }
        if (tuesday) {
            count++;
        }
        if (wednesday) {
            count++;
        }
        if (thursday) {
            count++;
        }
        if (friday) {
            count++;
        }
        if (saturday) {
            count++;
        }
        if (sunday) {
            count++;
        }

        if (every2Weeks && count > 1) {
            alert("When you choose to process every 2 weeks you can choose only 1 day");
            return false;
        }
        if (count == 0) {
            alert("Please choose when you want the game to be processed");
            return false;
        }
        if (!every2Weeks && !weekly) {
            alert("Please choose the process schedule")
            return false;
        }
        openPickupForm(1, 1, 1, 1, 1);
        return true;
    }

    function setTurnAroundWeek(el) {
        var jEl = $(el);
        if (jEl.attr("id") == "ps_weekly") {
            if (el.checked) {
                document.getElementById("ps_every2Weeks").checked = false;
            } else {
                document.getElementById("ps_every2Weeks").checked = true;
            }
        } else {
            if (el.checked) {
                document.getElementById("ps_weekly").checked = false;
            } else {
                document.getElementById("ps_weekly").checked = true;
            }
        }
    }

    function openPickupForm(scenario, game, nation, nationName, cost) {
        <c:url var="thisUrl" value="/"/>
        var url = "${thisUrl}" + "scenario/" + scenario + "/game/" + game + "/pickup/" + nation;
        $('#joinForm').attr('action', url);
        $('#form_nationId').text(nation);
        $('#nationImgFile').attr('src', "http://static.eaw1805.com/images/nations/nation-" + nation + "-list.jpg");
        $('#nationImgFile').attr('title', nationName);
        $('#cost').text("150");
        $('#joinTitle').text("Create Custom Game");
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

    function showAllNations() {
        for (i = 1; i <= 17; i++) {
            $("#nationItem_" + i).show();
        }
    }

    function hideNations1808() {
        for (i = 1; i < 4; i++) {
            $("#nationItem_" + i).hide();
        }
        for (i = 7; i <= 17; i++) {
            $("#nationItem_" + i).hide();
        }
    }

</script>
<style type="text/css">

    div.customgame input[type=submit], input[type=select] {
        background-color: transparent !important;
        float: right !important;
        width: 300px !important;
        height: 72px !important;
        background-image: url("http://static.eaw1805.com/images/site/gameButtons/CreateCustomGameOff.png") !important;
        background-size: auto 72px !important;
        background-repeat: no-repeat !important;
        display: inline-block !important;
        padding: 0 !important;
        position: relative !important;
        top: 0 !important;
        margin: 0 !important;
        text-shadow: 1px 1px 0 #fff;
        white-space: nowrap;
        border: 0 !important;
        border-bottom-color: transparent !important;
        overflow: visible;
        filter: progid:DXImageTransform.Microsoft.gradient(GradientType=0, startColorstr='#ff9e01', endColorstr='#fdffcd');
        -webkit-border-radius: 0 !important;
        -moz-border-radius: 0 !important;
        border-radius: 0 !important;
        -webkit-box-shadow: none !important;
        -moz-box-shadow: none !important;
        box-shadow: none !important;
        cursor: pointer;
        -webkit-font-smoothing: subpixel-antialiased !important;
    }

    div.customgame input[type=submit]:hover {
        background-color: transparent !important;
        float: right !important;
        width: 300px !important;
        height: 72px !important;
        background-image: url("http://static.eaw1805.com/images/site/gameButtons/CreateCustomGameHover.png") !important;
        background-size: auto 72px !important;
        background-repeat: no-repeat !important;
        display: inline-block !important;
        padding: 0 !important;
        position: relative !important;
        top: 0 !important;
        margin: 0 !important;
        text-shadow: 1px 1px 0 #fff;
        white-space: nowrap;
        border: 0 !important;
        border-bottom-color: transparent !important;
        overflow: visible;
        filter: progid:DXImageTransform.Microsoft.gradient(GradientType=0, startColorstr='#ff9e01', endColorstr='#fdffcd');
        -webkit-border-radius: 0 !important;
        -moz-border-radius: 0 !important;
        border-radius: 0 !important;
        -webkit-box-shadow: none !important;
        -moz-box-shadow: none !important;
        box-shadow: none !important;
        cursor: pointer;
        -webkit-font-smoothing: subpixel-antialiased !important;
    }
</style>
<article>
    <h1>Create Custom Game</h1>

    <form:form id="createCustomForm" name="createCustom" commandName="game" method="POST"
               onsubmit="if (!forceSubmit) {validateSchedule();return false;}" action='createCustom'>

        <div style="float: left; width: 470px;">
            <section style="width: 470px; height:525px; float:left;">
                <h1 class="manual">Scenario Settings</h1>

                <label class="manual" for="description" style="width: 9em;">Name:</label>

                <div class="manual">
                    <form:input class="customInput" path="description" id="description"/>
                    <spring:bind path="description">
                        <c:if test="${status.error}">
                            <img style="float: left;margin-bottom: -5px;"
                                 src="http://static.eaw1805.com/images/site/error.jpeg"
                                 width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
                        </c:if>
                    </spring:bind>
                </div>

                <label class="manual" for="scenarioId" style="width: 9em;">Base Scenario:</label>

                <div class="manual">
                    <form:select path="scenarioId" id="scenarioId">
                        <form:option value="1" onClick="showAllNations();">1802</form:option>
                        <form:option value="2" onClick="showAllNations();">1805</form:option>
                        <form:option value="3" onClick="hideNations1808();">1808</form:option>
                    </form:select>
                </div>

                <label class="manual" for="type" style="width: 9em;">Type:</label>

                <div class="manual">
                    <form:select path="type" id="type">
                        <form:option value="-1">SHORT</form:option>
                        <form:option value="0">NORMAL</form:option>
                        <form:option value="1">EPIC</form:option>
                    </form:select>
                </div>

                <table>
                    <tr>
                        <td>
                            <label class="manual" for="fieldBattle" style="width: 17em;">Field battles:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="fieldBattle" id="fieldBattle"/>
                            </div>

                        </td>
                    </tr>

                    <tr>
                        <td>
                            <label class="manual" for="randomEvents" style="width: 17em;">Random Events:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="randomEvents" id="randomEvents"/>
                            </div>

                        </td>
                    </tr>


                    <tr>
                        <td>
                            <label class="manual" for="fogOfWar" style="width: 17em;">Fog of war:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="fogOfWar" id="fogOfWar"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="boostedTaxation" style="width: 17em;">Boosted Taxation:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="boostedTaxation" id="boostedTaxation"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="boostedProduction" style="width: 17em;">Boosted
                                Production:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="boostedProduction" id="boostedProduction"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="fastPopulationGrowth" style="width: 17em;">Fast Population
                                Growth:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="fastPopulationGrowth" id="fastPopulationGrowth"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="boostedCAPoints" style="width: 17em;">Boosted A&C Points:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="boostedCAPoints" id="boostedCAPoints"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="fierceCasualties" style="width: 17em;">Fierce Casualties:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="fierceCasualties" id="fierceCasualties"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="fastAppointmentOfCommanders" style="width: 17em;">Fast
                                Appointment Of Commanders:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="fastAppointmentOfCommanders" id="fastAppointmentOfCommanders"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="extendedArrivalOfCommanders" style="width: 17em;">Extended
                                Arrival Of Commanders:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="extendedArrivalOfCommanders" id="extendedArrivalOfCommanders"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="fullMpsAtColonies" style="width: 17em;">Full Mps At
                                Colonies:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="fullMpsAtColonies" id="fullMpsAtColonies"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="alwaysSummerWeather" style="width: 17em;">Always Summer (no
                                storms):</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="alwaysSummerWeather" id="alwaysSummerWeather"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="fastShipConstruction" style="width: 17em;">Fast Ship
                                Construction:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="fastShipConstruction" id="fastShipConstruction"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="extendedEspionage" style="width: 17em;">Extended
                                Espionage:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="extendedEspionage" id="extendedEspionage"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="fastFortressConstruction" style="width: 17em;">Fast Fortress
                                Construction:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="fastFortressConstruction" id="fastFortressConstruction"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="rumorsEnabled" style="width: 17em;">Rumors Enabled:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="rumorsEnabled" id="rumorsEnabled"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="doubleCostsArmy" style="width: 17em;">Double Costs/Maintenance
                                for Land Forces:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="doubleCostsArmy" id="doubleCostsArmy"/>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="manual" for="doubleCostsNavy" style="width: 17em;">Double Costs/Maintenance
                                for Naval Forces:</label>
                        </td>
                        <td>
                            <div>
                                <form:checkbox path="doubleCostsNavy" id="doubleCostsNavyru1"/>
                            </div>

                        </td>
                    </tr>
                </table>
            </section>

            <section style="width: 470px; height:90px; float: left;">
                <h1 class="manual">Turnaround / Process Schedule</h1>
                <label class="manual" style="width: 9em;">Process Schedule:</label>

                <div class="manual">
                    Mon: <input type="checkbox" name="ps_monday" id="ps_monday" title="monday"/>
                    Tue: <input type="checkbox" name="ps_tuesday" id="ps_tuesday" title="tuesday"/>
                    Wed: <input type="checkbox" name="ps_wednesday" id="ps_wednesday" title="wednesday"/>
                    Thu: <input type="checkbox" name="ps_thursday" id="ps_thursday" title="thursday"/>
                    Fri: <input type="checkbox" name="ps_friday" id="ps_friday" title="friday"/>
                    Sat: <input type="checkbox" name="ps_saturday" id="ps_saturday" title="saturday"/>
                    Sun: <input type="checkbox" name="ps_sunday" id="ps_sunday" title="sunday"/>

                    <div class="manual" style="margin-left: 9em;">
                        Weekly: <input onchange="setTurnAroundWeek(this);" type="checkbox" name="ps_weekly"
                                       id="ps_weekly"
                                       title="weekly"/>
                        Every 2 weeks: <input onchange="setTurnAroundWeek(this);" type="checkbox" name="ps_every2Weeks"
                                              id="ps_every2Weeks" title="every 2 weeks"/>
                    </div>
                </div>
            </section>

            <section style="width: 470px; height:140px; float: left;">
                <h1 class="manual">Join Rules</h1>
                <label class="manual" for="privateGame" style="width: 9em;">Private:</label>

                <div>
                    <form:checkbox path="privateGame" id="privateGame"/>
                </div>
                <br>
                <label class="manual" for="vpsToJoin" style="width: 9em;">VPs to join:</label>

                <div class="manual">
                    <form:input class="customInput" path="vpsToJoin" id="vpsToJoin"/>
                    <spring:bind path="vpsToJoin">
                        <c:if test="${status.error}">
                            <img style="float: left;margin-bottom: -5px;"
                                 src="http://static.eaw1805.com/images/site/error.jpeg"
                                 width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
                        </c:if>
                    </spring:bind>
                </div>

                <label class="manual" for="bet" style="width: 9em;">Honour Bet:</label>

                <div>
                    <form:input class="customInput" path="bet" id="bet"/>
                </div>

                <label class="manual" for="betSharing" style="width: 9em;">Honour Sharing:</label>

                <div class="manual">
                    <form:select path="betSharing" id="betSharing">
                        <form:option value="1">Winners</form:option>
                        <form:option value="2">Winners and Runner-ups</form:option>
                        <form:option value="3">Survivors</form:option>
                    </form:select>
                </div>
            </section>

            <infomap style="width: 470px; float: left;">
                <ul class="nationList">
                    <li class="header">
                        <dl class="nationList">
                            <dt class="nationList">
                            <h2 class="bigmap">Nations</h2></dt>
                            <ds class="nationList">&nbsp;&nbsp;</ds>
                            <dd class="nationList" style="width: 50pt;"><h3 class="bigmap"
                                                                            style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                                Players</h3></dd>
                            <ds class="nationList">&nbsp;&nbsp;</ds>
                        </dl>
                    </li>
                    <c:forEach items="${nations}" var="nation">
                        <li class="nationList" style="width: 430px;" id="nationItem_${nation.id}">
                            <dl class="nationList">
                                <dt class="nationList"><a
                                        href='<c:url value="/scenario/1802/nation/${nation.id}"/>'
                                        title="Nation Info Page"
                                        style="float:right;line-height: 1.3!important; border-radius: 0px;"><img
                                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-list.jpg'
                                        alt="Nation's Flag"
                                        class="toolTip"
                                        title="${nation.name}"
                                        border=0 width=231></a></dt>

                                <dk class="nationList" style="float:left;">
                                    <input type="text" class="user_position" id="player_${nation.id}"
                                           name="player_${nation.id}"
                                           style="width: 140px;"
                                           onkeyup="updateNationUser(this);"/>
                                    <img
                                            src="https://secure.gravatar.com/avatar/${nationToPlayer[nation.id].emailEncoded}?s=33&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                            id="avatar_${nation.id}"
                                            style="display: none;"
                                            alt="${nationToPlayer[nation.id].username}" height="33"
                                            width="33"
                                            title="Click to select another user"
                                            class="tooltip" onclick="clearNationUser(this);">
                                    <span id="usernameGoesHere_${nation.id}"></span>
                                </dk>
                            </dl>
                        </li>
                    </c:forEach>
                </ul>
            </infomap>
        </div>
        <div style="float: right; width: 470px;">
            <section style="width: 470px; height:1420px; float: right;">
                <h3 class="manual" style="margin-top: 0; padding-top: 0;">Scenario Settings</h3>

                <p class="manual"><strong>Name:</strong> Write down a name for your game</p>

                <p class="manual"><strong>Base Scenario:</strong> Chose a scenario between those available</p>

                <p class="manual"><strong>Type:</strong> There are three types of games available:</p>

                <ul class="manual">
                    <li class="manual">
                        <strong>NORMAL</strong> games require the already known from the scenario description amount of
                        Victory
                        points to win.
                    </li>
                    <li class="manual">
                        <strong>SHORT</strong> games finish earlier, because they require 30% less VPs from Normal Game
                        to win.
                        However, your
                        Hall of Fame earnings when the game ends are also reduced by 30%.
                    </li>
                    <li class="manual">
                        <strong>EPIC</strong> games are longer, and require an additional 30% VPs to win from those
                        mentioned in
                        Normal Game.
                        However, your Hall of Fame earnings when the game ends are also increased by 30%.
                    </li>
                </ul>

                <p class="manual"><strong>Other scenario settings:</strong> Tick on the empty box if you want your game
                    to have
                    Field Battles,
                    Random events, or Fog of War. (You may have one, two, all, or none of the features included).</p>

                <h3 class="manual">Turnaround</h3>

                <p class="manual"><strong>Process Schedule:</strong> You may choose whether turnaround will be weekly or
                    every
                    two weeks, as well as on which exact day of the week you want processing to take place. <i>So if for
                        example
                        you
                        tick on <i>Tuesday</i> it means that every Tuesday (or every second Tuesday) around 12:01 am CET
                        (depending on game queue) your game will be processed, therefore the last day you are able to
                        input
                        a turn in is Monday.</i></p>

                <h3 class="manual">Join Rules</h3>

                <p class="manual"><strong>Private:</strong> You can make your game private by ticking this box,
                    therefore only
                    players whose
                    login name you type into specific countries will be allowed to join.</p>

                <p class="manual"><strong>Honour to Join:</strong> Then you can place a minimum "Honour" required for
                    players to
                    join your game.
                    <i>If for example you enter in the box “500” it means that only players with a Honour of 500 or more
                        in the
                        Hall of Fame will be allowed to join.</i></p>

                <p class="manual"><strong>Honour Bet:</strong> Here you can place "bets". Players will need to "expend"
                    this
                    amount
                    of Honour
                    to join your game. <i>(note : This honour is expended after the “minimum Honour needed” above is
                        taken into
                        account). Players joining immediately lose the requested Honour, which is placed in the Game
                        Honour Pool
                        and
                        will be distributed to survivors or game winners when the game ends. (example: if you place
                        "100" on
                        this
                        spot,
                        every player joining will “lose” 100 honour which will be added to the game honour pool;
                        therefore 1700 Honour should be there when the game starts)</i>. If you don’t want to use this
                    feature,
                    simply leave the box at 0.</p>

                <p class="manual"><strong>Honour Sharing:</strong> When game ends, in addition to any other honour
                    earned, some
                    players will
                    also share the "Game Honour Pool". It is in this section where you decide who will share this loot:
                    Winners only? Winners and Runner-ups? Or everyone surviving at game end? <i>Please note that sharing
                        is
                        equal.
                        So if for example you decide that all surviving countries will share the Game Honour pool which
                        for
                        example
                        has 1700 honour, and there are 10 surviving countries, each player running a surviving country
                        will get
                        170
                        additional honour.</i></p>

                <h3 class="manual">Nations</h3>

                <p class="manual">You decide who gets what, or simply chose your country and leave the rest open. It is
                    the final stage before you press the “Create Game” button.</p>

                <h3 class="manual">Description</h3>

                <p class="manual">Write a description of the game in this box, it would be historical, it could be
                    explanatory
                    or
                    anything else you want, and you can include pictures. This game description will appear and stay on
                    game
                    page
                    even after the game ends!</p>

                <h3 class="manual">Additional Notes</h3>
                <ol class="manual">
                    <li class="manual">
                        Every Custom Game requires 150 Eaw credits to be created.
                    </li>
                    <li class="manual">
                        Once Created, you will also be given the option to LAUNCH it, or to CANCEL it. You may launch it
                        at any
                        time, even without a full complement of players (who may join later via the picking up free
                        countries
                        mechanism). You may also CANCEL the game, in which case every player is refunded their
                        Honour/Credits.
                    </li>
                    <li class="manual">
                        Once LAUNCHED, no additional changes can be made, Honour and Credits are expended, and the game
                        begins.
                    </li>
                </ol>
            </section>
        </div>

        <section class="section-post-create">
            <h1 class="manual">Description</h1>

            <div>
                <div style="height: 100%; width: 100%; border-radius: 8px 8px 8px 8px;overflow: hidden;">
                    <form:textarea path="name" id="name" style="width:100%; height: 205px;"/>
                </div>
            </div>
            <script type="text/javascript">
                new nicEditor({fullPanel: true}).panelInstance('name', {hasPanel: true});
                function updateHTMLContent() {
                    nicEditors.findEditor("name").saveContent();
                }
            </script>
        </section>
        <div class="customgame" style="height:80px; width:320px;margin-top: 5px; float: left;">
            <input type="submit" onclick="updateHTMLContent();" value=""/>
        </div>
    </form:form>
</article>

<div id="question" style="display:none; position: relative; cursor: default">
    <h3 id="joinTitle" style="margin-top: 22px;width: 353px;margin-left: 20px;float: left;">Create Game</h3>

    <div>
        <h3 style="margin-top:20px;margin-left: 70px; float: left; width: 150px;">Costs &nbsp;
            <div id="cost" style="float:right;width: 20px;margin-right: 19px;"> 150</div>
        </h3>
        <img style="margin-top:25px;float:left;" src='http://static.eaw1805.com/images/goods/good-1.png'
             alt="Credits"
             class="toolTip"
             title="Credits"
             border=0 height=20>
    </div>

    <button class="ok" id="ok" onfocus=" this.blur();" onclick="forceSubmit=true;$('#createCustomForm').submit();"
            style="top: 149px; position: absolute; left: 147px;"
            title="Pick up position "></button>
    <button class="cancel" title="Cancel" id="cancel" value="Cancel"
            style="top: 149px; position: absolute; left: 203px;"
            onclick="$.unblockUI();$('#'+$('#cancel').attr('aria-describedby')).hide();return false;"
            aria-describedby=""></button>

    <div style="position: absolute; left: 0px; top: 110px;">
        <img style="float:left;margin-left: 20px; margin-top: 44px;"
             src='http://static.eaw1805.com/images/buttons/taxation/MUINormalTaxSlc.png'
             alt="Credits"
             class="toolTip"
             title="Account Balance"
             border=0 height=32>

        <div style="float: left;font-size: 20px; margin: 2px;margin-top: 47px; margin-left: 5px;">${user.creditFree+user.creditTransferred+user.creditBought}</div>
    </div>
</div>
