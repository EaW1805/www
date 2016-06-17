/* Copyright (C) YOOtheme GmbH, YOOtheme Proprietary Use License (http://www.yootheme.com/license) */

jQuery(function (a) {
    a('select[name="settings[style]"]').bind("change", function () {
        a("#form").trigger("submit")
    });
    a("button.action.add").bind("click", function () {
        a.post(widgetkitajax + "&task=item_slideshow", {id:a('input[name="id"]').val()}, function (c) {
            a(c).appendTo("#items");
            a("#order").trigger("update")
        })
    });
    a('input[name="id"]').val() == 0 && a("button.action.add").trigger("click");
    a("#items").delegate("#items > .box", "delete", function () {
        a(this).fadeOut(300, function () {
            a(this).remove();
            a("#order").trigger("update")
        })
    });
    a("#items").delegate("input.title", "update", function () {
        var c = a(this).closest(".item"), b = a(this).val() ? a(this).val() : "untitled";
        c.find("h3.title").html(b);
        a('#order li[rel="' + c.attr("id") + '"]').html(b)
    });
    a("#order").sortable({axis:"y", start:function (c, b) {
        a("#" + b.item.attr("rel")).addClass("sortactive")
    }, stop:function (c, b) {
        setTimeout(function () {
            a("#" + b.item.attr("rel")).removeClass("sortactive")
        }, 800)
    }, update:function (c, b) {
        var d = a("#" + b.item.attr("rel")), e = b.item.next(), f = b.item.prev();
        d.find(".html-editor").trigger("editor-action-start");
        e.length ? d.insertBefore(a("#" + e.attr("rel"))) : d.insertAfter(a("#" + f.attr("rel")));
        d.find(".html-editor").trigger("editor-action-stop")
    }}).bind("update",
            function () {
                var c = a(this);
                a("li", this).each(function () {
                    a("#" + a(this).attr("rel")).length || a(this).remove()
                });
                a("#items > .item").each(function () {
                    var b = a(this).attr("id");
                    c.find("[rel=" + b + "]").length || c.append('<li rel="' + b + '"></li>');
                    a("input.title", this).trigger("update")
                })
            }).trigger("update")
});
