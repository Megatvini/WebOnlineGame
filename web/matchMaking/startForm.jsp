<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fieldset class="innerFieldset">
    <legend>მონიშნე ოთახის ზომები</legend>
    <label for="checkBox1">
        <input type="checkbox" name="roomsize2" id="checkBox1" onclick="updateButton()"><span>2 Player Room</span>
    </label>

    <label for="checkBox2">
        <input type="checkbox" name="roomsize3" id="checkBox2" onclick="updateButton()"><span>3 Player Room</span>
    </label>

    <label for="checkBox3">
        <input type="checkbox" name="roomsize4" id="checkBox3" onclick="updateButton()"><span>4 Player Room</span>
    </label>
</fieldset>
<br>
<button style="margin-left:20%; margin-right:20%; width:60%;" type="submit"
        class="btn btn-block btn-primary btn-lg" id = "button" disabled>დაიწყე თამაში</button>
<br>