<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:useBean id="user" scope="request" type="com.eaw1805.data.model.User"/>
<jsp:useBean id="messageToTime" scope="request" type="java.util.HashMap<java.lang.Integer,java.lang.String>"/>
<script type="text/javascript">
    function fill_form(subject, recipient) {
        document.getElementById("subject").value = subject;
        document.getElementById("recipient").value = recipient;
    }

    //init autocommplete functionality.
    $(function () {
        var allUsers = new Array();
        <c:forEach items="${allUsers}" var="curUser" varStatus="status">
        allUsers[${status.index}] = '${curUser.username}';
        </c:forEach>
        a2 = $('#recipient').autocomplete({

            delimiter:/(,|;)\s*/,
            lookup:allUsers
        });
    });
</script>
<article>
    <section class="doubleSize" id="doubleSize">
        <h1>Compose Message</h1>
        <form:form commandName="message" method="POST" action='inbox'>
            <div style="clear: both;">
                <label class="inputCompose" for="recipient">To:</label>
                <spring:bind path="receiver">
                    <c:if test="${status.error}">
                        <img style="float: left;margin-bottom: -5px;margin-left: -27px;"
                             src="http://static.eaw1805.com/images/site/error.jpeg"
                             width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
                    </c:if>
                </spring:bind>
                <input type="text" class="sectionText" name="recipient" id="recipient"/>
            </div>

            <div style="clear: both;">
                <label class="inputCompose" for="subject">Subject:</label>
                <spring:bind path="subject">
                    <c:if test="${status.error}">
                        <img style="float: left;margin-bottom: -5px;margin-left: -27px;"
                             src="http://static.eaw1805.com/images/site/error.jpeg"
                             width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
                    </c:if>
                </spring:bind>
                <form:input class="sectionText" path="subject" name="subject" id="subject"/>
            </div>

            <label class="send-message-btn" style="clear: both; margin-top: -60px;">
                <input id="submitBtn" class="send-message-input" type="submit" value=""/>
            </label>

            <spring:bind path="bodyMessage">
                <c:if test="${status.error}">
                    <img style="float: left;margin-bottom: -22px;"
                         src="http://static.eaw1805.com/images/site/error.jpeg"
                         width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
                </c:if>
            </spring:bind>
            <form:textarea path="bodyMessage" class="sectionTextArea" style="margin-top: 10px;" rows="15"
                           name="bodyMessage" id="bodyMessage"/>

        </form:form>
    </section>
    <c:forEach items="${messagesList.pageList}" var="conversation">
        <section>
            <div id="content" style="overflow: hidden;">
                <a class="sectionLink" style="float: left;"
                        <c:choose>
                            <c:when test="${conversation[0].sender.userId!=user.userId}">
                                <c:set var="thisUserName" value="${conversation[0].sender.username}"/>
                                href='<c:url value="/user/${thisUserName}"/>'>
                                <img src="https://secure.gravatar.com/avatar/${conversation[0].sender.emailEncoded}?s=48&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                     alt="" height="48" width="48" align="left">
                                <c:set var="thisThreadSubj" value="${conversation[0].subject}"/>
                            </c:when>
                            <c:when test="${conversation[0].sender.userId==user.userId}">
                                <c:set var="thisUserName" value="${conversation[0].receiver.username}"/>
                                href='<c:url value="/user/${thisUserName}"/>'>
                                <img src="https://secure.gravatar.com/avatar/${conversation[0].receiver.emailEncoded}?s=48&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                     alt="" height="48" width="48" align="left">
                                <c:set var="thisThreadSubj" value="${conversation[0].subject}"/>
                            </c:when>
                        </c:choose>
                </a>
                    <c:set var="msgPresentedCnt" value="0"/>
                <h1 style="margin: 3px!important;width: 160px; float: right;">
                    <a class="sectionLink"
                       href='<c:url value="/inbox/private/${conversation[0].messageId}/view#reply"/>'>${conversation[0].subject}</a>
                </h1>
                <c:forEach items="${conversation}" var="thisMsg">
                <c:choose>
                <c:when test="${thisMsg.sender.userId!=user.userId}">
                <div id="subtext-left" style="overflow: hidden;">
                    </c:when>
                    <c:when test="${thisMsg.sender.userId==user.userId}">
                    <div id="subtext-right" style="overflow: hidden;">
                        </c:when>
                        </c:choose>
                        <div style="font-size: 12px;color: #444446!important; clear: both; overflow: hidden;">
                                ${thisMsg.sender.username}
                            said ${messageToTime[thisMsg.messageId]}
                        </div>
                            ${thisMsg.bodyMessage}
                    </div>

                    </c:forEach>
                </div>


                <div id="readMore"><a
                        href='<c:url value="/inbox/private/${conversation[0].messageId}/view"/>'>Read
                    More</a></div>

                <div id="quickReply" onclick="fill_form('${thisThreadSubj}', '${thisUserName}');"><a
                        href="#doubleSize">Reply</a></div>
        </section>
    </c:forEach>

    <div class="paging" style="clear:both;margin-top: 5px;margin-left: 50%; height: 15px;">
        <c:if test="${!messagesList.firstPage}">
            <a class="previous" href="inbox?page=previous" title="Previous Page"
               style="width: 40px; margin-left: -50px; padding-left: -50px;">
                <div id="previous" style="width: 40px;margin-left: -45px;float: left;"></div>
            </a>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <c:if test="${!messagesList.lastPage}">
            <a class="next" href="inbox?page=next" title="Next Page"
               style="width: 40px;margin-left: -50px; padding-left: -50px;">
                <div id="next" style="width: 40px;float: left;"></div>
            </a>
        </c:if>
    </div>
</article>
