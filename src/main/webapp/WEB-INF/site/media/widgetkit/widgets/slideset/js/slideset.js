/* Copyright (C) YOOtheme GmbH, YOOtheme Proprietary Use License (http://www.yootheme.com/license) */

(function (h) {
    var v = [], n = function () {
    }, p = {width:"auto", height:"auto", index:0, autoplay:!1, effect:"slide", interval:5E3, easing:"easeOutCirc", duration:300};
    n.prototype = h.extend(n.prototype, {name:"slideset", initialize:function (b) {
        this.options = h.extend({}, p, b);
        var d = this, f = this.element;
        this.sets = f.find("ul.set");
        this.navitems = f.find("ul.nav").children();
        this.current = this.sets[this.options.index] ? this.options.index : 0;
        this.busy = !1;
        this.timer = null;
        this.hover = !1;
        this.gwidth = this.options.width == "auto" ? f.width() :
                this.options.width;
        this.gheight = this.options.height == "auto" ? this.sets.eq(0).outerHeight(!0) : this.options.height;
        f.css({width:d.gwidth});
        this.sets.css({height:d.gheight});
        this.sets.each(function (a) {
            d.sets.eq(a).children().each(function () {
                var a = h(this), b = a.position();
                a.data("left", b.left)
            });
            d.sets.eq(a).children().each(function () {
                var a = h(this);
                a.css("position", "absolute").css("left", a.data("left"))
            });
            d.sets.eq(a).hide()
        });
        this.navitems.each(function (a) {
            h(this).bind("click", function () {
                d.stop().show(a)
            })
        });
        f.find(".next, .prev").bind("click", function () {
            d.stop()[h(this).hasClass("next") ? "next" : "previous"]()
        });
        "ontouchend"in document && (f.bind("touchstart", function (a) {
            function b(s) {
                if (j) {
                    var a = s.originalEvent.touches ? s.originalEvent.touches[0] : s;
                    i = {time:(new Date).getTime(), coords:[a.pageX, a.pageY]};
                    Math.abs(j.coords[0] - i.coords[0]) > 10 && s.preventDefault()
                }
            }

            var d = a.originalEvent.touches ? a.originalEvent.touches[0] : a, j = {time:(new Date).getTime(), coords:[d.pageX, d.pageY], origin:h(a.target)}, i;
            f.bind("touchmove",
                    b).one("touchend", function () {
                        f.unbind("touchmove", b);
                        j && i && i.time - j.time < 1E3 && Math.abs(j.coords[0] - i.coords[0]) > 30 && Math.abs(j.coords[1] - i.coords[1]) < 75 && j.origin.trigger("swipe").trigger(j.coords[0] > i.coords[0] ? "swipeleft" : "swiperight");
                        j = i = void 0
                    })
        }), f.bind("swipeleft",
                function () {
                    d.next()
                }).bind("swiperight", function () {
            d.previous()
        }));
        this.sets.eq(this.current).show();
        jQuery.support.opacity && this.sets.eq(this.current).css("transform", "scale(0.1)").animate({transform:"scale(1.0)"}, {easing:"easeOutCirc",
            duration:500});
        this.navitems.eq(this.current).addClass("active");
        f.hover(function () {
            d.hover = !0
        }, function () {
            d.hover = !1
        });
        this.options.autoplay && this.start()
    }, next:function () {
        this.show(this.sets[this.current + 1] ? this.current + 1 : 0)
    }, previous:function () {
        this.show(this.current - 1 > -1 ? this.current - 1 : this.sets.length - 1)
    }, start:function () {
        if (!this.timer) {
            var b = this;
            this.timer = setInterval(function () {
                !b.hover && !b.busy && b.next()
            }, this.options.interval);
            return this
        }
    }, stop:function () {
        this.timer && clearInterval(this.timer);
        return this
    }, show:function (b) {
        this.current == b || this.busy || (this[this[this.options.effect] ? this.options.effect : "slide"](b), this.navitems.removeClass("active").eq(b).addClass("active"))
    }, slide:function (b) {
        var d = b > this.current ? "left" : "right", f = this.sets.eq(b), a = this;
        this.busy = !0;
        this.sets.eq(this.current).animate({"margin-left":(d == "left" ? -1 : 1) * 2 * this.gwidth}, {complete:function () {
            f.css("margin-left", 0).children().hide().css({left:(d == "left" ? 1 : -1) * 2 * a.gwidth});
            f.show();
            a.sets.eq(a.current).hide();
            var h =
                    f.children(), k = 0;
            h.each(function (f) {
                d == "right" && (f = h.length - 1 - f);
                (function (b, s) {
                    setTimeout(function () {
                        h.eq(b).show().animate({left:h.eq(b).data("left")}, {complete:function () {
                            if (d == "left" && b == h.length - 1 || d == "right" && b == 0)a.busy = !1, a.current = s
                        }, duration:a.options.duration, easing:a.options.easing})
                    }, 100 * k)
                })(f, b);
                k += 1
            })
        }})
    }, zoom:function (b) {
        var d = this.sets.eq(b), h = 0, a = this.sets.eq(this.current).children(), m = this;
        this.busy = !0;
        this.sets.eq(this.current).children().animate(jQuery.support.opacity ? {transform:"scale(0)",
            opacity:0} : {opacity:0}, {complete:function () {
            h += 1;
            if (!(h != -1 && h < a.length - 1)) {
                h = -1;
                var k = d.children().css(jQuery.support.opacity ? {transform:"scale(0)", opacity:0} : {opacity:0}), j = 0;
                m.sets.eq(m.current).hide();
                d.css("margin-left", "").show();
                k.each(function (a) {
                    k.eq(a).css({left:k.eq(a).data("left")}).show();
                    (function (a, b) {
                        setTimeout(function () {
                            k.eq(a).show().animate(jQuery.support.opacity ? {transform:"scale(1)", opacity:1} : {opacity:1}, {complete:function () {
                                if (a == k.length - 1)m.busy = !1, m.current = b
                            }, duration:m.options.duration,
                                easing:m.options.easing})
                        }, Math.round(m.options.duration / 3) * j)
                    })(a, b);
                    j += 1
                })
            }
        }, easing:"swing", duration:Math.round(m.options.duration / 2)})
    }, deck:function (b) {
        if (!jQuery.support.opacity)return this.zoom(b);
        var d = b > this.current ? "left" : "right", f = this.sets.eq(b), a = this.sets.eq(this.current).children(), m = this.sets.eq(b).children(), k = a[b > this.current ? "first" : "last"](), j = m[b > this.current ? "last" : "first"](), i = this;
        d == "right" && a._reverse();
        d == "right" && m._reverse();
        this.busy = !0;
        a.each(function (s) {
            var d = h(this);
            (function (c, e) {
                setTimeout(function () {
                    e.animate({transform:"scale(0)", opacity:0, left:k.data("left")}, {complete:function () {
                        e.hide();
                        c == a.length - 1 && (i.sets.eq(i.current).hide(), m.css({transform:"scale(0)", opacity:0, left:j.data("left")}), f.css("margin-left", "").show(), m.each(function (c) {
                            var e = h(this);
                            (function (c, e) {
                                setTimeout(function () {
                                    e.animate({transform:"scale(1)", opacity:1, left:e.data("left")}, {complete:function () {
                                        if (c == m.length - 1)a.show().each(function (c) {
                                            a.eq(c).css({transform:"scale(1)", opacity:1,
                                                left:a.eq(c).data("left")})
                                        }), i.busy = !1, i.current = b
                                    }, duration:i.options.duration, easing:i.options.easing})
                                }, Math.round(i.options.duration / 3) * c)
                            })(c, e)
                        }))
                    }, duration:i.options.duration, easing:i.options.easing})
                }, Math.round(i.options.duration / 3) * c)
            })(s, d)
        })
    }});
    if (!h.fn._reverse)h.fn._reverse = [].reverse;
    h.fn[n.prototype.name] = function () {
        var b = arguments, d = b[0] ? b[0] : null;
        return this.each(function () {
            var f = h(this);
            if (n.prototype[d] && f.data(n.prototype.name) && d != "initialize")f.data(n.prototype.name)[d].apply(f.data(n.prototype.name),
                    Array.prototype.slice.call(b, 1)); else if (!d || h.isPlainObject(d)) {
                var a = new n;
                a.element = f;
                v.push(a);
                n.prototype.initialize && a.initialize.apply(a, b);
                f.data(n.prototype.name, a)
            } else h.error("Method " + d + " does not exist on jQuery." + n.prototype.name)
        })
    }
})(jQuery);
(function (h) {
    function v(a) {
        for (var a = a.split(")"), b = h.trim, c = a.length - 1, e, d, g, f = 1, i = 0, k = 0, m = 1, j, n, o, t = 0, r = 0; c--;) {
            e = a[c].split("(");
            d = b(e[0]);
            g = e[1];
            j = e = n = o = 0;
            switch (d) {
                case "translateX":
                    t += parseInt(g, 10);
                    continue;
                case "translateY":
                    r += parseInt(g, 10);
                    continue;
                case "translate":
                    g = g.split(",");
                    t += parseInt(g[0], 10);
                    r += parseInt(g[1] || 0, 10);
                    continue;
                case "rotate":
                    g = p(g);
                    j = Math.cos(g);
                    e = Math.sin(g);
                    n = -Math.sin(g);
                    o = Math.cos(g);
                    break;
                case "scaleX":
                    j = g;
                    o = 1;
                    break;
                case "scaleY":
                    j = 1;
                    o = g;
                    break;
                case "scale":
                    g = g.split(",");
                    j = g[0];
                    o = g.length > 1 ? g[1] : g[0];
                    break;
                case "skewX":
                    j = o = 1;
                    n = Math.tan(p(g));
                    break;
                case "skewY":
                    j = o = 1;
                    e = Math.tan(p(g));
                    break;
                case "skew":
                    j = o = 1;
                    g = g.split(",");
                    n = Math.tan(p(g[0]));
                    e = Math.tan(p(g[1] || 0));
                    break;
                case "matrix":
                    g = g.split(","), j = +g[0], e = +g[1], n = +g[2], o = +g[3], t += parseInt(g[4], 10), r += parseInt(g[5], 10)
            }
            d = f * j + i * n;
            i = f * e + i * o;
            j = k * j + m * n;
            m = k * e + m * o;
            f = d;
            k = j
        }
        return[f, i, k, m, t, r]
    }

    function n(a) {
        var b, c, e, d = a[0], g = a[1], h = a[2], f = a[3];
        d * f - g * h ? (b = Math.sqrt(d * d + g * g), d /= b, g /= b, e = d * h + g * f, h -= d * e, f -= g * e, c = Math.sqrt(h *
                h + f * f), h /= c, f /= c, e /= c, d * f < g * h && (d = -d, g = -g, e = -e, b = -b)) : rotate = b = c = e = 0;
        return{translate:[+a[4], +a[5]], rotate:Math.atan2(g, d), scale:[b, c], skew:[e, 0]}
    }

    function p(a) {
        return~a.indexOf("deg") ? parseInt(a, 10) * (Math.PI * 2 / 360) : ~a.indexOf("grad") ? parseInt(a, 10) * (Math.PI / 200) : parseFloat(a)
    }

    for (var b = document.createElement("div"), b = b.style, d = ["OTransform", "msTransform", "WebkitTransform", "MozTransform", "transform"], f = d.length, a, m, k, j, i = /Matrix([^)]*)/; f--;)d[f]in b && (h.support.transform = a = d[f]);
    if (!a)h.support.matrixFilter =
            m = b.filter === "";
    b = b = null;
    h.cssNumber.transform = !0;
    a && a != "transform" ? (h.cssProps.transform = a, a == "MozTransform" ? k = {get:function (b, d) {
        return d ? h.css(b, a).split("px").join("") : b.style[a]
    }, set:function (b, d) {
        b.style[a] = /matrix[^)p]*\)/.test(d) ? d.replace(/matrix((?:[^,]*,){4})([^,]*),([^)]*)/, "matrix$1$2px,$3px") : d
    }} : /^1\.[0-5](?:\.|$)/.test(h.fn.jquery) && (k = {get:function (b, d) {
        return d ? h.css(b, a.replace(/^ms/, "Ms")) : b.style[a]
    }})) : m && (k = {get:function (a, b) {
        var c = b && a.currentStyle ? a.currentStyle : a.style, e;
        c && i.test(c.filter) ? (e = RegExp.$1.split(","), e = [e[0].split("=")[1], e[2].split("=")[1], e[1].split("=")[1], e[3].split("=")[1]]) : e = [1, 0, 0, 1];
        e[4] = c ? c.left : 0;
        e[5] = c ? c.top : 0;
        return"matrix(" + e + ")"
    }, set:function (a, b, c) {
        var e = a.style, d, g, f;
        if (!c)e.zoom = 1;
        b = v(b);
        if (!c || c.M)if (g = ["Matrix(M11=" + b[0], "M12=" + b[2], "M21=" + b[1], "M22=" + b[3], "SizingMethod='auto expand'"].join(), f = (d = a.currentStyle) && d.filter || e.filter || "", e.filter = i.test(f) ? f.replace(i, g) : f + " progid:DXImageTransform.Microsoft." + g + ")", centerOrigin = h.transform.centerOrigin)e[centerOrigin ==
                "margin" ? "marginLeft" : "left"] = -(a.offsetWidth / 2) + a.clientWidth / 2 + "px", e[centerOrigin == "margin" ? "marginTop" : "top"] = -(a.offsetHeight / 2) + a.clientHeight / 2 + "px";
        if (!c || c.T)e.left = b[4] + "px", e.top = b[5] + "px"
    }});
    k && (h.cssHooks.transform = k);
    j = k && k.get || h.css;
    h.fx.step.transform = function (b) {
        var d = b.elem, c = b.start, e = b.end, f, g = b.pos, i, w, x, y, A = !1, z = !1, o;
        i = w = x = y = "";
        if (!c || typeof c === "string") {
            c || (c = j(d, a));
            if (m)d.style.zoom = 1;
            f = e.split(c);
            if (f.length == 2)e = f.join(""), b.origin = c, c = "none";
            c == "none" ? c = {translate:[0,
                0], rotate:0, scale:[1, 1], skew:[0, 0]} : (c = /\(([^,]*),([^,]*),([^,]*),([^,]*),([^,p]*)(?:px)?,([^)p]*)(?:px)?/.exec(c), c = n([c[1], c[2], c[3], c[4], c[5], c[6]]));
            b.start = c;
            if (~e.indexOf("matrix"))e = n(v(e)); else {
                e = e.split(")");
                f = [0, 0];
                for (var t = 0, r = [1, 1], u = [0, 0], B = e.length - 1, C = h.trim, l, q; B--;)l = e[B].split("("), q = C(l[0]), l = l[1], q == "translateX" ? f[0] += parseInt(l, 10) : q == "translateY" ? f[1] += parseInt(l, 10) : q == "translate" ? (l = l.split(","), f[0] += parseInt(l[0], 10), f[1] += parseInt(l[1] || 0, 10)) : q == "rotate" ? t += p(l) : q == "scaleX" ?
                        r[0] *= l : q == "scaleY" ? r[1] *= l : q == "scale" ? (l = l.split(","), r[0] *= l[0], r[1] *= l.length > 1 ? l[1] : l[0]) : q == "skewX" ? u[0] += p(l) : q == "skewY" ? u[1] += p(l) : q == "skew" && (l = l.split(","), u[0] += p(l[0]), u[1] += p(l[1] || "0"));
                e = {translate:f, rotate:t, scale:r, skew:u}
            }
            b.end = e;
            for (o in c)(o == "rotate" ? c[o] == e[o] : c[o][0] == e[o][0] && c[o][1] == e[o][1]) && delete c[o]
        }
        c.translate && (i = " translate(" + (c.translate[0] + (e.translate[0] - c.translate[0]) * g + 0.5 | 0) + "px," + (c.translate[1] + (e.translate[1] - c.translate[1]) * g + 0.5 | 0) + "px)", A = !0);
        c.rotate !=
                void 0 && (w = " rotate(" + (c.rotate + (e.rotate - c.rotate) * g) + "rad)", z = !0);
        c.scale && (x = " scale(" + (c.scale[0] + (e.scale[0] - c.scale[0]) * g) + "," + (c.scale[1] + (e.scale[1] - c.scale[1]) * g) + ")", z = !0);
        c.skew && (y = " skew(" + (c.skew[0] + (e.skew[0] - c.skew[0]) * g) + "rad," + (c.skew[1] + (e.skew[1] - c.skew[1]) * g) + "rad)", z = !0);
        b = b.origin ? b.origin + i + y + x + w : i + w + x + y;
        k && k.set ? k.set(d, b, {M:z, T:A}) : d.style[a] = b
    };
    h.transform = {centerOrigin:"margin"}
})(jQuery);
