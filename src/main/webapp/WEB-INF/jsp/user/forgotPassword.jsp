<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="pagehead"
     style="background-image: url('http://static.eaw1805.com/images/site/HallOfFamePanelsBig.png');
            background-position: 0px 0px;
            background-repeat: no-repeat;
            float: left;
            margin-top: -80px;
            clear: both;
            background-size: 520px 597px;
            margin-top: -20px;
            margin-left: 188px;
            width: 520px;
            height: 621px;">

    <div style="padding-top: 50px; padding-left: 30px;">
        <h1 style="font-size: 38px;
                       width: 410px;
                       text-align: center;">Retrieve Password</h1>
        <div style="padding-top: 10px;
                    padding-bottom: 15px;
                    clear: both;
                    width: 410px;
                    font-size: 14px;
                    text-align: justify;">
            Please type in your e-mail and username and an automated e-mail message will be send to you containing your username.
        </div>

        <div class="formbody">
            <script type="text/javascript">
                var RecaptchaOptions = {
                    theme:'white',
                    tabindex:2
                };
            </script>
            <form:form commandName="user" method="POST" action='forgot_password'>

            <label for="username"
                   style="font-size: 16px;
                padding-top: 5px;
                color: #444446;">Account Name:</label>

            <div>
                <form:input path="username" id="username"
                            class="customInput"
                            tabindex="1"
                            style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        width: 250px;
                        height: 20px;"/>
                <spring:bind path="username">
                    <c:if test="${status.error}">
                        <img style="float: left; margin-left: 4px; margin-bottom: -5px;"
                             src="http://static.eaw1805.com/images/site/error.jpeg"
                             width="20" height="20" class="error_tooltip" title="${status.errorMessage}"/>
                    </c:if>
                </spring:bind>
            </div>

            <label for="email"
                   style="font-size: 16px;
                   clear:both;
                            padding-top: 5px;
                            color: #444446;">Email Address:</label>

            <div>
                <form:input path="email" id="email"
                            class="customInput"
                            tabindex="1"
                            style="font-size: 16px;
                                        clear:both;
                                        float: left;
                                        margin-top: -2px;
                                        padding-left: 2px;
                                        width: 380px;
                                        height: 20px;"/>
                <spring:bind path="email">
                    <c:if test="${status.error}">
                        <img style="float: left; margin-left: 4px; margin-bottom: -5px;"
                             src="http://static.eaw1805.com/images/site/error.jpeg"
                             width="20" height="20" class="error_tooltip" title="${status.errorMessage}"/>
                    </c:if>
                </spring:bind>
            </div>

            <p class="manual" style="clear:both; padding-left: 2px; padding-top: 10px;">
                We need to verify that you are human and not a spam bot.
                <spring:bind path="twitterSecret">
                <c:if test="${status.error}">
                <img src="http://static.eaw1805.com/images/site/error.jpeg"
                     style="margin-bottom: -2px;"
                     width="15" height="15" class="error_tooltip" title="${status.errorMessage}"/>
                </c:if>
                </spring:bind>
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

            <label class='submit_btn'
                   style="padding-top: 40px;">
                <input name="submit"
                       type="image"
                       src="http://static.eaw1805.com/images/site/signup/RetrievePasswordoff.png"
                       onmouseover="this.src='http://static.eaw1805.com/images/site/signup/RetrievePasswordhov.png'"
                       onmouseout="this.src='http://static.eaw1805.com/images/site/signup/RetrievePasswordoff.png'"
                       style="font-size: 22px;
                                  padding-left: 30px;
                                  width: 344px;
                                  height: 60px;"
                       tabindex="3"
                       type="submit"
                       value="Retrieve Password"/>
            </label>
        </div>

        </form:form>

    </div>
</div>
