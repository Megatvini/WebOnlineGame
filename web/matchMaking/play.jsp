<!doctype html>
<html>
<head>
    <link href="/matchMaking/css/buttons.css" rel="stylesheet">
    <%@include file="userInGame.jsp" %>
</head>
<body>
<fieldset>
    <legend>PLAY!</legend>
    <div align="middle">
        <form action="/CreateRoom" method="post">
            <a class="xlarge blue button" href="/matchMaking/quickplay.jsp">Quick Play </a>
            <button type="submit" class="xlarge blue button">New Room</button>
        </form>
    </div>
</fieldset>
</body>
</html>