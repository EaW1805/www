// http://remysharp.com/2009/01/07/html5-enabling-script/
(function () {
    if (!/*@cc_on!@*/0)return;
    var e = "abbr,article,aside,audio,canvas,datalist,details,eventsource,figure,footer,header,hgroup,mark,menu,meter,nav,output,progress,section,time,video".split(','), i = e.length;
    while (i--) {
        document.createElement(e[i])
    }
})();

"use strict";
(function ($) {
    $(document).ready(function () {
        $.fn.qtip.cache = {screen:{scroll:{left:$(window).scrollLeft(), top:$(window).scrollTop()}, width:$(window).width(), height:$(window).height()}};
        var _1, i;
        $(window).bind("resize scroll", function (_2) {
            clearTimeout(_1);
            _1 = setTimeout(function () {
                if (_2.type === "scroll") {
                    $.fn.qtip.cache.screen.scroll = {left:$(window).scrollLeft(), top:$(window).scrollTop()};
                } else {
                    $.fn.qtip.cache.screen.width = $(window).width();
                    $.fn.qtip.cache.screen.height = $(window).height();
                }
                for (i = 0; i < $.fn.qtip.interfaces.length; i++) {
                    var _3 = $.fn.qtip.interfaces[i];
                    if (_3 && _3.status && _3.status.rendered === true && _3.options.position.type !== "static" && (_3.options.position.adjust.scroll && _2.type === "scroll" || _3.options.position.adjust.resize && _2.type === "resize")) {
                        _3.updatePosition(_2, true);
                    }
                }
            }, 100);
        });
        $(document).bind("mousedown.qtip", function (_4) {
            if ($(_4.target).parents("div.qtip").length === 0) {
                $(".qtip[unfocus]").each(function () {
                    var _5 = $(this).qtip("api");
                    if ($(this).is(":visible") && _5 && _5.status && !_5.status.disabled && $(_4.target).add(_5.elements.target).length > 1) {
                        _5.hide(_4);
                    }
                });
            }
        });
    });
    function _6(_7) {
        this.x = String(_7).replace(/middle/i, "center").match(/left|right|center/i)[0].toLowerCase();
        this.y = String(_7).replace(/middle/i, "center").match(/top|bottom|center/i)[0].toLowerCase();
        this.offset = {left:0, top:0};
        this.precedance = (_7.charAt(0).search(/^(t|b)/) > -1) ? "y" : "x";
        this.string = function () {
            return (this.precedance === "y") ? this.y + this.x : this.x + this.y;
        };
    }

    ;
    function _8(_9, _a, _b) {
        var _c = {bottomright:[
            [0, 0],
            [_a, _b],
            [_a, 0]
        ], bottomleft:[
            [0, 0],
            [_a, 0],
            [0, _b]
        ], topright:[
            [0, _b],
            [_a, 0],
            [_a, _b]
        ], topleft:[
            [0, 0],
            [0, _b],
            [_a, _b]
        ], topcenter:[
            [0, _b],
            [_a / 2, 0],
            [_a, _b]
        ], bottomcenter:[
            [0, 0],
            [_a, 0],
            [_a / 2, _b]
        ], rightcenter:[
            [0, 0],
            [_a, _b / 2],
            [0, _b]
        ], leftcenter:[
            [_a, 0],
            [_a, _b],
            [0, _b / 2]
        ]};
        _c.lefttop = _c.bottomright;
        _c.righttop = _c.bottomleft;
        _c.leftbottom = _c.topright;
        _c.rightbottom = _c.topleft;
        return _c[_9];
    }

    ;
    function _d(_e) {
        var _f;
        if ($("<canvas />").get(0).getContext) {
            _f = {topLeft:[_e, _e], topRight:[0, _e], bottomLeft:[_e, 0], bottomRight:[0, 0]};
        } else {
            if ($.browser.msie) {
                _f = {topLeft:[-90, 90, 0], topRight:[-90, 90, -_e], bottomLeft:[90, 270, 0], bottomRight:[90, 270, -_e]};
            }
        }
        return _f;
    }

    ;
    function _10(_11, sub) {
        var _12, i;
        _12 = $.extend(true, {}, _11);
        for (i in _12) {
            if (sub === true && (/(tip|classes)/i).test(i)) {
                delete _12[i];
            } else {
                if (!sub && (/(width|border|tip|title|classes|user)/i).test(i)) {
                    delete _12[i];
                }
            }
        }
        return _12;
    }

    ;
    function _13(_14) {
        if (typeof _14.tip !== "object") {
            _14.tip = {corner:_14.tip};
        }
        if (typeof _14.tip.size !== "object") {
            _14.tip.size = {width:_14.tip.size, height:_14.tip.size};
        }
        if (typeof _14.border !== "object") {
            _14.border = {width:_14.border};
        }
        if (typeof _14.width !== "object") {
            _14.width = {value:_14.width};
        }
        if (typeof _14.width.max === "string") {
            _14.width.max = parseInt(_14.width.max.replace(/([0-9]+)/i, "$1"), 10);
        }
        if (typeof _14.width.min === "string") {
            _14.width.min = parseInt(_14.width.min.replace(/([0-9]+)/i, "$1"), 10);
        }
        if (typeof _14.tip.size.x === "number") {
            _14.tip.size.width = _14.tip.size.x;
            delete _14.tip.size.x;
        }
        if (typeof _14.tip.size.y === "number") {
            _14.tip.size.height = _14.tip.size.y;
            delete _14.tip.size.y;
        }
        return _14;
    }

    ;
    function _15() {
        var _16, i, _17, _18, _19, _1a;
        _16 = this;
        _17 = [true, {}];
        for (i = 0; i < arguments.length; i++) {
            _17.push(arguments[i]);
        }
        _18 = [$.extend.apply($, _17)];
        while (typeof _18[0].name === "string") {
            _18.unshift(_13($.fn.qtip.styles[_18[0].name]));
        }
        _18.unshift(true, {classes:{tooltip:"qtip-" + (arguments[0].name || "defaults")}}, $.fn.qtip.styles.defaults);
        _19 = $.extend.apply($, _18);
        _1a = ($.browser.msie) ? 1 : 0;
        _19.tip.size.width += _1a;
        _19.tip.size.height += _1a;
        if (_19.tip.size.width % 2 > 0) {
            _19.tip.size.width += 1;
        }
        if (_19.tip.size.height % 2 > 0) {
            _19.tip.size.height += 1;
        }
        if (_19.tip.corner === true) {
            if (_16.options.position.corner.tooltip === "center" && _16.options.position.corner.target === "center") {
                _19.tip.corner = false;
            } else {
                _19.tip.corner = _16.options.position.corner.tooltip;
            }
        }
        return _19;
    }

    ;
    function _1b(_1c, _1d, _1e, _1f) {
        var _20 = _1c.get(0).getContext("2d");
        _20.fillStyle = _1f;
        _20.beginPath();
        _20.arc(_1d[0], _1d[1], _1e, 0, Math.PI * 2, false);
        _20.fill();
    }

    ;
    function _21() {
        var _22, i, _23, _24, _25, _26, _27, _28, _29, _2a, _2b, _2c, _2d, _2e, _2f;
        _22 = this;
        _22.elements.wrapper.find(".qtip-borderBottom, .qtip-borderTop").remove();
        _23 = _22.options.style.border.width;
        _24 = _22.options.style.border.radius;
        _25 = _22.options.style.border.color || _22.options.style.tip.color;
        _26 = _d(_24);
        _27 = {};
        for (i in _26) {
            _27[i] = "<div rel=\"" + i + "\" style=\"" + ((/Left/).test(i) ? "left" : "right") + ":0; " + "position:absolute; height:" + _24 + "px; width:" + _24 + "px; overflow:hidden; line-height:0.1px; font-size:1px\">";
            if ($("<canvas />").get(0).getContext) {
                _27[i] += "<canvas height=\"" + _24 + "\" width=\"" + _24 + "\" style=\"vertical-align: top\"></canvas>";
            } else {
                if ($.browser.msie) {
                    _28 = _24 * 2 + 3;
                    _27[i] += "<v:arc stroked=\"false\" fillcolor=\"" + _25 + "\" startangle=\"" + _26[i][0] + "\" endangle=\"" + _26[i][1] + "\" " + "style=\"width:" + _28 + "px; height:" + _28 + "px; margin-top:" + ((/bottom/).test(i) ? -2 : -1) + "px; " + "margin-left:" + ((/Right/).test(i) ? _26[i][2] - 3.5 : -1) + "px; " + "vertical-align:top; display:inline-block; behavior:url(#default#VML)\"></v:arc>";
                }
            }
            _27[i] += "</div>";
        }
        _29 = _22.getDimensions().width - (Math.max(_23, _24) * 2);
        _2a = "<div class=\"qtip-betweenCorners\" style=\"height:" + _24 + "px; width:" + _29 + "px; " + "overflow:hidden; background-color:" + _25 + "; line-height:0.1px; font-size:1px;\">";
        _2b = "<div class=\"qtip-borderTop\" dir=\"ltr\" style=\"height:" + _24 + "px; " + "margin-left:" + _24 + "px; line-height:0.1px; font-size:1px; padding:0;\">" + _27.topLeft + _27.topRight + _2a;
        _22.elements.wrapper.prepend(_2b);
        _2c = "<div class=\"qtip-borderBottom\" dir=\"ltr\" style=\"height:" + _24 + "px; " + "margin-left:" + _24 + "px; line-height:0.1px; font-size:1px; padding:0;\">" + _27.bottomLeft + _27.bottomRight + _2a;
        _22.elements.wrapper.append(_2c);
        if ($("<canvas />").get(0).getContext) {
            _22.elements.wrapper.find("canvas").each(function () {
                _2d = _26[$(this).parent("[rel]:first").attr("rel")];
                _1b.call(_22, $(this), _2d, _24, _25);
            });
        } else {
            if ($.browser.msie) {
                _22.elements.tooltip.append("<v:image style=\"behavior:url(#default#VML);\"></v:image>");
            }
        }
        _2e = Math.max(_24, (_24 + (_23 - _24)));
        _2f = Math.max(_23 - _24, 0);
        _22.elements.contentWrapper.css({border:"0px solid " + _25, borderWidth:_2f + "px " + _2e + "px"});
    }

    ;
    function _30(_31, _32, _33) {
        var _34 = _31.get(0).getContext("2d");
        _34.fillStyle = _33;
        _34.beginPath();
        _34.moveTo(_32[0][0], _32[0][1]);
        _34.lineTo(_32[1][0], _32[1][1]);
        _34.lineTo(_32[2][0], _32[2][1]);
        _34.fill();
    }

    ;
    function _35(_36) {
        var _37, _38, _39, _3a, _3b, _3c;
        _37 = this;
        if (_37.options.style.tip.corner === false || !_37.elements.tip) {
            return;
        }
        if (!_36) {
            _36 = new _6(_37.elements.tip.attr("rel"));
        }
        _38 = _39 = ($.browser.msie) ? 1 : 0;
        _37.elements.tip.css(_36[_36.precedance], 0);
        if (_36.precedance === "y") {
            if ($.browser.msie) {
                if (parseInt($.browser.version.charAt(0), 10) === 6) {
                    _39 = _36.y === "top" ? -3 : 1;
                } else {
                    _39 = _36.y === "top" ? 1 : 2;
                }
            }
            if (_36.x === "center") {
                _37.elements.tip.css({left:"50%", marginLeft:-(_37.options.style.tip.size.width / 2)});
            } else {
                if (_36.x === "left") {
                    _37.elements.tip.css({left:_37.options.style.border.radius - _38});
                } else {
                    _37.elements.tip.css({right:_37.options.style.border.radius + _38});
                }
            }
            if (_36.y === "top") {
                _37.elements.tip.css({top:-_39});
            } else {
                _37.elements.tip.css({bottom:_39});
            }
        } else {
            if ($.browser.msie) {
                _39 = (parseInt($.browser.version.charAt(0), 10) === 6) ? 1 : (_36.x === "left" ? 1 : 2);
            }
            if (_36.y === "center") {
                _37.elements.tip.css({top:"50%", marginTop:-(_37.options.style.tip.size.height / 2)});
            } else {
                if (_36.y === "top") {
                    _37.elements.tip.css({top:_37.options.style.border.radius - _38});
                } else {
                    _37.elements.tip.css({bottom:_37.options.style.border.radius + _38});
                }
            }
            if (_36.x === "left") {
                _37.elements.tip.css({left:-_39});
            } else {
                _37.elements.tip.css({right:_39});
            }
        }
        _3a = "padding-" + _36[_36.precedance];
        _3b = _37.options.style.tip.size[_36.precedance === "x" ? "width" : "height"];
        _37.elements.tooltip.css("padding", 0);
        _37.elements.tooltip.css(_3a, _3b);
        if ($.browser.msie && parseInt($.browser.version.charAt(0), 6) === 6) {
            _3c = parseInt(_37.elements.tip.css("margin-top"), 10) || 0;
            _3c += parseInt(_37.elements.content.css("margin-top"), 10) || 0;
            _37.elements.tip.css({marginTop:_3c});
        }
    }

    ;
    function _3d(_3e) {
        var _3f, _40, _41, _42, _43, tip;
        _3f = this;
        if (_3f.elements.tip !== null) {
            _3f.elements.tip.remove();
        }
        _40 = _3f.options.style.tip.color || _3f.options.style.border.color;
        if (_3f.options.style.tip.corner === false) {
            return;
        } else {
            if (!_3e) {
                _3e = new _6(_3f.options.style.tip.corner);
            }
        }
        _41 = _8(_3e.string(), _3f.options.style.tip.size.width, _3f.options.style.tip.size.height);
        _3f.elements.tip = "<div class=\"" + _3f.options.style.classes.tip + "\" dir=\"ltr\" rel=\"" + _3e.string() + "\" style=\"position:absolute; " + "height:" + _3f.options.style.tip.size.height + "px; width:" + _3f.options.style.tip.size.width + "px; " + "margin:0 auto; line-height:0.1px; font-size:1px;\"></div>";
        _3f.elements.tooltip.prepend(_3f.elements.tip);
        if ($("<canvas />").get(0).getContext) {
            tip = "<canvas height=\"" + _3f.options.style.tip.size.height + "\" width=\"" + _3f.options.style.tip.size.width + "\"></canvas>";
        } else {
            if ($.browser.msie) {
                _42 = _3f.options.style.tip.size.width + "," + _3f.options.style.tip.size.height;
                _43 = "m" + _41[0][0] + "," + _41[0][1];
                _43 += " l" + _41[1][0] + "," + _41[1][1];
                _43 += " " + _41[2][0] + "," + _41[2][1];
                _43 += " xe";
                tip = "<v:shape fillcolor=\"" + _40 + "\" stroked=\"false\" filled=\"true\" path=\"" + _43 + "\" coordsize=\"" + _42 + "\" " + "style=\"width:" + _3f.options.style.tip.size.width + "px; height:" + _3f.options.style.tip.size.height + "px; " + "line-height:0.1px; display:inline-block; behavior:url(#default#VML); " + "vertical-align:" + (_3e.y === "top" ? "bottom" : "top") + "\"></v:shape>";
                tip += "<v:image style=\"behavior:url(#default#VML);\"></v:image>";
                _3f.elements.contentWrapper.css("position", "relative");
            }
        }
        _3f.elements.tip = _3f.elements.tooltip.find("." + _3f.options.style.classes.tip).eq(0);
        _3f.elements.tip.html(tip);
        if ($("<canvas  />").get(0).getContext) {
            _30.call(_3f, _3f.elements.tip.find("canvas:first"), _41, _40);
        }
        if (_3e.y === "top" && $.browser.msie && parseInt($.browser.version.charAt(0), 10) === 6) {
            _3f.elements.tip.css({marginTop:-4});
        }
        _35.call(_3f, _3e);
    }

    ;
    function _44() {
        var _45 = this;
        if (_45.elements.title !== null) {
            _45.elements.title.remove();
        }
        _45.elements.tooltip.attr("aria-labelledby", "qtip-" + _45.id + "-title");
        _45.elements.title = $("<div id=\"qtip-" + _45.id + "-title\" class=\"" + _45.options.style.classes.title + "\"></div>").css(_10(_45.options.style.title, true)).css({zoom:($.browser.msie) ? 1 : 0}).prependTo(_45.elements.contentWrapper);
        if (_45.options.content.title.text) {
            _45.updateTitle.call(_45, _45.options.content.title.text);
        }
        if (_45.options.content.title.button !== false && typeof _45.options.content.title.button === "string") {
            _45.elements.button = $("<a class=\"" + _45.options.style.classes.button + "\" role=\"button\" style=\"float:right; position: relative\"></a>").css(_10(_45.options.style.button, true)).html(_45.options.content.title.button).prependTo(_45.elements.title).click(function (_46) {
                if (!_45.status.disabled) {
                    _45.hide(_46);
                }
            });
        }
    }

    ;
    function _47() {
        var _48, _49, _4a, _4b;
        _48 = this;
        _49 = _48.options.show.when.target;
        _4a = _48.options.hide.when.target;
        if (_48.options.hide.fixed) {
            _4a = _4a.add(_48.elements.tooltip);
        }
        _4b = ["click", "dblclick", "mousedown", "mouseup", "mousemove", "mouseout", "mouseenter", "mouseleave", "mouseover"];
        function _4c(_4d) {
            if (_48.status.disabled === true) {
                return;
            }
            clearTimeout(_48.timers.inactive);
            _48.timers.inactive = setTimeout(function () {
                $(_4b).each(function () {
                    _4a.unbind(this + ".qtip-inactive");
                    _48.elements.content.unbind(this + ".qtip-inactive");
                });
                _48.hide(_4d);
            }, _48.options.hide.delay);
        }

        ;
        if (_48.options.hide.fixed === true) {
            _48.elements.tooltip.bind("mouseover.qtip", function () {
                if (_48.status.disabled === true) {
                    return;
                }
                clearTimeout(_48.timers.hide);
            });
        }
        function _4e(_4f) {
            if (_48.status.disabled === true) {
                return;
            }
            if (_48.options.hide.when.event === "inactive") {
                $(_4b).each(function () {
                    _4a.bind(this + ".qtip-inactive", _4c);
                    _48.elements.content.bind(this + ".qtip-inactive", _4c);
                });
                _4c();
            }
            clearTimeout(_48.timers.show);
            clearTimeout(_48.timers.hide);
            if (_48.options.show.delay > 0) {
                _48.timers.show = setTimeout(function () {
                    _48.show(_4f);
                }, _48.options.show.delay);
            } else {
                _48.show(_4f);
            }
        }

        ;
        function _50(_51) {
            if (_48.status.disabled === true) {
                return;
            }
            if (_48.options.hide.fixed === true && (/mouse(out|leave)/i).test(_48.options.hide.when.event) && $(_51.relatedTarget).parents("div.qtip[id^=\"qtip\"]").length > 0) {
                _51.stopPropagation();
                _51.preventDefault();
                clearTimeout(_48.timers.hide);
                return false;
            }
            clearTimeout(_48.timers.show);
            clearTimeout(_48.timers.hide);
            _48.elements.tooltip.stop(true, true);
            _48.timers.hide = setTimeout(function () {
                _48.hide(_51);
            }, _48.options.hide.delay);
        }

        ;
        if ((_48.options.show.when.target.add(_48.options.hide.when.target).length === 1 && _48.options.show.when.event === _48.options.hide.when.event && _48.options.hide.when.event !== "inactive") || _48.options.hide.when.event === "unfocus") {
            _48.cache.toggle = 0;
            _49.bind(_48.options.show.when.event + ".qtip", function (_52) {
                if (_48.cache.toggle === 0) {
                    _4e(_52);
                } else {
                    _50(_52);
                }
            });
        } else {
            _49.bind(_48.options.show.when.event + ".qtip", _4e);
            if (_48.options.hide.when.event !== "inactive") {
                _4a.bind(_48.options.hide.when.event + ".qtip", _50);
            }
        }
        if ((/(fixed|absolute)/).test(_48.options.position.type)) {
            _48.elements.tooltip.bind("mouseover.qtip", _48.focus);
        }
        if (_48.options.position.target === "mouse" && _48.options.position.type !== "static") {
            _49.bind("mousemove.qtip", function (_53) {
                _48.cache.mouse = {x:_53.pageX, y:_53.pageY};
                if (_48.status.disabled === false && _48.options.position.adjust.mouse === true && _48.options.position.type !== "static" && _48.elements.tooltip.css("display") !== "none") {
                    _48.updatePosition(_53);
                }
            });
        }
    }

    ;
    function _54() {
        var _55, _56, _57;
        _55 = this;
        _57 = _55.getDimensions();
        _56 = "<iframe class=\"qtip-bgiframe\" frameborder=\"0\" tabindex=\"-1\" src=\"javascript:false\" " + "style=\"display:block; position:absolute; z-index:-1; filter:alpha(opacity='0'); border: 1px solid red; " + "height:" + _57.height + "px; width:" + _57.width + "px\" />";
        _55.elements.bgiframe = _55.elements.wrapper.prepend(_56).children(".qtip-bgiframe:first");
    }

    ;
    function _58() {
        var _59, _5a, url, _5b, _5c;
        _59 = this;
        _59.beforeRender.call(_59);
        _59.status.rendered = true;
        _59.elements.tooltip = "<div qtip=\"" + _59.id + "\" id=\"qtip-" + _59.id + "\" role=\"tooltip\" " + "aria-describedby=\"qtip-" + _59.id + "-content\" class=\"qtip " + (_59.options.style.classes.tooltip || _59.options.style) + "\" " + "style=\"display:none; -moz-border-radius:0; -webkit-border-radius:0; border-radius:0; position:" + _59.options.position.type + ";\"> " + "  <div class=\"qtip-wrapper\" style=\"position:relative; overflow:hidden; text-align:left;\"> " + "    <div class=\"qtip-contentWrapper\" style=\"overflow:hidden;\"> " + "       <div id=\"qtip-" + _59.id + "-content\" class=\"qtip-content " + _59.options.style.classes.content + "\"></div> " + "</div></div></div>";
        _59.elements.tooltip = $(_59.elements.tooltip);
        _59.elements.tooltip.appendTo(_59.options.position.container);
        _59.elements.tooltip.data("qtip", {current:0, interfaces:[_59]});
        _59.elements.wrapper = _59.elements.tooltip.children("div:first");
        _59.elements.contentWrapper = _59.elements.wrapper.children("div:first").css({background:_59.options.style.background});
        _59.elements.content = _59.elements.contentWrapper.children("div:first").css(_10(_59.options.style));
        if ($.browser.msie) {
            _59.elements.wrapper.add(_59.elements.content).css({zoom:1});
        }
        if (_59.options.hide.when.event === "unfocus") {
            _59.elements.tooltip.attr("unfocus", true);
        }
        if (typeof _59.options.style.width.value === "number") {
            _59.updateWidth();
        }
        if ($("<canvas />").get(0).getContext || $.browser.msie) {
            if (_59.options.style.border.radius > 0) {
                _21.call(_59);
            } else {
                _59.elements.contentWrapper.css({border:_59.options.style.border.width + "px solid " + _59.options.style.border.color});
            }
            if (_59.options.style.tip.corner !== false) {
                _3d.call(_59);
            }
        } else {
            _59.elements.contentWrapper.css({border:_59.options.style.border.width + "px solid " + _59.options.style.border.color});
            _59.options.style.border.radius = 0;
            _59.options.style.tip.corner = false;
        }
        if ((typeof _59.options.content.text === "string" && _59.options.content.text.length > 0) || (_59.options.content.text.jquery && _59.options.content.text.length > 0)) {
            _5a = _59.options.content.text;
        } else {
            if (typeof _59.elements.target.attr("title") === "string" && _59.elements.target.attr("title").length > 0) {
                _5a = _59.elements.target.attr("title").replace(/\n/gi, "<br />");
                _59.elements.target.attr("title", "");
            } else {
                if (typeof _59.elements.target.attr("alt") === "string" && _59.elements.target.attr("alt").length > 0) {
                    _5a = _59.elements.target.attr("alt").replace(/\n/gi, "<br />");
                    _59.elements.target.attr("alt", "");
                } else {
                    _5a = " ";
                }
            }
        }
        if (_59.options.content.title.text !== false) {
            _44.call(_59);
        }
        _59.updateContent(_5a);
        _47.call(_59);
        if (_59.options.show.ready === true) {
            _59.show();
        }
        if (_59.options.content.url !== false) {
            url = _59.options.content.url;
            _5b = _59.options.content.data;
            _5c = _59.options.content.method || "get";
            _59.loadContent(url, _5b, _5c);
        }
        _59.onRender.call(_59);
    }

    ;
    function _5d(_5e, _5f, id) {
        var _60 = this;
        _60.id = id;
        _60.options = _5f;
        _60.status = {animated:false, rendered:false, disabled:false, focused:false};
        _60.elements = {target:_5e.addClass(_60.options.style.classes.target), tooltip:null, wrapper:null, content:null, contentWrapper:null, title:null, button:null, tip:null, bgiframe:null};
        _60.cache = {mouse:{}, position:{}, toggle:0};
        _60.timers = {};
        $.extend(_60, _60.options.api, {show:function (_61) {
            var _62, _63;
            if (!_60.status.rendered) {
                return false;
            }
            if (_60.elements.tooltip.css("display") !== "none") {
                return _60;
            }
            _60.elements.tooltip.stop(true, false);
            _62 = _60.beforeShow.call(_60, _61);
            if (_62 === false) {
                return _60;
            }
            function _64() {
                _60.elements.tooltip.attr("aria-hidden", true);
                if (_60.options.position.type !== "static") {
                    _60.focus();
                }
                _60.onShow.call(_60, _61);
                if ($.browser.msie) {
                    _60.elements.tooltip.get(0).style.removeAttribute("filter");
                }
                _60.elements.tooltip.css({opacity:""});
            }

            ;
            _60.cache.toggle = 1;
            if (_60.options.position.type !== "static") {
                _60.updatePosition(_61, (_60.options.show.effect.length > 0));
            }
            if (typeof _60.options.show.solo === "object") {
                _63 = $(_60.options.show.solo);
            } else {
                if (_60.options.show.solo === true) {
                    _63 = $("div.qtip").not(_60.elements.tooltip);
                }
            }
            if (_63) {
                _63.each(function () {
                    if ($(this).qtip("api").status.rendered === true) {
                        $(this).qtip("api").hide();
                    }
                });
            }
            if (typeof _60.options.show.effect.type === "function") {
                _60.options.show.effect.type.call(_60.elements.tooltip, _60.options.show.effect.length);
                _60.elements.tooltip.queue(function () {
                    _64();
                    $(this).dequeue();
                });
            } else {
                switch (_60.options.show.effect.type.toLowerCase()) {
                    case "fade":
                        _60.elements.tooltip.fadeIn(_60.options.show.effect.length, _64);
                        break;
                    case "slide":
                        _60.elements.tooltip.slideDown(_60.options.show.effect.length, function () {
                            _64();
                            if (_60.options.position.type !== "static") {
                                _60.updatePosition(_61, true);
                            }
                        });
                        break;
                    case "grow":
                        _60.elements.tooltip.show(_60.options.show.effect.length, _64);
                        break;
                    default:
                        _60.elements.tooltip.show(null, _64);
                        break;
                }
                _60.elements.tooltip.addClass(_60.options.style.classes.active);
            }
            return _60;
        }, hide:function (_65) {
            var _66;
            if (!_60.status.rendered) {
                return false;
            } else {
                if (_60.elements.tooltip.css("display") === "none") {
                    return _60;
                }
            }
            clearTimeout(_60.timers.show);
            _60.elements.tooltip.stop(true, false);
            _66 = _60.beforeHide.call(_60, _65);
            if (_66 === false) {
                return _60;
            }
            function _67() {
                _60.elements.tooltip.attr("aria-hidden", true);
                _60.elements.tooltip.css({opacity:""});
                _60.onHide.call(_60, _65);
            }

            ;
            _60.cache.toggle = 0;
            if (typeof _60.options.hide.effect.type === "function") {
                _60.options.hide.effect.type.call(_60.elements.tooltip, _60.options.hide.effect.length);
                _60.elements.tooltip.queue(function () {
                    _67();
                    $(this).dequeue();
                });
            } else {
                switch (_60.options.hide.effect.type.toLowerCase()) {
                    case "fade":
                        _60.elements.tooltip.fadeOut(_60.options.hide.effect.length, _67);
                        break;
                    case "slide":
                        _60.elements.tooltip.slideUp(_60.options.hide.effect.length, _67);
                        break;
                    case "grow":
                        _60.elements.tooltip.hide(_60.options.hide.effect.length, _67);
                        break;
                    default:
                        _60.elements.tooltip.hide(null, _67);
                        break;
                }
                _60.elements.tooltip.removeClass(_60.options.style.classes.active);
            }
            return _60;
        }, toggle:function (_68, _69) {
            var _6a = /boolean|number/.test(typeof _69) ? _69 : !_60.elements.tooltip.is(":visible");
            _60[_6a ? "show" : "hide"](_68);
            return _60;
        }, updatePosition:function (_6b, _6c) {
            if (!_60.status.rendered) {
                return false;
            }
            var _6d = $(_5f.position.target), _6e = _5f.position, _6f = _60.elements.tooltip.width(), _70 = _60.elements.tooltip.height(), _71, _72, _73, my, at, _74, _75, i, _76, _77, _78 = {left:function () {
                var _79 = _73.left + _6f - $(window).width() - $(window).scrollLeft(), _7a = my.x === "left" ? -_6f : my.x === "right" ? _6f : 0, _7b = -2 * _6e.adjust.x;
                _73.left += _73.left < 0 ? _7a + _71 + _7b : _79 > 0 ? _7a - _71 + _7b : 0;
                return Math.round(_79);
            }, top:function () {
                var _7c = _73.top + _70 - $(window).height() - $(window).scrollTop(), _7d = my.y === "top" ? -_70 : my.y === "bottom" ? _70 : 0, _7e = at.y === "top" ? _72 : at.y === "bottom" ? -_72 : 0, _7f = -2 * _6e.adjust.y;
                _73.top += _73.top < 0 ? _7d + _72 + _7f : _7c > 0 ? _7d + _7e + _7f : 0;
                return Math.round(_7c);
            }};
            my = _5f.position.corner.tooltip;
            at = _5f.position.corner.target;
            if (_6b && _5f.position.target === "mouse") {
                at = {x:"left", y:"top"};
                _71 = _72 = 0;
                _73 = {top:_6b.pageY, left:_6b.pageX};
            } else {
                if (_6d[0] === document) {
                    _71 = _6d.width();
                    _72 = _6d.height();
                    _73 = {top:0, left:0};
                } else {
                    if (_6d[0] === window) {
                        _71 = _6d.width();
                        _72 = _6d.height();
                        _73 = {top:_6d.scrollTop(), left:_6d.scrollLeft()};
                    } else {
                        if (_6d.is("area")) {
                            _75 = _60.options.position.target.attr("coords").split(",");
                            for (i = 0; i < _75.length; i++) {
                                _75[i] = parseInt(_75[i], 10);
                            }
                            _76 = _60.options.position.target.parent("map").attr("name");
                            _77 = $("images[usemap=\"#" + _76 + "\"]:first").offset();
                            _6d.position = {left:Math.floor(_77.left + _75[0]), top:Math.floor(_77.top + _75[1])};
                            switch (_60.options.position.target.attr("shape").toLowerCase()) {
                                case "rect":
                                    _71 = Math.ceil(Math.abs(_75[2] - _75[0]));
                                    _72 = Math.ceil(Math.abs(_75[3] - _75[1]));
                                    break;
                                case "circle":
                                    _71 = _75[2] + 1;
                                    _72 = _75[2] + 1;
                                    break;
                                case "poly":
                                    _71 = _75[0];
                                    _72 = _75[1];
                                    for (i = 0; i < _75.length; i++) {
                                        if (i % 2 === 0) {
                                            if (_75[i] > _71) {
                                                _71 = _75[i];
                                            }
                                            if (_75[i] < _75[0]) {
                                                _73.left = Math.floor(_77.left + _75[i]);
                                            }
                                        } else {
                                            if (_75[i] > _72) {
                                                _72 = _75[i];
                                            }
                                            if (_75[i] < _75[1]) {
                                                _73.top = Math.floor(_77.top + _75[i]);
                                            }
                                        }
                                    }
                                    _71 = _71 - (_73.left - _77.left);
                                    _72 = _72 - (_73.top - _77.top);
                                    break;
                            }
                            _71 -= 2;
                            _72 -= 2;
                        } else {
                            _71 = _6d.outerWidth();
                            _72 = _6d.outerHeight();
                            _73 = _6d.offset();
                        }
                    }
                }
                _73.left += at.x === "right" ? _71 : at.x === "center" ? _71 / 2 : 0;
                _73.top += at.y === "bottom" ? _72 : at.y === "center" ? _72 / 2 : 0;
            }
            _73.left += _6e.adjust.x + (my.x === "right" ? -_6f : my.x === "center" ? -_6f / 2 : 0);
            _73.top += _6e.adjust.y + (my.y === "bottom" ? -_70 : my.y === "center" ? -_70 / 2 : 0);
            if (_60.options.style.border.radius > 0) {
                if (my.x === "left") {
                    _73.left -= _60.options.style.border.radius;
                } else {
                    if (my.x === "right") {
                        _73.left += _60.options.style.border.radius;
                    }
                }
                if (my.y === "top") {
                    _73.top -= _60.options.style.border.radius;
                } else {
                    if (my.y === "bottom") {
                        _73.top += _60.options.style.border.radius;
                    }
                }
            }
            if (_6e.adjust.screen) {
                _78.left();
                _78.top();
            }
            if (!_60.elements.bgiframe && $.browser.msie && parseInt($.browser.version.charAt(0), 10) === 6) {
                _54.call(_60);
            }
            _74 = _60.beforePositionUpdate.call(_60, _6b);
            if (_74 === false) {
                return _60;
            }
            _60.cache.position = _73;
            if (_6c === true) {
                _60.status.animated = true;
                _60.elements.tooltip.animate(_73, 200, "swing", function () {
                    _60.status.animated = false;
                });
            } else {
                _60.elements.tooltip.css(_73);
            }
            _60.onPositionUpdate.call(_60, _6b);
            return _60;
        }, updateWidth:function (_80) {
            if (!_60.status.rendered || (_80 && typeof _80 !== "number")) {
                return false;
            }
            var _81 = _60.elements.contentWrapper.siblings().add(_60.elements.tip).add(_60.elements.button), _82 = _60.elements.wrapper.add(_60.elements.contentWrapper.children()), _83 = _60.elements.tooltip, max = _60.options.style.width.max, min = _60.options.style.width.min;
            if (!_80) {
                if (typeof _60.options.style.width.value === "number") {
                    _80 = _60.options.style.width.value;
                } else {
                    _60.elements.tooltip.css({width:"auto"});
                    _81.hide();
                    _83.width(_80);
                    if ($.browser.msie) {
                        _82.css({zoom:""});
                    }
                    _80 = _60.getDimensions().width;
                    if (!_60.options.style.width.value) {
                        _80 = Math.min(Math.max(_80, min), max);
                    }
                }
            }
            if (_80 % 2) {
                _80 -= 1;
            }
            _60.elements.tooltip.width(_80);
            _81.show();
            if (_60.options.style.border.radius) {
                _60.elements.tooltip.find(".qtip-betweenCorners").each(function (i) {
                    $(this).width(_80 - (_60.options.style.border.radius * 2));
                });
            }
            if ($.browser.msie) {
                _82.css({zoom:1});
                _60.elements.wrapper.width(_80);
                if (_60.elements.bgiframe) {
                    _60.elements.bgiframe.width(_80).height(_60.getDimensions.height);
                }
            }
            return _60;
        }, updateStyle:function (_84) {
            var tip, _85, _86, _87, _88;
            if (!_60.status.rendered || typeof _84 !== "string" || !$.fn.qtip.styles[_84]) {
                return false;
            }
            _60.options.style = _15.call(_60, $.fn.qtip.styles[_84], _60.options.user.style);
            _60.elements.content.css(_10(_60.options.style));
            if (_60.options.content.title.text !== false) {
                _60.elements.title.css(_10(_60.options.style.title, true));
            }
            _60.elements.contentWrapper.css({borderColor:_60.options.style.border.color});
            if (_60.options.style.tip.corner !== false) {
                if ($("<canvas />").get(0).getContext) {
                    tip = _60.elements.tooltip.find(".qtip-tip canvas:first");
                    _86 = tip.get(0).getContext("2d");
                    _86.clearRect(0, 0, 300, 300);
                    _87 = tip.parent("div[rel]:first").attr("rel");
                    _88 = _8(_87, _60.options.style.tip.size.width, _60.options.style.tip.size.height);
                    _30.call(_60, tip, _88, _60.options.style.tip.color || _60.options.style.border.color);
                } else {
                    if ($.browser.msie) {
                        tip = _60.elements.tooltip.find(".qtip-tip [nodeName=\"shape\"]");
                        tip.attr("fillcolor", _60.options.style.tip.color || _60.options.style.border.color);
                    }
                }
            }
            if (_60.options.style.border.radius > 0) {
                _60.elements.tooltip.find(".qtip-betweenCorners").css({backgroundColor:_60.options.style.border.color});
                if ($("<canvas />").get(0).getContext) {
                    _85 = _d(_60.options.style.border.radius);
                    _60.elements.tooltip.find(".qtip-wrapper canvas").each(function () {
                        _86 = $(this).get(0).getContext("2d");
                        _86.clearRect(0, 0, 300, 300);
                        _87 = $(this).parent("div[rel]:first").attr("rel");
                        _1b.call(_60, $(this), _85[_87], _60.options.style.border.radius, _60.options.style.border.color);
                    });
                } else {
                    if ($.browser.msie) {
                        _60.elements.tooltip.find(".qtip-wrapper [nodeName=\"arc\"]").each(function () {
                            $(this).attr("fillcolor", _60.options.style.border.color);
                        });
                    }
                }
            }
            return _60;
        }, updateContent:function (_89, _8a) {
            var _8b, _8c, _8d;

            function _8e() {
                _60.updateWidth();
                if (_8a !== false) {
                    if (_60.options.position.type !== "static") {
                        _60.updatePosition(_60.elements.tooltip.is(":visible"), true);
                    }
                    if (_60.options.style.tip.corner !== false) {
                        _35.call(_60);
                    }
                }
            }

            ;
            if (!_60.status.rendered || !_89) {
                return false;
            }
            _8b = _60.beforeContentUpdate.call(_60, _89);
            if (typeof _8b === "string") {
                _89 = _8b;
            } else {
                if (_8b === false) {
                    return;
                }
            }
            if ($.browser.msie) {
                _60.elements.contentWrapper.children().css({zoom:"normal"});
            }
            if (_89.jquery && _89.length > 0) {
                _89.clone(true).appendTo(_60.elements.content).show();
            } else {
                _60.elements.content.html(_89);
            }
            _8c = _60.elements.content.find("images[complete=false]");
            if (_8c.length > 0) {
                _8d = 0;
                _8c.each(function (i) {
                    $("<images src=\"" + $(this).attr("src") + "\" />").load(function () {
                        if (++_8d === _8c.length) {
                            _8e();
                        }
                    });
                });
            } else {
                _8e();
            }
            _60.onContentUpdate.call(_60);
            return _60;
        }, loadContent:function (url, _8f, _90) {
            var _91;

            function _92(_93) {
                _60.onContentLoad.call(_60);
                _60.updateContent(_93);
            }

            ;
            if (!_60.status.rendered) {
                return false;
            }
            _91 = _60.beforeContentLoad.call(_60);
            if (_91 === false) {
                return _60;
            }
            if (_90 === "post") {
                $.post(url, _8f, _92);
            } else {
                $.get(url, _8f, _92);
            }
            return _60;
        }, updateTitle:function (_94) {
            var _95;
            if (!_60.status.rendered || !_94) {
                return false;
            }
            _95 = _60.beforeTitleUpdate.call(_60);
            if (_95 === false) {
                return _60;
            }
            if (_60.elements.button) {
                _60.elements.button = _60.elements.button.clone(true);
            }
            _60.elements.title.html(_94);
            if (_60.elements.button) {
                _60.elements.title.prepend(_60.elements.button);
            }
            _60.onTitleUpdate.call(_60);
            return _60;
        }, focus:function (_96) {
            var _97, _98, _99, _9a;
            if (!_60.status.rendered || _60.options.position.type === "static") {
                return false;
            }
            _97 = parseInt(_60.elements.tooltip.css("z-index"), 10);
            _98 = 15000 + $("div.qtip[id^=\"qtip\"]").length - 1;
            if (!_60.status.focused && _97 !== _98) {
                _9a = _60.beforeFocus.call(_60, _96);
                if (_9a === false) {
                    return _60;
                }
                $("div.qtip[id^=\"qtip\"]").not(_60.elements.tooltip).each(function () {
                    if ($(this).qtip("api").status.rendered === true) {
                        _99 = parseInt($(this).css("z-index"), 10);
                        if (typeof _99 === "number" && _99 > -1) {
                            $(this).css({zIndex:parseInt($(this).css("z-index"), 10) - 1});
                        }
                        $(this).qtip("api").status.focused = false;
                    }
                });
                _60.elements.tooltip.css({zIndex:_98});
                _60.status.focused = true;
                _60.onFocus.call(_60, _96);
            }
            return _60;
        }, disable:function (_9b) {
            if (!_60.status.rendered) {
                return false;
            }
            _60.status.disabled = _9b ? true : false;
            return _60;
        }, destroy:function () {
            var i, _9c, _9d;
            _9c = _60.beforeDestroy.call(_60);
            if (_9c === false) {
                return _60;
            }
            if (_60.status.rendered) {
                _60.options.show.when.target.unbind("mousemove.qtip", _60.updatePosition);
                _60.options.show.when.target.unbind("mouseout.qtip", _60.hide);
                _60.options.show.when.target.unbind(_60.options.show.when.event + ".qtip");
                _60.options.hide.when.target.unbind(_60.options.hide.when.event + ".qtip");
                _60.elements.tooltip.unbind(_60.options.hide.when.event + ".qtip");
                _60.elements.tooltip.unbind("mouseover.qtip", _60.focus);
                _60.elements.tooltip.remove();
            } else {
                _60.options.show.when.target.unbind(_60.options.show.when.event + ".qtip-create");
            }
            if (typeof _60.elements.target.data("qtip") === "object") {
                _9d = _60.elements.target.data("qtip").interfaces;
                if (typeof _9d === "object" && _9d.length > 0) {
                    for (i = 0; i < _9d.length - 1; i++) {
                        if (_9d[i].id === _60.id) {
                            _9d.splice(i, 1);
                        }
                    }
                }
            }
            $.fn.qtip.interfaces.splice(_60.id, 1);
            if (typeof _9d === "object" && _9d.length > 0) {
                _60.elements.target.data("qtip").current = _9d.length - 1;
            } else {
                _60.elements.target.removeData("qtip");
            }
            _60.onDestroy.call(_60);
            return _60.elements.target;
        }, getPosition:function () {
            var _9e, _9f;
            if (!_60.status.rendered) {
                return false;
            }
            _9e = (_60.elements.tooltip.css("display") !== "none") ? false : true;
            if (_9e) {
                _60.elements.tooltip.css({visiblity:"hidden"}).show();
            }
            _9f = _60.elements.tooltip.offset();
            if (_9e) {
                _60.elements.tooltip.css({visiblity:"visible"}).hide();
            }
            return _9f;
        }, getDimensions:function () {
            var _a0, _a1;
            if (!_60.status.rendered) {
                return false;
            }
            _a0 = (!_60.elements.tooltip.is(":visible")) ? true : false;
            if (_a0) {
                _60.elements.tooltip.css({visiblity:"hidden"}).show();
            }
            _a1 = {height:_60.elements.tooltip.outerHeight(), width:_60.elements.tooltip.outerWidth()};
            if (_a0) {
                _60.elements.tooltip.css({visiblity:"visible"}).hide();
            }
            return _a1;
        }});
    }

    ;
    $.fn.qtip = function (_a2, _a3) {
        var i, id, _a4, _a5, obj, _a6, _a7, api;
        if (typeof _a2 === "string") {
            if (_a2 === "api") {
                return $(this).data("qtip").interfaces[$(this).data("qtip").current];
            } else {
                if (_a2 === "interfaces") {
                    return $(this).data("qtip").interfaces;
                }
            }
        } else {
            if (!_a2) {
                _a2 = {};
            }
            if (typeof _a2.content !== "object" || (_a2.content.jquery && _a2.content.length > 0)) {
                _a2.content = {text:_a2.content};
            }
            if (typeof _a2.content.title !== "object") {
                _a2.content.title = {text:_a2.content.title};
            }
            if (typeof _a2.position !== "object") {
                _a2.position = {corner:_a2.position};
            }
            if (typeof _a2.position.corner !== "object") {
                _a2.position.corner = {target:_a2.position.corner, tooltip:_a2.position.corner};
            }
            if (typeof _a2.show !== "object") {
                _a2.show = {when:_a2.show};
            }
            if (typeof _a2.show.when !== "object") {
                _a2.show.when = {event:_a2.show.when};
            }
            if (typeof _a2.show.effect !== "object") {
                _a2.show.effect = {type:_a2.show.effect};
            }
            if (typeof _a2.hide !== "object") {
                _a2.hide = {when:_a2.hide};
            }
            if (typeof _a2.hide.when !== "object") {
                _a2.hide.when = {event:_a2.hide.when};
            }
            if (typeof _a2.hide.effect !== "object") {
                _a2.hide.effect = {type:_a2.hide.effect};
            }
            if (typeof _a2.style !== "object") {
                _a2.style = {name:_a2.style};
            }
            _a2.style = _13(_a2.style);
            _a5 = $.extend(true, {}, $.fn.qtip.defaults, _a2);
            _a5.style = _15.call({options:_a5}, _a5.style);
            _a5.user = $.extend(true, {}, _a2);
        }
        return $(this).each(function () {
            if (typeof _a2 === "string") {
                _a6 = _a2.toLowerCase();
                _a4 = $(this).qtip("interfaces");
                if (typeof _a4 === "object") {
                    if (_a3 === true && _a6 === "destroy") {
                        while (_a4.length > 0) {
                            _a4[_a4.length - 1].destroy();
                        }
                    } else {
                        if (_a3 !== true) {
                            _a4 = [$(this).qtip("api")];
                        }
                        for (i = 0; i < _a4.length; i++) {
                            if (_a6 === "destroy") {
                                _a4[i].destroy();
                            } else {
                                if (_a4[i].status.rendered === true) {
                                    if (_a6 === "show") {
                                        _a4[i].show();
                                    } else {
                                        if (_a6 === "hide") {
                                            _a4[i].hide();
                                        } else {
                                            if (_a6 === "focus") {
                                                _a4[i].focus();
                                            } else {
                                                if (_a6 === "disable") {
                                                    _a4[i].disable(true);
                                                } else {
                                                    if (_a6 === "enable") {
                                                        _a4[i].disable(false);
                                                    } else {
                                                        if (_a6 === "update") {
                                                            _a4[i].updatePosition();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                _a7 = $.extend(true, {}, _a5);
                _a7.hide.effect.length = _a5.hide.effect.length;
                _a7.show.effect.length = _a5.show.effect.length;
                if (_a7.position.container === false) {
                    _a7.position.container = $(document.body);
                }
                if (_a7.position.target === false) {
                    _a7.position.target = $(this);
                }
                if (_a7.show.when.target === false) {
                    _a7.show.when.target = $(this);
                }
                if (_a7.hide.when.target === false) {
                    _a7.hide.when.target = $(this);
                }
                _a7.position.corner.tooltip = new _6(_a7.position.corner.tooltip);
                _a7.position.corner.target = new _6(_a7.position.corner.target);
                id = $.fn.qtip.interfaces.length;
                for (i = 0; i < id; i++) {
                    if (typeof $.fn.qtip.interfaces[i] === "undefined") {
                        id = i;
                        break;
                    }
                }
                obj = new _5d($(this), _a7, id);
                $.fn.qtip.interfaces[id] = obj;
                if (typeof $(this).data("qtip") === "object" && $(this).data("qtip")) {
                    if (typeof $(this).attr("qtip") === "undefined") {
                        $(this).data("qtip").current = $(this).data("qtip").interfaces.length;
                    }
                    $(this).data("qtip").interfaces.push(obj);
                } else {
                    $(this).data("qtip", {current:0, interfaces:[obj]});
                }
                if (_a7.content.prerender === false && _a7.show.when.event !== false && _a7.show.ready !== true) {
                    _a7.show.when.target.bind(_a7.show.when.event + ".qtip-" + id + "-create", {qtip:id}, function (_a8) {
                        api = $.fn.qtip.interfaces[_a8.data.qtip];
                        api.options.show.when.target.unbind(api.options.show.when.event + ".qtip-" + _a8.data.qtip + "-create");
                        api.cache.mouse = {x:_a8.pageX, y:_a8.pageY};
                        _58.call(api);
                        api.options.show.when.target.trigger(api.options.show.when.event);
                    });
                } else {
                    obj.cache.mouse = {x:_a7.show.when.target.offset().left, y:_a7.show.when.target.offset().top};
                    _58.call(obj);
                }
            }
        });
    };
    $.fn.qtip.interfaces = [];
    $.fn.qtip.log = {error:function () {
        return this;
    }};
    $.fn.qtip.constants = {};
    $.fn.qtip.defaults = {content:{prerender:false, text:false, url:false, data:null, title:{text:false, button:false}}, position:{target:false, corner:{target:"bottomRight", tooltip:"topLeft"}, adjust:{x:0, y:0, mouse:true, screen:false, scroll:true, resize:true}, type:"absolute", container:false}, show:{when:{target:false, event:"mouseover"}, effect:{type:"fade", length:100}, delay:140, solo:false, ready:false}, hide:{when:{target:false, event:"mouseout"}, effect:{type:"fade", length:100}, delay:0, fixed:false}, api:{beforeRender:function () {
    }, onRender:function () {
    }, beforePositionUpdate:function () {
    }, onPositionUpdate:function () {
    }, beforeShow:function () {
    }, onShow:function () {
    }, beforeHide:function () {
    }, onHide:function () {
    }, beforeContentUpdate:function () {
    }, onContentUpdate:function () {
    }, beforeContentLoad:function () {
    }, onContentLoad:function () {
    }, beforeTitleUpdate:function () {
    }, onTitleUpdate:function () {
    }, beforeDestroy:function () {
    }, onDestroy:function () {
    }, beforeFocus:function () {
    }, onFocus:function () {
    }}};
    $.fn.qtip.styles = {defaults:{background:"white", color:"#111", overflow:"hidden", textAlign:"left", width:{min:0, max:250}, padding:"5px 9px", border:{width:1, radius:0, color:"#d3d3d3"}, tip:{corner:false, color:false, size:{width:13, height:13}, opacity:1}, title:{background:"#e1e1e1", fontWeight:"bold", padding:"7px 12px"}, button:{cursor:"pointer"}, classes:{target:"", tip:"qtip-tip", title:"qtip-title", button:"qtip-button", content:"qtip-content", active:"qtip-active"}}, cream:{border:{width:3, radius:0, color:"#F9E98E"}, title:{background:"#F0DE7D", color:"#A27D35"}, background:"#FBF7AA", color:"#A27D35", classes:{tooltip:"qtip-cream"}}, light:{border:{width:3, radius:0, color:"#E2E2E2"}, title:{background:"#f1f1f1", color:"#454545"}, background:"white", color:"#454545", classes:{tooltip:"qtip-light"}}, dark:{border:{width:3, radius:0, color:"#303030"}, title:{background:"#404040", color:"#f3f3f3"}, background:"#505050", color:"#f3f3f3", classes:{tooltip:"qtip-dark"}}, red:{border:{width:3, radius:0, color:"#CE6F6F"}, title:{background:"#f28279", color:"#9C2F2F"}, background:"#F79992", color:"#9C2F2F", classes:{tooltip:"qtip-red"}}, green:{border:{width:3, radius:0, color:"#A9DB66"}, title:{background:"#b9db8c", color:"#58792E"}, background:"#CDE6AC", color:"#58792E", classes:{tooltip:"qtip-green"}}, blue:{border:{width:3, radius:0, color:"#ADD9ED"}, title:{background:"#D0E9F5", color:"#5E99BD"}, background:"#E5F6FE", color:"#4D9FBF", classes:{tooltip:"qtip-blue"}}};
}(jQuery));


(function ($) {

    $('a.liame').each(function () {
        var e = this.rel.replace('/', '@');
        var ee = e.replace('#', '.com');
        this.href = 'mailto:' + ee;
        this.title = ee;
    });

//setup defaults for qTip (tooltip)
    $.fn.qtip.styles.tooltip = {
        width:{min:0, max:500},
        background:'#CC6633',
        color:'#fff',
        textAlign:'left',
        padding:'10px',
        border:{
            width:2,
            radius:3,
            color:'#CC6633'
        },
        tip:{
            corner:'leftMiddle',
            color:'#CC6633',
            size:{
                x:8,
                y:12
            }
        }

    }

    $.fn.qtip.defaults.show.delay = 0;
    $.fn.qtip.defaults.position.corner = {target:'rightMiddle', tooltip:'leftMiddle'};
    $.fn.qtip.defaults.position.adjust.screen = true;

//invoke tooltip
    $('.toolTip').qtip({content:false, style:'tooltip'});

})(jQuery)


