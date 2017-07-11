<%@page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
String processInstanceId = request.getParameter("P_processInstanceId");
%>
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<!-- ie7 mode -->
<title>流程跟踪</title>
<style type="text/css">
v\:RoundRect, v\:line, v\:textbox, v\:shadon, v\:stroke {
	behavior: url(#default#VML);
	position: absolute;
}

.coflow-tip-box {
	background: #FFE1C4; 
	border-bottom: black 1pt solid; 
	border-left: black 1pt solid; 
	border-right: black 1pt solid; 
	border-top: black 1pt solid;  
	position: absolute; 
	width: 200px;
	filter:Alpha(Opacity=70,style=1,Finishopacity=80);
	z-index:100;
}

.coflow-tip-box table {
	FONT-FAMILY: "simsun", arial; 
	FONT-SIZE: 12px;
}

.coflow-tip-box table td {
	height: 18px;
}
</style>
<%@include file="/cfg-resource/coral40/common/jsp/_cui_library.jsp" %>
<script type="text/javascript">
	var baseUrl = $.contextPath + "/appmanage/show-module";
	var isshowtip = false;
	var pid = "<%=processInstanceId%>";

	var setting = {
			colNames: ["任务名称", "流程名称", "操作者", "操作名称", "操作时间", "备注"],
			colModel: [
			    {name: "activityName", width: 200},
			    {name: "processNmae", width: 200},
			    {name: "performerName", width: 200},
			    {name: "actionName", width: 200},
			    {name: "actionTime", width: 200},
			    {name: "note", width: 400}],
			fitStyle: "fill",
			shrinkToFit: true,
			forceFit: true
	};
	function doOnLoad() {
		tsaiSetDisplayNone();
		var monitor = "";
		if($.browser.msie && $.browser.version < 9) {
			//ie5-8 use it
			monitor = vmlMonitor(pid);
		} else {
			//ie9+ other browser use it
			monitor = svgMonitor(pid);
		}
		$("#track${idSuffix}").append(monitor);
		setting.url = baseUrl + "!coflowTrack.json?P_op=logs&P_processInstanceId=" + pid + "&F_in=activityName,processNmae,performerName,actionName,actionTime,note";
		// 初始化列表
		$("#grid${idSuffix}").grid(setting);
	}
	
	function svgMonitor(id){
		var graphHtml = "";
		var url = baseUrl + "!coflowTrack.xml?P_op=svgGraph&P_processInstanceId=" + id;
		$.ajax({
			url : url,
			type: "get",
			dataType : "text",
			async : false,
			success : function(text) {
				graphHtml = text.replace("<String>", "").replace("</String>", "").replace(/&lt;/g, "<");;
			},
			error : function (req, error, errThrow) {
				$.error("function load json error: " + error);
			}
		});
		return graphHtml;
	}
	
	function vmlMonitor(id){
		var graphHtml = "";
		var url = baseUrl + "!coflowTrack.xml?P_op=vmlGraph&P_processInstanceId=" + id;
		$.ajax({
			url : url,
			type: "get",
			dataType : "text",
			async : false,
			success : function(text) {
				graphHtml = text.replace("<String>", "").replace("</String>", "").replace(/&lt;/g, "<");;
			},
			error : function (req, error, errThrow) {
				$.error("function load json error: " + error);
			}
		});
		return graphHtml;
	}
	
	function loadLog(id){
		var url = baseUrl + "!coflowTrack.json?P_op=logs&P_activityInstanceId=" + id + "&F_in=activityName,processNmae,performerName,actionName,actionTime,note";
		var jq = $("#grid${idSuffix}");
		jq.grid("option", "url", url);
		jq.grid("reload");
	}


	function tsaiSetDisplayNone() {
		$("#tipBox").hide();
	}

	function showtip(a, b, name, status, cusers, ousers, beginTime, endTime,
			limitTime, chuanchuCount) {
		var chuanchuStr = "否";
		if (chuanchuCount > 0) {
			chuanchuStr = "是";
		}
		var jq = $(a);
		var tipJQ = $("#tipBox");
		var chuanyueBody = "<tr><td nowrap>　是否传阅：</td><td >" + chuanchuStr
				+ "</td></tr>";
		var sMsg = "";
		if (b == 1) {
			sMsg = ("<table border=0 width=200px  cellspacing=0 cellpadding=0>  <tr>    <td width=17% height=18>　节点名称：</td>    <td >"
					+ name
					+ "</td>  </tr>  <tr>    <td >　当前状态：</td>    <td >"
					+ status
					+ "</td>  </tr> "
					+ chuanyueBody
					+ " <tr>    <td nowrap>当前处理人：</td>    <td >"
					+ cusers
					+ "</td>  </tr>  <tr>    <td >　已处理人：</td>    <td >"
					+ ousers
					+ " </td>  </tr>  <tr>    <td >　开始时间：</td>    <td >"
					+ beginTime
					+ "</td>  </tr><tr><td>　结束时间：</td><td>"
					+ endTime
					+ "</td></tr><tr><td>　办理期限：</td><td>"
					+ limitTime
					+ "</td></tr></table>"
					+ "<font color = 8080FF>子流程的详细信息,请</font><font color=red>点击</font><font color = 8080FF>查看</font> ");
		} else if (b == 2) {
			sMsg = ("<table border=0 width=200px  cellspacing=0 cellpadding=0>  <tr>    <td width=17% height=16>　节点名称：</td>    <td >"
					+ name
					+ "</td>  </tr>  <tr>    <td >　当前状态：</td>    <td >"
					+ status
					+ "</td>  </tr> "
					+ chuanyueBody
					+ " <tr>    <td nowrap>当前处理人：</td>    <td >"
					+ cusers
					+ "</td>  </tr>  <tr>    <td >　已处理人：</td>    <td >"
					+ ousers
					+ " </td>  </tr>  <tr>    <td >　开始时间：</td>    <td >"
					+ beginTime
					+ "</td>  </tr><tr><td>　结束时间：</td><td>"
					+ endTime
					+ "</td></tr><tr><td>　办理期限：</td><td>"
					+ limitTime
					+ "</td></tr></table>"
					+ "<font color = 8080FF>父流程的详细信息,请</font><font color=red>点击</font><font color = 8080FF>查看</font> ");
		} else {
			sMsg = ("<table border=0 width=200px  cellspacing=0 cellpadding=0>  <tr>    <td width=17% height=16>　节点名称：</td>    <td >"
					+ name
					+ "</td>  </tr>  <tr>    <td >　当前状态：</td>    <td >"
					+ status
					+ "</td>  </tr> "
					+ chuanyueBody
					+ " <tr>    <td nowrap>当前处理人：</td>    <td >"
					+ cusers
					+ "</td>  </tr>  <tr>    <td >　已处理人：</td>    <td >"
					+ ousers
					+ " </td>  </tr>  <tr>    <td >　开始时间：</td>    <td >"
					+ beginTime
					+ "</td>  </tr><tr><td>　结束时间：</td><td>"
					+ endTime
					+ "</td></tr><tr><td>　办理期限：</td><td>"
					+ limitTime
					+ "</td></tr></table>");
		}
		var top = jq.offset().top + 60;
		var left = jq.offset().left + 50;
		tipJQ.html(sMsg);
		tipJQ.css({top: top, left: left});
		tipJQ.show();
	}
	
	function clickTbar(e, ui) {
		if ("close" == ui.id) window.close();
	}
</script>
</head>
<body onload="doOnLoad()">
<div id="tipBox" class="coflow-tip-box"></div>
<div class="toolbarsnav clearfix">
<ces:toolbar id="tbar${idSuffix}" onClick="clickTbar"
					     data="[{'label':'关闭','id':'close','type':'button'}]"></ces:toolbar>
</div>
<ces:layout id="lout${idSuffix}" fit="true">
	<ces:layoutRegion region="north" split="true" title="流程跟踪" style="height:320px;">
		<div id="track${idSuffix }" style="height:100%"></div>
	</ces:layoutRegion>
	<ces:layoutRegion region="center" title="流程日志">
		<div id="grid${idSuffix }"></div>
	</ces:layoutRegion>
</ces:layout>
</body>
</html>
<script type="text/javascript">
	//$.extend($.ns("ns${idSuffix}"), {});
	//$(function() {
	//	var configInfo = CFG_initConfigInfo({
	//		/** 页面名称 */
	//		"page" : "graph.jsp",
	//		/** 页面中的最大元素 */
	//		"maxEleInPage" : $("body"),
	//		/** 获取构件嵌入的区域 */
	//		"getEmbeddedZone" : function() {
	//			return $("body");
	//		},
	//		/** 初始化预留区 */
	//		"initReserveZones" : function(configInfo) {
	//			//CFG_addToolbarButtons(configInfo, $("#tbar${idSuffix}"), "toolBarReserve", $("#toolbarId${idSuffix}").toolbar("getLength"));
	//		},
	//		/** 获取返回按钮添加的位置 */
	//		"setReturnButton" : function(configInfo) {
	//		    //CFG_setCloseButton(configInfo, $("#tbar${idSuffix}"));
	//		},
	//		/** 获取关闭按钮添加的位置 */
	//		"setCloseButton" : function(configInfo) {
	//			//CFG_setCloseButton(configInfo, $("#tbar${idSuffix}"));
	//		},
	//		/** 页面初始化的方法 */
	//		"bodyOnLoad" : function(configInfo) {
	//			//alert(1);
	//		}
	//	});
	//});//*/
</script>