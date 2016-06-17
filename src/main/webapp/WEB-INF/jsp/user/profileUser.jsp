<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:useBean id="profileUser" scope="request" type="com.eaw1805.data.model.User"/>
<jsp:useBean id="user" scope="request" type="com.eaw1805.data.model.User"/>
<jsp:useBean id="userGames" scope="request" type="java.util.Set<com.eaw1805.data.model.UserGame>"/>
<jsp:useBean id="userGameVps" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
<jsp:useBean id="userGameStats" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.lang.String>>"/>
<jsp:useBean id="history" scope="request" type="java.util.List<com.eaw1805.data.model.PaymentHistory>"/>
<jsp:useBean id="recentAchievements" scope="request" type="java.util.List<com.eaw1805.data.model.Achievement>"/>

<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.sparkline.min.js'></script>

<script type="text/javascript">
    $.blockUI.defaults.applyPlatformOpacityRules = false;

    $(function () {
        /** This code runs when everything has been loaded on the page */
        /* Use 'html' instead of an array of values to pass options
         to a sparkline with data in the tag */
        $('.inlinebar').sparkline('html', {type: 'bar', barColor: '#0000ff'});
        $('.inlinebar-player').sparkline('html', {type: 'bar', barColor: '#ff0000'});
    });

    //    $(document).ready(function () {
    //        $(function () {
    //            $('#main-article').css({ 'min-height':($('#main-footer').position().top - $('#main-article').position().top)});
    //        });
    //    });
</script>


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

</style>

<div id="main-article"
     style="position: relative;margin: 0px 0px 0px -35px;width: 1033px;background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px -200px transparent;clear: both; overflow: hidden;">
    <article id="main-article-top">

        <c:if test="${profileUser.profileHtml != null && profileUser.profileHtml != ''}">
            <section class="section-post-create">
                <h1 style="width: 440px;float: left; font-size: 27px !important;">Personal message</h1><br><br><br>
                    ${profileUser.profileHtml}
            </section>
        </c:if>

        <section class="achievements" id="doubleSize">
            <h1 style="float: left;">Recent Achievements</h1>

            <div style="float:right;">
                <a href="<c:url value="/user/${profileUser.username}/honour"/>"
                   class="minibutton"
                   title="Examine ${profileUser.username} honour achieved">
                    <img src="http://static.eaw1805.com/images/layout/buttons/ButSpyReportsOff.png"
                         alt="Examine ${profileUser.username} honour achieved"
                         onmouseover="this.src='http://static.eaw1805.com/images/layout/buttons/ButSpyReportsHover.png'"
                         onmouseout="this.src='http://static.eaw1805.com/images/layout/buttons/ButSpyReportsOff.png'"
                         style="border: 0;"
                         border=0></a>
            </div>

            <c:forEach items="${recentAchievements}" var="achievement">
                <div class="flagEntry" style=" float:left; border: 0pt; padding: 0pt; padding-top: 24px;">
                    <img src='http://static.eaw1805.com/images/achievements/ach-${achievement.category}-${achievement.level}.png'
                         alt="${achievement.description}"
                         title="${achievement.description}"
                         style="border: 0; padding: 0; margin: 0;"
                         height=116>
                </div>
            </c:forEach>
        </section>

        <section class="doubleSize" id="doubleSize">
            <h1>Compose Message</h1>
            <form:form commandName="message" method="POST" action='inbox'>
                <div style="clear: both;">
                    <label class="inputCompose" for="recipient">To:</label>
                    <spring:bind path="receiver">
                        <c:if test="${status.error}">
                            <img style="float: left;margin-bottom: -5px;margin-left: -27px;"
                                 src="http://static.eaw1805.com/images/site/error.jpeg"
                                 width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
                        </c:if>
                    </spring:bind>
                    <input type="text" class="sectionText" name="recipient" id="recipient"
                           value="${profileUser.username}"/>
                </div>

                <div style="clear: both;">
                    <label class="inputCompose" for="subject">Subject:</label>
                    <spring:bind path="subject">
                        <c:if test="${status.error}">
                            <img style="float: left;margin-bottom: -5px;margin-left: -27px;"
                                 src="http://static.eaw1805.com/images/site/error.jpeg"
                                 width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
                        </c:if>
                    </spring:bind>
                    <form:input class="sectionText" path="subject" name="subject" id="subject"/>
                </div>

                <label class="send-message-btn" style="clear: both; margin-top: -60px;">
                    <input id="submitBtn" class="send-message-input" type="submit" value=""/>
                </label>

                <spring:bind path="bodyMessage">
                    <c:if test="${status.error}">
                        <img style="float: left;margin-bottom: -22px;"
                             src="http://static.eaw1805.com/images/site/error.jpeg"
                             width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
                    </c:if>
                </spring:bind>
                <form:textarea path="bodyMessage" class="sectionTextArea" style="margin-top: 10px;" rows="15"
                               name="bodyMessage" id="bodyMessage"/>

            </form:form>
        </section>

    </article>
</div>
<div id="main-article-bottom" style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
</div>

<div id="main-article-top-games" style="z-index: 2;position: relative; margin: 0 -40px 0 -38px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h1 class="bigmap" style="width: 600px !important; font-size: 36px; padding-left: 40px; padding-top: 20px;">
        User games</h1>
</div>
<div id="main-article-games"
     style="position: relative;margin: -10px 0px 0px -35px;width: 1033px;background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px -200px transparent;clear: both; overflow: hidden;">
    <article>
        <br><br>

        <c:if test="${fn:length(userGames) > 0 }">
            <c:forEach items="${userGames}" var="thisUsergame">
                <jsp:useBean id="thisUsergame" class="com.eaw1805.data.model.UserGame"/>
                <c:set var="nation" value="${thisUsergame.nation}"/>
                <c:set var="vp" value="${userGameVps[thisUsergame.game.gameId]}"/>
                <section class="gameInfo" id="gameInfo"
                         style="width: 468px; height: 170px; margin-bottom: 5px!important; ">
                    <h1 style="width: 463px;float: left; font-size: 28px !important;">
                        <a style="font-size: 26px !important;"
                           href="<c:url value="/scenario/${thisUsergame.game.scenarioIdToString}/game/${thisUsergame.game.gameId}/info"/>">Game ${thisUsergame.game.gameId}</a>
                        / ${dates[thisUsergame.game.gameId]}
                <span style="float: right;font-size: 24px !important; margin-top: 2px !important;">vp: ${vp}&nbsp;(
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
                    <a href='<c:url value="/scenario/${thisUsergame.game.scenarioIdToString}/nation/${nation.id}"/>'>
                        <img style="float:left;clear: both; margin-top: 10px ; margin-left: 5px;"
                             src='http://static.eaw1805.com/images/nations/nation-${nation.id}-120.png'
                             alt="Nation Info Page"
                             class="toolTip"
                             title="${nation.name} Info Page"
                             border=0>
                    </a>

                    <div style="float: left; margin-left: 55px;margin-top: 0px;width: 130px; height: 125px;">
                        <c:choose>
                            <c:when test="${thisUsergame.alive == false}">
                                <span style="margin-left:-108px; clear:both;color: red;font-size: 22px !important;">Dead Empire</span>
                                <img style="float:left; margin-top: 30px ; margin-left: 5px;"
                                     src='http://static.eaw1805.com/images/site/skull-large.png'
                                     alt="Dead Empire"
                                     class="toolTip"
                                     title="Dead Empire"
                                     width="100"
                                     border=0>
                            </c:when>
                            <c:when test="${thisUsergame.game.ended == false}">
                                &nbsp;
                            </c:when>
                            <c:otherwise>
                                <div style="width:130px; margin-left: -35px; margin-top: 8px; font-size: 16px;">
                                    Game has Ended<br/><br/>
                                    <% if (thisUsergame.getGame().getWinners() != null && thisUsergame.getGame().getWinners().contains("*" + thisUsergame.getNation().getId() + "*")) { %>
                                    World Domination
                                    <% } else if (thisUsergame.getGame().getCoWinners() != null && thisUsergame.getGame().getCoWinners().contains("*" + thisUsergame.getNation().getId() + "*")) { %>
                                    Co-Winner for World Domination
                                    <% } else if (thisUsergame.getGame().getRunnerUp() != null && thisUsergame.getGame().getRunnerUp().contains("*" + thisUsergame.getNation().getId() + "*")) { %>
                                    Runner-Up for World Domination
                                    <% } else { %>
                                    Still Standing Nation
                                    <% } %>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div style="clear:both; width:140px;float: left; margin-left: 5px;margin-top: -35px;">
                        <div style="width:140px;float: right; ">
                            <a href='http://forum.eaw1805.com/viewforum.php?f=${thisUsergame.game.forumId}'
                               title="Game Forum"
                               style="float:left;line-height: 1.3!important;"><span
                                    style="font-size: 14px;">Game Forum</span></a>
                        </div>
                    </div>

                    <div style="float: left;margin-top: -5px;margin-left: 5px;">
                    <span style="float:left;" class="inlinebar">
                        <c:forEach items="${globalActivityStat[thisUsergame.game.gameId]}" var="turn">${turn},
                        </c:forEach>
                    </span>
			        <span style="clear:both;float:left;margin-top: -14px;" class="inlinebar-player">
                        <c:forEach
                                items="${activityStat[thisUsergame.game.gameId][nation.id]}"
                                var="turn">${turn},
                        </c:forEach>
                    </span>
                    </div>

                    <c:set var="thisGameStats" value="${userGameStats[thisUsergame.game.gameId]}"/>
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
            </c:forEach>
        </c:if>
    </article>
</div>

<c:if test="${user.userType == 3 }">
    <%--for admins--%>
    <div id="main-article-admin" style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
    </div>

    <div id="main-article-admin" style="z-index: 2;position: relative; margin: 0 -40px 0 -38px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
        <h1 class="bigmap" style="width: 600px !important; font-size: 36px; padding-left: 40px; padding-top: 20px;">
            Account Information</h1>
    </div>
    <div id="main-article-admin"
         style="position: relative;margin: -10px 0px 0px -35px;width: 1033px;background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px -200px transparent;clear: both; overflow: hidden;">
        <br>
        <article>
            <section class="balance" id="balance-section" style="height: 170px;">
                <h1>Balance
                    <div style="font-size: small; float: right; margin-right: 4.5em; margin-top: 3px;">(in credits)
                    </div>
                </h1>

                <div style="height:70px; width:170px;margin-top: 8px;line-height: 1.5!important;">
                    <label for="free_credits">Free:</label>
                    <span id="free_credits" style="text-align: right">${profileUser.creditFree}</span>

                    <label for="trans_credits">Transferred:</label>
                    <span id="trans_credits" style="text-align: right">${profileUser.creditTransferred}</span>

                    <label for="bought_credits">Bought:</label>
                    <span id="bought_credits" style="text-align: right">${profileUser.creditBought}</span>

                </div>
            </section>

            <section class="transfer" style="width:266px;height: 170px;">
                <h1>Transfer Credits</h1>
                <c:url var="postUrl" value="/adminTransfer"/>
                <form:form id="adminTransferForm" commandName="adminTransferCommand" method="POST" action="${postUrl}">
                    <label class="transfer-label" style="width: 7em;" for="freeCreditsAmount">Free</label>
                    <form:input class="customInputAdmin" path="freeCreditsAmount" id="freeCreditsAmount" value="0"/>
                    <img id="freeCreditsAmountImg" style="display:none; float:right;"
                         src="http://static.eaw1805.com/images/site/error.jpeg"
                         width="15" height="15" class="error_tooltip" title="Error"/>

                    <label class="transfer-label" style="width: 7em;" for="transferredCreditsAmount">Transferred</label>
                    <form:input class="customInputAdmin" path="transferredCreditsAmount" id="transferredCreditsAmount"
                                value="0"/>
                    <img id="transferredCreditsAmountImg" style="display:none; float:right;"
                         src="http://static.eaw1805.com/images/site/error.jpeg"
                         width="15" height="15" class="error_tooltip" title="Error"/>

                    <label class="transfer-label" style="width: 7em;" for="boughtCreditsAmount">Bought</label>
                    <form:input class="customInputAdmin" path="boughtCreditsAmount" id="boughtCreditsAmount" value="0"/>
                    <img id="boughtCreditsAmountImg" style="display:none; float:right;"
                         src="http://static.eaw1805.com/images/site/error.jpeg"
                         width="15" height="15" class="error_tooltip" title="Error"/>


                    <label class="transfer-label" style="width: 7em;" for="comment">Comment</label>
                    <form:input class="customInputAdmin" path="comment" id="comment"/>
                    <img id="commentImg" style="display:none; float:right;"
                         src="http://static.eaw1805.com/images/site/error.jpeg"
                         width="15" height="15" class="error_tooltip" title="Error"/>

                    <form:input type="hidden" class="customInput" path="receiver" id="receiver"
                                value="${profileUser.username}"/>

                    <label class='save-setting-btn'
                           style="clear:both;margin-right:5px;width: 36px; height: 36px; float: right;"
                           title="Transfer Credits" onclick="">
                        <input type="submit" value="" style="clear: both!important;"/>
                    </label>

                </form:form>
                <script>
                    $('#adminTransferForm').submit(function () {

                        $('#freeCreditsAmountImg').hide();
                        $('#transferredCreditsAmountImg').hide();
                        $('#boughtCreditsAmountImg').hide();


                        var freeCreditsAmount = $('#freeCreditsAmount').val();
                        var transferredCreditsAmount = $('#transferredCreditsAmount').val();
                        var boughtCreditsAmount = $('#boughtCreditsAmount').val();
                        var comment = $('#comment').val();


                        if (freeCreditsAmount == transferredCreditsAmount
                                && transferredCreditsAmount == boughtCreditsAmount
                                && boughtCreditsAmount == 0) {


                            $('#freeCreditsAmountImg').show();
                            $('#freeCreditsAmountImg').attr('title', 'At least one credit amount should be filled in');

                            $('#transferredCreditsAmountImg').show();
                            $('#transferredCreditsAmountImg').attr('title', 'At least one credit amount should be filled in');

                            $('#boughtCreditsAmountImg').show();
                            $('#boughtCreditsAmountImg').attr('title', 'At least one credit amount should be filled in');

                            return false;
                        }

                        if (freeCreditsAmount == null || isNaN(freeCreditsAmount)) {

                            $('#freeCreditsAmountImg').show();
                            $('#freeCreditsAmountImg').attr('title', 'Invalid Credits Amount');

                            return false;
                        }

                        if (transferredCreditsAmount == null || isNaN(transferredCreditsAmount)) {

                            $('#transferredCreditsAmountImg').show();
                            $('#transferredCreditsAmountImg').attr('title', 'Invalid Credits Amount');

                            return false;
                        }

                        if (boughtCreditsAmount == null || isNaN(boughtCreditsAmount)) {

                            $('#boughtCreditsAmountImg').show();
                            $('#boughtCreditsAmountImg').attr('title', 'Invalid Credits Amount');

                            return false;
                        }

                        if (comment == null || comment == "") {
                            $('#commentImg').show();
                            $('#commentImg').attr('title', 'Comment cannot be empty');

                            return false;
                        }

                        return true;

                    });
                </script>
            </section>
            <section class="section-history" style="width:470px;height: 260px;">
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

                <div style="margin-right: 16px; float: right;"><a
                        href='<c:url value="/user/${profileUser.username}/paymentHistory"/>'>View History</a></div>

            </section>

            <section class="balance" id="balance-section" style="height: 179px;width: 268px;">
                <h1>Admin Info</h1>
                <label style="width:7em;">Email:</label><span style="width: 13em;">${profileUser.email}</span>
                <label style="width:7em;">Last login:</label><span style="width: 13em;"><fmt:formatDate
                    value="${userLastVisit}" pattern="dd/MM/yyyy"/></span>
                <label style="width:7em;">Last process:</label><span style="width: 13em;"><fmt:formatDate
                    value="${profileUser.lastProcDate}" pattern="dd/MM/yyyy"/></span>
                <label style="width:7em;">Joined:</label><span style="width: 13em;"><fmt:formatDate
                    value="${userDateJoined}" pattern="dd/MM/yyyy"/></span>
                <c:if test="${freeUserGame != null}">

                    <div>
                        <a href="<c:url value="/play/scenario/${freeUserGame.game.scenarioIdToString}/game/${freeUserGame.game.gameId}/nation/5"/>">Tutorial
                            Game</a></div>
                </c:if>
                <div style="margin-top: 20px;">
                    <a href="javascript:$.blockUI({ message: $('#dropPopup_duser'), css: { width: '390px',cursor: 'default' } });"
                       class="minibutton"
                       title="Delete this user"
                       style="line-height: 1.3!important;"><span
                            style="font-size: 14px; width:7em; float:left;">Delete User</span></a>
                </div>

            </section>

            <div style="display:none;" id="dropPopup_duser">
                <h3 style="margin:auto; width:90%; margin-top: 17px;">Delete User</h3>

                <div>If you delete a user there is no way back.</div>
                <div>Games and messages will appear as sent by a "Deleted" user.</div>
                <div>Active user games will be dropped and set free for other players.</div>
                <div>User won't be able to login with this account ever again.</div>
                <div>Statistics will still be available.</div>
                <div style="height:70px; width:170px;margin-top: 14px;line-height: 1.5!important;margin-left: 60px;">
                    <a style="font-size: 22px;" href="<c:url value="/user/${profileUser.username}/delete"/>">Delete
                        user</a>
                </div>
                <button id="cancelb" class="cancel" title="Cancel" onfocus=" this.blur();"
                        style="clear: both; margin-top: -74px;margin-left: 98px; " aria-describedby=""
                        onclick="$.unblockUI();$('#'+$('#cancelb').attr('aria-describedby')).hide();return false;"
                        value="cancel"/>
            </div>
    </div>
</c:if>
<div id="main-footer" style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
</div>

<div style=" width: 338px;
    background: url('http://static.eaw1805.com/images/site/Empire_parchment.png') repeat-y scroll 0px 0px transparent;
    background-size: 338px 376px;
    float: left;
    min-height: 370px;
    margin-left: -34px;
    line-height: 1.3 !important;
    font-size: 95%;
    font-family: Georgia,'Times New Roman',Times,serif;
    text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);">
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
                        <c:when test="${key == 'achievements'}">
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
    text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);">

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

