/* Copyright (C) YOOtheme GmbH, YOOtheme Proprietary Use License (http://www.yootheme.com/license) */

jQuery(function (a) {
    var f = function (c) {
        var b = new Date(Date.parse(c.replace(/(\d+)-(\d+)-(\d+)T(.+)([-\+]\d+):(\d+)/g, "$1/$2/$3 $4 UTC$5$6"))), b = parseInt(((arguments.length > 1 ? arguments[1] : new Date).getTime() - b) / 1E3);
        return b < 60 ? a.trans.get("LESS_THAN_A_MINUTE_AGO") : b < 120 ? a.trans.get("ABOUT_A_MINUTE_AGO") : b < 2700 ? a.trans.get("X_MINUTES_AGO", parseInt(b / 60).toString()) : b < 5400 ? a.trans.get("ABOUT_AN_HOUR_AGO") : b < 86400 ? a.trans.get("X_HOURS_AGO", parseInt(b / 3600).toString()) : b < 172800 ? a.trans.get("ONE_DAY_AGO") :
                a.trans.get("X_DAYS_AGO", parseInt(b / 86400).toString())
    };
    a(".wk-twitter time").each(function () {
        a(this).html(f(a(this).attr("datetime")))
    });
    var d = a(".wk-twitter-bubbles");
    if (d.length) {
        var e = function () {
            d.each(function () {
                var c = 0;
                a(this).find("p.content").each(
                        function () {
                            var b = a(this).height();
                            b > c && (c = b)
                        }).css("min-height", c)
            })
        };
        e();
        a(window).bind("load", e)
    }
});
