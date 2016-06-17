<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="nationId" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="nation" scope="request" class="com.eaw1805.data.model.Nation"/>
<jsp:useBean id="nationList" scope="request" type="java.util.List<com.eaw1805.data.model.Nation>"/>
<jsp:useBean id="leaderName" scope="request" type="java.lang.String"/>
<jsp:useBean id="commanderList" scope="request" type="java.util.List<com.eaw1805.data.model.army.CommanderName>"/>
<jsp:useBean id="armyTypeList" scope="request" type="java.util.List<com.eaw1805.data.model.army.ArmyType>"/>
<jsp:useBean id="shipTypeList" scope="request" type="java.util.List<com.eaw1805.data.model.fleet.ShipType>"/>
<jsp:useBean id="staticData" scope="request" type="java.lang.String"/>
<jsp:useBean id="leaderData" scope="request" type="java.lang.String"/>
<jsp:useBean id="trait" scope="request" type="java.lang.String"/>
<jsp:useBean id="relations" scope="request" type="java.lang.String"/>
<jsp:useBean id="scenarioStr" scope="request" type="java.lang.String"/>
<jsp:useBean id="scenarioId" scope="request" type="java.lang.Integer"/>
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
            <h1 class="title">${nation.name}</h1>
            <img src='http://static.eaw1805.com/images/nations/nation-${nation.id}-trans.gif'
                 alt="Nation's Flag"
                 title="Flag of ${nation.name}"
                 border=0 width=200 align=left>
            ${staticData}

            <table border="0">
                <tr>
                    <th class="title" align="left" width="36"><img
                            src='http://static.eaw1805.com/images/buttons/taxation/MUINormalTax.png'
                            alt="Nation's Taxation"
                            class="toolTip"
                            title="Nation's Taxation"
                            border=0 width=32>&nbsp;Taxation
                    </th>
                    <td class="title" align="left">${nation.taxRate}</td>
                </tr>
                <tr>
                    <th class="title" align="left" width="195">Country trait</th>
                    <td class="title" align="left">${trait}</td>
                </tr>
                <tr>
                    <th class="title" align="left" width="195">Map Color</th>
                    <td class="title" align="left"><img
                            src='http://static.eaw1805.com/tiles/borders-fow/${nation.id}/base.png' height=16 width=48
                            border=1></td>
                </tr>
                <tr>
                    <th class="title" align="left" valign="top" width="150"><br>Starting Army</th>
                    <td class="title" align="left">
                        <table border=0>
                            <tr>
                                <td width="40">&nbsp;</td>
                                <td class="title" align="right" valign="top" width="70">&nbsp;Europe</td>
                                <td class="title" align="right" valign="top" width="70">&nbsp;Africa</td>
                                <td class="title" align="right" valign="top" width="70">&nbsp;Caribbean</td>
                                <td class="title" align="right" valign="top" width="70">&nbsp;Indies</td>
                            </tr>
                            <tr>
                                <td width="40"><img
                                        src='http://static.eaw1805.com/images/buttons/icons/formations/infantry.png'
                                        alt="Infantry" title="Infantry" border="0" height="18"></td>
                                <td class="title" align="right" valign="top"><fmt:formatNumber type="number"
                                                                                               maxFractionDigits="0"
                                                                                               groupingUsed="true"
                                                                                               value="${armyStats[0][0]}"/></td>
                                <td class="title" align="right" valign="top"><fmt:formatNumber type="number"
                                                                                               maxFractionDigits="0"
                                                                                               groupingUsed="true"
                                                                                               value="${armyStats[1][0]}"/></td>
                                <td class="title" align="right" valign="top"><fmt:formatNumber type="number"
                                                                                               maxFractionDigits="0"
                                                                                               groupingUsed="true"
                                                                                               value="${armyStats[2][0]}"/></td>
                                <td class="title" align="right" valign="top"><fmt:formatNumber type="number"
                                                                                               maxFractionDigits="0"
                                                                                               groupingUsed="true"
                                                                                               value="${armyStats[3][0]}"/></td>
                            </tr>
                            <tr>
                                <td width="40"><img
                                        src='http://static.eaw1805.com/images/buttons/icons/formations/cavalry.png'
                                        alt="Cavalry" title="Cavalry" border="0" height="18"></td>
                                <td class="title" align="right" valign="top"><fmt:formatNumber type="number"
                                                                                               maxFractionDigits="0"
                                                                                               groupingUsed="true"
                                                                                               value="${armyStats[0][1]}"/></td>
                                <td class="title" align="right" valign="top"><fmt:formatNumber type="number"
                                                                                               maxFractionDigits="0"
                                                                                               groupingUsed="true"
                                                                                               value="${armyStats[1][1]}"/></td>
                                <td class="title" align="right" valign="top"><fmt:formatNumber type="number"
                                                                                               maxFractionDigits="0"
                                                                                               groupingUsed="true"
                                                                                               value="${armyStats[2][1]}"/></td>
                                <td class="title" align="right" valign="top"><fmt:formatNumber type="number"
                                                                                               maxFractionDigits="0"
                                                                                               groupingUsed="true"
                                                                                               value="${armyStats[3][1]}"/></td>
                            </tr>
                            <tr>
                                <td width="40"><img
                                        src='http://static.eaw1805.com/images/buttons/icons/formations/artillery.png'
                                        alt="Infantry" title="Artillery" border="0" height="18"></td>
                                <td class="title" align="right" valign="top"><fmt:formatNumber type="number"
                                                                                               maxFractionDigits="0"
                                                                                               groupingUsed="true"
                                                                                               value="${armyStats[0][2]}"/></td>
                                <td class="title" align="right" valign="top"><fmt:formatNumber type="number"
                                                                                               maxFractionDigits="0"
                                                                                               groupingUsed="true"
                                                                                               value="${armyStats[1][2]}"/></td>
                                <td class="title" align="right" valign="top"><fmt:formatNumber type="number"
                                                                                               maxFractionDigits="0"
                                                                                               groupingUsed="true"
                                                                                               value="${armyStats[2][2]}"/></td>
                                <td class="title" align="right" valign="top"><fmt:formatNumber type="number"
                                                                                               maxFractionDigits="0"
                                                                                               groupingUsed="true"
                                                                                               value="${armyStats[3][2]}"/></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <th class="title" align="left" valign="top" width="150">Starting Navy</th>
                    <td class="title" align="left">
                        <table border=0>
                            <tr>
                                <td width="40"><img
                                        src='http://static.eaw1805.com/images/buttons/icons/formations/warship.png'
                                        alt="Warships" title="Warships" border="0" height="18"></td>
                                <td class="title" align="right" valign="top" width="70"><fmt:formatNumber type="number"
                                                                                                          maxFractionDigits="0"
                                                                                                          groupingUsed="true"
                                                                                                          value="${navyStats[0][0]}"/></td>
                                <td class="title" align="right" valign="top" width="70"><fmt:formatNumber type="number"
                                                                                                          maxFractionDigits="0"
                                                                                                          groupingUsed="true"
                                                                                                          value="${navyStats[1][0]}"/></td>
                                <td class="title" align="right" valign="top" width="70"><fmt:formatNumber type="number"
                                                                                                          maxFractionDigits="0"
                                                                                                          groupingUsed="true"
                                                                                                          value="${navyStats[2][0]}"/></td>
                                <td class="title" align="right" valign="top" width="70"><fmt:formatNumber type="number"
                                                                                                          maxFractionDigits="0"
                                                                                                          groupingUsed="true"
                                                                                                          value="${navyStats[3][0]}"/></td>
                            </tr>
                            <tr>
                                <td width="40"><img
                                        src='http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png'
                                        alt="Merchant Ships" title="Merchant Ships" border="0" height="18"></td>
                                <td class="title" align="right" valign="top" width="70"><fmt:formatNumber type="number"
                                                                                                          maxFractionDigits="0"
                                                                                                          groupingUsed="true"
                                                                                                          value="${navyStats[0][1]}"/></td>
                                <td class="title" align="right" valign="top" width="70"><fmt:formatNumber type="number"
                                                                                                          maxFractionDigits="0"
                                                                                                          groupingUsed="true"
                                                                                                          value="${navyStats[1][1]}"/></td>
                                <td class="title" align="right" valign="top" width="70"><fmt:formatNumber type="number"
                                                                                                          maxFractionDigits="0"
                                                                                                          groupingUsed="true"
                                                                                                          value="${navyStats[2][1]}"/></td>
                                <td class="title" align="right" valign="top" width="70"><fmt:formatNumber type="number"
                                                                                                          maxFractionDigits="0"
                                                                                                          groupingUsed="true"
                                                                                                          value="${navyStats[3][1]}"/></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <th class="title" align="left" width="150">Starting Political Relations</th>
                    <td class="title" align="left">${relations}</td>
                </tr>
                <tr>
                    <th class="title" align="left" width="150">Starting VP</th>
                    <td class="title" align="left">${nation.vpInit}</td>
                </tr>
                <tr>
                    <th class="title" align="left" width="150">VP to Win</th>
                    <td class="title" align="left">${nation.vpWin}</td>
                </tr>
            </table>

            <h2 class="title">Initial Territories</h2>
            <table border="0" cellspacing=15>
                <tr>
                    <td colspan=3><a href='http://static.eaw1805.com/maps/s${scenarioId}/scenario-geo-1.png'
                                     title="Geographical Map of Europe">
                        <img src='http://static.eaw1805.com/maps/s${scenarioId}/scenario-1.png'
                             alt="Initial Map of Europe"
                             title="Initial Map of Europe"
                             style="border-radius: 5px;"
                             border=0 height="648"></a></td>
                </tr>
                <c:if test="${scenarioId!=3}">
                    <tr>
                        <td><a href='http://static.eaw1805.com/maps/s${scenarioId}/scenario-geo-4.png'
                               title="Geographical Map of Africa">
                            <img src='http://static.eaw1805.com/maps/s${scenarioId}/scenario-4.png'
                                 alt="Initial Map of Africa"
                                 title="Initial Map of Africa"
                                 style="border-radius: 5px;"
                                 border="0" height="165"></a></td>
                        <td><a href='http://static.eaw1805.com/maps/s${scenarioId}/scenario-geo-2.png'
                               title="Geographical Map of Caribbean">
                            <img src='http://static.eaw1805.com/maps/s${scenarioId}/scenario-2.png'
                                 alt="Initial Map of Caribbean"
                                 title="Initial Map of Caribbean"
                                 style="border-radius: 5px;"
                                 border="0" height="165"></a></td>
                        <td><a href='http://static.eaw1805.com/maps/s${scenarioId}/scenario-geo-3.png'
                               title="Geographical Map of Indies">
                            <img src='http://static.eaw1805.com/maps/s${scenarioId}/scenario-3.png'
                                 alt="Initial Map of Indies"
                                 title="Initial Map of Indies"
                                 style="border-radius: 5px;"
                                 border="0" height="165"></a></td>
                    </tr>
                </c:if>
            </table>

        </div>
    </div>

    <script language="JavaScript" type="text/javascript"
            src="http://static.eaw1805.com/js/contentflow.js"></script>
    <script tyle="text/javascript">
        var cf1 = new ContentFlow('contentFlowPersonality', {
            reflectionColor: "#aaaaaa",
            maxItemHeight: 128,
            scaleFactorLandscape: 1.33,
            startItem: 2,
            circularFlow: false
        });

        var cf2 = new ContentFlow('contentFlowBattalion', {
            reflectionColor: "#aaaaaa",
            maxItemHeight: 128,
            scaleFactorLandscape: 1.33,
            startItem: 2,
            circularFlow: false
        });

        var cf3 = new ContentFlow('contentFlowShip', {
            reflectionColor: "#aaaaaa",
            maxItemHeight: 128,
            scaleFactorLandscape: 1.33,
            startItem: 2,
            circularFlow: false
        });
    </script>

    <h2 class="title">Leading Personality: ${leaderName}</h2>
    <img class="content" src="http://static.eaw1805.com/img/commanders/s${scenarioId}/${nation.id}/1.jpg" width="80"
         align="left"
         style="margin-right: 6px;"/>
    ${leaderData}

    <h2 class="title">Major Personalities</h2>
    <!-- ===== FLOW ===== -->
    <div id="contentFlowPersonality" class="ContentFlow">
        <!-- should be place before flow so that contained images will be loaded first -->
        <div class="loadIndicator">
            <div class="indicator"></div>
        </div>

        <div class="flow">
            <c:forEach items="${commanderList}" var="commName">
                <div class="item" href="#">
                    <img class="content"
                         src="http://static.eaw1805.com/img/commanders/s${scenarioId}/${nation.id}/${commName.position}.jpg"/>

                    <div class="caption">${commName.name}</div>
                </div>
            </c:forEach>
        </div>
        <div class="globalCaption"></div>
    </div>

    <h2 class="title">Battalion Types</h2>
    <!-- ===== FLOW ===== -->
    <div id="contentFlowBattalion" class="ContentFlow" style="height: 340px;">
        <!-- should be place before flow so that contained images will be loaded first -->
        <div class="loadIndicator">
            <div class="indicator"></div>
        </div>

        <div class="flow flowTall">
            <c:forEach items="${armyTypeList}" var="armyType">
                <div class="item" href="#">
                    <img class="content"
                         src="http://static.eaw1805.com/images/armies/${nation.id}/${armyType.intId}.jpg"/>

                    <div class="caption">${armyType.name}</div>
                    <table>
                        <tr>
                            <td colspan=2>
                                <c:choose>
                                    <c:when test="${armyType.elite == true}"><b>Crack
                                        Elite</b></c:when>
                                    <c:when test="${armyType.crack == true}"><b>Crack</b></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test='${armyType.type.equals("In")}'>Infantry</c:when>
                                    <c:when test='${armyType.type.equals("Ca")}'>Cavalry</c:when>
                                    <c:when test='${armyType.type.equals("Ar")}'>Artilery</c:when>
                                    <c:when test='${armyType.type.equals("Kt")}'>Colonial Troops</c:when>
                                    <c:when test='${armyType.type.equals("Co")}'>Colonial Troops</c:when>
                                    <c:when test='${armyType.type.equals("MC")}'>Colonial Cavalry</c:when>
                                    <c:when test='${armyType.type.equals("CC")}'>Colonial Cavalry</c:when>
                                </c:choose>
                            </td>
                        </tr>
                        <Tr>
                            <Td><b>Cost</b></td>
                            <td>${armyType.cost}&nbsp;/&nbsp;${armyType.indPt}</td>
                        </tr>
                        <Tr>
                            <Td><b>Str</b></td>
                            <td>${armyType.longCombat}&nbsp;/&nbsp;${armyType.longRange}&nbsp;/&nbsp;${armyType.handCombat}</td>
                        </tr>
                        <Tr>
                            <Td><b>Exp</b></td>
                            <td>${armyType.maxExp}</td>
                        </tr>
                        <Tr>
                            <Td><b>MP</b></td>
                            <td>${armyType.mps}&nbsp;/&nbsp;${armyType.sps}</td>
                        </tr>
                        <Tr>
                            <Td><b>Form</b></td>
                            <td><c:if test='${armyType.formationCo == true}'>C&nbsp;</c:if><c:if
                                    test='${armyType.formationLi == true}'>L&nbsp;</c:if><c:if
                                    test='${armyType.formationSk == true}'>S&nbsp;</c:if><c:if
                                    test='${armyType.formationSq == true}'>Q</c:if></td>
                        </tr>
                        <c:if test='${armyType.troopSpecsLr == true || armyType.troopSpecsLr == true || armyType.troopSpecsCu == true}'>
                            <Tr>
                                <Td><b>Special:</b></td>
                                <td><c:if test='${armyType.troopSpecsLr == true}'>Light&nbsp;</c:if><c:if
                                        test='${armyType.troopSpecsLc == true}'>Lancers&nbsp;</c:if><c:if
                                        test='${armyType.troopSpecsCu == true}'>Cuirassiers</c:if></td>
                            </tr>
                        </c:if>
                    </table>
                </div>
            </c:forEach>
        </div>
        <div class="globalCaption"></div>
    </div>


    <h2 class="title">Ship Types</h2>
    <!-- ===== FLOW ===== -->
    <div id="contentFlowShip" class="ContentFlow" style="height: 340px;">
        <!-- should be place before flow so that contained images will be loaded first -->
        <div class="loadIndicator">
            <div class="indicator"></div>
        </div>

        <div class="flow">
            <c:forEach items="${shipTypeList}" var="shipType">
                <div class="item" href="#">
                    <img class="content"
                         src="http://static.eaw1805.com/images/ships/${nation.id}/${shipType.intId}.png"/>

                    <div class="caption">${shipType.name}</div>
                    <table>
                        <Tr>
                            <Td><b>Class</b></td>
                            <td>&nbsp;${shipType.shipClass}</td>
                        </tr>
                        <Tr>
                            <Td><b>Cost</b></td>
                            <td>&nbsp;${shipType.cost}</td>
                        </tr>
                        <Tr>
                            <Td><img border="0" width="10"
                                     src="http://static.eaw1805.com/images/goods/good-3.png">
                                <img border="0" width="10"
                                     src="http://static.eaw1805.com/images/goods/good-6.png">
                                <img border="0" width="10"
                                     src="http://static.eaw1805.com/images/goods/good-10.png"></td>
                            <td>
                                &nbsp;${shipType.indPt}&nbsp;/&nbsp;${shipType.wood}&nbsp;/&nbsp;${shipType.fabrics}</td>
                        </tr>
                        <Tr>
                            <Td><b>Load</b></td>
                            <td>&nbsp;${shipType.loadCapacity}</td>
                        </tr>
                        <Tr>
                            <Td><b>Marines</b></td>
                            <td>&nbsp;${shipType.citizens}</td>
                        </tr>
                        <Tr>
                            <Td><b>MP</b></td>
                            <td>&nbsp;
                                ${shipType.movementFactor}</tr>
                    </table>
                </div>
            </c:forEach>
        </div>
        <div class="globalCaption"></div>
    </div>
</div>
