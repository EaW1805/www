<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>


<script type="text/javascript" src='<c:url value="/js/gwtexternal/jquery.atmosphere.js"/>'></script>
<script type="text/javascript">
    $(function () {
        "use strict";
        var url = 'http://www.eaw1805.com/chat';
        if (document.location.toString().indexOf('localhost') > 0) {
            url = 'ws://5.9.130.194:8098/chateaw/websocket';
        }
        var chatContent = $('#chatContent');
        var input = $('#input');
        var status = $('#status');
        var myName = false;
        var author = null;
        var logged = false;
        var socket = $.atmosphere;
        var request = { url:url,
            contentType:"application/json",
            logLevel:'debug',
            transport:'websocket',
            fallbackTransport:'long-polling'};


        request.onOpen = function (response) {
            chatContent.html($('<p>', { text:'Atmosphere connected using ' + response.transport }));
            input.removeAttr('disabled').focus();
            status.text('Choose name:');
        };

        request.onReconnect = function (request, response) {
            socket.info("Reconnecting")
        };

        request.onMessage = function (response) {
            var message = response.responseBody;

            try {
                var json = JSON.parse(message);
            } catch (e) {
                console.log('This doesn\'t look like a valid JSON: ', message.data);
                return;
            }

            if (!logged) {
                logged = true;
                status.text(myName + ': ').css('color', 'blue');
                input.removeAttr('disabled').focus();
            } else {
                input.removeAttr('disabled');

                var me = json.author == author;
                var date = typeof(json.time) == 'string' ? parseInt(json.time) : json.time;
                addMessage(json.author, json.text, me ? 'blue' : 'black', new Date());
            }
        };

        request.onError = function (response) {
            chatContent.html($('<p>', { text:'Sorry, but there\'s some problem with your '
                    + 'socket or the server is down' }));
        };

        var subSocket = socket.subscribe(request);

        input.keydown(function (e) {
            if (e.keyCode === 13) {
                var msg = $(this).val();

                // First message is always the author's name
                if (author == null) {
                    author = msg;
                }

                subSocket.push(JSON.stringify({ author:author, message:msg }));
                $(this).val('');

                input.attr('disabled', 'disabled');
                if (myName === false) {
                    myName = msg;
                }
            }
        });

        function addMessage(author, message, color, datetime) {
            chatContent.append('<p><span style="color:' + color + '">' + author + '</span> @ ' +
                    +(datetime.getHours() < 10 ? '0' + datetime.getHours() : datetime.getHours()) + ':'
                    + (datetime.getMinutes() < 10 ? '0' + datetime.getMinutes() : datetime.getMinutes())
                    + ': ' + message + '</p>');
        }
    });


</script>

<html>

<head>
    <meta charset="utf-8">
    <title>Atmosphere Chat</title>

    <style>
        * {
            font-family: tahoma;
            font-size: 12px;
            padding: 0px;
            margin: 0px;
        }

        p {
            line-height: 18px;
        }

        div {
            width: 500px;
            margin-left: auto;
            margin-right: auto;
        }

        #chatContent {
            padding: 5px;
            background: #ddd;
            border-radius: 5px;
            border: 1px solid #CCC;
            margin-top: 10px;
            height: 500px;
        }

        #header {
            padding: 5px;
            background: #f5deb3;
            border-radius: 5px;
            border: 1px solid #CCC;
            margin-top: 10px;
        }

        #input {
            border-radius: 2px;
            border: 1px solid #ccc;
            margin-top: 10px;
            padding: 5px;
            width: 400px;
        }

        #status {
            width: 88px;
            display: block;
            float: left;
            margin-top: 15px;
            height: 50px;
        }
    </style>
</head>
<body>
<h3>Atmosphere Chat. Default transport is WebSocket, fallback is long-polling</h3>

<div style="height:1000px;">
    <div id="chatContent"></div>
    <div>
        <span id="status">Connecting...</span>
        <input type="text" id="input" disabled="disabled"/>
    </div>
</div>

</body>
</html>