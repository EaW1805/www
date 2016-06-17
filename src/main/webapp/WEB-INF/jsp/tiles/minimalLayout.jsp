<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%--<jsp:useBean id="user" scope="request" type="com.eaw1805.data.model.User"/>--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:c="http://www.springframework.org/schema/util"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/util ">
<head>
    <META NAME="AUTHOR" CONTENT="Oplon Games">
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
    <META NAME="COPYRIGHT" CONTENT="&copy; 2011-2014 Oplon Games">
    <META HTTP-EQUIV="EXPIRES" CONTENT="0">
    <META NAME="ROBOTS" CONTENT="INDEX,FOLLOW">
    <meta name="distribution" content="global"/>
    <meta name="keywords" content="Strategy Diplomacy Napoleonic-Era Napoleon War Empires"/>
    <META NAME="Description" CONTENT="Empires at War 1805 Napoleonic-Era Web-based Strategy Game"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <META http-equiv="content-style-type" content="text/css"/>
    <META http-equiv="imagetoolbar" content="no"/>
    <META name="resource-type" content="document"/>
    <link rel="icon" type="image/ico" href='http://static.eaw1805.com/images/site/eaw1805-favicon.ico'/>
    <title>Empires at War 1805: <tiles:insertAttribute name="title" ignore="true"/></title>

    <link rel="stylesheet" type="text/css" href='http://direct.eaw1805.com/style/basic.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/widgetkit.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/autocomplete-styles.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/sliderman.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/InfoPanels.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/panels.css'/>
    <link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/ArmyRelated.css'/>
</head>
<body>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery-1.6.3.min.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.qtip.min.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.autocomplete.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/qtip-init.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.ui.core.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.ui.widget.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.ui.mouse.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.ui.draggable.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.cookie.js'></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/aplweb.scrollbars.js"></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/jquery.event.drag-2.0.min.js"></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/jquery.ba-resize.min.js"></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/mousehold.js"></script>
<script type="text/javascript" src="http://static.eaw1805.com/js/jquery.mousewheel.js"></script>
<script type="text/javascript">

    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-24086810-1']);
    _gaq.push(['_trackPageview']);

    (function () {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();

</script>
<script>
    $(function () {
        $('.pageheadMinimal [title]').qtip({
            position:{
                viewport:$(window)
            }
        });

        $('[tooltip]').addClass("pointer");

        $('[tooltip]').click(function () {
            tooltipcustomcontainerid++;
            largerZIndex++;
            var currentId = tooltipcustomcontainerid;
            //create container
            $('body').append("<div id='tooltip-container" + tooltipcustomcontainerid + "' onmousedown='setLargerZIndex(this);' style='display: none;position: fixed; left: 0px;top: 0px; z-index: " + largerZIndex + ";'></div>");
            var ttip = $(this).attr("tooltip");
            //add content to container
            $("#tooltip-container" + tooltipcustomcontainerid).html(ttip);
            $("#tooltip-container" + tooltipcustomcontainerid).show();
            //draggable it
            $(".draggablePopup").draggable();
            $('.draggablePopup').draggable({
                cursor:'move', zIndex:20000, handle:'#handleDrag'
            });
            //enable close functionality
            $("#tooltip-container" + tooltipcustomcontainerid + " .draggableCloser").click(function () {
                hidePopup(currentId);
            });

//        $("#tooltip-container").center(true);
        });
        //(re)init external tooltips
        initExternalTooltips();
    });
</script>
<div class="pageheadMinimal" style="padding-left:13px;">
    <tiles:insertAttribute name="header"/>
    <div id="content">
        <tiles:insertAttribute name="body"/>
    </div>
</div>

</body>
</html>
