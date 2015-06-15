<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Choose Number Of Players</title>
    <link href="css/buttons.css" rel="stylesheet">
    <script src="/jquery-1.9.1.js"></script>
</head>
<body>
<form action="/QuickPlay" method="get">
<fieldset>
    <legend>Quick Play</legend>
    <label for="checkBox1">
        <input type="checkbox" name="roomsize2" id="checkBox1" onclick="updateButton()"><span>2 Player Room</span>
    </label>

    <label for="checkBox2">
        <input type="checkbox" name="roomsize3" id="checkBox2" onclick="updateButton()"><span>3 Player Room</span>
    </label>

    <label for="checkBox3">
        <input type="checkbox" name="roomsize4" id="checkBox3" onclick="updateButton()"><span>4 Player Room</span>
    </label>

    <br>
    <button type="submit" class="xlarge blue button" disabled id="button">Quick Play</button>
</fieldset>
</form>
<script>
    function updateButton() {
        if ($("#button")) {
            var count = $("[type='checkbox']:checked").length;
            if (count == 0) $("#button").prop("disabled", true);
            else $("#button").prop("disabled", false);
        }
    }
</script>
</body>
</html>