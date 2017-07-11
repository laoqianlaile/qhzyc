<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>青海中药材登录</title>
<script src="<%=basePath%>/cfg-resource/coral40/_cui_library/ui/jquery-1.11.1.min.js"></script>
<script src="<%=basePath%>resource/js/hdtyLogin/icheck.min.js"></script>
<link rel="stylesheet" href="<%=basePath%>/cfg-resource/coral40/common/themes/login/css/qhstyle.css">
<body>
<div class="login">
    <!--header-->
    <div class="login-header">
        <span><img src="<%=basePath%>/cfg-resource/coral40/common/themes/login/css/images/qhlogin/login-r-t.png" alt=""></span>
        <div class="log"><img src="<%=basePath%>/cfg-resource/coral40/common/themes/login/css/images/qhlogin/logo.png" alt=""></div>
    </div>
    <!--main-->
    <div class="form form1">
        <form id="loginForm" action="${ctx}/login_check" method="post" onsubmit='return checkform(this);'>
            <div class="main">
                <div class="login-title">
                    <img src="<%=basePath%>/cfg-resource/coral40/common/themes/login/css/images/qhlogin/login-title.png" alt="">
                </div>
                <p>
                    <span><img src="<%=basePath%>/cfg-resource/coral40/common/themes/login/css/images/qhlogin/zh.png" alt=""></span>
                    <input type="text" placeholder="请输入用户名" id="username" name="username"  class="username" onKeyPress="checkEnter(event,this)"></p>
                <p>
                    <span><img src="<%=basePath%>/cfg-resource/coral40/common/themes/login/css/images/qhlogin/suo.png" alt=""></span>
                    <input type="password" placeholder="请输入密码"id="passwordInput" name="password"  class="password" onKeyPress="checkEnter(event,this)"></p>
                <p><button type="submit" >登&nbsp;&nbsp;&nbsp;&nbsp;录</button></p>
            </div>
        </form>

        <div class="login_l_b">
            <img src="<%=basePath%>/cfg-resource/coral40/common/themes/login/css/images/qhlogin/login-left.png" alt="">
        </div>

        <div class="login_r">
            <img src="<%=basePath%>/cfg-resource/coral40/common/themes/login/css/images/qhlogin/login-right.png" alt="">
        </div>
        <div class="foot">
            <img src="<%=basePath%>/cfg-resource/coral40/common/themes/login/css/images/qhlogin/login-foot.png" alt="">
        </div>
        <!--footer-->
        <div class="footer">
            COPYRIGHT 2015 上海中信信息发展股份有限公司 ALL RIGHTS RESERVED
        </div>
</body>


<script type="text/javascript">
    $(document).ready(function () {
        $('.skin-minimal input').iCheck({
            checkboxClass: 'icheckbox_minimal',
            increaseArea: '20%'
        });

        function getHeight() {
            $(".wrapper").css("height", '100%');
        }

        getHeight();

        $(window).resize(function () {
            getHeight();
        });
    });

    function logout() {
        if (confirm('确定退出吗?')) {
            window.close();
        }
    }

    function checkEnter(event, element) {

        var code = event.keyCode;
        if (code == 13) {
            if (element.name == 'username') {
                document.forms[0].password.focus();
                event.returnValue = false;
            } else if (element.name == 'password') {
                document.forms[0]._spring_security_remember_me.focus();
                event.returnValue = false;
            } else if (element.name == '_spring_security_remember_me') {
                document.forms[0].submit();
            }
        }
    }
    function chekform() {
        if ($("#username").val() == "") {
            alert('请输入用户名！');
            $("#username").focus();
            return false;
        }
        if ($("#username").val().replace(/\s/g) != $("#username").val()) {
            alert('用户名不可以有空格！');
            $("#username").focus();
            return false;
        }
        if ($("#passwordInput").val() == "") {
            alert('请输入密码！');
            $("#passwordInput").focus();
            return false;
        }
        return true;
    }
    /*function login() {
     if ($("#username").val() == "") {
     alert('请输入用户名！');
     $("#username").focus();
     return;
     }
     if ($("#username").val().replace(/\s/g) != $("#username").val()) {
     alert('用户名不可以有空格！');
     $("#username").focus();
     return;
     }
     if ($("#passwordInput").val() == "") {
     alert('请输入密码！');
     $("#passwordInput").focus();
     return;
     }
     debugger;
     var formData = $("#loginForm").serialize();
     $.ajax({
     url: "${ctx}/login_check",
     type: "post",
     data: $("#loginForm").serialize(),
     async: false,
     beforeSend: function (xhr) {
     xhr.setRequestHeader("TRACE-SASS-AJAX-LOGIN", "TRACE");
     },
     success: function(data) {
     debugger;
     if (data.success == true) {
     loginHandler();
     } else {
     alert("用户名不存在或密码错误！");
     //                    $("#errMsg").text("用户名不存在或密码错误！");
     }
     }
     });
     }*/
    /* function loginHandler() {
     debugger
     var jsonUrl = "<%=basePath%>" + "trace!getAllSystemByUser.json";
     $.ajax({
     url: jsonUrl,
     type: "post",
     async: false,
     success: function(data) {
     if (data.length == 1) {
     var url = "<%=basePath%>" + "app.jsp?systemId=" + data[0].sysCode + "&companyCode=" + data[0].companyCode;
     window.location.href = url;
     } else {
     window.location.href = "<%=basePath%>" + "trace.jsp";
     }
     }
     });
     /!*$.getJSON(jsonUrl, function (data) {
     if (data.length == 1) {
     var url = "<%=basePath%>" + "app.jsp?systemId=" + data[0].sysCode + "&companyCode=" + data[0].companyCode;
     window.location.href = url;
     } else {
     window.location.href = "<%=basePath%>" + "trace.jsp";
     }
     });*!/
     }*/

    document.onkeydown=function(event){

        var e = event || window.event || arguments.callee.caller.arguments[0];
        if(e && e.keyCode==13){

            if(document.getElementById('username').value!=''&&document.getElementById('passwordInput').value!=''){
                login();
            }
        }
    }

</script>
