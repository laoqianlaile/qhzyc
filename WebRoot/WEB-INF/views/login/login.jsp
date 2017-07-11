<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="cui" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ces.config.utils.SystemParameterUtil"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	String path = request.getContextPath();	
	request.setAttribute("path",request.getContextPath());
	// 档案馆不需要全宗，档案室需要
	String type = SystemParameterUtil.getInstance().getSystemParamValue("是否显示全宗");
	//项目发布时设置的自身参数
	String inParameter = request.getParameter("type")==null?"":request.getParameter("type");
	// 档案馆：0（其他）档案室：1
	if(!"".equals(inParameter)){
		request.setAttribute("type",inParameter);
	}else{
		request.setAttribute("type",type);
	}
	
%>
<!doctype html>
<html>
<head>

<title><%if("1".equals(type)){ %>光典档案信息资源管理软件5.0<%}else {%>光典馆藏资源管理系统3.0<%} %></title>

<script type="text/javascript" src="${path}/cfg-resource/coral40/_cui_library/ui/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${path}/cfg-resource/coral40/_cui_library/jquery.coral.min.js"></script>
<link href="${path}/cfg-resource/coral40/_cui_library/css/arch/css/reset.css" rel="stylesheet" type="text/css" />
<link href="${path}/cfg-resource/coral40/_cui_library/css/arch/css/login.css" rel="stylesheet" type="text/css" />
<link href="${path}/cfg-resource/coral40/_cui_library/themes/base/cui.all.css" rel="stylesheet"/>
<script type="text/javascript" src="${path}/cfg-resource/coral40/_cui_library/ui/respond.min.js"></script>

</head>
<script type="text/javascript">
	if (top.location.href.indexOf("previewUrl") == -1) {
		var url = self.location.href;
		if (url.indexOf("login_error=0") != -1 || url.indexOf("login_error=1") != -1) {
			if (top.location != self.location) {
				top.location = self.location;
			}
		} else {
			if (url.indexOf("fullscreen") == -1) {
				if (url.indexOf("?") != -1) {
					url += "&fullscreen=1";
				} else {
					url += "?fullscreen=1";
				}
				window.open(url, "", "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
				if (navigator.userAgent.indexOf("Firefox") > 0) {
		            top.location.href = 'about:blank ';
		        } else {
					top.opener = null;
					top.open("", "_self");
					window.setTimeout("top.close()", 1500);
				}
			}
		}
	}
</script>
<script type="text/javascript">
	function checkform(frm){
		if (frm.username.value=="") {
			$.alert('请输入用户名！');
			frm.username.focus();
			return false;
		}
		if (frm.username.value.replace(/\s/g) != frm.username.value) {
			$.alert('用户名不可以有空格！');
			frm.username.focus();
			return false;
		}
		if (frm.password.value == "") {
			$.alert('请输入密码！');
			frm.password.focus();
			return false;
		}
		return true;
	}
	function logout() {
		if(confirm('确定退出吗?')){
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
</script>
<body>
<div class="wrapper">
	<div class="loginbody">
    	<div class="loginblock">
    		<div class="loginbcenter"></div>
        </div>
    	<div class="loginbox">        	
        	<div class="<%if("1".equals(type)){ %>logo5<%}else{ %>logo3<%} %>"></div>
        	<form action="${ctx}/login_check" method="post" onsubmit='return checkform(this)'>
	            <div class="clearfix">
	                <ul class="loginul clearfix">
	                    <li>
	                        <span class="user"></span>
	                        <input type="text" class="logininput" name="username" id="username" value="<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>"/>
	                    </li>
	                    <li>
	                        <span class="password"></span>
	                        <input type="password" class="logininput" name="password" id="password" onKeyPress="checkEnter(event,this)"/>
	                    </li>
	                    <li>
	                        <input type="submit" class="submitbtn" value="">
	                    </li>
	                    <li>
	                        <input type="checkbox" class="re_checkbox"><label class="text_l">记住用户名密码</label>
	                    </li>
	                </ul>
	            </div>
            </form>
            <p class="copyright_l">上海中信信息发展股份有限公司   [版权所有]</p>
        </div>
    </div>
	
</div>
</body>
</html>
