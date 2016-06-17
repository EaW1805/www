<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="http://static.eaw1805.com/style/panels.css"/>
    <script type="text/javascript" src='http://static.eaw1805.com/js/jquery-1.6.3.min.js'></script>
</head>

<body style="background-color: black;" class="loginPanel">

<div style="width:100%;height:100%;position: relative;" ></div>
<div id="loadingDiv" style="position: absolute;left:-100px;top:0px;">
    <img src="http://static.eaw1805.com/images/loading/LoadingScreen.png"
         style="position: absolute;left: 10px;top:10px;"/>

    <div style="width: 432px; height: 30px; position: absolute; top: 490px; left: 546px;">
        <svg overflow="hidden" width="432" height="30">
            <defs></defs>
            <image id="img1" preserveAspectRatio="none" x="6" y="6" width="35" height="20"
                   xlink:href="http://static.eaw1805.com/images/loading/loadingBar1.png"></image>
            <image id="img2" preserveAspectRatio="none" x="41" y="6" width="33" height="20"
                   xlink:href="http://static.eaw1805.com/images/loading/loadingBar2.png"
                   style="display: none;"></image>
            <image id="img3" preserveAspectRatio="none" x="74" y="6" width="29" height="20"
                   xlink:href="http://static.eaw1805.com/images/loading/loadingBar3.png"
                   style="display: none;"></image>
            <image id="img4" preserveAspectRatio="none" x="103" y="6" width="27" height="20"
                   xlink:href="http://static.eaw1805.com/images/loading/loadingBar4.png"
                   style="display: none;"></image>
            <image id="img5" preserveAspectRatio="none" x="130" y="6" width="40" height="20"
                   xlink:href="http://static.eaw1805.com/images/loading/loadingBar5.png"
                   style="display: none;"></image>
            <image id="img6" preserveAspectRatio="none" x="170" y="6" width="38" height="20"
                   xlink:href="http://static.eaw1805.com/images/loading/loadingBar6.png"
                   style="display: none;"></image>
            <image id="img7" preserveAspectRatio="none" x="208" y="6" width="47" height="20"
                   xlink:href="http://static.eaw1805.com/images/loading/loadingBar7.png"
                   style="display: none;"></image>
            <image id="img8" preserveAspectRatio="none" x="254" y="6" width="46" height="20"
                   xlink:href="http://static.eaw1805.com/images/loading/loadingBar8.png"
                   style="display: none;"></image>
            <image id="img9" preserveAspectRatio="none" x="300" y="6" width="38" height="20"
                   xlink:href="http://static.eaw1805.com/images/loading/loadingBar9.png"
                   style="display: none;"></image>
            <image id="img10" preserveAspectRatio="none" x="338" y="6" width="40" height="20"
                   xlink:href="http://static.eaw1805.com/images/loading/loadingBar10.png"
                   style="display: none;"></image>
            <image id="img11" preserveAspectRatio="none" x="378" y="6" width="47" height="20"
                   xlink:href="http://static.eaw1805.com/images/loading/loadingBar11.png"
                   style="display: none;"></image>
        </svg>
    </div>
    <div class="clearFont" style="position: absolute; width: 400px; left: 550px; top: 529px;">Processing your game...
    </div>

</div>
<script type="text/javascript">
    $("#loadingDiv").css({"left":($(window).width() - 1440) / 2});
</script>
<%--${scenarioId} , ${gameId} , ${turnId}--%>

</body>

</html>

<script type="text/javascript">
    var currentImage = 2;
    var myVar = setInterval(function () {
        if (currentImage <= 11) {
            $("#img" + currentImage).show();
            currentImage++;
        }
    }, 15000);
    var done = false;
    var myVar = setInterval(function () {
        if (!done) {
            var currentTimeMillis = new Date().getTime();
            $.ajax({
                url:'<c:url value="/processing/scenario/${scenarioId}/game/${gameId}/turn/${turnId}/check"/>?rt=' + currentTimeMillis,
                dataType:"html",
                success:checkDone
            });
        }
    }, 10000);

    function checkDone(data) {
        if (data == 1) {
            done = true;
            window.location = '<c:url value="/play/scenario/${scenarioId}/game/${gameId}/nation/5"/>'
        }
    }

    $(window).resize(function() {
        $("#loadingDiv").css({"left":($(window).width() - 1440) / 2});
    });

</script>
