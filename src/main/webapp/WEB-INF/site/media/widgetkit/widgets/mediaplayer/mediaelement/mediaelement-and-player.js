var mejs = mejs || {};
mejs.version = "2.1.9";
mejs.meIndex = 0;
mejs.plugins = {silverlight:[
    {version:[3, 0], types:"video/mp4,video/m4v,video/mov,video/wmv,audio/wma,audio/m4a,audio/mp3,audio/wav,audio/mpeg".split(",")}
], flash:[
    {version:[9, 0, 124], types:"video/mp4,video/m4v,video/mov,video/flv,video/x-flv,audio/flv,audio/x-flv,audio/mp3,audio/m4a,audio/mpeg".split(",")}
]};
mejs.Utility = {encodeUrl:function (c) {
    return encodeURIComponent(c)
}, escapeHTML:function (c) {
    return c.toString().split("&").join("&amp;").split("<").join("&lt;").split('"').join("&quot;")
}, absolutizeUrl:function (c) {
    var a = document.createElement("div");
    a.innerHTML = '<a href="' + this.escapeHTML(c) + '">x</a>';
    return a.firstChild.href
}, getScriptPath:function (c) {
    for (var a = 0, b, d = "", e = "", f, g = document.getElementsByTagName("script"); a < g.length; a++) {
        f = g[a].src;
        for (b = 0; b < c.length; b++)if (e = c[b], f.indexOf(e) > -1) {
            d = f.substring(0,
                    f.indexOf(e));
            break
        }
        if (d !== "")break
    }
    return d
}, secondsToTimeCode:function (c, a) {
    var c = Math.round(c), b, d = Math.floor(c / 60);
    d >= 60 && (b = Math.floor(d / 60), d %= 60);
    b = b === void 0 ? "00" : b >= 10 ? b : "0" + b;
    c = Math.floor(c % 60);
    return(b > 0 || a === !0 ? b + ":" : "") + (d >= 10 ? d : "0" + d) + ":" + (c >= 10 ? c : "0" + c)
}, timeCodeToSeconds:function (c) {
    c = c.split(":");
    return c[0] * 3600 + c[1] * 60 + parseFloat(c[2].replace(",", "."))
}};
mejs.PluginDetector = {hasPluginVersion:function (c, a) {
    var b = this.plugins[c];
    a[1] = a[1] || 0;
    a[2] = a[2] || 0;
    return b[0] > a[0] || b[0] == a[0] && b[1] > a[1] || b[0] == a[0] && b[1] == a[1] && b[2] >= a[2] ? !0 : !1
}, nav:window.navigator, ua:window.navigator.userAgent.toLowerCase(), plugins:[], addPlugin:function (c, a, b, d, e) {
    this.plugins[c] = this.detectPlugin(a, b, d, e)
}, detectPlugin:function (c, a, b, d) {
    var e = [0, 0, 0], f;
    if (typeof this.nav.plugins != "undefined" && typeof this.nav.plugins[c] == "object") {
        if ((b = this.nav.plugins[c].description) && !(typeof this.nav.mimeTypes !=
                "undefined" && this.nav.mimeTypes[a] && !this.nav.mimeTypes[a].enabledPlugin)) {
            e = b.replace(c, "").replace(/^\s+/, "").replace(/\sr/gi, ".").split(".");
            for (c = 0; c < e.length; c++)e[c] = parseInt(e[c].match(/\d+/), 10)
        }
    } else if (typeof window.ActiveXObject != "undefined")try {
        (f = new ActiveXObject(b)) && (e = d(f))
    } catch (g) {
    }
    return e
}};
mejs.PluginDetector.addPlugin("flash", "Shockwave Flash", "application/x-shockwave-flash", "ShockwaveFlash.ShockwaveFlash", function (c) {
    var a = [];
    if (c = c.GetVariable("$version"))c = c.split(" ")[1].split(","), a = [parseInt(c[0], 10), parseInt(c[1], 10), parseInt(c[2], 10)];
    return a
});
mejs.PluginDetector.addPlugin("silverlight", "Silverlight Plug-In", "application/x-silverlight-2", "AgControl.AgControl", function (c) {
    var a = [0, 0, 0, 0], b = function (a, b, c, g) {
        for (; a.isVersionSupported(b[0] + "." + b[1] + "." + b[2] + "." + b[3]);)b[c] += g;
        b[c] -= g
    };
    b(c, a, 0, 1);
    b(c, a, 1, 1);
    b(c, a, 2, 1E4);
    b(c, a, 2, 1E3);
    b(c, a, 2, 100);
    b(c, a, 2, 10);
    b(c, a, 2, 1);
    b(c, a, 3, 1);
    return a
});
mejs.MediaFeatures = {init:function () {
    var c = mejs.PluginDetector.nav, a = mejs.PluginDetector.ua.toLowerCase(), b, d = ["source", "track", "audio", "video"];
    this.isiPad = a.match(/ipad/i) !== null;
    this.isiPhone = a.match(/iphone/i) !== null;
    this.isAndroid = a.match(/android/i) !== null;
    this.isBustedAndroid = a.match(/android 2\.[12]/) !== null;
    this.isIE = c.appName.toLowerCase().indexOf("microsoft") != -1;
    this.isChrome = a.match(/chrome/gi) !== null;
    this.isFirefox = a.match(/firefox/gi) !== null;
    for (c = 0; c < d.length; c++)b = document.createElement(d[c]);
    this.hasNativeFullScreen = typeof b.webkitRequestFullScreen !== "undefined";
    if (this.isChrome)this.hasNativeFullScreen = !1;
    if (this.hasNativeFullScreen && a.match(/mac os x 10_5/i))this.hasNativeFullScreen = !1
}};
mejs.MediaFeatures.init();
mejs.HtmlMediaElement = {pluginType:"native", isFullScreen:!1, setCurrentTime:function (c) {
    this.currentTime = c
}, setMuted:function (c) {
    this.muted = c
}, setVolume:function (c) {
    this.volume = c
}, stop:function () {
    this.pause()
}, setSrc:function (c) {
    if (typeof c == "string")this.src = c; else {
        var a, b;
        for (a = 0; a < c.length; a++)if (b = c[a], this.canPlayType(b.type))this.src = b.src
    }
}, setVideoSize:function (c, a) {
    this.width = c;
    this.height = a
}};
mejs.PluginMediaElement = function (c, a, b) {
    this.id = c;
    this.pluginType = a;
    this.src = b;
    this.events = {}
};
mejs.PluginMediaElement.prototype = {pluginElement:null, pluginType:"", isFullScreen:!1, playbackRate:-1, defaultPlaybackRate:-1, seekable:[], played:[], paused:!0, ended:!1, seeking:!1, duration:0, error:null, muted:!1, volume:1, currentTime:0, play:function () {
    if (this.pluginApi != null)this.pluginApi.playMedia(), this.paused = !1
}, load:function () {
    if (this.pluginApi != null)this.pluginApi.loadMedia(), this.paused = !1
}, pause:function () {
    if (this.pluginApi != null)this.pluginApi.pauseMedia(), this.paused = !0
}, stop:function () {
    if (this.pluginApi !=
            null)this.pluginApi.stopMedia(), this.paused = !0
}, canPlayType:function (c) {
    var a, b, d, e = mejs.plugins[this.pluginType];
    for (a = 0; a < e.length; a++)if (d = e[a], mejs.PluginDetector.hasPluginVersion(this.pluginType, d.version))for (b = 0; b < d.types.length; b++)if (c == d.types[b])return!0;
    return!1
}, setSrc:function (c) {
    if (typeof c == "string")this.pluginApi.setSrc(mejs.Utility.absolutizeUrl(c)), this.src = mejs.Utility.absolutizeUrl(c); else {
        var a, b;
        for (a = 0; a < c.length; a++)if (b = c[a], this.canPlayType(b.type))this.pluginApi.setSrc(mejs.Utility.absolutizeUrl(b.src)),
                this.src = mejs.Utility.absolutizeUrl(c)
    }
}, setCurrentTime:function (c) {
    if (this.pluginApi != null)this.pluginApi.setCurrentTime(c), this.currentTime = c
}, setVolume:function (c) {
    if (this.pluginApi != null)this.pluginApi.setVolume(c), this.volume = c
}, setMuted:function (c) {
    if (this.pluginApi != null)this.pluginApi.setMuted(c), this.muted = c
}, setVideoSize:function (c, a) {
    if (this.pluginElement.style)this.pluginElement.style.width = c + "px", this.pluginElement.style.height = a + "px";
    this.pluginApi != null && this.pluginApi.setVideoSize(c,
            a)
}, setFullscreen:function (c) {
    this.pluginApi != null && this.pluginApi.setFullscreen(c)
}, addEventListener:function (c, a) {
    this.events[c] = this.events[c] || [];
    this.events[c].push(a)
}, removeEventListener:function (c, a) {
    if (!c)return this.events = {}, !0;
    var b = this.events[c];
    if (!b)return!0;
    if (!a)return this.events[c] = [], !0;
    for (i = 0; i < b.length; i++)if (b[i] === a)return this.events[c].splice(i, 1), !0;
    return!1
}, dispatchEvent:function (c) {
    var a, b, d = this.events[c];
    if (d) {
        b = Array.prototype.slice.call(arguments, 1);
        for (a = 0; a < d.length; a++)d[a].apply(null,
                b)
    }
}};
mejs.MediaPluginBridge = {pluginMediaElements:{}, htmlMediaElements:{}, registerPluginElement:function (c, a, b) {
    this.pluginMediaElements[c] = a;
    this.htmlMediaElements[c] = b
}, initPlugin:function (c) {
    var a = this.pluginMediaElements[c], b = this.htmlMediaElements[c];
    switch (a.pluginType) {
        case "flash":
            a.pluginElement = a.pluginApi = document.getElementById(c);
            break;
        case "silverlight":
            a.pluginElement = document.getElementById(a.id), a.pluginApi = a.pluginElement.Content.MediaElementJS
    }
    a.pluginApi != null && a.success && a.success(a, b)
},
    fireEvent:function (c, a, b) {
        var d, e, c = this.pluginMediaElements[c];
        c.ended = !1;
        c.paused = !0;
        a = {type:a, target:c};
        for (d in b)c[d] = b[d], a[d] = b[d];
        e = b.bufferedTime || 0;
        a.target.buffered = a.buffered = {start:function () {
            return 0
        }, end:function () {
            return e
        }, length:1};
        c.dispatchEvent(a.type, a)
    }};
mejs.MediaElementDefaults = {mode:"auto", plugins:["flash", "silverlight"], enablePluginDebug:!1, type:"", pluginPath:mejs.Utility.getScriptPath(["mediaelement.js", "mediaelement.min.js", "mediaelement-and-player.js", "mediaelement-and-player.min.js"]), flashName:"flashmediaelement.swf", enablePluginSmoothing:!1, silverlightName:"silverlightmediaelement.xap", defaultVideoWidth:480, defaultVideoHeight:270, pluginWidth:-1, pluginHeight:-1, timerRate:250, success:function () {
}, error:function () {
}};
mejs.MediaElement = function (c, a) {
    return mejs.HtmlMediaElementShim.create(c, a)
};
mejs.HtmlMediaElementShim = {create:function (c, a) {
    var b = mejs.MediaElementDefaults, d = typeof c == "string" ? document.getElementById(c) : c, e = d.tagName.toLowerCase() == "video", f = typeof d.canPlayType != "undefined", g = {method:"", url:""}, j = d.getAttribute("poster"), h = d.getAttribute("autoplay"), k = d.getAttribute("preload"), l = d.getAttribute("controls"), m;
    for (m in a)b[m] = a[m];
    j = typeof j == "undefined" || j === null ? "" : j;
    k = typeof k == "undefined" || k === null || k === "false" ? "none" : k;
    h = !(typeof h == "undefined" || h === null || h === "false");
    l = !(typeof l == "undefined" || l === null || l === "false");
    g = this.determinePlayback(d, b, e, f);
    if (g.method == "native") {
        if (mejs.MediaFeatures.isBustedAndroid)d.src = g.url, d.addEventListener("click", function () {
            d.play()
        }, !0);
        return this.updateNative(d, b, h, k, g)
    } else if (g.method !== "")return this.createPlugin(d, b, e, g.method, g.url !== null ? mejs.Utility.absolutizeUrl(g.url) : "", j, h, k, l); else this.createErrorMessage(d, b, g.url !== null ? mejs.Utility.absolutizeUrl(g.url) : "", j)
}, determinePlayback:function (c, a, b, d) {
    var e = [], f, g,
            j = {method:"", url:""}, h = c.getAttribute("src"), k, l;
    if (h == "undefined" || h == "" || h === null)h = null;
    if (typeof a.type != "undefined" && a.type !== "")e.push({type:a.type, url:h}); else if (h !== null)g = this.checkType(h, c.getAttribute("type"), b), e.push({type:g, url:h}); else for (f = 0; f < c.childNodes.length; f++)g = c.childNodes[f], g.nodeType == 1 && g.tagName.toLowerCase() == "source" && (h = g.getAttribute("src"), g = this.checkType(h, g.getAttribute("type"), b), e.push({type:g, url:h}));
    if (mejs.MediaFeatures.isBustedAndroid)c.canPlayType = function (a) {
        return a.match(/video\/(mp4|m4v)/gi) !==
                null ? "maybe" : ""
    };
    if (d && (a.mode === "auto" || a.mode === "native"))for (f = 0; f < e.length; f++)if (c.canPlayType(e[f].type).replace(/no/, "") !== "" || c.canPlayType(e[f].type.replace(/mp3/, "mpeg")).replace(/no/, "") !== "")return j.method = "native", j.url = e[f].url, j;
    if (a.mode === "auto" || a.mode === "shim")for (f = 0; f < e.length; f++) {
        g = e[f].type;
        for (c = 0; c < a.plugins.length; c++) {
            h = a.plugins[c];
            k = mejs.plugins[h];
            for (b = 0; b < k.length; b++)if (l = k[b], mejs.PluginDetector.hasPluginVersion(h, l.version))for (d = 0; d < l.types.length; d++)if (g == l.types[d])return j.method =
                    h, j.url = e[f].url, j
        }
    }
    if (j.method === "")j.url = e[0].url;
    return j
}, checkType:function (c, a, b) {
    return c && !a ? (c = c.substring(c.lastIndexOf(".") + 1), (b ? "video" : "audio") + "/" + c) : a && ~a.indexOf(";") ? a.substr(0, a.indexOf(";")) : a
}, createErrorMessage:function (c, a, b, d) {
    var e = document.createElement("div");
    e.className = "me-cannotplay";
    try {
        e.style.width = c.width + "px", e.style.height = c.height + "px"
    } catch (f) {
    }
    e.innerHTML = d !== "" ? '<a href="' + b + '"><img src="' + d + '" /></a>' : '<a href="' + b + '"><span>Download File</span></a>';
    c.parentNode.insertBefore(e,
            c);
    c.style.display = "none";
    a.error(c)
}, createPlugin:function (c, a, b, d, e, f, g, j, h) {
    var k = f = 1, l = "me_" + d + "_" + mejs.meIndex++, m = new mejs.PluginMediaElement(l, d, e), n = document.createElement("div"), o;
    for (o = c.parentNode; o !== null && o.tagName.toLowerCase() != "body";) {
        if (o.parentNode.tagName.toLowerCase() == "p") {
            o.parentNode.parentNode.insertBefore(o, o.parentNode);
            break
        }
        o = o.parentNode
    }
    b ? (f = a.videoWidth > 0 ? a.videoWidth : c.getAttribute("width") !== null ? c.getAttribute("width") : a.defaultVideoWidth, k = a.videoHeight > 0 ? a.videoHeight :
            c.getAttribute("height") !== null ? c.getAttribute("height") : a.defaultVideoHeight) : a.enablePluginDebug && (f = 320, k = 240);
    m.success = a.success;
    mejs.MediaPluginBridge.registerPluginElement(l, m, c);
    n.className = "me-plugin";
    c.parentNode.insertBefore(n, c);
    b = ["id=" + l, "isvideo=" + (b ? "true" : "false"), "autoplay=" + (g ? "true" : "false"), "preload=" + j, "width=" + f, "startvolume=" + a.startVolume, "timerrate=" + a.timerRate, "height=" + k];
    e !== null && (d == "flash" ? b.push("file=" + mejs.Utility.encodeUrl(e)) : b.push("file=" + e));
    a.enablePluginDebug &&
    b.push("debug=true");
    a.enablePluginSmoothing && b.push("smoothing=true");
    h && b.push("controls=true");
    switch (d) {
        case "silverlight":
            n.innerHTML = '<object data="data:application/x-silverlight-2," type="application/x-silverlight-2" id="' + l + '" name="' + l + '" width="' + f + '" height="' + k + '"><param name="initParams" value="' + b.join(",") + '" /><param name="windowless" value="true" /><param name="background" value="black" /><param name="minRuntimeVersion" value="3.0.0.0" /><param name="autoUpgrade" value="true" /><param name="source" value="' +
                    a.pluginPath + a.silverlightName + '" /></object>';
            break;
        case "flash":
            mejs.MediaFeatures.isIE ? (d = document.createElement("div"), n.appendChild(d), d.outerHTML = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab" id="' + l + '" width="' + f + '" height="' + k + '"><param name="movie" value="' + a.pluginPath + a.flashName + "?x=" + new Date + '" /><param name="flashvars" value="' + b.join("&amp;") + '" /><param name="quality" value="high" /><param name="bgcolor" value="#000000" /><param name="wmode" value="transparent" /><param name="allowScriptAccess" value="always" /><param name="allowFullScreen" value="true" /></object>') :
                    n.innerHTML = '<embed id="' + l + '" name="' + l + '" play="true" loop="false" quality="high" bgcolor="#000000" wmode="transparent" allowScriptAccess="always" allowFullScreen="true" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" src="' + a.pluginPath + a.flashName + '" flashvars="' + b.join("&") + '" width="' + f + '" height="' + k + '"></embed>'
    }
    c.style.display = "none";
    return m
}, updateNative:function (c, a) {
    for (var b in mejs.HtmlMediaElement)c[b] = mejs.HtmlMediaElement[b];
    a.success(c,
            c);
    return c
}};
window.mejs = mejs;
window.MediaElement = mejs.MediaElement;
if (typeof jQuery != "undefined")mejs.$ = jQuery; else if (typeof ender != "undefined")mejs.$ = ender;
(function (c) {
    mejs.MepDefaults = {poster:"", defaultVideoWidth:480, defaultVideoHeight:270, videoWidth:-1, videoHeight:-1, audioWidth:400, audioHeight:30, startVolume:0.8, loop:!1, enableAutosize:!0, alwaysShowHours:!1, alwaysShowControls:!1, iPadUseNativeControls:!0, features:"playpause,current,progress,duration,tracks,volume,fullscreen".split(",")};
    mejs.mepIndex = 0;
    mejs.MediaElementPlayer = function (a, b) {
        if (!(this instanceof mejs.MediaElementPlayer))return new mejs.MediaElementPlayer(a, b);
        this.options = c.extend({},
                mejs.MepDefaults, b);
        this.$media = this.$node = c(a);
        this.node = this.media = this.$media[0];
        if (typeof this.node.player != "undefined")return this.node.player; else this.node.player = this;
        this.isVideo = this.media.tagName.toLowerCase() === "video";
        this.init();
        return this
    };
    mejs.MediaElementPlayer.prototype = {init:function () {
        var a = this, b = mejs.MediaFeatures, d = c.extend(!0, {}, a.options, {success:function (b, c) {
            a.meReady(b, c)
        }, error:function (b) {
            a.handleError(b)
        }});
        if (b.isiPad && a.options.iPadUseNativeControls || b.isiPhone)a.$media.attr("controls",
                "controls"), a.$media.removeAttr("poster"), b.isiPad && a.media.getAttribute("autoplay") !== null && (a.media.load(), a.media.play()); else if (!b.isAndroid || !a.isVideo)a.$media.removeAttr("controls"), a.id = "mep_" + mejs.mepIndex++, a.container = c('<div id="' + a.id + '" class="mejs-container"><div class="mejs-inner"><div class="mejs-mediaelement"></div><div class="mejs-layers"></div><div class="mejs-controls"></div><div class="mejs-clear"></div></div></div>').addClass(a.$media[0].className).insertBefore(a.$media), a.container.find(".mejs-mediaelement").append(a.$media),
                a.controls = a.container.find(".mejs-controls"), a.layers = a.container.find(".mejs-layers"), a.isVideo ? (a.width = a.options.videoWidth > 0 ? a.options.videoWidth : a.$media[0].getAttribute("width") !== null ? a.$media.attr("width") : a.options.defaultVideoWidth, a.height = a.options.videoHeight > 0 ? a.options.videoHeight : a.$media[0].getAttribute("height") !== null ? a.$media.attr("height") : a.options.defaultVideoHeight) : (a.width = a.options.audioWidth, a.height = a.options.audioHeight), a.setPlayerSize(a.width, a.height), d.pluginWidth =
                a.height, d.pluginHeight = a.width;
        mejs.MediaElement(a.$media[0], d)
    }, meReady:function (a, b) {
        var d = this, e = mejs.MediaFeatures, f;
        if (!d.created) {
            d.created = !0;
            d.media = a;
            d.domNode = b;
            if (!e.isiPhone && !e.isAndroid && (!e.isiPad || !d.options.iPadUseNativeControls)) {
                d.buildposter(d, d.controls, d.layers, d.media);
                d.buildoverlays(d, d.controls, d.layers, d.media);
                d.findTracks();
                for (f in d.options.features)if (e = d.options.features[f], d["build" + e])try {
                    d["build" + e](d, d.controls, d.layers, d.media)
                } catch (g) {
                }
                d.container.trigger("controlsready");
                d.setPlayerSize(d.width, d.height);
                d.setControlsSize();
                d.isVideo && (d.container.bind("mouseenter",
                        function () {
                            d.options.alwaysShowControls || (d.controls.css("visibility", "visible"), d.controls.stop(!0, !0).fadeIn(200))
                        }).bind("mouseleave", function () {
                    !d.media.paused && !d.options.alwaysShowControls && d.controls.stop(!0, !0).fadeOut(200, function () {
                        c(this).css("visibility", "hidden");
                        c(this).css("display", "block")
                    })
                }), d.domNode.getAttribute("autoplay") !== null && !d.options.alwaysShowControls && d.controls.css("visibility",
                        "hidden"), d.options.enableAutosize && d.media.addEventListener("loadedmetadata", function (a) {
                    d.options.videoHeight <= 0 && d.domNode.getAttribute("height") === null && !isNaN(a.target.videoHeight) && (d.setPlayerSize(a.target.videoWidth, a.target.videoHeight), d.setControlsSize(), d.media.setVideoSize(a.target.videoWidth, a.target.videoHeight))
                }, !1));
                d.media.addEventListener("ended", function () {
                    d.media.setCurrentTime(0);
                    d.media.pause();
                    d.setProgressRail && d.setProgressRail();
                    d.setCurrentRail && d.setCurrentRail();
                    d.options.loop ?
                            d.media.play() : d.options.alwaysShowControls || d.controls.css("visibility", "visible")
                }, !0);
                d.media.addEventListener("loadedmetadata", function () {
                    d.updateDuration && d.updateDuration();
                    d.updateCurrent && d.updateCurrent();
                    d.setControlsSize()
                }, !0);
                setTimeout(function () {
                    d.setControlsSize();
                    d.setPlayerSize(d.width, d.height)
                }, 50)
            }
            d.options.success && d.options.success(d.media, d.domNode)
        }
    }, handleError:function (a) {
        this.options.error && this.options.error(a)
    }, setPlayerSize:function (a, b) {
        this.width = parseInt(a, 10);
        this.height =
                parseInt(b, 10);
        this.container.width(this.width).height(this.height);
        this.layers.children(".mejs-layer").width(this.width).height(this.height)
    }, setControlsSize:function () {
        var a = 0, b = 0, d = this.controls.find(".mejs-time-rail"), e = this.controls.find(".mejs-time-total");
        this.controls.find(".mejs-time-current");
        this.controls.find(".mejs-time-loaded");
        others = d.siblings();
        others.each(function () {
            c(this).css("position") != "absolute" && (a += c(this).outerWidth(!0))
        });
        b = this.controls.width() - a - (d.outerWidth(!0) - d.outerWidth(!1));
        d.width(b);
        e.width(b - (e.outerWidth(!0) - e.width()));
        this.setProgressRail && this.setProgressRail();
        this.setCurrentRail && this.setCurrentRail()
    }, buildposter:function (a, b, d, e) {
        var f = c('<div class="mejs-poster mejs-layer"><img /></div>').appendTo(d), b = a.$media.attr("poster"), d = f.find("img").width(a.width).height(a.height);
        a.options.poster != "" ? d.attr("src", a.options.poster) : b !== "" && b != null ? d.attr("src", b) : f.remove();
        e.addEventListener("play", function () {
            f.hide()
        }, !1)
    }, buildoverlays:function (a, b, d, e) {
        if (a.isVideo) {
            var f =
                    c('<div class="mejs-overlay mejs-layer"><div class="mejs-overlay-loading"><span></span></div></div>').hide().appendTo(d), g = c('<div class="mejs-overlay mejs-layer"><div class="mejs-overlay-error"></div></div>').hide().appendTo(d), j = c('<div class="mejs-overlay mejs-layer mejs-overlay-play"><div class="mejs-overlay-button"></div></div>').appendTo(d).click(function () {
                e.paused ? e.play() : e.pause()
            });
            e.addEventListener("play", function () {
                j.hide();
                g.hide()
            }, !1);
            e.addEventListener("pause", function () {
                        j.show()
                    },
                    !1);
            e.addEventListener("loadstart", function () {
                (!mejs.MediaFeatures.isChrome || !(e.getAttribute && e.getAttribute("preload") === "none")) && f.show()
            }, !1);
            e.addEventListener("canplay", function () {
                f.hide()
            }, !1);
            e.addEventListener("error", function () {
                f.hide();
                g.show();
                g.find("mejs-overlay-error").html("Error loading this resource")
            }, !1)
        }
    }, findTracks:function () {
        var a = this, b = a.$media.find("track");
        a.tracks = [];
        b.each(function () {
            a.tracks.push({srclang:c(this).attr("srclang").toLowerCase(), src:c(this).attr("src"),
                kind:c(this).attr("kind"), entries:[], isLoaded:!1})
        })
    }, changeSkin:function (a) {
        this.container[0].className = "mejs-container " + a;
        this.setPlayerSize();
        this.setControlsSize()
    }, play:function () {
        this.media.play()
    }, pause:function () {
        this.media.pause()
    }, load:function () {
        this.media.load()
    }, setMuted:function (a) {
        this.media.setMuted(a)
    }, setCurrentTime:function (a) {
        this.media.setCurrentTime(a)
    }, getCurrentTime:function () {
        return this.media.currentTime
    }, setVolume:function (a) {
        this.media.setVolume(a)
    }, getVolume:function () {
        return this.media.volume
    },
        setSrc:function (a) {
            this.media.setSrc(a)
        }};
    if (typeof jQuery != "undefined")jQuery.fn.mediaelementplayer = function (a) {
        return this.each(function () {
            new mejs.MediaElementPlayer(this, a)
        })
    };
    window.MediaElementPlayer = mejs.MediaElementPlayer
})(mejs.$);
(function (c) {
    MediaElementPlayer.prototype.buildplaypause = function (a, b, d, e) {
        var f = c('<div class="mejs-button mejs-playpause-button mejs-play" type="button"><button type="button"></button></div>').appendTo(b).click(function (a) {
            a.preventDefault();
            e.paused ? e.play() : e.pause();
            return!1
        });
        e.addEventListener("play", function () {
            f.removeClass("mejs-play").addClass("mejs-pause")
        }, !1);
        e.addEventListener("playing", function () {
            f.removeClass("mejs-play").addClass("mejs-pause")
        }, !1);
        e.addEventListener("pause", function () {
                    f.removeClass("mejs-pause").addClass("mejs-play")
                },
                !1);
        e.addEventListener("paused", function () {
            f.removeClass("mejs-pause").addClass("mejs-play")
        }, !1)
    }
})(mejs.$);
(function (c) {
    MediaElementPlayer.prototype.buildstop = function (a, b, d, e) {
        c('<div class="mejs-button mejs-stop-button mejs-stop"><button type="button"></button></div>').appendTo(b).click(function () {
            e.paused || e.pause();
            e.currentTime > 0 && (e.setCurrentTime(0), b.find(".mejs-time-current").width("0px"), b.find(".mejs-time-handle").css("left", "0px"), b.find(".mejs-time-float-current").html(mejs.Utility.secondsToTimeCode(0)), b.find(".mejs-currenttime").html(mejs.Utility.secondsToTimeCode(0)), d.find(".mejs-poster").show())
        })
    }
})(mejs.$);
(function (c) {
    MediaElementPlayer.prototype.buildprogress = function (a, b, d, e) {
        c('<div class="mejs-time-rail"><span class="mejs-time-total"><span class="mejs-time-loaded"></span><span class="mejs-time-current"></span><span class="mejs-time-handle"></span><span class="mejs-time-float"><span class="mejs-time-float-current">00:00</span><span class="mejs-time-float-corner"></span></span></span></div>').appendTo(b);
        var f = b.find(".mejs-time-total"), d = b.find(".mejs-time-loaded"), g = b.find(".mejs-time-current"),
                j = b.find(".mejs-time-handle"), h = b.find(".mejs-time-float"), k = b.find(".mejs-time-float-current"), l = function (a) {
            var a = a.pageX, b = f.offset(), c = f.outerWidth(), d = 0, d = 0;
            a > b.left && a <= c + b.left && e.duration && (d = (a - b.left) / c, d = d <= 0.02 ? 0 : d * e.duration, m && e.setCurrentTime(d), h.css("left", a - b.left), k.html(mejs.Utility.secondsToTimeCode(d)))
        }, m = !1, n = !1;
        f.bind("mousedown", function (a) {
            m = !0;
            l(a);
            return!1
        });
        b.find(".mejs-time-rail").bind("mouseenter",
                function () {
                    n = !0
                }).bind("mouseleave", function () {
            n = !1
        });
        c(document).bind("mouseup",
                function () {
                    m = !1
                }).bind("mousemove", function (a) {
                    (m || n) && l(a)
                });
        e.addEventListener("progress", function (b) {
            a.setProgressRail(b);
            a.setCurrentRail(b)
        }, !1);
        e.addEventListener("timeupdate", function (b) {
            a.setProgressRail(b);
            a.setCurrentRail(b)
        }, !1);
        this.loaded = d;
        this.total = f;
        this.current = g;
        this.handle = j
    };
    MediaElementPlayer.prototype.setProgressRail = function (a) {
        var b = a != void 0 ? a.target : this.media, c = null;
        b && b.buffered && b.buffered.length > 0 && b.buffered.end && b.duration ? c = b.buffered.end(0) / b.duration : b && b.bytesTotal !=
                void 0 && b.bytesTotal > 0 && b.bufferedBytes != void 0 ? c = b.bufferedBytes / b.bytesTotal : a && a.lengthComputable && a.total != 0 && (c = a.loaded / a.total);
        c !== null && (c = Math.min(1, Math.max(0, c)), this.loaded && this.total && this.loaded.width(this.total.width() * c))
    };
    MediaElementPlayer.prototype.setCurrentRail = function () {
        if (this.media.currentTime != void 0 && this.media.duration && this.total && this.handle) {
            var a = this.total.width() * this.media.currentTime / this.media.duration, b = a - this.handle.outerWidth(!0) / 2;
            this.current.width(a);
            this.handle.css("left",
                    b)
        }
    }
})(mejs.$);
(function (c) {
    MediaElementPlayer.prototype.buildcurrent = function (a, b, d, e) {
        c('<div class="mejs-time"><span class="mejs-currenttime">' + (a.options.alwaysShowHours ? "00:" : "") + "00:00</span></div>").appendTo(b);
        this.currenttime = this.controls.find(".mejs-currenttime");
        e.addEventListener("timeupdate", function () {
            a.updateCurrent()
        }, !1)
    };
    MediaElementPlayer.prototype.buildduration = function (a, b, d, e) {
        b.children().last().find(".mejs-currenttime").length > 0 ? c(' <span> | </span> <span class="mejs-duration">' + (a.options.alwaysShowHours ?
                "00:" : "") + "00:00</span>").appendTo(b.find(".mejs-time")) : (b.find(".mejs-currenttime").parent().addClass("mejs-currenttime-container"), c('<div class="mejs-time mejs-duration-container"><span class="mejs-duration">' + (a.options.alwaysShowHours ? "00:" : "") + "00:00</span></div>").appendTo(b));
        this.durationD = this.controls.find(".mejs-duration");
        e.addEventListener("timeupdate", function () {
            a.updateDuration()
        }, !1)
    };
    MediaElementPlayer.prototype.updateCurrent = function () {
        this.currenttime && this.currenttime.html(mejs.Utility.secondsToTimeCode(this.media.currentTime |
                0, this.options.alwaysShowHours || this.media.duration > 3600))
    };
    MediaElementPlayer.prototype.updateDuration = function () {
        this.media.duration && this.durationD && this.durationD.html(mejs.Utility.secondsToTimeCode(this.media.duration, this.options.alwaysShowHours))
    }
})(mejs.$);
(function (c) {
    MediaElementPlayer.prototype.buildvolume = function (a, b, d, e) {
        var f = c('<div class="mejs-button mejs-volume-button mejs-mute"><button type="button"></button><div class="mejs-volume-slider"><div class="mejs-volume-total"></div><div class="mejs-volume-current"></div><div class="mejs-volume-handle"></div></div></div>').appendTo(b), b = f.find(".mejs-volume-slider"), g = f.find(".mejs-volume-total"), j = f.find(".mejs-volume-current"), h = f.find(".mejs-volume-handle"), k = function (a) {
            a = g.height() - g.height() *
                    a;
            h.css("top", a - h.height() / 2);
            j.height(g.height() - a + parseInt(g.css("top").replace(/px/, ""), 10));
            j.css("top", a)
        }, l = function (a) {
            var b = g.height(), c = g.offset(), d = parseInt(g.css("top").replace(/px/, ""), 10), a = a.pageY - c.top, c = (b - a) / b;
            a < 0 ? a = 0 : a > b && (a = b);
            h.css("top", a - h.height() / 2 + d);
            j.height(b - a);
            j.css("top", a + d);
            c == 0 ? (e.setMuted(!0), f.removeClass("mejs-mute").addClass("mejs-unmute")) : (e.setMuted(!1), f.removeClass("mejs-unmute").addClass("mejs-mute"));
            c = Math.max(0, c);
            c = Math.min(c, 1);
            e.setVolume(c)
        }, m = !1;
        b.bind("mousedown", function (a) {
            l(a);
            m = !0;
            return!1
        });
        c(document).bind("mouseup",
                function () {
                    m = !1
                }).bind("mousemove", function (a) {
            m && l(a)
        });
        f.find("button").click(function () {
            e.muted ? (e.setMuted(!1), f.removeClass("mejs-unmute").addClass("mejs-mute"), k(1)) : (e.setMuted(!0), f.removeClass("mejs-mute").addClass("mejs-unmute"), k(0))
        });
        e.addEventListener("volumechange", function (a) {
            m || k(a.target.volume)
        }, !0);
        k(a.options.startVolume);
        e.pluginType === "native" && e.setVolume(a.options.startVolume)
    }
})(mejs.$);
(function (c) {
    mejs.MediaElementDefaults.forcePluginFullScreen = !1;
    MediaElementPlayer.prototype.isFullScreen = !1;
    MediaElementPlayer.prototype.buildfullscreen = function (a, b, d, e) {
        if (a.isVideo) {
            mejs.MediaFeatures.hasNativeFullScreen && a.container.bind("webkitfullscreenchange", function () {
                document.webkitIsFullScreen ? a.setControlsSize() : a.exitFullScreen()
            });
            var f = 0, g = 0, j = a.container, h = document.documentElement, k, l = c('<div class="mejs-button mejs-fullscreen-button"><button type="button"></button></div>').appendTo(b).click(function () {
                (mejs.MediaFeatures.hasNativeFullScreen ?
                        document.webkitIsFullScreen : a.isFullScreen) ? a.exitFullScreen() : a.enterFullScreen()
            });
            a.enterFullScreen = function () {
                a.pluginType !== "native" && (mejs.MediaFeatures.isFirefox || a.options.forcePluginFullScreen) ? e.setFullscreen(!0) : (mejs.MediaFeatures.hasNativeFullScreen && a.container[0].webkitRequestFullScreen(), k = h.style.overflow, h.style.overflow = "hidden", f = a.container.height(), g = a.container.width(), j.addClass("mejs-container-fullscreen").width("100%").height("100%").css("z-index", 1E3), a.pluginType === "native" ?
                        a.$media.width("100%").height("100%") : (j.find("object embed").width("100%").height("100%"), a.media.setVideoSize(c(window).width(), c(window).height())), d.children("div").width("100%").height("100%"), l.removeClass("mejs-fullscreen").addClass("mejs-unfullscreen"), a.setControlsSize(), a.isFullScreen = !0)
            };
            a.exitFullScreen = function () {
                a.pluginType !== "native" && mejs.MediaFeatures.isFirefox ? e.setFullscreen(!1) : (mejs.MediaFeatures.hasNativeFullScreen && document.webkitIsFullScreen && document.webkitCancelFullScreen(),
                        h.style.overflow = k, j.removeClass("mejs-container-fullscreen").width(g).height(f).css("z-index", 1), a.pluginType === "native" ? a.$media.width(g).height(f) : (j.find("object embed").width(g).height(f), a.media.setVideoSize(g, f)), d.children("div").width(g).height(f), l.removeClass("mejs-unfullscreen").addClass("mejs-fullscreen"), a.setControlsSize(), a.isFullScreen = !1)
            };
            c(window).bind("resize", function () {
                a.setControlsSize()
            });
            c(document).bind("keydown", function (b) {
                a.isFullScreen && b.keyCode == 27 && a.exitFullScreen()
            })
        }
    }
})(mejs.$);
(function (c) {
    c.extend(mejs.MepDefaults, {startLanguage:"", translations:[], translationSelector:!1, googleApiKey:""});
    c.extend(MediaElementPlayer.prototype, {buildtracks:function (a, b, d, e) {
        if (a.isVideo && a.tracks.length != 0) {
            var f, g = "";
            a.chapters = c('<div class="mejs-chapters mejs-layer"></div>').prependTo(d).hide();
            a.captions = c('<div class="mejs-captions-layer mejs-layer"><div class="mejs-captions-position"><span class="mejs-captions-text"></span></div></div>').prependTo(d).hide();
            a.captionsText = a.captions.find(".mejs-captions-text");
            a.captionsButton = c('<div class="mejs-button mejs-captions-button"><button type="button" ></button><div class="mejs-captions-selector"><ul><li><input type="radio" name="' + a.id + '_captions" id="' + a.id + '_captions_none" value="none" checked="checked" /><label for="' + a.id + '_captions_none">None</label></li></ul></div></button>').appendTo(b).delegate("input[type=radio]", "click", function () {
                lang = this.value;
                if (lang == "none")a.selectedTrack = null; else for (f = 0; f < a.tracks.length; f++)if (a.tracks[f].srclang == lang) {
                    a.selectedTrack =
                            a.tracks[f];
                    a.captions.attr("lang", a.selectedTrack.srclang);
                    a.displayCaptions();
                    break
                }
            });
            a.options.alwaysShowControls ? a.container.find(".mejs-captions-position").addClass("mejs-captions-position-hover") : a.container.bind("mouseenter",
                    function () {
                        a.container.find(".mejs-captions-position").addClass("mejs-captions-position-hover")
                    }).bind("mouseleave", function () {
                e.paused || a.container.find(".mejs-captions-position").removeClass("mejs-captions-position-hover")
            });
            a.trackToLoad = -1;
            a.selectedTrack = null;
            a.isLoadingTrack =
                    !1;
            if (a.tracks.length > 0 && a.options.translations.length > 0)for (f = 0; f < a.options.translations.length; f++)a.tracks.push({srclang:a.options.translations[f].toLowerCase(), src:null, kind:"subtitles", entries:[], isLoaded:!1, isTranslation:!0});
            for (f = 0; f < a.tracks.length; f++)a.tracks[f].kind == "subtitles" && a.addTrackButton(a.tracks[f].srclang, a.tracks[f].isTranslation);
            a.loadNextTrack();
            e.addEventListener("timeupdate", function () {
                a.displayCaptions()
            }, !1);
            e.addEventListener("loadedmetadata", function () {
                        a.displayChapters()
                    },
                    !1);
            a.container.hover(function () {
                a.chapters.css("visibility", "visible");
                a.chapters.fadeIn(200)
            }, function () {
                e.paused || a.chapters.fadeOut(200, function () {
                    c(this).css("visibility", "hidden");
                    c(this).css("display", "block")
                })
            });
            a.node.getAttribute("autoplay") !== null && a.chapters.css("visibility", "hidden");
            if (a.options.translationSelector) {
                for (f in mejs.language.codes)g += '<option value="' + f + '">' + mejs.language.codes[f] + "</option>";
                a.container.find(".mejs-captions-selector ul").before(c('<select class="mejs-captions-translations"><option value="">--Add Translation--</option>' +
                        g + "</select>"));
                a.container.find(".mejs-captions-translations").change(function () {
                    lang = c(this).val();
                    if (lang != "" && (a.tracks.push({srclang:lang, src:null, entries:[], isLoaded:!1, isTranslation:!0}), !a.isLoadingTrack))a.trackToLoad--, a.addTrackButton(lang, !0), a.options.startLanguage = lang, a.loadNextTrack()
                })
            }
        }
    }, loadNextTrack:function () {
        this.trackToLoad++;
        this.trackToLoad < this.tracks.length ? (this.isLoadingTrack = !0, this.loadTrack(this.trackToLoad)) : this.isLoadingTrack = !1
    }, loadTrack:function (a) {
        var b = this,
                d = b.tracks[a], e = function () {
            d.isLoaded = !0;
            b.enableTrackButton(d.srclang);
            b.loadNextTrack()
        };
        d.isTranslation ? mejs.TrackFormatParser.translateTrackText(b.tracks[0].entries, b.tracks[0].srclang, d.srclang, b.options.googleApiKey, function (a) {
            d.entries = a;
            e()
        }) : c.ajax({url:d.src, success:function (a) {
            d.entries = mejs.TrackFormatParser.parse(a);
            e();
            d.kind == "chapters" && b.media.duration > 0 && b.drawChapters(d)
        }, error:function () {
            b.loadNextTrack()
        }})
    }, enableTrackButton:function (a) {
        this.captionsButton.find("input[value=" +
                a + "]").prop("disabled", !1).siblings("label").html(mejs.language.codes[a] || a);
        this.options.startLanguage == a && c("#" + this.id + "_captions_" + a).click();
        this.adjustLanguageBox()
    }, addTrackButton:function (a, b) {
        var d = mejs.language.codes[a] || a;
        this.captionsButton.find("ul").append(c('<li><input type="radio" name="' + this.id + '_captions" id="' + this.id + "_captions_" + a + '" value="' + a + '" disabled="disabled" /><label for="' + this.id + "_captions_" + a + '">' + d + (b ? " (translating)" : " (loading)") + "</label></li>"));
        this.adjustLanguageBox();
        this.container.find(".mejs-captions-translations option[value=" + a + "]").remove()
    }, adjustLanguageBox:function () {
        this.captionsButton.find(".mejs-captions-selector").height(this.captionsButton.find(".mejs-captions-selector ul").outerHeight(!0) + this.captionsButton.find(".mejs-captions-translations").outerHeight(!0))
    }, displayCaptions:function () {
        if (typeof this.tracks != "undefined") {
            var a, b = this.selectedTrack;
            if (b != null && b.isLoaded)for (a = 0; a < b.entries.times.length; a++)if (this.media.currentTime >= b.entries.times[a].start &&
                    this.media.currentTime <= b.entries.times[a].stop) {
                this.captionsText.html(b.entries.text[a]);
                this.captions.show();
                return
            }
            this.captions.hide()
        }
    }, displayChapters:function () {
        var a;
        for (a = 0; a < this.tracks.length; a++)if (this.tracks[a].kind == "chapters" && this.tracks[a].isLoaded) {
            this.drawChapters(this.tracks[a]);
            break
        }
    }, drawChapters:function (a) {
        var b = this, d, e, f = e = 0;
        b.chapters.empty();
        for (d = 0; d < a.entries.times.length; d++) {
            e = a.entries.times[d].stop - a.entries.times[d].start;
            e = Math.floor(e / b.media.duration * 100);
            if (e + f > 100 || d == a.entries.times.length - 1 && e + f < 100)e = 100 - f;
            b.chapters.append(c('<div class="mejs-chapter" rel="' + a.entries.times[d].start + '" style="left: ' + f.toString() + "%;width: " + e.toString() + '%;"><div class="mejs-chapter-block' + (d == a.entries.times.length - 1 ? " mejs-chapter-block-last" : "") + '"><span class="ch-title">' + a.entries.text[d] + '</span><span class="ch-time">' + mejs.Utility.secondsToTimeCode(a.entries.times[d].start) + "&ndash;" + mejs.Utility.secondsToTimeCode(a.entries.times[d].stop) + "</span></div></div>"));
            f += e
        }
        b.chapters.find("div.mejs-chapter").click(function () {
            b.media.setCurrentTime(parseFloat(c(this).attr("rel")));
            b.media.paused && b.media.play()
        });
        b.chapters.show()
    }});
    mejs.language = {codes:{af:"Afrikaans", sq:"Albanian", ar:"Arabic", be:"Belarusian", bg:"Bulgarian", ca:"Catalan", zh:"Chinese", "zh-cn":"Chinese Simplified", "zh-tw":"Chinese Traditional", hr:"Croatian", cs:"Czech", da:"Danish", nl:"Dutch", en:"English", et:"Estonian", tl:"Filipino", fi:"Finnish", fr:"French", gl:"Galician", de:"German", el:"Greek", ht:"Haitian Creole",
        iw:"Hebrew", hi:"Hindi", hu:"Hungarian", is:"Icelandic", id:"Indonesian", ga:"Irish", it:"Italian", ja:"Japanese", ko:"Korean", lv:"Latvian", lt:"Lithuanian", mk:"Macedonian", ms:"Malay", mt:"Maltese", no:"Norwegian", fa:"Persian", pl:"Polish", pt:"Portuguese", ro:"Romanian", ru:"Russian", sr:"Serbian", sk:"Slovak", sl:"Slovenian", es:"Spanish", sw:"Swahili", sv:"Swedish", tl:"Tagalog", th:"Thai", tr:"Turkish", uk:"Ukrainian", vi:"Vietnamese", cy:"Welsh", yi:"Yiddish"}};
    mejs.TrackFormatParser = {pattern_identifier:/^[0-9]+$/, pattern_timecode:/^([0-9]{2}:[0-9]{2}:[0-9]{2}(,[0-9]{1,3})?) --\> ([0-9]{2}:[0-9]{2}:[0-9]{2}(,[0-9]{3})?)(.*)$/,
        split2:function (a, b) {
            return a.split(b)
        }, parse:function (a) {
            for (var b = 0, a = this.split2(a, /\r?\n/), c = {text:[], times:[]}, e, f; b < a.length; b++)if (this.pattern_identifier.exec(a[b]) && (b++, (e = this.pattern_timecode.exec(a[b])) && b < a.length)) {
                b++;
                f = a[b];
                for (b++; a[b] !== "" && b < a.length;)f = f + "\n" + a[b], b++;
                c.text.push(f);
                c.times.push({start:mejs.Utility.timeCodeToSeconds(e[1]), stop:mejs.Utility.timeCodeToSeconds(e[3]), settings:e[5]})
            }
            return c
        }, translateTrackText:function (a, b, c, e, f) {
            var g = {text:[], times:[]}, j, h;
            this.translateText(a.text.join(" <a></a>"),
                    b, c, e, function (b) {
                        j = b.split("<a></a>");
                        for (h = 0; h < a.text.length; h++)g.text[h] = j[h], g.times[h] = {start:a.times[h].start, stop:a.times[h].stop, settings:a.times[h].settings};
                        f(g)
                    })
        }, translateText:function (a, b, c, e, f) {
            for (var g, j = [], h, k = "", l = function () {
                j.length > 0 ? (h = j.shift(), mejs.TrackFormatParser.translateChunk(h, b, c, e, function (a) {
                    a != "undefined" && (k += a);
                    l()
                })) : f(k)
            }; a.length > 0;)a.length > 1E3 ? (g = a.lastIndexOf(".", 1E3), j.push(a.substring(0, g)), a = a.substring(g + 1)) : (j.push(a), a = "");
            l()
        }, translateChunk:function (a, b, d, e, f) {
            a = {q:a, langpair:b + "|" + d, v:"1.0"};
            if (e !== "" && e !== null)a.key = e;
            c.ajax({url:"https://ajax.googleapis.com/ajax/services/language/translate", data:a, type:"GET", dataType:"jsonp", success:function (a) {
                f(a.responseData.translatedText)
            }, error:function () {
                f(null)
            }})
        }};
    if ("x\n\ny".split(/\n/gi).length != 3)mejs.TrackFormatParser.split2 = function (a, b) {
        var c = [], e = "", f;
        for (f = 0; f < a.length; f++)e += a.substring(f, f + 1), b.test(e) && (c.push(e.replace(b, "")), e = "");
        c.push(e);
        return c
    }
})(mejs.$);
