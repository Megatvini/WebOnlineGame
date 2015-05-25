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
      var socket = new WebSocket("ws://192.168.77.11:8080/app");
      socket.onmessage = function(msg) {
        clear();
        var points = msg.data.split("#");
        var i=0;
        for (; i<points.length; i++) {
          var ar = points[i].split(":");
          circle(Number(ar[0]), Number(ar[1]), 10);
        }
      };

      socket.onclose = function(arg) {
        console.log(arg.data);
      };

      socket.onerror = function (arg) {
        console.log(arg.data);
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
        var keyCode = evt.keyCode;
        if (keyCode >=37 && keyCode <=40) {
          try {
            socket.send(keyCode.toString());
          } catch (e) {
            console.log(e.data);
          }
        }
      }

      window.addEventListener('keydown',doKeyDown,true);
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
