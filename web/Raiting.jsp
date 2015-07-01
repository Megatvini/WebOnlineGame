<%@ page import="Core.Controller.Achievements" %>
<%@ page import="Interfaces.iProfile" %>
<%@ page import="java.util.Set" %>
<%@ page import="Core.Dao.AccountDao" %>
<%@ page import="java.util.List" %>
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
  <!-- jQuery 2.1.4 -->
  <script src="plugins/jQuery/jQuery-2.1.4.min.js"></script>

</head>
<body class="skin-blue sidebar-mini">
<div class="wrapper">

  <jsp:include page="Controller/Header.jsp" flush="true"></jsp:include>
  <jsp:include page="Controller/Sidebar.jsp" flush="true"></jsp:include>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper" style="padding: 1px;">
    <%
      AccountDao userControl = (AccountDao) application.getAttribute(AccountDao.class.getName());
      List<String> users = null;

      String nickname = (String)session.getAttribute("nickname");
      String pag =  request.getParameter("page");
      int pagID = 0;

      if(pag != null) pagID = Integer.parseInt(pag) - 1;
      int PageCount = (userControl.getUsersCount() + 4) / 5;

      if (nickname == null) {
        String redirectURL = "Accont/Login.jsp";
        response.sendRedirect(redirectURL);
        return;
      }
      else {
        users =  userControl.getUsersIntervalByRating(0, userControl.getUsersCount());
      }
      int index = 1;
    %>


    <div class="box">
      <div class="box-header">
        <h3 class="box-title">რეიტინგი</h3>
      </div><!-- /.box-header -->
      <div class="box-body">
        <table id="example1" class="table table-bordered table-striped">
          <thead>
          <tr>
            <th>ექაუნთი</th>
            <th>სახელი და გვარი</th>
            <th>სქესი</th>
            <th>წოდება</th>
            <th>რეიტინგი</th>
          </tr>
          </thead>
          <tbody>
          <%  for (String userNick : users) {
            iProfile shortProf = null;
            try {
              shortProf = userControl.getUser(userNick);
            } catch (Exception e) {
              continue;
            }
            double wid = ((users.size()-index+1) /(double) users.size()) * 100;
          %>
          <tr>
            <td><a href="Profile.jsp?nickname=<%=shortProf.getNickname()%>"><%=shortProf.getNickname()%></a></td>
            <td><%=shortProf.getFirstName() + " " + shortProf.getLastName()%></td>
            <td><%=shortProf.getGender()%></td>
            <td> <%= "mdzletamdzle"%></td>
            <td><%=shortProf.getRating()%></td>
          </tr>
          <% index++; } %>

          </tfoot>
        </table>
      </div><!-- /.box-body -->
    </div><!-- /.box -->







  </div><!-- /.content-wrapper -->
  <jsp:include page="Controller/Footer.jsp" flush="true"></jsp:include>
</div><!-- ./wrapper -->


<!-- jQuery UI 1.11.2 -->
<script src="http://code.jquery.com/ui/1.11.2/jquery-ui.min.js" type="text/javascript"></script>
<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
<script>
  $.widget.bridge('uibutton', $.ui.button);
</script>
<!-- Bootstrap 3.3.2 JS -->
<script src="../../bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<!-- DATA TABES SCRIPT -->
<script src="../../plugins/datatables/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="../../plugins/datatables/dataTables.bootstrap.min.js" type="text/javascript"></script>
<!-- SlimScroll -->
<script src="../../plugins/slimScroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<!-- FastClick -->
<script src='../../plugins/fastclick/fastclick.min.js'></script>
<!-- AdminLTE App -->
<script src="../../dist/js/app.min.js" type="text/javascript"></script>
<!-- AdminLTE for demo purposes -->
<script src="../../dist/js/demo.js" type="text/javascript"></script>
<!-- page script -->
<script type="text/javascript">
  $(function () {
    $("#example1").dataTable();
    $('#example2').dataTable({
      "bPaginate": true,
      "bLengthChange": false,
      "bFilter": false,
      "bSort": true,
      "bInfo": true,
      "bAutoWidth": false
    });
  });
</script>
</body>
</html>