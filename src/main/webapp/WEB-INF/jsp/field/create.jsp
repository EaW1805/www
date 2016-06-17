<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<jsp:useBean id="terrains" scope="request" class="java.util.List<com.eaw1805.data.model.map.Terrain>"/>
<jsp:useBean id="productions" scope="request" class="java.util.List<com.eaw1805.data.model.map.ProductionSite>"/>
<jsp:useBean id="nations" scope="request" class="java.util.List<com.eaw1805.data.model.Nation>"/>
<form action="create" method="post">


    <label for="terrain">Terrain:</label>

    <div>
        <select name="terrain" id="terrain">
            <c:forEach items="${terrains}" var="terrain">
                <option value="${terrain.id}">${terrain.name}</option>
            </c:forEach>
        </select>
    </div>

    <label for="production">Production site:</label>
    <div>
        <select name="production" id="production">
            <c:forEach items="${productions}" var="production">
                <option value="${production.id}">${production.name}</option>
            </c:forEach>
        </select>
    </div>
    <label for="population">Population density:</label>
    <div>
        <select name="population" id="population">
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
            <option value="6">6</option>
            <option value="7">7</option>
            <option value="8">8</option>
            <option value="9">9</option>
        </select>
    </div>
    <input type="button" value="Another nation" onclick="duplicateNation(1); return false;">
    <label for="nation1">Side 1:</label>
    <div>
        <select name="nation1" id="nation1">
            <c:forEach items="${nations}" var="nation">
                <option value="${nation.id}">${nation.name}</option>
            </c:forEach>
        </select>
    </div>

    <label for="nation2">Side 2:</label>
    <input type="button" value="Another nation" onclick="duplicateNation(2); return false;">
    <div>
        <select name="nation2" id="nation2">
            <c:forEach items="${nations}" var="nation">
                <option value="${nation.id}">${nation.name}</option>
            </c:forEach>
        </select>
    </div>

    <label for="armySize">Army Size:</label>
    <div>
        <select name="armySize" id="armySize">
            <option selected="selected" value="1">Small</option>
            <option value="2">Medium</option>
            <option value="3">Large</option>
            <option value="4">Huge</option>
        </select>
    </div>

    <input type="submit" value="create game"/>


</form>

<script type="text/javascript">
    function duplicateNation(side) {
        var originalSel = $("#nation" + side);
        var copySel = originalSel.clone();
        originalSel.after(copySel);

    }
</script>
