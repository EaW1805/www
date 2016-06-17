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
<div style=" position: relative;margin: -10px 0px 0px -35px;width: 1000px;min-height: 514px;padding: 0px 40px;
background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
    <h1 style="font-size: 42px;">Quick Start</h1>

    <div style="float: left; margin-left: -55px; margin-top: -20px; width: 600px;"><img
            src='http://static.eaw1805.com/images/site/MainPageBritishSoldierGrassFlipped.png'
            alt="British Soldier"
            title="British Soldier"
            border=0
            width="620"
            style="float: left; border: 0 none;">
    </div>

    <div style="float: left; width: 430px; margin-right: 0px;">
        <h3 style="margin-bottom: 2px;">
            <a
                    style="font-weight: normal;
                        text-align: left;
                        color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 28px;"
                    href='<c:url value="/help/introduction?id=156"/>'>Getting Started
            </a>
        </h3>
        <ul style="float: right; width: 400px; margin-right: 15px; white-space: normal; text-align: justify; font-size: 13px; margin-bottom: 20px;">
            The gaming environment is rich with details about all the critical aspects your empire.
            Start by getting a strategic-level introduction of the main elements of the game.
            &nbsp;&nbsp;<a style="font-weight: normal; color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 13px;"
                           href='<c:url value="/help/introduction?id=156"/>'>Read more</a>
        </ul>

        <h3 style="margin-bottom: 8px; ">
            <a
                    style="font-weight: normal;
                        text-align: left;
                        color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 28px;"
                    href='<c:url value="/help/introduction?id=158"/>'>Command your Forces
            </a>
        </h3>
        <ul style="float: right; width: 400px; margin-right: 15px; white-space: normal; text-align: justify; font-size: 13px; margin-bottom: 20px; margin-top: 28px; ">
            In Empires at War 1805 you can control Armies & Corps to conquer, Ships to expand and colonize,
            Baggage trains to perform trade, and Spies to conduct espionage. Each element is crucial for your empire to
            survive and prevail the wars that are about to start.
            &nbsp;&nbsp;<a style="font-weight: normal; color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 13px;"
                           href='<c:url value="/help/introduction?id=158"/>'>Read more</a>
        </ul>

        <h3 style="margin-bottom: 8px; ">
            <a
                    style="font-weight: normal;
                        text-align: left;
                        color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 28px;"
                    href='<c:url value="/help/introduction?id=157"/>'>Control & Conquer
            </a>
        </h3>
        <ul style="float: right; width: 400px; margin-right: 15px; white-space: normal; text-align: justify; font-size: 13px; margin-bottom: 20px; margin-top: 28px; ">
            The basis of your empire are your people. You need them for a solid production, drafting of troops and
            financial
            support.
            &nbsp;&nbsp;<a style="font-weight: normal; color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 13px;"
                           href='<c:url value="/help/introduction?id=157"/>'>Read more</a>
        </ul>

        <h3 style="margin-bottom: 8px; ">
            <a
                    style="font-weight: normal;
                        text-align: left;
                        color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 28px;"
                    href='<c:url value="/help/introduction?id=155"/>'>Basic Game Mechanics
            </a>
        </h3>
        <ul style="float: right; width: 400px; margin-right: 15px; white-space: normal; text-align: justify; font-size: 13px; margin-bottom: 20px; margin-top: 28px; ">
            Get valuable insight on the basic elements of the Napoleonic era of Empires at War 1805. Understand how
            orders
            are issued to your forces to establish a strong sphere of influence to the
            four theaters of operation: Europe, The Caribbean, Africa and India.
            &nbsp;&nbsp;<a style="font-weight: normal; color: rgb(68, 68, 70);
                        font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                        text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        letter-spacing: -1px;
                        font-size: 12px;"
                           href='<c:url value="/help/introduction?id=155"/>'>Read more</a>
        </ul>
    </div>
</div>
<div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78"></div>

<div style="z-index: 2; clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="font-size: 42px; padding-left: 40px; padding-top: 20px;">Game Tutorials</h2>
</div>

<div style=" z-index: 1; position: relative;margin: -10px 0px 0px -35px;width: 1000px;min-height: 303px;padding: 30px 40px;
background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
    <table style="margin-left: 10px;">
        <tr valign="top">
            <td width="510" valign="top">
                <h3 style="padding-top: 6px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                    Tutorial 1: Forming Corps &amp; Armies</h3>
                <iframe width="400" height="225" src="http://www.youtube.com/embed/4toIINeKfxY" frameborder="0"
                        allowfullscreen></iframe>
            </td>

            <td width="510" valign="top">
                <h3 style="padding-top: 6px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                    Tutorial 2: Managing Fleets</h3>
                <iframe width="400" height="225" src="http://www.youtube.com/embed/3NcPqVQpHcg" frameborder="0"
                        allowfullscreen></iframe>
            </td>
        </tr>
        <tr valign="top">
            <td width="510" valign="top">
                <h3 style="padding-top: 16px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                    Tutorial 3: Exchanging &amp; Merging Battalions</h3>
                <iframe width="400" height="225" src="http://www.youtube.com/embed/__7-udFwhEM" frameborder="0"
                        allowfullscreen></iframe>
            </td>

            <td width="510" valign="top">
                <h3 style="padding-top: 16px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                    Tutorial 4: Movement</h3>
                <iframe width="400" height="225" src="http://www.youtube.com/embed/S62I6UWO0U0" frameborder="0"
                        allowfullscreen></iframe>
            </td>
        </tr>
        <tr valign="top">
            <td width="510" valign="top">
                <h3 style="padding-top: 16px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                    Tutorial 5: Conquest</h3>
                <iframe width="400" height="225" src="http://www.youtube.com/embed/NZ1pDkFCLS8" frameborder="0"
                        allowfullscreen></iframe>
            </td>

            <td width="510" valign="top">
                <h3 style="padding-top: 16px;margin-bottom: 6px;font-size: 14px;height: 24px; text-align: left;
                        color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;text-shadow: 1px 1px 0.1px rgb(123, 113, 75);
                        font-weight: normal;letter-spacing: -1px;font-size: 20px;">
                    Tutorial 6: Trading</h3>
                <iframe width="400" height="225" src="http://www.youtube.com/embed/Pmd8m8TRnBg" frameborder="0"
                        allowfullscreen></iframe>
            </td>
        </tr>
    </table>
</div>
<div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78"></div>

<div style="z-index: 2; clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;">
    <h2 style="font-size: 42px; padding-left: 40px; padding-top: 20px;">Player's Handbook</h2>
</div>

<div style=" z-index: 1; position: relative;margin: -10px 0px 0px -35px;width: 1000px;min-height: 343px;padding: 30px 40px;
background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">
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
                        style="float: right; border: 0 none; -moz-transform: scaleX(-1); -o-transform: scaleX(-1); -webkit-transform: scaleX(-1); transform: scaleX(-1); filter: FlipH; -ms-filter: FlipH;">
            </td>
        </tr>
    </table>
</div>
