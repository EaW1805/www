<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="nationList" scope="request" type="java.util.List"/>
<jsp:useBean id="staticData" scope="request" type="java.lang.String"/>
<div id="slideshow-46-4f74d01455aa3" class="wk-slideshow wk-slideshow-listcloud"
     style="position: relative; width: 100%; visibility: visible; ">
    <div>
        <ul class="nav nav-250 clearfix">
            <c:forEach items="${nationList}" var="thisNation">
                <c:choose>
                    <c:when test="${nationId == thisNation.id}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                <span>
                     <a href='<c:url value="/scenario/${scenarioStr}/nation/${thisNation.id}"/>'>
                         <img style="vertical-align: middle;" alt="${thisNation.name}"
                              src='http://static.eaw1805.com/images/nations/nation-${thisNation.id}-list.jpg'>
                     </a>
               </span>
                </li>
            </c:forEach>
        </ul>
        <div class="slides-container">
            ${staticData}
        </div>
    </div>
    <div class="globalCaption"></div>
</div>
