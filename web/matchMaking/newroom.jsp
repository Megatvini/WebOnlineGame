<%@ page import="MatchMaking.StartingGroup" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Choose Number Of Players</title>
    <link href="matchMaking/css/buttons.css" rel="stylesheet">
    <script src="jquery-1.9.1.js"></script>
</head>
<body>
<form action="/StartGame" method="get">
    <fieldset>
        <legend>Choose Room Size</legend>

        <label for="checkBox1">
            <input type="checkbox" name="roomsize2" id="checkBox1"><span>2 Player Room</span>
        </label>

        <label for="checkBox2">
            <input type="checkbox" name="roomsize3" id="checkBox2"><span>3 Player Room</span>
        </label>

        <label for="checkBox3">
            <input type="checkbox" name="roomsize4" id="checkBox3"><span>4 Player Room</span>
        </label>
        <br>

        <fieldset>
            <legend>Player 1</legend>
            <input name="p1" id="p1" value="Empty" style="border:none" readonly>
        </fieldset>
        <br>

        <fieldset>
            <legend>Player 2</legend>
            <input name="p2" id="p2" value="Empty" style="border:none" readonly>
        </fieldset>
        <br>

        <fieldset>
            <legend>Player 3</legend>
            <input name="p3" id="p3" value="Empty" style="border:none" readonly>
        </fieldset>
        <br>

        <fieldset>
            <legend>Player 4</legend>
            <input name="p4" id="p4" value="Empty" style="border:none" readonly>
        </fieldset>
        <br>
        <%
            String userName = (String) session.getAttribute("userName");
            Map<String, StartingGroup> groupMap = (Map<String, StartingGroup>)
                    session.getServletContext().getAttribute(StartingGroup.class.getName());
            StartingGroup group = groupMap.get(userName);
            if (group == null) request.getRequestDispatcher("matchMaking/play.jsp").forward(request, response);
            else {
                if (group.getCreator().equals(userName))
                    out.print("        <button style=\"margin-left:20%; margin-right:20%; width:60%;\" " +
                            "type=\"submit\" " +
                            "class=\"xlarge blue button\">START GAME</button>\n");
            }
        %>
    </fieldset>
</form>
<script>
    function check() {
        $.get('StartingGroupService', function(resp) {
            //console.log(resp);
            var arr = resp.replace("[","").replace("]","").split(',');
            var i=0;
            for (; i<arr.length; i++) {
                var s = "#p".concat(i+1);
                $(s).val(arr[i]);
            }

            for (; i<4; i++) {
                var s = "#p".concat(i+1);
                $(s).val("Empty");
            }
            onChange();
        });
    }

    function onChange() {
        var count = 0;
        var i = 1;
        for (; i<=4; i++) {
            if ($("#p"+i).val() == "Empty") count++;
        }

        i = 1;
        for (; i<4; i++) {
            $( "#checkBox"+i).prop( "disabled", false);
        }

        switch (count) {
            case 0:
                $( "#checkBox1").prop( "disabled", true);
                $( "#checkBox2").prop( "disabled", true);
                break;
            case 1:
                $( "#checkBox").prop( "disabled", true);
                break;
        }

        //console.log(count);
    }
    setInterval(check, 1000);
</script>
</body>
</html>