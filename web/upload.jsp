<%@ page import="Core.Dao.AccountDao" %>
<%@ page import="Interfaces.iAccount" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>HTML5 drag'n'drop file upload with Servlet</title>
  <script>
    window.onload = function() {
      var dropbox = document.getElementById("dropbox");
      dropbox.addEventListener("dragenter", noop, false);
      dropbox.addEventListener("dragexit", noop, false);
      dropbox.addEventListener("dragover", noop, false);
      dropbox.addEventListener("drop", dropUpload, false);
    } ;

    function noop(event) {
      event.stopPropagation();
      event.preventDefault();
    }

    function dropUpload(event) {
      noop(event);
      var files = event.dataTransfer.files;

      for (var i = 0; i < files.length; i++) {
        upload(files[i]);
      }
    }

    function upload(file) {
      document.getElementById("status").innerHTML = "Uploading " + file.name;

      var formData = new FormData();
      formData.append("file", file);

      var xhr = new XMLHttpRequest();
      xhr.upload.addEventListener("progress", uploadProgress, false);
      xhr.addEventListener("load", uploadComplete, false);
      xhr.open("POST", "upload", true); // If async=false, then you'll miss progress bar support.
      xhr.send(formData);
    }

    function uploadProgress(event) {
      // Note: doesn't work with async=false.
      var progress = Math.round(event.loaded / event.total * 100);
      document.getElementById("status").innerHTML = "Progress " + progress + "%";
    }

    function uploadComplete(event) {
      document.getElementById("status").innerHTML = event.target.responseText;
    }
  </script>
  <style>
    #dropbox {
      width: 300px;
      height: 200px;
      border: 1px solid gray;
      border-radius: 5px;
      padding: 5px;
      color: gray;
    }
  </style>
</head>
<body>
<div id="dropbox">Drag and drop a file here...</div>
<div id="status"></div>



<%
AccountDao userControl = (AccountDao)pageContext.getServletContext().getAttribute(AccountDao.class.getName());
  String nickname = (String)session.getAttribute("nickname");
  iAccount profile = userControl.getUser(nickname);
%>



<div class="form-group" style="width: 300px;">

  <img src="<%= profile.getPicturePath() %>"  alt="Smiley face" style="border-radius: 50%" height="300" width="300">
  <br/>
  <input class="form-control" type="text" name="picture" value="<%= profile.getPicturePath() %>"  placeholder="Default input">
</div>


<form action="upload" method="post" enctype="multipart/form-data">
  <input type="text" name="description" />
  <input type="file" name="file" />
  <input type="submit" />
</form>



</body>
</html>