/* Copyright (C) YOOtheme GmbH, YOOtheme Proprietary Use License (http://www.yootheme.com/license) */

jQuery(function (a) {
    var c, b = widgetkitajax;
    a('select[name="settings[style]"]').bind("change", function () {
        a("#form").trigger("submit")
    });
    a("#finder").finder({url:b + "&task=dirs_gallery"}).delegate("a", "click", function () {
        a("#finder li").removeClass("selected");
        c = a(this).parent().addClass("selected").data("path")
    });
    a("button.add-folder").bind("click", function (b) {
        b.preventDefault();
        var d;
        a.each(a('#form input[name="paths[]"]').serializeArray(), function (a, b) {
            b.value == c && (d = !0)
        });
        c && !d && a("#gallery").trigger("add",
                [c])
    });
    a("#gallery").bind("add", function (c, d) {
        var e = a(this);
        a.post(b + "&task=files_gallery", {path:d}, function (b) {
            var c = a('<div class="box"><div class="deletable"></div><h3>' + d + '</h3><div class="content"></div></div>').appendTo(e).children(".content");
            a.each(b, function (a, b) {
                c.append('<div class="file"><div class="image"><img src="' + e.data("url") + b.path + '" class="size-auto"/></div><div class="filename">' + b.name + '</div><input type="text" name="captions[' + b.path + ']" placeholder="Enter caption here..." /><input type="text" name="links[' +
                        b.path + ']" placeholder="Enter custom link here..." /></div>')
            });
            c.append('<input type="hidden" name="paths[]" value="' + d + '" />');
            a("input:text", e.trigger("update")).placeholder()
        }, "json")
    });
    a("#gallery").delegate("#gallery > .box", "delete", function () {
        a(this).fadeOut(300, function () {
            a(this).remove()
        })
    })
});
