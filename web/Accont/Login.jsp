<%--
  Created by IntelliJ IDEA.
  User: Nika
  Date: 6/29/2015
  Time: 01:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <link href="../dist/css/LoginStyle.css" rel="stylesheet" type="text/css">
    <title>ავტორიზაცია</title>
</head>
<body>
<div class="login-box">
    <div class="lb-header">
        <a href="#" class="active" id="login-box-link">ავტორიზაცია</a>
        <a href="#" id="signup-box-link">რეგისტრაცია</a>
    </div>
    <div class="social-login">
        <a href="#">
            <i class="fa fa-facebook fa-lg"></i>
            facebook
        </a>
        <a href="#">
            <i class="fa fa-google-plus fa-lg"></i>
            Google+
        </a>
    </div>
    <form class="email-login" action="/Login" method="post">
        <div class="u-form-group">
            <input type="text" placeholder="ექაუნთის სახელი" name="nickname"/>
        </div>
        <div class="u-form-group">
            <input type="password" placeholder="პაროლი" name="password"/>
        </div>
        <div class="u-form-group">
            <button type="submit">ავტორიზაცია</button>
        </div>
    </form>
    <form class="email-signup" method="post" action="/Registration">
        <div class="u-form-group">
            <input type="email" placeholder="მეილი" name="mail" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required/>
        </div>
        <div class="u-form-group">
            <input type="text" placeholder="ექაუნთი" name="nickname" required/>
        </div>
        <div class="u-form-group">
            <input name="password" type="password" placeholder="პაროლი" required id="password1"
                   maxlength="12" pattern="((\p{Punct})*([a-zA-Z])*([0-9])*)*.{5,}"
                    title="პაროლი უნდა შედგებოდეს ლათინური ასოების, ციფრების, სასვენი ნიშნებისგან და უნდა შეიცავდეს მინიმუმ 5 სიმბოლოს"/>
        </div>
        <div class="u-form-group">
            <input type="password" placeholder="გაიმეორეთ პაროლი" id="password2"/>
        </div>
        <div class="u-form-group">
            <button type="submit">რეგისტრაცია</button>
        </div>
    </form>
</div>
<script>
    $(".email-signup").hide();
    $("#signup-box-link").click(function(){
        $(".email-login").fadeOut(100);
        $(".email-signup").delay(100).fadeIn(100);
        $("#login-box-link").removeClass("active");
        $("#signup-box-link").addClass("active");
    });
    $("#login-box-link").click(function(){
        $(".email-login").delay(100).fadeIn(100);;
        $(".email-signup").fadeOut(100);
        $("#login-box-link").addClass("active");
        $("#signup-box-link").removeClass("active");
    });

    $(function () {
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional
        });
    });

    window.onload = function () {
        document.getElementById("password1").onchange = validatePassword;
        document.getElementById("password2").onchange = validatePassword;
    }
    function validatePassword(){
        var pass2=document.getElementById("password2").value;
        var pass1=document.getElementById("password1").value;
        if(pass1!=pass2)
            document.getElementById("password2").setCustomValidity("Passwords Don't Match");
        else
            document.getElementById("password2").setCustomValidity('');
//empty string means no validation error
    }
</script>
</body>
</html>
