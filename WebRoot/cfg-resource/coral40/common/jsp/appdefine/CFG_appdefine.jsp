<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String path = request.getContextPath();
   	String tableId = request.getParameter("tableId");
    String componentVersionId = request.getParameter("componentVersionId");
    String menuId = request.getParameter("menuId");
    String cgridDivId = request.getParameter("cgridDivId");
    String idSuffix = request.getParameter("idSuffix");
    request.setAttribute("tableId", (null == tableId ? "-1" : tableId));
    request.setAttribute("componentVersionId", (null == componentVersionId ? "-1" : componentVersionId));
    request.setAttribute("menuId", (null == menuId ? "-1" : menuId));
    request.setAttribute("cgridDivId", cgridDivId);
    request.setAttribute("idSuffix", idSuffix);
	request.setAttribute("namespaceId", "ns" + idSuffix);
%>

<div id="userDefineDiv${idSuffix}" class="fill">
	<div id="tabsDiv${idSuffix}" class="toolbarsnav clearfix"></div>
	<ces:form id="defineForm${idSuffix}" heightStyle="fill">
		<div id="defineTabs${idSuffix}">
			<ul><li><a href="#columnDefine${idSuffix}"><span>列表自定义</span></a></li>
			    <li><a href="#searchDefine${idSuffix}"><span>检索自定义</span></a></li>
			    <li><a href="#sortDefine${idSuffix}"><span>排序自定义</span></a></li>
			</ul>
			<div id="columnDefine${idSuffix}"></div>
			<div id="sortDefine${idSuffix}"></div>
			<div id="searchDefine${idSuffix}"></div>
		</div>
	</ces:form>
	<div class="btnOption">
		<ul>
			<li><ces:button cls="save" onClick="$.ns('${namespaceId}').save()" label="保存" /></li>
			<li><ces:button cls="cancel" onClick="$.ns('${namespaceId}').reset()" label="恢复默认值" /></li>
			<li><ces:button cls="cancel" onClick="$.ns('${namespaceId}').back2grid()" label="返回" /></li>
		</ul>
	</div>
</div>	
<script type="text/javascript">

$.extend($.ns("${namespaceId}"), {
	modify : false, // 是否修改标记
	//
	initDefault : function () {
		$.ns("${namespaceId}").modify = false;
		$.ns("${namespaceId}").rowIndex = 0;
		$.ns("${namespaceId}").colIndex = 0;
	},
	// 字段检索
	appColumnSearch : function(jqGrid, keyWord, prop) {
		var sData = [];
		sData["filters"] = {"groupOp" : "AND", "rules":[{"field": prop, "op": "cn", "data": keyWord}]};
		jqGrid.grid("option", "localonce", true);
		$.extend(jqGrid.grid("option", "postData"), sData);
		jqGrid.grid("reload");
	},
	// 字段检索
	columnSearch : function () {
		var jqGrid  = $("#lGrid${idSuffix}"),
		    keyWord = $("#keyWord${idSuffix}").textbox("getText");
		
		$.ns("${namespaceId}").appColumnSearch(jqGrid, keyWord, "showName");
	},
	// 清空页面元素
	emptyTab : function () {
		$("#searchDefine${idSuffix}").empty();
		$("#columnDefine${idSuffix}").empty();
		$("#sortDefine${idSuffix}").empty();
	},
	// 激活TAB页 
	beforeActivate : function (e, ui) {
		if (true === $.ns("${namespaceId}").modify) {
			$.confirm("配置未保存，您确定要离开吗？", function (sure) {
				if (!sure) return;
				$.ns("${namespaceId}").jqTabs.tabs("option", "beforeActivate", null);
				$.ns("${namespaceId}").jqTabs.tabs("option", "active", "#" + ui.newPanel[0].id);
				$.ns("${namespaceId}").switchTab(e, ui);
				$.ns("${namespaceId}").jqTabs.tabs("option", "beforeActivate", $.ns("${namespaceId}").beforeActivate);
			});
			return false;
		} else  {
			$.ns("${namespaceId}").switchTab(e, ui);
		}
		return true;
	},
	// TAB页切换
	switchTab : function (e, ui) {
		if (ui.newPanel[0].id == "columnDefine${idSuffix}") {
			$.ns("${namespaceId}").initTab("column");
		} else if (ui.newPanel[0].id == "sortDefine${idSuffix}") {
			$.ns("${namespaceId}").initTab("sort");
		} else if (ui.newPanel[0].id == "searchDefine${idSuffix}") {
			$.ns("${namespaceId}").initTab("search");
		}
		$.ns("${namespaceId}").initDefault();
	},
	// 初始化TAB页
	initTab : function (role) {
		var url = $.contextPath+"/cfg-resource/coral40/common/jsp/appdefine/CFG_app" + role + ".jsp?" +
				"tableId=${tableId}&componentVersionId=${componentVersionId}&menuId=${menuId}&idSuffix=${idSuffix}";
		$.ajax({
			type: "post",
			url: url,
			dataType: "html",
			success: function(html) {
				$.ns("${namespaceId}").emptyTab();
				var jqTab = $("#" + role + "Define${idSuffix}");
				jqTab.html(html);
				$.parser.parse(jqTab);
				$.ns("${namespaceId}").initRoleTab();
				$("#defineForm${idSuffix}").form("refresh");
				$("#defineTabs${idSuffix}").tabs("refresh");
				//refresh拉进来的页面的layout
				$("#layoutId${idSuffix}").layout("refresh");
				if (role == "column") {
					$.ns("${namespaceId}").initCheckBox();
				}
			}
		});
	},
	// 初始化字段TAB页
	initRoleTab : function() {
		$.ns("${namespaceId}").initLGrid();
		$.ns("${namespaceId}").initRGrid();
		$.ns("${namespaceId}").initDefault();
	},
	// 工具事件
	clickTbar : function (event, ui) {
		if ("save" === ui.id) {
			$.ns("${namespaceId}").save();
		} else if ("reset" === ui.id) {
			$.ns("${namespaceId}").reset();
		} else { // "back2grid" === ui.id
			$.ns("${namespaceId}").back2grid();
		}
	},
	// 返回列表
	back2grid : function() {
		if (!window.CFG_clickReturnButton) return;
		if ($.ns("${namespaceId}").modify) {
			$.confirm("配置未保存，确定要返回吗？", function (sure) {
				if (!sure) return;
				CFG_clickReturnButton($("#userDefineDiv${idSuffix}").parent().data("parentConfigInfo"));
				$("#${cgridDivId}").cgrid("refresh");
				$("#${cgridDivId}").cgrid("reload");
			})
		} else {
			CFG_clickReturnButton($("#userDefineDiv${idSuffix}").parent().data("parentConfigInfo"));
			$("#${cgridDivId}").cgrid("refresh");
			$("#${cgridDivId}").cgrid("reload");
		}
	},
	// 移动所有记录
	moveAll : function (fromId, toId) {
		//获取当前被选中的行
		var fGrid = $("#" + fromId),
		    tGrid = $("#" + toId),
		    allData = fGrid.grid("getRowData");
		if (0 === allData.length) {
			return;
		}
		var i = 0, len = allData.length, data;
		for (i = 0; i < len; i++) {
			data = allData[i];
			tGrid.grid("addRowData", data.id, data); 
		}	
		//销毁当前被移动的行
		fGrid.grid("clearGridData");
	},
	// 移动选中的记录
	moveSelected : function (fromId, toId) {
		//获取当前被选中的行
		var fGrid = $("#" + fromId),
		    tGrid = $("#" + toId),
		    idArr = fGrid.grid("option", "selarrrow");
		if (0 === idArr.length) {
			$.message({message:"请选择字段！", cls:"warning"});
			return;
		}
		var i = 0, len = idArr.length, data;
		for (i = 0; i < len; i++) {
			data = fGrid.grid("getRowData", idArr[i]);
			tGrid.grid("addRowData", data.id, data); 
		}
		//删除当前被移动的行
		for (i = len - 1; i > -1; i--) {
			fGrid.grid("delRowData", idArr[i]);
		}
	},
	// 字段左移、右移
	move : function(target) {
		var lId = "lGrid${idSuffix}",
		    rId = "rGrid${idSuffix}";
		    
		$.ns("${namespaceId}").storeEdit();

		if ("allToLeft" === target) {
			$.ns("${namespaceId}").moveAll(rId, lId);
		} else if ("allToRight" === target) {
			$.ns("${namespaceId}").moveAll(lId, rId);
		} else if ("toRight" === target) {
			$.ns("${namespaceId}").moveSelected(lId, rId);
		} else if ("toLeft" === target) {
			$.ns("${namespaceId}").moveSelected(rId, lId);
		}
		$.ns("${namespaceId}").modify = true;
	},
	// 保存并取消grid的编辑状态
	saveEdit : function(targetId) {
		var editrow = $("#" + targetId).grid("option", "editrow");
	   	$("#" + targetId).grid("saveRow", editrow, null, "clientArray");
		
		$("#" + targetId).grid("saveCell", $.ns("${namespaceId}").rowIndex, $.ns("${namespaceId}").colIndex);
	},
	// 取消grid的编辑状态
	storeEdit : function(targetId) {
		$("#" + targetId).grid("restoreCell", $.ns("${namespaceId}").rowIndex, $.ns("${namespaceId}").colIndex);
	},
	// 列表编辑
	beforeEditCell : function (e, ui){
	    $.ns("${namespaceId}").rowIndex = ui.rowIndex;
	    $.ns("${namespaceId}").colIndex = ui.cellIndex;
		$.ns("${namespaceId}").modify = true;
	}
});
	
	
	
$(function() {
	var configInfo = CFG_initConfigInfo({
		/** 页面名称 */
		"page" : "userDefine.jsp",
		/** 页面中的最大元素 */
		"maxEleInPage" : $("#userDefineDiv${idSuffix}"),
		/** 获取构件嵌入的区域 */
		"getEmbeddedZone" : function() {
			return $("#userDefineDiv${idSuffix}");
		},
		/** 初始化预留区 */
		"initReserveZones" : function(configInfo) {
			//CFG_addToolbarButtons(configInfo, $("#toolbarId${idSuffix}"), "toolBarReserve", $("#toolbarId${idSuffix}").toolbar("getLength")-1);
		},
		/** 获取返回按钮添加的位置 */
		"setReturnButton" : function(configInfo) {
			CFG_setReturnButton(configInfo, $("#toolbarId${idSuffix}"));
		},
		/** 页面初始化的方法 */
		"bodyOnLoad" : function(configInfo) {
			// 初始化界面
			$.ns("${namespaceId}").jqTabs = $("#defineTabs${idSuffix}");
			$.ns("${namespaceId}").jqTabs.tabs({
					heightStyle : "fill", 
					beforeActivate : $.ns("${namespaceId}").beforeActivate,
					onActivate: $.noop
				});
			// 初始化字段定义
			$.ns("${namespaceId}").initTab("column");
		}
	});
	if (configInfo.parentConfigInfo && configInfo.parentConfigInfo.CFG_navigationBar) {
	    configInfo.CFG_navigationBar = configInfo.parentConfigInfo.CFG_navigationBar;
	}
});
</script>
