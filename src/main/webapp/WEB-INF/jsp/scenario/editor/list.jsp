<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%--<jsp:useBean id="usersScenarios" scope="request" class="java.util.List<com.eaw1805.data.model.Game>"/>--%>
<%--<jsp:useBean id="notUsersScenarios" scope="request" class="java.util.List<com.eaw1805.data.model.Game>"/>--%>

<article>
    <section class="section-followers" style="height: 50px;">
        <h1>Scenario Editor - Create new</h1>
        <ul>
            <li style="list-style-type: none;">
                <a href="<c:url value="/create/scenario"/>"
                   title="Scenario editor">Create new scenario
                </a>
            </li>
        </ul>
    </section>
</article>

<article>
    <section class="section-followers" style="overflow: auto;">
        <h1>Scenario Editor - Scenarios Owned by you</h1>

        <ul>
            <c:forEach items="${usersScenarios}" var="scenario">
                <li style="list-style-type: none;">
                    <a href="<c:url value="/create/scenario/${scenario.gameId}"/>"
                       title="Scenario editor">Scenario "${scenario.name}" ${scenario.gameId}
                    </a>
                </li>
            </c:forEach>

        </ul>
    </section>
</article>

<c:if test="${user.userType == 3 }">
<article>
    <section class="section-followers" style="overflow: auto;">
        <h1>Scenario Editor - Scenarios Owned by others</h1>

        <ul>
            <c:forEach items="${notUsersScenarios}" var="scenario">
                <li style="list-style-type: none;">
                    <a href="<c:url value="/create/scenario/${scenario.gameId}"/>"
                       title="Scenario editor">Scenario "${scenario.name}" ${scenario.gameId}
                    </a>
                </li>
            </c:forEach>

        </ul>
    </section>
</article>
</c:if>
