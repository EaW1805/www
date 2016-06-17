<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="article" scope="request" type="java.lang.String"/>

<link rel="stylesheet" type="text/css" href="http://static.eaw1805.com/site/cache/widgetkit/widgetkit-2e15391b.css"/>


<script type="text/javascript" src='<c:url value="/site/media/widgetkit/widgets/lightbox/js/lightbox.js"/>'></script>

<script type="text/javascript" src='<c:url value="/site/media/widgetkit/widgets/spotlight/js/spotlight.js"/>'></script>

<script type="text/javascript"
        src='<c:url value="/site/media/widgetkit/widgets/mediaplayer/mediaelement/mediaelement-and-player.js"/>'></script>


<script type="text/javascript" src='<c:url value="/js/widgetkit-69a2e5f4.js"/>'></script>

<%--


<script type="text/javascript" src="http://static.eaw1805.com/site/templates/yoo_cloud/js/template.js"></script>

<script type="text/javascript" src="http://static.eaw1805.com/site/media/system/js/mootools.js"></script>

<script type="text/javascript" src='<c:url value="/site/media/system/js/caption.js"/>'></script>

<script type="text/javascript" src="http://static.eaw1805.com/site/templates/yoo_cloud/warp/js/warp.js"></script>
<script type="text/javascript"
        src="http://static.eaw1805.com/site/templates/yoo_cloud/warp/js/accordionmenu.js"></script>
<script type="text/javascript"
        src="http://static.eaw1805.com/site/templates/yoo_cloud/warp/js/dropdownmenu.js"></script>
--%>

${article}
