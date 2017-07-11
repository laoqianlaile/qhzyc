<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String jhpch = request.getParameter("jhpch");
String jypzh = request.getParameter("jypzh")==null?"":request.getParameter("jypzh");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>追溯链条生成页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%@ include file="/cfg-resource/coral40/common/jsp/_cui_library.jsp"%> 
	<script type="text/javascript" src="<%=path %>/cfg-resource/coral40/views/cspt/zhuisuliantiao/test.js">
  </script>
  </head>
  <body>
  	<input type="text" id="jhpch" value="<%=jhpch%>" style="display: none;"/>
  	<input type="text" id="jypzh" value="<%=jypzh%>" style="display: none;"/>
    <div id="gridDemo1"><div class="gridDemo1"></div></div>
  </body>
</html>
