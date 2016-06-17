<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<jsp:useBean id="game" scope="request" class="com.eaw1805.data.model.Game"/>
<jsp:useBean id="gameId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="gameDate" scope="request" class="java.lang.String"/>

<jsp:useBean id="winnerList" scope="request" type="java.util.List<com.eaw1805.data.model.Nation>"/>
<jsp:useBean id="coWinnerList" scope="request" type="java.util.List<com.eaw1805.data.model.Nation>"/>
<jsp:useBean id="runnerUpList" scope="request" type="java.util.List<com.eaw1805.data.model.Nation>"/>

<jsp:useBean id="nationList" scope="request" type="java.util.List<com.eaw1805.data.model.Nation>"/>
<jsp:useBean id="nationFree" scope="request" type="java.util.List<com.eaw1805.data.model.Nation>"/>
<jsp:useBean id="nationDead" scope="request" type="java.util.List<com.eaw1805.data.model.Nation>"/>

<jsp:useBean id="nationToPlayer" scope="request" type="java.util.Map<java.lang.Integer, com.eaw1805.data.model.User>"/>
<jsp:useBean id="nationToStatus" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationToAlive" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>

<jsp:useBean id="nationVP" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationLand" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationEconomy" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationProduction" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationArmy" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationNavy" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationAKills" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationADeaths" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationSKills" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationSDeaths" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationBTotal" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationBWon" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationNTotal" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="nationNWon" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>

<jsp:useBean id="rankVP" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="rankEconomy" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="rankProduction" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="rankLand" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="rankArmy" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="rankNavy" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="rankAKills" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="rankADeaths" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="rankSKills" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="rankSDeaths" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="rankBTotal" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="rankBWon" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="rankNTotal" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>
<jsp:useBean id="rankNWon" scope="request" type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>

<jsp:useBean id="statVP" scope="request" type="java.util.List<java.util.List<com.eaw1805.www.commands.ChartData>>"/>
<jsp:useBean id="statDOMINATION" scope="request"
             type="java.util.List<java.util.List<com.eaw1805.www.commands.ChartData>>"/>

<jsp:useBean id="watchedGames" scope="request"
             type="java.util.Map<java.lang.Integer, java.lang.Integer>"/>

<jsp:useBean id="totalMoney" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="totalInPt" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="totalArmies" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="totalCannons" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="totalPopulation" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="monthsFull" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
<jsp:useBean id="news" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="allUsers" scope="request" class="java.util.List<com.eaw1805.data.model.User>"/>
<jsp:useBean id="nationToUserGame" scope="request"
             class="java.util.Map<java.lang.Integer, com.eaw1805.data.model.UserGame>"/>
<%@ include file="/WEB-INF/jsp/cards/CustomFunctions.jsp" %>
<script src='<c:url value="/js/sliderman.1.3.7.js"/>' type="text/javascript"></script>
<script src='<c:url value="/js/sliderman.1.3.7b.js"/>' type="text/javascript"></script>
<script src="<c:url value="/js/highcharts.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/vpchart.js"/>" type="text/javascript"></script>
<script src="http://js.nicedit.com/nicEdit-latest.js" type="text/javascript"></script>


<style type="text/css">

    .launchGameButton {
        background-color: transparent !important;
        float: right !important;
        width: 300px !important;
        height: 72px !important;
        background-image: url("http://static.eaw1805.com/images/site/gameButtons/LaunchCustomGameOff.png") !important;
        background-size: auto 72px !important;
        background-repeat: no-repeat !important;
        display: inline-block !important;
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

    .launchGameButton:hover {
        background-color: transparent !important;
        float: right !important;
        width: 300px !important;
        height: 72px !important;
        background-image: url("http://static.eaw1805.com/images/site/gameButtons/LaunchCustomGameHover.png") !important;
        background-size: auto 72px !important;
        background-repeat: no-repeat !important;
        display: inline-block !important;
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

    .cancelGameButton {
        background-color: transparent !important;
        float: right !important;
        width: 279px !important;
        height: 50px !important;
        background-image: url("http://static.eaw1805.com/images/site/gameButtons/FinishGameoff.png") !important;
        background-size: auto 48px !important;
        background-repeat: no-repeat !important;
        display: inline-block !important;
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

    .cancelGameButton:hover {
        background-color: transparent !important;
        float: right !important;
        width: 279px !important;
        height: 50px !important;
        background-image: url("http://static.eaw1805.com/images/site/gameButtons/FinishGamehov.png") !important;
        background-size: auto 48px !important;
        background-repeat: no-repeat !important;
        display: inline-block !important;
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


<%--<!-- HTML5 shim, for IE6-8 support of HTML elements--><!--[if lt IE 9]>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
<script src="<c:url value="/js/timeline_js/storyjs-embed.js"/>" type="text/javascript"></script>--%>
<script type="text/javascript">
    // For those who need them (< IE 9), add support for CSS functions
    var isStyleFuncSupported = !!CSSStyleDeclaration.prototype.getPropertyValue;
    if (!isStyleFuncSupported) {
        CSSStyleDeclaration.prototype.getPropertyValue = function (a) {
            return this.getAttribute(a);
        };
        CSSStyleDeclaration.prototype.setProperty = function (styleName, value, priority) {
            this.setAttribute(styleName, value);
            var priority = typeof priority != 'undefined' ? priority : '';
            if (priority != '') {
                // Add priority manually
                var rule = new RegExp(RegExp.escape(styleName) + '\\s*:\\s*' + RegExp.escape(value) + '(\\s*;)?', 'gmi');
                this.cssText = this.cssText.replace(rule, styleName + ': ' + value + ' !' + priority + ';');
            }
        }
        CSSStyleDeclaration.prototype.removeProperty = function (a) {
            return this.removeAttribute(a);
        }
        CSSStyleDeclaration.prototype.getPropertyPriority = function (styleName) {
            var rule = new RegExp(RegExp.escape(styleName) + '\\s*:\\s*[^\\s]*\\s*!important(\\s*;)?', 'gmi');
            return rule.test(this.cssText) ? 'important' : '';
        }
    }

    // Escape regex chars with \
    RegExp.escape = function (text) {
        return text.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
    }

    // The style function
    jQuery.fn.cssImportant = function (styleName, value, priority) {
        // DOM node
        var node = this.get(0);
        // Ensure we have a DOM node
        if (typeof node == 'undefined') {
            return;
        }
        // CSSStyleDeclaration
        var style = this.get(0).style;
        // Getter/Setter
        if (typeof styleName != 'undefined') {
            if (typeof value != 'undefined') {
                // Set style property
                var priority = typeof priority != 'undefined' ? priority : '';
                style.setProperty(styleName, value, priority);
            } else {
                // Get style property
                return style.getPropertyValue(styleName);
            }
        } else {
            // Get CSSStyleDeclaration
            return style;
        }
    }


    $.blockUI.defaults.applyPlatformOpacityRules = false;


    $(document).ready(function () {
        <c:choose>
        <c:when test="${param.err != null}">
        <c:if  test="${param.err == 'nec'}">
        $('#nec_cost').text('${param.cost}');
        </c:if>
        $.blockUI({message: $('#error_message_${param.err}'), css: {width: '390px'}});
        </c:when>
        <c:when test="${param.status != null}">
        <c:if test="${param.status == 'joined'}">
        $.blockUI({message: $('#status_joined'), css: {width: '390px'}});
        </c:if>
        </c:when>
        </c:choose>

    });


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

    var userToHash = new Object();
    <c:forEach items="${allUsers}" var="curUser">
    userToHash["${curUser.username}"] = "${curUser.emailEncoded}";
    </c:forEach>


    $(document).ready(function () {
        var chartVP = new Highcharts.Chart({
            chart: {
                renderTo: 'container-vp',
                type: 'spline'
            },
            credits: {
                enabled: false
            },
            title: {
                text: ''
            },
            xAxis: {
                <c:choose>
                <c:when test="${game.turn > 36}">
                tickInterval: 180 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 24}">
                tickInterval: 90 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 12}">
                tickInterval: 60 * 24 * 3600 * 1000,
                </c:when>
                <c:otherwise>
                tickInterval: 30 * 24 * 3600 * 1000,
                </c:otherwise>
                </c:choose>
                type: 'datetime',
                dateTimeLabelFormats: {
                    month: '%b %Y'
                }
            },
            yAxis: {
                title: {
                    text: 'Victory Points'
                }
            },
            tooltip: {
                formatter: function () {
                    return '<strong>' + this.series.name + '</strong><br/>' +
                            Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
                }
            },
            series: [
                <c:forEach items="${statVP}" var="thisNation">
                {
                    name: '<c:out value="${thisNation['0'].caption}"/>',
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data: [
                        <c:forEach items="${thisNation}" var="thisMonth">
                        [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/>],
                        </c:forEach>
                    ]
                },
                </c:forEach>
            ]
        });

        var chartDOMINATION = new Highcharts.Chart({
            chart: {
                renderTo: 'container-domination',
                type: 'spline'
            },
            credits: {
                enabled: false
            },
            title: {
                text: ''
            },
            xAxis: {
                <c:choose>
                <c:when test="${game.turn > 36}">
                tickInterval: 180 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 24}">
                tickInterval: 90 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 12}">
                tickInterval: 60 * 24 * 3600 * 1000,
                </c:when>
                <c:otherwise>
                tickInterval: 30 * 24 * 3600 * 1000,
                </c:otherwise>
                </c:choose>
                type: 'datetime',
                dateTimeLabelFormats: {
                    month: '%b %Y'
                }
            },
            yAxis: {
                title: {
                    text: 'World Domination'
                }
            },
            tooltip: {
                formatter: function () {
                    return '<strong>' + this.series.name + '</strong><br/>' +
                            Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
                }
            },
            series: [
                <c:forEach items="${statDOMINATION}" var="thisNation">
                {
                    name: '<c:out value="${thisNation['0'].caption}"/>',
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data: [
                        <c:forEach items="${thisNation}" var="thisMonth">
                        [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/>],
                        </c:forEach>
                    ]
                },
                </c:forEach>
            ]
        });

        <c:if test="${game.ended == true}">
        <jsp:useBean id="statLAND1" scope="request" type="java.util.List<java.util.List<com.eaw1805.www.commands.ChartData>>"/>
        <jsp:useBean id="statLAND2" scope="request" type="java.util.List<java.util.List<com.eaw1805.www.commands.ChartData>>"/>
        <jsp:useBean id="statLAND3" scope="request" type="java.util.List<java.util.List<com.eaw1805.www.commands.ChartData>>"/>
        <jsp:useBean id="statLAND4" scope="request" type="java.util.List<java.util.List<com.eaw1805.www.commands.ChartData>>"/>
        <jsp:useBean id="statARMY" scope="request" type="java.util.List<java.util.List<com.eaw1805.www.commands.ChartData>>"/>
        <jsp:useBean id="statNAVY" scope="request" type="java.util.List<java.util.List<com.eaw1805.www.commands.ChartData>>"/>
        <jsp:useBean id="statARMYKILLS" scope="request" type="java.util.List<java.util.List<com.eaw1805.www.commands.ChartData>>"/>
        <jsp:useBean id="statNAVYSINKS" scope="request" type="java.util.List<java.util.List<com.eaw1805.www.commands.ChartData>>"/>
        <jsp:useBean id="statMONEY" scope="request" type="java.util.List<java.util.List<com.eaw1805.www.commands.ChartData>>"/>

        var chartLAND1 = new Highcharts.Chart({
            chart: {
                renderTo: 'container-land-1',
                type: 'spline'
            },
            credits: {
                enabled: false
            },
            title: {
                text: ''
            },
            xAxis: {
                <c:choose>
                <c:when test="${game.turn > 36}">
                tickInterval: 180 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 24}">
                tickInterval: 90 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 12}">
                tickInterval: 60 * 24 * 3600 * 1000,
                </c:when>
                <c:otherwise>
                tickInterval: 30 * 24 * 3600 * 1000,
                </c:otherwise>
                </c:choose>
                type: 'datetime',
                dateTimeLabelFormats: {
                    month: '%b %Y'
                }
            },
            yAxis: {
                title: {
                    text: 'Population'
                }
            },
            tooltip: {
                formatter: function () {
                    return '<strong>' + this.series.name + '</strong><br/>' +
                            Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
                }
            },
            series: [
                <c:forEach items="${statLAND1}" var="thisNation">
                {
                    name: '<c:out value="${thisNation['0'].caption}"/>',
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data: [
                        <c:forEach items="${thisNation}" var="thisMonth">
                        [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/>],
                        </c:forEach>
                    ]
                },
                </c:forEach>
            ]
        });

        var chartLAND2 = new Highcharts.Chart({
            chart: {
                renderTo: 'container-land-2',
                type: 'spline'
            },
            credits: {
                enabled: false
            },
            title: {
                text: ''
            },
            xAxis: {
                <c:choose>
                <c:when test="${game.turn > 36}">
                tickInterval: 180 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 24}">
                tickInterval: 90 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 12}">
                tickInterval: 60 * 24 * 3600 * 1000,
                </c:when>
                <c:otherwise>
                tickInterval: 30 * 24 * 3600 * 1000,
                </c:otherwise>
                </c:choose>
                type: 'datetime',
                dateTimeLabelFormats: {
                    month: '%b %Y'
                }
            },
            yAxis: {
                title: {
                    text: 'Population'
                }
            },
            tooltip: {
                formatter: function () {
                    return '<strong>' + this.series.name + '</strong><br/>' +
                            Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
                }
            },
            series: [
                <c:forEach items="${statLAND2}" var="thisNation">
                {
                    name: '<c:out value="${thisNation['0'].caption}"/>',
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data: [
                        <c:forEach items="${thisNation}" var="thisMonth">
                        [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/>],
                        </c:forEach>
                    ]
                },
                </c:forEach>
            ]
        });

        var chartLAND3 = new Highcharts.Chart({
            chart: {
                renderTo: 'container-land-3',
                type: 'spline'
            },
            credits: {
                enabled: false
            },
            title: {
                text: ''
            },
            xAxis: {
                <c:choose>
                <c:when test="${game.turn > 36}">
                tickInterval: 180 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 24}">
                tickInterval: 90 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 12}">
                tickInterval: 60 * 24 * 3600 * 1000,
                </c:when>
                <c:otherwise>
                tickInterval: 30 * 24 * 3600 * 1000,
                </c:otherwise>
                </c:choose>
                type: 'datetime',
                dateTimeLabelFormats: {
                    month: '%b %Y'
                }
            },
            yAxis: {
                title: {
                    text: 'Population'
                }
            },
            tooltip: {
                formatter: function () {
                    return '<strong>' + this.series.name + '</strong><br/>' +
                            Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
                }
            },
            series: [
                <c:forEach items="${statLAND3}" var="thisNation">
                {
                    name: '<c:out value="${thisNation['0'].caption}"/>',
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data: [
                        <c:forEach items="${thisNation}" var="thisMonth">
                        [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/>],
                        </c:forEach>
                    ]
                },
                </c:forEach>
            ]
        });

        var chartLAND4 = new Highcharts.Chart({
            chart: {
                renderTo: 'container-land-4',
                type: 'spline'
            },
            credits: {
                enabled: false
            },
            title: {
                text: ''
            },
            xAxis: {
                <c:choose>
                <c:when test="${game.turn > 36}">
                tickInterval: 180 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 24}">
                tickInterval: 90 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 12}">
                tickInterval: 60 * 24 * 3600 * 1000,
                </c:when>
                <c:otherwise>
                tickInterval: 30 * 24 * 3600 * 1000,
                </c:otherwise>
                </c:choose>
                type: 'datetime',
                dateTimeLabelFormats: {
                    month: '%b %Y'
                }
            },
            yAxis: {
                title: {
                    text: 'Population'
                }
            },
            tooltip: {
                formatter: function () {
                    return '<strong>' + this.series.name + '</strong><br/>' +
                            Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
                }
            },
            series: [
                <c:forEach items="${statLAND4}" var="thisNation">
                {
                    name: '<c:out value="${thisNation['0'].caption}"/>',
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data: [
                        <c:forEach items="${thisNation}" var="thisMonth">
                        [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/>],
                        </c:forEach>
                    ]
                },
                </c:forEach>
            ]
        });

        var chartARMY = new Highcharts.Chart({
            chart: {
                renderTo: 'container-army',
                type: 'spline'
            },
            credits: {
                enabled: false
            },
            title: {
                text: ''
            },
            xAxis: {
                <c:choose>
                <c:when test="${game.turn > 36}">
                tickInterval: 180 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 24}">
                tickInterval: 90 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 12}">
                tickInterval: 60 * 24 * 3600 * 1000,
                </c:when>
                <c:otherwise>
                tickInterval: 30 * 24 * 3600 * 1000,
                </c:otherwise>
                </c:choose>
                type: 'datetime',
                dateTimeLabelFormats: {
                    month: '%b %Y'
                }
            },
            yAxis: {
                title: {
                    text: 'Battalions'
                }
            },
            tooltip: {
                formatter: function () {
                    return '<strong>' + this.series.name + '</strong><br/>' +
                            Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
                }
            },
            series: [
                <c:forEach items="${statARMY}" var="thisNation">
                {
                    name: '<c:out value="${thisNation['0'].caption}"/>',
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data: [
                        <c:forEach items="${thisNation}" var="thisMonth">
                        [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/>],
                        </c:forEach>
                    ]
                },
                </c:forEach>
            ]
        });

        var chartNAVY = new Highcharts.Chart({
            chart: {
                renderTo: 'container-navy',
                type: 'spline'
            },
            credits: {
                enabled: false
            },
            title: {
                text: ''
            },
            xAxis: {
                <c:choose>
                <c:when test="${game.turn > 36}">
                tickInterval: 180 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 24}">
                tickInterval: 90 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 12}">
                tickInterval: 60 * 24 * 3600 * 1000,
                </c:when>
                <c:otherwise>
                tickInterval: 30 * 24 * 3600 * 1000,
                </c:otherwise>
                </c:choose>
                type: 'datetime',
                dateTimeLabelFormats: {
                    month: '%b %Y'
                }
            },
            yAxis: {
                title: {
                    text: 'Ships'
                }
            },
            tooltip: {
                formatter: function () {
                    return '<strong>' + this.series.name + '</strong><br/>' +
                            Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
                }
            },
            series: [
                <c:forEach items="${statNAVY}" var="thisNation">
                {
                    name: '<c:out value="${thisNation['0'].caption}"/>',
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data: [
                        <c:forEach items="${thisNation}" var="thisMonth">
                        [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/>],
                        </c:forEach>
                    ]
                },
                </c:forEach>
            ]
        });

        var chartARMYKILLS = new Highcharts.Chart({
            chart: {
                renderTo: 'container-armykills',
                type: 'spline'
            },
            credits: {
                enabled: false
            },
            title: {
                text: ''
            },
            xAxis: {
                <c:choose>
                <c:when test="${game.turn > 36}">
                tickInterval: 180 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 24}">
                tickInterval: 90 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 12}">
                tickInterval: 60 * 24 * 3600 * 1000,
                </c:when>
                <c:otherwise>
                tickInterval: 30 * 24 * 3600 * 1000,
                </c:otherwise>
                </c:choose>
                type: 'datetime',
                dateTimeLabelFormats: {
                    month: '%b %Y'
                }
            },
            yAxis: {
                title: {
                    text: 'Soldiers'
                }
            },
            tooltip: {
                formatter: function () {
                    return '<strong>' + this.series.name + '</strong><br/>' +
                            Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
                }
            },
            series: [
                <c:forEach items="${statARMYKILLS}" var="thisNation">
                {
                    name: '<c:out value="${thisNation['0'].caption}"/>',
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data: [
                        <c:forEach items="${thisNation}" var="thisMonth">
                        [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/>],
                        </c:forEach>
                    ]
                },
                </c:forEach>
            ]
        });

        var chartNAVYSINKS = new Highcharts.Chart({
            chart: {
                renderTo: 'container-navysinks',
                type: 'spline'
            },
            credits: {
                enabled: false
            },
            title: {
                text: ''
            },
            xAxis: {
                <c:choose>
                <c:when test="${game.turn > 36}">
                tickInterval: 180 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 24}">
                tickInterval: 90 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 12}">
                tickInterval: 60 * 24 * 3600 * 1000,
                </c:when>
                <c:otherwise>
                tickInterval: 30 * 24 * 3600 * 1000,
                </c:otherwise>
                </c:choose>
                type: 'datetime',
                dateTimeLabelFormats: {
                    month: '%b %Y'
                }
            },
            yAxis: {
                title: {
                    text: 'Ships'
                }
            },
            tooltip: {
                formatter: function () {
                    return '<strong>' + this.series.name + '</strong><br/>' +
                            Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
                }
            },
            series: [
                <c:forEach items="${statNAVYSINKS}" var="thisNation">
                {
                    name: '<c:out value="${thisNation['0'].caption}"/>',
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data: [
                        <c:forEach items="${thisNation}" var="thisMonth">
                        [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/>],
                        </c:forEach>
                    ]
                },
                </c:forEach>
            ]
        });

        var chartMONEY = new Highcharts.Chart({
            chart: {
                renderTo: 'container-money',
                type: 'spline'
            },
            credits: {
                enabled: false
            },
            title: {
                text: ''
            },
            xAxis: {
                <c:choose>
                <c:when test="${game.turn > 36}">
                tickInterval: 180 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 24}">
                tickInterval: 90 * 24 * 3600 * 1000,
                </c:when>
                <c:when test="${game.turn > 12}">
                tickInterval: 60 * 24 * 3600 * 1000,
                </c:when>
                <c:otherwise>
                tickInterval: 30 * 24 * 3600 * 1000,
                </c:otherwise>
                </c:choose>
                type: 'datetime',
                dateTimeLabelFormats: {
                    month: '%b %Y'
                }
            },
            yAxis: {
                title: {
                    text: 'Ships'
                }
            },
            tooltip: {
                formatter: function () {
                    return '<strong>' + this.series.name + '</strong><br/>' +
                            Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
                }
            },
            series: [
                <c:forEach items="${statMONEY}" var="thisNation">
                {
                    name: '<c:out value="${thisNation['0'].caption}"/>',
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data: [
                        <c:forEach items="${thisNation}" var="thisMonth">
                        [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/>],
                        </c:forEach>
                    ]
                },
                </c:forEach>
            ]
        });
        </c:if>

        /*    createStoryJS({
         type:       'timeline',
         width:      '940',
         height:     '400',
         source:     '
        <c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/timeline"/>',
         embed_id:   'timeline-embed'
         });*/

    });

    function openPickupForm(scenario, game, nation, nationName, cost) {
        <c:url var="thisUrl" value="/"/>
        var url = "${thisUrl}" + "scenario/" + scenario + "/game/" + game + "/pickup/" + nation;
        $('#joinForm').attr('action', url);
        $('#form_nationId').text(nation);
        $('#nationImg').attr('src', "http://static.eaw1805.com/images/nations/nation-" + nation + "-list.jpg");
        $('#nationImg').attr('title', nationName);
        $('#joinTitle').text("Join Game " + game);
        $('#cost').text(cost);
        $('#ok').blur();
        $.blockUI({message: $('#question'), css: {width: '390px'}});
    }

    function cancelInvitation(nationId) {
        var username = $("#player_" + nationId).val();
        <c:url var="thisUrl" value="/"/>
        var url = "${thisUrl}" + "invitation/scenario/${game.scenarioIdToString}/game/${game.gameId}/nation/" + nationId + "/cancel";
        $.ajax({
            url: url,
            type: 'post',
            dataType: "html"
        }).done(function (data) {
            $.unblockUI();
            var err = (data.split(",")[0] == "e");
            var posTaken = (data.split(",")[1] == "1");
            var success = (data.split(",")[0] == "i");

            if (err) {
                if (posTaken) {
                    alert("Position has already been accepted by a player");
                } else {
                    alert("Could not cancel invitation");
                }

            }
            if (success) {
                $("#avatar_" + nationId).hide();
                $("#usernameGoesHere_" + nationId).hide();
                $("#player_" + nationId).val("");
                $("#player_" + nationId).show();

                $("#sentIndicator_" + nationId).cssImportant("display", "none", "important");
                $("#cancelIndicator_" + nationId).cssImportant("display", "none", "important");
                $("#sendButton_" + nationId).cssImportant("display", "", "important");
            }
//                if (success) {
//                    window.location.reload();
//                    return;
//                }
        });
    }

    function sendInvitation(nationId) {
        var username = $("#player_" + nationId).val();
        <c:url var="thisUrl" value="/"/>
        var url = "${thisUrl}" + "invitation/scenario/${game.scenarioIdToString}/game/${game.gameId}/nation/" + nationId + "/user/" + username + "/send";
        $.ajax({
            url: url,
            type: 'post',
            dataType: "html"
        }).done(function (data) {
            $.unblockUI();
            var err = (data.split(",")[0] == "e");
            var posTaken = (data.split(",")[1] == "1");
            var hasOtherInvite = (data.split(",")[1] == "3");
            var hasOtherPos = (data.split(",")[1] == "4");
            var hasOldPos = (data.split(",")[1] == "5");
            var success = (data.split(",")[0] == "i");

            if (err) {
                if (posTaken) {
                    alert("Position has already been accepted by a player");
                } else if (hasOtherInvite) {
                    alert("Could not send invitation, player has already a pending invitation");
                } else if (hasOtherPos) {
                    alert("Could not send invitation, player already plays a Nation in this game.")
                } else if (hasOldPos) {
                    alert("Could not send invitation, player was playing with a different nation in the past in this game.")
                } else {
                    alert("Could not send invitation");
                }
                $("#avatar_" + nationId).hide();
                $("#usernameGoesHere_" + nationId).hide();
                $("#player_" + nationId).val("");
                $("#player_" + nationId).show();

                $("#sentIndicator_" + nationId).cssImportant("display", "none", "important");
                $("#cancelIndicator_" + nationId).cssImportant("display", "none", "important");
                $("#sendButton_" + nationId).cssImportant("display", "", "important");
            }
            if (success) {
                $("#sendButton_" + nationId).cssImportant("display", "none", "important");
                $("#cancelIndicator_" + nationId).cssImportant("display", "none", "important");
                $("#sentIndicator_" + nationId).cssImportant("display", "", "");

            }
//                if (success) {
//                    window.location.reload();
//                    return;
//                }
        });

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

        $("#sentIndicator_" + nId).cssImportant("display", "none", "important");
        $("#cancelIndicator_" + nId).cssImportant("display", "none", "important");
        $("#sendButton_" + nId).cssImportant("display", "", "");
    }
</script>
<article>
    <smallmap style="height: 120px;">
        <h2 class="bigmap">Game Status</h2>
        <table border="0">
            <tr>
                <th align="left" width="130">Creation Date</th>
                <td align="left"><fmt:formatDate type="date" dateStyle="default"
                                                 timeStyle="default" value="${game.dateStart}"/></td>
            </tr>
            <tr>
                <th align="left" width="130">Turn schedule</th>
                <td align="left">
                    <c:choose>
                        <c:when test="${game.schedule > 0}">every ${game.schedule} days</c:when>
                        <c:when test="${game.schedule == 0}">${game.cronScheduleDescr}</c:when>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <th align="left" width="130">Last Processing</th>
                <td align="left"><fmt:formatDate type="date" dateStyle="default"
                                                 timeStyle="default" value="${game.dateLastProc}"/></td>
            </tr>
            <tr>
                <c:choose>
                    <c:when test="${game.ended}">
                        <th align="left" colspan="2">The Game has Ended</th>
                    </c:when>
                    <c:otherwise>
                        <th align="left" width="130">Next Processing</th>
                        <td align="left">
                            <c:choose>
                                <c:when test="${game.status.contains('processed')}">
                                    <b>${game.status}</b>
                                </c:when>
                                <c:when test="${game.status.contains('ready')}">
                                    <fmt:formatDate type="both" dateStyle="default" timeStyle="default"
                                                    value="${game.dateNextProc}"/>
                                </c:when>
                                <c:when test="${game.turn == 0}">
                                    <b>when all nations are set</b>
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatDate type="both" dateStyle="default" timeStyle="default"
                                                    value="${game.dateNextProc}"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </c:otherwise>
                </c:choose>
            </tr>
        </table>
        <div style="float: right; margin-right: 0px; margin-top: -114px;">
            <c:if test="${!watchedGames.containsKey(game.gameId)}">
                <a href="<c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/toggleWatch/watch"/>"
                   class="minibutton"
                   title="Watch game">
                    <img src="http://static.eaw1805.com/images/buttons/ButSpyReportsMapOff.png"
                         alt="Watch Game"
                         title="Watch Game"
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

    </smallmap>

    <smallmap style="height: 120px;width: 625px;">
        <h2 class="bigmap">Game Information</h2>
        <table border="0">
            <tr>
                <th align="left" width="152">Total Player Orders</th>
                <td align="left" width="160"><fmt:formatNumber
                        type="number"
                        maxFractionDigits="0"
                        groupingUsed="true"
                        value="${totalOrders}"/></td>
                <th align="left" width="152">Total Soldiers</th>
                <td align="left" width="160"><fmt:formatNumber
                        type="number"
                        maxFractionDigits="0"
                        groupingUsed="true"
                        value="${totalArmies}"/></td>
            </tr>
            <tr>
                <th align="left" width="152">Total Population</th>
                <td align="left" width="160"><fmt:formatNumber
                        type="number"
                        maxFractionDigits="0"
                        groupingUsed="true"
                        value="${totalPopulation}"/></td>
                <th align="left" width="152">Total Ship Cannons</th>
                <td align="left" width="160"><fmt:formatNumber
                        type="number"
                        maxFractionDigits="0"
                        groupingUsed="true"
                        value="${totalCannons}"/></td>
            </tr>
            <tr>
                <th align="left" width="152">Total Money Available</th>
                <td align="left" width="160"><fmt:formatNumber
                        type="number"
                        maxFractionDigits="0"
                        groupingUsed="true"
                        value="${totalMoney}"/></td>
                <th align="left" width="152">Total Tactical Battles</th>
                <td align="left" width="160"><fmt:formatNumber
                        type="number"
                        maxFractionDigits="0"
                        groupingUsed="true"
                        value="${totalTBattles}"/></td>
            </tr>
            <tr>
                <th align="left" width="152">Total Industrial Points</th>
                <td align="left" width="160"><fmt:formatNumber
                        type="number"
                        maxFractionDigits="0"
                        groupingUsed="true"
                        value="${totalInPt}"/></td>
                <th align="left" width="152">Total Naval Battles</th>
                <td align="left" width="160"><fmt:formatNumber
                        type="number"
                        maxFractionDigits="0"
                        groupingUsed="true"
                        value="${totalNBattles}"/></td>
            </tr>
        </table>
    </smallmap>


    <c:if test="${game.userId != 2}">
        <smallmap style="height: 200px;">
            <h2 class="bigmap">Join Rules</h2>
            <table border="0">
                <tr>
                    <th align="left" width="130">Positions</th>
                    <td align="left" width="140">
                        <c:choose>
                            <c:when test="${game.privateGame}">By invitation</c:when>
                            <c:otherwise>Open</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th align="left" width="130">Honour To Join</th>
                    <td align="left" width="140">
                        <c:choose>
                            <c:when test="${game.vpsToJoin == 0}">everyone</c:when>
                            <c:otherwise>${game.vpsToJoin}</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th align="left" width="130">Honour Bet</th>
                    <td align="left" width="140">
                        <c:choose>
                            <c:when test="${game.bet ==0}">no bet</c:when>
                            <c:otherwise>${game.bet}</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <c:choose>
                        <c:when test="${game.bet ==0}">
                            <th align="left" width="130">&nbsp;</th>
                            <td align="left" width="140">&nbsp;</td>
                        </c:when>
                        <c:otherwise>
                            <th align="left" width="130">Honour Sharing</th>
                            <td align="left" width="140">${game.betSharingToString}</td>
                        </c:otherwise>
                    </c:choose>
                </tr>

            </table>
        </smallmap>

        <smallmap style="height: 200px;width:625px;">
            <h2 class="bigmap">Scenario Settings</h2>
            <table border="0">
                <tr>
                    <th align="left" width="200">Duration</th>
                    <td align="left" width="50">${game.typeToString}</td>

                    <th align="left" width="200">Random Events</th>
                    <td align="left" width="50"><%=getTickHtml(game.isRandomEvents())%>
                    </td>

                    <th align="left" width="200">Rumors</th>
                    <td align="left" width="50"><%=getTickHtml(game.isRumorsEnabled())%>
                    </td>

                    <th align="left" width="200">Fog of War</th>
                    <td align="left" width="50"><%=getTickHtml(game.isFogOfWar())%>
                    </td>
                </tr>
                <tr>
                    <th align="left" width="200">Always Summer</th>
                    <td align="left" width="50"><%=getTickHtml(game.isAlwaysSummerWeather())%>
                    </td>

                    <th>&nbsp;</th>
                    <td>&nbsp;</td>

                    <th align="left" width="200">Field Battles:</th>
                    <td align="left" width="50"><%=getTickHtml(game.isFieldBattle())%>
                    </td>

                    <th align="left" width="200">Fierce Casualties</th>
                    <td align="left" width="50"><%=getTickHtml(game.isFierceCasualties())%>
                    </td>
                </tr>

                <tr>
                    <th align="left" width="200">Boosted C&A</th>
                    <td align="left" width="50"><%=getTickHtml(game.isBoostedCAPoints())%>
                    </td>

                    <th align="left" width="200">Boosted Taxation</th>
                    <td align="left" width="50"><%=getTickHtml(game.isBoostedTaxation())%>
                    </td>

                    <th align="left" width="200">Boosted Production</th>
                    <td align="left" width="50"><%=getTickHtml(game.isBoostedProduction())%>
                    </td>

                    <th align="left" width="200">Fast Population Growth</th>
                    <td align="left" width="50"><%=getTickHtml(game.isFastPopulationGrowth())%>
                    </td>
                </tr>

                <tr>
                    <th align="left" width="200">Fast Appointment Of Commanders</th>
                    <td align="left" width="50"><%=getTickHtml(game.isFastAppointmentOfCommanders())%>
                    </td>

                    <th align="left" width="200">Extended Arrival Of Commanders</th>
                    <td align="left" width="50"><%=getTickHtml(game.isExtendedArrivalOfCommanders())%>
                    </td>

                    <th align="left" width="200">Full MPs At Colonies</th>
                    <td align="left" width="50"><%=getTickHtml(game.isFullMpsAtColonies())%>
                    </td>

                    <th align="left" width="200">Extended Espionage</th>
                    <td align="left" width="50"><%=getTickHtml(game.isExtendedEspionage())%>
                    </td>
                </tr>
                <tr>
                    <th align="left" width="400" colspan=3>Fast Ship Construction</th>
                    <td align="left" width="50"><%=getTickHtml(game.isFastShipConstruction())%>
                    </td>

                    <th align="left" width="400" colspan=3>Fast Fortress Construction</th>
                    <td align="left" width="50"><%=getTickHtml(game.isFastFortressConstruction())%>
                    </td>

                </tr>
                <tr>
                    <th align="left" width="400" colspan=3>Double Costs/Maintenance Land Forces</th>
                    <td align="left" width="50"><%=getTickHtml(game.isDoubleCostsArmy())%>
                    </td>

                    <th align="left" width="400" colspan=3>Double Costs/Maintenance Naval Forces</th>
                    <td align="left" width="50"><%=getTickHtml(game.isDoubleCostsNavy())%>
                    </td>

                </tr>
                <tr>

                </tr>
            </table>
        </smallmap>

        <mediummap>
            <h2 class="bigmap">Scenario Description</h2>
                ${game.name}
        </mediummap>
    </c:if>

    <bigmap <c:if test="${game.scenarioId == 3}">style="height: 790px;"</c:if>>
        <h2 class="bigmap">Map of Europe</h2>

        <div id="EuropeMapContainer">
            <div id="EuropeMap" class="EuropeMap">
                <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${game.turn}-1-info.png'
                     alt="Europe"
                     border="0"
                     width="940"
                     onClick="startRollingEurope();"
                     style="border-radius: 5px;"
                     title="Map of Europe" usemap="#img1map">
                <map name="img1map">
                    <area href="#img1map-area1" shape="rect" coords="0,250,50,250"/>
                    <area href="#img1map-area2" shape="rect" coords="250,250,300,250"/>
                </map>
                <div class="EuropeMapDescription" style="display: none;">${monthsFull[game.turn]}</div>

                <c:forEach var="turn" begin="0" end="${game.turn-1}">
                    <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${turn}-1-info.png'
                         alt="Europe"
                         border="0"
                         width="940"
                         style="border-radius: 5px;"
                         title="Map of Europe">

                    <div class="EuropeMapDescription" style="display: none;">${monthsFull[turn]}</div>
                </c:forEach>
            </div>
            <div class="c"></div>
            <div id="EuropeMapNavigation"></div>
            <div class="c"></div>
            <script type="text/javascript">
                var demoSlider_1;
                function startRollingEurope() {
                    if (demoSlider_1 == null) {
                        demoSlider_1 = Sliderman.slider({
                            container: 'EuropeMap', width: 940, height: 883, effects: 'fade',
                            display: {
                                autoplay: 3000,
                                loading: {
                                    background: '#000000',
                                    opacity: 0.5,
                                    image: 'http://static.eaw1805.com/images/site/slider/loading.gif'
                                },
                                buttons: {
                                    hide: true,
                                    opacity: 1,
                                    prev: {className: 'EuropeMapPrev', label: ''},
                                    next: {className: 'EuropeMapNext', label: ''}
                                },
                                description: {
                                    hide: false,
                                    background: '#000000',
                                    opacity: 0.4,
                                    height: 30,
                                    position: 'bottom'
                                },
                                navigation: {
                                    container: 'EuropeMapNavigation',
                                    label: 'http://static.eaw1805.com/images/site/slider/clear.gif'
                                }
                            }
                        });
                    }
                }
            </script>
            <div class="c"></div>
        </div>
    </bigmap>

    <c:if test="${game.scenarioId != 3}">
        <smallmap>
            <h2 class="bigmap">Map of Africa</h2>

            <div id="AfricaMapContainer">
                <div id="AfricaMap" class="AfricaMap">
                    <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${game.turn}-4-info.png'
                         alt="Africa"
                         border="0"
                         width="300"
                         onClick="startRollingAfrica();"
                         style="border-radius: 5px;"
                         title="Map of Africa" usemap="#img2map">
                    <map name="img2map">
                        <area href="#img2map-area1" shape="rect" coords="0,250,50,250"/>
                        <area href="#img2map-area2" shape="rect" coords="250,250,300,250"/>
                    </map>
                    <div class="AfricaMapDescription" style="display: none;">${monthsFull[game.turn]}</div>

                    <c:forEach var="turn" begin="0" end="${game.turn-1}">
                        <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${turn}-4-info.png'
                             alt="Africa"
                             border="0"
                             width="300"
                             style="border-radius: 5px;"
                             title="Map of Africa">

                        <div class="AfricaMapDescription" style="display: none;">${monthsFull[turn]}</div>
                    </c:forEach>
                </div>
                <div class="c"></div>
                <div id="AfricaMapNavigation"></div>
                <div class="c"></div>
                <script type="text/javascript">
                    var demoSlider_2;
                    function startRollingAfrica() {
                        if (demoSlider_2 == null) {
                            demoSlider_2 = SlidermanSmall.slider({
                                container: 'AfricaMap', width: 300, height: 225, effects: 'fade',
                                display: {
                                    autoplay: 3000,
                                    loading: {
                                        background: '#000000',
                                        opacity: 0.5,
                                        image: 'http://static.eaw1805.com/images/site/slider/loading.gif'
                                    },
                                    buttons: {
                                        hide: true,
                                        opacity: 1,
                                        prev: {className: 'AfricaMapPrev', label: ''},
                                        next: {className: 'AfricaMapNext', label: ''}
                                    },
                                    description: {
                                        hide: false,
                                        background: '#000000',
                                        opacity: 0.4,
                                        height: 30,
                                        position: 'bottom'
                                    },
                                    navigation: {
                                        container: 'AfricaMapNavigation',
                                        label: 'http://static.eaw1805.com/images/site/slider/clear.gif'
                                    }
                                }
                            });
                        }
                    }
                </script>
                <div class="c"></div>
            </div>
        </smallmap>

        <smallmap>
            <h2 class="bigmap">Map of the Caribbean</h2>

            <div id="CaribbeanMapContainer">
                <div id="CaribbeanMap" class="CaribbeanMap">
                    <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${game.turn}-2-info.png'
                         alt="Caribbean"
                         border="0"
                         width="300"
                         style="border-radius: 5px;"
                         onClick="startRollingCaribbean();"
                         title="Map of the Caribbean" usemap="#img3map">
                    <map name="img3map">
                        <area href="#img3map-area1" shape="rect" coords="0,250,50,250"/>
                        <area href="#img3map-area2" shape="rect" coords="250,250,300,250"/>
                    </map>
                    <div class="CaribbeanMapDescription" style="display: none;">${monthsFull[game.turn]}</div>

                    <c:forEach var="turn" begin="0" end="${game.turn-1}">
                        <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${turn}-2-info.png'
                             alt="Caribbean"
                             border="0"
                             width="300"
                             style="border-radius: 5px;"
                             title="Map of Caribbean">

                        <div class="CaribbeanMapDescription" style="display: none;">${monthsFull[turn]}</div>
                    </c:forEach>
                </div>
                <div class="c"></div>
                <div id="CaribbeanMapNavigation"></div>
                <div class="c"></div>
                <script type="text/javascript">
                    var demoSlider_3;
                    function startRollingCaribbean() {
                        if (demoSlider_3 == null) {
                            demoSlider_3 = SlidermanSmall.slider({
                                container: 'CaribbeanMap', width: 300, height: 225, effects: 'fade',
                                display: {
                                    autoplay: 3000,
                                    loading: {
                                        background: '#000000',
                                        opacity: 0.5,
                                        image: 'http://static.eaw1805.com/images/site/slider/loading.gif'
                                    },
                                    buttons: {
                                        hide: true,
                                        opacity: 1,
                                        prev: {className: 'CaribbeanMapPrev', label: ''},
                                        next: {className: 'CaribbeanMapNext', label: ''}
                                    },
                                    description: {
                                        hide: false,
                                        background: '#000000',
                                        opacity: 0.4,
                                        height: 30,
                                        position: 'bottom'
                                    },
                                    navigation: {
                                        container: 'CaribbeanMapNavigation',
                                        label: 'http://static.eaw1805.com/images/site/slider/clear.gif'
                                    }
                                }
                            });
                        }
                    }
                </script>
                <div class="c"></div>
            </div>

        </smallmap>

        <smallmap>
            <h2 class="bigmap">Map of Indies</h2>

            <div id="IndiesMapContainer">
                <div id="IndiesMap" class="IndiesMap">
                    <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${game.turn}-3-info.png'
                         alt="Indies"
                         border="0"
                         width="300"
                         style="border-radius: 5px;"
                         onClick="startRollingIndies();"
                         title="Map of Indies" usemap="#img4map">
                    <map name="img4map">
                        <area href="#img4map-area1" shape="rect" coords="0,250,50,250"/>
                        <area href="#img4map-area2" shape="rect" coords="250,250,300,250"/>
                    </map>
                    <div class="IndiesMapDescription" style="display: none;">${monthsFull[game.turn]}</div>

                    <c:forEach var="turn" begin="0" end="${game.turn-1}">
                        <img src='http://static.eaw1805.com/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${turn}-3-info.png'
                             alt="Indies"
                             border="0"
                             width="300"
                             style="border-radius: 5px;"
                             title="Map of Indies">

                        <div class="IndiesMapDescription" style="display: none;">${monthsFull[turn]}</div>
                    </c:forEach>
                </div>
                <div class="c"></div>
                <div id="IndiesMapNavigation"></div>
                <div class="c"></div>
                <script type="text/javascript">
                    var demoSlider_4;
                    function startRollingIndies() {
                        if (demoSlider_4 == null) {
                            demoSlider_4 = SlidermanSmall.slider({
                                container: 'IndiesMap', width: 300, height: 225, effects: 'fade',
                                display: {
                                    autoplay: 3000,
                                    loading: {
                                        background: '#000000',
                                        opacity: 0.5,
                                        image: 'http://static.eaw1805.com/images/site/slider/loading.gif'
                                    },
                                    buttons: {
                                        hide: true,
                                        opacity: 1,
                                        prev: {className: 'IndiesMapPrev', label: ''},
                                        next: {className: 'IndiesMapNext', label: ''}
                                    },
                                    description: {
                                        hide: false,
                                        background: '#000000',
                                        opacity: 0.4,
                                        height: 30,
                                        position: 'bottom'
                                    },
                                    navigation: {
                                        container: 'IndiesMapNavigation',
                                        label: 'http://static.eaw1805.com/images/site/slider/clear.gif'
                                    }
                                }
                            });
                        }
                    }
                </script>
                <div class="c"></div>
            </div>
        </smallmap>
    </c:if>

    <c:if test="${winnerList.size() > 0}">
        <nationList>
            <ul class="nationList">
                <li class="header">
                    <dl class="nationList">
                        <dt class="nationList">
                        <h2 class="bigmap">Winning Nations</h2></dt>
                        <dd class="nationList" style="width: 30pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            VPs</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 50pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Land</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 70pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Economy&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Money&nbsp;/&nbsp;Goods</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 40pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Army</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 35pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Navy</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 38pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Battles&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Total&nbsp;/&nbsp;Won</h3></dd>
                        <dd class="nationList" style="width: 65pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Casualtues&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Inflicted&nbsp;/&nbsp;Received</h3></dd>
                        <dd class="nationList" style="width: 50pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Nav&nbsp;Battles&nbsp;<br>Total&nbsp;/&nbsp;Won</h3></dd>
                        <dd class="nationList" style="width: 45pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Ships&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Sunk&nbsp;/&nbsp;Lost</h3></dd>
                        <dk class="nationList">&nbsp;</dk>
                    </dl>
                </li>
                <c:forEach items="${winnerList}" var="nation">
                    <li class="nationList">
                        <dl class="nationList">
                            <dt class="nationList"><a
                                    href='<c:url value="/scenario/${game.scenarioIdToString}/nation/${nation.id}"/>'
                                    title="Nation Info Page"
                                    style="float:right;line-height: 1.3!important;"><img
                                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-list.jpg'
                                    alt="Nation's Flag"
                                    class="toolTip"
                                    title="${nation.name}"
                                    border=0 width=231></a></dt>
                            <dd class="nationList" style="width: 30pt; font-size: 11pt; padding-top: 6pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationVP[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankVP.containsKey(nation.id)}">(${rankVP[nation.id]})</c:if></ds>
                            <dd class="nationList" style="width: 45pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationLand[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankLand.containsKey(nation.id)}">(${rankLand[nation.id]})</c:if></ds>
                            <dd class="nationList" style="padding-top: 3px; width: 70pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationEconomy[nation.id]}"/><br>
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationProduction[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;">
                                (${rankEconomy[nation.id]})<br>(${rankProduction[nation.id]})
                            </ds>
                            <dd class="nationList" style="width: 40pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationArmy[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankArmy.containsKey(nation.id)}">(${rankArmy[nation.id]})</c:if></ds>
                            <dd class="nationList" style="width: 33pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationNavy[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankNavy.containsKey(nation.id)}">(${rankNavy[nation.id]})</c:if></ds>
                            <dd class="nationList" style="padding-top: 3pt; width: 35pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationBTotal[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationBWon[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationBTotal.containsKey(nation.id)}">(${rankBTotal[nation.id]})</c:if><br><c:if
                                    test="${nationBWon.containsKey(nation.id)}">(${rankBWon[nation.id]})</c:if></ds>
                            <dd class="nationList" style="font-size: 9pt; padding-top: 1pt; width: 65pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationAKills[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationADeaths[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationAKills.containsKey(nation.id)}">(${rankAKills[nation.id]})</c:if><br><c:if
                                    test="${nationADeaths.containsKey(nation.id)}">(${rankADeaths[nation.id]})</c:if>
                            </ds>
                            <dd class="nationList" style="padding-top: 3pt; width: 35pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationNTotal[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationNWon[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationNTotal.containsKey(nation.id)}">(${rankNTotal[nation.id]})</c:if><br><c:if
                                    test="${nationNWon.containsKey(nation.id)}">(${rankNWon[nation.id]})</c:if></ds>
                            <dd class="nationList" style="font-size: 9pt; padding-top: 1pt; width:35pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationSKills[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationSDeaths[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationSKills.containsKey(nation.id)}">(${rankSKills[nation.id]})</c:if><br><c:if
                                    test="${nationSDeaths.containsKey(nation.id)}">(${rankSDeaths[nation.id]})</c:if>
                            </ds>
                            <dk class="nationList"><a
                                    href="<c:url value="/user/${nationToPlayer[nation.id].username}"/>"
                                    title="${nationToPlayer[nation.id].username}"><img
                                    src="https://secure.gravatar.com/avatar/${nationToPlayer[nation.id].emailEncoded}?s=33&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                    alt="${nationToPlayer[nation.id].username}" height="33"
                                    width="33"
                                    style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                    title="${nationToPlayer[nation.id].username}"
                                    class="tooltip"></a></dk>
                        </dl>
                    </li>
                </c:forEach>
            </ul>
        </nationList>
    </c:if>

    <c:if test="${coWinnerList.size() > 0}">
        <nationList>
            <ul class="nationList">
                <li class="header">
                    <dl class="nationList">
                        <dt class="nationList">
                        <h2 class="bigmap">Co-Winning Nations</h2></dt>
                        <dd class="nationList" style="width: 30pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            VPs</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 50pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Land</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 70pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Economy&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Money&nbsp;/&nbsp;Goods</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 40pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Army</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 35pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Navy</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 38pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Battles&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Total&nbsp;/&nbsp;Won</h3></dd>
                        <dd class="nationList" style="width: 65pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Casualtues&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Inflicted&nbsp;/&nbsp;Received</h3></dd>
                        <dd class="nationList" style="width: 50pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Nav&nbsp;Battles&nbsp;<br>Total&nbsp;/&nbsp;Won</h3></dd>
                        <dd class="nationList" style="width: 45pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Ships&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Sunk&nbsp;/&nbsp;Lost</h3></dd>
                        <dk class="nationList">&nbsp;</dk>
                    </dl>
                </li>
                <c:forEach items="${coWinnerList}" var="nation">
                    <li class="nationList">
                        <dl class="nationList">
                            <dt class="nationList"><a
                                    href='<c:url value="/scenario/${game.scenarioIdToString}/nation/${nation.id}"/>'
                                    title="Nation Info Page"
                                    style="float:right;line-height: 1.3!important;"><img
                                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-list.jpg'
                                    alt="Nation's Flag"
                                    class="toolTip"
                                    title="${nation.name}"
                                    border=0 width=231></a></dt>
                            <dd class="nationList" style="width: 30pt; font-size: 11pt; padding-top: 6pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationVP[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankVP.containsKey(nation.id)}">(${rankVP[nation.id]})</c:if></ds>
                            <dd class="nationList" style="width: 45pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationLand[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankLand.containsKey(nation.id)}">(${rankLand[nation.id]})</c:if></ds>
                            <dd class="nationList" style="padding-top: 3px; width: 70pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationEconomy[nation.id]}"/><br>
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationProduction[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;">
                                (${rankEconomy[nation.id]})<br>(${rankProduction[nation.id]})
                            </ds>
                            <dd class="nationList" style="width: 40pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationArmy[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankArmy.containsKey(nation.id)}">(${rankArmy[nation.id]})</c:if></ds>
                            <dd class="nationList" style="width: 33pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationNavy[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankNavy.containsKey(nation.id)}">(${rankNavy[nation.id]})</c:if></ds>
                            <dd class="nationList" style="padding-top: 3pt; width: 35pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationBTotal[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationBWon[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationBTotal.containsKey(nation.id)}">(${rankBTotal[nation.id]})</c:if><br><c:if
                                    test="${nationBWon.containsKey(nation.id)}">(${rankBWon[nation.id]})</c:if></ds>
                            <dd class="nationList" style="font-size: 9pt; padding-top: 1pt; width: 65pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationAKills[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationADeaths[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationAKills.containsKey(nation.id)}">(${rankAKills[nation.id]})</c:if><br><c:if
                                    test="${nationADeaths.containsKey(nation.id)}">(${rankADeaths[nation.id]})</c:if>
                            </ds>
                            <dd class="nationList" style="padding-top: 3pt; width: 35pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationNTotal[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationNWon[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationNTotal.containsKey(nation.id)}">(${rankNTotal[nation.id]})</c:if><br><c:if
                                    test="${nationNWon.containsKey(nation.id)}">(${rankNWon[nation.id]})</c:if></ds>
                            <dd class="nationList" style="font-size: 9pt; padding-top: 1pt; width:35pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationSKills[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationSDeaths[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationSKills.containsKey(nation.id)}">(${rankSKills[nation.id]})</c:if><br><c:if
                                    test="${nationSDeaths.containsKey(nation.id)}">(${rankSDeaths[nation.id]})</c:if>
                            </ds>
                            <dk class="nationList"><a
                                    href="<c:url value="/user/${nationToPlayer[nation.id].username}"/>"
                                    title="${nationToPlayer[nation.id].username}"><img
                                    src="https://secure.gravatar.com/avatar/${nationToPlayer[nation.id].emailEncoded}?s=33&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                    alt="${nationToPlayer[nation.id].username}" height="33"
                                    style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                    width="33"
                                    title="${nationToPlayer[nation.id].username}"
                                    class="tooltip"></a></dk>
                        </dl>
                    </li>
                </c:forEach>
            </ul>
        </nationList>
    </c:if>

    <c:if test="${runnerUpList.size() > 0}">
        <nationList>
            <ul class="nationList">
                <li class="header">
                    <dl class="nationList">
                        <dt class="nationList">
                        <h2 class="bigmap">Runner-Up Nations</h2></dt>
                        <dd class="nationList" style="width: 30pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            VPs</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 50pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Land</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 70pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Economy&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Money&nbsp;/&nbsp;Goods</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 40pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Army</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 35pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Navy</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 38pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Battles&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Total&nbsp;/&nbsp;Won</h3></dd>
                        <dd class="nationList" style="width: 65pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Casualtues&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Inflicted&nbsp;/&nbsp;Received</h3></dd>
                        <dd class="nationList" style="width: 50pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Nav&nbsp;Battles&nbsp;<br>Total&nbsp;/&nbsp;Won</h3></dd>
                        <dd class="nationList" style="width: 45pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Ships&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Sunk&nbsp;/&nbsp;Lost</h3></dd>
                        <dk class="nationList">&nbsp;</dk>
                    </dl>
                </li>
                <c:forEach items="${runnerUpList}" var="nation">
                    <li class="nationList">
                        <dl class="nationList">
                            <dt class="nationList"><a
                                    href='<c:url value="/scenario/${game.scenarioIdToString}/nation/${nation.id}"/>'
                                    title="Nation Info Page"
                                    style="float:right;line-height: 1.3!important;"><img
                                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-list.jpg'
                                    alt="Nation's Flag"
                                    class="toolTip"
                                    title="${nation.name}"
                                    border=0 width=231></a></dt>
                            <dd class="nationList" style="width: 30pt; font-size: 11pt; padding-top: 6pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationVP[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankVP.containsKey(nation.id)}">(${rankVP[nation.id]})</c:if></ds>
                            <dd class="nationList" style="width: 45pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationLand[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankLand.containsKey(nation.id)}">(${rankLand[nation.id]})</c:if></ds>
                            <dd class="nationList" style="padding-top: 3px; width: 70pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationEconomy[nation.id]}"/><br>
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationProduction[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;">
                                (${rankEconomy[nation.id]})<br>(${rankProduction[nation.id]})
                            </ds>
                            <dd class="nationList" style="width: 40pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationArmy[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankArmy.containsKey(nation.id)}">(${rankArmy[nation.id]})</c:if></ds>
                            <dd class="nationList" style="width: 33pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationNavy[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankNavy.containsKey(nation.id)}">(${rankNavy[nation.id]})</c:if></ds>
                            <dd class="nationList" style="padding-top: 3pt; width: 35pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationBTotal[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationBWon[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationBTotal.containsKey(nation.id)}">(${rankBTotal[nation.id]})</c:if><br><c:if
                                    test="${nationBWon.containsKey(nation.id)}">(${rankBWon[nation.id]})</c:if></ds>
                            <dd class="nationList" style="font-size: 9pt; padding-top: 1pt; width: 65pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationAKills[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationADeaths[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationAKills.containsKey(nation.id)}">(${rankAKills[nation.id]})</c:if><br><c:if
                                    test="${nationADeaths.containsKey(nation.id)}">(${rankADeaths[nation.id]})</c:if>
                            </ds>
                            <dd class="nationList" style="padding-top: 3pt; width: 35pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationNTotal[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationNWon[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationNTotal.containsKey(nation.id)}">(${rankNTotal[nation.id]})</c:if><br><c:if
                                    test="${nationNWon.containsKey(nation.id)}">(${rankNWon[nation.id]})</c:if></ds>
                            <dd class="nationList" style="font-size: 9pt; padding-top: 1pt; width:35pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationSKills[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationSDeaths[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationSKills.containsKey(nation.id)}">(${rankSKills[nation.id]})</c:if><br><c:if
                                    test="${nationSDeaths.containsKey(nation.id)}">(${rankSDeaths[nation.id]})</c:if>
                            </ds>
                            <dk class="nationList"><a
                                    href="<c:url value="/user/${nationToPlayer[nation.id].username}"/>"
                                    title="${nationToPlayer[nation.id].username}"><img
                                    src="https://secure.gravatar.com/avatar/${nationToPlayer[nation.id].emailEncoded}?s=33&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                    style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                    alt="${nationToPlayer[nation.id].username}" height="33"
                                    width="33"
                                    title="${nationToPlayer[nation.id].username}"
                                    class="tooltip"></a></dk>
                        </dl>
                    </li>
                </c:forEach>
            </ul>
        </nationList>
    </c:if>

    <c:if test="${nationList.size() > 0}">
        <nationList>
            <ul class="nationList">
                <li class="header">
                    <dl class="nationList">
                        <dt class="nationList">
                        <h2 class="bigmap"><c:choose>
                            <c:when test="${winnerList.size() > 0}">
                                Standing Nations
                            </c:when>
                            <c:otherwise>
                                Active Nations
                            </c:otherwise>
                        </c:choose>
                        </h2></dt>
                        <dd class="nationList" style="width: 30pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            VPs</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 50pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Land</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 70pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Economy&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Money&nbsp;/&nbsp;Goods</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 40pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Army</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 35pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Navy</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 38pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Battles&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Total&nbsp;/&nbsp;Won</h3></dd>
                        <dd class="nationList" style="width: 65pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Casualtues&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Inflicted&nbsp;/&nbsp;Received</h3></dd>
                        <dd class="nationList" style="width: 50pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Nav&nbsp;Battles&nbsp;<br>Total&nbsp;/&nbsp;Won</h3></dd>
                        <dd class="nationList" style="width: 45pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Ships&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Sunk&nbsp;/&nbsp;Lost</h3></dd>
                        <dk class="nationList">&nbsp;</dk>
                    </dl>
                </li>
                <c:forEach items="${nationList}" var="nation">
                    <li class="nationList">
                        <dl class="nationList">
                            <dt class="nationList"><a
                                    href='<c:url value="/scenario/${game.scenarioIdToString}/nation/${nation.id}"/>'
                                    title="Nation Info Page"
                                    style="float:right;line-height: 1.3!important;"><img
                                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-list.jpg'
                                    alt="Nation's Flag"
                                    class="toolTip"
                                    title="${nation.name}"
                                    border=0 width=231></a></dt>
                            <dd class="nationList" style="width: 30pt; font-size: 11pt; padding-top: 6pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationVP[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankVP.containsKey(nation.id)}">(${rankVP[nation.id]})</c:if></ds>
                            <dd class="nationList" style="width: 45pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationLand[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankLand.containsKey(nation.id)}">(${rankLand[nation.id]})</c:if></ds>
                            <dd class="nationList" style="padding-top: 3px; width: 70pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationEconomy[nation.id]}"/><br>
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationProduction[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;">
                                (${rankEconomy[nation.id]})<br>(${rankProduction[nation.id]})
                            </ds>
                            <dd class="nationList" style="width: 40pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationArmy[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankArmy.containsKey(nation.id)}">(${rankArmy[nation.id]})</c:if></ds>
                            <dd class="nationList" style="width: 33pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationNavy[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankNavy.containsKey(nation.id)}">(${rankNavy[nation.id]})</c:if></ds>
                            <dd class="nationList" style="padding-top: 3pt; width: 35pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationBTotal[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationBWon[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationBTotal.containsKey(nation.id)}">(${rankBTotal[nation.id]})</c:if><br><c:if
                                    test="${nationBWon.containsKey(nation.id)}">(${rankBWon[nation.id]})</c:if></ds>
                            <dd class="nationList" style="font-size: 9pt; padding-top: 1pt; width: 65pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationAKills[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationADeaths[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationAKills.containsKey(nation.id)}">(${rankAKills[nation.id]})</c:if><br><c:if
                                    test="${nationADeaths.containsKey(nation.id)}">(${rankADeaths[nation.id]})</c:if>
                            </ds>
                            <dd class="nationList" style="padding-top: 3pt; width: 35pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationNTotal[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationNWon[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationNTotal.containsKey(nation.id)}">(${rankNTotal[nation.id]})</c:if><br><c:if
                                    test="${nationNWon.containsKey(nation.id)}">(${rankNWon[nation.id]})</c:if></ds>
                            <dd class="nationList" style="font-size: 9pt; padding-top: 1pt; width:35pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationSKills[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationSDeaths[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationSKills.containsKey(nation.id)}">(${rankSKills[nation.id]})</c:if><br><c:if
                                    test="${nationSDeaths.containsKey(nation.id)}">(${rankSDeaths[nation.id]})</c:if>
                            </ds>
                            <dk class="nationList"><a
                                    href="<c:url value="/user/${nationToPlayer[nation.id].username}"/>"
                                    title="${nationToPlayer[nation.id].username}"><img
                                    src="https://secure.gravatar.com/avatar/${nationToPlayer[nation.id].emailEncoded}?s=33&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                    style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                    alt="${nationToPlayer[nation.id].username}" height="33"
                                    width="33"
                                    title="${nationToPlayer[nation.id].username}"
                                    class="tooltip"></a></dk>
                        </dl>
                    </li>
                </c:forEach>
            </ul>
        </nationList>
    </c:if>

    <c:if test="${nationFree.size() > 0}">
        <nationList>
            <ul class="nationList">
                <li class="header">
                    <dl class="nationList">
                        <dt class="nationList">
                        <h2 class="bigmap"><c:choose>
                            <c:when test="${winnerList.size() > 0}">
                                Unrulled Nations
                            </c:when>
                            <c:otherwise>
                                Free Nations
                            </c:otherwise>
                        </c:choose></h2></dt>
                        <dd class="nationList" style="width: 30pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            VPs</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 50pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Land</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 70pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Economy&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Money&nbsp;/&nbsp;Goods</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 40pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Army</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 35pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Navy</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 38pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Battles&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Total&nbsp;/&nbsp;Won</h3></dd>
                        <dd class="nationList" style="width: 65pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Casualtues&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Inflicted&nbsp;/&nbsp;Received</h3></dd>
                        <dd class="nationList" style="width: 50pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Nav&nbsp;Battles&nbsp;<br>Total&nbsp;/&nbsp;Won</h3></dd>
                        <dd class="nationList" style="width: 45pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Ships&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Sunk&nbsp;/&nbsp;Lost</h3></dd>
                        <dk class="nationList">&nbsp;</dk>
                    </dl>
                </li>
                <c:forEach items="${nationFree}" var="nation">
                    <li class="nationList">
                        <dl class="nationList">
                            <dt class="nationList"><a
                                    href='<c:url value="/scenario/${game.scenarioIdToString}/nation/${nation.id}"/>'
                                    title="Nation Info Page"
                                    style="float:right;line-height: 1.3!important;"><img
                                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-list.jpg'
                                    alt="Nation's Flag"
                                    class="toolTip"
                                    title="${nation.name}"
                                    border=0 width=231></a></dt>
                            <dd class="nationList" style="width: 30pt; font-size: 11pt; padding-top: 6pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationVP[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankVP.containsKey(nation.id)}">(${rankVP[nation.id]})</c:if></ds>
                            <dd class="nationList" style="width: 45pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationLand[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankLand.containsKey(nation.id)}">(${rankLand[nation.id]})</c:if></ds>
                            <dd class="nationList" style="padding-top: 3px; width: 70pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationEconomy[nation.id]}"/><br>
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationProduction[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;">
                                (${rankEconomy[nation.id]})<br>(${rankProduction[nation.id]})
                            </ds>
                            <dd class="nationList" style="width: 40pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationArmy[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankArmy.containsKey(nation.id)}">(${rankArmy[nation.id]})</c:if></ds>
                            <dd class="nationList" style="width: 33pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationNavy[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankNavy.containsKey(nation.id)}">(${rankNavy[nation.id]})</c:if></ds>
                            <dd class="nationList" style="padding-top: 3pt; width: 35pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationBTotal[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationBWon[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationBTotal.containsKey(nation.id)}">(${rankBTotal[nation.id]})</c:if><br><c:if
                                    test="${nationBWon.containsKey(nation.id)}">(${rankBWon[nation.id]})</c:if></ds>
                            <dd class="nationList" style="font-size: 9pt; padding-top: 1pt; width: 65pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationAKills[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationADeaths[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationAKills.containsKey(nation.id)}">(${rankAKills[nation.id]})</c:if><br><c:if
                                    test="${nationADeaths.containsKey(nation.id)}">(${rankADeaths[nation.id]})</c:if>
                            </ds>
                            <dd class="nationList" style="padding-top: 3pt; width: 35pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationNTotal[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationNWon[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationNTotal.containsKey(nation.id)}">(${rankNTotal[nation.id]})</c:if><br><c:if
                                    test="${nationNWon.containsKey(nation.id)}">(${rankNWon[nation.id]})</c:if></ds>
                            <dd class="nationList" style="font-size: 9pt; padding-top: 1pt; width:35pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationSKills[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationSDeaths[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationSKills.containsKey(nation.id)}">(${rankSKills[nation.id]})</c:if><br><c:if
                                    test="${nationSDeaths.containsKey(nation.id)}">(${rankSDeaths[nation.id]})</c:if>
                            </ds>

                            <c:if test="${!userHasActiveCountry && winnerList.size() == 0 && !game.privateGame}">
                                <dk class="nationList">
                                    <button class="pickup" title="Pickup ${nation.name}"
                                            onclick="openPickupForm('${game.scenarioIdToString}', '${gameId}' , '${nation.id}','${nation.name}','${nation.cost}')"></button>
                                </dk>
                            </c:if>
                        </dl>
                    </li>
                </c:forEach>
            </ul>
        </nationList>
    </c:if>

    <c:if test="${nationDead.size() > 0}">
        <nationList>
            <ul class="nationList">
                <li class="header">
                    <dl class="nationList">
                        <dt class="nationList">
                        <h2 class="bigmap">Dead Nations</h2></dt>
                        <dd class="nationList" style="width: 30pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            VPs</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 50pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Land</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 70pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Economy&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Money&nbsp;/&nbsp;Goods</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 40pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Army</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 35pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            Navy</h3></dd>
                        <ds class="nationList">&nbsp;&nbsp;</ds>
                        <dd class="nationList" style="width: 38pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Battles&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Total&nbsp;/&nbsp;Won</h3></dd>
                        <dd class="nationList" style="width: 65pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Casualtues&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Inflicted&nbsp;/&nbsp;Received</h3></dd>
                        <dd class="nationList" style="width: 50pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Nav&nbsp;Battles&nbsp;<br>Total&nbsp;/&nbsp;Won</h3></dd>
                        <dd class="nationList" style="width: 45pt;"><h3 class="bigmap"
                                                                        style="text-align: right; font-size: 8pt; font-weight: normal; margin-top: -9pt;">
                            Ships&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Sunk&nbsp;/&nbsp;Lost</h3></dd>
                        <dk class="nationList">&nbsp;</dk>
                    </dl>
                </li>
                <c:forEach items="${nationDead}" var="nation">
                    <li class="nationList">
                        <dl class="nationList">
                            <dt class="nationList"><a
                                    href='<c:url value="/scenario/${game.scenarioIdToString}/nation/${nation.id}"/>'
                                    title="Nation Info Page"
                                    style="float:right;line-height: 1.3!important;"><img
                                    src='http://static.eaw1805.com/images/nations/nation-${nation.id}-list.jpg'
                                    alt="Nation's Flag"
                                    class="toolTip"
                                    title="${nation.name}"
                                    border=0 width=231></a></dt>
                            <dd class="nationList" style="width: 30pt; font-size: 11pt; padding-top: 6pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationVP[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankVP.containsKey(nation.id)}">(${rankVP[nation.id]})</c:if></ds>
                            <dd class="nationList" style="width: 45pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationLand[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankLand.containsKey(nation.id)}">(${rankLand[nation.id]})</c:if></ds>
                            <dd class="nationList" style="padding-top: 3px; width: 70pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationEconomy[nation.id]}"/><br>
                                <fmt:formatNumber type="number" maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationProduction[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;">
                                (${rankEconomy[nation.id]})<br>(${rankProduction[nation.id]})
                            </ds>
                            <dd class="nationList" style="width: 40pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationArmy[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankArmy.containsKey(nation.id)}">(${rankArmy[nation.id]})</c:if></ds>
                            <dd class="nationList" style="width: 33pt;"><fmt:formatNumber type="number"
                                                                                          maxFractionDigits="0"
                                                                                          groupingUsed="true"
                                                                                          value="${nationNavy[nation.id]}"/></dd>
                            <ds class="nationList"><c:if
                                    test="${rankNavy.containsKey(nation.id)}">(${rankNavy[nation.id]})</c:if></ds>
                            <dd class="nationList" style="padding-top: 3pt; width: 35pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationBTotal[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationBWon[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationBTotal.containsKey(nation.id)}">(${rankBTotal[nation.id]})</c:if><br><c:if
                                    test="${nationBWon.containsKey(nation.id)}">(${rankBWon[nation.id]})</c:if></ds>
                            <dd class="nationList" style="font-size: 9pt; padding-top: 1pt; width: 65pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationAKills[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationADeaths[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationAKills.containsKey(nation.id)}">(${rankAKills[nation.id]})</c:if><br><c:if
                                    test="${nationADeaths.containsKey(nation.id)}">(${rankADeaths[nation.id]})</c:if>
                            </ds>
                            <dd class="nationList" style="padding-top: 3pt; width: 35pt;"><fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value="${nationNTotal[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationNWon[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationNTotal.containsKey(nation.id)}">(${rankNTotal[nation.id]})</c:if><br><c:if
                                    test="${nationNWon.containsKey(nation.id)}">(${rankNWon[nation.id]})</c:if></ds>
                            <dd class="nationList" style="font-size: 9pt; padding-top: 1pt; width:35pt;">
                                <fmt:formatNumber
                                        type="number"
                                        maxFractionDigits="0"
                                        groupingUsed="true"
                                        value="${nationSKills[nation.id]}"/><br>
                                <fmt:formatNumber type="number"
                                                  maxFractionDigits="0"
                                                  groupingUsed="true"
                                                  value="${nationSDeaths[nation.id]}"/></dd>
                            <ds class="nationList" style="padding-top: 4.5px;"><c:if
                                    test="${nationSKills.containsKey(nation.id)}">(${rankSKills[nation.id]})</c:if><br><c:if
                                    test="${nationSDeaths.containsKey(nation.id)}">(${rankSDeaths[nation.id]})</c:if>
                            </ds>
                            <dk class="nationList"><a
                                    href="<c:url value="/user/${nationToPlayer[nation.id].username}"/>"
                                    title="${nationToPlayer[nation.id].username}"><img
                                    src="https://secure.gravatar.com/avatar/${nationToPlayer[nation.id].emailEncoded}?s=33&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                    style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                    alt="${nationToPlayer[nation.id].username}" height="33"
                                    width="33"
                                    title="${nationToPlayer[nation.id].username}"
                                    class="tooltip"></a></dk>
                        </dl>
                    </li>
                </c:forEach>
            </ul>
        </nationList>
    </c:if>

    <c:if test="${game.turn > 0}">
        <bigmap style="height: 635px;">
            <h2 class="bigmap">Victory Points</h2>

            <div id="container-vp" style="width: 940px; height: 600px"></div>
        </bigmap>
    </c:if>

    <c:if test="${game.turn > 0}">
        <bigmap style="height: 635px;">
            <h2 class="bigmap">World Domination</h2>

            <div id="container-domination" style="width: 940px; height: 600px"></div>
        </bigmap>
    </c:if>

    <c:if test="${fn:length(news) > 0}">
        <news style="width: 948px;">
            <h1>News from Around the World</h1>
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

    <%--<c:if test="${game.turn > 0}">
    <news style="width: 948px;">
        <h2 class="bigmap">Timeline</h2>
        <!-- BEGIN Timeline Embed -->
        <div id="timeline-embed"></div>
        <script type="text/javascript">
            var timeline_config = {
                width: "100%",
                height: "100%",
                source: '<c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/timeline"/>'
            }
        </script>
        <script type="text/javascript" src="<c:url value="/js/timeline_js/storyjs-embed.js"/>"></script>
        <!-- END Timeline Embed-->
    </news>
</c:if>--%>

    <c:if test="${game.ended == true}">
        <bigmap style="height: 635px;">
            <h2 class="bigmap">Available Money</h2>

            <div id="container-money" style="width: 940px; height: 600px"></div>
        </bigmap>

        <bigmap style="height: 635px;">
            <h2 class="bigmap">Land Forces</h2>

            <div id="container-army" style="width: 940px; height: 600px"></div>
        </bigmap>

        <bigmap style="height: 635px;">
            <h2 class="bigmap">Casualties Inflicted in Land Battles</h2>

            <div id="container-armykills" style="width: 940px; height: 600px"></div>
        </bigmap>

        <bigmap style="height: 635px;">
            <h2 class="bigmap">Naval Forces</h2>

            <div id="container-navy" style="width: 940px; height: 600px"></div>
        </bigmap>

        <bigmap style="height: 635px;">
            <h2 class="bigmap">Ships Sunk in Naval Battles</h2>

            <div id="container-navysinks" style="width: 940px; height: 600px"></div>
        </bigmap>

        <bigmap style="height: 635px;">
            <h2 class="bigmap">Land in Europe</h2>

            <div id="container-land-1" style="width: 940px; height: 600px"></div>
        </bigmap>

        <bigmap style="height: 635px;">
            <h2 class="bigmap">Land in Africa</h2>

            <div id="container-land-4" style="width: 940px; height: 600px"></div>
        </bigmap>

        <bigmap style="height: 635px;">
            <h2 class="bigmap">Land in Caribbean</h2>

            <div id="container-land-2" style="width: 940px; height: 600px"></div>
        </bigmap>

        <bigmap style="height: 635px;">
            <h2 class="bigmap">Land in Indies</h2>

            <div id="container-land-3" style="width: 940px; height: 600px"></div>
        </bigmap>
    </c:if>

    <security:authorize ifNotGranted="ROLE_ANONYMOUS">
        <c:if test="${user.userId != 2 && (user.userId == game.userId || user.userType == 3)}">

            <mediummap>
                <h2 class="bigmap">Game Master Information: Scenario Description</h2>

                <form method="post"
                      action="<c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/update/description"/>">
                    <label class='save-setting-btn'
                           style="clear:both;margin-right:5px;width: 36px; height: 36px; float: right; margin-top:-25px;"
                           title="Update game description">
                        <input class="tick-submit" type="submit" value="" style="clear: both!important;"/>
                    </label>
                    <br>

                    <div>
                        <div style="height: 100%; width: 100%; border-radius: 8px 8px 8px 8px;overflow: hidden;">
                            <textarea name="name" id="name" style="width:100%; height: 205px;">${game.name}</textarea>
                        </div>

                    </div>
                </form>
            </mediummap>
            <script type="text/javascript">
                new nicEditor({fullPanel: true}).panelInstance('name', {hasPanel: true});
                function updateHTMLContent() {
                    nicEditors.findEditor("name").saveContent();
                }
            </script>

            <infomap>
                <h2 class="bigmap">Game Master Information: Players &amp; Positions</h2>
                <ul class="nationList">
                    <li class="header">

                        <dl class="nationList">
                            <dt class="nationList">
                            <h2 class="bigmap">Nations</h2>
                            </dt>

                            <dd class="nationList" style="width: 63pt;"><h3 class="bigmap"
                                                                            style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                                Players</h3></dd>
                            <ds class="nationList">&nbsp;&nbsp;</ds>
                            <dd class="nationList" style="width: 102pt;"><h3 class="bigmap"
                                                                             style="text-align: right; font-size: 12pt; font-weight: normal; margin-top: -5pt;">
                            </h3></dd>
                            <ds class="nationList">&nbsp;&nbsp;</ds>
                        </dl>
                    </li>
                    <c:forEach items="${nationListComplete}" var="nation">
                        <li class="nationList" style="width: 500px;">
                            <dl class="nationList">
                                <dt class="nationList"><a
                                        href='<c:url value="/scenario/1802/nation/${nation.id}"/>'
                                        title="Nation Info Page"
                                        style="float:right;line-height: 1.3!important;"><img
                                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-list.jpg'
                                        alt="Nation's Flag"
                                        class="toolTip"
                                        title="${nation.name}"
                                        border=0 width=231></a></dt>

                                <dd class="nationList"
                                    style="width: 112pt; font-size: 11pt; padding-top: 0pt; text-align: left;">
                                    <input type="text" class="user_position" id="player_${nation.id}"
                                           name="player_${nation.id}"
                                           style="width: 140px;
                                           <c:if test="${nationToPlayer[nation.id].userId != 2}">display:none;</c:if>"
                                           onkeyup="updateNationUser(this);"/>
                                    <img
                                            src="https://secure.gravatar.com/avatar/${nationToPlayer[nation.id].emailEncoded}?s=33&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                            id="avatar_${nation.id}"
                                            style="<c:if
                                                    test="${nationToPlayer[nation.id].userId == 2}">display:none;</c:if>margin-left:4px; border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                            alt="${nationToPlayer[nation.id].username}" height="33"
                                            width="33"
                                            title="Click to select another user"
                                            class="tooltip"
                                    <c:if test="${!nationToUserGame[nation.id].accepted}">
                                            onclick="clearNationUser(this);"
                                    </c:if>
                                            >
                            <span id="usernameGoesHere_${nation.id}"><c:if
                                    test="${nationToPlayer[nation.id].userId != 2}">${nationToPlayer[nation.id].username}</c:if></span>
                                </dd>
                                <ds class="nationList"></ds>
                                <dd class="nationList" style="width: 45pt;">
                                    <input type="submit" value="" onclick="sendInvitation(${nation.id})"
                                           class="send-message-input" id="sendButton_${nation.id}"
                                           style="margin-top: -11px ! important; height: 32px ! important; background-size: auto 34px ! important; margin-right: -50px ! important;
                                           <c:if test="${nationToPlayer[nation.id].userId != 2}">display:none!important;</c:if>
                                                   " title="send invitation">
                                    <input type="submit" value="" onclick="cancelInvitation(${nation.id})"
                                           class="send-message-input" id="cancelIndicator_${nation.id}"
                                           style="margin-top: -11px ! important; height: 32px ! important; background-image: url('http://static.eaw1805.com/images/buttons/ButCancelOff.png') !important; height: 32px ! important; background-size: auto 34px ! important; margin-right: -50px ! important; width:81px!important;
                                           <c:if test="${nationToPlayer[nation.id].userId == 2 || nationToUserGame[nation.id].accepted}">display:none!important;</c:if>
                                                   " title="free position">
                                    <input type="submit" value="" class="send-message-input"
                                           id="sentIndicator_${nation.id}"
                                           style="margin-top: -11px ! important; height: 32px ! important; background-image: url('http://static.eaw1805.com/images/buttons/ButAcceptOff.png') !important; height: 32px ! important; background-size: auto 34px ! important; margin-right: -50px ! important; width:81px!important; display: none !important;"
                                           title="invitation sent">
                                </dd>
                            </dl>
                        </li>
                    </c:forEach>
                </ul>
                <c:if test="${game.waitingForPlayers}">
                    <input class="launchGameButton" title="Launch custom game" type="button"
                           onclick="window.location='<c:url
                                   value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/launch"/>';"
                           value="" style="position: absolute; top: 6px; left: 632px;"/>
                    <input class="cancelGameButton" title="Cancel custom game" type="button"
                           onclick="window.location='<c:url
                                   value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/cancel"/>';"
                           value="" style="position: absolute; top: 80px; left: 645px;"/>
                </c:if>
            </infomap>
        </c:if>

        <c:if test="${user.userType == 3}">
            <jsp:useBean id="stats" scope="request" type="java.util.List<com.eaw1805.data.model.EngineProcess>"/>
            <jsp:useBean id="months" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
            <jsp:useBean id="nationListComplete" scope="request" type="java.util.List<com.eaw1805.data.model.Nation>"/>
            <jsp:useBean id="nationOrders" scope="request"
                         type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, java.math.BigInteger>>"/>
            <jsp:useBean id="positionData" scope="request"
                         type="java.util.Map<java.lang.Integer, java.util.List<com.eaw1805.data.model.UserGame>>"/>
            <jsp:useBean id="userData" scope="request"
                         type="java.util.Map<java.lang.Integer, com.eaw1805.data.model.User>"/>
            <infomap>
                <h2 class="bigmap">Game Master Information: Engine</h2>

                <div style="float: right; margin-right: 2px; margin-top: -30px;">
                    <a href="<c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/edit"/>"
                       class="minibutton"
                       title="Edit game information">
                        <img src="http://static.eaw1805.com/images/layout/buttons/ButSpyReportsOff.png"
                             alt="Edit game information"
                             title="Edit game information"
                             onmouseover="this.src='http://static.eaw1805.com/images/layout/buttons/ButSpyReportsHover.png'"
                             onmouseout="this.src='http://static.eaw1805.com/images/layout/buttons/ButSpyReportsOff.png'"
                             border=0></a>
                </div>
                <table border="0">
                    <tr>
                        <th align="left">Turn&nbsp;&nbsp;</th>
                        <th align="left">Date&nbsp;&nbsp;</th>
                        <th align="left">Duration (sec)&nbsp;&nbsp;</th>
                        <th align="left">Log file&nbsp;&nbsp;</th>
                    </tr>
                    <c:forEach items="${stats}" var="statLine">
                        <tr>
                            <td><c:out value="${statLine.turn}"/>&nbsp;&nbsp;</td>
                            <td><c:out value="${statLine.dateStart}"/>&nbsp;&nbsp;</td>
                            <td><c:out value="${statLine.duration}"/>&nbsp;&nbsp;</td>
                            <td><c:if test="${statLine.buildNumber > 0}">
                                <c:choose>
                                    <c:when test="${game.scenarioId==2}">
                                        <a href='http://gamekeeper.oplongames.com:8098/jenkins/job/Engine (Scenario 1805)/${statLine.buildNumber}/console'>view
                                            log
                                            file</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href='http://gamekeeper.oplongames.com:8098/jenkins/job/Engine (Scenario 1802)/${statLine.buildNumber}/console'>view
                                            log
                                            file</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>

                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </infomap>

            <c:set var="minTurn" value="0"/>
            <c:if test="${game.turn > 24}">
                <c:set var="minTurn" value="${game.turn - 24}"/>
            </c:if>

            <infomap>
                <h2 class="bigmap">Game Master Information: Positions History</h2>
                <ul class="nationList">
                    <li class="header">
                        <dl class="nationList">
                            <dt class="nationList" style="width: 30pt;">
                            <h2 class="bigmap">&nbsp;</h2></dt>
                            <c:forEach var="turn" begin="${minTurn}" end="${game.turn}">
                                <dd class="nationList" style="width: 25pt;"><h3 class="bigmap"
                                                                                style="text-align: right; font-size: 10pt; font-weight: normal; margin-top: -5pt;">
                                        ${months[turn]}</h3></dd>
                            </c:forEach>

                            <dk class="nationList">&nbsp;</dk>
                        </dl>
                    </li>
                    <c:forEach items="${nationListComplete}" var="nation">
                        <li class="nationList">
                            <dl class="nationList">
                                <dt class="nationList" style="width: 30pt;"><a
                                        href='<c:url value="/scenario/${game.scenarioIdToString}/game/${game.gameId}/nation/${nation.id}/edit"/>'><img
                                        src='http://static.eaw1805.com/images/nations/nation-${nation.id}-36.png'
                                        alt="Nation's Flag"
                                        class="toolTip"
                                        title="${nation.name}"
                                        border=0 height=33></a></dt>
                                <c:forEach var="turn" begin="${minTurn}" end="${game.turn}">
                                    <dd class="nationList" style="width: 25pt;">${nationOrders[turn][nation.id]}</dd>
                                </c:forEach>
                                <dk class="nationList"><a
                                        href="<c:url value="/user/${nationToPlayer[nation.id].username}"/>"
                                        title="${nationToPlayer[nation.id].username}"><img
                                        src="https://secure.gravatar.com/avatar/${nationToPlayer[nation.id].emailEncoded}?s=33&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                        alt="${nationToPlayer[nation.id].username}" height="33"
                                        style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                        width="33"
                                        title="${nationToPlayer[nation.id].username}"
                                        class="tooltip"></a></dk>
                            </dl>
                            <c:if test="${fn:length(positionData[nation.id]) > 0}">
                                <dl class="nationList" style="clear:both; height: 18px;">
                                    <dt class="nationList" style="width: 30pt; height: 18px;">&nbsp;</dt>
                                    <c:forEach var="position" items="${positionData[nation.id]}">
                                        <dd class="nationList" style="width: 60pt; height: 18px; padding-top: 2px;">
                                            <a href="<c:url value="/user/${userData[position.userId].username}"/>"
                                               title="${userData[position.userId].username}"><img
                                                    src="https://secure.gravatar.com/avatar/${userData[position.userId].emailEncoded}?s=33&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                                    style="border-radius: 4px; -moz-border-radius: 4px; -webkit-border-radius: 4px;"
                                                    alt="${userData[position.userId].username}" height="16"
                                                    width="16"
                                                    title="${userData[position.userId].username}"
                                                    class="tooltip"></a>
                                                ${months[position.turnPickUp]} - ${months[position.turnDrop]}
                                        </dd>
                                    </c:forEach>
                                </dl>
                            </c:if>
                        </li>
                    </c:forEach>
                </ul>
            </infomap>
        </c:if>
    </security:authorize>
</article>

<div id="question" style="display:none; cursor: default">
    <h3 id="joinTitle" style="margin-top: 22px;width: 120px;margin-left: 14px;float: left;">Join Game</h3>
    <img style="float:right;margin-top: 20px; margin-right: 20px;" id="nationImg"
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
    <h3 style="margin:auto; width:40%; margin-top: 22px;">Not enough credits</h3>

    <div style="width: 100%">
        <h3 style="margin-top:15px;margin-left: 70px; float: left; width: 150px;">Position costs
            &nbsp;
            <div id="nec_cost" style="float:right;width: 20px;margin-right: 5px;"/>
        </h3>

        <img style="margin-top:20px;float:left;" src='http://static.eaw1805.com/images/goods/good-1.png'
             alt="Credits"
             class="toolTip"
             title="Credits"
             border=0 height=20>

        <h3 style="margin-top:15px;margin-left: 7px; float: left; width: 120px;">per turn</h3>

        <div style="margin-left: 90px;color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                text-shadow: 1px 1px 0.1px rgb(123, 113, 75);font-weight: normal;letter-spacing: -1px;
                font-size: 16px;width: 80%;text-align: left;clear: both;">
            Do you wish to buy more credits?
        </div>
        <button class="ok" title="Buy more" onfocus=" this.blur();"
                style="clear: both; margin-top: 15px;margin-right: 5px;"
                onclick="window.location='<c:url value="/settings"/>';" value="Ok"/>
        <button id="cancelb" class="cancel" title="Cancel" onfocus=" this.blur();"
                style="clear: both; margin-top: 15px;margin-left: 5px; " aria-describedby=""
                onclick="$.unblockUI();$('#'+$('#cancelb').attr('aria-describedby')).hide();return false;"
                value="cancel"/>
    </div>

    <div>
        <img style="float:left;margin-left: 20px; margin-top: -10px;"
             src='http://static.eaw1805.com/images/buttons/taxation/MUINormalTaxSlc.png'
             alt="Credits"
             class="toolTip"
             title="Account Balance"
             border=0 height=32>

        <div style="float: left;font-size: 20px; margin: 2px;margin-top: -5px; margin-left: 5px;">${user.creditFree+user.creditTransferred+user.creditBought}
        </div>
    </div>
</div>

<div id="error_message_hoag" style="display:none; cursor: default">
    <h3 style="margin:auto; width:50%; margin-top: 22px;">Cannot pick up position</h3>

    <div style="margin-left: 50px;color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                text-shadow: 1px 1px 0.1px rgb(123, 113, 75);font-weight: normal;letter-spacing: -1px;
                font-size: 16px;width: 70%;margin-top: 30px;">
        You cannot pick up another empire in this game, because you've already played one at an earlier time.
    </div>
    <div style="margin-top: 8px;">
        <button class="ok" onfocus="this.blur();" id="close_error_hoag" onclick="$.unblockUI();" value="Ok"/>
    </div>
</div>

<div id="status_joined" style="display:none; cursor: default">
    <h3 style="margin: auto; margin-top: 20px;width: 50%;">Joined new Game!!</h3>

    <h3 style="margin-top: 15px;width: 245px;margin-left: 63px;">You have joined game ${param.g} with </h3>
    <img style="margin-top: 5px; margin-right: 20px;"
         src='http://static.eaw1805.com/images/nations/nation-${param.n}-list.jpg'
         alt="Nation's Flag"
         class="toolTip"
         title="${param.nn}"
         border=0 height=33>

    <div style="margin-top: 10px;">
        <button class="ok" onfocus=" this.blur();" type="button" id="close_status"
                onclick="window.location = '<c:url
                        value="/play/scenario/${game.scenarioIdToString}/game/${param.g}/nation/${param.n}"/>' "
                value="Ok"/>
    </div>
</div>
