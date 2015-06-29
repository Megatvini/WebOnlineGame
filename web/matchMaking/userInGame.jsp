<%@ page import="MatchMaking.MatchMaker" %>
<%--
  Created by IntelliJ IDEA.
  User: Nika
  Date: 6/16/2015
  Time: 02:08
  To change this template use File | Settings | File Templates.
--%>
<%
    String name = (String) session.getAttribute("nickname");
    MatchMaker matchMaker = (MatchMaker) application.getAttribute(Servlets.MatchMaker.class.getName());
    if (matchMaker.containsParticipant(name)) {
        response.sendRedirect("/matchMaking/loading.jsp");
    }
%>