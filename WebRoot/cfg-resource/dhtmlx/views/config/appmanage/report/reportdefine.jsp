<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iconPath = dhxResPath + "/views/config/appmanage/report";
String reportId = request.getParameter("P_reportId");
System.out.println("P_reportId = " + reportId);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>系统配置平台1.0-CELL报表定义</title>
		<META http-equiv="x-ua-compatible" content="IE=8">
		<LINK rel=stylesheet type=text/css HREF="<%=dhxResPath %>/views/config/appmanage/report/control/olstyle.css">
		<script type="text/javascript">
			var basePath = "<%=basePath%>";
			var contextPath="<%=path%>";
			var iconPath = DHX_RES_PATH + "/views/config/appmanage/report/";
			var reportId = "<%=reportId%>";
			var cllPath  = "cll/" + reportId + ".cll";
		</script>
		<script src="<%=dhxResPath %>/common/js/base.js" type="text/javascript"></script>
		<script src="<%=dhxResPath %>/common/js/common.js" type="text/javascript"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appcommon.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/AppActionURI.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/reportdefine.js"></script>
		<SCRIPT language="vbscript" src="<%=dhxResPath %>/views/config/appmanage/report/control/function.vbs"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/report/control/buttons.js"></SCRIPT>
		<script language="vbscript" src="<%=dhxResPath %>/views/config/appmanage/report/control/menuhandler.vbs"></script>
		<SCRIPT type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/report/control/reportobject.js"></SCRIPT>
	</head>
	<body style="overflow: hidden;">
	<script type="text/javascript">
		<!-- 报表菜单控件 -->
		//writeCellMenu();
	</script>
	<!-- 报表菜单控件 -->
	<div id="reporttoolbar$1$div"></div>
	<div id="reporttoolbar$2$div"></div>
	<div id="reporttoolbar$3$div"></div>
	<table>
		<tr>
			<td>
				<div id="cellweb$div" style="float:left;border-width:0px;border-style:none;">
				<script type="text/javascript">
					writeCellWeb();
				</script>
				</div>
			</td>
			<td>
				<div id="accordion$div" style="width:260px;display:none;loat:left;border-width:0px;border-style:none;">
				</div>
			</td>
		</tr>
	</table>
	<SCRIPT type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/report/control/CellUtil.js"></SCRIPT>
	<script type="text/javascript">
		var fontNameCombo; // reportobject.js中使用的变量，不可以删除
		initReportToolbar();
		initObj();
		//
		initAccordion();
		CellUtil.initBinded();
	</script>
	</body>
</html>