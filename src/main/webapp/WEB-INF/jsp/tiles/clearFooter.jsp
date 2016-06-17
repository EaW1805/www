<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<security:authorize ifNotGranted="ROLE_ANONYMOUS">
    <script type="text/javascript">
        $(document).ready(function () {
            $(function () {

                <security:authorize ifNotGranted="ROLE_ANONYMOUS">
                <c:if test="${user.creditFree + user.creditTransferred + user.creditBought < 0
           && !fn:contains(requestScope['javax.servlet.forward.request_uri'],'settings')
               && user.userType!=3}">
                $.blockUI({ message:$('#error_message_nb'), css:{ width:'390px' } });
                </c:if>

                </security:authorize>
            });
        });
    </script>
</security:authorize>

<div id="footer">
    <div class="footer_upper">
        <div class="grass"></div>
        <div class="site">
            <div class="eaw-logo" style="margin-top: -167px"
                 onclick="window.location='http://www.eaw1805.com/home';"></div>
            <div class="footer_nav">
                <ul class="footer_nav">
                    <li><a href="<c:url value="/features"/>"
                           title="<spring:message code="baseLayout.status.tooltip"/>"><spring:message
                            code="baseLayout.status"/></a></li>
                    <li><a href="<c:url value="/news"/>"
                           title="<spring:message code="anonymousLayout.news.tooltip"/>"><spring:message
                            code="anonymousLayout.news"/></a></li>
                    <li><a href="<c:url value="/contact"/>"
                           title="<spring:message code="anonymousLayout.contact.tooltip"/>"><spring:message
                            code="anonymousLayout.contact"/></a></li>
                    <li><a href="<c:url value="/about"/>"
                           title="<spring:message code="baseLayout.about.tooltip"/>"><spring:message
                            code="baseLayout.about"/></a></li>
                </ul>
            </div>
            <div class="footer_nav2">
                <ul class="footer_nav">
                    <li><a href="<c:url value="/help/introduction"/>"
                           title="<spring:message code="handbook.introduction.tooltip"/>"><spring:message
                            code="handbook.introduction"/></a></li>
                    <li><a href="<c:url value="/handbook"/>"
                           title="<spring:message code="footer.handbook"/>"><spring:message
                            code="footer.handbook"/></a></li>
                    <li style="height: 20px; ">&nbsp;</li>
                    <li><a href="<c:url value="/joingame"/>"
                           title="<spring:message code="baseLayout.joingame.tooltip"/>"><spring:message
                            code="baseLayout.joingame"/></a></li>
                </ul>
            </div>
            <div class="footer_nav3">
                <ul class="footer_nav">
                    <li><a href="http://www.facebook.com/empires1805" target="_blank" style="text-decoration:none"><img
                            height=20 style="vertical-align:middle;"
                            src="http://www.oplongames.com/sites/default/files/social/icon-facebook.png"> Like us</a>
                    </li>
                    <li><a href="http://twitter.com/eaw1805" target="_blank" style="text-decoration:none"><img height=20
                                                                                                               style="vertical-align:middle;"
                                                                                                               src="http://www.oplongames.com/sites/default/files/social/icon-twitter.png">
                        Follow us</a></li>
                    <li><a href="http://www.linkedin.com/company/2624926?trk=tyah" target="_blank"
                           style="text-decoration:none"><img height=20 style="vertical-align:middle;"
                                                             src="http://www.oplongames.com/sites/default/files/social/icon-linkedin.png">
                        Connect with us</a></li>
                    </li>
                </ul>
            </div>
            <div class="oplon-logo" onclick="window.open('http://www.oplongames.com','_blank');"></div>
            <div class="footer_low">
                <p>&copy; 2011-2014 Copyright <a
                        href="http://www.oplongames.com" target="_blank"><img
                        src="http://www.oplongames.com/sites/all/themes/theme621/images/footer_logo.png"
                        height=24
                        style="margin-bottom: -8px;"
                        alt="Oplon Games">&nbsp;Oplon Games</a>
                    &nbsp;-&nbsp;
                    All rights reserved - Funded by <a
                            href="http://www.taneo.gr" target="_blank"><img
                            src="http://static.eaw1805.com/images/site/taneo.png"
                            style="margin-bottom: -4px;"
                            alt="TANEO Venture Capital"></a>
                    &nbsp;-&nbsp;
                    <a href='<c:url value="/terms"/>'>Terms of Service</a>
                </p>
            </div>
        </div>
    </div>
</div>

<security:authorize ifNotGranted="ROLE_ANONYMOUS">

    <div id="error_message_nb" style="display:none; cursor: default">
        <h3 style="margin:auto; width:75%; margin-top: 22px;">Your account has been deactivated</h3>

        <div style="width: 100%; height: 113px;">
            <h3 style="margin-top: 15px;margin-left: 107px;float: left;width: 193px;">

                <div style="color: rgb(68, 68, 70);font-family: Georgia,helvetica,arial,freesans,clean,sans-serif;
                    text-shadow: 1px 1px 0.1px rgb(123, 113, 75);font-weight: normal;letter-spacing: -1px;
                    font-size: 16px;width: 100%;text-align: left;clear: both;">
                    <p>Our records indicate that you have a negative balance.</p>

                    Click <a style="font-size: 14px;" href='<c:url value="/settings"/>'>here</a> to buy more credits.
                </div>
            </h3>
        </div>

        <div style="clear: both;">
            <img style="float:left;margin-left: 20px; margin-top: -10px;"
                 src='http://static.eaw1805.com/images/buttons/taxation/MUINormalTaxSlc.png'
                 alt="Credits"
                 class="toolTip"
                 title="Account Balance"
                 border=0 height=32>

            <div style="float: left;font-size: 20px; margin: 2px;margin-top: -5px; margin-left: 5px;">${user.creditFree+user.creditTransferred+user.creditBought}
            </div>
        </div>
    </div>
</security:authorize>