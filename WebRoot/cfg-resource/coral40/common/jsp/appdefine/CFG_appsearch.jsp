<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String path = request.getContextPath();

    String tableId = request.getParameter("tableId");
    String componentVersionId = request.getParameter("componentVersionId");
    String menuId = request.getParameter("menuId");
	String idSuffix = request.getParameter("idSuffix");
    String params = "?E_frame_name=coral&E_model_name=jqgrid&P_tableId=" + tableId + "&P_menuId=" + menuId + "&P_componentVersionId=" + componentVersionId + "&F_in=id,showName,columnName,filterType,dataType,codeTypeCode";
    request.setAttribute("lUrl", path + "/appmanage/app-search!defaultColumn.json" + params);
    request.setAttribute("rUrl", path + "/appmanage/app-search!defineColumn.json"  + params);
    request.setAttribute("tableId", tableId);
    request.setAttribute("componentVersionId", componentVersionId);
	request.setAttribute("menuId", menuId);
    request.setAttribute("idSuffix", idSuffix);
	request.setAttribute("namespaceId", "ns" + idSuffix);
    
%>
	<div class="fill">
		<ces:layout id="searchLayout${idSuffix}" name="layout" fit="true" style="width:auto;height:auto;">
			<ces:layoutRegion region="north" split="true"  style="width:300px;padding:0px;height:90px">
				<div class="toolbarsnav clearfix">
					<ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('${namespaceId}').clickTbar"
							data="[{'label': '保存','id':'save', 'disabled': 'false','type': 'button'},{'label': '恢复默认值','id':'reset', 'disabled': 'false','type': 'button'},{'label': '返回','id':'back2grid', 'disabled': 'false','type': 'button'}]">
					</ces:toolbar>
					</div>
					<div style="margin-top: 4px;margin-bottom: 2px;">
						<span class="search clearfix">
							<ces:input required="false" icons="icon-search3 jQuery.ns('${namespaceId}').columnSearch" onKeyUp="jQuery.ns('${namespaceId}').columnSearch"
							 id="keyWord${idSuffix}" name="keyWord" value="" placeholder="字段检索（支持拼音）" width="300" />
						</span>
				</div>
			</ces:layoutRegion>
			<ces:layoutRegion region="west" split="true" style="height:100%;width:300px;" maxWidth="300" minWidth="200">
				<div id="lGrid${idSuffix}"></div>
			</ces:layoutRegion>			
			<ces:layoutRegion region="center" split="true"  style="padding:0px;">
				<div class="fill coral-adjusted">
					<ces:layout id="searchLayout2${idSuffix}" fit="true" style="position:relative;">
						<ces:layoutRegion region="west" split="true" style="width:120px;">
							<table style="width: 100%;height: 100%;">
							<tr><td>
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
							</td></tr>
							</table>
						</ces:layoutRegion>
						<ces:layoutRegion region="center" split="true">
							<div id="rGrid${idSuffix}" style="border-left: 1px solid #BEBEBE;"></div>
						</ces:layoutRegion>
					</ces:layout>
				</div>
			</ces:layoutRegion> 
		</ces:layout>
	</div>
<script type="text/javascript">
$.extend($.ns("${namespaceId}"), {
	//左边grid
	initLGrid : function() {
		$("#lGrid${idSuffix}").empty();
		var jqGrid = $("#lGrid${idSuffix}");
		var _setting={
				width:"auto",
				height:"auto",
				multiselect:true,
				colModel:[
					{name: "id", hidden: true, sortable: true, width: "auto"},
					{name: "columnId", hidden: true, width: "auto"},
					{name: "showName", sortable: true, width: 100},
					{name: "filterType", hidden: true, width: "auto"}
				],
				colNames:["id","columnId","字段名称","过滤条件"],
				rowNum : "99999",
				url:"${lUrl}",
				fitStyle:"fill"
			};
		jqGrid.grid(_setting);
		jqGrid.grid("sortableRows", {
			cursor: ".handle",
			//start: function(event, ui) { },
			update: function(event, ui) {
				$.ns("${namespaceId}").modify = true;
			}
		});
	},
	//右边grid
	initRGrid : function() {
		var jqGrid = $("#rGrid${idSuffix}"),
		    filterOpts = {data:[{value:"LIKE",text:"包含"},
				               {value:"EQ",text:"等于"},
				               {value:"GT",text:"大于"},
				               {value:"GTE",text:"大于等于"},
				               {value:"LT",text:"小于"},
				               {value:"LTE",text:"小于等于"},
				               {value:"BT",text:"介于"},
				               {value:"NLL",text:"不为空"}]
		                 },
		    _setting = {
					width: "auto",
					height: "auto",
					multiselect: true,
					cellEdit: true,
					colModel: [
						{name: "id", hidden: true, width: "auto"},
						{name: "columnId", hidden: true, width: "auto"},
						{name: "showName", sortable: true, width: 180},
						{name: "filterType", width: 180, editable: true, edittype: "combobox", editoptions: filterOpts, formatter: jQuery.ns("${namespaceId}").getText, align:"center"}
					],
					colNames: ["id","columnId","字段名称","过滤条件"],
					rowNum : "99999",
					url: "${rUrl}",
					fitStyle: "fill",
					beforeEditCell:"$.ns('${namespaceId}').beforeEditCell"
				};
		jqGrid.grid(_setting);
		jqGrid.grid("sortableRows", {
			cursor: ".handle",
			//start: function(event, ui) { },
			update: function(event, ui){
				$.ns("${namespaceId}").modify = true;
			}
		});
	},
	//产生下拉框
	getText : function(cellvalue, options, rowObject) {
		var sel="";
		switch(cellvalue){
			case "LIKE" : sel = "包含";
			break;
			case "EQ" : sel = "等于";
			break;
			case "GT" : sel =  "大于";
			break;
			case "GTE" : sel = "大于等于";
			break;
			case "LT" : sel = "小于";
			break;
			case "LTE" : sel = "小于等于";
			break;
			case "NOT" : sel = "不等于";
			break;
			case "BT" : sel = "介于";
			break;
			case "NLL" : sel = "不为空";
			break;
			default:sel = "等于";
			break;
		}
		return sel;
	},
	//获取下拉框value的值
	getValue : function(text){
		var value="";
		switch(text){
			case "包含" : value = "LIKE";
			break;
			case "等于" : value = "EQ";
			break;
			case "大于" : value =  "GT";
			break;
			case "大于等于" : value = "GTE";
			break;
			case "小于" : value = "LT";
			break;
			case "小于等于" : value = "LTE";
			break;
			case "不等于" : value = "NOT";
			break;
			case "介于" : value = "BT";
			break;
			case "不为空" : value = "NLL";
			break;
			default : value = "EQ";
			break;
		}
		return value;
	},
	//保存
	save : function () {
		//$.ns("${namespaceId}").storeEdit();
		jQuery.ns("${namespaceId}").saveEdit("rGrid${idSuffix}");
		//获取grid对象
		var jqGrid = $("#rGrid${idSuffix}"),
		    rowData = jqGrid.grid("getRowData");
		
		if (rowData.lenght<=0) {
			$.message({message: "请选择记录！", cls: "warning"});
			return;
		}
		var i = 0, len = rowData.length, data, rowsValue = "";
		for (; i < len; i++) {
			data = rowData[i];
			rowsValue += ";" + data.columnId + "," + $.ns("${namespaceId}").getValue(data.filterType) + "," + data.showName;
		}
		rowsValue = rowsValue.substring(1);
		//console.log("search rows value: " + rowsValue);
		var params = {tableId: "${tableId}", componentVersionId: "${componentVersionId}", menuId: "${menuId}", P_rowsValue: rowsValue};
		// 列表属性配置（序号列、双击事件）
		var sPanel = $.loadJson($.contextPath + "/appmanage/app-search-panel!show.json", params);
		if (sPanel && isNotEmpty(sPanel.id)) {
			params.colspan = sPanel.colspan;
		} else {
			params.colspan = 1;
		}
		$.ajax({
			type: "post",
			url: $.contextPath + "/appmanage/app-search-panel!save.json",
			data: params,
			dataType: "json",
			success: function(msg) {
				if (msg.success) $.message({message: "保存成功！", cls: "success"});
				else $.message({message: "保存失败！", cls: "warning"});
				$("#lGrid${idSuffix}").grid("reload");
				$("#rGrid${idSuffix}").grid("reload");
				$.ns("${namespaceId}").initDefault();
			},
			error: function() {
				$.message({message: "保存失败！", cls: "error"});
			}
		}); 
	},
	//清空配置
	reset : function() {
		var url = $.contextPath + "/appmanage/app-search-panel!clear.json?tableId=${tableId}&menuId=${menuId}&componentVersionId=${componentVersionId}";
		$.ajax({
			type: "post",
			url: url,
			success: function(msg) {
				if (msg.success) $.message({message: "恢复默认值成功！", cls: "success"});
				else $.message({message: "恢复默认值失败！", cls: "warning"});
				$("#lGrid${idSuffix}").grid("reload");
				$("#rGrid${idSuffix}").grid("reload");
				$.ns("${namespaceId}").initDefault();
			},
			error : function () { }
		});
	}
});
</script>
