<%@ page import="java.util.Set" %>
<%@ page import="Core.Dao.FriendsDao" %>
<%@ page import="Interfaces.iAccount" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<% Set<String> onlineFriends = (Set<String>) application.getAttribute("onlineUsers");
    String nickName = (String) session.getAttribute("nickname");
    AccountDao accountDao = (AccountDao) application.getAttribute(AccountDao.class.getName());
    FriendsDao friendsDao = (FriendsDao) application.getAttribute(FriendsDao.class.getName());
    iAccount account = null;
    try {
        account = accountDao.getUser(nickName);
    } catch (Exception e) {
        response.sendRedirect("Accont/Login.jsp");
        return;
    }
    Set<String> friends = friendsDao.getFriendNamesByID(account.getID());
%>
<fieldset class="innerFieldset" style="width: 325px; float: left; margin-left: 15px;">
    <legend>მონიშნე ოთახის ზომები</legend>
    <label for="checkBox1">
        <input type="checkbox" name="roomsize2" id="checkBox1" onclick="updateButton()"><span>2 Player Room</span>
    </label>

    <label for="checkBox2">
        <input type="checkbox" name="roomsize3" id="checkBox2" onclick="updateButton()"><span>3 Player Room</span>
    </label>

    <label for="checkBox3">
        <input type="checkbox" name="roomsize4" id="checkBox3" onclick="updateButton()"><span>4 Player Room</span>
    </label></fieldset>
    <button style="margin-left:34%; margin-right:20%; width:60%;" type="submit"
            class="btn btn-block btn-primary btn-lg" id = "button" disabled>დაიწყე თამაში</button>
<div class="col-md-6" style="float: right; margin-right: 50px; width: 664;">
    <!-- USERS LIST -->
    <div class="box box-danger">
        <div class="box-header with-border">

        </div><!-- /.box-header -->
        <div class="box-body no-padding">
            <ul class="users-list clearfix">
                <%
                    for (String friend : friends) {
                %>
                <li>
                    <img src="default.png" data-path ="<%=friend%>" alt="User Image">
                    <a class="users-list-name" href="#"><%=friend%></a>
                    <span class="users-list-date"></span>
                </li>
                <%}%>
            </ul><!-- /.users-list -->
        </div><!-- /.box-body -->
        <div class="box-footer text-center">
            <a href="javascript:" class="uppercase">View All Users</a>
        </div><!-- /.box-footer -->
    </div><!--/.box -->
</div>
<script src="../assignPath.js"></script>
<script>
    function check() {
        $.get('StartingGroupService', function(resp) {
            //console.log(resp);
            var arr = resp.replace("[","").replace("]","").split(',');
            var i=0;
            for (; i<arr.length; i++) {
                var s = "#p".concat(i+1);
                $(s).val(arr[i]);
                $("#pic".concat(i+1)).attr("src", 'http://' + window.location.host + '/images?nickname=' + arr[i]);
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
                $( "#checkBox1").prop( "checked", false);
                $( "#checkBox2").prop( "disabled", true);
                $( "#checkBox2").prop( "checked", false);
                break;
            case 1:
                $( "#checkBox").prop( "disabled", true);
                $( "#checkBox").prop( "checked", false);
                break;
        }

        //console.log(count);
    }
    setInterval(check, 1000);

    function updateButton() {
        if ($("#button")) {
            var count = $("[type='checkbox']:checked").length;
            console.log(count);
            if (count == 0) $("#button").prop("disabled", true);
            else $("#button").prop("disabled", false);
        }
    }
</script>
<br>
<br>
<br>
<br>
<br>
<br>