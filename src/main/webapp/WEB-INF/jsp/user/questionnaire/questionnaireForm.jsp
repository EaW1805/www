<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<jsp:useBean id="qUser" scope="request" class="com.eaw1805.data.model.User"/>
<style type="text/css">
    .pagehead {
        background: none;
        margin-top: -192px;
    }

    #content {
        padding-left: 10px;
        padding-right: 60px;
        padding-bottom: 0px;
        overflow: visible;
    }

    #header .parchment-head {
        position: relative;
        margin: 0;
        width: 0;
        height: 0;
        padding: 0;
        background: none;
    }

    .topbox {
        margin-top: -39px !important;
    }

    #header {
        height: 20px !important;
    }

    .userbox {
        margin-top: -33px!important;
    }

</style>

<div class="pagehead"
     style="background-image: url('http://static.eaw1805.com/images/site/HallOfFamePanelsBig.png');
            background-position: 0px 0px;
            background-repeat: no-repeat;
            float: left;
            margin-top: -54px;
            clear: both;
            background-size: 560px 840px;
            margin-left: 168px;
            width: 560px;
            height: 841px;">

    <div style="padding-top: 40px; padding-left: 30px;">
        <script type="text/javascript">
            var RecaptchaOptions = {
                theme:'clean',
                tabindex:9
            };
        </script>

<form:form name="generalSettings" commandName="questionnaire" method="POST" action='${uuid}'>

    <h3 style="width: 453px;"><p>Dear ${qUser.username},</p>
        <p>
    We noticed that you have not logged in for a while - what is the main reason you are discontinuing? (you may choose more than one reason)
        </p>
        </h3>
        <%--<h3>Questionnaire</h3>--%>
        <div class="formbody" style="margin-top: 75px;">
            <div>
                    <form:checkbox path="myBrowser" id="myBrowser"
                                class="customInput"
                                tabindex="1"
                                style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
            <label for="myBrowser"
                   style="font-size: 16px;
                width: 450px;
                height: 50px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">The game does not work on my browser (please specify your browser below)</label>
                <div style="margin-left: 50px;">
                    <div>
                        <form:checkbox path="browserChrome" id="browserChrome"
                                       class="customInput"
                                       tabindex="1"
                                       style="font-size: 16px;
                            clear:both;
                            float: left;
                            margin-top: -2px;
                            padding-left: 2px;
                            height: 20px;"/>
                        <label for="browserChrome"
                               style="font-size: 16px;
                    width: 450px;
                    height: 28px;
                    margin-left: 11px;
                    margin-top: -4px;
                    color: #444446;">Chrome</label>

                    </div>
                    <div>
                        <form:checkbox path="browserFirefox" id="browserFirefox"
                                       class="customInput"
                                       tabindex="1"
                                       style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                        <label for="browserFirefox"
                               style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">Firefox</label>

                    </div>
                    <div>
                        <form:checkbox path="browserIE" id="browserIE"
                                       class="customInput"
                                       tabindex="1"
                                       style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                        <label for="browserIE"
                               style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">Internet Explorer</label>

                    </div>
                    <div>
                        <form:checkbox path="browserOpera" id="browserOpera"
                                       class="customInput"
                                       tabindex="1"
                                       style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                        <label for="browserOpera"
                               style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">Opera</label>

                    </div>
                    <div>
                        <form:checkbox path="browserNetscape" id="browserNetscape"
                                       class="customInput"
                                       tabindex="1"
                                       style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                        <label for="browserNetscape"
                               style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">Netscape</label>

                    </div>
                    <div>
                        <form:checkbox path="browserOther" id="browserOther"
                                       class="customInput"
                                       tabindex="1"
                                       style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                        <label for="browserOther"
                               style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">Other</label>

                    </div>
                </div>
                </div>


            <div>
                <form:checkbox path="slowUI" id="slowUI"
                               class="customInput"
                               tabindex="1"
                               style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                <label for="slowUI"
                       style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">The User Interface is slow</label>
            </div>

            <div>
                <form:checkbox path="difficultUI" id="difficultUI"
                               class="customInput"
                               tabindex="1"
                               style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                <label for="difficultUI"
                       style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">The User Interface is difficult to understand</label>
            </div>


            <div>
                <form:checkbox path="website" id="website"
                               class="customInput"
                               tabindex="1"
                               style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                <label for="website"
                       style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">I have problems finding my way around the website</label>
            </div>
            <div>
                <form:checkbox path="rules" id="rules"
                               class="customInput"
                               tabindex="1"
                               style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                <label for="rules"
                       style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">The rules of the game are too complex for me</label>
            </div>

            <div>
                <form:checkbox path="noTime" id="noTime"
                               class="customInput"
                               tabindex="1"
                               style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                <label for="noTime"
                       style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">I don't have the time needed to learn the game</label>
            </div>

            <div>
                <form:checkbox path="graphics" id="graphics"
                               class="customInput"
                               tabindex="1"
                               style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                <label for="graphics"
                       style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">The atmosphere & graphics do not appeal to me</label>
            </div>

            <div>
                <form:checkbox path="payment" id="payment"
                               class="customInput"
                               tabindex="1"
                               style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                <label for="payment"
                       style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">I don`t want to pay for the game</label>
            </div>

            <div>
                <form:checkbox path="customerService" id="customerService"
                               class="customInput"
                               tabindex="1"
                               style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                <label for="customerService"
                       style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">Your Customer Service is slow/insufficient</label>
            </div>

            <div>
                <form:checkbox path="tutorial" id="tutorial"
                               class="customInput"
                               tabindex="1"
                               style="font-size: 16px;
                        clear:both;
                        float: left;
                        margin-top: -2px;
                        padding-left: 2px;
                        height: 20px;"/>
                <label for="tutorial"
                       style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">The Tutorial game was not enough to help me learn the game</label>
            </div>

            <div>

                <label for="comments"
                       style="font-size: 16px;
                width: 450px;
                height: 28px;
                margin-left: 11px;
                margin-top: -4px;
                color: #444446;">Other (please specify):</label>
                <br>
                <form:textarea path="comments" id="comments" cols="55" rows="7"/>
            </div>
    <%--O  --%>
    <%--O  --%>
    <%--O  --%>
    <%--O  --%>
    <%--O  --%>
    <%--O  --%>
    <%--O  --%>

    <%--Thank you in advance for your feedback!--%>

    <%--Kind Regards,--%>
    <%--Oplon Games--%>

            <br>
        <input type="submit" value="submit"/>


</form:form>

    </div>
</div>
</div>
