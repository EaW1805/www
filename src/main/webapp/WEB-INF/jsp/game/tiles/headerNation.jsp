<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="game" scope="request" class="com.eaw1805.data.model.Game"/>
<jsp:useBean id="turnMonth" scope="request" class="java.lang.String"/>
<jsp:useBean id="nationId" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="nation" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="titleFirst" scope="request" class="java.lang.String"/>
<jsp:useBean id="titleSecond" scope="request" class="java.lang.String"/>
<jsp:useBean id="sizeFirst" scope="request" class="java.lang.Integer"/>
<jsp:useBean id="sizeSecond" scope="request" class="java.lang.Integer"/>

<div class="player" style="height: 200px;">
    <h1 class="news" style="font-size: ${sizeFirst}pt !important;">
    <div style="position: absolute; top: 39px;" >${titleFirst}</div>
    </h1>

    <h2 class="news">Game ${game.gameId}</h2>

    <h3 class="news" style="font-size: ${sizeSecond}pt !important;">${titleSecond}</h3>

    <h5 class="news">${game.description}</h5>

    <h4 class="news">${turnMonth}</h4>
</div>
