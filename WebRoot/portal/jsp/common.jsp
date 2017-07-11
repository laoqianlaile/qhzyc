<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/utility" prefix="utility" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="cui"%>


<%-- 定义常用页面变量 
<%@ page import="com.ces.zwww.utils.IOUtil"%>
<%@ page import="com.ces.xarch.core.security.entity.SysUser"%>
<%@ page import="org.springframework.security.core.context.SecurityContext"%>
--%>
<c:set var="request"    scope="page" value="${pageContext.request}" />
<c:set var="scheme"  	scope="page" value="${request.scheme}"      />
<c:set var="server"  	scope="page" value="${request.serverName}"  />
<c:set var="port"  		scope="page" value="${request.serverPort}"  />
<c:set var="ctx" 		scope="page" value="${request.contextPath}" />
<c:set var="basePath"   scope="page" value="${scheme}://${server}:${port}${ctx}" />
<c:set var="headline"   scope="page" value="上海市政务外网管理中心 电子政务综合管理平台" />
<c:set var="version"    scope="page" value="当前版本：V1.0.0" />
<c:set var="copyright"	scope="page" value="版权所有  &copy; 上海市政务外网管理中心" />
