<%@page import="com.ces.config.utils.*"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String menuTabSize = SystemParameterUtil.getInstance().getSystemParamValue("菜单标签页个数");
    if (StringUtil.isEmpty(menuTabSize)) {
        menuTabSize = "-1";
    }
    String systemName = SystemParameterUtil.getInstance().getSystemParamValue("系统名称");
    String systemPicture = SystemParameterUtil.getInstance().getSystemParamValue("系统图标");
    if (StringUtil.isEmpty(systemName)) {
        systemName = "系统配置平台V1.0";
    }
    String sessionId = request.getSession().getId();
    String currentUserId = CommonUtil.getCurrentUserId();
	//设置企业编码的session
	/*request.getSession().setAttribute("_companyCode_", request.getParameter("companyCode"));*/
	request.getSession().setAttribute("_multi_", request.getParameter("multi"));
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<% if (StringUtil.isNotEmpty(systemPicture)) { %>
<link rel="shortcut icon" type="image/ico" href="<%=systemPicture%>"></link>
<% } %>
<title><%=systemName%></title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<script type="text/javascript">
	var contextPath = "${ctx}";
	var systemId = '<%=request.getParameter("systemId")%>';
	var tabMaxSize = <%=menuTabSize%>;
	var jSessionId = '<%=sessionId%>';
	var currentUserId = '<%=currentUserId%>';
</script>
<%@include file="/cfg-resource/coral40/common/jsp/_cui_library.jsp" %>
</head>
<body onresize="" >
	<ces:layout id="mainLayout" fit="true">
		<ces:layoutRegion region="north" maxHeight="300" minWidth="1024" style="height:38px;">
			<jsp:include page="app_header_new.jsp">
				<jsp:param name="multi" value="${_multi_}"></jsp:param>
			</jsp:include>
			<%--<%@ include file="/WEB-INF/layouts/app_header_new.jsp"%>--%>
		</ces:layoutRegion>
		<ces:layoutRegion region="west" split="true" style="width:180px;" maxWidth="240" minWidth="200">
			<div id="layoutCeterDiv" style="height:100%;"></div>
		</ces:layoutRegion>
		<ces:layoutRegion region="center" minWidth="1024" >
			<ces:tabs id="tabbarDiv" cls="coral-tabs-bottom" heightStyle="fill" onTabClose="tabClose" onActivate="activateTab"><ul></ul></ces:tabs>
		</ces:layoutRegion>
	</ces:layout>
</body>
</html>
<script type="text/javascript"><!--
	var menuTabbarSize = 0;
    var bandingComponent = "1", // 绑定构件
    	bandingUrl="0"; // 绑定url
	$(function() {
		initQuickMenu();
		intiLayout();
		$(".left-menu").navigationmenu({
			textAlign: "left",
			onSelect: function(e , ui ){
				
			}
		});
		var companyCode = "<%=request.getParameter("companyCode")%>";
		if (systemId != null && systemId != "") {
			initQyda(systemId, companyCode);
		}
	});
	//初始化企业档案数据
	function initQyda(sysId, companyCode) {
		$.loadJson($.contextPath + "/trace!initQyda.json?sysId=" + sysId + "&companyCode=" + companyCode);
	}
	function initQuickMenu() {
		$.getJSON(contextPath + "/menu/menu!loadAppQuickMenuForCoral40.json?systemId="+systemId + "&P_timestamp=" + getTimestamp(), function(data) {
			if (typeof data == 'string') {
				data = eval("(" + data + ")");
			}
			$.each(data, function(i, quickMenu) {
			    if (!quickMenu) return;
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
	function intiLayout() {
		var pBody = $("#layoutCeterDiv");
		var mHtml = $.loadJson(contextPath + "/menu/menu!loadAppVerticalMenuForCoral40.json?systemId="+systemId + "&P_timestamp=" + getTimestamp());
		pBody.append(mHtml);
		$.parser.parse(pBody);
	}
	function menuClick (menuId) {
		var menu = $.loadJson(contextPath + "/menu/menu!getAppRootMenuById.json?id="+menuId + "&P_timestamp=" + getTimestamp());
		if ( bandingComponent == menu.bindingType) {
		   var componentVersion = $.loadJson(contextPath + "/menu/menu!getComponentVersionById.json?id="+menu.id + "&P_timestamp=" + getTimestamp());
              var url = "/cfg-resource/" + componentVersion.views + "/views/" + componentVersion.url;
              if (url.indexOf("?") == -1) {
                  url += "?bindingType=menu&menuId=" + menu.id + "&componentVersionId=" + componentVersion.id;
              } else {
                  url += "&bindingType=menu&menuId=" + menu.id + "&componentVersionId=" + componentVersion.id;
              }
              if ( menu.code != null && menu.code !="" ) {
                  url += "&menuCode=" + menu.code;
              }
              url += "&topComVersionId=" + componentVersion.id;
              if ("1" == menu.useNavigation ) {
                  url += "&useNavigation=1";
              }
              var menuParam = $.loadJson(contextPath + "/menu/menu!getParamsOfMenu.json?menuId="+menu.id + "&P_timestamp=" + getTimestamp())
              url += menuParam;
              menu.url = url;
          } else if ( bandingUrl == menu.bindingType ) {
              var url = menu.url;
              if ("1" == menu.useNavigation ) {
                  if (url.indexOf("?") == -1) {
                      url += "?useNavigation=1";
                  } else {
                      url += "&useNavigation=1";
                  }
              }
              if ( menu.code != null && menu.code !="" ) {
                  url += "&menuCode=" + menu.code;
              }
              menu.url = url;
          }
          loadBandingComponent(menu.name,menu.url);
	}
	function loadBandingComponent(name, url) {
		if (isEmpty(url)) {
			return;
		}
		if (url.charAt(0) != '/') {
			url = "/" + url;
    	}
    	if ($("#mainLayout").data(url)) {
    	    var tabId = $("#mainLayout").data(url);
            var currentTabId = $("#tabbarDiv").tabs("getIdByIndex", $("#tabbarDiv").tabs("option", "active"));
            if (currentTabId == tabId) {
                return;
            }
			$("#tabbarDiv").tabs("option", "active", "#" + tabId);
			$.confirm('是否刷新？', function(r) {
		        if (r) {
		            CFG_refreshTab(tabId, name, url);
		        }
		    });
		}else {
			if (tabMaxSize > 0 && menuTabbarSize >= tabMaxSize) {
			    CFG_message("最多能够打开" + tabMaxSize + "个菜单标签页！", "warning");
			    return;
			}
	    	var tabId = generateId("tab");
	    	$("#tabbarDiv").tabs("add", {
					label : name,
					content : "",
					href : "#" + tabId,
					closeable : name =='台账'?false:true
				}).tabs("option", "active", "#" + tabId);
	    	CFG_openTab(tabId, name, url);
			$("#tabbarDiv").tabs("option", "active", "#" + tabId);
			$("#mainLayout").data(url, tabId);
			menuTabbarSize++;
		}
	}
	function CFG_openTab(tabId, name, url) {
	    var navigationId = generateId("nav");
		$.ajax({
			url : contextPath + url,
			dataType : "html",
			context : document.body
		}).done(function(html) {
			var CFG_configInfo = {'tabId':tabId, 'name':name, 'url':url};
			$("#" + tabId).data("selfUrl", url);
			$("#" + tabId).data("parentConfigInfo", CFG_configInfo);
			$("#" + tabId).append(html);
			$.parser.parse($("#" + tabId));
			if (url.indexOf("useNavigation") != -1) {
				$("#" + tabId).prepend("<div id='" + navigationId + "'></div>");
				$('#' + navigationId).navigationbar();
				var content = $("#" + tabId).children("div")[1];
				$(content).height($("#" + tabId).height() - 31).addClass("coral-adjusted");
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
	}
	function CFG_refreshTab(tabId, name, url) {
	    CFG_emptyJQ($("#" + tabId));
        CFG_openTab(tabId, name, url);
	}
	function tabClose(e, eventData) {
		$.each($("#mainLayout").data(), function(i, v) {
			if (v == eventData.panelId) {
			    $("#mainLayout").removeData(i);
			}
		});
		menuTabbarSize--;
	}
	/** 刷新所有的组件 */
    function activateTab() {
        //$.coral.refreshAllComponent();
        $(".ctrl-init-layout:visible").layout("refresh");
    }
		
		
--></script>