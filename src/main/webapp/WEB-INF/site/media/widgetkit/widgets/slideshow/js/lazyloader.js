/* Copyright (C) YOOtheme GmbH, YOOtheme Proprietary Use License (http://www.yootheme.com/license) */

(function (c) {
    var i = [WIDGETKIT_URL + "/widgets/slideshow/js/slideshow.js"];
    c.widgetkit.lazyloaders.slideshow = function (b, a) {
        b.css("visibility", "hidden");
        var e = 0, f = 0, g = b.find("ul.slides:first");
        g.children().each(function () {
            e = Math.max(e, c(this).height());
            f = Math.max(f, c(this).width())
        });
        if (a.height == "auto")a.height = e;
        if (a.width == "auto")a.width = b.width();
        b.css({position:"relative", width:a.width});
        g.css({position:"relative", overflow:"hidden", height:a.height}).children().css({top:"0px", left:"0px", position:"absolute",
            width:b.width(), height:a.height});
        $script(i, "wk-slideshow");
        $script.ready("wk-slideshow", function () {
            b.slideshow(a).css("visibility", "visible")
        })
    };
    c.widgetkit.lazyloaders.showcase = function (b, a) {
        var e = b.find(".wk-slideshow").css("visibility", "hidden"), f = b.find(".wk-slideset").css("visibility", "hidden"), g = f.find("ul.set > li"), h = 0, j = 0, d = b.find("ul.slides:first");
        d.children().each(function () {
            h = Math.max(h, c(this).height());
            j = Math.max(j, c(this).width())
        });
        if (a.height == "auto")a.height = h;
        if (a.width == "auto")a.width =
                b.width();
        e.css({position:"relative", width:a.width});
        d.css({position:"relative", overflow:"hidden", height:a.height}).children().css({top:"0px", left:"0px", position:"absolute", width:b.width(), height:a.height});
        d = b.find("ul.set");
        gwidth = a.width == "auto" ? b.width() : a.width;
        c.browser.msie && c.browser.version < 8 && d.children().css("display", "inline");
        var k = d.eq(0).outerHeight(!0);
        b.css({width:gwidth});
        d.css({height:k}).hide();
        f.css("height", f.height() + k);
        d.show();
        $script(i, "wk-slideshow");
        $script([WIDGETKIT_URL +
                "/widgets/slideset/js/slideset.js"], "wk-slideshow-set");
        $script.ready("wk-slideshow", function () {
            $script.ready("wk-slideshow-set", function () {
                e.slideshow(a).css("visibility", "visible");
                f.slideset(c.extend({}, a, {height:"auto", autoplay:!1, duration:a.slideset_effect_duration, index:parseInt(a.index / a.items_per_set)})).css("visibility", "visible");
                var b = e.data("slideshow"), d = f.data("slideset");
                g.eq(b.index).addClass("active");
                e.bind("slideshow-show", function (a, b, c) {
                    if (!g.removeClass("active").eq(c).addClass("active").parent().is(":visible"))d[c >
                            b ? "next" : "previous"]()
                });
                g.each(function (a) {
                    c(this).bind("click", function () {
                        b.stop();
                        b.show(a)
                    })
                })
            })
        })
    }
})(jQuery);
