<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Plese wait...</title>
    <link href="/matchMaking/css/loading.css" rel="stylesheet">
    <script src="/jquery-1.9.1.js"></script>
</head>
<body>
<ul class="loader">
    <li>
        <div class="circle"></div>
        <div class="ball"></div>
    </li>
    <li>
        <div class="circle"></div>
        <div class="ball"></div>
    </li>
    <li>
        <div class="circle"></div>
        <div class="ball"></div>
    </li>
    <li>
        <div class="circle"></div>
        <div class="ball"></div>
    </li>
    <li>
        <div class="circle"></div>
        <div class="ball"></div>
    </li>
</ul>
<script>
    function check() {
        $.get("http://"+window.location.host + "/Loader", function(resp) {
            console.log(resp);
            if (resp.localeCompare("true") == 0) window.location.replace("/fourColors.html");
        });
    }
    check();
    setInterval(check, 1000);
</script>
</body>
</html>