<%@page contentType="application/json;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:useBean id="game" scope="request" class="com.eaw1805.data.model.Game"/>
<jsp:useBean id="news" scope="request" type="java.util.List<com.eaw1805.data.model.News>"/>
<jsp:useBean id="months" scope="request" type="java.util.Map<java.lang.Integer, java.lang.String>"/>
{
"timeline":
    {
        "headline":"Game ${game.gameId} Timeline",
        "type":"default",
        "text":"${game.description}",
        "startDate":"<c:choose>
            <c:when test="${game.gameId <= 7}">1805,1,1</c:when>
            <c:when test="${game.scenarioId == 1}">1802,1,1</c:when>
            <c:when test="${game.scenarioId == 2}">1805,1,1</c:when>
            <c:otherwise>1802,1,1</c:otherwise>
        </c:choose>",
        "date": [
            <c:forEach items="${news}" var="entry">
                {
                    "startDate":"${months[entry.turn]}",
                    "headline":"${entry.text}",
                    "text":"",
                    "asset":
                        {
                            "media":"<c:choose>
                            <c:when test="${entry.subject.id == -1}">http://static.eaw1805.com/images/nations/nation-none-36.png</c:when>
                            <c:otherwise>http://static.eaw1805.com/images/nations/nation-${entry.subject.id}-36.png</c:otherwise>
                        </c:choose>",
                            "credit":"",
                            "caption":""
                        }
                },
            </c:forEach>
        ]
    }
}
