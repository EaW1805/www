<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:useBean id="user" scope="request" type="com.eaw1805.data.model.User"/>
<jsp:useBean id="userAchievements" scope="request" type="java.util.HashMap<java.lang.Integer,java.lang.Integer>"/>
<style type="text/css">
    .pagehead {
        background: none;
    }

    #content {
        padding-left: 10px;
        padding-right: 60px;
        padding-bottom: 0px;
        overflow: visible;
    }

    #footer .parchment-footer {
        background: none;
    }

    div.player {
        position: relative;
        margin: -10px 0px 0px -25px;
        background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;
        padding-left: 28pt;
    }

    article {
        width: 965px;
        margin-left: 35px;
    }

</style>

<div id="main-article"
     style="position: relative;margin: 0px 0px 0px -35px;width: 1033px;background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px -200px transparent;clear: both; overflow: hidden;">
    <article>
        <h1>Honour : ${vps}</h1>

        <div id="recentAchievements-body" style="z-index: 1; position: relative; margin: -10px 0 0 -35px;width: 1033px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-body.png')
    repeat-y scroll 0px 0px transparent;clear: both; padding-left: 35px;">
            <article>
                <news style="width: 980px; border-width: 0px; margin-left: -35px; margin-top: 25px; margin-bottom: 0px;">
                    <c:set var="count" value="0"/>
                    <table width="100%" border=0 cellspacing="0" cellpadding="0">
                        <tr style="line-height: 9pt;">
                            <c:forEach items="${recentAchievements}" var="thisAchievement">
                            <td
                                    <c:choose>
                                        <c:when test="${count % 3 > 0}">
                                            width="270px" style="border-left-style: dashed; border-left-width: 1px; border-left-color: black; padding-left: 10px;"
                                        </c:when>
                                        <c:otherwise>
                                            width="280px"
                                        </c:otherwise>
                                    </c:choose>
                                    >
                                <div class="flagEntry" style="padding-top: 6px; float:left; border: none; width: 60px;">
                                    <h1>${thisAchievement.achievementPoints} VP</h1>
                                </div>
                                    <%--<div class="flagEntry" style=" float:left; border: none; margin-left: -1px;">--%>
                                    <%--<img src='http://static.eaw1805.com/images/achievements/ach-${thisAchievement.category}-${thisAchievement.level}.png'--%>
                                    <%--alt="${thisAchievement.description}"--%>
                                    <%--title="${thisAchievement.description}"--%>
                                    <%--style="border: 0; padding: 0; margin: 0;"--%>
                                    <%--height=48>--%>
                                    <%--</div>--%>
                                <div class="messageEntry"
                                        <c:choose>
                                            <c:when test="${count % 2 == 1}">
                                                style="width: 200px; float:left; margin-left: 15px; margin-top: 0px; left: 0px; top: 0px; margin-bottom: 15px;"
                                            </c:when>
                                            <c:otherwise>
                                                style="width: 220px; float:left; margin-left: 15px; margin-top: 0px; left: 0px; top: 0px; margin-bottom: 15px;"
                                            </c:otherwise>
                                        </c:choose>
                                        >

                                    <div style="text-align: left !important;">${thisAchievement.description}</div>
                                </div>
                            </td>
                            <c:if test="${count % 3 == 2}">
                        </tr>
                        <tr>
                            </c:if>
                            <c:set var="count" value="${count+1}"/>
                            </c:forEach>
                        </tr>
                    </table>
                </news>
            </article>
        </div>

    </article>
</div>
<div id="main-article-bottom" style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
</div>


<div style="width: 338px;
    background: url('http://static.eaw1805.com/images/site/Empire_parchment.png') repeat-y scroll 0px 0px transparent;
    background-size: 338px 376px;
    float: left;
    min-height: 370px;
    margin-left: -34px;
    line-height: 1.3 !important;
    font-size: 95%;
    font-family: Georgia,'Times New Roman',Times,serif;
    text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);
    position: relative;
     clear: both;">
    <div style="margin:10px">
        <h1>Empire Statistics</h1>
        <c:forEach var="key" items="${empireKeys}">
            <div style="clear:both;float: left;margin-left: 5px;">
                <div style="float: left;width: 190px;text-align: left;margin-right: 5px; line-height: 1.4!important;">
                    <spring:message code="${key}"/>
                </div>
                <div style="float: left;width: 80px; line-height: 1.4!important;text-align: right;">
                    <c:choose>
                        <c:when test="${profileStats[key]!= undefinedInt}">
                            <fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value='${profileStats[key]}'/>
                        </c:when>
                        <c:when test="${key == 'achievements'}">
                        </c:when>
                        <c:otherwise>
                            -
                        </c:otherwise>
                    </c:choose>
                </div>
                <div style="float: left;width: 20px; line-height: 1.4!important; text-align: right; margin-left: 5px;">
                    <c:set var="position">${key}Pos</c:set>
                        ${profileStats[position]}
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<div style=" width: 338px;
    background: url('http://static.eaw1805.com/images/site/Politics_parchment.png') repeat-y scroll 0px 0px transparent;
    background-size: 338px 290px;
    float: left;
    min-height: 290px;
    margin-left: 10px;
    line-height: 1.3 !important;
    font-size: 95%;
    font-family: Georgia,'Times New Roman',Times,serif;
    text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);
     position: relative;">
    <div style="margin:10px">
        <h1>Politics Statistics</h1>
        <c:forEach var="key" items="${politicsKeys}">
            <div style="clear:both;float: left;margin-left: 5px;">
                <div style="float: left;width: 190px;text-align: left;margin-right: 5px; line-height: 1.4!important;">
                    <spring:message code="${key}"/>
                </div>
                <div style="float: left;width: 70px; line-height: 1.4!important;text-align: right;">
                    <c:choose>
                        <c:when test="${profileStats[key]!= undefinedInt}">
                            <fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value='${profileStats[key]}'/>
                        </c:when>
                        <c:otherwise>
                            -
                        </c:otherwise>
                    </c:choose>
                </div>
                <div style="float: left;width: 15px; line-height: 1.4!important; text-align: right; margin-left: 5px;">
                    <c:set var="position">${key}Pos</c:set>
                        ${profileStats[position]}
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<div style="width: 338px;
        background: url('http://static.eaw1805.com/images/site/Warfare_parchment.png') repeat-y scroll 0px 0px transparent;
        background-size: 338px 465px;
        float: right;
        min-height: 465px;
        margin-right: -34px;
        margin-bottom: 10px;
        line-height: 1.3 !important;
        font-size: 95%;
        font-family: Georgia,'Times New Roman',Times,serif;
        text-shadow: 1px 1px 0.1px rgba(0, 0, 0, 0.2);
        position: relative;">
    <div style="margin:10px">
        <h1>Warfare Statistics</h1>
        <c:forEach var="key" items="${warfareKeys}">
            <div style="clear:both;float: left;margin-left: 5px;">
                <div style="float: left;width: 200px;text-align: left;margin-right: 5px; line-height: 1.4!important;">
                    <spring:message code="${key}"/>
                </div>
                <div style="float: left;width: 70px; line-height: 1.4!important;text-align: right;">
                    <c:choose>
                        <c:when test="${profileStats[key]!= undefinedInt}">
                            <fmt:formatNumber
                                    type="number"
                                    maxFractionDigits="0"
                                    groupingUsed="true"
                                    value='${profileStats[key]}'/>
                        </c:when>
                        <c:otherwise>
                            -
                        </c:otherwise>
                    </c:choose>
                </div>
                <div style="float: left;width: 15px; line-height: 1.4!important; text-align: right; margin-left: 5px;">
                    <c:set var="position">${key}Pos</c:set>
                        ${profileStats[position]}
                </div>
            </div>
            <c:if test="${key == 'battles.tactical.lost'
                                || key == 'battles.field.lost'
                                || key == 'battles.naval.lost' }">
                <div style="clear:both;float: left;margin: 5px;width: 295px; border-bottom: dashed; border-width: 1px;"></div>
            </c:if>
        </c:forEach>
    </div>
</div>

<div style="z-index: 2;position: relative; margin: 0 -40px 0 -40px;width: 1035px;height: 48px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;"></div>
<div id="third-article"
     style="position: relative;margin: 0px 0px 0px -35px;width: 1033px;background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px -200px transparent;clear: both; overflow: hidden;">
    <article>
        <c:forEach items="${shortAchievementID}" varStatus="counter" var="category">
            <section class="achievements" style="min-height:160px; height:160px;">
                <h2 style="float: left;">${shortAchievementName[counter.index]}</h2>

                <c:if test="${userAchievements[category] > 0}">
                    <h2 style="float: right; margin-right: 5px; margin-top: -2px;">${shortAchievementTitle[counter.index][userAchievements[category]]}</h2>
                </c:if>
                <c:forEach begin="1" end="${shortAchievementLMax[counter.index]}" varStatus="loop">
                    <div class="flagEntry" style=" float:left; border: 0pt; padding: 0pt; padding-top: 6px;
                            <c:if test="${userAchievements[category] < loop.index}">opacity: 0.3; filter: alpha(opacity=30);</c:if>
                            <c:if test="${loop.index==1}">clear:both;</c:if>
                            ">
                        <img src='http://static.eaw1805.com/images/achievements/ach-${category}-${loop.index}.png'
                             alt="${shortAchievementStr[counter.index][loop.index]}"
                             title="${shortAchievementStr[counter.index][loop.index]}"
                             style="border: 0; padding: 0; margin: 0;"
                             height=116>
                    </div>
                </c:forEach>
            </section>
        </c:forEach>
        <c:forEach items="${longAchievementID}" varStatus="counter" var="category">
            <section class="achievements" style="width: 950px; min-height:225px; height:225px;">
                <h2>${longAchievementName[counter.index]}</h2>
                <c:forEach begin="1" end="${longAchievementLMAX[counter.index]}" varStatus="loop">
                    <div class="flagEntry"
                         style=" float:left; border: 0pt; padding: 0pt; padding-top: 0px; margin-top: -10px;
                                 <c:if test="${counter.index == 0 && nationAchievements[loop.index] < 1}">opacity: 0.3; filter: alpha(opacity=30);</c:if>
                                 <c:if test="${counter.index == 1 && dominationAchievements[loop.index] < 1}">opacity: 0.3; filter: alpha(opacity=30);</c:if>
                                 ">
                        <img src='http://static.eaw1805.com/images/achievements/ach-${category}-${loop.index}.png'
                             alt="${longAchievementStr[counter.index][loop.index]}"
                             title="${longAchievementStr[counter.index][loop.index]}"
                             style="border: 0; padding: 0; margin: 0;"
                             height=105>
                    </div>
                </c:forEach>
            </section>
        </c:forEach>

        <c:if test="${specialAchievements[specialAchievementID[1]] + specialAchievements[specialAchievementID[2]] + specialAchievements[specialAchievementID[3]] > 0}">
            <section class="achievements" style="width: 950px; min-height:150px; height:150px;">
                <h2>Special Awards</h2>
                <c:forEach var="index" begin="1" end="4" step="1">
                    <c:if test="${specialAchievements[specialAchievementID[index]] == 1}">
                        <div class="flagEntry"
                             style=" float:left; border: 0pt; padding: 0pt; padding-top: 0px; margin-top: -10px;">
                            <img src='http://direct.eaw1805.com/images/achievements/ach-1001-${index}.png'
                                 alt="${specialAchievementStr[index]}"
                                 title="${specialAchievementStr[index]}"
                                 style="border: 0; padding: 0; margin: 0;"
                                 height=128>
                        </div>
                    </c:if>
                </c:forEach>
            </section>
        </c:if>
    </article>

</div>
<div id="third-article-bottom" style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78">
</div>
