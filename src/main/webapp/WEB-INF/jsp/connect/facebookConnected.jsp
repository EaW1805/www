<%@page contentType="text/html;charset=UTF-8" %>
<%@page language="java" session="true" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>




<script type="text/javascript">
    window.location = <c:choose>
            <c:when test="${sessionScope['redirect'] != null}">
            '${sessionScope["redirect"]}';
    <c:remove var="redirect" scope="session" />
    </c:when>
    <c:otherwise>
    '<c:url value="/settings"/>';
    </c:otherwise>
    </c:choose>
</script>