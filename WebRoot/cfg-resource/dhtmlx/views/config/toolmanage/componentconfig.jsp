<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>构件配置文件生成</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  <body>
  	<div id="jarDiv" style="visibility: hidden;">
  		<table width="100%" height="100%" style="top: 5px;">
  			<tr>
  				<td align="right" width="100">jar<img id="jar_plus" align="absmiddle" src="<%=dhxResPath %>/common/images/no.gif" onclick="addJars()">：</td>
  				<td id="jars"></td>
  			</tr>
  		</table>
  	</div>
	<script type="text/javascript">
		MODEL_URL = "<%=path%>/componentconfig/component-config";
		var batchPackageGrid;
		var configToolBar, packageToolBar, batchPackageToolBar;
		var bodyWidth = document.body.clientWidth;
		var formOffsetLeft = 0;
		if (bodyWidth > 1000) {
			formOffsetLeft = (bodyWidth - 1000)/2;
		}
		detailFormData = {
			format: [
		        {type: "block", width: "1000", offsetLeft:formOffsetLeft, list:[
					{type: "label", label:"基本信息", width: "950", list:[
						{type: "block", width: "900", list:[
							{type: "input", label: "编码:", name: "code", maxLength:100, required: true},
							{type: "input", label: "版本:", name: "version", maxLength:20, required: true},
							{type: "newcolumn"},
							{type: "input", label: "构件类名:", name: "name", maxLength:100, required: true, tooltip: "构件类名需要和构件包名一致（忽略大小写）", validate: "validComponentName"},
							{type: "combo", label: "类型:", name: "type", required: true, options:[
		              				{value: "", text: "请选择"},
		              				{value: "0", text: "公用构件"},
		              				{value: "1", text: "页面构件"},
		              				{value: "2", text: "逻辑构件"}
		              		]},
							{type: "newcolumn"},
							{type: "input", label: "构件名称:", name: "alias", maxLength:100, required: true},
							{type: "combo", label: "前台:", name: "views", required: true, options:[
		              				{value: "", text: "请选择"},
		              				{value: "dhtmlx", text: "dhtmlx"},
		              				{value: "coral40", text: "coral40"}
		              		]}
	              		]},
						{type: "block", width: "900", list:[
							{type: "input", label: "访问地址:", name: "url", maxLength:200, inputWidth:"745"},
							{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "remark", maxLength:500, rows:"3", inputWidth:"743"}
						]}
					]},
					{type: "label", label:"系统参数<img align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addSystemParameter()'>", name: "system_parameter", width: "950", list:[
						{type: "label", label:"系统参数_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeSystemParameter(1)'>", name: "system_parameter_block_1", width: "900", list:[
							{type: "block", width: "900", list:[
								{type: "input", label: "名称:", name: "system_parameter_name_1", maxLength:100, required: true}
							]},
							{type: "block", width: "900", list:[
								{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "system_parameter_remark_1", maxLength:500, rows:"3", inputWidth:"743"}
							]}
						]}
					]},
					{type: "label", label:"自身参数<img align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addSelfParam()'>", name: "self_parameter", width: "950", list:[
						{type: "label", label:"自身参数_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeSelfParam(1)'>", name: "self_parameter_block_1", width: "900", list:[
							{type: "block", width: "900", list:[
								{type: "input", label: "名称:", name: "self_parameter_name_1", maxLength:100, required: true},
								{type: "combo", label: "类型:", name: "self_parameter_type_1", required: true, options:[
		              				{value: "", text: "请选择"},
		              				{value: "0", text: "文本框"},
		              				{value: "1", text: "下拉框"}
		              			]},
								{type: "newcolumn"},
								{type: "input", label: "值:&nbsp;&nbsp;&nbsp;", name: "self_parameter_value_1", maxLength:200},
								{type: "input", label: "显示值:&nbsp;&nbsp;&nbsp;", name: "self_parameter_text_1", maxLength:200}
							]},
							{type: "block", width: "900", list:[
								{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "self_parameter_remark_1", maxLength:500, rows:"3", inputWidth:"743"}
							]}
						]}
					]},
					{type: "label", label:"输入参数<img align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addInputParameter()'>", name: "input_parameter", width: "950", list:[
						{type: "label", label:"输入参数_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeInputParameter(1)'>", name: "input_parameter_block_1", width: "900", list:[
							{type: "block", width: "900", list:[
								{type: "input", label: "名称:", name: "input_parameter_name_1", maxLength:100, required: true},
								{type: "newcolumn"},
								{type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "input_parameter_value_1", maxLength:200}
							]},
							{type: "block", width: "900", list:[
								{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "input_parameter_remark_1", maxLength:500, rows:"3", inputWidth:"743"}
							]}
						]}
					]},
					{type: "label", label:"输出参数<img id='output_parameter_plus' align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addOutputParameter()'>", name: "output_parameter", width: "950", list:[
						{type: "label", label:"输入参数_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeOutputParameter(1)'>", name: "output_parameter_block_1", width: "900", list:[
							{type: "block", width: "900", list:[
								{type: "input", label: "名称:", name: "output_parameter_name_1", maxLength:100, required: true}
							]},
							{type: "block", width: "900", list:[
								{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "output_parameter_remark_1", maxLength:500, rows:"3", inputWidth:"743"}
							]}
						]}
					]},
					{type: "label", label:"预留区<img id='reserve_zone_plus' align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addReserveZone()'>", name: "reserve_zone", offsetLeft: "14", width: "950", list:[
						{type: "label", label:"预留区_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeReserveZone(1)'>", name: "reserve_zone_block_1", offsetLeft: "14", width: "900", list:[
							{type: "block", width: "900", list:[
								{type: "input", label: "名称:", name: "reserve_zone_name_1", maxLength:100, required: true},
								{type: "combo", label: "类型:", name: "reserve_zone_type_1", required: true, options:[
		              				{value: "", text: "请选择"},
		              				{value: "0", text: "工具条"},
		              				{value: "1", text: "列表超链接"},
		              				{value: "2", text: "按钮预留区"},
		              				{value: "3", text: "树节点预留区"},
		              				{value: "4", text: "标签页预留区"}
		              			]},
								{type: "newcolumn"},
								{type: "input", label: "别名:", name: "reserve_zone_alias_1", maxLength:100, required: true},
								{type: "input", label: "所在页面:", name: "reserve_zone_page_1", maxLength:100, required: true}
							]}
						]}
					]},
					{type: "label", label:"方法<img id='function_plus' align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addFunction()'>", name: "function", offsetLeft: "28", width: "950", list:[
						{type: "label", label:"方法_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeFunction(1)'>", name: "function_block_1", offsetLeft: "28", width: "900", list:[
							{type: "block", width: "900", list:[
								{type: "input", label: "名称:", name: "function_name_1", maxLength:100, required: true},
								{type: "newcolumn"},
								{type: "input", label: "所在页面:", name: "function_page_1", maxLength:100, required: true}
							]},
							{type: "block", width: "900", list:[
								{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "function_remark_1", maxLength:500, rows:"3", inputWidth:"743"}
							]},
							{type: "label", label:"返回值<img align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addFunctionReturnData(1)'>", name: "function_return_data_1", offsetLeft: "28", width: "850", list:[
								{type: "label", label:"返回值_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeFunctionReturnData(1,1)'>", name: "function_return_data_1_block_1", offsetLeft: "28", width: "800", list:[
									{type: "input", label: "名称:", name: "function_return_data_1_name_1", maxLength:200, required: true},
									{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "function_return_data_1_remark_1", maxLength:500, rows:"3", inputWidth:"743"}
								]}
							]}
						]}
					]},
					{type: "label", label:"回调函数<img id='callback_plus' align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addCallback()'>", name: "callback", width: "950", list:[
						{type: "label", label:"回调函数_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeCallback(1)'>", name: "callback_block_1", width: "900", list:[
							{type: "block", width: "900", list:[
								{type: "input", label: "名称:", name: "callback_name_1", maxLength:100, required: true},
								{type: "newcolumn"},
								{type: "input", label: "所在页面:", name: "callback_page_1", maxLength:100, required: true}
							]},
							{type: "block", width: "900", list:[
								{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "callback_remark_1", maxLength:500, rows:"3", inputWidth:"743"}
							]},
							{type: "label", label:"参数<img align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addCallbackParameter(1)'>", name: "callback_parameter_1", offsetLeft: "42", width: "850", list:[
								{type: "label", label:"参数_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeCallbackParameter(1,1)'>", name: "callback_parameter_1_block_1", offsetLeft: "42", width: "800", list:[
									{type: "input", label: "名称:", name: "callback_parameter_1_name_1", maxLength:200, required: true},
									{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "callback_parameter_1_remark_1", maxLength:500, rows:"3", inputWidth:"743"}
								]}
							]}
						]}
					]},
					{type: "label", label:"权限按钮<img id='authority_button_plus' align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addAuthorityButton()'>", name: "authority_button", width: "950", list:[
 						{type: "label", label:"权限按钮_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeAuthorityButton(1)'>", name: "authority_button_block_1", width: "900", list:[
 							{type: "block", width: "900", list:[
 								{type: "input", label: "名称:", name: "authority_button_name_1", maxLength:100, required: true},
 								{type: "newcolumn"},
								{type: "input", label: "显示名称:", name: "authority_button_display_name_1", maxLength:100, required: true}
 							]}
 						]}
 					]},
					{type: "label", label:"表<img align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addTable()'>", name: "table", offsetLeft: "42", width: "950", list:[
						{type: "label", label:"表_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeTable(1)'>", name: "table_block_1", offsetLeft: "42", width: "900", list:[
							{type: "block", width: "900", list:[
								{type: "input", label: "名称:", name: "table_name_1", maxLength:100, required: true}
							]},
							{type: "block", width: "900", list:[
							    {type: "itemlabel", label: "是否是自定义的表:", labelWidth: 100, labelAlign:"right"},
							    {type:"newcolumn"},
							    {type: "radio", label: "不是", name: "table_is_selfdefine_1", value:"0", labelWidth: 60, labelAlign:"left", position:"label-right", checked: true, offsetLeft: 20},
							    {type:"newcolumn"},
							    {type: "radio", label: "是", name: "table_is_selfdefine_1", value:"1", labelWidth: 60, labelAlign:"left", position:"label-right"}
							]},
							{type: "block", width: "900", list:[
   								{type: "itemlabel", label: "发布时发布数据:", labelWidth: 100, labelAlign:"right"},
   							    {type:"newcolumn"},
   							    {type: "radio", label: "不生成", name: "table_release_with_data_1", value:"0", labelWidth: 60, labelAlign:"left", position:"label-right", checked: true, offsetLeft: 20},
   							    {type:"newcolumn"},
   							    {type: "radio", label: "生成", name: "table_release_with_data_1", value:"1", labelWidth: 60, labelAlign:"left", position:"label-right"}
   							]},
							{type: "label", label:"列<img align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addColumn(1)'>", name: "table_column_1", offsetLeft: "56", width: "850", list:[
								{type: "label", label:"列_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeColumn(1,1)'>", name: "table_column_1_block_1", offsetLeft: "56", width: "800", list:[
									{type: "block", width: "900", list:[
										{type: "input", label: "名称:", name: "table_column_1_name_1", maxLength:200, required: true},
										{type: "combo", label: "类型:", name: "table_column_1_type_1", required: true, options:[
				              				{value: "", text: "请选择"},
				              				{value: "字符型", text: "字符型"},
				              				{value: "日期型", text: "日期型"},
				              				{value: "数字型", text: "数字型"}
				              			]},
										{type: "combo", label: "可为空:", name: "table_column_1_isNull_1", required: true, options:[
				              				{value: "", text: "请选择"},
				              				{value: "1", text: "是"},
				              				{value: "0", text: "否"}
				              			]},
				              			{type: "newcolumn"},
										{type: "block", offsetTop:"27"},
										{type: "input", label: "长度:&nbsp;&nbsp;&nbsp;", name: "table_column_1_length_1", maxLength:10},
										{type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "table_column_1_defaultValue_1", maxLength:200}
									]},
									{type: "block", width: "900", list:[
										{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "table_column_1_remark_1", maxLength:500, rows:"3", inputWidth:"743"}
									]}
								]}
							]}
						]}
					]}
				]}
	        ],
			settings: {labelWidth: 100, inputWidth: 180}
		};
		function validComponentName() {
			var name1 = componentForm.getItemValue("name").toLowerCase();
			var name2 = detailForm.getItemValue("name").toLowerCase();
			if (name1 == name2) {
				return true;
			} else {
				dhtmlx.message("构件类名需要和构件包名一致（忽略大小写）!");
				return false;
			}
		}
		// 系统参数
		var systemParameterIndex = 2;
		function addSystemParameter() {
			var itemData = {type: "label", label:"系统参数_"+systemParameterIndex+"<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeSystemParameter("+systemParameterIndex+")'>", name: "system_parameter_block_"+systemParameterIndex, width: "900", list:[
					{type: "block", width: "900", list:[
						{type: "input", label: "名称:", name: "system_parameter_name_"+systemParameterIndex, maxLength:100, required: true}
					]},
					{type: "block", width: "900", list:[
						{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "system_parameter_remark_"+systemParameterIndex, maxLength:500, rows:"3", inputWidth:"743"}
					]}
				]};
			detailForm.addItem("system_parameter", itemData, systemParameterIndex++);
		}
		function removeSystemParameter(index) {
			detailForm.removeItem("system_parameter_block_" + index);
		}
		
		// 自身参数
		var selfParamIndex = 2;
		function addSelfParam() {
			var itemData = {type: "label", label:"自身参数_"+selfParamIndex+"<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeSelfParam("+selfParamIndex+")'>", name: "self_parameter_block_"+selfParamIndex, width: "900", list:[
					{type: "block", width: "900", list:[
						{type: "input", label: "名称:", name: "self_parameter_name_"+selfParamIndex, maxLength:100, required: true},
						{type: "combo", label: "类型:", name: "self_parameter_type_"+selfParamIndex, required: true, options:[
              				{value: "", text: "请选择"},
              				{value: "0", text: "文本框"},
              				{value: "1", text: "下拉框"}
              			]},
						{type: "newcolumn"},
						{type: "input", label: "值:&nbsp;&nbsp;&nbsp;", name: "self_parameter_value_"+selfParamIndex, maxLength:200},
						{type: "input", label: "显示值:&nbsp;&nbsp;&nbsp;", name: "self_parameter_text_"+selfParamIndex, maxLength:200}
					]},
					{type: "block", width: "900", list:[
						{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "self_parameter_remark_"+selfParamIndex, maxLength:500, rows:"3", inputWidth:"743"}
					]}
				]};
			detailForm.addItem("self_parameter", itemData, selfParamIndex);
			addSelfParamTypeChangeEvent(selfParamIndex);
			selfParamIndex++;
		}
		function removeSelfParam(index) {
			detailForm.removeItem("self_parameter_block_" + index);
		}
		var selfParamOptionIndex = {};
		// 自身参数类型为下拉框时添加 下拉框选项（大label）
		function addSelfParamOptions(index) {
			var itemData = {type: "label", label:"选项<img align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addSelfParamOption("+index+")'>", name: "self_parameter_option_"+index, offsetLeft: "42", width: "850", list:[
					{type: "label", label:"选项_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeSelfParamOption("+index+",1)'>", name: "self_parameter_option_"+index+"_block_1", offsetLeft: "42", width: "800", list:[
						{type: "input", label: "显示值:", name: "self_parameter_option_"+index+"_text_1", maxLength:200, required: true},
						{type: "newcolumn"},
						{type: "input", label: "值:&nbsp;&nbsp;&nbsp;", name: "self_parameter_option_"+index+"_value_1", maxLength:200}
					]}
				]};
			detailForm.addItem("self_parameter_block_"+index, itemData, 3);
			selfParamOptionIndex["options_" + index] = 2;
		}
		// 自身参数类型为下拉框时添加 下拉框选项（小label）
		function addSelfParamOption(index) {
			var oIndex = selfParamOptionIndex["options_" + index];
			var itemData = {type: "label", label:"选项_"+oIndex+"<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeSelfParamOption("+index+","+oIndex+")'>", name: "self_parameter_option_"+index+"_block_"+oIndex, offsetLeft: "42", width: "800", list:[
					{type: "input", label: "显示值:", name: "self_parameter_option_"+index+"_text_"+oIndex, maxLength:200, required: true},
					{type: "newcolumn"},
					{type: "input", label: "值:&nbsp;&nbsp;&nbsp;", name: "self_parameter_option_"+index+"_value_"+oIndex, maxLength:200}
				]};
			detailForm.addItem("self_parameter_option_"+index, itemData, oIndex);
			selfParamOptionIndex["options_" + index] = oIndex + 1;
		}
		// 自身参数类型为下拉框时删除 下拉框选项（小label）
		function removeSelfParamOption(optionsIndex, optionIndex) {
			detailForm.removeItem("self_parameter_option_"+optionsIndex+"_block_"+optionIndex);
		}
		// 添加自身参数类型改变事件
		function addSelfParamTypeChangeEvent(index) {
			var selfParamType = detailForm.getCombo("self_parameter_type_" + index);
			if (selfParamType) {
				selfParamType.attachEvent("onChange", function() {
					var existSelfParamOptions = detailForm.isItem("self_parameter_option_" + index);
					if ("1" != this.getActualValue() && existSelfParamOptions) {
						detailForm.removeItem("self_parameter_option_" + index);
					} else if ("1" == this.getActualValue() && !existSelfParamOptions) {
						addSelfParamOptions(index);
					}
				});
			}
		}
		
		// 输入参数
		var inputParameterIndex = 2;
		function addInputParameter() {
			var itemData = {type: "label", label:"输入参数_"+inputParameterIndex+"<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeInputParameter("+inputParameterIndex+")'>", name: "input_parameter_block_"+inputParameterIndex, width: "900", list:[
					{type: "block", width: "900", list:[
						{type: "input", label: "名称:", name: "input_parameter_name_"+inputParameterIndex, maxLength:100, required: true},
						{type: "newcolumn"},
						{type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "input_parameter_value_"+inputParameterIndex, maxLength:200}
					]},
					{type: "block", width: "900", list:[
						{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "input_parameter_remark_"+inputParameterIndex, maxLength:500, rows:"3", inputWidth:"743"}
					]}
				]};
			detailForm.addItem("input_parameter", itemData, inputParameterIndex++);
		}
		function removeInputParameter(index) {
			detailForm.removeItem("input_parameter_block_" + index);
		}
		
		// 输出参数
		var outputParameterIndex = 2;
		function addOutputParameter() {
			var itemData = {type: "label", label:"输出参数_"+outputParameterIndex+"<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeOutputParameter("+outputParameterIndex+")'>", name: "output_parameter_block_"+outputParameterIndex, width: "900", list:[
					{type: "block", width: "900", list:[
						{type: "input", label: "名称:", name: "output_parameter_name_"+outputParameterIndex, maxLength:100, required: true}
					]},
					{type: "block", width: "900", list:[
						{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "output_parameter_remark_"+outputParameterIndex, maxLength:500, rows:"3", inputWidth:"743"}
					]}
				]};
			detailForm.addItem("output_parameter", itemData, outputParameterIndex++);
		}
		function removeOutputParameter(index) {
			detailForm.removeItem("output_parameter_block_" + index);
		}
		
		// 预留区
		var reserveZoneIndex = 2;
		function addReserveZone() {
			var itemData = {type: "label", label:"预留区_"+reserveZoneIndex+"<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeReserveZone("+reserveZoneIndex+")'>", name: "reserve_zone_block_"+reserveZoneIndex, offsetLeft: "14", width: "900", list:[
					{type: "block", width: "900", list:[
						{type: "input", label: "名称:", name: "reserve_zone_name_"+reserveZoneIndex, maxLength:100, required: true},
						{type: "combo", label: "类型:", name: "reserve_zone_type_"+reserveZoneIndex, required: true, options:[
              				{value: "", text: "请选择"},
              				{value: "0", text: "工具条"},
              				{value: "1", text: "列表超链接"},
              				{value: "2", text: "按钮预留区"},
              				{value: "3", text: "树节点预留区"},
              				{value: "4", text: "标签页预留区"}
              			]},
						{type: "newcolumn"},
						{type: "input", label: "别名:", name: "reserve_zone_alias_"+reserveZoneIndex, maxLength:100, required: true},
						{type: "input", label: "所在页面:", name: "reserve_zone_page_"+reserveZoneIndex, maxLength:100, required: true}
					]}
				]};
			detailForm.addItem("reserve_zone", itemData, reserveZoneIndex++);
		}
		function removeReserveZone(index) {
			detailForm.removeItem("reserve_zone_block_" + index);
		}
		
		// 方法
		var functionIndex = 2;
		function addFunction() {
			var itemData = {type: "label", label:"方法_"+functionIndex+"<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeFunction("+functionIndex+")'>", name: "function_block_"+functionIndex, offsetLeft: "28", width: "900", list:[
					{type: "block", width: "900", list:[
						{type: "input", label: "名称:", name: "function_name_"+functionIndex, maxLength:100, required: true},
						{type: "newcolumn"},
						{type: "input", label: "所在页面:", name: "function_page_"+functionIndex, maxLength:100, required: true}
					]},
					{type: "block", width: "900", list:[
						{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "function_remark_"+functionIndex, maxLength:500, rows:"3", inputWidth:"743"}
					]},
					{type: "label", label:"返回值<img align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addFunctionReturnData("+functionIndex+")'>", name: "function_return_data_"+functionIndex, offsetLeft: "28", width: "850", list:[
						{type: "label", label:"返回值_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeFunctionReturnData("+functionIndex+",1)'>", name: "function_return_data_"+functionIndex+"_block_1", offsetLeft: "28", width: "800", list:[
							{type: "input", label: "名称:", name: "function_return_data_"+functionIndex+"_name_1", maxLength:200, required: true},
							{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "function_return_data_"+functionIndex+"_remark_1", maxLength:500, rows:"3", inputWidth:"743"}
						]}
					]}
				]};
			detailForm.addItem("function", itemData, functionIndex);
			functionReturnDataIndex["returnDatas_" + functionIndex] = 2;
			functionIndex++;
		}
		function removeFunction(index) {
			detailForm.removeItem("function_block_" + index);
		}
		var functionReturnDataIndex = {};
		functionReturnDataIndex.returnDatas_1 = 2;
		// 方法添加返回值
		function addFunctionReturnData(index) {
			var rIndex = functionReturnDataIndex["returnDatas_" + index];
			var itemData = {type: "label", label:"返回值_"+rIndex+"<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeFunctionReturnData("+index+","+rIndex+")'>", name: "function_return_data_"+index+"_block_"+rIndex, offsetLeft: "28", width: "800", list:[
					{type: "input", label: "名称:", name: "function_return_data_"+index+"_name_"+rIndex, maxLength:200, required: true},
					{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "function_return_data_"+index+"_remark_"+rIndex, maxLength:500, rows:"3", inputWidth:"743"}
				]};
			detailForm.addItem("function_return_data_"+index, itemData, rIndex);
			functionReturnDataIndex["returnDatas_" + index] = rIndex + 1;
		}
		// 方法删除返回值
		function removeFunctionReturnData(returnDatasIndex, returnDataIndex) {
			detailForm.removeItem("function_return_data_"+returnDatasIndex+"_block_"+returnDataIndex);
		}
		
		// 回调函数
		var callbackIndex = 2;
		function addCallback() {
			var itemData = {type: "label", label:"回调函数_"+callbackIndex+"<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeCallback("+callbackIndex+")'>", name: "callback_block_"+callbackIndex, width: "900", list:[
					{type: "block", width: "900", list:[
						{type: "input", label: "名称:", name: "callback_name_"+callbackIndex, maxLength:100, required: true},
						{type: "newcolumn"},
						{type: "input", label: "所在页面:", name: "callback_page_"+callbackIndex, maxLength:100, required: true}
					]},
					{type: "block", width: "900", list:[
						{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "callback_remark_"+callbackIndex, maxLength:500, rows:"3", inputWidth:"743"}
					]},
					{type: "label", label:"参数<img align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addCallbackParameter("+callbackIndex+")'>", name: "callback_parameter_"+callbackIndex, offsetLeft: "42", width: "850", list:[
						{type: "label", label:"参数_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeCallbackParameter("+callbackIndex+",1)'>", name: "callback_parameter_"+callbackIndex+"_block_1", offsetLeft: "42", width: "800", list:[
							{type: "input", label: "名称:", name: "callback_parameter_"+callbackIndex+"_name_1", maxLength:200, required: true},
							{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "callback_parameter_"+callbackIndex+"_remark_1", maxLength:500, rows:"3", inputWidth:"743"}
						]}
					]}
				]};
			detailForm.addItem("callback", itemData, callbackIndex);
			callbackParameterIndex["parameters_" + callbackIndex] = 2;
			callbackIndex++;
		}
		function removeCallback(index) {
			detailForm.removeItem("callback_block_" + index);
		}
		var callbackParameterIndex = {};
		callbackParameterIndex.parameters_1 = 2;
		// 回调函数添加参数
		function addCallbackParameter(index) {
			var rIndex = callbackParameterIndex["parameters_" + index];
			var itemData = {type: "label", label:"参数_"+rIndex+"<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeCallbackParameter("+index+","+rIndex+")'>", name: "callback_parameter_"+index+"_block_"+rIndex, offsetLeft: "42", width: "800", list:[
					{type: "input", label: "名称:", name: "callback_parameter_"+index+"_name_"+rIndex, maxLength:200, required: true},
					{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "callback_parameter_"+index+"_remark_"+rIndex, maxLength:500, rows:"3", inputWidth:"743"}
				]};
			detailForm.addItem("callback_parameter_"+index, itemData, rIndex);
			callbackParameterIndex["parameters_" + index] = rIndex + 1;
		}
		// 回调函数删除参数
		function removeCallbackParameter(parametersIndex, parameterIndex) {
			detailForm.removeItem("callback_parameter_"+parametersIndex+"_block_"+parameterIndex);
		}
		
		// 权限按钮
		var authorityButtonIndex = 2;
		function addAuthorityButton() {
			var itemData = {type: "label", label:"权限按钮_"+authorityButtonIndex+"<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeAuthorityButton("+authorityButtonIndex+")'>", name: "authority_button_block_"+authorityButtonIndex, width: "900", list:[
					{type: "block", width: "900", list:[
						{type: "input", label: "名称:", name: "authority_button_name_"+authorityButtonIndex, maxLength:100, required: true},
						{type: "newcolumn"},
						{type: "input", label: "显示名称:", name: "authority_button_display_name_"+authorityButtonIndex, maxLength:100, required: true}
					]}
				]};
			detailForm.addItem("authority_button", itemData, authorityButtonIndex++);
		}
		function removeAuthorityButton(index) {
			detailForm.removeItem("authority_button_block_" + index);
		}
		
		// 表
		var tableIndex = 2;
		function addTable() {
			var itemData = {type: "label", label:"表_"+tableIndex+"<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeTable("+tableIndex+")'>", name: "table_block_"+tableIndex, offsetLeft: "42", width: "900", list:[
					{type: "block", width: "900", list:[
						{type: "input", label: "名称:", name: "table_name_"+tableIndex, maxLength:100, required: true}
					]},
					{type: "block", width: "900", list:[
 						{type: "itemlabel", label: "是否是自定义的表:", labelWidth: 100, labelAlign:"right"},
 						{type:"newcolumn"},
 						{type: "radio", label: "不是", name: "table_is_selfdefine_"+tableIndex, value:"0", labelWidth: 60, labelAlign:"left", position:"label-right", checked: true, offsetLeft: 20},
 						{type:"newcolumn"},
 						{type: "radio", label: "是", name: "table_is_selfdefine_"+tableIndex, value:"1", labelWidth: 60, labelAlign:"left", position:"label-right"}
 					]},
					{type: "block", width: "900", list:[
 						{type: "itemlabel", label: "发布时发布数据:", labelWidth: 100, labelAlign:"right"},
 						{type:"newcolumn"},
 						{type: "radio", label: "不生成", name: "table_release_with_data_"+tableIndex, value:"0", labelWidth: 60, labelAlign:"left", position:"label-right", checked: true, offsetLeft: 20},
 						{type:"newcolumn"},
 						{type: "radio", label: "生成", name: "table_release_with_data_"+tableIndex, value:"1", labelWidth: 60, labelAlign:"left", position:"label-right"}
 					]},
					{type: "label", label:"列<img align='absmiddle' src='<%=dhxResPath %>/common/images/no.gif' onclick='addColumn("+tableIndex+")'>", name: "table_column_"+tableIndex, offsetLeft: "56", width: "850", list:[
						{type: "label", label:"列_1<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeColumn("+tableIndex+",1)'>", name: "table_column_"+tableIndex+"_block_1", offsetLeft: "56", width: "800", list:[
							{type: "block", width: "900", list:[
								{type: "input", label: "名称:", name: "table_column_"+tableIndex+"_name_1", maxLength:200, required: true},
								{type: "combo", label: "类型:", name: "table_column_"+tableIndex+"_type_1", required: true, options:[
		              				{value: "", text: "请选择"},
		              				{value: "字符型", text: "字符型"},
		              				{value: "日期型", text: "日期型"},
		              				{value: "数字型", text: "数字型"}
		              			]},
								{type: "combo", label: "可为空:", name: "table_column_"+tableIndex+"_isNull_1", required: true, options:[
		              				{value: "", text: "请选择"},
		              				{value: "1", text: "是"},
		              				{value: "0", text: "否"}
		              			]},
		              			{type: "newcolumn"},
								{type: "block", offsetTop:"27"},
								{type: "input", label: "长度:&nbsp;&nbsp;&nbsp;", name: "table_column_"+tableIndex+"_length_1", maxLength:10},
								{type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "table_column_"+tableIndex+"_defaultValue_1", maxLength:200}
							]},
							{type: "block", width: "900", list:[
								{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "table_column_"+tableIndex+"_remark_1", maxLength:500, rows:"3", inputWidth:"743"}
							]}
						]}
					]}
				]};
			detailForm.addItem("table", itemData, tableIndex);
			tableColumnIndex["columns_" + tableIndex] = 2;
			tableIndex++;
		}
		function removeTable(index) {
			detailForm.removeItem("table_block_" + index);
		}
		var tableColumnIndex = {};
		tableColumnIndex.columns_1 = 2;
		// 表添加列
		function addColumn(index) {
			var cIndex = tableColumnIndex["columns_" + index];
			var itemData = {type: "label", label:"列_"+cIndex+"<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeColumn("+index+","+cIndex+")'>", name: "table_column_"+index+"_block_"+cIndex, offsetLeft: "56", width: "800", list:[
				{type: "block", width: "900", list:[
					{type: "input", label: "名称:", name: "table_column_"+index+"_name_"+cIndex, maxLength:200, required: true},
					{type: "combo", label: "类型:", name: "table_column_"+index+"_type_"+cIndex, required: true, options:[
             				{value: "", text: "请选择"},
             				{value: "字符型", text: "字符型"},
             				{value: "日期型", text: "日期型"},
             				{value: "数字型", text: "数字型"}
             			]},
					{type: "combo", label: "可为空:", name: "table_column_"+index+"_isNull_"+cIndex, required: true, options:[
             				{value: "", text: "请选择"},
             				{value: "1", text: "是"},
             				{value: "0", text: "否"}
             			]},
             			{type: "newcolumn"},
					{type: "block", offsetTop:"27"},
					{type: "input", label: "长度:&nbsp;&nbsp;&nbsp;", name: "table_column_"+index+"_length_"+cIndex, maxLength:10},
					{type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "table_column_"+index+"_defaultValue_"+cIndex, maxLength:200}
				]},
				{type: "block", width: "900", list:[
					{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "table_column_"+index+"_remark_"+cIndex, maxLength:500, rows:"3", inputWidth:"743"}
				]}
			]};
			detailForm.addItem("table_column_"+index, itemData, cIndex);
			tableColumnIndex["columns_" + index] = cIndex + 1;
		}
		// 表删除列
		function removeColumn(columnsIndex, columnIndex) {
			detailForm.removeItem("table_column_"+columnsIndex+"_block_"+columnIndex);
		}
		
		
		dhxLayout = new dhtmlXLayoutObject("content", "2E");
		dhxLayout.cells("a").setText("构件包名");
		dhxLayout.cells("b").hideHeader();
		dhxLayout.cells("a").setHeight("100");
		var componentFormData = {
			format: [
		        {type: "block", width: "800", offsetTop:"20", list:[
					{type: "combo", label: "构件包名:", name: "name", maxLength:100, required: true, showAll: true},
					{type: "newcolumn"},
					{type: "block", width: "120", list:[
						{type: "button", name: "valid", value: "检验", width:100}
					]}
				]}
	        ],
			settings: {labelWidth: 120, inputWidth: 200}
		};
		var componentForm = dhxLayout.cells("a").attachForm(initDetailFormFormat(componentFormData));
		var componentNameCombo = componentForm.getCombo("name");
		componentNameCombo.clearAll(true);
		var componentNameComboUrl = MODEL_URL + "!getComponentNames.json";
		var componentNameJsonObj = loadJson(componentNameComboUrl);
		if(componentNameJsonObj) {
			var opt_data = [];
			var names = componentNameJsonObj.split(",");
			if (names && names.length>0) {
				for (var i=0; i<names.length; i++) {
					opt_data[i] = {text: names[i], value: names[i]};
				}
			}
			componentNameCombo.addOption(opt_data);
		}
		var tabbar = dhxLayout.cells("b").attachTabbar();
		componentForm.attachEvent("onButtonClick", function(id) {
			if (id == "valid") {
				if (this.validate()) {
					var name = this.getItemValue("name");
					if (name.toLowerCase() == detailForm.getItemValue("name").toLowerCase()) {
						return;
					}
					if (detailForm.getItemValue("name")) {
						detailForm.unload();
						initDetailForm();
						document.getElementById("jars").innerHTML = "";
					}
					var result = loadJson(MODEL_URL + "!existComponent.json?name=" + name);
					if (typeof result == "string") {
						result = eval("(" + result + ")");
					}
					if (result.exist) {
						detailForm.unlock();
						detailForm.setItemValue("name", name);
						configToolBar.enableItem("create");
						configToolBar.enableItem("view");
						packageToolBar.enableItem("package");
						packageToolBar.enableItem("download");
						document.getElementById("jar_plus").disabled = false;
						listJars();
						var result1 = loadJson(MODEL_URL + "!existConfigFile.json?name=" + name);
						if (typeof result1 == 'string') {
							result1 = eval("(" + result1 + ")");
						}
						if (result1.exist) {
							var configJson = loadJson(MODEL_URL + "!getComponentConfig.json?name=" + name);
							if (configJson && configJson.name) {
								detailForm.setItemValue("code", configJson.code);
								detailForm.setItemValue("name", configJson.name);
								detailForm.setItemValue("alias", configJson.alias);
								detailForm.setItemValue("version", configJson.version);
								detailForm.setItemValue("type", configJson.type);
								detailForm.setItemValue("views", configJson.views);
								detailForm.setItemValue("url", configJson.url);
								detailForm.setItemValue("remark", configJson.remark);
								if (configJson.componentSystemParameters) {
									var index = 0;
									for (var i in configJson.componentSystemParameters) {
										index++;
										if (!detailForm.isItem("system_parameter_name_"+index)) {
											addSystemParameter();
										}
										detailForm.setItemValue("system_parameter_name_"+index, configJson.componentSystemParameters[i].name);
										detailForm.setItemValue("system_parameter_remark_"+index, configJson.componentSystemParameters[i].remark);
									}
								} else {
									removeSystemParameter(1);
								}
								if (configJson.selfParams) {
									var index = 0;
									for (var i in configJson.selfParams) {
										index++;
										if (!detailForm.isItem("self_parameter_name_"+index)) {
											addSelfParam();
										}
										detailForm.setItemValue("self_parameter_name_"+index, configJson.selfParams[i].name);
										detailForm.setItemValue("self_parameter_type_"+index, configJson.selfParams[i].type);
										detailForm.setItemValue("self_parameter_value_"+index, configJson.selfParams[i].value);
										detailForm.setItemValue("self_parameter_text_"+index, configJson.selfParams[i].text);
										detailForm.setItemValue("self_parameter_remark_"+index, configJson.selfParams[i].remark);
										if (configJson.selfParams[i].type == "1") {
											var options = eval(configJson.selfParams[i].options);
											if (options) {
												var index1 = 0;
												for (var j in options) {
													if (!options[j].value) {
														continue;
													}
													index1++;
													if (!detailForm.isItem("self_parameter_option_"+index+"_text_"+index1)) {
														addSelfParamOption(index);
													}
													detailForm.setItemValue("self_parameter_option_"+index+"_value_"+index1, options[j].value);
													detailForm.setItemValue("self_parameter_option_"+index+"_text_"+index1, options[j].text);
												}
											} else {
												removeSelfParamOption(index, 1);
											}
										}
									}
								} else {
									removeSelfParam(1);
								}
								if (configJson.inputParams) {
									var index = 0;
									for (var i in configJson.inputParams) {
										index++;
										if (!detailForm.isItem("input_parameter_name_"+index)) {
											addInputParameter();
										}
										detailForm.setItemValue("input_parameter_name_"+index, configJson.inputParams[i].name);
										detailForm.setItemValue("input_parameter_value_"+index, configJson.inputParams[i].value);
										detailForm.setItemValue("input_parameter_remark_"+index, configJson.inputParams[i].remark);
									}
								} else {
									removeInputParameter(1);
								}
								if (configJson.outputParams) {
									var index = 0;
									for (var i in configJson.outputParams) {
										index++;
										if (!detailForm.isItem("output_parameter_name_"+index)) {
											addOutputParameter();
										}
										detailForm.setItemValue("output_parameter_name_"+index, configJson.outputParams[i].name);
										detailForm.setItemValue("output_parameter_value_"+index, configJson.outputParams[i].value);
										detailForm.setItemValue("output_parameter_remark_"+index, configJson.outputParams[i].remark);
									}
								} else {
									removeOutputParameter(1);
								}
								if (configJson.reserveZones) {
									var index = 0;
									for (var i in configJson.reserveZones) {
										index++;
										if (!detailForm.isItem("reserve_zone_name_"+index)) {
											addReserveZone();
										}
										detailForm.setItemValue("reserve_zone_name_"+index, configJson.reserveZones[i].name);
										detailForm.setItemValue("reserve_zone_alias_"+index, configJson.reserveZones[i].alias);
										detailForm.setItemValue("reserve_zone_type_"+index, configJson.reserveZones[i].type);
										detailForm.setItemValue("reserve_zone_page_"+index, configJson.reserveZones[i].page);
									}
								} else {
									removeReserveZone(1);
								}
								if (configJson.functions) {
									var index = 0;
									for (var i in configJson.functions) {
										index++;
										if (!detailForm.isItem("function_name_"+index)) {
											addFunction();
										}
										detailForm.setItemValue("function_name_"+index, configJson.functions[i].name);
										detailForm.setItemValue("function_page_"+index, configJson.functions[i].page);
										detailForm.setItemValue("function_remark_"+index, configJson.functions[i].remark);
										var functionDatas = configJson.functions[i].componentFunctionDataList;
										if (functionDatas) {
											var index1 = 0;
											for (var j in functionDatas) {
												index1++;
												if (!detailForm.isItem("function_return_data_"+index+"_name_"+index1)) {
													addFunctionReturnData(index);
												}
												detailForm.setItemValue("function_return_data_"+index+"_name_"+index1, functionDatas[j].name);
												detailForm.setItemValue("function_return_data_"+index+"_remark_"+index1, functionDatas[j].remark);
											}
										} else {
											removeFunctionReturnData(index, 1);
										}
									}
								} else {
									removeFunction(1);
								}
								if (configJson.callbacks) {
									var index = 0;
									for (var i in configJson.callbacks) {
										index++;
										if (!detailForm.isItem("callback_name_"+index)) {
											addCallback();
										}
										detailForm.setItemValue("callback_name_"+index, configJson.callbacks[i].name);
										detailForm.setItemValue("callback_page_"+index, configJson.callbacks[i].page);
										detailForm.setItemValue("callback_remark_"+index, configJson.callbacks[i].remark);
										var callbackParams = configJson.callbacks[i].componentCallbackParamList;
										if (callbackParams) {
											var index1 = 0;
											for (var j in callbackParams) {
												index1++;
												if (!detailForm.isItem("callback_parameter_"+index+"_name_"+index1)) {
													addCallbackParameter(index);
												}
												detailForm.setItemValue("callback_parameter_"+index+"_name_"+index1, callbackParams[j].name);
												detailForm.setItemValue("callback_parameter_"+index+"_remark_"+index1, callbackParams[j].remark);
											}
										} else {
											removeCallbackParameter(index, 1);
										}
									}
								} else {
									removeCallback(1);
								}
								if (configJson.buttons) {
									var index = 0;
									for (var i in configJson.buttons) {
										index++;
										if (!detailForm.isItem("authority_button_block_"+index)) {
											addAuthorityButton();
										}
										detailForm.setItemValue("authority_button_name_"+index, configJson.buttons[i].name);
										detailForm.setItemValue("authority_button_display_name_"+index, configJson.buttons[i].displayName);
									}
								} else {
									removeAuthorityButton(1);
								}
								if (configJson.componentTables) {
									var index = 0;
									for (var i in configJson.componentTables) {
										index++;
										if (!detailForm.isItem("table_name_"+index)) {
											addTable();
										}
										detailForm.setItemValue("table_name_"+index, configJson.componentTables[i].name);
										detailForm.checkItem("table_is_selfdefine_"+index, configJson.componentTables[i].isSelfdefine);
										detailForm.checkItem("table_release_with_data_"+index, configJson.componentTables[i].releaseWithData);
										var columns = configJson.componentTables[i].componentColumnList;
										if (columns) {
											var index1 = 0;
											for (var j in columns) {
												index1++;
												if (!detailForm.isItem("table_column_"+index+"_name_"+index1)) {
													addColumn(index);
												}
												detailForm.setItemValue("table_column_"+index+"_name_"+index1, columns[j].name);
												detailForm.setItemValue("table_column_"+index+"_type_"+index1, columns[j].type);
												detailForm.setItemValue("table_column_"+index+"_length_"+index1, columns[j].length);
												detailForm.setItemValue("table_column_"+index+"_isNull_"+index1, columns[j].isNull);
												detailForm.setItemValue("table_column_"+index+"_defaultValue_"+index1, columns[j].defaultValue);
												detailForm.setItemValue("table_column_"+index+"_remark_"+index1, columns[j].remark);
											}
										} else {
											removeColumn(index, 1);
										}
									}
								} else {
									removeTable(1);
								}
							}
						}
					} else {
						dhtmlx.message("该构件不存在！");
						detailForm.reset();
						detailForm.lock();
						configToolBar.disableItem("create");
						configToolBar.disableItem("view");
						packageToolBar.disableItem("package");
						packageToolBar.disableItem("download");
						document.getElementById("jar_plus").disabled = true;
					}
				}
			}
		});
		tabbar.setImagePath(IMAGE_PATH);
		tabbar.addTab("configForm", "配置文件", "100px");
		tabbar.addTab("package", "打包下载", "100px");
		tabbar.addTab("batchPackage", "批量打包", "100px");
		configToolBar = tabbar.cells("configForm").attachToolbar();
		configToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
	    configToolBar.addButton("create", 0, "生成配置文件", "new.gif");
	    configToolBar.addSeparator("septr$01", 1);
	    configToolBar.addButton("view", 2, "查看配置文件", "view.gif");
	    configToolBar.attachEvent('onClick', function(id) {
	        if (id == "create") {
				var formdata = detailForm.getFormData(true);
				if (detailForm.validate()) {
        			detailForm.send(MODEL_URL + "!createConfigFile.json", "post", function(loader, response){
						var result = eval("(" + response + ")");
						if (typeof result == "string") {
							result = eval("(" + result + ")");
						}
						if (result.success) {
							dhtmlx.message("创建成功！");
						} else {
							dhtmlx.message("创建失败！");
						}
	        		});
        		}
			} else if (id == "view") {
				var name = detailForm.getItemValue("name");
				var result = loadJson(MODEL_URL + "!existConfigFile.json?name=" + name);
				if (typeof result == 'string') {
					result = eval("(" + result + ")");
				}
				if (result.exist) {
					if (!dhxWins) {
				    	dhxWins = new dhtmlXWindows();
				    }
					var configFileWin = dhxWins.createWindow("configFileWin", 0, 0, 1000, 500);
				    configFileWin.setModal(true);
				    configFileWin.setText("构件配置信息");
				    configFileWin.center();
				    configFileWin.attachURL(MODEL_URL + "!previewConfigFile?name=" + name);
			    } else {
			    	dhtmlx.message("请先创建构件配置文件！");
			    }
			}
        });
        configToolBar.disableItem("create");
		configToolBar.disableItem("view");
        packageToolBar = tabbar.cells("package").attachToolbar();
		packageToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
	    packageToolBar.addButton("package", 0, "打包", "package.gif");
	    packageToolBar.addSeparator("septr$01", 1);
	    packageToolBar.addButton("download", 2, "下载", "download.gif");
	    packageToolBar.attachEvent('onClick', function(id) {
	        if (id == "package") {
				var name = detailForm.getItemValue("name");
				var views = detailForm.getItemValue("views");
				var result = loadJson(MODEL_URL + "!existConfigFile.json?name=" + name);
				if (typeof result == 'string') {
					result = eval("(" + result + ")");
				}
				if (result.exist) {
					var msg = loadJson(MODEL_URL + "!packageComponent.json?name=" + name + "&views=" + views);
					dhtmlx.message(msg);
			    } else {
			    	dhtmlx.message("请先创建构件配置文件！");
			    }
			} else if (id == "download") {
				var name = detailForm.getItemValue("name");
				var alias = detailForm.getItemValue("alias");
				var version = detailForm.getItemValue("version");
				var result = loadJson(MODEL_URL + "!existComponentPackage.json?name=" + name);
				if (typeof result == 'string') {
					result = eval("(" + result + ")");
				}
				if (result.exist) {
					download(MODEL_URL + "!downloadComponent?name=" + encodeURIComponent(name) + "&alias=" + encodeURIComponent(alias) + "&version=" + version);
			    } else {
			    	dhtmlx.message("请先打包！");
			    }
			}
        });
        packageToolBar.disableItem("package");
		packageToolBar.disableItem("download");
		tabbar.setContent("package", "jarDiv");
		document.getElementById("jarDiv").style.visibility = "visible";
		tabbar.setTabActive("configForm");
		initDetailForm();
		addSelfParamTypeChangeEvent(1);
		document.getElementById("jar_plus").disabled = true;
		batchPackageToolBar = tabbar.cells("batchPackage").attachToolbar();
		batchPackageToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
	    batchPackageToolBar.addButton("package", 0, "批量打包", "package.gif");
	    batchPackageToolBar.addSeparator("septr$01", 1);
	    batchPackageToolBar.addButton("download", 2, "下载", "download.gif");
	    batchPackageToolBar.addSeparator("septr$01", 3);
	    batchPackageToolBar.addButton("refresh", 4, "扫描配置文件", "refresh.gif");
	    batchPackageToolBar.attachEvent('onClick', function(id) {
	        if (id == "package") {
	        	var componentConfigFileNames;
	        	var selectIds = batchPackageGrid.getSelectedRowId();
	        	if (selectIds) {
	        		componentConfigFileNames = selectIds;
	        	} else {
	        		var allRowIds = batchPackageGrid.getAllRowIds();
	        		if (allRowIds) {
	        			componentConfigFileNames = allRowIds;
	        		} else {
	        			dhtmlx.message("没有配置文件！");
	        		}
	        	}
	        	var result = loadJson(MODEL_URL + "!batchPackage.json?componentConfigFileNames=" + componentConfigFileNames);
				dhtmlx.alert(result);
			} else if (id == "download") {
				var result = loadJson(MODEL_URL + "!existBatchComponent.json");
				if (typeof result == 'string') {
					result = eval("(" + result + ")");
				}
				if (result.exist) {
					download(MODEL_URL + "!downloadBatchComponent");
			    } else {
			    	dhtmlx.message("请先打包！");
			    }
			} else if (id == "refresh") {
				loadBatchGridData(batchPackageGrid, MODEL_URL + "!loadBatchPackageGrid.json?E_model_name=datagrid");
			}
        });
	    var batchPackageGridData = {
	    		format: {
	    			headers: ["<center>配置文件名称</center>"],
	    			colWidths: ["400"],
	    			colTypes: ["ro"],
	    			colAligns: ["left"]
	    		}
	    	};
	    function initBatchGrid(grid, gridcfg) {
	        grid.setImagePath(IMAGE_PATH);
	        grid.setHeader(gridcfg.format.headers.toString());
	        grid.setInitWidths(gridcfg.format.colWidths.toString());
	        grid.setColTypes(gridcfg.format.colTypes.toString());
	        grid.setColAlign(gridcfg.format.colAligns.toString());
	        grid.setSkin(Skin);
	        grid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
	        grid.init();
	        grid.enableMultiselect(true);
	        grid.enableDragAndDrop(true);
	    }
	    function loadBatchGridData(grid, url) {
	        grid.clearAll();
	        var dataJson = loadJson(url);
	        var jsonArray = dataJson.data ? dataJson.data : dataJson;
	        var datas = {};
	        datas.rows = [];
	        for (var i = 0; i < jsonArray.length; i++) {
	            var row = {};
	            row.id = jsonArray[i][0];
	            row.data = [];
	            for (var j = 1; j < jsonArray[i].length; j++) {
	                row.data[j - 1] = jsonArray[i][j];
	            }
	            datas.rows[i] = row;
	        }
	        grid.parse(datas, "json");
	    }
	    batchPackageGrid = tabbar.cells("batchPackage").attachGrid();
	    initBatchGrid(batchPackageGrid, batchPackageGridData);
	    loadBatchGridData(batchPackageGrid, MODEL_URL + "!loadBatchPackageGrid.json?E_model_name=datagrid");
		function initDetailForm() {
			detailForm = tabbar.cells("configForm").attachForm(initDetailFormFormat(detailFormData));
			detailForm.lock();
			var type = detailForm.getCombo("type");
			type.attachEvent("onChange", function() {
				if ("0" == this.getActualValue()) {
					// 公用构件
					for (var i=1; i<outputParameterIndex; i++) {
						if (detailForm.isItem("output_parameter_block_" + i)) {
							detailForm.removeItem("output_parameter_block_" + i);
						}
					}
					for (var i=1; i<authorityButtonIndex; i++) {
						if (detailForm.isItem("authority_button_block_" + i)) {
							detailForm.removeItem("authority_button_block_" + i);
						}
					}
					document.getElementById("output_parameter_plus").disabled = true;
					document.getElementById("reserve_zone_plus").disabled = true;
					document.getElementById("function_plus").disabled = true;
					document.getElementById("callback_plus").disabled = true;
					document.getElementById("authority_button_plus").disabled = true;
				} else if ("1" == this.getActualValue()) {
					// 页面构件
					document.getElementById("output_parameter_plus").disabled = false;
					document.getElementById("reserve_zone_plus").disabled = false;
					document.getElementById("function_plus").disabled = false;
					document.getElementById("callback_plus").disabled = false;
					document.getElementById("authority_button_plus").disabled = false;
				} else if ("2" == this.getActualValue()) {
					// 逻辑构件没有预留区、方法和回调函数
					for (var i=1; i<reserveZoneIndex; i++) {
						if (detailForm.isItem("reserve_zone_block_" + i)) {
							detailForm.removeItem("reserve_zone_block_" + i);
						}
					}
					for (var i=1; i<functionIndex; i++) {
						if (detailForm.isItem("function_block_" + i)) {
							detailForm.removeItem("function_block_" + i);
						}
					}
					for (var i=1; i<callbackIndex; i++) {
						if (detailForm.isItem("callback_block_" + i)) {
							detailForm.removeItem("callback_block_" + i);
						}
					}
					document.getElementById("output_parameter_plus").disabled = false;
					document.getElementById("reserve_zone_plus").disabled = true;
					document.getElementById("function_plus").disabled = true;
					document.getElementById("callback_plus").disabled = true;
					document.getElementById("authority_button_plus").disabled = true;
				}
			});
			systemParameterIndex = 2;
			selfParamIndex = 2;
			selfParamOptionIndex = {};
			inputParameterIndex = 2;
			outputParameterIndex = 2;
			reserveZoneIndex = 2;
			functionIndex = 2;
			functionReturnDataIndex = {};
			functionReturnDataIndex.returnDatas_1 = 2;
			callbackIndex = 2;
			callbackParameterIndex = {};
			callbackParameterIndex.parameters_1 = 2;
			tableIndex = 2;
			tableColumnIndex = {};
			tableColumnIndex.columns_1 = 2;
		}
		function addJars() {
			var name = detailForm.getItemValue("name");
			GWIN_WIDTH = 450;
			GWIN_HEIGHT = 280;
			if (!dhxWins) {
		    	dhxWins = new dhtmlXWindows();
		    }
		    dataWin = dhxWins.createWindow(WIN_ID, 0, 0, GWIN_WIDTH, GWIN_HEIGHT);
		    dataWin.setModal(true);
		    dataWin.setText("上传JAR");
		    dataWin.center();
		    dataWin.button('park').hide();
		    dataWin.button('minmax1').hide();
		    dataWin.denyResize();
		    var vaultDiv = document.createElement("div");
		    vaultDiv.setAttribute("id", "vaultDiv");
		    document.body.appendChild(vaultDiv);
		    var UPLOAD_URL = MODEL_URL + "!uploadHandler";
		    var GET_INFO_URL = MODEL_URL + "!getInfoHandler";
		    var GET_ID_URL = MODEL_URL + "!getIdHandler";
        	var vault = new dhtmlXVaultObject();
            vault.setImagePath(DHX_RES_PATH + "/common/css/imgs/");
            vault.setServerHandlers(UPLOAD_URL, GET_INFO_URL, GET_ID_URL);
            vault.strings.btnAdd = "添加";
			vault.strings.btnUpload = "上传";
			vault.strings.btnClean = "清空";
			vault.strings.remove = "删除";
			vault.strings.done = "完成";
			vault.strings.error = "错误"; 
            vault.onAddFile = function(fileName) { 
                var shortFileName = this.getFileName(fileName);
                for (var i in this.fileList) {
                    if (this.fileList[i].name && this.getFileName(this.fileList[i].name)==shortFileName) {
                        dhtmlx.message("该文件已经添加过！");
                        return false;
                    }
                }
				var ext = this.getFileExtension(fileName); 
				if (ext != "jar" && ext != "dll" && ext != "ocx") {
					dhtmlx.message("只能上传jar、dll、ocx文件！");
					return false;
				} else {
					return true;
				}
			};
			vault.onUploadComplete = function(files) { 
				dhtmlx.message("上传成功！");
				dataWin.close();
				listJars();
			};
            vault.create("vaultDiv");
            vault.setFormField("name", name);
		    dataWin.attachObject(vaultDiv);
		}
		function listJars() {
			var name = detailForm.getItemValue("name");
			var result = loadJson(MODEL_URL + "!listJars.json?name=" + name);
			var jarsStr = "";
			var jars = result.split(",");
			if (jars && jars.length>0) {
				for (var i in jars) {
					if (jars[i] != "") {
						jarsStr += jars[i] + "<img align='absmiddle' src='<%=dhxResPath %>/common/images/reset.gif' onclick='removeJar(\"" + jars[i] + "\")'>&nbsp;&nbsp;";
					} 
				}
			}
			document.getElementById("jars").innerHTML = jarsStr;
		}
		function removeJar(jarName) {
			var name = detailForm.getItemValue("name");
			var msg = loadJson(MODEL_URL + "!removeJar.json?name=" + name + "&jarName=" + encodeURIComponent(jarName));
			dhtmlx.message(msg);
			listJars();
		}
	</script>
  </body>
</html>
