
<%@ page import="Interfaces.View.iShorProfile" %>
<%@ page import="Core.Controller.Account" %>
<%@ page import="Interfaces.View.iProfile" %>
<%@ page import="Core.Model.UserControl" %>
<%@ page import="Core.Controller.Message" %>
<%@ page import="java.util.*" %>
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
    UserControl userControl = (UserControl)pageContext.getServletContext().getAttribute("userControl");

    String nickname = (String)session.getAttribute("nickname");
    Set<Integer> friends = new HashSet<Integer>();
    iProfile profile = null;

    if(nickname == null) {
      String redirectURL = "Accont/Login.jsp";
      response.sendRedirect(redirectURL);
      profile = new Account();
    }
    else
    {
      try {
        profile = userControl.getUser(nickname);
        friends = userControl.getFriends(profile.getID());
      }
      catch (Exception ex){
        String k = ex.getMessage();
      }
    }
   String friend = request.getParameter("friend");
    if(friend == null)
      friend = "-1";
  %>
  <jsp:include page="Controller/Header.jsp" flush="true"></jsp:include>
  <jsp:include page="Controller/Sidebar.jsp" flush="true"></jsp:include>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper" style="padding: 1px;">

    <div style="width: 300px; float: left">
      <div class="box box-solid">
        <div class="box-header with-border">
          <h3 class="box-title">მეგობრები</h3>
          <div class="box-tools">
            <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
          </div>
        </div>
        <div class="box-body no-padding">
          <ul class="nav nav-pills nav-stacked">
            <% for (int id : friends) {
              iShorProfile shortProf = userControl.getUser(id);
            %>
            <li class="<%= friend.equals(shortProf.getNickname()) ? "active" : ""%>">
              <a href="Messages.jsp?friend=<%=shortProf.getID()%>">
                <img src="<%= shortProf.getPicturePath() %>"  alt="Smiley face" style="width: 60px; border-radius: 50%; ">  <%=shortProf.getNickname()%>
              </a>
            </li>
            <% } %>
          </ul>
        </div><!-- /.box-body -->
      </div>
    </div>

    <div style=" padding-left: 310px">
      <div class="box box-solid">
        <div class="box-body no-padding">
          <ul class="nav nav-pills nav-stacked">
            <%

              ArrayList<Message> messages = userControl.getMessages(profile.getID(), Integer.parseInt(friend));


              for (int i = 0; i<messages.size(); i++ ) {
              String mess = messages.get(i).getText();
              String style = messages.get(i).getType() == Message.Type.SENT ?
                      "background-color: rgb(186, 223, 255);\n" +
                      "    text-align: right;" :
              "background-color: rgb(140, 179, 213)";
            %>
            <li style="<%= style %> ; font-size: 19px"> <%= mess %></li>
            <% } %>
          </ul>
        </div><!-- /.box-body -->
        <form action="/SendMessage" method="post">
          <input type="hidden" name="profileFrom" value="<%= profile.getID() %>">
          <input type="hidden" name="profileTo" value="<%= friend %>">
          <textarea  style="width: 100%; height:70px"  name="message"></textarea>
          <button class="btn btn-block btn-primary">მიწერა</button>
        </form>
      </div>
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