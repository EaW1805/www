/* Copyright (C) YOOtheme GmbH, YOOtheme Proprietary Use License (http://www.yootheme.com/license) */

(function (b) {
    b.widgetkit.lazyloaders.slideset = function (c, a) {
        var d = c.find("ul.set");
        gwidth = a.width == "auto" ? c.width() : a.width;
        b.browser.msie && b.browser.version < 8 && d.children().css("display", "inline");
        var e = a.height == "auto" ? d.eq(0).outerHeight(!0) : a.height;
        d.eq(0).parent().css({height:e});
        c.css({width:gwidth});
        d.css({height:e});
        $script([WIDGETKIT_URL + "/widgets/slideset/js/slideset.js"], "wk-slideset");
        $script.ready("wk-slideset", function () {
            b(c).slideset(a).css("visibility", "visible")
        })
    }
})(jQuery);
