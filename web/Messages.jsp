<%@ page import="Core.Bean.Account" %>
<%@ page import="Interfaces.iProfile" %>
<%@ page import="Core.Bean.Message" %>
<%@ page import="java.util.*" %>
<%@ page import="Core.Dao.AccountDao" %>
<%@ page import="Core.Dao.FriendsDao" %>
<%@ page import="Core.Dao.MessageDao" %>
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
  <script src="plugins/jQuery/jQuery-2.1.4.min.js"></script>
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
    AccountDao userControl = (AccountDao)pageContext.getServletContext().getAttribute(AccountDao.class.getName());
    FriendsDao friendControl = (FriendsDao)pageContext.getServletContext().getAttribute(FriendsDao.class.getName());
    MessageDao messageControl = (MessageDao)pageContext.getServletContext().getAttribute(MessageDao.class.getName());

    String nickname = (String)session.getAttribute("nickname");

    Set<String> friends = new HashSet<>();
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
        friends = friendControl.getFriendNamesByID(profile.getID());
      }
      catch (Exception ex){
        String k = ex.getMessage();
      }
    }
    String friendNickname = request.getParameter("friend");
    if(friendNickname == null)
      friendNickname = friends.isEmpty()? "0" : friends.iterator().next().toString();
    if (friendNickname.equals("0")){
      String redirectURL = "MessageRed.jsp";
      response.sendRedirect(redirectURL);
      return;
    }

    int friendID = userControl.getUser(friendNickname).getID();
    String friendPic = userControl.getUser(friendNickname).getPicturePath();
    String myPic = profile.getPicturePath();
    List<Message> messages = messageControl.getMessages(profile.getID(), friendID);
  %>
  <jsp:include page="Controller/Header.jsp" flush="true"></jsp:include>
  <jsp:include page="Controller/Sidebar.jsp" flush="true"></jsp:include>


  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper" style="padding: 1px;">

    <div style="width: 300px; float: left">
      <div class="box box-solid">
        <div class="box-header with-border" >
          <h3 class="box-title">მეგობრები</h3>
          <div class="box-tools">
            <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
          </div>
        </div>
        <div class="box-body no-padding">
          <ul class="nav nav-pills nav-stacked">
            <% for (String nick : friends) {
              iProfile shortProf = userControl.getUser(nick);
            %>
            <li class="<%= friendNickname.equals(nick) ? "active" : "" %>">
              <a href="Messages.jsp?friend=<%=nick%>">
                <img src="<%= shortProf.getPicturePath() %>"  alt="Smiley face" style="width: 60px; border-radius: 50%; ">  <%=shortProf.getNickname()%>
              </a>
              <div id="<%= nick %>" style="width:30px; float:right;"></div>
            </li>
            <% } %>
          </ul>
        </div><!-- /.box-body -->
      </div>
    </div>

    <div style=" padding-left: 310px">



      <div class="box box-warning direct-chat direct-chat-warning" style="height: 85%">
        <div class="box-header with-border" style="height: 50px;">
          <h3 class="box-title">მესიჯები</h3>
          <div class="box-tools pull-right">
            <span data-toggle="tooltip" title="3 New Messages" class="badge bg-yellow">3</span>
            <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
            <button class="btn btn-box-tool" data-toggle="tooltip" title="Contacts" data-widget="chat-pane-toggle"><i class="fa fa-comments"></i></button>
            <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
          </div>
        </div><!-- /.box-header -->
        <div class="box-body">

          <!-- Conversations are loaded here -->
          <div id="messages" style="height: 85%" class="direct-chat-messages">

            <% for (int i = 0; i<messages.size(); i++ ) {
                Message mess = messages.get(i);
              if(mess.getType() == Message.Type.SENT){
            %>

            <!-- Message. Default to the left -->
            <div class="direct-chat-msg right">
              <div class="direct-chat-info clearfix">
                <span class="direct-chat-name pull-right"><%= nickname %></span>
                <span class="direct-chat-timestamp pull-left"><%= mess.getDate() %></span>
              </div><!-- /.direct-chat-info -->
              <img class="direct-chat-img" src="<%= myPic %>" alt="message user image"><!-- /.direct-chat-img -->
              <div class="direct-chat-text">
                 <%= mess.getText() %>
              </div><!-- /.direct-chat-text -->
            </div><!-- /.direc            t-chat-msg -->


            <% }
            else
            {
            %>

            <!-- Message. Default to the left -->
            <div class="direct-chat-msg ">
              <div class="direct-chat-info clearfix">
                <span class="direct-chat-name pull-left"><%= friendNickname %></span>
                <span class="direct-chat-timestamp pull-right"><%= mess.getDate() %></span>
              </div><!-- /.direct-chat-info -->
              <img class="direct-chat-img" src="<%= friendPic %>" alt="message user image"><!-- /.direct-chat-img -->
              <div class="direct-chat-text">
                 <%= mess.getText() %>
              </div><!-- /.direct-chat-text -->
            </div><!-- /.direc            t-chat-msg -->


            <% }} %>

            <!-- Message to the right -->
            <div class="direct-chat-msg">
              <div class="direct-chat-info clearfix">
                <span class="direct-chat-name pull-left">Sarah Bullock</span>
                <span class="direct-chat-timestamp pull-right">23 Jan 2:05 pm</span>
              </div><!-- /.direct-chat-info -->
              <img class="direct-chat-img" src="dist/img/user3-128x128.jpg" alt="message user image"><!-- /.direct-chat-img -->
              <div class="direct-chat-text">
                You better believe it!
              </div><!-- /.direct-chat-text -->
            </div><!-- /.direct-chat-msg -->

          </div><!--/.direct-chat-messages-->
          <!-- Contacts are loaded here -->
          <div class="direct-chat-contacts">
            <ul class="contacts-list">
              <li>
                <a href="#">
                  <img class="contacts-list-img" src="dist/img/user1-128x128.jpg">
                  <div class="contacts-list-info">
                                <span class="contacts-list-name">
                                  Count Dracula
                                  <small class="contacts-list-date pull-right">2/28/2015</small>
                                </span>
                    <span class="contacts-list-msg">How have you been? I was...</span>
                  </div><!-- /.contacts-list-info -->
                </a>
              </li><!-- End Contact Item -->
              <li>
                <a href="#">
                  <img class="contacts-list-img" src="dist/img/user7-128x128.jpg">
                  <div class="contacts-list-info">
                                <span class="contacts-list-name">
                                  Sarah Doe
                                  <small class="contacts-list-date pull-right">2/23/2015</small>
                                </span>
                    <span class="contacts-list-msg">I will be waiting for...</span>
                  </div><!-- /.contacts-list-info -->
                </a>
              </li><!-- End Contact Item -->
              <li>
                <a href="#">
                  <img class="contacts-list-img" src="dist/img/user3-128x128.jpg">
                  <div class="contacts-list-info">
                                <span class="contacts-list-name">
                                  Nadia Jolie
                                  <small class="contacts-list-date pull-right">2/20/2015</small>
                                </span>
                    <span class="contacts-list-msg">I'll call you back at...</span>
                  </div><!-- /.contacts-list-info -->
                </a>
              </li><!-- End Contact Item -->
              <li>
                <a href="#">
                  <img class="contacts-list-img" src="dist/img/user5-128x128.jpg">
                  <div class="contacts-list-info">
                                <span class="contacts-list-name">
                                  Nora S. Vans
                                  <small class="contacts-list-date pull-right">2/10/2015</small>
                                </span>
                    <span class="contacts-list-msg">Where is your new...</span>
                  </div><!-- /.contacts-list-info -->
                </a>
              </li><!-- End Contact Item -->
              <li>
                <a href="#">
                  <img class="contacts-list-img" src="dist/img/user6-128x128.jpg">
                  <div class="contacts-list-info">
                                <span class="contacts-list-name">
                                  John K.
                                  <small class="contacts-list-date pull-right">1/27/2015</small>
                                </span>
                    <span class="contacts-list-msg">Can I take a look at...</span>
                  </div><!-- /.contacts-list-info -->
                </a>
              </li><!-- End Contact Item -->
              <li>
                <a href="#">
                  <img class="contacts-list-img" src="dist/img/user8-128x128.jpg">
                  <div class="contacts-list-info">
                                <span class="contacts-list-name">
                                  Kenneth M.
                                  <small class="contacts-list-date pull-right">1/4/2015</small>
                                </span>
                    <span class="contacts-list-msg">Never mind I found...</span>
                  </div><!-- /.contacts-list-info -->
                </a>
              </li><!-- End Contact Item -->
            </ul><!-- /.contatcts-list -->
          </div><!-- /.direct-chat-pane -->
        </div><!-- /.box-body -->
        <div class="box-footer">

            <div class="input-group">
              <input type="text" name="message" id="messageText" placeholder="Type Message ..." onkeydown="if (event.keyCode == 13) { sendMessage(); }" class="form-control">
                          <span class="input-group-btn">
                            <button type="button" onclick="sendMessage()" class="btn btn-warning btn-flat">Send</button>
                          </span>
            </div>

        </div>
      </div>

        <input type="hidden" name="profileFrom" value="<%= profile.getID() %>">
        <input type="hidden" name="myNick" value="<%= nickname %>">
        <input type="hidden" id="profileToID" value="<%= friendID %>">
        <input type="hidden" id="profileToNick" value="<%= friendNickname %>">
        <input type="hidden" id="myPic" value="<%= profile.getPicturePath() %>">
        <input type="hidden" id="friendPic" value="<%= friendPic %>">


      </div>
    </div>

  </div><!-- /.content-wrapper -->
  <jsp:include page="Controller/Footer.jsp" flush="true"></jsp:include>
</div><!-- ./wrapper -->


<script src="scripts.js"> </script>
<!-- jQuery 2.1.4 -->

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