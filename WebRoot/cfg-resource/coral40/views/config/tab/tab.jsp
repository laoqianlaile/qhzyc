<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String tabPosition = request.getParameter("tabPosition");
	String activeIndex = request.getParameter("activeIndex");
	if (activeIndex == null || "".equals(activeIndex)) {
	    activeIndex = "0";
	}
	String tPosition = "top";
	if ("2".equals(tabPosition)) {
	    tPosition = "bottom";
	} else if ("3".equals(tabPosition)) {
	    tPosition = "left";
	} else if ("4".equals(tabPosition)) {
	    tPosition = "right";
	}
	request.setAttribute("tPosition", tPosition);
	request.setAttribute("activeIndex", activeIndex);
	request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="max${idSuffix}" class="fill">
	<ces:layout id="layoutId${idSuffix}" name="" style="width:800px;height:600px;" fit="true">
		<ces:layoutRegion region="center" minWidth="1024" >
			<ces:tabs id="tabbarDiv${idSuffix}" cls="coral-tabs-${tPosition}" heightStyle="fill" onActivate="$.ns('namespaceId${idSuffix}').activateTab"><ul></ul></ces:tabs>
		</ces:layoutRegion>
	</ces:layout>
</div>
<script type="text/javascript">
	$.extend($.ns("namespaceId${idSuffix}"), {
	    activateTab : function() {
	        $.coral.refreshAllComponent("#max${idSuffix}");
	        $("#max${idSuffix}").find(".ctrl-init-layout").layout("refresh");
	        //$(".ctrl-init-layout:visible").layout("refresh");
	    }
	});
	$(function() {
		var configInfo = CFG_initConfigInfo({
			/** 页面名称 */
			'page' : 'tab.jsp',
			/** 页面中的最大元素 */
			'maxEleInPage' : $('#max${idSuffix}'),
			/** 获取构件嵌入的区域 */
			'getEmbeddedZone' : function() {
				return $('#layoutId${idSuffix}').layout('panel', 'center');
			},
			/** 初始化预留区 */
			'initReserveZones' : function(configInfo) {
				CFG_addTabbar(configInfo, "tabbar", $('#tabbarDiv${idSuffix}'), '4', $.ns("namespaceId${idSuffix}"));
				var tabSize = $('#tabbarDiv${idSuffix}').tabs("getAllTabId").length;
				var activeIndex = ${activeIndex};
				if (activeIndex >= tabSize) {
					$('#tabbarDiv${idSuffix}').tabs("option", "active", 0);
				} else {
					$('#tabbarDiv${idSuffix}').tabs("option", "active", activeIndex);
				}
			},
			/** 获取返回按钮添加的位置 */
			'setReturnButton' : function(configInfo) {
			},
			/** 获取关闭按钮添加的位置 */
			'setCloseButton' : function(configInfo) {
			},
			/** 页面初始化的方法 */
			'bodyOnLoad' : function(configInfo) {
				configInfo.tabId = "tabbarDiv${idSuffix}";
			}
		});
	});
</script>
