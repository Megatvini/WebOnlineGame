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
  <meta charset="UTF-8">
    <title></title>
</head>
<body>
<header class="main-header">
  <!-- Logo -->
  <a href="index.jsp" class="logo">
    <!-- mini logo for sidebar mini 50x50 pixels -->
    <span class="logo-mini"><b>4</b>C</span>
    <!-- logo for regular state and mobile devices -->
    <span class="logo-lg"><b>4</b>Color</span>
  </a>
  <!-- Header Navbar: style can be found in header.less -->
  <nav class="navbar navbar-static-top" role="navigation">
    <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
      <span class="sr-only">Toggle navigation</span>
    </a>
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
            <li>
              <!-- inner menu: contains the actual data -->
              <ul id="notFriends" class="menu">

              </ul>
            </li>
            <li class="footer"><a href="#">ყველას ნახვა</a></li>
          </ul>
        </li>
        <!-- Messages: style can be found in dropdown.less-->
        <li class="dropdown messages-menu">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">
            <i class="fa fa-envelope-o"></i>
            <span class="label label-success" id="messagesSpan"></span>
          </a>
          <ul class="dropdown-menu">
            <li class="header">მესიჯები</li>
            <li>
              <!-- inner menu: contains the actual data -->
              <ul class="menu" id="notMessages">


              </ul>
            </li>
            <li class="footer"><a href="../Messages.jsp">ყველას ნახვა</a></li>
          </ul>
        </li>
        <!-- Tasks: style can be found in dropdown.less -->
        <li class="dropdown tasks-menu">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">
            <i class="fa fa-flag-o"></i>
            <span class="label label-danger" id="invatesSpan"></span>
          </a>
          <ul class="dropdown-menu">
            <li>
              <!-- inner menu: contains the actual data -->
              <ul class="menu" id="notInvates">

              </ul>
            </li>
            <li class="footer">
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

<script src="../NotificationScripts.js" charset="UTF-8"></script>
<script>
  addToInvates('gukamaz', 'gukamaz');

</script>
</body>
</html>
