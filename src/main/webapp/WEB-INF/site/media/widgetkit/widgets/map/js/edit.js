/* Copyright (C) YOOtheme GmbH, YOOtheme Proprietary Use License (http://www.yootheme.com/license) */

jQuery(function (a) {
    a('select[name="settings[style]"]').bind("change", function () {
        a("#form").trigger("submit")
    });
    a("button.action.add").bind("click", function () {
        a.post(widgetkitajax + "&task=item_map", {id:a('input[name="id"]').val()}, function (b) {
            a(b).appendTo("#items");
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
        var b = a(this).closest(".item"), c = a(this).val() ? a(this).val() : "untitled", d = b.find("h3.title");
        b.is(".item:first-child") ? d.html(c + '<span class="info">center</span>') : d.html(c);
        a('#order li[rel="' + b.attr("id") + '"]').html(c)
    });
    a("#order").sortable({axis:"y", start:function (b, c) {
        a("#" + c.item.attr("rel")).addClass("sortactive")
    }, stop:function (b, c) {
        setTimeout(function () {
            a("#" + c.item.attr("rel")).removeClass("sortactive")
        }, 800)
    }, update:function (b, c) {
        var d =
                a("#" + c.item.attr("rel")), e = c.item.next(), g = c.item.prev();
        d.find(".html-editor").trigger("editor-action-start");
        e.length ? d.insertBefore(a("#" + e.attr("rel"))) : d.insertAfter(a("#" + g.attr("rel")));
        d.find(".html-editor").trigger("editor-action-stop");
        a("#order").trigger("update")
    }}).bind("update",
            function () {
                var b = a(this);
                a("li", this).each(function () {
                    a("#" + a(this).attr("rel")).length || a(this).remove()
                });
                a("#items > .item").each(function () {
                    var c = a(this).attr("id");
                    b.find("[rel=" + c + "]").length || b.append('<li rel="' +
                            c + '"></li>');
                    a("input.title", this).trigger("update")
                })
            }).trigger("update");
    var e = a("#addresslocator"), i = e.find("input.address");
    lat = e.find("input.lat");
    lng = e.find("input.lng");
    map = e.find("div.map");
    var h = new google.maps.Map(map.get(0), {zoom:4, streetViewControl:!1, center:new google.maps.LatLng(-34.397, 150.644), mapTypeId:google.maps.MapTypeId.ROADMAP});
    if (lat.val() != "" && lng.val() != "")var f = new google.maps.LatLng(lat.val(), lng.val()); else f = new google.maps.LatLng(51.151786, 10.415039), lat.val(51.151786),
            lng.val(10.415039);
    h.setCenter(f);
    var g = new google.maps.Marker({position:f, draggable:!0, map:h});
    google.maps.event.addListener(g, "dragend", function () {
        lat.val(g.position.lat());
        lng.val(g.position.lng())
    });
    a("#items").delegate(".loc-data", "click", function () {
        var b = a(this), c = b.find("span:first"), d = b.find('input[name$="[lat]"]'), f = b.find('input[name$="[lng]"]');
        i.autocomplete("destroy").autocomplete({delay:500, minLength:3, source:function (b, c) {
            a.getJSON(widgetkitajax + "&task=locate_map", {address:i.val()},
                    function (b) {
                        c(a.map(b.results, function (a) {
                            return{label:a.formatted_address, value:a.formatted_address, lat:a.geometry.location.lat, lng:a.geometry.location.lng}
                        }))
                    })
        }, select:function (a, b) {
            lat.val(b.item.lat);
            lng.val(b.item.lng);
            var c = new google.maps.LatLng(lat.val(), lng.val());
            g.setPosition(c);
            h.setCenter(c)
        }});
        i.val("");
        lat.val(d.val());
        lng.val(f.val());
        b = new google.maps.LatLng(d.val(), f.val());
        g.setPosition(b);
        h.setCenter(b);
        e.find("button").unbind("click").bind("click", function () {
            d.val(lat.val());
            f.val(lng.val());
            c.html("(Lat: " + lat.val() + ", Lng: " + lng.val() + ")");
            a.modalwin.close()
        });
        a.modalwin(e)
    })
});
