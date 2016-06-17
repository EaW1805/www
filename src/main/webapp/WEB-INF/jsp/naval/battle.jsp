<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="nationList" scope="request" type="java.util.List"/>
<jsp:useBean id="weatherList" scope="request" type="java.lang.String[]"/>
<h1>Naval Battle Engine: Opponents Selection</h1>
<c:url value="/naval/battle" var="theUrl"/>
<form:form method="POST" commandName="battleCommand" action="${theUrl}" acceptCharset="UTF-8">
    <fieldset>
        <legend>Weather &amp; Opponents</legend>
        <dl>
            <dd>
                <label for="weather"><b>Weather</b></label>
                <form:select path="weather" items="${weatherList}" id="weather"/>
            </dd>
            <dd>
                <label for="nation1"><b>Side 1</b></label>
                <form:select path="nation1" items="${nationList}"
                             id="nation1"
                             itemValue="id"
                             itemLabel="name"/>
            </dd>
            <dd>
                <label for="nation2"><b>Side 2</b></label>
                <form:select path="nation2" items="${nationList}"
                             id="nation2"
                             itemValue="id"
                             itemLabel="name"/>
            </dd>
        </dl>
    </fieldset>
    <input type="submit"/>
</form:form>
