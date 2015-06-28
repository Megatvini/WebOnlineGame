<%@ page import="Core.Dao.AccountDao" %>
<%@ page import="Interfaces.iAccount" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>HTML5 drag'n'drop file upload with Servlet</title>
  <script src="upload_script.js"> </script>

</head>
<body>

<div class="form-group" style="width: 300px;">

  <img src="http://localhost:8080/d?id=57166b2d-cf8b-4eb7-b890-81cf974ca8a9"  alt="Smiley face" style="border-radius: 50%" height="300" width="300">

</div>

<div class = "profile_pic_upload" >
  <div id="status"></div>
  <form id = "myForm" action="javascript:upload(this)" enctype="multipart/form-data" >
    <input type="text" name="description" />
    <input type="file" name="file" />
    <input type="submit" />

  </form>

</div>

<%
  AccountDao userControl = (AccountDao)pageContext.getServletContext().getAttribute(AccountDao.class.getName());
  String nickname = (String)session.getAttribute("nickname");
  iAccount profile = userControl.getUser(nickname);
%>


</body>
</html>