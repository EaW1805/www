/* Copyright (C) YOOtheme GmbH, YOOtheme Proprietary Use License (http://www.yootheme.com/license) */

/*
 Lightbox Plugin is based on Fancybox (http://fancybox.net, Janis Skarnelis, MIT License)
 */
(function (b) {
    var i, l, t, p, e, B, k, A, f, x, y, D, q = 0, c = {}, m = [], n = 0, a = {}, j = [], C = null, s = new Image, E, F = 1, G = /\.(jpg|gif|png|bmp|jpeg)(.*)?$/i, K = /[^\.]\.(swf)\s*$/i, H = /(\/\/.*?youtube\.[a-z]+)\/watch\?v=([^&]+)&?(.*)/, I = /(\/\/.*?)vimeo\.[a-z]+\/([0-9]+).*?/, L = /\.(mp4|ogv|webm|flv)(.*)?$/i, w = 0, u = "", r, g, h = !1, z = b.extend(b("<div/>")[0], {prop:0});
    _abort = function () {
        t.hide();
        s.onerror = s.onload = null;
        C && C.abort();
        l.empty()
    };
    _error = function () {
        !1 === c.onError(m, q, c) ? (t.hide(), h = !1) : (c.titleShow = !1, c.width = "auto", c.height =
                "auto", l.html('<p id="lightbox-error">The requested content cannot be loaded.<br />Please try again later.</p>'), _process_inline())
    };
    _start = function () {
        var d = m[q], a, o, e, g, j, f;
        _abort();
        c = b.extend({}, i.defaults, typeof b(d).data(i.name) == "undefined" ? c : b(d).data(i.name));
        b(d).attr("data-lightbox") && b.each(b(d).attr("data-lightbox").split(";"), function (a, d) {
            var b = d.match(/\s*([A-Z_]*?)\s*:\s*(.+)\s*/i);
            b && (c[b[1]] = b[2])
        });
        f = c.onStart(m, q, c);
        if (f === !1)h = !1; else {
            typeof f == "object" && (c = b.extend(c, f));
            e = c.title ||
                    (d.nodeName ? b(d).attr("title") : d.title) || "";
            if (d.nodeName && !c.orig)c.orig = b(d).children("img:first").length ? b(d).children("img:first") : b(d);
            e === "" && c.orig && c.titleFromAlt && (e = c.orig.attr("alt"));
            a = c.href || (d.nodeName ? b(d).attr("href") : d.href) || null;
            if (/^(?:javascript)/i.test(a) || a == "#")a = null;
            if (c.type) {
                if (o = c.type, !a)a = c.content
            } else c.content ? o = "html" : a && (a.match(G) ? o = "image" : a.match(K) ? o = "swf" : a.match(L) ? o = "video" : a.match(H) ? (a = a.replace(H, "$1/embed/$2?$3").replace("/(.*)?$/", ""), o = "iframe") : a.match(I) ?
                    (a = a.replace(I, "$1player.vimeo.com/video/$2"), o = "iframe") : o = a.indexOf("http://") != -1 && a.indexOf(location.hostname.toLowerCase()) == -1 ? "iframe" : a.indexOf("#") === 0 ? "inline" : "ajax");
            if (o) {
                o == "inline" && (d = a.substr(a.indexOf("#")), o = b(d).length > 0 ? "inline" : "ajax");
                c.type = o;
                c.href = a;
                c.title = e;
                if (c.autoDimensions && c.type !== "iframe" && c.type !== "swf" && c.type !== "video")c.width = "auto", c.height = "auto";
                if (c.modal)c.overlayShow = !0, c.hideOnOverlayClick = !1, c.hideOnContentClick = !1, c.enableEscapeButton = !1, c.showCloseButton =
                        !1;
                c.padding = parseInt(c.padding, 10);
                c.margin = parseInt(c.margin, 10);
                l.css("padding", c.padding + c.margin);
                b(".lightbox-inline-tmp").unbind("lightbox-cancel").bind("lightbox-change", function () {
                    b(this).replaceWith(k.children())
                });
                switch (o) {
                    case "html":
                        l.html(c.content);
                        _process_inline();
                        break;
                    case "video":
                        h = !1;
                        c.scrolling = "no";
                        d = c.width == "auto" ? 320 : c.width;
                        o = c.height == "auto" ? 240 : c.height;
                        e = [];
                        e.push('src="' + a + '"');
                        e.push('width="' + d + '"');
                        e.push('height="' + o + '"');
                        e.push('preload="none"');
                        b.type(c.autoplay) !=
                                "undefined" && e.push('autoplay="' + String(c.autoplay) + '"');
                        b.type(c.controls) != "undefined" && e.push('controls="' + String(c.controls) + '"');
                        b.type(c.loop) != "undefined" && e.push('loop="' + String(c.loop) + '"');
                        b.type(c.poster) != "undefined" && e.push('poster="' + String(c.poster) + '"');
                        l.html("<video " + e.join(" ") + " /></video>");
                        b.fn.mediaelementplayer && b("video", l).mediaelementplayer();
                        c.width = "auto";
                        c.height = "auto";
                        _process_inline();
                        break;
                    case "inline":
                        if (b(d).parent().is("#lightbox-content") === !0) {
                            h = !1;
                            break
                        }
                        b('<div class="lightbox-inline-tmp" />').hide().insertBefore(b(d)).bind("lightbox-cleanup",
                                function () {
                                    b(this).replaceWith(k.children())
                                }).bind("lightbox-cancel", function () {
                                    b(this).replaceWith(l.children())
                                });
                        b(d).appendTo(l);
                        _process_inline();
                        break;
                    case "image":
                        h = !1;
                        i.showActivity();
                        s = new Image;
                        s.onerror = function () {
                            _error()
                        };
                        s.onload = function () {
                            h = !0;
                            s.onerror = s.onload = null;
                            _process_image()
                        };
                        s.src = a;
                        break;
                    case "swf":
                        c.scrolling = "no";
                        c.autoDimensions = !1;
                        g = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="' + c.width + '" height="' + c.height + '"><param name="movie" value="' + a +
                                '"></param>';
                        j = "";
                        b.each(c.swf, function (a, d) {
                            g += '<param name="' + a + '" value="' + d + '"></param>';
                            j += " " + a + '="' + d + '"'
                        });
                        g += '<embed src="' + a + '" type="application/x-shockwave-flash" width="' + c.width + '" height="' + c.height + '"' + j + "></embed></object>";
                        l.html(g);
                        _process_inline();
                        break;
                    case "ajax":
                        h = !1;
                        i.showActivity();
                        c.ajax.win = c.ajax.success;
                        C = b.ajax(b.extend({}, c.ajax, {url:a, data:c.ajax.data || {}, error:function (a) {
                            a.status > 0 && _error()
                        }, success:function (d, b, e) {
                            if ((typeof e == "object" ? e : C).status == 200) {
                                if (typeof c.ajax.win ==
                                        "function")if (f = c.ajax.win(a, d, b, e), f === !1) {
                                    t.hide();
                                    return
                                } else if (typeof f == "string" || typeof f == "object")d = f;
                                l.html(d);
                                _process_inline()
                            }
                        }}));
                        break;
                    case "iframe":
                        c.autoDimensions = !1, _show()
                }
            } else _error()
        }
    };
    _process_inline = function () {
        l.wrapInner('<div style="width:' + (c.width == "auto" ? "auto" : c.width + "px") + ";height:" + (c.height == "auto" ? "auto" : c.height + "px") + ";overflow: " + (c.scrolling == "auto" ? "auto" : c.scrolling == "yes" ? "scroll" : "hidden") + '"></div>');
        c.width = l.width();
        c.height = l.height();
        _show()
    };
    _process_image =
            function () {
                c.width = s.width;
                c.height = s.height;
                b("<img />").attr({id:"lightbox-img", src:s.src, alt:c.title}).appendTo(l);
                _show()
            };
    _show = function () {
        var d, J;
        t.hide();
        if (e.is(":visible") && !1 === a.onCleanup(j, n, a))b.event.trigger("lightbox-cancel"), h = !1; else if (h = !0, b(k.add(p)).unbind(), b(window).unbind("resize.fb scroll.fb"), b(document).unbind("keydown.fb"), e.is(":visible") && a.titlePosition !== "outside" && e.css("height", e.height()), j = m, n = q, a = c, a.overlayShow ? (p.css({"background-color":a.overlayColor, opacity:a.overlayOpacity,
            cursor:a.hideOnOverlayClick ? "pointer" : "auto", height:b(document).height()}), p.is(":visible") || p.show()) : p.hide(), g = _get_zoom_to(), _process_title(), e.is(":visible"))b(A.add(x).add(y)).hide(), d = e.position(), r = {top:d.top, left:d.left, width:e.width(), height:e.height()}, J = r.width == g.width && r.height == g.height, k.fadeTo(a.changeFade, 0.3, function () {
            var d = function () {
                k.html(l.contents()).fadeTo(a.changeFade, 1, _finish)
            };
            b.event.trigger("lightbox-change");
            k.empty().removeAttr("filter").css({"border-width":a.padding,
                width:g.width - a.padding * 2, height:a.type == "image" || a.type == "swf" || a.type == "iframe" ? g.height - w - a.padding * 2 : "auto"});
            J ? d() : (z.prop = 0, b(z).animate({prop:1}, {duration:a.changeSpeed, easing:a.easingChange, step:_draw, complete:d}))
        }); else if (e.removeAttr("style"), k.css("border-width", a.padding), a.transitionIn == "elastic") {
            r = _get_zoom_from();
            k.html(l.contents());
            e.show();
            if (a.opacity)g.opacity = 0;
            z.prop = 0;
            b(z).animate({prop:1}, {duration:a.speedIn, easing:a.easingIn, step:_draw, complete:_finish})
        } else a.titlePosition ==
                "inside" && w > 0 && f.show(), k.css({width:g.width - a.padding * 2, height:a.type == "image" || a.type == "swf" || a.type == "iframe" ? g.height - w - a.padding * 2 : "auto"}).html(l.contents()), e.css(g).fadeIn(a.transitionIn == "none" ? 0 : a.speedIn, _finish)
    };
    _format_title = function (d) {
        return d && d.length ? '<div id="lightbox-title-' + a.titlePosition + '">' + d + "</div>" : !1
    };
    _process_title = function () {
        u = a.title || "";
        w = 0;
        f.empty().removeAttr("style").removeClass();
        if (a.titleShow !== !1 && (u = b.isFunction(a.titleFormat) ? a.titleFormat(u, j, n, a) : _format_title(u)) &&
                u !== "")switch (f.addClass("lightbox-title-" + a.titlePosition).html(u).appendTo("body").show(), a.titlePosition) {
            case "inside":
                f.css({width:g.width - a.padding * 2, marginLeft:a.padding, marginRight:a.padding});
                w = f.outerHeight(!0);
                f.appendTo(B);
                g.height += w;
                break;
            case "over":
                f.css({marginLeft:a.padding, width:g.width - a.padding * 2, bottom:a.padding}).appendTo(B);
                break;
            case "float":
                f.css("left", parseInt((f.width() - g.width - 40) / 2, 10) * -1).appendTo(e);
                break;
            default:
                f.css({width:g.width - a.padding * 2, paddingLeft:a.padding,
                    paddingRight:a.padding}).appendTo(e)
        }
        f.hide()
    };
    _set_navigation = function () {
        (a.enableEscapeButton || a.enableKeyboardNav) && b(document).bind("keydown.fb", function (d) {
            if (d.keyCode == 27 && a.enableEscapeButton)d.preventDefault(), i.close(); else if ((d.keyCode == 37 || d.keyCode == 39) && a.enableKeyboardNav && d.target.tagName !== "INPUT" && d.target.tagName !== "TEXTAREA" && d.target.tagName !== "SELECT")d.preventDefault(), i[d.keyCode == 37 ? "prev" : "next"]()
        });
        a.showNavArrows ? ((a.cyclic && j.length > 1 || n !== 0) && x.show(), (a.cyclic && j.length >
                1 || n != j.length - 1) && y.show()) : (x.hide(), y.hide())
    };
    _finish = function () {
        b.support.opacity || (k.get(0).style.removeAttribute("filter"), e.get(0).style.removeAttribute("filter"));
        e.css("height", "auto");
        a.type !== "image" && a.type !== "swf" && a.type !== "iframe" && k.css("height", "auto");
        u && u.length && f.show();
        a.showCloseButton && A.show();
        _set_navigation();
        a.hideOnContentClick && k.bind("click", i.close);
        a.hideOnOverlayClick && p.bind("click", i.close);
        b(window).bind("resize.fb", i.resize);
        a.centerOnScroll && b(window).bind("scroll.fb",
                i.center);
        a.type == "iframe" && b('<iframe id="lightbox-frame" name="lightbox-frame' + (new Date).getTime() + '" frameborder="0" hspace="0" ' + (b.browser.msie ? 'allowtransparency="true""' : "") + ' scrolling="' + c.scrolling + '" src="' + a.href + '"></iframe>').appendTo(k);
        e.show();
        h = !1;
        i.center();
        a.onComplete(j, n, a);
        _preload_images()
    };
    _preload_images = function () {
        var d, a;
        if (j.length - 1 > n && (d = j[n + 1].href, typeof d !== "undefined" && d.match(G)))a = new Image, a.src = d;
        if (n > 0 && (d = j[n - 1].href, typeof d !== "undefined" && d.match(G)))a =
                new Image, a.src = d
    };
    _draw = function (d) {
        var b = {width:parseInt(r.width + (g.width - r.width) * d, 10), height:parseInt(r.height + (g.height - r.height) * d, 10), top:parseInt(r.top + (g.top - r.top) * d, 10), left:parseInt(r.left + (g.left - r.left) * d, 10)};
        if (typeof g.opacity !== "undefined")b.opacity = d < 0.5 ? 0.5 : d;
        e.css(b);
        k.css({width:b.width - a.padding * 2, height:b.height - w * d - a.padding * 2})
    };
    _get_viewport = function () {
        return[b(window).width() - a.margin * 2, b(window).height() - a.margin * 2, b(document).scrollLeft() + a.margin, b(document).scrollTop() +
                a.margin]
    };
    _get_zoom_to = function () {
        var d = _get_viewport(), b = {}, e = a.autoScale, f = a.padding * 2;
        b.width = a.width.toString().indexOf("%") > -1 ? parseInt(d[0] * parseFloat(a.width) / 100, 10) : parseInt(a.width) + f;
        b.height = a.height.toString().indexOf("%") > -1 ? parseInt(d[1] * parseFloat(a.height) / 100, 10) : parseInt(a.height) + f;
        if (e && (b.width > d[0] || b.height > d[1]))if (c.type == "image" || c.type == "swf") {
            e = a.width / a.height;
            if (b.width > d[0])b.width = d[0], b.height = parseInt((b.width - f) / e + f, 10);
            if (b.height > d[1])b.height = d[1], b.width = parseInt((b.height -
                    f) * e + f, 10)
        } else b.width = Math.min(b.width, d[0]), b.height = Math.min(b.height, d[1]);
        b.top = parseInt(Math.max(d[3] - 20, d[3] + (d[1] - b.height - 40) * 0.5), 10);
        b.left = parseInt(Math.max(d[2] - 20, d[2] + (d[0] - b.width - 40) * 0.5), 10);
        return b
    };
    _get_obj_pos = function (a) {
        var b = a.offset();
        b.top += parseInt(a.css("paddingTop"), 10) || 0;
        b.left += parseInt(a.css("paddingLeft"), 10) || 0;
        b.top += parseInt(a.css("border-top-width"), 10) || 0;
        b.left += parseInt(a.css("border-left-width"), 10) || 0;
        b.width = a.width();
        b.height = a.height();
        return b
    };
    _get_zoom_from =
            function () {
                var d = c.orig ? b(c.orig) : !1, e = {};
                d && d.length ? (d = _get_obj_pos(d), e = {width:d.width + a.padding * 2, height:d.height + a.padding * 2, top:d.top - a.padding - 20, left:d.left - a.padding - 20}) : (d = _get_viewport(), e = {width:a.padding * 2, height:a.padding * 2, top:parseInt(d[3] + d[1] * 0.5, 10), left:parseInt(d[2] + d[0] * 0.5, 10)});
                return e
            };
    _animate_loading = function () {
        t.is(":visible") ? (b("div", t).css("top", F * -40 + "px"), F = (F + 1) % 12) : clearInterval(E)
    };
    var v = function () {
    };
    v.prototype = b.extend(v.prototype, {name:"lightbox", defaults:{padding:10,
        margin:40, opacity:!1, modal:!1, cyclic:!1, scrolling:"auto", width:560, height:340, autoScale:!0, autoDimensions:!0, centerOnScroll:!1, ajax:{}, swf:{wmode:"transparent"}, hideOnOverlayClick:!0, hideOnContentClick:!1, overlayShow:!0, overlayOpacity:0.7, overlayColor:"#777", titleShow:!0, titlePosition:"float", titleFormat:null, titleFromAlt:!1, transitionIn:"fade", transitionOut:"fade", speedIn:300, speedOut:300, changeSpeed:300, changeFade:"fast", easingIn:"swing", easingOut:"swing", showCloseButton:!0, showNavArrows:!0, enableEscapeButton:!0,
        enableKeyboardNav:!0, onStart:function () {
        }, onCancel:function () {
        }, onComplete:function () {
        }, onCleanup:function () {
        }, onClosed:function () {
        }, onError:function () {
        }}, init:function () {
        var d = this;
        b("#lightbox-wrap").length || (b("body").append(l = b('<div id="lightbox-tmp"></div>'), t = b('<div id="lightbox-loading"><div></div></div>'), p = b('<div id="lightbox-overlay"></div>'), e = b('<div id="lightbox-wrap"></div>')), D = p.show().position(), p.hide(), D.top != 0 && p.css("top", D.top * -1), B = b('<div id="lightbox-outer"></div>').appendTo(e),
                B.append(k = b('<div id="lightbox-content"></div>'), A = b('<a id="lightbox-close"></a>'), f = b('<div id="lightbox-title"></div>'), x = b('<a href="javascript:;" id="lightbox-left"><span id="lightbox-left-ico"></span></a>'), y = b('<a href="javascript:;" id="lightbox-right"><span id="lightbox-right-ico"></span></a>')), A.bind("click", this.close), t.bind("click", this.cancel), x.bind("click", function (a) {
            a.preventDefault();
            d.prev()
        }), y.bind("click", function (a) {
            a.preventDefault();
            d.next()
        }), b.fn.mousewheel && e.bind("mousewheel.fb",
                function (b, c) {
                    (h || a.type == "image") && b.preventDefault();
                    d[c > 0 ? "prev" : "next"]()
                }))
    }, open:function (a, c) {
        var e;
        if (!h) {
            h = !0;
            e = typeof c !== "undefined" ? c : {};
            m = [];
            q = parseInt(e.index, 10) || 0;
            if (b.isArray(a)) {
                for (var f = 0, g = a.length; f < g; f++)typeof a[f] == "object" ? b(a[f]).data(i.name, b.extend({}, e, a[f])) : a[f] = b({}).data(i.name, b.extend({content:a[f]}, e));
                m = b.merge(m, a)
            } else typeof a == "object" ? b(a).data(i.name, b.extend({}, e, a)) : a = b({}).data(i.name, b.extend({content:a}, e)), m.push(a);
            if (q > m.length || q < 0)q = 0;
            _start()
        }
    },
        showActivity:function () {
            clearInterval(E);
            t.show();
            E = setInterval(_animate_loading, 66)
        }, hideActivity:function () {
            t.hide()
        }, next:function () {
            return this.pos(n + 1)
        }, prev:function () {
            return this.pos(n - 1)
        }, pos:function (b) {
            h || (b = parseInt(b), m = j, b > -1 && b < j.length ? (q = b, _start()) : a.cyclic && j.length > 1 && (q = b >= j.length ? 0 : j.length - 1, _start()))
        }, cancel:function () {
            h || (h = !0, b.event.trigger("lightbox-cancel"), _abort(), c.onCancel(m, q, c), h = !1)
        }, close:function () {
            function d() {
                p.fadeOut("fast");
                f.empty().hide();
                e.hide();
                b.event.trigger("lightbox-cleanup");
                k.empty();
                a.onClosed(j, n, a);
                j = c = [];
                n = q = 0;
                a = c = {};
                h = !1
            }

            if (!h && !e.is(":hidden"))if (h = !0, a && !1 === a.onCleanup(j, n, a))h = !1; else if (_abort(), b(A.add(x).add(y)).hide(), b(k.add(p)).unbind(), b(window).unbind("resize.fb scroll.fb"), b(document).unbind("keydown.fb"), k.find("iframe").attr("src", "about:blank"), a.titlePosition !== "inside" && f.empty(), e.stop(), a.transitionOut == "elastic") {
                r = _get_zoom_from();
                var i = e.position();
                g = {top:i.top, left:i.left, width:e.width(), height:e.height()};
                if (a.opacity)g.opacity = 1;
                f.empty().hide();
                z.prop = 1;
                b(z).animate({prop:0}, {duration:a.speedOut, easing:a.easingOut, step:_draw, complete:d})
            } else e.fadeOut(a.transitionOut == "none" ? 0 : a.speedOut, d)
        }, resize:function () {
            p.is(":visible") && p.css("height", b(document).height());
            i.center(!0)
        }, center:function (b) {
            var c, f;
            if (!h && (f = b === !0 ? 1 : 0, c = _get_viewport(), f || !(e.width() > c[0] || e.height() > c[1])))e.stop().animate({top:parseInt(Math.max(c[3] - 20, c[3] + (c[1] - k.height() - 40) * 0.5 - a.padding)), left:parseInt(Math.max(c[2] - 20, c[2] + (c[0] - k.width() - 40) * 0.5 - a.padding))},
                    typeof b == "number" ? b : 200)
        }});
    b.fn[v.prototype.name] = function () {
        var a = arguments, c = a[0] ? a[0] : {};
        return this.each(function () {
            b(this).data(v.prototype.name, c).unbind("click." + v.prototype.name).bind("click." + v.prototype.name, function (a) {
                a.preventDefault();
                h || (h = !0, b(this).blur(), m = [], q = 0, (a = b(this).attr("data-lightbox") || "") && (a = a.match(/group:([^;]+)/i)) ? (m = b('a[data-lightbox*="' + a[0] + '"], area[data-lightbox*="' + a[0] + '"]'), q = m.index(this)) : m.push(this), _start())
            })
        })
    };
    b(document).ready(function () {
        i = new v;
        i.init();
        b[v.prototype.name] = i
    })
})(jQuery);
