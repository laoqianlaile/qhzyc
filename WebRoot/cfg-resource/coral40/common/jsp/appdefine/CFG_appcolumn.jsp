<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>

<%
	String tableId = request.getParameter("tableId");
	String componentVersionId = request.getParameter("componentVersionId");
	String menuId = request.getParameter("menuId");
	String idSuffix = request.getParameter("idSuffix");
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String params = "?E_model_name=jqgrid&P_tableId=" + tableId + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + menuId;
	request.setAttribute("headerUrl",path + "/appmanage/app-column!dhtmlxGrid.json" + params); 
	request.setAttribute("lUrl", path + "/appmanage/app-column!defaultColumn.json" + params);
	request.setAttribute("rUrl", path + "/appmanage/app-column!defineColumn.json"  + params);
	request.setAttribute("idSuffix", idSuffix);
	request.setAttribute("tableId", tableId);
	request.setAttribute("componentVersionId", componentVersionId);
	request.setAttribute("menuId", menuId);
	request.setAttribute("namespaceId", "ns" + idSuffix);
%>
	<div class="fill">
	<ces:layout id="layoutId${idSuffix}" name="layout" fit="true" style="width:auto;height:auto;">
	<ces:layoutRegion region="north" split="true"  style="width:300px;padding:0px;height:90px">
		<div class="toolbarsnav clearfix">
			<ces:toolbar id="toolbarId${idSuffix}" 
			             onClick="$.ns('${namespaceId}').clickTbar"
					     data="[{'label':'保存','id':'save','type':'button'},{'label':'恢复默认值','id':'reset','type':'button'},{'label':'返回','id':'back2grid','type':'button'}]">
			</ces:toolbar>
		</div>
		<div style="margin-top: 4px;margin-bottom: 2px;">
			<span class="search clearfix">
				<ces:input required="false" icons="icon-search3 jQuery.ns('${namespaceId}').columnSearch" onKeyUp="$.ns('${namespaceId}').columnSearch"
				  width="300" id="keyWord${idSuffix}" name="keyWord" value="" placeholder="字段检索（支持拼音）"/>
				<ces:checkbox id="check${idSuffix}" onChange="$.ns('${namespaceId}').onChangeBox()" name="check" label="显示序列号" ></ces:checkbox>
			</span>
		</div>
		
	</ces:layoutRegion>
	<ces:layoutRegion region="west" split="true"  style="width:300px;padding:0px;">
		<div id="lGrid${idSuffix}" ></div>
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
	// 格式化对齐方式
	formatterAlign : function (value, option, cell) {
		if ("center" === value) return "居中";
		if ("right" === value)  return "靠右";
		return "靠左";
	},
	// 初始化左边列表
	initLGrid : function () {
		var jqGrid = $("#lGrid${idSuffix}"),
		    setting= {
					width:"auto",
					height : "auto",
					colModel : [
						{name: "id", width: "auto", hidden: true},
						{name: "columnId", width: "auto", hidden: true},
						{name: "showName", width: 150, align: "center"},
						{name: "width", width: "100", hidden: true},
						{name: "align", width:"100",hidden: true, editable: true},
						{name: "type", width:"100",hidden: true},
						{name: "url", width:"250",hidden: true},
						{name: "columnType", width: "auto", hidden: true},
						{name: "columnName", width: "auto", hidden: true}
					],
					colNames : ["ID","字段Id","表头名称","宽度","对齐","类型","链接地址","字段类型","字段名称"],
					rowNum : "99999",
					multiselect : true,
					url:"${lUrl}",
					fitStyle:"fill" 
				};
		jqGrid.grid(setting);
	},
	// 初始化右边列表
	initRGrid : function () {
		var jqGrid = $("#rGrid${idSuffix}"),
		    alignOpts = {data:[{value: "left", text: "靠左"},
		                      {value: "center", text: "居中"},
			                  {value: "right", text: "靠右"}]
		                },
			typeOpts  = {data:[{value: "ro", text: "文本"},
			                  {value: "img", text: "图片"},
			                  {value: "link", text: "链接"},
			                  {value: "ro_card", text: "文本和缩略图信息"},
			                  {value: "card", text: "缩略图信息"}]
		               },
			setting = {
				width:"auto",
				height : "auto",
				colModel : [
					{name: "id", width: "auto", hidden: true},
					{name: "columnId", width: "auto", hidden: true},
					{name: "showName", width: 150, align: "center"},
					{name: "width", width: 100, edittype:"text", editable: true, align: "center"},
					{name: "align", width: 150, editable: true, edittype:"combobox", editoptions: alignOpts, formatter: "$.ns('${namespaceId}').formatterAlign", align: "center"},
					{name: "type", width: 150, hidden: true, editable: true, edittype: "combobox", editoptions: typeOpts, align: "center"},
					{name: "url", width: 250, align:"center", hidden: true},
					{name: "columnType", width: "auto", hidden: true},
					{name: "columnName", width: "auto", hidden: true}
				],
				colNames : ["ID","字段Id","表头名称","宽度","对齐","类型","链接地址","字段类型","字段名称"],
				rowNum : "99999",
				cellEdit: true,
				multiselect : true,
				cellEdit : true,
				url:"${rUrl}",
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
	// 获取配置信息
	getRowsValue : function() {
		// 
		function convert(v, type) {
			if ("align" === type) {
				if ("居中" === v) return "center";
				if ("靠右" === v) return "right";
				return "left"; // 靠左 or other
			}
			if ("图片" === v) return "img";
			if ("链接" === v) return "link";
			if ("文本和缩略图信息" === v) "ro_card";
			if ("缩略图信息" === v) return "card";
			
			return "ro"; // 文本 or other
		}
		
		var rowsValue = "", rowData = $("#rGrid${idSuffix}").grid("getRowData");
		if (0 === rowData.length) return rowsValue;
		var i = 0, len = rowData.length, data;
		for(; i < len; i++) {
			data = rowData[i];
			rowsValue += ";" + data.columnId + "|" + encodeURI(data.columnName) + "|" + encodeURI(data.showName) + "|" + data.width
	                        + "|" + convert(data.align, "align") + "|" + convert(data.type) + "|" + encodeURIComponent(data.url) + "|" + data.columnType;
		}
		rowsValue = rowsValue.substring(1);
		return rowsValue;
	},
	//回复默认值
	reset : function(){
		var _this = this;
		var url = $.contextPath + "/appmanage/app-grid!clear.json?tableId=${tableId}&menuId=${menuId}&componentVersionId=${componentVersionId}";
		$.ajax({
			type : "get",
			url : url,
			success : function (data) {
				$.message({message:"恢复默认值成功!", cls:"success"});
				$("#lGrid${idSuffix}").grid("reload");
				$("#rGrid${idSuffix}").grid("reload");
				$.ns("${namespaceId}").initDefault();
				_this.initCheckBox();
			},
			error : function (data) {
				$.message({message:"恢复默认值失败!", cls:"warning"});
			}
		});
	},
	// 保存配置
	save : function(){
		//jQuery.ns("${namespaceId}").storeEdit();
		jQuery.ns("${namespaceId}").saveEdit("rGrid${idSuffix}");
		var rowsValue  = $.ns("${namespaceId}").getRowsValue();
		if (isEmpty(rowsValue)) {
			$.message({message: "请选择记录！", cls: "warning"});
			return;
		}
		var params = {tableId: "${tableId}", componentVersionId: "${componentVersionId}", menuId: "${menuId}"};
		// 列表属性配置（序号列、双击事件）
		var appGrid = $.loadJson($.contextPath + "/appmanage/app-grid!show.json", params);
		if (appGrid && isNotEmpty(appGrid.id)) {
			params.dblclick = appGrid.dblclick;
		} else {
			params.dblclick = 0;
		}
		if ($("#check${idSuffix}").checkbox("isChecked")) {
			params.hasRowNumber = 1;
		} else {
			params.hasRowNumber = 0;
		}
		params.P_rowsValue=rowsValue;
		params.P_isDefault="1";
		$.ajax({
			type: "post",
			data: params,
			url: $.contextPath + "/appmanage/app-grid!save.json",
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
	},
	// 初始化是否显示序列号
	initCheckBox : function(){
        var params = {tableId: "${tableId}", componentVersionId: "${componentVersionId}", menuId: "${menuId}"};
		var appGrid = $.loadJson($.contextPath + "/appmanage/app-grid!show.json", params);
		if (!appGrid || appGrid.hasRowNumber == 1) {
			$("#check${idSuffix}").checkbox("check");
		} else {
			$("#check${idSuffix}").checkbox("uncheck");
		}
	},
	onChangeBox : function(){
		$.ns("${namespaceId}").modify = true;
	}
});
	
</script>
   
