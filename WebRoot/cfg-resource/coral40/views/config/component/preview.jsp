<%@page import="java.net.URLDecoder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>构件预览</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript">
			var contextPath = "<%=path%>";
		</script>
		<%@include file="/cfg-resource/coral40/common/jsp/_cui_library.jsp" %>
	</head>
	<body>
		<ces:tabs id="tabbarDiv" heightStyle="fill"><ul></ul></ces:tabs>
	</body>
</html>
<script type="text/javascript">
	function addTab(name, url) {
		var tabId = generateId("tab");
		//$("#tabbarDiv").tabs("add", name, "", {"href":tabId, "closeable":false});
		$("#tabbarDiv").tabs("add", {
			label : name,
			content : "",
			href : "#" + tabId,
			closeable : true
		}).tabs("option", "active", "#" + tabId);
		$.ajax({
			url : contextPath + "/" + url,
			dataType :"html", 
			context : document.body
		}).done(function(html) {
			var CFG_configInfo = {}
			$("#" + tabId).data("selfUrl", url);
			$("#" + tabId).data("parentConfigInfo", CFG_configInfo);
			$("#" + tabId).append(html);
			$.parser.parse($("#" + tabId));
			if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_bodyOnLoad) {
				CFG_configInfo.childConfigInfo.CFG_bodyOnLoad();
			}
		});
		$("#tabbarDiv").tabs("option", "active", "#" + tabId);
	}
	$(function(){
		var localUrl = window.location.href;
		var componentVersionId = '<%=request.getParameter("componentVersionId")%>';
		var previewUrl = localUrl.substring(localUrl.indexOf('&previewUrl=')+12);
		var componentType = '<%=request.getParameter("componentType")%>';
		var componentAlias = '<%=URLDecoder.decode(request.getParameter("componentAlias"), "utf-8")%>';
		if (componentType == "1") {
			addTab(componentAlias + "预览", previewUrl);
			addTab("构件配置信息", "component/component-version!previewConfigFile?componentVersionId=" + componentVersionId);
			$("#tabbarDiv").tabs("option", "active", 0);
		} else if (componentType == "2") {
			addTab("构件配置信息", "component/component-version!previewConfigFile?componentVersionId=" + componentVersionId);
		} else if (componentType == "3" || componentType == "4" || componentType == "5" || componentType == "6") {
			addTab(componentAlias + "预览", previewUrl);
		}
	});
</script>