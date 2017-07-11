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
		<div id="preview" style="width:100%;height:100%;">
			<ces:tabs id="tabbarDiv" heightStyle="fill" onTabClose="tabClose"><ul></ul></ces:tabs>
		</div>
	</body>
</html>
<script type="text/javascript">
	$(function(){
		var localUrl = window.location.href;
    	var CFG_componentUrl = localUrl.substring(localUrl.indexOf('CFG_componentUrl=') + 17);
    	if (CFG_componentUrl.charAt(0) != '/') {
    		CFG_componentUrl = "/" + CFG_componentUrl;
    	}
    	CFG_addTab({}, "构件", contextPath + CFG_componentUrl);
	});
	function CFG_addTab(CFG_configInfo, text, url) {
		var tabId = generateId("tab");
		//$("#tabbarDiv").tabs("add", text, "", {"href":tabId, "closeable":true});
		$("#tabbarDiv").tabs("add", {
				label : text,
				content : "",
				href : "#" + tabId,
				closeable : true
			}).tabs("option", "active", "#" + tabId);
		$.ajax({
			url : url,
			dataType :"html", 
			context : document.body
		}).done(function(html) {
			$("#" + tabId).data("selfUrl", url);
			$("#" + tabId).data("parentConfigInfo", CFG_configInfo);
			$("#" + tabId).append(html);
			$.parser.parse($("#" + tabId));
			if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_initReserveZones) {
				CFG_configInfo.childConfigInfo.CFG_initReserveZones();
			}
		});
		$("#tabbarDiv").tabs("option", "active", "#" + tabId);
	}
</script>