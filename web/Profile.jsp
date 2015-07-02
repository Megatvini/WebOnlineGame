<%@ page import="Core.Dao.AccountDao" %>
<%@ page import="Interfaces.iProfile" %>
<%@ page import="Core.Bean.Account" %>
<%@ page import="Interfaces.iAccount" %>
<%@ page import="Core.Dao.GameDao" %>
<%@ page import="Core.Bean.Game" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.Format" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Annie
  Date: 26-Jun-15
  Time: 19:35
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
<body class="skin-blue sidebar-mini">
<div class="wrapper">
  <%
    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    AccountDao userControl = (AccountDao)pageContext.getServletContext().getAttribute(AccountDao.class.getName());

    String nickname = (String)request.getParameter("nickname");
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

    GameDao gameDao = (GameDao) application.getAttribute(GameDao.class.getName());
    List<Game> gamesList = gameDao.getUserGamesByID(profile.getID(),10);


  %>
  <jsp:include page="Controller/Header.jsp" flush="true"></jsp:include>
  <jsp:include page="Controller/Sidebar.jsp" flush="true"></jsp:include>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper" style="padding: 1px;">
    <div class="box box-primary" style="width: 96%; margin: 20px; min-width: 350px">
      <form action="/ChangeAccount" method="get" accept-charset="UTF-8">
        <div class="box-header">
          <h3 class="box-title">პროფილი</h3>
        </div>

        <style type="text/css">
          .form-group
          {
            margin-right: 100px;
          }
          .tg  {border-collapse:collapse;border-spacing:0;}
          .tg td{font-family:Arial, sans-serif;font-size:14px;padding:5px 5px; border-width:0;overflow:hidden;word-break:normal;}
          .tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:5px 5px; border-width:0;overflow:hidden;word-break:normal; width: 450px; vertical-align: top;}
          #blanket {
            background-color:#111;
            opacity: 0.65;
            *background:none;
            position:absolute;
            z-index: 9001;
            top:0px;
            left:0px;
            width:100%;
          }

          #popUpDiv {
            position:absolute;
            width:400px;
            height:450px;
            z-index: 9002;
          }
        </style>
        <table class="tg">
          <tr>
            <th class="tg-031e" rowspan="6"> <div align="center">
              <div class="form-group" style="width: 300px;">
                <input type="hidden" id = "nickname" value="<%=nickname%>" />
                <div class="form-group" style="width: 300px;" >
                  <img  id = "profPic" src="default.png" alt="Smiley face"  height="300" width="300">
                  <script>
                    var host = "http://"+window.location.host+"/images?nickname=<%=nickname%>";
                    $("#profPic").attr("src",host)
                  </script>
                  <div class="small-box bg-green">
                    <div class="inner">
                      <h3><%=profile.getRating()%><sup style="font-size: 20px"></sup></h3>
                      <p>რეიტინგი</p>
                    </div>
                    <div class="icon">
                      <i class="ion ion-stats-bars"></i>
                    </div>
                    <a href="#" class="small-box-footer">
                      ყველას რეიტინგი <i class="fa fa-arrow-circle-right"></i>
                    </a>
                  </div>


                </div>

              </div>
            </div></th>
            <th class="tg-031e"> <div class="form-group">
              <label>სახელი</label>
              <input disabled class="form-control" type="text" name="firstname" value="<%= profile.getFirstName() %>" >
            </div></th>
          </tr>
          <tr>
            <td class="tg-031e">   <div class="form-group">
              <label>გვარი</label>
              <input disabled class="form-control" type="text" name="lastname"  value="<%= profile.getLastName() %>">
            </div></td>
          </tr>
          <tr>
            <td class="tg-031e">  <div class="form-group" style="width: 100%; margin-right: 25px">
              <label>მეილი</label>
              <div class="input-group" style="margin-right: 100px">
                <span class="input-group-addon"><i class="fa fa-envelope"></i></span>
                <input disabled type="email"  class="form-control" name="mail"  value="<%= profile.getMail() %>" >
              </div>
            </div></td>
          </tr>
          <tr>
            <td class="tg-031e">  <div class="form-group">
              <label>Date masks:</label>
              <div class="input-group" style="width: 100%">
                <div class="input-group-addon">
                  <i class="fa fa-calendar"></i>
                </div>
                <input disabled type="date" class="form-control" value="<%= profile.getBirthDate() %>" name="date" data-inputmask="'alias': 'dd/mm/yyyy'" data-mask/>
              </div><!-- /.input group -->
            </div><!-- /.form group --></td>
          </tr>
          <tr>
            <td class="tg-031e">
              <div class="form-group" style="width: 70px; float: left">
                <label>სქესი</label>
                <div class="radio">
                  <label>
                    <input disabled type="radio" name="optionsRadios" id="optionsRadios1" value="option1" <%= profile.getGender() == iAccount.Gender.MALE ? "checked=\"\"" : "" %> >
                    კაცი
                  </label>
                </div>
                <div class="radio">
                  <label>
                    <input disabled type="radio" name="optionsRadios" id="optionsRadios2" value="option2" <%= profile.getGender() == iAccount.Gender.FEMALE ? "checked=\"\"" : "" %>>
                    ქალი
                  </label>
                </div>

              </div>
            </td>

          </tr>

        </table>
        <div class="form-group" style="margin: 25px;">
          <label>აღწერა</label>
          <textarea disabled class="form-control" rows="3" name="about" placeholder="Enter ..."><%= profile.getAbout() %></textarea>
        </div>

      </form>

      <div id="blanket" style="display:none">

      </div>


      <div class="modal-content" id="popUpDiv" style="display:none" >
        <div class="modal-header">
          <button onclick="popup('popUpDiv')" type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
          <h4 class="modal-title">Modal Primary</h4>
        </div>
        <div class="modal-body"  id = "passwordChange">

          <div class="form-group" style="width: 100%">
            <label>შეიყვანეთ პაროლი</label>
            <input class="form-control" type="password" id="oldPassword" name="oldPass" value="" >
          </div>
          <div class="form-group" style="width: 100%">
            <label>ახალი პაროლი</label>
            <input class="form-control" type="password" id="newPassword" name="newPass" value="" >
          </div>
          <div class="form-group" style="width: 100%">
            <label>გაიმეორეთ ახალი პაროლი</label>
            <input class="form-control" type="password" id="passAgain"  name="passRep" value="">
          </div>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-default pull-left" onclick="popup('popUpDiv')" data-dismiss="modal">დახურვა</button>
          <button type="button" class="btn btn-default" onclick="javascript:savePassword()">შენახვა</button>
        </div>
        <div align="center">
          <label class="text-center" id ="validationInfo" >   </label>
        </div>
      </div>

      <div>

      </div>
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">თამაშების ისტორია</h3>

            </div><!-- /.box-header -->
            <div class="box-body table-responsive no-padding">
              <table class="table table-hover">
                <tbody><tr>
                  <th>#</th>
                  <th>თარიღი</th>
                  <th>ადგილი</th>
                  <th>მოთამაშეების რაოდენობა</th>
                  <th>რეიტინგის ცვლილება</th>
                </tr>
                <% int index = 1;
                  for(Game game : gamesList){
                    int place = game.getPlace();
                    String cssClass =  place == 1 ? "label-success" : place == 2 ? "label-warning" : place == 3 ? "label-primary" : "label-danger";
                %>
                <tr>
                  <td><%= index++ %></td>
                  <td><%= formatter.format(game.getDate()) %></td>
                  <td style=" padding-left: 27px;"><span class="label <%= cssClass %>"><%= place %></span></td>
                  <td style=" padding-left: 95px;"><%= game.getParticipantIDs().size() %></td>
                  <td style=" padding-left: 90px;"><%= game.getRatingChange() %></td>
                </tr>
                <% }%>
                </tbody></table>
            </div><!-- /.box-body -->
          </div><!-- /.box -->
        </div>
      </div>
    </div>


  </div><!-- /.content-wrapper -->

  <jsp:include page="Controller/Footer.jsp" flush="true"></jsp:include>
</div><!-- ./wrapper -->

<script src="assignPath.js"></script>

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