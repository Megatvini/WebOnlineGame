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
<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">
  <!-- sidebar: style can be found in sidebar.less -->
  <section class="sidebar">
    <!-- Sidebar user panela -->
    <div class="user-panel">
      <div class="pull-left image">
        <img src="dist/img/user2-160x160.jpg" class="img-circle" alt="User Image" />
      </div>
      <div class="pull-left info">
        <p>Alexander Pierce</p>

        <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
      </div>
    </div>
<% String uri = request.getRequestURI();
  String pageName = uri.substring(uri.lastIndexOf("/")+1); %>
    <!-- sidebar menu: : style can be found in sidebar.less -->
    <ul class="sidebar-menu">
      <li class="header">MAIN NAVIGATION</li>
      <li class="<%= pageName.equals("index.jsp") ? "active" : "" %>">
        <a href="../index.jsp">
          <i class="fa fa-th"></i> <span>ჩემი გვერდი</span>
        </a>
      </li>
      <li class="<%= pageName.equals("StartGame.jsp") ? "active" : "" %>">
        <a href="../StartGame.jsp">
          <i class="fa fa-th"></i> <span>თამაშის დაწყება</span>
        </a>
      </li>
      <li class="<%= pageName.equals("Friends.jsp") ? "active" : "" %>">
        <a href="../Friends.jsp">
          <i class="fa fa-th"></i> <span>მეგობრები</span>
        </a>
      </li>
      <li class="<%= pageName.equals("Users.jsp") ? "active" : "" %>">
        <a href="../Users.jsp">
          <i class="fa fa-th"></i> <span>ონლაინში</span>
        </a>
      </li>
      <li class="<%= pageName.equals("Raiting.jsp") ? "active" : "" %>">
      <a href="../Raiting.jsp">
        <i class="fa fa-th"></i> <span>რეიტინგი</span>
      </a>
    </li>
      <li class="<%= pageName.equals("Messages.jsp") ? "active" : "" %>">
      <a href="../Messages.jsp">
        <i class="fa fa-th"></i> <span>წერილები</span>
      </a>
    </li>
      <li class="<%= pageName.equals("GameInfo.jsp") ? "active" : "" %>">
      <a href="../GameInfo.jsp">
        <i class="fa fa-th"></i> <span>თამაშის შესახებ</span>
      </a>
    </li>
      <li class="<%= pageName.equals("Statistics.jsp") ? "active" : "" %>">
      <a href="../Statistics.jsp">
        <i class="fa fa-th"></i> <span>სტატისტიკა</span>
      </a>
    </li>
      <li>
      <a href="../widgets.jsp">
        <i class="fa fa-th"></i> <span>გამოსვლა</span>
      </a>
    </li>

    </ul>
  </section>
  <!-- /.sidebar -->
</aside>
</body>
</html>