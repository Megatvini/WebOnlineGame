<%--
  Created by IntelliJ IDEA.
  User: Nika
  Date: 5/24/2015
  Time: 12:27
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>Canvas Test</title>
  <script src="jquery-1.9.1.js"></script>
  <script type="text/javascript">
    $(document).ready(function() {
      console.log( "ready!" );
      var canvas = document.getElementById("canvas");
      var ctx = canvas.getContext("2d");
      var WIDTH = 300;
      var HEIGHT = 200;
      var playerID = getCookie("playerID")
      var socket = new WebSocket("ws://"+ window.location.host + "/game");

      socket.onopen = function() {
        console.log("connection is open!");
        socket.send(playerID + ":" + "init");
        window.addEventListener('keydown',doKeyDown,true);
      };

      socket.onmessage = function(msg) {
        console.log("received : " + msg.data);
        clear();
        var points = msg.data.split("#");
        var i=0;
        for (; i<points.length; i++) {
          var ar = points[i].split(":");
          circle(Number(ar[0]), Number(ar[1]), 10);
        }
      };

      socket.onclose = function(arg) {
        console.log("connection error");
      };

      socket.onerror = function (arg) {
        console.log("connection error");
      };

      function circle(x,y,r) {
        ctx.beginPath();
        ctx.arc(x, y, r, 0, Math.PI*2, true);
        ctx.fill();
      }

      function clear() {
        ctx.clearRect(0, 0, WIDTH, HEIGHT);
      }

      function doKeyDown(evt){
        if (socket.readyState != WebSocket.OPEN) {
          console.log("socket is not ready: " + socket.readyState);
          return;
        }
        var keyCode = evt.keyCode;
        if (keyCode >=37 && keyCode <=40) {
          try {
            var toSend = playerID + ":" + "update";
            console.log("trying to send: " + toSend);
            socket.send(toSend);
          } catch (e) {
            console.log("Could not send data :" + e.data);
          }
        }
      }

      function getCookie(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for(var i=0; i<ca.length; i++) {
          var c = ca[i];
          while (c.charAt(0)==' ') c = c.substring(1);
          if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
        }
        return "";
      }

    });
  </script>
</head>
<body>
<section>
  <div>
    <canvas id="canvas" width="300" height="200" style="border:3px solid #11ff00;">
      This text is displayed if your browser does not support HTML5 Canvas.
    </canvas>
  </div>
</section>
</body>
</html>
