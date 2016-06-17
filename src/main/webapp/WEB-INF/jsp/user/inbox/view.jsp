<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:useBean id="user" scope="request" type="com.eaw1805.data.model.User"/>
<jsp:useBean id="messageList" scope="request" type="java.util.List<com.eaw1805.data.model.Message>"/>

<article>
    <section class="viewSection">
        <form style="margin-top:5px;float: right;"
              action="<c:url value="/inbox/delete/${messageList[0].messageId}" />" method="get">
            <button class="delete" title="Delete this thread"></button>
        </form>

        <div id="content">
            <c:choose>
            <c:when test="${messageList[0].sender.userId!=user.userId}">
            <img src="https://secure.gravatar.com/avatar/${messageList[0].sender.emailEncoded}?s=48&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                 alt="" height="48" width="48">
                <c:set var="thisUserName" value="${messageList[0].sender.username}"/>
                <c:set var="thisThreadSubj" value="${messageList[0].subject}"/>
            </c:when>
            <c:when test="${messageList[0].sender.userId==user.userId}">
            <img src="https://secure.gravatar.com/avatar/${messageList[0].receiver.emailEncoded}?s=48&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                 alt="" height="48" width="48">
                <c:set var="thisUserName" value="${messageList[0].receiver.username}"/>
                <c:set var="thisThreadSubj" value="${messageList[0].subject}"/>
            </c:when>
            </c:choose>


            <h1>${messageList[0].subject}</h1>

            <c:forEach items="${messageList}" var="thisMessage">

            <c:choose>
            <c:when test="${thisMessage.sender.userId!=user.userId}">
            <div id="subtext-left">
                </c:when>
                <c:when test="${thisMessage.sender.userId==user.userId}">
                <div id="subtext-right">
                    </c:when>
                    </c:choose>

                    <div style="font-size: 12px;color: #444446!important; clear: both;">
                            ${thisMessage.sender.username} said at ${thisMessage.date}
                    </div>
                        ${thisMessage.bodyMessage}
                </div>
                </c:forEach>
            </div>

    </section>
    <section class="doubleSize-Reply" id="doubleSize">
        <h1>Reply</h1>
        <form:form commandName="message" method="POST" action='view'>
            <label class="reply-message-btn">
                <input class="send-message-input" type="submit" value=""/>
            </label>
            <input id="subject" name="subject" type="hidden" value="${thisThreadSubj}"/>
            <input id="recipient" name="recipient" type="hidden" value="${thisUserName}"/>

            <spring:bind path="bodyMessage">
                <c:if test="${status.error}">
                    <img style="float: left;margin-bottom: -22px;"
                         src="http://static.eaw1805.com/images/site/error.jpeg"
                         width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
                </c:if>
            </spring:bind>
            <form:textarea path="bodyMessage" class="sectionTextArea-reply" rows="15"/><br>
        </form:form>
    </section>


</article>
