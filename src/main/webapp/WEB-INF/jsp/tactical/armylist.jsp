<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="fortress" scope="request" type="java.lang.String"/>
<jsp:useBean id="terrainType" scope="request" class="com.eaw1805.data.model.map.Terrain"/>
<jsp:useBean id="region" scope="request" class="com.eaw1805.data.model.map.Region"/>
<jsp:useBean id="nation1" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="nation2" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="rank1" scope="request" class="com.eaw1805.data.model.army.Rank"/>
<jsp:useBean id="rank2" scope="request" class="com.eaw1805.data.model.army.Rank"/>
<jsp:useBean id="armyList1" scope="request" type="java.util.List"/>
<jsp:useBean id="armyList2" scope="request" type="java.util.List"/>
<h1 class="tacticalengine">Tactical Battle Engine: Army Selection</h1>
<form:form method="POST" commandName="battleCommand" action="perform" acceptCharset="UTF-8">
<h3>Region: ${region.name}</h3><input type="hidden" id="region" name="region" value="${region.id}"/>

<h3>Terrain type: ${terrainType.name}</h3><input type="hidden" id="terrainType" name="terrainType"
                                                 value="${terrainType.id}"/>
<table width="100%">
    <thead>
    <tr>
        <th width="50%" valign="top">
            <table width="100%" align=left>
                <tr valign="top">
                    <td width=130 rowspan=3 valign="top"><img
                            src='http://static.eaw1805.com/images/nations/nation-${nation1.id}-120.png'
                            alt="Nation's Flag"
                            class="toolTip"
                            title="Flag of ${nation1.name}"
                            border=0 width=120></td>
                    <td><h2>${nation1.name}</h2></td>
                    <input type="hidden" id="nation[0]" name="nation[0]" value="${nation1.id}"/>
                </tr>
                <tr>
                    <td><h3><img src='http://static.eaw1805.com/img/commanders/rank/${rank1.rankId}.png'
                                 alt="Commander's Rank"
                                 class="toolTip"
                                 title="${rank1.name}"
                                 border=0 width="24">${rank1.name}</h3></td>
                    <input type="hidden" id="rank[0]" name="rank[0]" value="${rank1.rankId}"/>
                </tr>
                <tr>
                    <td><h3>Fortress level: ${fortress}</h3></td>
                    <input type="hidden" id="fortress" name="fortress" value="${fortress}"/>
                </tr>
            </table>
        </th>
    </tr>
    </thead>
    <tbody>
    <tr valign="top">
        <td width="50%" valign="top">
            <table class="special">
                <thead class="special">
                <tr class="special" class="special">
                    <th class="special" align="left" width=100>Troop Type</th>
                    <th class="special" width=1>Cat</th>
                    <th class="special" width=1>LC</th>
                    <th class="special" width=1>LR</th>
                    <th class="special" width=1>HC</th>
                    <th class="special" width=1>Exp</th>
                    <th class="special" width=1>Cost</th>
                    <th class="special" width=1>IndPt</th>
                    <th class="special" width=1>Other</th>
                    <th class="special" width=20>Battalions</th>
                    <th class="special" width=20>Headcount</th>
                    <th class="special" width=20>Experience</th>
                </tr>
                </thead>
                <tbody class="special">
                <c:set var="index1" value="0"/>
                <c:forEach items="${armyList1}" var="armyType">
                    <tr
                            <c:choose>
                                <c:when test="${armyType.elite == true}">class="specialBlue"</c:when>
                                <c:when test="${armyType.crack == true}">class="specialCyan"</c:when>
                                <c:when test='${armyType.canColonies() == true || armyType.type.equals("CC") || armyType.type.equals("Kt")}'>class="specialBrown"</c:when>
                                <c:otherwise>class="special"</c:otherwise>
                            </c:choose>
                            >
                        <input type="hidden" id="id[0][${index1}]" name="id[0][${index1}]" value="${armyType.id}"/>
                        <td id="name[0][${index1}]" class="special" align="left" width=100>${armyType.name}</td>
                        <td id="type[0][${index1}]" class="special" width=1>${armyType.type}</td>
                        <td class="special" width=1>${armyType.longCombat}</td>
                        <td class="special" width=1>${armyType.longRange}</td>
                        <td class="special" width=1>${armyType.handCombat}</td>
                        <td class="special" width=1>${armyType.maxExp}</td>
                        <td class="special" width=1>${armyType.cost}</td>
                        <td class="special" width=1>${armyType.indPt}</td>
                        <td class="special" width=1>
                            <c:if test="${armyType.formationSk == true}">Sk&nbsp;</c:if>
                            <c:if test="${armyType.formationSq == true}">Sq&nbsp;</c:if>
                            <c:if test="${armyType.troopSpecsLc == true}">Lc&nbsp;</c:if>
                            <c:if test="${armyType.troopSpecsLr == true}">Lr&nbsp;</c:if>
                            <c:if test="${armyType.troopSpecsCu == true}">Cu&nbsp;</c:if>
                        </td>
                        <td class="special" width=20>
                            <input id="batt[0][${index1}]" name="batt[0][${index1}]" size="2" onkeyup="sum(0)"
                                   value="0"/>
                        </td>
                        <td class="special" width=20>
                            <input id="hc[0][${index1}]" size="3" readonly="true"/>
                        </td>
                        <td class="special" width=20>
                            <input id="exp[0][${index1}]" name="exp[0][${index1}]" size="1" value="3"/>
                        </td>
                    </tr>
                    <c:set var="index1" value="${index1+1}"/>
                </c:forEach>
                </tbody>
            </table>
            <script type="text/javascript">
                var list1
                list1 = <c:out value="${index1}"/>;
            </script>
        </td>
    </tr>
    <tr>
        <td width="50%" align="center">
            <table class="special">
                <thead class="special">
                <tr class="special">
                    <th class="special">Troop Type</th>
                    <th class="special"># Battalions</th>
                    <th class="special"># Troops</th>
                </tr>
                </thead>
                <tbody class="special">
                <tr class="special">
                    <td class="special"><img src='http://static.eaw1805.com/images/armies/dominant/infantry.png'
                                             title="Infantry"
                                             height=24
                                             border=0></td>
                    <td class="special"><input id="infbatt[0]" size="3" readonly="true"/></td>
                    <td class="special"><input id="infsize[0]" size="5" readonly="true"/></td>
                </tr>
                <tr class="special">
                    <td class="special"><img src='http://static.eaw1805.com/images/armies/dominant/cavalry.png'
                                             title="Cavalry"
                                             height=24
                                             border=0></td>
                    <td class="special"><input id="cavbatt[0]" size="3" readonly="true"/></td>
                    <td class="special"><input id="cavsize[0]" size="5" readonly="true"/></td>
                </tr>
                <tr class="special">
                    <td class="special"><img src='http://static.eaw1805.com/images/armies/dominant/artillery.png'
                                             title="Artillery"
                                             height=24
                                             border=0></td>
                    <td class="special"><input id="artbatt[0]" size="3" readonly="true"/></td>
                    <td class="special"><input id="artsize[0]" size="5" readonly="true"/></td>
                </tr>
                <tr class="special">
                    <td class="special"><img src='http://static.eaw1805.com/images/armies/dominant/engineers.png'
                                             title="Engineers"
                                             height=24
                                             border=0></td>
                    <td class="special"><input id="engbatt[0]" size="3" readonly="true"/></td>
                    <td class="special"><input id="engsize[0]" size="5" readonly="true"/></td>
                </tr>
                <tr class="special">
                    <td class="special"><b>Total</b></td>
                    <td class="special"><input id="totbatt[0]" size="3" readonly="true"/></td>
                    <td class="special"><input id="totsize[0]" size="5" readonly="true"/></td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>

<table width="100%">
    <thead>
    <tr>
        <th width="50%" valign="top">
            <table width="100%" align=left>
                <tr valign="top">
                    <td width=130 rowspan=3 valign="top"><img
                            src='http://static.eaw1805.com/images/nations/nation-${nation1.id}.png'
                            alt="Nation's Flag"
                            class="toolTip"
                            title="Flag of ${nation1.name}"
                            border=0 width=120></td>
                    <td><h2>${nation2.name}</h2></td>
                    <input type="hidden" id="nation[1]" name="nation[1]" value="${nation2.id}"/>
                </tr>
                <tr>
                    <td><h3><img src='http://static.eaw1805.com/img/commanders/rank/${rank2.rankId}.png'
                                 alt="Commander's Rank"
                                 class="toolTip"
                                 title="${rank1.name}"
                                 border=0 width="24">${rank2.name}</h3></td>
                    <input type="hidden" id="rank[1]" name="rank[1]" value="${rank2.rankId}"/>
                </tr>
            </table>
        </th>
    </tr>
    </thead>
    <tbody>
    <tr valign="top">
        <td width="50%" valign="top">
            <table class="special">
                <thead class="special">
                <tr class="special" class="special">
                    <th class="special" align="left" width=100>Troop Type</th>
                    <th class="special" width=1>Cat</th>
                    <th class="special" width=1>LC</th>
                    <th class="special" width=1>LR</th>
                    <th class="special" width=1>HC</th>
                    <th class="special" width=1>Exp</th>
                    <th class="special" width=1>Cost</th>
                    <th class="special" width=1>IndPt</th>
                    <th class="special" width=1>Other</th>
                    <th class="special" width=20>Battalions</th>
                    <th class="special" width=20>Headcount</th>
                    <th class="special" width=20>Experience</th>
                </tr>
                </thead>
                <tbody class="special">
                <c:set var="index2" value="0"/>
                <c:forEach items="${armyList2}" var="armyType">
                    <tr
                            <c:choose>
                                <c:when test="${armyType.elite == true}">class="specialBlue"</c:when>
                                <c:when test="${armyType.crack == true}">class="specialCyan"</c:when>
                                <c:when test='${armyType.canColonies() == true || armyType.type.equals("CC") || armyType.type.equals("Kt")}'>class="specialBrown"</c:when>
                                <c:otherwise>class="special"</c:otherwise>
                            </c:choose>
                            >
                        <input type="hidden" id="id[1][${index2}]" name="id[1][${index2}]" value="${armyType.id}"/>
                        <td id="name[1][${index2}]" class="special" align="left" width=100>${armyType.name}</td>
                        <td id="type[1][${index2}]" class="special" width=1>${armyType.type}</td>
                        <td class="special" width=1>${armyType.longCombat}</td>
                        <td class="special" width=1>${armyType.longRange}</td>
                        <td class="special" width=1>${armyType.handCombat}</td>
                        <td class="special" width=1>${armyType.maxExp}</td>
                        <td class="special" width=1>${armyType.cost}</td>
                        <td class="special" width=1>${armyType.indPt}</td>
                        <td class="special" width=1>
                            <c:if test="${armyType.formationSk == true}">Sk&nbsp;</c:if>
                            <c:if test="${armyType.formationSq == true}">Sq&nbsp;</c:if>
                            <c:if test="${armyType.troopSpecsLc == true}">Lc&nbsp;</c:if>
                            <c:if test="${armyType.troopSpecsLr == true}">Lr&nbsp;</c:if>
                            <c:if test="${armyType.troopSpecsCu == true}">Cu&nbsp;</c:if>
                        </td>
                        <td class="special" width=20>
                            <input id="batt[1][${index2}]" name="batt[1][${index2}]" size="2" onkeyup="sum(1)"
                                   value="0"/>
                        </td>
                        <td class="special" width=20>
                            <input id="hc[1][${index2}]" size="3" readonly="true"/>
                        </td>
                        <td class="special" width=20>
                            <input id="exp[1][${index2}]" name="exp[1][${index2}]" size="1" value="3"/>
                        </td>
                    </tr>
                    <c:set var="index2" value="${index2+1}"/>
                </c:forEach>
                </tbody>
            </table>
            <script type="text/javascript">
                var list2
                list2 = <c:out value="${index2}"/>;
            </script>
        </td>
    </tr>
    <tr>
        <td width="50%" align="center">
            <table class="special">
                <thead class="special">
                <tr class="special">
                    <th class="special">Troop Type</th>
                    <th class="special"># Battalions</th>
                    <th class="special"># Troops</th>
                </tr>
                </thead>
                <tbody class="special">
                <tr class="special">
                    <td class="special"><img src='http://static.eaw1805.com/images/armies/dominant/infantry.png'
                                             title="Infantry"
                                             height=24
                                             border=0></td>
                    <td class="special"><input id="infbatt[1]" size="3" readonly="true"/></td>
                    <td class="special"><input id="infsize[1]" size="5" readonly="true"/></td>
                </tr>
                <tr class="special">
                    <td class="special"><img src='http://static.eaw1805.com/images/armies/dominant/cavalry.png'
                                             title="Cavalry"
                                             height=24
                                             border=0></td>
                    <td class="special"><input id="cavbatt[1]" size="3" readonly="true"/></td>
                    <td class="special"><input id="cavsize[1]" size="5" readonly="true"/></td>
                </tr>
                <tr class="special">
                    <td class="special"><img src='http://static.eaw1805.com/images/armies/dominant/artillery.png'
                                             title="Artillery"
                                             height=24
                                             border=0></td>
                    <td class="special"><input id="artbatt[1]" size="3" readonly="true"/></td>
                    <td class="special"><input id="artsize[1]" size="5" readonly="true"/></td>
                </tr>
                <tr class="special">
                    <td class="special"><img src='http://static.eaw1805.com/images/armies/dominant/engineers.png'
                                             title="Engineers"
                                             height=24
                                             border=0></td>
                    <td class="special"><input id="engbatt[1]" size="3" readonly="true"/></td>
                    <td class="special"><input id="engsize[1]" size="5" readonly="true"/></td>
                </tr>
                <tr class="special">
                    <td class="special"><b>Total</b></td>
                    <td class="special"><input id="totbatt[1]" size="3" readonly="true"/></td>
                    <td class="special"><input id="totsize[1]" size="5" readonly="true"/></td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>


<input type="submit"/>
</form:form>
<script type="text/javascript">
    sum(0);
    sum(1);
</script>
