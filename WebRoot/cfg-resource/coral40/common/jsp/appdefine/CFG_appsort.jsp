<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>


<%
	String tableId = request.getParameter("tableId");
	String componentVersionId = request.getParameter("componentVersionId");
	String menuId = request.getParameter("menuId");
	String idSuffix = request.getParameter("idSuffix");
	String path = request.getContextPath();
	String params = "?E_model_name=jqgrid&P_tableId=" + tableId + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + menuId; 
	request.setAttribute("lUrl", path + "/appmanage/app-sort!defaultColumn.json" + params);
	request.setAttribute("rUrl", path + "/appmanage/app-sort!defineColumn.json"  + params);
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
		<ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('${namespaceId}').clickTbar"
				data="[{'label':'保存','id':'save','type':'button'},{'label':'恢复默认值','id':'reset','type':'button'},{'label':'返回','id':'back2grid','type':'button'}]">
		</ces:toolbar>
		</div>
		<div style="margin-top: 4px;margin-bottom: 2px;">
			<span class="search clearfix">
				<ces:input required="false" icons="icon-search3 jQuery.ns('${namespaceId}').columnSearch" onKeyUp="$.ns('${namespaceId}').columnSearch"
				 id="keyWord${idSuffix}" name="keyWord" value="" placeholder="字段检索（支持拼音）"  width="300"/>
			</span>
		</div>
  	</ces:layoutRegion>
	<ces:layoutRegion region="west" split="true" style="width:300px;padding:0px;">
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
	// 初始化左边列表
	initLGrid : function() {
		var jqGrid = $("#lGrid${idSuffix}");
		var _setting={
				width:"auto",
				height:"auto",
				multiselect : true,
				colModel:[
					{name: "id", width: 50, sortable: true, hidden: true},
					{name: "columnId", width:50, hidden: true},
					{name: "showName", sortable: true, width: 170, align: "center"},
					{name: "sortType", width: 100, sortable: true, hidden: true}
				],
				colNames: ["ID", "columnId", "字段名称", "排序"],
				rowNum : "99999",
				url: "${lUrl}",
				fitStyle: "fill"
			};
		jqGrid.grid(_setting);
	},
	// 初始化右边列表
	initRGrid : function() {
		var jqGrid = $("#rGrid${idSuffix}"),
		    _soptions = {data:[
					{value: "asc", text: "升序"},
					{value: "desc", text: "降序"}
					]},
		    _setting = {
				width : "auto",
				height: "auto",
				multiselect : true,
				cellEdit : true,
				rowNum : "-1",
				colModel: [
					{name: "id", width: 50,sortable: true, hidden: true},
					{name: "columnId", width: 50, hidden: true},
					{name: "showName", sortable: true, width: 150,align: "center"},
					{name: "sortType", width:150, editable: true, edittype: "combobox", editoptions: _soptions, formatter: $.ns("${namespaceId}").formatterSortType, align: "center"}
				],
				colNames: ["ID","columnId","字段名称","排序"],
				rowNum : "99999",
				url: "${rUrl}",
				fitStyle: "fill",
				beforeEditCell: $.ns("${namespaceId}").beforeEditCell
			};
		jqGrid.grid(_setting);
		jqGrid.grid("sortableRows", {
			cursor: ".handle",
			// start: function(event, ui) { },
			update: function(event, ui) {
				jQuery.ns("${namespaceId}").modify = true;
			}
		});
	},
	// 格式化排序字段
	formatterSortType : function(value, options, cell) {
		if ("desc" === value) return "降序";
		return "升序";
	},
	//回复默认值
	reset : function() {
		var url = $.contextPath + "/appmanage/app-sort!clear.json?P_tableId=${tableId}&P_componentVersionId=${componentVersionId}&P_menuId=${menuId}";
		$.ajax({
			type: 'post',
			url:url,
			success : function(data){
				$.message({message: "恢复默认值成功!", cls: "success"});
				jQuery.ns("${namespaceId}").modify = false;
				$("#rGrid${idSuffix}").grid("reload");
				$("#lGrid${idSuffix}").grid("reload");
			},
			error : function(){
				$.message({message: "恢复默认值失败!", cls: "warning"});
			}
		});
	},
	// 
	save : function(){
		//jQuery.ns("${namespaceId}").storeEdit();
		jQuery.ns("${namespaceId}").saveEdit("rGrid${idSuffix}");
		var rowsValue = "",
		    rowData = $("#rGrid${idSuffix}").grid("getRowData");
		if (0 === rowData.length) {
			$.message({message:"请选择记录！",cls:"warning"});
			return ;
		}
		var i = 0, len = rowData.length, data;
		for(; i < len; i++) {
			rowsValue += ";" + rowData[i].columnId + "," + ("降序" === rowData[i].sortType ? "desc" : "asc");
		}
		rowsValue = rowsValue.substring(1);
		//console.log("sort rows values: " + rowsValue);
		var sUrl = $.contextPath + "/appmanage/app-sort!save.json?P_tableId=${tableId}&P_componentVersionId=${componentVersionId}&P_menuId=${menuId}&P_rowsValue=" + rowsValue;
		$.ajax({
			type: "post",
			url: sUrl,
			dataType: "json",
			success : function(data) {
				$("#lGrid${idSuffix}").grid("reload");
				$("#rGrid${idSuffix}").grid("reload");
				$.message({message: "保存成功!", cls: "success"});
				jQuery.ns("${namespaceId}").modify =false;
			},
			error : function() {
				$.message({message: "保存失败!", cls: "warning"});
			}
		});
	},
});

	
</script>

