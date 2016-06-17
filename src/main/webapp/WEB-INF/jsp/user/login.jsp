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
        <form name="f" action="<c:url value='/j_spring_security_check'/>" method="POST" accept-charset="UTF-8">
            <h1 style="font-size: 38px;
                       width: 410px;
                       text-align: center;">Log in to your Account</h1>

            <c:if test="${not empty param.login_error}">
                <div style="padding-top: 10px;
                            margin-bottom: -10px;
                            color:#FF0000;
                            font-weight: bold;
                            width: 410px;
                            text-align: center;
                            font-size: 14px;">
                    Your login attempt was not successful, try again.
                </div>
            </c:if>

            <div class="formbody"
                 style="padding-top: 20px;
                        padding-left: 0px;">
                <label for="login"
                       style="font-size: 22px;
                               color: #444446;">Username<br/>
                    <input type='text' name='j_username'
                           style="font-size: 24px;
                                  padding-left: 4px;
                                  width: 400px;
                                  height: 38px;"
                           tabindex="1"
                           value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/>
                </label>

                <label for="password"
                       style="font-size: 22px;
                              clear: both;
                              color: #444446;
                              padding-top: 15px;">
                    Password
                    <br/>
                    <input class="text" name='j_password'
                           style="font-size: 24px;
                                  padding-left: 4px;
                                  width: 400px;
                                  height: 38px;"
                           tabindex="2" type="password" value=""/>
                </label>

                <div style="clear: both;
                              padding-top: 25px;">
                    <label class='submit_btn'>
                        <input name="submit"
                               style="font-size: 22px;
                                  margin-left: -5px;
                                  width: 208px;
                                  height: 56px;"
                               tabindex="3"
                               type="image"
                               src="http://static.eaw1805.com/images/site/signup/ButLogInOff.png"
                               onmouseover="this.src='http://static.eaw1805.com/images/site/signup/ButLogInHover.png'"
                               onmouseout="this.src='http://static.eaw1805.com/images/site/signup/ButLogInOff.png'"
                               value="Log In"/>
                    </label>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<c:url value="/signup"/>">
                        <img src="http://static.eaw1805.com/images/site/signup/ButCreateAccountOff.png"
                             alt="Sign up to Empires at War 1805"
                             title="Create Account and Join Empires at War 1805"
                             style="font-size: 22px;
                                  padding-left: 0px;
                                  width: 208px;
                                  height: 56px;"
                             onmouseover="this.src='http://static.eaw1805.com/images/site/signup/ButCreateAccountHover.png'"
                             onmouseout="this.src='http://static.eaw1805.com/images/site/signup/ButCreateAccountOff.png'">
                    </a>
                </div>
            </div>
        </form>

        <div style="padding-top: 30px;
                    clear: both;
                    width: 410px;
                    font-size: 14px;
                    text-align: justify;">
            <strong>Do you already have an account?</strong> Get ready for a competitive web-based strategy game that
            will test both your skill and mind.
        </div>

        <div style="padding-top: 20px;
                    clear: both;
                    width: 410px;
                    font-size: 14px;
                    text-align: justify;">
            The game is web-based and is 100% played via your browser. <strong>You do not have to install any
            software.</strong>
        </div>

        <div style="padding-top: 28px;
                    clear: both;
                    width: 410px;
                    text-align: center;">
            <a style="font-size: 14px;"
               href="<c:url value='/forgot_password'/>">forgot password</a>
            &nbsp;|&nbsp;
            <a style="font-size: 14px;"
               href="<c:url value='/forgot_username'/>">forgot username</a>
        </div>
    </div>
</div>
