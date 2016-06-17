<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="staticItems" scope="request" type="java.util.Map<java.lang.String, java.lang.Integer[]>"/>
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
</style>
<div style=" position: relative;margin: -10px 0px 0px -35px;width: 1000px;min-height: 343px;padding: 0px 40px;
background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
    <h1 style="font-size: 42px;">Player's Handbook</h1>
    <c:set var="cells" value="0"/>
    <table style="margin-left: 10px;">
        <tr valign="top">
            <td width="510" valign="top">
                <c:forEach items="${tocItems}" var="entry">
                <div style="float: left; margin-left: -15px; margin-top: 5px; width: 50px; height: 60px;
                        background-image: url('${tocImages[cells]}');
                        background-repeat: no-repeat;
                        background-size: 42px;">
                </div>
                <h3>
                    <a style="padding-top: 6px;margin-bottom: 2px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;"
                       href='<c:url value="/help/${entry}"/>'>${mainTitles[entry]}
                    </a>
                </h3>

                <ul style="float: none; width: 400px; white-space: normal; text-align: justify; font-size: 13px; margin-bottom: 10px; ">
                        ${chapters[cells]}
                </ul>

                <div style="float: none; width: 420px; margin-left: 15px; margin-bottom: 20px; white-space: normal;">
                    <c:set var="index" value="0"/>
                    <c:set var="length" value="${fn:length(staticItems.get(entry))}"/>
                    <c:forEach items="${staticItems.get(entry)}" var="id">
                        <a style="font-size:12px; color: rgb(68, 68, 70);
                                text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                                font-weight: normal; font-weight: normal;letter-spacing: -1px; line-height: 9px;"
                           href='<c:url value="/help/${entry}?id=${id}"/>'>${titles[id]}
                        </a>
                        <c:if test="${index < length - 1}">&nbsp;/&nbsp;</c:if>
                        <c:set var="index" value="${index+1}"/>
                    </c:forEach>
                </div>
                <c:if test="${cells == 5}">
            </td>
            <td width="450" valign="top">
                </c:if>
                <c:set var="cells" value="${cells+1}"/>
                </c:forEach>

                <img
                        src='http://static.eaw1805.com/images/site/MainPageFrenchSoldierGrass.png'
                        alt="French Soldier"
                        title="French Soldier"
                        border=0
                        width="450"
                        style="float: right; border: 0 none; -moz-transform: scaleX(-1); -o-transform: scaleX(-1); -webkit-transform: scaleX(-1); transform: scaleX(-1); filter: FlipH; -ms-filter: "
                        FlipH"; ">
            </td>
        </tr>
    </table>
</div>
