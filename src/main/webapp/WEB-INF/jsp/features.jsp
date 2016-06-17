<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src='http://static.eaw1805.com/js/slideshow/slides.jquery.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/slideshow/slides.min.jquery.js'></script>
<script type="text/javascript" src='http://static.eaw1805.com/js/jquery.transform.js'></script>

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

    .noScrollBars {
        overflow: hidden !important;
        cursor: crosshair;
    }

</style>

<script>
$(function () {
//        animateMovement();
    animateTrade();
});

function animatePolitics() {
    $("#slide3").css({marginLeft:"-326px", marginTop:"-71px", transform:'scale(0.5,0.5)'});
    $("#relationsorder").css({display:"none"});
    $("#politicsButton").css({display:"none"});
    $("#animCursor3").css({left:"559px", top:"215px"});
    var scale = 0.9;
    var marginTopToGo = -449;
    var marginLeftToGo = -233;
    $("#slide3").animate({
        marginTop:marginTopToGo + "px",
        marginLeft:marginLeftToGo + "px",
        transform:'scale(' + scale + ',' + scale + ')'
    }, 2000);
    $("#animCursor3").animate({
        top:"902px",
        left:"686px"
    }, 2000, function () {
        $("#politicsButton").css({display:""});
        $("#relationsPanel").css({display:""});
        scale = 0.5;
        marginTopToGo = -64;
        marginLeftToGo = -329;
        $("#slide3").animate({
            marginTop:marginTopToGo + "px",
            marginLeft:marginLeftToGo + "px",
            transform:'scale(' + scale + ',' + scale + ')'
        }, 2000, function () {
            scale = 0.8;
            marginTopToGo = -64;
            marginLeftToGo = -461;
            $("#slide3").animate({
                marginTop:marginTopToGo + "px",
                marginLeft:marginLeftToGo + "px",
                transform:'scale(' + scale + ',' + scale + ')'
            }, 2000);
            $("#animCursor3").animate({
                top:"400px",
                left:"1294px"
            }, 2000, function () {
                $("#relationsPanel").css({display:"none"});
                $("#relationsPanel2").css({display:""});
                $("#animCursor3").animate({
                    top:"177px",
                    left:"1300px"
                }, 2000, function () {
                    $("#relationsPanel2").css({display:"none"});
                    $("#relationsorder").css({display:""});
                    scale = 2.0;
                    marginTopToGo = -917;
                    marginLeftToGo = -1645;
                    $("#slide3").animate({
                        marginTop:marginTopToGo + "px",
                        marginLeft:marginLeftToGo + "px",
                        transform:'scale(' + scale + ',' + scale + ')'
                    }, 2000, function () {
                        animateTrade();
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
    $("#slide4").animate({
        marginTop:marginTopToGo + "px",
        marginLeft:marginLeftToGo + "px",
        transform:'scale(' + scale + ',' + scale + ')'
    }, 2000);
    $("#animCursor4").animate({
        top:"415px",
        left:"884px"
    }, 2000, function () {
        $("#formcorpsarmypopup").css({display:""});
        $("#animCursor4").animate({
            top:"429px",
            left:"894px"
        }, 1500, function () {
            $("#formcorpsarmypopup").css({display:"none"});
            $("#formcorpspanel").css({display:""});
            scale = 0.6;
            marginTopToGo = -81;
            marginLeftToGo = -361;
            $("#slide4").animate({
                marginTop:marginTopToGo + "px",
                marginLeft:marginLeftToGo + "px",
                transform:'scale(' + scale + ',' + scale + ')'
            }, 2000, function () {
                $("#animCursor4").animate({
                    top:"677px",
                    left:"494px"
                }, 2000, function () {
                    $("#formcorpspanel").css({display:"none"});
                    $("#formcorpspanel2").css({display:""});
                    $("#animCursor4").animate({
                        top:"513px",
                        left:"494px"
                    }, 2000, function () {
                        $("#formcorpspanel2").css({display:"none"});
                        $("#formcorpspanel3").css({display:""});
                        $("#animCursor4").animate({
                            top:"330px",
                            left:"494px"
                        }, 2000, function () {
                            $("#formcorpspanel3").css({display:""});
                            $("#formcorpspanel4").css({display:""});
                            $("#animCursor4").animate({
                                top:"255px",
                                left:"1293px"
                            }, 2000, function () {
                                $("#formcorpspanel4").css({display:"none"});
                                $("#formcorpspanel5").css({display:""});
                                animatePolitics();
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
    $("#slide2").animate({
        marginTop:marginTopToGo + "px",
        marginLeft:marginLeftToGo + "px",
        transform:'scale(' + scale + ',' + scale + ')'
    }, 2000, function () {
        $("#animCursor2").animate({
            top:"433px",
            left:"1027px"
        }, 2000, function () {
            $("#selectArmies").css("display", "");
            $("#animCursor2").animate({
                top:"463px",
                left:"1090px"
            }, 2000, function () {
                $("#selectArmies").css("display", "none");
                $("#armyMenu").css("display", "");
                $("#animCursor2").animate({
                    top:"380px",
                    left:"1012px"
                }, 2000, function () {
                    $("#armyMenu").css("display", "none");
                    $("#animCursor2").animate({
                        top:"500px",
                        left:"741px"
                    }, 2000, function () {
                        $("#aftermovement").css("display", "");
                        setTimeout(function () {
//                                $("#slide1").css("display", "");
//                                $("#slide2").css("display", "none");
                            animateForm();
                        }, 2000);
                    });
                });
            });
        });
    });

}

function animateTrade() {
    $("#slide1").css({marginLeft:"-328px", marginTop:"-392px", transform:'scale(1,1)'});
    $("#tradeButton").css({"display":"none"});
    $("#tradePanel").css({"display":"none"});
    $("#tradePanel2").css({"display":"none"});
    $("#tradePanel3").css({"display":"none"});
    $("#tradePanel4").css({"display":"none"});
    $("#tradePanel5").css({"display":"none"});
    $("#tradePanel6").css({"display":"none"});
    $("#tradePanel7").css({"display":"none"});
    $("#tradePanel8").css({"display":"none"});

    $("#slide1").animate({
        marginTop:"-545px"
    }, 2000, function () {

//            $("#slide1").css({"zoom": 0.10});
//            $("#slide1").zoomTarget();
    });

    $("#animCursor").animate({
        top:"928px",
        left:"730px"
    }, 2000, function () {
//            var marginTopToGo = $("slide1").css("marginTop");
//            var marginLeftToGo = $("slide1").css("marginLeft");
        var scale = 0.58;
        var marginTopToGo = -155;
        var marginLeftToGo = -328;
        $("#tradeButton").css({"display":""});
        $("#tradePanel").css({"display":""});
        $("#slide1").animate({
            marginTop:marginTopToGo + "px",
            marginLeft:marginLeftToGo + "px",
            transform:'scale(' + scale + ',' + scale + ')'
        }, 2000, function () {
            $("#animCursor").animate({
                top:"779px",
                left:"1470px"
            }, 2000, function () {
                $("#tradePanel").css({"display":"none"});
                $("#tradePanel2").css({"display":""});
                $("#animCursor").animate({
                    top:"379px",
                    left:"470px"
                }, 2000, function () {
                    $("#tradePanel2").css({"display":"none"});
                    $("#tradePanel3").css({"display":""});
                    $("#animCursor").animate({
                        top:"474px",
                        left:"1203px"
                    }, 2000, function () {
                        $("#tradePanel3").css({"display":"none"});
                        $("#tradePanel4").css({"display":""});
                        $("#animCursor").animate({
                            top:"610px",
                            left:"888px"
                        }, 2000, function () {
                            scale = 0.88;
                            $("#tradePanel4").css({"display":"none"});
                            $("#tradePanel5").css({"display":""});
                            $("#slide1").animate({
                                transform:'scale(' + scale + ',' + scale + ')'
                            }, 2000);
                            $("#animCursor").animate({
                                top:"513px",
                                left:"658px"
                            }, 2000, function () {
                                $("#tradePanel5").css({"display":"none"});
                                $("#tradePanel6").css({"display":""});

                                var marginTopToGo = -313;
                                var marginLeftToGo = -378;
                                $("#slide1").animate({
                                    marginTop:marginTopToGo + "px",
                                    marginLeft:marginLeftToGo + "px"
                                }, 2000);
                                $("#animCursor").animate({
                                    top:"694px",
                                    left:"825px"
                                }, 2000, function () {
                                    $("#tradePanel6").css({"display":"none"});
                                    $("#tradePanel7").css({"display":""});
                                    $("#animCursor").animate({
                                        top:"759px",
                                        left:"1093px"
                                    }, 2000, function () {
                                        $("#tradePanel7").css({"display":"none"});
                                        $("#tradePanel8").css({"display":""});
                                        setTimeout(function () {
//                                                $("#slide2").css("display", "");
//                                                $("#slide1").css("display", "none");
                                            animateMovement();

                                        }, 2000);
                                    });
                                });
                            });
//                                $("#tradePanel3").css({"display": "none"});
//                                $("#tradePanel4").css({"display": ""});
                        });
                    });
                });
            });
        });
    });
}


</script>

<div style=" position: relative;margin: 45px 0px 0px -35px;width: 1000px;min-height: 514px;padding: 0px 40px;
background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">

<div id="anim_container" style="clear: both;width: 945px; height: 400px; overflow: hidden;">
<%--onmouseover="$('.prev').show();$('.next').show();"--%>
<%--onmouseout="$('.prev').hide();$('.next').hide();">--%>
<div id="anim_slides">
<div id="slide1" style="margin-left: -328px;margin-top: -392px;">
<%--<c:forEach var="article" items="${news}">--%>
<div class="slide" style="width: 985px;position: relative;">
<img src="http://static.eaw1805.com/images/animation/economybackground.jpg" alt="bg"
     style="position: absolute;left: 10px;top: 10px;">
<img id="tradeButton" src="http://static.eaw1805.com/images/layout/buttons/ButTradingCitiesOn.png"
     class="pointer" id="widget-98" title="Perform Trade"
     style="width: 57px; height: 23px; position: absolute; left: 718px; top: 917px;display: none">

<div id="tradePanel"
     style="display:none; position: absolute; overflow: hidden; width: 1218px; height: 653px; left: 295px; top: 228px;"
     class="tradePanel"><img src="http://static.eaw1805.com/images/buttons/ButAcceptOff.png"
                             class="gwt-Image" id="widget-909"
                             style="position: absolute; left: 1162px; top: 47px; width: 35px; height: 35px;">

<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 94px; top: 53px;">
    <img class="pointer" id="widget-910"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOn.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 1
    </div>
</div>
<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 916px; top: 53px;">
    <img class="pointer" id="widget-911"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 2
    </div>
</div>
<div style="position: absolute; overflow: hidden; left: 0px; top: 83px; width: 1158px; height: 603px;">
<div style="position: absolute; overflow: hidden; width: 1148px; height: 560px; left: 10px; top: 0px;">
<img src="http://static.eaw1805.com/images/panels/trade/tradeCity.png" class="gwt-Image"
     style="width: 100%;">
<table cellspacing="0" cellpadding="0"
       style="width: 1107px; height: 509px; position: absolute; left: 23px; top: 41px;">
<tbody>
<tr>
<td align="left" style="vertical-align: top;">
<div style="overflow: auto; position: relative; width: 1091px; height: 509px;"
     class="noScrollBars">
<div style="position: relative;">
<table cellspacing="0" cellpadding="0"
       style="width: 1082px; height: 30px;">
<tbody>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Amsterdam
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                4,260,311
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses02.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric04.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool04.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Berlin
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                1,826,654
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood04.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone03.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood03.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre01.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses04.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric03.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool03.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious01.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial01.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Alexandria
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                4,542,814
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood03.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre02.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems04.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses04.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric03.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool01.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine01.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial02.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Constantinople
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                1,773,178
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre04.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems02.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses03.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool01.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious04.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial02.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Copenhagen
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                1,936,493
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial01.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone02.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses03.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric03.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool03.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious01.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial02.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Stockholm
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                2,946,655
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood04.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone03.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood03.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre01.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses04.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric03.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool03.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious01.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial01.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Lisbon
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                3,670,115
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial01.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone02.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses03.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric03.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool03.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious01.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial02.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                London
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                4,341,341
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood04.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone02.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood02.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre01.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems04.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses03.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric04.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool04.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine04.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial02.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Madrid
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                2,093,897
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial01.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone02.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses03.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric03.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool03.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious01.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial02.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Moscow
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                4,659,327
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone03.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood02.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre01.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems02.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses04.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric02.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool02.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious02.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine02.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial03.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Munich
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                3,379,846
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses02.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric04.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool04.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Naples
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                2,163,169
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses02.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric04.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool04.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Paris
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                3,379,846
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses02.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric04.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool04.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Petersburg
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                3,004,823
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone03.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood02.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre01.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems02.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses04.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric02.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool02.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious02.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine02.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial03.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Rabat
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                1,619,186
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial01.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood04.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood02.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre01.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems03.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses02.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric02.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool01.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious02.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine02.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial01.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Rome
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                2,025,269
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses02.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric04.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool04.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Tunis
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                3,454,177
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial01.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood04.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood02.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre01.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems03.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses02.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric02.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool01.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious02.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine02.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial01.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Vienna
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                1,917,931
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial01.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone02.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre04.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses02.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric02.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious02.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine01.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial02.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Warsaw
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                4,760,003
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood02.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone03.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems02.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses01.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric02.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool03.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial03.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Mecca
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                1,263,326
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood02.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses03.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric04.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine04.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial01.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                New York
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                2,171,664
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial01.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood02.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone02.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood02.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems04.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses03.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric01.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool01.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial03.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Shangai
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                3,521,432
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial01.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood04.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems03.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses03.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric02.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool04.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious02.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine01.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial03.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Belem
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                3,165,032
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial04.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood04.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone01.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood03.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre02.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses04.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric01.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool02.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious02.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial01.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Cartagena
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                3,239,292
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial01.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses03.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric02.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool01.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious01.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine02.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial04.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Martinique
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                1,737,769
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial02.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood04.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre01.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems04.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric03.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool01.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial03.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                New Orleans
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                3,666,963
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial01.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood02.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone01.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre01.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems03.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses04.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric01.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool02.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial02.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Santiago de Cuba
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                4,302,397
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial04.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood04.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone01.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood03.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre02.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses04.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric01.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool02.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious02.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial01.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Vera Cruz
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                4,302,397
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial04.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood04.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone01.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood03.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre02.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses04.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric01.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool02.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious02.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial01.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Calcutta
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                2,778,377
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial01.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone03.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood03.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses04.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric02.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool01.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial02.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Goa
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                2,764,874
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood02.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone01.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre02.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses01.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool02.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious01.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial02.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Haiphong
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                2,844,633
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial01.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood02.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone02.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood04.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre02.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems04.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses02.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric02.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Padany
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                1,822,079
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood03.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre02.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems03.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses01.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric01.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool01.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious04.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine01.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial04.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Saigon
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                2,801,078
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood02.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone01.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre02.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses01.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool02.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious01.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial02.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Trincomale
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                4,301,964
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial01.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone03.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood03.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses04.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric02.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool01.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial02.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Luanda
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                4,130,189
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial04.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood03.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone03.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood02.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre02.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool02.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine01.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial01.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Cape Town
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                4,308,538
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood04.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone03.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood03.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre01.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems02.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses04.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric01.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious01.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial03.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; height: 30px;">
            <div class="clearFont"
                 style="position: absolute; left: 0px; top: 7px; width: 107px; height: 15px;">
                Mozambique
            </div>
            <div class="clearFont textRight"
                 style="position: absolute; left: 130px; top: 7px; width: 161px; height: 15px;">
                1,559,839
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial02.png"
                 class="gwt-Image"
                 style="position: absolute; left: 306px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                class="gwt-Image"
                style="position: absolute; left: 370px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone01.png"
                class="gwt-Image"
                style="position: absolute; left: 434px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood03.png"
                class="gwt-Image"
                style="position: absolute; left: 498px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre02.png"
                class="gwt-Image"
                style="position: absolute; left: 562px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems04.png"
                class="gwt-Image"
                style="position: absolute; left: 626px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses04.png"
                class="gwt-Image"
                style="position: absolute; left: 690px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric02.png"
                class="gwt-Image"
                style="position: absolute; left: 754px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool04.png"
                class="gwt-Image"
                style="position: absolute; left: 818px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                class="gwt-Image"
                style="position: absolute; left: 882px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine01.png"
                class="gwt-Image"
                style="position: absolute; left: 946px; top: 3px; width: 60px; height: 24px;"><img
                src="http://static.eaw1805.com/images/panels/trade/buttons/CLRColonial03.png"
                class="gwt-Image"
                style="position: absolute; left: 1010px; top: 3px; width: 60px; height: 24px;">
        </div>
    </td>
</tr>
</tbody>
</table>
</div>
</div>
</td>
<td align="left" style="vertical-align: top;">
    <table cellspacing="0" cellpadding="0">
        <tbody>
        <tr>
            <td align="left" style="vertical-align: top;"><img
                    src="http://static.eaw1805.com/images/panels/trade/ButZoomInOff.png"
                    class="gwt-Image" id="widget-920"></td>
        </tr>
        <tr>
            <td align="left" style="vertical-align: top;">
                <div style="position: relative; overflow: hidden; width: 16px; height: 481px;">
                    <img src="http://static.eaw1805.com/images/panels/trade/barVertical.png"
                         class="gwt-Image"
                         style="width: 5px; height: 481px; position: absolute; left: 6px; top: 0px;"><img
                        src="http://static.eaw1805.com/images/panels/trade/ScrollerVertical.png"
                        class="gwt-Image"
                        style="height: 31px; position: absolute; left: 0px; top: 0px;">
                </div>
            </td>
        </tr>
        <tr>
            <td align="left" style="vertical-align: top;"><img
                    src="http://static.eaw1805.com/images/panels/trade/ButZoomOutOff.png"
                    class="gwt-Image" id="widget-921"></td>
        </tr>
        </tbody>
    </table>
</td>
</tr>
</tbody>
</table>
<div class="clearFont-large whiteText"
     style="position: absolute; left: 201px; top: 5px;">Wealth
</div>
<div class="clearFont-large whiteText"
     style="position: absolute; left: 54px; top: 5px;">City
</div>
</div>
</div>
<img src="http://static.eaw1805.com/images/panels/trade/buttons/ButTradeCitiesOn.png"
     class="pointer" id="widget-912"
     style="position: absolute; left: 1164px; top: 86px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButBaggageTrainsOff.png"
        class="pointer" id="widget-913"
        style="position: absolute; left: 1164px; top: 275px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButMerchantShipsOff.png"
        class="pointer" id="widget-914"
        style="position: absolute; left: 1164px; top: 469px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOff.png"
        class="pointer" id="widget-915" title="Select european theater"
        style="position: absolute; left: 482px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png"
        class="pointer" id="widget-916" title="Select african theater"
        style="position: absolute; left: 540px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png"
        class="pointer" id="widget-917" title="Select carribean theater"
        style="position: absolute; left: 598px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png"
        class="pointer" id="widget-918" title="Select india theater"
        style="position: absolute; left: 655px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/buttons/ButTradeCityOn.png" class="pointer"
        id="widget-919" title="Browse trade cities"
        style="position: absolute; left: 713px; top: 44px; width: 31px; height: 31px;"></div>
<div id="tradePanel2"
     style="display:none; position: absolute; overflow: hidden; width: 1218px; height: 653px; left: 295px; top: 228px;"
     class="tradePanel"><img src="http://static.eaw1805.com/images/buttons/ButAcceptOff.png"
                             class="gwt-Image" id="widget-909"
                             style="position: absolute; left: 1162px; top: 47px; width: 35px; height: 35px;">

<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 94px; top: 53px;">
    <img class="pointer" id="widget-910"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOn.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 1
    </div>
</div>
<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 916px; top: 53px;">
    <img class="pointer" id="widget-911"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 2
    </div>
</div>
<div style="position: absolute; overflow: hidden; left: 0px; top: 83px; width: 1158px; height: 603px;">
<div style="position: absolute; overflow: hidden; width: 1148px; height: 560px; left: 10px; top: 0px;"
     class="startTradeTab">
<table cellspacing="0" cellpadding="0"
       style="width: 323px; height: 529px; position: absolute; left: 14px; top: 16px;">
    <tbody>
    <tr>
        <td align="left" style="vertical-align: top;">
            <div style="overflow: auto; position: relative; width: 307px; height: 529px;"
                 class="noScrollBars">
                <div style="position: relative;">
                    <table cellspacing="0" cellpadding="0"
                           style="width: 100%; height: 0px;">
                        <tbody>
                        <tr>
                            <td align="left" style="vertical-align: top;">
                                <table cellspacing="0" cellpadding="0"
                                       style="width: 297px; height: 106px;">
                                    <tbody>
                                    <tr>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 297px; height: 106px;"
                                                 class="tradeUnit pointer"><img
                                                    src="http://static.eaw1805.com/images/buttons/icons/baggage.png"
                                                    class="gwt-Image"
                                                    style="width: 65px; height: 65px; position: absolute; left: 0px; top: 0px;">

                                                <div class="clearFont"
                                                     style="width: 250px; height: 19px; position: absolute; left: 71px; top: 46px;">
                                                    Type: Baggage train.
                                                </div>
                                                <div class="clearFontMiniTitle"
                                                     style="width: 193px; height: 26px; position: absolute; left: 71px; top: 0px;">
                                                    BaggageTrain 1
                                                </div>
                                                <img src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                                                     class="gwt-Image"
                                                     title="Unit is currently in a trade city."
                                                     style="position: absolute; left: 269px; top: 18px; width: 24px; height: 24px;">

                                                <div class="clearFont"
                                                     style="text-align: left; width: 199px; height: 15px; position: absolute; left: 1px; top: 67px;">
                                                    Weight : 0 / 1500
                                                </div>
                                                <div style="position: absolute; overflow: hidden; width: 300px; height: 35px; left: 1px; top: 87px;">
                                                    <img src="http://static.eaw1805.com/images/goods/good-1.png"
                                                         class="gwt-Image"
                                                         style="position: absolute; left: 0px; top: 0px; width: 15px; height: 15px;"
                                                         title="Money:1055993"></div>
                                                <div class="clearFontSmall"
                                                     style="position: absolute; left: 252px; top: 2px;">
                                                    E28/20
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="left" style="vertical-align: top;">
                                <table cellspacing="0" cellpadding="0"
                                       style="width: 297px; height: 106px;">
                                    <tbody>
                                    <tr>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 297px; height: 106px;"
                                                 class="tradeUnit pointer"><img
                                                    src="http://static.eaw1805.com/images/buttons/icons/baggage.png"
                                                    class="gwt-Image"
                                                    style="width: 65px; height: 65px; position: absolute; left: 0px; top: 0px;">

                                                <div class="clearFont"
                                                     style="width: 250px; height: 19px; position: absolute; left: 71px; top: 46px;">
                                                    Type: Baggage train.
                                                </div>
                                                <div class="clearFontMiniTitle"
                                                     style="width: 193px; height: 26px; position: absolute; left: 71px; top: 0px;">
                                                    BaggageTrain 2
                                                </div>
                                                <img src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                                                     class="gwt-Image"
                                                     title="Unit is currently in a trade city."
                                                     style="position: absolute; left: 269px; top: 18px; width: 24px; height: 24px;">

                                                <div class="clearFont"
                                                     style="text-align: left; width: 199px; height: 15px; position: absolute; left: 1px; top: 67px;">
                                                    Weight : 18 / 1500
                                                </div>
                                                <div style="position: absolute; overflow: hidden; width: 300px; height: 35px; left: 1px; top: 87px;">
                                                    <img src="http://static.eaw1805.com/images/goods/good-2.png"
                                                         class="gwt-Image"
                                                         style="position: absolute; left: 0px; top: 0px; width: 15px; height: 15px;"
                                                         title="Citizens:188"></div>
                                                <div class="clearFontSmall"
                                                     style="position: absolute; left: 252px; top: 2px;">
                                                    E23/27
                                                </div>
                                            </div>
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
        </td>
        <td align="left" style="vertical-align: top;">
            <table cellspacing="0" cellpadding="0">
                <tbody>
                <tr>
                    <td align="left" style="vertical-align: top;"><img
                            src="http://static.eaw1805.com/images/panels/trade/ButZoomInOff.png"
                            class="gwt-Image" id="widget-927"></td>
                </tr>
                <tr>
                    <td align="left" style="vertical-align: top;">
                        <div style="position: relative; overflow: hidden; width: 16px; height: 501px;">
                            <img src="http://static.eaw1805.com/images/panels/trade/barVertical.png"
                                 class="gwt-Image"
                                 style="width: 5px; height: 501px; position: absolute; left: 6px; top: 0px;"><img
                                src="http://static.eaw1805.com/images/panels/trade/ScrollerVertical.png"
                                class="gwt-Image"
                                style="height: 31px; position: absolute; left: 0px; top: 0px;">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="left" style="vertical-align: top;"><img
                            src="http://static.eaw1805.com/images/panels/trade/ButZoomOutOff.png"
                            class="gwt-Image" id="widget-928"></td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 323px; height: 529px; position: absolute; left: 814px; top: 16px;">
    <tbody>
    <tr>
        <td align="left" style="vertical-align: top;">
            <div style="overflow: auto; position: relative; width: 307px; height: 529px;"
                 class="noScrollBars">
                <div style="position: relative;">
                    <table cellspacing="0" cellpadding="0"
                           style="width: 100%; height: 0px;">
                        <tbody>
                        <tr>
                            <td align="center" style="vertical-align: middle;">
                                <div class="clearFont-large"
                                     style="text-align: center; width: 153px;">Select a
                                    unit from the right panel
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </td>
        <td align="left" style="vertical-align: top;">
            <table cellspacing="0" cellpadding="0">
                <tbody>
                <tr>
                    <td align="left" style="vertical-align: top;"><img
                            src="http://static.eaw1805.com/images/panels/trade/ButZoomInOff.png"
                            class="gwt-Image" id="widget-929"></td>
                </tr>
                <tr>
                    <td align="left" style="vertical-align: top;">
                        <div style="position: relative; overflow: hidden; width: 16px; height: 501px;">
                            <img src="http://static.eaw1805.com/images/panels/trade/barVertical.png"
                                 class="gwt-Image"
                                 style="width: 5px; height: 501px; position: absolute; left: 6px; top: 0px;"><img
                                src="http://static.eaw1805.com/images/panels/trade/ScrollerVertical.png"
                                class="gwt-Image"
                                style="height: 31px; position: absolute; left: 0px; top: 0px;">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="left" style="vertical-align: top;"><img
                            src="http://static.eaw1805.com/images/panels/trade/ButZoomOutOff.png"
                            class="gwt-Image" id="widget-930"></td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
<div style="position: absolute; overflow: hidden; width: 322px; height: 126px; left: 429px; top: 76px;"
     class="tradeUnitSelected">
    <div class="clearFontMiniTitle"
         style="text-align: center; width: 308px; height: 25px; position: absolute; left: 7px; top: 50px;">
        First trade unit selection
    </div>
</div>
<div style="position: absolute; overflow: hidden; width: 322px; height: 126px; left: 429px; top: 399px;"
     class="tradeUnitSelected">
    <div class="clearFontMiniTitle"
         style="text-align: center; width: 308px; height: 25px; position: absolute; left: 7px; top: 50px;">
        Second trade unit selection
    </div>
</div>
<img src="http://static.eaw1805.com/images/layout/buttons/ButTradingCitiesOff.png"
     class="pointer" id="widget-931"
     style="width: 160px; height: 45px; position: absolute; left: 510px; top: 278px;">
</div>
</div>
<img src="http://static.eaw1805.com/images/panels/trade/buttons/ButTradeCitiesOff.png"
     class="pointer" id="widget-912"
     style="position: absolute; left: 1164px; top: 86px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButBaggageTrainsOn.png"
        class="pointer" id="widget-913"
        style="position: absolute; left: 1164px; top: 275px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButMerchantShipsOff.png"
        class="pointer" id="widget-914"
        style="position: absolute; left: 1164px; top: 469px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOn.png"
        class="pointer" id="widget-915" title="Select european theater"
        style="position: absolute; left: 482px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png"
        class="pointer" id="widget-916" title="Select african theater"
        style="position: absolute; left: 540px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png"
        class="pointer" id="widget-917" title="Select carribean theater"
        style="position: absolute; left: 598px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png"
        class="pointer" id="widget-918" title="Select india theater"
        style="position: absolute; left: 655px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/buttons/ButTradeCityOff.png" class="pointer"
        id="widget-919" title="Browse trade cities"
        style="position: absolute; left: 713px; top: 44px; width: 31px; height: 31px;">
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
</div>
<div id="tradePanel3"
     style="display:none; position: absolute; overflow: hidden; width: 1218px; height: 653px; left: 295px; top: 228px;"
     class="tradePanel"><img src="http://static.eaw1805.com/images/buttons/ButAcceptOff.png"
                             class="gwt-Image" id="widget-909"
                             style="position: absolute; left: 1162px; top: 47px; width: 35px; height: 35px;">

<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 94px; top: 53px;">
    <img class="pointer" id="widget-910"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOn.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 1
    </div>
</div>
<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 916px; top: 53px;">
    <img class="pointer" id="widget-911"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 2
    </div>
</div>
<div style="position: absolute; overflow: hidden; left: 0px; top: 83px; width: 1158px; height: 603px;">
<div style="position: absolute; overflow: hidden; width: 1148px; height: 560px; left: 10px; top: 0px;"
     class="startTradeTab">
<table cellspacing="0" cellpadding="0"
       style="width: 323px; height: 529px; position: absolute; left: 14px; top: 16px;">
    <tbody>
    <tr>
        <td align="left" style="vertical-align: top;">
            <div style="overflow: auto; position: relative; width: 307px; height: 529px;"
                 class="noScrollBars">
                <div style="position: relative;">
                    <table cellspacing="0" cellpadding="0"
                           style="width: 100%; height: 0px;">
                        <tbody>
                        <tr>
                            <td align="left" style="vertical-align: top;">
                                <table cellspacing="0" cellpadding="0"
                                       style="width: 297px; height: 106px;">
                                    <tbody>
                                    <tr>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 297px; height: 106px;"
                                                 class="tradeUnitSel pointer"><img
                                                    src="http://static.eaw1805.com/images/buttons/icons/baggage.png"
                                                    class="gwt-Image"
                                                    style="width: 65px; height: 65px; position: absolute; left: 0px; top: 0px;">

                                                <div class="clearFont"
                                                     style="width: 250px; height: 19px; position: absolute; left: 71px; top: 46px;">
                                                    Type: Baggage train.
                                                </div>
                                                <div class="clearFontMiniTitle"
                                                     style="width: 193px; height: 26px; position: absolute; left: 71px; top: 0px;">
                                                    BaggageTrain 1
                                                </div>
                                                <img src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                                                     class="gwt-Image"
                                                     title="Unit is currently in a trade city."
                                                     style="position: absolute; left: 269px; top: 18px; width: 24px; height: 24px;">

                                                <div class="clearFont"
                                                     style="text-align: left; width: 199px; height: 15px; position: absolute; left: 1px; top: 67px;">
                                                    Weight : 0 / 1500
                                                </div>
                                                <div style="position: absolute; overflow: hidden; width: 300px; height: 35px; left: 1px; top: 87px;">
                                                    <img src="http://static.eaw1805.com/images/goods/good-1.png"
                                                         class="gwt-Image"
                                                         style="position: absolute; left: 0px; top: 0px; width: 15px; height: 15px;"
                                                         title="Money:1055993"></div>
                                                <div class="clearFontSmall"
                                                     style="position: absolute; left: 252px; top: 2px;">
                                                    E28/20
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="left" style="vertical-align: top;">
                                <table cellspacing="0" cellpadding="0"
                                       style="width: 297px; height: 106px;">
                                    <tbody>
                                    <tr>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 297px; height: 106px;"
                                                 class="tradeUnit pointer"><img
                                                    src="http://static.eaw1805.com/images/buttons/icons/baggage.png"
                                                    class="gwt-Image"
                                                    style="width: 65px; height: 65px; position: absolute; left: 0px; top: 0px;">

                                                <div class="clearFont"
                                                     style="width: 250px; height: 19px; position: absolute; left: 71px; top: 46px;">
                                                    Type: Baggage train.
                                                </div>
                                                <div class="clearFontMiniTitle"
                                                     style="width: 193px; height: 26px; position: absolute; left: 71px; top: 0px;">
                                                    BaggageTrain 2
                                                </div>
                                                <img src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                                                     class="gwt-Image"
                                                     title="Unit is currently in a trade city."
                                                     style="position: absolute; left: 269px; top: 18px; width: 24px; height: 24px;">

                                                <div class="clearFont"
                                                     style="text-align: left; width: 199px; height: 15px; position: absolute; left: 1px; top: 67px;">
                                                    Weight : 18 / 1500
                                                </div>
                                                <div style="position: absolute; overflow: hidden; width: 300px; height: 35px; left: 1px; top: 87px;">
                                                    <img src="http://static.eaw1805.com/images/goods/good-2.png"
                                                         class="gwt-Image"
                                                         style="position: absolute; left: 0px; top: 0px; width: 15px; height: 15px;"
                                                         title="Citizens:188"></div>
                                                <div class="clearFontSmall"
                                                     style="position: absolute; left: 252px; top: 2px;">
                                                    E23/27
                                                </div>
                                            </div>
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
        </td>
        <td align="left" style="vertical-align: top;">
            <table cellspacing="0" cellpadding="0">
                <tbody>
                <tr>
                    <td align="left" style="vertical-align: top;"><img
                            src="http://static.eaw1805.com/images/panels/trade/ButZoomInOff.png"
                            class="gwt-Image" id="widget-927"></td>
                </tr>
                <tr>
                    <td align="left" style="vertical-align: top;">
                        <div style="position: relative; overflow: hidden; width: 16px; height: 501px;">
                            <img src="http://static.eaw1805.com/images/panels/trade/barVertical.png"
                                 class="gwt-Image"
                                 style="width: 5px; height: 501px; position: absolute; left: 6px; top: 0px;"><img
                                src="http://static.eaw1805.com/images/panels/trade/ScrollerVertical.png"
                                class="gwt-Image"
                                style="height: 31px; position: absolute; left: 0px; top: 0px;">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="left" style="vertical-align: top;"><img
                            src="http://static.eaw1805.com/images/panels/trade/ButZoomOutOff.png"
                            class="gwt-Image" id="widget-928"></td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 323px; height: 529px; position: absolute; left: 814px; top: 16px;">
    <tbody>
    <tr>
        <td align="left" style="vertical-align: top;">
            <div style="overflow: auto; position: relative; width: 307px; height: 529px;"
                 class="noScrollBars">
                <div style="position: relative;">
                    <table cellspacing="0" cellpadding="0"
                           style="width: 100%; height: 0px;">
                        <tbody>
                        <tr>
                            <td align="center" style="vertical-align: middle;">
                                <table cellspacing="0" cellpadding="0"
                                       style="width: 297px; height: 106px;">
                                    <tbody>
                                    <tr>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 297px; height: 106px;"
                                                 class="tradeUnit pointer"><img
                                                    src="http://static.eaw1805.com/tiles/sites/tprod-12.png"
                                                    class="gwt-Image"
                                                    style="width: 65px; height: 65px; position: absolute; left: 0px; top: 0px;">

                                                <div class="clearFont"
                                                     style="width: 250px; height: 19px; position: absolute; left: 71px; top: 46px;">
                                                    Regional warehouse
                                                </div>
                                                <div class="clearFontMiniTitle"
                                                     style="width: 193px; height: 26px; position: absolute; left: 71px; top: 0px;">
                                                    France's Warehouse
                                                </div>
                                                <img src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                                                     class="gwt-Image"
                                                     title="Unit is currently in a trade city."
                                                     style="position: absolute; left: 269px; top: 18px; width: 24px; height: 24px;">

                                                <div class="clearFont"
                                                     style="text-align: left; width: 199px; height: 15px; position: absolute; left: 1px; top: 67px;">
                                                    Weight: Unlimited
                                                </div>
                                                <div style="position: absolute; overflow: hidden; width: 300px; height: 35px; left: 1px; top: 87px;"></div>
                                                <div class="clearFontSmall"
                                                     style="position: absolute; left: 252px; top: 2px;">
                                                    E28/20
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" style="vertical-align: middle;">
                                <table cellspacing="0" cellpadding="0"
                                       style="width: 297px; height: 106px;">
                                    <tbody>
                                    <tr>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 297px; height: 106px;"
                                                 class="tradeUnit pointer"><img
                                                    src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                                                    class="gwt-Image"
                                                    style="width: 65px; height: 65px; position: absolute; left: 0px; top: 0px;">

                                                <div class="clearFont"
                                                     style="width: 250px; height: 19px; position: absolute; left: 71px; top: 46px;">
                                                    Trade city
                                                </div>
                                                <div class="clearFontMiniTitle"
                                                     style="width: 193px; height: 26px; position: absolute; left: 71px; top: 0px;">
                                                    Amsterdam
                                                </div>
                                                <img src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                                                     class="gwt-Image"
                                                     title="Unit is currently in a trade city."
                                                     style="position: absolute; left: 269px; top: 18px; width: 24px; height: 24px;">

                                                <div class="clearFont"
                                                     style="text-align: left; width: 199px; height: 15px; position: absolute; left: 1px; top: 67px;">
                                                    Wealth: 4,260,311
                                                </div>
                                                <div style="position: absolute; overflow: hidden; width: 300px; height: 35px; left: 1px; top: 87px;"></div>
                                                <div class="clearFontSmall"
                                                     style="position: absolute; left: 252px; top: 2px;">
                                                    E28/20
                                                </div>
                                            </div>
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
        </td>
        <td align="left" style="vertical-align: top;">
            <table cellspacing="0" cellpadding="0">
                <tbody>
                <tr>
                    <td align="left" style="vertical-align: top;"><img
                            src="http://static.eaw1805.com/images/panels/trade/ButZoomInOff.png"
                            class="gwt-Image" id="widget-929"></td>
                </tr>
                <tr>
                    <td align="left" style="vertical-align: top;">
                        <div style="position: relative; overflow: hidden; width: 16px; height: 501px;">
                            <img src="http://static.eaw1805.com/images/panels/trade/barVertical.png"
                                 class="gwt-Image"
                                 style="width: 5px; height: 501px; position: absolute; left: 6px; top: 0px;"><img
                                src="http://static.eaw1805.com/images/panels/trade/ScrollerVertical.png"
                                class="gwt-Image"
                                style="height: 31px; position: absolute; left: 0px; top: 0px;">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="left" style="vertical-align: top;"><img
                            src="http://static.eaw1805.com/images/panels/trade/ButZoomOutOff.png"
                            class="gwt-Image" id="widget-930"></td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
<div style="position: absolute; overflow: hidden; width: 322px; height: 126px; left: 429px; top: 76px;"
     class="tradeUnitSelected">
    <table cellspacing="0" cellpadding="0"
           style="width: 297px; height: 106px; position: absolute; left: 8px; top: 7px;">
        <tbody>
        <tr>
            <td align="left" style="vertical-align: top;">
                <div style="position: relative; overflow: hidden; width: 297px; height: 106px;"
                     class="tradeUnitSel pointer"><img
                        src="http://static.eaw1805.com/images/buttons/icons/baggage.png"
                        class="gwt-Image"
                        style="width: 65px; height: 65px; position: absolute; left: 0px; top: 0px;">

                    <div class="clearFont"
                         style="width: 250px; height: 19px; position: absolute; left: 71px; top: 46px;">
                        Type: Baggage train.
                    </div>
                    <div class="clearFontMiniTitle"
                         style="width: 193px; height: 26px; position: absolute; left: 71px; top: 0px;">
                        BaggageTrain 1
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                         class="gwt-Image" title="Unit is currently in a trade city."
                         style="position: absolute; left: 269px; top: 18px; width: 24px; height: 24px;">

                    <div class="clearFont"
                         style="text-align: left; width: 199px; height: 15px; position: absolute; left: 1px; top: 67px;">
                        Weight : 0 / 1500
                    </div>
                    <div style="position: absolute; overflow: hidden; width: 300px; height: 35px; left: 1px; top: 87px;">
                        <img src="http://static.eaw1805.com/images/goods/good-1.png"
                             class="gwt-Image"
                             style="position: absolute; left: 0px; top: 0px; width: 15px; height: 15px;"
                             title="Money:1055993"></div>
                    <div class="clearFontSmall"
                         style="position: absolute; left: 252px; top: 2px;">E28/20
                    </div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div style="position: absolute; overflow: hidden; width: 322px; height: 126px; left: 429px; top: 399px;"
     class="tradeUnitSelected">
    <div class="clearFontMiniTitle"
         style="text-align: center; width: 308px; height: 25px; position: absolute; left: 7px; top: 50px;">
        Second trade unit selection
    </div>
</div>
<img src="http://static.eaw1805.com/images/layout/buttons/ButTradingCitiesOff.png"
     class="pointer" id="widget-931"
     style="width: 160px; height: 45px; position: absolute; left: 510px; top: 278px;">
</div>
</div>
<img src="http://static.eaw1805.com/images/panels/trade/buttons/ButTradeCitiesOff.png"
     class="pointer" id="widget-912"
     style="position: absolute; left: 1164px; top: 86px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButBaggageTrainsOn.png"
        class="pointer" id="widget-913"
        style="position: absolute; left: 1164px; top: 275px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButMerchantShipsOff.png"
        class="pointer" id="widget-914"
        style="position: absolute; left: 1164px; top: 469px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOn.png"
        class="pointer" id="widget-915" title="Select european theater"
        style="position: absolute; left: 482px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png"
        class="pointer" id="widget-916" title="Select african theater"
        style="position: absolute; left: 540px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png"
        class="pointer" id="widget-917" title="Select carribean theater"
        style="position: absolute; left: 598px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png"
        class="pointer" id="widget-918" title="Select india theater"
        style="position: absolute; left: 655px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/buttons/ButTradeCityOff.png" class="pointer"
        id="widget-919" title="Browse trade cities"
        style="position: absolute; left: 713px; top: 44px; width: 31px; height: 31px;">
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
</div>
<div id="tradePanel4"
     style="display:none; position: absolute; overflow: hidden; width: 1218px; height: 653px; left: 295px; top: 228px;"
     class="tradePanel"><img src="http://static.eaw1805.com/images/buttons/ButAcceptOff.png"
                             class="gwt-Image" id="widget-909"
                             style="position: absolute; left: 1162px; top: 47px; width: 35px; height: 35px;">

<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 94px; top: 53px;">
    <img class="pointer" id="widget-910"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOn.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 1
    </div>
</div>
<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 916px; top: 53px;">
    <img class="pointer" id="widget-911"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 2
    </div>
</div>
<div style="position: absolute; overflow: hidden; left: 0px; top: 83px; width: 1158px; height: 603px;">
<div style="overflow: hidden; width: 1148px; height: 560px; position: absolute; left: 10px; top: 0px;"
     class="startTradeTab">
<table cellspacing="0" cellpadding="0"
       style="width: 323px; height: 529px; position: absolute; left: 14px; top: 16px;">
    <tbody>
    <tr>
        <td align="left" style="vertical-align: top;">
            <div style="overflow: auto; position: relative; width: 307px; height: 529px;"
                 class="noScrollBars">
                <div style="position: relative;">
                    <table cellspacing="0" cellpadding="0"
                           style="width: 100%; height: 0px;">
                        <tbody>
                        <tr>
                            <td align="left" style="vertical-align: top;">
                                <table cellspacing="0" cellpadding="0"
                                       style="width: 297px; height: 106px;">
                                    <tbody>
                                    <tr>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 297px; height: 106px;"
                                                 class="tradeUnitSel pointer"><img
                                                    src="http://static.eaw1805.com/images/buttons/icons/baggage.png"
                                                    class="gwt-Image"
                                                    style="width: 65px; height: 65px; position: absolute; left: 0px; top: 0px;">

                                                <div class="clearFont"
                                                     style="width: 250px; height: 19px; position: absolute; left: 71px; top: 46px;">
                                                    Type: Baggage train.
                                                </div>
                                                <div class="clearFontMiniTitle"
                                                     style="width: 193px; height: 26px; position: absolute; left: 71px; top: 0px;">
                                                    BaggageTrain 1
                                                </div>
                                                <img src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                                                     class="gwt-Image"
                                                     title="Unit is currently in a trade city."
                                                     style="position: absolute; left: 269px; top: 18px; width: 24px; height: 24px;">

                                                <div class="clearFont"
                                                     style="text-align: left; width: 199px; height: 15px; position: absolute; left: 1px; top: 67px;">
                                                    Weight : 0 / 1500
                                                </div>
                                                <div style="position: absolute; overflow: hidden; width: 300px; height: 35px; left: 1px; top: 87px;">
                                                    <img src="http://static.eaw1805.com/images/goods/good-1.png"
                                                         class="gwt-Image"
                                                         style="position: absolute; left: 0px; top: 0px; width: 15px; height: 15px;"
                                                         title="Money:1055993"></div>
                                                <div class="clearFontSmall"
                                                     style="position: absolute; left: 252px; top: 2px;">
                                                    E28/20
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="left" style="vertical-align: top;">
                                <table cellspacing="0" cellpadding="0"
                                       style="width: 297px; height: 106px;">
                                    <tbody>
                                    <tr>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 297px; height: 106px;"
                                                 class="tradeUnit pointer"><img
                                                    src="http://static.eaw1805.com/images/buttons/icons/baggage.png"
                                                    class="gwt-Image"
                                                    style="width: 65px; height: 65px; position: absolute; left: 0px; top: 0px;">

                                                <div class="clearFont"
                                                     style="width: 250px; height: 19px; position: absolute; left: 71px; top: 46px;">
                                                    Type: Baggage train.
                                                </div>
                                                <div class="clearFontMiniTitle"
                                                     style="width: 193px; height: 26px; position: absolute; left: 71px; top: 0px;">
                                                    BaggageTrain 2
                                                </div>
                                                <img src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                                                     class="gwt-Image"
                                                     title="Unit is currently in a trade city."
                                                     style="position: absolute; left: 269px; top: 18px; width: 24px; height: 24px;">

                                                <div class="clearFont"
                                                     style="text-align: left; width: 199px; height: 15px; position: absolute; left: 1px; top: 67px;">
                                                    Weight : 18 / 1500
                                                </div>
                                                <div style="position: absolute; overflow: hidden; width: 300px; height: 35px; left: 1px; top: 87px;">
                                                    <img src="http://static.eaw1805.com/images/goods/good-2.png"
                                                         class="gwt-Image"
                                                         style="position: absolute; left: 0px; top: 0px; width: 15px; height: 15px;"
                                                         title="Citizens:188"></div>
                                                <div class="clearFontSmall"
                                                     style="position: absolute; left: 252px; top: 2px;">
                                                    E23/27
                                                </div>
                                            </div>
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
        </td>
        <td align="left" style="vertical-align: top;">
            <table cellspacing="0" cellpadding="0">
                <tbody>
                <tr>
                    <td align="left" style="vertical-align: top;"><img
                            src="http://static.eaw1805.com/images/panels/trade/ButZoomInOff.png"
                            class="gwt-Image" id="widget-927"></td>
                </tr>
                <tr>
                    <td align="left" style="vertical-align: top;">
                        <div style="position: relative; overflow: hidden; width: 16px; height: 501px;">
                            <img src="http://static.eaw1805.com/images/panels/trade/barVertical.png"
                                 class="gwt-Image"
                                 style="width: 5px; height: 501px; position: absolute; left: 6px; top: 0px;"><img
                                src="http://static.eaw1805.com/images/panels/trade/ScrollerVertical.png"
                                class="gwt-Image"
                                style="height: 31px; position: absolute; left: 0px; top: 0px;">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="left" style="vertical-align: top;"><img
                            src="http://static.eaw1805.com/images/panels/trade/ButZoomOutOff.png"
                            class="gwt-Image" id="widget-928"></td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 323px; height: 529px; position: absolute; left: 814px; top: 16px;">
    <tbody>
    <tr>
        <td align="left" style="vertical-align: top;">
            <div style="overflow: auto; position: relative; width: 307px; height: 529px;"
                 class="noScrollBars">
                <div style="position: relative;">
                    <table cellspacing="0" cellpadding="0"
                           style="width: 100%; height: 0px;">
                        <tbody>
                        <tr>
                            <td align="center" style="vertical-align: middle;">
                                <table cellspacing="0" cellpadding="0"
                                       style="width: 297px; height: 106px;">
                                    <tbody>
                                    <tr>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 297px; height: 106px;"
                                                 class="tradeUnit pointer"><img
                                                    src="http://static.eaw1805.com/tiles/sites/tprod-12.png"
                                                    class="gwt-Image"
                                                    style="width: 65px; height: 65px; position: absolute; left: 0px; top: 0px;">

                                                <div class="clearFont"
                                                     style="width: 250px; height: 19px; position: absolute; left: 71px; top: 46px;">
                                                    Regional warehouse
                                                </div>
                                                <div class="clearFontMiniTitle"
                                                     style="width: 193px; height: 26px; position: absolute; left: 71px; top: 0px;">
                                                    France's Warehouse
                                                </div>
                                                <img src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                                                     class="gwt-Image"
                                                     title="Unit is currently in a trade city."
                                                     style="position: absolute; left: 269px; top: 18px; width: 24px; height: 24px;">

                                                <div class="clearFont"
                                                     style="text-align: left; width: 199px; height: 15px; position: absolute; left: 1px; top: 67px;">
                                                    Weight: Unlimited
                                                </div>
                                                <div style="position: absolute; overflow: hidden; width: 300px; height: 35px; left: 1px; top: 87px;"></div>
                                                <div class="clearFontSmall"
                                                     style="position: absolute; left: 252px; top: 2px;">
                                                    E28/20
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" style="vertical-align: middle;">
                                <table cellspacing="0" cellpadding="0"
                                       style="width: 297px; height: 106px;">
                                    <tbody>
                                    <tr>
                                        <td align="left" style="vertical-align: top;">
                                            <div style="position: relative; overflow: hidden; width: 297px; height: 106px;"
                                                 class="tradeUnitSel pointer"><img
                                                    src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                                                    class="gwt-Image"
                                                    style="width: 65px; height: 65px; position: absolute; left: 0px; top: 0px;">

                                                <div class="clearFont"
                                                     style="width: 250px; height: 19px; position: absolute; left: 71px; top: 46px;">
                                                    Trade city
                                                </div>
                                                <div class="clearFontMiniTitle"
                                                     style="width: 193px; height: 26px; position: absolute; left: 71px; top: 0px;">
                                                    Amsterdam
                                                </div>
                                                <img src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                                                     class="gwt-Image"
                                                     title="Unit is currently in a trade city."
                                                     style="position: absolute; left: 269px; top: 18px; width: 24px; height: 24px;">

                                                <div class="clearFont"
                                                     style="text-align: left; width: 199px; height: 15px; position: absolute; left: 1px; top: 67px;">
                                                    Wealth: 4,260,311
                                                </div>
                                                <div style="position: absolute; overflow: hidden; width: 300px; height: 35px; left: 1px; top: 87px;"></div>
                                                <div class="clearFontSmall"
                                                     style="position: absolute; left: 252px; top: 2px;">
                                                    E28/20
                                                </div>
                                            </div>
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
        </td>
        <td align="left" style="vertical-align: top;">
            <table cellspacing="0" cellpadding="0">
                <tbody>
                <tr>
                    <td align="left" style="vertical-align: top;"><img
                            src="http://static.eaw1805.com/images/panels/trade/ButZoomInOff.png"
                            class="gwt-Image" id="widget-929"></td>
                </tr>
                <tr>
                    <td align="left" style="vertical-align: top;">
                        <div style="position: relative; overflow: hidden; width: 16px; height: 501px;">
                            <img src="http://static.eaw1805.com/images/panels/trade/barVertical.png"
                                 class="gwt-Image"
                                 style="width: 5px; height: 501px; position: absolute; left: 6px; top: 0px;"><img
                                src="http://static.eaw1805.com/images/panels/trade/ScrollerVertical.png"
                                class="gwt-Image"
                                style="height: 31px; position: absolute; left: 0px; top: 0px;">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="left" style="vertical-align: top;"><img
                            src="http://static.eaw1805.com/images/panels/trade/ButZoomOutOff.png"
                            class="gwt-Image" id="widget-930"></td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
<div style="position: absolute; overflow: hidden; width: 322px; height: 126px; left: 429px; top: 76px;"
     class="tradeUnitSelected">
    <table cellspacing="0" cellpadding="0"
           style="width: 297px; height: 106px; position: absolute; left: 8px; top: 7px;">
        <tbody>
        <tr>
            <td align="left" style="vertical-align: top;">
                <div style="position: relative; overflow: hidden; width: 297px; height: 106px;"
                     class="tradeUnitSel pointer"><img
                        src="http://static.eaw1805.com/images/buttons/icons/baggage.png"
                        class="gwt-Image"
                        style="width: 65px; height: 65px; position: absolute; left: 0px; top: 0px;">

                    <div class="clearFont"
                         style="width: 250px; height: 19px; position: absolute; left: 71px; top: 46px;">
                        Type: Baggage train.
                    </div>
                    <div class="clearFontMiniTitle"
                         style="width: 193px; height: 26px; position: absolute; left: 71px; top: 0px;">
                        BaggageTrain 1
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                         class="gwt-Image" title="Unit is currently in a trade city."
                         style="position: absolute; left: 269px; top: 18px; width: 24px; height: 24px;">

                    <div class="clearFont"
                         style="text-align: left; width: 199px; height: 15px; position: absolute; left: 1px; top: 67px;">
                        Weight : 0 / 1500
                    </div>
                    <div style="position: absolute; overflow: hidden; width: 300px; height: 35px; left: 1px; top: 87px;">
                        <img src="http://static.eaw1805.com/images/goods/good-1.png"
                             class="gwt-Image"
                             style="position: absolute; left: 0px; top: 0px; width: 15px; height: 15px;"
                             title="Money:1055993"></div>
                    <div class="clearFontSmall"
                         style="position: absolute; left: 252px; top: 2px;">E28/20
                    </div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div style="position: absolute; overflow: hidden; width: 322px; height: 126px; left: 429px; top: 399px;"
     class="tradeUnitSelected">
    <table cellspacing="0" cellpadding="0"
           style="width: 297px; height: 106px; position: absolute; left: 8px; top: 7px;">
        <tbody>
        <tr>
            <td align="left" style="vertical-align: top;">
                <div style="position: relative; overflow: hidden; width: 297px; height: 106px;"
                     class="tradeUnitSel pointer"><img
                        src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                        class="gwt-Image"
                        style="width: 65px; height: 65px; position: absolute; left: 0px; top: 0px;">

                    <div class="clearFont"
                         style="width: 250px; height: 19px; position: absolute; left: 71px; top: 46px;">
                        Trade city
                    </div>
                    <div class="clearFontMiniTitle"
                         style="width: 193px; height: 26px; position: absolute; left: 71px; top: 0px;">
                        Amsterdam
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/icons/tcity.png"
                         class="gwt-Image" title="Unit is currently in a trade city."
                         style="position: absolute; left: 269px; top: 18px; width: 24px; height: 24px;">

                    <div class="clearFont"
                         style="text-align: left; width: 199px; height: 15px; position: absolute; left: 1px; top: 67px;">
                        Wealth: 4,260,311
                    </div>
                    <div style="position: absolute; overflow: hidden; width: 300px; height: 35px; left: 1px; top: 87px;"></div>
                    <div class="clearFontSmall"
                         style="position: absolute; left: 252px; top: 2px;">E28/20
                    </div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<img src="http://static.eaw1805.com/images/layout/buttons/ButTradingCitiesOn.png"
     class="pointer" id="widget-931"
     style="width: 160px; height: 45px; position: absolute; left: 510px; top: 278px;">
</div>
</div>
<img src="http://static.eaw1805.com/images/panels/trade/buttons/ButTradeCitiesOff.png"
     class="pointer" id="widget-912"
     style="position: absolute; left: 1164px; top: 86px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButBaggageTrainsOn.png"
        class="pointer" id="widget-913"
        style="position: absolute; left: 1164px; top: 275px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButMerchantShipsOff.png"
        class="pointer" id="widget-914"
        style="position: absolute; left: 1164px; top: 469px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOn.png"
        class="pointer" id="widget-915" title="Select european theater"
        style="position: absolute; left: 482px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png"
        class="pointer" id="widget-916" title="Select african theater"
        style="position: absolute; left: 540px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png"
        class="pointer" id="widget-917" title="Select carribean theater"
        style="position: absolute; left: 598px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png"
        class="pointer" id="widget-918" title="Select india theater"
        style="position: absolute; left: 655px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/buttons/ButTradeCityOff.png" class="pointer"
        id="widget-919" title="Browse trade cities"
        style="position: absolute; left: 713px; top: 44px; width: 31px; height: 31px;">
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
</div>
<div id="tradePanel5"
     style="display:none; position: absolute; overflow: hidden; width: 1218px; height: 653px; left: 295px; top: 228px;"
     class="tradePanel"><img src="http://static.eaw1805.com/images/buttons/ButAcceptOff.png"
                             class="gwt-Image" id="widget-909"
                             style="position: absolute; left: 1162px; top: 47px; width: 35px; height: 35px;">

<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 94px; top: 53px;">
    <img class="pointer" id="widget-910"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOn.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 1
    </div>
</div>
<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 916px; top: 53px;">
    <img class="pointer" id="widget-911"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 2
    </div>
</div>
<div style="position: absolute; overflow: hidden; left: 0px; top: 83px; width: 1158px; height: 603px;">
<div style="position: absolute; overflow: hidden; width: 1148px; height: 559px; left: 10px; top: 0px;"
     class="doTradeTab">
<table cellspacing="0" cellpadding="0"
       style="width: 314px; height: 448px; position: absolute; left: 64px; top: 59px;">
<tbody>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Money
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                1,055,993
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                0%
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Citizens
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Industrial Points
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-932"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOn.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Food
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-933"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Stone
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-934"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wood
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-935"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Ore
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-936"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Gems
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-937"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Horses
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-938"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Fabric
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-939"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wool
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-940"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Precious Metals
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-941"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wine
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-942"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Colonial Goods
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-943"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Loaded Units
            </div>
            <div class="clearFontMedium"
                 style="position: absolute; left: 130px; top: 10px; width: 86px; height: 15px;">
                0
            </div>
            <div class="clearFontMedium"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px;">
                00.0%
            </div>
        </div>
    </td>
</tr>
</tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 273px; height: 448px; position: absolute; left: 869px; top: 59px;">
<tbody>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Money
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: left; width: 65px; height: 15px; position: absolute; left: 130px; top: 10px;">
                4,260,311
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                People
            </div>
            <div class="clearFontMedium"
                 style="position: absolute; left: 130px; top: 10px; width: 86px; height: 15px;">
                -
            </div>
            <div class="clearFontMedium"
                 style="text-align: right; position: absolute; left: 205px; top: 10px; width: 48px; height: 15px;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Industrial Points
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Food
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Stone
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wood
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Ore
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Gems
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Horses
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses02.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Fabric
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric04.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wool
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool04.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Precious Metals
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wine
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Colonial Goods
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; width: 78px; height: 15px; position: absolute; left: 130px; top: 10px;"></div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
</tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="position: absolute; left: 384px; top: 59px; width: 445px; height: 448px;">
    <tbody>
    <tr>
        <td align="left" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 445px; height: 448px;">
                <div style="position: absolute; overflow: hidden; width: 420px; height: 166px; left: 10px; top: 34px;"
                     class="exchPanel">
                    <div style="position: absolute; overflow: hidden; width: 180px; height: 33px; left: 45px; top: 68px;">
                        <img src="http://static.eaw1805.com/images/panels/trade/tradeBar.png"
                             class="pointer"
                             style="width: 180px; height: 33px; position: absolute; left: 0px; top: 0px;"><img
                            src="http://static.eaw1805.com/images/panels/trade/tradeBarScroller.png"
                            class="pointer" id="widget-944"
                            style="width: 64px; height: 33px; position: absolute; left: 0px; top: 0px;">
                    </div>
                    <img src="http://static.eaw1805.com/images/panels/trade/ButSliderLeftOff.png"
                         class="pointer" id="widget-945"
                         style="width: 29px; height: 33px; position: absolute; left: 16px; top: 68px;"><img
                        src="http://static.eaw1805.com/images/panels/trade/ButSliderRightOff.png"
                        class="pointer" id="widget-946"
                        style="width: 29px; height: 33px; position: absolute; left: 225px; top: 68px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 16px; top: 12px;">
                        BaggageTrain 1
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/icons/RightArrow.png"
                         class="gwt-Image"
                         style="width: 48px; height: 32px; position: absolute; left: 160px; top: 5px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 222px; top: 12px;">
                        Amsterdam
                    </div>
                    <div class="clearFont"
                         style="width: 73px; height: 19px; position: absolute; left: 16px; top: 106px;">
                        Quantity:
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/taxation/MUIHarshTax.png"
                         class="pointer" id="widget-947" title="Do transaction"
                         style="position: absolute; left: 378px; top: 124px;"><img
                        src="http://static.eaw1805.com/images/goods/good-3.png"
                        class="gwt-Image"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 71px;"><img
                        src="http://static.eaw1805.com/images/goods/good-1.png"
                        class="gwt-Image" title="Total Monetary Cost"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 132px;">

                    <div class="clearFont"
                         style="width: 120px; height: 19px; position: absolute; left: 290px; top: 75px;">
                        Weight: 0.04
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 290px; top: 139px;">0
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 260px; top: 107px;">Item cost:
                        0
                    </div>
                    <input type="text" class="gwt-TextBox"
                           style="width: 126px; height: 21px; position: absolute; left: 16px; top: 127px;">
                </div>
                <div style="position: absolute; overflow: hidden; width: 420px; height: 166px; left: 10px; top: 244px;"
                     class="exchPanel">
                    <div style="position: absolute; overflow: hidden; width: 180px; height: 33px; left: 45px; top: 68px;">
                        <img src="http://static.eaw1805.com/images/panels/trade/tradeBar.png"
                             class="pointer"
                             style="width: 180px; height: 33px; position: absolute; left: 0px; top: 0px;"><img
                            src="http://static.eaw1805.com/images/panels/trade/tradeBarScroller.png"
                            class="pointer" id="widget-948"
                            style="width: 64px; height: 33px; position: absolute; left: 0px; top: 0px;">
                    </div>
                    <img src="http://static.eaw1805.com/images/panels/trade/ButSliderLeftOff.png"
                         class="pointer" id="widget-949"
                         style="width: 29px; height: 33px; position: absolute; left: 16px; top: 68px;"><img
                        src="http://static.eaw1805.com/images/panels/trade/ButSliderRightOff.png"
                        class="pointer" id="widget-950"
                        style="width: 29px; height: 33px; position: absolute; left: 225px; top: 68px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 16px; top: 12px;">
                        Amsterdam
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/icons/RightArrow.png"
                         class="gwt-Image"
                         style="width: 48px; height: 32px; position: absolute; left: 120px; top: 5px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 182px; top: 12px;">
                        BaggageTrain 1
                    </div>
                    <div class="clearFont"
                         style="width: 73px; height: 19px; position: absolute; left: 16px; top: 106px;">
                        Quantity:
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/taxation/MUIHarshTax.png"
                         class="pointer" id="widget-951" title="Do transaction"
                         style="position: absolute; left: 378px; top: 124px;"><img
                        src="http://static.eaw1805.com/images/goods/good-3.png"
                        class="gwt-Image"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 71px;"><img
                        src="http://static.eaw1805.com/images/goods/good-1.png"
                        class="gwt-Image" title="Total Monetary Cost"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 132px;">

                    <div class="clearFont"
                         style="width: 120px; height: 19px; position: absolute; left: 290px; top: 75px;">
                        Weight: 0.04
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 290px; top: 139px;">0
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 260px; top: 107px;">Item cost:
                        4200
                    </div>
                    <input type="text" class="gwt-TextBox"
                           style="width: 126px; height: 21px; position: absolute; left: 16px; top: 127px;">
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<div class="clearFont-large whiteText"
     style="text-align: left; width: 346px; height: 28px; position: absolute; left: 32px; top: 25px;">
    BaggageTrain 1
</div>
<div class="clearFont-large whiteText"
     style="text-align: left; width: 317px; height: 28px; position: absolute; left: 834px; top: 25px;">
    Amsterdam
</div>
</div>
</div>
<img src="http://static.eaw1805.com/images/panels/trade/buttons/ButTradeCitiesOff.png"
     class="pointer" id="widget-912"
     style="position: absolute; left: 1164px; top: 86px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButBaggageTrainsOn.png"
        class="pointer" id="widget-913"
        style="position: absolute; left: 1164px; top: 275px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButMerchantShipsOff.png"
        class="pointer" id="widget-914"
        style="position: absolute; left: 1164px; top: 469px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOn.png"
        class="pointer" id="widget-915" title="Select european theater"
        style="position: absolute; left: 482px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png"
        class="pointer" id="widget-916" title="Select african theater"
        style="position: absolute; left: 540px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png"
        class="pointer" id="widget-917" title="Select carribean theater"
        style="position: absolute; left: 598px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png"
        class="pointer" id="widget-918" title="Select india theater"
        style="position: absolute; left: 655px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/buttons/ButTradeCityOff.png" class="pointer"
        id="widget-919" title="Browse trade cities"
        style="position: absolute; left: 713px; top: 44px; width: 31px; height: 31px;">
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
</div>
<div id="tradePanel6"
     style="display:none; position: absolute; overflow: hidden; width: 1218px; height: 653px; left: 295px; top: 228px;"
     class="tradePanel"><img src="http://static.eaw1805.com/images/buttons/ButAcceptOff.png"
                             class="gwt-Image" id="widget-909"
                             style="position: absolute; left: 1162px; top: 47px; width: 35px; height: 35px;">

<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 94px; top: 53px;">
    <img class="pointer" id="widget-910"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOn.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 1
    </div>
</div>
<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 916px; top: 53px;">
    <img class="pointer" id="widget-911"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 2
    </div>
</div>
<div style="position: absolute; overflow: hidden; left: 0px; top: 83px; width: 1158px; height: 603px;">
<div style="position: absolute; overflow: hidden; width: 1148px; height: 559px; left: 10px; top: 0px;"
     class="doTradeTab">
<table cellspacing="0" cellpadding="0"
       style="width: 314px; height: 448px; position: absolute; left: 64px; top: 59px;">
<tbody>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Money
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                1,055,993
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                0%
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Citizens
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Industrial Points
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-932"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Food
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-933"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Stone
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-934"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOn.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wood
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-935"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Ore
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-936"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Gems
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-937"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Horses
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-938"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Fabric
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-939"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wool
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-940"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Precious Metals
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-941"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wine
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-942"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Colonial Goods
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-943"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Loaded Units
            </div>
            <div class="clearFontMedium"
                 style="position: absolute; left: 130px; top: 10px; width: 86px; height: 15px;">
                0
            </div>
            <div class="clearFontMedium"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px;">
                00.0%
            </div>
        </div>
    </td>
</tr>
</tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 273px; height: 448px; position: absolute; left: 869px; top: 59px;">
<tbody>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Money
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: left; width: 65px; height: 15px; position: absolute; left: 130px; top: 10px;">
                4,260,311
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                People
            </div>
            <div class="clearFontMedium"
                 style="position: absolute; left: 130px; top: 10px; width: 86px; height: 15px;">
                -
            </div>
            <div class="clearFontMedium"
                 style="text-align: right; position: absolute; left: 205px; top: 10px; width: 48px; height: 15px;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Industrial Points
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Food
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Stone
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wood
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Ore
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Gems
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Horses
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses02.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Fabric
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric04.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wool
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool04.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Precious Metals
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wine
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Colonial Goods
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; width: 78px; height: 15px; position: absolute; left: 130px; top: 10px;"></div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
</tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="position: absolute; left: 384px; top: 59px; width: 445px; height: 448px;">
    <tbody>
    <tr>
        <td align="left" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 445px; height: 448px;">
                <div style="position: absolute; overflow: hidden; width: 420px; height: 166px; left: 10px; top: 34px;"
                     class="exchPanel">
                    <div style="position: absolute; overflow: hidden; width: 180px; height: 33px; left: 45px; top: 68px;">
                        <img src="http://static.eaw1805.com/images/panels/trade/tradeBar.png"
                             class="pointer"
                             style="width: 180px; height: 33px; position: absolute; left: 0px; top: 0px;"><img
                            src="http://static.eaw1805.com/images/panels/trade/tradeBarScroller.png"
                            class="pointer" id="widget-952"
                            style="width: 64px; height: 33px; position: absolute; left: 0px; top: 0px;">
                    </div>
                    <img src="http://static.eaw1805.com/images/panels/trade/ButSliderLeftOff.png"
                         class="pointer" id="widget-953"
                         style="width: 29px; height: 33px; position: absolute; left: 16px; top: 68px;"><img
                        src="http://static.eaw1805.com/images/panels/trade/ButSliderRightOff.png"
                        class="pointer" id="widget-954"
                        style="width: 29px; height: 33px; position: absolute; left: 225px; top: 68px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 16px; top: 12px;">
                        BaggageTrain 1
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/icons/RightArrow.png"
                         class="gwt-Image"
                         style="width: 48px; height: 32px; position: absolute; left: 160px; top: 5px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 222px; top: 12px;">
                        Amsterdam
                    </div>
                    <div class="clearFont"
                         style="width: 73px; height: 19px; position: absolute; left: 16px; top: 106px;">
                        Quantity:
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/taxation/MUIHarshTax.png"
                         class="pointer" id="widget-955" title="Do transaction"
                         style="position: absolute; left: 378px; top: 124px;"><img
                        src="http://static.eaw1805.com/images/goods/good-5.png"
                        class="gwt-Image"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 71px;"><img
                        src="http://static.eaw1805.com/images/goods/good-1.png"
                        class="gwt-Image" title="Total Monetary Cost"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 132px;">

                    <div class="clearFont"
                         style="width: 120px; height: 19px; position: absolute; left: 290px; top: 75px;">
                        Weight: 0.5
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 290px; top: 139px;">0
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 260px; top: 107px;">Item cost:
                        0
                    </div>
                    <input type="text" class="gwt-TextBox"
                           style="width: 126px; height: 21px; position: absolute; left: 16px; top: 127px;">
                </div>
                <div style="position: absolute; overflow: hidden; width: 420px; height: 166px; left: 10px; top: 244px;"
                     class="exchPanel">
                    <div style="position: absolute; overflow: hidden; width: 180px; height: 33px; left: 45px; top: 68px;">
                        <img src="http://static.eaw1805.com/images/panels/trade/tradeBar.png"
                             class="pointer"
                             style="width: 180px; height: 33px; position: absolute; left: 0px; top: 0px;"><img
                            src="http://static.eaw1805.com/images/panels/trade/tradeBarScroller.png"
                            class="pointer" id="widget-956"
                            style="width: 64px; height: 33px; position: absolute; left: 0px; top: 0px;">
                    </div>
                    <img src="http://static.eaw1805.com/images/panels/trade/ButSliderLeftOff.png"
                         class="pointer" id="widget-957"
                         style="width: 29px; height: 33px; position: absolute; left: 16px; top: 68px;"><img
                        src="http://static.eaw1805.com/images/panels/trade/ButSliderRightOff.png"
                        class="pointer" id="widget-958"
                        style="width: 29px; height: 33px; position: absolute; left: 225px; top: 68px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 16px; top: 12px;">
                        Amsterdam
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/icons/RightArrow.png"
                         class="gwt-Image"
                         style="width: 48px; height: 32px; position: absolute; left: 120px; top: 5px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 182px; top: 12px;">
                        BaggageTrain 1
                    </div>
                    <div class="clearFont"
                         style="width: 73px; height: 19px; position: absolute; left: 16px; top: 106px;">
                        Quantity:
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/taxation/MUIHarshTax.png"
                         class="pointer" id="widget-959" title="Do transaction"
                         style="position: absolute; left: 378px; top: 124px;"><img
                        src="http://static.eaw1805.com/images/goods/good-5.png"
                        class="gwt-Image"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 71px;"><img
                        src="http://static.eaw1805.com/images/goods/good-1.png"
                        class="gwt-Image" title="Total Monetary Cost"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 132px;">

                    <div class="clearFont"
                         style="width: 120px; height: 19px; position: absolute; left: 290px; top: 75px;">
                        Weight: 0.5
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 290px; top: 139px;">0
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 260px; top: 107px;">Item cost:
                        525
                    </div>
                    <input type="text" class="gwt-TextBox"
                           style="width: 126px; height: 21px; position: absolute; left: 16px; top: 127px;">
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<div class="clearFont-large whiteText"
     style="text-align: left; width: 346px; height: 28px; position: absolute; left: 32px; top: 25px;">
    BaggageTrain 1
</div>
<div class="clearFont-large whiteText"
     style="text-align: left; width: 317px; height: 28px; position: absolute; left: 834px; top: 25px;">
    Amsterdam
</div>
</div>
</div>
<img src="http://static.eaw1805.com/images/panels/trade/buttons/ButTradeCitiesOff.png"
     class="pointer" id="widget-912"
     style="position: absolute; left: 1164px; top: 86px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButBaggageTrainsOn.png"
        class="pointer" id="widget-913"
        style="position: absolute; left: 1164px; top: 275px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButMerchantShipsOff.png"
        class="pointer" id="widget-914"
        style="position: absolute; left: 1164px; top: 469px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOn.png"
        class="pointer" id="widget-915" title="Select european theater"
        style="position: absolute; left: 482px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png"
        class="pointer" id="widget-916" title="Select african theater"
        style="position: absolute; left: 540px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png"
        class="pointer" id="widget-917" title="Select carribean theater"
        style="position: absolute; left: 598px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png"
        class="pointer" id="widget-918" title="Select india theater"
        style="position: absolute; left: 655px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/buttons/ButTradeCityOff.png" class="pointer"
        id="widget-919" title="Browse trade cities"
        style="position: absolute; left: 713px; top: 44px; width: 31px; height: 31px;">
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
</div>
<div id="tradePanel7"
     style="display:none; position: absolute; overflow: hidden; width: 1218px; height: 653px; left: 295px; top: 228px;"
     class="tradePanel"><img src="http://static.eaw1805.com/images/buttons/ButAcceptOff.png"
                             class="gwt-Image" id="widget-909"
                             style="position: absolute; left: 1162px; top: 47px; width: 35px; height: 35px;">

<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 94px; top: 53px;">
    <img class="pointer" id="widget-910"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOn.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 1
    </div>
</div>
<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 916px; top: 53px;">
    <img class="pointer" id="widget-911"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 2
    </div>
</div>
<div style="position: absolute; overflow: hidden; left: 0px; top: 83px; width: 1158px; height: 603px;">
<div style="position: absolute; overflow: hidden; width: 1148px; height: 559px; left: 10px; top: 0px;"
     class="doTradeTab">
<table cellspacing="0" cellpadding="0"
       style="width: 314px; height: 448px; position: absolute; left: 64px; top: 59px;">
<tbody>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Money
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                1,055,993
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                0%
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Citizens
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Industrial Points
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-932"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Food
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-933"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Stone
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-934"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOn.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wood
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-935"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Ore
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-936"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Gems
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-937"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Horses
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-938"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Fabric
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-939"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wool
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-940"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Precious Metals
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-941"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wine
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-942"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Colonial Goods
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-943"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Loaded Units
            </div>
            <div class="clearFontMedium"
                 style="position: absolute; left: 130px; top: 10px; width: 86px; height: 15px;">
                0
            </div>
            <div class="clearFontMedium"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px;">
                00.0%
            </div>
        </div>
    </td>
</tr>
</tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 273px; height: 448px; position: absolute; left: 869px; top: 59px;">
<tbody>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Money
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: left; width: 65px; height: 15px; position: absolute; left: 130px; top: 10px;">
                4,260,311
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                People
            </div>
            <div class="clearFontMedium"
                 style="position: absolute; left: 130px; top: 10px; width: 86px; height: 15px;">
                -
            </div>
            <div class="clearFontMedium"
                 style="text-align: right; position: absolute; left: 205px; top: 10px; width: 48px; height: 15px;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Industrial Points
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Food
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Stone
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wood
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Ore
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Gems
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Horses
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses02.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Fabric
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric04.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wool
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool04.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Precious Metals
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wine
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Colonial Goods
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; width: 78px; height: 15px; position: absolute; left: 130px; top: 10px;"></div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
</tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="position: absolute; left: 384px; top: 59px; width: 445px; height: 448px;">
    <tbody>
    <tr>
        <td align="left" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 445px; height: 448px;">
                <div style="position: absolute; overflow: hidden; width: 420px; height: 166px; left: 10px; top: 34px;"
                     class="exchPanel">
                    <div style="position: absolute; overflow: hidden; width: 180px; height: 33px; left: 45px; top: 68px;">
                        <img src="http://static.eaw1805.com/images/panels/trade/tradeBar.png"
                             class="pointer"
                             style="width: 180px; height: 33px; position: absolute; left: 0px; top: 0px;"><img
                            src="http://static.eaw1805.com/images/panels/trade/tradeBarScroller.png"
                            class="pointer" id="widget-952"
                            style="width: 64px; height: 33px; position: absolute; left: 0px; top: 0px;">
                    </div>
                    <img src="http://static.eaw1805.com/images/panels/trade/ButSliderLeftOff.png"
                         class="pointer" id="widget-953"
                         style="width: 29px; height: 33px; position: absolute; left: 16px; top: 68px;"><img
                        src="http://static.eaw1805.com/images/panels/trade/ButSliderRightOff.png"
                        class="pointer" id="widget-954"
                        style="width: 29px; height: 33px; position: absolute; left: 225px; top: 68px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 16px; top: 12px;">
                        BaggageTrain 1
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/icons/RightArrow.png"
                         class="gwt-Image"
                         style="width: 48px; height: 32px; position: absolute; left: 160px; top: 5px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 222px; top: 12px;">
                        Amsterdam
                    </div>
                    <div class="clearFont"
                         style="width: 73px; height: 19px; position: absolute; left: 16px; top: 106px;">
                        Quantity:
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/taxation/MUIHarshTax.png"
                         class="pointer" id="widget-955" title="Do transaction"
                         style="position: absolute; left: 378px; top: 124px;"><img
                        src="http://static.eaw1805.com/images/goods/good-5.png"
                        class="gwt-Image"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 71px;"><img
                        src="http://static.eaw1805.com/images/goods/good-1.png"
                        class="gwt-Image" title="Total Monetary Cost"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 132px;">

                    <div class="clearFont"
                         style="width: 120px; height: 19px; position: absolute; left: 290px; top: 75px;">
                        Weight: 0.5
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 290px; top: 139px;">0
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 260px; top: 107px;">Item cost:
                        0
                    </div>
                    <input type="text" class="gwt-TextBox"
                           style="width: 126px; height: 21px; position: absolute; left: 16px; top: 127px;">
                </div>
                <div style="position: absolute; overflow: hidden; width: 420px; height: 166px; left: 10px; top: 244px;"
                     class="exchPanel">
                    <div style="position: absolute; overflow: hidden; width: 180px; height: 33px; left: 45px; top: 68px;">
                        <img src="http://static.eaw1805.com/images/panels/trade/tradeBar.png"
                             class="pointer"
                             style="width: 180px; height: 33px; position: absolute; left: 0px; top: 0px;"><img
                            src="http://static.eaw1805.com/images/panels/trade/tradeBarScroller.png"
                            class="pointer" id="widget-956"
                            style="width: 64px; height: 33px; position: absolute; left: 50px; top: 0px;">
                    </div>
                    <img src="http://static.eaw1805.com/images/panels/trade/ButSliderLeftOff.png"
                         class="pointer" id="widget-957"
                         style="width: 29px; height: 33px; position: absolute; left: 16px; top: 68px;"><img
                        src="http://static.eaw1805.com/images/panels/trade/ButSliderRightOff.png"
                        class="pointer" id="widget-958"
                        style="width: 29px; height: 33px; position: absolute; left: 225px; top: 68px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 16px; top: 12px;">
                        Amsterdam
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/icons/RightArrow.png"
                         class="gwt-Image"
                         style="width: 48px; height: 32px; position: absolute; left: 120px; top: 5px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 182px; top: 12px;">
                        BaggageTrain 1
                    </div>
                    <div class="clearFont"
                         style="width: 73px; height: 19px; position: absolute; left: 16px; top: 106px;">
                        Quantity:
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/taxation/MUIHarshTax.png"
                         class="pointer" id="widget-959" title="Do transaction"
                         style="position: absolute; left: 378px; top: 124px;"><img
                        src="http://static.eaw1805.com/images/goods/good-5.png"
                        class="gwt-Image"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 71px;"><img
                        src="http://static.eaw1805.com/images/goods/good-1.png"
                        class="gwt-Image" title="Total Monetary Cost"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 132px;">

                    <div class="clearFont"
                         style="width: 120px; height: 19px; position: absolute; left: 290px; top: 75px;">
                        Weight: 0.5
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 290px; top: 139px;">457,080
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 260px; top: 107px;">Item cost:
                        354
                    </div>
                    <input type="text" class="gwt-TextBox" value="1262"
                           style="width: 126px; height: 21px; position: absolute; left: 16px; top: 127px;">
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<div class="clearFont-large whiteText"
     style="text-align: left; width: 346px; height: 28px; position: absolute; left: 32px; top: 25px;">
    BaggageTrain 1
</div>
<div class="clearFont-large whiteText"
     style="text-align: left; width: 317px; height: 28px; position: absolute; left: 834px; top: 25px;">
    Amsterdam
</div>
</div>
</div>
<img src="http://static.eaw1805.com/images/panels/trade/buttons/ButTradeCitiesOff.png"
     class="pointer" id="widget-912"
     style="position: absolute; left: 1164px; top: 86px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButBaggageTrainsOn.png"
        class="pointer" id="widget-913"
        style="position: absolute; left: 1164px; top: 275px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButMerchantShipsOff.png"
        class="pointer" id="widget-914"
        style="position: absolute; left: 1164px; top: 469px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOn.png"
        class="pointer" id="widget-915" title="Select european theater"
        style="position: absolute; left: 482px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png"
        class="pointer" id="widget-916" title="Select african theater"
        style="position: absolute; left: 540px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png"
        class="pointer" id="widget-917" title="Select carribean theater"
        style="position: absolute; left: 598px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png"
        class="pointer" id="widget-918" title="Select india theater"
        style="position: absolute; left: 655px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/buttons/ButTradeCityOff.png" class="pointer"
        id="widget-919" title="Browse trade cities"
        style="position: absolute; left: 713px; top: 44px; width: 31px; height: 31px;">
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
</div>
<div id="tradePanel8"
     style="display:none; position: absolute; overflow: hidden; width: 1218px; height: 653px; left: 295px; top: 228px;"
     class="tradePanel"><img src="http://static.eaw1805.com/images/buttons/ButAcceptOff.png"
                             class="gwt-Image" id="widget-909"
                             style="position: absolute; left: 1162px; top: 47px; width: 35px; height: 35px;">

<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 94px; top: 53px;">
    <img class="pointer" id="widget-910"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOn.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 1
    </div>
</div>
<div style="position: absolute; overflow: hidden; width: 210px; height: 21px; left: 916px; top: 53px;">
    <img class="pointer" id="widget-911"
         src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
         style="position: absolute; left: 155px; top: 0px; width: 21px; height: 21px;">

    <div class="clearFont whiteText"
         style="white-space: nowrap; text-align: left; position: absolute; left: 0px; top: 0px; width: 150px; height: 21px;">
        Trade &amp; loading 2
    </div>
</div>
<div style="position: absolute; overflow: hidden; left: 0px; top: 83px; width: 1158px; height: 603px;">
<div style="position: absolute; overflow: hidden; width: 1148px; height: 559px; left: 10px; top: 0px;"
     class="doTradeTab">
<table cellspacing="0" cellpadding="0"
       style="width: 314px; height: 448px; position: absolute; left: 64px; top: 59px;">
<tbody>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Money
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                608,077
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                0%
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Citizens
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Industrial Points
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-932"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Food
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-933"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Stone
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                1,262
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                42.1%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-934"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOn.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wood
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-935"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Ore
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-936"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Gems
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-937"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Horses
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-938"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Fabric
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-939"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wool
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-940"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Precious Metals
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-941"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wine
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-942"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Colonial Goods
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; position: absolute; left: 120px; top: 10px; width: 75px; height: 15px;">
                0
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                00.0%
            </div>
            <div style="position: absolute; overflow: hidden; width: 21px; height: 21px; left: 280px; top: 5px;">
                <img class="pointer" id="widget-943"
                     src="http://static.eaw1805.com/images/buttons/CheckBoxOff.png"
                     style="position: absolute; left: 0px; top: 0px; width: 21px; height: 21px;">
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Loaded Units
            </div>
            <div class="clearFontMedium"
                 style="position: absolute; left: 130px; top: 10px; width: 86px; height: 15px;">
                0
            </div>
            <div class="clearFontMedium"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px;">
                00.0%
            </div>
        </div>
    </td>
</tr>
</tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 273px; height: 448px; position: absolute; left: 869px; top: 59px;">
<tbody>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Money
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: left; width: 65px; height: 15px; position: absolute; left: 130px; top: 10px;">
                4,260,311
            </div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                People
            </div>
            <div class="clearFontMedium"
                 style="position: absolute; left: 130px; top: 10px; width: 86px; height: 15px;">
                -
            </div>
            <div class="clearFontMedium"
                 style="text-align: right; position: absolute; left: 205px; top: 10px; width: 48px; height: 15px;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Industrial Points
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRIndustrial03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Food
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFood01.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Stone
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRStone04.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wood
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWood01.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Ore
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLROre03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Gems
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRGems01.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Horses
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRHorses02.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Fabric
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRFabric04.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wool
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWool04.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Precious Metals
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRPrecious03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Wine
            </div>
            <img src="http://static.eaw1805.com/images/panels/trade/buttons/CLRWine03.png"
                 class="gwt-Image"
                 style="width: 60px; height: 24px; position: absolute; left: 130px; top: 4px;">

            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
<tr>
    <td align="left" style="vertical-align: top;">
        <div style="position: relative; overflow: hidden; width: 100%; height: 32px;">
            <div class="clearFontMedium"
                 style="position: absolute; left: 0px; top: 10px; width: 127px; height: 15px;">
                Colonial Goods
            </div>
            <div class="clearFontMedSmall textRight"
                 style="text-align: right; width: 78px; height: 15px; position: absolute; left: 130px; top: 10px;"></div>
            <div class="clearFontMedSmall"
                 style="position: absolute; left: 205px; top: 10px; width: 48px; height: 15px; text-align: right;">
                -
            </div>
        </div>
    </td>
</tr>
</tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="position: absolute; left: 384px; top: 59px; width: 445px; height: 448px;">
    <tbody>
    <tr>
        <td align="left" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 445px; height: 448px;">
                <div style="position: absolute; overflow: hidden; width: 420px; height: 166px; left: 10px; top: 34px;"
                     class="exchPanel">
                    <div style="position: absolute; overflow: hidden; width: 180px; height: 33px; left: 45px; top: 68px;">
                        <img src="http://static.eaw1805.com/images/panels/trade/tradeBar.png"
                             class="pointer"
                             style="width: 180px; height: 33px; position: absolute; left: 0px; top: 0px;"><img
                            src="http://static.eaw1805.com/images/panels/trade/tradeBarScroller.png"
                            class="pointer" id="widget-970"
                            style="width: 64px; height: 33px; position: absolute; left: 0px; top: 0px;">
                    </div>
                    <img src="http://static.eaw1805.com/images/panels/trade/ButSliderLeftOff.png"
                         class="pointer" id="widget-971"
                         style="width: 29px; height: 33px; position: absolute; left: 16px; top: 68px;"><img
                        src="http://static.eaw1805.com/images/panels/trade/ButSliderRightOff.png"
                        class="pointer" id="widget-972"
                        style="width: 29px; height: 33px; position: absolute; left: 225px; top: 68px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 16px; top: 12px;">
                        BaggageTrain 1
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/icons/RightArrow.png"
                         class="gwt-Image"
                         style="width: 48px; height: 32px; position: absolute; left: 160px; top: 5px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 222px; top: 12px;">
                        Amsterdam
                    </div>
                    <div class="clearFont"
                         style="width: 73px; height: 19px; position: absolute; left: 16px; top: 106px;">
                        Quantity:
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/taxation/MUIHarshTax.png"
                         class="pointer" id="widget-973" title="Do transaction"
                         style="position: absolute; left: 378px; top: 124px;"><img
                        src="http://static.eaw1805.com/images/goods/good-5.png"
                        class="gwt-Image"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 71px;"><img
                        src="http://static.eaw1805.com/images/goods/good-1.png"
                        class="gwt-Image" title="Total Monetary Cost"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 132px;">

                    <div class="clearFont"
                         style="width: 120px; height: 19px; position: absolute; left: 290px; top: 75px;">
                        Weight: 0.5
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 290px; top: 139px;">0
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 260px; top: 107px;">Item cost:
                        72
                    </div>
                    <input type="text" class="gwt-TextBox"
                           style="width: 126px; height: 21px; position: absolute; left: 16px; top: 127px;">
                </div>
                <div style="position: absolute; overflow: hidden; width: 420px; height: 166px; left: 10px; top: 244px;"
                     class="exchPanel">
                    <div style="position: absolute; overflow: hidden; width: 180px; height: 33px; left: 45px; top: 68px;">
                        <img src="http://static.eaw1805.com/images/panels/trade/tradeBar.png"
                             class="pointer"
                             style="width: 180px; height: 33px; position: absolute; left: 0px; top: 0px;"><img
                            src="http://static.eaw1805.com/images/panels/trade/tradeBarScroller.png"
                            class="pointer" id="widget-974"
                            style="width: 64px; height: 33px; position: absolute; left: 0px; top: 0px;">
                    </div>
                    <img src="http://static.eaw1805.com/images/panels/trade/ButSliderLeftOff.png"
                         class="pointer" id="widget-975"
                         style="width: 29px; height: 33px; position: absolute; left: 16px; top: 68px;"><img
                        src="http://static.eaw1805.com/images/panels/trade/ButSliderRightOff.png"
                        class="pointer" id="widget-976"
                        style="width: 29px; height: 33px; position: absolute; left: 225px; top: 68px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 16px; top: 12px;">
                        Amsterdam
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/icons/RightArrow.png"
                         class="gwt-Image"
                         style="width: 48px; height: 32px; position: absolute; left: 120px; top: 5px;">

                    <div class="clearFont"
                         style="width: 280px; height: 32px; position: absolute; left: 182px; top: 12px;">
                        BaggageTrain 1
                    </div>
                    <div class="clearFont"
                         style="width: 73px; height: 19px; position: absolute; left: 16px; top: 106px;">
                        Quantity:
                    </div>
                    <img src="http://static.eaw1805.com/images/buttons/taxation/MUIHarshTax.png"
                         class="pointer" id="widget-977" title="Do transaction"
                         style="position: absolute; left: 378px; top: 124px;"><img
                        src="http://static.eaw1805.com/images/goods/good-5.png"
                        class="gwt-Image"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 71px;"><img
                        src="http://static.eaw1805.com/images/goods/good-1.png"
                        class="gwt-Image" title="Total Monetary Cost"
                        style="width: 24px; height: 24px; position: absolute; left: 260px; top: 132px;">

                    <div class="clearFont"
                         style="width: 120px; height: 19px; position: absolute; left: 290px; top: 75px;">
                        Weight: 0.5
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 290px; top: 139px;">0
                    </div>
                    <div class="clearFont"
                         style="position: absolute; left: 260px; top: 107px;">Item cost:
                        525
                    </div>
                    <input type="text" class="gwt-TextBox"
                           style="width: 126px; height: 21px; position: absolute; left: 16px; top: 127px;">
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<div class="clearFont-large whiteText"
     style="text-align: left; width: 346px; height: 28px; position: absolute; left: 32px; top: 25px;">
    BaggageTrain 1
</div>
<div class="clearFont-large whiteText"
     style="text-align: left; width: 317px; height: 28px; position: absolute; left: 834px; top: 25px;">
    Amsterdam
</div>
</div>
</div>
<img src="http://static.eaw1805.com/images/panels/trade/buttons/ButTradeCitiesOff.png"
     class="pointer" id="widget-912"
     style="position: absolute; left: 1164px; top: 86px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButBaggageTrainsOn.png"
        class="pointer" id="widget-913"
        style="position: absolute; left: 1164px; top: 275px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/panels/trade/buttons/ButMerchantShipsOff.png"
        class="pointer" id="widget-914"
        style="position: absolute; left: 1164px; top: 469px; width: 30px; height: 171px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOn.png"
        class="pointer" id="widget-915" title="Select european theater"
        style="position: absolute; left: 482px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png"
        class="pointer" id="widget-916" title="Select african theater"
        style="position: absolute; left: 540px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png"
        class="pointer" id="widget-917" title="Select carribean theater"
        style="position: absolute; left: 598px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png"
        class="pointer" id="widget-918" title="Select india theater"
        style="position: absolute; left: 655px; top: 44px; width: 31px; height: 31px;"><img
        src="http://static.eaw1805.com/images/buttons/ButTradeCityOff.png" class="pointer"
        id="widget-919" title="Browse trade cities"
        style="position: absolute; left: 713px; top: 44px; width: 31px; height: 31px;">
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<table cellspacing="0" cellpadding="0"
       style="width: 1199px; height: 42px; position: absolute; left: 10px; top: 7px;">
    <tbody>
    <tr>
        <td align="center" style="vertical-align: top;">
            <div style="position: relative; overflow: hidden; width: 1199px; height: 42px;"
                 class="economyPanel">
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 34px; top: 7px;"
                     title="Current Treasury level">1,311,255
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 154px; top: 7px;"
                     title="Current Regional Warehouse stock of Citizens">51,647
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 245px; top: 7px;"
                     title="Current Regional Warehouse stock of Industrial Points">208
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 336px; top: 7px;"
                     title="Current Regional Warehouse stock of Food">6,894
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 427px; top: 7px;"
                     title="Current Regional Warehouse stock of Fabric">6,959
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 518px; top: 7px;"
                     title="Current Regional Warehouse stock of Wool">2,554
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 609px; top: 7px;"
                     title="Current Regional Warehouse stock of Stone">12,260
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 700px; top: 7px;"
                     title="Current Regional Warehouse stock of Wood">27,039
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 791px; top: 7px;"
                     title="Current Regional Warehouse stock of Horse">3,150
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 882px; top: 7px;"
                     title="Current Regional Warehouse stock of Ore">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 949px; top: 7px;"
                     title="Current Regional Warehouse stock of Precious Metals">0
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1017px; top: 7px;"
                     title="Current Regional Warehouse stock of Gems">461
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1085px; top: 7px;"
                     title="Current Regional Warehouse stock of Wine">1,491
                </div>
                <div class="gwt-Label"
                     style="text-align: left; position: absolute; left: 1153px; top: 7px;"
                     title="Current Regional Warehouse stock of Colonial Goods">4,047
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
</div>

<img src="http://static.eaw1805.com/images/animation/cursor.png" id="animCursor"
     style="position: absolute; left: 20px; top: 20px;">

</div>
<%--</c:forEach>--%>
</div>
</div>
</div>

<h1 style="font-size: 42px; width: 945px; text-align: center; padding-top: 10px;">Realistic Trade System &amp; Economy
    of the 1805 era</h1>

<table style="width: 985px;">
    <tr valign="top">
        <td width="320" valign="top" style="text-align: justify; padding-right: 40px;">
            Trading is based on a per-commodity demand and supply mechanism: in the same city there may be a surplus of
            ore and a demand for wool. Demand for a commodity drives prices high and it is time to sell, while where
            there is surplus, goods are a bargain to buy.
        </td>
        <td width="320" valign="top" style="text-align: justify; padding-right: 40px;">
            Trading can become a significant addition to a country's income. Trade with local merchants as well as with
            other nations is a hefty source of income and resources, and the lucrative colonial trade routes can prove
            extremely beneficial to the Empires that control them.
        </td>
        <td width="320" valign="top" style="text-align: justify; padding-right: 40px;">
            In Napoleonic times it was not only nations that were trading. Non Player Characters also trade in the
            cities. Their trading influences Trade Rates and City Wealth. NPCs tend to buy when cheap and sell when
            expensive, driving the market to equilibrium. Or so it seems most of the time...
        </td>
    </tr>
</table>

<div style="padding-top: 10px; padding-bottom: 10px;">
    <a style="font-size: 16px; " href="<c:url value="/help/trade"/>">
        <img src="http://static.eaw1805.com/images/buttons/ButLoadOff.png" style="margin-bottom: -13px;">
        Read more about the Trade system</a>
</div>
</div>

<div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78"></div>

<div style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;"></div>

<div style=" position: relative;margin: -10px 0px 0px -35px;width: 1000px;min-height: 343px;padding: 0px 40px;
background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">

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

<h1 style="font-size: 42px; width: 945px; text-align: center; padding-top: 10px;">Rich Combined Arms Military
    System</h1>

<table style="width: 985px;">
    <tr valign="top">
        <td width="320" valign="top" style="text-align: justify; padding-right: 40px;">
            In Empires at War 1805 you can control Armies & Corps to conquer, Ships to expand and colonize, Baggage
            trains to perform trade, and Spies to conduct espionage. Each element is crucial for your empire to survive
            and prevail the wars that are about to start.
        </td>
        <td width="320" valign="top" style="text-align: justify; padding-right: 40px;">
            Efficient maneuvering was an important trait of a successful campaign in the Napoleonic era. The limited
            range of land forces, the attrition incurred by the harsh terrain and inefficient supply conditions, made
            decisions regarding movement very important.
        </td>
        <td width="320" valign="top" style="text-align: justify; padding-right: 40px;">
            Orders are considered to occupy some time from the emperor’s and his/her general staff’s time. Each nation
            has a certain number of command and administrative points and each command given requires the expenditure of
            a fraction of these points, based on its complexity.
        </td>
    </tr>
</table>

<div style="padding-top: 10px; padding-bottom: 10px;">
    <a style="font-size: 16px; " href="<c:url value="/help/movement"/>">
        <img src="http://static.eaw1805.com/images/buttons/ButForcedMarchOff.png" style="margin-bottom: -13px;">
        Read more about the Movement system</a>
</div>
</div>

<div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78"></div>

<div style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;"></div>

<div style=" position: relative;margin: -10px 0px 0px -35px;width: 1000px;min-height: 343px;padding: 0px 40px;
background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">

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

    <h1 style="font-size: 42px; width: 945px; text-align: center; padding-top: 10px;">Multi-Level Structuring of
        Napolenic Era Forces</h1>

    <table style="width: 985px;">
        <tr valign="top">
            <td width="320" valign="top" style="text-align: justify; padding-right: 40px;">
                To depict the level of military organization of the era, military land forces are setup in four
                administrative levels: battalions, brigades, corps and armies. Each empire has a plethora of troop types
                available for recruiting. These are available in the battalion level and are shown for each empire on
                the <a href="<c:url value="/scenario/1802/info"/>">scenario page</a>.
            </td>
            <td width="320" valign="top" style="text-align: justify; padding-right: 40px;">
                Operations at sea are crucial. Dominance of the oceans can offer supply routes, lucrative trading,
                opportunities for invasions and colonial expansion. There are many different types of war & merchant
                ships, from small corvettes to huge Ships-of-the-Line, to be used for naval operations.
            </td>
            <td width="320" valign="top" style="text-align: justify; padding-right: 40px;">
                Warfare is the ultimate decider of who the victor will be. Warfare in Empires at war 1805 is so
                detailed, portraying the complexity of military operations of the era, that achieving victory requires
                exceptional organization and strategic skills. The scale is taken from the strategic to the tactical
                level, requiring strong commanding capabilities in the field of battle.
            </td>
        </tr>
    </table>

    <div style="padding-top: 10px; padding-bottom: 10px;">
        <a style="font-size: 16px; " href="<c:url value="/help/warfare"/>">
            <img src="http://static.eaw1805.com/images/buttons/ButFormFedOff.png" style="margin-bottom: -13px;">
            Read more about the Warfare system</a>
    </div>
</div>

<div style="clear: both;position: relative; margin: 0 0 10px -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-footer.png') 0 0 no-repeat;clear: both;opacity: 0.78"></div>

<div style="clear: both;position: relative; margin: 0 0 0 -34px;width: 1035px;height: 48px;padding: 0 0 10px;
    background: url('http://static.eaw1805.com/images/site/background-parchment-header.png') 0 0 no-repeat;clear: both;"></div>

<div style=" position: relative;margin: -10px 0px 0px -35px;width: 1000px;min-height: 343px;padding: 0px 40px;
background: url('http://static.eaw1805.com/images/site/background-parchment-body.png') repeat-y scroll 0px 0px transparent;clear: both;">

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

    <h1 style="font-size: 42px; width: 945px; text-align: center; padding-top: 10px;">Detailed Politics &amp Foreign
        Relations</h1>

    <table style="width: 985px;">
        <tr valign="top">
            <td width="320" valign="top" style="text-align: justify; padding-right: 40px;">
                Foreign relations with other empires may be the detrimental factor for victory or defeat, irrespective
                of how strong a nation’s economy or army is. Forming alliances, offering right-of-passage or trade
                status, or declaring colonial and European wars: the choices are difficult.
            </td>
            <td width="320" valign="top" style="text-align: justify; padding-right: 40px;">
                Wars between empires do not have to go as far as total annihilation. Politics offer different
                alternatives to end wars: sides can agree to mutual peace, or empires may decide that it is to its
                benefit to surrender to one or more enemies. Politics is crucial towards world dominance.
            </td>
            <td width="320" valign="top" style="text-align: justify; padding-right: 40px;">
                While the fog-of-war system allows for limited knowledge of other countries' condition, the efficient
                use of spies can provide valuable information regarding political relations, economic growth and
                of-course the military forces of your friends and opponents.
            </td>
        </tr>
    </table>

    <div style="padding-top: 10px; padding-bottom: 10px;">
        <a style="font-size: 16px; " href="<c:url value="/help/politics"/>">
            <img src="http://static.eaw1805.com/images/buttons/ButSpyReportsMapOff.png" style="margin-bottom: -13px;">
            Read more about the Politics system</a>
    </div>
</div>
