<%@page import="com.ces.config.utils.StringUtil"%>
<%@ page import="com.ces.config.utils.SystemParameterUtil" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%

    String systemName = SystemParameterUtil.getInstance().getSystemParamValue("系统名称");
    String systemPicture = SystemParameterUtil.getInstance().getSystemParamValue("系统图标");
    if (StringUtil.isEmpty(systemName)) {
        systemName = "系统配置平台V1.0";
    }
	//设置企业编码的session
/*	request.getSession().setAttribute("_companyCode_", request.getParameter("companyCode"));*/
	request.getSession().setAttribute("_multi_", request.getParameter("multi"));
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<% if (StringUtil.isNotEmpty(systemPicture)) { %>
<link rel="shortcut icon" type="image/ico" href="<%=systemPicture%>" />
<% } %>
	<link href="cfg-resource/coral40/common/csgl/global.css" rel="stylesheet"/>
	<link href="cfg-resource/coral40/common/csgl/base.css" rel="stylesheet"/>
<title><%=systemName%></title>
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
        <div class="topbgfilter">
            <div class="topbarner clearfix">
                <div class="logo"></div>
                <div class="t-menu">
                    <div class="t-menu-mid">
                        <ul class="t-menu-ul">
                        </ul>
                    </div>
                </div>
                <div class="t-logout">
                    <div class="t-logout-input logout">
                        <input type="button" value="注销" onclick="logout()">
                    </div>
                </div>
            </div>
        </div>
        <div id="layoutCeterDiv" style="width: 100%; top: 44px; bottom: 62px; overflow: auto; position: absolute;">
            <div class = "left-menu">

            </div>
            <div id="menuContent" class="menuContent">

            </div>
        </div>
        <div class="footerbox">
            <div class="csglfooter clearfix">
                <div class="padding20">
                    <div class="clearfix">
                        <div class="f-float-left">
                            <p>上海中信信息发展股份有限公司 版权所有[2015]</p>
                        </div>
                        <div class="f-float-right">
                            <span class="top-help-telphone"><i class="icon-telphone"></i>全国统一客服热线：4007200100</span>
                            <span class="online-help"><a>在线客服</a><i class="textadd">|</i><a>社区</a></span>
                        </div>
                    </div>
                    <div class="clearfix">
                        <span class="online-help"><span>友情链接：</span><a>中华人民共和国商务部</a><i class="textadd">|</i><a>北京商务之窗</a><i class="textadd">|</i><a>天津商务之窗</a></span>
                    </div>
                </div>
            </div>
        </div>
	</ces:layout>
</body>
</html>
<script type="text/javascript">
	var menuTabbarSize = 0;
    var bandingComponent = "1", // 绑定构件
    	bandingUrl="0"; // 绑定url
	$(function() {
		initFirstMenu(systemId);
	});
	function initFirstMenu(parentMenuId) {//显示一级菜单
		var firstMenus = $.loadJson($.contextPath + "/csgl!getMenu.json?parentMenuId="+parentMenuId);
		for (var i in firstMenus) {
			var firstMenu = firstMenus[i];
			$(".t-menu-ul").append('<li><a onClick="showSecondMenu(\''+firstMenu.id+'\')">'+firstMenu.name+'</a></li>');
		}
	}

    function showSecondMenu(menuId){
        $(".left-menu").empty();
        var secondMenus = $.loadJson($.contextPath + "/csgl!getMenu.json?parentMenuId="+menuId);
        for(var i in secondMenus){
            $(".left-menu").append("<div class='secondMenu'>" +
                    "<a onclick='menuClick(\""+secondMenus[i].id+"\")'>"+
                    "<span class='menuImgSpan'>" +
                        "<div class='menuImg'></div></span>"+
                    "<span class='menuTextSapn'>"+secondMenus[i].name+"</span>"+
                    "</a>" +
                    "</div>");

        }
    }
	function menuClick (menuId) {
        $("#menuContent").empty();
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

        var tabId = "menuContent";
        CFG_openTab(tabId, name, url);

	}
	function CFG_openTab(tabId, name, url) {
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
				var content = $("#" + tabId).children("div")[1];
				$(content).height($("#" + tabId).height() - 31).addClass("coral-adjusted");
				activateTab();
				CFG_getEmbeddedInfo(CFG_configInfo);
			}
			if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_bodyOnLoad) {
				CFG_configInfo.childConfigInfo.CFG_bodyOnLoad();
			}
			if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_initReserveZones) {
				CFG_configInfo.childConfigInfo.CFG_initReserveZones();
			}
		});
	}
	/** 刷新所有的组件 */
    function activateTab() {
        //$.coral.refreshAllComponent();
        $(".ctrl-init-layout:visible").layout("refresh");
    }

    //注销
    function logout() {
        $.confirm("确定注销", function (r) {
            if (r) {
                window.location = '${ctx}/logout';
            }
        });
    }



		
		
</script>