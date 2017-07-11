<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>系统配置平台V1.0</title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<script type="text/javascript">
	var contextPath = "${ctx}";
</script>
<%@include file="/cfg-resource/coral40/common/jsp/_cui_library.jsp" %>
</head>
<body onresize="">
	<div id="head">
		<%@ include file="/WEB-INF/layouts/app_header.jsp"%>
	</div>
	<div id="main">
        <ces:tabs id="tabbarDiv" heightStyle="fill" onTabClose="tabClose"><ul></ul></ces:tabs>
    </div>
	<div id="foot">
		<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	</div>
</body>
</html>
<script type="text/javascript">
	$("#tabbarDiv").css({"width":"100%", "height":"100%"});
	function menuClick(e, datas) {
		if (isEmpty(datas.url)) {
			return;
		}
		if (url.charAt(0) != '/') {
			url = "/" + url;
    	}
		if ($("#menuObj").data(datas.url)) {
			$("#tabbarDiv").tabs("option", "active", "#" + $("#menuObj").data(datas.url));
		} else {
			var tabId = generateId("tab");
			//$("#tabbarDiv").tabs("add", datas.name, "<iframe src='" + contextPath + "/" + datas.url + "' width='100%' height='100%' scrolling='Auto' frameborder='0' marginwidth='0' marginheight='0'></iframe>");
			//$("#tabbarDiv").tabs("add", datas.name, "", {"href":tabId, "closeable":true});
			$("#tabbarDiv").tabs("add", {
				label : datas.name,
				content : "",
				href : "#" + tabId,
				closeable : true
			}).tabs("option", "active", "#" + tabId);
			var navigationId = generateId("nav");
			$.ajax({
				url : contextPath + datas.url,
				dataType : "html",
				context : document.body
			}).done(function(html) {
				var CFG_configInfo = {};
				$("#" + tabId).data("selfUrl", datas.url);
				$("#" + tabId).data("parentConfigInfo", CFG_configInfo);
				$("#" + tabId).append(html);
				$.parser.parse($("#" + tabId));
				if (datas.url.indexOf("useNavigation") != -1) {
					$("#" + tabId).prepend("<div id='" + navigationId + "'></div>");
					$('#' + navigationId).navigationbar();
					var content = $("#" + tabId).children()[1];
					$(content).height($("#" + tabId).height() - 30);
					setTimeout(function() {
						$(content).height($("#" + tabId).height() - 30);
		            }, 220);
				}
				CFG_configInfo.CFG_navigationBar = $('#' + navigationId);
				if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_bodyOnLoad) {
					CFG_configInfo.childConfigInfo.CFG_bodyOnLoad();
				}
				if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_initReserveZones) {
					CFG_configInfo.childConfigInfo.CFG_initReserveZones();
					CFG_configInfo.childConfigInfo.CFG_navigationBar = $('#' + navigationId);
				}
			});
			$("#tabbarDiv").tabs("option", "active", "#" + tabId);
			$("#menuObj").data(datas.url, tabId);
		}
	}
	function tabClose(e, eventData) {
		$.each($("#menuObj").data(), function(i, v) {
			if (v == eventData.panelId) {
				$("#menuObj").data(i, null);
			}
		});
	}
	function CFG_addTab(CFG_configInfo, text, url) {
		var tabId = generateId("tab");
		/*$("#tabbarDiv").tabs("add", text, "", {
			"href" : tabId,
			"closeable" : true
		});*/
		$("#tabbarDiv").tabs("add", {
			label : text,
			content : "",
			href : "#" + tabId,
			closeable : true
		}).tabs("option", "active", "#" + tabId);
		$.ajax({
			url : url,
			dataType : "html",
			context : document.body
		}).done(function(html) {
			$("#" + tabId).data("selfUrl", url);
			$("#" + tabId).data("parentConfigInfo", CFG_configInfo);
			$("#" + tabId).append(html);
			$.parser.parse($("#" + tabId));
			if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_bodyOnLoad) {
				CFG_configInfo.childConfigInfo.CFG_bodyOnLoad();
			}
			if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_initReserveZones) {
				CFG_configInfo.childConfigInfo.CFG_initReserveZones();
			}
		});
		$("#tabbarDiv").tabs("option", "active", "#" + tabId);
	}
	$(function() {
		$("#menuObj").append("<ul id='coralmenu'></ul>");
		$.getJSON(contextPath + "/menu/menu!loadAppMenuForCoral40.json", function(json) {
			$("#coralmenu").menubar({
				data : json,
				onClick : menuClick
			});
		});
	});
</script>