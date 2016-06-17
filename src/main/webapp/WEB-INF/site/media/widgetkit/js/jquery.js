/*
 jQuery JavaScript Library v1.6.2
 http://jquery.com/

 Copyright 2011, John Resig
 Dual licensed under the MIT or GPL Version 2 licenses.
 http://jquery.org/license

 Includes Sizzle.js
 http://sizzlejs.com/
 Copyright 2011, The Dojo Foundation
 Released under the MIT, BSD, and GPL Licenses.

 Date: Thu Jun 30 14:16:56 2011 -0400
 */
(function (n, k) {
    function pa(a, b, d) {
        if (d === k && a.nodeType === 1)if (d = "data-" + b.replace(bb, "$1-$2").toLowerCase(), d = a.getAttribute(d), typeof d === "string") {
            try {
                d = d === "true" ? !0 : d === "false" ? !1 : d === "null" ? null : !c.isNaN(d) ? parseFloat(d) : cb.test(d) ? c.parseJSON(d) : d
            } catch (e) {
            }
            c.data(a, b, d)
        } else d = k;
        return d
    }

    function aa(a) {
        for (var b in a)if (b !== "toJSON")return!1;
        return!0
    }

    function qa(a, b, d) {
        var e = b + "defer", f = b + "queue", g = b + "mark", h = c.data(a, e, k, !0);
        h && (d === "queue" || !c.data(a, f, k, !0)) && (d === "mark" || !c.data(a, g, k,
                !0)) && setTimeout(function () {
            !c.data(a, f, k, !0) && !c.data(a, g, k, !0) && (c.removeData(a, e, !0), h.resolve())
        }, 0)
    }

    function x() {
        return!1
    }

    function S() {
        return!0
    }

    function ra(a, b, d) {
        var e = c.extend({}, d[0]);
        e.type = a;
        e.originalEvent = {};
        e.liveFired = k;
        c.event.handle.call(b, e);
        e.isDefaultPrevented() && d[0].preventDefault()
    }

    function db(a) {
        var b, d, e, f, g, h, i, j, l, o, k, m = [];
        f = [];
        g = c._data(this, "events");
        if (!(a.liveFired === this || !g || !g.live || a.target.disabled || a.button && a.type === "click")) {
            a.namespace && (k = RegExp("(^|\\.)" + a.namespace.split(".").join("\\.(?:.*\\.)?") +
                    "(\\.|$)"));
            a.liveFired = this;
            var n = g.live.slice(0);
            for (i = 0; i < n.length; i++)g = n[i], g.origType.replace(ba, "") === a.type ? f.push(g.selector) : n.splice(i--, 1);
            f = c(a.target).closest(f, a.currentTarget);
            for (j = 0, l = f.length; j < l; j++) {
                o = f[j];
                for (i = 0; i < n.length; i++)if (g = n[i], o.selector === g.selector && (!k || k.test(g.namespace)) && !o.elem.disabled) {
                    h = o.elem;
                    e = null;
                    if (g.preType === "mouseenter" || g.preType === "mouseleave")a.type = g.preType, (e = c(a.relatedTarget).closest(g.selector)[0]) && c.contains(h, e) && (e = h);
                    (!e || e !== h) && m.push({elem:h,
                        handleObj:g, level:o.level})
                }
            }
            for (j = 0, l = m.length; j < l; j++) {
                f = m[j];
                if (d && f.level > d)break;
                a.currentTarget = f.elem;
                a.data = f.handleObj.data;
                a.handleObj = f.handleObj;
                k = f.handleObj.origHandler.apply(f.elem, arguments);
                if (k === !1 || a.isPropagationStopped())if (d = f.level, k === !1 && (b = !1), a.isImmediatePropagationStopped())break
            }
            return b
        }
    }

    function U(a, b) {
        return(a && a !== "*" ? a + "." : "") + b.replace(eb, "`").replace(fb, "&")
    }

    function sa(a, b, d) {
        b = b || 0;
        if (c.isFunction(b))return c.grep(a, function (a, c) {
            return!!b.call(a, c, a) === d
        });
        else if (b.nodeType)return c.grep(a, function (a) {
            return a === b === d
        }); else if (typeof b === "string") {
            var e = c.grep(a, function (a) {
                return a.nodeType === 1
            });
            if (gb.test(b))return c.filter(b, e, !d); else b = c.filter(b, e)
        }
        return c.grep(a, function (a) {
            return c.inArray(a, b) >= 0 === d
        })
    }

    function ta(a, b) {
        if (b.nodeType === 1 && c.hasData(a)) {
            var d = c.expando, e = c.data(a), f = c.data(b, e);
            if (e = e[d]) {
                var g = e.events, f = f[d] = c.extend({}, e);
                if (g) {
                    delete f.handle;
                    f.events = {};
                    for (var h in g) {
                        d = 0;
                        for (e = g[h].length; d < e; d++)c.event.add(b, h + (g[h][d].namespace ?
                                "." : "") + g[h][d].namespace, g[h][d], g[h][d].data)
                    }
                }
            }
        }
    }

    function ua(a, b) {
        var d;
        if (b.nodeType === 1) {
            b.clearAttributes && b.clearAttributes();
            b.mergeAttributes && b.mergeAttributes(a);
            d = b.nodeName.toLowerCase();
            if (d === "object")b.outerHTML = a.outerHTML; else if (d === "input" && (a.type === "checkbox" || a.type === "radio")) {
                if (a.checked)b.defaultChecked = b.checked = a.checked;
                if (b.value !== a.value)b.value = a.value
            } else if (d === "option")b.selected = a.defaultSelected; else if (d === "input" || d === "textarea")b.defaultValue = a.defaultValue;
            b.removeAttribute(c.expando)
        }
    }

    function V(a) {
        return"getElementsByTagName"in a ? a.getElementsByTagName("*") : "querySelectorAll"in a ? a.querySelectorAll("*") : []
    }

    function va(a) {
        if (a.type === "checkbox" || a.type === "radio")a.defaultChecked = a.checked
    }

    function wa(a) {
        c.nodeName(a, "input") ? va(a) : "getElementsByTagName"in a && c.grep(a.getElementsByTagName("input"), va)
    }

    function hb(a, b) {
        b.src ? c.ajax({url:b.src, async:!1, dataType:"script"}) : c.globalEval((b.text || b.textContent || b.innerHTML || "").replace(ib, "/*$0*/"));
        b.parentNode &&
        b.parentNode.removeChild(b)
    }

    function xa(a, b, d) {
        var e = b === "width" ? a.offsetWidth : a.offsetHeight, f = b === "width" ? jb : kb;
        if (e > 0)return d !== "border" && c.each(f, function () {
            d || (e -= parseFloat(c.css(a, "padding" + this)) || 0);
            d === "margin" ? e += parseFloat(c.css(a, d + this)) || 0 : e -= parseFloat(c.css(a, "border" + this + "Width")) || 0
        }), e + "px";
        e = K(a, b, b);
        if (e < 0 || e == null)e = a.style[b] || 0;
        e = parseFloat(e) || 0;
        d && c.each(f, function () {
            e += parseFloat(c.css(a, "padding" + this)) || 0;
            d !== "padding" && (e += parseFloat(c.css(a, "border" + this + "Width")) ||
                    0);
            d === "margin" && (e += parseFloat(c.css(a, d + this)) || 0)
        });
        return e + "px"
    }

    function ya(a) {
        return function (b, d) {
            var j;
            typeof b !== "string" && (d = b, b = "*");
            if (c.isFunction(d))for (var e = b.toLowerCase().split(za), f = 0, g = e.length, h, i; f < g; f++)h = e[f], (i = /^\+/.test(h)) && (h = h.substr(1) || "*"), j = a[h] = a[h] || [], h = j, h[i ? "unshift" : "push"](d)
        }
    }

    function W(a, b, c, e, f, g) {
        f = f || b.dataTypes[0];
        g = g || {};
        g[f] = !0;
        for (var f = a[f], h = 0, i = f ? f.length : 0, j = a === ca, l; h < i && (j || !l); h++)l = f[h](b, c, e), typeof l === "string" && (!j || g[l] ? l = k : (b.dataTypes.unshift(l),
                l = W(a, b, c, e, l, g)));
        if ((j || !l) && !g["*"])l = W(a, b, c, e, "*", g);
        return l
    }

    function da(a, b, d, e) {
        if (c.isArray(b))c.each(b, function (b, f) {
            d || lb.test(a) ? e(a, f) : da(a + "[" + (typeof f === "object" || c.isArray(f) ? b : "") + "]", f, d, e)
        }); else if (!d && b != null && typeof b === "object")for (var f in b)da(a + "[" + f + "]", b[f], d, e); else e(a, b)
    }

    function Aa() {
        try {
            return new n.XMLHttpRequest
        } catch (a) {
        }
    }

    function Ba() {
        setTimeout(mb, 0);
        return X = c.now()
    }

    function mb() {
        X = k
    }

    function H(a, b) {
        var d = {};
        c.each(Ca.concat.apply([], Ca.slice(0, b)), function () {
            d[this] =
                    a
        });
        return d
    }

    function Da(a) {
        if (!ea[a]) {
            var b = m.body, d = c("<" + a + ">").appendTo(b), e = d.css("display");
            d.remove();
            if (e === "none" || e === "") {
                if (!v)v = m.createElement("iframe"), v.frameBorder = v.width = v.height = 0;
                b.appendChild(v);
                if (!C || !v.createElement)C = (v.contentWindow || v.contentDocument).document, C.write((m.compatMode === "CSS1Compat" ? "<!doctype html>" : "") + "<html><body>"), C.close();
                d = C.createElement(a);
                C.body.appendChild(d);
                e = c.css(d, "display");
                b.removeChild(v)
            }
            ea[a] = e
        }
        return ea[a]
    }

    function fa(a) {
        return c.isWindow(a) ?
                a : a.nodeType === 9 ? a.defaultView || a.parentWindow : !1
    }

    var m = n.document, nb = n.navigator, ob = n.location, c = function () {
        function a() {
            if (!b.isReady) {
                try {
                    m.documentElement.doScroll("left")
                } catch (c) {
                    setTimeout(a, 1);
                    return
                }
                b.ready()
            }
        }

        var b = function (a, c) {
            return new b.fn.init(a, c, f)
        }, c = n.jQuery, e = n.$, f, g = /^(?:[^<]*(<[\w\W]+>)[^>]*$|#([\w\-]*)$)/, h = /\S/, i = /^\s+/, j = /\s+$/, l = /\d/, o = /^<(\w+)\s*\/?>(?:<\/\1>)?$/, T = /^[\],:{}\s]*$/, A = /\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, p = /"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
                s = /(?:^|:|,)(?:\s*\[)+/g, w = /(webkit)[ \/]([\w.]+)/, t = /(opera)(?:.*version)?[ \/]([\w.]+)/, z = /(msie) ([\w.]+)/, v = /(mozilla)(?:.*? rv:([\w.]+))?/, u = /-([a-z])/ig, q = function (a, b) {
            return b.toUpperCase()
        }, F = nb.userAgent, Y, P, pb = Object.prototype.toString, ga = Object.prototype.hasOwnProperty, ha = Array.prototype.push, Q = Array.prototype.slice, Ea = String.prototype.trim, R = Array.prototype.indexOf, Fa = {};
        b.fn = b.prototype = {constructor:b, init:function (a, c, d) {
            var e;
            if (!a)return this;
            if (a.nodeType)return this.context = this[0] =
                    a, this.length = 1, this;
            if (a === "body" && !c && m.body)return this.context = m, this[0] = m.body, this.selector = a, this.length = 1, this;
            if (typeof a === "string")if ((e = a.charAt(0) === "<" && a.charAt(a.length - 1) === ">" && a.length >= 3 ? [null, a, null] : g.exec(a)) && (e[1] || !c))if (e[1])return d = (c = c instanceof b ? c[0] : c) ? c.ownerDocument || c : m, (a = o.exec(a)) ? b.isPlainObject(c) ? (a = [m.createElement(a[1])], b.fn.attr.call(a, c, !0)) : a = [d.createElement(a[1])] : (a = b.buildFragment([e[1]], [d]), a = (a.cacheable ? b.clone(a.fragment) : a.fragment).childNodes),
                    b.merge(this, a); else {
                if ((c = m.getElementById(e[2])) && c.parentNode) {
                    if (c.id !== e[2])return d.find(a);
                    this.length = 1;
                    this[0] = c
                }
                this.context = m;
                this.selector = a;
                return this
            } else return!c || c.jquery ? (c || d).find(a) : this.constructor(c).find(a); else if (b.isFunction(a))return d.ready(a);
            if (a.selector !== k)this.selector = a.selector, this.context = a.context;
            return b.makeArray(a, this)
        }, selector:"", jquery:"1.6.2", length:0, size:function () {
            return this.length
        }, toArray:function () {
            return Q.call(this, 0)
        }, get:function (a) {
            return a ==
                    null ? this.toArray() : a < 0 ? this[this.length + a] : this[a]
        }, pushStack:function (a, c, d) {
            var e = this.constructor();
            b.isArray(a) ? ha.apply(e, a) : b.merge(e, a);
            e.prevObject = this;
            e.context = this.context;
            if (c === "find")e.selector = this.selector + (this.selector ? " " : "") + d; else if (c)e.selector = this.selector + "." + c + "(" + d + ")";
            return e
        }, each:function (a, c) {
            return b.each(this, a, c)
        }, ready:function (a) {
            b.bindReady();
            Y.done(a);
            return this
        }, eq:function (a) {
            return a === -1 ? this.slice(a) : this.slice(a, +a + 1)
        }, first:function () {
            return this.eq(0)
        },
            last:function () {
                return this.eq(-1)
            }, slice:function () {
                return this.pushStack(Q.apply(this, arguments), "slice", Q.call(arguments).join(","))
            }, map:function (a) {
                return this.pushStack(b.map(this, function (b, c) {
                    return a.call(b, c, b)
                }))
            }, end:function () {
                return this.prevObject || this.constructor(null)
            }, push:ha, sort:[].sort, splice:[].splice};
        b.fn.init.prototype = b.fn;
        b.extend = b.fn.extend = function () {
            var a, c, d, e, f, g = arguments[0] || {}, u = 1, h = arguments.length, q = !1;
            typeof g === "boolean" && (q = g, g = arguments[1] || {}, u = 2);
            typeof g !==
                    "object" && !b.isFunction(g) && (g = {});
            h === u && (g = this, --u);
            for (; u < h; u++)if ((a = arguments[u]) != null)for (c in a)d = g[c], e = a[c], g !== e && (q && e && (b.isPlainObject(e) || (f = b.isArray(e))) ? (f ? (f = !1, d = d && b.isArray(d) ? d : []) : d = d && b.isPlainObject(d) ? d : {}, g[c] = b.extend(q, d, e)) : e !== k && (g[c] = e));
            return g
        };
        b.extend({noConflict:function (a) {
            if (n.$ === b)n.$ = e;
            if (a && n.jQuery === b)n.jQuery = c;
            return b
        }, isReady:!1, readyWait:1, holdReady:function (a) {
            a ? b.readyWait++ : b.ready(!0)
        }, ready:function (a) {
            if (a === !0 && !--b.readyWait || a !== !0 && !b.isReady) {
                if (!m.body)return setTimeout(b.ready,
                        1);
                b.isReady = !0;
                a !== !0 && --b.readyWait > 0 || (Y.resolveWith(m, [b]), b.fn.trigger && b(m).trigger("ready").unbind("ready"))
            }
        }, bindReady:function () {
            if (!Y) {
                Y = b._Deferred();
                if (m.readyState === "complete")return setTimeout(b.ready, 1);
                if (m.addEventListener)m.addEventListener("DOMContentLoaded", P, !1), n.addEventListener("load", b.ready, !1); else if (m.attachEvent) {
                    m.attachEvent("onreadystatechange", P);
                    n.attachEvent("onload", b.ready);
                    var c = !1;
                    try {
                        c = n.frameElement == null
                    } catch (d) {
                    }
                    m.documentElement.doScroll && c && a()
                }
            }
        },
            isFunction:function (a) {
                return b.type(a) === "function"
            }, isArray:Array.isArray || function (a) {
                return b.type(a) === "array"
            }, isWindow:function (a) {
                return a && typeof a === "object" && "setInterval"in a
            }, isNaN:function (a) {
                return a == null || !l.test(a) || isNaN(a)
            }, type:function (a) {
                return a == null ? String(a) : Fa[pb.call(a)] || "object"
            }, isPlainObject:function (a) {
                if (!a || b.type(a) !== "object" || a.nodeType || b.isWindow(a))return!1;
                if (a.constructor && !ga.call(a, "constructor") && !ga.call(a.constructor.prototype, "isPrototypeOf"))return!1;
                for (var c in a);
                return c === k || ga.call(a, c)
            }, isEmptyObject:function (a) {
                for (var b in a)return!1;
                return!0
            }, error:function (a) {
                throw a;
            }, parseJSON:function (a) {
                if (typeof a !== "string" || !a)return null;
                a = b.trim(a);
                if (n.JSON && n.JSON.parse)return n.JSON.parse(a);
                if (T.test(a.replace(A, "@").replace(p, "]").replace(s, "")))return(new Function("return " + a))();
                b.error("Invalid JSON: " + a)
            }, parseXML:function (a, c, d) {
                n.DOMParser ? (d = new DOMParser, c = d.parseFromString(a, "text/xml")) : (c = new ActiveXObject("Microsoft.XMLDOM"),
                        c.async = "false", c.loadXML(a));
                d = c.documentElement;
                (!d || !d.nodeName || d.nodeName === "parsererror") && b.error("Invalid XML: " + a);
                return c
            }, noop:function () {
            }, globalEval:function (a) {
                a && h.test(a) && (n.execScript || function (a) {
                    n.eval.call(n, a)
                })(a)
            }, camelCase:function (a) {
                return a.replace(u, q)
            }, nodeName:function (a, b) {
                return a.nodeName && a.nodeName.toUpperCase() === b.toUpperCase()
            }, each:function (a, c, d) {
                var e, f = 0, g = a.length, u = g === k || b.isFunction(a);
                if (d)if (u)for (e in a) {
                    if (c.apply(a[e], d) === !1)break
                } else for (; f <
                                      g;) {
                    if (c.apply(a[f++], d) === !1)break
                } else if (u)for (e in a) {
                    if (c.call(a[e], e, a[e]) === !1)break
                } else for (; f < g;)if (c.call(a[f], f, a[f++]) === !1)break;
                return a
            }, trim:Ea ? function (a) {
                return a == null ? "" : Ea.call(a)
            } : function (a) {
                return a == null ? "" : a.toString().replace(i, "").replace(j, "")
            }, makeArray:function (a, c) {
                var d = c || [];
                if (a != null) {
                    var e = b.type(a);
                    a.length == null || e === "string" || e === "function" || e === "regexp" || b.isWindow(a) ? ha.call(d, a) : b.merge(d, a)
                }
                return d
            }, inArray:function (a, b) {
                if (R)return R.call(b, a);
                for (var c =
                        0, d = b.length; c < d; c++)if (b[c] === a)return c;
                return-1
            }, merge:function (a, b) {
                var c = a.length, d = 0;
                if (typeof b.length === "number")for (var e = b.length; d < e; d++)a[c++] = b[d]; else for (; b[d] !== k;)a[c++] = b[d++];
                a.length = c;
                return a
            }, grep:function (a, b, c) {
                for (var d = [], e, c = !!c, f = 0, g = a.length; f < g; f++)e = !!b(a[f], f), c !== e && d.push(a[f]);
                return d
            }, map:function (a, c, d) {
                var e, f, g = [], u = 0, h = a.length;
                if (a instanceof b || h !== k && typeof h === "number" && (h > 0 && a[0] && a[h - 1] || h === 0 || b.isArray(a)))for (; u < h; u++)e = c(a[u], u, d), e != null && (g[g.length] =
                        e); else for (f in a)e = c(a[f], f, d), e != null && (g[g.length] = e);
                return g.concat.apply([], g)
            }, guid:1, proxy:function (a, c) {
                if (typeof c === "string")var d = a[c], c = a, a = d;
                if (!b.isFunction(a))return k;
                var e = Q.call(arguments, 2), d = function () {
                    return a.apply(c, e.concat(Q.call(arguments)))
                };
                d.guid = a.guid = a.guid || d.guid || b.guid++;
                return d
            }, access:function (a, c, d, e, f, g) {
                var u = a.length;
                if (typeof c === "object") {
                    for (var h in c)b.access(a, h, c[h], e, f, d);
                    return a
                }
                if (d !== k) {
                    e = !g && e && b.isFunction(d);
                    for (h = 0; h < u; h++)f(a[h], c, e ? d.call(a[h],
                            h, f(a[h], c)) : d, g);
                    return a
                }
                return u ? f(a[0], c) : k
            }, now:function () {
                return(new Date).getTime()
            }, uaMatch:function (a) {
                a = a.toLowerCase();
                a = w.exec(a) || t.exec(a) || z.exec(a) || a.indexOf("compatible") < 0 && v.exec(a) || [];
                return{browser:a[1] || "", version:a[2] || "0"}
            }, sub:function () {
                function a(b, c) {
                    return new a.fn.init(b, c)
                }

                b.extend(!0, a, this);
                a.superclass = this;
                a.fn = a.prototype = this();
                a.fn.constructor = a;
                a.sub = this.sub;
                a.fn.init = function (d, e) {
                    e && e instanceof b && !(e instanceof a) && (e = a(e));
                    return b.fn.init.call(this,
                            d, e, c)
                };
                a.fn.init.prototype = a.fn;
                var c = a(m);
                return a
            }, browser:{}});
        b.each("Boolean Number String Function Array Date RegExp Object".split(" "), function (a, b) {
            Fa["[object " + b + "]"] = b.toLowerCase()
        });
        F = b.uaMatch(F);
        if (F.browser)b.browser[F.browser] = !0, b.browser.version = F.version;
        if (b.browser.webkit)b.browser.safari = !0;
        h.test("\u00a0") && (i = /^[\s\xA0]+/, j = /[\s\xA0]+$/);
        f = b(m);
        m.addEventListener ? P = function () {
            m.removeEventListener("DOMContentLoaded", P, !1);
            b.ready()
        } : m.attachEvent && (P = function () {
            m.readyState ===
                    "complete" && (m.detachEvent("onreadystatechange", P), b.ready())
        });
        return b
    }(), ia = "done fail isResolved isRejected promise then always pipe".split(" "), Ga = [].slice;
    c.extend({_Deferred:function () {
        var a = [], b, d, e, f = {done:function () {
            if (!e) {
                var d = arguments, h, i, j, l, o;
                b && (o = b, b = 0);
                for (h = 0, i = d.length; h < i; h++)j = d[h], l = c.type(j), l === "array" ? f.done.apply(f, j) : l === "function" && a.push(j);
                o && f.resolveWith(o[0], o[1])
            }
            return this
        }, resolveWith:function (c, f) {
            if (!e && !b && !d) {
                f = f || [];
                d = 1;
                try {
                    for (; a[0];)a.shift().apply(c,
                            f)
                } finally {
                    b = [c, f], d = 0
                }
            }
            return this
        }, resolve:function () {
            f.resolveWith(this, arguments);
            return this
        }, isResolved:function () {
            return!(!d && !b)
        }, cancel:function () {
            e = 1;
            a = [];
            return this
        }};
        return f
    }, Deferred:function (a) {
        var b = c._Deferred(), d = c._Deferred(), e;
        c.extend(b, {then:function (a, c) {
            b.done(a).fail(c);
            return this
        }, always:function () {
            return b.done.apply(b, arguments).fail.apply(this, arguments)
        }, fail:d.done, rejectWith:d.resolveWith, reject:d.resolve, isRejected:d.isResolved, pipe:function (a, d) {
            return c.Deferred(
                    function (e) {
                        c.each({done:[a,
                            "resolve"], fail:[d, "reject"]}, function (a, d) {
                            var f = d[0], g = d[1], k;
                            if (c.isFunction(f))b[a](function () {
                                if ((k = f.apply(this, arguments)) && c.isFunction(k.promise))k.promise().then(e.resolve, e.reject); else e[g](k)
                            }); else b[a](e[g])
                        })
                    }).promise()
        }, promise:function (a) {
            if (a == null) {
                if (e)return e;
                e = a = {}
            }
            for (var c = ia.length; c--;)a[ia[c]] = b[ia[c]];
            return a
        }});
        b.done(d.cancel).fail(b.cancel);
        delete b.cancel;
        a && a.call(b, b);
        return b
    }, when:function (a) {
        function b(a) {
            return function (b) {
                d[a] = arguments.length > 1 ? Ga.call(arguments,
                        0) : b;
                --g || h.resolveWith(h, Ga.call(d, 0))
            }
        }

        var d = arguments, e = 0, f = d.length, g = f, h = f <= 1 && a && c.isFunction(a.promise) ? a : c.Deferred();
        if (f > 1) {
            for (; e < f; e++)d[e] && c.isFunction(d[e].promise) ? d[e].promise().then(b(e), h.reject) : --g;
            g || h.resolveWith(h, d)
        } else h !== a && h.resolveWith(h, f ? [a] : []);
        return h.promise()
    }});
    c.support = function () {
        var a = m.createElement("div"), b = m.documentElement, d, e, f, g, h, i;
        a.setAttribute("className", "t");
        a.innerHTML = "   <link/><table></table><a href='/a' style='top:1px;float:left;opacity:.55;'>a</a><input type='checkbox'/>";
        d = a.getElementsByTagName("*");
        e = a.getElementsByTagName("a")[0];
        if (!d || !d.length || !e)return{};
        f = m.createElement("select");
        g = f.appendChild(m.createElement("option"));
        d = a.getElementsByTagName("input")[0];
        h = {leadingWhitespace:a.firstChild.nodeType === 3, tbody:!a.getElementsByTagName("tbody").length, htmlSerialize:!!a.getElementsByTagName("link").length, style:/top/.test(e.getAttribute("style")), hrefNormalized:e.getAttribute("href") === "/a", opacity:/^0.55$/.test(e.style.opacity), cssFloat:!!e.style.cssFloat,
            checkOn:d.value === "on", optSelected:g.selected, getSetAttribute:a.className !== "t", submitBubbles:!0, changeBubbles:!0, focusinBubbles:!1, deleteExpando:!0, noCloneEvent:!0, inlineBlockNeedsLayout:!1, shrinkWrapBlocks:!1, reliableMarginRight:!0};
        d.checked = !0;
        h.noCloneChecked = d.cloneNode(!0).checked;
        f.disabled = !0;
        h.optDisabled = !g.disabled;
        try {
            delete a.test
        } catch (j) {
            h.deleteExpando = !1
        }
        !a.addEventListener && a.attachEvent && a.fireEvent && (a.attachEvent("onclick", function () {
            h.noCloneEvent = !1
        }), a.cloneNode(!0).fireEvent("onclick"));
        d = m.createElement("input");
        d.value = "t";
        d.setAttribute("type", "radio");
        h.radioValue = d.value === "t";
        d.setAttribute("checked", "checked");
        a.appendChild(d);
        e = m.createDocumentFragment();
        e.appendChild(a.firstChild);
        h.checkClone = e.cloneNode(!0).cloneNode(!0).lastChild.checked;
        a.innerHTML = "";
        a.style.width = a.style.paddingLeft = "1px";
        f = m.getElementsByTagName("body")[0];
        e = m.createElement(f ? "div" : "body");
        g = {visibility:"hidden", width:0, height:0, border:0, margin:0};
        f && c.extend(g, {position:"absolute", left:-1E3, top:-1E3});
        for (i in g)e.style[i] = g[i];
        e.appendChild(a);
        b = f || b;
        b.insertBefore(e, b.firstChild);
        h.appendChecked = d.checked;
        h.boxModel = a.offsetWidth === 2;
        if ("zoom"in a.style)a.style.display = "inline", a.style.zoom = 1, h.inlineBlockNeedsLayout = a.offsetWidth === 2, a.style.display = "", a.innerHTML = "<div style='width:4px;'></div>", h.shrinkWrapBlocks = a.offsetWidth !== 2;
        a.innerHTML = "<table><tr><td style='padding:0;border:0;display:none'></td><td>t</td></tr></table>";
        f = a.getElementsByTagName("td");
        d = f[0].offsetHeight === 0;
        f[0].style.display =
                "";
        f[1].style.display = "none";
        h.reliableHiddenOffsets = d && f[0].offsetHeight === 0;
        a.innerHTML = "";
        if (m.defaultView && m.defaultView.getComputedStyle)d = m.createElement("div"), d.style.width = "0", d.style.marginRight = "0", a.appendChild(d), h.reliableMarginRight = (parseInt((m.defaultView.getComputedStyle(d, null) || {marginRight:0}).marginRight, 10) || 0) === 0;
        e.innerHTML = "";
        b.removeChild(e);
        if (a.attachEvent)for (i in{submit:1, change:1, focusin:1})b = "on" + i, d = b in a, d || (a.setAttribute(b, "return;"), d = typeof a[b] === "function"),
                h[i + "Bubbles"] = d;
        e = e = f = g = f = d = a = d = null;
        return h
    }();
    c.boxModel = c.support.boxModel;
    var cb = /^(?:\{.*\}|\[.*\])$/, bb = /([a-z])([A-Z])/g;
    c.extend({cache:{}, uuid:0, expando:"jQuery" + (c.fn.jquery + Math.random()).replace(/\D/g, ""), noData:{embed:!0, object:"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000", applet:!0}, hasData:function (a) {
        a = a.nodeType ? c.cache[a[c.expando]] : a[c.expando];
        return!!a && !aa(a)
    }, data:function (a, b, d, e) {
        if (c.acceptData(a)) {
            var f = c.expando, g = typeof b === "string", h = a.nodeType, i = h ? c.cache : a, j = h ? a[c.expando] :
                    a[c.expando] && c.expando;
            if (j && (!e || !j || i[j][f]) || !(g && d === k)) {
                if (!j)h ? a[c.expando] = j = ++c.uuid : j = c.expando;
                if (!i[j] && (i[j] = {}, !h))i[j].toJSON = c.noop;
                if (typeof b === "object" || typeof b === "function")e ? i[j][f] = c.extend(i[j][f], b) : i[j] = c.extend(i[j], b);
                a = i[j];
                e && (a[f] || (a[f] = {}), a = a[f]);
                d !== k && (a[c.camelCase(b)] = d);
                return b === "events" && !a[b] ? a[f] && a[f].events : g ? a[c.camelCase(b)] || a[b] : a
            }
        }
    }, removeData:function (a, b, d) {
        if (c.acceptData(a)) {
            var e = c.expando, f = a.nodeType, g = f ? c.cache : a, h = f ? a[c.expando] : c.expando;
            if (g[h]) {
                if (b) {
                    var i = d ? g[h][e] : g[h];
                    if (i && (delete i[b], !aa(i)))return
                }
                if (d && (delete g[h][e], !aa(g[h])))return;
                b = g[h][e];
                c.support.deleteExpando || g != n ? delete g[h] : g[h] = null;
                if (b) {
                    g[h] = {};
                    if (!f)g[h].toJSON = c.noop;
                    g[h][e] = b
                } else f && (c.support.deleteExpando ? delete a[c.expando] : a.removeAttribute ? a.removeAttribute(c.expando) : a[c.expando] = null)
            }
        }
    }, _data:function (a, b, d) {
        return c.data(a, b, d, !0)
    }, acceptData:function (a) {
        if (a.nodeName) {
            var b = c.noData[a.nodeName.toLowerCase()];
            if (b)return!(b === !0 || a.getAttribute("classid") !==
                    b)
        }
        return!0
    }});
    c.fn.extend({data:function (a, b) {
        var d = null;
        if (typeof a === "undefined") {
            if (this.length && (d = c.data(this[0]), this[0].nodeType === 1))for (var e = this[0].attributes, f, g = 0, h = e.length; g < h; g++)f = e[g].name, f.indexOf("data-") === 0 && (f = c.camelCase(f.substring(5)), pa(this[0], f, d[f]));
            return d
        } else if (typeof a === "object")return this.each(function () {
            c.data(this, a)
        });
        var i = a.split(".");
        i[1] = i[1] ? "." + i[1] : "";
        return b === k ? (d = this.triggerHandler("getData" + i[1] + "!", [i[0]]), d === k && this.length && (d = c.data(this[0],
                a), d = pa(this[0], a, d)), d === k && i[1] ? this.data(i[0]) : d) : this.each(function () {
            var d = c(this), e = [i[0], b];
            d.triggerHandler("setData" + i[1] + "!", e);
            c.data(this, a, b);
            d.triggerHandler("changeData" + i[1] + "!", e)
        })
    }, removeData:function (a) {
        return this.each(function () {
            c.removeData(this, a)
        })
    }});
    c.extend({_mark:function (a, b) {
        a && (b = (b || "fx") + "mark", c.data(a, b, (c.data(a, b, k, !0) || 0) + 1, !0))
    }, _unmark:function (a, b, d) {
        a !== !0 && (d = b, b = a, a = !1);
        if (b) {
            var d = d || "fx", e = d + "mark";
            (a = a ? 0 : (c.data(b, e, k, !0) || 1) - 1) ? c.data(b, e, a, !0) : (c.removeData(b,
                    e, !0), qa(b, d, "mark"))
        }
    }, queue:function (a, b, d) {
        if (a) {
            var b = (b || "fx") + "queue", e = c.data(a, b, k, !0);
            d && (!e || c.isArray(d) ? e = c.data(a, b, c.makeArray(d), !0) : e.push(d));
            return e || []
        }
    }, dequeue:function (a, b) {
        var b = b || "fx", d = c.queue(a, b), e = d.shift();
        e === "inprogress" && (e = d.shift());
        e && (b === "fx" && d.unshift("inprogress"), e.call(a, function () {
            c.dequeue(a, b)
        }));
        d.length || (c.removeData(a, b + "queue", !0), qa(a, b, "queue"))
    }});
    c.fn.extend({queue:function (a, b) {
        typeof a !== "string" && (b = a, a = "fx");
        return b === k ? c.queue(this[0],
                a) : this.each(function () {
            var d = c.queue(this, a, b);
            a === "fx" && d[0] !== "inprogress" && c.dequeue(this, a)
        })
    }, dequeue:function (a) {
        return this.each(function () {
            c.dequeue(this, a)
        })
    }, delay:function (a, b) {
        a = c.fx ? c.fx.speeds[a] || a : a;
        b = b || "fx";
        return this.queue(b, function () {
            var d = this;
            setTimeout(function () {
                c.dequeue(d, b)
            }, a)
        })
    }, clearQueue:function (a) {
        return this.queue(a || "fx", [])
    }, promise:function (a) {
        function b() {
            --g || d.resolveWith(e, [e])
        }

        typeof a !== "string" && (a = k);
        var a = a || "fx", d = c.Deferred(), e = this, f = e.length, g =
                1, h = a + "defer", i = a + "queue";
        a += "mark";
        for (var j; f--;)if (j = c.data(e[f], h, k, !0) || (c.data(e[f], i, k, !0) || c.data(e[f], a, k, !0)) && c.data(e[f], h, c._Deferred(), !0))g++, j.done(b);
        b();
        return d.promise()
    }});
    var Ha = /[\n\t\r]/g, ja = /\s+/, qb = /\r/g, rb = /^(?:button|input)$/i, sb = /^(?:button|input|object|select|textarea)$/i, tb = /^a(?:rea)?$/i, Ia = /^(?:autofocus|autoplay|async|checked|controls|defer|disabled|hidden|loop|multiple|open|readonly|required|scoped|selected)$/i, ub = /\:|^on/, I, Ja;
    c.fn.extend({attr:function (a, b) {
        return c.access(this,
                a, b, !0, c.attr)
    }, removeAttr:function (a) {
        return this.each(function () {
            c.removeAttr(this, a)
        })
    }, prop:function (a, b) {
        return c.access(this, a, b, !0, c.prop)
    }, removeProp:function (a) {
        a = c.propFix[a] || a;
        return this.each(function () {
            try {
                this[a] = k, delete this[a]
            } catch (b) {
            }
        })
    }, addClass:function (a) {
        var b, d, e, f, g, h, i;
        if (c.isFunction(a))return this.each(function (b) {
            c(this).addClass(a.call(this, b, this.className))
        });
        if (a && typeof a === "string") {
            b = a.split(ja);
            for (d = 0, e = this.length; d < e; d++)if (f = this[d], f.nodeType === 1)if (!f.className &&
                    b.length === 1)f.className = a; else {
                g = " " + f.className + " ";
                for (h = 0, i = b.length; h < i; h++)~g.indexOf(" " + b[h] + " ") || (g += b[h] + " ");
                f.className = c.trim(g)
            }
        }
        return this
    }, removeClass:function (a) {
        var b, d, e, f, g, h, i;
        if (c.isFunction(a))return this.each(function (b) {
            c(this).removeClass(a.call(this, b, this.className))
        });
        if (a && typeof a === "string" || a === k) {
            b = (a || "").split(ja);
            for (d = 0, e = this.length; d < e; d++)if (f = this[d], f.nodeType === 1 && f.className)if (a) {
                g = (" " + f.className + " ").replace(Ha, " ");
                for (h = 0, i = b.length; h < i; h++)g = g.replace(" " +
                        b[h] + " ", " ");
                f.className = c.trim(g)
            } else f.className = ""
        }
        return this
    }, toggleClass:function (a, b) {
        var d = typeof a, e = typeof b === "boolean";
        return c.isFunction(a) ? this.each(function (d) {
            c(this).toggleClass(a.call(this, d, this.className, b), b)
        }) : this.each(function () {
            if (d === "string")for (var f, g = 0, h = c(this), i = b, j = a.split(ja); f = j[g++];)i = e ? i : !h.hasClass(f), h[i ? "addClass" : "removeClass"](f); else if (d === "undefined" || d === "boolean")this.className && c._data(this, "__className__", this.className), this.className = this.className ||
                    a === !1 ? "" : c._data(this, "__className__") || ""
        })
    }, hasClass:function (a) {
        for (var a = " " + a + " ", b = 0, c = this.length; b < c; b++)if ((" " + this[b].className + " ").replace(Ha, " ").indexOf(a) > -1)return!0;
        return!1
    }, val:function (a) {
        var b, d, e = this[0];
        if (!arguments.length) {
            if (e) {
                if ((b = c.valHooks[e.nodeName.toLowerCase()] || c.valHooks[e.type]) && "get"in b && (d = b.get(e, "value")) !== k)return d;
                d = e.value;
                return typeof d === "string" ? d.replace(qb, "") : d == null ? "" : d
            }
            return k
        }
        var f = c.isFunction(a);
        return this.each(function (d) {
            var e = c(this);
            if (this.nodeType === 1 && (d = f ? a.call(this, d, e.val()) : a, d == null ? d = "" : typeof d === "number" ? d += "" : c.isArray(d) && (d = c.map(d, function (a) {
                return a == null ? "" : a + ""
            })), b = c.valHooks[this.nodeName.toLowerCase()] || c.valHooks[this.type], !b || !("set"in b) || b.set(this, d, "value") === k))this.value = d
        })
    }});
    c.extend({valHooks:{option:{get:function (a) {
        var b = a.attributes.value;
        return!b || b.specified ? a.value : a.text
    }}, select:{get:function (a) {
        var b, d = a.selectedIndex, e = [], f = a.options, a = a.type === "select-one";
        if (d < 0)return null;
        for (var g =
                a ? d : 0, h = a ? d + 1 : f.length; g < h; g++)if (b = f[g], b.selected && (c.support.optDisabled ? !b.disabled : b.getAttribute("disabled") === null) && (!b.parentNode.disabled || !c.nodeName(b.parentNode, "optgroup"))) {
            b = c(b).val();
            if (a)return b;
            e.push(b)
        }
        return a && !e.length && f.length ? c(f[d]).val() : e
    }, set:function (a, b) {
        var d = c.makeArray(b);
        c(a).find("option").each(function () {
            this.selected = c.inArray(c(this).val(), d) >= 0
        });
        if (!d.length)a.selectedIndex = -1;
        return d
    }}}, attrFn:{val:!0, css:!0, html:!0, text:!0, data:!0, width:!0, height:!0,
        offset:!0}, attrFix:{tabindex:"tabIndex"}, attr:function (a, b, d, e) {
        var f = a.nodeType;
        if (!a || f === 3 || f === 8 || f === 2)return k;
        if (e && b in c.attrFn)return c(a)[b](d);
        if (!("getAttribute"in a))return c.prop(a, b, d);
        var g, h;
        if (e = f !== 1 || !c.isXMLDoc(a))if (b = c.attrFix[b] || b, h = c.attrHooks[b], !h)if (Ia.test(b))h = Ja; else if (I && b !== "className" && (c.nodeName(a, "form") || ub.test(b)))h = I;
        return d !== k ? d === null ? (c.removeAttr(a, b), k) : h && "set"in h && e && (g = h.set(a, d, b)) !== k ? g : (a.setAttribute(b, "" + d), d) : h && "get"in h && e && (g = h.get(a,
                b)) !== null ? g : (g = a.getAttribute(b), g === null ? k : g)
    }, removeAttr:function (a, b) {
        var d;
        if (a.nodeType === 1 && (b = c.attrFix[b] || b, c.support.getSetAttribute ? a.removeAttribute(b) : (c.attr(a, b, ""), a.removeAttributeNode(a.getAttributeNode(b))), Ia.test(b) && (d = c.propFix[b] || b)in a))a[d] = !1
    }, attrHooks:{type:{set:function (a, b) {
        if (rb.test(a.nodeName) && a.parentNode)c.error("type property can't be changed"); else if (!c.support.radioValue && b === "radio" && c.nodeName(a, "input")) {
            var d = a.value;
            a.setAttribute("type", b);
            if (d)a.value =
                    d;
            return b
        }
    }}, tabIndex:{get:function (a) {
        var b = a.getAttributeNode("tabIndex");
        return b && b.specified ? parseInt(b.value, 10) : sb.test(a.nodeName) || tb.test(a.nodeName) && a.href ? 0 : k
    }}, value:{get:function (a, b) {
        return I && c.nodeName(a, "button") ? I.get(a, b) : b in a ? a.value : null
    }, set:function (a, b, d) {
        if (I && c.nodeName(a, "button"))return I.set(a, b, d);
        a.value = b
    }}}, propFix:{tabindex:"tabIndex", readonly:"readOnly", "for":"htmlFor", "class":"className", maxlength:"maxLength", cellspacing:"cellSpacing", cellpadding:"cellPadding",
        rowspan:"rowSpan", colspan:"colSpan", usemap:"useMap", frameborder:"frameBorder", contenteditable:"contentEditable"}, prop:function (a, b, d) {
        var e = a.nodeType;
        if (!a || e === 3 || e === 8 || e === 2)return k;
        var f, g;
        if (e !== 1 || !c.isXMLDoc(a))b = c.propFix[b] || b, g = c.propHooks[b];
        return d !== k ? g && "set"in g && (f = g.set(a, d, b)) !== k ? f : a[b] = d : g && "get"in g && (f = g.get(a, b)) !== k ? f : a[b]
    }, propHooks:{}});
    Ja = {get:function (a, b) {
        return c.prop(a, b) ? b.toLowerCase() : k
    }, set:function (a, b, d) {
        b === !1 ? c.removeAttr(a, d) : (b = c.propFix[d] || d, b in a && (a[b] =
                !0), a.setAttribute(d, d.toLowerCase()));
        return d
    }};
    if (!c.support.getSetAttribute)c.attrFix = c.propFix, I = c.attrHooks.name = c.attrHooks.title = c.valHooks.button = {get:function (a, b) {
        var c;
        return(c = a.getAttributeNode(b)) && c.nodeValue !== "" ? c.nodeValue : k
    }, set:function (a, b, c) {
        if (a = a.getAttributeNode(c))return a.nodeValue = b
    }}, c.each(["width", "height"], function (a, b) {
        c.attrHooks[b] = c.extend(c.attrHooks[b], {set:function (a, c) {
            if (c === "")return a.setAttribute(b, "auto"), c
        }})
    });
    c.support.hrefNormalized || c.each(["href",
        "src", "width", "height"], function (a, b) {
        c.attrHooks[b] = c.extend(c.attrHooks[b], {get:function (a) {
            a = a.getAttribute(b, 2);
            return a === null ? k : a
        }})
    });
    if (!c.support.style)c.attrHooks.style = {get:function (a) {
        return a.style.cssText.toLowerCase() || k
    }, set:function (a, b) {
        return a.style.cssText = "" + b
    }};
    if (!c.support.optSelected)c.propHooks.selected = c.extend(c.propHooks.selected, {get:function () {
    }});
    c.support.checkOn || c.each(["radio", "checkbox"], function () {
        c.valHooks[this] = {get:function (a) {
            return a.getAttribute("value") ===
                    null ? "on" : a.value
        }}
    });
    c.each(["radio", "checkbox"], function () {
        c.valHooks[this] = c.extend(c.valHooks[this], {set:function (a, b) {
            if (c.isArray(b))return a.checked = c.inArray(c(a).val(), b) >= 0
        }})
    });
    var ba = /\.(.*)$/, ka = /^(?:textarea|input|select)$/i, eb = /\./g, fb = / /g, vb = /[^\w\s.|`]/g, wb = function (a) {
        return a.replace(vb, "\\$&")
    };
    c.event = {add:function (a, b, d, e) {
        if (!(a.nodeType === 3 || a.nodeType === 8)) {
            if (d === !1)d = x; else if (!d)return;
            var f, g;
            if (d.handler)f = d, d = f.handler;
            if (!d.guid)d.guid = c.guid++;
            if (g = c._data(a)) {
                var h =
                        g.events, i = g.handle;
                if (!h)g.events = h = {};
                if (!i)g.handle = i = function (a) {
                    return typeof c !== "undefined" && (!a || c.event.triggered !== a.type) ? c.event.handle.apply(i.elem, arguments) : k
                };
                i.elem = a;
                for (var b = b.split(" "), j, l = 0, o; j = b[l++];) {
                    g = f ? c.extend({}, f) : {handler:d, data:e};
                    j.indexOf(".") > -1 ? (o = j.split("."), j = o.shift(), g.namespace = o.slice(0).sort().join(".")) : (o = [], g.namespace = "");
                    g.type = j;
                    if (!g.guid)g.guid = d.guid;
                    var m = h[j], n = c.event.special[j] || {};
                    if (!m && (m = h[j] = [], !n.setup || n.setup.call(a, e, o, i) === !1))a.addEventListener ?
                            a.addEventListener(j, i, !1) : a.attachEvent && a.attachEvent("on" + j, i);
                    if (n.add && (n.add.call(a, g), !g.handler.guid))g.handler.guid = d.guid;
                    m.push(g);
                    c.event.global[j] = !0
                }
                a = null
            }
        }
    }, global:{}, remove:function (a, b, d, e) {
        if (!(a.nodeType === 3 || a.nodeType === 8)) {
            d === !1 && (d = x);
            var f, g, h = 0, i, j, l, o, m, n, p = c.hasData(a) && c._data(a), s = p && p.events;
            if (p && s) {
                if (b && b.type)d = b.handler, b = b.type;
                if (!b || typeof b === "string" && b.charAt(0) === ".")for (f in b = b || "", s)c.event.remove(a, f + b); else {
                    for (b = b.split(" "); f = b[h++];)if (o = f, i = f.indexOf(".") <
                            0, j = [], i || (j = f.split("."), f = j.shift(), l = RegExp("(^|\\.)" + c.map(j.slice(0).sort(), wb).join("\\.(?:.*\\.)?") + "(\\.|$)")), m = s[f])if (d) {
                        o = c.event.special[f] || {};
                        for (g = e || 0; g < m.length; g++)if (n = m[g], d.guid === n.guid) {
                            if (i || l.test(n.namespace))e == null && m.splice(g--, 1), o.remove && o.remove.call(a, n);
                            if (e != null)break
                        }
                        if (m.length === 0 || e != null && m.length === 1)(!o.teardown || o.teardown.call(a, j) === !1) && c.removeEvent(a, f, p.handle), delete s[f]
                    } else for (g = 0; g < m.length; g++)if (n = m[g], i || l.test(n.namespace))c.event.remove(a,
                            o, n.handler, g), m.splice(g--, 1);
                    if (c.isEmptyObject(s)) {
                        if (b = p.handle)b.elem = null;
                        delete p.events;
                        delete p.handle;
                        c.isEmptyObject(p) && c.removeData(a, k, !0)
                    }
                }
            }
        }
    }, customEvent:{getData:!0, setData:!0, changeData:!0}, trigger:function (a, b, d, e) {
        var f = a.type || a, g = [], h;
        f.indexOf("!") >= 0 && (f = f.slice(0, -1), h = !0);
        f.indexOf(".") >= 0 && (g = f.split("."), f = g.shift(), g.sort());
        if (d && !c.event.customEvent[f] || c.event.global[f]) {
            a = typeof a === "object" ? a[c.expando] ? a : new c.Event(f, a) : new c.Event(f);
            a.type = f;
            a.exclusive = h;
            a.namespace =
                    g.join(".");
            a.namespace_re = RegExp("(^|\\.)" + g.join("\\.(?:.*\\.)?") + "(\\.|$)");
            if (e || !d)a.preventDefault(), a.stopPropagation();
            if (d) {
                if (!(d.nodeType === 3 || d.nodeType === 8)) {
                    a.result = k;
                    a.target = d;
                    b = b != null ? c.makeArray(b) : [];
                    b.unshift(a);
                    g = d;
                    e = f.indexOf(":") < 0 ? "on" + f : "";
                    do {
                        h = c._data(g, "handle");
                        a.currentTarget = g;
                        h && h.apply(g, b);
                        if (e && c.acceptData(g) && g[e] && g[e].apply(g, b) === !1)a.result = !1, a.preventDefault();
                        g = g.parentNode || g.ownerDocument || g === a.target.ownerDocument && n
                    } while (g && !a.isPropagationStopped());
                    if (!a.isDefaultPrevented()) {
                        var i, g = c.event.special[f] || {};
                        if ((!g._default || g._default.call(d.ownerDocument, a) === !1) && !(f === "click" && c.nodeName(d, "a")) && c.acceptData(d)) {
                            try {
                                if (e && d[f])(i = d[e]) && (d[e] = null), c.event.triggered = f, d[f]()
                            } catch (j) {
                            }
                            i && (d[e] = i);
                            c.event.triggered = k
                        }
                    }
                    return a.result
                }
            } else c.each(c.cache, function () {
                var d = this[c.expando];
                d && d.events && d.events[f] && c.event.trigger(a, b, d.handle.elem)
            })
        }
    }, handle:function (a) {
        var a = c.event.fix(a || n.event), b = ((c._data(this, "events") || {})[a.type] ||
                []).slice(0), d = !a.exclusive && !a.namespace, e = Array.prototype.slice.call(arguments, 0);
        e[0] = a;
        a.currentTarget = this;
        for (var f = 0, g = b.length; f < g; f++) {
            var h = b[f];
            if (d || a.namespace_re.test(h.namespace)) {
                a.handler = h.handler;
                a.data = h.data;
                a.handleObj = h;
                h = h.handler.apply(this, e);
                if (h !== k)a.result = h, h === !1 && (a.preventDefault(), a.stopPropagation());
                if (a.isImmediatePropagationStopped())break
            }
        }
        return a.result
    }, props:"altKey attrChange attrName bubbles button cancelable charCode clientX clientY ctrlKey currentTarget data detail eventPhase fromElement handler keyCode layerX layerY metaKey newValue offsetX offsetY pageX pageY prevValue relatedNode relatedTarget screenX screenY shiftKey srcElement target toElement view wheelDelta which".split(" "),
        fix:function (a) {
            if (a[c.expando])return a;
            for (var b = a, a = c.Event(b), d = this.props.length, e; d;)e = this.props[--d], a[e] = b[e];
            if (!a.target)a.target = a.srcElement || m;
            if (a.target.nodeType === 3)a.target = a.target.parentNode;
            if (!a.relatedTarget && a.fromElement)a.relatedTarget = a.fromElement === a.target ? a.toElement : a.fromElement;
            if (a.pageX == null && a.clientX != null)d = a.target.ownerDocument || m, b = d.documentElement, d = d.body, a.pageX = a.clientX + (b && b.scrollLeft || d && d.scrollLeft || 0) - (b && b.clientLeft || d && d.clientLeft || 0), a.pageY =
                    a.clientY + (b && b.scrollTop || d && d.scrollTop || 0) - (b && b.clientTop || d && d.clientTop || 0);
            if (a.which == null && (a.charCode != null || a.keyCode != null))a.which = a.charCode != null ? a.charCode : a.keyCode;
            if (!a.metaKey && a.ctrlKey)a.metaKey = a.ctrlKey;
            if (!a.which && a.button !== k)a.which = a.button & 1 ? 1 : a.button & 2 ? 3 : a.button & 4 ? 2 : 0;
            return a
        }, guid:1E8, proxy:c.proxy, special:{ready:{setup:c.bindReady, teardown:c.noop}, live:{add:function (a) {
            c.event.add(this, U(a.origType, a.selector), c.extend({}, a, {handler:db, guid:a.handler.guid}))
        }, remove:function (a) {
            c.event.remove(this,
                    U(a.origType, a.selector), a)
        }}, beforeunload:{setup:function (a, b, d) {
            if (c.isWindow(this))this.onbeforeunload = d
        }, teardown:function (a, b) {
            if (this.onbeforeunload === b)this.onbeforeunload = null
        }}}};
    c.removeEvent = m.removeEventListener ? function (a, b, c) {
        a.removeEventListener && a.removeEventListener(b, c, !1)
    } : function (a, b, c) {
        a.detachEvent && a.detachEvent("on" + b, c)
    };
    c.Event = function (a, b) {
        if (!this.preventDefault)return new c.Event(a, b);
        a && a.type ? (this.originalEvent = a, this.type = a.type, this.isDefaultPrevented = a.defaultPrevented ||
                a.returnValue === !1 || a.getPreventDefault && a.getPreventDefault() ? S : x) : this.type = a;
        b && c.extend(this, b);
        this.timeStamp = c.now();
        this[c.expando] = !0
    };
    c.Event.prototype = {preventDefault:function () {
        this.isDefaultPrevented = S;
        var a = this.originalEvent;
        if (a)a.preventDefault ? a.preventDefault() : a.returnValue = !1
    }, stopPropagation:function () {
        this.isPropagationStopped = S;
        var a = this.originalEvent;
        if (a)a.stopPropagation && a.stopPropagation(), a.cancelBubble = !0
    }, stopImmediatePropagation:function () {
        this.isImmediatePropagationStopped =
                S;
        this.stopPropagation()
    }, isDefaultPrevented:x, isPropagationStopped:x, isImmediatePropagationStopped:x};
    var Ka = function (a) {
        var b = a.relatedTarget, d = !1, e = a.type;
        a.type = a.data;
        if (b !== this && (b && (d = c.contains(this, b)), !d))c.event.handle.apply(this, arguments), a.type = e
    }, La = function (a) {
        a.type = a.data;
        c.event.handle.apply(this, arguments)
    };
    c.each({mouseenter:"mouseover", mouseleave:"mouseout"}, function (a, b) {
        c.event.special[a] = {setup:function (d) {
            c.event.add(this, b, d && d.selector ? La : Ka, a)
        }, teardown:function (a) {
            c.event.remove(this,
                    b, a && a.selector ? La : Ka)
        }}
    });
    if (!c.support.submitBubbles)c.event.special.submit = {setup:function () {
        if (c.nodeName(this, "form"))return!1; else c.event.add(this, "click.specialSubmit", function (a) {
            var b = a.target, d = b.type;
            (d === "submit" || d === "image") && c(b).closest("form").length && ra("submit", this, arguments)
        }), c.event.add(this, "keypress.specialSubmit", function (a) {
            var b = a.target, d = b.type;
            (d === "text" || d === "password") && c(b).closest("form").length && a.keyCode === 13 && ra("submit", this, arguments)
        })
    }, teardown:function () {
        c.event.remove(this,
                ".specialSubmit")
    }};
    if (!c.support.changeBubbles) {
        var N, Ma = function (a) {
            var b = a.type, d = a.value;
            if (b === "radio" || b === "checkbox")d = a.checked; else if (b === "select-multiple")d = a.selectedIndex > -1 ? c.map(a.options,
                    function (a) {
                        return a.selected
                    }).join("-") : ""; else if (c.nodeName(a, "select"))d = a.selectedIndex;
            return d
        }, Z = function (a, b) {
            var d = a.target, e, f;
            if (ka.test(d.nodeName) && !d.readOnly && (e = c._data(d, "_change_data"), f = Ma(d), (a.type !== "focusout" || d.type !== "radio") && c._data(d, "_change_data", f), !(e === k || f === e)))if (e !=
                    null || f)a.type = "change", a.liveFired = k, c.event.trigger(a, b, d)
        };
        c.event.special.change = {filters:{focusout:Z, beforedeactivate:Z, click:function (a) {
            var b = a.target, d = c.nodeName(b, "input") ? b.type : "";
            (d === "radio" || d === "checkbox" || c.nodeName(b, "select")) && Z.call(this, a)
        }, keydown:function (a) {
            var b = a.target, d = c.nodeName(b, "input") ? b.type : "";
            (a.keyCode === 13 && !c.nodeName(b, "textarea") || a.keyCode === 32 && (d === "checkbox" || d === "radio") || d === "select-multiple") && Z.call(this, a)
        }, beforeactivate:function (a) {
            a = a.target;
            c._data(a, "_change_data", Ma(a))
        }}, setup:function () {
            if (this.type === "file")return!1;
            for (var a in N)c.event.add(this, a + ".specialChange", N[a]);
            return ka.test(this.nodeName)
        }, teardown:function () {
            c.event.remove(this, ".specialChange");
            return ka.test(this.nodeName)
        }};
        N = c.event.special.change.filters;
        N.focus = N.beforeactivate
    }
    c.support.focusinBubbles || c.each({focus:"focusin", blur:"focusout"}, function (a, b) {
        function d(a) {
            var d = c.event.fix(a);
            d.type = b;
            d.originalEvent = {};
            c.event.trigger(d, null, d.target);
            d.isDefaultPrevented() &&
            a.preventDefault()
        }

        var e = 0;
        c.event.special[b] = {setup:function () {
            e++ === 0 && m.addEventListener(a, d, !0)
        }, teardown:function () {
            --e === 0 && m.removeEventListener(a, d, !0)
        }}
    });
    c.each(["bind", "one"], function (a, b) {
        c.fn[b] = function (a, e, f) {
            var g;
            if (typeof a === "object") {
                for (var h in a)this[b](h, e, a[h], f);
                return this
            }
            if (arguments.length === 2 || e === !1)f = e, e = k;
            b === "one" ? (g = function (a) {
                c(this).unbind(a, g);
                return f.apply(this, arguments)
            }, g.guid = f.guid || c.guid++) : g = f;
            if (a === "unload" && b !== "one")this.one(a, e, f); else {
                h = 0;
                for (var i =
                        this.length; h < i; h++)c.event.add(this[h], a, g, e)
            }
            return this
        }
    });
    c.fn.extend({unbind:function (a, b) {
        if (typeof a === "object" && !a.preventDefault)for (var d in a)this.unbind(d, a[d]); else {
            d = 0;
            for (var e = this.length; d < e; d++)c.event.remove(this[d], a, b)
        }
        return this
    }, delegate:function (a, b, c, e) {
        return this.live(b, c, e, a)
    }, undelegate:function (a, b, c) {
        return arguments.length === 0 ? this.unbind("live") : this.die(b, null, c, a)
    }, trigger:function (a, b) {
        return this.each(function () {
            c.event.trigger(a, b, this)
        })
    }, triggerHandler:function (a, b) {
        if (this[0])return c.event.trigger(a, b, this[0], !0)
    }, toggle:function (a) {
        var b = arguments, d = a.guid || c.guid++, e = 0, f = function (d) {
            var f = (c.data(this, "lastToggle" + a.guid) || 0) % e;
            c.data(this, "lastToggle" + a.guid, f + 1);
            d.preventDefault();
            return b[f].apply(this, arguments) || !1
        };
        for (f.guid = d; e < b.length;)b[e++].guid = d;
        return this.click(f)
    }, hover:function (a, b) {
        return this.mouseenter(a).mouseleave(b || a)
    }});
    var la = {focus:"focusin", blur:"focusout", mouseenter:"mouseover", mouseleave:"mouseout"};
    c.each(["live", "die"],
            function (a, b) {
                c.fn[b] = function (a, e, f, g) {
                    var h = 0, i, j, l = g || this.selector, o = g ? this : c(this.context);
                    if (typeof a === "object" && !a.preventDefault) {
                        for (i in a)o[b](i, e, a[i], l);
                        return this
                    }
                    if (b === "die" && !a && g && g.charAt(0) === ".")return o.unbind(g), this;
                    if (e === !1 || c.isFunction(e))f = e || x, e = k;
                    for (a = (a || "").split(" "); (g = a[h++]) != null;)if (i = ba.exec(g), j = "", i && (j = i[0], g = g.replace(ba, "")), g === "hover")a.push("mouseenter" + j, "mouseleave" + j); else if (i = g, la[g] ? (a.push(la[g] + j), g += j) : g = (la[g] || g) + j, b === "live") {
                        j = 0;
                        for (var m =
                                o.length; j < m; j++)c.event.add(o[j], "live." + U(g, l), {data:e, selector:l, handler:f, origType:g, origHandler:f, preType:i})
                    } else o.unbind("live." + U(g, l), f);
                    return this
                }
            });
    c.each("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error".split(" "), function (a, b) {
        c.fn[b] = function (a, c) {
            c == null && (c = a, a = null);
            return arguments.length > 0 ? this.bind(b, a, c) : this.trigger(b)
        };
        c.attrFn && (c.attrFn[b] =
                !0)
    });
    (function () {
        function a(a, b, c, d, e, f) {
            for (var e = 0, g = d.length; e < g; e++) {
                var h = d[e];
                if (h) {
                    for (var i = !1, h = h[a]; h;) {
                        if (h.sizcache === c) {
                            i = d[h.sizset];
                            break
                        }
                        if (h.nodeType === 1 && !f)h.sizcache = c, h.sizset = e;
                        if (h.nodeName.toLowerCase() === b) {
                            i = h;
                            break
                        }
                        h = h[a]
                    }
                    d[e] = i
                }
            }
        }

        function b(a, b, c, d, e, f) {
            for (var e = 0, g = d.length; e < g; e++) {
                var h = d[e];
                if (h) {
                    for (var i = !1, h = h[a]; h;) {
                        if (h.sizcache === c) {
                            i = d[h.sizset];
                            break
                        }
                        if (h.nodeType === 1) {
                            if (!f)h.sizcache = c, h.sizset = e;
                            if (typeof b !== "string") {
                                if (h === b) {
                                    i = !0;
                                    break
                                }
                            } else if (l.filter(b,
                                    [h]).length > 0) {
                                i = h;
                                break
                            }
                        }
                        h = h[a]
                    }
                    d[e] = i
                }
            }
        }

        var d = /((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^\[\]]*\]|['"][^'"]*['"]|[^\[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g, e = 0, f = Object.prototype.toString, g = !1, h = !0, i = /\\/g, j = /\W/;
        [0, 0].sort(function () {
            h = !1;
            return 0
        });
        var l = function (a, b, c, e) {
            var c = c || [], g = b = b || m;
            if (b.nodeType !== 1 && b.nodeType !== 9)return[];
            if (!a || typeof a !== "string")return c;
            var h, i, j, k, p, R = !0, A = l.isXML(b), r = [], t = a;
            do if (d.exec(""), h = d.exec(t))if (t = h[3], r.push(h[1]), h[2]) {
                k =
                        h[3];
                break
            } while (h);
            if (r.length > 1 && n.exec(a))if (r.length === 2 && o.relative[r[0]])i = v(r[0] + r[1], b); else for (i = o.relative[r[0]] ? [b] : l(r.shift(), b); r.length;)a = r.shift(), o.relative[a] && (a += r.shift()), i = v(a, i); else if (!e && r.length > 1 && b.nodeType === 9 && !A && o.match.ID.test(r[0]) && !o.match.ID.test(r[r.length - 1]) && (h = l.find(r.shift(), b, A), b = h.expr ? l.filter(h.expr, h.set)[0] : h.set[0]), b) {
                h = e ? {expr:r.pop(), set:s(e)} : l.find(r.pop(), r.length === 1 && (r[0] === "~" || r[0] === "+") && b.parentNode ? b.parentNode : b, A);
                i = h.expr ? l.filter(h.expr,
                        h.set) : h.set;
                for (r.length > 0 ? j = s(i) : R = !1; r.length;)h = p = r.pop(), o.relative[p] ? h = r.pop() : p = "", h == null && (h = b), o.relative[p](j, h, A)
            } else j = [];
            j || (j = i);
            j || l.error(p || a);
            if (f.call(j) === "[object Array]")if (R)if (b && b.nodeType === 1)for (a = 0; j[a] != null; a++)j[a] && (j[a] === !0 || j[a].nodeType === 1 && l.contains(b, j[a])) && c.push(i[a]); else for (a = 0; j[a] != null; a++)j[a] && j[a].nodeType === 1 && c.push(i[a]); else c.push.apply(c, j); else s(j, c);
            k && (l(k, g, c, e), l.uniqueSort(c));
            return c
        };
        l.uniqueSort = function (a) {
            if (t && (g = h, a.sort(t),
                    g))for (var b = 1; b < a.length; b++)a[b] === a[b - 1] && a.splice(b--, 1);
            return a
        };
        l.matches = function (a, b) {
            return l(a, null, null, b)
        };
        l.matchesSelector = function (a, b) {
            return l(b, null, null, [a]).length > 0
        };
        l.find = function (a, b, c) {
            var d;
            if (!a)return[];
            for (var e = 0, f = o.order.length; e < f; e++) {
                var g, h = o.order[e];
                if (g = o.leftMatch[h].exec(a)) {
                    var j = g[1];
                    g.splice(1, 1);
                    if (j.substr(j.length - 1) !== "\\" && (g[1] = (g[1] || "").replace(i, ""), d = o.find[h](g, b, c), d != null)) {
                        a = a.replace(o.match[h], "");
                        break
                    }
                }
            }
            d || (d = typeof b.getElementsByTagName !==
                    "undefined" ? b.getElementsByTagName("*") : []);
            return{set:d, expr:a}
        };
        l.filter = function (a, b, c, d) {
            for (var e, f, g = a, h = [], i = b, j = b && b[0] && l.isXML(b[0]); a && b.length;) {
                for (var m in o.filter)if ((e = o.leftMatch[m].exec(a)) != null && e[2]) {
                    var n, r, p = o.filter[m];
                    r = e[1];
                    f = !1;
                    e.splice(1, 1);
                    if (r.substr(r.length - 1) !== "\\") {
                        i === h && (h = []);
                        if (o.preFilter[m])if (e = o.preFilter[m](e, i, c, h, d, j)) {
                            if (e === !0)continue
                        } else f = n = !0;
                        if (e)for (var s = 0; (r = i[s]) != null; s++)if (r) {
                            n = p(r, e, s, i);
                            var A = d ^ !!n;
                            c && n != null ? A ? f = !0 : i[s] = !1 : A && (h.push(r),
                                    f = !0)
                        }
                        if (n !== k) {
                            c || (i = h);
                            a = a.replace(o.match[m], "");
                            if (!f)return[];
                            break
                        }
                    }
                }
                if (a === g)if (f == null)l.error(a); else break;
                g = a
            }
            return i
        };
        l.error = function (a) {
            throw"Syntax error, unrecognized expression: " + a;
        };
        var o = l.selectors = {order:["ID", "NAME", "TAG"], match:{ID:/#((?:[\w\u00c0-\uFFFF\-]|\\.)+)/, CLASS:/\.((?:[\w\u00c0-\uFFFF\-]|\\.)+)/, NAME:/\[name=['"]*((?:[\w\u00c0-\uFFFF\-]|\\.)+)['"]*\]/, ATTR:/\[\s*((?:[\w\u00c0-\uFFFF\-]|\\.)+)\s*(?:(\S?=)\s*(?:(['"])(.*?)\3|(#?(?:[\w\u00c0-\uFFFF\-]|\\.)*)|)|)\s*\]/,
            TAG:/^((?:[\w\u00c0-\uFFFF\*\-]|\\.)+)/, CHILD:/:(only|nth|last|first)-child(?:\(\s*(even|odd|(?:[+\-]?\d+|(?:[+\-]?\d*)?n\s*(?:[+\-]\s*\d+)?))\s*\))?/, POS:/:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^\-]|$)/, PSEUDO:/:((?:[\w\u00c0-\uFFFF\-]|\\.)+)(?:\((['"]?)((?:\([^\)]+\)|[^\(\)]*)+)\2\))?/}, leftMatch:{}, attrMap:{"class":"className", "for":"htmlFor"}, attrHandle:{href:function (a) {
            return a.getAttribute("href")
        }, type:function (a) {
            return a.getAttribute("type")
        }}, relative:{"+":function (a, b) {
            var c =
                    typeof b === "string", d = c && !j.test(b), c = c && !d;
            d && (b = b.toLowerCase());
            for (var d = 0, e = a.length, f; d < e; d++)if (f = a[d]) {
                for (; (f = f.previousSibling) && f.nodeType !== 1;);
                a[d] = c || f && f.nodeName.toLowerCase() === b ? f || !1 : f === b
            }
            c && l.filter(b, a, !0)
        }, ">":function (a, b) {
            var c, d = typeof b === "string", e = 0, f = a.length;
            if (d && !j.test(b))for (b = b.toLowerCase(); e < f; e++) {
                if (c = a[e])c = c.parentNode, a[e] = c.nodeName.toLowerCase() === b ? c : !1
            } else {
                for (; e < f; e++)(c = a[e]) && (a[e] = d ? c.parentNode : c.parentNode === b);
                d && l.filter(b, a, !0)
            }
        }, "":function (c, d, f) {
            var g, h = e++, i = b;
            typeof d === "string" && !j.test(d) && (g = d = d.toLowerCase(), i = a);
            i("parentNode", d, h, c, g, f)
        }, "~":function (c, d, f) {
            var g, h = e++, i = b;
            typeof d === "string" && !j.test(d) && (g = d = d.toLowerCase(), i = a);
            i("previousSibling", d, h, c, g, f)
        }}, find:{ID:function (a, b, c) {
            if (typeof b.getElementById !== "undefined" && !c)return(a = b.getElementById(a[1])) && a.parentNode ? [a] : []
        }, NAME:function (a, b) {
            if (typeof b.getElementsByName !== "undefined") {
                for (var c = [], d = b.getElementsByName(a[1]), e = 0, f = d.length; e < f; e++)d[e].getAttribute("name") ===
                        a[1] && c.push(d[e]);
                return c.length === 0 ? null : c
            }
        }, TAG:function (a, b) {
            if (typeof b.getElementsByTagName !== "undefined")return b.getElementsByTagName(a[1])
        }}, preFilter:{CLASS:function (a, b, c, d, e, f) {
            a = " " + a[1].replace(i, "") + " ";
            if (f)return a;
            for (var f = 0, g; (g = b[f]) != null; f++)g && (e ^ (g.className && (" " + g.className + " ").replace(/[\t\n\r]/g, " ").indexOf(a) >= 0) ? c || d.push(g) : c && (b[f] = !1));
            return!1
        }, ID:function (a) {
            return a[1].replace(i, "")
        }, TAG:function (a) {
            return a[1].replace(i, "").toLowerCase()
        }, CHILD:function (a) {
            if (a[1] ===
                    "nth") {
                a[2] || l.error(a[0]);
                a[2] = a[2].replace(/^\+|\s*/g, "");
                var b = /(-?)(\d*)(?:n([+\-]?\d*))?/.exec(a[2] === "even" && "2n" || a[2] === "odd" && "2n+1" || !/\D/.test(a[2]) && "0n+" + a[2] || a[2]);
                a[2] = b[1] + (b[2] || 1) - 0;
                a[3] = b[3] - 0
            } else a[2] && l.error(a[0]);
            a[0] = e++;
            return a
        }, ATTR:function (a, b, c, d, e, f) {
            b = a[1] = a[1].replace(i, "");
            !f && o.attrMap[b] && (a[1] = o.attrMap[b]);
            a[4] = (a[4] || a[5] || "").replace(i, "");
            a[2] === "~=" && (a[4] = " " + a[4] + " ");
            return a
        }, PSEUDO:function (a, b, c, e, f) {
            if (a[1] === "not")if ((d.exec(a[3]) || "").length > 1 ||
                    /^\w/.test(a[3]))a[3] = l(a[3], null, null, b); else return a = l.filter(a[3], b, c, 1 ^ f), c || e.push.apply(e, a), !1; else if (o.match.POS.test(a[0]) || o.match.CHILD.test(a[0]))return!0;
            return a
        }, POS:function (a) {
            a.unshift(!0);
            return a
        }}, filters:{enabled:function (a) {
            return a.disabled === !1 && a.type !== "hidden"
        }, disabled:function (a) {
            return a.disabled === !0
        }, checked:function (a) {
            return a.checked === !0
        }, selected:function (a) {
            return a.selected === !0
        }, parent:function (a) {
            return!!a.firstChild
        }, empty:function (a) {
            return!a.firstChild
        },
            has:function (a, b, c) {
                return!!l(c[3], a).length
            }, header:function (a) {
                return/h\d/i.test(a.nodeName)
            }, text:function (a) {
                var b = a.getAttribute("type"), c = a.type;
                return a.nodeName.toLowerCase() === "input" && "text" === c && (b === c || b === null)
            }, radio:function (a) {
                return a.nodeName.toLowerCase() === "input" && "radio" === a.type
            }, checkbox:function (a) {
                return a.nodeName.toLowerCase() === "input" && "checkbox" === a.type
            }, file:function (a) {
                return a.nodeName.toLowerCase() === "input" && "file" === a.type
            }, password:function (a) {
                return a.nodeName.toLowerCase() ===
                        "input" && "password" === a.type
            }, submit:function (a) {
                var b = a.nodeName.toLowerCase();
                return(b === "input" || b === "button") && "submit" === a.type
            }, image:function (a) {
                return a.nodeName.toLowerCase() === "input" && "image" === a.type
            }, reset:function (a) {
                var b = a.nodeName.toLowerCase();
                return(b === "input" || b === "button") && "reset" === a.type
            }, button:function (a) {
                var b = a.nodeName.toLowerCase();
                return b === "input" && "button" === a.type || b === "button"
            }, input:function (a) {
                return/input|select|textarea|button/i.test(a.nodeName)
            }, focus:function (a) {
                return a ===
                        a.ownerDocument.activeElement
            }}, setFilters:{first:function (a, b) {
            return b === 0
        }, last:function (a, b, c, d) {
            return b === d.length - 1
        }, even:function (a, b) {
            return b % 2 === 0
        }, odd:function (a, b) {
            return b % 2 === 1
        }, lt:function (a, b, c) {
            return b < c[3] - 0
        }, gt:function (a, b, c) {
            return b > c[3] - 0
        }, nth:function (a, b, c) {
            return c[3] - 0 === b
        }, eq:function (a, b, c) {
            return c[3] - 0 === b
        }}, filter:{PSEUDO:function (a, b, c, d) {
            var e = b[1], f = o.filters[e];
            if (f)return f(a, c, b, d); else if (e === "contains")return(a.textContent || a.innerText || l.getText([a]) || "").indexOf(b[3]) >=
                    0; else if (e === "not") {
                b = b[3];
                c = 0;
                for (d = b.length; c < d; c++)if (b[c] === a)return!1;
                return!0
            } else l.error(e)
        }, CHILD:function (a, b) {
            var c = b[1], d = a;
            switch (c) {
                case "only":
                case "first":
                    for (; d = d.previousSibling;)if (d.nodeType === 1)return!1;
                    if (c === "first")return!0;
                    d = a;
                case "last":
                    for (; d = d.nextSibling;)if (d.nodeType === 1)return!1;
                    return!0;
                case "nth":
                    var c = b[2], e = b[3];
                    if (c === 1 && e === 0)return!0;
                    var f = b[0], g = a.parentNode;
                    if (g && (g.sizcache !== f || !a.nodeIndex)) {
                        for (var h = 0, d = g.firstChild; d; d = d.nextSibling)if (d.nodeType ===
                                1)d.nodeIndex = ++h;
                        g.sizcache = f
                    }
                    d = a.nodeIndex - e;
                    return c === 0 ? d === 0 : d % c === 0 && d / c >= 0
            }
        }, ID:function (a, b) {
            return a.nodeType === 1 && a.getAttribute("id") === b
        }, TAG:function (a, b) {
            return b === "*" && a.nodeType === 1 || a.nodeName.toLowerCase() === b
        }, CLASS:function (a, b) {
            return(" " + (a.className || a.getAttribute("class")) + " ").indexOf(b) > -1
        }, ATTR:function (a, b) {
            var c = b[1], c = o.attrHandle[c] ? o.attrHandle[c](a) : a[c] != null ? a[c] : a.getAttribute(c), d = c + "", e = b[2], f = b[4];
            return c == null ? e === "!=" : e === "=" ? d === f : e === "*=" ? d.indexOf(f) >=
                    0 : e === "~=" ? (" " + d + " ").indexOf(f) >= 0 : !f ? d && c !== !1 : e === "!=" ? d !== f : e === "^=" ? d.indexOf(f) === 0 : e === "$=" ? d.substr(d.length - f.length) === f : e === "|=" ? d === f || d.substr(0, f.length + 1) === f + "-" : !1
        }, POS:function (a, b, c, d) {
            var e = o.setFilters[b[2]];
            if (e)return e(a, c, b, d)
        }}}, n = o.match.POS, A = function (a, b) {
            return"\\" + (b - 0 + 1)
        }, p;
        for (p in o.match)o.match[p] = RegExp(o.match[p].source + /(?![^\[]*\])(?![^\(]*\))/.source), o.leftMatch[p] = RegExp(/(^(?:.|\r|\n)*?)/.source + o.match[p].source.replace(/\\(\d+)/g, A));
        var s = function (a, b) {
            a = Array.prototype.slice.call(a, 0);
            return b ? (b.push.apply(b, a), b) : a
        };
        try {
            Array.prototype.slice.call(m.documentElement.childNodes, 0)
        } catch (w) {
            s = function (a, b) {
                var c = 0, d = b || [];
                if (f.call(a) === "[object Array]")Array.prototype.push.apply(d, a); else if (typeof a.length === "number")for (var e = a.length; c < e; c++)d.push(a[c]); else for (; a[c]; c++)d.push(a[c]);
                return d
            }
        }
        var t, z;
        m.documentElement.compareDocumentPosition ? t = function (a, b) {
            return a === b ? (g = !0, 0) : !a.compareDocumentPosition || !b.compareDocumentPosition ? a.compareDocumentPosition ?
                    -1 : 1 : a.compareDocumentPosition(b) & 4 ? -1 : 1
        } : (t = function (a, b) {
            if (a === b)return g = !0, 0; else if (a.sourceIndex && b.sourceIndex)return a.sourceIndex - b.sourceIndex;
            var c, d, e = [], f = [];
            c = a.parentNode;
            d = b.parentNode;
            var h = c;
            if (c === d)return z(a, b); else if (c) {
                if (!d)return 1
            } else return-1;
            for (; h;)e.unshift(h), h = h.parentNode;
            for (h = d; h;)f.unshift(h), h = h.parentNode;
            c = e.length;
            d = f.length;
            for (h = 0; h < c && h < d; h++)if (e[h] !== f[h])return z(e[h], f[h]);
            return h === c ? z(a, f[h], -1) : z(e[h], b, 1)
        }, z = function (a, b, c) {
            if (a === b)return c;
            for (a = a.nextSibling; a;) {
                if (a === b)return-1;
                a = a.nextSibling
            }
            return 1
        });
        l.getText = function (a) {
            for (var b = "", c, d = 0; a[d]; d++)c = a[d], c.nodeType === 3 || c.nodeType === 4 ? b += c.nodeValue : c.nodeType !== 8 && (b += l.getText(c.childNodes));
            return b
        };
        (function () {
            var a = m.createElement("div"), b = "script" + (new Date).getTime(), c = m.documentElement;
            a.innerHTML = "<a name='" + b + "'/>";
            c.insertBefore(a, c.firstChild);
            if (m.getElementById(b))o.find.ID = function (a, b, c) {
                if (typeof b.getElementById !== "undefined" && !c)return(b = b.getElementById(a[1])) ?
                        b.id === a[1] || typeof b.getAttributeNode !== "undefined" && b.getAttributeNode("id").nodeValue === a[1] ? [b] : k : []
            }, o.filter.ID = function (a, b) {
                var c = typeof a.getAttributeNode !== "undefined" && a.getAttributeNode("id");
                return a.nodeType === 1 && c && c.nodeValue === b
            };
            c.removeChild(a);
            c = a = null
        })();
        (function () {
            var a = m.createElement("div");
            a.appendChild(m.createComment(""));
            if (a.getElementsByTagName("*").length > 0)o.find.TAG = function (a, b) {
                var c = b.getElementsByTagName(a[1]);
                if (a[1] === "*") {
                    for (var d = [], e = 0; c[e]; e++)c[e].nodeType ===
                            1 && d.push(c[e]);
                    c = d
                }
                return c
            };
            a.innerHTML = "<a href='#'></a>";
            if (a.firstChild && typeof a.firstChild.getAttribute !== "undefined" && a.firstChild.getAttribute("href") !== "#")o.attrHandle.href = function (a) {
                return a.getAttribute("href", 2)
            };
            a = null
        })();
        m.querySelectorAll && function () {
            var a = l, b = m.createElement("div");
            b.innerHTML = "<p class='TEST'></p>";
            if (!(b.querySelectorAll && b.querySelectorAll(".TEST").length === 0)) {
                l = function (b, c, d, e) {
                    c = c || m;
                    if (!e && !l.isXML(c)) {
                        var f = /^(\w+$)|^\.([\w\-]+$)|^#([\w\-]+$)/.exec(b);
                        if (f && (c.nodeType === 1 || c.nodeType === 9))if (f[1])return s(c.getElementsByTagName(b), d); else if (f[2] && o.find.CLASS && c.getElementsByClassName)return s(c.getElementsByClassName(f[2]), d);
                        if (c.nodeType === 9) {
                            if (b === "body" && c.body)return s([c.body], d); else if (f && f[3]) {
                                var g = c.getElementById(f[3]);
                                if (g && g.parentNode) {
                                    if (g.id === f[3])return s([g], d)
                                } else return s([], d)
                            }
                            try {
                                return s(c.querySelectorAll(b), d)
                            } catch (h) {
                            }
                        } else if (c.nodeType === 1 && c.nodeName.toLowerCase() !== "object") {
                            var f = c, i = (g = c.getAttribute("id")) ||
                                    "__sizzle__", j = c.parentNode, k = /^\s*[+~]/.test(b);
                            g ? i = i.replace(/'/g, "\\$&") : c.setAttribute("id", i);
                            if (k && j)c = c.parentNode;
                            try {
                                if (!k || j)return s(c.querySelectorAll("[id='" + i + "'] " + b), d)
                            } catch (n) {
                            } finally {
                                g || f.removeAttribute("id")
                            }
                        }
                    }
                    return a(b, c, d, e)
                };
                for (var c in a)l[c] = a[c];
                b = null
            }
        }();
        (function () {
            var a = m.documentElement, b = a.matchesSelector || a.mozMatchesSelector || a.webkitMatchesSelector || a.msMatchesSelector;
            if (b) {
                var c = !b.call(m.createElement("div"), "div"), d = !1;
                try {
                    b.call(m.documentElement, "[test!='']:sizzle")
                } catch (e) {
                    d =
                            !0
                }
                l.matchesSelector = function (a, e) {
                    e = e.replace(/\=\s*([^'"\]]*)\s*\]/g, "='$1']");
                    if (!l.isXML(a))try {
                        if (d || !o.match.PSEUDO.test(e) && !/!=/.test(e)) {
                            var f = b.call(a, e);
                            if (f || !c || a.document && a.document.nodeType !== 11)return f
                        }
                    } catch (g) {
                    }
                    return l(e, null, null, [a]).length > 0
                }
            }
        })();
        (function () {
            var a = m.createElement("div");
            a.innerHTML = "<div class='test e'></div><div class='test'></div>";
            if (a.getElementsByClassName && a.getElementsByClassName("e").length !== 0 && (a.lastChild.className = "e", a.getElementsByClassName("e").length !==
                    1))o.order.splice(1, 0, "CLASS"), o.find.CLASS = function (a, b, c) {
                if (typeof b.getElementsByClassName !== "undefined" && !c)return b.getElementsByClassName(a[1])
            }, a = null
        })();
        l.contains = m.documentElement.contains ? function (a, b) {
            return a !== b && (a.contains ? a.contains(b) : !0)
        } : m.documentElement.compareDocumentPosition ? function (a, b) {
            return!!(a.compareDocumentPosition(b) & 16)
        } : function () {
            return!1
        };
        l.isXML = function (a) {
            return(a = (a ? a.ownerDocument || a : 0).documentElement) ? a.nodeName !== "HTML" : !1
        };
        var v = function (a, b) {
            for (var c,
                         d = [], e = "", f = b.nodeType ? [b] : b; c = o.match.PSEUDO.exec(a);)e += c[0], a = a.replace(o.match.PSEUDO, "");
            a = o.relative[a] ? a + "*" : a;
            c = 0;
            for (var g = f.length; c < g; c++)l(a, f[c], d);
            return l.filter(e, d)
        };
        c.find = l;
        c.expr = l.selectors;
        c.expr[":"] = c.expr.filters;
        c.unique = l.uniqueSort;
        c.text = l.getText;
        c.isXMLDoc = l.isXML;
        c.contains = l.contains
    })();
    var xb = /Until$/, yb = /^(?:parents|prevUntil|prevAll)/, zb = /,/, gb = /^.[^:#\[\.,]*$/, Ab = Array.prototype.slice, Na = c.expr.match.POS, Bb = {children:!0, contents:!0, next:!0, prev:!0};
    c.fn.extend({find:function (a) {
        var b =
                this, d, e;
        if (typeof a !== "string")return c(a).filter(function () {
            for (d = 0, e = b.length; d < e; d++)if (c.contains(b[d], this))return!0
        });
        var f = this.pushStack("", "find", a), g, h, i;
        for (d = 0, e = this.length; d < e; d++)if (g = f.length, c.find(a, this[d], f), d > 0)for (h = g; h < f.length; h++)for (i = 0; i < g; i++)if (f[i] === f[h]) {
            f.splice(h--, 1);
            break
        }
        return f
    }, has:function (a) {
        var b = c(a);
        return this.filter(function () {
            for (var a = 0, e = b.length; a < e; a++)if (c.contains(this, b[a]))return!0
        })
    }, not:function (a) {
        return this.pushStack(sa(this, a, !1), "not",
                a)
    }, filter:function (a) {
        return this.pushStack(sa(this, a, !0), "filter", a)
    }, is:function (a) {
        return!!a && (typeof a === "string" ? c.filter(a, this).length > 0 : this.filter(a).length > 0)
    }, closest:function (a, b) {
        var d = [], e, f, g = this[0];
        if (c.isArray(a)) {
            var h, i = {}, j = 1;
            if (g && a.length) {
                for (e = 0, f = a.length; e < f; e++)h = a[e], i[h] || (i[h] = Na.test(h) ? c(h, b || this.context) : h);
                for (; g && g.ownerDocument && g !== b;) {
                    for (h in i)e = i[h], (e.jquery ? e.index(g) > -1 : c(g).is(e)) && d.push({selector:h, elem:g, level:j});
                    g = g.parentNode;
                    j++
                }
            }
            return d
        }
        h = Na.test(a) ||
                typeof a !== "string" ? c(a, b || this.context) : 0;
        for (e = 0, f = this.length; e < f; e++)for (g = this[e]; g;)if (h ? h.index(g) > -1 : c.find.matchesSelector(g, a)) {
            d.push(g);
            break
        } else if (g = g.parentNode, !g || !g.ownerDocument || g === b || g.nodeType === 11)break;
        d = d.length > 1 ? c.unique(d) : d;
        return this.pushStack(d, "closest", a)
    }, index:function (a) {
        return!a || typeof a === "string" ? c.inArray(this[0], a ? c(a) : this.parent().children()) : c.inArray(a.jquery ? a[0] : a, this)
    }, add:function (a, b) {
        var d = typeof a === "string" ? c(a, b) : c.makeArray(a && a.nodeType ?
                [a] : a), e = c.merge(this.get(), d);
        return this.pushStack(!d[0] || !d[0].parentNode || d[0].parentNode.nodeType === 11 || !e[0] || !e[0].parentNode || e[0].parentNode.nodeType === 11 ? e : c.unique(e))
    }, andSelf:function () {
        return this.add(this.prevObject)
    }});
    c.each({parent:function (a) {
        return(a = a.parentNode) && a.nodeType !== 11 ? a : null
    }, parents:function (a) {
        return c.dir(a, "parentNode")
    }, parentsUntil:function (a, b, d) {
        return c.dir(a, "parentNode", d)
    }, next:function (a) {
        return c.nth(a, 2, "nextSibling")
    }, prev:function (a) {
        return c.nth(a,
                2, "previousSibling")
    }, nextAll:function (a) {
        return c.dir(a, "nextSibling")
    }, prevAll:function (a) {
        return c.dir(a, "previousSibling")
    }, nextUntil:function (a, b, d) {
        return c.dir(a, "nextSibling", d)
    }, prevUntil:function (a, b, d) {
        return c.dir(a, "previousSibling", d)
    }, siblings:function (a) {
        return c.sibling(a.parentNode.firstChild, a)
    }, children:function (a) {
        return c.sibling(a.firstChild)
    }, contents:function (a) {
        return c.nodeName(a, "iframe") ? a.contentDocument || a.contentWindow.document : c.makeArray(a.childNodes)
    }}, function (a, b) {
        c.fn[a] = function (d, e) {
            var f = c.map(this, b, d), g = Ab.call(arguments);
            xb.test(a) || (e = d);
            e && typeof e === "string" && (f = c.filter(e, f));
            f = this.length > 1 && !Bb[a] ? c.unique(f) : f;
            if ((this.length > 1 || zb.test(e)) && yb.test(a))f = f.reverse();
            return this.pushStack(f, a, g.join(","))
        }
    });
    c.extend({filter:function (a, b, d) {
        d && (a = ":not(" + a + ")");
        return b.length === 1 ? c.find.matchesSelector(b[0], a) ? [b[0]] : [] : c.find.matches(a, b)
    }, dir:function (a, b, d) {
        for (var e = [], a = a[b]; a && a.nodeType !== 9 && (d === k || a.nodeType !== 1 || !c(a).is(d));)a.nodeType ===
                1 && e.push(a), a = a[b];
        return e
    }, nth:function (a, b, c) {
        for (var b = b || 1, e = 0; a; a = a[c])if (a.nodeType === 1 && ++e === b)break;
        return a
    }, sibling:function (a, b) {
        for (var c = []; a; a = a.nextSibling)a.nodeType === 1 && a !== b && c.push(a);
        return c
    }});
    var Cb = / jQuery\d+="(?:\d+|null)"/g, ma = /^\s+/, Oa = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/ig, Pa = /<([\w:]+)/, Db = /<tbody/i, Eb = /<|&#?\w+;/, Qa = /<(?:script|object|embed|option|style)/i, Ra = /checked\s*(?:[^=]|=\s*.checked.)/i, Fb = /\/(java|ecma)script/i, ib = /^\s*<!(?:\[CDATA\[|\-\-)/,
            w = {option:[1, "<select multiple='multiple'>", "</select>"], legend:[1, "<fieldset>", "</fieldset>"], thead:[1, "<table>", "</table>"], tr:[2, "<table><tbody>", "</tbody></table>"], td:[3, "<table><tbody><tr>", "</tr></tbody></table>"], col:[2, "<table><tbody></tbody><colgroup>", "</colgroup></table>"], area:[1, "<map>", "</map>"], _default:[0, "", ""]};
    w.optgroup = w.option;
    w.tbody = w.tfoot = w.colgroup = w.caption = w.thead;
    w.th = w.td;
    if (!c.support.htmlSerialize)w._default = [1, "div<div>", "</div>"];
    c.fn.extend({text:function (a) {
        return c.isFunction(a) ?
                this.each(function (b) {
                    var d = c(this);
                    d.text(a.call(this, b, d.text()))
                }) : typeof a !== "object" && a !== k ? this.empty().append((this[0] && this[0].ownerDocument || m).createTextNode(a)) : c.text(this)
    }, wrapAll:function (a) {
        if (c.isFunction(a))return this.each(function (b) {
            c(this).wrapAll(a.call(this, b))
        });
        if (this[0]) {
            var b = c(a, this[0].ownerDocument).eq(0).clone(!0);
            this[0].parentNode && b.insertBefore(this[0]);
            b.map(
                    function () {
                        for (var a = this; a.firstChild && a.firstChild.nodeType === 1;)a = a.firstChild;
                        return a
                    }).append(this)
        }
        return this
    },
        wrapInner:function (a) {
            return c.isFunction(a) ? this.each(function (b) {
                c(this).wrapInner(a.call(this, b))
            }) : this.each(function () {
                var b = c(this), d = b.contents();
                d.length ? d.wrapAll(a) : b.append(a)
            })
        }, wrap:function (a) {
            return this.each(function () {
                c(this).wrapAll(a)
            })
        }, unwrap:function () {
            return this.parent().each(
                    function () {
                        c.nodeName(this, "body") || c(this).replaceWith(this.childNodes)
                    }).end()
        }, append:function () {
            return this.domManip(arguments, !0, function (a) {
                this.nodeType === 1 && this.appendChild(a)
            })
        }, prepend:function () {
            return this.domManip(arguments,
                    !0, function (a) {
                        this.nodeType === 1 && this.insertBefore(a, this.firstChild)
                    })
        }, before:function () {
            if (this[0] && this[0].parentNode)return this.domManip(arguments, !1, function (a) {
                this.parentNode.insertBefore(a, this)
            }); else if (arguments.length) {
                var a = c(arguments[0]);
                a.push.apply(a, this.toArray());
                return this.pushStack(a, "before", arguments)
            }
        }, after:function () {
            if (this[0] && this[0].parentNode)return this.domManip(arguments, !1, function (a) {
                this.parentNode.insertBefore(a, this.nextSibling)
            }); else if (arguments.length) {
                var a =
                        this.pushStack(this, "after", arguments);
                a.push.apply(a, c(arguments[0]).toArray());
                return a
            }
        }, remove:function (a, b) {
            for (var d = 0, e; (e = this[d]) != null; d++)if (!a || c.filter(a, [e]).length)!b && e.nodeType === 1 && (c.cleanData(e.getElementsByTagName("*")), c.cleanData([e])), e.parentNode && e.parentNode.removeChild(e);
            return this
        }, empty:function () {
            for (var a = 0, b; (b = this[a]) != null; a++)for (b.nodeType === 1 && c.cleanData(b.getElementsByTagName("*")); b.firstChild;)b.removeChild(b.firstChild);
            return this
        }, clone:function (a, b) {
            a =
                    a == null ? !1 : a;
            b = b == null ? a : b;
            return this.map(function () {
                return c.clone(this, a, b)
            })
        }, html:function (a) {
            if (a === k)return this[0] && this[0].nodeType === 1 ? this[0].innerHTML.replace(Cb, "") : null; else if (typeof a === "string" && !Qa.test(a) && (c.support.leadingWhitespace || !ma.test(a)) && !w[(Pa.exec(a) || ["", ""])[1].toLowerCase()]) {
                a = a.replace(Oa, "<$1></$2>");
                try {
                    for (var b = 0, d = this.length; b < d; b++)if (this[b].nodeType === 1)c.cleanData(this[b].getElementsByTagName("*")), this[b].innerHTML = a
                } catch (e) {
                    this.empty().append(a)
                }
            } else c.isFunction(a) ?
                    this.each(function (b) {
                        var d = c(this);
                        d.html(a.call(this, b, d.html()))
                    }) : this.empty().append(a);
            return this
        }, replaceWith:function (a) {
            if (this[0] && this[0].parentNode) {
                if (c.isFunction(a))return this.each(function (b) {
                    var d = c(this), e = d.html();
                    d.replaceWith(a.call(this, b, e))
                });
                typeof a !== "string" && (a = c(a).detach());
                return this.each(function () {
                    var b = this.nextSibling, d = this.parentNode;
                    c(this).remove();
                    b ? c(b).before(a) : c(d).append(a)
                })
            } else return this.length ? this.pushStack(c(c.isFunction(a) ? a() : a), "replaceWith",
                    a) : this
        }, detach:function (a) {
            return this.remove(a, !0)
        }, domManip:function (a, b, d) {
            var e, f, g, h = a[0], i = [];
            if (!c.support.checkClone && arguments.length === 3 && typeof h === "string" && Ra.test(h))return this.each(function () {
                c(this).domManip(a, b, d, !0)
            });
            if (c.isFunction(h))return this.each(function (e) {
                var f = c(this);
                a[0] = h.call(this, e, b ? f.html() : k);
                f.domManip(a, b, d)
            });
            if (this[0]) {
                e = h && h.parentNode;
                e = c.support.parentNode && e && e.nodeType === 11 && e.childNodes.length === this.length ? {fragment:e} : c.buildFragment(a, this, i);
                g =
                        e.fragment;
                if (f = g.childNodes.length === 1 ? g = g.firstChild : g.firstChild) {
                    b = b && c.nodeName(f, "tr");
                    f = 0;
                    for (var j = this.length, l = j - 1; f < j; f++)d.call(b ? c.nodeName(this[f], "table") ? this[f].getElementsByTagName("tbody")[0] || this[f].appendChild(this[f].ownerDocument.createElement("tbody")) : this[f] : this[f], e.cacheable || j > 1 && f < l ? c.clone(g, !0, !0) : g)
                }
                i.length && c.each(i, hb)
            }
            return this
        }});
    c.buildFragment = function (a, b, d) {
        var e, f, g, h;
        b && b[0] && (h = b[0].ownerDocument || b[0]);
        h.createDocumentFragment || (h = m);
        if (a.length ===
                1 && typeof a[0] === "string" && a[0].length < 512 && h === m && a[0].charAt(0) === "<" && !Qa.test(a[0]) && (c.support.checkClone || !Ra.test(a[0])))f = !0, (g = c.fragments[a[0]]) && g !== 1 && (e = g);
        e || (e = h.createDocumentFragment(), c.clean(a, h, e, d));
        f && (c.fragments[a[0]] = g ? e : 1);
        return{fragment:e, cacheable:f}
    };
    c.fragments = {};
    c.each({appendTo:"append", prependTo:"prepend", insertBefore:"before", insertAfter:"after", replaceAll:"replaceWith"}, function (a, b) {
        c.fn[a] = function (d) {
            var e = [], d = c(d), f = this.length === 1 && this[0].parentNode;
            if (f &&
                    f.nodeType === 11 && f.childNodes.length === 1 && d.length === 1)return d[b](this[0]), this; else {
                for (var f = 0, g = d.length; f < g; f++) {
                    var h = (f > 0 ? this.clone(!0) : this).get();
                    c(d[f])[b](h);
                    e = e.concat(h)
                }
                return this.pushStack(e, a, d.selector)
            }
        }
    });
    c.extend({clone:function (a, b, d) {
        var e = a.cloneNode(!0), f, g, h;
        if ((!c.support.noCloneEvent || !c.support.noCloneChecked) && (a.nodeType === 1 || a.nodeType === 11) && !c.isXMLDoc(a)) {
            ua(a, e);
            f = V(a);
            g = V(e);
            for (h = 0; f[h]; ++h)ua(f[h], g[h])
        }
        if (b && (ta(a, e), d)) {
            f = V(a);
            g = V(e);
            for (h = 0; f[h]; ++h)ta(f[h],
                    g[h])
        }
        return e
    }, clean:function (a, b, d, e) {
        b = b || m;
        typeof b.createElement === "undefined" && (b = b.ownerDocument || b[0] && b[0].ownerDocument || m);
        for (var f = [], g, h = 0, i; (i = a[h]) != null; h++)if (typeof i === "number" && (i += ""), i) {
            if (typeof i === "string")if (Eb.test(i)) {
                i = i.replace(Oa, "<$1></$2>");
                g = (Pa.exec(i) || ["", ""])[1].toLowerCase();
                var j = w[g] || w._default, l = j[0], k = b.createElement("div");
                for (k.innerHTML = j[1] + i + j[2]; l--;)k = k.lastChild;
                if (!c.support.tbody) {
                    l = Db.test(i);
                    j = g === "table" && !l ? k.firstChild && k.firstChild.childNodes :
                            j[1] === "<table>" && !l ? k.childNodes : [];
                    for (g = j.length - 1; g >= 0; --g)c.nodeName(j[g], "tbody") && !j[g].childNodes.length && j[g].parentNode.removeChild(j[g])
                }
                !c.support.leadingWhitespace && ma.test(i) && k.insertBefore(b.createTextNode(ma.exec(i)[0]), k.firstChild);
                i = k.childNodes
            } else i = b.createTextNode(i);
            var n;
            if (!c.support.appendChecked)if (i[0] && typeof(n = i.length) === "number")for (g = 0; g < n; g++)wa(i[g]); else wa(i);
            i.nodeType ? f.push(i) : f = c.merge(f, i)
        }
        if (d) {
            a = function (a) {
                return!a.type || Fb.test(a.type)
            };
            for (h = 0; f[h]; h++)e &&
                    c.nodeName(f[h], "script") && (!f[h].type || f[h].type.toLowerCase() === "text/javascript") ? e.push(f[h].parentNode ? f[h].parentNode.removeChild(f[h]) : f[h]) : (f[h].nodeType === 1 && (b = c.grep(f[h].getElementsByTagName("script"), a), f.splice.apply(f, [h + 1, 0].concat(b))), d.appendChild(f[h]))
        }
        return f
    }, cleanData:function (a) {
        for (var b, d, e = c.cache, f = c.expando, g = c.event.special, h = c.support.deleteExpando, i = 0, j; (j = a[i]) != null; i++)if (!j.nodeName || !c.noData[j.nodeName.toLowerCase()])if (d = j[c.expando]) {
            if ((b = e[d] && e[d][f]) &&
                    b.events) {
                for (var l in b.events)g[l] ? c.event.remove(j, l) : c.removeEvent(j, l, b.handle);
                if (b.handle)b.handle.elem = null
            }
            h ? delete j[c.expando] : j.removeAttribute && j.removeAttribute(c.expando);
            delete e[d]
        }
    }});
    var Sa = /alpha\([^)]*\)/i, Gb = /opacity=([^)]*)/, Hb = /([A-Z]|^ms)/g, Ta = /^-?\d+(?:px)?$/i, Ib = /^-?\d/, Jb = /^[+\-]=/, Kb = /[^+\-\.\de]+/g, Lb = {position:"absolute", visibility:"hidden", display:"block"}, jb = ["Left", "Right"], kb = ["Top", "Bottom"], K, Ua, Va;
    c.fn.css = function (a, b) {
        return arguments.length === 2 && b === k ? this :
                c.access(this, a, b, !0, function (a, b, f) {
                    return f !== k ? c.style(a, b, f) : c.css(a, b)
                })
    };
    c.extend({cssHooks:{opacity:{get:function (a, b) {
        if (b) {
            var c = K(a, "opacity", "opacity");
            return c === "" ? "1" : c
        } else return a.style.opacity
    }}}, cssNumber:{fillOpacity:!0, fontWeight:!0, lineHeight:!0, opacity:!0, orphans:!0, widows:!0, zIndex:!0, zoom:!0}, cssProps:{"float":c.support.cssFloat ? "cssFloat" : "styleFloat"}, style:function (a, b, d, e) {
        if (a && !(a.nodeType === 3 || a.nodeType === 8 || !a.style)) {
            var f, g = c.camelCase(b), h = a.style, i = c.cssHooks[g],
                    b = c.cssProps[g] || g;
            if (d !== k) {
                if (e = typeof d, !(e === "number" && isNaN(d) || d == null))if (e === "string" && Jb.test(d) && (d = +d.replace(Kb, "") + parseFloat(c.css(a, b)), e = "number"), e === "number" && !c.cssNumber[g] && (d += "px"), !i || !("set"in i) || (d = i.set(a, d)) !== k)try {
                    h[b] = d
                } catch (j) {
                }
            } else return i && "get"in i && (f = i.get(a, !1, e)) !== k ? f : h[b]
        }
    }, css:function (a, b, d) {
        var e, f, b = c.camelCase(b);
        f = c.cssHooks[b];
        b = c.cssProps[b] || b;
        b === "cssFloat" && (b = "float");
        if (f && "get"in f && (e = f.get(a, !0, d)) !== k)return e; else if (K)return K(a, b)
    },
        swap:function (a, b, c) {
            var e = {}, f;
            for (f in b)e[f] = a.style[f], a.style[f] = b[f];
            c.call(a);
            for (f in b)a.style[f] = e[f]
        }});
    c.curCSS = c.css;
    c.each(["height", "width"], function (a, b) {
        c.cssHooks[b] = {get:function (a, e, f) {
            var g;
            if (e) {
                if (a.offsetWidth !== 0)return xa(a, b, f); else c.swap(a, Lb, function () {
                    g = xa(a, b, f)
                });
                return g
            }
        }, set:function (a, b) {
            if (Ta.test(b)) {
                if (b = parseFloat(b), b >= 0)return b + "px"
            } else return b
        }}
    });
    if (!c.support.opacity)c.cssHooks.opacity = {get:function (a, b) {
        return Gb.test((b && a.currentStyle ? a.currentStyle.filter :
                a.style.filter) || "") ? parseFloat(RegExp.$1) / 100 + "" : b ? "1" : ""
    }, set:function (a, b) {
        var d = a.style, e = a.currentStyle;
        d.zoom = 1;
        var f = c.isNaN(b) ? "" : "alpha(opacity=" + b * 100 + ")", e = e && e.filter || d.filter || "";
        d.filter = Sa.test(e) ? e.replace(Sa, f) : e + " " + f
    }};
    c(function () {
        if (!c.support.reliableMarginRight)c.cssHooks.marginRight = {get:function (a, b) {
            var d;
            c.swap(a, {display:"inline-block"}, function () {
                d = b ? K(a, "margin-right", "marginRight") : a.style.marginRight
            });
            return d
        }}
    });
    m.defaultView && m.defaultView.getComputedStyle && (Ua =
            function (a, b) {
                var d, e, b = b.replace(Hb, "-$1").toLowerCase();
                if (!(e = a.ownerDocument.defaultView))return k;
                if (e = e.getComputedStyle(a, null))d = e.getPropertyValue(b), d === "" && !c.contains(a.ownerDocument.documentElement, a) && (d = c.style(a, b));
                return d
            });
    m.documentElement.currentStyle && (Va = function (a, b) {
        var c, e = a.currentStyle && a.currentStyle[b], f = a.runtimeStyle && a.runtimeStyle[b], g = a.style;
        if (!Ta.test(e) && Ib.test(e)) {
            c = g.left;
            if (f)a.runtimeStyle.left = a.currentStyle.left;
            g.left = b === "fontSize" ? "1em" : e || 0;
            e = g.pixelLeft +
                    "px";
            g.left = c;
            if (f)a.runtimeStyle.left = f
        }
        return e === "" ? "auto" : e
    });
    K = Ua || Va;
    if (c.expr && c.expr.filters)c.expr.filters.hidden = function (a) {
        var b = a.offsetHeight;
        return a.offsetWidth === 0 && b === 0 || !c.support.reliableHiddenOffsets && (a.style.display || c.css(a, "display")) === "none"
    }, c.expr.filters.visible = function (a) {
        return!c.expr.filters.hidden(a)
    };
    var Mb = /%20/g, lb = /\[\]$/, Wa = /\r?\n/g, Nb = /#.*$/, Ob = /^(.*?):[ \t]*([^\r\n]*)\r?$/mg, Pb = /^(?:color|date|datetime|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i,
            Qb = /^(?:GET|HEAD)$/, Rb = /^\/\//, Xa = /\?/, Sb = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, Tb = /^(?:select|textarea)/i, za = /\s+/, Ub = /([?&])_=[^&]*/, Ya = /^([\w\+\.\-]+:)(?:\/\/([^\/?#:]*)(?::(\d+))?)?/, Za = c.fn.load, ca = {}, $a = {}, D, G;
    try {
        D = ob.href
    } catch ($b) {
        D = m.createElement("a"), D.href = "", D = D.href
    }
    G = Ya.exec(D.toLowerCase()) || [];
    c.fn.extend({load:function (a, b, d) {
        if (typeof a !== "string" && Za)return Za.apply(this, arguments); else if (!this.length)return this;
        var e = a.indexOf(" ");
        if (e >= 0)var f = a.slice(e, a.length),
                a = a.slice(0, e);
        e = "GET";
        b && (c.isFunction(b) ? (d = b, b = k) : typeof b === "object" && (b = c.param(b, c.ajaxSettings.traditional), e = "POST"));
        var g = this;
        c.ajax({url:a, type:e, dataType:"html", data:b, complete:function (a, b, e) {
            e = a.responseText;
            a.isResolved() && (a.done(function (a) {
                e = a
            }), g.html(f ? c("<div>").append(e.replace(Sb, "")).find(f) : e));
            d && g.each(d, [e, b, a])
        }});
        return this
    }, serialize:function () {
        return c.param(this.serializeArray())
    }, serializeArray:function () {
        return this.map(
                function () {
                    return this.elements ? c.makeArray(this.elements) :
                            this
                }).filter(
                function () {
                    return this.name && !this.disabled && (this.checked || Tb.test(this.nodeName) || Pb.test(this.type))
                }).map(
                function (a, b) {
                    var d = c(this).val();
                    return d == null ? null : c.isArray(d) ? c.map(d, function (a) {
                        return{name:b.name, value:a.replace(Wa, "\r\n")}
                    }) : {name:b.name, value:d.replace(Wa, "\r\n")}
                }).get()
    }});
    c.each("ajaxStart ajaxStop ajaxComplete ajaxError ajaxSuccess ajaxSend".split(" "), function (a, b) {
        c.fn[b] = function (a) {
            return this.bind(b, a)
        }
    });
    c.each(["get", "post"], function (a, b) {
        c[b] = function (a, e, f, g) {
            c.isFunction(e) && (g = g || f, f = e, e = k);
            return c.ajax({type:b, url:a, data:e, success:f, dataType:g})
        }
    });
    c.extend({getScript:function (a, b) {
        return c.get(a, k, b, "script")
    }, getJSON:function (a, b, d) {
        return c.get(a, b, d, "json")
    }, ajaxSetup:function (a, b) {
        b ? c.extend(!0, a, c.ajaxSettings, b) : (b = a, a = c.extend(!0, c.ajaxSettings, b));
        for (var d in{context:1, url:1})d in b ? a[d] = b[d] : d in c.ajaxSettings && (a[d] = c.ajaxSettings[d]);
        return a
    }, ajaxSettings:{url:D, isLocal:/^(?:about|app|app\-storage|.+\-extension|file|widget):$/.test(G[1]),
        global:!0, type:"GET", contentType:"application/x-www-form-urlencoded", processData:!0, async:!0, accepts:{xml:"application/xml, text/xml", html:"text/html", text:"text/plain", json:"application/json, text/javascript", "*":"*/*"}, contents:{xml:/xml/, html:/html/, json:/json/}, responseFields:{xml:"responseXML", text:"responseText"}, converters:{"* text":n.String, "text html":!0, "text json":c.parseJSON, "text xml":c.parseXML}}, ajaxPrefilter:ya(ca), ajaxTransport:ya($a), ajax:function (a, b) {
        function d(a, b, d, m) {
            if (z !==
                    2) {
                z = 2;
                w && clearTimeout(w);
                s = k;
                A = m || "";
                q.readyState = a ? 4 : 0;
                var o, n, p;
                if (d) {
                    var m = e, u = q, r = m.contents, t = m.dataTypes, T = m.responseFields, B, y, x, F;
                    for (y in T)y in d && (u[T[y]] = d[y]);
                    for (; t[0] === "*";)t.shift(), B === k && (B = m.mimeType || u.getResponseHeader("content-type"));
                    if (B)for (y in r)if (r[y] && r[y].test(B)) {
                        t.unshift(y);
                        break
                    }
                    if (t[0]in d)x = t[0]; else {
                        for (y in d) {
                            if (!t[0] || m.converters[y + " " + t[0]]) {
                                x = y;
                                break
                            }
                            F || (F = y)
                        }
                        x = x || F
                    }
                    x ? (x !== t[0] && t.unshift(x), d = d[x]) : d = void 0
                } else d = k;
                if (a >= 200 && a < 300 || a === 304) {
                    if (e.ifModified) {
                        if (B =
                                q.getResponseHeader("Last-Modified"))c.lastModified[l] = B;
                        if (B = q.getResponseHeader("Etag"))c.etag[l] = B
                    }
                    if (a === 304)b = "notmodified", o = !0; else try {
                        B = e;
                        B.dataFilter && (d = B.dataFilter(d, B.dataType));
                        var I = B.dataTypes;
                        y = {};
                        var D, G, K = I.length, H, L = I[0], C, J, M, O, E;
                        for (D = 1; D < K; D++) {
                            if (D === 1)for (G in B.converters)typeof G === "string" && (y[G.toLowerCase()] = B.converters[G]);
                            C = L;
                            L = I[D];
                            if (L === "*")L = C; else if (C !== "*" && C !== L) {
                                J = C + " " + L;
                                M = y[J] || y["* " + L];
                                if (!M)for (O in E = k, y)if (H = O.split(" "), H[0] === C || H[0] === "*")if (E =
                                        y[H[1] + " " + L]) {
                                    O = y[O];
                                    O === !0 ? M = E : E === !0 && (M = O);
                                    break
                                }
                                !M && !E && c.error("No conversion from " + J.replace(" ", " to "));
                                M !== !0 && (d = M ? M(d) : E(O(d)))
                            }
                        }
                        n = d;
                        b = "success";
                        o = !0
                    } catch (N) {
                        b = "parsererror", p = N
                    }
                } else if (p = b, !b || a)b = "error", a < 0 && (a = 0);
                q.status = a;
                q.statusText = b;
                o ? h.resolveWith(f, [n, b, q]) : h.rejectWith(f, [q, b, p]);
                q.statusCode(j);
                j = k;
                v && g.trigger("ajax" + (o ? "Success" : "Error"), [q, e, o ? n : p]);
                i.resolveWith(f, [q, b]);
                v && (g.trigger("ajaxComplete", [q, e]), --c.active || c.event.trigger("ajaxStop"))
            }
        }

        typeof a === "object" &&
        (b = a, a = k);
        var b = b || {}, e = c.ajaxSetup({}, b), f = e.context || e, g = f !== e && (f.nodeType || f instanceof c) ? c(f) : c.event, h = c.Deferred(), i = c._Deferred(), j = e.statusCode || {}, l, m = {}, n = {}, A, p, s, w, t, z = 0, v, u, q = {readyState:0, setRequestHeader:function (a, b) {
            if (!z) {
                var c = a.toLowerCase(), a = n[c] = n[c] || a;
                m[a] = b
            }
            return this
        }, getAllResponseHeaders:function () {
            return z === 2 ? A : null
        }, getResponseHeader:function (a) {
            var b;
            if (z === 2) {
                if (!p)for (p = {}; b = Ob.exec(A);)p[b[1].toLowerCase()] = b[2];
                b = p[a.toLowerCase()]
            }
            return b === k ? null : b
        }, overrideMimeType:function (a) {
            if (!z)e.mimeType =
                    a;
            return this
        }, abort:function (a) {
            a = a || "abort";
            s && s.abort(a);
            d(0, a);
            return this
        }};
        h.promise(q);
        q.success = q.done;
        q.error = q.fail;
        q.complete = i.done;
        q.statusCode = function (a) {
            if (a) {
                var b;
                if (z < 2)for (b in a)j[b] = [j[b], a[b]]; else b = a[q.status], q.then(b, b)
            }
            return this
        };
        e.url = ((a || e.url) + "").replace(Nb, "").replace(Rb, G[1] + "//");
        e.dataTypes = c.trim(e.dataType || "*").toLowerCase().split(za);
        if (e.crossDomain == null)t = Ya.exec(e.url.toLowerCase()), e.crossDomain = !(!t || !(t[1] != G[1] || t[2] != G[2] || (t[3] || (t[1] === "http:" ?
                80 : 443)) != (G[3] || (G[1] === "http:" ? 80 : 443))));
        if (e.data && e.processData && typeof e.data !== "string")e.data = c.param(e.data, e.traditional);
        W(ca, e, b, q);
        if (z === 2)return!1;
        v = e.global;
        e.type = e.type.toUpperCase();
        e.hasContent = !Qb.test(e.type);
        v && c.active++ === 0 && c.event.trigger("ajaxStart");
        if (!e.hasContent && (e.data && (e.url += (Xa.test(e.url) ? "&" : "?") + e.data), l = e.url, e.cache === !1)) {
            t = c.now();
            var F = e.url.replace(Ub, "$1_=" + t);
            e.url = F + (F === e.url ? (Xa.test(e.url) ? "&" : "?") + "_=" + t : "")
        }
        (e.data && e.hasContent && e.contentType !==
                !1 || b.contentType) && q.setRequestHeader("Content-Type", e.contentType);
        e.ifModified && (l = l || e.url, c.lastModified[l] && q.setRequestHeader("If-Modified-Since", c.lastModified[l]), c.etag[l] && q.setRequestHeader("If-None-Match", c.etag[l]));
        q.setRequestHeader("Accept", e.dataTypes[0] && e.accepts[e.dataTypes[0]] ? e.accepts[e.dataTypes[0]] + (e.dataTypes[0] !== "*" ? ", */*; q=0.01" : "") : e.accepts["*"]);
        for (u in e.headers)q.setRequestHeader(u, e.headers[u]);
        if (e.beforeSend && (e.beforeSend.call(f, q, e) === !1 || z === 2))return q.abort(),
                !1;
        for (u in{success:1, error:1, complete:1})q[u](e[u]);
        if (s = W($a, e, b, q)) {
            q.readyState = 1;
            v && g.trigger("ajaxSend", [q, e]);
            e.async && e.timeout > 0 && (w = setTimeout(function () {
                q.abort("timeout")
            }, e.timeout));
            try {
                z = 1, s.send(m, d)
            } catch (x) {
                status < 2 ? d(-1, x) : c.error(x)
            }
        } else d(-1, "No Transport");
        return q
    }, param:function (a, b) {
        var d = [], e = function (a, b) {
            b = c.isFunction(b) ? b() : b;
            d[d.length] = encodeURIComponent(a) + "=" + encodeURIComponent(b)
        };
        if (b === k)b = c.ajaxSettings.traditional;
        if (c.isArray(a) || a.jquery && !c.isPlainObject(a))c.each(a,
                function () {
                    e(this.name, this.value)
                }); else for (var f in a)da(f, a[f], b, e);
        return d.join("&").replace(Mb, "+")
    }});
    c.extend({active:0, lastModified:{}, etag:{}});
    var Vb = c.now(), $ = /(\=)\?(&|$)|\?\?/i;
    c.ajaxSetup({jsonp:"callback", jsonpCallback:function () {
        return c.expando + "_" + Vb++
    }});
    c.ajaxPrefilter("json jsonp", function (a, b, d) {
        b = a.contentType === "application/x-www-form-urlencoded" && typeof a.data === "string";
        if (a.dataTypes[0] === "jsonp" || a.jsonp !== !1 && ($.test(a.url) || b && $.test(a.data))) {
            var e, f = a.jsonpCallback =
                    c.isFunction(a.jsonpCallback) ? a.jsonpCallback() : a.jsonpCallback, g = n[f], h = a.url, i = a.data, j = "$1" + f + "$2";
            a.jsonp !== !1 && (h = h.replace($, j), a.url === h && (b && (i = i.replace($, j)), a.data === i && (h += (/\?/.test(h) ? "&" : "?") + a.jsonp + "=" + f)));
            a.url = h;
            a.data = i;
            n[f] = function (a) {
                e = [a]
            };
            d.always(function () {
                n[f] = g;
                if (e && c.isFunction(g))n[f](e[0])
            });
            a.converters["script json"] = function () {
                e || c.error(f + " was not called");
                return e[0]
            };
            a.dataTypes[0] = "json";
            return"script"
        }
    });
    c.ajaxSetup({accepts:{script:"text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"},
        contents:{script:/javascript|ecmascript/}, converters:{"text script":function (a) {
            c.globalEval(a);
            return a
        }}});
    c.ajaxPrefilter("script", function (a) {
        if (a.cache === k)a.cache = !1;
        if (a.crossDomain)a.type = "GET", a.global = !1
    });
    c.ajaxTransport("script", function (a) {
        if (a.crossDomain) {
            var b, c = m.head || m.getElementsByTagName("head")[0] || m.documentElement;
            return{send:function (e, f) {
                b = m.createElement("script");
                b.async = "async";
                if (a.scriptCharset)b.charset = a.scriptCharset;
                b.src = a.url;
                b.onload = b.onreadystatechange = function (a, e) {
                    if (e || !b.readyState || /loaded|complete/.test(b.readyState))b.onload = b.onreadystatechange = null, c && b.parentNode && c.removeChild(b), b = k, e || f(200, "success")
                };
                c.insertBefore(b, c.firstChild)
            }, abort:function () {
                if (b)b.onload(0, 1)
            }}
        }
    });
    var na = n.ActiveXObject ? function () {
        for (var a in J)J[a](0, 1)
    } : !1, Wb = 0, J;
    c.ajaxSettings.xhr = n.ActiveXObject ? function () {
        var a;
        if (!(a = !this.isLocal && Aa()))a:{
            try {
                a = new n.ActiveXObject("Microsoft.XMLHTTP");
                break a
            } catch (b) {
            }
            a = void 0
        }
        return a
    } : Aa;
    (function (a) {
        c.extend(c.support, {ajax:!!a,
            cors:!!a && "withCredentials"in a})
    })(c.ajaxSettings.xhr());
    c.support.ajax && c.ajaxTransport(function (a) {
        if (!a.crossDomain || c.support.cors) {
            var b;
            return{send:function (d, e) {
                var f = a.xhr(), g, h;
                a.username ? f.open(a.type, a.url, a.async, a.username, a.password) : f.open(a.type, a.url, a.async);
                if (a.xhrFields)for (h in a.xhrFields)f[h] = a.xhrFields[h];
                a.mimeType && f.overrideMimeType && f.overrideMimeType(a.mimeType);
                !a.crossDomain && !d["X-Requested-With"] && (d["X-Requested-With"] = "XMLHttpRequest");
                try {
                    for (h in d)f.setRequestHeader(h,
                            d[h])
                } catch (i) {
                }
                f.send(a.hasContent && a.data || null);
                b = function (d, h) {
                    var i, m, n, p, s;
                    try {
                        if (b && (h || f.readyState === 4)) {
                            b = k;
                            if (g)f.onreadystatechange = c.noop, na && delete J[g];
                            if (h)f.readyState !== 4 && f.abort(); else {
                                i = f.status;
                                n = f.getAllResponseHeaders();
                                p = {};
                                if ((s = f.responseXML) && s.documentElement)p.xml = s;
                                p.text = f.responseText;
                                try {
                                    m = f.statusText
                                } catch (v) {
                                    m = ""
                                }
                                !i && a.isLocal && !a.crossDomain ? i = p.text ? 200 : 404 : i === 1223 && (i = 204)
                            }
                        }
                    } catch (t) {
                        h || e(-1, t)
                    }
                    p && e(i, m, p, n)
                };
                !a.async || f.readyState === 4 ? b() : (g = ++Wb, na && (J ||
                        (J = {}, c(n).unload(na)), J[g] = b), f.onreadystatechange = b)
            }, abort:function () {
                b && b(0, 1)
            }}
        }
    });
    var ea = {}, v, C, Xb = /^(?:toggle|show|hide)$/, Yb = /^([+\-]=)?([\d+.\-]+)([a-z%]*)$/i, E, Ca = [
        ["height", "marginTop", "marginBottom", "paddingTop", "paddingBottom"],
        ["width", "marginLeft", "marginRight", "paddingLeft", "paddingRight"],
        ["opacity"]
    ], X, oa = n.webkitRequestAnimationFrame || n.mozRequestAnimationFrame || n.oRequestAnimationFrame;
    c.fn.extend({show:function (a, b, d) {
        if (a || a === 0)return this.animate(H("show", 3), a, b, d); else {
            for (var d =
                    0, e = this.length; d < e; d++)if (a = this[d], a.style) {
                b = a.style.display;
                if (!c._data(a, "olddisplay") && b === "none")b = a.style.display = "";
                b === "" && c.css(a, "display") === "none" && c._data(a, "olddisplay", Da(a.nodeName))
            }
            for (d = 0; d < e; d++)if (a = this[d], a.style && (b = a.style.display, b === "" || b === "none"))a.style.display = c._data(a, "olddisplay") || "";
            return this
        }
    }, hide:function (a, b, d) {
        if (a || a === 0)return this.animate(H("hide", 3), a, b, d); else {
            a = 0;
            for (b = this.length; a < b; a++)this[a].style && (d = c.css(this[a], "display"), d !== "none" && !c._data(this[a],
                    "olddisplay") && c._data(this[a], "olddisplay", d));
            for (a = 0; a < b; a++)if (this[a].style)this[a].style.display = "none";
            return this
        }
    }, _toggle:c.fn.toggle, toggle:function (a, b, d) {
        var e = typeof a === "boolean";
        c.isFunction(a) && c.isFunction(b) ? this._toggle.apply(this, arguments) : a == null || e ? this.each(function () {
            var b = e ? a : c(this).is(":hidden");
            c(this)[b ? "show" : "hide"]()
        }) : this.animate(H("toggle", 3), a, b, d);
        return this
    }, fadeTo:function (a, b, c, e) {
        return this.filter(":hidden").css("opacity", 0).show().end().animate({opacity:b},
                a, c, e)
    }, animate:function (a, b, d, e) {
        var f = c.speed(b, d, e);
        if (c.isEmptyObject(a))return this.each(f.complete, [!1]);
        a = c.extend({}, a);
        return this[f.queue === !1 ? "each" : "queue"](function () {
            var g;
            f.queue === !1 && c._mark(this);
            var b = c.extend({}, f), d = this.nodeType === 1, e = d && c(this).is(":hidden"), j, l, k, m, n;
            b.animatedProperties = {};
            for (k in a) {
                j = c.camelCase(k);
                k !== j && (a[j] = a[k], delete a[k]);
                l = a[j];
                c.isArray(l) ? (b.animatedProperties[j] = l[1], g = a[j] = l[0], l = g) : b.animatedProperties[j] = b.specialEasing && b.specialEasing[j] ||
                        b.easing || "swing";
                if (l === "hide" && e || l === "show" && !e)return b.complete.call(this);
                if (d && (j === "height" || j === "width"))if (b.overflow = [this.style.overflow, this.style.overflowX, this.style.overflowY], c.css(this, "display") === "inline" && c.css(this, "float") === "none")c.support.inlineBlockNeedsLayout ? (l = Da(this.nodeName), l === "inline" ? this.style.display = "inline-block" : (this.style.display = "inline", this.style.zoom = 1)) : this.style.display = "inline-block"
            }
            if (b.overflow != null)this.style.overflow = "hidden";
            for (k in a)if (d =
                    new c.fx(this, b, k), l = a[k], Xb.test(l))d[l === "toggle" ? e ? "show" : "hide" : l](); else j = Yb.exec(l), m = d.cur(), j ? (l = parseFloat(j[2]), n = j[3] || (c.cssNumber[k] ? "" : "px"), n !== "px" && (c.style(this, k, (l || 1) + n), m *= (l || 1) / d.cur(), c.style(this, k, m + n)), j[1] && (l = (j[1] === "-=" ? -1 : 1) * l + m), d.custom(m, l, n)) : d.custom(m, l, "");
            return!0
        })
    }, stop:function (a, b) {
        a && this.queue([]);
        this.each(function () {
            var a = c.timers, e = a.length;
            for (b || c._unmark(!0, this); e--;)if (a[e].elem === this) {
                if (b)a[e](!0);
                a.splice(e, 1)
            }
        });
        b || this.dequeue();
        return this
    }});
    c.each({slideDown:H("show", 1), slideUp:H("hide", 1), slideToggle:H("toggle", 1), fadeIn:{opacity:"show"}, fadeOut:{opacity:"hide"}, fadeToggle:{opacity:"toggle"}}, function (a, b) {
        c.fn[a] = function (a, c, f) {
            return this.animate(b, a, c, f)
        }
    });
    c.extend({speed:function (a, b, d) {
        var e = a && typeof a === "object" ? c.extend({}, a) : {complete:d || !d && b || c.isFunction(a) && a, duration:a, easing:d && b || b && !c.isFunction(b) && b};
        e.duration = c.fx.off ? 0 : typeof e.duration === "number" ? e.duration : e.duration in c.fx.speeds ? c.fx.speeds[e.duration] :
                c.fx.speeds._default;
        e.old = e.complete;
        e.complete = function (a) {
            c.isFunction(e.old) && e.old.call(this);
            e.queue !== !1 ? c.dequeue(this) : a !== !1 && c._unmark(this)
        };
        return e
    }, easing:{linear:function (a, b, c, e) {
        return c + e * a
    }, swing:function (a, b, c, e) {
        return(-Math.cos(a * Math.PI) / 2 + 0.5) * e + c
    }}, timers:[], fx:function (a, b, c) {
        this.options = b;
        this.elem = a;
        this.prop = c;
        b.orig = b.orig || {}
    }});
    c.fx.prototype = {update:function () {
        this.options.step && this.options.step.call(this.elem, this.now, this);
        (c.fx.step[this.prop] || c.fx.step._default)(this)
    },
        cur:function () {
            if (this.elem[this.prop] != null && (!this.elem.style || this.elem.style[this.prop] == null))return this.elem[this.prop];
            var a, b = c.css(this.elem, this.prop);
            return isNaN(a = parseFloat(b)) ? !b || b === "auto" ? 0 : b : a
        }, custom:function (a, b, d) {
            function e(a) {
                return f.step(a)
            }

            var f = this, g = c.fx, h;
            this.startTime = X || Ba();
            this.start = a;
            this.end = b;
            this.unit = d || this.unit || (c.cssNumber[this.prop] ? "" : "px");
            this.now = this.start;
            this.pos = this.state = 0;
            e.elem = this.elem;
            e() && c.timers.push(e) && !E && (oa ? (E = !0, h = function () {
                E &&
                (oa(h), g.tick())
            }, oa(h)) : E = setInterval(g.tick, g.interval))
        }, show:function () {
            this.options.orig[this.prop] = c.style(this.elem, this.prop);
            this.options.show = !0;
            this.custom(this.prop === "width" || this.prop === "height" ? 1 : 0, this.cur());
            c(this.elem).show()
        }, hide:function () {
            this.options.orig[this.prop] = c.style(this.elem, this.prop);
            this.options.hide = !0;
            this.custom(this.cur(), 0)
        }, step:function (a) {
            var b = X || Ba(), d = !0, e = this.elem, f = this.options, g;
            if (a || b >= f.duration + this.startTime) {
                this.now = this.end;
                this.pos = this.state =
                        1;
                this.update();
                f.animatedProperties[this.prop] = !0;
                for (g in f.animatedProperties)f.animatedProperties[g] !== !0 && (d = !1);
                if (d) {
                    f.overflow != null && !c.support.shrinkWrapBlocks && c.each(["", "X", "Y"], function (a, b) {
                        e.style["overflow" + b] = f.overflow[a]
                    });
                    f.hide && c(e).hide();
                    if (f.hide || f.show)for (var h in f.animatedProperties)c.style(e, h, f.orig[h]);
                    f.complete.call(e)
                }
                return!1
            } else f.duration == Infinity ? this.now = b : (a = b - this.startTime, this.state = a / f.duration, this.pos = c.easing[f.animatedProperties[this.prop]](this.state,
                    a, 0, 1, f.duration), this.now = this.start + (this.end - this.start) * this.pos), this.update();
            return!0
        }};
    c.extend(c.fx, {tick:function () {
        for (var a = c.timers, b = 0; b < a.length; ++b)a[b]() || a.splice(b--, 1);
        a.length || c.fx.stop()
    }, interval:13, stop:function () {
        clearInterval(E);
        E = null
    }, speeds:{slow:600, fast:200, _default:400}, step:{opacity:function (a) {
        c.style(a.elem, "opacity", a.now)
    }, _default:function (a) {
        a.elem.style && a.elem.style[a.prop] != null ? a.elem.style[a.prop] = (a.prop === "width" || a.prop === "height" ? Math.max(0, a.now) :
                a.now) + a.unit : a.elem[a.prop] = a.now
    }}});
    if (c.expr && c.expr.filters)c.expr.filters.animated = function (a) {
        return c.grep(c.timers,
                function (b) {
                    return a === b.elem
                }).length
    };
    var Zb = /^t(?:able|d|h)$/i, ab = /^(?:body|html)$/i;
    c.fn.offset = "getBoundingClientRect"in m.documentElement ? function (a) {
        var b = this[0], d;
        if (a)return this.each(function (b) {
            c.offset.setOffset(this, a, b)
        });
        if (!b || !b.ownerDocument)return null;
        if (b === b.ownerDocument.body)return c.offset.bodyOffset(b);
        try {
            d = b.getBoundingClientRect()
        } catch (e) {
        }
        var f =
                b.ownerDocument, g = f.documentElement;
        if (!d || !c.contains(g, b))return d ? {top:d.top, left:d.left} : {top:0, left:0};
        b = f.body;
        f = fa(f);
        return{top:d.top + (f.pageYOffset || c.support.boxModel && g.scrollTop || b.scrollTop) - (g.clientTop || b.clientTop || 0), left:d.left + (f.pageXOffset || c.support.boxModel && g.scrollLeft || b.scrollLeft) - (g.clientLeft || b.clientLeft || 0)}
    } : function (a) {
        var b = this[0];
        if (a)return this.each(function (b) {
            c.offset.setOffset(this, a, b)
        });
        if (!b || !b.ownerDocument)return null;
        if (b === b.ownerDocument.body)return c.offset.bodyOffset(b);
        c.offset.initialize();
        var d, e = b.offsetParent, f = b.ownerDocument, g = f.documentElement, h = f.body;
        d = (f = f.defaultView) ? f.getComputedStyle(b, null) : b.currentStyle;
        for (var i = b.offsetTop, j = b.offsetLeft; (b = b.parentNode) && b !== h && b !== g;) {
            if (c.offset.supportsFixedPosition && d.position === "fixed")break;
            d = f ? f.getComputedStyle(b, null) : b.currentStyle;
            i -= b.scrollTop;
            j -= b.scrollLeft;
            if (b === e) {
                i += b.offsetTop;
                j += b.offsetLeft;
                if (c.offset.doesNotAddBorder && (!c.offset.doesAddBorderForTableAndCells || !Zb.test(b.nodeName)))i +=
                        parseFloat(d.borderTopWidth) || 0, j += parseFloat(d.borderLeftWidth) || 0;
                e = b.offsetParent
            }
            c.offset.subtractsBorderForOverflowNotVisible && d.overflow !== "visible" && (i += parseFloat(d.borderTopWidth) || 0, j += parseFloat(d.borderLeftWidth) || 0)
        }
        if (d.position === "relative" || d.position === "static")i += h.offsetTop, j += h.offsetLeft;
        c.offset.supportsFixedPosition && d.position === "fixed" && (i += Math.max(g.scrollTop, h.scrollTop), j += Math.max(g.scrollLeft, h.scrollLeft));
        return{top:i, left:j}
    };
    c.offset = {initialize:function () {
        var a =
                m.body, b = m.createElement("div"), d, e, f, g = parseFloat(c.css(a, "marginTop")) || 0;
        c.extend(b.style, {position:"absolute", top:0, left:0, margin:0, border:0, width:"1px", height:"1px", visibility:"hidden"});
        b.innerHTML = "<div style='position:absolute;top:0;left:0;margin:0;border:5px solid #000;padding:0;width:1px;height:1px;'><div></div></div><table style='position:absolute;top:0;left:0;margin:0;border:5px solid #000;padding:0;width:1px;height:1px;' cellpadding='0' cellspacing='0'><tr><td></td></tr></table>";
        a.insertBefore(b,
                a.firstChild);
        d = b.firstChild;
        e = d.firstChild;
        f = d.nextSibling.firstChild.firstChild;
        this.doesNotAddBorder = e.offsetTop !== 5;
        this.doesAddBorderForTableAndCells = f.offsetTop === 5;
        e.style.position = "fixed";
        e.style.top = "20px";
        this.supportsFixedPosition = e.offsetTop === 20 || e.offsetTop === 15;
        e.style.position = e.style.top = "";
        d.style.overflow = "hidden";
        d.style.position = "relative";
        this.subtractsBorderForOverflowNotVisible = e.offsetTop === -5;
        this.doesNotIncludeMarginInBodyOffset = a.offsetTop !== g;
        a.removeChild(b);
        c.offset.initialize =
                c.noop
    }, bodyOffset:function (a) {
        var b = a.offsetTop, d = a.offsetLeft;
        c.offset.initialize();
        c.offset.doesNotIncludeMarginInBodyOffset && (b += parseFloat(c.css(a, "marginTop")) || 0, d += parseFloat(c.css(a, "marginLeft")) || 0);
        return{top:b, left:d}
    }, setOffset:function (a, b, d) {
        var e = c.css(a, "position");
        if (e === "static")a.style.position = "relative";
        var f = c(a), g = f.offset(), h = c.css(a, "top"), i = c.css(a, "left"), j = {}, k = {};
        (e === "absolute" || e === "fixed") && c.inArray("auto", [h, i]) > -1 ? (k = f.position(), e = k.top, i = k.left) : (e = parseFloat(h) ||
                0, i = parseFloat(i) || 0);
        c.isFunction(b) && (b = b.call(a, d, g));
        if (b.top != null)j.top = b.top - g.top + e;
        if (b.left != null)j.left = b.left - g.left + i;
        "using"in b ? b.using.call(a, j) : f.css(j)
    }};
    c.fn.extend({position:function () {
        if (!this[0])return null;
        var a = this[0], b = this.offsetParent(), d = this.offset(), e = ab.test(b[0].nodeName) ? {top:0, left:0} : b.offset();
        d.top -= parseFloat(c.css(a, "marginTop")) || 0;
        d.left -= parseFloat(c.css(a, "marginLeft")) || 0;
        e.top += parseFloat(c.css(b[0], "borderTopWidth")) || 0;
        e.left += parseFloat(c.css(b[0],
                "borderLeftWidth")) || 0;
        return{top:d.top - e.top, left:d.left - e.left}
    }, offsetParent:function () {
        return this.map(function () {
            for (var a = this.offsetParent || m.body; a && !ab.test(a.nodeName) && c.css(a, "position") === "static";)a = a.offsetParent;
            return a
        })
    }});
    c.each(["Left", "Top"], function (a, b) {
        var d = "scroll" + b;
        c.fn[d] = function (b) {
            var f, g;
            if (b === k) {
                f = this[0];
                return!f ? null : (g = fa(f)) ? "pageXOffset"in g ? g[a ? "pageYOffset" : "pageXOffset"] : c.support.boxModel && g.document.documentElement[d] || g.document.body[d] : f[d]
            }
            return this.each(function () {
                (g =
                        fa(this)) ? g.scrollTo(!a ? b : c(g).scrollLeft(), a ? b : c(g).scrollTop()) : this[d] = b
            })
        }
    });
    c.each(["Height", "Width"], function (a, b) {
        var d = b.toLowerCase();
        c.fn["inner" + b] = function () {
            var a = this[0];
            return a && a.style ? parseFloat(c.css(a, d, "padding")) : null
        };
        c.fn["outer" + b] = function (a) {
            var b = this[0];
            return b && b.style ? parseFloat(c.css(b, d, a ? "margin" : "border")) : null
        };
        c.fn[d] = function (a) {
            var f = this[0];
            if (!f)return a == null ? null : this;
            if (c.isFunction(a))return this.each(function (b) {
                var f = c(this);
                f[d](a.call(this, b, f[d]()))
            });
            if (c.isWindow(f)) {
                var g = f.document.documentElement["client" + b];
                return f.document.compatMode === "CSS1Compat" && g || f.document.body["client" + b] || g
            } else return f.nodeType === 9 ? Math.max(f.documentElement["client" + b], f.body["scroll" + b], f.documentElement["scroll" + b], f.body["offset" + b], f.documentElement["offset" + b]) : a === k ? (f = c.css(f, d), g = parseFloat(f), c.isNaN(g) ? f : g) : this.css(d, typeof a === "string" ? a : a + "px")
        }
    });
    n.jQuery = n.$ = c
})(window);
jQuery.noConflict();
