<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path       = request.getContextPath();
String folderPath = request.getRequestURI();
String basePath   = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String reportId   = request.getParameter("P_reportId");
String tableId    = request.getParameter("P_tableId");
String rowIds     = request.getParameter("P_rowIds");
String timestamp  = request.getParameter("P_timestamp");

folderPath = folderPath.substring(0, folderPath.lastIndexOf("/"));
%>
<!DOCTYPE HTML>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>CELL报表打印</title>
		<META http-equiv="x-ua-compatible" content="IE=8">
	    <%@include file="/cfg-resource/coral40/common/jsp/_cui_library.jsp" %>
		<LINK rel=stylesheet type=text/css HREF="<%=folderPath %>/control/olstyle.css">
		<script type="text/javascript">
		var params = {
				reportId : "<%=reportId%>", // "402881eb41c24fd90141c25e3cf00015"
				tableId  : "<%=tableId%>",  // "402881e8412aaab801412ae92f6a01a7"
				rowIds   : "<%=rowIds%>",
				timestamp : "<%=timestamp%>",
				cllPath  : $.contextPath + "/cfg-resource/dhtmlx/views/config/appmanage/report/cll/<%=reportId%>.cll"
		};
		var cellUtil = null;
		</script>
		<SCRIPT language="vbscript" src="<%=folderPath %>/control/function.vbs"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=folderPath %>/control/buttons.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=folderPath %>/control/print.js"></SCRIPT>
	    <SCRIPT type="text/javascript" src="<%=folderPath %>/control/CellUtil.js"></SCRIPT>
	</head>
	<body scroll="no">
			<div id="_tbar_border_" class="toolbarsnav clearfix">
				<div id="_rpt_tbar_"></div>
			</div>
			<div id="_cell_body_" class="coral-adjusted"></div>
	</body>
</html>
