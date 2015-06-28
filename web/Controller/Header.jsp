<%@ page import="Core.Bean.Account" %>
<%@ page import="Interfaces.iAccount" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="Interfaces.iProfile" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="Core.Dao.AccountDao" %>
<%@ page import="Core.Dao.FriendsDao" %>
<%--
  Created by IntelliJ IDEA.
  User: Annie
  Date: 28-May-15
  Time: 22:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<header class="main-header">
  <!-- Logo -->
  <a href="index.jsp" class="logo">
    <!-- mini logo for sidebar mini 50x50 pixels -->
    <span class="logo-mini"><b>A</b>LT</span>
    <!-- logo for regular state and mobile devices -->
    <span class="logo-lg"><b>4</b>Color</span>
  </a>
  <!-- Header Navbar: style can be found in header.less -->
  <nav class="navbar navbar-static-top" role="navigation">

    <%
      String nick = (String)session.getAttribute("nickname");

      AccountDao userControl = (AccountDao)pageContext.getServletContext().getAttribute(AccountDao.class.getName());
      FriendsDao friendControl = (FriendsDao)pageContext.getServletContext().getAttribute(FriendsDao.class.getName());

      iProfile prof ;
      if (nick != null) {

        prof = userControl.getUser(nick); //TODO:
        }
      else{
        prof = new Account();
        return;
      }
      Set<String> waitingFriends = friendControl.getFriendRequestsTo(nick);
    %>
    <div class="navbar-custom-menu">
      <ul class="nav navbar-nav">
        <!-- Notifications: style can be found in dropdown.less -->
        <li class="dropdown notifications-menu">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">
            <i class="fa fa-bell-o"></i>
            <span class="label label-warning" id="requestsSpan"></span>
          </a>
          <ul class="dropdown-menu">
            <li class="header">შენ გაქვს <%=waitingFriends.size()%> მეგობრობის თხოვნა. </li>
            <li>
              <!-- inner menu: contains the actual data -->
              <ul id="notFriends" class="menu">

              </ul>
            </li>
            <li class="footer"><a href="#">View all</a></li>
          </ul>
        </li>
        <!-- Messages: style can be found in dropdown.less-->
        <li class="dropdown messages-menu">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">
            <i class="fa fa-envelope-o"></i>
            <span class="label label-success" id="messagesSpan"></span>
          </a>
          <ul class="dropdown-menu">
            <li class="header">You have 4 messages</li>
            <li>
              <!-- inner menu: contains the actual data -->
              <ul class="menu" id="notMessages">


              </ul>
            </li>
            <li class="footer"><a href="#">See All Messages</a></li>
          </ul>
        </li>
        <!-- Tasks: style can be found in dropdown.less -->
        <li class="dropdown tasks-menu">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">
            <i class="fa fa-flag-o"></i>
            <span class="label label-danger">9</span>
          </a>
          <ul class="dropdown-menu">
            <li class="header">You have 9 tasks</li>
            <li>
              <!-- inner menu: contains the actual data -->
              <ul class="menu">
                <li><!-- Task item -->
                  <a href="#">
                    <h3>
                      Design some buttons
                      <small class="pull-right">20%</small>
                    </h3>
                    <div class="progress xs">
                      <div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                        <span class="sr-only">20% Complete</span>
                      </div>
                    </div>
                  </a>
                </li><!-- end task item -->
                <li><!-- Task item -->
                  <a href="#">
                    <h3>
                      Create a nice theme
                      <small class="pull-right">40%</small>
                    </h3>
                    <div class="progress xs">
                      <div class="progress-bar progress-bar-green" style="width: 40%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                        <span class="sr-only">40% Complete</span>
                      </div>
                    </div>
                  </a>
                </li><!-- end task item -->
                <li><!-- Task item -->
                  <a href="#">
                    <h3>
                      Some task I need to do
                      <small class="pull-right">60%</small>
                    </h3>
                    <div class="progress xs">
                      <div class="progress-bar progress-bar-red" style="width: 60%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                        <span class="sr-only">60% Complete</span>
                      </div>
                    </div>
                  </a>
                </li><!-- end task item -->
                <li><!-- Task item -->
                  <a href="#">
                    <h3>
                      Make beautiful transitions
                      <small class="pull-right">80%</small>
                    </h3>
                    <div class="progress xs">
                      <div class="progress-bar progress-bar-yellow" style="width: 80%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                        <span class="sr-only">80% Complete</span>
                      </div>
                    </div>
                  </a>
                </li><!-- end task item -->
              </ul>
            </li>
            <li class="footer">
              <a href="#">View all tasks</a>
            </li>
          </ul>
        </li>
        <!-- User Account: style can be found in dropdown.less -->


      </ul>
    </div>
    <!-- search form -->
    <form action="Users.jsp" method="get" class="sidebar-form" style="height: 30px; max-width: 550px;">
      <div class="input-group">
        <input style="height: 28px;" type="text" class="form-control" name="search" placeholder="Search..."/>
              <span class="input-group-btn">
               <a></a> <button type='submit' id='search-btn' class="btn btn-flat"><i class="fa fa-search"></i></button>
              </span>
      </div>
    </form>
    <!-- /.search form -->
  </nav>
</header>

<script src="../NotificationScripts.js"></script>
<script>
  console.log( "ready!" );
  $( document ).ready(function() {
    console.log( "ready!" );
    addToFriends('shako', 'http://i0.wp.com/cvisioncentral.com/wp-content/themes/buddyboss/_inc/images/avatar-member.jpg');
    addToMessages('shako', 'http://i0.wp.com/cvisioncentral.com/wp-content/themes/buddyboss/_inc/images/avatar-member.jpg', 'hu');
    console.log( "ready!" );
  });

</script>
</body>
</html>
