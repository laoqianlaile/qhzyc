<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String folderPath = request.getRequestURI();
folderPath = folderPath.substring(0, folderPath.lastIndexOf("/"));
%>
<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>页面级构件demo</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript">
		// 页面级构件入参
		var moduleId = "<%=request.getParameter("moduleId")%>";
	</script>
	<script type="text/javascript" src="<%=path%>/cfg-resource/dhtmlx/common/js/CFG_component.js"></script>
	<script type="text/javascript" src="<%=folderPath%>/js/page.js"></script>
	<script type="text/javascript">
		// 测试
		alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue('关联的系统参数')
			+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue('selfParam1')
			+ "\n构件入参：\t" + "moduleId=<%=request.getParameter("moduleId")%>");
	</script>
  </head>
  <body onload="load()">
  </body>
</html>
