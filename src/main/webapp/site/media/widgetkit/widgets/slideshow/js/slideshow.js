/* Copyright (C) YOOtheme GmbH, YOOtheme Proprietary Use License (http://www.yootheme.com/license) */

(function (f) {
    function o(a) {
        a = a || {};
        a.transition && (a[r + "transition"] = a.transition);
        a.transform && (a[r + "transform"] = a.transform);
        return a
    }

    f.widgetkit = f.widgetkit || {};
    var n = document.createElement("div"), n = n.style, p, t = "-webkit- -moz- -o- -ms- -khtml-".split(" "), s = "Webkit Moz O ms Khtml".split(" "), r = "";
    p = !1;
    for (var l = 0; l < s.length; l++)if (n[s[l] + "Transition"] === "") {
        p = s[l] + "Transition";
        r = t[l];
        break
    }
    p && "WebKitCSSMatrix"in window && "m11"in new WebKitCSSMatrix && navigator.userAgent.match(/Chrome/i);
    var n = null,
            i = function () {
            };
    i.prototype = f.extend(i.prototype, {name:"slideshow", options:{index:0, width:"auto", height:"auto", autoplay:!0, interval:5E3, navbar_items:4, caption_animation_duration:500, slices:20, duration:500, animated:"random", easing:"swing"}, nav:null, navbar:null, captions:null, caption:null, initialize:function (a, c) {
        var b = this, d = 0, e = 0;
        this.options = f.extend({}, this.options, c);
        this.element = this.wrapper = a;
        this.ul = this.wrapper.find("ul.slides:first").css({width:"100%", overflow:"hidden"});
        if (this.options.width ==
                "auto")this.options.width = this.wrapper.width();
        this.wrapper.css({position:"relative", width:this.options.width});
        e = this.ul.width();
        this.ul.children().each(function () {
            d = Math.max(d, f(this).height())
        });
        if (this.options.height == "auto")this.options.height = d;
        this.slides = this.ul.css({position:"relative", height:this.options.height}).children().css({top:"0px", left:"0px", position:"absolute", width:e, height:this.options.height}).hide();
        this.index = this.slides[this.options.index] ? this.options.index : 0;
        f(".next", this.wrapper).bind("click",
                function () {
                    b.stop();
                    b.nextSlide()
                });
        f(".prev", this.wrapper).bind("click", function () {
            b.stop();
            b.prevSlide()
        });
        f(this.slides.get(this.index)).show();
        if (this.wrapper.find(".nav:first").length) {
            this.nav = this.wrapper.find(".nav:first");
            var m = this.nav.children();
            m.each(function (a) {
                f(this).bind("click", function () {
                    b.stop();
                    b.slides[a] && b.show(a)
                })
            });
            a.bind("slideshow-show", function (a, b, c) {
                f(m.removeClass("active").get(c)).addClass("active")
            })
        }
        if (this.wrapper.find(".captions:first").length && this.wrapper.find(".caption:first").length)this.captions =
                this.wrapper.find(".captions:first").hide().children(), this.caption = this.wrapper.find(".caption:first").hide();
        this.nav && f(this.nav.children().get(this.index)).addClass("active");
        this.navbar && f(this.navbar.children().get(this.index)).addClass("active");
        this.showCaption(this.index);
        this.timer = null;
        this.hover = !1;
        this.wrapper.hover(function () {
            b.hover = !0
        }, function () {
            b.hover = !1
        });
        this.options.autoplay && this.start();
        "ontouchend"in document && (a.bind("touchstart", function (b) {
            function c(a) {
                if (e) {
                    var b = a.originalEvent.touches ?
                            a.originalEvent.touches[0] : a;
                    k = {time:(new Date).getTime(), coords:[b.pageX, b.pageY]};
                    Math.abs(e.coords[0] - k.coords[0]) > 10 && a.preventDefault()
                }
            }

            var d = b.originalEvent.touches ? b.originalEvent.touches[0] : b, e = {time:(new Date).getTime(), coords:[d.pageX, d.pageY], origin:f(b.target)}, k;
            a.bind("touchmove", c).one("touchend", function () {
                a.unbind("touchmove", c);
                e && k && k.time - e.time < 1E3 && Math.abs(e.coords[0] - k.coords[0]) > 30 && Math.abs(e.coords[1] - k.coords[1]) < 75 && e.origin.trigger("swipe").trigger(e.coords[0] > k.coords[0] ?
                        "swipeleft" : "swiperight");
                e = k = void 0
            })
        }), this.wrapper.bind("swipeleft",
                function () {
                    b.stop();
                    b.nextSlide()
                }).bind("swiperight", function () {
            b.stop();
            b.prevSlide()
        }))
    }, nextSlide:function () {
        this.show(this.slides[this.index + 1] ? this.index + 1 : 0)
    }, prevSlide:function () {
        this.show(this.index - 1 > -1 ? this.index - 1 : this.slides.length - 1)
    }, show:function (a) {
        if (!(this.index == a || this.fx))this.current = f(this.slides.get(this.index)), this.next = f(this.slides.get(a)), this.animated = this.options.animated, this.duration = this.options.duration,
                this.easing = this.options.easing, this.dir = a > this.index ? "right" : "left", this.showCaption(a), this.element.trigger("slideshow-show", [this.index, a]), this.index = a, this[this.animated] ? (this.fx = !0, this[this.animated]()) : (this.current.hide(), this.next.show())
    }, showCaption:function (a) {
        if (this.caption && this.captions[a]) {
            var c = f(this.captions.get(a)).html();
            this.caption.is(":animated") && this.caption.stop();
            f.trim(c) == "" ? this.caption.is(":visible") && this.caption.fadeOut(this.options.caption_animation_duration) :
                    this.caption.is(":visible") ? this.caption.fadeOut(this.options.caption_animation_duration,
                            function () {
                                f(this).html(c)
                            }).delay(200).fadeIn(this.options.caption_animation_duration) : this.caption.html(c).fadeIn(this.options.caption_animation_duration)
        }
    }, start:function () {
        if (!this.timer) {
            var a = this;
            this.timer = setInterval(function () {
                !a.hover && !a.fx && a.nextSlide()
            }, this.options.interval)
        }
    }, stop:function () {
        this.timer && clearInterval(this.timer)
    }, bindTransitionEnd:function (a) {
        var c = this;
        f(a).bind("webkitTransitionEnd transitionend oTransitionEnd msTransitionEnd",
                function () {
                    c.fx = null;
                    c.next.css({"z-index":"2", left:0, top:0}).show();
                    c.current.hide();
                    c.slicer.remove()
                })
    }, randomSimple:function () {
        var a = "top,bottom,left,right,fade,scrollLeft,scrollRight,scroll".split(",");
        this.animated = a[Math.floor(Math.random() * a.length)];
        this[this.animated]()
    }, randomFx:function () {
        var a = "sliceUp,sliceDown,sliceUpDown,fold,puzzle,boxes,boxesRtl".split(",");
        this.animated = a[Math.floor(Math.random() * a.length)];
        this[this.animated]()
    }, top:function () {
        var a = this;
        this.current.css({"z-index":1});
        this.next.css({"z-index":2, display:"block", left:0, top:this.wrapper.height() * (this.animated == "bottom" ? 2 : -1)}).animate({top:0}, {duration:a.duration, easing:a.easing, complete:function () {
            a.fx = null;
            a.current.hide()
        }})
    }, bottom:function () {
        this.top.apply(this)
    }, left:function () {
        var a = this;
        this.current.css({"z-index":1});
        this.next.css({"z-index":2, display:"block", left:this.current.width() * (this.animated == "right" ? 2 : -1)}).animate({left:0}, {duration:a.duration, easing:this.easing, complete:function () {
            a.fx = null;
            a.current.hide()
        }})
    },
        right:function () {
            this.left()
        }, fade:function () {
            var a = this;
            this.next.css({top:0, left:0, "z-index":1}).fadeIn(a.duration);
            this.current.css({"z-index":2}).fadeOut(this.duration, function () {
                a.fx = null;
                a.current.hide().css({opacity:1})
            })
        }, scrollLeft:function () {
            var a = this;
            this.current.css({"z-index":1});
            this.next.css({"z-index":2, display:"block", left:this.current.width() * (this.animated == "scrollRight" ? 1 : -1)}).animate({left:0}, {duration:a.duration, easing:this.easing, complete:function () {
                a.fx = null;
                a.current.hide()
            },
                step:function (c, b) {
                    a.current.css("left", (Math.abs(b.start) - Math.abs(c)) * (a.animated == "scrollRight" ? -1 : 1))
                }})
        }, scrollRight:function () {
            this.scrollLeft(this)
        }, scroll:function () {
            var a = this;
            this.current.css({"z-index":1});
            this.next.css({"z-index":2, display:"block", left:this.current.width() * (this.dir == "right" ? 1 : -1)}).animate({left:0}, {duration:a.duration, easing:this.easing, complete:function () {
                a.fx = null;
                a.current.hide()
            }, step:function (c, b) {
                a.current.css("left", (Math.abs(b.start) - Math.abs(c)) * (a.dir == "right" ?
                        -1 : 1))
            }})
        }, slice:function () {
            var a = this, c = this.next.find("img:first"), b = this.animated == "sliceUp" ? "bottom" : "top";
            if (c.length) {
                var d = this.current.find("img:first").position();
                q(this, d.top, d.left);
                for (var d = Math.round(this.slicer.width() / this.options.slices), e = 0; e < this.options.slices; e++) {
                    var m = e == this.options.slices - 1 ? this.slicer.width() - d * e : d;
                    this.animated == "sliceUpDown" && (b = (e % 2 + 2) % 2 == 0 ? "top" : "bottom");
                    this.slicer.append(f("<div />").css(b, 0).css(o({position:"absolute", left:d * e + "px", width:m + "px", height:0,
                        background:"url(" + c.attr("src") + ") no-repeat -" + (d + e * d - d) + "px " + b, opacity:0, transition:"all " + a.duration + "ms ease-in " + e * 60 + "ms"})))
                }
                this.slices = this.slicer.children();
                this.bindTransitionEnd.apply(this, [this.slices.get(this.slices.length - 1)]);
                this.current.css({"z-index":1});
                this.slicer.show();
                var j = this.wrapper.height();
                if (p)this.slices.css(o({height:j, opacity:1})); else {
                    var g = 0;
                    this.slices.each(function (b) {
                        var c = f(this);
                        setTimeout(function () {
                            c.animate({height:j, opacity:1}, a.duration, function () {
                                b ==
                                        a.slices.length - 1 && f(this).trigger("transitionend")
                            })
                        }, g);
                        g += 60
                    })
                }
            } else this.next.css({"z-index":"2", left:0, top:0}).show(), this.current.hide(), this.fx = null
        }, sliceUp:function () {
            this.slice.apply(this)
        }, sliceDown:function () {
            this.slice.apply(this)
        }, sliceUpDown:function () {
            this.slice.apply(this)
        }, fold:function () {
            var a = this, c = this.next.find("img:first");
            if (c.length) {
                var b = this.current.find("img:first").position();
                q(this, b.top, b.left);
                for (var d = Math.round(this.slicer.width() / this.options.slices) + 2, b = 0; b <
                        this.options.slices + 1; b++) {
                    var e = b == a.options.slices ? a.slicer.width() - d * b : d;
                    this.slicer.append(f("<div />").css(o({position:"absolute", left:d * b + "px", width:e, top:"0px", height:a.options.height, background:"url(" + c.attr("src") + ") no-repeat -" + (d + b * d - d) + "px 0%", opacity:0, transform:"scalex(0.0001)", transition:"all " + a.duration + "ms ease-in " + (100 + b * 60) + "ms"})))
                }
                this.slices = this.slicer.children();
                this.bindTransitionEnd.apply(this, [this.slices.get(this.slices.length - 1)]);
                this.current.css({"z-index":1});
                this.slicer.show();
                if (p)this.slices.css(o({opacity:1, transform:"scalex(1)"})); else {
                    var m = 0;
                    this.slices.width(0).each(function (b) {
                        var c = b == a.options.slices - 1 ? a.slicer.width() - d * b : d, e = f(this);
                        setTimeout(function () {
                            e.animate({opacity:1, width:c}, a.duration, function () {
                                b == a.slices.length - 1 && f(this).trigger("transitionend")
                            })
                        }, m + 100);
                        m += 50
                    })
                }
            } else this.next.css({"z-index":"2", left:0, top:0}).show(), this.current.hide(), this.fx = null
        }, puzzle:function () {
            var a = this, c = Math.round(this.options.slices / 2), b = Math.round(this.wrapper.width() /
                    c), d = Math.round(this.wrapper.height() / b), e = Math.round(this.wrapper.height() / d) + 1, m = this.next.find("img:first");
            if (m.length) {
                var j = this.current.find("img:first").position();
                q(this, j.top, j.left);
                for (var j = this.wrapper.width(), g = 0; g < d; g++)for (var h = 0; h < c; h++) {
                    var i = f("<div />").css(o({position:"absolute", left:b * h + "px", width:h == c - 1 ? j - b * h + "px" : b + "px", top:e * g + "px", height:e + "px", background:"url(" + m.attr("src") + ") no-repeat -" + (b + h * b - b) + "px -" + (e + g * e - e) + "px", opacity:0, transition:"all " + a.duration + "ms ease-in 0ms"}));
                    l += 1;
                    this.slicer.append(i)
                }
                this.slices = u(this.slicer.children());
                this.bindTransitionEnd.apply(this, [this.slices.get(this.slices.length - 1)]);
                this.current.css({"z-index":1});
                this.slicer.show();
                this.slices.each(function (b) {
                    var c = f(this);
                    setTimeout(function () {
                        p ? c.css(o({opacity:1})) : c.animate({opacity:1}, a.duration, function () {
                            b == a.slices.length - 1 && f(this).trigger("transitionend")
                        })
                    }, 100 + b * 50)
                })
            } else this.next.css({"z-index":"2", left:0, top:0}).show(), this.current.hide(), this.fx = null
        }, boxes:function () {
            var a =
                    this, c = Math.round(this.options.slices / 2), b = Math.round(this.wrapper.width() / c), d = Math.round(this.wrapper.height() / b), e = Math.round(this.wrapper.height() / d) + 1, i = 0, j = this.next.find("img:first");
            if (j.length) {
                var g = this.current.find("img:first").position();
                q(this, g.top, g.left);
                for (g = 0; g < d; g++)for (var h = 0; h < c; h++)this.slicer.append(f("<div />").css(o({position:"absolute", left:b * h + "px", width:0, top:e * g + "px", height:0, background:"url(" + j.attr("src") + ") no-repeat -" + (b + h * b - b) + "px -" + (e + g * e - e) + "px", opacity:0,
                    transition:"all " + (100 + a.duration) + "ms ease-in 0ms"})).data("base", {width:h == c - 1 ? this.wrapper.width() - b * h : b, height:e}));
                this.slices = this.slicer.children();
                this.current.css({"z-index":1});
                this.slicer.show();
                var l = 0, k = 0, n = [];
                n[l] = [];
                b = this.animated == "boxesRtl" ? this.slices._reverse() : this.slices;
                this.bindTransitionEnd.apply(this, [b.get(b.length - 1)]);
                b.each(function () {
                    n[l][k] = f(this);
                    k++;
                    k == c && (l++, k = 0, n[l] = [])
                });
                for (h = e = 0; h < c * d; h++) {
                    j = h;
                    for (g = 0; g < d; g++)j >= 0 && j < c && (function (b, c, d, e) {
                        var g = n[b][c];
                        setTimeout(function () {
                            p ?
                                    g.css({opacity:"1", width:g.data("base").width, height:g.data("base").height}) : g.animate({opacity:"1", width:g.data("base").width, height:g.data("base").height}, a.duration, function () {
                                e == a.slices.length - 1 && f(this).trigger("transitionend")
                            })
                        }, 100 + d)
                    }(g, j, i, e, b.length), e++), j--;
                    i += 100
                }
            } else this.next.css({"z-index":"2", left:0, top:0}).show(), this.current.hide(), this.fx = null
        }, boxesRtl:function () {
            this.boxes.apply(this)
        }});
    f.fn._reverse = [].reverse;
    var u = function (a) {
        for (var c, b, d = a.length; d; c = parseInt(Math.random() *
                d), b = a[--d], a[d] = a[c], a[c] = b);
        return a
    }, q = function (a, c, b) {
        c = c || 0;
        b = b || 0;
        a.slicer = f("<li />").addClass("slices").css({top:c, left:b, position:"absolute", width:a.wrapper.width(), height:a.ul.height(), "z-index":3}).hide().appendTo(a.ul)
    };
    f.fn[i.prototype.name] = function () {
        var a = arguments, c = a[0] ? a[0] : null;
        return this.each(function () {
            var b = f(this);
            if (i.prototype[c] && b.data(i.prototype.name) && c != "initialize")b.data(i.prototype.name)[c].apply(b.data(i.prototype.name), Array.prototype.slice.call(a, 1)); else if (!c ||
                    f.isPlainObject(c)) {
                var d = new i;
                i.prototype.initialize && d.initialize.apply(d, f.merge([b], a));
                b.data(i.prototype.name, d)
            } else f.error("Method " + c + " does not exist on jQuery." + i.name)
        })
    }
})(jQuery);
