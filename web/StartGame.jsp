
<%@ page import="Interfaces.iProfile" %>
<%@ page import="Core.Bean.Account" %>
<%@ page import="Core.Dao.AccountDao" %>
<%--
  Created by IntelliJ IDEA.
  User: gukam
  Date: 5/24/2015
  Time: 3:31 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>თამაშის დაწყება</title>
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

  <link href="plugins/iCheck/all.css" rel="stylesheet" type="text/css">

  <link href="plugins/select2/select2.min.css" rel="stylesheet" type="text/css">
  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>

  <!-- jQuery 2.1.4 -->
  <script src="plugins/jQuery/jQuery-2.1.4.min.js"></script>
  <!-- jQuery UI 1.11.2 -->
  <script src="http://code.jquery.com/ui/1.11.2/jquery-ui.min.js" type="text/javascript"></script>

  <script src="upload_script.js"></script>
  <link href="matchMaking/css/buttons.css" rel="stylesheet">
  <![endif]-->
</head>
<body class="skin-blue sidebar-mini">
<div class="wrapper">
  <%
    AccountDao userControl = (AccountDao)pageContext.getServletContext().getAttribute(AccountDao.class.getName());

    String nickname = (String)session.getAttribute("nickname");
    iProfile profile;
    if(nickname == null) {
      String redirectURL = "Accont/Login.jsp";
      response.sendRedirect(redirectURL);
      profile = new Account();
    }
    else
    {
      profile = userControl.getUser(nickname);
    }


  %>
  <jsp:include page="Controller/Header.jsp" flush="true"></jsp:include>
  <jsp:include page="Controller/Sidebar.jsp" flush="true"></jsp:include>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper" style="padding: 1px;">
    <div class="box box-primary" style="width: 96%; height: 700px; margin: 20px; min-width: 350px">
      <div class="box-header"><h3 class="box-title">თამაშის დაწყება</h3></div>
      <div>

        <form action="/StartGame" method="get">
          <%@include  file="matchMaking/submitroom.jsp" %>

          <div class="col-md-4">
            <div class="info-box bg-yellow">
              <span class="info-box-icon"><i ><div class="pull-left" style="padding: 10px">
                <img id="pic1" data-path="<%=profile.getNickname()%>"  width="70px" height="70px"  src="/default.png" class="img-circle" alt="User Image" />
              </div></i></span>
              <div class="info-box-content">
                <span class="info-box-text">მოთამაშე 1</span>
                <span class="info-box-number">
                  <input name="p1" id="p1" value="Empty" style="border:none; background: rgba(255, 255, 255, 0);" readonly></span>

              </div><!-- /.info-box-content -->
            </div>

            <div class="info-box bg-green">
              <span class="info-box-icon"><i ><div class="pull-left" style="padding: 10px">
                <img id="pic2"   width="70px" height="70px"  src="/default.png" class="img-circle" alt="User Image" />
              </div></i></span>
              <div class="info-box-content">
                <span class="info-box-text">მოთამაშე 2</span>
                <span class="info-box-number">
                  <input name="p2" id="p2" value="Empty" style="border:none; background: rgba(255, 255, 255, 0);" readonly></span>

              </div><!-- /.info-box-content -->
            </div>

            <div class="info-box bg-red">
              <span class="info-box-icon"><i >
                <div class="pull-left" style="padding: 10px">
                  <img id="pic3"   width="70px" height="70px"  src="/default.png" width="80px" height="80px" class="img-circle" alt="User Image" />
                </div>
              </i></span>
              <div class="info-box-content">
                <span class="info-box-text">მოთამაშე 3</span>
                <span class="info-box-number">
                  <input name="p3" id="p3" value="Empty" style="border:none; background: rgba(255, 255, 255, 0);" readonly></span>

              </div><!-- /.info-box-content -->
            </div>

            <div class="info-box bg-aqua">
              <span class="info-box-icon"><i ><div class="pull-left" style="padding: 10px">
                <img id="pic4"   width="70px" height="70px"  src="/default.png" class="img-circle" alt="User Image" />
              </div></i></span>
              <div class="info-box-content">
                <span class="info-box-text">მოთამაშე 4</span>
                <span class="info-box-number">
                  <input name="p4" id="p4" value="Empty" style="border:none; background: rgba(255, 255, 255, 0);" readonly></span>

              </div><!-- /.info-box-content -->
            </div>
          </div>


          </fieldset>
        </form>
      </div>
      <div align="left">

      </div>
    </div>
  </div><!-- /.content-wrapper -->
  <div class="form-group">
    <label class="hover">
      <div class="icheckbox_flat-green checked hover" aria-checked="false" aria-disabled="false" style="position: relative;"><input type="checkbox" class="flat-red" checked="" style="position: absolute; opacity: 0;"><ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; border: 0px; opacity: 0; background: rgb(255, 255, 255);"></ins></div>
    </label>
    <label class="">
      <div class="icheckbox_flat-green" aria-checked="false" aria-disabled="false" style="position: relative;"><input type="checkbox" class="flat-red" style="position: absolute; opacity: 0;"><ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; border: 0px; opacity: 0; background: rgb(255, 255, 255);"></ins></div>
    </label>
    <label>
      <div class="icheckbox_flat-green disabled" aria-checked="false" aria-disabled="true" style="position: relative;"><input type="checkbox" class="flat-red" disabled="" style="position: absolute; opacity: 0;"><ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; border: 0px; opacity: 0; background: rgb(255, 255, 255);"></ins></div>
      Flat green skin checkbox
    </label>
  </div>
  <jsp:include page="Controller/Footer.jsp" flush="true"></jsp:include>
</div><!-- ./wrapper -->

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
<script src="dist/js/popup.js" type="text/javascript"></script>
</body>
</html>