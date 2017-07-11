<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>

<%
	String tableId = request.getParameter("tableId");
	String componentVersionId = request.getParameter("componentVersionId");
	String menuId = request.getParameter("menuId");
    String cgridDivId = request.getParameter("cgridDivId");
	String idSuffix = request.getParameter("idSuffix");
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String params = "?E_model_name=jqgrid&P_tableId=" + tableId + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + menuId;
	request.setAttribute("lUrl", path + "/appmanage/app-export!defaultColumn.json" + params);
	request.setAttribute("rUrl", path + "/appmanage/app-export!defineColumn.json"  + params);
	request.setAttribute("idSuffix", idSuffix);
	request.setAttribute("tableId", tableId);
	request.setAttribute("componentVersionId", componentVersionId);
	request.setAttribute("menuId", menuId);
	request.setAttribute("namespaceId", "ns" + idSuffix);
%>
	<div id="userDefineDiv${idSuffix}" class="fill">
	<ces:layout id="layoutId${idSuffix}" name="layout" fit="true" style="width:auto;height:auto;" >
	<ces:layoutRegion region="north" split="true"  style="width:300px;padding:0px;height:90px">
		<div class="toolbarsnav clearfix">
			<ces:toolbar id="toolbarId${idSuffix}" 
			             onClick="$.ns('${namespaceId}').clickTbar"
					     data="[{'label':'保存','id':'save','type':'button'},{'label':'返回','id':'back2grid','type':'button'}]"
					>
			</ces:toolbar>
		</div>
		<div style="margin-top: 4px;margin-bottom: 2px;">
			<span class="search clearfix">
				<ces:input required="false" icons="icon-search3 jQuery.ns('${namespaceId}').columnSearch" onKeyUp="$.ns('${namespaceId}').columnSearch"
				  width="300" id="keyWord${idSuffix}" name="keyWord" value="" placeholder="字段检索（支持拼音）"/>
			<ces:checkbox id="check${idSuffix}" onChange="$.ns('${namespaceId}').onChangeBox()" name="check" label="显示系统字段" ></ces:checkbox>
			</span>
		</div>
		
	</ces:layoutRegion>
	<ces:layoutRegion region="west" split="true" style="height:100%;width:550px;" maxWidth="600" minWidth="200">
		<div id="lGrid${idSuffix}" ></div>
	</ces:layoutRegion>
	<ces:layoutRegion region="center" split="true"  style="padding:0px;">
		<div class="fill coral-adjusted">
			<ces:layout id="searchLayout2${idSuffix}" fit="true" style="position:relative;">
				<ces:layoutRegion region="west" split="true" style="width:120px;">
					<div style="align:center;margin-top:100px;">
						<p align="center" style="margin:10px;">
							<ces:button id="allToRight${idSuffix}" label=" >> " onClick="jQuery.ns('${namespaceId}').move('allToRight')"  width="80"></ces:button>
						</p>
						<p align="center" style="margin:10px;">
							<ces:button id="toRight${idSuffix}" label=" > " onClick="jQuery.ns('${namespaceId}').move('toRight')" width="80"></ces:button>
						</p>
						<p align="center" style="margin:10px;">
							<ces:button id="toLeft${idSuffix}" label=" < " onClick="jQuery.ns('${namespaceId}').move('toLeft')" width="80"></ces:button>
						</p>
						<p align="center" style="margin:10px;">
							<ces:button id="allToLeft${idSuffix}" label=" << " onClick="jQuery.ns('${namespaceId}').move('allToLeft')" width="80"></ces:button>
						</p>
					</div>
				</ces:layoutRegion>
				<ces:layoutRegion region="center" split="true">
					<div id="rGrid${idSuffix}" style="border-left: 1px solid #BEBEBE;"></div>
				</ces:layoutRegion>
			</ces:layout>
		</div>
	</ces:layoutRegion> 
	<ces:layoutRegion region="south" style="padding:0px;">
		<%--<div class="btnOption">--%>
			<%--<ul>--%>
				<%--<li><ces:button cls="save" onClick="$.ns('${namespaceId}').save()" label="保存" /></li>--%>
				<%--<li><ces:button cls="cancel" onClick="$.ns('${namespaceId}').back2grid()" label="返回" /></li>--%>
			<%--</ul>--%>
		<%--</div>--%>
	</ces:layoutRegion>
  </ces:layout>
</div>

<script type="text/javascript">
var lUrl = "${lUrl}" + "&P_columnType=0";
var rUrl = "${rUrl}" + "&P_columnType=0";

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
			})
		} else {
			CFG_clickReturnButton($("#userDefineDiv${idSuffix}").parent().data("parentConfigInfo"));
			$("#${cgridDivId}").cgrid("refresh");
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
		//console.log("target: " + target);
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
	// 取消grid的编辑状态
	storeEdit : function(targetId) {
		$("#" + targetId).grid("restoreCell", $.ns("${namespaceId}").rowIndex, $.ns("${namespaceId}").colIndex);
	},
	// 列表编辑
	beforeEditCell : function (e, ui){
	    $.ns("${namespaceId}").rowIndex = ui.rowIndex;
	    $.ns("${namespaceId}").colIndex = ui.cellIndex;
		$.ns("${namespaceId}").modify = true;
	},
	// 初始化左边列表
	initLGrid : function () {
		var jqGrid = $("#lGrid${idSuffix}"),
		    setting= {
					width:"400",
					height : "auto",
					colModel : [
						{name: "id", width: "auto", hidden: true},
						{name: "columnId", width: "auto", hidden: true},
						{name: "showName", width: 150, align: "center"},
						{name: "columnName", width: "auto", hidden: true}
					],
					colNames : ["ID","字段Id","表头名称","字段名称"],
					rowNum : "99999",
					multiselect : true,
					url:lUrl,
					fitStyle:"fill" 
				};
		jqGrid.grid(setting);
	},
	// 初始化右边列表
	initRGrid : function () {
		var jqGrid = $("#rGrid${idSuffix}"),
			setting = {
				width:"auto",
				height : "auto",
				colModel : [
					{name: "id", width: "auto", hidden: true},
					{name: "columnId", width: "auto", hidden: true},
					{name: "showName", width: 150, align: "center"},
					{name: "columnName", width: "auto", hidden: true}
				],
				colNames : ["ID","字段Id","表头名称","字段名称"],
				rowNum : "99999",
				multiselect : true,
				url:rUrl,
				beforeEditCell: "$.ns('${namespaceId}').beforeEditCell",
				fitStyle: "fill" 
			};
		jqGrid.grid(setting);
		jqGrid.grid("sortableRows", {
			cursor: ".handle",
			start: function (event, ui) { },
			update: function(event, ui){
				jQuery.ns("${namespaceId}").modify = true;	
			}
		});
	},
	// 显示系统字段
	onChangeBox : function(){
		if (lUrl == "${lUrl}") {
			lUrl = lUrl + "&P_columnType=0";
			rUrl = rUrl + "&P_columnType=0";
		} else {
			lUrl = "${lUrl}";
			rUrl = "${rUrl}";
		}
		$("#lGrid${idSuffix}").grid("option","url",lUrl);
		$("#rGrid${idSuffix}").grid("option","url",rUrl);
		$("#lGrid${idSuffix}").grid("reload");
		$("#rGrid${idSuffix}").grid("reload");
		$.ns("${namespaceId}").initDefault();
	},
	// 获取配置信息
	getRowsValue : function() {
		
		var rowsValue = "", rowData = $("#rGrid${idSuffix}").grid("getRowData");
		if (0 === rowData.length) return rowsValue;
		var i = 0, len = rowData.length, data;
		for(; i < len; i++) {
			data = rowData[i];
			rowsValue += ";" + data.columnId + "|" + encodeURI(data.columnName);
		}
		rowsValue = rowsValue.substring(1);
		return rowsValue;
	},
	// 保存配置
	save : function(){
		jQuery.ns("${namespaceId}").storeEdit();
		var rowsValue  = $.ns("${namespaceId}").getRowsValue();
		if ($.ns("${namespaceId}").modify) {
			var params = {tableId: "${tableId}", componentVersionId: "${componentVersionId}", menuId: "${menuId}"};
			$.ajax({
				type: "post",
				data: params,
				url: $.contextPath + "/appmanage/app-export!save.json?P_rowsValue=" + rowsValue,
				dataType: "json",
				success : function(data) {
					$("#lGrid${idSuffix}").grid("reload");
					$("#rGrid${idSuffix}").grid("reload");
					$.message({message:"保存成功!",cls:"success"});
					$.ns("${namespaceId}").initDefault();
				},
				error : function() {
					$.message({message:"保存失败!",cls:"warning"});
				}
			});
		} else {
			$.message({message: "配置未变更！", cls: "warning"});
			return;
		}
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
				$.ns("${namespaceId}").initRoleTab();
		}
	});
});
</script>
   
