<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="helpPage" scope="request" class="java.lang.String"/>
<jsp:useBean id="staticData" scope="request" type="java.util.LinkedHashMap<java.lang.Integer,java.lang.String>"/>
<jsp:useBean id="titles" scope="request" type="java.util.HashMap<java.lang.Integer,java.lang.String>"/>
<script type="text/javascript">

    function show(id) {
    <c:forEach items="${staticData}" var="hofProfile">
        $('#div-${hofProfile.key}').hide();
        $('#li-${hofProfile.key}').removeClass("selected");
    </c:forEach>
        var li = '#li-' + id;
        var div = '#div-' + id;
        $(li).addClass("selected");
        $(div).show();
        resizePagehead();

    }
    if ($.cookie('isPlayersPanelOpen') == null) {
        $.cookie('isPlayersPanelOpen', '0', { expires:7, path:'/', domain:'.eaw1805.com' });
    }

    $(document).ready(function () {
        <c:if test="${param.id != null}">
        <c:if test="${fn:contains(staticData, param.id)}">
        show(${param.id});
        resizePagehead();
        </c:if>
        </c:if>


        $(function () {
            var width = 389;
            if ($('.pagehead').css('background-image') == 'none') {
                width = 344;
            }
            if ($(document).height() <= $(window).height()) {
                $('.pagehead').css({ 'min-height':(($(window).height()) - width)});
            }
        });
        setPlayersPanelPosition();
    });

    $(window).resize(function () {
        $(function () {
            var width = 389;
            if ($('.pagehead').css('background-image') == 'none') {
                width = 344;
            }
            if ($(document).height() <= $(window).height()) {
                $('.pagehead').css({ 'min-height':(($(window).height()) - width)});
            } else if ($('.pagehead').height() >= $('content').height()) {
                $('.pagehead').css({ 'min-height':(($(window).height()) - width)});
            }
        });
        setPlayersPanelPosition();
    });


    function resizePagehead() {
        if ($(document).height() <= $(window).height()) {
            $('.pagehead').css({ 'min-height':(($(window).height()) - 389)});
        } else if ($('.pagehead').height() >= $('content').height()) {
            $('.pagehead').css({ 'min-height':(($(window).height()) - 389)});
        }
    }
</script>
<h1 style="margin-top: -7px;margin-bottom: 20px;">${title}</h1>

<div class="help" style="float: left;margin-left: -29px;">
    <ul class="help-menu">
        <c:set var="isAnyActive" value="false"/>

        <c:forEach items="${staticData}" var="hofProfile">
            <c:choose>
                <c:when test="${!isAnyActive}">
                    <c:set var="isAnyActive" value="true"/>
                    <li id="li-${hofProfile.key}" class="selected">
                        <a href="javascript:show(${hofProfile.key})">
                            <span><span> ${titles[hofProfile.key]}</span></span>
                        </a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li id="li-${hofProfile.key}">
                        <a href="javascript:show(${hofProfile.key})">
                            <span><span> ${titles[hofProfile.key]}</span></span>
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </ul>
</div>
<c:set var="isAnyActive" value="false"/>
<c:forEach items="${staticData}" var="hofProfile">
    <c:choose>
        <c:when test="${!isAnyActive}">
            <c:set var="isAnyActive" value="true"/>
            <div id="div-${hofProfile.key}" style="float: left;width: 770px;margin-top: 0px;margin-left: 18px;">
                    ${hofProfile.value}
            </div>
        </c:when>
        <c:otherwise>
            <div id="div-${hofProfile.key}"
                 style="float: left;width: 770px;margin-top: 0px;margin-left: 18px; display:none; ">
                    ${hofProfile.value}
            </div>
        </c:otherwise>
    </c:choose>
</c:forEach>
