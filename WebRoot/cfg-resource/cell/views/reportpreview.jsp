<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<!DOCTYPE HTML>
<html style="width: 100%;height: 100%;">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>CELL报表打印</title>
		<script type="text/javascript" src="<%=path %>/cfg-resource/dhtmlx/common/js/dhtmlx.js"></script>
		<script type="text/javascript">
			var localUrl = window.location.href;
	    	var reportPrintUrl = localUrl.substring(localUrl.indexOf('reportPrintUrl=') + 15);
	    	var loader = dhtmlxAjax.getSync("<%=path %>/cell/cell!token.json");
	        var content = loader.xmlDoc.responseText.replace(/:null/g, ":''").replace(/\\r\\n/ig, "");
	        var jsonObj = eval("(" + content + ")");
	        function isIE() {
	            return ("ActiveXObject" in window);
	        }
			function f_init() {
				var url = "<%=basePath%>/cfg-resource/cell/views/token.jsp?_token=" + jsonObj + "&reportPrintUrl=" + reportPrintUrl;
				if (isIE()) {
					document.getElementById("_iff1").innerHTML = 
						'<OBJECT'
							+ ' id="CesBrowser"'
							+ ' classid="clsid:50C045EC-AB64-4F5E-A4DC-2E4E8853B4C1"'
							+ ' title="ces"'
							+ ' width="100%"'
							+ ' height="100%"'
						+ '>'
						+ '<param name="urlPath" value="'+url+'" />'
						+ '</OBJECT>';
				} else {
					document.getElementById("_iff1").innerHTML = 
						'<OBJECT'
							+ ' type="application/x-itst-activex"'
							+ ' id="CesBrowser"'
							+ ' clsid="{50C045EC-AB64-4F5E-A4DC-2E4E8853B4C1}"'
							+ ' title="cew"'
							+ ' width="100%"'
							+ ' height="100%"'
							+ ' param_urlPath="'+url+'"'
						+ '</OBJECT>';
				}
			}
		</script>
	</head>
	<body style="width: 100%;height: 100%;margin: 0px;padding: 0px;overflow: hidden;" scroll="no" onload="f_init()">
		<div id="_iff1" style="width: 100%;height: 100%;overflow: hidden;"></div>
	</body>
</html>
