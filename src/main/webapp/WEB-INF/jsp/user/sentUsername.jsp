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
            margin-top: 10px;
            clear: both;
            background-size: 520px 427px;
            margin-left: 188px;
            width: 520px;
            height: 351px;">

    <div style="padding-top: 50px; padding-left: 30px;">
        <h1 style="font-size: 38px;
                       width: 410px;
                       text-align: center;">Username Sent via E-mail</h1>

        <div style="padding-top: 30px;
                    padding-bottom: 20px;
                    clear: both;
                    width: 410px;
                    font-size: 14px;
                    text-align: justify;">
            An automated e-mail message was send to you containing your username.
        </div>

        <div style="padding-top: 0px;
                    padding-bottom: 20px;
                    clear: both;
                    width: 410px;
                    font-size: 14px;
                    text-align: justify;">
            Please check your mailbox and make sure that the message was not accidentally marked JUNK.
        </div>

        <div style="padding-top: 0px;
                    padding-bottom: 20px;
                    clear: both;
                    width: 410px;
                    font-size: 14px;
                    text-align: justify;">
            In case you do not receive this e-mail in the next five minutes, please contact us via the
            <a href="<c:url value="/contact"/>">Contact &amp; Support</a> page.
        </div>


        <div style="clear: both;
                    padding-top: 10px;
                    padding-left: 100px;">
            <a href="<c:url value="/login"/>">
                <img src="http://static.eaw1805.com/images/site/signup/ButLogInOff.png"
                     alt="Log In to Empires at War 1805"
                     title="Log In to Empires at War 1805"
                     style="font-size: 22px;
                                  padding-left: 0px;
                                  width: 208px;
                                  height: 56px;"
                     onmouseover="this.src='http://static.eaw1805.com/images/site/signup/ButLogInHover.png'"
                     onmouseout="this.src='http://static.eaw1805.com/images/site/signup/ButLogInOff.png'">
            </a>
        </div>
    </div>
</div>
