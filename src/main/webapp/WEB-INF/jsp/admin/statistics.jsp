<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="chartTitle" scope="request" type="java.lang.String[]"/>
<jsp:useBean id="seriesTitle" scope="request" type="java.lang.String[]"/>
<jsp:useBean id="axisTitle" scope="request" type="java.lang.String[]"/>
<jsp:useBean id="chartData" scope="request" type="java.util.List<java.util.List<com.eaw1805.www.commands.ChartData>>"/>
<script src="<c:url value="/js/highcharts.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/vpchart.js"/>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        <c:forEach items="${chartData}" var="entry" varStatus="count">
        var chart${count.index} = new Highcharts.Chart({
            chart:{
                renderTo:'container-${count.index}',
                zoomType:'xy'
            },
            credits:{
                enabled:false
            },
            title:{
                text:''
            },
            xAxis:{
                type:'datetime',
                dateTimeLabelFormats:{
                    month:'%b %Y'
                }
            },
            yAxis:[
                { // Primary yAxis
                    title:{
                        text:'Growth Rate',
                        style:{
                            color:'#4572A7'
                        }
                    },
                    labels:{
                        format:'{value}%',
                        style:{
                            color:'#4572A7'
                        }
                    },
                    opposite:true
                },
                { // Secondary yAxis
                    title:{
                        text:'${axisTitle[count.index]}',
                        style:{
                            color:'#89A54E'
                        }
                    },
                    labels:{
                        format:'{value}',
                        style:{
                            color:'#89A54E'
                        }
                    }
                }
            ],
            tooltip:{
                formatter:function () {
                    return '<strong>' + this.series.name + '</strong><br/>' +
                            Highcharts.dateFormat('%d %b %Y', this.x) + ': ' + Highcharts.numberFormat(this.y, 0, ',');
                }
            },
            series:[
                {
                    name:'${seriesTitle[count.index]}',
                    color:'#89A54E',
                    type:'column',
                    yAxis:1,
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data:[
                        <c:forEach items="${entry}" var="thisWeek">
                        [Date.UTC(<c:out value="${thisWeek.year}"/>, <c:out value="${thisWeek.month}"/>, <c:out value="${thisWeek.day}"/>), <c:out
            value="${thisWeek.value}"/> ],
                        </c:forEach>
                    ]
                },
                {
                    name:'Weekly Growth Rate',
                    color:'#4572A7',
                    type:'spline',
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data:[
                        <c:forEach items="${entry}" var="thisWeek">
                        [Date.UTC(<c:out value="${thisWeek.year}"/>, <c:out value="${thisWeek.month}"/>, <c:out value="${thisWeek.day}"/>), <c:out
            value="${thisWeek.doubleValue}"/> ],
                        </c:forEach>
                    ]
                }
            ]
        });
        </c:forEach>
    });
</script>
<article>
    <c:forEach items="${chartTitle}" var="title" varStatus="count">
        <bigmap style="height: 635px;">
            <h2 class="bigmap">${title}</h2>

            <div id="container-${count.index}" style="width: 940px; height: 600px"></div>
        </bigmap>
    </c:forEach>
</article>
