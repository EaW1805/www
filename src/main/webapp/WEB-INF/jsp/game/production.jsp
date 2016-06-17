<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="moneyStats" scope="request" type="java.util.List"/>
<jsp:useBean id="citizenStats" scope="request" type="java.util.List"/>
<jsp:useBean id="goodStats" scope="request" type="java.util.List"/>
<jsp:useBean id="treasuryStats" scope="request" type="java.util.List"/>
<jsp:useBean id="peopleStats" scope="request" type="java.util.List"/>
<jsp:useBean id="warehouseStats" scope="request" type="java.util.List"/>
<jsp:useBean id="goodStatsAfrica" scope="request" type="java.util.List"/>
<jsp:useBean id="warehouseStatsAfrica" scope="request" type="java.util.List"/>
<jsp:useBean id="goodStatsCaribbean" scope="request" type="java.util.List"/>
<jsp:useBean id="warehouseStatsCaribbean" scope="request" type="java.util.List"/>
<jsp:useBean id="goodStatsIndies" scope="request" type="java.util.List"/>
<jsp:useBean id="warehouseStatsIndies" scope="request" type="java.util.List"/>
<jsp:useBean id="goodList" scope="request" type="java.util.ArrayList<com.eaw1805.data.model.economy.Good>"/>
<jsp:useBean id="idToGood" scope="request" type="java.util.Map<java.lang.Integer, com.eaw1805.data.model.economy.Good>"/>
<jsp:useBean id="regionList" scope="request" type="java.util.ArrayList<com.eaw1805.data.model.map.Region>"/>
<jsp:useBean id="sites" scope="request" type="java.util.ArrayList<com.eaw1805.data.model.map.ProductionSite>"/>
<jsp:useBean id="regionToWarehouse" scope="request" type="java.util.Map<java.lang.Integer, com.eaw1805.data.model.economy.Warehouse>"/>
<jsp:useBean id="regionToInitialGoods" scope="request" type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, java.lang.Integer>>"/>
<jsp:useBean id="regionToOrdersGoods" scope="request" type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, java.lang.Integer>>"/>
<jsp:useBean id="subTotals" scope="request" type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.lang.Integer>>>"/>
<jsp:useBean id="regionToPsToStats" scope="request"
             type="java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.lang.Object>>>"/>
<script src="<c:url value="/js/highcharts.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/gray.js"/>" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function () {
    var chart1 = new Highcharts.Chart({
        chart:{
            renderTo:'container-money',
            type:'spline'
        },
        credits:{
            enabled:false
        },
        title:{
            text:'Monthly Income'
        },
        xAxis:{
            <c:choose>
            <c:when test="${game.turn > 36}">
            tickInterval:180 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 24}">
            tickInterval:90 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 12}">
            tickInterval:60 * 24 * 3600 * 1000,
            </c:when>
            <c:otherwise>
            tickInterval:30 * 24 * 3600 * 1000,
            </c:otherwise>
            </c:choose>
            type:'datetime',
            dateTimeLabelFormats:{
                month:'%b %Y'
            }
        },
        yAxis:{
            title:{
                text:'Money'
            },
            min:0
        },
        tooltip:{
            formatter:function () {
                return '<strong>' + this.series.name + '</strong><br/>' +
                        Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
            }
        },
        series:[
            {
                name:'Taxation',
                // Define the data points. All series have a dummy year
                // of 1970/71 in order to be compared on the same x axis. Note
                // that in JavaScript, months start at 0 for January, 1 for February etc.
                data:[
                    <c:forEach items="${treasuryStats[0]}" var="thisMonth">
                    [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/> ],
                    </c:forEach>
                ]
            },
            {
                name:'Mint Production',
                // Define the data points. All series have a dummy year
                // of 1970/71 in order to be compared on the same x axis. Note
                // that in JavaScript, months start at 0 for January, 1 for February etc.
                data:[
                    <c:forEach items="${treasuryStats[1]}" var="thisMonth">
                    [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/> ],
                    </c:forEach>
                ]
            }
        ]
    });

    var chart1b = new Highcharts.Chart({
        chart:{
            renderTo:'container-treasury',
            type:'spline'
        },
        credits:{
            enabled:false
        },
        title:{
            text:'Available Money at National Treasury'
        },
        xAxis:{
            <c:choose>
            <c:when test="${game.turn > 36}">
            tickInterval:180 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 24}">
            tickInterval:90 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 12}">
            tickInterval:60 * 24 * 3600 * 1000,
            </c:when>
            <c:otherwise>
            tickInterval:30 * 24 * 3600 * 1000,
            </c:otherwise>
            </c:choose>
            type:'datetime',
            dateTimeLabelFormats:{
                month:'%b %Y'
            }
        },
        yAxis:{
            title:{
                text:'Money'
            },
            min:0
        },
        tooltip:{
            formatter:function () {
                return '<strong>' + this.series.name + '</strong><br/>' +
                        Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
            }
        },
        series:[
            {
                name:'Money',
                // Define the data points. All series have a dummy year
                // of 1970/71 in order to be compared on the same x axis. Note
                // that in JavaScript, months start at 0 for January, 1 for February etc.
                data:[
                    <c:forEach items="${moneyStats}" var="thisMonth">
                    [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/> ],
                    </c:forEach>
                ]
            }
        ]
    });

    var chart2 = new Highcharts.Chart({
        chart:{
            renderTo:'container-citizen',
            type:'spline'
        },
        credits:{
            enabled:false
        },
        title:{
            text:'New Citizens arriving this month'
        },
        subtitle:{
            text:'Europe'
        },
        xAxis:{
            <c:choose>
            <c:when test="${game.turn > 36}">
            tickInterval:180 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 24}">
            tickInterval:90 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 12}">
            tickInterval:60 * 24 * 3600 * 1000,
            </c:when>
            <c:otherwise>
            tickInterval:30 * 24 * 3600 * 1000,
            </c:otherwise>
            </c:choose>
            type:'datetime',
            dateTimeLabelFormats:{
                month:'%b %Y'
            }
        },
        yAxis:{
            title:{
                text:'Citizens'
            },
            min:0
        },
        tooltip:{
            formatter:function () {
                return '<strong>' + this.series.name + '</strong><br/>' +
                        Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
            }
        },
        series:[
            {
                name:'Citizens',
                // Define the data points. All series have a dummy year
                // of 1970/71 in order to be compared on the same x axis. Note
                // that in JavaScript, months start at 0 for January, 1 for February etc.
                data:[
                    <c:forEach items="${peopleStats}" var="thisMonth">
                    [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/> ],
                    </c:forEach>
                ]
            }
        ]
    });

    var chart2b = new Highcharts.Chart({
        chart:{
            renderTo:'container-people',
            type:'spline'
        },
        credits:{
            enabled:false
        },
        title:{
            text:'Available Citizens at each Region'
        },
        xAxis:{
            <c:choose>
            <c:when test="${game.turn > 36}">
            tickInterval:180 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 24}">
            tickInterval:90 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 12}">
            tickInterval:60 * 24 * 3600 * 1000,
            </c:when>
            <c:otherwise>
            tickInterval:30 * 24 * 3600 * 1000,
            </c:otherwise>
            </c:choose>
            type:'datetime',
            dateTimeLabelFormats:{
                month:'%b %Y'
            }
        },
        yAxis:{
            title:{
                text:'Citizens'
            },
            min:0
        },
        tooltip:{
            formatter:function () {
                return '<strong>' + this.series.name + '</strong><br/>' +
                        Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
            }
        },
        series:[
            <c:forEach items="${citizenStats}" var="thisGood">
            {
                name:'<c:out value="${thisGood['0'].caption}"/>',
                // Define the data points. All series have a dummy year
                // of 1970/71 in order to be compared on the same x axis. Note
                // that in JavaScript, months start at 0 for January, 1 for February etc.
                data:[
                    <c:forEach items="${thisGood}" var="thisMonth">
                    [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/> ],
                    </c:forEach>
                ]
            },
            </c:forEach>
        ]
    });

    var chart3 = new Highcharts.Chart({
        chart:{
            renderTo:'container-goods',
            type:'spline'
        },
        credits:{
            enabled:false
        },
        title:{
            text:'Monhtly Production of Goods'
        },
        subtitle:{
            text:'Europe'
        },
        xAxis:{
            <c:choose>
            <c:when test="${game.turn > 36}">
            tickInterval:180 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 24}">
            tickInterval:90 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 12}">
            tickInterval:60 * 24 * 3600 * 1000,
            </c:when>
            <c:otherwise>
            tickInterval:30 * 24 * 3600 * 1000,
            </c:otherwise>
            </c:choose>
            type:'datetime',
            dateTimeLabelFormats:{
                month:'%b %Y'
            }
        },
        yAxis:{
            title:{
                text:'Available materials'
            },
            min:0
        },
        tooltip:{
            formatter:function () {
                return '<strong>' + this.series.name + '</strong><br/>' +
                        Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
            }
        },
        series:[
            <c:forEach items="${goodStats}" var="thisGood">
            {
                name:'<c:out value="${thisGood['0'].caption}"/>',
                // Define the data points. All series have a dummy year
                // of 1970/71 in order to be compared on the same x axis. Note
                // that in JavaScript, months start at 0 for January, 1 for February etc.
                data:[
                    <c:forEach items="${thisGood}" var="thisMonth">
                    [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/> ],
                    </c:forEach>
                ]
            },
            </c:forEach>
        ]
    });

    var chart3b = new Highcharts.Chart({
        chart:{
            renderTo:'container-warehouse',
            type:'spline'
        },
        credits:{
            enabled:false
        },
        title:{
            text:'Available Goods at Warehouse'
        },
        subtitle:{
            text:'Europe'
        },
        xAxis:{
            <c:choose>
            <c:when test="${game.turn > 36}">
            tickInterval:180 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 24}">
            tickInterval:90 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 12}">
            tickInterval:60 * 24 * 3600 * 1000,
            </c:when>
            <c:otherwise>
            tickInterval:30 * 24 * 3600 * 1000,
            </c:otherwise>
            </c:choose>
            type:'datetime',
            dateTimeLabelFormats:{
                month:'%b %Y'
            }
        },
        yAxis:{
            title:{
                text:'Available materials'
            },
            min:0
        },
        tooltip:{
            formatter:function () {
                return '<strong>' + this.series.name + '</strong><br/>' +
                        Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
            }
        },
        series:[
            <c:forEach items="${warehouseStats}" var="thisGood">
            {
                name:'<c:out value="${thisGood['0'].caption}"/>',
                // Define the data points. All series have a dummy year
                // of 1970/71 in order to be compared on the same x axis. Note
                // that in JavaScript, months start at 0 for January, 1 for February etc.
                data:[
                    <c:forEach items="${thisGood}" var="thisMonth">
                    [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/> ],
                    </c:forEach>
                ]
            },
            </c:forEach>
        ]
    });

    var chart4 = new Highcharts.Chart({
        chart:{
            renderTo:'container-goods-africa',
            type:'spline'
        },
        credits:{
            enabled:false
        },
        title:{
            text:'Monhtly Production of Goods'
        },
        subtitle:{
            text:'Africa'
        },
        xAxis:{
            <c:choose>
            <c:when test="${game.turn > 36}">
            tickInterval:180 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 24}">
            tickInterval:90 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 12}">
            tickInterval:60 * 24 * 3600 * 1000,
            </c:when>
            <c:otherwise>
            tickInterval:30 * 24 * 3600 * 1000,
            </c:otherwise>
            </c:choose>
            type:'datetime',
            dateTimeLabelFormats:{
                month:'%b %Y'
            }
        },
        yAxis:{
            title:{
                text:'Available materials'
            },
            min:0
        },
        tooltip:{
            formatter:function () {
                return '<strong>' + this.series.name + '</strong><br/>' +
                        Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
            }
        },
        series:[
            <c:forEach items="${goodStatsAfrica}" var="thisGood">
            {
                name:'<c:out value="${thisGood['0'].caption}"/>',
                // Define the data points. All series have a dummy year
                // of 1970/71 in order to be compared on the same x axis. Note
                // that in JavaScript, months start at 0 for January, 1 for February etc.
                data:[
                    <c:forEach items="${thisGood}" var="thisMonth">
                    [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/> ],
                    </c:forEach>
                ]
            },
            </c:forEach>
        ]
    });

    var chart4b = new Highcharts.Chart({
        chart:{
            renderTo:'container-warehouse-africa',
            type:'spline'
        },
        credits:{
            enabled:false
        },
        title:{
            text:'Available Goods at Warehouse'
        },
        subtitle:{
            text:'Africa'
        },
        xAxis:{
            <c:choose>
            <c:when test="${game.turn > 36}">
            tickInterval:180 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 24}">
            tickInterval:90 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 12}">
            tickInterval:60 * 24 * 3600 * 1000,
            </c:when>
            <c:otherwise>
            tickInterval:30 * 24 * 3600 * 1000,
            </c:otherwise>
            </c:choose>
            type:'datetime',
            dateTimeLabelFormats:{
                month:'%b %Y'
            }
        },
        yAxis:{
            title:{
                text:'Available materials'
            },
            min:0
        },
        tooltip:{
            formatter:function () {
                return '<strong>' + this.series.name + '</strong><br/>' +
                        Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
            }
        },
        series:[
            <c:forEach items="${warehouseStatsAfrica}" var="thisGood">
            {
                name:'<c:out value="${thisGood['0'].caption}"/>',
                // Define the data points. All series have a dummy year
                // of 1970/71 in order to be compared on the same x axis. Note
                // that in JavaScript, months start at 0 for January, 1 for February etc.
                data:[
                    <c:forEach items="${thisGood}" var="thisMonth">
                    [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/> ],
                    </c:forEach>
                ]
            },
            </c:forEach>
        ]
    });

    <!-- Car -->
    var chart5 = new Highcharts.Chart({
        chart:{
            renderTo:'container-goods-caribbean',
            type:'spline'
        },
        credits:{
            enabled:false
        },
        title:{
            text:'Monhtly Production of Goods'
        },
        subtitle:{
            text:'Caribbean'
        },
        xAxis:{
            <c:choose>
            <c:when test="${game.turn > 36}">
            tickInterval:180 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 24}">
            tickInterval:90 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 12}">
            tickInterval:60 * 24 * 3600 * 1000,
            </c:when>
            <c:otherwise>
            tickInterval:30 * 24 * 3600 * 1000,
            </c:otherwise>
            </c:choose>
            type:'datetime',
            dateTimeLabelFormats:{
                month:'%b %Y'
            }
        },
        yAxis:{
            title:{
                text:'Available materials'
            },
            min:0
        },
        tooltip:{
            formatter:function () {
                return '<strong>' + this.series.name + '</strong><br/>' +
                        Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
            }
        },
        series:[
            <c:forEach items="${goodStatsCaribbean}" var="thisGood">
            {
                name:'<c:out value="${thisGood['0'].caption}"/>',
                // Define the data points. All series have a dummy year
                // of 1970/71 in order to be compared on the same x axis. Note
                // that in JavaScript, months start at 0 for January, 1 for February etc.
                data:[
                    <c:forEach items="${thisGood}" var="thisMonth">
                    [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/> ],
                    </c:forEach>
                ]
            },
            </c:forEach>
        ]
    });

    var chart5b = new Highcharts.Chart({
        chart:{
            renderTo:'container-warehouse-caribbean',
            type:'spline'
        },
        credits:{
            enabled:false
        },
        title:{
            text:'Available Goods at Warehouse'
        },
        subtitle:{
            text:'Caribbean'
        },
        xAxis:{
            <c:choose>
            <c:when test="${game.turn > 36}">
            tickInterval:180 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 24}">
            tickInterval:90 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 12}">
            tickInterval:60 * 24 * 3600 * 1000,
            </c:when>
            <c:otherwise>
            tickInterval:30 * 24 * 3600 * 1000,
            </c:otherwise>
            </c:choose>
            type:'datetime',
            dateTimeLabelFormats:{
                month:'%b %Y'
            }
        },
        yAxis:{
            title:{
                text:'Available materials'
            },
            min:0
        },
        tooltip:{
            formatter:function () {
                return '<strong>' + this.series.name + '</strong><br/>' +
                        Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
            }
        },
        series:[
            <c:forEach items="${warehouseStatsCaribbean}" var="thisGood">
            {
                name:'<c:out value="${thisGood['0'].caption}"/>',
                // Define the data points. All series have a dummy year
                // of 1970/71 in order to be compared on the same x axis. Note
                // that in JavaScript, months start at 0 for January, 1 for February etc.
                data:[
                    <c:forEach items="${thisGood}" var="thisMonth">
                    [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/> ],
                    </c:forEach>
                ]
            },
            </c:forEach>
        ]
    });


    <!-- Ind -->
    var chart6 = new Highcharts.Chart({
        chart:{
            renderTo:'container-goods-indies',
            type:'spline'
        },
        credits:{
            enabled:false
        },
        title:{
            text:'Monhtly Production of Goods'
        },
        subtitle:{
            text:'Indies'
        },
        xAxis:{
            <c:choose>
            <c:when test="${game.turn > 36}">
            tickInterval:180 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 24}">
            tickInterval:90 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 12}">
            tickInterval:60 * 24 * 3600 * 1000,
            </c:when>
            <c:otherwise>
            tickInterval:30 * 24 * 3600 * 1000,
            </c:otherwise>
            </c:choose>
            type:'datetime',
            dateTimeLabelFormats:{
                month:'%b %Y'
            }
        },
        yAxis:{
            title:{
                text:'Available materials'
            },
            min:0
        },
        tooltip:{
            formatter:function () {
                return '<strong>' + this.series.name + '</strong><br/>' +
                        Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
            }
        },
        series:[
            <c:forEach items="${goodStatsIndies}" var="thisGood">
            {
                name:'<c:out value="${thisGood['0'].caption}"/>',
                // Define the data points. All series have a dummy year
                // of 1970/71 in order to be compared on the same x axis. Note
                // that in JavaScript, months start at 0 for January, 1 for February etc.
                data:[
                    <c:forEach items="${thisGood}" var="thisMonth">
                    [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/> ],
                    </c:forEach>
                ]
            },
            </c:forEach>
        ]
    });

    var chart6b = new Highcharts.Chart({
        chart:{
            renderTo:'container-warehouse-indies',
            type:'spline'
        },
        credits:{
            enabled:false
        },
        title:{
            text:'Available Goods at Warehouse'
        },
        subtitle:{
            text:'Indies'
        },
        xAxis:{
            <c:choose>
            <c:when test="${game.turn > 36}">
            tickInterval:180 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 24}">
            tickInterval:90 * 24 * 3600 * 1000,
            </c:when>
            <c:when test="${game.turn > 12}">
            tickInterval:60 * 24 * 3600 * 1000,
            </c:when>
            <c:otherwise>
            tickInterval:30 * 24 * 3600 * 1000,
            </c:otherwise>
            </c:choose>
            type:'datetime',
            dateTimeLabelFormats:{
                month:'%b %Y'
            }
        },
        yAxis:{
            title:{
                text:'Available materials'
            },
            min:0
        },
        tooltip:{
            formatter:function () {
                return '<strong>' + this.series.name + '</strong><br/>' +
                        Highcharts.dateFormat('%b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
            }
        },
        series:[
            <c:forEach items="${warehouseStatsIndies}" var="thisGood">
            {
                name:'<c:out value="${thisGood['0'].caption}"/>',
                // Define the data points. All series have a dummy year
                // of 1970/71 in order to be compared on the same x axis. Note
                // that in JavaScript, months start at 0 for January, 1 for February etc.
                data:[
                    <c:forEach items="${thisGood}" var="thisMonth">
                    [Date.UTC(<c:out value="${thisMonth.year}"/>, <c:out value="${thisMonth.month}"/>, 1), <c:out value="${thisMonth.value}"/> ],
                    </c:forEach>
                ]
            },
            </c:forEach>
        ]
    });

});
</script>
<% if (request.getParameter("fixForClient") == null) { %>
</div>

<div style="z-index: 2; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;">
    <h2 style="padding-left: 60px;">${gameDate}</h2>
</div>
<%}%>


<c:forEach items="${regionList}" var="region">
<c:if test="${hasGoodsInRegion[region.id]}">
<c:set var="factoryProduction" value="100"/>
<c:if test="${region.id != 1}"><c:set var="factoryProduction" value="75"/></c:if>
<div style="z-index: 2; position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 20px;">Production Sites - ${region.name}</h2>
</div>

<div style="z-index: 1;position: relative;margin: 4px 0px 0px -35px;width: 1000px;min-height: 333px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
<h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>
<% if (request.getParameter("fixForClient") == null) { %>
<%} else {%>
<%}%>
<c:if test="${region.id == 1}">
    <img src="http://direct.eaw1805.com/images/panels/reports/arrowSheepToMill.png"
         style="position: absolute; width: 84px; height: 86px; top: 154px; left: 443px;"/>
</c:if>
<img src="http://direct.eaw1805.com/images/panels/reports/arrowSheepToMill.png"
     style="position: absolute; width: 84px; height: 86px; top: 154px; left: 830px;"/>
<img src="http://direct.eaw1805.com/images/panels/reports/arrowMineToFactory.png"
     style="position: absolute; width: 84px; height: 308px; top: 146px; left: 342px;"/>
<img src="http://direct.eaw1805.com/images/panels/reports/arrowMillToFactory.png"
     style="position: absolute; height: 101px; left: 401px; width: 478px; top: 368px;"/>
<img src="http://direct.eaw1805.com/images/panels/reports/arrowLumpToFactory.png"
     style="position: absolute; width: 124px; left: 255px; top: 139px; height: 330px;"/>

<div style="position: absolute; top: 278px; left: 238px;" title="consumed Wood">
    <img src="http://static.eaw1805.com/images/goods/good-6.png"
         style="width: 15px; height: 15px"/>
    <fmt:formatNumber type="number" maxFractionDigits="0"
                      groupingUsed="true"
                      value="${regionToPsToStats[region.id][sites[1].id]['producedQuantity'][goodList[1].goodId]* (20 / factoryProduction)}"/>
</div>

<div style="position: absolute; top: 299px; left: 376px;" title="consumed Ore">
    <img src="http://static.eaw1805.com/images/goods/good-7.png"
         style="width: 15px; height: 15px"/>
    <fmt:formatNumber type="number" maxFractionDigits="0"
                      groupingUsed="true"
                      value="${regionToPsToStats[region.id][sites[1].id]['producedQuantity'][goodList[1].goodId]* (1 / factoryProduction)}"/>
</div>

<div style="position: absolute; left: 640px; top: 441px;" title="consumed Fabric">
    <img src="http://static.eaw1805.com/images/goods/good-10.png"
         style="width: 15px; height: 15px"/>
    <fmt:formatNumber type="number" maxFractionDigits="0"
                      groupingUsed="true"
                      value="${regionToPsToStats[region.id][sites[1].id]['producedQuantity'][goodList[1].goodId]* (5 / factoryProduction)}"/>
</div>
<c:if test="${region.id == 1}">
    <div style="position: absolute; top: 190px; left: 500px;" title="consumed Precious Metals">
        <img src="http://static.eaw1805.com/images/goods/good-12.png"
             style="width: 15px; height: 15px"/>
        <fmt:formatNumber type="number" maxFractionDigits="0"
                          groupingUsed="true"
                          value="${regionToPsToStats[region.id][sites[5].id]['producedQuantity'][goodMoney]* (1 / 30000)}"/>
    </div>
</c:if>
    <%--${thisReport.value * -2}--%>
<div style="position: absolute; left: 888px; top: 190px;" title="consumed Wool">
    <img src="http://static.eaw1805.com/images/goods/good-11.png"
         style="width: 15px; height: 15px"/>
    <fmt:formatNumber type="number" maxFractionDigits="0"
                      groupingUsed="true"
                      value="${regionToPsToStats[region.id][sites[10].id]['producedQuantity'][goodList[8].goodId] * 2}"/>
</div>

<table>
<tr>

    <c:forEach items="${sites}" var="thisSite">
        <c:if test="${thisSite.id != 2 && thisSite.id != 6 && thisSite.id != 11}">

            <c:forEach items="${regionToPsToStats[region.id][thisSite.id]['producedTypes']}" var="typeId">

                <td>
                    <article>
                        <productionsite
                                <c:if test="${regionToPsToStats[region.id][thisSite.id]['warning']}">
                                    style="background: none repeat scroll 0px 0px rgba(254, 25, 25, 0.4);"
                                    title="Some ${thisSite.name}s didn't produce any matterials"
                                </c:if>
                                >
                            <img
                                    src='http://static.eaw1805.com/tiles/sites/tprod-${thisSite.id}.png'
                                    alt="Total number of ${thisSite.name}s"
                                    class="toolTip"
                                    title="${thisSite.name}s"
                                    border=0 width=75/>
                                <%--<ul class="infopanel">--%>
                                <%--<li>--%>
                            <table width="75px">
                                <tr title="Total number of ${thisSite.name}s">
                                    <td class="infopanelTotals">#</td>
                                    <td class="infopanelTotals">${regionToPsToStats[region.id][thisSite.id]["number"][typeId]}</td>
                                </tr>
                                <tr title="Initial warehouse quantity of ${idToGood[typeId].name}">
                                    <td class="infopanelTotals">I</td>
                                    <td class="infopanelTotals" style="width: 100%;">
                                        <fmt:formatNumber type="number" maxFractionDigits="0"
                                                          groupingUsed="true"
                                                          value="${regionToInitialGoods[region.id][typeId]}"/>
                                    </td>
                                </tr>
                                <tr title="
                                        <table>
                                        <c:forEach items="${subTotals[region.id][typeId]}" var="entry">
                                            <tr><td align='left'>${entry.key} :</td><td align='right'> <fmt:formatNumber type='number' maxFractionDigits='0'
                                                                             groupingUsed='true'
                                                                             value='${entry.value}'/></td></tr>
                                    </c:forEach></table>">
                                    <td class="infopanelTotals">O</td>
                                    <td class="infopanelTotals" style="width: 100%;">
                                        <fmt:formatNumber type="number" maxFractionDigits="0"
                                                          groupingUsed="true"
                                                          value="${regionToOrdersGoods[region.id][typeId]}"/>
                                    </td>
                                </tr>
                                <tr title="produced ${idToGood[typeId].name}">
                                    <td class="infopanelTotals">P</td>
                                    <td class="infopanelTotals" style="width: 100%;">
                                        <fmt:formatNumber type="number" maxFractionDigits="0"
                                                          groupingUsed="true"
                                                          value="${regionToPsToStats[region.id][thisSite.id]['producedQuantity'][typeId]}"/>
                                    </td>
                                </tr>
                                <tr title="Available warehouse quantity of ${idToGood[typeId].name}">
                                    <td class="infopanelTotals">
                                        <img src="http://static.eaw1805.com/images/goods/good-${typeId}.png"
                                             style="width: 15px; height: 15px"/>
                                    </td>
                                    <td class="infopanelTotals" style="width: 100%;">
                                        <fmt:formatNumber type="number" maxFractionDigits="0"
                                                          groupingUsed="true"
                                                          value="${regionToWarehouse[region.id].storedGoodsQnt[typeId]}"/>
                                    </td>

                                </tr>

                            </table>
                                <%--</li>--%>
                                <%--</ul>--%>

                        </productionsite>
                    </article>
                </td>
            </c:forEach>
        </c:if>
    </c:forEach>

</tr>
<tr>
    <td>
        <div style="height: 50px"></div>
    </td>
</tr>
<tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <c:forEach items="${sites}" var="thisSite">
        <c:if test="${thisSite.id == 6 || thisSite.id == 11}">
            <c:forEach items="${regionToPsToStats[region.id][thisSite.id]['producedTypes']}"
                       var="typeId">
                <td>
                    <c:if test="${thisSite.id != 6 || region.id == 1}">
                        <article>
                            <productionsite
                                    <c:if test="${regionToPsToStats[region.id][thisSite.id]['warning']}">
                                        style="background: none repeat scroll 0px 0px rgba(254, 25, 25, 0.4);"
                                        title="Some ${thisSite.name}s didn't produce any matterials"
                                    </c:if>
                                    >
                                <img
                                        src='http://static.eaw1805.com/tiles/sites/tprod-${thisSite.id}.png'
                                        alt="Total number of ${thisSite.name}s"
                                        class="toolTip"
                                        title="${thisSite.name}s"
                                        border=0 width=75/>
                                <table width="75px">
                                    <tr title="Total number of ${thisSite.name}s">
                                        <td class="infopanelTotals">#</td>
                                        <td class="infopanelTotals">${regionToPsToStats[region.id][thisSite.id]["number"][typeId]}</td>
                                    </tr>
                                    <tr title="Initial warehouse quantity of ${idToGood[typeId].name}">
                                        <td class="infopanelTotals">I</td>
                                        <td class="infopanelTotals" style="width: 100%;">
                                            <fmt:formatNumber type="number" maxFractionDigits="0"
                                                              groupingUsed="true"
                                                              value="${regionToInitialGoods[region.id][typeId]}"/>
                                        </td>
                                    </tr>
                                    <tr title="
                                        <table>
                                        <c:forEach items="${subTotals[region.id][typeId]}" var="entry">
                                            <tr><td align='left'>${entry.key} :</td><td align='right'> <fmt:formatNumber type='number' maxFractionDigits='0'
                                                                             groupingUsed='true'
                                                                             value='${entry.value}'/></td></tr>
                                    </c:forEach></table>">
                                        <td class="infopanelTotals">O</td>
                                        <td class="infopanelTotals" style="width: 100%;">
                                            <fmt:formatNumber type="number" maxFractionDigits="0"
                                                              groupingUsed="true"
                                                              value="${regionToOrdersGoods[region.id][typeId]}"/>
                                        </td>
                                    </tr>
                                    <tr title="produced ${idToGood[typeId].name}">
                                        <td class="infopanelTotals">P</td>
                                        <td class="infopanelTotals" style="width: 100%;">
                                            <fmt:formatNumber type="number" maxFractionDigits="0"
                                                              groupingUsed="true"
                                                              value="${regionToPsToStats[region.id][thisSite.id]['producedQuantity'][typeId]}"/>
                                        </td>
                                    </tr>
                                    <tr title="Available warehouse quantity of ${idToGood[typeId].name}">
                                        <td class="infopanelTotals">
                                            <img src="http://static.eaw1805.com/images/goods/good-${typeId}.png"
                                                 style="width: 15px; height: 15px"/>
                                        </td>

                                        <td class="infopanelTotals" style="width: 100%;">
                                            <fmt:formatNumber type="number" maxFractionDigits="0"
                                                              groupingUsed="true"
                                                              value="${regionToWarehouse[region.id].storedGoodsQnt[typeId]}"/>
                                        </td>
                                    </tr>
                                </table>
                            </productionsite>
                        </article>
                    </c:if>
                </td>
            </c:forEach>

            <td></td>
            <td></td>
            <td></td>
        </c:if>
    </c:forEach>
</tr>
<tr>
    <td>
        <div style="height: 50px"></div>
    </td>
</tr>
<tr>
    <td></td>
    <td></td>
    <td></td>
    <c:forEach items="${sites}" var="thisSite">
        <c:if test="${thisSite.id == 2}">
            <c:forEach items="${regionToPsToStats[region.id][thisSite.id]['producedTypes']}"
                       var="typeId">
                <td>
                    <article>
                        <productionsite
                                <c:if test="${regionToPsToStats[region.id][thisSite.id]['warning']}">
                                    style="background: none repeat scroll 0px 0px rgba(254, 25, 25, 0.4);"
                                    title="Some ${thisSite.name}s didn't produce any matterials"
                                </c:if>
                                >
                            <img
                                    src='http://static.eaw1805.com/tiles/sites/tprod-${thisSite.id}.png'
                                    alt="Total number of ${thisSite.name}s"
                                    class="toolTip"
                                    title="${thisSite.name}s"
                                    border=0 width=75>
                            <table width="75px">
                                <tr title="Total number of ${thisSite.name}s">
                                    <td class="infopanelTotals">#</td>
                                    <td class="infopanelTotals">${regionToPsToStats[region.id][thisSite.id]["number"][typeId]}</td>
                                </tr>
                                <tr title="Initial warehouse quantity of ${idToGood[typeId].name}">
                                    <td class="infopanelTotals">I</td>
                                    <td class="infopanelTotals" style="width: 100%;">
                                        <fmt:formatNumber type="number" maxFractionDigits="0"
                                                          groupingUsed="true"
                                                          value="${regionToInitialGoods[region.id][typeId]}"/>
                                    </td>
                                </tr>
                                <tr title="
                                        <table>
                                        <c:forEach items="${subTotals[region.id][typeId]}" var="entry">
                                            <tr><td align='left'>${entry.key} :</td><td align='right'> <fmt:formatNumber type='number' maxFractionDigits='0'
                                                                             groupingUsed='true'
                                                                             value='${entry.value}'/></td></tr>
                                    </c:forEach></table>">
                                    <td class="infopanelTotals">O</td>
                                    <td class="infopanelTotals" style="width: 100%;">
                                        <fmt:formatNumber type="number" maxFractionDigits="0"
                                                          groupingUsed="true"
                                                          value="${regionToOrdersGoods[region.id][typeId]}"/>
                                    </td>
                                </tr>
                                <tr title="produced ${idToGood[typeId].name}">
                                    <td class="infopanelTotals">P</td>
                                    <td class="infopanelTotals" style="width: 100%;">
                                        <fmt:formatNumber type="number" maxFractionDigits="0"
                                                          groupingUsed="true"
                                                          value="${regionToPsToStats[region.id][thisSite.id]['producedQuantity'][typeId]}"/>
                                    </td>
                                </tr>
                                <tr title="Available warehouse quantity of ${idToGood[typeId].name}">
                                    <td class="infopanelTotals">
                                        <img src="http://static.eaw1805.com/images/goods/good-${typeId}.png"
                                             style="width: 15px; height: 15px"/>
                                    </td>

                                    <td class="infopanelTotals" style="width: 100%;">
                                        <fmt:formatNumber type="number" maxFractionDigits="0"
                                                          groupingUsed="true"
                                                          value="${regionToWarehouse[region.id].storedGoodsQnt[typeId]}"/>
                                    </td>
                                </tr>



                            </table>
                        </productionsite>
                    </article>
                </td>
            </c:forEach>
        </c:if>
    </c:forEach>
</tr>
</table>
</div>

<div style="clear: both; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;"></div>
</c:if>
</c:forEach>


<div style="z-index: 2; position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 20px;">Barracks & Shipyards</h2>
</div>

<div style="z-index: 1; position: relative;margin: 4px 0px 0px -35px;width: 1000px;min-height: 100px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
    <h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>
    <%--<% } %>--%>

    <table class="warehouse"
            <% if (request.getParameter("fixForClient") == null) { %>
           style="background: none; border: none; border-style: hidden; margin-top: 0px; box-shadow: 0 0 0 0;"
    <% } %>
    <% if (request.getParameter("fixForClient") != null) { %>
    <tr class="warehouse">
        <th colspan="5" class="warehouse-head">Barracks & Shipyards</th>
    </tr>
    <% } %>
    <tr class="warehouse">
        <th align="left" width="48" class="warehouse">&nbsp;</th>
        <th align="left" width="48" class="warehouse">&nbsp;</th>
        <th align="left" width="48" class="warehouse">&nbsp;</th>
        <c:forEach items="${regionList}" var="thisRegion">
            <c:if test="${thisRegion.id > 0}">
                <th class="warehouse">${thisRegion.name}</th>
            </c:if>
        </c:forEach>
    </tr>
    <c:forEach items="${barracks}" var="thisSite">
        <jsp:useBean id="thisSite" type="com.eaw1805.data.model.map.ProductionSite"/>
        <tr class="warehouse">
            <c:choose>
                <c:when test="${thisSite.id == 2}">
                    <th class="warehouseimg"></th>
                    <th class="warehouseimg"></th>
                    <th class="warehouseimg"><img
                            src='http://static.eaw1805.com/tiles/sites/tprod-${thisSite.id}.png'
                            alt="Total number of ${thisSite.name}s"
                            class="toolTip"
                            title="Total number of ${thisSite.name}s"
                            border=0 width=48></th>
                </c:when>
                <c:when test="${thisSite.id == 6 || thisSite.id==11}">
                    <th class="warehouseimg"></th>
                    <th class="warehouseimg"><img
                            src='http://static.eaw1805.com/tiles/sites/tprod-${thisSite.id}.png'
                            alt="Total number of ${thisSite.name}s"
                            class="toolTip"
                            title="Total number of ${thisSite.name}s"
                            border=0 width=48></th>
                    <th class="warehouseimg"></th>
                </c:when>
                <c:otherwise>
                    <th class="warehouseimg"><img
                            src='http://static.eaw1805.com/tiles/sites/tprod-${thisSite.id}.png'
                            alt="Total number of ${thisSite.name}s"
                            class="toolTip"
                            title="Total number of ${thisSite.name}s"
                            border=0 width=48></th>
                    <th class="warehouseimg"></th>
                    <th class="warehouseimg"></th>
                </c:otherwise>
            </c:choose>

            <c:forEach items="${siteStats}" var="region">
                <td class="warehouse"><fmt:formatNumber type="number" maxFractionDigits="0"
                                                        groupingUsed="true"
                                                        value="${region[thisSite.id-1].value}"/></td>
            </c:forEach>
        </tr>
    </c:forEach>
    </table>

    <% if (request.getParameter("fixForClient") == null) { %>
</div>

<div style="clear: both; position: relative; margin: -20px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;"></div>

<div style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 16px;">National Treasury & Income</h2>
</div>

<div style=" position: relative;margin: 4px 0px 0px -35px;width: 1000px;min-height: 100px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
    <h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>
    <% } else { %>
    <br>
    <% } %>
    <table>
        <tr>
            <td>
                <div id="container-treasury" style="width: 955px; height: 250px"></div>
                <br>
            </td>
        </tr>
        <tr>
            <td>
                <div id="container-money" style="width: 955px; height: 250px"></div>
                <% if (request.getParameter("fixForClient") == null) { %>
                <br>
                <%}%>
            </td>
        </tr>
        <% if (request.getParameter("fixForClient") == null) { %>
    </table>
</div>

<div style="clear: both; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;"></div>

<div style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 16px;">Available Citizens & New Citizens arriving</h2>
</div>

<div style=" position: relative;margin: 4px 0px 0px -35px;width: 1000px;min-height: 200px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
    <h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>
    <table>
        <% } %>
        <tr>
            <td>
                <div id="container-citizen" style="width: 955px; height: 250px"></div>
                <br>
            </td>
        </tr>
        <tr>
            <td>
                <div id="container-people" style="width: 955px; height: 250px"></div>
                <% if (request.getParameter("fixForClient") == null) { %>
                <br>
                <%}%>
            </td>
        </tr>
        <% if (request.getParameter("fixForClient") == null) { %>
    </table>
</div>

<div style="clear: both; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;"></div>

<div style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 16px;">European Goods & Production</h2>
</div>

<div style=" position: relative;margin: 4px 0px 0px -35px;width: 1000px;min-height: 200px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
    <h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>
    <table>
        <% } %>
        <tr>
            <td>
                <div id="container-warehouse" style="width: 955px; height: 400px"></div>
                <br>
            </td>
        </tr>
        <tr>
            <td>
                <div id="container-goods" style="width: 955px; height: 400px"></div>
                <% if (request.getParameter("fixForClient") == null) { %>
                <br>
                <%}%>
            </td>
                <% if (request.getParameter("fixForClient") == null) { %>
    </table>
</div>

<div style="clear: both; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;"></div>

<div style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 16px;">African Goods & Production</h2>
</div>

<div style=" position: relative;margin: 4px 0px 0px -35px;width: 1000px;min-height: 200px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
    <h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>
    <table>
        <% } %>
        <tr>
            <td>
                <div id="container-warehouse-africa" style="width: 955px; height: 400px"></div>
                <br>
            </td>
        </tr>
        <tr>
            <td>
                <div id="container-goods-africa" style="width: 955px; height: 400px"></div>
                <% if (request.getParameter("fixForClient") == null) { %>
                <br>
                <%}%>
            </td>
                <% if (request.getParameter("fixForClient") == null) { %>
    </table>
</div>

<div style="clear: both; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;"></div>

<div style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 16px;">Caribbean Goods & Production</h2>
</div>

<div style=" position: relative;margin: 4px 0px 0px -35px;width: 1000px;min-height: 200px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
    <h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>
    <table>
        <% } %>
        <tr>
            <td>
                <div id="container-warehouse-caribbean" style="width: 955px; height: 400px"></div>
                <br>
            </td>
        </tr>
        <tr>
            <td>
                <div id="container-goods-caribbean" style="width: 955px; height: 400px"></div>
                <% if (request.getParameter("fixForClient") == null) { %>
                <br>
                <%}%>
            </td>
                <% if (request.getParameter("fixForClient") == null) { %>
    </table>
</div>

<div style="clear: both; position: relative; margin: 0px 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;
    clear: both; opacity: 0.78;"></div>

<div style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="padding-left: 40px; padding-top: 16px;">Indies Goods & Production</h2>
</div>

<div style=" position: relative;margin: 4px 0px 0px -35px;width: 1000px;min-height: 200px;padding: 0px 40px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
    <h3 style="margin-top:-14px; margin-bottom: -20px; padding-top: 0px; padding-bottom: 0px;">&nbsp;</h3>
    <table>
        <% } %>
        <tr>
            <td>
                <div id="container-warehouse-indies" style="width: 955px; height: 400px"></div>
                <br>
            </td>
        </tr>
        <tr>
            <td>
                <div id="container-goods-indies" style="width: 955px; height: 400px"></div>
                <% if (request.getParameter("fixForClient") == null) { %>
                <br>
                <%}%>
            </td>
        </tr>
    </table>
</div>
</div>
