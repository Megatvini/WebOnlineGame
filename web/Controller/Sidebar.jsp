<%@ page import="Core.Bean.Account" %>
<%@ page import="Interfaces.iProfile" %>
<%@ page import="Core.Dao.AccountDao" %>
<%--
  Created by IntelliJ IDEA.
  User: Annie
  Date: 28-May-15
  Time: 22:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<%
  AccountDao userControl = (AccountDao)pageContext.getServletContext().getAttribute(AccountDao.class.getName());
 String nick = (String)session.getAttribute("nickname");
 iProfile prof ;
  if (nick != null) {
    prof = userControl.getUser(nick);
   //TODO:
  }
 else
    prof = new Account();
%>
<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">
  <!-- sidebar: style can be found in sidebar.less -->
  <section class="sidebar">
    <!-- Sidebar user panela -->
    <div class="user-panel">
      <div class="pull-left image">
        <img src="<%= prof.getPicturePath() %>" class="img-circle" alt="User Image" />
      </div>
      <div class="pull-left info">
        <p><%= prof.getNickname() %></p>
        <div class="fa fa-fw fa-line-chart" ></div>
         <%= prof.getRating() %> </a>
      </div>
    </div>
<% String uri = request.getRequestURI();
  String pageName = uri.substring(uri.lastIndexOf("/")+1); %>
    <!-- sidebar menu: : style can be found in sidebar.less -->
    <ul class="sidebar-menu">
      <li class="header">ნავიგაცია</li>
      <li class="<%= pageName.equals("index.jsp") ? "active" : "" %>">
        <a href="../index.jsp">
          <i class="fa fa-home"></i> <span>ჩემი გვერდი</span>
        </a>
      </li>
      <li class="<%= pageName.equals("StartGame.jsp") ? "active" : "" %>">
        <a href="../CreateRoom">
          <i class="fa fa-gamepad"></i> <span>თამაშის დაწყება</span>
        </a>
      </li>
      <li class="<%= pageName.equals("Friends.jsp") ? "active" : "" %>">
        <a href="../Friends.jsp">
          <i class="fa  fa-users"></i> <span>მეგობრები</span>
        </a>
      </li>
      <li class="<%= pageName.equals("Users.jsp") ? "active" : "" %>">
        <a href="../Users.jsp">
          <i class="fa  fa-cloud"></i> <span>ონლაინში</span>
        </a>
      </li>
      <li class="<%= pageName.equals("Raiting.jsp") ? "active" : "" %>">
      <a href="../Raiting.jsp">
        <i class="fa fa-signal"></i> <span>რეიტინგი</span>
      </a>
    </li>
      <li class="<%= pageName.equals("Messages.jsp") ? "active" : "" %>">
      <a href="../Messages.jsp">
        <i class="fa  fa-envelope-o"></i> <span>წერილები</span>
      </a>
    </li>
      <li class="<%= pageName.equals("GameInfo.jsp") ? "active" : "" %>">
      <a href="../GameInfo.jsp">
        <i class="fa fa-info-circle"></i> <span>თამაშის შესახებ</span>
      </a>
    </li>
      <li class="<%= pageName.equals("Statistics.jsp") ? "active" : "" %>">
      <a href="../Statistics.jsp">
        <i class="fa  fa-bar-chart-o"></i> <span>სტატისტიკა</span>
      </a>
    </li>
      <li>
      <a href="/Logout">
        <i class="fa fa-sign-out"></i> <span>გამოსვლა</span>
      </a>
    </li>

    </ul>
  </section>
  <!-- /.sidebar -->
</aside>
</body>
</html>
