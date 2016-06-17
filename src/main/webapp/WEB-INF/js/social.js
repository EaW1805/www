/**
 * Using this ajax function you can post to a provider.
 *
 * @param provider The provider you want to post(twit, etc).
 * @param id The id of the entry that contains the value text you want to post.
 *          This id also shows the container where the reply message will appear.
 */
function postToProvider(provider, id) {
    var valueEl = document.getElementById("entry" + id);
    var statusEl = document.getElementById("responseStatus" + id);
    var gameEl = document.getElementById("gameId");
    var nationEl = document.getElementById("nationId" + id);
    var newEl = document.getElementById("newId" + id);
    var xmlhttp;
    if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    }
    else {// code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            if (xmlhttp.responseText == 'ok') {
                statusEl.innerHTML = "posted!";
            } else if (xmlhttp.responseText.indexOf("connect_with_twitter") != -1) {
                document.getElementById('connectToTwitter').submit();
            } else if (xmlhttp.responseText.indexOf('connect_with_facebook') != -1) {
                document.getElementById('connectToFacebook').submit();
            } else {
                statusEl.innerHTML = "fail..";
            }
        }
    };
    var gameId = 0;
    if (gameEl != null) {
        gameId = gameEl.value;
    }

    var newId = 0;
    if (newEl != null) {
        newId = newEl.value;
    }
    var nationId = 0;
    if (nationEl != null) {
        nationId = nationEl.value;
    }

    statusEl.innerHTML = "wait..";
    var startsWith = "/";
    if (getDomainName() == "localhost") {
        startsWith = startsWith + "empire-web/";
    }
    var urlToCall = startsWith + "1805/social/" + provider + "/feed";
//    alert(urlToCall);
    xmlhttp.open("POST", urlToCall, true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.send("message=" + valueEl.value + "&gameId=" + gameId + "&newId=" + newId + "&nationId=" + nationId);
}

/**
 * Returns the domain name.
 * Needs to determine
 * if we are localhost or public.
 */
function getDomainName() {
    return window.location.hostname;
}

