<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="nationList" scope="request" type="java.util.List"/>
<jsp:useBean id="terrainList" scope="request" type="java.util.List"/>
<jsp:useBean id="regionList" scope="request" type="java.util.List"/>
<jsp:useBean id="rankList" scope="request" type="java.util.List"/>
<jsp:useBean id="fortressList" scope="request" type="java.lang.String[]"/>
<c:url value="/tactical/battle" var="theUrl"/>
<h1 class="tacticalengine">Tactical Battle Engine: Opponents Selection</h1>
<form:form method="POST" commandName="battleCommand" acceptCharset="UTF-8" action="${theUrl}">
    <fieldset>
        <legend>Place &amp; Opponents</legend>
        <dl>
            <dd>
                <label for="region"><b>Region</b></label>
                <form:select path="region" items="${regionList}"
                             id="region"
                             itemValue="id"
                             itemLabel="name"/>
            </dd>
            <dd>
                <label for="terrainType"><b>Terrain</b></label>
                <form:select path="terrainType" items="${terrainList}"
                             id="terrainType"
                             itemValue="id"
                             itemLabel="name"/>
            </dd>
            <dd>
                <label for="nation1"><b>Side 1</b></label>
                <form:select path="nation1" items="${nationList}"
                             id="nation1"
                             itemValue="id"
                             itemLabel="name"/>
            </dd>
            <dd>
                <label for="rank1"><b>Side 1 - Commander</b></label>
                <form:select path="rank1" items="${rankList}"
                             id="rank1"
                             itemValue="rankId"
                             itemLabel="name"/>
            </dd>
            <dd>
                <label for="fortress"><b>Side 1 - Fortress</b></label>
                <form:select path="fortress" items="${fortressList}" id="fortress"/>
            </dd>
            <dd>
                <label for="nation2"><b>Side 2</b></label>
                <form:select path="nation2" items="${nationList}"
                             id="nation2"
                             itemValue="id"
                             itemLabel="name"/>
            </dd>
            <dd>
                <label for="rank2"><b>Side 2 - Commander</b></label>
                <form:select path="rank2" items="${rankList}"
                             id="rank2"
                             itemValue="rankId"
                             itemLabel="name"/>
            </dd>
        </dl>
    </fieldset>
    <input type="submit"/>
</form:form>
