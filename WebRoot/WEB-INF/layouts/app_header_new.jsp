<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page import="com.ces.xarch.core.security.entity.SysUser" %>
<%@ page import="com.ces.utils.TokenUtils" %>
<%@ page import="com.ces.component.trace.utils.CompanyInfoUtil" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
	SysUser sysUser = CommonUtil.getUser();
	String token = TokenUtils.getToken(sysUser.getLoginName(), sysUser.getPassword()).replace("\n","").trim();
	String url = "/auth40/sso.jsp?cesssotoken=" + token;
	String multi = request.getParameter("multi");
	String username = CommonUtil.getUser().getUsername();
%>
<!--head start-->
<div class="headwrap">
	<div class="header">
		<div class="logo">
			<img src="${ctx}/cfg-resource/coral40/common/themes/base/images/logo.1.02.png" class="logoimg"/>
		</div>
		<div class="headMid">
			<div class="headMid_c">
				<ul class="headul">
				</ul>
			</div>
		</div>
		<div class="headRight">
			<ul class="set" style="width: 300px;">
				<li class="set_animate" onclick="logout()">注销</li>
				<!--
				<li class="set_help"><img src="${ctx}/cfg-resource/coral40/common/themes/base/images/help.png"></li>
				<li class="set_animate"><img src="${ctx}/cfg-resource/coral40/common/themes/base/images/setting.png"></li>
				-->
			<% if(CompanyInfoUtil.getInstance().isQy()) { %>
				<li class="set_animate" onclick="linkToAuth()">系统管理平台</li>
			<% } %>
			<% if("true".equals(multi)) { %>
				<li class="set_animate" onclick="linkToTrace()">选择系统</li>
			<% } %>
				<li class="set_animate">欢迎你：<%=username %></li>
			</ul>
		</div>
	</div>
	<!--second menu start-->
	<div class="menu_b menufix">
		<p class="lockside unlock"></p>
	</div>
	<!--second menu end-->
</div>
<!--head end-->
<script>
	//注销
	function logout() {
		$.confirm("确定注销", function (r) {
			if (r) {
				window.location = '${ctx}/logout';
			}
		});
	}
	//登录到系统管理平台
	function linkToAuth() {
		window.open("<%=url%>", "_blank");
	}
	//切换到选择系统界面
	function linkToTrace() {
		window.open("${ctx}/trace.jsp", "_self");
	}
</script>