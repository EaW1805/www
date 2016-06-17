<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<jsp:useBean id="engine" scope="request" type="com.eaw1805.data.model.EngineProcess"/>
<jsp:useBean id="user" scope="request" type="com.eaw1805.data.model.User"/>
<h1>Contact &amp; Support</h1>

<p class="manual">For questions about the rules and the user interface, we suggest that you use our
    <a href="http://forum.eaw1805.com">forums</a> where all players post their comments and our replies.

<p class="manual">If you want to report a problem, it will also help us a great deal if you could use the
    <strong>Build Number</strong> reported in this page when you report a bug.</p>

<p class="manual">To contact us directly or for for anything
    else, please send use the following form and we will get back to you.</p>

<article>

    <c:choose>
        <c:when test="${user.userId == -1}">
            <c:set var="height" value="280"/>
            <c:set var="heightArticle" value="580"/>
        </c:when>
        <c:otherwise>
            <c:set var="height" value="280"/>
            <c:set var="heightArticle" value="368"/>
        </c:otherwise>
    </c:choose>

    <section class="doublesize"
             style="width: 620px; height: ${heightArticle}px;">
        <form:form name="contact" commandName="message" method="POST" action='contact'>
        <h2 style="padding-top: 5px; padding-left:5px; padding-bottom:10px;">Contact Empires at War 1805 team</h2>

        <c:if test="${user.userId == -1}">
        <label for="fullname"
               style="padding-left: 2px; width: 100px;">Your Name</label>

        <div>
            <form:input path="sender.fullname" id="fullname"
                        class="customInput"
                        style="width: 300px;"/>
            <spring:bind path="sender.fullname">
                <c:if test="${status.error}">
                    <img style="float: right; margin-right: 180px; margin-bottom: -5px;"
                         src="http://static.eaw1805.com/images/site/error.jpeg"
                         width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
                </c:if>
            </spring:bind>
        </div>

        <label for="email"
               style="padding-left: 2px; width: 100px;">Email</label>

        <div>
            <form:input path="sender.email" id="email"
                        class="customInput"
                        style="width: 300px;"/>
            <spring:bind path="sender.email">
                <c:if test="${status.error}">
                    <img style="float: right; margin-right: 180px; margin-bottom: -5px;"
                         src="http://static.eaw1805.com/images/site/error.jpeg"
                         width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
                </c:if>
            </spring:bind>
        </div>
        </c:if>

        <label for="subject"
               style="padding-left: 2px; width: 100px;">Subject</label>

        <div>
            <form:input path="subject" id="subject"
                        class="customInput"
                        style="width: 300px;"/>
            <spring:bind path="subject">
                <c:if test="${status.error}">
                    <img style="float: right; margin-right: 180px; margin-bottom: -5px;"
                         src="http://static.eaw1805.com/images/site/error.jpeg"
                         width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
                </c:if>
            </spring:bind>
        </div>

        <label class="send-message-btn" style="clear: both; margin-top: -60px;">
            <input id="submitBtn" class="send-message-input" type="submit" value=""/>
        </label>

        <div>
            <spring:bind path="bodyMessage">
            <c:if test="${status.error}">
                <img style="float: right; margin-top: -110px; margin-right: 5px; margin-bottom: -8px;"
                     src="http://static.eaw1805.com/images/site/error.jpeg"
                     width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
            </c:if>

            <form:textarea path="bodyMessage"
                           class="sectionTextArea"
                           style="margin-top: 10px; width: 608px; height: ${height}px !important;"
                           rows="15"/>
        </div>

        <c:if test="${user.userId == -1}">
        <p class="manual" style="padding-left: 5px; padding-top: 10px;">Since you are not logged in, we need to verify
            that you are human and not a spam bot.
            <script type="text/javascript"
                    src="http://www.google.com/recaptcha/api/challenge?k=6LfgG9gSAAAAADjchILDor7h7DWN_Eaavg1Ei6ii">
            </script>
        <noscript>
            <iframe src="http://www.google.com/recaptcha/api/noscript?k=6LfgG9gSAAAAADjchILDor7h7DWN_Eaavg1Ei6ii"
                    style="margin-left: 15px;"
                    height="300"
                    width="500"
                    frameborder="0"></iframe>
            <br>
            <textarea
                    style="padding-left: 5px;"
                    name="recaptcha_challenge_field" rows="3" cols="40">
            </textarea>
            <input type="hidden" name="recaptcha_response_field"
                   value="manual_challenge">
        </noscript>
        </p>
        </c:if>
        </spring:bind>

        </form:form>
</article>

<h1 style="clear: both; padding-top: 15px;">Engine Status</h1>
<b>Build number:</b> <i><spring:message code="eaw.version"/></i>
<BR><b>Last heartbeat:</b> <i><c:out value="${engine.dateStart}"/></i>
