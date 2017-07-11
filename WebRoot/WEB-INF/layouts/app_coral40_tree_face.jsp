<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>

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
	var systemId = '<%=request.getParameter("systemId")%>';
</script>
<%@include file="/cfg-resource/coral40/common/jsp/_cui_library.jsp" %>
</head>
<body onresize="" >
	<ces:layout id="mainLayout" fit="true">
		<ces:layoutRegion region="north" maxHeight="300" minWidth="1024" style="height:38px;">
			<%@ include file="/WEB-INF/layouts/app_header_new.jsp"%>
		</ces:layoutRegion>
		<ces:layoutRegion region="center" minWidth="1024" >
			<ces:layout id="mainLayout" fit="true">
				<ces:layoutRegion region="west"  style="width:260px;">
					<div id="layoutCeterDiv" style="height:100%;"></div>
				</ces:layoutRegion>
				<ces:layoutRegion region="center">
					<ces:tabs id="tabbarDiv" cls="coral-tabs-bottom" heightStyle="fill" onTabClose="tabClose" onActivate="activateTab"><ul></ul></ces:tabs>
				</ces:layoutRegion>
			</ces:layout>
		</ces:layoutRegion>
	</ces:layout>
</body>
</html>
<script type="text/javascript"><!--
    var bandingComponent = "1", // 绑定构件
    	bandingUrl="0"; // 绑定url
	$(function() {
		initQuickMenu();
		intiLayout();
	});
	function initQuickMenu() {
		$.getJSON(contextPath + "/menu/menu!loadAppQuickMenuForCoral40.json?systemId="+systemId, function(data) {
			if (typeof data == 'string') {
				data = eval("(" + data + ")");
			}
			$.each(data, function(i, quickMenu) {
				if (quickMenu.quickIcon && isNotEmpty(quickMenu.quickIcon)) {
					if (quickMenu.url) {
						$('.headMid .headMid_c .headul').append('<li class="three" style="background-image: url(${ctx}/cfg-resource/coral40/_cui_library/css/arch/images/menuicon/quickicon/'+quickMenu.quickIcon+')" onclick="loadBandingComponent(\''+quickMenu.name+'\',\''+quickMenu.url+'\')">'+quickMenu.name+'</li>');
					}else {
						$('.headMid .headMid_c .headul').append('<li class="three" style="background-image: url(${ctx}/cfg-resource/coral40/_cui_library/css/arch/images/menuicon/quickicon/'+quickMenu.quickIcon+')">'+quickMenu.name+'</li>');
					}
				} else {
					if(quickMenu.url) {
						$('.headMid .headMid_c .headul').append('<li class="three" onclick="loadBandingComponent(\''+quickMenu.name+'\',\''+quickMenu.url+'\')">'+quickMenu.name+'</li>');
					}else {
						$('.headMid .headMid_c .headul').append('<li class="three">'+quickMenu.name+'</li>');
					}
				}
				var quickMenucontent = '<div class="headmenu clearfix">';
				quickMenucontent += '</div>';
				$('.menu_b').append(quickMenucontent);
			});
		});
		
	}
	function onActivate(e, ui) {
		var menuId = ui.newHeader[0].id, divId = ui.newPanel[0].id ;
		var name = $("#"+menuId).val();
		initMenuTree(menuId, divId);
	}
	function intiLayout() {
		var pBody = $("#layoutCeterDiv");
		var mHtml = $.loadJson(contextPath + "/menu/menu!loadAppMenuAccordionForCoral40.json?systemId="+systemId);
		pBody.append(mHtml);
		$.parser.parse(pBody);
		var iMenuId = $(".accordionChildH3_0")[0].id, iDivId = $(".accordionChild_0")[0].id;
		initMenuTree(iMenuId, iDivId);
	}
	 function initMenuTree (menuId, divId) {
	    var pBody = $("#" +divId);
	    if(pBody.html()!=""){
	    	return;
	    }
	    rNode  = null, 
	    outerDiv = $("<div class=\"left_tree_bg fill\"></div>").appendTo(pBody),
	    treeDiv  = $("<div> </div>").appendTo(outerDiv);
    	var uiTree = $("<ul id=\"uiMenuTree_"+divId+"\" class=\"outlook-tree\"></ul>").appendTo(treeDiv);
    	var menu = $.loadJson(contextPath + "/menu/menu!getAppRootMenuById.json?id="+menuId);
    	var zNodes = [{name:menu.name , id:menuId, isParent:true, type: "0"}];
		var setting = {
				asyncAutoParam : "id",
				asyncUrl  : getMenuTreeUrl(),
				asyncEnable : true,
				asyncType : "post", 
				onClick : function (e, treeId, treeNode) {
					menuTreeClick(e, treeId, treeNode );
				},
				addDiyDom : addDiyDom,
				cls : "outlook-tree"
			};
		
		uiTree.tree(setting, zNodes);
		// 获取根节点
		rNode = uiTree.tree("getNodeByParam", "id", zNodes[0].id);
		// 展开第一层节点
		uiTree.tree("expandNode", rNode, true);
	}
	
	function getMenuTreeUrl () {
		return (contextPath + "/menu/menu!getAppMenuTreeOfAccordionForCoral40.json?systemId="+systemId
				+ "?E_frame_name=coral"
				+ "&E_model_name=tree" 
				+ "&F_in=name,id,hasChild,code,parentId,bindingType,componentVersionId,url,useNavigation" 
				+ "&P_filterId=parentId"
				+ "&P_ISPARENT=hasChild"
	            + "&P_orders=showOrder");
	}
	function menuTreeClick (e, treeId, treeNode) {
		if ( bandingComponent == treeNode.bindingType) {
		   var componentVersion = $.loadJson(contextPath + "/menu/menu!getComponentVersionById.json?id="+treeNode.id);
              var url = "/cfg-resource/" + componentVersion.views + "/views/" + componentVersion.url;
              if (url.indexOf("?") == -1) {
                  url += "?bindingType=menu&menuId=" + treeNode.id + "&componentVersionId=" + componentVersion.id;
              } else {
                  url += "&bindingType=menu&menuId=" + treeNode.id + "&componentVersionId=" + componentVersion.id;
              }
              if ( treeNode.code != null && treeNode.code !="" ) {
                  url += "&menuCode=" + treeNode.code;
              }
              if ("1" == treeNode.useNavigation ) {
                  url += "&useNavigation=1";
              }
              var menuParam = $.loadJson(contextPath + "/menu/menu!getParamsOfMenu.json?menuId="+treeNode.id)
              url += menuParam;
              treeNode.url = url;
          } else if ( bandingUrl == treeNode.bindingType ) {
              var url = treeNode.url;
              if ("1" == treeNode.useNavigation ) {
                  if (url.indexOf("?") == -1) {
                      url += "?useNavigation=1";
                  } else {
                      url += "&useNavigation=1";
                  }
              }
              if ( treeNode.code != null && treeNode.code !="" ) {
                  url += "&menuCode=" + treeNode.code;
              }
              treeNode.url = url;
          }
          loadBandingComponent(treeNode.name,treeNode.url);
	}
	function loadBandingComponent(name, url) {
		if (isEmpty(url)) {
			return;
		}
		if (url.charAt(0) != '/') {
			url = "/" + url;
    	}
    	if ($("#mainLayout").data(url)) {
			$("#tabbarDiv").tabs("option", "active", "#" + $("#mainLayout").data(url));
		}else {
	    	var tabId = generateId("tab");
	    	$("#tabbarDiv").tabs("add", {
					label : name,
					content : "",
					href : "#" + tabId,
					closeable : name =='台账'?false:true
				}).tabs("option", "active", "#" + tabId);
	    	
	    	$.ajax({
				url : contextPath + url,
				dataType : "html",
				context : document.body
			}).done(function(html) {
				var CFG_configInfo = {};
				var navigationId = generateId("nav");
				$("#" + tabId).empty();
				$("#" + tabId).data("selfUrl", url);
				$("#" + tabId).data("parentConfigInfo", CFG_configInfo);
				$("#" + tabId).append(html);
				$.parser.parse($("#" + tabId));
				if (url.indexOf("useNavigation") != -1) {
					$("#" + tabId).prepend("<div id='" + navigationId + "'></div>");
					$('#' + navigationId).navigationbar();
					var content = $("#" + tabId).children("div")[1];
					$(content).height($("#" + tabId).height() - 31);
					activateTab();
					CFG_configInfo.CFG_navigationBar = $('#' + navigationId);
					CFG_getEmbeddedInfo(CFG_configInfo);
					$('#' + navigationId).addNavigation({'name':name, 'index':0, 'configInfo':CFG_configInfo.childConfigInfo});
				}
				if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_bodyOnLoad) {
					CFG_configInfo.childConfigInfo.CFG_bodyOnLoad();
				}
				if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_initReserveZones) {
					CFG_configInfo.childConfigInfo.CFG_initReserveZones();
				}
				if (CFG_configInfo.childConfigInfo && CFG_configInfo.CFG_navigationBar) {
					CFG_configInfo.childConfigInfo.CFG_navigationBar = $('#' + navigationId);
				}
			});
			$("#tabbarDiv").tabs("option", "active", "#" + tabId);
			$("#mainLayout").data(url, tabId);
		}
	}
	function tabClose(e, eventData) {
		$.each($("#mainLayout").data(), function(i, v) {
			if (v == eventData.panelId) {
				$("#mainLayout").data(i, null);
			}
		});
	}
	/** 刷新所有的组件 */
    function activateTab() {
        $.coral.refreshAllComponent();
        $("div.ctrl-init-layout").layout("refresh");
    }
		
		
--></script>