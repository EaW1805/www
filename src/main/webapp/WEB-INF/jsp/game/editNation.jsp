<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<jsp:useBean id="gameWarehouse" scope="request" class="java.util.List<com.eaw1805.data.model.economy.Warehouse>"/>
<jsp:useBean id="scenarioGoods" scope="request" class="java.util.List<com.eaw1805.data.model.economy.Good>"/>
<%@ page session="false" %>

<script type="text/javascript">
    //init autocommplete functionality.
    $(function () {
        var allUsers = new Array();
        <c:forEach items="${allUsers}" var="curUser" varStatus="status">
        allUsers[${status.index}] = '${curUser.username}';
        </c:forEach>
        var a2 = $('#newUser').autocomplete({
            delimiter:/(,|;)\s*/,
            lookup:allUsers
        });
        a2.setOptions({ zIndex:1000000 });
    });
</script>
<section style="height: 785px; width: 623px;">
    <div style="margin: 8px;">
        <h3 class="remove-margin-t">Edit Nation</h3>
        <form:form commandName="userGame" method="POST" action='edit'>
            <div style="margin:0;padding:0"></div>
            <div class="formbody">
                <label for="newUser">Username:</label>
                <input name="newUser" id="newUser" value="${gameUser.username}"/>
                <%--<form:errors path="gameUser" cssClass="error"/>--%><br/><br/>

                <label for="globalCost">Default Cost:</label>
                <span id="globalCost">${userGame.nation.cost}</span>
                <br><br>
                    <%--<form:errors path="alive" cssClass="error"/><br/><br/>--%>
                <label for="newCost">Cost For This Game:</label>
                <input name="newCost" id="newCost" value="${userGame.cost}"/>
                <br><br>

                <label for="discount">Discount:</label>
                <span id="discount">${userGame.game.discount}%</span>
                <br><br>

                <label for="active">Active:</label>
                <form:checkbox path="active" id="active"/>
                <form:errors path="active" cssClass="active"/><br/><br/>

                <table border="1">
                    <tr>
                        <th></th>
                        <c:forEach items="${gameWarehouse}" var="warehouse">
                            <th>${warehouse.region.name}</th>
                        </c:forEach>
                    </tr>
                    <c:forEach items="${scenarioGoods}" var="good">
                        <tr>
                            <th>${good.name}</th>
                            <c:forEach items="${gameWarehouse}" var="warehouse">
                                <th title="${good.name} ${warehouse.region.name}"><input style="width:100px;" name="good_${good.goodId}_${warehouse.region.id}" value="${warehouse.storedGoodsQnt[good.goodId]}"/></th>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </table><br><br>


                <button type="submit">Save Nation Changes</button>
                <input type="button" value="Cancel"
                       onclick="window.location='<c:url value="/scenario/${game.scenarioIdToString}/game/${userGame.game.gameId}/edit"/>'"/>
            </div>
            <a href="<c:url value="/scenario/${userGame.game.scenarioIdToString}/game/${userGame.game.gameId}/nation/${userGame.nation.id}/vps"/>">Edit VPs</a>
        </form:form>
    </div>
</section>
