<%--
  Created by IntelliJ IDEA.
  User: gukam
  Date: 5/24/2015
  Time: 3:41 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="header-topbar-option-demo" class="page-header-topbar">
  <nav id="topbar" role="navigation" style="margin-bottom: 0;" data-step="3" class="navbar navbar-default navbar-static-top">

    <div class="navbar-header">
      <button type="button" data-toggle="collapse" data-target=".sidebar-collapse" class="navbar-toggle"><span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button>
      <a id="logo" href="index.html" class="navbar-brand"><span class="fa fa-rocket"></span><span class="logo-text">4 Colors Game</span><span style="display: none" class="logo-text-icon">µ</span></a></div>
    <div class="topbar-main">

      <form id="topbar-search" action="" method="" class="hidden-sm hidden-xs">
        <div class="input-icon right text-white"><a href="#"><i class="fa fa-search"></i></a><input type="text" placeholder="Search here..." class="form-control text-white"/></div>
      </form>

      <ul class="nav navbar navbar-top-links navbar-right mbn">
        <li class="dropdown"><a data-hover="dropdown" href="#" class="dropdown-toggle"><i class="fa fa-bell fa-fw"></i><span class="badge badge-green">3</span></a>

        </li>
        <li class="dropdown"><a data-hover="dropdown" href="#" class="dropdown-toggle"><i class="fa fa-envelope fa-fw"></i><span class="badge badge-orange">7</span></a>

        </li>
        <li class="dropdown"><a data-hover="dropdown" href="#" class="dropdown-toggle"><i class="fa fa-tasks fa-fw"></i><span class="badge badge-yellow">8</span></a>

        </li>
      </ul>
    </div>
  </nav>
  <!--BEGIN MODAL CONFIG PORTLET-->
  <div id="modal-config" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" data-dismiss="modal" aria-hidden="true" class="close">
            &times;</button>
          <h4 class="modal-title">
            Modal title</h4>
        </div>
        <div class="modal-body">
          <p>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eleifend et nisl eget
            porta. Curabitur elementum sem molestie nisl varius, eget tempus odio molestie.
            Nunc vehicula sem arcu, eu pulvinar neque cursus ac. Aliquam ultricies lobortis
            magna et aliquam. Vestibulum egestas eu urna sed ultricies. Nullam pulvinar dolor
            vitae quam dictum condimentum. Integer a sodales elit, eu pulvinar leo. Nunc nec
            aliquam nisi, a mollis neque. Ut vel felis quis tellus hendrerit placerat. Vivamus
            vel nisl non magna feugiat dignissim sed ut nibh. Nulla elementum, est a pretium
            hendrerit, arcu risus luctus augue, mattis aliquet orci ligula eget massa. Sed ut
            ultricies felis.</p>
        </div>
        <div class="modal-footer">
          <button type="button" data-dismiss="modal" class="btn btn-default">
            Close</button>
          <button type="button" class="btn btn-primary">
            Save changes</button>
        </div>
      </div>
    </div>
  </div>
  <!--END MODAL CONFIG PORTLET-->
</div>
</body>
</html>
