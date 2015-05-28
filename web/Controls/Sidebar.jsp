<%--
  Created by IntelliJ IDEA.
  User: gukam
  Date: 5/24/2015
  Time: 3:36 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<%String uri = request.getRequestURI();
  uri = uri.substring(uri.lastIndexOf("/") + 1);
%>
<nav id="sidebar" role="navigation" data-step="2" data-intro="Template has &lt;b&gt;many navigation styles&lt;/b&gt;"
     data-position="right" class="navbar-default navbar-static-side">
  <div class="sidebar-collapse menu-scroll">
    <div class="col-md-panel">
      <div class="form-group">
        <div class="text-center mbl"><img src="http://www.iconmay.com/thumbnails/detail/1/user%20man%20consumer%20visitor%20user%20icon.png" alt="" class="img-responsive"/></div>       
      </div>
      <table class="table table-hover">
        <tbody>
        <tr>
          <td>Nickname</td>
          <td>gukamaz</td>
        </tr>
          <td>Status</td>
          <td><span class="label label-success">Active</span></td>
        </tr>
        </tbody>
      </table>
    </div>
    <ul id="side-menu" class="nav">
      <div class="clearfix"></div>

      <li class="<%= uri.equals("index.jsp")  ? "active" : ""%>"><a href="dashboard.html"><i class="fa fa-tachometer fa-fw">
        <div class="icon-bg bg-orange"></div>
      </i><span class="menu-title">ჩემი გვერდი</span></a></li>

      <li><a href="Layout.html"><i class="fa fa-desktop fa-fw">
        <div class="icon-bg bg-pink"></div>
      </i><span class="menu-title">თამაშის დაწყება</span></a>

      </li>
      <li><a href="UIElements.html"><i class="fa fa-send-o fa-fw">
        <div class="icon-bg bg-green"></div>
      </i><span class="menu-title">ონლაინში</span></a>

      </li>
      <li><a href="Forms.html"><i class="fa fa-edit fa-fw">
        <div class="icon-bg bg-violet"></div>
      </i><span class="menu-title">რეიტინგი</span></a>

      </li>
      <li><a href="Tables.html"><i class="fa fa-th-list fa-fw">
        <div class="icon-bg bg-blue"></div>
      </i><span class="menu-title">წერილები</span></a>

      </li>
      <li><a href="DataGrid.html"><i class="fa fa-database fa-fw">
        <div class="icon-bg bg-red"></div>
      </i><span class="menu-title">თამაშის შესახებ</span></a>

      </li>
      <li><a href="Pages.html"><i class="fa fa-file-o fa-fw">
        <div class="icon-bg bg-yellow"></div>
      </i><span class="menu-title">სტატისტიკა</span></a>

      </li>
      <li><a href="Extras.html"><i class="fa fa-gift fa-fw">
        <div class="icon-bg bg-grey"></div>
      </i><span class="menu-title">გამოსვლა</span></a>
    </ul>

  </div>
</nav>
</body>
</html>
