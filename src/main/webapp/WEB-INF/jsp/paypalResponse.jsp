<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<c:choose>
    <c:when test="${status == 'success'}">
        <h3>Success!!</h3>
    </c:when>
    <c:otherwise>
        <h3>Operation Canceled By YOU</h3>
    </c:otherwise>
</c:choose>