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
      var playerID = getCookie("playerID");

      var socket = new WebSocket("ws://"+ window.location.host + "/game");

      socket.onopen = function() {
        console.log("connection is open!");
        socket.onmessage = function(msg) {
          console.log("received : " + msg.data);
        };

        socket.onclose = function(arg) {
          console.log("connection error");
        };

        socket.onerror = function (arg) {
          console.log("connection error");
        };
      };


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
