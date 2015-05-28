<%--
  Created by IntelliJ IDEA.
  User: gukam
  Date: 5/24/2015
  Time: 3:31 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
  <title>პროფილი | პროფილი</title>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="shortcut icon" href="images/icons/favicon.ico">
  <link rel="apple-touch-icon" href="images/icons/favicon.png">
  <link rel="apple-touch-icon" sizes="72x72" href="images/icons/favicon-72x72.png">
  <link rel="apple-touch-icon" sizes="114x114" href="images/icons/favicon-114x114.png">
  <!--Loading bootstrap css-->
  <link type="text/css" rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,400,300,700">
  <link type="text/css" rel="stylesheet" href="http://fonts.googleapis.com/css?family=Oswald:400,700,300">
  <link type="text/css" rel="stylesheet" href="styles/jquery-ui-1.10.4.custom.min.css">
  <link type="text/css" rel="stylesheet" href="styles/font-awesome.min.css">
  <link type="text/css" rel="stylesheet" href="styles/bootstrap.min.css">
  <link type="text/css" rel="stylesheet" href="styles/animate.css">
  <link type="text/css" rel="stylesheet" href="styles/all.css">
  <link type="text/css" rel="stylesheet" href="styles/main.css">
  <link type="text/css" rel="stylesheet" href="styles/style-responsive.css">
  <link type="text/css" rel="stylesheet" href="styles/zabuto_calendar.min.css">
  <link type="text/css" rel="stylesheet" href="styles/pace.css">
  <link type="text/css" rel="stylesheet" href="styles/jquery.news-ticker.css">
  <link type="text/css" rel="stylesheet" href="styles/jplist-custom.css">
</head>
<body>

<div>

  <!--BEGIN BACK TO TOP-->
  <a id="totop" href="#"><i class="fa fa-angle-up"></i></a>
  <!--END BACK TO TOP-->
  <!--BEGIN TOPBAR-->
<jsp:include page="Controls/Header.jsp" flush="true"/>
  <!--END TOPBAR-->
  <div id="wrapper">
    <!--BEGIN SIDEBAR MENU-->
    <jsp:include page="Controls/Sidebar.jsp" flush="true"/>
    <div id="page-wrapper">
      <!--BEGIN TITLE & BREADCRUMB PAGE-->
      <div id="title-breadcrumb-option-demo" class="page-title-breadcrumb">
        <div class="page-header pull-left">
          <div class="page-title">
            პროფილი</div>
        </div>
        <ol class="breadcrumb page-breadcrumb pull-right">
          <li><i class="fa fa-home"></i>&nbsp;<a href="dashboard.html">Home</a>&nbsp;&nbsp;<i class="fa fa-angle-right"></i>&nbsp;&nbsp;</li>
          <li class="hidden"><a href="#">Animation</a>&nbsp;&nbsp;<i class="fa fa-angle-right"></i>&nbsp;&nbsp;</li>
          <li>Animation</li>
        </ol>
        <div class="clearfix">
        </div>
      </div>
      <!--END TITLE & BREADCRUMB PAGE-->
      <!--BEGIN CONTENT-->
      <div class="page-content">

      </div>
      <!--END CONTENT-->
      <!--BEGIN FOOTER-->
      <div id="footer">
        <div class="copyright">
          <a href="http://themifycloud.com">2014 © KAdmin Responsive Multi-Purpose Template</a></div>
      </div>
      <!--END FOOTER-->
    </div>
    <!--END PAGE WRAPPER-->
  </div>
</div>
<script src="script/jquery-1.10.2.min.js"></script>
<script src="script/jquery-migrate-1.2.1.min.js"></script>
<script src="script/jquery-ui.js"></script>
<script src="script/bootstrap.min.js"></script>
<script src="script/bootstrap-hover-dropdown.js"></script>
<script src="script/html5shiv.js"></script>
<script src="script/respond.min.js"></script>
<script src="script/jquery.metisMenu.js"></script>
<script src="script/jquery.slimscroll.js"></script>
<script src="script/jquery.cookie.js"></script>
<script src="script/icheck.min.js"></script>
<script src="script/custom.min.js"></script>
<script src="script/jquery.news-ticker.js"></script>
<script src="script/jquery.menu.js"></script>
<script src="script/pace.min.js"></script>
<script src="script/holder.js"></script>
<script src="script/responsive-tabs.js"></script>
<script src="script/jquery.flot.js"></script>
<script src="script/jquery.flot.categories.js"></script>
<script src="script/jquery.flot.pie.js"></script>
<script src="script/jquery.flot.tooltip.js"></script>
<script src="script/jquery.flot.resize.js"></script>
<script src="script/jquery.flot.fillbetween.js"></script>
<script src="script/jquery.flot.stack.js"></script>
<script src="script/jquery.flot.spline.js"></script>
<script src="script/zabuto_calendar.min.js"></script>
<script src="script/index.js"></script>
<script src="script/highcharts.js"></script>
<script src="script/data.js"></script>
<script src="script/drilldown.js"></script>
<script src="script/exporting.js"></script>
<script src="script/highcharts-more.js"></script>
<script src="script/charts-highchart-pie.js"></script>
<script src="script/charts-highchart-more.js"></script>
<script src="script/modernizr.min.js"></script>
<script src="script/jplist.min.js"></script>
<script src="script/jplist.js"></script>
<script src="script/animation.js"></script>
<!--CORE JAVASCRIPT-->
<script src="script/main.js"></script>
<script>        (function (i, s, o, g, r, a, m) {
  i['GoogleAnalyticsObject'] = r;
  i[r] = i[r] || function () {
            (i[r].q = i[r].q || []).push(arguments)
          }, i[r].l = 1 * new Date();
  a = s.createElement(o),
          m = s.getElementsByTagName(o)[0];
  a.async = 1;
  a.src = g;
  m.parentNode.insertBefore(a, m)
})(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');
ga('create', 'UA-145464-12', 'auto');
ga('send', 'pageview');


</script>
</body>
</html>
