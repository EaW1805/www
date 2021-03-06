/* Copyright (C) YOOtheme GmbH, YOOtheme Proprietary Use License (http://www.yootheme.com/license) */

(function (c) {
    var e = function () {
    }, g = !1, i = !1, f = [];
    window.google && google.maps && (i = g = !0);
    c.extend(e.prototype, {name:"googlemaps", options:{lat:53.553407, lng:9.992196, marker:!0, popup:!1, text:"", zoom:13, mapCtrl:1, zoomWhl:!0, mapTypeId:"roadmap", typeCtrl:!0, directions:!0, directionsDestUpdate:!0, mainIcon:"red-dot", otherIcon:"blue-dot", iconUrl:"http://maps.google.com/mapfiles/ms/micons/"}, initialize:function (a, d) {
        this.options.msgFromAddress = c.trans.get("FROM_ADDRESS");
        this.options.msgGetDirections = c.trans.get("GET_DIRECTIONS");
        this.options.msgEmpty = c.trans.get("FILL_IN_ADDRESS");
        this.options.msgNotFound = c.trans.get("ADDRESS_NOT_FOUND");
        this.options.msgAddressNotFound = c.trans.get("LOCATION_NOT_FOUND");
        this.options = c.extend({}, this.options, d);
        this.container = a;
        i ? this.setupMap() : f.push(this)
    }, setupMap:function () {
        var a = this.options;
        this.map = new google.maps.Map(this.container.get(0), {mapTypeId:a.mapTypeId, center:new google.maps.LatLng(a.lat, a.lng), streetViewControl:a.mapCtrl ? !0 : !1, navigationControl:a.mapCtrl, scrollwheel:a.zoomWhl ?
                !0 : !1, mapTypeControl:a.typeCtrl ? !0 : !1, zoomControl:a.mapCtrl ? !0 : !1, zoomControlOptions:{style:a.mapCtrl == 1 ? google.maps.ZoomControlStyle.DEFAULT : google.maps.ZoomControlStyle.SMALL}});
        this.infowindow = new google.maps.InfoWindow;
        a.marker && (a.popup == 0 ? (this.map.setCenter(new google.maps.LatLng(a.lat, a.lng)), this.map.setZoom(a.zoom)) : this.addMarkerLatLng(a.lat, a.lng, a.text, !0));
        if (a.mapTypeId == "roadmap")this.map.mapTypeIds = ["custom"], this.map.mapTypes.set("custom", new google.maps.StyledMapType([
            {featureType:"all",
                elementType:"all", stylers:[
                {invert_lightness:a.styler_invert_lightness},
                {hue:a.styler_hue},
                {saturation:a.styler_saturation},
                {lightness:a.styler_lightness},
                {gamma:a.styler_gamma}
            ]}
        ], {name:"CustomStyle"})), this.map.setMapTypeId("custom");
        if (a.adresses && a.adresses.length)for (var d = 0; d < a.adresses.length; d++) {
            var b = a.adresses[d];
            b.lat && b.lng && this.addMarkerLatLng(b.lat, b.lng, b.popup, b.center, b.icon)
        }
        a.directions && this.setupDirections()
    }, createMarker:function (a, d, b) {
        var c = this, e = this.map, f = this.infowindow,
                g = new google.maps.MarkerImage(this.options.iconUrl + b + ".png", new google.maps.Size(32, 32), new google.maps.Point(0, 0), new google.maps.Point(16, 32)), b = b.match("pushpin") ? this.options.iconUrl + "pushpin_shadow.png" : this.options.iconUrl + "msmarker.shadow.png", b = new google.maps.MarkerImage(b, new google.maps.Size(56, 32), new google.maps.Point(0, 0), new google.maps.Point(16, 32)), h = new google.maps.Marker({position:a, icon:g, shadow:b, map:this.map});
        google.maps.event.addListener(h, "click", function () {
            d.length && (f.setContent(d),
                    f.open(e, h));
            if (c.options.directionsDestUpdate)c.options.lat = h.getPosition().lat(), c.options.lng = h.getPosition().lng()
        });
        return h
    }, centerMap:function (a, d) {
        this.map.setCenter(new google.maps.LatLng(a, d));
        this.map.setZoom(this.options.zoom)
    }, addMarkerLatLng:function (a, d, b, c, e) {
        e = e || this.options.otherIcon;
        if (c)e = this.options.mainIcon;
        a = new google.maps.LatLng(a, d);
        e = this.createMarker(a, b, e);
        c && (this.map.setCenter(a), this.map.setZoom(this.options.zoom));
        c && b && b.length && this.options.popup == 2 && (this.infowindow.setContent(b),
                this.infowindow.open(this.map, e))
    }, setupDirections:function () {
        var a = this;
        this.directionsService = new google.maps.DirectionsService;
        this.directionsDisplay = new google.maps.DirectionsRenderer;
        this.directionsDisplay.setMap(this.map);
        this.directionsDisplay.setPanel(c("<div>").addClass("directions").css("position", "relative").insertAfter(this.container).get(0));
        var d = c("<p>").append('<label for="from-address">' + this.options.msgFromAddress + "</label>").append('<input type="text" name="address" style="margin:0 5px;" />').append('<button type="submit">' +
                this.options.msgGetDirections + "</button>");
        c('<form method="get" action="#">').append(d).insertAfter(this.container).bind("submit", function (b) {
            b.preventDefault();
            b.stopPropagation();
            a.setDirections(c(this))
        })
    }, setDirections:function (a) {
        var d = this;
        this.container.parent().find("div.alert").remove();
        a = a.find('input[name="address"]').val();
        a === "" ? this.showAlert(this.options.msgEmpty) : this.directionsService.route({origin:a, destination:new google.maps.LatLng(this.options.lat, this.options.lng), travelMode:google.maps.DirectionsTravelMode.DRIVING},
                function (a, c) {
                    c == google.maps.DirectionsStatus.OK ? d.directionsDisplay.setDirections(a) : d.showAlert(d.options.msgNotFound)
                })
    }, showAlert:function (a) {
        c("<div>").addClass("alert").append(c("<strong>").text(a)).insertAfter(this.container)
    }, cmd:function () {
        var a = arguments, c = a[0] ? a[0] : null;
        this.map[c] && this.map[c].apply(this.map, Array.prototype.slice.call(a, 1))
    }});
    c.fn[e.prototype.name] = function () {
        var a = arguments, d = a[0] ? a[0] : null;
        return this.each(function () {
            if (!g) {
                var b = document.createElement("script");
                b.type =
                        "text/javascript";
                b.async = 1;
                b.src = location.protocol + "//maps.google.com/maps/api/js?sensor=false&callback=jQuery.fn.googlemaps.ready";
                document.body.appendChild(b);
                g = !0
            }
            b = c(this);
            if (e.prototype[d] && b.data(e.prototype.name) && d != "initialize")b.data(e.prototype.name)[d].apply(b.data(e.prototype.name), Array.prototype.slice.call(a, 1)); else if (!d || c.isPlainObject(d)) {
                var f = new e;
                e.prototype.initialize && f.initialize.apply(f, c.merge([b], a));
                b.data(e.prototype.name, f)
            } else c.error("Method " + d + " does not exist on jQuery." +
                    e.name)
        })
    };
    c.fn[e.prototype.name].ready = function () {
        for (var a = 0; a < f.length; a++)f[a].setupMap && f[a].setupMap();
        i = !0
    }
})(jQuery);
