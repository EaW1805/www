/* Copyright (C) YOOtheme GmbH, YOOtheme Proprietary Use License (http://www.yootheme.com/license) */

(function (d) {
    var a = function () {
    };
    a.prototype = d.extend(a.prototype, {name:"accordion", options:{index:0, duration:500, easing:"easeOutQuart", animated:"slide", event:"click", collapseall:!0, matchheight:!0, toggler:".toggler", content:".content"}, initialize:function (a, b) {
        var b = d.extend({}, this.options, b), c = a.find(b.toggler), g = function (a) {
            var f = c.eq(a).hasClass("active") ? d([]) : c.eq(a), e = c.eq(a).hasClass("active") ? c.eq(a) : d([]);
            f.hasClass("active") && (e = f, f = d([]));
            b.collapseall && (e = c.filter(".active"));
            switch (b.animated) {
                case "slide":
                    f.next().stop().show().animate({height:f.next().data("height")},
                            {easing:b.easing, duration:b.duration});
                    e.next().stop().animate({height:0}, {easing:b.easing, duration:b.duration, complete:function () {
                        e.next().hide()
                    }});
                    break;
                default:
                    f.next().show().css("height", f.next().data("height")), e.next().hide().css("height", 0)
            }
            f.addClass("active");
            e.removeClass("active")
        }, i = 0;
        b.matchheight && a.find(b.content).each(
                function () {
                    i = Math.max(i, d(this).height())
                }).css("min-height", i);
        c.each(function (a) {
            var c = d(this), e = c.next(b.content).wrap("<div>").parent().css("overflow", "hidden");
            e.data("height", e.height()).addClass("content-wrapper");
            a == b.index ? (c.addClass("active"), e.show()) : e.hide().css("height", 0);
            c.bind(b.event, function () {
                g(a)
            })
        })
    }});
    d.fn[a.prototype.name] = function () {
        var h = arguments, b = h[0] ? h[0] : null;
        return this.each(function () {
            var c = d(this);
            if (a.prototype[b] && c.data(a.prototype.name) && b != "initialize")c.data(a.prototype.name)[b].apply(c.data(a.prototype.name), Array.prototype.slice.call(h, 1)); else if (!b || d.isPlainObject(b)) {
                var g = new a;
                a.prototype.initialize && g.initialize.apply(g,
                        d.merge([c], h));
                c.data(a.prototype.name, g)
            } else d.error("Method " + b + " does not exist on jQuery." + a.name)
        })
    };
    d.widgetkit && (d.widgetkit.lazyloaders.accordion = function (a, b) {
        d(a).accordion(b)
    })
})(jQuery);
