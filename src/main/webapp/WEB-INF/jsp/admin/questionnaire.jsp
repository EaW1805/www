<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="TYPES" scope="request" type="java.lang.String[][]"/>
<jsp:useBean id="titles" scope="request" type="java.lang.String[]"/>
<jsp:useBean id="statsInt" scope="request" type="java.util.Map<java.lang.String, java.lang.Integer>"/>
<jsp:useBean id="statsDouble" scope="request" type="java.util.Map<java.lang.String, java.lang.Double>"/>
<script src="<c:url value="/js/highcharts.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/vpchart.js"/>" type="text/javascript"></script>
<script type="text/javascript">

    $(document).ready(function () {
        <c:forEach items="${TYPES}" var="entryTypes" varStatus="status">


        var seriesData${status.count} = [
            <c:forEach items="${entryTypes}" var="type">
            ['${type}', <c:out value="${statsInt[type]}"/> ],
            </c:forEach>
        ];
        var seriesDoubleData${status.count} = [
            <c:forEach items="${entryTypes}" var="type">
            ['${type}', <c:out value="${statsDouble[type]}"/> ],
            </c:forEach>
        ];
        new Highcharts.Chart({
            chart:{
                renderTo:'container${status.count}',
                zoomType:'xy'
            },
            credits:{
                enabled:false
            },
            title:{
                text:''
            },
            xAxis:{

                tickInterval: 1,
                labels: {
                    rotation: -45,
                    align: "right",
                    enabled: true,
                    formatter: function() { return seriesData${status.count}[this.value][0];}
                }


//                type:'text'
//                dateTimeLabelFormats:{
//                    month:'%b %Y'
//                }
            },
            yAxis:[
                { // Primary yAxis
                    title:{
                        text:'Percent',
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
                        text:'Votes',
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
                            seriesData${status.count}[this.x][0] + ': ' + this.y;
                }
            },
            series:[
                {
                    name:'Types',
                    color:'#89A54E',
                    type:'column',
                    yAxis:1,
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data:seriesData${status.count}
                },
                {
                    name:'Percent',
                    color:'#4572A7',
                    type:'spline',
                    // Define the data points. All series have a dummy year
                    // of 1970/71 in order to be compared on the same x axis. Note
                    // that in JavaScript, months start at 0 for January, 1 for February etc.
                    data:seriesDoubleData${status.count}
                }

            ]
        });
        </c:forEach>
    });
</script>
<article>
    <c:forEach items="${TYPES}" var="entryTypes" varStatus="status">
    <bigmap style="height: 635px;">
        <h2 class="bigmap">${titles[status.index]}</h2>

        <div id="container${status.count}" style="width: 940px; height: 600px"></div>
    </bigmap>
    </c:forEach>
</article>