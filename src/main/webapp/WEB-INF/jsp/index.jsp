<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src='http://static.eaw1805.com/js/slideshow/slides.jquery.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/slideshow/slides.min.jquery.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.transform.js'></script>

<link rel="stylesheet" type="text/css" href='http://static.eaw1805.com/style/slideshow.css'/>
<jsp:useBean id="hofProfiles" scope="request"
             type="java.util.HashMap<java.lang.String,java.util.LinkedList< com.eaw1805.data.dto.web.HofDTO>>"/>
<jsp:useBean id="topNations" scope="request"
             type="java.util.HashMap< com.eaw1805.data.model.UserGame,java.lang.Integer>"/>

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

    span.logout-button-icon button {
        margin-top: -126px;
    }

    .loginbox {
        float: right;
        clear: right;
        margin-top: -127px;
        margin-right: 58px;
        display: block;
    }

    .userbox {
        margin-top: -127px;
        margin-right: 92px;
    }

    .topbox {
        margin-top: -133px;
    }

    #footer .parchment-footer {
        background: none;
    }

    .slides_container {
        width: 985px;
        height: 270px;
        display: block;
    }

    .site ul {
        white-space: normal;
    }

    ul.pagination {
        margin: 26px auto 0 476px !important;
        width: 100px;
    }

        /*
            Next/prev buttons
        */
    #slidess .next, #slidess .prev {
        position: absolute;
        top: 188px;
        left: 5px;
        width: 24px;
        height: 43px;
        display: block;
        z-index: 101;
    }

    #slidess .next {
        left: 995px;
    }

    .caption {
        z-index: 500;
        position: absolute;
        left: 0;
        top: 0;
        height: 60px;
        padding: 5px 20px 0 20px;
        background: rgba(0, 0, 0, .5);
        font-size: 1.3em;
        line-height: 1.33;
        color: #fff;
        border-top: rgba(0, 0, 0, .5);
        text-shadow: none;
    }
</style>

<script>

function isMobileDevice() {
    var isMob = navigator.userAgent.match(/(iPad)|(iPhone)|(iPod)|(android)|(webOS)/i)
    return isMob != null;
}

$(document).ready(function () {
    if (!isMobileDevice()) {
        $("#flash-container").css("display", "");
    }
});

function animatePolitics() {
    $("#slide3").css({marginLeft:"-326px", marginTop:"-71px", transform:'scale(0.5,0.5)'});
    $("#relationsorder").css({display:"none"});
    $("#politicsButton").css({display:"none"});
    $("#animCursor3").css({left:"559px", top:"215px"});
    var scale = 0.9;
    var marginTopToGo = -449;
    var marginLeftToGo = -233;
    $("#slide3").stop().animate({
        marginTop:marginTopToGo + "px",
        marginLeft:marginLeftToGo + "px",
        transform:'scale(' + scale + ',' + scale + ')'
    }, 2000);
    $("#animCursor3").stop().animate({
        top:"902px",
        left:"686px"
    }, 2000, function () {
        $("#politicsButton").css({display:""});
        $("#relationsPanel").css({display:""});
        scale = 0.5;
        marginTopToGo = -64;
        marginLeftToGo = -329;
        $("#slide3").stop().animate({
            marginTop:marginTopToGo + "px",
            marginLeft:marginLeftToGo + "px",
            transform:'scale(' + scale + ',' + scale + ')'
        }, 2000, function () {
            scale = 0.8;
            marginTopToGo = -64;
            marginLeftToGo = -461;
            $("#slide3").stop().animate({
                marginTop:marginTopToGo + "px",
                marginLeft:marginLeftToGo + "px",
                transform:'scale(' + scale + ',' + scale + ')'
            }, 2000);
            $("#animCursor3").stop().animate({
                top:"400px",
                left:"1294px"
            }, 2000, function () {
                $("#relationsPanel").css({display:"none"});
                $("#relationsPanel2").css({display:""});
                $("#animCursor3").stop().animate({
                    top:"177px",
                    left:"1300px"
                }, 2000, function () {
                    $("#relationsPanel2").css({display:"none"});
                    $("#relationsorder").css({display:""});
                    scale = 2.0;
                    marginTopToGo = -917;
                    marginLeftToGo = -1645;
                    $("#slide3").stop().animate({
                        marginTop:marginTopToGo + "px",
                        marginLeft:marginLeftToGo + "px",
                        transform:'scale(' + scale + ',' + scale + ')'
                    }, 2000, function () {
                        setTimeout(function () {
                            $('#slidess .next').click();
                        }, 2000);
                    });
                });
            });
        });
    });
}

function animateForm() {
    $("#slide4").css({marginLeft:"-326px", marginTop:"-71px", transform:'scale(0.5,0.5)'});
    $("#animCursor4").css({left:"559px", top:"215px"});
    $("#formcorpspanel5").css({display:"none"});
    $("#formcorpspanel3").css({display:"none"});
    var scale = 0.9;
    var marginTopToGo = -183;
    var marginLeftToGo = -361;
    $("#slide4").stop().animate({
        marginTop:marginTopToGo + "px",
        marginLeft:marginLeftToGo + "px",
        transform:'scale(' + scale + ',' + scale + ')'
    }, 2000);
    $("#animCursor4").stop().animate({
        top:"415px",
        left:"884px"
    }, 2000, function () {
        $("#formcorpsarmypopup").css({display:""});
        $("#animCursor4").stop().animate({
            top:"429px",
            left:"894px"
        }, 1500, function () {
            $("#formcorpsarmypopup").css({display:"none"});
            $("#formcorpspanel").css({display:""});
            scale = 0.6;
            marginTopToGo = -81;
            marginLeftToGo = -361;
            $("#slide4").stop().animate({
                marginTop:marginTopToGo + "px",
                marginLeft:marginLeftToGo + "px",
                transform:'scale(' + scale + ',' + scale + ')'
            }, 2000, function () {
                $("#animCursor4").stop().animate({
                    top:"677px",
                    left:"494px"
                }, 2000, function () {
                    $("#formcorpspanel").css({display:"none"});
                    $("#formcorpspanel2").css({display:""});
                    $("#animCursor4").stop().animate({
                        top:"513px",
                        left:"494px"
                    }, 2000, function () {
                        $("#formcorpspanel2").css({display:"none"});
                        $("#formcorpspanel3").css({display:""});
                        $("#animCursor4").stop().animate({
                            top:"330px",
                            left:"494px"
                        }, 2000, function () {
                            $("#formcorpspanel3").css({display:""});
                            $("#formcorpspanel4").css({display:""});
                            $("#animCursor4").stop().animate({
                                top:"255px",
                                left:"1293px"
                            }, 2000, function () {
                                $("#formcorpspanel4").css({display:"none"});
                                $("#formcorpspanel5").css({display:""});
                                setTimeout(function () {
                                    $('#slidess .next').click();
                                }, 2000);
                            })
                        });
                    });
                });
            });
        });
    });
}

function animateMovement() {
    $("#slide2").css({marginLeft:"-328px", marginTop:"-392px", transform:'scale(0.5,0.5)'});
    $("#slide2").css({marginLeft:"-326px", marginTop:"0px"});
    $("#selectarmies").css({"display":"none"});
    $("#armyMenu").css({"display":"none"});
    $("#aftermovement").css({"display":"none"});

    var scale = 0.9;
    var marginTopToGo = -215;
    var marginLeftToGo = -559;
    $("#slide2").stop().animate({
        marginTop:marginTopToGo + "px",
        marginLeft:marginLeftToGo + "px",
        transform:'scale(' + scale + ',' + scale + ')'
    }, 2000, function () {
        $("#animCursor2").stop().animate({
            top:"433px",
            left:"1027px"
        }, 2000, function () {
            $("#selectArmies").css("display", "");
            $("#animCursor2").stop().animate({
                top:"463px",
                left:"1090px"
            }, 2000, function () {
                $("#selectArmies").css("display", "none");
                $("#armyMenu").css("display", "");
                $("#animCursor2").stop().animate({
                    top:"380px",
                    left:"1012px"
                }, 2000, function () {
                    $("#armyMenu").css("display", "none");
                    $("#animCursor2").stop().animate({
                        top:"500px",
                        left:"741px"
                    }, 2000, function () {
                        $("#aftermovement").css("display", "");
                        setTimeout(function () {
                            $('#slidess .next').click();
                        }, 2000);
                    });
                });
            });
        });
    });
}

$(function () {
    $('#slides').slides({
        preload:true,
        preloadImage:'http://static.eaw1805.com/images/site/loading.gif',
        play:10000,
        pause:10000,
        hoverPause:true,
        animationStart:function (current) {
        }
    });

    $('#slidess').slides({
        preload:true,
        preloadImage:'http://static.eaw1805.com/images/site/loading.gif',
        play:0,
        pause:10000,
        pagination:false,
        generatePagination:false, // boolean, Auto generate pagination
        hoverPause:true,
        animationStart:function (current) {
            // example return of current slide number
            if (current == 1) {
                animateMovement();

            } else if (current == 2) {
                setTimeout(function () {
                    $('#slidess .next').click();
                }, 12000);

            } else if (current == 3) {
                animateForm();

            } else if (current == 4) {
                animatePolitics();

            } else if (current == 5) {
                setTimeout(function () {
                    $('#slidess .next').click();
                }, 11000);
            }
            ;
        }
    });
});
</script>
<div style="position: relative;margin: -10px 0px 0px -35px; width: 1035px;min-height: 430px;
background: url('http://static.eaw1805.com/images/site/NewsParchment.png') repeat-y scroll 0px 0px transparent;clear: both;
background-size: 1035px 430px;">

<div id="container" style="clear: both;width: 1035px; height: 400px;padding: 0;">
<div id="slidess">
<div class="slides_container" style="width: 1035px;height: 400px;">

<div class="slide" style="width: 1035px;">
    <img src='http://direct.eaw1805.com/images/site/napoleonic-wars.png'
         alt="A full-feldged HTML5 strategy game simulating all aspects of European history, during the Napoleonic era starting in 1805."
         title=""
         border=0 height=375
         onclick="window.location='<c:url value="/joingame"/>';"
         style="float: left; border: 0 none; margin-top: 20px; margin-left: 20px; cursor: pointer;">

    <p style="float: right; text-align: center; width: 480px; margin-top: 28px; margin-right: 40px; font-size: 14pt;">
        <strong>A browser based strategy game simulating all aspects of
            European history, during the Napoleonic era.</strong></p>

    <img src='http://static.eaw1805.com/images/eaw1805.png'
         alt="A full-feldged HTML5 strategy game simulating all aspects of European history, during the Napoleonic era starting in 1805."
         title=""
         border=0 width=260
         onclick="window.location='<c:url value="/joingame"/>';"
         style="float: right; border: 0 none; margin-top: 8px; margin-right: 140px; cursor: pointer;">

    <p style="float: right; text-align: center; width: 480px; margin-top: 8px; margin-right: 30px;">
        <a style="color: black; font-size: 14pt;" href="<c:url value="/joingame"/>">Sign Up &amp; Start playing for
            Free</a></p>

    <p style="float: right; text-align: center; width: 490px; margin-top: 8px; margin-right: 40px;  font-size: 11pt;">
        Challenge your opponents on the most complete warfare system of similar type games, depicting the richness
        of the combined arms military systems of the Napoleonic era.</p>

    <script type="text/javascript">
        setTimeout(function () {
            $('#slidess .next').click();
        }, 9000);
    </script>
</div>

<%--<div class="slide" style="width: 1035px;margin-left: 49px;margin-top: 22px;">--%>
<%--<div class="badge shadow rounded" style="margin-top: 43px;float: left;margin-left: 30px;">--%>
<%--<div class="image clearfix">--%>
<%--<a href="http://www.indiegogo.com/projects/247705?a=1582854&amp;i=wdgt" target="_blank">--%>
<%--<img alt="20121005042820-logoanimsmall" class="fl rounded rounded-no-bottom" height="194"--%>
<%--src="http://d2oadd98wnjs7n.cloudfront.net/projects/247705/pictures/baseball/20121005042820-logoAnimSmall.gif?1349436569"--%>
<%--width="220">--%>
<%--</a></div>--%>
<%--<div class="project-category translate" style="clear: both;">--%>
<%--<a style="color: black; font-size: 10pt;"--%>
<%--href="http://www.indiegogo.com/projects/247705?a=1582854&amp;i=wdgt">IndieGogo Campaign</a>--%>
<%--</div>--%>
<%--<div class="targets clearfix">--%>
<%--<p class="progress complete20 ir rounded rounded-no-right">Starting - 0.245067 Complete</p>--%>

<%--<div class="baseball_stats_wrapper">--%>
<%--<div class="baseball_stats">--%>
<%--<p class="fl percent-funded"><span class="amount notranslate">25%</span><br>funded</p>--%>

<%--<p class="fl money-raised"><span class="amount notranslate">$1,838</span><br>raised</p>--%>

<%--<p class="fl days-left"><span class="amount notranslate"> 50 </span><br> hours left</p>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--<div style="float: right; text-align: center; width: 600px; margin-top: 30px; margin-right: 110px;">--%>
<%--<p style="float: right; text-align: center; width: 600px; margin-top: 0px; margin-right: 5px;  font-size: 18pt;">--%>
<%--<strong>Empires at War 1805 is a work in progress.</strong></p>--%>

<%--<p style="float: right; text-align: center; width: 600px; margin-top: 15px; margin-right: 5px;  font-size: 18pt;">--%>
<%--<strong>Help us finish it by supporting our campaign at</strong></p>--%>

<%--<a href="http://www.indiegogo.com/eaw-1805">--%>
<%--<img src='http://static.eaw1805.com/images/site/indiegogo-empires-at-war-1805.png'--%>
<%--alt="A full-feldged HTML5 strategy game simulating all aspects of European history, during the Napoleonic era starting in 1805."--%>
<%--title=""--%>
<%--border=0 width=520--%>
<%--style="position: absolute; top: 110px;left: 380px;"></a>--%>

<%--<p style="float: right; text-align: center; width: 600px; margin-top: 130px; margin-right: 5px; font-size: 12pt;">--%>
<%--Your help, regardless how small it is, will be invaluable to us not only as a resource, but also as a morale--%>
<%--boost that there is a community here to back us up.</p>--%>

<%--<p style="float: right; text-align: center; width: 600px; margin-top: 15px; margin-right: 5px; font-size: 14pt;">--%>
<%--Play Empires at War for free. Have your name added in our credits page. Receive our fully coloured &--%>
<%--illustrated Game Manual to your home address. Create your own Eaw1805 scenario.</p>--%>
<%--</div>--%>
<%--</div>--%>

<div class="slide" style="width: 1035px;margin-left: 49px;margin-top: 22px;">
<div class="caption"
     onclick="window.location='<c:url value="/features"/>';"
     style="width:906px; cursor: pointer;">
    <h1 style="font-size: 42px;text-align: center;color: #ffffff;">
        Rich Combined Arms Military System</h1></div>
<div class="caption"
     onclick="window.location='<c:url value="/joingame"/>';"
     style="position: absolute; width:400px; left: 505px; top: 320px; cursor: pointer;">
    <h1 style="font-size: 32px;text-align: center;color: #ffffff;">
        Sign Up &amp; Start Playing Now</h1></div>
<div id="anim_container2" style="clear: both;width: 945px; height: 400px; overflow: hidden;">
<div id="anim_slides2">
<div id="slide2" style="margin-left: -326px;margin-top: 0px;transform:scale(0.5,0.5);">
<div class="slide" style="width: 985px;position: relative;">
<img src="http://static.eaw1805.com/images/animation/movementbackground.jpg" alt="bg"
     style="position: absolute;left: 10px;top: 10px;">

<div id="selectArmies"
     style="display:none; left: 1020px; top: 432px; position: absolute; overflow: visible;"
     class="">
    <div class="popupContent">
        <table cellspacing="0" cellpadding="0">
            <tbody>
            <tr>
                <td align="left" style="vertical-align: top;"><img
                        src="http://static.eaw1805.com/images/buttons/ButFormFedOff.png"
                        class="gwt-Image pointer" id="widget-575" title="Organize Armies"
                        style="width: 32px; height: 32px;"></td>
                <td align="left" style="vertical-align: top;">
                    <table cellspacing="0" cellpadding="0">
                        <tbody>
                        <tr>
                            <td align="left" style="vertical-align: top;">
                                <table cellspacing="0" cellpadding="0">
                                    <tbody>
                                    <tr>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 64px; height: 64px;">
                                                <img class="gwt-Image"
                                                     src="http://static.eaw1805.com/images/figures/corpBase.png"
                                                     style="width: 64px; height: 64px; position: absolute; left: 0px; top: 0px;"><img
                                                    class="pointer" id="widget-576"
                                                    src="http://static.eaw1805.com/images/figures/17/UnitMap00.png"
                                                    style="width: 64px; height: 64px; position: absolute; left: 0px; top: 0px;"><img
                                                    src="http://static.eaw1805.com/images/figures/walk.png"
                                                    class="gwt-Image"
                                                    style="display: none; position: absolute; left: 43px; top: 0px; width: 21px; height: 21px;">
                                            </div>
                                        </td>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 64px; height: 64px;">
                                                <img class="pointer" id="widget-577"
                                                     src="http://static.eaw1805.com/images/figures/17/UnitMap00.png"
                                                     style="width: 64px; height: 64px; position: absolute; left: 0px; top: 0px;"><img
                                                    src="http://static.eaw1805.com/images/figures/walk.png"
                                                    class="gwt-Image"
                                                    style="display: none; position: absolute; left: 43px; top: 0px; width: 21px; height: 21px;">
                                            </div>
                                        </td>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 64px; height: 64px;">
                                                <img class="pointer" id="widget-578"
                                                     src="http://static.eaw1805.com/images/figures/17/UnitMap00.png"
                                                     style="width: 64px; height: 64px; position: absolute; left: 0px; top: 0px;"><img
                                                    src="http://static.eaw1805.com/images/figures/walk.png"
                                                    class="gwt-Image"
                                                    style="display: none; position: absolute; left: 43px; top: 0px; width: 21px; height: 21px;">
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="left" style="vertical-align: top;">
                                <table cellspacing="0" cellpadding="0">
                                    <tbody>
                                    <tr>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 64px; height: 64px;">
                                                <img class="pointer" id="widget-579"
                                                     src="http://static.eaw1805.com/images/figures/17/UnitMap00.png"
                                                     style="width: 64px; height: 64px; position: absolute; left: 0px; top: 0px;"><img
                                                    src="http://static.eaw1805.com/images/figures/walk.png"
                                                    class="gwt-Image"
                                                    style="display: none; position: absolute; left: 43px; top: 0px; width: 21px; height: 21px;">
                                            </div>
                                        </td>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 64px; height: 64px;">
                                                <img class="pointer" id="widget-580"
                                                     src="http://static.eaw1805.com/images/figures/17/UnitMap00.png"
                                                     style="width: 64px; height: 64px; position: absolute; left: 0px; top: 0px;"><img
                                                    src="http://static.eaw1805.com/images/figures/walk.png"
                                                    class="gwt-Image"
                                                    style="display: none; position: absolute; left: 43px; top: 0px; width: 21px; height: 21px;">
                                            </div>
                                        </td>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 64px; height: 64px;">
                                                <img class="pointer" id="widget-581"
                                                     src="http://static.eaw1805.com/images/figures/17/UnitMap00.png"
                                                     style="width: 64px; height: 64px; position: absolute; left: 0px; top: 0px;"><img
                                                    src="http://static.eaw1805.com/images/figures/walk.png"
                                                    class="gwt-Image"
                                                    style="display: none; position: absolute; left: 43px; top: 0px; width: 21px; height: 21px;">
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="left" style="vertical-align: top;">
                                <table cellspacing="0" cellpadding="0">
                                    <tbody>
                                    <tr>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 64px; height: 64px;">
                                                <img class="pointer" id="widget-582"
                                                     src="http://static.eaw1805.com/images/figures/17/UnitMap00.png"
                                                     style="width: 64px; height: 64px; position: absolute; left: 0px; top: 0px;"><img
                                                    src="http://static.eaw1805.com/images/figures/walk.png"
                                                    class="gwt-Image"
                                                    style="display: none; position: absolute; left: 43px; top: 0px; width: 21px; height: 21px;">
                                            </div>
                                        </td>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 64px; height: 64px;">
                                                <img class="pointer" id="widget-583"
                                                     src="http://static.eaw1805.com/images/figures/17/UnitMap00.png"
                                                     style="width: 64px; height: 64px; position: absolute; left: 0px; top: 0px;"><img
                                                    src="http://static.eaw1805.com/images/figures/walk.png"
                                                    class="gwt-Image"
                                                    style="display: none; position: absolute; left: 43px; top: 0px; width: 21px; height: 21px;">
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<div id="armyMenu"
     style="display:none; left: 956px; top: 346px; position: absolute; overflow: visible;" class="">
    <div class="popupContent">
        <table cellspacing="0" cellpadding="0">
            <tbody>
            <tr>
                <td align="left" style="vertical-align: top;">
                    <div style="position: relative; overflow: hidden; width: 222px; height: 137px;"
                         class=""><img
                            src="http://static.eaw1805.com/images/buttons/menu/LeftEnd.png"
                            class="gwt-Image"
                            style="width: 37px; height: 73px; position: absolute; left: 0px; top: 0px;">

                        <div style="position: absolute; overflow: hidden; left: 66px; top: 73px; width: 64px; height: 64px;">
                            <div style="position: absolute; overflow: hidden; width: 64px; height: 64px; left: 0px; top: 0px;">
                                <img class="gwt-Image"
                                     src="http://static.eaw1805.com/images/figures/corpBase.png"
                                     style="width: 64px; height: 64px; position: absolute; left: 0px; top: 0px;"><img
                                    class="pointer" id="widget-565"
                                    src="http://static.eaw1805.com/images/figures/17/UnitMap00.png"
                                    style="width: 64px; height: 64px; position: absolute; left: 0px; top: 0px;"><img
                                    src="http://static.eaw1805.com/images/figures/walk.png"
                                    class="gwt-Image"
                                    style="display: none; position: absolute; left: 43px; top: 0px; width: 21px; height: 21px;">
                            </div>
                        </div>
                        <img src="http://static.eaw1805.com/images/buttons/menu/Middle.png"
                             class="gwt-Image"
                             style="width: 37px; height: 73px; position: absolute; left: 37px; top: 0px;"><img
                                src="http://static.eaw1805.com/images/buttons/ButMoveOff.png"
                                class="pointer" id="widget-571" title="Issue move orders."
                                style="width: 36px; height: 36px; position: absolute; left: 40px; top: 19px;"><img
                                src="http://static.eaw1805.com/images/buttons/menu/Middle.png"
                                class="gwt-Image"
                                style="width: 37px; height: 73px; position: absolute; left: 74px; top: 0px;"><img
                                src="http://static.eaw1805.com/images/buttons/ButEmbarkOff.png"
                                class="pointer" id="widget-572" title="Board."
                                style="width: 36px; height: 36px; position: absolute; left: 77px; top: 19px;"><img
                                src="http://static.eaw1805.com/images/buttons/menu/Middle.png"
                                class="gwt-Image"
                                style="width: 37px; height: 73px; position: absolute; left: 111px; top: 0px;"><img
                                src="http://static.eaw1805.com/images/buttons/ButAssignLeaderOff.png"
                                class="pointer" id="widget-573" title="Assign commander."
                                style="width: 36px; height: 36px; position: absolute; left: 114px; top: 19px;"><img
                                src="http://static.eaw1805.com/images/buttons/menu/Middle.png"
                                class="gwt-Image"
                                style="width: 37px; height: 73px; position: absolute; left: 148px; top: 0px;"><img
                                src="http://static.eaw1805.com/images/buttons/ButDismissCommanderOff.png"
                                class="pointer" id="widget-574" title="Dismiss commander"
                                style="width: 36px; height: 36px; position: absolute; left: 151px; top: 19px;"><img
                                src="http://static.eaw1805.com/images/buttons/menu/RightEnd.png"
                                class="gwt-Image"
                                style="width: 37px; height: 73px; position: absolute; left: 185px; top: 0px;">
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<img src="http://static.eaw1805.com/images/animation/aftermovement.png" id="aftermovement"
     style="position: absolute; left: 685px; top: 421px; display: none;">
<img src="http://static.eaw1805.com/images/animation/cursor.png" id="animCursor2"
     style="position: absolute; left: 559px; top: 215px;">

</div>
</div>
</div>
</div>
</div>

<div class="slide" style="width: 1035px;margin-left: 49px;margin-top: 22px;">

    <a href="<c:url value="/joingame/free"/>">
        <img src="http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_C_Off.png"
             alt="Free Strategy Game"
             title="Play a Free Position"
             onclick="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_C_On.png'"
             onmouseover="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_C_Hover.png'"
             onmouseout="this.src='http://static.eaw1805.com/images/site/gameButtons/ThreeStartButtons_C_Off.png'"
             width=320px
             style="float: left; border: 0 none; margin-top: 30px; margin-left: 10px; cursor: pointer;">
    </a>

    <div style="float: right; text-align: center; width: 580px; margin-top: 30px; margin-right: 100px;">
        <p style="float: right; text-align: center; width: 580px; margin-top: 0px; margin-right: 5px;  font-size: 29pt;">
            <strong>Play Empires at War for free</strong></p>

        <p style="float: right; text-align: center; width: 580px; margin-top: 20px; margin-right: 5px;  font-size: 18pt;">
            <strong>Start now and learn the game mechanics</strong></p>

        <p style="float: right; text-align: center; width: 580px; margin-top: 35px; margin-right: 5px; font-size: 14pt;">
            Assume the role of Napoleon Bonaparte starting from January 1804.</p>

        <p style="float: right; text-align: center; width: 580px; margin-top: 15px; margin-right: 5px; font-size: 14pt;">
            Become the political and military leader of France during the latter stages of the French Revolution and its
            associated wars in Europe.</p>

        <p style="float: right; text-align: center; width: 580px; margin-top: 30px; margin-right: 5px; font-size: 12pt;">
            <strong>Empires at War is a sophisticated Browser strategy game</strong></p>

        <p style="float: right; text-align: center; width: 580px; margin-top: 15px; margin-right: 5px; font-size: 26pt;">
            <strong>No download required</strong></p>
    </div>
</div>

<div class="slide" style="width: 1035px;margin-left: 49px;margin-top: 22px;">
    <div class="caption"
         onclick="window.location='<c:url value="/features"/>';"
         style="width:906px; cursor: pointer;">
        <h1 style="font-size: 42px;text-align: center;color: #ffffff;">Multi-Level
            Structuring of Napolenic Era Forces</h1></div>
    <div class="caption"
         onclick="window.location='<c:url value="/joingame"/>';"
         style="position: absolute; width:400px; left: 505px; top: 320px; cursor: pointer;">
        <h1 style="font-size: 32px;text-align: center;color: #ffffff;">
            Sign Up &amp; Start Playing Now</h1></div>
    <div id="anim_container4" style="clear: both;width: 945px; height: 400px; overflow: hidden;">
        <div id="anim_slides4">
            <div id="slide4" style="margin-left: -326px;margin-top: -71px;transform:scale(0.5,0.5);">
                <div class="slide" style="width: 985px;position: relative;">
                    <img src="http://static.eaw1805.com/images/animation/formcorpbackground.jpg" alt="bg"
                         style="position: absolute;left: 10px;top: 10px;">
                    <img id="formcorpsarmypopup"
                         src="http://static.eaw1805.com/images/animation/formcorpsarmiespopup.png" alt="bg"
                         style="position: absolute;left: 864px;top: 400px; display: none;">
                    <img id="formcorpspanel" src="http://static.eaw1805.com/images/animation/formcorpspanel.png"
                         alt="bg"
                         style="position: absolute;left: 356px;top: 133px; display: none;">
                    <img id="formcorpspanel2" src="http://static.eaw1805.com/images/animation/formcorpspanel2.png"
                         alt="bg"
                         style="position: absolute;left: 356px;top: 132px; display: none;">

                    <img id="formcorpspanel3" src="http://static.eaw1805.com/images/animation/formcorpspanel3.png"
                         alt="bg"
                         style="position: absolute;left: 356px;top: 132px; display: none;">

                    <img id="formcorpspanel4" src="http://static.eaw1805.com/images/animation/formcorpspanel4.png"
                         alt="bg"
                         style="position: absolute;left: 366px;top: 138px; display: none;">
                    <img id="formcorpspanel5" src="http://static.eaw1805.com/images/animation/formcorpspanel5.png"
                         alt="bg"
                         style="position: absolute;left: 361px;top: 137px; display: none;">

                    <img src="http://static.eaw1805.com/images/animation/cursor.png" id="animCursor4"
                         style="position: absolute; left: 559px; top: 215px;">
                </div>
            </div>
        </div>
    </div>
</div>

<div class="slide" style="width: 1035px;margin-left: 49px;margin-top: 22px;">
    <div class="caption"
         onclick="window.location='<c:url value="/features"/>';"
         style="width:905px; cursor: pointer;">
        <h1 style="font-size: 42px;text-align: center;color: #ffffff;">
            Detailed Politics &amp Foreign Relations</h1></div>
    <div class="caption"
         onclick="window.location='<c:url value="/joingame"/>';"
         style="position: absolute; width:400px; left: 505px; top: 320px; cursor: pointer;">
        <h1 style="font-size: 32px;text-align: center;color: #ffffff;">
            Sign Up &amp; Start Playing Now</h1></div>
    <div id="anim_container3" style="clear: both;width: 945px; height: 400px; overflow: hidden;">
        <div id="anim_slides3">
            <div id="slide3" style="margin-left: -326px;margin-top: -71px;transform:scale(0.5,0.5);">
                <div class="slide" style="width: 985px;position: relative;">
                    <img src="http://static.eaw1805.com/images/animation/politicsbackground.jpg" alt="bg"
                         style="position: absolute;left: 10px;top: 10px;">
                    <img id="politicsButton" src="http://static.eaw1805.com/images/layout/buttons/ButPoliticsOn.png"
                         class="pointer" title="Review &amp; Adjust Political Relations"
                         style="position: absolute; left: 659px; top: 892px; width: 57px; height: 23px;display:none;">
                    <img id="relationsPanel" src="http://static.eaw1805.com/images/animation/relationspanel.png"
                         class="pointer" title="Review &amp; Adjust Political Relations"
                         style="position: absolute; left: 559px; top: 145px; display:none;">
                    <img id="relationsPanel2" src="http://static.eaw1805.com/images/animation/relationspanel2.png"
                         class="pointer" title="Review &amp; Adjust Political Relations"
                         style="position: absolute; left: 558px; top: 144px; display:none;">
                    <img id="relationsorder" src="http://static.eaw1805.com/images/animation/relationorder.png"
                         class="pointer" title="Review &amp; Adjust Political Relations"
                         style="position: absolute; left: 1732px; top: 533px; display:none;">
                    <img src="http://static.eaw1805.com/images/animation/cursor.png" id="animCursor3"
                         style="position: absolute; left: 559px; top: 215px;">
                </div>

            </div>
        </div>
    </div>
</div>

</div>
<a href="#" class="prev" style="display: none;"></a>
<a href="#" class="next" style="display: none;"></a>
</div>
</div>

</div>

<div style="position: relative;margin: 0px 0px 0px -35px;width: 1000px;min-height: 360px;padding: 0px 40px;
background: url('http://static.eaw1805.com/images/site/NewsParchment.png') repeat-y scroll 0px 0px transparent;clear: both;
background-size: 1035px 360px;">

    <div id="container" style="clear: both;width: 985px; height: 280px;"
         onmouseover="$('.prev').show();$('.next').show();"
         onmouseout="$('.prev').hide();$('.next').hide();">
        <div id="slides">
            <div class="slides_container">
                <c:forEach var="article" items="${news}">
                    <div class="slide" style="width: 985px;">
                            ${article}
                    </div>
                </c:forEach>
            </div>
            <a href="#" class="prev" style="display: none;"><img
                    src="http://static.eaw1805.com/images/site/slider/left.png"
                    width="35" height="50" alt="Arrow Prev"></a>
            <a href="#" class="next" style="display: none;"><img
                    src="http://static.eaw1805.com/images/site/slider/right.png"
                    width="35" height="50" alt="Arrow Next"></a>
        </div>
    </div>
</div>

<div id="flash-container" style="display: none; position: relative;margin: 0px 0px 0px -35px; width: 1035px;min-height: 390px;
background: url('http://static.eaw1805.com/images/site/NewsParchment.png') repeat-y scroll 0px 0px transparent;clear: both;
background-size: 1035px 390px;">
    <div style="padding-top: 25px; padding-left: 30px;">
        <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" width="975" height="330" id="eawflash"
                align="middle">
            <param name="movie" value="http://static.eaw1805.com/images/site/eaw-playnow.swf"/>
            <param name="quality" value="high"/>
            <param name="bgcolor" value="#ffffff"/>
            <param name="play" value="true"/>
            <param name="loop" value="true"/>
            <param name="wmode" value="window"/>
            <param name="scale" value="showall"/>
            <param name="menu" value="true"/>
            <param name="devicefont" value="false"/>
            <param name="salign" value=""/>
            <param name="allowScriptAccess" value="sameDomain"/>
            <!--[if !IE]>-->
            <object type="application/x-shockwave-flash"
                    data="http://static.eaw1805.com/images/site/eaw-playnow.swf" width="975"
                    height="330">
                <param name="movie" value="ttp://static.eaw1805.com/images/site/eaw-playnow.swf"/>
                <param name="quality" value="high"/>
                <param name="bgcolor" value="#ffffff"/>
                <param name="play" value="true"/>
                <param name="loop" value="true"/>
                <param name="wmode" value="window"/>
                <param name="scale" value="showall"/>
                <param name="menu" value="true"/>
                <param name="devicefont" value="false"/>
                <param name="salign" value=""/>
                <param name="allowScriptAccess" value="sameDomain"/>
                <!--<![endif]-->
                <img src="http://static.eaw1805.com/images/site/playnow.png"
                     alt="Play Now Empires at War 1805"
                     onclick="window.location='<c:url value="/joingame"/>';"
                     style="cursor: pointer;">
                <!--[if !IE]>-->
            </object>
            <!--<![endif]-->
        </object>
    </div>
</div>

<div style="width: 510px;
background: url('http://static.eaw1805.com/images/site/NewsParchment.png') repeat-y scroll 0px 0px transparent;
background-size: 510px 470px;
float: left;
min-height: 470px;
margin-left: -34px;
margin-bottom: -50px;
margin-top: 10px;">
    <div style="margin: 10px;">
        <h1 style="margin: 10px;">Top Nations</h1>

        <div style="width: 500px;margin-top: 20px;margin-left: 5px;">
            <c:set var="index" value="0"/>
            <c:forEach items="${topNations}" var="entry">
                <c:if test="${index < 10}">
                    <div style="border-radius: 10px;
                            border-top: 1px solid white;
                            border-bottom: 1px solid #8F8F8F;
                            background: rgba(243, 243, 243, 0.3) none repeat-x 0 0;
                            margin-left: 0px;
                            margin-right: 4px;
                            height: 70px; width: 240px;float: left;cursor: pointer;"

                         title="More Info"
                         onclick='window.location = "<c:url value="/scenario/${entry.key.game.scenarioIdToString}/game/${entry.key.game.gameId}/info"/>";'
                            >

                        <img style="margin: 5px;"
                             src='http://static.eaw1805.com/images/nations/nation-${entry.key.nation.id}-list.jpg'/>

                        <p style="margin-left: 5px; width:110px;float: left;margin-top: 2px;">
                            Game ${entry.key.game.gameId} / ${monthsSmall[entry.key.game.gameId]}</p>

                        <p style="float: right;width: 100px;text-align: right;font-size: 24px;margin-top: -9px;margin-right: 16px;">
                            <span>VPs:</span> ${entry.value}</p>
                    </div>
                </c:if>
                <c:set var="index" value="${index+1}"/>
            </c:forEach>
        </div>
    </div>
</div>


<div style="width: 510px;
background: url('http://static.eaw1805.com/images/site/NewsParchment.png') repeat-y scroll 0px 0px transparent;
background-size: 510px 470px;
float: right;
min-height: 470px;
margin-bottom: -50px;
margin-right: -34px;
margin-top: 10px;">
    <div style="margin: 10px;">
        <h1 style="margin: 10px;">Top Players</h1>

        <div style="width: 500px;margin-top: 20px;margin-left: 5px;">
            <c:forEach items="${hofProfiles}" var="entry">
                <div style="width: 161px; float:left;">
                    <c:forEach items="${entry.value}" var="hofProfile">
                        <div style="border-radius: 10px;
                            border-top: 1px solid white;
                            border-bottom: 1px solid #8F8F8F;
                            background: rgba(243, 243, 243, 0.3) none repeat-x 0 0;
                            margin-left: 0px;
                            margin-right: 4px;
                            height: 70px; width: 156px;float: left;">


                            <a href='<c:url value="/user/${hofProfile.username}"/>'>
                                <img src="https://secure.gravatar.com/avatar/${hofProfile.user.emailEncoded}?s=48&d=http://static.eaw1805.com/images/site/Generic_Naval_Commander.png"
                                     alt="" height="48" width="48" title="${hofProfile.user.username}"
                                     style="padding: 2px;
                                    color: black !important;
                                    border: 1px solid rgba(0, 0, 0, 0.5);
                                    border-radius: 8px;
                                    -moz-border-radius: 8px;
                                    -webkit-border-radius: 8px;
                                    opacity: 1 !important;
                                    vertical-align: middle;
                                    margin: 8px;
                                    margin-right: 2px;
                                    float: left;">
                            </a>

                            <div style="cursor: pointer;height: 70px;"
                                 title="Hall Of Fame"
                                 onclick='window.location = "<c:url value="/hallOfFame"/>";'
                                    >
                                <c:if test="${entry.key == 'vps'}">
                                    <h2 style="float: left;height: 11px;white-space: normal;width: 18px;margin-top: -10px; font-size: 33px; margin-left: 38px;">
                                            ${hofProfile.vpsPosition}

                                    </h2>
                                    <c:if test="${hofProfile.vpsPosition == 1}">
                                        <h2 style="font-size: 12px;width: 11px;height: 10px;float: left;margin-top: 3px;margin-left: -3px;">
                                            st</h2>
                                    </c:if>
                                    <c:if test="${hofProfile.vpsPosition == 2}">
                                        <h2 style="font-size: 12px;width: 11px;height: 10px;float: left;margin-left: 2px;margin-top: 3px;">
                                            nd</h2>
                                    </c:if>
                                    <c:if test="${hofProfile.vpsPosition == 3}">
                                        <h2 style="font-size: 12px;width: 11px;height: 11px;float: left;margin-left: 2px;margin-top: 2px;">
                                            rd</h2>
                                    </c:if>
                                    <c:if test="${hofProfile.vpsPosition == 4 || hofProfile.vpsPosition == 5}">
                                        <h2 style="font-size: 12px;width: 11px;height: 11px;float: left;margin-left: 2px;margin-top: 2px;">
                                            th</h2>
                                    </c:if>
                                    <p style="width: 90px;float: right;margin-top: 23px;text-align: center;">on VPs</p>
                                </c:if>
                                <c:if test="${entry.key == 'battles.tactical.won'}">
                                    <h2 style="float: left;height: 11px;white-space: normal;width: 18px;margin-top: -10px;font-size: 33px;margin-left: 38px;">
                                            ${hofProfile.battlesTacticalWonPosition}
                                    </h2>
                                    <c:if test="${hofProfile.battlesTacticalWonPosition == 1}">
                                        <h2 style="font-size: 12px;width: 11px;height: 10px;float: left;margin-top: 3px;margin-left: -3px;">
                                            st</h2>
                                    </c:if>
                                    <c:if test="${hofProfile.battlesTacticalWonPosition== 2}">
                                        <h2 style="font-size: 12px;width: 11px;height: 10px;float: left;margin-left: 2px;margin-top: 3px;">
                                            nd</h2>
                                    </c:if>
                                    <c:if test="${hofProfile.battlesTacticalWonPosition == 3}">
                                        <h2 style="font-size: 12px;width: 11px;height: 11px;float: left;margin-left: 2px;margin-top: 2px;">
                                            rd</h2>
                                    </c:if>
                                    <c:if test="${hofProfile.battlesTacticalWonPosition == 4 || hofProfile.battlesTacticalWonPosition == 5}">
                                        <h2 style="font-size: 12px;width: 11px;height: 11px;float: left;margin-left: 2px;margin-top: 2px;">
                                            tf</h2>
                                    </c:if>
                                    <p style="width: 90px;float: right;margin-top: 16px;text-align: center;">on Tactical
                                        Battles
                                        Won</p>
                                </c:if>
                                <c:if test="${entry.key == 'battles.killed.all'}">
                                    <h2 style="float: left;height: 11px;white-space: normal;width: 18px;margin-top: -10px;font-size: 33px;margin-left: 38px;">
                                            ${hofProfile.enemyKilledPosition}
                                    </h2>
                                    <c:if test="${hofProfile.enemyKilledPosition == 1}">
                                        <h2 style="font-size: 12px;width: 11px;height: 10px;float: left;margin-top: 3px;margin-left: -3px;">
                                            st</h2>
                                    </c:if>
                                    <c:if test="${hofProfile.enemyKilledPosition == 2}">
                                        <h2 style="font-size: 12px;width: 11px;height: 10px;float: left;margin-left: 2px;margin-top: 3px;">
                                            nd</h2>
                                    </c:if>
                                    <c:if test="${hofProfile.enemyKilledPosition == 3}">
                                        <h2 style="font-size: 12px;width: 11px;height: 11px;float: left;margin-left: 2px;margin-top: 2px;">
                                            rd</h2>
                                    </c:if>
                                    <c:if test="${hofProfile.enemyKilledPosition == 4 || hofProfile.enemyKilledPosition == 5}">
                                        <h2 style="font-size: 12px;width: 11px;height: 11px;float: left;margin-left: 2px;margin-top: 2px;">
                                            th</h2>
                                    </c:if>
                                    <p style="width: 90px;float: right;margin-top: 16px;text-align: center;">on Troops
                                        Killed</p>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:forEach>
        </div>

    </div>
</div>
