<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="req" value="${pageContext.request}"/>
<c:set var="uri" value="${req.requestURI}"/>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="icon" type="image/ico" href='http://static.eaw1805.com/images/site/eaw1805-favicon.ico'/>

    <!--                                                               -->
    <!-- Consider inlining CSS to reduce the number of requested files -->
    <!--                                                               -->
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/CustomWidgetStyles.css'/>
    <link type="text/css" rel="stylesheet" href="http://static.eaw1805.com/style/RichTextToolbar.css"/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/jquery.qtip.min.css'/>
    <!--                                           -->
    <!-- Any title is fine                         -->
    <!--                                           -->
    <title>Empires at War 1805</title>
    <!--just a test-->
    <script src="//code.jquery.com/jquery-1.10.2.js"></script>
    <script type="text/javascript" language="javascript"
            src="http://static.eaw1805.com/js/gwtexternal/parseJavascript.js"></script>
    <script src="http://static.eaw1805.com/js/highcharts.js" type="text/javascript"></script>
    <script src="http://static.eaw1805.com/js/gray.js" type="text/javascript"></script>
    <script type="text/javascript" src='http://static.eaw1805.com/js/jquery.transform.js'></script>
    <script type="text/javascript" language="javascript" src='http://static.eaw1805.com/js/jquery.qtip.min.js'></script>
    <script type="text/javascript" language="javascript"
            src='http://static.eaw1805.com/js/jquery.autocomplete.js'></script>
    <script type="text/javascript" language="javascript" src='http://static.eaw1805.com/js/qtip-init.js'></script>
    <script type="text/javascript" language="javascript" src='http://static.eaw1805.com/js/jquery.ui.core.js'></script>
    <script type="text/javascript" language="javascript"
            src='http://static.eaw1805.com/js/jquery.ui.widget.js'></script>
    <script type="text/javascript" language="javascript" src='http://static.eaw1805.com/js/jquery.ui.mouse.js'></script>
    <script type="text/javascript" language="javascript"
            src='http://static.eaw1805.com/js/jquery.ui.draggable.js'></script>
    <%--this is for ads by google--%>
    <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
    <!--                                           -->
    <!-- This script loads your compiled module.   -->
    <!-- If you add any GWT meta tags, they must   -->
    <!-- be added before this line.                -->
    <!--                                           -->
    <script type="text/javascript" language="javascript"
            src="<c:url value='/com.eaw1805.www.EmpireFieldBattleClient/com.eaw1805.www.EmpireFieldBattleClient.nocache.js'/>?build=<spring:message code="eaw.version"/>"></script>

</head>
<body class="body">
<div id="fb-root"></div>
<script>
    window.fbAsyncInit = function() {
        try {

            FB.init({
            appId      : '105656276218992',//app id for deployment
//            appId      : '1467405233487625',//app id for developing
            status     : true,
            xfbml      : true
            });


        <c:choose>
            <c:when test="${standAlone == 1 && facebookId == ''}">
            FB.login(function(response) {
                if (response.authResponse) {
                    //wait 3 seconds before reloading.... seems important...
                    location.href = "token/" + response.authResponse.accessToken;

                }
            });
            </c:when>
            <c:otherwise>
                FB.Event.subscribe('auth.authResponseChange', function(response) {
                    if (response.status === 'connected') {
                        console.log('Logged in');
                    } else {
                        //FB.login();
                    }
                });
            </c:otherwise>
        </c:choose>







        } catch (e) {
            alert("failed to fb? " + e);
        }
    };


    (function(d, s, id){
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) {return;}
        js = d.createElement(s); js.id = id;
        js.src = "//connect.facebook.net/en_US/all.js";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));




</script>
<input type="hidden" name="scenarioId" id="scenarioId" value="${scenarioId}"/>
<input type="hidden" name="battleId" id="battleId" value="${battleId}"/>
<input type="hidden" name="nationId" id="nationId" value="${nationId}"/>
<input type="hidden" name="round" id="round" value="${round}"/>
<input type="hidden" name="side" id="side" value="${side}"/>
<input type="hidden" name="title" id="title" value="${title}"/>
<input type="hidden" name="sideReady" id="sideReady" value="${sideReady}"/>
<input type="hidden" name="gameEnded" id="gameEnded" value="${gameEnded}"/>
<input type="hidden" name="winner" id="winner" value="${winner}"/>
<input type="hidden" name="standAlone" id="standAlone" value="${standAlone}"/>
<input type="hidden" name="facebookId" id="facebookId" value="${facebookId}"/>

<!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
<noscript>
    <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
    </div>
</noscript>

<div id="BannerDiv"></div>
<div id="MainPanel"
<c:if test="${standAlone == 1 && facebookId == ''}">
    class="loginPanel"
</c:if>></div>
</body>
</html>
