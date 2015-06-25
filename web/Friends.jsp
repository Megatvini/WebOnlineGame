
<%@ page import="Core.Model.Bean.Account" %>
<%@ page import="Interfaces.iProfile" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
<%--
  Created by IntelliJ IDEA.
  User: gukam
  Date: 5/24/2015
  Time: 3:31 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>AdminLTE 2 | Dashboard</title>
  <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
  <!-- Bootstrap 3.3.4 -->
  <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
  <!-- FontAwesome 4.3.0 -->
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
  <!-- Ionicons 2.0.0 -->
  <link href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
  <!-- Theme style -->
  <link href="dist/css/AdminLTE.min.css" rel="stylesheet" type="text/css" />
  <!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
  <link href="dist/css/skins/_all-skins.min.css" rel="stylesheet" type="text/css" />
  <!-- iCheck -->
  <link href="plugins/iCheck/flat/blue.css" rel="stylesheet" type="text/css" />
  <!-- Morris chart -->
  <link href="plugins/morris/morris.css" rel="stylesheet" type="text/css" />
  <!-- jvectormap -->
  <link href="plugins/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
  <!-- Date Picker -->
  <link href="plugins/datepicker/datepicker3.css" rel="stylesheet" type="text/css" />
  <!-- Daterange picker -->
  <link href="plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
  <!-- bootstrap wysihtml5 - text editor -->
  <link href="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>
<body class="skin-blue sidebar-mini layout-boxed">
<div class="wrapper">
  <%
//    String nickname = (String)session.getAttribute("nickname");
//
//    Set<Integer> waitingFriends = new HashSet<Integer>();
//    Set<Integer> friends = new HashSet<Integer>();
//
//    iProfile profile;
//    if(nickname == null) {
//      String redirectURL = "Accont/Login.jsp";
//      response.sendRedirect(redirectURL);
//      profile = new Account();
//    }
//    else
//    {
//      profile = userControl.getUser(nickname);
//
//      waitingFriends = userControl.getWaitingFriends(profile.getID());
//      friends = userControl.getFriends(profile.getID());
//    }
    String nickname = "nika";
    iProfile shortProf = new Account();
    Set<iProfile> waitingFriends = new HashSet<>();
    Set<iProfile> Friends = new HashSet<>();
  %>
  <jsp:include page="Controller/Header.jsp" flush="true"></jsp:include>
  <jsp:include page="Controller/Sidebar.jsp" flush="true"></jsp:include>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper" style="padding: 1px;">
    <div style="background-color: #0063dc; margin: 30px" >
      <% for (iProfile friend : waitingFriends) {
        //iShorProfile shortProf = userControl.getUser(id);
      %>
      <div style="background-color: #B0EDFF; width: 49%; float: left; padding: 5px 5px 5px 20px; border: groove #010046 thin">
        <img src="<%= shortProf.getPicturePath() %>"  alt="Smiley face" style="width: 100px; height: 100px; border-radius: 50%; float: left">
        <div style="padding: 20px 5px 5px 5px; ">
          <div style="font-size: 22px; text-align: left; padding-left: 110px">
            <%=shortProf.getNickname()%>
          </div>
          <form action="/AcceptFriend" method="post">
            <input type="hidden" name="id1" value="<%= nickname %>">
            <input type="hidden" name="id2" value="<%= shortProf.getID() %>">
            <div style="width:  120px; float: left; padding: 5px"> <button class="btn btn-block btn-primary">დათანხმება</button></div>
            <div style="width:  120px; float: left; padding: 5px"> <button class="btn btn-block btn-primary">უარყოფა</button></div>
          </form>
        </div>
      </div>
      <% } %>
    </div>


    <div style="background-color: #0063dc; margin: 30px" >
      <% for (iProfile friend: Friends) {
        //iShorProfile shortProf = userControl.getUser(id);

      %>
      <div style="background-color: #B0EDFF; width: 49%; float: left; padding: 5px 5px 5px 20px; border: groove #010046 thin">
        <img src="<%= shortProf.getPicturePath() %>"  alt="Smiley face" style="width: 100px; height: 100px; border-radius: 50%; float: left">
        <div style="padding: 20px 5px 5px 5px; ">
          <div style="font-size: 22px; text-align: left; padding-left: 110px">
            <%=shortProf.getNickname()%>
          </div>
          <div style="width:  100px; float: left; padding: 5px"> <button class="btn btn-block btn-primary">წაშლა</button></div>          
          <div style="width:  100px; float: left; padding: 5px"><a href="Messages.jsp?friend=<%=shortProf.getNickname()%>"><button class="btn btn-block btn-primary">მიწერა</button></a></div>
        </div>
      </div>

      <% } %>
    </div>

  </div><!-- /.content-wrapper -->
  <jsp:include page="Controller/Footer.jsp" flush="true"></jsp:include>
</div><!-- ./wrapper -->

<!-- jQuery 2.1.4 -->
<script src="plugins/jQuery/jQuery-2.1.4.min.js"></script>
<!-- jQuery UI 1.11.2 -->
<script src="http://code.jquery.com/ui/1.11.2/jquery-ui.min.js" type="text/javascript"></script>
<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
<script>
  $.widget.bridge('uibutton', $.ui.button);
</script>
<!-- Bootstrap 3.3.2 JS -->
<script src="bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<!-- Morris.js charts -->
<script src="http://cdnjs.cloudflare.com/ajax/libs/raphael/2.1.0/raphael-min.js"></script>
<script src="plugins/morris/morris.min.js" type="text/javascript"></script>
<!-- Sparkline -->
<script src="plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
<!-- jvectormap -->
<script src="plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" type="text/javascript"></script>
<script src="plugins/jvectormap/jquery-jvectormap-world-mill-en.js" type="text/javascript"></script>
<!-- jQuery Knob Chart -->
<script src="plugins/knob/jquery.knob.js" type="text/javascript"></script>
<!-- daterangepicker -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.2/moment.min.js" type="text/javascript"></script>
<script src="plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
<!-- datepicker -->
<script src="plugins/datepicker/bootstrap-datepicker.js" type="text/javascript"></script>
<!-- Bootstrap WYSIHTML5 -->
<script src="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
<!-- Slimscroll -->
<script src="plugins/slimScroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<!-- FastClick -->
<script src='plugins/fastclick/fastclick.min.js'></script>
<!-- AdminLTE App -->
<script src="dist/js/app.min.js" type="text/javascript"></script>

<!-- AdminLTE dashboard demo (This is only for demo purposes) -->
<script src="dist/js/pages/dashboard.js" type="text/javascript"></script>

<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js" type="text/javascript"></script>
</body>
</html>