<%@ page import="MatchMaking.StartingGroup" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: Nika
  Date: 6/30/2015
  Time: 18:43
  To change this template use File | Settings | File Templates.
--%>
<%
  String userName = (String) session.getAttribute("nickname");
  Map<String, StartingGroup> groupMap = (Map<String, StartingGroup>)
          session.getServletContext().getAttribute(StartingGroup.class.getName());
  StartingGroup group = groupMap.get(userName);
  if (group == null) {
    response.sendRedirect("index.jsp");
  } else {
    if (group.getCreator().equals(userName)) {%>
      <%@include  file="startForm.jsp" %>
<%  }
  }%>