/* Copyright (C) YOOtheme GmbH, YOOtheme Proprietary Use License (http://www.yootheme.com/license) */

(function (e) {
    var k = [], a = function () {
    }, l = {width:200, height:"auto", total_width:"auto", sticky:!1, spacing:0, easing:"easeOutCirc", duration:500};
    a.prototype = e.extend(a.prototype, {name:"galleryslider", initialize:function (a) {
        this.options = e.extend({}, l, a);
        var c = this, a = this.element.find(".slides:first"), b = a.children(), d = this.element, g = this.options.total_width == "auto" ? d.width() : this.options.total_width, i = this.options.width, j = g / b.length - this.options.spacing, h = (g - i) / (b.length - 1) - this.options.spacing;
        b.css({width:j,
            "margin-right":this.options.spacing});
        a.width(b.eq(0).width() * b.length * 2);
        d.css({width:g, height:this.options.height});
        b.each(function () {
            var a = e(this);
            a.bind("mouseover", function () {
                b.stop().removeClass("active");
                var d = [];
                a.width();
                var f = b.not(a).filter(function () {
                    return e(this).width() > h
                });
                f.each(function () {
                    d.push(e(this).width())
                });
                a.animate({width:i}, {complete:function () {
                    f.css("width", h)
                }, easing:c.options.easing, duration:c.options.duration}).addClass("active");
                f.animate({width:h}, {easing:c.options.easing,
                    duration:c.options.duration})
            })
        });
        this.options.sticky || d.bind("mouseleave", function () {
            b.stop().animate({width:j}).removeClass("active")
        })
    }});
    e.fn[a.prototype.name] = function () {
        var f = arguments, c = f[0] ? f[0] : null;
        return this.each(function () {
            var b = e(this);
            if (a.prototype[c] && b.data(a.prototype.name) && c != "initialize")b.data(a.prototype.name)[c].apply(b.data(a.prototype.name), Array.prototype.slice.call(f, 1)); else if (!c || e.isPlainObject(c)) {
                var d = new a;
                d.element = b;
                k.push(d);
                a.prototype.initialize && d.initialize.apply(d,
                        f);
                b.data(a.prototype.name, d)
            } else e.error("Method " + c + " does not exist on jQuery." + a.prototype.name)
        })
    }
})(jQuery);
